package com.mmu.services.entity;

import java.io.Serializable;
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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the PATIENT_PRESCRIPTION_DT database table.
 * 
 */
@Entity
@Table(name="PATIENT_PRESCRIPTION_DT")
@NamedQuery(name="PatientPrescriptionDt.findAll", query="SELECT p FROM PatientPrescriptionDt p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_PRESCRIPTION_DT_SEQ", sequenceName="PATIENT_PRESCRIPTION_DT_SEQ", allocationSize=1)
public class PatientPrescriptionDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9013533870782682351L;
		
	@Id	
	@SequenceGenerator(name="PATIENT_PRESCRIPTION_DT_SEQ", sequenceName="PATIENT_PRESCRIPTION_DT_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_PRESCRIPTION_DT_SEQ")
	@Column(name="PRESCRIPTION_DT_ID")
	private Long prescriptionDtId;

	@Column(name="BATCH_EXPIRY_DATE")
	private Date batchExpiryDate;

	private String dosage;

	@Column(name="FREQUENCY_ID")
	private Long frequencyId;


	private String instruction;

	@Column(name="ITEM_ID")
	private Long itemId;

	/*@Column(name="ITEM_STOP_BY")
	private Long itemStopBy;


*/
	@Column(name="DISP_STOCK")
	private Long dispStock;
	
	@Column(name="NO_OF_DAYS")
	private Long noOfDays;

	@Column(name="PRESCRIPTION_HD_ID")
	private Long prescriptionHdId;

	@Column(name="QTY_BALANCE")
	private Long qtyBalance;

	@Column(name="QTY_ISSUED")
	private Long qtyIssued;

	private String route;

	@Column(name="STORE_STOCK")
	private Long storeStock;

	private Long total;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	private String status;
	
	@Column(name="ISSUED_DATE")
	private Timestamp issuedDate;

	//bi-directional many-to-one association to PatientPrescriptionHd
		@ManyToOne
		@JoinColumn(name="PRESCRIPTION_HD_ID",nullable=false,insertable=false,updatable=false)
		private PatientPrescriptionHd patientPrescriptionHd;
		
	@Column(name = "issued_by")
	private Long issuedBy;
		
	public Long getPrescriptionDtId() {
		return prescriptionDtId;
	}

	public void setPrescriptionDtId(Long prescriptionDtId) {
		this.prescriptionDtId = prescriptionDtId;
	}

	public Date getBatchExpiryDate() {
		return batchExpiryDate;
	}

	public void setBatchExpiryDate(Date batchExpiryDate) {
		this.batchExpiryDate = batchExpiryDate;
	}

	
	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public Long getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	
	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

/*	public Long getItemStopBy() {
		return itemStopBy;
	}

	public void setItemStopBy(Long itemStopBy) {
		this.itemStopBy = itemStopBy;
	}*/

	

	public Long getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Long noOfDays) {
		this.noOfDays = noOfDays;
	}

	
	public Long getPrescriptionHdId() {
		return prescriptionHdId;
	}

	public void setPrescriptionHdId(Long prescriptionHdId) {
		this.prescriptionHdId = prescriptionHdId;
	}

	public Long getQtyBalance() {
		return qtyBalance;
	}

	public void setQtyBalance(Long qtyBalance) {
		this.qtyBalance = qtyBalance;
	}

	public Long getQtyIssued() {
		return qtyIssued;
	}

	public void setQtyIssued(Long qtyIssued) {
		this.qtyIssued = qtyIssued;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Long getStoreStock() {
		return storeStock;
	}

	public void setStoreStock(Long storeStock) {
		this.storeStock = storeStock;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	
	/*@Column(name="ITEM_STOP_DATE")
	private Date itemStopDate;*/
	/*@Column(name="ITEM_STOP_STATUS")
	private Long itemStopStatus;*/

	/*public Date getItemStopDate() {
		return itemStopDate;
	}

	public void setItemStopDate(Date itemStopDate) {
		this.itemStopDate = itemStopDate;
	}

	public Long getItemStopStatus() {
		return itemStopStatus;
	}

	public void setItemStopStatus(Long itemStopStatus) {
		this.itemStopStatus = itemStopStatus;
	}*/
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID",nullable=false,insertable=false,updatable=false)
	private MasStoreItem masStoreItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FREQUENCY_ID",nullable=false,insertable=false,updatable=false)
	private MasFrequency masFrequency;

	public PatientPrescriptionHd getPatientPrescriptionHd() {
		return patientPrescriptionHd;
	}

	public void setPatientPrescriptionHd(PatientPrescriptionHd patientPrescriptionHd) {
		this.patientPrescriptionHd = patientPrescriptionHd;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public MasFrequency getMasFrequency() {
		return masFrequency;
	}

	public void setMasFrequency(MasFrequency masFrequency) {
		this.masFrequency = masFrequency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Timestamp issuedDate) {
		this.issuedDate = issuedDate;
	}	
	
	@Column(name="ITEM_STOP_BY")
	private Long itemStopBy;
	
	@Column(name="ITEM_STOP_DATE")
	private Date itemStopDate;
	
	@Column(name="ITEM_STOP_STATUS")
	private Long itemStopStatus;

	public Long getItemStopBy() {
		return itemStopBy;
	}

	public void setItemStopBy(Long itemStopBy) {
		this.itemStopBy = itemStopBy;
	}

	public Date getItemStopDate() {
		return itemStopDate;
	}

	public void setItemStopDate(Date itemStopDate) {
		this.itemStopDate = itemStopDate;
	}

	public Long getItemStopStatus() {
		return itemStopStatus;
	}

	public void setItemStopStatus(Long itemStopStatus) {
		this.itemStopStatus = itemStopStatus;
	}

	public Long getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(Long issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Long getDispStock() {
		return dispStock;
	}

	public void setDispStock(Long dispStock) {
		this.dispStock = dispStock;
	}
	
	@Column(name="rec_item_id")
	private Long recItemId;
	
	@Column(name="action_flag")
	private String actionFlag;
	
	@Column(name="remarks")
	private String remarks;

	public Long getRecItemId() {
		return recItemId;
	}

	public void setRecItemId(Long recItemId) {
		this.recItemId = recItemId;
	}

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	

	
	
	
}