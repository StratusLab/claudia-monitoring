#!/bin/sh
set -e
cd $(dirname $0)
./updaterpms_fast.sh
cd $(dirname $0)
./uninstall.sh
./install.sh
