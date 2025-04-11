package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the PATIENT_SERVICES_DETAILS database table.
 * 
 */
@Entity
@Table(name="PATIENT_SERVICES_DETAILS")
@NamedQuery(name="PatientServicesDetail.findAll", query="SELECT p FROM PatientServicesDetail p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_SERVICES_DETAILS_SEQ", sequenceName="PATIENT_SERVICES_DETAILS_SEQ",allocationSize=1)

public class PatientServicesDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SERVICE_DETAILS_ID")
	//@SequenceGenerator(name="PATIENT_SERVICES_DETAILS_SERVICEDETAILS_ID_GENERATOR", sequenceName="PATIENT_SERVICES_DETAILS_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_SERVICES_DETAILS_SEQ")

	private Long serviceDetailsId;

	@Temporal(TemporalType.DATE)
	@Column(name="FROM_DATE")
	private Date fromDate;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PATIENT_ID")
	private Long patientId;

	private String pf;

	private String place;

	@Temporal(TemporalType.DATE)
	@Column(name="TO_DATE")
	private Date toDate;

	public PatientServicesDetail() {
	}
 

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	 
	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	 

	public String getPf() {
		return this.pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}


	public Long getServiceDetailsId() {
		return serviceDetailsId;
	}


	public void setServiceDetailsId(Long serviceDetailsId) {
		this.serviceDetailsId = serviceDetailsId;
	}

	 

}