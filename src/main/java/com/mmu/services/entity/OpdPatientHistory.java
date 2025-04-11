/*package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


*//**
 * The persistent class for the OPD_PATIENT_HISTORY database table.
 * 
 *//*

@Entity
@Table(name="OPD_PATIENT_HISTORY")
@NamedQuery(name="OpdPatientHistory.findAll", query="SELECT o FROM OpdPatientHistory o")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OPD_PATIENT_HISTORY_SEQ", sequenceName="OPD_PATIENT_HISTORY_SEQ", allocationSize=1)
public class OpdPatientHistory implements Serializable {
	
	

	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -4613834530649085330L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OPD_PATIENT_HISTORY_SEQ")
	@Column(name="OPD_PATIENT_HISTORY_ID")
	private Long opdPatientHistoryId;

	@Column(name="ALLERGY_HISTORY")
	private String allergyHistory;

	@Column(name="CHIEF_COMPLAIN")
	private String chiefComplain;

	@Column(name="FAMILY_HISTORY")
	private String familyHistory;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="IMPLANT_HISTORY")
	private String implantHistory;

	@Column(name="INPATIENT_ID")
	private Long inpatientId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MEDICATION_HISTORY")
	private String medicationHistory;

	@Column(name="OPD_PATIENT_DETAILS_ID")
	private Long opdPatientDetailsId;

	@Column(name="PAST_MEDICAL_HISTORY")
	private String pastMedicalHistory;

	@Column(name="PAST_SURGICAL_HISTORY")
	private String pastSurgicalHistory;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="PERSONAL_HISTORY")
	private String personalHistory;

	@Column(name="PRESENT_ILLNESS_HISTORY")
	private String presentIllnessHistory;

	@Column(name="SOCIAL_HISTORY")
	private String socialHistory;

	private String status;

	@Column(name="VISIT_ID")
	private Long visitId;

	@Column(name="VISIT_INPATIENT_ID")
	private Long visitInpatientId;
	
	// bi-directional many-to-one association to Visit
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "VISIT_ID", nullable = false, insertable = false, updatable = false)
	private Visit visit;
	
	public Long getOpdPatientHistoryId() {
		return opdPatientHistoryId;
	}

	public void setOpdPatientHistoryId(Long opdPatientHistoryId) {
		this.opdPatientHistoryId = opdPatientHistoryId;
	}

	public String getAllergyHistory() {
		return allergyHistory;
	}

	public void setAllergyHistory(String allergyHistory) {
		this.allergyHistory = allergyHistory;
	}

	public String getChiefComplain() {
		return chiefComplain;
	}

	public void setChiefComplain(String chiefComplain) {
		this.chiefComplain = chiefComplain;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getImplantHistory() {
		return implantHistory;
	}

	public void setImplantHistory(String implantHistory) {
		this.implantHistory = implantHistory;
	}

	public Long getInpatientId() {
		return inpatientId;
	}

	public void setInpatientId(Long inpatientId) {
		this.inpatientId = inpatientId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMedicationHistory() {
		return medicationHistory;
	}

	public void setMedicationHistory(String medicationHistory) {
		this.medicationHistory = medicationHistory;
	}

	public Long getOpdPatientDetailsId() {
		return opdPatientDetailsId;
	}

	public void setOpdPatientDetailsId(Long opdPatientDetailsId) {
		this.opdPatientDetailsId = opdPatientDetailsId;
	}

	public String getPastMedicalHistory() {
		return pastMedicalHistory;
	}

	public void setPastMedicalHistory(String pastMedicalHistory) {
		this.pastMedicalHistory = pastMedicalHistory;
	}

	public String getPastSurgicalHistory() {
		return pastSurgicalHistory;
	}

	public void setPastSurgicalHistory(String pastSurgicalHistory) {
		this.pastSurgicalHistory = pastSurgicalHistory;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPersonalHistory() {
		return personalHistory;
	}

	public void setPersonalHistory(String personalHistory) {
		this.personalHistory = personalHistory;
	}

	public String getPresentIllnessHistory() {
		return presentIllnessHistory;
	}

	public void setPresentIllnessHistory(String presentIllnessHistory) {
		this.presentIllnessHistory = presentIllnessHistory;
	}

	public String getSocialHistory() {
		return socialHistory;
	}

	public void setSocialHistory(String socialHistory) {
		this.socialHistory = socialHistory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Long getVisitInpatientId() {
		return visitInpatientId;
	}

	public void setVisitInpatientId(Long visitInpatientId) {
		this.visitInpatientId = visitInpatientId;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	
	

}*/