package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.EmployeeRegistration;
import com.mmu.services.entity.MasApplication;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasDesignation;
import com.mmu.services.entity.MasDesignationMapping;
import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.MasMmuVendor;
import com.mmu.services.entity.MasRole;
import com.mmu.services.entity.MasTemplate;
import com.mmu.services.entity.MasUserType;
import com.mmu.services.entity.RoleDesignation;
import com.mmu.services.entity.RoleTemplate;
import com.mmu.services.entity.TemplateApplication;
import com.mmu.services.entity.UserApplication;
import com.mmu.services.entity.Users;

@Repository
public interface UserManagementDao {
	
	Map<String, List<UserApplication>> getAllUserApplication(JSONObject jsonObject);
	List<UserApplication> validateUserApplication(String applicationName, String url);
	String saveUserApplication(JSONObject jsonObject);
	String updateUserApplication(JSONObject jsonObject);
	Map<String, List<MasTemplate>> getAllTemplate(JSONObject jsonObject);
	Map<String, Object> getApplicationAutoComplete(JSONObject jsonObject);
	String updateTemplate(JSONObject jsonObject);
	List<MasTemplate> validateTemplate(String templateCode, String templateName);
	//String saveTemplate(MasTemplate masTemplate);
	String saveTemplate(JSONObject jsonObject);
	List<MasTemplate> getTemplateList();
	Map<String, List<TemplateApplication>> getModuleNameTemplateWise(JSONObject jsonObject);
	List<MasApplication> getModuleListForTemplate();
	Map<String, Object> populateApplications(JSONObject jsonObject);
	boolean validateAddFormAndReports(MasApplication masApplication);
	String addFormAndReports(MasApplication masApplication, Long appID);
	String updateUserApplicationStatus(long appID);
	List<Users> getUserAndHospitalFromServiceNo(Long userId);
	List<TemplateApplication> getApplicationNameBasesOnRole(Object role);
	
	
	Map<String, Object> getAllApplicationAndTemplates(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String addTemplateApplication(TemplateApplication templateApplication);
	
	List<MasRole> getRoleRightsList();
	List<MasTemplate> getTemplateNameList();
	List<RoleTemplate> getAssingedTemplateNameList(JSONObject json);
	String saveRolesRight(JSONObject json);
	List<MasApplication> getApplicationNameFormsAndReport(JSONObject jsonObject);
	String updateAddFormsAndReport(MasApplication masApplication);
	
	List<MasDesignationMapping> getDesignationList(JSONObject jsonObject);
	Map<String, List<RoleDesignation>> getRoleAndDesignationMappingList(JSONObject jsonObject);
	String roleAndDesignationMapping(RoleDesignation roleDesignation);
	String updateRoleAndDesignationMapping(JSONObject jsonObject);
	
	Map<String, List<MasDesignation>> getAllDesignations(JSONObject jsonObject);
	String updateDesignationDetails(JSONObject jsonObject);
	List<MasDesignation> validateMasDesignation(String designationCode, String designationName);
	String addDesignation(MasDesignation masDesignation);
	Long saveRoleAndDesignation(RoleDesignation roleDesignation);
	
	List<RoleDesignation> getMultipleRoleAndDesignation(JSONObject jsonObject);
	String getMasDesignationByDesignationId(Long designationId);
	String getMasRoleByRoleId(String masRoles);
	
	Map<String, Object> getApplicationNameBasesOnRoleNew(Object role,String hospitalId);
	List<Long> getRolesList(Users users,String hospitalId);
	String getDesignationNameFromUser(String designationId);
	String existDesignationInMasRoleDesignationMapping( long designationId, Long locationId);
	List<MasApplication> getAllApp_Id();
	
	//Map<String, Object> getServiceNoLdapAuth(HashMap<String, Object> jsondata);
	List<MasApplication> getParentApplicationName(String ParentId);
	List<MasApplication> addFormsReportParentChildHierarchy(JSONObject jsonObject);
	Map<String, Object> getAllApplicationOfSelectedParent(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);
	boolean setSequenceToApplication(JSONObject jsonObject);
	Long getMaxCountOfChild(String parentId);
	String getApplicationIdParent(String string);
	
	//////////////////////////MMU Application //////////////////////
	
	List<MasDistrict> getDistrictList(Map<String, Object> requestData);
	List<MasCity> getCityList(Map<String, Object> requestData);
	List<MasMMU> getMMUList(Map<String, Object> requestData);
	Long saveRoleAndTypeOfUsers(Users user);
	List<Users> getUserDetails();
	Map<String, Object> getLoginDetails(HashMap<String, Object> jsondataLdap);
	List getCampDetais(String mmuid);
	Map<String, Object> getUserDetailsList(HashMap<String, Object> jsonData);
	Map<String, List<MasUserType>> getAllUserType(JSONObject jsondata);
	boolean isActiveInactiveUsers(String string, String string2);
	Users checkLoginCredential(String username, boolean status);
	String getMasDistrictId(String districtId);
	Users getUsersByUserId(Long userId);
	String getMasMmu(String mmuId);
	MasUserType getMasTypeOfUsers(Long typeOfUserId);
	String getMasCity(String cityId);
	String updateUsersDetails(HashMap<String, Object> jsonObject);
	MasMMU getCityIdAndName(String mmuid);
	EmployeeRegistration checkUserNameEmployee(String username, boolean b);
	String updateEmpDetails(JSONObject jsonObject);
	Users checkActiveUsers(String username, boolean status);
	String closeIdleTrans();
	Map<String, List<Object[]>> filterUsers(JSONObject jsondata);
	List<MasMmuVendor> getVendorList(Map<String, Object> requestData);
	String getMasVendor(String vendorId);
	Long getAllMAsApplicationMax();
	String getPatientValid(JSONObject jsondata);
	List getApprovingDetais(Long userTypeId);
	List<Users> getUsersList(HashMap<String, String> jsondata);
	 
}
