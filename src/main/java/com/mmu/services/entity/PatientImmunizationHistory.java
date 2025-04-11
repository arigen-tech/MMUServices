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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the PATIENT_IMMUNIZATION_HISTORY database table.
 * 
 */
@Entity
@Table(name="PATIENT_IMMUNIZATION_HISTORY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@NamedQuery(name="PatientImmunizationHistory.findAll", query="SELECT p FROM PatientImmunizationHistory p")
@SequenceGenerator(name="PATIENT_IMMUNIZATION_HISTORY_IMMUNIZATION_ID_GENERATOR", sequenceName="PATIENT_IMMUNIZATION_HIST_SEQ", allocationSize=1)
public class PatientImmunizationHistory implements Serializable {
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = -1278351966812033806L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_IMMUNIZATION_HISTORY_IMMUNIZATION_ID_GENERATOR")
	@Column(name="IMMUNIZATION_ID")
	private long immunizationId;

	@Temporal(TemporalType.DATE)
	@Column(name="IMMUNIZATION_DATE")
	private Date immunizationDate;

	@Column(name="ITEM_ID")
	private Long itemId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="VISIT_ID")
	private Long visitId;

	public PatientImmunizationHistory() {
	}

	public long getImmunizationId() {
		return this.immunizationId;
	}

	public void setImmunizationId(long immunizationId) {
		this.immunizationId = immunizationId;
	}

	public Date getImmunizationDate() {
		return this.immunizationDate;
	}

	public void setImmunizationDate(Date immunizationDate) {
		this.immunizationDate = immunizationDate;
	}

	 

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasStoreItem masStoreItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Visit visit;

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public MasStoreItem getMasStoreItem() {
		return masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}
	@Column(name="DURATION")
	private String  duration;
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="NEXT_DUE_DATE")
	private Date nextDueDate;
	
	public Date getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(Date nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

	
	
	@Temporal(TemporalType.DATE)
	@Column(name="PRESCRIPTION_DATE")
	private Date prescriptionDate;

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}
	

	
}