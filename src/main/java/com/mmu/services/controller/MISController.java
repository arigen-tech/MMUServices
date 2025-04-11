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

import com.mmu.services.service.MISService;


@RestController
@CrossOrigin
@RequestMapping("/mis")
public class MISController {	
		
	@Autowired
	MISService misService;
	
	@RequestMapping(value="/getDaiDidiClinicData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getDaiDidiClinicData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getDaiDidiClinicData(map,request,response);
	}
	
	@RequestMapping(value="/getLabourBeneficiaryData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getLabourBeneficiaryData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getLabourBeneficiaryData(map,request,response);
	}
	
	@RequestMapping(value="/getMMSSYInfoData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getMMSSYInfoData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getMMSSYInfoData(map,request,response);
	}
	
	@RequestMapping(value="/getDaiDidiClinicData2", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getDaiDidiClinicData2(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getDaiDidiClinicData2(map,request,response);
	}
	
	@RequestMapping(value="/getLabourBeneficiaryData2", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getLabourBeneficiaryData2(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getLabourBeneficiaryData2(map,request,response);
	}
	
	@RequestMapping(value="/getMMSSYInfoData2", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getMMSSYInfoData2(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getMMSSYInfoData2(map,request,response);
	}
	
	@RequestMapping(value="/aiAuditReport", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String aiAuditReport(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.aiAuditReport(map,request,response);
	}
	@RequestMapping(value="/edlReport", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String edlReport(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.edlReport(map,request,response);
	}

	@RequestMapping(value="/labReport", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String labReport(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.labReport(map,request,response);
	}

	@RequestMapping(value="/medicineIssueReport", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String medicineIssueReport(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.medicineIssueReport(map,request,response);
	}

	@RequestMapping(value="/edlReportCityWise", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String edlReportCityWise(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.edlReportCityWise(map,request,response);
	}
	
	@RequestMapping(value="/getAttendanceRegister", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getAttendanceRegister(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getAttendanceRegister(map,request,response);
	}
	
	@RequestMapping(value="/getAuditOpdRegister", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getAuditOpdRegister(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getAuditOpdRegister(map,request,response);
	}
	
	@RequestMapping(value="/getAttendanceInOutTime", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getAttendanceInOutTime(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getAttendanceInOutTime(map,request,response);
	}
	
	@RequestMapping(value="/getLabRegister", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getLabRegister(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return misService.getLabRegister(map,request,response);
	}

	

}
