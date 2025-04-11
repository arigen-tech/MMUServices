package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the DG_SAMPLE_COLLECTION_HD database table.
 * 
 */
@Entity
@Table(name="DG_SAMPLE_COLLECTION_HD")
@NamedQuery(name="DgSampleCollectionHd.findAll", query="SELECT d FROM DgSampleCollectionHd d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="SAMPLE_COLLECTION_HD_SEQ", sequenceName="SAMPLE_COLLECTION_HD_SEQ", allocationSize=1 )
public class DgSampleCollectionHd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SAMPLE_COLLECTION_HD_SEQ")
	@Column(name="SAMPLE_COLLECTION_HD_ID")
	private Long sampleCollectionHdId;

	
	@Column(name="COLLECTION_DATE")
	private Timestamp collectionDate;

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	
	@Column(name="DIAGNOSIS_DATE")
	private Timestamp diagnosisDate;

	@Column(name="MMU_ID")
	private Long mmuId;
	
	@Column(name="CAMP_ID")
	private Long campId;

	@Column(name="INPATIENT_ID")
	private Long inpatientId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="ORDER_BY_DEPARTMENT")
	private Long orderByDepartment;

	@Column(name="ORDER_ID")
	private Long orderId;

	@Column(name="ORDER_STATUS")
	private String orderStatus;

	@Column(name="ORDERHD_ID")
	private Long orderhdId;

	@Column(name="PATIENT_ID")
	private Long patientId;

	//bi-directional many-to-one association to Patient
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="PATIENT_ID",nullable=false,insertable=false,updatable=false)
		private Patient patient;
		
	@Column(name="SAMPLE_VALIDATION_DATE")
	private Timestamp sampleValidationDate;

	@Column(name="VALIDATED_BY")
	private Long validatedBy;

	@Column(name="VISIT_ID")
	private Long visitId;

	//bi-directional many-to-one association to DgSampleCollectionDt
	@OneToMany(mappedBy="dgSampleCollectionHd")
	private List<DgSampleCollectionDt> dgSampleCollectionDts;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "ORDERHD_ID",nullable=false,insertable=false,updatable=false)
	private DgOrderhd dgOrderHd;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "VALIDATED_BY",nullable=false,insertable=false,updatable=false)
	private Users user;
	
	public DgSampleCollectionHd() {
	}

	public Long getSampleCollectionHdId() {
		return this.sampleCollectionHdId;
	}

	public void setSampleCollectionHdId(long sampleCollectionHdId) {
		this.sampleCollectionHdId = sampleCollectionHdId;
	}

	public Timestamp getCollectionDate() {
		return this.collectionDate;
	}

	public void setCollectionDate(Timestamp collectionDate) {
		this.collectionDate = collectionDate;
	}

	public Long getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Timestamp getDiagnosisDate() {
		return this.diagnosisDate;
	}

	public void setDiagnosisDate(Timestamp diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public Long getMmuId() {
		return this.mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}
	
	public Long getCampId() {
		return this.campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Long getInpatientId() {
		return this.inpatientId;
	}

	public void setInpatientId(Long inpatientId) {
		this.inpatientId = inpatientId;
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

	public Long getOrderByDepartment() {
		return this.orderByDepartment;
	}

	public void setOrderByDepartment(Long orderByDepartment) {
		this.orderByDepartment = orderByDepartment;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderhdId() {
		return this.orderhdId;
	}

	public void setOrderhdId(Long orderhdId) {
		this.orderhdId = orderhdId;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Timestamp getSampleValidationDate() {
		return this.sampleValidationDate;
	}

	public void setSampleValidationDate(Timestamp sampleValidationDate) {
		this.sampleValidationDate = sampleValidationDate;
	}

	public Long getValidatedBy() {
		return this.validatedBy;
	}

	public void setValidatedBy(Long validatedBy) {
		this.validatedBy = validatedBy;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public List<DgSampleCollectionDt> getDgSampleCollectionDts() {
		return this.dgSampleCollectionDts;
	}

	public void setDgSampleCollectionDts(List<DgSampleCollectionDt> dgSampleCollectionDts) {
		this.dgSampleCollectionDts = dgSampleCollectionDts;
	}

	public DgSampleCollectionDt addDgSampleCollectionDt(DgSampleCollectionDt dgSampleCollectionDt) {
		getDgSampleCollectionDts().add(dgSampleCollectionDt);
		dgSampleCollectionDt.setDgSampleCollectionHd(this);

		return dgSampleCollectionDt;
	}

	public DgSampleCollectionDt removeDgSampleCollectionDt(DgSampleCollectionDt dgSampleCollectionDt) {
		getDgSampleCollectionDts().remove(dgSampleCollectionDt);
		dgSampleCollectionDt.setDgSampleCollectionHd(null);

		return dgSampleCollectionDt;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public DgOrderhd getDgOrderHd() {
		return dgOrderHd;
	}

	public void setDgOrderHd(DgOrderhd dgOrderHd) {
		this.dgOrderHd = dgOrderHd;
	}
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public void setSampleCollectionHdId(Long sampleCollectionHdId) {
		this.sampleCollectionHdId = sampleCollectionHdId;
	}
	
}