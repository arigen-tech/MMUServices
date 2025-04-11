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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the TEMP_TABLE_FOR_BACK_LP database table.
 * 
 */
@Entity
@Table(name="TEMP_TABLE_FOR_BACK_LP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="TEMP_TABLE_FOR_BACK_LP_GENERATOR", sequenceName="TEMP_TABLE_FOR_BACK_LP_SEQ",allocationSize=1)
public class TempDirectReceivingForBackLp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8952859624972135389L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="TEMP_TABLE_FOR_BACK_LP_GENERATOR")
	@Column(name="TEMP_ID")
	private long tempId;

	@Column(name="RECEIVING_DATE")
	private Timestamp receivingDate;

	@Column(name="BATCH_NO")
	private String batchNo;

	@Temporal(TemporalType.DATE)
	@Column(name="DOM")
	private Date dom;

	@Temporal(TemporalType.DATE)
	@Column(name="DOE")
	private Date doe;
	
	@Column(name="RECEIVED_QTY")
	private long receivedQty;

	@Column(name="AMOUNT")
	private BigDecimal amount;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RECEIVED_BY")
	private Users receivedBy;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_ID")
	private MasStoreSupplier masStoreSupplier;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;
	
	@Column(name="BUDGETARY_STATUS")
	private String budgetaryStatus;
	
	@Column(name="QUOTATION_STATUS")
	private String quotationStatus;
	
	@Column(name="SANCTION_ORDER_STATUS")
	private String sanctionOrderStatus;
	
	@Column(name="SUPPLY_ORDER_STATUS")
	private String supplyOrderStatus;
	
	@Column(name="GRN_STATUS")
	private String grnStatus;

	@Column(name="STATUS")
	private String status;
	
	public TempDirectReceivingForBackLp() {
	}


	public long getTempId() {
		return tempId;
	}


	public Timestamp getReceivingDate() {
		return receivingDate;
	}


	public String getBatchNo() {
		return batchNo;
	}


	public Date getDom() {
		return dom;
	}


	public Date getDoe() {
		return doe;
	}


	public long getReceivedQty() {
		return receivedQty;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}


	public MasHospital getMasHospital() {
		return masHospital;
	}


	public MasDepartment getMasDepartment() {
		return masDepartment;
	}


	public Users getReceivedBy() {
		return receivedBy;
	}


	public MasStoreSupplier getMasStoreSupplier() {
		return masStoreSupplier;
	}


	public Users getLastChgBy() {
		return lastChgBy;
	}


	public String getBudgetaryStatus() {
		return budgetaryStatus;
	}


	public String getQuotationStatus() {
		return quotationStatus;
	}


	public String getSanctionOrderStatus() {
		return sanctionOrderStatus;
	}


	public String getSupplyOrderStatus() {
		return supplyOrderStatus;
	}


	public String getGrnStatus() {
		return grnStatus;
	}


	public void setTempId(long tempId) {
		this.tempId = tempId;
	}


	public void setReceivingDate(Timestamp receivingDate) {
		this.receivingDate = receivingDate;
	}


	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}


	public void setDom(Date dom) {
		this.dom = dom;
	}


	public void setDoe(Date doe) {
		this.doe = doe;
	}


	public void setReceivedQty(long receivedQty) {
		this.receivedQty = receivedQty;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}


	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}


	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}


	public void setReceivedBy(Users receivedBy) {
		this.receivedBy = receivedBy;
	}


	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}


	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}


	public void setBudgetaryStatus(String budgetaryStatus) {
		this.budgetaryStatus = budgetaryStatus;
	}


	public void setQuotationStatus(String quotationStatus) {
		this.quotationStatus = quotationStatus;
	}


	public void setSanctionOrderStatus(String sanctionOrderStatus) {
		this.sanctionOrderStatus = sanctionOrderStatus;
	}


	public void setSupplyOrderStatus(String supplyOrderStatus) {
		this.supplyOrderStatus = supplyOrderStatus;
	}


	public void setGrnStatus(String grnStatus) {
		this.grnStatus = grnStatus;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	

}