/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.dao;

import java.util.List;

/**
 *
 * @author charles
 */
public interface CrudInterface {
   public <T> T create (T t);
    public <T> T find(Class<T> type, Integer id);
    public <T> T findStringId(Class<T> type, String id);
    public <T> T update(T t);
    public void delete(Object t);
    public <T> List<T> findAll(Class<T> type); 
}
