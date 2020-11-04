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

######## Environment check:

######## OPENHOSPITAL Configuration
# POH_PATH is the directory where Portable OpenHospital files are located
# POH_PATH=/usr/local/PortableOpenHospital

######## Define architecture

ARCH=`uname -m`
case $ARCH in
	x86_64|amd64|AMD64)
		NATIVE_LIB_PATH=$POH_PATH/$OH_DIR/lib/native/Linux/amd64
		JAVA_ARCH=64
		;;
	i[3456789]86|x86|i86pc)
		NATIVE_LIB_PATH=$POH_PATH/$OH_DIR/lib/native/Linux/i386
		JAVA_ARCH=32
		;;
	*)
		echo "Unknown architecture $(uname -m)"
		exit 1
		;;
esac

######## Open Hospital configuration
OH_DISTRO=portable
#OH_DISTRO=client

######## Software configuration - change at your own risk :-)

# Database
MYSQL_PORT=3306
MYSQL_SERVER="127.0.0.1"
MYSQL_SOCKET="var/run/mysqld/mysql.sock"

DATABASE_NAME="oh"
DATABASE_USER="isf"
DATABASE_PASSWORD="isf123"
DICOM_MAX_SIZE="4M"

OH_DIR="oh"
SQL_DIR="sql"
DB_CREATE_SQL="database.sql"
DB_ARCHIVED_SQL="database.sql.imported"
DB_DEMO="demo.sql"
DATE=`date +%Y-%m-%d_%H-%M-%S`

######## MySQL Software
MYSQL_DIR="mysql-5.7.30-linux-glibc2.12-$ARCH"
MYSQL_URL="https://downloads.mysql.com/archives/get/p/23/file"

######## JAVA 64bit - Default architecture
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
	echo "Usage: $(basename $0) [-h -r -d -c -s -v -z]" 2>&1
        echo "   -h       shows this help"
        echo "   -r       restore Portable Open Hospital installation"
        echo "   -d       start Portable Open Hospital in demo mode (experimental - not working)"
        echo "   -c       clean Portable Open Hospital installation"
        echo "   -s       save Portable Open Hospital database data"
        echo "   -v       show Portable Open Hospital version information"
        echo "   -z       start Open Hospital client"
	exit 0
}

######## Functions

function set_path {
	# set current dir
	CURRENT_DIR=$PWD
	# set POH_PATH if not defined
	if [ -z $POH_PATH ]; then
	echo "Warning: POH_PATH not found - using current directory"
		if [ ! -f ./$SCRIPT_NAME ]; then
		echo "Error - oh.sh not found in the current PATH. Please cd the directory where POH was unzipped or set up POH_PATH properly."
		exit 1
		fi
	POH_PATH=$PWD
	POH_PATH_ESCAPED=$(echo $POH_PATH | sed -e 's/\//\\\//g')
	fi
}

function restore_db {
	if [ -f $POH_PATH/$SQL_DIR/$DB_CREATE_SQL ]; then
	        echo "Found SQL creation script!"
	elif [ -f $POH_PATH/$SQL_DIR/$DB_ARCHIVED_SQL ];
	then
	        echo "Found archived SQL creation script, restoring it..."
		mv $POH_PATH/$SQL_DIR/$DB_ARCHIVED_SQL $POH_PATH/$SQL_DIR/$DB_CREATE_SQL
	fi
}

function demo_mode {
	if [ -f $POH_PATH/$SQL_DIR/$DB_DEMO ]; then
	        echo "Found SQL Demo database, starting OH in demo mode...."
		DB_CREATE_SQL=$DB_DEMO

#		inizialize_database;
#		start_database;
#		cd $POH_PATH/sql
#	elif [ -f $POH_PATH/$SQL_DIR/$DB_ARCHIVED_SQL ];
#	then
#	        echo "Found archived SQL creation script, restoring and starting OH...."
#		mv $POH_PATH/$SQL_DIR/$DB_ARCHIVED_SQL $POH_PATH/$SQL_DIR/$DB_CREATE_SQL
	fi
}


