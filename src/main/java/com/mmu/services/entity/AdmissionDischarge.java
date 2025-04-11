/*package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


*//**
 * The persistent class for the ADMISSION_DISCHARGE database table.
 * 
 *//*
@Entity
@Table(name="ADMISSION_DISCHARGE")
@NamedQuery(name="AdmissionDischarge.findAll", query="SELECT a FROM AdmissionDischarge a")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="ADMISSION_DISCHARGE_SEQ", sequenceName="ADMISSION_DISCHARGE_SEQ", allocationSize=1)
public class AdmissionDischarge implements Serializable {

	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -8849083968646009506L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="ADMISSION_DISCHARGE_SEQ")
	@Column(name="ADMISSION_ID")
	private long admissionId;

	@Column(name="ADMISSION_NO")
	private String admissionNo;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_ADMISSION")
	private Date dateOfAdmission;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_DISCHARGE")
	private Date dateOfDischarge;

	@Column(name="NO_OF_DAYS")
	private BigDecimal noOfDays;

	@Column(name="ADMISSION_REMARKS")
	private String admissionRemarks;
	
	@Column(name="DISCHARGE_REMARKS")
	private String dischargeRemarks;

	@Column(name="WARD_NO")
	private String wardNo;

	//bi-directional many-to-one association to MasDisposal
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DISPOSAL_ID")
	private MasDisposal masDisposal;

	//bi-directional many-to-one association to Patient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID")
	private Patient patient;

	//bi-directional many-to-one association to ReferralPatientHd
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFERRAL_PATIENT_HD_ID")
	private ReferralPatientHd referralPatientHd;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	public AdmissionDischarge() {
	}

	public long getAdmissionId() {
		return this.admissionId;
	}

	public void setAdmissionId(long admissionId) {
		this.admissionId = admissionId;
	}

	public String getAdmissionNo() {
		return this.admissionNo;
	}

	public void setAdmissionNo(String admissionNo) {
		this.admissionNo = admissionNo;
	}

	public Date getDateOfAdmission() {
		return this.dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public Date getDateOfDischarge() {
		return this.dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public BigDecimal getNoOfDays() {
		return this.noOfDays;
	}

	public void setNoOfDays(BigDecimal noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getAdmissionRemarks() {
		return admissionRemarks;
	}

	public void setAdmissionRemarks(String admissionRemarks) {
		this.admissionRemarks = admissionRemarks;
	}

	public String getDischargeRemarks() {
		return dischargeRemarks;
	}

	public void setDischargeRemarks(String dischargeRemarks) {
		this.dischargeRemarks = dischargeRemarks;
	}

	public String getWardNo() {
		return this.wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

	public MasDisposal getMasDisposal() {
		return this.masDisposal;
	}

	public void setMasDisposal(MasDisposal masDisposal) {
		this.masDisposal = masDisposal;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public ReferralPatientHd getReferralPatientHd() {
		return this.referralPatientHd;
	}

	public void setReferralPatientHd(ReferralPatientHd referralPatientHd) {
		this.referralPatientHd = referralPatientHd;
	}

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}
	
}*/