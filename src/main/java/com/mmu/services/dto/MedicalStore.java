package com.mmu.services.dto;

public class MedicalStore {

	private Long id;
	private String supplierCode;
	private String supplierName;
	
	public MedicalStore() {
	}
	public MedicalStore(Long id, String supplierCode, String supplierName) {
		super();
		this.id = id;
		this.supplierCode = supplierCode;
		this.supplierName = supplierName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	
}
