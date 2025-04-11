/**
 * 
 */
package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Naveen_kr_Sankhala
 *
 */
@Entity
@Table(name="mas_fund_scheme")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_FUND_SCHEME_GENERATOR", sequenceName="mas_fund_scheme_seq", allocationSize=1)
public class FundSchemeMaster implements Serializable{

	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator ="MAS_FUND_SCHEME_GENERATOR")
	@Column(name="fund_scheme_id")
	private Long fundSchemeId;

	@Column(name="fund_scheme_code")
	private String fundSchemeCode; 

	@Column(name="fund_scheme_name")
	private String fundSchemeName;
	
	@Column(name="status")
	private String status;
	
	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Date lastChgDate;
	
	public FundSchemeMaster() {
		
	}

	public Long getFundSchemeId() {
		return fundSchemeId;
	}

	public void setFundSchemeId(Long fundSchemeId) {
		this.fundSchemeId = fundSchemeId;
	}

	public String getFundSchemeCode() {
		return fundSchemeCode;
	}

	public void setFundSchemeCode(String fundSchemeCode) {
		this.fundSchemeCode = fundSchemeCode;
	}

	public String getFundSchemeName() {
		return fundSchemeName;
	}

	public void setFundSchemeName(String fundSchemeName) {
		this.fundSchemeName = fundSchemeName;
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
