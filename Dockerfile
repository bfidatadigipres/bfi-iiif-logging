FROM openjdk:11-jdk AS build

RUN mkdir -p /build
COPY . /build
WORKDIR /build

ARG UV_SHA256SUM="a654377a0b58225b866ca77e7d05fb3fc928eb3dd12c8bdd6e86c5398c660193"
RUN curl -LO https://cdn.jsdelivr.net/npm/universalviewer@3.0.27/dist/uv.zip \
    && echo "$UV_SHA256SUM uv.zip" | sha256sum -c - \
    && unzip uv.zip -d src/main/resources/static/uv

RUN ./gradlew assemble --no-daemon

FROM openjdk:11-jre-slim
LABEL org.opencontainers.image.source=https://github.com/bfidatadigipres/bfi-iiif-logging

EXPOSE 8080

RUN mkdir /app
COPY --from=build /build/build/libs/*.jar /app/application.jar
COPY docker/usr/local/bin/entrypoint.sh /usr/local/bin

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["java", "-jar", "/app/application.jar"]
