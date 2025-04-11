package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the PROCEDURE_HD database table.
 * 
 */
@Entity
@Table(name="PROCEDURE_HD")
@NamedQuery(name="ProcedureHd.findAll", query="SELECT p FROM ProcedureHd p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="PROCEDURE_HEADER_SEQ", sequenceName="PROCEDURE_HEADER_SEQ", allocationSize=1)
public class ProcedureHd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="PROCEDURE_HEADER_SEQ")
	@Column(name="PROCEDURE_HD_ID")
	private long procedureHdId;

	@Column(name="MMU_ID")
	private Long mmuId;

	
	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="OPD_PATIENT_DETAILS_ID")
	private Long opdPatientDetailsId;

	@Column(name="PATIENT_ID")
	private Long patientId;

	@Column(name="PROCEDURE_TYPE")
	private String procedureType;

	@Column(name="REQUISITION_DATE")
	private Timestamp requisitionDate;

	private String status;

	@Column(name="VISIT_ID")
	private Long visitId;
	
	@Column(name="DOCTOR_ID")
	private Long doctorId;
	
	@Column(name="camp_id")
	private Long campId;

	   //bi-directional many-to-one association to ProcedureDt
		@OneToMany(mappedBy="procedureHd")
		@JsonBackReference
		private List<ProcedureDt> procedureDts;

		
		//bi-directional many-to-one association to MasHospital
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="MMU_ID",nullable=false,insertable=false,updatable=false)
		private MasMMU masMMU;

		//bi-directional many-to-one association to Patient
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="PATIENT_ID",nullable=false,insertable=false,updatable=false)
		private Patient patient;

		//bi-directional many-to-one association to User
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
		private Users user;

		//bi-directional many-to-one association to Visit
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="VISIT_ID",nullable=false,insertable=false,updatable=false)
		private Visit visit;
	    
		public OpdPatientDetail getOpdPatientDetails() {
			return opdPatientDetails;
		}

		public void setOpdPatientDetails(OpdPatientDetail opdPatientDetails) {
			this.opdPatientDetails = opdPatientDetails;
		}

		@ManyToOne
		@JoinColumn(name="OPD_PATIENT_DETAILS_ID",nullable=false,insertable=false,updatable=false)
		private OpdPatientDetail opdPatientDetails;
	
	public ProcedureHd() {
	}

	public long getProcedureHdId() {
		return this.procedureHdId;
	}

	public void setProcedureHdId(long procedureHdId) {
		this.procedureHdId = procedureHdId;
	}

   

	public Long getMmuId() {
		return mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public Long getOpdPatientDetailsId() {
		return this.opdPatientDetailsId;
	}

	public void setOpdPatientDetailsId(Long opdPatientDetailsId) {
		this.opdPatientDetailsId = opdPatientDetailsId;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getProcedureType() {
		return this.procedureType;
	}

	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}

	public Timestamp getRequisitionDate() {
		return this.requisitionDate;
	}

	public void setRequisitionDate(Timestamp requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public List<ProcedureDt> getProcedureDts() {
		return procedureDts;
	}

	public void setProcedureDts(List<ProcedureDt> procedureDts) {
		this.procedureDts = procedureDts;
	}

	
	public MasMMU getMasMMU() {
		return masMMU;
	}

	public void setMasMMU(MasMMU masMMU) {
		this.masMMU = masMMU;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}
	
	
	
    
}