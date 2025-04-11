package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the STORE_BUDGETARY_T database table.
 * 
 */
@Entity
@Table(name="STORE_BUDGETARY_T")
@NamedQuery(name="StoreBudgetaryT.findAll", query="SELECT s FROM StoreBudgetaryT s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_BUDGETARY_T_SEQ", sequenceName="STORE_BUDGETARY_T_SEQ")
public class StoreBudgetaryT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_BUDGETARY_T_SEQ")
	@Column(name="BUDGETARY_T_ID")
	private long budgetaryTId;

	@Column(name="LAST_LP_RATE")
	private BigDecimal lastLpRate;

	@Column(name="QTY_REQUIRED")
	private long qtyRequried;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem1;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BUDGETARY_M_ID")
	private StoreBudgetaryM storeBudgetaryM;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TEMP_TABLE_ID")
	private TempDirectReceivingForBackLp tempDirectReceivingForBackLp;
	
	
	public StoreBudgetaryT() {
	}

	public long getBudgetaryTId() {
		return this.budgetaryTId;
	}

	public void setBudgetaryTId(long budgetaryTId) {
		this.budgetaryTId = budgetaryTId;
	}

	

	

	

	public BigDecimal getLastLpRate() {
		return lastLpRate;
	}

	public void setLastLpRate(BigDecimal lastLpRate) {
		this.lastLpRate = lastLpRate;
	}

	public long getQtyRequried() {
		return qtyRequried;
	}

	public void setQtyRequried(long qtyRequried) {
		this.qtyRequried = qtyRequried;
	}


	public MasStoreItem getMasStoreItem1() {
		return this.masStoreItem1;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem1) {
		this.masStoreItem1 = masStoreItem1;
	}

	public StoreBudgetaryM getStoreBudgetaryM() {
		return this.storeBudgetaryM;
	}

	public void setStoreBudgetaryM(StoreBudgetaryM storeBudgetaryM) {
		this.storeBudgetaryM = storeBudgetaryM;
	}

	public TempDirectReceivingForBackLp getTempDirectReceivingForBackLp() {
		return tempDirectReceivingForBackLp;
	}

	public void setTempDirectReceivingForBackLp(TempDirectReceivingForBackLp tempDirectReceivingForBackLp) {
		this.tempDirectReceivingForBackLp = tempDirectReceivingForBackLp;
	}
	
	

}