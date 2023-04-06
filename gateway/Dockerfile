## build
FROM maven:3.9.0-eclipse-temurin-19-alpine as build
WORKDIR /app

COPY .mvn  mvnw mvnw.cmd pom.xml ./
COPY src ./src
RUN mvn package -Dmaven.test.skip

## deploy
FROM eclipse-temurin:19.0.2_7-jre-alpine as deploy

WORKDIR /app

COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
