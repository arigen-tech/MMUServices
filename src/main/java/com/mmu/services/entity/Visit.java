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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the VISIT database table.
 * 
 */
@Entity
@NamedQuery(name="Visit.findAll", query="SELECT v FROM Visit v")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "visit")
//@SequenceGenerator(name="VISIT_VISITID_GENERATOR", sequenceName="VISIT_SEQ", allocationSize=1)
public class Visit implements Serializable {
	private static final long serialVersionUID = 1L;

	//@Id
	//@SequenceGenerator(name="VISIT_VISITID_GENERATOR", sequenceName="visit_seq")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VISIT_VISITID_GENERATOR")
	@Id
	@SequenceGenerator(name="VISIT_VISITID_GENERATOR", sequenceName="visit_seq")
	@GeneratedValue(strategy = GenerationType.AUTO, generator="VISIT_VISITID_GENERATOR")
	@Column(name="visit_id", unique=true, nullable=false)
	private Long visitId;

	@Column(name="camp_id")
	private Long campId;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private Long priority;

	@Column(length=500)
	private String remarks;

	@Column(name="token_no")
	private Long tokenNo;

	@Column(name="visit_date")
	private Timestamp visitDate;

	@Column(name="visit_flag", length=1)
	private String visitFlag;

	@Column(name="visit_status", length=1)
	private String visitStatus;
	
	@Column(name="PATIENT_ID")
	private Long patientId;
	
	@Column(name="mmu_id")
	private Long mmuId;
	
	@Column(name="DOCTOR_ID")
	private Long doctorId;
	
	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	//bi-directional many-to-one association to MasDepartment
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="department_id",nullable=false,insertable=false,updatable=false)
	private MasDepartment masDepartment;

	//bi-directional many-to-one association to Patient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patient_id",nullable=false,insertable=false,updatable=false)
	private Patient patient;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="doctor_id",nullable=false,insertable=false,updatable=false)
	private Users user;

	//bi-directional many-to-one association to MasMmu
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMmu;

	public Visit() {
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Long getCampId() {
		return this.campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getTokenNo() {
		return this.tokenNo;
	}

	public void setTokenNo(Long tokenNo) {
		this.tokenNo = tokenNo;
	}

	public Timestamp getVisitDate() {
		return this.visitDate;
	}

	public void setVisitDate(Timestamp visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitFlag() {
		return this.visitFlag;
	}

	public void setVisitFlag(String visitFlag) {
		this.visitFlag = visitFlag;
	}

	public String getVisitStatus() {
		return this.visitStatus;
	}

	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}

	public MasDepartment getMasDepartment() {
		return this.masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MasMMU getMasMmu() {
		return masMmu;
	}

	public void setMasMmu(MasMMU masMmu) {
		this.masMmu = masMmu;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="visit")
	@JsonBackReference
	private OpdPatientDetail opdPatientDetails;
	
	public OpdPatientDetail getOpdPatientDetails() {
		return opdPatientDetails;
	}



	public void setOpdPatientDetails(OpdPatientDetail opdPatientDetails) {
		this.opdPatientDetails = opdPatientDetails;
	}
	
	@Column(name="audit_exception", length=1)
	private String auditException;
	
	@Column(name="running_exception")
	private String runningException;
	
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="visit")
	@JsonBackReference
	private AuditException auditExceptionRef;

	public String getAuditException() {
		return auditException;
	}

	public void setAuditException(String auditException) {
		this.auditException = auditException;
	}

	public String getRunningException() {
		return runningException;
	}

	public void setRunningException(String runningException) {
		this.runningException = runningException;
	}

	public AuditException getAuditExceptionRef() {
		return auditExceptionRef;
	}

	public void setAuditExceptionRef(AuditException auditExceptionRef) {
		this.auditExceptionRef = auditExceptionRef;
	}
	

	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="visit")
	@JsonBackReference
	private AuditException auditExceptionT;

	public AuditException getAuditExceptionT() {
		return auditExceptionT;
	}

	public void setAuditExceptionT(AuditException auditExceptionT) {
		this.auditExceptionT = auditExceptionT;
	}
	
	@Column(name="ai_audit_exception", length=1)
	private String aiAuditException;

	public String getAiAuditException() {
		return aiAuditException;
	}

	public void setAiAuditException(String aiAuditException) {
		this.aiAuditException = aiAuditException;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="camp_id",nullable=false,insertable=false,updatable=false)
	private MasCamp masCamp;

	public MasCamp getMasCamp() {
		return masCamp;
	}

	public void setMasCamp(MasCamp masCamp) {
		this.masCamp = masCamp;
	}


}