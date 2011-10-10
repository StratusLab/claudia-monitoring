/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorinterfaces;

import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author jomar
 */
public interface MonPersistence {
    public void insertData(Date time,String fqn,String measuredType,
            String measureUnit, Number value) throws SQLException;
    public void insertFQNMap(String fqn,String host,String pluginWithInstance)
            throws SQLException;
    public String searchFQN(String host,String pluginWithInstance) throws SQLException;
    public void deleteFQNMap(String host,String pluginWithInstance);
    public void shutdown();
}
