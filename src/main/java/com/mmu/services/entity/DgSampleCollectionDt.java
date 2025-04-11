package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the DG_SAMPLE_COLLECTION_DT database table.
 * 
 */
@Entity
@Table(name="DG_SAMPLE_COLLECTION_DT")
@NamedQuery(name="DgSampleCollectionDt.findAll", query="SELECT d FROM DgSampleCollectionDt d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="DG_SAMPLE_COLLECTION_DT_SEQ", sequenceName="DG_SAMPLE_COLLECTION_DT_SEQ", allocationSize=1)
public class DgSampleCollectionDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_SAMPLE_COLLECTION_DT_SEQ")
	@Column(name="SAMPLE_COLLECTION_DT_ID")
	private Long sampleCollectionDtId;

	@Column(name="COLLECTED")
	private String collected;

	@Column(name="COLLECTED_BY")
	private Long collectedBy;

	@Column(name="DIAG_NO")
	private String diagNo;

	@Column(name="EMPANELLED_STATUS")
	private String empanelledStatus;

	@Column(name="INVESTIGATION_ID")
	private Long investigationId;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="INVESTIGATION_ID", nullable=false, insertable=false, updatable=false)
	private DgMasInvestigation dgMasInvestigation;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MAINCHARGE")
	private Long maincharge;

	@Column(name="ORDER_STATUS")
	private String orderStatus;

	@Column(name="ORDERDT_ID")
	private Long orderdtId;

	@Column(name="QUANTITY")
	private String quantity;

	@Column(name="REASON")
	private String reason;

	@Column(name="REJECTED")
	private String rejected;

	@Temporal(TemporalType.DATE)
	@Column(name="SAMPLE_COLL_DATETIME")
	private Date sampleCollDatetime;

	@Column(name="SAMPLE_ID")
	private Long sampleId;

	@Column(name="SUBCHARGE")
	private Long subcharge;

	@Column(name="VALIDATED")
	private String validated;

	@Column(name="SAMPLE_COLLECTION_HD_ID")
	private Long sampleCollectionHdId;
	
	@Column(name="ADDITIONAL_REMARKS")
	private String additionalRemarks;
	
	
	
	public String getAdditionalRemarks() {
		return additionalRemarks;
	}

	public void setAdditionalRemarks(String additionalRemarks) {
		this.additionalRemarks = additionalRemarks;
	}

	//bi-directional many-to-one association to DgSampleCollectionHd
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="SAMPLE_COLLECTION_HD_ID", nullable=false,insertable=false,updatable=false)
	private DgSampleCollectionHd dgSampleCollectionHd;

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="SUBCHARGE", nullable=false, insertable=false, updatable=false)
	private MasSubChargecode masSubChargecode;
		
		@ManyToOne(cascade = CascadeType.ALL)
		@JoinColumn(name="ORDERDT_ID", nullable=false,insertable=false,updatable=false)
		private DgOrderdt dgOrderdt;
		
		@ManyToOne(cascade = CascadeType.ALL)
		@JoinColumn(name="SAMPLE_ID", nullable=false,insertable=false,updatable=false)
		private MasSample masSample;
		
		
	public MasSample getMasSample() {
			return masSample;
		}

		public void setMasSample(MasSample masSample) {
			this.masSample = masSample;
		}

	public DgOrderdt getDgOrderdt() {
			return dgOrderdt;
		}

		public void setDgOrderdt(DgOrderdt dgOrderdt) {
			this.dgOrderdt = dgOrderdt;
		}

	public DgSampleCollectionDt() {
	}

	public Long getSampleCollectionDtId() {
		return sampleCollectionDtId;
	}

	public String getCollected() {
		return this.collected;
	}

	public void setCollected(String collected) {
		this.collected = collected;
	}

	public Long getCollectedBy() {
		return this.collectedBy;
	}

	public void setCollectedBy(Long collectedBy) {
		this.collectedBy = collectedBy;
	}

	public String getDiagNo() {
		return this.diagNo;
	}

	public void setDiagNo(String diagNo) {
		this.diagNo = diagNo;
	}

	public String getEmpanelledStatus() {
		return this.empanelledStatus;
	}

	public void setEmpanelledStatus(String empanelledStatus) {
		this.empanelledStatus = empanelledStatus;
	}

	public Long getInvestigationId() {
		return this.investigationId;
	}

	public void setInvestigationId(Long investigationId) {
		this.investigationId = investigationId;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getMaincharge() {
		return this.maincharge;
	}

	public void setMaincharge(Long maincharge) {
		this.maincharge = maincharge;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderdtId() {
		return this.orderdtId;
	}

	public void setOrderdtId(Long orderdtId) {
		this.orderdtId = orderdtId;
	}

	public String getQuantity() {
		return this.quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRejected() {
		return this.rejected;
	}

	public void setRejected(String rejected) {
		this.rejected = rejected;
	}

	public Date getSampleCollDatetime() {
		return this.sampleCollDatetime;
	}

	public void setSampleCollDatetime(Date sampleCollDatetime) {
		this.sampleCollDatetime = sampleCollDatetime;
	}

	public Long getSampleId() {
		return this.sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	public Long getSubcharge() {
		return this.subcharge;
	}

	public void setSubcharge(Long subcharge) {
		this.subcharge = subcharge;
	}

	public String getValidated() {
		return this.validated;
	}

	public void setValidated(String validated) {
		this.validated = validated;
	}

	public DgSampleCollectionHd getDgSampleCollectionHd() {
		return this.dgSampleCollectionHd;
	}

	public void setDgSampleCollectionHd(DgSampleCollectionHd dgSampleCollectionHd) {
		this.dgSampleCollectionHd = dgSampleCollectionHd;
	}

	public Long getSampleCollectionHdId() {
		return sampleCollectionHdId;
	}

	public void setSampleCollectionHdId(Long sampleCollectionHdId) {
		this.sampleCollectionHdId = sampleCollectionHdId;
	}

	public void setSampleCollectionDtId(Long sampleCollectionDtId) {
		this.sampleCollectionDtId = sampleCollectionDtId;
	}

	public MasSubChargecode getMasSubChargecode() {
		return masSubChargecode;
	}

	public void setMasSubChargecode(MasSubChargecode masSubChargecode) {
		this.masSubChargecode = masSubChargecode;
	}

	
	
	public DgMasInvestigation getDgMasInvestigation() {
		return dgMasInvestigation;
	}

	public void setDgMasInvestigation(DgMasInvestigation dgMasInvestigation) {
		this.dgMasInvestigation = dgMasInvestigation;
	}

	
}