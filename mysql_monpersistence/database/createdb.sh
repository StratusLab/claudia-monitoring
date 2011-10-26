#!/bin/sh
. $(dirname $0)/database.conf

# required create user '${USER}@localhost' only if installed anonymous user
# otherwise ${USER} (equivalent to ${USER}@%) is enougth.
mysql <<EOF
create database if not exists $DBNAME;
create user '${USER}' identified by '$PASSWORD';
create user '${USER}'@localhost identified by '$PASSWORD';
grant all on $DBNAME.* to '$USER';
flush privileges
EOF

mysql -u $USER -p${PASSWORD} $DBNAME < $(dirname $0)/create_tables.sql
mysql -u $USER -p${PASSWORD} $DBNAME < $(dirname $0)/inserts_data.sql

#create user '${USER}'@'%' identified by '$PASSWORD';
#create user '${USER}'@'localhost' identified by '$PASSWORD';
