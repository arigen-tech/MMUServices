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
 * The persistent class for the MAS_DEPARTMENT database table.
 * 
 */
@Entity
@Table(name="MAS_DEPARTMENT")
@NamedQuery(name="MasDepartment.findAll", query="SELECT m FROM MasDepartment m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "MAS_DEPARTMENT_DEPARTMENTID_GENERATOR", sequenceName = "MAS_DEPARTMENT_SEQ", allocationSize = 1)
public class MasDepartment implements Serializable {
	private static final long serialVersionUID = 3425968115571952110L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_DEPARTMENT_DEPARTMENTID_GENERATOR")
	@Column(name = "DEPARTMENT_ID")
	private Long departmentId;


	@Column(name="DEPARTMENT_CODE")
	private String departmentCode;

	@Column(name="DEPARTMENT_NAME")
	private String departmentName;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENT_TYPE_ID")
	private MasDepartmentType masDepartmentType;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="STATUS")
	private String status;
	
	@OneToMany(mappedBy="masDepartment")
	@JsonBackReference
	private List<Visit> visit;

	@OneToMany(mappedBy = "masDepartment")
	@JsonBackReference
	private List<AppSetup> appSetups;
	
	@OneToMany(mappedBy="masDepartment")
	@JsonBackReference
	public List<ReferralPatientDt> referralPatientDts;
	
	//bi-directional many-to-one association to DoctorRoaster
	@OneToMany(mappedBy="masDepartment")
	@JsonBackReference
	private List<DoctorRoaster> doctorRoasters;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	/*
	 * @OneToMany(cascade=CascadeType.ALL,mappedBy="masDepartment")
	 * 
	 * @JsonBackReference private List<MasEmployee> masEmployees;
	 */

	@OneToMany(mappedBy="masDepartment")
	@JsonBackReference
	private List<MasTemplate> masTemplates; 
	
	public MasDepartment() {
	}

	public Long getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}



	public String getDepartmentCode() {
		return this.departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}



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

	public List<DoctorRoaster> getDoctorRoasters() {
		return this.doctorRoasters;
	}

	public void setDoctorRoasters(List<DoctorRoaster> doctorRoasters) {
		this.doctorRoasters = doctorRoasters;
	}

	public DoctorRoaster addDoctorRoaster(DoctorRoaster doctorRoaster) {
		getDoctorRoasters().add(doctorRoaster);
		doctorRoaster.setMasDepartment(this);

		return doctorRoaster;
	}

	public DoctorRoaster removeDoctorRoaster(DoctorRoaster doctorRoaster) {
		getDoctorRoasters().remove(doctorRoaster);
		doctorRoaster.setMasDepartment(null);

		return doctorRoaster;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	/*
	 * public List<MasEmployee> getMasEmployees() { return this.masEmployees; }
	 * 
	 * public void setMasEmployees(List<MasEmployee> masEmployees) {
	 * this.masEmployees = masEmployees; }
	 */
	

	public MasDepartmentType getMasDepartmentType() {
		return this.masDepartmentType;
	}

	public void setMasDepartmentType(MasDepartmentType masDepartmentType) {
		this.masDepartmentType = masDepartmentType;
	}

	public List<AppSetup> getAppSetups() {
		return appSetups;
	}

	public void setAppSetups(List<AppSetup> appSetups) {
		this.appSetups = appSetups;
	}

	public List<Visit> getVisit() {
		return visit;
	}

	public void setVisit(List<Visit> visit) {
		this.visit = visit;
	}
	
	public List<ReferralPatientDt> getReferralPatientDts() {
		return referralPatientDts;
	}

	public void setReferralPatientDts(List<ReferralPatientDt> referralPatientDts) {
		this.referralPatientDts = referralPatientDts;
	}

	public List<MasTemplate> getMasTemplates() {
		return masTemplates;
	}

	public void setMasTemplates(List<MasTemplate> masTemplates) {
		this.masTemplates = masTemplates;
	}

	
}