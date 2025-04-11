package com.mmu.services.service;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface PatientRegistrationService {


	/*
	 * Map<String,Object> findPatientAndDependentFromEmployee(Map<String, String>
	 * requestData); Map<String, Object>
	 * getRecordsFordepartmentBloodGroupAndMedicalCategory(Map<String, String>
	 * requestData); Map<String, Object>
	 * getTokenNoForDepartmentMultiVisit(Map<String, Object> requestData); String
	 * submitPatientDetails(String requestData); Map<String, Object>
	 * showAppointmentForOthers(Map<String, String> requestData); Map<String,
	 * Object> getTokenNoOfDepartmentForOthers(Map<String, Object> requestData);
	 * String submitPatientDetailsForOthers(String requestData); Map<String, Object>
	 * searchOthersRegisteredPatient(Map<String, String> requestData); Map<String,
	 * Object> patientListForUploadDocument(Map<String, String> requestData);
	 * Map<String, Object> patientAppointmentHistory(Map<String, String>
	 * requestData); Map<String, Object> patientVisitCancellation(Map<String,
	 * String> requestData); String getAppointmentTypeList(HttpServletRequest
	 * request, HttpServletResponse response); Map<String, Object>
	 * getExamSubType(Map<String, Object> requestData); Map<String, Object>
	 * getFutureAppointmentWaitingList(Map<String, Object> requestData); String
	 * getInvestigationEmptyResultByOrderNo(HashMap<String, Object>
	 * requestData,HttpServletRequest request, HttpServletResponse response); String
	 * submitInvestigationUp(HashMap<String, Object> map, HttpServletRequest
	 * request, HttpServletResponse response); String
	 * savePatientFromUploadDocument(String requestData); String
	 * opdEmergencySavePatient(String requestData); Map<String, Object>
	 * getStateFromDistrict(Map<String, Object> requestData); Map<String, Object>
	 * getRegionFromStation(Map<String, Object> requestData);
	 */
	Map<String, Object> getDistrictList(Map<String, Object> requestData);
	Map<String, Object> getCityList(Map<String, Object> requestData);
	Map<String, Object> getMMUList(Map<String, Object> requestData);
	Map<String, Object> createCampPlan(Map<String, Object> requestData);
	Map<String, Object> getWardList(Map<String, Object> requestData);
	Map<String, Object> getZoneList(Map<String, Object> requestData);
	Map<String, Object> getCampDetail(Map<String, Object> requestData);
	//Map<String, Object> savePatientRegistrationAndAppointment(Map<String, Object> requestData);
	Map<String, Object> getReligionList(Map<String, Object> requestData);	
	Map<String, Object> getBloodGroupList(Map<String, Object> requestData);
	Map<String, Object> getLabourTyeList(Map<String, Object> requestData);
	Map<String, Object> getIdentificationTypeList(Map<String, Object> requestData);
	Map<String, Object> getCampDepartment(Map<String, Object> requestData);
	Map<String, Object> updatePatientInformation(Map<String, Object> requestData);
	Map<String, Object> createPatientAndMakeAppointment(Map<String, Object> requestData);
	Map<String, Object> getMMUDepartment(Map<String, Object> requestData);
	Map<String, Object> getPatientList(Map<String, Object> requestData);
	Map<String, Object> getOnlinePatientList(Map<String, Object> requestData);
	Map<String, Object> getPatientDataBasedOnVisit(Map<String, Object> requestData);
	String saveVitalDetailsAndUpdateVisit(HashMap<String, Object> requestData);
	Map<String, Object> getWardListWithoutCity(Map<String, Object> requestData);
	Map<String, Object> getRelationList(Map<String, Object> requestData);
	Map<String, Object> getCityIdAndName(Map<String, Object> requestData);
	Map<String, Object> saveLabourRegistration(Map<String, Object> requestData);
	Map<String, Object> checkIfPatientIsAlreadyRegistered(Map<String, Object> requestData);
	Map<String, Object> deleteCampPlan(Map<String, Object> requestData);
	Map<String, Object> getFutureCampPlan(Map<String, Object> requestData);	
}
