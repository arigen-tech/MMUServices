package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_ITEM_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_ITEM_TYPE")
@NamedQuery(name="MasItemType.findAll", query="SELECT m FROM MasItemType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_ITEM_TYPE_SEQ", sequenceName="MAS_ITEM_TYPE_SEQ", allocationSize=1)
public class MasItemType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_ITEM_TYPE_SEQ")
	@Column(name="ITEM_TYPE_ID")
	private long itemTypeId;

	@Column(name="ITEM_TYPE_CODE")
	private String itemTypeCode;

	@Column(name="ITEM_TYPE_NAME")
	private String itemTypeName;

	@Column(name="LAST_CHG_BY")
	private Long lastChgBy;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to MasStoreSection
		@OneToMany(mappedBy="masItemType")
		private List<MasStoreSection> masStoreSections;

	public MasItemType() {
	}

	public long getItemTypeId() {
		return this.itemTypeId;
	}

	public void setItemTypeId(long itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public String getItemTypeCode() {
		return this.itemTypeCode;
	}

	public void setItemTypeCode(String itemTypeCode) {
		this.itemTypeCode = itemTypeCode;
	}

	public String getItemTypeName() {
		return this.itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
	
	public Long getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public List<MasStoreSection> getMasStoreSections() {
		return this.masStoreSections;
	}

	public void setMasStoreSections(List<MasStoreSection> masStoreSections) {
		this.masStoreSections = masStoreSections;
	}

	public MasStoreSection addMasStoreSection(MasStoreSection masStoreSection) {
		getMasStoreSections().add(masStoreSection);
		masStoreSection.setMasItemType(this);

		return masStoreSection;
	}

	public MasStoreSection removeMasStoreSection(MasStoreSection masStoreSection) {
		getMasStoreSections().remove(masStoreSection);
		masStoreSection.setMasItemType(null);

		return masStoreSection;
	}

}