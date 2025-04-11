package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQuery(name="DgFixedValue.findAll", query="SELECT f FROM DgFixedValue f")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "DG_FIXED_VALUE")
@SequenceGenerator(name="DG_FIXED_VALUE_GENERATOR", sequenceName="DG_FIXED_VALUE_SEQ", allocationSize=1)
public class DgFixedValue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3925739749430714971L;
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_FIXED_VALUE_GENERATOR")
	@Column(name="FIXED_ID")
	private Long fixedId;
	
	@Column(name="FIXED_VALUE")
	private String fixedValue;
	
	@Column(name="SUB_INVESTIGATION_ID")
	private Long subInvestigationId;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="SUB_INVESTIGATION_ID", nullable=false,insertable=false,updatable=false)
	private DgSubMasInvestigation dgSubMasInvestigation;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	public Long getFixedId() {
		return fixedId;
	}

	public void setFixedId(Long fixedId) {
		this.fixedId = fixedId;
	}

	public String getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
	}

	public Long getSubInvestigationId() {
		return subInvestigationId;
	}

	public void setSubInvestigationId(Long subInvestigationId) {
		this.subInvestigationId = subInvestigationId;
	}

	
	public DgSubMasInvestigation getDgSubMasInvestigation() {
		return dgSubMasInvestigation;
	}

	public void setDgSubMasInvestigation(DgSubMasInvestigation dgSubMasInvestigation) {
		this.dgSubMasInvestigation = dgSubMasInvestigation;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	
	
}
