/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author miren
 */
@Entity
@Table(name="devices")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //attributes
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlElement
    @Column(name="ID_Device")
    private Integer id;

    @Column(name="Mac")
    @XmlElement
    private String mac;
    
    @Column(name="TypeID_Type")
    @XmlElement
    private Integer typeID;
    
    @Column(name="Name")
    @XmlElement
    private String name;
    
    //Relations
    @ManyToOne
    @JoinColumn (name="ID_Type")
    private Type type;
    
    @OneToMany(mappedBy="device")
    private List<Metric> metrics = new ArrayList<>();
    
    @ManyToMany
    private List<User> users = new ArrayList<>();
    
    //getters and setters
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //methods
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Device)) {
            return false;
        }
        Device other = (Device) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.atlantis.domain.Device[ id=" + id + " ]";
    }
    
}
