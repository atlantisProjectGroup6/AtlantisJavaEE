/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.Device;
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
    public Response addMetric(String content) {
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
        
        Boolean isValid = mobileService.createRawData(MACAddress, deviceName, timestamp, value, typeId);
        
        Response resp = null;
        if(isValid) {
            resp = Response.accepted().build();
        } else {
            resp = Response.status(400).entity("n° CB invalide").build();
        }
        return resp;
        
        
    }
    
    @Path("user/{userId}/devices")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserDevices (@PathParam("userId") String userId) {
        List<Device> userDevices = mobileService.getUserDevices(userId);
        
        if(userDevices == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<Device>> genericList = new GenericEntity<List<Device>>(userDevices){};
        
        return Response.ok(genericList).build();
    }
    
    @Path("addUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String content) {
        StringReader reader = new StringReader(content);
        String userId;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject userInfo = jreader.readObject();
            userId = userInfo.getString("userId");
        }
        
        Boolean isValid = mobileService.createAccount(userId);
        
        Response resp = null;
        if(isValid) {
            resp = Response.accepted().build();
        } else {
            resp = Response.status(400).entity("User already exist.").build();
        }
        return resp;
    }
    
    @Path("user/{userId}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser (@PathParam("userId") Integer userId) {
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
