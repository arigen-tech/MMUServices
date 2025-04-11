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
@Table(name="fund_allocation_hd")
@NamedQuery(name="FundAllocationHd.findAll", query="SELECT f FROM FundAllocationHd f")
public class FundAllocationHd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FUND_ALLOCATION_HD_FUNDALLOCATIONHDID_GENERATOR", sequenceName="FUND_ALLOCATION_HD_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FUND_ALLOCATION_HD_FUNDALLOCATIONHDID_GENERATOR")
	@Column(name="fund_allocation_hd_id")
	private Long fundAllocationHdId;

	@Column(name="approved_by")
	private Long approvedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="approved_date")
	private Date approvedDate;

	@Column(name="created_by")
	private Long createdBy;

	@Column(name="created_on")
	private Date createdOn;

	@Column(name="file_name")
	private String fileName;

	@Column(name="financial_id")
	private Long financialId;

	@Column(name="financial_year")
	private String financialYear;

	@Temporal(TemporalType.DATE)
	@Column(name="fund_allocation_date")
	private Date fundAllocationDate;

	@Column(name="letter_no")
	private String letterNo;

	private String remarks;

	private String scheme;

	private String status;

	@Column(name="total_amount")
	private Long totalAmount;


	@Column(name="phase")
	private String phase;
	
	public FundAllocationHd() {
	}

	public Long getFundAllocationHdId() {
		return this.fundAllocationHdId;
	}

	public void setFundAllocationHdId(Long fundAllocationHdId) {
		this.fundAllocationHdId = fundAllocationHdId;
	}

	public Long getApprovedBy() {
		return this.approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
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

	public String getFinancialYear() {
		return this.financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public Date getFundAllocationDate() {
		return this.fundAllocationDate;
	}

	public void setFundAllocationDate(Date fundAllocationDate) {
		this.fundAllocationDate = fundAllocationDate;
	}

	public String getLetterNo() {
		return this.letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getScheme() {
		return this.scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

}