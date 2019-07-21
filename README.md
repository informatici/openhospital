# Open Hospital [![Build Status](https://travis-ci.org/informatici/openhospital.svg?branch=master)](https://travis-ci.org/informatici/openhospital)

This is the *root* project of the [Open Hospital][openhospital] code base.  
[Here][releases] you can download the portable distribution of Open Hospital.

## How to create Open Hospital portable

These are the steps to create the portable distributions of Open Hospital:

 1. Clone this repository and initialize the submodules:

        git clone --recurse-submodules https://github.com/informatici/openhospital

 2. Compile the projects *core* and *gui* by issuing:

        mvn package -DskipTests

 3. Assemble the portable distributions for Windows and for Linux:

        ./build_poh.sh


 [openhospital]: https://www.open-hospital.org/
 [releases]: https://github.com/informatici/openhospital/releases
