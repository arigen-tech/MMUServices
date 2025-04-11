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

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

 


/**
 * The persistent class for the MAS_DISTRICT database table.
 * 
 */
@Entity
@Table(name="mas_district")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MMU_DISTRICT_GENERATOR", sequenceName="mas_district_seq", allocationSize=1)
public class MasDistrict implements Serializable {

	private static final long serialVersionUID = -7773826869990687351L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="MAS_MMU_DISTRICT_GENERATOR")
	@Column(name="DISTRICT_ID",updatable = false, nullable = false)
	private long districtId;

	@Column(name="DISTRICT_CODE")
	private String districtCode;

	@Column(name="DISTRICT_NAME")
	private String districtName;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STATE_ID")
	private MasState masState;

	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;
	
	@Column(name = "population")
	private Long population;
	
	@Column(name = "latitudes")
	private Double lattitude;
	
	@Column(name = "longitudes")
	private Double longitude;
	
	
	public MasDistrict() {
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public MasState getMasState() {
		return masState;
	}

	public void setMasState(MasState masState) {
		this.masState = masState;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Long getPopulation() {
		return population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}

	public Double getLattitude() {
		return lattitude;
	}

	public void setLattitude(Double lattitude) {
		this.lattitude = lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
   
	@Column(name = "order_no")
	private Long orderNo;


	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	@Column(name="upss")
	private String upss;


	public String getUpss() {
		return upss;
	}

	public void setUpss(String upss) {
		this.upss = upss;
	}
	
	@Column(name="start_date")
	private Date startDate;


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	
	
	
}