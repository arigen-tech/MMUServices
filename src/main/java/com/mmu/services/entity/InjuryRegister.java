package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the INJURY_REGISTER database table.
 * 
 */
@Entity
@Table(name="INJURY_REGISTER")
@NamedQuery(name="InjuryRegister.findAll", query="SELECT i FROM InjuryRegister i")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="INJURY_REGISTER_SEQ", sequenceName="INJURY_REGISTER_SEQ",allocationSize=1)
public class InjuryRegister implements Serializable {
	private static final long serialVersionUID = -81141547352681553L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="INJURY_REGISTER_SEQ")
	@Column(name="INJURY_ID")
	private long injuryId;

	@Temporal(TemporalType.DATE)
	@Column(name="INJURY_DATE")
	private Date injuryDate;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LETTER_DATE")
	private Date letterDate;

	@Column(name="LETTER_NO")
	private String letterNo;

	private String remark;

	@Column(name="RICD_REPORT")
	private long ricdReportId;
	
	@Column(name="RICD_LETTER")
	private long ricdLetterId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APPROVAL_DATE")
	private Date approvalDate;

	@Column(name="APPROVING_AUTHORITY_NAME")
	private String approvingAuthorityName;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	/*
	 * //bi-directional many-to-one association to MasIcd
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="ICD_ID") private MasIcd masIcd;
	 */
	@Column(name = "ICD_DIAGNOSIS")
	private String icdDiagnosis;

	//bi-directional many-to-one association to Patient
	@ManyToOne
	@JoinColumn(name="PATIENT_ID")
	private Patient patient;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	public InjuryRegister() {
	}

	public long getInjuryId() {
		return this.injuryId;
	}

	public void setInjuryId(long injuryId) {
		this.injuryId = injuryId;
	}

	public Date getInjuryDate() {
		return this.injuryDate;
	}

	public void setInjuryDate(Date injuryDate) {
		this.injuryDate = injuryDate;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getLetterDate() {
		return this.letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public String getLetterNo() {
		return this.letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

	public long getRicdReportId() {
		return ricdReportId;
	}

	public void setRicdReportId(long ricdReportId) {
		this.ricdReportId = ricdReportId;
	}

	public long getRicdLetterId() {
		return ricdLetterId;
	}

	public void setRicdLetterId(long ricdLetterId) {
		this.ricdLetterId = ricdLetterId;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovingAuthorityName() {
		return approvingAuthorityName;
	}

	public void setApprovingAuthorityName(String approvingAuthorityName) {
		this.approvingAuthorityName = approvingAuthorityName;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	
	public String getIcdDiagnosis() {
		return icdDiagnosis;
	}

	public void setIcdDiagnosis(String icdDiagnosis) {
		this.icdDiagnosis = icdDiagnosis;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}



}