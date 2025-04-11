package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "capture_inspection_checklist")
@SequenceGenerator(name="CAPTURE_INSPECTION_CHECKLIST_GENERATOR", sequenceName="capture_inspection_checklist_seq", allocationSize=1)
public class CaptureInspectionChecklist implements Serializable {

    private static final long serialVersionUID = 984998728288776443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="CAPTURE_INSPECTION_CHECKLIST_GENERATOR")
    @Column(name = "capture_inspection_checklist_id")
    private Long captureInspectionChecklistId;

    @Column(name = "capture_inspection_detail_id")
    private Long inspectionDetailId;

    @Column(name = "inspection_checklist_id")
    private Long inspectionChecklistId;

    //bi-directional many-to-one association to Patient
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inspection_checklist_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private MasInspectionChecklist masInspectionChecklist;
    
    @Column(name = "create_incident")
    private String createIncident;

    public MasInspectionChecklist getMasInspectionChecklist() {
		return masInspectionChecklist;
	}

	public void setMasInspectionChecklist(MasInspectionChecklist masInspectionChecklist) {
		this.masInspectionChecklist = masInspectionChecklist;
	}

	@Column(name = "input_value")
    private String inputValue;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "audit_status")
    private String auditStatus;

    @Column(name = "validated_by")
    private Long validatedBy;

    @Column(name = "validation_date")
    private Date validationDate;

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

    public Long getCaptureInspectionChecklistId() {
        return captureInspectionChecklistId;
    }

    public void setCaptureInspectionChecklistId(Long captureInspectionChecklistId) {
        this.captureInspectionChecklistId = captureInspectionChecklistId;
    }

    public Long getInspectionDetailId() {
        return inspectionDetailId;
    }

    public void setInspectionDetailId(Long inspectionDetailId) {
        this.inspectionDetailId = inspectionDetailId;
    }

    public Long getInspectionChecklistId() {
        return inspectionChecklistId;
    }

    public void setInspectionChecklistId(Long inspectionChecklistId) {
        this.inspectionChecklistId = inspectionChecklistId;
    }

    public String getCreateIncident() {
        return createIncident;
    }

    public void setCreateIncident(String createIncident) {
        this.createIncident = createIncident;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
