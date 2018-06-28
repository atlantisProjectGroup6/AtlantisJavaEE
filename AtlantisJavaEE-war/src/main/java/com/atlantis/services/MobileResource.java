/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.User;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author charles
 */
@Path("mobile")
@RequestScoped
public class MobileResource {
    
    @EJB
    private MobileServiceEndpointRemote mobileService;

    public MobileResource() {
    }
    
    @Path("createTest")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String createUserTest() {
        User user = mobileService.createUser();
        String restMsg="{\"message\":\"hello REST\"}";
        return restMsg;
    }
    
    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser (@PathParam("id") Long userId) {
        User user = mobileService.getUser(userId);
        
        if(user == null){
            throw new NotFoundException();
        }
        
        return Response.ok(user).build();
    }

    /**
     * Retrieves representation of an instance of com.atlantis.services.MobileResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        String restMsg="{\"message\":\"hello REST\"}";
        return restMsg;
    }
}
