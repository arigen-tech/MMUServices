package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 


/**
 * The persistent class for the MAS_MEDICAL_EXAM_REPORT database table.
 * 
 */
@Entity
@Table(name="MAS_MEDICAL_EXAM_REPORT")
@NamedQuery(name="MasMedicalExamReport.findAll", query="SELECT m FROM MasMedicalExamReport m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MEDICAL_EXAM_REPORT_SEQ", sequenceName="MAS_MEDICAL_EXAM_REPORT_SEQ",allocationSize=1)
public class MasMedicalExamReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="MAS_MEDICAL_EXAM_REPORT_MEDICALEXAMINATIONID_GENERATOR", sequenceName="MAS_MEDICAL_EXAM_REPORT_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_MEDICAL_EXAM_REPORT_SEQ")
	@Column(name="MEDICAL_EXAMINATION_ID")
	private Long medicalExaminationId;

	@Column(name="AA_FINAL_OBSERVATION")
	private String aaFinalObservation;

	@Column(name="AA_REMARKS")
	private String aaRemarks;

	@Column(name="AA_SENDTO")
	private String aaSendto;

	@Column(name="AA_SIGNEDBY")
	private String aaSignedby;

	private String abdomen;

	@Column(name="ABNORMAL_TRAILS")
	private String abnormalTrails;

	private String abnormalities;

	private String abortion;

	@Temporal(TemporalType.DATE)
	@Column(name="ACCEPT_DATE")
	private Date acceptDate;

	@Column(name="ACCEPT_REMARKS")
	private String acceptRemarks;

	@Column(name="ACCEPT_SIGNEDBY")
	private String acceptSignedby;

	private Long acceptable;

	@Column(name="ACCOMMODATION_L")
	private String accommodationL;

	@Column(name="ACCOMMODATION_R")
	private String accommodationR;

	private String actualweight;

	@Column(name="ADMINISTRATIVE_PROFILE_A")
	private String administrativeProfileA;

	@Column(name="ADMINISTRATIVE_PROFILE_B")
	private String administrativeProfileB;

	@Column(name="ADMINISTRATIVE_PROFILE_C")
	private String administrativeProfileC;

	@Column(name="ADMISSION_STATUS")
	private String admissionStatus;

	@Column(name="ADMITTED_IN_HOSPITAL_FOR_ANY_I")
	private String admittedInHospitalForAnyI;

	@Temporal(TemporalType.DATE)
	private Date afrodate;

	@Column(name="AGGRAV_MATERIAL_PERIOD")
	private String aggravMaterialPeriod;

	@Column(name="AGGRAVATED_MISCONDUCT")
	private String aggravatedMisconduct;

	private String aggravatedservice;

	private String aggravatedservicedesc;

	@Column(name="AIR_SEA_CAR_TRAIN_SICKNESS")
	private String airSeaCarTrainSickness;

	@Temporal(TemporalType.DATE)
	private Date airdate;

	private String albumin;

	@Column(name="ALCOHOL_DRUG_RELATED")
	private String alcoholDrugRelated;

	private String allergies;

	@Column(name="ALOCOHOL_STATUS")
	private String alocoholStatus;

	@Column(name="AMENORRHOEA_DYSMENONHOEA")
	private String amenorrhoeaDysmenonhoea;

	@Column(name="ANY_EVIDENCE_OF_SKIN")
	private String anyEvidenceOfSkin;

	@Column(name="ANY_OTHE_ABNORMALITIES")
	private String anyOtheAbnormalities;

	@Column(name="ANY_OTHER_EAR_DISEASE")
	private String anyOtherEarDisease;

	@Column(name="ANY_OTHER_EYE_DISEASE")
	private String anyOtherEyeDisease;

	@Column(name="ANY_OTHER_INFORMATION_ABOUT_YO")
	private String anyOtherInformationAboutYo;

	@Column(name="ANY_OTHER_INV_CARRIED_OUT")
	private String anyOtherInvCarriedOut;

	@Column(name="ANY_OTHER_RELEVANT_INFORMATION")
	private String anyOtherRelevantInformation;

	@Temporal(TemporalType.DATE)
	@Column(name="APP_AUTH_DATE")
	private Date appAuthDate;

	@Column(name="APPARENT_AGE")
	private String apparentAge;

	private String appearance;

	@Temporal(TemporalType.DATE)
	@Column(name="APPOINTMENT_DATE")
	private Date appointmentDate;

	@Column(name="APPOINTMENT_TIME")
	private String appointmentTime;

	@Column(name="APPROVED_BY")
	private String approvedBy;

	@Column(name="APPROVING_AUTHORITY")
	private String approvingAuthority;

	@Column(name="ARMS_CORPS")
	private String armsCorps;

	@Column(name="ARTERIAL_WALLS")
	private String arterialWalls;

	@Column(name="ATTENDENT_MONTH")
	private String attendentMonth;

	@Column(name="ATTENDENT_YEAR")
	private String attendentYear;

	@Column(name="AUDIOMETRY_RECORD")
	private String audiometryRecord;

	private String authority;

	@Column(name="BATCH_NO")
	private String batchNo;

	@Column(name="BEENREJECTED_AS_MEDICALLY_UNFI")
	private String beenrejectedAsMedicallyUnfi;

	private String bhi;

	@Column(name="BINOCULAR_VISION_GRADE")
	private String binocularVisionGrade;

	@Column(name="BLEEDING_DISORDER")
	private String bleedingDisorder;

	private String boardmember;

	private String boardpresident;

	private String bodyfat;

	private String bp;

	@Column(name="BRANCH_ID")
	private Long branchId;

	@Column(name="BREAST_DISEASE_DISCHARGE")
	private String breastDiseaseDischarge;

	private String breasts;

	@Column(name="CARDIOVASCULAR_SYSTEM")
	private String cardiovascularSystem;

	private String casedetail;

	private String casesheet;

	@Temporal(TemporalType.DATE)
	private Date categorydate;

	private String categoryplace;

	@Temporal(TemporalType.DATE)
	private Date ceaseduty;

	@Column(name="CENTRAL_NERVOUS_SYSTEM")
	private String centralNervousSystem;

	@Column(name="CENTRAL_NERVOUS_SYSTEM_MMHG")
	private String centralNervousSystemMmhg;

	private String cervical;

	@Column(name="CHEST_MEASUREMENT")
	private String chestMeasurement;

	@Column(name="CHEST_NO")
	private String chestNo;

	private String chestfullexpansion;

	private String cholesterol;

	@Column(name="CHRONIC_BRONCHITIS")
	private String chronicBronchitis;

	@Column(name="CHRONIC_INDIGESTION")
	private String chronicIndigestion;

	private String clamingdisability;

	@Column(name="CLINICAL_OPD_DEPT")
	private Long clinicalOpdDept;

	@Column(name="CLINICAL_REFER_MH")
	private String clinicalReferMh;

	@Column(name="CM_USER_ID")
	private Long cmUserId;

	@Column(name="COMD_OFFICER_RANK")
	private String comdOfficerRank;

	@Column(name="COMD_OFFICER_UNIT")
	private String comdOfficerUnit;

	@Column(name="COMMAND_ID")
	private Long commandId;

	@Column(name="COMMAND_REMARKS")
	private String commandRemarks;

	@Temporal(TemporalType.DATE)
	private Date commanddate;

	private String commandingofficer;

	@Temporal(TemporalType.DATE)
	private Date commandingofficerdate;

	@Column(name="COMPLIANCE_WITH_TREATMENT")
	private String complianceWithTreatment;

	private String conditionofgums;

	@Temporal(TemporalType.DATE)
	@Column(name="CONFIRM_DATE")
	private Date confirmDate;

	@Column(name="CONFIRM_REMARKS")
	private String confirmRemarks;

	@Column(name="CONFIRM_SIGNEDBY")
	private String confirmSignedby;

	@Column(name="CONVERGENCE_C")
	private String convergenceC;

	@Column(name="CONVERGENCE_SC")
	private String convergenceSc;

	@Column(name="CORONORY_RISK_FACTOR")
	private String coronoryRiskFactor;

	@Column(name="COURSE_OF_ILLNESS")
	private String courseOfIllness;

	@Column(name="COVER_TEST")
	private String coverTest;

	@Column(name="DATA_OF_NURVE_HIC")
	private String dataOfNurveHic;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_APPROVING_AUTHORITY")
	private Date dateApprovingAuthority;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_MEDICAL_BOARD_EXAM")
	private Date dateMedicalBoardExam;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_MEDICAL_BOARD_SUBSEQUENT")
	private Date dateMedicalBoardSubsequent;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_COMPLETION")
	private Date dateOfCompletion;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_DISCHARGE")
	private Date dateOfDischarge;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_LEAVE")
	private Date dateOfLeave;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_PERUSING")
	private Date dateOfPerusing;

	@Column(name="DATE_OF_POSTING")
	private String dateOfPosting;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_POSTING_IN")
	private Date dateOfPostingIn;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_POSTING_OUT")
	private Date dateOfPostingOut;

	@Column(name="DATE_OF_POSTING_OUT_SPECIFY")
	private String dateOfPostingOutSpecify;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_REPORTING")
	private Date dateOfReporting;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_SPECIAL_EXAM")
	private Date dateSpecialExam;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_TEATH")
	private Date dateTeath;

	@Temporal(TemporalType.DATE)
	private Date dateapproximate;

	@Temporal(TemporalType.DATE)
	private Date datedischarge;

	@Temporal(TemporalType.DATE)
	private Date datedisease;

	@Temporal(TemporalType.DATE)
	private Date dateofcommun;

	@Temporal(TemporalType.DATE)
	private Date daterelease;

	@Temporal(TemporalType.DATE)
	private Date datesoflastleave;

	@Temporal(TemporalType.DATE)
	private Date datetheindividual;

	@Temporal(TemporalType.DATE)
	private Date datevalidated;

	@Column(name="DEFECT_NOT_TO_CAUSE_REJECTION")
	private String defectNotToCauseRejection;

	@Column(name="DENSTL_POINT")
	private String denstlPoint;

	@Temporal(TemporalType.DATE)
	@Column(name="DENTAL_CHECKUP_DATE")
	private Date dentalCheckupDate;

	@Column(name="DENTAL_OFFICER")
	private String dentalOfficer;

	private String dentalvalue;

	private String diabetes;

	@Column(name="DIAG_NO")
	private String diagNo;

	@Column(name="DIAG_NO_FOR_RADIO")
	private String diagNoForRadio;

	private String diagnosis;

	@Column(name="DIAGNOSIS_1_CLINI")
	private String diagnosis1Clini;

	@Column(name="DIAGNOSIS_2_CLINI")
	private String diagnosis2Clini;

	@Column(name="DIAPHRAGM_TEST")
	private String diaphragmTest;

	@Temporal(TemporalType.DATE)
	@Column(name="DIGITAL_DATE")
	private Date digitalDate;

	@Column(name="DIS_ONSET")
	private String disOnset;

	private String disability;

	@Column(name="DISABILITY_OTHER_CASE")
	private String disabilityOtherCase;

	private String disabilityattribute;

	private String disabilitybefore;

	@Column(name="DISABLE_AGGRAV_PERSIST")
	private String disableAggravPersist;

	@Column(name="DISCHARGE_AS_MEDICALLY_UNFIT_F")
	private String dischargeAsMedicallyUnfitF;

	@Column(name="DISCHARGE_FROM_EARS")
	private String dischargeFromEars;

	@Column(name="DISCIPLINARY_RECORD")
	private String disciplinaryRecord;

	@Column(name="DISEASE_SURROUNDING_AREAS")
	private String diseaseSurroundingAreas;

	@Column(name="DISSENT_NOTE")
	private String dissentNote;

	private String dlc;

	@Column(name="DOCTOR_ADVICE_FLAG")
	private String doctorAdviceFlag;

	@Temporal(TemporalType.DATE)
	@Column(name="DOCUMENT_FORWARD_DATE")
	private Date documentForwardDate;

	@Column(name="DOCUMENT_FORWARD_TO")
	private String documentForwardTo;

	@Column(name="DORSAL_VERTEBRATE")
	private String dorsalVertebrate;

	private String drinker;

	@Column(name="DUTY_EXCUSED")
	private String dutyExcused;

	@Temporal(TemporalType.DATE)
	@Column(name="EAR_DATE")
	private Date earDate;

	@Column(name="EAR_HEARING_BOTH_FW")
	private Long earHearingBothFw;

	@Column(name="EAR_HEARING_LFW")
	private Long earHearingLfw;

	@Column(name="EAR_HEARING_RFW")
	private Long earHearingRfw;

	private String ecg;

	private String elbows;

	@Column(name="EMPABILTY_RESTRIC")
	private String empabiltyRestric;

	@Column(name="ENDOCRINE_CONDITION")
	private String endocrineCondition;

	@Temporal(TemporalType.DATE)
	@Column(name="ENTRY_DATE")
	private Date entryDate;

	private String esr;

	@Column(name="ESSENTIAL_INSTABILITY")
	private String essentialInstability;

	@Column(name="EVIDENCE_OF_TRACHOMA")
	private String evidenceOfTrachoma;

	@Column(name="EXTERNAL_EAR_L")
	private String externalEarL;

	@Column(name="EXTERNAL_EAR_R")
	private String externalEarR;

	@Column(name="FAMILY_LIVING")
	private String familyLiving;

	@Column(name="FAMILY_LIVING_IN")
	private String familyLivingIn;

	@Column(name="FATHER_NAME")
	private String fatherName;

	private String fields;

	private String finalobservation;

	private String fingers;

	@Column(name="FITS_FAINTING_ATTACK")
	private String fitsFaintingAttack;

	@Column(name="FLAT_FEET")
	private String flatFeet;

	@Temporal(TemporalType.DATE)
	@Column(name="FLC_DATE")
	private Date flcDate;

	private String fmdm;

	@Column(name="FOLLOWUP_TREATMENT")
	private String followupTreatment;

	@Column(name="FORWARD_MO")
	private Long forwardMo;

	@Column(name="FOUND_FIT_IN_CATEGORY")
	private String foundFitInCategory;

	@Column(name="FREQUENT_COUGH_COLD_SINUSITIS")
	private String frequentCoughColdSinusitis;

	@Column(name="FROM_WHERE_HE_REPORT")
	private String fromWhereHeReport;

	@Column(name="FULL_EXPIRATION")
	private String fullExpiration;

	@Column(name="FUND_AND_MEDIA")
	private String fundAndMedia;

	private String gail;

	@Column(name="GASTRO_INTESTINAL_SYSTEM")
	private String gastroIntestinalSystem;

	@Column(name="GENERAL_EXAM")
	private String generalExam;

	@Column(name="GENERAL_OUTLOOK")
	private String generalOutlook;

	@Column(name="GENERAL_PHYSICAL_EXAM")
	private String generalPhysicalExam;

	@Column(name="GENITO_URIRARY_PERINEUM")
	private String genitoUriraryPerineum;

	@Temporal(TemporalType.DATE)
	@Column(name="GYNAECOLOGY_DATE")
	private Date gynaecologyDate;

	private String hand;

	private String hb;

	@Column(name="HB_PERCENTAGE")
	private String hbPercentage;

	private String hdl;

	@Column(name="HEARING_BOTH_CV")
	private Long hearingBothCv;

	@Column(name="HEARING_LCV")
	private Long hearingLcv;

	@Column(name="HEARING_RCV")
	private Long hearingRcv;

	@Column(name="HEART_DIABETES")
	private String heartDiabetes;

	@Column(name="HEART_SIZE")
	private String heartSize;

	private String height;

	private String hemorrhoids;

	@Column(name="HERNIA_MUSIC")
	private String herniaMusic;

	@Column(name="HIC_STATUS")
	private String hicStatus;

	@Column(name="HIGHER_MENTAL_FUNCTION")
	private String higherMentalFunction;

	private String hips;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="HOSPITAL_STD_CENTER")
	private String hospitalStdCenter;

	@Column(name="HOURS_OF_FLOWN")
	private String hoursOfFlown;

	@Temporal(TemporalType.DATE)
	private Date howlonghashe;

	private String hullux;

	private String hydrocele;

	private String hypertension;

	private String idealweight;

	@Column(name="IDENTIFICATION_MARKS1")
	private String identificationMarks1;

	@Column(name="IDENTIFICATION_MARKS2")
	private String identificationMarks2;

	@Column(name="IN_WAY")
	private String inWay;

	@Column(name="IN_WAY2")
	private String inWay2;

	@Column(name="INCIDENTS_DURING_YOUR_SERVICE")
	private String incidentsDuringYourService;

	@Column(name="INDIVIDUAL_ATTENDENT")
	private String individualAttendent;

	@Column(name="INDIVIDUAL_DIGITAL_SIGN")
	private String individualDigitalSign;

	@Column(name="INDIVIDUAL_FURTHER_TRATMENT")
	private String individualFurtherTratment;

	@Column(name="INDIVIDUAL_MISCONDUCT")
	private String individualMisconduct;

	@Column(name="INDIVIDUAL_NATURE")
	private String individualNature;

	@Column(name="INDIVIDUAL_REASON")
	private String individualReason;

	@Column(name="INDIVIDUAL_REFUSAL_TRATMENT")
	private String individualRefusalTratment;

	@Column(name="INFECTION_DISABILITY")
	private String infectionDisability;

	@Column(name="INJURY_REPORT")
	private String injuryReport;

	@Column(name="INNER_EAR_L")
	private String innerEarL;

	@Column(name="INNER_EAR_R")
	private String innerEarR;

	private String instructionbypresident;

	private String investigated;

	private String investigation;

	private String jaundice;

	private String joints;

	@Column(name="KIDNEY_BLADDER_TROUBLE")
	private String kidneyBladderTrouble;

	@Column(name="LASER_TREATEMENT_SURGERY_FOR_E")
	private String laserTreatementSurgeryForE;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_BOARD_DATE")
	private Date lastBoardDate;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHANGED_DATE")
	private Date lastChangedDate;

	@Column(name="LAST_CHANGED_TIME")
	private String lastChangedTime;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CONFINEMENT_DATE")
	private Date lastConfinementDate;

	@Column(name="LAST_REVIEW")
	private String lastReview;

	private String lastame;

	private String ldl;

	@Column(name="LEG_LENGTH")
	private String legLength;

	//private String liver;

	@Column(name="LIVING_UNIT_LINES")
	private String livingUnitLines;

	@Column(name="LMC_MONTH")
	private Long lmcMonth;

	@Column(name="LMC_YEAR")
	private Long lmcYear;

	@Temporal(TemporalType.DATE)
	private Date lmp;

	private String localexamination;

	@Column(name="LOCOMOTER_SYSTEM")
	private String locomoterSystem;

	private String locomotion;

	@Column(name="LOW_MED_CAT")
	private String lowMedCat;

	private String lumber;

	@Column(name="M_T_LL")
	private String mTLl;

	@Column(name="M_T_LR")
	private String mTLr;

	@Column(name="M_T_UL")
	private String mTUl;

	@Column(name="M_T_UR")
	private String mTUr;

	@Column(name="MA_USER_ID")
	private Long maUserId;

	@Column(name="MANIFEST_HYPERMETROPIA")
	private String manifestHypermetropia;

	@Column(name="MARITAL_STATUS")
	private Long maritalStatus;

	@Column(name="MB_FIELD")
	private String mbField;

	@Column(name="MD_REMARKS")
	private String mdRemarks;

	@Column(name="MD_USER_ID")
	private Long mdUserId;

	@Column(name="MED_BOARD_DURATION")
	private Long medBoardDuration;

	@Column(name="MED_BOARD_REMARKS")
	private String medBoardRemarks;

	@Column(name="MED_CAT_RELEASE")
	private Long medCatRelease;

	@Column(name="MED_COMPOSITE")
	private String medComposite;

	@Temporal(TemporalType.DATE)
	@Column(name="MED_DETAIL_DATE")
	private Date medDetailDate;

	@Column(name="MED_DETAIL_MEMBER1")
	private Long medDetailMember1;

	@Column(name="MED_DETAIL_MEMBER2")
	private Long medDetailMember2;

	@Column(name="MED_DETAIL_PRESIDENT")
	private Long medDetailPresident;

	@Column(name="MED_EXAM_CARRY_TIME")
	private String medExamCarryTime;

	@Column(name="MED_INSTRUCTIONS")
	private String medInstructions;

	@Column(name="MED_REMARKS")
	private String medRemarks;

	@Column(name="MEDBOARD_HELD")
	private String medboardHeld;

	private String medcatrec;

	@Column(name="MEDICAL_BOARD_FINDINGS")
	private String medicalBoardFindings;

	@Column(name="MEDICAL_BOARD_SUBSEQUENT_FIND")
	private String medicalBoardSubsequentFind;

	@Column(name="MEDICAL_CATEGORY_RECOMMEND")
	private Long medicalCategoryRecommend;

	@Column(name="MEDICAL_EXAM_HELD_AT")
	private Long medicalExamHeldAt;

	@Column(name="MEDICAL_STATUS")
	private String medicalStatus;

	

	private String medicalexamtype;

	private String medicaltype;

	private String medication;

	@Temporal(TemporalType.DATE)
	@Column(name="MEDICE_EXAM_DATE")
	private Date mediceExamDate;

	private String menonhagia;

	@Column(name="MENSTRUAL_HISTORY")
	private String menstrualHistory;

	@Column(name="MENTAL_DISEASE")
	private String mentalDisease;

	@Column(name="MENTAL_INSTABILITY")
	private String mentalInstability;

	@Column(name="MIDDLE_EAR")
	private String middleEar;

	@Column(name="MIDDLE_EAR_R")
	private String middleEarR;

	@Column(name="MISSING_TEETH")
	private String missingTeeth;

	private String missteeth;

	@Column(name="MO_USER_ID")
	private Long moUserId;

	private String mobilityl;

	private String mobilityr;

	@Temporal(TemporalType.DATE)
	private Date modate;

	@Column(name="MONTHLY_SERIAL_NO")
	private String monthlySerialNo;

	private String motivation;

	@Column(name="NAME_IN_FULL")
	private String nameInFull;

	@Column(name="NATURE_DUTIES")
	private String natureDuties;

	@Column(name="NEAR_VISION_WITH_GLASS_CP")
	private String nearVisionWithGlassCp;

	@Column(name="NEAR_VISION_WITHOUT_GLASS_CP")
	private String nearVisionWithoutGlassCp;

	@Column(name="NERVOUS_BREAKDOWN_MENTAL_ILLNE")
	private String nervousBreakdownMentalIllne;

	@Temporal(TemporalType.DATE)
	@Column(name="NEXT_BOARD_DATE")
	private Date nextBoardDate;

	@Column(name="NIGHT_BINDNESS")
	private String nightBindness;

	@Column(name="NIGHT_BLINDNESS")
	private String nightBlindness;

	@Column(name="NIGHT_VISUAL_CAPACITY")
	private String nightVisualCapacity;

	@Column(name="NO_OF_ABORTIONS")
	private Long noOfAbortions;

	@Column(name="NO_OF_CHILDREN")
	private Long noOfChildren;

	@Column(name="NO_OF_PREGNANCIES")
	private Long noOfPregnancies;

	private String nose;

	@Column(name="NOSE_THROAT_SINUSES")
	private String noseThroatSinuses;

	private String nosethroat;

	private String onset;

	@Column(name="ONSET_DIAG1")
	private String onsetDiag1;

	@Column(name="ONSET_DIAG2")
	private String onsetDiag2;

	@Column(name="OPERA_TREATMENT_RECOMMEND")
	private String operaTreatmentRecommend;

	@Column(name="OPERATION_TREAT_CURED")
	private String operationTreatCured;

	private String operational;

	@Column(name="OPERN_TREAT_CURED_ENTRY")
	private String opernTreatCuredEntry;

	private String opinion;

	@Temporal(TemporalType.DATE)
	private Date opiniondate;

	private String otherinformation;

	private String others;

	private String overweight;

	@Column(name="P_NO")
	private String pNo;

	@Column(name="PA_AFRO_FINAL_OBSER")
	private String paAfroFinalObser;

	@Column(name="PA_AFRO_REMARKS")
	private String paAfroRemarks;

	@Column(name="PA_AFRO_SIGNEDBY")
	private String paAfroSignedby;

	@Column(name="PA_FINALOBSERVATION")
	private String paFinalobservation;

	@Column(name="PA_REMARKS")
	private String paRemarks;

	@Column(name="PA_SENDTO")
	private String paSendto;

	@Column(name="PA_SIGNEDBY")
	private String paSignedby;

	@Column(name="PARMANENT_ADDRESS")
	private String parmanentAddress;

	private String particularofpreviousservice;

	@Column(name="PAST_MED_PERIOD")
	private String pastMedPeriod;

	private Long pastdisability;

	private Long pastmedicalcategory;

	private String pastmedicalhistory;

	@Column(name="PATIENT_ID")
	private Long patientId;

	private String patientheight;

	private String patientweight;

	private String pelvis;

	@Column(name="PENDING_DISCIPLINARY_CASES")
	private String pendingDisciplinaryCases;

	@Temporal(TemporalType.DATE)
	@Column(name="PER_AUTH_DATE")
	private Date perAuthDate;

	@Column(name="PERCENTAGE_DISABLE")
	private String percentageDisable;

	@Column(name="PERCENTAGE_DISABLE_TREATMENT")
	private String percentageDisableTreatment;

	@Column(name="PERFORMATION_UNDER_STRESS")
	private String performationUnderStress;

	private String period;

	private String permanentaddress;

	private String personalans;

	private String physique;

	private String place;

	@Column(name="PLACE_APPROVING_AUTHORITY")
	private Long placeApprovingAuthority;

	@Column(name="PLACE_LAST_CAT_BOARD")
	private String placeLastCatBoard;

	@Column(name="PLACE_MEDICAL_BOARD_EXAM")
	private Long placeMedicalBoardExam;

	@Column(name="PLACE_MEDICAL_BOARD_SUBSEQUENT")
	private Long placeMedicalBoardSubsequent;

	@Column(name="PLACE_NEXT_CAT_BOARD")
	private String placeNextCatBoard;

	private String placedisease;

	private String pleurisy;

	private String pregnancy;

	@Column(name="PRESENT_MED_PERIOD")
	private String presentMedPeriod;

	private String presentcondition;

	private String presentconditionin;

	private Long presentdisability;

	private String presentdisablement;

	private Long presentmedicalcategory;

	private String presentmedicalhistory;

	@Column(name="PREVENT_MEASURE")
	private String preventMeasure;

	private String previousdisablement;

	@Column(name="PRIOR_ABNORMAL_SPECIFY")
	private String priorAbnormalSpecify;

	@Column(name="PRIOR_TO_ONSET")
	private String priorToOnset;

	private Long priority;

	private String professional;

	private String prolapse;

	@Column(name="PULSE_RATES")
	private String pulseRates;

	@Column(name="RANGE_OF_EXPENSION")
	private String rangeOfExpension;

	private String rangeofexpansion;

	@Column(name="RANK_ID")
	private Long rankId;

	@Column(name="REASON_WOUND_INJURY")
	private String reasonWoundInjury;

	private String reasopnsvariation;

	@Column(name="REC_MED_PERIOD")
	private String recMedPeriod;

	private String recordoffice;

	@Column(name="REDUC_DISABLE_PENSION_ENTRY")
	private String reducDisablePensionEntry;

	@Column(name="REDUCTION_DISABLE_PENSION")
	private String reductionDisablePension;

	private String refertomh;

	private String reflexes;

	@Column(name="REFUSE_OPERATION_TREAT")
	private String refuseOperationTreat;

	private String regimental;

	@Column(name="REGIMENTAL_OTHERS")
	private String regimentalOthers;

	@Temporal(TemporalType.DATE)
	@Column(name="REJECT_DATE")
	private Date rejectDate;

	@Column(name="REJECT_STATUS")
	private String rejectStatus;

	@Column(name="RELEASE_MED_CAT_VALUE")
	private String releaseMedCatValue;

	@Column(name="RELEASE_MED_PERIOD")
	private String releaseMedPeriod;

	@Column(name="RELEASE_SHAPE_FACTOR")
	private String releaseShapeFactor;

	@Column(name="RELEVANT_FAMILY_HISTORY")
	private String relevantFamilyHistory;

	private String remarks;

	@Column(name="REMARKS__LOWERLIMBS")
	private String remarksLowerlimbs;

	@Column(name="REMARKS_CLINICAL")
	private String remarksClinical;

	@Column(name="REMARKS_EAR")
	private String remarksEar;

	@Column(name="REMARKS_GYNAECOLOGY")
	private String remarksGynaecology;

	@Column(name="REMARKS_SPECIAL_EXAM")
	private String remarksSpecialExam;

	@Column(name="REMARKS_TEATH")
	private String remarksTeath;

	@Column(name="RESPIRATORY_SYSTEM")
	private String respiratorySystem;

	@Column(name="RESPONSIBLITY_FACTOR")
	private String responsiblityFactor;

	private String restrictionemployment;

	@Column(name="RHEUMATISM_FREQUENT_SORETHROAT")
	private String rheumatismFrequentSorethroat;

	private String rhythm;

	private String rigigus;

	@Column(name="ROCCYX_VAROCOSE")
	private String roccyxVarocose;

	@Column(name="ROLL_NO")
	private String rollNo;

	@Column(name="RR_CLINICAL")
	private String rrClinical;

	private String sd;

	@Column(name="SELF_BALANCING_L")
	private String selfBalancingL;

	@Column(name="SELF_BALANCING_R")
	private String selfBalancingR;

	@Column(name="SELF_BALANCING_TEST")
	private String selfBalancingTest;

	private String sendto;

	@Column(name="SERVICE_TYPE_ID")
	private Long serviceTypeId;

	private String serviceiaf;

	private String serviceno;

	@Column(name="SEVERE_EXCEP_STRESS")
	private String severeExcepStress;

	@Column(name="SEVERE_HEAD_INJURY")
	private String severeHeadInjury;

	@Column(name="SHAP_FACTOR")
	private String shapFactor;

	@Column(name="SHAPE_FACTOR_PAST")
	private String shapeFactorPast;

	@Column(name="SHAPE_FACTOR_REC")
	private String shapeFactorRec;

	@Column(name="SHOULDER_GIRDLES")
	private String shoulderGirdles;

	@Column(name="SIGNED_BY")
	private String signedBy;

	private String signfoldthickness;

	private String since;

	@Column(name="SINCE_ABNORMAL_SPECIFY")
	private String sinceAbnormalSpecify;

	@Column(name="SINCE_IN")
	private String sinceIn;

	@Column(name="SINCE_LAST_PSYCHIATRIC")
	private String sinceLastPsychiatric;

	@Column(name="SINCE_LAST_PSYCHIATRIC_ABNORMA")
	private String sinceLastPsychiatricAbnorma;

	@Column(name="SINCE_ON")
	private String sinceOn;

	@Column(name="SINCE_WHEN2")
	private String sinceWhen2;

	@Temporal(TemporalType.DATE)
	private Date sincewhen;

	@Temporal(TemporalType.DATE)
	private Date sincewhen1;

	private String skin;

	private String smoker;

	@Column(name="SOICAL_INTERACTION")
	private String soicalInteraction;

	private String sounds;

	@Column(name="SOURCE_OF_DATA")
	private String sourceOfData;

	@Column(name="SP_GRAVITY")
	private String spGravity;

	@Column(name="SPECIAL_OCCASION_DAY")
	private String specialOccasionDay;

	@Column(name="SPECIALIST_REFER")
	private String specialistRefer;

	@Column(name="SPECIALIST_STATUS")
	private String specialistStatus;

	@Column(name="SPECIALIST_USER_ID")
	private Long specialistUserId;

	private String specialistopinion;

	private String specialistreport;

	private Long specilaistmedcatrec;

	@Temporal(TemporalType.DATE)
	private Date specilaistopiniondate;

	private String specilaistopinionremark;

	private String specilaisttreatmentadvice;

	private String speech;

	@Column(name="SPEECH_MENTAL_CAPACITY")
	private String speechMentalCapacity;

	private String spent;

	private String spine;

	@Column(name="SPL_DISABILITY")
	private String splDisability;

	//private String spleen;

	private String sportman;

	@Column(name="SR_CREATINE")
	private String srCreatine;

	@Column(name="STATE_SERVICE_FACTOR")
	private String stateServiceFactor;

	@Column(name="STATE_THE_NATURE_OF_DISEASE_DU")
	private String stateTheNatureOfDiseaseDu;

	private String status;

	private String std;

	private String sugar;

	private String sugarf;

	private String sugarpp;

	@Temporal(TemporalType.DATE)
	@Column(name="SURGERY_DATE")
	private Date surgeryDate;

	private String systexam;

	private Long temprature;

	private String throat;

	private String tlc;

	@Column(name="TOTAL_DEFECTIVE_TEETH")
	private String totalDefectiveTeeth;

	@Column(name="TOTAL_NO_DENTAL_POINT")
	private String totalNoDentalPoint;

	@Column(name="TOTAL_SERVICE")
	private String totalService;

	@Column(name="TOTAL_TEETH")
	private String totalTeeth;

	private String trachoma;

	@Column(name="TRADE_NATURE")
	private Long tradeNature;

	@Temporal(TemporalType.DATE)
	@Column(name="TRANMITTED_DATE")
	private Date tranmittedDate;

	@Column(name="TRANMITTED_DISEASE")
	private String tranmittedDisease;

	private String treatment;

	@Column(name="TREATMENT_MONTH")
	private String treatmentMonth;

	@Column(name="TREATMENT_YEAR")
	private String treatmentYear;

	private String tremors;

	private String triglycerides;

	@Column(name="TYMPANIC_MEMBRANCE_INTACT")
	private String tympanicMembranceIntact;

	private String tympanicl;

	private String tympanicr;

	@Column(name="TYPE_OF_ENTRY")
	private Long typeOfEntry;

	private String typeofcommision;

	@Column(name="U_T_LL")
	private String uTLl;

	@Column(name="U_T_LR")
	private String uTLr;

	@Column(name="U_T_UL")
	private String uTUl;

	@Column(name="U_T_UR")
	private String uTUr;

	@Column(name="UNDERSCENDED_TEST")
	private String underscendedTest;

	@Column(name="UNIT_ID")
	private Long unitId;

	@Column(name="UNSAVEABLE_TEETH")
	private String unsaveableTeeth;

	private String unserteeth;

	@Temporal(TemporalType.DATE)
	private Date uploadeddate;

	@Column(name="UPPER_LIMBS")
	private String upperLimbs;

	private String urea;

	private String uricacid;

	@Column(name="USG_ABDOMEN")
	private String usgAbdomen;

	@Column(name="VAGINAL_DISCHARGE")
	private String vaginalDischarge;

	private String valgus;

	private String varicocele;

	@Column(name="VISIT_ID")
	private Long visitId;

	private String vldl;

	@Column(name="VOCATIONAL_PERFORMANCE")
	private String vocationalPerformance;

	private String waist;

	private String waiver;

	private String weight;

	private String wheretreated;

	private String whr;

	@Column(name="WITH_GLASSES_L_DISTANT")
	private String withGlassesLDistant;

	@Column(name="WITH_GLASSES_L_NEARVISION")
	private String withGlassesLNearvision;

	@Column(name="WITH_GLASSES_R_DISTANT")
	private String withGlassesRDistant;

	@Column(name="WITH_GLASSES_R_NEARVISION")
	private String withGlassesRNearvision;

	@Column(name="WITHOUT_GLASSES_L_DISTANT")
	private String withoutGlassesLDistant;

	@Column(name="WITHOUT_GLASSES_L_NEARVISION")
	private String withoutGlassesLNearvision;

	@Column(name="WITHOUT_GLASSES_R_NEARVISION")
	private String withoutGlassesRNearvision;

	private String wrists;

	@Column(name="WTHOUT_GLASSES_R_DISTANT")
	private String wthoutGlassesRDistant;

	private String xray;

	@Column(name="YEARLY_SERIAL_NO")
	private String yearlySerialNo;

	
	@Column(name="PALLOR")
	private String pollor;
	@Column(name="EDEMA")
	private String edema;
	@Column(name="CYANOSIS")
	private String cyanosis;
	
	@Column(name="HAIR_NAIL")
	private String hairNail;
	@Column(name="ICTERUS")
	private String icterus;
	@Column(name="LYMPH_NODE")
	private String lymphNode;
	@Column(name="CLUBBING")
	private String clubbing;
	 
	
	
	@Column(name="REMARKS_PENDING")
	private String remarksPending;
	@Column(name="REMARKS_REF")
	private String remarksRef;
	@Column(name="REMARKS_REJ")
	private String remarksRej;
	
	@Column(name="REMARKS_FORWARD")
	private String remarksForward;
	
	@Column(name="DISABLITY_FLAG")
	private String disabilityFlag;
	
	@Column(name="DISABLITY_CLAIMED")
	private String disabilityClaimed;
	
	@Column(name="OTHER_HEALTH_INFO")
	private String otherHelathInfo;
	
	@Column(name="WITNESS_ID")
	private Long wittnessId;
	
	public MasMedicalExamReport() {
	}

	public Long getMedicalExaminationId() {
		return this.medicalExaminationId;
	}

	public void setMedicalExaminationId(Long medicalExaminationId) {
		this.medicalExaminationId = medicalExaminationId;
	}

	public String getAaFinalObservation() {
		return this.aaFinalObservation;
	}

	public void setAaFinalObservation(String aaFinalObservation) {
		this.aaFinalObservation = aaFinalObservation;
	}

	public String getAaRemarks() {
		return this.aaRemarks;
	}

	public void setAaRemarks(String aaRemarks) {
		this.aaRemarks = aaRemarks;
	}

	public String getAaSendto() {
		return this.aaSendto;
	}

	public void setAaSendto(String aaSendto) {
		this.aaSendto = aaSendto;
	}

	public String getAaSignedby() {
		return this.aaSignedby;
	}

	public void setAaSignedby(String aaSignedby) {
		this.aaSignedby = aaSignedby;
	}

	public String getAbdomen() {
		return this.abdomen;
	}

	public void setAbdomen(String abdomen) {
		this.abdomen = abdomen;
	}

	public String getAbnormalTrails() {
		return this.abnormalTrails;
	}

	public void setAbnormalTrails(String abnormalTrails) {
		this.abnormalTrails = abnormalTrails;
	}

	public String getAbnormalities() {
		return this.abnormalities;
	}

	public void setAbnormalities(String abnormalities) {
		this.abnormalities = abnormalities;
	}

	public String getAbortion() {
		return this.abortion;
	}

	public void setAbortion(String abortion) {
		this.abortion = abortion;
	}

	public Date getAcceptDate() {
		return this.acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getAcceptRemarks() {
		return this.acceptRemarks;
	}

	public void setAcceptRemarks(String acceptRemarks) {
		this.acceptRemarks = acceptRemarks;
	}

	public String getAcceptSignedby() {
		return this.acceptSignedby;
	}

	public void setAcceptSignedby(String acceptSignedby) {
		this.acceptSignedby = acceptSignedby;
	}

	public Long getAcceptable() {
		return this.acceptable;
	}

	public void setAcceptable(Long acceptable) {
		this.acceptable = acceptable;
	}

	public String getAccommodationL() {
		return this.accommodationL;
	}

	public void setAccommodationL(String accommodationL) {
		this.accommodationL = accommodationL;
	}

	public String getAccommodationR() {
		return this.accommodationR;
	}

	public void setAccommodationR(String accommodationR) {
		this.accommodationR = accommodationR;
	}

	public String getActualweight() {
		return this.actualweight;
	}

	public void setActualweight(String actualweight) {
		this.actualweight = actualweight;
	}

	public String getAdministrativeProfileA() {
		return this.administrativeProfileA;
	}

	public void setAdministrativeProfileA(String administrativeProfileA) {
		this.administrativeProfileA = administrativeProfileA;
	}

	public String getAdministrativeProfileB() {
		return this.administrativeProfileB;
	}

	public void setAdministrativeProfileB(String administrativeProfileB) {
		this.administrativeProfileB = administrativeProfileB;
	}

	public String getAdministrativeProfileC() {
		return this.administrativeProfileC;
	}

	public void setAdministrativeProfileC(String administrativeProfileC) {
		this.administrativeProfileC = administrativeProfileC;
	}

	public String getAdmissionStatus() {
		return this.admissionStatus;
	}

	public void setAdmissionStatus(String admissionStatus) {
		this.admissionStatus = admissionStatus;
	}

	public String getAdmittedInHospitalForAnyI() {
		return this.admittedInHospitalForAnyI;
	}

	public void setAdmittedInHospitalForAnyI(String admittedInHospitalForAnyI) {
		this.admittedInHospitalForAnyI = admittedInHospitalForAnyI;
	}

	public Date getAfrodate() {
		return this.afrodate;
	}

	public void setAfrodate(Date afrodate) {
		this.afrodate = afrodate;
	}

	public String getAggravMaterialPeriod() {
		return this.aggravMaterialPeriod;
	}

	public void setAggravMaterialPeriod(String aggravMaterialPeriod) {
		this.aggravMaterialPeriod = aggravMaterialPeriod;
	}

	public String getAggravatedMisconduct() {
		return this.aggravatedMisconduct;
	}

	public void setAggravatedMisconduct(String aggravatedMisconduct) {
		this.aggravatedMisconduct = aggravatedMisconduct;
	}

	public String getAggravatedservice() {
		return this.aggravatedservice;
	}

	public void setAggravatedservice(String aggravatedservice) {
		this.aggravatedservice = aggravatedservice;
	}

	public String getAggravatedservicedesc() {
		return this.aggravatedservicedesc;
	}

	public void setAggravatedservicedesc(String aggravatedservicedesc) {
		this.aggravatedservicedesc = aggravatedservicedesc;
	}

	public String getAirSeaCarTrainSickness() {
		return this.airSeaCarTrainSickness;
	}

	public void setAirSeaCarTrainSickness(String airSeaCarTrainSickness) {
		this.airSeaCarTrainSickness = airSeaCarTrainSickness;
	}

	public Date getAirdate() {
		return this.airdate;
	}

	public void setAirdate(Date airdate) {
		this.airdate = airdate;
	}

	public String getAlbumin() {
		return this.albumin;
	}

	public void setAlbumin(String albumin) {
		this.albumin = albumin;
	}

	public String getAlcoholDrugRelated() {
		return this.alcoholDrugRelated;
	}

	public void setAlcoholDrugRelated(String alcoholDrugRelated) {
		this.alcoholDrugRelated = alcoholDrugRelated;
	}

	public String getAllergies() {
		return this.allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getAlocoholStatus() {
		return this.alocoholStatus;
	}

	public void setAlocoholStatus(String alocoholStatus) {
		this.alocoholStatus = alocoholStatus;
	}

	public String getAmenorrhoeaDysmenonhoea() {
		return this.amenorrhoeaDysmenonhoea;
	}

	public void setAmenorrhoeaDysmenonhoea(String amenorrhoeaDysmenonhoea) {
		this.amenorrhoeaDysmenonhoea = amenorrhoeaDysmenonhoea;
	}

	public String getAnyEvidenceOfSkin() {
		return this.anyEvidenceOfSkin;
	}

	public void setAnyEvidenceOfSkin(String anyEvidenceOfSkin) {
		this.anyEvidenceOfSkin = anyEvidenceOfSkin;
	}

	public String getAnyOtheAbnormalities() {
		return this.anyOtheAbnormalities;
	}

	public void setAnyOtheAbnormalities(String anyOtheAbnormalities) {
		this.anyOtheAbnormalities = anyOtheAbnormalities;
	}

	public String getAnyOtherEarDisease() {
		return this.anyOtherEarDisease;
	}

	public void setAnyOtherEarDisease(String anyOtherEarDisease) {
		this.anyOtherEarDisease = anyOtherEarDisease;
	}

	public String getAnyOtherEyeDisease() {
		return this.anyOtherEyeDisease;
	}

	public void setAnyOtherEyeDisease(String anyOtherEyeDisease) {
		this.anyOtherEyeDisease = anyOtherEyeDisease;
	}

	public String getAnyOtherInformationAboutYo() {
		return this.anyOtherInformationAboutYo;
	}

	public void setAnyOtherInformationAboutYo(String anyOtherInformationAboutYo) {
		this.anyOtherInformationAboutYo = anyOtherInformationAboutYo;
	}

	public String getAnyOtherInvCarriedOut() {
		return this.anyOtherInvCarriedOut;
	}

	public void setAnyOtherInvCarriedOut(String anyOtherInvCarriedOut) {
		this.anyOtherInvCarriedOut = anyOtherInvCarriedOut;
	}

	public String getAnyOtherRelevantInformation() {
		return this.anyOtherRelevantInformation;
	}

	public void setAnyOtherRelevantInformation(String anyOtherRelevantInformation) {
		this.anyOtherRelevantInformation = anyOtherRelevantInformation;
	}

	public Date getAppAuthDate() {
		return this.appAuthDate;
	}

	public void setAppAuthDate(Date appAuthDate) {
		this.appAuthDate = appAuthDate;
	}

	public String getApparentAge() {
		return this.apparentAge;
	}

	public void setApparentAge(String apparentAge) {
		this.apparentAge = apparentAge;
	}

	public String getAppearance() {
		return this.appearance;
	}

	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}

	public Date getAppointmentDate() {
		return this.appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentTime() {
		return this.appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getApprovedBy() {
		return this.approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovingAuthority() {
		return this.approvingAuthority;
	}

	public void setApprovingAuthority(String approvingAuthority) {
		this.approvingAuthority = approvingAuthority;
	}

	public String getArmsCorps() {
		return this.armsCorps;
	}

	public void setArmsCorps(String armsCorps) {
		this.armsCorps = armsCorps;
	}

	public String getArterialWalls() {
		return this.arterialWalls;
	}

	public void setArterialWalls(String arterialWalls) {
		this.arterialWalls = arterialWalls;
	}

	public String getAttendentMonth() {
		return this.attendentMonth;
	}

	public void setAttendentMonth(String attendentMonth) {
		this.attendentMonth = attendentMonth;
	}

	public String getAttendentYear() {
		return this.attendentYear;
	}

	public void setAttendentYear(String attendentYear) {
		this.attendentYear = attendentYear;
	}

	public String getAudiometryRecord() {
		return this.audiometryRecord;
	}

	public void setAudiometryRecord(String audiometryRecord) {
		this.audiometryRecord = audiometryRecord;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBeenrejectedAsMedicallyUnfi() {
		return this.beenrejectedAsMedicallyUnfi;
	}

	public void setBeenrejectedAsMedicallyUnfi(String beenrejectedAsMedicallyUnfi) {
		this.beenrejectedAsMedicallyUnfi = beenrejectedAsMedicallyUnfi;
	}

	public String getBhi() {
		return this.bhi;
	}

	public void setBhi(String bhi) {
		this.bhi = bhi;
	}

	public String getBinocularVisionGrade() {
		return this.binocularVisionGrade;
	}

	public void setBinocularVisionGrade(String binocularVisionGrade) {
		this.binocularVisionGrade = binocularVisionGrade;
	}

	public String getBleedingDisorder() {
		return this.bleedingDisorder;
	}

	public void setBleedingDisorder(String bleedingDisorder) {
		this.bleedingDisorder = bleedingDisorder;
	}

	public String getBoardmember() {
		return this.boardmember;
	}

	public void setBoardmember(String boardmember) {
		this.boardmember = boardmember;
	}

	public String getBoardpresident() {
		return this.boardpresident;
	}

	public void setBoardpresident(String boardpresident) {
		this.boardpresident = boardpresident;
	}

	public String getBodyfat() {
		return this.bodyfat;
	}

	public void setBodyfat(String bodyfat) {
		this.bodyfat = bodyfat;
	}

	public String getBp() {
		return this.bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public Long getBranchId() {
		return this.branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getBreastDiseaseDischarge() {
		return this.breastDiseaseDischarge;
	}

	public void setBreastDiseaseDischarge(String breastDiseaseDischarge) {
		this.breastDiseaseDischarge = breastDiseaseDischarge;
	}

	public String getBreasts() {
		return this.breasts;
	}

	public void setBreasts(String breasts) {
		this.breasts = breasts;
	}

	public String getCardiovascularSystem() {
		return this.cardiovascularSystem;
	}

	public void setCardiovascularSystem(String cardiovascularSystem) {
		this.cardiovascularSystem = cardiovascularSystem;
	}

	public String getCasedetail() {
		return this.casedetail;
	}

	public void setCasedetail(String casedetail) {
		this.casedetail = casedetail;
	}

	public String getCasesheet() {
		return this.casesheet;
	}

	public void setCasesheet(String casesheet) {
		this.casesheet = casesheet;
	}

	public Date getCategorydate() {
		return this.categorydate;
	}

	public void setCategorydate(Date categorydate) {
		this.categorydate = categorydate;
	}

	public String getCategoryplace() {
		return this.categoryplace;
	}

	public void setCategoryplace(String categoryplace) {
		this.categoryplace = categoryplace;
	}

	public Date getCeaseduty() {
		return this.ceaseduty;
	}

	public void setCeaseduty(Date ceaseduty) {
		this.ceaseduty = ceaseduty;
	}

	public String getCentralNervousSystem() {
		return this.centralNervousSystem;
	}

	public void setCentralNervousSystem(String centralNervousSystem) {
		this.centralNervousSystem = centralNervousSystem;
	}

	public String getCentralNervousSystemMmhg() {
		return this.centralNervousSystemMmhg;
	}

	public void setCentralNervousSystemMmhg(String centralNervousSystemMmhg) {
		this.centralNervousSystemMmhg = centralNervousSystemMmhg;
	}

	public String getCervical() {
		return this.cervical;
	}

	public void setCervical(String cervical) {
		this.cervical = cervical;
	}

	public String getChestMeasurement() {
		return this.chestMeasurement;
	}

	public void setChestMeasurement(String chestMeasurement) {
		this.chestMeasurement = chestMeasurement;
	}

	public String getChestNo() {
		return this.chestNo;
	}

	public void setChestNo(String chestNo) {
		this.chestNo = chestNo;
	}

	public String getChestfullexpansion() {
		return this.chestfullexpansion;
	}

	public void setChestfullexpansion(String chestfullexpansion) {
		this.chestfullexpansion = chestfullexpansion;
	}

	public String getCholesterol() {
		return this.cholesterol;
	}

	public void setCholesterol(String cholesterol) {
		this.cholesterol = cholesterol;
	}

	public String getChronicBronchitis() {
		return this.chronicBronchitis;
	}

	public void setChronicBronchitis(String chronicBronchitis) {
		this.chronicBronchitis = chronicBronchitis;
	}

	public String getChronicIndigestion() {
		return this.chronicIndigestion;
	}

	public void setChronicIndigestion(String chronicIndigestion) {
		this.chronicIndigestion = chronicIndigestion;
	}

	public String getClamingdisability() {
		return this.clamingdisability;
	}

	public void setClamingdisability(String clamingdisability) {
		this.clamingdisability = clamingdisability;
	}

	public Long getClinicalOpdDept() {
		return this.clinicalOpdDept;
	}

	public void setClinicalOpdDept(Long clinicalOpdDept) {
		this.clinicalOpdDept = clinicalOpdDept;
	}

	public String getClinicalReferMh() {
		return this.clinicalReferMh;
	}

	public void setClinicalReferMh(String clinicalReferMh) {
		this.clinicalReferMh = clinicalReferMh;
	}

	public Long getCmUserId() {
		return this.cmUserId;
	}

	public void setCmUserId(Long cmUserId) {
		this.cmUserId = cmUserId;
	}

	public String getComdOfficerRank() {
		return this.comdOfficerRank;
	}

	public void setComdOfficerRank(String comdOfficerRank) {
		this.comdOfficerRank = comdOfficerRank;
	}

	public String getComdOfficerUnit() {
		return this.comdOfficerUnit;
	}

	public void setComdOfficerUnit(String comdOfficerUnit) {
		this.comdOfficerUnit = comdOfficerUnit;
	}

	public Long getCommandId() {
		return this.commandId;
	}

	public void setCommandId(Long commandId) {
		this.commandId = commandId;
	}

	public String getCommandRemarks() {
		return this.commandRemarks;
	}

	public void setCommandRemarks(String commandRemarks) {
		this.commandRemarks = commandRemarks;
	}

	public Date getCommanddate() {
		return this.commanddate;
	}

	public void setCommanddate(Date commanddate) {
		this.commanddate = commanddate;
	}

	public String getCommandingofficer() {
		return this.commandingofficer;
	}

	public void setCommandingofficer(String commandingofficer) {
		this.commandingofficer = commandingofficer;
	}

	public Date getCommandingofficerdate() {
		return this.commandingofficerdate;
	}

	public void setCommandingofficerdate(Date commandingofficerdate) {
		this.commandingofficerdate = commandingofficerdate;
	}

	public String getComplianceWithTreatment() {
		return this.complianceWithTreatment;
	}

	public void setComplianceWithTreatment(String complianceWithTreatment) {
		this.complianceWithTreatment = complianceWithTreatment;
	}

	public String getConditionofgums() {
		return this.conditionofgums;
	}

	public void setConditionofgums(String conditionofgums) {
		this.conditionofgums = conditionofgums;
	}

	public Date getConfirmDate() {
		return this.confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getConfirmRemarks() {
		return this.confirmRemarks;
	}

	public void setConfirmRemarks(String confirmRemarks) {
		this.confirmRemarks = confirmRemarks;
	}

	public String getConfirmSignedby() {
		return this.confirmSignedby;
	}

	public void setConfirmSignedby(String confirmSignedby) {
		this.confirmSignedby = confirmSignedby;
	}

	/*public Long getConvergenceC() {
		return this.convergenceC;
	}

	public void setConvergenceC(Long convergenceC) {
		this.convergenceC = convergenceC;
	}

	public Long getConvergenceSc() {
		return this.convergenceSc;
	}

	public void setConvergenceSc(Long convergenceSc) {
		this.convergenceSc = convergenceSc;
	}*/

	public String getCoronoryRiskFactor() {
		return this.coronoryRiskFactor;
	}

	public void setCoronoryRiskFactor(String coronoryRiskFactor) {
		this.coronoryRiskFactor = coronoryRiskFactor;
	}

	public String getCourseOfIllness() {
		return this.courseOfIllness;
	}

	public void setCourseOfIllness(String courseOfIllness) {
		this.courseOfIllness = courseOfIllness;
	}

	public String getCoverTest() {
		return this.coverTest;
	}

	public void setCoverTest(String coverTest) {
		this.coverTest = coverTest;
	}

	public String getDataOfNurveHic() {
		return this.dataOfNurveHic;
	}

	public void setDataOfNurveHic(String dataOfNurveHic) {
		this.dataOfNurveHic = dataOfNurveHic;
	}

	public Date getDateApprovingAuthority() {
		return this.dateApprovingAuthority;
	}

	public void setDateApprovingAuthority(Date dateApprovingAuthority) {
		this.dateApprovingAuthority = dateApprovingAuthority;
	}

	public Date getDateMedicalBoardExam() {
		return this.dateMedicalBoardExam;
	}

	public void setDateMedicalBoardExam(Date dateMedicalBoardExam) {
		this.dateMedicalBoardExam = dateMedicalBoardExam;
	}

	public Date getDateMedicalBoardSubsequent() {
		return this.dateMedicalBoardSubsequent;
	}

	public void setDateMedicalBoardSubsequent(Date dateMedicalBoardSubsequent) {
		this.dateMedicalBoardSubsequent = dateMedicalBoardSubsequent;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getDateOfCompletion() {
		return this.dateOfCompletion;
	}

	public void setDateOfCompletion(Date dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public Date getDateOfDischarge() {
		return this.dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public Date getDateOfLeave() {
		return this.dateOfLeave;
	}

	public void setDateOfLeave(Date dateOfLeave) {
		this.dateOfLeave = dateOfLeave;
	}

	public Date getDateOfPerusing() {
		return this.dateOfPerusing;
	}

	public void setDateOfPerusing(Date dateOfPerusing) {
		this.dateOfPerusing = dateOfPerusing;
	}

	public String getDateOfPosting() {
		return this.dateOfPosting;
	}

	public void setDateOfPosting(String dateOfPosting) {
		this.dateOfPosting = dateOfPosting;
	}

	public Date getDateOfPostingIn() {
		return this.dateOfPostingIn;
	}

	public void setDateOfPostingIn(Date dateOfPostingIn) {
		this.dateOfPostingIn = dateOfPostingIn;
	}

	public Date getDateOfPostingOut() {
		return this.dateOfPostingOut;
	}

	public void setDateOfPostingOut(Date dateOfPostingOut) {
		this.dateOfPostingOut = dateOfPostingOut;
	}

	public String getDateOfPostingOutSpecify() {
		return this.dateOfPostingOutSpecify;
	}

	public void setDateOfPostingOutSpecify(String dateOfPostingOutSpecify) {
		this.dateOfPostingOutSpecify = dateOfPostingOutSpecify;
	}

	public Date getDateOfReporting() {
		return this.dateOfReporting;
	}

	public void setDateOfReporting(Date dateOfReporting) {
		this.dateOfReporting = dateOfReporting;
	}

	public Date getDateSpecialExam() {
		return this.dateSpecialExam;
	}

	public void setDateSpecialExam(Date dateSpecialExam) {
		this.dateSpecialExam = dateSpecialExam;
	}

	public Date getDateTeath() {
		return this.dateTeath;
	}

	public void setDateTeath(Date dateTeath) {
		this.dateTeath = dateTeath;
	}

	public Date getDateapproximate() {
		return this.dateapproximate;
	}

	public void setDateapproximate(Date dateapproximate) {
		this.dateapproximate = dateapproximate;
	}

	public Date getDatedischarge() {
		return this.datedischarge;
	}

	public void setDatedischarge(Date datedischarge) {
		this.datedischarge = datedischarge;
	}

	public Date getDatedisease() {
		return this.datedisease;
	}

	public void setDatedisease(Date datedisease) {
		this.datedisease = datedisease;
	}

	public Date getDateofcommun() {
		return this.dateofcommun;
	}

	public void setDateofcommun(Date dateofcommun) {
		this.dateofcommun = dateofcommun;
	}

	public Date getDaterelease() {
		return this.daterelease;
	}

	public void setDaterelease(Date daterelease) {
		this.daterelease = daterelease;
	}

	public Date getDatesoflastleave() {
		return this.datesoflastleave;
	}

	public void setDatesoflastleave(Date datesoflastleave) {
		this.datesoflastleave = datesoflastleave;
	}

	public Date getDatetheindividual() {
		return this.datetheindividual;
	}

	public void setDatetheindividual(Date datetheindividual) {
		this.datetheindividual = datetheindividual;
	}

	public Date getDatevalidated() {
		return this.datevalidated;
	}

	public void setDatevalidated(Date datevalidated) {
		this.datevalidated = datevalidated;
	}

	public String getDefectNotToCauseRejection() {
		return this.defectNotToCauseRejection;
	}

	public void setDefectNotToCauseRejection(String defectNotToCauseRejection) {
		this.defectNotToCauseRejection = defectNotToCauseRejection;
	}

	public String getDenstlPoint() {
		return this.denstlPoint;
	}

	public void setDenstlPoint(String denstlPoint) {
		this.denstlPoint = denstlPoint;
	}

	public Date getDentalCheckupDate() {
		return this.dentalCheckupDate;
	}

	public void setDentalCheckupDate(Date dentalCheckupDate) {
		this.dentalCheckupDate = dentalCheckupDate;
	}

	public String getDentalOfficer() {
		return this.dentalOfficer;
	}

	public void setDentalOfficer(String dentalOfficer) {
		this.dentalOfficer = dentalOfficer;
	}

	public String getDentalvalue() {
		return this.dentalvalue;
	}

	public void setDentalvalue(String dentalvalue) {
		this.dentalvalue = dentalvalue;
	}

	public String getDiabetes() {
		return this.diabetes;
	}

	public void setDiabetes(String diabetes) {
		this.diabetes = diabetes;
	}

	public String getDiagNo() {
		return this.diagNo;
	}

	public void setDiagNo(String diagNo) {
		this.diagNo = diagNo;
	}

	public String getDiagNoForRadio() {
		return this.diagNoForRadio;
	}

	public void setDiagNoForRadio(String diagNoForRadio) {
		this.diagNoForRadio = diagNoForRadio;
	}

	public String getDiagnosis() {
		return this.diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getDiagnosis1Clini() {
		return this.diagnosis1Clini;
	}

	public void setDiagnosis1Clini(String diagnosis1Clini) {
		this.diagnosis1Clini = diagnosis1Clini;
	}

	public String getDiagnosis2Clini() {
		return this.diagnosis2Clini;
	}

	public void setDiagnosis2Clini(String diagnosis2Clini) {
		this.diagnosis2Clini = diagnosis2Clini;
	}

	public String getDiaphragmTest() {
		return this.diaphragmTest;
	}

	public void setDiaphragmTest(String diaphragmTest) {
		this.diaphragmTest = diaphragmTest;
	}

	public Date getDigitalDate() {
		return this.digitalDate;
	}

	public void setDigitalDate(Date digitalDate) {
		this.digitalDate = digitalDate;
	}

	public String getDisOnset() {
		return this.disOnset;
	}

	public void setDisOnset(String disOnset) {
		this.disOnset = disOnset;
	}

	public String getDisability() {
		return this.disability;
	}

	public void setDisability(String disability) {
		this.disability = disability;
	}

	public String getDisabilityOtherCase() {
		return this.disabilityOtherCase;
	}

	public void setDisabilityOtherCase(String disabilityOtherCase) {
		this.disabilityOtherCase = disabilityOtherCase;
	}

	public String getDisabilityattribute() {
		return this.disabilityattribute;
	}

	public void setDisabilityattribute(String disabilityattribute) {
		this.disabilityattribute = disabilityattribute;
	}

	public String getDisabilitybefore() {
		return this.disabilitybefore;
	}

	public void setDisabilitybefore(String disabilitybefore) {
		this.disabilitybefore = disabilitybefore;
	}

	public String getDisableAggravPersist() {
		return this.disableAggravPersist;
	}

	public void setDisableAggravPersist(String disableAggravPersist) {
		this.disableAggravPersist = disableAggravPersist;
	}

	public String getDischargeAsMedicallyUnfitF() {
		return this.dischargeAsMedicallyUnfitF;
	}

	public void setDischargeAsMedicallyUnfitF(String dischargeAsMedicallyUnfitF) {
		this.dischargeAsMedicallyUnfitF = dischargeAsMedicallyUnfitF;
	}

	public String getDischargeFromEars() {
		return this.dischargeFromEars;
	}

	public void setDischargeFromEars(String dischargeFromEars) {
		this.dischargeFromEars = dischargeFromEars;
	}

	public String getDisciplinaryRecord() {
		return this.disciplinaryRecord;
	}

	public void setDisciplinaryRecord(String disciplinaryRecord) {
		this.disciplinaryRecord = disciplinaryRecord;
	}

	public String getDiseaseSurroundingAreas() {
		return this.diseaseSurroundingAreas;
	}

	public void setDiseaseSurroundingAreas(String diseaseSurroundingAreas) {
		this.diseaseSurroundingAreas = diseaseSurroundingAreas;
	}

	public String getDissentNote() {
		return this.dissentNote;
	}

	public void setDissentNote(String dissentNote) {
		this.dissentNote = dissentNote;
	}

	public String getDlc() {
		return this.dlc;
	}

	public void setDlc(String dlc) {
		this.dlc = dlc;
	}

	public String getDoctorAdviceFlag() {
		return this.doctorAdviceFlag;
	}

	public void setDoctorAdviceFlag(String doctorAdviceFlag) {
		this.doctorAdviceFlag = doctorAdviceFlag;
	}

	public Date getDocumentForwardDate() {
		return this.documentForwardDate;
	}

	public void setDocumentForwardDate(Date documentForwardDate) {
		this.documentForwardDate = documentForwardDate;
	}

	public String getDocumentForwardTo() {
		return this.documentForwardTo;
	}

	public void setDocumentForwardTo(String documentForwardTo) {
		this.documentForwardTo = documentForwardTo;
	}

	public String getDorsalVertebrate() {
		return this.dorsalVertebrate;
	}

	public void setDorsalVertebrate(String dorsalVertebrate) {
		this.dorsalVertebrate = dorsalVertebrate;
	}

	public String getDrinker() {
		return this.drinker;
	}

	public void setDrinker(String drinker) {
		this.drinker = drinker;
	}

	public String getDutyExcused() {
		return this.dutyExcused;
	}

	public void setDutyExcused(String dutyExcused) {
		this.dutyExcused = dutyExcused;
	}

	public Date getEarDate() {
		return this.earDate;
	}

	public void setEarDate(Date earDate) {
		this.earDate = earDate;
	}

	public Long getEarHearingBothFw() {
		return this.earHearingBothFw;
	}

	public void setEarHearingBothFw(Long earHearingBothFw) {
		this.earHearingBothFw = earHearingBothFw;
	}

	public Long getEarHearingLfw() {
		return this.earHearingLfw;
	}

	public void setEarHearingLfw(Long earHearingLfw) {
		this.earHearingLfw = earHearingLfw;
	}

	public Long getEarHearingRfw() {
		return this.earHearingRfw;
	}

	public void setEarHearingRfw(Long earHearingRfw) {
		this.earHearingRfw = earHearingRfw;
	}

	public String getEcg() {
		return this.ecg;
	}

	public void setEcg(String ecg) {
		this.ecg = ecg;
	}

	public String getElbows() {
		return this.elbows;
	}

	public void setElbows(String elbows) {
		this.elbows = elbows;
	}

	public String getEmpabiltyRestric() {
		return this.empabiltyRestric;
	}

	public void setEmpabiltyRestric(String empabiltyRestric) {
		this.empabiltyRestric = empabiltyRestric;
	}

	public String getEndocrineCondition() {
		return this.endocrineCondition;
	}

	public void setEndocrineCondition(String endocrineCondition) {
		this.endocrineCondition = endocrineCondition;
	}

	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getEsr() {
		return this.esr;
	}

	public void setEsr(String esr) {
		this.esr = esr;
	}

	public String getEssentialInstability() {
		return this.essentialInstability;
	}

	public void setEssentialInstability(String essentialInstability) {
		this.essentialInstability = essentialInstability;
	}

	public String getEvidenceOfTrachoma() {
		return this.evidenceOfTrachoma;
	}

	public void setEvidenceOfTrachoma(String evidenceOfTrachoma) {
		this.evidenceOfTrachoma = evidenceOfTrachoma;
	}

	public String getExternalEarL() {
		return this.externalEarL;
	}

	public void setExternalEarL(String externalEarL) {
		this.externalEarL = externalEarL;
	}

	public String getExternalEarR() {
		return this.externalEarR;
	}

	public void setExternalEarR(String externalEarR) {
		this.externalEarR = externalEarR;
	}

	public String getFamilyLiving() {
		return this.familyLiving;
	}

	public void setFamilyLiving(String familyLiving) {
		this.familyLiving = familyLiving;
	}

	public String getFamilyLivingIn() {
		return this.familyLivingIn;
	}

	public void setFamilyLivingIn(String familyLivingIn) {
		this.familyLivingIn = familyLivingIn;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getFields() {
		return this.fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getFinalobservation() {
		return this.finalobservation;
	}

	public void setFinalobservation(String finalobservation) {
		this.finalobservation = finalobservation;
	}

	public String getFingers() {
		return this.fingers;
	}

	public void setFingers(String fingers) {
		this.fingers = fingers;
	}

	public String getFitsFaintingAttack() {
		return this.fitsFaintingAttack;
	}

	public void setFitsFaintingAttack(String fitsFaintingAttack) {
		this.fitsFaintingAttack = fitsFaintingAttack;
	}

	public String getFlatFeet() {
		return this.flatFeet;
	}

	public void setFlatFeet(String flatFeet) {
		this.flatFeet = flatFeet;
	}

	public Date getFlcDate() {
		return this.flcDate;
	}

	public void setFlcDate(Date flcDate) {
		this.flcDate = flcDate;
	}

	public String getFmdm() {
		return this.fmdm;
	}

	public void setFmdm(String fmdm) {
		this.fmdm = fmdm;
	}

	public String getFollowupTreatment() {
		return this.followupTreatment;
	}

	public void setFollowupTreatment(String followupTreatment) {
		this.followupTreatment = followupTreatment;
	}

	public Long getForwardMo() {
		return this.forwardMo;
	}

	public void setForwardMo(Long forwardMo) {
		this.forwardMo = forwardMo;
	}

	public String getFoundFitInCategory() {
		return this.foundFitInCategory;
	}

	public void setFoundFitInCategory(String foundFitInCategory) {
		this.foundFitInCategory = foundFitInCategory;
	}

	public String getFrequentCoughColdSinusitis() {
		return this.frequentCoughColdSinusitis;
	}

	public void setFrequentCoughColdSinusitis(String frequentCoughColdSinusitis) {
		this.frequentCoughColdSinusitis = frequentCoughColdSinusitis;
	}

	public String getFromWhereHeReport() {
		return this.fromWhereHeReport;
	}

	public void setFromWhereHeReport(String fromWhereHeReport) {
		this.fromWhereHeReport = fromWhereHeReport;
	}

	public String getFullExpiration() {
		return this.fullExpiration;
	}

	public void setFullExpiration(String fullExpiration) {
		this.fullExpiration = fullExpiration;
	}

	public String getFundAndMedia() {
		return this.fundAndMedia;
	}

	public void setFundAndMedia(String fundAndMedia) {
		this.fundAndMedia = fundAndMedia;
	}

	public String getGail() {
		return this.gail;
	}

	public void setGail(String gail) {
		this.gail = gail;
	}

	public String getGastroIntestinalSystem() {
		return this.gastroIntestinalSystem;
	}

	public void setGastroIntestinalSystem(String gastroIntestinalSystem) {
		this.gastroIntestinalSystem = gastroIntestinalSystem;
	}

	public String getGeneralExam() {
		return this.generalExam;
	}

	public void setGeneralExam(String generalExam) {
		this.generalExam = generalExam;
	}

	public String getGeneralOutlook() {
		return this.generalOutlook;
	}

	public void setGeneralOutlook(String generalOutlook) {
		this.generalOutlook = generalOutlook;
	}

	public String getGeneralPhysicalExam() {
		return this.generalPhysicalExam;
	}

	public void setGeneralPhysicalExam(String generalPhysicalExam) {
		this.generalPhysicalExam = generalPhysicalExam;
	}

	public String getGenitoUriraryPerineum() {
		return this.genitoUriraryPerineum;
	}

	public void setGenitoUriraryPerineum(String genitoUriraryPerineum) {
		this.genitoUriraryPerineum = genitoUriraryPerineum;
	}

	public Date getGynaecologyDate() {
		return this.gynaecologyDate;
	}

	public void setGynaecologyDate(Date gynaecologyDate) {
		this.gynaecologyDate = gynaecologyDate;
	}

	public String getHand() {
		return this.hand;
	}

	public void setHand(String hand) {
		this.hand = hand;
	}

	public String getHb() {
		return this.hb;
	}

	public void setHb(String hb) {
		this.hb = hb;
	}

	public String getHbPercentage() {
		return this.hbPercentage;
	}

	public void setHbPercentage(String hbPercentage) {
		this.hbPercentage = hbPercentage;
	}

	public String getHdl() {
		return this.hdl;
	}

	public void setHdl(String hdl) {
		this.hdl = hdl;
	}

	public Long getHearingBothCv() {
		return this.hearingBothCv;
	}

	public void setHearingBothCv(Long hearingBothCv) {
		this.hearingBothCv = hearingBothCv;
	}

	public Long getHearingLcv() {
		return this.hearingLcv;
	}

	public void setHearingLcv(Long hearingLcv) {
		this.hearingLcv = hearingLcv;
	}

	public Long getHearingRcv() {
		return this.hearingRcv;
	}

	public void setHearingRcv(Long hearingRcv) {
		this.hearingRcv = hearingRcv;
	}

	public String getHeartDiabetes() {
		return this.heartDiabetes;
	}

	public void setHeartDiabetes(String heartDiabetes) {
		this.heartDiabetes = heartDiabetes;
	}

	public String getHeartSize() {
		return this.heartSize;
	}

	public void setHeartSize(String heartSize) {
		this.heartSize = heartSize;
	}

	/*
	 * public Long getHeight() { return this.height; }
	 * 
	 * public void setHeight(Long height) { this.height = height; }
	 */

	public String getHemorrhoids() {
		return this.hemorrhoids;
	}

	public void setHemorrhoids(String hemorrhoids) {
		this.hemorrhoids = hemorrhoids;
	}

	public String getHerniaMusic() {
		return this.herniaMusic;
	}

	public void setHerniaMusic(String herniaMusic) {
		this.herniaMusic = herniaMusic;
	}

	public String getHicStatus() {
		return this.hicStatus;
	}

	public void setHicStatus(String hicStatus) {
		this.hicStatus = hicStatus;
	}

	public String getHigherMentalFunction() {
		return this.higherMentalFunction;
	}

	public void setHigherMentalFunction(String higherMentalFunction) {
		this.higherMentalFunction = higherMentalFunction;
	}

	public String getHips() {
		return this.hips;
	}

	public void setHips(String hips) {
		this.hips = hips;
	}

	public Long getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalStdCenter() {
		return this.hospitalStdCenter;
	}

	public void setHospitalStdCenter(String hospitalStdCenter) {
		this.hospitalStdCenter = hospitalStdCenter;
	}

	public String getHoursOfFlown() {
		return this.hoursOfFlown;
	}

	public void setHoursOfFlown(String hoursOfFlown) {
		this.hoursOfFlown = hoursOfFlown;
	}

	public Date getHowlonghashe() {
		return this.howlonghashe;
	}

	public void setHowlonghashe(Date howlonghashe) {
		this.howlonghashe = howlonghashe;
	}

	public String getHullux() {
		return this.hullux;
	}

	public void setHullux(String hullux) {
		this.hullux = hullux;
	}

	public String getHydrocele() {
		return this.hydrocele;
	}

	public void setHydrocele(String hydrocele) {
		this.hydrocele = hydrocele;
	}

	public String getHypertension() {
		return this.hypertension;
	}

	public void setHypertension(String hypertension) {
		this.hypertension = hypertension;
	}

	public String getIdealweight() {
		return this.idealweight;
	}

	public void setIdealweight(String idealweight) {
		this.idealweight = idealweight;
	}

	public String getIdentificationMarks1() {
		return this.identificationMarks1;
	}

	public void setIdentificationMarks1(String identificationMarks1) {
		this.identificationMarks1 = identificationMarks1;
	}

	public String getIdentificationMarks2() {
		return this.identificationMarks2;
	}

	public void setIdentificationMarks2(String identificationMarks2) {
		this.identificationMarks2 = identificationMarks2;
	}

	public String getInWay() {
		return this.inWay;
	}

	public void setInWay(String inWay) {
		this.inWay = inWay;
	}

	public String getInWay2() {
		return this.inWay2;
	}

	public void setInWay2(String inWay2) {
		this.inWay2 = inWay2;
	}

	public String getIncidentsDuringYourService() {
		return this.incidentsDuringYourService;
	}

	public void setIncidentsDuringYourService(String incidentsDuringYourService) {
		this.incidentsDuringYourService = incidentsDuringYourService;
	}

	public String getIndividualAttendent() {
		return this.individualAttendent;
	}

	public void setIndividualAttendent(String individualAttendent) {
		this.individualAttendent = individualAttendent;
	}

	public String getIndividualDigitalSign() {
		return this.individualDigitalSign;
	}

	public void setIndividualDigitalSign(String individualDigitalSign) {
		this.individualDigitalSign = individualDigitalSign;
	}

	public String getIndividualFurtherTratment() {
		return this.individualFurtherTratment;
	}

	public void setIndividualFurtherTratment(String individualFurtherTratment) {
		this.individualFurtherTratment = individualFurtherTratment;
	}

	public String getIndividualMisconduct() {
		return this.individualMisconduct;
	}

	public void setIndividualMisconduct(String individualMisconduct) {
		this.individualMisconduct = individualMisconduct;
	}

	public String getIndividualNature() {
		return this.individualNature;
	}

	public void setIndividualNature(String individualNature) {
		this.individualNature = individualNature;
	}

	public String getIndividualReason() {
		return this.individualReason;
	}

	public void setIndividualReason(String individualReason) {
		this.individualReason = individualReason;
	}

	public String getIndividualRefusalTratment() {
		return this.individualRefusalTratment;
	}

	public void setIndividualRefusalTratment(String individualRefusalTratment) {
		this.individualRefusalTratment = individualRefusalTratment;
	}

	public String getInfectionDisability() {
		return this.infectionDisability;
	}

	public void setInfectionDisability(String infectionDisability) {
		this.infectionDisability = infectionDisability;
	}

	public String getInjuryReport() {
		return this.injuryReport;
	}

	public void setInjuryReport(String injuryReport) {
		this.injuryReport = injuryReport;
	}

	public String getInnerEarL() {
		return this.innerEarL;
	}

	public void setInnerEarL(String innerEarL) {
		this.innerEarL = innerEarL;
	}

	public String getInnerEarR() {
		return this.innerEarR;
	}

	public void setInnerEarR(String innerEarR) {
		this.innerEarR = innerEarR;
	}

	public String getInstructionbypresident() {
		return this.instructionbypresident;
	}

	public void setInstructionbypresident(String instructionbypresident) {
		this.instructionbypresident = instructionbypresident;
	}

	public String getInvestigated() {
		return this.investigated;
	}

	public void setInvestigated(String investigated) {
		this.investigated = investigated;
	}

	public String getInvestigation() {
		return this.investigation;
	}

	public void setInvestigation(String investigation) {
		this.investigation = investigation;
	}

	public String getJaundice() {
		return this.jaundice;
	}

	public void setJaundice(String jaundice) {
		this.jaundice = jaundice;
	}

	public String getJoints() {
		return this.joints;
	}

	public void setJoints(String joints) {
		this.joints = joints;
	}

	public String getKidneyBladderTrouble() {
		return this.kidneyBladderTrouble;
	}

	public void setKidneyBladderTrouble(String kidneyBladderTrouble) {
		this.kidneyBladderTrouble = kidneyBladderTrouble;
	}

	public String getLaserTreatementSurgeryForE() {
		return this.laserTreatementSurgeryForE;
	}

	public void setLaserTreatementSurgeryForE(String laserTreatementSurgeryForE) {
		this.laserTreatementSurgeryForE = laserTreatementSurgeryForE;
	}

	public Date getLastBoardDate() {
		return this.lastBoardDate;
	}

	public void setLastBoardDate(Date lastBoardDate) {
		this.lastBoardDate = lastBoardDate;
	}

	

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChangedDate() {
		return this.lastChangedDate;
	}

	public void setLastChangedDate(Date lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public String getLastChangedTime() {
		return this.lastChangedTime;
	}

	public void setLastChangedTime(String lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
	}

	public Date getLastConfinementDate() {
		return this.lastConfinementDate;
	}

	public void setLastConfinementDate(Date lastConfinementDate) {
		this.lastConfinementDate = lastConfinementDate;
	}

	public String getLastReview() {
		return this.lastReview;
	}

	public void setLastReview(String lastReview) {
		this.lastReview = lastReview;
	}

	public String getLastame() {
		return this.lastame;
	}

	public void setLastame(String lastame) {
		this.lastame = lastame;
	}

	public String getLdl() {
		return this.ldl;
	}

	public void setLdl(String ldl) {
		this.ldl = ldl;
	}

	/*public Long getLegLength() {
		return this.legLength;
	}

	public void setLegLength(Long legLength) {
		this.legLength = legLength;
	}
*/
 

	public String getLivingUnitLines() {
		return this.livingUnitLines;
	}

	public void setLivingUnitLines(String livingUnitLines) {
		this.livingUnitLines = livingUnitLines;
	}

	public Long getLmcMonth() {
		return this.lmcMonth;
	}

	public void setLmcMonth(Long lmcMonth) {
		this.lmcMonth = lmcMonth;
	}

	public Long getLmcYear() {
		return this.lmcYear;
	}

	public void setLmcYear(Long lmcYear) {
		this.lmcYear = lmcYear;
	}

	public Date getLmp() {
		return this.lmp;
	}

	public void setLmp(Date lmp) {
		this.lmp = lmp;
	}

	public String getLocalexamination() {
		return this.localexamination;
	}

	public void setLocalexamination(String localexamination) {
		this.localexamination = localexamination;
	}

	public String getLocomoterSystem() {
		return this.locomoterSystem;
	}

	public void setLocomoterSystem(String locomoterSystem) {
		this.locomoterSystem = locomoterSystem;
	}

	public String getLocomotion() {
		return this.locomotion;
	}

	public void setLocomotion(String locomotion) {
		this.locomotion = locomotion;
	}

	public String getLowMedCat() {
		return this.lowMedCat;
	}

	public void setLowMedCat(String lowMedCat) {
		this.lowMedCat = lowMedCat;
	}

	public String getLumber() {
		return this.lumber;
	}

	public void setLumber(String lumber) {
		this.lumber = lumber;
	}

	public String getMTLl() {
		return this.mTLl;
	}

	public void setMTLl(String mTLl) {
		this.mTLl = mTLl;
	}

	public String getMTLr() {
		return this.mTLr;
	}

	public void setMTLr(String mTLr) {
		this.mTLr = mTLr;
	}

	public String getMTUl() {
		return this.mTUl;
	}

	public void setMTUl(String mTUl) {
		this.mTUl = mTUl;
	}

	public String getMTUr() {
		return this.mTUr;
	}

	public void setMTUr(String mTUr) {
		this.mTUr = mTUr;
	}

	public Long getMaUserId() {
		return this.maUserId;
	}

	public void setMaUserId(Long maUserId) {
		this.maUserId = maUserId;
	}

	public String getManifestHypermetropia() {
		return this.manifestHypermetropia;
	}

	public void setManifestHypermetropia(String manifestHypermetropia) {
		this.manifestHypermetropia = manifestHypermetropia;
	}

	public Long getMaritalStatus() {
		return this.maritalStatus;
	}

	public void setMaritalStatus(Long maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getMbField() {
		return this.mbField;
	}

	public void setMbField(String mbField) {
		this.mbField = mbField;
	}

	public String getMdRemarks() {
		return this.mdRemarks;
	}

	public void setMdRemarks(String mdRemarks) {
		this.mdRemarks = mdRemarks;
	}

	public Long getMdUserId() {
		return this.mdUserId;
	}

	public void setMdUserId(Long mdUserId) {
		this.mdUserId = mdUserId;
	}

	public Long getMedBoardDuration() {
		return this.medBoardDuration;
	}

	public void setMedBoardDuration(Long medBoardDuration) {
		this.medBoardDuration = medBoardDuration;
	}

	public String getMedBoardRemarks() {
		return this.medBoardRemarks;
	}

	public void setMedBoardRemarks(String medBoardRemarks) {
		this.medBoardRemarks = medBoardRemarks;
	}

	public Long getMedCatRelease() {
		return this.medCatRelease;
	}

	public void setMedCatRelease(Long medCatRelease) {
		this.medCatRelease = medCatRelease;
	}

	public String getMedComposite() {
		return this.medComposite;
	}

	public void setMedComposite(String medComposite) {
		this.medComposite = medComposite;
	}

	public Date getMedDetailDate() {
		return this.medDetailDate;
	}

	public void setMedDetailDate(Date medDetailDate) {
		this.medDetailDate = medDetailDate;
	}

	public Long getMedDetailMember1() {
		return this.medDetailMember1;
	}

	public void setMedDetailMember1(Long medDetailMember1) {
		this.medDetailMember1 = medDetailMember1;
	}

	public Long getMedDetailMember2() {
		return this.medDetailMember2;
	}

	public void setMedDetailMember2(Long medDetailMember2) {
		this.medDetailMember2 = medDetailMember2;
	}

	public Long getMedDetailPresident() {
		return this.medDetailPresident;
	}

	public void setMedDetailPresident(Long medDetailPresident) {
		this.medDetailPresident = medDetailPresident;
	}

	public String getMedExamCarryTime() {
		return this.medExamCarryTime;
	}

	public void setMedExamCarryTime(String medExamCarryTime) {
		this.medExamCarryTime = medExamCarryTime;
	}

	public String getMedInstructions() {
		return this.medInstructions;
	}

	public void setMedInstructions(String medInstructions) {
		this.medInstructions = medInstructions;
	}

	public String getMedRemarks() {
		return this.medRemarks;
	}

	public void setMedRemarks(String medRemarks) {
		this.medRemarks = medRemarks;
	}

	public String getMedboardHeld() {
		return this.medboardHeld;
	}

	public void setMedboardHeld(String medboardHeld) {
		this.medboardHeld = medboardHeld;
	}

	public String getMedcatrec() {
		return this.medcatrec;
	}

	public void setMedcatrec(String medcatrec) {
		this.medcatrec = medcatrec;
	}

	public String getMedicalBoardFindings() {
		return this.medicalBoardFindings;
	}

	public void setMedicalBoardFindings(String medicalBoardFindings) {
		this.medicalBoardFindings = medicalBoardFindings;
	}

	public String getMedicalBoardSubsequentFind() {
		return this.medicalBoardSubsequentFind;
	}

	public void setMedicalBoardSubsequentFind(String medicalBoardSubsequentFind) {
		this.medicalBoardSubsequentFind = medicalBoardSubsequentFind;
	}

	public Long getMedicalCategoryRecommend() {
		return this.medicalCategoryRecommend;
	}

	public void setMedicalCategoryRecommend(Long medicalCategoryRecommend) {
		this.medicalCategoryRecommend = medicalCategoryRecommend;
	}

	public Long getMedicalExamHeldAt() {
		return this.medicalExamHeldAt;
	}

	public void setMedicalExamHeldAt(Long medicalExamHeldAt) {
		this.medicalExamHeldAt = medicalExamHeldAt;
	}

	public String getMedicalStatus() {
		return this.medicalStatus;
	}

	public void setMedicalStatus(String medicalStatus) {
		this.medicalStatus = medicalStatus;
	}

	/*public String getMedicalcategory() {
		return this.medicalcategory;
	}

	public void setMedicalcategory(String medicalcategory) {
		this.medicalcategory = medicalcategory;
	}*/

	public String getMedicalexamtype() {
		return this.medicalexamtype;
	}

	public void setMedicalexamtype(String medicalexamtype) {
		this.medicalexamtype = medicalexamtype;
	}

	public String getMedicaltype() {
		return this.medicaltype;
	}

	public void setMedicaltype(String medicaltype) {
		this.medicaltype = medicaltype;
	}

	public String getMedication() {
		return this.medication;
	}

	public void setMedication(String medication) {
		this.medication = medication;
	}

	public Date getMediceExamDate() {
		return this.mediceExamDate;
	}

	public void setMediceExamDate(Date mediceExamDate) {
		this.mediceExamDate = mediceExamDate;
	}

	public String getMenonhagia() {
		return this.menonhagia;
	}

	public void setMenonhagia(String menonhagia) {
		this.menonhagia = menonhagia;
	}

	public String getMenstrualHistory() {
		return this.menstrualHistory;
	}

	public void setMenstrualHistory(String menstrualHistory) {
		this.menstrualHistory = menstrualHistory;
	}

	public String getMentalDisease() {
		return this.mentalDisease;
	}

	public void setMentalDisease(String mentalDisease) {
		this.mentalDisease = mentalDisease;
	}

	public String getMentalInstability() {
		return this.mentalInstability;
	}

	public void setMentalInstability(String mentalInstability) {
		this.mentalInstability = mentalInstability;
	}

	public String getMiddleEar() {
		return this.middleEar;
	}

	public void setMiddleEar(String middleEar) {
		this.middleEar = middleEar;
	}

	public String getMiddleEarR() {
		return this.middleEarR;
	}

	public void setMiddleEarR(String middleEarR) {
		this.middleEarR = middleEarR;
	}

	public String getMissingTeeth() {
		return this.missingTeeth;
	}

	public void setMissingTeeth(String missingTeeth) {
		this.missingTeeth = missingTeeth;
	}

	public String getMissteeth() {
		return this.missteeth;
	}

	public void setMissteeth(String missteeth) {
		this.missteeth = missteeth;
	}

	public Long getMoUserId() {
		return this.moUserId;
	}

	public void setMoUserId(Long moUserId) {
		this.moUserId = moUserId;
	}

	public String getMobilityl() {
		return this.mobilityl;
	}

	public void setMobilityl(String mobilityl) {
		this.mobilityl = mobilityl;
	}

	public String getMobilityr() {
		return this.mobilityr;
	}

	public void setMobilityr(String mobilityr) {
		this.mobilityr = mobilityr;
	}

	public Date getModate() {
		return this.modate;
	}

	public void setModate(Date modate) {
		this.modate = modate;
	}

	public String getMonthlySerialNo() {
		return this.monthlySerialNo;
	}

	public void setMonthlySerialNo(String monthlySerialNo) {
		this.monthlySerialNo = monthlySerialNo;
	}

	public String getMotivation() {
		return this.motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public String getNameInFull() {
		return this.nameInFull;
	}

	public void setNameInFull(String nameInFull) {
		this.nameInFull = nameInFull;
	}

	public String getNatureDuties() {
		return this.natureDuties;
	}

	public void setNatureDuties(String natureDuties) {
		this.natureDuties = natureDuties;
	}

	public String getNearVisionWithGlassCp() {
		return this.nearVisionWithGlassCp;
	}

	public void setNearVisionWithGlassCp(String nearVisionWithGlassCp) {
		this.nearVisionWithGlassCp = nearVisionWithGlassCp;
	}

	public String getNearVisionWithoutGlassCp() {
		return this.nearVisionWithoutGlassCp;
	}

	public void setNearVisionWithoutGlassCp(String nearVisionWithoutGlassCp) {
		this.nearVisionWithoutGlassCp = nearVisionWithoutGlassCp;
	}

	public String getNervousBreakdownMentalIllne() {
		return this.nervousBreakdownMentalIllne;
	}

	public void setNervousBreakdownMentalIllne(String nervousBreakdownMentalIllne) {
		this.nervousBreakdownMentalIllne = nervousBreakdownMentalIllne;
	}

	public Date getNextBoardDate() {
		return this.nextBoardDate;
	}

	public void setNextBoardDate(Date nextBoardDate) {
		this.nextBoardDate = nextBoardDate;
	}

	public String getNightBindness() {
		return this.nightBindness;
	}

	public void setNightBindness(String nightBindness) {
		this.nightBindness = nightBindness;
	}

	public String getNightBlindness() {
		return this.nightBlindness;
	}

	public void setNightBlindness(String nightBlindness) {
		this.nightBlindness = nightBlindness;
	}

	public String getNightVisualCapacity() {
		return this.nightVisualCapacity;
	}

	public void setNightVisualCapacity(String nightVisualCapacity) {
		this.nightVisualCapacity = nightVisualCapacity;
	}

	public Long getNoOfAbortions() {
		return this.noOfAbortions;
	}

	public void setNoOfAbortions(Long noOfAbortions) {
		this.noOfAbortions = noOfAbortions;
	}

	public Long getNoOfChildren() {
		return this.noOfChildren;
	}

	public void setNoOfChildren(Long noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	public Long getNoOfPregnancies() {
		return this.noOfPregnancies;
	}

	public void setNoOfPregnancies(Long noOfPregnancies) {
		this.noOfPregnancies = noOfPregnancies;
	}

	public String getNose() {
		return this.nose;
	}

	public void setNose(String nose) {
		this.nose = nose;
	}

	public String getNoseThroatSinuses() {
		return this.noseThroatSinuses;
	}

	public void setNoseThroatSinuses(String noseThroatSinuses) {
		this.noseThroatSinuses = noseThroatSinuses;
	}

	public String getNosethroat() {
		return this.nosethroat;
	}

	public void setNosethroat(String nosethroat) {
		this.nosethroat = nosethroat;
	}

	public String getOnset() {
		return this.onset;
	}

	public void setOnset(String onset) {
		this.onset = onset;
	}

	public String getOnsetDiag1() {
		return this.onsetDiag1;
	}

	public void setOnsetDiag1(String onsetDiag1) {
		this.onsetDiag1 = onsetDiag1;
	}

	public String getOnsetDiag2() {
		return this.onsetDiag2;
	}

	public void setOnsetDiag2(String onsetDiag2) {
		this.onsetDiag2 = onsetDiag2;
	}

	public String getOperaTreatmentRecommend() {
		return this.operaTreatmentRecommend;
	}

	public void setOperaTreatmentRecommend(String operaTreatmentRecommend) {
		this.operaTreatmentRecommend = operaTreatmentRecommend;
	}

	public String getOperationTreatCured() {
		return this.operationTreatCured;
	}

	public void setOperationTreatCured(String operationTreatCured) {
		this.operationTreatCured = operationTreatCured;
	}

	public String getOperational() {
		return this.operational;
	}

	public void setOperational(String operational) {
		this.operational = operational;
	}

	public String getOpernTreatCuredEntry() {
		return this.opernTreatCuredEntry;
	}

	public void setOpernTreatCuredEntry(String opernTreatCuredEntry) {
		this.opernTreatCuredEntry = opernTreatCuredEntry;
	}

	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Date getOpiniondate() {
		return this.opiniondate;
	}

	public void setOpiniondate(Date opiniondate) {
		this.opiniondate = opiniondate;
	}

	public String getOtherinformation() {
		return this.otherinformation;
	}

	public void setOtherinformation(String otherinformation) {
		this.otherinformation = otherinformation;
	}

	public String getOthers() {
		return this.others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getOverweight() {
		return this.overweight;
	}

	public void setOverweight(String overweight) {
		this.overweight = overweight;
	}

	public String getPNo() {
		return this.pNo;
	}

	public void setPNo(String pNo) {
		this.pNo = pNo;
	}

	public String getPaAfroFinalObser() {
		return this.paAfroFinalObser;
	}

	public void setPaAfroFinalObser(String paAfroFinalObser) {
		this.paAfroFinalObser = paAfroFinalObser;
	}

	public String getPaAfroRemarks() {
		return this.paAfroRemarks;
	}

	public void setPaAfroRemarks(String paAfroRemarks) {
		this.paAfroRemarks = paAfroRemarks;
	}

	public String getPaAfroSignedby() {
		return this.paAfroSignedby;
	}

	public void setPaAfroSignedby(String paAfroSignedby) {
		this.paAfroSignedby = paAfroSignedby;
	}

	public String getPaFinalobservation() {
		return this.paFinalobservation;
	}

	public void setPaFinalobservation(String paFinalobservation) {
		this.paFinalobservation = paFinalobservation;
	}

	public String getPaRemarks() {
		return this.paRemarks;
	}

	public void setPaRemarks(String paRemarks) {
		this.paRemarks = paRemarks;
	}

	public String getPaSendto() {
		return this.paSendto;
	}

	public void setPaSendto(String paSendto) {
		this.paSendto = paSendto;
	}

	public String getPaSignedby() {
		return this.paSignedby;
	}

	public void setPaSignedby(String paSignedby) {
		this.paSignedby = paSignedby;
	}

	public String getParmanentAddress() {
		return this.parmanentAddress;
	}

	public void setParmanentAddress(String parmanentAddress) {
		this.parmanentAddress = parmanentAddress;
	}

	public String getParticularofpreviousservice() {
		return this.particularofpreviousservice;
	}

	public void setParticularofpreviousservice(String particularofpreviousservice) {
		this.particularofpreviousservice = particularofpreviousservice;
	}

	public String getPastMedPeriod() {
		return this.pastMedPeriod;
	}

	public void setPastMedPeriod(String pastMedPeriod) {
		this.pastMedPeriod = pastMedPeriod;
	}

	public Long getPastdisability() {
		return this.pastdisability;
	}

	public void setPastdisability(Long pastdisability) {
		this.pastdisability = pastdisability;
	}

	public Long getPastmedicalcategory() {
		return this.pastmedicalcategory;
	}

	public void setPastmedicalcategory(Long pastmedicalcategory) {
		this.pastmedicalcategory = pastmedicalcategory;
	}

	public String getPastmedicalhistory() {
		return this.pastmedicalhistory;
	}

	public void setPastmedicalhistory(String pastmedicalhistory) {
		this.pastmedicalhistory = pastmedicalhistory;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPatientheight() {
		return this.patientheight;
	}

	public void setPatientheight(String patientheight) {
		this.patientheight = patientheight;
	}

	public String getPatientweight() {
		return this.patientweight;
	}

	public void setPatientweight(String patientweight) {
		this.patientweight = patientweight;
	}

	public String getPelvis() {
		return this.pelvis;
	}

	public void setPelvis(String pelvis) {
		this.pelvis = pelvis;
	}

	public String getPendingDisciplinaryCases() {
		return this.pendingDisciplinaryCases;
	}

	public void setPendingDisciplinaryCases(String pendingDisciplinaryCases) {
		this.pendingDisciplinaryCases = pendingDisciplinaryCases;
	}

	public Date getPerAuthDate() {
		return this.perAuthDate;
	}

	public void setPerAuthDate(Date perAuthDate) {
		this.perAuthDate = perAuthDate;
	}

	public String getPercentageDisable() {
		return this.percentageDisable;
	}

	public void setPercentageDisable(String percentageDisable) {
		this.percentageDisable = percentageDisable;
	}

	public String getPercentageDisableTreatment() {
		return this.percentageDisableTreatment;
	}

	public void setPercentageDisableTreatment(String percentageDisableTreatment) {
		this.percentageDisableTreatment = percentageDisableTreatment;
	}

	public String getPerformationUnderStress() {
		return this.performationUnderStress;
	}

	public void setPerformationUnderStress(String performationUnderStress) {
		this.performationUnderStress = performationUnderStress;
	}

	public String getPeriod() {
		return this.period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPermanentaddress() {
		return this.permanentaddress;
	}

	public void setPermanentaddress(String permanentaddress) {
		this.permanentaddress = permanentaddress;
	}

	public String getPersonalans() {
		return this.personalans;
	}

	public void setPersonalans(String personalans) {
		this.personalans = personalans;
	}

	public String getPhysique() {
		return this.physique;
	}

	public void setPhysique(String physique) {
		this.physique = physique;
	}

	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Long getPlaceApprovingAuthority() {
		return this.placeApprovingAuthority;
	}

	public void setPlaceApprovingAuthority(Long placeApprovingAuthority) {
		this.placeApprovingAuthority = placeApprovingAuthority;
	}

	public String getPlaceLastCatBoard() {
		return this.placeLastCatBoard;
	}

	public void setPlaceLastCatBoard(String placeLastCatBoard) {
		this.placeLastCatBoard = placeLastCatBoard;
	}

	public Long getPlaceMedicalBoardExam() {
		return this.placeMedicalBoardExam;
	}

	public void setPlaceMedicalBoardExam(Long placeMedicalBoardExam) {
		this.placeMedicalBoardExam = placeMedicalBoardExam;
	}

	public Long getPlaceMedicalBoardSubsequent() {
		return this.placeMedicalBoardSubsequent;
	}

	public void setPlaceMedicalBoardSubsequent(Long placeMedicalBoardSubsequent) {
		this.placeMedicalBoardSubsequent = placeMedicalBoardSubsequent;
	}

	public String getPlaceNextCatBoard() {
		return this.placeNextCatBoard;
	}

	public void setPlaceNextCatBoard(String placeNextCatBoard) {
		this.placeNextCatBoard = placeNextCatBoard;
	}

	public String getPlacedisease() {
		return this.placedisease;
	}

	public void setPlacedisease(String placedisease) {
		this.placedisease = placedisease;
	}

	public String getPleurisy() {
		return this.pleurisy;
	}

	public void setPleurisy(String pleurisy) {
		this.pleurisy = pleurisy;
	}

	public String getPregnancy() {
		return this.pregnancy;
	}

	public void setPregnancy(String pregnancy) {
		this.pregnancy = pregnancy;
	}

	public String getPresentMedPeriod() {
		return this.presentMedPeriod;
	}

	public void setPresentMedPeriod(String presentMedPeriod) {
		this.presentMedPeriod = presentMedPeriod;
	}

	public String getPresentcondition() {
		return this.presentcondition;
	}

	public void setPresentcondition(String presentcondition) {
		this.presentcondition = presentcondition;
	}

	public String getPresentconditionin() {
		return this.presentconditionin;
	}

	public void setPresentconditionin(String presentconditionin) {
		this.presentconditionin = presentconditionin;
	}

	public Long getPresentdisability() {
		return this.presentdisability;
	}

	public void setPresentdisability(Long presentdisability) {
		this.presentdisability = presentdisability;
	}

	public String getPresentdisablement() {
		return this.presentdisablement;
	}

	public void setPresentdisablement(String presentdisablement) {
		this.presentdisablement = presentdisablement;
	}

	public Long getPresentmedicalcategory() {
		return this.presentmedicalcategory;
	}

	public void setPresentmedicalcategory(Long presentmedicalcategory) {
		this.presentmedicalcategory = presentmedicalcategory;
	}

	public String getPresentmedicalhistory() {
		return this.presentmedicalhistory;
	}

	public void setPresentmedicalhistory(String presentmedicalhistory) {
		this.presentmedicalhistory = presentmedicalhistory;
	}

	public String getPreventMeasure() {
		return this.preventMeasure;
	}

	public void setPreventMeasure(String preventMeasure) {
		this.preventMeasure = preventMeasure;
	}

	public String getPreviousdisablement() {
		return this.previousdisablement;
	}

	public void setPreviousdisablement(String previousdisablement) {
		this.previousdisablement = previousdisablement;
	}

	public String getPriorAbnormalSpecify() {
		return this.priorAbnormalSpecify;
	}

	public void setPriorAbnormalSpecify(String priorAbnormalSpecify) {
		this.priorAbnormalSpecify = priorAbnormalSpecify;
	}

	public String getPriorToOnset() {
		return this.priorToOnset;
	}

	public void setPriorToOnset(String priorToOnset) {
		this.priorToOnset = priorToOnset;
	}

	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getProfessional() {
		return this.professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getProlapse() {
		return this.prolapse;
	}

	public void setProlapse(String prolapse) {
		this.prolapse = prolapse;
	}

	public String getPulseRates() {
		return this.pulseRates;
	}

	public void setPulseRates(String pulseRates) {
		this.pulseRates = pulseRates;
	}

	public String getRangeOfExpension() {
		return this.rangeOfExpension;
	}

	public void setRangeOfExpension(String rangeOfExpension) {
		this.rangeOfExpension = rangeOfExpension;
	}

	public String getRangeofexpansion() {
		return this.rangeofexpansion;
	}

	public void setRangeofexpansion(String rangeofexpansion) {
		this.rangeofexpansion = rangeofexpansion;
	}

	public Long getRankId() {
		return this.rankId;
	}

	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}

	public String getReasonWoundInjury() {
		return this.reasonWoundInjury;
	}

	public void setReasonWoundInjury(String reasonWoundInjury) {
		this.reasonWoundInjury = reasonWoundInjury;
	}

	public String getReasopnsvariation() {
		return this.reasopnsvariation;
	}

	public void setReasopnsvariation(String reasopnsvariation) {
		this.reasopnsvariation = reasopnsvariation;
	}

	public String getRecMedPeriod() {
		return this.recMedPeriod;
	}

	public void setRecMedPeriod(String recMedPeriod) {
		this.recMedPeriod = recMedPeriod;
	}

	public String getRecordoffice() {
		return this.recordoffice;
	}

	public void setRecordoffice(String recordoffice) {
		this.recordoffice = recordoffice;
	}

	public String getReducDisablePensionEntry() {
		return this.reducDisablePensionEntry;
	}

	public void setReducDisablePensionEntry(String reducDisablePensionEntry) {
		this.reducDisablePensionEntry = reducDisablePensionEntry;
	}

	public String getReductionDisablePension() {
		return this.reductionDisablePension;
	}

	public void setReductionDisablePension(String reductionDisablePension) {
		this.reductionDisablePension = reductionDisablePension;
	}

	public String getRefertomh() {
		return this.refertomh;
	}

	public void setRefertomh(String refertomh) {
		this.refertomh = refertomh;
	}

	public String getReflexes() {
		return this.reflexes;
	}

	public void setReflexes(String reflexes) {
		this.reflexes = reflexes;
	}

	public String getRefuseOperationTreat() {
		return this.refuseOperationTreat;
	}

	public void setRefuseOperationTreat(String refuseOperationTreat) {
		this.refuseOperationTreat = refuseOperationTreat;
	}

	public String getRegimental() {
		return this.regimental;
	}

	public void setRegimental(String regimental) {
		this.regimental = regimental;
	}

	public String getRegimentalOthers() {
		return this.regimentalOthers;
	}

	public void setRegimentalOthers(String regimentalOthers) {
		this.regimentalOthers = regimentalOthers;
	}

	public Date getRejectDate() {
		return this.rejectDate;
	}

	public void setRejectDate(Date rejectDate) {
		this.rejectDate = rejectDate;
	}

	public String getRejectStatus() {
		return this.rejectStatus;
	}

	public void setRejectStatus(String rejectStatus) {
		this.rejectStatus = rejectStatus;
	}

	public String getReleaseMedCatValue() {
		return this.releaseMedCatValue;
	}

	public void setReleaseMedCatValue(String releaseMedCatValue) {
		this.releaseMedCatValue = releaseMedCatValue;
	}

	public String getReleaseMedPeriod() {
		return this.releaseMedPeriod;
	}

	public void setReleaseMedPeriod(String releaseMedPeriod) {
		this.releaseMedPeriod = releaseMedPeriod;
	}

	public String getReleaseShapeFactor() {
		return this.releaseShapeFactor;
	}

	public void setReleaseShapeFactor(String releaseShapeFactor) {
		this.releaseShapeFactor = releaseShapeFactor;
	}

	public String getRelevantFamilyHistory() {
		return this.relevantFamilyHistory;
	}

	public void setRelevantFamilyHistory(String relevantFamilyHistory) {
		this.relevantFamilyHistory = relevantFamilyHistory;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarksLowerlimbs() {
		return this.remarksLowerlimbs;
	}

	public void setRemarksLowerlimbs(String remarksLowerlimbs) {
		this.remarksLowerlimbs = remarksLowerlimbs;
	}

	public String getRemarksClinical() {
		return this.remarksClinical;
	}

	public void setRemarksClinical(String remarksClinical) {
		this.remarksClinical = remarksClinical;
	}

	public String getRemarksEar() {
		return this.remarksEar;
	}

	public void setRemarksEar(String remarksEar) {
		this.remarksEar = remarksEar;
	}

	public String getRemarksGynaecology() {
		return this.remarksGynaecology;
	}

	public void setRemarksGynaecology(String remarksGynaecology) {
		this.remarksGynaecology = remarksGynaecology;
	}

	public String getRemarksSpecialExam() {
		return this.remarksSpecialExam;
	}

	public void setRemarksSpecialExam(String remarksSpecialExam) {
		this.remarksSpecialExam = remarksSpecialExam;
	}

	public String getRemarksTeath() {
		return this.remarksTeath;
	}

	public void setRemarksTeath(String remarksTeath) {
		this.remarksTeath = remarksTeath;
	}

	public String getRespiratorySystem() {
		return this.respiratorySystem;
	}

	public void setRespiratorySystem(String respiratorySystem) {
		this.respiratorySystem = respiratorySystem;
	}

	public String getResponsiblityFactor() {
		return this.responsiblityFactor;
	}

	public void setResponsiblityFactor(String responsiblityFactor) {
		this.responsiblityFactor = responsiblityFactor;
	}

	public String getRestrictionemployment() {
		return this.restrictionemployment;
	}

	public void setRestrictionemployment(String restrictionemployment) {
		this.restrictionemployment = restrictionemployment;
	}

	public String getRheumatismFrequentSorethroat() {
		return this.rheumatismFrequentSorethroat;
	}

	public void setRheumatismFrequentSorethroat(String rheumatismFrequentSorethroat) {
		this.rheumatismFrequentSorethroat = rheumatismFrequentSorethroat;
	}

	public String getRhythm() {
		return this.rhythm;
	}

	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}

	public String getRigigus() {
		return this.rigigus;
	}

	public void setRigigus(String rigigus) {
		this.rigigus = rigigus;
	}

	public String getRoccyxVarocose() {
		return this.roccyxVarocose;
	}

	public void setRoccyxVarocose(String roccyxVarocose) {
		this.roccyxVarocose = roccyxVarocose;
	}

	public String getRollNo() {
		return this.rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getRrClinical() {
		return this.rrClinical;
	}

	public void setRrClinical(String rrClinical) {
		this.rrClinical = rrClinical;
	}

	public String getSd() {
		return this.sd;
	}

	public void setSd(String sd) {
		this.sd = sd;
	}

	public String getSelfBalancingL() {
		return this.selfBalancingL;
	}

	public void setSelfBalancingL(String selfBalancingL) {
		this.selfBalancingL = selfBalancingL;
	}

	public String getSelfBalancingR() {
		return this.selfBalancingR;
	}

	public void setSelfBalancingR(String selfBalancingR) {
		this.selfBalancingR = selfBalancingR;
	}

	public String getSelfBalancingTest() {
		return this.selfBalancingTest;
	}

	public void setSelfBalancingTest(String selfBalancingTest) {
		this.selfBalancingTest = selfBalancingTest;
	}

	public String getSendto() {
		return this.sendto;
	}

	public void setSendto(String sendto) {
		this.sendto = sendto;
	}

	public Long getServiceTypeId() {
		return this.serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getServiceiaf() {
		return this.serviceiaf;
	}

	public void setServiceiaf(String serviceiaf) {
		this.serviceiaf = serviceiaf;
	}

	public String getServiceno() {
		return this.serviceno;
	}

	public void setServiceno(String serviceno) {
		this.serviceno = serviceno;
	}

	public String getSevereExcepStress() {
		return this.severeExcepStress;
	}

	public void setSevereExcepStress(String severeExcepStress) {
		this.severeExcepStress = severeExcepStress;
	}

	public String getSevereHeadInjury() {
		return this.severeHeadInjury;
	}

	public void setSevereHeadInjury(String severeHeadInjury) {
		this.severeHeadInjury = severeHeadInjury;
	}

	public String getShapFactor() {
		return this.shapFactor;
	}

	public void setShapFactor(String shapFactor) {
		this.shapFactor = shapFactor;
	}

	public String getShapeFactorPast() {
		return this.shapeFactorPast;
	}

	public void setShapeFactorPast(String shapeFactorPast) {
		this.shapeFactorPast = shapeFactorPast;
	}

	public String getShapeFactorRec() {
		return this.shapeFactorRec;
	}

	public void setShapeFactorRec(String shapeFactorRec) {
		this.shapeFactorRec = shapeFactorRec;
	}

	public String getShoulderGirdles() {
		return this.shoulderGirdles;
	}

	public void setShoulderGirdles(String shoulderGirdles) {
		this.shoulderGirdles = shoulderGirdles;
	}

	public String getSignedBy() {
		return this.signedBy;
	}

	public void setSignedBy(String signedBy) {
		this.signedBy = signedBy;
	}

	public String getSignfoldthickness() {
		return this.signfoldthickness;
	}

	public void setSignfoldthickness(String signfoldthickness) {
		this.signfoldthickness = signfoldthickness;
	}

	public String getSince() {
		return this.since;
	}

	public void setSince(String since) {
		this.since = since;
	}

	public String getSinceAbnormalSpecify() {
		return this.sinceAbnormalSpecify;
	}

	public void setSinceAbnormalSpecify(String sinceAbnormalSpecify) {
		this.sinceAbnormalSpecify = sinceAbnormalSpecify;
	}

	public String getSinceIn() {
		return this.sinceIn;
	}

	public void setSinceIn(String sinceIn) {
		this.sinceIn = sinceIn;
	}

	public String getSinceLastPsychiatric() {
		return this.sinceLastPsychiatric;
	}

	public void setSinceLastPsychiatric(String sinceLastPsychiatric) {
		this.sinceLastPsychiatric = sinceLastPsychiatric;
	}

	public String getSinceLastPsychiatricAbnorma() {
		return this.sinceLastPsychiatricAbnorma;
	}

	public void setSinceLastPsychiatricAbnorma(String sinceLastPsychiatricAbnorma) {
		this.sinceLastPsychiatricAbnorma = sinceLastPsychiatricAbnorma;
	}

	public String getSinceOn() {
		return this.sinceOn;
	}

	public void setSinceOn(String sinceOn) {
		this.sinceOn = sinceOn;
	}

	public String getSinceWhen2() {
		return this.sinceWhen2;
	}

	public void setSinceWhen2(String sinceWhen2) {
		this.sinceWhen2 = sinceWhen2;
	}

	public Date getSincewhen() {
		return this.sincewhen;
	}

	public void setSincewhen(Date sincewhen) {
		this.sincewhen = sincewhen;
	}

	public Date getSincewhen1() {
		return this.sincewhen1;
	}

	public void setSincewhen1(Date sincewhen1) {
		this.sincewhen1 = sincewhen1;
	}

	public String getSkin() {
		return this.skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getSmoker() {
		return this.smoker;
	}

	public void setSmoker(String smoker) {
		this.smoker = smoker;
	}

	public String getSoicalInteraction() {
		return this.soicalInteraction;
	}

	public void setSoicalInteraction(String soicalInteraction) {
		this.soicalInteraction = soicalInteraction;
	}

	public String getSounds() {
		return this.sounds;
	}

	public void setSounds(String sounds) {
		this.sounds = sounds;
	}

	public String getSourceOfData() {
		return this.sourceOfData;
	}

	public void setSourceOfData(String sourceOfData) {
		this.sourceOfData = sourceOfData;
	}

	public String getSpGravity() {
		return this.spGravity;
	}

	public void setSpGravity(String spGravity) {
		this.spGravity = spGravity;
	}

	public String getSpecialOccasionDay() {
		return this.specialOccasionDay;
	}

	public void setSpecialOccasionDay(String specialOccasionDay) {
		this.specialOccasionDay = specialOccasionDay;
	}

	public String getSpecialistRefer() {
		return this.specialistRefer;
	}

	public void setSpecialistRefer(String specialistRefer) {
		this.specialistRefer = specialistRefer;
	}

	public String getSpecialistStatus() {
		return this.specialistStatus;
	}

	public void setSpecialistStatus(String specialistStatus) {
		this.specialistStatus = specialistStatus;
	}

	public Long getSpecialistUserId() {
		return this.specialistUserId;
	}

	public void setSpecialistUserId(Long specialistUserId) {
		this.specialistUserId = specialistUserId;
	}

	public String getSpecialistopinion() {
		return this.specialistopinion;
	}

	public void setSpecialistopinion(String specialistopinion) {
		this.specialistopinion = specialistopinion;
	}

	public String getSpecialistreport() {
		return this.specialistreport;
	}

	public void setSpecialistreport(String specialistreport) {
		this.specialistreport = specialistreport;
	}

	public Long getSpecilaistmedcatrec() {
		return this.specilaistmedcatrec;
	}

	public void setSpecilaistmedcatrec(Long specilaistmedcatrec) {
		this.specilaistmedcatrec = specilaistmedcatrec;
	}

	public Date getSpecilaistopiniondate() {
		return this.specilaistopiniondate;
	}

	public void setSpecilaistopiniondate(Date specilaistopiniondate) {
		this.specilaistopiniondate = specilaistopiniondate;
	}

	public String getSpecilaistopinionremark() {
		return this.specilaistopinionremark;
	}

	public void setSpecilaistopinionremark(String specilaistopinionremark) {
		this.specilaistopinionremark = specilaistopinionremark;
	}

	public String getSpecilaisttreatmentadvice() {
		return this.specilaisttreatmentadvice;
	}

	public void setSpecilaisttreatmentadvice(String specilaisttreatmentadvice) {
		this.specilaisttreatmentadvice = specilaisttreatmentadvice;
	}

	public String getSpeech() {
		return this.speech;
	}

	public void setSpeech(String speech) {
		this.speech = speech;
	}

	public String getSpeechMentalCapacity() {
		return this.speechMentalCapacity;
	}

	public void setSpeechMentalCapacity(String speechMentalCapacity) {
		this.speechMentalCapacity = speechMentalCapacity;
	}

	public String getSpent() {
		return this.spent;
	}

	public void setSpent(String spent) {
		this.spent = spent;
	}

	public String getSpine() {
		return this.spine;
	}

	public void setSpine(String spine) {
		this.spine = spine;
	}

	public String getSplDisability() {
		return this.splDisability;
	}

	public void setSplDisability(String splDisability) {
		this.splDisability = splDisability;
	}

	 

	public String getSportman() {
		return this.sportman;
	}

	public void setSportman(String sportman) {
		this.sportman = sportman;
	}

	public String getSrCreatine() {
		return this.srCreatine;
	}

	public void setSrCreatine(String srCreatine) {
		this.srCreatine = srCreatine;
	}

	public String getStateServiceFactor() {
		return this.stateServiceFactor;
	}

	public void setStateServiceFactor(String stateServiceFactor) {
		this.stateServiceFactor = stateServiceFactor;
	}

	public String getStateTheNatureOfDiseaseDu() {
		return this.stateTheNatureOfDiseaseDu;
	}

	public void setStateTheNatureOfDiseaseDu(String stateTheNatureOfDiseaseDu) {
		this.stateTheNatureOfDiseaseDu = stateTheNatureOfDiseaseDu;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStd() {
		return this.std;
	}

	public void setStd(String std) {
		this.std = std;
	}

	public String getSugar() {
		return this.sugar;
	}

	public void setSugar(String sugar) {
		this.sugar = sugar;
	}

	public String getSugarf() {
		return this.sugarf;
	}

	public void setSugarf(String sugarf) {
		this.sugarf = sugarf;
	}

	public String getSugarpp() {
		return this.sugarpp;
	}

	public void setSugarpp(String sugarpp) {
		this.sugarpp = sugarpp;
	}

	public Date getSurgeryDate() {
		return this.surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public String getSystexam() {
		return this.systexam;
	}

	public void setSystexam(String systexam) {
		this.systexam = systexam;
	}

	public Long getTemprature() {
		return this.temprature;
	}

	public void setTemprature(Long temprature) {
		this.temprature = temprature;
	}

	public String getThroat() {
		return this.throat;
	}

	public void setThroat(String throat) {
		this.throat = throat;
	}

	public String getTlc() {
		return this.tlc;
	}

	public void setTlc(String tlc) {
		this.tlc = tlc;
	}

	public String getTotalDefectiveTeeth() {
		return this.totalDefectiveTeeth;
	}

	public void setTotalDefectiveTeeth(String totalDefectiveTeeth) {
		this.totalDefectiveTeeth = totalDefectiveTeeth;
	}

	public String getTotalNoDentalPoint() {
		return this.totalNoDentalPoint;
	}

	public void setTotalNoDentalPoint(String totalNoDentalPoint) {
		this.totalNoDentalPoint = totalNoDentalPoint;
	}

	public String getTotalService() {
		return this.totalService;
	}

	public void setTotalService(String totalService) {
		this.totalService = totalService;
	}

	public String getTotalTeeth() {
		return this.totalTeeth;
	}

	public void setTotalTeeth(String totalTeeth) {
		this.totalTeeth = totalTeeth;
	}

	public String getTrachoma() {
		return this.trachoma;
	}

	public void setTrachoma(String trachoma) {
		this.trachoma = trachoma;
	}

	public Long getTradeNature() {
		return this.tradeNature;
	}

	public void setTradeNature(Long tradeNature) {
		this.tradeNature = tradeNature;
	}

	public Date getTranmittedDate() {
		return this.tranmittedDate;
	}

	public void setTranmittedDate(Date tranmittedDate) {
		this.tranmittedDate = tranmittedDate;
	}

	public String getTranmittedDisease() {
		return this.tranmittedDisease;
	}

	public void setTranmittedDisease(String tranmittedDisease) {
		this.tranmittedDisease = tranmittedDisease;
	}

	public String getTreatment() {
		return this.treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getTreatmentMonth() {
		return this.treatmentMonth;
	}

	public void setTreatmentMonth(String treatmentMonth) {
		this.treatmentMonth = treatmentMonth;
	}

	public String getTreatmentYear() {
		return this.treatmentYear;
	}

	public void setTreatmentYear(String treatmentYear) {
		this.treatmentYear = treatmentYear;
	}

	public String getTremors() {
		return this.tremors;
	}

	public void setTremors(String tremors) {
		this.tremors = tremors;
	}

	public String getTriglycerides() {
		return this.triglycerides;
	}

	public void setTriglycerides(String triglycerides) {
		this.triglycerides = triglycerides;
	}

	public String getTympanicMembranceIntact() {
		return this.tympanicMembranceIntact;
	}

	public void setTympanicMembranceIntact(String tympanicMembranceIntact) {
		this.tympanicMembranceIntact = tympanicMembranceIntact;
	}

	public String getTympanicl() {
		return this.tympanicl;
	}

	public void setTympanicl(String tympanicl) {
		this.tympanicl = tympanicl;
	}

	public String getTympanicr() {
		return this.tympanicr;
	}

	public void setTympanicr(String tympanicr) {
		this.tympanicr = tympanicr;
	}

	public Long getTypeOfEntry() {
		return this.typeOfEntry;
	}

	public void setTypeOfEntry(Long typeOfEntry) {
		this.typeOfEntry = typeOfEntry;
	}

	public String getTypeofcommision() {
		return this.typeofcommision;
	}

	public void setTypeofcommision(String typeofcommision) {
		this.typeofcommision = typeofcommision;
	}

	public String getUTLl() {
		return this.uTLl;
	}

	public void setUTLl(String uTLl) {
		this.uTLl = uTLl;
	}

	public String getUTLr() {
		return this.uTLr;
	}

	public void setUTLr(String uTLr) {
		this.uTLr = uTLr;
	}

	public String getUTUl() {
		return this.uTUl;
	}

	public void setUTUl(String uTUl) {
		this.uTUl = uTUl;
	}

	public String getUTUr() {
		return this.uTUr;
	}

	public void setUTUr(String uTUr) {
		this.uTUr = uTUr;
	}

	public String getUnderscendedTest() {
		return this.underscendedTest;
	}

	public void setUnderscendedTest(String underscendedTest) {
		this.underscendedTest = underscendedTest;
	}

	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getUnsaveableTeeth() {
		return this.unsaveableTeeth;
	}

	public void setUnsaveableTeeth(String unsaveableTeeth) {
		this.unsaveableTeeth = unsaveableTeeth;
	}

	public String getUnserteeth() {
		return this.unserteeth;
	}

	public void setUnserteeth(String unserteeth) {
		this.unserteeth = unserteeth;
	}

	public Date getUploadeddate() {
		return this.uploadeddate;
	}

	public void setUploadeddate(Date uploadeddate) {
		this.uploadeddate = uploadeddate;
	}

	public String getUpperLimbs() {
		return this.upperLimbs;
	}

	public void setUpperLimbs(String upperLimbs) {
		this.upperLimbs = upperLimbs;
	}

	public String getUrea() {
		return this.urea;
	}

	public void setUrea(String urea) {
		this.urea = urea;
	}

	public String getUricacid() {
		return this.uricacid;
	}

	public void setUricacid(String uricacid) {
		this.uricacid = uricacid;
	}

	public String getUsgAbdomen() {
		return this.usgAbdomen;
	}

	public void setUsgAbdomen(String usgAbdomen) {
		this.usgAbdomen = usgAbdomen;
	}

	public String getVaginalDischarge() {
		return this.vaginalDischarge;
	}

	public void setVaginalDischarge(String vaginalDischarge) {
		this.vaginalDischarge = vaginalDischarge;
	}

	public String getValgus() {
		return this.valgus;
	}

	public void setValgus(String valgus) {
		this.valgus = valgus;
	}

	public String getVaricocele() {
		return this.varicocele;
	}

	public void setVaricocele(String varicocele) {
		this.varicocele = varicocele;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getVldl() {
		return this.vldl;
	}

	public void setVldl(String vldl) {
		this.vldl = vldl;
	}

	public String getVocationalPerformance() {
		return this.vocationalPerformance;
	}

	public void setVocationalPerformance(String vocationalPerformance) {
		this.vocationalPerformance = vocationalPerformance;
	}

	public String getWaist() {
		return this.waist;
	}

	public void setWaist(String waist) {
		this.waist = waist;
	}

	public String getWaiver() {
		return this.waiver;
	}

	public void setWaiver(String waiver) {
		this.waiver = waiver;
	}

	/*
	 * public Long getWeight() { return this.weight; }
	 * 
	 * public void setWeight(Long weight) { this.weight = weight; }
	 */
	public String getWheretreated() {
		return this.wheretreated;
	}

	public void setWheretreated(String wheretreated) {
		this.wheretreated = wheretreated;
	}

	public String getWhr() {
		return this.whr;
	}

	public void setWhr(String whr) {
		this.whr = whr;
	}

	public String getWithGlassesLDistant() {
		return this.withGlassesLDistant;
	}

	public void setWithGlassesLDistant(String withGlassesLDistant) {
		this.withGlassesLDistant = withGlassesLDistant;
	}

	public String getWithGlassesLNearvision() {
		return this.withGlassesLNearvision;
	}

	public void setWithGlassesLNearvision(String withGlassesLNearvision) {
		this.withGlassesLNearvision = withGlassesLNearvision;
	}

	public String getWithGlassesRDistant() {
		return this.withGlassesRDistant;
	}

	public void setWithGlassesRDistant(String withGlassesRDistant) {
		this.withGlassesRDistant = withGlassesRDistant;
	}

	public String getWithGlassesRNearvision() {
		return this.withGlassesRNearvision;
	}

	public void setWithGlassesRNearvision(String withGlassesRNearvision) {
		this.withGlassesRNearvision = withGlassesRNearvision;
	}

	public String getWithoutGlassesLDistant() {
		return this.withoutGlassesLDistant;
	}

	public void setWithoutGlassesLDistant(String withoutGlassesLDistant) {
		this.withoutGlassesLDistant = withoutGlassesLDistant;
	}

	public String getWithoutGlassesLNearvision() {
		return this.withoutGlassesLNearvision;
	}

	public void setWithoutGlassesLNearvision(String withoutGlassesLNearvision) {
		this.withoutGlassesLNearvision = withoutGlassesLNearvision;
	}

	public String getWithoutGlassesRNearvision() {
		return this.withoutGlassesRNearvision;
	}

	public void setWithoutGlassesRNearvision(String withoutGlassesRNearvision) {
		this.withoutGlassesRNearvision = withoutGlassesRNearvision;
	}

	public String getWrists() {
		return this.wrists;
	}

	public void setWrists(String wrists) {
		this.wrists = wrists;
	}

	public String getWthoutGlassesRDistant() {
		return this.wthoutGlassesRDistant;
	}

	public void setWthoutGlassesRDistant(String wthoutGlassesRDistant) {
		this.wthoutGlassesRDistant = wthoutGlassesRDistant;
	}

	public String getXray() {
		return this.xray;
	}

	public void setXray(String xray) {
		this.xray = xray;
	}

	public String getYearlySerialNo() {
		return this.yearlySerialNo;
	}

	public void setYearlySerialNo(String yearlySerialNo) {
		this.yearlySerialNo = yearlySerialNo;
	}

	@Column(name="PERIPHERAL_PULSATIONS")
	private String  peripheralPulsations;
	
	@Column(name="REGULAR_EXERCISE")
	private String  regularExercise;

	public String getPeripheralPulsations() {
		return peripheralPulsations;
	}

	public void setPeripheralPulsations(String peripheralPulsations) {
		this.peripheralPulsations = peripheralPulsations;
	}

	public String getRegularExercise() {
		return regularExercise;
	}

	public void setRegularExercise(String regularExercise) {
		this.regularExercise = regularExercise;
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

	public String getPollor() {
		return pollor;
	}

	public void setPollor(String pollor) {
		this.pollor = pollor;
	}

	public String getEdema() {
		return edema;
	}

	public void setEdema(String edema) {
		this.edema = edema;
	}

	public String getCyanosis() {
		return cyanosis;
	}

	public void setCyanosis(String cyanosis) {
		this.cyanosis = cyanosis;
	}

	public String getHairNail() {
		return hairNail;
	}

	public void setHairNail(String hairNail) {
		this.hairNail = hairNail;
	}

	public String getIcterus() {
		return icterus;
	}

	public void setIcterus(String icterus) {
		this.icterus = icterus;
	}

	public String getLymphNode() {
		return lymphNode;
	}

	public void setLymphNode(String lymphNode) {
		this.lymphNode = lymphNode;
	}

	public String getClubbing() {
		return clubbing;
	}

	public void setClubbing(String clubbing) {
		this.clubbing = clubbing;
	}

	public String getRemarksPending() {
		return remarksPending;
	}

	public void setRemarksPending(String remarksPending) {
		this.remarksPending = remarksPending;
	}

	public String getRemarksRef() {
		return remarksRef;
	}

	public void setRemarksRef(String remarksRef) {
		this.remarksRef = remarksRef;
	}

	public String getRemarksRej() {
		return remarksRej;
	}

	public void setRemarksRej(String remarksRej) {
		this.remarksRej = remarksRej;
	}

	public String getRemarksForward() {
		return remarksForward;
	}

	public void setRemarksForward(String remarksForward) {
		this.remarksForward = remarksForward;
	}

	 
	@Column(name="REMARKS_Approval")
	private String remarksApproval;

	public String getRemarksApproval() {
		return remarksApproval;
	}

	public void setRemarksApproval(String remarksApproval) {
		this.remarksApproval = remarksApproval;
	}
	@Column(name="GCS")
	private String gcs;

	public String getGcs() {
		return gcs;
	}

	public void setGcs(String gcs) {
		this.gcs = gcs;
	}
	@Column(name="CHEF_COMPLAINT")
	private String chiefComplaint;

	public String getChiefComplaint() {
		return chiefComplaint;
	}

	public void setChiefComplaint(String chiefComplaint) {
		this.chiefComplaint = chiefComplaint;
	}
 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MA_USER_ID",referencedColumnName = "USER_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Users usersMA;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MO_USER_ID",referencedColumnName = "USER_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Users usersMO;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CM_USER_ID",referencedColumnName = "USER_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Users usersCM;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MD_USER_ID",referencedColumnName = "USER_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Users usersMd;
	public Users getUsersMA() {
		return usersMA;
	}

	public void setUsersMA(Users usersMA) {
		this.usersMA = usersMA;
	}

	public Users getUsersMO() {
		return usersMO;
	}

	public void setUsersMO(Users usersMO) {
		this.usersMO = usersMO;
	}

	public Users getUsersCM() {
		return usersCM;
	}

	public void setUsersCM(Users usersCM) {
		this.usersCM = usersCM;
	}

	public Users getUsersMd() {
		return usersMd;
	}

	public void setUsersMd(Users usersMd) {
		this.usersMd = usersMd;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_ID",referencedColumnName = "UNIT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasUnit masUnit;
	
	

	@Temporal(TemporalType.DATE)
	@Column(name="PET_DATE")
	private Date petDate;

	@Column(name="PET_STATUS")
	private String petStatus;
	public Date getPetDate() {
		return petDate;
	}

	public void setPetDate(Date petDate) {
		this.petDate = petDate;
	}

	public String getPetStatus() {
		return petStatus;
	}

	public void setPetStatus(String petStatus) {
		this.petStatus = petStatus;
	}
	
	
	
	
	@Column(name="BP_SYSTOLIC")
	private String bpSystolic;
	
	@Column(name="BP_DIASTOLIC")
	private String bpDiastolic;
	public String getBpSystolic() {
		return bpSystolic;
	}

	public void setBpSystolic(String bpSystolic) {
		this.bpSystolic = bpSystolic;
	}

	public String getBpDiastolic() {
		return bpDiastolic;
	}

	public void setBpDiastolic(String bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}
	
	@Column(name="FOWARD_UNIT_ID")
	private Long forwardUnitId;
	public Long getForwardUnitId() {
		return forwardUnitId;
	}

	public void setForwardUnitId(Long forwardUnitId) {
		this.forwardUnitId = forwardUnitId;
	}

	public String getDisabilityFlag() {
		return disabilityFlag;
	}

	public void setDisabilityFlag(String disabilityFlag) {
		this.disabilityFlag = disabilityFlag;
	}

	public String getDisabilityClaimed() {
		return disabilityClaimed;
	}

	public void setDisabilityClaimed(String disabilityClaimed) {
		this.disabilityClaimed = disabilityClaimed;
	}

	public String getOtherHelathInfo() {
		return otherHelathInfo;
	}

	public void setOtherHelathInfo(String otherHelathInfo) {
		this.otherHelathInfo = otherHelathInfo;
	}

	public Long getWittnessId() {
		return wittnessId;
	}

	public void setWittnessId(Long wittnessId) {
		this.wittnessId = wittnessId;
	}
	
	
	@Column(name="MEDICALCATEGORY_ID")
	private Long medicalCategoryId;

	public Long getMedicalCategoryId() {
		return medicalCategoryId;
	}

	public void setMedicalCategoryId(Long medicalCategoryId) {
		this.medicalCategoryId = medicalCategoryId;
	}
	
	
	@Column(name="LMP_STATUS")
	private String lmpStatus;

	 
	

	public String getLmpStatus() {
		return lmpStatus;
	}

	public void setLmpStatus(String lmpStatus) {
		this.lmpStatus = lmpStatus;
	}

	@Column(name="AUDIOMETRY_RECORD_OTHERS")
	private String audiometryRecordOthers;

	public String getAudiometryRecordOthers() {
		return audiometryRecordOthers;
	}

	public void setAudiometryRecordOthers(String audiometryRecordOthers) {
		this.audiometryRecordOthers = audiometryRecordOthers;
	}
	@Column(name="WITNESS_SIG")
	private Long witnessSig;

	@Column(name="INDIVIDUAL_SIG ")
	private Long individualSig;
	
	@Temporal(TemporalType.DATE)
	@Column(name="WITNESS_DATE")
	private Date witnessDate ;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE ")
	private Date lastChgDate;

	 
	 

	public Long getWitnessSig() {
		return witnessSig;
	}

	public void setWitnessSig(Long witnessSig) {
		this.witnessSig = witnessSig;
	}

	public Date getWitnessDate() {
		return witnessDate;
	}

	public void setWitnessDate(Date witnessDate) {
		this.witnessDate = witnessDate;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getIndividualSig() {
		return individualSig;
	}

	public void setIndividualSig(Long individualSig) {
		this.individualSig = individualSig;
	} 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="WITNESS_SIG",referencedColumnName = "PATIENT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Patient patientWitness;

	public MasUnit getMasUnit() {
		return masUnit;
	}

	public void setMasUnit(MasUnit masUnit) {
		this.masUnit = masUnit;
	}

	 	 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INDIVIDUAL_SIG",referencedColumnName = "PATIENT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Patient individualPatient;

	
	
	 	public Patient getPatientWitness() {
		return patientWitness;
	}

	public void setPatientWitness(Patient patientWitness) {
		this.patientWitness = patientWitness;
	}

	public Patient getIndividualPatient() {
		return individualPatient;
	}

	public void setIndividualPatient(Patient individualPatient) {
		this.individualPatient = individualPatient;
	}

	/*public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}*/
	private Double liver;
	private Double spleen;

	public Double getLiver() {
		return liver;
	}

	public void setLiver(Double liver) {
		this.liver = liver;
	}

	public Double getSpleen() {
		return spleen;
	}

	public void setSpleen(Double spleen) {
		this.spleen = spleen;
	}

	 
	
	@Column(name="GE_TREMORS")
	private String geTremors;

	public String getGeTremors() {
		return geTremors;
	}

	public void setGeTremors(String geTremors) {
		this.geTremors = geTremors;
	}


	@Column(name="MA_DESIGNATION_ID")
	private Long maDesignationId;
	

	@Column(name="MO_DESIGNATION_ID")
	private Long moDesignationId;
	

	@Column(name="CM_DESIGNATION_ID")
	private Long cmDesignationId;
	

	@Column(name="MD_DESIGNATION_ID")
	private Long mdDesignationId;
	

	@Column(name="FORWARDED_DESIGNATION_ID")
	private Long fowardedDesignationId;

	public Long getMaDesignationId() {
		return maDesignationId;
	}

	public void setMaDesignationId(Long maDesignationId) {
		this.maDesignationId = maDesignationId;
	}

	public Long getMoDesignationId() {
		return moDesignationId;
	}

	public void setMoDesignationId(Long moDesignationId) {
		this.moDesignationId = moDesignationId;
	}

	public Long getCmDesignationId() {
		return cmDesignationId;
	}

	public void setCmDesignationId(Long cmDesignationId) {
		this.cmDesignationId = cmDesignationId;
	}

	public Long getMdDesignationId() {
		return mdDesignationId;
	}

	public void setMdDesignationId(Long mdDesignationId) {
		this.mdDesignationId = mdDesignationId;
	}

	public Long getFowardedDesignationId() {
		return fowardedDesignationId;
	}

	public void setFowardedDesignationId(Long fowardedDesignationId) {
		this.fowardedDesignationId = fowardedDesignationId;
	}
 

	@Temporal(TemporalType.DATE)
	@Column(name="MA_DATE")
	private Date maDate ;

	@Temporal(TemporalType.DATE)
	@Column(name="MO_DATE")
	private Date moDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CM_DATE")
	private Date cmDate;
	@Temporal(TemporalType.DATE)
	@Column(name="MD_DATE")
	private Date mdDate;

	public Date getMaDate() {
		return maDate;
	}

	public void setMaDate(Date maDate) {
		this.maDate = maDate;
	}

	public Date getMoDate() {
		return moDate;
	}

	public void setMoDate(Date moDate) {
		this.moDate = moDate;
	}

	public Date getCmDate() {
		return cmDate;
	}

	public void setCmDate(Date cmDate) {
		this.cmDate = cmDate;
	}

	public Date getMdDate() {
		return mdDate;
	}

	public void setMdDate(Date mdDate) {
		this.mdDate = mdDate;
	}
	
	@Column(name="RIDC_ID")
	private Long ridcId;

	public Long getRidcId() {
		return ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

	@Column(name="approve_name")
	private String approveName;
	@Column(name="approve_rank")
	private String approveRank;
	@Column(name="pursue_approve_name")
	private String pursueApproveName;
	@Column(name="pursue_approve_rank")
	private String pursueApproveRank;

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	public String getApproveRank() {
		return approveRank;
	}

	public void setApproveRank(String approveRank) {
		this.approveRank = approveRank;
	}

	public String getPursueApproveName() {
		return pursueApproveName;
	}

	public void setPursueApproveName(String pursueApproveName) {
		this.pursueApproveName = pursueApproveName;
	}

	public String getPursueApproveRank() {
		return pursueApproveRank;
	}

	public void setPursueApproveRank(String pursueApproveRank) {
		this.pursueApproveRank = pursueApproveRank;
	}
	
	@Column(name="place_of_exam")
	private String placeOfExam;

	public String getPlaceOfExam() {
		return placeOfExam;
	}

	public void setPlaceOfExam(String placeOfExam) {
		this.placeOfExam = placeOfExam;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "RANK_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasRank masRank;

	public MasRank getMasRank() {
		return masRank;
	}

	public void setMasRank(MasRank masRank) {
		this.masRank = masRank;
	}	
	@Column(name="AA_PLACE")
	private String aaPlace;
	
	@Column(name="PA_PLACE")
	private String paPlace;
	@Temporal(TemporalType.DATE)
	@Column(name="AA_DATE")
	private Date aaDate;
	@Temporal(TemporalType.DATE)
	@Column(name="PA_DATE")
	private Date paDate;

	public String getAaPlace() {
		return aaPlace;
	}

	public void setAaPlace(String aaPlace) {
		this.aaPlace = aaPlace;
	}

	public String getPaPlace() {
		return paPlace;
	}

	public void setPaPlace(String paPlace) {
		this.paPlace = paPlace;
	}

	public Date getAaDate() {
		return aaDate;
	}

	public void setAaDate(Date aaDate) {
		this.aaDate = aaDate;
	}

	public Date getPaDate() {
		return paDate;
	}

	public void setPaDate(Date paDate) {
		this.paDate = paDate;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	
	
	
	
	
	/*@Column(name="ecgRDMT")
	private String ecgRDMT;
	@Temporal(TemporalType.DATE)
	@Column(name="ecgDated")
	private Date ecgDated;
	@Column(name="ecgReport")
	private String ecgReport;
	@Column(name="ecgAMT")
	private String ecgAMT;
	@Temporal(TemporalType.DATE)
	@Column(name="ecgAmtDated")
	private Date ecgAmtDated;
	@Column(name="ecgAmtReport")
	private String ecgAmtReport;
	@Column(name="xRayChestPANos")
	private String xRayChestPANos;
	@Temporal(TemporalType.DATE)
	@Column(name="xRayChestPANosDated")
	private Date xRayChestPANosDated;
	@Column(name="xRayChestPANosReport")
	String xRayChestPANosReport;*/
	
	
	
	@Column(name="remarksOfLab")
	private String remarksOfLab;
	@Temporal(TemporalType.DATE)
	@Column(name="dateOfLab")
	private	Date dateOfLab;
	@Column(name="signatureOfEyeSpecialist")
	String signatureOfEyeSpecialist;
	@Column(name="remarksOfSurgery")
	private String remarksOfSurgery;
	@Temporal(TemporalType.DATE)
	@Column(name="dateOfSurgery")
	private Date dateOfSurgery;
	
	@Column(name="signatureOfSurgicalSpecialist")
	private String signatureOfSurgicalSpecialist;
	@Column(name="remarksOfDental")
	private String remarksOfDental;
	@Temporal(TemporalType.DATE)
	@Column(name="dateOfDental")
	private Date dateOfDental;
	@Column(name="signatureOfDentalOfficer")
	private	String signatureOfDentalOfficer;
	//@Column(name="evidenceOfTrachoma")
	//String evidenceOfTrachoma;
	//@Column(name="binocularVisionGrade")
	//String binocularVisionGrade;
	@Column(name="manifestHypermetropiaMyopia")
	private String manifestHypermetropiaMyopia;
	//@Column(name="coverTest")
	//String coverTest;
	//@Column(name="diaphragmTest")
	//String diaphragmTest;
	@Column(name="fundiMedia")
	private	String fundiMedia;
	@Column(name="specialfield")
	private String specialField;
	//@Column(name="nightVisualCapacity")
	//String nightVisualCapacity;
	//@Column(name="convergenceC")
	//String convergenceC;
//	@Column(name="convergenceSC")
	//private String convergenceSC;
	@Column(name="accomodationR")
	private String accomodationR;
	@Column(name="accomodationL")
	private String accomodationL;
	@Column(name="manifestHypeMyopiaRemarks")
	private String manifestHypeMyopiaRemarks;
	
	@Temporal(TemporalType.DATE)
	@Column(name="manifestHypeMyopiaDate")
	private Date manifestHypeMyopiaDate;
	@Column(name="noseThroatSinusesRemarks")
	private String noseThroatSinusesRemarks;
	
	@Temporal(TemporalType.DATE)
	@Column(name="noseThroatSinusesDate")
	private Date noseThroatSinusesDate;

	 

	 

	 
	 
	 

	 

	 
	 

	 

	public String getSignatureOfEyeSpecialist() {
		return signatureOfEyeSpecialist;
	}

	public void setSignatureOfEyeSpecialist(String signatureOfEyeSpecialist) {
		this.signatureOfEyeSpecialist = signatureOfEyeSpecialist;
	}

	public String getRemarksOfSurgery() {
		return remarksOfSurgery;
	}

	public void setRemarksOfSurgery(String remarksOfSurgery) {
		this.remarksOfSurgery = remarksOfSurgery;
	}

	 

	public String getSignatureOfSurgicalSpecialist() {
		return signatureOfSurgicalSpecialist;
	}

	public void setSignatureOfSurgicalSpecialist(String signatureOfSurgicalSpecialist) {
		this.signatureOfSurgicalSpecialist = signatureOfSurgicalSpecialist;
	}

	public String getRemarksOfDental() {
		return remarksOfDental;
	}

	public void setRemarksOfDental(String remarksOfDental) {
		this.remarksOfDental = remarksOfDental;
	}

	 

	public String getSignatureOfDentalOfficer() {
		return signatureOfDentalOfficer;
	}

	public void setSignatureOfDentalOfficer(String signatureOfDentalOfficer) {
		this.signatureOfDentalOfficer = signatureOfDentalOfficer;
	}

	public String getManifestHypermetropiaMyopia() {
		return manifestHypermetropiaMyopia;
	}

	public void setManifestHypermetropiaMyopia(String manifestHypermetropiaMyopia) {
		this.manifestHypermetropiaMyopia = manifestHypermetropiaMyopia;
	}

	public String getFundiMedia() {
		return fundiMedia;
	}

	public void setFundiMedia(String fundiMedia) {
		this.fundiMedia = fundiMedia;
	}

	 

	/*
	 * public String getConvergenceSC() { return convergenceSC; }
	 * 
	 * public void setConvergenceSC(String convergenceSC) { this.convergenceSC =
	 * convergenceSC; }
	 * 
	 */	public String getAccomodationR() {
		return accomodationR;
	}

	public void setAccomodationR(String accomodationR) {
		this.accomodationR = accomodationR;
	}

	public String getAccomodationL() {
		return accomodationL;
	}

	public void setAccomodationL(String accomodationL) {
		this.accomodationL = accomodationL;
	}

	public String getManifestHypeMyopiaRemarks() {
		return manifestHypeMyopiaRemarks;
	}

	public void setManifestHypeMyopiaRemarks(String manifestHypeMyopiaRemarks) {
		this.manifestHypeMyopiaRemarks = manifestHypeMyopiaRemarks;
	}

	 

	public String getNoseThroatSinusesRemarks() {
		return noseThroatSinusesRemarks;
	}

	public void setNoseThroatSinusesRemarks(String noseThroatSinusesRemarks) {
		this.noseThroatSinusesRemarks = noseThroatSinusesRemarks;
	}

	 

	public String getSpecialField() {
		return specialField;
	}

	public void setSpecialField(String specialField) {
		this.specialField = specialField;
	}

	 

	 

	 

 

	public Date getDateOfLab() {
		return dateOfLab;
	}

	public void setDateOfLab(Date dateOfLab) {
		this.dateOfLab = dateOfLab;
	}

	public Date getDateOfSurgery() {
		return dateOfSurgery;
	}

	public void setDateOfSurgery(Date dateOfSurgery) {
		this.dateOfSurgery = dateOfSurgery;
	}

	public Date getDateOfDental() {
		return dateOfDental;
	}

	public void setDateOfDental(Date dateOfDental) {
		this.dateOfDental = dateOfDental;
	}

	public Date getManifestHypeMyopiaDate() {
		return manifestHypeMyopiaDate;
	}

	public void setManifestHypeMyopiaDate(Date manifestHypeMyopiaDate) {
		this.manifestHypeMyopiaDate = manifestHypeMyopiaDate;
	}

	public Date getNoseThroatSinusesDate() {
		return noseThroatSinusesDate;
	}

	public void setNoseThroatSinusesDate(Date noseThroatSinusesDate) {
		this.noseThroatSinusesDate = noseThroatSinusesDate;
	}

	public String getRemarksOfLab() {
		return remarksOfLab;
	}

	public void setRemarksOfLab(String remarksOfLab) {
		this.remarksOfLab = remarksOfLab;
	}
	@Column(name="remarksOfGynaecology")
	private String remarksOfGynaecology;
	public String getRemarksOfGynaecology() {
		return remarksOfGynaecology;
	}

	public void setRemarksOfGynaecology(String remarksOfGynaecology) {
		this.remarksOfGynaecology = remarksOfGynaecology;
	}

	public Date getDateOfGynaecology() {
		return dateOfGynaecology;
	}

	public void setDateOfGynaecology(Date dateOfGynaecology) {
		this.dateOfGynaecology = dateOfGynaecology;
	}

	public String getSignatureOfGynaecologist() {
		return signatureOfGynaecologist;
	}

	public void setSignatureOfGynaecologist(String signatureOfGynaecologist) {
		this.signatureOfGynaecologist = signatureOfGynaecologist;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="dateOfGynaecology")
	private Date dateOfGynaecology;
	
	@Column(name="signatureOfGynaecologist")
	private String signatureOfGynaecologist;
	
	public String getpNo() {
		return pNo;
	}

	public void setpNo(String pNo) {
		this.pNo = pNo;
	}

	//////////////////////////start Form 2A New Field///////////////////////////////
	@Column(name="heartDisease")
	private String heartDisease;
	
	@Column(name="amenorrhoeaDysmemhorrheas")
	private String amenorrhoeaDysmemhorrheas;
	
	 
	@Column(name="mediyUnfitBraArmForRejected")
	private String mediyUnfitBraArmForRejected;
	

	@Column(name="mediyUnfitBraArmForDischarged")
	private String mediyUnfitBraArmForDischarged;
	
	
	@Column(name="illOperatInjDisDurat")
	private String illOperatInjDisDurat;
	

	@Column(name="illOperatInjDiseaDuratStayHos")
	private String illOperatInjDiseaDuratStayHos;
	
	@Column(name="otherInformationHealth")
	private String otherInformationHealth;
	
	@Column(name="signatureMedicalOfficer")
	private String signatureMedicalOfficer;
	

	@Column(name="signatureOfCanditate")
	private String signatureOfCanditate;
	@Temporal(TemporalType.DATE)
	@Column(name="signatureMedicalOfficerDate")
	private Date signatureMedicalOfficerDate;
	@Temporal(TemporalType.DATE)
	@Column(name="signatureOfCanditateDate")
	private Date signatureOfCanditateDate;
	
	@Column(name="nightBlindnessForFamily")
	private String nightBlindnessForFamily;

	public String getNightBlindnessForFamily() {
		return nightBlindnessForFamily;
	}

	public void setNightBlindnessForFamily(String nightBlindnessForFamily) {
		this.nightBlindnessForFamily = nightBlindnessForFamily;
	}
	
  	
	public String getHeartDisease() {
		return heartDisease;
	}

	public void setHeartDisease(String heartDisease) {
		this.heartDisease = heartDisease;
	}


	public String getAmenorrhoeaDysmemhorrheas() {
		return amenorrhoeaDysmemhorrheas;
	}

	public void setAmenorrhoeaDysmemhorrheas(String amenorrhoeaDysmemhorrheas) {
		this.amenorrhoeaDysmemhorrheas = amenorrhoeaDysmemhorrheas;
	}


	public String getMediyUnfitBraArmForRejected() {
		return mediyUnfitBraArmForRejected;
	}

	public void setMediyUnfitBraArmForRejected(String mediyUnfitBraArmForRejected) {
		this.mediyUnfitBraArmForRejected = mediyUnfitBraArmForRejected;
	}

	public String getMediyUnfitBraArmForDischarged() {
		return mediyUnfitBraArmForDischarged;
	}

	public void setMediyUnfitBraArmForDischarged(String mediyUnfitBraArmForDischarged) {
		this.mediyUnfitBraArmForDischarged = mediyUnfitBraArmForDischarged;
	}

	public String getIllOperatInjDisDurat() {
		return illOperatInjDisDurat;
	}

	public void setIllOperatInjDisDurat(String illOperatInjDisDurat) {
		this.illOperatInjDisDurat = illOperatInjDisDurat;
	}

	public String getIllOperatInjDiseaDuratStayHos() {
		return illOperatInjDiseaDuratStayHos;
	}

	public void setIllOperatInjDiseaDuratStayHos(String illOperatInjDiseaDuratStayHos) {
		this.illOperatInjDiseaDuratStayHos = illOperatInjDiseaDuratStayHos;
	}

	public String getOtherInformationHealth() {
		return otherInformationHealth;
	}

	public void setOtherInformationHealth(String otherInformationHealth) {
		this.otherInformationHealth = otherInformationHealth;
	}

	public String getSignatureMedicalOfficer() {
		return signatureMedicalOfficer;
	}

	public void setSignatureMedicalOfficer(String signatureMedicalOfficer) {
		this.signatureMedicalOfficer = signatureMedicalOfficer;
	}

	public String getSignatureOfCanditate() {
		return signatureOfCanditate;
	}

	public void setSignatureOfCanditate(String signatureOfCanditate) {
		this.signatureOfCanditate = signatureOfCanditate;
	}

 
	 
	 
	public Date getSignatureMedicalOfficerDate() {
		return signatureMedicalOfficerDate;
	}

	public void setSignatureMedicalOfficerDate(Date signatureMedicalOfficerDate) {
		this.signatureMedicalOfficerDate = signatureMedicalOfficerDate;
	}

	public Date getSignatureOfCanditateDate() {
		return signatureOfCanditateDate;
	}

	public void setSignatureOfCanditateDate(Date signatureOfCanditateDate) {
		this.signatureOfCanditateDate = signatureOfCanditateDate;
	}

	 
	
	@Column(name="signatureOfENTSpecialist")
	private String signatureOfENTSpecialist;

	public String getSignatureOfENTSpecialist() {
		return signatureOfENTSpecialist;
	}

	public void setSignatureOfENTSpecialist(String signatureOfENTSpecialist) {
		this.signatureOfENTSpecialist = signatureOfENTSpecialist;
	}
	
	@Column(name="signatureOfOfficer")
	private String signatureOfOfficer;

	public String getSignatureOfOfficer() {
		return signatureOfOfficer;
	}

	public void setSignatureOfOfficer(String signatureOfOfficer) {
		this.signatureOfOfficer = signatureOfOfficer;
	}
	
	
	@Column(name="memberPlace")
	private String memberPlace;
	
	@Temporal(TemporalType.DATE)
	@Column(name="memberDate")
	private Date memberDate;
	
	
	@Column(name="rankDesiMember1")
	private String rankDesiMember1;
	@Column(name="nameMember1")
	private String nameMember1;
	
	@Column(name="rankDesiMember2")
	private String rankDesiMember2;
	@Column(name="nameMember2")
	private String nameMember2;
	
	@Column(name="rankDesiPresident")
	private String rankDesiPresident;

	public String getMemberPlace() {
		return memberPlace;
	}

	public void setMemberPlace(String memberPlace) {
		this.memberPlace = memberPlace;
	}

	public Date getMemberDate() {
		return memberDate;
	}

	public void setMemberDate(Date memberDate) {
		this.memberDate = memberDate;
	}

	public String getRankDesiMember1() {
		return rankDesiMember1;
	}

	public void setRankDesiMember1(String rankDesiMember1) {
		this.rankDesiMember1 = rankDesiMember1;
	}

	public String getNameMember1() {
		return nameMember1;
	}

	public void setNameMember1(String nameMember1) {
		this.nameMember1 = nameMember1;
	}

	public String getRankDesiMember2() {
		return rankDesiMember2;
	}

	public void setRankDesiMember2(String rankDesiMember2) {
		this.rankDesiMember2 = rankDesiMember2;
	}

	public String getNameMember2() {
		return nameMember2;
	}

	public void setNameMember2(String nameMember2) {
		this.nameMember2 = nameMember2;
	}

	public String getRankDesiPresident() {
		return rankDesiPresident;
	}

	public void setRankDesiPresident(String rankDesiPresident) {
		this.rankDesiPresident = rankDesiPresident;
	}
	 
	@Column(name="erLocrine")
	private String erLocrine;

	public String getErLocrine() {
		return erLocrine;
	}

	public void setErLocrine(String erLocrine) {
		this.erLocrine = erLocrine;
	}
	
	@Column(name="lowerLimbs")
	private String lowerLimbs;

	public String getLowerLimbs() {
		return lowerLimbs;
	}

	public void setLowerLimbs(String lowerLimbs) {
		this.lowerLimbs = lowerLimbs;
	}
	

	
	@Column(name="memberPlaceSub")
	private String memberPlaceSub;
	
	@Temporal(TemporalType.DATE)
	@Column(name="memberDateSub")
	private Date memberDateSub;
	
	
	@Column(name="rankDesiMember1Sub")
	private String rankDesiMember1Sub;
	@Column(name="nameMember1Sub")
	private String nameMember1Sub;
	
	@Column(name="rankDesiMember2Sub")
	private String rankDesiMember2Sub;
	@Column(name="nameMember2Sub")
	private String nameMember2Sub;
	
	@Column(name="rankDesiPresidentSub")
	private String rankDesiPresidentSub;

	@Column(name="namePresidentSub")
	private String namePresidentSub;

	public String getMemberPlaceSub() {
		return memberPlaceSub;
	}

	public void setMemberPlaceSub(String memberPlaceSub) {
		this.memberPlaceSub = memberPlaceSub;
	}

	public Date getMemberDateSub() {
		return memberDateSub;
	}

	public void setMemberDateSub(Date memberDateSub) {
		this.memberDateSub = memberDateSub;
	}

	public String getRankDesiMember1Sub() {
		return rankDesiMember1Sub;
	}

	public void setRankDesiMember1Sub(String rankDesiMember1Sub) {
		this.rankDesiMember1Sub = rankDesiMember1Sub;
	}

	public String getNameMember1Sub() {
		return nameMember1Sub;
	}

	public void setNameMember1Sub(String nameMember1Sub) {
		this.nameMember1Sub = nameMember1Sub;
	}

	public String getRankDesiMember2Sub() {
		return rankDesiMember2Sub;
	}

	public void setRankDesiMember2Sub(String rankDesiMember2Sub) {
		this.rankDesiMember2Sub = rankDesiMember2Sub;
	}

	public String getNameMember2Sub() {
		return nameMember2Sub;
	}

	public void setNameMember2Sub(String nameMember2Sub) {
		this.nameMember2Sub = nameMember2Sub;
	}

	public String getRankDesiPresidentSub() {
		return rankDesiPresidentSub;
	}

	public void setRankDesiPresidentSub(String rankDesiPresidentSub) {
		this.rankDesiPresidentSub = rankDesiPresidentSub;
	}

	public String getNamePresidentSub() {
		return namePresidentSub;
	}

	public void setNamePresidentSub(String namePresidentSub) {
		this.namePresidentSub = namePresidentSub;
	}
	@Column(name="signatureMedicalSpecialist")
	private String signatureMedicalSpecialist;

	public String getSignatureMedicalSpecialist() {
		return signatureMedicalSpecialist;
	}

	public void setSignatureMedicalSpecialist(String signatureMedicalSpecialist) {
		this.signatureMedicalSpecialist = signatureMedicalSpecialist;
	}
	///*
	@Column(name="RMO_HOSPITAL_ID") private Long rmoHospitalId;
	  
	  @Column(name="PDMS_HOSPITAL_ID") private Long pdmsHospitalId;
	 
	  public Long getRmoHospitalId() { return rmoHospitalId; }
	  
	  public void setRmoHospitalId(Long rmoHospitalId) { this.rmoHospitalId =
	  rmoHospitalId; }
	  
	  public Long getPdmsHospitalId() { return pdmsHospitalId; }
	  
	  public void setPdmsHospitalId(Long pdmsHospitalId) { this.pdmsHospitalId =
	  pdmsHospitalId; }
	 //*/
	  
	  @Column(name="PET_STATUS_ON")
		private String petStatusOn;

	public String getPetStatusOn() {
		return petStatusOn;
	}

	public void setPetStatusOn(String petStatusOn) {
		this.petStatusOn = petStatusOn;
	}

	public String getConvergenceC() {
		return convergenceC;
	}

	public void setConvergenceC(String convergenceC) {
		this.convergenceC = convergenceC;
	}

	public String getConvergenceSc() {
		return convergenceSc;
	}

	public void setConvergenceSc(String convergenceSc) {
		this.convergenceSc = convergenceSc;
	}

	public String getLegLength() {
		return legLength;
	}

	public void setLegLength(String legLength) {
		this.legLength = legLength;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BRANCH_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasTrade masTrade;

	public MasTrade getMasTrade() {
		return masTrade;
	}

	public void setMasTrade(MasTrade masTrade) {
		this.masTrade = masTrade;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "HOSPITAL_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasHospital masHospital;

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	
	
	@Column(name="Me_RIDC_ID")
	private Long meRidcId;

	 
	 
	@Column(name="Me_Approval_RIDC_ID")
	private Long meApprovalRidcId;

	public Long getMeRidcId() {
		return meRidcId;
	}

	public void setMeRidcId(Long meRidcId) {
		this.meRidcId = meRidcId;
	}

	public Long getMeApprovalRidcId() {
		return meApprovalRidcId;
	}

	public void setMeApprovalRidcId(Long meApprovalRidcId) {
		this.meApprovalRidcId = meApprovalRidcId;
	}
	  
	  
	  
}