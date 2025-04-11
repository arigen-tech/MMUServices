package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the STORE_STOCK_TAKING_T database table.
 * 
 */
@Entity
@Table(name="STORE_STOCK_TAKING_T")
@NamedQuery(name="StoreStockTakingT.findAll", query="SELECT t FROM StoreStockTakingT t")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_STOCK_TAKING_T_ID_GENERATOR", sequenceName="STORE_STOCK_TAKING_T_SEQ",allocationSize=1)
public class StoreStockTakingT implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1651540101069883917L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_STOCK_TAKING_T_ID_GENERATOR")
	@Column(name="TAKING_T_ID")
	private long takingTId;

	@Column(name="BATCH_NO")
	private String batchNo;

	@Column(name="EXPIRY_DATE")
	private Date expiryDate;
	
	@Column(name="COMPUTED_STOCK")
	private BigDecimal computedStock;

	@Column(name="STORE_STOCK_SERVICE")
	private BigDecimal storeStockService;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="STOCK_SURPLUS")
	private BigDecimal stockSurplus;
	
	@Column(name="STOCK_DEFICIENT")
	private BigDecimal stockDeficient;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STOCK_ID")
	private StoreItemBatchStock storeItemBatchStock;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TAKING_M_ID")
	private StoreStockTakingM storeStockTakingM;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;

	public StoreStockTakingT() {
	}

	public long getTakingTId() {
		return takingTId;
	}

	public void setTakingTId(long takingTId) {
		this.takingTId = takingTId;
	}


	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	

	public BigDecimal getComputedStock() {
		return this.computedStock;
	}

	public void setComputedStock(BigDecimal computedStock) {
		this.computedStock = computedStock;
	}

	

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public BigDecimal getStockDeficient() {
		return this.stockDeficient;
	}

	public void setStockDeficient(BigDecimal stockDeficient) {
		this.stockDeficient = stockDeficient;
	}

	public BigDecimal getStockSurplus() {
		return this.stockSurplus;
	}

	public void setStockSurplus(BigDecimal stockSurplus) {
		this.stockSurplus = stockSurplus;
	}

	

	public BigDecimal getStoreStockService() {
		return this.storeStockService;
	}

	public void setStoreStockService(BigDecimal storeStockService) {
		this.storeStockService = storeStockService;
	}

	public StoreItemBatchStock getStoreItemBatchStock() {
		return this.storeItemBatchStock;
	}

	public void setStoreItemBatchStock(StoreItemBatchStock storeItemBatchStock) {
		this.storeItemBatchStock = storeItemBatchStock;
	}

	public StoreStockTakingM getStoreStockTakingM() {
		return storeStockTakingM;
	}

	public void setStoreStockTakingM(StoreStockTakingM storeStockTakingM) {
		this.storeStockTakingM = storeStockTakingM;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	

}