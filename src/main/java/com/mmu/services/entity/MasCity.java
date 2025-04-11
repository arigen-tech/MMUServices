package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mas_city")
@SequenceGenerator(name="MAS_MMU_CITY_GENERATOR", sequenceName="mas_city_seq", allocationSize=1)
public class MasCity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="MAS_MMU_CITY_GENERATOR")
	@Column(name = "city_id")
	private Long cityId;
	
	@Column(name = "city_code")
	private String cityCode;
	
	@Column(name = "city_name")
	public String cityName;
	
	@ManyToOne
	@JoinColumn(name = "district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;
	
	@Column(name = "mmu_operational")
	private String mmuOperational;
	
	@Column(name = "district_id")
	private Long districtId;

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(Long lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public String getMmuOperational() {
		return mmuOperational;
	}

	public void setMmuOperational(String mmuOperational) {
		this.mmuOperational = mmuOperational;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	@Column(name = "order_no")
	private Long orderNo;

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	@Column(name = "indent_city")
	private String indentCity;

	public String getIndentCity() {
		return indentCity;
	}

	public void setIndentCity(String indentCity) {
		this.indentCity = indentCity;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
	
	
}
