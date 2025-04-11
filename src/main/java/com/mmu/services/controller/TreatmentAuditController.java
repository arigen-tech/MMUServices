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

import com.mmu.services.service.TreatmentAuditService;

@RequestMapping("/treatmentAuditController")
@RestController
@CrossOrigin
public class TreatmentAuditController {

	@Autowired
	TreatmentAuditService treatmentAuditService;

	@RequestMapping(value = "/getAudittWatingList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAudittWatingList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getAudittWatingList(jsondata, request, response);
	}

	@RequestMapping(value = "/getPatientDianosisDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getPatientDianosisDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getPatientDianosisDetail(jsondata, request, response);
	}

	@RequestMapping(value = "/getInvestigationDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getInvestigationDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getInvestigationDetail(jsondata, request, response);
	}

	@RequestMapping(value = "/getTreatmentPatientDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getTreatmentPatientDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getTreatmentPatientDetail(jsondata, request, response);
	}

	//////////////////////////// save treatment audit details ///////////
	//////////////////////////// ////////////////////////

	@RequestMapping(value = "/saveTreatmentAuditDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String saveTreatmentAuditDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.saveTreatmentAuditDetails(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getRecommendedDiagnosisAllDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getRecommendedDiagnosisAllDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getRecommendedDiagnosisAllDetail(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getRecommendedInvestgationAllDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getRecommendedInvestgationAllDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getRecommendedInvestgationAllDetail(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getRecommendedTreatmentAllDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getRecommendedTreatmentAllDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getRecommendedTreatmentAllDetail(jsondata, request, response);
	}
	
	///////////////////// AI Implemention ////////////////////////////////////////
	@RequestMapping(value = "/getAIDiagnosisDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAIDiagnosisDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getAIDiagnosisDetail(jsondata, request, response);
	}

	@RequestMapping(value = "/getAIInvestgationDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAIInvestgationDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
	HttpServletResponse response) {
	return treatmentAuditService.getAIInvestgationDetail(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getAITreatmentDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAITreatmentDetail(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
	HttpServletResponse response) {
	return treatmentAuditService.getAITreatmentDetail(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getAllSymptomsForOpd", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSymptomsForOpd(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getAllSymptomsForOpd(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getAllIcdForOpd", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllIcdForOpd(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return treatmentAuditService.getAllIcdForOpd(jsondata, request, response);
	}
	
	@RequestMapping(value="/getExpiryMedicine", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getExpiryMedicine(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return treatmentAuditService.getExpiryMedicine(map,request,response);
	}
}
