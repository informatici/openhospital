@echo off
set OH_PATH=%~dps0
set XCHANGE32_PATH=%OH_PATH%\mysql\bin
set freePort=
set startPort=3306

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
%XCHANGE32_PATH%\Xchang32.exe dicom.properties "OH_PATH_SUBSTITUTE" "%OH_PATH%"
%XCHANGE32_PATH%\Xchang32.exe dicom.properties "^x5c" "^x2f"
echo f | xcopy database.properties.sample database.properties /y
%XCHANGE32_PATH%\Xchang32.exe database.properties "3306" "%freePort%"
%XCHANGE32_PATH%\Xchang32.exe database.properties "^x5c" "^x2f"
cd /d %OH_PATH%\mysql\bin
echo f | xcopy my.ori my.cnf /y
%XCHANGE32_PATH%\Xchang32.exe my.cnf "3306" "%freePort%"
%XCHANGE32_PATH%\Xchang32.exe my.cnf "OH_PATH_SUBSTITUTE" "%OH_PATH%"
%XCHANGE32_PATH%\Xchang32.exe my.cnf "^x5c" "^x2f"

start /b /min %OH_PATH%mysql\bin\mysqld --defaults-file=%OH_PATH%mysql\bin\my.cnf --standalone --console

IF EXIST "%OH_PATH%database.sql" (
  echo Initializing database...
  %OH_PATH%mysql\bin\mysql -u root --port=%freePort% oh < "%OH_PATH%database.sql"
  echo Database initialized.
  DEL %OH_PATH%database.sql
) ELSE (
  echo "missing database.sql or Database already initialized"
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
%OH_PATH%jre6\bin\java.exe -showversion -Dsun.java2d.dpiaware=false -Djava.library.path=%OH_PATH%oh\lib\native\Windows -cp %CLASSPATH% org.isf.menu.gui.Menu
start /b %OH_PATH%mysql\bin\mysqladmin --user=root --password= --port=%freePort% shutdown
exit
