package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="VU_MAS_UNITTYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_UNITTYPE_GENERATOR", sequenceName="MAS_UNITTYPE_SEQ", allocationSize=1)
public class MasUnitType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3022500315206375349L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_UNITTYPE_GENERATOR")
	@Column(name="UNITTYPE_ID")
	private Long unitTypeId;
	
	@Column(name="UNITTYPE_NAME")
	private String unitTypeName;
	
	@Column(name="UNITTYPE_CODE")
	private String unitTypeCode;
	
	/*
	 * @Column(name="LAST_CHG_BY") private Long lastChgBy;
	 * 
	 * @Column(name="LAST_CHG_DATE") private Timestamp lastChgDate;
	 */
	
	@OneToMany(mappedBy="masUnitType")
	@JsonBackReference
	private Set<MasUnit> masUnits;

	public Long getUnitTypeId() {
		return unitTypeId;
	}

	public void setUnitTypeId(Long unitTypeId) {
		this.unitTypeId = unitTypeId;
	}

	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}

	public String getUnitTypeCode() {
		return unitTypeCode;
	}

	public void setUnitTypeCode(String unitTypeCode) {
		this.unitTypeCode = unitTypeCode;
	}

	/*
	 * public Long getLastChgBy() { return lastChgBy; }
	 * 
	 * public void setLastChgBy(Long lastChgBy) { this.lastChgBy = lastChgBy; }
	 * 
	 * public Timestamp getLastChgDate() { return lastChgDate; }
	 * 
	 * public void setLastChgDate(Timestamp lastChgDate) { this.lastChgDate =
	 * lastChgDate; }
	 */

	public Set<MasUnit> getMasUnits() {
		return masUnits;
	}

	public void setMasUnits(Set<MasUnit> masUnits) {
		this.masUnits = masUnits;
	}
	
	
}
