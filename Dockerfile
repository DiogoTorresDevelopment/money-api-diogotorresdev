FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-8-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install -DskipTests

FROM openjdk:8-jdk-slim

EXPOSE 8080

COPY --from=build /target/money-api-1.0.0-SNAPSHOT.jar /money-api.jar

ENTRYPOINT [ "java", "-jar", "/money-api.jar" ]