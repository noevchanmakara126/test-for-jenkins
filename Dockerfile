# Stage 1: Build the Spring Boot JAR
FROM maven:3.9.3-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml first
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the jar (downloads dependencies automatically)
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]
