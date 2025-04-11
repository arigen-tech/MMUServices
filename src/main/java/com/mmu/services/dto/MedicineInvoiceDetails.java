package com.mmu.services.dto;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MedicineInvoiceDetails {

	@JsonProperty("batchNo")
	private BigInteger batch_no;
	@JsonProperty("billMonth")
	private BigInteger bill_month;
	@JsonProperty("billYear")
	private BigInteger bill_year;
	@JsonProperty("updatedDate")
	private Date last_chg_date;

	private String status;
	@JsonProperty("districtName")
	private String district_name;
	@JsonProperty("cityName")
	private String city_name;
	
	@JsonProperty("phase")
	private String phase;



	public Date getLast_chg_date() {
		return last_chg_date;
	}
	public void setLast_chg_date(Date last_chg_date) {
		this.last_chg_date = last_chg_date;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
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
	




}
