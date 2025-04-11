package com.mmu.services.service;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;

@Repository
public interface OpdMasterService {

	String departmentList(HashMap<String, Object> map);

	String getICD(HashMap<String, String> jsondata, HttpServletRequest request);

	String getInvestigation(HashMap<String, String> jsondata, HttpServletRequest request);

	String getMasStoreItem(HashMap<String, String> jsondata, HttpServletRequest request);

	String getMasFrequency(HashMap<String, String> jsondata, HttpServletRequest request);

	String getTemplateName(HashMap<String, String> jsondata, HttpServletRequest request);

	String getEmpanelledHospital(HashMap<String, String> jsondata, HttpServletRequest request);

	String getDisposalList(HashMap<String, String> jsondata, HttpServletRequest request);

	String getMasNursingCare(HashMap<String, Object> jsondata, HttpServletRequest request);
	
	String getMasItemClass(HashMap<String, Object> jsondata, HttpServletRequest request);

	String getTemplateInvestData(HashMap<String, Object> jsondata, HttpServletRequest request);

	String getTemplateTreatmentData(HashMap<String, Object> jsondata, HttpServletRequest request);

	String executeDbProcedure(String jsondata);

	String executeDbProcedureforStatistics(String jsondata);

	String getDispUnit(HashMap<String, Object> jsondata, HttpServletRequest request);

	String getMasStoreItemNip(HashMap<String, String> jsondata, HttpServletRequest request);
	
	String saveMasNursingCare(HashMap<String, Object> jsondata, HttpServletRequest request);
     
	String saveEmpanlledHospital(HashMap<String, Object> jsondata, HttpServletRequest request);
	
	//String executeDbProcedure(String jsondata) ;
	String executeProcedureForDashBoard(Map<String, Object> dashboardPayload, HttpServletRequest request, HttpServletResponse response);

	String getHospitalList(HashMap<String, String> jsondata, HttpServletRequest request);

	String getSpecialistList(HashMap<String, Object> map);


	String getICDListByName(HashMap<String, String> jsondata, HttpServletRequest request);

	String getTemplateMedicalAdviceData(HashMap<String, Object> jsondata, HttpServletRequest request);

	String geTreatmentInstruction(HashMap<String, Object> jsondata, HttpServletRequest request);

	int getAvailableStock(HashMap<String, Object> jsondata);

	
	

}
