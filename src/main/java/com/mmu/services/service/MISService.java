package com.mmu.services.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface MISService {

	public String getDaiDidiClinicData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getLabourBeneficiaryData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getMMSSYInfoData(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);

	public String getDaiDidiClinicData2(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	public String getLabourBeneficiaryData2(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	public String getMMSSYInfoData2(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	public String aiAuditReport(HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response);
	public String edlReport(HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response);

	public String labReport(HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response);

	public String medicineIssueReport(HashMap<String, Object> map, HttpServletRequest request,
									  HttpServletResponse response);

	public String edlReportCityWise(HashMap<String, Object> map, HttpServletRequest request,
									HttpServletResponse response);

	public String getAttendanceRegister(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	public String getAuditOpdRegister(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	public String getAttendanceInOutTime(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	public String getLabRegister(HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response);
}
