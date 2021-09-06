#%SystemRoot%\system32\WindowsPowerShell\v1.0\powershell.exe
#
#!/snap/bin/pwsh
# Open Hospital (www.open-hospital.org)
# Copyright © 2006-2021 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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
The script is used to setup and launch Open Hospital in PORTABLE, CLIENT or DEMO mode.
It can also be used to perform some basic operation like saving or importing a database.

Open Hospital CLIENT | PORTABLE
Usage: oh.ps1 [ -lang en|fr|it|es|pt ] [default set to en]
              [ -mode PORTABLE|CLIENT ]
              [ -debug INFO|DEBUG ] [default set to INFO]

.EXAMPLE
./oh.ps1 -lang en

.NOTES
Developed by Informatici Senza Frontiere

.LINK
https://www.open-hospital.org

#>

######## SET DEBUG mode
# saner programming env: these switches turn some bugs into errors
#Set-PSDebug -Strict
# Clean all variables in IDE
#Remove-Variable * -ErrorAction SilentlyContinue; Remove-Module *; $error.Clear();


######## Command line parameters
param ($lang, $debuglevel, $mode)
$script:OH_LANGUAGE=$lang
$script:LOG_LEVEL=$debuglevel
$script:OH_MODE=$mode

######## Global preferences
# disable progress bar
$global:ProgressPreference= 'SilentlyContinue'

######## Open Hospital Configuration
# OH_PATH is the directory where Open Hospital files are located
# OH_PATH="c:\Users\OH\OpenHospital\oh-1.11"

$script:OH_MODE="PORTABLE"  # set functioning mode to PORTABLE | CLIENT
#$script:DEMO_MODE="off"

# language setting - default set to en
#$script:OH_LANGUAGE=en # fr es it pt

# set log level to INFO | DEBUG - default set to INFO
#$script:LOG_LEVEL=INFO

# enable / disable DICOM (on|off)
#$script:DICOM_ENABLE="off"

######## Software configuration - change at your own risk :-)
# Database
$script:MYSQL_SERVER="localhost"
$script:MYSQL_PORT=3306
$script:MYSQL_ROOT_PW="tmp2021oh111"
$script:DATABASE_NAME="oh"
$script:DATABASE_USER="isf"
$script:DATABASE_PASSWORD="isf123"

$script:DICOM_MAX_SIZE="4M"

$script:OH_DIR="oh"
$script:SQL_DIR="sql"
$script:DATA_DIR="data/db"
$script:DICOM_DIR="data/dicom_storage"
$script:LOG_DIR="data/log"
$script:LOG_FILE="startup.log"
$script:LOG_FILE_ERR="startup.err"
$script:OH_LOG_FILE="openhospital.log"
$script:TMP_DIR="tmp"
$script:BACKUP_DIR="data/dump"

$script:DB_DEMO="create_all_demo.sql"
# date +%Y-%m-%d_%H-%M-%S
$script:DATE= Get-Date -Format "yyyy-MM-dd_HH-mm-ss"

######## Advanced options

## set MANUAL_CONFIG to "on" to setup configuration files manually
# my.cnf and all oh/rsc/*.properties files will not be generated or
# overwritten if already present
$script:MANUAL_CONFIG="off"

## set INTERACTIVE_MODE to "off" to launch oh.ps1 without calling the user
# interaction meno (script_menu). Useful if automatic startup of OH is needed.
# In order to use this mode, setup all the OH configuration variables in the script
# or pass arguments via command line.
$script:INTERACTIVE_MODE="on"

# set JAVA_BIN # Uncomment this if you want to use system wide JAVA
#$script:JAVA_BIN="C:\Program Files\JAVA\bin\java.exe"

######## Define architecture

$script:ARCH=$env:PROCESSOR_ARCHITECTURE

switch ( "$ARCH" ) {	
	"amd64" { $script:JAVA_ARCH=64; $script:MYSQL_ARCH="x64"; Break }
	"AMD64" { $script:JAVA_ARCH=64; $script:MYSQL_ARCH="x64"; Break }
	"x86_64" { $script:JAVA_ARCH=64; $script:MYSQL_ARCH="x64"; Break }
	("486","586","686","x86","i86pc") { $script:JAVA_ARCH=32; $script:MYSQL_ARCH=32; Break }
	default {
		Write-Host "Unknown architecture: $ARCH. Exiting." -ForegroundColor Red
		Read-Host; exit 1
	}
}

# workaround to force 32bit JAVA in order to have DICOM working on 64bit arch
if ( $DICOM_ENABLE -eq "on" ) {
	$script:JAVA_ARCH=32
}
	
# workaround to force 32bit JAVA in order to have DICOM working
#$script:JAVA_ARCH=32

