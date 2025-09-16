FROM maven:3.9.3-eclipse-temurin-21-alpine AS build
WORKDIR /app

# copy only pom.xml first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# then copy source
COPY src ./src
RUN mvn -B package -DskipTests