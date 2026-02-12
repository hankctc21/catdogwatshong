FROM maven:3.8.8-openjdk-11 AS build
WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw mvnw
COPY src src

RUN chmod +x mvnw && ./mvnw -DskipTests clean package

FROM eclipse-temurin:11-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/upload

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
