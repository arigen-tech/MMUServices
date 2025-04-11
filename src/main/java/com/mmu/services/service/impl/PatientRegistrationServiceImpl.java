package com.mmu.services.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mmu.services.dao.CommonDao;
import com.mmu.services.dao.DgNormalValueDao;
import com.mmu.services.dao.DgOrderhdDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.dao.impl.OpdMasterDaoImpl;
import com.mmu.services.entity.DgNormalValue;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.MMUDepartment;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasAppointmentType;
import com.mmu.services.entity.MasBloodGroup;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasCommand;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasEmployeeDependent;
import com.mmu.services.entity.MasIdentificationType;
import com.mmu.services.entity.MasLabor;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasMaritalStatus;
import com.mmu.services.entity.MasMedExam;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRecordOfficeAddress;
import com.mmu.services.entity.MasRegistrationType;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasReligion;
import com.mmu.services.entity.MasServiceType;
import com.mmu.services.entity.MasState;
import com.mmu.services.entity.MasTrade;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.MasWard;
import com.mmu.services.entity.MasZone;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDataUpload;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.PatientRegistrationService;
import com.mmu.services.service.impl.OpdServiceImpl;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;
import com.mmu.services.utils.ValidateUtils;



@Service("PatientRegistrationService")
public class PatientRegistrationServiceImpl implements PatientRegistrationService {

	@Autowired
	private Environment environment;
	
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	PatientRegistrationDao patientRegistrationDao;

	@Autowired
	OpdMasterDaoImpl opdMasterDaoImpl;
	@Autowired
	MedicalExamDAO medicalExamDAO;
	@Autowired
	CommonDao cd;
	@Autowired
	DgNormalValueDao dgNormalValueDao;
	@Autowired
	DgOrderhdDao dgOrderhdDao;

