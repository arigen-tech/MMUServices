package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 * The persistent class for the PATIENT_PRESCRIPTION_HD database table.
 * 
 */
@Entity
@Table(name="PATIENT_PRESCRIPTION_HD")
@NamedQuery(name="PatientPrescriptionHd.findAll", query="SELECT p FROM PatientPrescriptionHd p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_PRESCRIPTION_HD_SEQ", sequenceName="PATIENT_PRESCRIPTION_HD_SEQ", allocationSize=1)
public class PatientPrescriptionHd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 775052608316291206L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_PRESCRIPTION_HD_SEQ")
	@Column(name="PRESCRIPTION_HD_ID")
	private Long prescriptionHdId;

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	@Column(name="DISPENSARY_ISSUE_NO")
	private String dispensaryIssueNo;

	@Column(name="MMU_ID")
	private Long mmuId;

	
	@Column(name="OPD_PATIENT_DETAILS_ID")
	private Long opdPatientDetailsId;

	@Column(name="PRESCRIPTION_DATE")
	private Timestamp prescriptionDate;
	
	@Column(name="PATIENT_ID")
	private Long patientId;

	
	@Column(name="PRESCRIPTION_NO")
	private Long prescriptionNo;

	private String status;

	@Column(name="VISIT_ID")
	private Long visitId;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="DOCTOR_ID")
	private Long doctorId;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;
	
	@Column(name = "issued_by")
	private Long issuedBy;

	    //bi-directional many-to-one association to Visit
		@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
		@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
		private Visit visit;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID",nullable=false,insertable=false,updatable=false)
	private Patient patient;
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Long getPrescriptionHdId() {
		return prescriptionHdId;
	}

	public void setPrescriptionHdId(Long prescriptionHdId) {
		this.prescriptionHdId = prescriptionHdId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDispensaryIssueNo() {
		return dispensaryIssueNo;
	}

	public void setDispensaryIssueNo(String dispensaryIssueNo) {
		this.dispensaryIssueNo = dispensaryIssueNo;
	}

	



	public Long getOpdPatientDetailsId() {
		return opdPatientDetailsId;
	}

	public void setOpdPatientDetailsId(Long opdPatientDetailsId) {
		this.opdPatientDetailsId = opdPatientDetailsId;
	}

	

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}


	public Long getPrescriptionNo() {
		return prescriptionNo;
	}

	public void setPrescriptionNo(Long prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOCTOR_ID",nullable=false,insertable=false,updatable=false)
	private Users doctorIds;

	public Users getDoctorIds() {
		return doctorIds;
	}

	public void setDoctorIds(Users doctorIds) {
		this.doctorIds = doctorIds;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public Timestamp getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Timestamp prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}
	
	@Column(name="camp_id")
	private Long campId;

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Long getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(Long issuedBy) {
		this.issuedBy = issuedBy;
	}
	
}