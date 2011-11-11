#!/bin/sh
cd $(dirname $0)
cd .. ; mvn clean; mvn install || exit 
cd collectd-extendedprobes-rpm ; mvn clean; mvn install || exit
cd ../collectd-collector-rpm ; mvn clean;  mvn install 
