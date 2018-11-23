#!/bin/bash

echo "building jar..."
./gradlew bootJar

echo "running containers..."
docker-compose up --build
