package com.mmu.services.service;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeRegistrationService {


	
	Map<String, Object> saveEmployee(Map<String, Object> requestData);
	String savedEmployeeList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String getEmployeeDetails(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	Map<String, Object> saveAPMAction(Map<String, Object> requestData);
	Map<String, Object> saveAUDAction(Map<String, Object> requestData);
	Map<String, Object> saveCHMOAction(Map<String, Object> requestData);
	Map<String, Object> saveUPSSAction(Map<String, Object> requestData);
	Map<String, Object> checkDuplicateMobile(Map<String, Object> requestData);
	Map<String, Object> getIdTypeList(Map<String, Object> requestData);
	String getAllUserTypeForEmpReg(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String pendingAttendanceList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	Map<String, Object> saveAttendance(Map<String, Object> requestData);
	String pendingListPhoto(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	Map<String, Object> savePhotoValidation(Map<String, Object> requestData);
	Map<String, Object> saveAttendanceAudit(Map<String, Object> requestData);
	Map<String, Object> getAttendanceYears(Map<String, Object> requestData);
	Map<String, Object> getAttendanceMonths(Map<String, Object> requestData);
	String getPenaltyList(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	String auditAttendanceHistory(Map<String, Object> requestData);
	Map<String, Object> checkDuplicateIMEI(Map<String, Object> requestData);
	String getEmpListForEdit(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String getEmployeeRecordForUpdate(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	Map<String, Object> updateEmployee(Map<String, Object> requestData);
		
}
