/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.db_collectd2fqn;


import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 *
 * @author jomar
 */
public class Collectd2FQNMapDB implements CollectdName2FQNMap {
    private MemcachedClient memcachedClient=null;
    private MonPersistence monPersistence=null;
    private WebServerThread webserver;
    
    
    @Override
    public String collectd2FQN(String host, String plugin, String pluginInstance, String type, String typeInstance) {
        String fqn=null;
        if (pluginInstance!=null && !pluginInstance.isEmpty() ) {
            try {
                int l=pluginInstance.length();
                String pluginc=plugin+"-"+pluginInstance;
                String key="mon.fqn."+host+";"+pluginc; 
                int length=pluginInstance.length();
                Object fqnO=memcachedClient.get(key);
                if (fqnO!=null) return fqnO.toString();
                fqn=monPersistence.searchFQN(host,pluginc);
                if (fqn!=null)  
                   memcachedClient.add(key, 36000, fqn);
                else {
                    fqn=monPersistence.searchFQN(host,null);
                    if (fqn!=null) { 
                       fqn=fqn+"."+plugin+"."+pluginInstance;
                       memcachedClient.add(key, 36000, fqn);
                    } 
                }
            } catch (Exception ex) {
                return null;
            }
           
        } else {
            try {
                String key="mon.fqn."+host;
                Object fqn0=memcachedClient.get(key);
                if (fqn0!=null) return fqn0.toString();
                fqn=monPersistence.searchFQN(host,null);
                if (fqn!=null)
                  memcachedClient.add(key, 36000, fqn);
            } catch (Exception ex) {
                return null;
            }
        } 
        return fqn;
    }
    
 
    @Override
    public void shutdown()  {
        memcachedClient.shutdown();
        webserver.shutdown();
    }
     
    

    @Override
    public void setConfig(Properties config) {
      String port=config.getProperty("webserverport");
      webserver=new WebServerThread(port==null?8080:Integer.decode(port));    
      webserver.setDaemon(true);
      webserver.start();

      try {
            //space separated list of servers.
            String servers=config.getProperty("memcachedservers");
            if (servers==null) servers="localhost:11211";
           
            memcachedClient=new MemcachedClient(new BinaryConnectionFactory(),
                                               AddrUtil.getAddresses(servers));
      } catch (Exception ex) {
            Logger.getLogger(Collectd2FQNMapDB.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    @Override
    public void setMonPersistence(MonPersistence monPersistence) {
        this.monPersistence=monPersistence;
    }
}
