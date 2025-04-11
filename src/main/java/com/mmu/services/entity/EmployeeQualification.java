package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the MAS_EMPLOYEE database table.
 * 
 */

@SuppressWarnings("restriction")
@Entity
@Immutable
@Table(name="employee_qualification")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class EmployeeQualification implements Serializable {
	 

 	private static final long serialVersionUID = 8543607763179883176L;
	
	
 	@Id
	@SequenceGenerator(name="employee_qualification_seq", sequenceName="employee_qualification_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="employee_qualification_seq")
	@Column(name="employee_qualification_id", updatable = false, nullable = false)
	private long employeeQualificationId;
	

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	@Column(name="institution_name")
	private String institutionName;
	
	@Column(name="completion_year")
	private int completionYear;
	
	@Column(name="document")
	private byte[] document;
	 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="employee_id")
	private EmployeeRegistration employeeId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="last_chg_by")
	private Users lastChgBy;
	

	@Temporal(TemporalType.DATE)
	@Column(name="last_chg_date")
	private Date lastChgDate;	
	
	@Column(name="qualification_name")
	private String qualificationName;
	
	@Column(name="file_path")
	private String filePath;
	
	
	@Column(name="mime_type")
	private String mimeType;
	
	public EmployeeQualification() {
	}

		public Date getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public long getEmployeeQualificationId() {
		return employeeQualificationId;
	}

	public void setEmployeeQualificationId(long employeeQualificationId) {
		this.employeeQualificationId = employeeQualificationId;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public int getCompletionYear() {
		return completionYear;
	}

	public void setCompletionYear(int completionYear) {
		this.completionYear = completionYear;
	}



	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}

	public EmployeeRegistration getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(EmployeeRegistration employeeId) {
		this.employeeId = employeeId;
	}

	public String getQualificationName() {
		return qualificationName;
	}

	public void setQualificationName(String qualificationName) {
		this.qualificationName = qualificationName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	

}