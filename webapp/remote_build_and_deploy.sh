export JETTY_HOME="/usr/local/jetty"
mvn clean package
#ssh root@192.168.1.9 '/usr/local/jetty/bin/jetty.sh stop'
scp target/fudriver.war root@192.168.1.9:/usr/local/jetty/webapps/
#ssh root@192.168.1.9 '/usr/local/jetty/bin/jetty.sh start'

