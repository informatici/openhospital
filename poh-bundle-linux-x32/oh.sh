#!/bin/bash
#
# Open Hospital (www.open-hospital.org)
# Copyright © 2006-2020 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
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

######## Open Hospital - Portable Open Hospital Configuration
# POH_PATH is the directory where Portable OpenHospital files are located
# POH_PATH=/usr/local/PortableOpenHospital

OH_DISTRO=portable
#OH_DISTRO=client
DEMO_MODE=off

# Language setting - default set to en
#OH_LANGUAGE=en fr es it pt
#OH_LANGUAGE=it

# set debug level to INFO | DEBUG - default set to INFO
#DEBUG_LEVEL=INFO

######## Software configuration - change at your own risk :-)
# Database
MYSQL_SERVER=localhost
#MYSQL_SERVER="127.0.0.1"
MYSQL_PORT=3306
MYSQL_ROOT_PW="root2020oh"
DATABASE_NAME=oh
DATABASE_USER=isf
DATABASE_PASSWORD=isf123

DICOM_MAX_SIZE="4M"

OH_DIR=oh
SQL_DIR=sql
MYSQL_SOCKET="var/run/mysqld/mysql.sock"
MYSQL_DATA_DIR="var/lib/mysql/"
#DB_CREATE_SQL="create_all_en.sql" # default to create_all_en.sql
DB_DEMO="create_all_demo.sql"
DATE=`date +%Y-%m-%d_%H-%M-%S`

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
# MariaDB
MYSQL_URL="https://downloads.mariadb.com/MariaDB/mariadb-10.2.36/bintar-linux-x86_64"
MYSQL_DIR="mariadb-10.2.36-linux-$ARCH"
# MySQL
#MYSQL_DIR="mysql-5.7.30-linux-glibc2.12-$ARCH"
#MYSQL_URL="https://downloads.mysql.com/archives/get/p/23/file"
EXT="tar.gz"

######## JAVA Software
######## JAVA 64bit - default architecture
### JRE 8 - openlogic
#JAVA_DISTRO="openlogic-openjdk-jre-8u262-b10-linux-x64"
#JAVA_URL="https://builds.openlogic.com/downloadJDK/openlogic-openjdk-jre/8u262-b10/"
#JAVA_DIR="openlogic-openjdk-jre-8u262-b10-linux-64"

### JRE 11 - zulu
#JAVA_DISTRO="zulu11.43.21-ca-jre11.0.9-linux_x64"
#JAVA_URL="https://cdn.azul.com/zulu/bin"
#JAVA_DIR="zulu11.43.21-ca-jre11.0.9-linux_x64"

### JRE 11 - openjdk
JAVA_URL="https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.9%2B11.1"
JAVA_DISTRO="OpenJDK11U-jre_x64_linux_hotspot_11.0.9_11"
JAVA_DIR="jdk-11.0.9+11-jre"

######## JAVA 32bit
if [ $JAVA_ARCH = 32 ]; then
	# Setting JRE 32 bit
	
	### JRE 8 - openlogic 32bit
	#JAVA_DISTRO="openlogic-openjdk-8u262-b10-linux-x32"
	#JAVA_URL="https://builds.openlogic.com/downloadJDK/openlogic-openjdk/8u262-b10/"
	#JAVA_DIR="openlogic-openjdk-8u262-b10-linux-32"

	### JRE 8 - zulu 32bit
	JAVA_DISTRO="zulu8.50.0.21-ca-jre8.0.272-linux_i686"
	JAVA_URL="https://cdn.azul.com/zulu/bin/"
	JAVA_DIR="zulu8.50.0.21-ca-jre8.0.272-linux_i686"

	### JRE 11 32bit
	#JAVA_DISTRO="zulu11.43.21-ca-jre11.0.9-linux_i686"
	#JAVA_URL="https://cdn.azul.com/zulu/bin/"
	#JAVA_DIR="zulu11.43.21-ca-jre11.0.9-linux_i686"
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
	echo ""
	echo " Portable Open Hospital Client - OH"
	echo ""
	echo " Usage: $SCRIPT_NAME [-option]"
	echo ""
	echo "   -l    en|fr|it|es|pt   --> set language [default set to en]"
	echo ""
	echo "   -s    save OH database"
	echo "   -r    restore OH database"
	echo "   -c    clean POH installation"
	echo "   -d    start POH in debug mode"
	echo "   -C    start Open Hospital - Client / Server mode"
	echo "   -t    test database connection (Client mode only)"
	echo "   -v    show POH version information"
	echo "   -D    start POH in Demo mode"
	echo "   -G    Setup GSM"
	echo "   -h    show this help"
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
	# set POH_PATH if not defined
	if [ -z ${POH_PATH+x} ]; then
