package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the EMPLOYEE_BLOOD_GROUP database table.
 * 
 */
@Entity
@Table(name="EMPLOYEE_BLOOD_GROUP")
@NamedQuery(name="EmployeeBloodGroup.findAll", query="SELECT e FROM EmployeeBloodGroup e")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="EMPLOYEE_BLOOD_GROUP_SEQ", sequenceName="EMPLOYEE_BLOOD_GROUP_SEQ",allocationSize=1)
public class EmployeeBloodGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="EMPLOYEE_BLOOD_GROUP_SEQ")
	@Column(name="EMPLOYEE_BLOOD_GROUP_ID")
	private long employeeBloodGroupId;

	private String address;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Column(name="EMPLOYEE_NAME")
	private String employeeName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="RANK_ID")
	private long rankId;

	@Column(name="SERVICE_NO")
	private String serviceNo;

	@Column(name="UNIT_ID")
	private long unitId;

	//bi-directional many-to-one association to MasAdministrativeSex
	@ManyToOne
	@JoinColumn(name="ADMINISTRATIVE_SEX_ID")
	private MasAdministrativeSex masAdministrativeSex;

	//bi-directional many-to-one association to MasBloodGroup
	@ManyToOne
	@JoinColumn(name="BLOOD_GROUP_ID")
	private MasBloodGroup masBloodGroup;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	public EmployeeBloodGroup() {
	}

	public long getEmployeeBloodGroupId() {
		return this.employeeBloodGroupId;
	}

	public void setEmployeeBloodGroupId(long employeeBloodGroupId) {
		this.employeeBloodGroupId = employeeBloodGroupId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public long getRankId() {
		return this.rankId;
	}

	public void setRankId(long rankId) {
		this.rankId = rankId;
	}

	public String getServiceNo() {
		return this.serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}

	public long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	public MasAdministrativeSex getMasAdministrativeSex() {
		return this.masAdministrativeSex;
	}

	public void setMasAdministrativeSex(MasAdministrativeSex masAdministrativeSex) {
		this.masAdministrativeSex = masAdministrativeSex;
	}

	public MasBloodGroup getMasBloodGroup() {
		return this.masBloodGroup;
	}

	public void setMasBloodGroup(MasBloodGroup masBloodGroup) {
		this.masBloodGroup = masBloodGroup;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}


}