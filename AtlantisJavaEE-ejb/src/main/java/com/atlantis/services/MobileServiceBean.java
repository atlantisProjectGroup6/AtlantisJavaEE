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
    public User getUser(Integer id) {
        
//        Device myDevice = dao.findStringId(Device.class, "SLDLSDL54");
//        Metric myMetric = new Metric();
//        
//        myMetric.setDate(55555555);
//        myMetric.setValue("25");
//        
//        myMetric.setDevice(myDevice);
//        
//        dao.update(myMetric);
        
//        Device myDevice = dao.findStringId(Device.class, "SLDLSDL54");
//        Type temperature = dao.find(Type.class, 2);
//        
//        myDevice.setType(temperature);
//        
//        dao.update(myDevice);
        
//        User myUser = dao.find(User.class, 1);
//        Device myDevice = dao.findStringId(Device.class, "SLDLSDL54");
//        
//        List<Device> userDevices = myUser.getDevices();
//        if(!userDevices.contains(myDevice)){
//            userDevices.add(myDevice);
//            myUser.setDevices(userDevices);
//        }
        
        return dao.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return dao.findAll(User.class);
    }   
}
