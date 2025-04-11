package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mas_camp")
@SequenceGenerator(name="MAS_CAMP_CAMPID_GENERATOR", sequenceName="mas_camp_seq", allocationSize=1)
public class MasCamp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_CAMP_CAMPID_GENERATOR")
	@Column(name = "camp_id")
	private Long campId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id",nullable=false,insertable=false,updatable=false)
	private MasState masState;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id",nullable=false,insertable=false,updatable=false)
	private MasDepartment masDepartment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zone_id",nullable=false,insertable=false,updatable=false)
	private MasZone masZone;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id",nullable=false,insertable=false,updatable=false)
	private MasWard masWard;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;
	
	@Column(name = "camp_date")
	private Date campDate;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "landmark")
	private String landMark;
	
	@Column(name = "latitudes")
	private Double lattitude;
	
	@Column(name = "longitudes")
	private Double longitude;
	
	@Column(name = "camp_day")
	private String day;
	
	@Column(name = "weekly_off")
	private String weeklyOff;
	
	private String status;
	
	@Column(name = "start_time")
	private Time startTime;
	
	@Column(name = "end_time")
	private Time endTime;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;
	
	@Column(name = "camp_year")
	private Long year;
	
	@Column(name = "camp_month")
	private Long month;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@Column(name = "ward_id")
	private Long wardId;
	
	@Column(name = "zone_id")
	private Long zoneId;
	
	@Column(name = "department_id")
	private Long departmentId;
	
	@Column(name = "district_id") 
	private Long districtId;

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public MasState getMasState() {
		return masState;
	}

	public void setMasState(MasState masState) {
		this.masState = masState;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}
	
	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public MasZone getMasZone() {
		return masZone;
	}

	public void setMasZone(MasZone masZone) {
		this.masZone = masZone;
	}

	public MasWard getMasWard() {
		return masWard;
	}

	public void setMasWard(MasWard masWard) {
		this.masWard = masWard;
	}

	public MasMMU getMasMMU() {
		return masMMU;
	}

	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}

	public Date getCampDate() {
		return campDate;
	}

	public void setCampDate(Date campDate) {
		this.campDate = campDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
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
	
	public String getWeeklyOff() {
		return weeklyOff;
	}

	public void setWeeklyOff(String weeklyOff) {
		this.weeklyOff = weeklyOff;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
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

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
	/*
	 * @Column(name = "cluster_id") private Long clusterId;
	 * 
	 * public Long getClusterId() { return clusterId; }
	 * 
	 * public void setClusterId(Long clusterId) { this.clusterId = clusterId; }
	 */


}
