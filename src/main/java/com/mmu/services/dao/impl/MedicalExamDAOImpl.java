package com.mmu.services.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DgOrderdtDao;
import com.mmu.services.dao.DgOrderhdDao;
import com.mmu.services.dao.DgResultEntryHdDao;
import com.mmu.services.dao.FamilyDetailsDAO;
import com.mmu.services.dao.MasMedicalCategoryDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.dao.PatientDao;
import com.mmu.services.dao.PatientDocumentDetailDao;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.FamilyDetail;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasInvestignationMapping;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasUnit;

import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDiseaseInfo;
import com.mmu.services.entity.PatientDocumentDetail;
import com.mmu.services.entity.PatientImmunizationHistory;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.PatientPastMedHistory;
import com.mmu.services.entity.PatientServicesDetail;
import com.mmu.services.entity.ReferralPatientDt;
import com.mmu.services.entity.ReferralPatientHd;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.impl.MedicalExamServiceImpl;
import com.mmu.services.service.impl.OpdServiceImpl;
import com.mmu.services.service.impl.PatientRegistrationServiceImpl;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;
import com.mmu.services.utils.UtilityServices;

//import oracle.jdbc.OracleTypes;

@Repository
@Transactional
public class MedicalExamDAOImpl extends GenericDaoImpl<MasMedicalExamReport, Long> implements MedicalExamDAO {

	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String DG_ORDER_DT = databaseScema + "." + "DG_ORDER_DT";
	final String DG_ORDER_HD = databaseScema + "." + "DG_ORDER_HD";
	final String DG_MAS_INVESTIGATION = databaseScema + "." + "DG_MAS_INVESTIGATION";
	final String DG_RESULT_ENTRY_DT = databaseScema + "." + "DG_RESULT_ENTRY_DT";
	final String DG_RESULT_ENTRY_HD = databaseScema + "." + "DG_RESULT_ENTRY_HD";
	final String MAS_SUB_CHARGECODE = databaseScema + "." + "MAS_SUB_CHARGECODE";
	final String DG_UOM = databaseScema + "." + "DG_UOM";
	final String DG_SUB_MAS_INVESTIGATION = databaseScema + "." + "DG_SUB_MAS_INVESTIGATION";
	final String MAS_HOSPITAL = databaseScema + "." + "mas_hospital";
	final String MAS_MEDICAL_EXAM_REPORT = databaseScema + "." + "mas_medical_exam_report";

	final String VU_MAS_UNIT = databaseScema + "." + "vu_mas_unit";

	@Autowired
	GetHibernateUtils getHibernateUtils;
	@Autowired
	DgOrderhdDao dgOrderhdDao;

	@Autowired
	SystemAdminDao systemAdminDao;
	@Autowired
	OpdMasterDao opdMasterDao;

	// @Autowired
	// VisitDao visitDao;
	@Autowired
	PatientRegistrationServiceImpl patientRegistrationServiceImpl;

	@Autowired
	DgResultEntryHdDao dgResultEntryHdDao;

	@Autowired
	DgOrderdtDao dgOrderdtDao;

	// @Autowired
	// DgResultEntryDtDao dgResultEntryDtDao;
	@Autowired
	PatientDao patientDao;

	@Autowired
	PatientDocumentDetailDao patientDocumentDetailDao;
	@Autowired
	MasMedicalCategoryDao masMedicalCategoryDao;

	@Autowired
	FamilyDetailsDAO familyDetailsDAO;

	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = false) public Map<String,Object>
	 * getValidateMedicalExamList(HashMap<String,Object> jsonData) {
	 * List<Visit>listVisit=null; Map<String,Object>map=new HashMap<>(); int count =
	 * 0; Criterion cr1=null; Criterion cr2=null; try { int pageNo =
	 * Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * 
	 * Long userId=null; Long hospitalId=null; Criterion cr4=null; Criterion
	 * cr5=null; Criterion cr6=null; MasUnit masUnit=null; String serviceNo="";
	 * if(jsonData.get("employeeId")!=null &&
	 * StringUtils.isNotBlank(jsonData.get("employeeId").toString())) {
	 * userId=Long.parseLong(jsonData.get("employeeId").toString()); }
	 * 
	 * if(jsonData.get("hospitalId")!=null &&
	 * StringUtils.isNotBlank(jsonData.get("hospitalId").toString())) {
	 * hospitalId=Long.parseLong(jsonData.get("hospitalId").toString());
	 * masUnit=getMasUnitByHospitalId(hospitalId); } Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria
	 * criteria=session.createCriteria(Visit.class,"visit").createAlias(
	 * "visit.masAppointmentType",
	 * "masAppointmentType").createAlias("visit.patient", "patient");
	 * 
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("a")) {
	 * criteria.add(Restrictions.isNull("visit.tokenNo")).add(Restrictions.ne(
	 * "visit.visitFlag", "E").ignoreCase()); cr1=
	 * Restrictions.or(Restrictions.eq("visit.examStatus",
	 * "I").ignoreCase(),Restrictions.eq("visit.examStatus", "S").ignoreCase());
	 * criteria.add(cr1); } else
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("b")) {
	 * criteria.add(Restrictions.isNotNull("visit.tokenNo"));
	 * cr1=Restrictions.eq("visit.examStatus", "V").ignoreCase();
	 * cr2=Restrictions.or(Restrictions.eq("visit.examStatus",
	 * "T").ignoreCase(),Restrictions.eq("visit.examStatus",
	 * "R").ignoreCase(),Restrictions.eq("visit.examStatus", "S").ignoreCase());
	 * LogicalExpression orExp = Restrictions.or(cr1,cr2); criteria.add(orExp);
	 * 
	 * } else if(jsonData.get("flagForList").toString().equalsIgnoreCase("c")) {
	 * criteria.add(Restrictions.isNotNull("visit.tokenNo"));
	 * cr1=Restrictions.eq("visit.examStatus", "V").ignoreCase();
	 * cr2=Restrictions.or(Restrictions.eq("visit.examStatus",
	 * "T").ignoreCase(),Restrictions.eq("visit.examStatus",
	 * "R").ignoreCase(),Restrictions.eq("visit.examStatus", "S").ignoreCase());
	 * 
	 * LogicalExpression orExp = Restrictions.or(cr1,cr2); criteria.add(orExp);
	 * 
	 * }
	 * 
	 * if(jsonData.get("serviceNo")!=null) {
	 * serviceNo=jsonData.get("serviceNo").toString(); if (serviceNo != null &&
	 * !serviceNo.equals("") && !serviceNo.equals("null")) {
	 * cr2=Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase();
	 * criteria.add(cr2); }
	 * 
	 * } Criterion cr7=Restrictions.eq("visit.hospitalId", hospitalId); cr6=
	 * Restrictions.eq("masAppointmentType.appointmentTypeCode", "ME").ignoreCase();
	 * criteria.add(cr6); criteria.add(cr7).addOrder(Order.asc("visit.visitId"));
	 * listVisit=criteria.list();
	 * 
	 * count = listVisit.size();
	 * 
	 * //criteria = criteria.setFirstResult(pagingSize * (pageNo - 1)); if
	 * (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {
	 * criteria = criteria.setFirstResult(pagingSize * (1 - 1)); } else { criteria =
	 * criteria.setFirstResult(pagingSize * (pageNo - 1)); } criteria =
	 * criteria.setMaxResults(pagingSize);
	 * 
	 * criteria.setFirstResult((pagingSize) * (pageNo - 1));
	 * criteria.setMaxResults(pagingSize); listVisit = criteria.list();
	 * map.put("listVisit", listVisit); map.put("count", count);
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return map; }
	 * 
	 * @Override public Map<String, Object>
	 * getValidateMedicalExamDetails(HashMap<String, Object> jsonData) {
	 * List<Visit>listVisit=null;
	 * List<MasInvestignationMapping>listMasInvestignationMapping=null;
	 * List<DgMasInvestigation>listDgMasInvestigation=null;
	 * List<Object[]>listOfInvestigationList=null; Map<String,Object>mapValue=new
	 * HashMap<>(); List<Long>dgInvestigationId=new ArrayList<>(); Criteria
	 * criteria=null; String agePatient=""; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); String visitId =
	 * jsonData.get("visitId").toString(); String agePatientTemp="";
	 * if(jsonData.containsKey("meAge")) { agePatientTemp =
	 * jsonData.get("meAge").toString(); }
	 * visitId=OpdServiceImpl.getReplaceString(visitId);
	 * if(StringUtils.isNotEmpty(agePatientTemp) &&
	 * !agePatientTemp.equalsIgnoreCase("0")) { String []
	 * tempAge=agePatientTemp.split(","); if(tempAge!=null && tempAge.length>0) {
	 * agePatient=tempAge[tempAge.length-1]; } }
	 * 
	 * 
	 * criteria=session.createCriteria(Visit.class) .add(Restrictions.eq("visitId",
	 * Long.parseLong(visitId))); listVisit=criteria.list();
	 * if(StringUtils.isNotEmpty(agePatient)) { Criteria
	 * criteria1=session.createCriteria(MasInvestignationMapping.class)
	 * .add(Restrictions.ilike("age", "%"
	 * +agePatient.toString().trim()+"%",MatchMode.ANYWHERE));
	 * listMasInvestignationMapping=criteria1.list(); } String investigationList="";
	 * if(CollectionUtils.isNotEmpty(listMasInvestignationMapping)) {
	 * for(MasInvestignationMapping
	 * masInvestignationMapping:listMasInvestignationMapping) {
	 * if(StringUtils.isBlank(investigationList) ) {
	 * investigationList=masInvestignationMapping.getInvestignationId(); } else {
	 * investigationList+=","+masInvestignationMapping.getInvestignationId(); } }
	 * 
	 * if(investigationList!=null && !investigationList.equalsIgnoreCase("")) {
	 * String [] investMappingValue=investigationList.split(","); for(String
	 * ss:investMappingValue) { dgInvestigationId.add(Long.parseLong(ss.trim())); }
	 * } } if(CollectionUtils.isNotEmpty(listVisit) &&
	 * StringUtils.isNotEmpty(listVisit.get(0).getExamStatus())&&
	 * listVisit.get(0).getExamStatus().equalsIgnoreCase("I")) {
	 * if(CollectionUtils.isNotEmpty(dgInvestigationId)) { Criteria
	 * criteria2=session.createCriteria(DgMasInvestigation.class)
	 * .add(Restrictions.in("investigationId",dgInvestigationId)); ProjectionList
	 * projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("investigationId").as(
	 * "investigationId"));
	 * projectionList.add(Projections.property("investigationName").as(
	 * "investigationName"));
	 * projectionList.add(Projections.property("investigationType").as(
	 * "investigationType"));
	 * criteria2.setProjection(projectionList).addOrder(Order.asc("investigationId")
	 * );
	 * 
	 * listDgMasInvestigation = criteria2 .setResultTransformer(new
	 * AliasToBeanResultTransformer(DgMasInvestigation.class)).list(); } }
	 * if(CollectionUtils.isNotEmpty(listVisit) &&
	 * StringUtils.isNotEmpty(listVisit.get(0).getExamStatus()) &&
	 * listVisit.get(0).getExamStatus().equalsIgnoreCase("s") &&
	 * listVisit.get(0).getTokenNo()==null) { Criteria
	 * criteria2=session.createCriteria(DgOrderdt.class,"dgOrderDt").createAlias(
	 * "dgOrderDt.dgOrderHd", "dgOrderHd")
	 * .createAlias("dgOrderDt.dgMasInvestigations",
	 * "dgMasInvestigations",JoinType.LEFT_OUTER_JOIN)
	 * .add(Restrictions.eq("dgOrderHd.visitId",
	 * Long.parseLong(visitId))).add(Restrictions.isNull("dgOrderDt.orderDetId"));
	 * //.add(Restrictions.in("dgMasInvestigations.investigationId",
	 * dgInvestigationId)); ProjectionList projectionList =
	 * Projections.projectionList();
	 * projectionList.add(Projections.property("dgMasInvestigations.investigationId"
	 * ).as("investigationId")); projectionList.add(Projections.property(
	 * "dgMasInvestigations.investigationName").as("investigationName"));
	 * projectionList.add(Projections.property(
	 * "dgMasInvestigations.investigationType").as("investigationType"));
	 * 
	 * projectionList.add(Projections.property("dgOrderDt.orderdtId").as("orderdtId"
	 * ));
	 * projectionList.add(Projections.property("dgOrderHd.orderhdId").as("orderhdId"
	 * ));
	 * projectionList.add(Projections.property("dgOrderDt.investigationRemarks").as(
	 * "investigationRemarks"));
	 * projectionList.add(Projections.property("dgOrderHd.otherInvestigation").as(
	 * "otherInvestigation"));
	 * projectionList.add(Projections.property("dgOrderDt.labMark").as("labMark"));
	 * criteria2.setProjection(projectionList).addOrder(Order.asc("orderdtId"));
	 * listOfInvestigationList=criteria2.list(); } mapValue.put("listVisit",
	 * listVisit); mapValue.put("listDgMasInvestigation", listDgMasInvestigation);
	 * mapValue.put("listOfInvestigationList", listOfInvestigationList);
	 * 
	 * } catch(Exception e) { e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return mapValue; }
	 * 
	 * @Override
	 * 
	 * @Transactional public String deletePatientMedCat(PatientMedicalCat
	 * patientMedicalCat) { String result = null;
	 * dgOrderhdDao.deleteObject("PatientMedicalCat","medicalCatId",
	 * patientMedicalCat.getMedicalCatId());
	 * 
	 * 
	 * result = "success";
	 * 
	 * return result;
	 * 
	 * }
	 * 
	 * 
	 * @Transactional
	 * 
	 * @Override public Long saveOrUpdateVisit(Visit visit) { Session session=null;
	 * Long visitId=null; //Transaction tx=null; try{ session=
	 * getHibernateUtils.getHibernateUtlis().OpenSession();
	 * //tx=session.beginTransaction(); session.saveOrUpdate(visit);
	 * visitId=visit.getVisitId(); session.flush(); session.clear(); //tx.commit();
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * } return visitId; }
	 * 
	 * @Override public DgOrderhd getDgOrderHdByVisitIdAndPatient(Long visitId,Long
	 * patientId) { DgOrderhd dgOrderhd=null; List<DgOrderhd>listDgOrderhd=null;
	 * try{ Session session= getHibernateUtils.getHibernateUtlis().OpenSession();
	 * listDgOrderhd=
	 * session.createCriteria(DgOrderhd.class).add(Restrictions.eq("visitId",
	 * visitId)) .add(Restrictions.eq("patientId", patientId)).list() ;
	 * if(CollectionUtils.isNotEmpty(listDgOrderhd)) {
	 * dgOrderhd=listDgOrderhd.get(0); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return dgOrderhd;
	 * }
	 * 
	 * @Override
	 * 
	 * @Transactional public String saveOrUpdateMedicalExam(MasMedicalExamReport
	 * masMedicalExamReprt,HashMap<String, Object> payload,String patientId, String
	 * hospitalId,String userId) { Session session=null; Long
	 * medicalExaminationId=null; Transaction tx=null; String satusOfMessage="";
	 * String statusOfPatient=""; try{ session=
	 * getHibernateUtils.getHibernateUtlis().OpenSession();
	 * tx=session.beginTransaction(); String obsistyCheckAlready ="";
	 * if(payload.get("obsistyCheckAlready")!=null) { obsistyCheckAlready =
	 * payload.get("obsistyCheckAlready").toString(); obsistyCheckAlready =
	 * OpdServiceImpl.getReplaceString(obsistyCheckAlready); }
	 * if(!obsistyCheckAlready.equalsIgnoreCase("exits")) {
	 * 
	 * String obsistyMark = payload.get("obsistyMark").toString(); obsistyMark =
	 * OpdServiceImpl.getReplaceString(obsistyMark);
	 * if(!obsistyMark.equalsIgnoreCase("") &&
	 * !obsistyMark.equalsIgnoreCase("none")) {
	 * 
	 * saveUpdateOpdObsistyMe(payload) ; } } payload.put("digiFlag", "yes");
	 * saveOrUpdateMedicalCategory(payload);
	 * saveOrUpdateMedicalCatComposite(payload);
	 * 
	 * try { String medicalCompositeNameValue =
	 * payload.get("medicalCompositeNameValue").toString();
	 * medicalCompositeNameValue =
	 * OpdServiceImpl.getReplaceString(medicalCompositeNameValue);
	 * 
	 * String diagnosisiIdMC = payload.get("diagnosisiIdMC").toString();
	 * diagnosisiIdMC = OpdServiceImpl.getReplaceString(diagnosisiIdMC); String[]
	 * diagnosisiIdMCValues = null; String visitId =
	 * payload.get("visitId").toString(); visitId =
	 * OpdServiceImpl.getReplaceString(visitId);
	 * if(StringUtils.isNotEmpty(diagnosisiIdMC)) { diagnosisiIdMCValues =
	 * diagnosisiIdMC.trim().split(","); } String fitFlagCheckValue="";
	 * if(payload.containsKey("fitFlagCheckValue")) {
	 * fitFlagCheckValue=payload.get("fitFlagCheckValue").toString();
	 * fitFlagCheckValue=OpdServiceImpl.getReplaceString(fitFlagCheckValue); }
	 * if(StringUtils.isEmpty(medicalCompositeNameValue) &&
	 * diagnosisiIdMCValues==null && StringUtils.isNotEmpty(fitFlagCheckValue) &&
	 * (fitFlagCheckValue.equalsIgnoreCase("Yes")||
	 * fitFlagCheckValue.contains("Yes"))) { Long masMedicalCategoryId=null;
	 * if(StringUtils.isNotEmpty(patientId)) { Patient patient=
	 * checkPatientForPatientId(Long.parseLong(patientId));
	 * 
	 * Criterion cr1=Restrictions.eq("fitFlag", "F").ignoreCase();
	 * List<MasMedicalCategory>listMasMedicalCategory=masMedicalCategoryDao.
	 * findByCriteria(cr1); if(CollectionUtils.isNotEmpty(listMasMedicalCategory)) {
	 * masMedicalCategoryId=listMasMedicalCategory.get(0).getMedicalCategoryId();
	 * patient.setMedicalCategoryId(masMedicalCategoryId); }
	 * patient.setFitFlag("F");
	 * 
	 * if(payload.get("gender")!=null) { String
	 * gender=payload.get("gender").toString();
	 * gender=OpdServiceImpl.getReplaceString(gender);
	 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
	 * 
	 * if(StringUtils.isNotBlank(patientId))
	 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
	 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined") &&
	 * !gender.equalsIgnoreCase("0"))
	 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
	 * saveOrUpdatePatient(patient);
	 * 
	 * PatientMedicalCat patientMedicalCat=null;
	 * List<PatientMedicalCat>listPatientMedicalCat=null;
	 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
	 * listPatientMedicalCat=
	 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
	 * ;
	 * 
	 * if(CollectionUtils.isEmpty( listPatientMedicalCat)) { patientMedicalCat=new
	 * PatientMedicalCat(); if(StringUtils.isNotEmpty(patientId))
	 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
	 * if(masMedicalCategoryId!=null)
	 * patientMedicalCat.setpMedCatId(masMedicalCategoryId);
	 * //patientMedicalCat.setMedicalCategoryId(masMedicalCategoryId);
	 * if(StringUtils.isNotEmpty(visitId))
	 * patientMedicalCat.setVisitId(Long.parseLong(visitId)); String dateOfExam ="";
	 * if(payload.containsKey("dateOfExam")) { dateOfExam =
	 * payload.get("dateOfExam").toString(); dateOfExam =
	 * OpdServiceImpl.getReplaceString(dateOfExam); }
	 * 
	 * if(StringUtils.isNotEmpty(dateOfExam) && dateOfExam!=null &&
	 * !dateOfExam.equalsIgnoreCase("") &&
	 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date
	 * medicalCompositeDateValue = null; medicalCompositeDateValue =
	 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
	 * if(medicalCompositeDateValue!=null) { Timestamp ts = new
	 * Timestamp(medicalCompositeDateValue.getTime());
	 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
	 * patientMedicalCat.setpMedFitFlag("F"); saveOrUpdatePatientMedicalCat(
	 * patientMedicalCat); }
	 * 
	 * } } else { if(StringUtils.isNotEmpty(patientId)&&
	 * StringUtils.isEmpty(medicalCompositeNameValue)) { Patient patient=
	 * checkPatientForPatientId(Long.parseLong(patientId)); patient.setFitFlag("U");
	 * patient.setMedicalCategoryId(null);
	 * 
	 * if(payload.get("gender")!=null) { String
	 * gender=payload.get("gender").toString();
	 * gender=OpdServiceImpl.getReplaceString(gender);
	 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
	 * 
	 * if(StringUtils.isNotBlank(patientId))
	 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
	 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined") &&
	 * !gender.equalsIgnoreCase("0"))
	 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
	 * 
	 * saveOrUpdatePatient(patient);
	 * List<PatientMedicalCat>listPatientMedicalCat=null;
	 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
	 * listPatientMedicalCat=
	 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
	 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
	 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
	 * deletePatientMedCat(patientMedicalCat); }
	 * 
	 * } } } catch(Exception e) { e.printStackTrace(); }
	 * 
	 * masMedicalExamReprt=updateInvestigationDgResult(payload, patientId,
	 * hospitalId, userId, masMedicalExamReprt);
	 * 
	 * 
	 * 
	 * saveUpdatePatientImmunizationHistory(payload); String saveInDraft =
	 * payload.get("saveInDraft").toString(); saveInDraft =
	 * OpdServiceImpl.getReplaceString(saveInDraft);
	 * 
	 * 
	 * if (StringUtils.isNotEmpty(saveInDraft) &&
	 * !saveInDraft.equalsIgnoreCase("draftMa")) {
	 * masMedicalExamReprt=saveUpdateForReferalforMe(masMedicalExamReprt,payload,
	 * hospitalId); masMedicalExamReprt = getActionForMedicalExam(payload,
	 * masMedicalExamReprt); } else { OpdPatientDetail opdPatientDetail=null;
	 * saveUpdateOpdVitalDetails(payload, opdPatientDetail, hospitalId,
	 * masMedicalExamReprt); }
	 * 
	 * 
	 * String meRmsId=payload.get("meRmsId").toString();
	 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
	 * meRmsId.split(",");
	 * 
	 * if(meRmsIds!=null && meRmsIds.length>0 && meRmsIds[0]!=null &&
	 * !meRmsIds[0].equalsIgnoreCase("") && !meRmsIds[0].equalsIgnoreCase("0")) {
	 * masMedicalExamReprt.setRidcId(Long.parseLong(meRmsIds[0].toString())); }
	 * 
	 * String meRmsIdOld=payload.get("meRmsIdOld").toString();
	 * meRmsId=OpdServiceImpl.getReplaceString(meRmsIdOld); String[] meRmsIdOlds =
	 * meRmsIdOld.split(",");
	 * 
	 * if(meRmsIdOlds!=null && meRmsIdOlds.length>0 && meRmsIdOlds[0]!=null &&
	 * !meRmsIdOlds[0].equalsIgnoreCase("") &&
	 * !meRmsIdOlds[0].equalsIgnoreCase("0")) {
	 * masMedicalExamReprt.setMeRidcId(Long.parseLong(meRmsIdOlds[0].toString())); }
	 * 
	 * session.saveOrUpdate(masMedicalExamReprt);
	 * 
	 * medicalExaminationId=masMedicalExamReprt.getMedicalExaminationId();
	 * if(StringUtils.isNotEmpty(masMedicalExamReprt.getStatus())) {
	 * statusOfPatient=masMedicalExamReprt.getStatus(); } else {
	 * statusOfPatient="no status"; } session.flush(); session.clear(); tx.commit();
	 * if(medicalExaminationId!=null && medicalExaminationId!=0) {
	 * satusOfMessage=medicalExaminationId+
	 * "##"+"Medical Exam Submitted Successfully"+"##"+statusOfPatient; } else {
	 * satusOfMessage=0+
	 * "##"+"Data is not updated because column size exceed of actual size."+"##"+
	 * statusOfPatient; }
	 * 
	 * } catch(Exception e) { if (tx != null) { try { tx.rollback();
	 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
	 * toString()+"##"+statusOfPatient; } catch(Exception re) {
	 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+re.
	 * toString()+"##"+statusOfPatient; re.printStackTrace(); } }
	 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
	 * toString()+"##"+statusOfPatient; e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * } return satusOfMessage; }
	 * 
	 * public MasMedicalExamReport saveUpdateForReferalforMe(MasMedicalExamReport
	 * masMedicalExamReprt,HashMap<String, Object> payload,String hospitalId ) { try
	 * { OpdPatientDetail opdPatientDetail=null; String
	 * actionMe=payload.get("actionMe").toString();
	 * actionMe=OpdServiceImpl.getReplaceString(actionMe); String visitId =
	 * payload.get("visitId").toString(); visitId =
	 * OpdServiceImpl.getReplaceString(visitId);
	 * 
	 * if(StringUtils.isNotEmpty(actionMe) && actionMe.equalsIgnoreCase("referred"))
	 * { masMedicalExamReprt.setStatus("rf"); opdPatientDetail=
	 * getOpdPatientDetailByVisitId(Long.parseLong(visitId));
	 * if(opdPatientDetail==null) { opdPatientDetail=new OpdPatientDetail(); Date
	 * date=new Date(); opdPatientDetail.setLastChgDate(new
	 * Timestamp(date.getTime())); }
	 * 
	 * 
	 * if(payload.containsKey("chiefComplaint") &&
	 * payload.get("chiefComplaint")!=null) { String
	 * chiefComplaint=payload.get("chiefComplaint").toString();
	 * chiefComplaint=OpdServiceImpl.getReplaceString(chiefComplaint);
	 * masMedicalExamReprt.setChiefComplaint(chiefComplaint);
	 * 
	 * //Save or Update patient history
	 * 
	 * String patientId = payload.get("patientId").toString(); patientId =
	 * OpdServiceImpl.getReplaceString(patientId);
	 * 
	 * //String hospitalId = payload.get("hospitalId").toString(); // hospitalId =
	 * OpdServiceImpl.getReplaceString(hospitalId);
	 * 
	 * 
	 * String userId = payload.get("userId").toString(); userId =
	 * OpdServiceImpl.getReplaceString(userId); OpdPatientHistory
	 * opdPatientHistory=null; if(StringUtils.isNotEmpty(visitId)) {
	 * opdPatientHistory
	 * =checkVisitOpdPatientHistoryByVisitIdAndPatientId(Long.parseLong(visitId));
	 * if(opdPatientHistory==null) { opdPatientHistory=new OpdPatientHistory();
	 * if(StringUtils.isNotEmpty(visitId))
	 * opdPatientHistory.setVisitId(Long.parseLong(visitId)); } } else {
	 * opdPatientHistory=new OpdPatientHistory();
	 * if(StringUtils.isNotEmpty(visitId))
	 * opdPatientHistory.setVisitId(Long.parseLong(visitId)); }
	 * opdPatientHistory.setChiefComplain(chiefComplaint);
	 * if(StringUtils.isNotEmpty(hospitalId))
	 * opdPatientHistory.setHospitalId(Long.parseLong(hospitalId));
	 * 
	 * if(StringUtils.isNotEmpty(patientId))
	 * opdPatientHistory.setPatientId(Long.parseLong(patientId));
	 * 
	 * 
	 * if(StringUtils.isNotEmpty(userId))
	 * opdPatientHistory.setLastChgBy(Long.parseLong(userId)); Date date=new Date();
	 * opdPatientHistory.setLastChgDate(new Timestamp(date.getTime()));
	 * 
	 * saveOrUpdateOpdPatientHis(opdPatientHistory);
	 * 
	 * }
	 * 
	 * if(payload.get("remarksForReferal")!=null) { String
	 * remarksForReferal=payload.get("remarksForReferal").toString();
	 * remarksForReferal=OpdServiceImpl.getReplaceString(remarksForReferal);
	 * masMedicalExamReprt.setRemarksRef(remarksForReferal); }
	 * if(payload.get("Pollar")!=null) { String pollor =
	 * payload.get("Pollar").toString(); pollor =
	 * OpdServiceImpl.getReplaceString(pollor.trim());
	 * masMedicalExamReprt.setPollor(pollor); opdPatientDetail.setPollor(pollor); }
	 * 
	 * if(payload.get("Ordema")!=null) { String edema =
	 * payload.get("Ordema").toString(); edema =OpdServiceImpl.
	 * getReplaceString(edema); masMedicalExamReprt.setEdema(edema.trim());
	 * opdPatientDetail.setEdema(edema); } if(payload.get("cyanosis")!=null) {
	 * String cyanosis = payload.get("cyanosis").toString(); cyanosis =
	 * OpdServiceImpl.getReplaceString(cyanosis.trim());
	 * masMedicalExamReprt.setCyanosis(cyanosis);
	 * opdPatientDetail.setCyanosis(cyanosis); } if(payload.get("hairNail")!=null) {
	 * String hairNail = payload.get("hairNail").toString(); hairNail
	 * =OpdServiceImpl. getReplaceString(hairNail.trim());
	 * masMedicalExamReprt.setHairNail(hairNail);
	 * opdPatientDetail.setHairNail(hairNail); }
	 * 
	 * if( payload.get("Icterus")!=null) { String icterus =
	 * payload.get("Icterus").toString(); icterus =
	 * OpdServiceImpl.getReplaceString(icterus.trim());
	 * masMedicalExamReprt.setIcterus(icterus);
	 * opdPatientDetail.setIcterus(icterus); }
	 * 
	 * if( payload.get("GCS")!=null) { String gcs = payload.get("GCS").toString();
	 * gcs = OpdServiceImpl.getReplaceString(gcs.trim());
	 * masMedicalExamReprt.setGcs(gcs); opdPatientDetail.setGcs(gcs); } if(
	 * payload.get("lymphNode")!=null) { String lymphNode =
	 * payload.get("lymphNode").toString(); lymphNode =
	 * OpdServiceImpl.getReplaceString(lymphNode.trim());
	 * masMedicalExamReprt.setLymphNode(lymphNode);
	 * opdPatientDetail.setLymphNode(lymphNode);
	 * 
	 * } if( payload.get("Clubbing")!=null) { String clubbing =
	 * payload.get("Clubbing").toString(); clubbing =
	 * OpdServiceImpl.getReplaceString(clubbing.trim());
	 * masMedicalExamReprt.setClubbing(clubbing);
	 * opdPatientDetail.setClubbing(clubbing); } if(payload.get("Tremors")!=null) {
	 * String tremors = payload.get("Tremors").toString(); tremors =
	 * OpdServiceImpl.getReplaceString(tremors.trim());
	 * masMedicalExamReprt.setGeTremors(tremors);
	 * opdPatientDetail.setTremors(tremors); } if( payload.get("Others")!=null) {
	 * String others = payload.get("Others").toString(); others =
	 * OpdServiceImpl.getReplaceString(others.trim());
	 * masMedicalExamReprt.setOthers(others); } if(payload.containsKey("CNS") &&
	 * payload.get("CNS")!=null) { String CNS = payload.get("CNS").toString(); CNS =
	 * OpdServiceImpl.getReplaceString(CNS.trim()); opdPatientDetail.setCns(CNS); }
	 * if(payload.containsKey("Chest")&& payload.get("Chest")!=null) { String Chest
	 * = payload.get("Chest").toString(); Chest =
	 * OpdServiceImpl.getReplaceString(Chest.trim());
	 * opdPatientDetail.setChestResp(Chest); }
	 * 
	 * if(payload.containsKey("Musculoskeletal") &&
	 * payload.get("Musculoskeletal")!=null) { String Musculoskeletal =
	 * payload.get("Musculoskeletal").toString(); Musculoskeletal =
	 * OpdServiceImpl.getReplaceString(Musculoskeletal.trim());
	 * opdPatientDetail.setMusculoskeletal(Musculoskeletal); }
	 * 
	 * 
	 * if(payload.containsKey("CVS") && payload.get("CVS")!=null) { String CVS =
	 * payload.get("CVS").toString(); CVS =
	 * OpdServiceImpl.getReplaceString(CVS.trim()); opdPatientDetail.setCvs(CVS); }
	 * 
	 * 
	 * 
	 * if(payload.containsKey("Skin") && payload.get("Skin")!=null) { String Skin =
	 * payload.get("Skin").toString(); Skin =
	 * OpdServiceImpl.getReplaceString(Skin.trim()); opdPatientDetail.setSkin(Skin);
	 * }
	 * 
	 * 
	 * if(payload.containsKey("GI") && payload.get("GI")!=null) { String GI =
	 * payload.get("GI").toString(); GI =
	 * OpdServiceImpl.getReplaceString(GI.trim()); opdPatientDetail.setGi(GI); }
	 * 
	 * 
	 * 
	 * if(payload.containsKey("geneticurinary") &&
	 * payload.get("geneticurinary")!=null) { String geneticurinary =
	 * payload.get("geneticurinary").toString(); geneticurinary =
	 * OpdServiceImpl.getReplaceString(geneticurinary.trim());
	 * opdPatientDetail.setGenitoUrinary(geneticurinary); }
	 * 
	 * 
	 * if(payload.containsKey("sysOthers") && payload.get("sysOthers")!=null) {
	 * String sysOthers = payload.get("sysOthers").toString(); sysOthers =
	 * OpdServiceImpl.getReplaceString(sysOthers.trim());
	 * opdPatientDetail.setSystemOther(sysOthers); }
	 * 
	 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getHeight()!=null) {
	 * opdPatientDetail.setHeight(masMedicalExamReprt.getHeight().toString()); }
	 * 
	 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getWeight()!=null) {
	 * opdPatientDetail.setWeight(masMedicalExamReprt.getWeight().toString()); }
	 * 
	 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getIdealweight()!=null) {
	 * opdPatientDetail.setIdealWeight(masMedicalExamReprt.getIdealweight().toString
	 * ()); } if(masMedicalExamReprt!=null &&
	 * masMedicalExamReprt.getPulseRates()!=null) {
	 * opdPatientDetail.setPulse(masMedicalExamReprt.getPulseRates().toString()); }
	 * 
	 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getBpSystolic()!=null) {
	 * opdPatientDetail.setBpSystolic(masMedicalExamReprt.getBpSystolic().toString()
	 * ); }
	 * 
	 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getBpDiastolic()!=null) {
	 * opdPatientDetail.setBpDiastolic(masMedicalExamReprt.getBpDiastolic().toString
	 * ()); } String dateOfExam =""; if(payload.containsKey("dateOfExam")) {
	 * dateOfExam = payload.get("dateOfExam").toString(); dateOfExam =
	 * OpdServiceImpl.getReplaceString(dateOfExam); if
	 * (StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("") &&
	 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date dateOfExamValue =
	 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
	 * if(dateOfExamValue!=null) opdPatientDetail.setOpdDate(new
	 * Timestamp(dateOfExamValue.getTime())); }
	 * 
	 * } if(payload.containsKey("dateOfRelease")) { dateOfExam =
	 * payload.get("dateOfRelease").toString(); dateOfExam =
	 * OpdServiceImpl.getReplaceString(dateOfExam); if
	 * (StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("") &&
	 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date dateOfExamValue =
	 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
	 * if(dateOfExamValue!=null) opdPatientDetail.setOpdDate(new
	 * Timestamp(dateOfExamValue.getTime())); }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * //Update Referal Detail
	 * saveOrUpdateReferaMedicalExam(payload,opdPatientDetail,hospitalId); } else {
	 * 
	 * saveUpdateOpdVitalDetails(payload, opdPatientDetail, hospitalId,
	 * masMedicalExamReprt); } } catch(Exception e) { e.printStackTrace(); return
	 * null; } return masMedicalExamReprt; }
	 * 
	 * 
	 *//**
		 * @Description:Method is used for saveOrUpdateReferaMedicalExam
		 * @param payload
		 * @param opdPatientDetail
		 *//*
			 * public void saveOrUpdateReferaMedicalExam(HashMap<String, Object>
			 * payload,OpdPatientDetail opdPatientDetail,String hospitalId) { try { Long
			 * opdPatientDetailId=null; String patientId =
			 * payload.get("patientId").toString(); patientId =
			 * OpdServiceImpl.getReplaceString(patientId);
			 * 
			 * if(StringUtils.isNotEmpty(patientId))
			 * opdPatientDetail.setPatientId(Long.parseLong(patientId));
			 * 
			 * String visitId = payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId); if(StringUtils.isNotEmpty(visitId))
			 * opdPatientDetail.setVisitId(Long.parseLong(visitId));
			 * 
			 * try { if(payload.containsKey("diagnosisId")) { String diagnosisId1 =
			 * payload.get("diagnosisId").toString(); String diagnosisId2 =
			 * OpdServiceImpl.getReplaceString(diagnosisId1);
			 * if(StringUtils.isNotEmpty(diagnosisId2)) {
			 * opdPatientDetail.setIcdDiagnosis(diagnosisId1); } } } catch(Exception e) {
			 * e.printStackTrace(); } opdPatientDetailId=
			 * updateOpdPatientDetailMe(opdPatientDetail);
			 * 
			 * String referHospitalList = payload.get("medicalExamReferalHos").toString();
			 * referHospitalList = OpdServiceImpl.getReplaceString(referHospitalList);
			 * String referHospitalValues = ""; if
			 * (StringUtils.isNotBlank(referHospitalList)) { if
			 * (referHospitalList.contains("@")) { String[] referHospitalListValue =
			 * referHospitalList.split(","); int count = 0; for (int i = count; i <
			 * referHospitalListValue.length; i++) { String[] referHospitalListValueNew =
			 * referHospitalListValue[i].split("@"); referHospitalValues +=
			 * referHospitalListValueNew[0] + ","; // count+=2; } } referHospitalList =
			 * referHospitalValues; }
			 * 
			 * String departmentValue = payload.get("departmentValue").toString();
			 * departmentValue = OpdServiceImpl.getReplaceString(departmentValue);
			 * 
			 * String diagonsisIdValue = payload.get("diagonsisId").toString();
			 * diagonsisIdValue = OpdServiceImpl.getReplaceString(diagonsisIdValue);
			 * 
			 * String instruction = payload.get("instruction").toString(); instruction =
			 * OpdServiceImpl.getReplaceString(instruction);
			 * 
			 * String referalPatientDt = payload.get("referalPatientDt").toString();
			 * referalPatientDt = OpdServiceImpl.getReplaceString(referalPatientDt);
			 * 
			 * String referalPatientHd = payload.get("referalPatientHd").toString();
			 * referalPatientHd = OpdServiceImpl.getReplaceString(referalPatientHd);
			 * 
			 * saveOrUpdateReferrPatient(referHospitalList,departmentValue,diagonsisIdValue,
			 * instruction, referalPatientDt, referalPatientHd,
			 * opdPatientDetailId.toString(), patientId,null,null, payload,hospitalId);
			 * /////////////////Discharge Icg////////////////
			 * saveOrUpdateDischargeIcg(payload, visitId, opdPatientDetailId, patientId); }
			 * catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * // Saveupdadate Referal candidate public void
			 * saveOrUpdateReferrPatient(String referHospitalList, String departmentValue,
			 * String diagonsisId, String hos, String referalPatientDtValue, String
			 * referalPatientHdValue, String opdPatientDetailsId, String patient,String
			 * referalNotes,String referalDates,HashMap<String, Object> payload,String
			 * hospitalId) { String referToValue=""; try {
			 * 
			 * String[] referHospitalListValue = referHospitalList.split(","); String[]
			 * departmentValueArray = departmentValue.split(","); String[] diagonsisIdValue
			 * = diagonsisId.split(","); String[] hosValue = hos.split(",");
			 * 
			 * String[] referalPatientDtValueArray = referalPatientDtValue.split(",");
			 * String[] referalPatientHdValueArray = referalPatientHdValue.split(",");
			 * HashMap<String, String> mapInvestigationMap = new HashMap<>(); //dsadasdsa
			 * 
			 * String[] specialistOpinionArrays=null;
			 * if(payload.containsKey("specialistOpinion")) { String specialistOpinion =
			 * payload.get("specialistOpinion").toString(); specialistOpinion =
			 * OpdServiceImpl.getReplaceString(specialistOpinion); specialistOpinionArrays =
			 * specialistOpinion.split("@@@###"); }
			 * 
			 * if(payload.containsKey("referralNote")) { referalNotes =
			 * payload.get("referralNote").toString(); referalNotes =
			 * OpdServiceImpl.getReplaceString(referalNotes);
			 * 
			 * }
			 * 
			 * 
			 * String referalRmsId = payload.get("referalRmsId").toString(); referalRmsId =
			 * OpdServiceImpl.getReplaceString(referalRmsId); String[]
			 * referalRmsIdValueArrays = referalRmsId.split(",");
			 * 
			 * String totalLengthDigiFileRefer =
			 * payload.get("totalLengthDigiFileRefer").toString(); totalLengthDigiFileRefer
			 * = OpdServiceImpl.getReplaceString(totalLengthDigiFileRefer); String
			 * fileUploadFinalVal=""; if(StringUtils.isNotEmpty(totalLengthDigiFileRefer)&&
			 * !totalLengthDigiFileRefer.equalsIgnoreCase("0")) { Integer
			 * totalLengthDigiFilev=Integer.parseInt(totalLengthDigiFileRefer); for(int
			 * i=0;i<totalLengthDigiFilev;i++) { fileUploadFinalVal+="0"+","; }
			 * 
			 * } if(referalRmsIdValueArrays!=null) { for(String ss:referalRmsIdValueArrays)
			 * { fileUploadFinalVal+=ss+","; } }
			 * 
			 * String[] referalRmsIdValueArray = fileUploadFinalVal.split(",");
			 * 
			 * String finalValue = ""; Integer counter = 1; // String hospitalId=""; String
			 * userId=""; try { //hospitalId = payload.get("hospitalId").toString();
			 * //hospitalId = OpdServiceImpl.getReplaceString(hospitalId);
			 * 
			 * userId = payload.get("userId").toString(); userId
			 * =OpdServiceImpl.getReplaceString(userId); } catch(Exception e) {
			 * e.printStackTrace(); }
			 * 
			 * for (int i = 0; i < referHospitalListValue.length; i++) {
			 * 
			 * if (StringUtils.isNotEmpty(referHospitalListValue[i].toString()) &&
			 * !referHospitalListValue[i].equalsIgnoreCase("0")) { finalValue +=
			 * referHospitalListValue[i].trim(); if
			 * (!referalPatientDtValueArray[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(referalPatientDtValueArray[i])) { for (int m = i; m <
			 * referalPatientDtValueArray.length; m++) { finalValue += "###" +
			 * referalPatientDtValueArray[m].trim(); if (m == i) { break; } } } else {
			 * finalValue += "###" + "0"; }
			 * 
			 * if (!departmentValueArray[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(departmentValueArray[i])) { for (int j = i; j <
			 * departmentValueArray.length; j++) { finalValue += "###" +
			 * departmentValueArray[j].trim(); if (j == i) { break; } } } else { finalValue
			 * += "###" + "0"; } if (i < diagonsisIdValue.length &&
			 * StringUtils.isNotBlank(diagonsisIdValue[i])) { for (int k = i; k <
			 * diagonsisIdValue.length; k++) { finalValue += "###" +
			 * diagonsisIdValue[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; } if (i < hosValue.length &&
			 * StringUtils.isNotBlank(hosValue[i])) { for (int l = i; l < hosValue.length;
			 * l++) { finalValue += "###" + hosValue[l].trim(); if (l == i) { break; } } }
			 * else { finalValue += "###" + "0"; }
			 * 
			 * int tempI=0; if(payload.containsKey("specialistOpinion") &&
			 * specialistOpinionArrays.length>0) { tempI=i; i++; if (i <
			 * specialistOpinionArrays.length &&
			 * StringUtils.isNotBlank(specialistOpinionArrays[i])) { for (int k = i; k <
			 * specialistOpinionArrays.length; k++) { finalValue += "###" +
			 * specialistOpinionArrays[k].trim(); if (k == i) { i=tempI; tempI=0; break; } }
			 * } else { i=tempI; tempI=0; finalValue += "###" + "0"; } } else { i=tempI;
			 * tempI=0; finalValue += "###" + "0"; }
			 * 
			 * 
			 * 
			 * if (i < referalRmsIdValueArray.length &&
			 * StringUtils.isNotBlank(referalRmsIdValueArray[i])) { for (int l = i; l <
			 * referalRmsIdValueArray.length; l++) { finalValue += "###" +
			 * referalRmsIdValueArray[l].trim(); if (l == i) { break; } } } else {
			 * finalValue += "###" + "0"; }
			 * 
			 * 
			 * mapInvestigationMap.put(referHospitalListValue[i].trim() + "@#" + counter,
			 * finalValue); finalValue = ""; counter++; } } counter = 1; Date date=new
			 * Date(); for (String referHospitalId : referHospitalListValue) { if
			 * (StringUtils.isNotEmpty(referHospitalId)) { if
			 * (mapInvestigationMap.containsKey(referHospitalId.trim() + "@#" + counter)) {
			 * String referHospitalIdValue = mapInvestigationMap.get(referHospitalId.trim()
			 * + "@#" + counter);
			 * 
			 * if (StringUtils.isNotEmpty(referHospitalIdValue)) {
			 * 
			 * String[] finalValueReferal = referHospitalIdValue.split("###");
			 * ReferralPatientDt referralPatientDt = null; if (finalValueReferal[1] != null
			 * && !finalValueReferal[1].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(finalValueReferal[1])) { referralPatientDt =
			 * getReferralPatientDtByReferaldtIdMe(
			 * Long.parseLong(finalValueReferal[1].toString())); } else { referralPatientDt
			 * = new ReferralPatientDt(); if (referalPatientHdValueArray != null &&
			 * StringUtils.isNotBlank(referalPatientHdValueArray[0])) { referralPatientDt
			 * .setRefrealHdId(Long.parseLong(referalPatientHdValueArray[0].toString())); }
			 * 
			 * }
			 * 
			 * if (finalValueReferal != null) { referToValue=
			 * payload.get("referTo").toString(); referToValue=
			 * OpdServiceImpl.getReplaceString(referToValue); if(
			 * StringUtils.isNotEmpty(referToValue) && referToValue.equalsIgnoreCase("I")) {
			 * 
			 * if (StringUtils.isNotEmpty(finalValueReferal[2]) &&
			 * !finalValueReferal[2].equals("0")) {
			 * referralPatientDt.setIntDepartmentId(Long.parseLong(finalValueReferal[2].
			 * toString())); } } else { if (StringUtils.isNotEmpty(finalValueReferal[2]) &&
			 * !finalValueReferal[2].equals("0")) {
			 * referralPatientDt.setExtDepartment(finalValueReferal[2]); } } if
			 * (finalValueReferal[3] != null && StringUtils.isNotBlank(finalValueReferal[3])
			 * && !finalValueReferal[3].equals("0"))
			 * referralPatientDt.setDiagnosisId(Long.parseLong(finalValueReferal[3].toString
			 * ()));
			 * 
			 * if (finalValueReferal[4] != null &&
			 * StringUtils.isNotBlank(finalValueReferal[4]) &&
			 * !finalValueReferal[4].equals("0"))
			 * referralPatientDt.setInstruction(finalValueReferal[4].toString());
			 * 
			 * if (finalValueReferal[0] != null &&
			 * StringUtils.isNotBlank(finalValueReferal[0]) &&
			 * !finalValueReferal[0].equals("0")) {
			 * 
			 * ReferralPatientHd referralPatientHd = getPatientReferalHdByExtHospitalIdMe(
			 * Long.parseLong(finalValueReferal[0].trim()),
			 * Long.parseLong(opdPatientDetailsId.trim()),referToValue);
			 * 
			 * if (referralPatientHd != null) {
			 * 
			 * if(StringUtils.isNotEmpty(referToValue) &&
			 * referToValue.equalsIgnoreCase("I")) {
			 * referralPatientHd.setIntHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); } else {
			 * referralPatientHd.setExtHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); } } else { referralPatientHd = new ReferralPatientHd();
			 * 
			 * if(StringUtils.isNotEmpty(referToValue) &&
			 * referToValue.equalsIgnoreCase("I")) { if(finalValueReferal[0]!=null &&
			 * finalValueReferal[0]!="")
			 * referralPatientHd.setIntHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); } else { if(finalValueReferal[0]!=null && finalValueReferal[0]!="")
			 * referralPatientHd.setExtHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); }
			 * 
			 * if (payload.containsKey("specialistOpinion") && finalValueReferal[5] != null
			 * && StringUtils.isNotBlank(finalValueReferal[5]) &&
			 * !finalValueReferal[5].equals("0")) { String result = ""; int
			 * index=finalValueReferal[5].lastIndexOf(","); if(index==-1) { result =
			 * getHtmlText(finalValueReferal[5].toString());
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } else {
			 * result = finalValueReferal[5].toString().substring(0,
			 * finalValueReferal[5].toString().length() - 1); result = getHtmlText(result);
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } } else
			 * { referralPatientHd.setStatus("W"); } if(StringUtils.isNotBlank(hospitalId))
			 * { Long hospitalIdValue=Long.parseLong(hospitalId);
			 * referralPatientHd.setHospitalId(hospitalIdValue); }
			 * 
			 * }
			 * 
			 * if(StringUtils.isNotEmpty(referalNotes)) {
			 * referralPatientHd.setReferralNote(referalNotes);
			 * 
			 * } //if(StringUtils.isNotBlank(referalDates)) { //Date referalDate =
			 * HMSUtil.convertStringDateToUtilDate(new Date(), "yyyy-MM-dd");
			 * referralPatientHd.setReferralIniDate(new Date()); //}
			 * 
			 * if (payload.containsKey("specialistOpinion") && finalValueReferal[5] != null
			 * && StringUtils.isNotBlank(finalValueReferal[5]) &&
			 * !finalValueReferal[5].equals("0")) { String result = ""; int
			 * index=finalValueReferal[5].lastIndexOf(","); if(index==-1) { result =
			 * getHtmlText(finalValueReferal[5].toString());
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } else {
			 * result = finalValueReferal[5].toString().substring(0,
			 * finalValueReferal[5].toString().length() - 1); result = getHtmlText(result);
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } }
			 * referralPatientHd.setOpdPatientDetailsId(Long.parseLong(opdPatientDetailsId))
			 * ; referralPatientHd.setPatientId(Long.parseLong(patient));
			 * referralPatientHd.setTreatmentType("E");
			 * referralPatientHd.setLastChgBy(Long.parseLong(userId));
			 * referralPatientHd.setDoctorId(Long.parseLong(userId));
			 * referralPatientHd.setLastChgDate(new Timestamp(date.getTime())); Long
			 * referalPatientHdId = saveOrUpdateReferalHdMe(referralPatientHd);
			 * referralPatientDt.setRefrealHdId(referalPatientHdId); }
			 * 
			 * }
			 * 
			 * if (payload.containsKey("specialistOpinion") && finalValueReferal[5] != null
			 * && StringUtils.isNotBlank(finalValueReferal[5]) &&
			 * !finalValueReferal[5].equals("0")) { String result = ""; int
			 * index=finalValueReferal[5].lastIndexOf(","); if(index==-1) { result =
			 * getHtmlText(finalValueReferal[5].toString());
			 * referralPatientDt.setFinalNote(result);
			 * 
			 * } else { result = finalValueReferal[5].toString().substring(0,
			 * finalValueReferal[5].toString().length() - 1); result = getHtmlText(result);
			 * referralPatientDt.setFinalNote(result);
			 * 
			 * } }
			 * 
			 * 
			 * if (finalValueReferal[6] != null &&
			 * StringUtils.isNotBlank(finalValueReferal[6]) &&
			 * !finalValueReferal[6].equals("0"))
			 * referralPatientDt.setRidcId(Long.parseLong(finalValueReferal[6].toString()));
			 * 
			 * 
			 * referralPatientDt.setLastChgDate(new Timestamp(date.getTime()));
			 * saveOrUpdateReferalDtMe(referralPatientDt); } } } counter++; }
			 * 
			 * } catch (Exception e) { e.printStackTrace(); } }
			 * 
			 * 
			 * 
			 * 
			 * 
			 * @Transactional public void
			 * saveUpdatePatientImmunizationHistory(HashMap<String, Object> payload) { //try
			 * {
			 * 
			 * String saveInDraft =""; if(payload.get("saveInDraft")!=null) { saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft); }
			 * 
			 * String pvmsNumber =""; if(payload.get("pvmsNumber")!=null) { pvmsNumber =
			 * payload.get("pvmsNumber").toString(); pvmsNumber =
			 * OpdServiceImpl.getReplaceString(pvmsNumber); }
			 * 
			 * 
			 * String immunisationId =""; if(payload.get("immunisationId")!=null) {
			 * immunisationId = payload.get("immunisationId").toString(); immunisationId =
			 * OpdServiceImpl.getReplaceString(immunisationId); } String immnunizationDate =
			 * ""; if(payload.get("immnunizationDate")!=null) { immnunizationDate =
			 * payload.get("immnunizationDate").toString(); immnunizationDate =
			 * OpdServiceImpl.getReplaceString(immnunizationDate); } String durationImmu
			 * =""; if(payload.get("durationImmu")!=null) { durationImmu =
			 * payload.get("durationImmu").toString(); durationImmu =
			 * OpdServiceImpl.getReplaceString(durationImmu); } String nextDueImmu = "";
			 * if(payload.get("nextDueImmu")!=null) { nextDueImmu =
			 * payload.get("nextDueImmu").toString(); nextDueImmu =
			 * OpdServiceImpl.getReplaceString(nextDueImmu); } String visitId = "";
			 * if(payload.get("visitId")!=null) { visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId); } String userId ="";
			 * if(payload.get("userId")!=null) { userId = payload.get("userId").toString();
			 * userId = OpdServiceImpl.getReplaceString(userId); } String patientId ="";
			 * if(payload.get("patientId")!=null) { patientId =
			 * payload.get("patientId").toString(); patientId =
			 * OpdServiceImpl.getReplaceString(patientId); } String []
			 * immunisationIdValues=immunisationId.split(",");
			 * 
			 * String []immnunizationDateValue=immnunizationDate.split(",");
			 * 
			 * 
			 * String [] durationImmuValues=durationImmu.split(",");
			 * 
			 * String []nextDueImmuValue=nextDueImmu.split(",");
			 * 
			 * String []pvmsNumberValue=pvmsNumber.split(","); PatientImmunizationHistory
			 * patientImmunizationHistory=null; int count=0; String pvmsNoFixedVal =
			 * UtilityServices.getValueOfPropByKey("pvmsNo"); String[] pvmsNoFixedValues =
			 * null; if (StringUtils.isNotEmpty(pvmsNoFixedVal)) { pvmsNoFixedValues =
			 * pvmsNoFixedVal.split(","); } if(immunisationIdValues!=null) for(String
			 * immunizationId:immunisationIdValues) { Date immunizationDate =null;
			 * if(immnunizationDateValue!=null && immnunizationDateValue.length>0 &&
			 * immnunizationDateValue[count]!=null &&
			 * StringUtils.isNotEmpty(immnunizationDateValue[count].trim()))
			 * if(MedicalExamServiceImpl.checkDateFormat(immnunizationDateValue[count]))
			 * immunizationDate =
			 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[count].trim())
			 * ; //patientImmunizationHistory=
			 * getPatientImmunizationHistory(Long.parseLong(visitId.trim()),Long.parseLong(
			 * immunizationId.trim())); if(StringUtils.isNotEmpty(immunizationId)) {
			 * patientImmunizationHistory=
			 * getPatientImmunizationHistoryByDate(Long.parseLong(visitId.trim()),Long.
			 * parseLong(immunizationId.trim()),Long.parseLong(patientId),immunizationDate,
			 * "dateNotNull") ;
			 * 
			 * if(patientImmunizationHistory==null && immunizationDate==null) {
			 * patientImmunizationHistory=
			 * getPatientImmunizationHistory(Long.parseLong(visitId.trim()),Long.parseLong(
			 * immunizationId.trim())); } if(StringUtils.isNotEmpty(saveInDraft)&&
			 * saveInDraft.contains("draft")) { if(patientImmunizationHistory==null &&
			 * pvmsNumberValue!=null && pvmsNumberValue.length>0 &&
			 * pvmsNumberValue[count]!=null &&
			 * (pvmsNumberValue[count].trim().equalsIgnoreCase(pvmsNoFixedValues[0].trim())
			 * ||
			 * pvmsNumberValue[count].trim().equalsIgnoreCase(pvmsNoFixedValues[1].trim())))
			 * { patientImmunizationHistory=
			 * getPatientImmunizationHistoryByDate(Long.parseLong(visitId.trim()),Long.
			 * parseLong(immunizationId.trim()),Long.parseLong(patientId),immunizationDate,
			 * "dateNull") ; } } if(immunizationDate!=null) {
			 * if(patientImmunizationHistory==null) { patientImmunizationHistory=new
			 * PatientImmunizationHistory();
			 * patientImmunizationHistory.setVisitId(Long.parseLong(visitId.trim()));
			 * patientImmunizationHistory.setPatientId(Long.parseLong(patientId)); Date
			 * date=new Date(); patientImmunizationHistory.setLastChgDate(new
			 * Timestamp(date.getTime()));
			 * patientImmunizationHistory.setLastChgBy(Long.parseLong(userId));
			 * 
			 * }
			 * patientImmunizationHistory.setItemId(Long.parseLong(immunizationId.trim()));
			 * 
			 * patientImmunizationHistory.setLastChgBy(Long.parseLong(userId.trim()));
			 * if(immnunizationDateValue!=null && immnunizationDateValue.length>0 &&
			 * immnunizationDateValue[count]!=null &&
			 * StringUtils.isNotEmpty(immnunizationDateValue[count].trim())) {
			 * if(MedicalExamServiceImpl.checkDateFormat(immnunizationDateValue[count].trim(
			 * ))) immunizationDate =
			 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[count].trim())
			 * ; if(immunizationDate!=null)
			 * patientImmunizationHistory.setImmunizationDate(immunizationDate); }
			 * 
			 * if(durationImmuValues[count]!=null) {
			 * patientImmunizationHistory.setDuration(durationImmuValues[count].trim()); }
			 * 
			 * if(nextDueImmuValue!=null && nextDueImmuValue.length>0 &&
			 * nextDueImmuValue[count]!=null &&
			 * StringUtils.isNotEmpty(nextDueImmuValue[count].trim())) { Date
			 * nextDueImmuValueDate =null; if(
			 * MedicalExamServiceImpl.checkDateFormat(nextDueImmuValue[count]))
			 * nextDueImmuValueDate =
			 * HMSUtil.convertStringTypeDateToDateType(nextDueImmuValue[count].trim());
			 * if(nextDueImmuValueDate!=null)
			 * patientImmunizationHistory.setNextDueDate(nextDueImmuValueDate); }
			 * 
			 * 
			 * 
			 * if(immnunizationDateValue!=null && immnunizationDateValue.length>0 &&
			 * immnunizationDateValue[count]!=null &&
			 * StringUtils.isNotEmpty(immnunizationDateValue[count].trim()) &&
			 * patientImmunizationHistory.getPrescriptionDate()==null) {
			 * if(MedicalExamServiceImpl.checkDateFormat(immnunizationDateValue[count]))
			 * immunizationDate =
			 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[count].trim())
			 * ; if(immunizationDate!=null)
			 * patientImmunizationHistory.setPrescriptionDate(immunizationDate); }
			 * saveOrUpdatePatientImmunizationHistory(patientImmunizationHistory); } }
			 * count++; }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * @Transactional public void
			 * saveUpdatePatientImmunizationHistoryDigi(HashMap<String, Object> payload) {
			 * //try {
			 * 
			 * String saveInDraft =""; if(payload.get("saveInDraft")!=null) { saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft); }
			 * 
			 * String pvmsNumber =""; if(payload.get("pvmsNumber")!=null) { pvmsNumber =
			 * payload.get("pvmsNumber").toString(); pvmsNumber =
			 * OpdServiceImpl.getReplaceString(pvmsNumber); }
			 * 
			 * 
			 * String immunisationId =""; if(payload.get("immunisationId")!=null) {
			 * immunisationId = payload.get("immunisationId").toString(); immunisationId =
			 * OpdServiceImpl.getReplaceString(immunisationId); } String immnunizationDate =
			 * ""; if(payload.get("immnunizationDate")!=null) { immnunizationDate =
			 * payload.get("immnunizationDate").toString(); immnunizationDate =
			 * OpdServiceImpl.getReplaceString(immnunizationDate); } String durationImmu
			 * =""; if(payload.get("durationImmu")!=null) { durationImmu =
			 * payload.get("durationImmu").toString(); durationImmu =
			 * OpdServiceImpl.getReplaceString(durationImmu); } String nextDueImmu = "";
			 * if(payload.get("nextDueImmu")!=null) { nextDueImmu =
			 * payload.get("nextDueImmu").toString(); nextDueImmu =
			 * OpdServiceImpl.getReplaceString(nextDueImmu); } String visitId = "";
			 * if(payload.get("visitId")!=null) { visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId); } String userId ="";
			 * if(payload.get("userId")!=null) { userId = payload.get("userId").toString();
			 * userId = OpdServiceImpl.getReplaceString(userId); } String patientId ="";
			 * if(payload.get("patientId")!=null) { patientId =
			 * payload.get("patientId").toString(); patientId =
			 * OpdServiceImpl.getReplaceString(patientId); } String []
			 * immunisationIdValues=immunisationId.split(",");
			 * 
			 * String []immnunizationDateValue=immnunizationDate.split(",");
			 * 
			 * 
			 * String [] durationImmuValues=durationImmu.split(",");
			 * 
			 * String []nextDueImmuValue=nextDueImmu.split(",");
			 * 
			 * String []pvmsNumberValue=pvmsNumber.split(","); PatientImmunizationHistory
			 * patientImmunizationHistory=null; int count=0; String pvmsNoFixedVal =
			 * UtilityServices.getValueOfPropByKey("pvmsNo"); String[] pvmsNoFixedValues =
			 * null; if (StringUtils.isNotEmpty(pvmsNoFixedVal)) { pvmsNoFixedValues =
			 * pvmsNoFixedVal.split(","); } if(immunisationIdValues!=null) for(String
			 * immunizationId:immunisationIdValues) { Date immunizationDate =null;
			 * if(immnunizationDateValue!=null && immnunizationDateValue.length>0 &&
			 * immnunizationDateValue[count]!=null &&
			 * StringUtils.isNotEmpty(immnunizationDateValue[count].trim())) if(
			 * MedicalExamServiceImpl.checkDateFormat(immnunizationDateValue[count]))
			 * immunizationDate =
			 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[count].trim())
			 * ; //patientImmunizationHistory=
			 * getPatientImmunizationHistory(Long.parseLong(visitId.trim()),Long.parseLong(
			 * immunizationId.trim())); if(StringUtils.isNotEmpty(immunizationId) &&
			 * !immunizationId.trim().equalsIgnoreCase("")) { patientImmunizationHistory=
			 * getPatientImmunizationHistoryByDateDigi(Long.parseLong(visitId.trim()),Long.
			 * parseLong(immunizationId.trim()),Long.parseLong(patientId),immunizationDate,
			 * "dateNotNull") ;
			 * 
			 * if(patientImmunizationHistory==null && immunizationDate==null) {
			 * patientImmunizationHistory=
			 * getPatientImmunizationHistory(Long.parseLong(visitId.trim()),Long.parseLong(
			 * immunizationId.trim())); } if(StringUtils.isNotEmpty(saveInDraft)&&
			 * saveInDraft.contains("draft")) { if(patientImmunizationHistory==null &&
			 * pvmsNumberValue!=null && pvmsNumberValue.length>0 &&
			 * pvmsNumberValue[count]!=null &&
			 * (pvmsNumberValue[count].trim().equalsIgnoreCase(pvmsNoFixedValues[0].trim())
			 * ||
			 * pvmsNumberValue[count].trim().equalsIgnoreCase(pvmsNoFixedValues[1].trim())))
			 * { patientImmunizationHistory=
			 * getPatientImmunizationHistoryByDateDigi(Long.parseLong(visitId.trim()),Long.
			 * parseLong(immunizationId.trim()),Long.parseLong(patientId),immunizationDate,
			 * "dateNull") ; } } if(immunizationDate!=null) {
			 * if(patientImmunizationHistory==null) { patientImmunizationHistory=new
			 * PatientImmunizationHistory();
			 * patientImmunizationHistory.setVisitId(Long.parseLong(visitId.trim()));
			 * patientImmunizationHistory.setPatientId(Long.parseLong(patientId)); Date
			 * date=new Date(); patientImmunizationHistory.setLastChgDate(new
			 * Timestamp(date.getTime()));
			 * patientImmunizationHistory.setLastChgBy(Long.parseLong(userId));
			 * 
			 * }
			 * patientImmunizationHistory.setItemId(Long.parseLong(immunizationId.trim()));
			 * 
			 * patientImmunizationHistory.setLastChgBy(Long.parseLong(userId.trim())); try {
			 * if(immnunizationDateValue!=null && immnunizationDateValue.length>0 &&
			 * immnunizationDateValue[count]!=null &&
			 * StringUtils.isNotEmpty(immnunizationDateValue[count].trim())) {
			 * System.out.println("immuniza tion date1>>>>"+immnunizationDateValue[count]);
			 * if(MedicalExamServiceImpl.checkDateFormat(immnunizationDateValue[count]))
			 * immunizationDate =
			 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[count].
			 * toString().trim()); if(immunizationDate!=null) {
			 * patientImmunizationHistory.setImmunizationDate(immunizationDate);
			 * System.out.println("immuniza tion date"+immunizationDate); } } }
			 * catch(Exception e) {e.printStackTrace();}
			 * 
			 * if(durationImmuValues[count]!=null) {
			 * patientImmunizationHistory.setDuration(durationImmuValues[count].trim()); }
			 * 
			 * try { if(nextDueImmuValue!=null && nextDueImmuValue.length>0 &&
			 * nextDueImmuValue[count]!=null &&
			 * StringUtils.isNotEmpty(nextDueImmuValue[count].trim())) { Date
			 * nextDueImmuValueDate =null;
			 * System.out.println("Next immuniza tion date1>>>>"+nextDueImmuValue[count]);
			 * if(MedicalExamServiceImpl.checkDateFormat(nextDueImmuValue[count]))
			 * nextDueImmuValueDate =
			 * HMSUtil.convertStringTypeDateToDateType(nextDueImmuValue[count].toString().
			 * trim()); if(nextDueImmuValueDate!=null) {
			 * patientImmunizationHistory.setNextDueDate(nextDueImmuValueDate);
			 * System.out.println("Next immuniza tion date:::::"+nextDueImmuValueDate); } }
			 * } catch(Exception e) {e.printStackTrace();}
			 * 
			 * try { if(immnunizationDateValue!=null && immnunizationDateValue.length>0 &&
			 * immnunizationDateValue[count]!=null &&
			 * StringUtils.isNotEmpty(immnunizationDateValue[count].trim()) &&
			 * patientImmunizationHistory.getPrescriptionDate()==null) {
			 * System.out.println("Pres immuniza tion date1>>>>"+immnunizationDateValue[
			 * count]);
			 * if(MedicalExamServiceImpl.checkDateFormat(immnunizationDateValue[count].trim(
			 * ))) immunizationDate =
			 * HMSUtil.convertStringTypeDateToDateType(immnunizationDateValue[count].
			 * toString().trim()); if(immunizationDate!=null) {
			 * patientImmunizationHistory.setPrescriptionDate(immunizationDate);
			 * System.out.println("Pres immuniza tion date"+immunizationDate); } } }
			 * catch(Exception e) {e.printStackTrace();}
			 * saveOrUpdatePatientImmunizationHistory(patientImmunizationHistory); } }
			 * count++; }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public MasMedicalExamReport
			 * updateInvestigationDgResult(HashMap<String, Object> payload,String patientId,
			 * String hospitalId,String userId,MasMedicalExamReport masMedicalExamReport) {
			 * 
			 * deInitGlobalValue(); String saveInDraft ="";
			 * if(payload.containsKey("saveInDraft")) { saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft); } String actionMe ="";
			 * if(StringUtils.isNotBlank(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("draftMa")) {
			 * if(payload.containsKey("actionMe")) { actionMe =
			 * payload.get("actionMe").toString(); actionMe =
			 * OpdServiceImpl.getReplaceString(actionMe); } }
			 * 
			 * String visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(visitId);
			 * 
			 * String chargeCodeName=payload.get("chargeCodeName").toString();
			 * chargeCodeName=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(chargeCodeName
			 * ); String[] chargeCodeNames = chargeCodeName.split(",");
			 * 
			 * String investigationIdValue=payload.get("investigationIdValue").toString();
			 * investigationIdValue=OpdServiceImpl.getReplaceString(investigationIdValue);
			 * String[] investigationIdValues = investigationIdValue.split(",");
			 * 
			 * String resultInvs=payload.get("resultInvs").toString();
			 * resultInvs=OpdServiceImpl.getReplaceString(resultInvs); String[] resultInvss
			 * = resultInvs.split("@@@###");
			 * 
			 * 
			 * String dgOrderDtIdValue=payload.get("dgOrderDtIdValue").toString();
			 * dgOrderDtIdValue=OpdServiceImpl.getReplaceString(dgOrderDtIdValue); String[]
			 * dgOrderDtIdValues = dgOrderDtIdValue.split(",");
			 * 
			 * 
			 * String dgOrderHdId=payload.get("dgOrderHdId").toString();
			 * dgOrderHdId=OpdServiceImpl.getReplaceString(dgOrderHdId); String[]
			 * dgOrderHdIds = dgOrderHdId.split(",");
			 * 
			 * String dgResultDtId=payload.get("dgResultDtId").toString();
			 * dgResultDtId=OpdServiceImpl.getReplaceString(dgResultDtId); String[]
			 * dgResultDtIds = dgResultDtId.split(",");
			 * 
			 * 
			 * String dgResultHdId=payload.get("dgResultHdId").toString();
			 * dgResultHdId=OpdServiceImpl.getReplaceString(dgResultHdId); String[]
			 * dgResultHdIds = dgResultHdId.split(",");
			 * 
			 * 
			 * String
			 * dynamicUploadInvesIdAndfile=payload.get("dynamicUploadInvesIdAndfileNew").
			 * toString(); dynamicUploadInvesIdAndfile=OpdServiceImpl.getReplaceString(
			 * dynamicUploadInvesIdAndfile);
			 * dynamicUploadInvesIdAndfile=dynamicUploadInvesIdAndfile.replaceAll("\"", "");
			 * String[] dynamicUploadInvesIdAndfiles =
			 * dynamicUploadInvesIdAndfile.split(",");
			 * 
			 * String totalLengthDigiFile=payload.get("totalLengthDigiFile").toString();
			 * totalLengthDigiFile=OpdServiceImpl.getReplaceString(totalLengthDigiFile);
			 * String rmdIdVal=patientRegistrationServiceImpl.getFinalInvestigationValue(
			 * investigationIdValues, dynamicUploadInvesIdAndfiles,totalLengthDigiFile) ;
			 * 
			 * String[] rangeValues=null; if(payload.containsKey("range")) { String
			 * rangeValue=payload.get("range").toString();
			 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
			 * rangeValue.split(","); }
			 * 
			 * String[] rmsIdFinalValfileUp=null; rmsIdFinalValfileUp = rmdIdVal.split(",");
			 * 
			 * String[] investigationRemarksValues =null;
			 * if(payload.containsKey("investigationRemarks")) { String investigationRemarks
			 * = payload.get("investigationRemarks").toString(); investigationRemarks =
			 * OpdServiceImpl.getReplaceString(investigationRemarks);
			 * investigationRemarksValues = investigationRemarks.split(","); } String
			 * otherInvestigation =""; if(payload.containsKey("otherInvestigation")) {
			 * otherInvestigation = payload.get("otherInvestigation").toString();
			 * otherInvestigation = OpdServiceImpl.getReplaceString(otherInvestigation); }
			 * 
			 * 
			 * 
			 * /////////////////////////////////////////////////////////////////////////////
			 * ///////////////////////// String[] investigationTypes=null;
			 * if(payload.containsKey("investigationType")) {
			 * 
			 * String investigationType=payload.get("investigationType").toString();
			 * investigationType=OpdServiceImpl.getReplaceString(investigationType);
			 * investigationTypes=investigationType.split(",");
			 * 
			 * } String[] resultSubInvs=null; if(payload.containsKey("resultSubInv")) {
			 * String resultSubInv=payload.get("resultSubInv").toString();
			 * resultSubInv=OpdServiceImpl.getReplaceString(resultSubInv);
			 * resultSubInvs=resultSubInv.split("@@@###"); }
			 * 
			 * String[] rangeSubInvess=null; if(payload.containsKey("rangeSubInves")) {
			 * String rangeSubInves=payload.get("rangeSubInves").toString();
			 * rangeSubInves=OpdServiceImpl.getReplaceString(rangeSubInves);
			 * rangeSubInvess=rangeSubInves.split(","); }
			 * 
			 * String[] subInvestigationNameIdAndInvs=null;
			 * if(payload.containsKey("subInvestigationNameIdAndInv")) { String
			 * subInvestigationNameIdAndInv=payload.get("subInvestigationNameIdAndInv").
			 * toString(); subInvestigationNameIdAndInv=OpdServiceImpl.getReplaceString(
			 * subInvestigationNameIdAndInv);
			 * subInvestigationNameIdAndInvs=subInvestigationNameIdAndInv.split(",");
			 * subInvestigationNameIdAndInvsTemp=subInvestigationNameIdAndInv.split(","); }
			 * 
			 * String[] dgOrderDtIdValueSubInvess=null;
			 * if(payload.containsKey("dgOrderDtIdValueSubInves")) { String
			 * dgOrderDtIdValueSubInves=payload.get("dgOrderDtIdValueSubInves").toString();
			 * dgOrderDtIdValueSubInves=OpdServiceImpl.getReplaceString(
			 * dgOrderDtIdValueSubInves);
			 * dgOrderDtIdValueSubInvess=dgOrderDtIdValueSubInves.split(","); }
			 * 
			 * String[] dgOrderHdIdSubInvess=null;
			 * if(payload.containsKey("dgOrderHdIdSubInves")) { String
			 * dgOrderHdIdSubInves=payload.get("dgOrderHdIdSubInves").toString();
			 * dgOrderHdIdSubInves=OpdServiceImpl.getReplaceString(dgOrderHdIdSubInves);
			 * dgOrderHdIdSubInvess=dgOrderHdIdSubInves.split(","); }
			 * 
			 * String[] UOMSubss=null; if(payload.containsKey("UOMSub")) { String
			 * UOMSub=payload.get("UOMSub").toString();
			 * UOMSub=OpdServiceImpl.getReplaceString(UOMSub); UOMSubss=UOMSub.split(","); }
			 * 
			 * String[] subChargecodeIds=null;
			 * if(payload.containsKey("subChargecodeIdForInv")) { String
			 * subChargecodeId=payload.get("subChargecodeIdForInv").toString();
			 * subChargecodeId=OpdServiceImpl.getReplaceString(subChargecodeId);
			 * subChargecodeIds=subChargecodeId.split(","); }
			 * 
			 * 
			 * String[] mainChargecodeIdVals=null;
			 * if(payload.containsKey("mainChargecodeIdValForInv")) { String
			 * mainChargecodeIdVal=payload.get("mainChargecodeIdValForInv").toString();
			 * mainChargecodeIdVal=OpdServiceImpl.getReplaceString(mainChargecodeIdVal);
			 * mainChargecodeIdVals=mainChargecodeIdVal.split(","); }
			 * 
			 * 
			 * String[] subChargecodeIdsForSubs=null;
			 * if(payload.containsKey("subChargecodeIdForSub")) { String
			 * subChargecodeIdForSub=payload.get("subChargecodeIdForSub").toString();
			 * subChargecodeIdForSub=OpdServiceImpl.getReplaceString(subChargecodeIdForSub);
			 * subChargecodeIdsForSubs=subChargecodeIdForSub.split(","); }
			 * 
			 * 
			 * String[] mainChargecodeIdValsForSubs=null;
			 * if(payload.containsKey("mainChargecodeIdValForSub")) { String
			 * mainChargecodeIdValForSub=payload.get("mainChargecodeIdValForSub").toString()
			 * ; mainChargecodeIdValForSub=OpdServiceImpl.getReplaceString(
			 * mainChargecodeIdValForSub);
			 * mainChargecodeIdValsForSubs=mainChargecodeIdValForSub.split(","); }
			 * 
			 * 
			 * String[] dgResultDtIdValueSubInvess=null;
			 * if(payload.containsKey("dgResultDtIdValueSubInves")) { String
			 * dgResultDtIdValueSubInves=payload.get("dgResultDtIdValueSubInves").toString()
			 * ; dgResultDtIdValueSubInves=OpdServiceImpl.getReplaceString(
			 * dgResultDtIdValueSubInves);
			 * dgResultDtIdValueSubInvess=dgResultDtIdValueSubInves.split(","); }
			 * 
			 * 
			 * String[] dgResultHdIdSubInvess=null;
			 * if(payload.containsKey("dgResultHdIdSubInves")) { String
			 * dgResultHdIdSubInves=payload.get("dgResultHdIdSubInves").toString();
			 * dgResultHdIdSubInves=OpdServiceImpl.getReplaceString(dgResultHdIdSubInves);
			 * dgResultHdIdSubInvess=dgResultHdIdSubInves.split(","); }
			 * 
			 * 
			 * String[] investigationRemarksForSubs=null;
			 * if(payload.containsKey("investigationRemarksForSub")) { String
			 * investigationRemarksForSub=payload.get("investigationRemarksForSub").toString
			 * (); investigationRemarksForSub=OpdServiceImpl.getReplaceString(
			 * investigationRemarksForSub);
			 * investigationRemarksForSubs=investigationRemarksForSub.split(","); }
			 * 
			 * 
			 * String[] subInvestigationNameInvs=null;
			 * if(payload.containsKey("subInvestigationName")) { String
			 * subInvestigationName=payload.get("subInvestigationName").toString();
			 * subInvestigationName=OpdServiceImpl.getReplaceString(subInvestigationName);
			 * subInvestigationNameInvs=subInvestigationName.split(","); }
			 * 
			 * 
			 * 
			 * Date dateOfExamValue =null; String resultDate="";
			 * if(payload.containsKey("dateOfExam")) {
			 * resultDate=payload.get("dateOfExam").toString();
			 * resultDate=OpdServiceImpl.getReplaceString(resultDate); if
			 * (StringUtils.isNotEmpty(resultDate) && !resultDate.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(resultDate)) { dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(resultDate); } }
			 * 
			 * if(payload.containsKey("dateOfRelease")) {
			 * resultDate=payload.get("dateOfRelease").toString();
			 * resultDate=OpdServiceImpl.getReplaceString(resultDate); if
			 * (StringUtils.isNotEmpty(resultDate) && !resultDate.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(resultDate)) { dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(resultDate); }
			 * 
			 * }
			 * 
			 * String[] investigationResultDateArray=null;
			 * if(payload.containsKey("investigationResultDate")) { String
			 * investigationResultDate=payload.get("investigationResultDate").toString();
			 * investigationResultDate=OpdServiceImpl.getReplaceString(
			 * investigationResultDate);
			 * investigationResultDateArray=investigationResultDate.split(","); } String[]
			 * resultNumberArray=null; if(payload.containsKey("resultNumber")) { String
			 * resultNumber=payload.get("resultNumber").toString();
			 * resultNumber=OpdServiceImpl.getReplaceString(resultNumber);
			 * resultNumberArray=resultNumber.split(","); }
			 * 
			 * 
			 * 
			 * /////////////////////////////////////////////////////////////////////////////
			 * ////////////////////////
			 * 
			 * 
			 * HashMap<String, String> mapInvestigationMap = new HashMap<>();
			 * 
			 * String finalValue = ""; Integer counter = 1; Date date=new Date(); for (int i
			 * = 0; i < investigationIdValues.length; i++) {
			 * 
			 * if (StringUtils.isNotEmpty(investigationIdValues[i].toString()) &&
			 * !investigationIdValues[i].equalsIgnoreCase("0")) { finalValue +=
			 * investigationIdValues[i].trim(); if
			 * (!dgOrderDtIdValues[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(dgOrderDtIdValues[i])) { for (int m = i; m <
			 * dgOrderDtIdValues.length; m++) { finalValue += "###" +
			 * dgOrderDtIdValues[m].trim(); if (m == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * if (!dgOrderHdIds[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(dgOrderHdIds[i])) { for (int j = i; j <
			 * dgOrderHdIds.length; j++) { finalValue += "###" + dgOrderHdIds[j].trim(); if
			 * (j == i) { break; } } } else { finalValue += "###" + "0"; } if (i <
			 * dgResultDtIds.length && StringUtils.isNotBlank(dgResultDtIds[i]) &&
			 * !dgResultDtIds[i].equalsIgnoreCase("0")
			 * &&!dgResultDtIds[i].equalsIgnoreCase("undefined") ) { for (int k = i; k <
			 * dgResultDtIds.length; k++) { finalValue += "###" + dgResultDtIds[k].trim();
			 * if (k == i) { break; } } } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < dgResultHdIds.length && StringUtils.isNotBlank(dgResultHdIds[i]) &&
			 * !dgResultHdIds[i].trim().equalsIgnoreCase("0")
			 * &&!dgResultHdIds[i].equalsIgnoreCase("undefined")) { for (int k = i; k <
			 * dgResultHdIds.length; k++) { finalValue += "###" + dgResultHdIds[k].trim();
			 * if (k == i) { break; } } } else { finalValue += "###" + "0"; }
			 * 
			 * int tempI=0; if(resultInvss!=null && resultInvss.length>0) { tempI=i; i++;
			 * 
			 * if (i < resultInvss.length && StringUtils.isNotBlank(resultInvss[i])&&
			 * !resultInvss[i].equalsIgnoreCase(",")) { for (int k = i; k <
			 * resultInvss.length; k++) {
			 * 
			 * finalValue += "###" + resultInvss[k].trim(); if (k == i) { i=tempI; tempI=0;
			 * 
			 * break; } } } else { i=tempI; tempI=0;
			 * 
			 * finalValue += "###" + "0"; } } else { i=tempI; tempI=0;
			 * 
			 * finalValue += "###" + "0"; }
			 * 
			 * 
			 * if (i < rangeValues.length && StringUtils.isNotBlank(rangeValues[i])) { for
			 * (int k = i; k < rangeValues.length; k++) { finalValue += "###" +
			 * rangeValues[k].trim(); if (k == i) { break; } } } else { finalValue += "###"
			 * + "0"; }
			 * 
			 * if (i < investigationRemarksValues.length &&
			 * StringUtils.isNotBlank(investigationRemarksValues[i])) { for (int k = i; k <
			 * investigationRemarksValues.length; k++) { finalValue += "###" +
			 * investigationRemarksValues[k].trim(); if (k == i) { break; } } } else {
			 * finalValue += "###" + "0"; } if (i < rmsIdFinalValfileUp.length &&
			 * StringUtils.isNotBlank(rmsIdFinalValfileUp[i]) &&
			 * !rmsIdFinalValfileUp[i].trim().equalsIgnoreCase("0")) { for (int k = i; k <
			 * rmsIdFinalValfileUp.length; k++) { finalValue += "###" +
			 * rmsIdFinalValfileUp[k].trim(); if (k == i) { break; } } } else { finalValue
			 * += "###" + "0"; }
			 * 
			 * 
			 * if (investigationTypes!=null && i < investigationTypes.length &&
			 * StringUtils.isNotBlank(investigationTypes[i])) { for (int k = i; k <
			 * investigationTypes.length; k++) { finalValue += "###" +
			 * investigationTypes[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * 
			 * if (subChargecodeIds!=null && i < subChargecodeIds.length &&
			 * StringUtils.isNotBlank(subChargecodeIds[i])) { for (int k = i; k <
			 * subChargecodeIds.length; k++) { finalValue += "###" +
			 * subChargecodeIds[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * 
			 * if (mainChargecodeIdVals!=null && i < mainChargecodeIdVals.length &&
			 * StringUtils.isNotBlank(mainChargecodeIdVals[i])) { for (int k = i; k <
			 * mainChargecodeIdVals.length; k++) { finalValue += "###" +
			 * mainChargecodeIdVals[k].trim(); if (k == i) { break; } } } else { finalValue
			 * += "###" + "0"; }
			 * 
			 * 
			 * 
			 * if (investigationResultDateArray!=null && i <
			 * investigationResultDateArray.length &&
			 * StringUtils.isNotBlank(investigationResultDateArray[i])) { for (int k = i; k
			 * < investigationResultDateArray.length; k++) { finalValue += "###" +
			 * investigationResultDateArray[k].trim(); if (k == i) { break; } } } else {
			 * finalValue += "###" + "0"; }
			 * 
			 * if (resultNumberArray!=null && i < resultNumberArray.length &&
			 * StringUtils.isNotBlank(resultNumberArray[i])) { for (int k = i; k <
			 * resultNumberArray.length; k++) { finalValue += "###" +
			 * resultNumberArray[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * mapInvestigationMap.put(investigationIdValues[i].trim()+"@#"+counter,
			 * finalValue); finalValue = ""; counter++; } } Long dgOrderHdIdsSub=null; if
			 * (StringUtils.isNotEmpty(dgOrderHdIds[0]) && !dgOrderHdIds[0].equals("0")) {
			 * //DgOrderhd dgOrderhd=dgOrderhdDao.find(Long.parseLong(dgOrderHdIds[0])) ;
			 * DgOrderhd
			 * dgOrderhd=dgOrderhdDao.getDgOrderHdByOrderHdId(Long.parseLong(dgOrderHdIds[0]
			 * )); if(dgOrderhd!=null) {
			 * dgOrderhd.setOtherInvestigation(otherInvestigation);
			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(dgOrderhd); //
			 * dgOrderhdDao.saveOrUpdate(dgOrderhd);
			 * dgOrderHdIdsSub=Long.parseLong(dgOrderHdIds[0]); } }
			 * 
			 * counter = 1; String checkDuplicateOne=""; for (String investigationId :
			 * investigationIdValues) { Character charVal=null; if
			 * (StringUtils.isNotEmpty(investigationId)) { if
			 * (mapInvestigationMap.containsKey(investigationId.trim()+"@#"+counter)) {
			 * String investigationValue =
			 * mapInvestigationMap.get(investigationId.trim()+"@#"+counter);
			 * 
			 * if (StringUtils.isNotEmpty(investigationValue)) {
			 * 
			 * String[] finalValueInvestigation = investigationValue.split("###");
			 * checkDuplicateOne+=","+finalValueInvestigation[0].trim(); DgOrderhd
			 * dgOrderhd=null; if (StringUtils.isNotEmpty(finalValueInvestigation[2]) &&
			 * !finalValueInvestigation[2].equals("0")) { //DgOrderhd
			 * dgOrderhd=dgOrderhdDao.find(Long.parseLong(dgOrderHdIds[0])) ;
			 * dgOrderhd=dgOrderhdDao.getDgOrderHdByOrderHdId(Long.parseLong(
			 * finalValueInvestigation[2].trim())); if(dgOrderhd!=null) {
			 * dgOrderhd.setOtherInvestigation(otherInvestigation);
			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(dgOrderhd); //
			 * dgOrderhdDao.saveOrUpdate(dgOrderhd); dgOrderHdIdsSub=
			 * dgOrderhd.getOrderhdId(); } }else { Date date11 = new Date(); Calendar cal =
			 * Calendar.getInstance(); cal.setTime(date11); cal.set(Calendar.HOUR_OF_DAY,
			 * 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
			 * cal.set(Calendar.MILLISECOND, 0); Date from = cal.getTime();
			 * cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE,59); Date to =
			 * cal.getTime();
			 * 
			 * 
			 * //dgOrderhd=dgOrderhdDao.getDgOrderHdByHospitalIdAndPatientAndVisitIdMe(Long.
			 * parseLong(hospitalId),Long.parseLong(patientId),
			 * Long.parseLong(visitId),from,to) ;
			 * 
			 * 
			 * if(dgOrderhd==null) { dgOrderhd=new DgOrderhd();
			 * 
			 * if(StringUtils.isNotEmpty(otherInvestigation)) {
			 * dgOrderhd.setOtherInvestigation(otherInvestigation); }
			 * dgOrderhd.setOrderStatus("P"); if(StringUtils.isNotEmpty(userId)) {
			 * dgOrderhd.setLastChgBy(Long.parseLong(userId));
			 * dgOrderhd.setDoctorId(Long.parseLong(userId)); } dgOrderhd.setLastChgDate(new
			 * Timestamp(date.getTime()));
			 * 
			 * //if(StringUtils.isNotEmpty(departmentId))
			 * //dgOrderhd.setDepartmentId(Long.parseLong(departmentId));
			 * dgOrderhd.setOrderDate(new Timestamp(date.getTime()));
			 * dgOrderhd.setLastChgDate(new Timestamp(date.getTime()));
			 * dgOrderhd.setVisitId(Long.parseLong(visitId));
			 * dgOrderhd.setHospitalId(Long.parseLong(hospitalId));
			 * dgOrderhd.setPatientId(Long.parseLong(patientId));
			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(dgOrderhd);
			 * dgOrderHdIdsSub=dgOrderhd.getOrderhdId(); } else {
			 * dgOrderHdIdsSub=dgOrderhd.getOrderhdId(); } }
			 * 
			 * if(finalValueInvestigation[9] != null &&
			 * !finalValueInvestigation[9].equals("0") &&
			 * (finalValueInvestigation[9].equalsIgnoreCase("s")||finalValueInvestigation[9]
			 * .equalsIgnoreCase("t"))) {
			 * 
			 * if(finalValueInvestigation[5]!=null)
			 * resultOfInvAndSub+=investigationId+"@@##"+" "+"@@##"+finalValueInvestigation[
			 * 5].toString()+"@@##"+" "+"@@##"+"@@@@@@"; String result="";
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equalsIgnoreCase("0") &&
			 * !finalValueInvestigation[5].equalsIgnoreCase(",")) { charVal =
			 * finalValueInvestigation[5].charAt(finalValueInvestigation[5].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { result =
			 * finalValueInvestigation[5].toString().substring(0,
			 * finalValueInvestigation[5].toString().length() - 1); } else {
			 * result=getHtmlText(finalValueInvestigation[5].toString()); }
			 * 
			 * } else { result=""; }
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equals("0") && StringUtils.isNotEmpty(result)) {
			 * 
			 * DgResultEntryDt dgResultEntryDt = null; if (finalValueInvestigation[3] !=
			 * null && StringUtils.isNotBlank(finalValueInvestigation[3] ) &&
			 * !finalValueInvestigation[3].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(finalValueInvestigation[3])) { dgResultEntryDt =
			 * getDgResultEntryDtByDgResultEntryId(Long.parseLong(finalValueInvestigation[3]
			 * )); if(dgResultEntryDt==null) { dgResultEntryDt=new DgResultEntryDt(); } }
			 * else { dgResultEntryDt=new DgResultEntryDt();
			 * 
			 * } dgResultEntryDt.setResult(result); if (finalValueInvestigation != null) {
			 * 
			 * 
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[1] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[1])) {
			 * 
			 * //DgOrderdt
			 * dgOrderdt=dgOrderdtDao.find(Long.parseLong(finalValueInvestigation[1]));
			 * DgOrderdt dgOrderdt=dgOrderdtDao.getDgOrderdtByOrderDtId(Long.parseLong(
			 * finalValueInvestigation[1].trim())); if(dgOrderdt!=null) { if
			 * (finalValueInvestigation != null && finalValueInvestigation[7] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[7]) &&
			 * !finalValueInvestigation[7].equals("0")) {
			 * dgOrderdt.setInvestigationRemarks(finalValueInvestigation[7]); }
			 * dgOrderdt.setLabMark("O"); dgOrderdt.setUrgent("n");
			 * 
			 * 
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equalsIgnoreCase("0") &&
			 * StringUtils.isNotEmpty(result)) { dgOrderdt.setOrderStatus("C"); } else {
			 * dgOrderdt.setOrderStatus("P"); }
			 * 
			 * saveUpdADgOrderDtForDg(dgOrderdt); //dgOrderdtDao.saveOrUpdate(dgOrderdt); }
			 * }
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[0] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[0])) {
			 * dgResultEntryDt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].
			 * trim().toString()));
			 * 
			 * }
			 * 
			 * if (StringUtils.isNotEmpty(finalValueInvestigation[1]) &&
			 * !finalValueInvestigation[1].equals("0"))
			 * dgResultEntryDt.setOrderDtId(Long.parseLong(finalValueInvestigation[1].trim()
			 * .toString()));
			 * 
			 * 
			 * 
			 * 
			 * if (finalValueInvestigation[6] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[6]) &&
			 * !finalValueInvestigation[6].equals("0")) dgResultEntryDt.setRangeValue(
			 * finalValueInvestigation[6].toString());
			 * 
			 * if (finalValueInvestigation[8] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[8]) &&
			 * !finalValueInvestigation[8].equals("0"))
			 * dgResultEntryDt.setRidcId(Long.parseLong(finalValueInvestigation[8].toString(
			 * ))); }
			 * 
			 * DgResultEntryHd dgResultEntryHd=null; Long dgResultentryId=null; if
			 * (finalValueInvestigation[4] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[4]) &&
			 * !finalValueInvestigation[4].equals("0")) dgResultEntryHd=
			 * getDgResultEntryHdByPatientIdAndHospitalId(Long.parseLong(
			 * finalValueInvestigation[4].trim().toString())) ;
			 * 
			 * if(dgResultEntryHd==null) { dgResultEntryHd=
			 * getDgResultEntryHdByPatientIdAndHospitalIds(Long.parseLong(patientId),Long.
			 * parseLong(hospitalId)) ;
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[11].equals("0") &&
			 * StringUtils.isNotEmpty(finalValueInvestigation[11]) && dgOrderHdIdsSub!=null
			 * ) dgResultEntryHd
			 * =dgResultEntryHdDao.getDgResultEntryHdByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueInvestigation[10]),Long.parseLong(finalValueInvestigation
			 * [11]) ,dgOrderHdIdsSub);
			 * 
			 * 
			 * if(dgResultEntryHd!=null) {
			 * dgResultentryId=dgResultEntryHd.getResultEntryId(); } }
			 * 
			 * if(dgResultEntryHd!=null) {
			 * dgResultentryId=dgResultEntryHd.getResultEntryId(); }
			 * 
			 * if(dgResultEntryHd==null) {
			 * 
			 * dgResultEntryHd=new DgResultEntryHd(); if(StringUtils.isNotEmpty(hospitalId))
			 * dgResultEntryHd.setHospitalId(Long.parseLong(hospitalId));
			 * if(StringUtils.isNotEmpty(patientId))
			 * dgResultEntryHd.setPatientId(Long.parseLong(patientId));
			 * if(StringUtils.isNotEmpty(userId))
			 * dgResultEntryHd.setLastChgBy(Long.parseLong(userId));
			 * dgResultEntryHd.setLastChgDate(new Date());
			 * //if(StringUtils.isNotEmpty(dgOrderHdIdsSub) && !dgOrderHdIds[0].equals("0")
			 * ) if(dgOrderHdIdsSub!=null) dgResultEntryHd.setOrderHdId(dgOrderHdIdsSub);
			 * 
			 * if(StringUtils.isNotEmpty(visitId) && !visitId.equalsIgnoreCase("0"))
			 * dgResultEntryHd.setVisitId(Long.parseLong(visitId));
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") )
			 * dgResultEntryHd.setSubChargecodeId(Long.parseLong(finalValueInvestigation[10]
			 * ));
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[11]) &&
			 * !finalValueInvestigation[11].equals("0") )
			 * dgResultEntryHd.setMainChargecodeId(Long.parseLong(finalValueInvestigation[11
			 * ]));
			 * 
			 * dgResultEntryHd.setResultDate(dateOfExamValue);
			 * 
			 * }
			 * 
			 * if(StringUtils.isNotBlank(saveInDraft) &&
			 * (saveInDraft.equalsIgnoreCase("draftMa") ||
			 * saveInDraft.equalsIgnoreCase("draftMa18") ||
			 * saveInDraft.equalsIgnoreCase("draftMa3A"))) {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * if(StringUtils.isNotEmpty(userId))
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); }
			 * if(StringUtils.isNotBlank(saveInDraft) &&
			 * (!saveInDraft.equalsIgnoreCase("draftMa") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa18") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa3A"))) { if
			 * (StringUtils.isNotEmpty(actionMe) &&
			 * (actionMe.equalsIgnoreCase("approveAndClose"))){
			 * dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * if(StringUtils.isNotEmpty(userId))
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date());
			 * if(dgResultEntryHd.getCreatedBy()==null) {
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); } }
			 * 
			 * 
			 * } dgResultentryId = saveOrUpdateDgResultEntryHd(dgResultEntryHd) ;
			 * 
			 * dgResultEntryDt.setResultEntryId(dgResultentryId);
			 * 
			 * if (finalValueInvestigation[1] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[1]) &&
			 * finalValueInvestigation[1].equals("0")) {
			 * 
			 * DgOrderdt dgOrderdt=new DgOrderdt();
			 * dgOrderdt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].
			 * toString())); dgOrderdt.setLastChgBy(Long.parseLong(userId)); //
			 * if(dgOrderHdIds[0]!=null && StringUtils.isNotEmpty(dgOrderHdIds[0])) {
			 * if(dgOrderHdIdsSub!=null) { dgOrderdt.setOrderhdId(dgOrderHdIdsSub); }
			 * 
			 * dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
			 * dgOrderdt.setOrderDate(dateOfExamValue);
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[7] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[7]) &&
			 * !finalValueInvestigation[7].equals("0")) {
			 * dgOrderdt.setInvestigationRemarks(finalValueInvestigation[7]); } if
			 * (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equalsIgnoreCase("0") &&
			 * StringUtils.isNotEmpty(result)) { dgOrderdt.setOrderStatus("C"); } else {
			 * dgOrderdt.setOrderStatus("P"); } if (finalValueInvestigation != null &&
			 * finalValueInvestigation[12] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[12]) &&
			 * !finalValueInvestigation[12].equals("0")) { Date resultOfflineDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[12]);
			 * dgOrderdt.setResultOffLineDate(resultOfflineDate); } if
			 * (finalValueInvestigation != null && finalValueInvestigation[13] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[13]) &&
			 * !finalValueInvestigation[13].equals("0")) {
			 * dgOrderdt.setResultOffLineNumber(finalValueInvestigation[13]); } Long
			 * dgOrderDtId= saveUpdADgOrderDtForDg(dgOrderdt);
			 * dgResultEntryDt.setOrderDtId(dgOrderDtId); } if (finalValueInvestigation !=
			 * null && finalValueInvestigation[9] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0")) {
			 * dgResultEntryDt.setResultType(finalValueInvestigation[9]); }
			 * 
			 * if(StringUtils.isNotBlank(saveInDraft) &&
			 * (saveInDraft.equalsIgnoreCase("draftMa") ||
			 * saveInDraft.equalsIgnoreCase("draftMa18") ||
			 * saveInDraft.equalsIgnoreCase("draftMa3A"))) {
			 * dgResultEntryDt.setResultDetailStatus("E");
			 * dgResultEntryDt.setValidated(null); } if(StringUtils.isNotBlank(saveInDraft)
			 * && (!saveInDraft.equalsIgnoreCase("draftMa") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa18") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa3A"))) { if
			 * (StringUtils.isNotEmpty(actionMe) &&
			 * (actionMe.equalsIgnoreCase("approveAndClose"))){
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } }
			 * 
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[12] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[12]) &&
			 * !finalValueInvestigation[12].equals("0")) { Date resultOfflineDate =null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueInvestigation[12]))
			 * resultOfflineDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[12].trim());
			 * if(resultOfflineDate!=null)
			 * dgResultEntryDt.setResultOffLineDate(resultOfflineDate); } if
			 * (finalValueInvestigation != null && finalValueInvestigation[13] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[13]) &&
			 * !finalValueInvestigation[13].equals("0")) {
			 * dgResultEntryDt.setResultOffLineNumber(finalValueInvestigation[13]); }
			 * 
			 * saveOrUpdateDgResultEntryDt(dgResultEntryDt) ; }
			 * 
			 * else { if (finalValueInvestigation[1] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[1]) &&
			 * finalValueInvestigation[1].equals("0") && dgOrderHdIdsSub!=null ) { DgOrderdt
			 * dgOrderdt=new DgOrderdt();
			 * dgOrderdt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].
			 * toString())); if(StringUtils.isNotEmpty(userId))
			 * dgOrderdt.setLastChgBy(Long.parseLong(userId));
			 * dgOrderdt.setOrderhdId(dgOrderHdIdsSub); dgOrderdt.setLastChgDate(new
			 * Timestamp(date.getTime())); dgOrderdt.setLabMark("O");
			 * dgOrderdt.setUrgent("n");
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equalsIgnoreCase("0")) {
			 * dgOrderdt.setOrderStatus("C"); } else { dgOrderdt.setOrderStatus("P"); }
			 * 
			 * dgOrderdt.setOrderDate(dateOfExamValue); if (finalValueInvestigation != null
			 * && finalValueInvestigation[7] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[7]) &&
			 * !finalValueInvestigation[7].equals("0")) {
			 * dgOrderdt.setInvestigationRemarks(finalValueInvestigation[7]); }
			 * saveUpdADgOrderDtForDg(dgOrderdt); }
			 * 
			 * }
			 * 
			 * } /////////////////////////////////////////////
			 * 
			 * 
			 * else {
			 * 
			 * saveUpdateSubInvestigationForMe(investigationId,finalValueInvestigation,
			 * rangeSubInvess,resultSubInvs,subInvestigationNameIdAndInvs,
			 * dgOrderDtIdValueSubInvess,dgOrderHdIdSubInvess,
			 * Long.parseLong(patientId),hospitalId,userId,dgOrderHdIdsSub,date,
			 * UOMSubss,subChargecodeIdsForSubs,mainChargecodeIdValsForSubs,
			 * visitId,dgResultDtIdValueSubInvess,dgResultHdIdSubInvess,
			 * investigationRemarksForSubs, saveInDraft,
			 * actionMe,subInvestigationNameInvs,dateOfExamValue);
			 * 
			 * }
			 * 
			 * //////////////////////////////////////////////
			 * 
			 * if(dgOrderHdIds!=null && dgOrderHdIdsSub!=null) {
			 * 
			 * if(finalValueInvestigation[1]!=null &&
			 * !finalValueInvestigation[1].equalsIgnoreCase("0")) { Integer
			 * countForResult=dgOrderhdDao.getDgResultEntryDtByOrderDtId(Long.parseLong(
			 * finalValueInvestigation[1].toString().trim()));
			 * 
			 * if(countForResult!=null && countForResult!=0) { DgOrderdt
			 * dgOrderdtObj=dgOrderdtDao.getDgOrderdtByOrderDtId(Long.parseLong(
			 * finalValueInvestigation[1].trim())); dgOrderdtObj.setOrderStatus("C");
			 * saveUpdADgOrderDtForDg(dgOrderdtObj);
			 * 
			 * DgOrderhd dgOrderhdObj=null; if(dgOrderHdIds!=null && dgOrderHdIds[0]!=null)
			 * { dgOrderhdObj=dgOrderhdDao.getDgOrderHdByOrderHdId(dgOrderHdIdsSub);
			 * if(dgOrderhdObj!=null) { dgOrderhdObj.setOrderStatus("C");
			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(dgOrderhdObj); } } } else { DgOrderdt
			 * dgOrderdtObj=dgOrderdtDao.getDgOrderdtByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueInvestigation[0].toString()),dgOrderHdIdsSub);
			 * if(dgOrderdtObj!=null)
			 * countForResult=dgOrderhdDao.getDgResultEntryDtByOrderDtId(dgOrderdtObj.
			 * getOrderdtId()); if(countForResult==0 && dgOrderdtObj!=null &&
			 * dgOrderdtObj.getOrderStatus().equalsIgnoreCase("C")) {
			 * dgOrderdtObj.setOrderStatus("P"); saveUpdADgOrderDtForDg(dgOrderdtObj); } } }
			 * else { DgOrderdt dgOrderdtObj=null;
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[0]) &&
			 * dgOrderHdIdsSub!=null )
			 * dgOrderdtObj=dgOrderdtDao.getDgOrderdtByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueInvestigation[0].toString()),dgOrderHdIdsSub);
			 * if(dgOrderdtObj!=null)
			 * countForResult=dgOrderhdDao.getDgResultEntryDtByOrderDtId(dgOrderdtObj.
			 * getOrderdtId()); if(countForResult==0 &&dgOrderdtObj!=null &&
			 * dgOrderdtObj.getOrderStatus().equalsIgnoreCase("C")) {
			 * dgOrderdtObj.setOrderStatus("P"); saveUpdADgOrderDtForDg(dgOrderdtObj); } }
			 * 
			 * 
			 * List<DgOrderdt>listDgOrderDt=null; if(dgOrderHdIdsSub!=null)
			 * listDgOrderDt=dgOrderhdDao.getDgOrderDtByOrderHdId(dgOrderHdIdsSub);
			 * if(CollectionUtils.isNotEmpty(listDgOrderDt)) {
			 * List<DgOrderhd>listDgOrderhd=dgOrderhdDao.getDgOrderHdtByVisitId(Long.
			 * parseLong(visitId),"required"); if(CollectionUtils.isNotEmpty(listDgOrderhd))
			 * { //listDgOrderhd.get(0); listDgOrderhd.get(0).setOrderStatus("P");
			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(listDgOrderhd.get(0));
			 * 
			 * }
			 * 
			 * }
			 * 
			 * }
			 * 
			 * } } } counter++; }
			 * 
			 * //masMedicalExamReport=getInvestResultSetValue(chargeCodeNames,resultInvss,
			 * masMedicalExamReport); //} catch(Exception e) { e.printStackTrace(); return
			 * null; }
			 * 
			 * return masMedicalExamReport;
			 * 
			 * }
			 * 
			 * 
			 * @Transactional public void saveUpdateSubInvestigationForMe(String
			 * investigationId,String[] finalValueInvestigation,String[]rangeSubInvess,
			 * String[]resultSubInvs,String[]subInvestigationNameIdAndInvs,String[]
			 * dgOrderDtIdValueSubInvess,String[] dgOrderHdIdSubInvess, Long patientId,
			 * String hospitalId, String userId,Long dgOrderHdId,Date date, String[]
			 * UOMSubss,String[]subChargecodeIdsForSubs, String[]
			 * mainChargecodeIdValsForSubs,String
			 * visitId,String[]dgResultDtIdValueSubInvess,String[]dgResultHdIdSubInvess,
			 * String[] investigationRemarksForSubs,String saveInDraft,String
			 * actionMe,String[] subInvestigationNameInvs,Date dateOfExamValue) {
			 * HashMap<String, String> mapSubInvestigationMap = new HashMap<>(); String
			 * finalValue = "";
			 * 
			 * Integer counter=1; Integer resultCounter=0; String checkDuplicateOne="";
			 * 
			 * if(subInvestigationNameIdAndInvs!=null) for (int i = 0; i <
			 * subInvestigationNameIdAndInvs.length; i++) { String []
			 * invesAndSubInves=subInvestigationNameIdAndInvs[i].split("@@");
			 * if(invesAndSubInves[1]!=null &&
			 * invesAndSubInves[1].equalsIgnoreCase(investigationId.trim())) {
			 * 
			 * if (StringUtils.isNotEmpty(invesAndSubInves[0].toString()) &&
			 * !invesAndSubInves[0].equalsIgnoreCase("0") &&
			 * !checkDuplicateOne.contains(invesAndSubInves[0].trim())) {
			 * 
			 * //subInvestigationNameIdAndInvs[i].replace(subInvestigationNameIdAndInvs[i],
			 * "@@"); subInvestigationNameIdAndInvs[i]="00@@00"; finalValue =
			 * investigationId.trim(); finalValue +="###"+ invesAndSubInves[0].trim();
			 * checkDuplicateOne+=","+invesAndSubInves[0].trim(); if
			 * (i<dgOrderDtIdValueSubInvess.length &&
			 * !dgOrderDtIdValueSubInvess[countForOrderDtId].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(dgOrderDtIdValueSubInvess[countForOrderDtId])) {
			 * finalValue += "###" + dgOrderDtIdValueSubInvess[countForOrderDtId].trim(); }
			 * else { finalValue += "###" + "0"; }
			 * 
			 * if (i<dgOrderHdIdSubInvess.length &&
			 * !dgOrderHdIdSubInvess[countForOrderHdId].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(dgOrderHdIdSubInvess[countForOrderHdId])) {
			 * 
			 * finalValue += "###" + dgOrderHdIdSubInvess[countForOrderHdId].trim(); } else
			 * { finalValue += "###" + "0"; }
			 * 
			 * if (i < rangeSubInvess.length &&
			 * StringUtils.isNotBlank(rangeSubInvess[countForRange]) &&
			 * !rangeSubInvess[countForRange].equalsIgnoreCase("0")
			 * &&!rangeSubInvess[countForRange].equalsIgnoreCase("undefined") ) { finalValue
			 * += "###" + rangeSubInvess[countForRange].trim(); } else { finalValue += "###"
			 * + "0"; }
			 * 
			 * 
			 * if(resultSubInvs!=null && resultSubInvs.length>0) {
			 * 
			 * int tempI=i; int tempCountForResult=countForResult; i++; countForResult++;
			 * 
			 * if (i < resultSubInvs.length && countForResult < resultSubInvs.length &&
			 * StringUtils.isNotBlank(resultSubInvs[countForResult]) &&
			 * !resultSubInvs[countForResult].trim().equalsIgnoreCase("0")
			 * &&!resultSubInvs[countForResult].equalsIgnoreCase("undefined") &&
			 * !resultSubInvs[countForResult].trim().equalsIgnoreCase(",") ) { finalValue +=
			 * "###" + resultSubInvs[countForResult].trim();
			 * resultOfInvAndSub+=investigationId+"@@##"+subInvestigationNameInvs[
			 * countForSubInvestigationName]+"@@##"+resultSubInvs[countForResult].toString()
			 * +"@@##"+invesAndSubInves[0]+"@@@@@@"; i =tempI;
			 * countForResult=tempCountForResult; resultCounter++; } else { i =tempI;
			 * countForResult=tempCountForResult; finalValue += "###" + "0";
			 * resultOfInvAndSub+=investigationId+"@@##"+subInvestigationNameInvs[
			 * countForSubInvestigationName]+"@@##"+"0"+"@@##"+invesAndSubInves[0]+"@@@@@@";
			 * } } else { finalValue += "###" + "0";
			 * resultOfInvAndSub+=investigationId+"@@##"+subInvestigationNameInvs[
			 * countForSubInvestigationName]+"@@##"+"0"+"@@##"+invesAndSubInves[0]+"@@@@@@";
			 * }
			 * 
			 * 
			 * 
			 * 
			 * 
			 * if (i < UOMSubss.length &&
			 * StringUtils.isNotBlank(UOMSubss[countForUmoSubInv]) &&
			 * !UOMSubss[countForUmoSubInv].trim().equalsIgnoreCase("0")
			 * &&!UOMSubss[countForUmoSubInv].equalsIgnoreCase("undefined")) { finalValue +=
			 * "###" + UOMSubss[countForUmoSubInv].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < subChargecodeIdsForSubs.length &&
			 * StringUtils.isNotBlank(subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub
			 * ]) && !subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub].trim().
			 * equalsIgnoreCase("0")
			 * &&!subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub].equalsIgnoreCase(
			 * "undefined")) { finalValue += "###" +
			 * subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < mainChargecodeIdValsForSubs.length &&
			 * StringUtils.isNotBlank(mainChargecodeIdValsForSubs[
			 * countMainChargeCodeIdForSub]) &&
			 * !mainChargecodeIdValsForSubs[countMainChargeCodeIdForSub].trim().
			 * equalsIgnoreCase("0")
			 * &&!mainChargecodeIdValsForSubs[countMainChargeCodeIdForSub].equalsIgnoreCase(
			 * "undefined")) { finalValue += "###" +
			 * mainChargecodeIdValsForSubs[countMainChargeCodeIdForSub].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < dgResultDtIdValueSubInvess.length &&
			 * StringUtils.isNotBlank(dgResultDtIdValueSubInvess[
			 * countDgResultDtIdValueSubInvess]) &&
			 * !dgResultDtIdValueSubInvess[countDgResultDtIdValueSubInvess].trim().
			 * equalsIgnoreCase("0")
			 * &&!dgResultDtIdValueSubInvess[countDgResultDtIdValueSubInvess].
			 * equalsIgnoreCase("undefined")) { finalValue += "###" +
			 * dgResultDtIdValueSubInvess[countDgResultDtIdValueSubInvess].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < dgResultHdIdSubInvess.length &&
			 * StringUtils.isNotBlank(dgResultHdIdSubInvess[countDgResultHdIdSubInvess]) &&
			 * !dgResultHdIdSubInvess[countDgResultHdIdSubInvess].trim().equalsIgnoreCase(
			 * "0") &&!dgResultHdIdSubInvess[countDgResultHdIdSubInvess].equalsIgnoreCase(
			 * "undefined")) { finalValue += "###" +
			 * dgResultHdIdSubInvess[countDgResultHdIdSubInvess].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < investigationRemarksForSubs.length &&
			 * StringUtils.isNotBlank(investigationRemarksForSubs[
			 * countForInvestigationRemarkSub]) &&
			 * !dgResultHdIdSubInvess[countForInvestigationRemarkSub].trim().
			 * equalsIgnoreCase("0")
			 * &&!dgResultHdIdSubInvess[countForInvestigationRemarkSub].equalsIgnoreCase(
			 * "undefined")) { finalValue += "###" +
			 * investigationRemarksForSubs[countForInvestigationRemarkSub].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * 
			 * mapSubInvestigationMap.put(invesAndSubInves[0].trim()+"@#"+counter,
			 * finalValue); countForRange=countForRange+1; countForResult=countForResult+1;
			 * countForOrderDtId=countForOrderDtId+1; countForOrderHdId=countForOrderHdId+1;
			 * countForUmoSubInv=countForUmoSubInv+1;
			 * countSubMainChargeCodeIdForSub=countSubMainChargeCodeIdForSub+1;
			 * countMainChargeCodeIdForSub=countMainChargeCodeIdForSub+1;
			 * 
			 * countDgResultDtIdValueSubInvess=countDgResultDtIdValueSubInvess+1;
			 * countDgResultHdIdSubInvess=countDgResultHdIdSubInvess+1;
			 * countForInvestigationRemarkSub=countForInvestigationRemarkSub+1;
			 * countForSubInvestigationName=countForSubInvestigationName+1; finalValue = "";
			 * counter++; System.out.println("countForRange"+countForRange+"countForResult"+
			 * countForResult); } }
			 * 
			 * } counter=1; Long dgResultEntryHdFIdM=null;
			 * if(subInvestigationNameIdAndInvsTemp!=null) { //for(String
			 * subInves:subInvestigationNameIdAndInvsTemp) { for (int i = 0; i <
			 * subInvestigationNameIdAndInvsTemp.length; i++) {
			 * 
			 * Character charVal=null; //String [] invesAndSubInves=subInves.split("@@");
			 * String [] invesAndSubInves=subInvestigationNameIdAndInvsTemp[i].split("@@");
			 * if
			 * (mapSubInvestigationMap.containsKey(invesAndSubInves[0].trim()+"@#"+counter))
			 * { String finalValueOfMap =
			 * mapSubInvestigationMap.get(invesAndSubInves[0].trim()+"@#"+counter);
			 * subInvestigationNameIdAndInvsTemp[i]="00@@00"; String[] finalValueOfMaps
			 * =finalValueOfMap.split("###"); DgOrderhd dgOrderhd=null;
			 * if(finalValueOfMaps[3]!="" && !finalValueOfMaps[3].equalsIgnoreCase("0") &&
			 * dgOrderHdId==null) {
			 * //dgOrderhd=dgOrderhdDao.find(Long.parseLong(finalValueOfMaps[3].trim()));
			 * 
			 * dgOrderhd=dgOrderhdDao.getDgOrderHdByOrderHdId(Long.parseLong(
			 * finalValueOfMaps[3].trim())); dgOrderHdId=dgOrderhd.getOrderhdId(); }
			 * if(dgOrderHdId==null) if(StringUtils.isNotEmpty(hospitalId) &&
			 * patientId!=null) {
			 * dgOrderhd=dgOrderhdDao.getDgOrderHdByHospitalIdAndPatient(Long.parseLong(
			 * hospitalId),patientId); if(dgOrderhd!=null) {
			 * dgOrderHdId=dgOrderhd.getOrderhdId();
			 * 
			 * } }
			 * 
			 * if(dgOrderhd==null && dgOrderHdId==null) { dgOrderhd=new DgOrderhd();
			 * if(StringUtils.isNotEmpty(hospitalId))
			 * dgOrderhd.setHospitalId(Long.parseLong(hospitalId));
			 * if(StringUtils.isNotEmpty(userId))
			 * dgOrderhd.setLastChgBy(Long.parseLong(userId)); if(patientId!=null)
			 * dgOrderhd.setPatientId(patientId); dgOrderhd.setOrderDate(dateOfExamValue);
			 * dgOrderhd.setOrderStatus("C"); if(StringUtils.isNotEmpty(visitId)) {
			 * dgOrderhd.setVisitId(Long.parseLong(visitId)); }
			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(dgOrderhd);
			 * //dgOrderhdDao.saveOrUpdate(dgOrderhd);
			 * 
			 * dgOrderHdId=dgOrderhd.getOrderhdId();
			 * 
			 * }
			 * 
			 * 
			 * DgOrderdt dgOrderdt=null;
			 * 
			 * 
			 * if(finalValueOfMaps[2]!="" && !finalValueOfMaps[2].equalsIgnoreCase("0")) {
			 * //dgOrderdt=dgOrderdtDao.find(Long.parseLong(finalValueOfMaps[2].trim()));
			 * dgOrderdt=dgOrderdtDao.getDgOrderdtByOrderDtId(Long.parseLong(
			 * finalValueOfMaps[2].trim()));
			 * 
			 * } if(dgOrderdt==null) { if(finalValueOfMaps[0]!=null &&
			 * !finalValueOfMaps[0].equalsIgnoreCase("0") && dgOrderHdId!=null) {
			 * 
			 * Criterion cr1=Restrictions.eq("investigationId",
			 * Long.parseLong(finalValueOfMaps[0].trim())); Criterion
			 * cr2=Restrictions.eq("orderhdId", dgOrderHdId);
			 * List<DgOrderdt>listDgOrderdt=dgOrderdtDao.findByCriteria(cr1,cr2); dgOrderdt
			 * =dgOrderdtDao.getDgOrderdtByInvestigationIdAndOrderhdId(Long.parseLong(
			 * finalValueOfMaps[0].trim()),dgOrderHdId);
			 * if(CollectionUtils.isNotEmpty(listDgOrderdt)) {
			 * dgOrderdt=listDgOrderdt.get(0); } } } if(dgOrderdt==null) { dgOrderdt=new
			 * DgOrderdt(); // dgOrderdt.setOrderStatus("P"); dgOrderdt.setLabMark("O");
			 * dgOrderdt.setUrgent("n"); if(finalValueOfMaps[0]!=null)
			 * dgOrderdt.setInvestigationId(Long.parseLong(finalValueOfMaps[0].toString()));
			 * if(StringUtils.isNotEmpty(userId))
			 * dgOrderdt.setLastChgBy(Long.parseLong(userId)); else {
			 * dgOrderdt.setLastChgBy(null); } dgOrderdt.setOrderhdId(dgOrderHdId);
			 * dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
			 * dgOrderdt.setOrderDate(dateOfExamValue); if(finalValueOfMaps[5]!=null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[5]) &&
			 * !finalValueOfMaps[5].equals("0")) { dgOrderdt.setOrderStatus("C"); } else {
			 * dgOrderdt.setOrderStatus("P"); }
			 * 
			 * Long dgOrderDtId =saveUpdADgOrderDtForDg(dgOrderdt); }
			 * 
			 * DgResultEntryHd dgResultEntryHd=null;
			 * 
			 * if (finalValueOfMaps[10] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[10]) &&
			 * !finalValueOfMaps[10].equals("0")) dgResultEntryHd=
			 * getDgResultEntryHdByPatientIdAndHospitalId(Long.parseLong(finalValueOfMaps[10
			 * ].trim().toString())) ;
			 * 
			 * if(dgResultEntryHd==null) {
			 * 
			 * if(patientId!=null && StringUtils.isNotEmpty(hospitalId)) { dgResultEntryHd=
			 * medicalExamDAO.getDgResultEntryHdByPatientIdAndHospitalIdAndInves(patientId,
			 * Long.parseLong(hospitalId),Long.parseLong(finalValueOfMaps[0].toString())) ;
			 * 
			 * }
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(finalValueOfMaps[7]) &&
			 * !finalValueOfMaps[7].equals("0") &&
			 * StringUtils.isNotEmpty(finalValueOfMaps[8]) &&
			 * !finalValueOfMaps[8].equals("0") && dgOrderHdId!=null) {
			 * 
			 * 
			 * dgResultEntryHd
			 * =dgResultEntryHdDao.getDgResultEntryHdByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueOfMaps[7]),Long.parseLong(finalValueOfMaps[8])
			 * ,dgOrderHdId);
			 * 
			 * }
			 * 
			 * 
			 * if(dgResultEntryHd!=null) {
			 * dgResultEntryHdFIdM=dgResultEntryHd.getResultEntryId(); } }
			 * 
			 * if(dgResultEntryHd!=null) {
			 * dgResultEntryHdFIdM=dgResultEntryHd.getResultEntryId(); }
			 * if(dgResultEntryHdFIdM==null) { dgResultEntryHd =new DgResultEntryHd();
			 * dgResultEntryHd.setHospitalId(Long.parseLong(hospitalId));
			 * //dgResultEntryHd.setVisitId(visitId);
			 * dgResultEntryHd.setOrderHdId(dgOrderHdId);
			 * //dgResultEntryHd.setInvestigationId(Long.parseLong(finalValueInvestigation[0
			 * ].toString())); if(patientId!=null) dgResultEntryHd.setPatientId(patientId);
			 * if(StringUtils.isNotEmpty(userId))
			 * dgResultEntryHd.setLastChgBy(Long.parseLong(userId));
			 * dgResultEntryHd.setLastChgDate(new Timestamp(date.getTime()));
			 * 
			 * dgResultEntryHd.setResultDate(dateOfExamValue);
			 * if(StringUtils.isNotEmpty(visitId)) {
			 * dgResultEntryHd.setVisitId(Long.parseLong(visitId)); }
			 * if(StringUtils.isNotEmpty(finalValueOfMaps[7]) &&
			 * !finalValueOfMaps[7].equals("0") )
			 * dgResultEntryHd.setSubChargecodeId(Long.parseLong(finalValueOfMaps[7]));
			 * 
			 * if(StringUtils.isNotEmpty(finalValueOfMaps[8]) &&
			 * !finalValueOfMaps[8].equals("0") )
			 * dgResultEntryHd.setMainChargecodeId(Long.parseLong(finalValueOfMaps[8]));
			 * 
			 * }
			 * 
			 * if(StringUtils.isNotBlank(saveInDraft) &&
			 * (saveInDraft.equalsIgnoreCase("draftMa") ||
			 * saveInDraft.equalsIgnoreCase("draftMa18") ||
			 * saveInDraft.equalsIgnoreCase("draftMa3A"))) {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * if(StringUtils.isNotEmpty(userId))
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); }
			 * if(StringUtils.isNotBlank(saveInDraft) &&
			 * (!saveInDraft.equalsIgnoreCase("draftMa") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa18") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa3A"))) { if
			 * (StringUtils.isNotEmpty(actionMe) &&
			 * (actionMe.equalsIgnoreCase("approveAndClose"))){
			 * dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setVerifiedOn(new Date()); if(StringUtils.isNotEmpty(userId))
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId)); } }
			 * 
			 * if(resultCounter>0) dgResultEntryHdFIdM=
			 * saveOrUpdateDgResultEntryHd(dgResultEntryHd) ;
			 * 
			 * DgResultEntryDt dgResultEntryDt = null; if (finalValueOfMaps[9] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[9] ) &&
			 * !finalValueOfMaps[9].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(finalValueOfMaps[9])) { dgResultEntryDt =
			 * getDgResultEntryDtByDgResultEntryId(Long.parseLong(finalValueOfMaps[9]));
			 * if(dgResultEntryDt==null) { dgResultEntryDt=new DgResultEntryDt(); } } else {
			 * dgResultEntryDt=new DgResultEntryDt();
			 * 
			 * }
			 * 
			 * dgResultEntryDt.setInvestigationId(Long.parseLong(finalValueOfMaps[0].
			 * toString())); if(finalValueOfMaps[5]!=null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[5]) &&
			 * !finalValueOfMaps[5].equals("0")) {
			 * 
			 * //int index=finalValueOfMaps[5].lastIndexOf(","); String result="";
			 * if(index==-1) { result=finalValueOfMaps[5].toString();
			 * dgResultEntryDt.setResult(getHtmlText(result)); } else { result =
			 * finalValueOfMaps[5].toString().substring(0,
			 * finalValueOfMaps[5].toString().length() - 1);
			 * dgResultEntryDt.setResult(getHtmlText(result)); } charVal =
			 * finalValueOfMaps[5].charAt(finalValueOfMaps[5].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { result =
			 * finalValueOfMaps[5].toString().substring(0,
			 * finalValueOfMaps[5].toString().length() - 1);
			 * dgResultEntryDt.setResult(getHtmlText(result)); } else {
			 * dgResultEntryDt.setResult(getHtmlText(finalValueOfMaps[5].toString())); }
			 * 
			 * 
			 * 
			 * }
			 * 
			 * if (finalValueOfMaps[4] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[4]) &&
			 * !finalValueOfMaps[4].equals("0"))
			 * dgResultEntryDt.setRangeValue(finalValueOfMaps[4].toString());
			 * 
			 * if (finalValueOfMaps[1] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[1]) &&
			 * !finalValueOfMaps[1].equals("0"))
			 * dgResultEntryDt.setSubInvestigationId(Long.parseLong(finalValueOfMaps[1].
			 * toString()));
			 * 
			 * 
			 * if (finalValueInvestigation[8] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[8]) &&
			 * !finalValueInvestigation[8].equals("0"))
			 * dgResultEntryDt.setRidcId(Long.parseLong(finalValueInvestigation[8].trim().
			 * toString()));
			 * 
			 * if (finalValueInvestigation[9] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0"))
			 * dgResultEntryDt.setResultType(finalValueInvestigation[9].trim().toString());
			 * 
			 * dgResultEntryDt.setOrderDtId(dgOrderdt.getOrderdtId());
			 * dgResultEntryDt.setResultEntryId(dgResultEntryHdFIdM); if
			 * (finalValueOfMaps[11] != null && StringUtils.isNotBlank(finalValueOfMaps[11])
			 * && !finalValueOfMaps[11].equals("0"))
			 * dgResultEntryDt.setRemarks(finalValueOfMaps[11]);
			 * if(StringUtils.isNotBlank(saveInDraft) &&
			 * (saveInDraft.equalsIgnoreCase("draftMa") ||
			 * saveInDraft.equalsIgnoreCase("draftMa18") ||
			 * saveInDraft.equalsIgnoreCase("draftMa3A"))) {
			 * dgResultEntryDt.setResultDetailStatus("E");
			 * dgResultEntryDt.setValidated(null); } if(StringUtils.isNotBlank(saveInDraft)
			 * && (!saveInDraft.equalsIgnoreCase("draftMa") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa18") &&
			 * !saveInDraft.equalsIgnoreCase("draftMa3A"))) { if
			 * (StringUtils.isNotEmpty(actionMe) &&
			 * (actionMe.equalsIgnoreCase("approveAndClose"))){
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } } if(resultCounter>0)
			 * saveOrUpdateDgResultEntryDt(dgResultEntryDt) ;
			 * 
			 * 
			 * 
			 * counter++; }
			 * 
			 * } } }
			 * 
			 * 
			 * 
			 * 
			 * /////////////////////////////////////////////////////////////////////////////
			 * ///////////////////////////////////////
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * public MasMedicalExamReport getInvestResultSetValue(String[]
			 * chargeCodeNames,String[] resultInvss,MasMedicalExamReport
			 * masMedicalExamReport){ //try {
			 * 
			 * String chargeCodeValue=""; String investigationIdVal=""; String []
			 * investigationIdArray=null; String changeCodeNameTemp="";
			 * if(chargeCodeNames!=null && chargeCodeNames.length>0 &&
			 * !chargeCodeNames[0].equalsIgnoreCase("")) for(String
			 * chargeCodeName:chargeCodeNames) { String[] chargeCodeNameValueNew =
			 * chargeCodeName.trim().split("\\["); if(StringUtils.isEmpty(chargeCodeValue))
			 * { chargeCodeValue=chargeCodeNameValueNew[0]+",";
			 * changeCodeNameTemp=chargeCodeNameValueNew[0];
			 * investigationIdArray=chargeCodeNameValueNew[1].split("\\]");
			 * investigationIdVal=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(
			 * investigationIdArray[0]); if(investigationIdVal!=null &&
			 * resultOfInvAndSub.toString().contains(investigationIdVal.trim())) {
			 * resultOfInvAndSub=resultOfInvAndSub.replaceAll(investigationIdVal,
			 * changeCodeNameTemp); } } else { chargeCodeValue += chargeCodeNameValueNew[0]
			 * + ","; changeCodeNameTemp=chargeCodeNameValueNew[0];
			 * investigationIdArray=chargeCodeNameValueNew[1].split("\\]");
			 * investigationIdVal=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(
			 * investigationIdArray[0]); if(investigationIdVal!=null &&
			 * resultOfInvAndSub.toString().contains(investigationIdVal.trim())) {
			 * resultOfInvAndSub=resultOfInvAndSub.replaceAll(investigationIdVal,
			 * changeCodeNameTemp); }
			 * 
			 * } } String [] chargeCodeFinalValue=null;
			 * if(StringUtils.isNotBlank(chargeCodeValue)) {
			 * chargeCodeFinalValue=chargeCodeValue.split(","); }
			 * 
			 * 
			 * int count=0; if(chargeCodeNames.length!=resultInvss.length) { count=1;
			 * 
			 * }else { count=0; } String [] finalValOfInveResult=null;
			 * if(chargeCodeNames.length>0 && chargeCodeNames.length>0 &&
			 * !chargeCodeNames[0].equalsIgnoreCase(""))
			 * finalValOfInveResult=resultOfInvAndSub.split("@@@@@@");
			 * if(chargeCodeFinalValue!=null && chargeCodeFinalValue.length>0) for(String
			 * chargeCodeName:chargeCodeFinalValue){ String resultVal=""; String dlc="";
			 * String hb=""; String tlc=""; String esr=""; String spGravity=""; String
			 * albumen=""; String sugarR=""; String sugarF=""; String sugarPP=""; String
			 * urea=""; String uricAcid=""; String srCreatine=""; String cholesterol="";
			 * String triglycerids=""; String hdl=""; String vldl=""; Character
			 * charVal=null; if(chargeCodeName.equalsIgnoreCase("dlc")
			 * ||chargeCodeName.equalsIgnoreCase("dc")||
			 * chargeCodeName.equalsIgnoreCase("dlcp")) { //dlc=resultVal; String
			 * dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) { String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##");
			 * 
			 * } else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * //String[] resultvall=ss.split("@@##");
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=4) { charVal =
			 * resultvall[2].charAt(resultvall[2].length() - 1); if(charVal!=null &&
			 * charVal.equals(',')) { String result=""; result =
			 * resultvall[2].toString().substring(0,
			 * resultvall[2].trim().toString().length() - 1);
			 * if(StringUtils.isEmpty(result)) result="0";
			 * dcFinalValueTemp+=resultvall[3]+"##"+result.trim().toString()+","; } else {
			 * if(StringUtils.isEmpty(resultvall[2].toString())) resultvall[2]="0";
			 * dcFinalValueTemp+=resultvall[3]+"##"+resultvall[2].trim().toString(); }
			 * 
			 * } else dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setDlc(dcFinalValueTemp);
			 * 
			 * }else if(chargeCodeName.equalsIgnoreCase("Hb")) { String dcFinalValueTemp="";
			 * for(String ss:finalValOfInveResult) { if(StringUtils.isNotEmpty(ss)) {
			 * String[] resultvall=null; charVal = ss.charAt(ss.length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { ss = ss.toString().substring(0,
			 * ss.trim().toString().length() - 1); resultvall=ss.trim().split("@@##"); }
			 * else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=4) { charVal =
			 * resultvall[2].charAt(resultvall[2].length() - 1); if(charVal!=null &&
			 * charVal.equals(',')) { String result=""; result =
			 * resultvall[2].trim().toString().substring(0,
			 * resultvall[2].trim().toString().length() - 1);
			 * dcFinalValueTemp+=resultvall[3]+"##"+result.trim().toString()+","; } else {
			 * dcFinalValueTemp+=resultvall[3].trim()+"##"+resultvall[2].trim().toString();
			 * }
			 * 
			 * } else dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setHb(dcFinalValueTemp);
			 * 
			 * }else
			 * if(chargeCodeName.equalsIgnoreCase("tlc")||chargeCodeName.equalsIgnoreCase(
			 * "tc")) { String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) { String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##"); } else {
			 * resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].trim().toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setTlc(dcFinalValueTemp); }else
			 * if(chargeCodeName.equalsIgnoreCase("esr")) { String dcFinalValueTemp="";
			 * for(String ss:finalValOfInveResult) { if(StringUtils.isNotEmpty(ss)) { //int
			 * index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##");
			 * 
			 * } else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=4) {
			 * 
			 * charVal = resultvall[2].charAt(resultvall[2].length() - 1); if(charVal!=null
			 * && charVal.equals(',')) { String result=""; result =
			 * resultvall[2].trim().toString().substring(0,
			 * resultvall[2].trim().toString().length() - 1);
			 * dcFinalValueTemp+=resultvall[3]+"##"+result.trim().toString()+","; } else {
			 * dcFinalValueTemp+=resultvall[3].trim()+"##"+resultvall[2].trim().toString();
			 * }
			 * 
			 * //dcFinalValueTemp+=resultvall[3]+"##"+resultvall[2].toString()+",";
			 * 
			 * } else dcFinalValueTemp+="0"+","; } } }
			 * 
			 * masMedicalExamReport.setEsr(dcFinalValueTemp); } else
			 * if(chargeCodeName.equalsIgnoreCase("Gravity")||chargeCodeName.
			 * equalsIgnoreCase("URINE-SPECIFIC  GRAVITY")) { String dcFinalValueTemp="";
			 * for(String ss:finalValOfInveResult) { if(StringUtils.isNotEmpty(ss)) { //int
			 * index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##"); } else {
			 * resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3) {
			 * 
			 * charVal = resultvall[2].charAt(resultvall[2].length() - 1); if(charVal!=null
			 * && charVal.equals(',')) { String result=""; result =
			 * resultvall[2].trim().toString().substring(0,
			 * resultvall[2].trim().toString().length() - 1);
			 * dcFinalValueTemp+="##"+result.trim().toString()+","; } else {
			 * dcFinalValueTemp+="##"+resultvall[2].trim().toString(); }
			 * 
			 * // dcFinalValueTemp+=resultvall[3]+"##"+resultvall[2].toString()+","; } else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setSpGravity(dcFinalValueTemp);
			 * 
			 * }else if(chargeCodeName.equalsIgnoreCase("ALBUMIN")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * String[] resultvall=null; charVal = ss.charAt(ss.length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { ss = ss.toString().substring(0,
			 * ss.trim().toString().length() - 1); resultvall=ss.trim().split("@@##"); }
			 * else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setAlbumin(dcFinalValueTemp);
			 * 
			 * }else if(chargeCodeName.equalsIgnoreCase("SUGAR R")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * // int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##"); } else {
			 * resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3) {
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; } else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setSugarf(dcFinalValueTemp);
			 * 
			 * }else if(chargeCodeName.equalsIgnoreCase("SUGAR F")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * //int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##");
			 * 
			 * } else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setSugarf(dcFinalValueTemp);
			 * 
			 * }else if(chargeCodeName.equalsIgnoreCase("SUGAR PP")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * //int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##"); } else {
			 * resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setSugarpp(dcFinalValueTemp); }else
			 * if(chargeCodeName.equalsIgnoreCase("Urea")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * // int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##");
			 * 
			 * 
			 * } else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setUrea(dcFinalValueTemp);
			 * 
			 * }else if(chargeCodeName.equalsIgnoreCase("URIC ACID")) { //
			 * uricAcid=resultVal; // masMedicalExamReport.setUricacid(uricAcid); String
			 * dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * //int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##");
			 * 
			 * } else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setUricacid(dcFinalValueTemp);
			 * 
			 * }else if(chargeCodeName.equalsIgnoreCase("CREATININE")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * //int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##"); } else {
			 * resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setSrCreatine(dcFinalValueTemp); }else
			 * if(chargeCodeName.equalsIgnoreCase("CHOLESTEROL")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##"); } else {
			 * resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setCholesterol(dcFinalValueTemp); }else
			 * if(chargeCodeName.equalsIgnoreCase("TRIGLYCERIDE")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * //int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##");
			 * 
			 * } else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * 
			 * masMedicalExamReport.setTriglycerides(dcFinalValueTemp); }else
			 * if(chargeCodeName.equalsIgnoreCase("HDL")) { String dcFinalValueTemp="";
			 * for(String ss:finalValOfInveResult) { if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * //int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##");
			 * 
			 * } else { resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setHdl(dcFinalValueTemp); }else
			 * if(chargeCodeName.equalsIgnoreCase("VLDL")) {
			 * 
			 * String dcFinalValueTemp=""; for(String ss:finalValOfInveResult) {
			 * if(StringUtils.isNotEmpty(ss)) {
			 * 
			 * //int index=ss.lastIndexOf(","); String[] resultvall=null; charVal =
			 * ss.charAt(ss.length() - 1); if(charVal!=null && charVal.equals(',')) { ss =
			 * ss.toString().substring(0, ss.trim().toString().length() - 1);
			 * resultvall=ss.trim().split("@@##"); } else {
			 * resultvall=ss.trim().split("@@##"); }
			 * 
			 * if(resultvall[0].trim().equalsIgnoreCase(chargeCodeName)) {
			 * if(resultvall!=null && resultvall.length>=3)
			 * dcFinalValueTemp+="##"+resultvall[2].toString()+","; else
			 * dcFinalValueTemp+="0"+","; } } }
			 * masMedicalExamReport.setVldl(dcFinalValueTemp); } count+=1; }
			 * 
			 * 
			 * //}
			 * 
			 * catch(Exception e) {
			 * 
			 * } return masMedicalExamReport; }
			 * 
			 * @Transactional public Long saveUpdADgOrderDt(DgOrderdt dgOrderdt) { Long
			 * dgOrderDtId=null; try {
			 * 
			 * dgOrderdt.setOrderStatus("P"); dgOrderdt.setLabMark("O"); //
			 * dgOrderdt.setOrderStatus("P"); Date date=new Date();
			 * dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
			 * dgOrderdt.setUrgent("N"); dgOrderDtId=
			 * saveOrUpdateDgOrderdtForDgOrderdt(dgOrderdt);
			 * 
			 * } catch(Exception e) {
			 * 
			 * e.printStackTrace(); }
			 * 
			 * return dgOrderDtId; }
			 * 
			 * @Transactional public Long saveUpdADgOrderDtForDg(DgOrderdt dgOrderdt) { Long
			 * dgOrderDtId=null; try { dgOrderDtId=
			 * saveOrUpdateDgOrderdtForDgOrderdt(dgOrderdt);
			 * 
			 * } catch(Exception e) {
			 * 
			 * e.printStackTrace(); }
			 * 
			 * return dgOrderDtId; }
			 * 
			 * @Transactional
			 * 
			 * @Override public Long saveOrUpdateDgOrderdtForDgOrderdt(DgOrderdt dgOrderDt)
			 * { Session session=null; Long dgOrderDtId=null; //Transaction tx=null; try{
			 * session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(dgOrderDt);
			 * dgOrderDtId=dgOrderDt.getOrderdtId(); session.flush(); session.clear();
			 * //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * 
			 * // getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return dgOrderDtId; }
			 * 
			 * 
			 * @Transactional public void saveOrUpdateMedicalCatComposite(HashMap<String,
			 * Object> payload) { try {
			 * 
			 * String
			 * medicalCompositeNameValue=payload.get("medicalCompositeNameValue").toString()
			 * ; medicalCompositeNameValue=OpdServiceImpl.getReplaceString(
			 * medicalCompositeNameValue);
			 * 
			 * String medicalCompositeDate=payload.get("medicalCompositeDate").toString();
			 * medicalCompositeDate=OpdServiceImpl.getReplaceString(medicalCompositeDate);
			 * 
			 * String patientId=payload.get("patientId").toString();
			 * patientId=OpdServiceImpl.getReplaceString(patientId);
			 * 
			 * String visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceString(visitId); try {
			 * if(StringUtils.isNotEmpty(patientId)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId));
			 * if(StringUtils.isNotEmpty(medicalCompositeNameValue)) {
			 * patient.setMedicalCategoryId(Long.parseLong(medicalCompositeNameValue));
			 * patient.setFitFlag("U");
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * 
			 * saveOrUpdatePatient(patient);
			 * 
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
			 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
			 * deletePatientMedCat(patientMedicalCat); }
			 * 
			 * } } MasMedicalCategory masMedicalCategory=null;
			 * if(StringUtils.isNotEmpty(medicalCompositeNameValue))
			 * masMedicalCategory=getMasMedicalCategoryByCatId(Long.parseLong(
			 * medicalCompositeNameValue));
			 * 
			 * if(StringUtils.isNotEmpty(medicalCompositeDate) &&
			 * medicalCompositeDate!=null) {
			 * 
			 * Date medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(medicalCompositeDate); Timestamp ts =
			 * new Timestamp(medicalCompositeDateValue.getTime());
			 * masMedicalCategory.setLastChgDate(ts); Long catId=
			 * saveOrUpdateMasMedicalCategory(masMedicalCategory);
			 * 
			 * }
			 * 
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(medicalCompositeNameValue)) {
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),
			 * "composit") ;
			 * 
			 * if(CollectionUtils.isEmpty(listPatientMedicalCat)) { patientMedicalCat=new
			 * PatientMedicalCat();
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * patientMedicalCat.setpMedCatId(Long.parseLong(medicalCompositeNameValue));
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId));
			 * if(StringUtils.isNotEmpty(medicalCompositeDate) && medicalCompositeDate!=null
			 * && MedicalExamServiceImpl.checkDateFormat(medicalCompositeDate)) { Date
			 * medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(medicalCompositeDate.trim());
			 * if(medicalCompositeDateValue!=null) { Timestamp ts = new
			 * Timestamp(medicalCompositeDateValue.getTime());
			 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
			 * if(patientMedicalCat!=null) saveOrUpdatePatientMedicalCat(patientMedicalCat);
			 * } } } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * @Transactional public void
			 * saveOrUpdateMedicalCatCompositeDigi(HashMap<String, Object> payload) { //try
			 * {
			 * 
			 * String
			 * medicalCompositeNameValue=payload.get("medicalCompositeNameValue").toString()
			 * ; medicalCompositeNameValue=OpdServiceImpl.getReplaceString(
			 * medicalCompositeNameValue);
			 * 
			 * String medicalCompositeDate=payload.get("medicalCompositeDate").toString();
			 * medicalCompositeDate=OpdServiceImpl.getReplaceString(medicalCompositeDate);
			 * 
			 * String patientId=payload.get("patientId").toString();
			 * patientId=OpdServiceImpl.getReplaceString(patientId);
			 * 
			 * String visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceString(visitId); //try {
			 * if(StringUtils.isNotEmpty(patientId)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId));
			 * if(StringUtils.isNotEmpty(medicalCompositeNameValue)) {
			 * patient.setMedicalCategoryId(Long.parseLong(medicalCompositeNameValue));
			 * patient.setFitFlag("U");
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender) ) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined") &&
			 * !gender.equalsIgnoreCase("0"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * 
			 * saveOrUpdatePatient(patient);
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
			 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
			 * deletePatientMedCat(patientMedicalCat); }
			 * 
			 * } } MasMedicalCategory masMedicalCategory=null;
			 * if(StringUtils.isNotEmpty(medicalCompositeNameValue))
			 * masMedicalCategory=getMasMedicalCategoryByCatId(Long.parseLong(
			 * medicalCompositeNameValue));
			 * 
			 * if(StringUtils.isNotEmpty(medicalCompositeDate) &&
			 * medicalCompositeDate!=null) {
			 * 
			 * Date medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(medicalCompositeDate); Timestamp ts =
			 * new Timestamp(medicalCompositeDateValue.getTime());
			 * masMedicalCategory.setLastChgDate(ts); Long catId=
			 * saveOrUpdateMasMedicalCategory(masMedicalCategory);
			 * 
			 * }
			 * 
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(medicalCompositeNameValue)) {
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),
			 * "composit") ;
			 * 
			 * if(CollectionUtils.isEmpty(listPatientMedicalCat)) { patientMedicalCat=new
			 * PatientMedicalCat();
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * patientMedicalCat.setpMedCatId(Long.parseLong(medicalCompositeNameValue));
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId));
			 * if(StringUtils.isNotEmpty(medicalCompositeDate) && medicalCompositeDate!=null
			 * && MedicalExamServiceImpl.checkDateFormat(medicalCompositeDate)) { Date
			 * medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(medicalCompositeDate.trim());
			 * if(medicalCompositeDateValue!=null) { Timestamp ts = new
			 * Timestamp(medicalCompositeDateValue.getTime());
			 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
			 * if(patientMedicalCat!=null) saveOrUpdatePatientMedicalCat(patientMedicalCat);
			 * } } } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * //@Transactional public void saveOrUpdateMedicalCategory(HashMap<String,
			 * Object> payload) { try { if(payload.get("diagnosisiIdMC")!=null &&
			 * payload.get("diagnosisiIdMC")!="") { String
			 * diagnosisiIdMC=payload.get("diagnosisiIdMC").toString();
			 * diagnosisiIdMC=OpdServiceImpl.getReplaceString(diagnosisiIdMC);
			 * 
			 * String system=payload.get("system").toString();
			 * system=OpdServiceImpl.getReplaceString(system);
			 * 
			 * 
			 * String
			 * medicalCategoryValueId=payload.get("medicalCategoryValueId").toString();
			 * medicalCategoryValueId=OpdServiceImpl.getReplaceString(medicalCategoryValueId
			 * );
			 * 
			 * 
			 * String typeOfCategory=payload.get("typeOfCategory").toString();
			 * typeOfCategory=OpdServiceImpl.getReplaceString(typeOfCategory);
			 * 
			 * 
			 * String categoryDate=payload.get("categoryDate").toString();
			 * categoryDate=OpdServiceImpl.getReplaceString(categoryDate);
			 * 
			 * 
			 * String duration=payload.get("duration").toString();
			 * duration=OpdServiceImpl.getReplaceString(duration);
			 * 
			 * 
			 * String nextcategoryDate=payload.get("nextcategoryDate").toString();
			 * nextcategoryDate=OpdServiceImpl.getReplaceString(nextcategoryDate);
			 * 
			 * String patientMedicalCatId=payload.get("patientMedicalCatId").toString();
			 * patientMedicalCatId=OpdServiceImpl.getReplaceString(patientMedicalCatId);
			 * String flagForDigi=""; if(payload.containsKey("flagForDigi")) {
			 * flagForDigi=payload.get("flagForDigi").toString();
			 * flagForDigi=OpdServiceImpl.getReplaceString(flagForDigi); }
			 * 
			 * String dateOfOrigin=""; if(payload.containsKey("dateOfOrigin")) {
			 * dateOfOrigin=payload.get("dateOfOrigin").toString();
			 * dateOfOrigin=OpdServiceImpl.getReplaceString(dateOfOrigin); } String
			 * placeOfOrigin=""; if(payload.containsKey("placeOfOrigin")) {
			 * placeOfOrigin=payload.get("dateOfOrigin").toString();
			 * placeOfOrigin=OpdServiceImpl.getReplaceString(placeOfOrigin); } String[]
			 * diagnosisiIdMCValues = diagnosisiIdMC.trim().split(","); String[] systemValue
			 * = system.split(","); String[] medicalCategoryValueIdValue =
			 * medicalCategoryValueId.split(","); String[] typeOfCategoryValue =
			 * typeOfCategory.split(","); String[] categoryDateValue =
			 * categoryDate.split(","); String[] durationValue = duration.split(",");
			 * String[] nextcategoryDateValue = nextcategoryDate.split(","); String[]
			 * patientMedicalCatIdValue = patientMedicalCatId.split(",");
			 * 
			 * String[] dateOfOriginArray = dateOfOrigin.split(","); String[]
			 * placeOfOriginArray = placeOfOrigin.split(",");
			 * 
			 * String visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * String patientId=payload.get("patientId").toString();
			 * patientId=OpdServiceImpl.getReplaceString(patientId); String digiFlag="No";
			 * if(payload.containsKey("digiFlag")) {
			 * digiFlag=payload.get("digiFlag").toString();
			 * digiFlag=OpdServiceImpl.getReplaceString(digiFlag); } String finalValue = "";
			 * Integer counter = 1; HashMap<String, String> mapMedicalCategory = new
			 * HashMap<>(); for (int i = 0; i < diagnosisiIdMCValues.length; i++) {
			 * 
			 * if (StringUtils.isNotEmpty(diagnosisiIdMCValues[i].toString()) &&
			 * !diagnosisiIdMCValues[i].equalsIgnoreCase("0")) { finalValue +=
			 * diagnosisiIdMCValues[i].trim();
			 * 
			 * 
			 * 
			 * if (i<patientMedicalCatIdValue.length &&
			 * !patientMedicalCatIdValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(patientMedicalCatIdValue[i])) { for (int m = i; m <
			 * patientMedicalCatIdValue.length; m++) { finalValue += "," +
			 * patientMedicalCatIdValue[m].trim(); if (m == i) { break; } } } else {
			 * finalValue += "," + "0"; }
			 * 
			 * if (i< systemValue.length && !systemValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(systemValue[i])) { for (int m = i; m <
			 * systemValue.length; m++) { finalValue += "," + systemValue[m].trim(); if (m
			 * == i) { break; } } } else { finalValue += "," + "0"; }
			 * 
			 * if(i<medicalCategoryValueIdValue.length &&
			 * !medicalCategoryValueIdValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(medicalCategoryValueIdValue[i])) { for (int j = i; j <
			 * medicalCategoryValueIdValue.length; j++) { finalValue += "," +
			 * medicalCategoryValueIdValue[j].trim(); if (j == i) { break; } } } else {
			 * finalValue += "," + "0"; }
			 * 
			 * if (i < typeOfCategoryValue.length &&
			 * StringUtils.isNotBlank(typeOfCategoryValue[i])) { for (int k = i; k <
			 * typeOfCategoryValue.length; k++) { finalValue += "," +
			 * typeOfCategoryValue[k].trim(); if (k == i) { break; } } } else { finalValue
			 * += "," + "0"; }
			 * 
			 * 
			 * if (i < categoryDateValue.length &&
			 * StringUtils.isNotBlank(categoryDateValue[i])) { for (int l = i; l <
			 * categoryDateValue.length; l++) { finalValue += "," +
			 * categoryDateValue[l].trim(); if (l == i) { break; } } } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * if (i < durationValue.length && StringUtils.isNotBlank(durationValue[i])) {
			 * for (int l = i; l < durationValue.length; l++) { finalValue += "," +
			 * durationValue[l].trim(); if (l == i) { break; } } } else { finalValue += ","
			 * + "0"; }
			 * 
			 * 
			 * if (i < nextcategoryDateValue.length &&
			 * StringUtils.isNotBlank(nextcategoryDateValue[i])) { for (int l = i; l <
			 * nextcategoryDateValue.length; l++) { finalValue += "," +
			 * nextcategoryDateValue[l].trim(); if (l == i) { break; } } } else { finalValue
			 * += "," + "0"; }
			 * 
			 * 
			 * if (i < dateOfOriginArray.length &&
			 * StringUtils.isNotBlank(dateOfOriginArray[i])) { for (int l = i; l <
			 * dateOfOriginArray.length; l++) { finalValue += "," +
			 * dateOfOriginArray[l].trim(); if (l == i) { break; } } } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * 
			 * if (i < placeOfOriginArray.length &&
			 * StringUtils.isNotBlank(placeOfOriginArray[i])) { for (int l = i; l <
			 * placeOfOriginArray.length; l++) { finalValue += "," +
			 * placeOfOriginArray[l].trim(); if (l == i) { break; } } } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * mapMedicalCategory.put(diagnosisiIdMCValues[i].trim()+"@#"+counter,
			 * finalValue); finalValue = ""; counter++; } }
			 * 
			 * counter = 1; Date dateOfCate=null; Integer count=1; String
			 * flageForVisitWithNoFlag=""; if(diagnosisiIdMCValues!=null) for (String
			 * diagnosisId : diagnosisiIdMCValues) { if
			 * (StringUtils.isNotEmpty(diagnosisId.trim())) { if
			 * (mapMedicalCategory.containsKey(diagnosisId.trim()+"@#"+counter)) { String
			 * medicalCateAllValue = mapMedicalCategory.get(diagnosisId.trim()+"@#"+
			 * counter);
			 * 
			 * if (StringUtils.isNotEmpty(medicalCateAllValue)) {
			 * 
			 * String[] finalValueMedicalCategory = medicalCateAllValue.split(",");
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(finalValueMedicalCategory[1]!=null &&
			 * !finalValueMedicalCategory[1].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[1].equalsIgnoreCase("undefined")) {
			 * //patientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(finalValueMedicalCategory[1])) ; if((
			 * finalValueMedicalCategory[0]!=null &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("undefined") &&
			 * finalValueMedicalCategory[3]!=null &&
			 * !finalValueMedicalCategory[3].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[3].equalsIgnoreCase("undefined"))) {
			 * 
			 * listPatientMedicalCat=
			 * getPatientMedicalCatFoUnique(Long.parseLong(patientId),Long.parseLong(visitId
			 * ),"statusForIcg",
			 * Long.parseLong(finalValueMedicalCategory[0].trim().toString()),Long.parseLong
			 * (finalValueMedicalCategory[3].trim().toString())) ; }
			 * 
			 * if(CollectionUtils.isEmpty(listPatientMedicalCat) ) { patientMedicalCat=null;
			 * flageForVisitWithNoFlag="No"; } else { count=1; //patientMedicalCat=null;
			 * patientMedicalCat=listPatientMedicalCat.get(0); }
			 * 
			 * if(CollectionUtils.isNotEmpty(listPatientMedicalCat) &&
			 * patientMedicalCat!=null) { listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),
			 * "visit") ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat) ) {
			 * patientMedicalCat=listPatientMedicalCat.get(0); count++; } else {
			 * patientMedicalCat=null; flageForVisitWithNoFlag="No"; } } else {
			 * 
			 * if(patientMedicalCat!=null) { listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),
			 * "visit") ; if(diagnosisiIdMCValues.length!=listPatientMedicalCat.size()) {
			 * //if(patientMedicalCat==null) { patientMedicalCat=null;
			 * flageForVisitWithNoFlag="No"; //}
			 * 
			 * } else { patientMedicalCat=listPatientMedicalCat.get(0); count++; } }
			 * 
			 * }
			 * 
			 * } else { flageForVisitWithNoFlag="Yes"; } if(patientMedicalCat==null) {
			 * patientMedicalCat=new PatientMedicalCat(); count=0; } else { count=1;
			 * patientMedicalCat=null; } if(patientMedicalCat!=null &&
			 * finalValueMedicalCategory[0]!=null &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("undefined")) {
			 * 
			 * //if(finalValueMedicalCategory[0]!=null &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setIcdId(Long.parseLong(finalValueMedicalCategory[0].trim()
			 * .toString())); //} if(finalValueMedicalCategory[2]!=null &&
			 * !finalValueMedicalCategory[2].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[2].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setSystem(finalValueMedicalCategory[2].trim()); }
			 * if(finalValueMedicalCategory[3]!=null &&
			 * !finalValueMedicalCategory[3].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[3].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setMedicalCategoryId(Long.parseLong(
			 * finalValueMedicalCategory[3].trim().toString())); }
			 * if(finalValueMedicalCategory[4]!=null &&
			 * !finalValueMedicalCategory[4].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[4].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setCategoryType(finalValueMedicalCategory[4].trim().
			 * toString()); }
			 * 
			 * if(finalValueMedicalCategory[5]!=null &&
			 * !finalValueMedicalCategory[5].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[5].equalsIgnoreCase("undefined")) {
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueMedicalCategory[5].
			 * toString())) dateOfCate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueMedicalCategory[5].toString
			 * ().trim()); if(dateOfCate!=null)
			 * patientMedicalCat.setCategoryDate(dateOfCate); }
			 * 
			 * if(finalValueMedicalCategory[6]!=null &&
			 * !finalValueMedicalCategory[6].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[6].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setDuration(Long.parseLong(finalValueMedicalCategory[6].
			 * trim().toString())); }
			 * 
			 * if(finalValueMedicalCategory[7]!=null &&
			 * !finalValueMedicalCategory[7].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[7].equalsIgnoreCase("undefined")) {
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueMedicalCategory[7].
			 * toString().trim())) dateOfCate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueMedicalCategory[7].toString
			 * ()); if(dateOfCate!=null) patientMedicalCat.setNextCategoryDate(dateOfCate);
			 * }
			 * 
			 * 
			 * 
			 * Date dateOfOriginVal=null; if(finalValueMedicalCategory[8]!=null &&
			 * !finalValueMedicalCategory[8].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[8].equalsIgnoreCase("undefined")) {
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueMedicalCategory[8].
			 * toString())) dateOfOriginVal =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueMedicalCategory[8].toString
			 * ().trim()); if(dateOfOriginVal!=null)
			 * patientMedicalCat.setDateOfOrigin(dateOfOriginVal); }
			 * if(finalValueMedicalCategory[9]!=null &&
			 * !finalValueMedicalCategory[9].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[9].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setPlaceOfOrigin(finalValueMedicalCategory[9].trim().
			 * toString()); }
			 * 
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId));
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * if(flageForVisitWithNoFlag!="" &&
			 * flageForVisitWithNoFlag.equalsIgnoreCase("Yes") &&
			 * StringUtils.isNotEmpty(digiFlag) && digiFlag.equalsIgnoreCase("Yes") &&
			 * StringUtils.isEmpty(flagForDigi)) { patientMedicalCat.setMbStatus("P");
			 * patientMedicalCat.setApplyFor("Y"); } Long medicalCateId=null; if(count==0 &&
			 * patientMedicalCat!=null) {
			 * medicalCateId=saveOrUpdatePatientMedicalCat(patientMedicalCat); } } }
			 * 
			 * } } counter++;
			 * 
			 * } } } catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * 
			 * 
			 * public void saveOrUpdateMedicalCategoryDigi(HashMap<String, Object> payload)
			 * { //try { if(payload.get("diagnosisiIdMC")!=null &&
			 * payload.get("diagnosisiIdMC")!="") { String
			 * diagnosisiIdMC=payload.get("diagnosisiIdMC").toString();
			 * diagnosisiIdMC=OpdServiceImpl.getReplaceString(diagnosisiIdMC);
			 * 
			 * String system=payload.get("system").toString();
			 * system=OpdServiceImpl.getReplaceString(system);
			 * 
			 * 
			 * String
			 * medicalCategoryValueId=payload.get("medicalCategoryValueId").toString();
			 * medicalCategoryValueId=OpdServiceImpl.getReplaceString(medicalCategoryValueId
			 * );
			 * 
			 * 
			 * String typeOfCategory=payload.get("typeOfCategory").toString();
			 * typeOfCategory=OpdServiceImpl.getReplaceString(typeOfCategory);
			 * 
			 * 
			 * String categoryDate=payload.get("categoryDate").toString();
			 * categoryDate=OpdServiceImpl.getReplaceString(categoryDate);
			 * 
			 * 
			 * String duration=payload.get("duration").toString();
			 * duration=OpdServiceImpl.getReplaceString(duration);
			 * 
			 * 
			 * String nextcategoryDate=payload.get("nextcategoryDate").toString();
			 * nextcategoryDate=OpdServiceImpl.getReplaceString(nextcategoryDate);
			 * 
			 * String patientMedicalCatId=payload.get("patientMedicalCatId").toString();
			 * patientMedicalCatId=OpdServiceImpl.getReplaceString(patientMedicalCatId);
			 * String flagForDigi=""; if(payload.containsKey("flagForDigi")) {
			 * flagForDigi=payload.get("flagForDigi").toString();
			 * flagForDigi=OpdServiceImpl.getReplaceString(flagForDigi); } String[]
			 * diagnosisiIdMCValues = diagnosisiIdMC.trim().split(","); String[] systemValue
			 * = system.split(","); String[] medicalCategoryValueIdValue =
			 * medicalCategoryValueId.split(","); String[] typeOfCategoryValue =
			 * typeOfCategory.split(","); String[] categoryDateValue =
			 * categoryDate.split(","); String[] durationValue = duration.split(",");
			 * String[] nextcategoryDateValue = nextcategoryDate.split(","); String[]
			 * patientMedicalCatIdValue = patientMedicalCatId.split(","); String
			 * visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * String patientId=payload.get("patientId").toString();
			 * patientId=OpdServiceImpl.getReplaceString(patientId); String digiFlag="No";
			 * if(payload.containsKey("digiFlag")) {
			 * digiFlag=payload.get("digiFlag").toString();
			 * digiFlag=OpdServiceImpl.getReplaceString(digiFlag); } String finalValue = "";
			 * Integer counter = 1; HashMap<String, String> mapMedicalCategory = new
			 * HashMap<>(); for (int i = 0; i < diagnosisiIdMCValues.length; i++) {
			 * 
			 * if (StringUtils.isNotEmpty(diagnosisiIdMCValues[i].toString()) &&
			 * !diagnosisiIdMCValues[i].equalsIgnoreCase("0")) { finalValue +=
			 * diagnosisiIdMCValues[i].trim();
			 * 
			 * 
			 * 
			 * if (i<patientMedicalCatIdValue.length &&
			 * !patientMedicalCatIdValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(patientMedicalCatIdValue[i])) { for (int m = i; m <
			 * patientMedicalCatIdValue.length; m++) { finalValue += "," +
			 * patientMedicalCatIdValue[m].trim(); if (m == i) { break; } } } else {
			 * finalValue += "," + "0"; }
			 * 
			 * if (i< systemValue.length && !systemValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(systemValue[i])) { for (int m = i; m <
			 * systemValue.length; m++) { finalValue += "," + systemValue[m].trim(); if (m
			 * == i) { break; } } } else { finalValue += "," + "0"; }
			 * 
			 * if(i<medicalCategoryValueIdValue.length &&
			 * !medicalCategoryValueIdValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(medicalCategoryValueIdValue[i])) { for (int j = i; j <
			 * medicalCategoryValueIdValue.length; j++) { finalValue += "," +
			 * medicalCategoryValueIdValue[j].trim(); if (j == i) { break; } } } else {
			 * finalValue += "," + "0"; }
			 * 
			 * if (i < typeOfCategoryValue.length &&
			 * StringUtils.isNotBlank(typeOfCategoryValue[i])) { for (int k = i; k <
			 * typeOfCategoryValue.length; k++) { finalValue += "," +
			 * typeOfCategoryValue[k].trim(); if (k == i) { break; } } } else { finalValue
			 * += "," + "0"; }
			 * 
			 * 
			 * if (i < categoryDateValue.length &&
			 * StringUtils.isNotBlank(categoryDateValue[i])) { for (int l = i; l <
			 * categoryDateValue.length; l++) { finalValue += "," +
			 * categoryDateValue[l].trim(); if (l == i) { break; } } } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * if (i < durationValue.length && StringUtils.isNotBlank(durationValue[i])) {
			 * for (int l = i; l < durationValue.length; l++) { finalValue += "," +
			 * durationValue[l].trim(); if (l == i) { break; } } } else { finalValue += ","
			 * + "0"; }
			 * 
			 * 
			 * if (i < nextcategoryDateValue.length &&
			 * StringUtils.isNotBlank(nextcategoryDateValue[i])) { for (int l = i; l <
			 * nextcategoryDateValue.length; l++) { finalValue += "," +
			 * nextcategoryDateValue[l].trim(); if (l == i) { break; } } } else { finalValue
			 * += "," + "0"; }
			 * 
			 * mapMedicalCategory.put(diagnosisiIdMCValues[i].trim()+"@#"+counter,
			 * finalValue); finalValue = ""; counter++; } }
			 * 
			 * counter = 1; Date dateOfCate=null; Integer count=1; String
			 * flageForVisitWithNoFlag=""; if(diagnosisiIdMCValues!=null) for (String
			 * diagnosisId : diagnosisiIdMCValues) { if
			 * (StringUtils.isNotEmpty(diagnosisId.trim())) { if
			 * (mapMedicalCategory.containsKey(diagnosisId.trim()+"@#"+counter)) { String
			 * medicalCateAllValue = mapMedicalCategory.get(diagnosisId.trim()+"@#"+
			 * counter);
			 * 
			 * if (StringUtils.isNotEmpty(medicalCateAllValue)) {
			 * 
			 * String[] finalValueMedicalCategory = medicalCateAllValue.split(",");
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(finalValueMedicalCategory[1]!=null &&
			 * !finalValueMedicalCategory[1].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[1].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(finalValueMedicalCategory[1])) ;
			 * //listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),
			 * "notVisit") ;
			 * 
			 * 
			 * 
			 * if(CollectionUtils.isNotEmpty(listPatientMedicalCat) &&
			 * patientMedicalCat==null) { listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),
			 * "visit") ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat) ) {
			 * patientMedicalCat=listPatientMedicalCat.get(0); count++; } else {
			 * patientMedicalCat=null; flageForVisitWithNoFlag="No"; } } else {
			 * 
			 * if(patientMedicalCat!=null) { listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),
			 * "visit") ; if(diagnosisiIdMCValues.length!=listPatientMedicalCat.size()) {
			 * if(CollectionUtils.isEmpty(listPatientMedicalCat)) { patientMedicalCat=null;
			 * flageForVisitWithNoFlag="No"; }
			 * 
			 * } else { patientMedicalCat=listPatientMedicalCat.get(0); count++; } }
			 * 
			 * }
			 * 
			 * } else { flageForVisitWithNoFlag="Yes";
			 * 
			 * } if(patientMedicalCat==null) { patientMedicalCat=new PatientMedicalCat();
			 * count=0; } else { count=1; patientMedicalCat=null; }
			 * if(patientMedicalCat!=null && finalValueMedicalCategory[0]!=null &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("undefined")) {
			 * 
			 * //if(finalValueMedicalCategory[0]!=null &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[0].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setIcdId(Long.parseLong(finalValueMedicalCategory[0].trim()
			 * .toString())); //} if(finalValueMedicalCategory[2]!=null &&
			 * !finalValueMedicalCategory[2].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[2].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setSystem(finalValueMedicalCategory[2].trim()); }
			 * if(finalValueMedicalCategory[3]!=null &&
			 * !finalValueMedicalCategory[3].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[3].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setMedicalCategoryId(Long.parseLong(
			 * finalValueMedicalCategory[3].trim().toString())); }
			 * if(finalValueMedicalCategory[4]!=null &&
			 * !finalValueMedicalCategory[4].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[4].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setCategoryType(finalValueMedicalCategory[4].trim().
			 * toString()); }
			 * 
			 * if(finalValueMedicalCategory[5]!=null &&
			 * !finalValueMedicalCategory[5].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[5].equalsIgnoreCase("undefined")) {
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueMedicalCategory[5].
			 * toString().trim())) dateOfCate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueMedicalCategory[5].toString
			 * ()); if(dateOfCate!=null) patientMedicalCat.setCategoryDate(dateOfCate); }
			 * 
			 * if(finalValueMedicalCategory[6]!=null &&
			 * !finalValueMedicalCategory[6].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[6].equalsIgnoreCase("undefined")) {
			 * patientMedicalCat.setDuration(Long.parseLong(finalValueMedicalCategory[6].
			 * trim().toString())); }
			 * 
			 * if(finalValueMedicalCategory[7]!=null &&
			 * !finalValueMedicalCategory[7].equalsIgnoreCase("0") &&
			 * !finalValueMedicalCategory[7].equalsIgnoreCase("undefined")) {
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueMedicalCategory[7].
			 * toString().trim())) dateOfCate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueMedicalCategory[7].toString
			 * ()); if(dateOfCate!=null) patientMedicalCat.setNextCategoryDate(dateOfCate);
			 * } patientMedicalCat.setVisitId(Long.parseLong(visitId));
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * if(flageForVisitWithNoFlag!="" &&
			 * flageForVisitWithNoFlag.equalsIgnoreCase("Yes") &&
			 * StringUtils.isNotEmpty(digiFlag) && digiFlag.equalsIgnoreCase("Yes") &&
			 * StringUtils.isEmpty(flagForDigi)) { patientMedicalCat.setMbStatus("P");
			 * patientMedicalCat.setApplyFor("Y"); } Long medicalCateId=null; //if(count==0
			 * && patientMedicalCat!=null) {
			 * medicalCateId=saveOrUpdatePatientMedicalCat(patientMedicalCat); //} }
			 * 
			 * 
			 * 
			 * }
			 * 
			 * } } counter++;
			 * 
			 * } } } catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public MasMedicalExamReport
			 * getMasMedicalExamReprtByVisitId(Long visitId) { MasMedicalExamReport
			 * masMedicalExamReprt=null;
			 * List<MasMedicalExamReport>listMasMedicalExamReprt=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); listMasMedicalExamReprt=
			 * session.createCriteria(MasMedicalExamReport.class).add(Restrictions.eq(
			 * "visitId", visitId)) .list() ;
			 * if(CollectionUtils.isNotEmpty(listMasMedicalExamReprt)) {
			 * masMedicalExamReprt=listMasMedicalExamReprt.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally { //
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * masMedicalExamReprt; }
			 * 
			 * @Override public Long saveOrUpdateMasPastMedicalHistory(PatientPastMedHistory
			 * patientPastMedHistory) { Session session=null; Long
			 * pastMedicalHistoryId=null; Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * tx=session.beginTransaction(); session.saveOrUpdate(patientPastMedHistory);
			 * pastMedicalHistoryId=patientPastMedHistory.getPatientPastMedHistoryId();
			 * 
			 * session.flush(); session.clear(); tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * 
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return pastMedicalHistoryId; }
			 * 
			 * 
			 * @Override public DgResultEntryHd
			 * getDgResultEntryHdByPatientIdAndHospitalId(Long dgResultHdId) {
			 * DgResultEntryHd dgResultEntryHd=null;
			 * List<DgResultEntryHd>listDgResultEntryHd=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); listDgResultEntryHd=
			 * session.createCriteria(DgResultEntryHd.class).add(Restrictions.eq(
			 * "resultEntryId", dgResultHdId)) //.add(Restrictions.eq("hospitalId",
			 * hospitalId)) .list() ; if(CollectionUtils.isNotEmpty(listDgResultEntryHd)) {
			 * dgResultEntryHd=listDgResultEntryHd.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * dgResultEntryHd; }
			 * 
			 * @Override public DgResultEntryHd
			 * getDgResultEntryHdByPatientIdAndHospitalIds(Long patientId,Long hospitalId) {
			 * DgResultEntryHd dgResultEntryHd=null;
			 * List<DgResultEntryHd>listDgResultEntryHd=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); listDgResultEntryHd=
			 * session.createCriteria(DgResultEntryHd.class).add(Restrictions.eq(
			 * "patientId", patientId)) .add(Restrictions.eq("hospitalId", hospitalId))
			 * .list() ; if(CollectionUtils.isNotEmpty(listDgResultEntryHd)) {
			 * dgResultEntryHd=listDgResultEntryHd.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally { //
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * dgResultEntryHd; }
			 * 
			 * 
			 * @Override public Long saveOrUpdateDgResultEntryHd(DgResultEntryHd
			 * dgResultEntryHd) { Session session=null; Long resultEntryId=null;
			 * //Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); //
			 * tx=session.beginTransaction(); session.saveOrUpdate(dgResultEntryHd);
			 * resultEntryId=dgResultEntryHd.getResultEntryId();
			 * 
			 * session.flush(); session.clear(); //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * 
			 * // getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return resultEntryId; }
			 * 
			 * @Override public Long saveOrUpdateDgResultEntryDt(DgResultEntryDt
			 * dgResultEntryDt) { Session session=null; Long resultEntryDetailId=null; //
			 * Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(dgResultEntryDt);
			 * resultEntryDetailId=dgResultEntryDt.getResultEntryDetailId();
			 * 
			 * session.flush(); session.clear(); // tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * 
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return resultEntryDetailId; }
			 * 
			 * 
			 * 
			 * public List<Object[]> getDgMasInvestigationsAndResult_old(Long visitId) {
			 * Transaction transation=null; List<Object[]> listObject=null; try { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * transation=session.beginTransaction(); StringBuilder sbQuery = new
			 * StringBuilder(); sbQuery.
			 * append(" select dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME,ohd.ORDERHD_ID,odt.LAB_MARK,odt.urgent,odt.ORDER_DATE, ohd.VISIT_ID,ohd.OTHER_INVESTIGATION,"
			 * ); sbQuery.append(
			 * "ohd.DEPARTMENT_ID,ohd.HOSPITAL_ID,odt.ORDERDT_ID,rdt.RESULT_ENTRY_DT_ID,rht.RESULT_ENTRY_HD_ID,TO_CHAR(rdt.RESULT),dgUom.UOM_NAME,rdt.RIDC_ID,rdt.RANGE_VALUE,odt.INVESTIGATION_REMARKS,dgmas.MAIN_CHARGECODE_ID  from "
			 * +DG_ORDER_HD+" ohd "); sbQuery.append(" join " +DG_ORDER_DT+
			 * " odt on  ohd.orderhd_id=odt.orderhd_id join "+DG_MAS_INVESTIGATION );
			 * sbQuery.
			 * append(" dgmas on dgmas.INVESTIGATION_ID=odt.INVESTIGATION_ID left join DG_UOM  dgUom on dgUom.UOM_ID=dgmas.UOM_ID "
			 * ); sbQuery.append(" left join "+DG_RESULT_ENTRY_DT+
			 * "rdt on odt.ORDERDT_ID=rdt.ORDERDT_ID left join "+DG_RESULT_ENTRY_HD+
			 * " rht on ohd.ORDERHD_ID= rht.ORDERHD_ID ");
			 * 
			 * 
			 * sbQuery.append(" where ohd.VISIT_ID =:visitId"); Query query =
			 * session.createSQLQuery(sbQuery.toString());
			 * 
			 * query.setParameter("visitId", visitId);
			 * 
			 * listObject = query.list(); transation.commit(); } catch(Exception e) {
			 * e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return listObject; }
			 * 
			 * 
			 * 
			 * @Override public DgResultEntryDt getDgResultEntryDtByDgResultEntryId(Long
			 * dgResultEntryId) { DgResultEntryDt dgResultEntryDt=null;
			 * List<DgResultEntryDt>listDgResultEntryDt=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); listDgResultEntryDt=
			 * session.createCriteria(DgResultEntryDt.class).add(Restrictions.eq(
			 * "resultEntryDetailId", dgResultEntryId)) .list() ;
			 * if(CollectionUtils.isNotEmpty(listDgResultEntryDt)) {
			 * dgResultEntryDt=listDgResultEntryDt.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally { //
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * dgResultEntryDt; }
			 * 
			 * @Override public OpdPatientDetail getOpdPatientDetailByVisitId(Long visitId)
			 * { OpdPatientDetail opdPatientDetail=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); opdPatientDetail =
			 * (OpdPatientDetail) session.createCriteria(OpdPatientDetail.class)
			 * .add(Restrictions.eq("visitId", visitId)).uniqueResult(); } catch(Exception
			 * e) { e.printStackTrace(); } finally { //
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * opdPatientDetail; }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional(readOnly = false) public Map<String,Object>
			 * getApprovalMedicalExamList(HashMap<String,Object> jsonData) {
			 * List<MasMedicalExamReport>listVisit=null; Map<String,Object>map=new
			 * HashMap<>(); int count = 0; Criterion cr1=null; Criterion cr2=null; Criterion
			 * cr3=null; Criterion cr4=null; Criterion cr5=null; Criterion cr10=null;
			 * Criterion cr11=null; String serviceNo=""; try { int pageNo =
			 * Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
			 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			 * Long hospitalId=null; List<Long>listMasDesi= new ArrayList<>();; Users
			 * users=null; if(jsonData.get("hospitalId")!=null &&
			 * StringUtils.isNotBlank(jsonData.get("hospitalId").toString())) {
			 * hospitalId=Long.parseLong(jsonData.get("hospitalId").toString());
			 * users=systemAdminDao.getUsersByUserIdAndHospitalId(Long.parseLong(jsonData.
			 * get("userId").toString()),Long.parseLong(jsonData.get("hospitalId").toString(
			 * ))); if(users!=null) { if(users.getDesignationId()!=null ) { String []
			 * desigArray=users.getDesignationId().split(","); if(desigArray!=null &&
			 * desigArray.length>0) { listMasDesi= new ArrayList<>(); for(String
			 * ss:desigArray) { listMasDesi.add(Long.parseLong(ss.trim())); } } } }
			 * 
			 * } Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * 
			 * Criteria criteria=session.createCriteria(MasMedicalExamReport.class,
			 * "masMedicalExamReport").createAlias("visit",
			 * "visit").createAlias("visit.patient", "patient")
			 * .createAlias("visit.masAppointmentType", "masAppointmentType")
			 * .add(Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * "ME").ignoreCase());
			 * 
			 * 
			 * String flagForList=jsonData.get("flagForList").toString();
			 * 
			 * if(flagForList.equalsIgnoreCase("c")) {
			 * 
			 * cr10=Restrictions.eq("visit.examStatus", "V").ignoreCase();
			 * cr11=Restrictions.eq("visit.examStatus", "T").ignoreCase();
			 * 
			 * LogicalExpression orExp1 = Restrictions.or(cr10,cr11); criteria.add(orExp1);
			 * 
			 * } if(flagForList.equalsIgnoreCase("d")) { cr1=Restrictions.eq("approvedBy",
			 * "MO").ignoreCase(); cr2=Restrictions.eq("status", "af").ignoreCase();
			 * criteria.add(cr1); criteria.add(cr2);
			 * cr5=Restrictions.eq("forwardUnitId",hospitalId); criteria.add(cr5);
			 * cr4=Restrictions.in("fowardedDesignationId",listMasDesi); criteria.add(cr4);
			 * } if(flagForList.equalsIgnoreCase("e")) { cr1=Restrictions.eq("approvedBy",
			 * "RMO").ignoreCase(); cr2=Restrictions.eq("status", "af").ignoreCase();
			 * criteria.add(cr1); criteria.add(cr2);
			 * cr5=Restrictions.eq("forwardUnitId",hospitalId); criteria.add(cr5);
			 * cr4=Restrictions.in("fowardedDesignationId",listMasDesi); criteria.add(cr4);
			 * 
			 * } if(jsonData.get("serviceNo")!=null) {
			 * serviceNo=jsonData.get("serviceNo").toString(); if (serviceNo != null &&
			 * !serviceNo.equals("") && !serviceNo.equals("null")) {
			 * cr3=Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase();
			 * criteria.add(cr3); }
			 * 
			 * }
			 * 
			 * listVisit=criteria.list();
			 * 
			 * count = listVisit.size();
			 * 
			 * //criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			 * 
			 * 
			 * if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null"))
			 * { criteria = criteria.setFirstResult(pagingSize * (1 - 1)); } else { criteria
			 * = criteria.setFirstResult(pagingSize * (pageNo - 1)); }
			 * 
			 * criteria = criteria.setMaxResults(pagingSize);
			 * 
			 * criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize);
			 * 
			 * listVisit = criteria.list(); map.put("count", count); map.put("listVisit",
			 * listVisit); } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
			 * 
			 * return map; }
			 * 
			 * @Override public MasUnit getMasUnitByHospitalId(Long hospitalId) { MasUnit
			 * masUnit=null; MasHospital masHospital=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); masHospital =
			 * (MasHospital) session.createCriteria(MasHospital.class)
			 * .add(Restrictions.eq("hospitalId", hospitalId)).uniqueResult();
			 * 
			 * if(masHospital!=null) { masUnit=masHospital.getMasUnit(); } } catch(Exception
			 * e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return masUnit; }
			 * 
			 * 
			 * public MasHospital getMasHospitalByHospitalId(Long hospitalId) { MasHospital
			 * masHospital=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); masHospital =
			 * (MasHospital) session.createCriteria(MasHospital.class)
			 * .add(Restrictions.eq("hospitalId", hospitalId)).uniqueResult(); }
			 * catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return masHospital; }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public PatientMedicalCat getPatientMedicalCat(Long
			 * patientMedicalCatId) { PatientMedicalCat patientMedicalCat=null; try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * patientMedicalCat = (PatientMedicalCat)
			 * session.createCriteria(PatientMedicalCat.class)
			 * .add(Restrictions.eq("medicalCatId", patientMedicalCatId)).uniqueResult(); }
			 * catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * patientMedicalCat; }
			 * 
			 * 
			 * @Transactional
			 * 
			 * @Override public List<PatientMedicalCat> getPatientMedicalCat(Long
			 * patientId,Long visitId,String fitFlag) { PatientMedicalCat
			 * patientMedicalCat=null; List<PatientMedicalCat>listPatientMedicalCat=null;
			 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * Criteria cr1 = session.createCriteria(PatientMedicalCat.class);
			 * if(StringUtils.isNotEmpty(fitFlag) &&( fitFlag.equalsIgnoreCase("visit")) ) {
			 * cr1. add(Restrictions.eq("patientId",
			 * patientId)).add(Restrictions.eq("visitId", visitId));
			 * cr1.add(Restrictions.isNotNull("system")) ; }
			 * if(StringUtils.isNotEmpty(fitFlag) && fitFlag.equalsIgnoreCase("notVisit")) {
			 * cr1. add(Restrictions.eq("patientId", patientId)); }
			 * 
			 * if(StringUtils.isNotEmpty(fitFlag) && !fitFlag.equalsIgnoreCase("Fit") &&
			 * !fitFlag.equalsIgnoreCase("notVisit") && !fitFlag.equalsIgnoreCase("visit")
			 * && !fitFlag.equalsIgnoreCase("mbStatus")
			 * &&!fitFlag.equalsIgnoreCase("mbStatusCom")) { cr1.
			 * add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("visitId",
			 * visitId)); cr1.add(Restrictions.isNull("system")) ;
			 * cr1.add(Restrictions.isNull("pMedFitFlag")) ; }
			 * if(StringUtils.isNotEmpty(fitFlag) && fitFlag.equalsIgnoreCase("Fit")) { cr1.
			 * add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("visitId",
			 * visitId)); cr1.add(Restrictions.eq("pMedFitFlag","F").ignoreCase()) ; }
			 * 
			 * if(StringUtils.isNotEmpty(fitFlag) && fitFlag.equalsIgnoreCase("mbStatus")) {
			 * cr1. add(Restrictions.eq("patientId",
			 * patientId)).add(Restrictions.eq("visitId", visitId));
			 * //cr1.add(Restrictions.eq("mbStatus","C").ignoreCase()) ;
			 * cr1.add(Restrictions.eq("mbStatus","P").ignoreCase()) ; //cr1=(Criteria)
			 * Restrictions.or(Restrictions.eq("mbStatus","C").ignoreCase(),
			 * Restrictions.eq("mbStatus","P").ignoreCase()); }
			 * if(StringUtils.isNotEmpty(fitFlag) &&
			 * fitFlag.equalsIgnoreCase("mbStatusCom")) { cr1.
			 * add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("visitId",
			 * visitId)); cr1.add(Restrictions.eq("mbStatus","C").ignoreCase()) ;
			 * //cr1.add(Restrictions.eq("mbStatus","P").ignoreCase()) ; //cr1=(Criteria)
			 * Restrictions.or(Restrictions.eq("mbStatus","C").ignoreCase(),
			 * Restrictions.eq("mbStatus","P").ignoreCase()); }
			 * 
			 * 
			 * listPatientMedicalCat=cr1.list(); } catch(Exception e) { e.printStackTrace();
			 * } finally { //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
			 * return listPatientMedicalCat; }
			 * 
			 * @Transactional public List<PatientMedicalCat>
			 * getPatientMedicalCatFoUnique(Long patientId,Long visitId,String fitFlag,Long
			 * icdId,Long medicalCatId) { PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr1 =
			 * session.createCriteria(PatientMedicalCat.class);
			 * if(StringUtils.isNotEmpty(fitFlag) &&
			 * fitFlag.equalsIgnoreCase("statusForIcg")) { cr1.
			 * add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("visitId",
			 * visitId)); cr1.add(Restrictions.eq("icdId",icdId) ) ;
			 * cr1.add(Restrictions.eq("medicalCategoryId", medicalCatId) ) ; }
			 * 
			 * listPatientMedicalCat=cr1.list(); } catch(Exception e) { e.printStackTrace();
			 * } finally { //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
			 * return listPatientMedicalCat; }
			 * 
			 * 
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public Long saveOrUpdatePatientMedicalCat(PatientMedicalCat
			 * patientMedicalCat) { Session session=null; Long medCateId=null; //Transaction
			 * tx=null; //try{ session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(patientMedicalCat);
			 * medCateId=patientMedicalCat.getMedicalCatId();
			 * 
			 * session.flush(); session.clear(); //tx.commit();
			 * 
			 * //} catch(Exception e) { e.printStackTrace(); } finally {
			 * 
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return medCateId; }
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * @Override public List<MasHospital> getMasHospitalList(HashMap<String, Object>
			 * jsonData) { List<MasHospital>listMasHospital =new ArrayList<>();
			 * List<Object[]> listObject=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); //listMasHospital=
			 * session.createCriteria(MasHospital.class).list(); String hospitalId =
			 * jsonData.get("hospitalId").toString(); Integer
			 * hospitalIdValue=Integer.parseInt(hospitalId); StringBuilder sbQuery = new
			 * StringBuilder(); sbQuery.append("SELECT MH.HOSPITAL_ID,MH.HOSPITAL_NAME   ");
			 * sbQuery.
			 * append(" FROM MAS_HOSPITAL MH, ( SELECT  unittype,parent_name,child_name,parent_node, child_node, lv "
			 * ); sbQuery.
			 * append("FROM ( SELECT prior MAS_UNIT.UNIT_NAME parent_name, MAS_UNIT.UNIT_NAME child_name,  "
			 * ); sbQuery.
			 * append(" MAS_UNIT.UNIT_PARENT_ID parent_node,  MAS_UNIT.UNIT_ID child_node, MUT.UNITTYPE_CODE unittype,  level lv, "
			 * ); sbQuery.
			 * append("  rownum rn,   ROW_NUMBER() OVER ( PARTITION BY MAS_UNIT.UNIT_PARENT_ID,MAS_UNIT.UNIT_ID ORDER BY 1 ) rk    FROM   "
			 * ); sbQuery.
			 * append(" VU_MAS_UNIT MAS_UNIT,MAS_HOSPITAL IMH,VU_MAS_UNITTYPE MUT  WHERE   "
			 * ); sbQuery.
			 * append("IMH.UNIT_ID= MAS_UNIT.UNIT_ID and MUT.UNITTYPE_ID = MAS_UNIT.UNITTYPE_ID "
			 * ); sbQuery.
			 * append(" START WITH IMH.UNIT_ID= MAS_UNIT.UNIT_ID AND IMH.HOSPITAL_ID='"
			 * +hospitalIdValue+"' "); sbQuery.
			 * append(" CONNECT BY PRIOR MAS_UNIT.UNIT_ID=MAS_UNIT.UNIT_PARENT_ID ) WHERE rk=1 ) AKS  WHERE AKS.CHILD_NODE=MH.UNIT_ID  order by AKS.lv  "
			 * );
			 * 
			 * 
			 * session.doWork(new Work() {
			 * 
			 * @Override public void execute(java.sql.Connection connection) throws
			 * SQLException { System.out.println("connection is "+connection);
			 * connection.setAutoCommit(false); //CallableStatement call =
			 * connection.prepareCall(" CALL "+databaseScema+
			 * "."+"ASP_HIERARCHICAL_UNIT_C_TO_P(?, ?)"); String
			 * sss="{ ? = call "+databaseScema+".ASP_HIERARCHICAL_UNIT_C_TO_P( ? ) }";
			 * CallableStatement call = connection.prepareCall(sss);
			 * call.registerOutParameter(1, Types.REF_CURSOR); call.setInt(2,
			 * hospitalIdValue); //call.setLong(1, hospitalIdValue);
			 * //call.registerOutParameter(2, OracleTypes.CURSOR); call.execute(); ResultSet
			 * rs = (ResultSet)call.getObject(2);
			 * 
			 * while(rs!=null && rs.next()) { MasHospital masHospital=new MasHospital();
			 * 
			 * if(rs.getLong(1)!=0) { masHospital.setHospitalId(rs.getLong(1)); }
			 * if(rs.getString(2)!=null) { masHospital.setHospitalName(rs.getString(2)); }
			 * listMasHospital.add(masHospital); }
			 * 
			 * 
			 * } });
			 * 
			 * 
			 * 
			 * }
			 * 
			 * 
			 * catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * listMasHospital; }
			 * 
			 * 
			 * @Override public Users getUserByUserId(Long userId) { Users uers=null; try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession(); uers=
			 * (Users) session.createCriteria(Users.class).add(Restrictions.eq("userId",
			 * userId)).uniqueResult(); } catch(Exception e) { e.printStackTrace(); }
			 * finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * uers; }
			 * 
			 * @Override public HashMap<String, Object>
			 * getMedicalExamListByStatus(HashMap<String, Object> jsonData) {
			 * List<MasMedicalExamReport>listMasMedicalExamReport=null; HashMap<String,
			 * Object>map=new HashMap<>(); try { int pageNo =
			 * Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
			 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			 * int count=0; Users users=null; Criterion co7=null; Criterion co8=null;
			 * Criterion co9=null; String serviceNo=""; if(jsonData.get("hospitalId")!=null)
			 * // listMasDesignationMapping
			 * =systemAdminDao.getMassDesiByUnitId(Long.parseLong(jsonData.get("hospitalId")
			 * .toString()));
			 * users=systemAdminDao.getUsersByUserIdAndHospitalId(Long.parseLong(jsonData.
			 * get("userId").toString()),Long.parseLong(jsonData.get("hospitalId").toString(
			 * ))); List<Long>listMasDesi= new ArrayList<>(); if(users!=null) {
			 * if(users.getDesignationId()!=null ) { String []
			 * desigArray=users.getDesignationId().split(","); if(desigArray!=null &&
			 * desigArray.length>0) { listMasDesi= new ArrayList<>(); for(String
			 * ss:desigArray) { listMasDesi.add(Long.parseLong(ss.trim())); } } } }
			 * 
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * Criteria criteria=
			 * session.createCriteria(MasMedicalExamReport.class).createAlias("visit",
			 * "visit") .createAlias("visit.patient",
			 * "patient").createAlias("visit.masAppointmentType", "masAppointmentType");
			 * Criterion co1=null; Criterion co2=null; Criterion co3=null;
			 * 
			 * if(jsonData.get("flag").toString().equalsIgnoreCase("af")) {
			 * co2=Restrictions.eq("status", jsonData.get("flag").toString());
			 * co1=Restrictions.eq("forwardUnitId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())); LogicalExpression
			 * logexe=Restrictions.and(co2, co1); criteria.add(logexe);
			 * //if(CollectionUtils.isNotEmpty(listMasDesi)) { Criterion
			 * co111=Restrictions.in("fowardedDesignationId", listMasDesi);
			 * criteria.add(co111); //} //listMasMedicalExamReport=criteria.list(); }
			 * if(jsonData.get("flag").toString().equalsIgnoreCase("ac")) {
			 * 
			 * //co1 =Restrictions.eq("hospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())); Criterion
			 * co88=Restrictions.eq("lastChgBy",
			 * Long.parseLong(jsonData.get("userId").toString()));
			 * 
			 * // criteria.add(co1); criteria.add(co88); co2=Restrictions.eq("status",
			 * jsonData.get("flag").toString()); co3=Restrictions.eq("status", "af");
			 * 
			 * LogicalExpression loexe=Restrictions.or(co2,co3); criteria.add(loexe);
			 * //listMasMedicalExamReport=criteria.list();
			 * 
			 * co7=Restrictions.and(Restrictions.eq("approvedBy",
			 * "MO"),Restrictions.eq("hospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())));
			 * co8=Restrictions.and(Restrictions.eq("approvedBy",
			 * "RMO"),Restrictions.eq("rmoHospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())));
			 * co9=Restrictions.and(Restrictions.eq("approvedBy",
			 * "PRMo"),Restrictions.eq("pdmsHospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())));
			 * 
			 * Disjunction orExp31 = Restrictions.or(co7,co8,co9); criteria.add(orExp31);
			 * 
			 * 
			 * } if(jsonData.get("flag").toString().equalsIgnoreCase("rj")) { //
			 * co2=Restrictions.eq("status", jsonData.get("flag").toString());
			 * co3=Restrictions.eq("status", "rj"); criteria.add(co3);
			 * //co1=Restrictions.eq("hospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())); // criteria.add(co1);
			 * Criterion co88=Restrictions.eq("lastChgBy",
			 * Long.parseLong(jsonData.get("userId").toString())); criteria.add(co88); //
			 * Conjunction dij=Restrictions.and(co1,co2,co3);
			 * //listMasMedicalExamReport=criteria.list();
			 * 
			 * 
			 * 
			 * co7=Restrictions.and(Restrictions.eq("approvedBy",
			 * "MO"),Restrictions.eq("hospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())));
			 * co8=Restrictions.and(Restrictions.eq("approvedBy",
			 * "RMO"),Restrictions.eq("rmoHospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())));
			 * //co9=Restrictions.and(Restrictions.eq("approvedBy",
			 * "PRMo"),Restrictions.eq("rmoHospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())));
			 * co9=Restrictions.or(Restrictions.and(Restrictions.eq("approvedBy",
			 * "PRMo"),Restrictions.eq("rmoHospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())))
			 * ,Restrictions.and(Restrictions.eq("approvedBy",
			 * "PRMo"),Restrictions.eq("pdmsHospitalId",
			 * Long.parseLong(jsonData.get("hospitalId").toString())))); Disjunction orExp31
			 * = Restrictions.or(co7,co8,co9); criteria.add(orExp31); }
			 * 
			 * if(jsonData.get("serviceNo")!=null) {
			 * serviceNo=jsonData.get("serviceNo").toString(); if (serviceNo != null &&
			 * !serviceNo.equals("") && !serviceNo.equals("null")) { Criterion
			 * cr12=Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase();
			 * criteria.add(cr12); }
			 * 
			 * } String meMbFlag=jsonData.get("membFlab").toString();
			 * System.out.println("membFlab :::"+meMbFlag); Criterion
			 * crr1=Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * meMbFlag).ignoreCase(); criteria.add(crr1);
			 * listMasMedicalExamReport=criteria.list(); count =
			 * listMasMedicalExamReport.size(); //criteria =
			 * criteria.setFirstResult(pagingSize * (pageNo - 1));
			 * 
			 * if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null"))
			 * { criteria = criteria.setFirstResult(pagingSize * (1 - 1)); } else { criteria
			 * = criteria.setFirstResult(pagingSize * (pageNo - 1)); }
			 * 
			 * criteria = criteria.setMaxResults(pagingSize);
			 * 
			 * criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize); listMasMedicalExamReport =
			 * criteria.list(); map.put("count", count); map.put("listMasMedicalExamReport",
			 * listMasMedicalExamReport);
			 * 
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return map; }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public Long saveOrUpdatePatient(Patient patient) { Session
			 * session=null; Long patientId=null; //Transaction tx=null; //try{
			 * if(session==null) { session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); }
			 * //tx=session.beginTransaction(); session.saveOrUpdate(patient);
			 * patientId=patient.getPatientId(); session.flush(); session.clear();
			 * //tx.commit();
			 * 
			 * //} catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return patientId; }
			 * 
			 * @Override public MasMedicalCategory getMasMedicalCategoryByCatId(Long
			 * medicalCatId) { MasMedicalCategory masMedicalCategory=null; try { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * masMedicalCategory= (MasMedicalCategory)
			 * session.createCriteria(MasMedicalCategory.class)
			 * .add(Restrictions.eq("medicalCategoryId", medicalCatId)).uniqueResult(); }
			 * catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * masMedicalCategory; }
			 * 
			 * 
			 * 
			 * @Override public Long saveOrUpdateMasMedicalCategory(MasMedicalCategory
			 * masMedicalCategory) { Session session=null; Long catId=null; //Transaction
			 * tx=null; try{ session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(masMedicalCategory);
			 * catId=masMedicalCategory.getMedicalCategoryId(); session.flush();
			 * session.clear(); //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return catId; }
			 * 
			 * @Override public List<MasUnit> getMasUnitListByHospitalId(Long hospitalId) {
			 * MasUnit masUnit=null; List<MasUnit>listMasUnit=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cri =
			 * session.createCriteria(MasHospital.class).createAlias("masUnit", "masUnit")
			 * .add(Restrictions.eq("hospitalId", hospitalId)); ProjectionList
			 * projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("masUnit.unitId").as("unitId"));
			 * projectionList.add(Projections.property("hospitalId").as("hospitalId"));
			 * cri.setProjection(projectionList); List<Object[]> listObject=null;
			 * listObject=cri.list();
			 * 
			 * if(listObject!=null && listObject.size()>0) { listMasUnit=new ArrayList<>();
			 * for (Iterator<?> it = listObject.iterator(); it.hasNext();) { masUnit=new
			 * MasUnit(); Object[] row = (Object[]) it.next(); if(row[0]!=null) {
			 * masUnit.setUnitId(Long.parseLong(row[0].toString())); }
			 * 
			 * listMasUnit.add(masUnit); } } } catch(Exception e) { e.printStackTrace(); }
			 * finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * listMasUnit; }
			 * 
			 * @Override public PatientImmunizationHistory
			 * getPatientImmunizationHistory(Long visitId,Long itemId) {
			 * PatientImmunizationHistory patientImmunizationHistory=null;
			 * List<PatientImmunizationHistory>listPatientImmunizationHistory=null; try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * Criteria cr1= session.createCriteria(PatientImmunizationHistory.class)
			 * .add(Restrictions.eq("visitId", visitId)).add(Restrictions.eq("itemId",
			 * itemId)) ; listPatientImmunizationHistory=cr1.list();
			 * if(CollectionUtils.isNotEmpty(listPatientImmunizationHistory)) {
			 * patientImmunizationHistory=listPatientImmunizationHistory.get(0); } }
			 * catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * patientImmunizationHistory; }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public Long
			 * saveOrUpdatePatientImmunizationHistory(PatientImmunizationHistory
			 * patientImmunizationHistory) { Session session=null; Long immunizationId=null;
			 * //Transaction tx=null; // try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction();
			 * session.saveOrUpdate(patientImmunizationHistory);
			 * immunizationId=patientImmunizationHistory.getImmunizationId();
			 * session.flush(); session.clear(); //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally { //
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return immunizationId; }
			 * 
			 * @Override public Map<String, Object>
			 * getPatientImmunizationHistoryByVisitId(Long visitId,HashMap<String, Object>
			 * jsonData) {
			 * List<PatientImmunizationHistory>listPatientImmunizationHistory=null;
			 * Map<String, Object>mapPatientImmunizationHistory=new HashMap<>(); List
			 * totalMatches =null; try {
			 * 
			 * int pageNo =0; int pagingSize =0; Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criterion cr2=null;
			 * Criterion cr3=null; Criteria cr1= null; cr1=
			 * session.createCriteria(PatientImmunizationHistory.class,"pp").createAlias(
			 * "visit", "visit").createAlias("masStoreItem", "masStoreItem");
			 * if(jsonData.get("flagForForm")!=null &&
			 * jsonData.get("flagForForm").toString().equalsIgnoreCase("h")) { pageNo =
			 * Integer.parseInt(jsonData.get("pageNo") + ""); pagingSize =
			 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			 * String patientId=jsonData.get("patientId").toString();
			 * cr2=Restrictions.eq("patientId", Long.parseLong(patientId)); cr1.add(cr2);
			 * cr1.addOrder(Order.desc("immunizationDate"));
			 * 
			 * } else if(jsonData.containsKey("flagForForm") &&
			 * jsonData.get("flagForForm")!=null &&
			 * jsonData.get("flagForForm").toString().equalsIgnoreCase("digi")) {
			 * cr2=Restrictions.eq("visitId", visitId) ; cr1.add(cr2); String
			 * patientId=jsonData.get("patientId").toString();
			 * cr2=Restrictions.eq("patientId", Long.parseLong(patientId)); cr1.add(cr2);
			 * cr1.addOrder(Order.desc("immunizationDate"));
			 * 
			 * } else if(jsonData.containsKey("flagForForm") &&
			 * jsonData.get("flagForForm")!=null &&
			 * jsonData.get("flagForForm").toString().equalsIgnoreCase("f")) { String pvmsNo
			 * = UtilityServices.getValueOfPropByKey("pvmsNo"); String[] pvmsNoValue = null;
			 * List<String>pvmsLis=new ArrayList<>(); if (StringUtils.isNotEmpty(pvmsNo)) {
			 * pvmsNoValue = pvmsNo.split(","); pvmsLis.add(pvmsNoValue[0].trim());
			 * pvmsLis.add(pvmsNoValue[1].trim()); }
			 * cr2=Restrictions.in("masStoreItem.pvmsNo", pvmsLis) ; cr1.add(cr2); String
			 * patientId=jsonData.get("patientId").toString();
			 * cr2=Restrictions.eq("patientId", Long.parseLong(patientId)); cr1.add(cr2);
			 * cr1.addOrder(Order.desc("immunizationDate"));
			 * 
			 * } else if(jsonData.containsKey("flagForForm") &&
			 * jsonData.get("flagForForm")!=null &&
			 * jsonData.get("flagForForm").toString().equalsIgnoreCase("me")) {
			 * 
			 * String pvmsNo = UtilityServices.getValueOfPropByKey("pvmsNo"); String[]
			 * pvmsNoValue = null; List<String>pvmsLis=new ArrayList<>(); if
			 * (StringUtils.isNotEmpty(pvmsNo)) { pvmsNoValue = pvmsNo.split(",");
			 * pvmsLis.add(pvmsNoValue[0].trim()); pvmsLis.add(pvmsNoValue[1].trim()); }
			 * cr3=Restrictions.not(Restrictions.in("masStoreItem.pvmsNo", pvmsLis)) ;
			 * cr1.add(cr3);
			 * 
			 * 
			 * cr2=Restrictions.eq("visitId", visitId) ; cr1.add(cr2);
			 * 
			 * String patientId=jsonData.get("patientId").toString();
			 * cr2=Restrictions.eq("patientId", Long.parseLong(patientId)); cr1.add(cr2);
			 * 
			 * cr1.addOrder(Order.desc("immunizationDate"));
			 * 
			 * }
			 * 
			 * 
			 * else { //cr2=Restrictions.eq("visitId", visitId) ; //cr1.add(cr2); String
			 * patientId=jsonData.get("patientId").toString();
			 * cr2=Restrictions.eq("patientId", Long.parseLong(patientId)); cr1.add(cr2);
			 * cr1.addOrder(Order.desc("immunizationDate"));
			 * 
			 * } ProjectionList projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("itemId").as("itemId"));
			 * projectionList.add(Projections.max("pp.immunizationDate").as(
			 * "immunizationDate"));
			 * projectionList.add(Projections.max("pp.immunizationId").as("immunizationId"))
			 * ; projectionList.add(Projections.max("pp.lastChgBy").as("lastChgBy"));
			 * projectionList.add(Projections.max("pp.lastChgDate").as("lastChgDate"));
			 * projectionList.add(Projections.max("pp.duration").as("duration"));
			 * projectionList.add(Projections.max("pp.nextDueDate").as("nextDueDate"));
			 * projectionList.add(Projections.max("masStoreItem.nomenclature").as(
			 * "nomenclature"));
			 * projectionList.add(Projections.max("masStoreItem.pvmsNo").as("pvmsNo"));
			 * projectionList.add(Projections.max("pp.prescriptionDate").as(
			 * "prescriptionDate"));
			 * projectionList.add(Projections.groupProperty("itemId"));
			 * cr1.setProjection(projectionList); int count =0; List<Object[]>
			 * listObject=null; listObject=cr1.list();
			 * if(CollectionUtils.isNotEmpty(listObject)); count =listObject.size() ;
			 * 
			 * if(jsonData.get("flagForForm")!=null &&
			 * jsonData.get("flagForForm").toString().equalsIgnoreCase("h")) {
			 * cr1.setFirstResult(pagingSize * (pageNo - 1)); cr1.setMaxResults(pagingSize);
			 * }
			 * 
			 * listObject=cr1.list(); MasStoreItem masStoreItem=null;
			 * PatientImmunizationHistory patientImmunizationHistory=null;
			 * if(listObject!=null && listObject.size()>0) {
			 * listPatientImmunizationHistory=new ArrayList<>(); for (Iterator<?> it =
			 * listObject.iterator(); it.hasNext();) { masStoreItem=new MasStoreItem();
			 * patientImmunizationHistory=new PatientImmunizationHistory(); Object[] row =
			 * (Object[]) it.next(); if(row[0]!=null) {
			 * masStoreItem.setItemId(Long.parseLong(row[0].toString()));
			 * 
			 * }
			 * 
			 * if(row[1]!=null) { Date immunizationDate =
			 * HMSUtil.dateFormatteryyyymmdd(row[1].toString());
			 * patientImmunizationHistory.setImmunizationDate(immunizationDate); }
			 * if(row[2]!=null) {
			 * patientImmunizationHistory.setImmunizationId(Long.parseLong(row[2].toString()
			 * )); } if(row[3]!=null) {
			 * patientImmunizationHistory.setLastChgBy(Long.parseLong(row[3].toString())); }
			 * if(row[4]!=null) { Date lastChgDate =
			 * HMSUtil.dateFormatteryyyymmdd(row[4].toString()); Timestamp ts = new
			 * Timestamp(lastChgDate.getTime());
			 * patientImmunizationHistory.setLastChgDate(ts); }
			 * 
			 * 
			 * 
			 * if(row[5]!=null) {
			 * patientImmunizationHistory.setPatientId(Long.parseLong(row[5].toString())); }
			 * if(row[6]!=null) { Visit visit=new Visit();
			 * visit.setVisitId(Long.parseLong(row[6].toString()));
			 * patientImmunizationHistory.setVisit(visit);
			 * patientImmunizationHistory.setVisitId(Long.parseLong(row[6].toString())); }
			 * 
			 * if(row[5]!=null) { patientImmunizationHistory.setDuration(
			 * row[5].toString()); }
			 * 
			 * if(row[6]!=null) { Date nextDueDate =
			 * HMSUtil.dateFormatteryyyymmdd(row[6].toString()); Timestamp ts = new
			 * Timestamp(nextDueDate.getTime());
			 * patientImmunizationHistory.setNextDueDate(nextDueDate); } if(row[7]!=null) {
			 * masStoreItem.setNomenclature(row[7].toString()); } if(row[8]!=null) {
			 * masStoreItem.setPvmsNo(row[8].toString());
			 * 
			 * } if(row[9]!=null) { Date prescriptionDate =
			 * HMSUtil.dateFormatteryyyymmdd(row[9].toString()); Timestamp ts = new
			 * Timestamp(prescriptionDate.getTime());
			 * patientImmunizationHistory.setPrescriptionDate(prescriptionDate); }
			 * 
			 * patientImmunizationHistory.setMasStoreItem(masStoreItem);
			 * listPatientImmunizationHistory.add(patientImmunizationHistory); } }
			 * 
			 * 
			 * //listPatientImmunizationHistory = cr1.list();
			 * mapPatientImmunizationHistory.put("listPatientImmunizationHistory",
			 * listPatientImmunizationHistory); mapPatientImmunizationHistory.put("count",
			 * count);
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * mapPatientImmunizationHistory; }
			 * 
			 * @Override public Patient checkPatientForPatientId(Long i) { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
			 * session.createCriteria(Patient.class); cr.add(Restrictions.eq("patientId",
			 * i)); Patient list = (Patient) cr.uniqueResult(); return list; }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public Long updateOpdPatientDetailMe(OpdPatientDetail
			 * opdPatientDetail) { Long opdPatientDetailId=null; Session session=null;
			 * //Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(opdPatientDetail);
			 * opdPatientDetailId=opdPatientDetail.getOpdPatientDetailsId();
			 * session.flush(); session.clear(); //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return opdPatientDetailId; }
			 * 
			 * 
			 * @Override public ReferralPatientDt getReferralPatientDtByReferaldtIdMe(Long
			 * referalDtId) { ReferralPatientDt referralPatientDt=null;
			 * 
			 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * 
			 * referralPatientDt=(ReferralPatientDt)
			 * session.createCriteria(ReferralPatientDt.class)
			 * .add(Restrictions.eq("refrealDtId", referalDtId)).uniqueResult(); }
			 * catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * referralPatientDt; }
			 * 
			 * @Override public ReferralPatientHd getPatientReferalHdByExtHospitalIdMe(Long
			 * extHospitalId,Long opdPatientDetailId,String referalFlagValue) {
			 * ReferralPatientHd referralPatientHd=null;
			 * List<ReferralPatientHd>listReferralPatientHd=null; //Transaction tx=null; try
			 * { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); Criterion cr1=null;
			 * if(StringUtils.isNotEmpty(referalFlagValue)&&
			 * referalFlagValue.equalsIgnoreCase("I")) {
			 * cr1=Restrictions.eq("intHospitalId", extHospitalId); } else {
			 * cr1=Restrictions.eq("extHospitalId", extHospitalId); } Criteria criteria =
			 * session.createCriteria(ReferralPatientHd.class). add(cr1)
			 * .add(Restrictions.eq("opdPatientDetailsId", opdPatientDetailId)) ;
			 * listReferralPatientHd=criteria.list();
			 * 
			 * if(CollectionUtils.isNotEmpty(listReferralPatientHd)) {
			 * referralPatientHd=listReferralPatientHd.get(0); } // tx.commit(); }
			 * catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * referralPatientHd; }
			 * 
			 * @Override public Long saveOrUpdateReferalHdMe(ReferralPatientHd
			 * referralPatientHd) { Session session=null; Long patientReferalHdId=null;
			 * //Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(referralPatientHd);
			 * patientReferalHdId=referralPatientHd.getRefrealHdId(); session.flush();
			 * session.clear(); //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return patientReferalHdId; }
			 * 
			 * 
			 * @Override public Long saveOrUpdateReferalDtMe(ReferralPatientDt
			 * referralPatientDt) { Session session=null; Long patientReferalDtId=null;
			 * //Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(referralPatientDt);
			 * patientReferalDtId=referralPatientDt.getRefrealDtId(); session.flush();
			 * session.clear(); //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return patientReferalDtId; }
			 * 
			 * 
			 * @Override public Map<String,Object>
			 * getMEWaitingGridAFMS18(HashMap<String,Object> jsonData) {
			 * List<Visit>listVisit=null; Map<String,Object>map=new HashMap<>(); int count =
			 * 0; Criterion cr1=null; Criterion cr2=null; Criterion cr3=null; Criterion
			 * cr4=null; Criterion cr5=null; Criterion cr6=null; String serviceNo=""; try {
			 * int pageNo = Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
			 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			 * String serviceNoForPatient=""; if(jsonData.get("serviceNoForPatient")!=null
			 * && StringUtils.isNotBlank(jsonData.get("serviceNoForPatient").toString())) {
			 * serviceNoForPatient= jsonData.get("serviceNoForPatient").toString(); }
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * Criteria criteria=session.createCriteria(Visit.class,"visit").createAlias(
			 * "visit.masAppointmentType", "masAppointmentType")
			 * .createAlias("visit.patient", "patient").createAlias("visit.masMedExam",
			 * "masMedExam"); cr1=Restrictions.eq("visit.examStatus", "V").ignoreCase();
			 * //cr4=Restrictions.eq("masMedExam.medicalExamCode",
			 * UtilityServices.getValueOfPropByKey("meForm18")).ignoreCase();
			 * cr4=Restrictions.or(Restrictions.eq("masMedExam.medicalExamCode",
			 * UtilityServices.getValueOfPropByKey("meForm18")).ignoreCase(),
			 * Restrictions.eq("masMedExam.medicalExamCode",
			 * UtilityServices.getValueOfPropByKey("mbForm16")).ignoreCase());
			 * cr5=Restrictions.isNull("visit.tokenNo");
			 * cr6=Restrictions.eq("patient.serviceNo",serviceNoForPatient).ignoreCase();
			 * criteria.add(cr1); criteria.add(cr4); criteria.add(cr5); criteria.add(cr6);
			 * if(jsonData.get("serviceNo")!=null) {
			 * serviceNo=jsonData.get("serviceNo").toString(); if (serviceNo != null &&
			 * !serviceNo.equals("") && !serviceNo.equals("null")) {
			 * cr2=Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase();
			 * criteria.add(cr2); }
			 * 
			 * } //cr3= Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * "ME").ignoreCase();
			 * cr3=Restrictions.or(Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * "ME").ignoreCase(), Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * "MB").ignoreCase()); criteria.add(cr3);
			 * 
			 * listVisit=criteria.list(); count = listVisit.size();
			 * 
			 * //criteria = criteria.setFirstResult(pagingSize * (pageNo - 1)); if
			 * (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {
			 * criteria = criteria.setFirstResult(pagingSize * (1 - 1)); } else { criteria =
			 * criteria.setFirstResult(pagingSize * (pageNo - 1)); } criteria =
			 * criteria.setMaxResults(pagingSize); criteria.setFirstResult((pagingSize) *
			 * (pageNo - 1)); criteria.setMaxResults(pagingSize); listVisit =
			 * criteria.list();
			 * 
			 * map.put("listVisit", listVisit); map.put("count", count);
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
			 * 
			 * return map; }
			 * 
			 * 
			 * 
			 * 
			 * @Override public Map<String,Object>
			 * getpatientDetailForAFMS18(HashMap<String,Object> jsonData) {
			 * List<Visit>listVisit=null; Map<String,Object>mapValue=new HashMap<>(); try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession(); String
			 * visitId = jsonData.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceString(visitId); Criteria
			 * criteria=session.createCriteria(Visit.class) .add(Restrictions.eq("visitId",
			 * Long.parseLong(visitId))); listVisit=criteria.list();
			 * mapValue.put("listVisit", listVisit); } catch(Exception e) {
			 * e.printStackTrace(); } return mapValue; }
			 * 
			 * @Override public String saveUpdateAllTransation(MasMedicalExamReport
			 * masMedicalExamReprt,
			 * 
			 * HashMap<String, Object> payload,String patientId, String hospitalId,String
			 * userId,String saveInDraft) { Transaction tx = null; String satusOfMessage =
			 * ""; Long medicaleExamId = 0l; String statusOfPatient=""; try {
			 * 
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
			 * session.beginTransaction(); if (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) { payload.put("digiFlag",
			 * "yes"); //saveOrUpdateMedicalCategory(payload);
			 * //saveOrUpdateMedicalCatComposite(payload); }
			 * 
			 * try { String medicalCompositeNameValue =""; if
			 * (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) {
			 * medicalCompositeNameValue=
			 * payload.get("medicalCompositeNameValue").toString();
			 * medicalCompositeNameValue =
			 * OpdServiceImpl.getReplaceString(medicalCompositeNameValue); String
			 * diagnosisiIdMC =""; if(payload.get("diagnosisiIdMC")!=null) { diagnosisiIdMC
			 * = payload.get("diagnosisiIdMC").toString(); diagnosisiIdMC =
			 * OpdServiceImpl.getReplaceString(diagnosisiIdMC); } String[]
			 * diagnosisiIdMCValues = null; String visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * if(StringUtils.isNotEmpty(diagnosisiIdMC)) { diagnosisiIdMCValues =
			 * diagnosisiIdMC.trim().split(","); } String fitFlagCheckValue="";
			 * if(payload.containsKey("fitFlagCheckValue")) {
			 * fitFlagCheckValue=payload.get("fitFlagCheckValue").toString();
			 * fitFlagCheckValue=OpdServiceImpl.getReplaceString(fitFlagCheckValue); }
			 * if(StringUtils.isEmpty(medicalCompositeNameValue) &&
			 * diagnosisiIdMCValues==null && (fitFlagCheckValue.equalsIgnoreCase("Yes")||
			 * fitFlagCheckValue.contains("Yes"))) {
			 * 
			 * Long masMedicalCategoryId=null; if(StringUtils.isNotEmpty(patientId)) {
			 * Patient patient= checkPatientForPatientId(Long.parseLong(patientId));
			 * 
			 * Criterion cr1=Restrictions.eq("fitFlag", "F").ignoreCase();
			 * List<MasMedicalCategory>listMasMedicalCategory=masMedicalCategoryDao.
			 * findByCriteria(cr1); if(CollectionUtils.isNotEmpty(listMasMedicalCategory)) {
			 * masMedicalCategoryId=listMasMedicalCategory.get(0).getMedicalCategoryId();
			 * patient.setMedicalCategoryId(masMedicalCategoryId); }
			 * patient.setFitFlag("F");
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * saveOrUpdatePatient(patient);
			 * 
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ;
			 * 
			 * if(CollectionUtils.isEmpty( listPatientMedicalCat)) { patientMedicalCat=new
			 * PatientMedicalCat();
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * //patientMedicalCat.setMedicalCategoryId(masMedicalCategoryId);//Say Krishna
			 * patientMedicalCat.setpMedCatId(masMedicalCategoryId);
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId)); String dateOfRelease =
			 * payload.get("dateOfRelease").toString(); dateOfRelease =
			 * OpdServiceImpl.getReplaceString(dateOfRelease);
			 * 
			 * if(StringUtils.isNotEmpty(dateOfRelease) && dateOfRelease!=null) { Date
			 * medicalCompositeDateValue = null;
			 * if(MedicalExamServiceImpl.checkDateFormat(dateOfRelease))
			 * medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfRelease.trim());
			 * if(medicalCompositeDateValue!=null) { Timestamp ts = new
			 * Timestamp(medicalCompositeDateValue.getTime());
			 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
			 * patientMedicalCat.setpMedFitFlag("F"); saveOrUpdatePatientMedicalCat(
			 * patientMedicalCat); }
			 * 
			 * } } else { if(StringUtils.isNotEmpty(patientId)&&
			 * StringUtils.isEmpty(medicalCompositeNameValue)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId)); patient.setFitFlag("U");
			 * patient.setMedicalCategoryId(null); if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * saveOrUpdatePatient(patient);
			 * 
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
			 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
			 * deletePatientMedCat(patientMedicalCat); }
			 * 
			 * } } } } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * 
			 * 
			 * if (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) { String
			 * obsistyCheckAlready =""; if(payload.get("obsistyCheckAlready")!=null) {
			 * obsistyCheckAlready = payload.get("obsistyCheckAlready").toString();
			 * obsistyCheckAlready = OpdServiceImpl.getReplaceString(obsistyCheckAlready); }
			 * if( !obsistyCheckAlready.equalsIgnoreCase("exits")) {
			 * 
			 * String obsistyMark = payload.get("obsistyMark").toString(); obsistyMark =
			 * OpdServiceImpl.getReplaceString(obsistyMark);
			 * if(!obsistyMark.equalsIgnoreCase("") &&
			 * !obsistyMark.equalsIgnoreCase("none")) {
			 * 
			 * saveUpdateOpdObsistyMe(payload) ; } }
			 * 
			 * } saveUpdatePatientServicesDetail( payload, patientId, userId, session);
			 * 
			 * saveUpdatePatientDiseaseInfo(payload, patientId, userId, session);
			 * 
			 * saveUpdatePatientDiseaseForBeforeJoined( payload, patientId, userId,
			 * session);
			 * 
			 * if (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) {
			 * masMedicalExamReprt=updateInvestigationDgResult(payload, patientId,
			 * hospitalId, userId, masMedicalExamReprt); } if
			 * (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("draftMa18") &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) {
			 * masMedicalExamReprt=saveUpdateForReferalforMe(masMedicalExamReprt,payload,
			 * hospitalId); masMedicalExamReprt = getActionForMedicalExam(payload,
			 * masMedicalExamReprt); } else {
			 * if(!saveInDraft.equalsIgnoreCase("portalDraftMa18")){ OpdPatientDetail
			 * opdPatientDetail=null; saveUpdateOpdVitalDetails(payload, opdPatientDetail,
			 * hospitalId, masMedicalExamReprt); } } if(StringUtils.isNotEmpty(saveInDraft)
			 * && saveInDraft.equalsIgnoreCase("portalDraftMa18")) {
			 * masMedicalExamReprt.setStatus("po"); } String meRmsId=""; String[]
			 * meRmsIds=null; if(!saveInDraft.equalsIgnoreCase("portalDraftMa18")){
			 * meRmsId=payload.get("meRmsId").toString();
			 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); meRmsIds =
			 * meRmsId.split(","); } if(meRmsIds!=null && meRmsIds.length>0 &&
			 * meRmsIds[0]!=null && !meRmsIds[0].equalsIgnoreCase("") &&
			 * !meRmsIds[0].equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRidcId(Long.parseLong(meRmsIds[0].toString())); }
			 * 
			 * String meRmsIdOld=""; String[] meRmsIdOlds=null;
			 * if(!saveInDraft.equalsIgnoreCase("portalDraftMa18")){
			 * meRmsIdOld=payload.get("meRmsIdOld").toString();
			 * meRmsIdOld=OpdServiceImpl.getReplaceString(meRmsIdOld); meRmsIdOlds =
			 * meRmsIdOld.split(","); } if(meRmsIdOlds!=null && meRmsIdOlds.length>0 &&
			 * meRmsIdOlds[0]!=null && !meRmsIdOlds[0].equalsIgnoreCase("") &&
			 * !meRmsIdOlds[0].equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setMeRidcId(Long.parseLong(meRmsIdOlds[0].toString())); }
			 * 
			 * //if(masMedicalExamReprt!=null) { session.saveOrUpdate(masMedicalExamReprt);
			 * if(masMedicalExamReprt!=null &&
			 * masMedicalExamReprt.getMedicalExaminationId()!=null) medicaleExamId
			 * =masMedicalExamReprt.getMedicalExaminationId();
			 * if(StringUtils.isNotEmpty(masMedicalExamReprt.getStatus())) {
			 * statusOfPatient=masMedicalExamReprt.getStatus(); } else {
			 * statusOfPatient="no status"; } //} session.flush(); session.clear();
			 * tx.commit(); satusOfMessage = medicaleExamId + "##" +
			 * "Medical Exam Submitted Successfully"+"##"+statusOfPatient; } catch
			 * (Exception e) { if (tx != null) { try { tx.rollback();
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; } catch (Exception re) { satusOfMessage = 0
			 * + "##" + "Data is not updated because something is wrong" +
			 * re.toString()+"##"+statusOfPatient; re.printStackTrace(); } } satusOfMessage
			 * = 0 + "##" + "Data is not updated because something is wrong" +
			 * e.toString()+"##"+statusOfPatient; e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * }
			 * 
			 * return satusOfMessage; }
			 * 
			 * 
			 * public Integer saveUpdatePatientServicesDetail(HashMap<String, Object>
			 * payload, String patientId, String userId,Session session) {
			 * //List<PatientServicesDetail> listPatientServicesDetail = null;
			 * 
			 * PatientServicesDetail patientServicesDetail = null; Integer counter = 1;
			 * //try { Boolean checkForCurrentUser=false;
			 * 
			 * String saveInDraft = payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft); if
			 * (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) { checkForCurrentUser=true;
			 * }
			 * 
			 * String serviceDetailId = payload.get("serviceDetailId").toString();
			 * serviceDetailId = OpdServiceImpl.getReplaceString(serviceDetailId); String[]
			 * serviceDetailIdValue = serviceDetailId.split(",");
			 * 
			 * String serviceDetailFrom = payload.get("serviceDetailFrom").toString();
			 * serviceDetailFrom = OpdServiceImpl.getReplaceString(serviceDetailFrom);
			 * String[] serviceDetailFromValue = serviceDetailFrom.split(",");
			 * 
			 * String serviceDetailTo = payload.get("serviceDetailTo").toString();
			 * serviceDetailTo = OpdServiceImpl.getReplaceString(serviceDetailTo); String[]
			 * serviceDetailToValue = serviceDetailTo.split(",");
			 * 
			 * String serviceDetailPlace = payload.get("serviceDetailPlace").toString();
			 * serviceDetailPlace = OpdServiceImpl.getReplaceString(serviceDetailPlace);
			 * String[] serviceDetailPlaceValue = serviceDetailPlace.split(",");
			 * 
			 * String serviceDetailPf = payload.get("serviceDetailPf").toString();
			 * serviceDetailPf = OpdServiceImpl.getReplaceString(serviceDetailPf); String[]
			 * serviceDetailPfValue = serviceDetailPf.split(",");
			 * 
			 * String finalValue = ""; HashMap<String, String> mapServiceDetailInfo = new
			 * HashMap<>(); for (int i = 0; i < serviceDetailFromValue.length; i++) {
			 * 
			 * if (i<serviceDetailFromValue.length &&
			 * StringUtils.isNotEmpty(serviceDetailFromValue[i].toString()) &&
			 * !serviceDetailFromValue[i].equalsIgnoreCase("0")) { finalValue +=
			 * serviceDetailFromValue[i].trim();
			 * 
			 * if (i<serviceDetailToValue.length &&
			 * !serviceDetailToValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(serviceDetailToValue[i])) { finalValue += "," +
			 * serviceDetailToValue[i].trim(); } else { finalValue += "," + "0"; }
			 * 
			 * if (i<serviceDetailPlaceValue.length &&
			 * !serviceDetailPlaceValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(serviceDetailPlaceValue[i])) {
			 * 
			 * finalValue += "," + serviceDetailPlaceValue[i].trim(); } else { finalValue +=
			 * "," + "0"; } if (i<serviceDetailPfValue.length &&
			 * !serviceDetailPfValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(serviceDetailPfValue[i])) {
			 * 
			 * finalValue += "," + serviceDetailPfValue[i].trim(); } else { finalValue +=
			 * "," + "0"; } if (i<serviceDetailIdValue.length &&
			 * !serviceDetailIdValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(serviceDetailIdValue[i])) {
			 * 
			 * finalValue += "," + serviceDetailIdValue[i].trim(); } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * 
			 * mapServiceDetailInfo.put(serviceDetailFromValue[i].trim() + "@#" + counter,
			 * finalValue); finalValue = ""; counter++; } }
			 * 
			 * counter = 1;
			 * 
			 * if (serviceDetailFromValue != null && serviceDetailFromValue.length > 0) {
			 * //listPatientServicesDetail = new ArrayList<>(); for (String objServiceDetail
			 * : serviceDetailFromValue) {
			 * 
			 * if (mapServiceDetailInfo.containsKey(objServiceDetail.trim() + "@#" +
			 * counter)) { String patientServiceDetailFrom = mapServiceDetailInfo
			 * .get(objServiceDetail.trim() + "@#" + counter); if
			 * (StringUtils.isNotEmpty(patientServiceDetailFrom)) { String[]
			 * finalValueServiceDetail = patientServiceDetailFrom.split(",");
			 * 
			 * if(finalValueServiceDetail[4]!=null &&
			 * StringUtils.isNotEmpty(finalValueServiceDetail[4]) &&
			 * !finalValueServiceDetail[0].equalsIgnoreCase("0") &&
			 * !finalValueServiceDetail[0].trim().equalsIgnoreCase("")) {
			 * patientServicesDetail=
			 * getServiceDetailsByServiceDetailId(Long.parseLong(finalValueServiceDetail[4].
			 * trim().toString())); if(patientServicesDetail==null) { patientServicesDetail
			 * = new PatientServicesDetail(); } } else { patientServicesDetail = new
			 * PatientServicesDetail(); } if (finalValueServiceDetail[0] != null &&
			 * !finalValueServiceDetail[0].equalsIgnoreCase("")) { Date
			 * fromDateServiceDetail = null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueServiceDetail[0]))
			 * fromDateServiceDetail = HMSUtil
			 * .convertStringTypeDateToDateType(finalValueServiceDetail[0].trim());
			 * if(fromDateServiceDetail!=null)
			 * patientServicesDetail.setFromDate(fromDateServiceDetail); }
			 * 
			 * if (finalValueServiceDetail[1] != null &&
			 * !finalValueServiceDetail[1].equalsIgnoreCase("")) { Date toDateServiceDetail
			 * = null; if(
			 * MedicalExamServiceImpl.checkDateFormat(finalValueServiceDetail[1].trim()))
			 * toDateServiceDetail = HMSUtil
			 * .convertStringTypeDateToDateType(finalValueServiceDetail[1].trim());
			 * if(toDateServiceDetail!=null)
			 * patientServicesDetail.setToDate(toDateServiceDetail); }
			 * 
			 * if (finalValueServiceDetail[2] != null &&
			 * !finalValueServiceDetail[2].equalsIgnoreCase("")) {
			 * patientServicesDetail.setPlace(finalValueServiceDetail[2]); } if
			 * (finalValueServiceDetail[3] != null &&
			 * !finalValueServiceDetail[3].equalsIgnoreCase("")) {
			 * patientServicesDetail.setPf(finalValueServiceDetail[3]); } if
			 * (StringUtils.isNotEmpty(patientId)) {
			 * patientServicesDetail.setPatientId(Long.parseLong(patientId)); }
			 * 
			 * if (StringUtils.isNotEmpty(userId) && checkForCurrentUser)
			 * patientServicesDetail.setLastChgBy(Long.parseLong(userId)); Date date = new
			 * Date(); patientServicesDetail.setLastChgDate(new Timestamp(date.getTime()));
			 * 
			 * session.saveOrUpdate(patientServicesDetail);
			 * //listPatientServicesDetail.add(patientServicesDetail);
			 * 
			 * } } counter += 1; } } } catch (Exception e) { e.printStackTrace(); }
			 * 
			 * return counter;
			 * 
			 * } public Integer saveUpdatePatientDiseaseInfo(HashMap<String, Object>
			 * payload, String patientId, String userId,Session session) {
			 * //List<PatientDiseaseInfo> listPatientDiseaseInfo = null; PatientDiseaseInfo
			 * patientDiseaseInfo = null; Integer counter = 1; //try { // Disease,Wound Or
			 * Injury Details
			 * 
			 * Boolean checkForCurrentUser=false; String saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft); if
			 * (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) { checkForCurrentUser=true;
			 * } String patientDiagnosisId = payload.get("patientDiagnosisId").toString();
			 * patientDiagnosisId = OpdServiceImpl.getReplaceString(patientDiagnosisId);
			 * 
			 * String[] patientDiagnosisIdV = patientDiagnosisId.split(",");
			 * 
			 * 
			 * String icdDiagnaosis = payload.get("icdDiagnaosis").toString(); icdDiagnaosis
			 * = OpdServiceImpl.getReplaceString(icdDiagnaosis);
			 * 
			 * String[] icdDiagnaosisValue = icdDiagnaosis.split(",");
			 * 
			 * String firstStartedDate = payload.get("firstStartedDate").toString();
			 * firstStartedDate = OpdServiceImpl.getReplaceString(firstStartedDate);
			 * String[] firstStartedDateValue = firstStartedDate.split(",");
			 * 
			 * String firstStartedPlace = payload.get("firstStartedPlace").toString();
			 * firstStartedPlace = OpdServiceImpl.getReplaceString(firstStartedPlace);
			 * String[] firstStartedPlaceValue = firstStartedPlace.split(",");
			 * 
			 * String whereTreated = payload.get("whereTreated").toString(); whereTreated =
			 * OpdServiceImpl.getReplaceString(whereTreated); String[] whereTreatedValue =
			 * whereTreated.split(",");
			 * 
			 * String approximatePeriodsTreatedFrom =
			 * payload.get("approximatePeriodsTreatedFrom").toString();
			 * approximatePeriodsTreatedFrom =
			 * OpdServiceImpl.getReplaceString(approximatePeriodsTreatedFrom); String[]
			 * approximatePeriodsTreatedFromValue =
			 * approximatePeriodsTreatedFrom.split(",");
			 * 
			 * String approximatePeriodsTreatedTo =
			 * payload.get("approximatePeriodsTreatedTo").toString();
			 * approximatePeriodsTreatedTo =
			 * OpdServiceImpl.getReplaceString(approximatePeriodsTreatedTo); String[]
			 * approximatePeriodsTreatedToValue = approximatePeriodsTreatedTo.split(",");
			 * 
			 * 
			 * String finalValue = ""; HashMap<String, String> mapPatientDiseaseInfo = new
			 * HashMap<>(); for (int i = 0; i < icdDiagnaosisValue.length; i++) {
			 * 
			 * if (i<icdDiagnaosisValue.length &&
			 * StringUtils.isNotEmpty(icdDiagnaosisValue[i].toString()) &&
			 * !icdDiagnaosisValue[i].equalsIgnoreCase("0")) { finalValue +=
			 * icdDiagnaosisValue[i].trim();
			 * 
			 * if (i<firstStartedDateValue.length &&
			 * !firstStartedDateValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(firstStartedDateValue[i])) { finalValue += "," +
			 * firstStartedDateValue[i].trim(); } else { finalValue += "," + "0"; }
			 * 
			 * if (i<firstStartedPlaceValue.length &&
			 * !firstStartedPlaceValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(firstStartedPlaceValue[i])) {
			 * 
			 * finalValue += "," + firstStartedPlaceValue[i].trim(); } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * if (i<whereTreatedValue.length && !whereTreatedValue[i].equalsIgnoreCase("0")
			 * && StringUtils.isNotBlank(whereTreatedValue[i])) { finalValue += "," +
			 * whereTreatedValue[i].trim(); } else { finalValue += "," + "0"; }
			 * 
			 * if (i<approximatePeriodsTreatedFromValue.length &&
			 * !approximatePeriodsTreatedFromValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(approximatePeriodsTreatedFromValue[i])) { finalValue
			 * += "," + approximatePeriodsTreatedFromValue[i].trim(); } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * if (i<approximatePeriodsTreatedToValue.length &&
			 * !approximatePeriodsTreatedToValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(approximatePeriodsTreatedToValue[i])) { finalValue +=
			 * "," + approximatePeriodsTreatedToValue[i].trim(); } else { finalValue += ","
			 * + "0"; } if (i<patientDiagnosisIdV.length &&
			 * !patientDiagnosisIdV[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(patientDiagnosisIdV[i])) { finalValue += "," +
			 * patientDiagnosisIdV[i].trim(); } else { finalValue += "," + "0"; }
			 * 
			 * mapPatientDiseaseInfo.put(icdDiagnaosisValue[i].trim() + "@#" + counter,
			 * finalValue); finalValue = ""; counter++; } }
			 * 
			 * counter = 1;
			 * 
			 * if (icdDiagnaosisValue != null && icdDiagnaosisValue.length > 0) {
			 * //listPatientDiseaseInfo = new ArrayList<>(); for (String icdDiagnosis :
			 * icdDiagnaosisValue) { if
			 * (mapPatientDiseaseInfo.containsKey(icdDiagnosis.trim() + "@#" + counter)) {
			 * String patientDiagonosisAllValue = mapPatientDiseaseInfo
			 * .get(icdDiagnosis.trim() + "@#" + counter); if
			 * (StringUtils.isNotEmpty(patientDiagonosisAllValue)) {
			 * 
			 * String[] finalValuepatientDiagonosis = patientDiagonosisAllValue.split(",");
			 * 
			 * if(StringUtils.isNotEmpty(finalValuepatientDiagonosis[6])&&
			 * !finalValuepatientDiagonosis[6].equalsIgnoreCase("0")) { patientDiseaseInfo=
			 * getPatientDiseaseWoundInjuryDetailByDiseaseInfoId(Long.parseLong(
			 * finalValuepatientDiagonosis[6].trim().toString()));
			 * if(patientDiseaseInfo==null) { patientDiseaseInfo = new PatientDiseaseInfo();
			 * } } else { patientDiseaseInfo = new PatientDiseaseInfo(); } if
			 * (finalValuepatientDiagonosis[0] != null &&
			 * !finalValuepatientDiagonosis[0].equalsIgnoreCase("")) {
			 * patientDiseaseInfo.setIcdId(Long.parseLong(finalValuepatientDiagonosis[0]));
			 * } patientDiseaseInfo.setBeforeFlag("N");
			 * 
			 * if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[1]) &&
			 * !finalValuepatientDiagonosis[1].equalsIgnoreCase("")) { Date firstStartDate =
			 * null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValuepatientDiagonosis[1].trim
			 * ())) firstStartDate = HMSUtil
			 * .convertStringTypeDateToDateType(finalValuepatientDiagonosis[1].trim());
			 * if(firstStartDate!=null) patientDiseaseInfo.setStDate(firstStartDate); }
			 * 
			 * if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[2])) {
			 * patientDiseaseInfo.setStPlace(finalValuepatientDiagonosis[2]); }
			 * 
			 * if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[3])) {
			 * patientDiseaseInfo.setTreatedPlace(finalValuepatientDiagonosis[3]); }
			 * 
			 * if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[4])) { Date
			 * treatedDate = null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValuepatientDiagonosis[4].trim
			 * ())) treatedDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValuepatientDiagonosis[4].trim()
			 * ); if(treatedDate!=null) patientDiseaseInfo.setFromDate(treatedDate); }
			 * 
			 * if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[5]) &&
			 * !finalValuepatientDiagonosis[5].equalsIgnoreCase("")) { Date treatedTo =
			 * null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValuepatientDiagonosis[5].trim
			 * ())) treatedTo =
			 * HMSUtil.convertStringTypeDateToDateType(finalValuepatientDiagonosis[5].trim()
			 * ); if(treatedTo!=null) patientDiseaseInfo.setToDate(treatedTo); } if
			 * (StringUtils.isNotEmpty(userId) && checkForCurrentUser)
			 * patientDiseaseInfo.setLastChgBy(Long.parseLong(userId)); Date date = new
			 * Date(); patientDiseaseInfo.setLastChgDate(new Timestamp(date.getTime()));
			 * patientDiseaseInfo.setPatientId(Long.parseLong(patientId));
			 * session.saveOrUpdate(patientDiseaseInfo);
			 * //listPatientDiseaseInfo.add(patientDiseaseInfo);
			 * 
			 * } } counter += 1; }
			 * 
			 * }
			 * 
			 * } catch (Exception e) { e.printStackTrace(); } return counter; }
			 * 
			 * 
			 * public Integer saveUpdatePatientDiseaseForBeforeJoined(HashMap<String,
			 * Object> payload, String patientId, String userId,Session session) {
			 * List<PatientDiseaseInfo> listPatientDiseaseInfo = null; Integer counter = 1;
			 * // try {
			 * 
			 * Boolean checkForCurrentUser=false; String saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft); if
			 * (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("portalDraftMa18")) { checkForCurrentUser=true;
			 * }
			 * 
			 * PatientDiseaseInfo patientDiseaseInfo = null; String disabilityBeforeJoining
			 * = payload.get("disabilityBeforeJoining").toString(); disabilityBeforeJoining
			 * = OpdServiceImpl.getReplaceString(disabilityBeforeJoining); if
			 * (StringUtils.isNotEmpty(disabilityBeforeJoining) &&
			 * disabilityBeforeJoining.equalsIgnoreCase("Yes")) {
			 * 
			 * String armedForcesPatientDiagnosisId =
			 * payload.get("armedForcesPatientDiagnosisId").toString();
			 * armedForcesPatientDiagnosisId =
			 * OpdServiceImpl.getReplaceString(armedForcesPatientDiagnosisId); String[]
			 * armedForcesPatientDiagnosisIdValue =
			 * armedForcesPatientDiagnosisId.split(",");
			 * 
			 * String armedForcesFrom = payload.get("armedForcesFrom").toString();
			 * armedForcesFrom = OpdServiceImpl.getReplaceString(armedForcesFrom); String[]
			 * armedForcesFromValue = armedForcesFrom.split(",");
			 * 
			 * String armedForcesTo = payload.get("armedForcesTo").toString(); armedForcesTo
			 * = OpdServiceImpl.getReplaceString(armedForcesTo); String[] armedForcesToValue
			 * = armedForcesTo.split(",");
			 * 
			 * String armedForcesDetails = payload.get("armedForcesDetails").toString();
			 * armedForcesDetails = OpdServiceImpl.getReplaceString(armedForcesDetails);
			 * String[] armedForcesDetailsValue = armedForcesDetails.split(",");
			 * listPatientDiseaseInfo = new ArrayList<>();
			 * 
			 * 
			 * String finalValue = ""; HashMap<String, String> mapPatientDiseaseInfo = new
			 * HashMap<>(); for (int i = 0; i < armedForcesFromValue.length; i++) {
			 * 
			 * if (i<armedForcesFromValue.length &&
			 * StringUtils.isNotEmpty(armedForcesFromValue[i].toString()) &&
			 * !armedForcesFromValue[i].equalsIgnoreCase("0")) { finalValue +=
			 * armedForcesFromValue[i].trim();
			 * 
			 * if (i<armedForcesToValue.length &&
			 * !armedForcesToValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(armedForcesToValue[i])) { finalValue += "," +
			 * armedForcesToValue[i].trim(); } else { finalValue += "," + "0"; }
			 * 
			 * if (i<armedForcesDetailsValue.length &&
			 * !armedForcesDetailsValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(armedForcesDetailsValue[i])) {
			 * 
			 * finalValue += "," + armedForcesDetailsValue[i].trim(); } else { finalValue +=
			 * "," + "0"; }
			 * 
			 * if (i<armedForcesPatientDiagnosisIdValue.length &&
			 * !armedForcesPatientDiagnosisIdValue[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(armedForcesPatientDiagnosisIdValue[i])) {
			 * 
			 * finalValue += "," + armedForcesPatientDiagnosisIdValue[i].trim(); } else {
			 * finalValue += "," + "0"; }
			 * 
			 * mapPatientDiseaseInfo.put(armedForcesFromValue[i].trim() + "@#" + counter,
			 * finalValue); finalValue = ""; counter++; } }
			 * 
			 * counter = 1; for (String objArmedForcesFrom : armedForcesFromValue) { if
			 * (StringUtils.isNotEmpty(objArmedForcesFrom)) { patientDiseaseInfo = new
			 * PatientDiseaseInfo(); if
			 * (mapPatientDiseaseInfo.containsKey(objArmedForcesFrom.trim() + "@#" +
			 * counter)) { String patientArmedForcesFrom = mapPatientDiseaseInfo
			 * .get(objArmedForcesFrom.trim() + "@#" + counter); if
			 * (StringUtils.isNotEmpty(patientArmedForcesFrom)) { String[]
			 * finalValuepatientArmedForces = patientArmedForcesFrom.split(",");
			 * 
			 * if(StringUtils.isNotEmpty(finalValuepatientArmedForces[3])&&
			 * !finalValuepatientArmedForces[3].equalsIgnoreCase("0")) { patientDiseaseInfo=
			 * getPatientDiseaseWoundInjuryDetailByDiseaseInfoId(Long.parseLong(
			 * finalValuepatientArmedForces[3].trim().toString()));
			 * if(patientDiseaseInfo==null) { patientDiseaseInfo = new PatientDiseaseInfo();
			 * } } else { patientDiseaseInfo = new PatientDiseaseInfo(); }
			 * 
			 * if (finalValuepatientArmedForces[0] != null &&
			 * !finalValuepatientArmedForces[0].equalsIgnoreCase("")) { Date
			 * armedForcesFromV = null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValuepatientArmedForces[0].
			 * trim())) armedForcesFromV = HMSUtil
			 * .convertStringTypeDateToDateType(finalValuepatientArmedForces[0].trim());
			 * if(armedForcesFromV!=null) patientDiseaseInfo.setFromDate(armedForcesFromV);
			 * }
			 * 
			 * if (finalValuepatientArmedForces[1] != null &&
			 * !finalValuepatientArmedForces[1].equalsIgnoreCase("")) { Date armedForcesToV
			 * = null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValuepatientArmedForces[1].
			 * trim())) armedForcesToV = HMSUtil
			 * .convertStringTypeDateToDateType(finalValuepatientArmedForces[1].trim());
			 * if(armedForcesToV!=null) patientDiseaseInfo.setToDate(armedForcesToV); } if
			 * (finalValuepatientArmedForces[2] != null &&
			 * !finalValuepatientArmedForces[2].equalsIgnoreCase("")) {
			 * patientDiseaseInfo.setRemarks(finalValuepatientArmedForces[2]); }
			 * patientDiseaseInfo.setBeforeFlag("Y"); if (StringUtils.isNotEmpty(userId) &&
			 * checkForCurrentUser) patientDiseaseInfo.setLastChgBy(Long.parseLong(userId));
			 * Date date = new Date(); patientDiseaseInfo.setLastChgDate(new
			 * Timestamp(date.getTime()));
			 * patientDiseaseInfo.setPatientId(Long.parseLong(patientId));
			 * patientDiseaseInfo.setIcdId(null);
			 * //listPatientDiseaseInfo.add(patientDiseaseInfo);
			 * session.saveOrUpdate(patientDiseaseInfo); } }
			 * 
			 * } counter += 1; } } } catch (Exception e) { e.printStackTrace(); } //return
			 * listPatientDiseaseInfo; return counter; }
			 * 
			 * 
			 * 
			 * 
			 * @Override public List<PatientServicesDetail> getServiceDetails(Long
			 * patientId) { List<PatientServicesDetail>listPatientServicesDetail=null; try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * Criteria cr1=
			 * session.createCriteria(PatientServicesDetail.class).add(Restrictions.eq(
			 * "patientId", patientId)).addOrder(Order.asc("serviceDetailsId")) ;
			 * listPatientServicesDetail=cr1.list();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * listPatientServicesDetail; }
			 * 
			 * @Override public List<PatientDiseaseInfo>
			 * getPatientDiseaseWoundInjuryDetail(Long patientId) { List<PatientDiseaseInfo>
			 * listPatientDiseaseInfo=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr1=
			 * session.createCriteria(PatientDiseaseInfo.class).add(Restrictions.eq(
			 * "patientId", patientId)).addOrder(Order.asc("diseaseInfoId"));
			 * listPatientDiseaseInfo=cr1.list();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * listPatientDiseaseInfo; }
			 * 
			 * 
			 * @Override public PatientServicesDetail
			 * getServiceDetailsByServiceDetailId(Long serviceDetailId) {
			 * PatientServicesDetail patientServicesDetail=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); patientServicesDetail=
			 * (PatientServicesDetail)
			 * session.createCriteria(PatientServicesDetail.class).add(Restrictions.eq(
			 * "serviceDetailsId", serviceDetailId)).uniqueResult() ;
			 * 
			 * } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return patientServicesDetail; }
			 * 
			 * @Override public PatientDiseaseInfo
			 * getPatientDiseaseWoundInjuryDetailByDiseaseInfoId(Long diseaseInfoId) {
			 * PatientDiseaseInfo patientDiseaseInfo=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); patientDiseaseInfo=
			 * (PatientDiseaseInfo)
			 * session.createCriteria(PatientDiseaseInfo.class).add(Restrictions.eq(
			 * "diseaseInfoId", diseaseInfoId)).uniqueResult() ;
			 * 
			 * } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return patientDiseaseInfo; }
			 * 
			 * @Override public Integer getInvestigationResultEmpty(Long visitId) {
			 * List<Object[]> listObject=null; Integer count=0; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); StringBuilder sbQuery =
			 * new StringBuilder(); // sbQuery.
			 * append(" select dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME,ohd.ORDERHD_ID,odt.LAB_MARK,odt.urgent,odt.ORDER_DATE, ohd.VISIT_ID,ohd.OTHER_INVESTIGATION,"
			 * ); //sbQuery.append(
			 * "ohd.DEPARTMENT_ID,ohd.HOSPITAL_ID,odt.ORDERDT_ID,rdt.RESULT_ENTRY_DT_ID,rht.RESULT_ENTRY_HD_ID,rdt.RESULT from  DG_ORDER_HD ohd "
			 * ); // sbQuery.
			 * append(" join DG_ORDER_DT odt on  ohd.orderhd_id=odt.orderhd_id join DG_MAS_INVESTIGATION "
			 * );
			 * //sbQuery.append(" dgmas on dgmas.INVESTIGATION_ID=odt.INVESTIGATION_ID ");
			 * // sbQuery.
			 * append(" left join DG_RESULT_ENTRY_DT rdt on odt.ORDERDT_ID=rdt.ORDERDT_ID left join DG_RESULT_ENTRY_HD rht on ohd.ORDERHD_ID= rht.ORDERHD_ID "
			 * ); sbQuery.append(" select odt.ORDERDT_ID ,odt.ORDER_DATE from ");
			 * sbQuery.append( DG_ORDER_HD +" ohd "); sbQuery.append(" join "
			 * +DG_ORDER_DT+" odt on  ohd.orderhd_id=odt.orderhd_id  "); //
			 * sbQuery.append("   DG_ORDER_DT odt   ");
			 * //sbQuery.append(" where  rdt.result IS NULL and ohd.VISIT_ID =:visitId");
			 * sbQuery.append(" where  odt.ORDER_STATUS='P'  and ohd.VISIT_ID =:visitId ");
			 * Query query = session.createSQLQuery(sbQuery.toString());
			 * 
			 * query.setParameter("visitId", visitId);
			 * 
			 * listObject = query.list(); if(CollectionUtils.isNotEmpty(listObject)) {
			 * count=listObject.size(); } } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return count; }
			 * 
			 * @Override public List<MasUnit> getMasUnitByUnitCode(String unitCode) {
			 * List<MasUnit> listMasUnit=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr1 =
			 * session.createCriteria(MasUnit.class)
			 * .add(Restrictions.eq("unitCode",unitCode)) ; listMasUnit=cr1.list();
			 * 
			 * }catch (Exception e) { //
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
			 * } return listMasUnit; }
			 * 
			 * 
			 * @Override public List<DgMasInvestigation> getInvestigationListUOM(String
			 * mainChargeCode,HashMap<String, Object> jsondata) { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Long mainChargeId=null;
			 * if(!mainChargeCode.equals("") && mainChargeCode!=null) { MasMainChargecode
			 * mmcc=null; mmcc = opdMasterDao.getMainChargeCode(mainChargeCode);
			 * mainChargeId=mmcc.getMainChargecodeId(); System.out.println("mainChargeId :"
			 * +mainChargeId);
			 * 
			 * }
			 * 
			 * Criteria cr =
			 * session.createCriteria(DgMasInvestigation.class,"dgMasInvestigation").
			 * createAlias("dgMasInvestigation.masUOM", "masUOM", JoinType.LEFT_OUTER_JOIN);
			 * if(!mainChargeCode.equals("") && mainChargeCode !=null) {
			 * cr.add(Restrictions.eq("mainChargecodeId", mainChargeId)); }
			 * cr.add(Restrictions.eq("status", "Y").ignoreCase());
			 * if(jsondata.containsKey("icdName")&& jsondata.get("icdName")!="") { String
			 * icdName=(String) jsondata.get("icdName");
			 * cr.add(Restrictions.ilike("investigationName", "%"+icdName+"%")); }
			 * ProjectionList projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("investigationId").as(
			 * "investigationId"));
			 * projectionList.add(Projections.property("investigationName").as(
			 * "investigationName"));
			 * projectionList.add(Projections.property("investigationType").as(
			 * "investigationType"));
			 * projectionList.add(Projections.property("masUOM.UOMName").as("UOMName"));
			 * projectionList.add(Projections.property("masUOM.UOMCode").as("UOMCode"));
			 * projectionList.add(Projections.property("dgMasInvestigation.minNormalValue").
			 * as("minNormalValue"));
			 * projectionList.add(Projections.property("dgMasInvestigation.maxNormalValue").
			 * as("maxNormalValue"));
			 * projectionList.add(Projections.property("subChargecodeId").as(
			 * "subChargecodeId"));
			 * projectionList.add(Projections.property("mainChargecodeId").as(
			 * "mainChargecodeId")); cr.setProjection(projectionList);
			 * if(jsondata.containsKey("icdName")&& jsondata.get("icdName")!="") {
			 * cr.setFirstResult(0); cr.setMaxResults(10); }
			 * 
			 * List<DgMasInvestigation> list = cr .setResultTransformer(new
			 * AliasToBeanResultTransformer(DgMasInvestigation.class)).list();
			 * 
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * List<DgMasInvestigation> list = cr.list(); return list;
			 * 
			 * }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public String saveOrUpdateMedicalExam3A(MasMedicalExamReport
			 * masMedicalExamReprt,HashMap<String, Object> payload,String patientId, String
			 * hospitalId,String userId) { Session session=null; Long
			 * medicalExaminationId=null; Transaction tx=null; String satusOfMessage="";
			 * String statusOfPatient=""; Timestamp ts =null;
			 * 
			 * try{ session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * tx=session.beginTransaction(); String obsistyCheckAlready ="";
			 * if(payload.get("obsistyCheckAlready")!=null) { obsistyCheckAlready =
			 * payload.get("obsistyCheckAlready").toString(); obsistyCheckAlready =
			 * OpdServiceImpl.getReplaceString(obsistyCheckAlready); } if(
			 * !obsistyCheckAlready.equalsIgnoreCase("exits")) {
			 * 
			 * String obsistyMark = payload.get("obsistyMark").toString(); obsistyMark =
			 * OpdServiceImpl.getReplaceString(obsistyMark);
			 * if(!obsistyMark.equalsIgnoreCase("") &&
			 * !obsistyMark.equalsIgnoreCase("none")) {
			 * 
			 * saveUpdateOpdObsistyMe(payload) ; } } payload.put("digiFlag", "yes");
			 * saveOrUpdateMedicalCategory(payload);
			 * saveOrUpdateMedicalCatComposite(payload);
			 * 
			 * /////////////////////////////////////////////////// Long
			 * checkPatientUpdate=0l;
			 * 
			 * try { String medicalCompositeNameValue =
			 * payload.get("medicalCompositeNameValue").toString();
			 * medicalCompositeNameValue =
			 * OpdServiceImpl.getReplaceString(medicalCompositeNameValue);
			 * 
			 * String diagnosisiIdMC = payload.get("diagnosisiIdMC").toString();
			 * diagnosisiIdMC = OpdServiceImpl.getReplaceString(diagnosisiIdMC); String[]
			 * diagnosisiIdMCValues = null; String visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * if(StringUtils.isNotEmpty(diagnosisiIdMC)) { diagnosisiIdMCValues =
			 * diagnosisiIdMC.trim().split(","); } String fitFlagCheckValue="";
			 * if(payload.containsKey("fitFlagCheckValue")) {
			 * fitFlagCheckValue=payload.get("fitFlagCheckValue").toString();
			 * fitFlagCheckValue=OpdServiceImpl.getReplaceString(fitFlagCheckValue); }
			 * if(StringUtils.isEmpty(medicalCompositeNameValue) &&
			 * diagnosisiIdMCValues==null && StringUtils.isNotEmpty(fitFlagCheckValue) &&
			 * (fitFlagCheckValue.equalsIgnoreCase("Yes")||
			 * fitFlagCheckValue.contains("Yes"))) { Long masMedicalCategoryId=null;
			 * if(StringUtils.isNotEmpty(patientId)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId));
			 * 
			 * Criterion cr1=Restrictions.eq("fitFlag", "F").ignoreCase();
			 * List<MasMedicalCategory>listMasMedicalCategory=masMedicalCategoryDao.
			 * findByCriteria(cr1); if(CollectionUtils.isNotEmpty(listMasMedicalCategory)) {
			 * masMedicalCategoryId=listMasMedicalCategory.get(0).getMedicalCategoryId();
			 * patient.setMedicalCategoryId(masMedicalCategoryId); }
			 * patient.setFitFlag("F"); if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * saveOrUpdatePatient(patient);
			 * 
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ;
			 * 
			 * if(CollectionUtils.isEmpty( listPatientMedicalCat)) { patientMedicalCat=new
			 * PatientMedicalCat(); if(StringUtils.isNotEmpty(patientId))
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * if(masMedicalCategoryId!=null)
			 * //patientMedicalCat.setMedicalCategoryId(masMedicalCategoryId);
			 * patientMedicalCat.setpMedCatId(masMedicalCategoryId);
			 * if(StringUtils.isNotEmpty(visitId))
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId)); String dateOfExam ="";
			 * if(payload.containsKey("dateOfExam")) { dateOfExam =
			 * payload.get("dateOfExam").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam); }
			 * 
			 * if(StringUtils.isNotEmpty(dateOfExam) && dateOfExam!=null &&
			 * !dateOfExam.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date
			 * medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(medicalCompositeDateValue!=null) { ts = new
			 * Timestamp(medicalCompositeDateValue.getTime());
			 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
			 * patientMedicalCat.setpMedFitFlag("F"); saveOrUpdatePatientMedicalCat(
			 * patientMedicalCat); }
			 * 
			 * } }
			 * 
			 * else { if(StringUtils.isNotEmpty(patientId)&&
			 * StringUtils.isEmpty(medicalCompositeNameValue)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId)); patient.setFitFlag("U");
			 * patient.setMedicalCategoryId(null);
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * 
			 * checkPatientUpdate++;
			 * 
			 * saveOrUpdatePatient(patient);
			 * 
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
			 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
			 * deletePatientMedCat(patientMedicalCat); }
			 * 
			 * 
			 * } } } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * //////////////////////////////////////////////
			 * 
			 * 
			 * 
			 * masMedicalExamReprt=updateInvestigationDgResult(payload, patientId,
			 * hospitalId, userId, masMedicalExamReprt);
			 * 
			 * saveUpdatePatientImmunizationHistory(payload);
			 * 
			 * String saveInDraft = payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft);
			 * 
			 * 
			 * if (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("draftMa3A")) {
			 * masMedicalExamReprt=saveUpdateForReferalforMe(masMedicalExamReprt,payload,
			 * hospitalId); masMedicalExamReprt = getActionForMedicalExam(payload,
			 * masMedicalExamReprt); } else { OpdPatientDetail opdPatientDetail=null;
			 * saveUpdateOpdVitalDetails(payload, opdPatientDetail, hospitalId,
			 * masMedicalExamReprt); }
			 * 
			 * String meRmsId=payload.get("meRmsId").toString();
			 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
			 * meRmsId.split(",");
			 * 
			 * if(meRmsIds!=null && meRmsIds.length>0 && meRmsIds[0]!=null &&
			 * !meRmsIds[0].equalsIgnoreCase("") && !meRmsIds[0].equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRidcId(Long.parseLong(meRmsIds[0].toString())); }
			 * 
			 * String meRmsIdOld=payload.get("meRmsIdOld").toString();
			 * meRmsId=OpdServiceImpl.getReplaceString(meRmsIdOld); String[] meRmsIdOlds =
			 * meRmsIdOld.split(",");
			 * 
			 * if(meRmsIdOlds!=null && meRmsIdOlds.length>0 && meRmsIdOlds[0]!=null &&
			 * !meRmsIdOlds[0].equalsIgnoreCase("") &&
			 * !meRmsIdOlds[0].equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setMeRidcId(Long.parseLong(meRmsIdOlds[0].toString())); }
			 * 
			 * session.saveOrUpdate(masMedicalExamReprt);
			 * 
			 * medicalExaminationId=masMedicalExamReprt.getMedicalExaminationId();
			 * if(masMedicalExamReprt!=null &&
			 * StringUtils.isNotEmpty(masMedicalExamReprt.getStatus())) {
			 * statusOfPatient=masMedicalExamReprt.getStatus(); } else {
			 * statusOfPatient="no status"; } session.flush(); session.clear(); tx.commit();
			 * if(medicalExaminationId!=null && medicalExaminationId!=0) {
			 * satusOfMessage=medicalExaminationId+
			 * "##"+"Medical Exam Submitted Successfully"+"##"+statusOfPatient; } else {
			 * satusOfMessage=0+
			 * "##"+"Data is not updated because column size exceed of actual size."+"##"+
			 * statusOfPatient; } } catch(Exception e) { if (tx != null) { try {
			 * tx.rollback();
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; } catch(Exception re) {
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+re.
			 * toString()+"##"+statusOfPatient; re.printStackTrace(); } }
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return satusOfMessage; }
			 * 
			 * 
			 * @Override public OpdPatientHistory
			 * checkVisitOpdPatientHistoryByVisitIdAndPatientId(Long visitId) { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
			 * session.createCriteria(OpdPatientHistory.class);
			 * cr.add(Restrictions.eq("visitId", visitId)); OpdPatientHistory
			 * opdPatientHistory = (OpdPatientHistory) cr.uniqueResult(); return
			 * opdPatientHistory; }
			 * 
			 * @Override public Long saveOrUpdateOpdPatientHis(OpdPatientHistory
			 * opdPatientHistory) { Long opdPatientHistoryId=null; Session session=null;
			 * //Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * session.saveOrUpdate(opdPatientHistory);
			 * opdPatientHistoryId=opdPatientHistory.getOpdPatientHistoryId();
			 * session.flush(); session.clear();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * 
			 * } return opdPatientHistoryId; }
			 * 
			 * 
			 * @Override public Users getUserByUserId(String serviceNumber) { Users
			 * uers=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); uers= (Users)
			 * session.createCriteria(Users.class).add(Restrictions.eq("serviceNo",
			 * serviceNumber).ignoreCase()).uniqueResult(); } catch(Exception e) {
			 * e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return uers; }
			 * 
			 * @Override public List<Object[]> getMasStoreItemByItemCode() { Long itemId =
			 * null; Long sectionId=null; Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); String pvmsNo =
			 * UtilityServices.getValueOfPropByKey("pvmsNo"); String[] pvmsNoValue = null;
			 * if (StringUtils.isNotEmpty(pvmsNo)) { pvmsNoValue = pvmsNo.split(","); }
			 * Criteria cr = session.createCriteria(MasStoreItem.class,"masStoreItem");
			 * cr.add(Restrictions.in("pvmsNo", pvmsNoValue)); ProjectionList projectionList
			 * = Projections.projectionList();
			 * projectionList.add(Projections.property("masStoreItem.itemId").as("itemId"));
			 * projectionList.add(Projections.property("masStoreItem.pvmsNo").as("pvmsNo"));
			 * projectionList.add(Projections.property("masStoreItem.nomenclature").as(
			 * "nomenclature"));
			 * //projectionList.add(Projections.property("mh.storeUnitName").as("dispUnitId"
			 * ));
			 * //projectionList.add(Projections.property("mh.storeUnitName").as("itemUnitId"
			 * )); //set store unit id(Accounting unit
			 * 
			 * cr.setProjection(projectionList); List<Object[]> list = cr.list();
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
			 * 
			 * @SuppressWarnings("unchecked")
			 * 
			 * @Override public Map<String,Object>
			 * getAllCompleteMedicalExam(HashMap<String,Object> jsonData) {
			 * //List<Visit>listVisit=null; Map<String,Object>mapValue=new HashMap<>();
			 * Criterion cr1=null; Criterion cr2=null; Criterion cr3=null; Criterion
			 * cr4=null; Criterion cr5=null; Criterion cr51=null; String serviceNo ="";
			 * String aliasValue=""; String aliasValuea=""; Integer count =0; String
			 * yeadOFMbMe=""; String yearOfMeMBTo=""; String employeeNAme="";
			 * List<MasMedicalExamReport>listMasMedicalExamReport=null;
			 * List<Object[]>listObject=null; try { int pageNo =
			 * Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
			 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			 * int startPos=(pagingSize * (pageNo - 1)); int limit=pagingSize; String
			 * mestatus=""; if(jsonData.containsKey("mestatus") &&
			 * jsonData.get("mestatus")!=null ) { mestatus =
			 * jsonData.get("mestatus").toString();
			 * mestatus=OpdServiceImpl.getReplaceString(mestatus); }
			 * if(jsonData.containsKey("serviceNo") && jsonData.get("serviceNo")!=null ) {
			 * serviceNo = jsonData.get("serviceNo").toString();
			 * serviceNo=OpdServiceImpl.getReplaceString(serviceNo); }
			 * if(jsonData.containsKey("yearOfMeMB") && jsonData.get("yearOfMeMB")!=null ) {
			 * yeadOFMbMe = jsonData.get("yearOfMeMB").toString();
			 * yeadOFMbMe=OpdServiceImpl.getReplaceString(yeadOFMbMe); }
			 * if(jsonData.containsKey("yearOfMeMBTo") && jsonData.get("yearOfMeMBTo")!=null
			 * ) { yearOfMeMBTo = jsonData.get("yearOfMeMBTo").toString();
			 * yearOfMeMBTo=OpdServiceImpl.getReplaceString(yearOfMeMBTo); }
			 * if(jsonData.containsKey("employeeName") && jsonData.get("employeeName")!=null
			 * ) { employeeNAme = jsonData.get("employeeName").toString();
			 * employeeNAme=OpdServiceImpl.getReplaceString(employeeNAme); } String
			 * ralationNameVal = HMSUtil.getProperties("js_messages_en.properties",
			 * "relationName"); MasRelation
			 * masRelation=checkRelationByName(ralationNameVal.trim());
			 * 
			 * if(StringUtils.isNotEmpty(employeeNAme) &&
			 * StringUtils.isNotEmpty(employeeNAme.trim())) { Long relationId=null;
			 * if(masRelation!=null) relationId=masRelation.getRelationId();
			 * cr51=Restrictions.and( Restrictions.like("patient.employeeName", "%"
			 * +employeeNAme+ "%").ignoreCase(),Restrictions.eq("patient.relationId",
			 * relationId)); } //
			 * aliasValue="visit"+","+"visit.patient"+","+"visit.masAppointmentType"; //
			 * aliasValuea="visit"+","+"patient"+","+"masAppointmentType";
			 * 
			 * //Criteria
			 * criteria=session.createCriteria(Visit.class,"").createAlias(associationPath,
			 * alias) Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * 
			 * Criteria criteria=session.createCriteria(MasMedicalExamReport.class,
			 * "masMedicalExamReport").createAlias("masMedicalExamReport.visit",
			 * "visit",JoinType.LEFT_OUTER_JOIN) .createAlias("visit.masAppointmentType",
			 * "masAppointmentType",JoinType.LEFT_OUTER_JOIN).createAlias("visit.patient",
			 * "patient",JoinType.LEFT_OUTER_JOIN).createAlias("visit.patient.masRank",
			 * "masRank",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.patient.masAdministrativeSex",
			 * "masAdministrativeSex",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.masMedExam",
			 * "masMedExam",JoinType.LEFT_OUTER_JOIN).createAlias(
			 * "masMedicalExamReport.masRank", "mMasRank",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.patient.masMedicalCategory",
			 * "masMedicalCategory",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.patientMedBoard",
			 * "patientMedBoard",JoinType.LEFT_OUTER_JOIN);
			 * if(!jsonData.containsKey("membHistory")) {
			 * if(StringUtils.isNotEmpty(serviceNo) &&
			 * !serviceNo.trim().equalsIgnoreCase("")) { cr3=
			 * Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase(); }
			 * 
			 * if(StringUtils.isNotEmpty(mestatus) ) { if( mestatus.equalsIgnoreCase("me"))
			 * { cr2= Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * "ME").ignoreCase(); } if(mestatus.equalsIgnoreCase("mb")) { cr2=
			 * Restrictions.eq("masAppointmentType.appointmentTypeCode", "MB").ignoreCase();
			 * } if(StringUtils.isNotEmpty(yeadOFMbMe) &&
			 * StringUtils.isNotEmpty(yearOfMeMBTo)) {
			 * 
			 * Date s = HMSUtil.convertStringTypeDateToDateType(yeadOFMbMe.toString());
			 * 
			 * Date from = HMSUtil.convertStringTypeDateToDateType(yearOfMeMBTo.toString());
			 * Calendar cal = Calendar.getInstance(); cal.set(Calendar.HOUR_OF_DAY, 0);
			 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
			 * cal.set(Calendar.MILLISECOND, 0);
			 * 
			 * 
			 * if(from!=null) { cal.setTime(from); from = cal.getTime(); }
			 * cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE,59);
			 * 
			 * if(s!=null && from!=null) { cal.setTime(s); Date to = cal.getTime(); cr4=
			 * Restrictions.ge("masMedicalExamReport.mediceExamDate",from) ; cr5=
			 * Restrictions.le("masMedicalExamReport.mediceExamDate",to) ; }
			 * 
			 * } //cr1=Restrictions.eq("visit.examStatus", "C").ignoreCase();
			 * cr1=Restrictions.or(Restrictions.eq("masMedicalExamReport.status",
			 * "ea").ignoreCase(),Restrictions.eq("masMedicalExamReport.status",
			 * "ev").ignoreCase() ,Restrictions.eq("visit.examStatus", "C").ignoreCase());
			 * if(cr1!=null) criteria.add(cr1); if(cr2!=null) criteria.add(cr2);
			 * if(cr3!=null) criteria.add(cr3); if(cr4!=null) criteria.add(cr4);
			 * if(cr5!=null) criteria.add(cr5); if(cr51!=null) criteria.add(cr51);
			 * 
			 * 
			 * ProjectionList projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("patient.patientName").as(
			 * "patientName"));
			 * projectionList.add(Projections.property("patient.dateOfBirth").as(
			 * "dateOfBirth")); projectionList.add(Projections.property(
			 * "masAdministrativeSex.administrativeSexName").as("administrativeSexName"));
			 * projectionList.add(Projections.property("masRank.rankName").as("rankName"));
			 * projectionList.add(Projections.property("mMasRank.rankName").as("rankName1"))
			 * ; projectionList.add(Projections.property("masMedExam.medicalExamName").as(
			 * "medicalExamName"));
			 * projectionList.add(Projections.property("masMedExam.medicalExamCode").as(
			 * "medicalExamCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.status").as(
			 * "status"));
			 * projectionList.add(Projections.property("masMedicalExamReport.visitId").as(
			 * "visitId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.patientId").as(
			 * "patientId"));
			 * projectionList.add(Projections.property("patient.serviceNo").as("serviceNo"))
			 * ; projectionList.add(Projections.property("visit.departmentId").as(
			 * "departmentId")); projectionList.add(Projections.property(
			 * "masMedicalExamReport.medicalExaminationId").as("medicalExaminationId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.mediceExamDate"
			 * ).as("mediceExamDate"));
			 * projectionList.add(Projections.property("masMedicalExamReport.ridcId").as(
			 * "ridcId"));
			 * projectionList.add(Projections.property("visit.visitFlag").as("visitFlag"));
			 * projectionList.add(Projections.property("masMedicalExamReport.approvedBy").as
			 * ("approvedBy"));
			 * projectionList.add(Projections.property("masMedicalExamReport.dateOfBirth").
			 * as("dateOfBirth1"));
			 * projectionList.add(Projections.property("masMedicalExamReport.daterelease").
			 * as("daterelease")); projectionList.add(Projections.property(
			 * "masAppointmentType.appointmentTypeCode").as("appointmentTypeCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.apparentAge").
			 * as("apparentAge"));
			 * projectionList.add(Projections.property("visit.meAge").as("meAge"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryId").as("medicalCategoryId"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryName").as("medicalCategoryName"));
			 * projectionList.add(Projections.property(
			 * "masMedicalExamReport.meApprovalRidcId").as("meApprovalRidcId"));
			 * projectionList.add(Projections.property("patientMedBoard.ridcId").as(
			 * "ridcIdMb"));
			 * 
			 * criteria.setProjection(projectionList);
			 * criteria.addOrder(Order.desc("masMedicalExamReport.mediceExamDate"));
			 * 
			 * 
			 * //listMasMedicalExamReport=criteria.list(); listObject=criteria.list(); count
			 * = listObject.size(); criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize); //listMasMedicalExamReport =
			 * criteria.list(); listObject=criteria.list(); //listMasMedicalExamReport=
			 * findByLeftJoinCriteriaWithMultipleAlias(aliasValue,aliasValuea,null,startPos,
			 * limit, cr1,cr2,cr3);
			 * 
			 * 
			 * 
			 * } else { serviceNo = jsonData.get("serviceNo").toString();
			 * serviceNo=OpdServiceImpl.getReplaceString(serviceNo); String examTypeId =
			 * jsonData.get("examTypeId").toString();
			 * examTypeId=OpdServiceImpl.getReplaceString(examTypeId);
			 * if(StringUtils.isNotEmpty(yeadOFMbMe) &&
			 * StringUtils.isNotEmpty(yearOfMeMBTo)) {
			 * 
			 * Date from = HMSUtil.convertStringTypeDateToDateType(yeadOFMbMe.toString());
			 * 
			 * Date to = HMSUtil.convertStringTypeDateToDateType(yearOfMeMBTo.toString());
			 * Calendar cal = Calendar.getInstance(); cal.set(Calendar.HOUR_OF_DAY, 0);
			 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
			 * cal.set(Calendar.MILLISECOND, 0);
			 * 
			 * 
			 * if(from!=null) { cal.setTime(from); from = cal.getTime(); }
			 * cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE,59);
			 * 
			 * if(to!=null && from!=null) { cal.setTime(to); Date too = cal.getTime(); cr4=
			 * Restrictions.ge("masMedicalExamReport.mediceExamDate",from) ; cr5=
			 * Restrictions.le("masMedicalExamReport.mediceExamDate",too) ; }
			 * 
			 * } if(StringUtils.isNotEmpty(serviceNo) &&
			 * !serviceNo.trim().equalsIgnoreCase("")) { cr2=
			 * Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase(); }
			 * if(StringUtils.isNotEmpty(examTypeId) && !examTypeId.equalsIgnoreCase("0")) {
			 * cr3= Restrictions.eq("visit.examId", Long.parseLong(examTypeId.toString()));
			 * } //cr1=Restrictions.eq("examStatus", "C").ignoreCase();
			 * 
			 * //listVisit=visitDao.findByLeftJoinCriteria("patient","patient",
			 * null,startPos,limit, cr1,cr2,cr3) ;
			 * cr1=Restrictions.or(Restrictions.eq("masMedicalExamReport.status",
			 * "ea").ignoreCase(),Restrictions.eq("masMedicalExamReport.status",
			 * "ev").ignoreCase(),Restrictions.eq("masMedicalExamReport.status",
			 * "et").ignoreCase(),Restrictions.eq("masMedicalExamReport.status",
			 * "es").ignoreCase() ,Restrictions.eq("visit.examStatus", "C").ignoreCase());
			 * //listMasMedicalExamReport=
			 * findByLeftJoinCriteriaWithMultipleAlias(aliasValue,aliasValuea,null,startPos,
			 * 0, cr1,cr2,cr3); // count = listMasMedicalExamReport.size();
			 * //listMasMedicalExamReport=
			 * findByLeftJoinCriteriaWithMultipleAlias(aliasValue,aliasValuea,null,startPos,
			 * limit, cr1,cr2,cr3); if(cr1!=null) criteria.add(cr1); if(cr2!=null)
			 * criteria.add(cr2); if(cr3!=null) criteria.add(cr3); if(cr4!=null)
			 * criteria.add(cr4); if(cr5!=null) criteria.add(cr5); if(cr51!=null)
			 * criteria.add(cr51); ProjectionList projectionList =
			 * Projections.projectionList();
			 * projectionList.add(Projections.property("patient.patientName").as(
			 * "patientName"));
			 * projectionList.add(Projections.property("patient.dateOfBirth").as(
			 * "dateOfBirth")); projectionList.add(Projections.property(
			 * "masAdministrativeSex.administrativeSexName").as("administrativeSexName"));
			 * projectionList.add(Projections.property("masRank.rankName").as("rankName"));
			 * projectionList.add(Projections.property("mMasRank.rankName").as("rankName1"))
			 * ; projectionList.add(Projections.property("masMedExam.medicalExamName").as(
			 * "medicalExamName"));
			 * projectionList.add(Projections.property("masMedExam.medicalExamCode").as(
			 * "medicalExamCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.status").as(
			 * "status"));
			 * projectionList.add(Projections.property("masMedicalExamReport.visitId").as(
			 * "visitId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.patientId").as(
			 * "patientId"));
			 * projectionList.add(Projections.property("patient.serviceNo").as("serviceNo"))
			 * ; projectionList.add(Projections.property("visit.departmentId").as(
			 * "departmentId")); projectionList.add(Projections.property(
			 * "masMedicalExamReport.medicalExaminationId").as("medicalExaminationId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.mediceExamDate"
			 * ).as("mediceExamDate"));
			 * projectionList.add(Projections.property("masMedicalExamReport.ridcId").as(
			 * "ridcId"));
			 * 
			 * projectionList.add(Projections.property("visit.visitFlag").as("visitFlag"));
			 * projectionList.add(Projections.property("masMedicalExamReport.approvedBy").as
			 * ("approvedBy"));
			 * projectionList.add(Projections.property("masMedicalExamReport.dateOfBirth").
			 * as("dateOfBirth1"));
			 * projectionList.add(Projections.property("masMedicalExamReport.daterelease").
			 * as("daterelease")); projectionList.add(Projections.property(
			 * "masAppointmentType.appointmentTypeCode").as("appointmentTypeCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.apparentAge").
			 * as("apparentAge"));
			 * projectionList.add(Projections.property("visit.meAge").as("meAge"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryId").as("medicalCategoryId"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryName").as("medicalCategoryName"));
			 * projectionList.add(Projections.property(
			 * "masMedicalExamReport.meApprovalRidcId").as("meApprovalRidcId"));
			 * projectionList.add(Projections.property("patientMedBoard.ridcId").as(
			 * "ridcIdMb"));
			 * 
			 * criteria.setProjection(projectionList);
			 * criteria.addOrder(Order.desc("masMedicalExamReport.mediceExamDate"));
			 * 
			 * //listMasMedicalExamReport=criteria.list(); //count =
			 * listMasMedicalExamReport.size(); listObject=criteria.list(); count =
			 * listObject.size(); criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize); listObject = criteria.list(); } } else {
			 * serviceNo = jsonData.get("serviceNo").toString();
			 * serviceNo=OpdServiceImpl.getReplaceString(serviceNo); String examTypeId =
			 * jsonData.get("examTypeId").toString();
			 * examTypeId=OpdServiceImpl.getReplaceString(examTypeId);
			 * if(StringUtils.isNotEmpty(yeadOFMbMe)) {
			 * 
			 * Date s = HMSUtil.convertStringTypeDateToDateType(yeadOFMbMe.toString());
			 * Calendar cal = Calendar.getInstance(); cal.set(Calendar.HOUR_OF_DAY, 0);
			 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
			 * cal.set(Calendar.MILLISECOND, 0); if(s!=null) { cal.setTime(s); Date to =
			 * cal.getTime(); cr4= Restrictions.eq("masMedicalExamReport.mediceExamDate",to)
			 * ; }
			 * 
			 * } if(StringUtils.isNotEmpty(serviceNo) &&
			 * !serviceNo.trim().equalsIgnoreCase("")) { cr2=
			 * Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase(); }
			 * if(StringUtils.isNotEmpty(examTypeId) && !examTypeId.equalsIgnoreCase("0")) {
			 * cr3= Restrictions.eq("visit.examId", Long.parseLong(examTypeId.toString()));
			 * } cr1= Restrictions.eq("visit.examStatus", "V").ignoreCase();
			 * //listMasMedicalExamReport=findByLeftJoinCriteriaWithMultipleAlias(aliasValue
			 * ,aliasValuea,null,startPos,0, cr1,cr2); // count =
			 * listMasMedicalExamReport.size(); //listMasMedicalExamReport=
			 * findByLeftJoinCriteriaWithMultipleAlias(aliasValue,aliasValuea,null,startPos,
			 * limit, cr1,cr2); if(cr1!=null) criteria.add(cr1); if(cr2!=null)
			 * criteria.add(cr2); if(cr3!=null) criteria.add(cr3); if(cr4!=null)
			 * criteria.add(cr4); ProjectionList projectionList =
			 * Projections.projectionList();
			 * 
			 * projectionList.add(Projections.property("patient.patientName").as(
			 * "patientName"));
			 * projectionList.add(Projections.property("patient.dateOfBirth").as(
			 * "dateOfBirth")); projectionList.add(Projections.property(
			 * "masAdministrativeSex.administrativeSexName").as("administrativeSexName"));
			 * projectionList.add(Projections.property("masRank.rankName").as("rankName"));
			 * projectionList.add(Projections.property("mMasRank.rankName").as("rankName1"))
			 * ; projectionList.add(Projections.property("masMedExam.medicalExamName").as(
			 * "medicalExamName"));
			 * projectionList.add(Projections.property("masMedExam.medicalExamCode").as(
			 * "medicalExamCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.status").as(
			 * "status"));
			 * projectionList.add(Projections.property("masMedicalExamReport.visitId").as(
			 * "visitId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.patientId").as(
			 * "patientId"));
			 * projectionList.add(Projections.property("patient.serviceNo").as("serviceNo"))
			 * ; projectionList.add(Projections.property("visit.departmentId").as(
			 * "departmentId")); projectionList.add(Projections.property(
			 * "masMedicalExamReport.medicalExaminationId").as("medicalExaminationId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.mediceExamDate"
			 * ).as("mediceExamDate"));
			 * projectionList.add(Projections.property("masMedicalExamReport.ridcId").as(
			 * "ridcId"));
			 * projectionList.add(Projections.property("visit.visitFlag").as("visitFlag"));
			 * projectionList.add(Projections.property("masMedicalExamReport.approvedBy").as
			 * ("approvedBy"));
			 * projectionList.add(Projections.property("masMedicalExamReport.dateOfBirth").
			 * as("dateOfBirth1"));
			 * projectionList.add(Projections.property("masMedicalExamReport.daterelease").
			 * as("daterelease")); projectionList.add(Projections.property(
			 * "masAppointmentType.appointmentTypeCode").as("appointmentTypeCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.apparentAge").
			 * as("apparentAge"));
			 * projectionList.add(Projections.property("visit.meAge").as("meAge"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryId").as("medicalCategoryId"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryName").as("medicalCategoryName"));
			 * projectionList.add(Projections.property(
			 * "masMedicalExamReport.meApprovalRidcId").as("meApprovalRidcId"));
			 * projectionList.add(Projections.property("patientMedBoard.ridcId").as(
			 * "ridcIdMb")); criteria.setProjection(projectionList);
			 * criteria.addOrder(Order.desc("masMedicalExamReport.mediceExamDate"));
			 * //listMasMedicalExamReport=criteria.list(); //count =
			 * listMasMedicalExamReport.size(); listObject=criteria.list(); count =
			 * listObject.size(); criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize); listObject = criteria.list(); }
			 * 
			 * mapValue.put("listVisit", listObject); mapValue.put("count", count);
			 * 
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } return mapValue; }
			 * 
			 * @Transactional public MasMedicalExamReport
			 * getActionForMedicalExam(HashMap<String, Object> payload, MasMedicalExamReport
			 * masMedicalExamReprt) { try {
			 * 
			 * String actionMe = payload.get("actionMe").toString(); actionMe =
			 * OpdServiceImpl.getReplaceString(actionMe);
			 * 
			 * String visitId = payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * if (StringUtils.isNotEmpty(actionMe) &&
			 * (actionMe.equalsIgnoreCase("approveAndClose") ||
			 * actionMe.equalsIgnoreCase("approveAndForward"))) { try { if
			 * (payload.get("remarksForApproval") != null &&
			 * payload.get("remarksForApproval") != "") { String remarksForApproval =
			 * payload.get("remarksForApproval").toString(); remarksForApproval =
			 * OpdServiceImpl.getReplaceString(remarksForApproval);
			 * masMedicalExamReprt.setRemarksForward(remarksForApproval); } } catch
			 * (Exception e) { e.printStackTrace(); }
			 * 
			 * if (actionMe.equalsIgnoreCase("approveAndClose")) {
			 * masMedicalExamReprt.setStatus("ac");
			 * 
			 * // Update Visit Status
			 * 
			 * updateVisitStatus(visitId, "C"); return masMedicalExamReprt; } if
			 * (actionMe.equalsIgnoreCase("approveAndForward")) {
			 * masMedicalExamReprt.setStatus("af");
			 * 
			 * String forwardUnitId = payload.get("forwardTo").toString(); forwardUnitId =
			 * OpdServiceImpl.getReplaceString(forwardUnitId);
			 * masMedicalExamReprt.setForwardUnitId(Long.parseLong(forwardUnitId));
			 * 
			 * 
			 * String designationForMe = payload.get("designationForMe").toString();
			 * designationForMe = OpdServiceImpl.getReplaceString(designationForMe);
			 * masMedicalExamReprt.setFowardedDesignationId(Long.parseLong(designationForMe)
			 * ); masMedicalExamReprt.setRemarksRej(null);
			 * 
			 * updateVisitStatus(visitId, "F");
			 * 
			 * } return masMedicalExamReprt; } if (StringUtils.isNotEmpty(actionMe) &&
			 * actionMe.equalsIgnoreCase("reject")) {
			 * 
			 * String remarksReject = payload.get("remarksReject").toString(); remarksReject
			 * = OpdServiceImpl.getReplaceString(remarksReject);
			 * masMedicalExamReprt.setStatus("rj");
			 * masMedicalExamReprt.setRemarksRej(remarksReject);
			 * 
			 * updateVisitStatus(visitId, "R"); return masMedicalExamReprt; }
			 * 
			 * if (StringUtils.isNotEmpty(actionMe) && actionMe.equalsIgnoreCase("pending"))
			 * {
			 * 
			 * String remarksPending = payload.get("remarksPending").toString();
			 * remarksPending = OpdServiceImpl.getReplaceString(remarksPending);
			 * masMedicalExamReprt.setStatus("pe");
			 * masMedicalExamReprt.setRemarksPending(remarksPending);
			 * 
			 * String nextAppointmentDate = payload.get("nextAppointmentDate").toString();
			 * nextAppointmentDate = OpdServiceImpl.getReplaceString(nextAppointmentDate);
			 * if (StringUtils.isNotBlank(nextAppointmentDate) &&
			 * !nextAppointmentDate.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(nextAppointmentDate)) { Date
			 * appoinmentDate =
			 * HMSUtil.convertStringTypeDateToDateType(nextAppointmentDate.trim());
			 * if(appoinmentDate!=null)
			 * masMedicalExamReprt.setAppointmentDate(appoinmentDate); } return
			 * masMedicalExamReprt; } } catch (Exception e) { e.printStackTrace(); return
			 * null; } return masMedicalExamReprt; }
			 * 
			 * @Transactional
			 * 
			 * @Override public void updateVisitStatus(String visitId, String status) { try
			 * { List<Visit> listVisit = getPatientVisitForMe(Long.parseLong(visitId));
			 * Visit visit = listVisit.get(0); visit.setExamStatus(status); if(status!=null
			 * && status.equalsIgnoreCase("C")) visit.setVisitStatus("c");
			 * saveOrUpdateVisit(visit);
			 * 
			 * } catch (Exception e) { e.printStackTrace(); } }
			 * 
			 * @Transactional
			 * 
			 * @Override public void updateVisitStatusForDigi(String visitId, String
			 * status,String hospitalId,String dateOfExam) { //try { List<Visit> listVisit =
			 * getPatientVisitForMe(Long.parseLong(visitId)); Visit visit =
			 * listVisit.get(0); visit.setExamStatus(status); if(status!=null &&
			 * status.equalsIgnoreCase("C")) { visit.setVisitStatus("c");
			 * visit.setExamStatus("C"); } if(StringUtils.isNotEmpty(hospitalId)&&
			 * !hospitalId.equalsIgnoreCase("0")) {
			 * visit.setHospitalId(Long.parseLong(hospitalId));
			 * 
			 * } if(StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("")
			 * && MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date
			 * visitDate=HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(visitDate!=null) { Timestamp ts=new Timestamp(visitDate.getTime());
			 * visit.setVisitDate(ts); } } saveOrUpdateVisit(visit);
			 * 
			 * } catch (Exception e) { e.printStackTrace(); } }
			 * 
			 * 
			 * @Override public List<Visit> getPatientVisitForMe(Long visitId) { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
			 * session.createCriteria(Visit.class); cr.add(Restrictions.eq("visitId",
			 * visitId)); List<Visit> list = cr.list();
			 * 
			 * return list; }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public MasRelation checkRelationByName(String relationName) {
			 * MasRelation masRelation=null; try { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
			 * session.createCriteria(MasRelation.class);
			 * cr.add(Restrictions.eq("relationName", relationName)); masRelation =
			 * (MasRelation) cr.uniqueResult();
			 * 
			 * } catch(Exception e) {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * masRelation; }
			 * 
			 * @Override
			 * 
			 * @Transactional public
			 * Map<String,Object>getAllMasMedicalExamReportForDigi(HashMap<String,Object>
			 * jsonData){ List<Object[]>listObject=null;
			 * Map<String,Object>mapMasMedicalExamReport=new HashMap<>(); int pageNo =
			 * Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
			 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			 * int startPos=(pagingSize * (pageNo - 1)); int limit=pagingSize; String
			 * ServiceNo= jsonData.get("serviceNo").toString(); String employeeName=
			 * jsonData.get("employeeName").toString(); Criterion cr1=null; Session session
			 * = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
			 * session.createCriteria(MasMedicalExamReport.class,"masMedicalExamReport");
			 * String ralationNameVal = HMSUtil.getProperties("js_messages_en.properties",
			 * "relationName"); MasRelation
			 * masRelation=checkRelationByName(ralationNameVal.trim()); Long
			 * relationId=null; if(masRelation!=null)
			 * relationId=masRelation.getRelationId(); if(StringUtils.isNotEmpty(ServiceNo)
			 * && StringUtils.isNotEmpty(ServiceNo.trim()) && relationId!=null) {
			 * cr1=Restrictions.and(Restrictions.eq("patient.serviceNo",
			 * ServiceNo).ignoreCase(),Restrictions.eq("patient.relationId", relationId)); }
			 * Criterion cr2=null; if(StringUtils.isNotEmpty(employeeName) &&
			 * StringUtils.isNotEmpty(employeeName.trim())) {
			 * cr2=Restrictions.like("patient.employeeName", "%" +employeeName+
			 * "%").ignoreCase(); } Criterion cr3=
			 * Restrictions.or(Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * "ME").ignoreCase(),Restrictions.eq("masAppointmentType.appointmentTypeCode",
			 * "MB").ignoreCase()); Criterion cr4=null;
			 * if(jsonData.containsKey("flagForList") &&
			 * jsonData.get("flagForList").toString().equalsIgnoreCase("es")) { cr4=
			 * Restrictions.eq("status", "es"); } else
			 * if(jsonData.containsKey("flagForList") &&
			 * jsonData.get("flagForList").toString().equalsIgnoreCase("ev")) { cr4=
			 * Restrictions.eq("status", "ev"); } else
			 * if(jsonData.containsKey("flagForList") &&
			 * jsonData.get("flagForList").toString().equalsIgnoreCase("wt")) {
			 * 
			 * cr4=Restrictions.or(Restrictions.eq("status", "et"),Restrictions.eq("status",
			 * "er"),Restrictions.eq("status", "ar") ,Restrictions.eq("status",
			 * "vr"),Restrictions.eq("status", "es"),Restrictions.eq("status",
			 * "ev"),Restrictions.eq("status", "ea"));
			 * 
			 * cr4=Restrictions.eq("status", "et");
			 * 
			 * }
			 * 
			 * else if(jsonData.containsKey("flagForList") &&
			 * jsonData.get("flagForList").toString().equalsIgnoreCase("tr")) {
			 * cr4=Restrictions.or(Restrictions.eq("status", "er"),Restrictions.eq("status",
			 * "ar") ,Restrictions.eq("status", "vr"),Restrictions.eq("status",
			 * "es"),Restrictions.eq("status", "ev")); }
			 * 
			 * else if(jsonData.containsKey("flagForList") &&
			 * jsonData.get("flagForList").toString().equalsIgnoreCase("rj")) {
			 * cr4=Restrictions.or(Restrictions.eq("status", "ar")
			 * ,Restrictions.eq("status", "vr")); }
			 * 
			 * else if(jsonData.containsKey("flagForList") &&
			 * jsonData.get("flagForList").toString().equalsIgnoreCase("app")) {
			 * cr4=Restrictions.or(Restrictions.eq("status", "ev")
			 * ,Restrictions.eq("status", "ea")); cr4=Restrictions.eq("status","ea"); }
			 * 
			 * 
			 * String aliasValue="visit"+","+"visit.patient"+","+"visit.masAppointmentType";
			 * String aliasValuea="visit"+","+"patient"+","+"masAppointmentType";
			 * List<MasMedicalExamReport>listMasMedicalExamReport=null;
			 * //listMasMedicalExamReport=
			 * findByLeftJoinCriteriaWithMultipleAlias(aliasValue,aliasValuea,null,0,0,cr1,
			 * cr2,cr3,cr4); //criteria.createAlias("visit",
			 * "visit",JoinType.LEFT_OUTER_JOIN).createAlias("visit.patient",
			 * "patient",JoinType.LEFT_OUTER_JOIN).createAlias("visit.masAppointmentType",
			 * "masAppointmentType",JoinType.LEFT_OUTER_JOIN); criteria.createAlias("visit",
			 * "visit").createAlias("visit.patient", "patient",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.masAppointmentType",
			 * "masAppointmentType",JoinType.LEFT_OUTER_JOIN)
			 * //.createAlias("patient.masAdministrativeSex",
			 * "masAdministrativeSex",JoinType.LEFT_OUTER_JOIN)
			 * //.createAlias("patient.masRank", "masRank",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.masMedExam", "masMedExam",JoinType.LEFT_OUTER_JOIN);
			 * //.createAlias("visit.opdPatientDetails",
			 * "opdPatientDetails",JoinType.LEFT_OUTER_JOIN);
			 * 
			 * if(cr1!=null) criteria.add(cr1); if(cr2!=null) criteria.add(cr2);
			 * if(cr3!=null) criteria.add(cr3); if(cr4!=null) criteria.add(cr4);
			 * 
			 * ProjectionList projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("patient.patientName").as(
			 * "patientName"));
			 * projectionList.add(Projections.property("patient.dateOfBirth").as(
			 * "dateOfBirth")); // projectionList.add(Projections.property(
			 * "masAdministrativeSex.administrativeSexName").as("administrativeSexName"));
			 * //projectionList.add(Projections.property("masRank.rankName").as("rankName"))
			 * ;
			 * 
			 * projectionList.add(Projections.property(
			 * "masMedicalExamReport.medicalExaminationId").as("medicalExaminationId"));
			 * projectionList.add(Projections.property(
			 * "masMedicalExamReport.aaFinalObservation").as("aaFinalObservation"));
			 * 
			 * projectionList.add(Projections.property("masMedExam.medicalExamName").as(
			 * "medicalExamName"));
			 * projectionList.add(Projections.property("masMedExam.medicalExamCode").as(
			 * "medicalExamCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.status").as(
			 * "status"));
			 * projectionList.add(Projections.property("masMedicalExamReport.petStatus").as(
			 * "petStatus"));
			 * projectionList.add(Projections.property("masMedicalExamReport.visitId").as(
			 * "visitId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.patientId").as(
			 * "patientId"));
			 * projectionList.add(Projections.property("patient.serviceNo").as("serviceNo"))
			 * ; projectionList.add(Projections.property("visit.departmentId").as(
			 * "departmentId")); //projectionList.add(Projections.property(
			 * "opdPatientDetails.opdPatientDetailsId").as("opdPatientDetailsId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.aaRemarks").as(
			 * "aaRemarks"));
			 * projectionList.add(Projections.property("visit.tokenNo").as("tokenNo"));
			 * projectionList.add(Projections.property(
			 * "masAppointmentType.appointmentTypeCode").as("appointmentTypeCode"));
			 * 
			 * projectionList.add(Projections.property("masMedicalExamReport.mediceExamDate"
			 * ).as("mediceExamDate"));
			 * projectionList.add(Projections.property("masMedicalExamReport.daterelease").
			 * as("daterelease"));
			 * 
			 * if(jsonData.containsKey("flagForList") &&
			 * !jsonData.get("flagForList").toString().equalsIgnoreCase("wt")) {
			 * projectionList.add(Projections.property("masMedicalExamReport.maUserId").as(
			 * "usersMA"));
			 * projectionList.add(Projections.property("masMedicalExamReport.moUserId").as(
			 * "usersMO"));
			 * projectionList.add(Projections.property("masMedicalExamReport.cmUserId").as(
			 * "usersCM")); } else { projectionList.add(Projections.property(
			 * "masMedicalExamReport.medicalExaminationId").as("medicalExaminationId"));
			 * projectionList.add(Projections.property(
			 * "masMedicalExamReport.aaFinalObservation").as("aaFinalObservation"));
			 * projectionList.add(Projections.property("masMedicalExamReport.aaRemarks").as(
			 * "aaRemarks")); }
			 * 
			 * projectionList.add(Projections.property("masMedicalExamReport.maDate").as(
			 * "maDate"));
			 * projectionList.add(Projections.property("masMedicalExamReport.moDate").as(
			 * "moDate"));
			 * 
			 * 
			 * 
			 * criteria.setProjection(projectionList); if(cr1!=null) criteria.add(cr1);
			 * if(cr2!=null) criteria.add(cr2); if(cr3!=null) criteria.add(cr3);
			 * if(cr4!=null) criteria.add(cr4); //listMasMedicalExamReport = criteria //
			 * .setResultTransformer(new
			 * AliasToBeanResultTransformer(MasMedicalExamReport.class)).list();
			 * listObject=criteria.list(); //int count = listMasMedicalExamReport.size();
			 * int count = listObject.size(); //listMasMedicalExamReport=
			 * findByLeftJoinCriteriaWithMultipleAlias(aliasValue,aliasValuea,null,startPos,
			 * limit,cr1,cr2,cr3,cr4); if(cr1!=null) criteria.add(cr1); if(cr2!=null)
			 * criteria.add(cr2); if(cr3!=null) criteria.add(cr3); if(cr4!=null)
			 * criteria.add(cr4); //criteria = criteria.setFirstResult(pagingSize * (pageNo
			 * - 1));
			 * 
			 * if ((StringUtils.isNotEmpty(ServiceNo) && !ServiceNo.equals("") &&
			 * !ServiceNo.equals("null")) || (StringUtils.isNotEmpty(employeeName)&&
			 * !employeeName.equals("null"))) { criteria =
			 * criteria.setFirstResult(pagingSize * (1 - 1)); } else { criteria =
			 * criteria.setFirstResult(pagingSize * (pageNo - 1)); }
			 * criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize);
			 * 
			 * //criteria = criteria.setMaxResults(pagingSize);
			 * 
			 * //listMasMedicalExamReport = criteria // .setResultTransformer(new
			 * AliasToBeanResultTransformer(MasMedicalExamReport.class)).list();
			 * //listMasMedicalExamReport = criteria.list(); listObject=criteria.list();
			 * mapMasMedicalExamReport.put("listMasMedicalExamReport", listObject);
			 * mapMasMedicalExamReport.put("count", count);
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return
			 * mapMasMedicalExamReport; }
			 * 
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public String saveOrUpdateDigifileUpload(MasMedicalExamReport
			 * masMedicalExamReprt,HashMap<String, Object> payload,String patientId, String
			 * hospitalId,String userId) { Session session=null; Long
			 * medicalExaminationId=null; Transaction tx=null; String satusOfMessage="";
			 * String statusOfPatient=""; Timestamp ts =null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * tx=session.beginTransaction();
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
			 * if(payload.get("unitOrSlip")!=null ) { String
			 * unitOrSlip=payload.get("unitOrSlip").toString();
			 * unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
			 * if(StringUtils.isNotEmpty(unitOrSlip) &&
			 * !unitOrSlip.equalsIgnoreCase("undefined") &&
			 * !unitOrSlip.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setUnitId(Long.parseLong(unitOrSlip)); }
			 * 
			 * } if(payload.get("rank")!=null) { String rank=payload.get("rank").toString();
			 * rank=OpdServiceImpl.getReplaceString(rank); if(StringUtils.isNotEmpty(rank)
			 * && !rank.equalsIgnoreCase("undefined") && !rank.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRankId(Long.parseLong(rank)); } }
			 * if(payload.get("branchOrTrade")!=null) { String
			 * branchOrTrade=payload.get("branchOrTrade").toString();
			 * branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
			 * if(StringUtils.isNotEmpty(branchOrTrade) &&
			 * !branchOrTrade.equalsIgnoreCase("undefined") &&
			 * !branchOrTrade.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setBranchId(Long.parseLong(branchOrTrade)); } } String
			 * totalLengthDigiFile=payload.get("totalLengthDigiFile").toString();
			 * totalLengthDigiFile=OpdServiceImpl.getReplaceString(totalLengthDigiFile);
			 * String dateOfExam = payload.get("dateOfExam").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam);
			 * 
			 * 
			 * String rmdIdVal=patientRegistrationServiceImpl.getFinalInvestigationValue(
			 * investigationIdValues, dynamicUploadInvesIdAndfiles,totalLengthDigiFile) ;
			 * 
			 * 
			 * String[] rangeValues=null; if(payload.containsKey("range")) { String
			 * rangeValue=payload.get("range").toString();
			 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
			 * rangeValue.split(","); }
			 * 
			 * String[] rmsIdFinalValfileUp=null; rmsIdFinalValfileUp = rmdIdVal.split(",");
			 * 
			 * 
			 * String meRmsId=payload.get("meRmsId").toString();
			 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
			 * meRmsId.split(",");
			 * 
			 * payload.put("digiFlag", "yes");
			 * 
			 * saveOrUpdateMedicalCategoryDigi(payload);
			 * saveOrUpdateMedicalCatCompositeDigi(payload);
			 * 
			 * Long checkPatientUpdate=0l; String visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId); //try { String
			 * medicalCompositeNameValue =
			 * payload.get("medicalCompositeNameValue").toString();
			 * medicalCompositeNameValue =
			 * OpdServiceImpl.getReplaceString(medicalCompositeNameValue);
			 * 
			 * String diagnosisiIdMC = payload.get("diagnosisiIdMC").toString();
			 * diagnosisiIdMC = OpdServiceImpl.getReplaceString(diagnosisiIdMC); String[]
			 * diagnosisiIdMCValues = null; if(StringUtils.isNotEmpty(diagnosisiIdMC)) {
			 * diagnosisiIdMCValues = diagnosisiIdMC.trim().split(","); } String
			 * fitFlagCheckValue=payload.get("fitFlagCheckValue").toString();
			 * fitFlagCheckValue=OpdServiceImpl.getReplaceString(fitFlagCheckValue);
			 * 
			 * 
			 * 
			 * if(StringUtils.isEmpty(medicalCompositeNameValue) &&
			 * diagnosisiIdMCValues==null && (fitFlagCheckValue.equalsIgnoreCase("Yes") ||
			 * fitFlagCheckValue.contains("Yes"))) { Long masMedicalCategoryId=null;
			 * if(StringUtils.isNotEmpty(patientId)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId)); if(patient!=null) {
			 * Criterion cr1=Restrictions.eq("fitFlag", "F").ignoreCase();
			 * List<MasMedicalCategory>listMasMedicalCategory=masMedicalCategoryDao.
			 * findByCriteria(cr1); if(CollectionUtils.isNotEmpty(listMasMedicalCategory)) {
			 * masMedicalCategoryId=listMasMedicalCategory.get(0).getMedicalCategoryId();
			 * if(masMedicalCategoryId!=null)
			 * patient.setMedicalCategoryId(masMedicalCategoryId); }
			 * 
			 * patient.setFitFlag("F");
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined")&&
			 * !gender.equalsIgnoreCase("0"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * 
			 * checkPatientUpdate++; saveOrUpdatePatient(patient); } }
			 * 
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * 
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ;
			 * 
			 * if(CollectionUtils.isEmpty(listPatientMedicalCat)) { patientMedicalCat=new
			 * PatientMedicalCat(); if(StringUtils.isNotEmpty(patientId))
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * //patientMedicalCat.setMedicalCategoryId(masMedicalCategoryId);
			 * patientMedicalCat.setpMedCatId(masMedicalCategoryId);
			 * if(StringUtils.isNotEmpty(visitId))
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId));
			 * 
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(dateOfExam) && dateOfExam!=null &&
			 * !dateOfExam.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date
			 * medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(medicalCompositeDateValue!=null) { ts = new
			 * Timestamp(medicalCompositeDateValue.getTime());
			 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
			 * patientMedicalCat.setpMedFitFlag("F"); if(patientMedicalCat!=null)
			 * saveOrUpdatePatientMedicalCat(patientMedicalCat); }
			 * 
			 * } else { if(StringUtils.isNotEmpty(patientId) &&
			 * StringUtils.isEmpty(medicalCompositeNameValue)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId)); patient.setFitFlag("U");
			 * patient.setMedicalCategoryId(null);
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined") &&
			 * !gender.equalsIgnoreCase("0"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * 
			 * checkPatientUpdate++; saveOrUpdatePatient(patient);
			 * 
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
			 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
			 * deletePatientMedCat(patientMedicalCat); } } }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); }
			 * masMedicalExamReprt=updateInvestigationDgResultForDigiUpload(payload,
			 * patientId, hospitalId, userId,
			 * masMedicalExamReprt,rangeValues,rmsIdFinalValfileUp,ts);
			 * 
			 * saveUpdatePatientImmunizationHistoryDigi(payload);
			 * 
			 * //Save patient if(payload.get("gender")!=null && checkPatientUpdate==0 ) {
			 * String gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * Patient patient=null; if(StringUtils.isNotBlank(patientId))
			 * patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender));
			 * patientDao.saveOrUpdate(patient); } } } } String saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft);
			 * 
			 * if(payload.containsKey("actionDigiFile")) { String actionDigiFile =
			 * payload.get("actionDigiFile").toString(); actionDigiFile =
			 * OpdServiceImpl.getReplaceString(actionDigiFile);
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0")) {
			 * 
			 * masMedicalExamReprt.setStatus(actionDigiFile);
			 * 
			 * if(actionDigiFile.equalsIgnoreCase("ev")||actionDigiFile.equalsIgnoreCase(
			 * "vr")) { if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setMoUserId(Long.parseLong(userId));
			 * 
			 * String finalObservationMo = payload.get("finalObservationMo").toString();
			 * finalObservationMo = OpdServiceImpl.getReplaceString(finalObservationMo);
			 * masMedicalExamReprt.setFinalobservation(finalObservationMo);
			 * masMedicalExamReprt.setMoDate(new Date());
			 * masMedicalExamReprt.setPetStatus(actionDigiFile); String hospitalForDigi="";
			 * if(payload.containsKey("hospitalForDigi")) { hospitalForDigi =
			 * payload.get("hospitalForDigi").toString(); hospitalForDigi =
			 * OpdServiceImpl.getReplaceString(hospitalForDigi); }
			 * updateVisitStatusForDigi(visitId, "c",hospitalForDigi,dateOfExam); } } }
			 * 
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { String
			 * actionDigiFileApproved = payload.get("actionDigiFileApproved").toString();
			 * actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setStatus(actionDigiFileApproved);
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||actionDigiFileApproved.
			 * equalsIgnoreCase("ar")) { if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setCmUserId(Long.parseLong(userId));
			 * 
			 * String finalObservationRMo = payload.get("finalObservationRMo").toString();
			 * finalObservationRMo = OpdServiceImpl.getReplaceString(finalObservationRMo);
			 * masMedicalExamReprt.setAaFinalObservation(finalObservationRMo);
			 * masMedicalExamReprt.setCmDate(new Date()); String hospitalForDigi="";
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")) { visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * if(payload.containsKey("hospitalForDigi")) { hospitalForDigi =
			 * payload.get("hospitalForDigi").toString(); hospitalForDigi =
			 * OpdServiceImpl.getReplaceString(hospitalForDigi); }
			 * updateVisitStatusForDigi(visitId, "c",hospitalForDigi,dateOfExam); } } } }
			 * 
			 * 
			 * 
			 * if (StringUtils.isNotEmpty(saveInDraft) &&
			 * (saveInDraft.equalsIgnoreCase("et")||saveInDraft.equalsIgnoreCase("es"))){
			 * masMedicalExamReprt.setStatus(saveInDraft);
			 * if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setMaUserId(Long.parseLong(userId)); String
			 * finalObservationMa = payload.get("finalObservationMa").toString();
			 * finalObservationMa = OpdServiceImpl.getReplaceString(finalObservationMa);
			 * //masMedicalExamReprt.set masMedicalExamReprt.setMaDate(new Date());
			 * masMedicalExamReprt.setPetStatus(null);
			 * masMedicalExamReprt.setAaFinalObservation(null);
			 * masMedicalExamReprt.setFinalobservation(null); }
			 * 
			 * 
			 * 
			 * 
			 * else { masMedicalExamReprt.setStatus(saveInDraft); }
			 * 
			 * //if (StringUtils.isNotEmpty(saveInDraft) &&
			 * !saveInDraft.equalsIgnoreCase("draftMa")) {
			 * masMedicalExamReprt=saveUpdateForReferalforMeDigiFileUpload(
			 * masMedicalExamReprt,payload,hospitalId); //masMedicalExamReprt =
			 * getActionForMedicalExam(payload, masMedicalExamReprt); //} if(meRmsIds!=null
			 * && meRmsIds.length>0 && meRmsIds[0]!=null &&
			 * !meRmsIds[0].equalsIgnoreCase("") && !meRmsIds[0].equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRidcId(Long.parseLong(meRmsIds[0].toString())); }
			 * 
			 * //Save PatientDocumentDetail saveUpdateOfPatientDocumentDetail(payload);
			 * 
			 * session.saveOrUpdate(masMedicalExamReprt);
			 * 
			 * medicalExaminationId=masMedicalExamReprt.getMedicalExaminationId();
			 * if(StringUtils.isNotEmpty(masMedicalExamReprt.getStatus())) {
			 * statusOfPatient=masMedicalExamReprt.getStatus(); } else {
			 * statusOfPatient="no status"; } session.flush(); session.clear(); tx.commit();
			 * if(medicalExaminationId!=null && medicalExaminationId!=0) {
			 * satusOfMessage=medicalExaminationId+
			 * "##"+"Medical Exam Submitted Successfully"+"##"+statusOfPatient; } else {
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong."+"##"+
			 * statusOfPatient; } } catch(Exception e) { if (tx != null) { try {
			 * tx.rollback();
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; } catch(Exception re) {
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+re.
			 * toString()+"##"+statusOfPatient; re.printStackTrace(); } }
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return satusOfMessage; }
			 * 
			 * int countForRange=0; int countForResult=0; int countForOrderDtId=0; int
			 * countForOrderHdId=0; int countForRIDCId=0; int countForUmoSubInv=0;
			 * 
			 * int countSubMainChargeCodeIdForSub=0; int countMainChargeCodeIdForSub=0; int
			 * countDgResultDtIdValueSubInvess=0; int countDgResultHdIdSubInvess=0; int
			 * countForInvestigationRemarkSub=0; String resultOfInvAndSub=""; int
			 * countForSubInvestigationName=0; String []
			 * subInvestigationNameIdAndInvsTemp=null; protected void deInitGlobalValue() {
			 * countForRange=0; countForResult=0; countForOrderDtId=0; countForOrderHdId=0;
			 * countForRIDCId=0; countForUmoSubInv=0; countSubMainChargeCodeIdForSub=0;
			 * countMainChargeCodeIdForSub=0; countDgResultDtIdValueSubInvess=0;
			 * countDgResultHdIdSubInvess=0; countForInvestigationRemarkSub=0;
			 * resultOfInvAndSub=""; countForSubInvestigationName=0;
			 * 
			 * 
			 * }
			 * 
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public MasMedicalExamReport
			 * updateInvestigationDgResultForDigiUpload(HashMap<String, Object>
			 * payload,String patientId, String hospitalId,String
			 * userId,MasMedicalExamReport masMedicalExamReport,String[] rangeValue,String
			 * [] rmsIdFinalValue,Timestamp ts) { Session session=null; // Transaction
			 * tx=null; session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); deInitGlobalValue(); String
			 * visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(visitId);
			 * 
			 * String chargeCodeName=payload.get("chargeCodeName").toString();
			 * chargeCodeName=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(chargeCodeName
			 * ); String[] chargeCodeNames = chargeCodeName.split(",");
			 * 
			 * String investigationIdValue=payload.get("investigationIdValue").toString();
			 * investigationIdValue=OpdServiceImpl.getReplaceString(investigationIdValue);
			 * String[] investigationIdValues = investigationIdValue.split(","); String[]
			 * resultInvss=null; if(payload.containsKey("resultInvs")) { String
			 * resultInvs=payload.get("resultInvs").toString();
			 * resultInvs=OpdServiceImpl.getReplaceString(resultInvs); //resultInvss =
			 * resultInvs.split(","); resultInvss =resultInvs.split("@@@###"); } String
			 * dgOrderDtIdValue=payload.get("dgOrderDtIdValue").toString();
			 * dgOrderDtIdValue=OpdServiceImpl.getReplaceString(dgOrderDtIdValue); String[]
			 * dgOrderDtIdValues = dgOrderDtIdValue.split(",");
			 * 
			 * 
			 * String dgOrderHdIdv=payload.get("dgOrderHdId").toString();
			 * dgOrderHdIdv=OpdServiceImpl.getReplaceString(dgOrderHdIdv); String[]
			 * dgOrderHdIds = dgOrderHdIdv.split(",");
			 * 
			 * String dgResultDtId=payload.get("dgResultDtId").toString();
			 * dgResultDtId=OpdServiceImpl.getReplaceString(dgResultDtId); String[]
			 * dgResultDtIds = dgResultDtId.split(",");
			 * 
			 * 
			 * String dgResultHdId=payload.get("dgResultHdId").toString();
			 * dgResultHdId=OpdServiceImpl.getReplaceString(dgResultHdId); String[]
			 * dgResultHdIds = dgResultHdId.split(","); String resultDate=""; Date
			 * dateOfExamValue =null; if(payload.containsKey("dateOfExam")) {
			 * resultDate=payload.get("dateOfExam").toString();
			 * resultDate=OpdServiceImpl.getReplaceString(resultDate); if
			 * (StringUtils.isNotEmpty(resultDate) && !resultDate.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(resultDate)) { dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(resultDate.trim()); } }
			 * 
			 * if(payload.containsKey("dateOfRelease")) {
			 * resultDate=payload.get("dateOfRelease").toString();
			 * resultDate=OpdServiceImpl.getReplaceString(resultDate); if
			 * (StringUtils.isNotEmpty(resultDate) && !resultDate.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(resultDate)) { dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(resultDate.trim()); }
			 * 
			 * } String createdByMb=""; if(payload.containsKey("createdByMb")) {
			 * createdByMb=payload.get("createdByMb").toString();
			 * createdByMb=OpdServiceImpl.getReplaceString(createdByMb); }
			 * 
			 * 
			 * 
			 * if(payload.containsKey("doe_doc")) {
			 * resultDate=payload.get("doe_doc").toString();
			 * resultDate=OpdServiceImpl.getReplaceString(resultDate); if
			 * (StringUtils.isNotEmpty(resultDate) && !resultDate.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(resultDate)) { dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(resultDate.trim()); } }
			 * 
			 * 
			 * 
			 * /////////////////////////////////////////////////////////////////////////////
			 * ///////////////////////// String[] investigationTypes=null;
			 * if(payload.containsKey("investigationType")) {
			 * 
			 * String investigationType=payload.get("investigationType").toString();
			 * investigationType=OpdServiceImpl.getReplaceString(investigationType);
			 * investigationTypes=investigationType.split(",");
			 * 
			 * } String[] resultSubInvs=null; if(payload.containsKey("resultSubInv")) {
			 * String resultSubInv=payload.get("resultSubInv").toString();
			 * resultSubInv=OpdServiceImpl.getReplaceString(resultSubInv);
			 * //resultSubInvs=resultSubInv.split(",");
			 * resultSubInvs=resultSubInv.split("@@@###"); }
			 * 
			 * String[] rangeSubInvess=null; if(payload.containsKey("rangeSubInves")) {
			 * String rangeSubInves=payload.get("rangeSubInves").toString();
			 * rangeSubInves=OpdServiceImpl.getReplaceString(rangeSubInves);
			 * rangeSubInvess=rangeSubInves.split(","); }
			 * 
			 * String[] subInvestigationNameIdAndInvs=null;
			 * if(payload.containsKey("subInvestigationNameIdAndInv")) { String
			 * subInvestigationNameIdAndInv=payload.get("subInvestigationNameIdAndInv").
			 * toString(); subInvestigationNameIdAndInv=OpdServiceImpl.getReplaceString(
			 * subInvestigationNameIdAndInv);
			 * subInvestigationNameIdAndInvs=subInvestigationNameIdAndInv.split(","); }
			 * 
			 * 
			 * String[] dgOrderDtIdValueSubInvess=null;
			 * if(payload.containsKey("dgOrderDtIdValueSubInves")) { String
			 * dgOrderDtIdValueSubInves=payload.get("dgOrderDtIdValueSubInves").toString();
			 * dgOrderDtIdValueSubInves=OpdServiceImpl.getReplaceString(
			 * dgOrderDtIdValueSubInves);
			 * dgOrderDtIdValueSubInvess=dgOrderDtIdValueSubInves.split(","); }
			 * 
			 * String[] dgOrderHdIdSubInvess=null;
			 * if(payload.containsKey("dgOrderHdIdSubInves")) { String
			 * dgOrderHdIdSubInves=payload.get("dgOrderHdIdSubInves").toString();
			 * dgOrderHdIdSubInves=OpdServiceImpl.getReplaceString(dgOrderHdIdSubInves);
			 * dgOrderHdIdSubInvess=dgOrderHdIdSubInves.split(","); }
			 * 
			 * String[] UOMSubss=null; if(payload.containsKey("UOMSub")) { String
			 * UOMSub=payload.get("UOMSub").toString();
			 * UOMSub=OpdServiceImpl.getReplaceString(UOMSub); UOMSubss=UOMSub.split(","); }
			 * 
			 * String[] subChargecodeIds=null;
			 * if(payload.containsKey("subChargecodeIdForInv")) { String
			 * subChargecodeId=payload.get("subChargecodeIdForInv").toString();
			 * subChargecodeId=OpdServiceImpl.getReplaceString(subChargecodeId);
			 * subChargecodeIds=subChargecodeId.split(","); }
			 * 
			 * 
			 * String[] mainChargecodeIdVals=null;
			 * if(payload.containsKey("mainChargecodeIdValForInv")) { String
			 * mainChargecodeIdVal=payload.get("mainChargecodeIdValForInv").toString();
			 * mainChargecodeIdVal=OpdServiceImpl.getReplaceString(mainChargecodeIdVal);
			 * mainChargecodeIdVals=mainChargecodeIdVal.split(","); }
			 * 
			 * 
			 * String[] subChargecodeIdsForSubs=null;
			 * if(payload.containsKey("subChargecodeIdForSub")) { String
			 * subChargecodeIdForSub=payload.get("subChargecodeIdForSub").toString();
			 * subChargecodeIdForSub=OpdServiceImpl.getReplaceString(subChargecodeIdForSub);
			 * subChargecodeIdsForSubs=subChargecodeIdForSub.split(","); }
			 * 
			 * 
			 * String[] mainChargecodeIdValsForSubs=null;
			 * if(payload.containsKey("mainChargecodeIdValForSub")) { String
			 * mainChargecodeIdValForSub=payload.get("mainChargecodeIdValForSub").toString()
			 * ; mainChargecodeIdValForSub=OpdServiceImpl.getReplaceString(
			 * mainChargecodeIdValForSub);
			 * mainChargecodeIdValsForSubs=mainChargecodeIdValForSub.split(","); }
			 * 
			 * 
			 * String[] dgResultDtIdValueSubInvess=null;
			 * if(payload.containsKey("dgResultDtIdValueSubInves")) { String
			 * dgResultDtIdValueSubInves=payload.get("dgResultDtIdValueSubInves").toString()
			 * ; dgResultDtIdValueSubInves=OpdServiceImpl.getReplaceString(
			 * dgResultDtIdValueSubInves);
			 * dgResultDtIdValueSubInvess=dgResultDtIdValueSubInves.split(","); }
			 * 
			 * 
			 * String[] dgResultHdIdSubInvess=null;
			 * if(payload.containsKey("dgResultHdIdSubInves")) { String
			 * dgResultHdIdSubInves=payload.get("dgResultHdIdSubInves").toString();
			 * dgResultHdIdSubInves=OpdServiceImpl.getReplaceString(dgResultHdIdSubInves);
			 * dgResultHdIdSubInvess=dgResultHdIdSubInves.split(","); } String
			 * actionDigiFileApproved ="";
			 * 
			 * 
			 * 
			 * String[] subInvestigationNameInvs=null;
			 * if(payload.containsKey("subInvestigationName")) { String
			 * subInvestigationName=payload.get("subInvestigationName").toString();
			 * subInvestigationName=OpdServiceImpl.getReplaceString(subInvestigationName);
			 * subInvestigationNameInvs=subInvestigationName.split(","); }
			 * /////////////////////////////////////////////////////////////////////////////
			 * ////////////////////////
			 * 
			 * String actionDigiFile=""; if(payload.containsKey("actionDigiFile")) {
			 * actionDigiFile = payload.get("actionDigiFile").toString(); actionDigiFile =
			 * OpdServiceImpl.getReplaceString(actionDigiFile); }
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { actionDigiFileApproved =
			 * payload.get("actionDigiFileApproved").toString(); actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved); }
			 * 
			 * 
			 * String otherInvestigation =""; if(payload.containsKey("otherInvestigation"))
			 * { otherInvestigation = payload.get("otherInvestigation").toString();
			 * otherInvestigation = OpdServiceImpl.getReplaceString(otherInvestigation); }
			 * 
			 * 
			 * //////////////////////////////////////////////////////////
			 * 
			 * String[] investigationResultDateArray=null;
			 * if(payload.containsKey("investigationResultDate")) { String
			 * investigationResultDate=payload.get("investigationResultDate").toString();
			 * investigationResultDate=OpdServiceImpl.getReplaceString(
			 * investigationResultDate);
			 * investigationResultDateArray=investigationResultDate.split(","); } String[]
			 * resultNumberArray=null; if(payload.containsKey("resultNumber")) { String
			 * resultNumber=payload.get("resultNumber").toString();
			 * resultNumber=OpdServiceImpl.getReplaceString(resultNumber);
			 * resultNumberArray=resultNumber.split(","); }
			 * /////////////////////////////////////////////////////////////
			 * 
			 * 
			 * 
			 * HashMap<String, String> mapInvestigationMap = new HashMap<>();
			 * 
			 * String finalValue = ""; Integer counter = 1; Date date=new Date(); String
			 * finalValue = ""; Integer counter = 1; Date date=new Date(); for (int i = 0; i
			 * < investigationIdValues.length; i++) {
			 * 
			 * if (StringUtils.isNotEmpty(investigationIdValues[i].toString()) &&
			 * !investigationIdValues[i].equalsIgnoreCase("0")) { finalValue +=
			 * investigationIdValues[i].trim();
			 * 
			 * if (i<dgOrderDtIdValues.length && dgOrderDtIdValues[i].equalsIgnoreCase("0")
			 * && StringUtils.isNotBlank(dgOrderDtIdValues[i])) { for (int m = i; m <
			 * dgOrderDtIdValues.length; m++) { finalValue += "###" +
			 * dgOrderDtIdValues[m].trim(); if (m == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * if (i<dgOrderHdIds.length && !dgOrderHdIds[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(dgOrderHdIds[i])) { for (int j = i; j <
			 * dgOrderHdIds.length; j++) { finalValue += "###" + dgOrderHdIds[j].trim(); if
			 * (j == i) { break; } } } else { finalValue += "###" + "0"; } if (i <
			 * dgResultDtIds.length && StringUtils.isNotBlank(dgResultDtIds[i]) &&
			 * !dgResultDtIds[i].equalsIgnoreCase("0")
			 * &&!dgResultDtIds[i].equalsIgnoreCase("undefined") ) { for (int k = i; k <
			 * dgResultDtIds.length; k++) { finalValue += "###" + dgResultDtIds[k].trim();
			 * if (k == i) { break; } } } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < dgResultHdIds.length && StringUtils.isNotBlank(dgResultHdIds[i]) &&
			 * !dgResultHdIds[i].trim().equalsIgnoreCase("0")
			 * &&!dgResultHdIds[i].equalsIgnoreCase("undefined")) { for (int k = i; k <
			 * dgResultHdIds.length; k++) { finalValue += "###" + dgResultHdIds[k].trim();
			 * if (k == i) { break; } } } else { finalValue += "###" + "0"; }
			 * 
			 * 
			 * int tempI=0; if(resultInvss!=null && resultInvss.length>0) { tempI=i; i++; if
			 * (i < resultInvss.length && StringUtils.isNotBlank(resultInvss[i]) &&
			 * !resultInvss[i].equalsIgnoreCase(",")) { for (int k = i; k <
			 * resultInvss.length; k++) { finalValue += "###" + resultInvss[k].trim(); if (k
			 * == i) { i=tempI; tempI=0; break; } } } else { i=tempI; tempI=0; finalValue +=
			 * "###" + "0"; } } else { i=tempI; tempI=0; finalValue += "###" + "0"; }
			 * 
			 * if (i < rangeValue.length && StringUtils.isNotBlank(rangeValue[i])) { for
			 * (int k = i; k < rangeValue.length; k++) { finalValue += "###" +
			 * rangeValue[k].trim(); if (k == i) { break; } } } else { finalValue += "###" +
			 * "0"; }
			 * 
			 * if (i < rmsIdFinalValue.length && StringUtils.isNotBlank(rmsIdFinalValue[i]))
			 * { for (int k = i; k < rmsIdFinalValue.length; k++) { finalValue += "###" +
			 * rmsIdFinalValue[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * 
			 * if (i < investigationTypes.length &&
			 * StringUtils.isNotBlank(investigationTypes[i])) { for (int k = i; k <
			 * investigationTypes.length; k++) { finalValue += "###" +
			 * investigationTypes[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * 
			 * if (i < subChargecodeIds.length &&
			 * StringUtils.isNotBlank(subChargecodeIds[i])) { for (int k = i; k <
			 * subChargecodeIds.length; k++) { finalValue += "###" +
			 * subChargecodeIds[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * 
			 * if (i < mainChargecodeIdVals.length &&
			 * StringUtils.isNotBlank(mainChargecodeIdVals[i])) { for (int k = i; k <
			 * mainChargecodeIdVals.length; k++) { finalValue += "###" +
			 * mainChargecodeIdVals[k].trim(); if (k == i) { break; } } } else { finalValue
			 * += "###" + "0"; }
			 * 
			 * if (investigationResultDateArray!=null && i <
			 * investigationResultDateArray.length &&
			 * StringUtils.isNotBlank(investigationResultDateArray[i])) { for (int k = i; k
			 * < investigationResultDateArray.length; k++) { finalValue += "###" +
			 * investigationResultDateArray[k].trim(); if (k == i) { break; } } } else {
			 * finalValue += "###" + "0"; }
			 * 
			 * if (resultNumberArray!=null && i < resultNumberArray.length &&
			 * StringUtils.isNotBlank(resultNumberArray[i])) { for (int k = i; k <
			 * resultNumberArray.length; k++) { finalValue += "###" +
			 * resultNumberArray[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * 
			 * 
			 * 
			 * mapInvestigationMap.put(investigationIdValues[i].trim()+"@#"+counter,
			 * finalValue); finalValue = ""; counter++; } }
			 * 
			 * Long dgOrderHdId=null;
			 * 
			 * 
			 * 
			 * counter = 1; Long dgResultEntryHdFId=null; Character charVal=null; for
			 * (String investigationId : investigationIdValues) { String result=""; if
			 * (StringUtils.isNotEmpty(investigationId)) { if
			 * (mapInvestigationMap.containsKey(investigationId.trim()+"@#"+counter)) {
			 * String investigationValue =
			 * mapInvestigationMap.get(investigationId.trim()+"@#"+counter);
			 * 
			 * if (StringUtils.isNotEmpty(investigationValue)) {
			 * 
			 * String[] finalValueInvestigation = investigationValue.split("###");
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equals("0")&&
			 * !finalValueInvestigation[5].equalsIgnoreCase(",")) { charVal =
			 * finalValueInvestigation[5].charAt(finalValueInvestigation[5].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { result =
			 * finalValueInvestigation[5].toString().substring(0,
			 * finalValueInvestigation[5].toString().length() - 1); } else {
			 * result=getHtmlText(finalValueInvestigation[5].toString()); } } else {
			 * result=""; } DgOrderhd dgOrderhd=null; if(finalValueInvestigation[2]!="" &&
			 * !finalValueInvestigation[2].equalsIgnoreCase("0")) {
			 * dgOrderhd=dgOrderhdDao.find(Long.parseLong(finalValueInvestigation[2].trim())
			 * ); dgOrderHdId=dgOrderhd.getOrderhdId(); } if(dgOrderHdId==null)
			 * if(StringUtils.isNotEmpty(hospitalId) && patientId!=null && visitId!=null) {
			 * dgOrderhd=dgOrderhdDao.getDgOrderHdByHospitalIdAndPatientAndVisitId(Long.
			 * parseLong(hospitalId),Long.parseLong(patientId),Long.parseLong(visitId));
			 * if(dgOrderhd!=null) dgOrderHdId=dgOrderhd.getOrderhdId(); }
			 * if((dgOrderHdId==null && (dgOrderHdIds[0].equalsIgnoreCase("") ||
			 * dgOrderHdIds[0].equalsIgnoreCase("0")))&& StringUtils.isNotEmpty(result)) {
			 * dgOrderhd=new DgOrderhd();
			 * dgOrderhd.setHospitalId(Long.parseLong(hospitalId));
			 * dgOrderhd.setLastChgBy(Long.parseLong(userId)); if(dateOfExamValue!=null)
			 * dgOrderhd.setOrderDate(dateOfExamValue); dgOrderhd.setOrderStatus("C");
			 * if(StringUtils.isNotEmpty(visitId))
			 * dgOrderhd.setVisitId(Long.parseLong(visitId));
			 * if(StringUtils.isNotEmpty(patientId))
			 * dgOrderhd.setPatientId(Long.parseLong(patientId));
			 * dgOrderhd.setOtherInvestigation(otherInvestigation);
			 * dgOrderHdId=dgOrderhdDao.saveOrUpdateDgOrderHdInv(dgOrderhd);
			 * //dgOrderHdId=dgOrderhd.getOrderhdId(); } else { if(dgOrderHdId!=null &&
			 * dgOrderHdIds[0]!=null && !dgOrderHdIds[0].equalsIgnoreCase("") &&
			 * !dgOrderHdIds[0].equalsIgnoreCase("0"))
			 * dgOrderHdId=Long.parseLong(dgOrderHdIds[0].trim());
			 * 
			 * }
			 * 
			 * 
			 * if(finalValueInvestigation[8] != null &&
			 * !finalValueInvestigation[8].equals("0") && StringUtils.isNotEmpty(result) &&
			 * (finalValueInvestigation[8].equalsIgnoreCase("s")||finalValueInvestigation[8]
			 * .equalsIgnoreCase("t"))) { if(finalValueInvestigation[5]!=null)
			 * resultOfInvAndSub+=investigationId+"@@##"+" "+"@@##"+finalValueInvestigation[
			 * 5].toString()+"@@##"+" "+"@@##"+"@@@@@@";
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equals("0") && finalValueInvestigation[2] != null
			 * && StringUtils.isNotBlank(finalValueInvestigation[2]) &&
			 * !finalValueInvestigation[2].equals("0") ) { DgResultEntryDt dgResultEntryDt =
			 * null; if (finalValueInvestigation[3] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[3] ) &&
			 * !finalValueInvestigation[3].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(finalValueInvestigation[3])) { dgResultEntryDt =
			 * getDgResultEntryDtByDgResultEntryId(Long.parseLong(finalValueInvestigation[3]
			 * )); if(dgResultEntryDt==null) { dgResultEntryDt=new DgResultEntryDt(); } }
			 * else { dgResultEntryDt=new DgResultEntryDt();
			 * 
			 * }
			 * 
			 * if (finalValueInvestigation != null) {
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[0] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[0])) {
			 * dgResultEntryDt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].
			 * trim().toString()));
			 * 
			 * }
			 * 
			 * if (StringUtils.isNotEmpty(finalValueInvestigation[1]) &&
			 * !finalValueInvestigation[1].equals("0"))
			 * dgResultEntryDt.setOrderDtId(Long.parseLong(finalValueInvestigation[1].trim()
			 * .toString()));
			 * 
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equals("0")) { //int
			 * index=finalValueInvestigation[5].lastIndexOf(","); charVal =
			 * finalValueInvestigation[5].charAt(finalValueInvestigation[5].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { result =
			 * finalValueInvestigation[5].toString().substring(0,
			 * finalValueInvestigation[5].toString().length() - 1);
			 * dgResultEntryDt.setResult(getHtmlText(result)); } else {
			 * dgResultEntryDt.setResult(getHtmlText(finalValueInvestigation[5].toString()))
			 * ; result=getHtmlText(finalValueInvestigation[5].toString()); } } else {
			 * result=""; } if (finalValueInvestigation[6] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[6]) &&
			 * !finalValueInvestigation[6].equals("0"))
			 * dgResultEntryDt.setRangeValue(finalValueInvestigation[6].toString());
			 * 
			 * if (finalValueInvestigation[7] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[7]) &&
			 * !finalValueInvestigation[7].equals("0"))
			 * dgResultEntryDt.setRidcId(Long.parseLong(finalValueInvestigation[7].trim().
			 * toString()));
			 * 
			 * }
			 * 
			 * DgResultEntryHd dgResultEntryHd=null; Long dgResultentryId=null; if
			 * (finalValueInvestigation[4] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[4]) &&
			 * !finalValueInvestigation[4].equals("0")) dgResultEntryHd=
			 * getDgResultEntryHdByPatientIdAndHospitalId(Long.parseLong(
			 * finalValueInvestigation[4].trim().toString())) ;
			 * 
			 * if(dgResultEntryHd==null) {
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0") &&
			 * StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") && dgOrderHdId!=null) {
			 * dgResultEntryHd
			 * =dgResultEntryHdDao.getDgResultEntryHdByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueInvestigation[9]),Long.parseLong(finalValueInvestigation[
			 * 10]) ,dgOrderHdId); }
			 * 
			 * if(dgResultEntryHd!=null) {
			 * dgResultentryId=dgResultEntryHd.getResultEntryId(); } }
			 * 
			 * if(dgResultEntryHd!=null) { if(payload.containsKey("actionDigiFileApproved"))
			 * { actionDigiFileApproved = payload.get("actionDigiFileApproved").toString();
			 * actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||actionDigiFileApproved.
			 * equalsIgnoreCase("ev")) { dgResultEntryHd.setResultStatus("C");
			 * dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev")
			 * )) { dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setVerifiedOn(new Date());
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId)); } } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev")
			 * )) { dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setVerifiedOn(new Date());
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId)); } else {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); } } else {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); } if
			 * (dateOfExamValue!=null) { dgResultEntryHd.setResultDate(dateOfExamValue); }
			 * 
			 * else { dgResultEntryHd.setResultDate(new Date()); }
			 * //dgResultEntryHd.setVerifiedOn(new Date());
			 * //dgResultEntryHd.setVerified("V");
			 * 
			 * //dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0") )
			 * dgResultEntryHd.setSubChargecodeId(Long.parseLong(finalValueInvestigation[9])
			 * );
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") )
			 * dgResultEntryHd.setMainChargecodeId(Long.parseLong(finalValueInvestigation[10
			 * ]));
			 * 
			 * if(dgOrderHdId!=null) { dgResultEntryHd.setOrderHdId(dgOrderHdId); }
			 * 
			 * if(StringUtils.isNotEmpty(result)) {
			 * saveOrUpdateDgResultEntryHd(dgResultEntryHd) ;
			 * dgResultentryId=dgResultEntryHd.getResultEntryId(); } }
			 * 
			 * if(dgResultEntryHd==null) {
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0") &&
			 * StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") && dgOrderHdId!=null) {
			 * dgResultEntryHd
			 * =dgResultEntryHdDao.getDgResultEntryHdByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueInvestigation[9].trim()),Long.parseLong(
			 * finalValueInvestigation[10].trim()) ,dgOrderHdId); }
			 * 
			 * if(dgResultEntryHd==null) { dgResultEntryHd=new DgResultEntryHd();
			 * if(StringUtils.isNotEmpty(hospitalId))
			 * dgResultEntryHd.setHospitalId(Long.parseLong(hospitalId)); else {
			 * dgResultEntryHd.setHospitalId(null); } if(patientId!=null &&
			 * StringUtils.isNotEmpty(patientId))
			 * dgResultEntryHd.setPatientId(Long.parseLong(patientId)); else {
			 * dgResultEntryHd.setPatientId(null); } if(StringUtils.isNotEmpty(userId))
			 * dgResultEntryHd.setLastChgBy(Long.parseLong(userId)); else {
			 * dgResultEntryHd.setLastChgBy(null); } dgResultEntryHd.setLastChgDate(new
			 * Date());
			 * 
			 * if (dateOfExamValue!=null) { dgResultEntryHd.setResultDate(dateOfExamValue);
			 * } if(dgOrderHdId!=null ) { dgResultEntryHd.setOrderHdId(dgOrderHdId); }
			 * if(StringUtils.isNotEmpty(visitId)) {
			 * dgResultEntryHd.setVisitId(Long.parseLong(visitId)); }
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { actionDigiFileApproved =
			 * payload.get("actionDigiFileApproved").toString(); actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||actionDigiFileApproved.
			 * equalsIgnoreCase("ev")) { dgResultEntryHd.setResultStatus("C");
			 * dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev")
			 * )) { dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setVerifiedOn(new Date());
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId)); } } // else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) {
			 * 
			 * else if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") && (
			 * actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev"))
			 * ) { dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setVerifiedOn(new Date());
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId)); } else {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); } } else {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); } if
			 * (dateOfExamValue!=null) { dgResultEntryHd.setResultDate(dateOfExamValue); }
			 * 
			 * else { dgResultEntryHd.setResultDate(new Date()); }
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0") )
			 * dgResultEntryHd.setSubChargecodeId(Long.parseLong(finalValueInvestigation[9])
			 * );
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") )
			 * dgResultEntryHd.setMainChargecodeId(Long.parseLong(finalValueInvestigation[10
			 * ]));
			 * 
			 * if(StringUtils.isNotEmpty(result)) { dgResultEntryHdFId=
			 * saveOrUpdateDgResultEntryHd(dgResultEntryHd) ; } } else {
			 * if(dgResultEntryHd!=null)
			 * dgResultEntryHdFId=dgResultEntryHd.getResultEntryId(); } }
			 * if(dgResultEntryHdFId!=null) { dgResultentryId=dgResultEntryHdFId; }
			 * dgResultEntryDt.setResultEntryId(dgResultentryId);
			 * 
			 * if (finalValueInvestigation[1] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[1]) &&
			 * finalValueInvestigation[1].equals("0")) { DgOrderdt dgOrderdt=null;
			 * 
			 * 
			 * if(finalValueInvestigation[1]!="" &&
			 * !finalValueInvestigation[1].equalsIgnoreCase("0")) {
			 * dgOrderdt=dgOrderdtDao.find(Long.parseLong(finalValueInvestigation[1].trim())
			 * ); } if(dgOrderdt==null) { if(finalValueInvestigation[0]!=null &&
			 * !finalValueInvestigation[0].equalsIgnoreCase("0") && dgOrderHdId!=null) {
			 * 
			 * dgOrderdt
			 * =dgOrderdtDao.getDgOrderdtByInvestigationIdAndOrderhdId(Long.parseLong(
			 * finalValueInvestigation[0].trim()),dgOrderHdId); } } if(dgOrderdt==null) {
			 * dgOrderdt=new DgOrderdt(); }
			 * 
			 * 
			 * 
			 * if(finalValueInvestigation[0]!=null)
			 * dgOrderdt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].
			 * toString())); if(StringUtils.isNotEmpty(userId))
			 * dgOrderdt.setLastChgBy(Long.parseLong(userId)); else {
			 * dgOrderdt.setLastChgBy(null); } if(dgOrderHdId!=null) {
			 * dgOrderdt.setOrderhdId(dgOrderHdId); } dgOrderdt.setLastChgDate(new
			 * Timestamp(date.getTime())); if(dateOfExamValue!=null)
			 * dgOrderdt.setOrderDate(dateOfExamValue); dgOrderdt.setOrderStatus("C");
			 * 
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[11] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[11]) &&
			 * !finalValueInvestigation[11].equals("0")) { Date resultOfflineDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[11]);
			 * dgOrderdt.setResultOffLineDate(resultOfflineDate); } if
			 * (finalValueInvestigation != null && finalValueInvestigation[12] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[12]) &&
			 * !finalValueInvestigation[12].equals("0")) {
			 * dgOrderdt.setResultOffLineNumber(finalValueInvestigation[12]); }
			 * 
			 * 
			 * Long dgOrderDtId= saveUpdADgOrderDtForDg(dgOrderdt);
			 * dgResultEntryDt.setOrderDtId(dgOrderDtId); }
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { actionDigiFileApproved =
			 * payload.get("actionDigiFileApproved").toString(); actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||actionDigiFileApproved.
			 * equalsIgnoreCase("ev")) { dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V");
			 * 
			 * }
			 * 
			 * //else if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev")
			 * )) { dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); }
			 * 
			 * } //else if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev")
			 * )) { dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } else {
			 * dgResultEntryDt.setResultDetailStatus("E");
			 * dgResultEntryDt.setValidated(null); } } else {
			 * dgResultEntryDt.setResultDetailStatus("E");
			 * dgResultEntryDt.setValidated(null); }
			 * 
			 * if (finalValueInvestigation[8] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[8]) &&
			 * !finalValueInvestigation[8].equals("0"))
			 * dgResultEntryDt.setResultType(finalValueInvestigation[8].trim().toString());
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[11] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[11]) &&
			 * !finalValueInvestigation[11].equals("0")) { Date resultOfflineDate =null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueInvestigation[11]))
			 * resultOfflineDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[11].trim());
			 * if(resultOfflineDate!=null)
			 * dgResultEntryDt.setResultOffLineDate(resultOfflineDate); } if
			 * (finalValueInvestigation != null && finalValueInvestigation[12] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[12]) &&
			 * !finalValueInvestigation[12].equals("0")) {
			 * dgResultEntryDt.setResultOffLineNumber(finalValueInvestigation[12]); }
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(result)) {
			 * saveOrUpdateDgResultEntryDt(dgResultEntryDt) ; } }
			 * 
			 * else { if (finalValueInvestigation[1] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[1]) &&
			 * finalValueInvestigation[1].equals("0") && dgOrderHdIds[0]!=null) {
			 * 
			 * DgOrderdt dgOrderdt=null; if(finalValueInvestigation[1]!="" &&
			 * !finalValueInvestigation[1].equalsIgnoreCase("0")) {
			 * dgOrderdt=dgOrderdtDao.find(Long.parseLong(finalValueInvestigation[1].trim())
			 * ); } if(dgOrderdt==null) { if(finalValueInvestigation[0]!=null &&
			 * !finalValueInvestigation[0].equalsIgnoreCase("0") && dgOrderHdId!=null) {
			 * 
			 * dgOrderdt
			 * =dgOrderdtDao.getDgOrderdtByInvestigationIdAndOrderhdId(Long.parseLong(
			 * finalValueInvestigation[0].trim()),dgOrderHdId); } } if(dgOrderdt==null) {
			 * dgOrderdt=new DgOrderdt(); }
			 * 
			 * dgOrderdt.setOrderStatus("C"); dgOrderdt.setLabMark("O");
			 * dgOrderdt.setUrgent("n");
			 * dgOrderdt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].
			 * toString())); if(StringUtils.isNotEmpty(userId))
			 * dgOrderdt.setLastChgBy(Long.parseLong(userId)); else {
			 * dgOrderdt.setLastChgBy(null); } dgOrderdt.setOrderhdId(dgOrderHdId);
			 * dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
			 * if(dateOfExamValue!=null) dgOrderdt.setOrderDate(dateOfExamValue);
			 * 
			 * if (finalValueInvestigation != null && finalValueInvestigation[11] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[11]) &&
			 * !finalValueInvestigation[11].equals("0")) { Date resultOfflineDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[11]);
			 * dgOrderdt.setResultOffLineDate(resultOfflineDate); } if
			 * (finalValueInvestigation != null && finalValueInvestigation[12] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[12]) &&
			 * !finalValueInvestigation[12].equals("0")) {
			 * dgOrderdt.setResultOffLineNumber(finalValueInvestigation[12]); }
			 * 
			 * Long dgOrderDtId =saveUpdADgOrderDtForDg(dgOrderdt); DgResultEntryHd
			 * dgResultEntryHd=null;
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0") &&
			 * StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") && dgOrderHdId!=null) {
			 * dgResultEntryHd
			 * =dgResultEntryHdDao.getDgResultEntryHdByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueInvestigation[9]),Long.parseLong(finalValueInvestigation[
			 * 10]) ,dgOrderHdId); }
			 * 
			 * if(dgResultEntryHd==null) { dgResultEntryHd =new DgResultEntryHd();
			 * if(StringUtils.isNotEmpty(hospitalId))
			 * dgResultEntryHd.setHospitalId(Long.parseLong(hospitalId));
			 * 
			 * if(dgOrderHdId!=null) { dgResultEntryHd.setOrderHdId(dgOrderHdId); }
			 * //dgResultEntryHd.setInvestigationId(Long.parseLong(finalValueInvestigation[0
			 * ].toString())); if(StringUtils.isNotEmpty(patientId))
			 * dgResultEntryHd.setPatientId(Long.parseLong(patientId));
			 * dgResultEntryHd.setLastChgBy(Long.parseLong(userId));
			 * dgResultEntryHd.setLastChgDate(new Timestamp(date.getTime()));
			 * 
			 * if (dateOfExamValue!=null) { dgResultEntryHd.setResultDate(dateOfExamValue);
			 * }
			 * 
			 * else { dgResultEntryHd.setResultDate(new Date()); }
			 * 
			 * if(StringUtils.isNotEmpty(visitId)) {
			 * dgResultEntryHd.setVisitId(Long.parseLong(visitId)); }
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { actionDigiFileApproved =
			 * payload.get("actionDigiFileApproved").toString(); actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")
			 * ||actionDigiFileApproved.equalsIgnoreCase("ev") ) {
			 * dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev")
			 * )) { dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); } } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||actionDigiFile.equalsIgnoreCase("ev")
			 * )) { dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); } else {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); } } else {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); }
			 * 
			 * if(StringUtils.isNotEmpty(createdByMb) &&
			 * createdByMb.equalsIgnoreCase("yes")) {
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); }
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0") )
			 * dgResultEntryHd.setSubChargecodeId(Long.parseLong(finalValueInvestigation[9])
			 * );
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") )
			 * dgResultEntryHd.setMainChargecodeId(Long.parseLong(finalValueInvestigation[10
			 * ]));
			 * 
			 * if(StringUtils.isNotEmpty(result)) { dgResultEntryHdFId=
			 * saveOrUpdateDgResultEntryHd(dgResultEntryHd) ; }
			 * 
			 * } else { if(dgResultEntryHd!=null)
			 * dgResultEntryHdFId=dgResultEntryHd.getResultEntryId(); }
			 * 
			 * 
			 * //} DgResultEntryDt dgResultEntryDt=new DgResultEntryDt();
			 * dgResultEntryDt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].
			 * toString()));
			 * 
			 * if (finalValueInvestigation[5] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[5]) &&
			 * !finalValueInvestigation[5].equals("0")) { //int
			 * index=finalValueInvestigation[5].lastIndexOf(","); charVal =
			 * finalValueInvestigation[5].charAt(finalValueInvestigation[5].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { result =
			 * finalValueInvestigation[5].toString().substring(0,
			 * finalValueInvestigation[5].toString().length() - 1);
			 * dgResultEntryDt.setResult(getHtmlText(result)); } else {
			 * dgResultEntryDt.setResult(getHtmlText(finalValueInvestigation[5].toString()))
			 * ; result=getHtmlText(finalValueInvestigation[5].toString()); } } else {
			 * result=""; }
			 * 
			 * 
			 * if (finalValueInvestigation[6] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[6]) &&
			 * !finalValueInvestigation[6].equals("0"))
			 * dgResultEntryDt.setRangeValue(finalValueInvestigation[6].toString());
			 * 
			 * 
			 * if (finalValueInvestigation!=null && finalValueInvestigation[7] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[7]) &&
			 * !finalValueInvestigation[7].equals("0"))
			 * dgResultEntryDt.setRidcId(Long.parseLong(finalValueInvestigation[7].trim().
			 * toString())); dgResultEntryDt.setOrderDtId(dgOrderDtId);
			 * dgResultEntryDt.setResultEntryId(dgResultEntryHdFId);
			 * 
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { actionDigiFileApproved =
			 * payload.get("actionDigiFileApproved").toString(); actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea") ||
			 * actionDigiFileApproved.equalsIgnoreCase("ev")) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea") ||
			 * actionDigiFile.equalsIgnoreCase("ev"))) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||
			 * actionDigiFile.equalsIgnoreCase("ev"))) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } else {
			 * dgResultEntryDt.setResultDetailStatus("E");
			 * dgResultEntryDt.setValidated(null); } }else {
			 * dgResultEntryDt.setResultDetailStatus("E");
			 * dgResultEntryDt.setValidated(null); }
			 * 
			 * if(StringUtils.isNotEmpty(createdByMb) &&
			 * createdByMb.equalsIgnoreCase("yes")) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); }
			 * 
			 * 
			 * if (finalValueInvestigation[8] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[8]) &&
			 * !finalValueInvestigation[8].equals("0"))
			 * dgResultEntryDt.setResultType(finalValueInvestigation[8].trim().toString());
			 * 
			 * try { if (finalValueInvestigation != null && finalValueInvestigation[11] !=
			 * null && StringUtils.isNotBlank(finalValueInvestigation[11]) &&
			 * !finalValueInvestigation[11].equals("0")) { Date resultOfflineDate =null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueInvestigation[11].
			 * toString())) resultOfflineDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[11].trim());
			 * if(resultOfflineDate!=null)
			 * dgResultEntryDt.setResultOffLineDate(resultOfflineDate); } } catch(Exception
			 * e) {e.printStackTrace();} if (finalValueInvestigation != null &&
			 * finalValueInvestigation[12] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[12]) &&
			 * !finalValueInvestigation[12].equals("0")) {
			 * dgResultEntryDt.setResultOffLineNumber(finalValueInvestigation[12]); }
			 * 
			 * if(StringUtils.isNotEmpty(result)) {
			 * saveOrUpdateDgResultEntryDt(dgResultEntryDt) ; } } }
			 * 
			 * ///////////////////////////////////////////////////////////////////// }
			 * 
			 * else { if(finalValueInvestigation[8].equalsIgnoreCase("m"))
			 * saveUpdateSubInvestigation(investigationId,finalValueInvestigation,
			 * rangeSubInvess,resultSubInvs,subInvestigationNameIdAndInvs,
			 * dgOrderDtIdValueSubInvess,dgOrderHdIdSubInvess,
			 * Long.parseLong(patientId),hospitalId,userId,dgOrderHdId,date,
			 * UOMSubss,subChargecodeIdsForSubs,mainChargecodeIdValsForSubs,
			 * visitId,dgResultDtIdValueSubInvess,dgResultHdIdSubInvess,
			 * actionDigiFileApproved,subInvestigationNameInvs,dateOfExamValue,
			 * actionDigiFile, otherInvestigation,createdByMb);
			 * 
			 * }
			 * 
			 * 
			 * ///////////////////////////////////////////////////////////////
			 * ///////////////// } }
			 * 
			 * } counter++; }
			 * 
			 * //masMedicalExamReport=getInvestResultSetValue(chargeCodeNames,resultInvss,
			 * masMedicalExamReport); //} catch(Exception e) { e.printStackTrace(); return
			 * null; }
			 * 
			 * return masMedicalExamReport;
			 * 
			 * }
			 * 
			 * /////////////////////////////////////////////////////////////////////////////
			 * ////////////////////////////////////////
			 * 
			 * 
			 * @Transactional public void saveUpdateSubInvestigation(String
			 * investigationId,String[] finalValueInvestigation,String[]rangeSubInvess,
			 * String[]resultSubInvs,String[]subInvestigationNameIdAndInvs,String[]
			 * dgOrderDtIdValueSubInvess,String[] dgOrderHdIdSubInvess, Long patientId,
			 * String hospitalId, String userId,Long dgOrderHdId,Date date, String[]
			 * UOMSubss,String[]subChargecodeIdsForSubs, String[]
			 * mainChargecodeIdValsForSubs,String
			 * visitId,String[]dgResultDtIdValueSubInvess,String[]dgResultHdIdSubInvess,
			 * String actionDigiFileApproved,String[] subInvestigationNameInvs,Date
			 * dateOfExamValue,String actionDigiFile ,String otherInvestigation,String
			 * createdByMb) { HashMap<String, String> mapSubInvestigationMap = new
			 * HashMap<>(); String finalValue = "";
			 * 
			 * Integer counter=1; Integer resultCounter=0;
			 * if(subInvestigationNameIdAndInvs!=null &&
			 * subInvestigationNameIdAndInvs.length>0) for (int i = 0; i <
			 * subInvestigationNameIdAndInvs.length; i++) { String []
			 * invesAndSubInves=subInvestigationNameIdAndInvs[i].split("@@");
			 * if(invesAndSubInves[1]!=null &&
			 * invesAndSubInves[1].equalsIgnoreCase(investigationId.trim())) {
			 * 
			 * if (StringUtils.isNotEmpty(invesAndSubInves[0].toString()) &&
			 * !invesAndSubInves[0].equalsIgnoreCase("0")) { finalValue =
			 * investigationId.trim(); finalValue +="###"+ invesAndSubInves[0].trim();
			 * 
			 * if (i<dgOrderDtIdValueSubInvess.length &&
			 * dgOrderDtIdValueSubInvess.length>=countForOrderDtId &&
			 * !dgOrderDtIdValueSubInvess[countForOrderDtId].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(dgOrderDtIdValueSubInvess[countForOrderDtId])) {
			 * finalValue += "###" + dgOrderDtIdValueSubInvess[countForOrderDtId].trim(); }
			 * else { finalValue += "###" + "0"; }
			 * 
			 * if (i<dgOrderHdIdSubInvess.length &&
			 * !dgOrderHdIdSubInvess[countForOrderHdId].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(dgOrderHdIdSubInvess[countForOrderHdId])) {
			 * 
			 * finalValue += "###" + dgOrderHdIdSubInvess[countForOrderHdId].trim(); } else
			 * { finalValue += "###" + "0"; }
			 * 
			 * if (i < rangeSubInvess.length &&
			 * StringUtils.isNotBlank(rangeSubInvess[countForRange]) &&
			 * !rangeSubInvess[countForRange].equalsIgnoreCase("0")
			 * &&!rangeSubInvess[countForRange].equalsIgnoreCase("undefined") ) { finalValue
			 * += "###" + rangeSubInvess[countForRange].trim(); } else { finalValue += "###"
			 * + "0"; } if(resultSubInvs!=null && resultSubInvs.length>0) {
			 * 
			 * int tempI=i; int tempCountForResult=countForResult; i++; countForResult++; if
			 * (i < resultSubInvs.length &&
			 * StringUtils.isNotBlank(resultSubInvs[countForResult]) && countForResult <
			 * resultSubInvs.length &&
			 * !resultSubInvs[countForResult].trim().equalsIgnoreCase("0")
			 * &&!resultSubInvs[countForResult].equalsIgnoreCase("undefined") &&
			 * !resultSubInvs[countForResult].trim().equalsIgnoreCase(",")) { finalValue +=
			 * "###" + resultSubInvs[countForResult].trim();
			 * resultOfInvAndSub+=investigationId+"@@##"+subInvestigationNameInvs[
			 * countForSubInvestigationName]+"@@##"+resultSubInvs[countForResult].toString()
			 * +"@@##"+invesAndSubInves[0].toString()+"@@@@@@"; i =tempI;
			 * countForResult=tempCountForResult; resultCounter++; } else { finalValue +=
			 * "###" + "0"; i =tempI; countForResult=tempCountForResult;
			 * resultOfInvAndSub+=investigationId+"@@##"+subInvestigationNameInvs[
			 * countForSubInvestigationName]+"@@##"+"0"+"@@##"+invesAndSubInves[0].toString(
			 * )+"@@@@@@"; }
			 * 
			 * } else { finalValue += "###" + "0";
			 * resultOfInvAndSub+=investigationId+"@@##"+subInvestigationNameInvs[
			 * countForSubInvestigationName]+"@@##"+"0"+"@@##"+invesAndSubInves[0].toString(
			 * )+"@@@@@@"; }
			 * 
			 * if (i < UOMSubss.length &&
			 * StringUtils.isNotBlank(UOMSubss[countForUmoSubInv]) &&
			 * !UOMSubss[countForUmoSubInv].trim().equalsIgnoreCase("0")
			 * &&!UOMSubss[countForUmoSubInv].equalsIgnoreCase("undefined")) { finalValue +=
			 * "###" + UOMSubss[countForUmoSubInv].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < subChargecodeIdsForSubs.length &&
			 * StringUtils.isNotBlank(subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub
			 * ]) && !subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub].trim().
			 * equalsIgnoreCase("0")
			 * &&!subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub].equalsIgnoreCase(
			 * "undefined")) { finalValue += "###" +
			 * subChargecodeIdsForSubs[countSubMainChargeCodeIdForSub].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < mainChargecodeIdValsForSubs.length &&
			 * StringUtils.isNotBlank(mainChargecodeIdValsForSubs[
			 * countMainChargeCodeIdForSub]) &&
			 * !mainChargecodeIdValsForSubs[countMainChargeCodeIdForSub].trim().
			 * equalsIgnoreCase("0")
			 * &&!mainChargecodeIdValsForSubs[countMainChargeCodeIdForSub].equalsIgnoreCase(
			 * "undefined")) { finalValue += "###" +
			 * mainChargecodeIdValsForSubs[countMainChargeCodeIdForSub].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < dgResultDtIdValueSubInvess.length &&
			 * StringUtils.isNotBlank(dgResultDtIdValueSubInvess[
			 * countDgResultDtIdValueSubInvess]) &&
			 * !dgResultDtIdValueSubInvess[countDgResultDtIdValueSubInvess].trim().
			 * equalsIgnoreCase("0")
			 * &&!dgResultDtIdValueSubInvess[countDgResultDtIdValueSubInvess].
			 * equalsIgnoreCase("undefined")) { finalValue += "###" +
			 * dgResultDtIdValueSubInvess[countDgResultDtIdValueSubInvess].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * if (i < dgResultHdIdSubInvess.length &&
			 * StringUtils.isNotBlank(dgResultHdIdSubInvess[countDgResultHdIdSubInvess]) &&
			 * !dgResultHdIdSubInvess[countDgResultHdIdSubInvess].trim().equalsIgnoreCase(
			 * "0") &&!dgResultHdIdSubInvess[countDgResultHdIdSubInvess].equalsIgnoreCase(
			 * "undefined")) { finalValue += "###" +
			 * dgResultHdIdSubInvess[countDgResultHdIdSubInvess].trim();
			 * 
			 * } else { finalValue += "###" + "0"; }
			 * 
			 * mapSubInvestigationMap.put(invesAndSubInves[0].trim()+"@#"+counter,
			 * finalValue); countForRange=countForRange+1; countForResult=countForResult+1;
			 * countForOrderDtId=countForOrderDtId+1; countForOrderHdId=countForOrderHdId+1;
			 * countForUmoSubInv=countForUmoSubInv+1;
			 * countSubMainChargeCodeIdForSub=countSubMainChargeCodeIdForSub+1;
			 * countMainChargeCodeIdForSub=countMainChargeCodeIdForSub+1;
			 * 
			 * countDgResultDtIdValueSubInvess=countDgResultDtIdValueSubInvess+1;
			 * countDgResultHdIdSubInvess=countDgResultHdIdSubInvess+1;
			 * countForSubInvestigationName=countForSubInvestigationName+1; finalValue = "";
			 * counter++; System.out.println("countForRange"+countForRange+"countForResult"+
			 * countForResult); } }
			 * 
			 * } counter=1; Long dgResultEntryHdFIdM=null;
			 * if(subInvestigationNameIdAndInvs!=null && resultCounter>0) { for(String
			 * subInves:subInvestigationNameIdAndInvs) { String []
			 * invesAndSubInves=subInves.split("@@"); if
			 * (mapSubInvestigationMap.containsKey(invesAndSubInves[0].trim()+"@#"+counter))
			 * { String finalValueOfMap =
			 * mapSubInvestigationMap.get(invesAndSubInves[0].trim()+"@#"+counter);
			 * 
			 * String[] finalValueOfMaps =finalValueOfMap.split("###"); DgOrderhd
			 * dgOrderhd=null; if(finalValueOfMaps[3]!="" &&
			 * !finalValueOfMaps[3].equalsIgnoreCase("0")) {
			 * dgOrderhd=dgOrderhdDao.find(Long.parseLong(finalValueOfMaps[3].trim()));
			 * dgOrderHdId=dgOrderhd.getOrderhdId(); } if(dgOrderHdId==null)
			 * if(StringUtils.isNotEmpty(hospitalId) && patientId!=null && visitId!=null) {
			 * dgOrderhd=dgOrderhdDao.getDgOrderHdByHospitalIdAndPatientAndVisitId(Long.
			 * parseLong(hospitalId),patientId,Long.parseLong(visitId)); if(dgOrderhd!=null)
			 * dgOrderHdId=dgOrderhd.getOrderhdId(); } if(dgOrderhd==null &&
			 * dgOrderHdId==null) { dgOrderhd=new DgOrderhd();
			 * dgOrderhd.setHospitalId(Long.parseLong(hospitalId));
			 * dgOrderhd.setLastChgBy(Long.parseLong(userId));
			 * dgOrderhd.setPatientId(patientId); if(dateOfExamValue!=null)
			 * dgOrderhd.setOrderDate(dateOfExamValue); dgOrderhd.setOrderStatus("C");
			 * if(StringUtils.isNotEmpty(visitId)) {
			 * dgOrderhd.setVisitId(Long.parseLong(visitId)); }
			 * dgOrderhd.setOtherInvestigation(otherInvestigation);
			 * dgOrderHdId=dgOrderhdDao.saveOrUpdateDgOrderHdInv(dgOrderhd); //
			 * dgOrderHdId=dgOrderhd.getOrderhdId();
			 * 
			 * }
			 * 
			 * 
			 * DgOrderdt dgOrderdt=null;
			 * 
			 * if(finalValueOfMaps[2]!="" && !finalValueOfMaps[2].equalsIgnoreCase("0")) {
			 * dgOrderdt=dgOrderdtDao.find(Long.parseLong(finalValueOfMaps[2].trim())); }
			 * if(dgOrderdt==null) { if(finalValueOfMaps[0]!=null &&
			 * !finalValueOfMaps[0].equalsIgnoreCase("0") && dgOrderHdId!=null) {
			 * dgOrderdt=dgOrderdtDao.getDgOrderdtByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueOfMaps[0].trim()),dgOrderHdId);
			 * 
			 * } } if(dgOrderdt==null) { dgOrderdt=new DgOrderdt();
			 * dgOrderdt.setOrderStatus("C"); dgOrderdt.setLabMark("O");
			 * dgOrderdt.setUrgent("n"); if(finalValueOfMaps[0]!=null)
			 * dgOrderdt.setInvestigationId(Long.parseLong(finalValueOfMaps[0].toString()));
			 * if(StringUtils.isNotEmpty(userId))
			 * dgOrderdt.setLastChgBy(Long.parseLong(userId)); else {
			 * dgOrderdt.setLastChgBy(null); } dgOrderdt.setOrderhdId(dgOrderHdId);
			 * dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
			 * if(dateOfExamValue!=null) dgOrderdt.setOrderDate(dateOfExamValue); Long
			 * dgOrderDtId = saveUpdADgOrderDtForDg(dgOrderdt); //Long dgOrderDtId
			 * =dgOrderdt.getOrderdtId(); }
			 * 
			 * DgResultEntryHd dgResultEntryHd=null;
			 * 
			 * if (finalValueOfMaps[4] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[10]) &&
			 * !finalValueOfMaps[10].equals("0")) dgResultEntryHd=
			 * getDgResultEntryHdByPatientIdAndHospitalId(Long.parseLong(finalValueOfMaps[10
			 * ].trim().toString())) ;
			 * 
			 * if(dgResultEntryHd==null) {
			 * 
			 * if(StringUtils.isNotEmpty(finalValueOfMaps[7]) &&
			 * !finalValueOfMaps[7].equals("0") &&
			 * StringUtils.isNotEmpty(finalValueOfMaps[8]) &&
			 * !finalValueInvestigation[8].equals("0") && dgOrderHdId!=null) {
			 * 
			 * 
			 * dgResultEntryHd
			 * =dgResultEntryHdDao.getDgResultEntryHdByInvestigationIdAndOrderhdId(Long.
			 * parseLong(finalValueInvestigation[9]),Long.parseLong(finalValueInvestigation[
			 * 10]) ,dgOrderHdId);
			 * 
			 * }
			 * 
			 * 
			 * if(dgResultEntryHd!=null) {
			 * dgResultEntryHdFIdM=dgResultEntryHd.getResultEntryId(); } }
			 * 
			 * if(dgResultEntryHd!=null) {
			 * dgResultEntryHdFIdM=dgResultEntryHd.getResultEntryId(); }
			 * 
			 * 
			 * if(dgResultEntryHdFIdM==null) { dgResultEntryHd =new DgResultEntryHd();
			 * dgResultEntryHd.setHospitalId(Long.parseLong(hospitalId));
			 * dgResultEntryHd.setOrderHdId(dgOrderHdId);
			 * dgResultEntryHd.setPatientId(patientId);
			 * dgResultEntryHd.setLastChgBy(Long.parseLong(userId));
			 * dgResultEntryHd.setLastChgDate(new Timestamp(date.getTime()));
			 * if(dateOfExamValue!=null) dgResultEntryHd.setResultDate(dateOfExamValue);
			 * //dgResultEntryHd.setVerifiedOn(new Date());
			 * 
			 * //dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * if(StringUtils.isNotEmpty(visitId)) {
			 * dgResultEntryHd.setVisitId(Long.parseLong(visitId)); }
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[9]) &&
			 * !finalValueInvestigation[9].equals("0") )
			 * dgResultEntryHd.setSubChargecodeId(Long.parseLong(finalValueInvestigation[9])
			 * );
			 * 
			 * if(StringUtils.isNotEmpty(finalValueInvestigation[10]) &&
			 * !finalValueInvestigation[10].equals("0") )
			 * dgResultEntryHd.setMainChargecodeId(Long.parseLong(finalValueInvestigation[10
			 * ]));
			 * 
			 * } if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea") ||
			 * actionDigiFileApproved.equalsIgnoreCase("ev")) {
			 * dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||
			 * actionDigiFile.equalsIgnoreCase("ev"))) {
			 * dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); }
			 * 
			 * } //else if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||
			 * actionDigiFile.equalsIgnoreCase("ev"))) {
			 * dgResultEntryHd.setResultStatus("C"); dgResultEntryHd.setVerified("V");
			 * dgResultEntryHd.setResultVerifiedBy(Long.parseLong(userId));
			 * dgResultEntryHd.setVerifiedOn(new Date()); } else {
			 * dgResultEntryHd.setResultStatus("E"); dgResultEntryHd.setVerified(null);
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); }
			 * 
			 * if(StringUtils.isNotEmpty(createdByMb) &&
			 * createdByMb.equalsIgnoreCase("yes")) {
			 * dgResultEntryHd.setCreatedBy(Long.parseLong(userId)); }
			 * 
			 * 
			 * dgResultEntryHdFIdM= saveOrUpdateDgResultEntryHd(dgResultEntryHd) ;
			 * DgResultEntryDt dgResultEntryDt = null; if (finalValueOfMaps[9] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[9] ) &&
			 * !finalValueOfMaps[9].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(finalValueOfMaps[9])) { dgResultEntryDt =
			 * getDgResultEntryDtByDgResultEntryId(Long.parseLong(finalValueOfMaps[9]));
			 * if(dgResultEntryDt==null) { dgResultEntryDt=new DgResultEntryDt(); } } else {
			 * dgResultEntryDt=new DgResultEntryDt();
			 * 
			 * }
			 * 
			 * //DgResultEntryDt dgResultEntryDt=new DgResultEntryDt();
			 * dgResultEntryDt.setInvestigationId(Long.parseLong(finalValueOfMaps[0].
			 * toString())); Character charVal=null; if (finalValueOfMaps[5] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[5]) &&
			 * !finalValueOfMaps[5].equals("0")) { //int
			 * index=finalValueOfMaps[5].lastIndexOf(","); charVal =
			 * finalValueOfMaps[5].charAt(finalValueOfMaps[5].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { String result =
			 * finalValueOfMaps[5].toString().substring(0,
			 * finalValueOfMaps[5].toString().length() - 1);
			 * dgResultEntryDt.setResult(getHtmlText(result)); } else {
			 * dgResultEntryDt.setResult(getHtmlText(finalValueOfMaps[5].toString())); } }
			 * if (finalValueOfMaps[4] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[4]) &&
			 * !finalValueOfMaps[4].equals("0"))
			 * dgResultEntryDt.setRangeValue(finalValueOfMaps[4].toString());
			 * 
			 * if (finalValueOfMaps[1] != null &&
			 * StringUtils.isNotBlank(finalValueOfMaps[1]) &&
			 * !finalValueOfMaps[1].equals("0"))
			 * dgResultEntryDt.setSubInvestigationId(Long.parseLong(finalValueOfMaps[1].
			 * toString()));
			 * 
			 * 
			 * if (finalValueInvestigation!=null && finalValueInvestigation[7] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[7]) &&
			 * !finalValueInvestigation[7].equals("0"))
			 * dgResultEntryDt.setRidcId(Long.parseLong(finalValueInvestigation[7].trim().
			 * toString()));
			 * 
			 * if (finalValueInvestigation[8] != null &&
			 * StringUtils.isNotBlank(finalValueInvestigation[8]) &&
			 * !finalValueInvestigation[8].equals("0"))
			 * dgResultEntryDt.setResultType(finalValueInvestigation[8].trim().toString());
			 * 
			 * dgResultEntryDt.setOrderDtId(dgOrderdt.getOrderdtId());
			 * dgResultEntryDt.setResultEntryId(dgResultEntryHdFIdM);
			 * 
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||
			 * actionDigiFileApproved.equalsIgnoreCase("ev")) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } // else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||
			 * actionDigiFile.equalsIgnoreCase("ev"))) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } } //else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * actionDigiFile.equalsIgnoreCase("ev")) { else
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0") &&
			 * (actionDigiFile.equalsIgnoreCase("ea")||
			 * actionDigiFile.equalsIgnoreCase("ev"))) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); } else {
			 * dgResultEntryDt.setResultDetailStatus("E");
			 * dgResultEntryDt.setValidated(null); } if(StringUtils.isNotEmpty(createdByMb)
			 * && createdByMb.equalsIgnoreCase("yes")) {
			 * dgResultEntryDt.setResultDetailStatus("C");
			 * dgResultEntryDt.setValidated("V"); }
			 * 
			 * saveOrUpdateDgResultEntryDt(dgResultEntryDt) ; counter++; }
			 * 
			 * } } }
			 * 
			 * 
			 * /////////////////////////////////////////////////////////////////////////////
			 * ///////////////////////////////////////
			 * 
			 * @Override public MasMedicalExamReport
			 * saveUpdateForReferalforMeDigiFileUpload(MasMedicalExamReport
			 * masMedicalExamReprt,HashMap<String, Object> payload,String hospitalId) {
			 * //try { OpdPatientDetail opdPatientDetail=null; String
			 * actionMe=payload.get("actionMe").toString();
			 * actionMe=OpdServiceImpl.getReplaceString(actionMe);
			 * 
			 * String visitId = payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * //if(StringUtils.isNotEmpty(actionMe) &&
			 * actionMe.equalsIgnoreCase("referred")) { if(StringUtils.isNotEmpty(visitId))
			 * opdPatientDetail= getOpdPatientDetailByVisitId(Long.parseLong(visitId));
			 * if(opdPatientDetail==null) { opdPatientDetail=new OpdPatientDetail(); Date
			 * date=new Date(); opdPatientDetail.setLastChgDate(new
			 * Timestamp(date.getTime())); }
			 * 
			 * 
			 * if(payload.get("chiefComplaint")!=null) { String
			 * chiefComplaint=payload.get("chiefComplaint").toString();
			 * chiefComplaint=OpdServiceImpl.getReplaceString(chiefComplaint);
			 * masMedicalExamReprt.setChiefComplaint(chiefComplaint);
			 * 
			 * //Save or Update patient history
			 * 
			 * String patientId = payload.get("patientId").toString(); patientId =
			 * OpdServiceImpl.getReplaceString(patientId);
			 * 
			 * // String hospitalId = payload.get("hospitalId").toString(); // hospitalId =
			 * OpdServiceImpl.getReplaceString(hospitalId);
			 * 
			 * String userId = payload.get("userId").toString(); userId =
			 * OpdServiceImpl.getReplaceString(userId); OpdPatientHistory
			 * opdPatientHistory=null; if(StringUtils.isNotEmpty(visitId)) {
			 * opdPatientHistory
			 * =checkVisitOpdPatientHistoryByVisitIdAndPatientId(Long.parseLong(visitId));
			 * if(opdPatientHistory==null) { opdPatientHistory=new OpdPatientHistory();
			 * if(StringUtils.isNotEmpty(visitId))
			 * opdPatientHistory.setVisitId(Long.parseLong(visitId)); } } else {
			 * opdPatientHistory=new OpdPatientHistory();
			 * if(StringUtils.isNotEmpty(visitId))
			 * opdPatientHistory.setVisitId(Long.parseLong(visitId)); }
			 * opdPatientHistory.setChiefComplain(chiefComplaint);
			 * if(StringUtils.isNotEmpty(hospitalId) && !hospitalId.equalsIgnoreCase("0"))
			 * opdPatientHistory.setHospitalId(Long.parseLong(hospitalId));
			 * 
			 * if(StringUtils.isNotEmpty(patientId))
			 * opdPatientHistory.setPatientId(Long.parseLong(patientId));
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(userId))
			 * opdPatientHistory.setLastChgBy(Long.parseLong(userId)); Date date=new Date();
			 * opdPatientHistory.setLastChgDate(new Timestamp(date.getTime()));
			 * saveOrUpdateOpdPatientHis(opdPatientHistory);
			 * 
			 * }
			 * 
			 * if(payload.get("remarksForReferal")!=null) { String
			 * remarksForReferal=payload.get("remarksForReferal").toString();
			 * remarksForReferal=OpdServiceImpl.getReplaceString(remarksForReferal);
			 * masMedicalExamReprt.setRemarksRef(remarksForReferal); }
			 * if(payload.get("Pollar")!=null) { String pollor =
			 * payload.get("Pollar").toString(); pollor =
			 * OpdServiceImpl.getReplaceString(pollor.trim());
			 * masMedicalExamReprt.setPollor(pollor); opdPatientDetail.setPollor(pollor); }
			 * 
			 * if(payload.get("Ordema")!=null) { String edema =
			 * payload.get("Ordema").toString(); edema =OpdServiceImpl.
			 * getReplaceString(edema); masMedicalExamReprt.setEdema(edema.trim());
			 * opdPatientDetail.setEdema(edema); } if(payload.get("cyanosis")!=null) {
			 * String cyanosis = payload.get("cyanosis").toString(); cyanosis =
			 * OpdServiceImpl.getReplaceString(cyanosis.trim());
			 * masMedicalExamReprt.setCyanosis(cyanosis);
			 * opdPatientDetail.setCyanosis(cyanosis); } if(payload.get("hairNail")!=null) {
			 * String hairNail = payload.get("hairNail").toString(); hairNail
			 * =OpdServiceImpl. getReplaceString(hairNail.trim());
			 * masMedicalExamReprt.setHairNail(hairNail);
			 * opdPatientDetail.setHairNail(hairNail); }
			 * 
			 * if( payload.get("Icterus")!=null) { String icterus =
			 * payload.get("Icterus").toString(); icterus =
			 * OpdServiceImpl.getReplaceString(icterus.trim());
			 * masMedicalExamReprt.setIcterus(icterus);
			 * opdPatientDetail.setIcterus(icterus); }
			 * 
			 * if( payload.get("GCS")!=null) { String gcs = payload.get("GCS").toString();
			 * gcs = OpdServiceImpl.getReplaceString(gcs.trim());
			 * masMedicalExamReprt.setGcs(gcs); opdPatientDetail.setGcs(gcs); } if(
			 * payload.get("lymphNode")!=null) { String lymphNode =
			 * payload.get("lymphNode").toString(); lymphNode =
			 * OpdServiceImpl.getReplaceString(lymphNode.trim());
			 * masMedicalExamReprt.setLymphNode(lymphNode);
			 * opdPatientDetail.setLymphNode(lymphNode);
			 * 
			 * } if( payload.get("Clubbing")!=null) { String clubbing =
			 * payload.get("Clubbing").toString(); clubbing =
			 * OpdServiceImpl.getReplaceString(clubbing.trim());
			 * masMedicalExamReprt.setClubbing(clubbing);
			 * opdPatientDetail.setClubbing(clubbing); } if(payload.get("Tremors")!=null) {
			 * String tremors = payload.get("Tremors").toString(); tremors =
			 * OpdServiceImpl.getReplaceString(tremors.trim());
			 * masMedicalExamReprt.setGeTremors(tremors);
			 * opdPatientDetail.setTremors(tremors); } if( payload.get("Others")!=null) {
			 * String others = payload.get("Others").toString(); others =
			 * OpdServiceImpl.getReplaceString(others.trim());
			 * masMedicalExamReprt.setOthers(others); } if( payload.get("Others")!=null) {
			 * String others = payload.get("Others").toString(); others =
			 * OpdServiceImpl.getReplaceString(others.trim());
			 * masMedicalExamReprt.setOthers(others); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getHeight()!=null) {
			 * opdPatientDetail.setHeight(masMedicalExamReprt.getHeight().toString()); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getWeight()!=null) {
			 * opdPatientDetail.setWeight(masMedicalExamReprt.getWeight().toString()); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getIdealweight()!=null) {
			 * opdPatientDetail.setIdealWeight(masMedicalExamReprt.getIdealweight().toString
			 * ()); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getPulseRates()!=null) {
			 * opdPatientDetail.setPulse(masMedicalExamReprt.getPulseRates().toString()); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getBpSystolic()!=null) {
			 * opdPatientDetail.setBpSystolic(masMedicalExamReprt.getBpSystolic().toString()
			 * ); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getBpDiastolic()!=null) {
			 * opdPatientDetail.setBpDiastolic(masMedicalExamReprt.getBpDiastolic().toString
			 * ()); } String dateOfExam =""; if(payload.containsKey("dateOfExam")) {
			 * dateOfExam = payload.get("dateOfExam").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam); if
			 * (StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date dateOfExamValue
			 * =null; dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(dateOfExamValue!=null) opdPatientDetail.setOpdDate(new
			 * Timestamp(dateOfExamValue.getTime())); } else { try { Date opdDate=new
			 * Date(); opdPatientDetail.setOpdDate(new Timestamp(opdDate.getTime())); }
			 * catch(Exception e) {e.printStackTrace();} }
			 * 
			 * } if(payload.containsKey("dateOfRelease")) { dateOfExam =
			 * payload.get("dateOfRelease").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam); if
			 * (StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(dateOfExamValue!=null) opdPatientDetail.setOpdDate(new
			 * Timestamp(dateOfExamValue.getTime())); } else { try { Date opdDate=new
			 * Date(); opdPatientDetail.setOpdDate(new Timestamp(opdDate.getTime())); }
			 * catch(Exception e) {e.printStackTrace();} }
			 * 
			 * }
			 * 
			 * 
			 * 
			 * 
			 * //Update Referal Detail
			 * saveOrUpdateReferaMedicalExamDigiFileUpload(payload,opdPatientDetail,
			 * hospitalId);
			 * 
			 * 
			 * //}
			 * 
			 * } catch(Exception e) { e.printStackTrace(); return null; } return
			 * masMedicalExamReprt; }
			 * 
			 * 
			 * public void saveOrUpdateReferaMedicalExamDigiFileUpload(HashMap<String,
			 * Object> payload,OpdPatientDetail opdPatientDetail,String hospitalId) { //try
			 * { Long opdPatientDetailId=null; String patientId =
			 * payload.get("patientId").toString(); patientId =
			 * OpdServiceImpl.getReplaceString(patientId);
			 * 
			 * if(StringUtils.isNotEmpty(patientId))
			 * opdPatientDetail.setPatientId(Long.parseLong(patientId));
			 * 
			 * String visitId = payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId); if(StringUtils.isNotEmpty(visitId))
			 * opdPatientDetail.setVisitId(Long.parseLong(visitId)); try {
			 * if(payload.containsKey("diagnosisId")) { String diagnosisId1 =
			 * payload.get("diagnosisId").toString(); String diagnosisId2 =
			 * OpdServiceImpl.getReplaceString(diagnosisId1);
			 * 
			 * if(StringUtils.isNotEmpty(diagnosisId2)) {
			 * opdPatientDetail.setIcdDiagnosis(diagnosisId1); } } } catch(Exception e) {
			 * e.printStackTrace(); } if(opdPatientDetail!=null) opdPatientDetailId=
			 * updateOpdPatientDetailMe(opdPatientDetail);
			 * 
			 * String referHospitalList = payload.get("medicalExamReferalHos").toString();
			 * referHospitalList = OpdServiceImpl.getReplaceString(referHospitalList);
			 * String referHospitalValues = ""; if
			 * (StringUtils.isNotBlank(referHospitalList)) { if
			 * (referHospitalList.contains("@")) { String[] referHospitalListValue =
			 * referHospitalList.split(","); int count = 0; for (int i = count; i <
			 * referHospitalListValue.length; i++) { String[] referHospitalListValueNew =
			 * referHospitalListValue[i].split("@"); referHospitalValues +=
			 * referHospitalListValueNew[0] + ","; // count+=2; } } referHospitalList =
			 * referHospitalValues; }
			 * 
			 * String departmentValue = payload.get("departmentValue").toString();
			 * departmentValue = OpdServiceImpl.getReplaceString(departmentValue);
			 * 
			 * String diagonsisIdValue = payload.get("diagonsisId").toString();
			 * diagonsisIdValue = OpdServiceImpl.getReplaceString(diagonsisIdValue);
			 * 
			 * String instruction = payload.get("instruction").toString(); instruction =
			 * OpdServiceImpl.getReplaceString(instruction);
			 * 
			 * String referalPatientDt = payload.get("referalPatientDt").toString();
			 * referalPatientDt = OpdServiceImpl.getReplaceString(referalPatientDt);
			 * 
			 * String referalPatientHd = payload.get("referalPatientHd").toString();
			 * referalPatientHd = OpdServiceImpl.getReplaceString(referalPatientHd);
			 * 
			 * saveOrUpdateReferrPatientDigifileUpload(referHospitalList,departmentValue,
			 * diagonsisIdValue, instruction, referalPatientDt, referalPatientHd,
			 * opdPatientDetailId.toString(), patientId,null,null, payload,hospitalId);
			 * 
			 * if(payload.get("diagnosisiIdMC")!=null &&
			 * payload.containsKey("diagnosisiIdMC")) { saveOrUpdateDischargeIcg(payload,
			 * visitId, opdPatientDetailId, patientId); } //} catch(Exception e) {
			 * e.printStackTrace(); } }
			 * 
			 * 
			 * 
			 * // Saveupdadate Referal candidate public void
			 * saveOrUpdateReferrPatientDigifileUpload(String referHospitalList, String
			 * departmentValue, String diagonsisId, String hos, String
			 * referalPatientDtValue, String referalPatientHdValue, String
			 * opdPatientDetailsId, String patient,String referalNotes,String
			 * referalDates,HashMap<String, Object> payload,String hospitalId) { String
			 * referToValue=""; //try {
			 * 
			 * String[] referHospitalListValue = referHospitalList.split(","); String[]
			 * departmentValueArray = departmentValue.split(","); String[] diagonsisIdValue
			 * = diagonsisId.split(","); String[] hosValue = hos.split(",");
			 * 
			 * String[] referalPatientDtValueArray = referalPatientDtValue.split(",");
			 * String[] referalPatientHdValueArray = referalPatientHdValue.split(",");
			 * 
			 * String referalRmsId = payload.get("referalRmsId").toString(); referalRmsId =
			 * OpdServiceImpl.getReplaceString(referalRmsId); String[]
			 * referalRmsIdValueArrays = referalRmsId.split(",");
			 * 
			 * String totalLengthDigiFileRefer =
			 * payload.get("totalLengthDigiFileRefer").toString(); totalLengthDigiFileRefer
			 * = OpdServiceImpl.getReplaceString(totalLengthDigiFileRefer); String
			 * fileUploadFinalVal=""; if(StringUtils.isNotEmpty(totalLengthDigiFileRefer)&&
			 * !totalLengthDigiFileRefer.equalsIgnoreCase("0")) { Integer
			 * totalLengthDigiFilev=Integer.parseInt(totalLengthDigiFileRefer); for(int
			 * i=0;i<totalLengthDigiFilev;i++) { fileUploadFinalVal+="0"+","; }
			 * 
			 * } if(referalRmsIdValueArrays!=null) { for(String ss:referalRmsIdValueArrays)
			 * { fileUploadFinalVal+=ss+","; } }
			 * 
			 * String[] referalRmsIdValueArray = fileUploadFinalVal.split(",");
			 * HashMap<String, String> mapInvestigationMap = new HashMap<>();
			 * 
			 * String finalValue = ""; Integer counter = 1; String userId=""; try {
			 * hospitalId = payload.get("hospitalId").toString(); hospitalId =
			 * OpdServiceImpl.getReplaceString(hospitalId);
			 * 
			 * userId = payload.get("userId").toString(); userId
			 * =OpdServiceImpl.getReplaceString(userId);
			 * if(payload.containsKey("remarksForReferal")) {
			 * referalNotes=payload.get("remarksForReferal").toString();
			 * referalNotes=OpdServiceImpl.getReplaceString(referalNotes); } }
			 * catch(Exception e) { e.printStackTrace(); } String[]
			 * specialistOpinionArrays=null; if(payload.containsKey("specialistOpinion")) {
			 * String specialistOpinion = payload.get("specialistOpinion").toString();
			 * specialistOpinion = OpdServiceImpl.getReplaceString(specialistOpinion);
			 * specialistOpinionArrays = specialistOpinion.split("@@@###"); }
			 * 
			 * if(payload.containsKey("referralNote")) { referalNotes =
			 * payload.get("referralNote").toString(); referalNotes =
			 * OpdServiceImpl.getReplaceString(referalNotes); }
			 * 
			 * for (int i = 0; i < referHospitalListValue.length; i++) {
			 * 
			 * if (StringUtils.isNotEmpty(referHospitalListValue[i].toString()) &&
			 * !referHospitalListValue[i].equalsIgnoreCase("0")) { finalValue +=
			 * referHospitalListValue[i].trim(); if
			 * (!referalPatientDtValueArray[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(referalPatientDtValueArray[i])) { for (int m = i; m <
			 * referalPatientDtValueArray.length; m++) { finalValue += "###" +
			 * referalPatientDtValueArray[m].trim(); if (m == i) { break; } } } else {
			 * finalValue += "###" + "0"; }
			 * 
			 * if (!departmentValueArray[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(departmentValueArray[i])) { for (int j = i; j <
			 * departmentValueArray.length; j++) { finalValue += "###" +
			 * departmentValueArray[j].trim(); if (j == i) { break; } } } else { finalValue
			 * += "###" + "0"; } if (i < diagonsisIdValue.length &&
			 * StringUtils.isNotBlank(diagonsisIdValue[i])) { for (int k = i; k <
			 * diagonsisIdValue.length; k++) { finalValue += "###" +
			 * diagonsisIdValue[k].trim(); if (k == i) { break; } } } else { finalValue +=
			 * "###" + "0"; } if (i < hosValue.length &&
			 * StringUtils.isNotBlank(hosValue[i])) { for (int l = i; l < hosValue.length;
			 * l++) { finalValue += "###" + hosValue[l].trim(); if (l == i) { break; } } }
			 * else { finalValue += "###" + "0"; }
			 * 
			 * if (i < referalRmsIdValueArray.length &&
			 * StringUtils.isNotBlank(referalRmsIdValueArray[i])) { for (int l = i; l <
			 * referalRmsIdValueArray.length; l++) { finalValue += "###" +
			 * referalRmsIdValueArray[l].trim(); if (l == i) { break; } } } else {
			 * finalValue += "###" + "0"; }
			 * 
			 * int tempI=0; if (payload.containsKey("specialistOpinion")) { tempI=i; i++; if
			 * (i < specialistOpinionArrays.length &&
			 * StringUtils.isNotBlank(specialistOpinionArrays[i])) { for (int l = i; l <
			 * specialistOpinionArrays.length; l++) {
			 * if(StringUtils.isNotEmpty(specialistOpinionArrays[l].trim())) { finalValue +=
			 * "###" + specialistOpinionArrays[l].trim(); if (l == i) { i=tempI; tempI=0;
			 * break; } } else { i=tempI; tempI=0; finalValue += "###" + "0"; } }
			 * 
			 * } else { i=tempI; tempI=0; finalValue += "###" + "0"; }
			 * 
			 * } else { i=tempI; tempI=0;
			 * 
			 * finalValue += "###" + "0"; }
			 * 
			 * mapInvestigationMap.put(referHospitalListValue[i].trim() + "@#" + counter,
			 * finalValue); finalValue = ""; counter++; } } counter = 1; Date date=new
			 * Date(); for (String referHospitalId : referHospitalListValue) { if
			 * (StringUtils.isNotEmpty(referHospitalId)) { if
			 * (mapInvestigationMap.containsKey(referHospitalId.trim() + "@#" + counter)) {
			 * String referHospitalIdValue = mapInvestigationMap.get(referHospitalId.trim()
			 * + "@#" + counter);
			 * 
			 * if (StringUtils.isNotEmpty(referHospitalIdValue)) {
			 * 
			 * String[] finalValueReferal = referHospitalIdValue.split("###");
			 * ReferralPatientDt referralPatientDt = null; if (finalValueReferal[1] != null
			 * && !finalValueReferal[1].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(finalValueReferal[1])) { referralPatientDt =
			 * getReferralPatientDtByReferaldtIdMe(
			 * Long.parseLong(finalValueReferal[1].toString())); } else { referralPatientDt
			 * = new ReferralPatientDt(); if (referalPatientHdValueArray != null &&
			 * StringUtils.isNotBlank(referalPatientHdValueArray[0]) &&
			 * !referalPatientHdValueArray[0].equalsIgnoreCase("undefined")) {
			 * referralPatientDt
			 * .setRefrealHdId(Long.parseLong(referalPatientHdValueArray[0].toString())); }
			 * 
			 * }
			 * 
			 * if (finalValueReferal != null) { referToValue=
			 * payload.get("referTo").toString(); referToValue=
			 * OpdServiceImpl.getReplaceString(referToValue); if(
			 * StringUtils.isNotEmpty(referToValue) && referToValue.equalsIgnoreCase("I")) {
			 * 
			 * if (StringUtils.isNotEmpty(finalValueReferal[2]) &&
			 * !finalValueReferal[2].equals("0")) {
			 * referralPatientDt.setIntDepartmentId(Long.parseLong(finalValueReferal[2].
			 * toString())); } } else { if (StringUtils.isNotEmpty(finalValueReferal[2]) &&
			 * !finalValueReferal[2].equals("0")) {
			 * referralPatientDt.setExtDepartment(finalValueReferal[2]); } } if
			 * (finalValueReferal[3] != null && StringUtils.isNotBlank(finalValueReferal[3])
			 * && !finalValueReferal[3].equals("0"))
			 * referralPatientDt.setDiagnosisId(Long.parseLong(finalValueReferal[3].toString
			 * ()));
			 * 
			 * if (finalValueReferal[4] != null &&
			 * StringUtils.isNotBlank(finalValueReferal[4]) &&
			 * !finalValueReferal[4].equals("0"))
			 * referralPatientDt.setInstruction(finalValueReferal[4].toString());
			 * 
			 * if (finalValueReferal[0] != null &&
			 * StringUtils.isNotBlank(finalValueReferal[0]) &&
			 * !finalValueReferal[0].equals("0")) {
			 * 
			 * ReferralPatientHd referralPatientHd = getPatientReferalHdByExtHospitalIdMe(
			 * Long.parseLong(finalValueReferal[0].trim()),
			 * Long.parseLong(opdPatientDetailsId.trim()),referToValue);
			 * 
			 * if (referralPatientHd != null) {
			 * 
			 * if(StringUtils.isNotEmpty(referToValue) &&
			 * referToValue.equalsIgnoreCase("I")) { if(finalValueReferal[0]!=null)
			 * referralPatientHd.setIntHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); } else { if(finalValueReferal[0]!=null)
			 * referralPatientHd.setExtHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); } } else { referralPatientHd = new ReferralPatientHd();
			 * 
			 * if(StringUtils.isNotEmpty(referToValue) &&
			 * referToValue.equalsIgnoreCase("I")) { if(finalValueReferal[0]!=null)
			 * referralPatientHd.setIntHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); } else { if(finalValueReferal[0]!=null)
			 * referralPatientHd.setExtHospitalId(Long.parseLong(finalValueReferal[0].trim()
			 * )); }
			 * 
			 * 
			 * if (payload.containsKey("specialistOpinion") && finalValueReferal[6] != null
			 * && StringUtils.isNotBlank(finalValueReferal[6]) &&
			 * !finalValueReferal[6].equals("0")) { String result = ""; int
			 * index=finalValueReferal[6].lastIndexOf(","); if(index==-1) { result =
			 * getHtmlText(finalValueReferal[6].toString());
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } else {
			 * result = finalValueReferal[6].toString().substring(0,
			 * finalValueReferal[6].toString().length() - 1); result = getHtmlText(result);
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } } else
			 * { referralPatientHd.setStatus("W"); }
			 * 
			 * if(StringUtils.isNotBlank(hospitalId)) { Long
			 * hospitalIdValue=Long.parseLong(hospitalId);
			 * referralPatientHd.setHospitalId(hospitalIdValue); }
			 * 
			 * }
			 * 
			 * if(StringUtils.isNotEmpty(referalNotes)) {
			 * referralPatientHd.setReferralNote(referalNotes); }
			 * //if(StringUtils.isNotBlank(referalDates)) { //Date referalDate =
			 * HMSUtil.convertStringDateToUtilDate(new Date(), "yyyy-MM-dd");
			 * referralPatientHd.setReferralIniDate(new Date()); //}
			 * 
			 * if (payload.containsKey("specialistOpinion") && finalValueReferal[6] != null
			 * && StringUtils.isNotBlank(finalValueReferal[6]) &&
			 * !finalValueReferal[6].equals("0")) { String result = ""; int
			 * index=finalValueReferal[6].lastIndexOf(","); if(index==-1) { result =
			 * getHtmlText(finalValueReferal[6].toString());
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } else {
			 * result = finalValueReferal[6].toString().substring(0,
			 * finalValueReferal[6].toString().length() - 1); result = getHtmlText(result);
			 * if(StringUtils.isNotEmpty(result)) referralPatientHd.setStatus("D"); } }
			 * 
			 * if(StringUtils.isNotEmpty(opdPatientDetailsId))
			 * referralPatientHd.setOpdPatientDetailsId(Long.parseLong(opdPatientDetailsId))
			 * ; if(StringUtils.isNotEmpty(patient))
			 * referralPatientHd.setPatientId(Long.parseLong(patient));
			 * referralPatientHd.setTreatmentType("E"); if(StringUtils.isNotEmpty(userId))
			 * referralPatientHd.setLastChgBy(Long.parseLong(userId));
			 * referralPatientHd.setDoctorId(Long.parseLong(userId));
			 * referralPatientHd.setLastChgDate(new Timestamp(date.getTime())); Long
			 * referalPatientHdId = saveOrUpdateReferalHdMe(referralPatientHd);
			 * referralPatientDt.setRefrealHdId(referalPatientHdId); }
			 * 
			 * } referralPatientDt.setLastChgDate(new Timestamp(date.getTime())); if
			 * (finalValueReferal[5] != null && StringUtils.isNotBlank(finalValueReferal[5])
			 * && !finalValueReferal[5].equals("0"))
			 * referralPatientDt.setRidcId(Long.parseLong(finalValueReferal[5].toString()));
			 * 
			 * if (payload.containsKey("specialistOpinion") && finalValueReferal[6] != null
			 * && StringUtils.isNotBlank(finalValueReferal[6]) &&
			 * !finalValueReferal[6].equals("0")) {
			 * 
			 * int index=finalValueReferal[6].lastIndexOf(","); if(index==-1) {
			 * referralPatientDt.setFinalNote(getHtmlText(finalValueReferal[6].toString()));
			 * } else { String result = finalValueReferal[6].toString().substring(0,
			 * finalValueReferal[6].toString().length() - 1);
			 * referralPatientDt.setFinalNote(getHtmlText(result)); }
			 * 
			 * //referralPatientDt.setFinalNote(finalValueReferal[6]); }
			 * 
			 * saveOrUpdateReferalDtMe(referralPatientDt); } } } counter++; }
			 * 
			 * 
			 * 
			 * //} catch (Exception e) { e.printStackTrace(); } }
			 * 
			 * @Transactional public void saveOrUpdateDischargeIcg(HashMap<String, Object>
			 * payload,String visitId,Long opdPatientDetailId,String patientId) {
			 * 
			 * 
			 * String diagnosisId = payload.get("diagnosisiIdMC").toString(); diagnosisId =
			 * OpdServiceImpl.getReplaceString(diagnosisId); String[] diagnosisIdVal =
			 * diagnosisId.split(",");
			 * 
			 * String userId = payload.get("userId").toString(); userId
			 * =OpdServiceImpl.getReplaceString(userId);
			 * 
			 * if(diagnosisIdVal!=null) for(String icdDiagonosisId:diagnosisIdVal) {
			 * if(StringUtils.isNotEmpty(icdDiagonosisId)) { DischargeIcdCode
			 * dischargeIcdCode=getDischargeIcdByVisitIdandIcdId(Long.parseLong(visitId.trim
			 * ()),Long.parseLong(icdDiagonosisId.trim())); if(dischargeIcdCode==null) {
			 * dischargeIcdCode=new DischargeIcdCode(); }
			 * if(StringUtils.isNotEmpty(icdDiagonosisId))
			 * dischargeIcdCode.setIcdId(Long.parseLong(icdDiagonosisId.trim()));
			 * if(StringUtils.isNotEmpty(visitId))
			 * dischargeIcdCode.setVisitId(Long.parseLong(visitId.trim()));
			 * if(StringUtils.isNotEmpty(patientId))
			 * dischargeIcdCode.setPatientId(Long.parseLong(patientId.trim()));
			 * if(StringUtils.isNotEmpty(userId))
			 * dischargeIcdCode.setLastChgBy(Long.parseLong(userId));
			 * dischargeIcdCode.setOpdPatientDetailsId(opdPatientDetailId); Date date=new
			 * Date(); dischargeIcdCode.setLastChgDate(new Timestamp(date.getTime()));
			 * saveOrUpdateDischargeIcd(dischargeIcdCode); } } }
			 * 
			 * 
			 * @Transactional public Long saveOrUpdateDischargeIcd(DischargeIcdCode
			 * dischargeIcdCode) { Session session=null; Long dischargeIcdCodeId=null; try{
			 * session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * session.saveOrUpdate(dischargeIcdCode);
			 * dischargeIcdCodeId=dischargeIcdCode.getDischargeIcdCodeId(); session.flush();
			 * session.clear();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return dischargeIcdCodeId; }
			 * 
			 * @Override public DischargeIcdCode getDischargeIcdByVisitIdandIcdId(Long
			 * visitId,Long IcgId) { DischargeIcdCode dischargeIcdCode=null;
			 * List<DischargeIcdCode>listDischargeIcdCode=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); listDischargeIcdCode=
			 * session.createCriteria(DischargeIcdCode.class).add(Restrictions.eq("visitId",
			 * visitId)) .add(Restrictions.eq("icdId", IcgId)) .list() ;
			 * if(CollectionUtils.isNotEmpty(listDischargeIcdCode)) {
			 * dischargeIcdCode=listDischargeIcdCode.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally { } return
			 * dischargeIcdCode;
			 * 
			 * }
			 * 
			 * 
			 * public DgResultEntryHd getDgResultEntryHdByVisitId(Long visitId) {
			 * DgResultEntryHd dgResultEntryHd=null;
			 * List<DgResultEntryHd>listDgResultEntryHd=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); listDgResultEntryHd=
			 * session.createCriteria(DgResultEntryHd.class).add(Restrictions.eq("visitId",
			 * visitId)) //.add(Restrictions.eq("hospitalId", hospitalId)) .list() ;
			 * if(CollectionUtils.isNotEmpty(listDgResultEntryHd)) {
			 * dgResultEntryHd=listDgResultEntryHd.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
			 * dgResultEntryHd; }
			 * 
			 * 
			 * 
			 * 
			 * @Override public String
			 * saveUpdateAllTransationDigiFileUpload(MasMedicalExamReport
			 * masMedicalExamReprt,
			 * 
			 * HashMap<String, Object> payload,String patientId, String hospitalId,String
			 * userId,String saveInDraft) { Transaction tx = null; String satusOfMessage =
			 * ""; Long medicaleExamId = 0l; String statusOfPatient=""; Timestamp ts =null;
			 * try {
			 * 
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
			 * session.beginTransaction();
			 * 
			 * saveOrUpdateMedicalCategoryDigi(payload);
			 * saveOrUpdateMedicalCatCompositeDigi(payload); Long checkPatientUpdate=0l;
			 * String visitId = payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId); try { String
			 * medicalCompositeNameValue ="";
			 * if(payload.containsKey("medicalCompositeNameValue")) {
			 * medicalCompositeNameValue =
			 * payload.get("medicalCompositeNameValue").toString();
			 * medicalCompositeNameValue =
			 * OpdServiceImpl.getReplaceString(medicalCompositeNameValue); } String
			 * diagnosisiIdMC = ""; if(payload.containsKey("diagnosisiIdMC")) {
			 * diagnosisiIdMC = payload.get("diagnosisiIdMC").toString(); diagnosisiIdMC =
			 * OpdServiceImpl.getReplaceString(diagnosisiIdMC); } String[]
			 * diagnosisiIdMCValues = null; if(StringUtils.isNotEmpty(diagnosisiIdMC)) {
			 * diagnosisiIdMCValues = diagnosisiIdMC.trim().split(","); } String
			 * fitFlagCheckValue=payload.get("fitFlagCheckValue").toString();
			 * fitFlagCheckValue=OpdServiceImpl.getReplaceString(fitFlagCheckValue);
			 * if(StringUtils.isEmpty(medicalCompositeNameValue) &&
			 * diagnosisiIdMCValues==null &&
			 * (fitFlagCheckValue.equalsIgnoreCase("Yes")||fitFlagCheckValue.contains("Yes")
			 * )) { Long masMedicalCategoryId=null; if(StringUtils.isNotEmpty(patientId)) {
			 * Patient patient= checkPatientForPatientId(Long.parseLong(patientId));
			 * if(patient!=null) { Criterion cr1=Restrictions.eq("fitFlag",
			 * "F").ignoreCase();
			 * List<MasMedicalCategory>listMasMedicalCategory=masMedicalCategoryDao.
			 * findByCriteria(cr1); if(CollectionUtils.isNotEmpty(listMasMedicalCategory)) {
			 * masMedicalCategoryId=listMasMedicalCategory.get(0).getMedicalCategoryId();
			 * patient.setMedicalCategoryId(masMedicalCategoryId);
			 * if(listMasMedicalCategory.get(0)!=null)
			 * masMedicalExamReprt.setFinalobservation(listMasMedicalCategory.get(0).
			 * getMedicalCategoryName()); } patient.setFitFlag("F");
			 * 
			 * if(payload.get("gender")!=null || payload.get("maritalStatus")!=null ) {
			 * String gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * 
			 * String maritalStatus=payload.get("maritalStatus").toString();
			 * maritalStatus=OpdServiceImpl.getReplaceString(maritalStatus);
			 * 
			 * if(StringUtils.isNotEmpty(gender) && !gender.equalsIgnoreCase("0") &&
			 * !gender.equalsIgnoreCase("undefined")) {
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); }
			 * if(StringUtils.isNotEmpty(maritalStatus) &&
			 * !maritalStatus.equalsIgnoreCase("0")) {
			 * patient.setMaritalStatusId(Long.parseLong(maritalStatus)); }
			 * 
			 * } checkPatientUpdate++; saveOrUpdatePatient(patient); } }
			 * 
			 * 
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * 
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ;
			 * 
			 * if(CollectionUtils.isEmpty(listPatientMedicalCat)) { patientMedicalCat=new
			 * PatientMedicalCat();
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * //patientMedicalCat.setMedicalCategoryId(masMedicalCategoryId);
			 * patientMedicalCat.setpMedCatId(masMedicalCategoryId);
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId)); String dateOfExam ="";
			 * if(payload.containsKey("dateOfExam")) { dateOfExam =
			 * payload.get("dateOfExam").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam); }
			 * 
			 * if(payload.containsKey("dateOfRelease")) { dateOfExam =
			 * payload.get("dateOfRelease").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam); }
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(dateOfExam) && dateOfExam!=null &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date
			 * medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(medicalCompositeDateValue!=null) { ts = new
			 * Timestamp(medicalCompositeDateValue.getTime());
			 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
			 * patientMedicalCat.setpMedFitFlag("F"); if(patientMedicalCat!=null)
			 * saveOrUpdatePatientMedicalCat(patientMedicalCat); }
			 * 
			 * 
			 * } else { if(StringUtils.isNotEmpty(patientId)&&
			 * StringUtils.isEmpty(medicalCompositeNameValue)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId)); patient.setFitFlag("U");
			 * patient.setMedicalCategoryId(null);
			 * 
			 * 
			 * if(payload.get("gender")!=null || payload.get("maritalStatus")!=null ) {
			 * String gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * 
			 * String maritalStatus=payload.get("maritalStatus").toString();
			 * maritalStatus=OpdServiceImpl.getReplaceString(maritalStatus);
			 * 
			 * if(StringUtils.isNotEmpty(gender) && !gender.equalsIgnoreCase("0")
			 * &&!gender.equalsIgnoreCase("undefined")) {
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); }
			 * if(StringUtils.isNotEmpty(maritalStatus) &&
			 * !maritalStatus.equalsIgnoreCase("0")) {
			 * patient.setMaritalStatusId(Long.parseLong(maritalStatus)); }
			 * 
			 * }
			 * 
			 * checkPatientUpdate++; saveOrUpdatePatient(patient);
			 * 
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
			 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
			 * deletePatientMedCat(patientMedicalCat); } } } } catch(Exception e) {
			 * e.printStackTrace(); }
			 * 
			 * 
			 * saveUpdatePatientServicesDetail( payload, patientId, userId, session);
			 * 
			 * saveUpdatePatientDiseaseInfo(payload, patientId, userId, session);
			 * 
			 * saveUpdatePatientDiseaseForBeforeJoined( payload, patientId, userId,
			 * session);
			 * 
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
			 * if(payload.get("unitOrSlip")!=null) { String
			 * unitOrSlip=payload.get("unitOrSlip").toString();
			 * unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
			 * if(StringUtils.isNotEmpty(unitOrSlip) &&
			 * !unitOrSlip.equalsIgnoreCase("undefined") &&
			 * unitOrSlip.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setUnitId(Long.parseLong(unitOrSlip)); } }
			 * if(payload.get("rank")!=null) { String rank=payload.get("rank").toString();
			 * rank=OpdServiceImpl.getReplaceString(rank); if(StringUtils.isNotEmpty(rank)
			 * && !rank.equalsIgnoreCase("undefined") && !rank.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRankId(Long.parseLong(rank)); } }
			 * if(payload.get("branchOrTrade")!=null) { String
			 * branchOrTrade=payload.get("branchOrTrade").toString();
			 * branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
			 * if(StringUtils.isNotEmpty(branchOrTrade) &&
			 * !branchOrTrade.equalsIgnoreCase("undefined") &&
			 * !branchOrTrade.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setBranchId(Long.parseLong(branchOrTrade)); } } String
			 * totalLengthDigiFile=payload.get("totalLengthDigiFile").toString();
			 * totalLengthDigiFile=OpdServiceImpl.getReplaceString(totalLengthDigiFile);
			 * 
			 * 
			 * 
			 * String rmdIdVal=patientRegistrationServiceImpl.getFinalInvestigationValue(
			 * investigationIdValues, dynamicUploadInvesIdAndfiles,totalLengthDigiFile) ;
			 * 
			 * 
			 * String[] rangeValues=null; if(payload.containsKey("range")) { String
			 * rangeValue=payload.get("range").toString();
			 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
			 * rangeValue.split(","); }
			 * 
			 * String[] rmsIdFinalValfileUp=null; rmsIdFinalValfileUp = rmdIdVal.split(",");
			 * 
			 * 
			 * String meRmsId=payload.get("meRmsId").toString();
			 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
			 * meRmsId.split(",");
			 * 
			 * 
			 * masMedicalExamReprt=updateInvestigationDgResultForDigiUpload(payload,
			 * patientId, hospitalId, userId,
			 * masMedicalExamReprt,rangeValues,rmsIdFinalValfileUp,ts);
			 * 
			 * 
			 * masMedicalExamReprt=saveUpdateForReferalforMeDigiFileUpload(
			 * masMedicalExamReprt,payload,hospitalId);
			 * 
			 * if(meRmsIds!=null && meRmsIds.length>0 && meRmsIds[0]!=null &&
			 * !meRmsIds[0].equalsIgnoreCase("")) {
			 * masMedicalExamReprt.setRidcId(Long.parseLong(meRmsIds[0].toString())); }
			 * 
			 * //Save patient Patient patient=null; if((payload.get("gender")!=null ||
			 * payload.get("maritalStatus")!=null) && checkPatientUpdate==0) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * 
			 * String maritalStatus=payload.get("maritalStatus").toString();
			 * maritalStatus=OpdServiceImpl.getReplaceString(maritalStatus);
			 * 
			 * if(StringUtils.isNotEmpty(patientId)) {
			 * patient=getPatient(Long.parseLong(patientId)); }
			 * if(StringUtils.isNotEmpty(gender)) {
			 * patient.setAdministrativeSexId(Long.parseLong(gender));
			 * //saveOrUpdatePatient(patient); } if(StringUtils.isNotEmpty(maritalStatus)) {
			 * patient.setMaritalStatusId(Long.parseLong(maritalStatus)); }
			 * if(patient!=null) patientDao.saveOrUpdate(patient); }
			 * 
			 * 
			 * 
			 * //Save PatientDocumentDetail saveUpdateOfPatientDocumentDetail(payload);
			 * 
			 * 
			 * session.saveOrUpdate(masMedicalExamReprt); if(masMedicalExamReprt!=null &&
			 * masMedicalExamReprt.getMedicalExaminationId()!=null) medicaleExamId
			 * =masMedicalExamReprt.getMedicalExaminationId();
			 * if(StringUtils.isNotEmpty(masMedicalExamReprt.getStatus())) {
			 * statusOfPatient=masMedicalExamReprt.getStatus(); } else {
			 * statusOfPatient="no status"; } session.flush(); session.clear(); tx.commit();
			 * satusOfMessage = medicaleExamId + "##" +
			 * "Medical Exam Submitted Successfully"+"##"+statusOfPatient; } catch
			 * (Exception e) { if (tx != null) { try { tx.rollback();
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; } catch (Exception re) { satusOfMessage = 0
			 * + "##" + "Data is not updated because something is wrong" +
			 * re.toString()+"##"+statusOfPatient; re.printStackTrace(); } } satusOfMessage
			 * = 0 + "##" + "Data is not updated because something is wrong" +
			 * e.toString()+"##"+statusOfPatient; e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * }
			 * 
			 * return satusOfMessage; }
			 * 
			 * @Override public String getRankOfUserId(Users users) { List<MasEmployee>
			 * listMasEmployee=null; String rankOfEmployee=""; if(users!=null &&
			 * StringUtils.isNotEmpty(users.getServiceNo())) {
			 * listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(users.getServiceNo());
			 * } if(CollectionUtils.isNotEmpty(listMasEmployee)) {
			 * if(listMasEmployee.get(0).getMasRank()!=null ) { MasRank
			 * masRank=systemAdminDao.getRankByRankCode((listMasEmployee.get(0).getMasRank()
			 * )); if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
			 * rankOfEmployee=masRank.getRankName(); } } return rankOfEmployee; }
			 * 
			 * 
			 * 
			 * @Override public MasMainChargecode getMainChargeCodeByMainChargeCodeId(Long
			 * mainChargeCodeId) { Session session =null; MasMainChargecode
			 * mainChargeId=null; try {
			 * session=getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
			 * session.createCriteria(MasMainChargecode.class)
			 * .add(Restrictions.eq("mainChargecodeId", mainChargeCodeId)); ProjectionList
			 * projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("mainChargecodeCode").as(
			 * "mainChargecodeCode"));
			 * projectionList.add(Projections.property("mainChargecodeId").as(
			 * "mainChargecodeId")); cr.setProjection(projectionList);
			 * List<MasMainChargecode>listInvHd=cr.setResultTransformer(new
			 * AliasToBeanResultTransformer(MasMainChargecode.class)).list();
			 * if(CollectionUtils.isNotEmpty(listInvHd)) { mainChargeId=listInvHd.get(0); }
			 * } catch(Exception e) { e.printStackTrace(); } return mainChargeId; }
			 * 
			 * 
			 * @Override public String saveUpdateOpdObsistyMe(HashMap<String, Object>
			 * payload) { String Result=null; //Transaction t = null; // try { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
			 * session.createCriteria(OpdObesityHd.class); //t = session.beginTransaction();
			 * Long visitId =null; String visitIdV =""; if(payload.containsKey("VisitID")) {
			 * visitIdV = payload.get("VisitID").toString(); visitIdV =
			 * OpdServiceImpl.getReplaceString(visitIdV); }
			 * if(payload.containsKey("visitId")) { visitIdV =
			 * payload.get("visitId").toString(); visitIdV =
			 * OpdServiceImpl.getReplaceString(visitIdV); }
			 * 
			 * visitId = Long.parseLong(visitIdV); Calendar calendar =
			 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
			 * java.sql.Timestamp(calendar.getTime().getTime()); Date
			 * currentDate=ProjectUtils.getCurrentDate(); cr.add(Restrictions.eq("visitId",
			 * visitId)); OpdObesityHd list = (OpdObesityHd) cr.uniqueResult(); String
			 * hospitalIdV=""; String userIdV=""; String patientIdV=""; if (list != null) {
			 * patientIdV=payload.get("patientId").toString(); patientIdV =
			 * OpdServiceImpl.getReplaceString(patientIdV);
			 * list.setPatientId(Long.parseLong(patientIdV)); String
			 * obsistyMark=payload.get("obsistyMark").toString(); obsistyMark =
			 * OpdServiceImpl.getReplaceString(obsistyMark);
			 * list.setOverweightFlag(obsistyMark);
			 * 
			 * if (payload.containsKey("varation") && payload.get("varation") != null &&
			 * !payload.get("varation").equals("")) { String
			 * varationV=payload.get("varation").toString(); varationV =
			 * OpdServiceImpl.getReplaceString(varationV);
			 * if(StringUtils.isNotEmpty(varationV)) { BigDecimal bd = new
			 * BigDecimal(String.valueOf(varationV)); list.setVaration(bd); } }
			 * hospitalIdV=payload.get("hospitalId").toString(); hospitalIdV
			 * =OpdServiceImpl. getReplaceString(hospitalIdV);
			 * list.setHospitalId(Long.parseLong(hospitalIdV));
			 * 
			 * userIdV=payload.get("userId").toString(); userIdV =
			 * OpdServiceImpl.getReplaceString(userIdV);
			 * list.setLastChgBy(Long.parseLong(userIdV));
			 * list.setLastChgDate(ourJavaTimestampObject); list.setIniDate(currentDate);
			 * 
			 * //opdObesityHdDao.saveOrUpdate(list); session.saveOrUpdate(list);
			 * //t.commit(); Result = "200";
			 * 
			 * } else { OpdObesityHd oohd=new OpdObesityHd();
			 * 
			 * LocalDate currentDate1 = LocalDate.now(); // 2016-06-17 DayOfWeek dow =
			 * currentDate1.getDayOfWeek(); // FRIDAY int dom =
			 * currentDate1.getDayOfMonth(); // 17 int doy = currentDate1.getDayOfYear(); //
			 * 169 String m = currentDate1.getMonth()+""; // JUNE
			 * System.out.println("months"+m);
			 * 
			 * 
			 * oohd.setVisitId(visitId); patientIdV=payload.get("patientId").toString();
			 * patientIdV = OpdServiceImpl.getReplaceString(patientIdV);
			 * oohd.setPatientId(Long.parseLong(patientIdV));
			 * 
			 * hospitalIdV=payload.get("hospitalId").toString(); hospitalIdV =
			 * OpdServiceImpl.getReplaceString(hospitalIdV);
			 * oohd.setHospitalId(Long.parseLong(hospitalIdV));
			 * 
			 * userIdV=payload.get("userId").toString(); userIdV =
			 * OpdServiceImpl.getReplaceString(userIdV);
			 * oohd.setLastChgBy(Long.parseLong(userIdV));
			 * 
			 * oohd.setDoctorId( Long.parseLong(userIdV));
			 * 
			 * String obsistyMark=payload.get("obsistyMark").toString(); obsistyMark =
			 * OpdServiceImpl.getReplaceString(obsistyMark);
			 * oohd.setOverweightFlag(obsistyMark);
			 * 
			 * oohd.setLastChgDate(ourJavaTimestampObject);
			 * 
			 * if (payload.containsKey("varation") &&payload.get("varation") != null &&
			 * !payload.get("varation").equals("")) {
			 * 
			 * String varationV=payload.get("varation").toString(); varationV =
			 * OpdServiceImpl.getReplaceString(varationV);
			 * if(StringUtils.isNotEmpty(varationV)) { BigDecimal bd = new
			 * BigDecimal(varationV); oohd.setVaration(bd); } }
			 * 
			 * if (payload.containsKey("overWeight") && payload.get("overWeight") != null &&
			 * !payload.get("overWeight").equals("")) { String
			 * varationV=payload.get("overWeight").toString(); varationV =
			 * OpdServiceImpl.getReplaceString(varationV);
			 * if(StringUtils.isNotEmpty(varationV)) { BigDecimal bd = new
			 * BigDecimal(String.valueOf(varationV)); oohd.setVaration(bd); } }
			 * 
			 * 
			 * oohd.setIniDate(currentDate); oohd.setLastChgDate(ourJavaTimestampObject);
			 * 
			 * //opdObesityHdDao.saveOrUpdate(oohd); //long obesistyId =
			 * oohd.getObesityHdId(); long obesistyId =
			 * Long.parseLong(session.save(oohd).toString());
			 * System.out.println(obesistyId); //t.commit(); //t=session.beginTransaction();
			 * OpdObesityDt oodt=new OpdObesityDt(); if (payload.containsKey("bmi") &&
			 * payload.get("bmi") != null && !payload.get("bmi").equals("")) {
			 * 
			 * String bmiv=payload.get("bmi").toString(); bmiv =
			 * OpdServiceImpl.getReplaceString(bmiv); if(StringUtils.isNotEmpty(bmiv)) {
			 * BigDecimal bmi = new BigDecimal(bmiv); oodt.setBmi(bmi); } }
			 * oodt.setObesityDate(currentDate); if (payload.get("height") != null &&
			 * !payload.get("height").equals("")) {
			 * 
			 * String heightv=payload.get("height").toString(); heightv =
			 * OpdServiceImpl.getReplaceString(heightv); if(StringUtils.isNotEmpty(heightv))
			 * { BigDecimal height = new BigDecimal(heightv); oodt.setHeight(height); } } if
			 * (payload.containsKey("weight") && payload.get("weight") != null &&
			 * !payload.get("weight").equals("")) {
			 * 
			 * String weightv=payload.get("weight").toString(); weightv =
			 * OpdServiceImpl.getReplaceString(weightv); if(StringUtils.isNotEmpty(weightv))
			 * { BigDecimal weight = new BigDecimal(weightv); oodt.setWeight(weight); } }
			 * 
			 * if (payload.get("idealWeight") != null &&
			 * !payload.get("idealWeight").equals("")) { String
			 * idealWeightv=payload.get("idealWeight").toString(); idealWeightv =
			 * OpdServiceImpl.getReplaceString(idealWeightv);
			 * if(StringUtils.isNotEmpty(idealWeightv)) { BigDecimal idealWeight = new
			 * BigDecimal(idealWeightv); oodt.setIdealWeight(idealWeight); } } if
			 * (payload.containsKey("overWeight") && payload.get("overWeight") != null &&
			 * !payload.get("overWeight").equals("")) { String
			 * varationV=payload.get("overWeight").toString(); varationV =
			 * OpdServiceImpl.getReplaceString(varationV);
			 * if(StringUtils.isNotEmpty(varationV)) { BigDecimal bd = new
			 * BigDecimal(String.valueOf(varationV)); oodt.setVariation(bd); } }
			 * oodt.setMonth(m); oodt.setObesityHdId(obesistyId); session.save(oodt); //
			 * opdObesityDtDao.saveOrUpdate(oodt); //t.commit(); Result = "200"; } } catch
			 * (Exception e) { e.printStackTrace(); } finally { //
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return Result; }
			 * 
			 * @Transactional public Patient getPatient(Long patientId) { Patient
			 * patient=null; Criterion cr1= Restrictions.eq("patientId", patientId);
			 * List<Patient>listPatient=patientDao.findByCriteria(cr1);
			 * if(CollectionUtils.isNotEmpty(listPatient)) { patient=listPatient.get(0); }
			 * return patient; }
			 * 
			 * @Transactional
			 * 
			 * @Override public Long saveUpdateOfPatientDocumentDetail(HashMap<String,
			 * Object> payload) { Long patientDocumentDetailId=null; String[] docIdValue
			 * =null; if(payload.containsKey("docId")) { String
			 * docId=payload.get("docId").toString();
			 * docId=OpdServiceImpl.getReplaceString(docId); docIdValue = docId.split(",");
			 * } String[] medicalDocsArray =null; if(payload.containsKey("medicalDocs")) {
			 * String medicalDocs=payload.get("medicalDocs").toString();
			 * medicalDocs=OpdServiceImpl.getReplaceString(medicalDocs); medicalDocsArray =
			 * medicalDocs.split("@@@###"); } String[] patientDocumentIdArrays = null;
			 * if(payload.containsKey("patientDocumentId")) { String patientDocumentId =
			 * payload.get("patientDocumentId").toString(); patientDocumentId =
			 * OpdServiceImpl.getReplaceString(patientDocumentId); patientDocumentIdArrays =
			 * patientDocumentId.split(","); } String[] medicalBoardDocsUploadValueArrays
			 * =null; if(payload.containsKey("medicalBoardDocsUpload")) { String
			 * medicalBoardDocsUpload = payload.get("medicalBoardDocsUpload").toString();
			 * medicalBoardDocsUpload =
			 * OpdServiceImpl.getReplaceString(medicalBoardDocsUpload);
			 * medicalBoardDocsUploadValueArrays = medicalBoardDocsUpload.split(","); }
			 * String totalLengthDigiFileSupportDoc ="";
			 * if(payload.containsKey("totalLengthDigiFileSupportDoc")) {
			 * totalLengthDigiFileSupportDoc =
			 * payload.get("totalLengthDigiFileSupportDoc").toString();
			 * totalLengthDigiFileSupportDoc =
			 * OpdServiceImpl.getReplaceString(totalLengthDigiFileSupportDoc); } String
			 * fileUploadFinalVal="";
			 * 
			 * if(medicalBoardDocsUploadValueArrays!=null) { for(String
			 * ss:medicalBoardDocsUploadValueArrays) { fileUploadFinalVal+=ss+","; } }
			 * if(StringUtils.isNotEmpty(totalLengthDigiFileSupportDoc)&&
			 * !totalLengthDigiFileSupportDoc.equalsIgnoreCase("0")) { Integer
			 * totalLengthDigiFilev=Integer.parseInt(totalLengthDigiFileSupportDoc); for(int
			 * i=0;i<totalLengthDigiFilev;i++) { fileUploadFinalVal+="0"+","; }
			 * 
			 * } String[] supportDocRmsIdValueArray = fileUploadFinalVal.split(",");
			 * 
			 * String visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * String patientId=payload.get("patientId").toString();
			 * patientId=OpdServiceImpl.getReplaceString(patientId); int count =0; int
			 * countMed=1; Character charVal=null; PatientDocumentDetail
			 * patientDocumentDetail=null; if(docIdValue!=null) for(String
			 * docIdObj:docIdValue) { if(StringUtils.isNotEmpty(docIdObj)) { String[]
			 * docIdObjs=docIdObj.split("@"); List<PatientDocumentDetail>
			 * listPatientDocumentDetail=null;
			 * 
			 * if(patientDocumentIdArrays!=null &&
			 * !patientDocumentIdArrays[count].trim().equalsIgnoreCase("")) { Criterion
			 * cr2=Restrictions.eq("patientDocumentId",
			 * Long.parseLong(patientDocumentIdArrays[count].trim()));
			 * listPatientDocumentDetail= patientDocumentDetailDao.findByCriteria(cr2); }
			 * if(CollectionUtils.isNotEmpty(listPatientDocumentDetail)) {
			 * patientDocumentDetail=listPatientDocumentDetail.get(0); } else {
			 * patientDocumentDetail=new PatientDocumentDetail(); }
			 * 
			 * if(docIdObjs[0]!=null && !docIdObjs[0].trim().equalsIgnoreCase(""))
			 * patientDocumentDetail.setDocumentId(Long.parseLong(docIdObjs[0].trim())); if(
			 * medicalDocsArray!=null && countMed < medicalDocsArray.length &&
			 * !medicalDocsArray[countMed].equalsIgnoreCase("")) {
			 * 
			 * String result=""; charVal =
			 * medicalDocsArray[countMed].charAt(medicalDocsArray[countMed].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { result =
			 * medicalDocsArray[countMed].toString().substring(0,
			 * medicalDocsArray[countMed].toString().length() - 1);
			 * patientDocumentDetail.setDocumentRemarks(getHtmlText(result)); } else {
			 * patientDocumentDetail.setDocumentRemarks(getHtmlText(medicalDocsArray[
			 * countMed].toString())); }
			 * 
			 * 
			 * int index=medicalDocsArray[countMed].lastIndexOf(","); String resultView="";
			 * if(index==-1) { resultView=medicalDocsArray[countMed].toString();
			 * resultView=getHtmlText(resultView);
			 * patientDocumentDetail.setDocumentRemarks(resultView); } else { if(
			 * medicalDocsArray!=null && countMed < medicalDocsArray.length &&
			 * !medicalDocsArray[countMed].equalsIgnoreCase("")) { String result =
			 * medicalDocsArray[countMed].toString().substring(0,
			 * medicalDocsArray[countMed].toString().length() - 1);
			 * result=getHtmlText(result); patientDocumentDetail.setDocumentRemarks(result);
			 * } } } if(StringUtils.isNotEmpty(visitId))
			 * patientDocumentDetail.setVisitId(Long.parseLong(visitId));
			 * if(supportDocRmsIdValueArray!=null && count<supportDocRmsIdValueArray.length
			 * && supportDocRmsIdValueArray.length>0) { if(supportDocRmsIdValueArray!=null
			 * && !supportDocRmsIdValueArray[count].trim().equalsIgnoreCase("0") &&
			 * !supportDocRmsIdValueArray[count].trim().equalsIgnoreCase(""))
			 * patientDocumentDetail.setRidcId(Long.parseLong(supportDocRmsIdValueArray[
			 * count].trim())); } if(StringUtils.isNotEmpty(patientId))
			 * patientDocumentDetail.setPatientId(Long.parseLong(patientId)); Date date =
			 * new Date(); try { patientDocumentDetail.setLastChgDate(new
			 * Timestamp(date.getTime())); } catch(Exception e) {e.printStackTrace();}
			 * patientDocumentDetailDao.saveOrUpdate(patientDocumentDetail); count++;
			 * countMed++; } } return patientDocumentDetailId; }
			 * 
			 * 
			 * @Transactional
			 * 
			 * @Override public Long saveUpdateOfPatientDocumentDetailDoc(HashMap<String,
			 * Object> payload) { Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
			 * session.beginTransaction();
			 * 
			 * Long patientDocumentDetailId=null; String[] docIdValue =null;
			 * if(payload.containsKey("docId")) { String
			 * docId=payload.get("docId").toString();
			 * docId=OpdServiceImpl.getReplaceString(docId); docIdValue = docId.split(",");
			 * } String[] medicalDocsArray =null; if(payload.containsKey("medicalDocs")) {
			 * String medicalDocs=payload.get("medicalDocs").toString();
			 * medicalDocs=OpdServiceImpl.getReplaceString(medicalDocs); medicalDocsArray =
			 * medicalDocs.split("@@@###"); } String[] patientDocumentIdArrays = null;
			 * if(payload.containsKey("patientDocumentId")) { String patientDocumentId =
			 * payload.get("patientDocumentId").toString(); patientDocumentId =
			 * OpdServiceImpl.getReplaceString(patientDocumentId); patientDocumentIdArrays =
			 * patientDocumentId.split(","); } String[] medicalBoardDocsUploadValueArrays
			 * =null; if(payload.containsKey("medicalBoardDocsUpload")) { String
			 * medicalBoardDocsUpload = payload.get("medicalBoardDocsUpload").toString();
			 * medicalBoardDocsUpload =
			 * OpdServiceImpl.getReplaceString(medicalBoardDocsUpload);
			 * medicalBoardDocsUploadValueArrays = medicalBoardDocsUpload.split(","); }
			 * String totalLengthDigiFileSupportDoc ="";
			 * if(payload.containsKey("totalLengthDigiFileSupportDoc")) {
			 * totalLengthDigiFileSupportDoc =
			 * payload.get("totalLengthDigiFileSupportDoc").toString();
			 * totalLengthDigiFileSupportDoc =
			 * OpdServiceImpl.getReplaceString(totalLengthDigiFileSupportDoc); } String
			 * fileUploadFinalVal="";
			 * if(StringUtils.isNotEmpty(totalLengthDigiFileSupportDoc)&&
			 * !totalLengthDigiFileSupportDoc.equalsIgnoreCase("0")) { Integer
			 * totalLengthDigiFilev=Integer.parseInt(totalLengthDigiFileSupportDoc); for(int
			 * i=0;i<totalLengthDigiFilev;i++) { fileUploadFinalVal+="0"+","; }
			 * 
			 * } if(medicalBoardDocsUploadValueArrays!=null) { for(String
			 * ss:medicalBoardDocsUploadValueArrays) { fileUploadFinalVal+=ss+","; } }
			 * 
			 * String[] supportDocRmsIdValueArray = fileUploadFinalVal.split(",");
			 * 
			 * String visitId=null; if(payload.get("visitId")!=null) {
			 * visitId=payload.get("visitId").toString();
			 * visitId=OpdServiceImpl.getReplaceString(visitId); } String
			 * patientId=payload.get("patientId").toString();
			 * patientId=OpdServiceImpl.getReplaceString(patientId);
			 * 
			 * 
			 * String[] docDateOfExamValueArrays =null;
			 * if(payload.containsKey("docDateOfExam")) { String docDateOfExam =
			 * payload.get("docDateOfExam").toString(); docDateOfExam =
			 * OpdServiceImpl.getReplaceString(docDateOfExam); docDateOfExamValueArrays =
			 * docDateOfExam.split(","); }
			 * 
			 * 
			 * if (StringUtils.isNotEmpty(docDateOfExam) && docDateOfExam != null &&
			 * MedicalExamServiceImpl.checkDateFormat(docDateOfExam)) { Date
			 * docDateOfExamValue = null; docDateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(docDateOfExam.trim());
			 * if(docDateOfExamValue!=null) }
			 * 
			 * int count =0; int countMed=1; Character charVal=null; PatientDocumentDetail
			 * patientDocumentDetail=null;
			 * 
			 * if(docIdValue!=null) for(String docIdObj:docIdValue) {
			 * if(StringUtils.isNotEmpty(docIdObj)) { String[]
			 * docIdObjs=docIdObj.split("@"); List<PatientDocumentDetail>
			 * listPatientDocumentDetail=null;
			 * 
			 * if(patientDocumentIdArrays!=null &&
			 * !patientDocumentIdArrays[count].trim().equalsIgnoreCase("")) { Criterion
			 * cr2=Restrictions.eq("patientDocumentId",
			 * Long.parseLong(patientDocumentIdArrays[count].trim()));
			 * listPatientDocumentDetail= patientDocumentDetailDao.findByCriteria(cr2); }
			 * if(CollectionUtils.isNotEmpty(listPatientDocumentDetail)) {
			 * patientDocumentDetail=listPatientDocumentDetail.get(0); } else {
			 * patientDocumentDetail=new PatientDocumentDetail(); }
			 * 
			 * if(docIdObjs[0]!=null && !docIdObjs[0].trim().equalsIgnoreCase(""))
			 * patientDocumentDetail.setDocumentId(Long.parseLong(docIdObjs[0].trim())); if(
			 * medicalDocsArray!=null && countMed < medicalDocsArray.length &&
			 * !medicalDocsArray[countMed].equalsIgnoreCase("")) {
			 * 
			 * String result=""; charVal =
			 * medicalDocsArray[countMed].charAt(medicalDocsArray[countMed].length() - 1);
			 * if(charVal!=null && charVal.equals(',')) { result =
			 * medicalDocsArray[countMed].toString().substring(0,
			 * medicalDocsArray[countMed].toString().length() - 1);
			 * patientDocumentDetail.setDocumentRemarks(getHtmlText(result)); } else {
			 * patientDocumentDetail.setDocumentRemarks(getHtmlText(medicalDocsArray[
			 * countMed].toString())); }
			 * 
			 * 
			 * int index=medicalDocsArray[countMed].lastIndexOf(","); String resultView="";
			 * if(index==-1) { resultView=medicalDocsArray[countMed].toString();
			 * resultView=getHtmlText(resultView);
			 * patientDocumentDetail.setDocumentRemarks(resultView); } else { if(
			 * medicalDocsArray!=null && countMed < medicalDocsArray.length &&
			 * !medicalDocsArray[countMed].equalsIgnoreCase("")) { String result =
			 * medicalDocsArray[countMed].toString().substring(0,
			 * medicalDocsArray[countMed].toString().length() - 1);
			 * result=getHtmlText(result); patientDocumentDetail.setDocumentRemarks(result);
			 * } } } if(StringUtils.isNotEmpty(visitId))
			 * patientDocumentDetail.setVisitId(Long.parseLong(visitId));
			 * if(supportDocRmsIdValueArray!=null && count<supportDocRmsIdValueArray.length
			 * && supportDocRmsIdValueArray.length>0) { if(supportDocRmsIdValueArray!=null
			 * && !supportDocRmsIdValueArray[count].equalsIgnoreCase("0"))
			 * patientDocumentDetail.setRidcId(Long.parseLong(supportDocRmsIdValueArray[
			 * count].trim())); }
			 * 
			 * if(docDateOfExamValueArrays!=null && count<docDateOfExamValueArrays.length &&
			 * !docDateOfExamValueArrays[count].trim().equalsIgnoreCase("")) { Date
			 * docDateOfExamValue = null; if(docDateOfExamValueArrays[count]!=null &&
			 * !docDateOfExamValueArrays[count].trim().equalsIgnoreCase("") &&
			 * !docDateOfExamValueArrays[count].trim().equalsIgnoreCase("undefined"))
			 * docDateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(docDateOfExamValueArrays[count].trim(
			 * ).toString()); if(docDateOfExamValue!=null)
			 * patientDocumentDetail.setDocumentDate(docDateOfExamValue); }
			 * 
			 * if(StringUtils.isNotEmpty(patientId))
			 * patientDocumentDetail.setPatientId(Long.parseLong(patientId)); Date date =
			 * new Date(); try { patientDocumentDetail.setLastChgDate(new
			 * Timestamp(date.getTime())); } catch(Exception e) {e.printStackTrace();}
			 * patientDocumentDetailDao.saveOrUpdate(patientDocumentDetail);
			 * patientDocumentDetailId=patientDocumentDetail.getPatientDocumentId();
			 * count++; countMed++; } } session.flush(); session.clear(); tx.commit();
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return
			 * patientDocumentDetailId; }
			 * 
			 * public static String getHtmlText(String resultView) { return
			 * resultView.replace("/&lt;/g","<").replace("/&gt;/g",">").replace("/&quot;/g",
			 * "'").replace("/&amp;/g","&"); }
			 * 
			 * @Transactional public Long
			 * saveOrUpdatepatientDocumentDetail(PatientDocumentDetail
			 * patientDocumentDetail) { Session session=null; Long
			 * patientDocumentDetailId=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * session.saveOrUpdate(patientDocumentDetail);
			 * patientDocumentDetailId=patientDocumentDetail.getPatientDocumentId();
			 * session.flush(); session.clear();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return patientDocumentDetailId; }
			 * 
			 * @Override
			 * 
			 * @Transactional public PatientImmunizationHistory
			 * getPatientImmunizationHistoryByDate(Long visitId,Long itemId,Long
			 * patientId,Date immunizationDate,String flag) { PatientImmunizationHistory
			 * patientImmunizationHistory=null;
			 * List<PatientImmunizationHistory>listPatientImmunizationHistory=null; try {
			 * Criterion cro1=null; Criterion cro2=null; Session session =
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr1=null;
			 * if(patientId!=null && itemId!=null) { cr1=
			 * session.createCriteria(PatientImmunizationHistory.class)
			 * .add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("itemId",
			 * itemId));
			 * 
			 * if(StringUtils.isNotEmpty(flag) && flag.equalsIgnoreCase("dateNotNull")) {
			 * cro1=Restrictions.eq("immunizationDate", immunizationDate) ; cr1.add(cro1); }
			 * if(StringUtils.isNotEmpty(flag) && flag.equalsIgnoreCase("dateNull")) {
			 * cro1=Restrictions.isNull("immunizationDate") ; cr1.add(cro1);
			 * cro2=Restrictions.isNotNull("prescriptionDate") ; cr1.add(cro2); }
			 * listPatientImmunizationHistory=cr1.list();
			 * 
			 * if(CollectionUtils.isNotEmpty(listPatientImmunizationHistory)) {
			 * patientImmunizationHistory=listPatientImmunizationHistory.get(0); } } }
			 * catch(Exception e) { e.printStackTrace(); } finally { } return
			 * patientImmunizationHistory; }
			 * 
			 * @Override public PatientImmunizationHistory
			 * getPatientImmunizationHistoryByDateDigi(Long visitId,Long itemId,Long
			 * patientId,Date immunizationDate,String flag) { PatientImmunizationHistory
			 * patientImmunizationHistory=null;
			 * List<PatientImmunizationHistory>listPatientImmunizationHistory=null; try {
			 * Criterion cro1=null; Criterion cro2=null; Criterion cro3=null; Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr1=
			 * session.createCriteria(PatientImmunizationHistory.class)
			 * .add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("itemId",
			 * itemId));
			 * 
			 * if(StringUtils.isNotEmpty(flag) && flag.equalsIgnoreCase("dateNotNull")) {
			 * cro1=Restrictions.eq("immunizationDate", immunizationDate) ;
			 * cro3=Restrictions.eq("visitId", visitId) ; cr1.add(cro1); cr1.add(cro3); }
			 * if(StringUtils.isNotEmpty(flag) && flag.equalsIgnoreCase("dateNull")) {
			 * cro1=Restrictions.isNull("immunizationDate") ; cr1.add(cro1);
			 * cro2=Restrictions.isNotNull("prescriptionDate") ;
			 * cro3=Restrictions.eq("visitId", visitId) ; cr1.add(cro2); cr1.add(cro3); }
			 * listPatientImmunizationHistory=cr1.list();
			 * 
			 * if(CollectionUtils.isNotEmpty(listPatientImmunizationHistory)) {
			 * patientImmunizationHistory=listPatientImmunizationHistory.get(0); } }
			 * catch(Exception e) { e.printStackTrace(); } finally { } return
			 * patientImmunizationHistory; }
			 * 
			 * @Override public DgResultEntryHd
			 * getDgResultEntryHdByPatientIdAndHospitalIdAndInves(Long patientId,Long
			 * hospitalId,Long investigationId) { DgResultEntryHd dgResultEntryHd=null;
			 * List<DgResultEntryHd>listDgResultEntryHd=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); listDgResultEntryHd=
			 * session.createCriteria(DgResultEntryHd.class).add(Restrictions.eq(
			 * "patientId", patientId)) .add(Restrictions.eq("hospitalId",
			 * hospitalId)).add(Restrictions.eq("investigationId", investigationId)) .list()
			 * ; if(CollectionUtils.isNotEmpty(listDgResultEntryHd)) {
			 * dgResultEntryHd=listDgResultEntryHd.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return dgResultEntryHd; }
			 * 
			 * 
			 * 
			 * 
			 * @Override public List<Object[]>
			 * getDgMasInvestigationsAndResultForSubInvestigation(Long visitId) {
			 * //Transaction transation=null; List<Object[]> listObject=null; try { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //transation=session.beginTransaction(); StringBuilder sbQuery = new
			 * StringBuilder();
			 * 
			 * ////////////////////////////////////////////////////////
			 * 
			 * 
			 * sbQuery.
			 * append("	SELECT  DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,ORDER_HD.ORDERHD_ID,ORDER_DT.LAB_MARK,ORDER_DT.urgent,ORDER_DT.ORDER_DATE, "
			 * ); sbQuery.append(" ORDER_HD.VISIT_ID,ORDER_HD.OTHER_INVESTIGATION, ");
			 * sbQuery.
			 * append(" ORDER_HD.DEPARTMENT_ID,ORDER_HD.HOSPITAL_ID,ORDER_DT.ORDERDT_ID,DT.RESULT_ENTRY_DT_ID, "
			 * ); sbQuery.
			 * append(" HD.RESULT_ENTRY_HD_ID, DT.RESULT As RESULT,DU.UOM_NAME,DMI.MIN_NORMAL_VALUE,DMI.MAX_NORMAL_VALUE, "
			 * ); sbQuery.
			 * append("  DMI.investigation_Type,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,DT.RIDC_ID,ORDER_DT.INVESTIGATION_REMARKS,DT.RANGE_VALUE, DT.resultOffLineNumber, DT.resultOffLineDate "
			 * ); sbQuery.append(" FROM    "+DG_ORDER_HD+" ORDER_HD LEFT OUTER JOIN  "
			 * +DG_ORDER_DT+" ORDER_DT ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID ");
			 * sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
			 * ); sbQuery.append(" LEFT OUTER JOIN  "
			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID ");
			 * sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_RESULT_ENTRY_HD+" HD ON HD.ORDERHD_ID= ORDER_HD.ORDERHD_ID AND HD.SUB_CHARGECODE_ID=MSC.SUB_CHARGECODE_ID "
			 * ); sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_RESULT_ENTRY_DT+" DT ON DT.RESULT_ENTRY_HD_ID=HD.RESULT_ENTRY_HD_ID  AND DT.INVESTIGATION_ID=DMI.INVESTIGATION_ID "
			 * );
			 * sbQuery.append(" LEFT OUTER JOIN  "+DG_UOM+" DU ON  DU.UOM_ID=DMI.UOM_ID ");
			 * sbQuery.
			 * append(" WHERE  ORDER_HD.visit_id=:visitId AND UPPER(DMI.INVESTIGATION_TYPE) IN ('S','T') "
			 * ); sbQuery.append(" UNION "); sbQuery.
			 * append("SELECT  DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,ORDER_HD.ORDERHD_ID,ORDER_DT.LAB_MARK,ORDER_DT.urgent,ORDER_DT.ORDER_DATE, "
			 * ); sbQuery.append(" ORDER_HD.VISIT_ID,ORDER_HD.OTHER_INVESTIGATION, ");
			 * sbQuery.
			 * append("  ORDER_HD.DEPARTMENT_ID,ORDER_HD.HOSPITAL_ID,ORDER_DT.ORDERDT_ID,null As RESULT_ENTRY_DT_ID, "
			 * ); sbQuery.
			 * append(" NULL As RESULT_ENTRY_HD_ID,NULL AS RESULT,DU.UOM_NAME,DMI.MIN_NORMAL_VALUE,DMI.MAX_NORMAL_VALUE, "
			 * ); sbQuery.
			 * append(" DMI.investigation_Type,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,NULL As RIDC_ID,ORDER_DT.INVESTIGATION_REMARKS, NULL As RANGE_VALUE, NUll as resultOffLineNumber, NULL as resultOffLineDate   "
			 * ); sbQuery.append(" FROM    "+DG_ORDER_DT+" ORDER_DT LEFT OUTER JOIN  "
			 * +DG_ORDER_HD+" ORDER_HD ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID ");
			 * sbQuery.append("  LEFT OUTER JOIN  "
			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
			 * ); sbQuery.append("  LEFT OUTER JOIN  "
			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID ");
			 * sbQuery.append("    LEFT OUTER JOIN  "+DG_UOM+" DU ON  DU.UOM_ID=DMI.UOM_ID "
			 * ); sbQuery.
			 * append(" WHERE  ORDER_HD.visit_id=:visitId AND UPPER(DMI.INVESTIGATION_TYPE) ='M' "
			 * );
			 * 
			 * 
			 * 
			 * Query query = session.createSQLQuery(sbQuery.toString());
			 * 
			 * query.setParameter("visitId", visitId);
			 * 
			 * listObject = query.list(); //transation.commit(); } catch(Exception e) {
			 * e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return listObject; }
			 * 
			 * 
			 * @Override public List<Object[]> getDgMasInvestigationsAndResult(Long
			 * visitId,List<Long>investigationId) { List<Object[]> listObject=null; try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * StringBuilder sbQuery = new StringBuilder();
			 * 
			 * 
			 * ////////////////////////////////////////////////////
			 * 
			 * sbQuery.
			 * append(" SELECT  DSMI.SUB_INVESTIGATION_ID,DSMI.SUB_INVESTIGATION_NAME, ORDER_DT.ORDERDT_ID,ORDER_HD.orderhd_id,DT.RESULT_ENTRY_DT_ID, HD.RESULT_ENTRY_HD_ID,DT.result_type, "
			 * ); sbQuery.
			 * append(" DMI.INVESTIGATION_TYPE, DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,SDU.UOM_NAME, "
			 * ); sbQuery.
			 * append("  DT.RESULT as resulttt,DT.range_value,DT.ridc_id,DSMI.SUB_INVESTIGATION_CODE,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,DT.REMARKS "
			 * ); sbQuery.append(" FROM    "+DG_ORDER_DT+" ORDER_DT LEFT OUTER JOIN  "
			 * +DG_ORDER_HD+" ORDER_HD ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID ");
			 * sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
			 * ); sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_SUB_MAS_INVESTIGATION+" DSMI ON DSMI.INVESTIGATION_ID=DMI.INVESTIGATION_ID "
			 * ); sbQuery.append(" LEFT OUTER JOIN  "
			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID ");
			 * sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_RESULT_ENTRY_HD+" HD ON HD.ORDERHD_ID= ORDER_HD.ORDERHD_ID AND HD.SUB_CHARGECODE_ID=MSC.SUB_CHARGECODE_ID "
			 * ); sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_RESULT_ENTRY_DT+" DT ON DT.RESULT_ENTRY_HD_ID=HD.RESULT_ENTRY_HD_ID  AND DT.INVESTIGATION_ID=DMI.INVESTIGATION_ID AND DSMI.SUB_INVESTIGATION_ID= DT.SUB_INVESTIGATION_ID "
			 * );
			 * sbQuery.append(" LEFT OUTER JOIN  "+DG_UOM+" SDU ON  SDU.UOM_ID=DSMI.UOM_ID "
			 * ); sbQuery.
			 * append(" WHERE  ORDER_HD.visit_id=:visitId  AND UPPER(DMI.INVESTIGATION_TYPE) ='M' "
			 * ); sbQuery.append(" UNION "); sbQuery.
			 * append(" SELECT  null As SUB_INVESTIGATION_ID,null as SUB_INVESTIGATION_NAME, ORDER_DT.ORDERDT_ID,ORDER_HD.orderhd_id,DT.RESULT_ENTRY_DT_ID, HD.RESULT_ENTRY_HD_ID,DT.result_type, "
			 * ); sbQuery.
			 * append(" DMI.INVESTIGATION_TYPE, DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,DU.UOM_NAME,  "
			 * ); sbQuery.
			 * append("  DT.RESULT as resulttt,DT.range_value,DT.ridc_id,null As SUB_INVESTIGATION_CODE,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,DT.REMARKS "
			 * ); sbQuery.append(" FROM    "+DG_ORDER_HD+" ORDER_HD LEFT OUTER JOIN  "
			 * +DG_ORDER_DT+" ORDER_DT ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID ");
			 * sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
			 * ); sbQuery.append(" LEFT OUTER JOIN  "
			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID ");
			 * sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_RESULT_ENTRY_HD+" HD ON HD.ORDERHD_ID= ORDER_HD.ORDERHD_ID AND HD.SUB_CHARGECODE_ID=MSC.SUB_CHARGECODE_ID "
			 * ); sbQuery.append(" LEFT OUTER JOIN  "
			 * +DG_RESULT_ENTRY_DT+" DT ON DT.RESULT_ENTRY_HD_ID=HD.RESULT_ENTRY_HD_ID  AND DT.INVESTIGATION_ID=DMI.INVESTIGATION_ID "
			 * );
			 * sbQuery.append(" LEFT OUTER JOIN  "+DG_UOM+" DU ON  DU.UOM_ID=DMI.UOM_ID ");
			 * sbQuery.
			 * append(" WHERE  ORDER_HD.visit_id=:visitId   AND UPPER(DMI.INVESTIGATION_TYPE) IN ('S','T') And ORDER_DT.INVESTIGATION_ID in:investigationIds "
			 * );
			 * 
			 * 
			 * 
			 * ////////////////////////////////////////
			 * 
			 * 
			 * Query query = session.createSQLQuery(sbQuery.toString());
			 * 
			 * query.setParameter("visitId", visitId);
			 * query.setParameterList("investigationIds", investigationId); listObject =
			 * query.list(); } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return listObject; }
			 * 
			 * @SuppressWarnings("unchecked")
			 * 
			 * @Override public Map<String, Object> getMEMBHistory(HashMap<String, Object>
			 * jsondata) { Session session = null; Criteria criteria = null; Criteria
			 * criteria1 = null; String serviceNo = ""; Long patientId = null;
			 * 
			 * Map<String, Object> map = new HashMap<String, Object>(); List<Visit>
			 * medicalExamReportsList = null; List<Patient> pList = null;
			 * 
			 * if(jsondata.containsKey("serviceNo")) { if(jsondata.get("serviceNo")!=null &&
			 * !jsondata.get("serviceNo").toString().equalsIgnoreCase("")) serviceNo =
			 * jsondata.get("serviceNo").toString().toUpperCase(); }
			 * 
			 * try { int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			 * int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties",
			 * "pageSize").trim()); int count=0;
			 * 
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * 
			 * if(StringUtils.isNotBlank(serviceNo)) { criteria1 =
			 * session.createCriteria(Patient.class) .add(Restrictions.eq("serviceNo",
			 * serviceNo)); pList = criteria1.list();
			 * 
			 * if(pList!=null && pList.size()>0) { patientId = pList.get(0).getPatientId();
			 * criteria = session.createCriteria(Visit.class)
			 * .add(Restrictions.eq("examStatus", "V").ignoreCase())
			 * .add(Restrictions.eq("patientId", patientId))
			 * .addOrder(Property.forName("visitId").desc()); } }else { criteria =
			 * session.createCriteria(Visit.class) .add(Restrictions.eq("examStatus",
			 * "V").ignoreCase()) .addOrder(Property.forName("visitId").desc()); }
			 * 
			 * medicalExamReportsList = criteria.list(); count =
			 * medicalExamReportsList.size(); //criteria =
			 * criteria.setFirstResult(pagingSize * (pageNo - 1)); if (serviceNo != null &&
			 * !serviceNo.equals("") && !serviceNo.equals("null")) { criteria =
			 * criteria.setFirstResult(pagingSize * (1 - 1)); } else { criteria =
			 * criteria.setFirstResult(pagingSize * (pageNo - 1)); }
			 * 
			 * criteria = criteria.setMaxResults(pagingSize);
			 * criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize); medicalExamReportsList = criteria.list();
			 * 
			 * map.put("medicalExamReportsList", medicalExamReportsList); map.put("count",
			 * count);
			 * 
			 * }catch(Exception e) { e.printStackTrace(); }finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return map;
			 * 
			 * }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public String
			 * saveOrUpdateMedicalExam3AForDigi(MasMedicalExamReport
			 * masMedicalExamReprt,HashMap<String, Object> payload,String patientId, String
			 * hospitalId,String userId) {
			 * 
			 * Session session=null; Long medicalExaminationId=null; Transaction tx=null;
			 * String satusOfMessage=""; String statusOfPatient=""; Timestamp ts =null;
			 * 
			 * try{ session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * tx=session.beginTransaction();
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
			 * if(payload.get("unitOrSlip")!=null ) { String
			 * unitOrSlip=payload.get("unitOrSlip").toString();
			 * unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
			 * if(StringUtils.isNotEmpty(unitOrSlip) &&
			 * !unitOrSlip.equalsIgnoreCase("undefined") &&
			 * !unitOrSlip.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setUnitId(Long.parseLong(unitOrSlip)); } }
			 * 
			 * if(payload.get("rank")!=null) { String rank=payload.get("rank").toString();
			 * rank=OpdServiceImpl.getReplaceString(rank); if(StringUtils.isNotEmpty(rank)
			 * && !rank.equalsIgnoreCase("undefined") && !rank.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRankId(Long.parseLong(rank)); } }
			 * if(payload.get("branchOrTrade")!=null) { String
			 * branchOrTrade=payload.get("branchOrTrade").toString();
			 * branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
			 * if(StringUtils.isNotEmpty(branchOrTrade) &&
			 * !branchOrTrade.equalsIgnoreCase("undefined") &&
			 * !branchOrTrade.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setBranchId(Long.parseLong(branchOrTrade)); } } String
			 * totalLengthDigiFile=payload.get("totalLengthDigiFile").toString();
			 * totalLengthDigiFile=OpdServiceImpl.getReplaceString(totalLengthDigiFile);
			 * String dateOfExam = payload.get("dateOfExam").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam);
			 * 
			 * 
			 * String rmdIdVal=patientRegistrationServiceImpl.getFinalInvestigationValue(
			 * investigationIdValues, dynamicUploadInvesIdAndfiles,totalLengthDigiFile) ;
			 * 
			 * 
			 * 
			 * 
			 * 
			 * String[] rangeValues=null; if(payload.containsKey("range")) { String
			 * rangeValue=payload.get("range").toString();
			 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
			 * rangeValue.split(","); }
			 * 
			 * String[] rmsIdFinalValfileUp=null; rmsIdFinalValfileUp = rmdIdVal.split(",");
			 * 
			 * 
			 * String meRmsId=payload.get("meRmsId").toString();
			 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
			 * meRmsId.split(",");
			 * 
			 * payload.put("digiFlag", "yes"); saveOrUpdateMedicalCategoryDigi(payload);
			 * saveOrUpdateMedicalCatCompositeDigi(payload); //try { String
			 * medicalCompositeNameValue =
			 * payload.get("medicalCompositeNameValue").toString();
			 * medicalCompositeNameValue =
			 * OpdServiceImpl.getReplaceString(medicalCompositeNameValue);
			 * 
			 * String diagnosisiIdMC = payload.get("diagnosisiIdMC").toString();
			 * diagnosisiIdMC = OpdServiceImpl.getReplaceString(diagnosisiIdMC); String[]
			 * diagnosisiIdMCValues = null; if(StringUtils.isNotEmpty(diagnosisiIdMC)) {
			 * diagnosisiIdMCValues = diagnosisiIdMC.trim().split(","); } String
			 * fitFlagCheckValue=payload.get("fitFlagCheckValue").toString();
			 * fitFlagCheckValue=OpdServiceImpl.getReplaceString(fitFlagCheckValue); Long
			 * checkPatientUpdate=0l;
			 * 
			 * String visitId = payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * if(StringUtils.isEmpty(medicalCompositeNameValue) &&
			 * diagnosisiIdMCValues==null &&( fitFlagCheckValue.equalsIgnoreCase("Yes") ||
			 * fitFlagCheckValue.contains("Yes"))) { Long masMedicalCategoryId=null;
			 * if(StringUtils.isNotEmpty(patientId)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId)); if(patient!=null) {
			 * Criterion cr1=Restrictions.eq("fitFlag", "F").ignoreCase();
			 * List<MasMedicalCategory>listMasMedicalCategory=masMedicalCategoryDao.
			 * findByCriteria(cr1); if(CollectionUtils.isNotEmpty(listMasMedicalCategory)) {
			 * masMedicalCategoryId=listMasMedicalCategory.get(0).getMedicalCategoryId();
			 * if(masMedicalCategoryId!=null)
			 * patient.setMedicalCategoryId(masMedicalCategoryId); }
			 * 
			 * patient.setFitFlag("F");
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender) && !gender.equalsIgnoreCase("undefined") &&
			 * !gender.equalsIgnoreCase("0"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * 
			 * checkPatientUpdate++;
			 * 
			 * 
			 * saveOrUpdatePatient(patient); } }
			 * 
			 * PatientMedicalCat patientMedicalCat=null;
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * 
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ;
			 * 
			 * if(CollectionUtils.isEmpty(listPatientMedicalCat)) { patientMedicalCat=new
			 * PatientMedicalCat(); if(StringUtils.isNotEmpty(patientId))
			 * patientMedicalCat.setPatientId(Long.parseLong(patientId));
			 * //patientMedicalCat.setMedicalCategoryId(masMedicalCategoryId);
			 * patientMedicalCat.setpMedCatId(masMedicalCategoryId);
			 * if(StringUtils.isNotEmpty(visitId))
			 * patientMedicalCat.setVisitId(Long.parseLong(visitId));
			 * 
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(dateOfExam) && dateOfExam!=null &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date
			 * medicalCompositeDateValue = null; medicalCompositeDateValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(medicalCompositeDateValue!=null) { ts = new
			 * Timestamp(medicalCompositeDateValue.getTime());
			 * patientMedicalCat.setpMedCatDate(medicalCompositeDateValue); } }
			 * patientMedicalCat.setpMedFitFlag("F"); if(patientMedicalCat!=null)
			 * saveOrUpdatePatientMedicalCat(patientMedicalCat); }
			 * 
			 * } else { if(StringUtils.isNotEmpty(patientId)&&
			 * StringUtils.isEmpty(medicalCompositeNameValue)) { Patient patient=
			 * checkPatientForPatientId(Long.parseLong(patientId)); patient.setFitFlag("U");
			 * patient.setMedicalCategoryId(null);
			 * 
			 * if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * 
			 * if(StringUtils.isNotBlank(patientId))
			 * //patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined") &&
			 * !gender.equalsIgnoreCase("0"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender)); } } } }
			 * 
			 * checkPatientUpdate++;
			 * 
			 * saveOrUpdatePatient(patient);
			 * 
			 * List<PatientMedicalCat>listPatientMedicalCat=null;
			 * if(StringUtils.isNotEmpty(visitId)&& StringUtils.isNotEmpty(patientId))
			 * listPatientMedicalCat=
			 * getPatientMedicalCat(Long.parseLong(patientId),Long.parseLong(visitId),"Fit")
			 * ; if(CollectionUtils.isNotEmpty(listPatientMedicalCat)) { PatientMedicalCat
			 * patientMedicalCat=listPatientMedicalCat.get(0); if(patientMedicalCat!=null)
			 * deletePatientMedCat(patientMedicalCat); } } }
			 * masMedicalExamReprt=updateInvestigationDgResultForDigiUpload(payload,
			 * patientId, hospitalId, userId,
			 * masMedicalExamReprt,rangeValues,rmsIdFinalValfileUp,ts);
			 * 
			 * saveUpdatePatientImmunizationHistoryDigi(payload);
			 * 
			 * //Save patient if(payload.get("gender")!=null && checkPatientUpdate==0) {
			 * String gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * Patient patient=null; if(StringUtils.isNotBlank(patientId))
			 * patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender));
			 * patientDao.saveOrUpdate(patient); } } } } String saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft);
			 * 
			 * if(payload.containsKey("actionDigiFile")) { String actionDigiFile =
			 * payload.get("actionDigiFile").toString(); actionDigiFile =
			 * OpdServiceImpl.getReplaceString(actionDigiFile);
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0")) {
			 * 
			 * masMedicalExamReprt.setStatus(actionDigiFile);
			 * 
			 * if(actionDigiFile.equalsIgnoreCase("ev")||actionDigiFile.equalsIgnoreCase(
			 * "vr")) { if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setMoUserId(Long.parseLong(userId));
			 * 
			 * String finalObservationMo = payload.get("finalObservationMo").toString();
			 * finalObservationMo = OpdServiceImpl.getReplaceString(finalObservationMo);
			 * masMedicalExamReprt.setFinalobservation(finalObservationMo);
			 * masMedicalExamReprt.setMoDate(new Date());
			 * masMedicalExamReprt.setPetStatus(actionDigiFile); } } }
			 * 
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { String
			 * actionDigiFileApproved = payload.get("actionDigiFileApproved").toString();
			 * actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setStatus(actionDigiFileApproved);
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||actionDigiFileApproved.
			 * equalsIgnoreCase("ar")) { if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setCmUserId(Long.parseLong(userId));
			 * 
			 * String finalObservationRMo = payload.get("finalObservationRMo").toString();
			 * finalObservationRMo = OpdServiceImpl.getReplaceString(finalObservationRMo);
			 * masMedicalExamReprt.setAaFinalObservation(finalObservationRMo);
			 * masMedicalExamReprt.setCmDate(new Date()); String hospitalForDigi="";
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")) { visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * if(payload.containsKey("hospitalForDigi")) { hospitalForDigi =
			 * payload.get("hospitalForDigi").toString(); hospitalForDigi =
			 * OpdServiceImpl.getReplaceString(hospitalForDigi); }
			 * updateVisitStatusForDigi(visitId, "c",hospitalForDigi,dateOfExam); } } } }
			 * 
			 * 
			 * 
			 * if (StringUtils.isNotEmpty(saveInDraft) &&
			 * (saveInDraft.equalsIgnoreCase("et")||saveInDraft.equalsIgnoreCase("es"))){
			 * masMedicalExamReprt.setStatus(saveInDraft);
			 * if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setMaUserId(Long.parseLong(userId)); String
			 * finalObservationMa = payload.get("finalObservationMa").toString();
			 * finalObservationMa = OpdServiceImpl.getReplaceString(finalObservationMa);
			 * //masMedicalExamReprt.set masMedicalExamReprt.setMaDate(new Date());
			 * masMedicalExamReprt.setPetStatus(null);
			 * masMedicalExamReprt.setAaFinalObservation(null);
			 * masMedicalExamReprt.setFinalobservation(null); }
			 * 
			 * masMedicalExamReprt=saveUpdateForReferalforMeDigiFileUpload(
			 * masMedicalExamReprt,payload,hospitalId);
			 * 
			 * if(meRmsIds!=null && meRmsIds.length>0 && meRmsIds[0]!=null &&
			 * !meRmsIds[0].equalsIgnoreCase("") && !meRmsIds[0].equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRidcId(Long.parseLong(meRmsIds[0].toString())); }
			 * 
			 * //Save PatientDocumentDetail saveUpdateOfPatientDocumentDetail(payload);
			 * session.saveOrUpdate("MasMedicalExamReport", masMedicalExamReprt);
			 * ///////////////////////////////////////////////////
			 * 
			 * medicalExaminationId=masMedicalExamReprt.getMedicalExaminationId();
			 * if(masMedicalExamReprt!=null &&
			 * StringUtils.isNotEmpty(masMedicalExamReprt.getStatus())) {
			 * statusOfPatient=masMedicalExamReprt.getStatus(); } else {
			 * statusOfPatient="no status"; } session.flush(); session.clear(); tx.commit();
			 * if(medicalExaminationId!=null && medicalExaminationId!=0) {
			 * satusOfMessage=medicalExaminationId+
			 * "##"+"Medical Exam Submitted Successfully"+"##"+statusOfPatient; } else {
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong."+"##"+
			 * statusOfPatient; } } catch(Exception e) { if (tx != null) { try {
			 * tx.rollback();
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; } catch(Exception re) {
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+re.
			 * toString()+"##"+statusOfPatient; re.printStackTrace(); } }
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return satusOfMessage;
			 * 
			 * }
			 * 
			 * 
			 * @Transactional
			 * 
			 * @Override public Long saveOrUpdateMasMedicalExamReport(MasMedicalExamReport
			 * masMedicalExamReport) { Session session=null; Long medicalExamId=null;
			 * //Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * //tx=session.beginTransaction(); session.saveOrUpdate(masMedicalExamReport);
			 * medicalExamId=masMedicalExamReport.getMedicalExaminationId();
			 * session.flush(); session.clear(); //tx.commit();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * //getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return medicalExamId; }
			 * 
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public Users getUserByUserIdList(Long userId) { List<Users>
			 * listUers=null; Users users=null; try { Criteria cretieria=null; Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * cretieria=session.createCriteria(Users.class).add(Restrictions.eq("userId",
			 * userId)); ProjectionList projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("firstName").as("firstName"));
			 * projectionList.add(Projections.property("lastName").as("lastName"));
			 * projectionList.add(Projections.property("serviceNo").as("serviceNo"));
			 * listUers = cretieria.list();
			 * 
			 * if(CollectionUtils.isNotEmpty(listUers)) { for (Iterator<?> it =
			 * listUers.iterator(); it.hasNext();) { users=new Users(); Object[] row =
			 * (Object[]) it.next(); if(row[0]!=null) {
			 * users.setFirstName(row[0].toString()); } if(row[1]!=null) {
			 * users.setLastName(row[1].toString()); } if(row[2]!=null) {
			 * users.setServiceNo(row[2].toString()); } }
			 * 
			 * } if(CollectionUtils.isNotEmpty(listUers)) { users=listUers.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return users; }
			 * 
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public String
			 * saveOrUpdateMedicalExam2AForDigi(MasMedicalExamReport
			 * masMedicalExamReprt,HashMap<String, Object> payload,String patientId, String
			 * hospitalId,String userId) {
			 * 
			 * Session session=null; Long medicalExaminationId=null; Transaction tx=null;
			 * String satusOfMessage=""; String statusOfPatient=""; Timestamp ts =null;
			 * 
			 * try{ session= getHibernateUtils.getHibernateUtlis().OpenSession();
			 * tx=session.beginTransaction();
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
			 * if(payload.containsKey("unitOrSlip") && payload.get("unitOrSlip")!=null ) {
			 * String unitOrSlip=payload.get("unitOrSlip").toString();
			 * unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
			 * if(StringUtils.isNotEmpty(unitOrSlip) &&
			 * !unitOrSlip.equalsIgnoreCase("undefined") &&
			 * !unitOrSlip.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setUnitId(Long.parseLong(unitOrSlip)); } }
			 * 
			 * if(payload.get("rank")!=null) { String rank=payload.get("rank").toString();
			 * rank=OpdServiceImpl.getReplaceString(rank); if(StringUtils.isNotEmpty(rank)
			 * && !rank.equalsIgnoreCase("undefined") && !rank.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRankId(Long.parseLong(rank)); } }
			 * if(payload.get("branchOrTrade")!=null) { String
			 * branchOrTrade=payload.get("branchOrTrade").toString();
			 * branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
			 * if(StringUtils.isNotEmpty(branchOrTrade) &&
			 * !branchOrTrade.equalsIgnoreCase("undefined") &&
			 * !branchOrTrade.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setBranchId(Long.parseLong(branchOrTrade)); } } String
			 * totalLengthDigiFile=payload.get("totalLengthDigiFile").toString();
			 * totalLengthDigiFile=OpdServiceImpl.getReplaceString(totalLengthDigiFile);
			 * String dateOfExam = payload.get("dateOfExam").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam);
			 * 
			 * 
			 * String rmdIdVal=patientRegistrationServiceImpl.getFinalInvestigationValue(
			 * investigationIdValues, dynamicUploadInvesIdAndfiles,totalLengthDigiFile) ;
			 * 
			 * String[] rangeValues=null; if(payload.containsKey("range")) { String
			 * rangeValue=payload.get("range").toString();
			 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
			 * rangeValue.split(","); }
			 * 
			 * String[] rmsIdFinalValfileUp=null; rmsIdFinalValfileUp = rmdIdVal.split(",");
			 * 
			 * 
			 * String meRmsId=payload.get("meRmsId").toString();
			 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
			 * meRmsId.split(",");
			 * 
			 * masMedicalExamReprt=updateInvestigationDgResultForDigiUpload(payload,
			 * patientId, hospitalId, userId,
			 * masMedicalExamReprt,rangeValues,rmsIdFinalValfileUp,ts);
			 * 
			 * //saveUpdatePatientImmunizationHistory(payload);
			 * 
			 * //Save patient if(payload.get("gender")!=null) { String
			 * gender=payload.get("gender").toString();
			 * gender=OpdServiceImpl.getReplaceString(gender);
			 * if(StringUtils.isNotEmpty(patientId)) { if(StringUtils.isNotEmpty(gender)) {
			 * Patient patient=null; if(StringUtils.isNotBlank(patientId))
			 * patient=getPatient(Long.parseLong(patientId)); if(patient!=null) {
			 * if(StringUtils.isNotEmpty(gender)&& !gender.equalsIgnoreCase("undefined") &&
			 * !gender.equalsIgnoreCase("0"))
			 * patient.setAdministrativeSexId(Long.parseLong(gender));
			 * patientDao.saveOrUpdate(patient); } } } } String saveInDraft =
			 * payload.get("saveInDraft").toString(); saveInDraft =
			 * OpdServiceImpl.getReplaceString(saveInDraft);
			 * 
			 * if(payload.containsKey("actionDigiFile")) { String actionDigiFile =
			 * payload.get("actionDigiFile").toString(); actionDigiFile =
			 * OpdServiceImpl.getReplaceString(actionDigiFile);
			 * if(StringUtils.isNotEmpty(actionDigiFile) &&
			 * !actionDigiFile.equalsIgnoreCase("0")) {
			 * 
			 * masMedicalExamReprt.setStatus(actionDigiFile);
			 * 
			 * if(actionDigiFile.equalsIgnoreCase("ev")||actionDigiFile.equalsIgnoreCase(
			 * "vr")) { if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setMoUserId(Long.parseLong(userId));
			 * 
			 * String finalObservationMo = payload.get("finalObservationMo").toString();
			 * finalObservationMo = OpdServiceImpl.getReplaceString(finalObservationMo);
			 * masMedicalExamReprt.setFinalobservation(finalObservationMo);
			 * masMedicalExamReprt.setMoDate(new Date());
			 * masMedicalExamReprt.setPetStatus(actionDigiFile); } } }
			 * 
			 * 
			 * if(payload.containsKey("actionDigiFileApproved")) { String
			 * actionDigiFileApproved = payload.get("actionDigiFileApproved").toString();
			 * actionDigiFileApproved =
			 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
			 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
			 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setStatus(actionDigiFileApproved);
			 * 
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||actionDigiFileApproved.
			 * equalsIgnoreCase("ar")) { if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setCmUserId(Long.parseLong(userId));
			 * 
			 * String finalObservationRMo = payload.get("finalObservationRMo").toString();
			 * finalObservationRMo = OpdServiceImpl.getReplaceString(finalObservationRMo);
			 * masMedicalExamReprt.setAaFinalObservation(finalObservationRMo);
			 * masMedicalExamReprt.setCmDate(new Date()); String hospitalForDigi="";
			 * if(actionDigiFileApproved.equalsIgnoreCase("ea")) { String visitId =
			 * payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * if(payload.containsKey("hospitalForDigi")) { hospitalForDigi =
			 * payload.get("hospitalForDigi").toString(); hospitalForDigi =
			 * OpdServiceImpl.getReplaceString(hospitalForDigi); }
			 * updateVisitStatusForDigi(visitId, "c",hospitalForDigi,dateOfExam); } } } }
			 * 
			 * 
			 * 
			 * if (StringUtils.isNotEmpty(saveInDraft) &&
			 * (saveInDraft.equalsIgnoreCase("et")||saveInDraft.equalsIgnoreCase("es"))){
			 * masMedicalExamReprt.setStatus(saveInDraft);
			 * if(StringUtils.isNotEmpty(userId))
			 * masMedicalExamReprt.setMaUserId(Long.parseLong(userId)); String
			 * finalObservationMa = payload.get("finalObservationMa").toString();
			 * finalObservationMa = OpdServiceImpl.getReplaceString(finalObservationMa);
			 * //masMedicalExamReprt.set masMedicalExamReprt.setMaDate(new Date());
			 * masMedicalExamReprt.setPetStatus(null);
			 * masMedicalExamReprt.setAaFinalObservation(null);
			 * masMedicalExamReprt.setFinalobservation(null); }
			 * 
			 * // masMedicalExamReprt=saveUpdateForReferalforMeDigiFileUpload(
			 * masMedicalExamReprt,payload,hospitalId);
			 * 
			 * if(meRmsIds!=null && meRmsIds.length>0 && meRmsIds[0]!=null &&
			 * !meRmsIds[0].equalsIgnoreCase("") && !meRmsIds[0].equalsIgnoreCase("0")) {
			 * masMedicalExamReprt.setRidcId(Long.parseLong(meRmsIds[0].toString())); }
			 * 
			 * //Save PatientDocumentDetail saveUpdateOfPatientDocumentDetail(payload);
			 * 
			 * /////////////////////////////////saveOrUpdate Family
			 * Detail////////////////////////////// saveOrUpdateFamilyDetails(payload);
			 * session.saveOrUpdate(masMedicalExamReprt);
			 * ///////////////////////////////////////////////////
			 * 
			 * medicalExaminationId=masMedicalExamReprt.getMedicalExaminationId();
			 * if(masMedicalExamReprt!=null &&
			 * StringUtils.isNotEmpty(masMedicalExamReprt.getStatus())) {
			 * statusOfPatient=masMedicalExamReprt.getStatus(); } else {
			 * statusOfPatient="no status"; } session.flush(); session.clear(); tx.commit();
			 * if(medicalExaminationId!=null && medicalExaminationId!=0) {
			 * satusOfMessage=medicalExaminationId+
			 * "##"+"Medical Exam Submitted Successfully"+"##"+statusOfPatient; } else {
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong."+"##"+
			 * statusOfPatient; } } catch(Exception e) { if (tx != null) { try {
			 * tx.rollback();
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; } catch(Exception re) {
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+re.
			 * toString()+"##"+statusOfPatient; re.printStackTrace(); } }
			 * satusOfMessage=0+"##"+"Data is not updated because something is wrong"+e.
			 * toString()+"##"+statusOfPatient; e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return satusOfMessage;
			 * 
			 * }
			 * 
			 * 
			 * @Transactional public void saveOrUpdateFamilyDetails(HashMap<String, Object>
			 * payload) { //try {
			 * 
			 * String familyId = payload.get("familyId").toString(); familyId =
			 * OpdServiceImpl.getReplaceString(familyId);
			 * 
			 * String illnessHistory = payload.get("illnessHistory").toString();
			 * illnessHistory = OpdServiceImpl.getReplaceString(illnessHistory);
			 * 
			 * String ageOfFather = payload.get("ageOfFather").toString(); ageOfFather =
			 * OpdServiceImpl.getReplaceString(ageOfFather);
			 * 
			 * String healthOfFather = payload.get("healthOfFather").toString();
			 * healthOfFather = OpdServiceImpl.getReplaceString(healthOfFather);
			 * 
			 * String causeOfDeathFather = payload.get("causeOfDeathFather").toString();
			 * causeOfDeathFather = OpdServiceImpl.getReplaceString(causeOfDeathFather);
			 * 
			 * String dateDiedOfFather = payload.get("dateDiedOfFather").toString();
			 * dateDiedOfFather = OpdServiceImpl.getReplaceString(dateDiedOfFather);
			 * 
			 * 
			 * String[] familyIdArray = familyId.split(","); String[] illnessHistoryArray =
			 * illnessHistory.split(","); String[] ageOfFatherArray =
			 * ageOfFather.split(","); String[] healthOfFatherArray =
			 * healthOfFather.split("@@@###"); String[] causeOfDeathFatherArray =
			 * causeOfDeathFather.split("@@@###"); String[] dateDiedOfFatherArray =
			 * dateDiedOfFather.split(","); String visitId =
			 * payload.get("visitId").toString(); visitId
			 * =OpdServiceImpl.getReplaceString(visitId);
			 * 
			 * 
			 * String finalValue=""; HashMap<String,String> mapInvestigationMap=new
			 * HashMap<>(); Integer counter=1; for (int i = 0; i <
			 * illnessHistoryArray.length; i++) { if
			 * (StringUtils.isNotEmpty(illnessHistoryArray[i].toString()) &&
			 * !illnessHistoryArray[i].equalsIgnoreCase("0")) { finalValue +=
			 * illnessHistoryArray[i].trim(); if (!familyIdArray[i].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(familyIdArray[i])) { for (int m = i; m <
			 * familyIdArray.length; m++) { finalValue += "###" + familyIdArray[m].trim();
			 * if (m == i) { break; } } } else { finalValue += "###" + "0"; }
			 * 
			 * if (i< ageOfFatherArray.length && !ageOfFatherArray[i].equalsIgnoreCase("0")
			 * && StringUtils.isNotBlank(ageOfFatherArray[i])) { for (int j = i; j <
			 * ageOfFatherArray.length; j++) { finalValue += "###" +
			 * ageOfFatherArray[j].trim(); if (j == i) { break; } } } else { finalValue +=
			 * "###" + "0"; }
			 * 
			 * 
			 * 
			 * int tempI=0; if(healthOfFatherArray!=null && healthOfFatherArray.length>0) {
			 * tempI=i; i++; if (i < healthOfFatherArray.length &&
			 * StringUtils.isNotBlank(healthOfFatherArray[i])) { for (int k = i; k <
			 * healthOfFatherArray.length; k++) { finalValue += "###" +
			 * healthOfFatherArray[k].trim(); if (k == i) { i=tempI; tempI=0; break; } } }
			 * else { i=tempI; tempI=0; finalValue += "###" + "0"; } } else { i=tempI;
			 * tempI=0; finalValue += "###" + "0"; }
			 * 
			 * 
			 * 
			 * int tempIc=0; if(causeOfDeathFatherArray!=null &&
			 * causeOfDeathFatherArray.length>0) { tempIc=i; i++; if (i <
			 * causeOfDeathFatherArray.length &&
			 * StringUtils.isNotBlank(causeOfDeathFatherArray[i])) { for (int k = i; k <
			 * causeOfDeathFatherArray.length; k++) { finalValue += "###" +
			 * causeOfDeathFatherArray[k].trim(); if (k == i) { i=tempIc; tempIc=0; break; }
			 * } } else { i=tempIc; tempIc=0; finalValue += "###" + "0"; } } else {
			 * i=tempIc; tempIc=0; finalValue += "###" + "0"; }
			 * 
			 * 
			 * if (i < dateDiedOfFatherArray.length &&
			 * StringUtils.isNotBlank(dateDiedOfFatherArray[i])) { for (int l = i; l <
			 * dateDiedOfFatherArray.length; l++) { finalValue += "###" +
			 * dateDiedOfFatherArray[l].trim(); if (l == i) { break; } } } else { finalValue
			 * += "###" + "0"; }
			 * 
			 * 
			 * mapInvestigationMap.put(illnessHistoryArray[i].trim() + "@#" + counter,
			 * finalValue); finalValue = ""; counter++; } } counter = 1; for (String
			 * illnessHistoryObj : illnessHistoryArray) { if
			 * (StringUtils.isNotEmpty(illnessHistoryObj)) { if
			 * (mapInvestigationMap.containsKey(illnessHistoryObj.trim() + "@#" + counter))
			 * { String finalValueRow = mapInvestigationMap.get(illnessHistoryObj.trim() +
			 * "@#" + counter);
			 * 
			 * if (StringUtils.isNotEmpty(finalValueRow)) {
			 * 
			 * String[] finalValueFamily = finalValueRow.split("###"); FamilyDetail
			 * familyDetail = null; if (finalValueFamily[1] != null &&
			 * !finalValueFamily[1].equalsIgnoreCase("0") &&
			 * StringUtils.isNotBlank(finalValueFamily[1])) { familyDetail =
			 * familyDetailsDAO.
			 * getFamilyDetailsByFamilyId(Long.parseLong(finalValueFamily[1].toString())); }
			 * else { familyDetail = new FamilyDetail(); }
			 * 
			 * if (finalValueFamily != null) { if (finalValueFamily[0] != null &&
			 * StringUtils.isNotBlank(finalValueFamily[0]) &&
			 * !finalValueFamily[0].equals("0"))
			 * familyDetail.setLeaveApplicationId(Long.parseLong(finalValueFamily[0].
			 * toString())); try { if (finalValueFamily[2] != null &&
			 * StringUtils.isNotBlank(finalValueFamily[2]) &&
			 * !finalValueFamily[2].equals("0") &&
			 * !finalValueFamily[2].equalsIgnoreCase("NA"))
			 * familyDetail.setAge(Long.parseLong(finalValueFamily[2].toString())); }
			 * catch(Exception e) {e.printStackTrace();} if (finalValueFamily[3] != null &&
			 * StringUtils.isNotBlank(finalValueFamily[3]) &&
			 * !finalValueFamily[3].equals("0")) {
			 * 
			 * String health = ""; int index=finalValueFamily[3].lastIndexOf(",");
			 * if(index==-1) { health = getHtmlText(finalValueFamily[3].toString());
			 * 
			 * //referralPatientDt.setFinalNote(result); } else { health =
			 * finalValueFamily[3].toString().substring(0,
			 * finalValueFamily[3].toString().length() - 1); health = getHtmlText(health); }
			 * 
			 * familyDetail.setSelectFamily(health); }
			 * 
			 * if (finalValueFamily[4] != null &&
			 * StringUtils.isNotBlank(finalValueFamily[4]) &&
			 * !finalValueFamily[4].equals("0")) { String causeOfHealth = ""; int
			 * index=finalValueFamily[4].lastIndexOf(","); if(index==-1) { causeOfHealth =
			 * getHtmlText(finalValueFamily[4].toString());
			 * 
			 * } else { causeOfHealth = finalValueFamily[4].toString().substring(0,
			 * finalValueFamily[4].toString().length() - 1); causeOfHealth =
			 * getHtmlText(causeOfHealth); } familyDetail.setDependentPorNo(causeOfHealth);
			 * }
			 * 
			 * try { if (finalValueFamily[5] != null &&
			 * StringUtils.isNotBlank(finalValueFamily[5]) &&
			 * !finalValueFamily[5].equals("0")) { Date familyDate = null;
			 * if(MedicalExamServiceImpl.checkDateFormat(finalValueFamily[5].toString()))
			 * familyDate =
			 * HMSUtil.convertStringTypeDateToDateType(finalValueFamily[5].trim());
			 * if(familyDate!=null) familyDetail.setDob(familyDate); } } catch(Exception e)
			 * { e.printStackTrace(); } } if(StringUtils.isNotEmpty(visitId))
			 * familyDetail.setVisitId(Long.parseLong(visitId));
			 * saveOrUpdateFamilyDetail(familyDetail); } } } counter++; } //counter++; }
			 * 
			 * 
			 * 
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public void saveOrUpdateFamilyDetail(FamilyDetail
			 * familyDetails) { Session session=null; try{ if(session==null) { session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); }
			 * session.saveOrUpdate(familyDetails); session.flush(); session.clear();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } }
			 * 
			 * @Transactional public Long saveUpdateOpdVitalDetails(HashMap<String, Object>
			 * payload,OpdPatientDetail opdPatientDetail,String
			 * hospitalId,MasMedicalExamReport masMedicalExamReprt) {
			 * 
			 * Long opdPatientDetailId=null; String patientId =
			 * payload.get("patientId").toString(); patientId =
			 * OpdServiceImpl.getReplaceString(patientId);
			 * 
			 * String visitId = payload.get("visitId").toString(); visitId =
			 * OpdServiceImpl.getReplaceString(visitId); opdPatientDetail=
			 * getOpdPatientDetailByVisitId(Long.parseLong(visitId));
			 * if(opdPatientDetail==null) { opdPatientDetail=new OpdPatientDetail(); Date
			 * date=new Date(); opdPatientDetail.setLastChgDate(new
			 * Timestamp(date.getTime())); }
			 * 
			 * if(StringUtils.isNotEmpty(patientId))
			 * opdPatientDetail.setPatientId(Long.parseLong(patientId));
			 * 
			 * 
			 * if(StringUtils.isNotEmpty(visitId))
			 * opdPatientDetail.setVisitId(Long.parseLong(visitId));
			 * 
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getHeight()!=null) {
			 * opdPatientDetail.setHeight(masMedicalExamReprt.getHeight().toString()); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getWeight()!=null) {
			 * opdPatientDetail.setWeight(masMedicalExamReprt.getWeight().toString()); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getIdealweight()!=null) {
			 * opdPatientDetail.setIdealWeight(masMedicalExamReprt.getIdealweight().toString
			 * ()); } if(masMedicalExamReprt!=null &&
			 * masMedicalExamReprt.getPulseRates()!=null) {
			 * opdPatientDetail.setPulse(masMedicalExamReprt.getPulseRates().toString()); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getBpSystolic()!=null) {
			 * opdPatientDetail.setBpSystolic(masMedicalExamReprt.getBpSystolic().toString()
			 * ); }
			 * 
			 * if(masMedicalExamReprt!=null && masMedicalExamReprt.getBpDiastolic()!=null) {
			 * opdPatientDetail.setBpDiastolic(masMedicalExamReprt.getBpDiastolic().toString
			 * ()); } String dateOfExam =""; if(payload.containsKey("dateOfExam")) {
			 * dateOfExam = payload.get("dateOfExam").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam); if
			 * (StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(dateOfExamValue!=null) opdPatientDetail.setOpdDate(new
			 * Timestamp(dateOfExamValue.getTime())); } else { try { Date
			 * dateOfExamValue=new Date(); opdPatientDetail.setOpdDate(new
			 * Timestamp(dateOfExamValue.getTime())); }catch(Exception e)
			 * {e.printStackTrace();} }
			 * 
			 * } if(payload.containsKey("dateOfRelease")) { dateOfExam =
			 * payload.get("dateOfRelease").toString(); dateOfExam =
			 * OpdServiceImpl.getReplaceString(dateOfExam); if
			 * (StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("") &&
			 * MedicalExamServiceImpl.checkDateFormat(dateOfExam)) { Date dateOfExamValue =
			 * HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
			 * if(dateOfExamValue!=null) opdPatientDetail.setOpdDate(new
			 * Timestamp(dateOfExamValue.getTime())); } else { try { Date
			 * dateOfExamValue=new Date(); opdPatientDetail.setOpdDate(new
			 * Timestamp(dateOfExamValue.getTime())); }catch(Exception e)
			 * {e.printStackTrace();} } } try { if(payload.containsKey("diagnosisId")) {
			 * String diagnosisId1 = payload.get("diagnosisId").toString(); String
			 * diagnosisId2 = OpdServiceImpl.getReplaceString(diagnosisId1);
			 * if(StringUtils.isNotEmpty(diagnosisId2)) {
			 * opdPatientDetail.setIcdDiagnosis(diagnosisId1); } } } catch(Exception e) {
			 * e.printStackTrace(); }
			 * 
			 * 
			 * opdPatientDetailId= updateOpdPatientDetailMe(opdPatientDetail);
			 * if(payload.get("diagnosisiIdMC")!=null &&
			 * payload.containsKey("diagnosisiIdMC")) { saveOrUpdateDischargeIcg(payload,
			 * visitId, opdPatientDetailId, patientId); } return opdPatientDetailId;
			 * 
			 * }
			 * 
			 * 
			 * @Override public PatientImmunizationHistory
			 * getPatientImmunizationHistoryByVisit(Long visitId,Long itemId,Long patientId)
			 * { PatientImmunizationHistory patientImmunizationHistory=null;
			 * List<PatientImmunizationHistory>listPatientImmunizationHistory=null; try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * Criteria cr1= session.createCriteria(PatientImmunizationHistory.class)
			 * .add(Restrictions.eq("patientId", patientId)).add(Restrictions.eq("itemId",
			 * itemId)).add(Restrictions.eq("visitId", visitId)) ;
			 * listPatientImmunizationHistory=cr1.list();
			 * if(CollectionUtils.isNotEmpty(listPatientImmunizationHistory)) {
			 * patientImmunizationHistory=listPatientImmunizationHistory.get(0); } }
			 * catch(Exception e) { e.printStackTrace(); } finally { } return
			 * patientImmunizationHistory; }
			 * 
			 * 
			 * @Override public PatientImmunizationHistory
			 * getPatientImmunizationHistoryByImmunizationId(Long immunizationId) {
			 * PatientImmunizationHistory patientImmunizationHistory=null; try { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * patientImmunizationHistory=(PatientImmunizationHistory)
			 * session.createCriteria(PatientImmunizationHistory.class)
			 * .add(Restrictions.eq("immunizationId", immunizationId)).uniqueResult(); }
			 * catch(Exception e) { e.printStackTrace(); } finally { } return
			 * patientImmunizationHistory; }
			 * 
			 * @Override public Long
			 * saveOrUpdatePatientImmunizationHistoryComm(PatientImmunizationHistory
			 * patientImmunizationHistory,Session session) { Long immunizationId=null;
			 * session.saveOrUpdate(patientImmunizationHistory);
			 * immunizationId=patientImmunizationHistory.getImmunizationId();
			 * session.flush(); session.clear(); return immunizationId; }
			 * 
			 * 
			 * @Override
			 * 
			 * @Transactional public MasMedicalExamReport
			 * getMasMedicalExamReprtByVisitIdForStatus(Long visitId) { MasMedicalExamReport
			 * masMedicalExamReprt=null;
			 * List<MasMedicalExamReport>listMasMedicalExamReprt=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria2=
			 * session.createCriteria(MasMedicalExamReport.class).add(Restrictions.eq(
			 * "visitId", visitId)); ProjectionList projectionList =
			 * Projections.projectionList();
			 * projectionList.add(Projections.property("status").as("status"));
			 * projectionList.add(Projections.property("medicalExaminationId").as(
			 * "medicalExaminationId")); criteria2.setProjection(projectionList);
			 * 
			 * listMasMedicalExamReprt = criteria2 .setResultTransformer(new
			 * AliasToBeanResultTransformer(MasMedicalExamReport.class)).list();
			 * if(CollectionUtils.isNotEmpty(listMasMedicalExamReprt)) {
			 * masMedicalExamReprt=listMasMedicalExamReprt.get(0); }
			 * 
			 * } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return masMedicalExamReprt; }
			 * 
			 * @Override public String getMainChargeCodeByMainChargeCodeIdOp(List<Object[]>
			 * mainChargeCodeId) { Session session =null; String mainChargeCodeVal=""; try {
			 * List<Long>longValu=new ArrayList<Long>(); for(Object[] oo:mainChargeCodeId) {
			 * longValu.add(Long.parseLong(oo[3].toString())); }
			 * session=getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
			 * session.createCriteria(MasMainChargecode.class)
			 * .add(Restrictions.in("mainChargecodeId", longValu)); ProjectionList
			 * projectionList = Projections.projectionList();
			 * projectionList.add(Projections.property("mainChargecodeCode").as(
			 * "mainChargecodeCode"));
			 * projectionList.add(Projections.property("mainChargecodeId").as(
			 * "mainChargecodeId")); cr.setProjection(projectionList);
			 * List<Object[]>listString=cr.list();
			 * //List<MasMainChargecode>listInvHd=cr.setResultTransformer(new
			 * AliasToBeanResultTransformer(MasMainChargecode.class)).list();
			 * if(CollectionUtils.isNotEmpty(listString)) for(Object[] oo:listString) {
			 * mainChargeCodeVal+= oo[0].toString()+"##"+
			 * Long.parseLong(oo[1].toString())+","; } } catch(Exception e) {
			 * e.printStackTrace(); } return mainChargeCodeVal; }
			 * 
			 * @Override public String getRHQByVisitId(Long visitId) { String rhqName="";
			 * try {
			 * 
			 * Transaction transation=null; List<Object[]> listObject=null; try { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * transation=session.beginTransaction(); StringBuilder sbQuery = new
			 * StringBuilder(); sbQuery.
			 * append(" select  case when FOWARD_UNIT_ID=1484 then '' else (select hospital_name from "
			 * +MAS_HOSPITAL+" where hospital_id=FOWARD_UNIT_ID ) end "); sbQuery.append(
			 * "from "+MAS_MEDICAL_EXAM_REPORT+" where visit_id=:visitId ");
			 * 
			 * Query query = session.createSQLQuery(sbQuery.toString());
			 * query.setParameter("visitId", visitId); listObject = query.list();
			 * if(listObject!=null) { Object ooo=listObject.get(0); if(ooo!=null)
			 * rhqName=(String)ooo; } transation.commit(); } catch(Exception e) {
			 * e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return rhqName;
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } return rhqName; }
			 * 
			 * 
			 * @Override public List<Object[]> getAllRHQ() { List<Object[]> listObject=null;
			 * try {
			 * 
			 * 
			 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * StringBuilder sbQuery = new StringBuilder(); sbQuery.
			 * append(" select mas_hospital.HOSPITAL_ID,mas_hospital.HOSPITAL_NAME from "
			 * +VU_MAS_UNIT+","+
			 * MAS_HOSPITAL+" where vu_mas_unit.unit_id=mas_hospital.unit_id and  unittype_id=3 and upper(mas_hospital.status)='Y' "
			 * ); sbQuery.append( " union "); sbQuery.append(
			 * " select mas_hospital.HOSPITAL_ID,mas_hospital.HOSPITAL_NAME  from  vu_mas_unit,"
			 * +MAS_HOSPITAL+" where vu_mas_unit.unit_id=mas_hospital.unit_id and vu_mas_unit.unit_id=226 and upper(mas_hospital.status)='Y' "
			 * );
			 * 
			 * Query query = session.createSQLQuery(sbQuery.toString()); listObject =
			 * query.list();
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return listObject;
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } return listObject; }
			 * 
			 * 
			 * @Override public List<MasHospital> getRMOList(HashMap<String, Object>
			 * jsonData) { List<MasHospital>listMasHospital =new ArrayList<>(); try {
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * 
			 * String hospitalId = jsonData.get("hospitalId").toString();
			 * 
			 * if(StringUtils.isNotEmpty(hospitalId)) { int
			 * hospitalIdValue=Integer.parseInt(hospitalId);
			 * 
			 * StringBuilder sbQuery = new StringBuilder(); session.doWork(new Work() {
			 * 
			 * @Override public void execute(java.sql.Connection connection) throws
			 * SQLException { //CallableStatement call =
			 * connection.prepareCall("CALL ASP_HIERARCHICAL_UNIT(?, ?)"); //call.setInt(1,
			 * hospitalIdValue);
			 * 
			 * //call.registerOutParameter(2, OracleTypes.CURSOR);
			 * connection.setAutoCommit(false); String
			 * sss="{ ? = call "+databaseScema+".ASP_HIERARCHICAL_UNIT( ? ) }";
			 * CallableStatement call = connection.prepareCall(sss);
			 * call.registerOutParameter(1, Types.REF_CURSOR); call.setInt(2,
			 * hospitalIdValue); call.execute(); ResultSet rs =
			 * (ResultSet)call.getObject(2);
			 * 
			 * while(rs!=null && rs.next()) { MasHospital masHospital=new MasHospital();
			 * 
			 * if(rs.getLong(1)!=0) { masHospital.setHospitalId(rs.getLong(1)); }
			 * if(rs.getString(4)!=null) { masHospital.setHospitalName(rs.getString(4)); }
			 * listMasHospital.add(masHospital); }
			 * 
			 * } }); } } catch(Exception e) { e.printStackTrace(); }
			 * 
			 * return listMasHospital; }
			 * 
			 * @Override public String getRHQByVisitId(Long visitId,Long forwardedUnitId) {
			 * String rhqName=""; try {
			 * 
			 * Transaction transation=null; List<Object[]> listObject=null; try { Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * transation=session.beginTransaction(); StringBuilder sbQuery = new
			 * StringBuilder(); sbQuery.
			 * append(" select  case when FOWARD_UNIT_ID=:forwardedUnitId then '' else (select hospital_name from mas_hospital where hospital_id=FOWARD_UNIT_ID ) end "
			 * ); sbQuery.append(
			 * "from "+MAS_MEDICAL_EXAM_REPORT+" where visit_id=:visitId ");
			 * 
			 * Query query = session.createSQLQuery(sbQuery.toString());
			 * query.setParameter("forwardedUnitId", forwardedUnitId);
			 * query.setParameter("visitId", visitId); listObject = query.list();
			 * if(listObject!=null) { Object ooo=listObject.get(0); if(ooo!=null)
			 * rhqName=(String)ooo; } transation.commit(); } catch(Exception e) {
			 * e.printStackTrace(); } finally {
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return rhqName;
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } return rhqName; }
			 * 
			 * @Override public Map<String,Object> getApprovalData(HashMap<String,Object>
			 * jsonData) { Map<String,Object>mapValue=new HashMap<>(); Criterion cr1=null;
			 * Criterion cr2=null; Criterion cr3=null; Criterion cr4=null; Criterion
			 * cr5=null; Criterion cr51=null; String serviceNo =""; String aliasValue="";
			 * String aliasValuea=""; Integer count =0; String yeadOFMbMe=""; String
			 * yearOfMeMBTo=""; String employeeNAme="";
			 * List<MasMedicalExamReport>listMasMedicalExamReport=null;
			 * List<Object[]>listObject=null;
			 * 
			 * int pageNo = Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
			 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			 * int startPos=(pagingSize * (pageNo - 1)); int limit=pagingSize; String
			 * mestatus=""; Long hospitalId=null;
			 * 
			 * if(jsonData.containsKey("hospitalId") && jsonData.get("hospitalId")!=null ) {
			 * hospitalId = Long.parseLong(jsonData.get("hospitalId").toString());
			 * 
			 * } if(jsonData.containsKey("mestatus") && jsonData.get("mestatus")!=null ) {
			 * mestatus = jsonData.get("mestatus").toString();
			 * mestatus=OpdServiceImpl.getReplaceString(mestatus); }
			 * if(jsonData.containsKey("serviceNo") && jsonData.get("serviceNo")!=null ) {
			 * serviceNo = jsonData.get("serviceNo").toString();
			 * serviceNo=OpdServiceImpl.getReplaceString(serviceNo); }
			 * if(jsonData.containsKey("yearOfMeMB") && jsonData.get("yearOfMeMB")!=null ) {
			 * yeadOFMbMe = jsonData.get("yearOfMeMB").toString();
			 * yeadOFMbMe=OpdServiceImpl.getReplaceString(yeadOFMbMe); }
			 * if(jsonData.containsKey("yearOfMeMBTo") && jsonData.get("yearOfMeMBTo")!=null
			 * ) { yearOfMeMBTo = jsonData.get("yearOfMeMBTo").toString();
			 * yearOfMeMBTo=OpdServiceImpl.getReplaceString(yearOfMeMBTo); }
			 * if(jsonData.containsKey("employeeName") && jsonData.get("employeeName")!=null
			 * ) { employeeNAme = jsonData.get("employeeName").toString();
			 * employeeNAme=OpdServiceImpl.getReplaceString(employeeNAme); } String
			 * ralationNameVal = HMSUtil.getProperties("js_messages_en.properties",
			 * "relationName"); MasRelation
			 * masRelation=checkRelationByName(ralationNameVal.trim());
			 * 
			 * if(StringUtils.isNotEmpty(employeeNAme) &&
			 * StringUtils.isNotEmpty(employeeNAme.trim())) { Long relationId=null;
			 * if(masRelation!=null) relationId=masRelation.getRelationId();
			 * cr51=Restrictions.and( Restrictions.like("patient.employeeName", "%"
			 * +employeeNAme+ "%").ignoreCase(),Restrictions.eq("patient.relationId",
			 * relationId)); }
			 * 
			 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 * 
			 * Criteria criteria=session.createCriteria(MasMedicalExamReport.class,
			 * "masMedicalExamReport").createAlias("masMedicalExamReport.visit", "visit")
			 * .createAlias("visit.masAppointmentType",
			 * "masAppointmentType",JoinType.LEFT_OUTER_JOIN).createAlias("visit.patient",
			 * "patient",JoinType.LEFT_OUTER_JOIN).createAlias("visit.patient.masRank",
			 * "masRank",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.patient.masAdministrativeSex",
			 * "masAdministrativeSex",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.masMedExam",
			 * "masMedExam",JoinType.LEFT_OUTER_JOIN).createAlias(
			 * "masMedicalExamReport.masRank", "mMasRank",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.patient.masMedicalCategory",
			 * "masMedicalCategory",JoinType.LEFT_OUTER_JOIN)
			 * .createAlias("visit.patientMedBoard",
			 * "patientMedBoard",JoinType.LEFT_OUTER_JOIN);
			 * 
			 * try {
			 * 
			 * serviceNo = jsonData.get("serviceNo").toString();
			 * serviceNo=OpdServiceImpl.getReplaceString(serviceNo); String examTypeId =
			 * jsonData.get("examTypeId").toString();
			 * examTypeId=OpdServiceImpl.getReplaceString(examTypeId);
			 * if(StringUtils.isNotEmpty(yeadOFMbMe) &&
			 * StringUtils.isNotEmpty(yearOfMeMBTo)) {
			 * 
			 * Date from = HMSUtil.convertStringTypeDateToDateType(yeadOFMbMe.toString());
			 * 
			 * Date to = HMSUtil.convertStringTypeDateToDateType(yearOfMeMBTo.toString());
			 * Calendar cal = Calendar.getInstance(); cal.set(Calendar.HOUR_OF_DAY, 0);
			 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
			 * cal.set(Calendar.MILLISECOND, 0);
			 * 
			 * 
			 * if(from!=null) { cal.setTime(from); from = cal.getTime(); }
			 * cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE,59);
			 * 
			 * if(to!=null && from!=null) { cal.setTime(to); Date too = cal.getTime(); cr4=
			 * Restrictions.ge("masMedicalExamReport.mediceExamDate",from) ; cr5=
			 * Restrictions.le("masMedicalExamReport.mediceExamDate",too) ; }
			 * 
			 * } if(StringUtils.isNotEmpty(serviceNo) &&
			 * !serviceNo.trim().equalsIgnoreCase("")) { cr2=
			 * Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase(); }
			 * if(StringUtils.isNotEmpty(examTypeId) && !examTypeId.equalsIgnoreCase("0")) {
			 * cr3= Restrictions.eq("visit.examId", Long.parseLong(examTypeId.toString()));
			 * }
			 * 
			 * cr1= Restrictions.and(Restrictions.or(Restrictions.eq(
			 * "masMedicalExamReport.status",
			 * "ac").ignoreCase(),Restrictions.eq("masMedicalExamReport.status",
			 * "af").ignoreCase())
			 * ,Restrictions.isNull("masMedicalExamReport.meApprovalRidcId"),Restrictions.eq
			 * ("visit.hospitalId",hospitalId));
			 * 
			 * 
			 * if(cr1!=null) criteria.add(cr1).add(Restrictions.eq(
			 * "masAppointmentType.appointmentTypeCode", "ME").ignoreCase()); if(cr2!=null)
			 * criteria.add(cr2); if(cr3!=null) criteria.add(cr3); if(cr4!=null)
			 * criteria.add(cr4); if(cr5!=null) criteria.add(cr5); if(cr51!=null)
			 * criteria.add(cr51); ProjectionList projectionList =
			 * Projections.projectionList();
			 * projectionList.add(Projections.property("patient.patientName").as(
			 * "patientName"));
			 * projectionList.add(Projections.property("patient.dateOfBirth").as(
			 * "dateOfBirth")); projectionList.add(Projections.property(
			 * "masAdministrativeSex.administrativeSexName").as("administrativeSexName"));
			 * projectionList.add(Projections.property("masRank.rankName").as("rankName"));
			 * projectionList.add(Projections.property("mMasRank.rankName").as("rankName1"))
			 * ; projectionList.add(Projections.property("masMedExam.medicalExamName").as(
			 * "medicalExamName"));
			 * projectionList.add(Projections.property("masMedExam.medicalExamCode").as(
			 * "medicalExamCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.status").as(
			 * "status"));
			 * projectionList.add(Projections.property("masMedicalExamReport.visitId").as(
			 * "visitId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.patientId").as(
			 * "patientId"));
			 * projectionList.add(Projections.property("patient.serviceNo").as("serviceNo"))
			 * ; projectionList.add(Projections.property("visit.departmentId").as(
			 * "departmentId")); projectionList.add(Projections.property(
			 * "masMedicalExamReport.medicalExaminationId").as("medicalExaminationId"));
			 * projectionList.add(Projections.property("masMedicalExamReport.mediceExamDate"
			 * ).as("mediceExamDate"));
			 * projectionList.add(Projections.property("masMedicalExamReport.ridcId").as(
			 * "ridcId"));
			 * 
			 * projectionList.add(Projections.property("visit.visitFlag").as("visitFlag"));
			 * projectionList.add(Projections.property("masMedicalExamReport.approvedBy").as
			 * ("approvedBy"));
			 * projectionList.add(Projections.property("masMedicalExamReport.dateOfBirth").
			 * as("dateOfBirth1"));
			 * projectionList.add(Projections.property("masMedicalExamReport.daterelease").
			 * as("daterelease")); projectionList.add(Projections.property(
			 * "masAppointmentType.appointmentTypeCode").as("appointmentTypeCode"));
			 * projectionList.add(Projections.property("masMedicalExamReport.apparentAge").
			 * as("apparentAge"));
			 * projectionList.add(Projections.property("visit.meAge").as("meAge"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryId").as("medicalCategoryId"));
			 * projectionList.add(Projections.property(
			 * "masMedicalCategory.medicalCategoryName").as("medicalCategoryName"));
			 * projectionList.add(Projections.property(
			 * "masMedicalExamReport.meApprovalRidcId").as("meApprovalRidcId"));
			 * projectionList.add(Projections.property("patientMedBoard.ridcId").as(
			 * "ridcIdMb")); criteria.setProjection(projectionList);
			 * criteria.addOrder(Order.desc("masMedicalExamReport.mediceExamDate"));
			 * 
			 * listObject=criteria.list(); count = listObject.size();
			 * criteria.setFirstResult((pagingSize) * (pageNo - 1));
			 * criteria.setMaxResults(pagingSize); listObject = criteria.list();
			 * mapValue.put("listVisit", listObject); mapValue.put("count", count);
			 * 
			 * } catch(Exception e) {
			 * 
			 * 
			 * } return mapValue; }
			 * 
			 * 
			 * @Override public Map<String, Object> getTemplateInvestigationoDiver() {
			 * List<OpdTemplateInvestigation> list = null; Long templateId = null; Session
			 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); String
			 * templateCode = HMSUtil.getProperties("adt.properties",
			 * "templateCode").trim(); if(StringUtils.isNotEmpty(templateCode)) {
			 * OpdTemplate opdTemplate = (OpdTemplate)
			 * session.createCriteria(OpdTemplate.class)
			 * .add(Restrictions.eq("templateCode", templateCode)).uniqueResult();
			 * if(opdTemplate!=null) { templateId=opdTemplate.getTemplateId(); } }
			 * Map<String, Object> map = new HashMap<String, Object>(); try {
			 * 
			 * Criteria cr = session.createCriteria(OpdTemplateInvestigation.class)
			 * .add(Restrictions.eq("templateId", templateId)); list = cr.list();
			 * map.put("list", list);
			 * getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } catch (Exception e) { e.printStackTrace(); } return map; }
			 * 
			 * @Override
			 * 
			 * @Transactional public MasMedicalExamReport
			 * getMasMedicalExamReprtByMedicalId(Long medicalId) { MasMedicalExamReport
			 * masMedicalExamReprt=null; try{ Session session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession(); masMedicalExamReprt=
			 * (MasMedicalExamReport) session.createCriteria(MasMedicalExamReport.class)
			 * .add(Restrictions.eq("medicalExaminationId", medicalId)).uniqueResult();
			 * 
			 * } catch(Exception e) { e.printStackTrace();
			 * 
			 * } finally { //getHibernateUtils.getHibernateUtlis().CloseConnection(); }
			 * return masMedicalExamReprt; }
			 * 
			 * @Override public Long saveUpdateMedicalExamApprovalData(MasMedicalExamReport
			 * masMedicalExamReport) { Session session=null; Long medicalExamId=null;
			 * Transaction tx=null; try{ session=
			 * getHibernateUtils.getHibernateUtlis().OpenSession();
			 * tx=session.beginTransaction(); session.saveOrUpdate(masMedicalExamReport);
			 * medicalExamId=masMedicalExamReport.getMedicalExaminationId();
			 * session.flush(); session.clear(); tx.commit(); } catch(Exception e) {
			 * 
			 * 
			 * if (tx != null) { try { tx.rollback(); medicalExamId=0l; } catch(Exception
			 * re) { medicalExamId=0l; re.printStackTrace(); } } e.printStackTrace(); }
			 * finally { getHibernateUtils.getHibernateUtlis().CloseConnection();
			 * 
			 * } return medicalExamId; }
			 */
}
