package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
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


/**
 * The persistent class for the MAS_MED_EXAM database table.
 * 
 */
@Entity
@Table(name="MAS_MED_EXAM")
@NamedQuery(name="MasMedExam.findAll", query="SELECT m FROM MasMedExam m")
@SequenceGenerator(name="MAS_MED_EXAM_MEDICALEXAMID_GENERATOR", sequenceName="MAS_MED_EXAM_SEQ", allocationSize=1)
public class MasMedExam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_MED_EXAM_MEDICALEXAMID_GENERATOR")
	@Column(name="MEDICAL_EXAM_ID")
	private Long medicalExamId;
	
	@Column(name="APPOINTMENT_TYPE_ID")
	Long   appointmentTypeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY")
	private Users user;	

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MEDICAL_EXAM_NAME")
	private String medicalExamName;
	
	@Column(name="MEDICAL_EXAM_CODE")
	private String medicalExamCode;
	
	@Column(name="APPLY_FOR")
	private String onlineOffline;
	
	
	private String status;

	public MasMedExam() {
	}

	public long getMedicalExamId() {
		return this.medicalExamId;
	}

	public void setMedicalExamId(long medicalExamId) {
		this.medicalExamId = medicalExamId;
	}

		 
	public MasAppointmentType getMasAppointmentType() {
		return masAppointmentType;
	}

	public void setMasAppointmentType(MasAppointmentType masAppointmentType) {
		this.masAppointmentType = masAppointmentType;
	}

	public Long getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(Long appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}	

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMedicalExamName() {
		return this.medicalExamName;
	}

	public void setMedicalExamName(String medicalExamName) {
		this.medicalExamName = medicalExamName;
	}
	
	public String getMedicalExamCode() {
		return this.medicalExamCode;
	}

	public void setMedicalExamCode(String medicalExamCode) {
		this.medicalExamCode = medicalExamCode;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="APPOINTMENT_TYPE_ID",nullable=false,insertable=false,updatable=false)
	private MasAppointmentType masAppointmentType;

	public String getOnlineOffline() {
		return onlineOffline;
	}

	public void setOnlineOffline(String onlineOffline) {
		this.onlineOffline = onlineOffline;
	}

	public void setMedicalExamId(Long medicalExamId) {
		this.medicalExamId = medicalExamId;
	}	

	 


}
