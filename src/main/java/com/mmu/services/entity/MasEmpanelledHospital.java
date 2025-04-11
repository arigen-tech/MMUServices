package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the MAS_IMPANNELED_HOSPITAL database table.
 * 
 */
@Entity
@Table(name = "MAS_EMPANELLED_HOSPITAL")
@NamedQuery(name = "MasEmpanelledHospital.findAll", query = "SELECT m FROM MasEmpanelledHospital m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "MAS_EMPANELLED_HOSPITAL_SEQ", sequenceName = "MAS_EMPANELLED_HOSPITAL_SEQ", allocationSize = 1)
public class MasEmpanelledHospital implements Serializable {
	private static final long serialVersionUID = -583276470562642050L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MAS_EMPANELLED_HOSPITAL_SEQ")
	@Column(name = "EMPANELLED_HOSPITAL_ID")
	private long empanelledHospitalId;
	
	@Column(name = "EMPANELLED_HOSPITAL_NAME")
	private String empanelledHospitalName;
	
	@Column(name = "EMPANELLED_HOSPITAL_CODE")
	private String empanelledHospitalCode;	

	@Column(name = "PHONE_NO")
	private String phoneNo;
	
	
	@Column(name = "LAST_CHG_BY")
	private Long lastchgBy;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;
	
	//@Temporal(TemporalType.DATE)
	@Column(name = "LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name = "ADDRESS")
	private String empanelledHospitalAddress;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CITY_ID")
	private Long cityId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "city_id",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasCity masCity;

	public MasEmpanelledHospital() {
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}
	
	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getEmpanelledHospitalId() {
		return empanelledHospitalId;
	}

	public void setEmpanelledHospitalId(long empanelledHospitalId) {
		this.empanelledHospitalId = empanelledHospitalId;
	}

	
	public String getEmpanelledHospitalCode() {
		return empanelledHospitalCode;
	}

	public void setEmpanelledHospitalCode(String empanelledHospitalCode) {
		this.empanelledHospitalCode = empanelledHospitalCode;
	}

	public String getEmpanelledHospitalName() {
		return empanelledHospitalName;
	}

	public void setEmpanelledHospitalName(String empanelledHospitalName) {
		this.empanelledHospitalName = empanelledHospitalName;
	}

	public String getEmpanelledHospitalAddress() {
		return empanelledHospitalAddress;
	}

	public void setEmpanelledHospitalAddress(String empanelledHospitalAddress) {
		this.empanelledHospitalAddress = empanelledHospitalAddress;
	}
	
	public Long getLastchgBy() {
		return lastchgBy;
	}

	public void setLastchgBy(Long lastchgBy) {
		this.lastchgBy = lastchgBy;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	
	
	

}