package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_EMPLOYEE database table.
 * 
 */

@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_EMPLOYEE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class MasEmployee implements Serializable {
	 

 	private static final long serialVersionUID = 8543607763179883176L;
	
	
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="EMPLOYEE_ID", updatable = false, nullable = false)
	private long employeeId;

	
	@Column(name="SERVICE_NO")
	private String serviceNo;
	

	@Column(name="EMPLOYEE_NAME")
	private String employeeName;
	
	
	@Column(name="EMPLOYEE_PID")
	private String employeePid;

	@Column(name="MOBILE_NO")
	private String mobileNo;
	
	@Column(name="AD_ID")
	private String adId;

	@Column(name="AGE")
	private String age;

	@Temporal(TemporalType.DATE)
	@Column(name="DOB")
	private Date dob;

	@Temporal(TemporalType.DATE)
	@Column(name="DOE")
	private Date doe;

	@Column(name="EMAIL")
	private String email;
	
	
	@Column(name="UNIT_ID")
	private String masUnit;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADMINISTRATIVE_SEX_ID")
	private MasAdministrativeSex masAdministrativeSex;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MARITAL_STATUS_ID")
	private MasMaritalStatus masMaritalStatus;

	

	@Column(name="RANK_ID")
	private String masRank;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RECORD_OFFICE_ADDRESS_ID")
	private MasRecordOfficeAddress masRecordOfficeAddress;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TRADE_ID")
	private MasTrade masTrade;

	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EMPLOYEE_CATEGORY_ID")
	private MasEmployeeCategory masEmployeeCategory;

	
	@OneToMany(mappedBy="masEmployee")
	@JsonBackReference
	private List<MasEmployeeDependent> masEmployeeDependents;
	
	@OneToMany(mappedBy="masEmployee")
	@JsonBackReference
	private List<EmployeeAddress> employeeAddress;
	
	public List<MasEmployeeDependent> getMasEmployeeDependents() {
		return masEmployeeDependents;
	}


	public void setMasEmployeeDependents(List<MasEmployeeDependent> masEmployeeDependents) {
		this.masEmployeeDependents = masEmployeeDependents;
	}


	public MasEmployee() {
	}


	public long getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}


	

	public String getServiceNo() {
		return serviceNo;
	}


	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}


	public String getEmployeeName() {
		return employeeName;
	}


	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	public String getEmployeePid() {
		return employeePid;
	}


	public void setEmployeePid(String employeePid) {
		this.employeePid = employeePid;
	}


	


	public MasAdministrativeSex getMasAdministrativeSex() {
		return masAdministrativeSex;
	}


	public void setMasAdministrativeSex(MasAdministrativeSex masAdministrativeSex) {
		this.masAdministrativeSex = masAdministrativeSex;
	}



	public MasMaritalStatus getMasMaritalStatus() {
		return masMaritalStatus;
	}


	public void setMasMaritalStatus(MasMaritalStatus masMaritalStatus) {
		this.masMaritalStatus = masMaritalStatus;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	

	/*public MasRank getMasRank() {
		return masRank;
	}


	public void setMasRank(MasRank masRank) {
		this.masRank = masRank;
	}
*/

	public String getAdId() {
		return adId;
	}


	public void setAdId(String adId) {
		this.adId = adId;
	}


	public String getAge() {
		return age;
	}


	public void setAge(String age) {
		this.age = age;
	}


	public Date getDob() {
		return dob;
	}


	public void setDob(Date dob) {
		this.dob = dob;
	}


	public Date getDoe() {
		return doe;
	}


	public void setDoe(Date doe) {
		this.doe = doe;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	

	public MasRecordOfficeAddress getMasRecordOfficeAddress() {
		return masRecordOfficeAddress;
	}


	public void setMasRecordOfficeAddress(MasRecordOfficeAddress masRecordOfficeAddress) {
		this.masRecordOfficeAddress = masRecordOfficeAddress;
	}


	public MasTrade getMasTrade() {
		return masTrade;
	}


	public void setMasTrade(MasTrade masTrade) {
		this.masTrade = masTrade;
	}


	public String getMasUnit() {
		return masUnit;
	}


	public void setMasUnit(String masUnit) {
		this.masUnit = masUnit;
	}


	public MasEmployeeCategory getMasEmployeeCategory() {
		return masEmployeeCategory;
	}


	public void setMasEmployeeCategory(MasEmployeeCategory masEmployeeCategory) {
		this.masEmployeeCategory = masEmployeeCategory;
	}



	public List<EmployeeAddress> getEmployeeAddress() {
		return employeeAddress;
	}


	public void setEmployeeAddress(List<EmployeeAddress> employeeAddress) {
		this.employeeAddress = employeeAddress;
	}


	public String getMasRank() {
		return masRank;
	}


	public void setMasRank(String masRank) {
		this.masRank = masRank;
	}



}