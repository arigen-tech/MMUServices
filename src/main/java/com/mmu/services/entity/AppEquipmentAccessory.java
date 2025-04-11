package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the APP_EQUIPMENT_ACCESSORY database table.
 * 
 */
@Entity
@Table(name="APP_EQUIPMENT_ACCESSORY")
@NamedQuery(name="AppEquipmentAccessory.findAll", query="SELECT a FROM AppEquipmentAccessory a")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="APP_EQUIPMENT_INFO_SEQ", sequenceName="APP_EQUIPMENT_INFO_SEQ",allocationSize=1)
public class AppEquipmentAccessory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="APP_EQUIPMENT_INFO_SEQ")
	@Column(name="ACCESSORY_ID")
	private long accessoryId;

	@Column(name="ACCESSORY_DETAILS")
	private String accessoryDetails;

	@Column(name="ACCESSORY_NAME")
	private String accessoryName;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	@Column(name="MODEL_NUMBER")
	private String modelNumber;

	@Column(name="SERIAL_NUMBER")
	private String serialNumber;

	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;

	//bi-directional many-to-one association to AppEquipmentDt
	@ManyToOne
	@JoinColumn(name="EQUIPMENT_DT_ID")
	private AppEquipmentDt appEquipmentDt;

	public AppEquipmentAccessory() {
	}

	public long getAccessoryId() {
		return this.accessoryId;
	}

	public void setAccessoryId(long accessoryId) {
		this.accessoryId = accessoryId;
	}

	public String getAccessoryDetails() {
		return this.accessoryDetails;
	}

	public void setAccessoryDetails(String accessoryDetails) {
		this.accessoryDetails = accessoryDetails;
	}

	public String getAccessoryName() {
		return this.accessoryName;
	}

	public void setAccessoryName(String accessoryName) {
		this.accessoryName = accessoryName;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getModelNumber() {
		return this.modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public AppEquipmentDt getAppEquipmentDt() {
		return this.appEquipmentDt;
	}

	public void setAppEquipmentDt(AppEquipmentDt appEquipmentDt) {
		this.appEquipmentDt = appEquipmentDt;
	}

}