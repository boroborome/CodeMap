#!/usr/bin/env sh

cd `dirname $0`
java -Xdock:icon=icon.png -Xdock:name="Code Map" -jar code-map.jar
#java -Xdock:icon=icon.png -Xdock:name="Code Map" -jar code-map.jar&
cd -
