package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "medicine_invoice")
@SequenceGenerator(name = "MEDICINE_INVOICE_GENRATOR", sequenceName = "medicine_invoice_seq", allocationSize = 1)
public class CaptureMedicineInvoiceDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7419378423076253335L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MEDICINE_INVOICE_GENRATOR")
	@Column(name = "medicine_invoice_id")
	private Long medicineInvoiceId;

	@Column(name = "city_id")
	private Integer cityId;

	@Column(name = "supplier_type_id")
	private Integer supplierTypeId;

	@Column(name = "supplier_id")
	private Integer supplierId;

	@Column(name = "last_chg_by")
	private Long lastChgBy;

	@Column(name = "last_chg_date")
	private Timestamp lastChangeDate;

	@Column(name = "ulb_id")
	private Integer ulbId;

	@Column(name = "invoice_date")
	private Date invoiceDate;

	@Column(name = "invoice_no")
	private String invoiceNumber;

	@Column(name = "invoice_amount")
	private Long invoiceAmount;

	@Column(name = "invoice_doc")
	private String invoiceDoc;

	@Column(name = "action")
	private String action;

	@Column(name = "payment_date")
	private Date paymentDate;

	@Column(name = "payment_amount")
	private Date paymentAmount;

	@Column(name = "payment_doc")
	private Date paymentDoc;

	@Column(name = "invoice_remark")
	private String invoiceRemark;

	@Column(name = "payment_remark")
	private String paymentRemark;

	@Column(name = "bill_year")
	private Integer billYear;

	@Column(name = "bill_month")
	private Integer billMonth;

	@Column(name = "district_id")
	private Integer districtId;
	
	@Column(name = "batch_no")
	private Integer batchNo;
	
	@Column(name = "user_city_id")
	private String userCityId;

	public Long getMedicineInvoiceId() {
		return medicineInvoiceId;
	}

	public void setMedicineInvoiceId(Long medicineInvoiceId) {
		this.medicineInvoiceId = medicineInvoiceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getSupplierTypeId() {
		return supplierTypeId;
	}

	public void setSupplierTypeId(Integer supplierTypeId) {
		this.supplierTypeId = supplierTypeId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Timestamp lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public Integer getUlbId() {
		return ulbId;
	}

	public void setUlbId(Integer ulbId) {
		this.ulbId = ulbId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Long getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Long invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getInvoiceDoc() {
		return invoiceDoc;
	}

	public void setInvoiceDoc(String invoiceDoc) {
		this.invoiceDoc = invoiceDoc;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Date paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Date getPaymentDoc() {
		return paymentDoc;
	}

	public void setPaymentDoc(Date paymentDoc) {
		this.paymentDoc = paymentDoc;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}

	public String getPaymentRemark() {
		return paymentRemark;
	}

	public void setPaymentRemark(String paymentRemark) {
		this.paymentRemark = paymentRemark;
	}

	public Integer getBillYear() {
		return billYear;
	}

	public void setBillYear(Integer billYear) {
		this.billYear = billYear;
	}

	public Integer getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(Integer billMonth) {
		this.billMonth = billMonth;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Integer batchNo) {
		this.batchNo = batchNo;
	}

	public String getUserCityId() {
		return userCityId;
	}

	public void setUserCityId(String userCityId) {
		this.userCityId = userCityId;
	}
	
	@Column(name = "head_type_id")
	private Long headTypeId;

	public Long getHeadTypeId() {
		return headTypeId;
	}

	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}
	
	@Column(name="phase")
	private String phase;

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}
	
}
