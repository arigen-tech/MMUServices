package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the APP_EQUIPMENT_INFO database table.
 * 
 */
@Entity
@Table(name="APP_EQUIPMENT_INFO")
@NamedQuery(name="AppEquipmentInfo.findAll", query="SELECT a FROM AppEquipmentInfo a")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="APP_EQUIPMENT_INFO_SEQ", sequenceName="APP_EQUIPMENT_INFO_SEQ",allocationSize=1)
public class AppEquipmentInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="APP_EQUIPMENT_INFO_SEQ")
	@Column(name="EQUIPMENT_INFO_ID")
	private long equipmentInfoId;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="EQUIPMENT_DETAILS")
	private String equipmentDetails;

	@Column(name="EQUIPMENT_FLAG")
	private String equipmentFlag;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="PREVENTIVE_FLAG")
	private String preventiveFlag;

	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;

	@Column(name="TOTAL_PREVENTIVE")
	private long totalPreventive;

	//bi-directional many-to-one association to AppEquipmentDt
	@OneToOne
	@JoinColumn(name="EQUIPMENT_DT_ID")
	private AppEquipmentDt appEquipmentDt;

	public AppEquipmentInfo() {
	}

	public long getEquipmentInfoId() {
		return this.equipmentInfoId;
	}

	public void setEquipmentInfoId(long equipmentInfoId) {
		this.equipmentInfoId = equipmentInfoId;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEquipmentDetails() {
		return this.equipmentDetails;
	}

	public void setEquipmentDetails(String equipmentDetails) {
		this.equipmentDetails = equipmentDetails;
	}

	public String getEquipmentFlag() {
		return this.equipmentFlag;
	}

	public void setEquipmentFlag(String equipmentFlag) {
		this.equipmentFlag = equipmentFlag;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getPreventiveFlag() {
		return this.preventiveFlag;
	}

	public void setPreventiveFlag(String preventiveFlag) {
		this.preventiveFlag = preventiveFlag;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public long getTotalPreventive() {
		return this.totalPreventive;
	}

	public void setTotalPreventive(long totalPreventive) {
		this.totalPreventive = totalPreventive;
	}

	public AppEquipmentDt getAppEquipmentDt() {
		return this.appEquipmentDt;
	}

	public void setAppEquipmentDt(AppEquipmentDt appEquipmentDt) {
		this.appEquipmentDt = appEquipmentDt;
	}

}