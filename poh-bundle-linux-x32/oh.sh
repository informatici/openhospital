#!/bin/bash

MYSQL_DIR="mysql-5.7.30-linux-glibc2.12-i686"
JAVA_DIR="jdk8u252-b09-jre"
OH_DIR="oh"
DICOM_DEFAULT_SIZE="4M"

where_i_am=$(pwd)
cd $(dirname $0)
POH_PATH=$(pwd)
cd $where_i_am

# Find a free port to run MySQL starting from the default port.
mysql_port=3306
while [ $(ss -tna | awk '{ print $4 }' | grep ":$mysql_port") ]; do
	mysql_port=$(expr $mysql_port + 1)
done

POH_PATH_ESCAPED=$(echo $POH_PATH | sed -e 's/\//\\\//g')
DICOM_MAX_SIZE=$(grep -i '^dicom.max.size' $POH_PATH/$OH_DIR/rsc/dicom.properties.ori  | cut -f2 -d'=')
: ${DICOM_MAX_SIZE:=$DICOM_DEFAULT_SIZE}

rm -f $POH_PATH/etc/mysql/my.cnf or true
sed -e "s/DICOM_SIZE/$DICOM_MAX_SIZE/g" -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" -e "s/MYSQL_PORT/$mysql_port/" $POH_PATH/etc/mysql/my.ori > $POH_PATH/etc/mysql/my.cnf
chmod 0444 $POH_PATH/etc/mysql/my.cnf
sed -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" $POH_PATH/$OH_DIR/rsc/dicom.properties.ori > $POH_PATH/$OH_DIR/rsc/dicom.properties
sed -e "s/3306/$mysql_port/" $POH_PATH/$OH_DIR/rsc/database.properties.sample > $POH_PATH/$OH_DIR/rsc/database.properties
sed -e "s/MYSQL_PORT/$mysql_port/" $POH_PATH/$OH_DIR/rsc/log4j.properties.ori > $POH_PATH/$OH_DIR/rsc/log4j.properties

if [ -f $POH_PATH/database.sql ]
then
    echo "Initializing database... on port $mysql_port"
    cd $POH_PATH/$MYSQL_DIR/
    rm -rf ../var/lib/mysql
    mkdir -p ../var/lib/mysql
    mkdir -p ../var/log/mysql
    ./bin/mysqld --initialize-insecure --basedir=./ --datadir=../var/lib/mysql
    if [ $? -ne 0 ]; then
		echo "Error: Initialization failed!"
		exit 1
	fi
	echo "Default schemas initialized..."
	echo -n "Starting MySQL Server."
	./bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &
    # Wait till the MySQL socket file is created
    while [ ! -e $POH_PATH/var/run/mysqld/mysql.sock ]; do sleep 1; done
    ./bin/mysql --socket=$POH_PATH/var/run/mysqld/mysql.sock -u root --port=$mysql_port -e "CREATE SCHEMA oh; GRANT ALL ON oh.* TO 'isf'@'localhost' IDENTIFIED BY 'isf123'; GRANT ALL ON oh.* TO 'isf'@'%' IDENTIFIED BY 'isf123';"
    ./bin/mysql --socket=$POH_PATH/var/run/mysqld/mysql.sock -u root --port=$mysql_port oh < $POH_PATH/database.sql
    if [ $? -ne 0 ]; then
		echo "Error: Database not initialized!"
		exit 2
    fi
    echo "Database initialized."
    rm $POH_PATH/database.sql
else
	cd $POH_PATH/$MYSQL_DIR/
	echo "Starting MySQL... "
	./bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &
	if [ $? -ne 0 ]; then
		echo "Error: Database not started!"
		exit 1
	fi
fi

echo "Starting Open Hospital... "

CLASSPATH=$POH_PATH/$OH_DIR/bin/OH-gui.jar
CLASSPATH=$CLASSPATH:$POH_PATH/$OH_DIR/bundle
CLASSPATH=$CLASSPATH:$POH_PATH/$OH_DIR/rpt

DIRLIBS=$POH_PATH/$OH_DIR/lib/*.jar
for i in ${DIRLIBS}
do
	CLASSPATH="$i":$CLASSPATH
done

echo $CLASSPATH

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

cd $POH_PATH/$OH_DIR/
$POH_PATH/$JAVA_DIR/bin/java -Dsun.java2d.dpiaware=false -Djava.library.path=${NATIVE_LIB_PATH} -classpath $CLASSPATH org.isf.menu.gui.Menu 2>&1 > /dev/null

echo "Shutting down MySQL... "
cd $POH_PATH/$MYSQL_DIR/
./bin/mysqladmin --host=127.0.0.1 --port=$mysql_port --user=root shutdown 2>&1 > /dev/null
rm -f $POH_PATH/etc/mysql/my.cnf
rm -f $POH_PATH/$OH_DIR/rsc/database.properties
rm -f $POH_PATH/$OH_DIR/rsc/log4j.properties
rm -f $POH_PATH/$OH_DIR/rsc/dicom.properties

exit 0
