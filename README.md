# OpenHospital
OpenHospital 2.0 (ISF OpenHospital web version) - WIP

**How to build with Maven:**
_(requires Maven 3.2.5 or lesser installed and configured)_

    mvn clean install
    
**How to create the DataBase**:

You need a local (or remote) MySQL server where to run the script in mysql/db/ folder

	create_all_en.sql
	
For remote MySQL server you need to change:
- rsc/database.properties
- rsc/log4j.properties

***With docker compose***

Simply run:

docker-compose up 

**How to launch the software**:

Use scripts OpenHospital.sh (Linux) or OpenHospital.cmd (Windows)

**Other info**

Please read Admin and User manuals in doc/ folder

# How to contribute

Please read the OpenHospital [Wiki](https://openhospital.atlassian.net/wiki/display/OH/Contribution+Guidelines)

See the Open Issues on [Jira](https://openhospital.atlassian.net/issues/)
