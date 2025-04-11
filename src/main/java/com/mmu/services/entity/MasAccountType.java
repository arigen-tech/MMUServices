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
 * The persistent class for the MAS_ACCOUNT_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_ACCOUNT_TYPE")
@NamedQuery(name="MasAccountType.findAll", query="SELECT m FROM MasAccountType m")
@SequenceGenerator(name="MAS_ACCOUNT_TYPE_ACCOUNTID_GENERATOR", sequenceName="MAS_ACCOUNT_TYPE_SEQ", allocationSize = 1)
public class MasAccountType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_ACCOUNT_TYPE_ACCOUNTID_GENERATOR")
	@Column(name="ACCOUNT_ID")
	private Long accountId;

	@Column(name="ACCOUNT_TYPE_CODE")
	private String accountTypeCode;

	@Column(name="ACCOUNT_TYPE_NAME")
	private String accountTypeName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	public MasAccountType() {
	}

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountTypeCode() {
		return this.accountTypeCode;
	}

	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}

	public String getAccountTypeName() {
		return this.accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
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