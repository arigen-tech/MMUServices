package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the uc_upload_hd database table.
 * 
 */
@Entity
@Table(name="uc_upload_hd")
@NamedQuery(name="UcUploadHd.findAll", query="SELECT u FROM UcUploadHd u")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UcUploadHd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UC_UPLOAD_HD_UCUPLOADHDID_GENERATOR", sequenceName="UC_UPLOAD_HD_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UC_UPLOAD_HD_UCUPLOADHDID_GENERATOR")
	@Column(name="uc_upload_hd_id")
	private Long ucUploadHdId;

	@Column(name="created_by")
	private Long createdBy;

	@Column(name="file_name")
	private String fileName;

	@Column(name="financial_id")
	private Long financialId;

	private String phase;

	private String remarks;

	@Temporal(TemporalType.DATE)
	@Column(name="upload_date")
	private Date uploadDate;

	@Column(name="upload_flag")
	private String uploadFlag;

	@Column(name="upss_id")
	private Long upssId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="entry_date")
	private Date entryDate;
	
	@Column(name="certificate_no")
	private String certificateNo;
	
	
	public UcUploadHd() {
	}

	public Long getUcUploadHdId() {
		return this.ucUploadHdId;
	}

	public void setUcUploadHdId(Long ucUploadHdId) {
		this.ucUploadHdId = ucUploadHdId;
	}

	public Long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFinancialId() {
		return this.financialId;
	}

	public void setFinancialId(Long financialId) {
		this.financialId = financialId;
	}

	public String getPhase() {
		return this.phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUploadFlag() {
		return this.uploadFlag;
	}

	public void setUploadFlag(String uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public Long getUpssId() {
		return this.upssId;
	}

	public void setUpssId(Long upssId) {
		this.upssId = upssId;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	
	

}