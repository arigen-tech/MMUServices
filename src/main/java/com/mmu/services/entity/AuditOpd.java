package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the audit_opd database table.
 * 
 */
@Entity
@Table(name="audit_opd")
@NamedQuery(name="AuditOpd.findAll", query="SELECT a FROM AuditOpd a")
@SequenceGenerator(name="audit_opd_seq", sequenceName="audit_opd_seq", allocationSize=1)
public class AuditOpd implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="audit_opd_seq")
	@Column(name="audit_opd_id")
	private Long auditOpdId;

	@Column(name="auditor_remarks")
	private String auditorRemarks;

	@Column(name="DISCHARGE_ICD_CODE_ID")
	private Long dischargeIcdCodeId;

	@Column(name="investigation_id")
	private Long investigationId;

	@Column(name="item_id")
	private Long itemId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mmu_id")
	private Long mmuId;

	@Temporal(TemporalType.DATE)
	@Column(name="opd_date")
	private Date opdDate;

	@Column(name="opd_flag")
	private String opdFlag;

	@Column(name="opd_icd_code_id")
	private Long opdIcdCodeId;

	@Column(name="opd_investigation_id")
	private Long opdInvestigationId;

	@Column(name="opd_item_id")
	private Long opdItemId;

	@Column(name="patient_symptoms_id")
	private Long patientSymptomsId;

	private String status;

	@Column(name="visit_id")
	private Long visitId;
	
	/*@Column(name="audit_id")
	private Long auditId;
*/
	public AuditOpd() {
	}

	public Long getAuditOpdId() {
		return this.auditOpdId;
	}

	public void setAuditOpdId(Long auditOpdId) {
		this.auditOpdId = auditOpdId;
	}

	public String getAuditorRemarks() {
		return this.auditorRemarks;
	}

	public void setAuditorRemarks(String auditorRemarks) {
		this.auditorRemarks = auditorRemarks;
	}

	public Long getDischargeIcdCodeId() {
		return this.dischargeIcdCodeId;
	}

	public void setDischargeIcdCodeId(Long dischargeIcdCodeId) {
		this.dischargeIcdCodeId = dischargeIcdCodeId;
	}

	public Long getInvestigationId() {
		return this.investigationId;
	}

	public void setInvestigationId(Long investigationId) {
		this.investigationId = investigationId;
	}

	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public Date getOpdDate() {
		return this.opdDate;
	}

	public void setOpdDate(Date opdDate) {
		this.opdDate = opdDate;
	}

	public String getOpdFlag() {
		return this.opdFlag;
	}

	public void setOpdFlag(String opdFlag) {
		this.opdFlag = opdFlag;
	}

	public Long getOpdIcdCodeId() {
		return this.opdIcdCodeId;
	}

	public void setOpdIcdCodeId(Long opdIcdCodeId) {
		this.opdIcdCodeId = opdIcdCodeId;
	}

	public Long getOpdInvestigationId() {
		return this.opdInvestigationId;
	}

	public void setOpdInvestigationId(Long opdInvestigationId) {
		this.opdInvestigationId = opdInvestigationId;
	}

	public Long getOpdItemId() {
		return this.opdItemId;
	}

	public void setOpdItemId(Long opdItemId) {
		this.opdItemId = opdItemId;
	}

	public Long getPatientSymptomsId() {
		return this.patientSymptomsId;
	}

	public void setPatientSymptomsId(Long patientSymptomsId) {
		this.patientSymptomsId = patientSymptomsId;
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
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "DISCHARGE_ICD_CODE_ID",nullable=false,insertable=false,updatable=false)
	private DischargeIcdCode dischargeIcdCode;

	public DischargeIcdCode getDischargeIcdCode() {
		return dischargeIcdCode;
	}

	public void setDischargeIcdCode(DischargeIcdCode dischargeIcdCode) {
		this.dischargeIcdCode = dischargeIcdCode;
	}

	/*public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}*/

	@Override
	public String toString() {
		return "AuditOpd [auditOpdId=" + auditOpdId + ", auditorRemarks=" + auditorRemarks + ", dischargeIcdCodeId="
				+ dischargeIcdCodeId + ", investigationId=" + investigationId + ", itemId=" + itemId + ", lastChgBy="
				+ lastChgBy + ", lastChgDate=" + lastChgDate + ", mmuId=" + mmuId + ", opdDate=" + opdDate
				+ ", opdFlag=" + opdFlag + ", opdIcdCodeId=" + opdIcdCodeId + ", opdInvestigationId="
				+ opdInvestigationId + ", opdItemId=" + opdItemId + ", patientSymptomsId=" + patientSymptomsId
				+ ", status=" + status + ", visitId=" + visitId + ",  dischargeIcdCode="
				+ dischargeIcdCode + "]";
	}
	
	
	
	

}