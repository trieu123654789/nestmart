# Use OpenJDK 8 base image for compatibility with your Java 8 project
FROM openjdk:8-jdk-alpine

# Set working directory
WORKDIR /app

# Install necessary tools
RUN apk add --no-cache curl wget unzip

# Download and install GlassFish 5.1.0 (compatible with Java EE 7)
ENV GLASSFISH_VERSION=5.1.0
RUN wget -O /tmp/glassfish-${GLASSFISH_VERSION}.zip \
    https://download.eclipse.org/ee4j/glassfish/glassfish-${GLASSFISH_VERSION}.zip && \
    unzip /tmp/glassfish-${GLASSFISH_VERSION}.zip -d /opt && \
    mv /opt/glassfish5 /opt/glassfish && \
    rm /tmp/glassfish-${GLASSFISH_VERSION}.zip

# Set GlassFish environment variables
ENV GLASSFISH_HOME=/opt/glassfish
ENV PATH=$PATH:$GLASSFISH_HOME/bin
ENV AS_ADMIN_PASSWORD=""

# Copy the built WAR file to the container
# Note: You need to build the project first using 'ant dist'
COPY dist/nestmartappFinal.war /app/nestmartappFinal.war

# Copy application properties and configuration files
COPY web/WEB-INF/application.properties /app/application.properties
COPY web/WEB-INF/jdbc.properties /app/jdbc.properties

# Create password file for GlassFish admin
RUN echo "AS_ADMIN_PASSWORD=" > /tmp/glassfishpwd

# Start GlassFish domain, deploy the application, and configure
RUN $GLASSFISH_HOME/bin/asadmin start-domain && \
    $GLASSFISH_HOME/bin/asadmin --user admin --passwordfile /tmp/glassfishpwd deploy \
    --contextroot /nestmart /app/nestmartappFinal.war && \
    $GLASSFISH_HOME/bin/asadmin stop-domain && \
    rm /tmp/glassfishpwd

# Expose GlassFish ports
# 8080: HTTP listener port
# 4848: Admin console port
# 8181: HTTPS listener port (if needed)
EXPOSE 8080 4848 8181

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD curl -f http://localhost:8080/nestmart/ || exit 1

# Start GlassFish domain
CMD ["asadmin", "start-domain", "--verbose"]
