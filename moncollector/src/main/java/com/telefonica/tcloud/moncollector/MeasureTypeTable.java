/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.moncollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author jomar
 */
public class MeasureTypeTable {
    private final static String measuresTypeDir=
        "/opt/collectd/share/collectd/measures.d";
    private HashMap<String,MeasureType> table;
    private HashMap<String,ArrayList<MeasureType>> tableByType;
    
    public MeasureTypeTable() throws FileNotFoundException, IOException {
        this(measuresTypeDir);
    }
    public MeasureTypeTable(String configurationDir) throws FileNotFoundException, 
            IOException {
        File dir=new File(configurationDir);
        table=new HashMap<String,MeasureType>();
        tableByType=new HashMap<String,ArrayList<MeasureType>>();
        for (File file: dir.listFiles()) {
          BufferedReader reader=new BufferedReader(new FileReader(file)) ;
          boolean eof=false;
          while (!eof) {
            String line=reader.readLine();
            if (line==null) eof=true;
            else {
                int sep=line.indexOf(':');
                int sep2=line.lastIndexOf(':');
                if (sep<0||sep2<0) continue;
                table.put(line.substring(0,sep),new MeasureType(
                        line.substring(sep+1,sep2),line.substring(sep2+1)));
            }
          }
          reader.close();
        } 
    }
   
    public MeasureTypeTable(String measuresTypes[]) {
        table=new HashMap<String,MeasureType>();
        tableByType=new HashMap<String,ArrayList<MeasureType>>();
        for (String line : measuresTypes) {
            int sep=line.indexOf(':');
                int sep2=line.lastIndexOf(':');
                if (sep<0||sep2<0) continue;
                table.put(line.substring(0,sep),new MeasureType(
                        line.substring(sep+1,sep2),line.substring(sep2+1)));
            
        }        
    }
   
    public MeasureType getMeasureType(String typeWithTypeInstance,
            String datasource) {
        return table.get(typeWithTypeInstance+'.'+datasource);
        
    }

    
    
    public ArrayList<MeasureType> getMeauresTypeFromDataSources(String type,
            String typeInstance,String dataSources[]) {
        if (typeInstance!=null && typeInstance.length()!=0)
            type=type+"-"+typeInstance;
        ArrayList<MeasureType> result=tableByType.get(type);
        if (result!=null) return result;
        result=new ArrayList<MeasureType>();
        for (String dataSource : dataSources) 
            result.add(getMeasureType(type,dataSource));
        tableByType.put(type, result);
        return result;
    }
}