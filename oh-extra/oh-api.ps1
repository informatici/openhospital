#%SystemRoot%\system32\WindowsPowerShell\v1.0\powershell.exe
#!/usr/bin/pwsh
#
# Open Hospital (www.open-hospital.org)
# Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
#
# Open Hospital is a free and open source software for healthcare data management.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# https://www.gnu.org/licenses/gpl-3.0-standalone.html
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#

<#

.SYNOPSIS
Open Hospital startup script - oh.ps1

.DESCRIPTION
The script is used to setup and launch Open Hospital in PORTABLE, CLIENT or SERVER mode or with Demo data.
It can also be used to perform some basic operations like saving or importing a database.

Open Hospital CLIENT | PORTABLE | SERVER
Usage: oh.ps1 [ -lang en|fr|it|es|pt|ar ] [default set to en]
              [ -mode PORTABLE|CLIENT|SERVER ]
              [ -loglevel INFO|DEBUG ] [default set to INFO]
              [ -generate_config on|off ]
              [ -interactive on|off ]

.EXAMPLE
./oh.ps1 -lang it -mode PORTABLE -loglevel DEBUG -interactive off -generate_config on

.NOTES
Developed by Informatici Senza Frontiere - 2022

.LINK
https://www.open-hospital.org

#>

#################### Script info and configuration - Do not edit #####################

######## set script DEBUG mode
# saner programming env: these switches turn some bugs into errors
#Set-PSDebug -Strict
# Clean all variables in IDE
#Remove-Variable * -ErrorAction SilentlyContinue; Remove-Module *; $error.Clear();

######## set minimum PowerShell version 
#Requires -Version 5.1

######## command line parameters
param ($lang, $mode, $loglevel, $generate_config, $interactive)
$script:OH_LANGUAGE=$lang
$script:OH_MODE=$mode
$script:LOG_LEVEL=$loglevel
$script:WRITE_CONFIG_FILES=$generate_config
$script:INTERACTIVE_MODE=$interactive

######## get script info
# determine script name and location for PowerShell
$script:SCRIPT_DIR = Split-Path $script:MyInvocation.MyCommand.Path
$script:SCRIPT_NAME = $MyInvocation.MyCommand.Name
$script:POWERSHELL_EXE = (get-command PowerShell.exe).Path

######## global preferences
# disable progress bar
$global:ProgressPreference= 'SilentlyContinue'

######################## Script configuration #######################
#
# Interactive mode
# set INTERACTIVE_MODE to "off" to launch oh.ps1 without calling the user
# interaction menu (script_menu). Useful if automatic startup of OH is needed.
# In order to use this mode, setup all the OH configuration variables in the script
# or pass arguments via command line.
#$script:INTERACTIVE_MODE="on"
#
# set WRITE_CONFIG_FILES=on "on" to force generation / overwriting of OH configuration files:
# data/conf/my.cnf and oh/rsc/*.properties files will be regenerated from the original .dist files
# with the settings defined in this script.
#
# Default is set to "off": configuration files will not be regenerated or overwritten if already present.
#
#$script:WRITE_CONFIG_FILES="off"

##################### OH general configuration ####################

# -> OH_PATH is the directory where Open Hospital files are located
# OH_PATH="c:\Users\OH\OpenHospital\oh-1.12"

# set OH mode to PORTABLE | CLIENT | SERVER - default set to PORTABLE
#$script:OH_MODE="PORTABLE"

# language setting - default set to en
$script:OH_LANGUAGE_LIST="en|fr|es|it|pt|ar"
$script:OH_LANGUAGE="en" # default

# single / multiuser - set "yes" for single user configuration
$script:OH_SINGLE_USER="no"

# set log level to INFO | DEBUG - default set to INFO
#$script:LOG_LEVEL="INFO"

# set DEMO_DATA to on to enable demo database loading - default set to off
# ---> Warning <--- __requires deletion of all portable data__
$script:DEMO_DATA="off"
$script:DEMO_DATABASE="ohdemo"

# set JAVA_BIN 
# Uncomment this if you want to use system wide JAVA
#$script:JAVA_BIN="C:\Program Files\JAVA\bin\java.exe"

##################### Database configuration #######################
$script:DATABASE_SERVER="127.0.0.1"
$script:DATABASE_PORT=3306
$script:DATABASE_ROOT_PW="tmp2021oh111"
$script:DATABASE_NAME="oh"
$script:DATABASE_USER="isf"
$script:DATABASE_PASSWORD="isf123"

#######################  OH configuration  #########################
$script:DICOM_MAX_SIZE="4M"
$script:DICOM_STORAGE="FileSystemDicomManager" # SqlDicomManager
$script:DICOM_DIR="data/dicom_storage"

# path and directories
$script:OH_DIR="oh"
$script:OH_DOC_DIR="../doc"
$script:CONF_DIR="data/conf"
$script:DATA_DIR="data/db"
$script:PHOTO_DIR="data/photo"
$script:BACKUP_DIR="data/dump"
$script:LOG_DIR="data/log"
$script:SQL_DIR="sql"
$script:SQL_EXTRA_DIR="sql/extra"
$script:TMP_DIR="tmp"

# logging
$script:LOG_FILE="startup.log"
$script:LOG_FILE_ERR="startup_error.log"
$script:OH_LOG_FILE="openhospital.log"
$script:API_LOG_FILE="api.log"
$script:API_ERR_LOG_FILE="api_error.log"

# SQL creation files
#$script:DB_CREATE_SQL="create_all_en.sql" # default to en
$script:DB_DEMO="create_all_demo.sql"

######################## Other settings ########################
# available languages - do not modify
$script:languagearray= @("en","fr","es","it","pt","ar")

# date format
$script:DATE= Get-Date -Format "yyyy-MM-dd_HH-mm-ss"

# downloaded file extension
$script:EXT="zip"

# mysql configuration file
$script:MYSQL_CONF_FILE="my.cnf"

# OH files
$script:OH_GUI="OH-gui.jar"
$script:OH_SETTINGS="settings.properties"
$script:DATABASE_SETTINGS="database.properties"
$script:IMAGING_SETTINGS="dicom.properties"
$script:LOG4J_SETTINGS="log4j.properties"
$script:API_SETTINGS="application.properties"

# help file
$script:HELP_FILE="OH-readme.txt"

# set default database name
$script:DEFAULT_DATABASE_NAME="$DATABASE_NAME"
# set default data base_dir
$script:DEFAULT_DATADIR="$DATA_DIR"

############## Architecture and external software ##############

######## MariaDB/MySQL Software
# MariaDB version
$script:MYSQL_VERSION="10.6.12"
$script:MYSQL32_VERSION="10.6.5"

######## define system and software architecture
$script:ARCH=$env:PROCESSOR_ARCHITECTURE

$32archarray=@("386","486","586","686","x86","i86pc")
$64archarray=@("amd64","AMD64","x86_64")

if ($64archarray -contains "$ARCH") {
	$script:JAVA_ARCH=64;
	$script:MYSQL_ARCH="x64";
	$script:JAVA_PACKAGE_ARCH="x64";
}
elseif ($32archarray -contains "$ARCH") {
	$script:JAVA_ARCH=32;
	$script:MYSQL_ARCH=32;
	$script:MYSQL_VERSION=$script:MYSQL32_VERSION;
	$script:JAVA_PACKAGE_ARCH="i686";
}
else {
	Write-Host "Unknown architecture: $ARCH. Exiting." -ForegroundColor Red
	Read-Host; exit 1
}


# set MariaDB download URL / package
$script:MYSQL_URL="https://archive.mariadb.org/mariadb-$script:MYSQL_VERSION/win$script:MYSQL_ARCH-packages/"
$script:MYSQL_DIR="mariadb-$script:MYSQL_VERSION-win$script:MYSQL_ARCH"
$script:MYSQL_NAME="MariaDB" # For console output - MariaDB/MYSQL_NAME

