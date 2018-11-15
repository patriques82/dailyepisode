FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=web/build/libs/web-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar