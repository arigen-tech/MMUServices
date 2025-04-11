package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface DashBoardService {

	public String getDashBoardData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getHomePageData(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);

	public String getPandmicZoneData(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);
	
	public String getInvoiceData(HashMap<String, Object> map,HttpServletResponse response);
	public String getMedicineInvoiceData(HashMap<String, Object> map,HttpServletResponse response);
	
	public String getUpssInvoiceData(HashMap<String, Object> map,HttpServletResponse response);

	public String getAuthorityWiseData(HashMap<String, Object> map,HttpServletResponse response);

	public String getAuditDashBoardData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAuditDashBoardAuditorData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	public String getMMUMedicineData(HashMap<String, Object> jsondata, 
			HttpServletResponse response);
 

 
 
	 

 
	String getAllInvoiceDetail(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

}
