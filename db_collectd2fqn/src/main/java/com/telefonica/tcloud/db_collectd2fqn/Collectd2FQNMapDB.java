/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.db_collectd2fqn;


import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.KeyValueCache;
import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import java.util.Properties;


/**
 *
 * @author jomar
 */
public class Collectd2FQNMapDB implements CollectdName2FQNMap {
    private MonPersistence monPersistence=null;
    private WebServerThread webserver=null;
    private KeyValueCache keyValueCache=null;
    
    @Override
    public String collectd2FQN(String host, String plugin, String pluginInstance, String type, String typeInstance) {
        String fqn=null;
        if (pluginInstance!=null && !pluginInstance.isEmpty() ) {
            try {
                int l=pluginInstance.length();
                String pluginc=plugin+"-"+pluginInstance;
                String key="mon.fqn."+host+";"+pluginc; 
                int length=pluginInstance.length();
                Object fqnO=keyValueCache.getValue(key);
                if (fqnO!=null) return fqnO.toString();
                fqn=monPersistence.searchFQN(host,pluginc);
                if (fqn!=null)  
                   keyValueCache.putValue(key, fqn);
                else {
                    fqn=monPersistence.searchFQN(host,null);
                    if (fqn!=null) { 
                       // Old code, added plugin and pluginstance
                       // automatically. This changed: if you need
                       // pluginstance, create a mapping manually.
                       //fqn=fqn+"."+plugin+"."+pluginInstance;
                       keyValueCache.putValue(key, fqn);
                    } 
                }
            } catch (Exception ex) {
                return null;
            }
           
        } else {
            try {
                String key="mon.fqn."+host;
                Object fqn0=keyValueCache.getValue(key);
                if (fqn0!=null) return fqn0.toString();
                fqn=monPersistence.searchFQN(host,null);
                if (fqn!=null)
                    keyValueCache.putValue(key,fqn);
            } catch (Exception ex) {
                return null;
            }
        } 
        return fqn;
    }
    
 
    @Override
    public void shutdown()  {
        webserver.shutdown();
        if (keyValueCache!=null)
            keyValueCache.unref();
    }
     
    

    @Override
    public void setConfig(Properties config) {
      String port=config.getProperty("webserverport");
      webserver=new WebServerThread(port==null?8080:Integer.decode(port));    
      webserver.setDaemon(true);
      webserver.start();

    }
    @Override
    public void setMonPersistence(MonPersistence monPersistence) {
        this.monPersistence=monPersistence;
    }

    @Override
    public void setKeyValueCache(KeyValueCache keyValueCache) {
        this.keyValueCache=keyValueCache;
        keyValueCache.ref();
    }
}
