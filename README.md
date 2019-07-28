# Open Hospital [![Build Status](https://travis-ci.org/informatici/openhospital.svg?branch=master)](https://travis-ci.org/informatici/openhospital)

This is the *root* project of the [Open Hospital][openhospital] code base.  
[Here][releases] you can download the portable distribution of Open Hospital.

## How to create Open Hospital portable

To create the Open Hospital portable distributions,
make sure to have installed the following dependencies on a Linux machine: 
JDK 6+, Maven, asciidoctor-pdf, docker-compose, MySQL client.  
Then follow these simple steps:

 1. Clone this repository and initialize the submodules:

        git clone --recurse-submodules https://github.com/informatici/openhospital

 2. Run the script that compiles the projects *core* and *gui*, and assembles the portable distributions:

        ./build_poh.sh


 [openhospital]: https://www.open-hospital.org/
 [releases]: https://github.com/informatici/openhospital/releases
