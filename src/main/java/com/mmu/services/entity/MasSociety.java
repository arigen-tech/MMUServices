/**
 * 
 */
package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Naveen_kr_Sankhala
 *
 */
/**
 * The persistent class for the mas_society database table.
 * 
 */
@Entity
@Table(name="mas_ulb")
//@NamedQuery(name="MasSociety.findAll", query="SELECT ms FROM MasSociety ms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MMU_ULB_GENERATOR", sequenceName="mas_ulb_seq", allocationSize=1)
public class MasSociety implements Serializable{

	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator ="MAS_MMU_ULB_GENERATOR")
	@Column(name="ulb_id")
	private Long societyId;

	@Column(name="ulb_code")
	private String societyCode; 

	@Column(name="ulb_name")
	private String societyName;
	
	@Column(name="status")
	private String status;
	
	@Column(name="lasy_chg_by")
	private Long lastChgBy;

	@Column(name="lasy_chg_date")
	private Date lastChgDate;
	
	
	public MasSociety() {
		
	}


	public Long getSocietyId() {
		return societyId;
	}


	public void setSocietyId(Long societyId) {
		this.societyId = societyId;
	}


	public String getSocietyCode() {
		return societyCode;
	}


	public void setSocietyCode(String societyCode) {
		this.societyCode = societyCode;
	}


	public String getSocietyName() {
		return societyName;
	}


	public void setSocietyName(String societyName) {
		this.societyName = societyName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Long getLastChgBy() {
		return lastChgBy;
	}


	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}


	public Date getLastChgDate() {
		return lastChgDate;
	}


	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	
	
}
