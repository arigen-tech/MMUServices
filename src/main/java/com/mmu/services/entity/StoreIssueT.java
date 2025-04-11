package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the STORE_ISSUE_T database table.
 * 
 */
@Entity
@Table(name="STORE_ISSUE_T")
@NamedQuery(name="StoreIssueT.findAll", query="SELECT s FROM StoreIssueT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_ISSUE_T_SEQ1", sequenceName="STORE_ISSUE_T_SEQ1", allocationSize=1)
public class StoreIssueT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_ISSUE_T_SEQ1")	
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name="ACK_DATE")
	private Date ackDate;

	@Column(name="BATCH_NO")
	private String batchNo;

	@Column(name="BATCH_STOCK_ID")
	private Long batchStockId;

	@Column(name="BRAND_ID")
	private Long brandId;

	@Column(name="COST_PRICE")
	private Long costPrice;

	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRY_DATE")
	private Date expiryDate;

	@Column(name="ITEM_ID")
	private Long itemId;

	@Column(name="ITEM_ISSUED")
	private Long itemIssued;

	@Column(name="QTY_ISSUED")
	private Long qtyIssued;

	@Column(name="QTY_REQUEST")
	private Long qtyRequest;

	@Column(name="RECEIVED_BY")
	private Long receivedBy;

	private String status;

	//bi-directional many-to-one association to StoreIssueM
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ISSUE_M_ID")
	private StoreIssueM storeIssueM;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INDENT_T_ID")
	private StoreInternalIndentT storeInternalIndentT;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRESCRIPTION_DT_ID")
	private PatientPrescriptionDt patientPrescriptionDt;
	
	//bi-directional one-to-one association to StoreIssueM
	@Column(name="ISSUE_DATE")
	private Date issueDate;	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ISSUE_BY")
	private Users users;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ITEM_ID",nullable=false,insertable=false,updatable=false)
	private MasStoreItem masStoreItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BATCH_STOCK_ID",nullable=false,insertable=false,updatable=false)
	private StoreItemBatchStock storeItemBatchStock;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "store_co_t_id",nullable=false,insertable=false,updatable=false)
	private StoreCoInternalIndentT storeCoInternalIndentT;
	
	@Column(name = "store_co_t_id")
	private Long storeCoTId;
	
	@Column(name = "qty_received")
	private Long qtyReceived;
	
	@Column(name="received_date")
	private Date receivedDate;	
	
	public StoreIssueT() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAckDate() {
		return this.ackDate;
	}

	public void setAckDate(Date ackDate) {
		this.ackDate = ackDate;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Long getBatchStockId() {
		return this.batchStockId;
	}

	public void setBatchStockId(Long batchStockId) {
		this.batchStockId = batchStockId;
	}

	public Long getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getCostPrice() {
		return this.costPrice;
	}

	public void setCostPrice(Long costPrice) {
		this.costPrice = costPrice;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getItemIssued() {
		return this.itemIssued;
	}

	public void setItemIssued(Long itemIssued) {
		this.itemIssued = itemIssued;
	}

	public Long getQtyIssued() {
		return this.qtyIssued;
	}

	public void setQtyIssued(Long qtyIssued) {
		this.qtyIssued = qtyIssued;
	}

	public Long getQtyRequest() {
		return this.qtyRequest;
	}

	public void setQtyRequest(Long qtyRequest) {
		this.qtyRequest = qtyRequest;
	}

	public Long getReceivedBy() {
		return this.receivedBy;
	}

	public void setReceivedBy(Long receivedBy) {
		this.receivedBy = receivedBy;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public StoreIssueM getStoreIssueM() {
		return this.storeIssueM;
	}

	public void setStoreIssueM(StoreIssueM storeIssueM) {
		this.storeIssueM = storeIssueM;
	}

	public StoreInternalIndentT getStoreInternalIndentT() {
		return storeInternalIndentT;
	}

	public void setStoreInternalIndentT(StoreInternalIndentT storeInternalIndentT) {
		this.storeInternalIndentT = storeInternalIndentT;
	}

	public PatientPrescriptionDt getPatientPrescriptionDt() {
		return patientPrescriptionDt;
	}

	public void setPatientPrescriptionDt(PatientPrescriptionDt patientPrescriptionDt) {
		this.patientPrescriptionDt = patientPrescriptionDt;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public StoreItemBatchStock getStoreItemBatchStock() {
		return storeItemBatchStock;
	}

	public void setStoreItemBatchStock(StoreItemBatchStock storeItemBatchStock) {
		this.storeItemBatchStock = storeItemBatchStock;
	}

	public StoreCoInternalIndentT getStoreCoInternalIndentT() {
		return storeCoInternalIndentT;
	}

	public void setStoreCoInternalIndentT(StoreCoInternalIndentT storeCoInternalIndentT) {
		this.storeCoInternalIndentT = storeCoInternalIndentT;
	}

	public Long getStoreCoTId() {
		return storeCoTId;
	}

	public void setStoreCoTId(Long storeCoTId) {
		this.storeCoTId = storeCoTId;
	}

	public Long getQtyReceived() {
		return qtyReceived;
	}

	public void setQtyReceived(Long qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
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