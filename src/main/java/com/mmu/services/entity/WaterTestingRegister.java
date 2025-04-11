package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the WATER_TESTING_REGISTER database table.
 * 
 */
@Entity
@Table(name="WATER_TESTING_REGISTER")
@NamedQuery(name="WaterTestingRegister.findAll", query="SELECT w FROM WaterTestingRegister w")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="WATER_TESTING_REGISTER_SEQ", sequenceName="WATER_TESTING_REGISTER_SEQ",allocationSize=1)
public class WaterTestingRegister implements Serializable {
private static final long serialVersionUID = -81141547352681553L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="WATER_TESTING_REGISTER_SEQ")
	@Column(name="WATER_TESTING_REGISTER_ID")
	private long waterTestingRegisterId;

	@Column(name="CHLORINE_CONTENT")
	private String chlorineContent;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="LOCATION_OF_SAMPLING")
	private String locationOfSampling;

	private String remarks;

	@Column(name="SOURCE_OF_SUPPLY")
	private String sourceOfSupply;

	@Column(name="TESTED_BY")
	private String testedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="WATER_TESTING_DATE")
	private Date waterTestingDate;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	public WaterTestingRegister() {
	}

	public long getWaterTestingRegisterId() {
		return this.waterTestingRegisterId;
	}

	public void setWaterTestingRegisterId(long waterTestingRegisterId) {
		this.waterTestingRegisterId = waterTestingRegisterId;
	}

	public String getChlorineContent() {
		return this.chlorineContent;
	}

	public void setChlorineContent(String chlorineContent) {
		this.chlorineContent = chlorineContent;
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

	public String getTestedBy() {
		return this.testedBy;
	}

	public void setTestedBy(String testedBy) {
		this.testedBy = testedBy;
	}

	public Date getWaterTestingDate() {
		return this.waterTestingDate;
	}

	public void setWaterTestingDate(Date waterTestingDate) {
		this.waterTestingDate = waterTestingDate;
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