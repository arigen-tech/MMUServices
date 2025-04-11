package com.mmu.services.controller;

/** Copyright 2019 JK Technosoft Ltd. All rights reserved.
* Use is subject to license terms.
* Purpose of the Asha-ICG -  This is for login.
* @author  Krishna Thakur
* Create Date: 14/01/2019 
* @version 0.1
*/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mmu.services.entity.MasAudit;
import com.mmu.services.service.OpdService;



@RequestMapping("/opd")
@RestController
@CrossOrigin

public class OpdController {
	
	@Autowired
	OpdService os;
	
	
Map<String, Object> map = new HashMap<String, Object>();
	
	////////////////// Get Waiting Patient List for Pre-Consultation Assessment /////////////////////////////
	
	@RequestMapping(value="/getPreConsPatientWatingList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String PreConsPatientWatingList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.PreConsPatientWatingList(jsondata, request, response);
	}
	
	@RequestMapping(value="/getPreConsPatientWatingListMapped", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String PreConsPatientWatingListMapped(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.PreConsPatientWatingListMapped(jsondata, request, response);
	}
	
	@RequestMapping(value="/searchPatientWatingList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String searchPatientWatingList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.searchPatientWatingList(jsondata, request, response);
	}
	
	/*@RequestMapping(value="/getIdealWeight", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String idealWeight(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.idealWeight(jsondata, request, response);
	}
	*/
	
	///////////////////Insert Pre-Consultation Assessment /////////////////////////
	
	@RequestMapping(value="/addVitalDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String addVitalPreConsulataionDetails(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return os.addVitalPreConsulataionDetails(payload, request, response);
	}
	
	@RequestMapping(value="/getOpdPatientWatingList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String OpdPatientWatingList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.OpdPatientWatingList(jsondata, request, response);
	}
	
	@RequestMapping(value="/getPatientDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String OpdPatientDetails(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.OpdPatientDetails(jsondata, request, response);
	}
	
	@RequestMapping(value="/getFamilyHistoryDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String familyHistoryDetails(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.familyHistoryDetails(jsondata, request, response);
	}
	
	//////////////////////////// save opd details controller 27th Feb 2019 ////////////////////////
	
	@RequestMapping(value="/saveOpdPatientDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveOpdPatientDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.saveOpdPatientDetails(jsondata, request, response);
	}
	
	
	////////////////////////////// Save OPD Template Controller 05th March 2019 add modify 01/05/2019///////////////////////
	
	@RequestMapping(value="/saveOpdTemplates", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveOpdTemplates(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.saveOpdTemplates(jsondata, request, response);
	}
	
	@RequestMapping(value="/saveOpdTreatementTemplates", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveOpdTreatementTemplates(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.saveOpdTreatementTemplates(jsondata, request, response);
	}
	
	@RequestMapping(value="/updateOpdInvestigationTemplates", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateOpdInvestigationTemplates(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.updateOpdInvestigationTemplates(jsondata, request, response);
	}
	
	@RequestMapping(value="/deleteOpdInvestigationTemplates", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String deleteOpdInvestigationTemplates(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.deleteOpdInvestigationTemplates(jsondata, request, response);
	}
		
	//////////////////// cpoy from HAL project---Get Waiting Patient List for OPD ///////////////////////////////
	
	@RequestMapping(value = "/saveInvestigationDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String saveInvestigationDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.saveInvestigationDetails(jsondata, request, response);
	}
	
	@RequestMapping(value = "/saveReferalDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String saveReferalDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.saveReferalDetails(jsondata, request, response);
	}
	
	///////////////////////////////// OPD Reports Sections //////////////////////////////
	
	@RequestMapping(value = "/getOpdReportsDetailsbyServiceNo", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getOpdReportsDetailsbyServiceNo(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.getOpdReportsDetailsbyServiceNo(jsondata, request, response);
	}
	
	
	@RequestMapping(value = "/getOpdReportsDetailsbyPatinetId", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getOpdReportsDetailsbyPatinetId(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.getOpdReportsDetailsbyPatinetId(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getOpdReportsDetailsbyVisitId", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getOpdReportsDetailsbyVisitId(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.getOpdReportsDetailsbyVisitId(jsondata, request, response);
	}
	
///////////////////////////////// OPD Previous Visit Sections //////////////////////////////
	
	@RequestMapping(value = "/getOpdPreviousVisitRecord", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getOpdPreviousVisitRecord(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.getOpdPreviousVisitRecord(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getOpdPreviousVitalRecord", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getOpdPreviousVitalRecord(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.getOpdPreviousVitalRecord(jsondata, request, response);
	}
	
	
	@RequestMapping(value = "/getPreviousInvestigationAndResult")
	public String getPreviousInvestigationAndResult(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return os.getPreviousInvestigationAndResult(payload, request, response);

	}
	
	
	@RequestMapping(value="/getObesityWaitingList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String  getObesityWaitingList(@RequestBody HashMap<String, Object> map){		
		
		String resultMap = os.getObesityWaitingList(map);
		
		return resultMap;
		
	}
	
	@RequestMapping(value="/getObesityDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String  getObesityDetails(@RequestBody HashMap<String, Object> map){		
		
		String resultMap = os.getObesityDetails(map);
		//System.out.println("resultMap "+resultMap);
		return resultMap;
		
	}
	
	@RequestMapping(value="/getIdealWeight", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getIdealWeight(@RequestBody HashMap<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		String idealWeight = os.idealWeight(map, request, response);
		//System.out.println("idealWeight "+idealWeight);
		return idealWeight;
	}
	
	@RequestMapping(value="/saveObesityDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveObesityDetails(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		String idealWeight = os.saveObesityDetails(map);
		return idealWeight;  
	}
	
	@RequestMapping(value="/referredPatientList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String referredPatientList(@RequestBody HashMap<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		String idealWeight = os.referredPatientList(map, request,response);
		return idealWeight;  
	}	
	
	@RequestMapping(value="/referredPatientDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String referredPatientDetail(@RequestBody HashMap<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		String referredPatientDetail = os.referredPatientDetail(map, request,response);
		return referredPatientDetail;  
	}
	
/*	@RequestMapping(value="/updateReferralDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateReferralDetail(@RequestBody List<HashMap<String, String>> list, HttpServletRequest request, HttpServletResponse response) {
		String referralDetailStatus = os.updateReferralDetail(list, request,response);
		return referralDetailStatus; 
	}*/
	
	@RequestMapping(value="/updateReferralDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateReferralDetail(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		String referralDetailStatus = os.updateReferralDetail(map, request,response);
		return referralDetailStatus; 
	}
	
	@RequestMapping(value="/getPendingDischargeList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPendingDischargeList(@RequestBody HashMap<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		String referralDetailStatus = os.getPendingDischargeList(map, request,response);		
		return referralDetailStatus; 
	}
	
	@RequestMapping(value="/getAdmissionDischargeList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getAdmissionDischargeList(@RequestBody HashMap<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		String referralDetailStatus = os.getAdmissionDischargeList(map, request,response);		
		return referralDetailStatus; 
	}
	
	@RequestMapping(value="/dischargeMain", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String dischargeMain(@RequestBody HashMap<String, String> map, HttpServletRequest request, HttpServletResponse response) {
 		String result = os.dischargeMain(map, request,response);		
		//System.out.println("result is "+result);
		return result; 
	}	
	
	@RequestMapping(value="/admissionMain", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String admissionMain(@RequestBody HashMap<String, String> map, HttpServletRequest request, HttpServletResponse response) {
 		String result = os.admissionMain(map, request,response);		
		//System.out.println("result is "+result);
		return result; 
	}
	
	@RequestMapping(value="/savePatientAdmission", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String savePatientAdmission(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response) {
		return os.savePatientAdmission(jsondata, request, response);
	}
	
	@RequestMapping(value="/getServiceWisePatientList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getServiceWisePatientList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response) {
		return os.getServiceWisePatientList(jsondata, request, response);
	}
	
	@RequestMapping(value="/saveNewAdmission", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveNewAdmission(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response) {
		return os.saveNewAdmission(jsondata, request, response);
	}
	
	@RequestMapping(value="/nursingCareWaitingList",  method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String nursingCareWaitingList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response) {
		String result = os.nursingCareWaitingList(jsondata, request, response);
		return os.nursingCareWaitingList(jsondata, request, response);
	}
	
	@RequestMapping(value="/getNursingCareDetail",  method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getNursingCareDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response) {
		//String result = os.nursingCareWaitingList(jsondata, request, response);
		return os.getNursingCareDetail(jsondata, request, response);
	}
	
	@RequestMapping(value="/getProcedureDetail",  method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getProcedureDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response) {
		//String result = os.nursingCareWaitingList(jsondata, request, response);
		return os.getProcedureDetail(jsondata, request, response);
	}
	
	@RequestMapping(value="/saveProcedureDetail",  method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveProcedureDetail(@RequestBody Map<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		//String result = os.nursingCareWaitingList(jsondata, request, response);
		return os.saveProcedureDetail(jsondata, request, response);
	}
	
	@RequestMapping(value="/physioTherapyWaitingList",  method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String physioTherapyWaitingList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response) {
		return os.physioTherapyWaitingList(jsondata, request, response);
	}
	
	@RequestMapping(value="/getphysioTherapyDetail",  method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getphysioTherapyDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response) {
		return os.getphysioTherapyDetail(jsondata, request, response);
	}
	
	
	//Added by Avinash
	
	
	@RequestMapping(value="/getExaminationDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getExaminationDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.getExaminationDetail(jsondata, request, response);
	}
	
	@RequestMapping(value="/getInvestigationDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getInvestigationDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.getInvestigationDetail(jsondata, request, response);
	}
	 
	@RequestMapping(value="/getTreatmentPatientDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getTreatmentPatientDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.getTreatmentPatientDetail(jsondata, request, response);
	}
	
	@RequestMapping(value="/submitPatientRecall", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String submitPatientRecall(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return os.submitPatientRecall(payload, request, response);
		
		
	}
	@RequestMapping(value="/getPatientHistoryDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPatientHistoryDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.getPatientHistoryDetail(jsondata, request, response);
	}
	
	@RequestMapping(value="/getPatientReferalDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPatientReferalDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.getPatientReferalDetail(jsondata, request, response);
	}
	@RequestMapping(value="/deleteGridRow", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String deleteGridRow(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.deleteGridRow(jsondata, request, response);
	}
	
	
	@RequestMapping(value="/getPocedureDetailRecall", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPocedureDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.getPocedureDetailRecall(jsondata, request, response);
	}
	
                             /////////////////////code by dhiraj ///////////////
	
	@RequestMapping(value = "/minorSurgeryWaitingList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String minorSurgeryWaitingList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String result = os.minorSurgeryWaitingList(jsondata, request, response);
		return os.minorSurgeryWaitingList(jsondata, request, response);
	}

	@RequestMapping(value = "/getMinorSurgeryDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getMinorSurgeryDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
        //String result = os.nursingCareWaitingList(jsondata, request, response);
       //System.out.println("result vitals :: "+os.getMinorSurgeryDetail(jsondata, request, response));
		return os.getMinorSurgeryDetail(jsondata, request, response);
	}

	@RequestMapping(value = "/getAnesthesiaList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAnesthesiaList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
           //String result = os.nursingCareWaitingList(jsondata, request, response);
		return os.getAnesthesiaList(request, response);
	}

	@RequestMapping(value = "/saveMinorSurgery", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String saveMinorSurgery(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.saveMinorSurgery(jsondata, request, response);

	}

	@RequestMapping(value = "/validateMinorSurgery", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String validateMinorSurgery(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.validateMinorSurgery(jsondata, request, response);

	}

	@RequestMapping(value = "/deleteMinorSurgery", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String deleteMinorSurgery(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.deleteMinorSurgery(jsondata, request, response);
	}

	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String authenticateUser(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.authenticateUser(jsondata, request, response);
	}

	@RequestMapping(value = "/showCurrentMedication", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String showCurrentMedication(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.showCurrentMedication(jsondata, request, response);
	}

	@RequestMapping(value = "/checkForAuthenticateUser", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String checkForAuthenticateUser(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.checkForAuthenticateUser(jsondata, request, response);
	}

	@RequestMapping(value = "/updateCurrentMedication", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateCurrentMedication(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.updateCurrentMedication(jsondata, request, response);
	}

	@RequestMapping(value="/updateOpdTreatmentTemplates", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateOpdTreatmentTemplates(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.updateOpdTreatmentTemplates(jsondata, request, response);
	}
	
	@RequestMapping(value="/saveOpdMedicalAdviceTemplates", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveOpdMedicalAdviceTemplates(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.saveOpdMedicalAdviceTemplates(jsondata, request, response);
	}
	
	@RequestMapping(value="/saveorUpdateChildImunization", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveorUpdateChildImunization(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return os.saveorUpdateChildImunization(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getChildImunizationRecord", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getChildImunizationRecord(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.getChildImunizationRecord(jsondata, request, response);
	}

	/*@RequestMapping(value = "/checkAuthenticateEHR", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String checkAuthenticateEHR(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.checkAuthenticateEHR(jsondata, request, response);
	}
 	
	@RequestMapping(value = "/authenticateUHID", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String authenticateUHID(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.authenticateUHID(jsondata, request, response);
	}*/
	
	@RequestMapping(value = "/getPatientIdUHIDWise", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getPatientIdUHIDWise(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return os.getPatientIdUHIDWise(jsondata, request, response);
	}
	
	 @RequestMapping(value = "/patientListForEmployeeAndDependent", method = RequestMethod.POST)
		public ResponseEntity<Map<String, Object>> patientListForUploadDocument(@RequestBody Map<String, String> requestData) {
			Map<String, Object> patientList = new HashMap<String,Object>();
			patientList = os.patientListForEmployeeAndDependent(requestData);
			if (patientList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<Map<String, Object>>(patientList, HttpStatus.OK);
		}
	 
	 @RequestMapping(value="/getRankList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String getRankList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return os.getRankList(jsonObject);
		}
	 
	 @RequestMapping(value="/getGenderList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String getGenderList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return os.getGenderList(jsonObject);
		}
	 
		@RequestMapping(value="/getUnitList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String getUnitList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return os.getUnitList(jsonObject);
		}
		
	///////////////Reject OpdWaiting List 26-Nov-2020 ///////////////////////
	@RequestMapping(value = "/rejectOpdWaitingList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String rejectOpdWaitingList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return os.rejectOpdWaitingList(jsondata, request, response);
	}
		
		@RequestMapping(value="/getPatientByServiceNo", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
		public String getPatientByServiceNo(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
			JSONObject jsonObject = new JSONObject(requestdata);
			return os.getPatientByServiceNo(jsonObject,request,response);
		}
		
		@RequestMapping(value="/getAdmissionDischargeRegister",  method = RequestMethod.POST,produces="application/json",consumes="application/json")
		public String getAdmissionDischargeRegister(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response) {
			return os.getAdmissionDischargeRegister(jsondata, request, response);
		}
		
		@RequestMapping(value="/saveObesityEntry", method = RequestMethod.POST,produces="application/json",consumes="application/json")
		public String saveObesityEntry(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response) {
			return os.saveObesityEntry(jsondata, request, response);
		}
		
		
		@RequestMapping(value="/getPatientSympotons", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
		public String getPatientSympotons(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
			JSONObject jsonObject = new JSONObject(requestdata);
			return os.getPatientSympotons(jsonObject,request,response);
		}
		
		@RequestMapping(value = "/deletePatientSymptom", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String deletePatientSymptom(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) 
		{
			return os.deletePatientSymptom(jsondata, request, response);
		}
		
		@RequestMapping(value="/getPatientDianosisDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
		public String getPatientDianosisDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
				HttpServletResponse response)
		{
			return os.getPatientDianosisDetail(jsondata, request, response);
		}
		
		@RequestMapping(value = "/getPatientHistoryRecord", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String getPatientHistoryRecord(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) 
		{
			return os.getPatientHistoryRecord(jsondata, request, response);
		}
		
		@RequestMapping(value = "/getOpdPreviousAuditorRecord", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String getOpdPreviousAuditorRecord(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) 
		{
			return os.getOpdPreviousAuditorRemarks(jsondata, request, response);
		}
}
