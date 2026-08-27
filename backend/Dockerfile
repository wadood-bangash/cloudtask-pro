FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper files
COPY gradle/ gradle/
COPY build.gradle settings.gradle gradlew ./

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (using build instead of dependencies)
RUN ./gradlew build -x test --no-daemon || true

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
