#!/bin/bash
# This script assembles the portable distributions of Open Hospital.

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

# converting documentation
asciidoctor-pdf ./doc/doc_admin/AdminManual.adoc -o AdminManual.pdf
asciidoctor-pdf ./doc/doc_user/UserManual.adoc -o UserManual.pdf

# assembling windows portable
mkdir -p ./poh-win32-$poh_win32_version-core-$version/oh/doc
cp -rf ./poh-bundle-win/* ./poh-win32-$poh_win32_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-win32-$poh_win32_version-core-$version/oh
cp *.sql ./poh-win32-$poh_win32_version-core-$version
cp *.pdf ./poh-win32-$poh_win32_version-core-$version/oh/doc

# assembling linux portable 
mkdir -p ./poh-linux-$poh_linux_version-core-$version/oh/doc
cp -rf ./poh-bundle-linux/* ./poh-linux-$poh_linux_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-linux-$poh_linux_version-core-$version/oh
cp *.sql ./poh-linux-$poh_linux_version-core-$version
cp *.pdf ./poh-linux-$poh_linux_version-core-$version/oh/doc

# packaging
zip -r poh-win32-$poh_win32_version-core-$version.zip poh-win32-$poh_win32_version-core-$version
tar -cvzf poh-linux-$poh_linux_version-core-$version.tar.gz poh-linux-$poh_linux_version-core-$version

# check
ls

echo "Portable distributions of Open Hospital created successfully."
