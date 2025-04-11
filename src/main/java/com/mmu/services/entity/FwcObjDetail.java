package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the FWC_OBJ_DETAILS database table.
 * 
 */
@Entity
@Table(name="FWC_OBJ_DETAILS")
@NamedQuery(name="FwcObjDetail.findAll", query="SELECT f FROM FwcObjDetail f")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OBJ_DETAILS_SEQ", sequenceName="OBJ_DETAILS_SEQ", allocationSize=1)

public class FwcObjDetail implements Serializable {
	
	private static final long serialVersionUID = -4975822472022773237L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OBJ_DETAILS_SEQ")
	private long id;

	@Column(name="AGE_OF_MENARCHE")
	private Long ageOfMenarche;

	private String asicitis;

	private String booked;

	@Column(name="BREADTH_ADDED_SOUNDS")
	private String breadthAddedSounds;

	@Column(name="CLINICAL_HSIRORY")
	private String clinicalHsirory;

	private String conception;

	@Column(name="CONSAN_GUINITY")
	private String consanGuinity;

	@Column(name="CONSISTENCY_OF_CERVIX")
	private String consistencyOfCervix;

	@Column(name="CYCLE_TEXT")
	private String cycleText;

	private String cycles;

	@Column(name="DILATATION_OF_CERVIX")
	private Long dilatationOfCervix;

	private String dysmenorrhea;

	@Column(name="EFFACEMENT_OF_CERVIX")
	private String effacementOfCervix;

	@Column(name="EXTERNAL_GENITALIA")
	private String externalGenitalia;

	@Column(name="EXTERNAL_GENITALIA_OTHER")
	private String externalGenitaliaOther;

	private String fhr;

	private String flow;

	private String gc;

	@Column(name="GYN_ABDOMEN_INSPECTION")
	private String gynAbdomenInspection;

	@Column(name="GYN_AGE_OF_MENARCHE")
	private Long gynAgeOfMenarche;

	@Column(name="GYN_BIMANUAL_EXAMINATION")
	private String gynBimanualExamination;

	@Column(name="GYN_FLOW")
	private String gynFlow;

	@Column(name="GYN_INTERNAL_EXAMINATION")
	private String gynInternalExamination;

	@Column(name="GYN_LOCAL_EXAMINATION")
	private String gynLocalExamination;

	@Column(name="GYN_OBSTETRIC_HISTORY")
	private String gynObstetricHistory;

	@Column(name="GYN_PALPATION")
	private String gynPalpation;

	@Column(name="GYN_PER_SPECULUM")
	private String gynPerSpeculum;

	private String head;

	@Column(name="HEART_RATE_ABSENT")
	private Long heartRateAbsent;

	@Column(name="HEART_RATE_REGULAR")
	private Long heartRateRegular;

	@Column(name="HEIGHT_OF_UTERUS")
	private String heightOfUterus;

	private String hopi;

	private String immunised;

	@Column(name="INSPECTION_ABDOMEN")
	private String inspectionAbdomen;

	@Column(name="INSPECTION_HERNIA")
	private String inspectionHernia;

	@Column(name="INSPECTION_SCAR")
	private String inspectionScar;

	@Column(name="INSPECTION_UMBILICUS")
	private String inspectionUmbilicus;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MENSTRUAL")
	private Date lastMenstrual;

	@Column(name="LENGTH_OF_CERVIX")
	private Long lengthOfCervix;

	private String liquor;

	@Column(name="LOWER_GRIP")
	private String lowerGrip;

	@Column(name="LOWER_POLE")
	private String lowerPole;

	@Column(name="MARRIED_LIFE")
	private String marriedLife;

	@Column(name="MASS_CONSISTENCY")
	private String massConsistency;

	@Column(name="MASS_PALPABLE")
	private String massPalpable;

	@Column(name="MASS_POSITION")
	private String massPosition;

	@Column(name="MASS_SHAPE")
	private String massShape;

	@Column(name="MASS_SIZE")
	private String massSize;

	@Column(name="MASS_TEMP")
	private String massTemp;

	private String membrane;

	@Column(name="MENSTRUAL_PATTERN1")
	private String menstrualPattern1;

	@Column(name="MENSTRUAL_PATTERN2")
	private String menstrualPattern2;

	@Column(name="MENSTRUAL_PAUSE")
	private String menstrualPause;

	private String murmurs;

	@Column(name="OBG_REMARKS")
	private String obgRemarks;

	@Column(name="OBSTRETIC_HISTORY")
	private String obstreticHistory;

