/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
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
public class IntegrationTest2 extends TestCase {
    Collector collector=null;
    // only enable this test if you have a local installation. Disable with
    // continous integration.
    boolean enabled=true;
    public IntegrationTest2(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        collector=new Collector();
        
        DInjector dependencyInjector=new DInjector();
        if (enabled) dependencyInjector.inject(
                "/opt/monitoring/conf/monitoring.properties", collector);
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
            ItemResource_JerseyClient restClient=new ItemResource_JerseyClient("localhost.localdomain");
            restClient.putText("es.tid.customers.cc1.services.monitoring.vees.collector.replicas.3");
            collector.write("collector", "net", null, "if_packets", null, dataSources, values, 
                         now.getTime());
            
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
