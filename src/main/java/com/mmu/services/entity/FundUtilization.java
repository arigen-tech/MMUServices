package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the FUND_UTILIZATION database table.
 * 
 */
@Entity
@Table(name="FUND_UTILIZATION")
@NamedQuery(name="FundUtilization.findAll", query="SELECT f FROM FundUtilization f")
@SequenceGenerator(name="FUND_UTILIZATION_SEQ", sequenceName="FUND_UTILIZATION_SEQ", allocationSize=1)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FundUtilization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="FUND_UTILIZATION_SEQ")
	private long id;

	@Column(name="AMOUNT_ALLOTTED")
	private BigDecimal amountAllotted;

	@Column(name="AMOUNT_AVIL")
	private BigDecimal amountAvil;

	@Column(name="AUTHORITY_NO")
	private String authorityNo;



	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="REFUNDED_AMOUNT")
	private BigDecimal refundedAmount;

	//bi-directional many-to-one association to MasStoreFinancial
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="FINANCIAL_ID")
	private MasStoreFinancial masStoreFinancial;
	
	@ManyToOne(fetch=FetchType.LAZY)
	 @JsonBackReference
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@ManyToOne(fetch=FetchType.LAZY)
	  @JsonBackReference
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;
	
	public FundUtilization() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmountAllotted() {
		return this.amountAllotted;
	}

	public void setAmountAllotted(BigDecimal amountAllotted) {
		this.amountAllotted = amountAllotted;
	}

	public BigDecimal getAmountAvil() {
		return this.amountAvil;
	}

	public void setAmountAvil(BigDecimal amountAvil) {
		this.amountAvil = amountAvil;
	}

	

	
	public MasHospital getMasHospital() {
		return masHospital;
	}

	

	public String getAuthorityNo() {
		return authorityNo;
	}

	public void setAuthorityNo(String authorityNo) {
		this.authorityNo = authorityNo;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public BigDecimal getRefundedAmount() {
		return this.refundedAmount;
	}

	public void setRefundedAmount(BigDecimal refundedAmount) {
		this.refundedAmount = refundedAmount;
	}

	public MasStoreFinancial getMasStoreFinancial() {
		return this.masStoreFinancial;
	}

	public void setMasStoreFinancial(MasStoreFinancial masStoreFinancial) {
		this.masStoreFinancial = masStoreFinancial;
	}

}