FROM openjdk:17-jdk-slim
LABEL authors="tarikpasalic"

# Set the maintainer label
LABEL maintainer="tarikpasalic"

#RUN apt-get update && \
#    apt-get install -y openjdk-19-jdk && \
#    rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/chat-service-0.0.1-SNAPSHOT.jar /app/chat-service-0.0.1-SNAPSHOT.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the entry point for the container to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/chat-service-0.0.1-SNAPSHOT.jar"]