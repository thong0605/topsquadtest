#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/task-service-0.0.1-SNAPSHOT.jar task-service.jar

# ENV PORT=8082
EXPOSE 8082
ENTRYPOINT ["java","-jar","task-service.jar"]