/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.domain.Device;
import com.atlantis.domain.Metric;
import com.atlantis.domain.User;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author charles
 */
@Remote
public interface MobileServiceEndpointRemote {

    Boolean createRawData(String MACAddress, String deviceName, Integer timestamp, String value, Integer typeId);
    
    List<Device> getUserDevices(String userId);
    
    Boolean createAccount(String userId);
    
    Boolean updateAccount(String userId, String name);
    
    List<Metric> getLatestMetrics(String MACAddress, int timestamp);
    
    List<Metric> getAllMetrics(String MACAddress);
    
    User getUser(String id);
    
    List<User> getAllUsers();
    
    Boolean sendCommand(String MACAddress, String command);
    
    Device getCommand(String MACAddress);
    
    List<Metric> getMetricsByPeriod(String MACAddress, String period);
}
