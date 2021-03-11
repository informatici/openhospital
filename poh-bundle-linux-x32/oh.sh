#!/bin/bash
#
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
#

# SET DEBUG mode
# saner programming env: these switches turn some bugs into errors
#set -o errexit -o pipefail -o noclobber -o nounset

######## Open Hospital Configuration
# OH_PATH is the directory where Open Hospital files are located
# OH_PATH=/usr/local/OpenHospital/oh-1.11

OH_DISTRO=PORTABLE # set distro to PORTABLE | CLIENT
DEMO_MODE=off

# Language setting - default set to en
#OH_LANGUAGE=en fr es it pt
#OH_LANGUAGE=it

# set debug level to INFO | DEBUG - default set to INFO
#DEBUG_LEVEL=INFO

######## Software configuration - change at your own risk :-)
# Database
MYSQL_SERVER=localhost
MYSQL_PORT=3306
MYSQL_ROOT_PW="root2020oh111"
DATABASE_NAME=oh
DATABASE_USER=isf
DATABASE_PASSWORD=isf123

DICOM_MAX_SIZE="4M"

OH_DIR=oh
SQL_DIR=sql
DICOM_DIR="data/dicom_storage"
DATA_DIR="data/db"
LOG_DIR="data/log"
BACKUP_DIR=sql
TMP_DIR=tmp
#DB_CREATE_SQL="create_all_en.sql" # default to create_all_en.sql
DB_DEMO="create_all_demo.sql"
DATE=`date +%Y-%m-%d_%H-%M-%S`
LOG_FILE=startup.log

######## Advanced options
## set MANUAL_CONFIG to "on" to setup configuration files manually
# my.cnf and all oh/rsc/*.properties files will not be generated or
# overwritten if already present
MANUAL_CONFIG=off 

######## Define architecture

ARCH=`uname -m`
case $ARCH in
	x86_64|amd64|AMD64)
		JAVA_ARCH=64
		;;
	i[3456789]86|x86|i86pc)
		JAVA_ARCH=32
		;;
	*)
		echo "Unknown architecture: $ARCH. Exiting."
		exit 1
		;;
esac

######## MySQL Software
EXT="tar.gz"
# MariaDB
MYSQL_URL="https://downloads.mariadb.com/MariaDB/mariadb-10.2.37/bintar-linux-x86_64"
MYSQL_DIR="mariadb-10.2.37-linux-$ARCH"
# MySQL
#MYSQL_URL="https://downloads.mysql.com/archives/get/p/23/file"
#MYSQL_DIR="mysql-5.7.32-linux-glibc2.12-$ARCH"

######## JAVA Software
######## JAVA 64bit - default architecture
### JRE 8 - openlogic
#JAVA_DISTRO="openlogic-openjdk-jre-8u262-b10-linux-x64"
#JAVA_URL="https://builds.openlogic.com/downloadJDK/openlogic-openjdk-jre/8u262-b10/"
#JAVA_DIR="openlogic-openjdk-jre-8u262-b10-linux-64"

### JRE 11 - zulu
#JAVA_DISTRO="zulu11.45.27-ca-jre11.0.10-linux_x64"
#JAVA_URL="https://cdn.azul.com/zulu/bin"
#JAVA_DIR="zulu11.45.27-ca-jre11.0.9-linux_x64"

### JRE 11 - openjdk
JAVA_URL="https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.10%2B9"
JAVA_DISTRO="OpenJDK11U-jre_x64_linux_hotspot_11.0.10_9"
JAVA_DIR="jdk-11.0.10+9-jre"

######## JAVA 32bit
if [ $JAVA_ARCH = 32 ]; then
	# Setting JRE 32 bit
	
	### JRE 8 - openlogic 32bit
	#JAVA_DISTRO="openlogic-openjdk-8u262-b10-linux-x32"
	#JAVA_URL="https://builds.openlogic.com/downloadJDK/openlogic-openjdk/8u262-b10/"

	### JRE zulu distro
	JAVA_URL="https://cdn.azul.com/zulu/bin/"
	### JRE 8 32bit
	#JAVA_DISTRO="zulu8.52.0.23-ca-jre8.0.282-linux_i686"
	### JRE 11 32bit
	JAVA_DISTRO="zulu11.45.27-ca-jre11.0.10-linux_i686"

	JAVA_DIR=$JAVA_DISTRO
