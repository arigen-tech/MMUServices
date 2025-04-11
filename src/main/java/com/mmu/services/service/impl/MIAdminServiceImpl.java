package com.mmu.services.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.DispensaryDao;
import com.mmu.services.dao.MIAdminDao;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.dao.StoresDAO;
import com.mmu.services.entity.AppAuditEquipmentDt;
import com.mmu.services.entity.AppEquipmentAccessory;
import com.mmu.services.entity.AppEquipmentDt;
import com.mmu.services.entity.AppEquipmentHd;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.EmployeeBloodGroup;
import com.mmu.services.entity.HivStdRegister;
import com.mmu.services.entity.HospitalVisitRegister;
import com.mmu.services.entity.InjuryRegister;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasBloodGroup;
import com.mmu.services.entity.MasDepartment;

import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasEmployeeCategory;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRegistrationType;
import com.mmu.services.entity.MasServiceType;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.MilkTestingRegister;
import com.mmu.services.entity.OpdDisposalDetail;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientImmunizationHistory;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.SanitaryDiary;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.VisitorDetail;
import com.mmu.services.entity.VuPatientMedicalCat;
import com.mmu.services.entity.WaterTestingRegister;
import com.mmu.services.service.MIAdminService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.UtilityServices;

@Service("MIAdminService")
public class MIAdminServiceImpl implements MIAdminService {
	@Autowired
	DispensaryServiceImpl dispenceryService;

	@Autowired
	MIAdminDao mIAdminDao;
	@Autowired
	StoresDAO storesDAO;
	@Autowired
	DispensaryDao dispensaryDao;

	@Autowired
	PatientRegistrationDao patientRegistrationDao;

