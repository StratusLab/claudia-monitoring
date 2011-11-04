/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.claudia.collectdplugin;

import com.telefonica.tcloud.collectorexternalinterface.CollectorI;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.collectd.api.Collectd;
import org.collectd.api.CollectdConfigInterface;
import org.collectd.api.CollectdFlushInterface;
import org.collectd.api.CollectdShutdownInterface;
import org.collectd.api.CollectdWriteInterface;
import org.collectd.api.DataSource;
import org.collectd.api.OConfigItem;
import org.collectd.api.ValueList;

/**
 *
 * @author jomar
 */
public class PluginCollectd implements CollectdConfigInterface, 
        CollectdWriteInterface,CollectdFlushInterface ,
        CollectdShutdownInterface  {

    private CollectorI collector=null;
    private HashMap<String,String[]> dataSourcesByType=
            new HashMap<String,String[]>();
    
    @SuppressWarnings("LeakingThisInConstructor")
    public PluginCollectd(String configurationFile) throws IOException {
        Collectd.registerWrite("ClaudiaCollector",this);
        Collectd.registerConfig("ClaudiaCollector", this);
        Collectd.registerFlush("ClaudiaCollector", this);
        Collectd.registerShutdown("ClaudiaCollector", this);
        
    }
    @Override
    public int config(OConfigItem ci) {
        
        String collectorClass=null,collectorConfig=null;
        for (OConfigItem item : ci.getChildren()) {
            if (item.getKey().equalsIgnoreCase("collectorclass")) 
                 collectorClass=item.getValues().get(0).toString();
            if (item.getKey().equalsIgnoreCase("collectorconfig")) 
                 collectorConfig=item.getValues().get(0).toString();
        }
        try {
            
            
            collector.autoInjectDependencies(collectorConfig);
            return 0;
        } catch (IOException ex) {
            Logger.getLogger(PluginCollectd.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int write(ValueList vl) {
        String[] dataSources=dataSourcesByType.get(vl.getDataSet().getType());
        if (dataSources==null) {
           List<DataSource> lDataSources=vl.getDataSet().getDataSources();
           dataSources=new String[lDataSources.size()];
           int count=0;
           for (DataSource ds : lDataSources ) {
               dataSources[count]=ds.getName();
           }
           dataSourcesByType.put(vl.getType(), dataSources);
        }
        try {
            collector.write(vl.getHost(), vl.getPlugin(), vl.getPluginInstance(), 
                    vl.getType(), vl.getTypeInstance(), dataSources, vl.getValues(),
                    vl.getTime());
        } catch (Exception ex) {
            Logger.getLogger(PluginCollectd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public int flush(Number timeout, String identifier) {
        return 0;
    }

    @Override
    public int shutdown() {
        return collector.shutdown();
    }
    
    
}
