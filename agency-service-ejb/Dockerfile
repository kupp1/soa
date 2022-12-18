FROM tomee:8-jre17

COPY system.properties /usr/local/tomee/conf
COPY tomee.xml /usr/local/tomee/conf

COPY tomcat-users.xml /usr/local/tomee/conf
COPY context.xml /usr/local/tomee/webapps/manager/META-INF/

COPY target/agency-service-ejb-1.0-SNAPSHOT.war /usr/local/tomee/webapps/