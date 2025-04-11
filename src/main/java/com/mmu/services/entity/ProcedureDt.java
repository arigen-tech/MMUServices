package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the PROCEDURE_DT database table.
 * 
 */
@Entity
@Table(name="PROCEDURE_DT")
@NamedQuery(name="ProcedureDt.findAll", query="SELECT p FROM ProcedureDt p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PROCEDURE_DETAILS_SEQ", sequenceName="PROCEDURE_DETAILS_SEQ", allocationSize=1)
public class ProcedureDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PROCEDURE_DETAILS_SEQ")
	@Column(name="PROCEDURE_DT_ID")
	private long procedureDtId;

	@Column(name="APPOINTMENT_DATE")
	private Timestamp appointmentDate;

	@Column(name="FINAL_PROCEDURE_STATUS")
	private String finalProcedureStatus;

	@Column(name="FREQUENCY_ID")
	private Long frequencyId;

	@Temporal(TemporalType.DATE)
	@Column(name="NEXT_APPOINTMENT_DATE")
	private Date nextAppointmentDate;

	@Column(name="NO_OF_DAYS")
	private Long noOfDays;

	@Column(name="NURSING_REMARK")
	private String nursingRemark;

	@Column(name="PROCEDURE_DATE")
	private Timestamp procedureDate;

	@Column(name="PROCEDURE_HD_ID")
	private Long procedureHdId;

	@Column(name="PROCEDURE_ID")
	private Long procedureId;

	private String remarks;

	private String status;
	
	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	//bi-directional many-to-one association to MasFrequency
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="FREQUENCY_ID",nullable=false,insertable=false,updatable=false)
		private MasFrequency masFrequency;

		//bi-directional many-to-one association to MasNursingCare
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="PROCEDURE_ID",nullable=false,insertable=false,updatable=false)
		private MasNursingCare masNursingCare;

	
		//bi-directional many-to-one association to MasAnesthesia		
		@ManyToOne(fetch=FetchType.LAZY)
	  
		@JoinColumn(name="ANESTHESIA_ID")
		private MasAnesthesia masAnesthesia;
		
	
	  @ManyToOne(fetch=FetchType.LAZY)
	  @JoinColumn(name="DOCTOR_ID") 
	  private Users users;
	
		
		//bi-directional many-to-one association to ProcedureHd
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="PROCEDURE_HD_ID",nullable=false,insertable=false,updatable=false)
		private ProcedureHd procedureHd;
		
	
	
	public ProcedureDt() {
	}

	public long getProcedureDtId() {
		return this.procedureDtId;
	}

	public void setProcedureDtId(long procedureDtId) {
		this.procedureDtId = procedureDtId;
	}

	public Timestamp getAppointmentDate() {
		return this.appointmentDate;
	}

	public void setAppointmentDate(Timestamp appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getFinalProcedureStatus() {
		return this.finalProcedureStatus;
	}

	public void setFinalProcedureStatus(String finalProcedureStatus) {
		this.finalProcedureStatus = finalProcedureStatus;
	}

	public Long getFrequencyId() {
		return this.frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Date getNextAppointmentDate() {
		return this.nextAppointmentDate;
	}

	public void setNextAppointmentDate(Date nextAppointmentDate) {
		this.nextAppointmentDate = nextAppointmentDate;
	}

	public Long getNoOfDays() {
		return this.noOfDays;
	}

	public void setNoOfDays(Long noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getNursingRemark() {
		return this.nursingRemark;
	}

	public void setNursingRemark(String nursingRemark) {
		this.nursingRemark = nursingRemark;
	}

	public Timestamp getProcedureDate() {
		return this.procedureDate;
	}

	public void setProcedureDate(Timestamp procedureDate) {
		this.procedureDate = procedureDate;
	}

	public Long getProcedureHdId() {
		return this.procedureHdId;
	}

	public void setProcedureHdId(Long procedureHdId) {
		this.procedureHdId = procedureHdId;
	}

	public Long getProcedureId() {
		return this.procedureId;
	}

	public void setProcedureId(Long procedureId) {
		this.procedureId = procedureId;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasFrequency getMasFrequency() {
		return masFrequency;
	}

	public void setMasFrequency(MasFrequency masFrequency) {
		this.masFrequency = masFrequency;
	}

	public MasNursingCare getMasNursingCare() {
		return masNursingCare;
	}

	public void setMasNursingCare(MasNursingCare masNursingCare) {
		this.masNursingCare = masNursingCare;
	}

	public ProcedureHd getProcedureHd() {
		return procedureHd;
	}

	public void setProcedureHd(ProcedureHd procedureHd) {
		this.procedureHd = procedureHd;
	}
	
	public MasAnesthesia getMasAnesthesia() {
		return masAnesthesia;
	}

	public void setMasAnesthesia(MasAnesthesia masAnesthesia) {
		this.masAnesthesia = masAnesthesia;
	}

	
	  public Users getUsers()
	  { 
		  return users; 
	  }
	  
	  public void setUsers(Users users)
	  { 
		  this.users = users; 
	   }
	 
	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	
	

}