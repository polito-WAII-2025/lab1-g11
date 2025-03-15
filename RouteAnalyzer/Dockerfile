# Use official Gradle image with OpenJDK
FROM gradle:8.5-jdk21 AS build

# Set the working directory
WORKDIR /app

# Copy Gradle project files
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Copy source code
COPY src src/

# Build the application using Gradle ShadowJar
RUN ./gradlew shadowJar --no-daemon

# Use a smaller runtime image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/build/libs/*-all.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]