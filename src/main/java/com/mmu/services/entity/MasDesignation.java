package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

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

@Entity
@Table(name="MAS_DESIGNATION")
@SequenceGenerator(name = "MAS_DESIGNATION_GENERATOR", sequenceName = "MAS_DESIGNATION_SEQ", allocationSize=1)
public class MasDesignation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7105745785631626088L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_DESIGNATION_GENERATOR")
	@Column(name="DESIGNATION_ID")
	private Long designationId;
	
	@Column(name="DESIGNATION_NAME")
	private String designationName;
	
	@Column(name="DESIGNATION_CODE")
	private String designationCode;
	
	@Column(name="STATUS")
	private String status;
	
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	/*
	 * @Column(name="TYPE") private String type;
	 * 
	 * @Column(name="GROUP_ID") private Long groupId;
	 * 
	 * @Column(name="ID") private Long id;
	 */

	@ManyToOne(fetch = FetchType.LAZY)	 
	@JoinColumn(name = "LAST_CHG_BY") 
	private Users user;
	
	@OneToMany
	@JsonBackReference
	@JoinColumn(name="DESIGNATION_ID")		
	private List<RoleDesignation> roleDesignation;
	
	/*public long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(long designationId) {
		this.designationId = designationId;
	}*/
	

	public String getDesignationName() {
		return designationName;
	}

	public Long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String getDesignationCode() {
		return designationCode;
	}

	public void setDesignationCode(String designationCode) {
		this.designationCode = designationCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	/*
	 * public String getType() { return type; }
	 * 
	 * public void setType(String type) { this.type = type; }
	 */

	 

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	/*
	 * public Long getGroupId() { return groupId; }
	 * 
	 * public void setGroupId(Long groupId) { this.groupId = groupId; }
	 */

	/*
	 * public Long getId() { return id; }
	 * 
	 * public void setId(Long id) { this.id = id; }
	 */

	public List<RoleDesignation> getRoleDesignation() {
		return roleDesignation;
	}

	public void setRoleDesignation(List<RoleDesignation> roleDesignation) {
		this.roleDesignation = roleDesignation;
	}
	
	
}