	/*
	 * @SuppressWarnings({ "unchecked", "unused" })
	 * 
	 * @Override public Map<String, Object>
	 * findPatientAndDependentFromEmployee(Map<String, String> requestData) {
	 * Map<String, Object> map = new HashMap<String, Object>(); Map<String, Object>
	 * patientListofEmpAndDependent = new HashMap<String, Object>(); Map<Integer,
	 * Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
	 * List<Patient> existingPatientList= new ArrayList<Patient>();
	 * List<MasEmployee> empAndDependentPatientList= new ArrayList<MasEmployee>();
	 * List<MasEmployeeDependent> dependentPatientList= new
	 * ArrayList<MasEmployeeDependent>();
	 * 
	 * 
	 * 
	 * long empRankId = 0; long empTradeId = 0; String empTotalService = "0"; long
	 * empUnitId = 0; long empCommandId = 0; long empRecordOfficeId = 0; long
	 * empMaritalStatusId = 0; long empReligionId = 0; long relationId = 0; long
	 * genderId=0; long stateId=0; long nok1RelationId=0; long nok2RelationId=0;
	 * long patientBloodGroupId=0; long empMedicalCategoryId=0; long employeeId=0;
	 * long empCategoryId=0;
	 * 
	 * 
	 * String dateOfBirth = ""; String dateME=""; String empServiceJoinDate="";
	 * String age = "0";
	 * 
	 * String name = ""; String relation = ""; String gender = ""; String empName =
	 * ""; String serviceNo = ""; String empRank=""; String empTradeName=""; String
	 * empUnitName=""; String empCommandName=""; String empRecordOfficeName="";
	 * String empMaritalStatusName=""; String empReligion="";
	 * 
	 * 
	 * String addressL1=""; String addressL2 =""; String addressL3 =""; String
	 * addressL4="";
	 * 
	 * long districtId =0; String districtName=""; Long patientPincode= null ;
	 * 
	 * String empAge="0"; String empDepartmentName="";
	 * 
	 * 
	 * String stateName=""; String mobileNumber = ""; String patientEmailId="";
	 * String patientBloodGroup="";
	 * 
	 * String nok1Name=""; String nok1Relation=""; String nok1ContactNo=""; String
	 * nok1AddressL1=""; String nok1AddressL2 =""; String nok1AddressL3=""; String
	 * nok1AddressL4 ="";
	 * 
	 * long nok1DistrictId = 0; String nok1DistrictName = ""; long nok1StateId = 0;
	 * String nok1StateName = "";
	 * 
	 * Long nok1Pincode=null; String nok1PoliceStation="";
	 * 
	 * String nok1MobileNo=""; String nok1EamilId="";
	 * 
	 * String nok2Name=""; String nok2Relation=""; String nok2ContactNo=""; String
	 * nok2AddressL1=""; String nok2AddressL2 =""; String nok2AddressL3=""; String
	 * nok2AddressL4 =""; String nok2PoliceStation=""; Long nok2Pincode=null;
	 * 
	 * long nok2DistrictId = 0; String nok2DistrictName = ""; long nok2StateId = 0;
	 * String nok2StateName = "";
	 * 
	 * String nok2MobileNo=""; String nok2EamilId=""; String empMedicalCategory="";
	 * MasEmployee employeeObject =null; try { if (!requestData.isEmpty() &&
	 * requestData != null ) {
	 * 
	 * if(requestData.get("flagForDigi")!=null &&
	 * requestData.containsKey("flagForDigi")) { String selfRelationCode =
	 * HMSUtil.getProperties("adt.properties", "SELF_RELATION_CODE").trim(); Long
	 * selfRelationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * if (requestData.get("serviceNo") != null &&
	 * !requestData.get("serviceNo").isEmpty()) serviceNo =
	 * requestData.get("serviceNo").trim();
	 * List<Patient>listPatient=patientRegistrationDao.findPatientByServiceNumber(
	 * serviceNo,selfRelationId); int rowCount=0;
	 * if(CollectionUtils.isNotEmpty(listPatient)) { Patient
	 * patient=listPatient.get(0); data=getExistPatientDetailByPatient(patient,
	 * relation , selfRelationId, empName, serviceNo, employeeId, empRankId,
	 * empRank, empTradeId, empTradeName, empTotalService, empServiceJoinDate,
	 * empAge, age, dateOfBirth, empUnitId, empUnitName, empCommandId,
	 * empCommandName, empRecordOfficeId, empRecordOfficeName, empMaritalStatusId,
	 * empMaritalStatusName, empCategoryId, empDepartmentName, name, genderId,
	 * gender, mobileNumber, patientEmailId, patientBloodGroup, patientBloodGroupId,
	 * rowCount, data); }
	 * 
	 * if(CollectionUtils.isEmpty(listPatient)) { employeeObject =
	 * patientRegistrationDao.getMasEmployeeFromServiceNo(serviceNo);
	 * if(employeeObject!=null) { data=getNewPatientDetal(employeeObject, relation ,
	 * selfRelationId, empName, serviceNo, employeeId, empRankId, empRank,
	 * empTradeId, empTradeName, empTotalService, empServiceJoinDate, empAge, age,
	 * dateOfBirth, empUnitId, empUnitName, empCommandId, empCommandName,
	 * empRecordOfficeId, empRecordOfficeName, empMaritalStatusId,
	 * empMaritalStatusName, empCategoryId, empDepartmentName, name, genderId,
	 * gender, mobileNumber, patientEmailId, patientBloodGroup, patientBloodGroupId,
	 * rowCount, data); } else { map.put("msg","Service No. does not exist.");
	 * map.put("count", data.size()); map.put("status", "0"); return map; }
	 * 
	 * } map.put("data", data); map.put("count", data.size());
	 * map.put("msg","List of Detail"); map.put("status", "1"); return map; }
	 * 
	 * else { if (requestData.get("serviceNo") != null &&
	 * !requestData.get("serviceNo").isEmpty()) { serviceNo =
	 * requestData.get("serviceNo").trim(); employeeObject =
	 * patientRegistrationDao.getMasEmployeeFromServiceNo(serviceNo);
	 * if(employeeObject!=null) { patientListofEmpAndDependent =
	 * patientRegistrationDao.findPatientAndDependentFromEmployee(serviceNo);
	 * 
	 * if(patientListofEmpAndDependent.size()>0) { existingPatientList =
	 * (List<Patient>)patientListofEmpAndDependent.get("patientList");
	 * if(existingPatientList.size()>0) { int rowCount=0; for(Patient patient :
	 * existingPatientList) { Map<String, Object> responsePatientMap = new
	 * HashMap<String, Object>(); String patientName = ""; if
	 * (patient.getPatientName() != null) { patientName = patient.getPatientName();
	 * }
	 * 
	 * // Employee related details serviceNo= patient.getServiceNo();
	 * 
	 * if(patient.getMasRank()!=null) { empRankId= patient.getMasRank().getRankId();
	 * empRank = patient.getMasRank().getRankName(); }else { empRankId= 0; empRank =
	 * ""; }
	 * 
	 * if(patient.getMasTrade()!=null) { empTradeId=
	 * patient.getMasTrade().getTradeId(); empTradeName=
	 * patient.getMasTrade().getTradeName(); }else { empTradeId= 0; empTradeName=
	 * ""; }
	 * 
	 * empName= patient.getEmployeeName(); employeeId
	 * =employeeObject.getEmployeeId();
	 * 
	 * if(patient.getServiceJoinDate()!=null) { empTotalService=
	 * HMSUtil.calculateAge(patient.getServiceJoinDate());
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(patient.getServiceJoinDate())
	 * ; }else { empTotalService= "0"; empServiceJoinDate=""; }
	 * 
	 * if(patient.getMasUnit()!=null) { empUnitId=patient.getMasUnit().getUnitId();
	 * empUnitName=patient.getMasUnit().getUnitName(); }else { empUnitId=0;
	 * empUnitName=""; }
	 * 
	 * if(patient.getMasCommand()!=null) {
	 * empCommandId=patient.getMasCommand().getCommandId();
	 * empCommandName=patient.getMasCommand().getCommandName(); }else {
	 * empCommandId=0; empCommandName=""; }
	 * 
	 * if(patient.getMasRecordOfficeAddress()!=null) {
	 * empRecordOfficeId=patient.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressId();
	 * empRecordOfficeName=patient.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressName(); }else { empRecordOfficeId=0;
	 * empRecordOfficeName=""; }
	 * 
	 * if(patient.getMasMaritalStatus()!=null) {
	 * empMaritalStatusId=patient.getMasMaritalStatus().getMaritalStatusId();
	 * empMaritalStatusName=patient.getMasMaritalStatus().getMaritalStatusName();
	 * }else { empMaritalStatusId=0; empMaritalStatusName=""; }
	 * 
	 * if(patient.getMasReligion()!=null) {
	 * empReligionId=patient.getMasReligion().getReligionId();
	 * empReligion=patient.getMasReligion().getReligionName(); }else {
	 * empReligionId=0; empReligion=""; }
	 * 
	 * 
	 * 
	 * 
	 * empCategoryId =
	 * patient.getMasEmployeeCategory()!=null?patient.getMasEmployeeCategory().
	 * getEmployeeCategoryId():0;
	 * 
	 * 
	 * String relationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE");
	 * 
	 * if(relationCode.equalsIgnoreCase(patient.getMasRelation().getRelationCode()))
	 * { if(patient.getDateOfBirth()!=null) {
	 * empAge=HMSUtil.calculateAge(patient.getDateOfBirth()); }else { empAge="0"; }
	 * 
	 * 
	 * //Medical category and Date ME only for employee patient
	 * 
	 * if(patient.getMasMedicalCategory()!=null) {
	 * empMedicalCategory=patient.getMasMedicalCategory().getMedicalCategoryName();
	 * empMedicalCategoryId=patient.getMasMedicalCategory().getMedicalCategoryId();
	 * 
	 * }else { empMedicalCategory=""; empMedicalCategoryId=0; }
	 * 
	 * empMedicalCategory =
	 * patientRegistrationDao.getMasMedicalCategoryFromDBFunction(patient.
	 * getPatientId(),LocalDate.now());
	 * 
	 * dateME=(patient.getDateMe()!=null?HMSUtil.changeDateToddMMyyyy(patient.
	 * getDateMe()):"");
	 * 
	 * }else { if(employeeObject.getDob()!=null) {
	 * empAge=HMSUtil.calculateAge(employeeObject.getDob()); }else { empAge="0"; }
	 * 
	 * }
	 * 
	 * String empRankCode = employeeObject.getMasRank()!=null?
	 * employeeObject.getMasRank():"0"; List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(mRankList.size()>0) {
	 * empDepartmentName=mRankList.get(0).getMasTrade()!=null?mRankList.get(0).
	 * getMasTrade().getTradeName():""; }else { empDepartmentName=""; }
	 * 
	 * 
	 * // patient related details name = patientName;
	 * if(patient.getDateOfBirth()!=null) { age =
	 * HMSUtil.calculateAge(patient.getDateOfBirth()); dateOfBirth =
	 * HMSUtil.changeDateToddMMyyyy(patient.getDateOfBirth()); }else { age="0";
	 * dateOfBirth=""; }
	 * 
	 * if(patient.getMasAdministrativeSex()!=null) { gender =
	 * patient.getMasAdministrativeSex().getAdministrativeSexName(); genderId =
	 * patient.getMasAdministrativeSex().getAdministrativeSexId(); }else { gender =
	 * ""; genderId = 0; }
	 * 
	 * if(patient.getMasRelation()!=null) { relation =
	 * patient.getMasRelation().getRelationName();
	 * relationId=patient.getMasRelation().getRelationId(); }else { relation = "";
	 * relationId= 0; }
	 * 
	 * 
	 * mobileNumber=patient.getMobileNumber();
	 * 
	 * 
	 * addressL1 = patient.getAddressLine1()!=null?patient.getAddressLine1():"";
	 * addressL2 = patient.getAddressLine2()!=null?patient.getAddressLine2():"";
	 * addressL3 = patient.getAddressLine3()!=null?patient.getAddressLine3():"";
	 * addressL4 = patient.getAddressLine4()!=null?patient.getAddressLine4():"";
	 * districtId =
	 * patient.getMasDistrict()!=null?patient.getMasDistrict().getDistrictId():0;
	 * districtName =
	 * patient.getMasDistrict()!=null?patient.getMasDistrict().getDistrictName():"";
	 * 
	 * if(patient.getMasState()!=null) { stateId =
	 * patient.getMasState().getStateId(); stateName =
	 * patient.getMasState().getStateName();
	 * 
	 * }else { stateId = 0; stateName = "";
	 * 
	 * }
	 * 
	 * 
	 * patientPincode = patient.getPincode()!=null?patient.getPincode():0;
	 * patientEmailId=patient.getEmailId()!=null?patient.getEmailId():"";
	 * 
	 * if(patient.getMasBloodGroup()!=null) { patientBloodGroup =
	 * patient.getMasBloodGroup().getBloodGroupName(); patientBloodGroupId =
	 * patient.getMasBloodGroup().getBloodGroupId(); }else { patientBloodGroup = "";
	 * patientBloodGroupId = 0; }
	 * 
	 * 
	 * 
	 * // NOK1 related details
	 * 
	 * nok1Name = patient.getNok1Name()!=null?patient.getNok1Name():"";
	 * 
	 * if(patient.getMasRelationNok1()!=null) { nok1RelationId =
	 * patient.getMasRelationNok1().getRelationId();
	 * nok1Relation=patient.getMasRelationNok1().getRelationName(); }else {
	 * nok1RelationId = 0; nok1Relation=""; }
	 * 
	 * nok1ContactNo=patient.getNok1ContactNo()!=null?patient.getNok1ContactNo():"";
	 * 
	 * 
	 * nok1AddressL1 =
	 * patient.getNok1AddressLine1()!=null?patient.getNok1AddressLine1():"";
	 * nok1AddressL2 =
	 * patient.getNok1AddressLine2()!=null?patient.getNok1AddressLine2():"";
	 * nok1AddressL3 =
	 * patient.getNok1AddressLine3()!=null?patient.getNok1AddressLine3():"";
	 * nok1AddressL4 =
	 * patient.getNok1AddressLine4()!=null?patient.getNok1AddressLine4():"";
	 * 
	 * if(patient.getNok1Distrcit()!=null) { nok1DistrictId =
	 * patient.getNok1Distrcit().getDistrictId(); nok1DistrictName =
	 * patient.getNok1Distrcit().getDistrictName(); }else { nok1DistrictId = 0;
	 * nok1DistrictName = ""; }
	 * 
	 * if(patient.getNok1State()!=null) { nok1StateId =
	 * patient.getNok1State().getStateId(); nok1StateName =
	 * patient.getNok1State().getStateName(); }else { nok1StateId = 0; nok1StateName
	 * = ""; }
	 * 
	 * 
	 * nok1PoliceStation=patient.getNok1PoliceStation()!=null?patient.
	 * getNok1PoliceStation():"";
	 * nok1Pincode=patient.getNok1PinCode()!=null?patient.getNok1PinCode():0;
	 * nok1MobileNo=patient.getNok1MobileNo(); nok1EamilId=patient.getNok1EmailId();
	 * 
	 * 
	 * // NOK2 related details
	 * 
	 * nok2Name=patient.getNok2Name()!=null?patient.getNok2Name():"";
	 * 
	 * if(patient.getMasRelationNok2()!=null) {
	 * nok2RelationId=patient.getMasRelationNok2().getRelationId();
	 * nok2Relation=patient.getMasRelationNok2().getRelationName(); }else {
	 * nok2RelationId=0; nok2Relation=""; }
	 * 
	 * 
	 * nok2ContactNo=(patient.getNok2ContactNo()!=null?patient.getNok2ContactNo():""
	 * );
	 * 
	 * 
	 * nok2AddressL1 =
	 * patient.getNok2AddressLine1()!=null?patient.getNok2AddressLine1():"";
	 * nok2AddressL2 =
	 * patient.getNok2AddressLine2()!=null?patient.getNok2AddressLine2():"";
	 * nok2AddressL3 =
	 * patient.getNok2AddressLine3()!=null?patient.getNok2AddressLine3():"";
	 * nok2AddressL4 =
	 * patient.getNok2AddressLine4()!=null?patient.getNok2AddressLine4():"";
	 * 
	 * if(patient.getNok2Distrcit()!=null) { nok2DistrictId =
	 * patient.getNok2Distrcit().getDistrictId(); nok2DistrictName =
	 * patient.getNok2Distrcit().getDistrictName(); }else { nok2DistrictId = 0;
	 * nok2DistrictName = ""; }
	 * 
	 * if(patient.getNok2State()!=null) { nok2StateId =
	 * patient.getNok2State().getStateId(); nok2StateName
	 * =patient.getNok2State().getStateName(); }else { nok2StateId = 0;
	 * nok2StateName = ""; }
	 * 
	 * 
	 * nok2PoliceStation=patient.getNok2PoliceStation()!=null?patient.
	 * getNok2PoliceStation():"";
	 * nok2Pincode=patient.getNok2PinCode()!=null?patient.getNok2PinCode():0;
	 * 
	 * 
	 * 
	 * nok2MobileNo=patient.getNok2MobileNo()!=null?patient.getNok2MobileNo():"";
	 * nok2EamilId=patient.getNok2EmailId()!=null?patient.getNok2EmailId():"";
	 * 
	 * 
	 * responsePatientMap.put("patientId", patient.getPatientId());
	 * responsePatientMap.put("Id", ++rowCount);
	 * responsePatientMap.put("employeeId",employeeId);
	 * responsePatientMap.put("uhidNo", patient.getUhidNo());
	 * responsePatientMap.put("name", name); responsePatientMap.put("age", age);
	 * responsePatientMap.put("gender", gender); responsePatientMap.put("genderId",
	 * genderId); responsePatientMap.put("dateOfBirth", dateOfBirth);
	 * responsePatientMap.put("relation", relation);
	 * responsePatientMap.put("relationId", relationId);
	 * responsePatientMap.put("mobileNumber",mobileNumber);
	 * 
	 * responsePatientMap.put("patientAddressL1",addressL1);
	 * responsePatientMap.put("patientAddressL2",addressL2);
	 * responsePatientMap.put("patientAddressL3",addressL3);
	 * responsePatientMap.put("patientAddressL4",addressL4);
	 * responsePatientMap.put("patientDistrictId",districtId);
	 * responsePatientMap.put("patientDistrictName",districtName);
	 * 
	 * responsePatientMap.put("patientStateId",stateId);
	 * responsePatientMap.put("patientStateName",stateName);
	 * responsePatientMap.put("patientPincode",patientPincode);
	 * responsePatientMap.put("patientEmailId",patientEmailId);
	 * responsePatientMap.put("patientBloodGroup",patientBloodGroup);
	 * responsePatientMap.put("patientBloodGroupId",patientBloodGroupId);
	 * 
	 * responsePatientMap.put("empMedicalCategory",empMedicalCategory);
	 * responsePatientMap.put("empMedicalCategoryId",empMedicalCategoryId);
	 * responsePatientMap.put("dateME",dateME);
	 * 
	 * responsePatientMap.put("serviceNo",serviceNo);
	 * responsePatientMap.put("empRankId",empRankId);
	 * responsePatientMap.put("empAge",empAge);
	 * responsePatientMap.put("empRank",empRank);
	 * responsePatientMap.put("empTradeId",empTradeId);
	 * responsePatientMap.put("empTradeName",empTradeName);
	 * responsePatientMap.put("empName",empName);
	 * responsePatientMap.put("empTotalService",empTotalService);
	 * responsePatientMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responsePatientMap.put("empUnitId",empUnitId);
	 * responsePatientMap.put("empUnitName",empUnitName);
	 * responsePatientMap.put("empCommandId",empCommandId);
	 * responsePatientMap.put("empCommandName",empCommandName);
	 * responsePatientMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responsePatientMap.put("empRecordOfficeName",empRecordOfficeName);
	 * responsePatientMap.put("empDepartmentName",empDepartmentName);
	 * responsePatientMap.put("empCategoryId",empCategoryId);
	 * 
	 * responsePatientMap.put("empMaritalStatusId",empMaritalStatusId);
	 * responsePatientMap.put("empMaritalStatusName",empMaritalStatusName);
	 * responsePatientMap.put("empReligionId",empReligionId);
	 * responsePatientMap.put("empReligion",empReligion);
	 * 
	 * responsePatientMap.put("nok1Name",nok1Name);
	 * responsePatientMap.put("nok1RelationId",nok1RelationId);
	 * responsePatientMap.put("nok1Relation",nok1Relation);
	 * responsePatientMap.put("nok1ContactNo",nok1ContactNo);
	 * 
	 * responsePatientMap.put("nok1AddressL1",nok1AddressL1);
	 * responsePatientMap.put("nok1AddressL2",nok1AddressL2);
	 * responsePatientMap.put("nok1AddressL3",nok1AddressL3);
	 * responsePatientMap.put("nok1AddressL4",nok1AddressL4);
	 * 
	 * responsePatientMap.put("nok1DistrictId",nok1DistrictId);
	 * responsePatientMap.put("nok1DistrictName",nok1DistrictName);
	 * responsePatientMap.put("nok1StateId",nok1StateId);
	 * responsePatientMap.put("nok1StateName",nok1StateName);
	 * 
	 * responsePatientMap.put("nok1PoliceStation",nok1PoliceStation);
	 * responsePatientMap.put("nok1Pincode",nok1Pincode);
	 * responsePatientMap.put("nok1MobileNo",nok1MobileNo);
	 * responsePatientMap.put("nok1EamilId",nok1EamilId);
	 * 
	 * responsePatientMap.put("nok2Name",nok2Name);
	 * responsePatientMap.put("nok2RelationId",nok2RelationId);
	 * responsePatientMap.put("nok2Relation",nok2Relation);
	 * responsePatientMap.put("nok2ContactNo",nok2ContactNo);
	 * 
	 * responsePatientMap.put("nok2AddressL1",nok2AddressL1);
	 * responsePatientMap.put("nok2AddressL2",nok2AddressL2);
	 * responsePatientMap.put("nok2AddressL3",nok2AddressL3);
	 * responsePatientMap.put("nok2AddressL4",nok2AddressL4);
	 * 
	 * 
	 * responsePatientMap.put("nok2DistrictId",nok2DistrictId);
	 * responsePatientMap.put("nok2DistrictName",nok2DistrictName);
	 * responsePatientMap.put("nok2StateId",nok2StateId);
	 * responsePatientMap.put("nok2StateName",nok2StateName);
	 * 
	 * responsePatientMap.put("nok2PoliceStation",nok2PoliceStation);
	 * responsePatientMap.put("nok2Pincode",nok2Pincode);
	 * responsePatientMap.put("nok2MobileNo",nok2MobileNo);
	 * responsePatientMap.put("nok2EamilId",nok2EamilId);
	 * responsePatientMap.put("patientStatus",
	 * patient.getPatientStatus()!=null?patient.getPatientStatus():"");
	 * 
	 * data.put(++rowCount, responsePatientMap); } // first dependentPatientList =
	 * (List<MasEmployeeDependent>)patientListofEmpAndDependent.get(
	 * "employeeDependentList"); if(dependentPatientList.size()>0 ) {
	 * for(MasEmployeeDependent depList :dependentPatientList ) {
	 * 
	 * Map<String, Object> responseDepMap = new HashMap<String, Object>(); String
	 * depPatientName = ""; if (depList.getEmployeeDependentName() != null) {
	 * depPatientName = depList.getEmployeeDependentName(); }
	 * 
	 * //Patient related data name = depPatientName;
	 * 
	 * if(depList.getDateOfBirth()!=null) { age =
	 * HMSUtil.calculateAge(depList.getDateOfBirth());
	 * dateOfBirth=HMSUtil.changeDateToddMMyyyy(depList.getDateOfBirth()); }else {
	 * age="0"; dateOfBirth=""; }
	 * 
	 * if(depList.getMasAdministrativeSex()!=null) { gender =
	 * depList.getMasAdministrativeSex().getAdministrativeSexName(); genderId =
	 * depList.getMasAdministrativeSex().getAdministrativeSexId(); }else { gender =
	 * ""; genderId = 0; }
	 * 
	 * if(depList.getMasRelation()!=null) { relation =
	 * depList.getMasRelation().getRelationName();
	 * relationId=depList.getMasRelation().getRelationId(); }else { relation = "";
	 * relationId= 0; }
	 * 
	 * mobileNumber=""; addressL1 =
	 * depList.getAddresLine1()!=null?depList.getAddresLine1():""; addressL2 =
	 * depList.getAddresLine2()!=null?depList.getAddresLine2():""; addressL3 =
	 * depList.getAddresLine3()!=null?depList.getAddresLine3():""; addressL4 =
	 * depList.getAddresLine4()!=null?depList.getAddresLine4():"";
	 * 
	 * if(depList.getMasDistrict()!=null) { districtId =
	 * depList.getMasDistrict().getDistrictId(); districtName =
	 * depList.getMasDistrict().getDistrictName(); }else { districtId = 0;
	 * districtName = ""; }
	 * 
	 * if(depList.getMasState()!=null) { stateId =
	 * depList.getMasState().getStateId(); stateName =
	 * depList.getMasState().getStateName(); }else { stateId = 0; stateName = ""; }
	 * 
	 * 
	 * patientPincode =
	 * depList.getPincode()!=null?Long.parseLong(depList.getPincode()):0;
	 * patientEmailId=employeeObject.getEmail()!=null?employeeObject.getEmail():"";
	 * // This may be change in future
	 * 
	 * patientBloodGroup = ""; patientBloodGroupId = 0;
	 * 
	 * //Employee related
	 * 
	 * serviceNo= employeeObject.getServiceNo();
	 * 
	 * 
	 * String empRankCode = employeeObject.getMasRank()!=null?
	 * employeeObject.getMasRank():"0"; List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0) { empRankId =
	 * mRankList.get(0).getRankId(); empRank = mRankList.get(0).getRankName();
	 * 
	 * if(mRankList.get(0).getMasTrade()!=null) { empTradeId =
	 * mRankList.get(0).getMasTrade().getTradeId(); empTradeName =
	 * mRankList.get(0).getMasTrade().getTradeName();
	 * empDepartmentName=empTradeName; // we will show Trade Name In place of
	 * Department Name }else { empTradeId = 0; empTradeName = "";
	 * empDepartmentName=""; }
	 * 
	 * }else { empRankId = 0; empRank = ""; empTradeId = 0; empTradeName = ""; }
	 * 
	 * 
	 * empName=
	 * employeeObject.getEmployeeName()!=null?employeeObject.getEmployeeName():"";
	 * employeeId = employeeObject.getEmployeeId();
	 * if(employeeObject.getDoe()!=null) { empTotalService=
	 * HMSUtil.calculateAge(employeeObject.getDoe());
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(employeeObject.getDoe());
	 * }else { empTotalService= "0"; empServiceJoinDate=""; }
	 * 
	 * // This getMasUnit() gives unitCode not unit object. String empUnitCode =
	 * employeeObject.getMasUnit()!=null?employeeObject.getMasUnit():"0";
	 * List<MasUnit> unitList = patientRegistrationDao.getEmpUnitId(empUnitCode);
	 * if(unitList.size()>0) { empUnitId = unitList.get(0).getUnitId();
	 * empUnitName=unitList.get(0).getUnitName();
	 * 
	 * if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id
	 * empCommandName=unitList.get(0).getMasStation().getMasCommand().getCommandName
	 * (); // This is station Name }else { empCommandId=0; empCommandName=""; }
	 * 
	 * }else { empUnitId = 0; empUnitName=""; }
	 * 
	 * if(employeeObject.getMasRecordOfficeAddress()!=null) {
	 * empRecordOfficeId=employeeObject.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressId();
	 * empRecordOfficeName=employeeObject.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressName(); }else { empRecordOfficeId=0;
	 * empRecordOfficeName=""; }
	 * 
	 * if(employeeObject.getMasMaritalStatus()!=null) {
	 * empMaritalStatusId=employeeObject.getMasMaritalStatus().getMaritalStatusId();
	 * empMaritalStatusName=employeeObject.getMasMaritalStatus().
	 * getMaritalStatusName(); }else { empMaritalStatusId=0;
	 * empMaritalStatusName=""; }
	 * 
	 * empCategoryId=employeeObject.getMasEmployeeCategory()!=null?employeeObject.
	 * getMasEmployeeCategory().getEmployeeCategoryId():0;
	 * 
	 * below commented due to view not table
	 * empReligionId=employeeObject.getMasReligion().getReligionId();
	 * empReligion=employeeObject.getMasReligion().getReligionName();
	 * 
	 * if(employeeObject.getDob()!=null) {
	 * empAge=HMSUtil.calculateAge(employeeObject.getDob()); }else { empAge="0"; }
	 * 
	 * 
	 * 
	 * 
	 * responseDepMap.put("Id", depList.getEmployeeDependentId());
	 * responseDepMap.put("Id", ++rowCount); responseDepMap.put("employeeId",
	 * employeeId); responseDepMap.put("name", name); responseDepMap.put("age",
	 * age); responseDepMap.put("gender", gender); responseDepMap.put("genderId",
	 * genderId); responseDepMap.put("dateOfBirth", dateOfBirth);
	 * responseDepMap.put("relation", relation); responseDepMap.put("relationId",
	 * relationId); responseDepMap.put("mobileNumber",mobileNumber);
	 * 
	 * responseDepMap.put("patientAddressL1",addressL1);
	 * responseDepMap.put("patientAddressL2",addressL2);
	 * responseDepMap.put("patientAddressL3",addressL3);
	 * responseDepMap.put("patientAddressL4",addressL4);
	 * responseDepMap.put("patientDistrictId",districtId);
	 * responseDepMap.put("patientDistrictName",districtName);
	 * responseDepMap.put("patientStateId",stateId);
	 * responseDepMap.put("patientStateName",stateName);
	 * responseDepMap.put("patientPincode",patientPincode);
	 * responseDepMap.put("patientEmailId",patientEmailId);
	 * responseDepMap.put("uhidNo", "");
	 * 
	 * responseDepMap.put("serviceNo",serviceNo);
	 * responseDepMap.put("empRankId",empRankId);
	 * responseDepMap.put("empAge",empAge); responseDepMap.put("empRank",empRank);
	 * responseDepMap.put("empTradeId",empTradeId);
	 * responseDepMap.put("empTradeName",empTradeName);
	 * responseDepMap.put("empName",empName);
	 * responseDepMap.put("empTotalService",empTotalService);
	 * responseDepMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseDepMap.put("empUnitId",empUnitId);
	 * responseDepMap.put("empUnitName",empUnitName);
	 * responseDepMap.put("empCommandId",empCommandId);
	 * responseDepMap.put("empCommandName",empCommandName);
	 * responseDepMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseDepMap.put("empRecordOfficeName",empRecordOfficeName);
	 * responseDepMap.put("empDepartmentName",empDepartmentName);
	 * responseDepMap.put("empCategoryId",empCategoryId);
	 * 
	 * 
	 * responseDepMap.put("empMaritalStatusId",empMaritalStatusId);
	 * responseDepMap.put("empMaritalStatusName",empMaritalStatusName);
	 * responseDepMap.put("empReligionId",empReligionId);
	 * responseDepMap.put("empReligion",empReligion);
	 * 
	 * responseDepMap.put("patientBloodGroup",patientBloodGroup);
	 * responseDepMap.put("patientBloodGroupId",patientBloodGroupId);
	 * responseDepMap.put("patientStatus", "");
	 * 
	 * data.put(++rowCount, responseDepMap);
	 * 
	 * } }
	 * 
	 * 
	 * empAndDependentPatientList =
	 * (List<MasEmployee>)patientListofEmpAndDependent.get("employeeList");
	 * if(empAndDependentPatientList.size()>0 ) { for(MasEmployee ms :
	 * empAndDependentPatientList) {
	 * 
	 * 
	 * relation = HMSUtil.getProperties("adt.properties", "SELF_RELATION"); String
	 * selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); relationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * Map<String, Object> responseEmpMap = new HashMap<String, Object>(); String
	 * patientName = ""; if (ms.getEmployeeName() != null) { patientName =
	 * ms.getEmployeeName(); }
	 * 
	 * 
	 * serviceNo= ms.getServiceNo(); employeeId=ms.getEmployeeId();
	 * 
	 * 
	 * String empRankCode = ms.getMasRank()!=null? ms.getMasRank():"0";
	 * List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0) { empRankId =
	 * mRankList.get(0).getRankId(); empRank = mRankList.get(0).getRankName();
	 * 
	 * if(mRankList.get(0).getMasTrade()!=null) { empTradeId =
	 * mRankList.get(0).getMasTrade().getTradeId(); empTradeName
	 * =mRankList.get(0).getMasTrade().getTradeName();
	 * empDepartmentName=empTradeName; }else { empTradeId = 0; empTradeName = "";
	 * empDepartmentName=""; }
	 * 
	 * 
	 * }else { empRankId = 0; empRank = ""; empTradeId = 0; empTradeName = ""; }
	 * 
	 * 
	 * empName= patientName; if(ms.getDoe()!=null) { empTotalService=
	 * HMSUtil.calculateAge(ms.getDoe());
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(ms.getDoe()); }else {
	 * empTotalService= "0"; empServiceJoinDate=""; }
	 * 
	 * if(ms.getDob()!=null) { empAge=HMSUtil.calculateAge(ms.getDob()); age =
	 * HMSUtil.calculateAge(ms.getDob());
	 * dateOfBirth=HMSUtil.changeDateToddMMyyyy(ms.getDoe()); }else { empAge="0";
	 * age="0"; dateOfBirth=""; }
	 * 
	 * // This getMasUnit gives unit code not unit object String empUnitCode =
	 * ms.getMasUnit()!=null?ms.getMasUnit():"0"; List<MasUnit> unitList =
	 * patientRegistrationDao.getEmpUnitId(empUnitCode); if(unitList.size()>0) {
	 * 
	 * empUnitId = unitList.get(0).getUnitId();
	 * empUnitName=unitList.get(0).getUnitName();
	 * 
	 * if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id
	 * empCommandName=unitList.get(0).getMasStation().getMasCommand().getCommandName
	 * (); // This is station Name }else { empCommandId=0; empCommandName=""; }
	 * 
	 * }else { empUnitId = 0; empUnitName=""; }
	 * 
	 * if(ms.getMasRecordOfficeAddress()!=null) {
	 * empRecordOfficeId=ms.getMasRecordOfficeAddress().getRecordOfficeAddressId();
	 * empRecordOfficeName=ms.getMasRecordOfficeAddress().getRecordOfficeAddressName
	 * (); }else { empRecordOfficeId=0; empRecordOfficeName=""; }
	 * 
	 * if(ms.getMasMaritalStatus()!=null) {
	 * empMaritalStatusId=ms.getMasMaritalStatus().getMaritalStatusId();
	 * empMaritalStatusName=ms.getMasMaritalStatus().getMaritalStatusName(); }else {
	 * empMaritalStatusId=0; empMaritalStatusName=""; }
	 * 
	 * empCategoryId=ms.getMasEmployeeCategory()!=null?ms.getMasEmployeeCategory().
	 * getEmployeeCategoryId():0; Below commented for view
	 * empReligionId=ms.getMasReligion().getReligionId();
	 * empReligion=ms.getMasReligion().getReligionName();
	 * 
	 * 
	 * 
	 * name = patientName;
	 * 
	 * if(ms.getMasAdministrativeSex()!=null) { gender
	 * =ms.getMasAdministrativeSex().getAdministrativeSexName(); genderId
	 * =ms.getMasAdministrativeSex().getAdministrativeSexId(); }else { gender ="";
	 * genderId = 0; }
	 * 
	 * 
	 * mobileNumber=ms.getMobileNo()!=null?ms.getMobileNo():"";
	 * 
	 * if(!ms.getEmployeeAddress().isEmpty() && ms.getEmployeeAddress()!=null &&
	 * ms.getEmployeeAddress().get(0)!=null) { addressL1 =
	 * ms.getEmployeeAddress().get(0).getAddressLine1()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine1():""; addressL2 =
	 * ms.getEmployeeAddress().get(0).getAddressLine2()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine2():""; addressL3 =
	 * ms.getEmployeeAddress().get(0).getAddressLine3()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine3():""; addressL4 =
	 * ms.getEmployeeAddress().get(0).getAddressLine4()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine4():""; districtId =
	 * ms.getEmployeeAddress().get(0).getMasDistrict()!=null?ms.getEmployeeAddress()
	 * .get(0).getMasDistrict().getDistrictId():0; districtName =
	 * ms.getEmployeeAddress().get(0).getMasDistrict()!=null?ms.getEmployeeAddress()
	 * .get(0).getMasDistrict().getDistrictName():""; stateId =
	 * ms.getEmployeeAddress().get(0).getMasState()!=null?ms.getEmployeeAddress().
	 * get(0).getMasState().getStateId():0; stateName =
	 * ms.getEmployeeAddress().get(0).getMasState()!=null?ms.getEmployeeAddress().
	 * get(0).getMasState().getStateName():""; patientPincode =
	 * ms.getEmployeeAddress().get(0).getPincode()!=null?Long.parseLong(ms.
	 * getEmployeeAddress().get(0).getPincode()):0; }else { addressL1 = "";
	 * addressL2 = ""; addressL3 = ""; addressL4 = ""; patientPincode=0L; }
	 * 
	 * patientEmailId=ms.getEmail()!=null?ms.getEmail():"";
	 * 
	 * patientBloodGroup = ""; patientBloodGroupId = 0;
	 * 
	 * responseEmpMap.put("uhidNo", "");
	 * 
	 * responseEmpMap.put("Id", ms.getEmployeeId()); responseEmpMap.put("Id",
	 * ++rowCount); responseEmpMap.put("employeeId", employeeId);
	 * responseEmpMap.put("name", name); responseEmpMap.put("age", age);
	 * responseEmpMap.put("gender", gender); responseEmpMap.put("genderId",
	 * genderId); responseEmpMap.put("dateOfBirth", dateOfBirth);
	 * responseEmpMap.put("relation", relation); responseEmpMap.put("relationId",
	 * relationId); responseEmpMap.put("mobileNumber",mobileNumber);
	 * 
	 * 
	 * responseEmpMap.put("patientAddressL1",addressL1);
	 * responseEmpMap.put("patientAddressL2",addressL2);
	 * responseEmpMap.put("patientAddressL3",addressL3);
	 * responseEmpMap.put("patientAddressL4",addressL4);
	 * responseEmpMap.put("patientDistrictId",districtId);
	 * responseEmpMap.put("patientDistrictName",districtName);
	 * responseEmpMap.put("patientStateId",stateId);
	 * responseEmpMap.put("patientStateName",stateName);
	 * 
	 * responseEmpMap.put("patientPincode",patientPincode);
	 * responseEmpMap.put("patientEmailId",patientEmailId);
	 * responseEmpMap.put("patientBloodGroup", patientBloodGroup);
	 * responseEmpMap.put("patientBloodGroupId", patientBloodGroupId);
	 * 
	 * 
	 * responseEmpMap.put("serviceNo",serviceNo);
	 * responseEmpMap.put("empRankId",empRankId);
	 * responseEmpMap.put("empAge",empAge); responseEmpMap.put("empRank",empRank);
	 * responseEmpMap.put("empTradeId",empTradeId);
	 * responseEmpMap.put("empTradeName",empTradeName);
	 * responseEmpMap.put("empName",empName);
	 * responseEmpMap.put("empTotalService",empTotalService);
	 * responseEmpMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseEmpMap.put("empUnitId",empUnitId);
	 * responseEmpMap.put("empUnitName",empUnitName);
	 * responseEmpMap.put("empCommandId",empCommandId);
	 * responseEmpMap.put("empCommandName",empCommandName);
	 * responseEmpMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseEmpMap.put("empRecordOfficeName",empRecordOfficeName);
	 * responseEmpMap.put("empDepartmentName",empDepartmentName);
	 * responseEmpMap.put("empCategoryId",empCategoryId);
	 * 
	 * responseEmpMap.put("empMaritalStatusId",empMaritalStatusId);
	 * responseEmpMap.put("empMaritalStatusName",empMaritalStatusName);
	 * responseEmpMap.put("empReligionId",empReligionId);
	 * responseEmpMap.put("empReligion",empReligion);
	 * responseEmpMap.put("patientStatus", "");
	 * 
	 * data.put(++rowCount, responseEmpMap); } } map.put("data", data);
	 * map.put("count", data.size()); map.put("msg","List of Detail");
	 * map.put("status", "1"); return map;
	 * 
	 * }else { empAndDependentPatientList =
	 * (List<MasEmployee>)patientListofEmpAndDependent.get("employeeList"); int
	 * rowCount=0; for (MasEmployee ms : empAndDependentPatientList) {
	 * 
	 * relation = HMSUtil.getProperties("adt.properties", "SELF_RELATION"); String
	 * selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); relationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * Map<String, Object> responseEmpMap = new HashMap<String, Object>();
	 * 
	 * String patientName = ""; if (ms.getEmployeeName() != null) { patientName =
	 * ms.getEmployeeName(); }
	 * 
	 * empName= patientName; serviceNo = ms.getServiceNo(); employeeId =
	 * ms.getEmployeeId();
	 * 
	 * String empRankCode = ms.getMasRank()!=null? ms.getMasRank():"0";
	 * List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0) { empRankId =
	 * mRankList.get(0).getRankId(); empRank = mRankList.get(0).getRankName();
	 * empTradeId =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeId():0; empTradeName =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeName():"";
	 * 
	 * }else { empRankId = 0; empRank = ""; empTradeId = 0; empTradeName = ""; }
	 * 
	 * if(ms.getDoe()!=null) { empTotalService= HMSUtil.calculateAge(ms.getDoe());
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(ms.getDoe()); }else {
	 * empTotalService= "0"; empServiceJoinDate=""; }
	 * 
	 * if(ms.getDob()!=null) { empAge=HMSUtil.calculateAge(ms.getDob()); age =
	 * HMSUtil.calculateAge(ms.getDob()); dateOfBirth
	 * =HMSUtil.changeDateToddMMyyyy(ms.getDob()); }else { empAge="0"; age="0";
	 * dateOfBirth=""; }
	 * 
	 * 
	 * String empUnitCode = ms.getMasUnit()!=null?ms.getMasUnit():"0";
	 * 
	 * List<MasUnit> unitList = patientRegistrationDao.getEmpUnitId(empUnitCode);
	 * if(!unitList.isEmpty() && unitList.size()>0) {
	 * 
	 * empUnitId = unitList.get(0).getUnitId();
	 * empUnitName=unitList.get(0).getUnitName();
	 * 
	 * if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id
	 * empCommandName=unitList.get(0).getMasStation().getMasCommand().getCommandName
	 * (); // This is station Name }else { empCommandId=0; empCommandName=""; }
	 * 
	 * }else { empUnitId = 0; empUnitName=""; }
	 * 
	 * if(ms.getMasRecordOfficeAddress()!=null) {
	 * empRecordOfficeId=ms.getMasRecordOfficeAddress().getRecordOfficeAddressId();
	 * empRecordOfficeName=ms.getMasRecordOfficeAddress().getRecordOfficeAddressName
	 * (); }else { empRecordOfficeId=0; empRecordOfficeName=""; }
	 * 
	 * if(ms.getMasMaritalStatus()!=null) {
	 * empMaritalStatusId=ms.getMasMaritalStatus().getMaritalStatusId();
	 * empMaritalStatusName=ms.getMasMaritalStatus().getMaritalStatusName(); }else {
	 * empMaritalStatusId=0; empMaritalStatusName=""; }
	 * 
	 * empCategoryId=ms.getMasEmployeeCategory()!=null?ms.getMasEmployeeCategory().
	 * getEmployeeCategoryId():0;
	 * 
	 * Comment due to data from view
	 * empReligionId=ms.getMasReligion().getReligionId();
	 * empReligion=ms.getMasReligion().getReligionName();
	 * 
	 * 
	 * 
	 * 
	 * if(mRankList.size()>0) { empDepartmentName=
	 * mRankList.get(0).getMasTrade()!=null?
	 * mRankList.get(0).getMasTrade().getTradeName():""; }else {
	 * empDepartmentName=""; }
	 * 
	 * name = patientName;
	 * 
	 * if( ms.getMasAdministrativeSex()!=null) { genderId =
	 * ms.getMasAdministrativeSex().getAdministrativeSexId(); gender =
	 * ms.getMasAdministrativeSex().getAdministrativeSexName(); }else { genderId =
	 * 0; gender = ""; }
	 * 
	 * 
	 * 
	 * mobileNumber=ms.getMobileNo()!=null?ms.getMobileNo():"";
	 * 
	 * if(!ms.getEmployeeAddress().isEmpty() && ms.getEmployeeAddress()!=null &&
	 * ms.getEmployeeAddress().get(0)!=null) { addressL1 =
	 * ms.getEmployeeAddress().get(0).getAddressLine1()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine1():""; addressL2 =
	 * ms.getEmployeeAddress().get(0).getAddressLine2()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine2():""; addressL3 =
	 * ms.getEmployeeAddress().get(0).getAddressLine3()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine3():""; addressL4 =
	 * ms.getEmployeeAddress().get(0).getAddressLine4()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine4():""; districtId =
	 * ms.getEmployeeAddress().get(0).getMasDistrict()!=null?ms.getEmployeeAddress()
	 * .get(0).getMasDistrict().getDistrictId():0; districtName =
	 * ms.getEmployeeAddress().get(0).getMasDistrict()!=null?ms.getEmployeeAddress()
	 * .get(0).getMasDistrict().getDistrictName():""; stateId =
	 * ms.getEmployeeAddress().get(0).getMasState()!=null?ms.getEmployeeAddress().
	 * get(0).getMasState().getStateId():0; stateName =
	 * ms.getEmployeeAddress().get(0).getMasState()!=null?ms.getEmployeeAddress().
	 * get(0).getMasState().getStateName():""; patientPincode =
	 * ms.getEmployeeAddress().get(0).getPincode()!=null?Long.parseLong(ms.
	 * getEmployeeAddress().get(0).getPincode()):0; }else { addressL1 = "";
	 * addressL2 = ""; addressL3 = ""; addressL4 = ""; patientPincode = 0L; }
	 * 
	 * 
	 * patientEmailId=ms.getEmail()!=null?ms.getEmail():"";
	 * 
	 * patientBloodGroup = ""; patientBloodGroupId = 0;
	 * 
	 * responseEmpMap.put("Id", ms.getEmployeeId()); responseEmpMap.put("uhidNo",
	 * ""); responseEmpMap.put("Id", ++rowCount); responseEmpMap.put("employeeId",
	 * employeeId); responseEmpMap.put("name", name); responseEmpMap.put("empAge",
	 * empAge); responseEmpMap.put("age", age); responseEmpMap.put("gender",
	 * gender); responseEmpMap.put("genderId", genderId);
	 * responseEmpMap.put("dateOfBirth", dateOfBirth);
	 * responseEmpMap.put("relation", relation); responseEmpMap.put("relationId",
	 * relationId); responseEmpMap.put("mobileNumber",mobileNumber);
	 * 
	 * responseEmpMap.put("patientAddressL1",addressL1);
	 * responseEmpMap.put("patientAddressL2",addressL2);
	 * responseEmpMap.put("patientAddressL3",addressL3);
	 * responseEmpMap.put("patientAddressL4",addressL4);
	 * responseEmpMap.put("patientDistrictId",districtId);
	 * responseEmpMap.put("patientDistrictName",districtName);
	 * responseEmpMap.put("patientStateId",stateId);
	 * responseEmpMap.put("patientStateName",stateName);
	 * 
	 * 
	 * responseEmpMap.put("patientPincode",patientPincode);
	 * responseEmpMap.put("patientEmailId",patientEmailId);
	 * responseEmpMap.put("patientBloodGroup",patientBloodGroup);
	 * responseEmpMap.put("patientBloodGroupId",patientBloodGroupId);
	 * 
	 * 
	 * responseEmpMap.put("serviceNo",serviceNo);
	 * responseEmpMap.put("empRankId",empRankId);
	 * responseEmpMap.put("empRank",empRank);
	 * responseEmpMap.put("empTradeId",empTradeId);
	 * responseEmpMap.put("empTradeName",empTradeName);
	 * responseEmpMap.put("empName",empName);
	 * responseEmpMap.put("empTotalService",empTotalService);
	 * responseEmpMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseEmpMap.put("empUnitId",empUnitId);
	 * responseEmpMap.put("empUnitName",empUnitName);
	 * responseEmpMap.put("empCommandId",empCommandId);
	 * responseEmpMap.put("empCommandName",empCommandName);
	 * responseEmpMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseEmpMap.put("empRecordOfficeName",empRecordOfficeName);
	 * responseEmpMap.put("empDepartmentName",empDepartmentName);
	 * responseEmpMap.put("empCategoryId",empCategoryId);
	 * 
	 * responseEmpMap.put("empMaritalStatusId",empMaritalStatusId);
	 * responseEmpMap.put("empMaritalStatusName",empMaritalStatusName);
	 * responseEmpMap.put("empReligionId",empReligionId);
	 * responseEmpMap.put("empReligion",empReligion);
	 * responseEmpMap.put("patientStatus", "");
	 * 
	 * 
	 * data.put(++rowCount, responseEmpMap); // Second List<MasEmployeeDependent>
	 * dependentList = (List<MasEmployeeDependent>)patientListofEmpAndDependent.get(
	 * "employeeDependentList"); if (dependentList.size()>0) {
	 * 
	 * for (MasEmployeeDependent depList : dependentList) { Map<String, Object>
	 * responseDepMap = new HashMap<String, Object>(); String depPatientName = "";
	 * if (depList.getEmployeeDependentName() != null) { depPatientName =
	 * depList.getEmployeeDependentName(); }
	 * 
	 * 
	 * name = depPatientName;
	 * 
	 * if(depList.getDateOfBirth()!=null) { age =
	 * HMSUtil.calculateAge(depList.getDateOfBirth()); dateOfBirth =
	 * HMSUtil.changeDateToddMMyyyy(depList.getDateOfBirth()); }else { age = "0";
	 * dateOfBirth = ""; }
	 * 
	 * if(depList.getMasAdministrativeSex()!=null) { genderId =
	 * depList.getMasAdministrativeSex().getAdministrativeSexId(); gender =
	 * depList.getMasAdministrativeSex().getAdministrativeSexName(); }else {
	 * genderId = 0; gender = ""; }
	 * 
	 * if(depList.getMasRelation()!=null) {
	 * relationId=depList.getMasRelation().getRelationId(); relation =
	 * depList.getMasRelation().getRelationName(); }else { relationId=0; relation =
	 * ""; }
	 * 
	 * 
	 * // mobileNumber=ms.getMobileNo()!=null?ms.getMobileNo():""; mobileNumber="";
	 * 
	 * addressL1 = depList.getAddresLine1()!=null?depList.getAddresLine1():"";
	 * addressL2 = depList.getAddresLine2()!=null?depList.getAddresLine2():"";
	 * addressL3 = depList.getAddresLine3()!=null?depList.getAddresLine3():"";
	 * addressL4 = depList.getAddresLine4()!=null?depList.getAddresLine4():"";
	 * districtId =
	 * depList.getMasDistrict()!=null?depList.getMasDistrict().getDistrictId():0;
	 * districtName =
	 * depList.getMasDistrict()!=null?depList.getMasDistrict().getDistrictName():"";
	 * 
	 * stateId = depList.getMasState()!=null?depList.getMasState().getStateId():0;
	 * stateName =
	 * depList.getMasState()!=null?depList.getMasState().getStateName():"";
	 * patientPincode =
	 * depList.getPincode()!=null?Long.parseLong(depList.getPincode()):0;
	 * patientEmailId=ms.getEmail(); // This may be change in future
	 * 
	 * //Employee related
	 * 
	 * serviceNo= employeeObject!=null?employeeObject.getServiceNo():"";
	 * 
	 * 
	 * if(mRankList.size()>0) { empRankId = mRankList.get(0).getRankId(); empRank =
	 * mRankList.get(0).getRankName();
	 * 
	 * if(mRankList.get(0).getMasTrade()!=null) { empTradeId =
	 * mRankList.get(0).getMasTrade().getTradeId(); empTradeName =
	 * mRankList.get(0).getMasTrade().getTradeName();
	 * empDepartmentName=empTradeName; // we will show Trade Name In place of
	 * Department Name }else { empTradeId = 0; empTradeName = "";
	 * empDepartmentName=""; } }else { empRankId = 0; empRank = ""; empTradeId = 0;
	 * empTradeName = "";
	 * 
	 * }
	 * 
	 * 
	 * if(employeeObject.getDob()!=null) { empAge =
	 * HMSUtil.calculateAge(employeeObject.getDob()); }else { empAge = "0"; }
	 * 
	 * // This getMasUnit() gives unitCode not unit object. if(unitList.size()>0) {
	 * empUnitId = unitList.get(0).getUnitId();
	 * empUnitName=unitList.get(0).getUnitName(); if(unitList.get(0)!=null &&
	 * unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id
	 * empCommandName=unitList.get(0).getMasStation().getMasCommand().getCommandName
	 * (); // This is station Name }else { empCommandId=0; empCommandName=""; }
	 * 
	 * }else { empUnitId = 0; empUnitName=""; }
	 * 
	 * if(employeeObject!=null) { empName= employeeObject.getEmployeeName();
	 * employeeId =employeeObject.getEmployeeId(); }else { empName= ""; employeeId =
	 * 0; }
	 * 
	 * if(employeeObject.getDoe()!=null) { empTotalService=
	 * HMSUtil.calculateAge(employeeObject.getDoe());
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(employeeObject.getDoe());
	 * }else { empTotalService= "0"; empServiceJoinDate=""; }
	 * 
	 * if(employeeObject.getMasRecordOfficeAddress()!=null) {
	 * empRecordOfficeId=employeeObject.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressId();
	 * empRecordOfficeName=employeeObject.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressName(); }else { empRecordOfficeId=0;
	 * empRecordOfficeName=""; }
	 * 
	 * if(employeeObject.getMasMaritalStatus()!=null) {
	 * empMaritalStatusId=employeeObject.getMasMaritalStatus().getMaritalStatusId();
	 * empMaritalStatusName=employeeObject.getMasMaritalStatus().
	 * getMaritalStatusName(); }else { empMaritalStatusId=0;
	 * empMaritalStatusName=""; }
	 * 
	 * 
	 * empCategoryId=employeeObject.getMasEmployeeCategory()!=null?employeeObject.
	 * getMasEmployeeCategory().getEmployeeCategoryId():0;
	 * 
	 * Comment due to data from view
	 * empReligionId=employeeObject.getMasReligion().getReligionId();
	 * empReligion=employeeObject.getMasReligion().getReligionName();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * responseDepMap.put("Id", depList.getEmployeeDependentId());
	 * responseDepMap.put("uhidNo", ""); responseDepMap.put("Id", ++rowCount);
	 * responseDepMap.put("employeeId", employeeId); responseDepMap.put("name",
	 * name); responseDepMap.put("age", age); responseDepMap.put("empAge", empAge);
	 * responseDepMap.put("gender", gender); responseDepMap.put("genderId",
	 * genderId); responseDepMap.put("dateOfBirth", dateOfBirth);
	 * responseDepMap.put("relation", relation); responseDepMap.put("relationId",
	 * relationId); responseDepMap.put("mobileNumber",mobileNumber);
	 * 
	 * responseDepMap.put("addressL1",addressL1);
	 * responseDepMap.put("addressL2",addressL2);
	 * responseDepMap.put("addressL3",addressL3);
	 * responseDepMap.put("addressL4",addressL4);
	 * 
	 * responseDepMap.put("districtId",districtId);
	 * responseDepMap.put("districtName",districtName);
	 * 
	 * responseDepMap.put("patientEmailId",patientEmailId);
	 * responseDepMap.put("patientBloodGroup", patientBloodGroup);
	 * responseDepMap.put("patientBloodGroupId", patientBloodGroupId);
	 * 
	 * responseDepMap.put("serviceNo",serviceNo);
	 * responseDepMap.put("empRankId",empRankId);
	 * responseDepMap.put("empRank",empRank);
	 * responseDepMap.put("empTradeId",empTradeId);
	 * responseDepMap.put("empTradeName",empTradeName);
	 * responseDepMap.put("empName",empName);
	 * responseDepMap.put("empTotalService",empTotalService);
	 * responseDepMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseDepMap.put("empUnitId",empUnitId);
	 * responseDepMap.put("empUnitName",empUnitName);
	 * responseDepMap.put("empCommandId",empCommandId);
	 * responseDepMap.put("empCommandName",empCommandName);
	 * responseDepMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseDepMap.put("empRecordOfficeName",empRecordOfficeName);
	 * responseDepMap.put("empDepartmentName",empDepartmentName);
	 * responseDepMap.put("empMaritalStatusId",empMaritalStatusId);
	 * responseDepMap.put("empMaritalStatusName",empMaritalStatusName);
	 * responseDepMap.put("empReligionId",empReligionId);
	 * responseDepMap.put("empReligion",empReligion);
	 * responseDepMap.put("empCategoryId",empCategoryId);
	 * responseDepMap.put("patientStatus", "");
	 * 
	 * data.put(++rowCount, responseDepMap);
	 * 
	 * } }
	 * 
	 * map.put("data", data); map.put("count", data.size());
	 * map.put("msg","List of Detail"); map.put("status", "1"); return map; } }
	 * }else { // this will executes when exception occurs in daoImpl
	 * map.put("msg","Some error occurred in service no: " + serviceNo);
	 * map.put("status", "0"); return map; } } } } } map.put("msg","Service no:" +
	 * serviceNo + " does not exist."); map.put("count", data.size());
	 * map.put("status", "0"); return map;
	 * 
	 * } catch (Exception e) { map.put("msg","Some error occurred in service no: " +
	 * serviceNo); map.put("status", "0"); e.printStackTrace(); return map; }
	 * 
	 * }
	 * 
	 * 
	 * @Override public Map<String, Object>
	 * getRecordsFordepartmentBloodGroupAndMedicalCategory(Map<String, String>
	 * requestData) { // TODO Auto-generated method stub Map<String,Object>map = new
	 * HashMap<String, Object>(); List<HashMap<String, Object>> bloodGroupList = new
	 * ArrayList<HashMap<String, Object>>(); List<HashMap<String, Object>>
	 * departmentList = new ArrayList<HashMap<String, Object>>();
	 * List<HashMap<String, Object>> medicalCategoryList = new
	 * ArrayList<HashMap<String, Object>>(); List<HashMap<String, Object>>
	 * masRelationList = new ArrayList<HashMap<String, Object>>();
	 * List<HashMap<String, Object>> masStateList = new ArrayList<HashMap<String,
	 * Object>>(); List<HashMap<String, Object>> masDistrictList = new
	 * ArrayList<HashMap<String, Object>>();
	 * 
	 * String codeOpd = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_OPD").trim(); String codeME =
	 * HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_ME").trim();
	 * String codeMB = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_MB").trim();
	 * 
	 * String selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim();
	 * 
	 * String departmentTypeCodeMaternity = HMSUtil.getProperties("adt.properties",
	 * "DEPARTMENT_TYPE_CODE_MATERNITY").trim();
	 * 
	 * long appointmentTypeIdOPD=
	 * patientRegistrationDao.getAppointmentTypeIdFromCode(codeOpd); long
	 * appointmentTypeIdME=patientRegistrationDao.getAppointmentTypeIdFromCode(
	 * codeME); long
	 * appointmentTypeIdMB=patientRegistrationDao.getAppointmentTypeIdFromCode(
	 * codeMB); long selfRelationId=
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * long departmentTypeMaternityId =
	 * patientRegistrationDao.departmentTypeMaternityIdFromCode(
	 * departmentTypeCodeMaternity);
	 * 
	 * map.put("appointmentTypeIdOPD",appointmentTypeIdOPD);
	 * map.put("appointmentTypeIdME",appointmentTypeIdME);
	 * map.put("appointmentTypeIdMB",appointmentTypeIdMB);
	 * map.put("selfRelationId",selfRelationId);
	 * map.put("departmentTypeMaternityId",departmentTypeMaternityId);
	 * 
	 * 
	 * 
	 * List<MasBloodGroup>respBloodGroupList =
	 * patientRegistrationDao.getBloodGroupList(); if(respBloodGroupList.size()>0) {
	 * for(MasBloodGroup bloodGroup:respBloodGroupList) { HashMap<String,Object>
	 * bloodGroupMap=new HashMap<String, Object>();
	 * bloodGroupMap.put("bloodGroupId", bloodGroup.getBloodGroupId());
	 * bloodGroupMap.put("bloodGroupName", bloodGroup.getBloodGroupName());
	 * bloodGroupList.add(bloodGroupMap); }
	 * map.put("bloodGroupList",bloodGroupList); }
	 * 
	 * long hospitalId = Long.parseLong(requestData.get("hospitalId").toString());
	 * 
	 * List<MasDepartment> respDepartmentList =
	 * patientRegistrationDao.getDepartmentListFromAppointmentSession(hospitalId);
	 * if(respDepartmentList.size()>0) { for(MasDepartment
	 * departmentFromSession:respDepartmentList) { HashMap<String,Object>
	 * departmentMap=new HashMap<String, Object>(); if(departmentFromSession!=null)
	 * { departmentMap.put("departmentId", departmentFromSession.getDepartmentId());
	 * departmentMap.put("departmentName",
	 * departmentFromSession.getDepartmentName());
	 * departmentList.add(departmentMap); } } map.put("departmentList",
	 * departmentList); }else { map.put("departmentList", departmentList); }
	 * 
	 * 
	 * List<MasMedicalCategory>respMedicalCategoryList=
	 * patientRegistrationDao.getMedicalCategoryList();
	 * 
	 * if(respMedicalCategoryList.size()>0) { for(MasMedicalCategory
	 * medicalCategory:respMedicalCategoryList) { HashMap<String,Object>
	 * medicalCategoryMap=new HashMap<String, Object>();
	 * medicalCategoryMap.put("medicalCategoryId",
	 * medicalCategory.getMedicalCategoryId());
	 * medicalCategoryMap.put("medicalCategoryName",
	 * medicalCategory.getMedicalCategoryName());
	 * medicalCategoryList.add(medicalCategoryMap); }
	 * map.put("medicalCategoryList",medicalCategoryList); }
	 * 
	 * 
	 * 
	 * List<MasRelation>respMasRelationList
	 * =patientRegistrationDao.getRelationList(); if(respMasRelationList.size()>0) {
	 * for(MasRelation relation:respMasRelationList) { HashMap<String,Object>
	 * relationMap=new HashMap<String, Object>(); relationMap.put("relationId",
	 * relation.getRelationId()); relationMap.put("relationName",
	 * relation.getRelationName()); masRelationList.add(relationMap); }
	 * map.put("masRelationList",masRelationList); }
	 * 
	 * 
	 * List<MasState>respMasStateList =patientRegistrationDao.getStateList();
	 * if(respMasStateList.size()>0) { for(MasState state:respMasStateList) {
	 * HashMap<String,Object> stateMap=new HashMap<String, Object>();
	 * stateMap.put("stateId", state.getStateId()); stateMap.put("stateName",
	 * state.getStateName()); masStateList.add(stateMap); }
	 * map.put("masStateList",masStateList); }
	 * 
	 * 
	 * List<MasDistrict>respMasDistrictList
	 * =patientRegistrationDao.getDistrictList(); if(respMasDistrictList.size()>0) {
	 * for(MasDistrict district:respMasDistrictList) { HashMap<String,Object>
	 * districtMap=new HashMap<String, Object>(); districtMap.put("districtId",
	 * district.getDistrictId()); districtMap.put("districtName",
	 * district.getDistrictName()); masDistrictList.add(districtMap); }
	 * map.put("masDistrictList",masDistrictList); }
	 * 
	 * // Newly added code
	 * 
	 * List<HashMap<String, Object>> rankList = new ArrayList<HashMap<String,
	 * Object>>();
	 * 
	 * List<MasRank>respMasRankList =patientRegistrationDao.getMasRankList();
	 * if(respMasRankList.size()>0) { for(MasRank rank:respMasRankList) {
	 * HashMap<String,Object> rankMap=new HashMap<String, Object>();
	 * rankMap.put("rankId", rank.getRankId()); rankMap.put("rankName",
	 * rank.getRankName()); rankList.add(rankMap); } map.put("rankList",rankList); }
	 * 
	 * List<HashMap<String, Object>> unitList = new ArrayList<HashMap<String,
	 * Object>>();
	 * 
	 * List<MasUnit>respMasUnitList =patientRegistrationDao.getMasUnitList();
	 * if(respMasUnitList.size()>0) { for(MasUnit unit:respMasUnitList) {
	 * HashMap<String,Object> unitMap=new HashMap<String, Object>();
	 * unitMap.put("unitId", unit.getUnitId()); unitMap.put("unitName",
	 * unit.getUnitName()); unitList.add(unitMap); } map.put("unitList",unitList); }
	 * 
	 * List<HashMap<String, Object>> tradeList = new ArrayList<HashMap<String,
	 * Object>>();
	 * 
	 * List<MasTrade>respMasTradeList =patientRegistrationDao.getMasTradeList();
	 * if(respMasTradeList.size()>0) { for(MasTrade trade:respMasTradeList) {
	 * HashMap<String,Object> tradeMap=new HashMap<String, Object>();
	 * tradeMap.put("tradeId", trade.getTradeId()); tradeMap.put("tradeName",
	 * trade.getTradeName()); tradeList.add(tradeMap); }
	 * map.put("tradeList",tradeList); }
	 * 
	 * List<HashMap<String, Object>> commandList = new ArrayList<HashMap<String,
	 * Object>>();
	 * 
	 * List<MasCommand>respMasCommandList
	 * =patientRegistrationDao.getMasCommandList(); if(respMasCommandList.size()>0)
	 * { for(MasCommand command:respMasCommandList) { HashMap<String,Object>
	 * commandMap=new HashMap<String, Object>(); commandMap.put("commandId",
	 * command.getCommandId()); commandMap.put("commandName",
	 * command.getCommandName()); commandList.add(commandMap); }
	 * map.put("commandList",commandList); }
	 * 
	 * 
	 * List<HashMap<String, Object>> genderList = new ArrayList<HashMap<String,
	 * Object>>();
	 * 
	 * List<MasAdministrativeSex>respMasGenderList
	 * =patientRegistrationDao.getGenderList(); if(respMasGenderList.size()>0) {
	 * for(MasAdministrativeSex gender:respMasGenderList) { HashMap<String,Object>
	 * genderMap=new HashMap<String, Object>(); genderMap.put("genderId",
	 * gender.getAdministrativeSexId()); genderMap.put("genderName",
	 * gender.getAdministrativeSexName()); genderList.add(genderMap); }
	 * map.put("genderList",genderList); }
	 * 
	 * List<HashMap<String, Object>> maritalStatusList = new
	 * ArrayList<HashMap<String, Object>>();
	 * List<MasMaritalStatus>respMaritalStatusList
	 * =patientRegistrationDao.getMasMaritalStatusList();
	 * if(respMaritalStatusList.size()>0) { for(MasMaritalStatus
	 * maritalList:respMaritalStatusList) { HashMap<String,Object> mStatusMap=new
	 * HashMap<String, Object>(); mStatusMap.put("mStatusId",
	 * maritalList.getMaritalStatusId()); mStatusMap.put("mStatusName",
	 * maritalList.getMaritalStatusName()); maritalStatusList.add(mStatusMap); }
	 * map.put("maritalStatusList",maritalStatusList); }
	 * 
	 * List<HashMap<String, Object>> religionList = new ArrayList<HashMap<String,
	 * Object>>(); List<MasReligion>respreligionList
	 * =patientRegistrationDao.getMasReligionList(); if(respreligionList.size()>0) {
	 * for(MasReligion rList:respreligionList) { HashMap<String,Object> rMap=new
	 * HashMap<String, Object>(); rMap.put("religionId", rList.getReligionId());
	 * rMap.put("religionName", rList.getReligionName()); religionList.add(rMap); }
	 * map.put("religionList",religionList); }
	 * 
	 * 
	 * List<HashMap<String, Object>> recordOfficeAddressList = new
	 * ArrayList<HashMap<String, Object>>();
	 * List<MasRecordOfficeAddress>recordOfficeList
	 * =patientRegistrationDao.getMasRecordOfficeList();
	 * if(recordOfficeList.size()>0) { for(MasRecordOfficeAddress
	 * officeList:recordOfficeList) { HashMap<String,Object> officeMap=new
	 * HashMap<String, Object>(); officeMap.put("officeId",
	 * officeList.getRecordOfficeAddressId()); officeMap.put("officeName",
	 * officeList.getRecordOfficeAddressName());
	 * recordOfficeAddressList.add(officeMap); }
	 * map.put("recordOfficeAddressList",recordOfficeAddressList); }
	 * 
	 * 
	 * 
	 * 
	 * return map; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object>
	 * getTokenNoForDepartmentMultiVisit(Map<String, Object> requestData) {
	 * Map<String, Object> responseMap = new HashMap<String, Object>(); Map<String,
	 * Object> rosterValueForToken = new HashMap<String, Object>();
	 * 
	 * String VisitFlagForReception = HMSUtil.getProperties("adt.properties",
	 * "VISIT_FLAG_RECEPTION"); String VisitFlagForPortal =
	 * HMSUtil.getProperties("adt.properties", "VISIT_FLAG_PORTAL");
	 * 
	 * 
	 * String codeME = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_ME").trim(); String codeMB =
	 * HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_MB").trim();
	 * 
	 * 
	 * String appointmentTypeIdME=Long.toString(patientRegistrationDao.
	 * getAppointmentTypeIdFromCode(codeME)); String
	 * appointmentTypeIdMB=Long.toString(patientRegistrationDao.
	 * getAppointmentTypeIdFromCode(codeMB));
	 * 
	 * 
	 * long departmentId=0; long hospitalId=0; String visitFlag=""; String
	 * tokenMsg=""; String visitDate=""; long patientId =0;
	 * 
	 * List<Map<String,Object>> jsonList = new ArrayList<Map<String,Object>>();
	 * List<Map<String,Object>> jsonInput = (List<Map<String,Object>>)
	 * requestData.get("arrParam");
	 * 
	 * for(Map<String,Object> json : jsonInput) { //This loop will require when
	 * multi-facility appointment will given in future.
	 * 
	 * departmentId = Long.parseLong(json.get("deptId").toString()); hospitalId =
	 * Long.parseLong(json.get("hospitalId").toString());
	 * 
	 * visitFlag= json.get("visitFlag").toString();
	 * 
	 * if(visitFlag.equalsIgnoreCase(VisitFlagForReception) ||
	 * visitFlag.equalsIgnoreCase(VisitFlagForPortal)) { patientId =
	 * Long.parseLong(json.get("patientId").toString());
	 * 
	 * }
	 * 
	 * 
	 * if( json.get("visitDate")!=null) {
	 * visitDate=json.get("visitDate").toString(); }
	 * 
	 * 
	 * List<String> appointmentTypeList
	 * =(List<String>)json.get("appointmentTypeId");
	 * 
	 * for(int i=0;i<appointmentTypeList.size();i++) { Map<String, Object> map = new
	 * HashMap<String, Object>();
	 * 
	 * long appointmentTypeId = Long.parseLong((appointmentTypeList.get(i)));
	 * 
	 * rosterValueForToken =
	 * patientRegistrationDao.getTokenNoForDepartmentMultiVisit(departmentId,
	 * hospitalId,appointmentTypeId,visitFlag,visitDate,patientId);
	 * if(!rosterValueForToken.isEmpty() && rosterValueForToken.size()>0) {
	 * map.put("tokenMsg",rosterValueForToken.get("tokenMsg"));
	 * map.put("appointmentTypeId",rosterValueForToken.get("appointmentTypeId"));
	 * map.put("status","1");
	 * map.put("examType",rosterValueForToken.get("examType"));
	 * if(rosterValueForToken.get("appointmentTypeId").toString().equalsIgnoreCase(
	 * appointmentTypeIdME) ||
	 * rosterValueForToken.get("appointmentTypeId").toString().equalsIgnoreCase(
	 * appointmentTypeIdMB)) { if(rosterValueForToken.get("medExBrdId")!=null) {
	 * map.put("medExBrdId",rosterValueForToken.get("medExBrdId")); }
	 * if(rosterValueForToken.get("visitId")!=null) {
	 * map.put("visitId",rosterValueForToken.get("visitId")); }
	 * 
	 * // Testing for ME Age if(rosterValueForToken.get("lastAgeME")!=null) {
	 * map.put("lastAgeME",rosterValueForToken.get("lastAgeME")); }else {
	 * map.put("lastAgeME",""); }
	 * 
	 * if(rosterValueForToken.get("MEYear")!=null) {
	 * map.put("MEYear",rosterValueForToken.get("MEYear")); }else {
	 * map.put("MEYear",""); } }
	 * 
	 * }else { tokenMsg= "Token is not available"; map.put("tokenMsg",tokenMsg);
	 * map.put("status","0"); } jsonList.add(map); }
	 * 
	 * } responseMap.put("jsonList", jsonList); return responseMap; }
	 * 
	 * 
	 * @SuppressWarnings({ "unchecked"})
	 * 
	 * @Override public String submitPatientDetails(String requestData ) {
	 * Map<String,Object> responseMap = new HashMap<String, Object>();
	 * 
	 * JSONObject json = new JSONObject(); JSONObject jObject = new
	 * JSONObject(requestData);
	 * 
	 * responseMap = patientRegistrationDao.submitPatientDetails(jObject);
	 * if(responseMap.size()>0) { List<Map<String,Object>> visitIdsList =
	 * (List<Map<String,Object>>)responseMap.get("visitList");
	 * if(Integer.parseInt(visitIdsList.get(0).get("visitId").toString())==-1) {
	 * json.put("status","0"); json.put("responseMap",responseMap); }else {
	 * json.put("status","1"); json.put("listSize",visitIdsList.size());
	 * json.put("responseMap",responseMap); } }
	 * 
	 * return json.toString(); }
	 * 
	 * 
	 * 
	 * @Override public Map<String, Object> showAppointmentForOthers(Map<String,
	 * String> requestData) { // TODO Auto-generated method stub
	 * Map<String,Object>map = new HashMap<String, Object>(); List<HashMap<String,
	 * Object>> registrationTypeList = new ArrayList<HashMap<String, Object>>();
	 * List<HashMap<String, Object>> departmentList = new ArrayList<HashMap<String,
	 * Object>>(); List<HashMap<String, Object>> genderList = new
	 * ArrayList<HashMap<String, Object>>(); List<HashMap<String, Object>>
	 * identificationList = new ArrayList<HashMap<String, Object>>();
	 * List<HashMap<String, Object>> serviceTypeList = new ArrayList<HashMap<String,
	 * Object>>(); List<HashMap<String, Object>> relationTypeList = new
	 * ArrayList<HashMap<String, Object>>();
	 * 
	 * String codeOpd = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_OPD");
	 * 
	 * long appointmentTypeIdOPD=
	 * patientRegistrationDao.getAppointmentTypeIdFromCode(codeOpd);
	 * 
	 * map.put("appointmentTypeIdOPD",appointmentTypeIdOPD);
	 * 
	 * 
	 * List<MasRegistrationType>respRegistrationTypeList =
	 * patientRegistrationDao.getRegistrationTypeList();
	 * if(!respRegistrationTypeList.isEmpty() && respRegistrationTypeList.size()>0)
	 * { for(MasRegistrationType registrationType:respRegistrationTypeList) {
	 * HashMap<String,Object> registrationTypetMap=new HashMap<String, Object>();
	 * registrationTypetMap.put("registrationTypeId",
	 * registrationType.getRegistrationTypeId());
	 * registrationTypetMap.put("registrationTypeName",
	 * registrationType.getRegistrationTypeName());
	 * registrationTypeList.add(registrationTypetMap); }
	 * map.put("registrationTypeList",registrationTypeList); }
	 * 
	 * long hospitalId = Long.parseLong(requestData.get("hospitalId").toString());
	 * List<MasDepartment>respDepartmentList =
	 * patientRegistrationDao.getDepartmentListFromAppointmentSession(hospitalId);
	 * if(respDepartmentList.size()>0) { for(MasDepartment
	 * departmentFromSession:respDepartmentList) { HashMap<String,Object>
	 * departmentMap=new HashMap<String, Object>(); if(departmentFromSession!=null)
	 * { departmentMap.put("departmentId", departmentFromSession.getDepartmentId());
	 * departmentMap.put("departmentName",
	 * departmentFromSession.getDepartmentName());
	 * departmentList.add(departmentMap); } } map.put("departmentList",
	 * departmentList); }else { map.put("departmentList", departmentList); }
	 * 
	 * 
	 * List<MasAdministrativeSex>respGenderList=
	 * patientRegistrationDao.getGenderList(); if(!respGenderList.isEmpty() &&
	 * respGenderList.size()>0) { for(MasAdministrativeSex gender:respGenderList) {
	 * HashMap<String,Object> genderMap=new HashMap<String, Object>();
	 * genderMap.put("administrativeSexId", gender.getAdministrativeSexId());
	 * genderMap.put("administrativeSexName", gender.getAdministrativeSexName());
	 * genderList.add(genderMap); } map.put("genderList",genderList); }
	 * 
	 * 
	 * List<MasIdentificationType>respIdentificationList=
	 * patientRegistrationDao.getIdentificationList();
	 * if(!respIdentificationList.isEmpty() && respIdentificationList.size()>0) {
	 * for(MasIdentificationType identificationType:respIdentificationList) {
	 * HashMap<String,Object> identificationTypeMap=new HashMap<String, Object>();
	 * identificationTypeMap.put("identificationTypeId",
	 * identificationType.getIdentificationTypeId());
	 * identificationTypeMap.put("identificationTypetName",
	 * identificationType.getIdentificationName());
	 * identificationList.add(identificationTypeMap); }
	 * map.put("identificationList",identificationList); }
	 * 
	 * List<MasServiceType>respServiceTypeList=patientRegistrationDao.
	 * getServiceTypeList(); if(!respServiceTypeList.isEmpty() &&
	 * respServiceTypeList.size()>0) { for(MasServiceType
	 * serviceType:respServiceTypeList) { HashMap<String,Object> serviceTypeMap=new
	 * HashMap<String, Object>(); serviceTypeMap.put("serviceTypeId",
	 * serviceType.getServiceTypeId()); serviceTypeMap.put("serviceTypeName",
	 * serviceType.getServiceTypeName()); serviceTypeList.add(serviceTypeMap); }
	 * map.put("serviceTypeList",serviceTypeList); }
	 * 
	 * List<MasRelation>respRelationTypeList=patientRegistrationDao.getRelationList(
	 * ); if(!respRelationTypeList.isEmpty() && respRelationTypeList.size()>0) {
	 * for(MasRelation relationType:respRelationTypeList) { HashMap<String,Object>
	 * relationTypeMap=new HashMap<String, Object>();
	 * relationTypeMap.put("relationTypeId", relationType.getRelationId());
	 * relationTypeMap.put("relationTypeName", relationType.getRelationName());
	 * relationTypeList.add(relationTypeMap); }
	 * map.put("relationTypeList",relationTypeList); }
	 * 
	 * return map; }
	 * 
	 * @Override public Map<String, Object>
	 * getTokenNoOfDepartmentForOthers(Map<String, Object> requestData) {
	 * Map<String, Object> rosterValueForToken = new HashMap<String, Object>();
	 * Map<String, Object> map = new HashMap<String, Object>(); long departmentId=0;
	 * long hospitalId=0; long appointmentTypeId=0; long patientId=0;
	 * 
	 * String tokenMsg=""; String visitFlag=""; String visitDate="";
	 * 
	 * departmentId = Long.parseLong(requestData.get("deptId").toString());
	 * hospitalId = Long.parseLong(requestData.get("hospitalId").toString());
	 * appointmentTypeId=Long.parseLong(requestData.get("appointmentTypeId").
	 * toString()); visitFlag= requestData.get("visitFlag").toString();
	 * 
	 * rosterValueForToken =
	 * patientRegistrationDao.getTokenNoForDepartmentMultiVisit(departmentId,
	 * hospitalId,appointmentTypeId,visitFlag,visitDate,patientId);
	 * 
	 * if(!rosterValueForToken.isEmpty() && rosterValueForToken.size()>0) {
	 * tokenMsg= (String)rosterValueForToken.get("tokenMsg"); appointmentTypeId =
	 * (long) rosterValueForToken.get("appointmentTypeId");
	 * map.put("tokenMsg",tokenMsg); map.put("appointmentTypeId",appointmentTypeId);
	 * map.put("status","1"); }else { tokenMsg= "Token is not available";
	 * map.put("tokenMsg",tokenMsg); map.put("status","0"); } return map; }
	 * 
	 * 
	 * @Override public String submitPatientDetailsForOthers(String requestData) {
	 * long visitId=0; JSONObject json = new JSONObject(); JSONObject jObject = new
	 * JSONObject(requestData);
	 * 
	 * visitId = patientRegistrationDao.submitPatientDetailsForOthers(jObject);
	 * if(visitId!=0) { if(visitId==-1) { json.put("status","0");
	 * json.put("msg","Patient is already registered."); }else if(visitId==-2) {
	 * json.put("status","0"); json.put("msg","Some error occured."); }else {
	 * json.put("visitId",visitId); json.put("status","1");
	 * json.put("msg","Appointment created successfully."); } }else {
	 * json.put("status","0"); json.put("msg","Appointment is already booked."); }
	 * return json.toString();
	 * 
	 * }
	 * 
	 * 
	 * @Override public Map<String, Object>
	 * searchOthersRegisteredPatient(Map<String, String> requestData) { Map<String,
	 * Object> responseMap = new HashMap<String, Object>(); Map<Integer, Map<String,
	 * Object>> data = new HashMap<Integer, Map<String, Object>>();
	 * Map<String,List<Patient>> patientMap = new HashMap<String, List<Patient>>();
	 * List<Patient> existingPatient = new ArrayList<Patient>(); String
	 * serviceNo=""; String patientName=""; String uhinNo=""; String mobileNo="";
	 * long registrationTypeId=0;
	 * 
	 * if (requestData.get("UhidNoId") != null &&
	 * !requestData.get("UhidNoId").isEmpty()) {
	 * uhinNo=requestData.get("UhidNoId").toString() ; }
	 * 
	 * if (requestData.get("patientName") != null &&
	 * !requestData.get("patientName").isEmpty()) {
	 * patientName=requestData.get("patientName") ; }
	 * 
	 * if (requestData.get("searchServiceNo") != null &&
	 * !requestData.get("searchServiceNo").isEmpty()) {
	 * serviceNo=requestData.get("searchServiceNo"); }
	 * 
	 * if (requestData.get("searchMobileNo") != null &&
	 * !requestData.get("searchMobileNo").isEmpty()) {
	 * mobileNo=requestData.get("searchMobileNo"); }
	 * 
	 * if (requestData.get("registrationTypeId") != null &&
	 * !requestData.get("registrationTypeId").isEmpty()) {
	 * registrationTypeId=Long.parseLong(requestData.get("registrationTypeId")); }
	 * 
	 * 
	 * patientMap=patientRegistrationDao.searchOthersRegisteredPatient(uhinNo,
	 * patientName,serviceNo,mobileNo,registrationTypeId);
	 * if(patientMap.get("patientList").size()>0) { String name=""; String
	 * mobileNumber=""; String serviceType=""; String registrationTypeName="";
	 * String rank =""; String identificationTypeName=""; String gender=""; String
	 * idNumber="";
	 * 
	 * String age=""; long genderId=0; //long registrationTypeId=0; long
	 * serviceTypeId=0; long identificationTypeId=0; long id=0; String
	 * dateOfBirth="";
	 * 
	 * existingPatient = patientMap.get("patientList"); int rowCount=0; for(Patient
	 * patient: existingPatient) { Map<String, Object> responsePatientMap = new
	 * HashMap<String, Object>();
	 * 
	 * id=patient.getPatientId(); serviceNo
	 * =(patient.getServiceNo()!=null?patient.getServiceNo():""); name =
	 * patient.getPatientName();
	 * rank=(patient.getOtherRank()!=null?patient.getOtherRank():""); age =
	 * HMSUtil.calculateAgeNoOfYear(patient.getDateOfBirth()); age =
	 * HMSUtil.calculateAge(patient.getDateOfBirth()); gender =
	 * patient.getMasAdministrativeSex().getAdministrativeSexName(); genderId =
	 * patient.getMasAdministrativeSex().getAdministrativeSexId(); dateOfBirth =
	 * HMSUtil.changeDateToddMMyyyy(patient.getDateOfBirth());
	 * serviceTypeId=(patient.getMasServiceType()!=null?patient.getMasServiceType().
	 * getServiceTypeId():0);
	 * serviceType=(patient.getMasServiceType()!=null?patient.getMasServiceType().
	 * getServiceTypeName():""); uhinNo=patient.getUhidNo();
	 * mobileNumber=patient.getMobileNumber();
	 * registrationTypeId=(patient.getMasRegistrationType()!=null?patient.
	 * getMasRegistrationType().getRegistrationTypeId():0);
	 * registrationTypeName=(patient.getMasRegistrationType()!=null?patient.
	 * getMasRegistrationType().getRegistrationTypeName():""); identificationTypeId=
	 * (patient.getMasIdentificationType()!=null?patient.getMasIdentificationType().
	 * getIdentificationTypeId():0); identificationTypeName=
	 * (patient.getMasIdentificationType()!=null?patient.getMasIdentificationType().
	 * getIdentificationName():""); idNumber =
	 * (patient.getIdentificationNo()!=null?patient.getIdentificationNo():"");
	 * 
	 * 
	 * responsePatientMap.put("Id", id); responsePatientMap.put("name", name);
	 * responsePatientMap.put("serviceNo", serviceNo);
	 * responsePatientMap.put("rank", rank);
	 * 
	 * responsePatientMap.put("age", age); responsePatientMap.put("gender", gender);
	 * responsePatientMap.put("genderId", genderId);
	 * responsePatientMap.put("dateOfBirth", dateOfBirth);
	 * responsePatientMap.put("serviceTypeId", serviceTypeId);
	 * responsePatientMap.put("serviceType", serviceType);
	 * responsePatientMap.put("uhinNo", uhinNo);
	 * responsePatientMap.put("mobileNumber", mobileNumber);
	 * responsePatientMap.put("registrationTypeId", registrationTypeId);
	 * responsePatientMap.put("registrationTypeName", registrationTypeName);
	 * responsePatientMap.put("identificationTypeId", identificationTypeId);
	 * responsePatientMap.put("identificationTypeName", identificationTypeName);
	 * responsePatientMap.put("idNumber", idNumber);
	 * 
	 * data.put(++rowCount, responsePatientMap); }
	 * 
	 * responseMap.put("count", data.size()); responseMap.put("data", data);
	 * responseMap.put("msg", "Record Found"); responseMap.put("status", 1); }else {
	 * responseMap.put("count", data.size()); responseMap.put("msg",
	 * "No Record Found"); responseMap.put("status", 0); } return responseMap; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> patientListForUploadDocument(Map<String,
	 * String> requestData) { Map<String, Object> map = new HashMap<String,
	 * Object>(); Map<String, Object> patientListofEmpAndDependent = new
	 * HashMap<String, Object>(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); List<Patient> existingPatientList=
	 * new ArrayList<Patient>(); List<MasEmployee> empAndDependentPatientList= new
	 * ArrayList<MasEmployee>(); List<MasEmployeeDependent> dependentPatientList=
	 * new ArrayList<MasEmployeeDependent>();
	 * 
	 * String serviceNo = ""; String name=""; String dateOfBirth=""; String
	 * empServiceJoinDate=""; String empName=""; long genderId=0; long relationId=0;
	 * long empRankId=0; long empTradeId=0; long employeeId=0; long empUnitId=0;
	 * long empCommandId=0; long empRecordOfficeId=0; long empMaritalStatusId=0;
	 * MasEmployee employeeObject=null;
	 * 
	 * 
	 * try { if (!requestData.isEmpty() && requestData != null ) { if
	 * (requestData.get("serviceNo") != null &&
	 * !requestData.get("serviceNo").isEmpty()) { serviceNo =
	 * requestData.get("serviceNo").trim(); employeeObject =
	 * patientRegistrationDao.getMasEmployeeFromServiceNo(serviceNo);
	 * if(employeeObject!=null) { patientListofEmpAndDependent =
	 * patientRegistrationDao.findPatientAndDependentFromEmployee(serviceNo);
	 * if(patientListofEmpAndDependent.size()>0) { existingPatientList =
	 * (List<Patient>)patientListofEmpAndDependent.get("patientList");
	 * if(existingPatientList.size()>0) { int rowCount=0; for(Patient patient :
	 * existingPatientList) { Map<String, Object> responsePatientMap = new
	 * HashMap<String, Object>(); String patientName = ""; if
	 * (patient.getPatientName() != null) { patientName = patient.getPatientName();
	 * }
	 * 
	 * // Employee related details responsePatientMap.put("Id", ++rowCount);
	 * responsePatientMap.put("patientId", patient.getPatientId());
	 * responsePatientMap.put("uhidNo", patient.getUhidNo());
	 * responsePatientMap.put("serviceNo",patient.getServiceNo());
	 * responsePatientMap.put("name",patientName);
	 * responsePatientMap.put("genderId",patient.getMasAdministrativeSex().
	 * getAdministrativeSexId());
	 * 
	 * data.put(++rowCount, responsePatientMap); }
	 * 
	 * dependentPatientList =
	 * (List<MasEmployeeDependent>)patientListofEmpAndDependent.get(
	 * "employeeDependentList"); if(dependentPatientList.size()>0 ) {
	 * for(MasEmployeeDependent depList :dependentPatientList ) { Map<String,
	 * Object> responseDepMap = new HashMap<String, Object>(); String depPatientName
	 * = ""; if (depList.getEmployeeDependentName() != null) { depPatientName =
	 * depList.getEmployeeDependentName(); } //Patient related data name =
	 * depPatientName; if(depList.getDateOfBirth()!=null) {
	 * dateOfBirth=HMSUtil.changeDateToddMMyyyy(depList.getDateOfBirth()); }else {
	 * dateOfBirth=""; }
	 * 
	 * genderId =
	 * depList.getMasAdministrativeSex()!=null?depList.getMasAdministrativeSex().
	 * getAdministrativeSexId():0;
	 * relationId=depList.getMasRelation()!=null?depList.getMasRelation().
	 * getRelationId():0;
	 * 
	 * //Employee related serviceNo= employeeObject.getServiceNo(); String
	 * empRankCode = employeeObject.getMasRank()!=null?
	 * employeeObject.getMasRank():"0"; List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0) { empRankId =
	 * mRankList.get(0).getRankId(); empTradeId =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeId():0;
	 * 
	 * }else { empRankId = 0; empTradeId = 0; } empName=
	 * employeeObject.getEmployeeName()!=null?employeeObject.getEmployeeName():"";
	 * employeeId = employeeObject.getEmployeeId();
	 * if(employeeObject.getDoe()!=null) {
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(employeeObject.getDoe());
	 * }else { empServiceJoinDate=""; }
	 * 
	 * // This getMasUnit() gives unitCode not unit object. String empUnitCode =
	 * employeeObject.getMasUnit()!=null?employeeObject.getMasUnit():"0";
	 * List<MasUnit> unitList = patientRegistrationDao.getEmpUnitId(empUnitCode);
	 * if(unitList.size()>0) { empUnitId = unitList.get(0).getUnitId();
	 * if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id }else { empCommandId=0; } }else { empUnitId = 0; }
	 * empRecordOfficeId=employeeObject.getMasRecordOfficeAddress()!=null?
	 * employeeObject.getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
	 * empMaritalStatusId=employeeObject.getMasMaritalStatus()!=null?employeeObject.
	 * getMasMaritalStatus().getMaritalStatusId():0;
	 * 
	 * responseDepMap.put("Id", ++rowCount); responseDepMap.put("uhidNo", "");
	 * responseDepMap.put("patientId","0"); responseDepMap.put("employeeId",
	 * employeeId); responseDepMap.put("name", name); responseDepMap.put("genderId",
	 * genderId); responseDepMap.put("dateOfBirth", dateOfBirth);
	 * responseDepMap.put("relationId", relationId);
	 * responseDepMap.put("serviceNo",serviceNo);
	 * responseDepMap.put("empRankId",empRankId);
	 * responseDepMap.put("empTradeId",empTradeId);
	 * responseDepMap.put("empName",empName);
	 * responseDepMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseDepMap.put("empUnitId",empUnitId);
	 * responseDepMap.put("empCommandId",empCommandId);
	 * responseDepMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseDepMap.put("empMaritalStatusId",empMaritalStatusId);
	 * data.put(++rowCount, responseDepMap);
	 * 
	 * } }
	 * 
	 * 
	 * empAndDependentPatientList =
	 * (List<MasEmployee>)patientListofEmpAndDependent.get("employeeList");
	 * if(empAndDependentPatientList.size()>0 ) { for(MasEmployee ms :
	 * empAndDependentPatientList) {
	 * 
	 * String selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); relationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * Map<String, Object> responseEmpMap = new HashMap<String, Object>(); String
	 * patientName = ""; if (ms.getEmployeeName() != null) { patientName =
	 * ms.getEmployeeName(); } serviceNo= ms.getServiceNo();
	 * employeeId=ms.getEmployeeId();
	 * 
	 * String empRankCode = ms.getMasRank()!=null? ms.getMasRank():"0";
	 * List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0) { empRankId =
	 * mRankList.get(0).getRankId(); empTradeId =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeId():0;
	 * 
	 * }else { empRankId = 0; empTradeId = 0; }
	 * 
	 * empName= patientName; if(ms.getDoe()!=null) {
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(ms.getDoe()); }else {
	 * empServiceJoinDate=""; }
	 * 
	 * if(ms.getDob()!=null) {
	 * dateOfBirth=HMSUtil.changeDateToddMMyyyy(ms.getDoe()); }else {
	 * dateOfBirth=""; }
	 * 
	 * // This getMasUnit gives unit code not unit object String empUnitCode =
	 * ms.getMasUnit()!=null?ms.getMasUnit():"0"; List<MasUnit> unitList =
	 * patientRegistrationDao.getEmpUnitId(empUnitCode); if(unitList.size()>0) {
	 * empUnitId = unitList.get(0).getUnitId(); if(unitList.get(0)!=null &&
	 * unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id }else { empCommandId=0; }
	 * 
	 * }else { empUnitId = 0; }
	 * 
	 * empRecordOfficeId=ms.getMasRecordOfficeAddress()!=null?ms.
	 * getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
	 * empMaritalStatusId=ms.getMasMaritalStatus()!=null?ms.getMasMaritalStatus().
	 * getMaritalStatusId():0;
	 * 
	 * name = patientName; genderId =
	 * ms.getMasAdministrativeSex()!=null?ms.getMasAdministrativeSex().
	 * getAdministrativeSexId():0;
	 * 
	 * 
	 * responseEmpMap.put("Id", ++rowCount); responseEmpMap.put("uhidNo", "");
	 * responseEmpMap.put("patientId","0"); responseEmpMap.put("employeeId",
	 * employeeId); responseEmpMap.put("name", name); responseEmpMap.put("genderId",
	 * genderId); responseEmpMap.put("dateOfBirth", dateOfBirth);
	 * responseEmpMap.put("relationId", relationId);
	 * 
	 * responseEmpMap.put("serviceNo",serviceNo);
	 * responseEmpMap.put("empRankId",empRankId);
	 * responseEmpMap.put("empTradeId",empTradeId);
	 * responseEmpMap.put("empName",empName);
	 * responseEmpMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseEmpMap.put("empUnitId",empUnitId);
	 * responseEmpMap.put("empCommandId",empCommandId);
	 * responseEmpMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseEmpMap.put("empMaritalStatusId",empMaritalStatusId);
	 * 
	 * 
	 * data.put(++rowCount, responseEmpMap); } } map.put("data", data);
	 * map.put("count", data.size()); map.put("msg","List of Detail");
	 * map.put("status", "1"); return map;
	 * 
	 * }else { empAndDependentPatientList =
	 * (List<MasEmployee>)patientListofEmpAndDependent.get("employeeList"); int
	 * rowCount=0; for (MasEmployee ms : empAndDependentPatientList) {
	 * 
	 * String selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); relationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * Map<String, Object> responseEmpMap = new HashMap<String, Object>();
	 * 
	 * String patientName = ""; if (ms.getEmployeeName() != null) { patientName =
	 * ms.getEmployeeName(); }
	 * 
	 * empName= patientName; serviceNo = ms.getServiceNo(); employeeId =
	 * ms.getEmployeeId();
	 * 
	 * String empRankCode = ms.getMasRank()!=null? ms.getMasRank():"0";
	 * List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0) { empRankId =
	 * mRankList.get(0).getRankId(); empTradeId =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeId():0;
	 * 
	 * }else { empRankId = 0; empTradeId = 0; }
	 * 
	 * if(ms.getDoe()!=null) {
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(ms.getDoe()); }else {
	 * empServiceJoinDate=""; }
	 * 
	 * if(ms.getDob()!=null) { dateOfBirth
	 * =HMSUtil.changeDateToddMMyyyy(ms.getDob()); }else { dateOfBirth=""; }
	 * 
	 * 
	 * String empUnitCode = ms.getMasUnit()!=null?ms.getMasUnit():"0";
	 * 
	 * List<MasUnit> unitList = patientRegistrationDao.getEmpUnitId(empUnitCode);
	 * if(!unitList.isEmpty() && unitList.size()>0) { empUnitId =
	 * unitList.get(0).getUnitId(); if(unitList.get(0)!=null &&
	 * unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id }else { empCommandId=0; }
	 * 
	 * }else { empUnitId = 0; }
	 * 
	 * empRecordOfficeId=ms.getMasRecordOfficeAddress()!=null?ms.
	 * getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
	 * empMaritalStatusId=ms.getMasMaritalStatus()!=null?ms.getMasMaritalStatus().
	 * getMaritalStatusId():0;
	 * 
	 * name = patientName; genderId =
	 * ms.getMasAdministrativeSex()!=null?ms.getMasAdministrativeSex().
	 * getAdministrativeSexId():0;
	 * 
	 * 
	 * responseEmpMap.put("Id", ++rowCount); responseEmpMap.put("uhidNo", "");
	 * responseEmpMap.put("employeeId", employeeId);
	 * responseEmpMap.put("patientId","0"); responseEmpMap.put("name", name);
	 * responseEmpMap.put("genderId", genderId); responseEmpMap.put("dateOfBirth",
	 * dateOfBirth); responseEmpMap.put("relationId", relationId);
	 * responseEmpMap.put("serviceNo",serviceNo);
	 * responseEmpMap.put("empRankId",empRankId);
	 * responseEmpMap.put("empTradeId",empTradeId);
	 * responseEmpMap.put("empName",empName);
	 * responseEmpMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseEmpMap.put("empUnitId",empUnitId);
	 * responseEmpMap.put("empCommandId",empCommandId);
	 * responseEmpMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseEmpMap.put("empMaritalStatusId",empMaritalStatusId);
	 * 
	 * data.put(++rowCount, responseEmpMap);
	 * 
	 * List<MasEmployeeDependent> dependentList =
	 * (List<MasEmployeeDependent>)patientListofEmpAndDependent.get(
	 * "employeeDependentList"); if (dependentList.size()>0) { for
	 * (MasEmployeeDependent depList : dependentList) { Map<String, Object>
	 * responseDepMap = new HashMap<String, Object>(); String depPatientName = "";
	 * if (depList.getEmployeeDependentName() != null) { depPatientName =
	 * depList.getEmployeeDependentName(); } name = depPatientName;
	 * if(depList.getDateOfBirth()!=null) { dateOfBirth =
	 * HMSUtil.changeDateToddMMyyyy(depList.getDateOfBirth()); }else { dateOfBirth =
	 * ""; }
	 * 
	 * genderId =
	 * depList.getMasAdministrativeSex()!=null?depList.getMasAdministrativeSex().
	 * getAdministrativeSexId():0;
	 * 
	 * relationId=depList.getMasRelation()!=null?depList.getMasRelation().
	 * getRelationId():0;
	 * 
	 * //Employee related serviceNo=
	 * employeeObject!=null?employeeObject.getServiceNo():""; if(mRankList.size()>0)
	 * { empRankId = mRankList.get(0).getRankId(); empTradeId =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeId():0;
	 * 
	 * }else { empRankId = 0; empTradeId = 0; } // This getMasUnit() gives unitCode
	 * not unit object. if(unitList.size()>0) { empUnitId =
	 * unitList.get(0).getUnitId(); if(unitList.get(0)!=null &&
	 * unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id }else { empCommandId=0; } }else { empUnitId = 0; }
	 * empName= employeeObject!=null?employeeObject.getEmployeeName():""; employeeId
	 * =employeeObject!=null?employeeObject.getEmployeeId():0;
	 * if(employeeObject.getDoe()!=null) {
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(employeeObject.getDoe());
	 * }else { empServiceJoinDate=""; }
	 * 
	 * empRecordOfficeId=employeeObject.getMasRecordOfficeAddress()!=null?
	 * employeeObject.getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
	 * empMaritalStatusId=employeeObject.getMasMaritalStatus()!=null?employeeObject.
	 * getMasMaritalStatus().getMaritalStatusId():0;
	 * 
	 * 
	 * responseDepMap.put("Id", ++rowCount); responseDepMap.put("uhidNo", "");
	 * responseDepMap.put("employeeId", employeeId);
	 * responseDepMap.put("patientId","0"); responseDepMap.put("name", name);
	 * responseDepMap.put("genderId", genderId); responseDepMap.put("dateOfBirth",
	 * dateOfBirth); responseDepMap.put("relationId", relationId);
	 * 
	 * responseDepMap.put("serviceNo",serviceNo);
	 * responseDepMap.put("empRankId",empRankId);
	 * responseDepMap.put("empTradeId",empTradeId);
	 * responseDepMap.put("empName",empName);
	 * responseDepMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseDepMap.put("empUnitId",empUnitId);
	 * responseDepMap.put("empCommandId",empCommandId);
	 * responseDepMap.put("empRecordOfficeId",empRecordOfficeId);
	 * 
	 * data.put(++rowCount, responseDepMap); } } map.put("data", data);
	 * map.put("count", data.size()); map.put("msg","List of Detail");
	 * map.put("status", "1"); return map; } } }
	 * 
	 * } } } map.put("msg","Service no:" + serviceNo + " does not exist.");
	 * map.put("count", data.size()); map.put("status", "0"); return map;
	 * 
	 * }catch(Exception e) { map.put("msg", "Some error occurred in service no: " +
	 * serviceNo); map.put("status", "0"); e.printStackTrace(); return map; } }
	 * 
	 * 
	 * @Override public Map<String, Object> patientAppointmentHistory(Map<String,
	 * String> requestData) { List<Visit> patientHistoryList = new
	 * ArrayList<Visit>(); List<Map<String,Object>> responseList = new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); long uhidNo=0; String startDate=""; String
	 * endDate="";
	 * 
	 * if(!requestData.get("uhidNo").isEmpty() && requestData.get("uhidNo")!=null) {
	 * uhidNo =Long.parseLong(requestData.get("uhidNo").toString()); }
	 * 
	 * if(!requestData.get("startDate").isEmpty() &&
	 * requestData.get("startDate")!=null) { startDate
	 * =requestData.get("startDate").toString(); }
	 * 
	 * if(!requestData.get("endDate").isEmpty() && requestData.get("endDate")!=null)
	 * { endDate =requestData.get("endDate").toString(); }
	 * 
	 * 
	 * 
	 * patientHistoryList =
	 * patientRegistrationDao.getPatientAppointmentHistory(uhidNo,startDate,endDate)
	 * ; if(patientHistoryList!=null && patientHistoryList.size()>0) { for(Visit
	 * visit : patientHistoryList) { Map<String,Object>map=new HashMap<String,
	 * Object>(); map.put("departmentName",
	 * visit.getMasDepartment().getDepartmentName()); map.put("departmentId",
	 * visit.getMasDepartment().getDepartmentId()); map.put("doctorName",
	 * "DoctorName"); map.put("appointmentDate",
	 * HMSUtil.changeDateToddMMyyyy(visit.getVisitDate())); map.put("tokenNo",
	 * visit.getTokenNo()); map.put("hospitalName",
	 * visit.getMasHospital().getHospitalName()); map.put("hospitalId",
	 * visit.getMasHospital().getHospitalId()); map.put("visitStatus",
	 * visit.getVisitStatus()); map.put("visitId", visit.getVisitId());
	 * map.put("appointmentTypeId",
	 * visit.getMasAppointmentType().getAppointmentTypeId());
	 * map.put("appointmentTypeName",
	 * visit.getMasAppointmentType().getAppointmentTypeName());
	 * responseList.add(map); } if (responseList != null && responseList.size() > 0)
	 * { responseMap.put("patientHistoryList", responseList);
	 * responseMap.put("status", 1); } }else { responseMap.put("patientHistoryList",
	 * responseList); responseMap.put("msg", "No History Available");
	 * responseMap.put("status", 0); } return responseMap; }
	 * 
	 * 
	 * @Override public Map<String,Object> patientVisitCancellation(Map<String,
	 * String> requestData) { Map<String,Object> map = new HashMap<String,
	 * Object>(); long visitId=0; boolean status=false;
	 * 
	 * if(!requestData.get("visitId").isEmpty() && requestData.get("visitId")!=null)
	 * { visitId =Long.parseLong(requestData.get("visitId").toString()); } status =
	 * patientRegistrationDao.getPatientVisitCancellation(visitId); if(status) {
	 * map.put("msg", "Appointment cancelled"); }else { map.put("msg",
	 * "Appointment can not be cancelled"); } return map; }
	 * 
	 * 
	 * 
	 * @Override public String getAppointmentTypeList(HttpServletRequest request,
	 * HttpServletResponse response) { JSONObject jsonObj = new JSONObject();
	 * List<MasAppointmentType> appList =
	 * patientRegistrationDao.getAppointmentTypeList(); List<Map<String,Object>>
	 * appointmentTypeList = new ArrayList<Map<String,Object>>(); if(appList !=null
	 * && appList.size()>0) { for(MasAppointmentType mas :appList) { Map<String,
	 * Object> map = new HashMap<String, Object>();
	 * map.put("id",mas.getAppointmentTypeId()); map.put("appointmentType",
	 * mas.getAppointmentTypeName()); map.put("appointmentTypeCode",
	 * mas.getAppointmentTypeCode()); appointmentTypeList.add(map); }
	 * jsonObj.put("data", appointmentTypeList); }
	 * 
	 * System.out.println(jsonObj.toString()); return jsonObj.toString(); }
	 * 
	 * 
	 * @Override public Map<String, Object> getExamSubType(Map<String, Object>
	 * requestData) { Map<String,Object> map = new HashMap<String, Object>();
	 * List<HashMap<String, Object>> masExamList = new ArrayList<HashMap<String,
	 * Object>>(); long appTypeId=
	 * Long.parseLong(requestData.get("appTypeId").toString());
	 * 
	 * List<MasMedExam>examList
	 * =patientRegistrationDao.getExamList(appTypeId,requestData);
	 * if(!examList.isEmpty() && examList.size()>0) { for(MasMedExam exam:examList)
	 * { HashMap<String,Object> examMap=new HashMap<String, Object>();
	 * examMap.put("examId", exam.getMedicalExamId()); examMap.put("examName",
	 * exam.getMedicalExamName()); examMap.put("medicalExamCode",
	 * exam.getMedicalExamCode()); masExamList.add(examMap); }
	 * map.put("status","1"); map.put("masExamList",masExamList);
	 * map.put("appTypeId",appTypeId); }else { map.put("status","0");
	 * map.put("masExamList",masExamList); map.put("appTypeId",appTypeId); } return
	 * map; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object>
	 * getFutureAppointmentWaitingList(Map<String, Object> requestData) {
	 * Map<String,Object> map = new HashMap<String, Object>(); Map<String,Object>
	 * visitMap = new HashMap<String, Object>();
	 * List<Visit>futureAppointmentWaitingList = new ArrayList<Visit>();
	 * List<Integer>totalMatches = new ArrayList<Integer>(); List<HashMap<String,
	 * Object>> waitingListOfFutureAppointment = new ArrayList<HashMap<String,
	 * Object>>(); long hospitalId=
	 * Long.parseLong(requestData.get("hospitalId").toString()); int pageNo=
	 * Integer.parseInt(requestData.get("PN").toString());
	 * 
	 * visitMap=patientRegistrationDao.getFutureAppointmentWaitingList(hospitalId,
	 * pageNo); futureAppointmentWaitingList = (List<Visit>)
	 * visitMap.get("futureAppointmentWatingList"); totalMatches = (List<Integer>)
	 * visitMap.get("totalMatches"); if(!futureAppointmentWaitingList.isEmpty() &&
	 * futureAppointmentWaitingList.size()>0) { for(Visit
	 * list:futureAppointmentWaitingList) { HashMap<String,Object>
	 * futureAppointmentMap=new HashMap<String, Object>();
	 * futureAppointmentMap.put("visitId", list.getVisitId());
	 * futureAppointmentMap.put("tokenNo", list.getTokenNo());
	 * futureAppointmentMap.put("serviceNo", list.getPatient().getServiceNo());
	 * futureAppointmentMap.put("patientId", list.getPatient().getPatientId());
	 * futureAppointmentMap.put("patientName", list.getPatient().getPatientName());
	 * futureAppointmentMap.put("empName", list.getPatient().getEmployeeName());
	 * futureAppointmentMap.put("rank",
	 * list.getPatient().getMasRank().getRankName());
	 * futureAppointmentMap.put("relation",
	 * list.getPatient().getMasRelation().getRelationName());
	 * futureAppointmentMap.put("department",
	 * list.getMasDepartment().getDepartmentName());
	 * futureAppointmentMap.put("visitDate",
	 * HMSUtil.changeDateToddMMyyyy(list.getVisitDate()));
	 * futureAppointmentMap.put("visitTime",
	 * HMSUtil.convertTimeinHHMMfromLong(list.getVisitDate().getTime()));
	 * futureAppointmentMap.put("appointmentTypeId",
	 * list.getMasAppointmentType().getAppointmentTypeId());
	 * futureAppointmentMap.put("appointmentType",
	 * list.getMasAppointmentType().getAppointmentTypeName());
	 * futureAppointmentMap.put("visitStatus", list.getVisitStatus());
	 * futureAppointmentMap.put("cancelStatus",
	 * list.getCancelStatus()!=null?list.getCancelStatus():"");
	 * 
	 * waitingListOfFutureAppointment.add(futureAppointmentMap); }
	 * map.put("waitingListOfFutureAppointment",waitingListOfFutureAppointment);
	 * map.put("count",totalMatches.size()); map.put("status", 1); }else {
	 * map.put("waitingListOfFutureAppointment",waitingListOfFutureAppointment);
	 * map.put("count",totalMatches.size()); map.put("status", 0); } return map;
	 * 
	 * }
	 * 
	 * 
	 * @Override public String getInvestigationEmptyResultByOrderNo(HashMap<String,
	 * Object> jsondata,HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); List<Object[]> listObject = null; //
	 * List<Object[]> listObject1 = null; List<HashMap<String, Object>> c = new
	 * ArrayList<HashMap<String, Object>>(); List<HashMap<String, Object>>
	 * responseList = new ArrayList<HashMap<String, Object>>(); Long
	 * mainChargeCodeId=null; try { if (jsondata.get("orderNumberValue") != null) {
	 * 
	 * if (jsondata.containsKey("radioInvAndImaValue")) {
	 * 
	 * String mainChargeCode=jsondata.get("radioInvAndImaValue").toString();
	 * mainChargeCode=OpdServiceImpl.getReplaceString(mainChargeCode);
	 * if(StringUtils.isNotEmpty(mainChargeCode)) { MasMainChargecode mmcc=null;
	 * mmcc = opdMasterDaoImpl.getMainChargeCode(mainChargeCode);
	 * mainChargeCodeId=mmcc.getMainChargecodeId(); listObject =
	 * patientRegistrationDao
	 * .getInvestigationResultEmptyForOrderNo(jsondata.get("orderNumberValue").
	 * toString(),mainChargeCodeId); listObject1 = patientRegistrationDao
	 * .getInvestigationResultEmptyForOrderNoForUnique(jsondata.get(
	 * "orderNumberValue").toString(),mainChargeCodeId); }
	 * 
	 * }
	 * 
	 * } Long investigationId = null; String investigationName = ""; Long orderHdId
	 * = null; String urgent = ""; String labMark = ""; String orderDate = ""; Long
	 * visitId = null; String otherInvestigation = ""; Long departId = null; Long
	 * hospitalId = null; Long dgOrderDtId = null; Long dgResultDt = null; Long
	 * dgResultHd = null; String result = ""; String uomName=""; String
	 * minNormalValue = ""; String maxNormalValue=""; String investigationType="";
	 * Long mainChargeCodeForInv=null; Long subChargeCodeIdForInv=null;
	 * List<Long>listInvestigation=new ArrayList<>(); if (listObject!=null) {
	 * 
	 * for (Iterator<?> it = listObject.iterator(); it.hasNext();) { Object[] row =
	 * (Object[]) it.next();
	 * 
	 * HashMap<String, Object> pt = new HashMap<String, Object>();
	 * 
	 * if (row[0] != null) { investigationId = Long.parseLong(row[0].toString());
	 * 
	 * } if (row[1] != null) { investigationName = row[1].toString(); }
	 * 
	 * if (row[2] != null) { orderHdId = Long.parseLong(row[2].toString()); }
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
	 * } else { orderDate = ""; } if (row[6] != null) { visitId =
	 * Long.parseLong(row[6].toString()); } if (row[7] != null) { otherInvestigation
	 * = row[7].toString(); } if (row[8] != null) { departId =
	 * Long.parseLong(row[8].toString()); } if (row[9] != null) { hospitalId =
	 * Long.parseLong(row[9].toString()); } if (row[10] != null) { dgOrderDtId =
	 * Long.parseLong(row[10].toString()); } else { dgOrderDtId = null; }
	 * 
	 * if (row[11] != null && !row[11].equals("")) { dgResultDt =
	 * Long.parseLong(row[11].toString()); } else { dgResultDt = null; } if (row[12]
	 * != null && !row[12].equals("")) { dgResultHd =
	 * Long.parseLong(row[12].toString()); } else { dgResultHd = null; } if (row[13]
	 * != null) { result = row[13].toString(); } else { result = ""; } if (row[14]
	 * != null) { uomName = row[14].toString(); } else { uomName = ""; } if (row[15]
	 * != null) { minNormalValue = row[15].toString(); } else { minNormalValue = "";
	 * } if (row[16] != null) { maxNormalValue = row[16].toString(); } else {
	 * maxNormalValue = ""; }
	 * 
	 * if (row[17] != null) { investigationType = row[17].toString();
	 * if(investigationType!="" && investigationType.equalsIgnoreCase("m"))
	 * listInvestigation.add(investigationId); } else { investigationType = ""; }
	 * 
	 * 
	 * 
	 * if (row[18] != null) { subChargeCodeIdForInv =
	 * Long.parseLong(row[18].toString()); } else { subChargeCodeIdForInv =null; }
	 * 
	 * if (row[19] != null) { mainChargeCodeForInv =
	 * Long.parseLong(row[19].toString()); } else { mainChargeCodeForInv =null; }
	 * 
	 * 
	 * pt.put("investigationName", investigationName); pt.put("investigationId",
	 * investigationId); pt.put("labMark", labMark); pt.put("urgent", urgent);
	 * pt.put("orderDate", orderDate); pt.put("visitId", visitId);
	 * pt.put("otherInvestigation", otherInvestigation); pt.put("departId",
	 * departId); pt.put("hospitalId", hospitalId); pt.put("dgOrderDtId",
	 * dgOrderDtId); pt.put("orderHdId", orderHdId);
	 * 
	 * pt.put("dgResultDt", dgResultDt); pt.put("dgResultHd", dgResultHd);
	 * pt.put("result", result); pt.put("uomName", uomName);
	 * 
	 * pt.put("minNormalValue", minNormalValue); pt.put("maxNormalValue",
	 * maxNormalValue); pt.put("investigationType", investigationType);
	 * 
	 * pt.put("mainChargeCodeForInv", mainChargeCodeForInv);
	 * pt.put("subChargeCodeIdForInv", subChargeCodeIdForInv);
	 * 
	 * c.add(pt); //} if(investigation==1) { break; } } //} json.put("listObject",
	 * c); json.put("status", "1"); }
	 * 
	 * else { json.put("listObject", c); json.put("status", "0");
	 * 
	 * }
	 * 
	 * List<Object[]> listDgMasInvestigation=null; // List<Object[]>
	 * listDgMasInvestigation1=null; //if(investigationType!=null &&
	 * investigationType.equalsIgnoreCase("m"))
	 * if(CollectionUtils.isNotEmpty(listInvestigation)) listDgMasInvestigation=
	 * patientRegistrationDao.
	 * getDgMasInvestigationByInvestigationIds(listInvestigation,jsondata.get(
	 * "orderNumberValue").toString(),mainChargeCodeId); // listDgMasInvestigation1=
	 * patientRegistrationDao.
	 * getDgMasInvestigationByInvestigationIdsForUnique(listInvestigation,jsondata.
	 * get("orderNumberValue").toString(),mainChargeCodeId);
	 * 
	 * 
	 * String genderCode= cd. getAdminSexCode(jsondata);
	 * 
	 * Long subInvestigationId=null; String subInvestigationName=""; Long
	 * orderDtIdForSub=null; Long orderHdIdForSub=null; Long
	 * resultEntryDtidForSub=null; Long resultEntryHdidForSub=null; Long
	 * investigationIdSub=null; String rangeSub=""; String umoNameSub=""; String
	 * investigationTypeSub=""; Long ridcIdSub=null; String resultForSub=""; String
	 * investigationNameSub=""; String subInvestigationCadeForSub=""; Long
	 * mainChargeCodeIdForSub=null; Long subMainChargeCodeIdForSub=null; if
	 * (listDgMasInvestigation != null) {
	 * 
	 * 
	 * for (Iterator<?> it2 = listDgMasInvestigation1.iterator(); it2.hasNext();) {
	 * Object[] row3 = (Object[]) it2.next(); int subInvestigation=0;
	 * 
	 * 
	 * for (Iterator<?> it1 = listDgMasInvestigation.iterator(); it1.hasNext();) {
	 * Object[] row1 = (Object[]) it1.next(); HashMap<String, Object> jsonMap = new
	 * HashMap<String, Object>();
	 * 
	 * if(row3[0]!=null && row1[0]!=null &&
	 * Long.parseLong(row3[0].toString())==Long.parseLong(row1[0].toString())) {
	 * subInvestigation++; } else {
	 * 
	 * subInvestigation=0; } //if(subInvestigation==1) {
	 * 
	 * if (row1[0] != null) { subInvestigationId =
	 * Long.parseLong(row1[0].toString());
	 * jsonMap.put("subInvestigationId",subInvestigationId); } else {
	 * subInvestigationId=null; }
	 * 
	 * jsonMap.put("subInvestigationId",subInvestigationId);
	 * 
	 * 
	 * if (row1[1] != null) { subInvestigationName = row1[1].toString(); } else {
	 * subInvestigationName=""; }
	 * jsonMap.put("subInvestigationName",subInvestigationName);
	 * 
	 * if (row1[2] != null) { orderDtIdForSub = Long.parseLong(row1[2].toString());
	 * } else { orderDtIdForSub=null; }
	 * jsonMap.put("orderDtIdForSub",orderDtIdForSub);
	 * 
	 * 
	 * if (row1[3] != null) { orderHdIdForSub = Long.parseLong(row1[3].toString());
	 * } else { orderHdIdForSub=null; }
	 * jsonMap.put("orderHdIdForSub",orderHdIdForSub);
	 * 
	 * 
	 * if (row1[4] != null) { resultEntryDtidForSub =
	 * Long.parseLong(row1[4].toString()); } else { resultEntryDtidForSub=null; }
	 * jsonMap.put("resultEntryDtidForSub",resultEntryDtidForSub);
	 * 
	 * 
	 * if (row1[5] != null) { resultEntryHdidForSub =
	 * Long.parseLong(row1[5].toString()); } else { resultEntryHdidForSub=null; }
	 * jsonMap.put("resultEntryHdidForSub",resultEntryHdidForSub);
	 * 
	 * 
	 * if (row1[7] != null) { investigationTypeSub = row1[7].toString(); } else {
	 * investigationTypeSub=""; }
	 * jsonMap.put("investigationTypeSub",investigationTypeSub);
	 * 
	 * 
	 * if (row1[8] != null) { investigationIdSub =
	 * Long.parseLong(row1[8].toString()); } else { investigationIdSub=null; }
	 * jsonMap.put("investigationIdSub",investigationIdSub);
	 * 
	 * 
	 * if (row1[9] != null) { investigationNameSub = row1[9].toString(); } else {
	 * investigationNameSub=""; }
	 * jsonMap.put("investigationNameSub",investigationNameSub);
	 * 
	 * 
	 * if (row1[10] != null) { umoNameSub = row1[10].toString(); } else {
	 * umoNameSub=""; } jsonMap.put("umoNameSub",umoNameSub);
	 * 
	 * if (row1[11] != null) { resultForSub = row1[11].toString(); } else {
	 * resultForSub=""; } jsonMap.put("resultForSub",resultForSub);
	 * 
	 * if (row1[12] != null) { rangeSub = row1[12].toString(); } else { rangeSub="";
	 * }
	 * 
	 * 
	 * if(StringUtils.isEmpty(rangeSub)) { String range=""; Criterion
	 * cr1=Restrictions.eq("subInvestigationId",
	 * Long.parseLong(row1[0].toString()));
	 * List<DgNormalValue>listDgNormalValue=dgNormalValueDao.findByCriteria(cr1);
	 * 
	 * if(CollectionUtils.isNotEmpty(listDgNormalValue)) { for(DgNormalValue
	 * dgNormalValue :listDgNormalValue) { if(dgNormalValue!=null &&
	 * dgNormalValue.getSex().equalsIgnoreCase(genderCode)) {
	 * 
	 * if(dgNormalValue.getMinNormalValue()!=null)
	 * rangeSub=dgNormalValue.getMinNormalValue();
	 * if(dgNormalValue.getMaxNormalValue()!=null) rangeSub+= "-"+
	 * dgNormalValue.getMaxNormalValue();
	 * 
	 * } } }
	 * 
	 * }
	 * 
	 * jsonMap.put("mainChargeCodeIdSub", mainChargeCodeId);
	 * 
	 * 
	 * String mainChargeCodeName=""; if (mainChargeCodeId!= null) {
	 * 
	 * MasMainChargecode mmcc=null; mmcc =
	 * medicalExamDAO.getMainChargeCodeByMainChargeCodeId(mainChargeCodeId);
	 * mainChargeCodeName=mmcc.getMainChargecodeCode(); } else {
	 * mainChargeCodeName=""; } jsonMap.put("mainChargeCodeNameForSub",
	 * mainChargeCodeName);
	 * 
	 * jsonMap.put("rangeSub",rangeSub);
	 * jsonMap.put("subInvestigationCadeForSub",subInvestigationCadeForSub); if
	 * (row1[13] != null) { ridcIdSub = Long.parseLong(row1[13].toString()); } else
	 * { ridcIdSub=null; } if (row1[14] != null) { subInvestigationCadeForSub =
	 * row1[14].toString(); } else { subInvestigationCadeForSub=""; }
	 * jsonMap.put("ridcIdSub",ridcIdSub);
	 * 
	 * 
	 * if (row1[16] != null) { mainChargeCodeIdForSub = Long.parseLong(
	 * row1[16].toString()); } else { mainChargeCodeIdForSub=null; } if (row1[15] !=
	 * null) { subMainChargeCodeIdForSub = Long.parseLong( row1[15].toString()); }
	 * else { subMainChargeCodeIdForSub = null; }
	 * jsonMap.put("mainChargeCodeIdForSub",mainChargeCodeIdForSub);
	 * jsonMap.put("subMainChargeCodeIdForSub",subMainChargeCodeIdForSub);
	 * responseList.add(jsonMap);
	 * 
	 * //} if(subInvestigation==1) { break; } } //}
	 * 
	 * 
	 * }
	 * 
	 * if (responseList != null && responseList.size() > 0) {
	 * json.put("subInvestigationData", responseList); json.put("status", 1);
	 * 
	 * } else { json.put("subInvestigationData", responseList); json.put("msg",
	 * "Data not found"); json.put("status", 0); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return json.toString(); }
	 * 
	 * 
	 * 
	 * public static String getFinalInvestigationValue(String[]
	 * investigationIdValues,String[] dynamicUploadInvesIdAndfiles,String
	 * totalLengthDigiFile) { String finalValueOfInvest="";
	 * if(StringUtils.isNotEmpty(totalLengthDigiFile)&&
	 * !totalLengthDigiFile.equalsIgnoreCase("0")) { Integer
	 * totalLengthDigiFilev=Integer.parseInt(totalLengthDigiFile); for(int
	 * i=0;i<totalLengthDigiFilev;i++) { finalValueOfInvest+="0"+","; } }
	 * if(investigationIdValues!=null && investigationIdValues.length>0 &&
	 * !investigationIdValues[0].equalsIgnoreCase("")) { for (int i = 0; i <
	 * investigationIdValues.length; i++) { int countForFind=0;
	 * 
	 * if(dynamicUploadInvesIdAndfiles!=null &&
	 * dynamicUploadInvesIdAndfiles.length>0) {
	 * if(dynamicUploadInvesIdAndfiles.length==1 &&
	 * dynamicUploadInvesIdAndfiles[0].equalsIgnoreCase("") &&
	 * dynamicUploadInvesIdAndfiles[0].equalsIgnoreCase("0")) { break; } for(int j =
	 * 0; j < dynamicUploadInvesIdAndfiles.length; j++) { String []
	 * dynamicUploadInvesIdAndfilesa=dynamicUploadInvesIdAndfiles[j].split("##");
	 * if(dynamicUploadInvesIdAndfilesa!=null &&
	 * dynamicUploadInvesIdAndfilesa.length>=2 &&
	 * dynamicUploadInvesIdAndfilesa[0]!="" &&
	 * dynamicUploadInvesIdAndfilesa[0]!=null &&
	 * !dynamicUploadInvesIdAndfilesa[0].equalsIgnoreCase("") &&
	 * !dynamicUploadInvesIdAndfilesa[0].equalsIgnoreCase("0") &&
	 * dynamicUploadInvesIdAndfilesa[0].trim().equalsIgnoreCase(
	 * investigationIdValues[i].trim()) && dynamicUploadInvesIdAndfilesa[1]!=null &&
	 * dynamicUploadInvesIdAndfilesa[1]!="" &&
	 * !dynamicUploadInvesIdAndfilesa[1].trim().equalsIgnoreCase("0")) {
	 * if(!dynamicUploadInvesIdAndfilesa[1].contains(".")) {
	 * finalValueOfInvest+=dynamicUploadInvesIdAndfilesa[1]+","; countForFind++;
	 * break; } } } } if(countForFind==0) { finalValueOfInvest+="0"+","; } }
	 * 
	 * } return finalValueOfInvest; }
	 * 
	 * 
	 * @Override public String submitInvestigationUp(HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) {
	 * JSONObject json = new JSONObject(); JSONObject jsonObject = new
	 * JSONObject(payload); try { String
	 * radioInvAndImaValueandPre=payload.get("radioInvAndImaValueandPre").toString()
	 * ; radioInvAndImaValueandPre=OpdServiceImpl.getReplaceString(
	 * radioInvAndImaValueandPre);
	 * 
	 * String
	 * dynamicUploadInvesIdAndfile=payload.get("dynamicUploadInvesIdAndfileNew").
	 * toString(); dynamicUploadInvesIdAndfile=OpdServiceImpl.getReplaceString(
	 * dynamicUploadInvesIdAndfile);
	 * dynamicUploadInvesIdAndfile=dynamicUploadInvesIdAndfile.replaceAll("\"", "");
	 * String[] dynamicUploadInvesIdAndfiles =
	 * dynamicUploadInvesIdAndfile.split(",");
	 * 
	 * String investigationIdValue=payload.get("investigationIdValue").toString();
	 * investigationIdValue=OpdServiceImpl.getReplaceString(investigationIdValue);
	 * String[] investigationIdValues = investigationIdValue.split(",");
	 * 
	 * 
	 * String rmdIdVal=getFinalInvestigationValue(investigationIdValues,
	 * dynamicUploadInvesIdAndfiles,"0") ;
	 * 
	 * String[] resultInvss =null; if(payload.get("resultInvs")!=null) { String
	 * resultInvs=payload.get("resultInvs").toString();
	 * resultInvs=OpdServiceImpl.getReplaceString(resultInvs); //String[]
	 * resultInvss = resultInvs.split(","); resultInvss =resultInvs.split("@@@###");
	 * if(payload.containsKey("chargeCodeName")) { String
	 * chargeCodeName=payload.get("chargeCodeName").toString();
	 * chargeCodeName=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(chargeCodeName
	 * ); String[] chargeCodeNames = chargeCodeName.split(","); } } String[]
	 * dgOrderDtIdValues =null; if(payload.get("dgOrderDtIdValue")!=null) { String
	 * dgOrderDtIdValue=payload.get("dgOrderDtIdValue").toString();
	 * dgOrderDtIdValue=OpdServiceImpl.getReplaceString(dgOrderDtIdValue);
	 * dgOrderDtIdValues = dgOrderDtIdValue.split(","); } String[] dgOrderHdIds
	 * =null; if(payload.get("dgOrderHdId")!=null) { String
	 * dgOrderHdId=payload.get("dgOrderHdId").toString();
	 * dgOrderHdId=OpdServiceImpl.getReplaceString(dgOrderHdId); dgOrderHdIds =
	 * dgOrderHdId.split(","); } String[] dgResultDtIds =null;
	 * if(payload.get("dgResultDtId")!=null) { String
	 * dgResultDtId=payload.get("dgResultDtId").toString();
	 * dgResultDtId=OpdServiceImpl.getReplaceString(dgResultDtId); dgResultDtIds =
	 * dgResultDtId.split(","); } String[] dgResultHdIds =null;
	 * if(payload.get("dgResultHdId")!=null) { String
	 * dgResultHdId=payload.get("dgResultHdId").toString();
	 * dgResultHdId=OpdServiceImpl.getReplaceString(dgResultHdId);
	 * 
	 * dgResultHdIds = dgResultHdId.split(","); } String
	 * userId=payload.get("userId").toString();
	 * userId=OpdServiceImpl.getReplaceString(userId);
	 * 
	 * String hospitalId=payload.get("hospitalId").toString();
	 * hospitalId=OpdServiceImpl.getReplaceString(hospitalId);
	 * 
	 * String serviceNoIn=payload.get("serviceNoIn").toString();
	 * serviceNoIn=OpdServiceImpl.getReplaceString(serviceNoIn); String
	 * orderNumberInv=payload.get("orderNumberInv").toString();
	 * orderNumberInv=OpdServiceImpl.getReplaceString(orderNumberInv);
	 * 
	 * String patientIdObj=null; Long patientId=null;
	 * if(payload.containsKey("patientIdIn")) {
	 * 
	 * patientIdObj=payload.get("patientIdIn").toString();
	 * patientIdObj=OpdServiceImpl.getReplaceString(patientIdObj);
	 * 
	 * if(patientIdObj.equalsIgnoreCase("0")) { patientId =
	 * patientRegistrationDao.savePatientFromUploadDocument(jsonObject); } else {
	 * patientId=Long.parseLong(patientIdObj); } }
	 * 
	 * JSONObject jObject=new JSONObject(); jObject.put("departmentId", "1");
	 * jObject.put("priorityId", "3"); jObject.put("hospitalId", hospitalId);
	 * jObject.put("visitFlag", "R");
	 * 
	 * Long visitId =null; if(StringUtils.isNotEmpty(radioInvAndImaValueandPre) &&
	 * !radioInvAndImaValueandPre.equalsIgnoreCase("otherSup")) { if(
	 * StringUtils.isEmpty(orderNumberInv) || orderNumberInv.equalsIgnoreCase("0"))
	 * { visitId = patientRegistrationDao.createOpdEmergency(jObject,patientId); }
	 * if(StringUtils.isNotEmpty(orderNumberInv) &&
	 * !orderNumberInv.equalsIgnoreCase("0")) { Criterion
	 * cr1=Restrictions.eq("orderNo", orderNumberInv);
	 * List<DgOrderhd>listDgOrderhd=dgOrderhdDao.findByCriteria(cr1);
	 * if(CollectionUtils.isNotEmpty(listDgOrderhd)) { DgOrderhd
	 * DgOrderhd=listDgOrderhd.get(0); visitId=DgOrderhd.getVisitId(); } } }
	 * 
	 * if(StringUtils.isNotEmpty(radioInvAndImaValueandPre) &&
	 * radioInvAndImaValueandPre.equalsIgnoreCase("otherSup")) {
	 * payload.put("visitId", visitId); payload.put("patientId", patientId);
	 * 
	 * Long patientDocumentId
	 * =medicalExamDAO.saveUpdateOfPatientDocumentDetailDoc(payload); String
	 * satusOfMessageDoc=""; if(patientDocumentId!=0) { satusOfMessageDoc = 1 + "##"
	 * + "Data uploaded successfully "; json.put("message", satusOfMessageDoc);
	 * return json.toString(); }else { satusOfMessageDoc = 0 + "##" +
	 * "Data is not updated because something is wrong" +""; json.put("message",
	 * satusOfMessageDoc); return json.toString(); } }
	 * 
	 * 
	 * String[] rangeValues=null; if(payload.containsKey("range")) { String
	 * rangeValue=payload.get("range").toString();
	 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
	 * rangeValue.split(","); }
	 * 
	 * String[] rmsIdFinalValfileUp=null; if(payload.containsKey("rmsIdFinalVal")) {
	 * String rmsIdValfileUp= payload.get("rmsIdFinalVal").toString();
	 * 
	 * rmsIdValfileUp=OpdServiceImpl.getReplaceString(rmsIdValfileUp);
	 * rmsIdFinalValfileUp = rmsIdValfileUp.split(","); } rmsIdFinalValfileUp =
	 * rmdIdVal.split(",");
	 * 
	 * 
	 * 
	 * String messageVal=
	 * patientRegistrationDao.saveOrUpdateUploadData(investigationIdValues,
	 * dgOrderDtIdValues,dgOrderHdIds,dgResultDtIds,dgResultHdIds,
	 * resultInvss,patientId,hospitalId,userId,orderNumberInv,rangeValues,
	 * rmsIdFinalValfileUp,payload,visitId); json.put("message", messageVal); }
	 * catch(Exception e) { e.printStackTrace(); json.put("message", "0"+"##"+e); }
	 * return json.toString(); }
	 * 
	 * public Visit getVisitBean(String patientId,String hospitalId) { Visit
	 * visit=new Visit(); if(StringUtils.isNotEmpty(patientId)) {
	 * patientId=OpdServiceImpl.getReplaceString(patientId);
	 * visit.setPatientId(Long.parseLong(patientId)); }
	 * if(StringUtils.isNotEmpty(hospitalId)) {
	 * hospitalId=OpdServiceImpl.getReplaceString(hospitalId);
	 * visit.setHospitalId(Long.parseLong(hospitalId));
	 * 
	 * } visit.setVisitFlag("R");
	 * List<MasAppointmentType>listMasAppointmentType=patientRegistrationDao.
	 * findMasAppointmentTypeByAppCode();
	 * visit.setAppointmentTypeId(listMasAppointmentType.get(0).getAppointmentTypeId
	 * ()); visit.setTokenNo(1l); Date date=new Date(); visit.setVisitDate(new
	 * Timestamp(date.getTime())); visit.setLastChgDate(new
	 * Timestamp(date.getTime())); visit.setPriority(3l); visit.setVisitStatus("c");
	 * return visit; }
	 * 
	 * @Override public String savePatientFromUploadDocument(String requestData) {
	 * long patientId=0; JSONObject json = new JSONObject(); JSONObject jObject =
	 * new JSONObject(requestData);
	 * 
	 * patientId = patientRegistrationDao.savePatientFromUploadDocument(jObject); if
	 * (patientId!=0) { json.put("status", "1"); json.put("patientId", patientId); }
	 * else { json.put("status", "0"); json.put("patientId", patientId); } return
	 * json.toString();
	 * 
	 * }
	 * 
	 * 
	 * @Override public String opdEmergencySavePatient(String requestData) {
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); JSONObject json =
	 * new JSONObject(); JSONObject jObject = new JSONObject(requestData); map =
	 * patientRegistrationDao.opdEmergencySavePatient(jObject); json.put("data",
	 * map); return json.toString();
	 * 
	 * }
	 * 
	 * 
	 * @Override public Map<String, Object> getStateFromDistrict(Map<String, Object>
	 * requestData) {
	 * 
	 * Map<String,Object> map = new HashMap<String, Object>(); List<HashMap<String,
	 * Object>> stateList = new ArrayList<HashMap<String, Object>>(); long
	 * districtId= Long.parseLong(requestData.get("districtId").toString());
	 * 
	 * List<MasState>masStateList
	 * =patientRegistrationDao.getStateListFromDistrict(districtId);
	 * if(!masStateList.isEmpty() && masStateList.size()>0) { for(MasState
	 * state:masStateList) { HashMap<String,Object> stateMap=new HashMap<String,
	 * Object>(); stateMap.put("stateId", state.getStateId());
	 * stateMap.put("stateName", state.getStateName()); stateList.add(stateMap); }
	 * map.put("status","1"); map.put("stateList",stateList);
	 * 
	 * }else { map.put("status","0"); map.put("stateList",stateList); } return map;
	 * 
	 * }
	 * 
	 * 
	 * @Override public Map<String, Object> getRegionFromStation(Map<String, Object>
	 * requestData) {
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); List<HashMap<String,
	 * Object>> commandFromStation = new ArrayList<HashMap<String, Object>>();
	 * System.out.println(requestData.get("unitId")); long unitId =
	 * Long.parseLong(requestData.get("unitId").toString());
	 * System.out.println("uniitId "+ unitId); List<MasCommand> commandList =
	 * patientRegistrationDao.getRegionFromStation(unitId); if
	 * (!commandList.isEmpty() && commandList.size() > 0) { for (MasCommand command
	 * : commandList) { HashMap<String, Object> regionMap = new HashMap<String,
	 * Object>(); regionMap.put("commandId", command.getCommandId());
	 * regionMap.put("commandName", command.getCommandName());
	 * commandFromStation.add(regionMap); } map.put("status", "1");
	 * map.put("commandFromStation", commandFromStation);
	 * 
	 * } else { map.put("status", "0"); map.put("commandFromStation",
	 * commandFromStation); } return map;
	 * 
	 * }
	 * 
	 * 
	 * @Override public Map<String, Object> getDistrictFromState(Map<String, Object>
	 * requestData) {
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); List<HashMap<String,
	 * Object>> districtList = new ArrayList<HashMap<String, Object>>(); long
	 * stateId = Long.parseLong(requestData.get("stateId").toString());
	 * 
	 * List<MasDistrict> masDistrictList =
	 * patientRegistrationDao.getDistrictFromState(stateId); if
	 * (!masDistrictList.isEmpty() && masDistrictList.size() > 0) { for (MasDistrict
	 * district : masDistrictList) { HashMap<String, Object> districtMap = new
	 * HashMap<String, Object>(); districtMap.put("districtId",
	 * district.getDistrictId()); districtMap.put("districtName",
	 * district.getDistrictName()); districtList.add(districtMap); }
	 * map.put("status", "1"); map.put("districtList", districtList);
	 * 
	 * } else { map.put("status", "0"); map.put("districtList", districtList); }
	 * return map;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public Map<Integer, Map<String, Object>> getNewPatientDetal(MasEmployee
	 * ms,String relation ,long relationId,String empName,String serviceNo,long
	 * employeeId,long empRankId, String empRank,long empTradeId,String
	 * empTradeName,String empTotalService,String empServiceJoinDate, String
	 * empAge,String age,String dateOfBirth,long empUnitId,String empUnitName,long
	 * empCommandId, String empCommandName,long empRecordOfficeId,String
	 * empRecordOfficeName,long empMaritalStatusId,String empMaritalStatusName, long
	 * empCategoryId,String empDepartmentName,String name,long genderId, String
	 * gender,String mobileNumber,String patientEmailId,String patientBloodGroup,
	 * long patientBloodGroupId,int rowCount,Map<Integer, Map<String, Object>>
	 * data){ relation = HMSUtil.getProperties("adt.properties", "SELF_RELATION");
	 * // String selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); // relationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * Map<String, Object> responseEmpMap = new HashMap<String, Object>();
	 * 
	 * String patientName = ""; if (ms.getEmployeeName() != null) { patientName =
	 * ms.getEmployeeName(); }
	 * 
	 * empName= patientName; serviceNo = ms.getServiceNo(); employeeId =
	 * ms.getEmployeeId();
	 * 
	 * String empRankCode = ms.getMasRank()!=null? ms.getMasRank():"0";
	 * List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0) { empRankId =
	 * mRankList.get(0).getRankId(); empRank = mRankList.get(0).getRankName();
	 * empTradeId =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeId():0; empTradeName =
	 * mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().
	 * getTradeName():"";
	 * 
	 * }else { empRankId = 0; empRank = ""; empTradeId = 0; empTradeName = ""; }
	 * 
	 * if(ms.getDoe()!=null) { empTotalService= HMSUtil.calculateAge(ms.getDoe());
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(ms.getDoe()); }else {
	 * empTotalService= "0"; empServiceJoinDate=""; }
	 * 
	 * if(ms.getDob()!=null) { empAge=HMSUtil.calculateAge(ms.getDob()); age =
	 * HMSUtil.calculateAge(ms.getDob()); dateOfBirth
	 * =HMSUtil.changeDateToddMMyyyy(ms.getDob()); }else { empAge="0"; age="0";
	 * dateOfBirth=""; }
	 * 
	 * 
	 * String empUnitCode = ms.getMasUnit()!=null?ms.getMasUnit():"0";
	 * 
	 * List<MasUnit> unitList = patientRegistrationDao.getEmpUnitId(empUnitCode);
	 * if(!unitList.isEmpty() && unitList.size()>0) {
	 * 
	 * empUnitId = unitList.get(0).getUnitId();
	 * empUnitName=unitList.get(0).getUnitName();
	 * 
	 * if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null &&
	 * unitList.get(0).getMasStation().getMasCommand()!=null) {
	 * empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId();
	 * // This is station Id
	 * empCommandName=unitList.get(0).getMasStation().getMasCommand().getCommandName
	 * (); // This is station Name }else { empCommandId=0; empCommandName=""; }
	 * 
	 * }else { empUnitId = 0; empUnitName=""; }
	 * 
	 * if(ms.getMasRecordOfficeAddress()!=null) {
	 * empRecordOfficeId=ms.getMasRecordOfficeAddress().getRecordOfficeAddressId();
	 * empRecordOfficeName=ms.getMasRecordOfficeAddress().getRecordOfficeAddressName
	 * (); }else { empRecordOfficeId=0; empRecordOfficeName=""; }
	 * 
	 * if(ms.getMasMaritalStatus()!=null) {
	 * empMaritalStatusId=ms.getMasMaritalStatus().getMaritalStatusId();
	 * empMaritalStatusName=ms.getMasMaritalStatus().getMaritalStatusName(); }else {
	 * empMaritalStatusId=0; empMaritalStatusName=""; }
	 * 
	 * empCategoryId=ms.getMasEmployeeCategory()!=null?ms.getMasEmployeeCategory().
	 * getEmployeeCategoryId():0;
	 * 
	 * Comment due to data from view
	 * empReligionId=ms.getMasReligion().getReligionId();
	 * empReligion=ms.getMasReligion().getReligionName();
	 * 
	 * 
	 * 
	 * 
	 * if(mRankList.size()>0) { empDepartmentName=
	 * mRankList.get(0).getMasTrade()!=null?
	 * mRankList.get(0).getMasTrade().getTradeName():""; }else {
	 * empDepartmentName=""; }
	 * 
	 * name = patientName;
	 * 
	 * if( ms.getMasAdministrativeSex()!=null) { genderId =
	 * ms.getMasAdministrativeSex().getAdministrativeSexId(); gender =
	 * ms.getMasAdministrativeSex().getAdministrativeSexName(); }else { genderId =
	 * 0; gender = ""; }
	 * 
	 * 
	 * 
	 * mobileNumber=ms.getMobileNo()!=null?ms.getMobileNo():"";
	 * 
	 * if(!ms.getEmployeeAddress().isEmpty() && ms.getEmployeeAddress()!=null &&
	 * ms.getEmployeeAddress().get(0)!=null) { addressL1 =
	 * ms.getEmployeeAddress().get(0).getAddressLine1()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine1():""; addressL2 =
	 * ms.getEmployeeAddress().get(0).getAddressLine2()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine2():""; addressL3 =
	 * ms.getEmployeeAddress().get(0).getAddressLine3()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine3():""; addressL4 =
	 * ms.getEmployeeAddress().get(0).getAddressLine4()!=null?ms.getEmployeeAddress(
	 * ).get(0).getAddressLine4():""; districtId =
	 * ms.getEmployeeAddress().get(0).getMasDistrict()!=null?ms.getEmployeeAddress()
	 * .get(0).getMasDistrict().getDistrictId():0; districtName =
	 * ms.getEmployeeAddress().get(0).getMasDistrict()!=null?ms.getEmployeeAddress()
	 * .get(0).getMasDistrict().getDistrictName():""; stateId =
	 * ms.getEmployeeAddress().get(0).getMasState()!=null?ms.getEmployeeAddress().
	 * get(0).getMasState().getStateId():0; stateName =
	 * ms.getEmployeeAddress().get(0).getMasState()!=null?ms.getEmployeeAddress().
	 * get(0).getMasState().getStateName():""; patientPincode =
	 * ms.getEmployeeAddress().get(0).getPincode()!=null?Long.parseLong(ms.
	 * getEmployeeAddress().get(0).getPincode()):0; }else { addressL1 = "";
	 * addressL2 = ""; addressL3 = ""; addressL4 = ""; patientPincode = 0L; }
	 * 
	 * 
	 * patientEmailId=ms.getEmail()!=null?ms.getEmail():"";
	 * 
	 * patientBloodGroup = ""; patientBloodGroupId = 0;
	 * 
	 * responseEmpMap.put("Id", ms.getEmployeeId()); responseEmpMap.put("uhidNo",
	 * ""); responseEmpMap.put("Id", ++rowCount); responseEmpMap.put("employeeId",
	 * employeeId); responseEmpMap.put("name", name); responseEmpMap.put("empAge",
	 * empAge); responseEmpMap.put("age", age); responseEmpMap.put("gender",
	 * gender); responseEmpMap.put("genderId", genderId);
	 * responseEmpMap.put("dateOfBirth", dateOfBirth);
	 * responseEmpMap.put("relation", relation); responseEmpMap.put("relationId",
	 * relationId); responseEmpMap.put("mobileNumber",mobileNumber);
	 * 
	 * responseEmpMap.put("patientAddressL1",addressL1);
	 * responseEmpMap.put("patientAddressL2",addressL2);
	 * responseEmpMap.put("patientAddressL3",addressL3);
	 * responseEmpMap.put("patientAddressL4",addressL4);
	 * responseEmpMap.put("patientDistrictId",districtId);
	 * responseEmpMap.put("patientDistrictName",districtName);
	 * responseEmpMap.put("patientStateId",stateId);
	 * responseEmpMap.put("patientStateName",stateName);
	 * 
	 * 
	 * responseEmpMap.put("patientPincode",patientPincode);
	 * responseEmpMap.put("patientEmailId",patientEmailId);
	 * responseEmpMap.put("patientBloodGroup",patientBloodGroup);
	 * responseEmpMap.put("patientBloodGroupId",patientBloodGroupId);
	 * 
	 * 
	 * responseEmpMap.put("serviceNo",serviceNo);
	 * responseEmpMap.put("empRankId",empRankId);
	 * responseEmpMap.put("empRank",empRank);
	 * responseEmpMap.put("empTradeId",empTradeId);
	 * responseEmpMap.put("empTradeName",empTradeName);
	 * responseEmpMap.put("empName",empName);
	 * responseEmpMap.put("empTotalService",empTotalService);
	 * responseEmpMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responseEmpMap.put("empUnitId",empUnitId);
	 * responseEmpMap.put("empUnitName",empUnitName);
	 * responseEmpMap.put("empCommandId",empCommandId);
	 * responseEmpMap.put("empCommandName",empCommandName);
	 * responseEmpMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responseEmpMap.put("empRecordOfficeName",empRecordOfficeName);
	 * responseEmpMap.put("empDepartmentName",empDepartmentName);
	 * responseEmpMap.put("empCategoryId",empCategoryId);
	 * 
	 * responseEmpMap.put("empMaritalStatusId",empMaritalStatusId);
	 * responseEmpMap.put("empMaritalStatusName",empMaritalStatusName);
	 * //responseEmpMap.put("empReligionId",empReligionId);
	 * //responseEmpMap.put("empReligion",empReligion);
	 * 
	 * 
	 * 
	 * data.put(++rowCount, responseEmpMap); return data; }
	 * 
	 * 
	 * public Map<Integer, Map<String, Object>>
	 * getExistPatientDetailByPatient(Patient patient,String relation ,long
	 * relationId,String empName,String serviceNo,long employeeId,long empRankId,
	 * String empRank,long empTradeId,String empTradeName,String
	 * empTotalService,String empServiceJoinDate, String empAge,String age,String
	 * dateOfBirth,long empUnitId,String empUnitName,long empCommandId, String
	 * empCommandName,long empRecordOfficeId,String empRecordOfficeName,long
	 * empMaritalStatusId,String empMaritalStatusName, long empCategoryId,String
	 * empDepartmentName,String name,long genderId, String gender,String
	 * mobileNumber,String patientEmailId,String patientBloodGroup, long
	 * patientBloodGroupId,int rowCount,Map<Integer, Map<String, Object>> data){
	 * Map<String, Object> responsePatientMap = new HashMap<String, Object>();
	 * String patientName = ""; if (patient.getPatientName() != null) { patientName
	 * = patient.getPatientName(); }
	 * 
	 * // Employee related details serviceNo= patient.getServiceNo();
	 * 
	 * if(patient.getMasRank()!=null) { empRankId= patient.getMasRank().getRankId();
	 * empRank = patient.getMasRank().getRankName(); }else { empRankId= 0; empRank =
	 * ""; }
	 * 
	 * if(patient.getMasTrade()!=null) { empTradeId=
	 * patient.getMasTrade().getTradeId(); empTradeName=
	 * patient.getMasTrade().getTradeName(); }else { empTradeId= 0; empTradeName=
	 * ""; }
	 * 
	 * empName= patient.getEmployeeName();
	 * 
	 * if(patient.getServiceJoinDate()!=null) { empTotalService=
	 * HMSUtil.calculateAge(patient.getServiceJoinDate());
	 * empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(patient.getServiceJoinDate())
	 * ; }else { empTotalService= "0"; empServiceJoinDate=""; }
	 * 
	 * if(patient.getMasUnit()!=null) { empUnitId=patient.getMasUnit().getUnitId();
	 * empUnitName=patient.getMasUnit().getUnitName(); }else { empUnitId=0;
	 * empUnitName=""; }
	 * 
	 * if(patient.getMasCommand()!=null) {
	 * empCommandId=patient.getMasCommand().getCommandId();
	 * empCommandName=patient.getMasCommand().getCommandName(); }else {
	 * empCommandId=0; empCommandName=""; }
	 * 
	 * if(patient.getMasRecordOfficeAddress()!=null) {
	 * empRecordOfficeId=patient.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressId();
	 * empRecordOfficeName=patient.getMasRecordOfficeAddress().
	 * getRecordOfficeAddressName(); }else { empRecordOfficeId=0;
	 * empRecordOfficeName=""; }
	 * 
	 * if(patient.getMasMaritalStatus()!=null) {
	 * empMaritalStatusId=patient.getMasMaritalStatus().getMaritalStatusId();
	 * empMaritalStatusName=patient.getMasMaritalStatus().getMaritalStatusName();
	 * }else { empMaritalStatusId=0; empMaritalStatusName=""; }
	 * 
	 * if(patient.getMasReligion()!=null) {
	 * empReligionId=patient.getMasReligion().getReligionId();
	 * empReligion=patient.getMasReligion().getReligionName(); }else {
	 * empReligionId=0; empReligion=""; }
	 * 
	 * 
	 * 
	 * 
	 * empCategoryId =
	 * patient.getMasEmployeeCategory()!=null?patient.getMasEmployeeCategory().
	 * getEmployeeCategoryId():0;
	 * 
	 * 
	 * String relationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE"); MasEmployee employeeObject =
	 * patientRegistrationDao.getMasEmployeeFromServiceNo(serviceNo);
	 * if(employeeObject!=null) {
	 * 
	 * employeeId =employeeObject.getEmployeeId();
	 * 
	 * if(relationCode.equalsIgnoreCase(patient.getMasRelation().getRelationCode()))
	 * { if(patient.getDateOfBirth()!=null) {
	 * empAge=HMSUtil.calculateAge(patient.getDateOfBirth()); }else { empAge="0"; }
	 * }else { if(employeeObject.getDob()!=null) {
	 * empAge=HMSUtil.calculateAge(employeeObject.getDob()); }else { empAge="0"; }
	 * 
	 * }
	 * 
	 * String empRankCode = employeeObject.getMasRank()!=null?
	 * employeeObject.getMasRank():"0"; List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(mRankList.size()>0) {
	 * empDepartmentName=mRankList.get(0).getMasTrade()!=null?mRankList.get(0).
	 * getMasTrade().getTradeName():""; }else { empDepartmentName=""; }
	 * 
	 * }
	 * 
	 * // patient related details name = patientName;
	 * if(patient.getDateOfBirth()!=null) { age =
	 * HMSUtil.calculateAge(patient.getDateOfBirth()); dateOfBirth =
	 * HMSUtil.changeDateToddMMyyyy(patient.getDateOfBirth()); }else { age="0";
	 * dateOfBirth=""; }
	 * 
	 * if(patient.getMasAdministrativeSex()!=null) { gender =
	 * patient.getMasAdministrativeSex().getAdministrativeSexName(); genderId =
	 * patient.getMasAdministrativeSex().getAdministrativeSexId(); }else { gender =
	 * ""; genderId = 0; }
	 * 
	 * if(patient.getMasRelation()!=null) { relation =
	 * patient.getMasRelation().getRelationName();
	 * relationId=patient.getMasRelation().getRelationId(); }else { relation = "";
	 * relationId= 0; }
	 * 
	 * 
	 * mobileNumber=patient.getMobileNumber();
	 * 
	 * 
	 * addressL1 = patient.getAddressLine1()!=null?patient.getAddressLine1():"";
	 * addressL2 = patient.getAddressLine2()!=null?patient.getAddressLine2():"";
	 * addressL3 = patient.getAddressLine3()!=null?patient.getAddressLine3():"";
	 * addressL4 = patient.getAddressLine4()!=null?patient.getAddressLine4():"";
	 * districtId =
	 * patient.getMasDistrict()!=null?patient.getMasDistrict().getDistrictId():0;
	 * districtName =
	 * patient.getMasDistrict()!=null?patient.getMasDistrict().getDistrictName():"";
	 * 
	 * if(patient.getMasState()!=null) { stateId =
	 * patient.getMasState().getStateId(); stateName =
	 * patient.getMasState().getStateName();
	 * 
	 * }else { stateId = 0; stateName = "";
	 * 
	 * }
	 * 
	 * 
	 * patientPincode = patient.getPincode()!=null?patient.getPincode():0;
	 * patientEmailId=patient.getEmailId()!=null?patient.getEmailId():"";
	 * 
	 * if(patient.getMasBloodGroup()!=null) { patientBloodGroup =
	 * patient.getMasBloodGroup().getBloodGroupName(); patientBloodGroupId =
	 * patient.getMasBloodGroup().getBloodGroupId(); }else { patientBloodGroup = "";
	 * patientBloodGroupId = 0; }
	 * 
	 * 
	 * // Medical category and Date ME only for employee patient
	 * if(patient.getMasMedicalCategory()!=null) {
	 * empMedicalCategory=patient.getMasMedicalCategory().getMedicalCategoryName();
	 * empMedicalCategoryId=patient.getMasMedicalCategory().getMedicalCategoryId();
	 * }else { empMedicalCategory=""; empMedicalCategoryId=0; }
	 * 
	 * //dateME=(patient.getDateMe()!=null?HMSUtil.changeDateToddMMyyyy(patient.
	 * getDateMe()):"");
	 * 
	 * // NOK1 related details
	 * 
	 * //nok1Name = patient.getNok1Name()!=null?patient.getNok1Name():"";
	 * 
	 * if(patient.getMasRelationNok1()!=null) { nok1RelationId =
	 * patient.getMasRelationNok1().getRelationId();
	 * nok1Relation=patient.getMasRelationNok1().getRelationName(); }else {
	 * nok1RelationId = 0; nok1Relation=""; }
	 * 
	 * nok1ContactNo=patient.getNok1ContactNo()!=null?patient.getNok1ContactNo():"";
	 * 
	 * 
	 * nok1AddressL1 =
	 * patient.getNok1AddressLine1()!=null?patient.getNok1AddressLine1():"";
	 * nok1AddressL2 =
	 * patient.getNok1AddressLine2()!=null?patient.getNok1AddressLine2():"";
	 * nok1AddressL3 =
	 * patient.getNok1AddressLine3()!=null?patient.getNok1AddressLine3():"";
	 * nok1AddressL4 =
	 * patient.getNok1AddressLine4()!=null?patient.getNok1AddressLine4():"";
	 * 
	 * if(patient.getNok1Distrcit()!=null) { nok1DistrictId =
	 * patient.getNok1Distrcit().getDistrictId(); nok1DistrictName =
	 * patient.getNok1Distrcit().getDistrictName(); }else { nok1DistrictId = 0;
	 * nok1DistrictName = ""; }
	 * 
	 * if(patient.getNok1State()!=null) { nok1StateId =
	 * patient.getNok1State().getStateId(); nok1StateName =
	 * patient.getNok1State().getStateName(); }else { nok1StateId = 0; nok1StateName
	 * = ""; }
	 * 
	 * 
	 * nok1PoliceStation=patient.getNok1PoliceStation()!=null?patient.
	 * getNok1PoliceStation():"";
	 * nok1Pincode=patient.getNok1PinCode()!=null?patient.getNok1PinCode():0;
	 * nok1MobileNo=patient.getNok1MobileNo(); nok1EamilId=patient.getNok1EmailId();
	 * 
	 * 
	 * // NOK2 related details
	 * 
	 * // nok2Name=patient.getNok2Name()!=null?patient.getNok2Name():"";
	 * 
	 * if(patient.getMasRelationNok2()!=null) {
	 * nok2RelationId=patient.getMasRelationNok2().getRelationId();
	 * nok2Relation=patient.getMasRelationNok2().getRelationName(); }else {
	 * nok2RelationId=0; nok2Relation=""; }
	 * 
	 * 
	 * nok2ContactNo=(patient.getNok2ContactNo()!=null?patient.getNok2ContactNo():""
	 * );
	 * 
	 * 
	 * nok2AddressL1 =
	 * patient.getNok2AddressLine1()!=null?patient.getNok2AddressLine1():"";
	 * nok2AddressL2 =
	 * patient.getNok2AddressLine2()!=null?patient.getNok2AddressLine2():"";
	 * nok2AddressL3 =
	 * patient.getNok2AddressLine3()!=null?patient.getNok2AddressLine3():"";
	 * nok2AddressL4 =
	 * patient.getNok2AddressLine4()!=null?patient.getNok2AddressLine4():"";
	 * 
	 * if(patient.getNok2Distrcit()!=null) { nok2DistrictId =
	 * patient.getNok2Distrcit().getDistrictId(); nok2DistrictName =
	 * patient.getNok2Distrcit().getDistrictName(); }else { nok2DistrictId = 0;
	 * nok2DistrictName = ""; }
	 * 
	 * if(patient.getNok2State()!=null) { nok2StateId =
	 * patient.getNok2State().getStateId(); nok2StateName
	 * =patient.getNok2State().getStateName(); }else { nok2StateId = 0;
	 * nok2StateName = ""; }
	 * 
	 * 
	 * nok2PoliceStation=patient.getNok2PoliceStation()!=null?patient.
	 * getNok2PoliceStation():"";
	 * nok2Pincode=patient.getNok2PinCode()!=null?patient.getNok2PinCode():0;
	 * 
	 * 
	 * 
	 * nok2MobileNo=patient.getNok2MobileNo()!=null?patient.getNok2MobileNo():"";
	 * nok2EamilId=patient.getNok2EmailId()!=null?patient.getNok2EmailId():"";
	 * 
	 * 
	 * responsePatientMap.put("patientId", patient.getPatientId());
	 * responsePatientMap.put("Id", ++rowCount);
	 * responsePatientMap.put("employeeId",employeeId);
	 * responsePatientMap.put("uhidNo", patient.getUhidNo());
	 * responsePatientMap.put("name", name); responsePatientMap.put("age", age);
	 * responsePatientMap.put("gender", gender); responsePatientMap.put("genderId",
	 * genderId); responsePatientMap.put("dateOfBirth", dateOfBirth);
	 * responsePatientMap.put("relation", relation);
	 * responsePatientMap.put("relationId", relationId);
	 * responsePatientMap.put("mobileNumber",mobileNumber);
	 * 
	 * responsePatientMap.put("patientAddressL1",addressL1);
	 * responsePatientMap.put("patientAddressL2",addressL2);
	 * responsePatientMap.put("patientAddressL3",addressL3);
	 * responsePatientMap.put("patientAddressL4",addressL4);
	 * responsePatientMap.put("patientDistrictId",districtId);
	 * responsePatientMap.put("patientDistrictName",districtName);
	 * 
	 * responsePatientMap.put("patientStateId",stateId);
	 * responsePatientMap.put("patientStateName",stateName);
	 * responsePatientMap.put("patientPincode",patientPincode);
	 * 
	 * responsePatientMap.put("patientEmailId",patientEmailId);
	 * responsePatientMap.put("patientBloodGroup",patientBloodGroup);
	 * responsePatientMap.put("patientBloodGroupId",patientBloodGroupId);
	 * 
	 * responsePatientMap.put("empMedicalCategory",empMedicalCategory);
	 * responsePatientMap.put("empMedicalCategoryId",empMedicalCategoryId);
	 * responsePatientMap.put("dateME",dateME);
	 * 
	 * responsePatientMap.put("serviceNo",serviceNo);
	 * responsePatientMap.put("empRankId",empRankId);
	 * responsePatientMap.put("empAge",empAge);
	 * responsePatientMap.put("empRank",empRank);
	 * responsePatientMap.put("empTradeId",empTradeId);
	 * responsePatientMap.put("empTradeName",empTradeName);
	 * responsePatientMap.put("empName",empName);
	 * responsePatientMap.put("empTotalService",empTotalService);
	 * responsePatientMap.put("empServiceJoinDate",empServiceJoinDate);
	 * responsePatientMap.put("empUnitId",empUnitId);
	 * responsePatientMap.put("empUnitName",empUnitName);
	 * responsePatientMap.put("empCommandId",empCommandId);
	 * responsePatientMap.put("empCommandName",empCommandName);
	 * responsePatientMap.put("empRecordOfficeId",empRecordOfficeId);
	 * responsePatientMap.put("empRecordOfficeName",empRecordOfficeName);
	 * responsePatientMap.put("empDepartmentName",empDepartmentName);
	 * responsePatientMap.put("empCategoryId",empCategoryId);
	 * 
	 * responsePatientMap.put("empMaritalStatusId",empMaritalStatusId);
	 * responsePatientMap.put("empMaritalStatusName",empMaritalStatusName);
	 * 
	 * responsePatientMap.put("empReligionId",empReligionId);
	 * responsePatientMap.put("empReligion",empReligion);
	 * 
	 * responsePatientMap.put("nok1Name",nok1Name);
	 * responsePatientMap.put("nok1RelationId",nok1RelationId);
	 * responsePatientMap.put("nok1Relation",nok1Relation);
	 * responsePatientMap.put("nok1ContactNo",nok1ContactNo);
	 * 
	 * responsePatientMap.put("nok1AddressL1",nok1AddressL1);
	 * responsePatientMap.put("nok1AddressL2",nok1AddressL2);
	 * responsePatientMap.put("nok1AddressL3",nok1AddressL3);
	 * responsePatientMap.put("nok1AddressL4",nok1AddressL4);
	 * 
	 * responsePatientMap.put("nok1DistrictId",nok1DistrictId);
	 * responsePatientMap.put("nok1DistrictName",nok1DistrictName);
	 * responsePatientMap.put("nok1StateId",nok1StateId);
	 * responsePatientMap.put("nok1StateName",nok1StateName);
	 * 
	 * responsePatientMap.put("nok1PoliceStation",nok1PoliceStation);
	 * responsePatientMap.put("nok1Pincode",nok1Pincode);
	 * responsePatientMap.put("nok1MobileNo",nok1MobileNo);
	 * responsePatientMap.put("nok1EamilId",nok1EamilId);
	 * 
	 * responsePatientMap.put("nok2Name",nok2Name);
	 * responsePatientMap.put("nok2RelationId",nok2RelationId);
	 * responsePatientMap.put("nok2Relation",nok2Relation);
	 * responsePatientMap.put("nok2ContactNo",nok2ContactNo);
	 * 
	 * responsePatientMap.put("nok2AddressL1",nok2AddressL1);
	 * responsePatientMap.put("nok2AddressL2",nok2AddressL2);
	 * responsePatientMap.put("nok2AddressL3",nok2AddressL3);
	 * responsePatientMap.put("nok2AddressL4",nok2AddressL4);
	 * 
	 * 
	 * responsePatientMap.put("nok2DistrictId",nok2DistrictId);
	 * responsePatientMap.put("nok2DistrictName",nok2DistrictName);
	 * responsePatientMap.put("nok2StateId",nok2StateId);
	 * responsePatientMap.put("nok2StateName",nok2StateName);
	 * 
	 * responsePatientMap.put("nok2PoliceStation",nok2PoliceStation);
	 * responsePatientMap.put("nok2Pincode",nok2Pincode);
	 * responsePatientMap.put("nok2MobileNo",nok2MobileNo);
	 * responsePatientMap.put("nok2EamilId",nok2EamilId);
	 * 
	 * 
	 * data.put(++rowCount, responsePatientMap); return data; }
	 */

