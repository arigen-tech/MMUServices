package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the uc_upload_dt database table.
 * 
 */
@Entity
@Table(name="uc_upload_dt")
@NamedQuery(name="UcUploadDt.findAll", query="SELECT u FROM UcUploadDt u")
public class UcUploadDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UC_UPLOAD_DT_UCUPLOADDTID_GENERATOR", sequenceName="UC_UPLOAD_DT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UC_UPLOAD_DT_UCUPLOADDTID_GENERATOR")
	@Column(name="uc_upload_dt_id")
	private Long ucUploadDtId;

	@Column(name="allocation_flag")
	private String allocationFlag;

	@Column(name="available_balance")
	private Long availableBalance;

	@Column(name="available_utilization")
	private Long availableUtilization;

	@Column(name="head_type_id")
	private Long headTypeId;

	@Column(name="uc_upload_hd_id")
	private Long ucUploadHdId;

	public UcUploadDt() {
	}

	public Long getUcUploadDtId() {
		return this.ucUploadDtId;
	}

	public void setUcUploadDtId(Long ucUploadDtId) {
		this.ucUploadDtId = ucUploadDtId;
	}

	public String getAllocationFlag() {
		return this.allocationFlag;
	}

	public void setAllocationFlag(String allocationFlag) {
		this.allocationFlag = allocationFlag;
	}

	public Long getAvailableBalance() {
		return this.availableBalance;
	}

	public void setAvailableBalance(Long availableBalance) {
		this.availableBalance = availableBalance;
	}

	public Long getAvailableUtilization() {
		return this.availableUtilization;
	}

	public void setAvailableUtilization(Long availableUtilization) {
		this.availableUtilization = availableUtilization;
	}

	public Long getHeadTypeId() {
		return this.headTypeId;
	}

	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}

	public Long getUcUploadHdId() {
		return this.ucUploadHdId;
	}

	public void setUcUploadHdId(Long ucUploadHdId) {
		this.ucUploadHdId = ucUploadHdId;
	}

}