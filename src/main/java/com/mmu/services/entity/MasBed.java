package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the MAS_BED database table.
 * 
 */
@Entity
@Table(name="MAS_BED")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQuery(name="MasBed.findAll", query="SELECT m FROM MasBed m")
@SequenceGenerator(name="MAS_BED_BEDID_GENERATOR", sequenceName="MAS_BED_SEQ",allocationSize = 1)
public class MasBed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_BED_BEDID_GENERATOR")
	@Column(name="BED_ID")
	private long bedId;

	@Column(name="AD_NO")
	private String adNo;

	private String attached;

	@Column(name="BED_NO")
	private String bedNo;

	@Column(name="BED_TYPE")
	private String bedType;

	@Column(name="DIET_TYPE")
	private BigDecimal dietType;

	/*
	 * @Temporal(TemporalType.DATE)
	 * 
	 * @Column(name="DISCARD_DATE") private Date discardDate;
	 */

	private String flag;

	/*
	 * @Temporal(TemporalType.DATE)
	 * 
	 * @Column(name="INTRODUCTION_DATE") private Date introductionDate;
	 */

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="ROOM_ID")
	private BigDecimal roomId;

	private String status;
	
	@Column(name="BED_STATUS_ID")
	private Long bedStatusId;

	//bi-directional many-to-one association to MasBedStatus
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BED_STATUS_ID", nullable=false, insertable=false, updatable=false)
	private MasBedStatus masBedStatus;

	//bi-directional many-to-one association to MasDepartment
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users user;

	public MasBed() {
	}

	public long getBedId() {
		return this.bedId;
	}

	public void setBedId(long bedId) {
		this.bedId = bedId;
	}

	public String getAdNo() {
		return this.adNo;
	}

	public void setAdNo(String adNo) {
		this.adNo = adNo;
	}

	public String getAttached() {
		return this.attached;
	}

	public void setAttached(String attached) {
		this.attached = attached;
	}

	public String getBedNo() {
		return this.bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getBedType() {
		return this.bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}

	public BigDecimal getDietType() {
		return this.dietType;
	}

	public void setDietType(BigDecimal dietType) {
		this.dietType = dietType;
	}

	/*
	 * public Date getDiscardDate() { return this.discardDate; }
	 * 
	 * public void setDiscardDate(Date discardDate) { this.discardDate =
	 * discardDate; }
	 */

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/*
	 * public Date getIntroductionDate() { return this.introductionDate; }
	 * 
	 * public void setIntroductionDate(Date introductionDate) {
	 * this.introductionDate = introductionDate; }
	 */

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public BigDecimal getRoomId() {
		return this.roomId;
	}

	public void setRoomId(BigDecimal roomId) {
		this.roomId = roomId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasBedStatus getMasBedStatus() {
		return this.masBedStatus;
	}

	public void setMasBedStatus(MasBedStatus masBedStatus) {
		this.masBedStatus = masBedStatus;
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

	public Long getBedStatusId() {
		return bedStatusId;
	}

	public void setBedStatusId(Long bedStatusId) {
		this.bedStatusId = bedStatusId;
	}

	
}