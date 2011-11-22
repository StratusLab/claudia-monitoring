/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorinterfaces;

/**
 *
 * @author jomar
 */
public interface KeyValueCache {
    void put(String key,String value);
    void putObject(String key,Object value);
    String get(String key);
    Object getObject(String key);
}
