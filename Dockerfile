FROM openjdk:11-jdk AS build

RUN mkdir -p /build
COPY . /build
WORKDIR /build

ARG UV_SHA256SUM="3972aadadaeaaf98a01c9a792ce98b848998f27aa6dec592cbe2066aa66e7c0e"
RUN curl -LO https://cdn.jsdelivr.net/npm/universalviewer@3.1.1/dist/uv.zip \
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
