FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw mvnw
COPY src src

RUN mvn -DskipTests clean package

FROM eclipse-temurin:11-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/upload

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
