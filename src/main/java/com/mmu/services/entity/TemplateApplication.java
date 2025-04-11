package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;

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
 * The persistent class for the TEMPLATE_APPLICATION database table.
 * 
 */
@Entity
@Table(name="TEMPLATE_APPLICATION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="TemplateApplication.findAll", query="SELECT t FROM TemplateApplication t")
@SequenceGenerator(name="TEMPLATE_APPLICATION_GENERATOR", sequenceName="TEMPLATE_APPLICATION_SEQ", allocationSize=1)
public class TemplateApplication implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 9037613934511547340L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="TEMPLATE_APPLICATION_GENERATOR")
	@Column(name="TEMP_APP_ID")
	private Long tempAppId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="STATUS")
	private String status;

	//bi-directional many-to-one association to MasApplication
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APP_ID")
	private MasApplication masApplication;

	//bi-directional many-to-one association to MasTemplate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TEMPLATE_ID")
	private MasTemplate masTemplate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	public TemplateApplication() {
	}

	public long getTempAppId() {
		return this.tempAppId;
	}

	public void setTempAppId(long tempAppId) {
		this.tempAppId = tempAppId;
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

	public MasApplication getMasApplication() {
		return this.masApplication;
	}

	public void setMasApplication(MasApplication masApplication) {
		this.masApplication = masApplication;
	}

	public MasTemplate getMasTemplate() {
		return this.masTemplate;
	}

	public void setMasTemplate(MasTemplate masTemplate) {
		this.masTemplate = masTemplate;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public void setTempAppId(Long tempAppId) {
		this.tempAppId = tempAppId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((masTemplate == null) ? 0 : masTemplate.hashCode());
		result = prime * result + ((tempAppId == null) ? 0 : tempAppId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemplateApplication other = (TemplateApplication) obj;
		if (masTemplate == null) {
			if (other.masTemplate != null)
				return false;
		} else if (!masTemplate.equals(other.masTemplate))
			return false;
		if (tempAppId == null) {
			if (other.tempAppId != null)
				return false;
		} else if (!tempAppId.equals(other.tempAppId))
			return false;
		return true;
	}
	
	public static final Comparator<TemplateApplication> jobOrderComparator = new Comparator<TemplateApplication>() {
	    public int compare(TemplateApplication templateApplication, TemplateApplication templateApplication2) {
	      return templateApplication.getMasApplication().getOrderNo().compareTo(templateApplication2.getMasApplication().getOrderNo());
	    }
	  };
}