#!/bin/bash
cd "$(dirname "$0")"

echo "Compiling project..."
javac -cp ".:lib/*:src" src/entity/*.java src/util/*.java src/dao/*.java src/report/*.java src/main/*.java

echo "Running project..."
java -cp ".:lib/*:src" main.Main
