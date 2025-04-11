package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the vendor_invoice_approval database table.
 * 
 */
@Entity
@Table(name="vendor_invoice_approval")
@NamedQuery(name="VendorInvoiceApproval.findAll", query="SELECT v FROM VendorInvoiceApproval v")
@SequenceGenerator(name="VENDOR_INVOICE_APPROVAL_SEQ", sequenceName="VENDOR_INVOICE_APPROVAL_SEQ",allocationSize=1)
public class VendorInvoiceApproval implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="VENDOR_INVOICE_APPROVAL_SEQ")
	@Column(name="vendor_invoice_approval_id")
	private Long vendorInvoiceApprovalId;

	@Column(name="authority_action")
	private String authorityAction;

	@Temporal(TemporalType.DATE)
	@Column(name="authority_date")
	private Date authorityDate;

	@Column(name="authority_id")
	private Long authorityId;

	@Column(name="authority_name")
	private String authorityName;

	@Column(name="authority_remarks")
	private String authorityRemarks;

	@Column(name="authority_role")
	private String authorityRole;

	@Column(name="capture_vendor_bill_detail_id")
	private Long captureVendorBillDetailId;

	@Column(name="order_no")
	private Long orderNo;
	
	@Column(name="forward_authority_id")
	private Long forwardAuthorityId;
	
	@Column(name="forward_order_no")
	private Long forwardOrderNo;

	@Column(name="penalty_amount")
	private BigDecimal penaltyAmount;
	
	@Column(name="user_id")
	private Long userId;

	public VendorInvoiceApproval() {
	}

	public Long getVendorInvoiceApprovalId() {
		return this.vendorInvoiceApprovalId;
	}

	public void setVendorInvoiceApprovalId(Long vendorInvoiceApprovalId) {
		this.vendorInvoiceApprovalId = vendorInvoiceApprovalId;
	}

	public String getAuthorityAction() {
		return this.authorityAction;
	}

	public void setAuthorityAction(String authorityAction) {
		this.authorityAction = authorityAction;
	}

	public Date getAuthorityDate() {
		return this.authorityDate;
	}

	public void setAuthorityDate(Date authorityDate) {
		this.authorityDate = authorityDate;
	}

	public Long getAuthorityId() {
		return this.authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthorityName() {
		return this.authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getAuthorityRemarks() {
		return this.authorityRemarks;
	}

	public void setAuthorityRemarks(String authorityRemarks) {
		this.authorityRemarks = authorityRemarks;
	}

	public String getAuthorityRole() {
		return this.authorityRole;
	}

	public void setAuthorityRole(String authorityRole) {
		this.authorityRole = authorityRole;
	}

	public Long getCaptureVendorBillDetailId() {
		return this.captureVendorBillDetailId;
	}

	public void setCaptureVendorBillDetailId(Long captureVendorBillDetailId) {
		this.captureVendorBillDetailId = captureVendorBillDetailId;
	}

	

	public BigDecimal getPenaltyAmount() {
		return this.penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Long getForwardAuthorityId() {
		return forwardAuthorityId;
	}

	public void setForwardAuthorityId(Long forwardAuthorityId) {
		this.forwardAuthorityId = forwardAuthorityId;
	}

	public Long getForwardOrderNo() {
		return forwardOrderNo;
	}

	public void setForwardOrderNo(Long forwardOrderNo) {
		this.forwardOrderNo = forwardOrderNo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

}