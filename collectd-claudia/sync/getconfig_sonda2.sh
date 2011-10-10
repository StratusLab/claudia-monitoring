#!/bin/sh
DEST=$(dirname $0)/../collectdconfig/sonda2/opt/collectd
mkdir -p $DEST/etc
mkdir -p $DEST/share/collectd
mkdir -p $DEST/../../root
scp  root@monitorized2:/opt/collectd/etc/collectd.conf $DEST/etc
scp  root@monitorized2:/opt/collectd/share/collectd/*.db  $DEST/share/collectd
scp root@monitorized2:/root/ssh.py $DEST/../../root
scp root@monitorized2:/root/create_bindist.sh $DEST/../../root
scp root@monitorized2:/root/install_agent.sh $DEST/../../root
