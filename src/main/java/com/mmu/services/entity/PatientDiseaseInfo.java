package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the PATIENT_DISEASE_INFO database table.
 * 
 */
@Entity
@Table(name="PATIENT_DISEASE_INFO")
@NamedQuery(name="PatientDiseaseInfo.findAll", query="SELECT p FROM PatientDiseaseInfo p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_DISEASE_INFO_SEQ", sequenceName="PATIENT_DISEASE_INFO_SEQ",allocationSize=1)

public class PatientDiseaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	 
//	@SequenceGenerator(name="PATIENT_DISEASE_INFO_DISEASEINFO_ID_GENERATOR", sequenceName="PATIENT_DISEASE_INFO_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_DISEASE_INFO_SEQ")

	@Column(name="DISEASE_INFO_ID")
	private Long diseaseInfoId;

	@Column(name="BEFORE_FLAG")
	private String beforeFlag;

	@Temporal(TemporalType.DATE)
	@Column(name="FROM_DATE")
	private Date fromDate;

	@Column(name="ICD_ID")
	private Long icdId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PATIENT_ID")
	private Long patientId;

	private String remarks;

	@Temporal(TemporalType.DATE)
	@Column(name="ST_DATE")
	private Date stDate;

	@Column(name="ST_PLACE")
	private String stPlace;

	@Temporal(TemporalType.DATE)
	@Column(name="TO_DATE")
	private Date toDate;

	@Column(name="TREATED_PLACE")
	private String treatedPlace;

	public PatientDiseaseInfo() {
	}

 

	public String getBeforeFlag() {
		return this.beforeFlag;
	}

	public void setBeforeFlag(String beforeFlag) {
		this.beforeFlag = beforeFlag;
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

	 

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getStDate() {
		return this.stDate;
	}

	public void setStDate(Date stDate) {
		this.stDate = stDate;
	}

	public String getStPlace() {
		return this.stPlace;
	}

	public void setStPlace(String stPlace) {
		this.stPlace = stPlace;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getTreatedPlace() {
		return this.treatedPlace;
	}

	public void setTreatedPlace(String treatedPlace) {
		this.treatedPlace = treatedPlace;
	}

	public Long getIcdId() {
		return icdId;
	}

	public void setIcdId(Long icdId) {
		this.icdId = icdId;
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



	public Long getDiseaseInfoId() {
		return diseaseInfoId;
	}



	public void setDiseaseInfoId(Long diseaseInfoId) {
		this.diseaseInfoId = diseaseInfoId;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="ICD_ID",nullable=false,insertable=false,updatable=false)
	private MasIcd masIcd;

	public MasIcd getMasIcd() {
		return masIcd;
	}



	public void setMasIcd(MasIcd masIcd) {
		this.masIcd = masIcd;
	}
	
	

}