fi

######## set JAVA_BIN
# Uncomment this if you want to use system wide JAVA
#JAVA_BIN=`which java`

# name of this shell script
SCRIPT_NAME=$(basename "$0")

######################## DO NOT EDIT BELOW THIS LINE ########################

######## User input / option parsing

function script_usage {
	# show help


        # show menu
        # Clear-Host # clear console
        echo " ---------------------------------------------------------"
        echo "|                                                         |"
        echo "|                   Open Hospital | OH                    |"
        echo "|                                                         |"
        echo " ---------------------------------------------------------"
        echo " lang $OH_LANGUAGE | arch $ARCH"
        echo ""
        echo " Usage: $SCRIPT_NAME [ -lang en|fr|it|es|pt ] "
#        echo "               [ -distro PORTABLE|CLIENT ]"
#        echo "               [ -debug INFO|DEBUG ] "
        echo ""
        echo "   -C    start OH in CLIENT mode (Client / Server configuration)"
        echo "   -d    start OH in DEBUG mode"
        echo "   -D    start OH in DEMO mode"
        echo "   -G    setup GSM"
        echo "   -h    show this help"
        echo "   -l    set language: en|fr|it|es|pt"
        echo "   -s    save OH database"
        echo "   -r    restore OH database"
        echo "   -t    test database connection (CLIENT mode only)"
        echo "   -v    show OH software version and configuration"
        echo "   -X    clean OH installation"
        echo ""
	exit 0
}

######## Functions

function get_confirmation {
read -p "(y/n)? " choice
case "$choice" in 
	y|Y ) echo "yes";;
	n|N ) echo "Exiting."; exit 0;;
	* ) echo "Invalid choice. Exiting."; exit 1 ;;
esac
}

function set_path {
	# set current dir
	CURRENT_DIR=$PWD
	# set OH_PATH if not defined
	if [ -z ${OH_PATH+x} ]; then
#		echo "Warning: POH_PATH not found - using current directory"
		OH_PATH=$CURRENT_DIR
		if [ ! -f $OH_PATH/$SCRIPT_NAME ]; then
			echo "Error - oh.sh not found in the current PATH. Please browse to the directory where Open Hospital was unzipped or set up OH_PATH properly."
			exit 1
		fi
	fi
	OH_PATH_ESCAPED=$(echo $OH_PATH | sed -e 's/\//\\\//g')
	DATA_DIR_ESCAPED=$(echo $DATA_DIR | sed -e 's/\//\\\//g')
	TMP_DIR_ESCAPED=$(echo $TMP_DIR | sed -e 's/\//\\\//g')
	LOG_DIR_ESCAPED=$(echo $LOG_DIR | sed -e 's/\//\\\//g')
	DICOM_DIR_ESCAPED=$(echo $DICOM_DIR | sed -e 's/\//\\\//g')
}

function set_language {
	# Set OH interface languange - set default to en
	if [ -z ${OH_LANGUAGE+x} ]; then
		OH_LANGUAGE=en
	fi
	case $OH_LANGUAGE in 
		en|fr|it|es|pt) 
			# set database creation script in chosen language
			DB_CREATE_SQL="create_all_$OH_LANGUAGE.sql"
			;;
		*)
			echo "Invalid option: $OH_LANGUAGE. Exiting."
			exit 1
		;;
	esac
}

