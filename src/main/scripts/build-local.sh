#!/bin/bash
cd ~/development/raspberry_pi/gopig;
git pull;
mvn clean install
mvn spring-boot:run;
