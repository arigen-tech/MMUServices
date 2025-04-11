package com.mmu.services.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the MAS_STORE_SUPPLIER_TYPE database table.
 * 
 */
@Entity
@Table(name="MAS_STORE_SUPPLIER_TYPE")
@NamedQuery(name="MasStoreSupplierType.findAll", query="SELECT m FROM MasStoreSupplierType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_STORE_SUPPLIER_TYPE_SEQ", sequenceName="MAS_STORE_SUPPLIER_TYPE_SEQ",allocationSize=1)

public class MasStoreSupplierType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="MAS_STORE_SUPPLIER_TYPE_SEQ")
	@Column(name="SUPPLIER_TYPE_ID")
	private long supplierTypeId;

	@Column(name="LAST_CHG_DATE")
	private Timestamp lastChgDate;

	private String status;

	@Column(name="SUPPLIER_TYPE_CODE")
	private String supplierTypeCode;

	@Column(name="SUPPLIER_TYPE_NAME")
	private String supplierTypeName;

	//bi-directional many-to-one association to MasStoreSupplier
	@OneToMany(mappedBy="masStoreSupplierType")
	@JsonBackReference
	private List<MasStoreSupplier> masStoreSuppliers;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY")
	private Users lastChgBy;

	public MasStoreSupplierType() {
	}

	public long getSupplierTypeId() {
		return this.supplierTypeId;
	}

	public void setSupplierTypeId(long supplierTypeId) {
		this.supplierTypeId = supplierTypeId;
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

	public String getSupplierTypeCode() {
		return this.supplierTypeCode;
	}

	public void setSupplierTypeCode(String supplierTypeCode) {
		this.supplierTypeCode = supplierTypeCode;
	}

	public String getSupplierTypeName() {
		return this.supplierTypeName;
	}

	public void setSupplierTypeName(String supplierTypeName) {
		this.supplierTypeName = supplierTypeName;
	}

	public List<MasStoreSupplier> getMasStoreSuppliers() {
		return this.masStoreSuppliers;
	}

	public void setMasStoreSuppliers(List<MasStoreSupplier> masStoreSuppliers) {
		this.masStoreSuppliers = masStoreSuppliers;
	}

	public MasStoreSupplier addMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		getMasStoreSuppliers().add(masStoreSupplier);
		masStoreSupplier.setMasStoreSupplierType(this);

		return masStoreSupplier;
	}

	public MasStoreSupplier removeMasStoreSupplier(MasStoreSupplier masStoreSupplier) {
		getMasStoreSuppliers().remove(masStoreSupplier);
		masStoreSupplier.setMasStoreSupplierType(null);

		return masStoreSupplier;
	}

	public Users getLastChgBy() {
		return lastChgBy;
	}

	public void setLastChgBy(Users lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	

	

}