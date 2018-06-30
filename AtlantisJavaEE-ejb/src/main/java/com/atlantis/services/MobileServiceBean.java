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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

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
    public List<Metric> getLatestMetrics(String MACAddress, int timestamp) {
        List<Metric> metricsFromDate = new ArrayList<>();
        Device device = dao.findStringId(Device.class, MACAddress);
        if(device != null){
            metricsFromDate = device.getMetrics();
        }
        metricsFromDate.size(); // LAZY instantiation
        return metricsFromDate;
    }

    @Override
    public User getUser(Integer id) {        
        return dao.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return dao.findAll(User.class);
    }   
}
