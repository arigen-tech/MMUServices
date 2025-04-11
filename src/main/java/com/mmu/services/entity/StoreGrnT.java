package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the STORE_GRN_T database table.
 * 
 */
@Entity
@Table(name="STORE_GRN_T")
@NamedQuery(name="StoreGrnT.findAll", query="SELECT s FROM StoreGrnT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_GRN_T_GENERATOR", sequenceName="STORE_GRN_T_SEQ",allocationSize=1)
public class StoreGrnT implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2765018874227039503L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_GRN_T_GENERATOR")
	@Column(name="GRN_TRANS_ID")
	private long grnTransId;

	@Column(name="ACCEPTED_QTY")
	private BigDecimal acceptedQty;

	@Column(name="AMOUNT_AFTER_TAX")
	private BigDecimal amountAfterTax;


	@Column(name="BATCH_NO")
	private String batchNo;

	@Column(name="CGST_RATE")
	private BigDecimal cgstRate;

	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRY_DATE")
	private Date expiryDate;

	

	@Temporal(TemporalType.DATE)
	@Column(name="MANUFACTURER_DATE")
	private Date manufacturerDate;


	@Column(name="RECEIVED_QTY")
	private Long receivedQty;

	@Column(name="SUPPLIED_ORDER_QTY")
	private Long supplyOrderQty;
	
	
	@Column(name="SUPPLIED_QTY")
	private Long suppliedQty;
	
	@Column(name="SHORT")
	private Long shortQty;
	
	@Column(name="OVER")
	private Long overQty;
	
	
	private String remark;

	@Column(name="SGST_RATE")
	private BigDecimal sgstRate;

	@Column(name="TAXABLE_VALUE")
	private BigDecimal taxableValue;

	@Column(name="UNIT_RATE")
	private BigDecimal unitRate;
	
	@Column(name="TOTAL_AMOUNT")
	private BigDecimal totalAmount;
	
	@Column(name="TOTAL_VALUE")
	private BigDecimal totalValue;
	
	@Column(name="CGST_RATE_PERCENTAGE")
	private BigDecimal cgstPerct;
	
	@Column(name="SGST_RATE_PERCENTAGE")
	private BigDecimal sgstPerct;
	
	@Column(name="CHEMICAL_COMPOSITION")
	private String chemicalComposittion;
	
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MANUFACTURER_ID")
	private MasManufacturer masManufacturer;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BRAND_ID")
	private MasStoreBrand masStoreBrand;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GRN_MASTER_ID")
	private StoreGrnM storeGrnM;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PO_DETAIL_ID")
	private StorePoT storePoT;
	
	@Column(name = "store_do_dt_id")
	private Long storeDoDtId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="store_do_dt_id",nullable=false,insertable=false,updatable=false)
	private StoreDoInternalIndentT storeDoInternalIndentT;
	
	@Column(name = "vendor_name_id")
	private Long vendorNameId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vendor_name_id",nullable=false,insertable=false,updatable=false)
	private MasStoreSupplier masStoreSupplier;
	
	public StoreGrnT() {
	}

	public long getGrnTransId() {
		return this.grnTransId;
	}

	public void setGrnTransId(long grnTransId) {
		this.grnTransId = grnTransId;
	}

	public BigDecimal getAcceptedQty() {
		return this.acceptedQty;
	}

	public void setAcceptedQty(BigDecimal acceptedQty) {
		this.acceptedQty = acceptedQty;
	}

	public BigDecimal getAmountAfterTax() {
		return this.amountAfterTax;
	}

	public void setAmountAfterTax(BigDecimal amountAfterTax) {
		this.amountAfterTax = amountAfterTax;
	}


	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public BigDecimal getCgstRate() {
		return this.cgstRate;
	}

	public void setCgstRate(BigDecimal cgstRate) {
		this.cgstRate = cgstRate;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public Date getManufacturerDate() {
		return this.manufacturerDate;
	}

	public void setManufacturerDate(Date manufacturerDate) {
		this.manufacturerDate = manufacturerDate;
	}

	

	public StorePoT getStorePoT() {
		return storePoT;
	}

	public void setStorePoT(StorePoT storePoT) {
		this.storePoT = storePoT;
	}


	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getSgstRate() {
		return this.sgstRate;
	}

	public void setSgstRate(BigDecimal sgstRate) {
		this.sgstRate = sgstRate;
	}

	public BigDecimal getTaxableValue() {
		return this.taxableValue;
	}

	public void setTaxableValue(BigDecimal taxableValue) {
		this.taxableValue = taxableValue;
	}

	public BigDecimal getUnitRate() {
		return this.unitRate;
	}

	public void setUnitRate(BigDecimal unitRate) {
		this.unitRate = unitRate;
	}

	public MasManufacturer getMasManufacturer() {
		return this.masManufacturer;
	}

	public void setMasManufacturer(MasManufacturer masManufacturer) {
		this.masManufacturer = masManufacturer;
	}

	public MasStoreBrand getMasStoreBrand() {
		return this.masStoreBrand;
	}

	public void setMasStoreBrand(MasStoreBrand masStoreBrand) {
		this.masStoreBrand = masStoreBrand;
	}

	public StoreGrnM getStoreGrnM() {
		return this.storeGrnM;
	}

	public void setStoreGrnM(StoreGrnM storeGrnM) {
		this.storeGrnM = storeGrnM;
	}

	public Long getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(Long receivedQty) {
		this.receivedQty = receivedQty;
	}

	public Long getSupplyOrderQty() {
		return supplyOrderQty;
	}

	public void setSupplyOrderQty(Long supplyOrderQty) {
		this.supplyOrderQty = supplyOrderQty;
	}

	public Long getSuppliedQty() {
		return suppliedQty;
	}

	public void setSuppliedQty(Long suppliedQty) {
		this.suppliedQty = suppliedQty;
	}

	public Long getShortQty() {
		return shortQty;
	}

	public void setShortQty(Long shortQty) {
		this.shortQty = shortQty;
	}

	public Long getOverQty() {
		return overQty;
	}

	public void setOverQty(Long overQty) {
		this.overQty = overQty;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public BigDecimal getCgstPerct() {
		return cgstPerct;
	}

	public void setCgstPerct(BigDecimal cgstPerct) {
		this.cgstPerct = cgstPerct;
	}

	public BigDecimal getSgstPerct() {
		return sgstPerct;
	}

	public void setSgstPerct(BigDecimal sgstPerct) {
		this.sgstPerct = sgstPerct;
	}

	public String getChemicalComposittion() {
		return chemicalComposittion;
	}

	public void setChemicalComposittion(String chemicalComposittion) {
		this.chemicalComposittion = chemicalComposittion;
	}

	public Long getStoreDoDtId() {
		return storeDoDtId;
	}

	public void setStoreDoDtId(Long storeDoDtId) {
		this.storeDoDtId = storeDoDtId;
	}

	public StoreDoInternalIndentT getStoreDoInternalIndentT() {
		return storeDoInternalIndentT;
	}

	public void setStoreDoInternalIndentT(StoreDoInternalIndentT storeDoInternalIndentT) {
		this.storeDoInternalIndentT = storeDoInternalIndentT;
	}

	public Long getVendorNameId() {
		return vendorNameId;
	}

	public void setVendorNameId(Long vendorNameId) {
		this.vendorNameId = vendorNameId;
	}

	public MasStoreSupplier getMasStoreSupplier() {
		return masStoreSupplier;
	}

	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}
	
}