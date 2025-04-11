package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.math.BigDecimal;


/**
 * The persistent class for the VU_PATIENT_MEDICAL_CAT database table.
 * 
 */
@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="VU_PATIENT_MEDICAL_CAT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class VuPatientMedicalCat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", updatable = false, nullable = false)
	private long Id;
	
	@Column(name="ICD_NAME")
	private String icdName;

	
	 @Column(name="MEDICAL_CATEGORY_ID") 
	 private long medicalCategoryId;
	 /* @Column(name="PATIENT_ID") private long patientId;
	 * 
	 * @Column(name="VISIT_ID") private long visitId;
	 */
	
	// bi-directional many-to-one association to Visit
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "VISIT_ID")
		private Visit visit;

		
		@ManyToOne( fetch = FetchType.LAZY)
		//@JoinColumn(name = "MEDICAL_CATEGORY_ID")
		@JoinColumn(name="MEDICAL_CATEGORY_ID",nullable=false,insertable=false,updatable=false)
		private MasMedicalCategory masMedicalCategory;
		
		
		public long getMedicalCategoryId() {
			return medicalCategoryId;
		}

		public void setMedicalCategoryId(long medicalCategoryId) {
			this.medicalCategoryId = medicalCategoryId;
		}

			// bi-directional many-to-one association to Visit
			@ManyToOne( fetch = FetchType.LAZY)
			@JoinColumn(name = "PATIENT_ID")
			private Patient patient;

	public VuPatientMedicalCat() {
	}

	public String getIcdName() {
		return this.icdName;
	}

	
	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public MasMedicalCategory getMasMedicalCategory() {
		return masMedicalCategory;
	}

	public void setMasMedicalCategory(MasMedicalCategory masMedicalCategory) {
		this.masMedicalCategory = masMedicalCategory;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}
	

}