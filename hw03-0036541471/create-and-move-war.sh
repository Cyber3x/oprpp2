rm -rf ../tools/apache-tomcat-9.0.88/webapps/webapp2*
mvn package
mv target/webapp2.war ../tools/apache-tomcat-9.0.88/webapps
