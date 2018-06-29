/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.User;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
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

    public MobileResource() {}
    
    @Path("user/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser (@PathParam("id") Long userId) {
        User user = mobileService.getUser(userId);
        
        if(user == null){
            throw new NotFoundException();
        }
        
        return Response.ok(user).build();
    }
    
    @Path("user/users")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllUsers () {
        List<User> allUsers = mobileService.getAllUsers();
        
        if(allUsers == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<User>> genericList = new GenericEntity<List<User>>(allUsers){};
        
        return Response.ok(genericList).build();
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
