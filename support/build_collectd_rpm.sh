#!/bin/sh
cd $(dirname $0)
test  ! -n "$JAVA_HOME" && echo 'JAVA_HOME must be defined' && exit
test  ! $(id -u) -eq 0 && echo 'Please, invoke this script as root user' && exit
yum install python-devel perl-ExtUtils-MakeMaker rpm-build
cd collectd-rpm
cd SOURCES ; wget http://www.collectd.org/files/collectd-5.0.1.tar.gz
cd ../SPECS
rpmbuild -ba --define "_topdir $(pwd)/.." collectd.spec 



