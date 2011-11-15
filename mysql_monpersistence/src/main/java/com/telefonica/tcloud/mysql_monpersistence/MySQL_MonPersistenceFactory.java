/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.mysql_monpersistence;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public class MySQL_MonPersistenceFactory extends MonPersistenceFactory {
    
    @Override
    public MonPersistence getPersistence(Properties config) {
        try {
                    
            return new MySQL_MonPersistence(config.getProperty("mysqlurl"),
                    config.getProperty("mysqluser"),
                    config.getProperty("mysqlpassword"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySQL_MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(MySQL_MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
