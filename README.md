[![Build Status](https://travis-ci.org/informatici/openhospital-core.svg?branch=master)](https://travis-ci.org/informatici/openhospital-core)
# OpenHospital-core
OpenHospital 2.0 (ISF OpenHospital web version) - WIP

**Library gsvideo_isf**
You need a modified version of [gsvideo](https://sourceforge.net/projects/gsvideo/) library in order to use build core:

* clone [gsvideo_isf](https://github.com/informatici/gsvideo_isf)
* follow the instructions in the related README.md

**How to build with Maven:**
_(requires Maven 3.2.5 or lesser installed and configured)_

    mvn clean install
    
You need a local (or remote) MySQL server where to run the JUnit tests. Simply run:

	docker-compose up 

**How to launch the software**:

You need a GUI (Graphic User Interface) in order to use the core:

* clone [OpenHospital-gui](https://github.com/informatici/openhospital-gui) for a Java Swing interface
* follow the instructions in the related README.md


# How to contribute

Please read the OpenHospital [Wiki](https://openhospital.atlassian.net/wiki/display/OH/Contribution+Guidelines)

See the Open Issues on [Jira](https://openhospital.atlassian.net/issues/)
