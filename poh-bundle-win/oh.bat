@echo off
REM # Open Hospital (www.open-hospital.org)
REM # Copyright © 2006-2021 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
REM #
REM # Open Hospital is a free and open source software for healthcare data management.
REM #
REM # This program is free software: you can redistribute it and/or modify
REM # it under the terms of the GNU General Public License as published by
REM # the Free Software Foundation, either version 3 of the License, or
REM # (at your option) any later version.
REM #
REM # https://www.gnu.org/licenses/gpl-3.0-standalone.html
REM #
REM # This program is distributed in the hope that it will be useful,
REM # but WITHOUT ANY WARRANTY; without even the implied warranty of
REM # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM # GNU General Public License for more details.
REM #
REM # You should have received a copy of the GNU General Public License
REM # along with this program. If not, see <http://www.gnu.org/licenses/>.
REM #

REM ################### Configuration ###################
REM
REM set LEGACYMODE=on to start with legacy oh.bat script
REM
REM launch oh.bat -h to see available options
REM 
REM default start is powershell oh.ps1 script

set LEGACYMODE="off"

REM ##############################################
REM check for legacy mode

if %LEGACYMODE%=="on" goto legacy

REM ###### Functions and script start
goto :init

:header
	echo.
	echo  Open Hospital startup script
	echo  %__BAT_NAME% v%__SCRIPTVERSION%
	echo.
	goto :eof

:usage
	echo USAGE:
	echo   %__BAT_NAME% [-option]
	echo.
	echo.  -h, -?, --help              shows this help
	echo.  -legacymode, --legacymode   start OH with legacy oh.bat
	goto :eof

:init
	set "__SCRIPTVERSION=1.0"
	set "__BAT_FILE=%~0"
	set "__BAT_PATH=%~dp0"
	set "__BAT_NAME=%~nx0"

:parse
	if "%~1"=="" goto :main

	if /i "%~1"=="/?"	call :header & call :usage & goto :end
	if /i "%~1"=="-?"	call :header & call :usage & goto :end
	if /i "%~1"=="-h"	call :header & call :usage & goto :end
	if /i "%~1"=="-help"	call :header & call :usage & goto :end
	if /i "%~1"=="--help"	call :header & call :usage & goto :end

	if /i "%~1"=="/legacymode"	call :legacy & goto :end
	if /i "%~1"=="-legacymode"	call :legacy & goto :end
	if /i "%~1"=="--legacymode"	call :legacy & goto :end

	shift
	goto :parse

:main
	REM ################### oh.ps1 ###################
	REM default start: oh.ps1

	echo Starting OH with oh.ps1...

	REM launch powershell script
	powershell.exe  -ExecutionPolicy Bypass -File  ./oh.ps1

	goto end

:legacy
REM ############################# Legacy oh.bat ############################

echo Legacy mode - Starting OH with oh.bat...

REM ################### Configuration ###################
set OH_PATH=%~dps0

REM # Language setting - default set to en
REM set OH_LANGUAGE=en fr es it pt
set OH_LANGUAGE=en

REM # set log level to INFO | DEBUG - default set to INFO
set LOG_LEVEL=INFO

REM # enable / disable DICOM (true|false)
set DICOM_ENABLE=false

REM ### Software configuration - change at your own risk :-)
REM # Database
set MYSQL_SERVER=localhost
set MYSQL_PORT=3306
set MYSQL_ROOT_PW=tmp2021oh111
set DATABASE_NAME=oh
set DATABASE_USER=isf
set DATABASE_PASSWORD=isf123

set DICOM_MAX_SIZE="4M"

set OH_DIR=oh
set SQL_DIR=sql
set DATA_DIR=data\db
set LOG_DIR=data\log
set DICOM_DIR=data\dicom_storage
set TMP_DIR=tmp
set DB_CREATE_SQL=create_all_en.sql
REM #-> DB_CREATE_SQL default is set to create_all_en.sql - set to "create_all_demo.sql" for demo or create_all_[lang].sql for language
set LOG_FILE=startup.log
set OH_LOG_FILE=openhospital.log

REM ######## MySQL Software
REM # MariaDB 64bit
REM http://ftp.bme.hu/pub/mirrors/mariadb/mariadb-10.2.40/win32-packages/mariadb-10.2.40-winx64.zip
REM # MySQL 64bit
REM https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.35-winx64.zip

