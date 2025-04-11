package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the referral_patient_details database table.
 * 
 */

@Entity
@Table(name="REFERRAL_PATIENT_DETAILS")
@NamedQuery(name="ReferralPatientHd.findAll", query="SELECT r FROM ReferralPatientHd r")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="REFERRAL_PATIENT_DETAILS_SEQ", sequenceName="REFERRAL_PATIENT_DETAILS_SEQ", allocationSize=1)
public class ReferralPatientDetail implements Serializable {
	private static final long serialVersionUID = 3130144085591770694L;

	@Id
	@Column(name="referral_details_id")
	private Long referralDetailsId;

	@Column(name="camp_id")
	private Long campId;

	@Column(name="doctor_id")
	private Long doctorId;

	@Column(name="doctor_remark")
	private String doctorRemark;

	@Column(name="empanelled_hospital_id")
	private Long empanelledHospitalId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mmu_id")
	private Long mmuId;

	@Column(name="opd_patient_details_id")
	private Long opdPatientDetailsId;

	@Column(name="patient_id")
	private Long patientId;

	@Temporal(TemporalType.DATE)
	@Column(name="referral_date")
	private Date referralDate;

	@Column(name="referral_no")
	private String referralNo;

	@Column(name="referral_note")
	private String referralNote;

	@Column(name="speciality_id")
	private Long specialityId;

	private String status;

	@Column(name="visit_id")
	private Long visitId;

	public ReferralPatientDetail() {
	}

	public Long getReferralDetailsId() {
		return this.referralDetailsId;
	}

	public void setReferralDetailsId(Long referralDetailsId) {
		this.referralDetailsId = referralDetailsId;
	}

	public Long getCampId() {
		return this.campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Long getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorRemark() {
		return this.doctorRemark;
	}

	public void setDoctorRemark(String doctorRemark) {
		this.doctorRemark = doctorRemark;
	}

	public Long getEmpanelledHospitalId() {
		return this.empanelledHospitalId;
	}

	public void setEmpanelledHospitalId(Long empanelledHospitalId) {
		this.empanelledHospitalId = empanelledHospitalId;
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

	public Long getMmuId() {
		return this.mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public Long getOpdPatientDetailsId() {
		return this.opdPatientDetailsId;
	}

	public void setOpdPatientDetailsId(Long opdPatientDetailsId) {
		this.opdPatientDetailsId = opdPatientDetailsId;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Date getReferralDate() {
		return this.referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
	}

	public String getReferralNo() {
		return this.referralNo;
	}

	public void setReferralNo(String referralNo) {
		this.referralNo = referralNo;
	}

	public String getReferralNote() {
		return this.referralNote;
	}

	public void setReferralNote(String referralNote) {
		this.referralNote = referralNote;
	}

	public Long getSpecialityId() {
		return this.specialityId;
	}

	public void setSpecialityId(Long specialityId) {
		this.specialityId = specialityId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

}