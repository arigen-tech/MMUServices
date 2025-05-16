package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the vendor_invoice_payment database table.
 * 
 */
@Entity
@Table(name="vendor_invoice_payment")
@NamedQuery(name="VendorInvoicePayment.findAll", query="SELECT v FROM VendorInvoicePayment v")
public class VendorInvoicePayment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VENDOR_INVOICE_PAYMENT_VENDORINVOICEPAYMENTID_GENERATOR", sequenceName="VENDOR_INVOICE_PAYMENT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VENDOR_INVOICE_PAYMENT_VENDORINVOICEPAYMENTID_GENERATOR")
	@Column(name="vendor_invoice_payment_id")
	private Long vendorInvoicePaymentId;

	@Column(name="amount_paid")
	private Long amountPaid;

	@Column(name="capture_vendor_bill_detail_id")
	private Long captureVendorBillDetailId;

	@Column(name="invoice_amount")
	private Long invoiceAmount;

	@Column(name="mode_of_payment")
	private String modeOfPayment;

	@Temporal(TemporalType.DATE)
	@Column(name="payment_date")
	private Date paymentDate;

	@Column(name="payment_remarks")
	private String paymentRemarks;

	@Column(name="penalty_amount")
	private Long penaltyAmount;

	@Column(name="tds_deduction")
	private Long tdsDeduction;
	
	@Column(name="transaction_number")
	private String transactionNumber;

	public VendorInvoicePayment() {
	}

	public Long getVendorInvoicePaymentId() {
		return this.vendorInvoicePaymentId;
	}

	public void setVendorInvoicePaymentId(Long vendorInvoicePaymentId) {
		this.vendorInvoicePaymentId = vendorInvoicePaymentId;
	}

	public Long getAmountPaid() {
		return this.amountPaid;
	}

	public void setAmountPaid(Long amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Long getCaptureVendorBillDetailId() {
		return this.captureVendorBillDetailId;
	}

	public void setCaptureVendorBillDetailId(Long captureVendorBillDetailId) {
		this.captureVendorBillDetailId = captureVendorBillDetailId;
	}

	public Long getInvoiceAmount() {
		return this.invoiceAmount;
	}

	public void setInvoiceAmount(Long invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getModeOfPayment() {
		return this.modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentRemarks() {
		return this.paymentRemarks;
	}

	public void setPaymentRemarks(String paymentRemarks) {
		this.paymentRemarks = paymentRemarks;
	}

	public Long getPenaltyAmount() {
		return this.penaltyAmount;
	}

	public void setPenaltyAmount(Long penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public Long getTdsDeduction() {
		return this.tdsDeduction;
	}

	public void setTdsDeduction(Long tdsDeduction) {
		this.tdsDeduction = tdsDeduction;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	@Column(name="phase")
	private String phase;

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
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