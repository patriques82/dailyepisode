#!/bin/bash

echo "Stopping and removing containers..."
docker-compose down

echo "Removing images..."
docker rmi $(docker images -a -q)

echo "Removing volumes..."
docker volume rm $(docker volume ls -q)
