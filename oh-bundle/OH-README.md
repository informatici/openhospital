# OH - Open Hospital Portable | Client 

OH - Open Hospital (https://www.open-hospital.org/) is a free and open-source Electronic Health Record (EHR) software application.
Open Hospital is deployed as a desktop application that can be used in a standalone, single user mode (PORTABLE mode)
or in a client / server network configuration (CLIENT mode), where multiple clients and users connect to the same database server.

OH is developed in Java and it is based on open-source tools and libraries; it runs on any computer, requires low resources and is designed to work without an internet connection.

Open Hospital is the first of a set of software applications that ISF - Informatici Senza Frontiere (https://www.informaticisenzafrontiere.org/) has developed to support the information management and the activities of hospitals and health centers in the simplest manner possible, by providing tools for the administrative operations (like registering patients, manage laboratory analysis and pharmaceutical stocks) and to produce detailed statistics and reports.
It was first deployed in 2006 at the St. Luke Hospital in Angal (Uganda) and it is now used in dozens of different locations around the world.

When OH is used in PORTABLE mode, it is easily possible to move the installation on another computer or even run it from a USB stick or drive.
All you have to do is to copy the root installation directory of OH to your favourite folder, where the program and all the data are kept.
OH uses its own version of the Java Virtual Machine (JRE) and the MariaDB/MySQL server.
OH is released under the GNU GPL 3.0 License.

The Linux version has been tested on different distributions and versions,
including Ubuntu 16.04 i386 (32bit) and up to Ubuntu 21.04 x64 (64bit).
The Windows version has been tested on Windows 7/10/11 (64/bit)

# Dowloading OH - Releases

[**Download latest release from github**](https://github.com/informatici/openhospital/releases/latest) 

# Running OH - Quickstart

## Common to all Operating Systems / architectures

- Download Open Hospital package for the desired architecture
- Unzip/untar the package
- Browse to the extracted folder

## Linux

- start OH by running **./oh.sh**
- to see available options, run **./oh.sh -h**

```
 ---------------------------------------------------------
|                                                         |
|                   Open Hospital | OH                    |
|                                                         |
 ---------------------------------------------------------
 lang en | arch x86_64 | mode PORTABLE | log level INFO 
 ---------------------------------------------------------

 Usage: oh.sh [ -l en|fr|it|es|pt ] 

   -C    start OH in CLIENT mode (client / server configuration)
   -P    start OH in PORTABLE mode
   -d    start OH in debug mode
   -D    start OH with Demo data
   -g    generate configuration files
   -G    setup GSM
   -h    show this help
   -i    initialize/install OH database
   -l    set language: en|fr|it|es|pt
   -s    save OH database
   -r    restore OH database
   -t    test database connection (CLIENT mode only)
   -v    show OH software version and configuration
   -X    clean/reset OH installation
```

## Windows

- double click on the **oh.bat** batch file and choose among available options:

```
 ---------------------------------------------------------
|                                                         |
|                   Open Hospital | OH                    |
|                                                         |
 ---------------------------------------------------------
 lang en | arch x86_64 | mode PORTABLE/CLIENT | log level INFO

Usage: oh.ps1 [ -lang en|fr|it|es|pt ] 
              [ -mode PORTABLE|CLIENT ]
              [ -loglevel INFO|DEBUG ] 
              [ -dicom on|off ]
              [ -interactive on|off ]
              [ -manual_config on|off ]


 C    start OH in CLIENT mode (client / server configuration)
 P    start OH in PORTABLE mode
 d    start OH in debug mode
 D    start OH in Demo mode
 g    generate configuration files
 G    setup GSM
 i    initialize / install OH database
 l    set language: en|fr|it|es|pt
 s    save OH database
 r    restore OH database
 t    test database connection (Client mode only)
 v    show OH software version and configuration
 X    clean/reset OH installation
 q    quit
```

Note: The **oh.bat** launches the **oh.ps1** startup file automatically.
The script presents the interactive menu that can be used to setup and choose how to run Open Hospital.

-> To manually run **oh.ps1** (powershell script):

- right-click on **oh.ps1** -> Properties -> General -> Security
- select "Unblock"
- right click on **oh.ps1** and select "Run with Powershell"
- if asked for permission to execute the script select "Allow"
- choose among available options

It might be necessary to set the correct permissions / exclusions also in the Windows Security Center, to allow OH to communicate on
the MySQL / MariaDB local TCP port.

-> To run oh.ps1 directly from command line:

```
powershell.exe -ExecutionPolicy Bypass -File  ./oh.ps1 [options]
```

-> To run oh.ps1 with command line options (example):

```
./oh.ps1 -lang it -mode PORTABLE -loglevel DEBUG -dicom off -interactive off -manual_config on
```

### Windows - legacy mode

It's also possible to start Open Hospital with the legacy batch file (old oh.bat behaviour):
- open cmd.exe, browse to the OH installation directory and run **.\oh.bat -legacymode**
- to see available options in legacymode, run **.\oh.bat -h**

# Options 

- **C**    start Open Hospital in CLIENT mode, usually when an external database server is used (Client / Server configuration)
- **P**    start Open Hospital in PORTABLE mode, where data is saved locally
- **d**    start OH in DEBUG mode - useful to debug errors or bugs by logging more extended informations to log file
- **D**    start OH with Demo data - loads a demo database in order to test the software 
- **g**    generate OH configuration files (oh/rsc/\*.properties) and exit
- **G**    setup GSM modem to enable sms interaction
- **i**    initialize / install OH database
- **l**    set local language: en|fr|it|es|pt
- **s**    save / dump the Open Hospital database in sql format
- **r**    restore Open Hospital database from backup or external sql file: user will be prompted for input sql file
- **t**    test database connection to the configured database server (Client mode only)
- **v**    show Open Hospital external software version and configuration
- **X**    clean/reset OH installation by deleting all data and configuration files -> **use with caution** <-
- **q**    quit (windows only)
- **h**    help (linux only)

# Configuration

Some advanced options can be configured manually by editing the scripts (oh.sh and oh.ps1 - do not modify oh.bat unless legacymode is used) and setting the specific script variables.
This might also be useful to set different combinations of options (language, debug level, ...) for specific needs.

### OH directory path
```
############## OH general configuration - change at your own risk :-) ##############
# -> OH_PATH is the directory where Open Hospital files are located
# OH_PATH="c:\Users\OH\OpenHospital\oh-1.11"
```

### Distribution type - CLIENT | PORTABLE

```
############## OH general configuration - change at your own risk :-) ##############
OH_MODE=PORTABLE # set functioning mode to PORTABLE | CLIENT # linux
$script:OH_MODE="PORTABLE" # windows
```
### Demo mode
```
# set DEMO_DATA to on to enable demo database loading - default set to off
#
# -> Warning -> __requires deletion of all portable data__
#
DEMO_DATA=off # linux
#$script:DEMO_DATA="off" # windows
```
### Interface and software language:
```
# Language setting - default set to en
#OH_LANGUAGE=en fr es it pt # linux
#$script:OH_LANGUAGE="en" # fr es it pt # windows
```
### (Windows only) Enable / disable DICOM features
```
# enable / disable DICOM (on|off)
#$script:DICOM_ENABLE="off"
```
### Log level / debug mode
```
# set log level to INFO | DEBUG - default set to INFO
#LOG_LEVEL=INFO # linux
#$script:LOG_LEVEL="INFO" # windows
```
### Enable system wide JAVA
```
# set JAVA_BIN 
# Uncomment this if you want to use system wide JAVA
#JAVA_BIN=`which java` # linux
#$script:JAVA_BIN="C:\Program Files\JAVA\bin\java.exe" # windows
```
### Database and software configuration

If a database server hostname/address is specified (other then localhost), OH can be started in CLIENT mode and used in a client/server / LAN environment.
```
############## OH local configuration - change at your own risk :-) ##############
# Database
MYSQL_SERVER=localhost
MYSQL_PORT=3306
MYSQL_ROOT_PW="xxxxxxxxxx"
DATABASE_NAME=oh
DATABASE_USER=isf
DATABASE_PASSWORD="xxxxx"

DICOM_MAX_SIZE="4M"
DICOM_STORAGE="FileSystemDicomManager" # SqlDicomManager
DICOM_DIR="data/dicom_storage"

OH_DIR="oh"
OH_DOC_DIR="../doc"
CONF_DIR="data/conf"
DATA_DIR="data/db"
BACKUP_DIR="data/dump"
LOG_DIR="data/log"
SQL_DIR="sql"
TMP_DIR="tmp"

LOG_FILE=startup.log

DB_DEMO="create_all_demo.sql"
LOG_FILE=startup.log
OH_LOG_FILE=openhospital.log

```
### Manual config

It is possibile to set the MANUAL_CONFIG option to "on" to keep the OH configuration files, so they are not regenerated and overwritten at every startup.
This is useful for production environment where the configuration is fixed.

```
############## Script startup configuration - change at your own risk :-) ##############
## set MANUAL_CONFIG to "on" to setup configuration files manually
# my.cnf and all oh/rsc/*.properties files will not be generated or
# overwritten if already present
MANUAL_CONFIG=off # linux
$script:MANUAL_CONFIG="off" # windows
```
### (Windows only) Enable interactive mode
```
# Interactive mode
# set INTERACTIVE_MODE to "off" to launch oh.ps1 without calling the user
# interaction menu (script_menu). Useful if automatic startup of OH is needed.
# In order to use this mode, setup all the OH configuration variables in the script
# or pass arguments via command line.
$script:INTERACTIVE_MODE="on"
```

# Default directory structure

The scripts takes care of creating all the needed data directories and configuration files.
Everything is also parametric and user adjustable in the scripts with variables (or via command line options).
The default folder structure is now clean, simple and **common to all distros:**

```
/oh -> Open Hospital distribution
/sql -> containing the SQL creation scripts
/data/conf -> configuration files for database (MariaDB / MySQL)
```
Created at runtime:
```
/tmp 
data/db
data/log
data/dicom_storage
```
External software package downloaded at first run:

```
Mariadb 10.2.x server
Java JRE, Zulu or OpenJDK distribution
```

# Documentation

Administrator and User manuals are available in the **doc** folder.

# Known issues

If you experience problems in starting up the script, avoid long folder path and path with special characters / spaces in it.

## Linux

- If you get this error:

```
Error on creating OH Database error while loading shared libraries: libncurses.so.5.
```

You have to install the ncurses librares, on Ubuntu:

```
sudo apt-get install libncurses5
```

- If you get this error:
```
Error Initializing MySQL database on port 3306 error while loading shared libraries: libaio.so.1. I had to install it manually and re-launch the script.
```

You have to install the libaio libraries, on Ubuntu:

```
sudo apt-get install libaio1 
```

- If you select languages en-fr-it, a ICD10 patologies subset is loaded at startup, languages es-pt don't.

## Windows

Powershell minimun version 5.1 is required to run oh.ps1.
To install Powershell 5.1 go to https://www.microsoft.com/en-us/download/details.aspx?id=54616

Dicom functionalities are only available on 32bit JAVA environment. If DICOM is needed, 32bit jre is mandatory.
If you need DICOM on Windows 64 bit set **DICOM_ENABLE="on"** in the script.

If you get this error:

```
+ CategoryInfo : NotSpecified: (:) [], PSSecurityException
+ FullyQualifiedErrorId : RuntimeException or UnauthorizedAccess
```

- Start Windows PowerShell with the "Run as Administrator" option. Only members of the Administrators group on the computer can change the execution policy.
Enable running unsigned scripts by entering:
```
set-executionpolicy remotesigned
```
- You might also be required to enable access on Windows Firewall to oh.ps1 and/or to the TCP port used for the local database (PORTABLE mode).

## Windows - legacy mode

(*) If you are using oh.bat in legacy mode, you might have to download and unzip java ad mysql manually.
In order to download and unzip Java:

- Visit  https://cdn.azul.com/zulu/bin/
- download the **JRE - .zip version**

**x86 - 32bit:** https://cdn.azul.com/zulu/bin/zulu8.58.0.13-ca-fx-jre8.0.312-win_i686.zip

**x64 - 64bit:** https://cdn.azul.com/zulu/bin/zulu8.58.0.13-ca-fx-jre8.0.312-win_x64.zip

- unzip the downloaded file into the base directory where OpenHospital has been placed.

In order to download and unzip mariadb:

- Visit https://downloads.mariadb.org/mariadb/10.2/
- Select the Operating System: **Windows**
- Select package type: **ZIP file**
- Select CPU (architecture) **32 / 64**
- Download the zip file:


**x86 - 32bit:** https://downloads.mariadb.com/MariaDB/mariadb-10.2.41/win32-packages/mariadb-10.2.41-win32.zip

**x64 - 64bit:** https://downloads.mariadb.com/MariaDB/mariadb-10.2.41/winx64-packages/mariadb-10.2.41-winx64.zip

- unzip the downloaded file into the base directory where OpenHospital has been placed.

# oh.sh / oh.ps1 - features and development

In order to have a complete, easy to support and extensible solution to run Open Hospital on Linux, oh.sh has been rewritten, also adding a few possible useful user functions.
For the same reason, a completely new powershell script has been writtend for Windows: oh.ps1 (run by oh.bat).

A short description of changes for the Linux version (mostly the same behavior and options are applicable to the windows oh.ps1 version):

- Complete overhaul of the code, reworked the entire scripts
- Everything is now based on functions and parametric variables
- Menu based, interactive options for many operations: see **./oh.sh -h** (or see below for short description)
- **Unified script** for:
    Portable 64bit (default) and 32bit (with automatic architecture detection)
    Open Hospital client (no more separated startup.sh is needed ;-) (**it is now possible to package every linux distro, client/portable/32 or 64 bit with a single package**)
- **New**: Language support (both via variable in the script or user input option: **oh.sh -l fr**)
- **New**: Demo database support (See oh.sh -D)
- **New**: Client mode support (see oh.sh -C)
- **New**: Save (see oh.sh -s) / Restore (oh.sh -r) database, available both for CLIENT and PORTABLE mode !
- **New**: GSM setup integrated via -G command line option - setupGSM.sh (https://github.com/informatici/openhospital-gui/blob/develop/SetupGSM.sh) is obsolete now
- **New**: debug mode -> set log4.properties to DEBUG mode (default is INFO)
- **New**: manual config mode (set MANUAL_CONFIG=on in script) -> mysql and oh configuration files are not generated automatically or overwritten, useful for production environment
- **New**: test database connection option (see oh.sh -t)
- **New**: displays software versions and current configuration (see oh.sh -v)
- **New**: generate config files (see oh.sh -g)
- **New**: install / initialize database (see oh.sh -i)
- Centralized variable managing (see related config file changes applied): now all (well, almost all, still some "isf" reference in SQL creation script...that will be removed ;-) references to database password, mysql host, etc. etc. are in the script and can be easily adapted / modified for any need
- More flexible execution and configuration options
- Automatic configuration files generation
- Everything is commented in the code and a brief output all operations is shown in console
- Startup, database and OH operations are logged to configurable output file
- Backward compatible with old versions and installations, no default behaviour change
- Unified and simplified subdirectories structure
- Added sql subdirectory to organize sql creation scripts
- Added various checks about correct settings of parameters and startup of services
- Added security controls (no more _rm -rf_ here and there :-)
- Added support for **MariaDB** - (tested with version up to mariadb-10.2.41) (OH seems faster and more responsive)
- Windows -> addedd support for path with spaces / special characters 
- Updated MySQL db and user creation syntax (now compatible with MySQL 8 - unsupported)
- Fixed _a_few_ bugs ;-)


*last updated: 2021.12.12*

