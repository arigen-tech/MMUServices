package com.mmu.services.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.AppointmentDAO;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.entity.AppSetup;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasAppointmentSession;
import com.mmu.services.entity.MasAppointmentType;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDoctorMapping;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.service.AppointmentService;
import com.mmu.services.utils.HMSUtil;

@Service("AppointmentService")
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	AppointmentDAO appointmentDAO;

	@Autowired
	PatientRegistrationDao patientRegistrationDao;

	@Autowired
	PatientRegistrationServiceImpl patientRegistrationServiceImpl;

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getDataForDoctorAppointment(Map<String,
	 * String> requestData) { Map<String, Object> dataMap = new HashMap<String,
	 * Object>(); Map<String, Object> responseMap = new HashMap<String, Object>();
	 * long hospitalId = Long.parseLong(requestData.get("hospitalId")); dataMap =
	 * appointmentDAO.getDataForDoctorAppointment(hospitalId); List<Object>
	 * responseList = new ArrayList<Object>(); if (dataMap.size() > 0) {
	 * List<MasDepartment> departmentList = (List<MasDepartment>)
	 * dataMap.get("departmentList"); if (departmentList.size() > 0 &&
	 * departmentList != null) { for (MasDepartment md : departmentList) {
	 * HashMap<String, Object> map = new HashMap<String, Object>();
	 * map.put("departmentId", md.getDepartmentId()); map.put("departmentName",
	 * md.getDepartmentName()); responseList.add(map); } if (responseList != null &&
	 * responseList.size() > 0) { responseMap.put("departmentList", responseList);
	 * responseMap.put("status", 1); } } else { responseMap.put("data",
	 * responseList); responseMap.put("msg", "Data not found");
	 * responseMap.put("status", 0); } } return responseMap; }
	 * 
	 * @Override public Map<String, Object>
	 * getlocationWiseAppointmentType(Map<String, String> requestData) { Map<String,
	 * Object> responseMap = new HashMap<String, Object>(); List<Map<String,
	 * Object>> appointmentTypeList = new ArrayList<Map<String, Object>>(); long
	 * departmentId = Long.parseLong(requestData.get("deptId")); long hospitalId =
	 * Long.parseLong(requestData.get("hospitalId")); List<MasAppointmentSession>
	 * appointmentSessionList =
	 * appointmentDAO.getlocationWiseAppointmentType(departmentId, hospitalId); if
	 * (appointmentSessionList.size() > 0) { for (MasAppointmentSession appSession :
	 * appointmentSessionList) { Map<String, Object> map = new HashMap<String,
	 * Object>(); long appointmentTypeId =
	 * appSession.getMasAppointmentType().getAppointmentTypeId(); String
	 * appointmentTypeName =
	 * appSession.getMasAppointmentType().getAppointmentTypeName();
	 * map.put("appointmentTypeId", appointmentTypeId);
	 * map.put("appointmentTypeName", appointmentTypeName);
	 * appointmentTypeList.add(map); } if (appointmentTypeList != null &&
	 * appointmentTypeList.size() > 0) { responseMap.put("appointmentTypeList",
	 * appointmentTypeList); responseMap.put("status", 1); } } else {
	 * responseMap.put("appointmentTypeList", appointmentTypeList);
	 * responseMap.put("msg", "Data not found"); responseMap.put("status", 0); }
	 * return responseMap; }
	 * 
	 * @Override
	 * 
	 * @SuppressWarnings("unchecked") public Map<String, Object>
	 * getappointmentSetupDetails(Map<String, String> requestData) { Map<String,
	 * Object> resultMap = new HashMap<String, Object>(); Map<String, Object> map =
	 * new HashMap<String, Object>(); List<HashMap<String, Object>> appSetupdataList
	 * = new ArrayList<HashMap<String, Object>>(); List<HashMap<String, Object>>
	 * appSessionDataList = new ArrayList<HashMap<String, Object>>(); long
	 * appointmentTypeId = 0; long deptId = 0; long hospitalId = 0;
	 * 
	 * if (requestData.get("deptId") != null &&
	 * !requestData.get("deptId").isEmpty()) { deptId =
	 * Long.parseLong(requestData.get("deptId")); } if
	 * (requestData.get("appointmentTypeId") != null &&
	 * !requestData.get("appointmentTypeId").isEmpty()) { appointmentTypeId =
	 * Long.parseLong(requestData.get("appointmentTypeId")); } if
	 * (requestData.get("hospitalId") != null &&
	 * !requestData.get("hospitalId").isEmpty()) { hospitalId =
	 * Long.parseLong(requestData.get("hospitalId")); } resultMap =
	 * appointmentDAO.getappointmentSetupDetails(deptId, appointmentTypeId,
	 * hospitalId);
	 * 
	 * List<MasAppointmentSession> masAppSessionList = (List<MasAppointmentSession>)
	 * resultMap .get("appointmentSessionList"); if (masAppSessionList != null &&
	 * masAppSessionList.size() > 0) { for (MasAppointmentSession appSession :
	 * masAppSessionList) { HashMap<String, Object> appSessionMap = new
	 * HashMap<String, Object>(); appSessionMap.put("fromTime",
	 * appSession.getFromTime()); appSessionMap.put("toTime",
	 * appSession.getToTime()); appSessionDataList.add(appSessionMap); }
	 * map.put("appSessionDataList", appSessionDataList); }
	 * 
	 * List<AppSetup> appSetUpList = (List<AppSetup>) resultMap.get("appSetupList");
	 * if (appSetUpList != null && appSetUpList.size() > 0) { for (int i = 0; i <
	 * appSetUpList.size(); i++) { HashMap<String, Object> appSetUpMap = new
	 * HashMap<String, Object>(); appSetUpMap.put("id",
	 * appSetUpList.get(i).getId()); appSetUpMap.put("days",
	 * appSetUpList.get(i).getDays()); appSetUpMap.put("startToken",
	 * appSetUpList.get(i).getStartToken()); appSetUpMap.put("totalInterval",
	 * appSetUpList.get(i).getTotalInterval()); appSetUpMap.put("totalToken",
	 * appSetUpList.get(i).getTotalToken()); appSetUpMap.put("totalOnlineToken",
	 * appSetUpList.get(i).getTotalOnlineToken());
	 * appSetUpMap.put("totalPortalToken",
	 * appSetUpList.get(i).getTotalPortalToken());
	 * appSetUpMap.put("totalMobileToken",
	 * appSetUpList.get(i).getTotalMobileToken()); appSetUpMap.put("maxNoOfDays",
	 * appSetUpList.get(i).getMaxNoOfDays()); appSetUpMap.put("minNoOfDays",
	 * appSetUpList.get(i).getMinNoOfDays()); appSetupdataList.add(appSetUpMap); }
	 * map.put("appSetupdataList", appSetupdataList); map.put("status", 1);
	 * 
	 * } else { map.put("status", 0); } return map; }
	 * 
	 * @Override public String submitAppointmentSetup(String requestData) {
	 * JSONObject json = new JSONObject(); JSONObject jObject = new
	 * JSONObject(requestData); JSONObject jsonobjData =
	 * jObject.getJSONObject("appSetupData");
	 * 
	 * JSONArray appointment = jsonobjData.getJSONArray("appointment"); JSONArray
	 * days = jsonobjData.getJSONArray("days"); JSONArray tokenStart =
	 * jsonobjData.getJSONArray("TokenStart"); JSONArray tokenInterval =
	 * jsonobjData.getJSONArray("TokenInterval"); JSONArray totalToken =
	 * jsonobjData.getJSONArray("TotalToken"); JSONArray totalOnlineToken =
	 * jsonobjData.getJSONArray("TotalOnlineToken"); JSONArray totalPortalToken =
	 * jsonobjData.getJSONArray("totalPortalToken"); JSONArray totalMobileToken =
	 * jsonobjData.getJSONArray("totalMobileToken"); JSONArray maxDays =
	 * jsonobjData.getJSONArray("maxdays"); JSONArray minDays =
	 * jsonobjData.getJSONArray("mindays");
	 * 
	 * long departmentId = Long.parseLong(jsonobjData.getString("department")); long
	 * appointmentTypeId = Long.parseLong(jsonobjData.getString("appointmentType"));
	 * long hospitalId = Long.parseLong(jsonobjData.getString("hospitalId")); //
	 * This hospital Id must be come from // session
	 * 
	 * long userId = Long.parseLong(jsonobjData.getString("userId"));
	 * 
	 * long appointmentSessionId =
	 * patientRegistrationDao.getAppointmentSessionId(hospitalId, departmentId,
	 * appointmentTypeId);
	 * 
	 * String stratTime = jsonobjData.getString("startTime"); String endTime =
	 * jsonobjData.getString("endTime"); String resp = "";
	 * 
	 * Date currentDate = new Date();
	 * 
	 * // below code for (int i = 0; i < appointment.length(); i++) { // check null
	 * condition and row count value AppSetup appSetup = null; if
	 * (appointment.get(i).equals("")) { appSetup = new AppSetup(); } else {
	 * appSetup = appointmentDAO.getAppSetupObject(Long.parseLong((String)
	 * appointment.get(i))); }
	 * 
	 * appSetup.setDays((String) days.get(i));
	 * 
	 * if (!tokenStart.get(i).toString().isEmpty()) { appSetup.setStartToken((new
	 * BigDecimal((String) tokenStart.get(i)))); } if
	 * (!totalToken.get(i).toString().isEmpty()) { appSetup.setTotalToken((new
	 * BigDecimal((String) totalToken.get(i)))); }
	 * 
	 * if (!tokenInterval.get(i).toString().isEmpty()) {
	 * appSetup.setTotalInterval((new BigDecimal((String) tokenInterval.get(i)))); }
	 * 
	 * if (!totalOnlineToken.get(i).toString().isEmpty()) {
	 * appSetup.setTotalOnlineToken((new BigDecimal((String)
	 * totalOnlineToken.get(i)))); }
	 * 
	 * if (!totalPortalToken.get(i).toString().isEmpty()) {
	 * appSetup.setTotalPortalToken((new BigDecimal((String)
	 * totalPortalToken.get(i)))); } if
	 * (!totalMobileToken.get(i).toString().isEmpty()) {
	 * appSetup.setTotalMobileToken((new BigDecimal((String)
	 * totalMobileToken.get(i)))); }
	 * 
	 * if (!maxDays.get(i).toString().isEmpty()) { appSetup.setMaxNoOfDays((new
	 * BigDecimal((String) maxDays.get(i)))); }
	 * 
	 * if (!minDays.get(i).toString().isEmpty()) { appSetup.setMinNoOfDays((new
	 * BigDecimal((String) minDays.get(i)))); }
	 * 
	 * MasDepartment masDepartment = new MasDepartment();
	 * masDepartment.setDepartmentId(departmentId);
	 * appSetup.setMasDepartment(masDepartment);
	 * 
	 * if (appointmentSessionId != 0) { MasAppointmentSession masAppSession = new
	 * MasAppointmentSession(); masAppSession.setId(appointmentSessionId);
	 * appSetup.setMasAppointmentSession(masAppSession); ;
	 * 
	 * }
	 * 
	 * // Adding Start Time if (!(stratTime.isEmpty())) {
	 * appSetup.setStartTime(stratTime); }
	 * 
	 * // Adding End Time if (!(endTime.isEmpty())) { appSetup.setEndTime(endTime);
	 * }
	 * 
	 * MasHospital masHospital = new MasHospital();
	 * masHospital.setHospitalId(hospitalId); appSetup.setMasHospital(masHospital);
	 * 
	 * // saving user data is pending in this method. This data also come from
	 * session
	 * 
	 * Users user = new Users(); user.setUserId(userId); appSetup.setUser(user);
	 * 
	 * appSetup.setLastChgDate(new Timestamp(currentDate.getTime()));
	 * 
	 * resp = appointmentDAO.saveAppointmentSetup(appSetup);
	 * 
	 * } if (resp != null && resp.equalsIgnoreCase("200")) { json.put("msg",
	 * "Appointment saved successfully "); json.put("status", "1"); } else if (resp
	 * != null && resp.equalsIgnoreCase("403")) { // currently not using this else
	 * if loop but make it // useful json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { resp = "Appointment not saved successfully"; json.put("msg", resp);
	 * json.put("status", "0"); } return json.toString(); }
	 * 
	 * @Override public Map<String, Object> getAppointmentSessionDetails() {
	 * Map<String, Object> map = new HashMap<String, Object>(); List<HashMap<String,
	 * Object>> departmentList = new ArrayList<HashMap<String, Object>>();
	 * List<HashMap<String, Object>> appointmentTypeList = new
	 * ArrayList<HashMap<String, Object>>();
	 * 
	 * List<MasDepartment> respDepartmentList =
	 * patientRegistrationDao.getDepartmentList(); if (!respDepartmentList.isEmpty()
	 * && respDepartmentList.size() > 0) { for (MasDepartment department :
	 * respDepartmentList) { HashMap<String, Object> departmentMap = new
	 * HashMap<String, Object>(); departmentMap.put("departmentId",
	 * department.getDepartmentId()); departmentMap.put("departmentName",
	 * department.getDepartmentName()); departmentList.add(departmentMap); }
	 * map.put("departmentList", departmentList); }
	 * 
	 * List<MasAppointmentType> respAppointmentTypeList =
	 * appointmentDAO.getAppointmentTypeList(); if
	 * (!respAppointmentTypeList.isEmpty() && respAppointmentTypeList.size() > 0) {
	 * for (MasAppointmentType appointmentType : respAppointmentTypeList) {
	 * HashMap<String, Object> appointmentTypeMap = new HashMap<String, Object>();
	 * appointmentTypeMap.put("appointmentTypeId",
	 * appointmentType.getAppointmentTypeId());
	 * appointmentTypeMap.put("appointmentTypeName",
	 * appointmentType.getAppointmentTypeName());
	 * appointmentTypeList.add(appointmentTypeMap); } map.put("appointmentTypeList",
	 * appointmentTypeList); } return map; }
	 * 
	 * @Override public String submitAppointmentSession(String requestData) {
	 * 
	 * JSONObject json = new JSONObject(); JSONObject jsonObj = new
	 * JSONObject(requestData);
	 * 
	 * String resp = ""; long departmentId = 0; long appointmentTypeId = 0; long
	 * hospitalId = 0;
	 * 
	 * Date currentDate = new Date();
	 * 
	 * MasAppointmentSession appointmentSession = new MasAppointmentSession();
	 * 
	 * long userId = Long.parseLong(jsonObj.getString("userId"));
	 * 
	 * if (jsonObj.getLong("departmentId") != 0) { departmentId =
	 * jsonObj.getLong("departmentId"); MasDepartment md = new MasDepartment();
	 * md.setDepartmentId(departmentId); appointmentSession.setMasDepartment(md);
	 * 
	 * }
	 * 
	 * if (jsonObj.getLong("appointmentTypeId") != 0) { appointmentTypeId =
	 * jsonObj.getLong("appointmentTypeId"); MasAppointmentType appType = new
	 * MasAppointmentType(); appType.setAppointmentTypeId(appointmentTypeId);
	 * appointmentSession.setMasAppointmentType(appType); }
	 * 
	 * // HospitalId will get fetch from session
	 * 
	 * if (jsonObj.getLong("hospitalId") != 0) { hospitalId =
	 * jsonObj.getLong("hospitalId"); MasHospital hospital = new MasHospital();
	 * hospital.setHospitalId(hospitalId);
	 * appointmentSession.setMasHospital(hospital); }
	 * 
	 * if (!jsonObj.getString("startTime").isEmpty()) { String startTime =
	 * jsonObj.getString("startTime"); appointmentSession.setFromTime(startTime); }
	 * 
	 * if (!jsonObj.getString("endTime").isEmpty()) { String endTime =
	 * jsonObj.getString("endTime"); appointmentSession.setToTime(endTime); }
	 * 
	 * appointmentSession.setLastChgDate(new Timestamp(currentDate.getTime()));
	 * 
	 * Users user = new Users(); user.setUserId(userId);
	 * appointmentSession.setUser(user);
	 * 
	 * appointmentSession.setStatus("y");
	 * 
	 * boolean existingRecord = appointmentDAO.validateAppSession(departmentId,
	 * appointmentTypeId, hospitalId); if (!existingRecord) { resp =
	 * appointmentDAO.submitAppointmentSession(appointmentSession); }
	 * 
	 * if (resp != null && !resp.isEmpty() && resp.equalsIgnoreCase("200")) {
	 * json.put("msg", "Appointment Session saved successfully ");
	 * json.put("status", "1"); } else { resp =
	 * "Appointment Session already exists"; json.put("msg", resp);
	 * json.put("status", "0"); } return json.toString(); }
	 * 
	 * @Override public String getAllAppointmentSession(JSONObject jsonObject) {
	 * 
	 * JSONObject json = new JSONObject(); List<MasAppointmentSession>
	 * appointmentSessionsList = new ArrayList<MasAppointmentSession>();
	 * 
	 * Map<String, List<MasAppointmentSession>> appointmentSessionMap =
	 * appointmentDAO .getAllAppointmentSession(jsonObject); List<HashMap<String,
	 * Object>> appListObj = new ArrayList<HashMap<String, Object>>();
	 * List<MasAppointmentSession> totalMatches = new
	 * ArrayList<MasAppointmentSession>(); if
	 * (appointmentSessionMap.get("masAppointSessionList") != null) {
	 * appointmentSessionsList = appointmentSessionMap.get("masAppointSessionList");
	 * totalMatches = appointmentSessionMap.get("totalMatches");
	 * 
	 * for (MasAppointmentSession masAppointmentSession : appointmentSessionsList) {
	 * HashMap<String, Object> mapObj = new HashMap<String, Object>();
	 * 
	 * mapObj.put("Id", masAppointmentSession.getId()); mapObj.put("departmentName",
	 * masAppointmentSession.getMasDepartment().getDepartmentName());
	 * mapObj.put("departmentId",
	 * masAppointmentSession.getMasDepartment().getDepartmentId());
	 * mapObj.put("apppointmentType",
	 * masAppointmentSession.getMasAppointmentType().getAppointmentTypeName());
	 * mapObj.put("apppointmentTypeId",
	 * masAppointmentSession.getMasAppointmentType().getAppointmentTypeId());
	 * mapObj.put("startTime", masAppointmentSession.getFromTime());
	 * mapObj.put("endTime", masAppointmentSession.getToTime());
	 * mapObj.put("status", masAppointmentSession.getStatus());
	 * appListObj.add(mapObj); }
	 * 
	 * if (appListObj != null && appListObj.size() > 0) { json.put("data",
	 * appListObj);
	 * 
	 * json.put("count", totalMatches.size()); json.put("status", 1); } else {
	 * json.put("data", appListObj); json.put("count", totalMatches.size());
	 * json.put("msg", "Data not found"); json.put("status", 0); } }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public Map<String, Object> getHospitalList() { Map<String, Object>
	 * map = new HashMap<String, Object>(); List<Object> responseList = new
	 * ArrayList<Object>(); List<MasHospital> hospitalList = new
	 * ArrayList<MasHospital>(); hospitalList = appointmentDAO.getHospitalList();
	 * 
	 * if (hospitalList != null && hospitalList.size() > 0) { for (MasHospital
	 * hospital : hospitalList) { HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("hospitalId",
	 * hospital.getHospitalId()); jsonMap.put("hospitalName",
	 * hospital.getHospitalName()); responseList.add(jsonMap); } if (responseList !=
	 * null && responseList.size() > 0) { map.put("data", responseList);
	 * map.put("status", 1); } else { map.put("data", responseList); map.put("msg",
	 * "Data not found"); map.put("status", 0); } } return map; }
	 * 
	 * @Override public String updateAppointmentSession(String requestData) {
	 * JSONObject json = new JSONObject(); JSONObject jsonObj = new
	 * JSONObject(requestData); String resp = ""; long rowId = 0; long departmentId
	 * = 0; long appointmentType = 0; long hospitalId = 0; String stratTime = "";
	 * String endTime = ""; String status = "";
	 * 
	 * if (!(jsonObj.get("rowId").toString().isEmpty()) && jsonObj.get("rowId") !=
	 * null) { rowId = Long.parseLong(jsonObj.get("rowId").toString());
	 * 
	 * }
	 * 
	 * if (!(jsonObj.get("departmentId").toString().isEmpty()) &&
	 * jsonObj.get("departmentId") != null) { departmentId =
	 * Long.parseLong(jsonObj.get("departmentId").toString());
	 * 
	 * }
	 * 
	 * if (!(jsonObj.get("hospitalId").toString().isEmpty()) &&
	 * jsonObj.get("hospitalId") != null) { hospitalId =
	 * Long.parseLong(jsonObj.get("hospitalId").toString());
	 * 
	 * }
	 * 
	 * if (!(jsonObj.get("appointmentTypeId").toString().isEmpty()) &&
	 * jsonObj.get("appointmentTypeId") != null) { appointmentType =
	 * Long.parseLong(jsonObj.get("appointmentTypeId").toString());
	 * 
	 * }
	 * 
	 * if (!(jsonObj.get("startTime").toString().isEmpty()) &&
	 * jsonObj.get("startTime") != null) { stratTime =
	 * jsonObj.get("startTime").toString();
	 * 
	 * } if (!(jsonObj.get("endTime").toString().isEmpty()) &&
	 * jsonObj.get("endTime") != null) { endTime =
	 * jsonObj.get("endTime").toString();
	 * 
	 * } if (!(jsonObj.get("status").toString().isEmpty()) && jsonObj.get("status")
	 * != null) { status = jsonObj.get("status").toString();
	 * 
	 * } if (departmentId != 0 && appointmentType != 0 && !stratTime.isEmpty() &&
	 * !endTime.isEmpty()) { // check existing session // boolean existingRecord =
	 * appointmentDAO.validateAppSession(departmentId, // appointmentType,
	 * hospitalId); // if (!existingRecord) { resp =
	 * appointmentDAO.updateAppointmentSession(rowId, departmentId, appointmentType,
	 * stratTime, endTime, status); if (!resp.isEmpty()) { json.put("msg", resp);
	 * json.put("status", 1); }
	 * 
	 * } else { json.put("msg", "Appointment Session already exists");
	 * json.put("status", 1); }
	 * 
	 * } else { json.put("msg", "Record not updated"); json.put("status", 0); }
	 * 
	 * return json.toString(); }
	 * 
	 * ////////////////////////////////// // **************Web portal Code Start
	 **************************

	// By Anita(Get Hospital List)
	@Override
	public Map<Long, String> getAllHospitalList1() {
		// TODO Auto-generated method stub
		Map<Long, String> map = new HashMap<Long, String>();
		List<MasHospital> hospitalList = new ArrayList<MasHospital>();

		hospitalList = appointmentDAO.getHospitalList();
		for (MasHospital masHospital : hospitalList) {
			map.put(masHospital.getHospitalId(), masHospital.getHospitalName());
		}

		return map;
	}

	*

	@Override public String submitAppointmentByWebportal(String requestData)
	 * throws JSONException, ParseException { // TODO Auto-generated method stub //
	 * MasEmployeeDetails masEmployeeDetails=null;
	 * System.out.println("requestData on service:"+requestData);
	 * 
	 * JSONObject json = new JSONObject(requestData); JSONObject jsonObj = new
	 * JSONObject(requestData);
	 * 
	 * Patient patient = new Patient(); long visitId=0; long patientId=0; Date
	 * appointmentdate=null; String
	 * apDate=jsonObj.get("appointmentDate").toString();
	 * System.out.println("apDate----------"+apDate); long uhidNO
	 * =Long.parseLong(jsonObj.get("uhidNo").toString()); long serviceNo
	 * =Long.parseLong(jsonObj.get("serviceNo").toString()); String relation
	 * =jsonObj.get("relation").toString(); long empId
	 * =Long.parseLong(jsonObj.get("empId").toString());
	 * 
	 * long departmentId = Long.parseLong(jsonObj.get("department").toString());
	 * long hospitalId = Long.parseLong(jsonObj.get("hospital").toString()); long
	 * patientRelationId = Long.parseLong(jsonObj.get("relationId").toString());
	 * long
	 * registrationTypeId=Long.parseLong(HMSUtil.getProperties("adt.properties",
	 * "ICG_REGISTRATION_TYPE_ID"));
	 * 
	 * try { appointmentdate =
	 * HMSUtil.dateFormatteryyyymmdd(jsonObj.get("appointmentDate").toString());
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * Visit visit = new Visit(); if(jsonObj.getLong("uhidNo")!=0) {
	 * 
	 * patientId = patientRegistrationDao.getPatientFromUhidNo(uhidNO); Timestamp
	 * dateTime = new Timestamp(appointmentdate.getTime());
	 * 
	 * visit.setVisitDate(dateTime); visit.setVisitStatus("w");
	 * visit.setHospitalId(hospitalId); visit.setDepartmentId(departmentId);
	 * visit.setPatientId(patientId); visit.setVisitFlag("P"); long
	 * appointmentSessionId=
	 * patientRegistrationDao.getAppointmentSessionId(hospitalId,departmentId,Long.
	 * parseLong(jsonObj.get("appTypeId").toString()));
	 * 
	 * visit.setSessionId(appointmentSessionId); long tokenNo =
	 * Long.parseLong(jsonObj.get("tokenNumber").toString());
	 * visit.setTokenNo(tokenNo);
	 * 
	 * visit.setAppointmentTypeId(Long.parseLong(jsonObj.get("appTypeId").toString()
	 * ));
	 * 
	 * visitId= patientRegistrationDao.saveVisitForRegisteredPatient(visit);
	 * 
	 * }else {
	 * 
	 * patient =new Patient(); try {
	 * patient.setDateOfBirth(HMSUtil.dateFormatteryyyymmdd(jsonObj.get(
	 * "dateOfBirth").toString())); } catch (Exception e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * patient.setServiceNo(jsonObj.getString("serviceNo"));
	 * patient.setPatientName(jsonObj.getString("fullName"));
	 * 
	 * long patientGenderId=jsonObj.getLong("genderId");
	 * patient.setAdministrativeSexId(patientGenderId);
	 * 
	 * patient.setEmployeeId(empId);
	 * patient.setRegistrationTypeId(registrationTypeId);
	 * 
	 * uhidNO =(new Double(Math.random()* 50 + 1)).longValue();
	 * patient.setUhidNo(uhidNO);
	 * 
	 * String serviceNumber=jsonObj.get("serviceNo").toString(); uhidNO
	 * =patientRegistrationServiceImpl.getHinNo(serviceNumber,patientRelationId,
	 * registrationTypeId); patient.setUhidNo(uhidNO);
	 * 
	 * 
	 * if(!jsonObj.getString("empName").isEmpty()) { String empName =
	 * jsonObj.getString("empName"); patient.setEmployeeName(empName); }
	 * 
	 * if(jsonObj.getLong("empId")!=0) { long empId = jsonObj.getLong("empId");
	 * patient.setEmployeeId(empId); }
	 * 
	 * if(!jsonObj.getString("patientAddress").isEmpty()) { String patientAddress =
	 * jsonObj.getString("patientAddress"); patient.setAddress(patientAddress); }
	 * 
	 * 
	 * if(!jsonObj.getString("patientPincode").isEmpty()) { String patientPincode =
	 * jsonObj.getString("patientPincode");
	 * patient.setPincode(Long.parseLong(patientPincode)); }
	 * 
	 * 
	 * if(!jsonObj.getString("patientCity").isEmpty()) { String patientCity =
	 * jsonObj.getString("patientCity"); patient.setCity(patientCity); }
	 * 
	 * 
	 * if(!jsonObj.getString("patientDOB").isEmpty()) { String patientDOB =
	 * jsonObj.getString("patientDOB"); try {
	 * patient.setDateOfBirth(HMSUtil.dateFormatteryyyymmdd(patientDOB)); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * 
	 * if(!jsonObj.getString("patientEmail").isEmpty()) { String patientEmail =
	 * jsonObj.getString("patientEmail"); patient.setEmailId(patientEmail); }
	 * 
	 * if(!jsonObj.getString("nok1Firstname").isEmpty()) { String nok1FirstName=
	 * jsonObj.getString("nok1Firstname"); patient.setNok1Name(nok1FirstName); }
	 * 
	 * if(!jsonObj.getString("nok1Address").isEmpty()) { String nok1Address
	 * =jsonObj.getString("nok1Address"); patient.setNok1Address(nok1Address); }
	 * 
	 * if(!jsonObj.getString("nok1Policestation").isEmpty()) { String
	 * nok1PoliceStation=jsonObj.getString("nok1Policestation");
	 * patient.setNok1PoliceStation(nok1PoliceStation); }
	 * 
	 * if(!jsonObj.getString("nok1Email").isEmpty()) { String nok1Email
	 * =jsonObj.getString("nok1Email"); patient.setNok1EmailId(nok1Email); }
	 * 
	 * if(!jsonObj.getString("nok2Firstname").isEmpty()) { String
	 * nok2FirstName=jsonObj.getString("nok2Firstname");
	 * patient.setNok2Name(nok2FirstName); }
	 * 
	 * 
	 * if(!jsonObj.getString("nok2Address").isEmpty()) { String
	 * nok2Address=jsonObj.getString("nok2Address");
	 * patient.setNok2Address(nok2Address); }
	 * 
	 * if(!jsonObj.getString("nok2Policestation").isEmpty()) { String
	 * nok2PoliceStation=jsonObj.getString("nok2Policestation");
	 * patient.setNok2PoliceStation(nok2PoliceStation); }
	 * 
	 * if(!jsonObj.getString("nok2Email").isEmpty()) { String
	 * nok2Email=jsonObj.getString("nok2Email"); patient.setNok2EmailId(nok2Email);
	 * }
	 * 
	 * if(jsonObj.getLong("rankId")!=0) { long rankId=jsonObj.getLong("rankId");
	 * patient.setRankId(rankId); }
	 * 
	 * if(jsonObj.getLong("tradeId")!=0) { long tradeId= jsonObj.getLong("tradeId");
	 * // trade/branch patient.setTradeId(tradeId); }
	 * 
	 * 
	 * if(!jsonObj.getString("empServiceJoinDate").isEmpty()) { String
	 * empServiceJoinDate=jsonObj.getString("empServiceJoinDate"); try {
	 * patient.setServiceJoinDate(HMSUtil.dateFormatteryyyymmdd(empServiceJoinDate))
	 * ; } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * 
	 * if(jsonObj.getLong("unitId")!=0) { long unitId=jsonObj.getLong("unitId");
	 * patient.setUnitId(unitId); }
	 * 
	 * 
	 * if(jsonObj.getLong("regionId")!=0) { long
	 * regionId=jsonObj.getLong("regionId"); patient.setCommandId(regionId); }
	 * 
	 * 
	 * if(jsonObj.getLong("recordofficeId")!=0) { long
	 * recordofficeId=jsonObj.getLong("recordofficeId");
	 * patient.setRecordOfficeAddressId(recordofficeId); }
	 * 
	 * 
	 * 
	 * if(jsonObj.getLong("religionId")!=0) { long
	 * religionId=jsonObj.getLong("religionId"); patient.setReligionId(religionId);
	 * }
	 * 
	 * 
	 * 
	 * if(jsonObj.getLong("genderId")!=0) { long
	 * patientGenderId=jsonObj.getLong("genderId");
	 * patient.setAdministrativeSexId(patientGenderId); }
	 * 
	 * 
	 * if(!jsonObj.getString("patientMoblienumber").isEmpty()) { String
	 * patientMoblienumber=jsonObj.getString("patientMoblienumber");
	 * patient.setMobileNumber(patientMoblienumber); }
	 * 
	 * 
	 * 
	 * if(jsonObj.getLong("patientStateId")!=0) { long
	 * patientStateId=jsonObj.getLong("patientStateId");
	 * patient.setStateId(patientStateId); }
	 * 
	 * 
	 * if(!jsonObj.getString("nok1Moblienumber").isEmpty()) { String
	 * nok1Moblienumber=jsonObj.getString("nok1Moblienumber");
	 * patient.setNok1MobileNo(nok1Moblienumber); }
	 * 
	 * 
	 * if(jsonObj.getLong("nok1RelationId")!=0) { long
	 * nok1RelationId=jsonObj.getLong("nok1RelationId");
	 * patient.setNok1RelationId((nok1RelationId)); }
	 * 
	 * 
	 * if(!jsonObj.getString("nok1pincode").isEmpty()) { long
	 * nok1pincode=Long.parseLong(jsonObj.getString("nok1pincode"));
	 * patient.setNok1PinCode(nok1pincode); }
	 * 
	 * 
	 * if(!jsonObj.getString("nok1Contactnumber").isEmpty()) { String
	 * nok1Contactnumber=jsonObj.getString("nok1Contactnumber");
	 * patient.setNok1ContactNo(nok1Contactnumber); }
	 * 
	 * 
	 * if(!jsonObj.getString("nok2Moblienumber").isEmpty()) { String
	 * nok2Moblienumber=jsonObj.getString("nok2Moblienumber");
	 * patient.setNok2MobileNo(nok2Moblienumber); }
	 * 
	 * 
	 * 
	 * if(jsonObj.getLong("nok2Relation")!=0) { long
	 * nok2RelationId=jsonObj.getLong("nok2Relation");
	 * patient.setNok2RelationId((nok2RelationId)); }
	 * 
	 * if(!jsonObj.getString("nok2pincode").isEmpty()) { String
	 * nok2pincode=jsonObj.getString("nok2pincode");
	 * patient.setNok2PinCode(nok2pincode); }
	 * 
	 * 
	 * if(!jsonObj.getString("nok2Contactnumber").isEmpty()) { String
	 * nok2Contactnumber=jsonObj.getString("nok2Contactnumber");
	 * patient.setNok2ContactNo(nok2Contactnumber); }
	 * 
	 * if(jsonObj.getLong("maritalstarusId")!=0) { long maritalstarusId =
	 * jsonObj.getLong("maritalstarusId");
	 * patient.setMaritalStatusId(maritalstarusId); }
	 * 
	 * if(jsonObj.getLong("bloodGroup")!=0) { long bloodGroupId =
	 * jsonObj.getLong("bloodGroup"); patient.setBloodGroupId(bloodGroupId); }
	 * 
	 * 
	 * 
	 * long patientId = patientRegistrationDao.savePatient(patient);
	 * 
	 * if(patientId!=0) { Date date= new Date(); Timestamp dateTime = new
	 * Timestamp(date.getTime());
	 * 
	 * for(int i=0;i<appointmentType.length();i++) {
	 * 
	 * 
	 * patientId = patientRegistrationDao.savePatient(patient);
	 * 
	 * Timestamp dateTime = new Timestamp(appointmentdate.getTime());
	 * 
	 * visit.setVisitDate(dateTime); visit.setVisitStatus("w");
	 * visit.setHospitalId(hospitalId); visit.setDepartmentId(departmentId);
	 * visit.setPatientId(patientId);
	 * 
	 * long appointmentSessionId=
	 * patientRegistrationDao.getAppointmentSessionId(hospitalId,departmentId,Long.
	 * parseLong(jsonObj.get("appTypeId").toString()));
	 * 
	 * visit.setSessionId(appointmentSessionId); long tokenNo =
	 * Long.parseLong(jsonObj.get("tokenNumber").toString());
	 * visit.setTokenNo(tokenNo);
	 * 
	 * visit.setAppointmentTypeId(Long.parseLong(jsonObj.get("appTypeId").toString()
	 * ));
	 * 
	 * visitId= patientRegistrationDao.saveVisitForRegisteredPatient(visit); }
	 * json.put("visitId", visitId); json.put("patientId", patientId); return
	 * json.toString(); }

	@Override
	public Map<String, Object> getAppointmentHistory(String requestData) {
		// TODO Auto-generated method stub
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject(requestData);
		Date fromdate = null;
		Date todate = null;
		Timestamp fromdateTime = null;
		Timestamp todateTime = null;
		long visitid = 0;
		String serviceNo = json.getString("serviceNo");
		if (json.has("visitId")) {
			visitid = json.getLong("visitId");
			patientRegistrationDao.cancelAppointment(visitid);
		}
		if (json.has("fromDate")) {

			if (json.get("fromDate") != null && !json.get("fromDate").toString().isEmpty()
					&& json.get("fromDate") != "") {
				String fromDate = json.getString("fromDate");
				try {
					// fromdate = HMSUtil.dateFormatteryyyymmdd(fromDate);
					fromdate = HMSUtil.convertStringDateToUtilDate(fromDate, "dd/MM/yyyy");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fromdateTime = new Timestamp(fromdate.getTime());
			}
		}
		if (json.has("toDate")) {

			if (json.get("toDate") != null && !json.get("toDate").toString().isEmpty() && json.get("fromDate") != "") {

				String toDate = json.getString("toDate");
				try {
					// todate = HMSUtil.dateFormatteryyyymmdd(toDate);
					todate = HMSUtil.convertStringDateToUtilDate(toDate, "dd/MM/yyyy");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				todateTime = new Timestamp(todate.getTime());
			}
		}
		Map<String, Object> vistMap = new HashMap<String, Object>();
		List<Visit> visitList = new ArrayList<Visit>();

		String patientName = "";
		String relation = "";
		String age = "";
		String gender = "";
		String departmentName = "";
		String doctorName = "";
		String visitStatus = "";
		long visitId = 0;
		long tokenNo = 0;
		Timestamp visitDate;
		Date medExamDate;

		String dob = null;
		String hospitalName = "";
		long hospitalId = 0;

		long departmentId = 0;
		String hinNo = "";
		String appointmentType = "";
		String appointmentTypeCode = "";
		long appointmentTypeId = 0;
		long patientId = 0;

		vistMap = patientRegistrationDao.findPatientAndVisitList(json, serviceNo, fromdateTime, todateTime);
		int count = 1;
		if (vistMap.size() > 0 && !vistMap.isEmpty()) {
			visitList = (List<Visit>) vistMap.get("visitList");
			if (!visitList.isEmpty() && visitList.size() > 0) {
				for (Visit visit : visitList) {
					Map<String, Object> responsePatientMap = new HashMap<String, Object>();
					if (visit.getPatient().getPatientName() != null) {
						patientName = visit.getPatient().getPatientName();
					}
					if (visit.getTokenNo() != null)
						tokenNo = visit.getTokenNo();
					// doctorName=visit.getd
					visitDate = visit.getVisitDate();
					visitStatus = visit.getVisitStatus();
					visitId = visit.getVisitId();
					serviceNo = visit.getPatient().getServiceNo();
					if (visit.getPatient().getDateOfBirth() != null) {
						dob = HMSUtil.changeDateToddMMyyyy(visit.getPatient().getDateOfBirth());
						age = HMSUtil.calculateAge(visit.getPatient().getDateOfBirth());
					}
					if (visit.getMasDepartment() != null) {
						departmentName = visit.getMasDepartment().getDepartmentName();
						departmentId = visit.getMasDepartment().getDepartmentId();
					}
					if (visit.getMasHospital() != null) {
						hospitalName = visit.getMasHospital().getHospitalName();
						hospitalId = visit.getMasHospital().getHospitalId();
					}
					if (visit.getMasAppointmentType() != null)
						appointmentType = visit.getMasAppointmentType().getAppointmentTypeName();
					if (visit.getMasAppointmentType() != null) {
						appointmentTypeId = visit.getMasAppointmentType().getAppointmentTypeId();
						appointmentTypeCode = visit.getMasAppointmentType().getAppointmentTypeCode();
					}
					if (visit.getEffDoctor() != null) {
						doctorName = visit.getEffDoctor().getFirstName();
						responsePatientMap.put("doctorName", doctorName);
					} else
						responsePatientMap.put("doctorName", doctorName);

					if (visit.getPatient().getUhidNo() != null)
						hinNo = visit.getPatient().getUhidNo();
					// patient related details
					if (visit.getPatient().getMasAdministrativeSex() != null)
						gender = visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName();
					if (visit.getPatient().getMasRelation() != null)
						relation = visit.getPatient().getMasRelation().getRelationName();

					patientId = visit.getPatient().getPatientId();

					responsePatientMap.put("serviceNo", serviceNo);
					responsePatientMap.put("patientName", patientName);
					responsePatientMap.put("patientId", patientId);
					responsePatientMap.put("tokenNo", tokenNo);
					responsePatientMap.put("visitDate", visitDate);
					responsePatientMap.put("visitStatus", visitStatus);
					responsePatientMap.put("age", age);
					responsePatientMap.put("serviceNo", serviceNo);
					responsePatientMap.put("gender", gender);
					responsePatientMap.put("departmentName", departmentName);
					responsePatientMap.put("relation", relation);
					responsePatientMap.put("visitId", visitId);
					responsePatientMap.put("hinNo", hinNo);
					responsePatientMap.put("patientId", visit.getPatient().getPatientId());
					responsePatientMap.put("dateOfBirth", dob);
					responsePatientMap.put("departmentId", departmentId);
					responsePatientMap.put("hospitalName", hospitalName);
					responsePatientMap.put("hospitalId", hospitalId);
					responsePatientMap.put("appointmentType", appointmentType);
					responsePatientMap.put("appointmentTypeId", appointmentTypeId);
					responsePatientMap.put("appointmentTypeCode", appointmentTypeCode);
					responsePatientMap.put("rank", visit.getPatient().getMasRank().getRankName());

					if (visit.getMasMedExam() != null) {
						responsePatientMap.put("ExamType", visit.getMasMedExam().getMedicalExamName());
						responsePatientMap.put("ExamTypeId", visit.getMasMedExam().getMedicalExamId());
						responsePatientMap.put("ExamTypeCode", visit.getMasMedExam().getMedicalExamCode());
					}
					if (visit.getMasMedicalExamReport() != null) {
						medExamDate = visit.getMasMedicalExamReport().getMediceExamDate();
						responsePatientMap.put("medicalExamId",
								visit.getMasMedicalExamReport().getMedicalExaminationId());
						responsePatientMap.put("medExamDate", medExamDate);
						responsePatientMap.put("ridcId", visit.getMasMedicalExamReport().getRidcId());
					}
					if (visit.getPatientMedBoard() != null) {
						Date mbDate = visit.getPatientMedBoard().getDateOfEnrolment();
						responsePatientMap.put("mbDate", mbDate);
						responsePatientMap.put("ridcIdMB", visit.getPatientMedBoard().getRidcId());
					}
					data.put(count++, responsePatientMap);

				}
			}
		}
		Integer tm = (Integer) vistMap.get("totalMatches");
		map.put("data", data);
		map.put("count", tm);
		if (json.has("toDate")) {
			map.put("msg", "Appointment has been cancelled");
		} else {
			map.put("msg", "List of Detail");
		}
		map.put("status", "1");
		return map;

	}

	@Override
	public Map<String, Object> reschedulePatientAppointment(String requestData) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();

		JSONObject json = new JSONObject(requestData);
		JSONObject jsonObj = new JSONObject(requestData);
		String serviceNo = json.getString("serviceNo");
		long visitId = Long.parseLong(json.get("visitId").toString());
		String tokenNumber = json.getString("tokenNumber");
		long tokenNo = Long.parseLong(json.get("tokenNumber").toString());

		String appointmentDate = json.getString("appointmentDate");
		Date appointmentdate = null;
		try {
			appointmentdate = HMSUtil.convertStringDateToUtilDate(json.get("appointmentDate").toString(),
					"dd/MM/yyyy HH:mm:ss");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timestamp dateTime = new Timestamp(appointmentdate.getTime());
		String s = patientRegistrationDao.rescheduleAppointment(visitId, tokenNo, dateTime);
		map.put("message", "Appointment has been rescheduled");

		return map;
	}

	@Override
	public Map<String, Object> getRecordsForDoctorAppointment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getOpdHistory(String requestData) {
		// TODO Auto-generated method stub
		Map<Integer,Map<String,Object>>data=new HashMap<Integer,Map<String,Object>>();Map<String,Object>map=new HashMap<String,Object>();JSONObject json=new JSONObject(requestData);Date fromdate=null;Date todate=null;Timestamp fromdateTime=null;Timestamp todateTime=null;long visitid=0;String serviceNo=json.getString("serviceNo");if(json.has("visitId")){visitid=json.getLong("visitId");patientRegistrationDao.cancelAppointment(visitid);}if(json.has("fromDate")){

		if(json.get("fromDate")!=null&&!json.get("fromDate").toString().isEmpty()&&json.get("fromDate")!=""){String fromDate=json.getString("fromDate");try{fromdate=HMSUtil.dateFormatteryyyymmdd(fromDate);

		}catch(Exception e){
		// TODO Auto-generated catch block
		e.printStackTrace();}fromdateTime=new Timestamp(fromdate.getTime());}}if(json.has("toDate")){

		if(json.get("toDate")!=null&&!json.get("toDate").toString().isEmpty()&&json.get("fromDate")!=""){

		String toDate=json.getString("toDate");try{todate=HMSUtil.dateFormatteryyyymmdd(toDate);

		}catch(Exception e){
		// TODO Auto-generated catch block
		e.printStackTrace();}todateTime=new Timestamp(todate.getTime());}}Map<String,Object>vistMap=new HashMap<String,Object>();List<Visit>visitList=new ArrayList<Visit>();

		String patientName="";String relation="";String age="";String gender="";String departmentName="";String doctorName="";String visitStatus="";long visitId=0;long tokenNo=0;Timestamp visitDate;

		Date dateOfBirth=null;String hospitalName="";long hospitalId=0;

		long departmentId=0;

		String appointmentType="";long appointmentTypeId=0;

		vistMap=patientRegistrationDao.findOpdList(serviceNo);int count=1;if(vistMap.size()>0&&!vistMap.isEmpty()){visitList=(List<Visit>)vistMap.get("visitList");if(!visitList.isEmpty()&&visitList.size()>0){for(Visit visit:visitList){Map<String,Object>responsePatientMap=new HashMap<String,Object>();if(visit.getPatient().getPatientName()!=null){patientName=visit.getPatient().getPatientName();}tokenNo=visit.getTokenNo();
		// doctorName=visit.getd
		visitDate=visit.getVisitDate();visitStatus=visit.getVisitStatus();visitId=visit.getVisitId();serviceNo=visit.getPatient().getServiceNo();dateOfBirth=visit.getPatient().getDateOfBirth();age=HMSUtil.calculateAge(visit.getPatient().getDateOfBirth());departmentName=visit.getPatient().getMasTrade().getTradeName();departmentId=visit.getPatient().getMasTrade().getTradeId();

		*hospitalName=*visit.getPatient().getMasEmployee().getMasHospital().getHospitalName();*hospitalId=*visit.getPatient().getMasEmployee().getMasHospital().getHospitalId();

		hospitalName=visit.getMasHospital().getHospitalName();hospitalId=visit.getMasHospital().getHospitalId();

		appointmentType=visit.getMasAppointmentType().getAppointmentTypeName();appointmentTypeId=visit.getMasAppointmentType().getAppointmentTypeId();
		// patient related details
		gender=visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName();relation=visit.getPatient().getMasRelation().getRelationName();responsePatientMap.put("serviceNo",serviceNo);responsePatientMap.put("patientName",patientName);responsePatientMap.put("tokenNo",tokenNo);responsePatientMap.put("visitDate",visitDate);responsePatientMap.put("visitStatus",visitStatus);responsePatientMap.put("age",age);responsePatientMap.put("serviceNo",serviceNo);responsePatientMap.put("gender",gender);responsePatientMap.put("departmentName",departmentName);responsePatientMap.put("relation",relation);responsePatientMap.put("visitId",visitId);

		responsePatientMap.put("dateOfBirth",dateOfBirth);responsePatientMap.put("departmentId",departmentId);responsePatientMap.put("hospitalName",hospitalName);responsePatientMap.put("hospitalId",hospitalId);responsePatientMap.put("appointmentType",appointmentType);responsePatientMap.put("appointmentTypeId",appointmentTypeId);

		data.put(count++,responsePatientMap);

		}}}map.put("data",data);map.put("count",data.size());if(json.has("toDate")){map.put("msg","Appointment has been cancelled");}else{map.put("msg","List of Detail");}map.put("status","1");return map;

	}

	@Override
	public Map<String, Object> checkVisitExistOrNot(String requestData) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject(requestData);

		Date visitDate = null;

		Timestamp visitDateTime = null;
		long visitid = 0;
		long deptId = json.getLong("deptId");
		long appointmentTypeId = json.getLong("appointmentTypeId");
		long hospitalId = json.getLong("hospitalId");
		String uhidNo = json.getString("uhidNo");
		String visitFlag = json.getString("visitFlag");

		String visitdate = json.getString("visitDate");
		try {
			visitDate = HMSUtil.convertStringDateToUtilDate(visitdate, "dd-MM-yyyy");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// visitDateTime = new Timestamp(visitDate.getTime());

		map = patientRegistrationDao.checkVisitExist(deptId, appointmentTypeId, hospitalId, uhidNo, visitFlag,
				visitDate);
		return map;
	}

	@Override
	public Map<Long, String> getGenderList() {
		// TODO Auto-generated method stub
		Map<Long, String> map = new HashMap<Long, String>();
		List<MasAdministrativeSex> genderList = new ArrayList<MasAdministrativeSex>();

		genderList = patientRegistrationDao.getGenderList();
		for (MasAdministrativeSex masAdministrativeSex : genderList) {
			map.put(masAdministrativeSex.getAdministrativeSexId(), masAdministrativeSex.getAdministrativeSexName());
		}

		return map;
	}

	@Override
	public Map<Long, String> getRelationrList() {
		// TODO Auto-generated method stub
		Map<Long, String> map = new HashMap<Long, String>();
		List<MasRelation> genderList = new ArrayList<MasRelation>();

		genderList = patientRegistrationDao.getRelationList();
		for (MasRelation masRelation : genderList) {
			map.put(masRelation.getRelationId(), masRelation.getRelationName());
		}

		return map;
	}

	@Override
	public Map<String, Object> getDepartmentList(Map<String, String> requestData) {
		// TODO Auto-generated method stub
		long hospitalId = Long.parseLong(requestData.get("hospitalId"));
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<Long, String> map = new HashMap<Long, String>();
		List<MasAppointmentSession> departmentList = new ArrayList<MasAppointmentSession>();

		departmentList = appointmentDAO.getDepartmentListByHospital(hospitalId);
		for (MasAppointmentSession masAppointmentSession : departmentList) {
			map.put(masAppointmentSession.getMasDepartment().getDepartmentId(),
					masAppointmentSession.getMasDepartment().getDepartmentName());
		}
		if (departmentList != null && departmentList.size() > 0) {
			map1.put("data", map);
			map1.put("status", 1);
		} else {
			map1.put("data", map);
			map1.put("msg", "Data not found");
			map1.put("status", 0);
		}
		return map1;
	}

	@Override
	public Map<String, Object> getLabHistory(String requestData) {
		// TODO Auto-generated method stub
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject(requestData);
		String serviceNo = json.getString("serviceNo");

		Map<String, Object> responseMap = new HashMap<String, Object>();
		String patientName = "";
		String relation = "";
		String age = "";
		String gender = "";
		String hospitalName = "";

		String resultDtId = "";
		String resulthdId = "";
		String dateOfBirth = "";
		String relationName = "";
		String invname = "";
		String uom = "";
		String result = "";
		String range = "";
		String orderDate = "";
		String flag = "";
		String orderhdId = "";
		String ridcId = "";
		String subInvestigationName = "";
		String resultType = "";
		String subUom = "";

		List<Object[]> objectArrayList = new ArrayList<>();
		responseMap = patientRegistrationDao.getLabData(json);
		int count = 1;
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			objectArrayList = (List<Object[]>) responseMap.get("listObject");
			if (!objectArrayList.isEmpty() && objectArrayList.size() > 0) {
				for (Object[] data1 : objectArrayList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					if (data1[0] != null)
						resultDtId = data1[0].toString();
					if (data1[1] != null)
						resulthdId = data1[1].toString();
					if (data1[2] != null)
						patientName = data1[2].toString();
					if (data1[3] != null)
						hospitalName = data1[3].toString();
					if (data1[5] != null)
						relationName = data1[5].toString();
					if (data1[4] != null) {
						dateOfBirth = data1[4].toString();
						Date dob = HMSUtil.convertStringDateToUtilDate(dateOfBirth, "yyyy-MM-dd");
						age = HMSUtil.calculateAge(dob);
					}
					gender = data1[6].toString();
					if (data1[7] != null)
						invname = data1[7].toString();
					if (data1[8] != null) {
						uom = data1[8].toString();
					} else {
						uom = "";
					}
					if (data1[10] != null)
						orderDate = data1[10].toString();
					if (data1[11] != null)
						result = data1[11].toString();

					if (data1[12] != null && data1[13] != null)

						range = data1[12].toString() + "-" + data1[13].toString();

					if (data1[14] != null)

						flag = data1[14].toString();
					if (data1[15] != null)

						orderhdId = data1[15].toString();
					if (data1[16] != null)

						ridcId = data1[16].toString();
					if (data1[17] != null)

						range = data1[17].toString();

					if (data1[18] != null)
						subInvestigationName = data1[18].toString();
					else
						subInvestigationName = "";
					if (data1[19] != null)
						resultType = data1[19].toString();
					else
						resultType = "";
					if (data1[20] != null)
						subUom = data1[20].toString();
					else
						subUom = "";

					map1.put("ridcId", ridcId);
					// map1.put("range", range);
					map1.put("resultDtId", resultDtId);
					map1.put("orderhdId", orderhdId);
					map1.put("patientName", patientName);
					map1.put("hospitalName", hospitalName);
					map1.put("relationName", relationName);
					map1.put("age", age);

					map1.put("gender", gender);
					map1.put("invname", invname);

					map1.put("orderDate", orderDate);
					map1.put("result", result);
					map1.put("range", range);
					if (resultType.equalsIgnoreCase("s")) {
						map1.put("uom", uom);
					} else if (resultType.equalsIgnoreCase("m")) {
						map1.put("uom", subUom);
					} else {
						map1.put("uom", "");
					}

					map1.put("subInvestigationName", subInvestigationName);

					map1.put("flag", flag);
					data.put(count++, map1);
				}
			}
		}
		Integer tm = (Integer) responseMap.get("totalMatches");
		map.put("data", data);
		map.put("count", tm);

		map.put("status", "1");
		return map;
	}

	@Override
	public Map<String, Object> getDoctorListFromMapping(Map<String, String> requestData) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Map<String, Object>> doctorDeptMappingList = new ArrayList<Map<String, Object>>();
		long departmentId = Long.parseLong(requestData.get("deptId"));
		long hospitalId = Long.parseLong(requestData.get("hospitalId"));

		List<MasDoctorMapping> doctorMappingList = appointmentDAO.getDoctorListFromMapping(departmentId, hospitalId);
		if (doctorMappingList.size() > 0) {
			for (MasDoctorMapping mapping : doctorMappingList) {
				Map<String, Object> map = new HashMap<String, Object>();
				long doctorId = mapping.getDoctorId().getUserId();
				String doctorName = mapping.getDoctorId().getFirstName();
				map.put("doctorId", doctorId);
				map.put("doctorName", doctorName);
				doctorDeptMappingList.add(map);
			}
			if (doctorDeptMappingList != null && doctorDeptMappingList.size() > 0) {
				responseMap.put("doctorDeptMappingList", doctorDeptMappingList);
				responseMap.put("status", 1);
			}
		} else {
			responseMap.put("doctorDeptMappingList", doctorDeptMappingList);
			responseMap.put("msg", "Data not found");
			responseMap.put("status", 0);
		}
		return responseMap;

	}

	@Override
	public Map<Long, String> getHospitalListFromAppSetup() {
		// TODO Auto-generated method stub
		Map<Long, String> map = new HashMap<Long, String>();
		List<MasAppointmentSession> hospitalList = new ArrayList<MasAppointmentSession>();

		hospitalList = appointmentDAO.getHospitalFromAppSetup();
		for (MasAppointmentSession masAppointmentSession : hospitalList) {
			MasHospital masHospital = masAppointmentSession.getMasHospital();
			map.put(masHospital.getHospitalId(), masHospital.getHospitalName());
		}

		return map;
	}

	*/

}
