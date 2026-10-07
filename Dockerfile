FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S rms && adduser -S rms -G rms

COPY target/rms-backend-*.jar /app/rms-backend.jar

USER rms

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/rms-backend.jar"]
