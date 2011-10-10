/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.registerfqn4monitoring_ws;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.WebApplicationException;

/**
 * REST Web Service
 *
 * @author jomar
 */
public class ItemResource {

    private PersistenceFacade persistence=new PersistenceFacade(); 
    private String name;

    /** Creates a new instance of ItemResource */
    private ItemResource(String name) {
        this.name = name;
    }

    /** Get instance of the ItemResource */
    public static ItemResource getInstance(String name) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of ItemResource class.
        return new ItemResource(name);
    }

    /**
     * Retrieves representation of an instance of com.telefonica.tcloud.registerfqn4monitoring_ws.ItemResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getText() {
      try {
         if (name.contains("_")) {
                String parts[]=name.split("_",2);
                return persistence.searchFQN(parts[0],parts[1]);
         } else 
            return persistence.searchFQN(name,null);
        } catch (SQLException ex) {
            Logger.getLogger(ItemResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException();
        }
    }

    /**
     * PUT method for updating or creating an instance of ItemResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/plain")
    public void putText(String content) {
        try {
            if (name.contains("_")) {
                String parts[]=name.split("_",2);
                persistence.insertFQNMap(content,parts[0],parts[1]);
            } else
                persistence.insertFQNMap(content, name, null);
            
        } catch (SQLException ex) {
            Logger.getLogger(ItemResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException();
        }
        
        
    }

    /**
     * DELETE method for resource ItemResource
     */
    @DELETE
    public void delete() {
        if (name.contains("_")) {
            String parts[]=name.split("_",2);
            persistence.deleteFQNMap(parts[0],parts[1]);
        } else
          persistence.deleteFQNMap(name, null);
    }
}
