package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the PATIENT_MEDICAL_CAT database table.
 * 
 */
@Entity
@Table(name="PATIENT_MEDICAL_CAT")
@NamedQuery(name="PatientMedicalCat.findAll", query="SELECT p FROM PatientMedicalCat p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_MEDICAL_CAT_SEQ", sequenceName="PATIENT_MEDICAL_CAT_SEQ", allocationSize=1)

public class PatientMedicalCat implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PATIENT_MEDICAL_CAT_MEDICALCATID_GENERATOR", sequenceName="PATIENT_MEDICAL_CAT_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_MEDICAL_CAT_MEDICALCATID_GENERATOR")
	@Column(name="MEDICAL_CAT_ID")
	private long medicalCatId;

	@Temporal(TemporalType.DATE)
	@Column(name="CATEGORY_DATE")
	private Date categoryDate;

	@Column(name="CATEGORY_TYPE")
	private String categoryType;

	private Long duration;

	@Column(name="ICD_ID")
	private Long icdId;

	@Column(name="MB_STATUS")
	private String mbStatus;

	@Column(name="MEDICAL_CATEGORY_ID")
	private Long medicalCategoryId;

	@Temporal(TemporalType.DATE)
	@Column(name="NEXT_CATEGORY_DATE")
	private Date nextCategoryDate;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="SYSTEM")
	private String system;
	
	@Column(name="VISIT_ID")
	private Long visitId;
	
	@Column(name="APPLY_FOR")
	private String applyFor;
	
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_ORIGIN")
	private Date dateOfOrigin;
	
	@Column(name="PLACE_OF_ORIGIN")
	private String placeOfOrigin;
	
	@Column(name="RECOMMEND_FLAG")
	private String recommendFlag;
	
	@Column(name="P_MED_CAT_ID")
	private Long pMedCatId;
	
	@Column(name="P_MED_CAT_FID")
	private Long pMedCatFid;
	
	@Temporal(TemporalType.DATE)
	@Column(name="P_MED_CAT_DATE")
	private Date pMedCatDate;
	
	@Column(name="P_MED_FIT_FLAG")
	private String pMedFitFlag;
	
	
	@Column(name="MARK_FLAG")
	private String markFlag;
	
	public String getMarkFlag() {
		return markFlag;
	}

	public void setMarkFlag(String markFlag) {
		this.markFlag = markFlag;
	}

	// bi-directional many-to-one association to Visit
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "VISIT_ID", nullable = false, insertable = false, updatable = false)
	private Visit visit;

	// bi-directional many-to-one association to Visit
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ICD_ID", nullable = false, insertable = false, updatable = false)
	private MasIcd masIcd;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "MEDICAL_CATEGORY_ID", nullable = false, insertable = false, updatable = false)
	private MasMedicalCategory masMedicalCategory;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "P_MED_CAT_ID", nullable = false, insertable = false, updatable = false)
	private MasMedicalCategory masMedicalCategoryFit;
	
	// bi-directional many-to-one association to Visit
		@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
		@JoinColumn(name = "PATIENT_ID", nullable = false, insertable = false, updatable = false)
		private Patient patient;
	
	public PatientMedicalCat() {
	}

	public long getMedicalCatId() {
		return this.medicalCatId;
	}

	public void setMedicalCatId(long medicalCatId) {
		this.medicalCatId = medicalCatId;
	}

	public Date getCategoryDate() {
		return this.categoryDate;
	}

	public void setCategoryDate(Date categoryDate) {
		this.categoryDate = categoryDate;
	}

	public String getCategoryType() {
		return this.categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public Long getDuration() {
		return this.duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getIcdId() {
		return this.icdId;
	}

	public void setIcdId(Long icdId) {
		this.icdId = icdId;
	}

	public String getMbStatus() {
		return this.mbStatus;
	}

	public void setMbStatus(String mbStatus) {
		this.mbStatus = mbStatus;
	}

	public Long getMedicalCategoryId() {
		return this.medicalCategoryId;
	}

	public void setMedicalCategoryId(Long medicalCategoryId) {
		this.medicalCategoryId = medicalCategoryId;
	}

	public Date getNextCategoryDate() {
		return this.nextCategoryDate;
	}

	public void setNextCategoryDate(Date nextCategoryDate) {
		this.nextCategoryDate = nextCategoryDate;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getSystem() {
		return this.system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public MasIcd getMasIcd() {
		return masIcd;
	}

	public void setMasIcd(MasIcd masIcd) {
		this.masIcd = masIcd;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
    		
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public MasMedicalCategory getMasMedicalCategory() {
		return masMedicalCategory;
	}

	public void setMasMedicalCategory(MasMedicalCategory masMedicalCategory) {
		this.masMedicalCategory = masMedicalCategory;
	}

	public String getApplyFor() {
		return applyFor;
	}

	public void setApplyFor(String applyFor) {
		this.applyFor = applyFor;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getDateOfOrigin() {
		return dateOfOrigin;
	}

	public void setDateOfOrigin(Date dateOfOrigin) {
		this.dateOfOrigin = dateOfOrigin;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public String getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(String recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	public Long getpMedCatId() {
		return pMedCatId;
	}

	public void setpMedCatId(Long pMedCatId) {
		this.pMedCatId = pMedCatId;
	}

	public Long getpMedCatFid() {
		return pMedCatFid;
	}

	public void setpMedCatFid(Long pMedCatFid) {
		this.pMedCatFid = pMedCatFid;
	}

	public Date getpMedCatDate() {
		return pMedCatDate;
	}

	public void setpMedCatDate(Date pMedCatDate) {
		this.pMedCatDate = pMedCatDate;
	}

	public String getpMedFitFlag() {
		return pMedFitFlag;
	}

	public void setpMedFitFlag(String pMedFitFlag) {
		this.pMedFitFlag = pMedFitFlag;
	}

	public MasMedicalCategory getMasMedicalCategoryFit() {
		return masMedicalCategoryFit;
	}

	public void setMasMedicalCategoryFit(MasMedicalCategory masMedicalCategoryFit) {
		this.masMedicalCategoryFit = masMedicalCategoryFit;
	}
	
	

}