# Windows-specific Docker fix for WAR file issues
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

# Create a temporary directory and copy all files first
COPY . /tmp/build-context/

# Then move the WAR file from the copied context
RUN if [ -f /tmp/build-context/nestmartappFinal.war ]; then \
        cp /tmp/build-context/nestmartappFinal.war /usr/local/tomcat/webapps/nestmart.war; \
        ls -la /usr/local/tomcat/webapps/nestmart.war; \
    else \
        echo "ERROR: WAR file not found in build context"; \
        ls -la /tmp/build-context/; \
        exit 1; \
    fi

# Copy configuration files
RUN if [ -d /tmp/build-context/web/WEB-INF ]; then \
        mkdir -p /app && \
        cp /tmp/build-context/web/WEB-INF/application.properties /app/application.properties 2>/dev/null || echo "application.properties not found"; \
        cp /tmp/build-context/web/WEB-INF/jdbc.properties /app/jdbc.properties 2>/dev/null || echo "jdbc.properties not found"; \
    fi

# Clean up
RUN rm -rf /tmp/build-context

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/nestmart/login.htm || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
