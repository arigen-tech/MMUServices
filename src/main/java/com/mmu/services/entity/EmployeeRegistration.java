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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_EMPLOYEE database table.
 * 
 */

@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="MAS_EMPLOYEE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class EmployeeRegistration implements Serializable {
	 

 	private static final long serialVersionUID = 8543607763179883176L;
	
	
 	@Id
	@SequenceGenerator(name="employee_seq", sequenceName="employee_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="employee_seq")
	@Column(name="employee_id", updatable = false, nullable = false)
	private long employeeId;
	

	@Column(name="employee_name")
	private String employeeName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="administrative_sex_id")
	private MasAdministrativeSex masAdministrativeSex;
	
	@Temporal(TemporalType.DATE)	
	@Column(name="date_of_birth")
	private Date dob;
	 
	
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Column(name="address")
	private String address;
	
	@Column(name="state_id")
	private String state;
	
	@Column(name="district_id")
	private String district;
	
	@Column(name="city_id")
	private String city;

	@Column(name="pincode")
	private Long pinCode;
	
	@Column(name="mobile_number")
	private String mobileNo;
	
	@Column(name="imei_number")
	private String imeiNumber;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_type_id")
	private MasUserType userTypeId;
	
	@Column(name="employment_type")
	private String employmentType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="employment_startdt")
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="employment_enddt")
	private Date endDate;

	@Temporal(TemporalType.DATE)
	@Column(name="last_chg_date")
	private Date lastChgDate;	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id")
	private MasMMU mmuId;
	
	@Column(name="reg_no")
	private String regNo;
	
	@Column(name="profile_img")
	private byte[] profileImage;
	
	@Column(name="record_status")
	private String recordStatus;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="auditor_id")
	private Users auditorId;
	
	@Column(name="auditor_flag")
	private String auditorFlag;
	
	@Temporal(TemporalType.DATE)
	@Column(name="auditor_date")
	private Date auditorDate;	
	
	@Column(name="auditor_remarks")
	private String auditorRemarks;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="apm_id")
	private Users apmId;
	
	@Column(name="apm_flag")
	private String apmFlag;
	
	@Temporal(TemporalType.DATE)
	@Column(name="apm_date")
	private Date apmDate;	
	
	@Column(name="apm_remarks")
	private String apmRemarks;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="chmo_id")
	private Users chmoId;
	
	@Column(name="chmo_flag")
	private String chmoFlag;
	
	@Temporal(TemporalType.DATE)
	@Column(name="chmo_date")
	private Date chmoDate;	
	
	@Column(name="chmo_remarks")
	private String chmoRemarks;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="upss_id")
	private Users upssId;
	
	@Column(name="upss_flag")
	private String upssFlag;
	
	@Temporal(TemporalType.DATE)
	@Column(name="upss_date")
	private Date upssDate;	
	
	@Column(name="upss_remarks")
	private String upssRemarks;
	
	@Column(name="file_path")
	private String filePath;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="identification_type_id")
	private MasIdentificationType identificationTypeId;
	
	@Column(name="identification_type_no")
	private String identificationTypeNo;
	
	@Column(name="identification_type_image")
	private byte[] identificationTypeImage;
	
	@Column(name="id_mime_type")
	private String idMimeType;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Users getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Users auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorFlag() {
		return auditorFlag;
	}

	public void setAuditorFlag(String auditorFlag) {
		this.auditorFlag = auditorFlag;
	}

	public Date getAuditorDate() {
		return auditorDate;
	}

	public void setAuditorDate(Date auditorDate) {
		this.auditorDate = auditorDate;
	}

	public String getAuditorRemarks() {
		return auditorRemarks;
	}

	public void setAuditorRemarks(String auditorRemarks) {
		this.auditorRemarks = auditorRemarks;
	}

	public Users getApmId() {
		return apmId;
	}

	public void setApmId(Users apmId) {
		this.apmId = apmId;
	}

	public String getApmFlag() {
		return apmFlag;
	}

	public void setApmFlag(String apmFlag) {
		this.apmFlag = apmFlag;
	}

	public Date getApmDate() {
		return apmDate;
	}

	public void setApmDate(Date apmDate) {
		this.apmDate = apmDate;
	}

	public String getApmRemarks() {
		return apmRemarks;
	}

	public void setApmRemarks(String apmRemarks) {
		this.apmRemarks = apmRemarks;
	}

	public Users getChmoId() {
		return chmoId;
	}

	public void setChmoId(Users chmoId) {
		this.chmoId = chmoId;
	}

	public String getChmoFlag() {
		return chmoFlag;
	}

	public void setChmoFlag(String chmoFlag) {
		this.chmoFlag = chmoFlag;
	}

	public Date getChmoDate() {
		return chmoDate;
	}

	public void setChmoDate(Date chmoDate) {
		this.chmoDate = chmoDate;
	}

	public String getChmoRemarks() {
		return chmoRemarks;
	}

	public void setChmoRemarks(String chmoRemarks) {
		this.chmoRemarks = chmoRemarks;
	}

	public Users getUpssId() {
		return upssId;
	}

	public void setUpssId(Users upssId) {
		this.upssId = upssId;
	}

	public String getUpssFlag() {
		return upssFlag;
	}

	public void setUpssFlag(String upssFlag) {
		this.upssFlag = upssFlag;
	}

	public Date getUpssDate() {
		return upssDate;
	}

	public void setUpssDate(Date upssDate) {
		this.upssDate = upssDate;
	}

	public String getUpssRemarks() {
		return upssRemarks;
	}

	public void setUpssRemarks(String upssRemarks) {
		this.upssRemarks = upssRemarks;
	}

	public EmployeeRegistration() {
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public MasAdministrativeSex getMasAdministrativeSex() {
		return masAdministrativeSex;
	}

	public void setMasAdministrativeSex(MasAdministrativeSex masAdministrativeSex) {
		this.masAdministrativeSex = masAdministrativeSex;
	}

	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public MasUserType getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(MasUserType userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public MasMMU getMmuId() {
		return mmuId;
	}

	public void setMmuId(MasMMU mmuId) {
		this.mmuId = mmuId;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public MasIdentificationType getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(MasIdentificationType identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationTypeNo() {
		return identificationTypeNo;
	}

	public void setIdentificationTypeNo(String identificationTypeNo) {
		this.identificationTypeNo = identificationTypeNo;
	}

	public byte[] getIdentificationTypeImage() {
		return identificationTypeImage;
	}

	public void setIdentificationTypeImage(byte[] identificationTypeImage) {
		this.identificationTypeImage = identificationTypeImage;
	}

	public String getIdMimeType() {
		return idMimeType;
	}

	public void setIdMimeType(String idMimeType) {
		this.idMimeType = idMimeType;
	}
	
	
	

	

}