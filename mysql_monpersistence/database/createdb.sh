#!/bin/sh
. $(dirname $0)/database.conf


mysql <<EOF
create database $DBNAME;
create user '$USER' identified by '$PASSWORD';
grant all on $DBNAME.* to '$USER';
EOF

mysql -u $USER -p${PASSWORD} $DBNAME < $(dirname $0)/create_tables.sql
mysql -u $USER -p${PASSWORD} $DBNAME < $(dirname $0)/inserts_data.sql

