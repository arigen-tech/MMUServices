package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.sql.Timestamp;

/**	
 * The persistent class for the MAS_STORE_ITEM database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_ITEM")
@NamedQuery(name="MasStoreItem.findAll", query="SELECT m FROM MasStoreItem m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_STORE_ITEM_ITEMID_GENERATOR", sequenceName="MAS_STORE_ITEM_SEQ", allocationSize=1)
public class MasStoreItem implements Serializable {

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((nomenclature == null) ? 0 : nomenclature.hashCode());
		result = prime * result + ((pvmsNo == null) ? 0 : pvmsNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MasStoreItem other = (MasStoreItem) obj;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (nomenclature == null) {
			if (other.nomenclature != null)
				return false;
		} else if (!nomenclature.equals(other.nomenclature))
			return false;
		if (pvmsNo == null) {
			if (other.pvmsNo != null)
				return false;
		} else if (!pvmsNo.equals(other.pvmsNo))
			return false;
		return true;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -3865545846568127047L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_STORE_ITEM_ITEMID_GENERATOR")
	@Column(name="ITEM_ID")
	private Long itemId;

	@Column(name="DISP_UNIT_ID")
	private Long dispUnitId;

	@Column(name="DISP_UNIT_QTY")
	private Long dispUnitQty;

	private String expiry;

	@Column(name="GROUP_ID")
	private Long groupId;

	@Column(name="HOSPITAL_ID")
	private Long hospitalId;

	@Column(name="ITEM_CLASS_ID")
	private Long itemClassId;

	@Column(name="ITEM_TYPE_ID")
	private Long itemTypeId;

	@Column(name="ITEM_UNIT_ID")
	private Long itemUnitId;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;
	@Column(name="NOMENCLATURE")
	 public  String nomenclature;

	@Column(name="PVMS_NO")
	private String pvmsNo;

	@Column(name="ROL_D")
	private Long rolD;
	
	@Column(name="ROL_S")
	private Long rolS;
	
	@Column(name="TYPE_OF_ITEM")
	private String typeOfItem;
	
	@Column(name="DANGEROUS_DRUG")
	private String dangerousDrug;
	
	
	@Column(name="EDL")
	private String edl;
	
	@Column(name="SASTI_DAWAI")
	private String sastiDawai;
	
	@Column(name="facility_code")
	private String facilityCode;
	
	//bi-directional many-to-one association to StoreQuotationT
	@OneToMany(mappedBy="masStoreItem")
	private List<StoreQuotationT> storeQuotationTmasStoreItem;

	   //bi-directional many-to-one association to MasItemClass
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ITEM_CLASS_ID",nullable=false,insertable=false,updatable=false)
		private MasItemClass masItemClass;

		//bi-directional many-to-one association to MasItemType
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ITEM_TYPE_ID",nullable=false,insertable=false,updatable=false)
		private MasItemType masItemType;

		//bi-directional many-to-one association to MasStoreSection
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="SECTION_ID",nullable=false,insertable=false,updatable=false)
		private MasStoreSection masStoreSection;

		//bi-directional many-to-one association to MasStoreUnit
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="DISP_UNIT_ID",nullable=false,insertable=false,updatable=false)
		private MasStoreUnit masStoreUnit;
		
		//bi-directional many-to-one association to MasStoreUnit
				@ManyToOne(fetch=FetchType.LAZY)
				@JoinColumn(name="ITEM_UNIT_ID",nullable=false,insertable=false,updatable=false)
				private MasStoreUnit masStoreUnit1;
		
		//bi-directional many-to-one association to MasFrequency
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="frequency_id",nullable=false,insertable=false,updatable=false)
		private MasFrequency masFrequency;					
				
		//bi-directional many-to-one association to StoreInternalIndentT
		@OneToMany(mappedBy="masStoreItem")
		private List<StoreInternalIndentT> storeInternalIndentTs;
		
		@OneToMany(mappedBy="masStoreItem")
		private List<StoreItemBatchStock> storeItemBatchStock;

		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="GROUP_ID",nullable=false,insertable=false,updatable=false)
		private MasStoreGroup masStoreGroup;
		
	public List<StoreInternalIndentT> getStoreInternalIndentTs() {
			return storeInternalIndentTs;
		}

		public void setStoreInternalIndentTs(List<StoreInternalIndentT> storeInternalIndentTs) {
			this.storeInternalIndentTs = storeInternalIndentTs;
		}

		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="HOSPITAL_ID",nullable=false,insertable=false,updatable=false)
		private MasHospital masHospital;
		
		
	public List<StoreQuotationT> getStoreQuotationTmasStoreItem() {
			return storeQuotationTmasStoreItem;
		}

		public void setStoreQuotationTmasStoreItem(List<StoreQuotationT> storeQuotationTmasStoreItem) {
			this.storeQuotationTmasStoreItem = storeQuotationTmasStoreItem;
		}

	

	public Long getItemId() {
			return itemId;
		}

		public void setItemId(Long itemId) {
			this.itemId = itemId;
		}

	public Long getDispUnitId() {
		return dispUnitId;
	}

	public void setDispUnitId(Long dispUnitId) {
		this.dispUnitId = dispUnitId;
	}

	public Long getDispUnitQty() {
		return dispUnitQty;
	}

	public void setDispUnitQty(Long dispUnitQty) {
		this.dispUnitQty = dispUnitQty;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getItemClassId() {
		return itemClassId;
	}

	public void setItemClassId(Long itemClassId) {
		this.itemClassId = itemClassId;
	}

	public Long getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(Long itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public Long getItemUnitId() {
		return itemUnitId;
	}

	public void setItemUnitId(Long itemUnitId) {
		this.itemUnitId = itemUnitId;
	}
	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}
	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Timestamp getLastChgDate() {
		return lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getNomenclature() {
		return nomenclature;
	}

	public void setNomenclature(String nomenclature) {
		this.nomenclature = nomenclature;
	}

	public String getPvmsNo() {
		return pvmsNo;
	}

	public void setPvmsNo(String pvmsNo) {
		this.pvmsNo = pvmsNo;
	}

	public Long getRolD() {
		return rolD;
	}

	public void setRolD(Long rolD) {
		this.rolD = rolD;
	}

	public Long getRolS() {
		return rolS;
	}

	public void setRolS(Long rolS) {
		this.rolS = rolS;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	@Column(name="SECTION_ID")
	private Long sectionId;

	private String status;

	private String temperature;

		
	public MasStoreUnit getMasStoreUnit() {
		return masStoreUnit;
	}

	public void setMasStoreUnit(MasStoreUnit masStoreUnit) {
		this.masStoreUnit = masStoreUnit;
	}
	
	public MasItemClass getMasItemClass() {
		return masItemClass;
	}

	public void setMasItemClass(MasItemClass masItemClass) {
		this.masItemClass = masItemClass;
	}

	public MasItemType getMasItemType() {
		return masItemType;
	}

	public void setMasItemType(MasItemType masItemType) {
		this.masItemType = masItemType;
	}

	public MasStoreSection getMasStoreSection() {
		return masStoreSection;
	}

	public void setMasStoreSection(MasStoreSection masStoreSection) {
		this.masStoreSection = masStoreSection;
	}

	public MasHospital getMasHospital() {
		return masHospital;
	}

	public void setMasHospital(MasHospital masHospital) {
		this.masHospital = masHospital;
	}

	public MasStoreGroup getMasStoreGroup() {
		return masStoreGroup;
	}

	public void setMasStoreGroup(MasStoreGroup masStoreGroup) {
		this.masStoreGroup = masStoreGroup;
	}
	
	public List<StoreItemBatchStock> getStoreItemBatchStock() {
		return storeItemBatchStock;
	}

	public void setStoreItemBatchStock(List<StoreItemBatchStock> storeItemBatchStock) {
		this.storeItemBatchStock = storeItemBatchStock;
	}

	public MasStoreUnit getMasStoreUnit1() {
		return masStoreUnit1;
	}

	public void setMasStoreUnit1(MasStoreUnit masStoreUnit1) {
		this.masStoreUnit1 = masStoreUnit1;
	}

	public String getTypeOfItem() {
		return typeOfItem;
	}

	public void setTypeOfItem(String typeOfItem) {
		this.typeOfItem = typeOfItem;
	}

	public String getDangerousDrug() {
		return dangerousDrug;
	}

	public void setDangerousDrug(String dangerousDrug) {
		this.dangerousDrug = dangerousDrug;
	}

	public String getEdl() {
		return edl;
	}

	public void setEdl(String edl) {
		this.edl = edl;
	}

	public String getSastiDawai() {
		return sastiDawai;
	}

	public void setSastiDawai(String sastiDawai) {
		this.sastiDawai = sastiDawai;
	}

	public String getFacilityCode() {
		return facilityCode;
	}

	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}
	
	@Column(name="frequency_id")
	private Long frequencyId;
	
	@Column(name="dosage")
	private String dosage;
	
	@Column(name="no_of_days")
	private Long noOfDays;

	public Long getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public Long getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Long noOfDays) {
		this.noOfDays = noOfDays;
	}

	public MasFrequency getMasFrequency() {
		return masFrequency;
	}

	public void setMasFrequency(MasFrequency masFrequency) {
		this.masFrequency = masFrequency;
	}
	@Column(name="INACTIVE_FOR_ENTRY")
	private String inactiveForEntry;

	public String getInactiveForEntry() {
		return inactiveForEntry;
	}

	public void setInactiveForEntry(String inactiveForEntry) {
		this.inactiveForEntry = inactiveForEntry;
	}
	
}