package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the fund_allocation_hd database table.
 * 
 */
@Entity
@Table(name="capture_interest_hd")
@NamedQuery(name="CaptureInterestHd.findAll", query="SELECT f FROM CaptureInterestHd f")
public class CaptureInterestHd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CAPTURE_INTEREST_CAPTUREINTERESTHDID_GENERATOR", sequenceName="capture_interest_hd_id_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CAPTURE_INTEREST_CAPTUREINTERESTHDID_GENERATOR")
	@Column(name="capture_interest_hd_id")
	private Long captureInterestHdId;

	@Column(name="approved_by")
	private Long approvedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="approved_date")
	private Date approvedDate;

	@Column(name="created_by")
	private Long createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="created_on")
	private Date createdOn;

 
	
	@Column(name="financial_id")
	private Long financialId;

	@Column(name="financial_year")
	private String financialYear;

	 
 
	private String remarks;

 

	private String status;

	@Column(name="phase")
	private String phase;
 
 	
	public String getPhase() {
		return phase;
	}



	public void setPhase(String phase) {
		this.phase = phase;
	}



	public CaptureInterestHd() {
	}



	public Long getCaptureInterestHdId() {
		return captureInterestHdId;
	}



	public void setCaptureInterestHdId(Long captureInterestHdId) {
		this.captureInterestHdId = captureInterestHdId;
	}



	public Long getApprovedBy() {
		return approvedBy;
	}



	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}



	public Date getApprovedDate() {
		return approvedDate;
	}



	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}



	public Long getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}



	public Date getCreatedOn() {
		return createdOn;
	}



	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



	public Long getFinancialId() {
		return financialId;
	}



	public void setFinancialId(Long financialId) {
		this.financialId = financialId;
	}



	public String getFinancialYear() {
		return financialYear;
	}



	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}


 


	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


 
 


	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

 
}