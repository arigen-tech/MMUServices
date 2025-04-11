package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the mas_treatment_instructions database table.
 * 
 */
@Entity
@Table(name="mas_treatment_instructions")
@NamedQuery(name="MasTreatmentInstruction.findAll", query="SELECT m FROM MasTreatmentInstruction m")
public class MasTreatmentInstruction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="instructions_id")
	private Long instructionsId;

	@Column(name="instructions_code")
	private String instructionsCode;

	@Column(name="instructions_name")
	private String instructionsName;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	private String status;

	public MasTreatmentInstruction() {
	}

	public Long getInstructionsId() {
		return this.instructionsId;
	}

	public void setInstructionsId(Long instructionsId) {
		this.instructionsId = instructionsId;
	}

	public String getInstructionsCode() {
		return this.instructionsCode;
	}

	public void setInstructionsCode(String instructionsCode) {
		this.instructionsCode = instructionsCode;
	}

	public String getInstructionsName() {
		return this.instructionsName;
	}

	public void setInstructionsName(String instructionsName) {
		this.instructionsName = instructionsName;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}