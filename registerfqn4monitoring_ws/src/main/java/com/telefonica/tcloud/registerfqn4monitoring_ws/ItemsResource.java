/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telefonica.tcloud.registerfqn4monitoring_ws;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author jomar
 */
@Path("/maps2fqn")
public class ItemsResource {

    @Context
    private UriInfo context;

    /** Creates a new instance of ItemsResource */
    public ItemsResource() {
    }

    /**
     * Retrieves representation of an instance of com.telefonica.tcloud.registerfqn4monitoring_ws.ItemsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * Sub-resource locator method for {name}
     */
    @Path("{name}")
    public ItemResource getItemResource(@PathParam("name")
    String name) {
        return ItemResource.getInstance(name);
    }
}
