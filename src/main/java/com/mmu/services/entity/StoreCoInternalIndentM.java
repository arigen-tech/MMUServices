package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the STORE_INTERNAL_INDENT_M database table.
 * 
 */
@Entity
@Table(name="store_co_internal_indent_m")
//@NamedQuery(name="StoreCoInternalIndentM.findAll", query="SELECT s FROM StoreCoInternalIndentM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="store_co_internal_indent_m_seq", sequenceName="store_co_internal_indent_m_seq", allocationSize=1)
public class StoreCoInternalIndentM implements Serializable {
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4704816344725110322L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="store_co_internal_indent_m_seq")
	@Column(name = "co_m_id")
	private Long id;
	
	@Column(name="DEMAND_NO")
	private String demandNo;

	@Column(name="DEMAND_DATE")
	private Timestamp demandDate;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	private String status;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users lastChgBy;
	
	@Column(name = "LAST_CHG_BY")
	private Long userId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	@Column(name = "district_id")
	private Long districtId;
	
	@OneToMany(mappedBy="storeCoInternalIndentM1")
	@JsonBackReference
	private List<StoreCoInternalIndentT> storeCoInternalIndentTs;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="do_id",nullable=false,insertable=false,updatable=false)
	private Users doUser;
	
	@Column(name = "do_id")
	private Long doId;
	
	@Column(name = "do_flag")
	private String doFlag;
	
	@Column(name = "do_date")
	private Date doDate;
	
	private String remarks;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="co_id",nullable=false,insertable=false,updatable=false)
	private Users coUser;
	
	@Column(name = "co_id")
	private Long coId;
	
	@Column(name = "co_date")
	private Date coDate;
	
	@Column(name="RECEIVED_DATE")
	private Timestamp receivedDate;

	public StoreCoInternalIndentM() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDemandNo() {
		return demandNo;
	}

	public void setDemandNo(String demandNo) {
		this.demandNo = demandNo;
	}

	public Timestamp getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(Timestamp demandDate) {
		this.demandDate = demandDate;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public MasMMU getMasMMU() {
		return masMMU;
	}

	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public List<StoreCoInternalIndentT> getStoreCoInternalIndentTs() {
		return storeCoInternalIndentTs;
	}

	public void setStoreCoInternalIndentTs(List<StoreCoInternalIndentT> storeCoInternalIndentTs) {
		this.storeCoInternalIndentTs = storeCoInternalIndentTs;
	}

	public Users getDoUser() {
		return doUser;
	}

	public void setDoUser(Users doUser) {
		this.doUser = doUser;
	}

	public Long getDoId() {
		return doId;
	}

	public void setDoId(Long doId) {
		this.doId = doId;
	}

	public String getDoFlag() {
		return doFlag;
	}

	public void setDoFlag(String doFlag) {
		this.doFlag = doFlag;
	}

	public Date getDoDate() {
		return doDate;
	}

	public void setDoDate(Date doDate) {
		this.doDate = doDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Users getCoUser() {
		return coUser;
	}

	public void setCoUser(Users coUser) {
		this.coUser = coUser;
	}

	public Long getCoId() {
		return coId;
	}

	public void setCoId(Long coId) {
		this.coId = coId;
	}

	public Date getCoDate() {
		return coDate;
	}

	public void setCoDate(Date coDate) {
		this.coDate = coDate;
	}

	public Timestamp getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}
	
}