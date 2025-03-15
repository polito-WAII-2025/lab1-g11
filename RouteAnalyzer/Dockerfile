# Use OpenJDK as a base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Install dependencies: curl and unzip
RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Install Kotlin
RUN curl -sSL https://github.com/JetBrains/kotlin/releases/download/v1.8.20/kotlin-compiler-1.8.20.zip -o kotlin.zip \
    && unzip kotlin.zip -d /opt \
    && rm kotlin.zip \
    && ln -s /opt/kotlin-compiler-1.8.20/bin/kotlinc /usr/local/bin/kotlinc

# Set up the Kotlin environment
ENV PATH="/opt/kotlin-compiler-1.8.20/bin:${PATH}"

# Copy the Gradle wrapper and project files
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Copy the source code
COPY src src/

# Build the application using Gradle ShadowJar
RUN ./gradlew shadowJar --no-daemon

# Find and copy the generated fat JAR
RUN ls build/libs  # Debugging: List files to check if the JAR is generated
RUN mv build/libs/*-all.jar app.jar  # Move the JAR to a predictable location

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
