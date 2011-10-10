#!/bin/sh
export DEBIAN_FRONTEND=noninteractive
apt-get -q -y install bzip2 libxml2 libgcrypt11 libdbi0 python2.6 liboping libpq
#apt-get -q -y openjdk-6-jdk
#apt-get -q -y install libmemcached libpq libmysqlclient

/usr/sbin/update-rc.d collectd defaults
/etc/init.d/collectd start
