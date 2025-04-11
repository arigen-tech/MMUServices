package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 * The persistent class for the STORE_BUDGETARY_M database table.
 * 
 */
@Entity
@Table(name="STORE_BUDGETARY_M")
@NamedQuery(name="StoreBudgetaryM.findAll", query="SELECT s FROM StoreBudgetaryM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_BUDGETARY_M_SEQ", sequenceName="STORE_BUDGETARY_M_SEQ",allocationSize=1)
public class StoreBudgetaryM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -81141547352681553L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_BUDGETARY_M_SEQ")
	@Column(name="BUDGETARY_M_ID")
	private long budgetaryMId;

	@Column(name="APPROX_COST")
	private BigDecimal approxCost;

	@Column(name="FINAL_APPROVED_DATE")
	private Timestamp finalApprovedDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	 @JsonBackReference
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;
	
	
	@Column(name="QUOTATION_REMARK")
	private String quotationRemark;

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

	public Timestamp getFinalApprovedDate() {
		return finalApprovedDate;
	}

	public void setFinalApprovedDate(Timestamp finalApprovedDate) {
		this.finalApprovedDate = finalApprovedDate;
	}

	public String getFinalRemark() {
		return finalRemark;
	}

	public void setFinalRemark(String finalRemark) {
		this.finalRemark = finalRemark;
	}

	@Column(name="FINAL_REMARK")
	private String finalRemark;

	@Column(name="MO_APPROVED_DATE")
	private Timestamp moApprovedDate;

	/*
	 * @Column(name="MO_REMARK") private String moRemark;
	 */

	@Column(name="REQ_DATE")
	private Timestamp reqDate;

	@Column(name="REQ_NO")
	private String reqNo;

	private String status;

	@Column(name="LP_TYPE_FLAG")
	private String lpTypeFlag;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LP_TYPE_ID")
	private MasStoreLpType masStoreLpType;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MO_APPROVED_BY")
	private Users moApprovedBy;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REQ_BY")
	private Users reqBY;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FINAL_APPROVED_BY")
	private Users finalApprovedBy;

	@OneToMany(mappedBy="storeBudgetaryM")
	@JsonBackReference
	private List<StoreBudgetaryT> storeBudgetaryTs;

	
	//bi-directional many-to-one association to StoreQuotationM
		@OneToMany(mappedBy="storeBudgetaryM")
		@JsonBackReference
		private List<StoreQuotationM> storeQuotationMs;
		
	public StoreBudgetaryM() {
	}

	public long getBudgetaryMId() {
		return this.budgetaryMId;
	}

	public void setBudgetaryMId(long budgetaryMId) {
		this.budgetaryMId = budgetaryMId;
	}

	public Timestamp getMoApprovedDate() {
		return this.moApprovedDate;
	}

	public void setMoApprovedDate(Timestamp moApprovedDate) {
		this.moApprovedDate = moApprovedDate;
	}

	/*
	 * public String getMoRemark() { return this.moRemark; }
	 * 
	 * public void setMoRemark(String moRemark) { this.moRemark = moRemark; }
	 */

	public Timestamp getReqDate() {
		return this.reqDate;
	}

	public void setReqDate(Timestamp reqDate) {
		this.reqDate = reqDate;
	}

	public String getReqNo() {
		return this.reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasStoreLpType getMasStoreLpType() {
		return this.masStoreLpType;
	}

	public void setMasStoreLpType(MasStoreLpType masStoreLpType) {
		this.masStoreLpType = masStoreLpType;
	}

	

	

	public BigDecimal getApproxCost() {
		return approxCost;
	}

	public void setApproxCost(BigDecimal approxCost) {
		this.approxCost = approxCost;
	}

	public Users getMoApprovedBy() {
		return moApprovedBy;
	}

	public void setMoApprovedBy(Users moApprovedBy) {
		this.moApprovedBy = moApprovedBy;
	}

	public Users getReqBY() {
		return reqBY;
	}

	public void setReqBY(Users reqBY) {
		this.reqBY = reqBY;
	}

	public Users getFinalApprovedBy() {
		return finalApprovedBy;
	}

	public void setFinalApprovedBy(Users finalApprovedBy) {
		this.finalApprovedBy = finalApprovedBy;
	}

	public List<StoreBudgetaryT> getStoreBudgetaryTs() {
		return this.storeBudgetaryTs;
	}

	public void setStoreBudgetaryTs(List<StoreBudgetaryT> storeBudgetaryTs) {
		this.storeBudgetaryTs = storeBudgetaryTs;
	}

	public StoreBudgetaryT addStoreBudgetaryT(StoreBudgetaryT storeBudgetaryT) {
		getStoreBudgetaryTs().add(storeBudgetaryT);
		storeBudgetaryT.setStoreBudgetaryM(this);

		return storeBudgetaryT;
	}

	public List<StoreQuotationM> getStoreQuotationMs() {
		return storeQuotationMs;
	}

	public void setStoreQuotationMs(List<StoreQuotationM> storeQuotationMs) {
		this.storeQuotationMs = storeQuotationMs;
	}

	public StoreBudgetaryT removeStoreBudgetaryT(StoreBudgetaryT storeBudgetaryT) {
		getStoreBudgetaryTs().remove(storeBudgetaryT);
		storeBudgetaryT.setStoreBudgetaryM(null);

		return storeBudgetaryT;
	}

	public String getQuotationRemark() {
		return quotationRemark;
	}

	public void setQuotationRemark(String quotationRemark) {
		this.quotationRemark = quotationRemark;
	}

	public String getLpTypeFlag() {
		return lpTypeFlag;
	}

	public void setLpTypeFlag(String lpTypeFlag) {
		this.lpTypeFlag = lpTypeFlag;
	}

	
}