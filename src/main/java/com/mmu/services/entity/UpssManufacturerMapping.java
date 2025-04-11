package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
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
 * The persistent class for the mas_mmu database table.
 * 
 */
@Entity
@Table(name="upss_manufacturer_mapping")
@NamedQuery(name="UpssManufacturerMapping.findAll", query="SELECT m FROM UpssManufacturerMapping m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="Upss_Manufacturer_Mapping_GENERATOR", sequenceName="upss_manu_id_seq", allocationSize=1)
public class UpssManufacturerMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator ="Upss_Manufacturer_Mapping_GENERATOR")
	@Column(name="upss_manu_id")
	private Long upssManuId;

 
	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

 
  
	@Column(name="SUPPLIER_ID")
	private Long supplierId;

	 
	private String status;
	


	@Column(name="district_id")
	private Long districtId;

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;



 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_ID",nullable=false,insertable=false,updatable=false)
	private MasStoreSupplier masStoreSupplier;


 
	public UpssManufacturerMapping() {
	}

 
	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
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

  	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Long getUpssManuId() {
		return upssManuId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public MasStoreSupplier getMasStoreSupplier() {
		return masStoreSupplier;
	}

	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}

	public void setUpssManuId(Long upssManuId) {
		this.upssManuId = upssManuId;
	}

 
	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

 	
 
	 
		 	
}