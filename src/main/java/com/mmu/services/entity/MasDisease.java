package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the MAS_DISEASE database table.
 * 
 */
@Entity
@Table(name="MAS_DISEASE")
@NamedQuery(name="MasDisease.findAll", query="SELECT m FROM MasDisease m")
@SequenceGenerator(name="MAS_DISEASE_DISEASEID_GENERATOR", sequenceName="MAS_DISEASE_SEQ",allocationSize = 1 )
public class MasDisease implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DISEASE_DISEASEID_GENERATOR")
	@Column(name="DISEASE_ID")
	private Long diseaseId;

	@Column(name="DISEASE_CODE")
	private String diseaseCode;

	@Column(name="DISEASE_NAME")
	private String diseaseName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;
	
	@Column(name="DISEASE_TYPE_ID")
	private Long diseaseTypeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISEASE_TYPE_ID",nullable=false,insertable=false,updatable=false)
	private MasDiseaseType masDiseaseType;
	
	public MasDisease() {
	}

	public Long getDiseaseId() {
		return this.diseaseId;
	}

	public void setDiseaseId(Long diseaseId) {
		this.diseaseId = diseaseId;
	}

	public String getDiseaseCode() {
		return this.diseaseCode;
	}

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}

	public String getDiseaseName() {
		return this.diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
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

	public Long getDiseaseTypeId() {
		return diseaseTypeId;
	}

	public void setDiseaseTypeId(Long diseaseTypeId) {
		this.diseaseTypeId = diseaseTypeId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MasDiseaseType getMasDiseaseType() {
		return masDiseaseType;
	}

	public void setMasDiseaseType(MasDiseaseType masDiseaseType) {
		this.masDiseaseType = masDiseaseType;
	}
    
		  
}