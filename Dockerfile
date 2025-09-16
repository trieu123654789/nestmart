# Dockerfile optimized for Railway deployment
FROM tomcat:9.0-jdk8-openjdk-slim

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Install curl and wget for health checks and dependencies
RUN apt-get update && apt-get install -y curl wget && rm -rf /var/lib/apt/lists/*

# Download and add JavaMail API and Activation Framework
RUN wget -O /usr/local/tomcat/lib/javax.mail-1.6.2.jar \
    https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar && \
    wget -O /usr/local/tomcat/lib/activation-1.1.1.jar \
    https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar

# Add SQL Server JDBC driver for Azure SQL Database
RUN wget -O /usr/local/tomcat/lib/mssql-jdbc-12.4.2.jre8.jar \
    https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/12.4.2.jre8/mssql-jdbc-12.4.2.jre8.jar

# Copy the WAR file
COPY dist/nestmartappFinal.war /usr/local/tomcat/webapps/ROOT.war

# Verify the WAR file was copied correctly
RUN ls -la /usr/local/tomcat/webapps/nestmart.war && \
    echo "WAR file size: $(stat -c%s /usr/local/tomcat/webapps/nestmart.war) bytes"

# Copy configuration files
RUN mkdir -p /app
COPY web/WEB-INF/application.properties /app/application.properties
COPY web/WEB-INF/jdbc.properties /app/jdbc.properties

# Set environment variables for Railway
ENV PORT=8080
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

# Expose port (Railway will override this with PORT env var)
EXPOSE 8080


# Start Tomcat
CMD ["catalina.sh", "run"]
