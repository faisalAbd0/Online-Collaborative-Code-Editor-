#FROM maven:3.9.4-eclipse-temurin-21 AS builder
#
#WORKDIR /app
#
#COPY . .
#
#RUN mvn clean install -DskipTests
#
#FROM openjdk:21-jdk-slim
#
#WORKDIR /app
#
#
##COPY target/*.jar app.jar
#
#COPY --from=builder /app/target/*.jar app.jar
#
#EXPOSE 8082
#
#ENTRYPOINT ["java", "-jar", "app.jar"]
#
#
##
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
