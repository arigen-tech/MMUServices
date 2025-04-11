package com.mmu.services.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.UserManagementService;

@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserManagementController {

	@Autowired
	UserManagementService userManagementService;
	
	@RequestMapping(value = "/getSDCMUserRegistration", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getSDCMUserTypeDetailsSDCM(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> sdcmList = new HashMap<String, Object>();
		if(requestData.get("typeValue").equals("Dist"))
		{	
		
			sdcmList = userManagementService.getDistrictList(requestData);
		if (sdcmList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		}
		else if(requestData.get("typeValue").equals("City"))
		{
			sdcmList = userManagementService.getCityList(requestData);
			if (sdcmList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
		}
		else if(requestData.get("typeValue").equals("MMU"))
		{
			sdcmList = userManagementService.getMMUList(requestData);
			if (sdcmList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
		}
		else if(requestData.get("typeValue").equals("Vendor"))
		{
			sdcmList = userManagementService.getVendorList(requestData);
			if (sdcmList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
		}
		else if(requestData.get("typeValue").equals("State"))
		{
			Map<String, Object> map2 = new HashMap<>();
			map2.put("id", "1");
			map2.put("code","001");
			map2.put("name", "Chhattisgarh");
			sdcmList=map2;
		}
		
		return new ResponseEntity<Map<String, Object>>(sdcmList, HttpStatus.OK);
	}	

	
	

	@RequestMapping(value = "/getAllUserApplication", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public String getAllUserApplication(@RequestBody Map<String, Object> requestMapObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestMapObject);
		return userManagementService.getAllUserApplication(jsonObject, request, response);
	}

	@RequestMapping(value = "/updateUserApplication", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public String updateUserApplication(@RequestBody Map<String, Object> requestMapObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestMapObject);
		return userManagementService.updateUserApplication(jsonObject, request, response);
	}

	@RequestMapping(value = "/addUserApplication", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public String addUserApplication(@RequestBody Map<String, Object> requestMapObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestMapObject);
		return userManagementService.addUserApplication(jsonObject, request, response);
	}

	@RequestMapping(value = "/addTemplate", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addTemplate(@RequestBody Map<String, Object> requestObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.addTemplate(jsonObject, request, response);
	}

	@RequestMapping(value = "/getAllTemplate", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllTemplate(@RequestBody Map<String, Object> requestObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getAllTemplate(jsonObject, request, response);
	}

	@RequestMapping(value = "/getTemplateList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getTemplateList(@RequestBody Map<String, Object> requestObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getTemplateList(jsonObject, request, response);
	}

	@RequestMapping(value = "/getModuleNameTemplateWise", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getModuleNameTemplateWise(@RequestBody Map<String, Object> requestObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getModuleNameTemplateWise(jsonObject, request, response);
	}

	@RequestMapping(value = "/getApplicationListForTemplate", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getApplicationListForTemplate(@RequestBody Map<String, Object> requestObject,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getApplicationListForTemplate(jsonObject, request, response);
	}

	@RequestMapping(value="/getAllApplicationAndTemplates", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllApplicationAndTemplates(@RequestBody Map<String, Object> requestObject,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getAllApplicationAndTemplates(jsonObject, request, response);
	}
	
	@RequestMapping(value = "/updateTemplate", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateTemplate(@RequestBody Map<String, Object> requestObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.updateTemplate(jsonObject, request, response);
	}

	@RequestMapping(value = "/getApplicationAutoComplete", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getApplicationAutoComplete(@RequestBody Map<String, Object> requestObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getApplicationAutoComplete(jsonObject, request, response);
	}
	
	@RequestMapping(value ="/addFormAndReports", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String addFormAndReports(@RequestBody Map<String, Object> requestObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.addFormAndReports(jsonObject, request, response);
	}
	
	@RequestMapping(value="/addTemplateApplication", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String addTemplateApplication(@RequestBody Map<String, Object> requestObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.addTemplateApplication(jsonObject, request, response);
	}
	
	/*************************************
	 * Role Rights
	 ***************************************************************/
	/**
	 * 
	 * //@param Roles Right
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/getRoleRightsList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getRoleRightList(HttpServletRequest request, HttpServletResponse response) {
		String roleList = "";
		roleList = userManagementService.getRoleRightsList(request, response);
		return roleList;
	}
	
	@RequestMapping(value = "/getTemplateNameList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getTemplateNameList(HttpServletRequest request, HttpServletResponse response) {
		String tempNameList = "";
		tempNameList = userManagementService.getTemplateNameList(request, response);
		return tempNameList;
	}
	
	
	@RequestMapping(value = "/getAssingedTemplateNameList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAssingedTemplateNameList(@RequestBody Map<String, Object> roleId, HttpServletRequest request, HttpServletResponse response) {
		String tempAssignNameList = "";
		JSONObject json = new JSONObject(roleId);
		tempAssignNameList = userManagementService.getAssingedTemplateNameList(json,request, response);
		return tempAssignNameList;
	}
	

	@RequestMapping(value = "/saveRolesRight", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String saveRolesRight(@RequestBody Map<String, Object> command, HttpServletRequest request,
			HttpServletResponse response) {
		String saveRole = "";
		JSONObject json = new JSONObject(command);
		saveRole = userManagementService.saveRolesRight(json, request, response);
		return saveRole;
	}
	
	@RequestMapping(value="/getApplicationNameFormsAndReport", method=RequestMethod.POST,produces="application/json", consumes="application/json")
	public String getApplicationNameFormsAndReport(@RequestBody Map<String, Object> requestObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getApplicationNameFormsAndReport(jsonObject, request, response);
	}
	
	@RequestMapping(value="/updateAddFormsAndReport", method=RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes={ MediaType.APPLICATION_JSON_VALUE})
	public String updateAddFormsAndReport(@RequestBody Map<String, Object> requestObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.updateAddFormsAndReport(jsonObject, request, response);
	}
	
	@RequestMapping(value = "/getApplicationNameBasesOnRole", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getApplicationNameBasesOnRole(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> dataList = new HashMap<String,Object>();
		dataList = userManagementService.getApplicationNameBasesOnRole(requestData);
		if (dataList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(dataList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getRoleAndDesignationMappingList", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String getRoleAndDesignationMappingList(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return userManagementService.getRoleAndDesignationMappingList(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getDesignationList", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String getDesignationList(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestdata);
		return userManagementService.getDesignationList(jsonObject, request, response);
	}
	
	@RequestMapping(value="/roleAndDesignationMapping", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String roleAndDesignationMapping(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return userManagementService.roleAndDesignationMapping(jsonObject,request,response);
	}
	
	@RequestMapping(value="/updateRoleAndDesignationMapping", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String updateRoleAndDesignationMapping(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return userManagementService.updateRoleAndDesignationMapping(jsonObject,request,response);
	}
	
	/*********************************************
	 * mas designation
	 ********************************************************/
	@RequestMapping(value = "/getAllDesignations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getAllDesignations(@RequestBody Map<String, Object> designationPayload, HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(designationPayload);
		return userManagementService.getAllDesignations(jsonObject, request, response);
	}
	
	@RequestMapping(value = "/updateDesignationDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String updateDesignationDetails(@RequestBody Map<String, Object> requestObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.updateDesignationDetails(jsonObject, request, response);
	}
	
	@RequestMapping(value = "/addDesignation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String addDesignation(@RequestBody Map<String, Object> requestObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.addDesignation(jsonObject, request, response);
	}
	
	
	@RequestMapping(value="/submitRoleAndDesignation", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String submitRoleAndDesignation(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return userManagementService.submitRoleAndDesignation(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getMultipleRoleAndDesignation", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String getMultipleRoleAndDesignation(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return userManagementService.getMultipleRoleAndDesignation(jsonObject,request,response);
	}

	@RequestMapping(value = "/getApplicationNameBasesOnRoleNew", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getApplicationNameBasesOnRoleNew(@RequestBody Map<String, Object> requestData,HttpServletRequest request) {
		Map<String, Object> dataList = new HashMap<String,Object>();
		dataList = userManagementService.getApplicationNameBasesOnRoleNew(requestData);
		HttpSession session=request.getSession();
		session.setAttribute("dataList", dataList);
		if (dataList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(dataList, HttpStatus.OK);
	}
	
		
	@RequestMapping(value="/getAllApplicationOfSelectedParent", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllApplicationOfSelectedParent(@RequestBody Map<String, Object> requestObject,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.getAllApplicationOfSelectedParent(jsonObject, request, response);
	}
	
	
	@RequestMapping(value="/setSequenceToApplication", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String setSequenceToApplication(@RequestBody Map<String, Object> requestObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(requestObject);
		return userManagementService.setSequenceToApplication(jsonObject, request, response);
	}
	
	@RequestMapping(value="/submitRoleAndUsersType", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String submitRoleAndUsersType(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return userManagementService.submitRoleAndUsersType(jsonObject,request,response);
	}
	

	@RequestMapping(value = "/getUsersDetailsList", method = RequestMethod.POST)
	public String getUsersDetailsList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request) {
		return userManagementService.getUsersDetailsList(jsondata, request);
	}
	
	@RequestMapping(value = "/getLoginDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getLoginDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		JSONObject js= userManagementService.getLoginDetails(jsondata, request, response);
		if (js.get("status").equals("1")) {
			//HttpSession session = request.getSession(true);
			
			return js.toString();
		} else {
			return js.toString();
		}
   }
	
	
	@RequestMapping(value = "/getAllUserType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllUserType(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String getAllUserType = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllUserType = userManagementService.getAllUserType(jsonObject, request, response);
		return getAllUserType;
	}
	
	@RequestMapping(value = "/activeInactiveUsers", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String activeInactiveEmployee(@RequestBody Map<String, Object> jsonData, HttpServletRequest httpServletRequest) {
		return userManagementService.activeInactiveUsers(jsonData, httpServletRequest);
	}
	
	@RequestMapping(value="/editUserDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String editUserDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return userManagementService.editUserDetails(jsondata, request, response);
	}
	
	@RequestMapping(value="/updateUsersRegistartionType", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String updateUsersRegistartionType(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return userManagementService.updateUsersRegistartionType(jsondata, request, response);
	}
	
	@RequestMapping(value = "/checkUserName", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String checkUserName(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		JSONObject js= userManagementService.checkUserName(jsondata, request, response);
		if (js.get("status").equals("1")) {
			//HttpSession session = request.getSession(true);
			
			return js.toString();
		} else {
			return js.toString();
		}
   }
	
	@RequestMapping(value="/sendOtp", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String sendOtp(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return userManagementService.sendOtp(jsondata, request);
	}
	
	@RequestMapping(value="/verifyOtp", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String verifyOtp(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return userManagementService.verifyOtp(jsondata, request);
	}
	
	@RequestMapping(value = "/checkUserNameEmployee", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String checkUserNameEmployee(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		JSONObject js= userManagementService.checkUserNameEmployee(jsondata, request, response);
		if (js.get("status").equals("1")) {
			//HttpSession session = request.getSession(true);
			
			return js.toString();
		} else {
			return js.toString();
		}
   }
	
	@RequestMapping(value ="/logoutsMMUServices", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String logOut(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		
		HttpSession httpSession = request.getSession(true);
		SecurityContextHolder.clearContext();
        if(httpSession != null) {
        	httpSession.invalidate();
       }
        //JSONObject js= userManagementService.closeIdleTranaction(jsondata, request, response);
		/*httpSession.removeAttribute("role_type");
		httpSession.removeAttribute("emp_id");
		httpSession.removeAttribute("token");
		httpSession.removeAttribute("name");
		httpSession.removeAttribute("username");*/
		return "success";
	}

	@RequestMapping(value = "/getFilteredUsers", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getFilteredUsers(@RequestBody Map<String, Object> jsonData) {
		return userManagementService.getFilteredUsers(new JSONObject(jsonData));
	}
	
	@RequestMapping(value = "/getAuthenticateUser", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAuthenticateUser(@RequestBody Map<String, Object> jsonData) {
		return userManagementService.getAuthenticateUser(new JSONObject(jsonData));
	}
}
