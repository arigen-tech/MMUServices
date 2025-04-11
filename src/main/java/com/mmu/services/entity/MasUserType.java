package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the mas_user_type database table.
 * 
 */
@Entity
@Table(name="mas_user_type")
@NamedQuery(name="MasUserType.findAll", query="SELECT m FROM MasUserType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_USER_TYPE_GENERATOR", sequenceName="user_type_seq", allocationSize=1)
public class MasUserType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator = "MAS_USER_TYPE_GENERATOR" )
	@Column(name="user_type_id")
	private Long userTypeId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private String status;

	@Column(name="user_type_code")
	private String userTypeCode;

	@Column(name="user_type_name")
	private String userTypeName;

	@Column(name="mmu_staff")
	private String mmuStaff;
	
	@Column(name="map_id")
	private Long designation;
	
	public MasUserType() {
	}

	public Long getUserTypeId() {
		return this.userTypeId;
	}

	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
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

	public String getUserTypeCode() {
		return this.userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode) {
		this.userTypeCode = userTypeCode;
	}

	public String getUserTypeName() {
		return this.userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getMmuStaff() {
		return mmuStaff;
	}

	public void setMmuStaff(String mmuStaff) {
		this.mmuStaff = mmuStaff;
	}

	public Long getDesignation() {
		return designation;
	}

	public void setDesignation(Long designation) {
		this.designation = designation;
	}

	
}