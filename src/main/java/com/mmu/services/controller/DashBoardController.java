package com.mmu.services.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.DashBoardService;


@RestController
@CrossOrigin
@RequestMapping("/dashboard")
public class DashBoardController {
	
	@Autowired
	DashBoardService dashService;
	@RequestMapping(value="/getDashBoardData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getDashBoardData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return dashService.getDashBoardData(map,request,response);
	}
	
	@RequestMapping(value="/getHomePageData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getHomePageData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return dashService.getHomePageData(map,request,response);
	}
	
	@RequestMapping(value="/getPandmicZoneData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getPandmicZoneData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return dashService.getPandmicZoneData(map,request,response);
	}
	
	@RequestMapping(value="/getInvoiceData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getInvoiceData(@RequestBody HashMap<String, Object> map,  HttpServletResponse response) {
		return dashService.getInvoiceData(map,response);
	}
	@RequestMapping(value="/getUpssInvoiceData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getUpssInvoiceData(@RequestBody HashMap<String, Object> map,  HttpServletResponse response) {
		return dashService.getUpssInvoiceData(map,response);
	}
	@RequestMapping(value="/getUpssMedicineInvoiceData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getUpssMedicineInvoiceData(@RequestBody HashMap<String, Object> map,  HttpServletResponse response) {
		return dashService.getMedicineInvoiceData(map,response);
	}

	@RequestMapping(value="/getAuthorityWiseData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getAuthorityWiseData(@RequestBody HashMap<String, Object> map,  HttpServletResponse response) {
		return dashService.getAuthorityWiseData(map,response);
	}

	@RequestMapping(value="/getAuditDashBoardData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getAuditDashBoardData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return dashService.getAuditDashBoardData(map,request,response);
	}
	


	@RequestMapping(value="/getAuditDashBoardAuditorData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getAuditDashBoardAuditorData(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return dashService.getAuditDashBoardAuditorData(map,request,response);
	}
	
	@RequestMapping(value="/getMMUMedicineData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getMMUMedicineData(@RequestBody HashMap<String, Object> map,  HttpServletResponse response) {
		return dashService.getMMUMedicineData(map,response);
	}


	@RequestMapping(value="/getAllInvoiceDetail", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getAllInvoiceDetail(@RequestBody Map<String, Object> map, HttpServletRequest request,  HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(map);
		return dashService.getAllInvoiceDetail(jsonObject, request, response);
	}
}
