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
import com.mmu.services.entity.DgFixedValue;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgNormalValue;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasSample;
import com.mmu.services.entity.MasSubChargecode;
import com.mmu.services.entity.MasUOM;


/**
 * The persistent class for the DG_SUB_MAS_INVESTIGATION database table.
 * 
 */
@Entity
@Table(name="DG_SUB_MAS_INVESTIGATION")
@NamedQuery(name="DgSubMasInvestigation.findAll", query="SELECT d FROM DgSubMasInvestigation d")
@SequenceGenerator(name="DG_SUB_MAS_INVESTIGATION_SUBINVESTIGATIONID_GENERATOR", sequenceName="DG_SUB_MAS_INVESTIGATION_SEQ", allocationSize=1)
public class DgSubMasInvestigation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_SUB_MAS_INVESTIGATION_SUBINVESTIGATIONID_GENERATOR")
	@Column(name="SUB_INVESTIGATION_ID")
	private Long subInvestigationId;

	@Column(name="CHARGE_CODE_ID")
	private Long chargeCodeId;

	@Column(name="COMPARISON_TYPE")
	private String comparisonType;

	@Column(name="INVESTIGATION_ID")
	private Long investigationId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MAIN_CHARGECODE_ID")
	private Long mainChargecodeId;

	@Column(name="ORDER_NO")
	private Long orderNo;

	@Column(name="RESULT_TYPE")
	private String resultType;

	@Column(name="SAMPLE_ID")
	private Long sampleId;

	private String status;

	@Column(name="SUB_CHARGECODE_ID")
	private Long subChargecodeId;

	@Column(name="SUB_INVESTIGATION_CODE")
	private String subInvestigationCode;

	@Column(name="SUB_INVESTIGATION_NAME")
	private String subInvestigationName;

	@Column(name="UOM_ID")
	private Long uomId;
	
	@Column(name="LOINC_CODE")
	private String loincCode;
	
	@Column(name="MAIN_INVESTIGATION_ID")
	private Long mainInvestigationId;
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "INVESTIGATION_ID",nullable=false,insertable=false,updatable=false)
	private DgMasInvestigation dgMasInvestigation;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MAIN_CHARGECODE_ID",nullable=false,insertable=false,updatable=false)
	private MasMainChargecode masMainChargecode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SUB_CHARGECODE_ID",nullable=false,insertable=false,updatable=false)
	private MasSubChargecode masSubChargecode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_ID",nullable=false,insertable=false,updatable=false)
	private MasSample masSample;
	
	/*@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "COLLECTION_ID",nullable=false,insertable=false,updatable=false)
	private DgMasCollection dgMasCollection; */
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "UOM_ID",nullable=false,insertable=false,updatable=false)
	private MasUOM masUOM;	
	
	@OneToMany(mappedBy="dgSubMasInvestigation")
	@JsonBackReference
	List<DgFixedValue> dgFixedValues;
	
	public DgSubMasInvestigation() {
	}

	

	public Long getSubInvestigationId() {
		return subInvestigationId;
	}



	public Long getChargeCodeId() {
		return this.chargeCodeId;
	}

	public void setChargeCodeId(Long chargeCodeId) {
		this.chargeCodeId = chargeCodeId;
	}

	public String getComparisonType() {
		return this.comparisonType;
	}

	public void setComparisonType(String comparisonType) {
		this.comparisonType = comparisonType;
	}

	public Long getInvestigationId() {
		return this.investigationId;
	}

	public void setInvestigationId(Long investigationId) {
		this.investigationId = investigationId;
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

	public Long getMainChargecodeId() {
		return this.mainChargecodeId;
	}

	public void setMainChargecodeId(Long mainChargecodeId) {
		this.mainChargecodeId = mainChargecodeId;
	}

	public Long getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public String getResultType() {
		return this.resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public Long getSampleId() {
		return this.sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSubChargecodeId() {
		return this.subChargecodeId;
	}

	public void setSubChargecodeId(Long subChargecodeId) {
		this.subChargecodeId = subChargecodeId;
	}

	public String getSubInvestigationCode() {
		return this.subInvestigationCode;
	}

	public void setSubInvestigationCode(String subInvestigationCode) {
		this.subInvestigationCode = subInvestigationCode;
	}

	public String getSubInvestigationName() {
		return this.subInvestigationName;
	}

	public void setSubInvestigationName(String subInvestigationName) {
		this.subInvestigationName = subInvestigationName;
	}

	public Long getUomId() {
		return this.uomId;
	}

	public void setUomId(Long uomId) {
		this.uomId = uomId;
	}    
	
	public String getLoincCode() {
		return loincCode;
	}

	public void setLoincCode(String loincCode) {
		this.loincCode = loincCode;
	}

	public DgMasInvestigation getDgMasInvestigation() {
		return dgMasInvestigation;
	}

	public void setDgMasInvestigation(DgMasInvestigation dgMasInvestigation) {
		this.dgMasInvestigation = dgMasInvestigation;
	}

	public MasMainChargecode getMasMainChargecode() {
		return masMainChargecode;
	}

	public void setMasMainChargecode(MasMainChargecode masMainChargecode) {
		this.masMainChargecode = masMainChargecode;
	}

	public MasSubChargecode getMasSubChargecode() {
		return masSubChargecode;
	}

	public void setMasSubChargecode(MasSubChargecode masSubChargecode) {
		this.masSubChargecode = masSubChargecode;
	}

	public MasSample getMasSample() {
		return masSample;
	}

	public void setMasSample(MasSample masSample) {
		this.masSample = masSample;
	}
	/*
	public DgMasCollection getDgMasCollection() {
		return dgMasCollection;
	}

	public void setDgMasCollection(DgMasCollection dgMasCollection) {
		this.dgMasCollection = dgMasCollection;
	} */

	public MasUOM getMasUOM() {
		return masUOM;
	}

	public void setMasUOM(MasUOM masUOM) {
		this.masUOM = masUOM;
	}

	public void setSubInvestigationId(Long subInvestigationId) {
		this.subInvestigationId = subInvestigationId;
	}



	public List<DgFixedValue> getDgFixedValues() {
		return dgFixedValues;
	}



	public void setDgFixedValues(List<DgFixedValue> dgFixedValues) {
		this.dgFixedValues = dgFixedValues;
	}
	
	@OneToMany(mappedBy="dgSubMasInvestigation")
	@JsonBackReference
	List<DgNormalValue> dgNormalValue;

	public List<DgNormalValue> getDgNormalValue() {
		return dgNormalValue;
	}



	public void setDgNormalValue(List<DgNormalValue> dgNormalValue) {
		this.dgNormalValue = dgNormalValue;
	}

	public Long getMainInvestigationId() {
		return mainInvestigationId;
	}

	public void setMainInvestigationId(Long mainInvestigationId) {
		this.mainInvestigationId = mainInvestigationId;
	}
	
	
	 
	 

}