package com.mmu.services.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 * The persistent class for the STORE_INTERNAL_INDENT_T database table.
 * 
 */
@Entity
@Table(name="store_do_internal_indent_t")
//@NamedQuery(name="StoreCoInternalIndentT.findAll", query="SELECT s FROM StoreCoInternalIndentT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="store_do_internal_indent_t_seq", sequenceName="store_do_internal_indent_t_seq", allocationSize=1)
public class StoreDoInternalIndentT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="store_do_internal_indent_t_seq")
	@Column(name = "do_t_id")
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
	@JoinColumn(name="do_m_id",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private StoreDoInternalIndentM storeDoInternalIndentM1;
	
	@Column(name = "do_m_id")
	private Long storeDoMId;
	
	@Column(name = "approved_qty")
	private Long approvedQty;
	
	@Column(name = "forwarded_flag") 
	private String forwardedFlag;
	
	@Column(name = "po_qty")
	private Long poQty;
	
	@OneToMany(mappedBy="storeDoInternalIndentT")
	private List<StoreGrnT> storeGrnTList;
	
	//receive_status
	
	@Column(name = "receive_Status")
	private String receiveStatus;
	
	@Column(name = "unit_rate")
	private Double unitRate;

	public StoreDoInternalIndentT() {
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

	public StoreDoInternalIndentM getStoreDoInternalIndentM1() {
		return storeDoInternalIndentM1;
	}

	public void setStoreDoInternalIndentM1(StoreDoInternalIndentM storeDoInternalIndentM1) {
		this.storeDoInternalIndentM1 = storeDoInternalIndentM1;
	}

	public Long getStoreDoMId() {
		return storeDoMId;
	}

	public void setStoreDoMId(Long storeDoMId) {
		this.storeDoMId = storeDoMId;
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

	public Long getPoQty() {
		return poQty;
	}

	public void setPoQty(Long poQty) {
		this.poQty = poQty;
	}

	public List<StoreGrnT> getStoreGrnTList() {
		return storeGrnTList;
	}

	public void setStoreGrnTList(List<StoreGrnT> storeGrnTList) {
		this.storeGrnTList = storeGrnTList;
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public Double getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(Double unitRate) {
		this.unitRate = unitRate;
	}
	
}