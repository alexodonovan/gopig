#!/bin/bash
cd ~/development/gopig;
git pull;
mvn clean install -DskipTests;
java -jar target/gopig-1.0-SNAPSHOT.one-jar.jar;