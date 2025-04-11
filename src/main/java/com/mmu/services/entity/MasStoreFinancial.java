package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the MAS_STORE_FINANCIAL database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_FINANCIAL")
@NamedQuery(name="MasStoreFinancial.findAll", query="SELECT m FROM MasStoreFinancial m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_STORE_FINANCIAL_SEQ", sequenceName="MAS_STORE_FINANCIAL_SEQ", allocationSize=1)
public class MasStoreFinancial implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2577738156347755992L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_STORE_FINANCIAL_SEQ")
	@Column(name="FINANCIAL_ID")
	private long financialId;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="FINANCIAL_YEAR")
	private String financialYear;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;

	private String status;

	@Column(name="YEAR_DESCRIPTION")
	private String yearDescription;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	//bi-directional many-to-one association to StorePoM
	@OneToMany(mappedBy="masStoreFinancial")
	private Set<StorePoM> storePoMs;

	public MasStoreFinancial() {
	}

	public long getFinancialId() {
		return this.financialId;
	}

	public void setFinancialId(long financialId) {
		this.financialId = financialId;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFinancialYear() {
		return this.financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getYearDescription() {
		return this.yearDescription;
	}

	public void setYearDescription(String yearDescription) {
		this.yearDescription = yearDescription;
	}

	

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Set<StorePoM> getStorePoMs() {
		return this.storePoMs;
	}

	public void setStorePoMs(Set<StorePoM> storePoMs) {
		this.storePoMs = storePoMs;
	}

	public StorePoM addStorePoM(StorePoM storePoM) {
		getStorePoMs().add(storePoM);
		storePoM.setMasStoreFinancial(this);

		return storePoM;
	}

	public StorePoM removeStorePoM(StorePoM storePoM) {
		getStorePoMs().remove(storePoM);
		storePoM.setMasStoreFinancial(null);

		return storePoM;
	}
	@Column(name = "mark_financial_year")
	private String markFinancialYear;

	public String getMarkFinancialYear() {
		return markFinancialYear;
	}

	public void setMarkFinancialYear(String markFinancialYear) {
		this.markFinancialYear = markFinancialYear;
	}
	

	@Column(name="future_year")
	private String futureYear;
	
	@Column(name="order_year")
	private Long orderYear;

	public String getFutureYear() {
		return futureYear;
	}

	public void setFutureYear(String futureYear) {
		this.futureYear = futureYear;
	}

	public Long getOrderYear() {
		return orderYear;
	}

	public void setOrderYear(Long orderYear) {
		this.orderYear = orderYear;
	}
	
	
}