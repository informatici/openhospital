#!/bin/bash
# This script assembles the portable distributions of Open Hospital.

command_exists () { type "$1" &> /dev/null ; }

requirements="java mvn docker-compose mysql zip tar"
show_req () {
    echo `tput smul`$1' not found'`tput sgr0`
    echo ''
    echo 'Make sure to have installed the following dependencies on a Linux machine:'
    echo 'JDK 6+, Maven, asciidoctor-pdf, docker-compose, MySQL client, zip'
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

set -e

# set the portable distribution version
poh_win32_version="0.0.5"
poh_linux_version="0.0.5"

# get the Open Hospital version
version_file=./gui/rsc/version.properties
test -f $version_file
major=$(grep VER_MAJOR $version_file | cut -d"=" -f2)
minor=$(grep VER_MINOR $version_file | cut -d"=" -f2)
release=$(grep VER_RELEASE $version_file | cut -d"=" -f2)
version="$major.$minor.$release-beta2"

# compile core and gui projects
docker-compose -f core/docker-compose.yml up -d
mvn package

# dump the database to a SQL script
mysqldump --protocol tcp -h localhost -u isf -pisf123 --compatible=mysql40 oh > database.sql

# create distribution folders
WIN_DIR="./poh-win32-$poh_win32_version-$version"
LINUX32_DIR="./poh-linux-x32-$poh_linux_version-$version"
LINUX64_DIR="./poh-linux-x64-$poh_linux_version-$version"
mkdir -p $WIN_DIR/oh/doc
mkdir -p $LINUX32_DIR/oh/doc
mkdir -p $LINUX64_DIR/oh/doc

# compile and assemble documentation
if command_exists asciidoctor-pdf; then
    asciidoctor-pdf ./doc/doc_admin/AdminManual.adoc -o AdminManual.pdf
    asciidoctor-pdf ./doc/doc_user/UserManual.adoc -o UserManual.pdf
    cp *.pdf $WIN_DIR/oh/doc
    cp *.pdf $LINUX32_DIR/oh/doc
    cp *.pdf $LINUX64_DIR/oh/doc
else show_doc;
fi

# extract changelogs
cp core/doc/`ls core/doc/ -r | head -n 1` core_`ls core/doc/ -r | head -n 1`
cp gui/doc/`ls gui/doc/ -r | head -n 1` gui_`ls gui/doc/ -r | head -n 1`

# assemble OH Windows portable
cp -rf ./poh-bundle-win/* $WIN_DIR
cp -rf ./gui/target/OpenHospital20/* $WIN_DIR/oh
cp *.sql $WIN_DIR
cp *.txt $WIN_DIR/oh/doc
cp POH-README.md $WIN_DIR
cp POH-win-changelog.md $WIN_DIR
cp LICENSE $WIN_DIR

# assemble OH Linux x32 portable
cp -rf ./poh-bundle-linux-x32/* $LINUX32_DIR
cp -rf ./gui/target/OpenHospital20/* $LINUX32_DIR/oh
cp *.sql $LINUX32_DIR
cp *.txt $LINUX32_DIR/oh/doc
cp POH-README.md $LINUX32_DIR
cp POH-linux-changelog.md $LINUX32_DIR
cp LICENSE $LINUX32_DIR

# assemble OH Linux x64 portable
cp -rf ./poh-bundle-linux-x64/* $LINUX64_DIR
cp -rf ./gui/target/OpenHospital20/* $LINUX64_DIR/oh
cp *.sql $LINUX64_DIR
cp *.txt $LINUX64_DIR/oh/doc
cp POH-README.md $LINUX64_DIR
cp POH-linux-changelog.md $LINUX64_DIR
cp LICENSE $LINUX64_DIR

# package
zip -r $WIN_DIR.zip $WIN_DIR
tar -cvzf $LINUX32_DIR.tar.gz $LINUX32_DIR
tar -cvzf $LINUX64_DIR.tar.gz $LINUX64_DIR

# check
ls

echo "Portable distributions of Open Hospital created successfully."
