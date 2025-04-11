package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "capture_inspection_details")
@SequenceGenerator(name="CAPTURE_INSPECTION_DETAILS_GENERATOR", sequenceName="capture_inspection_details_seq", allocationSize=1)
public class CaptureInspectionDetails implements Serializable {

    private static final long serialVersionUID = 984998728265139443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="CAPTURE_INSPECTION_DETAILS_GENERATOR")
    @Column(name = "capture_inspection_detail_id")
    private Long inspectionDetailId;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "mmu_id")
    private Long mmuId;

    @Column(name = "inspection_date")
    private Date inspectionDate;

    @Column(name = "vehicle_registration_no")
    private String vehicleRegNo;

    @Column(name = "mmu_location")
    private String mmuLocation;

    @Column(name = "inspection_time")
    private String inspectionTime;

    @Column(name = "inspected_by")
    private Long inspectedBy;

    @Column(name = "audit_status")
    private String auditStatus;

    @Column(name = "validated_by")
    private Long validatedBy;

    @Column(name = "validation_date")
    private Date validationDate;

    @Column(name = "apm_name")
    private String apmName;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "doctor_reg_no")
    private String doctorRegNo;

    @Column(name = "final_audit_remarks")
    private String finalAuditRemarks;

    @Column(name = "commissioner_name")
    private String commissionerName;

    @Column(name = "nodal_officer_name")
    private String nodalOfficerName;

    @Column(name = "auditor_id")
    private Long auditorId;

    @Column(name = "sr_auditor_id")
    private Long srAuditorId;

    @Column(name = "tpa_member1_id")
    private Long tpaMember1Id;

    @Column(name = "tpa_member2_id")
    private Long tpaMember2Id;

    @Transient
    @Column(name = "camp_id")
    private Long campId;

    public Long getCampId() {
        return campId;
    }

    public void setCampId(Long campId) {
        this.campId = campId;
    }

    public Long getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public Long getSrAuditorId() {
        return srAuditorId;
    }

    public void setSrAuditorId(Long srAuditorId) {
        this.srAuditorId = srAuditorId;
    }

    public Long getTpaMember1Id() {
        return tpaMember1Id;
    }

    public void setTpaMember1Id(Long tpaMember1Id) {
        this.tpaMember1Id = tpaMember1Id;
    }

    public Long getTpaMember2Id() {
        return tpaMember2Id;
    }

    public void setTpaMember2Id(Long tpaMember2Id) {
        this.tpaMember2Id = tpaMember2Id;
    }

    public String getCommissionerName() {
        return commissionerName;
    }

    public void setCommissionerName(String commissionerName) {
        this.commissionerName = commissionerName;
    }

    public String getNodalOfficerName() {
        return nodalOfficerName;
    }

    public void setNodalOfficerName(String nodalOfficerName) {
        this.nodalOfficerName = nodalOfficerName;
    }

    public String getFinalAuditRemarks() {
        return finalAuditRemarks;
    }

    @Column(name = "sr_auditor_remarks")
    private String srAuditorRemarks;

    public String getSrAuditorRemarks() {
        return srAuditorRemarks;
    }

    public void setSrAuditorRemarks(String srAuditorRemarks) {
        this.srAuditorRemarks = srAuditorRemarks;
    }

    public void setFinalAuditRemarks(String finalAuditRemarks) {
        this.finalAuditRemarks = finalAuditRemarks;
    }

    public String getApmName() {
        return apmName;
    }

    public void setApmName(String apmName) {
        this.apmName = apmName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorRegNo() {
        return doctorRegNo;
    }

    public void setDoctorRegNo(String doctorRegNo) {
        this.doctorRegNo = doctorRegNo;
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

    public Long getInspectionDetailId() {
        return inspectionDetailId;
    }

    public void setInspectionDetailId(Long inspectionDetailId) {
        this.inspectionDetailId = inspectionDetailId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getMmuId() {
        return mmuId;
    }

    public void setMmuId(Long mmuId) {
        this.mmuId = mmuId;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getMmuLocation() {
        return mmuLocation;
    }

    public void setMmuLocation(String mmuLocation) {
        this.mmuLocation = mmuLocation;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public Long getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(Long inspectedBy) {
        this.inspectedBy = inspectedBy;
    }
}
