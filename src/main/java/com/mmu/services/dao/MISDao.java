package com.mmu.services.dao;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

@Repository
public interface MISDao {

	public Map<String, Object> getDaiDidiClinicData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getLabourBeneficiaryData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getMMSSYInfoData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getDaiDidiClinicData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getLabourBeneficiaryData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getMMSSYInfoData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> aiAuditReport(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getdlReportData(HashMap<String, Object> jsondata, HttpServletRequest request,
										HttpServletResponse response);

	Map<String, Object> getLabReportData(HashMap<String, Object> jsondata, HttpServletRequest request,
										 HttpServletResponse response);

	Map<String, Object> getmedicineIssueReportData(HashMap<String, Object> jsondata, HttpServletRequest request,
												   HttpServletResponse response);

	Map<String, Object> edlReportCityWise(HashMap<String, Object> jsondata, HttpServletRequest request,
										  HttpServletResponse response);

	Map<String, Object> getAttendanceInOutTime(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getLabRegister(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

}
