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

import com.mmu.services.service.EHRService;


@RestController
@CrossOrigin
@RequestMapping("/ehr")
public class EHRController {
	
	@Autowired
	EHRService ehrService;
	
	@RequestMapping(value="/getPatientSummary", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getPatientSummary(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return ehrService.getPatientSummary(map,request,response);
	}
	
	@RequestMapping(value="/getVisitSummary", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getVisitSummary(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return ehrService.getVisitSummary(map,request,response);
	}
}
