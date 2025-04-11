package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;




@Entity
@Table(name = "PATIENT")
@NamedQuery(name="Patient.findAll", query="SELECT p FROM Patient p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@SequenceGenerator(name="PATIENT_SEQ", sequenceName="PATIENT_SEQ", allocationSize=1)
public class Patient implements Serializable {
	private static final long serialVersionUID = 1L;

	//@Id
	//@SequenceGenerator(name="patient_seq", sequenceName="patient_seq")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="patient_seq")
	//@GeneratedValue(strategy = GenerationType.AUTO, generator ="PATIENT_SEQ")
	//private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PATIENT_GENERATOR", sequenceName="PATIENT_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="PATIENT_GENERATOR")
	@Column(name="patient_id")
	private Long patientId;

	@Column(length=2147483647)
	private String address;

	@Column(name="city_id")
	private Long cityId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;

	@Temporal(TemporalType.DATE)
	@Column(name="date_of_birth")
	private Date dateOfBirth;

	@Column(name="district_id")
	private Long districtId;

	@Column(name="form_submitted", length=1)
	private String formSubmitted;

	@Column(name="identification_no", length=72)
	private String identificationNo;

	@Column(name="labor_registered", length=15)
	private String laborRegistered;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mobile_number", length=15)
	private String mobileNumber;

	@Column(length=100)
	private String occuption;

	@Column(name="patient_name", length=2147483647)
	private String patientName;

	@Column(name="patient_type", length=1)
	private String patientType;

	private Integer pincode;

	@Column(name="reg_no", length=20)
	private String regNo;

	@Column(name="state_id")
	private Long stateId;

	@Column(name="uhid_no", length=20)
	private String uhidNo;
	
	@Column(name="ADMINISTRATIVE_SEX_ID")
	private Long administrativeSexId;
	
	@Column(name="patient_image", length=200)
	private String patientImage;
	

	//bi-directional many-to-one association to MasAdministrativeSex
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="administrative_sex_id", referencedColumnName="administrative_sex_id",nullable=false,insertable=false,updatable=false)
	private MasAdministrativeSex masAdministrativeSex;

	//bi-directional many-to-one association to MasBloodGroup
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blood_group_id",nullable=false,insertable=false,updatable=false)
	private MasBloodGroup masBloodGroup;
	
	@Column(name="blood_group_id")
	private Long bloodGroupId;

	//bi-directional many-to-one association to MasIdentificationType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="identification_type_id", referencedColumnName="identification_type_id",nullable=false,insertable=false,updatable=false)
	private MasIdentificationType masIdentificationType;
	
	@Column(name="identification_type_id")
	private Long identificationTypeId;

	//bi-directional many-to-one association to MasLabor
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="labor_id",nullable=false,insertable=false,updatable=false)
	private MasLabor masLabor;
	
	@Column(name="labor_id")
	private Long labourId;

	//bi-directional many-to-one association to MasReligion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="religion_id",nullable=false,insertable=false,updatable=false)
	private MasReligion masReligion;
	
	@Column(name="religion_id")
	private Long religionId;

	//bi-directional many-to-one association to Visit
	@OneToMany(mappedBy="patient")
	private List<Visit> visits;
	
	@Column(name = "caste_id")
	private Long castId;
	
	@Column(name = "ward_id")
	private Long wardId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="reg_date")
	private Date regDate;
	
	private Long age;
	
	@OneToMany(mappedBy = "patient")
	private List<Visit> visit;
	
	@Column(name = "camp_id")
	private Long campId;
	
	@Column(name = "relation_id")
	private Long relationId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ward_id",nullable=false,insertable=false,updatable=false)
	private MasWard masWard;

	public Patient() {
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Long getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getFormSubmitted() {
		return this.formSubmitted;
	}

	public void setFormSubmitted(String formSubmitted) {
		this.formSubmitted = formSubmitted;
	}

	public String getIdentificationNo() {
		return this.identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getLaborRegistered() {
		return this.laborRegistered;
	}

	public void setLaborRegistered(String laborRegistered) {
		this.laborRegistered = laborRegistered;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOccuption() {
		return this.occuption;
	}

	public void setOccuption(String occuption) {
		this.occuption = occuption;
	}

	public String getPatientName() {
		return this.patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientType() {
		return this.patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public Integer getPincode() {
		return this.pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getRegNo() {
		return this.regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public Long getStateId() {
		return this.stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getUhidNo() {
		return this.uhidNo;
	}

	public void setUhidNo(String uhidNo) {
		this.uhidNo = uhidNo;
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

	public MasIdentificationType getMasIdentificationType() {
		return this.masIdentificationType;
	}

	public void setMasIdentificationType(MasIdentificationType masIdentificationType) {
		this.masIdentificationType = masIdentificationType;
	}

	public MasLabor getMasLabor() {
		return this.masLabor;
	}

	public void setMasLabor(MasLabor masLabor) {
		this.masLabor = masLabor;
	}

	public MasReligion getMasReligion() {
		return this.masReligion;
	}

	public void setMasReligion(MasReligion masReligion) {
		this.masReligion = masReligion;
	}

	public List<Visit> getVisits() {
		return this.visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	public Visit addVisit(Visit visit) {
		getVisits().add(visit);
		visit.setPatient(this);

		return visit;
	}

	public Visit removeVisit(Visit visit) {
		getVisits().remove(visit);
		visit.setPatient(null);

		return visit;
	}
	
	public Long getAdministrativeSexId() {
		return administrativeSexId;
	}

	public void setAdministrativeSexId(Long administrativeSexId) {
		this.administrativeSexId = administrativeSexId;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public Long getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Long bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Long getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Long identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public Long getLabourId() {
		return labourId;
	}

	public void setLabourId(Long labourId) {
		this.labourId = labourId;
	}

	public Long getReligionId() {
		return religionId;
	}

	public void setReligionId(Long religionId) {
		this.religionId = religionId;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public Long getCastId() {
		return castId;
	}

	public void setCastId(Long castId) {
		this.castId = castId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public List<Visit> getVisit() {
		return visit;
	}

	public void setVisit(List<Visit> visit) {
		this.visit = visit;
	}

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public MasWard getMasWard() {
		return masWard;
	}

	public void setMasWard(MasWard masWard) {
		this.masWard = masWard;
	}

	public String getPatientImage() {
		return patientImage;
	}

	public void setPatientImage(String patientImage) {
		this.patientImage = patientImage;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "Patient [patientId=" + patientId + ", address=" + address + ", cityId=" + cityId + ", masCity="
				+ masCity + ", masDistrict=" + masDistrict + ", dateOfBirth=" + dateOfBirth + ", districtId="
				+ districtId + ", formSubmitted=" + formSubmitted + ", identificationNo=" + identificationNo
				+ ", laborRegistered=" + laborRegistered + ", lastChgDate=" + lastChgDate + ", mobileNumber="
				+ mobileNumber + ", occuption=" + occuption + ", patientName=" + patientName + ", patientType="
				+ patientType + ", pincode=" + pincode + ", regNo=" + regNo + ", stateId=" + stateId + ", uhidNo="
				+ uhidNo + ", administrativeSexId=" + administrativeSexId + ", patientImage=" + patientImage
				+ ", masAdministrativeSex=" + masAdministrativeSex + ", masBloodGroup=" + masBloodGroup
				+ ", bloodGroupId=" + bloodGroupId + ", masIdentificationType=" + masIdentificationType
				+ ", identificationTypeId=" + identificationTypeId + ", masLabor=" + masLabor + ", labourId=" + labourId
				+ ", masReligion=" + masReligion + ", religionId=" + religionId + ", visits=" + visits + ", castId="
				+ castId + ", wardId=" + wardId + ", regDate=" + regDate + ", age=" + age + ", visit=" + visit
				+ ", campId=" + campId + ", relationId=" + relationId + ", masWard=" + masWard + "]";
	}

	
	
	
}
