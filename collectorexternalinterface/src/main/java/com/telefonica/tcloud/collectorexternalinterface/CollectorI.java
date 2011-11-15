/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.collectorexternalinterface;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jomar
 */
public interface CollectorI {
    public void write(String host,String plugin,String pluginInstance,
            String type,String typeInstance,String dataSources[],
            List<Number> values,Date timestamp) throws Exception ;
    public int shutdown() ;
    
    public void autoInjectDependencies(String propertiesFile) throws IOException;
}
