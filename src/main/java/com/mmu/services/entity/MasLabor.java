package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the mas_labor database table.
 * 
 */
@Entity
@Table(name="mas_labor")
@NamedQuery(name="MasLabor.findAll", query="SELECT m FROM MasLabor m")
@SequenceGenerator(name="MAS_LABOR_LABORID_GENERATOR", sequenceName="mas_labor_seq",allocationSize = 1)
public class MasLabor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_LABOR_LABORID_GENERATOR")
	@Column(name="labor_id", unique=true, nullable=false)
	private Long laborId;

	@Column(name="labor_code", length=7)
	private String laborCode;

	@Column(name="labor_name", length=30)
	private String laborName;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(length=1)
	private String status;

	//bi-directional many-to-one association to Patient
	@OneToMany(mappedBy="masLabor")
	private List<Patient> patients;

	public MasLabor() {
	}

	public Long getLaborId() {
		return this.laborId;
	}

	public void setLaborId(Long laborId) {
		this.laborId = laborId;
	}

	public String getLaborCode() {
		return this.laborCode;
	}

	public void setLaborCode(String laborCode) {
		this.laborCode = laborCode;
	}

	public String getLaborName() {
		return this.laborName;
	}

	public void setLaborName(String laborName) {
		this.laborName = laborName;
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
		patient.setMasLabor(this);

		return patient;
	}

	public Patient removePatient(Patient patient) {
		getPatients().remove(patient);
		patient.setMasLabor(null);

		return patient;
	}

}