package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public interface UserManagementService {

	String getAllUserApplication(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateUserApplication(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getAllTemplate(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getApplicationAutoComplete(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String addUserApplication(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateTemplate(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String addTemplate(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getTemplateList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getModuleNameTemplateWise(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getApplicationListForTemplate(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String addFormAndReports(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getAllApplicationAndTemplates(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String addTemplateApplication(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	String getRoleRightsList(HttpServletRequest request, HttpServletResponse response);
	String getTemplateNameList(HttpServletRequest request, HttpServletResponse response);
	String getAssingedTemplateNameList(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String saveRolesRight(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getApplicationNameFormsAndReport(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateAddFormsAndReport(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	Map<String, Object> getApplicationNameBasesOnRole(Map<String, Object> requestData);
	
	String getDesignationList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getRoleAndDesignationMappingList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String roleAndDesignationMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateRoleAndDesignationMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	String getAllDesignations(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateDesignationDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String addDesignation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String submitRoleAndDesignation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getMultipleRoleAndDesignation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	Map<String, Object> getApplicationNameBasesOnRoleNew(Map<String, Object> requestData);
	
	//String getServiceNoLdapAuth(HashMap<String, Object> jsondata, HttpServletRequest request,HttpServletResponse response);

	String getAllApplicationOfSelectedParent(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);
	String setSequenceToApplication(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	////////////////////////MMU Application //////////////////////////////////////////
	
	Map<String, Object> getDistrictList(Map<String, Object> requestData);
	Map<String, Object> getCityList(Map<String, Object> requestData);
	Map<String, Object> getMMUList(Map<String, Object> requestData);
	String submitRoleAndUsersType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	//String getUsersDetailsList(HashMap<String, String> jsondata, HttpServletRequest request);
	JSONObject getLoginDetails(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	String getUsersDetailsList(HashMap<String, Object> jsondata, HttpServletRequest request);
	String getAllUserType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String activeInactiveUsers(Map<String, Object> jsonData, HttpServletRequest httpServletRequest);
	String editUserDetails(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	//String sendSMS(String mobile, String message);
	String updateUsersRegistartionType(HashMap<String, Object> jsonObject, HttpServletRequest request,HttpServletResponse response);
	JSONObject checkUserName(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String sendOtp(HashMap<String, Object> jsondata, HttpServletRequest request);
	String verifyOtp(HashMap<String, Object> jsondata, HttpServletRequest request);
	JSONObject checkUserNameEmployee(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String sendSMS(String mobile, String name, String password);
	JSONObject closeIdleTranaction(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String getFilteredUsers(JSONObject jsondata);
	Map<String, Object> getVendorList(Map<String, Object> requestData);
	 
	String getAuthenticateUser(JSONObject jsondata);
	String getUsersList(HashMap<String, String> jsondata, HttpServletRequest request);
		
}
