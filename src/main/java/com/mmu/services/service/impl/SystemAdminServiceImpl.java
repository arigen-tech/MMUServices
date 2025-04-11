package com.mmu.services.service.impl;

 

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.RoleTemplateDao;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.MasDesignation;
import com.mmu.services.entity.MasDesignationMapping;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRole;
import com.mmu.services.entity.MasTemplate;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.RoleTemplate;
import com.mmu.services.entity.Users;
import com.mmu.services.service.SystemAdminService;
import com.mmu.services.service.impl.OpdServiceImpl;
import com.mmu.services.utils.UtilityServices;
@Repository
@Transactional
public class SystemAdminServiceImpl implements SystemAdminService{
	
	@Autowired
	SystemAdminDao systemAdminDao;
	 
	/*
	 * @Autowired RoleTemplateDao roleTemplateDao;
	 * 
	 * @Override public Map<String, Object> getMasHospitalListForAdmin() {
	 * Map<String, Object> map = new HashMap<String, Object>(); List<Object>
	 * responseList = new ArrayList<Object>(); List<MasHospital> hospitalList = new
	 * ArrayList<MasHospital>(); hospitalList =
	 * systemAdminDao.getMasHospitalListForAdmin();
	 * 
	 * if (CollectionUtils.isNotEmpty(hospitalList) ) { for (MasHospital hospital :
	 * hospitalList) { HashMap<String, Object> jsonMap = new HashMap<String,
	 * Object>(); if(hospital!=null && hospital.getMasUnit()!=null &&
	 * hospital.getMasUnit().getUnitId()!=null) jsonMap.put("hospitalId",
	 * hospital.getMasUnit().getUnitId()); else { jsonMap.put("hospitalId", ""); }
	 * jsonMap.put("hospitalName", hospital.getHospitalName()); if(hospital!=null &&
	 * hospital.getMasUnit()!=null && hospital.getMasUnit().getUnitCode()!=null)
	 * jsonMap.put("unitCode", hospital.getMasUnit().getUnitCode());
	 * 
	 * else { jsonMap.put("unitCode",""); } if(hospital!=null &&
	 * hospital.getHospitalId()!=null ) jsonMap.put("hospitalIdForU",
	 * hospital.getHospitalId()); else { jsonMap.put("hospitalIdForU", ""); }
	 * responseList.add(jsonMap); } if (responseList != null && responseList.size()
	 * > 0) { map.put("data", responseList); map.put("status", 1); } else {
	 * map.put("data", responseList); map.put("msg", "Data not found");
	 * map.put("status", 0); } } return map; }
	 * 
	 * @Override public String getMasUnitListByUnitCode(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); List<Object> responseList = new
	 * ArrayList<Object>(); List<Object[]>listObject =
	 * systemAdminDao.getUnitListForAdminByUnitode(jsondata);
	 * 
	 * if (listObject != null) { for (Iterator<?> it = listObject.iterator();
	 * it.hasNext();) { HashMap<String, Object> jsonMap = new HashMap<String,
	 * Object>(); Object[] row = (Object[]) it.next(); Long unitId=null; String
	 * CENTITY=""; String DESCR=""; String MI_UNIT=""; if (row[0] != null) { unitId
	 * = Long.parseLong(row[0].toString()); } if (row[1] != null) { CENTITY =
	 * row[1].toString(); }
	 * 
	 * if (row[2] != null) { DESCR = row[2].toString(); } if (row[3] != null) {
	 * MI_UNIT = row[3].toString(); } jsonMap.put("unitId", unitId);
	 * jsonMap.put("CENTITY", CENTITY); jsonMap.put("DESCR", DESCR);
	 * jsonMap.put("MI_UNIT", MI_UNIT); responseList.add(jsonMap); }
	 * 
	 * if (responseList != null && responseList.size() > 0) { json.put("data",
	 * responseList); json.put("status", 1); } else { json.put("data",
	 * responseList); json.put("msg", "Data not found"); json.put("status", 0); } }
	 * 
	 * 
	 * 
	 * return json.toString(); }
	 * 
	 * 
	 * 
	 * @Override public String getMasEmployeeDetail(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * List<Object> responseList = new ArrayList<Object>(); JSONObject json = new
	 * JSONObject(); List<MasEmployee> listMasEmployee = null; List<HashMap<String,
	 * Object>> c = new ArrayList<HashMap<String, Object>>(); if
	 * (!jsondata.isEmpty()) { if (jsondata.get("serviceNo") != null &&
	 * jsondata.get("unitId")!=null ) { String flagCheck=
	 * jsondata.get("flag").toString(); MasHospital masHospital=new MasHospital();
	 * String hospitalId= jsondata.get("unitId").toString();
	 * masHospital.setHospitalId(Long.parseLong(hospitalId));
	 * 
	 * listMasEmployee =
	 * systemAdminDao.getMasEmployeeForAdmin(jsondata.get("serviceNo").toString(),
	 * masHospital,flagCheck ); }
	 * 
	 * 
	 * if (CollectionUtils.isNotEmpty(listMasEmployee)) { for (MasEmployee
	 * masEmployee : listMasEmployee) { HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("employeeId",
	 * masEmployee.getEmployeeId()); jsonMap.put("serviceNo",
	 * masEmployee.getServiceNo()); jsonMap.put("firstName",
	 * masEmployee.getEmployeeName()); jsonMap.put("lastName", "");
	 * jsonMap.put("middleName","");
	 * 
	 * if(masEmployee.getMasAdministrativeSex()!=null &&
	 * masEmployee.getMasAdministrativeSex().getAdministrativeSexName()!=null)
	 * jsonMap.put("gender",
	 * masEmployee.getMasAdministrativeSex().getAdministrativeSexName()); else {
	 * jsonMap.put("gender", ""); }
	 * 
	 * 
	 * if(masEmployee.getMasRank()!=null) { MasRank
	 * masRank=systemAdminDao.getRankByRankCode(masEmployee.getMasRank());
	 * if(masRank!=null) jsonMap.put("rank", masRank.getRankName()); else
	 * jsonMap.put("rank", ""); } else { jsonMap.put("rank", ""); }
	 * responseList.add(jsonMap); } if (responseList != null && responseList.size()
	 * > 0) { json.put("dataMasEmployee", responseList); json.put("status", 1); }
	 * else { json.put("data", responseList); json.put("msg", "Data not found");
	 * json.put("status", 0); } } else { try { json.put("msg",
	 * "Visit ID data not found"); json.put("status", 0); } catch (JSONException e)
	 * { e.printStackTrace(); } }
	 * 
	 * } return json.toString(); }
	 * 
	 * @Override public String getAllMasDesigation(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { List<Object>
	 * responseList = new ArrayList<Object>(); JSONObject json = new JSONObject();
	 * List<MasDesignation> listMasDesignation = null;
	 * 
	 * 
	 * List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
	 * 
	 * 
	 * listMasDesignation = systemAdminDao.getAllMasDesigation( );
	 * 
	 * if (CollectionUtils.isNotEmpty(listMasDesignation)) { for (MasDesignation
	 * masDesignation : listMasDesignation) { HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("designationId",
	 * masDesignation.getDesignationId()); jsonMap.put("designationName",
	 * masDesignation.getDesignationName()); responseList.add(jsonMap); } if
	 * (responseList != null && responseList.size() > 0) {
	 * json.put("dataMasDesignation", responseList); json.put("status", 1); } else {
	 * json.put("data", responseList); json.put("msg", "Data not found");
	 * json.put("status", 0); } } else { try { json.put("msg",
	 * "Visit ID data not found"); json.put("status", 0); } catch (JSONException e)
	 * { e.printStackTrace(); } } return json.toString(); }
	 * 
	 * @Override public String submitUnitAdmin(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { JSONObject json =
	 * new JSONObject(); try {
	 * 
	 * String serviceNo=jsondata.get("serviceNo").toString(); //Long
	 * unitId=Long.parseLong(jsondata.get("unitId").toString()); Long unitId=null;
	 * String unitCode= jsondata.get("unitId").toString(); List<Users>listUser=
	 * systemAdminDao.getUserByServiceNoAndHospital(serviceNo,unitId); Users
	 * users=null;
	 * 
	 * if(CollectionUtils.isEmpty(listUser)) { List<MasEmployee>listMasEmployee=
	 * systemAdminDao.getMasEmployeeForAdmin(serviceNo);
	 * if(jsondata.get("adminFlagValue").toString().equalsIgnoreCase("U")) {
	 * users=getUserBean(listMasEmployee.get(0), unitId, jsondata,unitCode); } else
	 * { users=getUserBean(listMasEmployee.get(0), unitId,jsondata,unitCode); }
	 * systemAdminDao. saveOrUsers(users);
	 * if(!jsondata.get("adminFlagValue").toString().equalsIgnoreCase("U")) {
	 * updateRoleTemplate(users); } json.put("status", "success"); } else {
	 * if(jsondata.get("adminFlagValue").toString().equalsIgnoreCase("U") &&
	 * StringUtils.isNotEmpty(jsondata.get("userIdValue").toString())) { Long
	 * userId=Long.parseLong(jsondata.get("userIdValue").toString());
	 * 
	 * Users usersExist=systemAdminDao.getUserbyUserId(userId); String
	 * masDesigationIdValues=jsondata.get("masDesigationIdValues").toString();
	 * masDesigationIdValues=OpdServiceImpl.getReplaceString(masDesigationIdValues);
	 * usersExist.setDesignationId(masDesigationIdValues);
	 * 
	 * String masRoleIdValues=jsondata.get("masRoleIdValues").toString();
	 * masRoleIdValues=OpdServiceImpl.getReplaceString(masRoleIdValues);
	 * usersExist.setRoleId(masRoleIdValues); systemAdminDao.
	 * saveOrUsers(usersExist); json.put("status", "updateSuccess"); return
	 * json.toString();
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * if(CollectionUtils.isNotEmpty(listUser)) json.put("adminFlag",
	 * listUser.get(0).getAdminFlag()); else json.put("adminFlag","");
	 * json.put("status", "fail"); } } catch(Exception e) { e.printStackTrace(); }
	 * return json.toString(); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @Override public String getUnitAdminDetail(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { JSONObject json =
	 * new JSONObject(); int count=0; try { List<Object> responseList = new
	 * ArrayList<Object>(); List<Users>listUsers=null; Map<String,Object>
	 * mapObject=null; if(jsondata!=null &&
	 * jsondata.get("adminFlagValue").toString().equalsIgnoreCase("U")) { String
	 * serviceNumber=""; if(jsondata.containsKey("searchService")) {
	 * serviceNumber=(String) jsondata.get("searchService"); }
	 * mapObject=systemAdminDao.getAllUsers(Long.parseLong(jsondata.get("unitId").
	 * toString()),Long.parseLong(jsondata.get("userId").toString()),Integer.
	 * parseInt(jsondata.get("pageNo").toString()),serviceNumber); count=(int)
	 * mapObject.get("count"); listUsers=(List<Users>) mapObject.get("list"); for
	 * (Users users : listUsers) { HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("userId", users.getUserId());
	 * jsonMap.put("serviceNo", users.getServiceNo()); jsonMap.put("firstName",
	 * users.getFirstName()); if(users.getMasHospital()!=null &&
	 * StringUtils.isNotEmpty(users.getMasHospital().getHospitalName())) {
	 * jsonMap.put("unitName", users.getMasHospital().getHospitalName()); } else {
	 * jsonMap.put("unitName", ""); }
	 * //System.out.println("unitname"+users.getMasHospital().getHospitalName());
	 * String designationName="";
	 * if(StringUtils.isNotEmpty(users.getDesignationId())) designationName=
	 * systemAdminDao.getMasDesignationByDesignationId(users.getDesignationId());
	 * 
	 * if(StringUtils.isNotEmpty(designationName)) { String []
	 * desinationArray=designationName.split("##"); if(desinationArray!=null &&
	 * desinationArray.length>0) { jsonMap.put("designationId", desinationArray[1]);
	 * jsonMap.put("designamtionName", desinationArray[0]); } else {
	 * jsonMap.put("designationId", ""); jsonMap.put("designamtionName", ""); } }
	 * else { jsonMap.put("designationId", ""); jsonMap.put("designamtionName", "");
	 * 
	 * } jsonMap.put("status", users.getStatus()); String roleNames="";
	 * if(StringUtils.isNotEmpty(users.getRoleId()))
	 * roleNames=systemAdminDao.getMasRoleByRoleId(users.getRoleId());
	 * if(StringUtils.isNotEmpty(roleNames)) { String []
	 * rolesArray=roleNames.split("##"); if(rolesArray!=null && rolesArray.length>0)
	 * { jsonMap.put("roleNames", rolesArray[0]); jsonMap.put("rolesId",
	 * rolesArray[1]); } else { jsonMap.put("roleNames", ""); jsonMap.put("rolesId",
	 * ""); } } else { jsonMap.put("roleNames", ""); jsonMap.put("rolesId", "");
	 * 
	 * } responseList.add(jsonMap); } //
	 * userManagementDao.getRolesList(listUsers.get(0));
	 * 
	 * } else { String serviceNumber=""; if(jsondata.containsKey("searchService")) {
	 * serviceNumber=(String) jsondata.get("searchService"); }
	 * mapObject=systemAdminDao.getAllUsers(Integer.parseInt(jsondata.get("pageNo").
	 * toString()),serviceNumber); count=(int) mapObject.get("count");
	 * listUsers=(List<Users>) mapObject.get("list"); for (Users users : listUsers)
	 * { HashMap<String, Object> jsonMap = new HashMap<String, Object>();
	 * jsonMap.put("userId", users.getUserId()); jsonMap.put("serviceNo",
	 * users.getServiceNo()); jsonMap.put("firstName", users.getFirstName());
	 * jsonMap.put("designation", users.getDesignationId());
	 * if(users.getMasHospital()!=null &&
	 * StringUtils.isNotEmpty(users.getMasHospital().getHospitalName())) {
	 * jsonMap.put("unitName", users.getMasHospital().getHospitalName()); } else {
	 * jsonMap.put("unitName", ""); } //String
	 * unitRoleAdmin=UtilityServices.getValueOfPropByKey("roleIdUnitAdmin");
	 * //String
	 * unitDesiAdmin=UtilityServices.getValueOfPropByKey("desinationIdUnitAdmin");
	 * //Resource resource = new ClassPathResource("/resource.properties");
	 * //java.util.Properties properties =
	 * PropertiesLoaderUtils.loadProperties(resource); //String unitRoleAdmin=
	 * properties.getProperty("roleIdUnitAdmin");
	 * 
	 * String designationName="";
	 * if(StringUtils.isNotEmpty(users.getDesignationId())) designationName=
	 * systemAdminDao.getMasDesignationByDesignationId(users.getDesignationId());
	 * 
	 * if(StringUtils.isNotEmpty(designationName) &&
	 * !designationName.equalsIgnoreCase("##")) { String []
	 * desinationArray=designationName.split("##"); if(desinationArray!=null &&
	 * desinationArray.length>0) { jsonMap.put("designationId", desinationArray[1]);
	 * jsonMap.put("designamtionName", desinationArray[0]); }else {
	 * jsonMap.put("designationId", ""); jsonMap.put("designamtionName", "");
	 * 
	 * }
	 * 
	 * }else { jsonMap.put("designationId", ""); jsonMap.put("designamtionName",
	 * "");
	 * 
	 * }
	 * 
	 * String roleNames=""; if(StringUtils.isNotEmpty(users.getRoleId()))
	 * roleNames=systemAdminDao.getMasRoleByRoleId(users.getRoleId());
	 * if(StringUtils.isNotEmpty(roleNames) && !roleNames.equalsIgnoreCase("##")) {
	 * String [] rolesArray=roleNames.split("##"); if(rolesArray!=null &&
	 * rolesArray.length>0) { jsonMap.put("roleNames", rolesArray[0]);
	 * jsonMap.put("rolesId", rolesArray[1]); } else { jsonMap.put("roleNames", "");
	 * jsonMap.put("rolesId", "");
	 * 
	 * } }else { jsonMap.put("roleNames", ""); jsonMap.put("rolesId", "");
	 * 
	 * }
	 * 
	 * 
	 * jsonMap.put("status", users.getStatus()); responseList.add(jsonMap); } }
	 * 
	 * 
	 * if (responseList != null && responseList.size() > 0) {
	 * json.put("dataUserList", responseList); //json.put("data",responseList);
	 * json.put("count",count); json.put("status", 1); } else {
	 * json.put("dataUserList", responseList); json.put("msg", "Data not found");
	 * json.put("status", 0); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Transactional public Long updateRoleTemplate(Users users) {
	 * 
	 * Long roleTemplateId=null;
	 * 
	 * String roleId=UtilityServices.getValueOfPropByKey("roleIdUnitAdmin"); MasRole
	 * masRole=new MasRole(); masRole.setRoleId(Long.parseLong(roleId));
	 * 
	 * 
	 * Criterion cr1=Restrictions.eq("masHospital", users.getMasHospital());
	 * Criterion cr2=Restrictions.eq("msRole", masRole); Criterion
	 * cr3=Restrictions.eq("status", "y").ignoreCase();
	 * List<RoleTemplate>lisRoleTemplate=roleTemplateDao.findByCriteria(cr1,cr2,cr3)
	 * ; if(CollectionUtils.isEmpty(lisRoleTemplate)) { RoleTemplate
	 * roleTemplate=new RoleTemplate(); String
	 * templateId=UtilityServices.getValueOfPropByKey("unitAdminTemplateId");
	 * 
	 * MasTemplate masTemplate=new MasTemplate();
	 * masTemplate.setTemplateId(Long.parseLong(templateId));
	 * roleTemplate.setLastChgDate(new Date());
	 * roleTemplate.setMasHospital(users.getMasHospital());
	 * roleTemplate.setMsTemplate(masTemplate); roleTemplate.setStatus("y");
	 * roleTemplate.setUser(users); roleTemplate.setMsRole(masRole);
	 * roleTemplateDao.saveOrUpdate(roleTemplate);
	 * 
	 * roleTemplateId=roleTemplate.getRoleTemplateId(); }
	 * 
	 * return roleTemplateId; }
	 * 
	 * 
	 * public Users getUserBean(MasEmployee masEmployee,Long unitId,HashMap<String,
	 * Object> jsondata,String unitCode) { Users users=new Users(); Date date=new
	 * Date(); users.setLoginName(masEmployee.getServiceNo());
	 * users.setPassword(masEmployee.getServiceNo());
	 * users.setUserName(masEmployee.getServiceNo());
	 * users.setServiceNo(masEmployee.getServiceNo());
	 * 
	 * users.setLastChgDate(new Timestamp(date.getTime())); Calendar calendar =
	 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime());
	 * users.setLastChgDate(ourJavaTimestampObject); if(unitCode!=null &&
	 * jsondata!=null &&
	 * jsondata.get("adminFlagValue").toString().equalsIgnoreCase("S")) {
	 * //List<MasHospital>listMasHospital=systemAdminDao.getMasHospitalByUnitId(
	 * unitId);
	 * List<MasHospital>listMasHospital=systemAdminDao.getMasHospitalByUnitCode(
	 * unitCode); if(CollectionUtils.isNotEmpty(listMasHospital)) { MasHospital
	 * masHospital=new MasHospital();
	 * masHospital.setHospitalId(listMasHospital.get(0).getHospitalId());
	 * users.setMasHospital(masHospital); } } else {
	 * 
	 * MasHospital masHospital=new MasHospital();
	 * 
	 * masHospital.setHospitalId(Long.parseLong(unitCode));
	 * users.setMasHospital(masHospital); masHospital.setLastChgDate(new
	 * Timestamp(date.getTime())); }
	 * users.setFirstName(masEmployee.getEmployeeName()); users.setLastName("");
	 * users.setStatus("y");
	 * 
	 * 
	 * String masRoleIdValues=""; if(jsondata!=null &&
	 * jsondata.get("adminFlagValue").toString().equalsIgnoreCase("U")) {
	 * users.setAdminFlag("U"); String
	 * masDesigationIdValues=jsondata.get("masDesigationIdValues").toString();
	 * masDesigationIdValues=OpdServiceImpl.getReplaceString(masDesigationIdValues);
	 * users.setDesignationId(masDesigationIdValues);
	 * 
	 * masRoleIdValues=jsondata.get("masRoleIdValues").toString();
	 * masRoleIdValues=OpdServiceImpl.getReplaceString(masRoleIdValues);
	 * users.setRoleId(masRoleIdValues); } else {
	 * 
	 * //String
	 * unitRoleAdmin=UtilityServices.getValueOfPropByKey("roleIdUnitAdmin");
	 * //String
	 * unitDesiAdmin=UtilityServices.getValueOfPropByKey("desinationIdUnitAdmin");
	 * //users.setDesignationId("unitAdmin"); //users.setRoleId("unitRoleAdmin");
	 * 
	 * String roleId=UtilityServices.getValueOfPropByKey("roleIdUnitAdmin");
	 * users.setRoleId(roleId.trim()); //String
	 * designationId=UtilityServices.getValueOfPropByKey("desinationIdUnitAdmin");
	 * //users.setDesignationId(designationId.trim()); users.setAdminFlag("S"); }
	 * 
	 * if(jsondata.get("selectedUnitId")!=null) { String selectedUnitId=
	 * jsondata.get("selectedUnitId").toString();
	 * if(StringUtils.isNotEmpty(selectedUnitId))
	 * users.setUnitId(Long.parseLong(selectedUnitId)); } return users; }
	 * 
	 * @Override public String activateDeActivatUser(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); try { Users
	 * users=systemAdminDao.getUserbyUserId(Long.parseLong(jsondata.get("userId").
	 * toString())); Long
	 * userId=systemAdminDao.activateDeActivatUser(users,jsondata.get("status").
	 * toString());
	 * 
	 * json.put("status", "success");
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * 
	 * 
	 * @Override public String submitMasDesignation(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); MasDesignationMapping
	 * masDesignationMappingExist=null; try { String
	 * unitId=jsondata.get("unitId").toString(); String massDesignation=
	 * jsondata.get("massDesignationValue").toString();
	 * massDesignation=OpdServiceImpl.getReplaceString(massDesignation); //String []
	 * massDesignationArray=massDesignation.split(",");
	 * 
	 * String masDesigtionMappingId=
	 * jsondata.get("masDesigtionMappingId").toString();
	 * ////////////////////////////////////////////getHospital//////////////////////
	 * /////////////////////////// List<MasHospital>listMasHospital =
	 * systemAdminDao.getMasHospitalByUnitCode(jsondata.get("unitId").toString());
	 * 
	 * ////////////////////////////////////////////////////////////////////////
	 * MasDesignationMapping masDesignationMappingExistUnitId=null;
	 * if(CollectionUtils.isNotEmpty(listMasHospital)) {
	 * masDesignationMappingExistUnitId=systemAdminDao.
	 * getMasDesignationMappingForUnitId(listMasHospital.get(0).getHospitalId()); }
	 * if(StringUtils.isEmpty(masDesigtionMappingId) &&
	 * masDesignationMappingExistUnitId==null) { MasDesignationMapping
	 * masDesignationMappingObj=new MasDesignationMapping();
	 * 
	 * 
	 * masDesignationMappingObj.setDesignationId(massDesignation);
	 * //List<MasHospital>listMasHospital=systemAdminDao.getMasHospitalByUnitId(Long
	 * .parseLong(unitId.trim())); if(CollectionUtils.isNotEmpty(listMasHospital)) {
	 * masDesignationMappingObj.setUnitId(listMasHospital.get(0).getHospitalId()); }
	 * //if(StringUtils.isNotEmpty(unitId))
	 * //masDesignationMappingObj.setUnitId(Long.parseLong(unitId.trim()));
	 * masDesignationMappingObj.setStatus("y"); Date date=new Date(); try {
	 * masDesignationMappingObj.setLastChgDate(new Timestamp(date.getTime()));
	 * String userId=jsondata.get("userId").toString();
	 * 
	 * if(StringUtils.isNotEmpty(userId))
	 * masDesignationMappingObj.setLastChgBy(Long.parseLong(userId)); }
	 * catch(Exception e) { e.printStackTrace(); } Long
	 * massId=systemAdminDao.saveAndUpdateMasDesignation(masDesignationMappingObj);
	 * } else {
	 * 
	 * if(StringUtils.isNotBlank(masDesigtionMappingId)) {
	 * masDesignationMappingExist=systemAdminDao.
	 * getMassDesiByMassDesignationMappingId(Long.parseLong(masDesigtionMappingId.
	 * trim())) ; } else {
	 * masDesignationMappingExist=masDesignationMappingExistUnitId; }
	 * 
	 * //List<MasHospital>listMasHospital=systemAdminDao.getMasHospitalByUnitId(Long
	 * .parseLong(unitId.trim())); if(CollectionUtils.isNotEmpty(listMasHospital)) {
	 * masDesignationMappingExist.setUnitId(listMasHospital.get(0).getHospitalId());
	 * }
	 * 
	 * //masDesignationMappingExist.setUnitId(Long.parseLong(unitId));
	 * masDesignationMappingExist.setDesignationId(massDesignation); Long
	 * massId=systemAdminDao.saveAndUpdateMasDesignation(masDesignationMappingExist)
	 * ; }
	 * 
	 * if(masDesignationMappingExist!=null) { json.put("status", "updateDesi");
	 * }else { json.put("status", "success"); } } catch(Exception e) {
	 * e.printStackTrace(); json.put("status", "fail"); }
	 * 
	 * return json.toString(); }
	 * 
	 * 
	 * @Override public String getMasDesinationDetail(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * Map<String,Object> map=null; int count=0; JSONObject json = new JSONObject();
	 * try { List<Object> responseList = new ArrayList<Object>();
	 * List<MasDesignationMapping>listMasDesignationMapping=null;
	 * 
	 * if(jsondata!=null &&
	 * jsondata.get("adminFlagValue").toString().equalsIgnoreCase("U")) {
	 * if(jsondata!=null &&
	 * StringUtils.isNotEmpty(jsondata.get("unitId").toString()))
	 * listMasDesignationMapping=systemAdminDao.getMassDesiByUnitId(Long.parseLong(
	 * jsondata.get("unitId").toString())); } else {
	 * map=systemAdminDao.getAllMassDesignation(Integer.parseInt(jsondata.get(
	 * "pageNo").toString()));
	 * listMasDesignationMapping=(List<MasDesignationMapping>)
	 * map.get("listmasDesignationMapping"); count=(int) map.get("count"); }
	 * if(CollectionUtils.isNotEmpty(listMasDesignationMapping)) for
	 * (MasDesignationMapping masDesignationMapping : listMasDesignationMapping) {
	 * String designationName="";
	 * if(StringUtils.isNotEmpty(masDesignationMapping.getDesignationId()))
	 * designationName =
	 * systemAdminDao.getMasDesignationByDesignationId(masDesignationMapping.
	 * getDesignationId());
	 * 
	 * HashMap<String, Object> jsonMap = new HashMap<String, Object>();
	 * jsonMap.put("masId", masDesignationMapping.getId());
	 * if(StringUtils.isNotEmpty(designationName)) { String []
	 * desigArray=designationName.split("##"); if(desigArray!=null &&
	 * desigArray.length>0) { jsonMap.put("designationId", desigArray[1]);
	 * jsonMap.put("designamtionName", desigArray[0]); } else {
	 * jsonMap.put("designationId", ""); jsonMap.put("designamtionName", ""); } }
	 * 
	 * else { jsonMap.put("designationId", ""); jsonMap.put("designamtionName", "");
	 * 
	 * } if(masDesignationMapping.getMasHospital()!=null &&
	 * StringUtils.isNotEmpty(masDesignationMapping.getMasHospital().getHospitalName
	 * ()))
	 * jsonMap.put("unitName",masDesignationMapping.getMasHospital().getHospitalName
	 * ()); else jsonMap.put("unitName","");
	 * if(masDesignationMapping.getStatus()!=null) jsonMap.put("status",
	 * masDesignationMapping.getStatus()); else { jsonMap.put("status", ""); }
	 * responseList.add(jsonMap); } if (responseList != null && responseList.size()
	 * > 0) { json.put("dataDesignationList", responseList); json.put("status", 1);
	 * json.put("count", count); } else { json.put("dataDesignationList",
	 * responseList); json.put("msg", "Data not found"); json.put("status", 0);
	 * json.put("count", count); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String editDesignation(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { JSONObject json =
	 * new JSONObject(); try { List<Object> responseList = new ArrayList<Object>();
	 * String masId=jsondata.get("massIdValue").toString(); masId=
	 * OpdServiceImpl.getReplaceString(masId); List<MasDesignationMapping>
	 * listMasDesignationMapping=systemAdminDao.getMassDesignationIdMasId(Long.
	 * parseLong(masId.trim())); HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("masId",
	 * listMasDesignationMapping.get(0).getId()); String designationName="";
	 * if(StringUtils.isNotEmpty(listMasDesignationMapping.get(0).getDesignationId()
	 * )) designationName=
	 * systemAdminDao.getMasDesignationByDesignationId(listMasDesignationMapping.get
	 * (0).getDesignationId()); if(StringUtils.isNotEmpty(designationName)) {
	 * String[] desinationValue=designationName.split("##");
	 * if(desinationValue!=null && desinationValue.length>0) {
	 * jsonMap.put("designamtionName", desinationValue[0]);
	 * jsonMap.put("designationId", desinationValue[1]); } else {
	 * jsonMap.put("designamtionName", ""); jsonMap.put("designationId", ""); }
	 * 
	 * } else { jsonMap.put("designamtionName", ""); jsonMap.put("designationId",
	 * "");
	 * 
	 * }
	 * 
	 * 
	 * Integer count=0; String designationId="";
	 * List<MasDesignation>listMasDesignation=systemAdminDao.getAllMasDesigation();
	 * for(MasDesignation masDesignation:listMasDesignation) { if(count==0) {
	 * designationId=""+masDesignation.getDesignationId(); } else {
	 * designationId+=","+masDesignation.getDesignationId(); } count++;
	 * 
	 * } String
	 * masDesignationValue=systemAdminDao.getMasDesignationByDesignationIdNotAvial(
	 * designationId, listMasDesignationMapping.get(0).getDesignationId());
	 * 
	 * 
	 * if(StringUtils.isNotEmpty(masDesignationValue)) { String[]
	 * desinationMappingValue=masDesignationValue.split("##");
	 * if(desinationMappingValue!=null && desinationMappingValue.length>0) {
	 * jsonMap.put("designamtionMappingName", desinationMappingValue[0]);
	 * jsonMap.put("designationMappingId", desinationMappingValue[1]); } else {
	 * jsonMap.put("designamtionMappingName", "");
	 * jsonMap.put("designationMappingId", ""); } } else {
	 * jsonMap.put("designamtionMappingName", "");
	 * jsonMap.put("designationMappingId", "");
	 * 
	 * }
	 * 
	 * 
	 * MasUnit
	 * masUnit=systemAdminDao.getMasHospitalByHospitalId(listMasDesignationMapping.
	 * get(0).getUnitId()); //jsonMap.put("unitId",
	 * listMasDesignationMapping.get(0).getUnitId()); //jsonMap.put("status",
	 * listMasDesignationMapping.get(0).getStatus()); if(masUnit!=null) {
	 * jsonMap.put("unitId", masUnit.getUnitId()); jsonMap.put("status",
	 * listMasDesignationMapping.get(0).getStatus()); jsonMap.put("unitCode",
	 * masUnit.getUnitCode()); } responseList.add(jsonMap);
	 * 
	 * if (responseList != null && responseList.size() > 0) {
	 * json.put("listDataDesignation", responseList); json.put("status", 1); } else
	 * { json.put("data", responseList); json.put("msg", "Data not found");
	 * json.put("status", 0); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String getUnitAdminMasRole(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { JSONObject json =
	 * new JSONObject(); try { List<Object> responseList = new ArrayList<Object>();
	 * List<MasRole> listMasRole=systemAdminDao.getMasRole(jsondata ); for(MasRole
	 * masRole:listMasRole) { HashMap<String, Object> jsonMap = new HashMap<String,
	 * Object>(); jsonMap.put("roleId", masRole.getRoleId());
	 * jsonMap.put("roleCode", masRole.getRoleCode()); jsonMap.put("roleName",
	 * masRole.getRoleName()); responseList.add(jsonMap); } if (responseList != null
	 * && responseList.size() > 0) { json.put("listMasRole", responseList);
	 * json.put("status", 1); } else { json.put("data", responseList);
	 * json.put("msg", "Data not found"); json.put("status", 0); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String editUnitAdminUser(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { JSONObject json =
	 * new JSONObject(); Long unitIdVal=null; String unitCodeVal=""; Long
	 * selectedUnitId=null; try { List<Object> responseList = new
	 * ArrayList<Object>(); Users
	 * users=systemAdminDao.getUsersByUserId(Long.parseLong(jsondata.get("userId").
	 * toString())); HashMap<String, Object> jsonMap = new HashMap<String,
	 * Object>(); jsonMap.put("userId", users.getUserId()); jsonMap.put("serviceNo",
	 * users.getServiceNo()); jsonMap.put("firstName", users.getFirstName());
	 * jsonMap.put("status", users.getStatus());
	 * 
	 * if(users.getUnitId()!=null) { selectedUnitId=users.getUnitId(); }
	 * 
	 * String designationName="";
	 * if(StringUtils.isNotEmpty(users.getDesignationId())) designationName=
	 * systemAdminDao.getMasDesignationByDesignationId(users.getDesignationId());
	 * if(StringUtils.isNotEmpty(designationName)) { String []
	 * desiValueee=designationName.split("##"); if(desiValueee!=null &&
	 * desiValueee.length>0) { jsonMap.put("designamtionName", desiValueee[0]);
	 * jsonMap.put("designationId", desiValueee[1]); } else {
	 * jsonMap.put("designamtionName", ""); jsonMap.put("designationId", ""); } }
	 * else { jsonMap.put("designamtionName", ""); jsonMap.put("designationId", "");
	 * 
	 * } String roleNames=""; if(StringUtils.isNotEmpty(users.getRoleId()))
	 * roleNames=systemAdminDao.getMasRoleByRoleId(users.getRoleId());
	 * if(StringUtils.isNotEmpty(roleNames)) { String []
	 * desiRoleValueee=roleNames.split("##"); if(desiRoleValueee!=null &&
	 * desiRoleValueee.length>0) { jsonMap.put("roleNames", desiRoleValueee[0]);
	 * jsonMap.put("rolesId", desiRoleValueee[1]); } else { jsonMap.put("roleNames",
	 * ""); jsonMap.put("rolesId", ""); } } else { jsonMap.put("roleNames", "");
	 * jsonMap.put("rolesId", "");
	 * 
	 * }
	 * 
	 * List<MasDesignationMapping>listMasDesignationMapping=systemAdminDao.
	 * getMassDesiByUnitId(Long.parseLong(jsondata.get("unitId").toString()));
	 * String designationMappingForUnit="";
	 * if(CollectionUtils.isNotEmpty(listMasDesignationMapping)) {
	 * designationMappingForUnit=
	 * systemAdminDao.getMasDesignationByDesignationIdNotAvial(
	 * listMasDesignationMapping.get(0).getDesignationId(),
	 * users.getDesignationId()); }
	 * if(StringUtils.isNotEmpty(designationMappingForUnit)) { String []
	 * desiValueee=designationMappingForUnit.split("##"); if(desiValueee!=null &&
	 * desiValueee.length>0) { jsonMap.put("designamtionMappingName",
	 * desiValueee[0]); jsonMap.put("designationMappingId", desiValueee[1]); } else
	 * { jsonMap.put("designamtionMappingName", "");
	 * jsonMap.put("designationMappingId", ""); } } else {
	 * jsonMap.put("designamtionMappingName", "");
	 * jsonMap.put("designationMappingId", "");
	 * 
	 * }
	 * 
	 * String roleMappingNames=""; if(StringUtils.isNotEmpty(users.getRoleId()))
	 * roleMappingNames=systemAdminDao.getMasRoleByRoleIdForMapping(users.getRoleId(
	 * )); if(StringUtils.isNotEmpty(roleMappingNames)) { String []
	 * desiRoleValueee=roleMappingNames.split("##"); if(desiRoleValueee!=null &&
	 * desiRoleValueee.length>0) { jsonMap.put("roleMappingNames",
	 * desiRoleValueee[0]); jsonMap.put("rolesMappingId", desiRoleValueee[1]); }
	 * else { jsonMap.put("roleMappingNames", ""); jsonMap.put("rolesMappingId",
	 * ""); } } else { jsonMap.put("roleMappingNames", "");
	 * jsonMap.put("rolesMappingId", "");
	 * 
	 * } try { MasUnit masUnit
	 * =systemAdminDao.getMasHospitalByHospitalId(Long.parseLong(jsondata.get(
	 * "unitId").toString())); unitIdVal=masUnit.getUnitId();
	 * unitCodeVal=masUnit.getUnitCode(); } catch(Exception e) {
	 * e.printStackTrace(); } responseList.add(jsonMap);
	 * 
	 * if (responseList != null && responseList.size() > 0) {
	 * json.put("listUserData", responseList); json.put("unitIdForUnitA",
	 * unitIdVal); json.put("unitCodeVal", unitCodeVal); json.put("selectUnitId",
	 * selectedUnitId);
	 * 
	 * json.put("status", 1); } else { json.put("data", responseList);
	 * json.put("msg", "Data not found"); json.put("status", 0);
	 * json.put("unitIdForUnitA", unitIdVal); json.put("unitCodeVal", unitCodeVal);
	 * json.put("selectUnitId", selectedUnitId); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * 
	 * @Override public String getMasDesigationForUnitId(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); try {
	 * List<MasHospital>listMasHospital=null; if(jsondata.get("unitId")!=null &&
	 * jsondata.get("adminFlgValue").toString().equalsIgnoreCase("S")) {
	 * 
	 * listMasHospital =
	 * systemAdminDao.getMasHospitalByUnitCode(jsondata.get("unitId").toString());
	 * // listMasHospital=systemAdminDao.getMasHospitalByUnitId(miUnitId);
	 * 
	 * } MasDesignationMapping masDesignationMapping=null;
	 * if(CollectionUtils.isNotEmpty(listMasHospital))
	 * masDesignationMapping=systemAdminDao.getMasDesignationMappingForUnitId(
	 * listMasHospital.get(0).getHospitalId()); if(masDesignationMapping!=null) {
	 * json.put("statusSuccess", "scuucess"); json.put("status", 1); } else {
	 * json.put("statusSuccess", "fail"); json.put("status", 0);
	 * 
	 * } } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String getAllServiceByUnitId(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); try { List<Object> responseList = new
	 * ArrayList<Object>(); List<MasEmployee>
	 * listMasEmployee=systemAdminDao.getAllServiceByUnitId(jsondata );
	 * for(MasEmployee masEmployee:listMasEmployee) { HashMap<String, Object>
	 * jsonMap = new HashMap<String, Object>(); jsonMap.put("serviceNo",
	 * masEmployee.getServiceNo()); jsonMap.put("empName",
	 * masEmployee.getEmployeeName()); responseList.add(jsonMap); } if (responseList
	 * != null && responseList.size() > 0) { json.put("listMasEmployee",
	 * responseList); json.put("status", 1); } else { json.put("listMasEmployee",
	 * responseList); json.put("msg", "Data not found"); json.put("status", 0); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 */
	
}
