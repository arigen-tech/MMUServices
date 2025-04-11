package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name="attendance")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Attendance implements Serializable {
	 

 	private static final long serialVersionUID = 8543607763179883176L;
	
	
 	
	//@SequenceGenerator(name="employee_seq", sequenceName="employee_seq")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="employee_seq")
	
 	@Id
 	@Column(name="attendance_id", nullable = false)
	private Long attendanceId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="attendance_date")
	private Date attendanceDate;	
	
	
	@Column(name = "latitude")
	private Double latitude;
	
	@Column(name = "longitude")
	private Double longitude;
	
	@Column(name="mobile_no")
	private String mobileNo;
	
	@Column(name="employee_id")
	private Long employeeId;

	@Column(name="file_name")
	private String fileName;
	
	@OneToOne
	@JoinColumn(name="mmu_id")
	private MasMMU mmuId;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private Users userId;
	
	@Column(name="attn_flag")
	private String attendanceFlag;
	
	public Attendance() {
	
	}


	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}



	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
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


	public Users getUserId() {
		return userId;
	}


	public void setUserId(Users userId) {
		this.userId = userId;
	}


	public String getAttendanceFlag() {
		return attendanceFlag;
	}


	public void setAttendanceFlag(String attendanceFlag) {
		this.attendanceFlag = attendanceFlag;
	}


	

}