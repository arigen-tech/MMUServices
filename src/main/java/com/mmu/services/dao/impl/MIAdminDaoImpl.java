package com.mmu.services.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MIAdminDao;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.entity.AppAuditEquipmentDt;
import com.mmu.services.entity.AppEquipmentAccessory;
import com.mmu.services.entity.AppEquipmentDt;
import com.mmu.services.entity.AppEquipmentHd;
import com.mmu.services.entity.AppEquipmentInfo;
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
import com.mmu.services.entity.MasManufacturer;
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
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.SanitaryDiary;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.VisitorDetail;
import com.mmu.services.entity.WaterTestingRegister;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.impl.DispensaryServiceImpl;
import com.mmu.services.utils.HMSUtil;

@Repository
@Transactional
public class MIAdminDaoImpl implements MIAdminDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	PatientRegistrationDao patientRegistrationDao;

	@Autowired
	DispensaryServiceImpl dispenceryService;

	/*
	 * @Override public Map<String, Object> getDisposalTypeList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * String civilianPersonnelCode = HMSUtil.getProperties("adt.properties",
	 * "CIVILIAN_PERSONNEL").trim(); int pageNo1= 0; String startDate=""; String
	 * endDate=""; Date orderStartDate=null; Date orderEndDate=null; Date
	 * finalorderEndDate=null; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<OpdDisposalDetail>
	 * opdPatientDetailList = new ArrayList<OpdDisposalDetail>(); JSONObject json =
	 * new JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; String
	 * status=json.get("flag").toString(); //long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); // long departmentId
	 * = Long.parseLong(json.getString("departmentId").toString()); // long
	 * hospitalId = Long.parseLong(json.getString("unit").toString());
	 * 
	 * Long [] hospitalId=null; if(!json.get("unit").toString().trim().isEmpty()) {
	 * String [] hospitals= json.get("unit").toString().split(","); hospitalId =
	 * HMSUtil.convertFromStringToLongArray(hospitals); }else { String [] hospitals=
	 * json.get("hospitalId").toString().split(","); hospitalId =
	 * HMSUtil.convertFromStringToLongArray(hospitals); }
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr =
	 * session.createCriteria(OpdDisposalDetail.class).createAlias("masDisposal",
	 * "md") .createAlias("visit", "visit").createAlias("patient", "pt")
	 * .createAlias("pt.masEmployeeCategory", "mec"); cr
	 * =cr.add(Restrictions.eq("visit.hospitalId", hospitalId))
	 * .add(Restrictions.ne("mec.employeeCategoryCode",
	 * civilianPersonnelCode).ignoreCase())
	 * .setProjection(Projections.projectionList()
	 * .add(Projections.groupProperty("md.disposalId"))
	 * .add(Projections.groupProperty("md.disposalName"),"name")
	 * .add(Projections.count("md.disposalId"))) ; cr.addOrder(Order.asc("name"));
	 * if(!startDate.equalsIgnoreCase("") && !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("visit.visitDate",orderStartDate);
	 * crrr1=Restrictions.le("visit.visitDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); opdPatientDetailList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("opdPatientDetailList", opdPatientDetailList);
	 * map.put("totalMatches", totalMatches.size()); return map; }
	 * 
	 * @Override public Map<String, Object> getAllDisposalTypeList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * String attcCode = HMSUtil.getProperties("adt.properties",
	 * "ATTC_CODE").trim(); String civilianPersonnelCode =
	 * HMSUtil.getProperties("adt.properties", "CIVILIAN_PERSONNEL").trim();
	 * 
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List totalMatches = new ArrayList(); if (requestData.get("PN") != null)
	 * pageNo1 = Integer.parseInt(requestData.get("PN").toString());
	 * List<OpdDisposalDetail> opdPatientDetailList = new
	 * ArrayList<OpdDisposalDetail>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } String category = json.getString("category").toString();
	 * 
	 * Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; //long
	 * hospitalId = Long.parseLong(json.getString("hospitalId").toString()); //long
	 * departmentId = Long.parseLong(json.getString("departmentId").toString());
	 * long hospitalId = Long.parseLong(json.getString("unit").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr =
	 * session.createCriteria(OpdDisposalDetail.class).createAlias("masDisposal",
	 * "md") .createAlias("patient", "pt").createAlias("visit", "visit")
	 * .createAlias("pt.masEmployeeCategory", "mec"); cr
	 * =cr.add(Restrictions.eq("visit.hospitalId", hospitalId))
	 * .add(Restrictions.ne("mec.employeeCategoryCode",
	 * civilianPersonnelCode).ignoreCase()) .add(Restrictions.eq("md.disposalCode",
	 * category).ignoreCase()); cr.addOrder(Order.desc("visit.visitDate"));
	 * if(!startDate.equalsIgnoreCase("") && !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("visit.visitDate",orderStartDate);
	 * crrr1=Restrictions.le("visit.visitDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); opdPatientDetailList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("opdPatientDetailList", opdPatientDetailList);
	 * map.put("totalMatches", totalMatches.size()); return map; }
	 * 
	 * @Override public List<MasEmployeeCategory> getEmpCategoryList() { // TODO
	 * Auto-generated method stub String civilianPersonnelCode =
	 * HMSUtil.getProperties("adt.properties", "CIVILIAN_PERSONNEL").trim();
	 * 
	 * List<MasEmployeeCategory> masEmployeeCategoryList = new
	 * ArrayList<MasEmployeeCategory>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try {
	 * masEmployeeCategoryList = session.createCriteria(MasEmployeeCategory.class)
	 * .add(Restrictions.ne("employeeCategoryCode", civilianPersonnelCode))
	 * .addOrder(Order.asc("employeeCategoryName")).list(); }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masEmployeeCategoryList; }
	 * 
	 * @Override public List<MasMedicalCategory> getMedCategoryList() { // TODO
	 * Auto-generated method stub List<MasMedicalCategory> masMedicalCategoryList =
	 * new ArrayList<MasMedicalCategory>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try {
	 * masMedicalCategoryList = session.createCriteria(MasMedicalCategory.class)
	 * .addOrder(Order.asc("medicalCategoryName")).list(); }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masMedicalCategoryList; }
	 * 
	 * @Override public Map<String, Object> getAmeReportList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * String meCode=HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_ME").trim(); String civilianPersonnelCode =
	 * HMSUtil.getProperties("adt.properties", "CIVILIAN_PERSONNEL").trim();
	 * 
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List<MasMedicalExamReport> totalMatches = new
	 * ArrayList<MasMedicalExamReport>(); if (requestData.get("PN") != null) pageNo1
	 * = Integer.parseInt(requestData.get("PN").toString());
	 * List<MasMedicalExamReport> masMedicalExamReportList = new
	 * ArrayList<MasMedicalExamReport>(); JSONObject json = new
	 * JSONObject(requestData); Long category ; Long medcategory;
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; //long
	 * hospitalId = Long.parseLong(json.getString("hospitalId").toString()); //long
	 * departmentId = Long.parseLong(json.getString("departmentId").toString());
	 * 
	 * 
	 * Long [] hospitalId=null; if(!json.get("unit").toString().isEmpty()) { String
	 * [] hospitals= json.get("unit").toString().split(","); hospitalId =
	 * HMSUtil.convertFromStringToLongArray(hospitals); }else { String [] hospitals=
	 * json.get("hospitalId").toString().split(","); hospitalId =
	 * HMSUtil.convertFromStringToLongArray(hospitals); }
	 * 
	 * 
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(MasMedicalExamReport.class).createAlias("visit",
	 * "visit") .createAlias("visit.masAppointmentType", "mat")
	 * .createAlias("visit.patient", "pt") //.createAlias("visit.patientMedicalCat",
	 * "pmc") .createAlias("visit.vuPatientMedicalCat", "pmc")
	 * .createAlias("pt.masEmployeeCategory", "mec")
	 * .add(Restrictions.ne("mec.employeeCategoryCode",
	 * civilianPersonnelCode).ignoreCase()) .add(Restrictions.in("hospitalId",
	 * hospitalId)) .add(Restrictions.eq("mat.appointmentTypeCode",
	 * meCode).ignoreCase()) .add(Restrictions.eq("status","ac").ignoreCase());
	 * cr.addOrder(Order.desc("moDate")); if(!startDate.equalsIgnoreCase("") &&
	 * !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("moDate",orderStartDate);
	 * crrr1=Restrictions.le("moDate",finalorderEndDate); cr.add(crrr).add(crrr1); }
	 * category = Long.parseLong(json.getString("category").toString());
	 * if(category!=0) { cr.add(Restrictions.eq("pt.employeeCategoryId", category));
	 * } medcategory = Long.parseLong(json.getString("medcategory").toString());
	 * if(medcategory!=0) {
	 * cr.add(Restrictions.eq("pmc.medicalCategoryId",medcategory)); }
	 * cr=cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); for(int i=(pageSize) * (pageNo1 - 1);i<5*pageNo1;i++
	 * ) { masMedicalExamReportList.add(totalMatches.get(i)); }
	 * //cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * //cr.setMaxResults(pageSize); //masMedicalExamReportList= cr.list(); } }
	 * catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("masMedicalExamReportList", masMedicalExamReportList);
	 * map.put("totalMatches", totalMatches.size()); return map; }
	 * 
	 * @Override public Map<String, Object> getMedicalStatistic(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * 
	 * String meCode=HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_ME").trim(); int pageNo1= 0; String startDate="";
	 * String endDate=""; Date orderStartDate=null; Date orderEndDate=null; Date
	 * finalorderEndDate=null; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<OpdPatientDetail>
	 * opdPatientDetailList = new ArrayList<OpdPatientDetail>(); JSONObject json =
	 * new JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criteria criteria=null; Criterion crrr=null; Criterion
	 * crrr1=null; String status=json.get("flag").toString(); long hospitalId =
	 * Long.parseLong(json.getString("unit").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * 
	 * cr = session.createCriteria(PatientMedicalCat.class) .createAlias("visit",
	 * "visit") .createAlias("visit.masAppointmentType", "mat")
	 * .createAlias("visit.masMedExam", "mme") .createAlias("masMedicalCategory",
	 * "mmc");
	 * 
	 * cr =cr.add(Restrictions.eq("visit.hospitalId", hospitalId))
	 * .add(Restrictions.eq("visit.visitStatus", "c").ignoreCase())
	 * .add(Restrictions.eq("visit.examStatus", "c").ignoreCase())
	 * .add(Restrictions.eq("visit.visitFlag", "r").ignoreCase())
	 * .add(Restrictions.eq("mat.appointmentTypeCode", meCode).ignoreCase())
	 * 
	 * .setProjection(Projections.projectionList()
	 * .add(Projections.groupProperty("mmc.medicalCategoryName"),"name")
	 * .add(Projections.rowCount()));
	 * //.add(Projections.count("medicalCategoryId"))) ;
	 * 
	 * cr.addOrder(Order.asc("name")); if(!startDate.equalsIgnoreCase("") &&
	 * !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("visit.visitDate",orderStartDate);
	 * crrr1=Restrictions.le("visit.visitDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); opdPatientDetailList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("opdPatientDetailList", opdPatientDetailList);
	 * map.put("totalMatches", totalMatches.size()); return map; }
	 * 
	 * @Override public List<MasBloodGroup> getBloodGroupList() { // TODO
	 * Auto-generated method stub List<MasBloodGroup> masBloodGroupList = new
	 * ArrayList<MasBloodGroup>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try { masBloodGroupList
	 * = session.createCriteria(MasBloodGroup.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .addOrder(Order.asc("bloodGroupName")).list();
	 * 
	 * }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masBloodGroupList; }
	 * 
	 * @Override public Map<String, Object> getSimilarBloodGroupPatient(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * String relationCode=HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); int pageNo1= 0; List totalMatches = new
	 * ArrayList(); if (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<EmployeeBloodGroup>
	 * empList = new ArrayList<EmployeeBloodGroup>(); JSONObject json = new
	 * JSONObject(requestData); Criteria cr=null; Criterion crrr=null; Criterion
	 * crrr1=null; //long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); // long departmentId
	 * = Long.parseLong(json.getString("departmentId").toString()); long unitId =
	 * Long.parseLong(json.getString("unit").toString()); long bloodGroupId =
	 * Long.parseLong(json.getString("bloodGroup").toString());
	 * 
	 * String serviceNo = json.getString("serviceNo").toString(); String empName =
	 * json.getString("empName").toString();
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(Patient.class).createAlias("masRelation", "mr")
	 * .add(Restrictions.isNotNull("bloodGroupId"))
	 * .add(Restrictions.eq("mr.relationCode",relationCode).ignoreCase())
	 * .addOrder(Order.desc("patientName"));
	 * 
	 * cr = session.createCriteria(EmployeeBloodGroup.class)
	 * .createAlias("masBloodGroup", "bg").createAlias("masAdministrativeSex", "as")
	 * .add(Restrictions.isNotNull("bg.bloodGroupId"))
	 * .addOrder(Order.desc("employeeName"));
	 * 
	 * if(bloodGroupId!=0) { cr =cr.add(Restrictions.eq("bg.bloodGroupId",
	 * bloodGroupId));
	 * 
	 * } if(unitId!=0) { cr =cr.add(Restrictions.eq("unitId", unitId));
	 * 
	 * } if(!empName.equalsIgnoreCase("") && !empName.isEmpty() && empName != null)
	 * { //cr =cr.add(Restrictions.eq("employeeName", empName).ignoreCase()); cr
	 * =cr.add(Restrictions.ilike("employeeName", "%"
	 * +empName.trim()+"%",MatchMode.ANYWHERE));
	 * 
	 * } if(!serviceNo.equalsIgnoreCase("")) { cr
	 * =cr.add(Restrictions.eq("serviceNo", serviceNo.trim()).ignoreCase());
	 * 
	 * } if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); empList= cr.list(); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("patientList", empList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public List<MasUnit> getMasUnitList() { // TODO Auto-generated
	 * method stub List<MasUnit> masUnitList = new ArrayList<MasUnit>(); Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria
	 * cr=null; try { cr =
	 * session.createCriteria(MasUnit.class).addOrder(Order.asc("unitName"));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { masUnitList=cr.list(); }
	 * }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masUnitList; }
	 * 
	 * @Override public long saveOrUpdate(SanitaryDiary sanitaryDiary) { // TODO
	 * Auto-generated method stub long sanitaryDiaryId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(SanitaryDiary.class); Transaction tx =
	 * session.beginTransaction(); session.saveOrUpdate(sanitaryDiary); tx.commit();
	 * sanitaryDiaryId=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * sanitaryDiaryId; }
	 * 
	 * @Override public Map<String, Object> getSanitaryReportList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List totalMatches = new ArrayList(); if (requestData.get("PN") != null)
	 * pageNo1 = Integer.parseInt(requestData.get("PN").toString());
	 * List<SanitaryDiary> sanitaryDiaryList = new ArrayList<SanitaryDiary>();
	 * JSONObject json = new JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; //String
	 * status=json.get("flag").toString(); //long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); //long departmentId
	 * = Long.parseLong(json.getString("departmentId").toString()); long hospitalId
	 * = Long.parseLong(json.getString("unit").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(SanitaryDiary.class).createAlias("masHospital",
	 * "mh"); cr =cr.add(Restrictions.eq("mh.hospitalId", hospitalId));
	 * cr.addOrder(Order.desc("diaryDate")); if(!startDate.equalsIgnoreCase("") &&
	 * !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("diaryDate",orderStartDate);
	 * crrr1=Restrictions.le("diaryDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); sanitaryDiaryList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("sanitaryDiaryList", sanitaryDiaryList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public Patient getNameByServiceNo(String serviceNo,String
	 * hospitalId) { // TODO Auto-generated method stub List<Patient> patientList
	 * =new ArrayList<Patient>(); Patient patient=null; long relationId; String
	 * selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); relationId =
	 * patientRegistrationDao.getRelationIdFromCode(selfRelationCode); try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * patientList = session.createCriteria(Patient.class)
	 * .add(Restrictions.eq("serviceNo",serviceNo).ignoreCase())
	 * .add(Restrictions.eq("relationId",relationId)).list();
	 * 
	 * if(patientList.size()>0) { patient =patientList.get(0);
	 * 
	 * Long unitIdFromPatient = patientList.get(0).getUnitId(); Criteria cri1 =
	 * session.createCriteria(MasHospital.class).add(Restrictions.eq("hospitalId",
	 * Long.parseLong(hospitalId))); List<MasHospital> hospitalsList = cri1.list();
	 * Long unitIdFromMasUnit = hospitalsList.get(0).getMasUnit().getUnitId();
	 * 
	 * StringBuilder queryBuilder = new StringBuilder(); queryBuilder.
	 * append(" SELECT COUNT(*) as counter, A.MI_UNIT FROM VU_MAS_MIUNIT A " +
	 * " WHERE A.MI_UNIT in (SELECT MI_UNIT FROM VU_MAS_MIUNIT WHERE UNIT_ID=:unitIdFromPatient) AND "
	 * + " A.UNIT_ID=:unitIdFromMasUnit ");
	 * 
	 * queryBuilder.append(" group by A.MI_UNIT ");
	 * 
	 * Query query2 = session.createSQLQuery(queryBuilder.toString());
	 * query2.setParameter("unitIdFromPatient", unitIdFromPatient);
	 * query2.setParameter("unitIdFromMasUnit", unitIdFromMasUnit);
	 * 
	 * List<Object[]> objectList = query2.list(); int counter = objectList.size();
	 * 
	 * if(counter>0) { patient =patientList.get(0);
	 * 
	 * }
	 * 
	 * } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return patient; }
	 * 
	 * @Override public long saveOrUpdateInjuryRegister(InjuryRegister
	 * injuryRegister) { // TODO Auto-generated method stub long injuryRegisterId
	 * =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria criteria =
	 * session.createCriteria(InjuryRegister.class); Transaction tx =
	 * session.beginTransaction(); session.saveOrUpdate(injuryRegister);
	 * tx.commit(); injuryRegisterId=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * injuryRegisterId; }
	 * 
	 * @Override public Map<String, Object> getInjuryReportList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List totalMatches = new ArrayList(); if (requestData.get("PN") != null)
	 * pageNo1 = Integer.parseInt(requestData.get("PN").toString());
	 * List<InjuryRegister> injuryRegisterList = new ArrayList<InjuryRegister>();
	 * JSONObject json = new JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; //String
	 * status=json.get("flag").toString(); //long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); //long departmentId
	 * = Long.parseLong(json.getString("departmentId").toString()); long hospitalId
	 * = Long.parseLong(json.getString("unit").toString()); String serviceNo =
	 * json.getString("serviceNo").toString(); String empName =
	 * json.getString("empName").toString();
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(InjuryRegister.class) .createAlias("masHospital",
	 * "mh").createAlias("patient", "pt"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId", hospitalId));
	 * cr.addOrder(Order.desc("injuryDate")); if(!startDate.equalsIgnoreCase("") &&
	 * !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("injuryDate",orderStartDate);
	 * crrr1=Restrictions.le("injuryDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); } if(!empName.equalsIgnoreCase("") &&
	 * !empName.isEmpty() && empName != null) { //cr
	 * =cr.add(Restrictions.eq("employeeName", empName).ignoreCase()); cr
	 * =cr.add(Restrictions.ilike("pt.employeeName", "%"
	 * +empName.trim()+"%",MatchMode.ANYWHERE));
	 * 
	 * } if(!serviceNo.equalsIgnoreCase("")) { cr
	 * =cr.add(Restrictions.eq("pt.serviceNo", serviceNo.trim()).ignoreCase());
	 * 
	 * } if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); injuryRegisterList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("injuryDiaryList", injuryRegisterList); map.put("totalMatches",
	 * totalMatches.size()); return map;
	 * 
	 * }
	 * 
	 * @Override public long saveOrUpdateHospitalVisitRegister(HospitalVisitRegister
	 * hospitalVisitRegister) { // TODO Auto-generated method stub long
	 * hospitalVisitId =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); session.saveOrUpdate(hospitalVisitRegister);
	 * tx.commit(); hospitalVisitId=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * hospitalVisitId; }
	 * 
	 * @Override public Map<String, Object> getHospitalVisitReportList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List totalMatches = new ArrayList(); if (requestData.get("PN") != null)
	 * pageNo1 = Integer.parseInt(requestData.get("PN").toString());
	 * List<HospitalVisitRegister> hospitalVisitList = new
	 * ArrayList<HospitalVisitRegister>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; long
	 * hospitalId = Long.parseLong(json.getString("unit").toString()); String
	 * serviceNo = json.getString("serviceNo").toString(); String empName =
	 * json.getString("empName").toString();
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(HospitalVisitRegister.class)
	 * .createAlias("masHospital", "mh").createAlias("patient", "pt"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId", hospitalId));
	 * cr.addOrder(Order.desc("hospitalVisitDate"));
	 * if(!startDate.equalsIgnoreCase("") && !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("hospitalVisitDate",orderStartDate);
	 * crrr1=Restrictions.le("hospitalVisitDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); } if(!empName.equalsIgnoreCase("") &&
	 * !empName.isEmpty() && empName != null) { //cr
	 * =cr.add(Restrictions.eq("employeeName", empName).ignoreCase()); cr
	 * =cr.add(Restrictions.ilike("pt.employeeName", "%"
	 * +empName.trim()+"%",MatchMode.ANYWHERE));
	 * 
	 * } if(!serviceNo.equalsIgnoreCase("")) { cr
	 * =cr.add(Restrictions.eq("pt.serviceNo", serviceNo.trim()).ignoreCase());
	 * 
	 * } if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); hospitalVisitList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("hospitalVisitList", hospitalVisitList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public long saveOrUpdateHivStdRegister(HivStdRegister
	 * hivStdRegister) { // TODO Auto-generated method stub long hivId =0; try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction();
	 * session.saveOrUpdate(hivStdRegister); tx.commit(); hivId=1; } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return hivId; }
	 * 
	 * @Override public Map<String, Object> getHivStdList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List totalMatches = new ArrayList(); if (requestData.get("PN") != null)
	 * pageNo1 = Integer.parseInt(requestData.get("PN").toString());
	 * List<HivStdRegister> hivStdList = new ArrayList<HivStdRegister>(); JSONObject
	 * json = new JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; //String
	 * status=json.get("flag").toString(); //long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); //long departmentId
	 * = Long.parseLong(json.getString("departmentId").toString()); long hospitalId
	 * = Long.parseLong(json.getString("unit").toString()); String serviceNo =
	 * json.getString("serviceNo").toString(); String empName =
	 * json.getString("empName").toString(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(HivStdRegister.class) .createAlias("masHospital",
	 * "mh").createAlias("patient", "pt"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId", hospitalId));
	 * cr.addOrder(Order.asc("registerDate")); if(!startDate.equalsIgnoreCase("") &&
	 * !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("registerDate",orderStartDate);
	 * crrr1=Restrictions.le("registerDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); } if(!empName.equalsIgnoreCase("") &&
	 * !empName.isEmpty() && empName != null) { //cr
	 * =cr.add(Restrictions.eq("employeeName", empName).ignoreCase()); cr
	 * =cr.add(Restrictions.ilike("pt.employeeName", "%"
	 * +empName.trim()+"%",MatchMode.ANYWHERE));
	 * 
	 * } if(!serviceNo.equalsIgnoreCase("")) { cr
	 * =cr.add(Restrictions.eq("pt.serviceNo", serviceNo.trim()).ignoreCase());
	 * 
	 * }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); hivStdList= cr.list(); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("hivStdList", hivStdList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public long saveOrUpdateMilkTesting(MilkTestingRegister
	 * milkTestingRegister) { // TODO Auto-generated method stub long mId =0; try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction();
	 * session.saveOrUpdate(milkTestingRegister); tx.commit(); mId=1; } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return mId; }
	 * 
	 * @Override public Map<String, Object> getMilkTestingList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List totalMatches = new ArrayList(); if (requestData.get("PN") != null)
	 * pageNo1 = Integer.parseInt(requestData.get("PN").toString());
	 * List<MilkTestingRegister> milkTestingList = new
	 * ArrayList<MilkTestingRegister>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; //String
	 * status=json.get("flag").toString(); //long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); //long departmentId
	 * = Long.parseLong(json.getString("departmentId").toString()); long hospitalId
	 * = Long.parseLong(json.getString("unit").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(MilkTestingRegister.class)
	 * .createAlias("masHospital", "mh"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId", hospitalId));
	 * cr.addOrder(Order.desc("milkTestingDate"));
	 * if(!startDate.equalsIgnoreCase("") && !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("milkTestingDate",orderStartDate);
	 * crrr1=Restrictions.le("milkTestingDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); milkTestingList= cr.list(); } } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("milkTestingList", milkTestingList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public long saveWaterTesting(WaterTestingRegister
	 * waterTestingRegister) { // TODO Auto-generated method stub long wId =0; try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction();
	 * session.saveOrUpdate(waterTestingRegister); tx.commit(); wId=1; } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return wId; }
	 * 
	 * @Override public Map<String, Object> getWaterTestingList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; String startDate=""; String endDate=""; Date
	 * orderStartDate=null; Date orderEndDate=null; Date finalorderEndDate=null;
	 * List totalMatches = new ArrayList(); if (requestData.get("PN") != null)
	 * pageNo1 = Integer.parseInt(requestData.get("PN").toString());
	 * List<WaterTestingRegister> waterTestingList = new
	 * ArrayList<WaterTestingRegister>(); JSONObject json = new
	 * JSONObject(requestData);
	 * 
	 * if(json.has("fromDate")) { startDate = json.getString("fromDate"); endDate =
	 * json.getString("toDate"); if((!startDate.isEmpty() && startDate!=null) &&
	 * (!endDate.isEmpty() && endDate!=null)) { orderStartDate =
	 * HMSUtil.convertStringDateToUtilDate(startDate, "dd/MM/yyyy"); orderEndDate =
	 * HMSUtil.convertStringDateToUtilDate(endDate, "dd/MM/yyyy");
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(orderEndDate);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 59);
	 * cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0); //Date from =
	 * cal.getTime(); cal.set(Calendar.HOUR_OF_DAY, 23); finalorderEndDate =
	 * cal.getTime();
	 * 
	 * } } Criteria cr=null; Criterion crrr=null; Criterion crrr1=null; //String
	 * status=json.get("flag").toString(); //long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); //long departmentId
	 * = Long.parseLong(json.getString("departmentId").toString()); long hospitalId
	 * = Long.parseLong(json.getString("unit").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(WaterTestingRegister.class)
	 * .createAlias("masHospital", "mh"); cr
	 * =cr.add(Restrictions.eq("mh.hospitalId", hospitalId));
	 * cr.addOrder(Order.desc("waterTestingDate"));
	 * if(!startDate.equalsIgnoreCase("") && !endDate.equalsIgnoreCase("")) {
	 * crrr=Restrictions.ge("waterTestingDate",orderStartDate);
	 * crrr1=Restrictions.le("waterTestingDate",finalorderEndDate);
	 * cr.add(crrr).add(crrr1); }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); waterTestingList= cr.list(); } } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("waterTestingList", waterTestingList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public List<MasEmployee> getEmpList(long hospitalId) { // TODO
	 * Auto-generated method stub
	 * 
	 * List<MasEmployee> masEmployeeList = new ArrayList<MasEmployee>(); MasHospital
	 * masHospital=new MasHospital(); MasUnit masUnit=new MasUnit(); try { Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(MasHospital.class) .add(Restrictions.eq("hospitalId",
	 * hospitalId)); if(!cr.list().isEmpty() && cr.list().size()>0) { masHospital =
	 * (MasHospital) cr.list().get(0); Criteria cr1 =
	 * session.createCriteria(MasUnit.class) .add(Restrictions.eq("unitId",
	 * masHospital.getMasUnit().getUnitId())); if(!cr1.list().isEmpty() &&
	 * cr1.list().size()>0) { masUnit=(MasUnit) cr1.list().get(0); Criteria cr2 =
	 * session.createCriteria(MasEmployee.class) .add(Restrictions.eq("masUnit",
	 * masUnit.getUnitCode())); if(!cr2.list().isEmpty() && cr2.list().size()>0) {
	 * cr2.addOrder(Order.asc("employeeName")); masEmployeeList= cr2.list(); } } } }
	 * catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masEmployeeList; }
	 * 
	 * @Override public Map<String, Object> getDangerousDrugRegisterList(long
	 * pageNo, HashMap<String, String> jsondata) { // TODO Auto-generated method
	 * stub Map<String,Object> map = new HashMap<>(); JSONObject object = new
	 * JSONObject(); JSONArray jsArray1 = new JSONArray(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession();
	 * 
	 * session.doWork(new Work() {
	 * 
	 * @Override public void execute(java.sql.Connection connection) throws
	 * SQLException { Date fDate =null; Date tDate = null; String frmDate ="";
	 * String toDate =""; Integer hospitalId=null; Integer userId=null; int count=0;
	 * int rowCount=0; if(jsondata.get("unit") !=null &&
	 * !(jsondata.get("unit")).equals("")) { hospitalId=
	 * Integer.parseInt(jsondata.get("unit").toString()); }
	 * if(jsondata.get("userId") !=null && !(jsondata.get("userId")).equals("")) {
	 * userId= Integer.parseInt(jsondata.get("userId").toString()); } String flag =
	 * jsondata.get("flag").toString();
	 * 
	 * if(jsondata.get("fromDate") !=null && !(jsondata.get("fromDate")).equals(""))
	 * { String fromDate = jsondata.get("fromDate").toString(); fDate =
	 * HMSUtil.convertStringTypeDateToDateType(fromDate); frmDate =
	 * HMSUtil.convertDateToStringFormat(fDate,"dd/MMM/yyyy");
	 * 
	 * }
	 * 
	 * if(jsondata.get("toDate") !=null && !(jsondata.get("toDate")).equals("")) {
	 * String tDate1 = jsondata.get("toDate").toString(); tDate =
	 * HMSUtil.convertStringTypeDateToDateType(tDate1); toDate =
	 * HMSUtil.convertDateToStringFormat(tDate,"dd/MMM/yyyy"); }
	 * connection.setAutoCommit(false); Long pageNott=pageNo; Integer pageNoTemp=
	 * Integer.parseInt(pageNott.toString()); String
	 * sss="{ ? = call "+databaseScema+".asp_store_ledger( ?, ?, ?, ?, ?, ? ) }";
	 * CallableStatement call = connection.prepareCall(sss);
	 * call.registerOutParameter(1, Types.REF_CURSOR); call.setInt(2, hospitalId);
	 * call.setString(3, flag); call.setDate(4, new java.sql.Date(fDate.getTime()));
	 * call.setDate(5, new java.sql.Date(tDate.getTime())); call.setInt(6, userId);
	 * call.setInt(7, pageNoTemp); call.execute(); ResultSet rs =
	 * (ResultSet)call.getObject(1); while(rs.next()) { int total_rows =
	 * rs.getMetaData().getColumnCount(); count++; JSONObject obj = new
	 * JSONObject(); for (int i = 0; i < total_rows; i++) {
	 * obj.put(rs.getMetaData().getColumnLabel(i + 1) .toLowerCase(),
	 * HMSUtil.convertNullToEmptyString(rs.getObject(i + 1))); } jsArray1.put(obj);
	 * System.out.println("jsArray1...."+jsArray1); } if(jsArray1!=null &&
	 * jsArray1.length()>0) { JSONObject object2 = jsArray1.getJSONObject(0); String
	 * total = object2.get("total_row").toString(); rowCount =
	 * Integer.parseInt(total);
	 * 
	 * } object.put("ref_cur1", jsArray1); map.put("totalMatches", rowCount);
	 * System.out.println("rowCount...."+rowCount); } });
	 * map.put("dangerousDrugList", object); map.put("tm", jsArray1.length());
	 * return map; }
	 * 
	 * @Override public Map<String, Object> getManufactureList(Map<String, String>
	 * requestData) { // TODO Auto-generated method stub String
	 * manufacturerCode=HMSUtil.getProperties("adt.properties",
	 * "MANUFACTURER_CODE").trim(); List<MasStoreSupplier> masStoreSupplierList =
	 * new ArrayList<MasStoreSupplier>(); MasManufacturer masManufacturer=new
	 * MasManufacturer(); Map<String, Object> map = new HashMap<String, Object>();
	 * String hospitalId = requestData.get("hospitalId").toString();
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(MasStoreSupplier.class).createAlias(
	 * "masStoreSupplierType", "st") .createAlias("masHospital", "mh")
	 * .add(Restrictions.eq("mh.hospitalId", Long.parseLong(hospitalId)))
	 * .add(Restrictions.eq("st.supplierTypeCode", manufacturerCode).ignoreCase())
	 * .add(Restrictions.eq("status", "Y").ignoreCase())
	 * .addOrder(Order.asc("supplierName")); if(!cr.list().isEmpty() &&
	 * cr.list().size()>0) { masStoreSupplierList= cr.list(); }
	 * 
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("masManufacturerList", masStoreSupplierList); return map; }
	 * 
	 * @Override public long submitEquipmentDetails(HashMap<String, Object>
	 * jsondata) { // TODO Auto-generated method stub JSONObject json = new
	 * JSONObject(jsondata); Transaction tx =null; AppEquipmentHd appEquipmentHd =
	 * new AppEquipmentHd(); long eqMId=0; String itemId1=""; if (jsondata != null)
	 * { if (!jsondata.get("equipmentDate").toString().isEmpty()) { String
	 * equipmentDate = jsondata.get("equipmentDate").toString(); String
	 * equipmentDate1 = dispenceryService.getReplaceString(equipmentDate); Date
	 * equipmentDate2 = HMSUtil.convertStringDateToUtilDate(equipmentDate1,
	 * "dd/MM/yyyy"); appEquipmentHd.setEquipmentDate(new
	 * Timestamp(equipmentDate2.getTime())); }
	 * 
	 * 
	 * if (!jsondata.get("itemId").toString().isEmpty()) { String itemId =
	 * jsondata.get("itemId").toString(); itemId1 =
	 * dispenceryService.getReplaceString(itemId); } if
	 * (!jsondata.get("scale").toString().isEmpty()) { String scale =
	 * jsondata.get("scale").toString(); String scale1 =
	 * dispenceryService.getReplaceString(scale);
	 * appEquipmentHd.setAuthorizedMeScale(scale1); } if
	 * (!jsondata.get("qty").toString().isEmpty()) { String qty =
	 * jsondata.get("qty").toString(); String qty1 =
	 * dispenceryService.getReplaceString(qty);
	 * appEquipmentHd.setAuthorizedQty(Long.parseLong(qty1)); } String departmentId
	 * = jsondata.get("departmentId").toString(); String departmentId1 =
	 * dispenceryService.getReplaceString(departmentId); String[] departmentIdValue
	 * = departmentId1.split(","); int deptLength = departmentIdValue.length;
	 * 
	 * String modelNo = jsondata.get("modelNo").toString(); String modelNo1 =
	 * dispenceryService.getReplaceString(modelNo); String[] modelNoValue =
	 * modelNo1.split(","); String[] installedDateValue=null; if
	 * (!jsondata.get("installedDate").toString().isEmpty()) { String installedDate
	 * = jsondata.get("installedDate").toString(); String equipmentDate1 =
	 * dispenceryService.getReplaceString(installedDate); installedDateValue =
	 * equipmentDate1.split(","); int a=installedDateValue.length; //Date
	 * equipmentDate2 = HMSUtil.convertStringDateToUtilDate(equipmentDate1,
	 * "dd/MM/yyyy");
	 * 
	 * }
	 * 
	 * 
	 * String serialNo = jsondata.get("serialNo").toString(); String serialNo1 =
	 * dispenceryService.getReplaceString(serialNo); String[] serialNoValue =
	 * serialNo1.split(",");
	 * 
	 * String depreciation = jsondata.get("depreciation").toString(); String
	 * depreciation1 = dispenceryService.getReplaceString(depreciation); String[]
	 * depreciationValue = depreciation1.split(",");
	 * 
	 * String price = jsondata.get("price").toString(); String price1 =
	 * dispenceryService.getReplaceString(price); String[] priceValue =
	 * price1.split(",");
	 * 
	 * String make = jsondata.get("make").toString(); String make1 =
	 * dispenceryService.getReplaceString(make); String[] makeValue =
	 * make1.split(",");
	 * 
	 * String manufacturer = jsondata.get("manufacturer").toString(); String
	 * manufacturer1 = dispenceryService.getReplaceString(manufacturer); String[]
	 * manufacturerValue = manufacturer1.split(",");
	 * 
	 * String technical = jsondata.get("technical").toString(); String technical1 =
	 * dispenceryService.getReplaceString(technical); String[] technicalValue =
	 * technical1.split(",");
	 * 
	 * String rvNo = jsondata.get("rvNo").toString(); String rvNo1 =
	 * dispenceryService.getReplaceString(rvNo); String[] rvNoValue =
	 * rvNo1.split(",");
	 * 
	 * String rvDate = jsondata.get("rvDate").toString(); String rvDate1 =
	 * dispenceryService.getReplaceString(rvDate); String[] rvDateValue =
	 * rvDate1.split(",");
	 * 
	 * Users users = new Users(); MasHospital masHospital = new MasHospital();
	 * MasStoreItem masStoreItem=new MasStoreItem(); // save data in table if
	 * (jsondata.get("userId") != null) { String userId =
	 * jsondata.get("userId").toString(); users.setUserId(Long.parseLong(userId)); }
	 * if (jsondata.get("hospitalId") != null) { String hospitalId =
	 * jsondata.get("hospitalId").toString();
	 * masHospital.setHospitalId(Long.parseLong(hospitalId)); }
	 * masStoreItem.setItemId(Long.parseLong(itemId1)); Date date = new Date();
	 * Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
	 * session1.beginTransaction(); try{ appEquipmentHd.setLastChgDate(new
	 * Timestamp(date.getTime())); appEquipmentHd.setMasStoreItem(masStoreItem);
	 * appEquipmentHd.setMasHospital(masHospital); appEquipmentHd.setUser(users);
	 * eqMId = (long) session1.save(appEquipmentHd);
	 * 
	 * for (int i = 0; i < deptLength; i++) { AppEquipmentDt appEquipmentDt = new
	 * AppEquipmentDt(); MasDepartment masDepartment =new MasDepartment();
	 * MasStoreSupplier masStoreSupplier=new MasStoreSupplier();
	 * 
	 * if (departmentIdValue[i] != null && !departmentIdValue[i].isEmpty()) {
	 * masDepartment.setDepartmentId(Long.parseLong(departmentIdValue[i].trim()));
	 * appEquipmentDt.setMasDepartment(masDepartment); } if (modelNoValue[i] != null
	 * && !modelNoValue[i].isEmpty())
	 * appEquipmentDt.setModelNumber(modelNoValue[i].trim());
	 * 
	 * if (serialNoValue[i] != null && !serialNoValue[i].isEmpty()) {
	 * appEquipmentDt.setSerialNumber(serialNoValue[i].trim()); } if
	 * (depreciationValue[i] != null && !depreciationValue[i].trim().isEmpty()) {
	 * appEquipmentDt.setDepreciation(new BigDecimal(depreciationValue[i].trim()));
	 * } if (priceValue[i] != null && !priceValue[i].trim().isEmpty()) {
	 * appEquipmentDt.setPrice(new BigDecimal(priceValue[i].trim())); } if
	 * (makeValue[i] != null && !makeValue[i].trim().isEmpty()) {
	 * appEquipmentDt.setMake(makeValue[i].trim()); } if (manufacturerValue[i] !=
	 * null && !manufacturerValue[i].trim().isEmpty()) {
	 * masStoreSupplier.setSupplierId(Long.parseLong(manufacturerValue[i].trim()));
	 * appEquipmentDt.setMasStoreSupplier(masStoreSupplier); } if (technicalValue[i]
	 * != null && !technicalValue[i].trim().isEmpty()) {
	 * appEquipmentDt.setTechnicalSpecification(technicalValue[i].trim()); } if
	 * (rvNoValue[i] != null && !rvNoValue[i].trim().isEmpty()) {
	 * appEquipmentDt.setReceivedNumber(rvNoValue[i].trim()); } if (rvDateValue[i]
	 * != null && !rvDateValue[i].trim().isEmpty()) { Date rDate1 =
	 * HMSUtil.convertStringDateToUtilDate(rvDateValue[i].trim(), "dd/MM/yyyy");
	 * appEquipmentDt.setReceivedDate(rDate1); }
	 * 
	 * if (installedDateValue[i] != null && !installedDateValue[i].trim().isEmpty())
	 * { Date installedDate =
	 * HMSUtil.convertStringDateToUtilDate(installedDateValue[i].trim(),
	 * "dd/MM/yyyy"); appEquipmentDt.setInstalledDate(installedDate);
	 * appEquipmentDt.setInstalled("Y"); } appEquipmentDt.setBoardOut("N");
	 * appEquipmentDt.setLastChgDate(new Timestamp(date.getTime()));
	 * appEquipmentDt.setAppEquipmentHd(appEquipmentHd);// need to check
	 * 
	 * long eqDt =(long) session1.save(appEquipmentDt); }
	 * 
	 * tx.commit(); session1.flush(); session1.clear(); }catch(Exception e) { if (tx
	 * != null) { try { tx.rollback(); eqMId=-1; } catch(Exception re) { eqMId=-1;
	 * re.printStackTrace(); } } eqMId=-1; e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return eqMId; } return -1; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getEquipmentDetails(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * JSONObject json = new JSONObject(requestData); Map<String, Object> map = new
	 * HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0;
	 * 
	 * Criteria cr=null; List totalMatches = new ArrayList(); List<AppEquipmentDt>
	 * appEquipmentDtList = new ArrayList<AppEquipmentDt>(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString()); long departmentId =
	 * Long.parseLong(json.getString("departmentId").toString()); long itemId =
	 * Long.parseLong(json.getString("itemId").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(AppEquipmentDt.class)
	 * .createAlias("masDepartment", "md") .createAlias("appEquipmentHd", "hd")
	 * .createAlias("hd.masHospital", "mh") .createAlias("hd.masStoreItem", "item");
	 * cr =cr.add(Restrictions.eq("mh.hospitalId", hospitalId))
	 * .add(Restrictions.eq("md.departmentId", departmentId))
	 * .add(Restrictions.eq("item.itemId", itemId));
	 * cr.addOrder(Order.asc("lastChgDate"));
	 * 
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); appEquipmentDtList= cr.list(); } } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.getMessage(); e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("appEquipmentDtList", appEquipmentDtList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public long submitWarrantydetails(HashMap<String, Object> jsondata)
	 * { // TODO Auto-generated method stub JSONObject json = new
	 * JSONObject(jsondata); Transaction tx =null; AppEquipmentDt appEquipmentDt=new
	 * AppEquipmentDt(); AppEquipmentInfo appEquipmentInfo = new AppEquipmentInfo();
	 * long appInfoId=0; Date date = new Date(); Criteria cr =null; AppEquipmentDt
	 * appEquipmentDt1=null; Session session1 =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); if (jsondata != null) {
	 * if (!jsondata.get("startDate").toString().isEmpty()) { String startDate =
	 * jsondata.get("startDate").toString(); String startDate1 =
	 * dispenceryService.getReplaceString(startDate); Date startDate2 =
	 * HMSUtil.convertStringDateToUtilDate(startDate1, "dd/MM/yyyy");
	 * appEquipmentInfo.setStartDate(new Timestamp(startDate2.getTime())); } if
	 * (!jsondata.get("endDate").toString().isEmpty()) { String endDate =
	 * jsondata.get("endDate").toString(); String endDate1 =
	 * dispenceryService.getReplaceString(endDate); Date endDate2 =
	 * HMSUtil.convertStringDateToUtilDate(endDate1, "dd/MM/yyyy");
	 * appEquipmentInfo.setEndDate(new Timestamp(endDate2.getTime())); }
	 * 
	 * if (!jsondata.get("details").toString().isEmpty()) { String details =
	 * jsondata.get("details").toString(); String details1 =
	 * dispenceryService.getReplaceString(details);
	 * appEquipmentInfo.setEquipmentDetails(details1); }
	 * 
	 * String dtId1=null; if (!jsondata.get("dtId").toString().isEmpty()) { String
	 * dtId = jsondata.get("dtId").toString(); dtId1 =
	 * dispenceryService.getReplaceString(dtId);
	 * appEquipmentDt.setEquipmentDtId(Long.parseLong(dtId1)); }
	 * appEquipmentInfo.setAppEquipmentDt(appEquipmentDt); cr =
	 * session1.createCriteria(AppEquipmentDt.class)
	 * .add(Restrictions.eq("equipmentDtId", Long.parseLong(dtId1)));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { appEquipmentDt1=
	 * (AppEquipmentDt) cr.list().get(0);
	 * 
	 * } if (!jsondata.get("flag").toString().isEmpty()) { String flag =
	 * jsondata.get("flag").toString(); String flag1 =
	 * dispenceryService.getReplaceString(flag);
	 * if(flag1.equalsIgnoreCase("warranty")) {
	 * appEquipmentInfo.setEquipmentFlag("W"); appEquipmentDt1.setWarranty("Y");
	 * }if(flag1.equalsIgnoreCase("amc")) { appEquipmentInfo.setEquipmentFlag("A");
	 * appEquipmentDt1.setAmc("Y"); }
	 * 
	 * } // if (!jsondata.get("preventive").toString().isEmpty()) { // String
	 * preventive = jsondata.get("preventive").toString(); // String preventive1 =
	 * dispenceryService.getReplaceString(preventive); //
	 * if(preventive1.equalsIgnoreCase("on")) //
	 * appEquipmentInfo.setPreventiveFlag("Y"); // else //
	 * appEquipmentInfo.setPreventiveFlag("N");
	 * 
	 * //} if (!jsondata.get("totalPreventive").toString().isEmpty()) { String
	 * totalPreventive = jsondata.get("totalPreventive").toString(); String
	 * totalPreventive1 = dispenceryService.getReplaceString(totalPreventive);
	 * if(Long.parseLong(totalPreventive1)==0)
	 * appEquipmentInfo.setPreventiveFlag("N"); else
	 * appEquipmentInfo.setPreventiveFlag("Y");
	 * 
	 * appEquipmentInfo.setTotalPreventive(Long.parseLong(totalPreventive1));
	 * 
	 * } tx = session1.beginTransaction();
	 * 
	 * try{ appEquipmentInfo.setLastChgDate(new Timestamp(date.getTime()));
	 * appInfoId = (long) session1.save(appEquipmentInfo);
	 * session1.update(appEquipmentDt1); tx.commit(); session1.flush();
	 * session1.clear(); }catch(Exception e) { if (tx != null) { try {
	 * tx.rollback(); appInfoId=-1; } catch(Exception re) { appInfoId=-1;
	 * re.printStackTrace(); } } appInfoId=-1; e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return appInfoId; } return -1; }
	 * 
	 * @Override public AppEquipmentDt getAppEquipmentDt(HashMap<String, Object>
	 * jsondata) { // TODO Auto-generated method stub List<AppEquipmentDt>
	 * appEquipmentDtList =new ArrayList<AppEquipmentDt>(); AppEquipmentDt
	 * appEquipmentDt=null; String dtId1=null; Criteria cr=null; if
	 * (!jsondata.get("dtId").toString().isEmpty()) { String dtId =
	 * jsondata.get("dtId").toString(); dtId1 =
	 * dispenceryService.getReplaceString(dtId); } try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(AppEquipmentDt.class)
	 * .add(Restrictions.eq("equipmentDtId", Long.parseLong(dtId1)));
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { appEquipmentDt=
	 * (AppEquipmentDt) cr.list().get(0);
	 * 
	 * } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * appEquipmentDt; }
	 * 
	 * @Override public long submitAccessarydetails(HashMap<String, Object>
	 * jsondata) { // TODO Auto-generated method stub JSONObject json = new
	 * JSONObject(jsondata); Transaction tx =null; AppEquipmentDt appEquipmentDt=new
	 * AppEquipmentDt(); long appInfoId=0; Date date = new Date(); Criteria cr
	 * =null; String[] modelNoValue=null; String[] startDateValue=null; String[]
	 * endDateValue=null; String[] serialNoValue=null; String[] detailsValue=null;
	 * String[] accessaryNameValue=null; String[] accIdValue=null;
	 * 
	 * Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession(); if
	 * (jsondata != null) { if (!jsondata.get("modelNo").toString().isEmpty()) {
	 * String modelNo = jsondata.get("modelNo").toString(); String modelNo1 =
	 * dispenceryService.getReplaceString(modelNo); modelNoValue =
	 * modelNo1.split(","); }
	 * 
	 * 
	 * if (!jsondata.get("startDate").toString().isEmpty()) { String startDate =
	 * jsondata.get("startDate").toString(); String startDate1 =
	 * dispenceryService.getReplaceString(startDate); startDateValue =
	 * startDate1.split(","); } if (!jsondata.get("endDate").toString().isEmpty()) {
	 * String endDate = jsondata.get("endDate").toString(); String endDate1 =
	 * dispenceryService.getReplaceString(endDate); endDateValue =
	 * endDate1.split(","); } int totalAccessary=startDateValue.length;
	 * 
	 * if (!jsondata.get("serialNo").toString().isEmpty()) { String serialNo =
	 * jsondata.get("serialNo").toString(); String serialNo1 =
	 * dispenceryService.getReplaceString(serialNo); serialNoValue =
	 * serialNo1.split(","); } if (!jsondata.get("details").toString().isEmpty()) {
	 * String details = jsondata.get("details").toString(); String details1 =
	 * dispenceryService.getReplaceString(details); detailsValue =
	 * details1.split(","); } if
	 * (!jsondata.get("accessaryName").toString().isEmpty()) { String accessaryName
	 * = jsondata.get("accessaryName").toString(); String accessaryName1 =
	 * dispenceryService.getReplaceString(accessaryName); accessaryNameValue =
	 * accessaryName1.split(","); }
	 * 
	 * if (!jsondata.get("accId").toString().isEmpty()) { String accessaryName =
	 * jsondata.get("accId").toString(); String accessaryName1 =
	 * dispenceryService.getReplaceString(accessaryName); accIdValue =
	 * accessaryName1.split(","); } String dtId1=null; if
	 * (!jsondata.get("dtId").toString().isEmpty()) { String dtId =
	 * jsondata.get("dtId").toString(); dtId1 =
	 * dispenceryService.getReplaceString(dtId); } cr =
	 * session1.createCriteria(AppEquipmentDt.class)
	 * .add(Restrictions.eq("equipmentDtId", Long.parseLong(dtId1)));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { appEquipmentDt=
	 * (AppEquipmentDt) cr.list().get(0);
	 * 
	 * } if (!jsondata.get("flag").toString().isEmpty()) { String flag =
	 * jsondata.get("flag").toString(); String flag1 =
	 * dispenceryService.getReplaceString(flag); appEquipmentDt.setAccessary("Y");
	 * 
	 * }
	 * 
	 * tx = session1.beginTransaction();
	 * 
	 * try{ for (int i = 0; i < totalAccessary; i++) { AppEquipmentAccessory
	 * appEquipmentAccessory = new AppEquipmentAccessory(); if (accIdValue[i] !=
	 * null && !accIdValue[i].trim().isEmpty()) {
	 * 
	 * Criteria criteria = session1.createCriteria(AppEquipmentAccessory.class)
	 * .add(Restrictions.eq("accessoryId", Long.parseLong(accIdValue[i].trim())));
	 * if(!criteria.list().isEmpty() && criteria.list().size()>0) {
	 * appEquipmentAccessory= (AppEquipmentAccessory) criteria.list().get(0);
	 * 
	 * }
	 * 
	 * } appEquipmentAccessory.setAppEquipmentDt(appEquipmentDt);
	 * appEquipmentAccessory.setLastChgDate(new Timestamp(date.getTime()));
	 * 
	 * if (detailsValue[i] != null && !detailsValue[i].trim().isEmpty()) {
	 * appEquipmentAccessory.setAccessoryDetails(detailsValue[i].trim()); } if
	 * (modelNoValue[i] != null && !modelNoValue[i].trim().isEmpty())
	 * appEquipmentAccessory.setModelNumber(modelNoValue[i].trim());
	 * 
	 * if (serialNoValue[i] != null && !serialNoValue[i].trim().isEmpty()) {
	 * appEquipmentAccessory.setSerialNumber(serialNoValue[i].trim()); } if
	 * (accessaryNameValue[i] != null && !accessaryNameValue[i].trim().isEmpty()) {
	 * appEquipmentAccessory.setAccessoryName(accessaryNameValue[i].trim()); }
	 * 
	 * if (endDateValue[i] != null && !endDateValue[i].trim().isEmpty()) { Date
	 * endDate = HMSUtil.convertStringDateToUtilDate(endDateValue[i].trim(),
	 * "dd/MM/yyyy"); appEquipmentAccessory.setEndDate(endDate);
	 * 
	 * } if (startDateValue[i] != null && !startDateValue[i].trim().isEmpty()) {
	 * Date startDate =
	 * HMSUtil.convertStringDateToUtilDate(startDateValue[i].trim(), "dd/MM/yyyy");
	 * appEquipmentAccessory.setStartDate(startDate); }
	 * 
	 * session1.saveOrUpdate(appEquipmentAccessory); appInfoId=
	 * appEquipmentAccessory.getAccessoryId(); }
	 * 
	 * session1.update(appEquipmentDt); tx.commit(); session1.flush();
	 * session1.clear(); }catch(Exception e) { if (tx != null) { try {
	 * tx.rollback(); appInfoId=-1; } catch(Exception re) { appInfoId=-1;
	 * re.printStackTrace(); } } appInfoId=-1; e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return appInfoId; } return -1; }
	 * 
	 * @Override public AppEquipmentHd getEquipmentDetails(String itemId, String
	 * hospitalId) { // TODO Auto-generated method stub Map<String, Object> map =
	 * new HashMap<String, Object>(); List<AppEquipmentHd> listAppEquipmentHd=null;
	 * AppEquipmentHd appEquipmentHd=null; int flag=0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction();
	 * 
	 * Criteria criteria =
	 * session.createCriteria(AppEquipmentHd.class).createAlias("masStoreItem",
	 * "masStoreItem") .add(Restrictions.eq("masStoreItem.itemId",
	 * Long.parseLong(itemId))) .add(Restrictions.eq("masHospital.hospitalId",
	 * Long.parseLong(hospitalId)));
	 * 
	 * criteria=criteria.addOrder(Order.desc("equipmentHdId")).setFirstResult(0).
	 * setMaxResults(1);
	 * 
	 * listAppEquipmentHd =criteria.list(); if(listAppEquipmentHd.size()>0) {
	 * appEquipmentHd =listAppEquipmentHd.get(0); }
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }
	 * 
	 * return appEquipmentHd; }
	 * 
	 * @Override public Map<String, Object> getEquipmentList(long pageNo,
	 * HashMap<String, String> requestData) { // TODO Auto-generated method stub
	 * Map<String, Object> map = new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0; List totalMatches = new ArrayList(); if
	 * (requestData.get("PN") != null) pageNo1 =
	 * Integer.parseInt(requestData.get("PN").toString()); List<AppEquipmentDt>
	 * appEquipmentDtList = new ArrayList<AppEquipmentDt>(); List<AppEquipmentDt>
	 * appEquipmentDtList1 = new ArrayList<AppEquipmentDt>(); JSONObject json = new
	 * JSONObject(requestData); long itemId =
	 * Long.parseLong(json.getString("itemId").toString()); long hospitalId =
	 * Long.parseLong(json.getString("hospitalId").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * //reset boardout value with status N\ Transaction tx =
	 * session.beginTransaction(); Criteria cr =
	 * session.createCriteria(AppEquipmentDt.class) .createAlias("appEquipmentHd",
	 * "appEquipmentHd") .createAlias("appEquipmentHd.masStoreItem", "masStoreItem")
	 * .add(Restrictions.eq("masStoreItem.itemId", itemId))
	 * .add(Restrictions.eq("appEquipmentHd.masHospital.hospitalId", hospitalId));
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); cr.setFirstResult((pageSize) * (pageNo1 - 1));
	 * cr.setMaxResults(pageSize); appEquipmentDtList1= cr.list(); if
	 * (!appEquipmentDtList1.isEmpty() && appEquipmentDtList1.size() > 0) {
	 * for(AppEquipmentDt appEquipmentDt : appEquipmentDtList1) {
	 * if(appEquipmentDt.getBoardOut().equalsIgnoreCase("T")) {
	 * appEquipmentDt.setBoardOut("N"); session.update(appEquipmentDt); } } }
	 * 
	 * }
	 * 
	 * //END Criteria criteria = session.createCriteria(AppEquipmentDt.class)
	 * .createAlias("appEquipmentHd", "appEquipmentHd")
	 * .createAlias("appEquipmentHd.masStoreItem", "masStoreItem")
	 * .add(Restrictions.eq("masStoreItem.itemId", itemId))
	 * .add(Restrictions.eq("appEquipmentHd.masHospital.hospitalId", hospitalId))
	 * .setProjection(Projections.projectionList()
	 * .add(Projections.groupProperty("receivedNumber"))
	 * .add(Projections.groupProperty("receivedDate"),"receivedDate")
	 * .add(Projections.rowCount())
	 * .add(Projections.groupProperty("appEquipmentHd.equipmentHdId")));
	 * criteria.addOrder(Order.desc("receivedDate"));
	 * 
	 * if(!criteria.list().isEmpty() && criteria.list().size()>0) {
	 * 
	 * totalMatches=criteria.list(); criteria.setFirstResult((pageSize) * (pageNo1 -
	 * 1)); criteria.setMaxResults(pageSize); appEquipmentDtList= criteria.list(); }
	 * tx.commit(); } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("appEquipmentDtList", appEquipmentDtList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public Map<String, Object> getEquipmentDetailsForstoreLedger(long
	 * pageNo, HashMap<String, String> requestData) { // TODO Auto-generated method
	 * stub JSONObject json = new JSONObject(requestData); Map<String, Object> map =
	 * new HashMap<String, Object>(); int pageSize =
	 * Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
	 * int pageNo1= 0;
	 * 
	 * Criteria cr=null; List totalMatches = new ArrayList(); List<AppEquipmentDt>
	 * appEquipmentDtList = new ArrayList<AppEquipmentDt>();
	 * 
	 * long eqHdId = Long.parseLong(json.getString("eqHdId").toString());
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * cr = session.createCriteria(AppEquipmentDt.class)
	 * .createAlias("appEquipmentHd", "appEquipmentHd")
	 * .createAlias("appEquipmentHd.masStoreItem", "masStoreItem")
	 * .add(Restrictions.eq("appEquipmentHd.equipmentHdId", eqHdId));
	 * cr.addOrder(Order.asc("lastChgDate"));
	 * 
	 * if(requestData.containsKey("rvNumber")) { String
	 * rvNumber=json.getString("rvNumber").toString(); String
	 * itemId=json.getString("itemId").toString();
	 * cr.add(Restrictions.eq("masStoreItem.itemId", Long.parseLong(itemId)));
	 * cr.add(Restrictions.eq("receivedNumber", rvNumber).ignoreCase()); }
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * totalMatches=cr.list(); appEquipmentDtList= cr.list(); } } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("appEquipmentDtList", appEquipmentDtList); map.put("totalMatches",
	 * totalMatches.size()); return map; }
	 * 
	 * @Override public List<AppEquipmentDt> appEquipmentDtList(long eqHdId, String
	 * rvNumber) { // TODO Auto-generated method stub
	 * 
	 * List<AppEquipmentDt> appEquipmentDtList = new ArrayList<AppEquipmentDt>();
	 * Criteria cr=null; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(AppEquipmentDt.class) .createAlias("appEquipmentHd",
	 * "appEquipmentHd") .add(Restrictions.eq("receivedNumber", rvNumber))
	 * .add(Restrictions.eq("boardOut", "Y").ignoreCase())
	 * .add(Restrictions.eq("appEquipmentHd.equipmentHdId", eqHdId));
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) {
	 * 
	 * appEquipmentDtList= cr.list(); }
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * appEquipmentDtList; }
	 * 
	 * @Override public int submitBoardOutDetails(HashMap<String, Object> jsondata)
	 * { // TODO Auto-generated method stub String[] boardOutValue=null; String[]
	 * auditRemarkValue=null; String[] dtIdValue=null;
	 * 
	 * Transaction tx =null; Criteria cr=null; int status=0; String[] boValue=null;
	 * Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * AppEquipmentDt appEquipmentDt=new AppEquipmentDt(); if (jsondata != null) {
	 * if (jsondata.containsKey("boardOut") &&
	 * !jsondata.get("boardOut").toString().isEmpty()) { String boardOut =
	 * jsondata.get("boardOut").toString(); String boardOut1 =
	 * dispenceryService.getReplaceString(boardOut); boardOutValue =
	 * boardOut1.split(","); boValue =
	 * Arrays.stream(boardOutValue).map(String::trim).toArray(String[]::new);
	 * 
	 * }
	 * 
	 * if (!jsondata.get("auditRemark").toString().isEmpty()) { String auditRemark =
	 * jsondata.get("auditRemark").toString(); String auditRemark1 =
	 * dispenceryService.getReplaceString(auditRemark); auditRemarkValue =
	 * auditRemark1.split(","); } if (!jsondata.get("dtId").toString().isEmpty()) {
	 * String dtId = jsondata.get("dtId").toString(); String dtId1 =
	 * dispenceryService.getReplaceString(dtId); dtIdValue = dtId1.split(","); } //
	 * List<String> list = Arrays.asList(boardOutValue); List<String> list=null;
	 * if(boValue!=null) list = Arrays.asList(boValue); dtIdValue =
	 * Arrays.stream(dtIdValue).map(String::trim).toArray(String[]::new); tx =
	 * session1.beginTransaction();
	 * 
	 * try{ for (int i = 0; i < dtIdValue.length; i++) { cr =
	 * session1.createCriteria(AppEquipmentDt.class)
	 * .add(Restrictions.eq("equipmentDtId", Long.parseLong(dtIdValue[i].trim())));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { appEquipmentDt=
	 * (AppEquipmentDt) cr.list().get(0);
	 * 
	 * } if(!auditRemarkValue[i].trim().equalsIgnoreCase(""))
	 * appEquipmentDt.setRemarks(auditRemarkValue[i].trim());
	 * 
	 * if(boValue!=null){ if(list.contains(dtIdValue[i])){ if (appEquipmentDt!=
	 * null) { appEquipmentDt.setBoardOut("T");//t== Temparory status=1; } } }
	 * session1.update(appEquipmentDt); }
	 * 
	 * 
	 * tx.commit(); session1.flush(); session1.clear(); }catch(Exception e) { if (tx
	 * != null) { try { tx.rollback(); status=-1; } catch(Exception re) { status=-1;
	 * re.printStackTrace(); } } status=-1; e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * } return status;
	 * 
	 * }
	 * 
	 * @Override public List<AppEquipmentHd> getAppEquipmentHdList(String itemId,
	 * String hospitalId) { // TODO Auto-generated method stub Map<String, Object>
	 * map = new HashMap<String, Object>(); List<AppEquipmentHd>
	 * listAppEquipmentHd=null; AppEquipmentHd appEquipmentHd=null; int flag=0; try
	 * { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction();
	 * 
	 * Criteria criteria =
	 * session.createCriteria(AppEquipmentHd.class).createAlias("masStoreItem",
	 * "masStoreItem") .add(Restrictions.eq("masStoreItem.itemId",
	 * Long.parseLong(itemId))) .add(Restrictions.eq("masHospital.hospitalId",
	 * Long.parseLong(hospitalId))); listAppEquipmentHd =criteria.list();
	 * 
	 * tx.commit();
	 * 
	 * } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }
	 * 
	 * return listAppEquipmentHd; }
	 * 
	 * @Override public long saveOrUpdateAppAuditEquipmentDt(AppAuditEquipmentDt
	 * appAuditEquipmentDt) { // TODO Auto-generated method stub long appAuditId =0;
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria criteria = session.createCriteria(AppAuditEquipmentDt.class);
	 * Transaction tx = session.beginTransaction();
	 * session.saveOrUpdate(appAuditEquipmentDt); tx.commit(); appAuditId=1; } catch
	 * (Exception e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * appAuditId=1; e.getMessage(); e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return appAuditId;
	 * }
	 * 
	 * @Override public int update(AppEquipmentDt appEquipmentDt) { // TODO
	 * Auto-generated method stub int status =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); session.update(appEquipmentDt); tx.commit();
	 * status=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return status; }
	 * 
	 * @Override public List<MasDisposal> getDisposalList() { // TODO Auto-generated
	 * method stub String attcCode = HMSUtil.getProperties("adt.properties",
	 * "ATTC_CODE").trim(); String siqCode = HMSUtil.getProperties("adt.properties",
	 * "SIQ_CODE").trim();
	 * 
	 * List<MasDisposal> masDisposalList = new ArrayList<MasDisposal>(); Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); try {
	 * masDisposalList = session.createCriteria(MasDisposal.class)
	 * .add(Restrictions.or(Restrictions.eq("disposalCode",
	 * attcCode),Restrictions.eq("disposalCode", siqCode).ignoreCase()))
	 * //.add(Restrictions.eq("disposalCode", siqCode).ignoreCase())
	 * .addOrder(Order.asc("disposalName")).list(); }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masDisposalList; }
	 * 
	 * @Override public MasEmployee getEmpInfoByServiceNo(String serviceNo,String
	 * hospitalId) { // TODO Auto-generated method stub List<MasEmployee>
	 * masEmployeeList =new ArrayList<MasEmployee>(); MasEmployee masEmployee=null;
	 * List<MasUnit> masUnitList =new ArrayList<MasUnit>(); MasUnit masUnit=null;
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masEmployeeList = session.createCriteria(MasEmployee.class)
	 * .add(Restrictions.eq("serviceNo",serviceNo).ignoreCase()).list();
	 * 
	 * if(masEmployeeList.size()>0) { String unitCode =
	 * masEmployeeList.get(0).getMasUnit(); //MasUnit masUnit=getMasUnit(unitCode);
	 * masUnitList = session.createCriteria(MasUnit.class)
	 * .add(Restrictions.eq("unitCode",unitCode).ignoreCase()).list();
	 * 
	 * if(masUnitList.size()>0) { masUnit =masUnitList.get(0); }
	 * 
	 * Long unitIdFromPatient = masUnit.getUnitId(); Criteria cri1 =
	 * session.createCriteria(MasHospital.class).add(Restrictions.eq("hospitalId",
	 * Long.parseLong(hospitalId))); List<MasHospital> hospitalsList = cri1.list();
	 * Long unitIdFromMasUnit = hospitalsList.get(0).getMasUnit().getUnitId();
	 * 
	 * StringBuilder queryBuilder = new StringBuilder(); queryBuilder.
	 * append(" SELECT COUNT(*) as counter, A.MI_UNIT FROM VU_MAS_MIUNIT A " +
	 * " WHERE A.MI_UNIT in (SELECT MI_UNIT FROM VU_MAS_MIUNIT WHERE UNIT_ID=:unitIdFromPatient) AND "
	 * + " A.UNIT_ID=:unitIdFromMasUnit ");
	 * 
	 * queryBuilder.append(" group by A.MI_UNIT ");
	 * 
	 * Query query2 = session.createSQLQuery(queryBuilder.toString());
	 * query2.setParameter("unitIdFromPatient", unitIdFromPatient);
	 * query2.setParameter("unitIdFromMasUnit", unitIdFromMasUnit);
	 * 
	 * List<Object[]> objectList = query2.list(); int counter = objectList.size();
	 * 
	 * if(counter>0) { masEmployee =masEmployeeList.get(0);
	 * 
	 * 
	 * } } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masEmployee; }
	 * 
	 * @Override public MasUnit getMasUnit(String unitCode) { // TODO Auto-generated
	 * method stub List<MasUnit> masUnitList =new ArrayList<MasUnit>(); MasUnit
	 * masUnit=null;
	 * 
	 * try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masUnitList = session.createCriteria(MasUnit.class)
	 * .add(Restrictions.eq("unitCode",unitCode).ignoreCase()).list();
	 * 
	 * if(masUnitList.size()>0) { masUnit =masUnitList.get(0); } } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return masUnit; }
	 * 
	 * @Override public MasRank getMasRank(String rankCode) { // TODO Auto-generated
	 * method stub List<MasRank> masRankList =new ArrayList<MasRank>(); MasRank
	 * masRank=null;
	 * 
	 * try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masRankList = session.createCriteria(MasRank.class)
	 * .add(Restrictions.eq("rankCode",rankCode).ignoreCase()).list();
	 * 
	 * if(masRankList.size()>0) { masRank =masRankList.get(0); } } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return masRank; }
	 * 
	 * @Override public long saveOrUpdateEmployeeBloodGroup(EmployeeBloodGroup
	 * employeeBloodGroup) { // TODO Auto-generated method stub long empBloodgroupId
	 * =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); session.saveOrUpdate(employeeBloodGroup);
	 * tx.commit(); empBloodgroupId=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * empBloodgroupId; }
	 * 
	 * @Override public MasUnit getMasUnitById(long unitId) { // TODO Auto-generated
	 * method stub List<MasUnit> masUnitList =new ArrayList<MasUnit>(); MasUnit
	 * masUnit=null;
	 * 
	 * try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masUnitList = session.createCriteria(MasUnit.class)
	 * .add(Restrictions.eq("unitId",unitId)).list();
	 * 
	 * if(masUnitList.size()>0) { masUnit =masUnitList.get(0); } } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return masUnit; }
	 * 
	 * @Override public MasRank getMasRankById(long rankId) { // TODO Auto-generated
	 * method stub List<MasRank> masRankList =new ArrayList<MasRank>(); MasRank
	 * masRank=null;
	 * 
	 * try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masRankList = session.createCriteria(MasRank.class)
	 * .add(Restrictions.eq("rankId",rankId)).list();
	 * 
	 * if(masRankList.size()>0) { masRank =masRankList.get(0); } } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return masRank; }
	 * 
	 * @Override public long submitHospitalVisitRegister(HashMap<String, Object>
	 * jsondata) { // TODO Auto-generated method stub JSONObject jsonObj = new
	 * JSONObject(); Transaction tx =null;
	 * 
	 * JSONObject json = new JSONObject(jsondata); HospitalVisitRegister
	 * hospitalVisitRegister = new HospitalVisitRegister(); long hvId=0;
	 * 
	 * if (jsondata != null) { if(!jsondata.get("visitDate").toString().isEmpty()) {
	 * String visitDate = jsondata.get("visitDate").toString(); String visitDate1 =
	 * dispenceryService.getReplaceString(visitDate); Date visitDate2 =
	 * HMSUtil.convertStringDateToUtilDate(visitDate1, "dd/MM/yyyy");
	 * hospitalVisitRegister.setHospitalVisitDate(new
	 * Timestamp(visitDate2.getTime())); } MasHospital masHospital1 = null; String
	 * hospitalId=""; if (jsondata.get("hospitalId") != null) { hospitalId =
	 * jsondata.get("hospitalId").toString(); // masHospital1 =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); } String
	 * icdId1=""; if (jsondata.get("diagnosisId") != null) { String icdId =
	 * jsondata.get("diagnosisId").toString(); // icdId1 =
	 * dispenceryService.getReplaceString(icdId); icdId1=icdId.substring(1,
	 * icdId.length()-1); }
	 * 
	 * if (jsondata.get("wardName") != null) { String wardName =
	 * jsondata.get("wardName").toString(); String wardName1 =
	 * dispenceryService.getReplaceString(wardName);
	 * hospitalVisitRegister.setWardName(wardName1); } if
	 * (jsondata.get("hospitalName") != null) { String hospitalName =
	 * jsondata.get("hospitalName").toString(); String hospitalName1 =
	 * dispenceryService.getReplaceString(hospitalName);
	 * hospitalVisitRegister.setHospitalName(hospitalName1); }
	 * 
	 * if (jsondata.get("remark") != null) { String remark =
	 * jsondata.get("remark").toString(); String remark1 =
	 * dispenceryService.getReplaceString(remark);
	 * hospitalVisitRegister.setRemarks(remark1); }
	 * 
	 * String empId1=""; if (jsondata.get("empId") != null) { String empId =
	 * jsondata.get("empId").toString(); empId1 =
	 * dispenceryService.getReplaceString(empId); }
	 * 
	 * int visitorlength=0; String[] visitorValue=null; String[]
	 * visitorNameValue=null; String[] remarkValue=null; if (jsondata.get("visitor")
	 * != null) { String visitor = jsondata.get("visitor").toString(); String
	 * visitor1 = dispenceryService.getReplaceString(visitor); visitorValue =
	 * visitor1.split(","); visitorlength=visitorValue.length; } if
	 * (jsondata.get("visitorName") != null) { String visitorName =
	 * jsondata.get("visitorName").toString(); String visitorName1 =
	 * dispenceryService.getReplaceString(visitorName); visitorNameValue =
	 * visitorName1.split(","); } if (jsondata.get("remark") != null) { String
	 * remark = jsondata.get("remark").toString(); String remark1 =
	 * dispenceryService.getReplaceString(remark); remarkValue = remark1.split(",");
	 * }
	 * 
	 * //checking patient is exist or not String empName=""; String empRankId="";
	 * String empGenderId=""; String sNo=""; String dob=""; Date birthDate2=null;
	 * String patientId=""; if(!jsondata.get("birthDate").toString().isEmpty()) {
	 * String birthDate = jsondata.get("birthDate").toString(); String birthDate1 =
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
	 * Patient patient=new Patient();
	 * //patient.setPatientId(Long.parseLong(empId1));
	 * 
	 * MasEmployee masEmployee=null; MasUnit masUnit=null;
	 * if(patientId.equalsIgnoreCase("")) { long relationId; String selfRelationCode
	 * = HMSUtil.getProperties("adt.properties", "SELF_RELATION_CODE").trim();
	 * relationId = patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * String serviceTypeCode = HMSUtil.getProperties("adt.properties",
	 * "SERVICE_TYPE_CODE_ICG"); String registrationTypeCode =
	 * HMSUtil.getProperties("adt.properties", "ICG_REGISTRATION_TYPE_ID");
	 * MasServiceType masServiceType=getServiceType(serviceTypeCode);
	 * MasRegistrationType
	 * masRegistrationType=getRegistrationType(registrationTypeCode); //String
	 * uhidNo = patientRegistrationDao.getHinNo(sNo,relationId,masRegistrationType.
	 * getRegistrationTypeId());
	 * 
	 * masEmployee= getEmpInfoByServiceNo(sNo,hospitalId); masUnit=
	 * getMasUnit(masEmployee.getMasUnit());
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
	 * }else { patient= getNameByServiceNo(sNo,hospitalId);
	 * patient.setAdministrativeSexId(Long.parseLong(empGenderId));
	 * patient.setDateOfBirth(birthDate2);
	 * patient.setRankId(Long.parseLong(empRankId)); } long pId =
	 * saveOrUpdatePatientDetails(patient); Date date = new Date();
	 * 
	 * Users users = new Users(); MasHospital masHospital =new MasHospital();
	 * 
	 * // save data in table if (jsondata.get("userId") != null) { String userId =
	 * jsondata.get("userId").toString(); users.setUserId(Long.parseLong(userId)); }
	 * if (jsondata.get("hospitalId") != null) { String hospitalId1 =
	 * jsondata.get("hospitalId").toString();
	 * masHospital.setHospitalId(Long.parseLong(hospitalId1)); } Session session1 =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
	 * session1.beginTransaction(); try{ //session1.saveOrUpdate(patient);
	 * hospitalVisitRegister.setLastChgBy(users);
	 * hospitalVisitRegister.setIcdDiagnosis(icdId1);
	 * hospitalVisitRegister.setMasHospital(masHospital);
	 * hospitalVisitRegister.setPatient(patient);
	 * hospitalVisitRegister.setLastChgDate(new Timestamp(date.getTime())); hvId =
	 * (long) session1.save(hospitalVisitRegister);
	 * 
	 * for (int i = 0; i < visitorlength; i++) { VisitorDetail visitorDetail = new
	 * VisitorDetail();
	 * visitorDetail.setHospitalVisitRegister(hospitalVisitRegister);
	 * visitorDetail.setLastChgDate(new Timestamp(date.getTime()));
	 * visitorDetail.setUser(users); if (visitorValue[i] != null &&
	 * !visitorValue[i].isEmpty()) { visitorDetail.setVisitorName(visitorValue[i]);
	 * } if (visitorNameValue[i] != null && !visitorNameValue[i].isEmpty()) {
	 * visitorDetail.setRankAndName(visitorNameValue[i]); } if (remarkValue[i] !=
	 * null && !remarkValue[i].isEmpty()) {
	 * visitorDetail.setRemarks(remarkValue[i]); } long eqDt =(long)
	 * session1.save(visitorDetail);
	 * 
	 * } tx.commit(); session1.flush(); session1.clear(); }catch(Exception e) { if
	 * (tx != null) { try { tx.rollback(); hvId=-1; } catch(Exception re) { hvId=-1;
	 * re.printStackTrace(); } } hvId=-1; e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return hvId; } return -1; }
	 * 
	 * @Override public Map<String, Object> getMedicalCategorylistForHIVstd(String
	 * patientId) { // TODO Auto-generated method stub List<PatientMedicalCat>
	 * patientMedicalCatList = new ArrayList<PatientMedicalCat>(); Map<String,
	 * Object> map = new HashMap<String, Object>(); // patientId = "1452"; Criteria
	 * cr=null; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(PatientMedicalCat.class) .createAlias("patient", "pt")
	 * .add(Restrictions.eq("pt.patientId", Long.parseLong(patientId)))
	 * .add(Restrictions.eq("mbStatus", "P").ignoreCase());
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { patientMedicalCatList=
	 * cr.list(); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("patientMedicalCatList", patientMedicalCatList); return map; }
	 * 
	 * @Override public long saveOrUpdateHivStdRegisterData(HashMap<String, Object>
	 * jsondata) { // TODO Auto-generated method stub JSONObject jsonObj = new
	 * JSONObject(); Transaction tx =null; JSONObject json = new
	 * JSONObject(jsondata);
	 * 
	 * MasHospital masHospital = new MasHospital(); MasDepartment masDepartment =
	 * null; long hvId=0; Date registerDate2=null; if (jsondata != null) {
	 * if(!jsondata.get("registerDate").toString().isEmpty()) { String registerDate
	 * = jsondata.get("registerDate").toString(); String registerDate1 =
	 * dispenceryService.getReplaceString(registerDate); registerDate2 =
	 * HMSUtil.convertStringDateToUtilDate(registerDate1, "dd/MM/yyyy");
	 * 
	 * } String hospitalId=""; if (jsondata.get("hospitalId") != null) { hospitalId
	 * = jsondata.get("hospitalId").toString();
	 * masHospital.setHospitalId(Long.parseLong(hospitalId)); } String remark1="";
	 * if (jsondata.get("remark") != null) { String remark =
	 * jsondata.get("remark").toString(); remark1 =
	 * dispenceryService.getReplaceString(remark);
	 * //hivStdRegister.setRemarks(remark1); } String empId1=""; if
	 * (jsondata.get("empId") != null) { String empId =
	 * jsondata.get("empId").toString(); empId1 =
	 * dispenceryService.getReplaceString(empId); }
	 * 
	 * int diagnosislength=0; String[] icdValue=null; String[]
	 * medicalCategoryValue=null; String[] systemValue=null; String[]
	 * durationValue=null; String[] categoryDateValue=null; String[]
	 * nextcategoryDateValue=null; String[] typeOfCategoryValue=null; String[]
	 * hivMarkValue=null; String[] pIdValue=null; String[] icdIdValue=null;
	 * 
	 * if (jsondata.get("icd") != null) { String icd =
	 * jsondata.get("icd").toString(); String icd1 =
	 * dispenceryService.getReplaceString(icd); icdValue = icd1.split(",");
	 * 
	 * } if (jsondata.get("medcategory") != null) { String medcategory =
	 * jsondata.get("medcategory").toString(); String medcategory1 =
	 * dispenceryService.getReplaceString(medcategory); medicalCategoryValue =
	 * medcategory1.split(","); } if (jsondata.get("system") != null) { String
	 * system = jsondata.get("system").toString(); String system1 =
	 * dispenceryService.getReplaceString(system); systemValue = system1.split(",");
	 * } if (jsondata.get("typeOfCategory") != null) { String typeOfCategory =
	 * jsondata.get("typeOfCategory").toString(); String typeOfCategory1 =
	 * dispenceryService.getReplaceString(typeOfCategory); typeOfCategoryValue =
	 * typeOfCategory1.split(","); }
	 * 
	 * if (jsondata.get("duration") != null) { String duration =
	 * jsondata.get("duration").toString(); String duration1 =
	 * dispenceryService.getReplaceString(duration); durationValue =
	 * duration1.split(","); }
	 * 
	 * if (jsondata.get("categoryDate") != null) { String categoryDate =
	 * jsondata.get("categoryDate").toString(); String categoryDate1 =
	 * dispenceryService.getReplaceString(categoryDate); categoryDateValue =
	 * categoryDate1.split(","); } if (jsondata.get("nextcategoryDate") != null) {
	 * String nextcategoryDate = jsondata.get("nextcategoryDate").toString(); String
	 * nextcategoryDate1 = dispenceryService.getReplaceString(nextcategoryDate);
	 * nextcategoryDateValue = nextcategoryDate1.split(","); } List<String>
	 * list=null; if (jsondata.get("hivMark") != null) { String hivMark =
	 * jsondata.get("hivMark").toString(); String hivMark1 =
	 * dispenceryService.getReplaceString(hivMark); String[] hivMarkValue1 =
	 * hivMark1.split(","); hivMarkValue=
	 * Arrays.stream(hivMarkValue1).map(String::trim).toArray(String[]::new); list =
	 * Arrays.asList(hivMarkValue); }
	 * 
	 * 
	 * if (jsondata.get("pId") != null) { String pId =
	 * jsondata.get("pId").toString(); String pId1 =
	 * dispenceryService.getReplaceString(pId); pIdValue = pId1.split(","); } if
	 * (jsondata.get("icdId") != null) { String icdId =
	 * jsondata.get("icdId").toString(); String icdId1 =
	 * dispenceryService.getReplaceString(icdId); icdIdValue = icdId1.split(",");
	 * diagnosislength=icdIdValue.length; } //checking patient is exist or not
	 * String empName=""; String empRankId=""; String empGenderId=""; String sNo="";
	 * String dob=""; Date birthDate2=null; String patientId="";
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
	 * Patient patient=new Patient();
	 * //patient.setPatientId(Long.parseLong(empId1));
	 * 
	 * MasEmployee masEmployee=null; MasUnit masUnit=null;
	 * if(patientId.equalsIgnoreCase("")) { long relationId; String selfRelationCode
	 * = HMSUtil.getProperties("adt.properties", "SELF_RELATION_CODE").trim();
	 * relationId = patientRegistrationDao.getRelationIdFromCode(selfRelationCode);
	 * 
	 * String serviceTypeCode = HMSUtil.getProperties("adt.properties",
	 * "SERVICE_TYPE_CODE_ICG"); String registrationTypeCode =
	 * HMSUtil.getProperties("adt.properties", "ICG_REGISTRATION_TYPE_ID");
	 * MasServiceType masServiceType=getServiceType(serviceTypeCode);
	 * MasRegistrationType
	 * masRegistrationType=getRegistrationType(registrationTypeCode); //String
	 * uhidNo = patientRegistrationDao.getHinNo(sNo,relationId,masRegistrationType.
	 * getRegistrationTypeId());
	 * 
	 * masEmployee= getEmpInfoByServiceNo(sNo,hospitalId); masUnit=
	 * getMasUnit(masEmployee.getMasUnit());
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
	 * patient.setTradeId(masRank.getMasTrade().getTradeId()); }else { patient=
	 * getNameByServiceNo(sNo,hospitalId);
	 * patient.setAdministrativeSexId(Long.parseLong(empGenderId));
	 * patient.setDateOfBirth(birthDate2);
	 * patient.setRankId(Long.parseLong(empRankId)); } long pId =
	 * saveOrUpdatePatientDetails(patient); Date date = new Date(); Users users=new
	 * Users(); Criteria cr=null; Criteria cr1=null; //save data in table
	 * if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString(); users.setUserId(Long.parseLong(userId)); }
	 * 
	 * 
	 * 
	 * Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession(); tx =
	 * session1.beginTransaction(); try{ //save patient details data
	 * //session1.saveOrUpdate(patient); if (jsondata.get("hivMark") != null) { for
	 * (int i = 0; i < diagnosislength; i++) { int flag=i+1; PatientMedicalCat
	 * patientMedicalCat=new PatientMedicalCat(); if ((pIdValue[i].trim() != null &&
	 * !pIdValue[i].trim().isEmpty()) && list.contains(Integer.toString(flag))) { cr
	 * = session1.createCriteria(PatientMedicalCat.class)
	 * .add(Restrictions.eq("medicalCatId", Long.parseLong(pIdValue[i].trim())));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { patientMedicalCat=
	 * (PatientMedicalCat) cr.list().get(0); patientMedicalCat.setMarkFlag("Y");
	 * 
	 * } session1.saveOrUpdate(patientMedicalCat); }else if ((pIdValue[i].trim() ==
	 * null || pIdValue[i].trim().isEmpty())) { patientMedicalCat.setLastChgDate(new
	 * Timestamp(date.getTime())); if (categoryDateValue[i] != null &&
	 * !categoryDateValue[i].trim().isEmpty()) { Date rDate1 =
	 * HMSUtil.convertStringDateToUtilDate(categoryDateValue[i].trim(),
	 * "dd/MM/yyyy"); patientMedicalCat.setCategoryDate(rDate1); } if
	 * (nextcategoryDateValue[i] != null &&
	 * !nextcategoryDateValue[i].trim().isEmpty()) { Date rDate1 =
	 * HMSUtil.convertStringDateToUtilDate(nextcategoryDateValue[i].trim(),
	 * "dd/MM/yyyy"); patientMedicalCat.setNextCategoryDate(rDate1); } if
	 * (typeOfCategoryValue[i] != null && !typeOfCategoryValue[i].trim().isEmpty())
	 * { patientMedicalCat.setCategoryType(typeOfCategoryValue[i].trim()); }
	 * 
	 * if (systemValue[i] != null && !systemValue[i].trim().isEmpty()) {
	 * patientMedicalCat.setSystem(systemValue[i].trim()); } if (durationValue[i] !=
	 * null && !durationValue[i].trim().isEmpty()) {
	 * patientMedicalCat.setDuration(Long.parseLong(durationValue[i].trim())); } if
	 * (icdIdValue[i] != null && !icdIdValue[i].trim().isEmpty()) {
	 * patientMedicalCat.setIcdId(Long.parseLong(icdIdValue[i].trim())); } if
	 * (empId1 != null && !empId1.trim().isEmpty()) {
	 * patientMedicalCat.setPatientId(Long.parseLong(empId1.trim())); }
	 * if(list.contains(Integer.toString(flag))) patientMedicalCat.setMarkFlag("Y");
	 * if (medicalCategoryValue[i] != null &&
	 * !medicalCategoryValue[i].trim().isEmpty()) {
	 * patientMedicalCat.setMedicalCategoryId(Long.parseLong(medicalCategoryValue[i]
	 * .trim()));
	 * 
	 * }
	 * 
	 * //patientMedicalCat.setMasIcd(masIcd);
	 * //patientMedicalCat.setMasMedicalCategory(masMedicalCategory);
	 * //patientMedicalCat.setMasMedicalCategoryFit(masMedicalCategoryFit);
	 * patientMedicalCat.setMbStatus("P");
	 * //patientMedicalCat.setDateOfOrigin(dateOfOrigin);
	 * patientMedicalCat.setApplyFor("Y");
	 * 
	 * //patientMedicalCat.setPatient(patient);
	 * 
	 * //patientMedicalCat.setPlaceOfOrigin(placeOfOrigin);
	 * //patientMedicalCat.setpMedCatDate(pMedCatDate);
	 * //patientMedicalCat.setpMedCatFid(pMedCatFid);
	 * //patientMedicalCat.setpMedCatId(pMedCatId);
	 * //patientMedicalCat.setpMedFitFlag(pMedFitFlag);
	 * //patientMedicalCat.setRecommendFlag(recommendFlag);
	 * //patientMedicalCat.setVisitId(visitId);
	 * session1.saveOrUpdate(patientMedicalCat);
	 * 
	 * }
	 * 
	 * 
	 * //hiv/std data save
	 * 
	 * if(list.contains(Integer.toString(flag))) { HivStdRegister hivStdRegister =
	 * new HivStdRegister(); cr1 =
	 * session1.createCriteria(HivStdRegister.class).createAlias(
	 * "patientMedicalCat", "pmc") .add(Restrictions.eq("pmc.medicalCatId",
	 * patientMedicalCat.getMedicalCatId())); if(cr1.list().isEmpty() &&
	 * cr1.list().size()==0) { //hivStdRegister= (HivStdRegister) cr.list().get(0);
	 * hivStdRegister.setRemarks(remark1); hivStdRegister.setRegisterDate(new
	 * Timestamp(registerDate2.getTime())); hivStdRegister.setLastChgBy(users);
	 * hivStdRegister.setMasHospital(masHospital);
	 * hivStdRegister.setPatient(patient); hivStdRegister.setLastChgDate(new
	 * Timestamp(date.getTime()));
	 * hivStdRegister.setPatientMedicalCat(patientMedicalCat);
	 * hvId=(long)session1.save(hivStdRegister); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * tx.commit(); session1.flush(); session1.clear(); }else { hvId=-2; }
	 * }catch(Exception e) { if (tx != null) { try { tx.rollback(); hvId=-1; }
	 * catch(Exception re) { hvId=-1; re.printStackTrace(); } } hvId=-1;
	 * e.printStackTrace(); }
	 * 
	 * finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * 
	 * }
	 * 
	 * return hvId;
	 * 
	 * } return -1; }
	 * 
	 * @Override public List<MasRank> getRankList() { // TODO Auto-generated method
	 * stub List<MasRank> masRankList = new ArrayList<MasRank>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr=null; try {
	 * cr = session.createCriteria(MasRank.class).addOrder(Order.asc("rankName"));
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { masRankList=cr.list(); }
	 * }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masRankList; }
	 * 
	 * @Override public EmployeeBloodGroup getemployeeBloodGroup(String serviceNo,
	 * String hospitalId) { // TODO Auto-generated method stub
	 * List<EmployeeBloodGroup> employeeBloodGroupList =new
	 * ArrayList<EmployeeBloodGroup>(); EmployeeBloodGroup employeeBloodGroup=null;
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * employeeBloodGroupList = session.createCriteria(EmployeeBloodGroup.class)
	 * .add(Restrictions.eq("serviceNo",serviceNo).ignoreCase()).list();
	 * 
	 * if(employeeBloodGroupList.size()>0) { employeeBloodGroup
	 * =employeeBloodGroupList.get(0);
	 * 
	 * } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * employeeBloodGroup; }
	 * 
	 * @Override public EmployeeBloodGroup getEmployeeBloodGroupById(long
	 * empBloodGroupId) { // TODO Auto-generated method stub
	 * List<EmployeeBloodGroup> employeeBloodGroupList =new
	 * ArrayList<EmployeeBloodGroup>(); EmployeeBloodGroup employeeBloodGroup=null;
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * employeeBloodGroupList = session.createCriteria(EmployeeBloodGroup.class)
	 * .add(Restrictions.eq("employeeBloodGroupId",empBloodGroupId)).list();
	 * 
	 * if(employeeBloodGroupList.size()>0) { employeeBloodGroup
	 * =employeeBloodGroupList.get(0);
	 * 
	 * } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * employeeBloodGroup; }
	 * 
	 * @Override public long saveOrUpdatePatientDetails(Patient patient) { // TODO
	 * Auto-generated method stub long flag =0; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Transaction tx =
	 * session.beginTransaction(); session.saveOrUpdate(patient); tx.commit();
	 * flag=1; } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return flag; }
	 * 
	 * @Override public MasServiceType getServiceType(String serviceTypeCode) { //
	 * TODO Auto-generated method stub List<MasServiceType> serviceTypeList =new
	 * ArrayList<MasServiceType>(); MasServiceType masServiceType=null;
	 * 
	 * try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * serviceTypeList = session.createCriteria(MasServiceType.class)
	 * .add(Restrictions.eq("serviceTypeCode",serviceTypeCode).ignoreCase()).list();
	 * 
	 * if(serviceTypeList.size()>0) { masServiceType =serviceTypeList.get(0); } }
	 * catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masServiceType; }
	 * 
	 * @Override public MasRegistrationType getRegistrationType(String
	 * registrationTypeCode) { // TODO Auto-generated method stub
	 * List<MasRegistrationType> registrationTypeList =new
	 * ArrayList<MasRegistrationType>(); MasRegistrationType
	 * masRegistrationType=null;
	 * 
	 * try {
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * registrationTypeList = session.createCriteria(MasRegistrationType.class)
	 * .add(Restrictions.eq("registrationTypeCode",Long.parseLong(
	 * registrationTypeCode))).list();
	 * 
	 * if(registrationTypeList.size()>0) { masRegistrationType
	 * =registrationTypeList.get(0); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); }finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masRegistrationType; }
	 * 
	 * @Override public Map<String, Object> getHospitalVisitDetails(HashMap<String,
	 * String> requestData) { // TODO Auto-generated method stub
	 * List<HospitalVisitRegister> hospitalVisitRegisterList = new
	 * ArrayList<HospitalVisitRegister>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); long hospitalVisitId =
	 * Long.parseLong(requestData.get("hospitalVisitId").toString()); Criteria
	 * cr=null; try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); cr =
	 * session.createCriteria(HospitalVisitRegister.class)
	 * .add(Restrictions.eq("hostpitalVisitRegisterId", hospitalVisitId));
	 * 
	 * if(!cr.list().isEmpty() && cr.list().size()>0) { hospitalVisitRegisterList=
	 * cr.list(); } } catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } finally {
	 * 
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * map.put("hospitalVisitRegisterList", hospitalVisitRegisterList); return map;
	 * }
	 */
}