	@Column(name="OBSTRETIC_SCORE_A")
	private Long obstreticScoreA;

	@Column(name="OBSTRETIC_SCORE_G")
	private Long obstreticScoreG;

	@Column(name="OBSTRETIC_SCORE_L")
	private Long obstreticScoreL;

	@Column(name="OBSTRETIC_SCORE_P")
	private Long obstreticScoreP;

	@Column(name="OPD_PATIENT_DETAILS_ID")
	private Long opdPatientDetailsId;

	@Column(name="OTHER_COMPLAINTS")
	private String otherComplaints;

	private String pa;

	private String palpation;

	@Column(name="PAP_SMEAR")
	private String papSmear;

	private String pe;

	private String pelvis;

	@Column(name="PERSONAL_HISTORY")
	private String personalHistory;

	@Column(name="POSITION_OF_CERVIX")
	private String positionOfCervix;

	private String presentation;

	private String pv;

	@Column(name="RANGE_INTERVAL")
	private String rangeInterval;

	@Column(name="RANGE_VALUE")
	private String rangeValue;

	@Column(name="RESPIRATORY_SYSTEM")
	private String respiratorySystem;

	private String s1;

	private String s2;

	private String specify;

	@Column(name="SPECULUM_CERVIX")
	private String speculumCervix;

	@Column(name="SPECULUM_DECENT")
	private String speculumDecent;

	@Column(name="SPECULUM_DISCHARGE")
	private String speculumDischarge;

	@Column(name="SPECULUM_VAGINA")
	private String speculumVagina;

	@Column(name="STATION_OF_PRESENTING_PART")
	private Long stationOfPresentingPart;

	private String sterilisation;

	@Column(name="SURGICAL_HISTORY")
	private String surgicalHistory;

	private String trimisters;

	private Long tt;

	@Column(name="UTERUS_CERVICAL_MOVEMENT")
	private String uterusCervicalMovement;

	@Column(name="UTERUS_FORNESS")
	private String uterusForness;

	@Column(name="UTERUS_SIZE")
	private String uterusSize;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	public FwcObjDetail() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getAgeOfMenarche() {
		return this.ageOfMenarche;
	}

	public void setAgeOfMenarche(Long ageOfMenarche) {
		this.ageOfMenarche = ageOfMenarche;
	}

	public String getAsicitis() {
		return this.asicitis;
	}

	public void setAsicitis(String asicitis) {
		this.asicitis = asicitis;
	}

	public String getBooked() {
		return this.booked;
	}

	public void setBooked(String booked) {
		this.booked = booked;
	}

	public String getBreadthAddedSounds() {
		return this.breadthAddedSounds;
	}

	public void setBreadthAddedSounds(String breadthAddedSounds) {
		this.breadthAddedSounds = breadthAddedSounds;
	}

	public String getClinicalHsirory() {
		return this.clinicalHsirory;
	}

	public void setClinicalHsirory(String clinicalHsirory) {
		this.clinicalHsirory = clinicalHsirory;
	}

	public String getConception() {
		return this.conception;
	}

	public void setConception(String conception) {
		this.conception = conception;
	}

	public String getConsanGuinity() {
		return this.consanGuinity;
	}

	public void setConsanGuinity(String consanGuinity) {
		this.consanGuinity = consanGuinity;
	}

	public String getConsistencyOfCervix() {
		return this.consistencyOfCervix;
	}

	public void setConsistencyOfCervix(String consistencyOfCervix) {
		this.consistencyOfCervix = consistencyOfCervix;
	}

	public String getCycleText() {
		return this.cycleText;
	}

	public void setCycleText(String cycleText) {
		this.cycleText = cycleText;
	}

	public String getCycles() {
		return this.cycles;
	}

	public void setCycles(String cycles) {
		this.cycles = cycles;
	}

	public Long getDilatationOfCervix() {
		return this.dilatationOfCervix;
	}

	public void setDilatationOfCervix(Long dilatationOfCervix) {
		this.dilatationOfCervix = dilatationOfCervix;
	}

	public String getDysmenorrhea() {
		return this.dysmenorrhea;
	}

	public void setDysmenorrhea(String dysmenorrhea) {
		this.dysmenorrhea = dysmenorrhea;
	}

	public String getEffacementOfCervix() {
		return this.effacementOfCervix;
	}

	public void setEffacementOfCervix(String effacementOfCervix) {
		this.effacementOfCervix = effacementOfCervix;
	}

