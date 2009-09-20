#!/bin/sh
# --------------------------------------------------------------------------------------
#  $Id$
# --------------------------------------------------------------------------------------

PWD="`pwd`"

if [ "${JAVA_HOME}" = "" ]; then
	echo "env. var. JAVA_HOME not set"
	exit 1
fi

if [ "${JETTY_HOME}" = "" ]; then
	echo "env. var. JETTY_HOME not set"
	exit 2
fi

JETTY_XML="`pwd`/jetty.xml"

echo "using JAVA_HOME ${JAVA_HOME} ..."
echo "using JETTY_HOME ${JETTY_HOME} ..."
echo "using JETTY_XML ${JETTY_XML} ..."

# -Djavassist-write-dir=${PWD}/javassist
JAVA_OPTS="-Dtapestry.production-mode=false -Dbase.path=${PWD} -Djava.compiler=NONE -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

echo $JAVA_OPTS

# --------------------------------------------------------------------
# -- Aufruf fuer Jetty
# --------------------------------------------------------------------

cd ${JETTY_HOME}
${JAVA_HOME}/bin/java ${JAVA_OPTS} -jar start.jar ${JETTY_XML}
cd ${PWD}

