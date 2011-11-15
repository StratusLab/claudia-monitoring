yum install mysql-server
yum install memcached
chkconfig --add mysqld
chkconfig --add memcached
chkconfig --level 235 mysqld on
chkconfig  --level 235 memcached on
/etc/init.d/mysqld start
/etc/init.d/memcached start
