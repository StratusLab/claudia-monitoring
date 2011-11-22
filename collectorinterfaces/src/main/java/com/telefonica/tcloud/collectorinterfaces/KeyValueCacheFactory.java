/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorinterfaces;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public abstract class KeyValueCacheFactory {
    public abstract KeyValueCache getKeyValueCache(Properties config);
    public static KeyValueCache getKeyValueCache(String factoryClassName,URL[] jarList,
            Properties config) {
        try {
            
            URLClassLoader classLoader=URLClassLoader.newInstance(jarList,
                    KeyValueCacheFactory.class.getClassLoader());
            KeyValueCacheFactory factory=Class.forName(factoryClassName,
                    true,classLoader).asSubclass(
                      KeyValueCacheFactory.class).newInstance();
            return factory.getKeyValueCache(config);
        } catch (Exception ex) {
            Logger.getLogger(KeyValueCacheFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static KeyValueCache getKeyValueCache(String factoryClassName,
            Properties config) {
        try {
            KeyValueCacheFactory factory=Class.forName(factoryClassName).asSubclass(
                                    KeyValueCacheFactory.class).newInstance();
            return factory.getKeyValueCache(config);
        } catch (Exception ex) {
            Logger.getLogger(MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
