#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /
#COPY /src /src
#COPY checkstyle-suppressions.xml /
#COPY pom.xml /
#RUN mvn -f /pom.xml clean package
#
#FROM openjdk:17-jdk-slim
#COPY --from=build /target/*.jar application.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/izum-bank-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]