REM # MariaDB 32bit
REM http://ftp.bme.hu/pub/mirrors/mariadb/mariadb-10.2.40/win32-packages/mariadb-10.2.40-win32.zip
REM # MySQL 32bit
REM https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.35-win32.zip

REM set MYSQL_DIR=mysql-5.7.35-win32
set MYSQL_DIR=mariadb-10.2.40-winx64

REM ####### JAVA Software
REM ######## JAVA 64bit - experimental architecture
REM ### JRE 11 - openjdk distribution
REM set JAVA_URL="https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/"
REM set JAVA_DISTRO="OpenJDK11U-jre_x64_windows_hotspot_11.0.11_9.zip"

REM ######## JAVA 32bit - default architecture
REM ### JRE 11 - openjdk distribution
REM set JAVA_URL="https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/"
REM set JAVA_DISTRO="OpenJDK11U-jre_x86-32_windows_hotspot_11.0.11_9.zip"

set JAVA_DIR=jdk-11.0.12+7-jre
set JAVA_BIN=%OH_PATH%\%JAVA_DIR%\bin\java.exe

set REPLACE_PATH=%OH_PATH%\%MYSQL_DIR%\bin

REM ######## Script start
echo Configuring Open Hospital...

REM # Set mysql TCP port
set startPort=%MYSQL_PORT%
:searchport
netstat -o -n -a | find "LISTENING" | find ":%startPort% " > NUL
if "%ERRORLEVEL%" equ "0" (
	echo TCP port %startPort% unavailable
	set /a startPort +=1
	goto :searchport
) ELSE (
	echo TCP port %startPort% available
	set MYSQL_PORT=%startPort%
	goto :foundport
)

:foundport
echo Found TCP port %MYSQL_PORT% for MySQL !

REM # Create tmp and log dir
mkdir %OH_PATH%\%TMP_DIR%
mkdir %OH_PATH%\%LOG_DIR%

echo Generating MySQL config file...
REM ### Setup MySQL configuration
echo f | xcopy %OH_PATH%\etc\mysql\my.cnf.dist %OH_PATH%\etc\mysql\my.cnf /y > "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe OH_PATH_SUBSTITUTE %OH_PATH% -- %OH_PATH%\etc\mysql\my.cnf  >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe MYSQL_SERVER %MYSQL_SERVER% -- %OH_PATH%\etc\mysql\my.cnf >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe MYSQL_PORT %MYSQL_PORT% -- %OH_PATH%\etc\mysql\my.cnf >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe MYSQL_DISTRO %MYSQL_DIR% -- %OH_PATH%\etc\mysql\my.cnf >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DICOM_SIZE %DICOM_MAX_SIZE% -- %OH_PATH%\etc\mysql\my.cnf >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe TMP_DIR %TMP_DIR% -- %OH_PATH%\etc\mysql\my.cnf >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DATA_DIR %DATA_DIR% -- %OH_PATH%\etc\mysql\my.cnf >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe LOG_DIR %LOG_DIR% -- %OH_PATH%\etc\mysql\my.cnf >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1

echo Setting up OH configuration files...
REM ### Setup dicom.properties
echo f | xcopy %OH_PATH%\%OH_DIR%\rsc\dicom.properties.dist %OH_PATH%\%OH_DIR%\rsc\dicom.properties /y >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe OH_PATH_SUBSTITUTE %OH_PATH% -- %OH_PATH%\%OH_DIR%\rsc\dicom.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DICOM_SIZE %DICOM_MAX_SIZE% -- %OH_PATH%\%OH_DIR%\rsc\dicom.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DICOM_DIR %DICOM_DIR% -- %OH_PATH%\%OH_DIR%\rsc\dicom.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1

REM ### Setup database.properties
echo f | xcopy %OH_PATH%\%OH_DIR%\rsc\database.properties.dist %OH_PATH%\%OH_DIR%\rsc\database.properties /y >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBSERVER %MYSQL_SERVER% -- %OH_PATH%\%OH_DIR%\rsc\database.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBPORT %MYSQL_PORT% -- %OH_PATH%\%OH_DIR%\rsc\database.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBUSER %DATABASE_USER% -- %OH_PATH%\%OH_DIR%\rsc\database.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBPASS %DATABASE_PASSWORD% -- %OH_PATH%\%OH_DIR%\rsc\database.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBNAME %DATABASE_NAME% -- %OH_PATH%\%OH_DIR%\rsc\database.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1