# MySQL
function mysql_check {
if [ ! -d "$POH_PATH/$MYSQL_DIR" ]; then
	if [ ! -f "$POH_PATH/$MYSQL_DIR.tar.gz" ]; then

		echo "Warning - MySQL  not found. Do you want to download it ? (630 MB)"

		read -p "(y/n)?" choice
		case "$choice" in 
			y|Y ) echo "yes";;
			n|N ) echo "Exiting..."; exit 0;;
			* ) echo "Invalid choice"; exit 1 ;;
		esac

		# Downloading mysql binary
		echo "Downloading $MYSQL_DIR..."
		wget $MYSQL_URL/$MYSQL_DIR.tar.gz
	fi
	echo "Unpacking $MYSQL_DIR..."
	tar zxvf $MYSQL_DIR.tar.gz
	echo "Removing downloaded file"
	rm $MYSQL_DIR.tar.gz
	echo "Done !"
else	
	echo "MySQL found !"
	echo "Using $MYSQL_DIR"
fi
}

# Java
function java_check {
if [ ! -x $JAVA_BIN ]; then
	if [ ! -f "$POH_PATH/$JAVA_DISTRO.tar.gz" ]; then

		echo "Warning - JAVA  not found. Do you want to download it ? (50 MB)"

		read -p "(y/n)?" choice
		case "$choice" in 
			y|Y ) echo "yes";;
			n|N ) echo "Exiting..."; exit 0;;
			* ) echo "Invalid choice"; exit 1 ;;
		esac

		# Downloading openjdk binaries
		echo "Downloading $JAVA_DISTRO..."
		wget $JAVA_URL/$JAVA_DISTRO.tar.gz
	fi
	echo "Unpacking $JAVA_DISTRO..."
	tar zxvf $JAVA_DISTRO.tar.gz
	echo "Removing downloaded file..."
#	rm $JAVA_DISTRO.tar.gz
	echo "Done !"
else
	echo "Java found !"
fi
}

function start_database {
	echo "Starting MySQL server... "
	$POH_PATH/$MYSQL_DIR/bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &
	if [ $? -ne 0 ]; then
		echo "Error: Database not started!"
		exit 2
	fi
	# Wait till the MySQL socket file is created
	while [ ! -e $POH_PATH/$MYSQL_SOCKET ]; do sleep 1; done
	echo "MySQL server started! "
}

function shutdown_database {
	echo "Shutting down MySQL... "
	$POH_PATH/$MYSQL_DIR/bin/mysqladmin --host=$MYSQL_SERVER --port=$MYSQL_PORT --user=root shutdown 2>&1 > /dev/null
	# Wait till the MySQL socket file is removed
	while [ -e $POH_PATH/$MYSQL_SOCKET ]; do sleep 1; done
}

function config_database {
	# Find a free TCP port to run MySQL starting from the default port
	echo "Looking for a free TCP port for MySQL database..."
	while [ $(ss -tna | awk '{ print $4 }' | grep ":$MYSQL_PORT") ]; do
		MYSQL_PORT=$(expr $MYSQL_PORT + 1)
	done
	echo "Found TCP port $MYSQL_PORT!"

	# Creating MySQL configuration
	#rm -f $POH_PATH/etc/mysql/my.cnf
	echo "Generating MySQL config file..."
	sed -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/g" -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" -e "s/MYSQL_PORT/$MYSQL_PORT/" -e "s/MYSQL_DISTRO/$MYSQL_DIR/g" $POH_PATH/etc/mysql/my.cnf.dist > $POH_PATH/etc/mysql/my.cnf
}

