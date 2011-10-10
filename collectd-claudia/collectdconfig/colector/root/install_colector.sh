#!/bin/sh
export DEBIAN_FRONTEND=noninteractive
apt-get -q -y install mysql-server memcached
apt-get -q -y install bzip2 build-essential libxml2-dev libcurl4-openssl-dev libgcrypt11-dev libdbi0-dev python2.6-dev liboping-dev openjdk-6-jdk
apt-get -q -y install libmemcached-dev libpq-dev libmysqlclient-dev

cd /root/database/
./createdb.sh

# to test publish-subscribe
apt-get -q -y install jetty
/usr/sbin/update-rc.d collectd defaults
/etc/init.d/collectd start
