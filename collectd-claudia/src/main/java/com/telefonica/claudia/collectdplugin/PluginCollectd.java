/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.claudia.collectdplugin;

import com.telefonica.tcloud.collectorexternalinterface.CollectorI;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
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
import org.collectd.api.OConfigValue;
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
    private Logger logger=null;
    @SuppressWarnings("LeakingThisInConstructor")
    public PluginCollectd() throws IOException {
        logger=Logger.getLogger("monitoring.PluginCollectd");
        Collectd.registerConfig("collector", this);
        Collectd.registerFlush("collector", this);
        Collectd.registerShutdown("collector", this);
        
    }
    @Override
    public int config(OConfigItem ci) {
        
        String collectorClass=null,collectorConfig=null;
        List<OConfigValue> collectorJars=null;
        for (OConfigItem item : ci.getChildren()) {
            if (item.getKey().equalsIgnoreCase("collectorclass")) 
                 collectorClass=item.getValues().get(0).toString();
            if (item.getKey().equalsIgnoreCase("collectorconfig")) 
                 collectorConfig=item.getValues().get(0).toString();
            if (item.getKey().equalsIgnoreCase("collectorjars")) 
                 collectorJars=item.getValues();
        }
        try {
            if (collectorJars==null) collector=
                    Class.forName(collectorClass).asSubclass(
                    CollectorI.class).newInstance();
            else {
                
                URL urls[]=new URL[collectorJars.size()];
                int cont=0;
                for(OConfigValue jarPath : collectorJars) {
                    urls[cont++]=new URL("file://"+jarPath);
                } 
                
                collector=Class.forName(collectorClass,true,
                        URLClassLoader.newInstance(urls)).asSubclass(
                        CollectorI.class).newInstance();
            }
            collector.autoInjectDependencies(collectorConfig);
            logger.fine("MonCollector has been configured.");
            Collectd.registerWrite("collector",this);
            return 0;
        } catch (InstantiationException ex) {
            Logger.getLogger(PluginCollectd.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PluginCollectd.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PluginCollectd.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
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
            Date timestamp=new Date(vl.getTime());
            logger.log(Level.FINEST, "Received {0};{1}_{2};{3}_{4} (Thread id:{5})",
                    new Object[]{timestamp, vl.getPlugin(), vl.getPluginInstance(),
                        vl.getType(), vl.getTypeInstance(),Thread.currentThread().getId()});
            collector.write(vl.getHost(), vl.getPlugin(), vl.getPluginInstance(), 
                    vl.getType(), vl.getTypeInstance(), dataSources, vl.getValues(),
                    timestamp);
            logger.log(Level.FINEST, "Sended (Thread Id: {0})", Thread.currentThread().getId());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "collector throw an exception:", ex);
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
