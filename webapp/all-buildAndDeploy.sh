#!/usr/bin/env bash
export JETTY_HOME="/home/marcin/jetty/jachimowicz/jetty-jachimowicz-fudriver"
#mvn clean package -Pfrontend -Dmaven.test.skip=true
mvn clean package -Pfrontend
$JETTY_HOME/bin/jetty.sh stop
if [ -f $JETTY_HOME/webapps/fudriver.war ]
then
 rm $JETTY_HOME/webapps/fudriver.war
fi
cp target/fudriver.war $JETTY_HOME/webapps/
$JETTY_HOME/bin/jetty.sh start
