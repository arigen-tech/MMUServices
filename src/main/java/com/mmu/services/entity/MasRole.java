package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

/**
 * The persistent class for the MAS_ROLE database table.
 * 
 */

@Entity
@Table(name="MAS_ROLE")
@NamedQuery(name="MasRole.findAll", query="SELECT m FROM MasRole m")
@SequenceGenerator(name = "MAS_ROLE_SEQ", sequenceName = "MAS_ROLE_SEQ", allocationSize=1)
public class MasRole implements Serializable {
	
	private static final long serialVersionUID = -6186331350482560628L;
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_ROLE_SEQ")
	@Column(name = "ROLE_ID")
	private Long roleId;
	
	@Column(name = "ROLE_CODE")
	private String roleCode;
	
	@Column(name = "ROLE_NAME")
	private String roleName;
	
	private String status;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;
	
	@Column(name = "LAST_CHG_DATE")
	private Date lastChgDate;

	/*@OneToMany
	@JsonBackReference
	@JoinColumn(name="ROLE_ID",nullable=false,insertable=false,updatable=false)		
	private List<RoleDesignation> roleDesignation;*/
	
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

}
