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
@Table(name="store_do_internal_indent_m")
//@NamedQuery(name="StoreCoInternalIndentM.findAll", query="SELECT s FROM StoreCoInternalIndentM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="store_do_internal_indent_m_seq", sequenceName="store_do_internal_indent_m_seq", allocationSize=1)
public class StoreDoInternalIndentM implements Serializable {
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4704816344725110322L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="store_do_internal_indent_m_seq")
	@Column(name = "do_m_id")
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
	@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	@Column(name = "district_id")
	private Long districtId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dhmo_id",nullable=false,insertable=false,updatable=false)
	private Users dhmoUser;
	
	@Column(name = "dhmo_id")
	private Long dhmoId;
	
	@Column(name = "dhmo_flag")
	private String dhmoFlag;
	
	@Column(name = "dhmo_date")
	private Date dhmoDate;
	
	private String remarks;
	
	@Column(name = "po_no")
	private String poNo;
	
	@Column(name = "po_date")
	private Date poDate;
	
	@Column(name = "vendor_type_id")
	private Long vendorTypeId;
	
	@Column(name = "vendor_new_id")
	private Long vendorNewId;
	
	@Column(name = "rv_notes")
	private String rvNotes;
	
	@Column(name = "type_of_po")
	private String typeOfPo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vendor_type_id",nullable=false,insertable=false,updatable=false)
	private MasStoreSupplierType masStoreSupplierType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vendor_new_id",nullable=false,insertable=false,updatable=false)
	private MasStoreSupplierNew masStoreSupplierNew;

	public StoreDoInternalIndentM() {
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

	public Users getDhmoUser() {
		return dhmoUser;
	}

	public void setDhmoUser(Users dhmoUser) {
		this.dhmoUser = dhmoUser;
	}

	public Long getDhmoId() {
		return dhmoId;
	}

	public void setDhmoId(Long dhmoId) {
		this.dhmoId = dhmoId;
	}

	public String getDhmoFlag() {
		return dhmoFlag;
	}

	public void setDhmoFlag(String dhmoFlag) {
		this.dhmoFlag = dhmoFlag;
	}

	public Date getDhmoDate() {
		return dhmoDate;
	}

	public void setDhmoDate(Date dhmoDate) {
		this.dhmoDate = dhmoDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public Long getVendorTypeId() {
		return vendorTypeId;
	}

	public void setVendorTypeId(Long vendorTypeId) {
		this.vendorTypeId = vendorTypeId;
	}

	public Long getVendorNewId() {
		return vendorNewId;
	}

	public void setVendorNewId(Long vendorNewId) {
		this.vendorNewId = vendorNewId;
	}

	public String getRvNotes() {
		return rvNotes;
	}

	public void setRvNotes(String rvNotes) {
		this.rvNotes = rvNotes;
	}

	public String getTypeOfPo() {
		return typeOfPo;
	}

	public void setTypeOfPo(String typeOfPo) {
		this.typeOfPo = typeOfPo;
	}

	public MasStoreSupplierType getMasStoreSupplierType() {
		return masStoreSupplierType;
	}

	public void setMasStoreSupplierType(MasStoreSupplierType masStoreSupplierType) {
		this.masStoreSupplierType = masStoreSupplierType;
	}

	public MasStoreSupplierNew getMasStoreSupplierNew() {
		return masStoreSupplierNew;
	}

	public void setMasStoreSupplierNew(MasStoreSupplierNew masStoreSupplierNew) {
		this.masStoreSupplierNew = masStoreSupplierNew;
	}

}