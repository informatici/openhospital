#!/bin/bash
# This script assembles the portable distributions of Open Hospital.

set -e

command_exists () { type "$1" &> /dev/null ; }

requirements="java mvn git docker docker-compose zip tar wget xargs"
show_req () {
    echo `tput smul`$1' not found'`tput sgr0`
    echo ''
    echo 'Make sure to have installed the following dependencies on a Linux machine:'
    echo 'JDK 8+, Maven, asciidoctor-pdf, docker, docker-compose, zip'
    exit 1
}

show_doc () {
    echo '******************************************'
    echo 'WARNING: asciidoctor-pdf is not installed.'
    echo '******************************************'
    echo 'The documentation will not be included in the distribution.'
    echo 'You can find the manuals online at these URLs:'
    echo '==> Admin manual: https://github.com/informatici/openhospital-doc/blob/master/doc_admin/AdminManual.adoc'
    echo '==> User manual: https://github.com/informatici/openhospital-doc/blob/master/doc_user/UserManual.adoc'
}

for arg in $requirements
do
    if ! command_exists $arg; then
        show_req $arg
    fi
done

# get Open Hospital version from git describe or OH_VERSION env variable
version="${OH_VERSION:-$(git describe --abbrev=0 --tags)}" 
echo "Building Open Hospital version: $version"

# clone core, gui and doc repositories
rm -rf core gui doc 
git clone -b $version https://github.com/informatici/openhospital-core.git core
git clone -b $version https://github.com/informatici/openhospital-gui.git gui
git clone -b $version https://github.com/informatici/openhospital-doc.git doc

# set the portable distribution version
poh_win32_version="0.0.6"
poh_linux_version="0.0.6"

# generate changelog from previous tag
pushd core
lasttag=$(git tag --sort=-committerdate | head -1)
secondlasttag=$(git tag --sort=-committerdate | head -2 | tail -n 1)
popd

cp CHANGELOG_TEMPLATE.md CHANGELOG.md
sed -i "s/VERSION/$version/g" CHANGELOG.md
sed -i "s/SECONDLASTTAG/${secondlasttag//$'\n'/\\n}/g" CHANGELOG.md
sed -i "s/LASTTAG/${lasttag//$'\n'/\\n}/g" CHANGELOG.md

head --lines=-4 CHANGELOG.md > CHANGELOG

# dump the database to a SQL script
docker-compose -f core/docker-compose.yml up -d
echo -n "Waiting for MySQL to start."
until docker exec -i core_database_1 mysqldump --protocol tcp -h localhost -u isf -pisf123 --no-tablespaces oh > database.sql 2>dump_error.log
do
  echo -n "."; sleep 2
done
echo
if grep Error dump_error.log; then exit 1; fi

# build and test the code
mvn -T 1.5C package
docker-compose -f core/docker-compose.yml down

# create distribution folders
FULL_DIR="./OpenHospital-$version"
WIN_DIR="./poh-win32-$poh_win32_version-core-$version"
LINUX32_DIR="./poh-linux-x32-$poh_linux_version-core-$version"
LINUX64_DIR="./poh-linux-x64-$poh_linux_version-core-$version"
mkdir -p $FULL_DIR/doc
mkdir -p $FULL_DIR/mysql
mkdir -p $WIN_DIR/oh/doc
mkdir -p $LINUX32_DIR/oh/doc
mkdir -p $LINUX64_DIR/oh/doc

# compile and assemble documentation
if command_exists asciidoctor-pdf; then
    echo 'Compile documentation...'
    asciidoctor-pdf ./doc/doc_admin/AdminManual.adoc -o AdminManual.pdf
    asciidoctor-pdf ./doc/doc_user/UserManual.adoc -o UserManual.pdf
    cp *.pdf $FULL_DIR/doc
    cp *.pdf $WIN_DIR/oh/doc
    cp *.pdf $LINUX32_DIR/oh/doc
    cp *.pdf $LINUX64_DIR/oh/doc
else show_doc;
fi

# echo 'Extract changelogs...'
# cd core && ./changelog.sh && cd ..
# cd gui && ./changelog.sh && cd ..
# cp core/doc/`ls core/doc/ -r | head -n 1` core_`ls core/doc/ -r | head -n 1`
# cp gui/doc/`ls gui/doc/ -r | head -n 1` gui_`ls gui/doc/ -r | head -n 1`

