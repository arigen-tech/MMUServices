package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the APP_EQUIPMENT_DT database table.
 * 
 */
@Entity
@Table(name="APP_EQUIPMENT_DT")
@NamedQuery(name="AppEquipmentDt.findAll", query="SELECT a FROM AppEquipmentDt a")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="APP_EQUIPMENT_DT_SEQ", sequenceName="APP_EQUIPMENT_DT_SEQ",allocationSize=1)
public class AppEquipmentDt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="APP_EQUIPMENT_DT_SEQ")
	@Column(name="EQUIPMENT_DT_ID")
	private long equipmentDtId;

	private String accessary;

	private String amc;
	
	
	@Column(name="BOARD_OUT")
	private String boardOut;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name="BOARDOUTDATE")
	private Date boardOutDate;
	
	private BigDecimal depreciation;

	/*
	 * @Column(name="EQUIPMENT_HD_ID") private BigDecimal equipmentHdId;
	 */

	private String installed;

	@Temporal(TemporalType.DATE)
	@Column(name="INSTALLED_DATE")
	private Date installedDate;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String make;
	
	@Column(name="REMARKS")
	private String remarks;
	

	@Column(name="MODEL_NUMBER")
	private String modelNumber;

	private BigDecimal price;

	@Temporal(TemporalType.DATE)
	@Column(name="RECEIVED_DATE")
	private Date receivedDate;

	@Column(name="RECEIVED_NUMBER")
	private String receivedNumber;

	@Column(name="SERIAL_NUMBER")
	private String serialNumber;

	@Column(name="TECHNICAL_SPECIFICATION")
	private String technicalSpecification;

	private String warranty;

	//bi-directional many-to-one association to AppEquipmentAccessory
	@OneToMany(mappedBy="appEquipmentDt")
	private List<AppEquipmentAccessory> appEquipmentAccessories;
	

	//bi-directional many-to-one association to AppEquipmentAccessory
	@OneToMany(mappedBy="appEquipmentDt")
	private List<AppAuditEquipmentDt> appAuditEquipmentDt;


	//bi-directional many-to-one association to AppEquipmentInfo
	/*
	 * @OneToMany(mappedBy="appEquipmentDt") private List<AppEquipmentInfo>
	 * appEquipmentInfos;
	 */

	public List<AppAuditEquipmentDt> getAppAuditEquipmentDt() {
		return appAuditEquipmentDt;
	}

	public void setAppAuditEquipmentDt(List<AppAuditEquipmentDt> appAuditEquipmentDt) {
		this.appAuditEquipmentDt = appAuditEquipmentDt;
	}

	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="appEquipmentDt")
	@JsonBackReference
	private AppEquipmentInfo appEquipmentInfos;
	
	//bi-directional many-to-one association to MasDepartment
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_ID")
	private MasDepartment masDepartment;

	//bi-directional many-to-one association to MasManufacturer
	@ManyToOne
	@JoinColumn(name="SUPPLIER_ID")
	private MasStoreSupplier masStoreSupplier;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EQUIPMENT_HD_ID")
	@JsonBackReference
	private AppEquipmentHd appEquipmentHd;
	
	public AppEquipmentDt() {
	}

	public long getEquipmentDtId() {
		return this.equipmentDtId;
	}

	public void setEquipmentDtId(long equipmentDtId) {
		this.equipmentDtId = equipmentDtId;
	}

	public String getAccessary() {
		return this.accessary;
	}

	public void setAccessary(String accessary) {
		this.accessary = accessary;
	}

	public String getAmc() {
		return this.amc;
	}

	public void setAmc(String amc) {
		this.amc = amc;
	}

	public BigDecimal getDepreciation() {
		return this.depreciation;
	}

	public void setDepreciation(BigDecimal depreciation) {
		this.depreciation = depreciation;
	}

	

	public String getInstalled() {
		return this.installed;
	}

	public void setInstalled(String installed) {
		this.installed = installed;
	}

	public Date getInstalledDate() {
		return this.installedDate;
	}

	public void setInstalledDate(Date installedDate) {
		this.installedDate = installedDate;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModelNumber() {
		return this.modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getReceivedDate() {
		return this.receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getReceivedNumber() {
		return this.receivedNumber;
	}

	public void setReceivedNumber(String receivedNumber) {
		this.receivedNumber = receivedNumber;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getTechnicalSpecification() {
		return this.technicalSpecification;
	}

	public void setTechnicalSpecification(String technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public String getWarranty() {
		return this.warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public List<AppEquipmentAccessory> getAppEquipmentAccessories() {
		return this.appEquipmentAccessories;
	}

	public void setAppEquipmentAccessories(List<AppEquipmentAccessory> appEquipmentAccessories) {
		this.appEquipmentAccessories = appEquipmentAccessories;
	}

	public AppEquipmentAccessory addAppEquipmentAccessory(AppEquipmentAccessory appEquipmentAccessory) {
		getAppEquipmentAccessories().add(appEquipmentAccessory);
		appEquipmentAccessory.setAppEquipmentDt(this);

		return appEquipmentAccessory;
	}

	public AppEquipmentAccessory removeAppEquipmentAccessory(AppEquipmentAccessory appEquipmentAccessory) {
		getAppEquipmentAccessories().remove(appEquipmentAccessory);
		appEquipmentAccessory.setAppEquipmentDt(null);

		return appEquipmentAccessory;
	}

	public AppEquipmentInfo getAppEquipmentInfos() {
		return appEquipmentInfos;
	}

	public void setAppEquipmentInfos(AppEquipmentInfo appEquipmentInfos) {
		this.appEquipmentInfos = appEquipmentInfos;
	}

	public MasDepartment getMasDepartment() {
		return this.masDepartment;
	}

	public void setMasDepartment(MasDepartment masDepartment) {
		this.masDepartment = masDepartment;
	}

	

	public AppEquipmentHd getAppEquipmentHd() {
		return appEquipmentHd;
	}

	public void setAppEquipmentHd(AppEquipmentHd appEquipmentHd) {
		this.appEquipmentHd = appEquipmentHd;
	}

	public String getBoardOut() {
		return boardOut;
	}

	public void setBoardOut(String boardOut) {
		this.boardOut = boardOut;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getBoardOutDate() {
		return boardOutDate;
	}

	public void setBoardOutDate(Date boardOutDate) {
		this.boardOutDate = boardOutDate;
	}

	public MasStoreSupplier getMasStoreSupplier() {
		return masStoreSupplier;
	}

	public void setMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		this.masStoreSupplier = masStoreSupplier;
	}

}