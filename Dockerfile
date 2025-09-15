# ==========================
# Stage 1: Build the Spring Boot app
# ==========================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (better cache layer)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now copy the source code
COPY src ./src

# Build the project (skip tests to speed up)
RUN mvn -B package -DskipTests

# ==========================
# Stage 2: Run the app
# ==========================
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy only the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