function inizialize_database {
	# Recreate directory structure
	rm -rf $POH_PATH/var/lib/mysql
	mkdir -p $POH_PATH/var/lib/mysql
	mkdir -p $POH_PATH/var/log/mysql
	# Inizialize MySQL
	echo "Initializing MySQL database on port $MYSQL_PORT..."
	$POH_PATH/$MYSQL_DIR/bin/mysqld --initialize-insecure --basedir=$POH_PATH/$MYSQL_DIR --datadir=$POH_PATH/var/lib/mysql
	if [ $? -ne 0 ]; then
		echo "Error: MySQL initialization failed!"
		exit 2
	fi
}

function load_database () {
	echo "Dropping OH Database (if existing)..."
	$POH_PATH/$MYSQL_DIR/bin/mysql -u root -h $MYSQL_SERVER --port=$MYSQL_PORT -e "DROP DATABASE IF EXISTS $DATABASE_NAME;"

	echo "Creating OH Database..."
	$POH_PATH/$MYSQL_DIR/bin/mysql -u root -h $MYSQL_SERVER --port=$MYSQL_PORT -e "CREATE DATABASE $DATABASE_NAME; GRANT ALL ON $DATABASE_NAME.* TO '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; GRANT ALL ON $DATABASE_NAME.* TO '$DATABASE_USER'@'%' IDENTIFIED BY '$DATABASE_PASSWORD';"

	echo "Importing database schema $DB_CREATE_SQL..."
	cd $POH_PATH/sql
	$POH_PATH/$MYSQL_DIR/bin/mysql -u root -h $MYSQL_SERVER --port=$MYSQL_PORT $DATABASE_NAME < $POH_PATH/$SQL_DIR/$DB_CREATE_SQL
	if [ $? -ne 0 ]; then
		echo "Error: Database not initialized!"
		exit 2
	fi
	cd $POH_PATH/
	echo "Database initialized !"
}

function dump_database {
	echo "Dumping MySQL database... "
	$POH_PATH/$MYSQL_DIR/bin/mysqldump -h $MYSQL_SERVER --port=$MYSQL_PORT -u root $DATABASE_NAME > $POH_PATH/$SQL_DIR/mysqldump_$DATE.sql
	if [ $? -ne 0 ]; then
		echo "Error: Database not dumped!"
		exit 2
	fi
	echo "MySQL dump file $SQL_DIR/mysqldump_$DATE.sql completed ! "
}

