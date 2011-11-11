#!/bin/sh
set -e
cd $(dirname $0)
./updaterpms.sh
cd $(dirname $0)
./uninstall.sh
./install.sh
