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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_DISEASE_MAPPING database table.
 * 
 */
@Entity
@Table(name="MAS_DISEASE_MAPPING")
@NamedQuery(name="MasDiseaseMapping.findAll", query="SELECT m FROM MasDiseaseMapping m")
@SequenceGenerator(name="MAS_DISEASE_MAPPING_DISEASEMAPPINGID_GENERATOR", sequenceName="MAS_DISEASE_MAPPING_SEQ", allocationSize = 1)
public class MasDiseaseMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DISEASE_MAPPING_DISEASEMAPPINGID_GENERATOR")
	@Column(name="DISEASE_MAPPING_ID")
	private Long diseaseMappingId;

	@Column(name="DISEASE_ID")
	private Long diseaseId;

	@Column(name="ICD_ID")
	private String icdId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;
    
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISEASE_ID",nullable=false,insertable=false,updatable=false)
	private MasDisease masDisease;
	
	public MasDiseaseMapping() {
	}

	public Long getDiseaseMappingId() {
		return this.diseaseMappingId;
	}

	public void setDiseaseMappingId(Long diseaseMappingId) {
		this.diseaseMappingId = diseaseMappingId;
	}

	public Long getDiseaseId() {
		return this.diseaseId;
	}

	public void setDiseaseId(Long diseaseId) {
		this.diseaseId = diseaseId;
	}

	public String getIcdId() {
		return icdId;
	}

	public void setIcdId(String icdId) {
		this.icdId = icdId;
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

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MasDisease getMasDisease() {
		return masDisease;
	}

	public void setMasDisease(MasDisease masDisease) {
		this.masDisease = masDisease;
	}
    
}