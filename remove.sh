#!/bin/bash

echo "Stopping containers..."
docker-compose down

echo "Removing containers..."
docker rm $(docker ps -a -q)

echo "Removing images..."
docker rmi $(docker images -a -q)

echo "Removing volumes..."
docker volume rm $(docker volume ls -q)
