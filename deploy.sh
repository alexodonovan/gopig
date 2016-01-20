#!/bin/bash
mvn clean install;
scp target/gopig-1.0-SNAPSHOT.one-jar.jar pi@192.168.1.7:~/development/gopig/target;
ssh pi@192.168.1.7 << EOF
	git pull;
	cd ~/development/gopig;
	sh run.sh;
EOF