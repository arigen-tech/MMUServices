package com.mmu.services.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_EMPLOYEE_CATEGORY database table.
 * 
 */
@Entity
@Immutable
@Table(name="VU_MAS_EMPLOYEE_CATEGORY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MasEmployeeCategory implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -750747784116611024L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="EMPLOYEE_CATEGORY_ID")
	private Long employeeCategoryId;

	@Column(name="EMPLOYEE_CATEGORY_CODE")
	private String employeeCategoryCode;

	@Column(name="EMPLOYEE_CATEGORY_NAME")
	private String employeeCategoryName;
	

	@OneToMany(mappedBy="masEmployeeCategory")
	@JsonBackReference
	private List<MasRank> masRanks;
	
	
	@OneToMany(mappedBy="masEmployeeCategory")
	@JsonBackReference
	private List<MasEmployee> masEmployee;


	public MasEmployeeCategory() {
	}

	public Long getEmployeeCategoryId() {
		return this.employeeCategoryId;
	}

	public void setEmployeeCategoryId(Long employeeCategoryId) {
		this.employeeCategoryId = employeeCategoryId;
	}

	public String getEmployeeCategoryCode() {
		return this.employeeCategoryCode;
	}

	public void setEmployeeCategoryCode(String employeeCategoryCode) {
		this.employeeCategoryCode = employeeCategoryCode;
	}

	public String getEmployeeCategoryName() {
		return this.employeeCategoryName;
	}

	public void setEmployeeCategoryName(String employeeCategoryName) {
		this.employeeCategoryName = employeeCategoryName;
	}

	
	public List<MasRank> getMasRanks() {
		return this.masRanks;
	}

	public void setMasRanks(List<MasRank> masRanks) {
		this.masRanks = masRanks;
	}


}