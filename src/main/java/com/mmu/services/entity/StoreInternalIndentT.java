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
@Table(name="STORE_INTERNAL_INDENT_T")
@NamedQuery(name="StoreInternalIndentT.findAll", query="SELECT s FROM StoreInternalIndentT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_INTERNAL_INDENT_T_SEQ", sequenceName="STORE_INTERNAL_INDENT_T_SEQ", allocationSize=1)
public class StoreInternalIndentT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_INTERNAL_INDENT_T_SEQ")
	private long id;

	@Column(name="AVAILABLE_STOCK")
	private Long availableStock;

	
	@Column(name="QTY_RECIVED")
	private Long qtyReceived;

	@Column(name="QTY_ISSUED")
	private Long qtyIssued;

	@Column(name="QTY_REQUEST")
	private Long qtyRequest;

	@Column(name="REASON_FOR_DEMAND")
	private String reasonForDemand;


	@Column(name="STORES_STOCK")
	private long storesStock;
	
	
	@Column(name="DISP_STOCK")
	private long dispStock;



	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	@JsonBackReference
	private MasStoreItem masStoreItem;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INTERNAL_ID")
	@JsonBackReference
	private StoreInternalIndentM storeInternalIndentM1;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	@JsonBackReference
	private MasDepartment department1;
			
	@Column(name = "forwarded_flag")
	private String forwardedFlag;  
	
	@Column(name = "approved_qty")
	private Long approvedQty;
	
	@Column(name = "store_co_dt_id")
	private Long storeCoDtId;
	
	@OneToMany(mappedBy="storeInternalIndentT")
	private List<StoreIssueT> storeIssueT;

	public StoreInternalIndentT() {
	}

	public long getId() {
		return this.id;
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

	public void setQtyIssued(Long qtyIssued) {
		this.qtyIssued = qtyIssued;
	}

	public void setQtyRequest(Long qtyRequest) {
		this.qtyRequest = qtyRequest;
	}

	public MasDepartment getDepartment1() {
		return department1;
	}

	public void setDepartment1(MasDepartment department1) {
		this.department1 = department1;
	}

	
	public Long getQtyReceived() {
		return qtyReceived;
	}

	public void setQtyReceived(long qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public Long getQtyIssued() {
		return this.qtyIssued;
	}

	public void setQtyIssued(long qtyIssued) {
		this.qtyIssued = qtyIssued;
	}

	public long getQtyRequest() {
		return this.qtyRequest;
	}

	public void setQtyRequest(long qtyRequest) {
		this.qtyRequest = qtyRequest;
	}

	public String getReasonForDemand() {
		return this.reasonForDemand;
	}

	public void setReasonForDemand(String reasonForDemand) {
		this.reasonForDemand = reasonForDemand;
	}

	
	public long getStoresStock() {
		return this.storesStock;
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

	

	public StoreInternalIndentM getStoreInternalIndentM1() {
		return this.storeInternalIndentM1;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public void setStoreInternalIndentM1(StoreInternalIndentM storeInternalIndentM1) {
		this.storeInternalIndentM1 = storeInternalIndentM1;
	}

	public String getForwardedFlag() {
		return forwardedFlag;
	}

	public void setForwardedFlag(String forwardedFlag) {
		this.forwardedFlag = forwardedFlag;
	}

	public void setQtyReceived(Long qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public Long getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(Long approvedQty) {
		this.approvedQty = approvedQty;
	}

	public Long getStoreCoDtId() {
		return storeCoDtId;
	}

	public void setStoreCoDtId(Long storeCoDtId) {
		this.storeCoDtId = storeCoDtId;
	}

	public List<StoreIssueT> getStoreIssueT() {
		return storeIssueT;
	}

	public void setStoreIssueT(List<StoreIssueT> storeIssueT) {
		this.storeIssueT = storeIssueT;
	}

	@Column(name="TOTAL_COST")
	private Double totalCost;

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	
	
	
}