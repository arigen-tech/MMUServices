package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the DG_ORDERHD database table.
 * 
 */
@Entity
@Table(name="DG_ORDER_HD")
@NamedQuery(name="DgOrderhd.findAll", query="SELECT d FROM DgOrderhd d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="DG_ORDERHD_SEQ", sequenceName="DG_ORDERHD_SEQ", allocationSize=1)
public class DgOrderhd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2397507978933832757L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DG_ORDERHD_SEQ")
	@Column(name="ORDERHD_ID")
	private Long orderhdId;

	/*@Column(name="BILLING_STATUS")
	private String billingStatus;*/

	@Column(name="DEPARTMENT_ID")
	private Long departmentId;

	@Column(name="MMU_ID")
	private Long mmuId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;


	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	@Column(name="ORDER_DATE")
	private Date orderDate;

	@Column(name="ORDER_NO")
	private String orderNo;

	@Column(name="ORDER_STATUS")
	private String orderStatus;

	
	@Column(name="PATIENT_ID")
	private Long patientId;

	
	@Column(name="VISIT_ID")
	private Long visitId;
	
	@Column(name="DOCTOR_ID")
	private Long doctorId;
	
	@Column(name="camp_id")
	private Long campId;
		
	@OneToMany(mappedBy="dgOrderHd")
	@JsonBackReference
	private List<DgOrderdt> dgOrderDt;
	
	@OneToMany(mappedBy="dgOrderHd")
	@JsonBackReference
	private List<DgSampleCollectionHd> dgSampleCollectionHds;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "VISIT_ID",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private Visit visit;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "PATIENT_ID",nullable=false,insertable=false,updatable=false)
	private Patient patient;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "DOCTOR_ID",nullable=false,insertable=false,updatable=false)
	private Users doctorIds;	
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "DEPARTMENT_ID",nullable=false,insertable=false,updatable=false)
	private MasDepartment masDepartment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users users;
	
	public Long getOrderhdId() {
		return orderhdId;
	}

	public void setOrderhdId(Long orderhdId) {
		this.orderhdId = orderhdId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	
	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	
	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}



	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public List<DgOrderdt> getDgOrderDt() {
		return dgOrderDt;
	}

	public void setDgOrderDt(List<DgOrderdt> dgOrderDt) {
		this.dgOrderDt = dgOrderDt;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public Users getDoctorIds() {
		return doctorIds;
	}

	public void setDoctorIds(Users doctorIds) {
		this.doctorIds = doctorIds;
	}

	public List<DgSampleCollectionHd> getDgSampleCollectionHds() {
		return dgSampleCollectionHds;
	}

	public void setDgSampleCollectionHds(List<DgSampleCollectionHd> dgSampleCollectionHds) {
		this.dgSampleCollectionHds = dgSampleCollectionHds;
	}

	
 
	
	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}




	/*
	 * @ManyToOne(fetch=FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "ORDERHD_ID",referencedColumnName = "ORDERHD_ID",
	 * nullable=false,insertable=false,updatable=false) private DgResultEntryHd
	 * dgResultEntryHd;
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MMU_ID",nullable=false,insertable=false,updatable=false)
	private MasMMU masMmu;
	
	

	/*
	 * @ManyToOne(fetch=FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "ORDERHD_ID",referencedColumnName = "ORDERHD_ID",
	 * nullable=false,insertable=false,updatable=false) private DgOrderdt dgOrderdt;
	 */

}