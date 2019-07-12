# Open Hospital [![Build Status](https://travis-ci.org/informatici/openhospital.svg?branch=master)](https://travis-ci.org/informatici/openhospital)

This is the *root* project of the [Open Hospital][openhospital] code base.  
[Here][releases] you can download the portable distribution of Open Hospital.

## How to create Open Hospital portable

After git-cloning this project, to initialize the submodules, issue:

    git submodule init
    git submodule update

Alternatively, you can git-clone all modules recursively:

    git clone --recurse-submodules https://github.com/informatici/openhospital

After that, you can compile the project by issuing:

    mvn package

Finally, executing `build_poh.sh` should create the two zip files
of the portable distribution for Linux and Windows.


 [openhospital]: https://www.open-hospital.org/
 [releases]: https://github.com/informatici/openhospital/releases
