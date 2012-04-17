/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.momcollector.memcachedkeyvaluecache;

import com.telefonica.tcloud.collectorinterfaces.KeyValueCache;
import com.telefonica.tcloud.collectorinterfaces.KeyValueCacheFactory;
import java.util.Properties;

/**
 *
 * @author jomar
 */
public class MemCachedKeyValueCacheFactory extends KeyValueCacheFactory {

    @Override
    public KeyValueCache getKeyValueCache(Properties config) {
       String servers=config.getProperty("memcached.servers");
       String localCapacity=config.getProperty("memcached.local_cache_capacity");
       return new MemCachedKeyValueCache(servers,localCapacity==null?-1:
                Integer.decode(localCapacity));

    }
    
}
