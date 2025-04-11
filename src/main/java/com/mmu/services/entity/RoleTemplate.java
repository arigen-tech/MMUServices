package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

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
 * The persistent class for the ROLE_TEMPLATE database table.
 * 
 */

@Entity
@Table(name = "ROLE_TEMPLATE")
@NamedQuery(name = "RoleTemplate.findAll", query = "SELECT r FROM RoleTemplate r")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@SequenceGenerator(name = "ROLE_TEMPLATE_SEQ", sequenceName = "ROLE_TEMPLATE_SEQ", allocationSize=1)
public class RoleTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4032007290862704043L;
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ROLE_TEMPLATE_SEQ")
	@Column(name = "ROLE_TEMPLATE_ID")
	private Long roleTemplateId;
	
	
	@Column(name = "STATUS")
	private String status;

	@Column(name = "LAST_CHG_DATE")
	private Date lastChgDate;

	
	 @ManyToOne(fetch = FetchType.LAZY)	 
	 @JoinColumn(name = "LAST_CHG_BY") 
	 private Users user;
	 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_ID")
	private MasTemplate msTemplate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	private MasRole msRole;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOSPITAL_ID")
	private MasHospital masHospital;
	
	
	public long getRoleTemplateId() {
		return roleTemplateId;
	}

	public void setRoleTemplateId(long roleTemplateId) {
		this.roleTemplateId = roleTemplateId;
	}

	
	

	public MasTemplate getMsTemplate() {
		return msTemplate;
	}

	public void setMsTemplate(MasTemplate msTemplate) {
		this.msTemplate = msTemplate;
	}

	public MasRole getMsRole() {
		return msRole;
	}

	public void setMsRole(MasRole msRole) {
		this.msRole = msRole;
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

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}
    
	
}
