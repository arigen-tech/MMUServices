package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the patient_symptoms database table.
 * 
 */
@Entity
@Table(name="patient_symptoms")
@NamedQuery(name="PatientSymptom.findAll", query="SELECT p FROM PatientSymptom p")
public class PatientSymptom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PATIENT_SYMPTOMS_PATIENTSYMPTOMSID_GENERATOR", sequenceName="PATIENT_SYMPTOMS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PATIENT_SYMPTOMS_PATIENTSYMPTOMSID_GENERATOR")
	@Column(name="patient_symptoms_id", unique=true, nullable=false)
	private Long patientSymptomsId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	//bi-directional many-to-one association to MasCamp
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="camp_id",nullable=false,insertable=false,updatable=false)
	private MasCamp masCamp;

	//bi-directional many-to-one association to MasMmu
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMmu;

	//bi-directional many-to-one association to MasSymptom
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="symptoms_id",nullable=false,insertable=false,updatable=false)
	private MasSymptoms masSymptom;

	//bi-directional many-to-one association to OpdPatientDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="opd_patient_details_id",nullable=false,insertable=false,updatable=false)
	private OpdPatientDetail opdPatientDetail;

	//bi-directional many-to-one association to Patient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patient_id",nullable=false,insertable=false,updatable=false)
	private Patient patient;

	//bi-directional many-to-one association to Visit
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_id",nullable=false,insertable=false,updatable=false)
	private Visit visit;
	
	@Column(name="patient_id")
	private Long patientId;
	
	@Column(name="visit_id")
	private Long visitId;
	
	@Column(name="camp_id")
	private Long campId;
	
	@Column(name="opd_patient_details_id")
	private Long opdPatientDetailId;
	
	@Column(name="symptoms_id")
	private Long symptomId;
	
	@Column(name="mmu_id")
	private Long mmuId;

	public PatientSymptom() {
	}

	public Long getPatientSymptomsId() {
		return this.patientSymptomsId;
	}

	public void setPatientSymptomsId(Long patientSymptomsId) {
		this.patientSymptomsId = patientSymptomsId;
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

	public MasCamp getMasCamp() {
		return this.masCamp;
	}

	public void setMasCamp(MasCamp masCamp) {
		this.masCamp = masCamp;
	}

	public MasMMU getMasMmu() {
		return masMmu;
	}

	public void setMasMmu(MasMMU masMmu) {
		this.masMmu = masMmu;
	}

	public MasSymptoms getMasSymptom() {
		return masSymptom;
	}

	public void setMasSymptom(MasSymptoms masSymptom) {
		this.masSymptom = masSymptom;
	}

	public OpdPatientDetail getOpdPatientDetail() {
		return this.opdPatientDetail;
	}

	public void setOpdPatientDetail(OpdPatientDetail opdPatientDetail) {
		this.opdPatientDetail = opdPatientDetail;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

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

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Long getOpdPatientDetailId() {
		return opdPatientDetailId;
	}

	public void setOpdPatientDetailId(Long opdPatientDetailId) {
		this.opdPatientDetailId = opdPatientDetailId;
	}

	public Long getSymptomId() {
		return symptomId;
	}

	public void setSymptomId(Long symptomId) {
		this.symptomId = symptomId;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}
	
}