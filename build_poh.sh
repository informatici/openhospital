#!/bin/bash

set -e

if [ ! -d ./poh-bundle-win/poh ]; then
    mkdir ./poh-bundle-win/poh
fi
cp -r ./gui/target/OpenHospital20/* ./poh-bundle-win/poh

# get the version
version_file="./gui/rsc/version.properties"
major=$(grep VER_MAJOR $version_file | cut -d"=" -f2)
minor=$(grep VER_MINOR $version_file | cut -d"=" -f2)
release=$(grep VER_RELEASE $version_file | cut -d"=" -f2)
version="$major.$minor.$release"

poh_version="0.0.5"

cd ./poh-bundle-win
zip -r poh-win32-$poh_version-$version.zip *
cd ..

