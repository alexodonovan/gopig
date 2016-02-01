#!/bin/bash
cd ../../../;
mvn clean install;
scp target/gopig-0.1.0.jar pi@192.168.1.7:~/development/gopig/target;

# add the below lines to execute the application on the raspberry pi on deploy
# ssh pi@192.168.1.7 << EOF
#	git pull;
#	cd ~/development/gopig;
#	sh run.sh;
# EOF