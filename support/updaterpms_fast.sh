#!/bin/sh
cd $(dirname $0)
cd .. ; mvn clean; mvn install -Dmaven.test.skip=true || exit 
cd collectd-extendedprobes-rpm ; mvn clean ;  mvn -Dmaven.test.skip=true install || exit
cd ../collectd-collector-rpm ; mvn clean;  mvn -Dmaven.test.skip=true install 
