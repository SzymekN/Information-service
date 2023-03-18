FROM openjdk:19-ea-19-jdk-slim
WORKDIR /app

COPY target/editorial-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]