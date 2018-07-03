/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.Device;
import com.atlantis.domain.Metric;
import com.atlantis.domain.User;
import com.atlantis.helper.CalculatedMetrics;
import com.atlantis.helper.DeviceMacWrapper;
import com.atlantis.helper.ResponseWrapper;
import java.io.StringReader;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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
            resp = Response.accepted().build();
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String content) {
        StringReader reader = new StringReader(content);
        String userId;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject userInfo = jreader.readObject();
            userId = userInfo.getString("userId");
        }
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setIsValid(mobileService.createAccount(userId));
        
        return Response.ok(responseWrapper).build();
    }
    
    @Path("addUserName")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserName(String content) {
        StringReader reader = new StringReader(content);
        String userId;
        String name;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject userInfo = jreader.readObject();
            userId = userInfo.getString("userId");
            name = userInfo.getString("name");
        }
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setIsValid(mobileService.updateAccount(userId, name));
        
        return Response.ok(responseWrapper).build();
    }
    
    @Path("latestMetrics")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestMetrics(String content) {
        StringReader reader = new StringReader(content);
        String deviceMac;
        int timestamp;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonArray array = jreader.readArray();
            JsonObject userInfo = array.getJsonObject(0);
            deviceMac = userInfo.getString("deviceMac");
            timestamp = userInfo.getJsonNumber("timestamp").intValue();
        }
        
        List<Metric> latestMetrics = mobileService.getLatestMetrics(deviceMac, timestamp);
        
        if(latestMetrics == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<Metric>> genericList = new GenericEntity<List<Metric>>(latestMetrics){};
        
        return Response.ok(genericList).build();
    }
    
    @Path("user/{userId}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser (@PathParam("userId") String userId) {
        User user = mobileService.getUser(userId);
        
        if(user == null){
            throw new NotFoundException();
        }
        
        return Response.ok(user).build();
    }
    
    @Path("device/{deviceMac}/allMetrics")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllMetrics (@PathParam("deviceMac") String deviceMac) {
        List<Metric> allMetrics = mobileService.getAllMetrics(deviceMac);
        
        if(allMetrics == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<Metric>> genericList = new GenericEntity<List<Metric>>(allMetrics){};
        
        return Response.ok(genericList).build();
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
    
    @Path("device/sendCommand")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendCommand(String content) {
        StringReader reader = new StringReader(content);
        String deviceMac;
        String command;
        try(JsonReader jreader = Json.createReader(reader)) {
            JsonObject userInfo = jreader.readObject();
            deviceMac = userInfo.getString("deviceMac");
            command = userInfo.getString("command");
        }
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setIsValid(mobileService.sendCommand(deviceMac, command));
        
        return Response.ok(responseWrapper).build();
    }
    
    @Path("device/{deviceMac}/getCommand")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCommand (@PathParam("deviceMac") String deviceMac) {
        Device device = mobileService.getCommand(deviceMac);
        
        if(device == null){
            throw new NotFoundException();
        }
        
        return Response.ok(device).build();
    }
    
    @Path("device/{deviceMac}/calculatedMetrics")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCalulatedMetrics (@PathParam("deviceMac") String deviceMac) {
        CalculatedMetrics calculatedMetrics = new CalculatedMetrics();
        DeviceMacWrapper deviceMacWrapper = new DeviceMacWrapper();
        
        deviceMacWrapper.setDeviceMac(deviceMac);
        
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://192.168.43.21:52282/CalculationEndpoint.svc/device/getAllCalculatedMetricsById")
                .request()
                .post(Entity.entity(deviceMacWrapper, MediaType.APPLICATION_JSON_TYPE));
        
        calculatedMetrics = response.readEntity(CalculatedMetrics.class);
        
        return Response.ok(calculatedMetrics).build();
    }
    
    @Path("device/{deviceMac}/{period}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDayMetrics (@PathParam("deviceMac") String deviceMac, @PathParam("period") String period) {
        List<Metric> allMetrics = mobileService.getMetricsByPeriod(deviceMac, period);
        
        if(allMetrics == null){
            throw new NotFoundException();
        }
        
        GenericEntity<List<Metric>> genericList = new GenericEntity<List<Metric>>(allMetrics){};
        
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
