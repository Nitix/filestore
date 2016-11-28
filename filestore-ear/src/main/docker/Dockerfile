FROM jboss/wildfly:10.1.0.Final

ADD filestore-ear.ear  /opt/jboss/wildfly/standalone/deployments/filestore.ear

ADD standalone-full.xml /opt/jboss/wildfly/standalone/configuration/filestore.xml

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "filestore.xml", "-b", "0.0.0.0"]