function java_lib_setup {
	# NATIVE LIB setup
	case $JAVA_ARCH in
		64)
		NATIVE_LIB_PATH=$OH_PATH/$OH_DIR/lib/native/Linux/amd64
		;;
		32)
		NATIVE_LIB_PATH=$OH_PATH/$OH_DIR/lib/native/Linux/i386
		;;
	esac

	# CLASSPATH setup
	
	# include OH jar
	OH_CLASSPATH=$OH_PATH/$OH_DIR/bin/OH-gui.jar
	# include all needed directories
	OH_CLASSPATH=$OH_CLASSPATH:$OH_PATH/$OH_DIR/bundle
	OH_CLASSPATH=$OH_CLASSPATH:$OH_PATH/$OH_DIR/rpt
	OH_CLASSPATH=$OH_CLASSPATH:$OH_PATH/$OH_DIR/rsc

	# include all jar files under lib/
	DIRLIBS=$OH_PATH/$OH_DIR/lib/*.jar
	for i in ${DIRLIBS}
	do
		OH_CLASSPATH="$i":$OH_CLASSPATH
	done
}

function java_check {
if [ -z ${JAVA_BIN+x} ]; then
	JAVA_BIN=$OH_PATH/$JAVA_DIR/bin/java
fi

if [ ! -x $JAVA_BIN ]; then
	if [ ! -f "$OH_PATH/$JAVA_DISTRO.$EXT" ]; then
		echo "Warning - JAVA not found. Do you want to download it?"
		get_confirmation;
		# Downloading openjdk binaries
		echo "Downloading $JAVA_DISTRO..."
		wget -P $OH_PATH/ $JAVA_URL/$JAVA_DISTRO.$EXT
	fi
	echo "Unpacking $JAVA_DISTRO..."
	tar xf $OH_PATH/$JAVA_DISTRO.$EXT -C $OH_PATH/
	if [ $? -ne 0 ]; then
		echo "Error unpacking Java. Exiting."
		exit 1
	fi
		echo "JAVA unpacked successfully!"
		echo "Removing downloaded file..."
		rm $OH_PATH/$JAVA_DISTRO.$EXT
		echo "Done!"
	fi
# check for java binary
if [ -x $OH_PATH/$JAVA_DIR/bin/java ]; then
	JAVA_BIN=$OH_PATH/$JAVA_DIR/bin/java
	echo "JAVA found!"
	echo "Using $JAVA_DIR"
else 
	echo "JAVA not found! Exiting."
	exit 1
fi
}

function mysql_check {
if [ ! -d "$OH_PATH/$MYSQL_DIR" ]; then
	if [ ! -f "$OH_PATH/$MYSQL_DIR.$EXT" ]; then
		echo "Warning - MariaDB/MySQL not found. Do you want to download it?"
		get_confirmation;
		# Downloading mysql binary
		echo "Downloading $MYSQL_DIR..."
		wget -P $OH_PATH/ $MYSQL_URL/$MYSQL_DIR.$EXT
	fi
	echo "Unpacking $MYSQL_DIR..."
	tar xf $OH_PATH/$MYSQL_DIR.$EXT -C $OH_PATH/
	if [ $? -ne 0 ]; then
		echo "Error unpacking MySQL. Exiting."
		exit 2
	fi
	echo "MySQL unpacked successfully!"
	echo "Removing downloaded file..."
	rm $OH_PATH/$MYSQL_DIR.$EXT
	echo "Done!"
fi
# check for mysql binary
if [ -x $OH_PATH/$MYSQL_DIR/bin/mysqld_safe ]; then
	echo "MySQL found!"
	echo "Using $MYSQL_DIR"
else
	echo "MySQL not found! Exiting."
	exit 2
fi
}

function config_database {
	# Find a free TCP port to run MySQL starting from the default port
	echo "Looking for a free TCP port for MySQL database..."
	while [[ $(ss -tl4  sport = :$MYSQL_PORT | grep LISTEN) ]]; do
		MYSQL_PORT=$(expr $MYSQL_PORT + 1)
	done
	echo "Found TCP port $MYSQL_PORT!"

	# Creating MySQL configuration
	echo "Generating MySQL config file..."
	[ -f $OH_PATH/etc/mysql/my.cnf ] && mv -f $OH_PATH/etc/mysql/my.cnf $OH_PATH/etc/mysql/my.cnf.old
	sed -e "s/MYSQL_SERVER/$MYSQL_SERVER/g" -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/g" -e "s/OH_PATH_SUBSTITUTE/$OH_PATH_ESCAPED/g" \
	-e "s/TMP_DIR/$TMP_DIR_ESCAPED/g" -e "s/DATA_DIR/$DATA_DIR_ESCAPED/g" -e "s/LOG_DIR/$LOG_DIR_ESCAPED/g" \
	-e "s/MYSQL_PORT/$MYSQL_PORT/g" -e "s/MYSQL_DISTRO/$MYSQL_DIR/g" $OH_PATH/etc/mysql/my.cnf.dist > $OH_PATH/etc/mysql/my.cnf
}

function inizialize_database {
	# Recreate directory structure
	mkdir -p "$OH_PATH/$DATA_DIR"
	mkdir -p "$OH_PATH/$TMP_DIR"
	mkdir -p "$OH_PATH/$LOG_DIR"
	mkdir -p "$OH_PATH/$DICOM_DIR"
	mkdir -p "$OH_PATH/$BACKUP_DIR"
	# Inizialize MySQL
	echo "Initializing MySQL database on port $MYSQL_PORT..."
	case "$MYSQL_DIR" in 
	*mariadb*)
		$OH_PATH/$MYSQL_DIR/scripts/mysql_install_db --basedir=$OH_PATH/$MYSQL_DIR --datadir="$OH_PATH/$DATA_DIR" \
		--auth-root-authentication-method=normal >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1
		;;
	*mysql*)
		$OH_PATH/$MYSQL_DIR/bin/mysqld --initialize-insecure --basedir=$OH_PATH/$MYSQL_DIR --datadir=$OH_PATH/$DATA_DIR >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1
		;;
	esac

	if [ $? -ne 0 ]; then
		echo "Error: MySQL initialization failed! Exiting."
		exit 2
	fi
}

function start_database {
	echo "Starting MySQL server... "
	$OH_PATH/$MYSQL_DIR/bin/mysqld_safe --defaults-file=$OH_PATH/etc/mysql/my.cnf >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1 &
	if [ $? -ne 0 ]; then
		echo "Error: Database not started! Exiting."
		exit 2
	fi
	# Wait till the MySQL tcp port is open
	until nc -z $MYSQL_SERVER $MYSQL_PORT; do sleep 1; done
	echo "MySQL server started! "
}

function set_database_root_pw {
	# If using MySQL/MariaDB root password need to be set
	echo "Setting MySQL root password..."
	$OH_PATH/$MYSQL_DIR/bin/mysql -u root --skip-password --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '$MYSQL_ROOT_PW';" >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1
}


function import_database {
	echo "Creating OH Database..."
	# Create OH database and user
	$OH_PATH/$MYSQL_DIR/bin/mysql -u root -p$MYSQL_ROOT_PW --protocol=tcp --host=$MYSQL_SERVER --port=$MYSQL_PORT \
	-e "CREATE DATABASE $DATABASE_NAME; CREATE USER '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; \
	CREATE USER '$DATABASE_USER'@'%' IDENTIFIED BY '$DATABASE_PASSWORD'; GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'localhost'; \
	GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'%' ; " >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1

	# Check for database creation script
	if [ -f $OH_PATH/$SQL_DIR/$DB_CREATE_SQL ]; then
		echo "Using SQL file $SQL_DIR/$DB_CREATE_SQL..."
	else
		echo "No SQL file found! Exiting."
		shutdown_database;
		exit 2
	fi

	# Create OH database structure
	echo "Importing database schema $DB_CREATE_SQL..."
	cd $OH_PATH/$SQL_DIR
	$OH_PATH/$MYSQL_DIR/bin/mysql --local-infile=1 -u root -p$MYSQL_ROOT_PW --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp $DATABASE_NAME < $OH_PATH/$SQL_DIR/$DB_CREATE_SQL >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1
	if [ $? -ne 0 ]; then
		echo "Error: Database not imported! Exiting."
		shutdown_database;
		exit 2
	fi
	echo "Database imported!"
}

function dump_database {
	# Save OH database if existing
	if [ -x "$OH_PATH/$MYSQL_DIR/bin/mysqldump" ]; then
		echo "Dumping MySQL database..."
		# $OH_PATH/$MYSQL_DIR/bin/mysqldump --no-create-info --skip-extended-insert -h $MYSQL_SERVER --port=$MYSQL_PORT -u root $DATABASE_NAME > $OH_PATH/$SQL_DIR/mysqldump_$DATE.sql
		$OH_PATH/$MYSQL_DIR/bin/mysqldump --skip-extended-insert -u root --password=$MYSQL_ROOT_PW --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp $DATABASE_NAME > $OH_PATH/$BACKUP_DIR/mysqldump_$DATE.sql
		if [ $? -ne 0 ]; then
			echo "Error: Database not dumped! Exiting."
			shutdown_database;
			exit 2
		fi
	else
		echo "Error: No mysqldump utility found! Exiting."
		shutdown_database;
		exit 2
	fi
	echo "MySQL dump file $BACKUP_DIR/mysqldump_$DATE.sql completed!"
}

function shutdown_database {
	echo "Shutting down MySQL..."
	$OH_PATH/$MYSQL_DIR/bin/mysqladmin -u root -p$MYSQL_ROOT_PW --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp shutdown >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1
	# Wait till the MySQL tcp port is closed
	until !( nc -z $MYSQL_SERVER $MYSQL_PORT ); do sleep 1; done
}

function clean_database {
	echo "Warning: do you want to remove all data and databases ?"
	get_confirmation;
	echo "--->>> This operation cannot be undone"
	echo "--->>> Are you sure ?"
	get_confirmation;
	echo "Removing data..."
	# remove databases
	rm -rf $OH_PATH/$DATA_DIR/*
	rm -rf $OH_PATH/$TMP_DIR/*
}

function test_database_connection {
	# Test connection to the OH MySQL database
	echo "Testing database connection..."
	DBTEST=$($OH_PATH/$MYSQL_DIR/bin/mysql --user=$DATABASE_USER --password=$DATABASE_PASSWORD --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp -e "USE $DATABASE_NAME" >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1; echo "$?" )
	if [ $DBTEST -eq 0 ];then
		echo "Database connection successfully established!"
	else
		echo "Error: can't connect to database! Exiting."
		exit 2
	fi
}

function clean_files {
	# clean all generated files - leave only .dist files
	echo "Warning: do you want to remove all configuration and log files ?"
	get_confirmation;
	echo "Removing files..."
	rm -f $OH_PATH/etc/mysql/my.cnf
	rm -f $OH_PATH/etc/mysql/my.cnf.old
	rm -f $OH_PATH/$LOG_DIR/*
	rm -f $OH_PATH/$OH_DIR/rsc/generalData.properties
	rm -f $OH_PATH/$OH_DIR/rsc/generalData.properties.old
	rm -f $OH_PATH/$OH_DIR/rsc/database.properties
	rm -f $OH_PATH/$OH_DIR/rsc/database.properties.old
	rm -f $OH_PATH/$OH_DIR/rsc/log4j.properties
	rm -f $OH_PATH/$OH_DIR/rsc/log4j.properties.old
	rm -f $OH_PATH/$OH_DIR/rsc/dicom.properties
	rm -f $OH_PATH/$OH_DIR/rsc/dicom.properties.old
	rm -f $OH_PATH/$OH_DIR/logs/*
}


######## Pre-flight checks

# check user running the script
if [ $(id -u) -eq 0 ]; then
	echo "Error - do not run this script as root. Exiting."
	exit 1
fi

# debug level - set default to INFO
if [ -z ${DEBUG_LEVEL+x} ]; then
	DEBUG_LEVEL=INFO
fi	

######## Environment setup

set_path;
set_language;

######## User input

# list of arguments expected in user the input
OPTIND=1 # Reset in case getopts has been used previously in the shell.
OPTSTRING=":CdDGh?lsrtvX"

# function to parse input
while getopts ${OPTSTRING} opt; do
	case ${opt} in
	d)	# debug
        	echo "Starting Open Hospital in debug mode..."
		DEBUG_LEVEL=DEBUG
		echo "Debug level set to $DEBUG_LEVEL"
		;;
	C)	# start in CLIENT mode
		OH_DISTRO=CLIENT
		;;
	D)	# demo mode
        	echo "Starting Open Hospital in demo mode..."
		# exit if OH is configured in CLIENT mode
		if [ $OH_DISTRO = "CLIENT" ]; then
			echo "Error - OH_DISTRO set to CLIENT mode. Cannot run in DEMO mode, exiting."
			exit 1;
		else OH_DISTRO="PORTABLE"
		fi
		DEMO_MODE="on"
		clean_database;
		;;
	h)	# help
		script_usage;
		;;
	l)	# set language
		OH_LANGUAGE=$OPTARG
		set_language;
		;;
	s)	# save database
		# checking if data exist
		if [ -d $OH_PATH/$DATA_DIR/$DATABASE_NAME ]; then
			mysql_check;
			if [ $MANUAL_CONFIG != "on" ]; then
				config_database;
			fi
			start_database;
	        	echo "Saving Open Hospital database..."
			dump_database;
			shutdown_database;
	        	echo "Done!"
			exit 0
		else
	        	echo "Error: no data found! Exiting."
			exit 1
		fi
		;;
	r)	# restore 
        	echo "Restoring Open Hospital database...."
		clean_database;
		# ask user for database/sql script to restore
		read -p "Enter SQL dump/backup file that you want to restore - (in sql/ subdirectory) -> " DB_CREATE_SQL
		if [ -f $OH_PATH/$SQL_DIR/$DB_CREATE_SQL ]; then
		        echo "Found $SQL_DIR/$DB_CREATE_SQL, restoring it..."
		else
			echo "No SQL file found! Exiting."
			exit 2
		fi
        	# normal startup from here
		;;
	t)	# test database connection
		if [ $OH_DISTRO = PORTABLE ]; then
			echo "Only for CLIENT mode. Exiting."
			exit 1
		fi
		test_database_connection;
		exit 0
		;;
	G)	# set up GSM
		echo "Setting up GSM..."
		java_check;
		java_lib_setup;
		cd $OH_PATH/$OH_DIR
		$JAVA_BIN -Djava.library.path=${NATIVE_LIB_PATH} -classpath "$OH_CLASSPATH" org.isf.utils.sms.SetupGSM "$@"
		exit 0;
		;;
	v)	# show version
        	echo "--------- Software version ---------"
		source $OH_PATH/$OH_DIR/rsc/version.properties
        	echo "Open Hospital version" $VER_MAJOR.$VER_MINOR.$VER_RELEASE
        	echo "MySQL version: $MYSQL_DIR"
        	echo "JAVA version:"
		echo $JAVA_DISTRO
        	echo ""
		# show configuration
        	echo "--------- Configuration ---------"
        	echo "Architecture is $ARCH"
		echo "Open Hospital is configured in $OH_DISTRO mode"
		echo "Language is set to $OH_LANGUAGE"
		echo "DEMO mode is set to $DEMO_MODE"
        	echo ""
		echo "MYSQL_SERVER=$MYSQL_SERVER"
		echo "MYSQL_PORT=$MYSQL_PORT"
		echo "DATABASE_NAME=$DATABASE_NAME"
		echo "DATABASE_USER=$DATABASE_USER"
		echo "DATABASE_PASSWORD=$DATABASE_PASSWORD"
		echo "DICOM_MAX_SIZE=$DICOM_MAX_SIZE"
		echo "OH_DIR=$OH_DIR"
		echo "BACKUP_DIR=$BACKUP_DIR"
		echo "DICOM_DIR=$DICOM_DIR"
		echo "DATA_DIR=$DATA_DIR"
		echo "LOG_DIR=$LOG_DIR"
        	echo ""
		exit 0
		;;
	X)	# clean
        	echo "Cleaning Open Hospital installation..."
		clean_files;
		clean_database;
        	echo "Done!"
		exit 0
		;;
	?)	# default
		echo "Invalid option: -${OPTARG}. See $SCRIPT_NAME -h for help"
		exit 3
		;;
	esac
done

######################## Script start ########################

# check distro
if [ -z ${OH_DISTRO+x} ]; then
	echo "Error - OH_DISTRO not defined [CLIENT - PORTABLE]! Exiting."
	exit 1
fi

# check for demo mode
if [ $DEMO_MODE = "on" ]; then
	# exit if OH is configured in CLIENT mode
	if [ $OH_DISTRO = "CLIENT" ]; then
		echo "Error - OH_DISTRO set to CLIENT mode. Cannot run in DEMO mode, exiting."
		exit 1;
	fi

	if [ -f $OH_PATH/$SQL_DIR/$DB_DEMO ]; then
	        echo "Found SQL demo database, starting OH in demo mode..."
		DB_CREATE_SQL=$DB_DEMO
	else
	      	echo "Error: no $DB_DEMO found! Exiting."
		exit 1
	fi
fi

echo "Starting Open Hospital in $OH_DISTRO mode..."
echo "OH_PATH set to $OH_PATH"
echo "OH language is set to $OH_LANGUAGE"

# check for java
java_check;

# setup java lib
java_lib_setup;

######## Database setup

# Start MySQL and create database
if [ $OH_DISTRO = PORTABLE ]; then
	# Check for MySQL software
	mysql_check;
	# Config MySQL
	if [ $MANUAL_CONFIG != "on" ]; then
		config_database;
	fi
	# Check if OH database already exists
	if [ ! -d $OH_PATH/$DATA_DIR/$DATABASE_NAME ]; then
		# Prepare MySQL
		inizialize_database;
		# Start MySQL
		start_database;	
		# Set database root password
		set_database_root_pw;
		# Create database and load data
		import_database;
	else
		# Starting MySQL
		start_database;
	fi
fi

# test database connection
test_database_connection;

if [ $MANUAL_CONFIG != "on" ]; then

# set up configuration files
echo "Setting up OH configuration files..."

######## DICOM setup
[ -f $OH_PATH/$OH_DIR/rsc/dicom.properties ] && mv -f $OH_PATH/$OH_DIR/rsc/dicom.properties $OH_PATH/$OH_DIR/rsc/dicom.properties.old
sed -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/g" -e "s/OH_PATH_SUBSTITUTE/$OH_PATH_ESCAPED/g" \
    -e "s/DICOM_DIR/$DICOM_DIR_ESCAPED/g" $OH_PATH/$OH_DIR/rsc/dicom.properties.dist > $OH_PATH/$OH_DIR/rsc/dicom.properties

######## log4j.properties setup
[ -f $OH_PATH/$OH_DIR/rsc/log4j.properties ] && mv -f $OH_PATH/$OH_DIR/rsc/log4j.properties $OH_PATH/$OH_DIR/rsc/log4j.properties.old
sed -e "s/DBSERVER/$MYSQL_SERVER/g" -e "s/DBPORT/$MYSQL_PORT/" -e "s/DBUSER/$DATABASE_USER/g" -e "s/DBPASS/$DATABASE_PASSWORD/g" \
    -e "s/DEBUG_LEVEL/$DEBUG_LEVEL/g" $OH_PATH/$OH_DIR/rsc/log4j.properties.dist > $OH_PATH/$OH_DIR/rsc/log4j.properties

######## database.properties setup 
[ -f $OH_PATH/$OH_DIR/rsc/database.properties ] && mv -f $OH_PATH/$OH_DIR/rsc/database.properties $OH_PATH/$OH_DIR/rsc/database.properties.old
sed -e "s/DBSERVER/$MYSQL_SERVER/g" -e "s/DBPORT/$MYSQL_PORT/g" -e"s/DBNAME/$DATABASE_NAME/g" \
    -e "s/DBUSER/$DATABASE_USER/g" -e "s/DBPASS/$DATABASE_PASSWORD/g" \
$OH_PATH/$OH_DIR/rsc/database.properties.dist > $OH_PATH/$OH_DIR/rsc/database.properties

######## generalData.properties language setup 
# set language in OH config file
[ -f $OH_PATH/$OH_DIR/rsc/generalData.properties ] && mv -f $OH_PATH/$OH_DIR/rsc/generalData.properties $OH_PATH/$OH_DIR/rsc/generalData.properties.old
sed -e "s/OH_SET_LANGUAGE/$OH_LANGUAGE/g" $OH_PATH/$OH_DIR/rsc/generalData.properties.dist > $OH_PATH/$OH_DIR/rsc/generalData.properties

fi

######## Open Hospital start

echo "Starting Open Hospital..."

cd $OH_PATH/$OH_DIR

# OH GUI launch
$JAVA_BIN -Dsun.java2d.dpiaware=false -Djava.library.path=${NATIVE_LIB_PATH} -classpath $OH_CLASSPATH org.isf.menu.gui.Menu >> $OH_PATH/$LOG_DIR/$LOG_FILE 2>&1

echo "Exiting Open Hospital..."

if [ $OH_DISTRO = PORTABLE ]; then
	shutdown_database;
fi

# go back to starting directory
cd $CURRENT_DIR

# exiting
echo "Done!"
exit 0
