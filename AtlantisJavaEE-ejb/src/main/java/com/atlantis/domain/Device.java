/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlTransient;

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
    @Id
    @XmlElement
    @Column(name="MAC_Address")
    private String MACAddress;
    
    @Column(name="Name")
    @XmlElement
    private String name;
    
    //Relations
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TypeID_Type")
    @XmlElement
    private Type type;
    
    @ManyToMany(mappedBy="devices")
    @XmlTransient
    private List<User> users;
    
    @OneToMany(mappedBy="device")
    @XmlTransient
    private List<Metric> metrics;   

    
    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    //methods
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (MACAddress != null ? MACAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Device)) {
            return false;
        }
        Device other = (Device) object;
        if ((this.MACAddress == null && other.MACAddress != null) || (this.MACAddress != null && !this.MACAddress.equals(other.MACAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.atlantis.domain.Device[ id=" + MACAddress + " ]";
    }
    
}
