#!/bin/bash
cd ~/development/gopig;
git pull;
mvn clean install -DskipTests;
cd target;
java -jar gopig-1.0-SNAPSHOT.one-jar.jar;