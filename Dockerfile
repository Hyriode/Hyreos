# Build Application Jar
FROM gradle:jdk16 AS build

WORKDIR /usr/app/

# Copy Hyreos project files
COPY . .

# Get username and token used in build.gradle
ARG USERNAME
ARG TOKEN
ENV USERNAME=$USERNAME TOKEN=$TOKEN

RUN gradle shadowJar

# Run Application
FROM openjdk:18.0.1.1-jdk

VOLUME ["/hyreos"]
WORKDIR /hyreos

# Get all environments variables
ENV MEMORY="1G"

# Copy previous builded Jar
COPY --from=build /usr/app/build/libs/Hyreos-all.jar /usr/app/Hyreos.jar
# Copy entrypoint script
COPY --from=build /usr/app/docker-entrypoint.sh /usr/app/docker-entrypoint.sh

# Add permission to file
RUN chmod +x /usr/app/docker-entrypoint.sh

STOPSIGNAL SIGTERM

# Start application
ENTRYPOINT ["sh", "/usr/app/docker-entrypoint.sh"]