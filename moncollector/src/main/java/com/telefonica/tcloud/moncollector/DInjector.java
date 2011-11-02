/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMapFactory;
import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import com.telefonica.tcloud.collectorinterfaces.MonPublisher;
import com.telefonica.tcloud.collectorinterfaces.MonPublisherFactory;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 *
 * @author jomar
 */
public class DInjector {
       
    public void inject(String fileName,Collector collector) throws IOException {
        Properties properties=new Properties();
        properties.load(new FileReader(fileName));
        inject(properties,collector);
    }
    public void inject(Properties properties,Collector collector) throws
            IOException  {
        LinkedHashMap<String,String[]> configuration=new LinkedHashMap();
        MonPersistence monPersistence=null;
                
        for(String key : properties.stringPropertyNames()) {
            String values[]=new String[1];
            values[0]=properties.getProperty(key);
            configuration.put(key, values);
        }
        
        String measuresFilterName=properties.getProperty("measuresfilter.path");
        String hostFilterName=properties.getProperty("hostfilter.path");
        String publisher=properties.getProperty("publisher.class");
        String persistence=properties.getProperty("persistence.class");
        String conversor=properties.getProperty("conversor2fqn.class");
        String measuresTypesDir=properties.getProperty("measuretypes.path");
        
        
        collector.setHostFilter(hostFilterName!=null?new HostFilter(hostFilterName)
                :null);
        collector.setMeasuresFilter(measuresFilterName!=null?
                new MeasuresFilter(measuresFilterName):null);
        
        if (publisher!=null) {
            MonPublisher monPublisher=MonPublisherFactory.getPublisher(
                    publisher,configuration);
            collector.setMonPublisher(monPublisher);
                    
        } else collector.setMonPublisher(null);
        if (persistence!=null) {
            collector.setMonPersistence(monPersistence=
                MonPersistenceFactory.getPersistence(persistence, configuration)
                    );
        } else collector.setMonPersistence(null);
        if (conversor!=null) {
            //Class.forName("").getConstructor(String.class).newInstance("");
            CollectdName2FQNMap map=CollectdName2FQNMapFactory.getConversor(
                    conversor, configuration);
            map.setMonPersistence(monPersistence);
            collector.setConversor2FQN(map);
                    
        } else collector.setConversor2FQN(null);
        collector.setMeasuresTypeTable(measuresTypesDir!=null
                ?new MeasureTypeTable(measuresTypesDir)
                :new MeasureTypeTable());
        
        
    }
}