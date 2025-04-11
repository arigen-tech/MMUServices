package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the CHILD_VACATINATION_CHART database table.
 * 
 */
@Entity
@Immutable
@Table(name="CHILD_VACATINATION_CHART")
@NamedQuery(name="ChildVacatinationChart.findAll", query="SELECT c FROM ChildVacatinationChart c")
@SequenceGenerator(name="CHILD_VACATINATION_CHART_CHARTID_GENERATOR", sequenceName="CHILD_VACATINATION_CHART_SEQ", allocationSize=1)
public class ChildVacatinationChart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="CHILD_VACATINATION_CHART_CHARTID_GENERATOR")
	
	@Column(name="CHART_ID")
	private Long chartId;

	@Column(name="AGE_TYPE")
	private String ageType;

	@Temporal(TemporalType.DATE)
	@Column(name="DUE_DATE")
	private Date dueDate;

	@Temporal(TemporalType.DATE)
	@Column(name="GIVEN_DATE")
	private Date givenDate;

	@Column(name="GIVEN_FLAG")
	private String givenFlag;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="ORDER_NO")
	private Long orderNo;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="PLACE_OF_VACATINATION")
	private String placeOfVacatination;

	private String remarks;

	@Column(name="VACCINE_TYPE")
	private String vaccineType;

	@Column(name="VISIT_ID")
	private Long visitId;

	public ChildVacatinationChart() {
	}

	public long getChartId() {
		return this.chartId;
	}

	public void setChartId(long chartId) {
		this.chartId = chartId;
	}

	public String getAgeType() {
		return this.ageType;
	}

	public void setAgeType(String ageType) {
		this.ageType = ageType;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getGivenDate() {
		return this.givenDate;
	}

	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}

	public String getGivenFlag() {
		return this.givenFlag;
	}

	public void setGivenFlag(String givenFlag) {
		this.givenFlag = givenFlag;
	}

	public Long getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
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

	public Long getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPlaceOfVacatination() {
		return this.placeOfVacatination;
	}

	public void setPlaceOfVacatination(String placeOfVacatination) {
		this.placeOfVacatination = placeOfVacatination;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getVaccineType() {
		return this.vaccineType;
	}

	public void setVaccineType(String vaccineType) {
		this.vaccineType = vaccineType;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Patient patient;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	

}