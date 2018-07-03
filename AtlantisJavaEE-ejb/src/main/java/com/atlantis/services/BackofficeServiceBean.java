/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.dao.CrudInterface;
import com.atlantis.domain.Admin;
import com.atlantis.domain.Device;
import com.atlantis.domain.Metric;
import com.atlantis.domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author charles
 */
@Stateless
public class BackofficeServiceBean implements BackofficeServiceEndpointRemote {
    
    @Inject
    private CrudInterface dao;
    
    @Override
    public List<Device> getAllDevices() {
        return dao.findAll(Device.class);
    }

    @Override
    public List<User> getAllUsers() {
        return dao.findAll(User.class);
    }
    
    @Override
    public List<Device> getUserDevices(String userId) {
        User user = dao.findStringId(User.class, userId);
        
        List<Device> userDevices = user.getDevices(); 
        userDevices.size(); // LAZY instantiation
        return userDevices;
    }

    @Override
    public List<User> getDeviceUsers(String deviceMAC) {
        Device device = dao.findStringId(Device.class, deviceMAC);
        
        List<User> deviceUsers = device.getUsers(); 
        deviceUsers.size(); // LAZY instantiation
        return deviceUsers;
    }

    @Override
    public Boolean createAssociation(String deviceMAC, String idUser) {
        Device device = dao.findStringId(Device.class, deviceMAC);
        User user = dao.findStringId(User.class, idUser);
        
        if (user != null && device != null){
            List<Device> userDevices = user.getDevices();
            if(!userDevices.contains(device)) {
                userDevices.add(device);
                dao.update(user);  
                return true;
            }
        }
        return false;                    
    }

    @Override
    public Boolean loginAdmin(String adminLogin, String adminPassword) {
        Admin admin = dao.findStringId(Admin.class, adminLogin);
        
        if(admin != null){
            if(adminPassword.equals(admin.getPassword())){
                return true;
            }
        }
        
        return false;
    }    

    @Override
    public Boolean deleteUser(String userId) {
        User user = dao.findStringId(User.class, userId);
        if(user != null){
            dao.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteDevice(String deviceMAC) {
        Device device = dao.findStringId(Device.class, deviceMAC);
        if(device != null){
            
//            List<Metric> deviceMetrics = device.getMetrics();
//            if(deviceMetrics != null){
//                deviceMetrics.forEach((metric) -> {
//                    dao.delete(metric);
//                });
//            }
            dao.delete(device);
            
            return true;
        }
        return false;
    }
}
