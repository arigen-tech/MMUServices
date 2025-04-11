package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the STORE_BALANCE_T database table.
 * 
 */
@Entity
@Table(name="STORE_BALANCE_T")
@NamedQuery(name="StoreBalanceT.findAll", query="SELECT s FROM StoreBalanceT s")
@SequenceGenerator(name="STORE_BALANCE_T_ID_GENERATOR", sequenceName="STORE_BALANCE_T_SEQ", allocationSize=1)
public class StoreBalanceT implements Serializable {

	private static final long serialVersionUID = 4443337112939323135L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_BALANCE_T_ID_GENERATOR")
	private long id;

	@Column(name="BATCH_NO")
	private String batchNo;

	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRY_DATE")
	private Date expiryDate;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name="MANUFACTURE_DATE")
	private Date manufactureDate;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;

	private Long qty;

	@Column(name="TOTAL_AMOUNT")
	private BigDecimal totalAmount;

	@Column(name="UNIT_RATE")
	private BigDecimal unitRate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MANUFACTURER_ID")
	private MasManufacturer masManufacturer;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BRAND_ID")
	private MasStoreBrand masStoreBrand;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STORE_BALANCE_M_ID")
	private StoreBalanceM storeBalanceM;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
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

	public StoreBalanceT() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	

	public Date getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public Long getQty() {
		return this.qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getUnitRate() {
		return unitRate;
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

	
	public StoreBalanceM getStoreBalanceM() {
		return storeBalanceM;
	}

	public void setStoreBalanceM(StoreBalanceM storeBalanceM) {
		this.storeBalanceM = storeBalanceM;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
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