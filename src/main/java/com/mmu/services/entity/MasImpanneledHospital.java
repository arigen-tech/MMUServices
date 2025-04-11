package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * The persistent class for the MAS_IMPANNELED_HOSPITAL database table.
 * 
 */
@Entity
@Table(name="MAS_EMPANELLED_HOSPITAL")
@NamedQuery(name="MasImpanneledHospital.findAll", query="SELECT m FROM MasImpanneledHospital m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_IMPANNELED_HOSPITAL_SEQ", sequenceName="MAS_IMPANNELED_HOSPITAL_SEQ", allocationSize=1)
public class MasImpanneledHospital implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1525183189842145181L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_IMPANNELED_HOSPITAL_SEQ")
	
	@Column(name="EMPANELLED_HOSPITAL_ID")
	private long impanneledHospitalId;

	private String address;

	@Column(name="EMPANELLED_HOSPITAL_CODE")
	private String impanneledHospitalCode;

	@Column(name="EMPANELLED_HOSPITAL_NAME")
	private String impanneledHospitalName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	//bi-directional many-to-one association to ReferralPatientHd
	@OneToMany(mappedBy="masImpanneledHospital")
	@JsonBackReference
	private List<ReferralPatientHd> referralPatientHds;

	public MasImpanneledHospital() {
	}

	public long getImpanneledHospitalId() {
		return this.impanneledHospitalId;
	}

	public void setImpanneledHospitalId(long impanneledHospitalId) {
		this.impanneledHospitalId = impanneledHospitalId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImpanneledHospitalCode() {
		return this.impanneledHospitalCode;
	}

	public void setImpanneledHospitalCode(String impanneledHospitalCode) {
		this.impanneledHospitalCode = impanneledHospitalCode;
	}

	public String getImpanneledHospitalName() {
		return this.impanneledHospitalName;
	}

	public void setImpanneledHospitalName(String impanneledHospitalName) {
		this.impanneledHospitalName = impanneledHospitalName;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
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

	public List<ReferralPatientHd> getReferralPatientHds() {
		return this.referralPatientHds;
	}

	public void setReferralPatientHds(List<ReferralPatientHd> referralPatientHds) {
		this.referralPatientHds = referralPatientHds;
	}

	public ReferralPatientHd addReferralPatientHd(ReferralPatientHd referralPatientHd) {
		getReferralPatientHds().add(referralPatientHd);
		referralPatientHd.setMasImpanneledHospital(this);

		return referralPatientHd;
	}

	public ReferralPatientHd removeReferralPatientHd(ReferralPatientHd referralPatientHd) {
		getReferralPatientHds().remove(referralPatientHd);
		referralPatientHd.setMasImpanneledHospital(null);

		return referralPatientHd;
	}

}