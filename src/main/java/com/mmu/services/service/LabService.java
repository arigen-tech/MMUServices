package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface LabService {

	/************************************************************* Sample Collection *************************************************/
	String getPendingSampleCollectionWaitingListGrid(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getPendingSampleCollection(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getPendingSampleCollectionDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String submitSampleCollectionDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	
	/********************************************************** Sample Validate ********************************************************/
	String getPendingSampleValidateListGrid(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getPendingSampleValidateList(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getPendingSampleValidateDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String submitSampleValidationDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	
	/************************************************************ Result Entry *********************************************************/
	String getResultEntryWaitingList(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getResultEntryDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	//String submitResultEntryDetails1(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String submitResultEntryDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getResultEntryWaitingListGrid(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	
	/********************************************************* Result Validation ******************************************************/
	String getResultValidationWaitingListGrid(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getResultValidationWaitingList(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getResultValidationDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String submitResultValidationDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	
	/********************************************************* Lab History ******************************************************/
	String getLabHistory(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getPatientList(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	
	/********************************************************* Result Update ******************************************************/
	String getResultUpdateWaitingList(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getResultEntryUpdateDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String updateResultEntryDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	
	/*********************************************** OutSide Patient Waiting List ********************************************/
	String getOutSidePatientWaitingList(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getOutSidePatientDetails(JSONObject jsonObject, HttpServletRequest request,HttpServletResponse response);
	String getSampleRejectedWaitingList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getSampleRejectedDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);
	String submitSampleRejectedDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String autheniticateUHID(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String checkAuthenticateUser(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String checkAuthenticateServiceNo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String getLabHistoryDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	Map<String, Object> getInvestigationList(Map<String, Object> requestData);
	
	
	
	
}
