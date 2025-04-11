package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


/**
 * The persistent class for the MAS_SECTION database table.
 * 
 */
@Entity
@Table(name="MAS_SECTION")
@NamedQuery(name="MasSection.findAll", query="SELECT m FROM MasSection m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_SECTION_SEQ" , allocationSize=1)
public class MasSection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_SECTION_SEQ")
	@Column(name="SECTION_ID")
	private long sectionId;

	/*
	 * @Column(name="HOSPITAL_ID") private Long hospitalId;
	 */

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	@Column(name="SECTION_CODE")
	private String sectionCode;

	@Column(name="SECTION_NAME")
	private String sectionName;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users users;
	
	//bi-directional many-to-one association to User
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="HOSPITAL_ID")
		private MasHospital masHospital;

	public MasSection() {
	}

	public long getSectionId() {
		return this.sectionId;
	}

	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}

	/*
	 * public Long getHospitalId() { return this.hospitalId; }
	 * 
	 * public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }
	 */

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getSectionCode() {
		return this.sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getSectionName() {
		return this.sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

}