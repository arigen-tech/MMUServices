package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mas_symptoms")
@NamedQuery(name="MasSymptoms.findAll", query="SELECT s FROM MasSymptoms s")
@SequenceGenerator(name="MAS_SYMPTOMS_GENERATOR", sequenceName="mas_symptoms_seq", allocationSize=1)
public class MasSymptoms implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_SYMPTOMS_GENERATOR")
	@Column(name = "symptoms_id")
	private Long id;
	
	@Column(name = "symptoms_code")
	private String code;
	
	@Column(name = "symptoms_name")
	private String name;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "last_chg_date")
	private Date lastChgDate;
	
	@Column(name = "last_chg_by")
	private Long lastChgBy;
	
	@Column(name = "most_common_user")
	private String mostFrequentSymptoms;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public String getMostFrequentSymptoms() {
		return mostFrequentSymptoms;
	}

	public void setMostFrequentSymptoms(String mostFrequentSymptoms) {
		this.mostFrequentSymptoms = mostFrequentSymptoms;
	}
		
	
}
