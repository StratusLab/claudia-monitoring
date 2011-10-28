#!/bin/sh
apt-get install postgresql
echo "create user claudia with password 'ClaudiaPass' ; create database monitoring with owner claudia; " | su postgres -c psql 
cat create_tables.sql inserts_data.sql | env PGPASSWORD=ClaudiaPass  psql -U claudia -h localhost -d monitoring
