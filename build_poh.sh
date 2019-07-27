#!/bin/bash
# This script assembles the portable distributions of Open Hospital.

set -e

# set the portable distribution version
poh_version="0.0.5"

# get the Open Hospital version
test -f ./gui/rsc/version.properties
version_file="./gui/rsc/version.properties"
major=$(grep VER_MAJOR $version_file | cut -d"=" -f2)
minor=$(grep VER_MINOR $version_file | cut -d"=" -f2)
release=$(grep VER_RELEASE $version_file | cut -d"=" -f2)
version="$major.$minor.$release"

mkdir -p ./poh-win32-$poh_version-core-$version/oh
cp -rf ./poh-bundle-win/* ./poh-win32-$poh_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-win32-$poh_version-core-$version/oh
cp *.sql ./poh-win32-$poh_version-core-$version

mkdir -p ./poh-linux-$poh_version-core-$version/oh
cp -rf ./poh-bundle-linux/* ./poh-linux-$poh_version-core-$version
cp -rf ./gui/target/OpenHospital20/* ./poh-linux-$poh_version-core-$version/oh
cp *.sql ./poh-linux-$poh_version-core-$version

zip -r poh-win32-$poh_version-core-$version.zip poh-win32-$poh_version-core-$version

tar -cvzf poh-linux-$poh_version-core-$version.tar.gz poh-linux-$poh_version-core-$version

echo "Portable distributions of Open Hospital created successfully."
