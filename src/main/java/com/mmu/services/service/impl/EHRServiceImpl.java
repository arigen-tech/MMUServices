package com.mmu.services.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmu.services.dao.impl.EHRDaoImpl;
import com.mmu.services.service.EHRService;


@Service
public class EHRServiceImpl implements EHRService {
	
	@Autowired
	EHRDaoImpl ehrDao;

	@Override
	public String getPatientSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		JSONArray jArray = new JSONArray();
		//List<Integer> list
		Map<String,Object> map = (Map<String,Object>)ehrDao.getPatientSummary(jsondata, request, response);
		List list = ehrDao.getPatientTotalVisit(jsondata);
		for(Object object : list) {
			jArray.put(object.toString());
		}
		JSONObject result = (JSONObject)map.get("patient_summary");
		jsonResponse.put("patient_summary", result);
		jsonResponse.put("visit_array", jArray);
		System.out.println("jsonResponse "+jsonResponse.toString());
		return jsonResponse.toString();
	}

	@Override
	public String getVisitSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)ehrDao.getVisitSummary(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("visit_summary");
		jsonResponse.put("visit_summary", result);
		return jsonResponse.toString(); 
	}
	
	

}
