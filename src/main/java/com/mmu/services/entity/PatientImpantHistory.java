package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the PATIENT_IMPANT_HISTORY database table.
 * 
 */
@Entity
@Table(name="PATIENT_IMPANT_HISTORY")
@NamedQuery(name="PatientImpantHistory.findAll", query="SELECT p FROM PatientImpantHistory p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PATIENT_IMPANT_HISTORY_SEQ", sequenceName="PATIENT_IMPANT_HISTORY_SEQ",allocationSize=1)
public class PatientImpantHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PATIENT_IMPANT_HISTORY_SEQ")
	@Column(name="PATIENT_IMPANT_ID")
	private Long patientImpantId;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_IMPANT")
	private Date dateOfImpant;

	@Column(name="DEVICE_ID")
	private String deviceId;

	@Column(name="DEVICE_NAME")
	private String deviceName;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="OPD_PATIENT_ID")
	private Long opdPatientId;

	@Column(name="PATIENT_ID")
	private Long patientId;

	private String remarks;

	public PatientImpantHistory() {
	}

	public long getPatientImpantId() {
		return this.patientImpantId;
	}

	public void setPatientImpantId(long patientImpantId) {
		this.patientImpantId = patientImpantId;
	}

	public Date getDateOfImpant() {
		return this.dateOfImpant;
	}

	public void setDateOfImpant(Date dateOfImpant) {
		this.dateOfImpant = dateOfImpant;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Long getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getOpdPatientId() {
		return this.opdPatientId;
	}

	public void setOpdPatientId(Long opdPatientId) {
		this.opdPatientId = opdPatientId;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Column(name="VISIT_ID")
	private Long visitId;

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
	
	

}