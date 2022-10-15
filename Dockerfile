# Build Application Jar
FROM gradle:jdk16 AS build

WORKDIR /usr/app/

# Copy Hyreos project files
COPY . .

RUN gradle shadowJar

# Run Application
FROM openjdk:18.0.1.1-jdk

# Set working directory
WORKDIR /usr/app/

# Get all environments variables
ENV MEMORY="1G"

# Copy previous builded Jar
COPY --from=build /usr/app/build/libs/Hyreos-all.jar /usr/app/Hyreos.jar

# Start application
ENTRYPOINT java -Xmx${MEMORY} -jar Hyreos.jar
