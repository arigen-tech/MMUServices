package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
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

/**
 * The persistent class for the TEMPLATE_MEDICAL_ADVICE database table.
 * 
 */
@Entity
@Table(name="OPD_TEMPLATE_MEDICAL_ADVICE")
@NamedQuery(name="OpdTemplateMedicalAdvice.findAll", query="SELECT o FROM OpdTemplateMedicalAdvice o")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OPD_TEMPLATE_MEDICAL_ADVICE_S", sequenceName="OPD_TEMPLATE_MEDICAL_ADVICE_S", allocationSize=1)
public class OpdTemplateMedicalAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2338245462993157886L;


	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OPD_TEMPLATE_MEDICAL_ADVICE_S")
	
	@Column(name="TEMPLATE_MADVICE_ID")
	private Long templateMadviceId;

	
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;


	@Column(name="OPD_TEMPLATE_ID")
	private Long opdTemplateId;
	
	@Column(name="MEDICAL_ADVICE")
	private String medicalAdvice;
    
	@Column(name = "status")
	private String status;
		
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "OPD_TEMPLATE_ID",nullable=false,insertable=false,updatable=false)
	private OpdTemplate opdTemplate;


	public Long getTemplateMadviceId() {
		return templateMadviceId;
	}


	public void setTemplateMadviceId(Long templateMadviceId) {
		this.templateMadviceId = templateMadviceId;
	}


	public Long getLastChgBy() {
		return lastChgBy;
	}


	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}


	public Timestamp getLastChgDate() {
		return lastChgDate;
	}


	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}


	
	
	
	public String getMedicalAdvice() {
		return medicalAdvice;
	}


	public void setMedicalAdvice(String medicalAdvice) {
		this.medicalAdvice = medicalAdvice;
	}


	public Long getOpdTemplateId() {
		return opdTemplateId;
	}


	public void setOpdTemplateId(Long opdTemplateId) {
		this.opdTemplateId = opdTemplateId;
	}


	public OpdTemplate getOpdTemplate() {
		return opdTemplate;
	}


	public void setOpdTemplate(OpdTemplate opdTemplate) {
		this.opdTemplate = opdTemplate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

      
		

}