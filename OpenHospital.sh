#!/bin/sh

######## Environment check:

# check for java home
JAVA_EXE=$JAVA_HOME/bin/java

if [ -z $JAVA_HOME ]; then
  echo "JAVA_HOME not found. Please set it up properly."
  JAVA_EXE=java
fi

######## OPENHOSPITAL Configuration:

# add the libraries to the OPENHOSPITAL_CLASSPATH.
# EXEDIR is the directory where this executable is.
EXEDIR=${0%/*}

DIRLIBS=${EXEDIR}/bin/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done

DIRLIBS=${EXEDIR}/rsc/*.xml
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done


DIRLIBS=${EXEDIR}/lib/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done

DIRLIBS=${EXEDIR}/lib/h8/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done

DIRLIBS=${EXEDIR}/lib/dicom/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done

DIRLIBS=${EXEDIR}/lib/dicom/dcm4che/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done

DIRLIBS=${EXEDIR}/lib/dicom/jai/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done

DIRLIBS=${EXEDIR}/lib/*.zip
for i in ${DIRLIBS}
do
  if [ -z "$OPENHOSPITAL_CLASSPATH" ] ; then
    OPENHOSPITAL_CLASSPATH=$i
  else
    OPENHOSPITAL_CLASSPATH="$i":$OPENHOSPITAL_CLASSPATH
  fi
done

OPENHOSPITAL_CLASSPATH="${EXEDIR}/../classes":$OPENHOSPITAL_CLASSPATH
OPENHOSPITAL_CLASSPATH="${EXEDIR}/bundle":$OPENHOSPITAL_CLASSPATH
OPENHOSPITAL_CLASSPATH="${EXEDIR}":$OPENHOSPITAL_CLASSPATH
OPENHOSPITAL_HOME="${EXEDIR}"

ARCH=$(uname -m)
case $ARCH in
	x86_64|amd64|AMD64)
		NATIVE_LIB_PATH=${OPENHOSPITAL_HOME}/lib/native/Linux/amd64
		;;
	i[3456789]86|x86|i86pc)
		NATIVE_LIB_PATH=${OPENHOSPITAL_HOME}/lib/native/Linux/i386
		;;
	*)
		echo "Unknown architecture $(uname -m)"
		;;
esac

$JAVA_EXE -Djava.library.path=${NATIVE_LIB_PATH} -classpath "$OPENHOSPITAL_CLASSPATH:$CLASSPATH" org.isf.menu.gui.Menu "$@"
