# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# copy pom first for caching dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# copy source and build
COPY src ./src
RUN mvn -B package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java","-jar","app.jar"]