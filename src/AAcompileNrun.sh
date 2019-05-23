#!/usr/bin/env bash
javac -cp Mobile.jar:. *.java
jar cvf Mobile.jar *
printf "\n\n"
java -cp Mobile.jar Inject localhost 56777 MyAgent localhost localhost localhost