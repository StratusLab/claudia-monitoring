/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.mysql_monpersistence;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public class MySQL_MonPersistenceFactory extends MonPersistenceFactory {
    
    @Override
    public MonPersistence getPersistence(LinkedHashMap<String, String[]> config) {
        try {
            System.out.println(config.get("mysqlurl")[0]+
                    config.get("mysqluser")[0]+
                    config.get("mysqlpassword")[0]);
                    
            return new MySQL_MonPersistence(config.get("mysqlurl")[0],
                    config.get("mysqluser")[0],
                    config.get("mysqlpassword")[0]);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySQL_MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(MySQL_MonPersistenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
