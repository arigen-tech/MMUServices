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
@Table(name="STORE_INTERNAL_INDENT_M")
@NamedQuery(name="StoreInternalIndentM.findAll", query="SELECT s FROM StoreInternalIndentM s")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="STORE_INTERNAL_INDENT_M_SEQ", sequenceName="STORE_INTERNAL_INDENT_M_SEQ", allocationSize=1)
public class StoreInternalIndentM implements Serializable {
	 
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4704816344725110322L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="STORE_INTERNAL_INDENT_M_SEQ")
	private Long id;

	@Column(name="APPROVAL_DATE")
	private Timestamp approvalDate;

	@Column(name="APPROVAL_REMARKS")
	private String approvalRemarks;

	@Column(name="DEMAND_DATE")
	private Timestamp demandDate;

	@Column(name="DEMAND_NO")
	private String demandNo;


	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="RECEIVED_DATE")
	private Timestamp receivedDate;

	private String status;


	@ManyToOne(fetch=FetchType.LAZY)
   @JsonBackReference
	@JoinColumn(name="REQUESTED_BY")
	private Users users4;

	@ManyToOne(fetch=FetchType.LAZY)
	  @JsonBackReference
	@JoinColumn(name="APPROVED_BY")
	private Users user3;
	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	  @JsonBackReference
	@JoinColumn(name="DEPARTMENT_ID",nullable=false,insertable=false,updatable=false)
	private MasDepartment department1;
		
		
	@ManyToOne(fetch=FetchType.LAZY)
	  @JsonBackReference
	@JoinColumn(name="TO_STORE")
	private MasDepartment department2;
		

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="FWC_PARENT_HOSPITAL_ID")
	private MasHospital fwcParentMasHospital;


	@ManyToOne(fetch=FetchType.LAZY)
	  @JsonBackReference
	@JoinColumn(name="LAST_CHG_BY")
	private Users user1;

	@ManyToOne(fetch=FetchType.LAZY)
	  @JsonBackReference
	@JoinColumn(name="CREATED_BY")
	private Users user2;

	@OneToMany(mappedBy="storeInternalIndentM1")
	@JsonBackReference
	private List<StoreInternalIndentT> storeInternalIndentTs;

	@Column(name = "city_id")
	private Long cityId;
	
	@Column(name = "mmu_id")
	private Long mmuId;
	
	@Column(name = "department_id")
	private Long departmentId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
	private MasMMU masMMU;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="auditor_id",nullable=false,insertable=false,updatable=false)
	private Users auditorUser;
	
	@Column(name = "auditor_id")
	private Long auditorId;
	
	@Column(name = "auditor_flag")
	private String auditorFlag;
	
	@Column(name = "auditor_date")
	private Date auditorDate;
	
	@Column(name = "auditor_remarks")
	private String auditorRemarks;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="apm_id",nullable=false,insertable=false,updatable=false)
	private Users apmUser;
	
	@Column(name = "apm_id")
	private Long apmId;
	
	@Column(name = "apm_flag")
	private String apmFlag;
	
	@Column(name = "apm_date")
	private Date apmDate;
	
	@Column(name = "apm_remarks")
	private String apmRemarks;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="chmo_id",nullable=false,insertable=false,updatable=false)
	private Users coUser;
	
	
	@Column(name = "chmo_id")
	private Long coId;
	
	@Column(name = "chmo_flag")
	private String coFlag;
	
	@Column(name = "chmo_date")
	private Date coDate;
	
	@Column(name = "chmo_remarks")
	private String coRemarks;

	public StoreInternalIndentM() {
	}

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Timestamp getApprovalDate() {
		return this.approvalDate;
	}

	public void setApprovalDate(Timestamp approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalRemarks() {
		return this.approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public Timestamp getDemandDate() {
		return this.demandDate;
	}

	public void setDemandDate(Timestamp demandDate) {
		this.demandDate = demandDate;
	}

	public String getDemandNo() {
		return this.demandNo;
	}

	public void setDemandNo(String demandNo) {
		this.demandNo = demandNo;
	}


	/*
	 * public String getDispensaryLp() { return this.dispensaryLp; }
	 * 
	 * public void setDispensaryLp(String dispensaryLp) { this.dispensaryLp =
	 * dispensaryLp; }
	 */

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


	public Users getUser3() {
		return user3;
	}

	public Users getUsers4() {
		return users4;
	}

	public void setUsers4(Users users4) {
		this.users4 = users4;
	}

	public MasDepartment getDepartment1() {
		return department1;
	}

	public void setDepartment1(MasDepartment department1) {
		this.department1 = department1;
	}

	public MasDepartment getDepartment2() {
		return department2;
	}

	public void setDepartment2(MasDepartment department2) {
		this.department2 = department2;
	}

	public void setUser3(Users user3) {
		this.user3 = user3;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public Users getUser1() {
		return this.user1;
	}

	public void setUser1(Users user1) {
		this.user1 = user1;
	}

	public Users getUser2() {
		return this.user2;
	}

	public void setUser2(Users user2) {
		this.user2 = user2;
	}

	public List<StoreInternalIndentT> getStoreInternalIndentTs() {
		return this.storeInternalIndentTs;
	}

	public void setStoreInternalIndentTs(List<StoreInternalIndentT> storeInternalIndentTs) {
		this.storeInternalIndentTs = storeInternalIndentTs;
	}

	public StoreInternalIndentT addStoreInternalIndentT(StoreInternalIndentT storeInternalIndentT) {
		getStoreInternalIndentTs().add(storeInternalIndentT);
		storeInternalIndentT.setStoreInternalIndentM1(this);

		return storeInternalIndentT;
	}

	public StoreInternalIndentT removeStoreInternalIndentT(StoreInternalIndentT storeInternalIndentT) {
		getStoreInternalIndentTs().remove(storeInternalIndentT);
		storeInternalIndentT.setStoreInternalIndentM1(null);

		return storeInternalIndentT;
	}


	public Timestamp getReceivedDate() {
		return receivedDate;
	}


	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}


	public MasHospital getFwcParentMasHospital() {
		return fwcParentMasHospital;
	}


	public void setFwcParentMasHospital(MasHospital fwcParentMasHospital) {
		this.fwcParentMasHospital = fwcParentMasHospital;
	}


	public Long getCityId() {
		return cityId;
	}


	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}


	public Long getMmuId() {
		return mmuId;
	}


	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}


	public Long getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}


	public MasMMU getMasMMU() {
		return masMMU;
	}


	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}


	public Long getAuditorId() {
		return auditorId;
	}


	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}


	public String getAuditorFlag() {
		return auditorFlag;
	}


	public void setAuditorFlag(String auditorFlag) {
		this.auditorFlag = auditorFlag;
	}


	public Date getAuditorDate() {
		return auditorDate;
	}


	public void setAuditorDate(Date auditorDate) {
		this.auditorDate = auditorDate;
	}


	public String getAuditorRemarks() {
		return auditorRemarks;
	}


	public void setAuditorRemarks(String auditorRemarks) {
		this.auditorRemarks = auditorRemarks;
	}


	public Long getApmId() {
		return apmId;
	}


	public void setApmId(Long apmId) {
		this.apmId = apmId;
	}


	public String getApmFlag() {
		return apmFlag;
	}


	public void setApmFlag(String apmFlag) {
		this.apmFlag = apmFlag;
	}


	public Date getApmDate() {
		return apmDate;
	}


	public void setApmDate(Date apmDate) {
		this.apmDate = apmDate;
	}


	public String getApmRemarks() {
		return apmRemarks;
	}


	public void setApmRemarks(String apmRemarks) {
		this.apmRemarks = apmRemarks;
	}


	public Long getCoId() {
		return coId;
	}


	public void setCoId(Long coId) {
		this.coId = coId;
	}


	public String getCoFlag() {
		return coFlag;
	}


	public void setCoFlag(String coFlag) {
		this.coFlag = coFlag;
	}


	public Date getCoDate() {
		return coDate;
	}


	public void setCoDate(Date coDate) {
		this.coDate = coDate;
	}


	public String getCoRemarks() {
		return coRemarks;
	}


	public void setCoRemarks(String coRemarks) {
		this.coRemarks = coRemarks;
	}


	public Users getAuditorUser() {
		return auditorUser;
	}


	public void setAuditorUser(Users auditorUser) {
		this.auditorUser = auditorUser;
	}


	public Users getCoUser() {
		return coUser;
	}


	public void setCoUser(Users coUser) {
		this.coUser = coUser;
	}


	public Users getApmUser() {
		return apmUser;
	}


	public void setApmUser(Users apmUser) {
		this.apmUser = apmUser;
	}
	
}