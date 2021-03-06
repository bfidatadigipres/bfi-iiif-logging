FROM openjdk:11-jdk AS build

RUN mkdir -p /build
COPY . /build
WORKDIR /build
RUN ./gradlew unzipUvBundle assemble --no-daemon -x test

FROM openjdk:11-jre-slim
LABEL org.opencontainers.image.source=https://github.com/bfidatadigipres/bfi-iiif-logging

EXPOSE 8080

RUN mkdir /app
COPY --from=build /build/build/libs/*.jar /app/application.jar
COPY docker/usr/local/bin/entrypoint.sh /usr/local/bin

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["java", "-jar", "/app/application.jar"]
