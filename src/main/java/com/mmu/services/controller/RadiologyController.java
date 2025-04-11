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

import com.mmu.services.service.RadiologyService;

@RestController
@CrossOrigin
@RequestMapping("/radiology")
public class RadiologyController {
	
	@Autowired
	RadiologyService radiologyService;
	
	/*
	 * @RequestMapping(value="/getResultValidation", method = RequestMethod.POST,
	 * produces="application/json", consumes="application/json") public String
	 * getResultValidation(@RequestBody HashMap<String, Object> map,
	 * HttpServletRequest request, HttpServletResponse response){ return
	 * radiologyService.getResultValidation(map); }
	 * 
	 * @RequestMapping(value="/getResultPrintingData", method = RequestMethod.POST,
	 * produces="application/json", consumes="application/json") public String
	 * getResultPrintingData(@RequestBody HashMap<String, Object> map,
	 * HttpServletRequest request, HttpServletResponse response){ return
	 * radiologyService.getResultPrintingData(map); }
	 * 
	 * @RequestMapping(value="/saveDocumentData", method = RequestMethod.POST,
	 * produces="application/json", consumes="application/json") public String
	 * saveDocumentData(@RequestBody HashMap<String, Object> map, HttpServletRequest
	 * request, HttpServletResponse response){ return
	 * radiologyService.saveDocumentData(map); }
	 */

}
