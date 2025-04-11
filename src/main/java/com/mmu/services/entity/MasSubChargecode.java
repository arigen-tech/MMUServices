package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

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





/**
 * The persistent class for the MAS_SUB_CHARGECODE database table.
 * 
 */
@Entity
@Table(name="MAS_SUB_CHARGECODE")
@NamedQuery(name="MasSubChargecode.findAll", query="SELECT m FROM MasSubChargecode m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_SUB_CHARGECODE_SEQ", sequenceName="MAS_SUB_CHARGECODE_SEQ", allocationSize=1)
public class MasSubChargecode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2948030094889032391L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_SUB_CHARGECODE_SEQ")
	@Column(name="SUB_CHARGECODE_ID")
	private Long subChargecodeId;

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	@Column(name="MAIN_CHARGECODE_ID")
	private Long mainChargecodeId;

	private String status;

	@Column(name="SUB_CHARGECODE_CODE")
	private String subChargecodeCode;

	@Column(name="SUB_CHARGECODE_NAME")
	private String subChargecodeName;

	/*
	 * @OneToMany(mappedBy="subChargeCode", cascade = CascadeType.ALL)
	 * 
	 * @JsonBackReference private List<MainCharge> mainCharge;
	 */

	public Long getSubChargecodeId() {
		return subChargecodeId;
	}

	public void setSubChargecodeId(Long subChargecodeId) {
		this.subChargecodeId = subChargecodeId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getMainChargecodeId() {
		return mainChargecodeId;
	}

	public void setMainChargecodeId(Long mainChargecodeId) {
		this.mainChargecodeId = mainChargecodeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubChargecodeCode() {
		return subChargecodeCode;
	}

	public void setSubChargecodeCode(String subChargecodeCode) {
		this.subChargecodeCode = subChargecodeCode;
	}

	public String getSubChargecodeName() {
		return subChargecodeName;
	}

	public void setSubChargecodeName(String subChargecodeName) {
		this.subChargecodeName = subChargecodeName;
	}
	
		
	public MasMainChargecode getMasMainChargecode() {
		return masMainChargecode;
	}

	public void setMasMainChargecode(MasMainChargecode masMainChargecode) {
		this.masMainChargecode = masMainChargecode;
	}

	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="MAIN_CHARGECODE_ID" ,nullable=false,insertable=false,updatable=false)
	private MasMainChargecode masMainChargecode;

}