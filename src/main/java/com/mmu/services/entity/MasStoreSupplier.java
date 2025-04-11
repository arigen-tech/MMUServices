package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * The persistent class for the MAS_STORE_SUPPLIER database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_SUPPLIER")
@NamedQuery(name="MasStoreSupplier.findAll", query="SELECT m FROM MasStoreSupplier m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_STORE_SUPPLIER_SEQ", sequenceName="MAS_STORE_SUPPLIER_SEQ",allocationSize=1)

public class MasStoreSupplier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_STORE_SUPPLIER_SEQ")
	@Column(name="SUPPLIER_ID")
	private long supplierId;

	private String address1;

	private String address2;

	@Column(name="CF_LOCAL_DISTRIBUTOR_ADDRESS1")
	private String cfLocalDistributorAddress1;

	@Column(name="CF_LOCAL_DISTRIBUTOR_ADDRESS2")
	private String cfLocalDistributorAddress2;

	@Column(name="CF_LOCAL_DISTRIBUTOR_NAME")
	private String cfLocalDistributorName;

	private String emailid;

	private String faxnumber;

	@Column(name="FDR_RECEIPT_ATTACHED")
	private String fdrReceiptAttached;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="LICENCE_NO")
	private String licenceNo;

	@Column(name="LOCAL_ADDRESS1")
	private String localAddress1;

	@Column(name="LOCAL_ADDRESS2")
	private String localAddress2;

	@Column(name="LOCAL_PHONE_NO")
	private String localPhoneNo;

	@Column(name="LOCAL_PIN_CODE")
	private Long localPinCode;

	private String mobileno;

	private String phoneno;

	@Column(name="PIN_CODE")
	private Long pinCode;

	@Column(name="PIN_NO")
	private String pinNo;

	@Column(name="SALES_TAX_NO")
	private String salesTaxNo;

	private String status;

	@Column(name="SUPPLIER_CODE")
	private String supplierCode;

	@Column(name="SUPPLIER_NAME")
	private String supplierName;

	@Column(name="TIN_NO")
	private String tinNo;

	@Column(name="TYPE_OF_REG")
	private String typeOfReg;

	private String url;
	
	@Column(name="PAN_NUMBER")
	private String panNumber;
	
	@Column(name="BANK_ID")
	private Long bankId;

	@Column(name="BENEFICIARY_NAME")
	private String beneficiaryName;

	@Column(name="BRANCH_ADDRESS")
	private String branchAddress;
	
	@Column(name="IFS_CODE")
	private String ifsCode;

	@Column(name="BRANCH_CODE")
	private Long branchCode;
	
	
	@Column(name="MICR_CODE")
	private Long micrCode;
	
	@Column(name="ACCOUNT_ID")
	private Long accountId;

	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	//bi-directional many-to-one association to StoreQuotationM
	@OneToMany(mappedBy="masStoreSupplier")
	@JsonBackReference
	private List<StoreQuotationM> storeQuotationMs;

	//bi-directional many-to-one association to StoreQuotationT
	@OneToMany(mappedBy="masStoreSupplier")
	@JsonBackReference
	private List<StoreQuotationT> storeQuotationTs;

	//bi-directional many-to-one association to MasDistrict
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CITY")
	private MasDistrict city;

	//bi-directional many-to-one association to MasDistrict
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LOCAL_CITY")
	private MasDistrict localCity;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to MasState
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LOCAL_STATE")
	private MasState masState;

	//bi-directional many-to-one association to MasState
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STATE")
	private MasState state;
	
	

	//bi-directional many-to-one association to MasStoreSupplierType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUPPLIER_TYPE_ID",nullable=false, insertable=false, updatable=false)
	private MasStoreSupplierType masStoreSupplierType;

	public MasStoreSupplier() {
	}

	public long getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
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

	public String getEmailid() {
		return this.emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getFaxnumber() {
		return this.faxnumber;
	}

	public void setFaxnumber(String faxnumber) {
		this.faxnumber = faxnumber;
	}

	public String getFdrReceiptAttached() {
		return this.fdrReceiptAttached;
	}

	public void setFdrReceiptAttached(String fdrReceiptAttached) {
		this.fdrReceiptAttached = fdrReceiptAttached;
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

	public String getLocalAddress1() {
		return this.localAddress1;
	}

	public void setLocalAddress1(String localAddress1) {
		this.localAddress1 = localAddress1;
	}

	public String getLocalAddress2() {
		return this.localAddress2;
	}

	public void setLocalAddress2(String localAddress2) {
		this.localAddress2 = localAddress2;
	}

	public String getLocalPhoneNo() {
		return this.localPhoneNo;
	}

	public void setLocalPhoneNo(String localPhoneNo) {
		this.localPhoneNo = localPhoneNo;
	}

	public Long getLocalPinCode() {
		return this.localPinCode;
	}

	public void setLocalPinCode(Long localPinCode) {
		this.localPinCode = localPinCode;
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

	public String getPinNo() {
		return this.pinNo;
	}

	public void setPinNo(String pinNo) {
		this.pinNo = pinNo;
	}

	public String getSalesTaxNo() {
		return this.salesTaxNo;
	}

	public void setSalesTaxNo(String salesTaxNo) {
		this.salesTaxNo = salesTaxNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getTinNo() {
		return this.tinNo;
	}

	public void setTinNo(String tinNo) {
		this.tinNo = tinNo;
	}

	public String getTypeOfReg() {
		return this.typeOfReg;
	}

	public void setTypeOfReg(String typeOfReg) {
		this.typeOfReg = typeOfReg;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	

	public List<StoreQuotationM> getStoreQuotationMs() {
		return this.storeQuotationMs;
	}

	public void setStoreQuotationMs(List<StoreQuotationM> storeQuotationMs) {
		this.storeQuotationMs = storeQuotationMs;
	}

	public StoreQuotationM addStoreQuotationM(StoreQuotationM storeQuotationM) {
		getStoreQuotationMs().add(storeQuotationM);
		storeQuotationM.setMasStoreSupplier(this);

		return storeQuotationM;
	}

	public StoreQuotationM removeStoreQuotationM(StoreQuotationM storeQuotationM) {
		getStoreQuotationMs().remove(storeQuotationM);
		storeQuotationM.setMasStoreSupplier(null);

		return storeQuotationM;
	}

	public List<StoreQuotationT> getStoreQuotationTs() {
		return this.storeQuotationTs;
	}

	public void setStoreQuotationTs(List<StoreQuotationT> storeQuotationTs) {
		this.storeQuotationTs = storeQuotationTs;
	}

	public StoreQuotationT addStoreQuotationT(StoreQuotationT storeQuotationT) {
		getStoreQuotationTs().add(storeQuotationT);
		storeQuotationT.setMasStoreSupplier(this);

		return storeQuotationT;
	}

	public StoreQuotationT removeStoreQuotationT(StoreQuotationT storeQuotationT) {
		getStoreQuotationTs().remove(storeQuotationT);
		storeQuotationT.setMasStoreSupplier(null);

		return storeQuotationT;
	}



	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	

	public MasStoreSupplierType getMasStoreSupplierType() {
		return this.masStoreSupplierType;
	}

	public void setMasStoreSupplierType(MasStoreSupplierType masStoreSupplierType) {
		this.masStoreSupplierType = masStoreSupplierType;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public MasDistrict getCity() {
		return city;
	}

	public void setCity(MasDistrict city) {
		this.city = city;
	}

	public MasDistrict getLocalCity() {
		return localCity;
	}

	public void setLocalCity(MasDistrict localCity) {
		this.localCity = localCity;
	}

	public MasState getMasState() {
		return masState;
	}

	public void setMasState(MasState masState) {
		this.masState = masState;
	}

	public MasState getState() {
		return state;
	}

	public void setState(MasState state) {
		this.state = state;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getIfsCode() {
		return ifsCode;
	}

	public void setIfsCode(String ifsCode) {
		this.ifsCode = ifsCode;
	}

	public Long getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(Long branchCode) {
		this.branchCode = branchCode;
	}

	public Long getMicrCode() {
		return micrCode;
	}

	public void setMicrCode(Long micrCode) {
		this.micrCode = micrCode;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name="SUPPLIER_TYPE_ID")
	private Long supplierTypeId;

	public Long getSupplierTypeId() {
		return supplierTypeId;
	}

	public void setSupplierTypeId(Long supplierTypeId) {
		this.supplierTypeId = supplierTypeId;
	}
	
	

	
	
	
}