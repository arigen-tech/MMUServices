package com.mmu.services.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.RadiologyDao;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.Users;
import com.mmu.services.service.RadiologyService;
import com.mmu.services.utils.HMSUtil;

@Repository
public class RadiologyServiceImpl implements RadiologyService{
	
	@Autowired
	RadiologyDao radiologyDao;


	/*
	 * @SuppressWarnings({ "deprecation", "unchecked" })
	 * 
	 * @Override public String getResultValidation(HashMap<String, Object>
	 * inputJson) { JSONObject obj = new JSONObject(); List<Map<String,Object>>
	 * resultValidationList = new ArrayList<>(); try { Map<String,Object> map =
	 * radiologyDao.getResultValidation(inputJson); if(MapUtils.isEmpty(map)) {
	 * obj.put("resultValidationData", resultValidationList); }else {
	 * List<DgResultEntryDt> resultList = (List<DgResultEntryDt>) map.get("list");
	 * obj.put("docName", HMSUtil.convertNullToEmptyString(map.get("docName")));
	 * obj.put("dId", HMSUtil.convertNullToEmptyString(map.get("dId")));
	 * obj.put("resultEntry",map.get("html1")); obj.put("dFormat",
	 * HMSUtil.convertNullToEmptyString(map.get("dFormat")));
	 * if(resultList.isEmpty()) { obj.put("resultValidationData",
	 * resultValidationList); }else { Users userData = (Users) map.get("userData");
	 * String resultDate="", enteredBy="", serviceNo="", patientName="",
	 * relation="", empName="",age="",gender="", investigationName = "",
	 * enteredByRank="", modality=""; for(DgResultEntryDt dgResultEntryDt :
	 * resultList) { Map<String,Object> jsonData = new HashMap<>();
	 * if(dgResultEntryDt.getDgResultEntryHd().getResultDate() != null) { resultDate
	 * = HMSUtil.changeDateToddMMyyyy(dgResultEntryDt.getDgResultEntryHd().
	 * getResultDate()); } if(dgResultEntryDt.getDgResultEntryHd().getUsers() !=
	 * null) { enteredBy =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getUsers().getFirstName()); String enteredByServiceNo =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getUsers().getServiceNo()); enteredByRank =
	 * radiologyDao.getRankUsingServiceNo(enteredByServiceNo); }
	 * 
	 * if(dgResultEntryDt.getDgResultEntryHd().getPatient() != null) { serviceNo =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getPatient().getServiceNo()); patientName =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getPatient().getPatientName()); empName =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getPatient().getEmployeeName());
	 * if(dgResultEntryDt.getDgResultEntryHd().getPatient().getMasAdministrativeSex(
	 * ) != null) { gender =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getPatient().getMasAdministrativeSex().getAdministrativeSexName()); }
	 * if(dgResultEntryDt.getDgResultEntryHd().getPatient().getDateOfBirth() !=
	 * null) { Date date =
	 * dgResultEntryDt.getDgResultEntryHd().getPatient().getDateOfBirth(); age =
	 * HMSUtil.calculateAge(date); }
	 * if(dgResultEntryDt.getDgResultEntryHd().getPatient().getMasRelation() !=
	 * null) { relation =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getPatient().getMasRelation().getRelationName()); } }
	 * if(dgResultEntryDt.getDgMasInvestigation() != null) {
	 * if(dgResultEntryDt.getDgMasInvestigation().getMasSubChargecode() != null) {
	 * modality =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgMasInvestigation().
	 * getMasSubChargecode().getSubChargecodeName()); } investigationName =
	 * HMSUtil.convertNullToEmptyString(
	 * dgResultEntryDt.getDgMasInvestigation().getInvestigationName()); } String
	 * verifiedBy = "", virifiedByRank="", ridcId = ""; ridcId =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getRidcId()); if(userData !=
	 * null) { verifiedBy =
	 * HMSUtil.convertNullToEmptyString(userData.getFirstName()); String
	 * verifiedByServiceNo =
	 * HMSUtil.convertNullToEmptyString(userData.getServiceNo()); virifiedByRank =
	 * radiologyDao.getRankUsingServiceNo(verifiedByServiceNo); }
	 * jsonData.put("verifiedBy", verifiedBy); jsonData.put("virifiedByRank",
	 * virifiedByRank); jsonData.put("resultDtId",
	 * dgResultEntryDt.getResultEntryDetailId()); jsonData.put("resultHdId",
	 * dgResultEntryDt.getDgResultEntryHd().getResultEntryId());
	 * jsonData.put("resultDate", resultDate); jsonData.put("enteredBy", enteredBy);
	 * jsonData.put("enteredByRank", enteredByRank); jsonData.put("serviceNo",
	 * serviceNo); jsonData.put("patientName", patientName); jsonData.put("empName",
	 * empName); jsonData.put("gender", gender); jsonData.put("age", age);
	 * jsonData.put("relation", relation); jsonData.put("investigationName",
	 * investigationName); jsonData.put("modality", modality);
	 * jsonData.put("ridcId", ridcId); jsonData.put("remarks",
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getRemarks()));
	 * jsonData.put("status",
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getResultDetailStatus()));
	 * //jsonData.put("resultEntry", HMSUtil.convertNullToEmptyString(result));
	 * //jsonData.put("validatedBy", ""); resultValidationList.add(jsonData); }
	 * obj.put("resultValidationData", resultValidationList); } } }catch(Exception
	 * ex) { ex.printStackTrace(); } return obj.toString(); }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public String getResultPrintingData(HashMap<String, Object>
	 * jsonData) { JSONObject obj = new JSONObject(); Map<String,Object> data =
	 * radiologyDao.getResultPrintingData(jsonData); List<Map<String,Object>>
	 * validationList = new ArrayList<>(); if(MapUtils.isEmpty(data)) {
	 * obj.put("count", 0); obj.put("validationList", validationList); }else {
	 * List<DgResultEntryDt> list = (List<DgResultEntryDt>) data.get("list"); int
	 * count = Integer.parseInt(String.valueOf(data.get("count"))); if
	 * (list.isEmpty()) { obj.put("count", 0); obj.put("validationList",
	 * validationList); }else { String date = "", serviceNo = "", patientName = "",
	 * age = "", gender = "", relation = "", investigation = "", enteredBy = "",
	 * modality = "", enteredByRank="",dgOrderHdId="",
	 * validatedByRank="",validatedBy=""; for (DgResultEntryDt dgResultEntryDt :
	 * list) { Map<String, Object> json = new HashMap<>(); if
	 * (dgResultEntryDt.getDgResultEntryHd().getResultDate() != null) { date =
	 * HMSUtil.changeDateToddMMyyyy(dgResultEntryDt.getDgResultEntryHd().
	 * getResultDate()); } if (dgResultEntryDt.getDgResultEntryHd().getPatient() !=
	 * null) { serviceNo = HMSUtil.convertNullToEmptyString(
	 * dgResultEntryDt.getDgResultEntryHd().getPatient().getServiceNo());
	 * patientName = HMSUtil.convertNullToEmptyString(
	 * dgResultEntryDt.getDgResultEntryHd().getPatient().getPatientName()); if
	 * (dgResultEntryDt.getDgResultEntryHd().getPatient().getDateOfBirth() != null)
	 * { Date dob =
	 * dgResultEntryDt.getDgResultEntryHd().getPatient().getDateOfBirth(); age =
	 * HMSUtil.calculateAge(dob); } if
	 * (dgResultEntryDt.getDgResultEntryHd().getPatient() .getMasAdministrativeSex()
	 * != null) { gender =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getPatient().getMasAdministrativeSex().getAdministrativeSexName()); } if
	 * (dgResultEntryDt.getDgResultEntryHd().getPatient().getMasRelation() != null)
	 * { relation =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getPatient().getMasrelation().getRelationName()); }
	 * 
	 * } if (dgResultEntryDt.getDgMasInvestigation() != null) { investigation =
	 * HMSUtil.convertNullToEmptyString(
	 * dgResultEntryDt.getDgMasInvestigation().getInvestigationName()); if
	 * (dgResultEntryDt.getDgMasInvestigation() .getMasSubChargecode() != null) {
	 * modality =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgMasInvestigation().
	 * getMasSubChargecode().getSubChargecodeName()); } }
	 * if(dgResultEntryDt.getDgResultEntryHd().getUsers() != null) { enteredBy =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getUsers().getFirstName()); String enteredByServiceNo =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getUsers().getServiceNo()); enteredByRank =
	 * radiologyDao.getRankUsingServiceNo(enteredByServiceNo); }
	 * 
	 * 
	 * if(dgResultEntryDt.getDgResultEntryHd().getResultVerified() != null) {
	 * validatedBy =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getResultVerified().getFirstName()); String validatedByServiceNo =
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getResultVerified().getServiceNo()); validatedByRank =
	 * radiologyDao.getRankUsingServiceNo(validatedByServiceNo); }
	 * 
	 * if(dgResultEntryDt.getDgResultEntryHd().getDgOrderhd() != null) { dgOrderHdId
	 * = HMSUtil.convertNullToEmptyString(dgResultEntryDt.getDgResultEntryHd().
	 * getDgOrderhd().getOrderhdId()); }
	 * 
	 * json.put("resultDtId", dgResultEntryDt.getResultEntryDetailId());
	 * json.put("resultHdId",
	 * dgResultEntryDt.getDgResultEntryHd().getResultEntryId()); json.put("date",
	 * date); json.put("serviceNo", serviceNo); json.put("patientName",
	 * patientName); json.put("age", age); json.put("gender", gender);
	 * json.put("relation", relation); json.put("investigation", investigation);
	 * json.put("modality", modality); json.put("enteredBy", enteredBy);
	 * json.put("enteredByRank", enteredByRank); json.put("validatedBy",
	 * validatedBy); json.put("validatedByRank", validatedByRank);
	 * json.put("dgOrderHdId", dgOrderHdId); json.put("status",
	 * HMSUtil.convertNullToEmptyString(dgResultEntryDt.getResultDetailStatus()));
	 * validationList.add(json); } } obj.put("count", count);
	 * obj.put("validationList", validationList); } return obj.toString(); }
	 * 
	 * 
	 * @Override public String saveDocumentData(HashMap<String, Object> inputJson) {
	 * JSONObject obj = new JSONObject(); Map<String,Object> map =
	 * radiologyDao.saveDocumentData(inputJson); String result =
	 * String.valueOf(map.get("result")); String ridcId =
	 * String.valueOf(map.get("ridcId")); obj.put("result", result);
	 * obj.put("ridcId", ridcId); return obj.toString(); }
	 */
}
