# Use official Tomcat image with JDK 8
FROM tomcat:9.0-jdk8-openjdk-slim

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Install curl for health checks and wget for downloading dependencies
RUN apt-get update && apt-get install -y curl wget && rm -rf /var/lib/apt/lists/*

# Download and add JavaMail API and Activation Framework (required for mail functionality)
RUN wget -O /usr/local/tomcat/lib/javax.mail-1.6.2.jar \
    https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar && \
    wget -O /usr/local/tomcat/lib/activation-1.1.1.jar \
    https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar

# Copy the pre-built WAR file
COPY nestmartappFinal.war /usr/local/tomcat/webapps/nestmart.war

# Copy configuration files
COPY web/WEB-INF/application.properties /app/application.properties
COPY web/WEB-INF/jdbc.properties /app/jdbc.properties

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/nestmart/ || exit 1

# Start Tomcat (default CMD from base image)
