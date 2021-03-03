FROM openjdk:11-jdk AS build
RUN mkdir -p /build
COPY . /build
WORKDIR /build
RUN ./gradlew assemble --no-daemon

FROM openjdk:11-jre-slim

EXPOSE 8080

RUN mkdir /app
COPY --from=build /build/build/libs/*.jar /app/application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
