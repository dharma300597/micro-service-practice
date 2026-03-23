# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom.xml first for dependency caching
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-jammy



# Create non-root user for security
RUN useradd -ms /bin/bash spring

RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy only the jar
COPY --from=build /app/target/*.jar app.jar
RUN curl -L -o /app/opentelemetry-javaagent.jar \
 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.22.0/opentelemetry-javaagent.jar
# Change ownership
RUN chown spring:spring app.jar

USER spring

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75 \
               -XX:+ExitOnOutOfMemoryError \
               -javaagent:/app/opentelemetry-javaagent.jar"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
