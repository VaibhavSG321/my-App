# Use an official Java runtime as a parent image
FROM openjdk:23-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/Authentication-0.0.1-SNAPSHOT.jar Authentication-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "Authentication-0.0.1-SNAPSHOT.jar"]
