package com.mmu.services.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.SystemAdminService;

@RestController
@CrossOrigin
@RequestMapping("/createAdmin")
public class SystemAdminRestController {
	@Autowired
	SystemAdminService systemAdminService;
	
	/*
	 * @RequestMapping(value = "/getMasHospitalListForAdmin", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getMasHospitalListForAdmin() { Map<String, Object> hospitalList = new
	 * HashMap<String, Object>(); hospitalList =
	 * systemAdminService.getMasHospitalListForAdmin(); if (hospitalList.isEmpty())
	 * { return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); }
	 * return new ResponseEntity<Map<String, Object>>(hospitalList, HttpStatus.OK);
	 * }
	 * 
	 * @RequestMapping(value="/getMasEmployeeDetail", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMasEmployeeDetail(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * systemAdminService.getMasEmployeeDetail(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getAllMasDesigation", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getAllMasDesigation(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * systemAdminService.getAllMasDesigation(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/submitUnitAdmin", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitUnitAdmin(@RequestBody HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * systemAdminService.submitUnitAdmin(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/submitMasDesignation", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitMasDesignation(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * systemAdminService.submitMasDesignation(jsondata, request, response); }
	 * 
	 * 
	 * @RequestMapping(value="/getUnitAdminDetail", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getUnitAdminDetail(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * systemAdminService.getUnitAdminDetail(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/activateDeActivatUser", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String activateDeActivatUser(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * System.out.println("activate and deactivate"); return
	 * systemAdminService.activateDeActivatUser(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMasDesinationDetail", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMasDesinationDetail(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * System.out.println("activate and deactivate"); return
	 * systemAdminService.getMasDesinationDetail(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/editDesignation", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String editDesignation(@RequestBody HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * System.out.println("activate and deactivate"); return
	 * systemAdminService.editDesignation(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getUnitAdminMasRole", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getUnitAdminMasRole(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * System.out.println("activate and deactivate"); return
	 * systemAdminService.getUnitAdminMasRole(jsondata, request, response); }
	 * 
	 * 
	 * @RequestMapping(value="/editUnitAdminUser", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String editUnitAdminUser(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * systemAdminService.editUnitAdminUser(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMasDesigationForUnitId", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMasDesigationForUnitId(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * System.out.println("getMasDesigationForUnitId"); return
	 * systemAdminService.getMasDesigationForUnitId(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getAllServiceByUnitId", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getAllServiceByUnitId(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * System.out.println("getAllServiceByUnitId"); return
	 * systemAdminService.getAllServiceByUnitId(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMasUnitListByUnitCode", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMasUnitListByUnitCode(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * System.out.println("getMasUnitListByUnitCode"); return
	 * systemAdminService.getMasUnitListByUnitCode(jsondata, request, response); }
	 */
}
