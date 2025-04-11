package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mas_phase database table.
 * 
 */
@Entity
@Table(name="mas_phase")
@NamedQuery(name="MasPhase.findAll", query="SELECT m FROM MasPhase m")
@SequenceGenerator(name="MAS_PHASE_PHASEID_GENERATOR", sequenceName="MAS_PHASE_SEQ", allocationSize = 1)
public class MasPhase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_PHASE_PHASEID_GENERATOR")
	@Column(name="phase_id")
	private Long phaseId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="phase_name")
	private String phaseName;

	@Column(name="phase_value")
	private String phaseValue;

	private String status;

	public MasPhase() {
	}

	public Long getPhaseId() {
		return this.phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public String getPhaseName() {
		return this.phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getPhaseValue() {
		return this.phaseValue;
	}

	public void setPhaseValue(String phaseValue) {
		this.phaseValue = phaseValue;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}