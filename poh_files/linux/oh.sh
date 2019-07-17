#!/bin/bash

MYSQL_DIR="mysql-5.0.51a-linux-i686"
JAVA_DIR="jre1.6.0_45"
OH_DIR="oh-1.8.4"

where_i_am=$(pwd)
cd $(dirname $0)
POH_PATH=$(pwd)
cd $where_i_am

# Find a free port to run MySQL starting from the default port.
mysql_port=3306
while [ $(netstat -tna | grep -e '^tcp' | awk '{ print $4 }' | grep ":$mysql_port") ]; do
	mysql_port=$(expr $mysql_port + 1)
done

POH_PATH_ESCAPED=$(echo $POH_PATH | sed -e 's/\//\\\//g')
sed -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" -e "s/MYSQL_PORT/$mysql_port/" $POH_PATH/etc/mysql/my.ori > $POH_PATH/etc/mysql/my.cnf
sed -e "s/MYSQL_PORT/$mysql_port/" $POH_PATH/$OH_DIR/rsc/database.properties.ori > $POH_PATH/$OH_DIR/rsc/database.properties
sed -e "s/MYSQL_PORT/$mysql_port/" $POH_PATH/$OH_DIR/rsc/log4j.properties.ori > $POH_PATH/$OH_DIR/rsc/log4j.properties
sed -e "s/MYSQL_PORT/$mysql_port/" $POH_PATH/$OH_DIR/rsc/h8.properties.ori > $POH_PATH/$OH_DIR/rsc/h8.properties
sed -e "s/OH_PATH_SUBSTITUTE/$POH_PATH_ESCAPED/g" $POH_PATH/$OH_DIR/rsc/dicom.properties.ori > $POH_PATH/$OH_DIR/rsc/dicom.properties

echo "Starting MySQL... "
cd $POH_PATH/$MYSQL_DIR/
./bin/mysqld_safe --defaults-file=$POH_PATH/etc/mysql/my.cnf 2>&1 > /dev/null &

echo "Starting Open Hospital... "

CLASSPATH=$POH_PATH/$OH_DIR/bin/OH.jar
CLASSPATH=$CLASSPATH:$POH_PATH/$OH_DIR/bundle
CLASSPATH=$CLASSPATH:$POH_PATH/$OH_DIR/rpt

DIRLIBS=$POH_PATH/$OH_DIR/lib/*.jar
for i in ${DIRLIBS}
do
	CLASSPATH="$i":$CLASSPATH
done
DIRLIBS=$POH_PATH/$OH_DIR/lib/dicom/*.jar
for i in ${DIRLIBS}
do
	CLASSPATH="$i":$CLASSPATH
done
DIRLIBS=$POH_PATH/$OH_DIR/lib/dicom/dcm4che/*.jar
for i in ${DIRLIBS}
do
	CLASSPATH="$i":$CLASSPATH
done
DIRLIBS=$POH_PATH/$OH_DIR/lib/dicom/jai/*.jar
for i in ${DIRLIBS}
do
	CLASSPATH="$i":$CLASSPATH
done
DIRLIBS=$POH_PATH/$OH_DIR/lib/h8/*.jar
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
rm -f $POH_PATH/$OH_DIR/rsc/h8.properties
rm -f $POH_PATH/$OH_DIR/rsc/dicom.properties

exit 0