function clean {
	restore_db;
	echo "Removing files... "
	rm -f $POH_PATH/etc/mysql/my.cnf
	rm -f $POH_PATH/$OH_DIR/rsc/database.properties
	rm -f $POH_PATH/$OH_DIR/rsc/log4j.properties
	rm -f $POH_PATH/$OH_DIR/rsc/dicom.properties
	rm -rf $POH_PATH/var/lib/mysql
	rm -f $POH_PATH/var/run/mysqld/*.sock*
	rm -f $POH_PATH/var/run/mysqld/*.pid*
}


# list of arguments expected in the input
optstring=":hrdcsvz"

# function to parse input
while getopts ${optstring} arg; do
	case ${arg} in
	"h")
		script_usage;
		;;
	"r")
        	echo "Resetting Portable Open Hospital installation...."
		set_path;
		restore_db;
		;;
	"d")
        	echo "Starting Portable Open Hospital in demo mode..."
		set_path;
		demo_mode;
		;;
	"c")
        	echo "Cleaning Portable Open Hospital installation..."
		set_path;
		clean;
        	echo "Done !"
		exit 0
		;;
	"s")
        	echo "Saving Portable Open Hospital database"
		set_path;
		start_database;
		dump_database;
		shutdown_database;
        	echo "Done !"
		exit 0
		;;
	"v")	# show versions
        	echo "Architecture is $ARCH"
        	echo "Showing software versions:"
		source $OH_DIR/rsc/version.properties
        	echo "Open Hospital version" $VER_MAJOR.$VER_MINOR.$VER_RELEASE
        	echo "MySQL version: $MYSQL_DIR"
        	echo "JAVA version:"
		echo $JAVA_DISTRO
		exit 0;
		;;
	"z")
        	echo "Starting Open Hospital client.."
		OH_DISTRO=client
		;;
	?)
		echo "Invalid option: -${OPTARG}. See -h for help"
		exit 2
		;;
	esac
done


######## Script start

# check user
if [ $OH_DISTRO = portable ]; then
	if [ $(id -u) -eq 0 ]; then
		echo "Error - do not run this script as root."
	exit 1
	fi
fi

set_path;

# set JAVA_BIN
if [ -z $JAVA_BIN ]; then
	JAVA_BIN=$POH_PATH/$JAVA_DIR/bin/java
fi

######## Environment setup

cd $POH_PATH

echo "Setting up environment...."

######## DICOM setup
echo "Setting up configuration files...."

#DICOM_MAX_SIZE=$(grep -i '^dicom.max.size' $POH_PATH/$OH_DIR/rsc/dicom.properties.dist  | cut -f2 -d'=')
#: ${DICOM_MAX_SIZE:=$DICOM_DEFAULT_SIZE}
sed -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/" $POH_PATH/$OH_DIR/rsc/dicom.properties.dist > $POH_PATH/$OH_DIR/rsc/dicom.properties

######## log4j.properties setup
sed -e "s/DBPORT/$MYSQL_PORT/" -e "s/DBSERVER/$MYSQL_SERVER/" -e "s/DBUSER/$DATABASE_USER/" -e "s/DBPASS/$DATABASE_PASSWORD/" \
$POH_PATH/$OH_DIR/rsc/log4j.properties.dist > $POH_PATH/$OH_DIR/rsc/log4j.properties

######## database.properties setup
echo "jdbc.url=jdbc:mysql://$MYSQL_SERVER:$MYSQL_PORT/$DATABASE_NAME" > $POH_PATH/$OH_DIR/rsc/database.properties
echo "jdbc.username=$DATABASE_USER" >> $POH_PATH/$OH_DIR/rsc/database.properties
echo "jdbc.password=$DATABASE_PASSWORD" >> $POH_PATH/$OH_DIR/rsc/database.properties

######## Check for JAVA software

java_check;

######## Database setup

# Start MySQL and create database
if [ $OH_DISTRO = portable ]; then
	if [ -f $POH_PATH/$SQL_DIR/$DB_CREATE_SQL ]; then
		# Check for MySQL software
		mysql_check;
		# Config MySQL
		config_database;
		# Prepare MySQL
		inizialize_database;
		# Start MySQL
		start_database;	
		# Create database and load data
		load_database;
		# Archive sql creation script
		echo "Achiving SQL creation script..."
		mv $POH_PATH/$SQL_DIR/$DB_CREATE_SQL $POH_PATH/$SQL_DIR/$DB_ARCHIVED_SQL
	else
		# Check for MySQL software
		mysql_check;
		# Config MySQL
		config_database;
		# Starting MySQL
		start_database;
	fi
fi

######## CLASSPATH setup

CLASSPATH=$POH_PATH/$OH_DIR/bin/OH-gui.jar
CLASSPATH=$CLASSPATH:$POH_PATH/$OH_DIR/bundle
CLASSPATH=$CLASSPATH:$POH_PATH/$OH_DIR/rpt

DIRLIBS=$POH_PATH/$OH_DIR/lib/*.jar
for i in ${DIRLIBS}
do
	CLASSPATH="$i":$CLASSPATH
done

######## Open Hospital start

echo "Starting Open Hospital... "

cd $POH_PATH/$OH_DIR

$JAVA_BIN -Dsun.java2d.dpiaware=false -Djava.library.path=${NATIVE_LIB_PATH} -classpath $CLASSPATH org.isf.menu.gui.Menu 2>&1 > /dev/null

echo "Exiting Open Hospital..."

if [ $OH_DISTRO = portable ]; then
	shutdown_database;
fi

# go back to starting directory
cd $CURRENT_DIR

# exiting
echo "Done ! "
exit 0
