package com.mmu.services.entity;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the MAS_DEPARTMENT_TYPE database table.
 * 
 */
@Entity
@Table(name = "MAS_DEPARTMENT_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "MAS_DEPARTMENT_TYPE_DEPARTMENTTYPEID_GENERATOR", sequenceName = "MAS_DEPARTMENT_TYPE_SEQ", allocationSize = 1)
public class MasDepartmentType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2855444151706468461L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_DEPARTMENT_TYPE_DEPARTMENTTYPEID_GENERATOR")
	@Column(name = "DEPARTMENT_TYPE_ID")
	private long departmentTypeId;

	@Column(name = "DEPARTMENT_TYPE_CODE")
	private String departmentTypeCode;

	@Column(name = "DEPARTMENT_TYPE_NAME")
	private String departmentTypeName;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_CHG_DATE")
	private Date lastChgDate;

	@Column(name = "STATUS")
	private String status;

	@OneToMany(mappedBy = "masDepartmentType")
	@JsonBackReference
	private List<MasDepartment> masDepartments;

	/*
	 * @ManyToOne(fetch=FetchType.LAZY)
	 * 
	 * @JoinColumn(name="LAST_CHG_BY") private Users user;
	 */

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;

	public MasDepartmentType() {
	}

	public long getDepartmentTypeId() {
		return this.departmentTypeId;
	}

	public void setDepartmentTypeId(long departmentTypeId) {
		this.departmentTypeId = departmentTypeId;
	}

	public String getDepartmentTypeCode() {
		return this.departmentTypeCode;
	}

	public void setDepartmentTypeCode(String departmentTypeCode) {
		this.departmentTypeCode = departmentTypeCode;
	}

	public String getDepartmentTypeName() {
		return this.departmentTypeName;
	}

	public void setDepartmentTypeName(String departmentTypeName) {
		this.departmentTypeName = departmentTypeName;
	}

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<MasDepartment> getMasDepartments() {
		return this.masDepartments;
	}

	public void setMasDepartments(List<MasDepartment> masDepartments) {
		this.masDepartments = masDepartments;
	}

	public MasDepartment addMasDepartment(MasDepartment masDepartment) {
		getMasDepartments().add(masDepartment);
		masDepartment.setMasDepartmentType(this);

		return masDepartment;
	}

	public MasDepartment removeMasDepartment(MasDepartment masDepartment) {
		getMasDepartments().remove(masDepartment);
		masDepartment.setMasDepartmentType(null);

		return masDepartment;
	}
	
	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	/*
	 * public Users getUser() { return this.user; }
	 * 
	 * public void setUser(Users user) { this.user = user; }
	 */

}