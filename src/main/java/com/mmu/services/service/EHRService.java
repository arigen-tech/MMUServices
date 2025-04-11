package com.mmu.services.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface EHRService {

	public String getPatientSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	public String getVisitSummary(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
}
