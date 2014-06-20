/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "REQUEST")
@NamedQueries({
    @NamedQuery(name = "Request.findAll", query = "SELECT r FROM Request r"),
    @NamedQuery(name = "Request.findById", query = "SELECT r FROM Request r WHERE r.id = :id"),
    @NamedQuery(name = "Request.findByCreated", query = "SELECT r FROM Request r WHERE r.created = :created"),
    @NamedQuery(name = "Request.findByCreator", query = "SELECT r FROM Request r WHERE r.creator = :creator"),
    @NamedQuery(name = "Request.findBySent", query = "SELECT r FROM Request r WHERE r.sent = :sent"),
	@NamedQuery(name = "Request.getCurrent", query = "SELECT r FROM Request r WHERE r.id = (SELECT MAX(rr.id) FROM Request rr WHERE rr.sent IS NULL)")})
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "RequestSequence", sequenceName = "REQUEST_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RequestSequence")
    @Column(name = "ID")
    private Long id;
    
    @NotNull
    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @NotNull
    @Size(min = 3, max = 50)
    @Basic(optional = false)
    @Column(name = "CREATOR", columnDefinition = "nvarchar2(255)")
    private String creator;
    
    @Column(name = "SENT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="REQUEST")
    private Set<RequestItem> items;
    
    /*@ManyToOne(optional = true, fetch = FetchType.LAZY)
    private QuotationRequest quotationRequest;*/

    public Request() {
    }

    /*public Request(Long id) {
        this.id = id;
    }*/

    public Request(Long id, Date created, String creator) {
        this.id = id;
        this.created = created;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    /*public void setId(Long id) {
        this.id = id;
    }*/

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Set<RequestItem> getItems() {
        return items;
    }

    public void setItems(Set<RequestItem> items) {
        this.items = items;
    }

    /*@XmlTransient
    public QuotationRequest getQuotationRequest() {
        return quotationRequest;
    }

    public void setQuotationRequest(QuotationRequest quotationRequest) {
        this.quotationRequest = quotationRequest;
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
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Request[ id=" + id + " ]";
    }

}