######## MySQL Software
# MariaDB
$script:MYSQL_VERSION="10.2.40"
$script:MYSQL_URL="http://ftp.bme.hu/pub/mirrors/mariadb/mariadb-$script:MYSQL_VERSION/win$script:MYSQL_ARCH-packages/"
$script:MYSQL_DIR="mariadb-$script:MYSQL_VERSION-win$script:MYSQL_ARCH"
# MySQL
#$script:MYSQL_DIR="mysql-5.7.35-win$script:MYSQL_ARCH"
#$script:MYSQL_URL=" https://downloads.mysql.com/archives/get/p/23/file"
$script:EXT="zip"

######## JAVA Software
######## JAVA 64bit - default architecture
### JRE 11 - zulu
#$script:JAVA_DISTRO="zulu11.45.27-ca-jre11.0.10-win_i686"
#$script:JAVA_URL="https://cdn.azul.com/zulu/bin/"
#$script:JAVA_DIR="zulu11.45.27-ca-jre11.0.10-win_i686"

### JRE 11 - openjdk
$script:JAVA_URL="https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/"
$script:JAVA_DISTRO="OpenJDK11U-jre_x64_windows_hotspot_11.0.11_9"
$script:JAVA_DIR="jdk-11.0.11+9-jre"

######## JAVA 32bit
if ( $JAVA_ARCH -eq "32" ) {
	# set JRE 32 bit
	### JRE 8 32bit - openjdk distribution
	# https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u292-b10/OpenJDK8U-jre_x86-32_windows_hotspot_8u292b10.zip
	$script:JAVA_DISTRO="OpenJDK8U-jre_x86-32_windows_hotspot_8u292b10"
	$script:JAVA_URL="https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u292-b10/"
	$script:JAVA_DIR="jdk8u292-b10-jre"
	### JRE 8 32bit - zulu distribution
	#$script:JAVA_DISTRO="zulu8.56.0.21-ca-jre8.0.302-win_i686"
	#$script:JAVA_URL="https://cdn.azul.com/zulu/bin/"
	#$script:JAVA_DIR="$JAVA_DISTRO"

	### JRE 11 32bit - zulu distribution
	#$script:JAVA_DISTRO="zulu11.45.27-ca-jre11.0.10-win_i686"
	#$script:JAVA_URL="https://cdn.azul.com/zulu/bin/"
	#$script:JAVA_DIR="zulu11.45.27-ca-jre11.0.10-win_i686"
}

######## get script info
# determine script name and location for PowerShell
$script:SCRIPT_DIR = Split-Path $script:MyInvocation.MyCommand.Path
$script:SCRIPT_NAME = $MyInvocation.MyCommand.Name

######################## DO NOT EDIT BELOW THIS LINE ########################

######################## Functions ########################

######## User input / option parsing

function script_menu {
	# show menu
	# Clear-Host # clear console
	Write-Host " ---------------------------------------------------------"
	Write-Host "|                                                         |"
	Write-Host "|                   Open Hospital | OH                    |"
	Write-Host "|                                                         |"
	Write-Host " ---------------------------------------------------------"
	Write-Host " lang $script:OH_LANGUAGE | arch $ARCH | mode $OH_MODE"
	Write-Host " ---------------------------------------------------------"
	Write-Host ""
	Write-Host " Usage: $SCRIPT_NAME [ -lang en|fr|it|es|pt ] "
	Write-Host "               [ -mode PORTABLE|CLIENT ]"
	Write-Host "               [ -debug INFO|DEBUG ] "
	Write-Host ""
	Write-Host "   C    start OH - CLIENT mode (Client / Server configuration)"
	Write-Host "   d    start OH in debug mode"
	Write-Host "   D    start OH in DEMO mode"
	Write-Host "   G    setup GSM"
	Write-Host "   l    set language: en|fr|it|es|pt"
	Write-Host "   s    save OH database"
	Write-Host "   r    restore OH database"
	Write-Host "   t    test database connection (Client mode only)"
	Write-Host "   v    show OH software version and configuration"
	Write-Host "   X    clean/reset OH installation"
	Write-Host "   q    quit"
	Write-Host ""
}

function get_confirmation {
	$choice = Read-Host -Prompt "(y/n)? "
	switch ("$choice") {
		"y"  { "yes"; break }
		"n"  { "Exiting."; Read-Host; exit 0 }
		default { "Invalid choice. Exiting."; Read-Host; exit 1; }
	}
}

