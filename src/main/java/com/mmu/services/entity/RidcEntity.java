package com.mmu.services.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
* This class consists five columns 
*
* <p>The "destructive" algorithms contained in this class, that is, the
* 
* <p><b>dId</b> unique if of the saved document.
* <p><b>dname</b> name of the saved document.
* <p><b>dDocId</b> docId of the saved document.
* <p><b>docName</b> name its provided by WebCenter Content Server
* <p><b>dFormat</b> content-type of the document
* 
* @author  Durgpal Singh
*/

@Entity
@Table(name = "RIDC_UPLOAD")
@SequenceGenerator(name="RIDC_UPLOAD_SEQ", sequenceName="RIDC_UPLOAD_SEQ", allocationSize=1)
public class RidcEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="RIDC_UPLOAD_SEQ")
    @Column(name = "RIDC_ID")
	private Long ridcId;
    
	@Column(name = "DOCUMENT_NAME")
	private String documentName;
	
    @Column(name= "DOCUMENT_ID")
	private Long documentId;
    
    @Column(name = "ENCRYPTED_NAME")
	private String encryptedName;
    
    @Column(name = "DOCUMENT_FORMAT")
    private String documentFormat;
    
    @Column(name = "DOCUMENT_URL")
    private String documentUrl;
    
    @Column(name = "ALFRESCO_ID")
    private String alfrescoId;

	public long getRidcId() {
		return ridcId;
	}

	public void setRidcId(long ridcId) {
		this.ridcId = ridcId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public String getEncryptedName() {
		return encryptedName;
	}

	public void setEncryptedName(String encryptedName) {
		this.encryptedName = encryptedName;
	}

	public String getDocumentFormat() {
		return documentFormat;
	}

	public void setDocumentFormat(String documentFormat) {
		this.documentFormat = documentFormat;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public String getAlfrescoId() {
		return alfrescoId;
	}

	public void setAlfrescoId(String alfrescoId) {
		this.alfrescoId = alfrescoId;
	}
	
	
	
	
	
}
