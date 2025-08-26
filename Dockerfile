FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Устанавливаем Maven
RUN apk add --no-cache maven

# Копируем исходный код
COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем собранный JAR
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Простой скрипт ожидания
ENTRYPOINT ["sh", "-c", "sleep 5 && java -jar app.jar"]