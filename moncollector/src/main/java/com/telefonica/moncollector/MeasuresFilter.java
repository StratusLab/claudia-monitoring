/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.moncollector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 *
 * @author jomar
 */
public class MeasuresFilter {
    // filter by plugin.plugin_instance
    //     or by plugin
    
                    
    private boolean whiteList=false;
    private HashSet<String> filter;
    
    public MeasuresFilter(String file) throws FileNotFoundException, IOException {
        BufferedReader reader=new BufferedReader(new FileReader(file)) ;
        filter=new HashSet<String>();
        boolean eof=false;
        while (!eof) {
            String line=reader.readLine();
            if (line==null) eof=true;
            else {
               filter.add(line);
            }
  
        }
        reader.close();
        whiteList=filter.contains("!"); 
    }
    boolean accept(String plugin,String pluginInstance) {
       if (pluginInstance==null) pluginInstance="";
       if (whiteList) {
           if (pluginInstance.length()>0&&filter.contains(plugin+'-'+
                   pluginInstance)) return true;
           if (filter.contains(plugin)&&!filter.contains('!'+plugin))
               return true;
           return false;
       } else {
           if (pluginInstance.length()>0&&filter.contains('!'+plugin+'-'+
                   pluginInstance)) return false;
           if (filter.contains('!'+plugin)&&(pluginInstance.length()==0||
                   !filter.contains(plugin+'-'+pluginInstance)))
               return false;
           else return true;
       }
    }
}
