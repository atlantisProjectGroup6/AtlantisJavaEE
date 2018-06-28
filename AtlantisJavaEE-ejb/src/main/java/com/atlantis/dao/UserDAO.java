/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.dao;

import com.atlantis.domain.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author charles
 */
@Stateless
public class UserDAO {
    @PersistenceContext(unitName="atlantisPU")
    private EntityManager em;
    
    public User createOrUpdate(User user){
        return em.merge(user);
    }
    
    public User find(Long id) {
        User user = em.find(User.class, id);
        return user;
    }
}
