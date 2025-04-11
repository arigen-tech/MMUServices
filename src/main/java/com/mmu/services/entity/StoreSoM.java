package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the STORE_SO_M database table.
 * 
 */
@Entity
@Table(name="STORE_SO_M")
@NamedQuery(name="StoreSoM.findAll", query="SELECT s FROM StoreSoM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_SO_M_SEQ", sequenceName="STORE_SO_M_SEQ",allocationSize=1)
public class StoreSoM implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1547471153662084541L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_SO_M_SEQ")
	@Column(name="SO_M_ID")
	private long soMId;

	//bi-directional many-to-one association to User
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="APPROVED_BY")
		private Users approvedBy;
	@Column(name="APPROVED_DATE")
	private Timestamp approvedDate;

	@Column(name="BOOKED_BUDGET")
	private String bookedBudget;

	@Column(name="COMM_WITH_SANC")
	private String commWithSanc;

	@Column(name="FILE_NO")
	private String fileNo;

	@Column(name="ISSUED_UNDER")
	private String issuedUnder;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Temporal(TemporalType.DATE)
	@Column(name="ORDER_DATE")
	private Date orderDate;

	@Column(name="ORDER_PURPOSE")
	private String orderPurpose;

	@Column(name="ORDER_VALUE")
	private String orderValue;
	
	@Column(name="REMARKS")
	private String remarks;

	@Column(name="LP_TYPE_FLAG")
	private String lpTypeFlag;

	
	@Column(name="RIDC_ID")
	private Long ridcId;
	
	public String getBookedBudget() {
		return bookedBudget;
	}

	public void setBookedBudget(String bookedBudget) {
		this.bookedBudget = bookedBudget;
	}

	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}

	@Column(name="PAYING_AGENCY")
	private String payingAgency;

	@Column(name="QUATATION_NO")
	private String quatationNo;

	@Column(name="QUATATION_OF_ITEM")
	private String quatationOfItem;

	@Column(name="REF_OF_GOVT_AUTHORITY")
	private String refOfGovtAuthority;

	@Column(name="RFP_NO")
	private String rfpNo;

	@Column(name="SANC_OVERRULING")
	private String sancOverruling;
	
	@Column(name="STATUS")
	private String status;

	@Column(name="SANCTION_NO")
	private String sanctionNo;

	@Column(name="TOTAL_AMT")
	private BigDecimal totalAmt;

	@Column(name="TOTAL_AMT_IN_WORDS")
	private String totalAmtInWords;

	@Column(name="UO_NO")
	private String uoNo;

	@Column(name="VIDE_NOTE_NUMBER")
	private String videNoteNumber;

	//bi-directional many-to-one association to StorePoM
	@OneToMany(mappedBy="storeSoM")
	private Set<StorePoM> storePoMs;

	//bi-directional many-to-one association to MasDepartment
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to MasStoreSupplier
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_ID")
	private MasStoreSupplier masStoreSupplier;

	//bi-directional many-to-one association to StoreQuotationM
	/*
	 * @ManyToOne(fetch=FetchType.LAZY)
	 * 
	 * @JoinColumn(name="QUOTATION_M_ID") private StoreQuotationM storeQuotationM;
	 */
	
	@OneToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "QUOTATION_M_ID",nullable=false,insertable=true,updatable=false)
	private StoreQuotationM storeQuotationM;
	

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;
	
	//bi-directional many-to-one association to User
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="CREATED_BY")
		private Users createdBy;
		
		@OneToMany(mappedBy="storeSoM")
		@JsonBackReference
		private List<StoreSoT> storeSoTList; 
		

	public List<StoreSoT> getStoreSoTList() {
			return storeSoTList;
		}

		public void setStoreSoTList(List<StoreSoT> storeSoTList) {
			this.storeSoTList = storeSoTList;
		}

	public Users getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Users createdBy) {
			this.createdBy = createdBy;
		}

	public StoreSoM() {
	}

	public long getSoMId() {
		return this.soMId;
	}

	public void setSoMId(long soMId) {
		this.soMId = soMId;
	}

	

	public Users getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Timestamp getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Timestamp approvedDate) {
		this.approvedDate = approvedDate;
	}

	

	public String getCommWithSanc() {
		return this.commWithSanc;
	}

	public void setCommWithSanc(String commWithSanc) {
		this.commWithSanc = commWithSanc;
	}

	public String getFileNo() {
		return this.fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getIssuedUnder() {
		return this.issuedUnder;
	}

	public void setIssuedUnder(String issuedUnder) {
		this.issuedUnder = issuedUnder;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderPurpose() {
		return this.orderPurpose;
	}

	public void setOrderPurpose(String orderPurpose) {
		this.orderPurpose = orderPurpose;
	}

	
	public String getOrderValue() {
		return orderValue;
	}

	public String getPayingAgency() {
		return this.payingAgency;
	}

	public void setPayingAgency(String payingAgency) {
		this.payingAgency = payingAgency;
	}

	public String getQuatationNo() {
		return this.quatationNo;
	}

	public void setQuatationNo(String quatationNo) {
		this.quatationNo = quatationNo;
	}

	public String getQuatationOfItem() {
		return this.quatationOfItem;
	}

	public void setQuatationOfItem(String quatationOfItem) {
		this.quatationOfItem = quatationOfItem;
	}

	public String getRefOfGovtAuthority() {
		return this.refOfGovtAuthority;
	}

	public void setRefOfGovtAuthority(String refOfGovtAuthority) {
		this.refOfGovtAuthority = refOfGovtAuthority;
	}

	public String getRfpNo() {
		return this.rfpNo;
	}

	public void setRfpNo(String rfpNo) {
		this.rfpNo = rfpNo;
	}

	public String getSancOverruling() {
		return this.sancOverruling;
	}

	public void setSancOverruling(String sancOverruling) {
		this.sancOverruling = sancOverruling;
	}

	public String getSanctionNo() {
		return this.sanctionNo;
	}

	public void setSanctionNo(String sanctionNo) {
		this.sanctionNo = sanctionNo;
	}

	public BigDecimal getTotalAmt() {
		return this.totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getTotalAmtInWords() {
		return this.totalAmtInWords;
	}

	public void setTotalAmtInWords(String totalAmtInWords) {
		this.totalAmtInWords = totalAmtInWords;
	}

	public String getUoNo() {
		return this.uoNo;
	}

	public void setUoNo(String uoNo) {
		this.uoNo = uoNo;
	}

	public String getVideNoteNumber() {
		return this.videNoteNumber;
	}

	public void setVideNoteNumber(String videNoteNumber) {
		this.videNoteNumber = videNoteNumber;
	}

	public Set<StorePoM> getStorePoMs() {
		return this.storePoMs;
	}

	public void setStorePoMs(Set<StorePoM> storePoMs) {
		this.storePoMs = storePoMs;
	}

	public StorePoM addStorePoM(StorePoM storePoM) {
		getStorePoMs().add(storePoM);
		storePoM.setStoreSoM(this);

		return storePoM;
	}

	public StorePoM removeStorePoM(StorePoM storePoM) {
		getStorePoMs().remove(storePoM);
		storePoM.setStoreSoM(null);

		return storePoM;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasDepartment getMasDepartment() {
		return this.masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
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

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getRidcId() {
		return ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

	public String getLpTypeFlag() {
		return lpTypeFlag;
	}

	public void setLpTypeFlag(String lpTypeFlag) {
		this.lpTypeFlag = lpTypeFlag;
	}

	
	

	
}