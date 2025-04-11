package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the PATIENT_MED_BOARD database table.
 * 
 */
@Entity
@Table(name="PATIENT_MED_BOARD")
@NamedQuery(name="PatientMedBoard.findAll", query="SELECT p FROM PatientMedBoard p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_MED_BOARD_PATIENTMEDICALBOARDID_GENERATOR", sequenceName="PATIENT_MEDICAL_BOARD_SEQ",allocationSize=1)
public class PatientMedBoard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_MED_BOARD_PATIENTMEDICALBOARDID_GENERATOR")
	@Column(name="PATIENT_MEDICAL_BOARD_ID")
	private Long patientMedicalBoardId;

	@Column(name="ADDRESS_ON_LEAVE")
	private String addressOnLeave;
    
	
	@Column(name="CEASED_DUTY_ON")
	private Date ceasedDutyOn;

	
	@Column(name="DATE_OF_ENROLMENT")
	private Date dateOfEnrolment;

	@Column(name="DIRECTLY_ATTR_SERVICE")
	private String directlyAttrService;

	@Column(name="DIRECTLY_ATTR_SERVICE_REMARK")
	private String directlyAttrServiceRemark;

	@Column(name="DISABLITY_ATTR_SERVICE")
	private String disablityAttrService;

	@Column(name="DISABLITY_ATTR_SERVICE_REMARK")
	private String disablityAttrServiceRemark;

	@Column(name="INSTRUCTION_NOTE")
	private String instructionNote;

	@Column(name="INSTRUCTION_REMARK")
	private String instructionRemark;

	@Column(name="MEM1_RANK")
	private String mem1Rank;

	@Column(name="MEM2_RANK")
	private String mem2Rank;

	@Column(name="MEM3_RANK")
	private String mem3Rank;

	private String member1;

	private String member2;

	private String member3;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="PRESENT_DISABLEMENT")
	private String presentDisablement;

	@Column(name="PREVIOUS_DISABLEMENT")
	private String previousDisablement;

	@Column(name="RECORD_OFFICE_ADDRESS")
	private String recordOfficeAddress;

	@Column(name="RESTRICTION_REGARDING_EMP")
	private String restrictionRegardingEmp;

	@Column(name="SIGNATURE_INDIVIDUAL")
	private String signatureIndividual;

	@Column(name="VISIT_ID")
	private Long visitId;
    
	@Column(name="REASON_FOR_VARIOUS")
	private String reasonForVarious;
	
	@Column(name="PLACE_OF_BOARD")
	private String placeOfBoard;
	
	@Column(name="AUTHORITY")
	private String authority;
	
	@Column(name="HEIGHT")
	private Long height;
	
	@Column(name="WEIGHT")
	private Double weight;
	
	
	@Column(name="SIG_DATE")
	private Date sigDate;
	
	@Column(name="PENDING_REMARKS")
	private String pendingRemarks;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="RIDC_ID")
	private Long ridcId;
	
	@Column(name="AA_NAME")
	private String aaName;
	
	@Column(name="AA_RANK_DEST")
	private String aaRankDest;
	
	@Column(name="AA_PLACE")
	private String aaPlace;
	
	
	@Column(name="AA_DATE")
	private Date aaDate;
	
	@Column(name="PA_NAME")
	private String paName;
	
	@Column(name="PA_RANK_DEST")
	private String paRankDest;
	
	@Column(name="PA_PLACE")
	private String paPlace;
	
	
	@Column(name="PA_DATE")
	private Date paDate;
	
	@Column(name="RANK_ID")
	private Long rankId;
	
	@Column(name="UNIT_ID")
	private Long unitId;
	
	@Column(name="HOSPITAL_ID")
	private Long hospitalId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "RANK_ID",nullable=false,insertable=false,updatable=false)
	private MasRank masRank;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_ID",referencedColumnName = "UNIT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasUnit masUnit;
	
	@Column(name="TYPE_OF_COMMISSION")
	private String typeOfCommission;
	
	public PatientMedBoard() {
	}

	public long getPatientMedicalBoardId() {
		return this.patientMedicalBoardId;
	}

	public void setPatientMedicalBoardId(long patientMedicalBoardId) {
		this.patientMedicalBoardId = patientMedicalBoardId;
	}

	public String getAddressOnLeave() {
		return this.addressOnLeave;
	}

	public void setAddressOnLeave(String addressOnLeave) {
		this.addressOnLeave = addressOnLeave;
	}

	public Date getCeasedDutyOn() {
		return this.ceasedDutyOn;
	}

	public void setCeasedDutyOn(Date ceasedDutyOn) {
		this.ceasedDutyOn = ceasedDutyOn;
	}

	public Date getDateOfEnrolment() {
		return this.dateOfEnrolment;
	}

	public void setDateOfEnrolment(Date dateOfEnrolment) {
		this.dateOfEnrolment = dateOfEnrolment;
	}

	public String getDirectlyAttrService() {
		return this.directlyAttrService;
	}

	public void setDirectlyAttrService(String directlyAttrService) {
		this.directlyAttrService = directlyAttrService;
	}

	public String getDirectlyAttrServiceRemark() {
		return this.directlyAttrServiceRemark;
	}

	public void setDirectlyAttrServiceRemark(String directlyAttrServiceRemark) {
		this.directlyAttrServiceRemark = directlyAttrServiceRemark;
	}

	public String getDisablityAttrService() {
		return this.disablityAttrService;
	}

	public void setDisablityAttrService(String disablityAttrService) {
		this.disablityAttrService = disablityAttrService;
	}

	public String getDisablityAttrServiceRemark() {
		return this.disablityAttrServiceRemark;
	}

	public void setDisablityAttrServiceRemark(String disablityAttrServiceRemark) {
		this.disablityAttrServiceRemark = disablityAttrServiceRemark;
	}

	public String getInstructionNote() {
		return this.instructionNote;
	}

	public void setInstructionNote(String instructionNote) {
		this.instructionNote = instructionNote;
	}

	public String getInstructionRemark() {
		return this.instructionRemark;
	}

	public void setInstructionRemark(String instructionRemark) {
		this.instructionRemark = instructionRemark;
	}

	public String getMem1Rank() {
		return this.mem1Rank;
	}

	public void setMem1Rank(String mem1Rank) {
		this.mem1Rank = mem1Rank;
	}

	public String getMem2Rank() {
		return this.mem2Rank;
	}

	public void setMem2Rank(String mem2Rank) {
		this.mem2Rank = mem2Rank;
	}

	public String getMem3Rank() {
		return this.mem3Rank;
	}

	public void setMem3Rank(String mem3Rank) {
		this.mem3Rank = mem3Rank;
	}

	public String getMember1() {
		return this.member1;
	}

	public void setMember1(String member1) {
		this.member1 = member1;
	}

	public String getMember2() {
		return this.member2;
	}

	public void setMember2(String member2) {
		this.member2 = member2;
	}

	public String getMember3() {
		return this.member3;
	}

	public void setMember3(String member3) {
		this.member3 = member3;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPresentDisablement() {
		return this.presentDisablement;
	}

	public void setPresentDisablement(String presentDisablement) {
		this.presentDisablement = presentDisablement;
	}

	public String getPreviousDisablement() {
		return this.previousDisablement;
	}

	public void setPreviousDisablement(String previousDisablement) {
		this.previousDisablement = previousDisablement;
	}

	public String getRecordOfficeAddress() {
		return this.recordOfficeAddress;
	}

	public void setRecordOfficeAddress(String recordOfficeAddress) {
		this.recordOfficeAddress = recordOfficeAddress;
	}

	public String getRestrictionRegardingEmp() {
		return this.restrictionRegardingEmp;
	}

	public void setRestrictionRegardingEmp(String restrictionRegardingEmp) {
		this.restrictionRegardingEmp = restrictionRegardingEmp;
	}

	public String getSignatureIndividual() {
		return this.signatureIndividual;
	}

	public void setSignatureIndividual(String signatureIndividual) {
		this.signatureIndividual = signatureIndividual;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getReasonForVarious() {
		return reasonForVarious;
	}

	public void setReasonForVarious(String reasonForVarious) {
		this.reasonForVarious = reasonForVarious;
	}

	public String getPlaceOfBoard() {
		return placeOfBoard;
	}

	public void setPlaceOfBoard(String placeOfBoard) {
		this.placeOfBoard = placeOfBoard;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Date getSigDate() {
		return sigDate;
	}

	public void setSigDate(Date sigDate) {
		this.sigDate = sigDate;
	}

	public String getPendingRemarks() {
		return pendingRemarks;
	}

	public void setPendingRemarks(String pendingRemarks) {
		this.pendingRemarks = pendingRemarks;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getRidcId() {
		return ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

	public String getAaName() {
		return aaName;
	}

	public void setAaName(String aaName) {
		this.aaName = aaName;
	}

	public String getAaRankDest() {
		return aaRankDest;
	}

	public void setAaRankDest(String aaRankDest) {
		this.aaRankDest = aaRankDest;
	}

	public String getAaPlace() {
		return aaPlace;
	}

	public void setAaPlace(String aaPlace) {
		this.aaPlace = aaPlace;
	}

	public Date getAaDate() {
		return aaDate;
	}

	public void setAaDate(Date aaDate) {
		this.aaDate = aaDate;
	}

	public String getPaName() {
		return paName;
	}

	public void setPaName(String paName) {
		this.paName = paName;
	}

	public String getPaRankDest() {
		return paRankDest;
	}

	public void setPaRankDest(String paRankDest) {
		this.paRankDest = paRankDest;
	}

	public String getPaPlace() {
		return paPlace;
	}

	public void setPaPlace(String paPlace) {
		this.paPlace = paPlace;
	}

	public Date getPaDate() {
		return paDate;
	}

	public void setPaDate(Date paDate) {
		this.paDate = paDate;
	}

	public Long getRankId() {
		return rankId;
	}

	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public MasRank getMasRank() {
		return masRank;
	}

	public void setMasRank(MasRank masRank) {
		this.masRank = masRank;
	}

	public MasUnit getMasUnit() {
		return masUnit;
	}

	public void setMasUnit(MasUnit masUnit) {
		this.masUnit = masUnit;
	}
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Visit visit;



	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public String getTypeOfCommission() {
		return typeOfCommission;
	}

	public void setTypeOfCommission(String typeOfCommission) {
		this.typeOfCommission = typeOfCommission;
	}
	
	
	
	
	
	
	
	
	
	
	

}