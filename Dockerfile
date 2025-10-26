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

# Build native image using Micronaut Gradle plugin
RUN ./gradlew nativeCompile

# Stage 2: Runtime (minimal image)
FROM gcr.io/distroless/static-debian12

WORKDIR /app

# Copy the native executable from build
COPY --from=build /app/build/native/nativeCompile/application /app/application

# Expose port
EXPOSE 8080

# Run the native application
ENTRYPOINT ["/app/application"]
