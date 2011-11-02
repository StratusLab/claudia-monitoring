/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMapFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author jomar
 */
public class BasicTest extends TestCase {
    private Collector collector=null;
    
    public BasicTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        collector=new Collector();
        String measureTypes[]={"if_packages.rx:networkIn:packages",
                              "if_packages.tx:networkOut:packages"};
        MeasureTypeTable table=new MeasureTypeTable(measureTypes);
        collector.setMeasuresTypeTable(table);
        collector.setConversor2FQN(new CollectdName2FQNDefaultImpl());
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        collector.shutdown();
    }
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    
    
    public void testWriteMeasure() {
       PublisherForTesting publisher=new PublisherForTesting(false);
       collector.setMonPublisher(publisher);
      
       Date now=new Date();
       String dataSources[]={"rx","tx"};
       List<Number> values= new ArrayList<Number>();
       values.add(30);
       values.add(500);
       
       try {
            collector.write("acme$tid$s1$vee1$1", "net", null, "if_packages", null, dataSources, values, 
                    now.getTime());

            assertTrue(
              publisher.getPublishedData().contains(
                    "acme.customers.tid.services.s1.vees.vee1.replicas.1,networkIn=30")
                    &&
              publisher.getPublishedData().contains(
                    "acme.customers.tid.services.s1.vees.vee1.replicas.1,networkOut=500"));
                    
        } catch (Exception ex) {
            Logger.getLogger(BasicTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
    }
    
    public void testDynamicLoad() {
        try {
            URL urls[]={new URL(
                "file:////opt/collectd/share/collectd/java/db_collectd2fqn.jar"),
                new URL(
                "file:////opt/collectd/share/collectd/java/spymemcached-2.5_2.jar"),
            };
            CollectdName2FQNMap map=CollectdName2FQNMapFactory.getConversor(
                    
                    "com.telefonica.tcloud.db_collectd2fqn.Collectd2FQNMapDBFactory",
                    urls,null);
            assertNotNull(map);
        } catch (MalformedURLException ex) {
            Logger.getLogger(BasicTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
                
    }
}
