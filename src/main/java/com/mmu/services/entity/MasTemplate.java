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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name="public.MAS_TEMPLATE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_TEMPLATE_GENERATOR", sequenceName="mas_template_seq", allocationSize=1)
public class MasTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1781679160382157427L;
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_TEMPLATE_GENERATOR")
	@Column(name="TEMPLATE_ID")
	private long templateId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="STATUS")
	private String status;

	@Column(name="TEMPLATE_CODE")
	private String templateCode;

	@Column(name="TEMPLATE_NAME")
	private String templateName;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="HOSPITAL_ID", insertable = false, updatable = false)
	private MasHospital masHospital;
	
	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;
	
	//start self join
	//bi-directional many-to-one association to MasTemplate
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="TEMPLATE_PARENT_ID")
	private MasTemplate masTemplate;

	//bi-directional many-to-one association to MasTemplate
	@OneToMany(mappedBy="masTemplate")
	@JsonBackReference
	private List<MasTemplate> masTemplates;

	//end self join
	
	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	//bi-directional many-to-one association to TemplateApplication
		@OneToMany(mappedBy="masTemplate")
		@JsonBackReference
		private List<TemplateApplication> templateApplications;
		
	public MasTemplate() {
	}

	public long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
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

	public String getTemplateCode() {
		return this.templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	/*public MasTemplate getMasTemplate() {
		return this.masTemplate;
	}

	public void setMasTemplate(MasTemplate masTemplate) {
		this.masTemplate = masTemplate;
	}

	public List<MasTemplate> getMasTemplates() {
		return this.masTemplates;
	}

	public void setMasTemplates(List<MasTemplate> masTemplates) {
		this.masTemplates = masTemplates;
	}*/

	/*public MasTemplate addMasTemplate(MasTemplate masTemplate) {
		getMasTemplates().add(masTemplate);
		masTemplate.setMasTemplate(this);

		return masTemplate;
	}

	public MasTemplate removeMasTemplate(MasTemplate masTemplate) {
		getMasTemplates().remove(masTemplate);
		masTemplate.setMasTemplate(null);

		return masTemplate;
	}*/

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public MasTemplate getMasTemplate() {
		return masTemplate;
	}

	public void setMasTemplate(MasTemplate masTemplate) {
		this.masTemplate = masTemplate;
	}

	public List<MasTemplate> getMasTemplates() {
		return masTemplates;
	}

	public void setMasTemplates(List<MasTemplate> masTemplates) {
		this.masTemplates = masTemplates;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

}
