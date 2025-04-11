package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;


/**
 * The persistent class for the STORE_SO_T database table.
 * 
 */
@Entity
@Table(name="STORE_SO_T")
@NamedQuery(name="StoreSoT.findAll", query="SELECT s FROM StoreSoT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_SO_T_SEQ", sequenceName="STORE_SO_T_SEQ",allocationSize=1)
public class StoreSoT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_SO_T_SEQ")
	@Column(name="SO_T_ID")
	private long soTId;

	@Column(name="ITEM_QTY")
	private BigDecimal itemQty;

	@Column(name="ITEM_VALUE")
	private BigDecimal itemValue;

	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SO_M_ID")
	private StoreSoM storeSoM;
	

	@Column(name="UNIT_RATE")
	private BigDecimal unitRate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_ID")
	private MasStoreUnit masStoreUnit;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="QUOTATION_T_ID")
	private StoreQuotationT storeQuotationT;
	
	
	
	public StoreSoT() {
	}

	public long getSoTId() {
		return this.soTId;
	}

	public void setSoTId(long soTId) {
		this.soTId = soTId;
	}

	public BigDecimal getItemQty() {
		return this.itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getItemValue() {
		return this.itemValue;
	}

	public void setItemValue(BigDecimal itemValue) {
		this.itemValue = itemValue;
	}

	

	public StoreSoM getStoreSoM() {
		return storeSoM;
	}

	public void setStoreSoM(StoreSoM storeSoM) {
		this.storeSoM = storeSoM;
	}

	public BigDecimal getUnitRate() {
		return this.unitRate;
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

	public MasStoreUnit getMasStoreUnit() {
		return this.masStoreUnit;
	}

	public void setMasStoreUnit(MasStoreUnit masStoreUnit) {
		this.masStoreUnit = masStoreUnit;
	}

	public StoreQuotationT getStoreQuotationT() {
		return storeQuotationT;
	}

	public void setStoreQuotationT(StoreQuotationT storeQuotationT) {
		this.storeQuotationT = storeQuotationT;
	}

	
	
}