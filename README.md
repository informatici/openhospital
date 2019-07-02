# Open Hospital [![Build Status](https://travis-ci.org/pviotti/openhospital.svg?branch=master)](https://travis-ci.org/pviotti/openhospital)

This is the root project of the [Open Hospital][openhospital] code base.

## Getting started

After git-cloning this project, to initialize the submodules, issue:

    git submodule init
    git submodule update


Alternatively, you can git-clone all modules recursively:

    git clone --recurse-submodules https://github.com/pviotti/openhospital

After that, you can compile the project by issuing:

    mvn package


 [openhospital]: https://www.open-hospital.org/
