/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.dao.CrudInterface;
import com.atlantis.domain.Device;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
