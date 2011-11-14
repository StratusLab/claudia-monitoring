#!/bin/sh
cd $(dirname $0)
sudo rpm -U collectd-rpm/RPMS/*/*.rpm
sudo rpm -U ../collectd-extendedprobes-rpm/target/rpm/collectd-extendedprobes-rpm/RPMS/noarch/*.rpm
sudo rpm -U ../collectd-collector-rpm/target/rpm/collectd-collector-rpm/RPMS/noarch/*.rpm
