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
public  abstract class MonPublisherFactory {
    public abstract MonPublisher getPublisher(Properties config);
        public static MonPublisher getPublisher(String factoryClassName,URL[] jarList,
            Properties config) {
        try {
            
            URLClassLoader classLoader=URLClassLoader.newInstance(jarList,
                    MonPublisherFactory.class.getClassLoader());
            
            MonPublisherFactory factory=Class.forName(factoryClassName,
                    true,classLoader).asSubclass(
                      MonPublisherFactory.class).newInstance();
            return factory.getPublisher(config);
        } catch (Exception ex) {
            Logger.getLogger(CollectdName2FQNMapFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public static MonPublisher getPublisher(String factoryClassName,
            Properties config) {
        try {
            MonPublisherFactory factory=Class.forName(factoryClassName).asSubclass(
                                    MonPublisherFactory.class).newInstance();
            return factory.getPublisher(config);
        } catch (Exception ex) {
            Logger.getLogger(MonPublisherFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
