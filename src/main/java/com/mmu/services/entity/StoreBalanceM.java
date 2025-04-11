package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the STORE_BALANCE_M database table.
 * 
 */
@Entity

@NamedQuery(name="StoreBalanceM.findAll", query="SELECT s FROM StoreBalanceM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_BALANCE_M_ID_GENERATOR", sequenceName="STORE_BALANCE_M_SEQ", allocationSize=1)
@Table(name="STORE_BALANCE_M")
public class StoreBalanceM implements Serializable {
	

	private static final long serialVersionUID = -4803304667302668682L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_BALANCE_M_ID_GENERATOR")
	private long id;


	@Temporal(TemporalType.DATE)
	@Column(name="BALANCE_DATE")
	private Date balanceDate;

	@Column(name="BALANCE_NO")
	private String balanceNo;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String remarks;

	private String status;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUBMITTED_BY")
	private Users submittedBy;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPROVED_BY")
	private Users approvedBy;

	
	@OneToMany(mappedBy="storeBalanceM")
	@JsonBackReference
	private List<StoreBalanceT> storeBalanceTs;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@Column(name = "district_id")
	private Long districtId;

	public StoreBalanceM() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public Users getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public String getBalanceNo() {
		return this.balanceNo;
	}

	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
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

	
	public Users getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(Users submittedBy) {
		this.submittedBy = submittedBy;
	}

	public MasDepartment getMasDepartment() {
		return this.masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
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

	public List<StoreBalanceT> getStoreBalanceTs() {
		return this.storeBalanceTs;
	}

	public void setStoreBalanceTs(List<StoreBalanceT> storeBalanceTs) {
		this.storeBalanceTs = storeBalanceTs;
	}

	public StoreBalanceT addStoreBalanceT(StoreBalanceT storeBalanceT) {
		getStoreBalanceTs().add(storeBalanceT);
		storeBalanceT.setStoreBalanceM(this);

		return storeBalanceT;
	}

	public StoreBalanceT removeStoreBalanceT(StoreBalanceT storeBalanceT) {
		getStoreBalanceTs().remove(storeBalanceT);
		storeBalanceT.setStoreBalanceM(null);

		return storeBalanceT;
	}

	public MasMMU getMasMMU() {
		return masMMU;
	}

	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
	@Column(name="invoice_no")
	private String invoiceNo;
	
	@Column(name="file_name")
	private String fileName;

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}