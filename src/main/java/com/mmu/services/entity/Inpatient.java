package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the INPATIENT database table.
 * 
 */
@Entity
@NamedQuery(name="Inpatient.findAll", query="SELECT i FROM Inpatient i")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="INPATIENT_SEQ", sequenceName="INPATIENT_SEQ", allocationSize=1)
public class Inpatient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="INPATIENT_SEQ")
	@Column(name="INPATIENT_ID")
	private long inpatientId;

	@Column(name="AD_NO")
	private String adNo;

	@Column(name="AD_STATUS")
	private String adStatus;

	@Column(name="AD_WARD_ID")
	private Long adWardId;

	@Column(name="ADMISSION_TYPE_ID")
	private Long admissionTypeId;

	private String age;

	@Column(name="BED_ID")
	private Long bedId;

	@Column(name="CONDITION_STATUS")
	private String conditionStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_ADDMISSION")
	private Date dateOfAddmission;

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	@Column(name="DIET_TYPE")
	private String dietType;

	@Column(name="DISCHARGE_DATE")
	private Timestamp dischargeDate;

	@Column(name="DOCTOR_ID")
	private Long doctorId;

	@Column(name="ICD_ID")
	private Long icdId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="LIST_DATE")
	private Timestamp listDate;

	@Column(name="PATIENT_CONDITION")
	private String patientCondition;

	private String status;

	@Column(name="PATIENT_ID")
	private Long patientId;
	
	@Column(name="VISIT_ID")
	private Long visitId;
	
	@Column(name="HOSPITAL_ID")
	private Long hospitalId;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChangeBy; 
	
	//bi-directional many-to-one association to MasHospital
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID", nullable=false,insertable=false,updatable=false)
	private MasHospital masHospital;

	//bi-directional many-to-one association to Patient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID", nullable=false,insertable=false,updatable=false)
	private Patient patient;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY", nullable=false,insertable=false,updatable=false)
	private Users user1;

	//bi-directional many-to-one association to User
/*	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID", nullable=false,insertable=false,updatable=false)
	private Users user2;*/

	//bi-directional many-to-one association to Visit
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VISIT_ID", nullable=false,insertable=false,updatable=false)
	private Visit visit;


	public Inpatient() {
	}

	public long getInpatientId() {
		return this.inpatientId;
	}

	public void setInpatientId(long inpatientId) {
		this.inpatientId = inpatientId;
	}

	public String getAdNo() {
		return this.adNo;
	}

	public void setAdNo(String adNo) {
		this.adNo = adNo;
	}

	public String getAdStatus() {
		return this.adStatus;
	}

	public void setAdStatus(String adStatus) {
		this.adStatus = adStatus;
	}

	public Long getAdWardId() {
		return this.adWardId;
	}

	public void setAdWardId(Long adWardId) {
		this.adWardId = adWardId;
	}

	public Long getAdmissionTypeId() {
		return this.admissionTypeId;
	}

	public void setAdmissionTypeId(Long admissionTypeId) {
		this.admissionTypeId = admissionTypeId;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Long getBedId() {
		return this.bedId;
	}

	public void setBedId(Long bedId) {
		this.bedId = bedId;
	}

	public String getConditionStatus() {
		return this.conditionStatus;
	}

	public void setConditionStatus(String conditionStatus) {
		this.conditionStatus = conditionStatus;
	}

	public Date getDateOfAddmission() {
		return this.dateOfAddmission;
	}

	public void setDateOfAddmission(Date dateOfAddmission) {
		this.dateOfAddmission = dateOfAddmission;
	}

	public Long getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDietType() {
		return this.dietType;
	}

	public void setDietType(String dietType) {
		this.dietType = dietType;
	}

	public Timestamp getDischargeDate() {
		return this.dischargeDate;
	}

	public void setDischargeDate(Timestamp dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public Long getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}


	public Long getIcdId() {
		return this.icdId;
	}

	public void setIcdId(Long icdId) {
		this.icdId = icdId;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}


	public Timestamp getListDate() {
		return this.listDate;
	}

	public void setListDate(Timestamp listDate) {
		this.listDate = listDate;
	}

	public String getPatientCondition() {
		return this.patientCondition;
	}

	public void setPatientCondition(String patientCondition) {
		this.patientCondition = patientCondition;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getUser1() {
		return this.user1;
	}

	public void setUser1(Users user1) {
		this.user1 = user1;
	}

/*	public Users getUser2() {
		return this.user2;
	}

	public void setUser2(Users user2) {
		this.user2 = user2;
	}*/

	public Visit getVisit() {
		return this.visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	
	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(Long lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}
	
}