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


/**
 * The persistent class for the DG_ORDERDT database table.
 * 
 */
@Entity
@Table(name="DIVER_ORDER_DET")
@NamedQuery(name="DiverOrderDet.findAll", query="SELECT d FROM DiverOrderDet d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="DIVER_ORDER_DET_SEQ", sequenceName="DIVER_ORDER_DET_SEQ", allocationSize=1)
public class DiverOrderDet implements Serializable {

	 
	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DIVER_ORDER_DET_SEQ")
	@Column(name="ORDER_DET_ID")
	private Long orderDetId;

	@Column(name="INVESTIGATION_ID")
	private Long investigationId;
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	 			
	 

	 
	 
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
	
	@Column(name="DIVER_DONE_ON")
	private Date diverDoneOn;
	
	@Column(name="DIVER_NEXT_DUE_ON")
	private Date diverNextDueOn;
	
	@Column(name="DIVER_LAST_DONE_ON")
	private Date diverLastDoneOn;
	
	@Column(name="DIVER_DURATION")
	private String  diverDuration;
	
	@Column(name="DIVER_ACTION")
	private String  diverAction;

	@Column(name="visit_id")
	private Long visitId;

	@Column(name="patient_id")
	private Long patientId;

	 
	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getDiverAction() {
		return diverAction;
	}

	public void setDiverAction(String diverAction) {
		this.diverAction = diverAction;
	}

	 

	public Long getOrderDetId() {
		return orderDetId;
	}

	public void setOrderDetId(Long orderDetId) {
		this.orderDetId = orderDetId;
	}

	public Long getInvestigationId() {
		return investigationId;
	}

	public void setInvestigationId(Long investigationId) {
		this.investigationId = investigationId;
	}

	public String getDiverDuration() {
		return diverDuration;
	}

	public void setDiverDuration(String diverDuration) {
		this.diverDuration = diverDuration;
	}

	public Date getDiverDoneOn() {
		return diverDoneOn;
	}

	public void setDiverDoneOn(Date diverDoneOn) {
		this.diverDoneOn = diverDoneOn;
	}

	public Date getDiverNextDueOn() {
		return diverNextDueOn;
	}

	public void setDiverNextDueOn(Date diverNextDueOn) {
		this.diverNextDueOn = diverNextDueOn;
	}

	public Date getDiverLastDoneOn() {
		return diverLastDoneOn;
	}

	public void setDiverLastDoneOn(Date diverLastDoneOn) {
		this.diverLastDoneOn = diverLastDoneOn;
	}
	
	
	
}