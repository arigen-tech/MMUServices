package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the UPLOAD_DOCUMENTS database table.
 * 
 */
@Entity
@Table(name="UPLOAD_DOCUMENTS")
@NamedQuery(name="UploadDocument.findAll", query="SELECT u FROM UploadDocument u")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="UPLOAD_DOCUMENTS_FILEID_GENERATOR", sequenceName="UPLOAD_DOCUMENTS_SEQ", allocationSize=1)
public class UploadDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="UPLOAD_DOCUMENTS_FILEID_GENERATOR")
	@Column(name="FILE_ID")
	private long fileId;

	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="BASE_64_IMAGE")
	private String base64Image;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;
	
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	//bi-directional many-to-one association to Patient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID")
	private Patient patient;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public UploadDocument() {
	}

	public long getFileId() {
		return this.fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	
	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	@Column(name="RIDC_ID")
	private Long ridcId;

	public Long getRidcId() {
		return ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

}