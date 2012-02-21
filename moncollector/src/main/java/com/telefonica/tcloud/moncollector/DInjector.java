/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMapFactory;
import com.telefonica.tcloud.collectorinterfaces.KeyValueCache;
import com.telefonica.tcloud.collectorinterfaces.KeyValueCacheFactory;
import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import com.telefonica.tcloud.collectorinterfaces.MonPublisher;
import com.telefonica.tcloud.collectorinterfaces.MonPublisherFactory;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author jomar
 */


public class DInjector {
    
    public static final String default_modules_path="/opt/monitoring/modules/";
    private URL[] jarList2URLs(String baseDir,String jarList) throws MalformedURLException {
      String jars[]=jarList.split(",");
      URL urls[]=new URL[jars.length];
      String baseUrl="file://"+baseDir+'/';
      int cont=0;
      for (String jar: jars) {
          urls[cont++]=new URL(baseUrl+jar);
      }
      return urls;
    }
    
    public void inject(String fileName,Collector collector) throws IOException {
        Properties properties=new Properties();
        properties.load(new FileReader(fileName));
        inject(properties,collector);
    }
    public void inject(Properties properties,Collector collector) throws
            IOException  {
        MonPersistence monPersistence=null;
        KeyValueCache keyValueCache=null;
                
        
        String publishOnly=properties.getProperty("publishonly");
        String modules=properties.getProperty("modules.path");
        String measuresFilterName=properties.getProperty("measuresfilter.path");
        String hostFilterName=properties.getProperty("hostfilter.path");
        String publisher=properties.getProperty("publisher.class");
        String persistence=properties.getProperty("persistence.class");
        String conversor=properties.getProperty("conversor2fqn.class");
        String measuresTypesDir=properties.getProperty("measuretypes.path");
        String keyValueCacheName=properties.getProperty("keyvaluecache.class");
        
        if (modules==null) modules=default_modules_path;
        
        collector.setHostFilter(hostFilterName!=null?new HostFilter(hostFilterName)
                :null);
        collector.setMeasuresFilter(measuresFilterName!=null?
                new MeasuresFilter(measuresFilterName):null);
        if (keyValueCacheName!=null) {
            String jars=properties.getProperty("keyvaluecache.jars");
            URL jarURLs[]=jarList2URLs(modules,jars);
            
            keyValueCache=KeyValueCacheFactory.getKeyValueCache(keyValueCacheName,
                    jarURLs,properties);
        } else {
            
            keyValueCache=new KeyValueCacheDefaultImpl(30000);
            
        }
        if (publisher!=null) {
            String publisherJars=properties.getProperty("publisher.jars");
            URL jarURLs[]=jarList2URLs(modules,publisherJars);
            
            MonPublisher monPublisher=MonPublisherFactory.getPublisher(
                    publisher,jarURLs,properties);
            collector.setMonPublisher(monPublisher);
                    
        } else collector.setMonPublisher(null);
        if (persistence!=null) {
            String persistenceJars=properties.getProperty("persistence.jars");
            URL jarURLs[]=jarList2URLs(modules,persistenceJars);
            collector.setMonPersistence(monPersistence=
                MonPersistenceFactory.getPersistence(persistence, jarURLs,
                    properties)
                    );
        } else collector.setMonPersistence(null);
        if (conversor!=null) {
            //Class.forName("").getConstructor(String.class).newInstance("");
            String conversorJars=properties.getProperty("conversor2fqn.jars");
            URL jarURLs[]=jarList2URLs(modules,conversorJars);
            CollectdName2FQNMap map=CollectdName2FQNMapFactory.getConversor(
                    conversor, jarURLs, properties);
            map.setMonPersistence(monPersistence);
            map.setKeyValueCache(keyValueCache);
            collector.setConversor2FQN(map);
                    
        } else collector.setConversor2FQN(null);
        collector.setMeasuresTypeTable(measuresTypesDir!=null
                ?new MeasureTypeTable(measuresTypesDir)
                :new MeasureTypeTable());
        
        if (publishOnly!=null&&publishOnly.equalsIgnoreCase("true"))
            collector.setPublishOnly(true);
        
    }
}
