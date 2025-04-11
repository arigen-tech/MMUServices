package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the MAS_REGISTRATION_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_REGISTRATION_TYPE")
@NamedQuery(name="MasRegistrationType.findAll", query="SELECT m FROM MasRegistrationType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_REGISTRATION_TYPE_REGISTRATIONTYPEID_GENERATOR", sequenceName="REGISTRATION_TYPE_ID", allocationSize=1)
public class MasRegistrationType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8891935684301296269L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_REGISTRATION_TYPE_REGISTRATIONTYPEID_GENERATOR")
	@Column(name="REGISTRATION_TYPE_ID")
	private long registrationTypeId;

	@Column(name="LAST_CHG_BY")
	private BigDecimal lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="REGISTRATION_TYPE_CODE")
	private long registrationTypeCode;

	@Column(name="REGISTRATION_TYPE_NAME")
	private String registrationTypeName;

	private String status;

	public MasRegistrationType() {
	}

	public long getRegistrationTypeId() {
		return this.registrationTypeId;
	}

	public void setRegistrationTypeId(long registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public BigDecimal getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(BigDecimal lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public long getRegistrationTypeCode() {
		return this.registrationTypeCode;
	}

	public void setRegistrationTypeCode(long registrationTypeCode) {
		this.registrationTypeCode = registrationTypeCode;
	}

	public String getRegistrationTypeName() {
		return this.registrationTypeName;
	}

	public void setRegistrationTypeName(String registrationTypeName) {
		this.registrationTypeName = registrationTypeName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}