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
    void putValue(String key,Object value);
    Object getValue(String key);
    void ref();
    void unref();
}
