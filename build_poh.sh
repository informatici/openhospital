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

# compile core and gui projects
docker-compose -f core/docker-compose.yml up -d
mvn package

# dump the database to a SQL script
mysqldump --protocol tcp -h localhost -u isf -pisf123 --compatible=mysql40 oh > database.sql

# convert documentation
asciidoctor-pdf ./doc/doc_admin/AdminManual.adoc -o AdminManual.pdf
asciidoctor-pdf ./doc/doc_user/UserManual.adoc -o UserManual.pdf

# extract changelogs
cp core/doc/`ls core/doc/ -r | head -n 1` core_`ls core/doc/ -r | head -n 1`
cp gui/doc/`ls gui/doc/ -r | head -n 1` gui_`ls gui/doc/ -r | head -n 1`

# assemble OH Windows portable
mkdir -p ./poh-win32-$poh_win32_version-core-$version/oh/doc
cp -rf ./poh-bundle-win/* ./poh-win32-$poh_win32_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-win32-$poh_win32_version-core-$version/oh
cp *.sql ./poh-win32-$poh_win32_version-core-$version
cp *.pdf ./poh-win32-$poh_win32_version-core-$version/oh/doc
cp *.txt ./poh-win32-$poh_win32_version-core-$version/oh/doc

# assemble OH Linux portable
mkdir -p ./poh-linux-$poh_linux_version-core-$version/oh/doc
cp -rf ./poh-bundle-linux/* ./poh-linux-$poh_linux_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-linux-$poh_linux_version-core-$version/oh
cp *.sql ./poh-linux-$poh_linux_version-core-$version
cp *.pdf ./poh-linux-$poh_linux_version-core-$version/oh/doc
cp *.txt ./poh-linux-$poh_linux_version-core-$version/oh/doc

# package
zip -r poh-win32-$poh_win32_version-core-$version.zip poh-win32-$poh_win32_version-core-$version
tar -cvzf poh-linux-$poh_linux_version-core-$version.tar.gz poh-linux-$poh_linux_version-core-$version

# check
ls

# cleanup
docker-compose -f core/docker-compose.yml down

echo "Portable distributions of Open Hospital created successfully."
