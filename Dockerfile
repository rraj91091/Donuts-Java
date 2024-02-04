# Build:  docker build --no-cache -t donuts-app .
# Verify: docker images

FROM alpine:latest
WORKDIR /app

RUN apk add --no-cache openjdk17

CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app/app.jar"]
