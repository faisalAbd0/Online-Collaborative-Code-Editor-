FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests


FROM openjdk:21-jdk-slim


RUN apt-get update && apt-get install -y #/
#    apt-transport-https \
#    ca-certificates \
#    curl \
#    gnupg \
#    lsb-release

RUN apt-get install -y docker.io


COPY . .

WORKDIR /app

# RUN mvn clean package
# COPY target/*.jar app.jar
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
