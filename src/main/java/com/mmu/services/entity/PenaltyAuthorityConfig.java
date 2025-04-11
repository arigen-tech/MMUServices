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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Penalty_authority_config")
@SequenceGenerator(name="PENALTY_AUTHORITY_CONFIG_GENERATOR", sequenceName="authority_config_seq", allocationSize=1)
public class PenaltyAuthorityConfig implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="PENALTY_AUTHORITY_CONFIG_GENERATOR")
	@Column(name = "authority_config_id")
	private Long authorityConfigId;
	
	 

	 
	@Column(name="authority_id")
	private Long authorityId;
	
	@Column(name="upps_id")
	private Long uppsId;
 	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;
	
 	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="upps_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private MasDistrict masDistrict;

 
	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public ApprovingAuthority getApprovingAuthority() {
		return approvingAuthority;
	}

	public void setApprovingAuthority(ApprovingAuthority approvingAuthority) {
		this.approvingAuthority = approvingAuthority;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="authority_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private ApprovingAuthority approvingAuthority;

 	 

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

	public Long getAuthorityConfigId() {
		return authorityConfigId;
	}

	public void setAuthorityConfigId(Long authorityConfigId) {
		this.authorityConfigId = authorityConfigId;
	}

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public Long getUppsId() {
		return uppsId;
	}

	public void setUppsId(Long uppsId) {
		this.uppsId = uppsId;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	 
	 
}
