# Use a base image with Maven and JDK for building
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a smaller base image with JDK for runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/MovieApplication-0.0.1-SNAPSHOT.jar /app/MovieApplication.jar

# Expose port 8000
EXPOSE 8000

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/MovieApplication.jar"]