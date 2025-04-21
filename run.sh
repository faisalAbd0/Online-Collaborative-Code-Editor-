#!/bin/bash

echo "Stopping current Docker containers..."
sudo docker compose down

echo "Rebuilding and starting Docker containers..."
sudo docker compose up --build

