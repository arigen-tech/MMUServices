package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "equipment_checklist_validation_history")
@SequenceGenerator(name="EQUIPMENT_CHECKLIST_VALIDATION_HISTORY_GENERATOR", sequenceName="equipment_checklist_validation_history_seq", allocationSize=1)
public class EquipmentChecklistValidationHistory implements Serializable {

    private static final long serialVersionUID = 984998726278776443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="EQUIPMENT_CHECKLIST_VALIDATION_HISTORY_GENERATOR")
    @Column(name = "equipment_checklist_validation_history_id")
    private Long historyId;

    @Column(name = "capture_equipment_checklist_detail_id")
    private Long equipmentDetailId;

    @Column(name = "capture_equipment_checklist_id")
    private Long captureEquipmentChecklistId;

    @Column(name = "equipment_checklist_id")
    private Long equipmentChecklistId;

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

    @Column(name = "penalty_quantity")
    private Integer penaltyQuantity;

    @Column(name = "create_incident")
    private String createIncident;

    public Integer getPenaltyQuantity() {
        return penaltyQuantity;
    }

    public void setPenaltyQuantity(Integer penaltyQuantity) {
        this.penaltyQuantity = penaltyQuantity;
    }

    public String getCreateIncident() {
        return createIncident;
    }

    public void setCreateIncident(String createIncident) {
        this.createIncident = createIncident;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getEquipmentDetailId() {
        return equipmentDetailId;
    }

    public void setEquipmentDetailId(Long equipmentDetailId) {
        this.equipmentDetailId = equipmentDetailId;
    }

    public Long getCaptureEquipmentChecklistId() {
        return captureEquipmentChecklistId;
    }

    public void setCaptureEquipmentChecklistId(Long captureEquipmentChecklistId) {
        this.captureEquipmentChecklistId = captureEquipmentChecklistId;
    }

    public Long getEquipmentChecklistId() {
        return equipmentChecklistId;
    }

    public void setEquipmentChecklistId(Long equipmentChecklistId) {
        this.equipmentChecklistId = equipmentChecklistId;
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

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
