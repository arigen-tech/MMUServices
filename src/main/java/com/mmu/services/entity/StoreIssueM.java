package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the STORE_ISSUE_M database table.
 * 
 */
@Entity
@Table(name="STORE_ISSUE_M")
@NamedQuery(name="StoreIssueM.findAll", query="SELECT s FROM StoreIssueM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_ISSUE_M_SEQ1", sequenceName="STORE_ISSUE_M_SEQ1", allocationSize=1)
public class StoreIssueM implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_ISSUE_M_SEQ1")
	private Long id;

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="INPATIENT_ID")
	private Long inpatientId;

	@Temporal(TemporalType.DATE)
	@Column(name="ISSUE_DATE")
	private Date issueDate;

	@Column(name="ISSUE_NO")
	private String issueNo;

	@Column(name="ISSUE_TYPE")
	private String issueType;

	@Column(name="ISSUED_BY")
	private Long issuedBy;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PRESCRIPTION_HD_ID")
	private Long prescriptionId;

	@Column(name="REQUEST_BY")
	private Long requestBy;

	@Temporal(TemporalType.DATE)
	@Column(name="REQUEST_DATE")
	private Date requestDate;

	@Column(name="INDENT_M_ID")
	private Long indentMId;

	private String status;

	@Column(name="TO_STORE")
	private Long toStore;


	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="FWC_HOSPITAL_ID")
	private MasHospital fwcHospitalId;
	

	//bi-directional many-to-one association to StoreIssueT
	@OneToMany(mappedBy="storeIssueM")
	private List<StoreIssueT> storeIssueTs;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INDENT_M_ID",nullable=false,insertable=false,updatable=false)
	private StoreInternalIndentM storeInternalIndentM;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ISSUED_BY",nullable=false,insertable=false,updatable=false)
	private Users userIssuedBy;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRESCRIPTION_HD_ID",nullable=false,insertable=false,updatable=false)
	private PatientPrescriptionHd patientPrescriptionHd;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="district_id",nullable=false,insertable=false,updatable=false)
	private MasDistrict masDistrict;
	
	@Column(name = "district_id")
	private Long districtId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;
	
	@Column(name = "city_id")
	private Long cityId;
	
	//indent_co_m_id
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="indent_co_m_id",nullable=false,insertable=false,updatable=false)
	private StoreCoInternalIndentM storeCoInternalIndentM;
	
	@Column(name = "indent_co_m_id")
	private Long indentCoMId;

	public StoreIssueM() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getInpatientId() {
		return this.inpatientId;
	}

	public void setInpatientId(Long inpatientId) {
		this.inpatientId = inpatientId;
	}

	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueNo() {
		return this.issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getIssueType() {
		return this.issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public Long getIssuedBy() {
		return this.issuedBy;
	}

	public void setIssuedBy(Long issuedBy) {
		this.issuedBy = issuedBy;
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

	public Long getPrescriptionId() {
		return this.prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Long getRequestBy() {
		return this.requestBy;
	}

	public void setRequestBy(Long requestBy) {
		this.requestBy = requestBy;
	}

	public Date getRequestDate() {
		return this.requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getToStore() {
		return this.toStore;
	}

	public Long getIndentMId() {
		return indentMId;
	}

	public void setIndentMId(Long indentMId) {
		this.indentMId = indentMId;
	}

	public void setToStore(Long toStore) {
		this.toStore = toStore;
	}

	public List<StoreIssueT> getStoreIssueTs() {
		return this.storeIssueTs;
	}

	public void setStoreIssueTs(List<StoreIssueT> storeIssueTs) {
		this.storeIssueTs = storeIssueTs;
	}

	public StoreIssueT addStoreIssueT(StoreIssueT storeIssueT) {
		getStoreIssueTs().add(storeIssueT);
		storeIssueT.setStoreIssueM(this);

		return storeIssueT;
	}

	public StoreIssueT removeStoreIssueT(StoreIssueT storeIssueT) {
		getStoreIssueTs().remove(storeIssueT);
		storeIssueT.setStoreIssueM(null);

		return storeIssueT;
	}

	public StoreInternalIndentM getStoreInternalIndentM() {
		return storeInternalIndentM;
	}

	public void setStoreInternalIndentM(StoreInternalIndentM storeInternalIndentM) {
		this.storeInternalIndentM = storeInternalIndentM;
	}

	public PatientPrescriptionHd getPatientPrescriptionHd() {
		return patientPrescriptionHd;
	}

	public void setPatientPrescriptionHd(PatientPrescriptionHd patientPrescriptionHd) {
		this.patientPrescriptionHd = patientPrescriptionHd;
	}

	

	public MasHospital getFwcHospitalId() {
		return fwcHospitalId;
	}

	public void setFwcHospitalId(MasHospital fwcHospitalId) {
		this.fwcHospitalId = fwcHospitalId;
	}

	public Users getUserIssuedBy() {
		return userIssuedBy;
	}

	public void setUserIssuedBy(Users userIssuedBy) {
		this.userIssuedBy = userIssuedBy;
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

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public StoreCoInternalIndentM getStoreCoInternalIndentM() {
		return storeCoInternalIndentM;
	}

	public void setStoreCoInternalIndentM(StoreCoInternalIndentM storeCoInternalIndentM) {
		this.storeCoInternalIndentM = storeCoInternalIndentM;
	}

	public Long getIndentCoMId() {
		return indentCoMId;
	}

	public void setIndentCoMId(Long indentCoMId) {
		this.indentCoMId = indentCoMId;
	}
	
}