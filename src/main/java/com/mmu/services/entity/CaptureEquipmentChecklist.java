package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "capture_equipment_checklist")
@SequenceGenerator(name="CAPTURE_EQUIPMENT_CHECKLIST_GENERATOR", sequenceName="capture_equipment_checklist_seq", allocationSize=1)
public class CaptureEquipmentChecklist implements Serializable {

    private static final long serialVersionUID = 984998722335139443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="CAPTURE_EQUIPMENT_CHECKLIST_GENERATOR")
    @Column(name = "capture_equipment_checklist_id")
    private Long captureEquipmentChecklistId;

    @Column(name = "capture_equipment_checklist_detail_id")
    private Long equipmentChecklistDetailId;

    @Column(name = "equipment_checklist_id")
    private Long equipmentChecklistId;

    @Column(name = "assigned_quantity")
    private Integer assignedQuantity;

    @Column(name = "operational_quantity")
    private Integer operationalQuantity;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "audit_status")
    private String auditStatus;

    @Column(name = "validated_by")
    private Long validatedBy;

    @Column(name = "validation_date")
    private Date validationDate;

    @Column(name = "penalty_quantity")
    private Integer penaltyQuantity;

    @Column(name = "create_incident")
    private String createIncident;

    @Column(name = "incident_date")
    private Date incidentDate;

    @Column(name = "uploaded_file")
    private String uploadedFile;

    @Column(name = "encoded_file")
    private String encodedFile;

    public String getEncodedFile() {
        return encodedFile;
    }

    public void setEncodedFile(String encodedFile) {
        this.encodedFile = encodedFile;
    }

    public String getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(String uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

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

    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(Long validatedBy) {
        this.validatedBy = validatedBy;
    }

    public Long getCaptureEquipmentChecklistId() {
        return captureEquipmentChecklistId;
    }

    public void setCaptureEquipmentChecklistId(Long captureEquipmentChecklistId) {
        this.captureEquipmentChecklistId = captureEquipmentChecklistId;
    }

    public Long getEquipmentChecklistDetailId() {
        return equipmentChecklistDetailId;
    }

    public void setEquipmentChecklistDetailId(Long equipmentChecklistDetailId) {
        this.equipmentChecklistDetailId = equipmentChecklistDetailId;
    }

    public Long getEquipmentChecklistId() {
        return equipmentChecklistId;
    }

    public void setEquipmentChecklistId(Long equipmentChecklistId) {
        this.equipmentChecklistId = equipmentChecklistId;
    }

    public Integer getAssignedQuantity() {
        return assignedQuantity;
    }

    public void setAssignedQuantity(Integer assignedQuantity) {
        this.assignedQuantity = assignedQuantity;
    }

    public Integer getOperationalQuantity() {
        return operationalQuantity;
    }

    public void setOperationalQuantity(Integer operationalQuantity) {
        this.operationalQuantity = operationalQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
