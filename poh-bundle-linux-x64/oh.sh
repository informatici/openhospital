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

echo ""
echo "**************************************************é"
echo "* Warning - Do not run this script as root user ! *"
echo "***************************************************"
echo ""

######## Environment check:

######## OPENHOSPITAL Configuration
# POH_PATH is the directory where Portable OpenHospital files are located
# POH_PATH=/usr/local/OpenHospital
POH_PATH=/home/mizzio/Scaricati/OH_dev/poh-linux-x64-0.0.6-core-1.10.0
SCRIPT_NAME="oh.sh"

# set current dir
CURRENT_DIR=$PWD

if [ -z $POH_PATH ]; then
	echo "Warning - POH_PATH not found. Please set it up properly."
		if [ ! -f ./$SCRIPT_NAME ]; then
		echo "Error - oh.sh not found in the current PATH. Please cd the directory where POH was unzipped or set up POH_PATH properly."
		exit 0
		fi
	POH_PATH=$PWD
fi

######## Software configuration - change at your own risk :-)

MYSQL_DIR="mysql-5.7.30-linux-glibc2.12-x86_64"
MYSQL_URL="https://downloads.mysql.com/archives/get/p/23/file"

JAVA_DISTRO="OpenJDK11U-jdk_x64_linux_openj9_11.0.8_10_openj9-0.21.0"
JAVA_URL="https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.8%2B10_openj9-0.21.0"
JAVA_DIR="jdk-11.0.8+10"


OH_DIR="oh"
DATABASE_NAME="oh"
DATABASE_USER="isf"
DATABASE_PASSWORD="isf123"
DICOM_DEFAULT_SIZE="4M"

cd $POH_PATH

######################## DO NOT EDIT BELOW THIS LINE ########################

echo "Checking for software...."

if [ ! -d "$POH_PATH/$MYSQL_DIR" ]; then

		if [ ! -f "$POH_PATH/$MYSQL_DIR.tar.gz" ]; then

		echo "Warning - MySQL  not found. Do you want to download it ? (-630 Mb)"

		read -p "(y/n)?" choice
		case "$choice" in 
			y|Y ) echo "yes";;
			n|N ) echo "Exiting..."; exit 0;;
			* ) echo "Invalid";;
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


if [ ! -d "$POH_PATH/$JAVA_DIR" ]; then

	if [ ! -f "$POH_PATH/$JAVA_DISTRO.tar.gz" ]; then

		echo "Warning - JAVA  not found. Do you want to download it ? (-180 Mb)"

		read -p "(y/n)?" choice
		case "$choice" in 
			y|Y ) echo "yes";;
			n|N ) echo "Exiting..."; exit 0;;
			* ) echo "Invalid";;
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

# Find a free port to run MySQL starting from the default port

POH_PATH_ESCAPED=$(echo $POH_PATH | sed -e 's/\//\\\//g')

######## DICOM setup

DICOM_MAX_SIZE=$(grep -i '^dicom.max.size' $POH_PATH/$OH_DIR/rsc/dicom.properties.ori  | cut -f2 -d'=')
: ${DICOM_MAX_SIZE:=$DICOM_DEFAULT_SIZE}

######## Database setup

MYSQL_PORT=3307

while [ $(ss -tna | awk '{ print $4 }' | grep ":$MYSQL_PORT") ]; do
	MYSQL_PORT=$(expr $MYSQL_PORT + 1)
done

rm -f $POH_PATH/etc/mysql/my.cnf or true

sed -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/g" -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" -e "s/MYSQL_PORT/$MYSQL_PORT/" $POH_PATH/etc/mysql/my.ori > $POH_PATH/etc/mysql/my.cnf
chmod 0444 $POH_PATH/etc/mysql/my.cnf
sed -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" $POH_PATH/$OH_DIR/rsc/dicom.properties.ori > $POH_PATH/$OH_DIR/rsc/dicom.properties
sed -e "s/3306/$MYSQL_PORT/" $POH_PATH/$OH_DIR/rsc/database.properties.sample > $POH_PATH/$OH_DIR/rsc/database.properties
sed -e "s/MYSQL_PORT/$MYSQL_PORT/" $POH_PATH/$OH_DIR/rsc/log4j.properties.ori > $POH_PATH/$OH_DIR/rsc/log4j.properties

