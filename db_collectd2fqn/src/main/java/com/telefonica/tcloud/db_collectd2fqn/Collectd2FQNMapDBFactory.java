/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.db_collectd2fqn;
import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMapFactory;
import java.util.Properties;
/**
 *
 * @author jomar
 */
public class Collectd2FQNMapDBFactory extends CollectdName2FQNMapFactory {

    @Override
    public CollectdName2FQNMap getConversor(Properties config) {
        Collectd2FQNMapDB conversor=new Collectd2FQNMapDB();
        conversor.setConfig(config);
        return conversor;
    }
    
}