function set_path {
	# get current directory
	$script:CURRENT_DIR=Get-Location | select -ExpandProperty Path
	# set OH_PATH if not defined
	if ( ! $OH_PATH ) {
		Write-Host "Info: OH_PATH not defined - setting to script path"
		$script:OH_PATH=$PSScriptRoot
		if ( !(Test-Path "$OH_PATH\$SCRIPT_NAME") ) {
			Write-Host "Error - $SCRIPT_NAME not found in the current PATH. Please browse to the directory where Open Hospital was unzipped or set up OH_PATH properly." -ForegroundColor Yellow
			Read-Host; exit 1
		}
	}
}

function set_language {
	# Set OH interface languange - set default to en
	if ( ! $OH_LANGUAGE ) {
		$script:OH_LANGUAGE="en"
	}	
	switch ( "$OH_LANGUAGE" ) {
		{"en","fr","it","es","pt"} {
			# set database creation script in chosen language
			$script:DB_CREATE_SQL="create_all_$OH_LANGUAGE.sql"
			break
	        }
		default {
	        	Write-Host "Invalid language option: $OH_LANGUAGE. Exiting." -ForegroundColor Red
			Read-Host; exit 1
	        }
	}
}

function initialize_dir_structure {
	# create directory structure
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$TMP_DIR") > $null
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$LOG_DIR") > $null
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$DICOM_DIR") > $null
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$BACKUP_DIR") > $null
}

function java_lib_setup {
	# NATIVE LIB setup
	switch ( "$JAVA_ARCH" ) {
		"64" { $script:NATIVE_LIB_PATH="$OH_PATH\$OH_DIR\lib\native\Win64" }
		"32" { $script:NATIVE_LIB_PATH="$OH_PATH\$OH_DIR\lib\native\Windows" }
	}

	# CLASSPATH setup
	# include OH jar file
	$script:OH_CLASSPATH="$OH_PATH\$OH_DIR\bin\OH-gui.jar"

	# include all jar files under lib\
	$script:jarlist= Get-ChildItem "$OH_PATH\$OH_DIR\lib" -Filter *.jar |  % { $_.FullName }
	ForEach( $n in $jarlist ){
		$script:OH_CLASSPATH="$n;$OH_CLASSPATH"
	}
	
	# include all needed directories
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\bundle\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rpt\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rsc\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rsc\icons\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rsc\images\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\rsc\SmsGateway\"
	$script:OH_CLASSPATH="$OH_CLASSPATH;$OH_PATH\$OH_DIR\lib\"
}

