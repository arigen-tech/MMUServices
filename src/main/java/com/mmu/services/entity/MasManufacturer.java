package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the MAS_MANUFACTURER database table.
 * 
 */
@Entity
@Table(name="MAS_MANUFACTURER")
@NamedQuery(name="MasManufacturer.findAll", query="SELECT m FROM MasManufacturer m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MANUFACTURER_MANUFACTURERID_GENERATOR", sequenceName="mas_manufacturer_seq", allocationSize=1)
public class MasManufacturer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_MANUFACTURER_MANUFACTURERID_GENERATOR")
	@Column(name="MANUFACTURER_ID")
	private long manufacturerId;

	private String address1;

	private String address2;

	@Column(name="CF_LOCAL_DISTRIBUTOR_ADDRESS1")
	private String cfLocalDistributorAddress1;

	@Column(name="CF_LOCAL_DISTRIBUTOR_ADDRESS2")
	private String cfLocalDistributorAddress2;

	@Column(name="CF_LOCAL_DISTRIBUTOR_NAME")
	private String cfLocalDistributorName;

	@Column(name="CITY_ID")
	private Long cityId;
	
	@Column(name="SUPPLIER_TYPE_ID")
	private Long supplierTypeId;

	@Column(name="CONTACT_PERSON")
	private String contactPerson;

	@Column(name="EMAIL_ID")
	private String emailId;

	@Column(name="FAX_NUMBER")
	private String faxNumber;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="LICENCE_NO")
	private String licenceNo;

	@Column(name="MANUFACTURER_CODE")
	private String manufacturerCode;

	@Column(name="MANUFACTURER_NAME")
	private String manufacturerName;

	private String mobileno;

	private String phoneno;

	@Column(name="PIN_CODE")
	private Long pinCode;

	@Column(name="SALES_TAX_NO")
	private String salesTaxNo;

	@Column(name="STATE_ID")
	private Long stateId;
	
	@Column(name = "last_chg_by")
	private Long lastChangeBy;

	private String status;

	private String url;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;

	@OneToMany(mappedBy="masManufacturer")
	@JsonBackReference
	private List<MasStoreBrand> masStoreBrands;

	@OneToMany(mappedBy="masManufacturer")
	@JsonBackReference
	private List<StoreItemBatchStock> storeItemBatchStocks;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_TYPE_ID", nullable=false,insertable=false,updatable=false)
	private MasStoreSupplierType masStoreSupplierType;

	public MasManufacturer() {
	}

	public long getManufacturerId() {
		return this.manufacturerId;
	}

	public void setManufacturerId(long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCfLocalDistributorAddress1() {
		return this.cfLocalDistributorAddress1;
	}

	public void setCfLocalDistributorAddress1(String cfLocalDistributorAddress1) {
		this.cfLocalDistributorAddress1 = cfLocalDistributorAddress1;
	}

	public String getCfLocalDistributorAddress2() {
		return this.cfLocalDistributorAddress2;
	}

	public void setCfLocalDistributorAddress2(String cfLocalDistributorAddress2) {
		this.cfLocalDistributorAddress2 = cfLocalDistributorAddress2;
	}

	public String getCfLocalDistributorName() {
		return this.cfLocalDistributorName;
	}

	public void setCfLocalDistributorName(String cfLocalDistributorName) {
		this.cfLocalDistributorName = cfLocalDistributorName;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFaxNumber() {
		return this.faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getLicenceNo() {
		return this.licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public String getManufacturerName() {
		return this.manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getMobileno() {
		return this.mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getPhoneno() {
		return this.phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public Long getPinCode() {
		return this.pinCode;
	}

	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}

	public String getSalesTaxNo() {
		return this.salesTaxNo;
	}

	public void setSalesTaxNo(String salesTaxNo) {
		this.salesTaxNo = salesTaxNo;
	}

	public Long getStateId() {
		return this.stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<MasStoreBrand> getMasStoreBrands() {
		return this.masStoreBrands;
	}

	public void setMasStoreBrands(List<MasStoreBrand> masStoreBrands) {
		this.masStoreBrands = masStoreBrands;
	}

	public MasStoreBrand addMasStoreBrand(MasStoreBrand masStoreBrand) {
		getMasStoreBrands().add(masStoreBrand);
		masStoreBrand.setMasManufacturer(this);

		return masStoreBrand;
	}

	public MasStoreBrand removeMasStoreBrand(MasStoreBrand masStoreBrand) {
		getMasStoreBrands().remove(masStoreBrand);
		masStoreBrand.setMasManufacturer(null);

		return masStoreBrand;
	}

	public List<StoreItemBatchStock> getStoreItemBatchStocks() {
		return this.storeItemBatchStocks;
	}

	public void setStoreItemBatchStocks(List<StoreItemBatchStock> storeItemBatchStocks) {
		this.storeItemBatchStocks = storeItemBatchStocks;
	}

	public StoreItemBatchStock addStoreItemBatchStock(StoreItemBatchStock storeItemBatchStock) {
		getStoreItemBatchStocks().add(storeItemBatchStock);
		storeItemBatchStock.setMasManufacturer(this);

		return storeItemBatchStock;
	}

	public StoreItemBatchStock removeStoreItemBatchStock(StoreItemBatchStock storeItemBatchStock) {
		getStoreItemBatchStocks().remove(storeItemBatchStock);
		storeItemBatchStock.setMasManufacturer(null);

		return storeItemBatchStock;
	}

	public Long getSupplierTypeId() {
		return supplierTypeId;
	}

	public void setSupplierTypeId(Long supplierTypeId) {
		this.supplierTypeId = supplierTypeId;
	}

	public MasStoreSupplierType getMasStoreSupplierType() {
		return masStoreSupplierType;
	}

	public void setMasStoreSupplierType(MasStoreSupplierType masStoreSupplierType) {
		this.masStoreSupplierType = masStoreSupplierType;
	}

	public Long getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(Long lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}
	
	

}