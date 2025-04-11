package com.mmu.services.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.CommonDao;
import com.mmu.services.dao.FamilyDetailsDAO;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgNormalValue;
import com.mmu.services.entity.DgSubMasInvestigation;
import com.mmu.services.entity.FamilyDetail;
import com.mmu.services.entity.MasDocument;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasUOM;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.PatientDocumentDetail;
import com.mmu.services.entity.PatientImmunizationHistory;
import com.mmu.services.entity.RidcEntity;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.CommonService;
import com.mmu.services.service.impl.OpdServiceImpl;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.UtilityServices;

@Repository
public class CommonServiceImpl implements CommonService {

	@Autowired
	CommonDao cd;
	@Autowired
	MedicalExamDAO medicalExamDAO;
	@Autowired
	OpdMasterDao md;

	@Autowired
	PatientRegistrationDao patientRegistrationDao;
	@Autowired
	FamilyDetailsDAO familyDetailsDAO;
	@Autowired
	GetHibernateUtils getHibernateUtils;

	/*
	 * @Override public String getRidcDocumentInfo(HashMap<String, String> jsondata,
	 * HttpServletRequest request) { JSONObject json = new JSONObject(); try { if
	 * (jsondata.get("ridcId") == null ||
	 * jsondata.get("ridcId").toString().trim().equals("")) { return
	 * "{\"status\":\"0\",\"msg\":\"json is not contain ridcId as a  key or value or it is null\"}"
	 * ; } else {
	 * 
	 * List<RidcEntity> ridc_info = cd.getRidcDocument(jsondata);
	 * 
	 * if (ridc_info.size() == 0) { return
	 * "{\"status\":\"0\",\"msg\":\"Data not found\"}"; } else {
	 * 
	 * json.put("ridcInfoList", ridc_info); json.put("Size", ridc_info.size());
	 * json.put("msg", "ridcInfo  get  sucessfull... "); json.put("status", "1");
	 * 
	 * }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String getDocumentList(HashMap<String, Object> jsondata) {
	 * JSONObject json = new JSONObject(); try { if (jsondata.get("employeeId") ==
	 * null || jsondata.get("employeeId").toString().trim().equals("")) { return
	 * "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}"
	 * ; } else {
	 * 
	 * List<MasDocument> mstMasDocument = cd.getDocumentList(jsondata);
	 * List<PatientDocumentDetail> listPatientDocumentDetail =
	 * cd.getPatientDocumentDetail(jsondata); if (mstMasDocument.size() == 0) {
	 * return "{\"status\":\"0\",\"msg\":\"Data not found\"}"; } else {
	 * json.put("MasDocumentList", mstMasDocument);
	 * json.put("listPatientDocumentDetail", listPatientDocumentDetail);
	 * json.put("Size", mstMasDocument.size()); json.put("msg",
	 * "mstMasDocument  get  sucessfull... "); json.put("status", "1"); //} }
	 * 
	 * 
	 * 
	 * } } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String getSubInvestigationHtml(HashMap<String, Object>
	 * jsondata) { JSONObject json = new JSONObject(); List<Object> responseList =
	 * new ArrayList<Object>(); try { if (jsondata.get("employeeId") == null ||
	 * jsondata.get("employeeId").toString().trim().equals("")) { return
	 * "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}"
	 * ; } else {
	 * 
	 * 
	 * List<DgMasInvestigation> listDgMasInvestigation =
	 * cd.getDgMasInvestigationByInvestigationId(jsondata); String genderCode= cd.
	 * getAdminSexCode( jsondata); if (listDgMasInvestigation.size() == 0) { return
	 * "{\"status\":\"0\",\"msg\":\"Data not found\"}"; } else { if
	 * (CollectionUtils.isNotEmpty(listDgMasInvestigation)) { for
	 * (DgMasInvestigation dgMasInvestigation : listDgMasInvestigation) {
	 * 
	 * 
	 * MasUOM masUOM=dgMasInvestigation.getMasUOM(); if(masUOM!=null)
	 * System.out.println("masUOMDetail"+masUOM.getUOMCode()+""+masUOM.getUOMName())
	 * ; List<DgSubMasInvestigation>listDgSubMasInvestigations=dgMasInvestigation.
	 * getDgSubMasInvestigations(); for(DgSubMasInvestigation
	 * dgSubMasInvestigation:listDgSubMasInvestigations) { MasUOM
	 * masUOM1=dgSubMasInvestigation.getMasUOM();
	 * 
	 * HashMap<String, Object> jsonMap = new HashMap<String, Object>();
	 * if(dgSubMasInvestigation.getSubInvestigationId()!=null)
	 * jsonMap.put("dgSubMasInvestigationId",
	 * dgSubMasInvestigation.getSubInvestigationId()); else
	 * jsonMap.put("dgSubMasInvestigationId", "");
	 * 
	 * if(dgSubMasInvestigation.getSubInvestigationName()!=null)
	 * jsonMap.put("subInvestigationName",
	 * dgSubMasInvestigation.getSubInvestigationName()); else
	 * jsonMap.put("subInvestigationName", "");
	 * 
	 * if(dgSubMasInvestigation.getSubInvestigationName()!=null)
	 * jsonMap.put("subInvestigationCode",
	 * dgSubMasInvestigation.getSubInvestigationCode()); else
	 * jsonMap.put("subInvestigationCode", "");
	 * 
	 * if(masUOM1!=null ) { if(masUOM1.getUOMId()!=null) jsonMap.put("uOMId",
	 * masUOM1.getUOMId()); else jsonMap.put("uOMId", "");
	 * 
	 * 
	 * if(masUOM1.getUOMName()!=null) jsonMap.put("uOMName", masUOM1.getUOMName());
	 * else jsonMap.put("uOMName", "");
	 * 
	 * if(masUOM1.getUOMCode()!=null) jsonMap.put("uOMCode", masUOM1.getUOMCode());
	 * else jsonMap.put("uOMCode", ""); } else { jsonMap.put("uOMId", "");
	 * jsonMap.put("uOMName", ""); jsonMap.put("uOMCode", ""); }
	 * 
	 * if(dgSubMasInvestigation.getInvestigationId()!=null)
	 * jsonMap.put("investigationIdInsub",
	 * dgSubMasInvestigation.getInvestigationId()); else
	 * jsonMap.put("investigationIdInsub", "");
	 * 
	 * if(dgSubMasInvestigation.getDgMasInvestigation()!=null) {
	 * if(dgSubMasInvestigation.getDgMasInvestigation().getInvestigationType()!=
	 * null) jsonMap.put("investigationType",
	 * dgSubMasInvestigation.getDgMasInvestigation().getInvestigationType()); else
	 * jsonMap.put("investigationType", ""); }
	 * 
	 * if(dgSubMasInvestigation.getDgNormalValue()!=null) { String range="";
	 * 
	 * List<DgNormalValue>listDgNormalValue=dgSubMasInvestigation.getDgNormalValue()
	 * ;
	 * 
	 * if(CollectionUtils.isNotEmpty(listDgNormalValue)) { for(DgNormalValue
	 * dgNormalValue :listDgNormalValue) { if(dgNormalValue!=null &&
	 * dgNormalValue.getSex().equalsIgnoreCase(genderCode)) {
	 * 
	 * if(dgNormalValue.getMinNormalValue()!=null)
	 * range=dgNormalValue.getMinNormalValue();
	 * if(dgNormalValue.getMaxNormalValue()!=null) range+= "-"+
	 * dgNormalValue.getMaxNormalValue();
	 * 
	 * } } }
	 * 
	 * 
	 * jsonMap.put("rangeValSub", range); String mainChargeCodeName=""; if
	 * (dgMasInvestigation.getMainChargecodeId() != null) {
	 * 
	 * MasMainChargecode mmcc=null; mmcc =
	 * medicalExamDAO.getMainChargeCodeByMainChargeCodeId(dgMasInvestigation.
	 * getMainChargecodeId()); mainChargeCodeName=mmcc.getMainChargecodeCode(); }
	 * jsonMap.put("mainChargeCodeName", mainChargeCodeName);
	 * 
	 * if(dgSubMasInvestigation.getDgMasInvestigation()!=null) {
	 * if(dgSubMasInvestigation.getDgMasInvestigation().getMainChargecodeId()!=null)
	 * jsonMap.put("mainChargeCodeIdSubVal",
	 * dgSubMasInvestigation.getDgMasInvestigation().getMainChargecodeId()); else
	 * jsonMap.put("mainChargeCodeIdSubVal", "");
	 * 
	 * if(dgSubMasInvestigation.getDgMasInvestigation().getSubChargecodeId()!=null)
	 * jsonMap.put("subChargeCodeIdVal",
	 * dgSubMasInvestigation.getDgMasInvestigation().getSubChargecodeId()); else
	 * jsonMap.put("subChargeCodeIdVal", ""); } else {
	 * jsonMap.put("mainChargeCodeIdSubVal", ""); jsonMap.put("subChargeCodeIdVal",
	 * ""); }
	 * 
	 * }
	 * 
	 * //}
	 * 
	 * responseList.add(jsonMap); } if (responseList != null && responseList.size()
	 * > 0) { json.put("subInvestigationData", responseList); json.put("status", 1);
	 * 
	 * } else { json.put("subInvestigationData", responseList); json.put("msg",
	 * "Data not found"); json.put("status", 0); } }
	 * 
	 * 
	 * } }
	 * 
	 * } } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String
	 * getInvestigationAndSubInvesForTemplate(HashMap<String, Object> jsondata) {
	 * JSONObject json = new JSONObject(); if (jsondata.get("templateId") == null ||
	 * jsondata.get("templateId").toString().trim().equals("")) { return
	 * "{\"status\":\"0\",\"msg\":\"json is not contain Template ID as a  key or value or it is null\"}"
	 * ;
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * 
	 * Map<String,Object> map = md.getTemplateInvestigation(jsondata);
	 * List<OpdTemplateInvestigation> getInvestigationData =
	 * (List<OpdTemplateInvestigation>) map.get("list"); List<HashMap<String,
	 * Object>> c = new ArrayList<HashMap<String, Object>>();
	 * 
	 * String genderCode= cd. getAdminSexCode( jsondata);
	 * 
	 * 
	 * Map<String,List<Map<String, Object>>>listOfObject=new HashMap<>();
	 * 
	 * Long investionIdForTemplate=null; try {
	 * 
	 * for (OpdTemplateInvestigation tempInve : getInvestigationData) { String
	 * mainChargeCodeNameForInve=""; HashMap<String, Object> ti = new
	 * HashMap<String, Object>(); ti.put("templateName",
	 * tempInve.getOpdTemplate().getTemplateCode());
	 * ti.put("templateCode",tempInve.getOpdTemplate().getTemplateName());
	 * if(tempInve.getInvestigationId()!=null) {
	 * investionIdForTemplate=tempInve.getInvestigationId(); } else {
	 * investionIdForTemplate=null; }
	 * ti.put("templateInvestgationId",investionIdForTemplate);
	 * ti.put("templateDataId", tempInve.getTemplateInvestigationId());
	 * ti.put("investigationName",
	 * tempInve.getDgMasInvestigation().getInvestigationName());
	 * 
	 * 
	 * if(tempInve!=null && tempInve.getDgMasInvestigation()!=null &&
	 * tempInve.getDgMasInvestigation().getMasUOM()!=null &&
	 * tempInve.getDgMasInvestigation().getMasUOM().getUOMName()!=null) {
	 * ti.put("uomName", tempInve.getDgMasInvestigation().getMasUOM().getUOMName());
	 * } else { ti.put("uomName", ""); }
	 * 
	 * if(tempInve!=null && tempInve.getDgMasInvestigation()!=null &&
	 * tempInve.getDgMasInvestigation().getMainChargecodeId()!=null ) {
	 * ti.put("mainChargeCodeId",
	 * tempInve.getDgMasInvestigation().getMainChargecodeId()); MasMainChargecode
	 * mmcc=null; mmcc =
	 * medicalExamDAO.getMainChargeCodeByMainChargeCodeId(Long.parseLong(tempInve.
	 * getDgMasInvestigation().getMainChargecodeId().toString()));
	 * mainChargeCodeNameForInve=mmcc.getMainChargecodeCode(); } else {
	 * ti.put("mainChargeCodeId", ""); mainChargeCodeNameForInve=""; }
	 * if(tempInve!=null && tempInve.getDgMasInvestigation()!=null &&
	 * tempInve.getDgMasInvestigation().getSubChargecodeId()!=null ) {
	 * ti.put("subChargeCodeId",
	 * tempInve.getDgMasInvestigation().getSubChargecodeId()); } else {
	 * ti.put("subChargeCodeId", ""); }
	 * 
	 * if(tempInve!=null && tempInve.getDgMasInvestigation()!=null &&
	 * tempInve.getDgMasInvestigation().getInvestigationType()!=null ) {
	 * ti.put("investigationType",
	 * tempInve.getDgMasInvestigation().getInvestigationType()); } else {
	 * ti.put("investigationType", ""); } if(tempInve!=null &&
	 * tempInve.getDgMasInvestigation()!=null &&
	 * tempInve.getDgMasInvestigation().getMinNormalValue()!=null ) {
	 * ti.put("minNormalValue",
	 * tempInve.getDgMasInvestigation().getMinNormalValue()); } else {
	 * ti.put("minNormalValue", ""); }
	 * 
	 * if(tempInve!=null && tempInve.getDgMasInvestigation()!=null &&
	 * tempInve.getDgMasInvestigation().getMaxNormalValue()!=null ) {
	 * ti.put("maxNormalValue",
	 * tempInve.getDgMasInvestigation().getMaxNormalValue()); } else {
	 * ti.put("maxNormalValue", ""); } ti.put("mainChargeCodeNameVal",
	 * mainChargeCodeNameForInve);
	 * 
	 * 
	 * 
	 * //////////////////////////////////////////////////////////////////////
	 * if(tempInve.getDgMasInvestigation()!=null &&
	 * tempInve.getDgMasInvestigation().getDgSubMasInvestigations()!=null) {
	 * List<Map<String, Object>> responseList = new ArrayList<Map<String,
	 * Object>>(); List<DgSubMasInvestigation>listDgSubMasInvestigations=tempInve.
	 * getDgMasInvestigation().getDgSubMasInvestigations();
	 * for(DgSubMasInvestigation dgSubMasInvestigation:listDgSubMasInvestigations) {
	 * MasUOM masUOM1=dgSubMasInvestigation.getMasUOM();
	 * 
	 * HashMap<String, Object> jsonMap = new HashMap<String, Object>();
	 * if(dgSubMasInvestigation.getSubInvestigationId()!=null)
	 * jsonMap.put("dgSubMasInvestigationId",
	 * dgSubMasInvestigation.getSubInvestigationId()); else
	 * jsonMap.put("dgSubMasInvestigationId", "");
	 * 
	 * if(dgSubMasInvestigation.getSubInvestigationName()!=null)
	 * jsonMap.put("subInvestigationName",
	 * dgSubMasInvestigation.getSubInvestigationName()); else
	 * jsonMap.put("subInvestigationName", "");
	 * 
	 * if(dgSubMasInvestigation.getSubInvestigationName()!=null)
	 * jsonMap.put("subInvestigationCadeForSub",
	 * dgSubMasInvestigation.getSubInvestigationCode()); else
	 * jsonMap.put("subInvestigationCadeForSub", "");
	 * 
	 * if(masUOM1!=null ) { if(masUOM1.getUOMId()!=null) jsonMap.put("uOMId",
	 * masUOM1.getUOMId()); else jsonMap.put("uOMId", "");
	 * 
	 * 
	 * if(masUOM1.getUOMName()!=null) jsonMap.put("umoNameSub",
	 * masUOM1.getUOMName()); else jsonMap.put("umoNameSub", "");
	 * 
	 * if(masUOM1.getUOMCode()!=null) jsonMap.put("uOMCode", masUOM1.getUOMCode());
	 * else jsonMap.put("uOMCode", ""); } else { jsonMap.put("uOMId", "");
	 * jsonMap.put("uOMName", ""); jsonMap.put("uOMCode", ""); }
	 * 
	 * if(dgSubMasInvestigation.getInvestigationId()!=null)
	 * jsonMap.put("investigationIdInsub",
	 * dgSubMasInvestigation.getInvestigationId()); else
	 * jsonMap.put("investigationIdInsub", "");
	 * 
	 * if(dgSubMasInvestigation.getDgMasInvestigation()!=null) {
	 * if(dgSubMasInvestigation.getDgMasInvestigation().getInvestigationType()!=
	 * null) jsonMap.put("investigationTypeSub",
	 * dgSubMasInvestigation.getDgMasInvestigation().getInvestigationType()); else
	 * jsonMap.put("investigationTypeSub", ""); }
	 * 
	 * if(dgSubMasInvestigation.getDgNormalValue()!=null) { String range="";
	 * 
	 * List<DgNormalValue>listDgNormalValue=dgSubMasInvestigation.getDgNormalValue()
	 * ;
	 * 
	 * if(CollectionUtils.isNotEmpty(listDgNormalValue)) { for(DgNormalValue
	 * dgNormalValue :listDgNormalValue) { if(dgNormalValue!=null &&
	 * dgNormalValue.getSex().equalsIgnoreCase(genderCode)) {
	 * 
	 * if(dgNormalValue.getMinNormalValue()!=null)
	 * range=dgNormalValue.getMinNormalValue();
	 * if(dgNormalValue.getMaxNormalValue()!=null) range+= "-"+
	 * dgNormalValue.getMaxNormalValue();
	 * 
	 * } } }
	 * 
	 * 
	 * jsonMap.put("rangeSub", range); String mainChargeCodeName=""; if
	 * (tempInve.getDgMasInvestigation().getMainChargecodeId() != null) {
	 * 
	 * MasMainChargecode mmcc=null; mmcc =
	 * medicalExamDAO.getMainChargeCodeByMainChargeCodeId(tempInve.
	 * getDgMasInvestigation().getMainChargecodeId());
	 * mainChargeCodeName=mmcc.getMainChargecodeCode(); }
	 * jsonMap.put("mainChargeCodeNameForSub", mainChargeCodeName);
	 * 
	 * if(dgSubMasInvestigation.getDgMasInvestigation()!=null) {
	 * if(dgSubMasInvestigation.getDgMasInvestigation().getMainChargecodeId()!=null)
	 * jsonMap.put("mainChargeCodeIdForSub",
	 * dgSubMasInvestigation.getDgMasInvestigation().getMainChargecodeId()); else
	 * jsonMap.put("mainChargeCodeIdForSub", "");
	 * 
	 * if(dgSubMasInvestigation.getDgMasInvestigation().getSubChargecodeId()!=null)
	 * jsonMap.put("subMainChargeCodeIdForSub",
	 * dgSubMasInvestigation.getDgMasInvestigation().getSubChargecodeId()); else
	 * jsonMap.put("subMainChargeCodeIdForSub", ""); } else {
	 * jsonMap.put("mainChargeCodeIdSubVal", ""); jsonMap.put("subChargeCodeIdVal",
	 * ""); } jsonMap.put("orderDtIdForSub", ""); jsonMap.put("orderHdIdForSub",
	 * ""); jsonMap.put("resultEntryDtidForSub", "");
	 * jsonMap.put("resultEntryHdidForSub", ""); jsonMap.put("resultForSub", ""); }
	 * 
	 * responseList.add(jsonMap); }
	 * listOfObject.put(investionIdForTemplate.toString(),responseList); }
	 * 
	 * 
	 * ///////////////////////////////////////////////////////////////////////
	 * 
	 * 
	 * 
	 * c.add(ti);
	 * 
	 * } if(c != null && c.size()>0){ json.put("data", c); json.put("listOfObject",
	 * listOfObject); json.put("count", c.size()); json.put("msg",
	 * "Visit List  get  sucessfull... "); json.put("status", "1"); }else{ return
	 * "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}"; } }
	 * catch(Exception e) { System.out.println(e); }
	 * 
	 * return json.toString(); } }
	 * 
	 * @Override public String getFamilyDetailsHistory(HashMap<String, Object>
	 * jsondata) { JSONObject json = new JSONObject(); try { if
	 * (jsondata.get("employeeId") == null ||
	 * jsondata.get("employeeId").toString().trim().equals("")) { return
	 * "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}"
	 * ; } else { List<HashMap<String, Object>> masRelationList = new
	 * ArrayList<HashMap<String, Object>>(); List<MasRelation>respMasRelationList
	 * =patientRegistrationDao.getRelationList(); if(respMasRelationList.size()>0) {
	 * for(MasRelation relation:respMasRelationList) { HashMap<String,Object>
	 * relationMap=new HashMap<String, Object>(); relationMap.put("relationId",
	 * relation.getRelationId()); relationMap.put("relationName",
	 * relation.getRelationName()); masRelationList.add(relationMap); }
	 * json.put("masRelationList",masRelationList); }
	 * 
	 * 
	 * List<FamilyDetail> listFamilyDetails =
	 * familyDetailsDAO.getFamilyDetailsByVisitId(jsondata); List<Object>
	 * responseList = new ArrayList<Object>();
	 * if(CollectionUtils.isNotEmpty(listFamilyDetails)) { for(FamilyDetail
	 * familyDetails:listFamilyDetails) {
	 * 
	 * HashMap<String,Object> familyDetailsMap=new HashMap<String, Object>();
	 * if(familyDetails.getFamilyId()!=null) familyDetailsMap.put("familyId",
	 * familyDetails.getFamilyId()); else { familyDetailsMap.put("familyId", null);
	 * } if(familyDetails.getLeaveApplicationId()!=null) {
	 * familyDetailsMap.put("relationId", familyDetails.getLeaveApplicationId()); }
	 * else { familyDetailsMap.put("relationId", null); }
	 * 
	 * 
	 * if(familyDetails.getAge()!=null) { familyDetailsMap.put("age",
	 * familyDetails.getAge()); } else { familyDetailsMap.put("age", null); }
	 * 
	 * if(familyDetails.getSelectFamily()!=null) {
	 * familyDetailsMap.put("healthOfFather", familyDetails.getSelectFamily()); }
	 * else { familyDetailsMap.put("healthOfFather", null); }
	 * 
	 * if(familyDetails.getDependentPorNo()!=null) {
	 * familyDetailsMap.put("causeOfDeathFather",
	 * familyDetails.getDependentPorNo()); } else {
	 * familyDetailsMap.put("causeOfDeathFather", null); } String dob="";
	 * if(familyDetails.getDob()!=null) {
	 * 
	 * Date s =
	 * HMSUtil.convertStringDateToUtilDate(familyDetails.getDob().toString(),
	 * "yyyy-MM-dd"); dob = HMSUtil.convertDateToStringFormat(s, "dd/MM/yyyy");
	 * 
	 * 
	 * familyDetailsMap.put("dob", dob); } else { familyDetailsMap.put("dob", null);
	 * } responseList.add(familyDetailsMap);
	 * 
	 * 
	 * } }
	 * 
	 * if(CollectionUtils.isNotEmpty(responseList)) { json.put("familyDetailList",
	 * responseList); json.put("msg", "mstMasDocument  get  sucessfull... ");
	 * json.put("status", "1"); } else { json.put("familyDetailList", responseList);
	 * json.put("msg", "Data is not getting "); json.put("status", "0");
	 * 
	 * }
	 * 
	 * } } catch(Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * @Override public String submitPatientImmunizationHistory(HashMap<String,
	 * Object> payload) { JSONObject json = new JSONObject(); int checkCount=0;
	 * Session session=null ; Transaction tx=null; try { session=
	 * getHibernateUtils.getHibernateUtlis().OpenSession();
	 * tx=session.beginTransaction(); String patientImmuzationIdArray ="";
	 * if(payload.get("patientImmuzationIdArray")!=null) { patientImmuzationIdArray
	 * = payload.get("patientImmuzationIdArray").toString();
	 * patientImmuzationIdArray =
	 * OpdServiceImpl.getReplaceString(patientImmuzationIdArray); }
	 * 
	 * String itemIdsValArray =""; if(payload.get("itemIdsValArray")!=null) {
	 * itemIdsValArray = payload.get("itemIdsValArray").toString(); itemIdsValArray
	 * = OpdServiceImpl.getReplaceString(itemIdsValArray); } String pvmsValArray
	 * =""; if(payload.get("pvmsValArray")!=null) { pvmsValArray =
	 * payload.get("pvmsValArray").toString(); pvmsValArray =
	 * OpdServiceImpl.getReplaceString(pvmsValArray); }
	 * 
	 * String immnunizationDate = ""; if(payload.get("immunizationDateArray")!=null)
	 * { immnunizationDate = payload.get("immunizationDateArray").toString();
	 * immnunizationDate = OpdServiceImpl.getReplaceString(immnunizationDate); }
	 * String durationImmu =""; if(payload.get("durationArray")!=null) {
	 * durationImmu = payload.get("durationArray").toString(); durationImmu =
	 * OpdServiceImpl.getReplaceString(durationImmu); } String nextDueImmu = "";
	 * if(payload.get("nextDueDateArray")!=null) { nextDueImmu =
	 * payload.get("nextDueDateArray").toString(); nextDueImmu =
	 * OpdServiceImpl.getReplaceString(nextDueImmu); }
	 * 
	 * String presceriptionDateArray = "";
	 * if(payload.get("presceriptionDateArray")!=null) { presceriptionDateArray =
	 * payload.get("presceriptionDateArray").toString(); presceriptionDateArray =
	 * OpdServiceImpl.getReplaceString(presceriptionDateArray); }
	 * 
	 * String visitId = ""; if(payload.get("visitId")!=null) { visitId =
	 * payload.get("visitId").toString(); visitId =
	 * OpdServiceImpl.getReplaceString(visitId); } String userId ="";
	 * if(payload.get("userId")!=null) { userId = payload.get("userId").toString();
	 * userId = OpdServiceImpl.getReplaceString(userId); } String patientId ="";
	 * if(payload.get("patientId")!=null) { patientId =
	 * payload.get("patientId").toString(); patientId =
	 * OpdServiceImpl.getReplaceString(patientId); }
	 * 
	 * String [] itemIdsValArrayValues=itemIdsValArray.split(",");
	 * 
	 * String [] immunisationIdValues=patientImmuzationIdArray.split(",");
	 * 
	 * String []immnunizationDateValue=immnunizationDate.split(",");
	 * 
	 * 
	 * String [] durationImmuValues=durationImmu.split(",");
	 * 
	 * String []nextDueImmuValue=nextDueImmu.split(",");
	 * 
	 * 
	 * String []presceriptionDateValue= presceriptionDateArray.split(","); String
	 * []pvmsNumberValue=pvmsValArray.split(",");
	 * 
	 * String pvmsNoFixedVal = UtilityServices.getValueOfPropByKey("pvmsNo");
	 * String[] pvmsNoFixedValues = null; if
	 * (StringUtils.isNotEmpty(pvmsNoFixedVal)) { pvmsNoFixedValues =
	 * pvmsNoFixedVal.split(","); }
	 * 
	 * if(itemIdsValArrayValues!=null) for(int
	 * i=0;i<itemIdsValArrayValues.length;i++) { PatientImmunizationHistory
	 * patientImmunizationHistory=null; if(immunisationIdValues[i]!=null &&
	 * !immunisationIdValues[i].equalsIgnoreCase("0"))
	 * patientImmunizationHistory=medicalExamDAO.
	 * getPatientImmunizationHistoryByImmunizationId(Long.parseLong(
	 * immunisationIdValues[i].trim())); if(patientImmunizationHistory==null) {
	 * patientImmunizationHistory=medicalExamDAO.
	 * getPatientImmunizationHistoryByVisit(Long.parseLong(visitId),Long.parseLong(
	 * itemIdsValArrayValues[i]),Long.parseLong( patientId)); } Date
	 * immunizationDate =null; if(immnunizationDateValue!=null &&
	 * immnunizationDateValue.length>0 && immnunizationDateValue[i]!=null &&
	 * StringUtils.isNotEmpty(immnunizationDateValue[i].trim()) &&
	 * !immnunizationDateValue[i].trim().equalsIgnoreCase("0")) immunizationDate =
	 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[i]);
	 * if(StringUtils.isNotEmpty(itemIdsValArrayValues[i].trim())) {
	 * patientImmunizationHistory=
	 * medicalExamDAO.getPatientImmunizationHistoryByDate(Long.parseLong(visitId.
	 * trim()),Long.parseLong(itemIdsValArrayValues[i].trim()),Long.parseLong(
	 * patientId),immunizationDate,"dateNotNull") ;
	 * 
	 * if(patientImmunizationHistory==null && immunizationDate==null) {
	 * patientImmunizationHistory=
	 * medicalExamDAO.getPatientImmunizationHistory(Long.parseLong(visitId.trim()),
	 * Long.parseLong(itemIdsValArrayValues[i].trim())); }
	 * if(patientImmunizationHistory==null) { if(immunisationIdValues[i]!=null &&
	 * !immunisationIdValues[i].trim().equalsIgnoreCase("0") &&
	 * !immunisationIdValues[i].trim().equalsIgnoreCase("undefined")) {
	 * PatientImmunizationHistory patientImmunizationHistoryTemp=medicalExamDAO.
	 * getPatientImmunizationHistoryByImmunizationId(Long.parseLong(
	 * immunisationIdValues[i].trim())); if(patientImmunizationHistoryTemp!=null &&
	 * patientImmunizationHistoryTemp.getImmunizationDate()==null &&
	 * patientImmunizationHistoryTemp.getNextDueDate()==null) {
	 * patientImmunizationHistory=patientImmunizationHistoryTemp; } } }
	 * 
	 * if(patientImmunizationHistory==null) { patientImmunizationHistory=new
	 * PatientImmunizationHistory();
	 * patientImmunizationHistory.setVisitId(Long.parseLong(visitId));
	 * patientImmunizationHistory.setPatientId(Long.parseLong( patientId)); }
	 * if(itemIdsValArrayValues[i]!=null &&
	 * !itemIdsValArrayValues[i].trim().equalsIgnoreCase("0"))
	 * patientImmunizationHistory.setItemId(Long.parseLong(itemIdsValArrayValues[i].
	 * trim()));
	 * 
	 * patientImmunizationHistory.setLastChgBy(Long.parseLong(userId.trim()));
	 * if(immnunizationDateValue!=null && immnunizationDateValue.length>0 &&
	 * immnunizationDateValue[i]!=null &&
	 * StringUtils.isNotEmpty(immnunizationDateValue[i].trim()) &&
	 * !immnunizationDateValue[i].trim().equalsIgnoreCase("0")) { immunizationDate =
	 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[i].trim());
	 * patientImmunizationHistory.setImmunizationDate(immunizationDate); }
	 * 
	 * if(durationImmuValues[i]!=null &&
	 * !durationImmuValues[i].trim().equalsIgnoreCase("0")) {
	 * patientImmunizationHistory.setDuration(durationImmuValues[i].trim()); }
	 * 
	 * if(nextDueImmuValue!=null && nextDueImmuValue.length>0 &&
	 * nextDueImmuValue[i]!=null &&
	 * StringUtils.isNotEmpty(nextDueImmuValue[i].trim()) &&
	 * !nextDueImmuValue[i].trim().equalsIgnoreCase("0")) { Date
	 * nextDueImmuValueDate =
	 * HMSUtil.convertStringTypeDateToDateType(nextDueImmuValue[i].trim());
	 * patientImmunizationHistory.setNextDueDate(nextDueImmuValueDate); }
	 * 
	 * 
	 * 
	 * if(presceriptionDateValue!=null && presceriptionDateValue.length>0 &&
	 * presceriptionDateValue[i]!=null &&
	 * StringUtils.isNotEmpty(immnunizationDateValue[i].trim()) &&
	 * !presceriptionDateValue[i].trim().equalsIgnoreCase("0")) { immunizationDate =
	 * HMSUtil.convertStringTypeDateToDateType(presceriptionDateValue[i].trim());
	 * patientImmunizationHistory.setPrescriptionDate(immunizationDate); } Date
	 * date=new Date(); patientImmunizationHistory.setLastChgDate(new
	 * Timestamp(date.getTime()));
	 * patientImmunizationHistory.setLastChgBy(Long.parseLong(userId));
	 * medicalExamDAO.saveOrUpdatePatientImmunizationHistoryComm(
	 * patientImmunizationHistory,session); checkCount++; } } tx.commit();
	 * if(checkCount>0) { json.put("status", 1); json.put("message",
	 * "Data Updated Successfully"); } else { json.put("status", 0);
	 * json.put("message", "Something is wrong"); } } catch(Exception e) { if (tx !=
	 * null) { try { tx.rollback(); } catch (Exception re) { re.printStackTrace(); }
	 * } e.printStackTrace(); json.put("status", 0); json.put("message",
	 * "Something is wrong"); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * json.toString(); }
	 * 
	 * @Override public String saveItemCommon(HashMap<String, Object> map) {
	 * 
	 * JSONObject json = new JSONObject(); int checkCount=0; Session session=null ;
	 * Transaction tx=null; try { session=
	 * getHibernateUtils.getHibernateUtlis().OpenSession();
	 * tx=session.beginTransaction(); String addItem ="";
	 * if(map.get("addItem")!=null) { addItem = map.get("addItem").toString();
	 * addItem = OpdServiceImpl.getReplaceString(addItem); }
	 * 
	 * String globalFlag =""; if(map.get("tableFlag")!=null) { globalFlag =
	 * map.get("tableFlag").toString(); globalFlag =
	 * OpdServiceImpl.getReplaceString(globalFlag); }
	 * 
	 * String userIdCommon =""; if(map.get("userIdCommon")!=null) { userIdCommon =
	 * map.get("userIdCommon").toString(); userIdCommon =
	 * OpdServiceImpl.getReplaceString(userIdCommon); } String addItemCode ="";
	 * if(map.get("addItemCode")!=null) { addItemCode =
	 * map.get("addItemCode").toString(); addItemCode =
	 * OpdServiceImpl.getReplaceString(addItemCode); } Object object=null;
	 * if(globalFlag!="" && (globalFlag.equalsIgnoreCase("dignosis") ||
	 * globalFlag.equalsIgnoreCase("dignosisSHO") ||
	 * globalFlag.equalsIgnoreCase("diagnosisForReferal") ||
	 * globalFlag.equalsIgnoreCase("diagnosisMe") ||
	 * globalFlag.equalsIgnoreCase("diagnosisMe18") ||
	 * globalFlag.equalsIgnoreCase("diagnosismi")
	 * ||globalFlag.equalsIgnoreCase("icd_dignosis"))){ MasIcd masIcd=new MasIcd();
	 * masIcd.setIcdName(addItem); masIcd.setLastChgDate(new Date());
	 * if(StringUtils.isNotEmpty(userIdCommon))
	 * masIcd.setLastChgBy(Long.parseLong(userIdCommon)); Date d=new Date();
	 * Timestamp date = new Timestamp(d.getTime()); masIcd.setLastChgDate(date);
	 * masIcd.setStatus("Y"); masIcd.setIcdCode(addItemCode); object=masIcd; }
	 * if(object!=null) session.save(object); checkCount=1; tx.commit();
	 * if(checkCount>0) { json.put("status", 1); json.put("message",
	 * "Data Updated Successfully"); } else { json.put("status", 0);
	 * json.put("message", "Something is wrong"); } } catch(Exception e) { if (tx !=
	 * null) { try { tx.rollback(); } catch (Exception re) { re.printStackTrace(); }
	 * } e.printStackTrace(); json.put("status", 0); json.put("message",
	 * "Something is wrong"); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * json.toString();
	 * 
	 * }
	 */

}
