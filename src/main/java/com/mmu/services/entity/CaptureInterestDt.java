package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the fund_allocation_dt database table.
 * 
 */
@Entity
@Table(name="capture_interest_dt")
@NamedQuery(name="CaptureInterestDt.findAll", query="SELECT f FROM CaptureInterestDt f")
public class CaptureInterestDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CAPTURE_INTEREST_GENERATOR", sequenceName="capture_interest_dt_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CAPTURE_INTEREST_GENERATOR")
	@Column(name="capture_interest_dt_id")
	private Long captureInterestDtId;

	public Long getCaptureInterestDtId() {
		return captureInterestDtId;
	}
 
	public void setCaptureInterestDtId(Long captureInterestDtId) {
		this.captureInterestDtId = captureInterestDtId;
	}



	@Column(name="interest")
	private Long interest;

	 
	@Column(name="city_id")
	private Long cityId;

	@Column(name="district_id")
	private Long districtId;

	@Column(name="head_type_id")
	private Long headTypeId;
	@Column(name="FINANCIAL_ID")
	private Long financialId;
	@Column(name="capture_interest_hd_id")
	private Long captureInterestHdId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="capture_interest_hd_id",nullable=false,insertable=false,updatable=false)
	private CaptureInterestHd captureInterestHd;
	
	public Long getCaptureInterestHdId() {
		return captureInterestHdId;
	}





	public void setCaptureInterestHdId(Long captureInterestHdId) {
		this.captureInterestHdId = captureInterestHdId;
	}





	public Long getFinancialId() {
		return financialId;
	}





	public void setFinancialId(Long financialId) {
		this.financialId = financialId;
	}


	
	@Column(name = "created_by")
	private Long createBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name="approved_by")
	private Long approvedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="approved_date")
	private Date approvedDate;

	
	
	public Long getCreateBy() {
		return createBy;
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





	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}





	public Date getCreatedOn() {
		return createdOn;
	}





	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



	@Column(name="allocation_flag")
	private String allocationFlag;
	
	public String getAllocationFlag() {
		return allocationFlag;
	}





	public void setAllocationFlag(String allocationFlag) {
		this.allocationFlag = allocationFlag;
	}



	private String status;
	
	public CaptureInterestDt() {
	}

 

	 

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	 

	public Long getHeadTypeId() {
		return this.headTypeId;
	}

	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}

 
	public Long getInterest() {
		return interest;
	}



	public void setInterest(Long interest) {
		this.interest = interest;
	}



 


	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	public CaptureInterestHd getCaptureInterestHd() {
		return captureInterestHd;
	}

	public void setCaptureInterestHd(CaptureInterestHd captureInterestHd) {
		this.captureInterestHd = captureInterestHd;
	}

}