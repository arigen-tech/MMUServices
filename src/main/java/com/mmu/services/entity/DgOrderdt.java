package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mmu.services.entity.DiverOrderDet;


/**
 * The persistent class for the DG_ORDERDT database table.
 * 
 */
@Entity
@Table(name="DG_ORDER_DT")
@NamedQuery(name="DgOrderdt.findAll", query="SELECT d FROM DgOrderdt d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="DG_ORDERDT_SEQ", sequenceName="DG_ORDERDT_SEQ", allocationSize=1)
public class DgOrderdt implements Serializable {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 6182435377667966821L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_ORDERDT_SEQ")
	@Column(name="ORDERDT_ID")
	private Long orderdtId;

	@Column(name="INVESTIGATION_ID")
	private Long investigationId;

	//bi-directional many-to-one association to DgSampleCollectionHd
			@ManyToOne(fetch=FetchType.LAZY)
			@JoinColumn(name="INVESTIGATION_ID", nullable=false,insertable=false,updatable=false)
			private DgMasInvestigation dgMasInvestigations;
			
	@Column(name="LAB_MARK")
	private String labMark;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="ORDER_STATUS")
	private String orderStatus;

	@Column(name="ORDERHD_ID")
	private Long orderhdId;
	
	@Column(name="ORDER_DATE")
	private Date orderDate;

	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "ORDERHD_ID",nullable=false,insertable=false,updatable=false)
	private DgOrderhd dgOrderHd;
	
	public Long getOrderdtId() {
		return orderdtId;
	}

	public void setOrderdtId(Long orderdtId) {
		this.orderdtId = orderdtId;
	}

	public Long getInvestigationId() {
		return investigationId;
	}

	public void setInvestigationId(Long investigationId) {
		this.investigationId = investigationId;
	}

	public String getLabMark() {
		return labMark;
	}

	public void setLabMark(String labMark) {
		this.labMark = labMark;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderhdId() {
		return orderhdId;
	}

	public void setOrderhdId(Long orderhdId) {
		this.orderhdId = orderhdId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	 
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDERDT_ID", referencedColumnName = "ORDERDT_ID", insertable = false, updatable = false)
	private List<DgMasInvestigation> dgMasInvestigation;

	public DgOrderhd getDgOrderHd() {
		return dgOrderHd;
	}

	public void setDgOrderHd(DgOrderhd dgOrderHd) {
		this.dgOrderHd = dgOrderHd;
	}

	public DgMasInvestigation getDgMasInvestigations() {
		return dgMasInvestigations;
	}

	public void setDgMasInvestigations(DgMasInvestigation dgMasInvestigations) {
		this.dgMasInvestigations = dgMasInvestigations;
	}
	/*@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "ORDERDT_ID",referencedColumnName = "ORDERDT_ID",nullable=false,insertable=false,updatable=false)
	private DgResultEntryDt dgResultEntryDt;*/
	@Column(name="INVESTIGATION_REMARKS")
	private String investigationRemarks;

	public String getInvestigationRemarks() {
		return investigationRemarks;
	}

	public void setInvestigationRemarks(String investigationRemarks) {
		this.investigationRemarks = investigationRemarks;
	}
	/*
	 * @Column(name="resultOffLineNumber") private String resultOffLineNumber;
	 * 
	 * @Temporal(TemporalType.DATE)
	 * 
	 * @Column(name="resultOffLineDate") private Date resultOffLineDate;
	 */

	/*
	 * public String getResultOffLineNumber() { return resultOffLineNumber; }
	 * 
	 * public void setResultOffLineNumber(String resultOffLineNumber) {
	 * this.resultOffLineNumber = resultOffLineNumber; }
	 * 
	 * public Date getResultOffLineDate() { return resultOffLineDate; }
	 * 
	 * public void setResultOffLineDate(Date resultOffLineDate) {
	 * this.resultOffLineDate = resultOffLineDate; }
	 */
	
	@Column(name="rec_investigation_id")
	private Long recInvestigationId;
	
	@Column(name="action_flag")
	private String actionFlag;
	
	@Column(name="remarks")
	private String remarks;

	public Long getRecInvestigationId() {
		return recInvestigationId;
	}

	public void setRecInvestigationId(Long recInvestigationId) {
		this.recInvestigationId = recInvestigationId;
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