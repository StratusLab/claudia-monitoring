/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.registerfqn4monitoring_ws;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jomar
 */
public class PersistenceFacade  {
    private MonPersistence persistence=null;
    public PersistenceFacade() {
        Properties config=new Properties();
        LinkedHashMap<String,String[]> configHM=
                new LinkedHashMap<String,String[]>();
        try {
            config.load(new FileReader("/opt/collectd/etc/mapws.properties"));
            
            for (java.util.Map.Entry<Object,Object> entry : config.entrySet()) {
                String arrayString[]=new String[1];
                arrayString[0]=entry.getValue().toString();
                configHM.put(entry.getKey().toString(),arrayString);
            }
            Object className=config.get("classname");
            if (className!=null )
                persistence=MonPersistenceFactory.getPersistence(
                        className.toString(),configHM);
            
        } catch (IOException ex) {
            Logger.getLogger(ItemResource.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public void insertFQNMap(String fqn, String host, String pluginWithInstance) throws SQLException {
        persistence.insertFQNMap(fqn,host,pluginWithInstance);
    }

    
    public String searchFQN(String host, String pluginWithInstance) throws SQLException {
        return persistence.searchFQN(host, pluginWithInstance);
    }

    
    public void deleteFQNMap(String host, String pluginWithInstance) {
        persistence.deleteFQNMap(host,pluginWithInstance);
    }

        
}
