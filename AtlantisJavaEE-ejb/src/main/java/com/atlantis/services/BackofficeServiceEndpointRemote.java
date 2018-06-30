/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.Device;
import com.atlantis.domain.User;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author charles
 */
@Remote
public interface BackofficeServiceEndpointRemote {
    
    List<Device> getAllDevices();
    
    List<User> getAllUsers();
    
    Boolean createAssociation(String deviceMAC, String idUser);
    
    Boolean loginAdmin(String adminLogin, String adminPassword);
}
