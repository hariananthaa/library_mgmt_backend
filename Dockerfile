# Use the official OpenJDK 21 image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file
COPY build/libs/*.jar app.jar

# Set environment variables (you can override these when running the container)
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8081

# Expose the port that the application will run on
EXPOSE 8081

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

