# Stage 1: Build the Spring Boot app
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy everything and build the JAR
COPY . .


# Stage 2: Run the Spring Boot app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
