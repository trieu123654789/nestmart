# Dockerfile optimized for Railway deployment
FROM openjdk:8-jdk-slim AS builder

# Install Ant to build the WAR from source
RUN apt-get update && apt-get install -y ant wget unzip && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy source to build the WAR
COPY . .

# Build using Ant (outputs to dist/)
RUN ant -f build.xml clean && ant -f build.xml

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

# Copy the WAR file built in the builder stage
COPY --from=builder /app/dist/nestmartappFinal.war /usr/local/tomcat/webapps/nestmart.war

# Verify the WAR file was copied correctly
RUN ls -la /usr/local/tomcat/webapps/nestmart.war && \
    echo "WAR file size: $(stat -c%s /usr/local/tomcat/webapps/nestmart.war) bytes"

# Copy configuration files
RUN mkdir -p /app
COPY web/WEB-INF/application.properties /app/application.properties
COPY web/WEB-INF/jdbc.properties /app/jdbc.properties

# Create uploads directories (will be created after WAR extraction)
RUN mkdir -p /tmp/upload-setup

# Set environment variables for Railway
ENV PORT=8080
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

# Expose port (Railway will override this with PORT env var)
EXPOSE 8080


# Create a startup script that handles WAR extraction and upload directories
RUN echo '#!/bin/bash' > /startup.sh && \
    echo 'set -e' >> /startup.sh && \
    echo 'cd /usr/local/tomcat/webapps' >> /startup.sh && \
    echo 'if [ -f "nestmart.war" ] && [ ! -f "nestmart/.extracted" ]; then' >> /startup.sh && \
    echo '  if [ -d "nestmart" ]; then' >> /startup.sh && \
    echo '    find nestmart -type f ! -path "nestmart/assets/admin/images/uploads/*" -delete 2>/dev/null || true' >> /startup.sh && \
    echo '    find nestmart -type d -empty -delete 2>/dev/null || true' >> /startup.sh && \
    echo '  fi' >> /startup.sh && \
    echo '  mkdir -p nestmart && cd nestmart' >> /startup.sh && \
    echo '  jar -xf ../nestmart.war' >> /startup.sh && \
    echo '  touch .extracted' >> /startup.sh && \
    echo '  mkdir -p assets/admin/images/uploads/{products,discount}' >> /startup.sh && \
    echo '  chmod -R 777 assets/admin/images/uploads/' >> /startup.sh && \
    echo '  cd ..' >> /startup.sh && \
    echo 'fi' >> /startup.sh && \
    echo 'exec catalina.sh run' >> /startup.sh && \
    chmod +x /startup.sh

# Use the startup script
CMD ["/startup.sh"]
