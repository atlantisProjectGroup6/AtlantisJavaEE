/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.Device;
import com.atlantis.domain.User;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author charles
 */
@Path("backoffice")
public class BackofficeResource {
    
    @EJB
    private BackofficeServiceEndpointRemote backofficeService;

    public BackofficeResource() {
    }
    
    @Path("device/devices")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllDevices () {
        List<Device> allDevices = backofficeService.getAllDevices();
        
        if(allDevices == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<Device>> genericList = new GenericEntity<List<Device>>(allDevices){};
        
        return Response.ok(genericList).build();
    }
    
    @Path("user/users")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllUsers () {
        List<User> allUsers = backofficeService.getAllUsers();
        
        if(allUsers == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<User>> genericList = new GenericEntity<List<User>>(allUsers){};
        
        return Response.ok(genericList).build();
    }
    
    @Path("addAssociation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAssociation(String content) {
        StringReader reader = new StringReader(content);
        String deviceMac;
        String userId;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject associationInfo = jreader.readObject();
            deviceMac = associationInfo.getString("deviceMac");
            userId = associationInfo.getString("userId");
        }
        
        Boolean isValid = backofficeService.createAssociation(deviceMac, userId);
        
        Response resp = null;
        if(isValid) {
            resp = Response.accepted().build();
        } else {
            resp = Response.status(400).entity("Association already exist.").build();
        }
        return resp;
    }
}
