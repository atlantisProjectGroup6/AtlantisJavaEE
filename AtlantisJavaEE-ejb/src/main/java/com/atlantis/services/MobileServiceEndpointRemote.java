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
    
    User getUser(Long id);
    
    List<User> getAllUsers();
}
