/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.KeyValueCache;
import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import java.util.Properties;



/**
 *
 * @author jomar
 */
public class CollectdName2FQNDefaultImpl implements 
        CollectdName2FQNMap {
    private Properties config=null;
    //private MonPersistence monPersistence=null;
    public CollectdName2FQNDefaultImpl() {
         config=null;
    }
    
    @Override
    public void setConfig(Properties config) { 
        this.config=config;
    }
    @Override
    public void setMonPersistence(MonPersistence monPersistence) {
      //  this.monPersistence=monPersistence;
    }
    
    @Override
    public void shutdown() {
        
    }
    
    @Override
    public String collectd2FQN(String host, String plugin, String pluginInstance, 
            String type, String typeInstance) {     
     if (host.indexOf('$')!=-1) {
            host=host.replaceFirst("\\$", ".customers.");
            host=host.replaceFirst("\\$", ".services.");
            host=host.replaceFirst("\\$", ".vees.");
            host=host.replaceFirst("\\$", ".replicas.");
     }
     if (pluginInstance==null||pluginInstance.length()==0) return host;
     StringBuilder sbuilder=new StringBuilder(host);
     sbuilder.append('.').append(plugin).append('.').append(pluginInstance);
     return sbuilder.toString();
   }

    @Override
    public void setKeyValueCache(KeyValueCache keyValueCache) {
        // not used.
    }
}
