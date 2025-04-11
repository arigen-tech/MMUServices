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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_ADMINISTRATIVE_SEX database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="MAS_ADMINISTRATIVE_SEX")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "MAS_ADMINISTRATIVE_SEX_SEQ", sequenceName = "MAS_ADMINISTRATIVE_SEX_SEQ", allocationSize = 1)

public class MasAdministrativeSex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5936461079386972893L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ADMINISTRATIVE_SEX_ID",updatable = false, nullable = false)
	private Long administrativeSexId;

	@Column(name="ADMINISTRATIVE_SEX_CODE")
	private String administrativeSexCode;

	@Column(name="ADMINISTRATIVE_SEX_NAME")
	private String administrativeSexName;
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;

	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;


	@Column(name="STATUS")
	private String status;
	
	
	@OneToMany(mappedBy="masAdministrativeSex")
	@JsonBackReference
	private List<Patient> patient;

	
	
	@OneToMany(mappedBy="masAdministrativeSex")
	@JsonBackReference
	private List<MasRange> masRanges;

	@OneToMany(mappedBy="masAdministrativeSex")
	@JsonBackReference
	private List<MasIdealWeight> masIdealWeights;
		
	public MasAdministrativeSex() {
	}

	public List<MasRange> getMasRanges() {
		return masRanges;
	}

	public void setMasRanges(List<MasRange> masRanges) {
		this.masRanges = masRanges;
	}

	public Long getAdministrativeSexId() {
		return this.administrativeSexId;
	}

	public void setAdministrativeSexId(Long administrativeSexId) {
		this.administrativeSexId = administrativeSexId;
	}

	public String getAdministrativeSexCode() {
		return this.administrativeSexCode;
	}

	public void setAdministrativeSexCode(String administrativeSexCode) {
		this.administrativeSexCode = administrativeSexCode;
	}

	public String getAdministrativeSexName() {
		return this.administrativeSexName;
	}

	public void setAdministrativeSexName(String administrativeSexName) {
		this.administrativeSexName = administrativeSexName;
	}


	
	public List<Patient> getPatient() {
		return patient;
	}

	public void setPatient(List<Patient> patient) {
		this.patient = patient;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public List<MasIdealWeight> getMasIdealWeights() {
		return masIdealWeights;
	}

	public void setMasIdealWeights(List<MasIdealWeight> masIdealWeights) {
		this.masIdealWeights = masIdealWeights;
	}

	
}