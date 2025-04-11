package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the MAS_APPOINTMENT_SESSION database table.
 * 
 */
@Entity
@Table(name="MAS_APPOINTMENT_SESSION")
@NamedQuery(name="MasAppointmentSession.findAll", query="SELECT m FROM MasAppointmentSession m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_APPOINTMENT_SESSION_ID_GENERATOR", sequenceName="MAS_APPOINTMENT_SESSION_SEQ", allocationSize=1)
public class MasAppointmentSession implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6845303648383691562L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_APPOINTMENT_SESSION_ID_GENERATOR")
	
	
	@Column(name="ID")
	private long id;

	@Column(name="FROM_TIME")
	private String fromTime;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="TO_TIME")
	private String toTime;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPOINTMENT_TYPE_ID")
	private MasAppointmentType masAppointmentType;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPT_ID")
	private MasDepartment masDepartment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@Column(name="STATUS")
	private String Status;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;
	
	@OneToMany(mappedBy="masAppointmentSession")
	@JsonBackReference
	private List<AppSetup> appSetups;
	

	public MasAppointmentSession() {
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getFromTime() {
		return this.fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getToTime() {
		return this.toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public MasAppointmentType getMasAppointmentType() {
		return this.masAppointmentType;
	}

	public void setMasAppointmentType(MasAppointmentType masAppointmentType) {
		this.masAppointmentType = masAppointmentType;
	}

	public MasDepartment getMasDepartment() {
		return this.masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

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
	
	public List<AppSetup> getAppSetups() {
		return this.appSetups;
	}

	public void setAppSetups(List<AppSetup> appSetups) {
		this.appSetups = appSetups;
	}

	public AppSetup addAppSetup(AppSetup appSetup) {
		getAppSetups().add(appSetup);
		appSetup.setMasAppointmentSession(this);

		return appSetup;
	}

	public AppSetup removeAppSetup(AppSetup appSetup) {
		getAppSetups().remove(appSetup);
		appSetup.setMasAppointmentSession(null);

		return appSetup;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
	
	
}