#!/bin/sh
cd $(dirname $0)/..
mvn clean; mvn install || exit 
cd extended-probes-rpm ; mvn clean; mvn install || exit
cd ../collectd-collector-rpm ; mvn clean;  mvn install 
