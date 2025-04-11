package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the STORE_QUOTATION_M database table.
 * 
 */
@Entity
@Table(name="STORE_QUOTATION_M")
@NamedQuery(name="StoreQuotationM.findAll", query="SELECT s FROM StoreQuotationM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_QUOTATION_M_SEQ", sequenceName="STORE_QUOTATION_M_SEQ",allocationSize=1)
public class StoreQuotationM implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1488260586036979927L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_QUOTATION_M_SEQ")
	@Column(name="QUOTATION_M_ID")
	private long quotationMId;

	@Column(name="APPROVED_DATE")
	private Timestamp approvedDate;

	

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	
	@Column(name="REMARKS")
	private String remarks;

	@Column(name="QUOTATION_DATE")
	private Timestamp quotationDate;

	@Column(name="QUOTATION_NO")
	private String quotationNo;

	private String status;
	
	
	@Column(name="L1_STATUS")
	private String l1Status;

	

	@Column(name="TOTAL_COST")
	private BigDecimal totalCost;
	
	@ManyToOne(fetch=FetchType.LAZY)
	 @JsonBackReference
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;
	
	@ManyToOne(fetch=FetchType.LAZY)
	  @JsonBackReference
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;
	
	@Column(name="RIDC_ID")
	private Long ridcId;

	
	@Column(name="LP_TYPE_FLAG")
	private String lpTypeFlag;
	
	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	//bi-directional many-to-one association to MasStoreSupplier
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_ID")
	private MasStoreSupplier masStoreSupplier;

	//bi-directional many-to-one association to StoreBudgetaryM
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BUDGETARY_M_ID")
	private StoreBudgetaryM storeBudgetaryM;
	
	@OneToOne(fetch=FetchType.LAZY, mappedBy = "storeQuotationM") 
	private StoreSoM storeSoM;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CREATED_BY")
	private Users createdBy;

	public StoreSoM getStoreSoM() {
		return storeSoM;
	}

	public void setStoreSoM(StoreSoM storeSoM) {
		this.storeSoM = storeSoM;
	}

	public Users getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Users createdBy) {
		this.createdBy = createdBy;
	}

	public String getL1Status() {
		return l1Status;
	}

	public void setL1Status(String l1Status) {
		this.l1Status = l1Status;
	}
	public Users getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPROVED_BY")
	private Users approvedBy;

	//bi-directional many-to-one association to StoreQuotationT
	@OneToMany(mappedBy="storeQuotationM")
	@JsonBackReference
	private List<StoreQuotationT> storeQuotationTs;

	public StoreQuotationM() {
	}

	public long getQuotationMId() {
		return this.quotationMId;
	}

	public void setQuotationMId(long quotationMId) {
		this.quotationMId = quotationMId;
	}

	public Timestamp getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Timestamp approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	

	public Timestamp getQuotationDate() {
		return this.quotationDate;
	}

	public void setQuotationDate(Timestamp quotationDate) {
		this.quotationDate = quotationDate;
	}

	public String getQuotationNo() {
		return this.quotationNo;
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public MasStoreSupplier getMasStoreSupplier() {
		return this.masStoreSupplier;
	}

	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}

	public StoreBudgetaryM getStoreBudgetaryM() {
		return this.storeBudgetaryM;
	}

	public void setStoreBudgetaryM(StoreBudgetaryM storeBudgetaryM) {
		this.storeBudgetaryM = storeBudgetaryM;
	}

	

	public List<StoreQuotationT> getStoreQuotationTs() {
		return this.storeQuotationTs;
	}

	public void setStoreQuotationTs(List<StoreQuotationT> storeQuotationTs) {
		this.storeQuotationTs = storeQuotationTs;
	}

	public StoreQuotationT addStoreQuotationT(StoreQuotationT storeQuotationT) {
		getStoreQuotationTs().add(storeQuotationT);
		storeQuotationT.setStoreQuotationM(this);

		return storeQuotationT;
	}

	public StoreQuotationT removeStoreQuotationT(StoreQuotationT storeQuotationT) {
		getStoreQuotationTs().remove(storeQuotationT);
		storeQuotationT.setStoreQuotationM(null);

		return storeQuotationT;
	}

	public Long getRidcId() {
		return ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLpTypeFlag() {
		return lpTypeFlag;
	}

	public void setLpTypeFlag(String lpTypeFlag) {
		this.lpTypeFlag = lpTypeFlag;
	}
	

}