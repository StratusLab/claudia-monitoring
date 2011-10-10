#!/bin/sh
scp -r root@monitoring:/opt/collectd/share/collectd/measures.d/* $(dirname $0)/../maps