#		echo "Warning: POH_PATH not found - using current directory"
		POH_PATH=$CURRENT_DIR
		if [ ! -f $POH_PATH/$SCRIPT_NAME ]; then
			echo "Error - oh.sh not found in the current PATH. Please cd the directory where POH was unzipped or set up POH_PATH properly."
			exit 1
		fi
	fi
	POH_PATH_ESCAPED=$(echo $POH_PATH | sed -e 's/\//\\\//g')
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
		NATIVE_LIB_PATH=$POH_PATH/$OH_DIR/lib/native/Linux/amd64
		;;
		32)
		NATIVE_LIB_PATH=$POH_PATH/$OH_DIR/lib/native/Linux/i386
		;;
	esac

	# CLASSPATH setup
	OH_CLASSPATH=$POH_PATH/$OH_DIR/bin/OH-gui.jar
	OH_CLASSPATH=$OH_CLASSPATH:$POH_PATH/$OH_DIR/bundle
	OH_CLASSPATH=$OH_CLASSPATH:$POH_PATH/$OH_DIR/rpt
	OH_CLASSPATH=$OH_CLASSPATH:$POH_PATH/$OH_DIR/rsc

	DIRLIBS=$POH_PATH/$OH_DIR/lib/*.jar
	for i in ${DIRLIBS}
	do
		OH_CLASSPATH="$i":$OH_CLASSPATH
	done
}

function java_check {
if [ -z ${JAVA_BIN+x} ]; then
	JAVA_BIN=$POH_PATH/$JAVA_DIR/bin/java
fi

if [ ! -x $JAVA_BIN ]; then
	if [ ! -f "$POH_PATH/$JAVA_DISTRO.$EXT" ]; then
		echo "Warning - JAVA not found. Do you want to download it? (50 MB)"
		get_confirmation;
		# Downloading openjdk binaries
		echo "Downloading $JAVA_DISTRO..."
		wget -P $POH_PATH/ $JAVA_URL/$JAVA_DISTRO.$EXT
	fi
	echo "Unpacking $JAVA_DISTRO..."
	tar xf $POH_PATH/$JAVA_DISTRO.$EXT -C $POH_PATH/
	if [ $? -ne 0 ]; then
		echo "Error unpacking Java. Exiting."
		exit 1
	fi
		echo "JAVA unpacked successfully!"
		echo "Removing downloaded file..."
		rm $POH_PATH/$JAVA_DISTRO.$EXT
		echo "Done!"
	fi
# check for java binary
if [ -x $POH_PATH/$JAVA_DIR/bin/java ]; then
	JAVA_BIN=$POH_PATH/$JAVA_DIR/bin/java
	echo "JAVA found!"
	echo "Using $JAVA_DIR"
else 
	echo "JAVA not found! Exiting."
	exit 1
fi
}

function mysql_check {
if [ ! -d "$POH_PATH/$MYSQL_DIR" ]; then
	if [ ! -f "$POH_PATH/$MYSQL_DIR.$EXT" ]; then
		echo "Warning - MySQL not found. Do you want to download it? (630 MB)"
		get_confirmation;
		# Downloading mysql binary
		echo "Downloading $MYSQL_DIR..."
		wget -P $POH_PATH/ $MYSQL_URL/$MYSQL_DIR.$EXT
	fi
	echo "Unpacking $MYSQL_DIR..."
	tar xf $POH_PATH/$MYSQL_DIR.$EXT -C $POH_PATH/
	if [ $? -ne 0 ]; then
		echo "Error unpacking MySQL. Exiting."
		exit 2
	fi
	echo "MySQL unpacked successfully!"
	echo "Removing downloaded file..."
	rm $POH_PATH/$MYSQL_DIR.$EXT
	echo "Done!"
fi
# check for mysql binary
if [ -x $POH_PATH/$MYSQL_DIR/bin/mysqld_safe ]; then
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
	[ -f $POH_PATH/etc/mysql/my.cnf ] && mv -f $POH_PATH/etc/mysql/my.cnf $POH_PATH/etc/mysql/my.cnf.old
	sed -e "s/MYSQL_SERVER/$MYSQL_SERVER/g" -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/g" -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" \
	    -e "s/MYSQL_PORT/$MYSQL_PORT/" -e "s/MYSQL_DISTRO/$MYSQL_DIR/g" $POH_PATH/etc/mysql/my.cnf.dist > $POH_PATH/etc/mysql/my.cnf
}

function inizialize_database {
	# Recreate directory structure
	mkdir -p $POH_PATH/$MYSQL_DATA_DIR
	mkdir -p $POH_PATH/var/run/mysqld
	mkdir -p $POH_PATH/var/log/mysql
	# Inizialize MySQL
	echo "Initializing MySQL database on port $MYSQL_PORT..."
	case "$MYSQL_DIR" in 
	*mariadb*)
#		$POH_PATH/$MYSQL_DIR/scripts/mysql_install_db --socket=$POH_PATH/$MYSQL_SOCKET --basedir="$POH_PATH/$MYSQL_DIR" --datadir="$POH_PATH/$MYSQL_DATA_DIR" \
		$POH_PATH/$MYSQL_DIR/scripts/mysql_install_db --basedir=$POH_PATH/$MYSQL_DIR --datadir="$POH_PATH/$MYSQL_DATA_DIR" \
		--auth-root-authentication-method=normal 2>&1 > /dev/null
		;;
	*mysql*)
#		$POH_PATH/$MYSQL_DIR/bin/mysqld --initialize-insecure --socket=$POH_PATH/$MYSQL_SOCKET --basedir=$POH_PATH/$MYSQL_DIR --datadir=$POH_PATH/$MYSQL_DATA_DIR 2>&1 > /dev/null
		$POH_PATH/$MYSQL_DIR/bin/mysqld --initialize-insecure --basedir=$POH_PATH/$MYSQL_DIR --datadir=$POH_PATH/$MYSQL_DATA_DIR 2>&1 > /dev/null
		;;
	esac

	if [ $? -ne 0 ]; then
		echo "Error: MySQL initialization failed! Exiting."
		exit 2
	fi
}

function start_database {
	echo "Starting MySQL server... "
	$POH_PATH/$MYSQL_DIR/bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &
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
	$POH_PATH/$MYSQL_DIR/bin/mysql -u root --skip-password --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '$MYSQL_ROOT_PW';" 2>&1 > /dev/null
}


function import_database {
	echo "Creating OH Database..."
	# Create OH database and user
	$POH_PATH/$MYSQL_DIR/bin/mysql -u root -p$MYSQL_ROOT_PW --protocol=tcp --host=$MYSQL_SERVER --port=$MYSQL_PORT \
	-e "CREATE DATABASE $DATABASE_NAME; CREATE USER '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; \
	CREATE USER '$DATABASE_USER'@'%' IDENTIFIED BY '$DATABASE_PASSWORD'; GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'localhost'; \
	GRANT ALL PRIVILEGES ON $DATABASE_NAME.* TO '$DATABASE_USER'@'%' ; "

	# Check for database creation script
	if [ -f $POH_PATH/$SQL_DIR/$DB_CREATE_SQL ]; then
		echo "Using SQL file $SQL_DIR/$DB_CREATE_SQL..."
	else
		echo "No SQL file found! Exiting."
		shutdown_database;
		exit 2
	fi

	# Create OH database structure
	echo "Importing database schema $DB_CREATE_SQL..."
	cd $POH_PATH/$SQL_DIR
	$POH_PATH/$MYSQL_DIR/bin/mysql --local-infile=1 -u root -p$MYSQL_ROOT_PW --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp $DATABASE_NAME < $POH_PATH/$SQL_DIR/$DB_CREATE_SQL
	if [ $? -ne 0 ]; then
		echo "Error: Database not imported! Exiting."
		shutdown_database;
		exit 2
	fi
	echo "Database imported!"
}

function dump_database {
	# Save OH database if existing
	if [ -x "$POH_PATH/$MYSQL_DIR/bin/mysqldump" ]; then
		echo "Dumping MySQL database..."
		# $POH_PATH/$MYSQL_DIR/bin/mysqldump --no-create-info --skip-extended-insert -h $MYSQL_SERVER --port=$MYSQL_PORT -u root $DATABASE_NAME > $POH_PATH/$SQL_DIR/mysqldump_$DATE.sql
		$POH_PATH/$MYSQL_DIR/bin/mysqldump --skip-extended-insert -u root --password=$MYSQL_ROOT_PW --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp $DATABASE_NAME > $POH_PATH/$SQL_DIR/mysqldump_$DATE.sql
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
	echo "MySQL dump file $SQL_DIR/mysqldump_$DATE.sql completed!"
}

function shutdown_database {
	echo "Shutting down MySQL..."
	$POH_PATH/$MYSQL_DIR/bin/mysqladmin -u root -p$MYSQL_ROOT_PW --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp shutdown 2>&1 > /dev/null
	# Wait till the MySQL tcp port is closed
	until !( nc -z $MYSQL_SERVER $MYSQL_PORT ); do sleep 1; done
}

function clean_database {
	echo "Warning: do you want to remove all data and database ?"
	get_confirmation;
	echo "Removing data..."
	# remove databases
	rm -rf $POH_PATH/$MYSQL_DATA_DIR/*
	rm -ff $POH_PATH/var/run/mysqld/*
}

function test_database_connection {
	# Test connection to the OH MySQL database
	echo "Testing database connection..."
	DBTEST=$($POH_PATH/$MYSQL_DIR/bin/mysql --user=$DATABASE_USER --password=$DATABASE_PASSWORD --host=$MYSQL_SERVER --port=$MYSQL_PORT --protocol=tcp -e "USE $DATABASE_NAME" >/dev/null 2>&1; echo "$?" )
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
	rm -f $POH_PATH/etc/mysql/my.cnf
	rm -f $POH_PATH/etc/mysql/my.cnf.old
	rm -f $POH_PATH/var/log/mysql/*
	rm -f $POH_PATH/$OH_DIR/rsc/generalData.properties
	rm -f $POH_PATH/$OH_DIR/rsc/generalData.properties.old
	rm -f $POH_PATH/$OH_DIR/rsc/database.properties
	rm -f $POH_PATH/$OH_DIR/rsc/database.properties.old
	rm -f $POH_PATH/$OH_DIR/rsc/log4j.properties
	rm -f $POH_PATH/$OH_DIR/rsc/log4j.properties.old
	rm -f $POH_PATH/$OH_DIR/rsc/dicom.properties
	rm -f $POH_PATH/$OH_DIR/rsc/dicom.properties.old
	rm -f $POH_PATH/$OH_DIR/logs/*
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
OPTSTRING=":h?rcsdCtGDvl:"

# function to parse input
while getopts ${OPTSTRING} opt; do
	case ${opt} in
	h)	# help
		script_usage;
		;;
	r)	# restore 
        	echo "Restoring Portable Open Hospital database...."
		clean_database;
		# ask user for database/sql script to restore
		read -p "Enter SQL dump/backup file that you want to restore - (in sql/ subdirectory) -> " DB_CREATE_SQL
		if [ -f $POH_PATH/$SQL_DIR/$DB_CREATE_SQL ]; then
		        echo "Found $SQL_DIR/$DB_CREATE_SQL, restoring it..."
		else
			echo "No SQL file found! Exiting."
			exit 2
		fi
        	echo "Restore ready!"
		;;

	c)	# clean
        	echo "Cleaning Portable Open Hospital installation..."
		clean_files;
		clean_database;
        	echo "Done!"
		exit 0
		;;
	d)	# debug
        	echo "Starting Portable Open Hospital in debug mode..."
		DEBUG_LEVEL=DEBUG
		echo "Debug level set to $DEBUG_LEVEL"
		;;
	s)	# save database
		# checking if data exist
		if [ -d $POH_PATH/$MYSQL_DATA_DIR/$DATABASE_NAME ]; then
			mysql_check;
			config_database;
			start_database;
	        	echo "Saving Portable Open Hospital database..."
			dump_database;
			shutdown_database;
	        	echo "Done!"
			exit 0
		else
	        	echo "Error: no data found! Exiting."
			exit 1
		fi
		;;
	C)	# start in client mode
		OH_DISTRO=client
		;;
	l)	# set language
		OH_LANGUAGE=$OPTARG
		set_language;
		;;
	t)	# test database connection
		if [ $OH_DISTRO = portable ]; then
			echo "Only for client mode. Exiting."
			exit 1
		fi
		test_database_connection;
		exit 0
		;;
	G)	# set up GSM
		echo "Setting up GSM..."
		java_check;
		java_lib_setup;
		cd $POH_PATH/$OH_DIR
		$JAVA_BIN -Djava.library.path=${NATIVE_LIB_PATH} -classpath "$OH_CLASSPATH" org.isf.utils.sms.SetupGSM "$@"
		exit 0;
		;;
	D)	# demo mode
        	echo "Starting Portable Open Hospital in demo mode..."
		OH_DISTRO=portable
		DEMO_MODE="on"
		;;
	v)	# show versions
        	echo "Architecture is $ARCH"
		echo "Open Hospital is configured in $OH_DISTRO mode"
		echo "Language is set to $OH_LANGUAGE"
		echo "Demo mode is set to $DEMO_MODE"
        	echo "Software versions:"
		source $POH_PATH/$OH_DIR/rsc/version.properties
        	echo "Open Hospital version" $VER_MAJOR.$VER_MINOR.$VER_RELEASE
        	echo "MySQL version: $MYSQL_DIR"
        	echo "JAVA version:"
		echo $JAVA_DISTRO
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
	echo "Error - OH_DISTRO not defined [client - portable]! Exiting."
	exit 1
fi

# check for demo mode
if [ $DEMO_MODE = "on" ]; then
	if [ -f $POH_PATH/$SQL_DIR/$DB_DEMO ]; then
	        echo "Found SQL demo database, starting OH in demo mode..."
		DB_CREATE_SQL=$DB_DEMO
	else
	      	echo "Error: no $DB_DEMO found! Exiting."
		exit 1
	fi
fi

echo "Starting Open Hospital in $OH_DISTRO mode..."
echo "POH_PATH set to $POH_PATH"
echo "POH language is set to $OH_LANGUAGE"

# check for java
java_check;

# setup java lib
java_lib_setup;

######## Database setup

# Start MySQL and create database
if [ $OH_DISTRO = portable ]; then
	# Check for MySQL software
	mysql_check;
	# Config MySQL
	config_database;
	# Check if OH database already exists
	if [ ! -d $POH_PATH/$MYSQL_DATA_DIR/$DATABASE_NAME ]; then
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

# test database
test_database_connection;

echo "Setting up OH configuration files..."

######## DICOM setup
[ -f $POH_PATH/$OH_DIR/rsc/dicom.properties ] && mv -f $POH_PATH/$OH_DIR/rsc/dicom.properties $POH_PATH/$OH_DIR/rsc/dicom.properties.old
sed -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/" -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" $POH_PATH/$OH_DIR/rsc/dicom.properties.dist > $POH_PATH/$OH_DIR/rsc/dicom.properties

######## log4j.properties setup
[ -f $POH_PATH/$OH_DIR/rsc/log4j.properties ] && mv -f $POH_PATH/$OH_DIR/rsc/log4j.properties $POH_PATH/$OH_DIR/rsc/log4j.properties.old
sed -e "s/DBSERVER/$MYSQL_SERVER/" -e "s/DBPORT/$MYSQL_PORT/" -e "s/DBUSER/$DATABASE_USER/" -e "s/DBPASS/$DATABASE_PASSWORD/" -e "s/DEBUG_LEVEL/$DEBUG_LEVEL/" \
$POH_PATH/$OH_DIR/rsc/log4j.properties.dist > $POH_PATH/$OH_DIR/rsc/log4j.properties

######## database.properties setup 
[ -f $POH_PATH/$OH_DIR/rsc/database.properties ] && mv -f $POH_PATH/$OH_DIR/rsc/database.properties $POH_PATH/$OH_DIR/rsc/database.properties.old
sed -e "s/DBSERVER/$MYSQL_SERVER/" -e "s/DBPORT/$MYSQL_PORT/" -e"s/DBNAME/$DATABASE_NAME/" \
-e "s/DBUSER/$DATABASE_USER/" -e "s/DBPASS/$DATABASE_PASSWORD/" \
$POH_PATH/$OH_DIR/rsc/database.properties.dist > $POH_PATH/$OH_DIR/rsc/database.properties

######## generalData.properties language setup 
# set language in OH config file
[ -f $POH_PATH/$OH_DIR/rsc/generalData.properties ] && mv -f $POH_PATH/$OH_DIR/rsc/generalData.properties $POH_PATH/$OH_DIR/rsc/generalData.properties.old
sed -e "s/OH_SET_LANGUAGE/$OH_LANGUAGE/" $POH_PATH/$OH_DIR/rsc/generalData.properties.dist > $POH_PATH/$OH_DIR/rsc/generalData.properties

######## Open Hospital start

echo "Starting Open Hospital..."

cd $POH_PATH/$OH_DIR

# OH GUI launch
$JAVA_BIN -Dsun.java2d.dpiaware=false -Djava.library.path=${NATIVE_LIB_PATH} -classpath $OH_CLASSPATH org.isf.menu.gui.Menu 2>&1 > /dev/null

echo "Exiting Open Hospital..."

if [ $OH_DISTRO = portable ]; then
	shutdown_database;
fi

# go back to starting directory
cd $CURRENT_DIR

# exiting
echo "Done!"
exit 0
