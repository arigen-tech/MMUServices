package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the MAS_DISEASE_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_DISEASE_TYPE")
@NamedQuery(name="MasDiseaseType.findAll", query="SELECT m FROM MasDiseaseType m")
@SequenceGenerator(name="MAS_DISEASE_TYPE_SEQ", sequenceName="MAS_DISEASE_TYPE_SEQ",allocationSize = 1)
public class MasDiseaseType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_DISEASE_TYPE_SEQ")
	@Column(name="DISEASE_TYPE_ID")
	private Long diseaseTypeId;

	@Column(name="DISEASE_TYPE_CODE")
	private String diseaseTypeCode;

	@Column(name="DISEASE_TYPE_NAME")
	private String diseaseTypeName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;
	
	public MasDiseaseType() {
	}

	public Long getDiseaseTypeId() {
		return this.diseaseTypeId;
	}

	public void setDiseaseTypeId(Long diseaseTypeId) {
		this.diseaseTypeId = diseaseTypeId;
	}

	public String getDiseaseTypeCode() {
		return this.diseaseTypeCode;
	}

	public void setDiseaseTypeCode(String diseaseTypeCode) {
		this.diseaseTypeCode = diseaseTypeCode;
	}

	public String getDiseaseTypeName() {
		return this.diseaseTypeName;
	}

	public void setDiseaseTypeName(String diseaseTypeName) {
		this.diseaseTypeName = diseaseTypeName;
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
    
}