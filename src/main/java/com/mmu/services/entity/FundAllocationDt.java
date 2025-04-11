package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fund_allocation_dt database table.
 * 
 */
@Entity
@Table(name="fund_allocation_dt")
@NamedQuery(name="FundAllocationDt.findAll", query="SELECT f FROM FundAllocationDt f")
public class FundAllocationDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FUND_ALLOCATION_DT_FUNDALLOCATIONDTID_GENERATOR", sequenceName="FUND_ALLOCATION_DT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FUND_ALLOCATION_DT_FUNDALLOCATIONDTID_GENERATOR")
	@Column(name="fund_allocation_dt_id")
	private Long fundAllocationDtId;

	@Column(name="allocated_amount")
	private Long allocatedAmount;

	@Column(name="allocation_flag")
	private String allocationFlag;

	@Column(name="city_id")
	private Long cityId;

	@Column(name="district_id")
	private Long districtId;

	@Column(name="fund_allocation_hd_id")
	private Long fundAllocationHdId;

	@Column(name="head_type_id")
	private Long headTypeId;

	public FundAllocationDt() {
	}

	public Long getFundAllocationDtId() {
		return this.fundAllocationDtId;
	}

	public void setFundAllocationDtId(Long fundAllocationDtId) {
		this.fundAllocationDtId = fundAllocationDtId;
	}

	public Long getAllocatedAmount() {
		return this.allocatedAmount;
	}

	public void setAllocatedAmount(Long allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}

	public String getAllocationFlag() {
		return this.allocationFlag;
	}

	public void setAllocationFlag(String allocationFlag) {
		this.allocationFlag = allocationFlag;
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

	public Long getFundAllocationHdId() {
		return this.fundAllocationHdId;
	}

	public void setFundAllocationHdId(Long fundAllocationHdId) {
		this.fundAllocationHdId = fundAllocationHdId;
	}

	public Long getHeadTypeId() {
		return this.headTypeId;
	}

	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}

}