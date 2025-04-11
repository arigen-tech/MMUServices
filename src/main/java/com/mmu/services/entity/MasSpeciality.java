package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the MAS_SPECIALITY database table.
 * 
 */
@Entity
@Table(name="MAS_SPECIALITY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasSpeciality.findAll", query="SELECT m FROM MasSpeciality m")
public class MasSpeciality implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_SPECIALITY_SEQ", sequenceName="MAS_SPECIALITY_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_SPECIALITY_SEQ")
	@Column(name="SPECIALITY_ID")
	private long specialityId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="SPECIALITY_CODE")
	private String specialityCode;

	@Column(name="SPECIALITY_NAME")
	private String specialityName;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasSpeciality() {
	}

	public long getSpecialityId() {
		return this.specialityId;
	}

	public void setSpecialityId(long specialityId) {
		this.specialityId = specialityId;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public String getSpecialityName() {
		return this.specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
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

}