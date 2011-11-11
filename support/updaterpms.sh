#!/bin/sh
cd $(dirname $0)/..
mvn clean; mvn install || exit 
cd extended-probes-rpm ; mvn install || exit
cd ../collectd-collector-rpm ; mvn install 
