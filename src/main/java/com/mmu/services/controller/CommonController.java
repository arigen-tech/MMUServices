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

import com.mmu.services.service.CommonService;
import com.mmu.services.service.OpdMasterService;

@RequestMapping("/commonController")
@RestController
@CrossOrigin
public class CommonController {

	@Autowired
	CommonService cs;
	
	/*
	 * @RequestMapping(value = "/getRidcDocumentInfo", method = RequestMethod.POST)
	 * public String getRidcDocumentInfo(@RequestBody HashMap<String, String>
	 * jsondata, HttpServletRequest request) { return
	 * cs.getRidcDocumentInfo(jsondata, request); }
	 * 
	 * ////////Get Document for ME/MB ///////////////////////////////
	 * 
	 * @RequestMapping(value = "/getDocumentList", method = RequestMethod.POST)
	 * public String getDocumentList(@RequestBody HashMap<String, Object> map,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * cs.getDocumentList(map); }
	 * 
	 * @RequestMapping(value="/getSubInvestigationHtml",method=RequestMethod.POST)
	 * public String getSubInvestigationHtml(@RequestBody HashMap<String, Object>
	 * map, HttpServletRequest request, HttpServletResponse response) { return
	 * cs.getSubInvestigationHtml(map); }
	 * 
	 * @RequestMapping(value="/getInvestigationAndSubInvesForTemplate",method=
	 * RequestMethod.POST) public String
	 * getInvestigationAndSubInvesForTemplate(@RequestBody HashMap<String, Object>
	 * map, HttpServletRequest request, HttpServletResponse response) { return
	 * cs.getInvestigationAndSubInvesForTemplate(map); }
	 * 
	 * @RequestMapping(value="/getFamilyDetailsHistory",method=RequestMethod.POST)
	 * public String getFamilyDetailsHistory(@RequestBody HashMap<String, Object>
	 * map, HttpServletRequest request, HttpServletResponse response) { return
	 * cs.getFamilyDetailsHistory(map); }
	 * 
	 * @RequestMapping(value="/submitPatientImmunizationHistory",method=
	 * RequestMethod.POST) public String
	 * submitPatientImmunizationHistory(@RequestBody HashMap<String, Object> map,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * cs.submitPatientImmunizationHistory(map); }
	 * 
	 * @RequestMapping(value = "/saveItemCommon", method = RequestMethod.POST)
	 * public String saveItemCommon(@RequestBody HashMap<String, Object> map,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * cs.saveItemCommon(map); }
	 */
}
