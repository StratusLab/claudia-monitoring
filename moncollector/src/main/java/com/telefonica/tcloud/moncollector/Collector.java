/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.claudia.collectdplugin;

import com.telefonica.tcloud.collectorinterfaces.CollectdName2FQNMap;
import com.telefonica.tcloud.collectorinterfaces.MonPersistence;
import com.telefonica.tcloud.collectorinterfaces.MonPublisher;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jomar
 */
public class Collector {
    private MeasureTypeTable measureTypes=null;
    private CollectdName2FQNMap conversor=null;
    private MeasuresFilter filter=null;
    private HostFilter hostFilter=null;
    private MonPersistence persistence=null;
    private MonPublisher publishService=null;

    public void setMonPersistence(MonPersistence monPersistence) {
        persistence=monPersistence;
    }
    
    public void setMonPublisher(MonPublisher monPublisher) {
        publishService=monPublisher;
    }

    public void setHostFilter(HostFilter hostFilter) {
        this.hostFilter=hostFilter;
    }
    
    public void setMeasuresFilter(MeasuresFilter measuresFilter) {
        this.filter=measuresFilter;
    }
    
    public Collector() throws IOException {
        measureTypes=new MeasureTypeTable();
    }
    
    public void write(String host,String plugin,String pluginInstance,
            String type,String typeInstance,String dataSources[],
            List<Number> values,long timestamp) throws Exception {
        StringBuilder errorMessage=new StringBuilder();
        
        if (hostFilter!=null && !hostFilter.accept(host)) return;
        if (filter!=null && !filter.accept(plugin,pluginInstance)) 
            return;
          
        String fqn=null;
        
        Iterator<MeasureType> itMT=measureTypes.getMeauresTypeFromDataSources(
                  type,typeInstance,dataSources).iterator();
                  
          
        for (Number n: values) {
              MeasureType measureType=itMT.next();
              Date date=new Date(timestamp);
              if (measureType==null) continue;
              // only get fqn if the measure is used.
              if (fqn==null) {
                  fqn=conversor.collectd2FQN(host,plugin,
                      pluginInstance,type,typeInstance);
                  if (fqn==null) return;
              }
              if (persistence!=null) try {
        
                persistence.insertData(date, fqn,
                        measureType.getMeasureType(),measureType.getMeasureUnit()
                        ,n);
              } catch (SQLException ex) {
                errorMessage.append("error saving data to database ");
                errorMessage.append(ex.toString());
              }
              if (publishService!=null)
                publishService.publish(date, fqn, measureType.getMeasureType(),
                      measureType.getMeasureUnit(), n);
              
              if (errorMessage.length()!=0) throw new Exception(
                      errorMessage.toString());
        }

    }
    
    public int shutdown() {
        if (persistence!=null) {
          persistence.shutdown();
          persistence=null;
        }
        if (publishService!=null) {
            publishService.shutdown();
            publishService=null;
        }
        conversor.shutdown();
        return 0;
        
    }
}
