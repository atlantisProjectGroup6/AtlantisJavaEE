/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.services;

import com.atlantis.dao.CrudInterface;
import com.atlantis.domain.User;
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
    public User getUser(Long id) {
        return dao.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return dao.findAll(User.class);
    }
}
