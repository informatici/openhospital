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

######## Software configuration - change at your own risk :-)

MYSQL_DIR="mysql-5.7.30-linux-glibc2.12-i686"
MYSQL_URL="https://dev.mysql.com/get/Downloads/MySQL-5.7/"

### JRE 8 - openlogic
#JAVA_DISTRO="openlogic-openjdk-8u262-b10-linux-x32"
#JAVA_URL="https://builds.openlogic.com/downloadJDK/openlogic-openjdk/8u262-b10/"
#JAVA_DIR="openlogic-openjdk-8u262-b10-linux-32"

### JRE 8 - zulu
#JAVA_DISTRO="zulu8.50.0.21-ca-jre8.0.272-linux_i686"
#JAVA_URL="https://cdn.azul.com/zulu/bin/"
#JAVA_DIR="zulu8.50.0.21-ca-jre8.0.272-linux_i686"

### JRE 11
JAVA_DISTRO="zulu11.43.21-ca-jre11.0.9-linux_i686"
JAVA_URL="https://cdn.azul.com/zulu/bin/"
JAVA_DIR="zulu11.43.21-ca-jre11.0.9-linux_i686"

MYSQL_PORT=3307
MYSQL_SOCKET="var/run/mysqld/mysql.sock"
DB_CREATE_SQL="database.sql"
DB_ARCHIVED_SQL="database.sql.imported"

OH_DIR="oh"
DATABASE_NAME="oh"
DATABASE_USER="isf"
DATABASE_PASSWORD="isf123"
DICOM_DEFAULT_SIZE="4M"

######################## DO NOT EDIT BELOW THIS LINE ########################
# name of this shell script
SCRIPT_NAME=$(basename "$0")

# set current dir
CURRENT_DIR=$PWD

# check user
if [ $(id -u) -eq 0 ]
  then echo "Error - do not run this script as root."
  exit 1
fi

# set POH_PATH if not defined
if [ -z $POH_PATH ]; then
	echo "Warning - POH_PATH not found. Please set it up properly."
		if [ ! -f ./$SCRIPT_NAME ]; then
		echo "Error - oh.sh not found in the current PATH. Please cd the directory where POH was unzipped or set up POH_PATH properly."
		exit 0
		fi
	POH_PATH=$PWD
fi

cd $POH_PATH

######## User input / option parsing

function script_usage {
	echo "Usage: $(basename $0) [-h -r]" 2>&1
        echo "   -h       shows this short help"
        echo "   -r       reset Portable Open Hospital installation"
	exit 0
}


function restore_db {
	if [ -f $POH_PATH/$DB_CREATE_SQL ];
	then
	        echo "Found SQL creation script, starting OH...."

	elif [ -f $POH_PATH/$DB_ARCHIVED_SQL ];
	then
	        echo "Found archived SQL creation script, restoring and starting OH...."
		mv $POH_PATH/$DB_ARCHIVED_SQL $POH_PATH/$DB_CREATE_SQL
	fi
}

function start_database {
	cd $POH_PATH/$MYSQL_DIR/
	echo "Starting MySQL... "
	$POH_PATH/$MYSQL_DIR/bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &
	if [ $? -ne 0 ]; then
		echo "Error: Database not started!"
		exit 2
	fi
}


# list of arguments expected in the input
optstring=":hr"

# function to parse input
while getopts ${optstring} arg; do
	case ${arg} in
	"h")
		script_usage
		;;
	"r")
        	echo "Resetting Portable Open Hospital installation...."
		restore_db
		;;
	?)
		echo "Invalid option: -${OPTARG}."
		exit 2
		;;
	esac
done


######## Script start

echo "Checking for software...."

# MySQL
if [ ! -d "$POH_PATH/$MYSQL_DIR" ]; then

	if [ ! -f "$POH_PATH/$MYSQL_DIR.tar.gz" ]; then

		echo "Warning - MySQL  not found. Do you want to download it ? (600 MB)"

		read -p "(y/n)?" choice
		case "$choice" in 
			y|Y ) echo "yes";;
			n|N ) echo "Exiting..."; exit 1;;
			* ) echo "Invalid choice";;
		esac

		# Downloading mysql binary
		echo "Downloading $MYSQL_DIR..."
		wget $MYSQL_URL/$MYSQL_DIR.tar.gz
		echo "Unpacking $MYSQL_DIR..."
		tar zxvf $MYSQL_DIR.tar.gz
		echo "Done !"
	fi
	echo "Using $MYSQL_DIR"
fi

# Java
if [ ! -d "$POH_PATH/$JAVA_DIR" ]; then

	if [ ! -f "$POH_PATH/$JAVA_DISTRO.tar.gz" ]; then

		echo "Warning - JAVA  not found. Do you want to download it ? (180 MB)"

		read -p "(y/n)?" choice
		case "$choice" in 
			y|Y ) echo "yes";;
			n|N ) echo "Exiting..."; exit 1;;
			* ) echo "Invalid choice";;
		esac

		# Downloading openjdk binaries
		echo "Downloading $JAVA_DISTRO..."
		wget $JAVA_URL/$JAVA_DISTRO.tar.gz
		echo "Unpacking $JAVA_DISTRO..."
		tar zxvf $JAVA_DISTRO.tar.gz
		echo "Done !"
	fi
fi

######## Environment setup

echo "Setting up environment...."

POH_PATH_ESCAPED=$(echo $POH_PATH | sed -e 's/\//\\\//g')

######## DICOM setup

