package com.mmu.services.dao;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

@Repository
public interface EHRDao {
	
	public Map<String, Object>  getPatientSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response); 
	
	public Map<String, Object> getVisitSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

}
