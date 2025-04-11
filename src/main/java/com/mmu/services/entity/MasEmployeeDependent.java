package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * The persistent class for the MAS_EMPLOYEE_DEPENDENT database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_EMPLOYEE_DEPENDENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasEmployeeDependent implements Serializable {
	private static final long serialVersionUID = 7384469056673691414L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EMPLOYEE_DEPENDENT_ID", updatable = false, nullable = false)
	private long employeeDependentId;

	@Column(name="EMPLOYEE_DEPENDENT_CODE")
	private String employeeDependentCode;

	@Column(name="EMPLOYEE_DEPENDENT_NAME")
	private String employeeDependentName;
	
	@Column(name="EMPLOYEE_DEPENDENT_PID")
	private String employeeDependentPid;

	
	@Temporal(TemporalType.DATE)
	@Column(name = "DOB")
	private Date dateOfBirth;
	
	@Column(name = "AGE")
	private String age;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RELATION_ID")
	private MasRelation masRelation;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ADMINISTRATIVE_SEX_ID")
	private MasAdministrativeSex masAdministrativeSex;
	
	
	@Column(name="ADD1")
	private String addresLine1;
	
	@Column(name="ADD2")
	private String addresLine2;
	
	@Column(name="ADD3")
	private String addresLine3;
	
	@Column(name="ADD4")
	private String addresLine4;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DISTRICT_ID")
	private MasDistrict masDistrict;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STATE_ID")
	private MasState masState;
	
	@Column(name="PINCODE")
	private String pincode;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EMPLOYEE_ID")
	private MasEmployee masEmployee;

	
	public MasEmployeeDependent() {
	}

	public long getEmployeeDependentId() {
		return this.employeeDependentId;
	}

	public void setEmployeeDependentId(long employeeDependentId) {
		this.employeeDependentId = employeeDependentId;
	}

	

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	

	public String getEmployeeDependentCode() {
		return this.employeeDependentCode;
	}

	public void setEmployeeDependentCode(String employeeDependentCode) {
		this.employeeDependentCode = employeeDependentCode;
	}

	

	public MasRelation getMasRelation() {
		return masRelation;
	}

	public void setMasRelation(MasRelation masRelation) {
		this.masRelation = masRelation;
	}


	public MasEmployee getMasEmployee() {
		return this.masEmployee;
	}

	public void setMasEmployee(MasEmployee masEmployee) {
		this.masEmployee = masEmployee;
	}

	public String getEmployeeDependentName() {
		return employeeDependentName;
	}

	public void setEmployeeDependentName(String employeeDependentName) {
		this.employeeDependentName = employeeDependentName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public MasAdministrativeSex getMasAdministrativeSex() {
		return masAdministrativeSex;
	}

	public void setMasAdministrativeSex(MasAdministrativeSex masAdministrativeSex) {
		this.masAdministrativeSex = masAdministrativeSex;
	}

	public String getAddresLine1() {
		return addresLine1;
	}

	public void setAddresLine1(String addresLine1) {
		this.addresLine1 = addresLine1;
	}

	public String getAddresLine2() {
		return addresLine2;
	}

	public void setAddresLine2(String addresLine2) {
		this.addresLine2 = addresLine2;
	}

	public String getAddresLine3() {
		return addresLine3;
	}

	public void setAddresLine3(String addresLine3) {
		this.addresLine3 = addresLine3;
	}

	

	public String getAddresLine4() {
		return addresLine4;
	}

	public void setAddresLine4(String addresLine4) {
		this.addresLine4 = addresLine4;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public MasState getMasState() {
		return masState;
	}

	public void setMasState(MasState masState) {
		this.masState = masState;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	
	
}