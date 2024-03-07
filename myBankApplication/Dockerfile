## Используем официальный образ OpenJDK 17
#FROM openjdk:17 AS builder
#
## Устанавливаем переменную среды для PostgreSQL
#ENV POSTGRES_USER=postgres
#ENV POSTGRES_DB=postgres
#
## Копируем исходный код проекта в контейнер
#COPY . /app
#WORKDIR /app
#
## Собираем проект с помощью Gradle
## RUN ./gradlew build
#
## Используем официальный образ PostgreSQL
#FROM postgres:latest
#
## Копируем собранные файлы из предыдущего этапа сборки
#COPY --from=builder /app/build/libs/*.jar /docker-entrypoint-initdb.d/
#
#FROM openjdk:21-jdk-slim
#WORKDIR /app
#COPY build/libs/myBankApplication-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]



