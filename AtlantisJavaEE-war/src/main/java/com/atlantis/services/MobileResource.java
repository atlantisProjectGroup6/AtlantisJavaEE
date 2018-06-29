/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.User;
import java.io.StringReader;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
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
    
    @Path("addMetric")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMetric(String content) {
        StringReader reader = new StringReader(content);
        
        String MACAddress;
        String deviceName;
        Integer timestamp;
        String value;
        Integer typeId;
        
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject metricInfo = jreader.readObject();
            MACAddress = metricInfo.getString("mac");
            deviceName = metricInfo.getString("name");
            timestamp = metricInfo.getJsonNumber("timestamp").intValue();
            value = metricInfo.getString("value");
            typeId = metricInfo.getJsonNumber("type").intValue();
        }
        
        mobileService.createRawData(MACAddress, deviceName, timestamp, value, typeId);
    }
    
    @Path("user/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser (@PathParam("id") Integer userId) {
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
    
    @Path("user/createUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pay(String content) {
        StringReader reader = new StringReader(content);
        String userLogin;
        String password;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject userInfo = jreader.readObject();
            userLogin = userInfo.getString("userLogin");
            password = userInfo.getString("password");
        }
        
        Boolean isValid = mobileService.createAccount(userLogin, password);
        
        Response resp = null;
        if(isValid) {
            resp = Response.accepted().build();
        } else {
            resp = Response.status(400).entity("Non valide").build();
        }
        return resp;
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
