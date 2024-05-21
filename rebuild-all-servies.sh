#!/bin/bash

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
  echo "Maven could not be found. Please install Maven and ensure it is in your PATH."
  exit 1
fi

# Define service directories and names as parallel arrays
services=("chat-service" "game-room-service" "movement-service" "player-service")
service_dirs=("chat-service" "game-room-service" "movement-service" "player-service")

# Loop through each service to build the JAR file and rebuild the Docker image
for i in "${!services[@]}"; do
    SERVICE_NAME=${services[$i]}
    SERVICE_DIR=${service_dirs[$i]}

    echo "Processing $SERVICE_NAME..."

    # Navigate to the service directory
    cd "$SERVICE_DIR" || { echo "Failed to navigate to $SERVICE_DIR"; exit 1; }

    # Build the JAR file
    echo "Building the JAR file for $SERVICE_NAME..."
    mvn clean package || { echo "Failed to build JAR for $SERVICE_NAME"; exit 1; }

    # Navigate back to the project root directory
    cd - || { echo "Failed to navigate back to project root"; exit 1; }
done

# Rebuild Docker images
echo "Rebuilding Docker images for all services..."
docker-compose build || { echo "Failed to build Docker images"; exit 1; }

# Restart the Docker containers
echo "Restarting Docker containers..."
docker-compose up -d || { echo "Failed to restart Docker containers"; exit 1; }

echo "All services have been processed!"
