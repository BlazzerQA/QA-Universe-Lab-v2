# Этап 1: сборка бэкенда (контекст — корень монорепы)
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B
COPY backend/src ./src
RUN mvn clean package -DskipTests -B

# Этап 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
COPY --from=builder /build/target/qa-universe-lab-backend-*-exec.jar app.jar
COPY knowledge /app/knowledge
EXPOSE 8080
ENV KNOWLEDGE_PATH=/app/knowledge
ENTRYPOINT ["java", "-jar", "app.jar"]
