/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.User;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author charles
 */
@Remote
public interface MobileServiceEndpointRemote {
    
    void createRawData(String MACAddress, String deviceName, Integer timestamp, String value, Integer typeId);
    
    Boolean createAccount(String userLogin, String password);
    
    User getUser(Integer id);
    
    List<User> getAllUsers();
}
