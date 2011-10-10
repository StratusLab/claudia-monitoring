/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorinterfaces;

import java.util.LinkedHashMap;


/**
 * This interface must be implemented by a class whose name is
 * set in the collectd configuration.
 * 
 * The contract of this interface is convert from Collectd database
 * to a FQN and (optionally) a preffix to apply to measuredType.
 * 
 * @author Chema
 * 
 */
public interface CollectdName2FQNMap {
      
        
        /**
         * Method that receives the collectd configuration
         * 
         * The object OConfigItem is defined in the collectd Java
         * plugin documentation.
         * 
         * @param config a object with the configuration
         */
    public void setConfig(LinkedHashMap<String,String[]> config);    
    
    public void setMonPersistence(MonPersistence monPersistence);
    
        /**
         * Method that translate from collectd name schema to fqn
         * 
         * This method is invoked for each dataset received; it translate
         * from collectd name schema (host,plugin[,pluginstance],type[,typeinst]
         * to a fqn 
         * 
         * @param host hostname origin of the data
         * @param plugin plugin origin of the data
         * @param pluginInstance (optional) plugin instance origin of the data
         * @param type dataset 
         * @param typeInstance (optional) dataset instance
         * @return String The fqn 
         */
    public String collectd2FQN(String host,String plugin,String pluginInstance,
            String type,String typeInstance);
    
    public void shutdown();}
