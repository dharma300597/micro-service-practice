FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
#manually installing curl in ubuntu
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=build /app/target/configserver-0.0.1-SNAPSHOT.jar app.jar
RUN curl -L -o /app/opentelemetry-javaagent.jar \
 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.22.0/opentelemetry-javaagent.jar
ENTRYPOINT ["java","-javaagent:/app/opentelemetry-javaagent.jar", "-jar", "app.jar"]