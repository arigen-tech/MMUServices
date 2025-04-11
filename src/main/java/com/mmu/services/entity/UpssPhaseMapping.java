package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the upss_phase_mapping database table.
 * 
 */
@Entity
@Table(name="upss_phase_mapping")
@NamedQuery(name="UpssPhaseMapping.findAll", query="SELECT u FROM UpssPhaseMapping u")
@SequenceGenerator(name="UPSS_PHASE_MAPPING_UPSSPHASEID_GENERATOR", sequenceName="UPSS_PHASE_MAPPING_SEQ", allocationSize = 1)
public class UpssPhaseMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UPSS_PHASE_MAPPING_UPSSPHASEID_GENERATOR")
	@Column(name="upss_phase_id")
	private Long upssPhaseId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="phase_id")
	private Long phaseId;

	private String status;

	@Column(name="upss_id")
	private Long upssId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="upss_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="phase_id",nullable=false,insertable=false,updatable=false)
	private MasPhase masPhase;

	public UpssPhaseMapping() {
	}

	public Long getUpssPhaseId() {
		return this.upssPhaseId;
	}

	public void setUpssPhaseId(Long upssPhaseId) {
		this.upssPhaseId = upssPhaseId;
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

	public Long getPhaseId() {
		return this.phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUpssId() {
		return this.upssId;
	}

	public void setUpssId(Long upssId) {
		this.upssId = upssId;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public MasPhase getMasPhase() {
		return masPhase;
	}

	public void setMasPhase(MasPhase masPhase) {
		this.masPhase = masPhase;
	}

	
}