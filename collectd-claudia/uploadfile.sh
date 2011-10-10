#!/bin/sh
#HOST=109.231.78.227
HOST=monitoring
scp ~/.m2/repository/com/telefonica/tcloud/collectorinterfaces/1.0-SNAPSHOT/collectorinterfaces-1.0-SNAPSHOT.jar root@$HOST:/opt/collectd/share/collectd/java/collectorinterfaces.jar
scp ~/.m2/repository/com/telefonica/tcloud/mysql_monpersistence/1.0-SNAPSHOT/mysql_monpersistence-1.0-SNAPSHOT-jar-with-dependencies.jar root@$HOST:/opt/collectd/share/collectd/java/mysql_monpersistence.jar
scp ~/.m2/repository/com/telefonica/tcloud/mongodb_monpersistence/1.0-SNAPSHOT/mongodb_monpersistence-1.0-SNAPSHOT-jar-with-dependencies.jar root@$HOST:/opt/collectd/share/collectd/java/mongodb_monpersistence.jar
scp ~/.m2/repository/com/telefonica/tcloud/silbops_publisher/1.0-SNAPSHOT/silbops_publisher-1.0-SNAPSHOT-jar-with-dependencies.jar root@$HOST:/opt/collectd/share/collectd/java/silbops_publisher.jar
scp  ~/.m2/repository/com/telefonica/tcloud/silbops_subscribe_test/1.0-SNAPSHOT/silbops_subscribe_test-1.0-SNAPSHOT-jar-with-dependencies.jar root@$HOST:test_subscribe.jar
scp ~/.m2/repository/com/telefonica/tcloud/db_collectd2fqn/1.0-SNAPSHOT/db_collectd2fqn-1.0-SNAPSHOT.jar root@$HOST:db_collectd2fqn.jar
scp ~/.m2/repository/org/apache/servicemix/bundles/org.apache.servicemix.bundles.spymemcached/2.5_2/org.apache.servicemix.bundles.spymemcached-2.5_2.jar root@$HOST:/opt/collectd/share/collectd/java/spymemcached-2.5.2.jar
scp ~/.m2/repository/com/telefonica/tcloud/db_collectd2fqn/1.0-SNAPSHOT/db_collectd2fqn-1.0-SNAPSHOT.jar root@$HOST:/opt/collectd/share/collectd/java/db_collectd2fqn.jar
scp ~/.m2/repository/com/telefonica/tcloud/collectd/collectd-claudia/1.0/collectd-claudia-1.0.jar root@$HOST:/opt/collectd/share/collectd/java/
scp ~/.m2/repository/com/telefonica/tcloud/registerfqn4monitoring_ws/1.0-SNAPSHOT/registerfqn4monitoring_ws-1.0-SNAPSHOT.war root@$HOST:/var/lib/jetty/webapps/registerfqn4monitoring_ws.war
