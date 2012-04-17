/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.moncollector.test;

import com.telefonica.moncollector.DInjector;
import com.telefonica.moncollector.Collector;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.moncollector.Collector;
import com.telefonica.moncollector.DInjector;
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
    boolean enabled=false;
    public IntegrationTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        collector=new Collector();
        
        Properties properties=new Properties();
        
        properties.setProperty("modules.path","/opt/monitoring/modules/");
        properties.setProperty("measuresfilter.path", "/opt/monitoring/conf/filter.conf");
        properties.setProperty("hostfilter.path", "/opt/monitoring/conf/hostFilter.conf");
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
        if (enabled) dependencyInjector.inject(properties, collector);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testAll()  {
       
       if (!enabled) {  return; }
       
       Date now=new Date();
       String dataSources[]={"rx","tx"};
       List<Number> values= new ArrayList<Number>();
       values.add(30);
       values.add(500);
       
        try {
            ItemResource_JerseyClient restClient=new ItemResource_JerseyClient("collector2");
            restClient.putText("es.tid.customers.cc1.services.monitoring.vees.collector.replicas.2");
            collector.write("collector", "net", null, "if_packets", null, dataSources, values, 
                         now);
            
            assert(true);
        } catch (Exception ex) {
            Logger.getLogger(IntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
       
    }

    static class ItemResource_JerseyClient {

        private WebResource webResource;
        private Client client;
        private static final String BASE_URI = "http://localhost:8080/registerfqn4monitoring_ws/webresources/";

        public ItemResource_JerseyClient(String name) {
            com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
            client = Client.create(config);
            String resourcePath = java.text.MessageFormat.format("maps2fqn/{0}", new Object[]{name});
            webResource = client.resource(BASE_URI).path(resourcePath);
        }

        public void setResourcePath(String name) {
            String resourcePath = java.text.MessageFormat.format("maps2fqn/{0}", new Object[]{name});
            webResource = client.resource(BASE_URI).path(resourcePath);
        }

        public String getText() throws UniformInterfaceException {
            WebResource resource = webResource;
            return resource.accept(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
        }

        public void delete() throws UniformInterfaceException {
            webResource.delete();
        }

        public void putText(Object requestEntity) throws UniformInterfaceException {
            webResource.type(javax.ws.rs.core.MediaType.TEXT_PLAIN).put(requestEntity);
        }

        public void close() {
            client.destroy();
        }
    }
    
   
}
