FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ./target/RatingServiceApi-1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
