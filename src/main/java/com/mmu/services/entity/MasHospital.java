package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the MAS_HOSPITAL database table.
 * 
 */
@Entity
@Table(name="MAS_HOSPITAL")
@NamedQuery(name="MasHospital.findAll", query="SELECT m FROM MasHospital m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_HOSPITAL_HOSPITALID_GENERATOR", sequenceName="MAS_HOSPITAL_SEQ",allocationSize=1)
public class MasHospital implements Serializable {
	private static final long serialVersionUID = -8647496571766655879L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_HOSPITAL_HOSPITALID_GENERATOR")
	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	/* private String address; */

	
	/*
	 * @Column(name="DISTRICT_ID") private BigDecimal districtId;
	 */

	@Column(name="HOSPITAL_CODE")
	private String hospitalCode;

	@Column(name="HOSPITAL_NAME")
	private String hospitalName;

	//@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	
	@Column(name="STATUS")
	private String status;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "UNIT_ID")
	@JsonBackReference
	private MasUnit masUnit;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	@JsonBackReference
	private Users user;

	@OneToMany(mappedBy="masHospital")
	@JsonBackReference
	private List<AppSetup> appSetups;

	@OneToMany(mappedBy="masHospital")
	@JsonBackReference
	private List<MasAppointmentSession> masAppointmentSessions;
	
	

	public MasHospital() {
	}

	public Long getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	/*
	 * @Column(name="HOSPITAL_FLAG") private String hospitalFlag;
	 */
	/*
	 * public String getAddress() { return this.address; }
	 * 
	 * public void setAddress(String address) { this.address = address; }
	 */

	/*
	 * public BigDecimal getCommandId() { return this.commandId; }
	 * 
	 * public void setCommandId(BigDecimal commandId) { this.commandId = commandId;
	 * }
	 */

	
	/*
	 * public BigDecimal getDistrictId() { return this.districtId; }
	 * 
	 * public void setDistrictId(BigDecimal districtId) { this.districtId =
	 * districtId; }
	 */

	public String getHospitalCode() {
		return this.hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalName() {
		return this.hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
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

	public MasUnit getMasUnit() {
		return this.masUnit;
	}

	public void setMasUnit(MasUnit masUnit) {
		this.masUnit = masUnit;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	/*
	 * public String getHospitalFlag() { return this.hospitalFlag; }
	 * 
	 * public void setHospitalFlag(String hospitalFlag) { this.hospitalFlag =
	 * hospitalFlag; }
	 */
	public List<AppSetup> getAppSetups() {
		return this.appSetups;
	}

	public void setAppSetups(List<AppSetup> appSetups) {
		this.appSetups = appSetups;
	}

	public AppSetup addAppSetup(AppSetup appSetup) {
		getAppSetups().add(appSetup);
		appSetup.setMasHospital(this);

		return appSetup;
	}

	public AppSetup removeAppSetup(AppSetup appSetup) {
		getAppSetups().remove(appSetup);
		appSetup.setMasHospital(null);

		return appSetup;
	}

	public List<MasAppointmentSession> getMasAppointmentSessions() {
		return this.masAppointmentSessions;
	}

	public void setMasAppointmentSessions(List<MasAppointmentSession> masAppointmentSessions) {
		this.masAppointmentSessions = masAppointmentSessions;
	}

	public MasAppointmentSession addMasAppointmentSession(MasAppointmentSession masAppointmentSession) {
		getMasAppointmentSessions().add(masAppointmentSession);
		masAppointmentSession.setMasHospital(this);

		return masAppointmentSession;
	}

	public MasAppointmentSession removeMasAppointmentSession(MasAppointmentSession masAppointmentSession) {
		getMasAppointmentSessions().remove(masAppointmentSession);
		masAppointmentSession.setMasHospital(null);

		return masAppointmentSession;
	}
	
	//bi-directional many-to-one association to ReferralPatientHd
	
	/*  @OneToMany(mappedBy="masHospital")
	  @JsonBackReference
	  private List<ReferralPatientHd> referralPatientHds1;*/
	  
	/* //bi-directional many-to-one association to ReferralPatientHd
		@OneToMany(mappedBy="masHospital2")
		@JsonBackReference
		private List<ReferralPatientHd> referralPatientHds2;*/



	/*
	 public List<ReferralPatientHd> getReferralPatientHds1()
	 { 
		 return	 referralPatientHds1; 
	 }
	 public void setReferralPatientHds1(List<ReferralPatientHd> referralPatientHds1)
	  { 
		  this.referralPatientHds1 = referralPatientHds1; 
	  }
	 */

	  /*	public List<ReferralPatientHd> getReferralPatientHds2() {
			return referralPatientHds2;
		}

		public void setReferralPatientHds2(List<ReferralPatientHd> referralPatientHds2) {
			this.referralPatientHds2 = referralPatientHds2;
		}*/

	/*	public List<AdmissionDischarge> getAdmissionDischarge() {
			return admissionDischarge;
		}

		public void setAdmissionDischarge(List<AdmissionDischarge> admissionDischarge) {
			this.admissionDischarge = admissionDischarge;
		}
		*/
		
		@Column(name="HOSPITAL_PARENT_ID")
		private Long hospitalParentId;
		
		public Long getHospitalParentId() {
			return hospitalParentId;
		}

		public void setHospitalParentId(Long hospitalParentId) {
			this.hospitalParentId = hospitalParentId;
		}
		
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "HOSPITAL_PARENT_ID",nullable=false,insertable=false,updatable=false)
		@JsonBackReference
		private MasHospital hospitalParent;
		
		
		
		public MasHospital getHospitalParent() {
			return hospitalParent;
		}

		public void setHospitalParent(MasHospital hospitalParent) {
			this.hospitalParent = hospitalParent;
		}

		@Column(name="HOSPITAL_FLAG")
		private String hospitalFlag;

		

		public String getHospitalFlag() {
			return hospitalFlag;
		}

		public void setHospitalFlag(String hospitalFlag) {
			this.hospitalFlag = hospitalFlag;
		}
		
		
		
		

}