######## JAVA Software
######## JAVA 64bit - default architecture
### JRE 11 - openjdk
#$script:JAVA_URL="https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/"
#$script:JAVA_DISTRO="OpenJDK11U-jre_x64_windows_hotspot_11.0.11_9"
#$script:JAVA_DIR="jdk-11.0.11+9-jre"

### JRE 11 - zulu distribution
$script:JAVA_URL="https://cdn.azul.com/zulu/bin"
$script:JAVA_DISTRO="zulu11.62.17-ca-jre11.0.18-win_$JAVA_PACKAGE_ARCH"

# workaround for JRE 11 - 32bit
#	if ( $JAVA_ARCH -eq "32" ) {
#	$script:JAVA_DISTRO="zulu11.58.25-ca-jre11.0.16.1-win_$JAVA_PACKAGE_ARCH"
#}

$script:JAVA_DIR=$JAVA_DISTRO

######################## DO NOT EDIT BELOW THIS LINE ########################

######################## Functions ########################

######## User input / option parsing

function script_menu {
	# show menu
	# Clear-Host # clear console
	Write-Host " -----------------------------------------------------------------"
	Write-Host "|                                                                 |"
	Write-Host "|                    Open Hospital - $OH_VERSION                       |"
	Write-Host "|                                                                 |"
	Write-Host " -----------------------------------------------------------------"
	Write-Host " arch $ARCH | lang $OH_LANGUAGE | mode $OH_MODE | log level $LOG_LEVEL | Demo $DEMO_DATA"
	Write-Host " -----------------------------------------------------------------"
	Write-Host " API server set to $API_SERVER"
	Write-Host " -----------------------------------------------------------------"
	Write-Host "   A    toggle API server - EXPERIMENTAL"
	Write-Host "   C    set OH in CLIENT mode"
	Write-Host "   P    set OH in PORTABLE mode"
	Write-Host "   S    set OH in SERVER mode (portable)"
	Write-Host "   l    set language: $OH_LANGUAGE_LIST"
	Write-Host "   s    save OH configuration"
	Write-Host "   X    clean/reset OH installation"
	Write-Host "   v    show configuration"
	Write-Host "   q    quit"
	Write-Host ""
	Write-Host "   --------------------- "
	Write-Host "    advanced options"
	Write-Host ""
	Write-Host "   e    export/save OH database"
	Write-Host "   r    restore OH database"
	Write-Host "   d    toggle log level INFO/DEBUG"
	Write-Host "   D    initialize OH with Demo data"
	Write-Host "   G    setup GSM"
	Write-Host "   i    initialize/install OH database"
	Write-Host "   m    configure database connection manually"
	Write-Host "   t    test database connection (CLIENT mode only)"
	Write-Host "   u    create Desktop shortcut with current params"
	Write-Host ""
	Write-Host "   h    show help"
	Write-Host ""
}

###################################################################
function get_confirmation ($arg) {
	$choice = Read-Host -Prompt "(y/n)? "
	switch ("$choice") {
		"y"  { "yes"; break }
		"n"  { "Exiting.";
			Read-Host;
			if ( $arg -eq 1 ) {
				parse_user_input;
			}
			
			exit 0;
			}
		default { "Invalid choice. Exiting.";
			Read-Host;
			if ( $arg -eq 1 ) {
				parse_user_input;
			}
			exit 1;
			}
	}
}

###################################################################
function set_path {
	# get current directory
	$script:CURRENT_DIR=Get-Location | select -ExpandProperty Path
	# set OH_PATH if not defined
	if ( ! $OH_PATH ) {
		Write-Host "Info: OH_PATH not defined - setting to script path"
		$script:OH_PATH=$PSScriptRoot
		if ( !(Test-Path "$OH_PATH/$SCRIPT_NAME" -PathType leaf) ) {
			Write-Host "Error - $SCRIPT_NAME not found in the current PATH. Please browse to the directory where Open Hospital was unzipped or set up OH_PATH properly." -ForegroundColor Yellow
			Read-Host; exit 1
		}
	}
	
}

