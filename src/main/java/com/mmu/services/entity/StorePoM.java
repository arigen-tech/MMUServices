package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the STORE_PO_M database table.
 * 
 */
@Entity
@Table(name="STORE_PO_M")
@NamedQuery(name="StorePoM.findAll", query="SELECT s FROM StorePoM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_PO_M_POMID_GENERATOR", sequenceName="STORE_PO_M_SEQ",allocationSize=1)
public class StorePoM implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_PO_M_POMID_GENERATOR")
	@Column(name="PO_M_ID")
	private long poMId;

	@Temporal(TemporalType.DATE)
	@Column(name="APPROVED_DATE")
	private Date approvedDate;

	@Temporal(TemporalType.DATE)
	@Column(name="DELIVERY_DATE")
	private Date deliveryDate;

	@Column(name="DELIVERY_SCHEDULE")
	private String deliverySchedule;

	@Column(name="LP_TYPE_FLAG")
	private String lpTypeFlag;


	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PAY_TERMS")
	private String payTerms;

	@Temporal(TemporalType.DATE)
	@Column(name="PO_DATE")
	private Date poDate;

	@Column(name="PO_NUMBER")
	private String poNumber;

	@Temporal(TemporalType.DATE)
	@Column(name="PROPOSAL_DATE")
	private Date proposalDate;

	@Column(name="RECEIVED_STATUS")
	private String receivedStatus;

	private String remarks;

	@Column(name="RFP_NO")
	private String rfpNo;

	private String status;

	@Column(name="TAX_TERM")
	private String taxTerm;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	
	//bi-directional many-to-one association to MasStoreSupplier
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STOCKIST_ID")
	private MasStoreSupplierType masStoreSupplierType;

	//bi-directional many-to-one association to MasStoreSupplier
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_ID")
	private MasStoreSupplier masStoreSupplier;

	//bi-directional many-to-one association to StoreQuotationM
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="QUOTATION_M_ID")
	private StoreQuotationM storeQuotationM;

	//bi-directional many-to-one association to StoreSoM
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SO_M_ID")
	private StoreSoM storeSoM;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPROVED_BY")
	private Users approvedBy;

	//bi-directional many-to-one association to StorePoT
	@OneToMany(mappedBy="storePoM")
	private Set<StorePoT> storePoTs;

	//bi-directional many-to-one association to MasStoreFinancial
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FINANCIAL_YEAR")
	private MasStoreFinancial masStoreFinancial;

	public StorePoM() {
	}

	public long getPoMId() {
		return this.poMId;
	}

	public void setPoMId(long poMId) {
		this.poMId = poMId;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliverySchedule() {
		return this.deliverySchedule;
	}

	public void setDeliverySchedule(String deliverySchedule) {
		this.deliverySchedule = deliverySchedule;
	}

	


	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getPayTerms() {
		return this.payTerms;
	}

	public void setPayTerms(String payTerms) {
		this.payTerms = payTerms;
	}

	public Date getPoDate() {
		return this.poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public String getPoNumber() {
		return this.poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Date getProposalDate() {
		return this.proposalDate;
	}

	public void setProposalDate(Date proposalDate) {
		this.proposalDate = proposalDate;
	}

	public String getReceivedStatus() {
		return this.receivedStatus;
	}

	public void setReceivedStatus(String receivedStatus) {
		this.receivedStatus = receivedStatus;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRfpNo() {
		return this.rfpNo;
	}

	public void setRfpNo(String rfpNo) {
		this.rfpNo = rfpNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaxTerm() {
		return this.taxTerm;
	}

	public void setTaxTerm(String taxTerm) {
		this.taxTerm = taxTerm;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	

	public MasStoreSupplierType getMasStoreSupplierType() {
		return masStoreSupplierType;
	}

	public void setMasStoreSupplierType(MasStoreSupplierType masStoreSupplierType) {
		this.masStoreSupplierType = masStoreSupplierType;
	}

	public MasStoreSupplier getMasStoreSupplier() {
		return masStoreSupplier;
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

	public StoreSoM getStoreSoM() {
		return this.storeSoM;
	}

	public void setStoreSoM(StoreSoM storeSoM) {
		this.storeSoM = storeSoM;
	}

	

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Users getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Set<StorePoT> getStorePoTs() {
		return this.storePoTs;
	}

	public void setStorePoTs(Set<StorePoT> storePoTs) {
		this.storePoTs = storePoTs;
	}

	public StorePoT addStorePoT(StorePoT storePoT) {
		getStorePoTs().add(storePoT);
		storePoT.setStorePoM(this);

		return storePoT;
	}

	public StorePoT removeStorePoT(StorePoT storePoT) {
		getStorePoTs().remove(storePoT);
		storePoT.setStorePoM(null);

		return storePoT;
	}

	public MasStoreFinancial getMasStoreFinancial() {
		return this.masStoreFinancial;
	}

	public void setMasStoreFinancial(MasStoreFinancial masStoreFinancial) {
		this.masStoreFinancial = masStoreFinancial;
	}

	public String getLpTypeFlag() {
		return lpTypeFlag;
	}

	public void setLpTypeFlag(String lpTypeFlag) {
		this.lpTypeFlag = lpTypeFlag;
	}
	
	

}