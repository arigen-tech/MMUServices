package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the MAS_ITEM_CLASS database table.
 * 
 */
@Entity
@Table(name="MAS_ITEM_CLASS")
@NamedQuery(name="MasItemClass.findAll", query="SELECT m FROM MasItemClass m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_ITEM_CLASS_ITEMCLASSID_GENERATOR", sequenceName="mas_item_class_seq", allocationSize=1)
public class MasItemClass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_ITEM_CLASS_ITEMCLASSID_GENERATOR")
	@Column(name="ITEM_CLASS_ID")
	private long itemClassId;

	@Column(name="ITEM_CLASS_CODE")
	private String itemClassCode;

	@Column(name="ITEM_CLASS_NAME")
	private String itemClassName;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	//bi-directional many-to-one association to MasStoreSection
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SECTION_ID")
	private MasStoreSection masStoreSection;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users users;

	
	
	public MasItemClass() {
	}

	public long getItemClassId() {
		return this.itemClassId;
	}

	public void setItemClassId(long itemClassId) {
		this.itemClassId = itemClassId;
	}

	public String getItemClassCode() {
		return this.itemClassCode;
	}

	public void setItemClassCode(String itemClassCode) {
		this.itemClassCode = itemClassCode;
	}

	public String getItemClassName() {
		return this.itemClassName;
	}

	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
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

	public MasStoreSection getMasStoreSection() {
		return this.masStoreSection;
	}

	public void setMasStoreSection(MasStoreSection masStoreSection) {
		this.masStoreSection = masStoreSection;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

}