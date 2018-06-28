/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.dao.UserDAO;
import com.atlantis.domain.User;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;

/**
 *
 * @author charles
 */
@Stateless
public class MobileServiceBean implements MobileServiceEndpointRemote {

    @Inject
    private UserDAO userDAO;
    
    @Override
    public User createUser() {
        User user = new User();
        user.setId(21L);
        user.setName("Jean Teston");
        return userDAO.createOrUpdate(user);
    }

    @Override
    public User getUser(Long id) {
        User user = userDAO.find(id);
        return user;
    }
}
