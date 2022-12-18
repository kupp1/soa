FROM payara/server-full:5.2022.3-jdk11

COPY server.crt /
COPY server.p12 /
RUN keytool -import -alias thawtecert -keystore $PAYARA_DIR/glassfish/domains/domain1/config/cacerts.jks -trustcacerts -file /server.crt -storepass changeit -noprompt
RUN keytool -importkeystore -destkeystore $PAYARA_DIR/glassfish/domains/domain1/config/keystore.jks -srckeystore /server.p12 -srcstoretype PKCS12 -alias my_cert_ssl -srcstorepass changeit -deststorepass changeit

COPY target/flat-service-1.0-SNAPSHOT.war /
COPY post-boot-commands.asadmin $POSTBOOT_COMMANDS
