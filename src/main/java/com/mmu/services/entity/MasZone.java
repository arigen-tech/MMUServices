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
@Table(name = "mas_zone")
@SequenceGenerator(name="MAS_MMU_ZONE_GENERATOR", sequenceName="mas_zone_seq", allocationSize=1)
public class MasZone implements Serializable {

	private static final long serialVersionUID = 1641351028207062928L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator ="MAS_MMU_ZONE_GENERATOR")
	@Column(name = "zone_id")
	private Long zoneId;
	
	@Column(name = "zone_code")
	private String zoneCode;
	
	@Column(name = "zone_name")
	private String zoneName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private MasCity masCity;
	
	private String status;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;
	
	@Column(name = "last_chg_date")
	private Date lastChangeDate;

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
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
	
}
