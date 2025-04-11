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

import com.mmu.services.service.MasterService;
import com.mmu.services.service.OpdMasterService;

/**
 * Copyright 2019 JK Technosoft Ltd. All rights reserved. Use is subject to
 * license terms. Purpose of the HMS - This is for login.
 * 
 * @author Krishna Thakur Create Date: 16/01/2019
 * @version 0.1
 */

@RequestMapping("/opdmaster")
@RestController
@CrossOrigin
public class OpdMasterController {

	@Autowired
	OpdMasterService ms;

	//////// Get Master Department List ///////////////////////////////

	@RequestMapping(value = "/getDepartmentList", method = RequestMethod.POST)
	public String getDepartmentList(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return ms.departmentList(map);
	}

	////////////////// Get Master ICD List /////////////////////////////

	@RequestMapping(value = "/getICDList", method = RequestMethod.POST)
	public String getICD(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getICD(jsondata, request);
	}
	
    //////////////////Get Master Hospital List /////////////////////////////

	@RequestMapping(value = "/getHospitalList", method = RequestMethod.POST)
	public String getHospitalList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getHospitalList(jsondata, request);
	}

	////////////////// Get Master ICD List /////////////////////////////

	@RequestMapping(value = "/getInvestigationList", method = RequestMethod.POST)
	public String getInvestigation(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getInvestigation(jsondata, request);
	}

	////////////////// Get Master MasStoreItem List
	////////////////// /////////////////////////////

	@RequestMapping(value = "/getMasStoreItem", method = RequestMethod.POST)
	public String getMasStoreItem(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getMasStoreItem(jsondata, request);
	}

	////////////////// Get Master MasStoreItem List //////////////////
	////////////////// /////////////////////////////

	@RequestMapping(value = "/getMasFrequency", method = RequestMethod.POST)
	public String getMasFrequency(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getMasFrequency(jsondata, request);
	}

	@RequestMapping(value = "/getTemplateName", method = RequestMethod.POST)
	public String getTemplateName(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getTemplateName(jsondata, request);
	}

	@RequestMapping(value = "/getTemplateInvestData", method = RequestMethod.POST)
	public String getTemplateInvestData(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.getTemplateInvestData(jsondata, request);
	}
	
	@RequestMapping(value = "/getTemplateTreatmentData", method = RequestMethod.POST)
	public String getTemplateTreatmentData(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.getTemplateTreatmentData(jsondata, request);
	}

	////////////////// Get Empanelled Hospital List //////////////////

	@RequestMapping(value = "/getEmpanelledHospital", method = RequestMethod.POST)
	public String getEmpanelledHospital(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getEmpanelledHospital(jsondata, request);
	}

	////////////////// Get Disposal List //////////////////

	@RequestMapping(value = "/getDisposalList", method = RequestMethod.POST)
	public String getDisposalList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getDisposalList(jsondata, request);
	}

	////////////////// Get MasNursingCare List //////////////////

	@RequestMapping(value = "/getMasNursingCare", method = RequestMethod.POST)
	public String getMasNursingCare(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.getMasNursingCare(jsondata, request);
	}
	
	@RequestMapping(value = "/getMasItemClass", method = RequestMethod.POST)
	public String getMasItemClass(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.getMasItemClass(jsondata, request);
	}
	
	@RequestMapping(value = "/getDispUnit", method = RequestMethod.POST)
	public String getDispUnit(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.getDispUnit(jsondata, request);
	}
	
	@RequestMapping(value = "/getMasStoreItemNip", method = RequestMethod.POST)
	public String getMasStoreItemNip(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getMasStoreItemNip(jsondata, request);
	}
	
	@RequestMapping(value = "/saveMasNursingCare", method = RequestMethod.POST)
	public String saveMasNursingCare(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.saveMasNursingCare(jsondata, request);
	}
	
	@RequestMapping(value = "/saveEmpanlledHospital", method = RequestMethod.POST)
	public String saveEmpanlledHospital(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.saveEmpanlledHospital(jsondata, request);
	}


	/*
	 * Method to Execute Stored Procedure: Kaushal Mishra
	 * 
	 * @RequestMapping(value = "/executeDbProcedure", method = RequestMethod.POST)
	 * public String executeDbProcedure(@RequestBody String jsondata,
	 * HttpServletRequest request) { return ms.executeDbProcedure(jsondata); }
	 */
	/* Method to Execute Stored Procedure: Kaushal Mishra */

	@RequestMapping(value = "/executeDbProcedure", method = RequestMethod.POST)
	public String executeDbProcedure(@RequestBody String jsondata, HttpServletRequest request){
		return ms.executeDbProcedure(jsondata);
	}
	
	
	@RequestMapping(value = "/executeDbProcedureforStatistics", method = RequestMethod.POST)
	public String executeDbProcedureforStatistics(@RequestBody String jsondata, HttpServletRequest request){
		return ms.executeDbProcedureforStatistics(jsondata);
	}
	
	//rajdeo dashboard
	//rajdeo
		@RequestMapping(value="/executeProcedureForDashBoard", method = RequestMethod.POST, produces="application/json", consumes="application/json")
		public String executeProcedureForDashBoard(@RequestBody HashMap<String, Object> dashboardPayload, HttpServletRequest request, HttpServletResponse response) {
			return ms.executeProcedureForDashBoard(dashboardPayload,request,response);
		}
		
    ////////Get Master Specialist List ///////////////////////////////

	@RequestMapping(value = "/getSpecialistList", method = RequestMethod.POST)
	public String getSpecialistList(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return ms.getSpecialistList(map);
	}
	
	@RequestMapping(value = "/getIcdListByName", method = RequestMethod.POST)
	public String getICDListByName(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
		return ms.getICDListByName(jsondata, request);
	}
	
	@RequestMapping(value = "/getTemplateMedicalAdviceData", method = RequestMethod.POST)
	public String getTemplateMedicalAdviceData(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.getTemplateMedicalAdviceData(jsondata, request);
	}
	
	@RequestMapping(value = "/geTreatmentInstruction", method = RequestMethod.POST)
	public String geTreatmentInstruction(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return ms.geTreatmentInstruction(jsondata, request);
	}
	
}
