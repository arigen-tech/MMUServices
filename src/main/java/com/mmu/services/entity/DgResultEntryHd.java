package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the DG_RESULT_ENTRY_HD database table.
 * 
 */
@Entity
@Table(name="DG_RESULT_ENTRY_HD")
@NamedQuery(name="DgResultEntryHd.findAll", query="SELECT d FROM DgResultEntryHd d")
@SequenceGenerator(name="DG_RESULT_ENTRY_HEADER_SEQ", sequenceName="DG_RESULT_ENTRY_HEADER_SEQ", allocationSize=1)
public class DgResultEntryHd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_RESULT_ENTRY_HEADER_SEQ")
	@Column(name="RESULT_ENTRY_HD_ID")
	private Long resultEntryId;

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	/*
	 * @Column(name="EMPLOYEE_ID") private Long employeeId;
	 */

	@Column(name="MMU_ID")
	private Long mmuId;
	
	@Column(name="CAMP_ID")
	private Long campId;

	/* private String impression; */

	@Column(name="INPATIENT_ID")
	private Long inpatientId;

	@Column(name="INVESTIGATION_ID")
	private Long investigationId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;


	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;
	
	
	@Column(name="MAIN_CHARGECODE_ID")
	private Long mainChargecodeId;

	@Column(name="PATIENT_ID")
	private Long patientId;

	/*
	 * @Column(name="PRESCRIBED_BY") private Long prescribedBy;
	 */
	/*
	 * @Column(name="RECEIVED_BY") private String receivedBy;
	 */
	/*
	 * @Column(name="RELATION_ID") private Long relationId;
	 */
	/* private String remarks; */

 

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RESULT_DATE")
	private Date resultDate;
	
	
	
	@Column(name="RESULT_NO")
	private String resultNo;

	@Column(name="RESULT_STATUS")
	private String resultStatus;

	/*
	 * @Column(name="RESULT_TYPE") private String resultType;
	 */
	/*
	 * @Column(name="RESULT_UPDATED_BY") private Long resultUpdatedBy;
	 */
	@Column(name="RESULT_VERIFIED_BY")
	private Long resultVerifiedBy;	
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	 @Column(name="SAMPLE_COLLECTION_HD_ID")
	 private Long sampleCollectionHeaderId;
	 
	 @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	 @JoinColumn(name="SAMPLE_COLLECTION_HD_ID", nullable=false,insertable=false,updatable=false)
	 private DgSampleCollectionHd dgSampleCollectionHd;
	 
	 @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	 @JoinColumn(name="RESULT_VERIFIED_BY", nullable=false,insertable=false,updatable=false)
	 private Users resultVerified;

	@Column(name="SUB_CHARGECODE_ID")
	private Long subChargecodeId;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="SUB_CHARGECODE_ID", nullable=false,insertable=false,updatable=false)
	private MasSubChargecode masSubChargecode;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="CREATED_BY", nullable=false,insertable=false,updatable=false)
	private Users users;

	/*
	 * @Column(name="TEMPELATE_ID") private String tempelateId;
	 */
	@Column(name="TEST_ORDER_NO")
	private Long testOrderNo;

	/*
	 * @Column(name="UPDATE_ON") private Timestamp updateOn;
	 */
	@Column(name="VERIFIED")
	private String verified;

	@Temporal(TemporalType.DATE)
	@Column(name="VERIFIED_ON")
	private Date verifiedOn;
	

	@Column(name="VISIT_ID")
	private Long visitId;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "VISIT_ID",nullable=false,insertable=false,updatable=false)
	private Visit visit;
	
	@Column(name="ORDERHD_ID")
	private Long orderHdId;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "ORDERHD_ID",referencedColumnName = "ORDERHD_ID",nullable=false,insertable=false,updatable=false)
	private DgOrderhd dgOrderhd;	
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "INVESTIGATION_ID",nullable=false,insertable=false,updatable=false)
	private DgMasInvestigation dgMasInvestigation;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "PATIENT_ID",nullable=false,insertable=false,updatable=false)
	private Patient patient;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MMU_ID",nullable=false,insertable=false,updatable=false)
	private MasMMU masMmu;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CAMP_ID",nullable=false,insertable=false,updatable=false)
	private MasCamp masCamp;


	public Long getVisitId() {
		return visitId;
	}

	public DgResultEntryHd() {
	}	 

	public Long getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	/*
	 * public Long getEmployeeId() { return this.employeeId; }
	 * 
	 * public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
	 */

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
	/*
	 * public String getImpression() { return this.impression; }
	 * 
	 * public void setImpression(String impression) { this.impression = impression;
	 * }
	 */
	public Long getInpatientId() {
		return this.inpatientId;
	}

	public void setInpatientId(Long inpatientId) {
		this.inpatientId = inpatientId;
	}

	public Long getInvestigationId() {
		return this.investigationId;
	}

	public void setInvestigationId(Long investigationId) {
		this.investigationId = investigationId;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	 
	public Long getMainChargecodeId() {
		return this.mainChargecodeId;
	}

	public void setMainChargecodeId(Long mainChargecodeId) {
		this.mainChargecodeId = mainChargecodeId;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	/*
	 * public Long getPrescribedBy() { return this.prescribedBy; }
	 * 
	 * public void setPrescribedBy(Long prescribedBy) { this.prescribedBy =
	 * prescribedBy; }
	 */

	/*
	 * public String getReceivedBy() { return this.receivedBy; }
	 * 
	 * public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy;
	 * }
	 */

	/*
	 * public Long getRelationId() { return this.relationId; }
	 * 
	 * public void setRelationId(Long relationId) { this.relationId = relationId; }
	 */

	/*
	 * public String getRemarks() { return this.remarks; }
	 * 
	 * public void setRemarks(String remarks) { this.remarks = remarks; }
	 */
	 
	public String getResultNo() {
		return this.resultNo;
	}

	public void setResultNo(String resultNo) {
		this.resultNo = resultNo;
	}

	public String getResultStatus() {
		return this.resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	/*
	 * public String getResultType() { return this.resultType; }
	 * 
	 * public void setResultType(String resultType) { this.resultType = resultType;
	 * }
	 */

	/*
	 * public Long getResultUpdatedBy() { return this.resultUpdatedBy; }
	 * 
	 * public void setResultUpdatedBy(Long resultUpdatedBy) { this.resultUpdatedBy =
	 * resultUpdatedBy; }
	 */

	public Long getResultVerifiedBy() {
		return this.resultVerifiedBy;
	}

	public void setResultVerifiedBy(Long resultVerifiedBy) {
		this.resultVerifiedBy = resultVerifiedBy;
	}

	/*
	 * public Long getSampleCollectionHeaderId() { return
	 * this.FHeaderId; }
	 * 
	 * public void setSampleCollectionHeaderId(Long sampleCollectionHeaderId) {
	 * this.sampleCollectionHeaderId = sampleCollectionHeaderId; }
	 */
	
	

	public Long getSubChargecodeId() {
		return this.subChargecodeId;
	}

	public Long getSampleCollectionHeaderId() {
		return sampleCollectionHeaderId;
	}

	public void setSampleCollectionHeaderId(Long sampleCollectionHeaderId) {
		this.sampleCollectionHeaderId = sampleCollectionHeaderId;
	}

	public void setSubChargecodeId(Long subChargecodeId) {
		this.subChargecodeId = subChargecodeId;
	}

	/*
	 * public String getTempelateId() { return this.tempelateId; }
	 * 
	 * public void setTempelateId(String tempelateId) { this.tempelateId =
	 * tempelateId; }
	 */

	public Long getTestOrderNo() {
		return this.testOrderNo;
	}

	public void setTestOrderNo(Long testOrderNo) {
		this.testOrderNo = testOrderNo;
	}

	/*
	 * public Timestamp getUpdateOn() { return this.updateOn; }
	 * 
	 * public void setUpdateOn(Timestamp updateOn) { this.updateOn = updateOn; }
	 */

	public String getVerified() {
		return this.verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public Date getVerifiedOn() {
		return this.verifiedOn;
	}

	public void setVerifiedOn(Date verifiedOn) {
		this.verifiedOn = verifiedOn;
	}

	public Long getResultEntryId() {
		return resultEntryId;
	}

	public void setResultEntryId(Long resultEntryId) {
		this.resultEntryId = resultEntryId;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getResultDate() {
		return resultDate;
	}

	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Long getOrderHdId() {
		return orderHdId;
	}

	public void setOrderHdId(Long orderHdId) {
		this.orderHdId = orderHdId;
	}

	public DgOrderhd getDgOrderhd() {
		return dgOrderhd;
	}

	public void setDgOrderhd(DgOrderhd dgOrderhd) {
		this.dgOrderhd = dgOrderhd;
	}

	public DgMasInvestigation getDgMasInvestigation() {
		return dgMasInvestigation;
	}

	public void setDgMasInvestigation(DgMasInvestigation dgMasInvestigation) {
		this.dgMasInvestigation = dgMasInvestigation;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public MasSubChargecode getMasSubChargecode() {
		return masSubChargecode;
	}

	public void setMasSubChargecode(MasSubChargecode masSubChargecode) {
		this.masSubChargecode = masSubChargecode;
	}

	public DgSampleCollectionHd getDgSampleCollectionHd() {
		return dgSampleCollectionHd;
	}

	public void setDgSampleCollectionHd(DgSampleCollectionHd dgSampleCollectionHd) {
		this.dgSampleCollectionHd = dgSampleCollectionHd;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	public Users getResultVerified() {
		return resultVerified;
	}

	public void setResultVerified(Users resultVerified) {
		this.resultVerified = resultVerified;
	}

	public MasMMU getMasMmu() {
		return masMmu;
	}

	public void setMasMmu(MasMMU masMmu) {
		this.masMmu = masMmu;
	}

	public MasCamp getMasCamp() {
		return masCamp;
	}

	public void setMasCamp(MasCamp masCamp) {
		this.masCamp = masCamp;
	}
	/*@OneToMany(fetch=FetchType.LAZY) 
	@JoinColumn(name = "RESULT_ENTRY_HD_ID",referencedColumnName = "RESULT_ENTRY_HD_ID", nullable=false,insertable=false,updatable=false)
	private DgResultEntryDt dgResultEntryDt;*/
	
	 
	/*
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="DG_RESULT_ENTRY_HD")
	@JsonBackReference
	private DgResultEntryDt dgResultEntryDt;*/
}