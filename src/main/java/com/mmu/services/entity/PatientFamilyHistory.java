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




/**
 * The persistent class for the PATIENT_FAMILY_HISTORY database table.
 * 
 */
@Entity
@Table(name="PATIENT_FAMILY_HISTORY")
@NamedQuery(name="PatientFamilyHistory.findAll", query="SELECT p FROM PatientFamilyHistory p")
@SequenceGenerator(name="PATIENT_FAMILY_HISTORY_SEQ", sequenceName="PATIENT_FAMILY_HISTORY_SEQ", allocationSize=1)
public class PatientFamilyHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7342670032682493837L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_FAMILY_HISTORY_SEQ")
	@Column(name="PATIENT_FAMILY_HISTORY_ID")
	private Long patientFamilyHistoryId;

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	@Column(name="PATIENT_FAMILY_HISTORY_CODE")
	private String patientFamilyHistoryCode;

	@Column(name="PATIENT_FAMILY_HISTORY_NAME")
	private String patientFamilyHistoryName;

	@Column(name="PATIENT_PRESENT_COMPLAINT_NAME")
	private String patientPresentComplaintName;

	@Column(name="PATIENT_TREATMENT_NAME")
	private String patientTreatmentName;

	private String status;

	@Column(name="TEMPLATE_CODE")
	private String templateCode;

	public Long getPatientFamilyHistoryId() {
		return patientFamilyHistoryId;
	}

	public void setPatientFamilyHistoryId(Long patientFamilyHistoryId) {
		this.patientFamilyHistoryId = patientFamilyHistoryId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getPatientFamilyHistoryCode() {
		return patientFamilyHistoryCode;
	}

	public void setPatientFamilyHistoryCode(String patientFamilyHistoryCode) {
		this.patientFamilyHistoryCode = patientFamilyHistoryCode;
	}

	public String getPatientFamilyHistoryName() {
		return patientFamilyHistoryName;
	}

	public void setPatientFamilyHistoryName(String patientFamilyHistoryName) {
		this.patientFamilyHistoryName = patientFamilyHistoryName;
	}

	public String getPatientPresentComplaintName() {
		return patientPresentComplaintName;
	}

	public void setPatientPresentComplaintName(String patientPresentComplaintName) {
		this.patientPresentComplaintName = patientPresentComplaintName;
	}

	public String getPatientTreatmentName() {
		return patientTreatmentName;
	}

	public void setPatientTreatmentName(String patientTreatmentName) {
		this.patientTreatmentName = patientTreatmentName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	
}