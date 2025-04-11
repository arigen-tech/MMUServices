package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MedicalBoardDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.PatientDocumentDetailDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasEmployeeCategory;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasInvestignationMapping;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasMedicalExamReport;

import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDiseaseInfo;
import com.mmu.services.entity.PatientMedBoard;
import com.mmu.services.entity.PatientMedBoardChecklist;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.PatientServicesDetail;
import com.mmu.services.entity.ReferralPatientDt;
import com.mmu.services.entity.ReferralPatientHd;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.impl.OpdServiceImpl;
import com.mmu.services.service.impl.PatientRegistrationServiceImpl;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

@Repository
@Transactional
public class MedicalBoardDaoImpl implements MedicalBoardDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String visit = databaseScema + "." + "visit";
	final String MAS_APPOINTMENT_TYPE = databaseScema + "." + "MAS_APPOINTMENT_TYPE";
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	PatientRegistrationServiceImpl patientRegistrationServiceImpl;
	@Autowired
	MedicalExamDAO medicalExamDAO;
	@Autowired
	PatientDocumentDetailDao patientDocumentDetailDao;

	/*
	 * @Override public Map<String, Object>
	 * getValidateMedicalBoardList(HashMap<String, Object> jsonData) {
	 * List<Visit>listVisit=null; Map<String,Object>map=new HashMap<>(); int count =
	 * 0; Criterion cr1=null; Criterion cr3=null; try { int pageNo =
	 * Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria criteria=session.createCriteria(Visit.class).createAlias(
	 * "masAppointmentType", "masAppointmentType").createAlias("patient", "patient")
	 * .add(Restrictions.eq("masAppointmentType.appointmentTypeCode",
	 * "MB").ignoreCase());
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("a")) {
	 * cr1=Restrictions.or(Restrictions.eq("examStatus", "I"),
	 * Restrictions.eq("examStatus", "S"));//Restrictions.eq("examStatus",
	 * "I").ignoreCase(); criteria.add(cr1); } else
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("b")) {
	 * cr1=Restrictions.or(Restrictions.eq("examStatus", "I"),
	 * Restrictions.eq("examStatus", "S"));//Restrictions.eq("examStatus",
	 * "I").ignoreCase(); criteria.add(cr1); } else {
	 * cr1=Restrictions.or(Restrictions.eq("examStatus", "I"),
	 * Restrictions.eq("examStatus", "S"));//Restrictions.eq("examStatus",
	 * "I").ignoreCase(); criteria.add(cr1); } if(jsonData.get("serviceNo")!=null) {
	 * String serviceNo=jsonData.get("serviceNo").toString(); if (serviceNo != null
	 * && !serviceNo.equals("") && !serviceNo.equals("null")) {
	 * cr3=Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase();
	 * criteria.add(cr3); } } listVisit=criteria.list();
	 * 
	 * count = listVisit.size();
	 * 
	 * criteria = criteria.setFirstResult(pagingSize * (pageNo - 1)); criteria =
	 * criteria.setMaxResults(pagingSize); listVisit = criteria.list();
	 * map.put("count", count); map.put("listVisit", listVisit); } catch(Exception
	 * e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return map; }
	 * 
	 * 
	 * @Override
	 * 
	 * public Map<String, Object> getValidateMedicalBoardWaitingList(HashMap<String,
	 * Object> jsonData) { List<Visit>listVisit=null; Map<String,Object>map=new
	 * HashMap<>(); int count = 0;
	 * 
	 * 
	 * Date date = new Date(); Calendar cal = Calendar.getInstance();
	 * cal.setTime(date); cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE,
	 * 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); Date from
	 * = cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23);
	 * cal.set(Calendar.MINUTE,59); Date to = cal.getTime();
	 * 
	 * Criterion cr1=null; Criterion cr3=null; Criterion cr4VisitDate=null; try {
	 * int pageNo = Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria criteria=session.createCriteria(Visit.class).createAlias(
	 * "masAppointmentType", "masAppointmentType").createAlias("patient", "patient")
	 * .add(Restrictions.eq("masAppointmentType.appointmentTypeCode",
	 * "MB").ignoreCase()) .add(Restrictions.ne("visitFlag", "E").ignoreCase());
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("mbPre") &&
	 * jsonData.get("serviceNo").equals("")) {
	 * cr1=Restrictions.or(Restrictions.eq("visitStatus", "w"),
	 * Restrictions.eq("visitStatus", "s")); Criterion
	 * cr2=Restrictions.eq("examStatus", "T").ignoreCase(); cr4VisitDate=
	 * Restrictions.between("visitDate", from, to); LogicalExpression
	 * lg=Restrictions.and(cr1,cr2); criteria.add(lg); criteria.add(cr4VisitDate); }
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("reffer") &&
	 * jsonData.get("serviceNo").equals("")) {
	 * cr1=Restrictions.or(Restrictions.eq("visitStatus", "w"),
	 * Restrictions.eq("visitStatus", "s")); Criterion
	 * cr2=Restrictions.eq("examStatus", "T").ignoreCase(); cr4VisitDate=
	 * Restrictions.between("visitDate", from, to); LogicalExpression
	 * lg=Restrictions.and(cr1,cr2); criteria.add(lg); criteria.add(cr4VisitDate); }
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("tes") &&
	 * jsonData.get("serviceNo").equals("")) { cr1=Restrictions.eq("visitStatus",
	 * "p").ignoreCase(); Criterion cr2=Restrictions.eq("examStatus",
	 * "T").ignoreCase(); cr4VisitDate= Restrictions.between("visitDate", from, to);
	 * LogicalExpression lg=Restrictions.and(cr1,cr2); criteria.add(lg);
	 * criteria.add(cr4VisitDate); }
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("tem")) {
	 * cr1=Restrictions.or(Restrictions.eq("visitStatus", "m").ignoreCase(),
	 * Restrictions.eq("visitStatus",
	 * "c").ignoreCase(),Restrictions.eq("visitStatus", "R").ignoreCase());
	 * Criterion cr2=Restrictions.or(Restrictions.eq("examStatus",
	 * "T").ignoreCase(), Restrictions.eq("examStatus", "F")); LogicalExpression
	 * lg=Restrictions.and(cr1,cr2); criteria.add(lg); }
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("b")) {
	 * cr1=Restrictions.eq("examStatus", "V").ignoreCase(); criteria.add(cr1); }
	 * if(jsonData.get("serviceNo")!=null) { String
	 * serviceNo=jsonData.get("serviceNo").toString(); if (serviceNo != null &&
	 * !serviceNo.equals("") && !serviceNo.equals("null")) {
	 * cr3=Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase();
	 * criteria.add(cr3); }
	 * 
	 * }
	 * 
	 * listVisit=criteria.list();
	 * 
	 * count = listVisit.size(); criteria = criteria.setFirstResult(pagingSize *
	 * (pageNo - 1)); criteria = criteria.setMaxResults(pagingSize); listVisit =
	 * criteria.list(); map.put("count", count); map.put("listVisit", listVisit); }
	 * catch(Exception e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return map; }
	 * 
	 * @Override public String saveOpdInvestigation(DgOrderhd orderhd,
	 * List<DgOrderdt> dgorderdt) { String result = null; Transaction t = null;
	 * Session session = null; try { session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); t =
	 * session.beginTransaction(); Serializable id = session.save(orderhd); for
	 * (DgOrderdt single : dgorderdt) {
	 * single.setOrderhdId(Long.valueOf(id.toString())); session.save(single); }
	 * t.commit(); getHibernateUtils.getHibernateUtlis().CloseConnection(); } catch
	 * (Exception ex) { ex.printStackTrace(); t.rollback(); return "500"; } return
	 * "200"; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public String savePreliminaryMBDetails(HashMap<String, Object>
	 * jsondata) { Date currentDate = ProjectUtils.getCurrentDate(); Session session
	 * = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(OpdPatientDetail.class); Transaction tx =
	 * session.beginTransaction(); Long visitId = Long.parseLong((String)
	 * jsondata.get("visitId")); Calendar calendar = Calendar.getInstance();
	 * java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); //Long
	 * patientCategoryIdVal=null; Long dgOrderHdIdss=null; Long dgOrderDtss=null;
	 * ////////////////////////////////////////Patient MedicalComposite
	 * Data////////////////////////////////////////
	 * ////////////////////////////////////////
	 * if(jsondata.get("medicalCompositeName")!= null &&
	 * jsondata.get("medicalCompositeName")!="") { Long
	 * patientId=Long.parseLong((String) jsondata.get("patientId"));
	 * if(patientId!=null) { Patient pt = (Patient) session.get(Patient.class,
	 * patientId);
	 * pt.setMedicalCategoryId(Long.parseLong(String.valueOf(jsondata.get(
	 * "medicalCompositeName")))); pt.setFitFlag("C");
	 * if(jsondata.get("categoryCompositeDate")!=null &&
	 * jsondata.get("categoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get("categoryCompositeDate")
	 * .toString()); pt.setMedCatDate(md); } session.update(pt); } }
	 * 
	 * ////////////////////////////////////////Investigation
	 * Section////////////////////////////////////////
	 * //////////////////////////////////////// try { if
	 * (jsondata.get("listofInvestigation") != null) { List<HashMap<String, Object>>
	 * listInvestigation = (List<HashMap<String, Object>>) (Object) jsondata
	 * .get("listofInvestigation");
	 * 
	 * if (listInvestigation != null) { for (HashMap<String, Object> map :
	 * listInvestigation) {
	 * 
	 * 
	 * DgOrderhd orderhd = null; Long headerInveId = null; Date orderDate =
	 * HMSUtil.convertStringTypeDateToDateType(map.get("orderDate").toString());
	 * Long visitId11 = (Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * 
	 * if (orderDate != null && visitId11 != null) { orderhd =
	 * getOrderDatebyInvestigation(orderDate, visitId11); } if (orderhd == null) {
	 * orderhd = new DgOrderhd();
	 * orderhd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * orderhd.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get(
	 * "departmentId")))); orderhd.setOrderDate(orderDate);
	 * if(!map.get("otherInvestigation").equals("")) {
	 * orderhd.setOtherInvestigation(String.valueOf(map.get("otherInvestigation")));
	 * }
	 * orderhd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))
	 * ));
	 * orderhd.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"
	 * )))); orderhd.setOrderStatus("P");
	 * orderhd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * orderhd.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * orderhd.setLastChgDate(ourJavaTimestampObject); headerInveId =
	 * Long.parseLong(session.save(orderhd).toString()); } else { headerInveId =
	 * orderhd.getOrderhdId(); } List<HashMap<String, Object>> invList =
	 * (List<HashMap<String, Object>>) (Object) map.get("listofInvestigationDT");
	 * 
	 * for (HashMap<String, Object> invMap : invList) {
	 * if(invMap.get("dgOrderHdIdss")!=null && invMap.get("dgOrderHdIdss")!="") {
	 * dgOrderHdIdss=Long.valueOf(String.valueOf(invMap.get("dgOrderHdIdss"))); }
	 * if(invMap.get("dgOrderfDtIdss")!=null && invMap.get("dgOrderfDtIdss")!="" &&
	 * invMap.get("mbStatus").equals("Saved")) {
	 * dgOrderDtss=Long.valueOf(String.valueOf(invMap.get("dgOrderfDtIdss")));
	 * DgOrderdt dgUpdate =(DgOrderdt) session.get(DgOrderdt.class, dgOrderDtss); if
	 * (invMap.get("investigationId") != null && invMap.get("investigationId") !=
	 * "") { dgUpdate.setInvestigationId(Long.valueOf(invMap.get("investigationId").
	 * toString())); } dgUpdate.setLabMark(String.valueOf(invMap.get("labMark")));
	 * dgUpdate.setUrgent(String.valueOf(invMap.get("urgent"))); if
	 * (invMap.get("orderDate") != null && invMap.get("orderDate") != "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(map.get("orderDate").toString());//
	 * HMSUtil.convertStringDateToUtilDate(invMap.get("orderDate").toString(), //
	 * "yyyy-MM-dd"); dgUpdate.setOrderDate(d); }
	 * dgUpdate.setLastChgDate(ourJavaTimestampObject);
	 * dgUpdate.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))))
	 * ; dgUpdate.setOrderStatus("P"); if (invMap.get("investigationRemarks") !=
	 * null && !invMap.get("investigationId").equals("")) {
	 * dgUpdate.setInvestigationRemarks(invMap.get("investigationRemarks").toString(
	 * )); } dgUpdate.setOrderhdId(dgOrderHdIdss); session.update(dgUpdate); }
	 * 
	 * else { DgOrderdt ob1 = new DgOrderdt(); if (invMap.get("investigationId") !=
	 * null && invMap.get("investigationId") != "") {
	 * ob1.setInvestigationId(Long.valueOf(invMap.get("investigationId").toString())
	 * ); } ob1.setLabMark(String.valueOf(invMap.get("labMark")));
	 * ob1.setUrgent(String.valueOf(invMap.get("urgent"))); if
	 * (invMap.get("orderDate") != null && invMap.get("orderDate") != "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(map.get("orderDate").toString());//
	 * HMSUtil.convertStringDateToUtilDate(invMap.get("orderDate").toString(), //
	 * "yyyy-MM-dd"); ob1.setOrderDate(d); }
	 * ob1.setLastChgDate(ourJavaTimestampObject);
	 * ob1.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * ob1.setOrderStatus("P"); if (invMap.get("investigationRemarks") != null &&
	 * !invMap.get("investigationId").equals("")) {
	 * ob1.setInvestigationRemarks(invMap.get("investigationRemarks").toString()); }
	 * ob1.setOrderhdId(headerInveId); session.save(ob1); } } }
	 * 
	 * } } //////////////////////////////////////// List of Medical
	 * Category////////////////////////////////////////
	 * //////////////////////////////////////// if
	 * (jsondata.get("listOfMedicalCategory") != null) { List<HashMap<String,
	 * Object>> listMedicalCategory = (List<HashMap<String, Object>>) (Object)
	 * jsondata .get("listOfMedicalCategory");
	 * 
	 * if (listMedicalCategory != null) { for (HashMap<String, Object> mapMedical :
	 * listMedicalCategory) { Long patientCategoryIdVal=null;
	 * if(!mapMedical.get("patientMedicalCatId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedical.get(
	 * "patientMedicalCatId"))); }else { patientCategoryIdVal= null; }
	 * if(patientCategoryIdVal!=null && !mapMedical.get("mbStatus").equals("Saved"))
	 * { PatientMedicalCat pmtUpdate =(PatientMedicalCat)
	 * session.get(PatientMedicalCat.class, patientCategoryIdVal);
	 * pmtUpdate.setMbStatus("C"); session.update(pmtUpdate); PatientMedicalCat pmt
	 * = new PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmt.setLastChgDate(ourJavaTimestampObject);
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * pmt.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory"))));
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("P"); //pmt.setLastChgDate(ourJavaTimestampObject); if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate")
	 * != null && mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("nextCategoryDate").
	 * toString()); pmt.setNextCategoryDate(d1); }
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * pmt.setLastChgDate(ourJavaTimestampObject); if
	 * (mapMedical.get("dateOfOrigin") != null && mapMedical.get("dateOfOrigin") !=
	 * "") { Date dOrgin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmt.setDateOfOrigin(dOrgin); }
	 * pmt.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * session.save(pmt); } if(patientCategoryIdVal!=null &&
	 * !patientCategoryIdVal.equals("") &&
	 * mapMedical.get("mbStatus").equals("Saved")) { PatientMedicalCat pmtUpdate
	 * =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal);
	 * pmtUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); pmtUpdate.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmtUpdate.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"
	 * ))));
	 * pmtUpdate.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); if(mapMedical.get("categoryType")=="T") {
	 * pmtUpdate.setCategoryType("T"); } else
	 * if(mapMedical.get("categoryType")=="P") { pmtUpdate.setCategoryType("P"); }
	 * pmtUpdate.setMbStatus("P"); if (mapMedical.get("categoryDate") != null &&
	 * !mapMedical.get("categoryDate").equals("")) { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmtUpdate.setCategoryDate(d); } if
	 * (mapMedical.get("nextCategoryDate") != null &&
	 * !mapMedical.get("nextCategoryDate").equals("")) { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("nextCategoryDate").
	 * toString()); pmtUpdate.setNextCategoryDate(d1); }
	 * pmtUpdate.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"
	 * )))); if (mapMedical.get("dateOfOrigin") != null &&
	 * !mapMedical.get("dateOfOrigin").equals("")) { Date dateOfOrigin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmtUpdate.setCategoryDate(dateOfOrigin); }
	 * if(mapMedical.get("placeOfOrigin")!=null &&
	 * !mapMedical.get("placeOfOrigin").equals("")) {
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * } pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * session.update(pmtUpdate); } if(patientCategoryIdVal == null) {
	 * PatientMedicalCat pmt = new PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmt.setLastChgDate(ourJavaTimestampObject);
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * pmt.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory"))));
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("P"); //pmt.setLastChgDate(ourJavaTimestampObject); if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate")
	 * != null && mapMedical.get("nextCategoryDate") != "") { Date d1 = HMSUtil
	 * .convertStringTypeDateToDateType(mapMedical.get("nextCategoryDate").toString(
	 * )); pmt.setNextCategoryDate(d1); }
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * pmt.setLastChgDate(ourJavaTimestampObject); if
	 * (mapMedical.get("dateOfOrigin") != null && mapMedical.get("dateOfOrigin") !=
	 * "") { Date dOrgin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmt.setDateOfOrigin(dOrgin); }
	 * pmt.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * session.save(pmt);
	 * 
	 * } }
	 * 
	 * } } if(jsondata.get("medicalCompositeName")!= null &&
	 * jsondata.get("medicalCompositeName")!="") { Long compositeCategoryIdVal=null;
	 * if(!jsondata.get("compositeCategoryIdVal").equals("")) {
	 * compositeCategoryIdVal=Long.valueOf(String.valueOf(jsondata.get(
	 * "compositeCategoryIdVal"))); }else { compositeCategoryIdVal= null; }
	 * if(compositeCategoryIdVal!=null) { PatientMedicalCat pt = (PatientMedicalCat)
	 * session.get(PatientMedicalCat.class, compositeCategoryIdVal);
	 * pt.setpMedCatId(Long.parseLong(String.valueOf(jsondata.get(
	 * "medicalCompositeName")))); if(jsondata.get("categoryCompositeDate")!=null &&
	 * jsondata.get("categoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get("categoryCompositeDate")
	 * .toString()); pt.setpMedCatDate(md); }
	 * pt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * session.update(pt); }
	 * 
	 * else { PatientMedicalCat pmt = new PatientMedicalCat();
	 * pmt.setpMedCatId(Long.parseLong(String.valueOf(jsondata.get(
	 * "medicalCompositeName")))); if(jsondata.get("categoryCompositeDate")!=null &&
	 * jsondata.get("categoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get("categoryCompositeDate")
	 * .toString()); pmt.setpMedCatDate(md); }
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * session.save(pmt); } } if(jsondata.containsKey("saveEnable")) { if (visitId
	 * != null) { Visit visit = (Visit) session.get(Visit.class, visitId); if (visit
	 * != null) { visit.setExamStatus("V"); session.update(visit); } } }
	 * if(jsondata.containsKey("saveDisable")) { if (visitId != null) { Visit visit
	 * = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setExamStatus("S"); session.update(visit); } } }
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); tx.rollback();
	 * System.out.println("Exception Message Print ::" + ex.toString()); return
	 * ex.toString(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * "Successfully saved"; }
	 * 
	 * public DgOrderhd getOrderDatebyInvestigation(Date orderdate,Long visitId) {
	 * Session session =null; DgOrderhd investigationHeaderHd=null; try {
	 * session=getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(DgOrderhd.class) .add(Restrictions.eq("orderDate",
	 * orderdate)).add(Restrictions.eq("visitId", visitId));
	 * List<DgOrderhd>listInvHd=cr.list(); if(CollectionUtils.isNotEmpty(listInvHd))
	 * { investigationHeaderHd=listInvHd.get(0); } } catch(Exception e) {
	 * e.printStackTrace(); } return investigationHeaderHd; }
	 * 
	 * @Override public List<PatientMedicalCat> getPatientMedicalBoardDetails(Long
	 * visitId) { Criteria cr = null; List<PatientMedicalCat> list = null; int count
	 * = 0; Map<String, Object> map = new HashMap<String, Object>(); Session session
	 * = getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(PatientMedicalCat.class).add(Restrictions.eq(
	 * "visitId", visitId)); cr.addOrder(Order.asc("medicalCatId")); list =
	 * cr.list(); count = list.size(); System.out.println(count);
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * @Override public Map<String, Object>
	 * getPatientMedicalBoardDetailsDgOrder(HashMap<String, Object> jsonData) {
	 * List<DgOrderdt> list = null; long visitId = Long.parseLong((String)
	 * jsonData.get("visitId")); //long patientId = Long.parseLong((String)
	 * jsonData.get("patientId")); Map<String, Object> map = new HashMap<String,
	 * Object>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(DgOrderdt.class) .createAlias("dgOrderHd",
	 * "hd").createAlias("hd.visit", "v") .add(Restrictions.eq("v.visitId",
	 * visitId)); // .add(Restrictions.eq("v.patientId", patientId));
	 * 
	 * list = cr.list(); map.put("list", list);
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return map; }
	 * 
	 * @Override public String opdVitalDetails(OpdPatientDetail ob,HashMap<String,
	 * Object> getjsondata) {
	 * 
	 * JSONArray jarr=new JSONArray(getjsondata.get("jsondata").toString());
	 * JSONObject jsondata = (JSONObject) jarr.get(0);
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(OpdPatientDetail.class);
	 * cr.add(Restrictions.eq("visitId", ob.getVisitId())); OpdPatientDetail list =
	 * (OpdPatientDetail) cr.uniqueResult(); Transaction t =
	 * session.beginTransaction(); String Result = null; Long visitId =
	 * Long.parseLong((String) jsondata.get("visitId")); Long patientId=null; Long
	 * hospitalId = null; Calendar calendar = Calendar.getInstance();
	 * java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); Date currentDate =
	 * ProjectUtils.getCurrentDate(); Timestamp ts =null;
	 * 
	 * String
	 * dynamicUploadInvesIdAndfile=getjsondata.get("dynamicUploadInvesIdAndfileNew")
	 * .toString(); dynamicUploadInvesIdAndfile=OpdServiceImpl.getReplaceString(
	 * dynamicUploadInvesIdAndfile);
	 * dynamicUploadInvesIdAndfile=dynamicUploadInvesIdAndfile.replaceAll("\"", "");
	 * String[] dynamicUploadInvesIdAndfiles =
	 * dynamicUploadInvesIdAndfile.split(",");
	 * 
	 * String
	 * investigationIdValue=getjsondata.get("investigationIdValue").toString();
	 * investigationIdValue=OpdServiceImpl.getReplaceString(investigationIdValue);
	 * String[] investigationIdValues = investigationIdValue.split(",");
	 * 
	 * String totalLengthDigiFile=getjsondata.get("totalLengthDigiFile").toString();
	 * totalLengthDigiFile=OpdServiceImpl.getReplaceString(totalLengthDigiFile);
	 * 
	 * String rmdIdVal=patientRegistrationServiceImpl.getFinalInvestigationValue(
	 * investigationIdValues, dynamicUploadInvesIdAndfiles,totalLengthDigiFile) ;
	 * 
	 * String[] rangeValues=null; if(!getjsondata.get("range").equals("")) { String
	 * rangeValue=getjsondata.get("range").toString();
	 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
	 * rangeValue.split(","); }
	 * 
	 * 
	 * String[] rmsIdFinalValfileUp=null; rmsIdFinalValfileUp = rmdIdVal.split(",");
	 * 
	 * 
	 * String meRmsId=getjsondata.get("meRmsId").toString();
	 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
	 * meRmsId.split(",");
	 * 
	 * String patientId1 = getjsondata.get("PatientID12").toString();
	 * if(StringUtils.isNotEmpty(patientId1)) { patientId1 =
	 * OpdServiceImpl.getReplaceString(patientId1); //
	 * masMedicalExamReprt.setPatientId(Long.parseLong(patientId)); }
	 * 
	 * String hospitalId1 = getjsondata.get("hospitalId").toString();
	 * if(StringUtils.isNotEmpty(hospitalId1)) { hospitalId1 =
	 * OpdServiceImpl.getReplaceString(hospitalId1);
	 * //masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId)); } String
	 * userId1 = getjsondata.get("userId").toString(); userId1 =
	 * OpdServiceImpl.getReplaceString(userId1);
	 * if(jsondata.has("dateOfEnrollment")) { String
	 * dateOfMB=jsondata.get("dateOfEnrollment").toString();
	 * if(StringUtils.isNotEmpty(dateOfMB) && dateOfMB!=null) { Date
	 * medicalCompositeDateValue = null; medicalCompositeDateValue =
	 * HMSUtil.convertStringTypeDateToDateType(dateOfMB); ts = new
	 * Timestamp(medicalCompositeDateValue.getTime()); } } else { ts =
	 * ourJavaTimestampObject; }
	 * 
	 * MasMedicalExamReport masMedicalExamReprt=new MasMedicalExamReport();
	 * //medicalExamDAO.updateInvestigationDgResultForDigiUpload(getjsondata,
	 * patientId1, hospitalId1, userId1,
	 * masMedicalExamReprt,rangeValues,rmsIdFinalValfileUp,ts);
	 * medicalExamDAO.updateInvestigationDgResult(getjsondata,
	 * patientId1.toString(), hospitalId1.toString(), userId1, masMedicalExamReprt);
	 * 
	 * try {
	 * 
	 * // Session session=sessionFactory.getCurrentSession();
	 * 
	 * if (list != null) { list.setPollor(ob.getPollor());
	 * list.setEdema(ob.getEdema()); list.setCyanosis(ob.getCyanosis());
	 * list.setIcterus(ob.getIcterus()); list.setLymphNode(ob.getLymphNode());
	 * list.setClubbing(ob.getClubbing()); list.setGcs(ob.getGcs());
	 * list.setTremors(ob.getTremors()); list.setGeneralOther(ob.getGeneralOther());
	 * // list.setGeneral(ob.getGeneral()); list.setCns(ob.getCns());
	 * list.setChestResp(ob.getChestResp());
	 * list.setMusculoskeletal(ob.getMusculoskeletal()); list.setCvs(ob.getCvs());
	 * list.setSkin(ob.getSkin()); list.setGi(ob.getGi());
	 * list.setSystemOther(ob.getSystemOther());
	 * list.setGenitoUrinary(ob.getGenitoUrinary()); list.setHeight(ob.getHeight());
	 * list.setWeight(ob.getWeight()); list.setIdealWeight(ob.getIdealWeight());
	 * list.setVaration(ob.getVaration()); list.setTemperature(ob.getTemperature());
	 * list.setBpDiastolic(ob.getBpDiastolic());
	 * list.setBpSystolic(ob.getBpSystolic()); list.setPulse(ob.getPulse());
	 * list.setSpo2(ob.getSpo2()); list.setBmi(ob.getBmi()); list.setRr(ob.getRr());
	 * list.setLastChgDate(ourJavaTimestampObject);
	 * //list.setLastChgBy(ob.getLastChgBy());
	 * 
	 * session.update(list); visitId = ob.getVisitId(); if (visitId != null) { Visit
	 * visit = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("s"); session.update(visit); } }
	 * 
	 * 
	 * 
	 * t.commit(); Result = "200";
	 * 
	 * } else { session.save(ob); visitId = ob.getVisitId(); if (visitId != null) {
	 * Visit visit = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("s"); session.update(visit); } } /////////////
	 * Investigation Result Details Section ////////////////////////////////
	 * t.commit(); Result = "200"; } } catch (Exception e) { t.rollback();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); Result =
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return Result; }
	 * 
	 * 
	 * @Override public String saveReferforOpinionMBDetails(HashMap<String, Object>
	 * getJsondata) {
	 * 
	 * JSONArray jarr=new JSONArray(getJsondata.get("jsondata").toString());
	 * JSONObject jsondata = (JSONObject) jarr.get(0);
	 * 
	 * Date currentDate = ProjectUtils.getCurrentDate(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(OpdPatientDetail.class); Criteria cr1 =
	 * session.createCriteria(MasMedicalExamReport.class); Transaction tx =
	 * session.beginTransaction(); Long visitId = Long.parseLong((String)
	 * jsondata.get("visitId")); Long patientId; Long hospitalId; Long userId; Long
	 * opdId; Long headerNivId = null; Calendar calendar = Calendar.getInstance();
	 * java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); String procedureType =
	 * null;
	 * 
	 * MasMedicalExamReport masMedicalExamReprt=new MasMedicalExamReport();
	 * //medicalExamDAO.updateInvestigationDgResultForDigiUpload(getjsondata,
	 * patientId1, hospitalId1, userId1,
	 * masMedicalExamReprt,rangeValues,rmsIdFinalValfileUp,ts);
	 * medicalExamDAO.updateInvestigationDgResult(getJsondata,
	 * jsondata.get("patientId").toString(), jsondata.get("hospitalId").toString(),
	 * jsondata.get("userId").toString(), masMedicalExamReprt);
	 * 
	 * cr.add(Restrictions.eq("visitId", visitId));
	 * cr1.add(Restrictions.eq("visitId", visitId)); OpdPatientDetail opdDetails =
	 * (OpdPatientDetail) cr.uniqueResult(); MasMedicalExamReport
	 * mmer=(MasMedicalExamReport) cr1.uniqueResult(); try {
	 * 
	 * if (opdDetails != null) {
	 * opdDetails.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * opdDetails.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId")))
	 * ); opdDetails.setOpdDate(ourJavaTimestampObject);
	 * if(jsondata.has("icdDiagnosis")) {
	 * opdDetails.setIcdDiagnosis(String.valueOf(jsondata.get("icdDiagnosis"))); }
	 * opdDetails.setHeight(jsondata.get("height").toString());
	 * opdDetails.setWeight(jsondata.get("weight").toString());
	 * opdDetails.setIdealWeight(jsondata.get("idealWeight").toString()); if
	 * (jsondata.get("varation") != null && !jsondata.get("varation").equals("")) {
	 * Double bd = new Double(jsondata.get("varation").toString());
	 * opdDetails.setVaration(bd); }
	 * opdDetails.setTemperature(jsondata.get("temperature").toString());
	 * opdDetails.setBpSystolic(jsondata.get("bp").toString());
	 * opdDetails.setBpDiastolic(jsondata.get("bp1").toString());
	 * opdDetails.setPulse(jsondata.get("pulse").toString());
	 * opdDetails.setSpo2(jsondata.get("spo2").toString());
	 * opdDetails.setBmi(jsondata.get("bmi").toString());
	 * opdDetails.setRr(jsondata.get("rr").toString());
	 * opdDetails.setPollor(String.valueOf(jsondata.get("pallar")));
	 * opdDetails.setEdema(String.valueOf(jsondata.get("edema")));
	 * opdDetails.setCyanosis(String.valueOf(jsondata.get("cyanosis")));
	 * opdDetails.setIcterus(String.valueOf(jsondata.get("icterus")));
	 * opdDetails.setHairNail(String.valueOf(jsondata.get("hairnail")));
	 * opdDetails.setLymphNode(String.valueOf(jsondata.get("lymphNode")));
	 * opdDetails.setClubbing(String.valueOf(jsondata.get("clubbing")));
	 * opdDetails.setGcs(String.valueOf(jsondata.get("gcs")));
	 * opdDetails.setTremors(String.valueOf(jsondata.get("tremors")));
	 * opdDetails.setGeneralOther(String.valueOf(jsondata.get("generalOther")));
	 * opdDetails.setCns(String.valueOf(jsondata.get("cns")));
	 * opdDetails.setChestResp(String.valueOf(jsondata.get("chestresp")));
	 * opdDetails.setMusculoskeletal(String.valueOf(jsondata.get("musculoskeletal"))
	 * ); opdDetails.setCvs(String.valueOf(jsondata.get("cvs")));
	 * opdDetails.setSkin(String.valueOf(jsondata.get("skin")));
	 * opdDetails.setGi(String.valueOf(jsondata.get("gi")));
	 * opdDetails.setSystemOther(String.valueOf(jsondata.get("systemother")));
	 * opdDetails.setLastChgDate(ourJavaTimestampObject);
	 * opdDetails.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * opdDetails.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId"))));
	 * opdDetails.setGenitoUrinary(String.valueOf(jsondata.get("geneticurinary")));
	 * session.update(opdDetails);
	 * if(!jsondata.get("obsistyCheckAlready").equals("exits")) {
	 * if(jsondata.get("obsistyMark")!=null &&
	 * !jsondata.get("obsistyMark").equals("")) {
	 * 
	 * try { //Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); //Criteria cr =
	 * session.createCriteria(OpdObesityHd.class); //t = session.beginTransaction();
	 * 
	 * Criteria cr2 = session.createCriteria(OpdObesityHd.class);
	 * cr2.add(Restrictions.eq("visitId", visitId)); OpdObesityHd listOpdObsisty =
	 * (OpdObesityHd) cr2.uniqueResult(); if (listOpdObsisty != null) {
	 * listOpdObsisty.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId")))); if (jsondata.get("varation") != null &&
	 * !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation")));
	 * listOpdObsisty.setVaration(bd); }
	 * 
	 * listOpdObsisty.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId"))));
	 * listOpdObsisty.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId").toString())));
	 * listOpdObsisty.setLastChgDate(ourJavaTimestampObject);
	 * listOpdObsisty.setIniDate(currentDate); session.update(listOpdObsisty);
	 * 
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
	 * oohd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * oohd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * oohd.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")))
	 * ); oohd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * oohd.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * oohd.setOverweightFlag(String.valueOf(jsondata.get("obsistyMark")));
	 * oohd.setLastChgDate(ourJavaTimestampObject); if (jsondata.get("varation") !=
	 * null && !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation"))); oohd.setVaration(bd); }
	 * oohd.setIniDate(currentDate); oohd.setLastChgDate(ourJavaTimestampObject);
	 * long obesistyId = Long.parseLong(session.save(oohd).toString());
	 * System.out.println(obesistyId); OpdObesityDt oodt=new OpdObesityDt(); if
	 * (jsondata.get("bmi") != null && !jsondata.get("bmi").equals("")) { BigDecimal
	 * bmi = new BigDecimal(String.valueOf(jsondata.get("bmi"))); oodt.setBmi(bmi);
	 * } oodt.setObesityDate(currentDate); if (jsondata.get("height") != null &&
	 * !jsondata.get("height").equals("")) { BigDecimal height = new
	 * BigDecimal(String.valueOf(jsondata.get("height"))); oodt.setHeight(height); }
	 * if (jsondata.get("weight") != null && !jsondata.get("weight").equals("")) {
	 * BigDecimal weight = new BigDecimal(String.valueOf(jsondata.get("weight")));
	 * oodt.setWeight(weight); } if (jsondata.get("idealWeight") != null &&
	 * !jsondata.get("idealWeight").equals("")) { BigDecimal idealWeight = new
	 * BigDecimal(String.valueOf(jsondata.get("idealWeight")));
	 * oodt.setIdealWeight(idealWeight); } if (jsondata.get("varation") != null &&
	 * !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation"))); oodt.setVariation(bd);
	 * } oodt.setMonth(m); oodt.setObesityHdId(obesistyId); session.save(oodt);
	 * 
	 * //opdObsisty(jsondata); } }catch (Exception e) { e.printStackTrace(); } } }
	 * opdId = opdDetails.getOpdPatientDetailsId();
	 * if(jsondata.has("specialistopinion")) {
	 * if(jsondata.get("specialistopinion")!="" &&
	 * jsondata.get("specialistopinion")!=null) { if (visitId != null) { Visit visit
	 * = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("m"); session.update(visit); } } } } else { if (visitId
	 * != null) { Visit visit = (Visit) session.get(Visit.class, visitId); if (visit
	 * != null) { visit.setVisitStatus("p"); session.update(visit); } } }
	 * 
	 * /////////////////////opd patient history /////////////////////////////
	 * OpdPatientHistory opdPatinetHistory = new OpdPatientHistory();
	 * opdPatinetHistory.setVisitId(Long.parseLong(jsondata.get("visitId").toString(
	 * ))); opdPatinetHistory.setPatientId(Long.parseLong(jsondata.get("patientId").
	 * toString()));
	 * opdPatinetHistory.setHospitalId(Long.parseLong(jsondata.get("hospitalId").
	 * toString()));
	 * opdPatinetHistory.setLastChgBy(Long.parseLong(jsondata.get("userId").toString
	 * ())); opdPatinetHistory.setChiefComplain(String.valueOf(jsondata.get(
	 * "chiefComplain"))); opdPatinetHistory.setLastChgDate(ourJavaTimestampObject);
	 * session.save(opdPatinetHistory);
	 * 
	 * //////////////////////MasMedicalExamReport ////////////////////////////// if
	 * (mmer != null) {
	 * mmer.setFinalobservation(String.valueOf(jsondata.get("finalobservation")));
	 * mmer.setVisitId(Long.parseLong(jsondata.get("visitId").toString()));
	 * mmer.setPatientId(Long.parseLong(jsondata.get("patientId").toString()));
	 * mmer.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * mmer.setLastChgDate(ourJavaTimestampObject); session.update(mmer); } else {
	 * MasMedicalExamReport masEReports = new MasMedicalExamReport();
	 * masEReports.setVisitId(Long.parseLong(jsondata.get("visitId").toString()));
	 * masEReports.setPatientId(Long.parseLong(jsondata.get("patientId").toString())
	 * ); masEReports.setFinalobservation(String.valueOf(jsondata.get(
	 * "finalobservation")));
	 * masEReports.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * masEReports.setLastChgDate(ourJavaTimestampObject);
	 * session.save(masEReports); } ///////////// Referal Details Section
	 * //////////////////////////////// if (jsondata.get("listofReferallHD") !=
	 * null) { //List<HashMap<String, Object>> list = (List<HashMap<String,
	 * Object>>) (Object) jsondata // .get("listofReferallHD"); JSONArray list=new
	 * JSONArray(jsondata.get("listofReferallHD").toString());
	 * System.out.println(list.toString()); if (list != null) { for(int
	 * i=0;i<list.length();i++) { JSONObject map = list.getJSONObject(i); String
	 * extId = (String) map.get("extHospitalId"); if(extId!=null && extId!="") {
	 * Long empID = Long.parseLong(extId); String referralNote = (String)
	 * map.get("referralNote"); patientId = Long.parseLong((String)
	 * map.get("patientId")); hospitalId =
	 * Long.parseLong(String.valueOf(map.get("hospitalId"))); // String
	 * treatmentType=(String)map.get("treatmentType"); ReferralPatientHd header =
	 * null; if (empID != null && opdId != null) { header =
	 * getReferralPatientHdByExeHosAndOpdPd(empID, opdId); } Long id = null; if
	 * (header == null) {
	 * 
	 * header = new ReferralPatientHd(); header.setPatientId(patientId);
	 * header.setHospitalId(hospitalId); header.setExtHospitalId(empID);
	 * header.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * header.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * header.setLastChgDate(ourJavaTimestampObject);
	 * header.setReferralIniDate(currentDate); header.setTreatmentType("E");
	 * header.setReferralIniDate(currentDate);
	 * header.setReferralNote(String.valueOf(referralNote)); header.setStatus("W");
	 * header.setOpdPatientDetailsId(opdId); id =
	 * Long.parseLong(session.save(header).toString()); } else { id =
	 * header.getRefrealHdId(); } Long extHosId = header.getExtHospitalId();
	 * System.out.println(extHosId); //List<HashMap<String, Object>> diagnosisList =
	 * (List<HashMap<String, Object>>) (Object) map.get("listofReferalDT");
	 * JSONArray diagnosisList=new JSONArray(map.get("listofReferalDT").toString());
	 * //for (HashMap<String, Object> diagnosisMap : diagnosisList) { for(int
	 * j=0;j<diagnosisList.length();j++) { JSONObject diagnosisMap =
	 * diagnosisList.getJSONObject(j); if(diagnosisMap.get("diagnosisId")!=null &&
	 * diagnosisMap.get("diagnosisId")!="") { Long diagnosisId =
	 * Long.parseLong((String) diagnosisMap.get("diagnosisId")); ReferralPatientDt
	 * refDt = new ReferralPatientDt(); refDt.setDiagnosisId(diagnosisId);
	 * refDt.setLastChgDate(ourJavaTimestampObject);
	 * refDt.setExtDepartment(String.valueOf(diagnosisMap.get("extDepartment")));
	 * refDt.setInstruction(String.valueOf(diagnosisMap.get("instruction")));
	 * refDt.setRefrealHdId(id); session.save(refDt); } } } }
	 * 
	 * } }
	 * 
	 * ///////////// Investigation Result Details Section
	 * //////////////////////////////// if (jsondata.get("listofResultDetails") !=
	 * null) { List<HashMap<String, Object>> listOfResult = (List<HashMap<String,
	 * Object>>) (Object) jsondata.get("listofResultDetails"); Long headerResultId =
	 * null;
	 * 
	 * DgResultEntryHd header = null; Long visitIdCheck =
	 * Long.parseLong(String.valueOf(jsondata.get("visitId")));
	 * 
	 * if (visitIdCheck != null) { header = getResultByVisitId(visitIdCheck); }
	 * if(header==null) { if (listOfResult != null && !listOfResult.isEmpty()) {
	 * DgResultEntryHd pphd = new DgResultEntryHd();
	 * pphd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pphd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pphd.setLastChgDate(ourJavaTimestampObject);
	 * pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * pphd.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")))
	 * ); //pphd.setOrderHdId(listOfResult.get(arg0)); headerResultId =
	 * Long.parseLong(session.save(pphd).toString()); //
	 * pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * List<DgResultEntryDt> patientReultDT = new ArrayList<>(); for
	 * (HashMap<String, Object> singleopd : listOfResult) { DgResultEntryDt ppdt1 =
	 * new DgResultEntryDt(); if (singleopd.get("investigationId") != null &&
	 * singleopd.get("investigationId") != "") {
	 * ppdt1.setInvestigationId(Long.valueOf(singleopd.get("investigationId").
	 * toString())); } if (singleopd.get("orderDtId") != null &&
	 * singleopd.get("orderDtId") != "") {
	 * ppdt1.setOrderDtId(Long.valueOf(singleopd.get("orderDtId").toString())); } if
	 * (singleopd.get("result") != null && singleopd.get("result").toString() != "")
	 * { ppdt1.setResult(String.valueOf(singleopd.get("result"))); }
	 * //ppdt1.setResultType("P"); ppdt1.setResultEntryId(headerResultId);
	 * session.save(ppdt1); }
	 * 
	 * } } else { for (HashMap<String, Object> singleopd : listOfResult) {
	 * //DgResultEntryDt ppdt1 = new DgResultEntryDt(); Long
	 * DgResultId=Long.valueOf(String.valueOf(singleopd.get("dgResultId")));
	 * 
	 * DgResultEntryDt ppdt1 = (DgResultEntryDt) session.get(DgResultEntryDt.class,
	 * DgResultId);
	 * 
	 * if (singleopd.get("investigationId") != null &&
	 * singleopd.get("investigationId") != "") {
	 * ppdt1.setInvestigationId(Long.valueOf(singleopd.get("investigationId").
	 * toString())); } if (singleopd.get("orderDtId") != null &&
	 * singleopd.get("orderDtId") != "") {
	 * ppdt1.setOrderDtId(Long.valueOf(singleopd.get("orderDtId").toString())); } if
	 * (singleopd.get("result") != null && singleopd.get("result").toString() != "")
	 * { ppdt1.setResult(String.valueOf(singleopd.get("result"))); }
	 * session.update(ppdt1); } } }
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * OpdPatientDetail masExamReports = new OpdPatientDetail();
	 * masExamReports.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReports.setVisitId(Long.parseLong(String.valueOf(jsondata.get(
	 * "visitId")))); masExamReports.setHeight(jsondata.get("height").toString());
	 * masExamReports.setWeight(jsondata.get("weight").toString());
	 * masExamReports.setIdealWeight(jsondata.get("idealWeight").toString()); if
	 * (jsondata.get("varation") != null && !jsondata.get("varation").equals("")) {
	 * Double bd = new Double(jsondata.get("varation").toString());
	 * masExamReports.setVaration(bd); }
	 * masExamReports.setTemperature(jsondata.get("temperature").toString());
	 * masExamReports.setBpSystolic(jsondata.get("bp").toString());
	 * masExamReports.setBpDiastolic(jsondata.get("bp1").toString());
	 * masExamReports.setPulse(jsondata.get("pulse").toString());
	 * masExamReports.setSpo2(jsondata.get("spo2").toString());
	 * masExamReports.setBmi(jsondata.get("bmi").toString());
	 * masExamReports.setRr(jsondata.get("rr").toString());
	 * masExamReports.setPollor(String.valueOf(jsondata.get("pallar")));
	 * masExamReports.setEdema(String.valueOf(jsondata.get("edema")));
	 * masExamReports.setCyanosis(String.valueOf(jsondata.get("cyanosis")));
	 * masExamReports.setIcterus(String.valueOf(jsondata.get("icterus")));
	 * masExamReports.setHairNail(String.valueOf(jsondata.get("hairnail")));
	 * masExamReports.setLymphNode(String.valueOf(jsondata.get("lymphNode")));
	 * masExamReports.setClubbing(String.valueOf(jsondata.get("clubbing")));
	 * masExamReports.setGcs(String.valueOf(jsondata.get("gcs")));
	 * masExamReports.setTremors(String.valueOf(jsondata.get("tremors")));
	 * masExamReports.setGeneralOther(String.valueOf(jsondata.get("generalOther")));
	 * masExamReports.setCns(String.valueOf(jsondata.get("cns")));
	 * masExamReports.setChestResp(String.valueOf(jsondata.get("chestresp")));
	 * masExamReports.setMusculoskeletal(String.valueOf(jsondata.get(
	 * "musculoskeletal")));
	 * masExamReports.setCvs(String.valueOf(jsondata.get("cvs")));
	 * masExamReports.setSkin(String.valueOf(jsondata.get("skin")));
	 * masExamReports.setGi(String.valueOf(jsondata.get("gi")));
	 * masExamReports.setSystemOther(String.valueOf(jsondata.get("systemother")));
	 * masExamReports.setLastChgDate(ourJavaTimestampObject);
	 * masExamReports.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId").toString())));
	 * masExamReports.setGenitoUrinary(String.valueOf(jsondata.get("geneticurinary")
	 * )); opdId = Long.parseLong(session.save(masExamReports).toString());
	 * if(jsondata.get("specialistopinion")!="" &&
	 * jsondata.get("specialistopinion")!=null) { if (visitId != null) { Visit visit
	 * = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("m"); session.update(visit); } } } else { if (visitId !=
	 * null) { Visit visit = (Visit) session.get(Visit.class, visitId); if (visit !=
	 * null) { visit.setVisitStatus("p"); session.update(visit); } } }
	 * if(!jsondata.get("obsistyCheckAlready").equals("exits")) {
	 * if(jsondata.get("obsistyMark")!=null &&
	 * !jsondata.get("obsistyMark").equals("")) {
	 * 
	 * try { //Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); //Criteria cr =
	 * session.createCriteria(OpdObesityHd.class); //t = session.beginTransaction();
	 * 
	 * Criteria cr2 = session.createCriteria(OpdObesityHd.class);
	 * cr2.add(Restrictions.eq("visitId", visitId)); OpdObesityHd listOpdObsisty =
	 * (OpdObesityHd) cr2.uniqueResult(); if (listOpdObsisty != null) {
	 * listOpdObsisty.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId")))); if (jsondata.get("varation") != null &&
	 * !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation")));
	 * listOpdObsisty.setVaration(bd); }
	 * 
	 * listOpdObsisty.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId"))));
	 * listOpdObsisty.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId").toString())));
	 * listOpdObsisty.setLastChgDate(ourJavaTimestampObject);
	 * listOpdObsisty.setIniDate(currentDate); session.update(listOpdObsisty);
	 * 
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
	 * oohd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * oohd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * oohd.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")))
	 * ); oohd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * oohd.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * oohd.setOverweightFlag(String.valueOf(jsondata.get("obsistyMark")));
	 * oohd.setLastChgDate(ourJavaTimestampObject); if (jsondata.get("varation") !=
	 * null && !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation"))); oohd.setVaration(bd); }
	 * oohd.setIniDate(currentDate); oohd.setLastChgDate(ourJavaTimestampObject);
	 * long obesistyId = Long.parseLong(session.save(oohd).toString());
	 * System.out.println(obesistyId); OpdObesityDt oodt=new OpdObesityDt(); if
	 * (jsondata.get("bmi") != null && !jsondata.get("bmi").equals("")) { BigDecimal
	 * bmi = new BigDecimal(String.valueOf(jsondata.get("bmi"))); oodt.setBmi(bmi);
	 * } oodt.setObesityDate(currentDate); if (jsondata.get("height") != null &&
	 * !jsondata.get("height").equals("")) { BigDecimal height = new
	 * BigDecimal(String.valueOf(jsondata.get("height"))); oodt.setHeight(height); }
	 * if (jsondata.get("weight") != null && !jsondata.get("weight").equals("")) {
	 * BigDecimal weight = new BigDecimal(String.valueOf(jsondata.get("weight")));
	 * oodt.setWeight(weight); } if (jsondata.get("idealWeight") != null &&
	 * !jsondata.get("idealWeight").equals("")) { BigDecimal idealWeight = new
	 * BigDecimal(String.valueOf(jsondata.get("idealWeight")));
	 * oodt.setIdealWeight(idealWeight); } if (jsondata.get("varation") != null &&
	 * !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation"))); oodt.setVariation(bd);
	 * } oodt.setMonth(m); oodt.setObesityHdId(obesistyId); session.save(oodt);
	 * 
	 * //opdObsisty(jsondata); } }catch (Exception e) { e.printStackTrace(); } } }
	 * /////////////////////opd patient history /////////////////////////////
	 * OpdPatientHistory opdPatinetHistory = new OpdPatientHistory();
	 * opdPatinetHistory.setVisitId(Long.parseLong(jsondata.get("visitId").toString(
	 * ))); opdPatinetHistory.setPatientId(Long.parseLong(jsondata.get("patientId").
	 * toString()));
	 * opdPatinetHistory.setHospitalId(Long.parseLong(jsondata.get("hospitalId").
	 * toString())); opdPatinetHistory.setChiefComplain(String.valueOf(jsondata.get(
	 * "chiefComplain"))); opdPatinetHistory.setLastChgDate(ourJavaTimestampObject);
	 * opdPatinetHistory.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId").toString()))); session.save(opdPatinetHistory);
	 * //////////////////////MasMedicalExamReport ////////////////////////////// if
	 * (mmer != null) {
	 * mmer.setFinalobservation(String.valueOf(jsondata.get("finalobservation")));
	 * mmer.setVisitId(Long.parseLong(jsondata.get("visitId").toString()));
	 * mmer.setPatientId(Long.parseLong(jsondata.get("patientId").toString()));
	 * mmer.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * mmer.setLastChgDate(ourJavaTimestampObject);
	 * 
	 * session.update(mmer); } else { MasMedicalExamReport masEReports = new
	 * MasMedicalExamReport();
	 * masEReports.setVisitId(Long.parseLong(jsondata.get("visitId").toString()));
	 * masEReports.setPatientId(Long.parseLong(jsondata.get("patientId").toString())
	 * ); masEReports.setFinalobservation(String.valueOf(jsondata.get(
	 * "finalobservation")));
	 * masEReports.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * masEReports.setLastChgDate(ourJavaTimestampObject);
	 * session.save(masEReports); } ///////////// Referal Details Section
	 * //////////////////////////////// if (jsondata.get("listofReferallHD") !=
	 * null) { List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)
	 * (Object) jsondata .get("listofReferallHD");
	 * 
	 * if (list != null) { for (HashMap<String, Object> map : list) {
	 * 
	 * String extId = (String) map.get("extHospitalId"); Long empID =
	 * Long.parseLong(extId); String referralNote = (String)
	 * map.get("referralNote"); patientId = Long.parseLong((String)
	 * map.get("patientId")); hospitalId =
	 * Long.parseLong(String.valueOf(map.get("hospitalId"))); // String
	 * treatmentType=(String)map.get("treatmentType"); ReferralPatientHd header =
	 * null; if (empID != null && opdId != null) { header =
	 * getReferralPatientHdByExeHosAndOpdPd(empID, opdId); } Long id = null; if
	 * (header == null) {
	 * 
	 * header = new ReferralPatientHd(); header.setPatientId(patientId);
	 * header.setHospitalId(hospitalId); header.setExtHospitalId(empID);
	 * header.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * header.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * header.setLastChgDate(ourJavaTimestampObject);
	 * header.setReferralIniDate(currentDate); header.setTreatmentType("E");
	 * header.setReferralIniDate(currentDate);
	 * header.setReferralNote(String.valueOf(referralNote)); header.setStatus("W");
	 * header.setOpdPatientDetailsId(opdId); id =
	 * Long.parseLong(session.save(header).toString()); } else { id =
	 * header.getRefrealHdId(); } Long extHosId = header.getExtHospitalId();
	 * System.out.println(extHosId); List<HashMap<String, Object>> diagnosisList =
	 * (List<HashMap<String, Object>>) (Object) map .get("listofReferalDT"); for
	 * (HashMap<String, Object> diagnosisMap : diagnosisList) {
	 * if(diagnosisMap.get("diagnosisId")!=null &&
	 * diagnosisMap.get("diagnosisId")!="") { long diagnosisId =
	 * Long.parseLong((String) diagnosisMap.get("diagnosisId")); ReferralPatientDt
	 * refDt = new ReferralPatientDt(); refDt.setDiagnosisId(diagnosisId);
	 * refDt.setLastChgDate(ourJavaTimestampObject);
	 * refDt.setExtDepartment(String.valueOf(diagnosisMap.get("extDepartment")));
	 * refDt.setInstruction(String.valueOf(diagnosisMap.get("instruction")));
	 * refDt.setRefrealHdId(id); session.save(refDt); } } }
	 * 
	 * } }
	 * 
	 * ///////////// Investigation Result Details Section
	 * //////////////////////////////// if (jsondata.get("listofResultDetails") !=
	 * null) { List<HashMap<String, Object>> listOfResult = (List<HashMap<String,
	 * Object>>) (Object) jsondata.get("listofResultDetails"); Long headerResultId =
	 * null;
	 * 
	 * DgResultEntryHd header = null; Long visitIdCheck =
	 * Long.parseLong(String.valueOf(jsondata.get("visitId")));
	 * 
	 * if (visitIdCheck != null) { header = getResultByVisitId(visitIdCheck); }
	 * if(header==null) { if (listOfResult != null && !listOfResult.isEmpty()) {
	 * DgResultEntryHd pphd = new DgResultEntryHd();
	 * pphd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pphd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pphd.setLastChgDate(ourJavaTimestampObject);
	 * pphd.setLastChgDate(ourJavaTimestampObject);
	 * pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * pphd.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")))
	 * ); //pphd.setOrderHdId(listOfResult.get(arg0)); headerResultId =
	 * Long.parseLong(session.save(pphd).toString()); //
	 * pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
	 * List<DgResultEntryDt> patientReultDT = new ArrayList<>(); for
	 * (HashMap<String, Object> singleopd : listOfResult) { DgResultEntryDt ppdt1 =
	 * new DgResultEntryDt(); if (singleopd.get("investigationId") != null &&
	 * singleopd.get("investigationId") != "") {
	 * ppdt1.setInvestigationId(Long.valueOf(singleopd.get("investigationId").
	 * toString())); } if (singleopd.get("orderDtId") != null &&
	 * singleopd.get("orderDtId") != "") {
	 * ppdt1.setOrderDtId(Long.valueOf(singleopd.get("orderDtId").toString())); } if
	 * (singleopd.get("result") != null && singleopd.get("result").toString() != "")
	 * { ppdt1.setResult(String.valueOf(singleopd.get("result"))); }
	 * //ppdt1.setResultType("P"); ppdt1.setResultEntryId(headerResultId);
	 * session.save(ppdt1); }
	 * 
	 * } } else { for (HashMap<String, Object> singleopd : listOfResult) {
	 * //DgResultEntryDt ppdt1 = new DgResultEntryDt(); Long
	 * DgResultId=Long.valueOf(String.valueOf(singleopd.get("dgResultId")));
	 * 
	 * DgResultEntryDt ppdt1 = (DgResultEntryDt) session.get(DgResultEntryDt.class,
	 * DgResultId);
	 * 
	 * if (singleopd.get("investigationId") != null &&
	 * singleopd.get("investigationId") != "") {
	 * ppdt1.setInvestigationId(Long.valueOf(singleopd.get("investigationId").
	 * toString())); } if (singleopd.get("orderDtId") != null &&
	 * singleopd.get("orderDtId") != "") {
	 * ppdt1.setOrderDtId(Long.valueOf(singleopd.get("orderDtId").toString())); } if
	 * (singleopd.get("result") != null && singleopd.get("result").toString() != "")
	 * { ppdt1.setResult(String.valueOf(singleopd.get("result"))); }
	 * session.update(ppdt1); } } }
	 * 
	 * }
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); tx.rollback();
	 * System.out.println("Exception Message Print ::" + ex.toString()); return
	 * ex.toString(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return "Successfully saved"; }
	 * 
	 * 
	 * public ReferralPatientHd getReferralPatientHdByExeHosAndOpdPd(Long exeHoss,
	 * Long opdPdId) { Session session = null; ReferralPatientHd referralPatientHd =
	 * null; try { session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr =
	 * session.createCriteria(ReferralPatientHd.class).add(Restrictions.eq(
	 * "extHospitalId", exeHoss)) .add(Restrictions.eq("opdPatientDetailsId",
	 * opdPdId)); List<ReferralPatientHd> listReferralPatientHd = cr.list(); if
	 * (CollectionUtils.isNotEmpty(listReferralPatientHd)) { referralPatientHd =
	 * listReferralPatientHd.get(0); } } catch (Exception e) { e.printStackTrace();
	 * } return referralPatientHd; }
	 * 
	 * 
	 * @Override public List<Visit> getPatientVisit(Long visitId) { Session session
	 * = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(Visit.class); cr.add(Restrictions.eq("visitId",
	 * visitId)); List<Visit> list = cr.list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * 
	 * @Override public List<OpdPatientDetail> getVitalRecord(Long visitId) {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(OpdPatientDetail.class);
	 * cr.add(Restrictions.eq("visitId", visitId)); List<OpdPatientDetail> list =
	 * cr.list(); System.out.println(list.size());
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * 
	 * @Override public List<MasMedicalCategory> getMasMedicalCategory() { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(MasMedicalCategory.class);
	 * cr.add(Restrictions.eq("status", "Y").ignoreCase());
	 * 
	 * ProjectionList projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("medicalCategoryId").as(
	 * "medicalCategoryId"));
	 * projectionList.add(Projections.property("medicalCategoryCode").as(
	 * "medicalCategoryCode"));
	 * projectionList.add(Projections.property("medicalCategoryName").as(
	 * "medicalCategoryName"));
	 * projectionList.add(Projections.property("fitFlag").as("fitFlag"));
	 * 
	 * cr.setProjection(projectionList); List<MasMedicalCategory> list =
	 * cr.setResultTransformer(new
	 * AliasToBeanResultTransformer(MasMedicalCategory.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * 
	 * @Override public Map<String, Object>
	 * getValidateMedicalExamDetails(HashMap<String, Object> jsonData) {
	 * List<Visit>listVisit=null;
	 * List<MasInvestignationMapping>listMasInvestignationMapping=null;
	 * List<DgMasInvestigation>listDgMasInvestigation=null;
	 * Map<String,Object>mapValue=new HashMap<>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); String visitId =
	 * jsonData.get("visitId").toString(); //String agePatient =
	 * jsonData.get("age").toString();
	 * visitId=OpdServiceImpl.getReplaceString(visitId); Criteria
	 * criteria=session.createCriteria(Visit.class) .add(Restrictions.eq("visitId",
	 * Long.parseLong(visitId))); listVisit=criteria.list();
	 * List<Long>listMedicalExamId=new ArrayList<Long>();
	 * 
	 * if(CollectionUtils.isNotEmpty(listVisit)) { for(Visit vv:listVisit) {
	 * listMedicalExamId.add(vv.getExamId()); } } Criteria
	 * criteria1=session.createCriteria(MasInvestignationMapping.class);
	 * //.add(Restrictions.in("medicalExamId",listMedicalExamId))
	 * //.add(Restrictions.ilike("age", "%"
	 * +agePatient.toString().trim()+"%",MatchMode.ANYWHERE));
	 * listMasInvestignationMapping=criteria1.list(); String investigationList="";
	 * if(CollectionUtils.isNotEmpty(listMasInvestignationMapping)) {
	 * for(MasInvestignationMapping
	 * masInvestignationMapping:listMasInvestignationMapping) {
	 * if(StringUtils.isBlank(investigationList) ) {
	 * investigationList=masInvestignationMapping.getInvestignationId(); } else {
	 * investigationList+=","+masInvestignationMapping.getInvestignationId(); } }
	 * List<Long>dgInvestigationId=new ArrayList<>(); if(investigationList!=null &&
	 * !investigationList.equalsIgnoreCase("")) { String []
	 * investMappingValue=investigationList.split(","); for(String
	 * ss:investMappingValue) { dgInvestigationId.add(Long.parseLong(ss.trim())); }
	 * }
	 * 
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
	 * 
	 * criteria2.setProjection(projectionList);
	 * 
	 * listDgMasInvestigation = criteria2 .setResultTransformer(new
	 * AliasToBeanResultTransformer(DgMasInvestigation.class)).list(); } }
	 * mapValue.put("listVisit", listVisit); mapValue.put("listDgMasInvestigation",
	 * listDgMasInvestigation); } catch(Exception e) { e.printStackTrace(); } return
	 * mapValue; }
	 * 
	 * 
	 * @Override public List<MasHospital> getHospitalList() {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(MasHospital.class); ProjectionList
	 * projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("hospitalId").as("hospitalId"));
	 * projectionList.add(Projections.property("hospitalName").as("hospitalName"));
	 * cr.setProjection(projectionList); List<MasHospital> list =
	 * cr.setResultTransformer(new
	 * AliasToBeanResultTransformer(MasHospital.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * 
	 * @Override public List<MasEmpanelledHospital>
	 * getEmpanelledHospital(HashMap<String, String> jsonData) { //Long hospitalId =
	 * Long.parseLong(jsonData.get("hospitalId").toString()); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(MasEmpanelledHospital.class); //
	 * cr.add(Restrictions.eq("hospitalId", hospitalId)); ProjectionList
	 * projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("empanelledHospitalId").as(
	 * "empanelledHospitalId"));
	 * projectionList.add(Projections.property("empanelledHospitalCode").as(
	 * "empanelledHospitalCode"));
	 * projectionList.add(Projections.property("empanelledHospitalName").as(
	 * "empanelledHospitalName"));
	 * 
	 * cr.setProjection(projectionList); List<MasEmpanelledHospital> list = cr
	 * .setResultTransformer(new
	 * AliasToBeanResultTransformer(MasEmpanelledHospital.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * 
	 * @Override public List<MasDepartment> getDepartmentsList() {
	 * List<MasDepartment> departmentList = new ArrayList<MasDepartment>(); try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria criteria = session.createCriteria(MasDepartment.class);
	 * criteria.add(Restrictions.eq("status", "Y")); ProjectionList projectionList =
	 * Projections.projectionList();
	 * projectionList.add(Projections.property("departmentId").as("departmentId"));
	 * projectionList.add(Projections.property("departmentName").as("departmentName"
	 * )); criteria.setProjection(projectionList);
	 * 
	 * departmentList = criteria.setResultTransformer(new
	 * AliasToBeanResultTransformer(MasDepartment.class)).list();
	 * 
	 * }catch(Exception e) { e.printStackTrace(); } return departmentList; }
	 * 
	 * 
	 * @Override public List<Object[]> getDgMasInvestigationsAndResult(Long visitId)
	 * { Transaction transation=null; List<Object[]> listObject=null; try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * transation=session.beginTransaction(); StringBuilder sbQuery = new
	 * StringBuilder(); sbQuery.
	 * append(" select dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME,ohd.ORDERHD_ID,odt.LAB_MARK,odt.urgent,odt.ORDER_DATE, ohd.VISIT_ID,ohd.OTHER_INVESTIGATION,"
	 * ); sbQuery.append(
	 * "ohd.DEPARTMENT_ID,ohd.HOSPITAL_ID,odt.ORDERDT_ID,dgUom.UOM_NAME,odt.INVESTIGATION_REMARKS from  DG_ORDER_HD ohd "
	 * ); sbQuery.
	 * append(" join DG_ORDER_DT odt on  ohd.orderhd_id=odt.orderhd_id join DG_MAS_INVESTIGATION "
	 * ); sbQuery.
	 * append(" dgmas on dgmas.INVESTIGATION_ID=odt.INVESTIGATION_ID left join DG_UOM  dgUom on dgUom.UOM_ID=dgmas.UOM_ID "
	 * ); //sbQuery.
	 * append(" left join DG_RESULT_ENTRY_DT rdt on odt.ORDERDT_ID=rdt.ORDERDT_ID left join DG_RESULT_ENTRY_HD rht on ohd.ORDERHD_ID= rht.ORDERHD_ID "
	 * ); sbQuery.append(" where ohd.VISIT_ID =:visitId"); Query query =
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
	 * @Override public Long saveOrUpdateMedicalExam(MasMedicalExamReport
	 * masExamReport) { Session session=null; Long medicalExaminationId=null;
	 * Transaction tx=null; try{ session=
	 * getHibernateUtils.getHibernateUtlis().OpenSession();
	 * tx=session.beginTransaction(); session.saveOrUpdate(masExamReport);
	 * medicalExaminationId=masExamReport.getMedicalExaminationId();
	 * 
	 * session.flush(); session.clear(); tx.commit();
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * } return medicalExaminationId; }
	 * 
	 * @Override public Long saveOrUpdatePatientMedicalCat(PatientMedicalCat
	 * patientMedicalCat) { Session session=null; Long medCateId=null; Transaction
	 * tx=null; try{ session= getHibernateUtils.getHibernateUtlis().OpenSession();
	 * tx=session.beginTransaction(); session.saveOrUpdate(patientMedicalCat);
	 * medCateId=patientMedicalCat.getMedicalCatId();
	 * 
	 * session.flush(); session.clear(); tx.commit();
	 * 
	 * } catch(Exception e) { e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * } return medCateId; }
	 * 
	 * @Override public PatientMedicalCat getPatientMedicalCat(Long
	 * patientMedicalCatId) { PatientMedicalCat patientMedicalCat=null; try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * patientMedicalCat = (PatientMedicalCat)
	 * session.createCriteria(PatientMedicalCat.class)
	 * .add(Restrictions.eq("medicalCatId", patientMedicalCatId)).uniqueResult(); }
	 * catch(Exception e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * patientMedicalCat; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public String saveAmsfForm16Details(HashMap<String, Object>
	 * jsondata) { Date currentDate = ProjectUtils.getCurrentDate(); Session session
	 * = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(MasMedicalExamReport.class); Transaction tx =
	 * session.beginTransaction(); Long visitId = Long.parseLong((String)
	 * jsondata.get("visitId")); Long masExamReportId; Calendar calendar =
	 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); String procedureType =
	 * null; cr.add(Restrictions.eq("visitId", visitId)); MasMedicalExamReport
	 * masExamReport = (MasMedicalExamReport) cr.uniqueResult(); try {
	 * 
	 * if (masExamReport != null) {
	 * masExamReport.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReport.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"
	 * )))); masExamReport.setIdentificationMarks1(String.valueOf(jsondata.get(
	 * "identificationMarks1")));
	 * masExamReport.setIdentificationMarks2(String.valueOf(jsondata.get(
	 * "identificationMarks2")));
	 * masExamReport.setLastChgDate(ourJavaTimestampObject);
	 * masExamReport.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId")))); //
	 * masExamReport.setDisabilityFlag(String.valueOf(jsondata.get("disabilityFlag")
	 * )); // masExamReport.setDisabilityClaimed(String.valueOf(jsondata.get(
	 * "disabilityClaimed"))); if(jsondata.get("wittnessId")!=null &&
	 * jsondata.get("wittnessId")!="") {
	 * masExamReport.setWittnessId(Long.parseLong(String.valueOf(jsondata.get(
	 * "wittnessId")))); } //Denatl if(jsondata.get("totalTeeth")!="") {
	 * masExamReport.setTotalTeeth(String.valueOf(jsondata.get("totalTeeth"))); }
	 * if(jsondata.get("totalDefectiveTeeth")!="") {
	 * masExamReport.setTotalDefectiveTeeth(String.valueOf(jsondata.get(
	 * "totalDefectiveTeeth"))); } if(jsondata.get("totalNoDentalPoint")!="") {
	 * masExamReport.setTotalNoDentalPoint(String.valueOf(jsondata.get(
	 * "totalNoDentalPoint"))); } if(jsondata.get("missingTeeth")!="") {
	 * masExamReport.setMissingTeeth(String.valueOf(jsondata.get("missingTeeth")));
	 * } if(jsondata.get("unsaveableTeeth")!="") {
	 * masExamReport.setUnsaveableTeeth(String.valueOf(jsondata.get(
	 * "unsaveableTeeth"))); } if(jsondata.get("conditionofgums")!="") {
	 * masExamReport.setConditionofgums(String.valueOf(jsondata.get(
	 * "conditionofgums"))); } if(jsondata.get("dentalOfficer")!="") {
	 * masExamReport.setDentalOfficer(String.valueOf(jsondata.get("dentalOfficer")))
	 * ; } if(jsondata.get("dentalCheckupDate")!=null &&
	 * jsondata.get("dentalCheckupDate")!="") {
	 * masExamReport.setDentalCheckupDate(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("dentalCheckupDate").toString())); }
	 * if(jsondata.get("remarks")!="") {
	 * masExamReport.setRemarks(String.valueOf(jsondata.get("remarks"))); }
	 * //physical Capacity if(jsondata.get("height")!=null &&
	 * jsondata.get("height")!="") {
	 * masExamReport.setHeight(String.valueOf(jsondata.get("height"))); }
	 * if(jsondata.get("idealweight")!=null && jsondata.get("idealweight")!="") {
	 * masExamReport.setIdealweight(String.valueOf(jsondata.get("idealweight"))); }
	 * if(jsondata.get("weight")!=null && jsondata.get("weight")!="") {
	 * masExamReport.setWeight(String.valueOf(jsondata.get("weight"))); }
	 * if(jsondata.get("variationinWeight")!="") {
	 * masExamReport.setOverweight(String.valueOf(jsondata.get("variationinWeight"))
	 * ); } masExamReport.setBhi(String.valueOf(jsondata.get("bmi")));
	 * masExamReport.setBodyfat(String.valueOf(jsondata.get("bodyfat")));
	 * if(jsondata.get("waist")!="") {
	 * masExamReport.setWaist(String.valueOf(jsondata.get("waist"))); }
	 * if(jsondata.get("hips")!="") {
	 * masExamReport.setHips(String.valueOf(jsondata.get("hips"))); }
	 * if(jsondata.get("whr")!="") {
	 * masExamReport.setWhr(String.valueOf(jsondata.get("whr"))); }
	 * if(jsondata.get("skin")!="") {
	 * masExamReport.setSkin(String.valueOf(jsondata.get("skin"))); }
	 * if(jsondata.get("chestfullexpansion")!="") {
	 * masExamReport.setChestfullexpansion(String.valueOf(jsondata.get(
	 * "chestfullexpansion"))); } if(jsondata.get("rangeofexpansion")!="") {
	 * masExamReport.setRangeofexpansion(String.valueOf(jsondata.get(
	 * "rangeofexpansion"))); } if(jsondata.get("sportman")!="") {
	 * masExamReport.setSportman(String.valueOf(jsondata.get("sportman"))); }
	 * //Vision if(jsondata.get("withoutGlassesDistantR")!="") {
	 * masExamReport.setWthoutGlassesRDistant(String.valueOf(jsondata.get(
	 * "withoutGlassesDistantR"))); } if(jsondata.get("withoutGlassesDistantL")!="")
	 * { masExamReport.setWithoutGlassesLDistant(String.valueOf(jsondata.get(
	 * "withoutGlassesDistantL"))); } if(jsondata.get("withGlassesDistantR")!="") {
	 * masExamReport.setWithGlassesRDistant(String.valueOf(jsondata.get(
	 * "withGlassesDistantR"))); } if(jsondata.get("withGlassesDistantL")!="") {
	 * masExamReport.setWithGlassesLDistant(String.valueOf(jsondata.get(
	 * "withGlassesDistantL"))); } if(jsondata.get("withoutGlassesNearVisionR")!="")
	 * { masExamReport.setWithoutGlassesRNearvision(String.valueOf(jsondata.get(
	 * "withoutGlassesNearVisionR"))); }
	 * if(jsondata.get("withoutGlassesNearVisionL")!="") {
	 * masExamReport.setWithoutGlassesLNearvision(String.valueOf(jsondata.get(
	 * "withoutGlassesNearVisionL"))); }
	 * if(jsondata.get("withGlassesNearVisionR")!="") {
	 * masExamReport.setWithGlassesRNearvision(String.valueOf(jsondata.get(
	 * "withGlassesNearVisionR"))); } if(jsondata.get("withGlassesNearVisionL")!="")
	 * { masExamReport.setWithGlassesLNearvision(String.valueOf(jsondata.get(
	 * "withGlassesNearVisionL"))); } if(jsondata.get("cp")!="") {
	 * masExamReport.setNearVisionWithGlassCp(String.valueOf(jsondata.get("cp"))); }
	 * 
	 * // masExamReport.set(String.valueOf(jsondata.get("withoutGlassesLDistant")));
	 * //Hearing if(jsondata.get("earHearingRfw")!=null &&
	 * jsondata.get("earHearingRfw")!="") {
	 * masExamReport.setEarHearingRfw(Long.parseLong(String.valueOf(jsondata.get(
	 * "earHearingRfw")))); } if(jsondata.get("earHearingLfw")!=null &&
	 * jsondata.get("earHearingLfw")!="") {
	 * masExamReport.setEarHearingLfw(Long.parseLong(String.valueOf(jsondata.get(
	 * "earHearingLfw")))); } if(jsondata.get("earHearingBothFw")!=null &&
	 * jsondata.get("earHearingBothFw")!="") {
	 * masExamReport.setEarHearingBothFw(Long.parseLong(String.valueOf(jsondata.get(
	 * "earHearingBothFw")))); } if(jsondata.get("hearingRcv")!=null &&
	 * jsondata.get("hearingRcv")!="") {
	 * masExamReport.setHearingRcv(Long.parseLong(String.valueOf(jsondata.get(
	 * "hearingRcv")))); } if(jsondata.get("hearingLcv")!=null &&
	 * jsondata.get("hearingLcv")!="") {
	 * masExamReport.setHearingLcv(Long.parseLong(String.valueOf(jsondata.get(
	 * "hearingLcv")))); } if(jsondata.get("hearingBothCv")!=null &&
	 * jsondata.get("hearingBothCv")!="") {
	 * masExamReport.setHearingBothCv(Long.parseLong(String.valueOf(jsondata.get(
	 * "hearingBothCv")))); } if(jsondata.get("tympanicr")!="") {
	 * masExamReport.setTympanicr(String.valueOf(jsondata.get("tympanicr"))); }
	 * if(jsondata.get("tympanicl")!="") {
	 * masExamReport.setTympanicl(String.valueOf(jsondata.get("tympanicl"))); }
	 * if(jsondata.get("mobilityl")!="") {
	 * masExamReport.setMobilityl(String.valueOf(jsondata.get("mobilityl"))); }
	 * if(jsondata.get("mobilityr")!="") {
	 * masExamReport.setMobilityr(String.valueOf(jsondata.get("mobilityr"))); }
	 * if(jsondata.get("noseThroatSinuses")!="") {
	 * masExamReport.setNoseThroatSinuses(String.valueOf(jsondata.get(
	 * "noseThroatSinuses"))); } if(jsondata.get("audiometryRecord")!="") {
	 * masExamReport.setAudiometryRecord(String.valueOf(jsondata.get(
	 * "audiometryRecord"))); } //Cardio Vascular System
	 * if(jsondata.get("pulseRates")!="") {
	 * masExamReport.setPulseRates(String.valueOf(jsondata.get("pulseRates"))); }
	 * if(jsondata.get("bpSystolic")!=null) {
	 * masExamReport.setBpSystolic(String.valueOf(jsondata.get("bpSystolic"))); }
	 * if(jsondata.get("bpDiastolic")!=null) {
	 * masExamReport.setBpDiastolic(String.valueOf(jsondata.get("bpDiastolic"))); }
	 * if(jsondata.get("peripheralPulsations")!="") {
	 * masExamReport.setPeripheralPulsations(String.valueOf(jsondata.get(
	 * "peripheralPulsations"))); } if(jsondata.get("heartSize")!="") {
	 * masExamReport.setHeartSize(String.valueOf(jsondata.get("heartSize"))); }
	 * if(jsondata.get("sounds")!="") {
	 * masExamReport.setSounds(String.valueOf(jsondata.get("sounds"))); }
	 * if(jsondata.get("rhythm")!="") {
	 * masExamReport.setRhythm(String.valueOf(jsondata.get("rhythm"))); }
	 * //Respiratory System if(jsondata.get("respiratorySystem")!="") {
	 * masExamReport.setRespiratorySystem(String.valueOf(jsondata.get(
	 * "respiratorySystem"))); } //Gastro Intestinal System
	 * if(jsondata.get("liver")!=null &&jsondata.get("liver")!="") {
	 * masExamReport.setLiver(Double.parseDouble(String.valueOf(jsondata.get("liver"
	 * )))); } if(jsondata.get("spleen")!=null &&jsondata.get("spleen")!="") {
	 * masExamReport.setSpleen(Double.parseDouble(String.valueOf(jsondata.get(
	 * "spleen")))); } //Central Nervous System
	 * if(jsondata.get("higherMentalFunction")!="") {
	 * masExamReport.setHigherMentalFunction(String.valueOf(jsondata.get(
	 * "higherMentalFunction"))); } if(jsondata.get("speech")!="") {
	 * masExamReport.setSpeech(String.valueOf(jsondata.get("speech"))); }
	 * if(jsondata.get("reflexes")!="") {
	 * masExamReport.setReflexes(String.valueOf(jsondata.get("reflexes"))); }
	 * if(jsondata.get("tremors")!="") {
	 * masExamReport.setTremors(String.valueOf(jsondata.get("tremors"))); }
	 * if(jsondata.get("selfBalancingTest")!="") {
	 * masExamReport.setSelfBalancingTest(String.valueOf(jsondata.get(
	 * "selfBalancingTest"))); } if(jsondata.get("locomoterSystem")!="") {
	 * masExamReport.setLocomoterSystem(String.valueOf(jsondata.get(
	 * "locomoterSystem"))); } if(jsondata.get("spine")!="") {
	 * masExamReport.setSpine(String.valueOf(jsondata.get("spine"))); }
	 * if(jsondata.get("herniaMusic")!="") {
	 * masExamReport.setHerniaMusic(String.valueOf(jsondata.get("herniaMusic"))); }
	 * if(jsondata.get("hydrocele")!="") {
	 * masExamReport.setHydrocele(String.valueOf(jsondata.get("hydrocele"))); }
	 * if(jsondata.get("hemorrhoids")!="") {
	 * masExamReport.setHemorrhoids(String.valueOf(jsondata.get("hemorrhoids"))); }
	 * if(jsondata.get("hemorrhoids")!="") {
	 * masExamReport.setBreasts(String.valueOf(jsondata.get("hemorrhoids"))); }
	 * if(jsondata.get("hospitalId")!="") {
	 * masExamReport.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); } masExamReport.setLastChangedDate(ourJavaTimestampObject);
	 * if(jsondata.get("mensturalHistory")!="") {
	 * masExamReport.setMenstrualHistory(String.valueOf(jsondata.get(
	 * "mensturalHistory"))); } if(jsondata.get("lmpSelect")!="") {
	 * masExamReport.setLmpStatus(String.valueOf(jsondata.get("lmpSelect"))); }
	 * if(jsondata.get("lMP")!=null && jsondata.get("lMP")!="") {
	 * masExamReport.setLmp(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "lMP").toString())); } if(jsondata.get("nosOfPregnancies")!=null &&
	 * jsondata.get("nosOfPregnancies")!="") {
	 * masExamReport.setNoOfPregnancies(Long.parseLong(String.valueOf(jsondata.get(
	 * "nosOfPregnancies")))); } if(jsondata.get("nosOfAbortions")!=null &&
	 * jsondata.get("nosOfAbortions")!="") {
	 * masExamReport.setNoOfAbortions(Long.parseLong(String.valueOf(jsondata.get(
	 * "nosOfAbortions")))); } if(jsondata.get("nosOfAbortions")!=null &&
	 * jsondata.get("nosOfAbortions")!="") {
	 * masExamReport.setNoOfChildren(Long.parseLong(String.valueOf(jsondata.get(
	 * "nosOfChildren")))); } if(jsondata.get("childDateOfLastConfinement")!=null &&
	 * jsondata.get("childDateOfLastConfinement")!="") {
	 * masExamReport.setLastConfinementDate(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("childDateOfLastConfinement").toString())); }
	 * if(jsondata.get("vaginalDischarge")!="") {
	 * masExamReport.setVaginalDischarge(String.valueOf(jsondata.get(
	 * "vaginalDischarge")));
	 * 
	 * } if(jsondata.get("usgAbdomen")!="") {
	 * masExamReport.setUsgAbdomen(String.valueOf(jsondata.get("usgAbdomen"))); }
	 * if(jsondata.get("prolapse")!="") {
	 * masExamReport.setProlapse(String.valueOf(jsondata.get("prolapse"))); }
	 * 
	 * session.update(masExamReport); masExamReportId =
	 * masExamReport.getMedicalExaminationId(); ///////////////////////////////////
	 * list Of Illness Injury //////////////////////////////
	 * if(jsondata.get("listOfIllnessInjury")!=null
	 * &&jsondata.get("listOfIllnessInjury")!="") { List<HashMap<String, Object>>
	 * listOfIllnessInjury = (List<HashMap<String, Object>>) (Object)
	 * jsondata.get("listOfIllnessInjury"); for (HashMap<String, Object>
	 * listOfIllness : listOfIllnessInjury) { PatientDiseaseInfo pdinfo=new
	 * PatientDiseaseInfo();
	 * pdinfo.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId")))
	 * ); pdinfo.setIcdId(Long.parseLong(String.valueOf(listOfIllness.get(
	 * "icdIdWillness")))); if(listOfIllness.get("stDate")!=null &&
	 * listOfIllness.get("stDate")!="") {
	 * pdinfo.setStDate(HMSUtil.convertStringTypeDateToDateType(listOfIllness.get(
	 * "stDate").toString())); }
	 * pdinfo.setStPlace(String.valueOf(listOfIllness.get("stPlace")));
	 * pdinfo.setTreatedPlace(String.valueOf(listOfIllness.get("treatedPlace")));
	 * if(listOfIllness.get("fromDate")!=null && listOfIllness.get("fromDate")!="")
	 * {
	 * pdinfo.setFromDate(HMSUtil.convertStringTypeDateToDateType(listOfIllness.get(
	 * "fromDate").toString())); } if(listOfIllness.get("toDate")!=null &&
	 * listOfIllness.get("toDate")!="") {
	 * pdinfo.setToDate(HMSUtil.convertStringTypeDateToDateType(listOfIllness.get(
	 * "toDate").toString())); } pdinfo.setLastChgDate(ourJavaTimestampObject);
	 * pdinfo.setBeforeFlag("N");
	 * pdinfo.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * session.save(pdinfo); } }
	 * 
	 * /////////////////////////////////// list Of Service Details
	 * ////////////////////////////// if (jsondata.get("serviceDetails") != null &&
	 * jsondata.get("serviceDetails") != "") { List<HashMap<String, Object>>
	 * listOfServiceDetails = (List<HashMap<String, Object>>) (Object) jsondata
	 * .get("serviceDetails"); for (HashMap<String, Object> listOfService :
	 * listOfServiceDetails) { PatientServicesDetail psd = new
	 * PatientServicesDetail();
	 * psd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * if (listOfService.get("fromDate") != null && listOfService.get("fromDate") !=
	 * "") {
	 * psd.setFromDate(HMSUtil.convertStringTypeDateToDateType(listOfService.get(
	 * "fromDate").toString())); } if (listOfService.get("toDate") != null &&
	 * listOfService.get("toDate") != "") {
	 * psd.setToDate(HMSUtil.convertStringTypeDateToDateType(listOfService.get(
	 * "toDate").toString())); }
	 * psd.setPlace(String.valueOf(listOfService.get("place")));
	 * psd.setPf(String.valueOf(listOfService.get("pf")));
	 * psd.setLastChgDate(ourJavaTimestampObject);
	 * psd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * session.save(psd); } }
	 * 
	 * if (visitId != null) { Visit visit = (Visit) session.get(Visit.class,
	 * visitId); if (visit != null) { visit.setVisitStatus("c");
	 * visit.setExamStatus("F"); session.update(visit); } }
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * MasMedicalExamReport masExamReportsSave = new MasMedicalExamReport();
	 * masExamReportsSave.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReportsSave.setVisitId(Long.parseLong(String.valueOf(jsondata.get(
	 * "visitId")))); masExamReportsSave.setLastChgDate(ourJavaTimestampObject);
	 * masExamReportsSave.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId"))));
	 * masExamReportsSave.setIdentificationMarks1(String.valueOf(jsondata.get(
	 * "identificationMarks1")));
	 * masExamReportsSave.setIdentificationMarks2(String.valueOf(jsondata.get(
	 * "identificationMarks2"))); //
	 * masExamReportsSave.setDisabilityFlag(String.valueOf(jsondata.get(
	 * "disabilityFlag"))); //
	 * masExamReportsSave.setDisabilityClaimed(String.valueOf(jsondata.get(
	 * "disabilityClaimed"))); if(jsondata.get("wittnessId")!=null &&
	 * jsondata.get("wittnessId")!="") {
	 * masExamReportsSave.setWittnessId(Long.parseLong(String.valueOf(jsondata.get(
	 * "wittnessId")))); } //Denatl if(jsondata.get("totalTeeth")!="") {
	 * masExamReportsSave.setTotalTeeth(String.valueOf(jsondata.get("totalTeeth")));
	 * } if(jsondata.get("totalDefectiveTeeth")!="") {
	 * masExamReportsSave.setTotalDefectiveTeeth(String.valueOf(jsondata.get(
	 * "totalDefectiveTeeth"))); } if(jsondata.get("totalNoDentalPoint")!="") {
	 * masExamReportsSave.setTotalNoDentalPoint(String.valueOf(jsondata.get(
	 * "totalNoDentalPoint"))); } if(jsondata.get("missingTeeth")!="") {
	 * masExamReportsSave.setMissingTeeth(String.valueOf(jsondata.get("missingTeeth"
	 * ))); } if(jsondata.get("unsaveableTeeth")!="") {
	 * masExamReportsSave.setUnsaveableTeeth(String.valueOf(jsondata.get(
	 * "unsaveableTeeth"))); } if(jsondata.get("conditionofgums")!="") {
	 * masExamReportsSave.setConditionofgums(String.valueOf(jsondata.get(
	 * "conditionofgums"))); } if(jsondata.get("dentalOfficer")!="") {
	 * masExamReportsSave.setDentalOfficer(String.valueOf(jsondata.get(
	 * "dentalOfficer"))); } if(jsondata.get("dentalCheckupDate")!=null &&
	 * jsondata.get("dentalCheckupDate")!="") {
	 * masExamReportsSave.setDentalCheckupDate(HMSUtil.
	 * convertStringTypeDateToDateType(jsondata.get("dentalCheckupDate").toString())
	 * ); } if(jsondata.get("remarks")!="") {
	 * masExamReportsSave.setRemarks(String.valueOf(jsondata.get("remarks"))); }
	 * //physical Capacity if(jsondata.get("height")!=null &&
	 * jsondata.get("height")!="") { masExamReportsSave.setHeight(
	 * String.valueOf(jsondata.get("height"))); }
	 * if(jsondata.get("idealweight")!=null && jsondata.get("idealweight")!="") {
	 * masExamReportsSave.setIdealweight(String.valueOf(jsondata.get("idealweight"))
	 * ); } if(jsondata.get("weight")!=null && jsondata.get("weight")!="") {
	 * masExamReportsSave.setWeight( String.valueOf(jsondata.get("weight"))); }
	 * if(jsondata.get("variationinWeight")!="") {
	 * masExamReportsSave.setOverweight(String.valueOf(jsondata.get(
	 * "variationinWeight"))); }
	 * masExamReportsSave.setBhi(String.valueOf(jsondata.get("bmi")));
	 * masExamReportsSave.setBodyfat(String.valueOf(jsondata.get("bodyfat")));
	 * if(jsondata.get("waist")!="") {
	 * masExamReportsSave.setWaist(String.valueOf(jsondata.get("waist"))); }
	 * if(jsondata.get("hips")!="") {
	 * masExamReportsSave.setHips(String.valueOf(jsondata.get("hips"))); }
	 * if(jsondata.get("whr")!="") {
	 * masExamReportsSave.setWhr(String.valueOf(jsondata.get("whr"))); }
	 * if(jsondata.get("skin")!="") {
	 * masExamReportsSave.setSkin(String.valueOf(jsondata.get("skin"))); }
	 * if(jsondata.get("chestfullexpansion")!="") {
	 * masExamReportsSave.setChestfullexpansion(String.valueOf(jsondata.get(
	 * "chestfullexpansion"))); } if(jsondata.get("rangeofexpansion")!="") {
	 * masExamReportsSave.setRangeofexpansion(String.valueOf(jsondata.get(
	 * "rangeofexpansion"))); } if(jsondata.get("sportman")!="") {
	 * masExamReportsSave.setSportman(String.valueOf(jsondata.get("sportman"))); }
	 * //Vision if(jsondata.get("withoutGlassesDistantR")!="") {
	 * masExamReportsSave.setWthoutGlassesRDistant(String.valueOf(jsondata.get(
	 * "withoutGlassesDistantR"))); } if(jsondata.get("withoutGlassesDistantL")!="")
	 * { masExamReportsSave.setWithoutGlassesLDistant(String.valueOf(jsondata.get(
	 * "withoutGlassesDistantL"))); } if(jsondata.get("withGlassesDistantR")!="") {
	 * masExamReportsSave.setWithGlassesRDistant(String.valueOf(jsondata.get(
	 * "withGlassesDistantR"))); } if(jsondata.get("withGlassesDistantL")!="") {
	 * masExamReportsSave.setWithGlassesLDistant(String.valueOf(jsondata.get(
	 * "withGlassesDistantL"))); } if(jsondata.get("withoutGlassesNearVisionR")!="")
	 * {
	 * masExamReportsSave.setWithoutGlassesRNearvision(String.valueOf(jsondata.get(
	 * "withoutGlassesNearVisionR"))); }
	 * if(jsondata.get("withoutGlassesNearVisionL")!="") {
	 * masExamReportsSave.setWithoutGlassesLNearvision(String.valueOf(jsondata.get(
	 * "withoutGlassesNearVisionL"))); }
	 * if(jsondata.get("withGlassesNearVisionR")!="") {
	 * masExamReportsSave.setWithGlassesRNearvision(String.valueOf(jsondata.get(
	 * "withGlassesNearVisionR"))); } if(jsondata.get("withGlassesNearVisionL")!="")
	 * { masExamReportsSave.setWithGlassesLNearvision(String.valueOf(jsondata.get(
	 * "withGlassesNearVisionL"))); } if(jsondata.get("cp")!="") {
	 * masExamReportsSave.setNearVisionWithGlassCp(String.valueOf(jsondata.get("cp")
	 * )); }
	 * 
	 * //
	 * masExamReportsSave.set(String.valueOf(jsondata.get("withoutGlassesLDistant"))
	 * ); //Hearing if(jsondata.get("earHearingRfw")!=null &&
	 * jsondata.get("earHearingRfw")!="") {
	 * masExamReportsSave.setEarHearingRfw(Long.parseLong(String.valueOf(jsondata.
	 * get("earHearingRfw")))); } if(jsondata.get("earHearingLfw")!=null &&
	 * jsondata.get("earHearingLfw")!="") {
	 * masExamReportsSave.setEarHearingLfw(Long.parseLong(String.valueOf(jsondata.
	 * get("earHearingLfw")))); } if(jsondata.get("earHearingBothFw")!=null &&
	 * jsondata.get("earHearingBothFw")!="") {
	 * masExamReportsSave.setEarHearingBothFw(Long.parseLong(String.valueOf(jsondata
	 * .get("earHearingBothFw")))); } if(jsondata.get("hearingRcv")!=null &&
	 * jsondata.get("hearingRcv")!="") {
	 * masExamReportsSave.setHearingRcv(Long.parseLong(String.valueOf(jsondata.get(
	 * "hearingRcv")))); } if(jsondata.get("hearingLcv")!=null &&
	 * jsondata.get("hearingLcv")!="") {
	 * masExamReportsSave.setHearingLcv(Long.parseLong(String.valueOf(jsondata.get(
	 * "hearingLcv")))); } if(jsondata.get("hearingBothCv")!=null &&
	 * jsondata.get("hearingBothCv")!="") {
	 * masExamReportsSave.setHearingBothCv(Long.parseLong(String.valueOf(jsondata.
	 * get("hearingBothCv")))); } if(jsondata.get("tympanicr")!="") {
	 * masExamReportsSave.setTympanicr(String.valueOf(jsondata.get("tympanicr"))); }
	 * if(jsondata.get("tympanicl")!="") {
	 * masExamReportsSave.setTympanicl(String.valueOf(jsondata.get("tympanicl"))); }
	 * if(jsondata.get("mobilityl")!="") {
	 * masExamReportsSave.setMobilityl(String.valueOf(jsondata.get("mobilityl"))); }
	 * if(jsondata.get("mobilityr")!="") {
	 * masExamReportsSave.setMobilityr(String.valueOf(jsondata.get("mobilityr"))); }
	 * if(jsondata.get("noseThroatSinuses")!="") {
	 * masExamReportsSave.setNoseThroatSinuses(String.valueOf(jsondata.get(
	 * "noseThroatSinuses"))); } if(jsondata.get("audiometryRecord")!="") {
	 * masExamReportsSave.setAudiometryRecord(String.valueOf(jsondata.get(
	 * "audiometryRecord"))); } //Cardio Vascular System
	 * if(jsondata.get("pulseRates")!="") {
	 * masExamReportsSave.setPulseRates(String.valueOf(jsondata.get("pulseRates")));
	 * } if(jsondata.get("bpSystolic")!=null) {
	 * masExamReportsSave.setBpSystolic(String.valueOf(jsondata.get("bpSystolic")));
	 * } if(jsondata.get("bpDiastolic")!=null) {
	 * masExamReportsSave.setBpDiastolic(String.valueOf(jsondata.get("bpDiastolic"))
	 * ); } if(jsondata.get("peripheralPulsations")!="") {
	 * masExamReportsSave.setPeripheralPulsations(String.valueOf(jsondata.get(
	 * "peripheralPulsations"))); } if(jsondata.get("heartSize")!="") {
	 * masExamReportsSave.setHeartSize(String.valueOf(jsondata.get("heartSize"))); }
	 * if(jsondata.get("sounds")!="") {
	 * masExamReportsSave.setSounds(String.valueOf(jsondata.get("sounds"))); }
	 * if(jsondata.get("rhythm")!="") {
	 * masExamReportsSave.setRhythm(String.valueOf(jsondata.get("rhythm"))); }
	 * //Respiratory System if(jsondata.get("respiratorySystem")!="") {
	 * masExamReportsSave.setRespiratorySystem(String.valueOf(jsondata.get(
	 * "respiratorySystem"))); } //Gastro Intestinal System
	 * if(jsondata.get("liver")!=null &&jsondata.get("liver")!="") {
	 * masExamReportsSave.setLiver(Double.parseDouble(String.valueOf(jsondata.get(
	 * "liver")))); } if(jsondata.get("spleen")!=null &&jsondata.get("spleen")!="")
	 * {
	 * masExamReportsSave.setSpleen(Double.parseDouble(String.valueOf(jsondata.get(
	 * "spleen")))); } //Central Nervous System
	 * if(jsondata.get("higherMentalFunction")!="") {
	 * masExamReportsSave.setHigherMentalFunction(String.valueOf(jsondata.get(
	 * "higherMentalFunction"))); } if(jsondata.get("speech")!="") {
	 * masExamReportsSave.setSpeech(String.valueOf(jsondata.get("speech"))); }
	 * if(jsondata.get("reflexes")!="") {
	 * masExamReportsSave.setReflexes(String.valueOf(jsondata.get("reflexes"))); }
	 * if(jsondata.get("tremors")!="") {
	 * masExamReportsSave.setTremors(String.valueOf(jsondata.get("tremors"))); }
	 * if(jsondata.get("selfBalancingTest")!="") {
	 * masExamReportsSave.setSelfBalancingTest(String.valueOf(jsondata.get(
	 * "selfBalancingTest"))); } if(jsondata.get("locomoterSystem")!="") {
	 * masExamReportsSave.setLocomoterSystem(String.valueOf(jsondata.get(
	 * "locomoterSystem"))); } if(jsondata.get("spine")!="") {
	 * masExamReportsSave.setSpine(String.valueOf(jsondata.get("spine"))); }
	 * if(jsondata.get("herniaMusic")!="") {
	 * masExamReportsSave.setHerniaMusic(String.valueOf(jsondata.get("herniaMusic"))
	 * ); } if(jsondata.get("hydrocele")!="") {
	 * masExamReportsSave.setHydrocele(String.valueOf(jsondata.get("hydrocele"))); }
	 * if(jsondata.get("hemorrhoids")!="") {
	 * masExamReportsSave.setHemorrhoids(String.valueOf(jsondata.get("hemorrhoids"))
	 * ); } if(jsondata.get("hemorrhoids")!="") {
	 * masExamReportsSave.setBreasts(String.valueOf(jsondata.get("hemorrhoids"))); }
	 * if(jsondata.get("hospitalId")!="") {
	 * masExamReportsSave.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); }
	 * masExamReportsSave.setLastChangedDate(ourJavaTimestampObject);
	 * if(jsondata.get("mensturalHistory")!="") {
	 * masExamReportsSave.setMenstrualHistory(String.valueOf(jsondata.get(
	 * "mensturalHistory"))); } if(jsondata.get("lmpSelect")!="") {
	 * masExamReportsSave.setLmpStatus(String.valueOf(jsondata.get("lmpSelect"))); }
	 * if(jsondata.get("lMP")!=null && jsondata.get("lMP")!="") {
	 * masExamReportsSave.setLmp(HMSUtil.convertStringTypeDateToDateType(jsondata.
	 * get("lMP").toString())); } if(jsondata.get("nosOfPregnancies")!=null &&
	 * jsondata.get("nosOfPregnancies")!="") {
	 * masExamReportsSave.setNoOfPregnancies(Long.parseLong(String.valueOf(jsondata.
	 * get("nosOfPregnancies")))); } if(jsondata.get("nosOfAbortions")!=null &&
	 * jsondata.get("nosOfAbortions")!="") {
	 * masExamReportsSave.setNoOfAbortions(Long.parseLong(String.valueOf(jsondata.
	 * get("nosOfAbortions")))); } if(jsondata.get("nosOfAbortions")!=null &&
	 * jsondata.get("nosOfAbortions")!="") {
	 * masExamReportsSave.setNoOfChildren(Long.parseLong(String.valueOf(jsondata.get
	 * ("nosOfChildren")))); } if(jsondata.get("childDateOfLastConfinement")!=null
	 * && jsondata.get("childDateOfLastConfinement")!="") {
	 * masExamReportsSave.setLastConfinementDate(HMSUtil.
	 * convertStringTypeDateToDateType(jsondata.get("childDateOfLastConfinement").
	 * toString())); } if(jsondata.get("vaginalDischarge")!="") {
	 * masExamReportsSave.setVaginalDischarge(String.valueOf(jsondata.get(
	 * "vaginalDischarge")));
	 * 
	 * } if(jsondata.get("usgAbdomen")!="") {
	 * masExamReportsSave.setUsgAbdomen(String.valueOf(jsondata.get("usgAbdomen")));
	 * } if(jsondata.get("prolapse")!="") {
	 * masExamReportsSave.setProlapse(String.valueOf(jsondata.get("prolapse"))); }
	 * session.save(masExamReportsSave); /////////////////////////////////// list Of
	 * Illness Injury //////////////////////////////
	 * if(jsondata.get("listOfIllnessInjury")!=null
	 * &&jsondata.get("listOfIllnessInjury")!="") { List<HashMap<String, Object>>
	 * listOfIllnessInjury = (List<HashMap<String, Object>>) (Object)
	 * jsondata.get("listOfIllnessInjury"); for (HashMap<String, Object>
	 * listOfIllness : listOfIllnessInjury) { PatientDiseaseInfo pdinfo=new
	 * PatientDiseaseInfo();
	 * pdinfo.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId")))
	 * ); pdinfo.setIcdId(Long.parseLong(String.valueOf(listOfIllness.get(
	 * "icdIdWillness")))); if(listOfIllness.get("stDate")!=null &&
	 * listOfIllness.get("stDate")!="") {
	 * pdinfo.setStDate(HMSUtil.convertStringTypeDateToDateType(listOfIllness.get(
	 * "stDate").toString())); }
	 * pdinfo.setStPlace(String.valueOf(listOfIllness.get("stPlace")));
	 * pdinfo.setTreatedPlace(String.valueOf(listOfIllness.get("treatedPlace")));
	 * if(listOfIllness.get("fromDate")!=null && listOfIllness.get("fromDate")!="")
	 * {
	 * pdinfo.setFromDate(HMSUtil.convertStringTypeDateToDateType(listOfIllness.get(
	 * "fromDate").toString())); } if(listOfIllness.get("toDate")!=null &&
	 * listOfIllness.get("toDate")!="") {
	 * pdinfo.setToDate(HMSUtil.convertStringTypeDateToDateType(listOfIllness.get(
	 * "toDate").toString())); } pdinfo.setLastChgDate(ourJavaTimestampObject);
	 * pdinfo.setBeforeFlag("N");
	 * pdinfo.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * 
	 * session.save(pdinfo); } }
	 * 
	 * /////////////////////////////////// list Of Service Details
	 * ////////////////////////////// if (jsondata.get("serviceDetails") != null &&
	 * jsondata.get("serviceDetails") != "") { List<HashMap<String, Object>>
	 * listOfServiceDetails = (List<HashMap<String, Object>>) (Object) jsondata
	 * .get("serviceDetails"); for (HashMap<String, Object> listOfService :
	 * listOfServiceDetails) { PatientServicesDetail psd = new
	 * PatientServicesDetail();
	 * psd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * if (listOfService.get("fromDate") != null && listOfService.get("fromDate") !=
	 * "") {
	 * psd.setFromDate(HMSUtil.convertStringTypeDateToDateType(listOfService.get(
	 * "fromDate").toString())); } if (listOfService.get("toDate") != null &&
	 * listOfService.get("toDate") != "") {
	 * psd.setToDate(HMSUtil.convertStringTypeDateToDateType(listOfService.get(
	 * "toDate").toString())); }
	 * psd.setPlace(String.valueOf(listOfService.get("place")));
	 * psd.setPf(String.valueOf(listOfService.get("pf")));
	 * psd.setLastChgDate(ourJavaTimestampObject);
	 * psd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * session.save(psd); } }
	 * 
	 * masExamReportsSave.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId"))));
	 * 
	 * masExamReportId =
	 * Long.parseLong(session.save(masExamReportsSave).toString()); if (visitId !=
	 * null) { Visit visit = (Visit) session.get(Visit.class, visitId); if (visit !=
	 * null) { visit.setVisitStatus("c"); visit.setExamStatus("F");
	 * session.update(visit); } }
	 * 
	 * 
	 * } String actionMe=(String) jsondata.get("actionMe");
	 * if(actionMe.contentEquals("approveAndForward")) { if(masExamReport!=null) {
	 * masExamReport.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReport.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"
	 * )))); if(jsondata.get("forWardStatus").equals("RMO")) {
	 * masExamReport.setApprovedBy("RMO");
	 * masExamReport.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); } else { masExamReport.setApprovedBy("MO");
	 * masExamReport.setMoUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); }
	 * masExamReport.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId"))));
	 * masExamReport.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId"))));
	 * masExamReport.setForwardUnitId(Long.parseLong(String.valueOf(jsondata.get(
	 * "forwardTo"))));
	 * masExamReport.setFowardedDesignationId(Long.parseLong(String.valueOf(jsondata
	 * .get("designationForMe")))); masExamReport.setStatus("af");
	 * masExamReport.setLastChgDate(ourJavaTimestampObject);
	 * session.update(masExamReport); Visit visit = (Visit) session.get(Visit.class,
	 * visitId); if (visit != null) { visit.setVisitStatus("O");
	 * session.update(visit); } } else { MasMedicalExamReport mmer=new
	 * MasMedicalExamReport();
	 * mmer.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * mmer.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * if(jsondata.get("forWardStatus").equals("RMO")) { mmer.setApprovedBy("RMO");
	 * mmer.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId")))); }
	 * else { mmer.setApprovedBy("MO");
	 * mmer.setMoUserId(Long.parseLong(String.valueOf(jsondata.get("userId")))); }
	 * mmer.setStatus("af");
	 * mmer.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * mmer.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")))
	 * );
	 * mmer.setForwardUnitId(Long.parseLong(String.valueOf(jsondata.get("forwardTo")
	 * )));
	 * mmer.setFowardedDesignationId(Long.parseLong(String.valueOf(jsondata.get(
	 * "designationForMe")))); mmer.setLastChgDate(ourJavaTimestampObject);
	 * session.save(mmer); Visit visit = (Visit) session.get(Visit.class, visitId);
	 * if (visit != null) { visit.setVisitStatus("O"); session.update(visit); } } }
	 * if(actionMe.contentEquals("pending")) { if (masExamReport != null) {
	 * masExamReport.setFinalobservation(String.valueOf(jsondata.get(
	 * "pendingRemarks"))); session.update(masExamReport); } Visit visit = (Visit)
	 * session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("R"); session.update(visit); } }
	 * if(actionMe.contentEquals("approveAndClose")) { if(masExamReport!=null) {
	 * masExamReport.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReport.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"
	 * )))); if(jsondata.get("forWardStatus").equals("RMO")) {
	 * masExamReport.setApprovedBy("RMO");
	 * masExamReport.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); } else if(jsondata.get("forWardStatus").equals("RMO")) {
	 * masExamReport.setMdUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); } else { masExamReport.setApprovedBy("MO");
	 * masExamReport.setMoUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); }
	 * masExamReport.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId")))); masExamReport.setStatus("ac"); session.update(masExamReport); }
	 * Visit visit = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("C"); visit.setExamStatus("C");
	 * System.out.println("Closedddd"); session.update(visit); } PatientMedicalCat
	 * pmt = (PatientMedicalCat) session.get(PatientMedicalCat.class, visitId); if
	 * (pmt != null) { pmt.setMbStatus("C"); System.out.println("Closedddd");
	 * session.update(pmt); }
	 * 
	 * } tx.commit();
	 * 
	 * } catch (Exception ex) {
	 * 
	 * // System.out.println("Exception e="+ex.); ex.printStackTrace();
	 * tx.rollback(); System.out.println("Exception Message Print ::" +
	 * ex.toString()); return ex.toString(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return "Successfully saved"; }
	 * 
	 * @Override public Map<String, Object>
	 * getMOValidateMedicalBoardWaitingList(HashMap<String, Object> jsonData) {
	 * List<Visit>listVisit=null; Map<String,Object>map=new HashMap<>(); int count =
	 * 0; Criterion cr1=null; try { int pageNo =
	 * Integer.parseInt(jsonData.get("pageNo") + ""); int pagingSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria criteria=session.createCriteria(Visit.class).createAlias(
	 * "masAppointmentType", "masAppointmentType")
	 * .add(Restrictions.eq("masAppointmentType.appointmentTypeCode",
	 * "MB").ignoreCase()) .add(Restrictions.ne("visitFlag", "E").ignoreCase());
	 * if(jsonData.get("flagForList").toString().equalsIgnoreCase("a")) {
	 * cr1=Restrictions.eq("examStatus", "F").ignoreCase(); criteria.add(cr1); }
	 * else if(jsonData.get("flagForList").toString().equalsIgnoreCase("b")) {
	 * cr1=Restrictions.eq("examStatus", "F").ignoreCase(); criteria.add(cr1); }
	 * else { cr1=Restrictions.eq("examStatus", "F").ignoreCase();
	 * //cr1=Restrictions.eq("visitStatus", "C").ignoreCase(); criteria.add(cr1); }
	 * listVisit=criteria.list();
	 * 
	 * count = listVisit.size(); criteria = criteria.setFirstResult(pagingSize *
	 * (pageNo - 1)); criteria = criteria.setMaxResults(pagingSize); listVisit =
	 * criteria.list(); map.put("count", count); map.put("listVisit", listVisit); }
	 * catch(Exception e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return map; }
	 * 
	 * 
	 * 
	 * @Override public List<MasMedicalExamReport> getMasMedicalExamReport(Long
	 * visitId) { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(MasMedicalExamReport.class);
	 * cr.add(Restrictions.eq("visitId", visitId)); List<MasMedicalExamReport> list
	 * = cr.list(); getHibernateUtils.getHibernateUtlis().CloseConnection(); return
	 * list; }
	 * 
	 * public DgResultEntryHd getResultByVisitId(Long visitId) { Session session
	 * =null; DgResultEntryHd referralPatientHd=null; try {
	 * session=getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(DgResultEntryHd.class) .add(Restrictions.eq("visitId",
	 * visitId)); List<DgResultEntryHd>listReferralPatientHd=cr.list();
	 * if(CollectionUtils.isNotEmpty(listReferralPatientHd)) {
	 * referralPatientHd=listReferralPatientHd.get(0); } } catch(Exception e) {
	 * e.printStackTrace(); } return referralPatientHd; }
	 * 
	 * @Override public String saveUpdateSpeicialistOpinionMBDetails(HashMap<String,
	 * Object> getjsondata) { JSONArray newForm=new
	 * JSONArray(getjsondata.get("refferalSpecialistOpinion").toString());
	 * JSONObject jsondata = (JSONObject) newForm.get(0);
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction(); Calendar calendar =
	 * Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime());
	 * 
	 * Long visitId = Long.parseLong((String) jsondata.get("visitId"));
	 * 
	 * try {
	 * 
	 * if (jsondata.get("listofRefferalDetails") != null) { JSONArray list=new
	 * JSONArray(jsondata.get("listofRefferalDetails").toString()); if (list !=
	 * null) { for(int i=0;i<list.length();i++) { JSONObject singleopd =
	 * list.getJSONObject(i); Long
	 * DgResultDetailId=Long.valueOf(String.valueOf(singleopd.get("dgRferralDtId")))
	 * ;
	 * 
	 * ReferralPatientDt ppdt1 = (ReferralPatientDt)
	 * session.get(ReferralPatientDt.class, DgResultDetailId);
	 * 
	 * if (singleopd.get("specialistOpinion") != null &&
	 * singleopd.get("specialistOpinion") != "") {
	 * ppdt1.setFinalNote(String.valueOf(singleopd.get("specialistOpinion"))); }
	 * String referalRmsId = getjsondata.get("referalRmsId").toString();
	 * referalRmsId = OpdServiceImpl.getReplaceString(referalRmsId);
	 * if(referalRmsId.endsWith(",")) {
	 * referalRmsId=referalRmsId.substring(0,referalRmsId.length()-1); } //String[]
	 * referalRmsIdValueArrays = referalRmsId.split(",");
	 * if(getjsondata.get("referalRmsId")!=null &&
	 * getjsondata.get("referalRmsId")!="") {
	 * ppdt1.setRidcId(Long.parseLong(String.valueOf(referalRmsId))); }
	 * 
	 * session.update(ppdt1); } }
	 * 
	 * if (visitId != null) { Visit visit = (Visit) session.get(Visit.class,
	 * visitId); if (visit != null) { visit.setVisitStatus("m");
	 * session.update(visit); } }
	 * 
	 * }
	 * 
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); tx.rollback();
	 * System.out.println("Exception Message Print ::" + ex.toString()); return
	 * ex.toString(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return "Successfully Saved"; }
	 * 
	 * 
	 * @Override public String saveAmsfForm15Details(HashMap<String, Object>
	 * getjsondata) { JSONArray newForm=new
	 * JSONArray(getjsondata.get("amsfForm15Data").toString()); JSONObject jsondata
	 * = (JSONObject) newForm.get(0);
	 * 
	 * Calendar calendar = Calendar.getInstance(); java.sql.Timestamp
	 * ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); Long
	 * patientCategoryIdVal=null; Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(PatientMedBoard.class); Transaction tx =
	 * session.beginTransaction(); Long visitId = Long.parseLong((String)
	 * jsondata.get("visitId")); cr.add(Restrictions.eq("visitId", visitId));
	 * PatientMedBoard pmb = (PatientMedBoard) cr.uniqueResult();
	 * //PatientMedBoardChecklist pmcheck=new PatientMedBoardChecklist();
	 * 
	 * Criteria cr2 = session.createCriteria(MasMedicalExamReport.class);
	 * cr2.add(Restrictions.eq("visitId", visitId)); MasMedicalExamReport
	 * masExamReport = (MasMedicalExamReport) cr2.uniqueResult();
	 * 
	 * Criteria cr1 = session.createCriteria(PatientMedBoardChecklist.class);
	 * cr1.add(Restrictions.eq("visitId", visitId)); List<PatientMedBoardChecklist>
	 * list=cr1.list();
	 * 
	 * if(CollectionUtils.isNotEmpty(list)) { try {
	 * 
	 * if (pmb != null) {
	 * pmb.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmb.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmb.setAuthority(String.valueOf(jsondata.get("authorityforBoard")));
	 * pmb.setPlaceOfBoard(String.valueOf(jsondata.get("place")));
	 * if(jsondata.get("height")!=null && !jsondata.get("height").equals("")) {
	 * pmb.setHeight(Long.parseLong(String.valueOf(jsondata.get("height")))); }
	 * if(jsondata.get("weight")!=null && !jsondata.get("weight").equals("")) {
	 * pmb.setWeight(Double.parseDouble(String.valueOf(jsondata.get("weight")))); }
	 * if(jsondata.get("dateOfEnrollment")!=null &&
	 * !jsondata.get("dateOfEnrollment").equals("")) {
	 * pmb.setDateOfEnrolment(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "dateOfEnrollment").toString())); }
	 * pmb.setAddressOnLeave(String.valueOf(jsondata.get("addressOnLeave")));
	 * if(jsondata.get("ceasedDutyOn")!=null &&
	 * !jsondata.get("ceasedDutyOn").equals("")) {
	 * pmb.setCeasedDutyOn(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "ceasedDutyOn").toString())); }
	 * pmb.setRecordOfficeAddress(String.valueOf(jsondata.get("recordOfficeAddress")
	 * )); //pmb.setDisablityAttrService(String.valueOf(jsondata.get(
	 * "disablityAttrService")));
	 * pmb.setDisablityAttrServiceRemark(String.valueOf(jsondata.get(
	 * "disablityAttrServiceRemark")));
	 * //pmb.setDirectlyAttrService((String.valueOf(jsondata.get(
	 * "directlyAttrService"))));
	 * pmb.setDirectlyAttrServiceRemark((String.valueOf(jsondata.get(
	 * "directlyAttrServiceRemark")))); if(jsondata.get("previousDisablement")!=null
	 * && !jsondata.get("previousDisablement").equals("")) {
	 * pmb.setPreviousDisablement(String.valueOf(jsondata.get("previousDisablement")
	 * )); } if(jsondata.get("presentDisablement")!=null &&
	 * !jsondata.get("presentDisablement").equals("")) {
	 * pmb.setPresentDisablement(String.valueOf(jsondata.get("presentDisablement")))
	 * ; } if(jsondata.get("reasonForVarious")!=null &&
	 * !jsondata.get("reasonForVarious").equals("")) {
	 * pmb.setReasonForVarious(String.valueOf(jsondata.get("reasonForVarious"))); }
	 * pmb.setRestrictionRegardingEmp(String.valueOf(jsondata.get(
	 * "restrictionRegardingEmp")));
	 * pmb.setInstructionRemark(String.valueOf(jsondata.get("instructionRemark")));
	 * pmb.setInstructionNote(String.valueOf(jsondata.get("instructionNote")));
	 * if(jsondata.get("signDate")!=null && !jsondata.get("signDate").equals("")) {
	 * pmb.setSigDate(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "signDate").toString())); }
	 * //pmb.setSignatureIndividual(String.valueOf(jsondata.get(
	 * "signatureIndividual")));
	 * pmb.setMember1(String.valueOf(jsondata.get("member1Name")));
	 * pmb.setMember2(String.valueOf(jsondata.get("member2Name")));
	 * pmb.setMember3(String.valueOf(jsondata.get("member3Name")));
	 * pmb.setMem1Rank(String.valueOf(jsondata.get("mem1Rank")));
	 * pmb.setMem2Rank(String.valueOf(jsondata.get("mem2Rank")));
	 * pmb.setMem3Rank(String.valueOf(jsondata.get("mem3Rank")));
	 * pmb.setLastChgDate(ourJavaTimestampObject);
	 * if(getjsondata.get("mbRmsId")!=null &&
	 * !getjsondata.get("mbRmsId").equals("")) {
	 * pmb.setRidcId(Long.parseLong(String.valueOf(getjsondata.get("mbRmsId")))); }
	 * if(jsondata.has("aaName")) {
	 * pmb.setAaName(String.valueOf(jsondata.get("aaName")));
	 * pmb.setAaRankDest(String.valueOf(jsondata.get("aaRank")));
	 * pmb.setAaPlace(String.valueOf(jsondata.get("aaPlace")));
	 * if(jsondata.get("aaDate")!=null && !jsondata.get("aaDate").equals("")) {
	 * pmb.setAaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.get("aaDate").
	 * toString())); } pmb.setPaName(String.valueOf(jsondata.get("paName")));
	 * pmb.setPaRankDest(String.valueOf(jsondata.get("paRank")));
	 * pmb.setPaPlace(String.valueOf(jsondata.get("paPlace")));
	 * if(jsondata.get("paDate")!=null && !jsondata.get("paDate").equals("")) {
	 * pmb.setPaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.get("paDate").
	 * toString())); }
	 * pmb.setTypeOfCommission(String.valueOf(jsondata.get("typeOfCommission")));
	 * pmb.setLastChgDate(ourJavaTimestampObject);
	 * pmb.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))))
	 * ; } session.update(pmb);
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * PatientMedBoard patientMedBoard = new PatientMedBoard();
	 * patientMedBoard.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * patientMedBoard.setVisitId(Long.parseLong(String.valueOf(jsondata.get(
	 * "visitId"))));
	 * patientMedBoard.setAuthority(String.valueOf(jsondata.get("authorityforBoard")
	 * )); patientMedBoard.setPlaceOfBoard(String.valueOf(jsondata.get("place")));
	 * if(!jsondata.get("height").equals("")) {
	 * patientMedBoard.setHeight(Long.parseLong(String.valueOf(jsondata.get("height"
	 * )))); } if(!jsondata.get("weight").equals("")) {
	 * patientMedBoard.setWeight(Double.parseDouble(String.valueOf(jsondata.get(
	 * "weight")))); }
	 * 
	 * if(jsondata.get("dateOfEnrollment")!=null &&
	 * !jsondata.get("dateOfEnrollment").equals("")) {
	 * patientMedBoard.setDateOfEnrolment(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("dateOfEnrollment").toString())); }
	 * patientMedBoard.setAddressOnLeave(String.valueOf(jsondata.get(
	 * "addressOnLeave"))); if(jsondata.get("ceasedDutyOn")!=null &&
	 * !jsondata.get("ceasedDutyOn").equals("")) {
	 * patientMedBoard.setCeasedDutyOn(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("ceasedDutyOn").toString())); }
	 * patientMedBoard.setRecordOfficeAddress(String.valueOf(jsondata.get(
	 * "recordOfficeAddress")));
	 * //patientMedBoard.setDisablityAttrService(String.valueOf(jsondata.get(
	 * "disablityAttrService")));
	 * patientMedBoard.setDisablityAttrServiceRemark(String.valueOf(jsondata.get(
	 * "disablityAttrServiceRemark")));
	 * //patientMedBoard.setDirectlyAttrService((String.valueOf(jsondata.get(
	 * "directlyAttrService"))));
	 * patientMedBoard.setDirectlyAttrServiceRemark((String.valueOf(jsondata.get(
	 * "directlyAttrServiceRemark")))); if(jsondata.get("previousDisablement")!=null
	 * && !jsondata.get("previousDisablement").equals("")) {
	 * patientMedBoard.setPreviousDisablement(String.valueOf(jsondata.get(
	 * "previousDisablement"))); } if(jsondata.get("presentDisablement")!=null &&
	 * !jsondata.get("presentDisablement").equals("")) {
	 * patientMedBoard.setPresentDisablement(String.valueOf(jsondata.get(
	 * "presentDisablement"))); } if(jsondata.get("previousDisablement")!=null &&
	 * !jsondata.get("previousDisablement").equals("")) {
	 * patientMedBoard.setReasonForVarious(String.valueOf(jsondata.get(
	 * "reasonForVarious"))); }
	 * patientMedBoard.setRestrictionRegardingEmp(String.valueOf(jsondata.get(
	 * "restrictionRegardingEmp")));
	 * patientMedBoard.setInstructionRemark(String.valueOf(jsondata.get(
	 * "instructionRemark")));
	 * patientMedBoard.setInstructionNote(String.valueOf(jsondata.get(
	 * "instructionNote"))); if(jsondata.get("signDate")!=null &&
	 * !jsondata.get("signDate").equals("")) {
	 * patientMedBoard.setSigDate(HMSUtil.convertStringTypeDateToDateType(jsondata.
	 * get("signDate").toString())); }
	 * //patientMedBoard.setSignatureIndividual(String.valueOf(jsondata.get(
	 * "signatureIndividual")));
	 * patientMedBoard.setMember1(String.valueOf(jsondata.get("member1Name")));
	 * patientMedBoard.setMember2(String.valueOf(jsondata.get("member2Name")));
	 * patientMedBoard.setMember3(String.valueOf(jsondata.get("member3Name")));
	 * patientMedBoard.setMem1Rank(String.valueOf(jsondata.get("mem1Rank")));
	 * patientMedBoard.setMem2Rank(String.valueOf(jsondata.get("mem2Rank")));
	 * patientMedBoard.setMem3Rank(String.valueOf(jsondata.get("mem3Rank")));
	 * patientMedBoard.setLastChgDate(ourJavaTimestampObject);
	 * if(getjsondata.get("mbRmsId")!=null &&
	 * !getjsondata.get("mbRmsId").equals("")) {
	 * patientMedBoard.setRidcId(Long.parseLong(String.valueOf(getjsondata.get(
	 * "mbRmsId")))); }
	 * patientMedBoard.setAaName(String.valueOf(jsondata.get("aaName")));
	 * patientMedBoard.setAaRankDest(String.valueOf(jsondata.get("aaRank")));
	 * patientMedBoard.setAaPlace(String.valueOf(jsondata.get("aaPlace")));
	 * if(jsondata.get("aaDate")!=null && !jsondata.get("aaDate").equals("")) {
	 * patientMedBoard.setAaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.
	 * get("aaDate").toString())); }
	 * patientMedBoard.setPaName(String.valueOf(jsondata.get("paName")));
	 * patientMedBoard.setPaRankDest(String.valueOf(jsondata.get("paRank")));
	 * patientMedBoard.setPaPlace(String.valueOf(jsondata.get("paPlace")));
	 * if(jsondata.get("paDate")!=null && !jsondata.get("paDate").equals("")) {
	 * patientMedBoard.setPaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.
	 * get("paDate").toString())); }
	 * patientMedBoard.setTypeOfCommission(String.valueOf(jsondata.get(
	 * "typeOfCommission")));
	 * patientMedBoard.setLastChgDate(ourJavaTimestampObject);
	 * patientMedBoard.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); session.save(patientMedBoard); }
	 * ///////////////////////////////Medical Category List
	 * ////////////////////////////////////////////////////////// String
	 * preAndPreCatId=""; if(jsondata.has("listOfMedicalCategory15")) { if
	 * (jsondata.get("listOfMedicalCategory15") != null &&
	 * !jsondata.get("listOfMedicalCategory15").equals("")) { JSONArray
	 * listMedicalCategory15 = new
	 * JSONArray(jsondata.get("listOfMedicalCategory15").toString());
	 * System.out.println(listMedicalCategory15); if (listMedicalCategory15 != null
	 * ) { //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for
	 * (int i = 0; i < listMedicalCategory15.length(); i++) { JSONObject mapMedical
	 * = listMedicalCategory15.getJSONObject(i);
	 * 
	 * Long patientCategoryIdValOld=null;
	 * if(!mapMedical.get("patientCategoryId").equals("")) {
	 * patientCategoryIdValOld=Long.valueOf(String.valueOf(mapMedical.get(
	 * "patientCategoryId"))); }else { patientCategoryIdValOld= null; }
	 * if(patientCategoryIdValOld!=null) { PatientMedicalCat pmtUpdate
	 * =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdValOld); pmtUpdate.setMbStatus("C");
	 * session.update(pmtUpdate); } PatientMedicalCat pmt = new PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmt.setLastChgDate(ourJavaTimestampObject);
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * pmt.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory"))));
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("P"); //pmt.setLastChgDate(ourJavaTimestampObject); if
	 * (mapMedical.get("categoryDate") != null &&
	 * !mapMedical.get("categoryDate").equals("")) { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate")
	 * != null && !mapMedical.get("nextCategoryDate").equals("")) { Date d1 =
	 * HMSUtil
	 * .convertStringTypeDateToDateType(mapMedical.get("nextCategoryDate").toString(
	 * )); pmt.setNextCategoryDate(d1); } if(mapMedical.get("duration")!=null &&
	 * !mapMedical.get("duration").equals("")) {
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * } pmt.setLastChgDate(ourJavaTimestampObject); if
	 * (mapMedical.get("dateOfOrigin") != null &&
	 * !mapMedical.get("dateOfOrigin").equals("")) { Date dOrgin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmt.setDateOfOrigin(dOrgin); }
	 * pmt.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * session.save(pmt); preAndPreCatId+=""+pmt.getMedicalCatId()+","; } } } }
	 * ////////////////////////////////////////List of Medical Category New 19
	 * section////////////////////////////////////////
	 * //////////////////////////////////////// if
	 * (jsondata.get("listOfMedicalCategory") != null &&
	 * !jsondata.get("listOfMedicalCategory").equals("")) { JSONArray
	 * listMedicalCategory = new
	 * JSONArray(jsondata.get("listOfMedicalCategory").toString());
	 * System.out.println(listMedicalCategory); if (listMedicalCategory != null ) {
	 * String [] preAndPreCatIdA= null; if(!preAndPreCatId.equals("")) {
	 * preAndPreCatIdA=preAndPreCatId.split(","); } Integer count=0; //for
	 * (HashMap<String, Object> mapMedical : listMedicalCategory) { for (int i = 0;
	 * i < listMedicalCategory.length(); i++) { JSONObject mapMedical =
	 * listMedicalCategory.getJSONObject(i); if (mapMedical.get("patientCategoryId")
	 * != null && !mapMedical.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal =
	 * Long.valueOf(String.valueOf(mapMedical.get("patientCategoryId"))); } if
	 * (patientCategoryIdVal != null &&
	 * !mapMedical.get("patientCategoryId").equals("") &&
	 * !mapMedical.get("patientFitFlag").equals("F")) { PatientMedicalCat pmtUpdate
	 * = (PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal);
	 * pmtUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); pmtUpdate.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmtUpdate.setApplyFor("Y"); pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"
	 * )))); if(mapMedical.get("medicalCategory")!=null &&
	 * !mapMedical.get("medicalCategory").equals("")) {
	 * pmtUpdate.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); } if (mapMedical.get("categoryType").equals("T")) {
	 * pmtUpdate.setCategoryType("T"); } else if
	 * (mapMedical.get("categoryType").equals("P")) {
	 * pmtUpdate.setCategoryType("P"); } pmtUpdate.setMbStatus("A");
	 * pmtUpdate.setRecommendFlag("Y"); if (mapMedical.get("categoryDate") != null
	 * && mapMedical.get("categoryDate") != "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmtUpdate.setCategoryDate(d); } if
	 * (mapMedical.get("nextCategoryDate") != null &&
	 * mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(
	 * mapMedical.get("nextCategoryDate").toString());
	 * pmtUpdate.setNextCategoryDate(d1); }
	 * pmtUpdate.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"
	 * )))); if(mapMedical.has("dateOfOrigin")) { if (mapMedical.get("dateOfOrigin")
	 * != null && !mapMedical.get("dateOfOrigin").equals("")) {
	 * pmtUpdate.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.
	 * get("dateOfOrigin").toString())); } else {
	 * pmtUpdate.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.
	 * get("categoryDate").toString())); }
	 * if(!mapMedical.get("placeOfOrigin").equals("")) {
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * } else { pmtUpdate.setPlaceOfOrigin(String.valueOf(jsondata.get("place"))); }
	 * } pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * session.update(pmtUpdate); } else
	 * if(!mapMedical.get("patientFitFlag").equals("F")) { PatientMedicalCat pmt =
	 * new PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor("Y"); pmt.setLastChgDate(ourJavaTimestampObject);
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * pmt.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory"))));
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("A"); pmt.setRecommendFlag("Y"); if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate")
	 * != null && mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("nextCategoryDate").
	 * toString()); pmt.setNextCategoryDate(d1); }
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * if(mapMedical.has("dateOfOrigin")) { if (mapMedical.get("dateOfOrigin") !=
	 * null && !mapMedical.get("dateOfOrigin").equals("")) {
	 * pmt.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.get(
	 * "dateOfOrigin").toString())); } else {
	 * pmt.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.get(
	 * "categoryDate").toString())); }
	 * if(!mapMedical.get("placeOfOrigin").equals("")) {
	 * pmt.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin"))); } else
	 * { pmt.setPlaceOfOrigin(String.valueOf(jsondata.get("place"))); } }
	 * pmt.setLastChgDate(ourJavaTimestampObject);
	 * if(mapMedical.get("patientCategoryFId")!=null &&
	 * !mapMedical.get("patientCategoryFId").equals("")) {
	 * pmt.setpMedCatFid(Long.parseLong(String.valueOf(mapMedical.get(
	 * "patientCategoryFId")))); } if(preAndPreCatIdA!=null &&
	 * ArrayUtils.isNotEmpty(preAndPreCatIdA) && preAndPreCatIdA.length > count) {
	 * pmt.setpMedCatFid(Long.parseLong(preAndPreCatIdA[count].trim())); }
	 * session.save(pmt); count++; } else { if (patientCategoryIdVal != null &&
	 * !mapMedical.get("patientCategoryId").equals("") &&
	 * mapMedical.get("patientFitFlag").equals("F")) { PatientMedicalCat pmtUpdate =
	 * (PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal);
	 * pmtUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); pmtUpdate.setApplyFor("Y");
	 * pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"
	 * )))); if(mapMedical.get("medicalCategory")!=null &&
	 * !mapMedical.get("medicalCategory").equals("")) {
	 * pmtUpdate.setpMedCatId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); } if (mapMedical.get("categoryDate") != null &&
	 * mapMedical.get("categoryDate") != "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmtUpdate.setpMedCatDate(d); }
	 * if(mapMedical.has("dateOfOrigin")) { if (mapMedical.get("dateOfOrigin") !=
	 * null && !mapMedical.get("dateOfOrigin").equals("")) {
	 * pmtUpdate.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.
	 * get("dateOfOrigin").toString())); }
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * } pmtUpdate.setMbStatus("C"); pmtUpdate.setRecommendFlag("Y");
	 * pmtUpdate.setpMedFitFlag("F");
	 * pmtUpdate.setLastChgDate(ourJavaTimestampObject); session.update(pmtUpdate);
	 * } else if(mapMedical.get("patientFitFlag").equals("F")) { PatientMedicalCat
	 * pmt = new PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * pmt.setpMedCatId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); if (mapMedical.get("categoryDate") != null &&
	 * mapMedical.get("categoryDate") != "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setpMedCatDate(d); } if(mapMedical.has("dateOfOrigin")) { if
	 * (mapMedical.get("dateOfOrigin") != null &&
	 * !mapMedical.get("dateOfOrigin").equals("")) {
	 * pmt.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.get(
	 * "dateOfOrigin").toString())); }
	 * pmt.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin"))); }
	 * pmt.setMbStatus("C"); pmt.setRecommendFlag("Y"); pmt.setpMedFitFlag("F");
	 * pmt.setLastChgDate(ourJavaTimestampObject);
	 * if(mapMedical.get("patientCategoryFId")!=null &&
	 * !mapMedical.get("patientCategoryFId").equals("")) {
	 * pmt.setpMedCatFid(Long.parseLong(String.valueOf(mapMedical.get(
	 * "patientCategoryFId")))); } if(preAndPreCatIdA!=null &&
	 * ArrayUtils.isNotEmpty(preAndPreCatIdA) && preAndPreCatIdA.length > count) {
	 * pmt.setpMedCatFid(Long.parseLong(preAndPreCatIdA[count].trim())); }
	 * session.save(pmt); count++; } } } } }
	 * //////////////////////////////////update date Of Origin and place of origin
	 * ///////////////////////// if (jsondata.get("listOfOrigin") != null &&
	 * !jsondata.get("listOfOrigin").equals("")) { JSONArray listOfOrigin=new
	 * JSONArray(jsondata.get("listOfOrigin").toString()); if (listOfOrigin != null)
	 * {
	 * 
	 * //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for(int
	 * i=0;i<listOfOrigin.length();i++) { JSONObject mapMedicalOrigin =
	 * listOfOrigin.getJSONObject(i);
	 * if(mapMedicalOrigin.get("patientCategoryId")!=null &&
	 * !mapMedicalOrigin.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedicalOrigin.get(
	 * "patientCategoryId"))); } if(patientCategoryIdVal!=null) { PatientMedicalCat
	 * pmtUpdate =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal); if (mapMedicalOrigin.get("dateOfOrigin") != null &&
	 * !mapMedicalOrigin.get("dateOfOrigin").equals("")) { Date dateOfOrigin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedicalOrigin.get("dateOfOrigin").
	 * toString()); pmtUpdate.setDateOfOrigin(dateOfOrigin); }
	 * if(mapMedicalOrigin.get("placeOfOrigin")!=null &&
	 * !mapMedicalOrigin.get("placeOfOrigin").equals("")) {
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedicalOrigin.get(
	 * "placeOfOrigin"))); } pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * session.update(pmtUpdate);
	 * 
	 * } } }
	 * 
	 * } //masExamReportId =
	 * Long.parseLong(session.save(masExamReportsSave).toString()); if (visitId !=
	 * null) { Visit visit = (Visit) session.get(Visit.class, visitId); if (visit !=
	 * null) { visit.setVisitStatus("c"); visit.setExamStatus("F");
	 * session.update(visit); } } medicalExamDAO. saveUpdateOfPatientDocumentDetail(
	 * getjsondata); String actionMe=(String) jsondata.get("actionMe");
	 * if(actionMe.contentEquals("approveAndForward")) { if(masExamReport!=null) {
	 * masExamReport.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReport.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"
	 * )))); if(jsondata.get("forWardStatus").equals("RMO")) {
	 * masExamReport.setApprovedBy("RMO");
	 * masExamReport.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * ))));
	 * masExamReport.setRmoHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); } else if(jsondata.get("forWardStatus").equals("PRMO")) {
	 * masExamReport.setApprovedBy("PRMO");
	 * masExamReport.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * ))));
	 * masExamReport.setPdmsHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); } else { masExamReport.setApprovedBy("MO");
	 * masExamReport.setMoUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); masExamReport.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); }
	 * masExamReport.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId")))); //
	 * masExamReport.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId"))));
	 * masExamReport.setForwardUnitId(Long.parseLong(String.valueOf(jsondata.get(
	 * "forwardTo"))));
	 * masExamReport.setFowardedDesignationId(Long.parseLong(String.valueOf(jsondata
	 * .get("designationForMe")))); masExamReport.setStatus("af");
	 * masExamReport.setLastChgDate(ourJavaTimestampObject);
	 * if(jsondata.get("signDate")!=null && !jsondata.get("signDate").equals("")) {
	 * masExamReport.setMediceExamDate(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("signDate").toString())); } session.update(masExamReport); Visit
	 * visit = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("O"); session.update(visit); } } else {
	 * MasMedicalExamReport mmer=new MasMedicalExamReport();
	 * mmer.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * mmer.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * if(jsondata.get("forWardStatus").equals("RMO")) { mmer.setApprovedBy("RMO");
	 * mmer.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * mmer.setRmoHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"
	 * )))); } else { mmer.setApprovedBy("MO");
	 * mmer.setMoUserId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * mmer.setRmoHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"
	 * )))); } mmer.setStatus("af");
	 * mmer.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * //mmer.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")
	 * )));
	 * mmer.setForwardUnitId(Long.parseLong(String.valueOf(jsondata.get("forwardTo")
	 * )));
	 * mmer.setFowardedDesignationId(Long.parseLong(String.valueOf(jsondata.get(
	 * "designationForMe")))); mmer.setLastChgDate(ourJavaTimestampObject);
	 * if(jsondata.get("signDate")!=null && !jsondata.get("signDate").equals("")) {
	 * mmer.setMediceExamDate(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "signDate").toString())); } session.save(mmer); Visit visit = (Visit)
	 * session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("O"); session.update(visit); } } }
	 * if(actionMe.contentEquals("pending")) { if (pmb != null) {
	 * pmb.setPendingRemarks(String.valueOf(jsondata.get("pendingRemarks")));
	 * session.update(pmb); } Visit visit = (Visit) session.get(Visit.class,
	 * visitId); if (visit != null) { visit.setVisitStatus("R");
	 * session.update(visit); } } if(actionMe.contentEquals("approveAndClose")) {
	 * if(masExamReport!=null) {
	 * masExamReport.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReport.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"
	 * )))); if(jsondata.get("forWardStatus").equals("RMO")) {
	 * masExamReport.setApprovedBy("RMO");
	 * masExamReport.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * ))));
	 * masExamReport.setRmoHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); } else if(jsondata.get("forWardStatus").equals("PRMO")) {
	 * masExamReport.setApprovedBy("PRMO");
	 * masExamReport.setMdUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * ))));
	 * masExamReport.setPdmsHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); } else { masExamReport.setApprovedBy("MO");
	 * masExamReport.setMoUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); masExamReport.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); }
	 * masExamReport.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get(
	 * "userId")))); masExamReport.setStatus("ac"); session.update(masExamReport); }
	 * Visit visit = (Visit) session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("C"); visit.setExamStatus("C");
	 * System.out.println("Closedddd"); session.update(visit); } if
	 * (jsondata.get("listOfMedicalCategoryUpdate") != null) { JSONArray
	 * listOfMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategoryUpdate").toString()); if
	 * (listOfMedicalCategory != null) {
	 * 
	 * //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for(int
	 * i=0;i<listOfMedicalCategory.length();i++) { JSONObject mapMedicalOrigin =
	 * listOfMedicalCategory.getJSONObject(i);
	 * if(mapMedicalOrigin.get("patientCategoryId")!=null &&
	 * !mapMedicalOrigin.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedicalOrigin.get(
	 * "patientCategoryId"))); } if(patientCategoryIdVal!=null) { PatientMedicalCat
	 * pmtUpdate =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal); pmtUpdate.setMbStatus("C"); session.update(pmtUpdate);
	 * 
	 * } } }
	 * 
	 * } if (jsondata.get("listOfMedicalCategoryUpdateNew") != null) { JSONArray
	 * listOfMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategoryUpdateNew").toString()); if
	 * (listOfMedicalCategory != null) {
	 * 
	 * //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for(int
	 * i=0;i<listOfMedicalCategory.length();i++) { JSONObject mapMedicalOrigin =
	 * listOfMedicalCategory.getJSONObject(i);
	 * if(mapMedicalOrigin.get("patientCategoryId")!=null &&
	 * !mapMedicalOrigin.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedicalOrigin.get(
	 * "patientCategoryId"))); } if(patientCategoryIdVal!=null) { PatientMedicalCat
	 * pmtUpdate =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal); pmtUpdate.setMbStatus("P"); session.update(pmtUpdate);
	 * 
	 * } } }
	 * 
	 * }
	 * 
	 * } ///////////////////////////Recommendation Composite Data
	 * ///////////////////////////// if(jsondata.get("recommedicalCompositeName")!=
	 * null && !jsondata.get("recommedicalCompositeName").equals("")) {
	 * if(jsondata.get("recommedicalCompositeNamePId").equals("")) {
	 * PatientMedicalCat pmt = new PatientMedicalCat();
	 * pmt.setpMedCatId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName"))));
	 * if(jsondata.get("categoryCompositeDate")!=null &&
	 * jsondata.get("recomcategoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pmt.setpMedCatDate(md); }
	 * pmt.setRecommendFlag("Y");
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * session.save(pmt);
	 * 
	 * 
	 * Long patientId=Long.parseLong((String) jsondata.get("patientId"));
	 * if(patientId!=null) { Patient pt = (Patient) session.get(Patient.class,
	 * patientId); if(jsondata.get("recommedicalCompositeName")!=null &&
	 * !jsondata.get("recommedicalCompositeName").equals("")) {
	 * pt.setMedicalCategoryId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName")))); }
	 * if(jsondata.get("recomcategoryCompositeDate")!=null &&
	 * !jsondata.get("recomcategoryCompositeDate").equals("")) { Date mdss =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pt.setMedCatDate(mdss); }
	 * session.update(pt); }
	 * 
	 * 
	 * } else { Long
	 * RecommnedCatId=Long.parseLong(jsondata.get("recommedicalCompositeNamePId").
	 * toString()); PatientMedicalCat pmtRecomm=(PatientMedicalCat)
	 * session.get(PatientMedicalCat.class, RecommnedCatId);
	 * pmtRecomm.setpMedCatId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName"))));
	 * if(jsondata.get("recomcategoryCompositeDate")!=null &&
	 * jsondata.get("recomcategoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pmtRecomm.setpMedCatDate(md); }
	 * pmtRecomm.setRecommendFlag("Y");
	 * pmtRecomm.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtRecomm.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); session.update(pmtRecomm);
	 * 
	 * } Long patientId=Long.parseLong((String) jsondata.get("patientId"));
	 * if(patientId!=null) { Patient pt = (Patient) session.get(Patient.class,
	 * patientId); if(jsondata.get("recommedicalCompositeName")!=null &&
	 * !jsondata.get("recommedicalCompositeName").equals("")) {
	 * pt.setMedicalCategoryId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName")))); pt.setFitFlag("C"); }
	 * if(jsondata.get("recomcategoryCompositeDate")!=null &&
	 * !jsondata.get("recomcategoryCompositeDate").equals("")) { Date md1 =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pt.setMedCatDate(md1); }
	 * session.update(pt); } } ////////////////////////////////////////Patient
	 * MedicalComposite Data////////////////////////////////////////
	 * ////////////////////////////////////////
	 * if(jsondata.has("medicalCompositeName")) {
	 * if(jsondata.get("medicalCompositeName")!= null &&
	 * jsondata.get("medicalCompositeName")!="" &&
	 * !jsondata.get("recommedicalCompositeName").equals("")) { Long
	 * patientId=Long.parseLong((String) jsondata.get("patientId"));
	 * if(patientId!=null) { Patient pt = (Patient) session.get(Patient.class,
	 * patientId); if(jsondata.get("medicalCompositeName")!=null &&
	 * !jsondata.get("medicalCompositeName").equals("") &&
	 * jsondata.get("recommedicalCompositeName").equals("")) {
	 * pt.setMedicalCategoryId(Long.parseLong(String.valueOf(jsondata.get(
	 * "medicalCompositeName")))); pt.setFitFlag("C"); }
	 * if(jsondata.get("categoryCompositeDate")!=null &&
	 * jsondata.get("categoryCompositeDate").equals("") &&
	 * jsondata.get("recommedicalCompositeName").equals("")) { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get("categoryCompositeDate")
	 * .toString()); pt.setMedCatDate(md); }
	 * 
	 * session.update(pt); } } } Long opdId = null;
	 * if(jsondata.has("diagnosisName")) { Criteria crOpd =
	 * session.createCriteria(OpdPatientDetail.class);
	 * crOpd.add(Restrictions.eq("visitId", visitId)); OpdPatientDetail opdlist =
	 * (OpdPatientDetail) crOpd.uniqueResult(); if (opdlist != null) {
	 * opdlist.setIcdDiagnosis(String.valueOf(jsondata.get("diagnosisName")));
	 * opdlist.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))
	 * ));
	 * opdlist.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * opdlist.setLastChgDate(ourJavaTimestampObject);
	 * opdlist.setOpdDate(ourJavaTimestampObject);
	 * opdlist.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * session.update(opdlist); opdId = opdlist.getOpdPatientDetailsId(); } else {
	 * OpdPatientDetail opddetails = new OpdPatientDetail();
	 * opddetails.setIcdDiagnosis(String.valueOf(jsondata.get("diagnosisName")));
	 * opddetails.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * opddetails.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId")))
	 * );
	 * opddetails.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))
	 * )); opddetails.setLastChgDate(ourJavaTimestampObject);
	 * opddetails.setOpdDate(ourJavaTimestampObject); opdId =
	 * Long.parseLong(session.save(opddetails).toString()); } }
	 * ///////////////////////// Discharge ICD Code Details
	 * Entry///////////////////////// ///////////////////////////// if
	 * (jsondata.has("diagnosisData")) { if (jsondata.get("diagnosisData") != null)
	 * { //List<HashMap<String, Object>> listIcdValue = (List<HashMap<String,
	 * Object>>) (Object) jsondata.get("diagnosisData"); JSONArray listIcdValue=new
	 * JSONArray(jsondata.get("diagnosisData").toString()); Long icdId=null; if
	 * (listIcdValue != null) { List<DischargeIcdCode> dischargeDT = new
	 * ArrayList<>(); for(int i=0;i<listIcdValue.length();i++) { JSONObject icdOpd =
	 * listIcdValue.getJSONObject(i); // Criteria crDischarge =
	 * session.createCriteria(DischargeIcdCode.class);
	 * //crDischarge.add(Restrictions.eq("visitId", visitId)); //DischargeIcdCode
	 * dischargeIcdCode = (DischargeIcdCode) crDischarge.uniqueResult();
	 * DischargeIcdCode dischargePrescHd = null; if(icdOpd.get("diagnosisId")!=null
	 * && !icdOpd.get("diagnosisId").equals("") &&
	 * !icdOpd.get("diagnosisId").equals("undefined")) {
	 * icdId=Long.parseLong(String.valueOf(icdOpd.get("diagnosisId")));
	 * dischargePrescHd = getDischargeIcd(icdId, visitId); }
	 * 
	 * if(dischargePrescHd!=null) { Long
	 * icdUpdate=dischargePrescHd.getDischargeIcdCodeId(); DischargeIcdCode
	 * discUpdate =(DischargeIcdCode) session.get(DischargeIcdCode.class,
	 * icdUpdate);
	 * discUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * discUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId")))
	 * ); discUpdate.setLastChgDate(ourJavaTimestampObject);
	 * discUpdate.setOpdPatientDetailsId(opdId);
	 * discUpdate.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))
	 * )); if(icdOpd.get("diagnosisId")!=null &&
	 * !icdOpd.get("diagnosisId").equals("") &&
	 * !icdOpd.get("diagnosisId").equals("undefined")) {
	 * discUpdate.setIcdId(Long.parseLong(String.valueOf(icdOpd.get("diagnosisId")))
	 * ); dischargeDT.add(discUpdate); session.update(discUpdate); }
	 * 
	 * } else { DischargeIcdCode disIcdCode = new DischargeIcdCode();
	 * disIcdCode.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * disIcdCode.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId")))
	 * ); disIcdCode.setLastChgDate(ourJavaTimestampObject);
	 * disIcdCode.setOpdPatientDetailsId(opdId);
	 * disIcdCode.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))
	 * )); if(icdOpd.get("diagnosisId")!=null &&
	 * !icdOpd.get("diagnosisId").equals("")&&!icdOpd.get("diagnosisId").equals(
	 * "undefined")) {
	 * disIcdCode.setIcdId(Long.parseLong(String.valueOf(icdOpd.get("diagnosisId")))
	 * ); dischargeDT.add(disIcdCode); session.save(disIcdCode); }
	 * 
	 * 
	 * } } } } }
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception ex) {
	 * 
	 * // System.out.println("Exception e="+ex.); ex.printStackTrace();
	 * tx.rollback(); System.out.println("Exception Message Print ::" +
	 * ex.toString()); return ex.toString(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } } else {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return
	 * "Please Fill Employability Restrictions CheckList"; } return
	 * "Successfully saved"; }
	 * 
	 * @Override public List<PatientMedBoard> getPatientMedBoardReports(Long
	 * visitId) { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(PatientMedBoard.class);
	 * cr.add(Restrictions.eq("visitId", visitId)); List<PatientMedBoard> list =
	 * cr.list(); getHibernateUtils.getHibernateUtlis().CloseConnection(); return
	 * list; }
	 * 
	 * @Override public String saveAmsf15CheckList(HashMap<String, Object> jsondata)
	 * { System.out.println("jsondata "+jsondata.toString()); Long
	 * checkListIdVal=null; Calendar calendar = Calendar.getInstance();
	 * java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(PatientMedBoardChecklist.class); Transaction tx =
	 * session.beginTransaction(); try { if (jsondata.get("checkListValue") != null)
	 * { List<HashMap<String, Object>> listCheckValue = (List<HashMap<String,
	 * Object>>) (Object) jsondata.get("checkListValue"); if (listCheckValue !=
	 * null) {
	 * 
	 * List<PatientMedBoardChecklist> tableCheckPatientMed = new ArrayList<>(); for
	 * (HashMap<String, Object> checkList : listCheckValue) {
	 * if(checkList.get("checklistId")!=null &&checkList.get("checklistId")!="") {
	 * checkListIdVal=Long.valueOf(String.valueOf(checkList.get("checklistId"))); }
	 * if(checkListIdVal!=null) { PatientMedBoardChecklist pmcUpdate
	 * =(PatientMedBoardChecklist) session.get(PatientMedBoardChecklist.class,
	 * checkListIdVal); if(checkList.get("checklistHeaderName")!=null &&
	 * checkList.containsKey("checklistName")) {
	 * pmcUpdate.setPatientId(Long.parseLong(String.valueOf(checkList.get(
	 * "patientId"))));
	 * pmcUpdate.setVisitId(Long.parseLong(String.valueOf(checkList.get("visitId")))
	 * ); pmcUpdate.setChecklistHdCode(String.valueOf(checkList.get(
	 * "checklistHeaderName")));
	 * pmcUpdate.setChecklistName(String.valueOf(checkList.get("checklistName")));
	 * if(checkList.get("checklistValue")!=null &&
	 * !checkList.get("checklistValue").equals("")) {
	 * pmcUpdate.setChecklistValue(String.valueOf(checkList.get("checklistValue")));
	 * } pmcUpdate.setLastChgDate(ourJavaTimestampObject);
	 * tableCheckPatientMed.add(pmcUpdate); session.update(pmcUpdate); } } else {
	 * PatientMedBoardChecklist pmc = new PatientMedBoardChecklist();
	 * if(checkList.get("checklistHeaderName")!=null &&
	 * checkList.containsKey("checklistName")) {
	 * pmc.setPatientId(Long.parseLong(String.valueOf(checkList.get("patientId"))));
	 * pmc.setVisitId(Long.parseLong(String.valueOf(checkList.get("visitId"))));
	 * pmc.setChecklistHdCode(String.valueOf(checkList.get("checklistHeaderName")));
	 * pmc.setChecklistName(String.valueOf(checkList.get("checklistName")));
	 * if(checkList.get("checklistValue")!=null &&
	 * !checkList.get("checklistValue").equals("")) {
	 * pmc.setChecklistValue(String.valueOf(checkList.get("checklistValue"))); }
	 * pmc.setLastChgDate(ourJavaTimestampObject); tableCheckPatientMed.add(pmc);
	 * session.save(pmc); } }
	 * 
	 * } } }
	 * 
	 * 
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception ex) {
	 * 
	 * // System.out.println("Exception e="+ex.); ex.printStackTrace();
	 * tx.rollback(); System.out.println("Exception Message Print ::" +
	 * ex.toString()); return ex.toString(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return "Successfully saved"; }
	 * 
	 * @Override public List<PatientMedBoardChecklist> getCheckList(Long visitId) {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(PatientMedBoardChecklist.class);
	 * cr.add(Restrictions.eq("visitId", visitId));
	 * 
	 * ProjectionList projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("checklistId").as("checklistId"));
	 * projectionList.add(Projections.property("checklistHdCode").as(
	 * "checklistHdCode"));
	 * projectionList.add(Projections.property("checklistName").as("checklistName"))
	 * ;
	 * projectionList.add(Projections.property("checklistValue").as("checklistValue"
	 * ));
	 * 
	 * cr.setProjection(projectionList);
	 * 
	 * List<PatientMedBoardChecklist> list = cr.setResultTransformer(new
	 * AliasToBeanResultTransformer(PatientMedBoardChecklist.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list;
	 * 
	 * }
	 * 
	 * @Override public List<MasEmployeeCategory> getAllEmployeeCategory() {
	 * List<MasEmployeeCategory> employeeCategoryList = new
	 * ArrayList<MasEmployeeCategory>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(MasEmployeeCategory.class); ProjectionList
	 * projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("employeeCategoryId").as(
	 * "employeeCategoryId"));
	 * projectionList.add(Projections.property("employeeCategoryName").as(
	 * "employeeCategoryName")); criteria.setProjection(projectionList);
	 * 
	 * employeeCategoryList = criteria.setResultTransformer(new
	 * AliasToBeanResultTransformer(MasEmployeeCategory.class)).list();
	 * 
	 * }catch(Exception e) { e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * employeeCategoryList; }
	 * 
	 * 
	 * @Override public String dataDigitizationsaveAmsfForm15(HashMap<String,
	 * Object> getjsondata) { Calendar calendar = Calendar.getInstance();
	 * java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); Long
	 * patientCategoryIdVal=null; Long patientCategoryIdValNew=null; Session session
	 * = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(PatientMedBoard.class); Transaction tx =
	 * session.beginTransaction();
	 * 
	 * JSONArray newForm=new JSONArray(getjsondata.get("amsfForm15").toString());
	 * JSONObject jsondata = (JSONObject) newForm.get(0);
	 * 
	 * Long visitId = Long.parseLong((String) jsondata.get("visitId"));
	 * cr.add(Restrictions.eq("visitId", visitId)); PatientMedBoard pmb =
	 * (PatientMedBoard) cr.uniqueResult(); //PatientMedBoardChecklist pmcheck=new
	 * PatientMedBoardChecklist();
	 * 
	 * Criteria cr2 = session.createCriteria(MasMedicalExamReport.class);
	 * cr2.add(Restrictions.eq("visitId", visitId)); MasMedicalExamReport
	 * masExamReport = (MasMedicalExamReport) cr2.uniqueResult();
	 * 
	 * if(masExamReport==null) { masExamReport=new MasMedicalExamReport(); }
	 * 
	 * //Criteria cr1 = session.createCriteria(PatientMedBoardChecklist.class);
	 * //cr1.add(Restrictions.eq("visitId", visitId));
	 * //List<PatientMedBoardChecklist> list=cr1.list(); try {
	 * 
	 * if (pmb != null) {
	 * pmb.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmb.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmb.setAuthority(String.valueOf(jsondata.get("authorityforBoard")));
	 * pmb.setPlaceOfBoard(String.valueOf(jsondata.get("place")));
	 * if(jsondata.get("height")!=null && !jsondata.get("height").equals("")) {
	 * pmb.setHeight(Long.parseLong(String.valueOf(jsondata.get("height")))); }
	 * if(jsondata.get("weight")!=null && !jsondata.get("weight").equals("")) {
	 * pmb.setWeight(Double.parseDouble(String.valueOf(jsondata.get("weight")))); }
	 * if(jsondata.get("dateOfEnrollment")!=null &&
	 * !jsondata.get("dateOfEnrollment").equals("")) {
	 * pmb.setDateOfEnrolment(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "dateOfEnrollment").toString())); } if(jsondata.get("hospitalId")!=null &&
	 * !jsondata.get("hospitalId").equals("")) {
	 * pmb.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))))
	 * ; } pmb.setAddressOnLeave(String.valueOf(jsondata.get("addressOnLeave")));
	 * if(jsondata.get("ceasedDutyOn")!=null &&
	 * !jsondata.get("ceasedDutyOn").equals("")) {
	 * pmb.setCeasedDutyOn(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "ceasedDutyOn").toString())); }
	 * pmb.setRecordOfficeAddress(String.valueOf(jsondata.get("recordOfficeAddress")
	 * )); //pmb.setDisablityAttrService(String.valueOf(jsondata.get(
	 * "disablityAttrService")));
	 * pmb.setDisablityAttrServiceRemark(String.valueOf(jsondata.get(
	 * "disablityAttrServiceRemark")));
	 * //pmb.setDirectlyAttrService((String.valueOf(jsondata.get(
	 * "directlyAttrService"))));
	 * pmb.setDirectlyAttrServiceRemark((String.valueOf(jsondata.get(
	 * "directlyAttrServiceRemark")))); if(jsondata.get("previousDisablement")!=null
	 * && !jsondata.get("previousDisablement").equals("")) {
	 * pmb.setPreviousDisablement(String.valueOf(jsondata.get("previousDisablement")
	 * )); } if(jsondata.get("presentDisablement")!=null &&
	 * !jsondata.get("presentDisablement").equals("")) {
	 * pmb.setPresentDisablement(String.valueOf(jsondata.get("presentDisablement")))
	 * ; } if(jsondata.get("reasonForVarious")!=null &&
	 * !jsondata.get("reasonForVarious").equals("")) {
	 * pmb.setReasonForVarious(String.valueOf(jsondata.get("reasonForVarious"))); }
	 * pmb.setRestrictionRegardingEmp(String.valueOf(jsondata.get(
	 * "restrictionRegardingEmp")));
	 * pmb.setInstructionRemark(String.valueOf(jsondata.get("instructionRemark")));
	 * pmb.setInstructionNote(String.valueOf(jsondata.get("instructionNote")));
	 * if(jsondata.get("signDate")!=null && !jsondata.get("signDate").equals("")) {
	 * pmb.setSigDate(HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "signDate").toString())); }
	 * //pmb.setSignatureIndividual(String.valueOf(jsondata.get(
	 * "signatureIndividual")));
	 * pmb.setMember1(String.valueOf(jsondata.get("member1Name")));
	 * pmb.setMember2(String.valueOf(jsondata.get("member2Name")));
	 * pmb.setMember3(String.valueOf(jsondata.get("member3Name")));
	 * pmb.setMem1Rank(String.valueOf(jsondata.get("mem1Rank")));
	 * pmb.setMem2Rank(String.valueOf(jsondata.get("mem2Rank")));
	 * pmb.setMem3Rank(String.valueOf(jsondata.get("mem3Rank")));
	 * if(getjsondata.get("mbRmsId")!=null && getjsondata.get("mbRmsId")!="") {
	 * pmb.setRidcId(Long.parseLong(String.valueOf(getjsondata.get("mbRmsId")))); }
	 * pmb.setLastChgDate(ourJavaTimestampObject);
	 * pmb.setAaName(String.valueOf(jsondata.get("aaName")));
	 * pmb.setAaRankDest(String.valueOf(jsondata.get("aaRank")));
	 * pmb.setAaPlace(String.valueOf(jsondata.get("aaPlace")));
	 * if(jsondata.get("aaDate")!=null && !jsondata.get("aaDate").equals("")) {
	 * pmb.setAaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.get("aaDate").
	 * toString())); } pmb.setPaName(String.valueOf(jsondata.get("paName")));
	 * pmb.setPaRankDest(String.valueOf(jsondata.get("paRank")));
	 * pmb.setPaPlace(String.valueOf(jsondata.get("paPlace")));
	 * if(jsondata.get("paDate")!=null && !jsondata.get("paDate").equals("")) {
	 * pmb.setPaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.get("paDate").
	 * toString())); }
	 * //pmb.setTypeOfCommission(String.valueOf(jsondata.get("typeOfCommission")));
	 * session.update(pmb); ////////////////////////////////////////List of Medical
	 * Category
	 * /////////////////////////////////////////////////////////////////////////////
	 * /// String preAndPreCatId=""; if (jsondata.get("listOfMedicalCategory") !=
	 * null) { JSONArray listMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategory").toString()); if
	 * (listMedicalCategory != null) {
	 * 
	 * //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for(int
	 * i=0;i<listMedicalCategory.length();i++) { JSONObject mapMedical =
	 * listMedicalCategory.getJSONObject(i);
	 * if(mapMedical.get("patientCategoryId")!=null &&
	 * !mapMedical.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedical.get(
	 * "patientCategoryId"))); } if(patientCategoryIdVal!=null) { PatientMedicalCat
	 * pmtUpdate =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal);
	 * pmtUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); pmtUpdate.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmtUpdate.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"
	 * ))));
	 * pmtUpdate.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); if(mapMedical.get("categoryType")=="Temporary") {
	 * pmtUpdate.setCategoryType("T"); } else
	 * if(mapMedical.get("categoryType")=="Permanent") {
	 * pmtUpdate.setCategoryType("P"); } pmtUpdate.setMbStatus("P"); if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d = HMSUtil
	 * .convertStringTypeDateToDateType(mapMedical.get("categoryDate").toString());
	 * pmtUpdate.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate") !=
	 * null && mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(
	 * mapMedical.get("nextCategoryDate").toString());
	 * pmtUpdate.setNextCategoryDate(d1); }
	 * pmtUpdate.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"
	 * )))); if (mapMedical.get("dateOfOrigin") != null &&
	 * !mapMedical.get("dateOfOrigin").equals("")) { Date dateOfOrigin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmtUpdate.setCategoryDate(dateOfOrigin); }
	 * if(mapMedical.get("placeOfOrigin")!=null &&
	 * mapMedical.get("placeOfOrigin")!="") {
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * } pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * session.update(pmtUpdate);
	 * preAndPreCatId+=""+pmtUpdate.getMedicalCatId()+","; } else {
	 * PatientMedicalCat pmt = new PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmt.setLastChgDate(ourJavaTimestampObject);
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * pmt.setMedicalCategoryId(
	 * Long.parseLong(String.valueOf(mapMedical.get("medicalCategory"))));
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("P"); if (mapMedical.get("categoryDate") != null &&
	 * mapMedical.get("categoryDate") != "") { Date d = HMSUtil
	 * .convertStringTypeDateToDateType(mapMedical.get("categoryDate").toString());
	 * pmt.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate") != null &&
	 * mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(
	 * mapMedical.get("nextCategoryDate").toString()); pmt.setNextCategoryDate(d1);
	 * }
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * if(mapMedical.get("dateOfOrigin")!=null &&
	 * !mapMedical.get("dateOfOrigin").equals("")) {
	 * pmt.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.get(
	 * "dateOfOrigin").toString())); }
	 * pmt.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * pmt.setLastChgDate(ourJavaTimestampObject); session.save(pmt);
	 * preAndPreCatId+=""+pmt.getMedicalCatId()+","; } }
	 * 
	 * } }
	 * 
	 * if (jsondata.get("listOfMedicalCategoryNew") != null) { JSONArray
	 * listMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategoryNew").toString());
	 * 
	 * if (listMedicalCategory != null) { String []
	 * preAndPreCatIdA=preAndPreCatId.split(","); Integer count=0; for(int
	 * i=0;i<listMedicalCategory.length();i++) { JSONObject mapMedical =
	 * listMedicalCategory.getJSONObject(i);
	 * if(mapMedical.get("patientCategoryId")!=null &&
	 * !mapMedical.get("patientCategoryId").equals("")) {
	 * patientCategoryIdValNew=Long.valueOf(String.valueOf(mapMedical.get(
	 * "patientCategoryId"))); } if(patientCategoryIdValNew!=null) {
	 * PatientMedicalCat pmtUpdate =(PatientMedicalCat)
	 * session.get(PatientMedicalCat.class, patientCategoryIdVal);
	 * pmtUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); pmtUpdate.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmtUpdate.setApplyFor("Y"); pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"
	 * ))));
	 * pmtUpdate.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); if(mapMedical.get("categoryType")=="Temporary") {
	 * pmtUpdate.setCategoryType("T"); } else
	 * if(mapMedical.get("categoryType")=="Permanent") {
	 * pmtUpdate.setCategoryType("P"); } pmtUpdate.setMbStatus("A"); if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmtUpdate.setCategoryDate(d); } if
	 * (mapMedical.get("nextCategoryDate") != null &&
	 * mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(
	 * mapMedical.get("nextCategoryDate").toString());
	 * pmtUpdate.setNextCategoryDate(d1); }
	 * pmtUpdate.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"
	 * ))));
	 * 
	 * if (mapMedical.get("dateOfOrigin") != null && mapMedical.get("dateOfOrigin")
	 * != "") { Date dateOfOrigin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmtUpdate.setCategoryDate(dateOfOrigin); }
	 * if(mapMedical.get("placeOfOrigin")!=null &&
	 * mapMedical.get("placeOfOrigin")!="") {
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * }
	 * 
	 * pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setRecommendFlag("Y");
	 * pmtUpdate.setpMedCatFid(Long.parseLong(preAndPreCatIdA[count].trim()));
	 * session.update(pmtUpdate); count++; } else { PatientMedicalCat pmt = new
	 * PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor("Y"); pmt.setLastChgDate(ourJavaTimestampObject);
	 * if(mapMedical.get("diagnosisId")!=null &&
	 * !mapMedical.get("diagnosisId").equals("")) {
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * } if(mapMedical.get("fitType").equals("F")) {
	 * pmt.setpMedCatId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); pmt.setpMedFitFlag("F"); } else {
	 * pmt.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); }
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("A"); if(mapMedical.get("fitType").equals("F")) { if
	 * (mapMedical.get("categoryDate") != null &&
	 * !mapMedical.get("categoryDate").equals("")) { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setpMedCatDate(d); } } else { if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setCategoryDate(d); } } if
	 * (mapMedical.get("nextCategoryDate") != null &&
	 * !mapMedical.get("nextCategoryDate").equals("")) { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("nextCategoryDate").
	 * toString()); pmt.setNextCategoryDate(d1); }
	 * if(mapMedical.get("duration")!=null &&
	 * !mapMedical.get("duration").equals("")) {
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * }
	 * 
	 * pmt.setLastChgDate(ourJavaTimestampObject); pmt.setRecommendFlag("Y");
	 * pmt.setpMedCatFid(Long.parseLong(preAndPreCatIdA[count].trim()));
	 * session.save(pmt); count++; } }
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * 
	 * if (visitId != null) { Visit visit = (Visit) session.get(Visit.class,
	 * visitId); if (visit != null) { visit.setVisitStatus("c");
	 * visit.setExamStatus("F"); session.update(visit); } }
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * PatientMedBoard patientMedBoard = new PatientMedBoard();
	 * patientMedBoard.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * patientMedBoard.setVisitId(Long.parseLong(String.valueOf(jsondata.get(
	 * "visitId"))));
	 * patientMedBoard.setAuthority(String.valueOf(jsondata.get("authorityforBoard")
	 * )); patientMedBoard.setPlaceOfBoard(String.valueOf(jsondata.get("place")));
	 * if(jsondata.get("hospitalId")!=null &&
	 * !jsondata.get("hospitalId").equals("")) {
	 * patientMedBoard.setHospitalId(Long.parseLong(String.valueOf(jsondata.get(
	 * "hospitalId")))); } if(jsondata.get("height")!=null &&
	 * !jsondata.get("height").equals("")) {
	 * patientMedBoard.setHeight(Long.parseLong(String.valueOf(jsondata.get("height"
	 * )))); } if(jsondata.get("weight")!=null &&
	 * !jsondata.get("weight").equals("")) {
	 * patientMedBoard.setWeight(Double.parseDouble(String.valueOf(jsondata.get(
	 * "weight")))); }
	 * 
	 * if(jsondata.get("dateOfEnrollment")!=null &&
	 * !jsondata.get("dateOfEnrollment").equals("")) {
	 * patientMedBoard.setDateOfEnrolment(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("dateOfEnrollment").toString())); }
	 * patientMedBoard.setAddressOnLeave(String.valueOf(jsondata.get(
	 * "addressOnLeave"))); if(jsondata.get("ceasedDutyOn")!=null &&
	 * !jsondata.get("ceasedDutyOn").equals("")) {
	 * patientMedBoard.setCeasedDutyOn(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("ceasedDutyOn").toString())); }
	 * patientMedBoard.setRecordOfficeAddress(String.valueOf(jsondata.get(
	 * "recordOfficeAddress")));
	 * //patientMedBoard.setDisablityAttrService(String.valueOf(jsondata.get(
	 * "disablityAttrService")));
	 * patientMedBoard.setDisablityAttrServiceRemark(String.valueOf(jsondata.get(
	 * "disablityAttrServiceRemark")));
	 * //patientMedBoard.setDirectlyAttrService((String.valueOf(jsondata.get(
	 * "directlyAttrService"))));
	 * patientMedBoard.setDirectlyAttrServiceRemark((String.valueOf(jsondata.get(
	 * "directlyAttrServiceRemark")))); if(jsondata.get("previousDisablement")!=null
	 * && !jsondata.get("previousDisablement").equals("")) {
	 * patientMedBoard.setPreviousDisablement(String.valueOf(jsondata.get(
	 * "previousDisablement"))); } if(jsondata.get("presentDisablement")!=null &&
	 * !jsondata.get("presentDisablement").equals("")) {
	 * patientMedBoard.setPresentDisablement(String.valueOf(jsondata.get(
	 * "presentDisablement"))); } if(jsondata.get("previousDisablement")!=null &&
	 * !jsondata.get("previousDisablement").equals("")) {
	 * patientMedBoard.setReasonForVarious(String.valueOf(jsondata.get(
	 * "reasonForVarious"))); }
	 * patientMedBoard.setRestrictionRegardingEmp(String.valueOf(jsondata.get(
	 * "restrictionRegardingEmp")));
	 * patientMedBoard.setInstructionRemark(String.valueOf(jsondata.get(
	 * "instructionRemark")));
	 * patientMedBoard.setInstructionNote(String.valueOf(jsondata.get(
	 * "instructionNote"))); if(jsondata.get("signDate")!=null &&
	 * !jsondata.get("signDate").equals("")) {
	 * patientMedBoard.setSigDate(HMSUtil.convertStringTypeDateToDateType(jsondata.
	 * get("signDate").toString())); }
	 * //patientMedBoard.setSignatureIndividual(String.valueOf(jsondata.get(
	 * "signatureIndividual")));
	 * patientMedBoard.setMember1(String.valueOf(jsondata.get("member1Name")));
	 * patientMedBoard.setMember2(String.valueOf(jsondata.get("member2Name")));
	 * patientMedBoard.setMember3(String.valueOf(jsondata.get("member3Name")));
	 * patientMedBoard.setMem1Rank(String.valueOf(jsondata.get("mem1Rank")));
	 * patientMedBoard.setMem2Rank(String.valueOf(jsondata.get("mem2Rank")));
	 * patientMedBoard.setMem3Rank(String.valueOf(jsondata.get("mem3Rank")));
	 * patientMedBoard.setLastChgDate(ourJavaTimestampObject);
	 * if(getjsondata.get("mbRmsId")!=null &&
	 * !getjsondata.get("mbRmsId").equals("")) {
	 * patientMedBoard.setRidcId(Long.parseLong(String.valueOf(getjsondata.get(
	 * "mbRmsId")))); }
	 * patientMedBoard.setAaName(String.valueOf(jsondata.get("aaName")));
	 * patientMedBoard.setAaRankDest(String.valueOf(jsondata.get("aaRank")));
	 * patientMedBoard.setAaPlace(String.valueOf(jsondata.get("aaPlace")));
	 * if(jsondata.get("aaDate")!=null && !jsondata.get("aaDate").equals("")) {
	 * patientMedBoard.setAaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.
	 * get("aaDate").toString())); }
	 * patientMedBoard.setPaName(String.valueOf(jsondata.get("paName")));
	 * patientMedBoard.setPaRankDest(String.valueOf(jsondata.get("paRank")));
	 * patientMedBoard.setPaPlace(String.valueOf(jsondata.get("paPlace")));
	 * if(jsondata.get("paDate")!=null && !jsondata.get("paDate").equals("")) {
	 * patientMedBoard.setPaDate(HMSUtil.convertStringTypeDateToDateType(jsondata.
	 * get("paDate").toString())); }
	 * //patientMedBoard.setTypeOfCommission(String.valueOf(jsondata.get(
	 * "typeOfCommission"))); session.save(patientMedBoard);
	 * ////////////////////////////////////////List of Medical Category
	 * /////////////////////////////////////////////////////////////////////////////
	 * /// String preAndPreCatId=""; if (jsondata.get("listOfMedicalCategory") !=
	 * null) { JSONArray listMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategory").toString()); if
	 * (listMedicalCategory != null) {
	 * 
	 * //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for(int
	 * i=0;i<listMedicalCategory.length();i++) { JSONObject mapMedical =
	 * listMedicalCategory.getJSONObject(i);
	 * if(mapMedical.get("patientCategoryId")!=null &&
	 * !mapMedical.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedical.get(
	 * "patientCategoryId"))); } if(patientCategoryIdVal!=null) { PatientMedicalCat
	 * pmtUpdate =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal);
	 * pmtUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); pmtUpdate.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmtUpdate.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"
	 * ))));
	 * pmtUpdate.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); if(mapMedical.get("categoryType")=="Temporary") {
	 * pmtUpdate.setCategoryType("T"); } else
	 * if(mapMedical.get("categoryType")=="Permanent") {
	 * pmtUpdate.setCategoryType("P"); } pmtUpdate.setMbStatus("P"); if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d = HMSUtil
	 * .convertStringTypeDateToDateType(mapMedical.get("categoryDate").toString());
	 * pmtUpdate.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate") !=
	 * null && mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(
	 * mapMedical.get("nextCategoryDate").toString());
	 * pmtUpdate.setNextCategoryDate(d1); }
	 * pmtUpdate.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"
	 * )))); if (mapMedical.get("dateOfOrigin") != null &&
	 * !mapMedical.get("dateOfOrigin").equals("")) { Date dateOfOrigin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmtUpdate.setCategoryDate(dateOfOrigin); }
	 * if(mapMedical.get("placeOfOrigin")!=null &&
	 * mapMedical.get("placeOfOrigin")!="") {
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * } pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * session.update(pmtUpdate);
	 * preAndPreCatId+=""+pmtUpdate.getMedicalCatId()+","; } else {
	 * PatientMedicalCat pmt = new PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor(String.valueOf(mapMedical.get("medicalCatCheck")));
	 * pmt.setLastChgDate(ourJavaTimestampObject);
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * pmt.setMedicalCategoryId(
	 * Long.parseLong(String.valueOf(mapMedical.get("medicalCategory"))));
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("P"); if (mapMedical.get("categoryDate") != null &&
	 * mapMedical.get("categoryDate") != "") { Date d = HMSUtil
	 * .convertStringTypeDateToDateType(mapMedical.get("categoryDate").toString());
	 * pmt.setCategoryDate(d); } if (mapMedical.get("nextCategoryDate") != null &&
	 * mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(
	 * mapMedical.get("nextCategoryDate").toString()); pmt.setNextCategoryDate(d1);
	 * }
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * if(mapMedical.get("dateOfOrigin")!=null &&
	 * !mapMedical.get("dateOfOrigin").equals("")) {
	 * pmt.setDateOfOrigin(HMSUtil.convertStringTypeDateToDateType(mapMedical.get(
	 * "dateOfOrigin").toString())); }
	 * pmt.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * pmt.setLastChgDate(ourJavaTimestampObject); session.save(pmt);
	 * preAndPreCatId+=""+pmt.getMedicalCatId()+","; } }
	 * 
	 * } }
	 * 
	 * if (jsondata.get("listOfMedicalCategoryNew") != null) { JSONArray
	 * listMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategoryNew").toString());
	 * 
	 * if (listMedicalCategory != null) { String []
	 * preAndPreCatIdA=preAndPreCatId.split(","); Integer count=0; for(int
	 * i=0;i<listMedicalCategory.length();i++) { JSONObject mapMedical =
	 * listMedicalCategory.getJSONObject(i);
	 * if(mapMedical.get("patientCategoryId")!=null &&
	 * !mapMedical.get("patientCategoryId").equals("")) {
	 * patientCategoryIdValNew=Long.valueOf(String.valueOf(mapMedical.get(
	 * "patientCategoryId"))); } if(patientCategoryIdValNew!=null) {
	 * PatientMedicalCat pmtUpdate =(PatientMedicalCat)
	 * session.get(PatientMedicalCat.class, patientCategoryIdVal);
	 * pmtUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); pmtUpdate.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmtUpdate.setApplyFor("Y"); pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"
	 * ))));
	 * pmtUpdate.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); if(mapMedical.get("categoryType")=="Temporary") {
	 * pmtUpdate.setCategoryType("T"); } else
	 * if(mapMedical.get("categoryType")=="Permanent") {
	 * pmtUpdate.setCategoryType("P"); } pmtUpdate.setMbStatus("A"); if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmtUpdate.setCategoryDate(d); } if
	 * (mapMedical.get("nextCategoryDate") != null &&
	 * mapMedical.get("nextCategoryDate") != "") { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(
	 * mapMedical.get("nextCategoryDate").toString());
	 * pmtUpdate.setNextCategoryDate(d1); }
	 * pmtUpdate.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"
	 * ))));
	 * 
	 * if (mapMedical.get("dateOfOrigin") != null && mapMedical.get("dateOfOrigin")
	 * != "") { Date dateOfOrigin =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("dateOfOrigin").
	 * toString()); pmtUpdate.setCategoryDate(dateOfOrigin); }
	 * if(mapMedical.get("placeOfOrigin")!=null &&
	 * mapMedical.get("placeOfOrigin")!="") {
	 * pmtUpdate.setPlaceOfOrigin(String.valueOf(mapMedical.get("placeOfOrigin")));
	 * }
	 * 
	 * pmtUpdate.setLastChgDate(ourJavaTimestampObject);
	 * pmtUpdate.setRecommendFlag("Y");
	 * pmtUpdate.setpMedCatFid(Long.parseLong(preAndPreCatIdA[count].trim()));
	 * session.update(pmtUpdate); count++; } else { PatientMedicalCat pmt = new
	 * PatientMedicalCat();
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * pmt.setSystem(String.valueOf(mapMedical.get("system")));
	 * pmt.setApplyFor("Y"); pmt.setLastChgDate(ourJavaTimestampObject);
	 * if(mapMedical.get("diagnosisId")!=null &&
	 * !mapMedical.get("diagnosisId").equals("")) {
	 * pmt.setIcdId(Long.parseLong(String.valueOf(mapMedical.get("diagnosisId"))));
	 * } if(mapMedical.get("fitType").equals("F")) {
	 * pmt.setpMedCatId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); pmt.setpMedFitFlag("F"); } else {
	 * pmt.setMedicalCategoryId(Long.parseLong(String.valueOf(mapMedical.get(
	 * "medicalCategory")))); }
	 * pmt.setCategoryType(String.valueOf(mapMedical.get("categoryType")));
	 * pmt.setMbStatus("A"); if(mapMedical.get("fitType").equals("F")) { if
	 * (mapMedical.get("categoryDate") != null &&
	 * !mapMedical.get("categoryDate").equals("")) { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setpMedCatDate(d); } } else { if
	 * (mapMedical.get("categoryDate") != null && mapMedical.get("categoryDate") !=
	 * "") { Date d =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("categoryDate").
	 * toString()); pmt.setCategoryDate(d); } } if
	 * (mapMedical.get("nextCategoryDate") != null &&
	 * !mapMedical.get("nextCategoryDate").equals("")) { Date d1 =
	 * HMSUtil.convertStringTypeDateToDateType(mapMedical.get("nextCategoryDate").
	 * toString()); pmt.setNextCategoryDate(d1); }
	 * if(mapMedical.get("duration")!=null &&
	 * !mapMedical.get("duration").equals("")) {
	 * pmt.setDuration(Long.parseLong(String.valueOf(mapMedical.get("duration"))));
	 * }
	 * 
	 * pmt.setLastChgDate(ourJavaTimestampObject); pmt.setRecommendFlag("Y");
	 * if(preAndPreCatIdA!=null && preAndPreCatIdA.length > count)
	 * pmt.setpMedCatFid(Long.parseLong(preAndPreCatIdA[count].trim()));
	 * session.save(pmt); count++; } }
	 * 
	 * } }
	 * 
	 * 
	 * //masExamReportId =
	 * Long.parseLong(session.save(masExamReportsSave).toString()); if (visitId !=
	 * null) { Visit visit = (Visit) session.get(Visit.class, visitId); if (visit !=
	 * null) { visit.setVisitStatus("c"); visit.setExamStatus("F");
	 * session.update(visit); } }
	 * 
	 * 
	 * } Long meidcalDocsId=null; if (jsondata.get("uploadOthersDocument") != null)
	 * { JSONArray listOfMedicalDocsNew=new
	 * JSONArray(jsondata.get("uploadOthersDocument").toString());
	 * 
	 * String medicalBoardDocsUpload =
	 * getjsondata.get("medicalBoardDocsUpload").toString(); medicalBoardDocsUpload
	 * = OpdServiceImpl.getReplaceString(medicalBoardDocsUpload); String[]
	 * medicalBoardDocsUploadArrays = medicalBoardDocsUpload.split(",");
	 * 
	 * 
	 * if (listOfMedicalDocsNew != null) { for(int
	 * i=0;i<listOfMedicalDocsNew.length();i++) { JSONObject mapMedicalDocs =
	 * listOfMedicalDocsNew.getJSONObject(i);
	 * if(!mapMedicalDocs.get("meidcalDocsId").equals("")) {
	 * meidcalDocsId=Long.valueOf(String.valueOf(mapMedicalDocs.get("meidcalDocsId")
	 * )); } if(meidcalDocsId!=null) { PatientDocumentDetail pddUpdate
	 * =(PatientDocumentDetail) session.get(PatientDocumentDetail.class,
	 * meidcalDocsId);
	 * pddUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ; pddUpdate.setLastChgDate(ourJavaTimestampObject);
	 * session.update(pddUpdate); } else {
	 * 
	 * PatientDocumentDetail pdd = new PatientDocumentDetail();
	 * pdd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pdd.setDocumentId(Long.parseLong(String.valueOf(mapMedicalDocs.get(
	 * "documentId"))));
	 * pdd.setDocumentRemarks(String.valueOf(mapMedicalDocs.get("documentRemarks")))
	 * ; pdd.setLastChgDate(ourJavaTimestampObject);
	 * pdd.setRidcId(Long.parseLong(medicalBoardDocsUploadArrays[i].trim()));
	 * session.save(pdd);
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * if(jsondata.get("medicalCompositeName")!= null &&
	 * !jsondata.get("medicalCompositeName").equals("")) { PatientMedicalCat pmt =
	 * new PatientMedicalCat();
	 * pmt.setpMedCatId(Long.parseLong(String.valueOf(jsondata.get(
	 * "medicalCompositeName")))); if(jsondata.get("categoryCompositeDate")!=null &&
	 * jsondata.get("categoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get("categoryCompositeDate")
	 * .toString()); pmt.setpMedCatDate(md); }
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * session.save(pmt); }
	 * 
	 * if(jsondata.get("recommedicalCompositeName")!= null &&
	 * !jsondata.get("recommedicalCompositeName").equals("")) {
	 * if(jsondata.get("recommedicalCompositeNamePId")==null &&
	 * jsondata.get("recommedicalCompositeNamePId").equals("")) { PatientMedicalCat
	 * pmt = new PatientMedicalCat();
	 * pmt.setpMedCatId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName"))));
	 * if(jsondata.get("categoryCompositeDate")!=null &&
	 * jsondata.get("recomcategoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pmt.setpMedCatDate(md); }
	 * pmt.setRecommendFlag("C");
	 * pmt.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * pmt.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * session.save(pmt);
	 * 
	 * 
	 * Long patientId=Long.parseLong((String) jsondata.get("patientId"));
	 * if(patientId!=null) { Patient pt = (Patient) session.get(Patient.class,
	 * patientId); if(jsondata.get("recommedicalCompositeName")!=null &&
	 * !jsondata.get("recommedicalCompositeName").equals("")) {
	 * pt.setMedicalCategoryId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName")))); }
	 * if(jsondata.get("recomcategoryCompositeDate")!=null &&
	 * !jsondata.get("recomcategoryCompositeDate").equals("")) { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pt.setMedCatDate(md); }
	 * session.update(pt); }
	 * 
	 * 
	 * } else { if(jsondata.get("recommedicalCompositeNamePId")!=null &&
	 * !jsondata.get("recommedicalCompositeNamePId").equals("")) { Long
	 * RecommnedCatId=Long.parseLong(jsondata.get("recommedicalCompositeNamePId").
	 * toString()); PatientMedicalCat pmtRecomm=(PatientMedicalCat)
	 * session.get(PatientMedicalCat.class, RecommnedCatId);
	 * 
	 * pmtRecomm.setpMedCatId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName"))));
	 * if(jsondata.get("recomcategoryCompositeDate")!=null &&
	 * jsondata.get("recomcategoryCompositeDate")!="") { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pmtRecomm.setpMedCatDate(md); }
	 * pmtRecomm.setRecommendFlag("C");
	 * pmtRecomm.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))))
	 * ;
	 * pmtRecomm.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"
	 * )))); session.update(pmtRecomm); } Long patientId=Long.parseLong((String)
	 * jsondata.get("patientId")); if(patientId!=null) { Patient pt = (Patient)
	 * session.get(Patient.class, patientId);
	 * if(jsondata.get("recommedicalCompositeName")!=null &&
	 * !jsondata.get("recommedicalCompositeName").equals("")) {
	 * pt.setMedicalCategoryId(Long.parseLong(String.valueOf(jsondata.get(
	 * "recommedicalCompositeName")))); }
	 * if(jsondata.get("recomcategoryCompositeDate")!=null &&
	 * !jsondata.get("recomcategoryCompositeDate").equals("")) { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get(
	 * "recomcategoryCompositeDate").toString()); pt.setMedCatDate(md); }
	 * session.update(pt); } } } if(jsondata.has("medicalCompositeName")) {
	 * if(jsondata.get("medicalCompositeName")!= null &&
	 * jsondata.get("medicalCompositeName")!="") { Long
	 * patientId=Long.parseLong((String) jsondata.get("patientId"));
	 * if(patientId!=null) { Patient pt = (Patient) session.get(Patient.class,
	 * patientId); if(jsondata.get("medicalCompositeName")!=null &&
	 * !jsondata.get("medicalCompositeName").equals("")) {
	 * pt.setMedicalCategoryId(Long.parseLong(String.valueOf(jsondata.get(
	 * "medicalCompositeName")))); pt.setFitFlag("C"); }
	 * if(jsondata.get("categoryCompositeDate")!=null &&
	 * !jsondata.get("categoryCompositeDate").equals("")) { Date md =
	 * HMSUtil.convertStringTypeDateToDateType(jsondata.get("categoryCompositeDate")
	 * .toString()); pt.setMedCatDate(md); }
	 * 
	 * session.update(pt); } } } medicalExamDAO. saveUpdateOfPatientDocumentDetail(
	 * getjsondata);
	 * 
	 * masExamReport.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * masExamReport.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"
	 * )))); if(getjsondata.get("unitOrSlip")!=null) { String
	 * unitOrSlip=getjsondata.get("unitOrSlip").toString();
	 * unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
	 * if(StringUtils.isNotEmpty(unitOrSlip)) {
	 * masExamReport.setUnitId(Long.parseLong(unitOrSlip)); } }
	 * if(getjsondata.get("rank")!=null) { String
	 * rank=getjsondata.get("rank").toString();
	 * rank=OpdServiceImpl.getReplaceString(rank); if(StringUtils.isNotEmpty(rank))
	 * { masExamReport.setRankId(Long.parseLong(rank)); } }
	 * if(getjsondata.get("branchOrTrade")!=null) { String
	 * branchOrTrade=getjsondata.get("branchOrTrade").toString();
	 * branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
	 * if(StringUtils.isNotEmpty(branchOrTrade)) {
	 * masExamReport.setBranchId(Long.parseLong(branchOrTrade)); } }
	 * if(jsondata.get("signDate")!=null && !jsondata.get("signDate").equals("")) {
	 * masExamReport.setMediceExamDate(HMSUtil.convertStringTypeDateToDateType(
	 * jsondata.get("signDate").toString())); } //
	 * masExamReport.setStatus(String.valueOf(jsondata.get("saveInDraft"))); //
	 * if(jsondata.containsKey("actionDigiFile")) {
	 * if(jsondata.get("actionDigiFile")!="") { String actionDigiFile =
	 * jsondata.get("actionDigiFile").toString(); actionDigiFile =
	 * OpdServiceImpl.getReplaceString(actionDigiFile);
	 * if(StringUtils.isNotEmpty(actionDigiFile) &&
	 * !actionDigiFile.equalsIgnoreCase("0")) {
	 * 
	 * masExamReport.setStatus(actionDigiFile);
	 * 
	 * if(actionDigiFile.equalsIgnoreCase("ev")||actionDigiFile.equalsIgnoreCase(
	 * "vr")) { if(StringUtils.isNotEmpty(jsondata.get("userId").toString()))
	 * masExamReport.setMoUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * ))));
	 * 
	 * String finalObservationMo = jsondata.get("finalObservationMo").toString();
	 * finalObservationMo = OpdServiceImpl.getReplaceString(finalObservationMo);
	 * masExamReport.setFinalobservation(finalObservationMo);
	 * masExamReport.setMoDate(new Date());
	 * masExamReport.setPetStatus(actionDigiFile); } } }
	 * 
	 * 
	 * //if(jsondata.containsKey("actionDigiFileApproved")) {
	 * if(jsondata.get("actionDigiFileApproved")!="") { String
	 * actionDigiFileApproved = jsondata.get("actionDigiFileApproved").toString();
	 * actionDigiFileApproved =
	 * OpdServiceImpl.getReplaceString(actionDigiFileApproved);
	 * if(StringUtils.isNotEmpty(actionDigiFileApproved) &&
	 * !actionDigiFileApproved.equalsIgnoreCase("0")) {
	 * masExamReport.setStatus(actionDigiFileApproved);
	 * 
	 * if(actionDigiFileApproved.equalsIgnoreCase("ea")||actionDigiFileApproved.
	 * equalsIgnoreCase("ar")) {
	 * if(StringUtils.isNotEmpty(jsondata.get("userId").toString()))
	 * masExamReport.setCmUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * ))));
	 * 
	 * String finalObservationRMo = jsondata.get("finalObservationRMo").toString();
	 * finalObservationRMo = OpdServiceImpl.getReplaceString(finalObservationRMo);
	 * masExamReport.setAaFinalObservation(finalObservationRMo);
	 * masExamReport.setCmDate(new Date());
	 * 
	 * if(actionDigiFileApproved.equalsIgnoreCase("ea")) { Visit visit = (Visit)
	 * session.get(Visit.class, visitId); if (visit != null) {
	 * visit.setVisitStatus("C"); visit.setExamStatus("C");
	 * System.out.println("Closedddd"); session.update(visit); } PatientMedicalCat
	 * pmt = (PatientMedicalCat) session.get(PatientMedicalCat.class, visitId); if
	 * (pmt != null) { pmt.setMbStatus("C"); System.out.println("Closedddd");
	 * session.update(pmt); }
	 * 
	 * } if (jsondata.get("listOfMedicalCategoryUpdate") != null) { JSONArray
	 * listOfMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategoryUpdate").toString()); if
	 * (listOfMedicalCategory != null) {
	 * 
	 * //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for(int
	 * i=0;i<listOfMedicalCategory.length();i++) { JSONObject mapMedicalOrigin =
	 * listOfMedicalCategory.getJSONObject(i);
	 * if(mapMedicalOrigin.get("patientCategoryId")!=null &&
	 * !mapMedicalOrigin.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedicalOrigin.get(
	 * "patientCategoryId"))); } if(patientCategoryIdVal!=null) { PatientMedicalCat
	 * pmtUpdate =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal); pmtUpdate.setMbStatus("C"); session.update(pmtUpdate);
	 * 
	 * } } }
	 * 
	 * } if (jsondata.get("listOfMedicalCategoryUpdateNew") != null) { JSONArray
	 * listOfMedicalCategory=new
	 * JSONArray(jsondata.get("listOfMedicalCategoryUpdateNew").toString()); if
	 * (listOfMedicalCategory != null) {
	 * 
	 * //for (HashMap<String, Object> mapMedical : listMedicalCategory) { for(int
	 * i=0;i<listOfMedicalCategory.length();i++) { JSONObject mapMedicalOrigin =
	 * listOfMedicalCategory.getJSONObject(i);
	 * if(mapMedicalOrigin.get("patientCategoryId")!=null &&
	 * !mapMedicalOrigin.get("patientCategoryId").equals("")) {
	 * patientCategoryIdVal=Long.valueOf(String.valueOf(mapMedicalOrigin.get(
	 * "patientCategoryId"))); } if(patientCategoryIdVal!=null) { PatientMedicalCat
	 * pmtUpdate =(PatientMedicalCat) session.get(PatientMedicalCat.class,
	 * patientCategoryIdVal); pmtUpdate.setMbStatus("C"); session.update(pmtUpdate);
	 * 
	 * } } }
	 * 
	 * }
	 * 
	 * if(actionDigiFileApproved.equalsIgnoreCase("ea")) { String visitIds =
	 * jsondata.get("visitId").toString(); visitId =
	 * OpdServiceImpl.getReplaceString(visitId); updateVisitStatus(visitId, "c"); }
	 * 
	 * } } }
	 * 
	 * 
	 * String saveInDraft =String.valueOf(jsondata.get("saveInDraft")); if
	 * (StringUtils.isNotEmpty(saveInDraft) &&
	 * (saveInDraft.equalsIgnoreCase("et")||saveInDraft.equalsIgnoreCase("es"))){
	 * masExamReport.setStatus(saveInDraft);
	 * if(StringUtils.isNotEmpty(jsondata.get("userId").toString()))
	 * masExamReport.setMaUserId(Long.parseLong(String.valueOf(jsondata.get("userId"
	 * )))); String finalObservationMa =
	 * jsondata.get("finalObservationMa").toString(); finalObservationMa =
	 * OpdServiceImpl.getReplaceString(finalObservationMa); //masExamReport.set
	 * masExamReport.setMaDate(new Date()); }
	 * 
	 * 
	 * 
	 * session.saveOrUpdate(masExamReport); Long opdId = null;
	 * if(jsondata.has("diagnosisName")) { Criteria crOpd =
	 * session.createCriteria(OpdPatientDetail.class);
	 * crOpd.add(Restrictions.eq("visitId", visitId)); OpdPatientDetail opdlist =
	 * (OpdPatientDetail) crOpd.uniqueResult(); if (opdlist != null) {
	 * opdlist.setIcdDiagnosis(String.valueOf(jsondata.get("diagnosisName")));
	 * opdlist.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))
	 * ));
	 * opdlist.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * opdlist.setLastChgDate(ourJavaTimestampObject); session.update(opdlist);
	 * opdId = opdlist.getOpdPatientDetailsId(); } else { OpdPatientDetail
	 * opddetails = new OpdPatientDetail();
	 * opddetails.setIcdDiagnosis(String.valueOf(jsondata.get("diagnosisName")));
	 * opddetails.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * opddetails.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId")))
	 * ); opddetails.setLastChgDate(ourJavaTimestampObject); opdId =
	 * Long.parseLong(session.save(opddetails).toString()); } }
	 * ///////////////////////// Discharge ICD Code Details
	 * Entry///////////////////////// ///////////////////////////// if
	 * (jsondata.has("diagnosisData")) { if (jsondata.get("diagnosisData") != null)
	 * { //List<HashMap<String, Object>> listIcdValue = (List<HashMap<String,
	 * Object>>) (Object) jsondata.get("diagnosisData"); JSONArray listIcdValue=new
	 * JSONArray(jsondata.get("diagnosisData").toString()); Long icdId=null; if
	 * (listIcdValue != null) { List<DischargeIcdCode> dischargeDT = new
	 * ArrayList<>(); for(int i=0;i<listIcdValue.length();i++) { JSONObject icdOpd =
	 * listIcdValue.getJSONObject(i); // Criteria crDischarge =
	 * session.createCriteria(DischargeIcdCode.class);
	 * //crDischarge.add(Restrictions.eq("visitId", visitId)); //DischargeIcdCode
	 * dischargeIcdCode = (DischargeIcdCode) crDischarge.uniqueResult();
	 * DischargeIcdCode dischargePrescHd = null; if(icdOpd.get("diagnosisId")!=null
	 * && !icdOpd.get("diagnosisId").equals("") &&
	 * !icdOpd.get("diagnosisId").equals("undefined")) {
	 * icdId=Long.parseLong(String.valueOf(icdOpd.get("diagnosisId")));
	 * dischargePrescHd = getDischargeIcd(icdId, visitId); }
	 * 
	 * if(dischargePrescHd!=null) { Long
	 * icdUpdate=dischargePrescHd.getDischargeIcdCodeId(); DischargeIcdCode
	 * discUpdate =(DischargeIcdCode) session.get(DischargeIcdCode.class,
	 * icdUpdate);
	 * discUpdate.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * discUpdate.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId")))
	 * ); discUpdate.setLastChgDate(ourJavaTimestampObject);
	 * discUpdate.setOpdPatientDetailsId(opdId); if(icdOpd.get("diagnosisId")!=null
	 * && !icdOpd.get("diagnosisId").equals("") &&
	 * !icdOpd.get("diagnosisId").equals("undefined")) {
	 * discUpdate.setIcdId(Long.parseLong(String.valueOf(icdOpd.get("diagnosisId")))
	 * ); dischargeDT.add(discUpdate); session.update(discUpdate); }
	 * 
	 * } else { DischargeIcdCode disIcdCode = new DischargeIcdCode();
	 * disIcdCode.setPatientId(Long.parseLong(String.valueOf(jsondata.get(
	 * "patientId"))));
	 * disIcdCode.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId")))
	 * ); disIcdCode.setLastChgDate(ourJavaTimestampObject);
	 * disIcdCode.setOpdPatientDetailsId(opdId); if(icdOpd.get("diagnosisId")!=null
	 * && !icdOpd.get("diagnosisId").equals("")&&!icdOpd.get("diagnosisId").equals(
	 * "undefined")) {
	 * disIcdCode.setIcdId(Long.parseLong(String.valueOf(icdOpd.get("diagnosisId")))
	 * ); dischargeDT.add(disIcdCode); session.save(disIcdCode); }
	 * 
	 * 
	 * } } } } }
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception ex) {
	 * 
	 * // System.out.println("Exception e="+ex.); ex.printStackTrace();
	 * tx.rollback(); System.out.println("Exception Message Print ::" +
	 * ex.toString()); return ex.toString(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return "Successfully saved"; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public HashMap<String, Object>
	 * dataDigitizationSaveSpecialistOpinion(HashMap<String, Object> jsondata) {
	 * 
	 * HashMap<String,Object>mapval = new HashMap<String, Object>();
	 * 
	 * Date currentDate = ProjectUtils.getCurrentDate(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(OpdPatientDetail.class); Criteria cr1 =
	 * session.createCriteria(MasMedicalExamReport.class); Transaction tx =
	 * session.beginTransaction(); String
	 * visitIds=jsondata.get("visitId").toString();
	 * visitIds=OpdServiceImpl.getReplaceStringOnlyLastAndFirst(visitIds);
	 * 
	 * 
	 * Long visitId = Long.parseLong(visitIds);
	 * 
	 * Long patientId; Long hospitalId; Long userId; Long opdId; Long headerNivId =
	 * null; Timestamp ts =null; Calendar calendar = Calendar.getInstance();
	 * java.sql.Timestamp ourJavaTimestampObject = new
	 * java.sql.Timestamp(calendar.getTime().getTime()); String procedureType =
	 * null;
	 * 
	 * cr.add(Restrictions.eq("visitId", visitId));
	 * cr1.add(Restrictions.eq("visitId", visitId));
	 * 
	 * Criteria cr2 = session.createCriteria(OpdPatientHistory.class);
	 * cr2.add(Restrictions.eq("visitId", visitId)); OpdPatientHistory
	 * opdPatientHistory = (OpdPatientHistory) cr2.uniqueResult();
	 * 
	 * if(opdPatientHistory==null) { opdPatientHistory=new OpdPatientHistory(); }
	 * 
	 * String
	 * dynamicUploadInvesIdAndfile=jsondata.get("dynamicUploadInvesIdAndfileNew").
	 * toString(); dynamicUploadInvesIdAndfile=OpdServiceImpl.getReplaceString(
	 * dynamicUploadInvesIdAndfile);
	 * dynamicUploadInvesIdAndfile=dynamicUploadInvesIdAndfile.replaceAll("\"", "");
	 * String[] dynamicUploadInvesIdAndfiles =
	 * dynamicUploadInvesIdAndfile.split(",");
	 * 
	 * String investigationIdValue=jsondata.get("investigationIdValue").toString();
	 * investigationIdValue=OpdServiceImpl.getReplaceString(investigationIdValue);
	 * String[] investigationIdValues = investigationIdValue.split(",");
	 * 
	 * String totalLengthDigiFile=jsondata.get("totalLengthDigiFile").toString();
	 * totalLengthDigiFile=OpdServiceImpl.getReplaceString(totalLengthDigiFile);
	 * 
	 * String rmdIdVal=patientRegistrationServiceImpl.getFinalInvestigationValue(
	 * investigationIdValues, dynamicUploadInvesIdAndfiles,totalLengthDigiFile) ;
	 * 
	 * String[] rangeValues=null; if(jsondata.containsKey("range")) { String
	 * rangeValue=jsondata.get("range").toString();
	 * rangeValue=OpdServiceImpl.getReplaceString(rangeValue); rangeValues =
	 * rangeValue.split(","); }
	 * 
	 * 
	 * String[] rmsIdFinalValfileUp=null; rmsIdFinalValfileUp = rmdIdVal.split(",");
	 * 
	 * 
	 * String meRmsId=jsondata.get("meRmsId").toString();
	 * meRmsId=OpdServiceImpl.getReplaceString(meRmsId); String[] meRmsIds =
	 * meRmsId.split(",");
	 * 
	 * String patientId1 = jsondata.get("patientId").toString();
	 * if(StringUtils.isNotEmpty(patientId1)) { patientId1 =
	 * OpdServiceImpl.getReplaceString(patientId1); //
	 * masMedicalExamReprt.setPatientId(Long.parseLong(patientId)); }
	 * 
	 * 
	 * String hospitalId1 = jsondata.get("hospitalId").toString();
	 * if(StringUtils.isNotEmpty(hospitalId1)) { hospitalId1 =
	 * OpdServiceImpl.getReplaceString(hospitalId1);
	 * //masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId)); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * String userId1 = jsondata.get("userId").toString(); userId1 =
	 * OpdServiceImpl.getReplaceString(userId1);
	 * 
	 * MasMedicalExamReport masMedicalExamReprt=new MasMedicalExamReport();
	 * JSONArray jarr=new JSONArray(jsondata.get("jsondata").toString()); JSONObject
	 * getJsonData = (JSONObject) jarr.get(0);
	 * 
	 * String hospitalId1 =""; if(getJsonData.has("hospitalForDigi")) { String
	 * hospitalForDigi = getJsonData.get("hospitalForDigi").toString();
	 * hospitalForDigi = OpdServiceImpl.getReplaceString(hospitalForDigi); if
	 * (StringUtils.isNotEmpty(hospitalForDigi) &&
	 * !hospitalForDigi.equalsIgnoreCase("0")) {
	 * //masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalForDigi));
	 * hospitalId1=hospitalForDigi; } else { //Change will be do hospitalId=null; }
	 * }
	 * 
	 * 
	 * String dateOfMB=getJsonData.get("dateOfEnrollment").toString();
	 * if(StringUtils.isNotEmpty(dateOfMB) && dateOfMB!=null) { Date
	 * medicalCompositeDateValue = null; medicalCompositeDateValue =
	 * HMSUtil.convertStringTypeDateToDateType(dateOfMB); ts = new
	 * Timestamp(medicalCompositeDateValue.getTime()); }
	 * 
	 * medicalExamDAO.updateInvestigationDgResultForDigiUpload(jsondata, patientId1,
	 * hospitalId1, userId1,
	 * masMedicalExamReprt,rangeValues,rmsIdFinalValfileUp,ts);
	 * 
	 * 
	 * //medicalExamDAO. saveUpdateForReferalforMeDigiFileUpload(
	 * masMedicalExamReprt, jsondata);
	 * 
	 * OpdPatientDetail opdDetails = (OpdPatientDetail) cr.uniqueResult();
	 * MasMedicalExamReport mmer=(MasMedicalExamReport) cr1.uniqueResult(); try { //
	 * JSONObject jsonVal1 = new JSONObject(sdfsd); // JSONArray jarr1=new
	 * JSONArray(getJsonData.get("listofReferallHDNew").toString()); // JSONObject
	 * getJsonDataHd = (JSONObject) jarr1.get(0);
	 * 
	 * 
	 * // JSONArray jarr2=new
	 * JSONArray(getJsonDataHd.get("listofReferalDT").toString()); //JSONObject
	 * getJsonDataDt = (JSONObject) jarr2.get(0);
	 * 
	 * //List<HashMap<String, Object>> list1 = (List<HashMap<String, Object>>)
	 * getJsonData.get("listofReferallHDNew");
	 * 
	 * if (opdDetails != null) {
	 * opdDetails.setPatientId(Long.parseLong(String.valueOf(getJsonData.get(
	 * "patientId"))));
	 * opdDetails.setVisitId(Long.parseLong(String.valueOf(getJsonData.get("visitId"
	 * )))); opdDetails.setOpdDate(ourJavaTimestampObject);
	 * //opdDetails.setIcdDiagnosis(String.valueOf(getJsonData.get("icdDiagnosis")))
	 * ; opdDetails.setHeight(getJsonData.get("height").toString());
	 * opdDetails.setWeight(getJsonData.get("weight").toString());
	 * opdDetails.setIdealWeight(getJsonData.get("idealWeight").toString()); if
	 * (getJsonData.get("varation") != null &&
	 * !getJsonData.get("varation").equals("")) { Double bd = new
	 * Double(getJsonData.get("varation").toString()); opdDetails.setVaration(bd); }
	 * opdDetails.setTemperature(getJsonData.get("temperature").toString());
	 * opdDetails.setBpSystolic(getJsonData.get("bp").toString());
	 * opdDetails.setBpDiastolic(getJsonData.get("bp1").toString());
	 * opdDetails.setPulse(getJsonData.get("pulse").toString());
	 * opdDetails.setSpo2(getJsonData.get("spo2").toString());
	 * opdDetails.setBmi(getJsonData.get("bmi").toString());
	 * opdDetails.setRr(getJsonData.get("rr").toString());
	 * opdDetails.setPollor(String.valueOf(getJsonData.get("pallar")));
	 * opdDetails.setEdema(String.valueOf(getJsonData.get("edema")));
	 * opdDetails.setCyanosis(String.valueOf(getJsonData.get("cyanosis")));
	 * opdDetails.setIcterus(String.valueOf(getJsonData.get("icterus")));
	 * opdDetails.setHairNail(String.valueOf(getJsonData.get("hairnail")));
	 * opdDetails.setLymphNode(String.valueOf(getJsonData.get("lymphNode")));
	 * opdDetails.setClubbing(String.valueOf(getJsonData.get("clubbing")));
	 * opdDetails.setGcs(String.valueOf(getJsonData.get("gcs")));
	 * opdDetails.setTremors(String.valueOf(getJsonData.get("tremors")));
	 * opdDetails.setGeneralOther(String.valueOf(getJsonData.get("generalOther")));
	 * opdDetails.setLastChgDate(ourJavaTimestampObject);
	 * opdDetails.setLastChgBy(Long.parseLong(getJsonData.get("userId").toString()))
	 * ; opdDetails.setHospitalId(Long.parseLong(String.valueOf(getJsonData.get(
	 * "hospitalId")))); session.update(opdDetails); opdId =
	 * opdDetails.getOpdPatientDetailsId();
	 * 
	 * 
	 * /////////////////////opd patient history /////////////////////////////
	 * 
	 * opdPatientHistory.setVisitId(Long.parseLong(getJsonData.get("visitId").
	 * toString()));
	 * opdPatientHistory.setPatientId(Long.parseLong(getJsonData.get("patientId").
	 * toString()));
	 * opdPatientHistory.setHospitalId(Long.parseLong(getJsonData.get("hospitalId").
	 * toString()));
	 * opdPatientHistory.setLastChgBy(Long.parseLong(getJsonData.get("userId").
	 * toString()));
	 * opdPatientHistory.setChiefComplain(String.valueOf(getJsonData.get(
	 * "chiefComplain"))); opdPatientHistory.setLastChgDate(ourJavaTimestampObject);
	 * session.saveOrUpdate(opdPatientHistory);
	 * 
	 * //////////////////////MasMedicalExamReport ////////////////////////////// if
	 * (mmer != null) {
	 * mmer.setFinalobservation(String.valueOf(getJsonData.get("finalobservation")))
	 * ; mmer.setVisitId(Long.parseLong(getJsonData.get("visitId").toString()));
	 * mmer.setPatientId(Long.parseLong(getJsonData.get("patientId").toString()));
	 * mmer.setLastChgBy(Long.parseLong(getJsonData.get("userId").toString()));
	 * mmer.setLastChgDate(ourJavaTimestampObject); session.saveOrUpdate(mmer); }
	 * 
	 * ///////////// Referal Details Section //////////////////////////////// if
	 * (getJsonDataHd != null) { JSONArray list=new
	 * JSONArray(getJsonData.get("listofReferallHDNew").toString()); if (list !=
	 * null) { for(int i=0;i<list.length();i++) { JSONObject map =
	 * list.getJSONObject(i); JSONArray jarray =
	 * map.getJSONArray("listofReferalDT");
	 * 
	 * 
	 * String extId = (String) map.get("extHospitalId"); if(extId!=null &&
	 * extId!="") { Long empID = Long.parseLong(extId); String referralNote =
	 * (String) map.get("referralNote"); patientId = Long.parseLong((String)
	 * map.get("patientId")); hospitalId =
	 * Long.parseLong(String.valueOf(map.get("hospitalId"))); // String
	 * treatmentType=(String)map.get("treatmentType"); ReferralPatientHd header =
	 * null; if (empID != null && opdId != null) { header =
	 * getReferralPatientHdByExeHosAndOpdPd(empID, opdId); } Long id = null; if
	 * (header == null) {
	 * 
	 * header = new ReferralPatientHd(); header.setPatientId(patientId);
	 * header.setHospitalId(hospitalId); header.setExtHospitalId(empID);
	 * header.setDoctorId(Long.parseLong(String.valueOf(getJsonData.get("userId"))))
	 * ;
	 * header.setLastChgBy(Long.parseLong(String.valueOf(getJsonData.get("userId")))
	 * ); header.setLastChgDate(ourJavaTimestampObject);
	 * header.setReferralIniDate(currentDate); header.setTreatmentType("E");
	 * header.setReferralIniDate(currentDate);
	 * header.setReferralNote(String.valueOf(referralNote)); header.setStatus("W");
	 * header.setOpdPatientDetailsId(opdId); id =
	 * Long.parseLong(session.save(header).toString()); } else { id =
	 * header.getRefrealHdId(); } Long extHosId = header.getExtHospitalId();
	 * System.out.println(extHosId); String referalRmsId =
	 * jsondata.get("referalRmsId").toString(); referalRmsId =
	 * OpdServiceImpl.getReplaceString(referalRmsId); String[]
	 * referalRmsIdValueArrays = referalRmsId.split(","); int count=0; for(int
	 * j=0;j<jarray.length();j++) { JSONObject diagnosisMap =
	 * jarray.getJSONObject(j); if(diagnosisMap.get("diagnosisId")!=null &&
	 * diagnosisMap.get("diagnosisId")!="") { Long diagnosisId =
	 * Long.parseLong((String) diagnosisMap.get("diagnosisId")); ReferralPatientDt
	 * refDt = new ReferralPatientDt(); refDt.setDiagnosisId(diagnosisId);
	 * refDt.setLastChgDate(ourJavaTimestampObject);
	 * refDt.setExtDepartment(String.valueOf(diagnosisMap.get("extDepartment")));
	 * refDt.setInstruction(String.valueOf(diagnosisMap.get("instruction")));
	 * if(referalRmsIdValueArrays[count]!=null &&
	 * !referalRmsIdValueArrays[count].equalsIgnoreCase("0")) {
	 * refDt.setRidcId(Long.parseLong(referalRmsIdValueArrays[count])); }
	 * refDt.setRefrealHdId(id); session.save(refDt); count++; } } } }
	 * 
	 * }
	 * 
	 * } medicalExamDAO.saveUpdateForReferalforMeDigiFileUpload(
	 * masMedicalExamReprt, jsondata,hospitalId1);
	 * 
	 * ///////////// Investigation Result Details Section
	 * ////////////////////////////////
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * OpdPatientDetail masExamReports = new OpdPatientDetail();
	 * masExamReports.setPatientId(Long.parseLong(String.valueOf(getJsonData.get(
	 * "patientId"))));
	 * masExamReports.setVisitId(Long.parseLong(String.valueOf(getJsonData.get(
	 * "visitId"))));
	 * masExamReports.setHeight(getJsonData.get("height").toString());
	 * masExamReports.setWeight(getJsonData.get("weight").toString());
	 * masExamReports.setIdealWeight(getJsonData.get("idealWeight").toString()); if
	 * (getJsonData.get("varation") != null &&
	 * !getJsonData.get("varation").equals("")) { Double bd = new
	 * Double(getJsonData.get("varation").toString());
	 * masExamReports.setVaration(bd); }
	 * masExamReports.setTemperature(getJsonData.get("temperature").toString());
	 * masExamReports.setBpSystolic(getJsonData.get("bp").toString());
	 * masExamReports.setBpDiastolic(getJsonData.get("bp1").toString());
	 * masExamReports.setPulse(getJsonData.get("pulse").toString());
	 * masExamReports.setSpo2(getJsonData.get("spo2").toString());
	 * masExamReports.setBmi(getJsonData.get("bmi").toString());
	 * masExamReports.setRr(getJsonData.get("rr").toString());
	 * masExamReports.setPollor(String.valueOf(getJsonData.get("pallar")));
	 * masExamReports.setEdema(String.valueOf(getJsonData.get("edema")));
	 * masExamReports.setCyanosis(String.valueOf(getJsonData.get("cyanosis")));
	 * masExamReports.setIcterus(String.valueOf(getJsonData.get("icterus")));
	 * masExamReports.setHairNail(String.valueOf(getJsonData.get("hairnail")));
	 * masExamReports.setLymphNode(String.valueOf(getJsonData.get("lymphNode")));
	 * masExamReports.setClubbing(String.valueOf(getJsonData.get("clubbing")));
	 * masExamReports.setGcs(String.valueOf(getJsonData.get("gcs")));
	 * masExamReports.setTremors(String.valueOf(getJsonData.get("tremors")));
	 * masExamReports.setGeneralOther(String.valueOf(getJsonData.get("generalOther")
	 * )); masExamReports.setLastChgDate(ourJavaTimestampObject);
	 * masExamReports.setLastChgBy(Long.parseLong(String.valueOf(getJsonData.get(
	 * "userId").toString()))); opdId =
	 * Long.parseLong(session.save(masExamReports).toString());
	 * 
	 * 
	 * /////////////////////opd patient history /////////////////////////////
	 * 
	 * opdPatientHistory.setVisitId(Long.parseLong(getJsonData.get("visitId").
	 * toString()));
	 * opdPatientHistory.setPatientId(Long.parseLong(getJsonData.get("patientId").
	 * toString()));
	 * opdPatientHistory.setHospitalId(Long.parseLong(getJsonData.get("hospitalId").
	 * toString()));
	 * opdPatientHistory.setLastChgBy(Long.parseLong(getJsonData.get("userId").
	 * toString()));
	 * opdPatientHistory.setChiefComplain(String.valueOf(getJsonData.get(
	 * "chiefComplain"))); opdPatientHistory.setLastChgDate(ourJavaTimestampObject);
	 * session.saveOrUpdate(opdPatientHistory);
	 * 
	 * //////////////////////MasMedicalExamReport ////////////////////////////// if
	 * (mmer != null) {
	 * mmer.setFinalobservation(String.valueOf(getJsonData.get("finalobservation")))
	 * ; mmer.setVisitId(Long.parseLong(getJsonData.get("visitId").toString()));
	 * mmer.setPatientId(Long.parseLong(getJsonData.get("patientId").toString()));
	 * mmer.setLastChgBy(Long.parseLong(getJsonData.get("userId").toString()));
	 * mmer.setLastChgDate(ourJavaTimestampObject);
	 * 
	 * session.update(mmer); } else { MasMedicalExamReport masEReports = new
	 * MasMedicalExamReport();
	 * masEReports.setVisitId(Long.parseLong(getJsonData.get("visitId").toString()))
	 * ;
	 * masEReports.setPatientId(Long.parseLong(getJsonData.get("patientId").toString
	 * ())); masEReports.setFinalobservation(String.valueOf(getJsonData.get(
	 * "finalobservation")));
	 * masEReports.setLastChgBy(Long.parseLong(getJsonData.get("userId").toString())
	 * ); masEReports.setLastChgDate(ourJavaTimestampObject);
	 * session.save(masEReports); }
	 * 
	 * medicalExamDAO.saveUpdateForReferalforMeDigiFileUpload( masMedicalExamReprt,
	 * jsondata,getJsonData.get("hospitalId").toString()); ///////////// Referal
	 * Details Section //////////////////////////////// if (getJsonDataHd != null) {
	 * JSONArray list=new
	 * JSONArray(getJsonData.get("listofReferallHDNew").toString()); if (list !=
	 * null) { for(int i=0;i<list.length();i++) { JSONObject map =
	 * list.getJSONObject(i); JSONArray jarray =
	 * map.getJSONArray("listofReferalDT");
	 * 
	 * 
	 * String extId = (String) map.get("extHospitalId"); if(extId!=null &&
	 * extId!="") { Long empID = Long.parseLong(extId); String referralNote =
	 * (String) map.get("referralNote"); patientId = Long.parseLong((String)
	 * map.get("patientId")); hospitalId =
	 * Long.parseLong(String.valueOf(map.get("hospitalId"))); // String
	 * treatmentType=(String)map.get("treatmentType"); ReferralPatientHd header =
	 * null; if (empID != null && opdId != null) { header =
	 * getReferralPatientHdByExeHosAndOpdPd(empID, opdId); } Long id = null; if
	 * (header == null) {
	 * 
	 * header = new ReferralPatientHd(); header.setPatientId(patientId);
	 * header.setHospitalId(hospitalId); header.setExtHospitalId(empID);
	 * header.setDoctorId(Long.parseLong(String.valueOf(getJsonData.get("userId"))))
	 * ;
	 * header.setLastChgBy(Long.parseLong(String.valueOf(getJsonData.get("userId")))
	 * ); header.setLastChgDate(ourJavaTimestampObject);
	 * header.setReferralIniDate(currentDate); header.setTreatmentType("E");
	 * header.setReferralIniDate(currentDate);
	 * header.setReferralNote(String.valueOf(referralNote)); header.setStatus("W");
	 * header.setOpdPatientDetailsId(opdId); id =
	 * Long.parseLong(session.save(header).toString()); } else { id =
	 * header.getRefrealHdId(); } Long extHosId = header.getExtHospitalId();
	 * System.out.println(extHosId); String referalRmsId =
	 * jsondata.get("referalRmsId").toString(); referalRmsId =
	 * OpdServiceImpl.getReplaceString(referalRmsId); String[]
	 * referalRmsIdValueArrays = referalRmsId.split(","); int count=0; for(int
	 * j=0;j<jarray.length();j++) { JSONObject diagnosisMap =
	 * jarray.getJSONObject(j); if(diagnosisMap.get("diagnosisId")!=null &&
	 * diagnosisMap.get("diagnosisId")!="") { Long diagnosisId =
	 * Long.parseLong((String) diagnosisMap.get("diagnosisId")); ReferralPatientDt
	 * refDt = new ReferralPatientDt(); refDt.setDiagnosisId(diagnosisId);
	 * refDt.setLastChgDate(ourJavaTimestampObject);
	 * refDt.setExtDepartment(String.valueOf(diagnosisMap.get("extDepartment")));
	 * refDt.setInstruction(String.valueOf(diagnosisMap.get("instruction")));
	 * if(referalRmsIdValueArrays[count]!=null &&
	 * !referalRmsIdValueArrays[count].equalsIgnoreCase("0")) {
	 * refDt.setRidcId(Long.parseLong(referalRmsIdValueArrays[count])); }
	 * refDt.setRefrealHdId(id); session.save(refDt); count++; } } } }
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * tx.commit();
	 * 
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); tx.rollback();
	 * System.out.println("Exception Message Print ::" + ex.toString());
	 * mapval.put("error",ex.toString()); return mapval; } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * mapval.put("Successfully saved","Successfully saved"); return mapval; }
	 * 
	 * @Override public List<PatientMedicalCat>
	 * getPatientMedicalBoardDetailsCategory(Long patientId) { Criteria cr = null;
	 * List<PatientMedicalCat> list = null; int count = 0; Map<String, Object> map =
	 * new HashMap<String, Object>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(PatientMedicalCat.class).add(Restrictions.eq(
	 * "patientId", patientId)) .add(Restrictions.or(Restrictions.eq("mbStatus",
	 * "P").ignoreCase(), Restrictions.eq("mbStatus", "P").ignoreCase())); list =
	 * cr.list(); count = list.size(); System.out.println(count);
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * @Override public Long getInvestigationsVisitId(Long patientId) { BigDecimal
	 * visitId=null; Long visitIdd=null; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); StringBuilder sbQuery =
	 * new StringBuilder(); sbQuery.append(" select MAX(VISIT_ID)");
	 * sbQuery.append("	from "+visit+" inner join "
	 * +MAS_APPOINTMENT_TYPE+" on APPOINTMENT_TYPE_CODE='MB' " );
	 * sbQuery.append("	and visit_status='C'  and VISIT_FLAG='R'  ");
	 * 
	 * sbQuery.append(" and patient_id=:patientId"); Query query =
	 * session.createSQLQuery(sbQuery.toString());
	 * 
	 * query.setParameter("patientId", patientId);
	 * 
	 * 
	 * List listVal = query.list(); if(CollectionUtils.isNotEmpty(listVal)) {
	 * visitId=(BigDecimal) listVal.get(0); System.out.println("vistId="+visitId);
	 * visitIdd=visitId.longValue();
	 * System.out.println("vistId>>>>>>>>>>>>>>>>>="+visitIdd); } } catch(Exception
	 * e) { e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * } return visitIdd; }
	 * 
	 * @Override public String opdObsisty(JSONObject jsondata) { String Result=null;
	 * Transaction t = null; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(OpdObesityHd.class); t = session.beginTransaction();
	 * Long visitId = Long.parseLong((String) jsondata.get("visitId")); Calendar
	 * calendar = Calendar.getInstance(); java.sql.Timestamp ourJavaTimestampObject
	 * = new java.sql.Timestamp(calendar.getTime().getTime()); Date
	 * currentDate=ProjectUtils.getCurrentDate(); cr.add(Restrictions.eq("visitId",
	 * visitId)); OpdObesityHd list = (OpdObesityHd) cr.uniqueResult(); if (list !=
	 * null) {
	 * list.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * if (jsondata.get("varation") != null && !jsondata.get("varation").equals(""))
	 * { BigDecimal bd = new BigDecimal(String.valueOf(jsondata.get("varation")));
	 * list.setVaration(bd); }
	 * 
	 * list.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")))
	 * ); list.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId").
	 * toString()))); list.setLastChgDate(ourJavaTimestampObject);
	 * list.setIniDate(currentDate); session.update(list); Result = "200";
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
	 * oohd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	 * oohd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
	 * oohd.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId")))
	 * ); oohd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * oohd.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	 * oohd.setOverweightFlag(String.valueOf(jsondata.get("obsistyMark")));
	 * oohd.setLastChgDate(ourJavaTimestampObject); if (jsondata.get("varation") !=
	 * null && !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation"))); oohd.setVaration(bd); }
	 * oohd.setIniDate(currentDate); oohd.setLastChgDate(ourJavaTimestampObject);
	 * long obesistyId = Long.parseLong(session.save(oohd).toString());
	 * System.out.println(obesistyId); t.commit(); t=session.beginTransaction();
	 * OpdObesityDt oodt=new OpdObesityDt(); if (jsondata.get("bmi") != null &&
	 * !jsondata.get("bmi").equals("")) { BigDecimal bmi = new
	 * BigDecimal(String.valueOf(jsondata.get("bmi"))); oodt.setBmi(bmi); }
	 * oodt.setObesityDate(currentDate); if (jsondata.get("height") != null &&
	 * !jsondata.get("height").equals("")) { BigDecimal height = new
	 * BigDecimal(String.valueOf(jsondata.get("height"))); oodt.setHeight(height); }
	 * if (jsondata.get("weight") != null && !jsondata.get("weight").equals("")) {
	 * BigDecimal weight = new BigDecimal(String.valueOf(jsondata.get("weight")));
	 * oodt.setWeight(weight); } if (jsondata.get("idealWeight") != null &&
	 * !jsondata.get("idealWeight").equals("")) { BigDecimal idealWeight = new
	 * BigDecimal(String.valueOf(jsondata.get("idealWeight")));
	 * oodt.setIdealWeight(idealWeight); } if (jsondata.get("varation") != null &&
	 * !jsondata.get("varation").equals("")) { BigDecimal bd = new
	 * BigDecimal(String.valueOf(jsondata.get("varation"))); oodt.setVariation(bd);
	 * } oodt.setMonth(m); oodt.setObesityHdId(obesistyId); session.save(oodt);
	 * 
	 * t.commit(); Result = "200"; } } catch (Exception e) { e.printStackTrace(); }
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return Result; }
	 * 
	 * @Override public List<DischargeIcdCode> getDischargeIcdCode(Long visitId) {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(DischargeIcdCode.class);
	 * cr.add(Restrictions.eq("visitId", visitId));
	 * 
	 * ProjectionList projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("dischargeIcdCodeId").as(
	 * "dischargeIcdCodeId"));
	 * 
	 * cr.setProjection(projectionList); List<DischargeIcdCode> list =
	 * cr.setResultTransformer(new
	 * AliasToBeanResultTransformer(DischargeIcdCode.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * public DischargeIcdCode getDischargeIcd(Long icdId,Long visitId) { Session
	 * session =null; DischargeIcdCode pIcdId=null; try {
	 * session=getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(DischargeIcdCode.class) .add(Restrictions.eq("icdId",
	 * icdId)).add(Restrictions.eq("visitId", visitId));
	 * List<DischargeIcdCode>listInvHd=cr.list();
	 * if(CollectionUtils.isNotEmpty(listInvHd)) { pIcdId=listInvHd.get(0); } }
	 * catch(Exception e) { e.printStackTrace(); } return pIcdId; }
	 * 
	 * 
	 * @Override public List<MasMedicalCategory> getMasMedicalCategoryName(String
	 * icdName) { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(MasMedicalCategory.class);
	 * cr.add(Restrictions.eq("status", "Y").ignoreCase());
	 * 
	 * if( StringUtils.isNotBlank(icdName)) {
	 * 
	 * cr.add(Restrictions.ilike("medicalCategoryName", "%"+icdName+"%")); }
	 * ProjectionList projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("medicalCategoryId").as(
	 * "medicalCategoryId"));
	 * projectionList.add(Projections.property("medicalCategoryCode").as(
	 * "medicalCategoryCode"));
	 * projectionList.add(Projections.property("medicalCategoryName").as(
	 * "medicalCategoryName"));
	 * projectionList.add(Projections.property("fitFlag").as("fitFlag"));
	 * 
	 * cr.setProjection(projectionList); List<MasMedicalCategory> list =
	 * cr.setResultTransformer(new
	 * AliasToBeanResultTransformer(MasMedicalCategory.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 */
}
