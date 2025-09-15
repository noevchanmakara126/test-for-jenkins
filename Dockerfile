FROM openjdk:21 AS build

WORKDIR /app
COPY pom.xml .
COPY src/ src/

RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
