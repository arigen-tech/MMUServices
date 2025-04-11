package com.mmu.services.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_MARITAL_STATUS database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_MARITAL_STATUS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasMaritalStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="MARITAL_STATUS_ID",updatable = false, nullable = false)
	private Long maritalStatusId;


	@Column(name="MARITAL_STATUS_CODE")
	private String maritalStatusCode;

	@Column(name="MARITAL_STATUS_NAME")
	private String maritalStatusName;

	

	@OneToMany(mappedBy="masMaritalStatus")
	@JsonBackReference
	private List<MasEmployee> masEmployees;

	public MasMaritalStatus() {
	}

	public long getMaritalStatusId() {
		return this.maritalStatusId;
	}

	public void setMaritalStatusId(long maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}
	

	public String getMaritalStatusCode() {
		return this.maritalStatusCode;
	}

	public void setMaritalStatusCode(String maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}

	public String getMaritalStatusName() {
		return this.maritalStatusName;
	}

	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}

	

	public List<MasEmployee> getMasEmployees() {
		return this.masEmployees;
	}

	public void setMasEmployees(List<MasEmployee> masEmployees) {
		this.masEmployees = masEmployees;
	}

	

}