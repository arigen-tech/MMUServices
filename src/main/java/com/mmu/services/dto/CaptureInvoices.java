package com.mmu.services.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



public class CaptureInvoices implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4397360763612856701L;
	private String city;
	private Integer cityId;
	private String district;
	private Integer districtId;
	private Integer month;
	private Integer year;
	private List<InvoiceDetail> invoiceDetails;
	private String action;
	private Long userId;
	@JsonInclude(Include.NON_NULL)
	private String batchNo;
	@JsonInclude(Include.NON_NULL)
	private String userCityId;
	private Long headTypeId;
	private String phase;
	public CaptureInvoices(String city, Integer cityId, String district, Integer districtId, Integer month,
			Integer year, List<InvoiceDetail> invoiceDetails, String action, Long userId, String batchNo,Long headTypeId,String phase) {
		super();
		this.city = city;
		this.cityId = cityId;
		this.district = district;
		this.districtId = districtId;
		this.month = month;
		this.year = year;
		this.invoiceDetails = invoiceDetails;
		this.action = action;
		this.userId = userId;
		this.batchNo = batchNo;
		this.headTypeId=headTypeId;
		this.phase=phase;
	}
	public CaptureInvoices() {
		super();
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public List<InvoiceDetail> getInvoiceDetails() {
		return invoiceDetails;
	}
	public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getUserCityId() {
		return userCityId;
	}
	public void setUserCityId(String userCityId) {
		this.userCityId = userCityId;
	}
	public Long getHeadTypeId() {
		return headTypeId;
	}
	public void setHeadTypeId(Long headTypeId) {
		this.headTypeId = headTypeId;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	
	
}
