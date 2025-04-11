package com.mmu.services.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface AdminService {
	
	public String getDepartmentList(HashMap<String, Object> map);
	
	public String getDoctorList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);	
	
	public String getDoctorRoaster(HashMap<String, Object> jsondata);	
	public String submitDepartmentRoaster(HashMap<String, Object> jsondata);

	public String getDepartmentListBasedOnDepartmentType(HashMap<String, Object> map);
		
	
}
