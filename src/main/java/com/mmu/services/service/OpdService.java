package com.mmu.services.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mmu.services.entity.MasAudit;

@Service
public interface OpdService {

	Map<String, Object> getWaitingPatientList(Map mapForDS);

	String addVitalPreConsulataionDetails(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response);

	String PreConsPatientWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String PreConsPatientWatingListMapped(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String OpdPatientWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String searchPatientWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	//String idealWeight(HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response);

	String OpdPatientDetails(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String familyHistoryDetails(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String saveOpdPatientDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String saveOpdTemplates(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);

	String saveOpdTreatementTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String saveInvestigationDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String saveReferalDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	//String getOpdPreviousVisitRecord(HashMap<String, String> jsondata, HttpServletRequest request,
			//HttpServletResponse response);
	
	String getOpdPreviousVitalRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getObesityWaitingList(HashMap<String, Object> jsondata);	

	String getObesityDetails(HashMap<String, Object> jsondata);	
	
	String idealWeight(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String saveObesityDetails(HashMap<String, Object> jsondata);
	
	String referredPatientList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String referredPatientDetail(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String updateReferralDetail(HashMap<String, Object> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String getAdmissionDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String dischargeMain(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String admissionMain(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String savePatientAdmission(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String getServiceWisePatientList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String saveNewAdmission(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String nursingCareWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	String getNursingCareDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getProcedureDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);  
	
	String saveProcedureDetail(Map<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String getPendingDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String physioTherapyWaitingList(Map<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getphysioTherapyDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getOpdReportsDetailsbyServiceNo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getOpdReportsDetailsbyPatinetId(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getOpdReportsDetailsbyVisitId(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getExaminationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getInvestigationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getTreatmentPatientDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String getPatientHistoryDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String getPatientReferalDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String submitPatientRecall(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response);
	
	String deleteGridRow(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getPocedureDetailRecall(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	///////////////////////by dhiraj ////////////////
	String minorSurgeryWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	String getMinorSurgeryDetail(HashMap<String, String> jsondata, HttpServletRequest request,
	HttpServletResponse response);
	
	String getAnesthesiaList(HttpServletRequest request, HttpServletResponse response);
	String saveMinorSurgery(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	String deleteMinorSurgery(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	
	String authenticateUser(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String showCurrentMedication(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	 String checkForAuthenticateUser(HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response);

	String updateCurrentMedication(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String updateOpdInvestigationTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String deleteOpdInvestigationTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String validateMinorSurgery(HashMap<String, Object> jsondata, HttpServletRequest request,HttpServletResponse response);

	String getPreviousInvestigationAndResult(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response);

	String updateOpdTreatmentTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getOpdPreviousVisitRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String saveOpdMedicalAdviceTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String saveorUpdateChildImunization(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getChildImunizationRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	/*String checkAuthenticateEHR(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String authenticateUHID(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);*/

	String getPatientIdUHIDWise(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	Map<String, Object> patientListForEmployeeAndDependent(Map<String, String> requestData);

	String getRankList(JSONObject jsonObject);

	String getGenderList(JSONObject jsonObject);

	String getUnitList(JSONObject jsonObject);

	String rejectOpdWaitingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getPatientByServiceNo(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	String getAdmissionDischargeRegister(Map<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String saveObesityEntry(HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response);

	String getPatientSympotons(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String deletePatientSymptom(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getPatientDianosisDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getPatientHistoryRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getOpdPreviousAuditorRemarks(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	

	
}
