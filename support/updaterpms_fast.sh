#!/bin/sh
cd $(dirname $0)/..
mvn clean; mvn install -Dmaven.test.skip=true || exit 
cd extended-probes-rpm ; mvn clean ;  mvn -Dmaven.test.skip=true install || exit
cd ../collectd-collector-rpm ; mvn clean;  mvn -Dmaven.test.skip=true install 