	/*
	 * @Override public Map<String, Object> getAllDisposalTypeList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<OpdDisposalDetail>
	 * opdPatientDetailList = new ArrayList<OpdDisposalDetail>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); List<Object[]> objectArrayList =
	 * new ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getDisposalTypeList(pageNo,requestData); int count =
	 * 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) { objectArrayList =
	 * (List<Object[]>) responseMap.get("opdPatientDetailList"); if
	 * (!objectArrayList.isEmpty() && objectArrayList.size() > 0) { for( Object[]
	 * data1 : objectArrayList) { Long disposalId =
	 * Long.parseLong(data1[0].toString()); String disposalName =
	 * data1[1].toString(); Long c = Long.parseLong(data1[2].toString()); String
	 * hospitalName = data1[3].toString(); Map<String,Object>map1=new
	 * HashMap<String, Object>(); map1.put("disposalName", disposalName);
	 * map1.put("hospitalName", hospitalName); map1.put("count", c);
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map;
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getAllAttCTypeReportList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<OpdDisposalDetail>
	 * opdPatientDetailList = new ArrayList<OpdDisposalDetail>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); List<Object[]> objectArrayList =
	 * new ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * String attcCode = HMSUtil.getProperties("adt.properties",
	 * "ATTC_CODE").trim(); String siqCode = HMSUtil.getProperties("adt.properties",
	 * "SIQ_CODE").trim(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); long pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * //String category = requestData.get("category").toString();
	 * 
	 * responseMap = mIAdminDao.getAllDisposalTypeList(pageNo,requestData); int
	 * count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * opdPatientDetailList = (List<OpdDisposalDetail>)
	 * responseMap.get("opdPatientDetailList"); if (!opdPatientDetailList.isEmpty()
	 * && opdPatientDetailList.size() > 0) { for(OpdDisposalDetail opdDisposalDetail
	 * : opdPatientDetailList) { String diagnosis=""; Map<String,Object>map1=new
	 * HashMap<String, Object>(); map1.put("name",
	 * opdDisposalDetail.getPatient().getPatientName()); map1.put("serviceNo",
	 * opdDisposalDetail.getPatient().getServiceNo());
	 * map1.put("rank",opdDisposalDetail.getPatient().getMasRank().getRankName());
	 * map1.put("hospitalName",
	 * opdDisposalDetail.getVisit().getMasHospital().getHospitalName());
	 * List<DischargeIcdCode> dischargeIcdCodeList =
	 * opdDisposalDetail.getVisit().getOpdPatientDetails().getDischargeIcdCode();
	 * for(DischargeIcdCode dischargeIcdCode : dischargeIcdCodeList) { diagnosis =
	 * diagnosis+","+dischargeIcdCode.getMasIcd().getIcdName(); } diagnosis =
	 * diagnosis.substring(1); map1.put("diagnosis", diagnosis);
	 * map1.put("startDate",HMSUtil.changeDateToddMMyyyy(opdDisposalDetail.getVisit(
	 * ).getVisitDate()));
	 * if(opdDisposalDetail.getMasDisposal().getDisposalCode().equalsIgnoreCase(
	 * attcCode)) { if(opdDisposalDetail.getDisposalDays()!=null &&
	 * opdDisposalDetail.getDisposalDays()==1)
	 * map1.put("noOfDays",opdDisposalDetail.getDisposalDays()+"   day"); else
	 * if(opdDisposalDetail.getDisposalDays()!=null &&
	 * opdDisposalDetail.getDisposalDays()!=1)
	 * map1.put("noOfDays",opdDisposalDetail.getDisposalDays()+"   days"); else
	 * map1.put("noOfDays",""); }else if(opdDisposalDetail.getDisposalDays()!=null)
	 * map1.put("noOfDays",opdDisposalDetail.getDisposalDays()*24+"   hours"); else
	 * map1.put("noOfDays","");
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public Map<String, Object> getEmployeeCategory() { // TODO
	 * Auto-generated method stub Map<String, Object> map1 = new HashMap<String,
	 * Object>();
	 * 
	 * Map<Long, String> map = new HashMap<Long, String>();
	 * List<MasEmployeeCategory> empCategoryList = new
	 * ArrayList<MasEmployeeCategory>();
	 * 
	 * empCategoryList = mIAdminDao.getEmpCategoryList(); for (MasEmployeeCategory
	 * masEmployeeCategory : empCategoryList) {
	 * map.put(masEmployeeCategory.getEmployeeCategoryId(),
	 * masEmployeeCategory.getEmployeeCategoryName()); } if (empCategoryList != null
	 * && empCategoryList.size() > 0) { map1.put("data", map); map1.put("status",
	 * 1); } else { map1.put("data", map); map1.put("msg", "Data not found");
	 * map1.put("status", 0); }
	 * 
	 * return map1; }
	 * 
	 * @Override public Map<String, Object> getMedCategory() { // TODO
	 * Auto-generated method stub Map<String, Object> map1 = new HashMap<String,
	 * Object>();
	 * 
	 * Map<Long, String> map = new HashMap<Long, String>(); List<MasMedicalCategory>
	 * masMedicalCategoryList = new ArrayList<MasMedicalCategory>();
	 * 
	 * masMedicalCategoryList = mIAdminDao.getMedCategoryList(); for
	 * (MasMedicalCategory masMedicalCategory : masMedicalCategoryList) {
	 * map.put(masMedicalCategory.getMedicalCategoryId(),
	 * masMedicalCategory.getMedicalCategoryName()); } if (masMedicalCategoryList !=
	 * null && masMedicalCategoryList.size() > 0) { map1.put("data", map);
	 * map1.put("status", 1); } else { map1.put("data", map); map1.put("msg",
	 * "Data not found"); map1.put("status", 0); }
	 * 
	 * return map1; }
	 * 
	 * @Override public Map<String, Object> getAllAReportList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<MasMedicalExamReport>
	 * masMedicalExamReportList = new ArrayList<MasMedicalExamReport>();
	 * Map<String,Object> responseMap=new HashMap<String, Object>(); List<Object[]>
	 * objectArrayList = new ArrayList<>(); List<Object> responseList = new
	 * ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * long medcategory = Long.parseLong(requestData.get("medcategory").toString());
	 * String bp="";
	 * 
	 * responseMap = mIAdminDao.getAmeReportList(pageNo,requestData); int count = 1;
	 * if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * masMedicalExamReportList = (List<MasMedicalExamReport>)
	 * responseMap.get("masMedicalExamReportList"); if
	 * (!masMedicalExamReportList.isEmpty() && masMedicalExamReportList.size() > 0)
	 * { for(MasMedicalExamReport masMedicalExamReport : masMedicalExamReportList) {
	 * String medicalCategorylist=""; List<VuPatientMedicalCat>
	 * patientMedicalCatList;
	 * patientMedicalCatList=masMedicalExamReport.getVisit().getVuPatientMedicalCat(
	 * ); for (VuPatientMedicalCat patientMedicalCat : patientMedicalCatList) {
	 * if(patientMedicalCat.getMasMedicalCategory()!=null && medcategory==0)
	 * medicalCategorylist=medicalCategorylist+","+patientMedicalCat.
	 * getMasMedicalCategory().getMedicalCategoryName(); else
	 * if(patientMedicalCat.getMasMedicalCategory()!=null &&
	 * patientMedicalCat.getMasMedicalCategory().getMedicalCategoryId()==
	 * medcategory) { medicalCategorylist=medicalCategorylist+","+patientMedicalCat.
	 * getMasMedicalCategory().getMedicalCategoryName();
	 * 
	 * } } if(medcategory!=0 && medicalCategorylist.equalsIgnoreCase("")) { break; }
	 * String diagnosis=""; Map<String,Object>map1=new HashMap<String, Object>();
	 * map1.put("name",
	 * masMedicalExamReport.getVisit().getPatient().getPatientName());
	 * map1.put("serviceNo",
	 * masMedicalExamReport.getVisit().getPatient().getServiceNo());
	 * map1.put("rank",masMedicalExamReport.getVisit().getPatient().getMasRank().
	 * getRankName());
	 * map1.put("hospitalName",masMedicalExamReport.getVisit().getMasHospital().
	 * getHospitalName());
	 * map1.put("date",HMSUtil.changeDateToddMMyyyy(masMedicalExamReport.getMoDate()
	 * )); map1.put("age",HMSUtil.calculateAge(masMedicalExamReport.getVisit().
	 * getPatient().getDateOfBirth())); if(masMedicalExamReport.getHeight()!=null)
	 * map1.put("height", masMedicalExamReport.getHeight()); else map1.put("height",
	 * ""); if(masMedicalExamReport.getWeight()!=null) map1.put("weight",
	 * masMedicalExamReport.getWeight()); else map1.put("weight", "");
	 * if(masMedicalExamReport.getWeight()!=null) map1.put("idealWeight",
	 * masMedicalExamReport.getIdealweight()); else map1.put("idealWeight", "");
	 * if(masMedicalExamReport.getChestfullexpansion()!=null) map1.put("chest",
	 * masMedicalExamReport.getChestfullexpansion()); else map1.put("chest", "");
	 * if(masMedicalExamReport.getWaist()!=null) map1.put("waist",
	 * masMedicalExamReport.getWaist()); else map1.put("waist", "");
	 * if(masMedicalExamReport.getPulseRates()!=null) map1.put("pulse",
	 * masMedicalExamReport.getPulseRates()); else map1.put("pulse", ""); if(
	 * masMedicalExamReport.getBpDiastolic()!=null &&
	 * masMedicalExamReport.getBpSystolic()!=null) map1.put("bp",
	 * masMedicalExamReport.getBpSystolic()+"/"+masMedicalExamReport.getBpDiastolic(
	 * )); else if( masMedicalExamReport.getBpDiastolic()!=null &&
	 * masMedicalExamReport.getBpSystolic()==null) map1.put("bp",
	 * masMedicalExamReport.getBpDiastolic()); else if(
	 * masMedicalExamReport.getBpDiastolic()==null &&
	 * masMedicalExamReport.getBpSystolic()!=null) map1.put("bp",
	 * masMedicalExamReport.getBpSystolic()); else map1.put("bp", "");
	 * 
	 * String pvmsNo = UtilityServices.getValueOfPropByKey("pvmsNo"); String[]
	 * pvmsNoValue=null; if (StringUtils.isNotEmpty(pvmsNo)) { pvmsNoValue =
	 * pvmsNo.split(","); } List<MasStoreItem>
	 * listMasStoreItem=storesDAO.getStoreItemListForAutoComplete(pvmsNoValue[1],
	 * "",""); Long tabItemId=listMasStoreItem.get(0).getItemId();
	 * 
	 * List<MasStoreItem>
	 * listMasStoreItemtt=storesDAO.getStoreItemListForAutoComplete(pvmsNoValue[0],
	 * "",""); Long ttItemId=listMasStoreItemtt.get(0).getItemId(); Date
	 * tabDate=null; Date ttDate=null; List<PatientImmunizationHistory>
	 * patientImmunizationHistoryList =
	 * masMedicalExamReport.getVisit().getPatientImmunizationHistory();
	 * for(PatientImmunizationHistory patientImmunizationHistory :
	 * patientImmunizationHistoryList) {
	 * if(patientImmunizationHistory.getItemId().intValue()==tabItemId.intValue()) {
	 * tabDate=patientImmunizationHistory.getImmunizationDate(); }
	 * 
	 * if(patientImmunizationHistory.getItemId().intValue()==ttItemId.intValue()) {
	 * ttDate=patientImmunizationHistory.getImmunizationDate(); } }
	 * if(tabDate!=null) map1.put("tabDate", HMSUtil.changeDateToddMMyyyy(tabDate));
	 * else map1.put("tabDate", ""); if(ttDate!=null) map1.put("ttDate",
	 * HMSUtil.changeDateToddMMyyyy(ttDate)); else map1.put("ttDate", ""); if(
	 * masMedicalExamReport.getPetStatusOn()!=null) map1.put("dv",
	 * masMedicalExamReport.getPetStatusOn());//need to change else map1.put("dv",
	 * ""); if( masMedicalExamReport.getPetStatusOn()!=null) map1.put("nv",
	 * masMedicalExamReport.getPetStatusOn());//need to change else map1.put("nv",
	 * ""); if( masMedicalExamReport.getPetStatusOn()!=null) map1.put("pet",
	 * masMedicalExamReport.getPetStatusOn()); else map1.put("pet", "");
	 * 
	 * 
	 * if(masMedicalExamReport.getVisit().getPatientMedicalCat()!=null) {
	 * 
	 * if(!medicalCategorylist.isEmpty() &&
	 * !medicalCategorylist.equalsIgnoreCase("")) medicalCategorylist =
	 * medicalCategorylist.substring(1);
	 * map1.put("medicalCategory",medicalCategorylist); }else
	 * map1.put("medicalCategory", "");
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public Map<String, Object> masMedicalStatisticsList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<OpdPatientDetail>
	 * opdPatientDetailList = new ArrayList<OpdPatientDetail>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); List<Object[]> objectArrayList =
	 * new ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getMedicalStatistic(pageNo,requestData); int count =
	 * 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) { objectArrayList =
	 * (List<Object[]>) responseMap.get("opdPatientDetailList"); if
	 * (!objectArrayList.isEmpty() && objectArrayList.size() > 0) { for( Object[]
	 * data1 : objectArrayList) { //Long disposalId =
	 * Long.parseLong(data1[0].toString()); String medExamCategory =
	 * data1[0].toString(); Long c = Long.parseLong(data1[1].toString());
	 * Map<String,Object>map1=new HashMap<String, Object>();
	 * map1.put("medExamCategory", medExamCategory); map1.put("count", c);
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public Map<String, Object> getBloodGroup() { // TODO Auto-generated
	 * method stub Map<String, Object> map1 = new HashMap<String, Object>();
	 * 
	 * Map<String, Long> map = new LinkedHashMap<String, Long>();
	 * List<MasBloodGroup> masBloodGroupList = new ArrayList<MasBloodGroup>();
	 * 
	 * masBloodGroupList = mIAdminDao.getBloodGroupList(); for (MasBloodGroup
	 * masBloodGroup : masBloodGroupList) {
	 * map.put(masBloodGroup.getBloodGroupName(), masBloodGroup.getBloodGroupId());
	 * } if (masBloodGroupList != null && masBloodGroupList.size() > 0) {
	 * map1.put("data", map); map1.put("status", 1); } else { map1.put("data", map);
	 * map1.put("msg", "Data not found"); map1.put("status", 0); }
	 * 
	 * return map1; }
	 * 
	 * @Override public Map<String, Object> getBloodGroupRegister(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { List<EmployeeBloodGroup> patientDetailList = new
	 * ArrayList<EmployeeBloodGroup>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getSimilarBloodGroupPatient(pageNo,requestData); int
	 * count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * patientDetailList = (List<EmployeeBloodGroup>)
	 * responseMap.get("patientList"); if (!patientDetailList.isEmpty() &&
	 * patientDetailList.size() > 0) { for(EmployeeBloodGroup emp :
	 * patientDetailList) { String address=""; MasUnit masUnit=
	 * mIAdminDao.getMasUnitById(emp.getUnitId()); MasRank masRank=
	 * mIAdminDao.getMasRankById(emp.getRankId()); Map<String,Object>map1=new
	 * HashMap<String, Object>(); if(emp.getEmployeeName()!=null)
	 * map1.put("name",emp.getEmployeeName()); else map1.put("name", "");
	 * if(emp.getServiceNo()!=null)
	 * 
	 * map1.put("serviceNo", emp.getServiceNo()); else map1.put("serviceNo", "");
	 * if(masRank!=null) map1.put("rank",masRank.getRankName()); else
	 * map1.put("rank", ""); if(masUnit!=null) map1.put("unit",
	 * masUnit.getUnitName()); else map1.put("unit", "");
	 * if(emp.getMasBloodGroup()!=null) map1.put("blood",
	 * emp.getMasBloodGroup().getBloodGroupName()); else map1.put("blood", "");
	 * if(emp.getMasAdministrativeSex()!=null) map1.put("gender",
	 * emp.getMasAdministrativeSex().getAdministrativeSexName()); else
	 * map1.put("gender", "");
	 * 
	 * if(emp.getDateOfBirth()!=null)
	 * map1.put("age",HMSUtil.calculateAge(emp.getDateOfBirth())); else
	 * map1.put("age",""); if(emp.getMobileNo()!=null) map1.put("contactNo",
	 * emp.getMobileNo()); else map1.put("contactNo", "");
	 * if(emp.getAddress()!=null) map1.put("address",emp.getAddress()); else
	 * map1.put("address",""); data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public Map<String, Object> getUnitList() { // TODO Auto-generated
	 * method stub Map<String, Object> map1 = new LinkedHashMap<String, Object>();
	 * 
	 * Map<String,Long> map = new LinkedHashMap<String,Long>(); List<MasUnit>
	 * masUnitList = new ArrayList<MasUnit>();
	 * 
	 * masUnitList = mIAdminDao.getMasUnitList(); for (MasUnit masUnit :
	 * masUnitList) { map.put( masUnit.getUnitName(), masUnit.getUnitId()); } if
	 * (masUnitList != null && masUnitList.size() > 0) { map1.put("data", map);
	 * map1.put("status", 1); } else { map1.put("data", map); map1.put("msg",
	 * "Data not found"); map1.put("status", 0); }
	 * 
	 * return map1; }
	 * 
	 * @Override public String submitSanitary(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); JSONObject
	 * json = new JSONObject(jsondata); SanitaryDiary sanitaryDiary = new
	 * SanitaryDiary();
	 * 
	 * MasHospital masHospital = null; MasDepartment masDepartment = null;
	 * 
	 * if (jsondata != null) {
	 * if(!jsondata.get("sanitaryDate").toString().isEmpty()) { String sanitaryDate
	 * = jsondata.get("sanitaryDate").toString(); String sanitaryDate1 =
	 * dispenceryService.getReplaceString(sanitaryDate); Date sanitaryDate2 =
	 * HMSUtil.convertStringDateToUtilDate(sanitaryDate1, "dd/MM/yyyy");
	 * sanitaryDiary.setDiaryDate(new Timestamp(sanitaryDate2.getTime())); }
	 * 
	 * if (jsondata.get("hospital") != null) { String hospitalId =
	 * jsondata.get("hospital").toString(); String hospitalId1 =
	 * dispenceryService.getReplaceString(hospitalId); masHospital =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId1)); }
	 * 
	 * String hospitalId=""; if (jsondata.get("hospitalId") != null) { hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); } if
	 * (jsondata.get("area") != null) { String area =
	 * jsondata.get("area").toString(); String area1 =
	 * dispenceryService.getReplaceString(area); sanitaryDiary.setArea(area1); }
	 * 
	 * if (jsondata.get("pmoObse") != null) { String pmoObse =
	 * jsondata.get("pmoObse").toString(); String pmoObse1 =
	 * dispenceryService.getReplaceString(pmoObse);
	 * sanitaryDiary.setPmoObservation(pmoObse1); } if (jsondata.get("pmoReco") !=
	 * null) { String pmoReco = jsondata.get("pmoReco").toString(); String pmoReco1
	 * = dispenceryService.getReplaceString(pmoReco);
	 * sanitaryDiary.setPmoRecommendation(pmoReco1); } if (jsondata.get("action") !=
	 * null) { String action = jsondata.get("action").toString(); String action1 =
	 * dispenceryService.getReplaceString(action);
	 * sanitaryDiary.setTakenAction(action1); } if (jsondata.get("followUp") !=
	 * null) { String followUp = jsondata.get("followUp").toString(); String
	 * followUp1 = dispenceryService.getReplaceString(followUp);
	 * sanitaryDiary.setTakenFollow(followUp1); } if (jsondata.get("exoRemark") !=
	 * null) { String exoRemark = jsondata.get("exoRemark").toString(); String
	 * exoRemark1 = dispenceryService.getReplaceString(exoRemark);
	 * sanitaryDiary.setExoRemarks(exoRemark1); } if (jsondata.get("coRemark") !=
	 * null) { String coRemark = jsondata.get("coRemark").toString(); String
	 * coRemark1 = dispenceryService.getReplaceString(coRemark);
	 * sanitaryDiary.setCoRemarks(coRemark1); } Date date = new Date(); Users
	 * users=null;
	 * 
	 * //save data in table if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); }
	 * sanitaryDiary.setLastChgBy(users); sanitaryDiary.setMasHospital(masHospital);
	 * sanitaryDiary.setLastChgDate(new Timestamp(date.getTime())); long
	 * sanitaryDiaryId = mIAdminDao.saveOrUpdate(sanitaryDiary);
	 * 
	 * if (sanitaryDiaryId != 0) { jsonObj.put("sanitaryId", sanitaryDiaryId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * } else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getSanitaryReportList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<SanitaryDiary>
	 * sanitaryDiaryList = new ArrayList<SanitaryDiary>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); List<Object[]> objectArrayList =
	 * new ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getSanitaryReportList(pageNo,requestData); int count
	 * = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * sanitaryDiaryList = (List<SanitaryDiary>)
	 * responseMap.get("sanitaryDiaryList"); if (!sanitaryDiaryList.isEmpty() &&
	 * sanitaryDiaryList.size() > 0) { for(SanitaryDiary sanitaryDiary :
	 * sanitaryDiaryList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * if(sanitaryDiary.getMasHospital().getHospitalName()!=null)
	 * map1.put("location", sanitaryDiary.getMasHospital().getHospitalName()); else
	 * map1.put("location",""); if(sanitaryDiary.getArea()!=null) map1.put("area",
	 * sanitaryDiary.getArea()); else map1.put("area","");
	 * if(sanitaryDiary.getPmoObservation()!=null)
	 * map1.put("pmoObservation",sanitaryDiary.getPmoObservation()); else
	 * map1.put("pmoObservation", "");
	 * if(sanitaryDiary.getPmoRecommendation()!=null)
	 * map1.put("pmoRecommendations",sanitaryDiary.getPmoRecommendation()); else
	 * map1.put("pmoRecommendations", ""); if(sanitaryDiary.getTakenAction()!=null)
	 * map1.put("actionTobeTaken",sanitaryDiary.getTakenAction()); else
	 * map1.put("actionTobeTaken",""); if(sanitaryDiary.getTakenFollow()!=null)
	 * map1.put("followUp", sanitaryDiary.getTakenFollow()); else
	 * map1.put("followUp", ""); if(sanitaryDiary.getExoRemarks()!=null)
	 * map1.put("exoRemark", sanitaryDiary.getExoRemarks()); else
	 * map1.put("exoRemark", ""); if(sanitaryDiary.getCoRemarks()!=null)
	 * map1.put("coRemark", sanitaryDiary.getCoRemarks()); else map1.put("coRemark",
	 * ""); if(sanitaryDiary.getDiaryDate()!=null) map1.put("date",
	 * HMSUtil.changeDateToddMMyyyy(sanitaryDiary.getDiaryDate())); else
	 * map1.put("date", ""); data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public Map<String, Object> getNameByServiceNo(String jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Patient patient=null; Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); JSONObject json = new
	 * JSONObject(jsondata); String serviceNo =
	 * json.get("memberService").toString(); String hospitalId =
	 * json.get("hospitalId").toString(); MasEmployee masEmployee=null; masEmployee=
	 * mIAdminDao.getEmpInfoByServiceNo(serviceNo,hospitalId); if(masEmployee!=null)
	 * { patient= mIAdminDao.getNameByServiceNo(serviceNo,hospitalId);
	 * if(patient!=null) { responseMap.put("name", patient.getPatientName());
	 * responseMap.put("empId", patient.getPatientId());
	 * responseMap.put("patientId", patient.getPatientId());
	 * if(patient.getDateOfBirth()!=null) { responseMap.put("age",HMSUtil.
	 * calculateAge(patient.getDateOfBirth()));
	 * responseMap.put("dob",HMSUtil.changeDateToddMMyyyy(patient.getDateOfBirth()))
	 * ; }else { responseMap.put("age",""); responseMap.put("dob", ""); }
	 * 
	 * if(patient.getMasAdministrativeSex()!=null) {
	 * responseMap.put("gender",patient.getMasAdministrativeSex().
	 * getAdministrativeSexName()); responseMap.put("genderId",
	 * patient.getMasAdministrativeSex().getAdministrativeSexId()); }else {
	 * responseMap.put("gender",""); responseMap.put("genderId", ""); }
	 * if(patient.getMasRank()!=null) { responseMap.put("rank",
	 * patient.getMasRank().getRankName()); responseMap.put("rankId",
	 * patient.getMasRank().getRankId()); } else { responseMap.put("rank","");
	 * responseMap.put("rankId", ""); } responseMap.put("msg", "success");
	 * responseMap.put("status", 1); }else {
	 * 
	 * MasUnit masUnit=null; MasRank masRank=null;
	 * 
	 * masEmployee= mIAdminDao.getEmpInfoByServiceNo(serviceNo,hospitalId);
	 * if(masEmployee!=null) {
	 * 
	 * masUnit= mIAdminDao.getMasUnit(masEmployee.getMasUnit()); masRank=
	 * mIAdminDao.getMasRank(masEmployee.getMasRank()); responseMap.put("name",
	 * masEmployee.getEmployeeName()); if(masEmployee.getDob()!=null) {
	 * responseMap.put("dob", HMSUtil.changeDateToddMMyyyy(masEmployee.getDob()));
	 * responseMap.put("age",HMSUtil.calculateAge(masEmployee.getDob())); } else {
	 * responseMap.put("dob", ""); responseMap.put("age",""); }
	 * 
	 * responseMap.put("empId", masEmployee.getEmployeeId());
	 * responseMap.put("patientId", ""); if(masRank!=null) { responseMap.put("rank",
	 * masRank.getRankName()); responseMap.put("rankId", masRank.getRankId()); }else
	 * { responseMap.put("rank", ""); responseMap.put("rankId",""); }
	 * 
	 * if(masEmployee.getMasAdministrativeSex()!=null) { responseMap.put("gender",
	 * masEmployee.getMasAdministrativeSex().getAdministrativeSexName());
	 * responseMap.put("genderId",
	 * masEmployee.getMasAdministrativeSex().getAdministrativeSexId()); }else {
	 * responseMap.put("gender", ""); responseMap.put("genderId", ""); }
	 * 
	 * 
	 * responseMap.put("msg", "success"); responseMap.put("status", 1);
	 * 
	 * } } }else { responseMap.put("status", 0); responseMap.put("msg",
	 * "Service number is not mapped with the unit"); } return responseMap; }
	 * 
	 * @Override public String submitInjuryRegister(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); JSONObject
	 * json = new JSONObject(jsondata); InjuryRegister injuryRegister = new
	 * InjuryRegister();
	 * 
	 * MasHospital masHospital = null; MasDepartment masDepartment = null;
	 * 
	 * if (jsondata != null) {
	 * 
	 * if(!jsondata.get("injuryDate").toString().isEmpty()) { String injuryDate =
	 * jsondata.get("injuryDate").toString(); String injuryDate1 =
	 * dispenceryService.getReplaceString(injuryDate); Date injuryDate2 =
	 * HMSUtil.convertStringDateToUtilDate(injuryDate1, "dd/MM/yyyy");
	 * injuryRegister.setInjuryDate(new Timestamp(injuryDate2.getTime())); }
	 * 
	 * MasHospital masHospital1 = null; String hospitalId=""; if
	 * (jsondata.get("hospitalId") != null) { hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital1 =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); } String
	 * icdId1=""; if (jsondata.get("diagnosisId") != null) { String icdId =
	 * jsondata.get("diagnosisId").toString(); // icdId1 =
	 * dispenceryService.getReplaceString(icdId); icdId1=icdId.substring(1,
	 * icdId.length()-1); } if (jsondata.get("letterNo") != null) { String letterNo
	 * = jsondata.get("letterNo").toString(); String letterNo1 =
	 * dispenceryService.getReplaceString(letterNo);
	 * injuryRegister.setLetterNo(letterNo1); } if (jsondata.get("letterDate") !=
	 * null) { String letterDate = jsondata.get("letterDate").toString(); String
	 * letterDate1 = dispenceryService.getReplaceString(letterDate); Date
	 * letterDate2 = HMSUtil.convertStringDateToUtilDate(letterDate1, "dd/MM/yyyy");
	 * injuryRegister.setLetterDate(new Timestamp(letterDate2.getTime())); } if
	 * (jsondata.get("authorityletterNo") != null &&
	 * !jsondata.get("authorityletterNo").toString().isEmpty()) { String
	 * authorityletterNo = jsondata.get("authorityletterNo").toString(); String
	 * authorityletterNo1 = dispenceryService.getReplaceString(authorityletterNo);
	 * injuryRegister.setApprovingAuthorityName(authorityletterNo1); } if
	 * (jsondata.get("approvalDate") != null &&
	 * !jsondata.get("approvalDate").toString().isEmpty()) { String approvalDate =
	 * jsondata.get("approvalDate").toString(); String approvalDate1 =
	 * dispenceryService.getReplaceString(approvalDate);
	 * if(!approvalDate1.equalsIgnoreCase("")) { Date approvalDate2 =
	 * HMSUtil.convertStringDateToUtilDate(approvalDate1, "dd/MM/yyyy");
	 * injuryRegister.setApprovalDate(new Timestamp(approvalDate2.getTime())); } }
	 * 
	 * if (jsondata.get("remark") != null) { String remark =
	 * jsondata.get("remark").toString(); String remark1 =
	 * dispenceryService.getReplaceString(remark);
	 * injuryRegister.setRemark(remark1); } String empId1=""; if
	 * (jsondata.get("empId") != null) { String empId =
	 * jsondata.get("empId").toString(); empId1 =
	 * dispenceryService.getReplaceString(empId); } //checking patient is exist or
	 * not String empName=""; String empRankId=""; String empGenderId=""; String
	 * sNo=""; String dob=""; Date birthDate2=null; String patientId="";
	 * if(!jsondata.get("birthDate").toString().isEmpty()) { String birthDate =
	 * jsondata.get("birthDate").toString(); String birthDate1 =
	 * dispenceryService.getReplaceString(birthDate); birthDate2 =
	 * HMSUtil.convertStringDateToUtilDate(birthDate1, "dd/MM/yyyy"); }
	 * 
	 * if (jsondata.get("serviceNo") != null) { String serviceNo =
	 * jsondata.get("serviceNo").toString(); sNo =
	 * dispenceryService.getReplaceString(serviceNo);
	 * 
	 * } if (jsondata.get("empName") != null) { String empName1 =
	 * jsondata.get("empName").toString(); empName =
	 * dispenceryService.getReplaceString(empName1);
	 * 
	 * } if (jsondata.get("patientId") != null) { String patientId1 =
	 * jsondata.get("patientId").toString(); patientId =
	 * dispenceryService.getReplaceString(patientId1);
	 * 
	 * } MasRank masRank =new MasRank(); if (jsondata.get("rankId") != null) {
	 * String rankId1 = jsondata.get("rankId").toString(); empRankId =
	 * dispenceryService.getReplaceString(rankId1);
	 * masRank.setRankId(Long.parseLong(empRankId)); } MasAdministrativeSex
	 * masAdministrativeSex =new MasAdministrativeSex(); MasBloodGroup masBloodGroup
	 * =new MasBloodGroup(); if (jsondata.get("genderId") != null) { String
	 * genderId1 = jsondata.get("genderId").toString(); empGenderId =
	 * dispenceryService.getReplaceString(genderId1);
	 * masAdministrativeSex.setAdministrativeSexId(Long.parseLong(empGenderId));
	 * 
	 * }
	 * 
	 * Patient patient=new Patient(); MasEmployee masEmployee=null; MasUnit
	 * masUnit=null; if(patientId.equalsIgnoreCase("")) { long relationId; String
	 * selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); relationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode); String
	 * serviceTypeCode = HMSUtil.getProperties("adt.properties",
	 * "SERVICE_TYPE_CODE_ICG"); String registrationTypeCode =
	 * HMSUtil.getProperties("adt.properties", "ICG_REGISTRATION_TYPE_ID");
	 * MasServiceType masServiceType=mIAdminDao.getServiceType(serviceTypeCode);
	 * MasRegistrationType
	 * masRegistrationType=mIAdminDao.getRegistrationType(registrationTypeCode);
	 * //String uhidNo =
	 * patientRegistrationDao.getHinNo(sNo,relationId,masRegistrationType.
	 * getRegistrationTypeId()); masEmployee=
	 * mIAdminDao.getEmpInfoByServiceNo(sNo,hospitalId); masUnit=
	 * mIAdminDao.getMasUnit(masEmployee.getMasUnit());
	 * 
	 * patient.setAdministrativeSexId(Long.parseLong(empGenderId));
	 * patient.setDateOfBirth(birthDate2);
	 * patient.setRankId(Long.parseLong(empRankId));
	 * patient.setPatientName(empName);
	 * patient.setServiceJoinDate(masEmployee.getDoe());
	 * patient.setServiceNo(sNo.toUpperCase()); patient.setEmployeeName(empName);
	 * if(masEmployee.getMasEmployeeCategory()!=null)
	 * patient.setEmployeeCategoryId(masEmployee.getMasEmployeeCategory().
	 * getEmployeeCategoryId()); patient.setMobileNumber(masEmployee.getMobileNo());
	 * patient.setEmailId(masEmployee.getEmail());
	 * patient.setRegistrationTypeId(masRegistrationType.getRegistrationTypeId());
	 * patient.setServiceStatusId(masServiceType.getServiceTypeId());
	 * if(masEmployee.getMasRecordOfficeAddress()!=null)
	 * patient.setRecordOfficeAddressId(masEmployee.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressId()); if(masUnit!=null)
	 * patient.setUnitId(masUnit.getUnitId()); patient.setRelationId(relationId);
	 * //patient.setUhidNo(uhidNo); if(masRank!=null && masRank.getMasTrade()!=null)
	 * patient.setTradeId(masRank.getMasTrade().getTradeId());
	 * 
	 * }else { //patient= mIAdminDao.getPatient(Long.parseLong(patientId));get
	 * patient= mIAdminDao.getNameByServiceNo(sNo,hospitalId);
	 * patient.setAdministrativeSexId(Long.parseLong(empGenderId));
	 * patient.setDateOfBirth(birthDate2);
	 * patient.setRankId(Long.parseLong(empRankId)); } long pId =
	 * mIAdminDao.saveOrUpdatePatientDetails(patient);
	 * //patient.setPatientId(Long.parseLong(empId1)); Date date = new Date(); Users
	 * users=null; String ridcId="";//TBD
	 * 
	 * 
	 * //ridc id code String[] ridcIdValue=null; int ridcIdLength=0; String
	 * report=""; String letter="";
	 * 
	 * if (jsondata.get("ridcId") != null &&
	 * !jsondata.get("ridcId").toString().isEmpty()) { String ridcid =
	 * jsondata.get("ridcId").toString(); ridcIdValue = ridcid.split(",");
	 * ridcIdLength = ridcIdValue.length; } //End Ridc code if
	 * (jsondata.get("report") != null &&
	 * !jsondata.get("report").toString().isEmpty()) { report =
	 * jsondata.get("report").toString(); } if (jsondata.get("letter") != null &&
	 * !jsondata.get("letter").toString().isEmpty()) { letter =
	 * jsondata.get("letter").toString(); } //save data in table
	 * if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); }
	 * injuryRegister.setLastChgBy(users); injuryRegister.setIcdDiagnosis(icdId1);
	 * injuryRegister.setMasHospital(masHospital1);
	 * injuryRegister.setPatient(patient); injuryRegister.setLastChgDate(new
	 * Timestamp(date.getTime())); injuryRegister.setInjuryDate(new
	 * Timestamp(date.getTime())); if(ridcIdValue!=null && ridcIdLength==2) {
	 * injuryRegister.setRicdReportId(Long.parseLong(ridcIdValue[0].trim()));
	 * injuryRegister.setRicdLetterId(Long.parseLong(ridcIdValue[1].trim())); }else
	 * if (jsondata.get("report") != null &&
	 * !jsondata.get("report").toString().isEmpty())
	 * 
	 * injuryRegister.setRicdReportId(Long.parseLong(ridcIdValue[0].trim())); else
	 * if (jsondata.get("letter") != null &&
	 * !jsondata.get("letter").toString().isEmpty())
	 * 
	 * injuryRegister.setRicdLetterId(Long.parseLong(ridcIdValue[0].trim()));
	 * 
	 * long injuryRegisterId =
	 * mIAdminDao.saveOrUpdateInjuryRegister(injuryRegister);
	 * 
	 * if (injuryRegisterId != 0) { jsonObj.put("injuryId", injuryRegisterId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * } else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getInjuryReportList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<InjuryRegister>
	 * injuryDiaryList = new ArrayList<InjuryRegister>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); List<Object[]> objectArrayList =
	 * new ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getInjuryReportList(pageNo,requestData); int count =
	 * 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * injuryDiaryList = (List<InjuryRegister>) responseMap.get("injuryDiaryList");
	 * if (!injuryDiaryList.isEmpty() && injuryDiaryList.size() > 0) {
	 * for(InjuryRegister injuryRegister : injuryDiaryList) {
	 * Map<String,Object>map1=new HashMap<String, Object>();
	 * if(injuryRegister.getPatient().getServiceNo()!=null) map1.put("serviceNo",
	 * injuryRegister.getPatient().getServiceNo()); else map1.put("serviceNo","");
	 * if(injuryRegister.getPatient().getEmployeeName()!=null) map1.put("empName",
	 * injuryRegister.getPatient().getEmployeeName()); else map1.put("empName","");
	 * if(injuryRegister.getPatient().getDateOfBirth()!=null)
	 * map1.put("age",HMSUtil.calculateAge(injuryRegister.getPatient().
	 * getDateOfBirth())); else map1.put("age", "");
	 * if(injuryRegister.getIcdDiagnosis()!=null)
	 * map1.put("diagnosis",injuryRegister.getIcdDiagnosis()); else
	 * map1.put("diagnosis", ""); if(injuryRegister.getIcdDiagnosis()!=null)
	 * map1.put("icdNo",injuryRegister.getIcdDiagnosis()); else
	 * map1.put("icdNo",""); if(injuryRegister.getLetterNo()!=null)
	 * map1.put("letterNo", injuryRegister.getLetterNo()); else map1.put("letterNo",
	 * ""); if(injuryRegister.getLetterDate()!=null) map1.put("letterDate",
	 * HMSUtil.changeDateToddMMyyyy(injuryRegister.getLetterDate())); else
	 * map1.put("letterDate", "");
	 * 
	 * if(injuryRegister.getApprovingAuthorityName()!=null)
	 * map1.put("authorityletterNo", injuryRegister.getApprovingAuthorityName());
	 * else map1.put("authorityletterNo", "");
	 * if(injuryRegister.getApprovalDate()!=null) map1.put("approvalDate",
	 * HMSUtil.changeDateToddMMyyyy(injuryRegister.getApprovalDate())); else
	 * map1.put("approvalDate", ""); if(injuryRegister.getRemark()!=null)
	 * map1.put("remark",injuryRegister.getRemark()); else map1.put("remark", "");
	 * 
	 * if(injuryRegister.getInjuryDate()!=null) map1.put("date",
	 * HMSUtil.changeDateToddMMyyyy(injuryRegister.getInjuryDate())); else
	 * map1.put("date", "");
	 * 
	 * if(injuryRegister.getRicdReportId()!=0) map1.put("ridcId",
	 * injuryRegister.getRicdReportId()); else map1.put("ridcId", "");
	 * 
	 * if(injuryRegister.getRicdLetterId()!=0) map1.put("ridcLetterId",
	 * injuryRegister.getRicdLetterId()); else map1.put("ridcLetterId", "");
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public String submitHospitalVisitRegister(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * JSONObject jsonObj = new JSONObject(); long
	 * hospitalVisitId=mIAdminDao.submitHospitalVisitRegister(jsondata); if
	 * (hospitalVisitId != 0 && hospitalVisitId != -1) {
	 * jsonObj.put("hospitalVisitId", hospitalVisitId); jsonObj.put("status", 1);
	 * jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else { if (hospitalVisitId == -1) { jsonObj.put("status", 0);
	 * jsonObj.put("msg", "Something went wrong Record not Saved ");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString();
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getHospitalVisitList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { List<HospitalVisitRegister> hospitalVisitList = new
	 * ArrayList<HospitalVisitRegister>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); List<Object[]> objectArrayList = new
	 * ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getHospitalVisitReportList(pageNo,requestData); int
	 * count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * hospitalVisitList = (List<HospitalVisitRegister>)
	 * responseMap.get("hospitalVisitList"); if (!hospitalVisitList.isEmpty() &&
	 * hospitalVisitList.size() > 0) { for(HospitalVisitRegister
	 * hospitalVisitRegister : hospitalVisitList) { Map<String,Object>map1=new
	 * HashMap<String, Object>();
	 * if(hospitalVisitRegister.getPatient().getServiceNo()!=null)
	 * map1.put("serviceNo", hospitalVisitRegister.getPatient().getServiceNo());
	 * else map1.put("serviceNo","");
	 * map1.put("hospitalVisitId",hospitalVisitRegister.getHostpitalVisitRegisterId(
	 * )); if(hospitalVisitRegister.getPatient().getMasRank().getRankName()!=null)
	 * map1.put("rank",
	 * hospitalVisitRegister.getPatient().getMasRank().getRankName()); else
	 * map1.put("rank","");
	 * if(hospitalVisitRegister.getPatient().getEmployeeName()!=null)
	 * map1.put("empName", hospitalVisitRegister.getPatient().getEmployeeName());
	 * else map1.put("empName","");
	 * if(hospitalVisitRegister.getPatient().getDateOfBirth()!=null)
	 * map1.put("age",HMSUtil.calculateAge(hospitalVisitRegister.getPatient().
	 * getDateOfBirth())); else map1.put("age", "");
	 * if(hospitalVisitRegister.getIcdDiagnosis()!=null)
	 * map1.put("diagnosis",hospitalVisitRegister.getIcdDiagnosis()); else
	 * map1.put("diagnosis", ""); if(hospitalVisitRegister.getWardName()!=null)
	 * map1.put("wardName",hospitalVisitRegister.getWardName()); else
	 * map1.put("wardName",""); if(hospitalVisitRegister.getHospitalName()!=null)
	 * map1.put("hospitalName", hospitalVisitRegister.getHospitalName()); else
	 * map1.put("hospitalName", "");
	 * if(hospitalVisitRegister.getVisitRemarksDivOfficer()!=null)
	 * map1.put("medChief", hospitalVisitRegister.getVisitRemarksDivOfficer()); else
	 * map1.put("medChief", ""); if(hospitalVisitRegister.getVisitByMedDept()!=null)
	 * map1.put("medDept",hospitalVisitRegister.getVisitByMedDept()); else
	 * map1.put("medDept", "");
	 * 
	 * if(hospitalVisitRegister.getRemarks()!=null)
	 * map1.put("PMOremarks",hospitalVisitRegister.getRemarks()); else
	 * map1.put("PMOremarks", "");
	 * if(hospitalVisitRegister.getCaptainRemark()!=null) map1.put("captainRemarks",
	 * hospitalVisitRegister.getCaptainRemark()); else map1.put("captainRemarks",
	 * "");
	 * 
	 * if(hospitalVisitRegister.getHospitalVisitDate()!=null) map1.put("date",
	 * HMSUtil.changeDateToddMMyyyy(hospitalVisitRegister.getHospitalVisitDate()));
	 * else map1.put("date", "");
	 * 
	 * List<VisitorDetail> visitorDetailList; String visitor=""; String
	 * visitornameAndRank=""; String remark="";
	 * if(hospitalVisitRegister.getVisitorDetail()!=null) {
	 * visitorDetailList=hospitalVisitRegister.getVisitorDetail(); for
	 * (VisitorDetail visitorDetail : visitorDetailList) {
	 * visitor=visitor+","+visitorDetail.getVisitorName();
	 * visitornameAndRank=visitornameAndRank+","+visitorDetail.getRankAndName();
	 * remark=remark+","+visitorDetail.getRemarks(); } map1.put("visitor",
	 * visitor.substring(1)); map1.put("visitornameAndRank",
	 * visitornameAndRank.substring(1)); map1.put("remark", remark.substring(1)); }
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public String submitHivStdRegister(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject jsonObj = new JSONObject(); long hivId =
	 * mIAdminDao.saveOrUpdateHivStdRegisterData(jsondata);
	 * 
	 * if (hivId != 0 && hivId != -1) { jsonObj.put("hivId", hivId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else { if (hivId == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Something went wrong Record not Saved ");
	 * 
	 * }else { if (hivId == -2) { jsonObj.put("status", 1); jsonObj.put("msg",
	 * "Record Saved Successfully");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } } }
	 * return jsonObj.toString();
	 * 
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getHivStdList(HashMap<String, String>
	 * requestData, HttpServletRequest request, HttpServletResponse response) { //
	 * TODO Auto-generated method stub List<HivStdRegister> hivStdList = new
	 * ArrayList<HivStdRegister>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getHivStdList(pageNo,requestData); int count = 1; if
	 * (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * hivStdList = (List<HivStdRegister>) responseMap.get("hivStdList"); if
	 * (!hivStdList.isEmpty() && hivStdList.size() > 0) { for(HivStdRegister
	 * hivStdRegister : hivStdList) { Map<String,Object>map1=new HashMap<String,
	 * Object>(); if(hivStdRegister.getPatient().getServiceNo()!=null)
	 * map1.put("serviceNo", hivStdRegister.getPatient().getServiceNo()); else
	 * map1.put("serviceNo","");
	 * if(hivStdRegister.getPatient().getMasRank().getRankName()!=null)
	 * map1.put("rank", hivStdRegister.getPatient().getMasRank().getRankName());
	 * else map1.put("rank","");
	 * if(hivStdRegister.getPatient().getEmployeeName()!=null) map1.put("empName",
	 * hivStdRegister.getPatient().getEmployeeName()); else map1.put("empName","");
	 * if(hivStdRegister.getPatient().getDateOfBirth()!=null)
	 * map1.put("age",HMSUtil.calculateAge(hivStdRegister.getPatient().
	 * getDateOfBirth())); else map1.put("age", "");
	 * if(hivStdRegister.getPatientMedicalCat()!=null)
	 * map1.put("diagnosis",hivStdRegister.getPatientMedicalCat().getMasIcd().
	 * getIcdName()); else map1.put("diagnosis", "");
	 * if(hivStdRegister.getPatientMedicalCat()!=null)
	 * map1.put("medCategory",hivStdRegister.getPatientMedicalCat().
	 * getMasMedicalCategory().getMedicalCategoryName()); else
	 * map1.put("medCategory",""); if(hivStdRegister.getRegisterDate()!=null)
	 * map1.put("registerDate",
	 * HMSUtil.changeDateToddMMyyyy(hivStdRegister.getRegisterDate())); else
	 * map1.put("registerDate", ""); if(hivStdRegister.getRemarks()!=null)
	 * map1.put("remarks",hivStdRegister.getRemarks()); else map1.put("remarks",
	 * "");
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public String submitMilkTesting(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); JSONObject
	 * json = new JSONObject(jsondata); MilkTestingRegister milkTestingRegister =
	 * new MilkTestingRegister();
	 * 
	 * MasHospital masHospital = null; MasDepartment masDepartment = null;
	 * 
	 * if (jsondata != null) { if(!jsondata.get("testingDate").toString().isEmpty())
	 * { String testingDate = jsondata.get("testingDate").toString(); String
	 * testingDate1 = dispenceryService.getReplaceString(testingDate); Date
	 * testingDate2 = HMSUtil.convertStringDateToUtilDate(testingDate1,
	 * "dd/MM/yyyy"); milkTestingRegister.setMilkTestingDate(new
	 * Timestamp(testingDate2.getTime())); } MasHospital masHospital1 = null; String
	 * hospitalId=""; if (jsondata.get("hospitalId") != null) { hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital1 =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); }
	 * 
	 * if (jsondata.get("sos") != null) { String sos =
	 * jsondata.get("sos").toString(); String sos1 =
	 * dispenceryService.getReplaceString(sos);
	 * milkTestingRegister.setSourceOfSupply(sos1); } if (jsondata.get("location")
	 * != null) { String location = jsondata.get("location").toString(); String
	 * location1 = dispenceryService.getReplaceString(location);
	 * milkTestingRegister.setLocationOfSampling(location1); } if
	 * (jsondata.get("gravity") != null) { String gravity =
	 * jsondata.get("gravity").toString(); String gravity1 =
	 * dispenceryService.getReplaceString(gravity);
	 * milkTestingRegister.setSpecificGravity(gravity1); } if
	 * (jsondata.get("testedBy") != null) { String testedBy =
	 * jsondata.get("testedBy").toString(); String testedBy1 =
	 * dispenceryService.getReplaceString(testedBy);
	 * milkTestingRegister.setTestedBy(testedBy1); } if (jsondata.get("remark") !=
	 * null) { String remark = jsondata.get("remark").toString(); String remark1 =
	 * dispenceryService.getReplaceString(remark);
	 * milkTestingRegister.setRemarks(remark1); }
	 * 
	 * Date date = new Date(); Users users=null;
	 * 
	 * //save data in table if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); }
	 * milkTestingRegister.setLastChgBy(users);
	 * milkTestingRegister.setMasHospital(masHospital1);
	 * milkTestingRegister.setLastChgDate(new Timestamp(date.getTime()));
	 * //injuryRegister.setRicdId(Long.parseLong(ridcId)); long mId =
	 * mIAdminDao.saveOrUpdateMilkTesting(milkTestingRegister);
	 * 
	 * if (mId != 0) { jsonObj.put("mId", mId); jsonObj.put("status", 1);
	 * jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * } else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getMilkTestingList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<MilkTestingRegister>
	 * milkTestingList = new ArrayList<MilkTestingRegister>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getMilkTestingList(pageNo,requestData); int count =
	 * 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * milkTestingList = (List<MilkTestingRegister>)
	 * responseMap.get("milkTestingList"); if (!milkTestingList.isEmpty() &&
	 * milkTestingList.size() > 0) { for(MilkTestingRegister milkTestingRegister :
	 * milkTestingList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * if(milkTestingRegister.getMilkTestingDate()!=null) map1.put("date",
	 * HMSUtil.changeDateToddMMyyyy(milkTestingRegister.getMilkTestingDate())); else
	 * map1.put("date",""); if(milkTestingRegister.getSourceOfSupply()!=null)
	 * map1.put("sos",milkTestingRegister.getSourceOfSupply()); else
	 * map1.put("sos",""); if(milkTestingRegister.getLocationOfSampling()!=null)
	 * map1.put("location", milkTestingRegister.getLocationOfSampling()); else
	 * map1.put("location",""); if(milkTestingRegister.getSpecificGravity()!=null)
	 * map1.put("gravity",milkTestingRegister.getSpecificGravity()); else
	 * map1.put("gravity", ""); if(milkTestingRegister.getRemarks()!=null)
	 * map1.put("remarks",milkTestingRegister.getRemarks()); else
	 * map1.put("remarks", ""); if(milkTestingRegister.getTestedBy()!=null)
	 * map1.put("testedBy",milkTestingRegister.getTestedBy()); else
	 * map1.put("testedBy", ""); data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public String submitWaterTesting(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); JSONObject
	 * json = new JSONObject(jsondata); WaterTestingRegister waterTestingRegister =
	 * new WaterTestingRegister();
	 * 
	 * MasHospital masHospital = null; MasDepartment masDepartment = null;
	 * 
	 * if (jsondata != null) { if(!jsondata.get("testingDate").toString().isEmpty())
	 * { String testingDate = jsondata.get("testingDate").toString(); String
	 * testingDate1 = dispenceryService.getReplaceString(testingDate); Date
	 * testingDate2 = HMSUtil.convertStringDateToUtilDate(testingDate1,
	 * "dd/MM/yyyy"); waterTestingRegister.setWaterTestingDate(new
	 * Timestamp(testingDate2.getTime())); } MasHospital masHospital1 = null; String
	 * hospitalId=""; if (jsondata.get("hospitalId") != null) { hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital1 =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); }
	 * 
	 * if (jsondata.get("sos") != null) { String sos =
	 * jsondata.get("sos").toString(); String sos1 =
	 * dispenceryService.getReplaceString(sos);
	 * waterTestingRegister.setSourceOfSupply(sos1); } if (jsondata.get("location")
	 * != null) { String location = jsondata.get("location").toString(); String
	 * location1 = dispenceryService.getReplaceString(location);
	 * waterTestingRegister.setLocationOfSampling(location1); } if
	 * (jsondata.get("chlorine") != null) { String chlorine =
	 * jsondata.get("chlorine").toString(); String chlorine1 =
	 * dispenceryService.getReplaceString(chlorine);
	 * waterTestingRegister.setChlorineContent(chlorine1); } if
	 * (jsondata.get("testedBy") != null) { String testedBy =
	 * jsondata.get("testedBy").toString(); String testedBy1 =
	 * dispenceryService.getReplaceString(testedBy);
	 * waterTestingRegister.setTestedBy(testedBy1); } if (jsondata.get("remark") !=
	 * null) { String remark = jsondata.get("remark").toString(); String remark1 =
	 * dispenceryService.getReplaceString(remark);
	 * waterTestingRegister.setRemarks(remark1); }
	 * 
	 * Date date = new Date(); Users users=null;
	 * 
	 * //save data in table if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); }
	 * waterTestingRegister.setLastChgBy(users);
	 * waterTestingRegister.setMasHospital(masHospital1);
	 * waterTestingRegister.setLastChgDate(new Timestamp(date.getTime()));
	 * //injuryRegister.setRicdId(Long.parseLong(ridcId)); long wId =
	 * mIAdminDao.saveWaterTesting(waterTestingRegister);
	 * 
	 * if (wId != 0) { jsonObj.put("wId", wId); jsonObj.put("status", 1);
	 * jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * } else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getWaterTestingList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<WaterTestingRegister>
	 * waterTestingList = new ArrayList<WaterTestingRegister>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getWaterTestingList(pageNo,requestData); int count =
	 * 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * waterTestingList = (List<WaterTestingRegister>)
	 * responseMap.get("waterTestingList"); if (!waterTestingList.isEmpty() &&
	 * waterTestingList.size() > 0) { for(WaterTestingRegister waterTestingRegister
	 * : waterTestingList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * if(waterTestingRegister.getWaterTestingDate()!=null) map1.put("date",
	 * HMSUtil.changeDateToddMMyyyy(waterTestingRegister.getWaterTestingDate()));
	 * else map1.put("date",""); if(waterTestingRegister.getSourceOfSupply()!=null)
	 * map1.put("sos",waterTestingRegister.getSourceOfSupply()); else
	 * map1.put("sos",""); if(waterTestingRegister.getLocationOfSampling()!=null)
	 * map1.put("location", waterTestingRegister.getLocationOfSampling()); else
	 * map1.put("location",""); if(waterTestingRegister.getChlorineContent()!=null)
	 * map1.put("ppm",waterTestingRegister.getChlorineContent()); else
	 * map1.put("ppm", ""); if(waterTestingRegister.getRemarks()!=null)
	 * map1.put("remarks",waterTestingRegister.getRemarks()); else
	 * map1.put("remarks", ""); if(waterTestingRegister.getTestedBy()!=null)
	 * map1.put("testedBy",waterTestingRegister.getTestedBy()); else
	 * map1.put("testedBy", ""); data.put(count++, map1);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public Map<String, Object> getEmpList(HashMap<String, String>
	 * jsondata) { // TODO Auto-generated method stub Map<String, Object> map1 = new
	 * HashMap<String, Object>(); String hospitalId =
	 * jsondata.get("hospitalId").toString(); List<MasEmployee> masEmployeeList =
	 * new ArrayList<MasEmployee>(); Map<String, Object> map = new
	 * LinkedHashMap<String, Object>();
	 * 
	 * masEmployeeList = mIAdminDao.getEmpList(Long.parseLong(hospitalId)); for
	 * (MasEmployee masEmployee : masEmployeeList) {
	 * map.put(masEmployee.getEmployeeName(),
	 * masEmployee.getEmployeeName()+"["+masEmployee.getServiceNo()+"]"); } if
	 * (masEmployeeList != null && masEmployeeList.size() > 0) { map1.put("data",
	 * map); map1.put("status", 1); } else { map1.put("data", map); map1.put("msg",
	 * "Data not found"); map1.put("status", 0); }
	 * 
	 * return map1; }
	 * 
	 * @Override public Map<String, Object>
	 * getDangerousDrugRegisterList(HashMap<String, String> requestData,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); JSONObject jsonResponse = new JSONObject(); Map<Integer,
	 * Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
	 * Map<String, Object> map = new HashMap<String, Object>(); long pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * Object waterTestingList =null; responseMap =
	 * mIAdminDao.getDangerousDrugRegisterList(pageNo,requestData); int count = 1;
	 * if (responseMap.size() > 0 && !responseMap.isEmpty()) { waterTestingList =
	 * responseMap.get("dangerousDrugList"); JSONObject result =
	 * (JSONObject)responseMap.get("dangerousDrugList"); map.put("data", result);
	 * jsonResponse.put("dangerousDrugList", result);
	 * 
	 * for(Object obj : waterTestingList) { Map<String,Object>map1=new
	 * HashMap<String, Object>();
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * } //Integer tm = (Integer) responseMap.get("totalMatches");
	 * 
	 * //map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public Map<String, Object> getManufactureList(Map<String, String>
	 * requestData) { // TODO Auto-generated method stub Map<String, Object> dataMap
	 * = new HashMap<String, Object>(); Map<String, Object> responseMap = new
	 * HashMap<String, Object>(); dataMap =
	 * mIAdminDao.getManufactureList(requestData); List<Object> responseList = new
	 * ArrayList<Object>(); if (dataMap.size() > 0) { List<MasStoreSupplier>
	 * masManufacturerList= (List<MasStoreSupplier>)
	 * dataMap.get("masManufacturerList"); if (masManufacturerList.size() > 0 &&
	 * masManufacturerList != null) { for (MasStoreSupplier mm :
	 * masManufacturerList) { HashMap<String, Object> map = new HashMap<String,
	 * Object>(); map.put("manufacturerId", mm.getSupplierId());
	 * map.put("manufacturerName", mm.getSupplierName()); responseList.add(map); }
	 * if (responseList != null && responseList.size() > 0) {
	 * responseMap.put("manufacturerList", responseList); responseMap.put("status",
	 * 1); } } else { responseMap.put("data", responseList); responseMap.put("msg",
	 * "Data not found"); responseMap.put("status", 0); } } return responseMap; }
	 * 
	 * @Override public String submitEquipmentDetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); long
	 * eqMId=mIAdminDao.submitEquipmentDetails(jsondata); if (eqMId != 0 && eqMId !=
	 * -1) { jsonObj.put("eqMId", eqMId); jsonObj.put("status", 1);
	 * jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else { if (eqMId == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Something went wrong Record not Saved ");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString();
	 * 
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getEquipmentDetails(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<AppEquipmentDt>
	 * appEquipmentDtList = new ArrayList<AppEquipmentDt>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); List<Object[]> objectArrayList =
	 * new ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getEquipmentDetails(pageNo,requestData); int count =
	 * 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * appEquipmentDtList = (List<AppEquipmentDt>)
	 * responseMap.get("appEquipmentDtList"); if (!appEquipmentDtList.isEmpty() &&
	 * appEquipmentDtList.size() > 0) { for(AppEquipmentDt appEquipmentDt :
	 * appEquipmentDtList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * if(appEquipmentDt.getMasDepartment()!=null) map1.put("department",
	 * appEquipmentDt.getMasDepartment().getDepartmentName()); else
	 * map1.put("department",""); if(appEquipmentDt!=null)
	 * map1.put("dtId",appEquipmentDt.getEquipmentDtId()); else map1.put("dtId","");
	 * if(appEquipmentDt.getModelNumber()!=null)
	 * map1.put("modelNo",appEquipmentDt.getModelNumber()); else map1.put("modelNo",
	 * ""); if(appEquipmentDt.getSerialNumber()!=null)
	 * map1.put("serialNo",appEquipmentDt.getSerialNumber()); else
	 * map1.put("serialNo", ""); if(appEquipmentDt.getDepreciation()!=null)
	 * map1.put("depriciation",appEquipmentDt.getDepreciation()); else
	 * map1.put("depriciation",""); if(appEquipmentDt.getPrice()!=null)
	 * map1.put("price", appEquipmentDt.getPrice()); else map1.put("price", "");
	 * if(appEquipmentDt.getMake()!=null) map1.put("make",
	 * appEquipmentDt.getMake()); else map1.put("make", "");
	 * if(appEquipmentDt.getMasStoreSupplier()!=null) map1.put("manufacturer",
	 * appEquipmentDt.getMasStoreSupplier().getSupplierName()); else
	 * map1.put("manufacturer", "");
	 * if(appEquipmentDt.getTechnicalSpecification()!=null)
	 * map1.put("technicalSpec", appEquipmentDt.getTechnicalSpecification()); else
	 * map1.put("technicalSpec", ""); if(appEquipmentDt.getReceivedNumber()!=null)
	 * map1.put("rvNumber", appEquipmentDt.getReceivedNumber()); else
	 * map1.put("rvNumber", ""); if(appEquipmentDt.getReceivedDate()!=null)
	 * map1.put("rvDate",
	 * HMSUtil.changeDateToddMMyyyy(appEquipmentDt.getReceivedDate())); else
	 * map1.put("rvDate", ""); if(appEquipmentDt.getInstalled()!=null &&
	 * appEquipmentDt.getInstalled().equalsIgnoreCase("Y")) map1.put("installed",
	 * HMSUtil.changeDateToddMMyyyy(appEquipmentDt.getInstalledDate())); else
	 * map1.put("installed","");
	 * 
	 * if(appEquipmentDt.getAmc()!=null || appEquipmentDt.getWarranty()!=null)
	 * map1.put("statusAW", "1"); else map1.put("statusAW", "0");
	 * 
	 * if(appEquipmentDt.getAccessary()!=null) map1.put("statusAcc", "1"); else
	 * map1.put("statusAcc", "0");
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public String submitWarrantydetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); String flag
	 * = jsondata.get("flag").toString(); String flag1 =
	 * dispenceryService.getReplaceString(flag);
	 * 
	 * String statusFlag = jsondata.get("statusFlag").toString(); String statusFlag1
	 * = dispenceryService.getReplaceString(statusFlag);
	 * 
	 * Map<String,Object> map1=new HashMap<String, Object>(); AppEquipmentDt
	 * appEquipmentDt = mIAdminDao.getAppEquipmentDt(jsondata);
	 * if(appEquipmentDt.getAmc()==null && appEquipmentDt.getWarranty()==null &&
	 * statusFlag1.equalsIgnoreCase("")) { long
	 * appInfoId=mIAdminDao.submitWarrantydetails(jsondata);
	 * 
	 * if (appInfoId != 0 && appInfoId != -1) { jsonObj.put("appInfoId", appInfoId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else { if (appInfoId == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Something went wrong Record not Saved ");
	 * 
	 * }else {
	 * 
	 * jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } } }else {
	 * 
	 * if(appEquipmentDt.getAmc()==null && appEquipmentDt.getWarranty()!=null &&
	 * flag1.equalsIgnoreCase("amc")) { jsonObj.put("status", 2);//AMC==2
	 * jsonObj.put("msg", "AMC not exist"); } else
	 * if(appEquipmentDt.getWarranty()==null && appEquipmentDt.getAmc()!=null &&
	 * flag1.equalsIgnoreCase("warranty")) { jsonObj.put("status", 4);//AMC==4
	 * jsonObj.put("msg", "Warranty not exist"); } else
	 * if(appEquipmentDt.getAmc()!=null || appEquipmentDt.getWarranty()!=null) {
	 * map1.put("startDate",
	 * HMSUtil.changeDateToddMMyyyy(appEquipmentDt.getAppEquipmentInfos().
	 * getStartDate())); map1.put("endDate",
	 * HMSUtil.changeDateToddMMyyyy(appEquipmentDt.getAppEquipmentInfos().getEndDate
	 * ())); map1.put("details",
	 * appEquipmentDt.getAppEquipmentInfos().getEquipmentDetails());
	 * map1.put("totalPreventive",
	 * appEquipmentDt.getAppEquipmentInfos().getTotalPreventive());
	 * jsonObj.put("status", 3); jsonObj.put("data", map1);
	 * if(flag1.equalsIgnoreCase("warranty")) jsonObj.put("msg",
	 * "Warranty already  exist"); else jsonObj.put("msg", "AMC already  exist");
	 * }else { jsonObj.put("status", 10); jsonObj.put("msg", ""); } } return
	 * jsonObj.toString(); }
	 * 
	 * @Override public String submitAccessarydetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject();
	 * Map<String,Object> map1=new HashMap<String, Object>(); Map<Integer,
	 * Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
	 * String statusFlag = jsondata.get("statusFlag").toString(); String statusFlag1
	 * = dispenceryService.getReplaceString(statusFlag); AppEquipmentDt
	 * appEquipmentDt = mIAdminDao.getAppEquipmentDt(jsondata); int count=0;
	 * //if(appEquipmentDt.getAccessary()==null && statusFlag1.equalsIgnoreCase(""))
	 * { if(statusFlag1.equalsIgnoreCase("")) { long
	 * appInfoId=mIAdminDao.submitAccessarydetails(jsondata);
	 * 
	 * if (appInfoId != 0 && appInfoId != -1) { jsonObj.put("appInfoId", appInfoId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else { if (appInfoId == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Something went wrong Record not Saved ");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * 
	 * }else { List<AppEquipmentAccessory> accList =
	 * appEquipmentDt.getAppEquipmentAccessories(); if(accList.size()>0) {
	 * for(AppEquipmentAccessory appEquipmentAccessory : accList) {
	 * Map<String,Object>map=new HashMap<String, Object>();
	 * if(appEquipmentAccessory.getAccessoryName()!=null) map.put("accessaryName",
	 * appEquipmentAccessory.getAccessoryName()); else map.put("accessaryName","");
	 * 
	 * if(appEquipmentAccessory.getAccessoryDetails()!=null) map.put("details",
	 * appEquipmentAccessory.getAccessoryDetails()); else map.put("details","");
	 * if(appEquipmentAccessory.getEndDate()!=null) map.put("endDate",
	 * HMSUtil.changeDateToddMMyyyy(appEquipmentAccessory.getEndDate())); else
	 * map.put("endDate",""); if(appEquipmentAccessory.getStartDate()!=null)
	 * map.put("startDate",
	 * HMSUtil.changeDateToddMMyyyy(appEquipmentAccessory.getStartDate())); else
	 * map.put("startDate",""); if(appEquipmentAccessory.getSerialNumber()!=null)
	 * map.put("serialNo", appEquipmentAccessory.getSerialNumber()); else
	 * map.put("serialNo",""); if(appEquipmentAccessory.getModelNumber()!=null)
	 * map.put("modelNo", appEquipmentAccessory.getModelNumber()); else
	 * map.put("modelNo","");
	 * map.put("accId",appEquipmentAccessory.getAccessoryId()); data.put(count++,
	 * map); } jsonObj.put("status",2); //jsonObj.put("msg",
	 * "Accessary details  exist"); jsonObj.put("msg", ""); }else {
	 * jsonObj.put("status",10); jsonObj.put("msg", ""); }
	 * jsonObj.put("accList",data); } return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getEquipmentDetailsByItemId(String
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); JSONObject json = new JSONObject(jsondata); AppEquipmentHd
	 * appEquipmentHd=null; int status=0; String itemId =
	 * json.get("itemId").toString(); String hospitalId =
	 * json.get("hospitalId").toString(); appEquipmentHd=
	 * mIAdminDao.getEquipmentDetails(itemId,hospitalId); if(appEquipmentHd!=null) {
	 * responseMap.put("status", 1); responseMap.put("scale",
	 * appEquipmentHd.getAuthorizedMeScale()); responseMap.put("qty",
	 * appEquipmentHd.getAuthorizedQty()); responseMap.put("msg", "success"); } else
	 * { responseMap.put("status", 1); responseMap.put("scale", "");
	 * responseMap.put("qty", 0); responseMap.put("msg", "success"); }
	 * 
	 * 
	 * return responseMap; }
	 * 
	 * @Override public Map<String, Object> getEquipmentReportList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub //List<OpdDisposalDetail>
	 * opdPatientDetailList = new ArrayList<OpdDisposalDetail>(); Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); List<Object[]> objectArrayList =
	 * new ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * List<AppEquipmentDt> appEquipmentDtList = new ArrayList<AppEquipmentDt>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = mIAdminDao.getEquipmentList(pageNo,requestData); int count = 1;
	 * int match = 0; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * objectArrayList = (List<Object[]>) responseMap.get("appEquipmentDtList"); if
	 * (!objectArrayList.isEmpty() && objectArrayList.size() > 0) { for( Object[]
	 * data1 : objectArrayList) { String rvNumber = data1[0].toString(); String
	 * rvDate = data1[1].toString(); int qty= Integer.parseInt(data1[2].toString());
	 * String eqHdId= data1[3].toString(); String[] arr = rvDate.split(" "); Date
	 * rvdate=null; int boardOut=0; appEquipmentDtList =
	 * mIAdminDao.appEquipmentDtList(Long.parseLong(eqHdId),rvNumber);
	 * boardOut=appEquipmentDtList.size(); try { rvdate =
	 * HMSUtil.dateFormatteryyyymmdd(arr[0]); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } String rvdate1 =
	 * HMSUtil.convertDateToStringFormat(rvdate,"dd/MM/yyyy");
	 * Map<String,Object>map1=new HashMap<String, Object>(); if(qty-boardOut!=0) {
	 * map1.put("rvNumber", rvNumber); map1.put("rvDate", rvdate1); map1.put("qty",
	 * qty); map1.put("eqHdId", eqHdId); map1.put("boardOut", boardOut);
	 * map1.put("balance", qty-boardOut); data.put(count++, map1); match++; }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); //map.put("count", tm); map.put("count", match); map.put("status",
	 * "1"); return map; }
	 * 
	 * @Override public Map<String, Object>
	 * getEquipmentDetailsForstoreLedger(HashMap<String, String> requestData,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub List<AppEquipmentDt> appEquipmentDtList = new
	 * ArrayList<AppEquipmentDt>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); List<Object[]> objectArrayList = new
	 * ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0; responseMap =
	 * mIAdminDao.getEquipmentDetailsForstoreLedger(pageNo,requestData); int count =
	 * 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * appEquipmentDtList = (List<AppEquipmentDt>)
	 * responseMap.get("appEquipmentDtList"); if (!appEquipmentDtList.isEmpty() &&
	 * appEquipmentDtList.size() > 0) { for(AppEquipmentDt appEquipmentDt :
	 * appEquipmentDtList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * if(appEquipmentDt.getMasDepartment()!=null) map1.put("department",
	 * appEquipmentDt.getMasDepartment().getDepartmentName()); else
	 * map1.put("department",""); if(appEquipmentDt!=null)
	 * map1.put("dtId",appEquipmentDt.getEquipmentDtId()); else map1.put("dtId","");
	 * if(appEquipmentDt.getModelNumber()!=null)
	 * map1.put("modelNo",appEquipmentDt.getModelNumber()); else map1.put("modelNo",
	 * ""); if(appEquipmentDt.getSerialNumber()!=null)
	 * map1.put("serialNo",appEquipmentDt.getSerialNumber()); else
	 * map1.put("serialNo", ""); if(appEquipmentDt.getDepreciation()!=null)
	 * map1.put("depriciation",appEquipmentDt.getDepreciation()); else
	 * map1.put("depriciation",""); if(appEquipmentDt.getPrice()!=null)
	 * map1.put("price", appEquipmentDt.getPrice()); else map1.put("price", "");
	 * if(appEquipmentDt.getMake()!=null) map1.put("make",
	 * appEquipmentDt.getMake()); else map1.put("make", "");
	 * if(appEquipmentDt.getMasStoreSupplier()!=null) map1.put("manufacturer",
	 * appEquipmentDt.getMasStoreSupplier().getSupplierName()); else
	 * map1.put("manufacturer", "");
	 * if(appEquipmentDt.getTechnicalSpecification()!=null)
	 * map1.put("technicalSpec", appEquipmentDt.getTechnicalSpecification()); else
	 * map1.put("technicalSpec", ""); if(appEquipmentDt.getReceivedNumber()!=null)
	 * map1.put("rvNumber", appEquipmentDt.getReceivedNumber()); else
	 * map1.put("rvNumber", ""); if(appEquipmentDt.getReceivedDate()!=null)
	 * map1.put("rvDate",
	 * HMSUtil.changeDateToddMMyyyy(appEquipmentDt.getReceivedDate())); else
	 * map1.put("rvDate", ""); if(appEquipmentDt.getInstalled()!=null &&
	 * appEquipmentDt.getInstalled().equalsIgnoreCase("Y")) map1.put("installed",
	 * "Yes"); else map1.put("installed", "No");
	 * 
	 * if(appEquipmentDt.getAmc()!=null || appEquipmentDt.getWarranty()!=null)
	 * map1.put("statusAW", "1"); else map1.put("statusAW", "0");
	 * 
	 * if(appEquipmentDt.getAccessary()!=null) map1.put("statusAcc", "1"); else
	 * map1.put("statusAcc", "0");
	 * 
	 * if(appEquipmentDt.getBoardOut()!=null &&
	 * (appEquipmentDt.getBoardOut().equalsIgnoreCase("Y")||
	 * appEquipmentDt.getBoardOut().equalsIgnoreCase("T"))) map1.put("boardOut",
	 * "0"); else map1.put("boardOut", "1"); if(appEquipmentDt.getRemarks()!=null )
	 * map1.put("boardOutRemark", appEquipmentDt.getRemarks()); else
	 * map1.put("boardOutRemark", ""); List<AppAuditEquipmentDt>
	 * appAuditEquipmentDtlist=appEquipmentDt.getAppAuditEquipmentDt(); if
	 * (!appAuditEquipmentDtlist.isEmpty() && appAuditEquipmentDtlist.size() > 0) {
	 * map1.put("auditDate",
	 * HMSUtil.changeDateToddMMyyyy(appAuditEquipmentDtlist.get(0).getAuditDate()));
	 * map1.put("auditBy", appAuditEquipmentDtlist.get(0).getAuditBy());
	 * 
	 * }else { map1.put("auditBy", ""); map1.put("auditDate", "");
	 * 
	 * } data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map; }
	 * 
	 * @Override public String submitBoardOutDetails(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject jsonObj = new JSONObject(); Map<String,Object> map1=new
	 * HashMap<String, Object>(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); int status =
	 * mIAdminDao.submitBoardOutDetails(jsondata); if (status != 0 && status != -1)
	 * { jsonObj.put("status", 1); jsonObj.put("msg", "item has been board out");
	 * 
	 * }else if (status == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Something went wrong Record not Saved "); }else if (status == 0) {
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record has been Saved ");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); }
	 * 
	 * 
	 * return jsonObj.toString(); }
	 * 
	 * @Override public String submitAuditDetails(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); JSONObject json = new JSONObject(jsondata); JSONObject jsonObj =
	 * new JSONObject();
	 * 
	 * List<AppEquipmentHd> appEquipmentHdList =new ArrayList<AppEquipmentHd>();
	 * long appauditId =0; int status=0; if (jsondata != null) { String itemId = "";
	 * String hospitalId = ""; String auditBy =""; String auditRemarks = ""; Date
	 * auditDate = null;
	 * 
	 * if (!json.get("auditRemarks").toString().isEmpty()) { String auditRemarks1 =
	 * json.get("auditRemarks").toString(); auditRemarks =
	 * dispenceryService.getReplaceString(auditRemarks1);
	 * 
	 * } if (!jsondata.get("itemId").toString().isEmpty()) { String itemId1 =
	 * jsondata.get("itemId").toString(); itemId =
	 * dispenceryService.getReplaceString(itemId1);
	 * 
	 * } if (!jsondata.get("hospitalId").toString().isEmpty()) { String hospitalId1
	 * = jsondata.get("hospitalId").toString(); hospitalId =
	 * dispenceryService.getReplaceString(hospitalId1);
	 * 
	 * } if (!jsondata.get("auditBy").toString().isEmpty()) { String auditBy1 =
	 * jsondata.get("auditBy").toString(); auditBy =
	 * dispenceryService.getReplaceString(auditBy1);
	 * 
	 * } if (!jsondata.get("auditDate").toString().isEmpty()) { String auditDate1 =
	 * jsondata.get("auditDate").toString(); String auditDate2 =
	 * dispenceryService.getReplaceString(auditDate1); auditDate =
	 * HMSUtil.convertStringDateToUtilDate(auditDate2, "dd/MM/yyyy"); }
	 * 
	 * 
	 * appEquipmentHdList= mIAdminDao.getAppEquipmentHdList(itemId,hospitalId);
	 * 
	 * if (appEquipmentHdList!=null && appEquipmentHdList.size() > 0) {
	 * for(AppEquipmentHd appEquipmentHd : appEquipmentHdList) {
	 * List<AppEquipmentDt> appEquipmentDtList = appEquipmentHd.getAppEquipmentDt();
	 * for(AppEquipmentDt appEquipmentDt : appEquipmentDtList) {
	 * if(appEquipmentDt.getBoardOut()==null ||
	 * appEquipmentDt.getBoardOut().equalsIgnoreCase("T")) { AppAuditEquipmentDt
	 * appAuditEquipmentDt=new AppAuditEquipmentDt();
	 * appAuditEquipmentDt.setAppEquipmentDt(appEquipmentDt);
	 * appAuditEquipmentDt.setAppEquipmentHd(appEquipmentHd);
	 * appAuditEquipmentDt.setAuditBy(auditBy);
	 * appAuditEquipmentDt.setAuditDate(auditDate); if(!auditRemarks.isEmpty() &&
	 * !auditRemarks.equalsIgnoreCase(""))
	 * appAuditEquipmentDt.setAuditRemarks(auditRemarks); appauditId =
	 * mIAdminDao.saveOrUpdateAppAuditEquipmentDt(appAuditEquipmentDt);
	 * 
	 * } if(appEquipmentDt.getBoardOut()!=null &&
	 * appEquipmentDt.getBoardOut().equalsIgnoreCase("T")) {
	 * appEquipmentDt.setBoardOut("Y"); appEquipmentDt.setBoardOutDate(auditDate);
	 * status = mIAdminDao.update(appEquipmentDt); } } } if (appauditId != -1) {
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * } else if (appauditId == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Error occured"); } } } return jsonObj.toString(); }
	 * 
	 * @Override public String getDangerousDrugRegisterList1(HashMap<String, String>
	 * requestData, HttpServletRequest request, HttpServletResponse response) { //
	 * TODO Auto-generated method stub Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); JSONObject jsonResponse = new JSONObject();
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>(); long
	 * pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); } responseMap =
	 * mIAdminDao.getDangerousDrugRegisterList(pageNo,requestData); int count = 1;
	 * if (responseMap.size() > 0 && !responseMap.isEmpty()) { JSONObject result =
	 * (JSONObject)responseMap.get("dangerousDrugList"); int tm=
	 * (int)responseMap.get("totalMatches"); map.put("data", result);
	 * jsonResponse.put("dangerousDrugList", result); jsonResponse.put("status",
	 * "1"); jsonResponse.put("count", tm);
	 * 
	 * }
	 * 
	 * 
	 * return jsonResponse.toString(); }
	 * 
	 * @Override public Map<String, Object> getDisposalList() { // TODO
	 * Auto-generated method stub Map<String, Object> map1 = new HashMap<String,
	 * Object>();
	 * 
	 * Map<String,String > map = new LinkedHashMap<String, String>();
	 * List<MasDisposal> disposalList = new ArrayList<MasDisposal>();
	 * 
	 * disposalList = mIAdminDao.getDisposalList(); for (MasDisposal masDisposal :
	 * disposalList) { map.put(masDisposal.getDisposalName(),
	 * masDisposal.getDisposalCode()); } if (disposalList != null &&
	 * disposalList.size() > 0) { map1.put("data", map); map1.put("status", 1); }
	 * else { map1.put("data", map); map1.put("msg", "Data not found");
	 * map1.put("status", 0); }
	 * 
	 * return map1; }
	 * 
	 * @Override public Map<String, Object> getEmpDetailsByServiceNo(String
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); JSONObject json = new JSONObject(jsondata); Patient patient=null;
	 * EmployeeBloodGroup employeeBloodGroup=null; String serviceNo =
	 * json.get("memberService").toString(); String hospitalId =
	 * json.get("hospitalId").toString(); employeeBloodGroup=
	 * mIAdminDao.getemployeeBloodGroup(serviceNo,hospitalId);
	 * if(employeeBloodGroup!=null) { responseMap.put("name",
	 * employeeBloodGroup.getEmployeeName());
	 * if(employeeBloodGroup.getDateOfBirth()!=null) { responseMap.put("dob",
	 * HMSUtil.changeDateToddMMyyyy(employeeBloodGroup.getDateOfBirth()));
	 * responseMap.put("age",HMSUtil.calculateAge(employeeBloodGroup.getDateOfBirth(
	 * ))); } else { responseMap.put("dob", ""); responseMap.put("age",""); }
	 * 
	 * responseMap.put("empBloodId", employeeBloodGroup.getEmployeeBloodGroupId());
	 * responseMap.put("empId", ""); if(employeeBloodGroup.getRankId()!=0) { MasRank
	 * masRank= mIAdminDao.getMasRankById(employeeBloodGroup.getRankId());
	 * responseMap.put("rank", masRank.getRankName()); responseMap.put("rankId",
	 * employeeBloodGroup.getRankId()); }else { responseMap.put("rank", "");
	 * responseMap.put("rankId",""); } if(employeeBloodGroup.getUnitId()!=0) {
	 * MasUnit masUnit= mIAdminDao.getMasUnitById(employeeBloodGroup.getUnitId());
	 * responseMap.put("unit", masUnit.getUnitName()); responseMap.put("unitId",
	 * masUnit.getUnitId()); }else { responseMap.put("unit", "");
	 * responseMap.put("unitId", ""); }
	 * if(employeeBloodGroup.getMasAdministrativeSex()!=null) {
	 * responseMap.put("gender",
	 * employeeBloodGroup.getMasAdministrativeSex().getAdministrativeSexName());
	 * responseMap.put("genderId",
	 * employeeBloodGroup.getMasAdministrativeSex().getAdministrativeSexId()); }
	 * if(employeeBloodGroup.getMasBloodGroup()!=null) {
	 * responseMap.put("bloodGroupId",
	 * employeeBloodGroup.getMasBloodGroup().getBloodGroupId());
	 * responseMap.put("bloodGroupName",
	 * employeeBloodGroup.getMasBloodGroup().getBloodGroupName()); }
	 * if(employeeBloodGroup.getMobileNo()!=null) { responseMap.put("mobileNo",
	 * employeeBloodGroup.getMobileNo()); }
	 * if(employeeBloodGroup.getAddress()!=null) { responseMap.put("address",
	 * employeeBloodGroup.getAddress()); } responseMap.put("msg", "success");
	 * responseMap.put("status", 1); }else { patient=
	 * mIAdminDao.getNameByServiceNo(serviceNo,hospitalId); if(patient!=null) {
	 * 
	 * responseMap.put("name", patient.getEmployeeName());
	 * if(patient.getDateOfBirth()!=null) { responseMap.put("dob",
	 * HMSUtil.changeDateToddMMyyyy(patient.getDateOfBirth()));
	 * responseMap.put("age",HMSUtil.calculateAge(patient.getDateOfBirth())); } else
	 * { responseMap.put("dob", ""); responseMap.put("age",""); }
	 * 
	 * responseMap.put("empBloodId",""); //responseMap.put("empId",
	 * patient.getEmployeeId()); responseMap.put("empId", patient.getPatientId());
	 * if(patient.getMasRank()!=null) { responseMap.put("rank",
	 * patient.getMasRank().getRankName()); responseMap.put("rankId",
	 * patient.getMasRank().getRankId()); }else { responseMap.put("rank", "");
	 * responseMap.put("rankId",""); } if(patient.getMasUnit()!=null) {
	 * responseMap.put("unit", patient.getMasUnit().getUnitName());
	 * responseMap.put("unitId", patient.getMasUnit().getUnitId()); }else {
	 * responseMap.put("unit", ""); responseMap.put("unitId", ""); }
	 * if(patient.getMasAdministrativeSex()!=null) { responseMap.put("gender",
	 * patient.getMasAdministrativeSex().getAdministrativeSexName());
	 * responseMap.put("genderId",
	 * patient.getMasAdministrativeSex().getAdministrativeSexId()); }
	 * responseMap.put("bloodGroupId", 0); responseMap.put("bloodGroupName", "");
	 * 
	 * responseMap.put("mobileNo", ""); responseMap.put("address", "");
	 * 
	 * responseMap.put("msg", "success"); responseMap.put("status", 1); }else {
	 * MasEmployee masEmployee=null; MasUnit masUnit=null; MasRank masRank=null;
	 * 
	 * masEmployee= mIAdminDao.getEmpInfoByServiceNo(serviceNo,hospitalId);
	 * if(masEmployee!=null) {
	 * 
	 * masUnit= mIAdminDao.getMasUnit(masEmployee.getMasUnit()); masRank=
	 * mIAdminDao.getMasRank(masEmployee.getMasRank()); responseMap.put("name",
	 * masEmployee.getEmployeeName()); if(masEmployee.getDob()!=null) {
	 * responseMap.put("dob", HMSUtil.changeDateToddMMyyyy(masEmployee.getDob()));
	 * responseMap.put("age",HMSUtil.calculateAge(masEmployee.getDob())); } else {
	 * responseMap.put("dob", ""); responseMap.put("age",""); }
	 * 
	 * responseMap.put("empBloodId",""); responseMap.put("empId",
	 * masEmployee.getEmployeeId()); if(masRank!=null) { responseMap.put("rank",
	 * masRank.getRankName()); responseMap.put("rankId", masRank.getRankId()); }else
	 * { responseMap.put("rank", ""); responseMap.put("rankId",""); }
	 * if(masUnit!=null) { responseMap.put("unit", masUnit.getUnitName());
	 * responseMap.put("unitId", masUnit.getUnitId()); }else {
	 * responseMap.put("unit", ""); responseMap.put("unitId", ""); }
	 * if(masEmployee.getMasAdministrativeSex()!=null) { responseMap.put("gender",
	 * masEmployee.getMasAdministrativeSex().getAdministrativeSexName());
	 * responseMap.put("genderId",
	 * masEmployee.getMasAdministrativeSex().getAdministrativeSexId()); }
	 * responseMap.put("bloodGroupId", 0); responseMap.put("bloodGroupName", "");
	 * 
	 * responseMap.put("mobileNo", ""); responseMap.put("address", "");
	 * responseMap.put("msg", "success"); responseMap.put("status", 1);
	 * 
	 * } else { responseMap.put("status", 0); responseMap.put("msg",
	 * "Service number is not mapped with the unit"); } } } return responseMap; }
	 * 
	 * @Override public String submitBloodGroupRegister(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); JSONObject
	 * json = new JSONObject(jsondata); EmployeeBloodGroup employeeBloodGroup = new
	 * EmployeeBloodGroup();
	 * 
	 * MasHospital masHospital = null; MasDepartment masDepartment = null; String
	 * empBloodId1 =""; if (jsondata != null) {
	 * 
	 * if (jsondata.get("empBloodId") != null &&
	 * !jsondata.get("empBloodId").toString().isEmpty()) { String empBloodId =
	 * jsondata.get("empBloodId").toString(); empBloodId1=
	 * dispenceryService.getReplaceString(empBloodId);
	 * if(!empBloodId1.equalsIgnoreCase("")) employeeBloodGroup=
	 * mIAdminDao.getEmployeeBloodGroupById(Long.parseLong(empBloodId1)); }
	 * 
	 * if(!jsondata.get("birthDate").toString().isEmpty()) { String birthDate =
	 * jsondata.get("birthDate").toString(); String birthDate1 =
	 * dispenceryService.getReplaceString(birthDate); Date birthDate2 =
	 * HMSUtil.convertStringDateToUtilDate(birthDate1, "dd/MM/yyyy");
	 * employeeBloodGroup.setDateOfBirth(new Timestamp(birthDate2.getTime())); }
	 * 
	 * if (jsondata.get("serviceNo") != null) { String serviceNo =
	 * jsondata.get("serviceNo").toString(); String serviceNo1 =
	 * dispenceryService.getReplaceString(serviceNo);
	 * employeeBloodGroup.setServiceNo(serviceNo1.toUpperCase()); } if
	 * (jsondata.get("unitId") != null) { String unitId =
	 * jsondata.get("unitId").toString(); String unitId1 =
	 * dispenceryService.getReplaceString(unitId);
	 * employeeBloodGroup.setUnitId(Long.parseLong(unitId1)); } if
	 * (jsondata.get("empName") != null) { String empName =
	 * jsondata.get("empName").toString(); String empName1 =
	 * dispenceryService.getReplaceString(empName);
	 * employeeBloodGroup.setEmployeeName(empName1); }
	 * 
	 * 
	 * if (jsondata.get("rankId") != null) { String rankId =
	 * jsondata.get("rankId").toString(); String rankId1 =
	 * dispenceryService.getReplaceString(rankId);
	 * employeeBloodGroup.setRankId(Long.parseLong(rankId1)); } MasAdministrativeSex
	 * masAdministrativeSex =new MasAdministrativeSex(); MasBloodGroup masBloodGroup
	 * =new MasBloodGroup(); if (jsondata.get("genderId") != null) { String genderId
	 * = jsondata.get("genderId").toString(); String genderId1 =
	 * dispenceryService.getReplaceString(genderId);
	 * masAdministrativeSex.setAdministrativeSexId(Long.parseLong(genderId1));
	 * employeeBloodGroup.setMasAdministrativeSex(masAdministrativeSex); } if
	 * (jsondata.get("bloodGroup") != null) { String bloodGroup =
	 * jsondata.get("bloodGroup").toString(); String bloodGroup1 =
	 * dispenceryService.getReplaceString(bloodGroup);
	 * masBloodGroup.setBloodGroupId(Long.parseLong(bloodGroup1));
	 * employeeBloodGroup.setMasBloodGroup(masBloodGroup); } if
	 * (jsondata.get("contactNo") != null) { String contactNo =
	 * jsondata.get("contactNo").toString(); String contactNo1 =
	 * dispenceryService.getReplaceString(contactNo);
	 * employeeBloodGroup.setMobileNo(contactNo1); }
	 * 
	 * if (jsondata.get("address") != null) { String address =
	 * jsondata.get("address").toString(); String address1 =
	 * dispenceryService.getReplaceString(address);
	 * employeeBloodGroup.setAddress(address1); }
	 * 
	 * 
	 * Date date = new Date(); Users users=null;
	 * 
	 * //save data in table if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); }
	 * employeeBloodGroup.setLastChgBy(users); employeeBloodGroup.setLastChgDate(new
	 * Timestamp(date.getTime()));
	 * //injuryRegister.setRicdId(Long.parseLong(ridcId)); long bloodGroupRegisterId
	 * = mIAdminDao.saveOrUpdateEmployeeBloodGroup(employeeBloodGroup);
	 * 
	 * if (bloodGroupRegisterId != 0) { jsonObj.put("bloodGroupRegisterId",
	 * bloodGroupRegisterId); jsonObj.put("status", 1);
	 * if(!empBloodId1.equalsIgnoreCase("")) jsonObj.put("msg",
	 * "Record Updated Successfully"); else jsonObj.put("msg",
	 * "Record Saved Successfully");
	 * 
	 * } else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getMedicalCategory(String jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); int count = 1; Map<String, Object>
	 * map = new HashMap<String, Object>();
	 * 
	 * List<PatientMedicalCat> patientMedicalCatList = new
	 * ArrayList<PatientMedicalCat>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); responseMap =
	 * mIAdminDao.getMedicalCategorylistForHIVstd(jsondata); if (responseMap.size()
	 * > 0 && !responseMap.isEmpty()) { patientMedicalCatList =
	 * (List<PatientMedicalCat>) responseMap.get("patientMedicalCatList"); if
	 * (patientMedicalCatList.size() > 0 && !responseMap.isEmpty()) {
	 * for(PatientMedicalCat patientMedicalCat : patientMedicalCatList) {
	 * Map<String, Object> map1 = new HashMap<String, Object>(); map1.put("pId",
	 * patientMedicalCat.getMedicalCatId());
	 * map1.put("icd",patientMedicalCat.getMasIcd().getIcdName());
	 * map1.put("icdId",patientMedicalCat.getMasIcd().getIcdId());
	 * map1.put("medicalCategory",
	 * patientMedicalCat.getMasMedicalCategory().getMedicalCategoryId());
	 * map1.put("system", patientMedicalCat.getSystem()); map1.put("categoryType",
	 * patientMedicalCat.getCategoryType()); map1.put("duration",
	 * patientMedicalCat.getDuration());
	 * map1.put("categoryDate",HMSUtil.changeDateToddMMyyyy(patientMedicalCat.
	 * getCategoryDate()));
	 * map1.put("categoryNextDate",HMSUtil.changeDateToddMMyyyy(patientMedicalCat.
	 * getNextCategoryDate())); if(patientMedicalCat.getMarkFlag()!=null &&
	 * patientMedicalCat.getMarkFlag().equalsIgnoreCase("Y")) map1.put("hivmark",
	 * "yes"); else map1.put("hivmark", "no");
	 * 
	 * data.put(count++, map1);
	 * 
	 * map.put("data", data); map.put("count", count - 1); } } else {
	 * map.put("data", ""); map.put("count", 0); } }
	 * 
	 * 
	 * return map; }
	 * 
	 * @Override public Map<String, Object> getRankList() { // TODO Auto-generated
	 * method stub Map<String, Object> map1 = new HashMap<String, Object>();
	 * 
	 * Map<String, Long> map = new LinkedHashMap<String, Long>(); List<MasRank>
	 * masRankList = new ArrayList<MasRank>();
	 * 
	 * masRankList = mIAdminDao.getRankList(); for (MasRank MasRank : masRankList) {
	 * map.put(MasRank.getRankName(), MasRank.getRankId()); } if (masRankList !=
	 * null && masRankList.size() > 0) { map1.put("data", map); map1.put("status",
	 * 1); } else { map1.put("data", map); map1.put("msg", "Data not found");
	 * map1.put("status", 0); }
	 * 
	 * return map1; }
	 * 
	 * @Override public Map<String, Object>
	 * getDetailsForHospitalRegister(HashMap<String, String> requestData,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub List<HospitalVisitRegister> hospitalVisitList =
	 * new ArrayList<HospitalVisitRegister>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); List<Object[]> objectArrayList = new
	 * ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * 
	 * Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String,
	 * Object>>(); Map<Integer, Map<String, Object>> visitorMap = new
	 * HashMap<Integer, Map<String, Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); long pageNo=0;
	 * 
	 * 
	 * responseMap = mIAdminDao.getHospitalVisitDetails(requestData); int count = 1;
	 * if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * hospitalVisitList = (List<HospitalVisitRegister>)
	 * responseMap.get("hospitalVisitRegisterList"); if
	 * (!hospitalVisitList.isEmpty() && hospitalVisitList.size() > 0) {
	 * for(HospitalVisitRegister hospitalVisitRegister : hospitalVisitList) {
	 * Map<String,Object>map1=new HashMap<String, Object>();
	 * if(hospitalVisitRegister.getPatient().getServiceNo()!=null)
	 * map1.put("serviceNo", hospitalVisitRegister.getPatient().getServiceNo());
	 * else map1.put("serviceNo","");
	 * map1.put("hospitalVisitId",hospitalVisitRegister.getHostpitalVisitRegisterId(
	 * )); if(hospitalVisitRegister.getPatient().getMasRank().getRankName()!=null)
	 * map1.put("rank",
	 * hospitalVisitRegister.getPatient().getMasRank().getRankName()); else
	 * map1.put("rank","");
	 * if(hospitalVisitRegister.getPatient().getEmployeeName()!=null)
	 * map1.put("empName", hospitalVisitRegister.getPatient().getEmployeeName());
	 * else map1.put("empName","");
	 * if(hospitalVisitRegister.getPatient().getDateOfBirth()!=null)
	 * map1.put("age",HMSUtil.calculateAge(hospitalVisitRegister.getPatient().
	 * getDateOfBirth())); else map1.put("age", "");
	 * if(hospitalVisitRegister.getIcdDiagnosis()!=null)
	 * map1.put("diagnosis",hospitalVisitRegister.getIcdDiagnosis()); else
	 * map1.put("diagnosis", ""); if(hospitalVisitRegister.getWardName()!=null)
	 * map1.put("wardName",hospitalVisitRegister.getWardName()); else
	 * map1.put("wardName",""); if(hospitalVisitRegister.getHospitalName()!=null)
	 * map1.put("hospitalName", hospitalVisitRegister.getHospitalName()); else
	 * map1.put("hospitalName", "");
	 * if(hospitalVisitRegister.getVisitRemarksDivOfficer()!=null)
	 * map1.put("medChief", hospitalVisitRegister.getVisitRemarksDivOfficer()); else
	 * map1.put("medChief", ""); if(hospitalVisitRegister.getVisitByMedDept()!=null)
	 * map1.put("medDept",hospitalVisitRegister.getVisitByMedDept()); else
	 * map1.put("medDept", "");
	 * 
	 * if(hospitalVisitRegister.getRemarks()!=null)
	 * map1.put("PMOremarks",hospitalVisitRegister.getRemarks()); else
	 * map1.put("PMOremarks", "");
	 * if(hospitalVisitRegister.getCaptainRemark()!=null) map1.put("captainRemarks",
	 * hospitalVisitRegister.getCaptainRemark()); else map1.put("captainRemarks",
	 * "");
	 * 
	 * if(hospitalVisitRegister.getHospitalVisitDate()!=null) map1.put("date",
	 * HMSUtil.changeDateToddMMyyyy(hospitalVisitRegister.getHospitalVisitDate()));
	 * else map1.put("date", "");
	 * 
	 * if(hospitalVisitRegister.getPatient().getDateOfBirth()!=null) map1.put("dob",
	 * HMSUtil.changeDateToddMMyyyy(hospitalVisitRegister.getPatient().
	 * getDateOfBirth())); else map1.put("dob", "");
	 * if(hospitalVisitRegister.getPatient().getMasAdministrativeSex()!=null)
	 * map1.put("gender",
	 * hospitalVisitRegister.getPatient().getMasAdministrativeSex().
	 * getAdministrativeSexName()); else map1.put("gender", "");
	 * 
	 * List<VisitorDetail> visitorDetailList; String visitor=""; String
	 * visitornameAndRank=""; String remark="";
	 * if(hospitalVisitRegister.getVisitorDetail()!=null) {
	 * visitorDetailList=hospitalVisitRegister.getVisitorDetail(); for
	 * (VisitorDetail visitorDetail : visitorDetailList) {
	 * Map<String,Object>map2=new HashMap<String, Object>();
	 * visitor=visitor+","+visitorDetail.getVisitorName();
	 * visitornameAndRank=visitornameAndRank+","+visitorDetail.getRankAndName();
	 * remark=remark+","+visitorDetail.getRemarks(); map2.put("visitorType",
	 * visitorDetail.getVisitorName()); map2.put("visitorName",
	 * visitorDetail.getRankAndName()); map2.put("remark",
	 * visitorDetail.getRemarks()); visitorMap.put(count++, map2); }
	 * map1.put("visitor", visitor.substring(1)); map1.put("visitornameAndRank",
	 * visitornameAndRank.substring(1)); map1.put("remark", remark.substring(1)); }
	 * data.put(1, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } map.put("data", data); map.put("visitorData", visitorMap); map.put("count",
	 * count-1); map.put("status", "1"); return map; }
	 * 
	 * 
	 */
}
