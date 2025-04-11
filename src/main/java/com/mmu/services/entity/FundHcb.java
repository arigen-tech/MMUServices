package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="fund_hcb")
public class FundHcb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FUND_HCB_FUNDHCBID_GENERATOR", sequenceName="FUND_HCB_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FUND_HCB_FUNDHCBID_GENERATOR")
	@Column(name="fund_hcb_id")
	private Long fundHcbId;
	

	@Column(name="fund_flag")
	private String fundFlag;

	@Column(name="city_id")
	private Long cityId;

	@Column(name="upss_id")
	private Long upssId;

	

	@Column(name="head_type_id")
	private Long headTypeId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="hcb_date")
	private Date hcbDate;
	
	@Column(name="opening_balance")
	private Long openingBalance;
	
	@Column(name="dr_cr")
	private Long debitCredit;
	
	@Column(name="hcb_balance")
	private Long hcbBalance;
	
	@Column(name="phase")
	private String phase;
	
	public FundHcb() {
		
	}

	public Long getFundHcbId() {
		return fundHcbId;
	}

	public void setFundHcbId(Long fundHcbId) {
		this.fundHcbId = fundHcbId;
	}



	public String getFundFlag() {
		return fundFlag;
	}

	public void setFundFlag(String fundFlag) {
		this.fundFlag = fundFlag;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getUpssId() {
		return upssId;
	}

	public void setUpssId(Long upssId) {
		this.upssId = upssId;
	}


	public Long getHeadTypeId() {
		return headTypeId;
	}

	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}

	public Date getHcbDate() {
		return hcbDate;
	}

	public void setHcbDate(Date hcbDate) {
		this.hcbDate = hcbDate;
	}

	public Long getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Long openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Long getDebitCredit() {
		return debitCredit;
	}

	public void setDebitCredit(Long debitCredit) {
		this.debitCredit = debitCredit;
	}

	public Long getHcbBalance() {
		return hcbBalance;
	}

	public void setHcbBalance(Long hcbBalance) {
		this.hcbBalance = hcbBalance;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	
	
}
