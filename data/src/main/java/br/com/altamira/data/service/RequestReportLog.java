/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.service;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PARTH
 */

@XmlRootElement
@Entity
@Table(name = "REQUEST_REPORT_LOG")
public class RequestReportLog {
    
    @Id
    @Column(name = "REPORT_INSTANCE")
    @Lob
    private byte[] reportInstance;
    
    @Column(name = "GENERATED_DATETIME", insertable = false)
    @Temporal(TemporalType.DATE)
    private Date dateTime;

    public byte[] getReportInstance() {
        return reportInstance;
    }

    public void setReportInstance(byte[] reportInstance) {
        this.reportInstance = reportInstance;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

}
