#!/bin/sh
cd $(dirname $0)/..
mvn clean; mvn install -Dmaven.test.skip=true || exit 
cd extended-probes-rpm ; mvn -Dmaven.test.skip=true install || exit
cd ../collectd-collector-rpm ; mvn -Dmaven.test.skip=true install 
