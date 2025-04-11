package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the PATIENT_DOCUMENT_DETAILS database table.
 * 
 */
@Entity
@Table(name="PATIENT_DOCUMENT_DETAILS")
@NamedQuery(name="PatientDocumentDetail.findAll", query="SELECT p FROM PatientDocumentDetail p")
@SequenceGenerator(name="PATIENT_DOCUMENT_DETAILS_SEQ", sequenceName="PATIENT_DOCUMENT_DETAILS_SEQ",allocationSize=1)
public class PatientDocumentDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="PATIENT_DOCUMENT_DETAILS_PATIENTDOCUMENTID_GENERATOR", sequenceName="PATIENT_DOCUMENT_DETAILS_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_DOCUMENT_DETAILS_SEQ")
	@Column(name="PATIENT_DOCUMENT_ID")
	private Long patientDocumentId;

	@Column(name="DOCUMENT_ID")
	private Long documentId;

	@Column(name="DOCUMENT_REMARKS")
	private String documentRemarks;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="RIDC_ID")
	private Long ridcId;

	@Column(name="VISIT_ID")
	private Long visitId;

	 
	@Temporal(TemporalType.DATE)
	@Column(name="document_date")
	private Date documentDate;
	public PatientDocumentDetail() {
	}

 

	public Long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public String getDocumentRemarks() {
		return this.documentRemarks;
	}

	public void setDocumentRemarks(String documentRemarks) {
		this.documentRemarks = documentRemarks;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getRidcId() {
		return this.ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}



	public Long getPatientDocumentId() {
		return patientDocumentId;
	}



	public void setPatientDocumentId(Long patientDocumentId) {
		this.patientDocumentId = patientDocumentId;
	}



	public Date getDocumentDate() {
		return documentDate;
	}



	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}



	 

}