package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the labour database table.
 * 
 */
@Entity
@NamedQuery(name="Labour.findAll", query="SELECT l FROM Labour l")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "labour")
@SequenceGenerator(name="LABOUR_LABOURID_GENERATOR", sequenceName="LABOUR_SEQ", allocationSize=1)
public class Labour implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LABOUR_LABOURID_GENERATOR")
	@Column(name="labour_id")
	private Long labourId;

	@Column(name="aadhaar_card")
	private String aadhaarCard;

	@Column(name="aadhaar_card_no")
	private String aadhaarCardNo;

	@Column(name="account_no")
	private String accountNo;

	@Column(name="administrative_sex_id")
	private Long administrativeSexId;

	@Column(name="admission_flag")
	private String admissionFlag;

	@Column(name="bank_branch_name")
	private String bankBranchName;

	@Column(name="bank_flag")
	private String bankFlag;

	@Column(name="bank_name")
	private String bankName;

	@Column(name="caste_id")
	private String casteId;

	@Column(name="city_id")
	private Long cityId;

	@Temporal(TemporalType.DATE)
	@Column(name="date_of_birth")
	private Date dateOfBirth;

	@Column(name="district_id")
	private Long districtId;

	@Column(name="education_flag")
	private String educationFlag;

	@Column(name="education_level")
	private String educationLevel;

	@Column(name="education_status")
	private String educationStatus;

	@Column(name="father_name")
	private String fatherName;

	private String handicapped;

	@Column(name="head_of_family")
	private String headOfFamily;

	@Column(name="head_of_family_relation_id")
	private String headOfFamilyRelationId;

	@Column(name="head_of_family_sex_id")
	private String headOfFamilySexId;

	@Column(name="health_card_flag")
	private String healthCardFlag;

	@Column(name="health_card_no")
	private String healthCardNo;

	@Column(name="health_status")
	private String healthStatus;

	@Column(name="houser_no")
	private String houserNo;

	@Column(name="ifsc_no")
	private String ifscNo;

	@Column(name="labour_eng_name")
	private String labourEngName;

	@Column(name="labour_hin_name")
	private String labourHinName;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private String literacy;

	@Column(name="marital_status_id")
	private String maritalStatusId;

	@Column(name="mnraga_card_flag")
	private String mnragaCardFlag;

	@Column(name="mnraga_card_no")
	private String mnragaCardNo;

	@Column(name="mobile_number")
	private String mobileNumber;

	@Column(name="mother_name")
	private String motherName;

	private String other;

	@Column(name="par_city_id")
	private Long parCityId;

	
	@Column(name="assembly")
	private String assembly;
	
	@Column(name="block")
	private String block;
	
	@Column(name="panchayat")
	private String panchayat;
	
	@Column(name="gram")
	private String gram;
	
	@Column(name="par_assembly")
	private String parAssembly;
	
	@Column(name="par_block")
	private String parBlock;
	
	@Column(name="par_panchayat")
	private String parPanchayat;
	
	@Column(name="par_gram")
	private String parGram;
	
	
	@Column(name="par_district_id")
	private Long parDistrictId;

	@Column(name="par_houser_no")
	private String parHouserNo;

	@Column(name="par_pincode")
	private Integer parPincode;

	@Column(name="par_rural_urban")
	private String parRuralUrban;

	@Column(name="par_street")
	private String parStreet;

	@Column(name="par_ward_id")
	private Long parWardId;

	@Column(name="par_zone_id")
	private Long parZoneId;

	@Column(name="parmanent_address")
	private String parmanentAddress;

	private Integer pincode;

	@Column(name="ration_card")
	private String rationCard;

	@Column(name="ration_card_no")
	private String rationCardNo;

	private String reason;

	@Column(name="rural_urban")
	private String ruralUrban;

	private String schoolarship;

	@Column(name="schoolarship_dept")
	private String schoolarshipDept;

	@Column(name="schoolarship_scheme")
	private String schoolarshipScheme;

	private String street;

	@Column(name="sub_caste_id")
	private String subCasteId;

	@Column(name="tin_no")
	private String tinNo;

	@Column(name="tin_no_flag")
	private String tinNoFlag;

	private String training;

	private String transport;

	@Column(name="type_of_illness")
	private String typeOfIllness;

	@Column(name="type_of_school")
	private String typeOfSchool;

	@Column(name="un_flag")
	private String unFlag;

	@Column(name="un_no")
	private String unNo;

	@Column(name="ward_id")
	private Long wardId;

	@Column(name="worker_id")
	private String workerId;
	
	@Column(name="m_jobcard_no")
	private String mJobcardNo;

	@Column(name="zone_id")
	private Long zoneId;
    
	@Column(name="scheme_no")
	private String schemeNo;

	@Column(name="scheme_flag")
	private String schemeFlag;
	
	public Labour() {
	}

	public Long getLabourId() {
		return this.labourId;
	}

	public void setLabourId(Long labourId) {
		this.labourId = labourId;
	}

	public String getAadhaarCard() {
		return this.aadhaarCard;
	}

	public void setAadhaarCard(String aadhaarCard) {
		this.aadhaarCard = aadhaarCard;
	}

	public String getAadhaarCardNo() {
		return this.aadhaarCardNo;
	}

	public void setAadhaarCardNo(String aadhaarCardNo) {
		this.aadhaarCardNo = aadhaarCardNo;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Long getAdministrativeSexId() {
		return this.administrativeSexId;
	}

	public void setAdministrativeSexId(Long administrativeSexId) {
		this.administrativeSexId = administrativeSexId;
	}

	public String getAdmissionFlag() {
		return this.admissionFlag;
	}

	public void setAdmissionFlag(String admissionFlag) {
		this.admissionFlag = admissionFlag;
	}

	public String getBankBranchName() {
		return this.bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getBankFlag() {
		return this.bankFlag;
	}

	public void setBankFlag(String bankFlag) {
		this.bankFlag = bankFlag;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCasteId() {
		return this.casteId;
	}

	public void setCasteId(String casteId) {
		this.casteId = casteId;
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

	public String getEducationFlag() {
		return this.educationFlag;
	}

	public void setEducationFlag(String educationFlag) {
		this.educationFlag = educationFlag;
	}

	public String getEducationLevel() {
		return this.educationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}

	public String getEducationStatus() {
		return this.educationStatus;
	}

	public void setEducationStatus(String educationStatus) {
		this.educationStatus = educationStatus;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getHandicapped() {
		return this.handicapped;
	}

	public void setHandicapped(String handicapped) {
		this.handicapped = handicapped;
	}

	public String getHeadOfFamily() {
		return this.headOfFamily;
	}

	public void setHeadOfFamily(String headOfFamily) {
		this.headOfFamily = headOfFamily;
	}

	public String getHeadOfFamilyRelationId() {
		return this.headOfFamilyRelationId;
	}

	public void setHeadOfFamilyRelationId(String headOfFamilyRelationId) {
		this.headOfFamilyRelationId = headOfFamilyRelationId;
	}

	public String getHeadOfFamilySexId() {
		return this.headOfFamilySexId;
	}

	public void setHeadOfFamilySexId(String headOfFamilySexId) {
		this.headOfFamilySexId = headOfFamilySexId;
	}

	public String getHealthCardFlag() {
		return this.healthCardFlag;
	}

	public void setHealthCardFlag(String healthCardFlag) {
		this.healthCardFlag = healthCardFlag;
	}

	public String getHealthCardNo() {
		return this.healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getHealthStatus() {
		return this.healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public String getHouserNo() {
		return this.houserNo;
	}

	public void setHouserNo(String houserNo) {
		this.houserNo = houserNo;
	}

	public String getIfscNo() {
		return this.ifscNo;
	}

	public void setIfscNo(String ifscNo) {
		this.ifscNo = ifscNo;
	}

	public String getLabourEngName() {
		return this.labourEngName;
	}

	public void setLabourEngName(String labourEngName) {
		this.labourEngName = labourEngName;
	}

	public String getLabourHinName() {
		return this.labourHinName;
	}

	public void setLabourHinName(String labourHinName) {
		this.labourHinName = labourHinName;
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

	public String getLiteracy() {
		return this.literacy;
	}

	public void setLiteracy(String literacy) {
		this.literacy = literacy;
	}

	public String getMaritalStatusId() {
		return this.maritalStatusId;
	}

	public void setMaritalStatusId(String maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}

	public String getMnragaCardFlag() {
		return this.mnragaCardFlag;
	}

	public void setMnragaCardFlag(String mnragaCardFlag) {
		this.mnragaCardFlag = mnragaCardFlag;
	}

	public String getMnragaCardNo() {
		return this.mnragaCardNo;
	}

	public void setMnragaCardNo(String mnragaCardNo) {
		this.mnragaCardNo = mnragaCardNo;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMotherName() {
		return this.motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Long getParCityId() {
		return this.parCityId;
	}

	public void setParCityId(Long parCityId) {
		this.parCityId = parCityId;
	}

	public Long getParDistrictId() {
		return this.parDistrictId;
	}

	public void setParDistrictId(Long parDistrictId) {
		this.parDistrictId = parDistrictId;
	}

	public String getParHouserNo() {
		return this.parHouserNo;
	}

	public void setParHouserNo(String parHouserNo) {
		this.parHouserNo = parHouserNo;
	}

	public Integer getParPincode() {
		return this.parPincode;
	}

	public void setParPincode(Integer parPincode) {
		this.parPincode = parPincode;
	}

	public String getParRuralUrban() {
		return this.parRuralUrban;
	}

	public void setParRuralUrban(String parRuralUrban) {
		this.parRuralUrban = parRuralUrban;
	}

	public String getParStreet() {
		return this.parStreet;
	}

	public void setParStreet(String parStreet) {
		this.parStreet = parStreet;
	}

	public Long getParWardId() {
		return this.parWardId;
	}

	public void setParWardId(Long parWardId) {
		this.parWardId = parWardId;
	}

	public Long getParZoneId() {
		return this.parZoneId;
	}

	public void setParZoneId(Long parZoneId) {
		this.parZoneId = parZoneId;
	}

	public String getParmanentAddress() {
		return this.parmanentAddress;
	}

	public void setParmanentAddress(String parmanentAddress) {
		this.parmanentAddress = parmanentAddress;
	}

	public Integer getPincode() {
		return this.pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getRationCard() {
		return this.rationCard;
	}

	public void setRationCard(String rationCard) {
		this.rationCard = rationCard;
	}

	public String getRationCardNo() {
		return this.rationCardNo;
	}

	public void setRationCardNo(String rationCardNo) {
		this.rationCardNo = rationCardNo;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRuralUrban() {
		return this.ruralUrban;
	}

	public void setRuralUrban(String ruralUrban) {
		this.ruralUrban = ruralUrban;
	}

	public String getSchoolarship() {
		return this.schoolarship;
	}

	public void setSchoolarship(String schoolarship) {
		this.schoolarship = schoolarship;
	}

	public String getSchoolarshipDept() {
		return this.schoolarshipDept;
	}

	public void setSchoolarshipDept(String schoolarshipDept) {
		this.schoolarshipDept = schoolarshipDept;
	}

	public String getSchoolarshipScheme() {
		return this.schoolarshipScheme;
	}

	public void setSchoolarshipScheme(String schoolarshipScheme) {
		this.schoolarshipScheme = schoolarshipScheme;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSubCasteId() {
		return this.subCasteId;
	}

	public void setSubCasteId(String subCasteId) {
		this.subCasteId = subCasteId;
	}

	public String getTinNo() {
		return this.tinNo;
	}

	public void setTinNo(String tinNo) {
		this.tinNo = tinNo;
	}

	public String getTinNoFlag() {
		return this.tinNoFlag;
	}

	public void setTinNoFlag(String tinNoFlag) {
		this.tinNoFlag = tinNoFlag;
	}

	public String getTraining() {
		return this.training;
	}

	public void setTraining(String training) {
		this.training = training;
	}

	public String getTransport() {
		return this.transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getTypeOfIllness() {
		return this.typeOfIllness;
	}

	public void setTypeOfIllness(String typeOfIllness) {
		this.typeOfIllness = typeOfIllness;
	}

	public String getTypeOfSchool() {
		return this.typeOfSchool;
	}

	public void setTypeOfSchool(String typeOfSchool) {
		this.typeOfSchool = typeOfSchool;
	}

	public String getUnFlag() {
		return this.unFlag;
	}

	public void setUnFlag(String unFlag) {
		this.unFlag = unFlag;
	}

	public String getUnNo() {
		return this.unNo;
	}

	public void setUnNo(String unNo) {
		this.unNo = unNo;
	}

	public Long getWardId() {
		return this.wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public String getWorkerId() {
		return this.workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public Long getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public String getAssembly() {
		return assembly;
	}

	public void setAssembly(String assembly) {
		this.assembly = assembly;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getPanchayat() {
		return panchayat;
	}

	public void setPanchayat(String panchayat) {
		this.panchayat = panchayat;
	}

	public String getGram() {
		return gram;
	}

	public void setGram(String gram) {
		this.gram = gram;
	}

	public String getParAssembly() {
		return parAssembly;
	}

	public void setParAssembly(String parAssembly) {
		this.parAssembly = parAssembly;
	}

	public String getParBlock() {
		return parBlock;
	}

	public void setParBlock(String parBlock) {
		this.parBlock = parBlock;
	}

	public String getParPanchayat() {
		return parPanchayat;
	}

	public void setParPanchayat(String parPanchayat) {
		this.parPanchayat = parPanchayat;
	}

	public String getParGram() {
		return parGram;
	}

	public void setParGram(String parGram) {
		this.parGram = parGram;
	}

	public String getmJobcardNo() {
		return mJobcardNo;
	}

	public void setmJobcardNo(String mJobcardNo) {
		this.mJobcardNo = mJobcardNo;
	}

	public String getSchemeNo() {
		return schemeNo;
	}

	public void setSchemeNo(String schemeNo) {
		this.schemeNo = schemeNo;
	}

	public String getSchemeFlag() {
		return schemeFlag;
	}

	public void setSchemeFlag(String schemeFlag) {
		this.schemeFlag = schemeFlag;
	}
  
	
	
}