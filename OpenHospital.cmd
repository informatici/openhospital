@echo off
set OH_HOME=%~dps0
REM if java is not in the system path set JAVA_HOME variable
REM set JAVA_HOME=%OH_HOME%jvm\bin

for %%i in (java.exe) do set JAVA=%%~$PATH:i

IF NOT DEFINED JAVA (
	@echo Java not found
	EXIT /B
)

set OH_BIN=%OH_HOME%bin
set OH_LIB=%OH_HOME%lib
set OH_BUNDLE=%OH_HOME%bundle
set OH_RESOURCE=%OH_HOME%rsc

set CLASSPATH=%OH_BIN%

SETLOCAL ENABLEDELAYEDEXPANSION

FOR %%A IN (%OH_LIB%\*.jar) DO (
	set CLASSPATH=!CLASSPATH!;%%A
)

FOR %%A IN (%OH_LIB%\h8\*.jar) DO (
	set CLASSPATH=!CLASSPATH!;%%A
)

FOR %%A IN (%OH_LIB%\dicom\*.jar) DO (
	set CLASSPATH=!CLASSPATH!;%%A
)

FOR %%A IN (%OH_LIB%\dicom\dcm4che\*.jar) DO (
	set CLASSPATH=!CLASSPATH!;%%A
)

FOR %%A IN (%OH_LIB%\dicom\jai\*.jar) DO (
	set CLASSPATH=!CLASSPATH!;%%A
)

set CLASSPATH=%CLASSPATH%;%OH_BUNDLE%
set CLASSPATH=%CLASSPATH%;%OH_RESOURCE%
set CLASSPATH=%CLASSPATH%;%OH_BIN%;%OH_BIN%\OH.jar

IF (%PROCESSOR_ARCHITECTURE%)==(AMD64) (set NATIVE_PATH=%OH_LIB%\native\Win64) ELSE (set NATIVE_PATH=%OH_LIB%\native\Windows)

cd /d %OH_HOME%\

REM set JAVA=C:\PROGRA~2\Java\jdk1.6.0_29\bin\java.exe
@echo on
REM start /min %JAVA_HOME%\java -showversion -Djava.library.path=%NATIVE_PATH% -classpath %CLASSPATH% org.isf.menu.gui.Menu
start /min %JAVA% -showversion -Djava.library.path=%NATIVE_PATH% -classpath %CLASSPATH% org.isf.menu.gui.Menu