/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author jomar
 */
public class IntegrationTest extends TestCase {
    Collector collector=null;
    // only enable this test if you have a local installation. Disable with
    // continous integration.
    boolean disabled=true;
    public IntegrationTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        collector=new Collector();
        
        Properties properties=new Properties();
        
        properties.setProperty("modules.path","/opt/collectd/share/collectd/java/");
        properties.setProperty("measuresfilter.path", "/opt/collectd/etc/filter.conf");
        properties.setProperty("hostfilter.path", "/opt/collectd/etc/hostFilter.conf");
        //properties.setProperty("publisher.class","");
        properties.setProperty("persistence.class","com.telefonica.tcloud.mysql_monpersistence.MySQL_MonPersistenceFactory");
        properties.setProperty("persistence.jars","mysql_monpersistence.jar");
        properties.setProperty("conversor2fqn.class","com.telefonica.tcloud.db_collectd2fqn.Collectd2FQNMapDBFactory");
        properties.setProperty("conversor2fqn.jars",
                "db_collectd2fqn.jar,spymemcached-2.5_2.jar,jetty-server.jar"+
                ",jetty-webapp.jar,servlet-api-2.5.jar");
        properties.setProperty("mysqlurl","jdbc:mysql://localhost:3306/monitoring");
        properties.setProperty("mysqluser","claudia");
        properties.setProperty("mysqlpassword","ClaudiaPass");
        
        properties.store(new FileWriter("/tmp/monitoring.properties"),"properties of monitoring");
        DInjector dependencyInjector=new DInjector();
        if (!disabled) dependencyInjector.inject(properties, collector);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testAll()  {
        
       if (disabled) { assert(true); return; }
       
       Date now=new Date();
       String dataSources[]={"rx","tx"};
       List<Number> values= new ArrayList<Number>();
       values.add(30);
       values.add(500);
        try {
            collector.write("collector", "net", null, "if_packets", null, dataSources, values, 
                         now.getTime());
            assert(true);
        } catch (Exception ex) {
            Logger.getLogger(IntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
}
