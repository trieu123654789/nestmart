# Multi-stage Dockerfile for robust builds
# Stage 1: Validate and prepare WAR file
FROM alpine:latest AS war-validator
WORKDIR /tmp
# Copy and validate WAR file exists and has correct size
COPY nestmartappFinal.war /tmp/nestmart.war
RUN ls -la /tmp/nestmart.war && \
    [ -f /tmp/nestmart.war ] && \
    [ $(stat -f%z /tmp/nestmart.war 2>/dev/null || stat -c%s /tmp/nestmart.war) -gt 1000000 ] && \
    echo "WAR file validated successfully"

# Stage 2: Production Tomcat image
FROM tomcat:9.0-jdk8-openjdk-slim

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Install required tools
RUN apt-get update && apt-get install -y curl wget && rm -rf /var/lib/apt/lists/*

# Download and add JavaMail API and Activation Framework
RUN wget -O /usr/local/tomcat/lib/javax.mail-1.6.2.jar \
    https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar && \
    wget -O /usr/local/tomcat/lib/activation-1.1.1.jar \
    https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar

# Copy the validated WAR file from stage 1
COPY --from=war-validator /tmp/nestmart.war /usr/local/tomcat/webapps/nestmart.war

# Copy configuration files
COPY web/WEB-INF/application.properties /app/application.properties
COPY web/WEB-INF/jdbc.properties /app/jdbc.properties

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/nestmart/login.htm || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
