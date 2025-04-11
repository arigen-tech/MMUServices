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
 * The persistent class for the MAS_STATE database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="MAS_STATE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasState implements Serializable {
	
	private static final long serialVersionUID = -4594046566142347491L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="STATE_ID")
	private long stateId;


	@Column(name="STATE_CODE")
	private String stateCode;

	@Column(name="STATE_NAME")
	private String stateName;
	
	private String status;

	
	@OneToMany(mappedBy="masState")
	@JsonBackReference
	private List<MasDistrict> masDistrict;

	public MasState() {
	}

	public long getStateId() {
		return this.stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	
	public String getStateCode() {
		return this.stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<MasDistrict> getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(List<MasDistrict> masDistrict) {
		this.masDistrict = masDistrict;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}