	@Override
	public Map<String, Object> getDistrictList(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> districtList = new ArrayList<Map<String, Object>>();
		try {
			if (HMSUtil.checkIfNull(requestData.get("stateId")) || requestData.get("stateId").equals("")) {
				map.put("msg", "State Id cannot be null");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}

			List<MasDistrict> list = (List<MasDistrict>) patientRegistrationDao.getDistrictList(requestData);
			if (!list.isEmpty()) {
				for (MasDistrict district : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("districtId", district.getDistrictId());
					map2.put("districtCode", district.getDistrictCode());
					map2.put("districtName", district.getDistrictName());
					districtList.add(map2);
				}
				response.put("status", true);
				response.put("list", districtList);
			} else {
				response.put("status", true);
				response.put("list", districtList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", districtList);
		return response;
	}

	@Override
	public Map<String, Object> getCityList(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> cityList = new ArrayList<Map<String, Object>>();
		try {
			List<MasCity> list = patientRegistrationDao.getCityList(requestData);
			if (!list.isEmpty()) {
				for (MasCity city : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("cityId", city.getCityId());
					map2.put("cityCode", city.getCityCode());
					map2.put("cityName", city.getCityName());
					cityList.add(map2);
				}
				response.put("status", true);
				response.put("list", cityList);
			} else {
				response.put("status", true);
				response.put("list", cityList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.put("status", false);
		response.put("list", cityList);
		return response;
	}

	@Override
	public Map<String, Object> getMMUList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> mmuList = new ArrayList<Map<String, Object>>();
		try {

			List<MasMMU> list = patientRegistrationDao.getMMUList(requestData);
			if (!list.isEmpty()) {
				for (MasMMU mmu : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("mmuId", mmu.getMmuId());
					map2.put("mmuCode", mmu.getMmuCode());
					map2.put("mmuName", mmu.getMmuName());
					mmuList.add(map2);
				}
				response.put("status", true);
				response.put("list", mmuList);
			} else {
				response.put("status", true);
				response.put("list", mmuList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", mmuList);
		return response;
	}

	@Override
	public Map<String, Object> createCampPlan(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = patientRegistrationDao.createCampPlan(requestData);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}

	@Override
	public Map<String, Object> getWardList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> wardList = new ArrayList<Map<String, Object>>();
		try {
			if (HMSUtil.checkIfNull(requestData.get("cityId")) || requestData.get("cityId").equals("")) {
				map.put("msg", "City Id cannot be null");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}
			List<MasWard> list = patientRegistrationDao.getWardList(requestData);
			if (!list.isEmpty()) {
				for (MasWard ward : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("wardId", ward.getWardId());
					map2.put("wardCode", ward.getWardCode());
					map2.put("wardName", ward.getWardName());
					map2.put("wardNo",ward.getWardNo());
					map2.put("wardNameWithCode", ward.getWardNo() +"-"+ward.getWardName());
					map2.put("wardNameWithNo", ward.getWardName() +"-"+ward.getWardNo());
					if(ward.getMasCity() != null) {
						map2.put("cityId", ward.getMasCity().getCityId());
					}else {
						map2.put("cityId", "");
					}
					if(ward.getMasCity() != null && ward.getMasCity().getMasDistrict() != null) {
						map2.put("districtId", "");
					}else {
						map2.put("districtId", "");
					}
					
					if(ward.getMasZone() != null) {
						map2.put("zoneId", ward.getMasZone().getZoneId());
					}else {
						map2.put("zoneId", "");
					}
					//map2.put("cityId", ward.getMasCity().getCityId());
					wardList.add(map2);
				}
				response.put("status", true);
				response.put("list", wardList);
			} else {
				response.put("status", true);
				response.put("list", wardList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", wardList);
		return response;
	}

	@Override
	public Map<String, Object> getZoneList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> zoneList = new ArrayList<Map<String, Object>>();
		try {
			if (HMSUtil.checkIfNull(requestData.get("cityId")) || requestData.get("cityId").equals("")) {
				map.put("msg", "City Id cannot be null");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}
			List<MasZone> list = patientRegistrationDao.getZoneList(requestData);
			if (!list.isEmpty()) {
				for (MasZone zone : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("zoneId", zone.getZoneId());
					map2.put("zoneCode", zone.getZoneCode());
					map2.put("zoneName", zone.getZoneName());
					if(zone.getMasCity()!=null && zone.getMasCity().getCityId()!=null)
					map2.put("cityId", zone.getMasCity().getCityId());
					else
						map2.put("cityId", "");
					
					zoneList.add(map2);
				}
				response.put("status", true);
				response.put("list", zoneList);
			} else {
				response.put("status", true);
				response.put("list", zoneList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", zoneList);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCampDetail(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> campDetailList = new ArrayList<Map<String, Object>>();
		try {
			/*
			 * if (HMSUtil.checkIfNull(requestData.get("districtId")) ||
			 * requestData.get("districtId").equals("")) { map.put("msg",
			 * "Please select District"); map.put("status", false); map.put("list", new
			 * ArrayList()); return map; }
			 */
			Long mmuId = null, cityId = null;
			if (HMSUtil.checkIfNull(requestData.get("cityId")) || requestData.get("cityId").equals("")) {
				map.put("msg", "Please select City");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}else {
				cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));
			}
			if (HMSUtil.checkIfNull(requestData.get("mmuId")) || requestData.get("mmuId").equals("")) {
				map.put("msg", "Please select MMU");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}else {
				mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			}
			if (HMSUtil.checkIfNull(requestData.get("year")) || requestData.get("year").equals("")) {
				map.put("msg", "Please select Year");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}
			if (HMSUtil.checkIfNull(requestData.get("month")) || requestData.get("month").equals("")) {
				map.put("msg", "Please select Month");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}
			Map<String,Object> inputMap = new HashMap<String, Object>();
			inputMap.put("cityId", cityId);
			inputMap.put("mmuId", mmuId); 
			
			response.put("zoneList", getZoneList(inputMap));
			response.put("wardList", getWardList(inputMap));
			response.put("departmentList", getMMUDepartment(inputMap));
			
			Map<String, Object> data = patientRegistrationDao.getCampDetail(requestData);
			Boolean status = (Boolean) data.get("status");
			if (status) {
				List<MasCamp> list = (List<MasCamp>) data.get("list");
				if(list!=null && list.size() >0) {
					for (MasCamp mc : list) {
						Map<String, Object> outputMap = new HashMap<>();
						outputMap.put("id", mc.getCampId());
						outputMap.put("campDate", HMSUtil.convertUtilDateToddMMyyyy(mc.getCampDate()));
						outputMap.put("day", mc.getDay());
						outputMap.put("campOrOff", mc.getWeeklyOff());
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						String startTime = "";
						if(mc.getStartTime() != null) {
							startTime = sdf.format(mc.getStartTime());
						}
						String endTime = "";
						if(mc.getEndTime() != null) {
							endTime = sdf.format(mc.getEndTime());
						}
						
						outputMap.put("startTime", startTime);
						outputMap.put("endTime", endTime);
						outputMap.put("location", mc.getLocation());
						outputMap.put("landmark", mc.getLandMark());
						outputMap.put("zoneId", "");
						outputMap.put("zoneName", "");
						if(mc.getMasZone() != null) {
							outputMap.put("zoneId", mc.getMasZone().getZoneId());
							outputMap.put("zoneName", mc.getMasZone().getZoneName());
						}
						outputMap.put("wardId", "");
						outputMap.put("wardName", "");
						if(mc.getMasWard() != null) {
							outputMap.put("wardId", mc.getMasWard().getWardId());
							outputMap.put("wardName", mc.getMasWard().getWardName());
						}
						
						outputMap.put("longitude", HMSUtil.convertNullToEmptyString(mc.getLongitude()));
						outputMap.put("lattitude", HMSUtil.convertNullToEmptyString(mc.getLattitude()));
						outputMap.put("departmentId","");
						outputMap.put("departmentName","");
						if(mc.getMasDepartment() != null) {
							outputMap.put("departmentId",mc.getMasDepartment().getDepartmentId());
							outputMap.put("departmentName",mc.getMasDepartment().getDepartmentName());
						}
						
						/*
						 * List<MasCampDepartment> departmentList = mc.getMasCampDepartment();
						 * List<Long> departList = new ArrayList<Long>(); for (MasCampDepartment mcd :
						 * departmentList) { departList.add(mcd.getMasDepartment().getDepartmentId()); }
						 * outputMap.put("departmentList", departList);
						 */
						// get ward List
						/*
						 * Map<String, Object> zoneMap = new HashMap<String, Object>();
						 * zoneMap.put("zoneId", mc.getMasZone().getZoneId()); Map<String, Object>
						 * masWardMap = getWardList(zoneMap); outputMap.put("wardList",
						 * masWardMap.get("list"));
						 */
						if(mc.getCityId()!=null && mc.getCityId()!=0)
						outputMap.put("cityId", mc.getCityId());
						else {
							outputMap.put("cityId", "");
						}
						if(mc.getMasCity()!=null && mc.getMasCity().getCityName()!=null && !mc.getMasCity().getCityName().equalsIgnoreCase(""))
							outputMap.put("cityName", mc.getMasCity().getCityName());
							else {
								outputMap.put("cityName", "");
							}
						campDetailList.add(outputMap);
					}
					response.put("status", true);
					response.put("msg", "success");
					response.put("list", campDetailList);
				}else {
					response.put("status", true);
					response.put("msg", "success");
					response.put("list", campDetailList);
				}
				
			} else {
				response.put("status", false);
				response.put("msg", "Something went wrong.");
			}
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong.");
		return response;
	}

	@Override
	public Map<String, Object> updatePatientInformation(Map<String, Object> requestData) {
		
		Map<String, Object> map = new HashMap<>();
		try {
			map = patientRegistrationDao.updatePatientInformation(requestData);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public Map<String, Object> getReligionList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> zoneList = new ArrayList<Map<String, Object>>();
		try {
			List<MasReligion> list = patientRegistrationDao.getReligionList(requestData);
			if (!list.isEmpty()) {
				for (MasReligion zone : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("religionId", zone.getReligionId());
					map2.put("religionCode", zone.getReligionCode());
					map2.put("religionName", zone.getReligionName());
					zoneList.add(map2);
				}
				response.put("status", true);
				response.put("list", zoneList);
			} else {
				response.put("status", true);
				response.put("list", zoneList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", zoneList);
		return response;
	}

	@Override
	public Map<String, Object> getBloodGroupList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> bloodGroupList = new ArrayList<Map<String, Object>>();
		try {
			List<MasBloodGroup> list = patientRegistrationDao.getBloodGroupList(requestData);
			if (!list.isEmpty()) {
				for (MasBloodGroup zone : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("bloodGroupId", zone.getBloodGroupId());
					map2.put("bloodGroupCode", zone.getBloodGroupCode());
					map2.put("bloodGroupName", zone.getBloodGroupName());
					bloodGroupList.add(map2);
				}
				response.put("status", true);
				response.put("list", bloodGroupList);
			} else {
				response.put("status", true);
				response.put("list", bloodGroupList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", bloodGroupList);
		return response;
	}

	@Override
	public Map<String, Object> getLabourTyeList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> labourList = new ArrayList<Map<String, Object>>();
		try {
			List<MasLabor> list = patientRegistrationDao.getLabourTyeList(requestData);
			if (!list.isEmpty()) {
				for (MasLabor labour : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("labourTypeId", labour.getLaborId());
					map2.put("labourTypeCode", labour.getLaborCode());
					map2.put("labourTypeName", labour.getLaborName());
					labourList.add(map2);
				}
				response.put("status", true);
				response.put("list", labourList);
			} else {
				response.put("status", true);
				response.put("list", labourList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", labourList);
		return response;
	}

	@Override
	public Map<String, Object> getIdentificationTypeList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> identificationTypeList = new ArrayList<Map<String, Object>>();
		try {
			List<MasIdentificationType> list = patientRegistrationDao.getIdentificationTypeList(requestData);
			if (!list.isEmpty()) {
				for (MasIdentificationType identification : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("identificationTypeId", identification.getIdentificationTypeId());
					map2.put("identificationTypeCode", identification.getIdentificationCode());
					map2.put("identificationTypeName", identification.getIdentificationName());
					identificationTypeList.add(map2);
				}
				response.put("status", true);
				response.put("list", identificationTypeList);
			} else {
				response.put("status", true);
				response.put("list", identificationTypeList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", identificationTypeList);
		return response;
	}

	@Override
	public Map<String, Object> getCampDepartment(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> departmentList = new ArrayList<Map<String, Object>>();
		try {
			if (HMSUtil.checkIfNull(requestData.get("mmuId")) || requestData.get("mmuId").equals("")) {
				map.put("msg", "MMU Id cannot be null or blank");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			List<MasCamp> list = patientRegistrationDao.getCampDepartment(mmuId);
			if (!list.isEmpty()) {
				for (MasCamp mapCamp : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("departmentId", mapCamp.getMasDepartment().getDepartmentId());
					map2.put("departmentCode", mapCamp.getMasDepartment().getDepartmentCode());
					map2.put("departmentName", mapCamp.getMasDepartment().getDepartmentName());
					departmentList.add(map2);
				}
				response.put("status", true);
				response.put("list", departmentList);
			} else {
				response.put("status", true);
				response.put("list", departmentList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", departmentList);
		return response;
	}

	@Override
	public Map<String, Object> createPatientAndMakeAppointment(Map<String, Object> requestData) {
		
		Map<String, Object> map = new HashMap<>();
		try {
			map = patientRegistrationDao.createPatientAndMakeAppointment(requestData);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> getMMUDepartment(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> departmentList = new ArrayList<Map<String, Object>>();
		try {
			if (HMSUtil.checkIfNull(requestData.get("mmuId")) || requestData.get("mmuId").equals("")) {
				map.put("msg", "MMU Id cannot be null");
				map.put("status", false);
				map.put("list", new ArrayList());
				return map;
			}
			List<MMUDepartment> list = patientRegistrationDao.getMMUDepartment(requestData);
			if (!list.isEmpty()) {
				for (MMUDepartment department : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("departmentId", department.getDepartmentId());
					map2.put("departmentName", department.getMasDepartment().getDepartmentName());
					
					departmentList.add(map2);
				}
				response.put("status", true);
				response.put("list", departmentList);
			} else {
				response.put("status", true);
				response.put("list", departmentList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", departmentList);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPatientList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> patientList = new ArrayList<Map<String, Object>>();
		List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
		 String ServerImageUrl = environment.getProperty("server.imageUrl");
		try {
			
			Map<String,Object> data = patientRegistrationDao.getPatientList(requestData);
			int count = (int) data.get("count");
			List<Patient> list = (List<Patient>) data.get("list");
			if (!list.isEmpty()) {
				for (Patient patient : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("patientId", patient.getPatientId());
					map2.put("patientName", patient.getPatientName());
					map2.put("mobileNo", patient.getMobileNumber());
					map2.put("uhidNo", patient.getUhidNo());
					map2.put("age", HMSUtil.calculateAgeFromDOB(patient.getDateOfBirth()));
					map2.put("genderId", "");
					map2.put("genderName", "");
					/*patient data*/
					String inPhotoBase64String = "";
					String rootPath = environment.getProperty("server.imageUrl");
		
					
					if(patient.getPatientImage() != null){
						String imageFolderPathDirctory = rootPath ;
						String completePath = imageFolderPathDirctory +"/"+ patient.getPatientImage();
						//System.out.println("completePath"+completePath);
						File f = new File(completePath);
						if(f.exists()) {
							inPhotoBase64String = encodeFileToBase64Binary(f);
							//System.out.println("inPhotoBase64String:::"+inPhotoBase64String);
						}
						//System.out.println(inPhotoBase64String);
						map2.put("patientImage",inPhotoBase64String);
					}/*else{
		             	map2.put("patientImage",  "/MMUWeb/resources/images/no-photo.jpg");
					}*/
					if(patient.getMasAdministrativeSex() != null) {
						map2.put("genderId", patient.getMasAdministrativeSex().getAdministrativeSexId());
						map2.put("genderName", patient.getMasAdministrativeSex().getAdministrativeSexName());
					}
					map2.put("patientType", HMSUtil.convertNullToEmptyString(patient.getPatientType()));
					map2.put("isLabourRegistered", HMSUtil.convertNullToEmptyString(patient.getLaborRegistered()));
					map2.put("isFormSubmitted", HMSUtil.convertNullToEmptyString(patient.getFormSubmitted()));
					map2.put("registrationNo", HMSUtil.convertNullToEmptyString(patient.getRegNo()));
					map2.put("relationId", HMSUtil.convertNullToEmptyString(patient.getRelationId()));
					map2.put("occupation", HMSUtil.convertNullToEmptyString(patient.getOccuption()));
					map2.put("labourTypeId", HMSUtil.convertNullToEmptyString(patient.getLabourId()));
					map2.put("identificationType","");
					if(patient.getMasIdentificationType() != null) {
						map2.put("identificationTypeId", HMSUtil.convertNullToEmptyString(patient.getMasIdentificationType().getIdentificationTypeId()));
					}
					map2.put("identificationNo", HMSUtil.convertNullToEmptyString(patient.getIdentificationNo()));
					/*
					 * map2.put("religionId",""); if(patient.getMasReligion() != null){
					 * map2.put("religionId",HMSUtil.convertNullToEmptyString(patient.getMasReligion
					 * ().getReligionId())); }
					 */
					map2.put("wardId",HMSUtil.convertNullToEmptyString(patient.getWardId()));
					if(patient.getMasWard() != null){
						String wardNo = HMSUtil.convertNullToEmptyString(patient.getMasWard().getWardNo());
						String wardName = HMSUtil.convertNullToEmptyString(patient.getMasWard().getWardName());
						map2.put("wardName", wardNo +"-"+wardName);
					}
					
					map2.put("castId", HMSUtil.convertNullToEmptyString(patient.getCastId()));      
					map2.put("districtId", HMSUtil.convertNullToEmptyString(patient.getDistrictId()));
					map2.put("cityId", "");
					map2.put("cityName", "");
					if(patient.getMasCity() != null) {
						map2.put("cityId", HMSUtil.convertNullToEmptyString(patient.getCityId()));
						map2.put("cityName", HMSUtil.convertNullToEmptyString(patient.getMasCity().getCityName()));
					}
					
					map2.put("address", HMSUtil.convertNullToEmptyString(patient.getAddress()));
					map2.put("pincode", HMSUtil.convertNullToEmptyString(patient.getPincode()));
					map2.put("bloodGroupId","");
					if(patient.getMasBloodGroup() != null) {
						map2.put("bloodGroupId", HMSUtil.convertNullToEmptyString(patient.getMasBloodGroup().getBloodGroupId()));
					}
					
					
					 
					 // map2.put("patientImg", patientDataUploadsList);
					patientList.add(map2);
				}
				
				//Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				  
			    //patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase()).list();
								
			    //System.out.println("PatientDataUpload:"+patientDataUploadsList);
				//System.out.println(patientList);
				
				response.put("status", true);
				response.put("list", patientList);
				response.put("count", count);
				response.put("PatientDataUpload", patientDataUploadsList);
				response.put("ServerImageUrl", ServerImageUrl);
			} else {
				response.put("status", true);
				response.put("list", patientList);
				response.put("count", "0");
				response.put("PatientDataUpload", patientDataUploadsList);
				response.put("ServerImageUrl", ServerImageUrl);
			}
			  //System.out.println("response:"+response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", patientList);
		response.put("count", "0");
		response.put("PatientDataUpload", patientDataUploadsList);
		response.put("ServerImageUrl", ServerImageUrl);
		return response;
	}

	@Override
	public Map<String, Object> getOnlinePatientList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> patientList = new ArrayList<Map<String, Object>>();
		try {
			
			response = patientRegistrationDao.getOnlinePatientList(requestData);
			List<Visit> list = (List<Visit>) response.get("list");
			int count = (int) response.get("count");
			if (!list.isEmpty()) {
				for (Visit visit : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("visitId", visit.getVisitId());
					map2.put("patientId", visit.getPatientId());
					map2.put("patientName", visit.getPatient().getPatientName());
					map2.put("mobileNo", visit.getPatient().getMobileNumber());
					map2.put("age", HMSUtil.calculateAgeFromDOB(visit.getPatient().getDateOfBirth()));
					map2.put("gender","");
					if(visit.getPatient().getMasAdministrativeSex() != null) {
						map2.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						map2.put("genderId",visit.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
					}
					patientList.add(map2);
				}
				response.put("status", true);
				response.put("count", count);
				response.put("list", patientList);
			} else {
				response.put("status", true);
				response.put("count", count);
				response.put("list", patientList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("count", "0");
		response.put("list", patientList);
		return response;
	}

	@Override
	public Map<String, Object> getPatientDataBasedOnVisit(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			Long visitId = Long.parseLong(String.valueOf(requestData.get("visitId")));
			Visit visit = patientRegistrationDao.getPatientDataBasedOnVisit(visitId);
			if (visit != null) {
				
				map.put("visitId", visit.getVisitId());
				map.put("patientId", visit.getPatient().getPatientId());
				map.put("patientName", visit.getPatient().getPatientName());
				map.put("mobileNo", visit.getPatient().getMobileNumber());
				map.put("age", HMSUtil.calculateAgeFromDOB(visit.getPatient().getDateOfBirth()));
				map.put("gender","");
				if(visit.getPatient().getMasAdministrativeSex() != null) {
					map.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
				}
				
			}
			map.put("status", true);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("status", false);
		map.put("msg", "Something went wrong.");
		return map;
	}

	@Override
	public String saveVitalDetailsAndUpdateVisit(HashMap<String, Object> payload) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		OpdPatientDetail opddetails = new OpdPatientDetail();
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		
		try {
			if (!payload.isEmpty()) {
				JSONObject nullbalankvalidation = null;
				nullbalankvalidation = ValidateUtils.addVitalPreConsulataionDetailsvalidation(payload);
				if (nullbalankvalidation.optString("status").equals("0")) {
					return nullbalankvalidation.toString();
				} else {

					//opddetails.setUhidNo(Long.parseLong(payload.get("uhidNo").toString()));
					opddetails.setPatientId(Long.parseLong(payload.get("patientId").toString()));
					opddetails.setVisitId(Long.parseLong(payload.get("visitId").toString()));
					opddetails.setHeight(payload.get("height").toString());
					opddetails.setWeight(payload.get("weight").toString());
					opddetails.setTemperature(payload.get("temperature").toString());
					opddetails.setBpSystolic(payload.get("bp").toString());
					opddetails.setBpDiastolic(payload.get("bp1").toString());
					opddetails.setPulse(payload.get("pulse").toString());
					opddetails.setSpo2(payload.get("spo2").toString());
					opddetails.setBmi(payload.get("bmi").toString());
					opddetails.setRr(payload.get("rr").toString());
					opddetails.setOpdDate(ourJavaTimestampObject);
					opddetails.setLastChgDate(ourJavaTimestampObject);
				

					//String resp = patientRegistrationDao.opdVitalDetails(opddetails);
					String resp = patientRegistrationDao.opdVitalDetails(payload);
					
				

					if (resp != null && resp.equalsIgnoreCase("200")) {
						json.put("msg", "Vital details inserted successfully");
						json.put("status", "1");
					} else if (resp != null && resp.equalsIgnoreCase("403")) {
						json.put("msg", " you are not authorized for this activity ");
						json.put("status", "0");
					} else {
						json.put("msg", resp);
						json.put("status", "0");
					}
				}
			} else {
				json.put("status", "0");
				json.put("msg", "json not contain any object");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public Map<String, Object> getWardListWithoutCity(Map<String, Object> requestData) {
		
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> wardList = new ArrayList<Map<String, Object>>();
		try {
			
			List<MasWard> list = patientRegistrationDao.getWardListWithoutCity(requestData);
			if (!list.isEmpty()) {
				for (MasWard ward : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("wardId", ward.getWardId());
					map2.put("wardCode", ward.getWardCode());
					map2.put("wardName", ward.getWardName());
					map2.put("wardNameWithCode", ward.getWardNo() +"-"+ward.getWardName());
					if(ward.getMasCity() != null) {
						map2.put("cityId", ward.getMasCity().getCityId());
					}else {
						map2.put("cityId", "");
					}
					if(ward.getMasCity() != null && ward.getMasCity().getMasDistrict() != null) {
						map2.put("districtId", ward.getMasCity().getMasDistrict().getDistrictId());
					}else {
						map2.put("districtId", "");
					}
					
					if(ward.getMasZone() != null) {
						map2.put("zoneId", ward.getMasZone().getZoneId());
					}else {
						map2.put("zoneId", "");
					}
					 
					//map2.put("cityId", ward.getMasCity().getCityId());
					wardList.add(map2);
				}
				response.put("status", true);
				response.put("list", wardList);
			} else {
				response.put("status", true);
				response.put("list", wardList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", wardList);
		return response;
	}

	@Override
	public Map<String, Object> getRelationList(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> relationList = new ArrayList<Map<String, Object>>();
		try {
			List<MasRelation> list = patientRegistrationDao.getRelationList(requestData);
			if (!list.isEmpty()) {
				for (MasRelation relation : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("relationId", relation.getRelationId());
					map2.put("relationCode", relation.getRelationCode());
					map2.put("relationName", relation.getRelationName());
					relationList.add(map2);
				}
				response.put("status", true);
				response.put("list", relationList);
			} else {
				response.put("status", true);
				response.put("list", relationList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.put("status", false);
		response.put("list", relationList);
		return response;
	}

	@Override
	public Map<String, Object> getCityIdAndName(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			MasMMU masMMU = (MasMMU) patientRegistrationDao.getCityIdAndName(requestData);
			response.put("cityId", masMMU.getCityId());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("cityId", null);
		return response;
	}

	@Override
	public Map<String, Object> saveLabourRegistration(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = patientRegistrationDao.saveLabourRegistration(requestData);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> checkIfPatientIsAlreadyRegistered(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = patientRegistrationDao.checkIfPatientIsAlreadyRegistered(requestData);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> deleteCampPlan(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = patientRegistrationDao.deleteCampPlan(requestData);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> getFutureCampPlan(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		List<Map<String, Object>> campList = new ArrayList<Map<String,Object>>();
		try {
			map = patientRegistrationDao.getFutureCampPlan(requestData);
			Boolean status = (Boolean) map.get("status");
			if(status) {
				List<MasCamp> list = (List<MasCamp>) map.get("list");
				if(list.isEmpty()) {
					response.put("status", true);
					response.put("list",campList);
				}else {
					int count = 1;
					for(MasCamp masCamp : list) {
						Map<String, Object> data = new HashMap<>();
						data.put("sNo", count);
						data.put("mmuName", masCamp.getMasMMU().getMmuName());
						data.put("campLocation", masCamp.getLocation());
						data.put("landmark", masCamp.getLandMark());
						data.put("timing", masCamp.getStartTime()+" - "+masCamp.getEndTime());
						count++;
						campList.add(data);
					}
					response.put("status", true);
					response.put("list",campList);
				}
			}else {
				return map;
			}
			
				
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list",campList);
		response.put("msg","Something went wrong");
		return response;
	}
	
	private String encodeFileToBase64Binary(File file) {
		String encodedfile = null;
		try {
			@SuppressWarnings("resource")
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
			fileInputStreamReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedfile;
	}

	
}
