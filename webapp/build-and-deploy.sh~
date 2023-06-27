#!/usr/bin/env bash
export JETTY_HOME="/home/marcin/jetty/rymuza/jetty-rymuza-fudriver"
mvn clean package -Dmaven.test.skip=true
$JETTY_HOME/bin/jetty.sh stop
if [ -f $JETTY_HOME/webapps/fudriver.war ]
then
 rm $JETTY_HOME/webapps/fudriver.war
fi
cp target/fudriver.war $JETTY_HOME/webapps/
$JETTY_HOME/bin/jetty.sh start
