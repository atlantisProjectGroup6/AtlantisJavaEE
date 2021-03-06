/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.dao;

import com.atlantis.domain.Metric;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author charles
 */
@Stateless
public class Crud implements CrudInterface {
    @PersistenceContext(unitName="atlantisPU")
    private EntityManager em;

    @Override
    public <T> T create(T t) {
        em.persist(t);
        return t;
    }  
    
    @Override
    public <T> T find(Class<T> type, Integer id) {
        return em.find(type, id);
    }
    
    @Override
    public <T> T findStringId(Class<T> type, String id) {
        return em.find(type, id);
    }
    
    @Override
    public <T> T update(T t) {
        t = em.merge(t);
        return t;
    }
    
    @Override
    public void delete(Object t) {
        t = em.merge(t);
        em.remove(t);
    }
    
    @Override
    public <T> List<T> findAll(Class<T> type){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> identificationVariable = cq.from(type);
        cq.select(identificationVariable);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Metric> findMetricsByPeriod(String MACAddress, Integer timestamp) {
        Query query  = em.createQuery("SELECT m FROM Metric AS m WHERE m.addressMAC = :MACAddress AND m.date > :timestamp ORDER BY m.date DESC", Metric.class);        
        return (List<Metric>)query.setParameter("MACAddress", MACAddress).setParameter("timestamp", timestamp).getResultList();
    }
}
