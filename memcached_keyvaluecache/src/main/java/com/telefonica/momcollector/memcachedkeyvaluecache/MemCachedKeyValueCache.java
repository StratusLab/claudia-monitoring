/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.momcollector.memcachedkeyvaluecache;

import com.telefonica.tcloud.collectorinterfaces.KeyValueCache;
import com.telefonica.tcloud.collectorinterfaces.KeyValueCacheFactory;
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
public class MemCachedKeyValueCache implements KeyValueCache {

    private int refCounter=0;
    private MemcachedClient memcachedClient=null;
    private KeyValueCache localCache=null;
    public MemCachedKeyValueCache(String servers,int localCapacity) {
              try {
            //space separated list of servers.
            if (localCapacity!=-1) {
                Properties prop=new Properties();
                prop.setProperty("keyvaluecache.capacity",Integer.toString(
                        localCapacity));
                try {
                  localCache=KeyValueCacheFactory.getKeyValueCache(
                   "com.telefonica.moncollector.KeyValueCacheDefaultImplFactory"
                   , prop);
                } catch (Exception e) {}
            }
            if (servers==null) servers="localhost:11211";
           
            memcachedClient=new MemcachedClient(new BinaryConnectionFactory(),
                                               AddrUtil.getAddresses(servers));
      } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            memcachedClient=null;
      }

    }
    public void putValue(String key, Object value) {
        if (localCache!=null) localCache.putValue(key,value);
        if (memcachedClient!=null) memcachedClient.add(key,3600,value);
        
    }

    public Object getValue(String key) {
        if (localCache!=null) {
            Object value=localCache.getValue(key);
            if (value!=null) return value;
            if (memcachedClient==null) return null;
            value=memcachedClient.get(key);
            localCache.putValue(key,value);
            return value;
        } else
        return memcachedClient==null?null:memcachedClient.get(key);
    }

    public void ref() {
        refCounter++;
    }

    public void unref() {
        if (--refCounter==0 && memcachedClient!=null) 
          memcachedClient.shutdown();
    }
    
}
