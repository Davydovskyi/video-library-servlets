FROM tomcat:10.1.23 AS video-library-tomcat

ADD target/video-library-servlets-*.war /usr/local/tomcat/webapps/ROOT.war

WORKDIR /app

EXPOSE 8080
CMD ["catalina.sh", "run"]