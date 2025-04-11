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
 * The persistent class for the MAS_MEDICAL_CATEGORY database table.
 * 
 */
@Entity
@Table(name="MAS_MEDICAL_CATEGORY")
@NamedQuery(name="MasMedicalCategory.findAll", query="SELECT m FROM MasMedicalCategory m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MEDICAL_CATEGORY_MEDICALCATEGORYID_GENERATOR", sequenceName="MAS_MEDICAL_CATEGORY_SEQ", allocationSize=1)
public class MasMedicalCategory implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7865180468809685402L;

	@Id
	/*@SequenceGenerator(name="MAS_MEDICAL_CATEGORY_MEDICALCATEGORYID_GENERATOR", sequenceName="MEDICAL_CATEGORY_ID")*/
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_MEDICAL_CATEGORY_MEDICALCATEGORYID_GENERATOR")
	@Column(name="MEDICAL_CATEGORY_ID")
	private long medicalCategoryId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MEDICAL_CATEGORY_CODE")
	private Long medicalCategoryCode;

	@Column(name="MEDICAL_CATEGORY_NAME")
	private String medicalCategoryName;

	private String status;

	/*
	 * @OneToMany(mappedBy="masMedicalCategory")
	 * 
	 * @JsonBackReference private List<Patient> patients;
	 */

	public MasMedicalCategory() {
	}

	public long getMedicalCategoryId() {
		return this.medicalCategoryId;
	}

	public void setMedicalCategoryId(long medicalCategoryId) {
		this.medicalCategoryId = medicalCategoryId;
	}

	/*
	 * public BigDecimal getLastChgBy() { return this.lastChgBy; }
	 * 
	 * public void setLastChgBy(BigDecimal lastChgBy) { this.lastChgBy = lastChgBy;
	 * }
	 */

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getMedicalCategoryCode() {
		return this.medicalCategoryCode;
	}

	public void setMedicalCategoryCode(Long medicalCategoryCode) {
		this.medicalCategoryCode = medicalCategoryCode;
	}

	public String getMedicalCategoryName() {
		return this.medicalCategoryName;
	}

	public void setMedicalCategoryName(String medicalCategoryName) {
		this.medicalCategoryName = medicalCategoryName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	@Column(name="FIT_FLAG")
	private String fitFlag;

	public String getFitFlag() {
		return fitFlag;
	}

	public void setFitFlag(String fitFlag) {
		this.fitFlag = fitFlag;
	}
}