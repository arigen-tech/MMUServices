package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the OPD_TEMPLATE database table.
 * 
 */
@Entity
@Table(name="OPD_TEMPLATE")
@NamedQuery(name="OpdTemplate.findAll", query="SELECT o FROM OpdTemplate o")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="OPD_TEMPLATE_SEQ", sequenceName="OPD_TEMPLATE_SEQ", allocationSize=1)
public class OpdTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7890631305059404048L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="OPD_TEMPLATE_SEQ")
	
	@Column(name="TEMPLATE_ID")
	private Long templateId;

	

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	@Column(name="DOCTOR_ID")
	private Long doctorId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	@Column(name="TEMPLATE_CODE")
	private String templateCode;

	@Column(name="TEMPLATE_NAME")
	private String templateName;

	@Column(name="TEMPLATE_TYPE")
	private String templateType;


	 @OneToMany(mappedBy="opdTemplate", cascade = CascadeType.ALL)
	 private List<OpdTemplateInvestigation> opdTemplate;

	public List<OpdTemplateInvestigation> getOpdTemplate() {
		return opdTemplate;
	}

	public void setOpdTemplate(List<OpdTemplateInvestigation> opdTemplate) {
		this.opdTemplate = opdTemplate;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
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

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	
}