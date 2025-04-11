package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
 * The persistent class for the STORE_QUOTATION_T database table.
 * 
 */
@Entity
@Table(name="STORE_QUOTATION_T")
@NamedQuery(name="StoreQuotationT.findAll", query="SELECT s FROM StoreQuotationT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_QUOTATION_T_SEQ", sequenceName="STORE_QUOTATION_T_SEQ",allocationSize=1)

public class StoreQuotationT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_QUOTATION_T_SEQ")
	@Column(name="QUOTATION_T_ID")
	private long quotationTId;

	@Column(name="TOTAL_COST")
	private BigDecimal totalCost;

	@Column(name="UNIT_RATE")
	private BigDecimal unitRate;
	
	
	@Column(name="QTY_REQUIRED")
	private Long qtyRequried;

	public Long getQtyRequried() {
		return qtyRequried;
	}

	public void setQtyRequried(Long qtyRequried) {
		this.qtyRequried = qtyRequried;
	}

	//bi-directional many-to-one association to MasStoreItem
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;

	//bi-directional many-to-one association to MasStoreSupplier
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_ID")
	private MasStoreSupplier masStoreSupplier;

	//bi-directional many-to-one association to StoreQuotationM
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="QUOTATION_M_ID")
	private StoreQuotationM storeQuotationM;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TEMP_TABLE_ID")
	private TempDirectReceivingForBackLp tempDirectReceivingForBackLp;
	
	
	public StoreQuotationT() {
	}

	public long getQuotationTId() {
		return this.quotationTId;
	}

	public void setQuotationTId(long quotationTId) {
		this.quotationTId = quotationTId;
	}

	
	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public BigDecimal getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(BigDecimal unitRate) {
		this.unitRate = unitRate;
	}

	public MasStoreItem getMasStoreItem() {
		return this.masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public MasStoreSupplier getMasStoreSupplier() {
		return this.masStoreSupplier;
	}

	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}

	public StoreQuotationM getStoreQuotationM() {
		return this.storeQuotationM;
	}

	public void setStoreQuotationM(StoreQuotationM storeQuotationM) {
		this.storeQuotationM = storeQuotationM;
	}

	public TempDirectReceivingForBackLp getTempDirectReceivingForBackLp() {
		return tempDirectReceivingForBackLp;
	}

	public void setTempDirectReceivingForBackLp(TempDirectReceivingForBackLp tempDirectReceivingForBackLp) {
		this.tempDirectReceivingForBackLp = tempDirectReceivingForBackLp;
	}
	

}