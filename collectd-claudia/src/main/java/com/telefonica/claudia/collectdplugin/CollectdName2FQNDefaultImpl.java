/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.claudia.collectdplugin;

import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import java.util.LinkedHashMap;



/**
 *
 * @author jomar
 */
public class CollectdName2FQNDefaultImpl implements 
        CollectdName2FQNMap {
    private LinkedHashMap<String,String[]> config=null;
    //private MonPersistence monPersistence=null;
    public CollectdName2FQNDefaultImpl() {
         config=null;
    }
    
    public void setConfig(LinkedHashMap<String,String[]> config) { 
        this.config=config;
    }
    public void setMonPersistence(MonPersistence monPersistence) {
      //  this.monPersistence=monPersistence;
    }
    
    public void shutdown() {
        
    }
    
    public String collectd2FQN(String host, String plugin, String pluginInstance, 
            String type, String typeInstance) {     
     if (host.indexOf('$')!=-1) {
            host=host.replaceFirst("\\$", "customers");
            host=host.replaceFirst("\\$", "services");
            host=host.replaceFirst("\\$", "vees");
            host=host.replaceFirst("\\$", "replicas");
     }
     if (pluginInstance==null||pluginInstance.length()==0) return host;
     StringBuilder sbuilder=new StringBuilder(host);
     sbuilder.append('.').append(plugin).append('.').append(pluginInstance);
     return sbuilder.toString();
   } 
}
