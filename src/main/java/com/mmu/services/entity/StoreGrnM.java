package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the STORE_GRN_M database table.
 * 
 */
@Entity
@Table(name="STORE_GRN_M")
@NamedQuery(name="StoreGrnM.findAll", query="SELECT s FROM StoreGrnM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_GRN_M_GENERATOR", sequenceName="STORE_GRN_M_SEQ",allocationSize=1)
public class StoreGrnM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -454273394742277491L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_GRN_M_GENERATOR")
	@Column(name="GRN_M_ID")
	private long grnMId;

	@Column(name="GRN_DATE")
	private Timestamp grnDate;

	@Column(name="GRN_NO")
	private String grnNo;

	@Column(name="GRN_STATUS")
	private String grnStatus;

	@Column(name="INVOICE_AMOUNT")
	private BigDecimal invoiceAmount;

	@Temporal(TemporalType.DATE)
	@Column(name="INVOICE_DATE")
	private Date invoiceDate;

	@Column(name="INVOICE_NO")
	private String invoiceNo;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="RECEIVE_TYPE")
	private String receiveType;

	private String remarks;

	private String status;
	
	@Column(name="LP_TYPE_FLAG")
	private String lpTypeFlag;

/*
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INDENT_ID")
	private StoreIndentM storeIndentM;
*/	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PO_M_ID")
	private StorePoM storePoM;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CREATED_BY")
	private Users createdBy;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_ID")
	private MasStoreSupplier masStoreSupplier;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;
	
	
	@OneToMany(mappedBy="storeGrnM")
	@JsonBackReference
	private List<StoreGrnT> storeGrnTs;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="store_do_hd_id",nullable=false,insertable=false,updatable=false)
	private StoreDoInternalIndentM storeDoInternalIndentM1;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	@Column(name = "store_do_hd_id")
	private Long storeDoHdId;
	
	@Column(name = "district_id")
	private Long districtId;

	public StoreGrnM() {
	}

	public long getGrnMId() {
		return this.grnMId;
	}

	public void setGrnMId(long grnMId) {
		this.grnMId = grnMId;
	}

	
	public Timestamp getGrnDate() {
		return this.grnDate;
	}

	public void setGrnDate(Timestamp grnDate) {
		this.grnDate = grnDate;
	}

	public String getGrnNo() {
		return this.grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public String getGrnStatus() {
		return this.grnStatus;
	}

	public void setGrnStatus(String grnStatus) {
		this.grnStatus = grnStatus;
	}

	

	public BigDecimal getInvoiceAmount() {
		return this.invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getReceiveType() {
		return this.receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public Users getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Users createdBy) {
		this.createdBy = createdBy;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public StorePoM getStorePoM() {
		return this.storePoM;
	}

	public void setStorePoM(StorePoM storePoM) {
		this.storePoM = storePoM;
	}

	public List<StoreGrnT> getStoreGrnTs() {
		return this.storeGrnTs;
	}

	public void setStoreGrnTs(List<StoreGrnT> storeGrnTs) {
		this.storeGrnTs = storeGrnTs;
	}
	
	

	public MasStoreSupplier getMasStoreSupplier() {
		return masStoreSupplier;
	}

	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}

	public StoreGrnT addStoreGrnT(StoreGrnT storeGrnT) {
		getStoreGrnTs().add(storeGrnT);
		storeGrnT.setStoreGrnM(this);

		return storeGrnT;
	}

	public StoreGrnT removeStoreGrnT(StoreGrnT storeGrnT) {
		getStoreGrnTs().remove(storeGrnT);
		storeGrnT.setStoreGrnM(null);

		return storeGrnT;
	}

	public String getLpTypeFlag() {
		return lpTypeFlag;
	}

	public void setLpTypeFlag(String lpTypeFlag) {
		this.lpTypeFlag = lpTypeFlag;
	}

	public StoreDoInternalIndentM getStoreDoInternalIndentM1() {
		return storeDoInternalIndentM1;
	}

	public void setStoreDoInternalIndentM1(StoreDoInternalIndentM storeDoInternalIndentM1) {
		this.storeDoInternalIndentM1 = storeDoInternalIndentM1;
	}

	public MasDistrict getMasDistrict() {
		return masDistrict;
	}

	public void setMasDistrict(MasDistrict masDistrict) {
		this.masDistrict = masDistrict;
	}

	public Long getStoreDoHdId() {
		return storeDoHdId;
	}

	public void setStoreDoHdId(Long storeDoHdId) {
		this.storeDoHdId = storeDoHdId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

}