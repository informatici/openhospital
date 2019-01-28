# OpenHospital-gui
OpenHospital 2.0 (ISF OpenHospital web version) - WIP

**OpenHospital-core + library gsvideo_isf**
You need the openhospital-core in order to run and a custom library.

* clone [gsvideo_isf](https://github.com/informatici/gsvideo_isf)
* follow the instructions in the related README.md
* clone [openhospital-core](https://github.com/informatici/openhospital-core)
* follow the instructions in the related README.md


**How to build with Maven:**
_(requires Maven 3.2.5 or lesser installed and configured)_

    mvn clean install
    
**How to create the DataBase**:

You need a local (or remote) MySQL server where to run the script in mysql/db/ folder

	create_all_en.sql
	
For remote MySQL server you need to change IP (localost) and PORT (3306) in rsc/applicationContext.properties:

	<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/oh" />

**With docker compose**

Simply run (it will run on localhost:3306):

	docker-compose up 

**How to launch the software**:

Use scripts startup.sh (Linux) or startup.cmd (Windows)

**Other info**

Please read Admin and User manuals in doc/ folder

# How to contribute

Please read the OpenHospital [Wiki](https://openhospital.atlassian.net/wiki/display/OH/Contribution+Guidelines)

See the Open Issues on [Jira](https://openhospital.atlassian.net/issues/)
