#!/bin/sh
DEST=$(dirname $0)/../collectdconfig/colector/opt/collectd
install -d $DEST/etc/conf.d
install -d $DEST/share/collectd/measures.d
install -d $DEST/../../root
scp  root@monitoring:/opt/collectd/share/collectd/*.db  $DEST/share/collectd
scp -r root@monitoring:/opt/collectd/share/collectd/measures.d/* $DEST/share/collectd/measures.d/
scp  root@monitoring:/opt/collectd/etc/collectd.conf root@monitoring:/opt/collectd/etc/passwd_* root@monitoring:/opt/collectd/etc/hostFilter.conf root@monitoring:/opt/collectd/etc/mapws.properties root@monitoring:/opt/collectd/etc/filter.conf  $DEST/etc
scp  root@monitoring:/opt/collectd/etc/conf.d/* root@monitoring:/opt/collectd/etc/filter.conf  $DEST/etc/conf.d
scp root@monitoring:/root/install_colector.sh $DEST/../../root
scp root@monitoring:/root/ssh.py $DEST/../../root
scp root@monitoring:/root/create_bindist.sh $DEST/../../root
