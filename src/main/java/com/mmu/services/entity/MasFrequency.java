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
 * The persistent class for the MAS_FREQUENCY database table.
 * 
 */
@Entity
@Table(name="MAS_FREQUENCY")
@NamedQuery(name="MasFrequency.findAll", query="SELECT m FROM MasFrequency m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_FREQUENCY_FREQUENCYID_GENERATOR", sequenceName="MAS_FREQUENCY_SEQ", allocationSize=1)
public class MasFrequency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -71373866342875305L;

	@Id
	@SequenceGenerator(name="MAS_FREQUENCY_FREQUENCYID_GENERATOR", sequenceName="MAS_FREQUENCY_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_FREQUENCY_FREQUENCYID_GENERATOR")
	@Column(name="FREQUENCY_ID")
	private Long frequencyId;

	@Column(name="FREQUENCY_CODE")
	private String frequencyCode;
	
	@Column(name="FEQ")
	private Double feq;

	@Column(name="FREQUENCY_NAME")
	private String frequencyName;
    
	@Column(name="FREQUENCY_HIN_NAME")
	private String frequencyHinName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY", nullable = true)
	private Users user;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="STATUS")
	private String status;
    
	@Column(name="ORDER_NO")
	private Long orderNo;
	
	public Long getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getFrequencyCode() {
		return frequencyCode;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public Double getFeq() {
		return feq;
	}

	public void setFeq(Double feq) {
		this.feq = feq;
	}

	public String getFrequencyName() {
		return frequencyName;
	}

	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}

	/*
	 * public Long getLastChgBy() { return lastChgBy; }
	 * 
	 * public void setLastChgBy(Long lastChgBy) { this.lastChgBy = lastChgBy; }
	 */
	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return status;
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

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public String getFrequencyHinName() {
		return frequencyHinName;
	}

	public void setFrequencyHinName(String frequencyHinName) {
		this.frequencyHinName = frequencyHinName;
	}

	
}