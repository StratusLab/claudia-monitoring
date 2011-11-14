/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.registerfqn4monitoring_ws;

import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPersistenceFactory;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    public static final String default_modules_path="/opt/monitoring/modules/";
    static private MonPersistence injectedPersistence=null;
    private MonPersistence persistence=null;
    public PersistenceFacade() {
        if (injectedPersistence!=null) {
            persistence=injectedPersistence;
            return;
        }
        Properties config=new Properties();
        LinkedHashMap<String,String[]> configHM=
                new LinkedHashMap<String,String[]>();
        try {
            config.load(new FileReader("/opt/monitoring/conf/monitoring.properties"));
            
            for (java.util.Map.Entry<Object,Object> entry : config.entrySet()) {
                String arrayString[]=new String[1];
                arrayString[0]=entry.getValue().toString();
                configHM.put(entry.getKey().toString(),arrayString);
            }
            String className=config.getProperty("persistence.class");
            String jars=config.getProperty("persistence.jars");
            if (jars!=null) {
                String dir=config.getProperty("modules.path");
                URL urls[]=jarList2URLs(dir==null?default_modules_path:dir
                        ,jars);
                persistence=MonPersistenceFactory.getPersistence(className,urls,
                        configHM);
            } else   
                persistence=MonPersistenceFactory.getPersistence(
                        className,configHM);
            
        } catch (IOException ex) {
            Logger.getLogger(ItemResource.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private URL[] jarList2URLs(String baseDir,String jarList) throws MalformedURLException {
      String jars[]=jarList.split(",");
      URL urls[]=new URL[jars.length];
      String baseUrl="file://"+baseDir+'/';
      int cont=0;
      for (String jar: jars) {
          urls[cont++]=new URL(baseUrl+jar);
      }
      return urls;
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

    static public void injectMonPersistence(MonPersistence persistence) {
        injectedPersistence=persistence;
    }    
}
