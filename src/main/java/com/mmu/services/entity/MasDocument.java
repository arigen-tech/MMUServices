package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the MAS_DOCUMENT database table.
 * 
 */
@Entity
@Table(name="MAS_DOCUMENT")
@NamedQuery(name="MasDocument.findAll", query="SELECT m FROM MasDocument m")
@SequenceGenerator(name="MAS_DOCUMENT_DOCUMENTID_GENERATOR", sequenceName="MAS_DOCUMENT_SEQ", allocationSize = 1)
public class MasDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DOCUMENT_DOCUMENTID_GENERATOR")
	@Column(name="DOCUMENT_ID")
	private long documentId;

	@Column(name="DOCUMENT_CODE")
	private String documentCode;

	@Column(name="DOCUMENT_NAME")
	private String documentName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;
	
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	private String status;

	public MasDocument() {
	}

	public long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	public String getDocumentCode() {
		return this.documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getDocumentName() {
		return this.documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}