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
 * The persistent class for the OPD_TEMPLATE_INVESTIGATION database table.
 * 
 */
@Entity
@Table(name="OPD_TEMPLATE_INVESTIGATION")
@NamedQuery(name="OpdTemplateInvestigation.findAll", query="SELECT o FROM OpdTemplateInvestigation o")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OPD_TEMPLATE_INVESTIGATION_SEQ", sequenceName="OPD_TEMPLATE_INVESTIGATION_SEQ", allocationSize=1)
public class OpdTemplateInvestigation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2338245462993157886L;


	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OPD_TEMPLATE_INVESTIGATION_SEQ")
	
	@Column(name="TEMPLATE_INVESTIGATION_ID")
	private Long templateInvestigationId;

	
	@Column(name="INVESTIGATION_ID")
	private Long investigationId;
	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	@Column(name="TEMPLATE_ID")
	private Long templateId;
    
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "INVESTIGATION_ID",nullable=false,insertable=false,updatable=false)
	private DgMasInvestigation dgMasInvestigation;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "TEMPLATE_ID",nullable=false,insertable=false,updatable=false)
	private OpdTemplate opdTemplate;
	
	 public DgMasInvestigation getDgMasInvestigation() {
		return dgMasInvestigation;
	}


	public void setDgMasInvestigation(DgMasInvestigation dgMasInvestigation) {
		this.dgMasInvestigation = dgMasInvestigation;
	}

	 
	 public Long getTemplateInvestigationId() {
			return templateInvestigationId;
		}


		public void setTemplateInvestigationId(Long templateInvestigationId) {
			this.templateInvestigationId = templateInvestigationId;
		}


		public Long getInvestigationId() {
			return investigationId;
		}


		public void setInvestigationId(Long investigationId) {
			this.investigationId = investigationId;
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


		public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
		}


		public Long getTemplateId() {
			return templateId;
		}


		public void setTemplateId(Long templateId) {
			this.templateId = templateId;
		}


		public OpdTemplate getOpdTemplate() {
			return opdTemplate;
		}


		public void setOpdTemplate(OpdTemplate opdTemplate) {
			this.opdTemplate = opdTemplate;
		}


		

}