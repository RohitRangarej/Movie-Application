# Use the slim JDK image for the build stage
FROM openjdk:17.0.1-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from your local machine to the container
COPY target/MovieApplication-0.0.1-SNAPSHOT.jar /app/MovieApplication.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/MovieApplication.jar"]