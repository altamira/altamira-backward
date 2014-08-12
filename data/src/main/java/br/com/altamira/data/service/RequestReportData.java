/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author PARTH
 */
public class RequestReportData {
    
    private Long id;//0
    private String lamination;//1
    private String treatment;//2
    private BigDecimal thickness;//3
    private BigDecimal width;//4
    private BigDecimal length;//5
    private BigDecimal weight;//6
    private Date arrivalDate;//7

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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    
}
