package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the mas_authority database table.
 * 
 */
@Entity
@Table(name="mas_authority")
@NamedQuery(name="MasAuthority.findAll", query="SELECT m FROM MasAuthority m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "MAS_AUTHORITY_AUTHORITYID_GENERATOR", sequenceName = "MAS_AUTHORITY_SEQ", allocationSize=1)
public class MasAuthority implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_AUTHORITY_AUTHORITYID_GENERATOR")
	@Column(name="authority_id")
	private Long authorityId;

	@Column(name="authority_code")
	private String authorityCode;

	@Column(name="authority_name")
	private String authorityName;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="level_no")
	private String levelNo;
	
	@Column(name="order_no")
	private Long orderNo;

	private String status;

	public MasAuthority() {
	}

	public Long getAuthorityId() {
		return this.authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthorityCode() {
		return this.authorityCode;
	}

	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	public String getAuthorityName() {
		return this.authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getLevelNo() {
		return this.levelNo;
	}

	public void setLevelNo(String levelNo) {
		this.levelNo = levelNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	

}