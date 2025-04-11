package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the REFERRAL_PATIENT_HD database table.
 * 
 */
@Entity
@Table(name="REFERRAL_PATIENT_HD")
//@NamedQuery(name="ReferralPatientHd.findAll", query="SELECT r FROM ReferralPatientHd r")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="REFERRAL_PATIENT_HD_SEQ", sequenceName="REFERRAL_PATIENT_HD_SEQ", allocationSize=1)
public class ReferralPatientHd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3130144085591770694L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="REFERRAL_PATIENT_HD_SEQ")
	@Column(name="REFREAL_HD_ID")
	private Long refrealHdId;

	@Column(name="EXT_HOSPITAL_ID")
	private Long extHospitalId;

	@Column(name="MMU_ID")
	private Long mmuId;
	
	@Column(name="CAMP_ID")
	private Long campId;

	@Column(name="INT_HOSPITAL_ID")
	private Long intHospitalId;

	@Column(name="OPD_PATIENT_DETAILS_ID")
	private Long opdPatientDetailsId;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Temporal(TemporalType.DATE)
	@Column(name="REFERRAL_INI_DATE")
	private Date referralIniDate;

	@Column(name="REFERRAL_NO")
	private String referralNo;

	@Column(name="REFERRAL_NOTE")
	private String referralNote;

	private String status;

	@Column(name="TREATMENT_TYPE")
	private String treatmentType;
    
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;
	
	@Column(name="DOCTOR_ID")
	private Long doctorId;
	
	@Column(name="DOCTOR_NOTE")
	private String doctorNote;
	
	//bi-directional many-to-one association to MasCamp

	  @ManyToOne(fetch=FetchType.LAZY)
	  @JoinColumn(name="CAMP_ID",nullable=false,insertable=false,updatable=false)
	  private MasCamp masCamp;
	 
	//bi-directional many-to-one association to MasMMU
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MMU_ID",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;

	//bi-directional many-to-one association to MasImpanneledHospital
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EXT_HOSPITAL_ID",nullable=false,insertable=false,updatable=false)
	private MasImpanneledHospital masImpanneledHospital;

	//bi-directional many-to-one association to OpdPatientDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OPD_PATIENT_DETAILS_ID",nullable=false,insertable=false,updatable=false)
	private OpdPatientDetail opdPatientDetail;

	//bi-directional many-to-one association to Patient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID",nullable=false,insertable=false,updatable=false)
	private Patient patient;


	
	//bi-directional many-to-one association to ReferralPatientDt
	@OneToMany(mappedBy = "referralPatientHd")
	@JsonBackReference	
	private List<ReferralPatientDt> referralPatientDts;


	public Long getRefrealHdId() {
		return refrealHdId;
	}


	public void setRefrealHdId(Long refrealHdId) {
		this.refrealHdId = refrealHdId;
	}


	 

	 

	public Long getExtHospitalId() {
		return extHospitalId;
	}


	public void setExtHospitalId(Long extHospitalId) {
		this.extHospitalId = extHospitalId;
	}

    


	public Long getCampId() {
		return campId;
	}


	public void setCampId(Long campId) {
		this.campId = campId;
	}


	public Long getMmuId() {
		return mmuId;
	}


	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}


	public Long getIntHospitalId() {
		return intHospitalId;
	}


	public void setIntHospitalId(Long intHospitalId) {
		this.intHospitalId = intHospitalId;
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


	public Date getReferralIniDate() {
		return referralIniDate;
	}


	public void setReferralIniDate(Date referralIniDate) {
		this.referralIniDate = referralIniDate;
	}


	public String getReferralNo() {
		return referralNo;
	}


	public void setReferralNo(String referralNo) {
		this.referralNo = referralNo;
	}


	public String getReferralNote() {
		return referralNote;
	}


	public void setReferralNote(String referralNote) {
		this.referralNote = referralNote;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getTreatmentType() {
		return treatmentType;
	}


	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}


	

	public MasCamp getMasCamp() {
		return masCamp;
	}


	public void setMasCamp(MasCamp masCamp) {
		this.masCamp = masCamp;
	}


	public MasMMU getMasMMU() {
		return masMMU;
	}


	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}


	public MasImpanneledHospital getMasImpanneledHospital() {
		return masImpanneledHospital;
	}


	public void setMasImpanneledHospital(MasImpanneledHospital masImpanneledHospital) {
		this.masImpanneledHospital = masImpanneledHospital;
	}


	public OpdPatientDetail getOpdPatientDetail() {
		return opdPatientDetail;
	}


	public void setOpdPatientDetail(OpdPatientDetail opdPatientDetail) {
		this.opdPatientDetail = opdPatientDetail;
	}


	public Patient getPatient() {
		return patient;
	}


	public void setPatient(Patient patient) {
		this.patient = patient;
	}


	
	public List<ReferralPatientDt> getReferralPatientDts() {
		return referralPatientDts;
	}


	public void setReferralPatientDts(List<ReferralPatientDt> referralPatientDts) {
		this.referralPatientDts = referralPatientDts;
	}


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


	public String getDoctorNote() {
		return doctorNote;
	}


	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}
	
	
	
	
	
}