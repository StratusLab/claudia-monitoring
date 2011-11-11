/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorinterfaces;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public abstract class CollectdName2FQNMapFactory {
    public abstract CollectdName2FQNMap getConversor(LinkedHashMap<String,String[]> config);
    public static CollectdName2FQNMap getConversor(String factoryClassName,URL[] jarList,
            LinkedHashMap<String,String[]> config) {
        try {
            URLClassLoader classLoader=URLClassLoader.newInstance(jarList,
                    CollectdName2FQNMapFactory.class.getClassLoader());
            CollectdName2FQNMapFactory factory=Class.forName(factoryClassName,
                    true,classLoader).asSubclass(
                      CollectdName2FQNMapFactory.class).newInstance();
            return factory.getConversor(config);
        } catch (Exception ex) {
            Logger.getLogger(CollectdName2FQNMapFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public static CollectdName2FQNMap getConversor(String factoryClassName,
            LinkedHashMap<String,String[]> config) {
        try {
            CollectdName2FQNMapFactory factory=Class.forName(factoryClassName).asSubclass(
                                    CollectdName2FQNMapFactory.class).newInstance();
            return factory.getConversor(config);
        } catch (Exception ex) {
            Logger.getLogger(MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    
}
