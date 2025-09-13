# Working Dockerfile for Windows Docker Desktop
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

# Copy the WAR file directly (this works on Windows Docker Desktop)
COPY dist/nestmartappFinal.war /usr/local/tomcat/webapps/nestmart.war

# Verify the WAR file was copied correctly
RUN ls -la /usr/local/tomcat/webapps/nestmart.war && \
    echo "WAR file size: $(stat -c%s /usr/local/tomcat/webapps/nestmart.war) bytes"

# Copy configuration files (create directory first)
RUN mkdir -p /app
COPY web/WEB-INF/application.properties /app/application.properties
COPY web/WEB-INF/jdbc.properties /app/jdbc.properties

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/nestmart/login.htm || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
