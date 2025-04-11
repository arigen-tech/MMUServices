package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the capture_vendor_supporting_docs database table.
 * 
 */
@Entity
@Table(name="capture_vendor_supporting_docs")
@NamedQuery(name="VendorInvoicSupportingDocs.findAll", query="SELECT v FROM VendorInvoicSupportingDocs v")
@SequenceGenerator(name="CAPTURE_VENDOR_SUPPORTING_DOCS_SEQ", sequenceName="CAPTURE_VENDOR_SUPPORTING_DOCS_SEQ",allocationSize=1)
public class VendorInvoicSupportingDocs implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="CAPTURE_VENDOR_SUPPORTING_DOCS_SEQ")
	@Column(name="capture_vendor_supporting_docs_id")
	private Long captureVendorSupportingDocsId;

	@Column(name="capture_vendor_bill_detail_id")
	private Long captureVendorBillDetailId;

	@Column(name="document_name")
	private String documentName;

	@Column(name="document_note")
	private String documentNote;

	@Column(name="file_name")
	private String fileName;
	
	@Column(name="uploaded_by")
	private Long userId;
	
	@Column(name="screen_type")
	private String screenType;
	

//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="uploaded_date")
//	private Timestamp uploadedDate;
	

	public VendorInvoicSupportingDocs() {
	}


	public Long getCaptureVendorSupportingDocsId() {
		return captureVendorSupportingDocsId;
	}


	public void setCaptureVendorSupportingDocsId(Long captureVendorSupportingDocsId) {
		this.captureVendorSupportingDocsId = captureVendorSupportingDocsId;
	}


	public Long getCaptureVendorBillDetailId() {
		return captureVendorBillDetailId;
	}


	public void setCaptureVendorBillDetailId(Long captureVendorBillDetailId) {
		this.captureVendorBillDetailId = captureVendorBillDetailId;
	}


	public String getDocumentName() {
		return documentName;
	}


	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}


	public String getDocumentNote() {
		return documentNote;
	}


	public void setDocumentNote(String documentNote) {
		this.documentNote = documentNote;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getScreenType() {
		return screenType;
	}


	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}
	
	

/*
	public Timestamp getUploadedDate() {
		return uploadedDate;
	}


	public void setUploadedDate(Timestamp uploadedDate) {
		this.uploadedDate = uploadedDate;
	}*/

	
}