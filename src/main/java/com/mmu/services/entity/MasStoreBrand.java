package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.Date;
import java.util.List;


/**
 * The persistent class for the MAS_STORE_BRAND database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_BRAND")
@NamedQuery(name="MasStoreBrand.findAll", query="SELECT m FROM MasStoreBrand m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_STORE_BRAND_BRANDID_GENERATOR", sequenceName="BRAND_ID", allocationSize=1)
public class MasStoreBrand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_STORE_BRAND_BRANDID_GENERATOR")
	@Column(name="BRAND_ID")
	private long brandId;

	@Column(name="BRAND_CODE")
	private String brandCode;

	@Column(name="BRAND_NAME")
	private String brandName;

	@Column(name="ITEM_GENERIC_ID")
	private Long itemGenericId;

	@Column(name="ITEM_ID")
	private Long itemId;

	@Column(name="LAST_CHG_BY")
	private String lastChgBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	private String status;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MANUFACTURER_ID")
	private MasManufacturer masManufacturer;

	@OneToMany(mappedBy="masStoreBrand")
	@JsonBackReference
	private List<StoreItemBatchStock> storeItemBatchStocks;

	public MasStoreBrand() {
	}

	public long getBrandId() {
		return this.brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Long getItemGenericId() {
		return this.itemGenericId;
	}

	public void setItemGenericId(Long itemGenericId) {
		this.itemGenericId = itemGenericId;
	}

	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(String lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasManufacturer getMasManufacturer() {
		return this.masManufacturer;
	}

	public void setMasManufacturer(MasManufacturer masManufacturer) {
		this.masManufacturer = masManufacturer;
	}

	public List<StoreItemBatchStock> getStoreItemBatchStocks() {
		return this.storeItemBatchStocks;
	}

	public void setStoreItemBatchStocks(List<StoreItemBatchStock> storeItemBatchStocks) {
		this.storeItemBatchStocks = storeItemBatchStocks;
	}

	public StoreItemBatchStock addStoreItemBatchStock(StoreItemBatchStock storeItemBatchStock) {
		getStoreItemBatchStocks().add(storeItemBatchStock);
		storeItemBatchStock.setMasStoreBrand(this);

		return storeItemBatchStock;
	}

	public StoreItemBatchStock removeStoreItemBatchStock(StoreItemBatchStock storeItemBatchStock) {
		getStoreItemBatchStocks().remove(storeItemBatchStock);
		storeItemBatchStock.setMasStoreBrand(null);

		return storeItemBatchStock;
	}

}