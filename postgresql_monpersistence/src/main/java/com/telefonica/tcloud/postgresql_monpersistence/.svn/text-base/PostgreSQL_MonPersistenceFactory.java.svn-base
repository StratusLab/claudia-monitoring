/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.postgresql_monpersistence;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public class PostgreSQL_MonPersistenceFactory extends MonPersistenceFactory {
    private Connection con=null;
    private PreparedStatement insertFQNMap,deleteFQNMap,searchFQN,
            searchFQNPlugNull,deleteFQNMapPlugNull;
    private PreparedStatement selectAssociatedId,insertNodeDirectory;
    private PreparedStatement insertMeasure,markNodeAsInactive;
    private HashMap<String,Long> associatedIdCache;
    @Override
    public MonPersistence getPersistence(LinkedHashMap<String, String[]> config) {
        try {
            return new PostgreSQL_MonPersistence(config.get("pgsqlurl")[0],
                    config.get("pgsqluser")[0],config.get("pgsqlpassword")[0]);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PostgreSQL_MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQL_MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
