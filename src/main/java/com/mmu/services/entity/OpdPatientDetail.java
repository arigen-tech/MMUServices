package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the OPD_PATIENT_DETAILS database table.
 * 
 */

@Entity
@Table(name="OPD_PATIENT_DETAILS")
@NamedQuery(name="OpdPatientDetail.findAll", query="SELECT o FROM OpdPatientDetail o")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OPD_PATIENT_DETAILS_SEQ", sequenceName="OPD_PATIENT_DETAILS_SEQ", allocationSize=1)


public class OpdPatientDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 64775304860510356L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OPD_PATIENT_DETAILS_SEQ")
	@Column(name="OPD_PATIENT_DETAILS_ID")
	private Long opdPatientDetailsId;

	private String bmi;

	@Column(name="bp_diastolic")
	private String bpDiastolic;

	@Column(name="bp_systolic")
	private String bpSystolic;

	@Column(name="camp_id")
	private Long campId;

	@Column(name="doctor_id")
	private Long doctorId;

	private String height;

	@Column(name="icd_diagnosis")
	private String icdDiagnosis;

	@Column(name="ideal_weight")
	private String idealWeight;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mlc_case")
	private String mlcCase;

	@Column(name="opd_date")
	private Timestamp opdDate;

	@Column(name="past_history")
	private String pastHistory;

	@Column(name="patient_id")
	private Long patientId;

	@Column(name="patient_symptoms")
	private String patientSymptoms;

	private String pulse;

	private String rr;

	private String spo2;

	private String temperature;

	private double varation;

	@Column(name="visit_id")
	private Long visitId;

	private String weight;

	@Column(name="working_diagnosis")
	private String workingDiagnosis;
	
	@Column(name="followup_msg_status")
	private String followupMsgStatus;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID", nullable=false, insertable=false, updatable=false)
	private Patient patient;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOCTOR_ID", nullable=false, insertable=false, updatable=false)
	private Users users;
	
	/*@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID", nullable=false, insertable=false, updatable=false)
	private MasDepartment masDepartment;
	*/
	

	
	 @OneToMany(mappedBy="opdPatientDetail", cascade = CascadeType.ALL)
	 @JsonBackReference
	 private List<DischargeIcdCode> dischargeIcdCode;

	 public Long getOpdPatientDetailsId() {
		return opdPatientDetailsId;
	}

	public void setOpdPatientDetailsId(Long opdPatientDetailsId) {
		this.opdPatientDetailsId = opdPatientDetailsId;
	}

	
	public String getBmi() {
		return bmi;
	}

	public void setBmi(String bmi) {
		this.bmi = bmi;
	}


	
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	
	public String getIcdDiagnosis() {
		return icdDiagnosis;
	}

	public void setIcdDiagnosis(String icdDiagnosis) {
		this.icdDiagnosis = icdDiagnosis;
	}

	
	public String getIdealWeight() {
		return idealWeight;
	}

	public void setIdealWeight(String idealWeight) {
		this.idealWeight = idealWeight;
	}

	

	public Timestamp getOpdDate() {
		return opdDate;
	}

	public void setOpdDate(Timestamp opdDate) {
		this.opdDate = opdDate;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	

	public String getPulse() {
		return pulse;
	}

	public void setPulse(String pulse) {
		this.pulse = pulse;
	}

	

	public String getRr() {
		return rr;
	}

	public void setRr(String rr) {
		this.rr = rr;
	}

	public String getSpo2() {
		return this.spo2;
	}

	
	public void setSpo2(String spo2) {
		this.spo2 = spo2;
	}


	
	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}


	public Double getVaration() {
		return varation;
	}

	public void setVaration(Double varation) {
		this.varation = varation;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWorkingDiagnosis() {
		return workingDiagnosis;
	}

	public void setWorkingDiagnosis(String workingDiagnosis) {
		this.workingDiagnosis = workingDiagnosis;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	

	public String getBpDiastolic() {
		return bpDiastolic;
	}

	public void setBpDiastolic(String bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}

	public String getBpSystolic() {
		return bpSystolic;
	}

	public void setBpSystolic(String bpSystolic) {
		this.bpSystolic = bpSystolic;
	}

    
	//bi-directional many-to-one association to Visit
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
	private Visit visit;

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	/*public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}*/

	

	public List<DischargeIcdCode> getDischargeIcdCode() {
		return dischargeIcdCode;
	}

	public void setDischargeIcdCode(List<DischargeIcdCode> dischargeIcdCode) {
		this.dischargeIcdCode = dischargeIcdCode;
	}


	
	/*@Column(name="RECMMD_MED_ADVICE")
	private String recmndMedAdvice;*/

	/*public String getRecmndMedAdvice() {
		return recmndMedAdvice;
	}

	public void setRecmndMedAdvice(String recmndMedAdvice) {
		this.recmndMedAdvice = recmndMedAdvice;
	}*/

	
	
	@Column(name="OTHER_INVESTIGATION")
	private String otherInvestigation;

	public String getOtherInvestigation() {
		return otherInvestigation;
	}

	public void setOtherInvestigation(String otherInvestigation) {
		this.otherInvestigation = otherInvestigation;
	}

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public String getMlcCase() {
		return mlcCase;
	}

	public void setMlcCase(String mlcCase) {
		this.mlcCase = mlcCase;
	}

	public String getPastHistory() {
		return pastHistory;
	}

	public void setPastHistory(String pastHistory) {
		this.pastHistory = pastHistory;
	}

	public String getPatientSymptoms() {
		return patientSymptoms;
	}

	public void setPatientSymptoms(String patientSymptoms) {
		this.patientSymptoms = patientSymptoms;
	}

	public void setVaration(double varation) {
		this.varation = varation;
	}
	
	@Column(name="follow_up_flag")
	private String followUpFlag;
	
	@Column(name="follow_up_days")
	private Long followUpDays;
	
	@Column(name="sos_flag")
	private String sosFlag;
	
	@Column(name="followup_date")
	private Date followupDate;

	public String getFollowUpFlag() {
		return followUpFlag;
	}

	public void setFollowUpFlag(String followUpFlag) {
		this.followUpFlag = followUpFlag;
	}

	public Long getFollowUpDays() {
		return followUpDays;
	}

	public void setFollowUpDays(Long followUpDays) {
		this.followUpDays = followUpDays;
	}

	public String getSosFlag() {
		return sosFlag;
	}
	
	

	public Date getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}

	public void setSosFlag(String nosFlag) {
		this.sosFlag = nosFlag;
	}
	
	@Column(name="recmmd_med_advice")
	private String recmmdMedAdvice;
	
	@Column(name="dispensary_flag")
	private String dispensaryFlag;
	
	@Column(name="lab_flag")
	private String labFlag;
	
	@Column(name="referral_flag")
	private String reerralFlag;
	
	@Column(name="mlc_flag")
	private String mlcFlag;
	
	@Column(name="treated_as")
	private String treatedAs;
	
	@Column(name="police_station")
	private String policeStation;

	public String getRecmmdMedAdvice() {
		return recmmdMedAdvice;
	}

	public void setRecmmdMedAdvice(String recmmdMedAdvice) {
		this.recmmdMedAdvice = recmmdMedAdvice;
	}

	public String getDispensaryFlag() {
		return dispensaryFlag;
	}

	public void setDispensaryFlag(String dispensaryFlag) {
		this.dispensaryFlag = dispensaryFlag;
	}

	public String getLabFlag() {
		return labFlag;
	}

	public void setLabFlag(String labFlag) {
		this.labFlag = labFlag;
	}

	public String getReerralFlag() {
		return reerralFlag;
	}

	public void setReerralFlag(String reerralFlag) {
		this.reerralFlag = reerralFlag;
	}

	public String getMlcFlag() {
		return mlcFlag;
	}

	public void setMlcFlag(String mlcFlag) {
		this.mlcFlag = mlcFlag;
	}

	public String getTreatedAs() {
		return treatedAs;
	}

	public void setTreatedAs(String treatedAs) {
		this.treatedAs = treatedAs;
	}

	public String getPoliceStation() {
		return policeStation;
	}

	public void setPoliceStation(String policeStation) {
		this.policeStation = policeStation;
	}
	
	@Column(name="police_name")
	private String policeName;
	
	@Column(name="designation")
	private String designation;
	
	@Column(name="id_number")
	private String idNumber;

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	@Column(name="ecg_remarks")
	private String ecgRemarks;

	public String getEcgRemarks() {
		return ecgRemarks;
	}

	public void setEcgRemarks(String ecgRemarks) {
		this.ecgRemarks = ecgRemarks;
	}
	
	
	@Column(name="follow_up_landnarks")
	private String followUpLandmarks;
	
	@Column(name="follow_up_location")
	private String followUpLocation;

	public String getFollowUpLandmarks() {
		return followUpLandmarks;
	}

	public void setFollowUpLandmarks(String followUpLandmarks) {
		this.followUpLandmarks = followUpLandmarks;
	}

	public String getFollowUpLocation() {
		return followUpLocation;
	}

	public void setFollowUpLocation(String followUpLocation) {
		this.followUpLocation = followUpLocation;
	}

	public String getFollowupMsgStatus() {
		return followupMsgStatus;
	}

	public void setFollowupMsgStatus(String followupMsgStatus) {
		this.followupMsgStatus = followupMsgStatus;
	}
	
	
	@Column(name="prescription_flag")
	private String prescriptionFlag;
	
	@Column(name="diagnosis_flag")
	private String diagnosisFlag;
	
	@Column(name="investigation_flag")
	private String investigationFlag;

	public String getPrescriptionFlag() {
		return prescriptionFlag;
	}

	public void setPrescriptionFlag(String prescriptionFlag) {
		this.prescriptionFlag = prescriptionFlag;
	}

	public String getDiagnosisFlag() {
		return diagnosisFlag;
	}

	public void setDiagnosisFlag(String diagnosisFlag) {
		this.diagnosisFlag = diagnosisFlag;
	}

	public String getInvestigationFlag() {
		return investigationFlag;
	}

	public void setInvestigationFlag(String investigationFlag) {
		this.investigationFlag = investigationFlag;
	}
	
	
	

	
	
	
	
	
	

	
}