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
    
    @Path("user/{userId}/devices")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserDevices (@PathParam("userId") String userId) {
        List<Device> userDevices = backofficeService.getUserDevices(userId);
        
        if(userDevices == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<Device>> genericList = new GenericEntity<List<Device>>(userDevices){};
        
        return Response.ok(genericList).build();
    }
    
    @Path("device/{devicesMac}/users")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDeviceUsers (@PathParam("devicesMac") String devicesMac) {
        List<User> deviceUsers = backofficeService.getDeviceUsers(devicesMac);
        
        if(deviceUsers == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<User>> genericList = new GenericEntity<List<User>>(deviceUsers){};
        
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
    
    @Path("admin/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adminLogin(String content) {
                
        StringReader reader = new StringReader(content);
        String adminLogin;
        String adminPassword;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject associationInfo = jreader.readObject();
            adminLogin = associationInfo.getString("adminLogin");
            adminPassword = associationInfo.getString("adminPassword");
        }
        
        Boolean isValid = backofficeService.loginAdmin(adminLogin, adminPassword);
        
        
        Response resp = null;
        if(isValid) {
            resp = Response.accepted().build();
        } else {
            resp = Response.status(400).entity("Association already exist.").build();
        }
        return resp;
    }
    
    @Path("user/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(String content) {
        StringReader reader = new StringReader(content);
        String userId;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject associationInfo = jreader.readObject();
            userId = associationInfo.getString("userId");
        }
        
        Boolean isValid = backofficeService.deleteUser(userId);
        
        Response resp = null;
        if(isValid) {
            resp = Response.accepted().build();
        } else {
            resp = Response.status(400).entity("User doesn't exist.").build();
        }
        return resp;
    }
    
    @Path("device/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteDevice(String content) {
        StringReader reader = new StringReader(content);
        String deviceMac;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject associationInfo = jreader.readObject();
            deviceMac = associationInfo.getString("deviceMac");
        }
        
        Boolean isValid = backofficeService.deleteDevice(deviceMac);
        
        Response resp = null;
        if(isValid) {
            resp = Response.accepted().build();
        } else {
            resp = Response.status(400).entity("Device doesn't exist.").build();
        }
        return resp;
    }
}
