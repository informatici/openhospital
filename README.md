# Open Hospital

This repository is used to assemble the portable (or all-in-one) packages of the [Open Hospital][openhospital] project, which you can download [here][releases].

## How to contribute

If you'd like to contribute to the Open Hospital project, please read [CONTRIBUTING.md][contributing].

## How to create Open Hospital packages

To create the Open Hospital packages,
make sure to have installed the following dependencies on a Linux machine:
_JDK 8+, Maven, asciidoctor-pdf, docker-compose, MySQL client, zip._

Then follow these simple steps:

 1. Clone this repository and initialize the submodules:

        git clone https://github.com/informatici/openhospital

 2. Run the script that compiles the components of Open Hospital, and assembles the portable distributions:

        cd openhospital
        ./build_poh.sh


 [openhospital]: https://www.open-hospital.org/
 [releases]: https://github.com/informatici/openhospital/releases
 [contributing]: https://github.com/informatici/openhospital/blob/master/CONTRIBUTING.md
