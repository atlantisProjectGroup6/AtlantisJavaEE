/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.dao.CrudInterface;
import com.atlantis.domain.Device;
import com.atlantis.domain.Metric;
import com.atlantis.domain.User;
import java.util.List;
import javax.ejb.Remote;
import javax.inject.Inject;

/**
 *
 * @author charles
 */
@Remote
public interface MobileServiceEndpointRemote {

    Boolean createRawData(String MACAddress, String deviceName, Integer timestamp, String value, Integer typeId);
    
    List<Device> getUserDevices(String userId);
    
    Boolean createAccount(String userId);
    
    List<Metric> getLatestMetrics(String MACAddress, int timestamp);
    
    User getUser(Integer id);
    
    List<User> getAllUsers();
}