REM ### Setup settings.properties
echo f | xcopy %OH_PATH%\%OH_DIR%\rsc\settings.properties.dist %OH_PATH%\%OH_DIR%\rsc\settings.properties /y >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe OH_SET_LANGUAGE %OH_LANGUAGE% -- %OH_PATH%\%OH_DIR%\rsc\settings.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1

REM ### Setup log4j.properties
REM # double escape path
set OH_LOG_DEST=%OH_PATH:\=\\%
set OH_LOG_DEST=%OH_LOG_DEST%\\%LOG_DIR%\\%OH_LOG_FILE%
echo f | xcopy %OH_PATH%\%OH_DIR%\rsc\log4j.properties.dist %OH_PATH%\%OH_DIR%\rsc\log4j.properties /y >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBSERVER %MYSQL_SERVER% -- %OH_PATH%\%OH_DIR%\rsc\log4j.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBPORT %MYSQL_PORT% -- %OH_PATH%\%OH_DIR%\rsc\log4j.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBUSER %DATABASE_USER% -- %OH_PATH%\%OH_DIR%\rsc\log4j.properties  >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBPASS %DATABASE_PASSWORD% -- %OH_PATH%\%OH_DIR%\rsc\log4j.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe DBNAME %DATABASE_NAME% -- %OH_PATH%\%OH_DIR%\rsc\log4j.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe LOG_LEVEL %LOG_LEVEL% -- %OH_PATH%\%OH_DIR%\rsc\log4j.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
%REPLACE_PATH%\replace.exe LOG_DEST %OH_LOG_DEST% -- %OH_PATH%\%OH_DIR%\rsc\log4j.properties >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1

REM ### Setup database if not already existing
if not EXIST %OH_PATH%\%DATA_DIR%\%DATABASE_NAME% (
 	REM # Remove database files
	echo Removing data...
	rmdir /s /q %OH_PATH%\%DATA_DIR%
	REM # Create directories
	mkdir %OH_PATH%\%DATA_DIR%
	mkdir %OH_PATH%\%TMP_DIR%
	mkdir %OH_PATH%\%LOG_DIR%
	mkdir %OH_PATH%\%DICOM_DIR%
	del /s /q %OH_PATH%\%TMP_DIR%\*

	if %MYSQL_DIR:~0,5% == maria (
		echo Initializing MariaDB...
		start /b /min /wait %OH_PATH%\%MYSQL_DIR%\bin\mysql_install_db.exe --datadir=%OH_PATH%\%DATA_DIR% --password=%MYSQL_ROOT_PW%  >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
	)
	if %MYSQL_DIR:~0,5% == mysql (
		echo Initializing MySQL...
		start /b /min /wait %OH_PATH%\%MYSQL_DIR%\bin\mysqld.exe --initialize-insecure --console --basedir="%OH_PATH%\%MYSQL_DIR%" --datadir="%OH_PATH%\%DATA_DIR%"
	)
	if ERRORLEVEL 1 (goto error)

	echo Starting MySQL server on port %MYSQL_PORT%...
	start /b /min %OH_PATH%\%MYSQL_DIR%\bin\mysqld.exe --defaults-file=%OH_PATH%\etc\mysql\my.cnf --tmpdir=%OH_PATH%\%TMP_DIR% --standalone --console
	if ERRORLEVEL 1 (goto error)
	timeout /t 2 /nobreak >nul
	
	REM # If using MySQL root password need to be set
	if %MYSQL_DIR:~0,5% == mysql (
		echo Setting MySQL root password...
		start /b /min /wait %OH_PATH%\%MYSQL_DIR%\bin\mysql.exe -u root --skip-password --host=%MYSQL_SERVER% --port=%MYSQL_PORT% -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '%MYSQL_ROOT_PW%';" >> %OH_PATH%\%LOG_DIR%\%LOG_FILE% 2>&1
		if ERRORLEVEL 1 (goto error)
	)
	
	echo Creating database...
	start /b /min /wait %OH_PATH%\%MYSQL_DIR%\bin\mysql.exe -u root -p%MYSQL_ROOT_PW% --host=%MYSQL_SERVER% --port=%MYSQL_PORT% -e "CREATE DATABASE %DATABASE_NAME%; CREATE USER '%DATABASE_USER%'@'localhost' IDENTIFIED BY '%DATABASE_PASSWORD%'; GRANT ALL PRIVILEGES ON %DATABASE_NAME%.* TO '%DATABASE_USER%'@'localhost' IDENTIFIED BY '%DATABASE_PASSWORD%';" >> %OH_PATH%\%LOG_DIR%\%LOG_FILE% 2>&1
	if ERRORLEVEL 1 (goto error)
	
	echo Importing database schema %DB_CREATE_SQL%...
	cd /d %OH_PATH%\%SQL_DIR%
	start /b /min /wait %OH_PATH%\%MYSQL_DIR%\bin\mysql.exe --local-infile=1 -u root -p%MYSQL_ROOT_PW% --host=%MYSQL_SERVER% --port=%MYSQL_PORT% %DATABASE_NAME% < "%OH_PATH%\sql\%DB_CREATE_SQL%"  >> "%OH_PATH%\%LOG_DIR%\%LOG_FILE%" 2>&1
	if ERRORLEVEL 1 (goto error)
	echo Database imported!
) else (
	echo Database already initialized, trying to start...
	echo Starting MySQL server on port %MYSQL_PORT%...
	start /b /min %OH_PATH%\%MYSQL_DIR%\bin\mysqld.exe --defaults-file=%OH_PATH%\etc\mysql\my.cnf --tmpdir=%OH_PATH%\%TMP_DIR% --standalone --console
	if ERRORLEVEL 1 (goto error)
)