function download_file ($download_url,$download_file){
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

function java_check {
	if ( !( $JAVA_BIN ) ) {
		$script:JAVA_BIN="$OH_PATH\$JAVA_DIR\bin\java.exe"
	}

	if ( !(Test-Path $JAVA_BIN) ) {
        	if ( !(Test-Path "$OH_PATH\$JAVA_DISTRO.$EXT") ) {
			Write-Host "Warning - JAVA not found. Do you want to download it?" -ForegroundColor Yellow
			get_confirmation;
			# Downloading openjdk binaries
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
	}
	# check for java binary
	if ( Test-Path "$OH_PATH\$JAVA_DIR\bin\java.exe" ) {
		$script:JAVA_BIN="$OH_PATH\$JAVA_DIR\bin\java.exe"
	}
	else {
		Write-Host "Error: JAVA not found. Exiting." -ForegroundColor Red
		Read-Host; exit 1
	}
	Write-Host "JAVA found!"
	Write-Host "Using $JAVA_BIN"
}

function mysql_check {
	if (  !(Test-Path "$OH_PATH\$MYSQL_DIR") ) {
		if ( !(Test-Path "$OH_PATH\$MYSQL_DIR.$EXT") ) {
			Write-Host "Warning - MariaDB/MySQL not found. Do you want to download it?" -ForegroundColor Yellow
			get_confirmation;
			# Downloading mysql binary
			download_file "$MYSQL_URL" "$MYSQL_DIR.$EXT" 
		}
		Write-Host "Unpacking $MYSQL_DIR..."
		try {
			Expand-Archive "$OH_PATH\$MYSQL_DIR.$EXT" -DestinationPath "$OH_PATH\" -Force
		}
		catch {
			Write-Host "Error unpacking MySQL. Exiting." -ForegroundColor Red
			Read-Host; exit 1
		}
	        Write-Host "MySQL unpacked successfully!"
	}
	# check for mysql binary
	if ( Test-Path "$OH_PATH\$MYSQL_DIR\bin\mysqld.exe" ) {
        	Write-Host "MySQL found!"
		Write-Host "Using $MYSQL_DIR"
	}
	else {
		Write-Host "Error: MySQL not found. Exiting." -ForegroundColor Red
		Read-Host; exit 1
	}
}

function config_database {
	# find a free TCP port to run MySQL starting from the default port
	Write-Host "Looking for a free TCP port for MySQL database..."

	$ProgressPreference = 'SilentlyContinue'
	while ( Test-NetConnection $script:MYSQL_SERVER -Port $MYSQL_PORT -InformationLevel Quiet -ErrorAction SilentlyContinue -WarningAction SilentlyContinue ) {
    		Write-Host "Testing TCP port $MYSQL_PORT...."
        	$script:MYSQL_PORT++
	}

	Write-Host "Found TCP port $MYSQL_PORT!"

	# create MySQL configuration
	Write-Host "Generating MySQL config file..."
	if ( Test-Path "$OH_PATH/etc/mysql/my.cnf" ) {
		mv -Force "$OH_PATH/etc/mysql/my.cnf" "$OH_PATH/etc/mysql/my.cnf.old"
	}
	(Get-Content "$OH_PATH/etc/mysql/my.cnf.dist").replace("DICOM_SIZE","$DICOM_MAX_SIZE") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
	(Get-Content "$OH_PATH/etc/mysql/my.cnf").replace("OH_PATH_SUBSTITUTE","$OH_PATH") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
	(Get-Content "$OH_PATH/etc/mysql/my.cnf").replace("MYSQL_SERVER","$MYSQL_SERVER") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
	(Get-Content "$OH_PATH/etc/mysql/my.cnf").replace("MYSQL_PORT","$MYSQL_PORT") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
	(Get-Content "$OH_PATH/etc/mysql/my.cnf").replace("MYSQL_DISTRO","$MYSQL_DIR") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
	(Get-Content "$OH_PATH/etc/mysql/my.cnf").replace("DATA_DIR","$DATA_DIR") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
	(Get-Content "$OH_PATH/etc/mysql/my.cnf").replace("TMP_DIR","$TMP_DIR") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
	(Get-Content "$OH_PATH/etc/mysql/my.cnf").replace("LOG_DIR","$LOG_DIR") | Set-Content "$OH_PATH/etc/mysql/my.cnf"
}

function initialize_database {
	# create data directory
	[System.IO.Directory]::CreateDirectory("$OH_PATH/$DATA_DIR") > $null
	# inizialize MySQL
	Write-Host "Initializing MySQL database on port $MYSQL_PORT..."
	switch -Regex ( $MYSQL_DIR ) {
		"mariadb" {
			try {
				Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysql_install_db.exe" -ArgumentList ("--datadir=`"$OH_PATH\$DATA_DIR`" --password=$MYSQL_ROOT_PW") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
	        	}
			catch {
				Write-Host "Error: MariaDB initialization failed! Exiting." -ForegroundColor Red
				Read-Host; exit 2
			}
		}
		"mysql" {
			try {
				Start-Process "$OH_PATH\$MYSQL_DIR\bin\mysqld.exe" -ArgumentList ("--initialize-insecure --basedir=`"$OH_PATH\$MYSQL_DIR`" --datadir=`"$OH_PATH\$DATA_DIR`" ") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"; break
			}
			catch {
				Write-Host "Error: MySQL initialization failed! Exiting." -ForegroundColor Red
				Read-Host; exit 2
			}
		}
	}
}

function start_database {
	Write-Host "Starting MySQL server... "
	try {
		Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysqld.exe" -ArgumentList ("--defaults-file=`"$OH_PATH\etc\mysql\my.cnf`" --tmpdir=`"$OH_PATH\$TMP_DIR`" --standalone") -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
		Start-Sleep -Seconds 2
	}
	catch {
		Write-Host "Error: MySQL server not started! Exiting." -ForegroundColor Red
		Read-Host; exit 2
	}

	# wait till the MySQL socket file is created -> TO BE IMPLEMENTED
	# while ( -e $OH_PATH/$MYSQL_SOCKET ); do sleep 1; done
	# # Wait till the MySQL tcp port is open
	# until nc -z $MYSQL_SERVER $MYSQL_PORT; do sleep 1; done

	Write-Host "MySQL server started! "
}

function set_database_root_pw {
	# if using MySQL root password need to be set
	switch -Regex ( $MYSQL_DIR ) {
		"mysql" {
		echo "Setting MySQL root password..."
        $SQLCOMMAND=@"
        -u root --skip-password -h $MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '$MYSQL_ROOT_PW';"
"@
			try {
				Start-Process -FilePath "$OH_PATH/$MYSQL_DIR/bin/mysql.exe" -ArgumentList ("$SQLCOMMAND") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
			}
			catch {
				Write-Host "Error: MySQL root password not set! Exiting." -ForegroundColor Red
				shutdown_database;
				Read-Host; exit 2
			}
		}
	}
}

function import_database {
	Write-Host "Creating OH Database..."
	# create OH database and user
	
    $SQLCOMMAND=@"
    -u root -p$MYSQL_ROOT_PW -h $MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp -e "CREATE DATABASE $DATABASE_NAME; CREATE USER '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; CREATE USER '$DATABASE_USER'@'%' IDENTIFIED BY '$DATABASE_PASSWORD'; GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'localhost'; GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'%';"
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
	if ( Test-Path "$OH_PATH\$SQL_DIR\$DB_CREATE_SQL" ) {
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
   --local-infile=1 -u root -p$MYSQL_ROOT_PW -h $MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp $DATABASE_NAME -e "source ./$DB_CREATE_SQL"
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

function dump_database {
	# save OH database if existing
	if ( Test-Path "$OH_PATH\$MYSQL_DIR\bin\mysqldump.exe" ) {
		[System.IO.Directory]::CreateDirectory("$OH_PATH/$BACKUP_DIR") > $null
		Write-Host "Dumping MySQL database..."	
        $SQLCOMMAND=@"
    --skip-extended-insert -u root --password=$MYSQL_ROOT_PW -h $MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp $DATABASE_NAME
"@
	Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysqldump.exe" -ArgumentList ("$SQLCOMMAND") -Wait -NoNewWindow -RedirectStandardOutput "$OH_PATH\$BACKUP_DIR\mysqldump_$DATE.sql" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"	
	}
	else {
		Write-Host "Error: No mysqldump utility found! Exiting." -ForegroundColor Red
		shutdown_database;
		cd "$CURRENT_DIR"
		Read-Host; exit 2
	}
	Write-Host "MySQL dump file $BACKUP_DIR\mysqldump_$DATE.sql completed!" -ForegroundColor Green
}

function shutdown_database {
	Write-Host "Shutting down MySQL..."
	Start-Process -FilePath "$OH_PATH\$MYSQL_DIR\bin\mysqladmin.exe" -ArgumentList ("-u root -p$MYSQL_ROOT_PW --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp shutdown") -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"
	# wait till the MySQL socket file is removed -> TO BE IMPLEMENTED
	# while ( -e $OH_PATH/$MYSQL_SOCKET ); do sleep 1; done
	Start-Sleep -Seconds 3
	Write-Host "MySQL stopped!"
}

function clean_database {
	Write-Host "Warning: do you want to remove all existing data and databases ?" -ForegroundColor Red
	get_confirmation;
	Write-Host "--->>> This operation cannot be undone" -ForegroundColor Red
	Write-Host "--->>> Are you sure ?" -ForegroundColor Red
	get_confirmation;
	Write-Host "Killing mysql processes..."
	# stop mysqld zombies
	Get-Process mysqld -ErrorAction SilentlyContinue | Stop-Process -PassThru
	Write-Host "Removing data..."
	# remove database files
	$filetodel="$OH_PATH\$DATA_DIR\*"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$TMP_DIR\*"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
}

function test_database_connection {
	# test connection to the OH MySQL database
	Write-Host "Testing database connection..."
	try {
		Start-Process -FilePath ("$OH_PATH\$MYSQL_DIR\bin\mysql.exe") -ArgumentList ("--user=$DATABASE_USER --password=$DATABASE_PASSWORD --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp -e $([char]34)USE $DATABASE_NAME$([char]34) " ) -Wait -NoNewWindow
		Write-Host "Database connection successfully established!"
	}
	catch {
		Write-Host "Error: can't connect to database! Exiting." -ForegroundColor Red
		Read-Host; exit 2
	}
}

function clean_files {
	# clean all configuration files - leave only .dist files
	Write-Host "Warning: do you want to remove all existing configuration and log files ?" -ForegroundColor Red
	get_confirmation;
	Write-Host "Removing files..."

	$filetodel="$OH_PATH\etc\mysql\my.cnf"; if (Test-Path $filetodel){ Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\etc\mysql\my.cnf.old"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$LOG_DIR\*"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\settings.properties"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\settings.properties.old"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\database.properties"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\database.properties.old"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\log4j.properties"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\log4j.properties.old"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\dicom.properties"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\rsc\dicom.properties.old"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
	$filetodel="$OH_PATH\$OH_DIR\$LOG_DIR\*"; if (Test-Path $filetodel) { Remove-Item $filetodel -Recurse -Confirm:$false -ErrorAction Ignore }
}


######################## Script start ########################

######## Pre-flight checks

# check user running the script
# Write-Host "Checking for elevated permissions..."
# if (-NOT ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole(`[Security.Principal.WindowsBuiltInRole] "Administrator")) {
# Write-Host "Error: Cannot run as Administrator user. Exiting" -ForegroundColor Red
# exit 1
#}
# else { Write-Host "User ok — go on executing the script..." -ForegroundColor Green }

# log level - set default to INFO
if ( [string]::IsNullOrEmpty($LOG_LEVEL) ) {
	$script:LOG_LEVEL="INFO"
}

######## Environment setup

set_path;
set_language;

# set working dir to OH base dir
cd "$OH_PATH" # workaround for hard coded paths

######## User input

# If INTERACTIVE_MODE is set to "off" don't ask for user input
if ( $INTERACTIVE_MODE -eq "on") {
	script_menu;
	$opt = Read-Host "Please make a selection or press any other key to start Open Hospital in $OH_MODE mode"
	Write-Host ""

	# parse_input
	switch -casesensitive( "$opt" ) {
	"C"	{ # start in client mode 
		$script:OH_MODE="CLIENT"
	}
	"d"	{ # debug 
           	Write-Host "Starting Open Hospital in debug mode..."
		$LOG_LEVEL="DEBUG"
		Write-Host "Log level set to $LOG_LEVEL"
	}
	"D"	{ # demo mode 
		Write-Host "Starting Open Hospital in DEMO mode..."
		# exit if OH is configured in CLIENT mode
		if ( $OH_MODE -eq "CLIENT" ) {
			Write-Host "Error - OH_MODE set to CLIENT mode. Cannot run in DEMO mode, exiting." -ForeGroundcolor Red
			Read-Host;
			exit 1;
		}
		else { $script:OH_MODE="PORTABLE" }
		$DEMO_MODE="on"
	}
	"G"	{ # set up GSM 
		Write-Host "Setting up GSM..."
		java_check;
		java_lib_setup;
		Start-Process -FilePath "$JAVA_BIN" -ArgumentList ("-Djava.library.path=${NATIVE_LIB_PATH} -classpath $OH_CLASSPATH org.isf.utils.sms.SetupGSM $@ ") -Wait -NoNewWindow
		Read-Host;
		exit 0;
	}
	"l"	{ # set language 
		$script:OH_LANGUAGE = Read-Host "Select language: en|fr|es|it|pt (default is en)"
		set_language;
	}
	"s"	{ # save database 
		# check if portable mode is on

		if ( $OH_MODE -eq "PORTABLE" ) {
			# check if database already exists
			if ( Test-Path "$OH_PATH\$DATA_DIR\$DATABASE_NAME" ) {
				mysql_check;
				if ($MANUAL_CONFIG -eq "off" ) {
					config_database;
				}
			}
			else {
		        	Write-Host "Error: no data found! Exiting." -ForegroundColor Red
				Read-Host; exit 1
			}
			start_database;
			Write-Host "Saving Open Hospital database..."
			dump_database;
			shutdown_database;
			Write-Host "Done!"
			Read-Host;
			exit 0
		}
		# dump remote database for CLIENT mode configuration
		test_database_connection;
		echo "Saving Open Hospital database..."
		dump_database;
		Write-Host "Done!"
                exit 0
	}
	"r"	{ # restore
	       	Write-Host "Restoring Open Hospital database...."
		# ask user for database to restore
		$DB_CREATE_SQL = Read-Host -Prompt "Enter SQL dump/backup file that you want to restore - (in $script:BACKUP_DIR subdirectory) -> "
		if ( Test-Path "$OH_PATH\$SQL_DIR\$DB_CREATE_SQL" ) {
			Write-Host "Found $SQL_DIR\$DB_CREATE_SQL, restoring it..."
			# reset database if exists
			clean_database;
			mysql_check;
			if ($MANUAL_CONFIG -eq "off" ) {
				config_database;
			}
			initialize_dir_structure;
			initialize_database;
			start_database;	
			set_database_root_pw;
			import_database;
			shutdown_database;
			echo "Done!"
			exit 0
		}
		else {
			Write-Host "Error: No SQL file found! Exiting." -ForegroundColor Red
			Read-Host; exit 2
		}
        	Read-Host; exit 0
	}
	"t"	{ # test database connection 
		if ( !($OH_MODE -eq "CLIENT") ) {
			Write-Host "Error: Only for CLIENT mode. Exiting." -ForegroundColor Red
			Read-Host; exit 1
		}
		test_database_connection;
		Read-Host; exit 0
	}
	"v"	{ # show version
        	Write-Host "--------- Software version ---------"
	
		Get-Content $OH_PATH\$OH_DIR\rsc\version.properties | Where-Object {$_.length -gt 0} | Where-Object {!$_.StartsWith("#")} | ForEach-Object {
		$var = $_.Split('=',2).Trim()
		New-Variable -Scope Script -Name $var[0] -Value $var[1]
		}
		Write-Host "Open Hospital version" $script:VER_MAJOR $script:VER_MINOR $script:VER_RELEASE
		Write-Host "MySQL version: $MYSQL_DIR"
		Write-Host "JAVA version:"
		Write-Host "$JAVA_DISTRO"
		Write-Host ""

		# show configuration
 		Write-Host "--------- Configuration ---------"
 		Write-Host "Architecture is $ARCH"
 		Write-Host "Open Hospital is configured in $OH_MODE mode"
		Write-Host "Language is set to $OH_LANGUAGE"
		Write-Host "DEMO mode is set to $DEMO_MODE"
		Write-Host ""
		Write-Host "MYSQL_SERVER=$MYSQL_SERVER"
		Write-Host "MYSQL_PORT=$MYSQL_PORT"
		Write-Host "DATABASE_NAME=$DATABASE_NAME"
		Write-Host "DATABASE_USER=$DATABASE_USER"
		Write-Host "DATABASE_PASSWORD=$DATABASE_PASSWORD"
		Write-Host "DICOM_MAX_SIZE=$DICOM_MAX_SIZE"
		Write-Host "OH_DIR=$OH_DIR"
		Write-Host "BACKUP_DIR=$BACKUP_DIR"
		Write-Host "DICOM_DIR=$DICOM_DIR"
		Write-Host "DATA_DIR=$DATA_DIR"
		Write-Host "LOG_DIR=$LOG_DIR"
		Write-Host ""
	
		Read-Host;
		exit 0
	}
	"X"	{ # clean
		Write-Host "Cleaning Open Hospital installation..."
		clean_files;
		clean_database;
		Write-Host "Done!"
		Read-Host;
		exit 0
	}
	"q"	{ # quit
		exit 0; 
	}
#		default { Write-Host "Invalid option: $opt. Exiting."; exit 1; }
#		-> default is set to start with OH 
	}
}

######################### OH start ############################

Write-Host "Interactive mode set to $script:INTERACTIVE_MODE"

# check mode 
if ( !( $OH_MODE -eq "PORTABLE" ) -And !( $OH_MODE -eq "CLIENT" ) ) {
	Write-Host "Error - OH_MODE not defined [CLIENT - PORTABLE]! Exiting." -ForegroundColor Red
	Read-Host;
	exit 1
}

# check demo mode
if ( $DEMO_MODE -eq "on" ) {
	# exit if OH is configured in Client mode
	if (( $OH_MODE -eq "CLIENT" )) {
		Write-Host "Error - OH_MODE set to CLIENT mode. Cannot run in DEMO mode, exiting." -ForeGroundcolor Red
		Read-Host; 
		exit 1
		else { $script:OH_MODE="PORTABLE" }
	}
	
	# reset database if exists
	clean_database;

	if ( Test-Path -Path "$OH_PATH\$SQL_DIR\$DB_DEMO" ) {
	        Write-Host "Found SQL demo database, starting OH in DEMO mode..."
		$DB_CREATE_SQL=$DB_DEMO
	}
	else {
	      	Write-Host "Error: no $DB_DEMO found! Exiting." -ForegroundColor Red
		Read-Host;
		exit 1
	}
}

Write-Host "Starting Open Hospital in $OH_MODE mode..."
Write-Host "OH_PATH set to $OH_PATH"
Write-Host "OH language is set to $OH_LANGUAGE"

# check for java
java_check;

# setup java lib
java_lib_setup;

# create directories
initialize_dir_structure;

######## Database setup

# start MySQL and create database
if ( $OH_MODE -eq "PORTABLE" ) {
	# check for MySQL software
	mysql_check;
	# config MySQL
	config_database;
	# check if OH database already exists
	if ( !(Test-Path "$OH_PATH\$DATA_DIR\$DATABASE_NAME") ) {
		Write-Host "OH database not found, starting from scratch..."
		# prepare MySQL
		initialize_database;
		# start MySQL
		start_database;	
		# set database root password
		set_database_root_pw;
		# create database and load data
		import_database;
	}
	else {
		Write-Host "OH database found!"
		# start MySQL
		start_database;
	}
}

# test if database connection is working
test_database_connection;

if ($MANUAL_CONFIG -eq "off" ) {
# set up configuration files
Write-Host "Setting up OH configuration files..."

######## DICOM setup
if ( Test-Path "$OH_PATH/$OH_DIR/rsc/dicom.properties" ) {
	mv -Force $OH_PATH/$OH_DIR/rsc/dicom.properties $OH_PATH/$OH_DIR/rsc/dicom.properties.old
}
(Get-Content "$OH_PATH/$OH_DIR/rsc/dicom.properties.dist").replace("OH_PATH_SUBSTITUTE","$OH_PATH") | Set-Content "$OH_PATH/$OH_DIR/rsc/dicom.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/dicom.properties").replace("DICOM_DIR","$DICOM_DIR") | Set-Content "$OH_PATH/$OH_DIR/rsc/dicom.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/dicom.properties").replace("DICOM_SIZE","$DICOM_MAX_SIZE") | Set-Content "$OH_PATH/$OH_DIR/rsc/dicom.properties"

######## log4j.properties setup
if ( Test-Path "$OH_PATH/$OH_DIR/rsc/log4j.properties" ) {
	mv -Force $OH_PATH/$OH_DIR/rsc/log4j.properties $OH_PATH/$OH_DIR/rsc/log4j.properties.old
}
(Get-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties.dist").replace("DBSERVER","$MYSQL_SERVER") | Set-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties").replace("DBPORT","$MYSQL_PORT") | Set-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties").replace("DBUSER","$DATABASE_USER") | Set-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties").replace("DBPASS","$DATABASE_PASSWORD") | Set-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties").replace("DBNAME","$DATABASE_NAME") | Set-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties").replace("LOG_LEVEL","$LOG_LEVEL") | Set-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties").replace("LOG_DEST","../$LOG_DIR/$OH_LOG_FILE") | Set-Content "$OH_PATH/$OH_DIR/rsc/log4j.properties"

