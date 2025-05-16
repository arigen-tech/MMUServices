package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="capture_vendor_bill_detail")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="CAPTURE_VENDOR_BILL_DETAIL_GENERATOR", sequenceName="capture_vendor_bill_detail_seq", allocationSize=1)
public class CaptureVendorBillDetail implements Serializable {
    private static final long serialVersionUID = 118181811817L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator = "CAPTURE_VENDOR_BILL_DETAIL_GENERATOR")
    @Column(name="capture_vendor_bill_detail_id")
    private Long captureVendorBillDetailId;

    @Column(name="vendor_id")
    private Long vendorId;

    @Column(name="city_id")
    private Long cityId;

    @Column(name="bill_month")
    private Integer billMonth;

    @Column(name="bill_year")
    private Integer billYear;

    @Column(name="invoice_no")
    private String invoiceNo;

    @Column(name="invoice_date")
    private Date invoiceDate;

    @Column(name="invoice_amount")
    private Long invoiceAmount;

    @Column(name="upoaded_invoice_file_name")
    private String uploadedFileName;

    @Column(name="created_by")
    private Long createdBy;

    @Column(name="created_on")
    private Date createdOn;

    @Column(name="status")
    private String status;

    @Column(name="auditor_file_name")
    private String auditorFileName;
    
    @Column(name="district_id")
    private Long districtId;
    
    @Column(name="vendor_status")
    private String vendorStatus;
    
    @Column(name="payment_status")
    private String paymentStatus;
    
    @Column(name="current_authority_id")
    private Long currentAuthorityId;
    
    @Column(name="next_authority_id")
    private Long nextAuthorityId;
    
    @Column(name="final_auditors_remarks")
    private String finalAuditorsRemarks;
    
    @Column(name="penalty_amount")
    private Long penaltyAmount;

    @Column(name="final_amount")
    private Long finalAmount;
    
    @Column(name="last_approval_status")
    private String lastApprovalStatus;
    
    @Column(name="initial_penalty_amount")
    private Long initialPenaltyAmount;
    
    @Column(name="auditor_penalty_amount")
    private Long auditorPenaltyAmount;
    
    public String getAuditorFileName() {
        return auditorFileName;
    }

    public void setAuditorFileName(String auditorFileName) {
        this.auditorFileName = auditorFileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCaptureVendorBillDetailId() {
        return captureVendorBillDetailId;
    }

    public void setCaptureVendorBillDetailId(Long captureVendorBillDetailId) {
        this.captureVendorBillDetailId = captureVendorBillDetailId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Integer getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(Integer billMonth) {
        this.billMonth = billMonth;
    }

    public Integer getBillYear() {
        return billYear;
    }

    public void setBillYear(Integer billYear) {
        this.billYear = billYear;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Long invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getVendorStatus() {
		return vendorStatus;
	}

	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	

	public String getFinalAuditorsRemarks() {
		return finalAuditorsRemarks;
	}

	public void setFinalAuditorsRemarks(String finalAuditorsRemarks) {
		this.finalAuditorsRemarks = finalAuditorsRemarks;
	}

	public Long getCurrentAuthorityId() {
		return currentAuthorityId;
	}

	public void setCurrentAuthorityId(Long currentAuthorityId) {
		this.currentAuthorityId = currentAuthorityId;
	}

	public Long getNextAuthorityId() {
		return nextAuthorityId;
	}

	public void setNextAuthorityId(Long nextAuthorityId) {
		this.nextAuthorityId = nextAuthorityId;
	}

	public Long getPenaltyAmount() {
		return penaltyAmount;
	}

	public void setPenaltyAmount(Long penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public Long getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Long finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getLastApprovalStatus() {
		return lastApprovalStatus;
	}

	public void setLastApprovalStatus(String lastApprovalStatus) {
		this.lastApprovalStatus = lastApprovalStatus;
	}

	public Long getInitialPenaltyAmount() {
		return initialPenaltyAmount;
	}

	public void setInitialPenaltyAmount(Long initialPenaltyAmount) {
		this.initialPenaltyAmount = initialPenaltyAmount;
	}
    
	
	@Column(name="phase")
	private String phase;

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public Long getAuditorPenaltyAmount() {
		return auditorPenaltyAmount;
	}

	public void setAuditorPenaltyAmount(Long auditorPenaltyAmount) {
		this.auditorPenaltyAmount = auditorPenaltyAmount;
	}
	
	@Column(name="clear_amount")
    private Long clearAmount;
	
	@Column(name="advanced_payment")
    private Long advancedPayment;
	

	public Long getClearAmount() {
		return clearAmount;
	}

	public void setClearAmount(Long clearAmount) {
		this.clearAmount = clearAmount;
	}

	public Long getAdvancedPayment() {
		return advancedPayment;
	}

	public void setAdvancedPayment(Long advancedPayment) {
		this.advancedPayment = advancedPayment;
	}
	
	
	
	
	
	
	
    
	
}
