# Build:  docker build --no-cache -t donuts-app .
# Verify: docker images

FROM alpine:3.19
WORKDIR /app

RUN apk add --no-cache openjdk17

COPY /build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app/app.jar"]
