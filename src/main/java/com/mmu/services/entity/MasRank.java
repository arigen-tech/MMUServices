package com.mmu.services.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_RANK database table.
 * 
 */

@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_MAS_RANK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasRank implements Serializable {


	private static final long serialVersionUID = 3900325364045955518L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="RANK_ID",updatable = false, nullable = false)
	private long rankId;

	
	@Column(name="RANK_CODE")
	private String rankCode;

	@Column(name="RANK_NAME")
	private String rankName;
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EMPLOYEE_CATEGORY_ID")
	private MasEmployeeCategory masEmployeeCategory;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TRADE_ID")
	private MasTrade masTrade;
	
	
	
	public MasRank() {
	}

	public long getRankId() {
		return this.rankId;
	}

	public void setRankId(long rankId) {
		this.rankId = rankId;
	}


	public String getRankCode() {
		return this.rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public String getRankName() {
		return this.rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}


	
	public MasEmployeeCategory getMasEmployeeCategory() {
		return this.masEmployeeCategory;
	}
	
	public void setMasEmployeeCategory(MasEmployeeCategory masEmployeeCategory) {
		this.masEmployeeCategory = masEmployeeCategory;
	}

	public MasTrade getMasTrade() {
		return masTrade;
	}

	public void setMasTrade(MasTrade masTrade) {
		this.masTrade = masTrade;
	}
	
	
}