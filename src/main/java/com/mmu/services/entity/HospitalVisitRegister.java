package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

/**
 * The persistent class for the HOSPITAL_VISIT_REGISTER database table.
 * 
 */
@Entity
@Table(name = "HOSPITAL_VISIT_REGISTER")
@NamedQuery(name = "HospitalVisitRegister.findAll", query = "SELECT h FROM HospitalVisitRegister h")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@SequenceGenerator(name = "HOSPITAL_VISIT_REGISTER_SEQ", sequenceName = "HOSPITAL_VISIT_REGISTER_SEQ", allocationSize = 1)
public class HospitalVisitRegister implements Serializable {
	private static final long serialVersionUID = -81141547352681553L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "HOSPITAL_VISIT_REGISTER_SEQ")
	@Column(name = "HOSTPITAL_VISIT_REGISTER_ID")
	private long hostpitalVisitRegisterId;

	@Column(name = "CAPTAIN_REMARK")
	private String captainRemark;

	@Column(name = "HOSPITAL_NAME")
	private String hospitalName;

	@Temporal(TemporalType.DATE)
	@Column(name = "HOSPITAL_VISIT_DATE")
	private Date hospitalVisitDate;

	@Column(name = "LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String remarks;

	@Column(name = "VISIT_BY_MED_DEPT")
	private String visitByMedDept;

	@Column(name = "VISIT_REMARKS_DIV_OFFICER")
	private String visitRemarksDivOfficer;

	@Column(name = "WARD_NAME")
	private String wardName;
	
	@Column(name = "ICD_DIAGNOSIS")
	private String icdDiagnosis;

	// bi-directional many-to-one association to MasHospital
	@ManyToOne
	@JoinColumn(name = "HOSPITAL_ID")
	private MasHospital masHospital;

	/*
	 * // bi-directional many-to-one association to MasIcd
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "ICD_ID") private MasIcd masIcd;
	 */

	// bi-directional many-to-one association to Patient
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "LAST_CHG_BY")
	private Users lastChgBy;

	

	@OneToMany(mappedBy="hospitalVisitRegister")
	@JsonBackReference
	private List<VisitorDetail> visitorDetail;

	public List<VisitorDetail> getVisitorDetail() {
		return visitorDetail;
	}

	public void setVisitorDetail(List<VisitorDetail> visitorDetail) {
		this.visitorDetail = visitorDetail;
	}

	public HospitalVisitRegister() {
	}

	public long getHostpitalVisitRegisterId() {
		return this.hostpitalVisitRegisterId;
	}

	public void setHostpitalVisitRegisterId(long hostpitalVisitRegisterId) {
		this.hostpitalVisitRegisterId = hostpitalVisitRegisterId;
	}

	public String getCaptainRemark() {
		return this.captainRemark;
	}

	public void setCaptainRemark(String captainRemark) {
		this.captainRemark = captainRemark;
	}

	public String getHospitalName() {
		return this.hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Date getHospitalVisitDate() {
		return this.hospitalVisitDate;
	}

	public void setHospitalVisitDate(Date hospitalVisitDate) {
		this.hospitalVisitDate = hospitalVisitDate;
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

	public String getVisitByMedDept() {
		return this.visitByMedDept;
	}

	public void setVisitByMedDept(String visitByMedDept) {
		this.visitByMedDept = visitByMedDept;
	}

	public String getVisitRemarksDivOfficer() {
		return this.visitRemarksDivOfficer;
	}

	public void setVisitRemarksDivOfficer(String visitRemarksDivOfficer) {
		this.visitRemarksDivOfficer = visitRemarksDivOfficer;
	}

	public String getWardName() {
		return this.wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	

	public String getIcdDiagnosis() {
		return icdDiagnosis;
	}

	public void setIcdDiagnosis(String icdDiagnosis) {
		this.icdDiagnosis = icdDiagnosis;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

}