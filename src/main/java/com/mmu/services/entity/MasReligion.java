package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_RELIGION database table.
 * 
 */
@Entity
@Table(name="MAS_RELIGION")
@NamedQuery(name="MasReligion.findAll", query="SELECT m FROM MasReligion m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_RELIGION_RELIGIONID_GENERATOR", sequenceName="MAS_RELIGION_SEQ2", allocationSize=1)
public class MasReligion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1069755963689039783L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_RELIGION_RELIGIONID_GENERATOR")
	@Column(name="RELIGION_ID")
	private long religionId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="RELIGION_CODE")
	private String religionCode;

	@Column(name="RELIGION_NAME")
	private String religionName;

	private String status;

	

	@OneToMany(mappedBy="masReligion")
	@JsonBackReference
	private List<Patient> patients;

	public MasReligion() {
	}

	public long getReligionId() {
		return this.religionId;
	}

	public void setReligionId(long religionId) {
		this.religionId = religionId;
	}

	/*
	 * public Long getLastChgBy() { return this.lastChgBy; }
	 * 
	 * public void setLastChgBy(Long lastChgBy) { this.lastChgBy = lastChgBy; }
	 */

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getReligionCode() {
		return this.religionCode;
	}

	public void setReligionCode(String religionCode) {
		this.religionCode = religionCode;
	}

	public String getReligionName() {
		return this.religionName;
	}

	public void setReligionName(String religionName) {
		this.religionName = religionName;
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
		patient.setMasReligion(this);

		return patient;
	}

	public Patient removePatient(Patient patient) {
		getPatients().remove(patient);
		patient.setMasReligion(null);

		return patient;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
}