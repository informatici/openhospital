@echo off
REM # Open Hospital (www.open-hospital.org)
REM # Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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

REM ################### Script configuration ###################
REM
REM launch oh.bat -h to see available options
REM 
REM -> default startup script called is oh-api.ps1 (powershell) <-
REM 

REM ############################################################

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

	shift
	goto :parse

:main
	REM ################### oh-api.ps1 ###################
	REM default startup script called: oh.ps1

	echo Starting OH with oh.ps1...

	REM launch powershell script
	powershell.exe  -ExecutionPolicy Bypass -File  ./oh-api.ps1
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

