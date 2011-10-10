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
public abstract class MonPersistenceFactory {
    public abstract MonPersistence getPersistence(LinkedHashMap<String,String[]> config);
    public static MonPersistence getPersistence(String factoryClassName,
            LinkedHashMap<String,String[]> config) {
        try {
            MonPersistenceFactory factory=Class.forName(factoryClassName).asSubclass(
                                    MonPersistenceFactory.class).newInstance();
            return factory.getPersistence(config);
        } catch (Exception ex) {
            Logger.getLogger(MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
