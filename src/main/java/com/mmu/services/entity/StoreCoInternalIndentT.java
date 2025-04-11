package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 * The persistent class for the STORE_INTERNAL_INDENT_T database table.
 * 
 */
@Entity
@Table(name="store_co_internal_indent_t")
//@NamedQuery(name="StoreCoInternalIndentT.findAll", query="SELECT s FROM StoreCoInternalIndentT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="store_co_internal_indent_t_seq", sequenceName="store_co_internal_indent_t_seq", allocationSize=1)
public class StoreCoInternalIndentT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="store_co_internal_indent_t_seq")
	@Column(name = "co_t_id")
	private long id;

	@Column(name="available_stock")
	private Long availableStock;
	
	@Column(name="qty_recived")
	private Long qtyReceived;

	@Column(name="qty_issued")
	private Long qtyIssued;

	@Column(name="qty_request")
	private Long qtyRequest;

	@Column(name="reason_for_demand")
	private String reasonForDemand;

	@Column(name="stores_stock")
	private long storesStock;
	
	@Column(name="disp_stock")
	private long dispStock;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasStoreItem masStoreItem;
	
	@Column(name = "item_id")
	private Long itemId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="co_m_id",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private StoreCoInternalIndentM storeCoInternalIndentM1;
	
	@Column(name = "co_m_id")
	private Long storeCoMId;
	
	@Column(name = "approved_qty")
	private Long approvedQty;
	
	@Column(name = "forwarded_flag") 
	private String forwardedFlag;
	
	@Column(name = "store_do_dt_id")
	private Long storeDoDtId;

	public StoreCoInternalIndentT() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Long availableStock) {
		this.availableStock = availableStock;
	}

	public Long getQtyReceived() {
		return qtyReceived;
	}

	public void setQtyReceived(Long qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public Long getQtyIssued() {
		return qtyIssued;
	}

	public void setQtyIssued(Long qtyIssued) {
		this.qtyIssued = qtyIssued;
	}

	public Long getQtyRequest() {
		return qtyRequest;
	}

	public void setQtyRequest(Long qtyRequest) {
		this.qtyRequest = qtyRequest;
	}

	public String getReasonForDemand() {
		return reasonForDemand;
	}

	public void setReasonForDemand(String reasonForDemand) {
		this.reasonForDemand = reasonForDemand;
	}

	public long getStoresStock() {
		return storesStock;
	}

	public void setStoresStock(long storesStock) {
		this.storesStock = storesStock;
	}

	public long getDispStock() {
		return dispStock;
	}

	public void setDispStock(long dispStock) {
		this.dispStock = dispStock;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public StoreCoInternalIndentM getStoreCoInternalIndentM1() {
		return storeCoInternalIndentM1;
	}

	public void setStoreCoInternalIndentM1(StoreCoInternalIndentM storeCoInternalIndentM1) {
		this.storeCoInternalIndentM1 = storeCoInternalIndentM1;
	}

	public Long getStoreCoMId() {
		return storeCoMId;
	}

	public void setStoreCoMId(Long storeCoMId) {
		this.storeCoMId = storeCoMId;
	}

	public Long getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(Long approvedQty) {
		this.approvedQty = approvedQty;
	}

	public String getForwardedFlag() {
		return forwardedFlag;
	}

	public void setForwardedFlag(String forwardedFlag) {
		this.forwardedFlag = forwardedFlag;
	}

	public Long getStoreDoDtId() {
		return storeDoDtId;
	}

	public void setStoreDoDtId(Long storeDoDtId) {
		this.storeDoDtId = storeDoDtId;
	}
	
}