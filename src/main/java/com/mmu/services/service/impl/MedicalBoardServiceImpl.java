package com.mmu.services.service.impl;

import java.time.LocalDate;
import java.time.Period;
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
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.DgOrderhdDao;
import com.mmu.services.dao.MasterDao;
import com.mmu.services.dao.MedicalBoardDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.OpdDao;
import com.mmu.services.dao.OpdPatientDetailDao;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasEmployeeCategory;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasServiceType;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.PatientMedBoard;
import com.mmu.services.entity.PatientMedBoardChecklist;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.MedicalBoardService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;
import com.mmu.services.utils.ValidateUtils;

@Repository
public class MedicalBoardServiceImpl implements MedicalBoardService {

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	MedicalBoardDao mbDao;

	@Autowired
	OpdDao opdDao;

	@Autowired
	DgOrderhdDao dgOrderhdDao;

	@Autowired
	OpdPatientDetailDao opdPatientDetailDao;

	@Autowired
	MedicalExamDAO medicalExamDAO;

	@Autowired
	MasterDao masterDao;
	@Autowired
	SystemAdminDao systemAdminDao;

	/*
	 * @Override public String getPIMBWaitingList(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { List<Object>
	 * responseList = new ArrayList<Object>(); JSONObject json = new JSONObject();
	 * List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
	 * List<Visit>listVisit=null; Map<String, Object>map=null; if
	 * (!jsondata.isEmpty()) { map=mbDao.getValidateMedicalBoardList(jsondata); int
	 * count=(int) map.get("count"); listVisit=(List<Visit>) map.get("listVisit");
	 * 
	 * if (CollectionUtils.isNotEmpty(listVisit)) { for (Visit visit : listVisit) {
	 * LocalDate today = LocalDate.now(); HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("patientName",
	 * visit.getPatient().getPatientName());
	 * 
	 * if(visit.getPatient()!=null && visit.getPatient().getDateOfBirth()!=null) {
	 * Date s=visit.getPatient().getDateOfBirth(); Calendar lCal =
	 * Calendar.getInstance(); lCal.setTime(s); int yr=lCal.get(Calendar.YEAR); int
	 * mn=lCal.get(Calendar.MONTH) + 1; int dt=lCal.get(Calendar.DATE);
	 * 
	 * LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
	 * System.out.println("birthday"+birthday); Period p = Period.between(birthday,
	 * today); String ageFull = HMSUtil.calculateAge(s); jsonMap.put("age",
	 * ageFull); jsonMap.put("ageValue", p.getYears()); } else { jsonMap.put("age",
	 * ""); }
	 * 
	 * 
	 * jsonMap.put("gender",
	 * visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
	 * if(visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
	 * jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName()); }
	 * else { jsonMap.put("rankName", ""); }
	 * jsonMap.put("meTypeName",visit.getMasMedExam().getMedicalExamName());
	 * jsonMap.put("meTypeCode",visit.getMasMedExam().getMedicalExamCode()); String
	 * visitstatus=""; if(StringUtils.isNotEmpty(visit.getExamStatus())) {
	 * if(visit.getExamStatus().equalsIgnoreCase("I")) { visitstatus="Initiated"; }
	 * if(visit.getExamStatus().equalsIgnoreCase("S")) { visitstatus="Saved"; } }
	 * jsonMap.put("visitFlag",visit.getVisitFlag());
	 * jsonMap.put("status",visitstatus); jsonMap.put("visitId",visit.getVisitId());
	 * jsonMap.put("patientId",visit.getPatientId());
	 * 
	 * jsonMap.put("serviceNo",visit.getPatient().getServiceNo());
	 * jsonMap.put("departmentId",visit.getDepartmentId());
	 * 
	 * jsonMap.put("tokenNo",visit.getTokenNo()); if(visit.getVisitDate()!=null) {
	 * Date s=visit.getVisitDate(); String visitDate =
	 * HMSUtil.convertDateToStringFormat(s, "dd/MM/yyyy"); jsonMap.put("visitDate",
	 * visitDate);
	 * 
	 * } else { jsonMap.put("visitDate", ""); } responseList.add(jsonMap);
	 * 
	 * 
	 * } if (responseList != null && responseList.size() > 0) { json.put("data",
	 * responseList); json.put("status", 1); json.put("count", count);
	 * 
	 * } else { json.put("data", responseList); json.put("msg", "Data not found");
	 * json.put("status", 0); } } else { try { json.put("msg",
	 * "Visit ID data not found"); json.put("status", 0); } catch (JSONException e)
	 * { e.printStackTrace(); } }
	 * 
	 * } return json.toString(); }
	 * 
	 * @Override public String savePiMBInvestigation(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { String
	 * opdInvesti = null;
	 * 
	 * // TODO Auto-generated method stub JSONObject json = new JSONObject(); try {
	 * 
	 * if (!jsondata.isEmpty()) {
	 * 
	 * opdInvesti = mbDao.savePreliminaryMBDetails(jsondata);
	 * 
	 * 
	 * if (opdInvesti != null && opdInvesti.equalsIgnoreCase("Successfully saved"))
	 * { json.put("msg", "Premiminary MB Details Saved successfully ");
	 * json.put("status", "1"); } else if (opdInvesti != null &&
	 * opdInvesti.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", opdInvesti); json.put("status", "0"); }
	 * 
	 * }
	 * 
	 * else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String getMbPreAssestmentWaitingList(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * List<Object> responseList = new ArrayList<Object>(); JSONObject json = new
	 * JSONObject(); List<HashMap<String, Object>> c = new ArrayList<HashMap<String,
	 * Object>>(); List<Visit>listVisit=null; Map<String, Object>map=null; if
	 * (!jsondata.isEmpty()) {
	 * map=mbDao.getValidateMedicalBoardWaitingList(jsondata); int count=(int)
	 * map.get("count"); listVisit=(List<Visit>) map.get("listVisit");
	 * 
	 * if (CollectionUtils.isNotEmpty(listVisit)) { for (Visit visit : listVisit) {
	 * LocalDate today = LocalDate.now(); HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("patientName",
	 * visit.getPatient().getPatientName());
	 * 
	 * if(visit.getPatient()!=null && visit.getPatient().getDateOfBirth()!=null) {
	 * Date s=visit.getPatient().getDateOfBirth(); Calendar lCal =
	 * Calendar.getInstance(); lCal.setTime(s); int yr=lCal.get(Calendar.YEAR); int
	 * mn=lCal.get(Calendar.MONTH) + 1; int dt=lCal.get(Calendar.DATE);
	 * 
	 * LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
	 * System.out.println("birthday"+birthday); Period p = Period.between(birthday,
	 * today); String ageFull = HMSUtil.calculateAge(s); jsonMap.put("age",
	 * ageFull); jsonMap.put("ageValue", p.getYears()); } else { jsonMap.put("age",
	 * ""); }
	 * 
	 * 
	 * jsonMap.put("gender",
	 * visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
	 * if(visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
	 * jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName()); }
	 * else { jsonMap.put("rankName", ""); }
	 * jsonMap.put("meTypeName",visit.getMasMedExam().getMedicalExamName());
	 * jsonMap.put("meTypeCode",visit.getMasMedExam().getMedicalExamCode()); String
	 * visitstatus=""; if(StringUtils.isNotEmpty(visit.getExamStatus())) {
	 * if(visit.getExamStatus().equalsIgnoreCase("V")) { visitstatus="Initiate"; } }
	 * 
	 * jsonMap.put("status",visitstatus);
	 * jsonMap.put("visitStatus",visit.getVisitStatus());
	 * if(visit.getExamStatus()!=null && visit.getExamStatus()!="") {
	 * jsonMap.put("examStatus",visit.getExamStatus()); }
	 * jsonMap.put("visitId",visit.getVisitId());
	 * jsonMap.put("patientId",visit.getPatientId());
	 * 
	 * jsonMap.put("serviceNo",visit.getPatient().getServiceNo());
	 * jsonMap.put("departmentId",visit.getDepartmentId());
	 * jsonMap.put("visitStatus",visit.getVisitStatus());
	 * jsonMap.put("visitFlag",visit.getVisitFlag()); if(visit.getTokenNo()!=null) {
	 * jsonMap.put("tokenNo",visit.getTokenNo()); } else {
	 * jsonMap.put("tokenNo",""); } if(visit.getVisitDate()!=null) { Date
	 * s=visit.getVisitDate(); String visitDate =
	 * HMSUtil.convertDateToStringFormat(s, "dd/MM/yyyy"); jsonMap.put("visitDate",
	 * visitDate);
	 * 
	 * } else { jsonMap.put("visitDate", ""); } responseList.add(jsonMap); } if
	 * (responseList != null && responseList.size() > 0) { json.put("data",
	 * responseList); json.put("status", 1); json.put("count", count);
	 * 
	 * } else { json.put("data", responseList); json.put("msg", "Data not found");
	 * json.put("status", 0); } } else { try { json.put("msg",
	 * "Visit ID data not found"); json.put("status", 0); } catch (JSONException e)
	 * { e.printStackTrace(); } }
	 * 
	 * } return json.toString(); }
	 * 
	 * @Override public String getMbPreAssestmentDetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Long patientId=null; JSONObject json = new JSONObject(); List<HashMap<String,
	 * Object>> c = new ArrayList<HashMap<String, Object>>(); List<HashMap<String,
	 * Object>> c1 = new ArrayList<HashMap<String, Object>>(); if
	 * (!jsondata.isEmpty()) {
	 * 
	 * JSONObject nullbalankvalidation = null; nullbalankvalidation =
	 * ValidateUtils.checkPatientVisitId(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else {
	 * 
	 * List<PatientMedicalCat> getPatientVisit =
	 * mbDao.getPatientMedicalBoardDetails(Long.parseLong(jsondata.get("visitId").
	 * toString())); Map<String,Object> getDgOrder =
	 * mbDao.getPatientMedicalBoardDetailsDgOrder(jsondata); List<DgOrderdt>
	 * getDgOderData = (List<DgOrderdt>) getDgOrder.get("list");
	 * 
	 * if (getPatientVisit != null && ! CollectionUtils.isEmpty(getPatientVisit) ) {
	 * 
	 * try { for (PatientMedicalCat v : getPatientVisit) { //if
	 * (v.getVisitStatus().equals("w")||v.getVisitStatus().equals("p")){
	 * HashMap<String, Object> pt = new HashMap<String, Object>();
	 * 
	 * pt.put("visitId", v.getVisitId()); pt.put("patientId", v.getPatientId());
	 * if(v.getMasIcd()!=null && v.getMasIcd().getIcdName()!=null) {
	 * pt.put("icdName", v.getMasIcd().getIcdName()); } else { pt.put("icdName",
	 * ""); } if(v.getSystem()!=null) { pt.put("system", v.getSystem()); } else {
	 * pt.put("system", ""); } if(v.getMbStatus()!="") { pt.put("mbStatus",
	 * v.getMbStatus()); } else { pt.put("mbStatus", ""); }
	 * if(v.getMasMedicalCategory()!=null) {
	 * pt.put("medicalCategory",v.getMasMedicalCategory().getMedicalCategoryName());
	 * } else { pt.put("medicalCategory",""); } if(v.getCategoryType()!=null) {
	 * pt.put("categoryType", v.getCategoryType()); } else { pt.put("categoryType",
	 * ""); } if(v.getCategoryDate()!=null) { String dateOfPresentCategory =
	 * HMSUtil.convertDateToStringFormat(v.getCategoryDate(), "dd/MM/yyyy");
	 * pt.put("categoryDate", dateOfPresentCategory); } else {
	 * pt.put("categoryDate", ""); } if(v.getDuration()!=null) {
	 * pt.put("duration",v.getDuration()); } else { pt.put("duration",""); }
	 * if(v.getNextCategoryDate()!=null) { String dateOfNextCategory =
	 * HMSUtil.convertDateToStringFormat(v.getNextCategoryDate(), "dd/MM/yyyy");
	 * pt.put("nextCategoryDate",dateOfNextCategory ); } else {
	 * pt.put("nextCategoryDate",""); } pt.put("patientMedicalCategoryId",
	 * v.getMedicalCatId()); pt.put("diagnosisId", v.getIcdId()); pt.put("applyFor",
	 * v.getApplyFor()); if(v.getDateOfOrigin()!=null) { String dateOfOrigin =
	 * HMSUtil.convertDateToStringFormat(v.getDateOfOrigin(), "dd/MM/yyyy");
	 * pt.put("dateOfOrigin",dateOfOrigin); } if(v.getPlaceOfOrigin()!=null) {
	 * pt.put("placeOfOrigin",v.getPlaceOfOrigin()); } else {
	 * pt.put("placeOfOrigin",""); } pt.put("medicalCategoryId",
	 * v.getMedicalCategoryId()); pt.put("recommendStatus", v.getRecommendFlag());
	 * pt.put("fitFlag", v.getpMedFitFlag()); pt.put("fitCatId", v.getpMedCatId());
	 * if(v.getMasMedicalCategoryFit()!=null) {
	 * pt.put("medicalCategoryFit",v.getMasMedicalCategoryFit().
	 * getMedicalCategoryName()); } else { pt.put("medicalCategoryFit",""); }
	 * if(v.getpMedCatDate()!=null) { String fitCatDate =
	 * HMSUtil.convertDateToStringFormat(v.getpMedCatDate(), "dd/MM/yyyy");
	 * pt.put("fitCatDate",fitCatDate); }
	 * 
	 * c.add(pt); } for (DgOrderdt dg : getDgOderData) { HashMap<String, Object>
	 * dData = new HashMap<String, Object>();
	 * dData.put("orderHdId",dg.getOrderhdId());
	 * dData.put("orderDtId",dg.getOrderdtId());
	 * dData.put("inveId",dg.getDgMasInvestigations().getInvestigationId());
	 * dData.put("inveName",dg.getDgMasInvestigations().getInvestigationName());
	 * c1.add(dData); }
	 * 
	 * json.put("data", c); json.put("dataDgDt", c1); json.put("size", c.size()); //
	 * json.put("Visit List", getvisit); json.put("msg",
	 * "Medical board List  get  sucessfull... "); json.put("status", "1"); }
	 * 
	 * 
	 * catch(Exception e) { e.printStackTrace(); return
	 * "{\"status\":\"0\",\"msg\":\"Somting went wrong}"; } } else { try {
	 * json.put("msg", "Visit ID data not found"); json.put("status", 0); } catch
	 * (JSONException e) { e.printStackTrace(); } } }
	 * 
	 * } return json.toString();
	 * 
	 * }
	 * 
	 * @Override public String saveMbPreAssestmentDetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * // TODO Auto-generated method stub JSONObject json = new JSONObject();
	 * OpdPatientDetail opddetails = new OpdPatientDetail(); Calendar calendar =
	 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); String opdDensistyHd=null;
	 * 
	 * try { if (!jsondata.isEmpty()) { JSONArray jarr=new
	 * JSONArray(jsondata.get("jsondata").toString()); JSONObject payload =
	 * (JSONObject) jarr.get(0);
	 * 
	 * //opddetails.setUhidNo(Long.parseLong(payload.get("uhidNo").toString()));
	 * opddetails.setPatientId(Long.parseLong(payload.get("patientId").toString()));
	 * opddetails.setVisitId(Long.parseLong(payload.get("visitId").toString()));
	 * opddetails.setHeight(payload.get("height").toString());
	 * opddetails.setWeight(payload.get("weight").toString());
	 * opddetails.setIdealWeight(payload.get("idealWeight").toString()); if
	 * (payload.get("varation") != null && !payload.get("varation").equals("")) {
	 * Double bd = new Double(payload.get("varation").toString());
	 * opddetails.setVaration(bd); }
	 * opddetails.setTemperature(payload.get("temperature").toString());
	 * opddetails.setBpSystolic(payload.get("bp").toString());
	 * opddetails.setBpDiastolic(payload.get("bp1").toString());
	 * opddetails.setPulse(payload.get("pulse").toString());
	 * opddetails.setSpo2(payload.get("spo2").toString());
	 * opddetails.setBmi(payload.get("bmi").toString());
	 * opddetails.setRr(payload.get("rr").toString());
	 * opddetails.setOpdDate(ourJavaTimestampObject);
	 * 
	 * if(!jsondata.get("obsistyCheckAlready").equals("exits")) {
	 * if(payload.get("obsistyMark")!=null &&
	 * !payload.get("obsistyMark").equals("")) {
	 * 
	 * opdDensistyHd= mbDao.opdObsisty(payload); } } String resp =
	 * mbDao.opdVitalDetails(opddetails,jsondata);
	 * 
	 * 
	 * if (resp != null && resp.equalsIgnoreCase("200")) { json.put("msg",
	 * " Vitals Details Insert successfully "); json.put("status", "1"); } else if
	 * (resp != null && resp.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", resp); json.put("status", "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String saveReferforOpinionMBDetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { String
	 * opd = null;
	 * 
	 * JSONObject json = new JSONObject(); Calendar calendar =
	 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); String opdDensistyHd=null;
	 * 
	 * try { if (!jsondata.isEmpty()) { JSONObject nullbalankvalidation = null;
	 * nullbalankvalidation =
	 * ValidateUtils.addVitalPreConsulataionDetailsvalidation(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else {
	 * 
	 * opd = mbDao.saveReferforOpinionMBDetails(jsondata);
	 * 
	 * }
	 * 
	 * 
	 * if (opd != null && opd.equalsIgnoreCase("Successfully saved")) {
	 * json.put("msg",
	 * "Refer for Opinion and Medical Board Details Saved successfully ");
	 * json.put("status", "1"); } else if (opd != null &&
	 * opd.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", opd); json.put("status", "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String MbPatientDetails(HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { Long
	 * patientId=null; JSONObject json = new JSONObject(); List<HashMap<String,
	 * Object>> c = new ArrayList<HashMap<String, Object>>(); if
	 * (!jsondata.isEmpty()) {
	 * 
	 * JSONObject nullbalankvalidation = null; nullbalankvalidation =
	 * ValidateUtils.checkPatientVisit(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else {
	 * 
	 * List<Visit> getPatientVisit =
	 * mbDao.getPatientVisit(Long.parseLong(jsondata.get("visitId").toString()));
	 * List<OpdPatientDetail> getvitalDetails =
	 * mbDao.getVitalRecord(Long.parseLong(jsondata.get("visitId").toString()));
	 * List<MasServiceType>listMasServiceType=masterDao.getServiceTypeList();
	 * 
	 * //Map<String,Object> mapObject =
	 * dgOrderhdDao.getOpdObesityHd(Long.parseLong(jsondata.get("visitId").toString(
	 * )));
	 * 
	 * if (getPatientVisit != null && ! CollectionUtils.isEmpty(getPatientVisit) ) {
	 * 
	 * try { for (Visit v : getPatientVisit) { //if
	 * (v.getVisitStatus().equals("w")||v.getVisitStatus().equals("p")){
	 * HashMap<String, Object> pt = new HashMap<String, Object>();
	 * if(v.getPatient().getDateOfBirth()!=null) { Date
	 * s=v.getPatient().getDateOfBirth(); Period p=ProjectUtils.getDOB(s); String
	 * ageFull = HMSUtil.calculateAge(s); //pt.put("age", ageFull); pt.put("age",
	 * p.getYears()+" years"); pt.put("ageValue", p.getYears());
	 * 
	 * }
	 * 
	 * pt.put("visitId", v.getVisitId()); pt.put("patientId", v.getPatientId());
	 * patientId= v.getPatientId(); List<OpdObesityHd> getObsistyDetails =
	 * opdDao.getObsisityRecord(patientId); pt.put("tokenNo", v.getTokenNo());
	 * 
	 * pt.put("serviceNo", v.getPatient().getServiceNo());
	 * if(v.getMasMedExam()!=null && v.getMasMedExam().getMedicalExamName()!=null) {
	 * pt.put("mbType", v.getMasMedExam().getMedicalExamName()); }
	 * if(v.getPatient()!=null && v.getPatient().getMasEmployee() !=null &&
	 * v.getPatient().getMasEmployee().getEmployeeName()!=null) {
	 * pt.put("empName",v.getPatient().getMasEmployee().getEmployeeName()); }
	 * if(v.getPatient()!=null && v.getPatient().getRankId()!=null) { pt.put("rank",
	 * v.getPatient().getMasRank().getRankName()); pt.put("rankName",
	 * v.getPatient().getMasRank().getRankName());
	 * 
	 * } try { if(v.getPatient()!=null && v.getPatient().getTradeId()!=null) {
	 * pt.put("tradeBranch", v.getPatient().getMasTrade().getTradeName()); } }
	 * catch(Exception e) { pt.put("tradeBranch", ""); e.printStackTrace(); }
	 * if(v.getPatient()!=null && v.getPatient().getServiceJoinDate()!=null) {
	 * 
	 * Date s1=v.getPatient().getServiceJoinDate(); Period
	 * serviceDate=ProjectUtils.getDOB(s1);
	 * pt.put("totalService",serviceDate.getYears()+" years"); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getTotalService()!=null) {
	 * pt.put("totalService",v.getMasMedicalExamReport().getTotalService()); }
	 * 
	 * if(v.getPatient()!=null && v.getPatient().getUnitId()!=null) { pt.put("unit",
	 * v.getPatient().getMasUnit().getUnitName()); } if(v.getPatient()!=null &&
	 * v.getPatient().getReligionId()!=null) { pt.put("religionName",
	 * v.getPatient().getMasReligion().getReligionName()); } if(v.getPatient()!=null
	 * && v.getPatient().getCommandId()!=null &&
	 * v.getPatient().getMasCommand().getCommandName()!=null) {
	 * pt.put("religionCommand", v.getPatient().getMasCommand().getCommandName()); }
	 * if(v.getPatient()!=null && v.getPatient().getMasAdministrativeSex()!=null &&
	 * v.getPatient().getMasAdministrativeSex().getAdministrativeSexName()!=null) {
	 * pt.put("gender",
	 * v.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
	 * pt.put("genderId",
	 * v.getPatient().getMasAdministrativeSex().getAdministrativeSexId()); }
	 * if(v.getPatient()!=null && v.getPatient().getMasRelation()!=null &&
	 * v.getPatient().getMasRelation().getRelationName()!=null) { pt.put("relation",
	 * v.getPatient().getMasRelation().getRelationName()); } if(v.getPatient()!=null
	 * && v.getPatient().getMedicalCategoryId()!=null) { pt.put("medicalCategory",
	 * v.getPatient().getMasMedicalCategory().getMedicalCategoryName()); }
	 * if(v.getPatient()!=null && v.getPatient().getMasEmployeeCategory()!=null) {
	 * pt.put("masEmployeeCategory",v.getPatient().getMasEmployeeCategory().
	 * getEmployeeCategoryName()); } if(v.getPatient()!=null &&
	 * v.getPatient().getMasMaritalStatus()!=null &&
	 * v.getPatient().getMaritalStatusId()!=null &&
	 * v.getPatient().getMaritalStatusId()!=0) { pt.put("marritalStatus",
	 * v.getPatient().getMasMaritalStatus().getMaritalStatusName());
	 * pt.put("maritalStatusId", v.getPatient().getMaritalStatusId()); }
	 * if(v.getPatient()!=null && v.getPatient().getRecordOfficeAddressId()!=null) {
	 * pt.put("recordOfficeAddress",
	 * v.getPatient().getMasRecordOfficeAddress().getRecordOfficeAddressName()); }
	 * pt.put("patientName", v.getPatient().getPatientName());
	 * 
	 * 
	 * String dateOfBirth=""; Date dateOfBirthForDateFun=null;
	 * if(v.getPatient()!=null && v.getPatient().getDateOfBirth()!=null ) {
	 * dateOfBirth =
	 * HMSUtil.convertDateToStringFormat(v.getPatient().getDateOfBirth(),
	 * "dd/MM/yyyy"); dateOfBirthForDateFun=v.getPatient().getDateOfBirth();
	 * //String dateOfBirth =
	 * HMSUtil.getDateWithoutTime1(v.getPatient().getDateOfBirth()); pt.put("dob",
	 * dateOfBirth); pt.put("dateOfBirth", dateOfBirth); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getDateOfBirth()!=null ) { dateOfBirth =
	 * HMSUtil.convertDateToStringFormat(v.getMasMedicalExamReport().getDateOfBirth(
	 * ), "dd/MM/yyyy");
	 * dateOfBirthForDateFun=v.getMasMedicalExamReport().getDateOfBirth(); //String
	 * dateOfBirth = HMSUtil.getDateWithoutTime1(v.getPatient().getDateOfBirth());
	 * pt.put("dob", dateOfBirth); pt.put("dateOfBirth", dateOfBirth); }
	 * 
	 * 
	 * 
	 * 
	 * if(v.getPatient().getAddressLine1()!=null) pt.put("address",
	 * v.getPatient().getAddressLine1()); else { pt.put("address", ""); }
	 * if(v.getPatient()!=null && v.getPatient().getCity()!=null) { pt.put("city",
	 * v.getPatient().getCity()); } if(v.getPatient()!=null &&
	 * v.getPatient().getStateId()!=null) { pt.put("state",
	 * v.getPatient().getMasState().getStateName()); } pt.put("pincode",
	 * v.getPatient().getPincode()); pt.put("mobileno",
	 * v.getPatient().getMobileNumber()); pt.put("email",
	 * v.getPatient().getEmailId());
	 * 
	 * if(v.getPatient()!=null && v.getPatient().getDuration()!=null) {
	 * pt.put("duration", v.getPatient().getDuration()); } if(v.getPatient()!=null
	 * && v.getPatient().getMasMedicalCategory()!=null &&
	 * v.getPatient().getMasMedicalCategory().getMedicalCategoryName()!=null ) {
	 * pt.put("medicalCategogyName",v.getPatient().getMasMedicalCategory().
	 * getMedicalCategoryName());
	 * pt.put("medicalCategogyId",v.getPatient().getMasMedicalCategory().
	 * getMedicalCategoryId()); } if(v.getPatient()!=null &&
	 * v.getPatient().getMasMedicalCategory()!=null &&
	 * v.getPatient().getMasMedicalCategory().getFitFlag()!=null ) {
	 * pt.put("fitFlag",v.getPatient().getMasMedicalCategory().getFitFlag()); }
	 * if(v.getPatient()!=null && v.getPatient()!=null &&
	 * v.getPatient().getMedCatDate()!=null ) { String dateOfCategory =
	 * HMSUtil.convertDateToStringFormat(v.getPatient().getMedCatDate(),
	 * "dd/MM/yyyy"); pt.put("medicalCategogyDate",dateOfCategory); }
	 * 
	 * 
	 * 
	 * 
	 * if(v.getOpdPatientDetails()!=null &&
	 * v.getOpdPatientDetails().getOpdPatientDetailsId()!=null) {
	 * pt.put("opdPatientDetailId",v.getOpdPatientDetails().getOpdPatientDetailsId()
	 * ); } if(v.getDepartmentId()!=null) {
	 * pt.put("departmentId",v.getDepartmentId()); }
	 * if(v.getOpdPatientDetails()!=null &&
	 * StringUtils.isNotEmpty(v.getOpdPatientDetails().getDisposalDays())) {
	 * pt.put("disposalDays",v.getOpdPatientDetails().getDisposalDays()); }
	 * if(v.getOpdPatientDetails()!=null &&
	 * v.getOpdPatientDetails().getDisposal1Id()!=null) { Long disposalId
	 * =v.getOpdPatientDetails().getDisposal1Id();
	 * pt.put("disposalIdValue",disposalId); } if(v.getVisitStatus()!=null &&
	 * v.getVisitStatus()!="") { pt.put("visitStatusV",v.getVisitStatus()); }
	 * if(v.getOpdPatientDetails()!=null &&
	 * v.getOpdPatientDetails().getOpdPatientDetailsId()!=null) {
	 * pt.put("opdPatientDetailsId",v.getOpdPatientDetails().getOpdPatientDetailsId(
	 * )); } String visitstatus = "";
	 * 
	 * 
	 * MasMedicalExamReport masMedicalExamReport = null; masMedicalExamReport =
	 * v.getMasMedicalExamReport(); if (masMedicalExamReport == null) { visitstatus
	 * = "New"; }
	 * 
	 * if (masMedicalExamReport != null &&
	 * v.getMasMedicalExamReport().getStatus()!=null) {
	 * 
	 * if(masMedicalExamReport != null &&
	 * masMedicalExamReport.getMediceExamDate()!=null) {
	 * if(masMedicalExamReport.getMediceExamDate()!=null &&
	 * v.getPatient().getDateOfBirth()!=null) { Date
	 * getDob=v.getPatient().getDateOfBirth(); Date
	 * dateOfBoard=masMedicalExamReport.getMediceExamDate(); String
	 * dateAgeAtTimeOfMe=HMSUtil.getDateBetweenTwoDate(dateOfBoard,getDob);
	 * pt.put("ageOfBoard",dateAgeAtTimeOfMe); } }
	 * 
	 * if (v.getMasMedicalExamReport().getStatus().equalsIgnoreCase("s")) {
	 * visitstatus = "Saved"; }
	 * 
	 * if (v.getMasMedicalExamReport().getStatus().equalsIgnoreCase("af") ||
	 * v.getMasMedicalExamReport().getStatus().equalsIgnoreCase("f")) { visitstatus
	 * = "Forwarded"; } if
	 * (v.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rj")) {
	 * visitstatus = "Rejected"; } if
	 * (v.getMasMedicalExamReport().getStatus().equalsIgnoreCase("pe")) {
	 * visitstatus = "Pending"; } if
	 * (v.getMasMedicalExamReport().getStatus().equalsIgnoreCase("ac")) {
	 * visitstatus = "Approved"; } } else { visitstatus = "New"; } if
	 * (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getRidcId()!=null) {
	 * pt.put("ridcId",v.getMasMedicalExamReport().getRidcId()); }else {
	 * pt.put("ridcId",""); }
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getAuthority() != null) { pt.put("authority",
	 * v.getMasMedicalExamReport().getAuthority()); } else { pt.put("authority",
	 * ""); } if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPlace() != null) { pt.put("place",
	 * v.getMasMedicalExamReport().getPlace()); } else { pt.put("place", ""); }
	 * 
	 * 
	 * String dateAME = ""; if (v.getMasMedicalExamReport() != null &&
	 * v.getMasMedicalExamReport().getDateMedicalBoardExam() != null) { dateAME =
	 * HMSUtil.convertDateToStringFormat(
	 * v.getMasMedicalExamReport().getDateMedicalBoardExam(), "dd/MM/yyyy");
	 * pt.put("dateAME", dateAME); } else { pt.put("dateAME", ""); }
	 * 
	 * String dateMedicalExam = ""; if (v.getMasMedicalExamReport() != null &&
	 * v.getMasMedicalExamReport().getMediceExamDate() != null) { dateMedicalExam =
	 * HMSUtil.convertDateToStringFormat(
	 * v.getMasMedicalExamReport().getMediceExamDate(), "dd/MM/yyyy");
	 * pt.put("dateOfExam", dateMedicalExam); } else { pt.put("dateOfExam", ""); }
	 * 
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getTypeofcommision() != null) {
	 * pt.put("typeOfcommision", v.getMasMedicalExamReport().getTypeofcommision());
	 * } else { pt.put("typeOfcommision", ""); }
	 * 
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getApparentAge() != null) {
	 * pt.put("ageOfPatient", v.getMasMedicalExamReport().getApparentAge()); } else
	 * { pt.put("ageOfPatient", ""); }
	 * 
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getApparentAge() != null) { String
	 * ageDateOfExam=MedicalExamServiceImpl.getAgeAtTimeOfMe(v.
	 * getMasMedicalExamReport().getApparentAge()); pt.put("ageForGigi",
	 * ageDateOfExam); } else { pt.put("ageForGigi", ""); }
	 * 
	 * 
	 * if (v.getPatient() != null && v.getPatient().getMedicalCategoryId() != null)
	 * { pt.put("medicalCategory",
	 * v.getPatient().getMasMedicalCategory().getMedicalCategoryName());
	 * pt.put("medicalCompositeNameValue", v.getPatient().getMedicalCategoryId());
	 * String medicalCategoryDate = ""; if
	 * (v.getPatient().getMasMedicalCategory().getLastChgDate() != null) {
	 * medicalCategoryDate = HMSUtil.convertDateToStringFormat(
	 * v.getPatient().getMasMedicalCategory().getLastChgDate(), "dd/MM/yyyy");
	 * pt.put("medicalCategoryDate", medicalCategoryDate); } else {
	 * pt.put("medicalCategoryDate", ""); }
	 * 
	 * } else { pt.put("medicalCategory", ""); pt.put("medicalCompositeNameValue",
	 * ""); pt.put("medicalCategoryDate", ""); }
	 * 
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getStatus() != null) {
	 * pt.put("medicalExamStatus", v.getMasMedicalExamReport().getStatus()); } else
	 * { pt.put("medicalExamStatus", ""); } Users users =null;
	 * if(jsondata.get("userId")!=null) users =
	 * medicalExamDAO.getUserByUserId(Long.parseLong(jsondata.get("userId").toString
	 * ()));
	 * 
	 * List<MasEmployee> listMasEmployee=null; if(users!=null &&
	 * users.getServiceNo()!=null)
	 * listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(users.getServiceNo());
	 * 
	 * 
	 * String rankEmployee=""; String maUserDetail = ""; if
	 * (v.getMasMedicalExamReport()!=null&&v.getMasMedicalExamReport().getUsersMA()
	 * != null && v.getMasMedicalExamReport().getUsersMA().getFirstName() != null) {
	 * maUserDetail = v.getMasMedicalExamReport().getUsersMA().getFirstName();
	 * rankEmployee=medicalExamDAO.getRankOfUserId(v.getMasMedicalExamReport().
	 * getUsersMA());
	 * 
	 * } if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getUsersMA() != null &&
	 * v.getMasMedicalExamReport().getUsersMA().getLastName() != null) {
	 * maUserDetail += "" + v.getMasMedicalExamReport().getUsersMA().getLastName();
	 * }
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getUsersMA() != null) {
	 * 
	 * if(StringUtils.isNotEmpty(rankEmployee)) { //maUserDetail+=" "+rankEmployee;
	 * rankEmployee+=" "+maUserDetail; } pt.put("maUser", rankEmployee);
	 * 
	 * } else { if (users.getFirstName() != null) { maUserDetail =
	 * users.getFirstName(); rankEmployee=medicalExamDAO.getRankOfUserId(users);
	 * 
	 * } if (users.getLastName() != null) { maUserDetail += "" +
	 * users.getLastName(); } if(StringUtils.isNotEmpty(rankEmployee)) {
	 * //maUserDetail+=" "+rankEmployee; rankEmployee+=" "+maUserDetail; }
	 * pt.put("maUser", rankEmployee); }
	 * 
	 * String moUserDetail = ""; if
	 * (v.getMasMedicalExamReport()!=null&&v.getMasMedicalExamReport().getUsersMO()
	 * != null && v.getMasMedicalExamReport().getUsersMO().getFirstName() != null) {
	 * moUserDetail = v.getMasMedicalExamReport().getUsersMO().getFirstName(); } if
	 * (v.getMasMedicalExamReport()!=null&&v.getMasMedicalExamReport().getUsersMO()
	 * != null && v.getMasMedicalExamReport().getUsersMO().getLastName() != null) {
	 * moUserDetail += "" + v.getMasMedicalExamReport().getUsersMO().getLastName();
	 * }
	 * 
	 * if
	 * (v.getMasMedicalExamReport()!=null&&v.getMasMedicalExamReport().getUsersMO()
	 * != null) {
	 * rankEmployee=medicalExamDAO.getRankOfUserId(v.getMasMedicalExamReport().
	 * getUsersMO()); if(StringUtils.isNotEmpty(rankEmployee)) {
	 * //moUserDetail+=" "+rankEmployee; rankEmployee+=" "+moUserDetail; }
	 * pt.put("moUser", rankEmployee); } else { if (users!=null &&
	 * users.getFirstName() != null) { moUserDetail = users.getFirstName();
	 * 
	 * } if (users!=null && users.getLastName() != null) { moUserDetail += "" +
	 * users.getLastName(); } rankEmployee=medicalExamDAO.getRankOfUserId(users);
	 * if(StringUtils.isNotEmpty(rankEmployee)) { //moUserDetail+=" "+rankEmployee;
	 * rankEmployee+=" "+moUserDetail; } pt.put("moUser", rankEmployee); }
	 * 
	 * String rmoUserDetail = ""; if
	 * (v.getMasMedicalExamReport()!=null&&v.getMasMedicalExamReport().getUsersCM()
	 * != null && v.getMasMedicalExamReport().getUsersCM().getFirstName() != null) {
	 * rmoUserDetail = v.getMasMedicalExamReport().getUsersCM().getFirstName();
	 * rankEmployee=medicalExamDAO.getRankOfUserId(v.getMasMedicalExamReport().
	 * getUsersCM()); } if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getUsersCM() != null &&
	 * v.getMasMedicalExamReport().getUsersCM().getLastName() != null) {
	 * rmoUserDetail += "" + v.getMasMedicalExamReport().getUsersCM().getLastName();
	 * 
	 * }
	 * 
	 * if
	 * (v.getMasMedicalExamReport()!=null&&v.getMasMedicalExamReport().getUsersCM()
	 * != null) { if(StringUtils.isNotEmpty(rankEmployee)) {
	 * //rmoUserDetail+=" "+rankEmployee; rankEmployee+=" "+rmoUserDetail; }
	 * pt.put("rMoUser", rankEmployee); } else {
	 * 
	 * if (users.getFirstName() != null) { rmoUserDetail = users.getFirstName();
	 * 
	 * } if (users.getLastName() != null) { rmoUserDetail += "" +
	 * users.getLastName(); } rankEmployee=medicalExamDAO.getRankOfUserId(users);
	 * 
	 * if(StringUtils.isNotEmpty(rankEmployee)) { //rmoUserDetail+=" "+rankEmployee;
	 * rankEmployee+=" "+rmoUserDetail; } pt.put("rMoUser", rankEmployee); }
	 * 
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getFinalobservation() != null) {
	 * pt.put("finalObservationMo",
	 * v.getMasMedicalExamReport().getFinalobservation()); } else {
	 * pt.put("finalObservationMo", ""); }
	 * 
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getAaFinalObservation() != null) {
	 * pt.put("finalObservationRMo",
	 * v.getMasMedicalExamReport().getAaFinalObservation()); } else {
	 * pt.put("finalObservationRMo", ""); }
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPaFinalobservation() != null) {
	 * pt.put("finalObservationPRMo",
	 * v.getMasMedicalExamReport().getPaFinalobservation()); } else {
	 * pt.put("finalObservationPRMo", ""); }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getIdentificationMarks1()!=null) {
	 * pt.put("identificationMarks1",
	 * v.getMasMedicalExamReport().getIdentificationMarks1()); } else {
	 * pt.put("identificationMarks1",""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getIdentificationMarks2()!=null) {
	 * pt.put("identificationMarks2",
	 * v.getMasMedicalExamReport().getIdentificationMarks2()); } else {
	 * pt.put("identificationMarks2",""); }
	 * 
	 * 
	 * String dateOfReport = ""; if (v.getMasMedicalExamReport() != null &&
	 * v.getMasMedicalExamReport().getDateOfReporting() != null) { dateOfReport =
	 * HMSUtil.convertDateToStringFormat(
	 * v.getMasMedicalExamReport().getDateOfReporting(), "dd/MM/yyyy");
	 * 
	 * } pt.put("dateOfReport", dateOfReport);
	 * 
	 * 
	 * String dateOfRelease = ""; if (v.getMasMedicalExamReport() != null &&
	 * v.getMasMedicalExamReport().getDaterelease() != null) { dateOfRelease =
	 * HMSUtil.convertDateToStringFormat(
	 * v.getMasMedicalExamReport().getDaterelease(), "dd/MM/yyyy"); }
	 * pt.put("dateOfRelease", dateOfRelease);
	 * 
	 * Long serviceType = null; if (v.getMasMedicalExamReport() != null &&
	 * v.getMasMedicalExamReport().getServiceTypeId() != null) {
	 * serviceType=v.getMasMedicalExamReport().getServiceTypeId();
	 * 
	 * } pt.put("serviceType", serviceType);
	 * 
	 * 
	 * 
	 * String disabilityBeforeJoining=""; if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getDisabilitybefore()!=null) {
	 * disabilityBeforeJoining=v.getMasMedicalExamReport().getDisabilitybefore(); }
	 * pt.put("disabilityBeforeJoining", disabilityBeforeJoining); String
	 * marritalStatus=""; Long maritalStatusId=null; try { if( v.getPatient()!=null
	 * && v.getPatient().getMasMaritalStatus()!=null &&
	 * v.getPatient().getMasMaritalStatus().getMaritalStatusId()!=0) {
	 * marritalStatus=v.getPatient().getMasMaritalStatus().getMaritalStatusName();
	 * maritalStatusId=v.getPatient().getMasMaritalStatus().getMaritalStatusId(); }
	 * pt.put("marritalStatus", marritalStatus); pt.put("maritalStatusId",
	 * maritalStatusId); }catch(Exception e) { pt.put("marritalStatus", "");
	 * e.printStackTrace(); } String address="";
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * StringUtils.isNotEmpty(v.getMasMedicalExamReport().getParmanentAddress())) {
	 * address=v.getMasMedicalExamReport().getParmanentAddress(); } else { if(
	 * v.getPatient()!=null && v.getPatient().getAddressLine1()!=null) {
	 * address=v.getPatient().getAddressLine1(); } }
	 * 
	 * pt.put("address",address);
	 * 
	 * String authority=""; if( v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getAuthority()!=null) {
	 * authority=v.getMasMedicalExamReport().getAuthority(); }
	 * pt.put("authority",authority);
	 * 
	 * 
	 * String particularsOfPreviousService=""; if( v.getMasMedicalExamReport()!=null
	 * && v.getMasMedicalExamReport().getParticularofpreviousservice()!=null) {
	 * particularsOfPreviousService=v.getMasMedicalExamReport().
	 * getParticularofpreviousservice(); }
	 * pt.put("particularsOfPreviousService",particularsOfPreviousService);
	 * 
	 * 
	 * String disabilityPensionRecieved=""; if( v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getReductionDisablePension()!=null) {
	 * disabilityPensionRecieved=v.getMasMedicalExamReport().
	 * getReductionDisablePension(); }
	 * pt.put("disabilityPensionRecieved",disabilityPensionRecieved);
	 * 
	 * String claimAnyDisability=""; if( v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getClamingdisability()!=null) {
	 * claimAnyDisability=v.getMasMedicalExamReport().getClamingdisability(); }
	 * pt.put("claimAnyDisability",claimAnyDisability);
	 * 
	 * 
	 * String anyOtherInformation=""; if( v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getAnyOtherInformationAboutYo()!=null) {
	 * anyOtherInformation=v.getMasMedicalExamReport().getAnyOtherInformationAboutYo
	 * (); } pt.put("anyOtherInformation",anyOtherInformation);
	 * 
	 * String dateOfWitness="";
	 * 
	 * if( v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPatientWitness()!=null) { Long witnessId=null;
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getWitnessSig()!=null) {
	 * witnessId=v.getMasMedicalExamReport().getWitnessSig(); }
	 * pt.put("witnessId",witnessId);
	 * pt.put("serviceNoEmployee",v.getMasMedicalExamReport().getPatientWitness().
	 * getServiceNo()); String rankOfEmployee="";
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPatientWitness()!=null &&
	 * v.getMasMedicalExamReport().getPatientWitness().getMasRank()!=null ) {
	 * //MasRank
	 * masRank=systemAdminDao.getRankByRankCode((v.getMasMedicalExamReport().
	 * getPatientWitness().getMasRank())); MasRank masRank=null;
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPatientWitness()!=null &&
	 * v.getMasMedicalExamReport().getPatientWitness().getMasRank()!=null) {
	 * masRank=v.getMasMedicalExamReport().getPatientWitness().getMasRank();//
	 * systemAdminDao.getRankByRankCode((visit.getMasMedicalExamReport().
	 * getPatientWitness().getMasRank())); } if(masRank!=null &&
	 * StringUtils.isNotEmpty(masRank.getRankName())) pt.put("rankOfEmployee",
	 * masRank.getRankName()); else pt.put("rankOfEmployee", "");
	 * 
	 * } String employeeName="";
	 * if(v.getMasMedicalExamReport().getPatientWitness().getEmployeeName()!=null) {
	 * employeeName=v.getMasMedicalExamReport().getPatientWitness().getEmployeeName(
	 * ); } pt.put("signatureOfWitness",employeeName); }
	 * 
	 * String signatureOfIndividual="";
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getUsers()!=null) {
	 * if(v.getMasMedicalExamReport().getUsers().getFirstName()!=null)
	 * signatureOfIndividual=v.getMasMedicalExamReport().getUsers().getFirstName();
	 * if(v.getMasMedicalExamReport().getUsers().getLastName()!=null)
	 * signatureOfIndividual+=""+v.getMasMedicalExamReport().getUsers().getLastName(
	 * ); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getIndividualPatient()!=null) {
	 * if(v.getMasMedicalExamReport().getIndividualPatient().getEmployeeName()!=
	 * null)
	 * signatureOfIndividual=v.getMasMedicalExamReport().getIndividualPatient().
	 * getEmployeeName();
	 * //if(visit.getMasMedicalExamReport()..getIndividualEmployees().
	 * getEmployeeName()!=null)
	 * //signatureOfIndividual+=""+visit.getMasMedicalExamReport().
	 * getIndividualEmployees().getEmployeeName(); }
	 * 
	 * pt.put("signatureOfIndividual",signatureOfIndividual); Long
	 * signatureOfIndividualId=null; if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getIndividualSig()!=null) {
	 * signatureOfIndividualId=v.getMasMedicalExamReport().getIndividualSig(); }
	 * pt.put("signatureOfIndividualId",signatureOfIndividualId);
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getWitnessDate()!=null) { dateOfWitness =
	 * HMSUtil.convertDateToStringFormat(
	 * v.getMasMedicalExamReport().getWitnessDate(), "dd/MM/yyyy"); }
	 * pt.put("dateOfWitness",dateOfWitness);
	 * 
	 * 
	 * String carefullyExaminedEmployeeName=""; if(users!=null) {
	 * if(users.getFirstName()!=null)
	 * carefullyExaminedEmployeeName=users.getFirstName();
	 * if(users.getLastName()!=null)
	 * carefullyExaminedEmployeeName+=" "+users.getLastName(); }
	 * pt.put("carefullyExaminedEmployeeName",carefullyExaminedEmployeeName);
	 * 
	 * String carefullyExaminedRank="";
	 * if(CollectionUtils.isNotEmpty(listMasEmployee)) {
	 * if(listMasEmployee.get(0).getMasRank()!=null ) { MasRank
	 * masRank=systemAdminDao.getRankByRankCode((listMasEmployee.get(0).getMasRank()
	 * )); if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
	 * carefullyExaminedRank=masRank.getRankName(); } }
	 * pt.put("carefullyExaminedRank",carefullyExaminedRank);
	 * 
	 * String carefullyUnitName=""; if(CollectionUtils.isNotEmpty(listMasEmployee))
	 * { if(listMasEmployee.get(0).getMasUnit()!=null ) {
	 * List<MasUnit>listMasUnit=medicalExamDAO.getMasUnitByUnitCode(listMasEmployee.
	 * get(0).getMasUnit()); if(CollectionUtils.isNotEmpty(listMasUnit))
	 * carefullyUnitName=listMasUnit.get(0).getUnitName(); } }
	 * pt.put("carefullyUnitName",carefullyUnitName);
	 * 
	 * String carefullyExaminedServiceNo=""; if(users!=null &&
	 * users.getServiceNo()!=null) {
	 * carefullyExaminedServiceNo=users.getServiceNo(); }
	 * pt.put("carefullyExaminedServiceNo",carefullyExaminedServiceNo);
	 * 
	 * String carefullyExaminedEmployeeName=""; if(users!=null) {
	 * if(users.getFirstName()!=null)
	 * carefullyExaminedEmployeeName=users.getFirstName();
	 * if(users.getLastName()!=null)
	 * carefullyExaminedEmployeeName+=" "+users.getLastName(); }
	 * pt.put("carefullyExaminedEmployeeName",carefullyExaminedEmployeeName);
	 * 
	 * String carefullyExaminedRank="";
	 * if(CollectionUtils.isNotEmpty(listMasEmployee)) {
	 * if(listMasEmployee.get(0).getMasRank()!=null ) { MasRank
	 * masRank=systemAdminDao.getRankByRankCode((listMasEmployee.get(0).getMasRank()
	 * )); if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
	 * carefullyExaminedRank=masRank.getRankName(); } }
	 * pt.put("carefullyExaminedRank",carefullyExaminedRank);
	 * 
	 * String carefullyUnitName=""; if(CollectionUtils.isNotEmpty(listMasEmployee))
	 * { if(listMasEmployee.get(0).getMasUnit()!=null ) {
	 * List<MasUnit>listMasUnit=medicalExamDAO.getMasUnitByUnitCode(listMasEmployee.
	 * get(0).getMasUnit()); if(CollectionUtils.isNotEmpty(listMasUnit))
	 * carefullyUnitName=listMasUnit.get(0).getUnitName(); } }
	 * pt.put("carefullyUnitName",carefullyUnitName);
	 * 
	 * 
	 * String hospitalName=""; if(v.getMasHospital()!=null &&
	 * StringUtils.isNotEmpty(v.getMasHospital().getHospitalName())) { hospitalName=
	 * v.getMasHospital().getHospitalName(); } else { hospitalName=""; }
	 * 
	 * pt.put("hospitalName",hospitalName);
	 * 
	 * String typeOfCommission=""; if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getTypeofcommision()!=null) { typeOfCommission=
	 * v.getMasMedicalExamReport().getTypeofcommision(); } else {
	 * typeOfCommission=""; }
	 * 
	 * 
	 * pt.put("typeOfCommission",typeOfCommission);
	 * 
	 * 
	 * String serviceJoinDate=""; if(v.getPatient()!=null &&
	 * v.getPatient().getServiceJoinDate()!=null) { serviceJoinDate =
	 * HMSUtil.convertDateToStringFormat( v.getPatient().getServiceJoinDate(),
	 * "dd/MM/yyyy");
	 * 
	 * } else { serviceJoinDate=""; }
	 * 
	 * 
	 * pt.put("serviceJoinDate",serviceJoinDate);
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getDateOfCompletion()!=null ) { String doeORDocD
	 * = HMSUtil.convertDateToStringFormat(v.getMasMedicalExamReport().
	 * getDateOfCompletion(), "dd/MM/yyyy");
	 * 
	 * //String dateOfBirth =
	 * HMSUtil.getDateWithoutTime1(v.getPatient().getDateOfBirth());
	 * pt.put("serviceJoinDate", doeORDocD); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getRankId()!=null) { pt.put("rankIdForDigi",
	 * v.getMasMedicalExamReport().getRankId()); } else { pt.put("rankIdForDigi",
	 * ""); }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getBranchId()!=null) { pt.put("branchTradIdDigi",
	 * v.getMasMedicalExamReport().getBranchId()); } else {
	 * pt.put("branchTradIdDigi", ""); }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getUnitId()!=null) { pt.put("unitIdDigi",
	 * v.getMasMedicalExamReport().getUnitId()); } else { pt.put("unitIdDigi", "");
	 * }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getApproveName()!=null) { pt.put("approvingName",
	 * v.getMasMedicalExamReport().getApproveName()); } else {
	 * pt.put("approvingName", ""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getApproveRank()!=null) { pt.put("approvingRank",
	 * v.getMasMedicalExamReport().getApproveRank()); } else {
	 * pt.put("approvingRank", ""); }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPursueApproveName()!=null) {
	 * pt.put("persuingName", v.getMasMedicalExamReport().getPursueApproveName()); }
	 * else { pt.put("persuingName", ""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPursueApproveRank()!=null) {
	 * pt.put("persuingRank", v.getMasMedicalExamReport().getPursueApproveRank()); }
	 * else { pt.put("persuingRank", ""); }
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPlaceOfExam()!= null) { pt.put("placeOfExam",
	 * v.getMasMedicalExamReport().getPlaceOfExam()); } else { pt.put("placeOfExam",
	 * ""); } if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getHospitalId()!= null) {
	 * pt.put("hospitalIdForDigi", v.getMasMedicalExamReport().getHospitalId()); }
	 * else { pt.put("hospitalIdForDigi", ""); } if(v.getPatient()!=null &&
	 * v.getPatient().getFitFlag() !=null ) {
	 * pt.put("fitFlag",v.getPatient().getFitFlag()); } else { pt.put("fitFlag","");
	 * }
	 * 
	 * 
	 * /////////////////////////////////////////////////As Per
	 * change/////////////////////////////////////////////////////
	 * if(jsondata.containsKey("newEntryStatus") &&
	 * jsondata.get("newEntryStatus")!=null ) { List<PatientMedicalCat>
	 * listPatientMedicalCat=null; listPatientMedicalCat=
	 * medicalExamDAO.getPatientMedicalCat( patientId,
	 * Long.parseLong(jsondata.get("visitId").toString()),"composit") ;
	 * PatientMedicalCat patientMedCate=null; if
	 * (CollectionUtils.isNotEmpty(listPatientMedicalCat)) { patientMedCate=
	 * listPatientMedicalCat.get(0);
	 * if(patientMedCate.getMasMedicalCategoryFit()!=null &&
	 * StringUtils.isNotEmpty(patientMedCate.getMasMedicalCategoryFit().
	 * getMedicalCategoryName())) { pt.put("medicalCategory",
	 * patientMedCate.getMasMedicalCategoryFit().getMedicalCategoryName());
	 * pt.put("medicalCompositeNameValue", patientMedCate.getpMedCatId());
	 * 
	 * } else { pt.put("medicalCategory", ""); pt.put("medicalCompositeNameValue",
	 * ""); }
	 * 
	 * String medicalCategoryDate = ""; if (patientMedCate.getpMedCatDate() != null)
	 * { medicalCategoryDate = HMSUtil.convertDateToStringFormat(
	 * patientMedCate.getpMedCatDate(), "dd/MM/yyyy"); pt.put("medicalCategoryDate",
	 * medicalCategoryDate); } else { pt.put("medicalCategoryDate", ""); }
	 * 
	 * } else { pt.put("medicalCategory", ""); pt.put("medicalCompositeNameValue",
	 * ""); pt.put("medicalCategoryDate", ""); }
	 * 
	 * /////////////////////////////////////////////////////////////////////////////
	 * ////////////////////////////////////////////
	 * listPatientMedicalCat=medicalExamDAO.
	 * getPatientMedicalCat(patientId,Long.parseLong(jsondata.get("visitId").
	 * toString()),"Fit") ;
	 * 
	 * if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { patientMedCate=
	 * listPatientMedicalCat.get(0);
	 * 
	 * if(patientMedCate!=null && patientMedCate.getpMedFitFlag() !=null ) {
	 * pt.put("fitFlag",patientMedCate.getpMedFitFlag()); } else {
	 * pt.put("fitFlag",""); } } else { pt.put("fitFlag",""); }
	 * 
	 * 
	 * }
	 * 
	 * if (v.getPatientMedBoard()!=null && v.getPatientMedBoard().getHospitalId()!=
	 * null) { pt.put("hospitalIdForMb", v.getPatientMedBoard().getHospitalId()); }
	 * else { pt.put("hospitalIdForMb", ""); }
	 * 
	 * ///////////////////////////////////////////////////////////////////////////
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getAaPlace()!=null) { pt.put("apporvingPlace",
	 * v.getMasMedicalExamReport().getAaPlace()); } else { pt.put("apporvingPlace",
	 * ""); }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getAaDate()!=null) {
	 * 
	 * String apporvingDate =
	 * HMSUtil.convertDateToStringFormat(v.getMasMedicalExamReport().getAaDate(),
	 * "dd/MM/yyyy"); pt.put("apporvingDate", apporvingDate); } else {
	 * pt.put("apporvingDate", ""); }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPaPlace()!=null) { pt.put("persuingPlace",
	 * v.getMasMedicalExamReport().getPaPlace()); } else { pt.put("persuingPlace",
	 * ""); }
	 * 
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getPaDate()!=null) { String persuingDate =
	 * HMSUtil.convertDateToStringFormat(v.getMasMedicalExamReport().getPaDate(),
	 * "dd/MM/yyyy"); pt.put("persuingDate", persuingDate); } else {
	 * pt.put("persuingDate", ""); }
	 * 
	 * if (v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getMaritalStatus() != null) {
	 * pt.put("maritalStatusId",v.getMasMedicalExamReport().getMaritalStatus()); }
	 * else { if(v.getPatient()!=null && v.getPatient().getMasMaritalStatus()!=null)
	 * { pt.put("marritalStatus",
	 * v.getPatient().getMasMaritalStatus().getMaritalStatusName());
	 * pt.put("maritalStatusId", v.getPatient().getMaritalStatusId()); } else {
	 * pt.put("marritalStatus",""); pt.put("maritalStatusId",""); } }
	 * 
	 * //////////////////////////////////////////////////////////////////
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getSignatureMedicalSpecialist() !=null ) {
	 * pt.put("signatureOfMedicalSpecialist",v.getMasMedicalExamReport().
	 * getSignatureMedicalSpecialist()); } else {
	 * pt.put("signatureOfMedicalSpecialist",""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getExternalEarR() !=null ) {
	 * pt.put("externalEarR",v.getMasMedicalExamReport().getExternalEarR()); } else
	 * { pt.put("externalEarR",""); } if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getExternalEarL() !=null ) {
	 * pt.put("externalEarL",v.getMasMedicalExamReport().getExternalEarL()); } else
	 * { pt.put("externalEarL",""); } if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getMiddleEar() !=null ) {
	 * pt.put("middleEarR",v.getMasMedicalExamReport().getMiddleEar()); } else {
	 * pt.put("middleEarR",""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getMiddleEarR() !=null ) {
	 * pt.put("middleEarL",v.getMasMedicalExamReport().getMiddleEarR()); } else {
	 * pt.put("middleEarL",""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getInnerEarR() !=null ) {
	 * pt.put("innerEarR",v.getMasMedicalExamReport().getInnerEarR()); } else {
	 * pt.put("innerEarR",""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getInnerEarL() !=null ) {
	 * pt.put("innerEarL",v.getMasMedicalExamReport().getInnerEarL()); } else {
	 * pt.put("innerEarL",""); } if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getSignatureOfENTSpecialist() !=null ) {
	 * pt.put("signatureOfENTSpecialist",v.getMasMedicalExamReport().
	 * getSignatureOfENTSpecialist()); } else {
	 * pt.put("signatureOfENTSpecialist",""); }
	 * 
	 * 
	 * if(v.getMasMedicalExamReport()!=null && v.getMasMedicalExamReport().getNose()
	 * !=null ) { pt.put("noseSinuses",v.getMasMedicalExamReport().getNose()); }
	 * else { pt.put("noseSinuses",""); } if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getNosethroat() !=null ) {
	 * pt.put("throatSinuses",v.getMasMedicalExamReport().getNosethroat()); } else {
	 * pt.put("throatSinuses",""); }
	 * 
	 * if(v.getMasMedicalExamReport()!=null &&
	 * v.getMasMedicalExamReport().getSignatureOfOfficer() !=null ) {
	 * pt.put("signatureOfOfficer",v.getMasMedicalExamReport().getSignatureOfOfficer
	 * ()); } else { pt.put("signatureOfOfficer",""); }
	 * 
	 * 
	 * /////////////////////////////////////////////////////////////////////////////
	 * /
	 * 
	 * 
	 * 
	 * 
	 * if(getvitalDetails!=null && ! CollectionUtils.isEmpty(getvitalDetails)) {
	 * OpdObesityHd opdObesityHd =
	 * opdPatientDetailDao.getOpdObesityHd(Long.parseLong(jsondata.get("visitId").
	 * toString())); if(opdObesityHd!=null) { pt.put("overweightFlag",
	 * opdObesityHd.getOverweightFlag()); } else { pt.put("overweightFlag", ""); }
	 * pt.put("height",v.getOpdPatientDetails().getHeight());
	 * pt.put("idealWeight",v.getOpdPatientDetails().getIdealWeight());
	 * pt.put("weight",v.getOpdPatientDetails().getWeight()); String variation = "";
	 * if(v.getOpdPatientDetails()!=null &&
	 * v.getOpdPatientDetails().getVaration()!=null) { if (
	 * v.getOpdPatientDetails().getVaration().compareTo(Double.MAX_VALUE) > 0) {
	 * variation = "+"+v.getOpdPatientDetails().getVaration(); }else { variation =
	 * v.getOpdPatientDetails().getVaration()+""; } } pt.put("varation", variation);
	 * pt.put("tempature",v.getOpdPatientDetails().getTemperature());
	 * pt.put("bp",v.getOpdPatientDetails().getBpSystolic());
	 * pt.put("bp1",v.getOpdPatientDetails().getBpDiastolic());
	 * pt.put("pulse",v.getOpdPatientDetails().getPulse()); pt.put("spo2",
	 * v.getOpdPatientDetails().getSpo2());
	 * pt.put("bmi",v.getOpdPatientDetails().getBmi());
	 * pt.put("rr",v.getOpdPatientDetails().getRr());
	 * pt.put("pollar",v.getOpdPatientDetails().getPollor());
	 * pt.put("edema",v.getOpdPatientDetails().getEdema());
	 * pt.put("cyanosis",v.getOpdPatientDetails().getCyanosis());
	 * pt.put("hairnail",v.getOpdPatientDetails().getHairNail());
	 * pt.put("icterus",v.getOpdPatientDetails().getIcterus());
	 * pt.put("lymphNode",v.getOpdPatientDetails().getLymphNode());
	 * pt.put("clubbing",v.getOpdPatientDetails().getClubbing());
	 * pt.put("gcs",v.getOpdPatientDetails().getGcs());
	 * pt.put("Tremors",v.getOpdPatientDetails().getTremors());
	 * pt.put("others",v.getOpdPatientDetails().getGeneralOther());
	 * pt.put("cns",v.getOpdPatientDetails().getCns());
	 * pt.put("chestResp",v.getOpdPatientDetails().getChestResp());
	 * pt.put("musculoskeletal",v.getOpdPatientDetails().getMusculoskeletal());
	 * pt.put("cvs",v.getOpdPatientDetails().getCvs());
	 * pt.put("skin",v.getOpdPatientDetails().getSkin());
	 * pt.put("gi",v.getOpdPatientDetails().getGi());
	 * pt.put("geneticurinary",v.getOpdPatientDetails().getGenitoUrinary());
	 * pt.put("systemOthers",v.getOpdPatientDetails().getSystemOther());
	 * if(v.getOpdPatientHistory()!=null &&
	 * v.getOpdPatientHistory().getChiefComplain()!=null) {
	 * pt.put("cheifComplaint",v.getOpdPatientHistory().getChiefComplain()); }
	 * if(v.getMasMedicalExamReport()!=null
	 * &&v.getMasMedicalExamReport().getFinalobservation()!=null) {
	 * pt.put("finalObservation",v.getMasMedicalExamReport().getFinalobservation());
	 * }
	 * 
	 * } if(getObsistyDetails!=null && ! CollectionUtils.isEmpty(getObsistyDetails))
	 * { for (OpdObesityHd oohd : getObsistyDetails) {
	 * if(oohd.getOverweightFlag()!=null) {
	 * pt.put("obesityOverWeightFlag",oohd.getOverweightFlag()); } else {
	 * pt.put("obesityOverWeightFlag",""); } if(oohd.getCloseDate()!=null) {
	 * pt.put("obsistyCloseDate",oohd.getCloseDate()); } else {
	 * pt.put("obsistyCloseDate",""); } } }
	 * 
	 * 
	 * 
	 * 
	 * c.add(pt); json.put("status", visitstatus); json.put("data", c);
	 * json.put("size", c.size()); json.put("listMasServiceType",
	 * listMasServiceType); // json.put("Visit List", getvisit); json.put("msg",
	 * "OPD Patients Visit List  get  sucessfull... "); json.put("status", "1");
	 * 
	 * } }
	 * 
	 * catch(Exception e) { e.printStackTrace(); return
	 * "{\"status\":\"0\",\"msg\":\"Somting went wrong}"; } } else { try {
	 * json.put("msg", "Visit ID data not found"); json.put("status", 0); } catch
	 * (JSONException e) { e.printStackTrace(); } }
	 * 
	 * 
	 * } return json.toString(); }
	 * 
	 * @Override public String getMedicalBoardAutocomplete(HashMap<String, String>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); try { if (jsondata.get("employeeId") ==
	 * null || jsondata.get("employeeId").toString().trim().equals("")) { return
	 * "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}"
	 * ; } else {
	 * 
	 * 
	 * List<MasMedicalCategory> mas_medicalCategory =null;
	 * 
	 * if (jsondata.containsKey("icdName")) {
	 * mas_medicalCategory=mbDao.getMasMedicalCategoryName(jsondata.get("icdName").
	 * toString().trim()); } else { mas_medicalCategory =
	 * mbDao.getMasMedicalCategory(); } if (mas_medicalCategory.size() == 0) {
	 * return "{\"status\":\"0\",\"msg\":\"Data not found\"}"; } else {
	 * json.put("masMedicalCategoryList", mas_medicalCategory); json.put("msg",
	 * "masMedicalCategoryList  get  sucessfull... "); json.put("status", "1");
	 * 
	 * }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String getPatientDetailToValidate(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * List<Object> responseList = new ArrayList<Object>(); JSONObject json = new
	 * JSONObject(); List<HashMap<String, Object>> c = new ArrayList<HashMap<String,
	 * Object>>(); List<DgMasInvestigation>listDgMasInvestigation=null; try {
	 * Map<String, Object>mapObject =mbDao.getValidateMedicalExamDetails(jsondata);
	 * 
	 * List<Visit>listVisit=(List<Visit>) mapObject.get("listVisit");
	 * if(jsondata.get("flagForForm")!=null &&
	 * jsondata.get("flagForForm").toString().equalsIgnoreCase("f1")) {
	 * listDgMasInvestigation=(List<DgMasInvestigation>)
	 * mapObject.get("listDgMasInvestigation"); } List<OpdPatientDetail>
	 * getvitalDetails
	 * =opdDao.getVitalRecord(Long.parseLong(jsondata.get("visitId").toString()));
	 * 
	 * if (CollectionUtils.isNotEmpty(listVisit)) { for (Visit visit : listVisit) {
	 * LocalDate today = LocalDate.now(); HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("patientName",
	 * visit.getPatient().getPatientName());
	 * 
	 * if(visit.getPatient()!=null && visit.getPatient().getDateOfBirth()!=null) {
	 * Date s=visit.getPatient().getDateOfBirth(); Calendar lCal =
	 * Calendar.getInstance(); lCal.setTime(s); int yr=lCal.get(Calendar.YEAR); int
	 * mn=lCal.get(Calendar.MONTH) + 1; int dt=lCal.get(Calendar.DATE);
	 * 
	 * LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
	 * System.out.println("birthday"+birthday); Period p = Period.between(birthday,
	 * today); String ageFull = HMSUtil.calculateAge(s); jsonMap.put("age",
	 * ageFull); jsonMap.put("ageValue", p.getYears()); } else { jsonMap.put("age",
	 * ""); }
	 * 
	 * 
	 * jsonMap.put("gender",
	 * visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
	 * //jsonMap.put("gender","test"); if(visit.getPatient() != null &&
	 * visit.getPatient().getMasRank() != null) { jsonMap.put("rankName",
	 * visit.getPatient().getMasRank().getRankName()); } else {
	 * jsonMap.put("rankName", ""); } //jsonMap.put("rankName", "rrrrrrr");
	 * if(visit.getMasMedExam()!=null
	 * &&visit.getMasMedExam().getMedicalExamName()!=null) {
	 * jsonMap.put("meTypeName",visit.getMasMedExam().getMedicalExamName()); }
	 * if(visit.getMasMedExam()!=null
	 * &&visit.getMasMedExam().getMedicalExamCode()!=null) {
	 * jsonMap.put("meTypeCode",visit.getMasMedExam().getMedicalExamCode()); }
	 * String visitstatus=""; if(StringUtils.isNotEmpty(visit.getExamStatus())) {
	 * if(visit.getExamStatus().equalsIgnoreCase("I")) { visitstatus="Initiate"; } }
	 * 
	 * jsonMap.put("status",visitstatus); jsonMap.put("visitId",visit.getVisitId());
	 * jsonMap.put("patientId",visit.getPatientId());
	 * jsonMap.put("visitStatus",visit.getVisitStatus());
	 * jsonMap.put("serviceNo",visit.getPatient().getServiceNo());
	 * jsonMap.put("departmentId",visit.getDepartmentId());
	 * jsonMap.put("examTypeId",visit.getExamId());
	 * jsonMap.put("appointmentId",visit.getAppointmentTypeId());
	 * 
	 * 
	 * if(visit.getPatient()!=null && visit.getPatient().getDateOfBirth()!=null ) {
	 * String dateOfBirth =
	 * HMSUtil.convertDateToStringFormat(visit.getPatient().getDateOfBirth(),
	 * "dd/MM/yyyy");
	 * 
	 * jsonMap.put("dateOfBirth", dateOfBirth); }
	 * 
	 * 
	 * if(visit.getPatient()!=null && visit.getPatient().getServiceJoinDate()!=null)
	 * {
	 * 
	 * Date s1=visit.getPatient().getServiceJoinDate(); Period
	 * serviceDate=ProjectUtils.getDOB(s1);
	 * jsonMap.put("totalService",serviceDate.getYears()); }
	 * if(visit.getPatient()!=null && visit.getPatient().getUnitId()!=null) {
	 * jsonMap.put("unit", visit.getPatient().getMasUnit().getUnitName()); }
	 * //jsonMap.put("unit", "tessttttt"); if(visit.getPatient()!=null &&
	 * visit.getPatient().getMedicalCategoryId()!=null) {
	 * jsonMap.put("medicalCategogyName",visit.getPatient().getMasMedicalCategory().
	 * getMedicalCategoryName()); } if(visit.getPatient()!=null &&
	 * visit.getPatient().getMedCatDate()!=null) {
	 * jsonMap.put("medicalCompositeDate",visit.getPatient().getMedCatDate()); }
	 * if(visit.getPatient()!=null && visit.getPatient().getTradeId()!=null) {
	 * jsonMap.put("tradeBranch", visit.getPatient().getMasTrade().getTradeName());
	 * }
	 * 
	 * if(visit.getMasMedicalExamReport()!=null && visit.
	 * getMasMedicalExamReport().getMediceExamDate()!=null ) { String dateOfExam =
	 * HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().
	 * getMediceExamDate(), "dd/MM/yyyy");
	 * 
	 * jsonMap.put("dateOfExam", dateOfExam); }
	 * //jsonMap.put("medicalCategory","medicalCategory");
	 * jsonMap.put("tradeBranch","tradeBranch");
	 * jsonMap.put("dateOfExam","dateOfExam"); if(visit.getOpdPatientDetails()!=null
	 * && visit.getOpdPatientDetails().getOpdPatientDetailsId()!=null) {
	 * jsonMap.put("opdPatientDetailId",visit.getOpdPatientDetails().
	 * getOpdPatientDetailsId()); } else { jsonMap.put("opdPatientDetailId",""); }
	 * responseList.add(jsonMap); } if (responseList != null && responseList.size()
	 * > 0) { json.put("listVisit", responseList);
	 * json.put("listDgMasInvestigation", listDgMasInvestigation);
	 * json.put("status", 1);
	 * 
	 * } else { json.put("listVisit", responseList); json.put("msg",
	 * "Data not found"); json.put("status", 0); } } else { try { json.put("msg",
	 * "Visit ID data not found"); json.put("status", 0); } catch (JSONException e)
	 * { e.printStackTrace(); } }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String getPatientReferalDetail(HashMap<String, String>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); List<HashMap<String, Object>> c = new
	 * ArrayList<HashMap<String, Object>>(); List<MasEmpanelledHospital>
	 * masEmpanelledHospitalList = null; List<MasHospital> masMasHospitalList =
	 * null; //List<MasIcd>listMasIcd=null; Long visitId=null; Long patientId=null;
	 * Long opdPatientDetailId=null; if (!jsondata.isEmpty()) { List<Object[]>
	 * listReferralPatientDt = null; if (jsondata.get("opdPatientDetailId") != null)
	 * { listReferralPatientDt =
	 * dgOrderhdDao.getReferralPatientDtList(Long.parseLong(jsondata.get(
	 * "opdPatientDetailId").toString()));
	 * 
	 * 
	 * //if(jsondata.get("flagForRefer")!=null &&
	 * jsondata.get("flagForRefer").equalsIgnoreCase("In")) { masMasHospitalList=
	 * mbDao.getHospitalList(); } else { masEmpanelledHospitalList =
	 * mbDao.getEmpanelledHospital(jsondata); //} try {
	 * if(jsondata.get("visitId")!=null) {
	 * visitId=Long.parseLong(jsondata.get("visitId").toString()); }
	 * if(jsondata.get("opdPatientDetailId")!=null) {
	 * opdPatientDetailId=Long.parseLong(jsondata.get("opdPatientDetailId").toString
	 * ()); }
	 * 
	 * if(jsondata.get("patientId")!=null) {
	 * patientId=Long.parseLong(jsondata.get("patientId").toString()); }
	 * //listMasIcd=
	 * dgOrderhdDao.getMasIcdByVisitPatAndOpdPD(visitId,patientId,opdPatientDetailId
	 * ); } catch(Exception e) { e.printStackTrace(); }
	 * 
	 * } if (listReferralPatientDt != null) { try { Long masEmpanalId = null; String
	 * masEmpanalName = ""; Long masDepatId = null; String massDeptName = ""; Long
	 * diagonisId = null; String daiganosisName = ""; String instruction = ""; Long
	 * referalPatientDt = null; Long referalPatientHd = null; String
	 * exDepartmentValue = ""; String masCode=""; String referalNotes=""; String
	 * speicalistNotes=""; String referalDate=""; Long intDepartmentId=null; Long
	 * intHospitalId=null; Long ridcId=null; for (Iterator<?> it =
	 * listReferralPatientDt.iterator(); it.hasNext();) { Object[] row = (Object[])
	 * it.next();
	 * 
	 * HashMap<String, Object> pt = new HashMap<String, Object>();
	 * 
	 * if (row[0] != null) { masEmpanalId = Long.parseLong(row[0].toString()); } if
	 * (row[1] != null) { masEmpanalName = row[1].toString(); } if (row[2] != null)
	 * { masDepatId = Long.parseLong(row[2].toString()); } if (row[3] != null) {
	 * massDeptName = row[3].toString(); }
	 * 
	 * 
	 * if (row[4] != null) { diagonisId = Long.parseLong(row[4].toString()); }
	 * 
	 * if (row[5] != null) { daiganosisName = row[5].toString(); } if (row[6] !=
	 * null) { instruction = row[6].toString(); } if (row[7] != null) {
	 * referalPatientDt = Long.parseLong(row[7].toString()); } if (row[8] != null) {
	 * referalPatientHd = Long.parseLong(row[8].toString()); }
	 * 
	 * if (row[9] != null) { exDepartmentValue = row[9].toString(); } if (row[10] !=
	 * null) { masCode = row[10].toString(); } if (row[11] != null) { referalNotes =
	 * row[11].toString(); } if (row[12] != null) { speicalistNotes =
	 * row[12].toString(); } if (row[13] != null) { referalDate =
	 * row[13].toString(); Date dd1 = HMSUtil.dateFormatteryyyymmdd(referalDate);
	 * referalDate = HMSUtil.getDateWithoutTime(dd1); }
	 * 
	 * if (row[14] != null) { intDepartmentId = Long.parseLong(row[14].toString());
	 * } if (row[15] != null) { intHospitalId = Long.parseLong(row[15].toString());
	 * } if (row[16] != null) { ridcId = Long.parseLong(row[16].toString()); }
	 * 
	 * pt.put("masEmpanalId", masEmpanalId); pt.put("masEmpanalName",
	 * masEmpanalName); pt.put("masDepatId", masDepatId); pt.put("massDeptName",
	 * massDeptName); pt.put("diagonisId", diagonisId); pt.put("daiganosisName",
	 * daiganosisName); pt.put("instruction", instruction);
	 * 
	 * pt.put("referalPatientDt", referalPatientDt); pt.put("referalPatientHd",
	 * referalPatientHd); pt.put("exDepartmentValue", exDepartmentValue);
	 * pt.put("masCode", masCode); pt.put("referalNotes", referalNotes);
	 * pt.put("referalNotes", referalNotes); pt.put("speicalistNotes",
	 * speicalistNotes); pt.put("intDepartmentId", intDepartmentId);
	 * pt.put("intHospitalId", intHospitalId); pt.put("ridcId",ridcId); c.add(pt); }
	 * json.put("listReferralPatientDt", c);
	 * 
	 * // List<MasDepartment> departmentList = mbDao.getDepartmentsList();
	 * 
	 * // if(jsondata.get("flagForRefer")!=null &&
	 * jsondata.get("flagForRefer").equalsIgnoreCase("In")) { //
	 * json.put("masMasHospitalList", masMasHospitalList); } else {
	 * json.put("masEmpanelledHospitalList", masEmpanelledHospitalList); //} //
	 * json.put("departmentList", departmentList);
	 * 
	 * // json.put("listMasIcd", listMasIcd); json.put("msg",
	 * "Refferal Patient List  get  sucessfull... "); json.put("status", "1");
	 * 
	 * }
	 * 
	 * catch (Exception e) { e.printStackTrace(); return
	 * "{\"status\":\"0\",\"msg\":\"Somting went wrong}"; } } else { try {
	 * json.put("msg", "Visit ID data not found"); json.put("status", 0); } catch
	 * (JSONException e) { e.printStackTrace(); } } } return json.toString();
	 * 
	 * }
	 * 
	 * @Override public String getInvestigationAndResult(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); List<Object[]> listObject = null;
	 * List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
	 * try { String vStatus="Saved"; if(jsondata.get("vstatus")!=null &&
	 * jsondata.get("vstatus").equals(vStatus)) { if (jsondata.get("visitId") !=
	 * null) { listObject =
	 * mbDao.getDgMasInvestigationsAndResult(Long.parseLong(jsondata.get("visitId").
	 * toString())); } } else { if (jsondata.get("patientId") != null) { Long
	 * visitId=mbDao.getInvestigationsVisitId(Long.parseLong(jsondata.get(
	 * "patientId").toString())); listObject =
	 * mbDao.getDgMasInvestigationsAndResult(visitId); } }
	 * if(jsondata.containsKey("mbFlag")) { Long
	 * visitId=mbDao.getInvestigationsVisitId(); listObject =
	 * mbDao.getDgMasInvestigationsAndResult(visitId); } else { if
	 * (jsondata.get("visitId") != null) { listObject =
	 * mbDao.getDgMasInvestigationsAndResult(Long.parseLong(jsondata.get("visitId").
	 * toString())); } } Long investigationId = null; String investigationName = "";
	 * String orderHdId = null; String dgOrderDtId = null; String urgent = "";
	 * String labMark = ""; String orderDate = ""; String visitId = null; String
	 * otherInvestigation = ""; String departId = null; String hospitalId = null;
	 * String uomName=""; String investagationRemarks=""; if (listObject != null) {
	 * 
	 * for (Iterator<?> it = listObject.iterator(); it.hasNext();) { Object[] row =
	 * (Object[]) it.next();
	 * 
	 * 
	 * for(Object dgMasInvestigation : listObject) { Object row=dgMasInvestigation;
	 * 
	 * HashMap<String, Object> pt = new HashMap<String, Object>();
	 * 
	 * if (row[0] != null) { investigationId = Long.parseLong(row[0].toString()); }
	 * if (row[1] != null) { investigationName = row[1].toString(); }
	 * 
	 * if (row[2] != null) { orderHdId = row[2].toString(); }
	 * 
	 * if (row[3] != null) { labMark = row[3].toString(); } if (row[4] != null) {
	 * urgent = row[4].toString(); }
	 * 
	 * if (row[5] != null) { orderDate = row[5].toString(); Date dd1 =
	 * HMSUtil.dateFormatteryyyymmdd(orderDate); // Date dd1
	 * =HMSUtil.convertStringDateToUtilDateForDatabase(orderDate); //orderDate =
	 * HMSUtil.getDateWithoutTime(dd1); orderDate =
	 * HMSUtil.convertDateToStringFormat(dd1, "dd/MM/yyyy");
	 * 
	 * } else { orderDate = ""; } if (row[6] != null) { visitId = row[6].toString();
	 * } if (row[7] != null) { otherInvestigation = row[7].toString(); } if (row[8]
	 * != null) { departId = row[8].toString(); } if (row[9] != null) { hospitalId =
	 * row[9].toString(); } if (row[10] != null) { dgOrderDtId = row[10].toString();
	 * }
	 * 
	 * if (row[11] != null) { uomName = row[11].toString(); } else { uomName = ""; }
	 * if (row[12] != null) { investagationRemarks = row[12].toString(); } else {
	 * investagationRemarks = ""; }
	 * 
	 * pt.put("investigationName", investigationName); pt.put("investigationId",
	 * investigationId); pt.put("labMark", labMark); pt.put("urgent", urgent);
	 * pt.put("orderDate", orderDate); pt.put("visitId", visitId);
	 * pt.put("otherInvestigation", otherInvestigation); pt.put("departId",
	 * departId); pt.put("hospitalId", hospitalId); pt.put("orderHdId", orderHdId);
	 * pt.put("dgOrderDtId", dgOrderDtId); pt.put("uomName", uomName);
	 * pt.put("investagationRemarks",investagationRemarks); c.add(pt); } }
	 * json.put("listObject", c); json.put("msg",
	 * "OPD Patients Visit List  get  sucessfull... "); json.put("status", "1");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String saveAmsfForm16Details(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { String
	 * opd = null; // TODO Auto-generated method stub Date
	 * currentDate=ProjectUtils.getCurrentDate();
	 * 
	 * JSONObject json = new JSONObject();
	 * 
	 * //OpdPatientDetail opddetails = new OpdPatientDetail();
	 * 
	 * Calendar calendar = Calendar.getInstance(); java.sql.Timestamp
	 * ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime());
	 * 
	 * 
	 * //OpdPatientDetail checkOpdPatientVisit =
	 * od.checkVisitOpdPatientDetails(Long.parseLong(jsondata.get("visitId").
	 * toString())); //OpdPatientHistory checkOpdPatinetHistoryVisit =
	 * od.checkVisitOpdPatientHistory(Long.parseLong(jsondata.get("visitId").
	 * toString())); try { if (!jsondata.isEmpty()) { JSONObject
	 * nullbalankvalidation = null; nullbalankvalidation =
	 * ValidateUtils.addVitalPreConsulataionDetailsvalidation(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else { opd =
	 * mbDao.saveAmsfForm16Details(jsondata);
	 * 
	 * }
	 * 
	 * //if (opd != null ||oph != null &&
	 * opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) { if (opd != null
	 * && opd.equalsIgnoreCase("Successfully saved")) { json.put("msg",
	 * "Opd Patinet Details Saved successfully "); json.put("status", "1"); } else
	 * if (opd != null && opd.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", opd); json.put("status", "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String getMOValidateWaitingList(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * List<Object> responseList = new ArrayList<Object>(); JSONObject json = new
	 * JSONObject(); List<HashMap<String, Object>> c = new ArrayList<HashMap<String,
	 * Object>>(); List<Visit>listVisit=null; Map<String, Object>map=null; if
	 * (!jsondata.isEmpty()) {
	 * map=mbDao.getMOValidateMedicalBoardWaitingList(jsondata); int count=(int)
	 * map.get("count"); listVisit=(List<Visit>) map.get("listVisit");
	 * 
	 * if (CollectionUtils.isNotEmpty(listVisit)) { for (Visit visit : listVisit) {
	 * LocalDate today = LocalDate.now(); HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>(); jsonMap.put("patientName",
	 * visit.getPatient().getPatientName());
	 * 
	 * if(visit.getPatient()!=null && visit.getPatient().getDateOfBirth()!=null) {
	 * Date s=visit.getPatient().getDateOfBirth(); Calendar lCal =
	 * Calendar.getInstance(); lCal.setTime(s); int yr=lCal.get(Calendar.YEAR); int
	 * mn=lCal.get(Calendar.MONTH) + 1; int dt=lCal.get(Calendar.DATE);
	 * 
	 * LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
	 * System.out.println("birthday"+birthday); Period p = Period.between(birthday,
	 * today); String ageFull = HMSUtil.calculateAge(s); jsonMap.put("age",
	 * ageFull); jsonMap.put("ageValue", p.getYears()); } else { jsonMap.put("age",
	 * ""); }
	 * 
	 * 
	 * jsonMap.put("gender",
	 * visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
	 * if(visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
	 * jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName()); }
	 * else { jsonMap.put("rankName", ""); }
	 * jsonMap.put("meTypeName",visit.getMasMedExam().getMedicalExamName());
	 * jsonMap.put("meTypeCode",visit.getMasMedExam().getMedicalExamCode());
	 * if(visit.getMasMedicalExamReport()!=null &&
	 * visit.getMasMedicalExamReport().getApprovedBy()!=null) {
	 * jsonMap.put("approvedBy",visit.getMasMedicalExamReport().getApprovedBy()); }
	 * String visitstatus=""; if(StringUtils.isNotEmpty(visit.getExamStatus())) {
	 * if(visit.getExamStatus().equalsIgnoreCase("V")||visit.getVisitStatus().
	 * equalsIgnoreCase("S")) { visitstatus="Saved by Transcription"; } }
	 * 
	 * jsonMap.put("status",visitstatus); jsonMap.put("visitId",visit.getVisitId());
	 * jsonMap.put("patientId",visit.getPatientId());
	 * 
	 * jsonMap.put("serviceNo",visit.getPatient().getServiceNo());
	 * jsonMap.put("departmentId",visit.getDepartmentId());
	 * jsonMap.put("visitStatus",visit.getVisitStatus());
	 * jsonMap.put("visitFlag",visit.getVisitFlag()); if(visit.getTokenNo()!=null) {
	 * jsonMap.put("tokenNo",visit.getTokenNo()); } else {
	 * jsonMap.put("tokenNo",""); } responseList.add(jsonMap); } if (responseList !=
	 * null && responseList.size() > 0) { json.put("data", responseList);
	 * json.put("status", 1); json.put("count", count);
	 * 
	 * } else { json.put("data", responseList); json.put("msg", "Data not found");
	 * json.put("status", 0); } } else { try { json.put("msg",
	 * "Visit ID data not found"); json.put("status", 0); } catch (JSONException e)
	 * { e.printStackTrace(); } }
	 * 
	 * } return json.toString(); }
	 * 
	 * @Override public String getAmsfFormDetails(HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { Long
	 * patientId=null; JSONObject json = new JSONObject(); List<HashMap<String,
	 * Object>> c = new ArrayList<HashMap<String, Object>>(); if
	 * (!jsondata.isEmpty()) {
	 * 
	 * JSONObject nullbalankvalidation = null; nullbalankvalidation =
	 * ValidateUtils.checkPatientVisit(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else {
	 * 
	 * List<MasMedicalExamReport> getMasExam =
	 * mbDao.getMasMedicalExamReport(Long.parseLong(jsondata.get("visitId").toString
	 * ()));
	 * 
	 * if (getMasExam != null && ! CollectionUtils.isEmpty(getMasExam) ) {
	 * 
	 * try { for (MasMedicalExamReport v : getMasExam) {
	 * 
	 * HashMap<String, Object> pt = new HashMap<String, Object>();
	 * 
	 * pt.put("visitId", v.getVisitId()); pt.put("patientId", v.getPatientId());
	 * //pt.put("serviceNo", v.getPatient().getServiceNo());
	 * if(v.getIdentificationMarks1()!=null ) {
	 * pt.put("identificationMarks1",v.getIdentificationMarks1()); }
	 * if(v.getIdentificationMarks2()!=null) { pt.put("identificationMarks2",
	 * v.getIdentificationMarks2()); } if(v.getWittnessId()!=null) {
	 * pt.put("wittnessId", v.getWittnessId()); } if(v.getTotalTeeth()!=null) {
	 * pt.put("totalTeeth",v.getTotalTeeth()); }
	 * 
	 * if(v.getTotalDefectiveTeeth()!=null) { pt.put("totalDefectiveTeeth",
	 * v.getTotalDefectiveTeeth()); } if(v.getTotalNoDentalPoint()!=null) {
	 * pt.put("totalNoDentalPoint", v.getTotalNoDentalPoint()); }
	 * 
	 * if(v.getMissingTeeth()!=null) { pt.put("missingTeeth", v.getMissingTeeth());
	 * 
	 * } if(v.getUnsaveableTeeth()!=null) { pt.put("unsaveableTeeth",
	 * v.getUnsaveableTeeth()); } if(v.getConditionofgums()!=null) {
	 * pt.put("conditionofgums", v.getConditionofgums()); }
	 * if(v.getDentalOfficer()!=null) { pt.put("dentalOfficer",
	 * v.getDentalOfficer()); } if(v.getDentalCheckupDate()!=null) { String
	 * dentalCheckupDate =
	 * HMSUtil.convertDateToStringFormat(v.getDentalCheckupDate(), "dd/MM/yyyy");
	 * pt.put("dentalCheckupDate", dentalCheckupDate); } if(v.getRemarks()!=null) {
	 * pt.put("remarks", v.getRemarks()); } if(v.getHeight()!=null) {
	 * pt.put("height", v.getHeight()); }
	 * 
	 * if(v.getIdealweight()!=null) { pt.put("idealweight", v.getIdealweight()); }
	 * if(v.getWeight()!=null) { pt.put("weight",v.getWeight()); }
	 * if(v.getOverweight()!=null) { pt.put("variationinWeight",v.getOverweight());
	 * }
	 * 
	 * if(v.getBhi()!=null) { pt.put("bmi",v.getBhi()); } if(v.getBodyfat()!=null) {
	 * pt.put("bodyfat",v.getBodyfat()); } if(v.getWaist()!=null) {
	 * pt.put("waist",v.getWaist()); } if(v.getHips()!=null) {
	 * pt.put("hips",v.getHips()); } if(v.getWhr()!=null) {
	 * pt.put("whr",v.getWhr()); } if(v.getSkin()!=null) {
	 * pt.put("skin",v.getSkin()); } if(v.getChestfullexpansion()!=null) {
	 * pt.put("chestfullexpansion",v.getChestfullexpansion()); }
	 * if(v.getRangeofexpansion()!=null) {
	 * pt.put("rangeofexpansion",v.getRangeofexpansion()); }
	 * if(v.getSportman()!=null) { pt.put("sportman",v.getSportman()); }
	 * if(v.getWthoutGlassesRDistant()!=null) {
	 * pt.put("withoutGlassesRDistant",v.getWthoutGlassesRDistant()); }
	 * if(v.getWithoutGlassesLDistant()!=null) {
	 * pt.put("withoutGlassesLDistant",v.getWithoutGlassesLDistant()); }
	 * if(v.getWithGlassesLDistant()!=null) {
	 * pt.put("withGlassesLDistant",v.getWithGlassesLDistant()); }
	 * if(v.getWithGlassesRDistant()!=null) {
	 * pt.put("withGlassesRDistant",v.getWithGlassesRDistant()); }
	 * if(v.getWithoutGlassesRNearvision()!=null) {
	 * pt.put("withoutGlassesRNearvision",v.getWithoutGlassesRNearvision()); }
	 * if(v.getWithoutGlassesLNearvision()!=null) {
	 * pt.put("withoutGlassesLNearvision",v.getWithoutGlassesLNearvision()); }
	 * if(v.getWithGlassesRNearvision()!=null) {
	 * pt.put("withGlassesRNearvision",v.getWithGlassesRNearvision()); }
	 * if(v.getWithGlassesLNearvision()!=null) {
	 * pt.put("withGlassesLNearvision",v.getWithGlassesLNearvision()); }
	 * if(v.getNearVisionWithGlassCp()!=null) {
	 * pt.put("cp",v.getNearVisionWithGlassCp()); } if(v.getEarHearingRfw()!=null) {
	 * pt.put("earHearingRfw",v.getEarHearingRfw()); }
	 * if(v.getEarHearingLfw()!=null) {
	 * pt.put("earHearingLfw",v.getEarHearingLfw()); }
	 * if(v.getEarHearingBothFw()!=null) {
	 * pt.put("earHearingBothFw",v.getEarHearingBothFw()); }
	 * if(v.getHearingRcv()!=null) { pt.put("hearingRcv",v.getHearingRcv()); }
	 * if(v.getHearingLcv()!=null) { pt.put("hearingLcv",v.getHearingLcv()); }
	 * if(v.getHearingBothCv()!=null) {
	 * pt.put("hearingBothCv",v.getHearingBothCv()); }
	 * 
	 * if(v.getTympanicr()!=null) { pt.put("tympanicr",v.getTympanicr()); }
	 * if(v.getTympanicl()!=null) { pt.put("tympanicl",v.getTympanicl()); }
	 * if(v.getMobilityl()!=null) { pt.put("mobilityl",v.getMobilityl()); }
	 * if(v.getMobilityr()!=null) { pt.put("mobilityr",v.getMobilityr()); }
	 * if(v.getNoseThroatSinuses()!=null) {
	 * pt.put("noseThroatSinuses",v.getNoseThroatSinuses()); }
	 * if(v.getAudiometryRecord()!=null) {
	 * pt.put("audiometryRecord",v.getAudiometryRecord()); }
	 * if(v.getPulseRates()!=null) { pt.put("pulseRates",v.getPulseRates()); }
	 * if(v.getBpSystolic()!=null) { pt.put("bpSystolic",v.getBpSystolic()); }
	 * if(v.getBpDiastolic()!=null) { pt.put("bpDiastolic",v.getBpDiastolic()); }
	 * if(v.getPeripheralPulsations()!=null) {
	 * pt.put("peripheralPulsations",v.getPeripheralPulsations()); }
	 * if(v.getHeartSize()!=null) { pt.put("heartSize",v.getHeartSize()); }
	 * if(v.getSounds()!=null) { pt.put("sounds",v.getSounds()); }
	 * if(v.getRhythm()!=null) { pt.put("rhythm",v.getRhythm()); }
	 * if(v.getRespiratorySystem()!=null) {
	 * pt.put("respiratorySystem",v.getRespiratorySystem()); }
	 * if(v.getLiver()!=null) { pt.put("liver",v.getLiver()); }
	 * if(v.getSpleen()!=null) { pt.put("spleen",v.getSpleen()); }
	 * if(v.getHigherMentalFunction()!=null) {
	 * pt.put("higherMentalFunction",v.getHigherMentalFunction()); }
	 * if(v.getSpeech()!=null) { pt.put("speech",v.getSpeech()); }
	 * if(v.getReflexes()!=null) { pt.put("reflexes",v.getReflexes()); }
	 * 
	 * 
	 * if(v.getTremors()!=null) { pt.put("tremors",v.getTremors()); }
	 * if(v.getSelfBalancingTest()!=null) {
	 * pt.put("selfBalancingTest",v.getSelfBalancingTest()); }
	 * if(v.getLocomoterSystem()!=null) {
	 * pt.put("locomoterSystem",v.getLocomoterSystem()); } if(v.getSpine()!=null) {
	 * pt.put("spine",v.getSpine()); } if(v.getHerniaMusic()!=null) {
	 * pt.put("herniaMusic",v.getHerniaMusic()); } if(v.getHydrocele()!=null) {
	 * pt.put("hydrocele",v.getHydrocele()); } if(v.getHemorrhoids()!=null) {
	 * pt.put("hemorrhoids",v.getHemorrhoids()); } if(v.getBreasts()!=null) {
	 * pt.put("breasts",v.getBreasts()); } if(v.getFinalobservation()!=null) {
	 * pt.put("pendingRemarks",v.getFinalobservation()); }
	 * 
	 * if(v.getMenstrualHistory()!=null && v.getMenstrualHistory()!="") {
	 * pt.put("menstrualHistory", v.getMenstrualHistory()); }
	 * if(v.getLmpStatus()!=null && v.getLmpStatus()!="") { pt.put("lmpStatus",
	 * v.getLmpStatus()); } if(v.getNoOfPregnancies()!=null) {
	 * pt.put("noOfPregnancies", v.getNoOfPregnancies()); }
	 * if(v.getNoOfAbortions()!=null) { pt.put("noOfAbortions",
	 * v.getNoOfAbortions()); } if(v.getNoOfChildren()!=null) {
	 * pt.put("noOfChildren", v.getNoOfChildren()); }
	 * 
	 * if(v.getLastConfinementDate()!=null) { pt.put("lastConfinementDate",
	 * v.getLastConfinementDate()); } if(v.getVaginalDischarge()!=null) {
	 * pt.put("vaginalDischarge", v.getVaginalDischarge()); }
	 * if(v.getUsgAbdomen()!=null) { pt.put("usgAbdomen", v.getUsgAbdomen()); }
	 * if(v.getProlapse()!=null) { pt.put("prolapse", v.getProlapse()); }
	 * 
	 * c.add(pt); //}
	 * 
	 * else { return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}"; }
	 * 
	 * json.put("data", c); json.put("size", c.size()); // json.put("Visit List",
	 * getvisit); json.put("msg", "Mb AMSF Form Details sucessfull... ");
	 * json.put("status", "1");
	 * 
	 * } }
	 * 
	 * catch(Exception e) { e.printStackTrace(); return
	 * "{\"status\":\"0\",\"msg\":\"Somting went wrong}"; } } else { try {
	 * json.put("msg", "Visit ID data not found"); json.put("status", 0); } catch
	 * (JSONException e) { e.printStackTrace(); } } }
	 * 
	 * } return json.toString();
	 * 
	 * }
	 * 
	 * @Override public String saveUpdateSpecialistMBDetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { String
	 * opd = null; JSONObject json = new JSONObject(); Calendar calendar =
	 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime());
	 * 
	 * try { if (!jsondata.isEmpty()) {
	 * 
	 * opd = mbDao.saveUpdateSpeicialistOpinionMBDetails(jsondata);
	 * 
	 * if (opd != null && opd.equalsIgnoreCase("Successfully saved")) {
	 * json.put("msg",
	 * "Refer for Opinion and Medical Board Details Saved successfully ");
	 * json.put("status", "1"); } else if (opd != null &&
	 * opd.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", opd); json.put("status", "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String saveAmsfForm15Details(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { String
	 * mbAMSF15 = null; // TODO Auto-generated method stub Date
	 * currentDate=ProjectUtils.getCurrentDate();
	 * 
	 * JSONObject json = new JSONObject();
	 * 
	 * Calendar calendar = Calendar.getInstance(); java.sql.Timestamp
	 * ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); try { if
	 * (!jsondata.isEmpty()) { JSONObject nullbalankvalidation = null;
	 * nullbalankvalidation =
	 * ValidateUtils.addVitalPreConsulataionDetailsvalidation(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else { mbAMSF15 =
	 * mbDao.saveAmsfForm15Details(jsondata);
	 * 
	 * }
	 * 
	 * //if (opd != null ||oph != null &&
	 * opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) { if (mbAMSF15 !=
	 * null && mbAMSF15.equalsIgnoreCase("Successfully saved")) { json.put("msg",
	 * "MB Patinet Details Saved successfully "); json.put("status", "1"); } else if
	 * (mbAMSF15 != null && mbAMSF15.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", mbAMSF15); json.put("status", "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String getAmsf15FormDetails(HashMap<String, String>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { Long
	 * patientId = null; JSONObject json = new JSONObject(); List<HashMap<String,
	 * Object>> c = new ArrayList<HashMap<String, Object>>(); if
	 * (!jsondata.isEmpty()) {
	 * 
	 * JSONObject nullbalankvalidation = null; nullbalankvalidation =
	 * ValidateUtils.checkPatientVisit(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else {
	 * 
	 * List<PatientMedBoard> getPatientMed =
	 * mbDao.getPatientMedBoardReports(Long.parseLong(jsondata.get("visitId").
	 * toString())); if (getPatientMed != null &&
	 * !CollectionUtils.isEmpty(getPatientMed)) { try { for (PatientMedBoard v :
	 * getPatientMed) {
	 * 
	 * HashMap<String, Object> pt = new HashMap<String, Object>();
	 * 
	 * pt.put("visitId", v.getVisitId()); pt.put("patientId", v.getPatientId()); //
	 * pt.put("serviceNo", v.getPatient().getServiceNo()); if (v.getAuthority() !=
	 * null) { pt.put("authorityforBoard", v.getAuthority()); } if
	 * (v.getPlaceOfBoard() != null) { pt.put("placeOfBoard", v.getPlaceOfBoard());
	 * } if (v.getHeight() != null) { pt.put("height", v.getHeight()); } if
	 * (v.getWeight() != null) { pt.put("weight", v.getWeight()); } if
	 * (v.getDateOfEnrolment() != null) { String dateOfEnrolment =
	 * HMSUtil.convertDateToStringFormat(v.getDateOfEnrolment(), "dd/MM/yyyy");
	 * pt.put("dateOfEnrolment", dateOfEnrolment); } if (v.getAddressOnLeave() !=
	 * null) { pt.put("addressOnLeave", v.getAddressOnLeave()); } if
	 * (v.getCeasedDutyOn() != null) { String ceasedDutyOn =
	 * HMSUtil.convertDateToStringFormat(v.getDateOfEnrolment(), "dd/MM/yyyy");
	 * pt.put("ceasedDutyOn", ceasedDutyOn); } if (v.getRecordOfficeAddress() !=
	 * null) { pt.put("recordOfficeAddress", v.getRecordOfficeAddress()); } if
	 * (v.getDisablityAttrService() != null) { pt.put("disablityAttrService",
	 * v.getDisablityAttrService()); } if (v.getDisablityAttrServiceRemark() !=
	 * null) { pt.put("disablityAttrServiceRemark",
	 * v.getDisablityAttrServiceRemark()); } if (v.getDirectlyAttrService() != null)
	 * { pt.put("directlyAttrService", v.getDirectlyAttrService()); } if
	 * (v.getDirectlyAttrServiceRemark() != null) {
	 * pt.put("directlyAttrServiceRemark", v.getDirectlyAttrServiceRemark()); } if
	 * (v.getPreviousDisablement() != null) { pt.put("previousDisablement",
	 * v.getPreviousDisablement()); } if (v.getPresentDisablement() != null) {
	 * pt.put("presentDisablement", v.getPresentDisablement()); } if
	 * (v.getPreviousDisablement() != null) { pt.put("previousDisablement",
	 * v.getPreviousDisablement()); } if (v.getReasonForVarious() != null) {
	 * pt.put("reasonForVarious", v.getReasonForVarious()); } if
	 * (v.getRestrictionRegardingEmp() != null) { pt.put("restrictionRegardingEmp",
	 * v.getRestrictionRegardingEmp()); } if (v.getInstructionRemark() != null) {
	 * pt.put("instructionRemark", v.getInstructionRemark()); } if
	 * (v.getInstructionNote() != null) { pt.put("instructionNote",
	 * v.getInstructionNote()); } if (v.getSigDate() != null) { String signDate =
	 * HMSUtil.convertDateToStringFormat(v.getSigDate(), "dd/MM/yyyy");
	 * pt.put("signDate", signDate); } if (v.getSignatureIndividual() != null) {
	 * pt.put("signatureIndividual", v.getSignatureIndividual()); } if
	 * (v.getMember1() != null) { pt.put("member1Name", v.getMember1()); } if
	 * (v.getMember2() != null) { pt.put("member2Name", v.getMember2()); } if
	 * (v.getMember3() != null) { pt.put("member3Name", v.getMember3()); } if
	 * (v.getMem1Rank() != null) { pt.put("mem1Rank", v.getMem1Rank()); } if
	 * (v.getMem2Rank() != null) { pt.put("mem2Rank", v.getMem2Rank()); } if
	 * (v.getMem3Rank() != null) { pt.put("mem3Rank", v.getMem3Rank()); } if
	 * (v.getPendingRemarks() != null) { pt.put("pendingRemarks",
	 * v.getPendingRemarks()); } if (v.getRidcId()!= null) { pt.put("ridcId",
	 * v.getRidcId()); } if (v.getAaName()!= null) { pt.put("aaName",
	 * v.getAaName()); } if (v.getAaRankDest()!= null) { pt.put("aaRank",
	 * v.getAaRankDest()); } if (v.getAaPlace()!= null) { pt.put("aaPlace",
	 * v.getAaPlace()); } if (v.getAaDate()!= null) { String aaDate =
	 * HMSUtil.convertDateToStringFormat(v.getAaDate(), "dd/MM/yyyy");
	 * pt.put("aaDate", aaDate); } if (v.getPaName() != null) { pt.put("paName",
	 * v.getPaName()); } if (v.getPaRankDest() != null) { pt.put("paRank",
	 * v.getPaRankDest()); } if (v.getPaPlace() != null) { pt.put("paPlace",
	 * v.getPaPlace()); } if (v.getPaDate() != null) { String paDate =
	 * HMSUtil.convertDateToStringFormat(v.getPaDate(), "dd/MM/yyyy");
	 * pt.put("paDate", paDate); } if (v.getHospitalId() != null) {
	 * pt.put("hospitalId", v.getHospitalId()); } if (v.getTypeOfCommission() !=
	 * null) { pt.put("typeOfCommission", v.getTypeOfCommission()); } if
	 * (v.getVisit().getMasMedicalExamReport()!= null &&
	 * v.getVisit().getMasMedicalExamReport().getStatus()!=null) {
	 * pt.put("forwardStatus", v.getVisit().getMasMedicalExamReport().getStatus());
	 * } if (v.getVisit().getMasMedicalExamReport()!= null &&
	 * v.getVisit().getMasMedicalExamReport().getForwardUnitId()!=null) {
	 * pt.put("forwardUnitId",
	 * v.getVisit().getMasMedicalExamReport().getForwardUnitId()); } if
	 * (v.getVisit().getMasMedicalExamReport()!= null &&
	 * v.getVisit().getMasMedicalExamReport().getFowardedDesignationId()!=null) {
	 * pt.put("fowardedDesignationId",
	 * v.getVisit().getMasMedicalExamReport().getFowardedDesignationId()); }
	 * c.add(pt); json.put("data", c); json.put("size", c.size()); //
	 * json.put("Visit List", getvisit); json.put("msg",
	 * "Mb AMSF Form Details sucessfull... "); json.put("status", "1");
	 * 
	 * } }
	 * 
	 * catch (Exception e) { e.printStackTrace(); return
	 * "{\"status\":\"0\",\"msg\":\"Somting went wrong}"; } } else { try {
	 * json.put("msg", "Visit ID data not found"); json.put("status", 0); } catch
	 * (JSONException e) { e.printStackTrace(); } } }
	 * 
	 * } return json.toString();
	 * 
	 * }
	 * 
	 * @Override public String saveAmsf15CheckList(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { String
	 * mbAMSF15CheckList = null; JSONObject json = new JSONObject(); try { if
	 * (!jsondata.isEmpty()) { JSONObject nullbalankvalidation = null;
	 * nullbalankvalidation =
	 * ValidateUtils.addVitalPreConsulataionDetailsvalidation(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else { mbAMSF15CheckList =
	 * mbDao.saveAmsf15CheckList(jsondata);
	 * 
	 * }
	 * 
	 * if (mbAMSF15CheckList != null &&
	 * mbAMSF15CheckList.equalsIgnoreCase("Successfully saved")) { json.put("msg",
	 * "MB CheckList Patinet Details Saved successfully "); json.put("status", "1");
	 * } else if (mbAMSF15CheckList != null &&
	 * mbAMSF15CheckList.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", mbAMSF15CheckList); json.put("status", "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String getAmsf15CheckList(HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { JSONObject json =
	 * new JSONObject(); try { if (jsondata.get("visitId") == null ||
	 * jsondata.get("visitId").toString().trim().equals("")) { return
	 * "{\"status\":\"0\",\"msg\":\"json is not contain visitId as a  key or value or it is null\"}"
	 * ; } else {
	 * 
	 * List<PatientMedBoardChecklist> patientCheckListData =
	 * mbDao.getCheckList(Long.parseLong(jsondata.get("visitId").toString())); if
	 * (patientCheckListData.size() == 0) { return
	 * "{\"status\":\"0\",\"msg\":\"Data not found\"}"; } else {
	 * json.put("checkListData", patientCheckListData); json.put("msg",
	 * "Patient Check List  get sucessfull... "); json.put("status", "1");
	 * json.put("size", patientCheckListData.size());
	 * 
	 * }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String getAllEmployeeCategory(HttpServletRequest request,
	 * HttpServletResponse response) { JSONObject jsonObj = new JSONObject();
	 * List<MasEmployeeCategory> employeeCategory = mbDao.getAllEmployeeCategory();
	 * if (employeeCategory != null && employeeCategory.size() > 0) {
	 * 
	 * jsonObj.put("data", employeeCategory); jsonObj.put("count",
	 * employeeCategory.size()); jsonObj.put("status", 1); } else {
	 * jsonObj.put("data", employeeCategory); jsonObj.put("count", 0);
	 * jsonObj.put("msg", "No Record Found"); jsonObj.put("status", 0); } return
	 * jsonObj.toString(); }
	 * 
	 * @Override public String dataDigitizationSaveAmsfForm15(HashMap<String,
	 * Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * String mbAMSF15 = null; // TODO Auto-generated method stub Date
	 * currentDate=ProjectUtils.getCurrentDate();
	 * 
	 * JSONObject json = new JSONObject();
	 * 
	 * Calendar calendar = Calendar.getInstance(); java.sql.Timestamp
	 * ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); try { if
	 * (!jsondata.isEmpty()) { JSONObject nullbalankvalidation = null;
	 * nullbalankvalidation =
	 * ValidateUtils.addVitalPreConsulataionDetailsvalidation(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else { mbAMSF15 =
	 * mbDao.dataDigitizationsaveAmsfForm15(jsondata);
	 * 
	 * }
	 * 
	 * //if (opd != null ||oph != null &&
	 * opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) { if (mbAMSF15 !=
	 * null && mbAMSF15.equalsIgnoreCase("Successfully saved")) { json.put("msg",
	 * "MB Patinet Details Saved successfully "); json.put("status", "1"); } else if
	 * (mbAMSF15 != null && mbAMSF15.equalsIgnoreCase("403")) { json.put("msg",
	 * " you are not authorized for this activity "); json.put("status", "0"); }
	 * else { json.put("msg", mbAMSF15); json.put("status", "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @Override public String dataDigitizationSaveSpecialistOpinion(HashMap<String,
	 * Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * HashMap<String,Object>opd ;
	 * 
	 * JSONObject json = new JSONObject(); Calendar calendar =
	 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime());
	 * 
	 * try { if (!jsondata.isEmpty()) { JSONObject nullbalankvalidation = null;
	 * nullbalankvalidation =
	 * ValidateUtils.addVitalPreConsulataionDetailsvalidation(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else {
	 * 
	 * opd = mbDao.dataDigitizationSaveSpecialistOpinion(jsondata);
	 * 
	 * //}
	 * 
	 * if (opd.get("Successfully saved").toString()!=null) { json.put("msg",
	 * "Refer for Opinion and Medical Board Details Saved successfully ");
	 * json.put("status", "1"); } else { json.put("msg", opd); json.put("status",
	 * "0"); }
	 * 
	 * } else { json.put("status", "0"); json.put("msg",
	 * "json not contain any object"); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return json.toString(); }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object>
	 * dataDigitizationSaveSpecialistOpinionNew(Map<String, Object> requestData) {
	 * Map<String, Object> responseMap = new HashMap<String, Object>(); Map<String,
	 * Object> rosterValueForToken = new HashMap<String, Object>(); //responseMap =
	 * mbDao.dataDigitizationSaveSpecialistOpinion(requestData);
	 * 
	 * responseMap.put("jsonList", ""); return responseMap; }
	 * 
	 * @Override public String getCategoryDetails(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Long patientId=null; JSONObject json = new JSONObject(); List<HashMap<String,
	 * Object>> c = new ArrayList<HashMap<String, Object>>(); List<HashMap<String,
	 * Object>> c1 = new ArrayList<HashMap<String, Object>>(); if
	 * (!jsondata.isEmpty()) {
	 * 
	 * JSONObject nullbalankvalidation = null; nullbalankvalidation =
	 * ValidateUtils.checkPatientId(jsondata); if
	 * (nullbalankvalidation.optString("status").equals("0")) { return
	 * nullbalankvalidation.toString(); } else { MasMedicalExamReport
	 * masMedicalExamReport=null; List<PatientMedicalCat> getPatientVisit =null;
	 * 
	 * if(jsondata.containsKey("flagForMe") && jsondata.get("visitId")!=null) {
	 * masMedicalExamReport=medicalExamDAO.getMasMedicalExamReprtByVisitIdForStatus(
	 * Long.parseLong(jsondata.get("visitId").toString()));
	 * if(masMedicalExamReport!=null && (masMedicalExamReport.getStatus()==null ||
	 * masMedicalExamReport.getStatus().equalsIgnoreCase(""))) { getPatientVisit =
	 * mbDao.getPatientMedicalBoardDetailsCategory(Long.parseLong(jsondata.get(
	 * "patientId").toString())); } else { getPatientVisit =
	 * mbDao.getPatientMedicalBoardDetails(Long.parseLong(jsondata.get("visitId").
	 * toString())); } }
	 * 
	 * else if(jsondata.containsKey("flagForDigi") && jsondata.get("visitId")!=null)
	 * { getPatientVisit =
	 * mbDao.getPatientMedicalBoardDetails(Long.parseLong(jsondata.get("visitId").
	 * toString())); } else { getPatientVisit =
	 * mbDao.getPatientMedicalBoardDetailsCategory(Long.parseLong(jsondata.get(
	 * "patientId").toString())); } Map<String,Object> getDgOrder =
	 * mbDao.getPatientMedicalBoardDetailsDgOrder(jsondata); List<DgOrderdt>
	 * getDgOderData = (List<DgOrderdt>) getDgOrder.get("list");
	 * 
	 * if (getPatientVisit != null && ! CollectionUtils.isEmpty(getPatientVisit) ) {
	 * 
	 * try { for (PatientMedicalCat v : getPatientVisit) { //if
	 * (v.getVisitStatus().equals("w")||v.getVisitStatus().equals("p")){
	 * HashMap<String, Object> pt = new HashMap<String, Object>(); pt.put("visitId",
	 * v.getVisitId()); pt.put("patientId", v.getPatientId());
	 * if(v.getMasIcd()!=null && StringUtils.isNotEmpty(v.getMasIcd().getIcdName()))
	 * pt.put("icdName", v.getMasIcd().getIcdName()); else pt.put("icdName", "");
	 * pt.put("system", v.getSystem()); pt.put("mbStatus", v.getMbStatus());
	 * if(v.getMasMedicalCategory()!=null)
	 * pt.put("medicalCategory",v.getMasMedicalCategory().getMedicalCategoryName());
	 * if(v.getCategoryType()!=null) { pt.put("categoryType", v.getCategoryType());
	 * } else { pt.put("categoryType",""); } String dateOfPresentCategory =
	 * HMSUtil.convertDateToStringFormat(v.getCategoryDate(), "dd/MM/yyyy");
	 * pt.put("categoryDate", dateOfPresentCategory);
	 * pt.put("duration",v.getDuration()); String dateOfNextCategory =
	 * HMSUtil.convertDateToStringFormat(v.getNextCategoryDate(), "dd/MM/yyyy");
	 * pt.put("nextCategoryDate",dateOfNextCategory );
	 * pt.put("patientMedicalCategoryId", v.getMedicalCatId());
	 * pt.put("diagnosisId", v.getIcdId()); pt.put("applyFor", v.getApplyFor());
	 * if(v.getDateOfOrigin()!=null) { String dateOfOrigin =
	 * HMSUtil.convertDateToStringFormat(v.getDateOfOrigin(), "dd/MM/yyyy");
	 * pt.put("dateOfOrigin",dateOfOrigin); }
	 * pt.put("placeOfOrigin",v.getPlaceOfOrigin()); pt.put("medicalCategoryId",
	 * v.getMedicalCategoryId()); pt.put("recommendStatus", v.getRecommendFlag());
	 * pt.put("fitFlag", v.getpMedFitFlag()); pt.put("fitCatId", v.getpMedCatId());
	 * if(v.getMasMedicalCategoryFit()!=null) {
	 * pt.put("medicalCategoryFit",v.getMasMedicalCategoryFit().
	 * getMedicalCategoryName()); } else { pt.put("medicalCategoryFit",""); }
	 * if(v.getpMedCatDate()!=null) { String fitCatDate =
	 * HMSUtil.convertDateToStringFormat(v.getpMedCatDate(), "dd/MM/yyyy");
	 * pt.put("fitCatDate",fitCatDate); } c.add(pt); }
	 * 
	 * for (DgOrderdt dg : getDgOderData) { HashMap<String, Object> dData = new
	 * HashMap<String, Object>(); dData.put("orderHdId",dg.getOrderhdId());
	 * dData.put("orderDtId",dg.getOrderdtId());
	 * dData.put("inveId",dg.getDgMasInvestigations().getInvestigationId());
	 * dData.put("inveName",dg.getDgMasInvestigations().getInvestigationName());
	 * 
	 * c1.add(dData); } json.put("data", c); json.put("dataDgDt", c1);
	 * json.put("size", c.size()); // json.put("Visit List", getvisit);
	 * json.put("msg", "Medical board List  get  sucessfull... ");
	 * json.put("status", "1");
	 * 
	 * }
	 * 
	 * 
	 * catch(Exception e) { e.printStackTrace(); return
	 * "{\"status\":\"0\",\"msg\":\"Somting went wrong}"; } } else { try {
	 * json.put("msg", "Visit ID data not found"); json.put("status", 0); } catch
	 * (JSONException e) { e.printStackTrace(); } } }
	 * 
	 * } return json.toString();
	 * 
	 * }
	 */

}
