package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_DESIGNATION_MAPPING database table.
 * 
 */
@Entity
@Table(name="MAS_DESIGNATION_MAPPING")
@NamedQuery(name="MasDesignationMapping.findAll", query="SELECT m FROM MasDesignationMapping m")
@SequenceGenerator(name="MAS_DESIGNATION_MAPPING_ID_GENERATOR", sequenceName="MAS_DESIGNATION_MAPPING_SEQ", allocationSize=1)
public class MasDesignationMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DESIGNATION_MAPPING_ID_GENERATOR")
	private Long id;

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="DESIGNATION_ID")
	private String designationId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	@Column(name="UNIT_ID")
	private Long unitId;

	public MasDesignationMapping() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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

/*	public Long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}*/

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getDesignationId() {
		return designationId;
	}

	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}

	 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_ID",nullable=false,insertable=false,updatable=false)
	private MasHospital masHospital;

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	 

	 

	 
	 
	
}