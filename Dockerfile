# Multi-stage build for Micronaut Native Image with GraalVM

# Stage 1: Build Native Image
FROM ghcr.io/graalvm/native-image-community:21 AS build

# Install required build tools
RUN microdnf install -y findutils

WORKDIR /app

# Copy Gradle wrapper and build files
COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew ./
COPY gradle ./gradle

RUN chmod +x ./gradlew

# Copy source code
COPY src ./src

# Build using Micronaut Gradle plugin
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Runtime (minimal image)
FROM gcr.io/distroless/java21-debian12

WORKDIR /app

# Copy the executable from build
COPY --from=build /app/build/libs/*-all.jar /app/application.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["/app/application.jar"]
