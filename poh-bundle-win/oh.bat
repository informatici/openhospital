@echo off
set OH_PATH=%~dps0
set REPLACE_PATH=%OH_PATH%\mysql-5.7.30-win32\bin
set freePort=
set startPort=3306
FOR /F "tokens=2,2 delims==" %%i IN ('findstr /i "dicom.max.size" %OH_PATH%oh\rsc\dicom.properties.ori') DO (
  set dicom_size=%%i
)
if "%dicom_size%" equ "" (
	set dicom_size=4M
)

:SEARCHPORT
netstat -o -n -a | find "LISTENING" | find ":%startPort% " > NUL
if "%ERRORLEVEL%" equ "0" (
  echo "port unavailable %startPort%"
  set /a startPort +=1
  GOTO :SEARCHPORT
) ELSE (
  echo "port available %startPort%"
  set freePort=%startPort%
  GOTO :FOUNDPORT
)

:FOUNDPORT
echo "MySQL will listen on the free port %freePort%"

cd /d %OH_PATH%\oh\rsc
echo f | xcopy dicom.properties.ori dicom.properties /y
%REPLACE_PATH%\replace.exe OH_PATH_SUBSTITUTE %OH_PATH% -- dicom.properties
REM %REPLACE_PATH%\replace.exe "^x5c" "^x2f" -- dicom.properties
echo f | xcopy database.properties.sample database.properties /y
%REPLACE_PATH%\replace.exe 3306 %freePort% -- database.properties
REM %REPLACE_PATH%\replace.exe "^x5c" "^x2f" -- database.properties 
echo f | xcopy log4j.properties.ori log4j.properties /y
%REPLACE_PATH%\replace.exe 3306 %freePort% -- log4j.properties
REM %REPLACE_PATH%\replace.exe "^x5c" "^x2f" -- log4j.properties 

cd /d %OH_PATH%
echo f | xcopy my.ori %OH_PATH%mysql-5.7.30-win32\bin\my.cnf /y
%REPLACE_PATH%\replace.exe 3306 %freePort% -- %OH_PATH%mysql-5.7.30-win32\bin\my.cnf
%REPLACE_PATH%\replace.exe OH_PATH_SUBSTITUTE %OH_PATH% -- %OH_PATH%mysql-5.7.30-win32\bin\my.cnf
%REPLACE_PATH%\replace.exe DICOM_SIZE %dicom_size% -- %OH_PATH%mysql-5.7.30-win32\bin\my.cnf
REM %REPLACE_PATH%\replace.exe "^x5c" "^x2f" -- my.cnf 

IF EXIST "%OH_PATH%database.sql" (
  echo Initializing database...
  %OH_PATH%mysql-5.7.30-win32\bin\mysqld --initialize-insecure --tmpdir=%OH_PATH%\tmp
  IF ERRORLEVEL 1 (goto END)
  start /b /min %OH_PATH%mysql-5.7.30-win32\bin\mysqld --tmpdir=%OH_PATH%\tmp --standalone --console
  %OH_PATH%mysql-5.7.30-win32\bin\mysql -u root --port=%freePort% -e "CREATE SCHEMA oh; GRANT ALL ON oh.* TO 'isf'@'localhost' IDENTIFIED BY 'isf123'; GRANT ALL ON oh.* TO 'isf'@'%' IDENTIFIED BY 'isf123';"
  %OH_PATH%mysql-5.7.30-win32\bin\mysql -u root --port=%freePort% oh < "%OH_PATH%database.sql"
  IF ERRORLEVEL 1 (goto END)
  echo Database initialized.
  DEL %OH_PATH%database.sql
) ELSE (
  echo "missing database.sql or Database already initialized"
  start /b /min %OH_PATH%mysql-5.7.30-win32\bin\mysqld --tmpdir=%OH_PATH%\tmp --standalone --console
)

set OH_HOME=%OH_PATH%oh

set OH_BIN=%OH_HOME%\bin
set OH_LIB=%OH_HOME%\lib
set OH_BUNDLE=%OH_HOME%\bundle
set OH_REPORT=%OH_HOME%\rpt

set CLASSPATH=%OH_BIN%

SETLOCAL ENABLEDELAYEDEXPANSION

FOR %%A IN (%OH_LIB%\*.jar) DO (
	set CLASSPATH=!CLASSPATH!;%%A
)
set CLASSPATH=%CLASSPATH%;%OH_BIN%\OH-gui.jar
set CLASSPATH=%CLASSPATH%;%OH_BUNDLE%
set CLASSPATH=%CLASSPATH%;%OH_REPORT%

cd /d %OH_PATH%oh\
%OH_PATH%jdk8u252-b09-jre\bin\java.exe -Dlog4j.configuration=%OH_PATH%oh/rsc/log4j.properties -showversion -Dsun.java2d.dpiaware=false -Djava.library.path=%OH_PATH%oh\lib\native\Windows -cp %CLASSPATH% org.isf.menu.gui.Menu
start /b %OH_PATH%mysql-5.7.30-win32\bin\mysqladmin --user=root --password= --port=%freePort% shutdown
exit

:END
echo Error initializing the DB.
pause