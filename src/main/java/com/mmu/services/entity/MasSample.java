package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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
 * The persistent class for the MAS_SAMPLE database table.
 * 
 */
@Entity
@Table(name="MAS_SAMPLE")
@NamedQuery(name="MasSample.findAll", query="SELECT m FROM MasSample m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_SAMPLE_SAMPLEID_GENERATOR", sequenceName="MAS_SAMPLE_SEQ", allocationSize=1)
public class MasSample implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_SAMPLE_SAMPLEID_GENERATOR", sequenceName="MAS_SAMPLE_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_SAMPLE_SAMPLEID_GENERATOR")
	@Column(name="SAMPLE_ID")
	private Long sampleId;

	/*
	 * @Column(name="LAST_CHG_BY") private long lastChgBy;
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="SAMPLE_CODE")
	private String sampleCode;

	@Column(name="SAMPLE_DESCRIPTION")
	private String sampleDescription;

	private String status;

	public MasSample() {
	}

	public Long getSampleId() {
		return this.sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	/*
	 * public long getLastChgBy() { return this.lastChgBy; }
	 * 
	 * public void setLastChgBy(long lastChgBy) { this.lastChgBy = lastChgBy; }
	 */

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getSampleCode() {
		return this.sampleCode;
	}

	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}

	public String getSampleDescription() {
		return this.sampleDescription;
	}

	public void setSampleDescription(String sampleDescription) {
		this.sampleDescription = sampleDescription;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}