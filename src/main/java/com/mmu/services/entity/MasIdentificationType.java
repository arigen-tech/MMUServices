package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the MAS_IDENTIFICATION_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_IDENTIFICATION_TYPE")
@NamedQuery(name="MasIdentificationType.findAll", query="SELECT m FROM MasIdentificationType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="IDENTIFICATION_TYPE_ID", sequenceName="MAS_IDENTIFICATION_TYPE_SEQ", allocationSize=1)
public class MasIdentificationType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8413526026911055932L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="IDENTIFICATION_TYPE_ID")
	@Column(name="IDENTIFICATION_TYPE_ID")
	private long identificationTypeId;

	@Column(name="IDENTIFICATION_CODE")
	private String identificationCode;

	@Column(name="IDENTIFICATION_NAME")
	private String identificationName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	private String status;
	
	@Column(name="map_id")
	private Long mappedId;

	//bi-directional many-to-one association to Patient
	@OneToMany(mappedBy="masIdentificationType")
	@JsonBackReference
	private List<Patient> patients;

	public MasIdentificationType() {
	}

	public long getIdentificationTypeId() {
		return this.identificationTypeId;
	}

	public void setIdentificationTypeId(long identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationCode() {
		return this.identificationCode;
	}

	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}

	public String getIdentificationName() {
		return this.identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Patient> getPatients() {
		return this.patients;
	}

	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}

	public Patient addPatient(Patient patient) {
		getPatients().add(patient);
		patient.setMasIdentificationType(this);

		return patient;
	}

	public Patient removePatient(Patient patient) {
		getPatients().remove(patient);
		patient.setMasIdentificationType(null);

		return patient;
	}

	public Long getMappedId() {
		return mappedId;
	}

	public void setMappedId(Long mappedId) {
		this.mappedId = mappedId;
	}

}