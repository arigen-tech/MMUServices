package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the authority_mapping database table.
 * 
 */
@Entity
@Table(name="authority_mapping")
@NamedQuery(name="AuthorityMapping.findAll", query="SELECT a FROM AuthorityMapping a")
@SequenceGenerator(name="authority_mapping_seq", sequenceName="authority_mapping_seq", allocationSize=1)
public class AuthorityMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="authority_mapping_seq")
	@Column(name="authority_mapping_id")
	private Long authorityMappingId;

	@Column(name="authority_id")
	private String authorityId;

	@Column(name="user_type_id")
	private Long userTypeId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="authority_id",nullable=false,insertable=false,updatable=false)
	private MasAuthority masAuthority;

	public Long getAuthorityMappingId() {
		return authorityMappingId;
	}

	public void setAuthorityMappingId(Long authorityMappingId) {
		this.authorityMappingId = authorityMappingId;
	}

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public Long getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
	}

	public MasAuthority getMasAuthority() {
		return masAuthority;
	}

	public void setMasAuthority(MasAuthority masAuthority) {
		this.masAuthority = masAuthority;
	}

	
}