	public String getExternalGenitalia() {
		return this.externalGenitalia;
	}

	public void setExternalGenitalia(String externalGenitalia) {
		this.externalGenitalia = externalGenitalia;
	}

	public String getExternalGenitaliaOther() {
		return this.externalGenitaliaOther;
	}

	public void setExternalGenitaliaOther(String externalGenitaliaOther) {
		this.externalGenitaliaOther = externalGenitaliaOther;
	}

	public String getFhr() {
		return this.fhr;
	}

	public void setFhr(String fhr) {
		this.fhr = fhr;
	}

	public String getFlow() {
		return this.flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getGc() {
		return this.gc;
	}

	public void setGc(String gc) {
		this.gc = gc;
	}

	public String getGynAbdomenInspection() {
		return this.gynAbdomenInspection;
	}

	public void setGynAbdomenInspection(String gynAbdomenInspection) {
		this.gynAbdomenInspection = gynAbdomenInspection;
	}

	public Long getGynAgeOfMenarche() {
		return this.gynAgeOfMenarche;
	}

	public void setGynAgeOfMenarche(Long gynAgeOfMenarche) {
		this.gynAgeOfMenarche = gynAgeOfMenarche;
	}

	public String getGynBimanualExamination() {
		return this.gynBimanualExamination;
	}

	public void setGynBimanualExamination(String gynBimanualExamination) {
		this.gynBimanualExamination = gynBimanualExamination;
	}

	public String getGynFlow() {
		return this.gynFlow;
	}

	public void setGynFlow(String gynFlow) {
		this.gynFlow = gynFlow;
	}

	public String getGynInternalExamination() {
		return this.gynInternalExamination;
	}

	public void setGynInternalExamination(String gynInternalExamination) {
		this.gynInternalExamination = gynInternalExamination;
	}

	public String getGynLocalExamination() {
		return this.gynLocalExamination;
	}

	public void setGynLocalExamination(String gynLocalExamination) {
		this.gynLocalExamination = gynLocalExamination;
	}

	public String getGynObstetricHistory() {
		return this.gynObstetricHistory;
	}

	public void setGynObstetricHistory(String gynObstetricHistory) {
		this.gynObstetricHistory = gynObstetricHistory;
	}

	public String getGynPalpation() {
		return this.gynPalpation;
	}

	public void setGynPalpation(String gynPalpation) {
		this.gynPalpation = gynPalpation;
	}

	public String getGynPerSpeculum() {
		return this.gynPerSpeculum;
	}

	public void setGynPerSpeculum(String gynPerSpeculum) {
		this.gynPerSpeculum = gynPerSpeculum;
	}

	public String getHead() {
		return this.head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public Long getHeartRateAbsent() {
		return this.heartRateAbsent;
	}

	public void setHeartRateAbsent(Long heartRateAbsent) {
		this.heartRateAbsent = heartRateAbsent;
	}

	public Long getHeartRateRegular() {
		return this.heartRateRegular;
	}

	public void setHeartRateRegular(Long heartRateRegular) {
		this.heartRateRegular = heartRateRegular;
	}

	public String getHeightOfUterus() {
		return this.heightOfUterus;
	}

	public void setHeightOfUterus(String heightOfUterus) {
		this.heightOfUterus = heightOfUterus;
	}

	public String getHopi() {
		return this.hopi;
	}

	public void setHopi(String hopi) {
		this.hopi = hopi;
	}

	public String getImmunised() {
		return this.immunised;
	}

	public void setImmunised(String immunised) {
		this.immunised = immunised;
	}

	public String getInspectionAbdomen() {
		return this.inspectionAbdomen;
	}

	public void setInspectionAbdomen(String inspectionAbdomen) {
		this.inspectionAbdomen = inspectionAbdomen;
	}

	public String getInspectionHernia() {
		return this.inspectionHernia;
	}

	public void setInspectionHernia(String inspectionHernia) {
		this.inspectionHernia = inspectionHernia;
	}

	public String getInspectionScar() {
		return this.inspectionScar;
	}

	public void setInspectionScar(String inspectionScar) {
		this.inspectionScar = inspectionScar;
	}

	public String getInspectionUmbilicus() {
		return this.inspectionUmbilicus;
	}

	public void setInspectionUmbilicus(String inspectionUmbilicus) {
		this.inspectionUmbilicus = inspectionUmbilicus;
	}

	public Date getLastMenstrual() {
		return this.lastMenstrual;
	}

	public void setLastMenstrual(Date lastMenstrual) {
		this.lastMenstrual = lastMenstrual;
	}

	public Long getLengthOfCervix() {
		return this.lengthOfCervix;
	}

	public void setLengthOfCervix(Long lengthOfCervix) {
		this.lengthOfCervix = lengthOfCervix;
	}

	public String getLiquor() {
		return this.liquor;
	}

	public void setLiquor(String liquor) {
		this.liquor = liquor;
	}

	public String getLowerGrip() {
		return this.lowerGrip;
	}

	public void setLowerGrip(String lowerGrip) {
		this.lowerGrip = lowerGrip;
	}

	public String getLowerPole() {
		return this.lowerPole;
	}

	public void setLowerPole(String lowerPole) {
		this.lowerPole = lowerPole;
	}

	public String getMarriedLife() {
		return this.marriedLife;
	}

	public void setMarriedLife(String marriedLife) {
		this.marriedLife = marriedLife;
	}

	public String getMassConsistency() {
		return this.massConsistency;
	}

	public void setMassConsistency(String massConsistency) {
		this.massConsistency = massConsistency;
	}

	public String getMassPalpable() {
		return this.massPalpable;
	}

	public void setMassPalpable(String massPalpable) {
		this.massPalpable = massPalpable;
	}

	public String getMassPosition() {
		return this.massPosition;
	}

	public void setMassPosition(String massPosition) {
		this.massPosition = massPosition;
	}

	public String getMassShape() {
		return this.massShape;
	}

	public void setMassShape(String massShape) {
		this.massShape = massShape;
	}

	public String getMassSize() {
		return this.massSize;
	}

	public void setMassSize(String massSize) {
		this.massSize = massSize;
	}

	public String getMassTemp() {
		return this.massTemp;
	}

	public void setMassTemp(String massTemp) {
		this.massTemp = massTemp;
	}

	public String getMembrane() {
		return this.membrane;
	}

	public void setMembrane(String membrane) {
		this.membrane = membrane;
	}

	public String getMenstrualPattern1() {
		return this.menstrualPattern1;
	}

	public void setMenstrualPattern1(String menstrualPattern1) {
		this.menstrualPattern1 = menstrualPattern1;
	}

	public String getMenstrualPattern2() {
		return this.menstrualPattern2;
	}

	public void setMenstrualPattern2(String menstrualPattern2) {
		this.menstrualPattern2 = menstrualPattern2;
	}

	public String getMenstrualPause() {
		return this.menstrualPause;
	}

	public void setMenstrualPause(String menstrualPause) {
		this.menstrualPause = menstrualPause;
	}

	public String getMurmurs() {
		return this.murmurs;
	}

	public void setMurmurs(String murmurs) {
		this.murmurs = murmurs;
	}

	public String getObgRemarks() {
		return this.obgRemarks;
	}

	public void setObgRemarks(String obgRemarks) {
		this.obgRemarks = obgRemarks;
	}

	public String getObstreticHistory() {
		return this.obstreticHistory;
	}

	public void setObstreticHistory(String obstreticHistory) {
		this.obstreticHistory = obstreticHistory;
	}

	public Long getObstreticScoreA() {
		return this.obstreticScoreA;
	}

	public void setObstreticScoreA(Long obstreticScoreA) {
		this.obstreticScoreA = obstreticScoreA;
	}

	public Long getObstreticScoreG() {
		return this.obstreticScoreG;
	}

	public void setObstreticScoreG(Long obstreticScoreG) {
		this.obstreticScoreG = obstreticScoreG;
	}

	public Long getObstreticScoreL() {
		return this.obstreticScoreL;
	}

	public void setObstreticScoreL(Long obstreticScoreL) {
		this.obstreticScoreL = obstreticScoreL;
	}

	public Long getObstreticScoreP() {
		return this.obstreticScoreP;
	}

	public void setObstreticScoreP(Long obstreticScoreP) {
		this.obstreticScoreP = obstreticScoreP;
	}

	public Long getOpdPatientDetailsId() {
		return this.opdPatientDetailsId;
	}

	public void setOpdPatientDetailsId(Long opdPatientDetailsId) {
		this.opdPatientDetailsId = opdPatientDetailsId;
	}

	public String getOtherComplaints() {
		return this.otherComplaints;
	}

	public void setOtherComplaints(String otherComplaints) {
		this.otherComplaints = otherComplaints;
	}

	public String getPa() {
		return this.pa;
	}

	public void setPa(String pa) {
		this.pa = pa;
	}

	public String getPalpation() {
		return this.palpation;
	}

	public void setPalpation(String palpation) {
		this.palpation = palpation;
	}

	public String getPapSmear() {
		return this.papSmear;
	}

	public void setPapSmear(String papSmear) {
		this.papSmear = papSmear;
	}

	public String getPe() {
		return this.pe;
	}

	public void setPe(String pe) {
		this.pe = pe;
	}

	public String getPelvis() {
		return this.pelvis;
	}

	public void setPelvis(String pelvis) {
		this.pelvis = pelvis;
	}

	public String getPersonalHistory() {
		return this.personalHistory;
	}

	public void setPersonalHistory(String personalHistory) {
		this.personalHistory = personalHistory;
	}

	public String getPositionOfCervix() {
		return this.positionOfCervix;
	}

	public void setPositionOfCervix(String positionOfCervix) {
		this.positionOfCervix = positionOfCervix;
	}

	public String getPresentation() {
		return this.presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

	public String getPv() {
		return this.pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getRangeInterval() {
		return this.rangeInterval;
	}

	public void setRangeInterval(String rangeInterval) {
		this.rangeInterval = rangeInterval;
	}

	public String getRangeValue() {
		return this.rangeValue;
	}

	public void setRangeValue(String rangeValue) {
		this.rangeValue = rangeValue;
	}

	public String getRespiratorySystem() {
		return this.respiratorySystem;
	}

	public void setRespiratorySystem(String respiratorySystem) {
		this.respiratorySystem = respiratorySystem;
	}

	public String getS1() {
		return this.s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return this.s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	public String getSpecify() {
		return this.specify;
	}

	public void setSpecify(String specify) {
		this.specify = specify;
	}

	public String getSpeculumCervix() {
		return this.speculumCervix;
	}

	public void setSpeculumCervix(String speculumCervix) {
		this.speculumCervix = speculumCervix;
	}

	public String getSpeculumDecent() {
		return this.speculumDecent;
	}

	public void setSpeculumDecent(String speculumDecent) {
		this.speculumDecent = speculumDecent;
	}

	public String getSpeculumDischarge() {
		return this.speculumDischarge;
	}

	public void setSpeculumDischarge(String speculumDischarge) {
		this.speculumDischarge = speculumDischarge;
	}

	public String getSpeculumVagina() {
		return this.speculumVagina;
	}

	public void setSpeculumVagina(String speculumVagina) {
		this.speculumVagina = speculumVagina;
	}

	public Long getStationOfPresentingPart() {
		return this.stationOfPresentingPart;
	}

	public void setStationOfPresentingPart(Long stationOfPresentingPart) {
		this.stationOfPresentingPart = stationOfPresentingPart;
	}

	public String getSterilisation() {
		return this.sterilisation;
	}

	public void setSterilisation(String sterilisation) {
		this.sterilisation = sterilisation;
	}

	public String getSurgicalHistory() {
		return this.surgicalHistory;
	}

	public void setSurgicalHistory(String surgicalHistory) {
		this.surgicalHistory = surgicalHistory;
	}

	public String getTrimisters() {
		return this.trimisters;
	}

	public void setTrimisters(String trimisters) {
		this.trimisters = trimisters;
	}

	public Long getTt() {
		return this.tt;
	}

	public void setTt(Long tt) {
		this.tt = tt;
	}

	public String getUterusCervicalMovement() {
		return this.uterusCervicalMovement;
	}

	public void setUterusCervicalMovement(String uterusCervicalMovement) {
		this.uterusCervicalMovement = uterusCervicalMovement;
	}

	public String getUterusForness() {
		return this.uterusForness;
	}

	public void setUterusForness(String uterusForness) {
		this.uterusForness = uterusForness;
	}

	public String getUterusSize() {
		return this.uterusSize;
	}

	public void setUterusSize(String uterusSize) {
		this.uterusSize = uterusSize;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	
	
	@Column(name="LIE")
	private String lie;
	
	@Column(name="PRESENTING_PART")
	private String presentingPart;

	public String getLie() {
		return lie;
	}

	public void setLie(String lie) {
		this.lie = lie;
	}

	public String getPresentingPart() {
		return presentingPart;
	}

	public void setPresentingPart(String presentingPart) {
		this.presentingPart = presentingPart;
	}
	
	@Column(name="VISIT_ID")
	private Long visitId;
	
	@Column(name="PATIENT_ID")
	private Long patientId;
	
	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	
	
	

}