###################################################################
function read_settings {
	# check and read OH version file
	if ( Test-Path "$OH_PATH/$OH_DIR/rsc/version.properties" -PathType leaf ) {
		# read Open Hospital Version
		Get-Content $OH_PATH\$OH_DIR\rsc\version.properties | Where-Object {$_.length -gt 0} | Where-Object {!$_.StartsWith("#")} | ForEach-Object {
			$var = $_.Split('=',2).Trim()
			New-Variable -Force -Scope Private -Name $var[0] -Value $var[1] 
		}
		$script:OH_VERSION="$VER_MAJOR.$VER_MINOR.$VER_RELEASE"
	}
	else {
		Write-Host "Error: Open Hospital not found. Exiting" -ForegroundColor Red
		Read-Host; exit 1
	}

	# check for OH settings file and read values
	if ( Test-Path "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS" -PathType leaf ) {
		Write-Host "Reading OH settings file..."
		$oh_settings = [pscustomobject](Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS" -Raw | ConvertFrom-StringData)
		
		$script:OH_MODE=$oh_settings.MODE
		$script:OH_LANGUAGE=$oh_settings.LANGUAGE
		$script:OH_SINGLE_USER=$oh_settings.SINGLE_USER
		$script:OH_DOC_DIR=$oh_settings.OH_DOC_DIR
		$script:DEMO_DATA=$oh_settings.DEMODATA
	}
		
	# check for database settings file and read values
	if ( Test-Path "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS" -PathType leaf ) {
		Write-Host "Reading database settings file..."
		$db_settings = [pscustomobject](Get-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS" -Raw | ConvertFrom-StringData)

		$DATABASE_URL=$db_settings."jdbc.url"
		$script:DATABASE_SERVER=$DATABASE_URL.TrimStart("jdbc:mysql://").Split(":",2)[0]
		$script:DATABASE_PORT=$DATABASE_URL.TrimStart("jdbc:mysql://").Split(":",2)[1].Split("/",2)[0]
		$script:DATABASE_NAME=$DATABASE_URL.TrimStart("jdbc:mysql://").Split(":",2)[1].Split("/",2)[1]
		$script:DATABASE_USER=$db_settings."jdbc.username"
		$script:DATABASE_PASSWORD=$db_settings."jdbc.password"
	}
	else {
		Write-Host "Warning: configuration file $DATABASE_SETTINGS not found." -ForegroundColor Yellow
	}
}

###################################################################
function set_defaults {
        # set default values for script variables
	# interactive mode - set default to on
	if ( [string]::IsNullOrEmpty($INTERACTIVE_MODE) ) {
		$script:INTERACTIVE_MODE="on"
	}

	# config files generation - set default to off
	if ( [string]::IsNullOrEmpty($WRITE_CONFIG_FILES) ) {
		$script:WRITE_CONFIG_FILES="off"
	}

	# OH mode - set default to PORTABLE
	if ( [string]::IsNullOrEmpty($OH_MODE) ) {
		$script:OH_MODE="PORTABLE"
	}
	
	# OH language - set default to en
	if ( [string]::IsNullOrEmpty($OH_LANGUAGE) ) {
		$script:OH_LANGUAGE="en"
	}

	# set database creation script in chosen language
	if ( [string]::IsNullOrEmpty($DB_CREATE_SQL) ) {
		$script:DB_CREATE_SQL="create_all_$OH_LANGUAGE.sql"
	}

	# log level - set default to INFO
	if ( [string]::IsNullOrEmpty($LOG_LEVEL) ) {
		$script:LOG_LEVEL="INFO"
	}
	
	# single / multiuser - set "yes" for single user configuration
	if ( [string]::IsNullOrEmpty($OH_SINGLE_USER) ) {
		$script:OH_SINGLE_USER="no"
	}

	# demo data - set default to off
	if ( [string]::IsNullOrEmpty($DEMO_DATA) ) {
		$script:DEMO_DATA="off"
	}
	
	# api server - set default to off
	if ( [string]::IsNullOrEmpty($API_SERVER) ) {
		$script:API_SERVER="off"
	}

	# set escaped path (/ in place of \)
	$script:OH_PATH_SUBSTITUTE=$OH_PATH -replace "\\", "/"
}

###################################################################
function set_db_name {
	# set DATA_DIR with db name
	$script:DATA_DIR="$DEFAULT_DATADIR/$DATABASE_NAME"
	#
	# set escaped values (/ in place of \)
	$script:DATA_DIR=$DATA_DIR -replace "\\", "/"
}

###################################################################
function set_oh_mode {
	# if $OH_SETTINGS is present set OH mode
	if ( Test-Path "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS" -PathType leaf ) {
		Write-Host "Configuring OH mode..."
	        ######## $OH_SETTINGS language configuration
		Write-Host "Setting OH mode to $OH_MODE in OH configuration files-> $OH_SETTINGS..."
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS") -replace('^(MODE.+)',"MODE=$OH_MODE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
	}
	else {
		Write-Host "Warning: $OH_SETTINGS file not found." -ForegroundColor Yellow
	}
	Write-Host "OH mode set to $OH_MODE." -ForeGroundcolor Green
}

###################################################################
function set_demo_data {
	# set database name for demo data
	switch -CaseSensitive( $script:DEMO_DATA ) {
	"on"	{ # 
		$script:DATABASE_NAME=$DEMO_DATABASE
		}
	"off"	{ # 
		$script:DATABASE_NAME="$script:DEFAULT_DATABASE_NAME"
		}
	}
}

###################################################################
function set_language {
	# check for valid language selection
	if ($script:languagearray -contains "$OH_LANGUAGE") {
		# set localized database creation script
		$Script:DB_CREATE_SQL="create_all_$OH_LANGUAGE.sql"
	}
	else {
		Write-Host "Invalid language option: $OH_LANGUAGE. Exiting." -ForegroundColor Red
		Read-Host; exit 1
	}
	# set database creation script in chosen language
	$script:DB_CREATE_SQL="create_all_$OH_LANGUAGE.sql"

	# if $OH_SETTINGS is present set language
	if ( Test-Path "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS" -PathType leaf ) {
		Write-Host "Configuring OH language..."
	        ######## $OH_SETTINGS language configuration
		Write-Host "Setting language to $OH_LANGUAGE in OH configuration files-> $OH_SETTINGS..."
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS") -replace('^(LANGUAGE.+)',"LANGUAGE=$OH_LANGUAGE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
		Write-Host "Language set to $OH_LANGUAGE."
	}
	else {
		Write-Host "Warning: $OH_SETTINGS file not found." -ForegroundColor Yellow
	}
}


###################################################################
function set_log_level {
	if ( Test-Path "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS" -PathType leaf ) {
		######## $LOG4J_SETTINGS log_level configuration
		Write-Host "Setting log level in OH configuration file -> $LOG4J_SETTINGS..."
		switch -CaseSensitive( $script:LOG_LEVEL ) {
		###################################################
		"INFO"	{
			(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("DEBUG","$LOG_LEVEL") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
			break;
			}
		"DEBUG"	{
			(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("INFO","$LOG_LEVEL") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
			}
		default {
			Write-Host "Invalid log level option: $LOG_LEVEL." -ForegroundColor Red
			exit 2;
			}
		}
		Write-Host "Log level set to $script:LOG_LEVEL" -ForeGroundcolor Green
	}
	else {
		Write-Host "Warning: $LOG4J_SETTINGS file not found." -ForegroundColor Yellow
	}
}

###################################################################
function initialize_dir_structure {
	# create directory structure
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$TMP_DIR") > $null
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$LOG_DIR") > $null
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$DICOM_DIR") > $null
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$PHOTO_DIR") > $null
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$BACKUP_DIR") > $null
}

###################################################################
function download_file ($download_url,$download_file) {
	Write-Host "Downloading $download_file from $download_url..."
	try {
        	$wc = new-object System.Net.WebClient
	        $wc.DownloadFile("$download_url\$download_file","$OH_PATH\$download_file")
	}
	catch [System.Net.WebException],[System.IO.IOException] {
		Write-Host "Unable to download $download_file from $download_url" -ForegroundColor Red
		Read-Host; exit 1;
	}
	catch {
		Write-Host "An error occurred. Exiting." -ForegroundColor Red
		Read-Host; exit 1;
	}
}

###################################################################
function create_desktop_shortcut {
	Write-Host "Creating/updating OH shortcut on Desktop..."
	
	$WshShell = New-Object -comObject WScript.Shell

	#$Shortcut = $WshShell.CreateShortcut("$env:ProgramData\Microsoft\Windows\Start Menu\Programs\OpenHospital.lnk")

	$Shortcut = $WshShell.CreateShortcut("$Home\Desktop\OpenHospital.lnk")
	$Shortcut.TargetPath = "$POWERSHELL_EXE" # $SCRIPT_DIR\$SCRIPT_NAME"
	$Shortcut.Arguments = "-ExecutionPolicy Bypass $SCRIPT_DIR\$SCRIPT_NAME -interactive off -mode $OH_MODE -lang $OH_LANGUAGE"
	$Shortcut.WorkingDirectory = "$OH_PATH"
	$ShortCut.IconLocation = "$OH_PATH\oh.ico"
	$Shortcut.Save()
	Write-Host "Done!"
}

###################################################################
function java_lib_setup {
	# NATIVE LIB setup
	switch ( "$JAVA_ARCH" ) {
		"64" { $script:NATIVE_LIB_PATH="$OH_PATH\$OH_DIR\lib\native\Win64" }
		"32" { $script:NATIVE_LIB_PATH="$OH_PATH\$OH_DIR\lib\native\Windows" }
	}

	# CLASSPATH setup
	# include OH jar file
	$script:OH_CLASSPATH="$OH_PATH\$OH_DIR\bin\$OH_GUI"

	# include all needed directories
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\bundle\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rpt_base\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rpt_extra\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rpt_stat\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rsc\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rsc\icons\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rsc\images\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\lib\"
	
	# include all jar files under lib\
	$script:jarlist= Get-ChildItem "$OH_PATH\$OH_DIR\lib" -Filter *.jar |  % { $_.FullName }
	ForEach( $n in $jarlist ) {
		$script:OH_CLASSPATH="$n;$OH_CLASSPATH"
	}
	
}

###################################################################
function java_check {
	# check if JAVA_BIN is already set and it exists
	if ( !( $JAVA_BIN ) -or !(Test-Path $JAVA_BIN -PathType leaf ) ) {
        	# set default
        	Write-Host "Setting default JAVA..."
		$script:JAVA_BIN="$OH_PATH\$JAVA_DIR\bin\java.exe"
	}

	# if JAVA_BIN is not found download JRE
	if ( !(Test-Path $JAVA_BIN  -PathType leaf ) ) {
        	if ( !(Test-Path "$OH_PATH/$JAVA_DISTRO.$EXT" -PathType leaf ) ) {
			Write-Host "Warning - JAVA not found. Do you want to download it?" -ForegroundColor Yellow
			get_confirmation;
			# Download java binaries
			download_file "$JAVA_URL" "$JAVA_DISTRO.$EXT"
		}
		Write-Host "Unpacking $JAVA_DISTRO..."
		try {
			Expand-Archive "$OH_PATH\$JAVA_DISTRO.$EXT" -DestinationPath "$OH_PATH\" -Force
		}
		catch {
			Write-Host "Error unpacking Java. Exiting." -ForegroundColor Red
			Read-Host; exit 1
		}
		Write-Host "Java unpacked successfully!"
		Write-Host "Removing downloaded file..."
		Remove-Item "$OH_PATH\$JAVA_DISTRO.$EXT"
        	Write-Host "Done!"
	}
	Write-Host "JAVA found!"
	Write-Host "Using $JAVA_BIN"
}

###################################################################
function mysql_check {
	if (  !(Test-Path "$OH_PATH/$MYSQL_DIR") ) {
		if ( !(Test-Path "$OH_PATH/$MYSQL_DIR.$EXT" -PathType leaf) ) {
			Write-Host "Warning - $MYSQL_NAME not found. Do you want to download it?" -ForegroundColor Yellow
			get_confirmation;
			# Downloading mysql binary
			download_file "$MYSQL_URL" "$MYSQL_DIR.$EXT" 
		}
		Write-Host "Unpacking $MYSQL_DIR..."
		try {
			Expand-Archive "$OH_PATH\$MYSQL_DIR.$EXT" -DestinationPath "$OH_PATH\" -Force
		}
		catch {
			Write-Host "Error unpacking $MYSQL_NAME. Exiting." -ForegroundColor Red
			Read-Host; exit 1
		}
	        Write-Host "$MYSQL_NAME unpacked successfully!"
		Write-Host "Removing downloaded file..."
		Remove-Item "$OH_PATH\$MYSQL_DIR.$EXT"
        	Write-Host "Done!"
	}
	# check for mysqld binary
	if (Test-Path "$OH_PATH/$MYSQL_DIR/bin/mysqld.exe" -PathType leaf) {
        	Write-Host "$MYSQL_NAME found!"
		Write-Host "Using $MYSQL_DIR"
	}
	else {
		Write-Host "Error: $MYSQL_NAME not found. Exiting." -ForegroundColor Red
		Read-Host; exit 1
	}
}

###################################################################
function config_database {
	Write-Host "Checking for $MYSQL_NAME config file..."
	if ( ($script:WRITE_CONFIG_FILES -eq "on") -or !(Test-Path "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE" -PathType leaf) ) {
	if (Test-Path "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE" -PathType leaf) { mv -Force "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE" "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE.old" }

		# find a free TCP port to run MariaDB/MySQL starting from the default port
		Write-Host "Looking for a free TCP port for $MYSQL_NAME database..."

		$ProgressPreference = 'SilentlyContinue'

		### windows 10 only ####
		#while ( Test-NetConnection $script:DATABASE_SERVER -Port $DATABASE_PORT -InformationLevel Quiet -ErrorAction SilentlyContinue -WarningAction SilentlyContinue ){
		#	Write-Host "Testing TCP port $DATABASE_PORT...."
		#      	$script:DATABASE_PORT++
		#}
		### end windows 10 only ###

		# convert port to integer
		$script:DATABASE_PORT=[int]$DATABASE_PORT
		### windows 7/10 ###
		do {
			$socktest = (New-Object System.Net.Sockets.TcpClient).ConnectAsync("$DATABASE_SERVER", $DATABASE_PORT).Wait(1000) 
			Write-Host "Testing TCP port $DATABASE_PORT...."
			$script:DATABASE_PORT++
		}
		while ( $socktest )
		$script:DATABASE_PORT--
		### end windows 7/10 ###

		Write-Host "Found TCP port $DATABASE_PORT!"

		Write-Host "Writing $MYSQL_NAME config files..."
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE.dist").replace("DICOM_SIZE","$DICOM_MAX_SIZE") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE").replace("OH_PATH_SUBSTITUTE","$OH_PATH_SUBSTITUTE") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE").replace("DATABASE_SERVER","$DATABASE_SERVER") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE").replace("DATABASE_PORT","$DATABASE_PORT") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE").replace("MYSQL_DISTRO","$MYSQL_DIR") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE").replace("DATA_DIR","$DATA_DIR") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE").replace("TMP_DIR","$TMP_DIR") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
		(Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE").replace("LOG_DIR","$LOG_DIR") | Set-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"
	}
}

###################################################################
function initialize_database {
	# create data directory
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$DATA_DIR") > $null
	# inizialize MariaDB/MySQL
	Write-Host "Initializing $MYSQL_NAME database on port $DATABASE_PORT..."
	switch -Regex ( $MYSQL_DIR ) {
		"mariadb" {
			try {
				Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysql_install_db.exe" -ArgumentList ("--datadir=`"$OH_PATH\$DATA_DIR`" --password=$DATABASE_ROOT_PW") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
	        	}
			catch {
				Write-Host "Error: $MYSQL_NAME initialization failed! Exiting." -ForegroundColor Red
				Read-Host; exit 2
			}
		}
		"mysql" {
			try {
				Start-Process "$OH_PATH\$MYSQL_DIR\bin\mysqld.exe" -ArgumentList ("--initialize-insecure --basedir=`"$OH_PATH\$MYSQL_DIR`" --datadir=`"$OH_PATH\$DATA_DIR`" ") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"; 
			}
			catch {
				Write-Host "Error: $MYSQL_NAME initialization failed! Exiting." -ForegroundColor Red
				Read-Host; exit 2
			}
		}
	}
}

###################################################################
function start_database {
	Write-Host "Checking if $MYSQL_NAME is running..."
	if ( ( Test-Path "$OH_PATH/$TMP_DIR/mysql.sock" ) -or ( Test-Path "$OH_PATH/$TMP_DIR/mysql.pid" ) ) {
		Write-Host "$MYSQL_NAME already running ! Exiting."
		exit 1
	}

	Write-Host "Starting $MYSQL_NAME server... "
	try {
		Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysqld.exe" -ArgumentList ("--defaults-file=`"$OH_PATH\$CONF_DIR\$MYSQL_CONF_FILE`" --tmpdir=`"$OH_PATH\$TMP_DIR`" --standalone") -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
		Start-Sleep -Seconds 2
	}
	catch {
		Write-Host "Error: $MYSQL_NAME server not started! Exiting." -ForegroundColor Red
		Read-Host; exit 2
	}

	# wait till the MariaDB/MySQL socket file is created -> TO BE IMPLEMENTED
	# while ( -e $OH_PATH/$MYSQL_SOCKET ); do sleep 1; done
	# # Wait till the MariaDB/MySQL tcp port is open
	# until nc -z $DATABASE_SERVER $DATABASE_PORT; do sleep 1; done

	Write-Host "$MYSQL_NAME server started! "
}

###################################################################
function set_database_root_pw {
	# if using MySQL root password need to be set
	switch -Regex ( $MYSQL_DIR ) {
		"mysql" {
		Write-Host "Setting MySQL root password..."
        $SQLCOMMAND=@"
        -u root --skip-password -h $DATABASE_SERVER --port=$DATABASE_PORT --protocol=tcp -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '$DATABASE_ROOT_PW';"
"@
			try {
				Start-Process -FilePath "$OH_PATH/$MYSQL_DIR/bin/mysql.exe" -ArgumentList ("$SQLCOMMAND") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
			}
			catch {
				Write-Host "Error: MySQL root password not set! Try resetting installation with option [X]. Exiting." -ForegroundColor Red
				shutdown_database;
				Read-Host; exit 2
			}
		}
	}
}

###################################################################
function import_database {
	Write-Host "Creating OH Database..."
	# create OH database and user
	
    $SQLCOMMAND=@"
    -u root -p$DATABASE_ROOT_PW -h $DATABASE_SERVER --port=$DATABASE_PORT --protocol=tcp -e "CREATE DATABASE $DATABASE_NAME CHARACTER SET utf8; CREATE USER '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; CREATE USER '$DATABASE_USER'@'%' IDENTIFIED BY '$DATABASE_PASSWORD'; GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'localhost'; GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'%';"
"@
	try {
		Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysql.exe" -ArgumentList ("$SQLCOMMAND") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
 	}
	catch {
		Write-Host "Error: Database creation failed! Exiting." -ForeGroundColor Red
		shutdown_database;
		Read-Host; exit 2
	}
	# check for database creation script
	if (Test-Path "$OH_PATH/$SQL_DIR/$DB_CREATE_SQL" -PathType leaf) {
 		Write-Host "Using SQL file $SQL_DIR\$DB_CREATE_SQL..."
	}
	else {
		Write-Host "Error: No SQL file found! Exiting." -ForeGroundColor Red
		shutdown_database;
		Read-Host; exit 2
	}

	# create OH database structure
	Write-Host "Importing database schema..."

	cd "./$SQL_DIR"

    $SQLCOMMAND=@"
   --local-infile=1 -u root -p$DATABASE_ROOT_PW -h $DATABASE_SERVER --port=$DATABASE_PORT --protocol=tcp $DATABASE_NAME -e "source ./$DB_CREATE_SQL"
"@
	try {
		Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysql.exe" -ArgumentList ("$SQLCOMMAND") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
 	}
	catch {
		Write-Host "Error: Database not imported! Exiting." -ForeGroundColor Red
		shutdown_database;
		cd "$CURRENT_DIR"
		Read-Host; exit 2
	}
	Write-Host "Database imported!"
	cd "$OH_PATH"
}

###################################################################
function dump_database {
	# save OH database if existing
	if (Test-Path "$OH_PATH/$MYSQL_DIR/bin/mysqldump.exe" -PathType leaf) {
		[System.IO.Directory]::CreateDirectory("$OH_PATH/$BACKUP_DIR") > $null
		Write-Host "Dumping $MYSQL_NAME database..."	
        $SQLCOMMAND=@"
    --skip-extended-insert -u root --password=$DATABASE_ROOT_PW -h $DATABASE_SERVER --port=$DATABASE_PORT --protocol=tcp $DATABASE_NAME
"@
	Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysqldump.exe" -ArgumentList ("$SQLCOMMAND") -Wait -NoNewWindow -RedirectStandardOutput "$OH_PATH\$BACKUP_DIR\mysqldump_$DATE.sql" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"	
	}
	else {
		Write-Host "Error: No mysqldump utility found! Exiting." -ForegroundColor Red
		shutdown_database;
		cd "$CURRENT_DIR"
		Read-Host; exit 2
	}
	Write-Host "$MYSQL_NAME dump file $BACKUP_DIR/mysqldump_$DATE.sql completed!" -ForegroundColor Green
}

###################################################################
function shutdown_database {
	if ( !( $OH_MODE -eq "CLIENT" ) ) {
		Write-Host "Shutting down $MYSQL_NAME..."
		Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysqladmin.exe" -ArgumentList ("-u root -p$DATABASE_ROOT_PW --host=$DATABASE_SERVER --port=$DATABASE_PORT --protocol=tcp shutdown") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
		# wait till the $MYSQL_NAME socket file is removed -> TO BE IMPLEMENTED
		# while ( -e $OH_PATH/$MYSQL_SOCKET ); do sleep 1; done
		Start-Sleep -Seconds 2
		Write-Host "$MYSQL_NAME stopped!"
	}

	else { # do nothing
	}
}

###################################################################
function test_database_connection {
	# test if mysql client is available
	if (Test-Path "$OH_PATH/$MYSQL_DIR/bin/mysql.exe" -PathType leaf) {
		# test connection to the OH MariaDB/MySQL database
		Write-Host "Testing database connection..."
		try {
		Start-Process -FilePath ("$OH_PATH\$MYSQL_DIR\bin\mysql.exe") -ArgumentList ("--user=$DATABASE_USER --password=$DATABASE_PASSWORD --host=$DATABASE_SERVER --port=$DATABASE_PORT --protocol=tcp -e $([char]34)USE $DATABASE_NAME$([char]34) " ) -Wait -NoNewWindow
		}
		catch {
			Write-Host "Error: can't connect to database! Exiting." -ForegroundColor Red
			Read-Host; exit 2
		}
		# temporary disabled - catch not working
		# Write-Host "Database connection successfully established!"
	}
	else {
		Write-Host "Can't test database connection..." 
	}
}

###################################################################
function write_api_config_file {
	######## application.properties setup - OH API server
	if ( ($script:WRITE_CONFIG_FILES -eq "on") -or !(Test-Path "$OH_PATH/$OH_DIR/rsc/$API_SETTINGS" -PathType leaf) ) {
		if (Test-Path "$OH_PATH/$OH_DIR/rsc/$API_SETTINGS" -PathType leaf) { mv -Force $OH_PATH/$OH_DIR/rsc/$API_SETTINGS $OH_PATH/$OH_DIR/rsc/$API_SETTINGS.old }
		# generate OH API token and save to settings file
		$JWT_TOKEN_SECRET=( -join ($(for($i=0; $i -lt 64; $i++) { ((65..90)+(97..122)+(".")+("!")+("?")+("&") | Get-Random | % {[char]$_}) })) )
		Write-Host "Writing OH API configuration file -> $API_SETTINGS..."
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$API_SETTINGS.dist").replace("JWT_TOKEN_SECRET","$JWT_TOKEN_SECRET") | Set-Content "$OH_PATH/$OH_DIR/rsc/$API_SETTINGS"
	}
}

###################################################################
function start_api_server {
	# check for configuration files
	if ( !( Test-Path "$OH_PATH/$OH_DIR/rsc/$API_SETTINGS" -PathType leaf )) {
		Write-Host "Error: missing $API_SETTINGS settings file. Exiting" -ForeGround Red
		exit 1;
	}
	Write-Host "------------------------"
	Write-Host "---- EXPERIMENTAL ------"
	Write-Host "------------------------"
	Write-Host "Starting API server..."
	Write-Host ""
	Write-Host "Connect to http://localhost:8080 for dashboard"
	Write-Host ""

        cd "$OH_PATH/$OH_DIR" # workaround for hard coded paths

	$JAVA_API_ARGS="-server -Xms64m -Xmx1024m -cp ./bin/openhospital-api-0.0.2.jar;./rsc;./static org.springframework.boot.loader.JarLauncher"

	Start-Process -FilePath "$JAVA_BIN" -ArgumentList $JAVA_API_ARGS -WindowStyle Hidden -RedirectStandardOutput "$OH_PATH/$LOG_DIR/$API_LOG_FILE" -RedirectStandardError "$OH_PATH/$LOG_DIR/$API_ERR_LOG_FILE"

        # $JAVA_BIN -client -Xms64m -Xmx1024m -cp "./bin/openhospital-api-0.0.2.jar:./rsc::./static" org.springframework.boot.loader.JarLauncher

#        if [ $? -ne 0 ]; then
#                echo "An error occurred while starting Open Hospital API. Exiting."
#                shutdown_database;
#                cd "$CURRENT_DIR"
#                exit 4
#        fi
        cd "$OH_PATH"
}

###################################################################
function write_config_files {
	# set up configuration files
	Write-Host "Checking for OH configuration files..."

	######## DICOM setup
	if ( ($script:WRITE_CONFIG_FILES -eq "on") -or !(Test-Path "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS" -PathType leaf) ) {
		if (Test-Path "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS" -PathType leaf) { mv -Force $OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS $OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS.old }
		Write-Host "Writing OH configuration file -> $IMAGING_SETTINGS..."
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS.dist").replace("OH_PATH_SUBSTITUTE","$OH_PATH_SUBSTITUTE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS").replace("DICOM_DIR","$DICOM_DIR") | Set-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS").replace("DICOM_STORAGE","$DICOM_STORAGE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS").replace("DICOM_SIZE","$DICOM_MAX_SIZE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS"
	}

	######## $LOG4J_SETTINGS setup
	if ( ($script:WRITE_CONFIG_FILES -eq "on") -or !(Test-Path "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS" -PathType leaf) ) {
		if (Test-Path "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS" -PathType leaf) { mv -Force $OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS $OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS.old }
		Write-Host "Writing OH configuration file -> $LOG4J_SETTINGS..."
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS.dist").replace("DBSERVER","$DATABASE_SERVER") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("DBPORT","$DATABASE_PORT") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("DBUSER","$DATABASE_USER") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("DBPASS","$DATABASE_PASSWORD") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("DBNAME","$DATABASE_NAME") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("LOG_LEVEL","$LOG_LEVEL") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS").replace("LOG_DEST","../$LOG_DIR/$OH_LOG_FILE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"
	}

	######## $DATABASE_SETTINGS setup 
	if ( ($script:WRITE_CONFIG_FILES -eq "on") -or !(Test-Path "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS" -PathType leaf) ) {
		if (Test-Path "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS" -PathType leaf) { mv -Force $OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS $OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS.old }
		Write-Host "Writing OH database configuration file -> $DATABASE_SETTINGS..."
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS.dist").replace("DBSERVER","$DATABASE_SERVER") | Set-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS").replace("DBPORT","$DATABASE_PORT") | Set-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS").replace("DBUSER","$DATABASE_USER") | Set-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS").replace("DBPASS","$DATABASE_PASSWORD") | Set-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS"
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS").replace("DBNAME","$DATABASE_NAME") | Set-Content "$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS"

		# direct creation of $DATABASE_SETTINGS - deprecated
		#Set-Content -Path $OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS -Value "jdbc.url=jdbc:mysql://"$DATABASE_SERVER":$DATABASE_PORT/$DATABASE_NAME"
		#Add-Content -Path $OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS -Value "jdbc.username=$DATABASE_USER"
		#Add-Content -Path $OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS -Value "jdbc.password=$DATABASE_PASSWORD"
	}

	######## $OH_SETTINGS setup
	if ( ($script:WRITE_CONFIG_FILES -eq "on") -or !(Test-Path "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS" -PathType leaf) ) {
		if (Test-Path "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS" -PathType leaf) { mv -Force $OH_PATH/$OH_DIR/rsc/$OH_SETTINGS $OH_PATH/$OH_DIR/rsc/$OH_SETTINGS.old }
		Write-Host "Writing OH configuration file -> $OH_SETTINGS..."
		# set OH mode
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS.dist").replace("OH_MODE","$OH_MODE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
		# set LANGUAGE
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS").replace("OH_LANGUAGE","$OH_LANGUAGE") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
		# set DOC_DIR
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS").replace("OH_DOC_DIR","$OH_DOC_DIR") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
		# set PHOTO_DIR
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS").replace("PHOTO_DIR","$PHOTO_DIR") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
		# set singleuser = yes / no
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS").replace("YES_OR_NO","$OH_SINGLE_USER") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
		# set DEMO DATA
		(Get-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS").replace("DEMODATA=off","DEMODATA=$DEMO_DATA") | Set-Content "$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"
	}
}

###################################################################
function clean_database {
	# kill mariadb/mysqld processes
	Write-Host "Killing mariadb/mysql processes..."
	Get-Process mysqld -ErrorAction SilentlyContinue | Stop-Process -PassThru
	# remove socket and pid file
	Write-Host "Removing socket and pid file..."
	$filetodel="$OH_PATH/$TMP_DIR/*"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	# remove database files
	Write-Host "Removing databases..."
	$filetodel="$OH_PATH/$DATA_DIR"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
}

###################################################################
function clean_conf_files {
	# remove configuration files - leave only .dist files
	Write-Host "Removing configuration files..."
	$filetodel="$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$OH_SETTINGS.old"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$DATABASE_SETTINGS.old"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$LOG4J_SETTINGS.old"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$IMAGING_SETTINGS.old"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$API_SETTINGS"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH/$OH_DIR/rsc/$API_SETTINGS.old"; if (Test-Path $filetodel -PathType leaf) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
}

###################################################################
function clean_log_files {
	# remove all log files
	Write-Host "Removing log files..."
	$filetodel="$OH_PATH/$LOG_DIR/*"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
}

###################################################################
function start_gui {
	Write-Host "Starting Open Hospital GUI..."
	# OH GUI launch
	cd "$OH_PATH\$OH_DIR" # workaround for hard coded paths

	#$JAVA_ARGS="-client -Dlog4j.configuration=`"`'$OH_PATH\$OH_DIR\rsc\$LOG4J_SETTINGS`'`" -Dsun.java2d.dpiaware=false -Djava.library.path=`"`'$NATIVE_LIB_PATH`'`" -cp `"`'$OH_CLASSPATH`'`" org.isf.menu.gui.Menu"

	# log4j configuration is now read directly
$JAVA_ARGS="-client -Xms64m -Xmx1024m -Dsun.java2d.dpiaware=false -Djava.library.path=`"$NATIVE_LIB_PATH`" -cp `"`'$OH_CLASSPATH`'`" org.isf.menu.gui.Menu"

	Start-Process -FilePath "$JAVA_BIN" -ArgumentList $JAVA_ARGS -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
	
	# go back to starting directory
	cd "$CURRENT_DIR"
}

###################################################################
function parse_user_input {
# If INTERACTIVE_MODE is set to "off" don't show menu
if ( $INTERACTIVE_MODE -eq "on" ) {
	do {
		script_menu;
		$option = Read-Host "Please select an option or press enter to start OH"
		switch -CaseSensitive ( "$option" ) {
		###################################################
		"A"	{ # toggle API server
			switch -CaseSensitive( $script:API_SERVER ) {
			"on"	{ # 
				$script:API_SERVER="off"
				}
			"off"	{ # 
				$script:API_SERVER="on"
				}
			}
			write_api_config_file;
			#Read-Host "Press any key to continue";
		}
		###################################################
		"C"	{ # start in CLIENT mode
			$script:OH_MODE="CLIENT"
			$script:DEMO_DATA="off"
			set_oh_mode;
			Read-Host "Press any key to continue";
		}
		###################################################
		"P"	{ # start in PORTABLE mode
			$script:OH_MODE="PORTABLE"
			set_oh_mode;
			Read-Host "Press any key to continue";
		}
		###################################################
		"S"	{ # start in SERVER (portable) mode
			$script:OH_MODE="SERVER"
			set_oh_mode;
			Read-Host "Press any key to continue";
		}
		###################################################
		"d"	{ # toggle debug mode
			switch -CaseSensitive( $script:LOG_LEVEL ) {
			"INFO"	{ # 
				$script:LOG_LEVEL="DEBUG"
				}
			"DEBUG"	{ # 
				$script:LOG_LEVEL="INFO"
				}
			}
			# create config files if not present
			#write_config_files;
			# set configuration
			set_log_level;
			Read-Host "Press any key to continue";
		}
		###################################################
		"D"	{ # demo mode 
			# exit if OH is configured in CLIENT mode
			if ( $OH_MODE -eq "CLIENT" ) {
				Write-Host "Error - OH_MODE set to CLIENT mode. Cannot run with Demo data. Exiting" -ForeGroundcolor Red
				Read-Host; exit 1;
			}
			# invert values if D is pressed
			switch -CaseSensitive( $script:DEMO_DATA ) {
			"on"	{ # 
				$script:DEMO_DATA="off"
				}
			"off"	{ # 
				$script:DEMO_DATA="on"
				}
			}
			# update configuration settings
			set_demo_data;
			set_db_name;

			$script:WRITE_CONFIG_FILES="on"; write_config_files;
			
			Read-Host "Press any key to continue";
		}
		###################################################
		"G"	{ # set up GSM 
			Write-Host "Setting up GSM..."
			java_check;
			java_lib_setup;
			Start-Process -FilePath "$JAVA_BIN" -ArgumentList ("-Djava.library.path=${NATIVE_LIB_PATH} -classpath $OH_CLASSPATH org.isf.utils.sms.SetupGSM $@ ") -Wait -NoNewWindow
			Write-Host "Done!"
			Read-Host "Press any key to continue";
		}
		###################################################
		"i"	{ # initialize/install OH database
			# set mode to CLIENT
			$OH_MODE="CLIENT"
			Write-Host "Do you want to initialize/install the OH database on:"
			Write-Host ""
			Write-Host " Database Server -> $DATABASE_SERVER"
			Write-Host " TCP port -> $DATABASE_PORT"
			Write-Host ""
			get_confirmation 1;
			initialize_dir_structure;
			set_language;
			mysql_check;
			# ask user for database root password
			$script:DATABASE_ROOT_PW = Read-Host "Please insert the MariaDB / MySQL database root password (root@$DATABASE_SERVER) -> "
			Write-Host "Installing the database....."
			Write-Host ""
			Write-Host " Database name -> $DATABASE_NAME"
			Write-Host " Database user -> $DATABASE_USER"
			Write-Host " Database password -> $DATABASE_PASSWORD"
			Write-Host ""
			import_database;
			test_database_connection;
			Write-Host "Done!"
			Read-Host "Press any key to continue";
		}
		###################################################
		"h"	{ # show help
			Get-content $HELP_FILE | more
			Read-Host "Press any key to continue";
		}
		###################################################
		"l"	{ # set language 
			$script:OH_LANGUAGE = Read-Host "Select language: $OH_LANGUAGE_LIST (default is en)"
			set_language;
			Read-Host "Press any key to continue";
		}
		###################################################
		"m"	{ # configure OH database connection manually
			$script:DEMO_DATA="off"
			#$script:OH_SINGLE_USER=Read-Host	"Please select Single user configuration (yes/no)" 
	                #### script:OH_SINGLE_USER=${OH_SINGLE_USER:-Off} # set default # TBD
			Write-Host 				""
			Write-Host 				"***** Database configuration *****"
			Write-Host 				""
			$script:DATABASE_SERVER=Read-Host	"Enter database server IP address [DATABASE_SERVER]"
			$script:DATABASE_PORT=Read-Host		"Enter database server TCP port [DATABASE_PORT]"
			$script:DATABASE_NAME=Read-Host		"Enter database database name [DATABASE_NAME]"
			$script:DATABASE_USER=Read-Host		"Enter database user name [DATABASE_USER]"
			$script:DATABASE_PASSWORD=Read-Host	"Enter database password [DATABASE_PASSWORD]"
			Write-Host				"Do you want to save entered settings to OH configuration files?"
			get_confirmation 1;
			set_db_name;
			$script:WRITE_CONFIG_FILES="on"; write_config_files;
			Write-Host "Done!"
			Read-Host "Press any key to continue";
		}
		###################################################
		"e"	{ # export/save database
			# check if mysql utilities exist
			mysql_check;
			# check if portable mode is on
			if ( !($OH_MODE -eq "CLIENT" )) {
				# check if database already exists
				if ( !(Test-Path "$OH_PATH/$DATA_DIR")) {
			        	Write-Host "Error: no data found! Exiting." -ForegroundColor Red
					exit 2;
				}
				else {
					config_database;
					start_database;
				}
			}
			test_database_connection;
			Write-Host "Trying to save Open Hospital database..."
			dump_database;

			if ( !($OH_MODE -eq "CLIENT" )) {
				shutdown_database;
			}
			Write-Host "Done!"
			Read-Host "Press any key to continue";
		}
		###################################################
		"r"	{ # restore database
			# check if database exists
			if ( (Test-Path "$OH_PATH/$DATA_DIR" )) {
				Write-Host "Error: Database already present. Remove existing database before restoring. Exiting." -ForegroundColor Red
			}
			else {
				Write-Host "Restoring Open Hospital database...."
				# ask user for database to restore
				$DB_CREATE_SQL = Read-Host -Prompt "Enter SQL dump/backup file that you want to restore - (in $script:SQL_DIR subdirectory) -> "
				if ( !(Test-Path "$OH_PATH/$SQL_DIR/$DB_CREATE_SQL" -PathType leaf)) {
					Write-Host "Error: No SQL file found!" -ForegroundColor Red
				}
				else {
					Write-Host "Found $SQL_DIR/$DB_CREATE_SQL, restoring it..."
					# check if mysql utilities exist
					mysql_check;
					if ( !($OH_MODE -eq "CLIENT" )) {
						set_db_name;
						config_database;
						initialize_dir_structure;
						initialize_database;
						start_database;	
						set_database_root_pw;
					}
					import_database;
					if ( !($OH_MODE -eq "CLIENT" )) {
						shutdown_database;
					}
					Write-Host "Done!"
				}
			}
			Read-Host "Press any key to continue";
			}
		###################################################
		"s"	{ # save / write config files
			Write-Host "Do you want to save current settings to OH configuration files?"
			
			get_confirmation 1;
			# overwrite configuration files if existing
			$script:WRITE_CONFIG_FILES="on"; write_config_files;
			set_oh_mode;
			set_language;
			set_log_level;
			# if Desktop link is present update it
			if (Test-Path -Path "$Home\Desktop\OpenHospital.lnk" -PathType leaf) {
				create_desktop_shortcut;
			}
			Write-Host "Done!"
			Read-Host "Press any key to continue";
		}
		###################################################
		"t"	{ # test database connection 
			if ( !($OH_MODE -eq "CLIENT") ) {
				Write-Host "Error: Only for CLIENT mode." -ForegroundColor Red
				Read-Host; break;
			}
			mysql_check;
			test_database_connection;
			Read-Host "Press any key to continue";
		}
		###################################################
		"u"	{ # create Desktop shortcut
			create_desktop_shortcut;
			Read-Host "Press any key to continue";
		}
		###################################################
		"v"	{ # display software version and configuration
	        	Write-Host "--------- OH version ---------"
			# show configuration
			Write-Host "Open Hospital version:" $OH_VERSION
			Write-Host ""
	        	Write-Host "--------- Software versions ---------"
			Write-Host "$MYSQL_NAME version: $MYSQL_DIR"
			Write-Host "JAVA version: $JAVA_DISTRO"
			Write-Host ""
	 		Write-Host "--------- Script Configuration ---------"
	 		Write-Host "Architecture is $ARCH"
	 		Write-Host "Config file generation is set to $WRITE_CONFIG_FILES"
			Write-Host ""
	 		Write-Host "--------- OH default configuration ---------"
			Write-Host "Language is set to $OH_LANGUAGE"
			Write-Host "Demo data is set to $DEMO_DATA"
			Write-Host ""
			Write-Host "--- Database ---"
			Write-Host "DATABASE_SERVER=$DATABASE_SERVER"
			Write-Host "DATABASE_PORT=$DATABASE_PORT"
			Write-Host "DATABASE_NAME=$DATABASE_NAME"
			Write-Host "DATABASE_USER=$DATABASE_USER"
			Write-Host ""
			Write-Host "--- Imaging / Dicom ---"
			Write-Host "DICOM_MAX_SIZE=$DICOM_MAX_SIZE"
			Write-Host "DICOM_STORAGE=$DICOM_STORAGE"
			Write-Host "DICOM_DIR=$DICOM_DIR"
			Write-Host ""
			Write-Host "--- OH Folders ---"
			Write-Host "OH_DIR=$OH_DIR"
			Write-Host "OH_DOC_DIR=$OH_DOC_DIR"
			Write-Host "OH_SINGLE_USER=$OH_SINGLE_USER"
			Write-Host "CONF_DIR=$CONF_DIR"
			Write-Host "DATA_DIR=$DATA_DIR"
			Write-Host "PHOTO_DIR=$PHOTO_DIR"
			Write-Host "BACKUP_DIR=$BACKUP_DIR"
			Write-Host "LOG_DIR=$LOG_DIR"
			Write-Host "SQL_DIR=$SQL_DIR"
			Write-Host "TMP_DIR=$TMP_DIR"
			Write-Host ""
			Write-Host "--- Logging ---"
			Write-Host "Log level is set to $LOG_LEVEL"
			Write-Host "LOG_FILE=$LOG_FILE"
			Write-Host "LOG_FILE_ERR=$LOG_FILE_ERR"
			Write-Host "OH_LOG_FILE=$OH_LOG_FILE"
			Write-Host ""

			Read-Host "Press any key to continue";
		}
		###################################################
		"X"	{ # clean
			Write-Host "Cleaning Open Hospital installation..."
			Write-Host "Warning: do you want to remove all existing log files?" -ForegroundColor Red
			$choice = Read-Host -Prompt "Press [y] to confirm: "
			if (( "$choice" -eq "y" )) {
				clean_log_files;
			}
			# remove all configuration files - leave only .dist files
			Write-Host "Warning: do you want to remove all existing configuration files?" -ForegroundColor Red
			$choice = Read-Host -Prompt "Press [y] to confirm: "
			if (( "$choice" -eq "y" )) {
				clean_conf_files;
			}
			Write-Host "Warning: do you want to remove all existing data and databases?" -ForegroundColor Red
			$choice = Read-Host -Prompt "Press [y] to confirm: "
			if (( "$choice" -eq "y" )) {
				Write-Host "--->>> This operation cannot be undone" -ForegroundColor Red
				Write-Host "--->>> Are you sure?" -ForegroundColor Red
				$choice = Read-Host -Prompt "Press [y] to confirm: "
				if (( "$choice" -eq "y" )) {
					clean_database;
					Write-Host "Done!"
				}
			}
			# unset variables
			#Clear-Variable -name OH_MODE
			#Clear-Variable -name OH_LANGUAGE
			#Clear-Variable -name OH_SINGLE_USER
			#Clear-Variable -name LOG_LEVEL
			#Clear-Variable -name DEMO_DATA
			Read-Host "Press any key to continue";
		}
		###################################################
		"q" 	{ # quit
			Write-Host "Quit pressed. Exiting.";
			exit 0; 
		}
		###################################################
		"Q"	{ # Quit
			Write-Host "Quit pressed. Exiting.";
			exit 0; 
		}
		###################################################
		""	{ # Start
			Write-Host "Starting Open Hospital...";
			$option="Z";
		}
		###################################################
		default { Write-Host "Invalid option: $option."; 
			Read-Host "Press any key to continue";
			break;
		}
		}
	Clear-Host;
	}
	# execute until quit is pressed or CLIENT/PORTABLE/SERVER mode is select (Z option)
	until ( ($option -ieq 'q') -Or ($option -ceq 'Z') )
}
}

######################## Script start ########################

######## Pre-flight checks

# check user running the script
#Write-Host "Checking for elevated permissions..."
#	if (-NOT ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole(`[Security.Principal.WindowsBuiltInRole] "Administrator")) {
#	Write-Host "Error: Cannot run as Administrator user. Exiting" -ForegroundColor Red
#	exit 1
#}
# else { Write-Host "User ok — go on executing the script..." -ForegroundColor Green }


######## Environment setup

set_path;
read_settings;
set_defaults;
set_db_name;

# set working dir to OH base dir
cd "$OH_PATH" # workaround for hard coded paths
Write-Host "Interactive mode is set to $script:INTERACTIVE_MODE"

######## Parse user input and show interactive menu
parse_user_input;

######################### OH start ############################

# check demo mode
if ( $DEMO_DATA -eq "on" ) {
	# exit if OH is configured in CLIENT mode
	if ( $OH_MODE -eq "CLIENT" ) {
		Write-Host "Error - OH_MODE is set to $OH_MODE mode. Cannot run with Demo data. Exiting." -ForeGroundcolor Red
		Read-Host; 
		exit 1
	}
	
	# set database name to demo
	$script:DATABASE_NAME=$DEMO_DATABASE
	
	if (Test-Path -Path "$OH_PATH/$SQL_DIR/$DB_DEMO" -PathType leaf) {
	        Write-Host "Found SQL demo database, starting OH with Demo data..."
		$DB_CREATE_SQL=$DB_DEMO
	}
	else {
	      	Write-Host "Error: no $DB_DEMO found! Exiting." -ForegroundColor Red
		Read-Host;
		exit 1
	}
	set_db_name;
}

# display running configuration
Write-Host "Write config files is set to $WRITE_CONFIG_FILES"
Write-Host "Starting Open Hospital in $OH_MODE mode..."
Write-Host "OH_PATH is set to $OH_PATH"

# display OH settings
Write-Host "OH language is set to $OH_LANGUAGE"

# check for java
java_check;

# setup java lib
java_lib_setup;

# create directories
initialize_dir_structure;

######## Database setup

# start MariaDB/MySQL database server and create database
if ( ($OH_MODE -eq "PORTABLE") -Or ($OH_MODE -eq "SERVER") ){
	# check for MariaDB/MySQL software
	mysql_check;
	# config database
	config_database;
	# check if OH database already exists
	if ( !(Test-Path "$OH_PATH/$DATA_DIR") ) {
		Write-Host "OH database not found, starting from scratch..."
		# prepare database
		initialize_database;
		# start database
		start_database;	
		# set database root password
		set_database_root_pw;
		# create database and load data
		import_database;
	}
	else {
		Write-Host "OH database found!"
		# start database
		start_database;
	}
	# check for API server
	if ( $API_SERVER -eq "on" ) {
		start_api_server;
	}
}

######## OH startup

# if SERVER mode is selected, wait for CTRL-C input to exit
if ( $OH_MODE -eq "SERVER" ) {

	Write-Host "Open Hospital - SERVER mode started"
	# show MariaDB/MySQL server running configuration
	Write-Host "*******************************"
	Write-Host "* Database server listening on:"
	Write-Host ""
	Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE" | Select-String -Pattern "bind-address" | Select-Object -First 1 -Unique
	Get-Content "$OH_PATH/$CONF_DIR/$MYSQL_CONF_FILE" | Select-String -Pattern "port" | Select-Object -First 1 -Unique
	Write-Host ""
	Write-Host "*******************************"
	Write-Host "Database server ready for connections..."
	
	while ($true) {
		$choice = Read-Host -Prompt "Press Q to exit"

		# switch -CaseSensitive ("$choice") {
		switch ("$choice") {
			"Q" {
				Write-Host "Exiting Open Hospital..."
				shutdown_database;		
				exit 0
			}
			default { "Invalid choice. " }
		}
	}
# CTRL-C version 
#	while ($true) {
#		if ([console]::KeyAvailable) {
#			$key = [system.console]::readkey($true)
#			if (($key.modifiers -band [consolemodifiers]"control") -and ($key.key -eq "C")){
#				echo "Exiting Open Hospital XX..."
#				shutdown_database;		
#				exit 0
#			}
#		}
#	}

}
else {
	######## Open Hospital GUI startup - only for CLIENT or PORTABLE mode

	# test if database connection is working
	test_database_connection;

	# generate config files if not existent
	write_config_files;

	# start OH gui
	start_gui;

	# Close and exit
	Write-Host "Exiting Open Hospital..."
	shutdown_database;

	# go back to starting directory
	cd "$CURRENT_DIR"
}

# Final exit
Write-Host "Done!"
Read-Host
exit 0
