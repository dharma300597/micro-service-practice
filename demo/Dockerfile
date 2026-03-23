FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pre-built JAR (this is now INSIDE order-service folder)
COPY lib/common-utils-0.0.1-SNAPSHOT.jar lib/

# Install it to local Maven repository in container
RUN mvn install:install-file \
    -Dfile=lib/common-utils-0.0.1-SNAPSHOT.jar \
    -DgroupId=com.example \
    -DartifactId=common-utils \
    -Dversion=0.0.1-SNAPSHOT \
    -Dpackaging=jar

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

RUN curl -L -o /app/opentelemetry-javaagent.jar \
 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.22.0/opentelemetry-javaagent.jar

ENTRYPOINT ["java","-javaagent:/app/opentelemetry-javaagent.jar", "-jar", "app.jar"]
