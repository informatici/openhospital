#!/bin/bash
# This script assembles the portable distributions of Open Hospital.

set -e

command_exists () { type "$1" &> /dev/null ; }

requirements="java mvn git docker-compose mysql zip tar"
show_req () {
    echo `tput smul`$1' not found'`tput sgr0`
    echo ''
    echo 'Make sure to have installed the following dependencies on a Linux machine:'
    echo 'JDK 8+, Maven, asciidoctor-pdf, docker-compose, MySQL client, zip'
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

# get the Open Hospital version from git describe
version=$(git describe --abbrev=0)

# clone core, gui and doc repositories
git clone -b $version https://github.com/informatici/openhospital-core.git core
git clone -b $version https://github.com/informatici/openhospital-gui.git gui
git clone -b $version https://github.com/informatici/openhospital-doc.git doc

# set the portable distribution version
poh_win32_version="0.0.6"
poh_linux_version="0.0.6"

# generate changelog from previous tag
cd core
last_tag=$(git tag --sort=-committerdate | head -1)
second_last_tag=$(git tag --sort=-committerdate | head -2 | tail -n 1)
changelogcore=$(git log --no-merges --pretty=format:%s $last_tag..$second_last_tag | grep -i "^OP" | sed 's/^/ - /')
cd ..; cd gui
last_tag=$(git tag --sort=-committerdate | head -1)
second_last_tag=$(git tag --sort=-committerdate | head -2 | tail -n 1)
changeloggui=$(git log --no-merges --pretty=format:%s $last_tag..$second_last_tag | grep -i "^OP" | sed 's/^/ - /')
cd ..

cp CHANGELOG_TEMPLATE.md CHANGELOG.md
sed -i "s/VERSION/$version/g" CHANGELOG.md
sed -i "s/CHANGELOGCORE/${changelogcore//$'\n'/\\n}/g" CHANGELOG.md
sed -i "s/CHANGELOGGUI/${changeloggui//$'\n'/\\n}/g" CHANGELOG.md

head --lines=-4 CHANGELOG.md > CHANGELOG

# compile core and gui projects
docker-compose -f core/docker-compose.yml up -d

# dump the database to a SQL script
until mysqldump --protocol tcp -h localhost -u isf -pisf123 --compatible=mysql40 oh > database.sql 2>dump_error.log
do
  echo "Waiting docker..."
  sleep 5
done
cat dump_error.log

# build and test the code
mvn package

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

echo 'Assemble OpenHospital (full)...'
cp -rf ./gui/target/OpenHospital20/* $FULL_DIR
cp -rf ./core/mysql/db/* $FULL_DIR/mysql
rm $FULL_DIR/generate_changelog.sh || true
# cp *.txt $FULL_DIR/doc
cp LICENSE $FULL_DIR
cp CHANGELOG $FULL_DIR

echo 'Assemble OH Windows portable...'
cp -rf ./poh-bundle-win/* $WIN_DIR
curl -L https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u252-b09.1/OpenJDK8U-jre_x86-32_windows_hotspot_8u252b09.zip > win-java.zip
unzip win-java.zip -d $WIN_DIR
rm win-java.zip
cp -rf ./gui/target/OpenHospital20/* $WIN_DIR/oh
rm $WIN_DIR/oh/generate_changelog.sh || true
cp *.sql $WIN_DIR
# cp *.txt $WIN_DIR/oh/doc
cp POH-README.md $WIN_DIR
cp POH-win-changelog.md $WIN_DIR
cp LICENSE $WIN_DIR
cp CHANGELOG $WIN_DIR

echo 'Assemble OH Linux x32 portable...'
cp -rf ./poh-bundle-linux-x32/* $LINUX32_DIR
curl -L https://cdn.azul.com/zulu/bin/zulu8.46.0.19-ca-jre8.0.252-linux_i686.tar.gz | tar xz -C $LINUX32_DIR
cp -rf ./gui/target/OpenHospital20/* $LINUX32_DIR/oh
rm $LINUX32_DIR/oh/generate_changelog.sh || true
cp *.sql $LINUX32_DIR
# cp *.txt $LINUX32_DIR/oh/doc
cp POH-README.md $LINUX32_DIR
cp POH-linux-changelog.md $LINUX32_DIR
cp LICENSE $LINUX32_DIR
cp CHANGELOG $LINUX32_DIR

echo 'Assemble OH Linux x64 portable...'
cp -rf ./poh-bundle-linux-x64/* $LINUX64_DIR
curl -L https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u252-b09/OpenJDK8U-jre_x64_linux_hotspot_8u252b09.tar.gz | tar xz -C $LINUX64_DIR
cp -rf ./gui/target/OpenHospital20/* $LINUX64_DIR/oh
rm $LINUX64_DIR/oh/generate_changelog.sh || true
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
mkdir release-files
cp *.zip *.tar.gz release-files/

# check
ls release-files/

echo 'Compute SHA256 checksum...'
checksum=$(sha256sum *.zip *.gz)
checksum=${checksum//$'\n'/\\n}
echo $checksum
sed -i "s/CHECKSUM/$checksum/g" CHANGELOG.md

# clean up
rm -rf $FULL_DIR $WIN_DIR $LINUX32_DIR $LINUX64_DIR *.sql *.txt

echo "Full and portable distributions of Open Hospital created successfully."
