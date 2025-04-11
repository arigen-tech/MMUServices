package com.mmu.services.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "mas_audit")
@SequenceGenerator(name = "MAS_MMU_AUDIT_GENERATOR", sequenceName = "mas_audit_seq", allocationSize = 1)
public class MasAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_MMU_AUDIT_GENERATOR")
	@Column(name = "audit_id")
	private Long auditId;

	@Column(name = "audit_code")
	private String auditCode;

	@Column(name = "audit_name")
	private String auditName;

	@Column(name = "status")
	private String status;

	@Column(name = "last_chg_by")
	private Long lastChangeBy;

	@Column(name = "last_chg_date")
	private Date lastChangeDate;

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(Long lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	@Override
	public String toString() {
		return "MasAudit [auditId=" + auditId + ", auditCode=" + auditCode + ", auditName=" + auditName + ", status="
				+ status + ", lastChangeBy=" + lastChangeBy + ", lastChangeDate=" + lastChangeDate + "]";
	}

}
