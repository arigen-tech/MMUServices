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
@Table(name = "authority_mapping")
@SequenceGenerator(name="AUTHORITY_MAPPING_GENERATOR", sequenceName="AUTHORITY_MAPPING_seq", allocationSize=1)
public class ApprovingMapping implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="AUTHORITY_MAPPING_GENERATOR")
	@Column(name = "authority_mapping_id")
	private Long authorityMappingId;
	
	
	@Column(name = "authority_id")
	private Long authorityId;
	
	@Column(name = "user_type_id")
	private Long userTypeId;
	
	private String status;

	public Long getAuthorityMappingId() {
		return authorityMappingId;
	}

	public void setAuthorityMappingId(Long authorityMappingId) {
		this.authorityMappingId = authorityMappingId;
	}

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public Long getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="authority_id",nullable=false,insertable=false,updatable=false)
	private ApprovingAuthority approvingAuthority;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_type_id",nullable=false,insertable=false,updatable=false)
	private MasUserType masUserType;

	
	public ApprovingAuthority getApprovingAuthority() {
		return approvingAuthority;
	}

	public void setApprovingAuthority(ApprovingAuthority approvingAuthority) {
		this.approvingAuthority = approvingAuthority;
	}

	public MasUserType getMasUserType() {
		return masUserType;
	}

	public void setMasUserType(MasUserType masUserType) {
		this.masUserType = masUserType;
	}

	@Override
	public String toString() {
		return "ApprovingMapping [authorityMappingId=" + authorityMappingId + ", status=" + status + "]";
	}
	 	 
	 
}
