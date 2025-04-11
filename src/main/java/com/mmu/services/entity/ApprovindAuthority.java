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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mas_approving_authority")
@SequenceGenerator(name="MAS_HEAD_TYPE_GENERATOR", sequenceName="MAS_HEAD_TYPE_seq", allocationSize=1)
public class ApprovindAuthority implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="MAS_HEAD_TYPE_GENERATOR")
	@Column(name = "authority_id")
	private Long authorityId;
	
	@Column(name = "approvind_authority_code")
	private String approvingAuthorityCode;
	
	@Column(name = "approvind_authority_name")
	public String approvingAuthorityName;
	
	@Column(name="level_of_user")
	private String levelOfUser;
	
	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;
	
	@Column(name="final_approving_authority")
	private String finalApprovingAuthority;
	
	@Column(name="order_no")
	private String orderNo;
	
	
	public String getFinalApprovingAuthority() {
		return finalApprovingAuthority;
	}

	public void setFinalApprovingAuthority(String finalApprovingAuthority) {
		this.finalApprovingAuthority = finalApprovingAuthority;
	}

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public String getApprovingAuthorityCode() {
		return approvingAuthorityCode;
	}

	public void setApprovingAuthorityCode(String approvingAuthorityCode) {
		this.approvingAuthorityCode = approvingAuthorityCode;
	}

	public String getApprovingAuthorityName() {
		return approvingAuthorityName;
	}

	public void setApprovingAuthorityName(String approvingAuthorityName) {
		this.approvingAuthorityName = approvingAuthorityName;
	}

	public String getLevelOfUser() {
		return levelOfUser;
	}

	public void setLevelOfUser(String levelOfUser) {
		this.levelOfUser = levelOfUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(Long lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	 
	 
}
