package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_EMPLOYEE database table.
 * 
 */

@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="employee_document")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class EmployeeDocument implements Serializable {
	 

 	private static final long serialVersionUID = 8543607763179883176L;
	
	
 	@Id
	@SequenceGenerator(name="employee_document_seq", sequenceName="employee_document_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="employee_document_seq")
	@Column(name="employee_document_id", updatable = false, nullable = false)
	private long employeeDocumentId;
	

	@Column(name="document_name")
	private String documentName;
	
	
	@Column(name="document")
	private byte[] document;
	 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="employee_id")
	private EmployeeRegistration employeeId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="last_chg_by")
	private Users lastChgBy;
	

	@Temporal(TemporalType.DATE)
	@Column(name="last_chg_date")
	private Date lastChgDate;	
	
	@Column(name="file_path")
	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	@Column(name="mime_type")
	private String mimeType;
	
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public EmployeeDocument() {
	}

		public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}



	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}

	public EmployeeRegistration getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(EmployeeRegistration employeeId) {
		this.employeeId = employeeId;
	}

	public long getEmployeeDocumentId() {
		return employeeDocumentId;
	}

	public void setEmployeeDocumentId(long employeeDocumentId) {
		this.employeeDocumentId = employeeDocumentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	


	

}