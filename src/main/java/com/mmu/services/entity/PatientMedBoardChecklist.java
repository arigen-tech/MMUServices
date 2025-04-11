package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the PATIENT_MED_BOARD_CHECKLIST database table.
 * 
 */
@Entity
@Table(name="PATIENT_MED_BOARD_CHECKLIST")
@NamedQuery(name="PatientMedBoardChecklist.findAll", query="SELECT p FROM PatientMedBoardChecklist p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_MED_BOARD_CHECKLIST_CHECKLISTID_GENERATOR", sequenceName="CHECKLIST_SEQ",allocationSize=1)
public class PatientMedBoardChecklist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2737973630962959260L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_MED_BOARD_CHECKLIST_CHECKLISTID_GENERATOR")
	@Column(name="CHECKLIST_ID")
	private Long checklistId;

	@Column(name="CHECKLIST_HD_CODE")
	private String checklistHdCode;

	@Column(name="CHECKLIST_NAME")
	private String checklistName;

	@Column(name="CHECKLIST_VALUE")
	private String checklistValue;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="VISIT_ID")
	private Long visitId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	public PatientMedBoardChecklist() {
	}

	public Long getChecklistId() {
		return this.checklistId;
	}

	public void setChecklistId(Long checklistId) {
		this.checklistId = checklistId;
	}

	public String getChecklistHdCode() {
		return this.checklistHdCode;
	}

	public void setChecklistHdCode(String checklistHdCode) {
		this.checklistHdCode = checklistHdCode;
	}

	public String getChecklistName() {
		return this.checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}

	public String getChecklistValue() {
		return this.checklistValue;
	}

	public void setChecklistValue(String checklistValue) {
		this.checklistValue = checklistValue;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	
	

}