package com.mmu.services.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.MedicalExamService;


@RequestMapping("/medicalexam")
@RestController
@CrossOrigin
public class MedicalExamRestController {
	
	@Autowired
	MedicalExamService medicalExamService;
	
	  
	
	  
	/*
	 * @RequestMapping(value="/getMEWaitingList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMEWaitingList(@RequestBody HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getMEWaitingList(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getPatientDetailToValidate", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getPatientDetailToValidate(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getPatientDetailToValidate(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/submitMedicalExamByMo", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitMedicalExamByMo(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.submitMedicalExamByMo(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/submitMedicalExamByMA", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitMedicalExamByMA(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.submitMedicalExamByMA(payload, request, response); }
	 * 
	 * @RequestMapping(value="/getAFMSF3BForMOOrMA", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getAFMSF3BForMOOrMA(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getAFMSF3BForMOOrMA(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getInvestigationAndResult") public String
	 * getInvestigationAndResult(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getInvestigationAndResult(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getMEApprovalWaitingGrid") public String
	 * getMEApprovalWaitingGrid(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getMEApprovalWaitingGrid(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getPatientDetailOfVisitId") public String
	 * getPatientDetailOfVisitId(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getPatientDetailOfVisitId(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getUnitDetail", method = RequestMethod.POST) public
	 * String getUnitDetail(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * return medicalExamService.getUnitDetail(payload, request, response); }
	 * 
	 * @RequestMapping(value="/getApprovalListByFlag", method = RequestMethod.POST)
	 * public String getApprovalListByFlag(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * return medicalExamService.getApprovalListByFlag(payload, request, response);
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getPatientReferalDetailMe", method =
	 * RequestMethod.POST) public String getPatientReferalDetailMe(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) { return
	 * medicalExamService.getPatientReferalDetailMe(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getImmunisationHistory") public String
	 * getImmunisationHistory(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getImmunisationHistory(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getMEWaitingGridAFMS18", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMEWaitingGridAFMS18(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getMEWaitingGridAFMS18(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getPatientDetailOfVisitIdAfms18", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getPatientDetailOfVisitIdAfms18(@RequestBody HashMap<String,
	 * Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * return medicalExamService.getPatientDetailOfVisitIdAfms18(jsondata, request,
	 * response); }
	 * 
	 * @RequestMapping(value="/submitMedicalExamByMAForm18", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitMedicalExamByMAForm18(@RequestBody HashMap<String,
	 * Object> payload, HttpServletRequest request, HttpServletResponse response) {
	 * return medicalExamService.submitMedicalExamByMAForm18(payload, request,
	 * response); }
	 * 
	 * @RequestMapping(value="/getServiceDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getServiceDetails(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getServiceDetails(payload, request, response); }
	 * 
	 * @RequestMapping(value="/getPatientDiseaseWoundInjuryDetail ", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getPatientDiseaseWoundInjuryDetail(@RequestBody HashMap<String,
	 * Object> payload, HttpServletRequest request, HttpServletResponse response) {
	 * return medicalExamService.getPatientDiseaseWoundInjuryDetail(payload,
	 * request, response); }
	 * 
	 * 
	 * @RequestMapping(value="/getMasEmployeeDetailForService ", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMasEmployeeDetailForService(@RequestBody HashMap<String,
	 * Object> payload, HttpServletRequest request, HttpServletResponse response) {
	 * return medicalExamService.getMasEmployeeDetailForService(payload, request,
	 * response); }
	 * 
	 * @RequestMapping(value="/getMasDesignationMappingByUnitId ", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMasDesignationMappingByUnitId(@RequestBody HashMap<String,
	 * Object> payload, HttpServletRequest request, HttpServletResponse response) {
	 * return medicalExamService.getMasDesignationMappingByUnitId(payload, request,
	 * response); }
	 * 
	 * @RequestMapping(value="/getInvestigationListUOM", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getInvestigationListUOM(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getInvestigationListUOM(jsondata, request, response); }
	 * 
	 * 
	 * @RequestMapping(value="/submitMedicalExamByMA3A", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitMedicalExamByMA3A(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.submitMedicalExamByMA3A(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getPatientDetailOfVisitIdAfms18P", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getPatientDetailOfVisitIdAfms18P(@RequestBody HashMap<String,
	 * Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * return medicalExamService.getPatientDetailOfVisitIdAfms18P(jsondata, request,
	 * response); }
	 * 
	 * @RequestMapping(value="/getMedicalExamListCommon", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMedicalExamListCommon(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getMedicalExamListCommon(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMEMBHistory", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMEMBHistory(@RequestBody HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.getMEMBHistory(jsondata, request, response); }
	 * 
	 * 
	 * @RequestMapping(value="/submitApprovalDate", method = RequestMethod.POST,
	 * produces="application/json", consumes="application/json") public String
	 * submitApprovalDate(@RequestBody HashMap<String, Object> map,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * medicalExamService.submitApprovalDate(map,request,response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getTemplateInvestDataForDiver", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getTemplateInvestDataForDiver(@RequestBody HashMap<String,
	 * Object> jsondata, HttpServletRequest request ) { return
	 * medicalExamService.getTemplateInvestDataForDiver(jsondata, request); }
	 */
}
