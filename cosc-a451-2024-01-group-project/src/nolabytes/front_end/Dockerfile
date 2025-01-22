# this uses the tomcat image in docker
FROM tomcat

RUN mkdir /usr/local/tomcat/webapps/bytes
ADD build/bytes /usr/local/tomcat/webapps/bytes
ADD build/bytes.war /usr/local/tomcat/webapps


EXPOSE 8080
# this copies our bytes.war zipped file into the webapps directory in docker'
CMD ["catalina.sh", "run"]