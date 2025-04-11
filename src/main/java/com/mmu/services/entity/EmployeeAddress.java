package com.mmu.services.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the VU_MAS_EMPLOYEE_ADR database view.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_EMPLOYEE_ADR")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeAddress implements Serializable {
	


	private static final long serialVersionUID = -2468521536980926873L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ADR_ID",updatable = false, nullable = false)
	private long AddressId;

	@Column(name="ADR1")
	private String addressLine1;

	@Column(name="ADR2")
	private String addressLine2;
	
	@Column(name="ADR3")
	private String addressLine3;
	
	@Column(name="ADR4")
	private String addressLine4;

	@Column(name="EMPLOYEE_PID")
	private String employeePid;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DISTRICT_ID")
	private MasDistrict masDistrict;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EMPLOYEE_ID")
	private MasEmployee masEmployee;
	
	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STATE_ID")
	private MasState masState;

	@Column(name="PINCODE")
	private String pincode;

	public long getAddressId() {
		return AddressId;
	}

	public void setAddressId(long addressId) {
		AddressId = addressId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public MasEmployee getMasEmployee() {
		return masEmployee;
	}

	public void setMasEmployee(MasEmployee masEmployee) {
		this.masEmployee = masEmployee;
	}

	

	public String getEmployeePid() {
		return employeePid;
	}

	public void setEmployeePid(String employeePid) {
		this.employeePid = employeePid;
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