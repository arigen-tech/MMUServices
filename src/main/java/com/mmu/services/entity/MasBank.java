package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_BANK database table.
 * 
 */
@Entity
@Table(name="MAS_BANK")
@NamedQuery(name="MasBank.findAll", query="SELECT m FROM MasBank m")
@SequenceGenerator(name="MAS_BANK_BANKID_GENERATOR", sequenceName="MAS_BANK_SEQ", allocationSize = 1)
public class MasBank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_BANK_BANKID_GENERATOR")
	@Column(name="BANK_ID")
	private Long bankId;

	@Column(name="BANK_CODE")
	private String bankCode;

	@Column(name="BANK_NAME")
	private String bankName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	public MasBank() {
	}

	public Long getBankId() {
		return this.bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}