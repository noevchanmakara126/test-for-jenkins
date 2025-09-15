# ==========================
# Stage 1: Build the Spring Boot app
# ==========================
# Use a Maven image with a JDK to build the application
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file (pom.xml) first.
# This allows Docker to cache this layer, so dependencies are only
# re-downloaded when the pom.xml changes.
COPY pom.xml .

# Download project dependencies to the local Maven repository
RUN mvn -B dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application into a JAR file, skipping tests for speed
RUN mvn -B package -DskipTests

# ==========================
# Stage 2: Run the app
# ==========================
# Use a lightweight JDK image for the final runtime environment
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy only the built JAR file from the 'build' stage
# The 'build' stage is temporary and its files are not included in the final image
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]