#!/bin/sh
DIRNAME=`dirname $0`
CP=
for jar in `find $DIRNAME -name '*.jar'`; do
  CP=$CP:$jar
done;
$JAVA_HOME/bin/java -DWF_JNI_MODE=false -cp $CP eu.w4.contrib.genactors.Main $*
