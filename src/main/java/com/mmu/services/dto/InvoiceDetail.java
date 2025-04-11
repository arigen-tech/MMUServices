package com.mmu.services.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class InvoiceDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7532546406116755787L;
	private Integer sourceOfMedicine;
	private Integer medicalStore;
	private String invoiceDate;
	private String invoiceNum;
	private Long inoviceAmount;
	private String fileName;
		
	public InvoiceDetail() {
		
	}
	public InvoiceDetail(Integer sourceOfMedicine, Integer medicalStore, String invoiceDate, String invoiceNum,
			Long inoviceAmount, String fileName, Long invoiceId) {
		this.sourceOfMedicine = sourceOfMedicine;
		this.medicalStore = medicalStore;
		this.invoiceDate = invoiceDate;
		this.invoiceNum = invoiceNum;
		this.inoviceAmount = inoviceAmount;
		this.fileName = fileName;
		this.invoiceId = invoiceId;
	}

	
	public Integer getSourceOfMedicine() {
		return sourceOfMedicine;
	}
	public void setSourceOfMedicine(Integer sourceOfMedicine) {
		this.sourceOfMedicine = sourceOfMedicine;
	}
	public Integer getMedicalStore() {
		return medicalStore;
	}
	public void setMedicalStore(Integer medicalStore) {
		this.medicalStore = medicalStore;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public Long getInoviceAmount() {
		return inoviceAmount;
	}
	public void setInoviceAmount(Long inoviceAmount) {
		this.inoviceAmount = inoviceAmount;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}
	@JsonInclude(value = Include.NON_NULL)
	private Long invoiceId;

	
	
	
}
