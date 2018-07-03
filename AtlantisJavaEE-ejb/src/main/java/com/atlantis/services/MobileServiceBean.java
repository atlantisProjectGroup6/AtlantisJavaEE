/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.dao.CrudInterface;
import com.atlantis.domain.Device;
import com.atlantis.domain.Metric;
import com.atlantis.domain.Type;
import com.atlantis.domain.User;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author charles
 */
@Stateless
public class MobileServiceBean implements MobileServiceEndpointRemote {

    @Inject
    private CrudInterface dao;
    
    @Override
    public Boolean createRawData(String MACAddress, String deviceName, Integer timestamp, String value, Integer typeId) {
        Device device = new Device();
        Metric metric = new Metric();
        
        if(dao.findStringId(Device.class, MACAddress) == null){
            
            Type deviceType = dao.find(Type.class, typeId);
            
            device.setMACAddress(MACAddress);
            device.setType(deviceType);
            device.setName(deviceName);
            dao.create(device);
            
        } else {
            device = dao.findStringId(Device.class, MACAddress);
        }
        
        metric.setDate(timestamp);
        metric.setValue(value);
        metric.setDevice(device);
        
        if(dao.create(metric) == null) {
            return false;
        }
        
        return true;
    }
    
    
    @Override
    public List<Device> getUserDevices(String userId) {
        User user = dao.findStringId(User.class, userId);
        
        List<Device> userDevices = user.getDevices(); 
        userDevices.size(); // LAZY instantiation
        
        return userDevices;
    }
    
    @Override
    public Boolean createAccount(String userId) {
        if(dao.findStringId(User.class, userId) == null) {
            User newUser = new User();
            newUser.setId(userId);

            dao.create(newUser);
            return true;
        }
        return false;
    }
    
    @Override
    public Boolean updateAccount(String userId, String name) {
        User user = dao.findStringId(User.class, userId);
        if(user != null){
            user.setName(name);
            dao.update(user);
            return true;
        }
        return false;
    }
    
    @Override
    public List<Metric> getLatestMetrics(String MACAddress, int timestamp) {
        List<Metric> latestMetrics = new ArrayList<>();
        int oneTimestampDay = 86400;
        Device device = dao.findStringId(Device.class, MACAddress);
        if(device != null){
            latestMetrics = device.getMetrics();
        }
        
        latestMetrics.size(); // LAZY instantiation
        return latestMetrics;
    }
    
    @Override
    public List<Metric> getAllMetrics(String MACAddress) {
        List<Metric> latestMetrics = new ArrayList<>();
        Device device = dao.findStringId(Device.class, MACAddress);
        if(device != null){
            latestMetrics = device.getMetrics();
        }
        
        latestMetrics.size(); // LAZY instantiation
        return latestMetrics;
    }

    @Override
    public User getUser(String id) {    
        return dao.findStringId(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return dao.findAll(User.class);
    }   

    @Override
    public Boolean sendCommand(String MACAddress, String command) {
        Device device = dao.findStringId(Device.class, MACAddress);
        
        if(device != null){
            device.setCommand(command);
            return true;
        }       
        return false;
    }

    @Override
    public Device getCommand(String MACAddress) {
        return dao.findStringId(Device.class, MACAddress);
    }

    @Override
    public List<Metric> getMetricsByPeriod(String MACAddress, String period) {
        
        Instant instant = Instant.now();
        Integer currentTimestamp = (int) instant.getEpochSecond();
        Integer timestampDay = 86400;
        Integer timestampPeriod = 0;
        
        List<Metric> metricsByPeriod = null; 
        
        if(period.equals("day")){
            timestampPeriod = currentTimestamp - timestampDay;
            metricsByPeriod = dao.findMetricsByPeriod(MACAddress, timestampPeriod);
        } else if (period.equals("week")) {
            timestampPeriod = currentTimestamp - timestampDay * 7;
            metricsByPeriod = dao.findMetricsByPeriod(MACAddress, timestampPeriod);
        } else if (period.equals("month")) {
            timestampPeriod = currentTimestamp - timestampDay * 30;
            metricsByPeriod = dao.findMetricsByPeriod(MACAddress, timestampPeriod);
        }
        
        return metricsByPeriod;
    }
}