######## database.properties setup 
if ( Test-Path "$OH_PATH/$OH_DIR/rsc/database.properties" ) {
	mv -Force $OH_PATH/$OH_DIR/rsc/database.properties $OH_PATH/$OH_DIR/rsc/database.properties.old
}
(Get-Content "$OH_PATH/$OH_DIR/rsc/database.properties.dist").replace("DBSERVER","$MYSQL_SERVER") | Set-Content "$OH_PATH/$OH_DIR/rsc/database.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/database.properties").replace("DBPORT","$MYSQL_PORT") | Set-Content "$OH_PATH/$OH_DIR/rsc/database.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/database.properties").replace("DBUSER","$DATABASE_USER") | Set-Content "$OH_PATH/$OH_DIR/rsc/database.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/database.properties").replace("DBPASS","$DATABASE_PASSWORD") | Set-Content "$OH_PATH/$OH_DIR/rsc/database.properties"
(Get-Content "$OH_PATH/$OH_DIR/rsc/database.properties").replace("DBNAME","$DATABASE_NAME") | Set-Content "$OH_PATH/$OH_DIR/rsc/database.properties"

# direct creation of database.properties - deprecated
#Set-Content -Path $OH_PATH/$OH_DIR/rsc/database.properties -Value "jdbc.url=jdbc:mysql://"$MYSQL_SERVER":$MYSQL_PORT/$DATABASE_NAME"
#Add-Content -Path $OH_PATH/$OH_DIR/rsc/database.properties -Value "jdbc.username=$DATABASE_USER"
#Add-Content -Path $OH_PATH/$OH_DIR/rsc/database.properties -Value "jdbc.password=$DATABASE_PASSWORD"

