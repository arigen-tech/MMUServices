package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the procedure_details database table.
 * 
 */
@Entity
@Table(name="procedure_details")
@NamedQuery(name="ProcedureDetail.findAll", query="SELECT p FROM ProcedureDetail p")
public class ProcedureDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="procedure_details_id")
	private Long procedureDetailsId;

	@Column(name="camp_id")
	private Long campId;

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
	@Column(name="procedure_date")
	private Date procedureDate;

	@Column(name="procedure_name")
	private String procedureName;

	@Column(name="procedure_remark")
	private String procedureRemark;

	private String status;

	@Column(name="visit_id")
	private Long visitId;

	public ProcedureDetail() {
	}

	public Long getProcedureDetailsId() {
		return this.procedureDetailsId;
	}

	public void setProcedureDetailsId(Long procedureDetailsId) {
		this.procedureDetailsId = procedureDetailsId;
	}

	public Long getCampId() {
		return this.campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
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

	public Date getProcedureDate() {
		return this.procedureDate;
	}

	public void setProcedureDate(Date procedureDate) {
		this.procedureDate = procedureDate;
	}

	public String getProcedureName() {
		return this.procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getProcedureRemark() {
		return this.procedureRemark;
	}

	public void setProcedureRemark(String procedureRemark) {
		this.procedureRemark = procedureRemark;
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