package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the DOCTOR_ROASTER database table.
 * 
 */
@Entity
@Table(name="DOCTOR_ROASTER")
@NamedQuery(name="DoctorRoaster.findAll", query="SELECT d FROM DoctorRoaster d")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="DOCTOR_ROASTER_SEQ", sequenceName="DOCTOR_ROASTER_SEQ", allocationSize=1)
public class DoctorRoaster implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -4975822472022773237L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="DOCTOR_ROASTER_SEQ")
	private long id;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Temporal(TemporalType.DATE)
	@Column(name="ROASTER_DATE")
	private Date roasterDate;

	@Column(name="ROASTER_VALUE")
	private String roasterValue;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	/*
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="DOCTOR_ID")
	private MasEmployee masEmployee;
	*/
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public DoctorRoaster() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Date getRoasterDate() {
		return this.roasterDate;
	}

	public void setRoasterDate(Date roasterDate) {
		this.roasterDate = roasterDate;
	}

	public String getRoasterValue() {
		return this.roasterValue;
	}

	public void setRoasterValue(String roasterValue) {
		this.roasterValue = roasterValue;
	}

	public MasDepartment getMasDepartment() {
		return this.masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	/*
	 * public MasEmployee getMasEmployee() { return this.masEmployee; }
	 * 
	 * public void setMasEmployee(MasEmployee masEmployee) { this.masEmployee =
	 * masEmployee; }
	 */

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}