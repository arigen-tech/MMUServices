package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;



/**
 * The persistent class for the MAIN_CHARGE database table.
 * 
 */
@Entity
@Table(name="MAIN_CHARGE")
@NamedQuery(name="MainCharge.findAll", query="SELECT m FROM MainCharge m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MAIN_CHARGECODE_SEQ", sequenceName="MAS_MAIN_CHARGECODE_SEQ", allocationSize=1)
public class MainCharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8328773358969785568L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_MAIN_CHARGECODE_SEQ")
	@Column(name="MAIN_CHARGE_ID")
	private Long mainChargeId;

	@Column(name="ADD_EDIT_BY_ID")
	private Long addEditById;

	@Column(name="ADD_EDIT_DATE_TIME")
	private Timestamp addEditDateTime;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="MAIN_CHARGE_CODE")
	private String mainChargeCode;

	@Column(name="MAIN_CHARGE_DESCRIPTION")
	private String mainChargeDescription;

	@Column(name="STATUS_ID")
	private Long statusId;

	@ManyToOne(fetch=FetchType.LAZY) //--for multiple patient one to many
	@JoinColumn(name = "MAIN_CHARGECODE_ID",nullable=false,insertable=false,updatable=false)
	private MasSubChargecode subChargeCode;
	
	@OneToMany(mappedBy="mainChargeCode", cascade = CascadeType.ALL)
	 @JsonBackReference
	 private List<DgMasInvestigation> dgMasInvestigations;

	public Long getMainChargeId() {
		return mainChargeId;
	}

	public void setMainChargeId(Long mainChargeId) {
		this.mainChargeId = mainChargeId;
	}

	public Long getAddEditById() {
		return addEditById;
	}

	public void setAddEditById(Long addEditById) {
		this.addEditById = addEditById;
	}

	public Timestamp getAddEditDateTime() {
		return addEditDateTime;
	}

	public void setAddEditDateTime(Timestamp addEditDateTime) {
		this.addEditDateTime = addEditDateTime;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getMainChargeCode() {
		return mainChargeCode;
	}

	public void setMainChargeCode(String mainChargeCode) {
		this.mainChargeCode = mainChargeCode;
	}

	public String getMainChargeDescription() {
		return mainChargeDescription;
	}

	public void setMainChargeDescription(String mainChargeDescription) {
		this.mainChargeDescription = mainChargeDescription;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public MasSubChargecode getSubChargeCode() {
		return subChargeCode;
	}

	public void setSubChargeCode(MasSubChargecode subChargeCode) {
		this.subChargeCode = subChargeCode;
	}

	public List<DgMasInvestigation> getDgMasInvestigations() {
		return dgMasInvestigations;
	}

	public void setDgMasInvestigations(List<DgMasInvestigation> dgMasInvestigations) {
		this.dgMasInvestigations = dgMasInvestigations;
	}

}