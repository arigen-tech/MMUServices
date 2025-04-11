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

import com.mmu.services.service.AdminService;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;
		
	@RequestMapping(value="/getDepartmentList", method = RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getDepartmentList(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return adminService.getDepartmentList(map);	
		
	}
	
	@RequestMapping(value="/getDoctorList", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public String getDoctorList(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		
		return adminService.getDoctorList(map, request, response);
	}
	
/*	@RequestMapping(value="/getDoctorRoasterDetail", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public String getDoctorRoaster(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		
		return ad.getDoctorRoaster(map);
	}*/
	
	@RequestMapping(value="/getDoctorRoasterDetail", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public String getDoctorRoaster(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		
		//return ad.getDoctorRoaster(map);
		/*Map<String, Object> mapForDS = new HashMap<String, Object>();
		mapForDS = ad.getDoctorRoaster(map);*/
		return adminService.getDoctorRoaster(map);
	}
	
	
	@RequestMapping(value="/submitDepartmentRoaster", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public String submitDepartmentRoaster(@RequestBody HashMap<String, Object> map) {
		return adminService.submitDepartmentRoaster(map);
	}
	
	@RequestMapping(value="/getDepartmentListBasedOnDepartmentType", method = RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getDepartmentListBasedOnDepartmentType(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return adminService.getDepartmentListBasedOnDepartmentType(map);	
		
	}

}
