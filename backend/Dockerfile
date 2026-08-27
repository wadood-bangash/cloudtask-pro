#----------BUILD STAGE--------------------
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY gradle/ gradle/
COPY build.gradle settings.gradle gradlew ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
COPY src/ src/
RUN ./gradlew build -x test --no-daemon
#----------RUNNING STAGE---------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S  appgroup &&  adduser  -S  appgroup -G appgroup
COPY  --from=builder /app/build/libs/*.jar app.jar
RUN chown appuser:appgroup app.jar
USER appuser
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuatorhealth || exit 1
ENTRYPOINT ["java" , "-jar" , "app.jar"]
