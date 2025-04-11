package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the STORE_ITEM_BATCH_STOCK database table.
 * 
 */
@Entity
@Table(name="STORE_ITEM_BATCH_STOCK")
@NamedQuery(name="StoreItemBatchStock.findAll", query="SELECT s FROM StoreItemBatchStock s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_ITEM_BATCH_STOCK_STOCKID_GENERATOR", sequenceName="STORE_ITEM_BATCH_STOCK_SEQ", allocationSize=1)
public class StoreItemBatchStock implements Serializable {

	private static final long serialVersionUID = -6190226000400405091L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_ITEM_BATCH_STOCK_STOCKID_GENERATOR")
	@Column(name="STOCK_ID")
	private long stockId;

	/*
	 * @Column(name="ADJUST_QTY") private BigDecimal adjustQty;
	 */

	@Column(name="STOCK_SURPLUS")
	private BigDecimal stockSurplus;
	
	@Column(name="STOCK_DEFICIENT")
	private BigDecimal stockDeficient;
	
	
	
	@Column(name="BATCH_NO")
	private String batchNo;

	@Column(name="CLOSING_STOCK")
	private Long closingStock;

	@Column(name="COST_PRICE")
	private BigDecimal costPrice;

	@Column(name="DEFECT_QTY")
	private Long defectQty;

	@Column(name="EQUIPMENT_DETAIL_STATUS")
	private String equipmentDetailStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRY_DATE")
	private Date expiryDate;

	@Column(name="ISSUE_QTY")
	private Long issueQty;

	@Column(name="ISSUE_RETURN")
	private Long issueReturn;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Temporal(TemporalType.DATE)
	@Column(name="MANUFACTURE_DATE")
	private Date manufactureDate;

	private BigDecimal mrp;

	@Column(name="OPENING_BALANCE_AMOUNT")
	private BigDecimal openingBalanceAmount;

	@Temporal(TemporalType.DATE)
	@Column(name="OPENING_BALANCE_DATE")
	private Date openingBalanceDate;

	@Column(name="OPENING_BALANCE_QTY")
	private Long openingBalanceQty;

	@Column(name="RECEIVED_QTY")
	private Long receivedQty;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MANUFACTURER_ID")
	private MasManufacturer masManufacturer;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BRAND_ID")
	private MasStoreBrand masStoreBrand;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	@Column(name = "district_id")
	private Long districtId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="supplier_id",nullable=false,insertable=false,updatable=false)
	private MasStoreSupplier masStoreSupplier;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="supplier_type_id",nullable=false,insertable=false,updatable=false)
	private MasStoreSupplierType masStoreSupplierType;
	
	@Column(name = "supplier_id")
	private Long supplierId;
	
	@Column(name = "supplier_type_id")
	private Long supplierTypeId;

	public StoreItemBatchStock() {
	}

	public long getStockId() {
		return this.stockId;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	
	public String getEquipmentDetailStatus() {
		return this.equipmentDetailStatus;
	}

	public void setEquipmentDetailStatus(String equipmentDetailStatus) {
		this.equipmentDetailStatus = equipmentDetailStatus;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	
	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getManufactureDate() {
		return this.manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	
	public Date getOpeningBalanceDate() {
		return this.openingBalanceDate;
	}

	public void setOpeningBalanceDate(Date openingBalanceDate) {
		this.openingBalanceDate = openingBalanceDate;
	}
	
	/*
	 * public BigDecimal getAdjustQty() { return adjustQty; }
	 * 
	 * public void setAdjustQty(BigDecimal adjustQty) { this.adjustQty = adjustQty;
	 * }
	 */
	
	
	

	public Long getClosingStock() {
		return closingStock;
	}

	public BigDecimal getStockSurplus() {
		return stockSurplus;
	}

	public BigDecimal getStockDeficient() {
		return stockDeficient;
	}

	public void setStockSurplus(BigDecimal stockSurplus) {
		this.stockSurplus = stockSurplus;
	}

	public void setStockDeficient(BigDecimal stockDeficient) {
		this.stockDeficient = stockDeficient;
	}

	public void setClosingStock(Long closingStock) {
		this.closingStock = closingStock;
	}

	

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public Long getDefectQty() {
		return defectQty;
	}

	public void setDefectQty(Long defectQty) {
		this.defectQty = defectQty;
	}

	public Long getIssueQty() {
		return issueQty;
	}

	public void setIssueQty(Long issueQty) {
		this.issueQty = issueQty;
	}

	public Long getIssueReturn() {
		return issueReturn;
	}

	public void setIssueReturn(Long issueReturn) {
		this.issueReturn = issueReturn;
	}

	

	public BigDecimal getMrp() {
		return mrp;
	}

	public void setMrp(BigDecimal mrp) {
		this.mrp = mrp;
	}

	

	public BigDecimal getOpeningBalanceAmount() {
		return openingBalanceAmount;
	}

	public void setOpeningBalanceAmount(BigDecimal openingBalanceAmount) {
		this.openingBalanceAmount = openingBalanceAmount;
	}

	public Long getOpeningBalanceQty() {
		return openingBalanceQty;
	}

	public void setOpeningBalanceQty(Long openingBalanceQty) {
		this.openingBalanceQty = openingBalanceQty;
	}

	public Long getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(Long receivedQty) {
		this.receivedQty = receivedQty;
	}

	public MasDepartment getMasDepartment() {
		return this.masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
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

	public MasStoreItem getMasStoreItem() {
		return this.masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MasMMU getMasMMU() {
		return masMMU;
	}

	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public MasStoreSupplier getMasStoreSupplier() {
		return masStoreSupplier;
	}

	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}

	public MasStoreSupplierType getMasStoreSupplierType() {
		return masStoreSupplierType;
	}

	public void setMasStoreSupplierType(MasStoreSupplierType masStoreSupplierType) {
		this.masStoreSupplierType = masStoreSupplierType;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getSupplierTypeId() {
		return supplierTypeId;
	}

	public void setSupplierTypeId(Long supplierTypeId) {
		this.supplierTypeId = supplierTypeId;
	}
	
}