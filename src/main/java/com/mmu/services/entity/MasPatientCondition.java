package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the MAS_PATIENT_CONDITION database table.
 * 
 */
@Entity
@Table(name="MAS_PATIENT_CONDITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasPatientCondition.findAll", query="SELECT m FROM MasPatientCondition m")
public class MasPatientCondition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_PATIENT_CONDITION_SEQ", sequenceName="MAS_PATIENT_CONDITION_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_PATIENT_CONDITION_SEQ")
	@Column(name="CONDITION_ID")
	private long conditionId;

	/*
	 * @Column(name="CONDITION_CODE") private String conditionCode;
	 */

	@Column(name="CONDITION_NAME")
	private String conditionName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasPatientCondition() {
	}

	public long getConditionId() {
		return this.conditionId;
	}

	public void setConditionId(long conditionId) {
		this.conditionId = conditionId;
	}

	/*
	 * public String getConditionCode() { return this.conditionCode; }
	 * 
	 * public void setConditionCode(String conditionCode) { this.conditionCode =
	 * conditionCode; }
	 */

	public String getConditionName() {
		return this.conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
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

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}