#Multi-stage Docker Build for CI/CD pipelines

#Build stage
FROM alpine:3.19 AS BUILD
WORKDIR /app
RUN apk add --no-cache openjdk17
COPY . .
RUN ./gradlew bootJar

# Package stage
FROM alpine:3.19
ENV JAR_NAME=app.jar
ENV APP_HOME=/app
WORKDIR $APP_HOME
RUN apk add --no-cache openjdk17
COPY --from=BUILD $APP_HOME/build/libs/*.jar $JAR_NAME
EXPOSE 8080
ENTRYPOINT java -jar $JAR_NAME
