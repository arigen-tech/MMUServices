package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="capture_vendor_bill_mmu_detail")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="CAPTURE_VENDOR_BILL_MMU_DETAIL_GENERATOR", sequenceName="capture_vendor_bill_mmu_detail_seq", allocationSize=1)
public class CaptureVendorBillMMUDetail implements Serializable {

    private static final long serialVersionUID = 118181746817L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator = "CAPTURE_VENDOR_BILL_MMU_DETAIL_GENERATOR")
    @Column(name="capture_vendor_bill_mmu_detail_id")
    private Long captureVendorBillMMUDetailId;

    @Column(name="capture_vendor_bill_detail_id")
    private Long captureVendorBillDetailId;

    @Column(name="mmu_id")
    private Long mmuId;

    @Column(name="auditors_remarks")
    private String auditorsRemarks;

    @Column(name="remove_penalty")
    private String removePenalty;

    @Column(name="co_remarks")
    private String coRemarks;
    
    @Column(name="penalty_amount")
	private Long penaltyAmount;
    
    
    @Column(name="manual_penalty_amount")
	private Long manualPenaltyAmount;
    
    @Column(name="manual_penalty_file")
    private String manualPenaltyFile;
    

    public Long getCaptureVendorBillMMUDetailId() {
        return captureVendorBillMMUDetailId;
    }

    public void setCaptureVendorBillMMUDetailId(Long captureVendorBillMMUDetailId) {
        this.captureVendorBillMMUDetailId = captureVendorBillMMUDetailId;
    }

    public Long getCaptureVendorBillDetailId() {
        return captureVendorBillDetailId;
    }

    public void setCaptureVendorBillDetailId(Long captureVendorBillDetailId) {
        this.captureVendorBillDetailId = captureVendorBillDetailId;
    }

    public Long getMmuId() {
        return mmuId;
    }

    public void setMmuId(Long mmuId) {
        this.mmuId = mmuId;
    }

    public String getAuditorsRemarks() {
        return auditorsRemarks;
    }

    public void setAuditorsRemarks(String auditorsRemarks) {
        this.auditorsRemarks = auditorsRemarks;
    }

    public String getRemovePenalty() {
        return removePenalty;
    }

    public void setRemovePenalty(String removePenalty) {
        this.removePenalty = removePenalty;
    }

    public String getCoRemarks() {
        return coRemarks;
    }

    public void setCoRemarks(String coRemarks) {
        this.coRemarks = coRemarks;
    }

	public Long getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(Long penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public Long getManualPenaltyAmount() {
		return manualPenaltyAmount;
	}

	public void setManualPenaltyAmount(Long manualPenaltyAmount) {
		this.manualPenaltyAmount = manualPenaltyAmount;
	}

	public String getManualPenaltyFile() {
		return manualPenaltyFile;
	}

	public void setManualPenaltyFile(String manualPenaltyFile) {
		this.manualPenaltyFile = manualPenaltyFile;
	}
	
	
    
    
}
