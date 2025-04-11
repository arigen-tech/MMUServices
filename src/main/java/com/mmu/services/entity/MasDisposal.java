/*package com.mmu.services.entity;

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

*//**
 * The persistent class for the MAS_DISPOSAL database table.
 * 
 *//*
@Entity
@Table(name = "MAS_DISPOSAL")
@NamedQuery(name = "MasDisposal.findAll", query = "SELECT m FROM MasDisposal m")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@SequenceGenerator(name = "MAS_DISPOSAL_SEQ", sequenceName = "MAS_DISPOSAL_SEQ", allocationSize = 1)
public class MasDisposal implements Serializable {

	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -1747552949408504983L;

	@Id
	//@SequenceGenerator(name = "MAS_DISPOSAL_SEQ", sequenceName = "MAS_DISPOSAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_DISPOSAL_SEQ")
	@Column(name = "DISPOSAL_ID")
	private long disposalId;

	@Column(name = "DISPOSAL_CODE")
	private String disposalCode;

	@Column(name = "DISPOSAL_NAME")
	private String disposalName;

	@Column(name = "DISPOSAL_TYPE")
	private String disposalType;

	private String flag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users users;

	// @Temporal(TemporalType.DATE)
	@Column(name = "LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name = "STATUS")
	private String status;

	@OneToMany(mappedBy = "masDisposal")
	@JsonBackReference
	private List<AdmissionDischarge> admissionDischarges;

	public MasDisposal() {
	}

	public long getDisposalId() {
		return this.disposalId;
	}

	public void setDisposalId(long disposalId) {
		this.disposalId = disposalId;
	}

	public String getDisposalCode() {
		return this.disposalCode;
	}

	public void setDisposalCode(String disposalCode) {
		this.disposalCode = disposalCode;
	}

	public String getDisposalName() {
		return this.disposalName;
	}

	public void setDisposalName(String disposalName) {
		this.disposalName = disposalName;
	}

	public String getDisposalType() {
		return this.disposalType;
	}

	public void setDisposalType(String disposalType) {
		this.disposalType = disposalType;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public List<AdmissionDischarge> getAdmissionDischarges() {
		return this.admissionDischarges;
	}

	public void setAdmissionDischarges(List<AdmissionDischarge> admissionDischarges) {
		this.admissionDischarges = admissionDischarges;
	}

	public AdmissionDischarge addAdmissionDischarge(AdmissionDischarge admissionDischarge) {
		getAdmissionDischarges().add(admissionDischarge);
		admissionDischarge.setMasDisposal(this);

		return admissionDischarge;
	}

	public AdmissionDischarge removeAdmissionDischarge(AdmissionDischarge admissionDischarge) {
		getAdmissionDischarges().remove(admissionDischarge);
		admissionDischarge.setMasDisposal(null);

		return admissionDischarge;
	}

}*/