if [ -f $POH_PATH/database.sql ]
then
    echo "Initializing MySQL database... on port $MYSQL_PORT"
#    cd $POH_PATH/$MYSQL_DIR/
    rm -rf $POH_PATH/var/lib/mysql
    mkdir -p $POH_PATH/var/lib/mysql
    mkdir -p $POH_PATH/var/log/mysql
    $POH_PATH/$MYSQL_DIR/bin/mysqld --initialize-insecure --basedir=$POH_PATH/$MYSQL_DIR --datadir=$POH_PATH/var/lib/mysql
    if [ $? -ne 0 ]; then
		echo "Error: MySQL initialization failed!"
		exit 1
	fi
	echo "Default schemas initialized..."
	echo "Starting MySQL Server."

	$POH_PATH/$MYSQL_DIR/bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &

	# Wait till the MySQL socket file is created
	while [ ! -e $POH_PATH/var/run/mysqld/mysql.sock ]; do sleep 1; done


	echo "Dropping OH Database (if existing)..."
	$POH_PATH/$MYSQL_DIR/bin/mysql --socket=$POH_PATH/var/run/mysqld/mysql.sock -u root --port=$MYSQL_PORT -e "DROP DATABASE IF EXISTS $DATABASE_NAME;"

	echo "Creating OH Database..."
	$POH_PATH/$MYSQL_DIR/bin/mysql --socket=$POH_PATH/var/run/mysqld/mysql.sock -u root --port=$MYSQL_PORT -e "CREATE DATABASE $DATABASE_NAME; GRANT ALL ON oh.* TO '$DATABASE_USER'@'localhost' IDENTIFIED BY '$DATABASE_PASSWORD'; GRANT ALL ON oh.* TO '$DATABASE_USER'@'%' IDENTIFIED BY '$DATABASE_PASSWORD';"

	echo "Importing schema..."
	$POH_PATH/$MYSQL_DIR/bin/mysql --socket=$POH_PATH/var/run/mysqld/mysql.sock -u root --port=$MYSQL_PORT oh < $POH_PATH/database.sql
    if [ $? -ne 0 ]; then
		echo -n "Error: Database not initialized!"
		exit 2
    fi
    echo "Database initialized !"
#    rm $POH_PATH/database.sql # we don't need to remove the database file
else
	cd $POH_PATH/$MYSQL_DIR/
	echo  "Starting MySQL... "
	$POH_PATH/$MYSQL_DIR/bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &
	if [ $? -ne 0 ]; then
		echo "Error: Database not started!"
		exit 1
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

echo "Starting Open Hospital Portable... "

cd $POH_PATH/$OH_DIR/
$POH_PATH/$JAVA_DIR/bin/java -Dsun.java2d.dpiaware=false -Djava.library.path=${NATIVE_LIB_PATH} -classpath $CLASSPATH org.isf.menu.gui.Menu 2>&1 > /dev/null

echo "Exiting Open Hospital Portable..."
echo "Shutting down MySQL... "

$POH_PATH/$MYSQL_DIR/bin/mysqladmin --host=127.0.0.1 --port=$MYSQL_PORT --user=root shutdown 2>&1 > /dev/null

echo "Removing files... "
rm -f $POH_PATH/etc/mysql/my.cnf
rm -f $POH_PATH/$OH_DIR/rsc/database.properties
rm -f $POH_PATH/$OH_DIR/rsc/log4j.properties
rm -f $POH_PATH/$OH_DIR/rsc/dicom.properties

echo "Done ! "

exit 0
