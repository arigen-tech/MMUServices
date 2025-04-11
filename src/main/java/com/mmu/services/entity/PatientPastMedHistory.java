package com.mmu.services.entity;

import java.io.Serializable;
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


/**
 * The persistent class for the PATIENT_PAST_MED_HISTORY database table.
 * 
 */
@Entity
@Table(name="PATIENT_PAST_MED_HISTORY")
@NamedQuery(name="PatientPastMedHistory.findAll", query="SELECT p FROM PatientPastMedHistory p")
public class PatientPastMedHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PATIENT_PAST_MED_HISTORY_PATIENTPASTMEDHISTORYID_GENERATOR", sequenceName="PATIENT_PAST_MED_HISTORY_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_PAST_MED_HISTORY_PATIENTPASTMEDHISTORYID_GENERATOR")
	@Column(name="PATIENT_PAST_MED_HISTORY_ID")
	private Long patientPastMedHistoryId;

	private String age;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_EXAM")
	private Date dateOfExam;

	@Column(name="EXISTING_MEDICAL_CAT")
	private Long existingMedicalCat;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;
	
	
	@Column(name="PAST_MEDICAL_HISTORY_ID")
	private Long pastMedicalHistoryId;

	@Column(name="PATIENT_HISTORY")
	private String patientHistory;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="PLACE_OF_EXAM")
	private String placeOfExam;

	@Column(name="VISIT_ID")
	private Long visitId;

	public PatientPastMedHistory() {
	}

	public long getPatientPastMedHistoryId() {
		return this.patientPastMedHistoryId;
	}

	public void setPatientPastMedHistoryId(long patientPastMedHistoryId) {
		this.patientPastMedHistoryId = patientPastMedHistoryId;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Date getDateOfExam() {
		return this.dateOfExam;
	}

	public void setDateOfExam(Date dateOfExam) {
		this.dateOfExam = dateOfExam;
	}

	public Long getExistingMedicalCat() {
		return this.existingMedicalCat;
	}

	public void setExistingMedicalCat(Long existingMedicalCat) {
		this.existingMedicalCat = existingMedicalCat;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}


	public Long getPastMedicalHistoryId() {
		return this.pastMedicalHistoryId;
	}

	public void setPastMedicalHistoryId(Long pastMedicalHistoryId) {
		this.pastMedicalHistoryId = pastMedicalHistoryId;
	}

	public String getPatientHistory() {
		return this.patientHistory;
	}

	public void setPatientHistory(String patientHistory) {
		this.patientHistory = patientHistory;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPlaceOfExam() {
		return this.placeOfExam;
	}

	public void setPlaceOfExam(String placeOfExam) {
		this.placeOfExam = placeOfExam;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public void setPatientPastMedHistoryId(Long patientPastMedHistoryId) {
		this.patientPastMedHistoryId = patientPastMedHistoryId;
	}

}