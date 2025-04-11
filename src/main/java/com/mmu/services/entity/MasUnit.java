package com.mmu.services.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * The persistent class for the VU_MAS_UNIT database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_UNIT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasUnit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5010107093035370094L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "UNIT_ID",updatable = false, nullable = false)
	private Long unitId;
	
	@Column(name = "UNIT_CODE")
	private String unitCode;
	
	@Column(name = "UNIT_NAME")
	private String unitName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_PARENT_ID")
	private MasUnit masUnitParent;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "STATION_ID")
	private MasStation masStation;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "UNITTYPE_ID")
	@JsonBackReference
	private MasUnitType masUnitType;

	
	

	public MasUnit() {
	}

	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	

	public MasStation getMasStation() {
		return masStation;
	}

	public void setMasStation(MasStation masStation) {
		this.masStation = masStation;
	}

	public MasUnitType getMasUnittype() {
		return this.masUnitType;
	}

	public void setMasUnittype(MasUnitType masUnitType) {
		this.masUnitType = masUnitType;
	}


	

	public MasUnit getMasUnitParent() {
		return masUnitParent;
	}

	public void setMasUnitParent(MasUnit masUnitParent) {
		this.masUnitParent = masUnitParent;
	}

	public MasUnitType getMasUnitType() {
		return masUnitType;
	}

	public void setMasUnitType(MasUnitType masUnitType) {
		this.masUnitType = masUnitType;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	
	

}