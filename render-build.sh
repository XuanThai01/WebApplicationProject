#!/usr/bin/env bash

# Clean project & build JAR without running tests
mvn clean package -DskipTests
