package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 * The persistent class for the DG_RESULT_ENTRY_DT database table.
 * 
 */
@Entity
@Table(name="DG_RESULT_ENTRY_DT")
@NamedQuery(name="DgResultEntryDt.findAll", query="SELECT d FROM DgResultEntryDt d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="DG_RESULT_ENTRY_DETAIL_SEQ", sequenceName="DG_RESULT_ENTRY_DETAIL_SEQ", allocationSize=1)
public class DgResultEntryDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_RESULT_ENTRY_DETAIL_SEQ")
	@Column(name="RESULT_ENTRY_DT_ID")
	private Long resultEntryDetailId;

	@Column(name="CHARGE_CODE_ID")
	private Long chargeCodeId;

	/*
	 * @Column(name="FILM_SIZE") private String filmSize;
	 */
	/*
	 * @Column(name="FILM_USED") private Long filmUsed;
	 */
	
	 @Column(name="FIXED_ID") 
	 private Long fixedId;
	 
	 
	 @Temporal(TemporalType.DATE)
		@Column(name="LAST_CHG_DATE")
		private Date lastChgDate; 
	/*
	 * @Column(name="HL7_FLAG") private String hl7Flag;
	 */
	@Column(name="INVESTIGATION_ID")
	private Long investigationId;

	/*
	 * @Column(name="NORMAL_ID") private Long normalId;
	 */

	@Column(name="NPRMAL_ID")
	private Long nprmalId;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="NPRMAL_ID", insertable=false, updatable=false)
	private DgNormalValue dgNormalValue;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="FIXED_ID", insertable=false, updatable=false)
	private DgFixedValue dgFixedValue;

	@Column(name="REMARKS")
	private String remarks;

	//@Lob
	@Column(name="RESULT")
	private String result;

	@Column(name="RESULT_DETAIL_STATUS")
	private String resultDetailStatus;

	@Column(name="RESULT_ENTRY_HD_ID")
	private Long resultEntryId;

	
	  @Column(name="RESULT_TYPE") 
	  private String resultType;
	
	  @Column(name="SAMPLE_COLLECTION_DT_ID") 
	  private Long sampleCollectionDetailsId;
	  
	  @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	  @JoinColumn(name="SAMPLE_COLLECTION_DT_ID", nullable=false, insertable=false, updatable=false)
	  private DgSampleCollectionDt dgSampleCollectionDt;
	  
	  @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	  @JoinColumn(name="INVESTIGATION_ID", nullable=false, insertable=false, updatable=false)
	  private DgMasInvestigation dgMasInvestigation;
	  
	  @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	  @JoinColumn(name="SUB_INVESTIGATION_ID", nullable=false, insertable=false, updatable=false)
	  private DgSubMasInvestigation dgSubMasInvestigation;
	 
	@Column(name="SAMPLE_ID")
	private Long sampleId;

	@Column(name="SUB_INVESTIGATION_ID")
	private Long subInvestigationId;

	/*
	 * @Column(name="TEMPLATE_ID") private Long templateId;
	 */

	@Column(name="UOM_ID")
	private Long uomId;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="UOM_ID", nullable=false,insertable=false,updatable=false)
	private MasUOM masUOM;

	@Column(name="VALIDATED")
	private String validated;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "RESULT_ENTRY_HD_ID",nullable=false,insertable=false,updatable=false)
	private DgResultEntryHd dgResultEntryHd; 
	
	@Column(name="RIDC_ID")
	private Long ridcId;
	
	@Column(name="LAST_UPDATED_BY")
	private Long lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate; 
	
	@Column(name="range_status")
	private String rangeStatus;
	
	public DgResultEntryDt() {
	}

	public Long getChargeCodeId() {
		return this.chargeCodeId;
	}

	public void setChargeCodeId(Long chargeCodeId) {
		this.chargeCodeId = chargeCodeId;
	}

	  public Long getFixedId() { 
		  return this.fixedId; 
		  }
	  
	  public void setFixedId(Long fixedId) {
		  this.fixedId = fixedId; 
		  }

	public Long getInvestigationId() {
		return this.investigationId;
	}

	public void setInvestigationId(Long investigationId) {
		this.investigationId = investigationId;
	}
	
	public Long getNprmalId() {
		return this.nprmalId;
	}

	public void setNprmalId(Long nprmalId) {
		this.nprmalId = nprmalId;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultDetailStatus() {
		return this.resultDetailStatus;
	}

	public void setResultDetailStatus(String resultDetailStatus) {
		this.resultDetailStatus = resultDetailStatus;
	}

	public Long getResultEntryId() {
		return this.resultEntryId;
	}

	public void setResultEntryId(Long resultEntryId) {
		this.resultEntryId = resultEntryId;
	}
	
	public Long getSampleId() {
		return this.sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	public Long getSubInvestigationId() {
		return this.subInvestigationId;
	}

	public void setSubInvestigationId(Long subInvestigationId) {
		this.subInvestigationId = subInvestigationId;
	}

	public Long getUomId() {
		return this.uomId;
	}

	public void setUomId(Long uomId) {
		this.uomId = uomId;
	}

	public String getValidated() {
		return this.validated;
	}

	public void setValidated(String validated) {
		this.validated = validated;
	}

	public Long getResultEntryDetailId() {
		return resultEntryDetailId;
	}

	public void setResultEntryDetailId(Long resultEntryDetailId) {
		this.resultEntryDetailId = resultEntryDetailId;
	}
	@Column(name="ORDERDT_ID")
	private Long orderDtId;

	public Long getOrderDtId() {
		return orderDtId;
	}

	public void setOrderDtId(Long orderDtId) {
		this.orderDtId = orderDtId;
	}

	public Long getSampleCollectionDetailsId() {
		return sampleCollectionDetailsId;
	}

	public void setSampleCollectionDetailsId(Long sampleCollectionDetailsId) {
		this.sampleCollectionDetailsId = sampleCollectionDetailsId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public DgResultEntryHd getDgResultEntryHd() {
		return dgResultEntryHd;
	}

	public void setDgResultEntryHd(DgResultEntryHd dgResultEntryHd) {
		this.dgResultEntryHd = dgResultEntryHd;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}	
	
	public DgSampleCollectionDt getDgSampleCollectionDt() {
		return dgSampleCollectionDt;
	}

	public void setDgSampleCollectionDt(DgSampleCollectionDt dgSampleCollectionDt) {
		this.dgSampleCollectionDt = dgSampleCollectionDt;
	}

	public DgMasInvestigation getDgMasInvestigation() {
		return dgMasInvestigation;
	}

	public void setDgMasInvestigation(DgMasInvestigation dgMasInvestigation) {
		this.dgMasInvestigation = dgMasInvestigation;
	}

	public DgSubMasInvestigation getDgSubMasInvestigation() {
		return dgSubMasInvestigation;
	}

	public void setDgSubMasInvestigation(DgSubMasInvestigation dgSubMasInvestigation) {
		this.dgSubMasInvestigation = dgSubMasInvestigation;
	}

	public DgNormalValue getDgNormalValue() {
		return dgNormalValue;
	}

	public void setDgNormalValue(DgNormalValue dgNormalValue) {
		this.dgNormalValue = dgNormalValue;
	}

	public DgFixedValue getDgFixedValue() {
		return dgFixedValue;
	}

	public void setDgFixedValue(DgFixedValue dgFixedValue) {
		this.dgFixedValue = dgFixedValue;
	}
	public Long getRidcId() {
		return ridcId;
	}

	public void setRidcId(Long ridcId) {
		this.ridcId = ridcId;
	}
	@Column(name="RANGE_VALUE")
	private String rangeValue;

	public String getRangeValue() {
		return rangeValue;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	public void setRangeValue(String rangeValue) {
		this.rangeValue = rangeValue;
	}

	public MasUOM getMasUOM() {
		return masUOM;
	}

	public void setMasUOM(MasUOM masUOM) {
		this.masUOM = masUOM;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVESTIGATION_ID", nullable=false,insertable=false,updatable=false)
	private DgMasInvestigation dgMasInvestigations;
	
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "ORDERDT_ID",referencedColumnName = "ORDERDT_ID",nullable=false,insertable=false,updatable=false)
	private DgOrderdt dgOrderdt;

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getRangeStatus() {
		return rangeStatus;
	}

	public void setRangeStatus(String rangeStatus) {
		this.rangeStatus = rangeStatus;
	}	
	
	@Column(name="resultOffLineNumber")
	private String resultOffLineNumber;
	@Temporal(TemporalType.DATE)
	@Column(name="resultOffLineDate")
	private Date resultOffLineDate;

	public String getResultOffLineNumber() {
		return resultOffLineNumber;
	}

	public void setResultOffLineNumber(String resultOffLineNumber) {
		this.resultOffLineNumber = resultOffLineNumber;
	}

	public Date getResultOffLineDate() {
		return resultOffLineDate;
	}

	public void setResultOffLineDate(Date resultOffLineDate) {
		this.resultOffLineDate = resultOffLineDate;
	}
	
	
}