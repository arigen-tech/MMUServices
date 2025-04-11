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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the DISCHARGE_ICD_CODE database table.
 * 
 */
@Entity
@Table(name="DISCHARGE_ICD_CODE")
@NamedQuery(name="DischargeIcdCode.findAll", query="SELECT d FROM DischargeIcdCode d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="DISCHARGE_ICD_CODE_SEQ", sequenceName="DISCHARGE_ICD_CODE_SEQ", allocationSize=1)
public class DischargeIcdCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DISCHARGE_ICD_CODE_SEQ")
	@Column(name="DISCHARGE_ICD_CODE_ID")
	private long dischargeIcdCodeId;

	@Column(name="camp_id")
	private Long campId;

	@Column(name="icd_id")
	private Long icdId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mark_flag")
	private String markFlag;

	@Column(name="mmu_id")
	private Long mmuId;

	@Column(name="opd_patient_details_id")
	private Long opdPatientDetailsId;

	@Column(name="patient_id")
	private Long patientId;

	@Column(name="visit_id")
	private Long visitId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ICD_ID",nullable=false,insertable=false,updatable=false)
	private MasIcd masIcd;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID",nullable=false,insertable=false,updatable=false)
	private Patient patient;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user1;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
	private Visit visit;

	public DischargeIcdCode() {
	}

	public long getDischargeIcdCodeId() {
		return this.dischargeIcdCodeId;
	}

	public void setDischargeIcdCodeId(long dischargeIcdCodeId) {
		this.dischargeIcdCodeId = dischargeIcdCodeId;
	}

	/*public String getDiagnosisStatus() {
		return this.diagnosisStatus;
	}

	public void setDiagnosisStatus(String diagnosisStatus) {
		this.diagnosisStatus = diagnosisStatus;
	}*/

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public MasIcd getMasIcd() {
		return this.masIcd;
	}

	public void setMasIcd(MasIcd masIcd) {
		this.masIcd = masIcd;
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

	public Long getOpdPatientDetailsId() {
		return opdPatientDetailsId;
	}

	public void setOpdPatientDetailsId(Long opdPatientDetailsId) {
		this.opdPatientDetailsId = opdPatientDetailsId;
	}

	public Long getIcdId() {
		return icdId;
	}

	public void setIcdId(Long icdId) {
		this.icdId = icdId;
	}
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "OPD_PATIENT_DETAILS_ID",nullable=false,insertable=false,updatable=false)
	private OpdPatientDetail opdPatientDetail;

	public OpdPatientDetail getOpdPatientDetail() {
		return opdPatientDetail;
	}

	public void setOpdPatientDetail(OpdPatientDetail opdPatientDetail) {
		this.opdPatientDetail = opdPatientDetail;
	}

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public String getMarkFlag() {
		return markFlag;
	}

	public void setMarkFlag(String markFlag) {
		this.markFlag = markFlag;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}
	
	@Column(name="rec_icd_id")
	private Long recIcdId;
	
	@Column(name="action_flag")
	private String actionFlag;
	
	@Column(name="remarks")
	private String remarks;

	public Long getRecIcdId() {
		return recIcdId;
	}

	public void setRecIcdId(Long recIcdId) {
		this.recIcdId = recIcdId;
	}

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}