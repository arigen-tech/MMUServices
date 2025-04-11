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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="DG_UOM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasUOM.findAll", query="SELECT m FROM MasUOM m")
@SequenceGenerator(name="MAS_UNI_GENERATOR", sequenceName="DG_UOM_SEQ", allocationSize=1)
public class MasUOM implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6298305253409590970L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="MAS_UNI_GENERATOR")
	@Column(name="UOM_ID")
	private Long UOMId;
	
	@Column(name="UOM_CODE")
	private String UOMCode;
	
	@Column(name="UOM_NAME")
	private String UOMName;
	
	@Column(name="STATUS")
	private String UOMStatus;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	public Long getUOMId() {
		return UOMId;
	}

	public void setUOMId(Long uOMId) {
		UOMId = uOMId;
	}

	public String getUOMCode() {
		return UOMCode;
	}

	public void setUOMCode(String uOMCode) {
		UOMCode = uOMCode;
	}

	public String getUOMName() {
		return UOMName;
	}

	public void setUOMName(String uOMName) {
		UOMName = uOMName;
	}

	public String getUOMStatus() {
		return UOMStatus;
	}

	public void setUOMStatus(String uOMStatus) {
		UOMStatus = uOMStatus;
	}

	/*
	 * public Long getLastChgBy() { return lastChgBy; }
	 * 
	 * public void setLastChgBy(Long lastChgBy) { this.lastChgBy = lastChgBy; }
	 */

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
