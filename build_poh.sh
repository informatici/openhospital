#!/bin/bash
# This script assembles the portable distributions of Open Hospital.

command_exists () {
    type "$1" &> /dev/null ;
}

requirements="java mvn docker-compose mysql zip"
show_req () {
	echo `tput smul`$1' not found'`tput sgr0`
	echo ''
	echo 'Make sure to have installed the following dependencies on a Linux machine:'
	echo 'JDK 6+, Maven, asciidoctor-pdf, docker-compose, MySQL client, zip'
	exit 1
}

show_doc () {
	echo '*********************************'
	echo 'asciidoctor-pdf is not installed.'
	echo '*********************************'
	echo '==> Admin Manual at https://github.com/informatici/openhospital-doc/blob/master/doc_admin/AdminManual.adoc'
	echo '==> User Manual at https://github.com/informatici/openhospital-doc/blob/master/doc_user/UserManual.adoc'
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
poh_linux_version="0.0.3"

# get the Open Hospital version
test -f ./gui/rsc/version.properties
version_file="./gui/rsc/version.properties"
major=$(grep VER_MAJOR $version_file | cut -d"=" -f2)
minor=$(grep VER_MINOR $version_file | cut -d"=" -f2)
release=$(grep VER_RELEASE $version_file | cut -d"=" -f2)
version="$major.$minor.$release"

# convert documentation
if command_exists asciidoctor-pdf; then
	asciidoctor-pdf ./doc/doc_admin/AdminManual.adoc -o AdminManual.pdf
	asciidoctor-pdf ./doc/doc_user/UserManual.adoc -o UserManual.pdf
fi

# estracting changelogs
cp core/doc/`ls core/doc/ -r | head -n 1` core_`ls core/doc/ -r | head -n 1`
cp gui/doc/`ls gui/doc/ -r | head -n 1` gui_`ls gui/doc/ -r | head -n 1`

# assemble OH Windows portable
mkdir -p ./poh-win32-$poh_win32_version-core-$version/oh/doc
cp -rf ./poh-bundle-win/* ./poh-win32-$poh_win32_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-win32-$poh_win32_version-core-$version/oh
cp *.sql ./poh-win32-$poh_win32_version-core-$version
cp *.txt ./poh-win32-$poh_win32_version-core-$version/oh/doc

# assemble OH Linux portable
mkdir -p ./poh-linux-$poh_linux_version-core-$version/oh/doc
cp -rf ./poh-bundle-linux/* ./poh-linux-$poh_linux_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-linux-$poh_linux_version-core-$version/oh
cp *.sql ./poh-linux-$poh_linux_version-core-$version
cp *.txt ./poh-linux-$poh_linux_version-core-$version/oh/doc

# check documentation
if command_exists asciidoctor-pdf; then
	cp *.pdf ./poh-win32-$poh_win32_version-core-$version/oh/doc
	cp *.pdf ./poh-linux-$poh_linux_version-core-$version/oh/doc
else show_doc;
fi

# packaging
zip -r poh-win32-$poh_win32_version-core-$version.zip poh-win32-$poh_win32_version-core-$version
tar -cvzf poh-linux-$poh_linux_version-core-$version.tar.gz poh-linux-$poh_linux_version-core-$version

# check
ls

echo "Portable distributions of Open Hospital created successfully."
