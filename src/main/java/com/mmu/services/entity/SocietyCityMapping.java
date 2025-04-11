/**
 * 
 */
package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Naveen_kr_Sankhala
 *The persistent class for the cluster_city_mapping database table.
 */

@Entity
@Table(name="ulb_city_mapping")
//@NamedQuery(name="SocietyCityMapping.findAll", query="SELECT c FROM SocietyCityMapping c")
public class SocietyCityMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SOCIETY_CITY_MAPPING_GENERATOR", sequenceName="ulb_city_mapping_seq")
	@GeneratedValue(strategy=GenerationType.AUTO,generator ="SOCIETY_CITY_MAPPING_GENERATOR")
	@Column(name="ulb_city_mapping_id")
	private Long societyCityMappingId;

	@Column(name="city_id")
	private Long cityId;

	@Column(name="ulb_id")
	private Long societyId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="status")
	private String status;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ulb_id",nullable=false,insertable=false,updatable=false)
	private MasSociety masSociety;
	
	public SocietyCityMapping() {
		
	}

	public Long getSocietyCityMappingId() {
		return societyCityMappingId;
	}

	public void setSocietyCityMappingId(Long societyCityMappingId) {
		this.societyCityMappingId = societyCityMappingId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getSocietyId() {
		return societyId;
	}

	public void setSocietyId(Long societyId) {
		this.societyId = societyId;
	}

	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
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

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public MasSociety getMasSociety() {
		return masSociety;
	}

	public void setMasSociety(MasSociety masSociety) {
		this.masSociety = masSociety;
	}
	
	
}
