#!/bin/sh
. $(dirname $0)/database.conf
mysql <<EOF
drop database $DBNAME;
drop user $USER;
drop user $USER@localhost;
EOF
