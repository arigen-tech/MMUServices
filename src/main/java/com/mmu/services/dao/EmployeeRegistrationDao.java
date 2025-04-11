package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MasIdentificationType;
import com.mmu.services.entity.MasUserType;



@Repository
public interface EmployeeRegistrationDao  {

	
	Map<String, Object> saveEmployee(Map<String, Object> requestData);	
	Map<String,Object> getEmpList(HashMap<String,Object> json);
	Map<String, Object> getEmployeeDetails(HashMap<String, String> jsondata);
	Map<String, Object> saveAPMAction(Map<String, Object> requestData);
	Map<String, Object> saveAUDAction(Map<String, Object> requestData);
	Map<String, Object> saveCHMOAction(Map<String, Object> requestData);
	Map<String, Object> saveUPSSAction(Map<String, Object> requestData);
	Map<String, Object> getEmployeeQualoificationDetails(HashMap<String, String> jsondata);
	Map<String, Object> getEmployeeDocumentDetails(HashMap<String, String> jsondata);
	Map<String, Object> checkDuplicateMobile(Map<String, Object> requestData);
	List<MasIdentificationType> getIdTypeList(Map<String, Object> requestData);
	Map<String, List<MasUserType>> getAllUserTypeForEmpReg(JSONObject jsondata);
	Map<String, Object> getPendingAttendanceList(HashMap<String, Object> jsondata);
	Map<String, Object> saveAttendance(Map<String, Object> requestData);
	Map<String, Object> pendingListPhoto(HashMap<String, Object> jsondata);
	Map<String, Object> savePhotoValidation(Map<String, Object> requestData);
	Map<String, Object> getAttendanceInfo(Long empId, String mobileNo);
	Map<String, Object> getCampInfo(Long mmuId, String attenDate);
	Map<String, Object> saveAttendanceAudit(Map<String, Object> requestData);
	List<Integer> getAttendanceYears(Map<String, Object> requestData);
	List<Integer> getAttendanceMonths(Map<String, Object> requestData);
	Map<String, Object> getPenaltyList(HashMap<String, Object> jsondata);
	Map<String, Object> auditAttendanceHistory(Map<String, Object> requestData);
	Map<String, Object> checkDuplicateIMEI(Map<String, Object> requestData);
	Map<String, Object> getEmpListForEdit(HashMap<String, Object> jsondata);
	Map<String, Object> getEmployeeRecordForUpdate(HashMap<String, String> jsondata);
	Map<String, Object> updateEmployee(Map<String, Object> requestData);
	
}
