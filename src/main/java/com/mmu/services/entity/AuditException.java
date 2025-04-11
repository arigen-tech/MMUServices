package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the audit_exception database table.
 * 
 */
@Entity
@Table(name="audit_exception")
@NamedQuery(name="AuditException.findAll", query="SELECT a FROM AuditException a")
@SequenceGenerator(name="audit_exception_seq", sequenceName="audit_exception_seq", allocationSize=1)
public class AuditException implements Serializable {
	private static final long serialVersionUID = 1L;

	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="audit_exception_seq")
	@Column(name="audit_exception_id")
	private Long auditExceptionId;

	@Column(name="diagnosis_flag")
	private String diagnosisFlag;

	
	@Column(name="exception_date")
	private Date exceptionDate;

	@Column(name="investigation_flag")
	private String investigationFlag;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mmu_id")
	private Long mmuId;

	@Column(name="opd_patient_details_id")
	private Long opdPatientDetailsId;

	private String remarks;

	@Column(name="treatment_flag")
	private String treatmentFlag;

	@Column(name="visit_id")
	private Long visitId;

	@Column(name="audit_id")
	private Long auditId;
	
	public AuditException() {
	}

	public Long getAuditExceptionId() {
		return this.auditExceptionId;
	}

	public void setAuditExceptionId(Long auditExceptionId) {
		this.auditExceptionId = auditExceptionId;
	}

	

	public Date getExceptionDate() {
		return this.exceptionDate;
	}

	public void setExceptionDate(Date exceptionDate) {
		this.exceptionDate = exceptionDate;
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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getDiagnosisFlag() {
		return diagnosisFlag;
	}

	public void setDiagnosisFlag(String diagnosisFlag) {
		this.diagnosisFlag = diagnosisFlag;
	}

	public String getInvestigationFlag() {
		return investigationFlag;
	}

	public void setInvestigationFlag(String investigationFlag) {
		this.investigationFlag = investigationFlag;
	}

	public String getTreatmentFlag() {
		return treatmentFlag;
	}

	public void setTreatmentFlag(String treatmentFlag) {
		this.treatmentFlag = treatmentFlag;
	}
	
	//bi-directional many-to-one association to User
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="last_chg_by",nullable=false,insertable=false,updatable=false)
		private Users user;
		
		//bi-directional many-to-one association to Visit
		@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
		@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
		private Visit visit;

		public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}

		public Visit getVisit() {
			return visit;
		}

		public void setVisit(Visit visit) {
			this.visit = visit;
		}
		
		
		@Column(name="final_flag")
		private String finalFlag;

		public String getFinalFlag() {
			return finalFlag;
		}

		public void setFinalFlag(String finalFlag) {
			this.finalFlag = finalFlag;
		}

		public Long getAuditId() {
			return auditId;
		}

		public void setAuditId(Long auditId) {
			this.auditId = auditId;
		}
		
		
		
		
	

}