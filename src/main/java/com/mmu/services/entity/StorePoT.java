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
 * The persistent class for the STORE_PO_T database table.
 * 
 */
@Entity
@Table(name="STORE_PO_T")
@NamedQuery(name="StorePoT.findAll", query="SELECT s FROM StorePoT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_PO_T_POTID_GENERATOR", sequenceName="STORE_PO_T_SEQ",allocationSize=1)
public class StorePoT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_PO_T_POTID_GENERATOR")
	@Column(name="PO_T_ID")
	private long poTId;

	@Column(name="ACCEPTED_QTY")
	private Long acceptedQty;

	private BigDecimal amount;

	@Column(name="CHEMICAL_COMPOSITION")
	private String chemicalComposition;


	@Column(name="QTY_ADVISED")
	private Long qtyAdvised;

	@Column(name="QUANTITY_ORDERED")
	private Long quantityOrdered;

	@Column(name="QUANTITY_RECEIVED")
	private Long quantityReceived;

	@Column(name="REJECTED_QTY")
	private Long rejectedQty;

	@Column(name="UNIT_RATE")
	private BigDecimal unitRate;

	@Column(name="RECEIVED_STATUS")
	private String receiveStatus;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PO_M_ID")
	private StorePoM storePoM;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="QUOTATION_T_ID")
	private StoreQuotationT storeQuatationDetails;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SO_T_ID")
	private StoreSoT storeSoT;
	
	

	public StorePoT() {
	}

	public long getPoTId() {
		return this.poTId;
	}

	public void setPoTId(long poTId) {
		this.poTId = poTId;
	}

	
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getChemicalComposition() {
		return this.chemicalComposition;
	}

	public void setChemicalComposition(String chemicalComposition) {
		this.chemicalComposition = chemicalComposition;
	}

	

	public Long getAcceptedQty() {
		return acceptedQty;
	}

	public void setAcceptedQty(Long acceptedQty) {
		this.acceptedQty = acceptedQty;
	}

	public Long getQtyAdvised() {
		return qtyAdvised;
	}

	public void setQtyAdvised(Long qtyAdvised) {
		this.qtyAdvised = qtyAdvised;
	}

	public Long getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Long quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Long getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Long quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public Long getRejectedQty() {
		return rejectedQty;
	}

	public void setRejectedQty(Long rejectedQty) {
		this.rejectedQty = rejectedQty;
	}


	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	
	public StoreQuotationT getStoreQuatationDetails() {
		return storeQuatationDetails;
	}

	public void setStoreQuatationDetails(StoreQuotationT storeQuatationDetails) {
		this.storeQuatationDetails = storeQuatationDetails;
	}

	

	

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public StoreSoT getStoreSoT() {
		return storeSoT;
	}

	public void setStoreSoT(StoreSoT storeSoT) {
		this.storeSoT = storeSoT;
	}

	public BigDecimal getUnitRate() {
		return this.unitRate;
	}

	public void setUnitRate(BigDecimal unitRate) {
		this.unitRate = unitRate;
	}

	public StorePoM getStorePoM() {
		return this.storePoM;
	}

	public void setStorePoM(StorePoM storePoM) {
		this.storePoM = storePoM;
	}

}