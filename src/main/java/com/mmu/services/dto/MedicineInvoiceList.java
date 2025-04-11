package com.mmu.services.dto;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MedicineInvoiceList 
{
	
	@JsonProperty("invoiceDate")
	private Date invoice_date;
	@JsonProperty("invoiceNum")
	private String invoice_no;	
	@JsonProperty("invoiceAmount")
	private BigInteger invoice_amount;	
	@JsonProperty("billYear")
	private BigInteger bill_year;
	@JsonProperty("billMonth")
	private BigInteger bill_month;
	@JsonProperty("medicineInvoiceId")
	private BigInteger medicine_invoice_id;
	@JsonProperty("batchNo")
	private BigInteger batch_no;
	
	private String status;
	@JsonProperty("districtName")
	private String district_name;
	@JsonProperty("cityName")
	private String city_name;
	@JsonProperty("sourceOfMedicine")
	private String supplier_type_name;
	
	
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public BigInteger getInvoice_amount() {
		return invoice_amount;
	}
	public void setInvoice_amount(BigInteger invoice_amount) {
		this.invoice_amount = invoice_amount;
	}
	public BigInteger getBill_year() {
		return bill_year;
	}
	public void setBill_year(BigInteger bill_year) {
		this.bill_year = bill_year;
	}
	public BigInteger getBill_month() {
		return bill_month;
	}
	public void setBill_month(BigInteger bill_month) {
		this.bill_month = bill_month;
	}
	public BigInteger getMedicine_invoice_id() {
		return medicine_invoice_id;
	}
	public void setMedicine_invoice_id(BigInteger medicine_invoice_id) {
		this.medicine_invoice_id = medicine_invoice_id;
	}
	public BigInteger getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(BigInteger batch_no) {
		this.batch_no = batch_no;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getSupplier_type_name() {
		return supplier_type_name;
	}
	public void setSupplier_type_name(String supplier_type_name) {
		this.supplier_type_name = supplier_type_name;
	}


}
