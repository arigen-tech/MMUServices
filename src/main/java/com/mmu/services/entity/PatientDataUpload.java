package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;




@Entity
@Table(name = "patient_data_upload")
@NamedQuery(name="patient_data_upload.findAll", query="SELECT p FROM PatientDataUpload p")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})


@SequenceGenerator(name="PATIENT_DATA_UPLOAD_GENERATOR", sequenceName="PATIENT_SEQ", allocationSize=1)
public class PatientDataUpload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="patient_data_upload_seq", sequenceName="patient_data_upload_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="patient_data_upload_seq")
	@Column(name="patient_data_upload_id", unique=true, nullable=false)
	private Long patientDataUploadId;

	@Column(name="patient_id", nullable=false)
	private Long patientId;


	@Column(name="file_data" ,length=200)
	private String fileData;
	

	@Column(name="status", length=1)
	private String status;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
	private MasCity masCity;
	*/
	@Column(name="last_chg_by")
	private Long lastchgBy;
	
	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	public Long getPatientDataUploadId() {
		return patientDataUploadId;
	}

	public void setPatientDataUploadId(Long patientDataUploadId) {
		this.patientDataUploadId = patientDataUploadId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Long getLastchgBy() {
		return lastchgBy;
	}

	public void setLastchgBy(Long lastchgBy) {
		this.lastchgBy = lastchgBy;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	@Override
	public String toString() {
		return "PatientDataUpload [patientDataUploadId=" + patientDataUploadId + ", patientId=" + patientId
				+ ", fileData=" + fileData + ", status=" + status + ", lastchgBy=" + lastchgBy + ", lastChgDate="
				+ lastChgDate + "]";
	}



	
}
