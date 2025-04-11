package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "inspection_checklist_validation_history")
@SequenceGenerator(name="INSPECTION_CHECKLIST_VALIDATION_HISTORY_GENERATOR", sequenceName="inspection_checklist_validation_history_seq", allocationSize=1)
public class InspectionChecklistValidationHistory implements Serializable {

    private static final long serialVersionUID = 984998772638776443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="INSPECTION_CHECKLIST_VALIDATION_HISTORY_GENERATOR")
    @Column(name = "inspection_checklist_validation_history_id")
    private Long historyId;

    @Column(name = "capture_inspection_detail_id")
    private Long inspectionDetailId;

    @Column(name = "capture_inspection_checklist_id")
    private Long captureInspectionChecklistId;

    @Column(name = "inspection_checklist_id")
    private Long inspectionChecklistId;

    @Column(name = "auditor_remarks")
    private String auditorRemarks;

    @Column(name = "uploaded_evidence_file_name")
    private String fileName;

    @Column(name = "is_evidence_file_required")
    private String isEvidenceFileRequired;

    @Column(name = "audit_status")
    private String auditStatus;

    @Column(name = "queried_by")
    private Long queriedBy;

    @Column(name = "query_date")
    private Date queryDate;

    @Column(name = "responded_by")
    private Long respondedBy;

    @Column(name = "response_date")
    private Date responseDate;

    @Column(name = "response")
    private String response;

    public Long getQueriedBy() {
        return queriedBy;
    }

    public void setQueriedBy(Long queriedBy) {
        this.queriedBy = queriedBy;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getRespondedBy() {
        return respondedBy;
    }

    public void setRespondedBy(Long respondedBy) {
        this.respondedBy = respondedBy;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getInspectionDetailId() {
        return inspectionDetailId;
    }

    public void setInspectionDetailId(Long inspectionDetailId) {
        this.inspectionDetailId = inspectionDetailId;
    }

    public Long getCaptureInspectionChecklistId() {
        return captureInspectionChecklistId;
    }

    public void setCaptureInspectionChecklistId(Long captureInspectionChecklistId) {
        this.captureInspectionChecklistId = captureInspectionChecklistId;
    }

    public Long getInspectionChecklistId() {
        return inspectionChecklistId;
    }

    public void setInspectionChecklistId(Long inspectionChecklistId) {
        this.inspectionChecklistId = inspectionChecklistId;
    }

    public String getAuditorRemarks() {
        return auditorRemarks;
    }

    public void setAuditorRemarks(String auditorRemarks) {
        this.auditorRemarks = auditorRemarks;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIsEvidenceFileRequired() {
        return isEvidenceFileRequired;
    }

    public void setIsEvidenceFileRequired(String isEvidenceFileRequired) {
        this.isEvidenceFileRequired = isEvidenceFileRequired;
    }
}
