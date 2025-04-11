package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the MAS_ADMISSION_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_ADMISSION_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasAdmissionType.findAll", query="SELECT m FROM MasAdmissionType m")
public class MasAdmissionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ADMISSION_TYPE_ID")
	private long admissionTypeId;

	@Column(name="ADMISSION_TYPE_CODE")
	private String admissionTypeCode;

	@Column(name="ADMISSION_TYPE_NAME")
	private String admissionTypeName;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasAdmissionType() {
	}

	public long getAdmissionTypeId() {
		return this.admissionTypeId;
	}

	public void setAdmissionTypeId(long admissionTypeId) {
		this.admissionTypeId = admissionTypeId;
	}

	public String getAdmissionTypeCode() {
		return this.admissionTypeCode;
	}

	public void setAdmissionTypeCode(String admissionTypeCode) {
		this.admissionTypeCode = admissionTypeCode;
	}

	public String getAdmissionTypeName() {
		return this.admissionTypeName;
	}

	public void setAdmissionTypeName(String admissionTypeName) {
		this.admissionTypeName = admissionTypeName;
	}

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}