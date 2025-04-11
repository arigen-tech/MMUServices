package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the REFERRAL_PATIENT_DT database table.
 * 
 */
@Entity
@Table(name="REFERRAL_PATIENT_DT")
//@NamedQuery(name="ReferralPatientDt.findAll", query="SELECT r FROM ReferralPatientDt r")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="REFERRAL_PATIENT_DT_SEQ", sequenceName="REFERRAL_PATIENT_DT_SEQ", allocationSize=1)
public class ReferralPatientDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3669272312673482964L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="REFERRAL_PATIENT_DT_SEQ")
	
	@Column(name="REFREAL_DT_ID")
	private Long refrealDtId;

	private String admitted;

	@Column(name="CATEGORY_ID")
	private Long categoryId;

	@Column(name="CLOSE")
	private String close;

	@Column(name="DIAGNOSIS_ID")
	private Long diagnosisId;

	private String disease;

	@Column(name="EXT_DEPARTMENT")
	private String extDepartment;

	@Column(name="FINAL_NOTE")
	private String finalNote;

	private String instruction;

	@Column(name="INT_DEPARTMENT_ID")
	private Long intDepartmentId;

	private String mb;

	@Temporal(TemporalType.DATE)
	@Column(name="REFERRAL_DATE")
	private Date referralDate;

	@Column(name="REFREAL_HD_ID")
	private Long refrealHdId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	public Long getRefrealDtId() {
		return refrealDtId;
	}

	public void setRefrealDtId(Long refrealDtId) {
		this.refrealDtId = refrealDtId;
	}

	public String getAdmitted() {
		return admitted;
	}

	public void setAdmitted(String admitted) {
		this.admitted = admitted;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getExtDepartment() {
		return extDepartment;
	}

	public void setExtDepartment(String extDepartment) {
		this.extDepartment = extDepartment;
	}

	public String getFinalNote() {
		return finalNote;
	}

	public void setFinalNote(String finalNote) {
		this.finalNote = finalNote;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Long getIntDepartmentId() {
		return intDepartmentId;
	}

	public void setIntDepartmentId(Long intDepartmentId) {
		this.intDepartmentId = intDepartmentId;
	}

	public String getMb() {
		return mb;
	}

	public void setMb(String mb) {
		this.mb = mb;
	}

	public Date getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
	}

	public Long getRefrealHdId() {
		return refrealHdId;
	}

	public void setRefrealHdId(Long refrealHdId) {
		this.refrealHdId = refrealHdId;
	}

	public ReferralPatientHd getReferralPatientHd() {
		return referralPatientHd;
	}

	public void setReferralPatientHd(ReferralPatientHd referralPatientHd) {
		this.referralPatientHd = referralPatientHd;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public MasIcd getMasIcd() {
		return masIcd;
	}

	public void setMasIcd(MasIcd masIcd) {
		this.masIcd = masIcd;
	}

	//bi-directional many-to-one association to ReferralPatientHd
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFREAL_HD_ID",nullable=false,insertable=false,updatable=false)
	private ReferralPatientHd referralPatientHd;
	
	//bi-directional many-to-one association to ReferralPatientHd
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INT_DEPARTMENT_ID",nullable=false,insertable=false,updatable=false)
	private MasDepartment masDepartment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DIAGNOSIS_ID",nullable=false,insertable=false,updatable=false)
	private MasIcd masIcd;

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

   
}