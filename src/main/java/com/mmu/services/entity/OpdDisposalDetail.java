package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the OPD_DISPOSAL_DETAILS database table.
 * 
 */
@Entity
@Table(name="OPD_DISPOSAL_DETAILS")
@NamedQuery(name="OpdDisposalDetail.findAll", query="SELECT o FROM OpdDisposalDetail o")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OPD_DISPOSAL_DETAILS_SEQ", sequenceName="OPD_DISPOSAL_DETAILS_SEQ", allocationSize=1)
public class OpdDisposalDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6390192340476737646L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OPD_DISPOSAL_DETAILS_SEQ")
	@Column(name="DISPOSAL_DETAILS_ID")
	private Long disposalDetailsId;

	@Column(name="DISPOSAL_DAYS")
	private Long disposalDays;
	
	@Column(name="PATIENT_ID")
	private Long patientId;
	
	@Column(name="DISPOSAL_ID")
	private Long disposalId;
	
	@Column(name="VISIT_ID")
	private Long visitId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;
	 
	/*//bi-directional many-to-one association to MasDisposal
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DISPOSAL_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasDisposal masDisposal;*/

	//bi-directional many-to-one association to MasHospital
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasHospital masHospital;

 
	//bi-directional many-to-one association to Patient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Patient patient;

	//bi-directional many-to-one association to Visit
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Visit visit;
   
	public OpdDisposalDetail() {
	}

	public long getDisposalDetailsId() {
		return this.disposalDetailsId;
	}

	public void setDisposalDetailsId(long disposalDetailsId) {
		this.disposalDetailsId = disposalDetailsId;
	}

	public Long getDisposalDays() {
		return this.disposalDays;
	}

	public void setDisposalDays(Long disposalDays) {
		this.disposalDays = disposalDays;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	/*public MasDisposal getMasDisposal() {
		return this.masDisposal;
	}

	public void setMasDisposal(MasDisposal masDisposal) {
		this.masDisposal = masDisposal;
	}*/

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Visit getVisit() {
		return this.visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
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

	public Long getDisposalId() {
		return disposalId;
	}

	public void setDisposalId(Long disposalId) {
		this.disposalId = disposalId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	
	
	
	
	
	

}