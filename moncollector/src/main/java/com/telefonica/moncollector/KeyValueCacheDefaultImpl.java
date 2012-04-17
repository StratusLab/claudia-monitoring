/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.moncollector;

import com.telefonica.tcloud.collectorinterfaces.KeyValueCache;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author jomar
 */
public class KeyValueCacheDefaultImpl extends LinkedHashMap<String,Object>
implements KeyValueCache {
    private int maxCapacity;
    int refCounter=0;
    public KeyValueCacheDefaultImpl(int maxCapacity) {
        super();
        this.maxCapacity=maxCapacity;
    }
    
    @Override
    public void putValue(String key, Object value) {
        super.put(key,value);
    }

   
    @Override
    public Object getValue(String key) {
        return super.get(key);
    }

   
    @Override
    public void ref() {
        refCounter++;
    }

    @Override
    public void unref() {
        if (++refCounter==0)
           super.clear();
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size()>maxCapacity;
    }
    
}