DOWNLOAD_DIR="/tmp/oh-downloads"
mkdir -p $DOWNLOAD_DIR
download_jre_mysql() {
    pushd $DOWNLOAD_DIR
    URL_LIST=(
        "https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u252-b09.1/OpenJDK8U-jre_x86-32_windows_hotspot_8u252b09.zip"
        "https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u252-b09/OpenJDK8U-jre_x64_linux_hotspot_8u252b09.tar.gz"
        "https://cdn.azul.com/zulu/bin/zulu8.46.0.19-ca-jre8.0.252-linux_i686.tar.gz"
        "https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.30-win32.zip"
        "https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.30-linux-glibc2.12-x86_64.tar.gz"
        "https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.30-linux-glibc2.12-i686.tar.gz")
    echo "${URL_LIST[@]}" | xargs -n 1 -P 6 wget -q -nc
    popd
}

echo 'Assemble OpenHospital (full)...'
cp -rf ./gui/target/OpenHospital20/* $FULL_DIR
cp -rf ./core/mysql/db/* $FULL_DIR/mysql
rm -rf $FULL_DIR/generate_changelog.sh
# cp *.txt $FULL_DIR/doc
cp LICENSE $FULL_DIR
cp CHANGELOG $FULL_DIR

echo 'Download MySQL and JRE binaries...'
download_jre_mysql

echo 'Assemble OH Windows portable...'
cp -rf ./poh-bundle-win/* $WIN_DIR
unzip $DOWNLOAD_DIR/OpenJDK8U-jre_x86-32_windows_hotspot_8u252b09.zip -d $WIN_DIR
unzip $DOWNLOAD_DIR/mysql-5.7.30-win32.zip -d $WIN_DIR
cp -rf ./gui/target/OpenHospital20/* $WIN_DIR/oh
rm -rf $WIN_DIR/oh/generate_changelog.sh
cp *.sql $WIN_DIR
# cp *.txt $WIN_DIR/oh/doc
cp POH-README.md $WIN_DIR
cp POH-win-changelog.md $WIN_DIR
cp LICENSE $WIN_DIR
cp CHANGELOG $WIN_DIR

echo 'Assemble OH Linux x32 portable...'
cp -rf ./poh-bundle-linux-x32/* $LINUX32_DIR
tar xz -C $LINUX32_DIR -f $DOWNLOAD_DIR/zulu8.46.0.19-ca-jre8.0.252-linux_i686.tar.gz
tar xz -C $LINUX32_DIR -f $DOWNLOAD_DIR/mysql-5.7.30-linux-glibc2.12-i686.tar.gz
cp -rf ./gui/target/OpenHospital20/* $LINUX32_DIR/oh
rm -rf $LINUX32_DIR/oh/generate_changelog.sh
cp *.sql $LINUX32_DIR
# cp *.txt $LINUX32_DIR/oh/doc
cp POH-README.md $LINUX32_DIR
cp POH-linux-changelog.md $LINUX32_DIR
cp LICENSE $LINUX32_DIR
cp CHANGELOG $LINUX32_DIR

echo 'Assemble OH Linux x64 portable...'
cp -rf ./poh-bundle-linux-x64/* $LINUX64_DIR
tar xz -C $LINUX64_DIR -f $DOWNLOAD_DIR/OpenJDK8U-jre_x64_linux_hotspot_8u252b09.tar.gz
tar xz -C $LINUX64_DIR -f $DOWNLOAD_DIR/mysql-5.7.30-linux-glibc2.12-x86_64.tar.gz
cp -rf ./gui/target/OpenHospital20/* $LINUX64_DIR/oh
rm -rf $LINUX64_DIR/oh/generate_changelog.sh
cp *.sql $LINUX64_DIR
# cp *.txt $LINUX64_DIR/oh/doc
cp POH-README.md $LINUX64_DIR
cp POH-linux-changelog.md $LINUX64_DIR
cp LICENSE $LINUX64_DIR
cp CHANGELOG $LINUX64_DIR

echo 'Package...'
zip -r $FULL_DIR.zip $FULL_DIR
zip -r $WIN_DIR.zip $WIN_DIR
tar -cvzf $LINUX32_DIR.tar.gz $LINUX32_DIR
tar -cvzf $LINUX64_DIR.tar.gz $LINUX64_DIR

echo 'Compute SHA256 checksum...'
checksum=$(sha256sum *.{zip,gz})
echo $checksum
checksum=${checksum//$'\n'/\\n}
sed -i "s/CHECKSUM/$checksum/g" CHANGELOG.md

mkdir -p release-files
mv *.zip *.tar.gz release-files/
ls release-files

# clean up
rm -rf $FULL_DIR $WIN_DIR $LINUX32_DIR $LINUX64_DIR *.sql *.txt

echo "Full and portable distributions of Open Hospital created successfully."
