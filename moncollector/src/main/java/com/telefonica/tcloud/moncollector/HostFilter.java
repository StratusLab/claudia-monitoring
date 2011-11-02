/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.claudia.collectdplugin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 *
 * @author jomar
 */
public class HostFilter {
   
                    
    private boolean whiteList=false;
    private HashSet<String> filter;
    
    public HostFilter(String file) throws FileNotFoundException, IOException {
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
    boolean accept(String host) {
       if (whiteList) 
           return filter.contains(host);
       else
           return !filter.contains(host);
    }
}