######## settings.properties language setup 
# set language in OH config file
if ( Test-Path "$OH_PATH/$OH_DIR/rsc/settings.properties" ) {
	mv -Force $OH_PATH/$OH_DIR/rsc/settings.properties $OH_PATH/$OH_DIR/rsc/settings.properties.old
}
(Get-Content "$OH_PATH/$OH_DIR/rsc/settings.properties.dist").replace("OH_SET_LANGUAGE","$OH_LANGUAGE") | Set-Content "$OH_PATH/$OH_DIR/rsc/settings.properties"
}

######## Open Hospital start

Write-Host "Starting Open Hospital..."

# OH GUI launch
cd "$OH_PATH\$OH_DIR" # workaround for hard coded paths

$JAVA_ARGS="-client -Dlog4j.configuration=`"`'$OH_PATH\oh\rsc\log4j.properties`'`" -Dsun.java2d.dpiaware=false -Djava.library.path=`"`'$NATIVE_LIB_PATH`'`" -cp `"`'$OH_CLASSPATH`'`" org.isf.menu.gui.Menu"

Start-Process -FilePath "$JAVA_BIN" -ArgumentList $JAVA_ARGS -Wait -NoNewWindow -RedirectStandardOutput "$LOG_DIR/$LOG_FILE" -RedirectStandardError "$LOG_DIR/$LOG_FILE_ERR"

Write-Host "Exiting Open Hospital..."

if ( $OH_MODE -eq "PORTABLE" ) {
	shutdown_database;
}

# go back to starting directory
cd "$CURRENT_DIR"

# exit
Write-Host "Done!"
Read-Host
exit 0
