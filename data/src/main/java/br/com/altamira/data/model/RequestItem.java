/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "REQUEST_ITEM", uniqueConstraints = @UniqueConstraint(name = "UK1", columnNames = {"REQUEST", "MATERIAL", "ARRIVAL_DATE"}))
@NamedQueries({
    @NamedQuery(name = "RequestItem.list", query = "SELECT r FROM RequestItem r JOIN FETCH r.material JOIN r.request rq WHERE rq.id = :requestId"),
    @NamedQuery(name = "RequestItem.findById", query = "SELECT r FROM RequestItem r WHERE r.id = :id")})
public class RequestItem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "RequestItemSequence", sequenceName = "REQUEST_ITEM_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RequestItemSequence")
    @Column(name = "ID")
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "ARRIVAL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrival;
    
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private BigDecimal weight;

    @JsonIgnore
	@JoinColumn(name = "REQUEST", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Request request;
    
	@JsonSerialize(using = NullCollectionSerializer.class)
    @JoinColumn(name = "MATERIAL", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Material material;
    
    /*@OneToMany(cascade = CascadeType.ALL,mappedBy = "requestItem", fetch = FetchType.LAZY)
    private Set<PurchasePlanningItem> purchasePlanningItem;*/

    public RequestItem() {
    }

    public RequestItem(Date arrival, BigDecimal weight) {
        this.arrival = arrival;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }
    
    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
	
    @JsonIgnore
    @XmlTransient
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    /*@XmlTransient
    public Set<PurchasePlanningItem> getPurchasePlanningItem() {
        return purchasePlanningItem;
    }

    public void setPurchasePlanningItem(
            Set<PurchasePlanningItem> purchasePlanningItem) {
        this.purchasePlanningItem = purchasePlanningItem;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof RequestItem)) {
            return false;
        }
        RequestItem other = (RequestItem) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.RequestItem[ id=" + id + " ]";
    }

}
