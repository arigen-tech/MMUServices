package com.mmu.services.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface TreatmentAuditService {

	String getAudittWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getPatientDianosisDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getInvestigationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getTreatmentPatientDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String saveTreatmentAuditDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getRecommendedDiagnosisAllDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getRecommendedInvestgationAllDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getRecommendedTreatmentAllDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAIDiagnosisDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAIInvestgationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAITreatmentDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAllSymptomsForOpd(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAllIcdForOpd(HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response);

	String getExpiryMedicine(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	
}
