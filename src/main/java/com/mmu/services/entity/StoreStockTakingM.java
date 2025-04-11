package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

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
 * The persistent class for the STORE_STOCK_TAKING_M database table.
 * 
 */
@Entity
@Table(name="STORE_STOCK_TAKING_M")
@NamedQuery(name="StoreStockTakingM.findAll", query="SELECT m FROM StoreStockTakingM m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_STOCK_TAKING_M_ID_GENERATOR", sequenceName="STORE_STOCK_TAKING_M_SEQ",allocationSize=1)
public class StoreStockTakingM implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7870781458469516160L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_STOCK_TAKING_M_ID_GENERATOR")
	@Column(name="TAKING_M_ID")
	private long takingMId;
	
	@Column(name="PHYSICAL_DATE")
	private Timestamp physicalDate;
	
	@Column(name="REASON")
	private String reason;

	@Column(name="STATUS")
	private String status;
	
	@Column(name="STOCK_TAKING_NO")
	private String stockTakingNo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUBMITTED_BY")
	private Users submittedBy;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPROVED_BY")
	private Users approvedBy;
	
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChangedDate;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;


	@OneToMany(mappedBy="storeStockTakingM")
	@JsonBackReference
	private Set<StoreStockTakingT> storeStockTakingTs;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@Column(name = "district_id")
	private Long districtId;

	public StoreStockTakingM() {
	}


	public long getTakingMId() {
		return takingMId;
	}


	public void setTakingMId(long takingMId) {
		this.takingMId = takingMId;
	}


	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public Users getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChangedDate() {
		return this.lastChangedDate;
	}

	public void setLastChangedDate(Timestamp lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	

	public Timestamp getPhysicalDate() {
		return this.physicalDate;
	}

	public void setPhysicalDate(Timestamp physicalDate) {
		this.physicalDate = physicalDate;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Users getSubmittedBy() {
		return submittedBy;
	}


	public void setSubmittedBy(Users submittedBy) {
		this.submittedBy = submittedBy;
	}


	public String getStockTakingNo() {
		return this.stockTakingNo;
	}

	public void setStockTakingNo(String stockTakingNo) {
		this.stockTakingNo = stockTakingNo;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}
	

	public Set<StoreStockTakingT> getStoreStockTakingTs() {
		return this.storeStockTakingTs;
	}

	public void setStoreStockTakingTs(Set<StoreStockTakingT> storeStockTakingTs) {
		this.storeStockTakingTs = storeStockTakingTs;
	}

	public StoreStockTakingT addStoreStockTakingT(StoreStockTakingT storeStockTakingT) {
		getStoreStockTakingTs().add(storeStockTakingT);
		storeStockTakingT.setStoreStockTakingM(this);

		return storeStockTakingT;
	}

	public StoreStockTakingT removeStoreStockTakingT(StoreStockTakingT storeStockTakingT) {
		getStoreStockTakingTs().remove(storeStockTakingT);
		storeStockTakingT.setStoreStockTakingM(null);

		return storeStockTakingT;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}


	public Long getCityId() {
		return cityId;
	}


	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}


	public Long getDistrictId() {
		return districtId;
	}


	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
}