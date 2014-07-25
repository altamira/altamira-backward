/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "MATERIAL",
        uniqueConstraints = @UniqueConstraint(columnNames = {"LAMINATION", "TREATMENT", "THICKNESS", "WIDTH", "LENGTH"}))
@NamedQueries({
    @NamedQuery(name = "Material.list", query = "SELECT m FROM Material m"),
    @NamedQuery(name = "Material.findById", query = "SELECT m FROM Material m WHERE m.id = :id"),
    @NamedQuery(name = "Material.findByLamination", query = "SELECT m FROM Material m WHERE m.lamination = :lamination"),
    @NamedQuery(name = "Material.findByTreatment", query = "SELECT m FROM Material m WHERE m.treatment = :treatment"),
    @NamedQuery(name = "Material.findByThickness", query = "SELECT m FROM Material m WHERE m.thickness = :thickness"),
    @NamedQuery(name = "Material.findByWidth", query = "SELECT m FROM Material m WHERE m.width = :width"),
    @NamedQuery(name = "Material.findByLength", query = "SELECT m FROM Material m WHERE m.length = :length"),
    @NamedQuery(name = "Material.findByTax", query = "SELECT m FROM Material m WHERE m.tax = :tax"),
    @NamedQuery(name = "Material.findUnique", query = "SELECT m FROM Material m WHERE m.lamination = :lamination AND m.treatment = :treatment AND m.thickness = :thickness AND m.width = :width AND m.length = :length")})
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "MaterialSequence", sequenceName = "MATERIAL_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MaterialSequence")
    @Column(name = "ID")
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "LAMINATION", columnDefinition = "char(2)")
    private String lamination;
    
    @Basic(optional = false)
    @Column(name = "TREATMENT", columnDefinition = "char(2)")
    private String treatment;
    
    @Basic(optional = false)
    @Column(name = "THICKNESS")
    private BigDecimal thickness;
    
    @Basic(optional = false)
    @Column(name = "WIDTH")
    private BigDecimal width;
    
    @Basic(optional = false)
    @Column(name = "LENGTH")
    private BigDecimal length;
    
    @Column(name = "TAX")
    private BigDecimal tax;
    
    /*@JoinColumn(name = "COMPANY", referencedColumnName = "ID", columnDefinition = "number default 1")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Company company;*/

//    @OneToMany(/*cascade = CascadeType.ALL,*/mappedBy = "material", fetch = FetchType.LAZY)
//    private Set<MaterialStandard> materialStandardSet;
//    @OneToMany(/*cascade = CascadeType.ALL,*/mappedBy = "material", fetch = FetchType.LAZY)
//    private Set<SupplierPriceList> supplierPriceListSet;
//    @OneToMany(/*cascade = CascadeType.ALL,*/mappedBy = "material", fetch = FetchType.LAZY)
//    private Set<RequestItem> requestItemSet;

    public Material() {
    }

    public Material(String lamination, String treatment,
            BigDecimal thickness, BigDecimal width, BigDecimal length) {
        this.lamination = lamination;
        this.treatment = treatment;
        this.thickness = thickness;
        this.width = width;
        this.length = length;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }

    public String getLamination() {
        return lamination;
    }

    public void setLamination(String lamination) {
        this.lamination = lamination;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public BigDecimal getThickness() {
        return thickness;
    }

    public void setThickness(BigDecimal thickness) {
        this.thickness = thickness;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    //@XmlTransient
    //@JsonIgnore
    /*public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }*/
    
    /*@XmlTransient
    public Set<MaterialStandard> getMaterialStandardSet() {
        return materialStandardSet;
    }

    public void setMaterialStandardSet(Set<MaterialStandard> materialStandardSet) {
        this.materialStandardSet = materialStandardSet;
    }

    @XmlTransient
    public Set<SupplierPriceList> getSupplierPriceListSet() {
        return supplierPriceListSet;
    }

    public void setSupplierPriceListSet(
            Set<SupplierPriceList> supplierPriceListSet) {
        this.supplierPriceListSet = supplierPriceListSet;
    }

    @XmlTransient
    public Set<RequestItem> getRequestItemSet() {
        return requestItemSet;
    }

    public void setRequestItemSet(Set<RequestItem> requestItemSet) {
        this.requestItemSet = requestItemSet;
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
        if (!(object instanceof Material)) {
            return false;
        }
        Material other = (Material) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Material[ id=" + id + " ]";
    }

}
