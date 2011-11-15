#!/bin/sh
set -e
echo "Building..."
cd $(dirname $0)
./updaterpms_fast.sh
cd $(dirname $0)
echo "Uninstalling..."
./uninstall.sh
echo "Installing..."
./install.sh
