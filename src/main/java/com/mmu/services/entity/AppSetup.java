package com.mmu.services.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the APP_SETUP database table.
 * 
 */
@Entity
@Table(name="APP_SETUP")
@NamedQuery(name="AppSetup.findAll", query="SELECT a FROM AppSetup a")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="APP_SETUP_SEQ", sequenceName="APP_SETUP_SEQ", allocationSize=1)
public class AppSetup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6202167208046523699L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="APP_SETUP_SEQ")
	private long id;

	@Column(name="DAYS")
	private String days;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPT_ID")
	private MasDepartment masDepartment;

	@Column(name="DOCTOR_ID")
	private BigDecimal doctorId;

	@Column(name="END_TIME")
	private String endTime;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_CHG_DATE")
	private Date lastChgDate;

	@Column(name="MAX_NO_OF_DAYS")
	private BigDecimal maxNoOfDays;

	@Column(name="MIN_NO_OF_DAYS")
	private BigDecimal minNoOfDays;

	@Column(name="START_TIME")
	private String startTime;

	@Column(name="START_TOKEN")
	private BigDecimal startToken;

	@Column(name="TOTAL_INTERVAL")
	private BigDecimal totalInterval;

	@Column(name="TOTAL_ONLINE_TOKEN")
	private BigDecimal totalOnlineToken;

	/*
	 * @Column(name="PORTAL_TOKEN") private BigDecimal totalPortalToken;
	 */
	
	/*
	 * @Column(name="MOBILE_TOKEN") private BigDecimal totalMobileToken;
	 */
	

	@Column(name="TOTAL_TOKEN")
	private BigDecimal totalToken;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SESSION_ID")
	private MasAppointmentSession masAppointmentSession;
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public AppSetup() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public String getDays() {
		return this.days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	

	public MasDepartment getMasDepartment() {
		return masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	public BigDecimal getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(BigDecimal doctorId) {
		this.doctorId = doctorId;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	

	public Date getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}


	public BigDecimal getMaxNoOfDays() {
		return this.maxNoOfDays;
	}

	public void setMaxNoOfDays(BigDecimal maxNoOfDays) {
		this.maxNoOfDays = maxNoOfDays;
	}

	public BigDecimal getMinNoOfDays() {
		return this.minNoOfDays;
	}

	public void setMinNoOfDays(BigDecimal minNoOfDays) {
		this.minNoOfDays = minNoOfDays;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getStartToken() {
		return this.startToken;
	}

	public void setStartToken(BigDecimal startToken) {
		this.startToken = startToken;
	}

	

	public BigDecimal getTotalInterval() {
		return this.totalInterval;
	}

	public void setTotalInterval(BigDecimal totalInterval) {
		this.totalInterval = totalInterval;
	}

	public BigDecimal getTotalOnlineToken() {
		return this.totalOnlineToken;
	}

	public void setTotalOnlineToken(BigDecimal totalOnlineToken) {
		this.totalOnlineToken = totalOnlineToken;
	}

	public BigDecimal getTotalToken() {
		return this.totalToken;
	}

	public void setTotalToken(BigDecimal totalToken) {
		this.totalToken = totalToken;
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
	
	public MasAppointmentSession getMasAppointmentSession() {
		return masAppointmentSession;
	}

	public void setMasAppointmentSession(MasAppointmentSession masAppointmentSession) {
		this.masAppointmentSession = masAppointmentSession;
	}

}