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
@Table(name = "mas_ward")
@SequenceGenerator(name="MAS_MMU_WARD_GENERATOR", sequenceName="mas_ward_seq", allocationSize=1)
public class MasWard implements Serializable {

	private static final long serialVersionUID = 984571528265139374L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="MAS_MMU_WARD_GENERATOR")
	@Column(name = "ward_id")
	private Long wardId;
	
	@Column(name = "ward_code")
	private String wardCode;
	
	@Column(name = "ward_name")
	private String wardName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zone_id")
	private MasZone masZone;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;

	
	@Column(name = "ward_no")
	private String wardNo;
	
	public MasWard() {
		super();
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public MasZone getMasZone() {
		return masZone;
	}

	public void setMasZone(MasZone masZone) {
		this.masZone = masZone;
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

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public String getWardNo() {
		return wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	
	
}
