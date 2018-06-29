/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="metrics")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Metric implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //attributes
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlElement
    @Column(name="ID_Metric")
    private Integer id;
    
    @Column(name="Value")
    @XmlElement
    private String value;

    @Column(name="Date")
    @XmlElement
    private Timestamp date;

    @Column(name="DeviceID_Device")
    @XmlElement
    private Integer deviceID;
    
    //Relations 
    @ManyToOne
    @JoinColumn (name="ID_Device")
    private Device device;
    
    //getters and setters
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {    
        this.deviceID = deviceID;
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
        if (!(object instanceof Metric)) {
            return false;
        }
        Metric other = (Metric) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.atlantis.domain.Metric[ id=" + id + " ]";
    }
    
}
