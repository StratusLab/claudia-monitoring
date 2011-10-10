#!/bin/sh
DEST=$(dirname $0)/../collectdconfig/sonda1/opt/collectd
mkdir -p $DEST/etc
mkdir -p $DEST/share/collectd
scp  root@monitorized1:/opt/collectd/share/collectd/types_ex1.db  $DEST/share/collectd
scp  root@monitorized1:/opt/collectd/etc/collectd.conf $DEST/etc
