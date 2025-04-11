package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_STORE_LP_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_LP_TYPE")
@NamedQuery(name="MasStoreLpType.findAll", query="SELECT m FROM MasStoreLpType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_STORE_LP_TYPE_SEQ", sequenceName="MAS_STORE_LP_TYPE_SEQ", allocationSize=1)
public class MasStoreLpType implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1389315299696615418L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_STORE_LP_TYPE_SEQ")
	@Column(name="LP_TYPE_ID")
	private long lpTypeId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	@Column(name="LP_CODE")
	private long lpCode;
	
	@Column(name="FORM_LP_AMT")
	private long fromlpAmount;
	
	@Column(name="TO_LP_AMT")
	private long tolpAmount;

	public long getLpTypeId() {
		return lpTypeId;
	}

	public void setLpTypeId(long lpTypeId) {
		this.lpTypeId = lpTypeId;
	}

	public long getLpCode() {
		return lpCode;
	}

	public void setLpCode(long lpCode) {
		this.lpCode = lpCode;
	}

	

	public long getFromlpAmount() {
		return fromlpAmount;
	}

	public void setFromlpAmount(long fromlpAmount) {
		this.fromlpAmount = fromlpAmount;
	}

	public long getTolpAmount() {
		return tolpAmount;
	}

	public void setTolpAmount(long tolpAmount) {
		this.tolpAmount = tolpAmount;
	}

	public String getLpName() {
		return lpName;
	}

	public void setLpName(String lpName) {
		this.lpName = lpName;
	}

	@Column(name="LP_NAME")
	private String lpName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	@OneToMany(mappedBy="masStoreLpType")
	@JsonBackReference
	private List<StoreBudgetaryM> storeBudgetaryMs;

	public MasStoreLpType() {
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<StoreBudgetaryM> getStoreBudgetaryMs() {
		return this.storeBudgetaryMs;
	}

	public void setStoreBudgetaryMs(List<StoreBudgetaryM> storeBudgetaryMs) {
		this.storeBudgetaryMs = storeBudgetaryMs;
	}

	public StoreBudgetaryM addStoreBudgetaryM(StoreBudgetaryM storeBudgetaryM) {
		getStoreBudgetaryMs().add(storeBudgetaryM);
		storeBudgetaryM.setMasStoreLpType(this);

		return storeBudgetaryM;
	}

	public StoreBudgetaryM removeStoreBudgetaryM(StoreBudgetaryM storeBudgetaryM) {
		getStoreBudgetaryMs().remove(storeBudgetaryM);
		storeBudgetaryM.setMasStoreLpType(null);

		return storeBudgetaryM;
	}

}