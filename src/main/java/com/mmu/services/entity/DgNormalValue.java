package com.mmu.services.entity;

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
@NamedQuery(name="DgNormalValue.findAll", query="SELECT v FROM DgNormalValue v")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "DG_NORMAL_VALUE")
@SequenceGenerator(name="DG_NORMAL_VALUE_GENERATOR", sequenceName="DG_NORMAL_VALUE_SEQ", allocationSize=1)
public class DgNormalValue {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_NORMAL_VALUE_GENERATOR")
	@Column(name="NORMAL_ID")
	public Long normalId;
	
	@Column(name="SEX")
	public String sex;
	
	@Column(name="FROM_AGE")
	public Long fromAge;
	
	@Column(name="TO_AGE")
	public Long toAge;
	
	@Column(name="MIN_NORMAL_VALUE")
	public String minNormalValue;
	
	@Column(name="MAX_NORMAL_VALUE")
	public String maxNormalValue;
	
	@Column(name="NORMALVALUE")
	public String normalValue;
	
	
	@Column(name="SUB_INVESTIGATION_ID")
	public Long subInvestigationId;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="SUB_INVESTIGATION_ID", nullable=false,insertable=false,updatable=false)
	public DgSubMasInvestigation dgSubMasInvestigation;
	
	@Column(name="LAST_CHG_BY")
	public Long lastChgBy;
	
	@Column(name="LAST_CHG_DATE")
	public Timestamp lastChgDate;
	
	@Column(name="CHARGE_CODE_ID")
	public Long chargeCodeId;

	public Long getNormalId() {
		return normalId;
	}

	public void setNormalId(Long normalId) {
		this.normalId = normalId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Long getFromAge() {
		return fromAge;
	}

	public void setFromAge(Long fromAge) {
		this.fromAge = fromAge;
	}

	public Long getToAge() {
		return toAge;
	}

	public void setToAge(Long toAge) {
		this.toAge = toAge;
	}

	public String getMinNormalValue() {
		return minNormalValue;
	}

	public void setMinNormalValue(String minNormalValue) {
		this.minNormalValue = minNormalValue;
	}

	public String getMaxNormalValue() {
		return maxNormalValue;
	}

	public void setMaxNormalValue(String maxNormalValue) {
		this.maxNormalValue = maxNormalValue;
	}

	public String getNormalValue() {
		return normalValue;
	}

	public void setNormalValue(String normalValue) {
		this.normalValue = normalValue;
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

	public Long getChargeCodeId() {
		return chargeCodeId;
	}

	public void setChargeCodeId(Long chargeCodeId) {
		this.chargeCodeId = chargeCodeId;
	}
	
	
	
}
