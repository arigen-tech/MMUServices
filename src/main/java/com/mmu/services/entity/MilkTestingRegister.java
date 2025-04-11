package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the MILK_TESTING_REGISTER database table.
 * 
 */
@Entity
@Table(name="MILK_TESTING_REGISTER")
@NamedQuery(name="MilkTestingRegister.findAll", query="SELECT m FROM MilkTestingRegister m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MILK_TESTING_REGISTER_SEQ", sequenceName="MILK_TESTING_REGISTER_SEQ",allocationSize=1)
public class MilkTestingRegister implements Serializable {
private static final long serialVersionUID = -81141547352681553L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MILK_TESTING_REGISTER_SEQ")
	@Column(name="MILK_TESTING_REGISTER_ID")
	private long milkTestingRegisterId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="LOCATION_OF_SAMPLING")
	private String locationOfSampling;

	@Temporal(TemporalType.DATE)
	@Column(name="MILK_TESTING_DATE")
	private Date milkTestingDate;

	private String remarks;

	@Column(name="SOURCE_OF_SUPPLY")
	private String sourceOfSupply;

	@Column(name="SPECIFIC_GRAVITY")
	private String specificGravity;

	@Column(name="TESTED_BY")
	private String testedBy;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	public MilkTestingRegister() {
	}

	public long getMilkTestingRegisterId() {
		return this.milkTestingRegisterId;
	}

	public void setMilkTestingRegisterId(long milkTestingRegisterId) {
		this.milkTestingRegisterId = milkTestingRegisterId;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getLocationOfSampling() {
		return this.locationOfSampling;
	}

	public void setLocationOfSampling(String locationOfSampling) {
		this.locationOfSampling = locationOfSampling;
	}

	public Date getMilkTestingDate() {
		return this.milkTestingDate;
	}

	public void setMilkTestingDate(Date milkTestingDate) {
		this.milkTestingDate = milkTestingDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSourceOfSupply() {
		return this.sourceOfSupply;
	}

	public void setSourceOfSupply(String sourceOfSupply) {
		this.sourceOfSupply = sourceOfSupply;
	}

	public String getSpecificGravity() {
		return this.specificGravity;
	}

	public void setSpecificGravity(String specificGravity) {
		this.specificGravity = specificGravity;
	}

	public String getTestedBy() {
		return this.testedBy;
	}

	public void setTestedBy(String testedBy) {
		this.testedBy = testedBy;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}


}