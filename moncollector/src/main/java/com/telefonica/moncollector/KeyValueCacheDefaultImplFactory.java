package com.telefonica.moncollector;

import com.telefonica.tcloud.collectorinterfaces.KeyValueCache;
import com.telefonica.tcloud.collectorinterfaces.KeyValueCacheFactory;
import java.util.Properties;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jomar
 */
public class KeyValueCacheDefaultImplFactory extends KeyValueCacheFactory {
    public final static int default_capacity=50000;
    @Override
    public KeyValueCache getKeyValueCache(Properties config) {
        String capacity=
                config.getProperty("keyvaluecache.capacity");
        
        return new KeyValueCacheDefaultImpl(capacity==null?default_capacity:
                Integer.decode(capacity));
    }
    
}
