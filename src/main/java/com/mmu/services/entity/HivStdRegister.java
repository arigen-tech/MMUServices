package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the HIV_STD_REGISTER database table.
 * 
 */
@Entity
@Table(name="HIV_STD_REGISTER")
@NamedQuery(name="HivStdRegister.findAll", query="SELECT h FROM HivStdRegister h")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="HIV_STD_REGISTER_SEQ", sequenceName="HIV_STD_REGISTER_SEQ",allocationSize=1)
public class HivStdRegister implements Serializable {
	private static final long serialVersionUID = -81141547352681553L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="HIV_STD_REGISTER_SEQ")
	@Column(name="HIV_STD_REGISTER_ID")
	private long id;

	@Temporal(TemporalType.DATE)
	@Column(name="REGISTER_DATE")
	private Date registerDate;

	private String remarks;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to PatientMedicalCat
		@ManyToOne
		@JoinColumn(name="MEDICAL_CAT_ID")
		private PatientMedicalCat patientMedicalCat;
	//bi-directional many-to-one association to Patient
	@ManyToOne
	@JoinColumn(name="PATIENT_ID")
	private Patient patient;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	public Date getLastChgDate() {
		return lastChgDate;
	}


	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}


	public HivStdRegister() {
	}

	
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}


	

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}




	public PatientMedicalCat getPatientMedicalCat() {
		return patientMedicalCat;
	}


	public void setPatientMedicalCat(PatientMedicalCat patientMedicalCat) {
		this.patientMedicalCat = patientMedicalCat;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Users getLastChgBy() {
		return lastChgBy;
	}


	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}


	

}