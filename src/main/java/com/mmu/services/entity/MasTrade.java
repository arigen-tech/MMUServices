package com.mmu.services.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_TRADE database table.
 * 
 */
@Entity
@Immutable
@Table(name="VU_MAS_TRADE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasTrade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8079886433392705092L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="TRADE_ID",updatable = false, nullable = false)
	private long tradeId;

	@Column(name="TRADE_CODE")
	private String tradeCode;

	@Column(name="TRADE_NAME")
	private String tradeName;

	@OneToMany(mappedBy="masTrade")
	@JsonBackReference
	private List<MasEmployee> masEmployees;

	public MasTrade() {
	}

	public long getTradeId() {
		return this.tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	

	public String getTradeCode() {
		return this.tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getTradeName() {
		return this.tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public List<MasEmployee> getMasEmployees() {
		return this.masEmployees;
	}

	public void setMasEmployees(List<MasEmployee> masEmployees) {
		this.masEmployees = masEmployees;
	}

}