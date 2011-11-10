#!/bin/sh
sudo rpm -e collectd-java collectd-basicprobes collectd-base collectd-collector-rpm collectd-extendedprobes-rpm
sudo rm -rf /opt/collectd /opt/monitoring
