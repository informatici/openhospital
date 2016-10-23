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

**How to launch the softer**:

Use scripts OpenHospital.sh (Linux) or OpenHospital.cmd (Windows)


**Other info**

Please read Admin and User manuals in doc/ folder
