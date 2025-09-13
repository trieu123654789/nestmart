# Use Eclipse Temurin 8 with Alpine (more recent Alpine version)
FROM eclipse-temurin:8-jdk-alpine

# Install necessary tools: ant + wget + unzip
RUN apk add --no-cache apache-ant curl wget unzip

# Install GlassFish first (needed for build classpath)
ENV GLASSFISH_VERSION=6.2.5
RUN wget -O /tmp/glassfish-${GLASSFISH_VERSION}.zip \
    https://download.eclipse.org/ee4j/glassfish/glassfish-${GLASSFISH_VERSION}.zip && \
    unzip /tmp/glassfish-${GLASSFISH_VERSION}.zip -d /opt && \
    mv /opt/glassfish5 /opt/glassfish && \
    rm /tmp/glassfish-${GLASSFISH_VERSION}.zip

ENV GLASSFISH_HOME=/opt/glassfish
ENV PATH=$PATH:$GLASSFISH_HOME/bin
ENV AS_ADMIN_PASSWORD=""

# Set working directory
WORKDIR /app

# Copy source code into container
COPY . .

# Build WAR using Ant (with GlassFish server home set)
RUN ant -Dj2ee.server.home=$GLASSFISH_HOME dist

# Copy properties/configs (nếu cần)
COPY web/WEB-INF/application.properties /app/application.properties
COPY web/WEB-INF/jdbc.properties /app/jdbc.properties

# Create password file
RUN echo "AS_ADMIN_PASSWORD=" > /tmp/glassfishpwd

# Deploy WAR to GlassFish
RUN $GLASSFISH_HOME/bin/asadmin start-domain && \
    $GLASSFISH_HOME/bin/asadmin --user admin --passwordfile /tmp/glassfishpwd deploy \
    --contextroot /nestmart /app/dist/nestmartappFinal.war && \
    $GLASSFISH_HOME/bin/asadmin stop-domain && \
    rm /tmp/glassfishpwd

# Expose ports
EXPOSE 8080 4848 8181

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD curl -f http://localhost:8080/nestmart/ || exit 1

# Start GlassFish
CMD ["asadmin", "start-domain", "--verbose"]
