# stage 1: build app

FROM ghcr.io/graalvm/native-image-community:21 AS build

RUN microdnf install -y findutils

WORKDIR /home/app

COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew ./
COPY gradle ./gradle
COPY src ./src

RUN ./gradlew nativeCompile

# state 2: run app

FROM gcr.io/distroless/java21-debian12

WORKDIR /home/app

COPY --from=build /home/app/build/native/nativeCompile/application /home/app/main

EXPOSE 8080

ENTRYPOINT ["/home/app/main"]
