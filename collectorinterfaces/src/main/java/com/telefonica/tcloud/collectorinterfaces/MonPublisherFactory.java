/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorinterfaces;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public  abstract class MonPublisherFactory {
    public abstract MonPublisher getPublisher(LinkedHashMap<String,String[]> config);
    public static MonPublisher getPublisher(String factoryClassName,
            LinkedHashMap<String,String[]> config) {
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
