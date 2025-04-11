package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;


/**
 * The persistent class for the APP_EQUIPMENT_HD database table.
 * 
 */
@Entity
@Table(name="APP_EQUIPMENT_HD")
@NamedQuery(name="AppEquipmentHd.findAll", query="SELECT a FROM AppEquipmentHd a")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="APP_EQUIPMENT_HD_SEQ", sequenceName="APP_EQUIPMENT_HD_SEQ",allocationSize=1)
public class AppEquipmentHd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="APP_EQUIPMENT_HD_SEQ")
	@Column(name="EQUIPMENT_HD_ID")
	private long equipmentHdId;

	@Column(name="AUTHORIZED_ME_SCALE")
	private String authorizedMeScale;

	@Column(name="AUTHORIZED_QTY")
	private long authorizedQty;

	@Temporal(TemporalType.DATE)
	@Column(name="EQUIPMENT_DATE")
	private Date equipmentDate;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	//bi-directional many-to-one association to MasHospital
	@ManyToOne
	@JoinColumn(name="HOSPITAL_ID")
	private MasHospital masHospital;

	//bi-directional many-to-one association to MasStoreItem
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private MasStoreItem masStoreItem;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;
	
	//bi-directional many-to-one association to AppEquipmentAccessory
	@OneToMany(mappedBy="appEquipmentHd")
	private List<AppEquipmentDt> appEquipmentDt;


	public AppEquipmentHd() {
	}

	public long getEquipmentHdId() {
		return this.equipmentHdId;
	}

	public void setEquipmentHdId(long equipmentHdId) {
		this.equipmentHdId = equipmentHdId;
	}

	public String getAuthorizedMeScale() {
		return this.authorizedMeScale;
	}

	public void setAuthorizedMeScale(String authorizedMeScale) {
		this.authorizedMeScale = authorizedMeScale;
	}

	public long getAuthorizedQty() {
		return this.authorizedQty;
	}

	public void setAuthorizedQty(long authorizedQty) {
		this.authorizedQty = authorizedQty;
	}

	public Date getEquipmentDate() {
		return this.equipmentDate;
	}

	public void setEquipmentDate(Date equipmentDate) {
		this.equipmentDate = equipmentDate;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public MasHospital getMasHospital() {
		return this.masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public MasStoreItem getMasStoreItem() {
		return this.masStoreItem;
	}

	public void setMasStoreItem(MasStoreItem masStoreItem) {
		this.masStoreItem = masStoreItem;
	}

	public Users getUser() {
		return this.lastChgBy;
	}

	public void setUser(Users user) {
		this.lastChgBy = user;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public List<AppEquipmentDt> getAppEquipmentDt() {
		return appEquipmentDt;
	}

	public void setAppEquipmentDt(List<AppEquipmentDt> appEquipmentDt) {
		this.appEquipmentDt = appEquipmentDt;
	}

}