DICOM_MAX_SIZE=$(grep -i '^dicom.max.size' $POH_PATH/$OH_DIR/rsc/dicom.properties.dist  | cut -f2 -d'=')
: ${DICOM_MAX_SIZE:=$DICOM_DEFAULT_SIZE}

######## Database setup

# Find a free TCP port to run MySQL starting from the default port

echo "Looking for a free TCP port for MySQL database..."
while [ $(ss -tna | awk '{ print $4 }' | grep ":$MYSQL_PORT") ]; do
	MYSQL_PORT=$(expr $MYSQL_PORT + 1)
done
echo "Found TCP port $MYSQL_PORT!"

# Creating MySQL configuration

rm -f $POH_PATH/etc/mysql/my.cnf or true
sed -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/g" -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" -e "s/MYSQL_PORT/$MYSQL_PORT/" -e "s/MYSQL_DISTRO/$MYSQL_DIR/g" $POH_PATH/etc/mysql/my.cnf.dist > $POH_PATH/etc/mysql/my.cnf

chmod 0444 $POH_PATH/etc/mysql/my.cnf

sed -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" $POH_PATH/$OH_DIR/rsc/dicom.properties.dist > $POH_PATH/$OH_DIR/rsc/dicom.properties
sed -e "s/3306/$MYSQL_PORT/" $POH_PATH/$OH_DIR/rsc/database.properties.sample > $POH_PATH/$OH_DIR/rsc/database.properties
sed -e "s/MYSQL_PORT/$MYSQL_PORT/" $POH_PATH/$OH_DIR/rsc/log4j.properties.dist > $POH_PATH/$OH_DIR/rsc/log4j.properties

if [ -f $POH_PATH/$DB_CREATE_SQL ]; then
	echo "Initializing MySQL database on port $MYSQL_PORT..."

	# Recreate directory structure
	rm -rf $POH_PATH/var/lib/mysql
	mkdir -p $POH_PATH/var/lib/mysql
	mkdir -p $POH_PATH/var/log/mysql

	# Inizialize MySQL
	$POH_PATH/$MYSQL_DIR/bin/mysqld --initialize-insecure --basedir=$POH_PATH/$MYSQL_DIR --datadir=$POH_PATH/var/lib/mysql
    
	if [ $? -ne 0 ]; then
		echo "Error: MySQL initialization failed!"
		exit 2
	fi

	# Starting MySQL
	start_database;	

	# Wait till the MySQL socket file is created
	while [ ! -e $POH_PATH/$MYSQL_SOCKET ]; do sleep 1; done

	echo "Dropping OH Database (if existing)..."
	$POH_PATH/$MYSQL_DIR/bin/mysql --socket=$POH_PATH/$MYSQL_SOCKET -u root --port=$MYSQL_PORT -e "DROP DATABASE IF EXISTS $DATABASE_NAME;"

	echo "Creating OH Database..."
	$POH_PATH/$MYSQL_DIR/bin/mysql --socket=$POH_PATH/$MYSQL_SOCKET -u root --port=$MYSQL_PORT -e "CREATE DATABASE $DATABASE_NAME; GRANT ALL ON $DATABASE_NAME.* TO '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; GRANT ALL ON $DATABASE_NAME.* TO '$DATABASE_USER'@'%' IDENTIFIED BY '$DATABASE_PASSWORD';"

	echo "Importing database schema..."
	$POH_PATH/$MYSQL_DIR/bin/mysql --socket=$POH_PATH/$MYSQL_SOCKET -u root --port=$MYSQL_PORT $DATABASE_NAME < $POH_PATH/$DB_CREATE_SQL
	if [ $? -ne 0 ]; then
		echo -n "Error: Database not initialized!"
		exit 2
	fi
	echo "Database initialized !"

	# archive sql creation script
	echo "Achiving SQL creation script..."
	mv $POH_PATH/$DB_CREATE_SQL $POH_PATH/$DB_ARCHIVED_SQL
else
	# Starting MySQL
	start_database;
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

ARCH=$(uname -m)
case $ARCH in
	x86_64|amd64|AMD64)
		NATIVE_LIB_PATH=$POH_PATH/$OH_DIR/lib/native/Linux/amd64
		;;
	i[3456789]86|x86|i86pc)
		NATIVE_LIB_PATH=$POH_PATH/$OH_DIR/lib/native/Linux/i386
		;;
	*)
		echo "Unknown architecture $(uname -m)"
		;;
esac

######## Portable Open Hospital start

echo "Starting Portable Open Hospital... "

cd $POH_PATH/$OH_DIR/
$POH_PATH/$JAVA_DIR/bin/java -Dsun.java2d.dpiaware=false -Djava.library.path=${NATIVE_LIB_PATH} -classpath $CLASSPATH org.isf.menu.gui.Menu 2>&1 > /dev/null

echo "Exiting Portable Open Hospital..."

echo "Shutting down MySQL... "

$POH_PATH/$MYSQL_DIR/bin/mysqladmin --host=127.0.0.1 --port=$MYSQL_PORT --user=root shutdown 2>&1 > /dev/null

echo "Removing files... "
rm -f $POH_PATH/etc/mysql/my.cnf
rm -f $POH_PATH/$OH_DIR/rsc/database.properties
rm -f $POH_PATH/$OH_DIR/rsc/log4j.properties
rm -f $POH_PATH/$OH_DIR/rsc/dicom.properties

echo "Done ! "

# go back to starting directory
cd $CURRENT_DIR
exit 0
