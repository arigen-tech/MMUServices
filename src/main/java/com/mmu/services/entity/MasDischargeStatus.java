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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_DISCHARGE_STATUS database table.
 * 
 */
@Entity
@Table(name="MAS_DISCHARGE_STATUS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasDischargeStatus.findAll", query="SELECT m FROM MasDischargeStatus m")
public class MasDischargeStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_DISCHARGE_STATUS_DISCHARGESTATUSID_GENERATOR",sequenceName="MAS_DISCHARGE_STATUS_SEQ" )
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DISCHARGE_STATUS_DISCHARGESTATUSID_GENERATOR")
	@Column(name="DISCHARGE_STATUS_ID")
	private long dischargeStatusId;

	@Column(name="DISCHARGE_STATUS_CODE")
	private String dischargeStatusCode;

	@Column(name="DISCHARGE_STATUS_NAME")
	private String dischargeStatusName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasDischargeStatus() {
	}

	public long getDischargeStatusId() {
		return this.dischargeStatusId;
	}

	public void setDischargeStatusId(long dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}

	public String getDischargeStatusCode() {
		return this.dischargeStatusCode;
	}

	public void setDischargeStatusCode(String dischargeStatusCode) {
		this.dischargeStatusCode = dischargeStatusCode;
	}

	public String getDischargeStatusName() {
		return this.dischargeStatusName;
	}

	public void setDischargeStatusName(String dischargeStatusName) {
		this.dischargeStatusName = dischargeStatusName;
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