#!/bin/sh
set -e
cd $(dirname $0)
./updaterpms.sh
cd $(dirname $0)
echo "Uninstalling..."
./uninstall.sh
echo "Installing..."
./install.sh
