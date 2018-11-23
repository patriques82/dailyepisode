FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=web/build/libs/web-1.0-SNAPSHOT.jar
COPY ld-musl-x86_64.path /etc/ld-musl-x86_64.path
COPY ${JAR_FILE} app.jar