REM ###### Setup CLASSPATH #####
set CLASSPATH=%OH_PATH%\%OH_DIR%\lib

SETLOCAL ENABLEDELAYEDEXPANSION

REM Include all jar files under lib\
for %%A IN (%OH_PATH%\%OH_DIR%\lib\*.jar) DO (
	set CLASSPATH=!CLASSPATH!;%%A
)
set CLASSPATH=%CLASSPATH%;%OH_PATH%\%OH_DIR%\bundle
set CLASSPATH=%CLASSPATH%;%OH_PATH%\%OH_DIR%\rpt
set CLASSPATH=%CLASSPATH%;%OH_PATH%\%OH_DIR%\rsc
set CLASSPATH=%CLASSPATH%;%OH_PATH%\%OH_DIR%\rsc\icons
set CLASSPATH=%CLASSPATH%;%OH_PATH%\%OH_DIR%\rsc\images
set CLASSPATH=%CLASSPATH%;%OH_PATH%\%OH_DIR%\rsc\SmsGateway
set CLASSPATH=%CLASSPATH%;%OH_PATH%\%OH_DIR%\bin\OH-gui.jar

REM # Setup native_lib_path for current architecture
REM # with DICOM workaround - force NATIVE_LIB to 32bit

if %PROCESSOR_ARCHITECTURE%==AMD64 if not %DICOM_ENABLE%==true (
	set NATIVE_LIB_PATH=%OH_PATH%\%OH_DIR%\lib\native\Win64
) else (
	set NATIVE_LIB_PATH=%OH_PATH%\%OH_DIR%\lib\native\Windows
)

REM ###### Start Open Hospital #####

echo Starting Open Hospital...

cd /d %OH_PATH%\%OH_DIR%
%JAVA_BIN% -client -Dsun.java2d.dpiaware=false -Djava.library.path=%NATIVE_LIB_PATH% -cp %CLASSPATH% org.isf.menu.gui.Menu

REM # Shutdown MySQL
echo Shutting down MySQL...
start /b /min /wait %OH_PATH%\%MYSQL_DIR%\bin\mysqladmin --user=root --password=%MYSQL_ROOT_PW% --host=%MYSQL_SERVER% --port=%MYSQL_PORT% shutdown >> %OH_PATH%\%LOG_DIR%\%LOG_FILE% 2>&1

REM # Exit
echo Exiting Open Hospital...
cd /d %OH_PATH%
echo Done !

goto end

:error
	echo Error starting Open Hospital, exiting.
	cd /d %OH_PATH%
	goto end

:end
	call :cleanup
	exit /B

:cleanup
	REM The cleanup function is only really necessary if you
	REM are _not_ using SETLOCAL.
	set "__SCRIPTVERSION="
	set "__BAT_FILE="
	set "__BAT_PATH="
	set "__BAT_NAME="
	set "LEGACYMODE="

	goto :eof

