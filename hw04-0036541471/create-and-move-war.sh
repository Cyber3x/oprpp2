rm -rf ../tools/apache-tomcat-9.0.88/webapps/voting-app*
mvn package
mv target/voting-app.war ../tools/apache-tomcat-9.0.88/webapps
