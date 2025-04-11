package com.mmu.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name="audit_attendance")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class AuditAttendance implements Serializable {
	 

 	private static final long serialVersionUID = 8543607763179883176L;
 	
	
 	@Id
 	@SequenceGenerator(name="audit_attendance_seq", sequenceName="audit_attendance_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="audit_attendance_seq")
	@Column(name="audit_attendance_id", updatable = false, nullable = false)
	private Long attendanceId;
	
 	@OneToOne
 	@JoinColumn(name="employee_id")
 	private EmployeeRegistration employeeId;
 	
	@Temporal(TemporalType.DATE)
	@Column(name="attendance_date")
	private Date attendanceDate;	

	
	@OneToOne
	@JoinColumn(name="mmu_id")
	private MasMMU mmuId;
	
	@Column(name="attendance_location")
	private String attendanceLocation;
	
	@Column(name="attendance_time")
	private String attendanceTime;
	
	@Column(name="attendance_photo")
	private String attendancePhoto;
	
	@Column(name="status")
	private String status;
	
	@Column(name="remarks")
	private String remarks;
	
	@OneToOne
	@JoinColumn(name="last_chg_by")
	private Users lastChangeBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="last_chg_date")
	private Date lastChgDate;	
	
	
	
	public AuditAttendance() {
	
	}


	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}


	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}


	public Long getAttendanceId() {
		return attendanceId;
	}


	public MasMMU getMmuId() {
		return mmuId;
	}


	public void setMmuId(MasMMU mmuId) {
		this.mmuId = mmuId;
	}




	public EmployeeRegistration getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(EmployeeRegistration employeeId) {
		this.employeeId = employeeId;
	}


	public String getAttendanceLocation() {
		return attendanceLocation;
	}


	public void setAttendanceLocation(String attendanceLocation) {
		this.attendanceLocation = attendanceLocation;
	}


	public String getAttendanceTime() {
		return attendanceTime;
	}


	public void setAttendanceTime(String attendanceTime) {
		this.attendanceTime = attendanceTime;
	}


	public String getAttendancePhoto() {
		return attendancePhoto;
	}


	public void setAttendancePhoto(String attendancePhoto) {
		this.attendancePhoto = attendancePhoto;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Users getLastChangeBy() {
		return lastChangeBy;
	}


	public void setLastChangeBy(Users lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}


	public Date getLastChgDate() {
		return lastChgDate;
	}


	public void setLastChgDate(Date lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	

}