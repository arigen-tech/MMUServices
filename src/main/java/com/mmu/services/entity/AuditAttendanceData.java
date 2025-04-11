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
@Table(name="audit_attendance_data")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class AuditAttendanceData implements Serializable {
	 

 	private static final long serialVersionUID = 8543607763179883176L;
	
	
 	@Id
	@SequenceGenerator(name="attendance_data_seq", sequenceName="attendance_data_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="attendance_data_seq")
	@Column(name="attendance_data_id", updatable = false, nullable = false)
	private Long attendanceDataId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private Users userId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mmu_id")
	private MasMMU mmuId;
	
	@Column(name="first_in_photo")
	private String firstInPhoto;
	
	@Column(name="last_out_photo")
	private String lastOutPhoto;

	@Column(name = "first_lat")
	private Double firstLat;
	
	@Column(name = "first_long")
	private Double firstLong;
	
	@Column(name = "last_lat")
	private Double lastLat;
	
	@Column(name = "last_long")
	private Double lastLong;
	
	@Column(name="intime")
	private String inTime;
	
	@Column(name="outtime")
	private String outTime;
	
	@Column(name="first_att_id")
	private Long firstAttId;
	
	@Column(name="last_att_id")
	private Long lastAttId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="attendance_date")
	private Date attendanceDate;
	
	
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
	
	@Column(name="attendance_year")
	private int attendanceYear;
	
	@Column(name="attendance_month")
	private int attendanceMonth;
	

	public AuditAttendanceData() {
	}

	public Long getAttendanceDataId() {
		return attendanceDataId;
	}

	public void setAttendanceDataId(Long attendanceDataId) {
		this.attendanceDataId = attendanceDataId;
	}

	public Users getUserId() {
		return userId;
	}

	public void setUserId(Users userId) {
		this.userId = userId;
	}

	public MasMMU getMmuId() {
		return mmuId;
	}

	public void setMmuId(MasMMU mmuId) {
		this.mmuId = mmuId;
	}

	public String getFirstInPhoto() {
		return firstInPhoto;
	}

	public void setFirstInPhoto(String firstInPhoto) {
		this.firstInPhoto = firstInPhoto;
	}

	public String getLastOutPhoto() {
		return lastOutPhoto;
	}

	public void setLastOutPhoto(String lastOutPhoto) {
		this.lastOutPhoto = lastOutPhoto;
	}

	public Double getFirstLat() {
		return firstLat;
	}

	public void setFirstLat(Double firstLat) {
		this.firstLat = firstLat;
	}

	public Double getFirstLong() {
		return firstLong;
	}

	public void setFirstLong(Double firstLong) {
		this.firstLong = firstLong;
	}

	public Double getLastLat() {
		return lastLat;
	}

	public void setLastLat(Double lastLat) {
		this.lastLat = lastLat;
	}

	public Double getLastLong() {
		return lastLong;
	}

	public void setLastLong(Double lastLong) {
		this.lastLong = lastLong;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public Long getFirstAttId() {
		return firstAttId;
	}

	public void setFirstAttId(Long firstAttId) {
		this.firstAttId = firstAttId;
	}

	public Long getLastAttId() {
		return lastAttId;
	}

	public void setLastAttId(Long lastAttId) {
		this.lastAttId = lastAttId;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
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

	public int getAttendanceYear() {
		return attendanceYear;
	}

	public void setAttendanceYear(int attendanceYear) {
		this.attendanceYear = attendanceYear;
	}

	public int getAttendanceMonth() {
		return attendanceMonth;
	}

	public void setAttendanceMonth(int attendanceMonth) {
		this.attendanceMonth = attendanceMonth;
	}
	
	

}