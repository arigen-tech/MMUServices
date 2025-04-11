package com.mmu.services.dao.impl;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mmu.services.dao.DgOrderdtDao;
import com.mmu.services.dao.DgOrderhdDao;
import com.mmu.services.dao.DgResultEntryDtDao;
import com.mmu.services.dao.DgResultEntryHdDao;
import com.mmu.services.dao.MasAppointmentTypeDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.dao.VisitDao;
import com.mmu.services.entity.Labour;
import com.mmu.services.entity.MMUDepartment;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasBloodGroup;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.MasIdentificationType;
import com.mmu.services.entity.MasLabor;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasReligion;
import com.mmu.services.entity.MasWard;
import com.mmu.services.entity.MasZone;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDataUpload;
import com.mmu.services.entity.PatientSymptom;
import com.mmu.services.entity.StoreInternalIndentT;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import javax.xml.bind.DatatypeConverter;

import java.awt.image.BufferedImage;
import java.io.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

@Repository
@Transactional
public class PatientRegistrationDaoImpl implements PatientRegistrationDao {

	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String DG_ORDER_DT = databaseScema + "." + "DG_ORDER_DT";
	final String DG_ORDER_HD = databaseScema + "." + "DG_ORDER_HD";
	final String DG_MAS_INVESTIGATION = databaseScema + "." + "DG_MAS_INVESTIGATION";
	final String DG_RESULT_ENTRY_DT = databaseScema + "." + "DG_RESULT_ENTRY_DT";
	final String DG_RESULT_ENTRY_HD = databaseScema + "." + "DG_RESULT_ENTRY_HD";
	final String MAS_SUB_CHARGECODE = databaseScema + "." + "MAS_SUB_CHARGECODE";
	final String DG_UOM = databaseScema + "." + "DG_UOM";
	final String DG_SUB_MAS_INVESTIGATION = databaseScema + "." + "DG_SUB_MAS_INVESTIGATION";

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	MedicalExamDAO medicalExamDAO;
	@Autowired
	DgResultEntryHdDao dgResultEntryHdDao;
	@Autowired
	DgOrderdtDao dgOrderdtDao;
	@Autowired
	DgResultEntryDtDao dgResultEntryDtDao;
	@Autowired
	MasAppointmentTypeDao masAppointmentTypeDao;
	@Autowired
	VisitDao visitDao;
	@Autowired
	DgOrderhdDao dgOrderhdDao;

	@Autowired
	OpdMasterDaoImpl opdMasterDaoImpl;
	
	@Autowired
	private Environment environment;
	/*
	 * @SuppressWarnings({ "unchecked"})
	 * 
	 * @Override public Map<String, Object>
	 * findPatientAndDependentFromEmployee(String serviceNo) { Map<String, Object>
	 * map = new HashMap<String, Object>(); List<MasEmployee> employeeList = new
	 * ArrayList<MasEmployee>(); List<MasEmployee> pidFromEmployeeList = new
	 * ArrayList<MasEmployee>(); List<Patient> patientList = new
	 * ArrayList<Patient>(); List<MasEmployeeDependent> employeeDependentList = new
	 * ArrayList<MasEmployeeDependent>();
	 * 
	 * String selfRelationCode = HMSUtil.getProperties("adt.properties",
	 * "SELF_RELATION_CODE").trim(); long selfRelationId =
	 * getRelationIdFromCode(selfRelationCode); long registrationTypeId =
	 * Long.parseLong(HMSUtil.getProperties("adt.properties",
	 * "ICG_REGISTRATION_TYPE_ID")); String employeePId="";
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * 
	 * patientList = session.createCriteria(Patient.class)
	 * .add(Restrictions.eq("serviceNo", serviceNo).ignoreCase())
	 * .add(Restrictions.eq("masRegistrationType.registrationTypeId",
	 * registrationTypeId)) .add(Restrictions.isNotNull("uhidNo")).list();
	 * 
	 * int i = 0; if (patientList.size() > 0) {
	 * 
	 * Long[] relationArray = new Long[patientList.size()]; String[] nameArray = new
	 * String[patientList.size()];
	 * 
	 * for (Patient patient : patientList) { relationArray[i] =
	 * patient.getMasRelation().getRelationId(); nameArray[i] =
	 * patient.getPatientName().trim(); i++; }
	 * 
	 * boolean contains = Arrays.stream(relationArray).anyMatch(x -> x ==
	 * selfRelationId);
	 * 
	 * pidFromEmployeeList
	 * =session.createCriteria(MasEmployee.class).add(Restrictions.eq("serviceNo",
	 * serviceNo).ignoreCase()).list(); if(pidFromEmployeeList.size()>0) {
	 * employeePId = pidFromEmployeeList.get(0).getEmployeePid(); }
	 * 
	 * if (contains) { // true employeeDependentList =
	 * session.createCriteria(MasEmployeeDependent.class)
	 * .add(Restrictions.eq("employeeDependentPid", employeePId)) .add(Restrictions
	 * .not(Restrictions.and(Restrictions.in("masRelation.relationId",
	 * relationArray), Restrictions.in("employeeDependentName", nameArray))))
	 * .list(); } else { // false
	 * 
	 * employeeList = session.createCriteria(MasEmployee.class)
	 * .add(Restrictions.eq("serviceNo", serviceNo).ignoreCase()).list();
	 * 
	 * employeeDependentList = session.createCriteria(MasEmployeeDependent.class)
	 * .add(Restrictions.eq("employeeDependentPid", employeePId)) .add(Restrictions
	 * .not(Restrictions.and(Restrictions.in("masRelation.relationId",
	 * relationArray), Restrictions.in("employeeDependentName", nameArray))))
	 * .list(); } } else { employeeList = session.createCriteria(MasEmployee.class)
	 * .add(Restrictions.eq("serviceNo", serviceNo).ignoreCase()).list();
	 * 
	 * pidFromEmployeeList
	 * =session.createCriteria(MasEmployee.class).add(Restrictions.eq("serviceNo",
	 * serviceNo).ignoreCase()).list(); if(pidFromEmployeeList.size()>0) {
	 * employeePId = pidFromEmployeeList.get(0).getEmployeePid(); }
	 * 
	 * employeeDependentList = session.createCriteria(MasEmployeeDependent.class)
	 * .add(Restrictions.eq("employeeDependentPid", employeePId)) .list();
	 * 
	 * } map.put("patientList", patientList); map.put("employeeList", employeeList);
	 * map.put("employeeDependentList", employeeDependentList); } catch (Exception
	 * e) { getHibernateUtils.getHibernateUtlis().CloseConnection();
	 * e.printStackTrace(); } finally {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); } return map; }
	 * 
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasDepartment> getDepartmentList() {
	 * List<MasDepartment> departmentList = new ArrayList<MasDepartment>(); String
	 * departmentTypeCode = HMSUtil.getProperties("adt.properties",
	 * "DEPARTMENT_TYPE_CODE").trim();
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * long departmentTypeId = CommonUtil.getDepartmentTypeIdAgainstCode(session,
	 * departmentTypeCode);
	 * departmentList=session.createCriteria(MasDepartment.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .createAlias("masDepartmentType", "dt")
	 * .add(Restrictions.eq("dt.departmentTypeId",departmentTypeId))
	 * .addOrder(Order.asc("departmentName")) .list();
	 * 
	 * }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * departmentList; }
	 * 
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasBloodGroup> getBloodGroupList() {
	 * List<MasBloodGroup> bloodGroupList = new ArrayList<MasBloodGroup>(); try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * bloodGroupList= session.createCriteria(MasBloodGroup.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .addOrder(Order.asc("bloodGroupName")).list(); }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * bloodGroupList; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasMedicalCategory> getMedicalCategoryList() {
	 * List<MasMedicalCategory> medicalCategoryList = new
	 * ArrayList<MasMedicalCategory>();
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * medicalCategoryList = session.createCriteria(MasMedicalCategory.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .addOrder(Order.asc("medicalCategoryName")).list(); }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return medicalCategoryList; }
	 * 
	 * 
	 * //Code for web+portal+online token
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getTokenNoForDepartmentMultiVisit(long
	 * departmentId,long hospitalId,long appointmentTypeId,String visitFlag,String
	 * visitDate,long patientId) { Map<String, Object>map = new HashMap<String,
	 * Object>(); List<DoctorRoaster> doctorRosterList = new
	 * ArrayList<DoctorRoaster>();
	 * 
	 * long appSessionId=0; String tokenMsg="";
	 * 
	 * String codeOpd = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_OPD").trim(); long appointmentTypeIdOPD=
	 * getAppointmentTypeIdFromCode(codeOpd); try { Date visitStartDate= null; Date
	 * visitEndDate= null; Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); if(!visitDate.isEmpty()
	 * && visitDate!=null) { visitStartDate =
	 * HMSUtil.convertStringDateToUtilDate(visitDate, "dd-MM-yyyy"); Date
	 * nextDatefromVisit = HMSUtil.convertStringDateToUtilDate(visitDate,
	 * "dd-MM-yyyy"); visitEndDate =HMSUtil.getNextDate(nextDatefromVisit) ; }else {
	 * Date today = new Date(); String dateString =
	 * HMSUtil.convertDateToStringFormat(today, "dd-MM-yyyy"); try { visitStartDate
	 * = new SimpleDateFormat("dd-MM-yyyy").parse(dateString); } catch
	 * (ParseException e) { e.printStackTrace(); } visitEndDate =
	 * HMSUtil.getNextDate(today); } doctorRosterList
	 * =session.createCriteria(DoctorRoaster.class)
	 * .add(Restrictions.eq("masDepartment.departmentId", departmentId))
	 * .add(Restrictions.eq("masHospital.hospitalId", hospitalId))
	 * .add(Restrictions.eq("roasterDate", visitStartDate)) .list();
	 * 
	 * if(doctorRosterList.size()>0) { String rosterValues =
	 * doctorRosterList.get(0).getRoasterValue(); String[] arrayValues =
	 * rosterValues.split(","); String appTypeAndValue= ""; for(int
	 * i=0;i<arrayValues.length;i++) { appTypeAndValue = arrayValues[i]; String[]
	 * arr = appTypeAndValue.split("@");
	 * if(appTypeAndValue.substring(appTypeAndValue.lastIndexOf('@')+1).
	 * equalsIgnoreCase(String.valueOf(appointmentTypeId))) {
	 * if(arr[0].equalsIgnoreCase("y")) { appSessionId =
	 * getAppointmentSessionId(hospitalId, departmentId, appointmentTypeId);
	 * if(appSessionId!=0) { // start with appointmentsessionId not equal zero
	 * String dayName = new SimpleDateFormat("EEEE",
	 * Locale.ENGLISH).format(visitStartDate.getTime());
	 * 
	 * if (visitFlag.equalsIgnoreCase("P")) { // for portal long visitIdInitiated=0;
	 * long medExBrdId=0; if (appointmentTypeId != appointmentTypeIdOPD) { String
	 * comboVisitIdMedExBrId =
	 * checkForAlreadyInitiatedVisit(session,patientId,appointmentTypeId,
	 * visitStartDate,visitEndDate); if(!comboVisitIdMedExBrId.isEmpty()) {
	 * if(comboVisitIdMedExBrId.equalsIgnoreCase("0")) { // Details need to fill in
	 * portal tokenMsg =
	 * "Please fill details for release and discharge on web portal before proceeding for appointment."
	 * ; map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } else if(comboVisitIdMedExBrId.equalsIgnoreCase("-1")) {
	 * tokenMsg = "Please visit MI room to collect the investigation slip.";
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); }else { String[] splitCombo =
	 * comboVisitIdMedExBrId.split("~"); visitIdInitiated =
	 * Long.parseLong(splitCombo[0]); medExBrdId = Long.parseLong(splitCombo[1]);
	 * map = generateTokenForPortal(session, hospitalId, departmentId, dayName,
	 * appSessionId, visitStartDate, visitEndDate, appointmentTypeId);
	 * map.put("visitId", visitIdInitiated); map.put("medExBrdId", medExBrdId); }
	 * 
	 * }else { tokenMsg = "Please visit MI room to collect the investigation slip.";
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } } else { map =
	 * generateTokenForPortal(session,hospitalId,departmentId,dayName,appSessionId,
	 * visitStartDate,visitEndDate,appointmentTypeId); }
	 * 
	 * } else if (visitFlag.equalsIgnoreCase("M")) { // for Mobile long
	 * visitIdInitiated=0; long medExBrdId=0; if (appointmentTypeId !=
	 * appointmentTypeIdOPD) { String comboVisitIdMedExBrId =
	 * checkForAlreadyInitiatedVisit(session,patientId,appointmentTypeId,
	 * visitStartDate,visitEndDate); if(!comboVisitIdMedExBrId.isEmpty()) {
	 * if(comboVisitIdMedExBrId.equalsIgnoreCase("0")) { // Details need to fill in
	 * portal tokenMsg =
	 * "Please fill details for release and discharge on web portal before proceeding for appointment."
	 * ; map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } else if(comboVisitIdMedExBrId.equalsIgnoreCase("-1")) {
	 * tokenMsg = "Please visit MI room to collect the investigation slip.";
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); }else { String[] splitCombo =
	 * comboVisitIdMedExBrId.split("~"); visitIdInitiated =
	 * Long.parseLong(splitCombo[0]); medExBrdId = Long.parseLong(splitCombo[1]);
	 * map = generateTokenForMobile(session, hospitalId, departmentId, dayName,
	 * appSessionId, visitStartDate, visitEndDate, appointmentTypeId);
	 * map.put("visitId", visitIdInitiated); map.put("medExBrdId", medExBrdId); }
	 * 
	 * }else { tokenMsg = "Please visit MI room to collect the investigation slip.";
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } } else { map = generateTokenForMobile(session,
	 * hospitalId, departmentId, dayName, appSessionId, visitStartDate,
	 * visitEndDate,appointmentTypeId); } } else { if (appointmentTypeId !=
	 * appointmentTypeIdOPD) { map = checkAppointForMEorMB(session, appSessionId,
	 * appointmentTypeId, hospitalId, patientId, visitStartDate, visitEndDate,
	 * departmentId, dayName); } else { map = getTokenNumberForVisit(session,
	 * appSessionId, appointmentTypeId, hospitalId, visitStartDate, visitEndDate,
	 * departmentId, dayName); } } } } else { tokenMsg = "Doctor is not available.";
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } break; }else { tokenMsg =
	 * "Doctor Roster is not available (click here)"; map.put("APPTYPE","ROSTER");
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } } }else { tokenMsg =
	 * "Doctor Roster is not available (click here)"; map.put("APPTYPE","ROSTER");
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return map; }
	 * 
	 * 
	 * @SuppressWarnings({ "unchecked","static-access" }) private String
	 * checkForAlreadyInitiatedVisit(Session session, long patientId, long
	 * appointmentTypeId, Date visitStartDate, Date visitEndDate) { long
	 * visitIdInitiated = 0; long medExBrdId = 0; String comboVisitIdAndmedExBrId =
	 * ""; Calendar cal = Calendar.getInstance(); int currentYear =
	 * cal.getInstance().get(Calendar.YEAR); cal.set(Calendar.YEAR, currentYear);
	 * cal.set(Calendar.DAY_OF_YEAR, 1); cal.set(Calendar.HOUR, 0);
	 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); // for converting time into 00:00:00
	 * 
	 * Date yearStartDate = cal.getTime(); Date currentDate = visitEndDate;
	 * 
	 * List<Visit> existingList =
	 * session.createCriteria(Visit.class).createAlias("masAppointmentType",
	 * "appType") .add(Restrictions.eq("appType.appointmentTypeId",
	 * appointmentTypeId)) .add(Restrictions.eq("visitStatus", "w").ignoreCase())
	 * 
	 * .add(Restrictions.disjunction().add(Restrictions.eq("examStatus",
	 * "V").ignoreCase()) .add(Restrictions.eq("examStatus", "T").ignoreCase())
	 * .add(Restrictions.eq("examStatus", "I").ignoreCase())
	 * .add(Restrictions.eq("examStatus", "S").ignoreCase()))
	 * 
	 * // .add(Restrictions.or(Restrictions.eq("examStatus",
	 * "V").ignoreCase(),Restrictions.eq("examStatus", "T").ignoreCase()))
	 * 
	 * .add(Restrictions.ne("visitFlag", "E").ignoreCase())
	 * .add(Restrictions.eq("patient.patientId",
	 * patientId)).add(Restrictions.ge("visitDate", yearStartDate))
	 * .add(Restrictions.lt("visitDate",
	 * currentDate)).addOrder(Order.desc("visitId")).list();
	 * 
	 * if (existingList.size() > 0) { String examStatusIndb =
	 * existingList.get(0).getExamStatus(); if (examStatusIndb.equalsIgnoreCase("I")
	 * || examStatusIndb.equalsIgnoreCase("S")) { comboVisitIdAndmedExBrId = "-1"; }
	 * else { visitIdInitiated = existingList.get(0).getVisitId(); medExBrdId =
	 * existingList.get(0).getMasMedExam().getMedicalExamId();
	 * 
	 * String medExBrdCode =
	 * existingList.get(0).getMasMedExam().getMedicalExamCode(); String meForm18 =
	 * HMSUtil.getProperties("resource.properties", "meForm18").trim(); String
	 * mbForm16 = HMSUtil.getProperties("resource.properties", "mbForm16").trim();
	 * 
	 * if (medExBrdCode.equalsIgnoreCase(meForm18) ||
	 * medExBrdCode.equalsIgnoreCase(mbForm16)) { boolean releaseFlag =
	 * checkDetailsFilledOrNotPortal(visitIdInitiated); if (releaseFlag) {
	 * comboVisitIdAndmedExBrId = visitIdInitiated + "~" + medExBrdId; } else {
	 * comboVisitIdAndmedExBrId = "0"; // Details need to fill in Portal. } } else {
	 * comboVisitIdAndmedExBrId = visitIdInitiated + "~" + medExBrdId;
	 * 
	 * } }
	 * 
	 * } return comboVisitIdAndmedExBrId; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @SuppressWarnings("unchecked") private Map<String, Object>
	 * generateTokenForMobile(Session session, long hospitalId, long departmentId,
	 * String dayName, long appSessionId, Date visitStartDate, Date
	 * visitEndDate,long appointmentTypeId) { Map<String, Object>map = new
	 * HashMap<String, Object>(); List<AppSetup> appointmentSetupListForMobile = new
	 * ArrayList<AppSetup>();
	 * 
	 * appointmentSetupListForMobile=session.createCriteria(AppSetup.class)
	 * .add(Restrictions.eq("masHospital.hospitalId", hospitalId))
	 * .add(Restrictions.eq("masDepartment.departmentId", departmentId))
	 * .add(Restrictions.eq("days", dayName))
	 * .add(Restrictions.eq("masAppointmentSession.id", appSessionId)) .list();
	 * 
	 * if(!appointmentSetupListForMobile.isEmpty() &&
	 * appointmentSetupListForMobile.size()>0) { List<HashMap<String, Object>>
	 * mobileTokenList = new ArrayList<HashMap<String, Object>>(); long portalToken
	 * =(appointmentSetupListForMobile.get(0).getTotalPortalToken().longValue());
	 * long mobileToken
	 * =(appointmentSetupListForMobile.get(0).getTotalMobileToken().longValue());
	 * //Check this line while execution long tokenInterval =
	 * (appointmentSetupListForMobile.get(0).getTotalInterval().longValue()); long
	 * startToken =
	 * (appointmentSetupListForMobile.get(0).getStartToken().longValue()); long
	 * totalToken =
	 * (appointmentSetupListForMobile.get(0).getTotalToken().longValue()); String
	 * startTime = appointmentSetupListForMobile.get(0).getStartTime(); String
	 * endTime = appointmentSetupListForMobile.get(0).getEndTime();
	 * 
	 * long minDaysLimit =
	 * (appointmentSetupListForMobile.get(0).getMinNoOfDays().longValue()); long
	 * maxDaysLimt =
	 * (appointmentSetupListForMobile.get(0).getMaxNoOfDays().longValue());
	 * 
	 * long minutes = HMSUtil.calculateTotalMinutes(startTime, endTime); float
	 * timePerTokenInMinutes = ((minutes)/totalToken);
	 * 
	 * 
	 * String tokenStartTime=""; String tokenEndTime="";
	 * 
	 * String tokenStatus=""; long mobileTokenCount=1; List<Visit> tokenList = new
	 * ArrayList<Visit>(); long tokenValue =
	 * stratTokenValueForMobile(startToken,portalToken,totalToken,tokenInterval);
	 * 
	 * String
	 * tokenStartTimeMobile=startTokenTimeForMobile(startTime,timePerTokenInMinutes,
	 * tokenInterval,startToken,portalToken,totalToken);
	 * 
	 * if(totalToken>0 && mobileToken>0 && tokenInterval!=0){ while
	 * (mobileTokenCount<=mobileToken && tokenValue <= totalToken) {
	 * HashMap<String,Object> tokenMap = new HashMap<String, Object>();
	 * mobileTokenCount = mobileTokenCount + 1; //token status need to fetch here
	 * tokenList= session.createCriteria(Visit.class)
	 * .add(Restrictions.eq("tokenNo", tokenValue)) .createAlias("masHospital",
	 * "hospital") .add(Restrictions.eq("hospital.hospitalId", hospitalId))
	 * .createAlias("masAppointmentSession",
	 * "session").add(Restrictions.eq("session.id", appSessionId))
	 * .add(Restrictions.ge("visitDate", visitStartDate))
	 * .add(Restrictions.lt("visitDate", visitEndDate)).list();
	 * if(tokenList.size()>0) { // Here I have to check record with status for
	 * cancel // for each loop apply here for(Visit visit : tokenList) {
	 * if(visit.getVisitStatus().equalsIgnoreCase("W") &&
	 * !(visit.getVisitStatus().equalsIgnoreCase("N"))) { tokenStatus ="booked";
	 * tokenMap.put("tokenValue", tokenValue); tokenMap.put("tokenStatus",
	 * tokenStatus); break; }else { tokenStatus ="available";
	 * tokenMap.put("tokenValue", tokenValue); tokenMap.put("tokenStatus",
	 * tokenStatus); } }
	 * 
	 * }else { tokenStatus ="available"; tokenMap.put("tokenValue", tokenValue);
	 * tokenMap.put("tokenStatus", tokenStatus);
	 * 
	 * }
	 * 
	 * tokenStartTime = tokenStartTimeMobile; tokenEndTime =
	 * HMSUtil.addingMinutes(tokenStartTime, (int)timePerTokenInMinutes);
	 * tokenStartTimeMobile = HMSUtil.addingMinutes(tokenEndTime,
	 * (int)(timePerTokenInMinutes*(tokenInterval-1)));
	 * 
	 * tokenValue = tokenValue + tokenInterval; //This token-value will add in next
	 * count
	 * 
	 * tokenMap.put("tokenStartTime", tokenStartTime); tokenMap.put("tokenEndTime",
	 * tokenEndTime); tokenMap.put("tokenDate",
	 * HMSUtil.convertDateToStringFormat(visitStartDate, "dd-MM-yyyy"));
	 * tokenMap.put("maxDaysLimt",maxDaysLimt);
	 * tokenMap.put("minDaysLimit",minDaysLimit);
	 * 
	 * mobileTokenList.add(tokenMap); } map.put("tokenMsg", mobileTokenList);
	 * map.put("appointmentTypeId",appointmentTypeId ); } } return map; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked") private Map<String, Object>
	 * generateTokenForPortal(Session session, long hospitalId, long departmentId,
	 * String dayName, long appSessionId, Date visitStartDate, Date
	 * visitEndDate,long appointmentTypeId) { Map<String, Object>map = new
	 * HashMap<String, Object>(); List<AppSetup> appointmentSetupListForPortal = new
	 * ArrayList<AppSetup>();
	 * 
	 * appointmentSetupListForPortal = session.createCriteria(AppSetup.class)
	 * .add(Restrictions.eq("masHospital.hospitalId", hospitalId))
	 * .add(Restrictions.eq("masDepartment.departmentId", departmentId))
	 * .add(Restrictions.eq("days", dayName))
	 * .add(Restrictions.eq("masAppointmentSession.id", appSessionId)).list();
	 * 
	 * if (!appointmentSetupListForPortal.isEmpty() &&
	 * appointmentSetupListForPortal.size() > 0) { List<HashMap<String, Object>>
	 * portalTokenList = new ArrayList<HashMap<String, Object>>(); long totalToken =
	 * (appointmentSetupListForPortal.get(0).getTotalToken()!=null?
	 * appointmentSetupListForPortal.get(0).getTotalToken().longValue():0);
	 * if(totalToken>0) { long portalToken =
	 * (appointmentSetupListForPortal.get(0).getTotalPortalToken()!=null?
	 * appointmentSetupListForPortal.get(0).getTotalPortalToken().longValue():0); //
	 * Check this line while execution long tokenInterval =
	 * (appointmentSetupListForPortal.get(0).getTotalInterval()!=null?
	 * appointmentSetupListForPortal.get(0).getTotalInterval().longValue():0); long
	 * startToken = (appointmentSetupListForPortal.get(0).getStartToken()!=null?
	 * appointmentSetupListForPortal.get(0).getStartToken().longValue():0);
	 * 
	 * String startTime = appointmentSetupListForPortal.get(0).getStartTime();
	 * String endTime = appointmentSetupListForPortal.get(0).getEndTime();
	 * 
	 * long minDaysLimit =
	 * (appointmentSetupListForPortal.get(0).getMinNoOfDays()!=null?
	 * appointmentSetupListForPortal.get(0).getMinNoOfDays().longValue():0); long
	 * maxDaysLimt = (appointmentSetupListForPortal.get(0).getMaxNoOfDays()!=null?
	 * appointmentSetupListForPortal.get(0).getMaxNoOfDays().longValue():0);
	 * 
	 * long minutes = HMSUtil.calculateTotalMinutes(startTime, endTime); long
	 * timePerTokenInMinutes = ((minutes) / totalToken);
	 * 
	 * String tokenStartTime = ""; String tokenEndTime = "";
	 * 
	 * String tokenStatus = ""; long portalTokenCount = 1; List<Visit> tokenList =
	 * new ArrayList<Visit>(); long tokenValue = startToken; if (totalToken > 0 &&
	 * portalToken > 0 && tokenInterval != 0) { while (portalTokenCount <=
	 * portalToken && tokenValue <= totalToken) { HashMap<String, Object> tokenMap =
	 * new HashMap<String, Object>(); portalTokenCount = portalTokenCount + 1;
	 * 
	 * // token status need to fetch here tokenList =
	 * session.createCriteria(Visit.class) .add(Restrictions.eq("tokenNo",
	 * tokenValue)) .createAlias("masAppointmentSession", "session")
	 * .add(Restrictions.eq("session.id", appSessionId)) .createAlias("masHospital",
	 * "hospital") .add(Restrictions.eq("hospital.hospitalId", hospitalId))
	 * .add(Restrictions.ge("visitDate", visitStartDate))
	 * .add(Restrictions.lt("visitDate", visitEndDate)).list(); if (tokenList.size()
	 * > 0) { // Here I have to check record with status for cancel for (Visit visit
	 * : tokenList) { if (visit.getVisitStatus().equalsIgnoreCase("W") && !(visit
	 * .getVisitStatus().equalsIgnoreCase("N"))) { tokenStatus = "booked";
	 * tokenMap.put("tokenValue", tokenValue); tokenMap.put("tokenStatus",
	 * tokenStatus); break; } else { tokenStatus = "available";
	 * tokenMap.put("tokenValue", tokenValue); tokenMap.put("tokenStatus",
	 * tokenStatus); } } // for each loop will apply here } else { tokenStatus =
	 * "available"; tokenMap.put("tokenValue", tokenValue);
	 * tokenMap.put("tokenStatus", tokenStatus);
	 * 
	 * }
	 * 
	 * tokenStartTime = startTime; tokenEndTime =
	 * HMSUtil.addingMinutes(tokenStartTime, (int) timePerTokenInMinutes); startTime
	 * = HMSUtil.addingMinutes(tokenEndTime, (int) (timePerTokenInMinutes *
	 * (tokenInterval-1)));
	 * 
	 * tokenValue = tokenValue + tokenInterval; // This token-value will // add in
	 * next count.
	 * 
	 * tokenMap.put("tokenStartTime", tokenStartTime); tokenMap.put("tokenEndTime",
	 * tokenEndTime); tokenMap.put("tokenDate", HMSUtil
	 * .convertDateToStringFormat(visitStartDate, "dd-MM-yyyy"));
	 * tokenMap.put("maxDaysLimt", maxDaysLimt); tokenMap.put("minDaysLimit",
	 * minDaysLimit);
	 * 
	 * portalTokenList.add(tokenMap); } map.put("tokenMsg", portalTokenList);
	 * map.put("appointmentTypeId", appointmentTypeId); } }else {
	 * map.put("tokenMsg", "No Appointment Setup For "+dayName);
	 * map.put("appointmentTypeId", appointmentTypeId); } }
	 * 
	 * return map; }
	 * 
	 * @SuppressWarnings({ "unchecked", "static-access" }) private Map<String,
	 * Object> checkAppointForMEorMB(Session session, long appSessionId, long
	 * appointmentTypeId, long hospitalId, long patientId, Date visitStartDate, Date
	 * visitEndDate, long departmentId, String dayName) { Map<String, Object> map =
	 * new HashMap<String, Object>();
	 * 
	 * String examStatusValidate = HMSUtil.getProperties("adt.properties",
	 * "EXAM_STATUS_VALIDATE"); String examStatusInitiate =
	 * HMSUtil.getProperties("adt.properties", "EXAM_STATUS_INITIATE"); String
	 * examStatusTempSave = HMSUtil.getProperties("adt.properties",
	 * "EXAM_STATUS_TEMP_SAVE"); String examStatusReject =
	 * HMSUtil.getProperties("adt.properties", "EXAM_STATUS_REJECTED"); String
	 * examStatusFinal = HMSUtil.getProperties("adt.properties",
	 * "EXAM_STATUS_FINAL"); String examStatusComplete =
	 * HMSUtil.getProperties("adt.properties", "EXAM_STATUS_COMPLETE"); String
	 * examStatusTokenAvailable = HMSUtil.getProperties("adt.properties",
	 * "EXAM_STATUS_TOKEN_AVAILABLE");
	 * 
	 * String appointmentTypeCodeME = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_ME"); long appointmentTypeIdME=
	 * getAppointmentTypeIdFromCode(appointmentTypeCodeME);
	 * 
	 * String tokenMsg = "";
	 * 
	 * Calendar cal = Calendar.getInstance(); int currentYear =
	 * cal.getInstance().get(Calendar.YEAR); cal.set(Calendar.YEAR, currentYear);
	 * cal.set(Calendar.DAY_OF_YEAR, 1); cal.set(Calendar.HOUR, 0);
	 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
	 * cal.set(Calendar.HOUR_OF_DAY, 0); // for converting time into 00:00:00
	 * 
	 * Date yearStartDate = cal.getTime(); Date currentDate = visitEndDate;
	 * 
	 * Criteria cr =
	 * session.createCriteria(Visit.class).createAlias("masAppointmentType",
	 * "appType") .add(Restrictions.eq("appType.appointmentTypeId",
	 * appointmentTypeId)) .add(Restrictions.ne("visitFlag", "E").ignoreCase())
	 * .add(Restrictions.eq("patient.patientId", patientId));
	 * 
	 * if (appointmentTypeId == appointmentTypeIdME) { cr =
	 * cr.add(Restrictions.ge("visitDate",
	 * yearStartDate)).add(Restrictions.lt("visitDate", currentDate)); } List<Visit>
	 * existingList = cr.addOrder(Order.desc("visitId")).list(); if
	 * (existingList.size() > 0 &&
	 * !existingList.get(0).getExamStatus().equalsIgnoreCase(examStatusReject)) {
	 * 
	 * if (existingList.get(0).getExamStatus().equalsIgnoreCase(
	 * examStatusTokenAvailable) ||
	 * existingList.get(0).getExamStatus().equalsIgnoreCase(examStatusFinal)) {
	 * tokenMsg = "T"; map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); map.put("examType",
	 * existingList.get(0).getMasMedExam().getMedicalExamCode()); } else if
	 * (existingList.get(0).getExamStatus().equalsIgnoreCase(examStatusValidate)) {
	 * // Token for ME/MB, Patient coming for medical exam/medical board token need
	 * to generate long medExBrdId =
	 * existingList.get(0).getMasMedExam().getMedicalExamId(); long visitId =
	 * existingList.get(0).getVisitId(); boolean releaseFlag = false; String
	 * medExBrdCode = existingList.get(0).getMasMedExam().getMedicalExamCode();
	 * String meForm18 = HMSUtil.getProperties("resource.properties",
	 * "meForm18").trim(); String mbForm16 =
	 * HMSUtil.getProperties("resource.properties", "mbForm16").trim(); if
	 * (medExBrdCode.equalsIgnoreCase(meForm18) ||
	 * medExBrdCode.equalsIgnoreCase(mbForm16)) { releaseFlag =
	 * checkDetailsFilledOrNotPortal(visitId); if (releaseFlag) { // Details are
	 * filled into portal map = getTokenNumberForVisit(session, appSessionId,
	 * appointmentTypeId, hospitalId, visitStartDate, visitEndDate, departmentId,
	 * dayName); map.put("medExBrdId", medExBrdId); map.put("visitId", visitId); }
	 * else { // Details are not filled into portal tokenMsg = "PE";
	 * map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); map.put("examType",
	 * existingList.get(0).getMasMedExam().getMedicalExamCode()); } } else { map =
	 * getTokenNumberForVisit(session, appSessionId, appointmentTypeId, hospitalId,
	 * visitStartDate, visitEndDate, departmentId, dayName); map.put("medExBrdId",
	 * medExBrdId); map.put("visitId", visitId); }
	 * 
	 * } else if
	 * (existingList.get(0).getExamStatus().equalsIgnoreCase(examStatusInitiate) ||
	 * existingList.get(0).getExamStatus().equalsIgnoreCase(examStatusTempSave)) {
	 * tokenMsg = "AI"; map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); map.put("examType",
	 * existingList.get(0).getMasMedExam().getMedicalExamCode()); } else if
	 * (existingList.get(0).getExamStatus().equalsIgnoreCase(examStatusComplete)) {
	 * tokenMsg = "C"; map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); map.put("examType",
	 * existingList.get(0).getMasMedExam().getMedicalExamCode()); } } else { // When
	 * ME & MB is initiated
	 * 
	 * if (appointmentTypeId == appointmentTypeIdME) { List<Visit> lastMEDoneList=
	 * session.createCriteria(Visit.class).createAlias("masAppointmentType",
	 * "appType") .add(Restrictions.eq("appType.appointmentTypeId",
	 * appointmentTypeId)) .add(Restrictions.ne("visitFlag", "E").ignoreCase())
	 * .add(Restrictions.eq("visitStatus", "c").ignoreCase())
	 * .add(Restrictions.eq("patient.patientId", patientId))
	 * .addOrder(Order.desc("visitId")).list();
	 * 
	 * if (lastMEDoneList.size() > 0) { String stringVlauelastAgeME =
	 * lastMEDoneList.get(0).getMeAge(); String[] ageArrayME =
	 * stringVlauelastAgeME.split(","); String lastAgeME =
	 * ageArrayME[ageArrayME.length-1]; long timestamp =
	 * lastMEDoneList.get(0).getVisitDate().getTime(); Calendar calDate =
	 * Calendar.getInstance(); calDate.setTimeInMillis(timestamp); int MEYear =
	 * calDate.get(Calendar.YEAR);
	 * 
	 * map.put("lastAgeME", lastAgeME); map.put("MEYear", MEYear);
	 * 
	 * } }
	 * 
	 * 
	 * tokenMsg = "I"; map.put("tokenMsg", tokenMsg); map.put("appointmentTypeId",
	 * appointmentTypeId); } return map; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @SuppressWarnings("unchecked") private boolean
	 * checkDetailsFilledOrNotPortal(long visitId) { boolean flag = false;
	 * List<MasMedicalExamReport> reportList = new
	 * ArrayList<MasMedicalExamReport>(); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); try { reportList =
	 * session.createCriteria(MasMedicalExamReport.class)
	 * .add(Restrictions.eq("visit.visitId", visitId))
	 * .add(Restrictions.isNotNull("status")).list(); if(reportList.size()>0) {
	 * flag= true; }else { flag=false; } }catch (Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.getMessage();
	 * e.printStackTrace(); } return flag;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @SuppressWarnings("unchecked") private Map<String, Object>
	 * getTokenNumberForVisit(Session session,long appSessionId, long
	 * appointmentTypeId, long hospitalId, Date visitStartDate, Date visitEndDate,
	 * long departmentId, String dayName) { Map<String, Object>map = new
	 * HashMap<String, Object>(); List<Long>existingTokenNoList = new
	 * ArrayList<Long>(); List<AppSetup>setupList = new ArrayList<AppSetup>(); long
	 * tokenNo = 0; String tokenMsg=""; existingTokenNoList =
	 * session.createCriteria(Visit.class) .createAlias("masAppointmentSession",
	 * "session").add(Restrictions.eq("session.id", appSessionId))
	 * .createAlias("masAppointmentType",
	 * "appType").add(Restrictions.eq("appType.appointmentTypeId",
	 * appointmentTypeId)) .createAlias("masHospital", "hospital")
	 * .add(Restrictions.eq("hospital.hospitalId", hospitalId))
	 * .add(Restrictions.ge("visitDate", visitStartDate))
	 * .add(Restrictions.lt("visitDate", visitEndDate))
	 * .setProjection(Projections.property("tokenNo")) .list();
	 * 
	 * setupList=session.createCriteria(AppSetup.class)
	 * .add(Restrictions.eq("masHospital.hospitalId", hospitalId))
	 * .add(Restrictions.eq("masDepartment.departmentId", departmentId))
	 * .add(Restrictions.eq("days", dayName))
	 * .add(Restrictions.eq("masAppointmentSession.id", appSessionId)) .list();
	 * 
	 * if (setupList != null && setupList.size() > 0) {
	 * if(setupList.get(0).getTotalToken()!=null) { long totalToken =
	 * setupList.get(0).getTotalToken().longValue(); List<Long> totalTokenValue =new
	 * ArrayList<Long>(); if (!existingTokenNoList.isEmpty() && existingTokenNoList
	 * != null ) { for(long count=1;count<=totalToken;count++) {
	 * totalTokenValue.add(count); }
	 * 
	 * totalTokenValue.removeAll(existingTokenNoList);
	 * Collections.sort(totalTokenValue); tokenNo = totalTokenValue.get(0);
	 * 
	 * } else { // tokenNoList is null tokenNo = 1; } if(tokenNo >
	 * (setupList.get(0).getTotalToken().intValue())) { tokenNo = 0;
	 * tokenMsg="Token is Full for " + "("+ dayName+")"; map.put("tokenMsg",tokenMsg
	 * ); map.put("appointmentTypeId",appointmentTypeId ); } else{ tokenMsg =
	 * String.valueOf(tokenNo); map.put("appointmentTypeId",appointmentTypeId );
	 * map.put("tokenMsg",tokenMsg ); } }else{
	 * 
	 * tokenMsg ="No Appointment Setup (click here)"; map.put("APPTYPE","SETUP");
	 * map.put("tokenMsg",tokenMsg ); map.put("appointmentTypeId",appointmentTypeId
	 * ); } } else { tokenMsg ="No Appointment Setup (click here)";
	 * map.put("APPTYPE","SETUP"); map.put("tokenMsg",tokenMsg );
	 * map.put("appointmentTypeId",appointmentTypeId ); }
	 * 
	 * return map;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * private String startTokenTimeForMobile(String startTime, float
	 * timePerTokenInMinutes, long tokenInterval,long startToken,long
	 * portalToken,long totalToken) {
	 * 
	 * long tokenValue = startToken; long portalTokenCount = 1; //changes value from
	 * zero to one
	 * 
	 * String tokenStartTime = startTime; String tokenEndTime="";
	 * 
	 * while (portalTokenCount <= portalToken && tokenValue <= totalToken) {
	 * portalTokenCount = portalTokenCount + 1; tokenEndTime =
	 * HMSUtil.addingMinutes(tokenStartTime, (int)timePerTokenInMinutes);
	 * tokenStartTime = HMSUtil.addingMinutes(tokenEndTime,
	 * (int)(timePerTokenInMinutes*tokenInterval)); } return tokenStartTime;
	 * 
	 * 
	 * 
	 * }
	 * 
	 * private long stratTokenValueForMobile(long startToken,long portalToken,long
	 * totalToken,long tokenInterval) { long tokenValue = startToken; long
	 * portalTokenCount = 1; while (portalTokenCount <= portalToken && tokenValue <=
	 * totalToken) { portalTokenCount = portalTokenCount + 1; tokenValue =
	 * tokenValue + tokenInterval; } return tokenValue; }
	 * 
	 * 
	 * // Ending the code for web+portal+online
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasRegistrationType> getRegistrationTypeList() {
	 * List<MasRegistrationType> masRegistrationTypeList = new
	 * ArrayList<MasRegistrationType>();
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * masRegistrationTypeList = session.createCriteria(MasRegistrationType.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .addOrder(Order.desc("registrationTypeName")).list(); }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * masRegistrationTypeList; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasAdministrativeSex> getGenderList() {
	 * List<MasAdministrativeSex> genderList = new
	 * ArrayList<MasAdministrativeSex>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); genderList =
	 * session.createCriteria(MasAdministrativeSex.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .addOrder(Order.asc("administrativeSexName")).list(); }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * genderList; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasIdentificationType> getIdentificationList() {
	 * List<MasIdentificationType> identificationTypeList = new
	 * ArrayList<MasIdentificationType>(); try { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); identificationTypeList =
	 * session.createCriteria(MasIdentificationType.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .addOrder(Order.asc("identificationName")).list(); }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * identificationTypeList; }
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasServiceType> getServiceTypeList() {
	 * List<MasServiceType> serviceTypeList = new ArrayList<MasServiceType>(); try {
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * serviceTypeList = session.createCriteria(MasServiceType.class)
	 * .add(Restrictions.eq("status","y").ignoreCase())
	 * .addOrder(Order.asc("serviceTypeName")).list(); }catch(Exception e) {
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); }
	 * 
	 * return serviceTypeList; }
	 * 
	 * 
	 * 
	 * 
	 * @Override public Map<String,Object> submitPatientDetails(JSONObject jObject)
	 * { long newVisitId=0; long patientId =0;
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Transaction tx = session.beginTransaction(); Map<String,Object> map = new
	 * HashMap<String, Object>();
	 * 
	 * Patient patient = null; Visit visit= null;
	 * 
	 * try {
	 * 
	 * List<HashMap<String, Object>> visitList = new ArrayList<HashMap<String,
	 * Object>>();
	 * 
	 * JSONArray appointmentType= jObject.getJSONArray("appointmentTypeId");
	 * JSONArray tokenIds= jObject.getJSONArray("tokenIds"); long departmentId=
	 * Long.parseLong(jObject.getString("departmentId")); long priorityId =
	 * Long.parseLong(jObject.getString("priorityId"));
	 * 
	 * JSONObject jsonObj =jObject.getJSONObject("patientDetailsForm"); long
	 * hospitalId =jsonObj.getLong("hospitalId");
	 * 
	 * String codeOpd = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_OPD"); String codeME =
	 * HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_ME"); String
	 * codeMB = HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_MB");
	 * 
	 * 
	 * long appointmentTypeIdOPD= getAppointmentTypeIdFromCode(codeOpd); long
	 * appointmentTypeME=getAppointmentTypeIdFromCode(codeME); long
	 * appointmentTypeMB=getAppointmentTypeIdFromCode(codeMB);
	 * 
	 * 
	 * 
	 * 
	 * Date visitDate= null; Date currentDate= new Date();
	 * 
	 * 
	 * String uhidNO=jsonObj.getString("uhidNo");
	 * 
	 * 
	 * if(!jsonObj.getString("visitDate").isEmpty() &&
	 * jsonObj.getString("visitDate")!=null) { String dateString =
	 * jsonObj.getString("visitDate"); visitDate =
	 * HMSUtil.convertStringDateToUtilDate(dateString, "dd-MM-yyyy HH:mm:ss");
	 * 
	 * }else { visitDate = currentDate; }
	 * 
	 * if(!uhidNO.isEmpty()) { String visitFlag=""; long doctorId=0; patientId =
	 * getPatientFromUhidNo(uhidNO); patient= getPatient(patientId);
	 * if(patient!=null) { if(!jsonObj.getString("visitFlag").isEmpty() &&
	 * !jsonObj.getString("visitFlag").equalsIgnoreCase("P")) {
	 * updatePatientDetails(patient,jsonObj,session); } }
	 * 
	 * for (int i = 0; i < appointmentType.length(); i++) { HashMap<String, Object>
	 * visitMap = new HashMap<String, Object>(); long existingVisitId=0; long
	 * masMedExamId =0; if (!jsonObj.getString("visitFlag").isEmpty() &&
	 * !jsonObj.getString("visitFlag").equalsIgnoreCase("P") &&
	 * Long.parseLong(appointmentType.get(i).toString()) != appointmentTypeIdOPD) {
	 * 
	 * if (Long.parseLong(appointmentType.get(i).toString()) == appointmentTypeME) {
	 * existingVisitId = jsonObj.getLong("existingVisitIdME");
	 * masMedExamId=Long.parseLong(jsonObj.getString("examTypeId")); } if
	 * (Long.parseLong(appointmentType.get(i).toString()) == appointmentTypeMB) {
	 * existingVisitId = jsonObj.getLong("existingVisitIdMB");
	 * masMedExamId=Long.parseLong(jsonObj.getString("boardTypeId")); } }
	 * 
	 * if(!jsonObj.getString("visitFlag").isEmpty() &&
	 * jsonObj.getString("visitFlag").equalsIgnoreCase("P") &&
	 * Long.parseLong(appointmentType.get(i).toString()) != appointmentTypeIdOPD) {
	 * 
	 * existingVisitId = jsonObj.getLong("visitId");
	 * masMedExamId=Long.parseLong(jsonObj.getString("medExBrdId")); }
	 * 
	 * boolean existFlag = checkExistingAppointmentForPatient(visitDate, hospitalId,
	 * departmentId, patientId,
	 * Long.parseLong(appointmentType.get(i).toString()),masMedExamId,
	 * existingVisitId);
	 * 
	 * if (!existFlag) { if (existingVisitId != 0) { newVisitId =
	 * updateVisitForTokenNo(existingVisitId,
	 * Long.parseLong(tokenIds.get(i).toString()),jsonObj.getString("visitFlag"),
	 * visitDate);
	 * 
	 * visitMap.put("appType", Long.parseLong(appointmentType.get(i).toString()));
	 * visitMap.put("visitId", newVisitId); visitMap.put("tokenNo",
	 * Long.parseLong(tokenIds.get(i).toString()));
	 * visitMap.put("msg","Appointment booked successfully.");
	 * visitList.add(visitMap);
	 * 
	 * } else { visit = new Visit(); if (!tokenIds.get(i).toString().isEmpty() &&
	 * !tokenIds.get(i).toString().equalsIgnoreCase("0")) {
	 * visit.setTokenNo(Long.parseLong(tokenIds.get(i).toString())); }
	 * visit.setVisitDate(new Timestamp(visitDate.getTime()));
	 * visit.setLastChgDate(new Timestamp(currentDate.getTime()));
	 * 
	 * visit.setPriority(priorityId); visit.setVisitStatus("w");
	 * 
	 * if (!jsonObj.getString("visitFlag").isEmpty()) { visitFlag =
	 * jsonObj.getString("visitFlag"); visit.setVisitFlag(visitFlag); }
	 * 
	 * 
	 * if(jsonObj.has("doctorMappingId") &&
	 * !jsonObj.getString("doctorMappingId").isEmpty() &&
	 * Long.parseLong(jsonObj.getString("doctorMappingId"))!=0) { doctorId =
	 * Long.parseLong(jsonObj.getString("doctorMappingId"));
	 * visit.setEffDoctorId(doctorId); visit.setIntDoctorId(doctorId); }
	 * 
	 * if (Long.parseLong(appointmentType.get(i).toString()) == appointmentTypeME) {
	 * if (!jsonObj.getString("examTypeId").isEmpty()) {
	 * 
	 * long examTypeId = Long.parseLong(jsonObj.getString("examTypeId"));
	 * visit.setExamId(examTypeId); visit.setExamStatus("I");
	 * visit.setMeAge(jsonObj.getString("ageListForME"));
	 * 
	 * if(jObject.getString("ridcId")!=null && !
	 * jObject.getString("ridcId").isEmpty()) {
	 * visit.setRidcId(Long.parseLong(jObject.getString("ridcId"))); }
	 * 
	 * } }
	 * 
	 * if (Long.parseLong(appointmentType.get(i).toString()) == appointmentTypeMB) {
	 * if (!jsonObj.getString("boardTypeId").isEmpty()) { long examTypeId =
	 * Long.parseLong(jsonObj.getString("boardTypeId"));
	 * visit.setExamId(examTypeId); visit.setExamStatus("I"); } }
	 * 
	 * visit.setAppointmentTypeId(Long.parseLong(appointmentType.get(i).toString()))
	 * ; visit.setHospitalId(hospitalId); visit.setDepartmentId(departmentId);
	 * visit.setPatientId(patientId);
	 * 
	 * long appointmentSessionId = getAppointmentSessionId(hospitalId, departmentId,
	 * Long.parseLong(appointmentType.get(i).toString()));
	 * 
	 * visit.setSessionId(appointmentSessionId); session.save(visit); newVisitId =
	 * visit.getVisitId();
	 * 
	 * visitMap.put("appType", Long.parseLong(appointmentType.get(i).toString()));
	 * visitMap.put("visitId", newVisitId);
	 * visitMap.put("tokenNo",visit.getTokenNo()!=null?visit.getTokenNo():"");
	 * visitMap.put("msg", "Appointment booked successfully.");
	 * visitList.add(visitMap); }
	 * 
	 * } else { // In this case existFlag is true. Appointment already booked.
	 * newVisitId = 0; visitMap.put("tokenNo", ""); visitMap.put("visitId",
	 * newVisitId); visitMap.put("appType",
	 * Long.parseLong(appointmentType.get(i).toString()));
	 * if(Long.parseLong(appointmentType.get(i).toString())!=appointmentTypeIdOPD) {
	 * 
	 * 
	 * HashMap<String, String> visitStatusMap=checkVisitStatusForMeAndMb(visitDate,
	 * hospitalId, departmentId, patientId,masMedExamId,existingVisitId);
	 * 
	 * visitMap.put("msg", visitStatusMap.get("message"));
	 * 
	 * }else { visitMap.put("msg", "Appointment is already booked for OPD"); }
	 * visitList.add(visitMap); } } }else { patient = new Patient(); // Start
	 * employee info patientId =
	 * createPatientForFirstTime(currentDate,patient,jsonObj,session);
	 * 
	 * for(int i=0;i<appointmentType.length();i++) { HashMap<String, Object>
	 * visitMap = new HashMap<String, Object>(); String visitFlag=""; long
	 * doctorId=0; visit = new Visit(); if(!(tokenIds.get(i).toString().isEmpty())
	 * && !tokenIds.get(i).toString().equalsIgnoreCase("0")) {
	 * visit.setTokenNo(Long.parseLong(tokenIds.get(i).toString())); }
	 * 
	 * visit.setVisitDate(new Timestamp(visitDate.getTime()));
	 * visit.setLastChgDate(new Timestamp(visitDate.getTime()));
	 * visit.setPriority(priorityId); visit.setVisitStatus("w");
	 * 
	 * if(!jsonObj.getString("visitFlag").isEmpty()) { visitFlag =
	 * jsonObj.getString("visitFlag"); visit.setVisitFlag(visitFlag); }
	 * 
	 * if(!jsonObj.getString("doctorMappingId").isEmpty() &&
	 * Long.parseLong(jsonObj.getString("doctorMappingId"))!=0) { doctorId =
	 * Long.parseLong(jsonObj.getString("doctorMappingId"));
	 * visit.setEffDoctorId(doctorId); visit.setIntDoctorId(doctorId); }
	 * 
	 * 
	 * if(Long.parseLong(appointmentType.get(i).toString())==appointmentTypeME) {
	 * if(!jsonObj.getString("examTypeId").isEmpty()) {
	 * 
	 * long examTypeId = Long.parseLong(jsonObj.getString("examTypeId"));
	 * visit.setExamId(examTypeId); visit.setExamStatus("I");
	 * visit.setMeAge(jsonObj.getString("ageListForME"));
	 * 
	 * if(jObject.getString("ridcId")!=null && !
	 * jObject.getString("ridcId").isEmpty()) {
	 * visit.setRidcId(Long.parseLong(jObject.getString("ridcId"))); }
	 * 
	 * } }
	 * 
	 * if(Long.parseLong(appointmentType.get(i).toString())==appointmentTypeMB) {
	 * if(!jsonObj.getString("boardTypeId").isEmpty()) { long examTypeId =
	 * Long.parseLong(jsonObj.getString("boardTypeId"));
	 * visit.setExamId(examTypeId); visit.setExamStatus("I"); } }
	 * 
	 * 
	 * visit.setAppointmentTypeId(Long.parseLong(appointmentType.get(i).toString()))
	 * ; visit.setHospitalId(hospitalId); visit.setDepartmentId(departmentId);
	 * visit.setPatientId(patientId);
	 * 
	 * long
	 * appointmentSessionId=getAppointmentSessionId(hospitalId,departmentId,Long.
	 * parseLong(appointmentType.get(i).toString())) ;
	 * 
	 * visit.setSessionId(appointmentSessionId); session.save(visit);
	 * newVisitId=visit.getVisitId();
	 * 
	 * visitMap.put("appType", Long.parseLong(appointmentType.get(i).toString()));
	 * visitMap.put("visitId", newVisitId); visitMap.put("tokenNo",
	 * visit.getTokenNo()!=null?visit.getTokenNo():"");
	 * visitMap.put("msg","Appointment booked successfully.");
	 * visitList.add(visitMap); } } tx.commit(); map.put("visitList",visitList);
	 * }catch (Exception e) { newVisitId=-1; HashMap<String,Object> visitMap = new
	 * HashMap<String, Object>(); List<HashMap<String, Object>> visitList = new
	 * ArrayList<HashMap<String, Object>>();
	 * visitMap.put("msg","Some error occured.");
	 * visitMap.put("visitId",newVisitId); visitMap.put("appType","");
	 * visitMap.put("tokenNo",""); visitList.add(visitMap);
	 * map.put("visitList",visitList); e.getMessage(); e.printStackTrace(); } return
	 * map; }
	 * 
	 * 
	 * 
	 * @SuppressWarnings({ "unchecked", "static-access" }) private HashMap<String,
	 * String> checkVisitStatusForMeAndMb(Date visitDate, long hospitalId, long
	 * departmentId, long patientId,long masMedExamId, long existingVisitId) {
	 * 
	 * List<Visit> visitList= new ArrayList<Visit>(); List<String>
	 * masMedExamcodeList= new ArrayList<String>(); HashMap<String, String> map =
	 * new HashMap<String, String>(); String message="";
	 * 
	 * Date visitEndDate=null;
	 * 
	 * String date= HMSUtil.convertDateToStringFormat(visitDate, "dd-MM-yyyy"); Date
	 * nextDatefromVisit = HMSUtil.convertStringDateToUtilDate(date, "dd-MM-yyyy");
	 * visitEndDate =HMSUtil.getNextDate(nextDatefromVisit) ;
	 * 
	 * 
	 * String codeME = HMSUtil.getProperties("adt.properties",
	 * "APPOINTMENT_TYPE_CODE_ME"); String codeMB =
	 * HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_MB");
	 * 
	 * 
	 * long appointmentTypeME=getAppointmentTypeIdFromCode(codeME); long
	 * appointmentTypeMB=getAppointmentTypeIdFromCode(codeMB);
	 * 
	 * 
	 * Calendar cal = Calendar.getInstance(); int currentYear =
	 * cal.getInstance().get(Calendar.YEAR); cal.set(Calendar.YEAR, currentYear);
	 * cal.set(Calendar.DAY_OF_YEAR, 1); cal.set(Calendar.HOUR, 0);
	 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
	 * cal.set(Calendar.HOUR_OF_DAY,0); // for converting time into 00:00:00
	 * 
	 * Date yearStartDate = cal.getTime(); Date currentDate = visitEndDate;
	 * 
	 * try { Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * 
	 * 
	 * String meForm18=HMSUtil.getProperties("resource.properties", "meForm18");
	 * String meForm3A=HMSUtil.getProperties("resource.properties", "meForm3A");
	 * String meForm3B=HMSUtil.getProperties("resource.properties", "meForm3B");
	 * String meForm3BS=HMSUtil.getProperties("resource.properties","meForm3BS");
	 * 
	 * String mbForm15=HMSUtil.getProperties("resource.properties", "mbForm15");
	 * String mbForm16=HMSUtil.getProperties("resource.properties", "mbForm16");
	 * 
	 * String examTypeCode = getExamCodeFromId(masMedExamId);
	 * 
	 * // Case of ME and MB visitList = session.createCriteria(Visit.class)
	 * .createAlias("masAppointmentType", "appType")
	 * .add(Restrictions.or(Restrictions.eq("appType.appointmentTypeId",
	 * appointmentTypeME), Restrictions.eq("appType.appointmentTypeId",
	 * appointmentTypeMB))) .add(Restrictions.ne("visitFlag", "E").ignoreCase())
	 * .add(Restrictions.eq("patient.patientId",patientId))
	 * .add(Restrictions.ge("visitDate", yearStartDate))
	 * .add(Restrictions.lt("visitDate", currentDate)) .list();
	 * 
	 * String examCodeDb=""; if(visitList.size()>0) { for (Visit visit : visitList)
	 * { examCodeDb = visit.getMasMedExam().getMedicalExamCode();
	 * masMedExamcodeList.add(examCodeDb); } if
	 * (examTypeCode.equalsIgnoreCase(mbForm15)) {
	 * if(masMedExamcodeList.contains(mbForm16)) { message =
	 * "Appointment is already booked for " + mbForm16; }else
	 * if(masMedExamcodeList.contains(meForm18)){ message =
	 * "Appointment is already booked for " + meForm18; } } if
	 * (examTypeCode.equalsIgnoreCase(mbForm16) ||
	 * examTypeCode.equalsIgnoreCase(meForm18)) { if
	 * (masMedExamcodeList.contains(mbForm16) ||
	 * masMedExamcodeList.contains(meForm18) ||
	 * masMedExamcodeList.contains(examTypeCode)) { if(existingVisitId!=0 &&
	 * examTypeCode.equalsIgnoreCase(mbForm16)) { message =
	 * "Appointment can not booked."; }else if(existingVisitId!=0 &&
	 * examTypeCode.equalsIgnoreCase(meForm18)) { message =
	 * "Appointment can not booked."; }else { message =
	 * "Appointment is already booked for "+examTypeCode; } } else {
	 * if(masMedExamcodeList.contains(mbForm15) &&
	 * examTypeCode.equalsIgnoreCase(meForm18)) { message =
	 * "Appointment is already booked for "+ mbForm15; }else { message =
	 * "Appointment can not booked."; } } }
	 * 
	 * if (examTypeCode.equalsIgnoreCase(meForm3A) ||
	 * examTypeCode.equalsIgnoreCase(meForm3B) ||
	 * examTypeCode.equalsIgnoreCase(meForm3BS)) { if
	 * ((masMedExamcodeList.contains(meForm3A) ||
	 * masMedExamcodeList.contains(meForm3B) ||
	 * masMedExamcodeList.contains(meForm3BS)) ||
	 * masMedExamcodeList.contains(examTypeCode)) { if(existingVisitId!=0) { message
	 * = "Appointment can not booked."; }else {
	 * if(masMedExamcodeList.contains(meForm3A) ) { message =
	 * "Appointment is already booked for "+meForm3A; }else
	 * if(masMedExamcodeList.contains(meForm3B) ) { message =
	 * "Appointment is already booked for "+meForm3B; }else
	 * if(masMedExamcodeList.contains(meForm3BS) ) { message =
	 * "Appointment is already booked for "+meForm3BS; }
	 * 
	 * } } else { message = "Appointment can not booked."; } }
	 * 
	 * }else { message = "Appointment can not booked."; }
	 * 
	 * map.put("message",message); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return map; }
	 * 
	 * 
	 * 
	 * private long createPatientForFirstTime(Date currentDate,Patient patient,
	 * JSONObject jsonObj, Session session) {
	 * 
	 * long patientId=0; long patientRelationId=0; long registrationTypeId=0; String
	 * serviceNo=""; String uhidNO="";
	 * 
	 * String serviceTypeCodeIcg=HMSUtil.getProperties("adt.properties",
	 * "SERVICE_TYPE_CODE_ICG"); long serviceTypeIdIcg =
	 * getServiceTypeIdFromCode(serviceTypeCodeIcg);
	 * 
	 * 
	 * if(!jsonObj.getString("empService").isEmpty() &&
	 * jsonObj.getString("empService")!=null) { serviceNo =
	 * jsonObj.getString("empService"); patient.setServiceNo(serviceNo); }
	 * 
	 * if(!jsonObj.getString("empName").isEmpty() &&
	 * jsonObj.getString("empName")!=null) { String empName =
	 * jsonObj.getString("empName").trim(); patient.setEmployeeName(empName); }
	 * 
	 * if(jsonObj.getLong("empId")!=0 && jsonObj.get("empId")!=null) { long empId =
	 * jsonObj.getLong("empId"); //patient.setEmployeeId(empId); }
	 * if(jsonObj.getLong("rankId")!=0 && jsonObj.get("rankId")!=null) { long
	 * rankId=jsonObj.getLong("rankId"); patient.setRankId(rankId); }
	 * 
	 * if(jsonObj.getLong("tradeId")!=0 && jsonObj.get("tradeId")!=null) { long
	 * tradeId= jsonObj.getLong("tradeId"); // trade/branch
	 * patient.setTradeId(tradeId); }
	 * 
	 * 
	 * if(jsonObj.getLong("unitId")!=0 && jsonObj.get("unitId")!=null) { long
	 * unitId=jsonObj.getLong("unitId"); patient.setUnitId(unitId); }
	 * 
	 * 
	 * if(jsonObj.getLong("regionId")!=0 && jsonObj.get("regionId")!=null) { long
	 * regionId=jsonObj.getLong("regionId"); patient.setCommandId(regionId); }
	 * 
	 * long empCategoryId=0; if(jsonObj.getLong("empCategoryId")!=0 &&
	 * jsonObj.get("empCategoryId")!=null) {
	 * empCategoryId=jsonObj.getLong("empCategoryId");
	 * patient.setEmployeeCategoryId(empCategoryId); }
	 * 
	 * if(jsonObj.getLong("maritalstarusId")!=0 &&
	 * jsonObj.get("maritalstarusId")!=null) { long maritalstarusId =
	 * jsonObj.getLong("maritalstarusId");
	 * patient.setMaritalStatusId(maritalstarusId); }
	 * 
	 * 
	 * if(jsonObj.getLong("religionId")!=0 && jsonObj.get("religionId")!=null) {
	 * long religionId = jsonObj.getLong("religionId");
	 * patient.setReligionId(religionId); }
	 * 
	 * if(jsonObj.getLong("recordofficeId")!=0 &&
	 * jsonObj.get("recordofficeId")!=null) { long
	 * recordofficeId=jsonObj.getLong("recordofficeId");
	 * patient.setRecordOfficeAddressId(recordofficeId); }
	 * 
	 * if(!jsonObj.getString("empServiceJoinDate").isEmpty() &&
	 * jsonObj.get("empServiceJoinDate")!=null) { String
	 * empServiceJoinDate=jsonObj.getString("empServiceJoinDate"); try {
	 * patient.setServiceJoinDate(HMSUtil.convertStringDateToUtilDate(
	 * empServiceJoinDate, "dd/MM/yyyy")); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * // End employee info
	 * 
	 *//****************************************************************//*
																			 * 
																			 * // Start patient info
																			 * 
																			 * if(!jsonObj.getString("patientname").
																			 * isEmpty()) { String patientName =
																			 * jsonObj.getString("patientname").trim();
																			 * patient.setPatientName(patientName); }
																			 * 
																			 * if(!jsonObj.getString("patientDOB").
																			 * isEmpty()) { String patientDOB =
																			 * jsonObj.getString("patientDOB"); try {
																			 * patient.setDateOfBirth(HMSUtil.
																			 * convertStringDateToUtilDate(patientDOB,
																			 * "dd/MM/yyyy")); } catch (Exception e) {
																			 * // TODO Auto-generated catch block
																			 * e.printStackTrace(); } }
																			 * 
																			 * 
																			 * if(jsonObj.getLong("genderId")!=0) { long
																			 * patientGenderId=jsonObj.getLong(
																			 * "genderId");
																			 * patient.setAdministrativeSexId(
																			 * patientGenderId); }
																			 * if(jsonObj.getLong("patientRelationId")!=
																			 * 0) { patientRelationId=jsonObj.getLong(
																			 * "patientRelationId");
																			 * patient.setRelationId(patientRelationId);
																			 * }
																			 * 
																			 * if(!jsonObj.getString(
																			 * "patientMoblienumber").isEmpty()) {
																			 * String
																			 * patientMoblienumber=jsonObj.getString(
																			 * "patientMoblienumber");
																			 * patient.setMobileNumber(
																			 * patientMoblienumber); }
																			 * 
																			 * if(!jsonObj.getString("patientEmail").
																			 * isEmpty()) { String patientEmail =
																			 * jsonObj.getString("patientEmail");
																			 * patient.setEmailId(patientEmail); }
																			 * 
																			 * if(!jsonObj.getString("visitFlag").
																			 * isEmpty() &&
																			 * !jsonObj.getString("visitFlag").
																			 * equalsIgnoreCase("P")) {
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine1").isEmpty()) {
																			 * String patientAddressLine1 =
																			 * jsonObj.getString("patientAddressLine1");
																			 * patient.setAddressLine1(
																			 * patientAddressLine1); }
																			 * 
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine2").isEmpty()) {
																			 * String patientAddressLine2 =
																			 * jsonObj.getString("patientAddressLine2");
																			 * patient.setAddressLine2(
																			 * patientAddressLine2); }
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine3").isEmpty()) {
																			 * String patientAddressLine3 =
																			 * jsonObj.getString("patientAddressLine3");
																			 * patient.setAddressLine3(
																			 * patientAddressLine3); }
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine4").isEmpty()) {
																			 * String patientAddressLine4 =
																			 * jsonObj.getString("patientAddressLine4");
																			 * patient.setAddressLine4(
																			 * patientAddressLine4); }
																			 * 
																			 * if(!jsonObj.getString("patientPincode").
																			 * isEmpty() &&
																			 * jsonObj.getLong("patientPincode")!=0) {
																			 * long patientPincode=jsonObj.getLong(
																			 * "patientPincode");
																			 * patient.setPincode(patientPincode); }
																			 * 
																			 * 
																			 * if(jsonObj.getLong("patientDistrictId")!=
																			 * 0) { long
																			 * patientDistrictId=jsonObj.getLong(
																			 * "patientDistrictId");
																			 * patient.setDistrictId(patientDistrictId);
																			 * }
																			 * if(jsonObj.getLong("patientStateId")!=0)
																			 * { long patientStateId=jsonObj.getLong(
																			 * "patientStateId");
																			 * patient.setStateId(patientStateId); }
																			 * if(jsonObj.getLong("patientCountryId")!=
																			 * 0) { long
																			 * patientCountryId=jsonObj.getLong(
																			 * "patientCountryId");
																			 * patient.setCountryId(patientCountryId); }
																			 * }
																			 * 
																			 * 
																			 * if(jsonObj.getLong("registrationTypeId")!
																			 * =0) { registrationTypeId=jsonObj.getLong(
																			 * "registrationTypeId");
																			 * patient.setRegistrationTypeId(
																			 * registrationTypeId); }
																			 * 
																			 * 
																			 * // Adding serviceTypeId for ICG employee
																			 * 
																			 * patient.setServiceStatusId(
																			 * serviceTypeIdIcg);
																			 * 
																			 * uhidNO =
																			 * getHinNo(serviceNo,patientRelationId,
																			 * registrationTypeId);
																			 * //patient.setUhidNo(uhidNO);
																			 * 
																			 * if(!jsonObj.getString("visitFlag").
																			 * isEmpty() &&
																			 * !jsonObj.getString("visitFlag").
																			 * equalsIgnoreCase("P")) {
																			 * if(!jsonObj.getString("nok1Firstname").
																			 * isEmpty()) { String nok1FirstName=
																			 * jsonObj.getString("nok1Firstname");
																			 * patient.setNok1Name(nok1FirstName); }
																			 * 
																			 * if(jsonObj.getLong("nok1RelationId")!=0)
																			 * { long nok1RelationId=jsonObj.getLong(
																			 * "nok1RelationId");
																			 * patient.setNok1RelationId((nok1RelationId
																			 * )); }
																			 * if(!jsonObj.getString("nok1Moblienumber")
																			 * .isEmpty()) { String
																			 * nok1Moblienumber=jsonObj.getString(
																			 * "nok1Moblienumber");
																			 * patient.setNok1MobileNo(nok1Moblienumber)
																			 * ; }
																			 * 
																			 * if(!jsonObj.getString("nok1Email").
																			 * isEmpty()) { String nok1Email
																			 * =jsonObj.getString("nok1Email");
																			 * patient.setNok1EmailId(nok1Email); }
																			 * 
																			 * if(!jsonObj.getString("nok1Contactnumber"
																			 * ).isEmpty()) { String
																			 * nok1Contactnumber=jsonObj.getString(
																			 * "nok1Contactnumber");
																			 * patient.setNok1ContactNo(
																			 * nok1Contactnumber); }
																			 * 
																			 * if(!jsonObj.getString("nok1Policestation"
																			 * ).isEmpty()) { String
																			 * nok1PoliceStation=jsonObj.getString(
																			 * "nok1Policestation");
																			 * patient.setNok1PoliceStation(
																			 * nok1PoliceStation); }
																			 * 
																			 * 
																			 * 
																			 * if(!jsonObj.getString("nok1AddressLine1")
																			 * .isEmpty()) { String nok1AddressLine1 =
																			 * jsonObj.getString("nok1AddressLine1");
																			 * patient.setNok1AddressLine1(
																			 * nok1AddressLine1); }
																			 * 
																			 * if(!jsonObj.getString("nok1AddressLine2")
																			 * .isEmpty()) { String nok1AddressLine2 =
																			 * jsonObj.getString("nok1AddressLine2");
																			 * patient.setNok1AddressLine2(
																			 * nok1AddressLine2); }
																			 * if(!jsonObj.getString("nok1AddressLine3")
																			 * .isEmpty()) { String nok1AddressLine3 =
																			 * jsonObj.getString("nok1AddressLine3");
																			 * patient.setNok1AddressLine3(
																			 * nok1AddressLine3); }
																			 * if(!jsonObj.getString("nok1AddressLine4")
																			 * .isEmpty()) { String nok1AddressLine4 =
																			 * jsonObj.getString("nok1AddressLine4");
																			 * patient.setNok1AddressLine4(
																			 * nok1AddressLine4); }
																			 * 
																			 * if(!jsonObj.getString("nok1pincode").
																			 * isEmpty() &&
																			 * jsonObj.getLong("nok1pincode")!=0) { long
																			 * nok1pincode=jsonObj.getLong("nok1pincode"
																			 * ); patient.setNok1PinCode(nok1pincode); }
																			 * 
																			 * 
																			 * if(jsonObj.getLong("nok1DistrictId")!=0)
																			 * { long nok1DistrictId=jsonObj.getLong(
																			 * "nok1DistrictId");
																			 * patient.setNok1DistrictId(nok1DistrictId)
																			 * ; } if(jsonObj.getLong("nok1StateId")!=0)
																			 * { long
																			 * nok1StateId=jsonObj.getLong("nok1StateId"
																			 * ); patient.setNok1StateId(nok1StateId); }
																			 * if(jsonObj.getLong("nok1Country")!=0) {
																			 * long
																			 * nok1Country=jsonObj.getLong("nok1Country"
																			 * ); patient.setNok1CountryId(nok1Country);
																			 * }
																			 * 
																			 * if(!jsonObj.getString("nok2Firstname").
																			 * isEmpty()) { String
																			 * nok2FirstName=jsonObj.getString(
																			 * "nok2Firstname");
																			 * patient.setNok2Name(nok2FirstName); }
																			 * if(!jsonObj.getString("nok2Relation").
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("nok2Relation"
																			 * ).toString())!=0) { long
																			 * nok2RelationId=jsonObj.getLong(
																			 * "nok2Relation");
																			 * patient.setNok2RelationId((nok2RelationId
																			 * )); }
																			 * if(!jsonObj.getString("nok2Moblienumber")
																			 * .isEmpty()) { String
																			 * nok2Moblienumber=jsonObj.getString(
																			 * "nok2Moblienumber");
																			 * patient.setNok2MobileNo(nok2Moblienumber)
																			 * ; }
																			 * 
																			 * if(!jsonObj.getString("nok2Email").
																			 * isEmpty()) { String
																			 * nok2Email=jsonObj.getString("nok2Email");
																			 * patient.setNok2EmailId(nok2Email); }
																			 * if(!jsonObj.getString("nok2Contactnumber"
																			 * ).isEmpty()) { String
																			 * nok2Contactnumber=jsonObj.getString(
																			 * "nok2Contactnumber");
																			 * patient.setNok2ContactNo(
																			 * nok2Contactnumber); }
																			 * 
																			 * if(!jsonObj.getString("nok2Policestation"
																			 * ).isEmpty()) { String
																			 * nok2PoliceStation=jsonObj.getString(
																			 * "nok2Policestation");
																			 * patient.setNok2PoliceStation(
																			 * nok2PoliceStation); }
																			 * 
																			 * 
																			 * // NOK2 Address start
																			 * 
																			 * if(!jsonObj.getString("nok2AddressLine1")
																			 * .isEmpty()) { String nok2AddressLine1 =
																			 * jsonObj.getString("nok2AddressLine1");
																			 * patient.setNok2AddressLine1(
																			 * nok2AddressLine1); }
																			 * 
																			 * if(!jsonObj.getString("nok2AddressLine2")
																			 * .isEmpty()) { String nok2AddressLine2 =
																			 * jsonObj.getString("nok2AddressLine2");
																			 * patient.setNok2AddressLine2(
																			 * nok2AddressLine2); }
																			 * if(!jsonObj.getString("nok2AddressLine3")
																			 * .isEmpty()) { String nok2AddressLine3 =
																			 * jsonObj.getString("nok2AddressLine3");
																			 * patient.setNok2AddressLine3(
																			 * nok2AddressLine3); }
																			 * if(!jsonObj.getString("nok2AddressLine4")
																			 * .isEmpty()) { String nok2AddressLine4 =
																			 * jsonObj.getString("nok2AddressLine4");
																			 * patient.setNok2AddressLine4(
																			 * nok2AddressLine4); }
																			 * 
																			 * if(!jsonObj.getString("nok2pincode").
																			 * isEmpty() &&
																			 * jsonObj.getLong("nok2pincode")!=0) { long
																			 * nok2pincode=jsonObj.getLong("nok2pincode"
																			 * ); patient.setNok2PinCode(nok2pincode); }
																			 * 
																			 * 
																			 * if(jsonObj.getLong("nok2DistrictId")!=0)
																			 * { long nok2DistrictId=jsonObj.getLong(
																			 * "nok2DistrictId");
																			 * patient.setNok2DistrictId(nok2DistrictId)
																			 * ; } if(jsonObj.getLong("nok2StateId")!=0)
																			 * { long
																			 * nok2StateId=jsonObj.getLong("nok2StateId"
																			 * ); patient.setNok2StateId(nok2StateId); }
																			 * if(jsonObj.getLong("nok2Country")!=0) {
																			 * long
																			 * nok2Country=jsonObj.getLong("nok2Country"
																			 * ); patient.setNok2CountryId(nok2Country);
																			 * }
																			 * 
																			 * }
																			 * 
																			 * 
																			 * if(jsonObj.getLong("bloodGroup")!=0) {
																			 * long bloodGroupId =
																			 * jsonObj.getLong("bloodGroup");
																			 * patient.setBloodGroupId(bloodGroupId); }
																			 * 
																			 * patient.setLastChgDate(new
																			 * Timestamp(currentDate.getTime()));
																			 * 
																			 * 
																			 * long existingPatientId =
																			 * getPatientFromUhidNo(uhidNO);
																			 * 
																			 * if(existingPatientId!=0) {
																			 * patientId=existingPatientId; }else {
																			 * session.save(patient); patientId =
																			 * patient.getPatientId(); }
																			 * 
																			 * session.save(patient); patientId =
																			 * patient.getPatientId();
																			 * 
																			 * return patientId; }
																			 * 
																			 * 
																			 * 
																			 * private long
																			 * getServiceTypeIdFromCode(String
																			 * serviceTypeCodeIcg) { long
																			 * serviceTypeIdIcg=0; MasServiceType
																			 * masService = null; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try {
																			 * masService=(MasServiceType)
																			 * session.createCriteria(MasServiceType.
																			 * class)
																			 * .add(Restrictions.eq("serviceTypeCode",
																			 * serviceTypeCodeIcg)).uniqueResult();
																			 * 
																			 * if(masService!=null) { serviceTypeIdIcg =
																			 * masService.getServiceTypeId(); }
																			 * 
																			 * }catch (Exception e) { e.getMessage();
																			 * e.printStackTrace();
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return
																			 * serviceTypeIdIcg; }
																			 * 
																			 * 
																			 * 
																			 * private void updatePatientDetails(Patient
																			 * patient, JSONObject jsonObj, Session
																			 * session) {
																			 * 
																			 * if (jsonObj.has("rankId") &&
																			 * jsonObj.getLong("rankId") != 0) { long
																			 * rankId = jsonObj.getLong("rankId");
																			 * patient.setRankId(rankId); }
																			 * 
																			 * if (jsonObj.getLong("tradeId") != 0) {
																			 * long tradeId =
																			 * jsonObj.getLong("tradeId"); //
																			 * trade/branch patient.setTradeId(tradeId);
																			 * }
																			 * 
																			 * if (jsonObj.getLong("unitId") != 0) {
																			 * long unitId = jsonObj.getLong("unitId");
																			 * patient.setUnitId(unitId); }
																			 * 
																			 * if(jsonObj.getLong("regionId")!=0 &&
																			 * jsonObj.get("regionId")!=null) { long
																			 * regionId=jsonObj.getLong("regionId");
																			 * patient.setCommandId(regionId); }
																			 * 
																			 * if (jsonObj.has("maritalstarusId") &&
																			 * jsonObj.getLong("maritalstarusId") != 0
																			 * && jsonObj.get("maritalstarusId")!=null)
																			 * { long maritalstarusId =
																			 * jsonObj.getLong("maritalstarusId");
																			 * patient.setMaritalStatusId(
																			 * maritalstarusId); }
																			 * 
																			 * if (jsonObj.has("religionId") &&
																			 * jsonObj.getLong("religionId") != 0 &&
																			 * jsonObj.get("religionId") != null) { long
																			 * religionId =
																			 * jsonObj.getLong("religionId");
																			 * patient.setReligionId(religionId); } else
																			 * { Long religionId = null;
																			 * patient.setReligionId(religionId); }
																			 * 
																			 * if (jsonObj.getLong("recordofficeId") !=
																			 * 0) { long recordofficeId =
																			 * jsonObj.getLong("recordofficeId");
																			 * patient.setRecordOfficeAddressId(
																			 * recordofficeId); }
																			 * 
																			 * if (jsonObj.getLong("genderId") != 0) {
																			 * long patientGenderId =
																			 * jsonObj.getLong("genderId");
																			 * patient.setAdministrativeSexId(
																			 * patientGenderId); }
																			 * 
																			 * if(!jsonObj.getString("patientDOB").
																			 * isEmpty()) { String patientDOB =
																			 * jsonObj.getString("patientDOB"); try {
																			 * patient.setDateOfBirth(HMSUtil.
																			 * convertStringDateToUtilDate(patientDOB,
																			 * "dd/MM/yyyy")); } catch (Exception e) {
																			 * // TODO Auto-generated catch block
																			 * e.printStackTrace(); } }
																			 * 
																			 * if (jsonObj.getLong("patientRelationId")
																			 * != 0) { long patientRelationId =
																			 * jsonObj.getLong("patientRelationId");
																			 * patient.setRelationId(patientRelationId);
																			 * }
																			 * 
																			 * if
																			 * (!jsonObj.getString("patientMoblienumber"
																			 * ).isEmpty()) { String patientMoblienumber
																			 * =
																			 * jsonObj.getString("patientMoblienumber");
																			 * patient.setMobileNumber(
																			 * patientMoblienumber); }
																			 * 
																			 * if (!jsonObj.getString("patientEmail").
																			 * isEmpty()) { String patientEmail =
																			 * jsonObj.getString("patientEmail");
																			 * patient.setEmailId(patientEmail); }
																			 * 
																			 * if(jsonObj.getLong("bloodGroup")!=0) {
																			 * long bloodGroupId =
																			 * jsonObj.getLong("bloodGroup");
																			 * patient.setBloodGroupId(bloodGroupId); }
																			 * 
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine1").isEmpty()) {
																			 * String patientAddressLine1 =
																			 * jsonObj.getString("patientAddressLine1");
																			 * patient.setAddressLine1(
																			 * patientAddressLine1); }
																			 * 
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine2").isEmpty()) {
																			 * String patientAddressLine2 =
																			 * jsonObj.getString("patientAddressLine2");
																			 * patient.setAddressLine2(
																			 * patientAddressLine2); }
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine3").isEmpty()) {
																			 * String patientAddressLine3 =
																			 * jsonObj.getString("patientAddressLine3");
																			 * patient.setAddressLine3(
																			 * patientAddressLine3); }
																			 * if(!jsonObj.getString(
																			 * "patientAddressLine4").isEmpty()) {
																			 * String patientAddressLine4 =
																			 * jsonObj.getString("patientAddressLine4");
																			 * patient.setAddressLine4(
																			 * patientAddressLine4); }
																			 * 
																			 * if(!jsonObj.getString("patientPincode").
																			 * isEmpty() &&
																			 * jsonObj.getLong("patientPincode")!=0) {
																			 * long patientPincode=jsonObj.getLong(
																			 * "patientPincode");
																			 * patient.setPincode(patientPincode); }
																			 * 
																			 * 
																			 * if(jsonObj.getLong("patientDistrictId")!=
																			 * 0) { long
																			 * patientDistrictId=jsonObj.getLong(
																			 * "patientDistrictId");
																			 * patient.setDistrictId(patientDistrictId);
																			 * }
																			 * if(jsonObj.getLong("patientStateId")!=0)
																			 * { long patientStateId=jsonObj.getLong(
																			 * "patientStateId");
																			 * patient.setStateId(patientStateId); }
																			 * 
																			 * if (!jsonObj.getString("nok1Firstname").
																			 * isEmpty()) { String nok1FirstName =
																			 * jsonObj.getString("nok1Firstname");
																			 * patient.setNok1Name(nok1FirstName); }
																			 * 
																			 * if (!jsonObj.getString("nok1RelationId").
																			 * isEmpty() && Long.parseLong(jsonObj.get(
																			 * "nok1RelationId").toString()) != 0) {
																			 * long nok1RelationId =
																			 * jsonObj.getLong("nok1RelationId");
																			 * patient.setNok1RelationId((nok1RelationId
																			 * )); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok1Moblienumber").
																			 * isEmpty()) { String nok1Moblienumber =
																			 * jsonObj.getString("nok1Moblienumber");
																			 * patient.setNok1MobileNo(nok1Moblienumber)
																			 * ; }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok1Email").isEmpty(
																			 * )) { String nok1Email =
																			 * jsonObj.getString("nok1Email");
																			 * patient.setNok1EmailId(nok1Email); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok1Contactnumber").
																			 * isEmpty()) { String nok1Contactnumber =
																			 * jsonObj.getString("nok1Contactnumber");
																			 * patient.setNok1ContactNo(
																			 * nok1Contactnumber); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok1Policestation").
																			 * isEmpty()) { String nok1Policestation =
																			 * jsonObj.getString("nok1Policestation");
																			 * patient.setNok1PoliceStation(
																			 * nok1Policestation); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok1AddressLine1").
																			 * isEmpty()) { String nok1AddressLine1 =
																			 * jsonObj.getString("nok1AddressLine1");
																			 * patient.setNok1AddressLine1(
																			 * nok1AddressLine1); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok1AddressLine2").
																			 * isEmpty()) { String nok1AddressLine2 =
																			 * jsonObj.getString("nok1AddressLine2");
																			 * patient.setNok1AddressLine2(
																			 * nok1AddressLine2); } if
																			 * (!jsonObj.getString("nok1AddressLine3").
																			 * isEmpty()) { String nok1AddressLine3 =
																			 * jsonObj.getString("nok1AddressLine3");
																			 * patient.setNok1AddressLine3(
																			 * nok1AddressLine3); } if
																			 * (!jsonObj.getString("nok1AddressLine4").
																			 * isEmpty()) { String nok1AddressLine4 =
																			 * jsonObj.getString("nok1AddressLine4");
																			 * patient.setNok1AddressLine4(
																			 * nok1AddressLine4); }
																			 * 
																			 * if (!jsonObj.getString("nok1pincode").
																			 * isEmpty() &&
																			 * jsonObj.getLong("nok1pincode") != 0) {
																			 * long nok1pincode =
																			 * jsonObj.getLong("nok1pincode");
																			 * patient.setNok1PinCode(nok1pincode); }
																			 * 
																			 * if (jsonObj.getLong("nok1DistrictId") !=
																			 * 0) { long nok1DistrictId =
																			 * jsonObj.getLong("nok1DistrictId");
																			 * patient.setNok1DistrictId(nok1DistrictId)
																			 * ; } if (jsonObj.getLong("nok1StateId") !=
																			 * 0) { long nok1StateId =
																			 * jsonObj.getLong("nok1StateId");
																			 * patient.setNok1StateId(nok1StateId); } if
																			 * (jsonObj.getLong("nok1Country") != 0) {
																			 * long nok1Country =
																			 * jsonObj.getLong("nok1Country");
																			 * patient.setNok1CountryId(nok1Country); }
																			 * 
																			 * if (!jsonObj.getString("nok2Firstname").
																			 * isEmpty()) { String nok2FirstName =
																			 * jsonObj.getString("nok2Firstname");
																			 * patient.setNok2Name(nok2FirstName); }
																			 * 
																			 * if (!jsonObj.getString("nok2Relation").
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("nok2Relation"
																			 * ).toString()) != 0) { long nok2RelationId
																			 * = jsonObj.getLong("nok2Relation");
																			 * patient.setNok2RelationId((nok2RelationId
																			 * )); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok2Moblienumber").
																			 * isEmpty()) { String nok2Moblienumber =
																			 * jsonObj.getString("nok2Moblienumber");
																			 * patient.setNok2MobileNo(nok2Moblienumber)
																			 * ; }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok2Policestation").
																			 * isEmpty()) { String nok2PoliceStation =
																			 * jsonObj.getString("nok2Policestation");
																			 * patient.setNok2PoliceStation(
																			 * nok2PoliceStation); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok2Email").isEmpty(
																			 * )) { String nok2Email =
																			 * jsonObj.getString("nok2Email");
																			 * patient.setNok2EmailId(nok2Email); }
																			 * 
																			 * if (!jsonObj.getString("nok2pincode").
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("nok2pincode")
																			 * .toString()) != 0) { long nok2pincode =
																			 * Long.parseLong(jsonObj.getString(
																			 * "nok2pincode"));
																			 * patient.setNok2PinCode(nok2pincode); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok2Contactnumber").
																			 * isEmpty()) { String nok2Contactnumber =
																			 * jsonObj.getString("nok2Contactnumber");
																			 * patient.setNok2ContactNo(
																			 * nok2Contactnumber); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok2AddressLine1").
																			 * isEmpty()) { String nok2AddressLine1 =
																			 * jsonObj.getString("nok2AddressLine1");
																			 * patient.setNok2AddressLine1(
																			 * nok2AddressLine1); }
																			 * 
																			 * if
																			 * (!jsonObj.getString("nok2AddressLine2").
																			 * isEmpty()) { String nok2AddressLine2 =
																			 * jsonObj.getString("nok2AddressLine2");
																			 * patient.setNok2AddressLine2(
																			 * nok2AddressLine2); } if
																			 * (!jsonObj.getString("nok2AddressLine3").
																			 * isEmpty()) { String nok2AddressLine3 =
																			 * jsonObj.getString("nok2AddressLine3");
																			 * patient.setNok2AddressLine3(
																			 * nok2AddressLine3); } if
																			 * (!jsonObj.getString("nok2AddressLine4").
																			 * isEmpty()) { String nok2AddressLine4 =
																			 * jsonObj.getString("nok2AddressLine4");
																			 * patient.setNok2AddressLine4(
																			 * nok2AddressLine4); }
																			 * 
																			 * if (jsonObj.getLong("nok2DistrictId") !=
																			 * 0) { long nok2DistrictId =
																			 * jsonObj.getLong("nok2DistrictId");
																			 * patient.setNok2DistrictId(nok2DistrictId)
																			 * ; } if (jsonObj.getLong("nok2StateId") !=
																			 * 0) { long nok2StateId =
																			 * jsonObj.getLong("nok2StateId");
																			 * patient.setNok2StateId(nok2StateId); } if
																			 * (jsonObj.getLong("nok2Country") != 0) {
																			 * long nok2Country =
																			 * jsonObj.getLong("nok2Country");
																			 * patient.setNok2CountryId(nok2Country); }
																			 * 
																			 * session.update(patient);
																			 * 
																			 * }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public long
																			 * submitPatientDetailsForOthers(JSONObject
																			 * jObject) { long patientId=0; long
																			 * visitId=0; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx =
																			 * session.beginTransaction(); try { Patient
																			 * patient =null; JSONObject jsonObj
																			 * =jObject.getJSONObject(
																			 * "patientDetailsFormOthers");
																			 * 
																			 * long
																			 * hospitalId=jsonObj.getLong("hospitalId");
																			 * String
																			 * tokenValue=jObject.getString("tokenNo");
																			 * 
																			 * Date date = new Date();
																			 * 
																			 * if(!jsonObj.get("uhidNoPatient").toString
																			 * ().isEmpty()) { String uhidNO
																			 * =jsonObj.get("uhidNoPatient").toString();
																			 * patientId = getPatientFromUhidNo(uhidNO);
																			 * Timestamp dateTime = new
																			 * Timestamp(date.getTime());
																			 * 
																			 * Visit visit = new Visit();
																			 * 
																			 * visit.setVisitDate(dateTime);
																			 * visit.setLastChgDate((dateTime));
																			 * visit.setVisitStatus("w");
																			 * 
																			 * long departmentId=0; if
																			 * (!jsonObj.get("department").toString().
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("department").
																			 * toString())!=0) { departmentId =
																			 * Long.parseLong(jsonObj.get("department").
																			 * toString());
																			 * visit.setDepartmentId(departmentId); }
																			 * 
																			 * long appointmentTypeId=0; if
																			 * (!jsonObj.get("checkDiv").toString().
																			 * isEmpty()) { appointmentTypeId =
																			 * Long.parseLong(jsonObj.get("checkDiv").
																			 * toString()); visit.setAppointmentTypeId(
																			 * appointmentTypeId); }
																			 * 
																			 * visit.setHospitalId(hospitalId);
																			 * visit.setPatientId(patientId);
																			 * 
																			 * long appointmentSessionId=
																			 * getAppointmentSessionId(hospitalId,
																			 * departmentId,appointmentTypeId) ;
																			 * 
																			 * visit.setSessionId(appointmentSessionId);
																			 * 
																			 * 
																			 * String visitFlag="";
																			 * if(!jsonObj.getString("visitFlag").
																			 * isEmpty()) { visitFlag =
																			 * jsonObj.getString("visitFlag");
																			 * visit.setVisitFlag(visitFlag); }
																			 * 
																			 * 
																			 * if (!jsonObj.get("priority").toString().
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("priority").
																			 * toString())!=0) { long priority =
																			 * Long.parseLong(jsonObj.get("priority").
																			 * toString()); visit.setPriority(priority);
																			 * } if (!tokenValue.isEmpty()) { long
																			 * tokenNo = Long.parseLong(tokenValue);
																			 * visit.setTokenNo(tokenNo);
																			 * 
																			 * } long examTypeId=0; // It will blank in
																			 * case of others. long existingVisitId =0;
																			 * // It will blank in case of others.
																			 * boolean existFlag=
																			 * checkExistingAppointmentForPatient(
																			 * dateTime,hospitalId,departmentId,
																			 * patientId,appointmentTypeId,examTypeId,
																			 * existingVisitId); if(!existFlag) {
																			 * session.save(visit); tx.commit();
																			 * visitId= visit.getVisitId();
																			 * 
																			 * }else { visitId=0; } }else {
																			 * 
																			 * String mobileNo="";
																			 * if(!jsonObj.get("mobilenumber").toString(
																			 * ).isEmpty()) {
																			 * mobileNo=jsonObj.get("mobilenumber").
																			 * toString(); }
																			 * 
																			 * String patientName = "";
																			 * if(!jsonObj.get("firstname").toString().
																			 * isEmpty()) { String firstname =
																			 * jsonObj.get("firstname").toString();
																			 * patientName=firstname; }
																			 * 
																			 * boolean existingFlag
																			 * =checkExistingOtherPatient(mobileNo,
																			 * patientName); if(!existingFlag) { // new
																			 * patient will create patient =new
																			 * Patient();
																			 * 
																			 * patient.setMobileNumber(mobileNo);
																			 * patient.setPatientName(patientName);
																			 * 
																			 * if(!jsonObj.get("idnumber").toString().
																			 * isEmpty()) { String
																			 * idnumber=jsonObj.get("idnumber").toString
																			 * ();
																			 * patient.setIdentificationNo(idnumber); }
																			 * 
																			 * if (!jsonObj.get("empName").toString().
																			 * isEmpty()) { String empName =
																			 * jsonObj.get("empName").toString();
																			 * patient.setEmployeeName(empName); }
																			 * 
																			 * 
																			 * long relationId = 0; if
																			 * (!jsonObj.get("relationId").toString().
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("relationId").
																			 * toString()) != 0) { relationId =
																			 * Long.parseLong(jsonObj.get("relationId").
																			 * toString());
																			 * patient.setRelationId(relationId);
																			 * 
																			 * }
																			 * 
																			 * 
																			 * long registrationType=0;
																			 * if(!jsonObj.get("registrationType").
																			 * toString().isEmpty() &&
																			 * Long.parseLong(jsonObj.get(
																			 * "registrationType").toString())!=0) {
																			 * registrationType =
																			 * Long.parseLong(jsonObj.get(
																			 * "registrationType").toString());
																			 * patient.setRegistrationTypeId(
																			 * registrationType);
																			 * 
																			 * } if(!jsonObj.get("gender").toString().
																			 * isEmpty()) { long gender =
																			 * Long.parseLong(jsonObj.get("gender").
																			 * toString());
																			 * patient.setAdministrativeSexId(gender);
																			 * 
																			 * }
																			 * if(!jsonObj.get("serviceTypeId").toString
																			 * ().isEmpty() &&
																			 * Long.parseLong(jsonObj.get(
																			 * "serviceTypeId").toString())!=0) { long
																			 * serviceTypeId =
																			 * Long.parseLong(jsonObj.get(
																			 * "serviceTypeId").toString());
																			 * patient.setServiceStatusId(serviceTypeId)
																			 * ;; }
																			 * 
																			 * if(!jsonObj.get("rank").toString().
																			 * isEmpty()) { String rank =
																			 * jsonObj.get("rank").toString();
																			 * patient.setOtherRank(rank); }
																			 * 
																			 * 
																			 * if(!jsonObj.get("identification").
																			 * toString().isEmpty() &&
																			 * Long.parseLong(jsonObj.get(
																			 * "identification").toString())!=0) { long
																			 * identification =
																			 * Long.parseLong(jsonObj.get(
																			 * "identification").toString());
																			 * patient.setIdentificationTypeId(
																			 * identification);; }
																			 * 
																			 * 
																			 * String serviceNo="";
																			 * if(!jsonObj.get("serviceNo").toString().
																			 * isEmpty()) { serviceNo=
																			 * jsonObj.get("serviceNo").toString();
																			 * patient.setServiceNo(serviceNo); }
																			 * 
																			 * 
																			 * String selfRelationCode =
																			 * HMSUtil.getProperties("adt.properties",
																			 * "SELF_RELATION_CODE").trim(); long
																			 * patientRelationId=
																			 * getRelationIdFromCode(selfRelationCode);
																			 * 
																			 * String uhidNO =
																			 * getHinNo(serviceNo,patientRelationId,
																			 * registrationType);
																			 * patient.setUhidNo(uhidNO);
																			 * 
																			 * 
																			 * if(jsonObj.get("serviceNo").toString().
																			 * isEmpty()) {
																			 * patient.setServiceNo(uhidNO); }
																			 * 
																			 * 
																			 * if(!jsonObj.get("dataOfBirth").toString()
																			 * .isEmpty()) { String dataOfBirth =
																			 * jsonObj.get("dataOfBirth").toString();
																			 * try { patient.setDateOfBirth(HMSUtil.
																			 * convertStringDateToUtilDate(dataOfBirth,
																			 * "dd/MM/yyyy")); } catch (Exception e) {
																			 * // TODO Auto-generated catch block
																			 * e.printStackTrace(); }
																			 * 
																			 * }
																			 * 
																			 * patient.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * 
																			 * session.save(patient); patientId =
																			 * patient.getPatientId();
																			 * 
																			 * if(patientId!=0) { Visit visit = new
																			 * Visit(); Timestamp dateTime = new
																			 * Timestamp(date.getTime());
																			 * 
																			 * visit.setVisitDate(dateTime);
																			 * visit.setLastChgDate((dateTime));
																			 * visit.setVisitStatus("w");
																			 * 
																			 * 
																			 * long departmentId=0; if
																			 * (!jsonObj.get("department").toString().
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("department").
																			 * toString())!=0) { departmentId =
																			 * Long.parseLong(jsonObj.get("department").
																			 * toString());
																			 * visit.setDepartmentId(departmentId); }
																			 * 
																			 * long appointmentTypeId=0; if
																			 * (!jsonObj.get("checkDiv").toString().
																			 * isEmpty()) { appointmentTypeId =
																			 * Long.parseLong(jsonObj.get("checkDiv").
																			 * toString()); visit.setAppointmentTypeId(
																			 * appointmentTypeId); }
																			 * 
																			 * 
																			 * visit.setHospitalId(hospitalId);
																			 * visit.setPatientId(patientId);
																			 * 
																			 * long appointmentSessionId=
																			 * getAppointmentSessionId(hospitalId,
																			 * departmentId,appointmentTypeId) ;
																			 * 
																			 * visit.setSessionId(appointmentSessionId);
																			 * 
																			 * 
																			 * String visitFlag="";
																			 * if(!jsonObj.getString("visitFlag").
																			 * isEmpty()) { visitFlag =
																			 * jsonObj.getString("visitFlag");
																			 * visit.setVisitFlag(visitFlag); }
																			 * 
																			 * 
																			 * if (!jsonObj.get("priority").toString().
																			 * isEmpty() &&
																			 * Long.parseLong(jsonObj.get("priority").
																			 * toString())!=0) { long priority =
																			 * Long.parseLong(jsonObj.get("priority").
																			 * toString()); visit.setPriority(priority);
																			 * } if (!tokenValue.isEmpty()) { long
																			 * tokenNo = Long.parseLong(tokenValue);
																			 * visit.setTokenNo(tokenNo);
																			 * 
																			 * }
																			 * 
																			 * session.save(visit); tx.commit();
																			 * visitId=visit.getVisitId();
																			 * 
																			 * if(!jsonObj.get("registrationType").
																			 * toString().isEmpty() &&
																			 * Long.parseLong(jsonObj.get(
																			 * "registrationType").toString())==3) {
																			 * updateServiceNoWithUhidNo(patientId); } }
																			 * }else { visitId=-1; // For already
																			 * registered users. } }
																			 * 
																			 * }catch(Exception e) { visitId=-2;
																			 * tx.rollback();
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); }
																			 * 
																			 * return visitId;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked") private
																			 * void updateServiceNoWithUhidNo(long
																			 * patientId) { String serviceNo=""; Patient
																			 * patient=null; List<Patient> patientList =
																			 * new ArrayList<Patient>(); Session session
																			 * = getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx =
																			 * session.beginTransaction(); try {
																			 * patientList =
																			 * session.createCriteria(Patient.class)
																			 * .add(Restrictions.eq("patientId",
																			 * patientId)).list();
																			 * 
																			 * if(patientList.size()>0) {
																			 * patient=patientList.get(0); serviceNo =
																			 * patientList.get(0).getUhidNo();
																			 * patient.setServiceNo(serviceNo);
																			 * 
																			 * session.update(patient); tx.commit(); }
																			 * }catch(Exception e) {
																			 * e.printStackTrace(); } }
																			 * 
																			 * 
																			 * 
																			 * @Override public String getHinNo(String
																			 * serviceNo,long patientRelationId,long
																			 * registrationTypeId) {
																			 * 
																			 * Map<String, Object> serviceAndRelationMap
																			 * = new HashMap<String, Object>(); long
																			 * IcgRegistrationTypeId
																			 * =Long.parseLong(HMSUtil.getProperties(
																			 * "adt.properties",
																			 * "ICG_REGISTRATION_TYPE_ID"));
																			 * 
																			 * String uhidNO = ""; String patientCode =
																			 * ""; String relationCode = ""; long
																			 * dependentCount=0; //boolean
																			 * existsUhidNo=false;
																			 * 
																			 * serviceAndRelationMap =
																			 * getPatientTypeCodeAndRelationCode(
																			 * patientRelationId,registrationTypeId);
																			 * 
																			 * if (serviceAndRelationMap.get(
																			 * "relationCode") != null) { relationCode =
																			 * (String)
																			 * serviceAndRelationMap.get("relationCode")
																			 * ; } if (serviceAndRelationMap.get(
																			 * "registrationTypeCode") != null) {
																			 * patientCode = (String)
																			 * serviceAndRelationMap.get(
																			 * "registrationTypeCode"); } if
																			 * (registrationTypeId==
																			 * IcgRegistrationTypeId) {
																			 * if(relationCode.equalsIgnoreCase("N") ||
																			 * relationCode.equalsIgnoreCase("D")) {
																			 * dependentCount =
																			 * getNoOfDependentForServiceNo(serviceNo,
																			 * patientRelationId); if(dependentCount!=0)
																			 * {
																			 * relationCode=relationCode+(dependentCount
																			 * +1); uhidNO=
																			 * patientCode.concat(serviceNo).concat(
																			 * relationCode); }else {
																			 * relationCode=relationCode+1; uhidNO=
																			 * patientCode.concat(serviceNo).concat(
																			 * relationCode); } }else { uhidNO =
																			 * patientCode.concat(serviceNo).concat(
																			 * relationCode); }
																			 * 
																			 * } else { String maxSequenceNo = "";
																			 * maxSequenceNo =
																			 * getHinIdOthers(patientCode); Integer i;
																			 * if (!maxSequenceNo.equals("")) { i =
																			 * Integer.parseInt(maxSequenceNo) + 1;
																			 * 
																			 * } else { i = 01; } String seqNo = ""; if
																			 * (i <= 9) { seqNo = "0" + i.toString(); }
																			 * else { seqNo = i.toString(); } uhidNO =
																			 * patientCode.concat(seqNo.toString()); }
																			 * return uhidNO; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public long
																			 * getPatientFromUhidNo(String uhidNO) {
																			 * Patient patient = null; long patientId =
																			 * 0; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { patient = (Patient)
																			 * session.createCriteria(Patient.class).add
																			 * (Restrictions.eq("uhidNo",
																			 * uhidNO).ignoreCase()) .uniqueResult(); if
																			 * (patient != null) { patientId =
																			 * patient.getPatientId(); }
																			 * 
																			 * } catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace(); }
																			 * return patientId; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unused")
																			 * 
																			 * @Override public long
																			 * saveVisitForRegisteredPatient(Visit
																			 * visit) {
																			 * 
																			 * long visitId =0;
																			 * 
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Criteria criteria =
																			 * session.createCriteria(Visit.class);
																			 * Transaction tx =
																			 * session.beginTransaction();
																			 * session.save(visit); tx.commit(); if
																			 * (visit != null) { visitId =
																			 * visit.getVisitId(); } else { visitId = 0;
																			 * } } catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return visitId; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unused")
																			 * 
																			 * @Override public long savePatient(Patient
																			 * patient) { long patientId =0; try {
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Criteria criteria =
																			 * session.createCriteria(Patient.class);
																			 * Transaction tx =
																			 * session.beginTransaction(); Serializable
																			 * id = session.save(patient); tx.commit();
																			 * if (id != null) { patientId = (long)id; }
																			 * else { patientId = 0; } } catch
																			 * (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return patientId; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasRelation>
																			 * getRelationList() { List<MasRelation>
																			 * masRelationList = new
																			 * ArrayList<MasRelation>(); Session session
																			 * = getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { masRelationList =
																			 * session.createCriteria(MasRelation.class)
																			 * .addOrder(Order.asc("relationName")).list
																			 * (); }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return
																			 * masRelationList; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public Map<String,
																			 * List<Patient>>
																			 * searchOthersRegisteredPatient(String
																			 * uhinNo, String patientName, String
																			 * serviceNo, String mobileNo, long
																			 * registrationTypeId) {
																			 * Map<String,List<Patient>> map = new
																			 * HashMap<String, List<Patient>>();
																			 * List<Patient> patientList = new
																			 * ArrayList<Patient>(); //long
																			 * ICG_REGISTRATION_TYPE_ID=
																			 * Long.parseLong(HMSUtil.getProperties(
																			 * "adt.properties",
																			 * "ICG_REGISTRATION_TYPE_ID")); try {
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Criteria cr=null;
																			 * cr=session.createCriteria(Patient.class);
																			 * if(!uhinNo.isEmpty()) {
																			 * cr=cr.add(Restrictions.eq("uhidNo",
																			 * uhinNo).ignoreCase()); }
																			 * if(!patientName.isEmpty()) {
																			 * cr=cr.add(Restrictions.ilike(
																			 * "patientName", "%"+patientName+"%"));
																			 * 
																			 * } if(!serviceNo.isEmpty()) {
																			 * cr=cr.add(Restrictions.eq("serviceNo",
																			 * serviceNo).ignoreCase()); }
																			 * if(!mobileNo.isEmpty()) {
																			 * cr=cr.add(Restrictions.eq("mobileNumber",
																			 * mobileNo)); }
																			 * 
																			 * // condition for registration type not
																			 * equal to ICG //
																			 * cr=cr.add(Restrictions.ne(
																			 * "masRegistrationType.registrationTypeId",
																			 * ICG_REGISTRATION_TYPE_ID));
																			 * 
																			 * //Below condition with registration type
																			 * id cr=cr.add(Restrictions.eq(
																			 * "masRegistrationType.registrationTypeId",
																			 * registrationTypeId)); patientList=
																			 * cr.list();
																			 * 
																			 * map.put("patientList", patientList);
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return map; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public Map<String, Object>
																			 * getPatientTypeCodeAndRelationCode(long
																			 * patientRelationId, long
																			 * registrationTypeId) {
																			 * 
																			 * List<MasRelation> masRelationList = new
																			 * ArrayList<MasRelation>();
																			 * List<MasRegistrationType>
																			 * registrationTypeList = new
																			 * ArrayList<MasRegistrationType>();
																			 * Map<String, Object> map = new
																			 * HashMap<String, Object>();
																			 * 
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); String relationCode = "";
																			 * String registrationTypeCode = "";
																			 * 
																			 * 
																			 * masRelationList =
																			 * session.createCriteria(MasRelation.class)
																			 * .add(Restrictions.eq("status",
																			 * "y").ignoreCase())
																			 * .add(Restrictions.idEq(patientRelationId)
																			 * ).list();
																			 * 
																			 * 
																			 * for (MasRelation masRelation :
																			 * masRelationList) { relationCode
																			 * =String.valueOf(masRelation.
																			 * getRelationCode()) ;
																			 * map.put("relationCode", relationCode); }
																			 * 
																			 * registrationTypeList =
																			 * session.createCriteria(
																			 * MasRegistrationType.class)
																			 * .add(Restrictions.eq("status",
																			 * "y").ignoreCase())
																			 * .add(Restrictions.idEq(registrationTypeId
																			 * )).list();
																			 * 
																			 * for (MasRegistrationType registrationType
																			 * : registrationTypeList) {
																			 * registrationTypeCode =
																			 * String.valueOf(registrationType.
																			 * getRegistrationTypeCode());
																			 * map.put("registrationTypeCode",
																			 * registrationTypeCode); } }catch
																			 * (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } return map; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public String
																			 * getHinIdOthers(String patientCode) {
																			 * String previousHinNo = ""; String
																			 * maxSequenceNo = ""; List<Patient>
																			 * previousHinNoList = new
																			 * ArrayList<Patient>();
																			 * 
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * 
																			 * previousHinNoList =
																			 * session.createCriteria(Patient.class).
																			 * createAlias("masRegistrationType", "mt")
																			 * .add(Restrictions.eq(
																			 * "mt.registrationTypeCode",
																			 * Long.parseLong(patientCode))).list();
																			 * 
																			 * if (previousHinNoList.size() > 0) {
																			 * 
																			 * ArrayList hinNoSequenceList = new
																			 * ArrayList(); for (Patient patient :
																			 * previousHinNoList) {
																			 * 
																			 * previousHinNo = (patient.getUhidNo());
																			 * String sequenceNo =
																			 * previousHinNo.substring(1); int i =
																			 * Integer.parseInt(sequenceNo);
																			 * hinNoSequenceList.add(i);
																			 * 
																			 * 
																			 * }
																			 * 
																			 * if (hinNoSequenceList.size() > 0) {
																			 * maxSequenceNo =
																			 * Collections.max(hinNoSequenceList)
																			 * .toString(); } } } catch
																			 * (HibernateException e) {
																			 * e.printStackTrace(); // session.close();
																			 * } catch (NumberFormatException e) {
																			 * e.printStackTrace(); } return
																			 * maxSequenceNo;
																			 * 
																			 * 
																			 * }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public long
																			 * getAppointmentSessionId(long hospitalId,
																			 * long departmentId, long
																			 * appointmentTypeId) { List<Long>
																			 * appSession = new ArrayList<Long>(); long
																			 * appSessionId = 0;
																			 * 
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); appSession =
																			 * session.createCriteria(
																			 * MasAppointmentSession.class)
																			 * .add(Restrictions.eq(
																			 * "masHospital.hospitalId", hospitalId))
																			 * .add(Restrictions.eq(
																			 * "masDepartment.departmentId",
																			 * departmentId)) .add(Restrictions.eq(
																			 * "masAppointmentType.appointmentTypeId",
																			 * appointmentTypeId))
																			 * .setProjection(Projections.property("id")
																			 * ).list();
																			 * 
																			 * if (appSession.size() > 0) { appSessionId
																			 * = appSession.get(0); } } catch (Exception
																			 * e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } finally { //This
																			 * session will not be close
																			 * //getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return appSessionId;
																			 * }
																			 * 
																			 * 
																			 * //By Anita
																			 * 
																			 * @Override public Map<String, Object>
																			 * findPatientAndVisitList(JSONObject
																			 * json,String serviceNo ,Timestamp
																			 * fromdateTime, Timestamp todateTime) { //
																			 * TODO Auto-generated method stub
																			 * Map<String, Object> map = new
																			 * HashMap<String, Object>(); long
																			 * registrationTypeId=
																			 * Long.parseLong(HMSUtil.getProperties(
																			 * "adt.properties",
																			 * "ICG_REGISTRATION_TYPE_ID")); int
																			 * pageSize =
																			 * Integer.parseInt(HMSUtil.getProperties(
																			 * "adt.properties", "pageSize").trim());
																			 * 
																			 * String code=""; long appointmentTypeId;
																			 * if(json.get("flag").toString().
																			 * equalsIgnoreCase("c")){ code =
																			 * HMSUtil.getProperties("adt.properties",
																			 * "APPOINTMENT_TYPE_CODE_OPD"); }
																			 * if(json.get("flag").toString().
																			 * equalsIgnoreCase("me")){ code =
																			 * HMSUtil.getProperties("adt.properties",
																			 * "APPOINTMENT_TYPE_CODE_ME"); //long
																			 * meTypeId=
																			 * getAppointmentTypeIdFromCode(code); }
																			 * if(json.get("flag").toString().
																			 * equalsIgnoreCase("mb")){ code =
																			 * HMSUtil.getProperties("adt.properties",
																			 * "APPOINTMENT_TYPE_CODE_MB"); //long
																			 * mbTypeId=
																			 * getAppointmentTypeIdFromCode(code); }
																			 * appointmentTypeId=
																			 * getAppointmentTypeIdFromCode(code);
																			 * 
																			 * int pageNo= 0; Date visitStratDate=null;
																			 * Date visitEndDate=null; Date
																			 * finalVisitEndDate=null; String name ="";
																			 * long relation=0; if(json.has("name"))
																			 * name = json.getString("name");
																			 * if(json.has("relation")) relation =
																			 * Long.parseLong(json.get("relation").
																			 * toString()); ///start
																			 * if(json.has("fromDate")) { String
																			 * startDate = json.getString("fromDate");
																			 * String endDate =
																			 * json.getString("toDate");
																			 * if((!startDate.isEmpty() &&
																			 * startDate!=null) && (!endDate.isEmpty()
																			 * && endDate!=null)) { visitStratDate =
																			 * HMSUtil.convertStringDateToUtilDate(
																			 * startDate, "dd/MM/yyyy"); visitEndDate =
																			 * HMSUtil.convertStringDateToUtilDate(
																			 * endDate, "dd/MM/yyyy");
																			 * 
																			 * Calendar cal = Calendar.getInstance();
																			 * cal.setTime(visitEndDate);
																			 * cal.set(Calendar.HOUR_OF_DAY, 0);
																			 * cal.set(Calendar.MINUTE, 59);
																			 * cal.set(Calendar.SECOND, 0);
																			 * cal.set(Calendar.MILLISECOND, 0); //Date
																			 * from = cal.getTime();
																			 * cal.set(Calendar.HOUR_OF_DAY, 23);
																			 * finalVisitEndDate = cal.getTime();
																			 * 
																			 * } }
																			 * 
																			 * 
																			 * //end
																			 * 
																			 * List totalMatches = new ArrayList(); if
																			 * (json.get("PN") != null) pageNo =
																			 * Integer.parseInt(json.get("PN").toString(
																			 * )); List<Visit> visitList = new
																			 * ArrayList<Visit>(); Criteria cr=null; try
																			 * { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * if(json.get("flag").toString().
																			 * equalsIgnoreCase("me")){ cr =
																			 * session.createCriteria(Visit.class).
																			 * createAlias("patient",
																			 * "pt").createAlias("masMedicalExamReport",
																			 * "mme"); cr.add(Restrictions.eq(
																			 * "pt.registrationTypeId",
																			 * registrationTypeId));
																			 * cr.addOrder(Order.desc("visitDate"));
																			 * }else if(json.get("flag").toString().
																			 * equalsIgnoreCase("mb")) { cr =
																			 * session.createCriteria(Visit.class).
																			 * createAlias("patient",
																			 * "pt").createAlias("patientMedBoard",
																			 * "pmb"); cr.add(Restrictions.eq(
																			 * "pt.registrationTypeId",
																			 * registrationTypeId));
																			 * cr.addOrder(Order.desc("visitDate"));
																			 * }else { cr =
																			 * session.createCriteria(Visit.class).
																			 * createAlias("patient", "pt");
																			 * cr.add(Restrictions.eq(
																			 * "pt.registrationTypeId",
																			 * registrationTypeId));
																			 * cr.addOrder(Order.desc("visitDate")); }
																			 * if(json.get("flag").toString().
																			 * equalsIgnoreCase("W")) {
																			 * cr.add(Restrictions.eq("visitFlag",
																			 * "P").ignoreCase());//show only portal
																			 * appointment } Criterion criterion=null;
																			 * Criterion crrr=null; Criterion
																			 * crrr1=null; Criterion crvisit=null;
																			 * if(fromdateTime==null &&
																			 * todateTime==null) {
																			 * criterion=Restrictions.eq("pt.serviceNo",
																			 * serviceNo).ignoreCase();
																			 * cr.add(criterion);
																			 * if(json.get("flag").toString().
																			 * equalsIgnoreCase("c")||
																			 * json.get("flag").toString().
																			 * equalsIgnoreCase("me")||
																			 * json.get("flag").toString().
																			 * equalsIgnoreCase("mb")) {
																			 * crrr1=Restrictions.eq("visitStatus",'c').
																			 * ignoreCase();
																			 * cr.add(crrr1).add(Restrictions.eq(
																			 * "appointmentTypeId", appointmentTypeId));
																			 * } }else {
																			 * criterion=Restrictions.eq("pt.serviceNo",
																			 * serviceNo).ignoreCase();
																			 * 
																			 * if(json.get("flag").toString().
																			 * equalsIgnoreCase("c")||
																			 * json.get("flag").toString().
																			 * equalsIgnoreCase("me")||
																			 * json.get("flag").toString().
																			 * equalsIgnoreCase("mb")) {
																			 * crvisit=Restrictions.eq("visitStatus",'c'
																			 * ).ignoreCase();;
																			 * cr.add(crvisit).add(Restrictions.eq(
																			 * "appointmentTypeId", appointmentTypeId));
																			 * } if(json.get("flag").toString().
																			 * equalsIgnoreCase("me")) {
																			 * if(fromdateTime!=null) {
																			 * crrr=Restrictions.ge("mme.mediceExamDate"
																			 * ,visitStratDate); } if(todateTime!=null)
																			 * { crrr1=Restrictions.le(
																			 * "mme.mediceExamDate",finalVisitEndDate);
																			 * } cr.add(criterion).add(crrr).add(crrr1);
																			 * }else if(json.get("flag").toString().
																			 * equalsIgnoreCase("mb")) {
																			 * if(fromdateTime!=null) {
																			 * crrr=Restrictions.ge(
																			 * "pmb.dateOfEnrolment",visitStratDate); }
																			 * if(todateTime!=null) {
																			 * crrr1=Restrictions.le(
																			 * "pmb.dateOfEnrolment",finalVisitEndDate);
																			 * } cr.add(criterion).add(crrr).add(crrr1);
																			 * }else { if(fromdateTime!=null) {
																			 * crrr=Restrictions.ge("visitDate",
																			 * visitStratDate); } if(todateTime!=null) {
																			 * crrr1=Restrictions.le("visitDate",
																			 * finalVisitEndDate); }
																			 * cr.add(criterion).add(crrr).add(crrr1); }
																			 * 
																			 * } if(!name.isEmpty() && name != null) {
																			 * cr.add(Restrictions.ilike(
																			 * "pt.patientName", "%"
																			 * +name.trim()+"%",MatchMode.ANYWHERE)); }
																			 * if(relation !=0) {
																			 * cr.add(Restrictions.eq("pt.relationId",
																			 * relation)); } totalMatches=cr.list();
																			 * cr.setFirstResult((pageSize) * (pageNo -
																			 * 1)); cr.setMaxResults(pageSize);
																			 * visitList= cr.list();
																			 * 
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } map.put("visitList",
																			 * visitList); map.put("totalMatches",
																			 * totalMatches.size()); return map; }
																			 * 
																			 * 
																			 * @Override public Map<String, Object>
																			 * findOpdList(String serviceNo) { // TODO
																			 * Auto-generated method stub Map<String,
																			 * Object> map = new HashMap<String,
																			 * Object>(); List<Visit> opdList = new
																			 * ArrayList<Visit>(); Criteria cr=null; try
																			 * { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); cr =
																			 * session.createCriteria(Visit.class).
																			 * createAlias("patient", "pt");
																			 * 
																			 * Criterion criterion=null;
																			 * //if(fromdateTime==null &&
																			 * todateTime==null) {
																			 * criterion=Restrictions.eq("pt.serviceNo",
																			 * serviceNo);
																			 * criterion=Restrictions.eq("visitStatus",
																			 * 'c').ignoreCase(); cr.add(criterion);
																			 * 
																			 * opdList= cr.list();
																			 * 
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } map.put("opdList",
																			 * opdList); return map; }
																			 * 
																			 * 
																			 * ///for Web portal(Anita)/// //update
																			 * appointment Status
																			 * 
																			 * @Override public void
																			 * cancelAppointment(long visitId) { // TODO
																			 * Auto-generated method stub Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx = null;
																			 * try{ tx = session.beginTransaction();
																			 * Visit visit =
																			 * (Visit)session.get(Visit.class, visitId);
																			 * visit.setVisitStatus("n");
																			 * session.update(visit); tx.commit();
																			 * }catch (HibernateException e) { if
																			 * (tx!=null) tx.rollback();
																			 * e.printStackTrace(); }finally {
																			 * session.close(); } } //reschedule
																			 * Appointment
																			 * 
																			 * @Override public String
																			 * rescheduleAppointment(long visitId, long
																			 * tokenNo, Timestamp dateTime) { // TODO
																			 * Auto-generated method stub Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx = null;
																			 * try{ tx = session.beginTransaction();
																			 * Visit visit =
																			 * (Visit)session.get(Visit.class, visitId);
																			 * visit.setTokenNo(tokenNo);
																			 * visit.setVisitDate(dateTime);
																			 * session.update(visit); tx.commit();
																			 * }catch (HibernateException e) { if
																			 * (tx!=null) tx.rollback();
																			 * e.printStackTrace(); }finally {
																			 * session.close(); } return
																			 * "Appointment has been rescheduled."; }
																			 * 
																			 * //End Web Portal//
																			 * 
																			 * @Override public Map<String,Object>
																			 * patientListForUploadDocument(String
																			 * serviceNo) { Map<String,Object> map = new
																			 * HashMap<String, Object>(); try { map =
																			 * findPatientAndDependentFromEmployee(
																			 * serviceNo); }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } return map;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<Visit>
																			 * getPatientAppointmentHistory(long uhidNo,
																			 * String startDate, String endDate) {
																			 * List<Visit> visitHistoryList = new
																			 * ArrayList<Visit>(); List<Object> patient=
																			 * new ArrayList<Object>(); Date
																			 * visitStratDate=null; Date
																			 * visitEndDate=null; Criteria cr=null;
																			 * 
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * 
																			 * patient =
																			 * session.createCriteria(Patient.class)
																			 * .add(Restrictions.eq("uhidNo", uhidNo))
																			 * .setProjection(Projections.property(
																			 * "patientId")) .list();
																			 * 
																			 * if(!patient.isEmpty() &&
																			 * patient.size()>0) { long patientId =
																			 * (long) patient.get(0); cr =
																			 * session.createCriteria(Visit.class)
																			 * .add(Restrictions.eq("patient.patientId",
																			 * patientId))
																			 * .add(Restrictions.eq("visitFlag",
																			 * "M").ignoreCase());
																			 * 
																			 * if((!startDate.isEmpty() &&
																			 * startDate!=null) && (!endDate.isEmpty()
																			 * && endDate!=null)) { visitStratDate =
																			 * HMSUtil.convertStringDateToUtilDate(
																			 * startDate, "dd-MM-yyyy"); visitEndDate =
																			 * HMSUtil.convertStringDateToUtilDate(
																			 * endDate, "dd-MM-yyyy");
																			 * 
																			 * cr=cr.add(Restrictions.ge("visitDate",
																			 * visitStratDate))
																			 * .add(Restrictions.lt("visitDate",
																			 * visitEndDate)); }
																			 * visitHistoryList=cr.list(); } } catch
																			 * (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } finally {
																			 * 
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); }
																			 * 
																			 * return visitHistoryList; }
																			 * 
																			 * 
																			 * 
																			 * @Override public boolean
																			 * getPatientVisitCancellation(long visitId)
																			 * { boolean status=false; try { Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx =
																			 * session.beginTransaction(); Visit visit =
																			 * (Visit) session.get(Visit.class,
																			 * visitId); if(visit!=null) {
																			 * visit.setVisitStatus("n");
																			 * session.update(visit); tx.commit();
																			 * status =true; }
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } finally {
																			 * 
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); }
																			 * 
																			 * return status; }
																			 * 
																			 * 
																			 * 
																			 * @Override public Map<String, Object>
																			 * checkVisitExist(long deptId, long
																			 * appointmentTypeId, long hospitalId,
																			 * String uhidNo, String visitFlag, Date
																			 * visitDate) { // TODO Auto-generated
																			 * method stub
																			 * 
																			 * Map<String, Object> map = new
																			 * HashMap<String, Object>(); List<Visit>
																			 * visitList = new ArrayList<Visit>();
																			 * Criteria cr=null; try {
																			 * 
																			 * 
																			 * //Date date = new Date(visitDate);
																			 * Calendar cal = Calendar.getInstance();
																			 * cal.setTime(visitDate);
																			 * cal.set(Calendar.HOUR_OF_DAY, 0);
																			 * cal.set(Calendar.MINUTE, 0);
																			 * cal.set(Calendar.SECOND, 0);
																			 * cal.set(Calendar.MILLISECOND, 0); Date
																			 * from = cal.getTime();
																			 * cal.set(Calendar.HOUR_OF_DAY, 23); Date
																			 * to = cal.getTime();
																			 * 
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); cr =
																			 * session.createCriteria(Visit.class).
																			 * createAlias("patient", "pt")
																			 * .createAlias("masHospital",
																			 * "mh").createAlias("masDepartment", "md")
																			 * .createAlias("masAppointmentType",
																			 * "mat");
																			 * 
																			 * cr=cr.add(Restrictions.eq("pt.uhidNo",
																			 * uhidNo))
																			 * .add(Restrictions.eq("visitStatus",
																			 * 'w').ignoreCase()) .add(Restrictions.eq(
																			 * "mat.appointmentTypeId",
																			 * appointmentTypeId))
																			 * .add(Restrictions.eq("mh.hospitalId",
																			 * hospitalId))
																			 * //.add(Restrictions.eq("visitDate",
																			 * visitDate)) .add(
																			 * Restrictions.between("visitDate", from,
																			 * to))
																			 * .add(Restrictions.eq("md.departmentId",
																			 * deptId));
																			 * 
																			 * 
																			 * visitList= cr.list();
																			 * if(visitList.size()>0) map.put("status",
																			 * "booked"); else map.put("status",0);
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); }
																			 * 
																			 * if(visitList.size()>0) map.put("status",
																			 * "booked"); else map.put("status",
																			 * "Not booked");
																			 * 
																			 * return map;
																			 * 
																			 * 
																			 * }
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasAppointmentType>
																			 * getAppointmentTypeList() {
																			 * List<MasAppointmentType> appList = new
																			 * ArrayList<MasAppointmentType>(); try {
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Criteria criteria =
																			 * session.createCriteria(MasAppointmentType
																			 * .class);
																			 * criteria.add(Restrictions.eq("status",
																			 * "Y")); appList=criteria.list();
																			 * 
																			 * } catch (Exception e) {
																			 * e.printStackTrace(); } finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return appList; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings({ "unchecked",
																			 * "static-access" }) public boolean
																			 * checkExistingAppointmentForPatient(Date
																			 * visitDate, long hospitalId, long
																			 * departmentId, long patientId, long
																			 * appointmentTypeId, long examTypeId,long
																			 * existingVisitId) { List<Visit> visitList=
																			 * new ArrayList<Visit>(); List<String>
																			 * masMedExamcodeList= new
																			 * ArrayList<String>(); boolean flag=false;
																			 * Date visitStartDate=null; Date
																			 * visitEndDate=null;
																			 * 
																			 * String date=
																			 * HMSUtil.convertDateToStringFormat(
																			 * visitDate, "dd-MM-yyyy"); visitStartDate
																			 * =
																			 * HMSUtil.convertStringDateToUtilDate(date,
																			 * "dd-MM-yyyy"); Date nextDatefromVisit =
																			 * HMSUtil.convertStringDateToUtilDate(date,
																			 * "dd-MM-yyyy"); visitEndDate
																			 * =HMSUtil.getNextDate(nextDatefromVisit) ;
																			 * 
																			 * 
																			 * String codeOpd =
																			 * HMSUtil.getProperties("adt.properties",
																			 * "APPOINTMENT_TYPE_CODE_OPD"); String
																			 * codeME =
																			 * HMSUtil.getProperties("adt.properties",
																			 * "APPOINTMENT_TYPE_CODE_ME"); String
																			 * codeMB =
																			 * HMSUtil.getProperties("adt.properties",
																			 * "APPOINTMENT_TYPE_CODE_MB");
																			 * 
																			 * 
																			 * long appointmentTypeIdOPD=
																			 * getAppointmentTypeIdFromCode(codeOpd);
																			 * long appointmentTypeME=
																			 * getAppointmentTypeIdFromCode(codeME);
																			 * long appointmentTypeMB=
																			 * getAppointmentTypeIdFromCode(codeMB);
																			 * 
																			 * 
																			 * Calendar cal = Calendar.getInstance();
																			 * int currentYear =
																			 * cal.getInstance().get(Calendar.YEAR);
																			 * cal.set(Calendar.YEAR, currentYear);
																			 * cal.set(Calendar.DAY_OF_YEAR, 1);
																			 * cal.set(Calendar.HOUR, 0);
																			 * cal.set(Calendar.MINUTE, 0);
																			 * cal.set(Calendar.SECOND, 0);
																			 * cal.set(Calendar.HOUR_OF_DAY,0); // for
																			 * converting time into 00:00:00
																			 * 
																			 * Date yearStartDate = cal.getTime(); Date
																			 * currentDate = visitEndDate;
																			 * 
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * 
																			 * if(appointmentTypeIdOPD!=
																			 * appointmentTypeId) {
																			 * 
																			 * String meForm18=HMSUtil.getProperties(
																			 * "resource.properties", "meForm18");
																			 * String meForm3A=HMSUtil.getProperties(
																			 * "resource.properties", "meForm3A");
																			 * String meForm3B=HMSUtil.getProperties(
																			 * "resource.properties", "meForm3B");
																			 * String meForm3BS=HMSUtil.getProperties(
																			 * "resource.properties","meForm3BS");
																			 * 
																			 * String mbForm15=HMSUtil.getProperties(
																			 * "resource.properties", "mbForm15");
																			 * String mbForm16=HMSUtil.getProperties(
																			 * "resource.properties", "mbForm16");
																			 * 
																			 * String examTypeCode =
																			 * getExamCodeFromId(examTypeId);
																			 * 
																			 * // Case of ME and MB visitList =
																			 * session.createCriteria(Visit.class)
																			 * .createAlias("masAppointmentType",
																			 * "appType")
																			 * .add(Restrictions.or(Restrictions.eq(
																			 * "appType.appointmentTypeId",
																			 * appointmentTypeME), Restrictions.eq(
																			 * "appType.appointmentTypeId",
																			 * appointmentTypeMB)))
																			 * .add(Restrictions.ne("visitFlag",
																			 * "E").ignoreCase())
																			 * .add(Restrictions.eq("patient.patientId",
																			 * patientId))
																			 * .add(Restrictions.ge("visitDate",
																			 * yearStartDate))
																			 * .add(Restrictions.lt("visitDate",
																			 * currentDate)) .list();
																			 * 
																			 * String examCodeDb="";
																			 * if(visitList.size()>0) { for (Visit visit
																			 * : visitList) { examCodeDb =
																			 * visit.getMasMedExam().getMedicalExamCode(
																			 * ); masMedExamcodeList.add(examCodeDb); }
																			 * if
																			 * (examTypeCode.equalsIgnoreCase(mbForm15))
																			 * {
																			 * if(masMedExamcodeList.contains(mbForm16)
																			 * || masMedExamcodeList.contains(meForm18))
																			 * { flag = true; }else { flag = false; } }
																			 * if
																			 * (examTypeCode.equalsIgnoreCase(mbForm16)
																			 * ||
																			 * examTypeCode.equalsIgnoreCase(meForm18))
																			 * { if
																			 * (masMedExamcodeList.contains(mbForm16) ||
																			 * masMedExamcodeList.contains(meForm18) ||
																			 * masMedExamcodeList.contains(examTypeCode)
																			 * ) { if(existingVisitId!=0 &&
																			 * examTypeCode.equalsIgnoreCase(mbForm16))
																			 * { flag = false; }else
																			 * if(existingVisitId!=0 &&
																			 * examTypeCode.equalsIgnoreCase(meForm18))
																			 * { flag = false; }else { flag = true; } }
																			 * else {
																			 * if(masMedExamcodeList.contains(mbForm15)
																			 * &&
																			 * examTypeCode.equalsIgnoreCase(meForm18))
																			 * { flag = true; }else { flag = false; } }
																			 * }
																			 * 
																			 * if
																			 * (examTypeCode.equalsIgnoreCase(meForm3A)
																			 * ||
																			 * examTypeCode.equalsIgnoreCase(meForm3B)
																			 * ||
																			 * examTypeCode.equalsIgnoreCase(meForm3BS))
																			 * { if
																			 * ((masMedExamcodeList.contains(meForm3A)
																			 * || masMedExamcodeList.contains(meForm3B)
																			 * ||
																			 * masMedExamcodeList.contains(meForm3BS))
																			 * ||
																			 * masMedExamcodeList.contains(examTypeCode)
																			 * ) { if(existingVisitId!=0) { flag =
																			 * false; }else { flag = true; } } else {
																			 * flag = false; } }
																			 * 
																			 * }else { flag =false; }
																			 * 
																			 * }else { // Case of OPD visitList =
																			 * session.createCriteria(Visit.class)
																			 * .add(Restrictions.eq("patient.patientId",
																			 * patientId))
																			 * .add(Restrictions.isNotNull("tokenNo"))
																			 * .add(Restrictions.disjunction()
																			 * .add(Restrictions.eq("visitStatus",
																			 * 'w').ignoreCase())
																			 * .add(Restrictions.eq("visitStatus",
																			 * 'c').ignoreCase())
																			 * .add(Restrictions.eq("visitStatus",
																			 * 'p').ignoreCase()))
																			 * .createAlias("masDepartment",
																			 * "department").add(Restrictions.eq(
																			 * "department.departmentId", departmentId))
																			 * .createAlias("masAppointmentType",
																			 * "appType").add(Restrictions.eq(
																			 * "appType.appointmentTypeId",
																			 * appointmentTypeId))
																			 * .createAlias("masHospital", "hospital")
																			 * .add(Restrictions.eq(
																			 * "hospital.hospitalId", hospitalId))
																			 * .add(Restrictions.ge("visitDate",
																			 * visitStartDate))
																			 * .add(Restrictions.lt("visitDate",
																			 * visitEndDate)).list();
																			 * 
																			 * if(visitList.size()>0) { flag =true;
																			 * }else { flag =false; } } } catch
																			 * (Exception e) { flag =false;
																			 * e.printStackTrace(); } return flag; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked") private
																			 * String getExamCodeFromId(long examTypeId)
																			 * { List<MasMedExam> masMedList = new
																			 * ArrayList<MasMedExam>(); String
																			 * examTypeCode=""; try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); masMedList =
																			 * session.createCriteria(MasMedExam.class).
																			 * add(Restrictions.eq("medicalExamId",
																			 * examTypeId)).list(); examTypeCode =
																			 * masMedList.get(0).getMedicalExamCode();
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } return
																			 * examTypeCode; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public boolean
																			 * checkExistingOtherPatient(String
																			 * mobileNo, String patientName) {
																			 * List<Patient> patientList = new
																			 * ArrayList<Patient>(); boolean
																			 * status=false; try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); patientList
																			 * =session.createCriteria(Patient.class).
																			 * add(Restrictions.eq("mobileNumber",
																			 * mobileNo))
																			 * .add(Restrictions.ilike("patientName",
																			 * patientName)).list();
																			 * if(patientList.size()>0) { status=true; }
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } return status; }
																			 * 
																			 * 
																			 * 
																			 * @Override public Patient getPatient(long
																			 * patientId) { Patient patient =null;
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); patient = (Patient)
																			 * session.get(Patient.class, patientId);
																			 * return patient;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @Override public void
																			 * updatePatientNok2Details(Patient patient)
																			 * { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx =
																			 * session.beginTransaction();
																			 * session.update(patient); tx.commit(); }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasMedExam>
																			 * getExamList(long appTypeId,Map<String,
																			 * Object> jsonData) { List<MasMedExam>
																			 * examList = new ArrayList<MasMedExam>();
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { Criterion crit=null;
																			 * Criterion onlineOffline=null;
																			 * 
																			 * Criteria criteria =
																			 * session.createCriteria(MasMedExam.class)
																			 * .add(Restrictions.eq("status","y").
																			 * ignoreCase());
																			 * 
																			 * if(jsonData.get("onlineOffline")!=null &&
																			 * jsonData.containsKey("onlineOffline")) {
																			 * criteria.add(Restrictions.or(Restrictions
																			 * .eq("onlineOffline", "O").ignoreCase(),
																			 * Restrictions.eq("onlineOffline",
																			 * "B").ignoreCase(),Restrictions.eq(
																			 * "onlineOffline", "T").ignoreCase())); }
																			 * else {
																			 * criteria.add(Restrictions.or(Restrictions
																			 * .eq("onlineOffline", "O").ignoreCase(),
																			 * Restrictions.eq("onlineOffline",
																			 * "B").ignoreCase())); } if(appTypeId!=0) {
																			 * crit=Restrictions.eq(
																			 * "masAppointmentType.appointmentTypeId",
																			 * appTypeId); criteria.add(crit); }
																			 * criteria.addOrder(Order.asc(
																			 * "medicalExamName"));
																			 * examList=criteria.list(); }catch
																			 * (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return examList; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public Map<String,Object>
																			 * getFutureAppointmentWaitingList(long
																			 * hospitalId,int pageNo ) { int pageSize =
																			 * Integer.parseInt(HMSUtil.getProperties(
																			 * "adt.properties", "pageSize").trim());
																			 * Map<String,Object> map = new
																			 * HashMap<String, Object>(); List<Visit>
																			 * futureAppointmentWatingList = new
																			 * ArrayList<Visit>(); List<Integer>
																			 * totalMatches = new ArrayList<Integer>();
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { Date tomorrowDate
																			 * =HMSUtil.getNextDate(new Date()) ;
																			 * Criteria criteria
																			 * =session.createCriteria(Visit.class)
																			 * .add(Restrictions.eq(
																			 * "masHospital.hospitalId", hospitalId))
																			 * .add(Restrictions.or(Restrictions.eq(
																			 * "visitStatus", 'w').ignoreCase(),
																			 * Restrictions.eq("cancelStatus",
																			 * 'f').ignoreCase()))
																			 * .add(Restrictions.ne("visitFlag",
																			 * "R").ignoreCase())
																			 * .add(Restrictions.gt("visitDate",
																			 * tomorrowDate))
																			 * .addOrder(Order.asc("visitDate"));
																			 * 
																			 * 
																			 * totalMatches = criteria.list();
																			 * criteria.setFirstResult((pageSize) *
																			 * (pageNo - 1));
																			 * criteria.setMaxResults(pageSize);
																			 * 
																			 * futureAppointmentWatingList =
																			 * criteria.list();
																			 * 
																			 * }catch (Exception e) { e.getMessage();
																			 * e.printStackTrace();
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); }
																			 * map.put("futureAppointmentWatingList",
																			 * futureAppointmentWatingList);
																			 * map.put("totalMatches", totalMatches);
																			 * return map; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasState>
																			 * getStateList() {
																			 * 
																			 * List<MasState> masStateList = new
																			 * ArrayList<MasState>(); Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { masStateList =
																			 * session.createCriteria(MasState.class)
																			 * .addOrder(Order.asc("stateName")).list();
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return masStateList;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasDistrict>
																			 * getDistrictList() { List<MasDistrict>
																			 * masDistrictList = new
																			 * ArrayList<MasDistrict>(); Session session
																			 * = getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { masDistrictList =
																			 * session.createCriteria(MasDistrict.class)
																			 * .addOrder(Order.asc("districtName")).list
																			 * (); }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return
																			 * masDistrictList; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public long
																			 * getNoOfDependentForServiceNo(String
																			 * serviceNo,long patientRelationId) { long
																			 * count=0; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { count= (long)
																			 * session.createCriteria(Patient.class)
																			 * .add(Restrictions.eq("serviceNo",
																			 * serviceNo)).add(Restrictions.eq(
																			 * "masRelation.relationId",
																			 * patientRelationId))
																			 * .setProjection(Projections.rowCount()).
																			 * uniqueResult();
																			 * 
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } return count;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public boolean
																			 * checkExitingPatient(String uhidNO) {
																			 * boolean flag = false; List<Patient>
																			 * patient = new ArrayList<Patient>();
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { patient =
																			 * session.createCriteria(Patient.class).add
																			 * (Restrictions.eq("uhidNo",
																			 * uhidNO)).list(); if(patient.size()>0) {
																			 * flag= true; }else { flag=false; } }catch
																			 * (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } return flag; }
																			 * 
																			 * 
																			 * 
																			 * public long updateVisitForTokenNo(long
																			 * existingVisitId,long tokenNo, String
																			 * visitFlag, Date visitDate) { Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try{ Visit visit =
																			 * (Visit)session.get(Visit.class,
																			 * existingVisitId);
																			 * visit.setTokenNo(tokenNo);
																			 * visit.setExamStatus("T");
																			 * if(visitFlag.equalsIgnoreCase("P")) {
																			 * visit.setVisitFlag(visitFlag); }
																			 * visit.setVisitDate(new
																			 * Timestamp(visitDate.getTime()));
																			 * visit.setLastChgDate(new Timestamp(new
																			 * Date().getTime()));
																			 * session.update(visit);
																			 * 
																			 * }catch (HibernateException e) {
																			 * e.printStackTrace(); e.getMessage(); }
																			 * return existingVisitId; }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasRank>
																			 * getEmpRankAndTrade(String empRankCode) {
																			 * List<MasRank> rankList = new
																			 * ArrayList<MasRank>(); Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try {
																			 * rankList=session.createCriteria(MasRank.
																			 * class) .add(Restrictions.eq("rankCode",
																			 * empRankCode)).list();
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return rankList;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasUnit>
																			 * getEmpUnitId(String empUnitCode) {
																			 * List<MasUnit> unitList = new
																			 * ArrayList<MasUnit>(); Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try {
																			 * unitList=session.createCriteria(MasUnit.
																			 * class) .add(Restrictions.eq("unitCode",
																			 * empUnitCode)).list();
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } return unitList; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public long
																			 * getAppointmentTypeIdFromCode(String
																			 * appointmentTypeCode) { long
																			 * appointmentTypeId=0; MasAppointmentType
																			 * appointmentType = null; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try {
																			 * appointmentType=(MasAppointmentType)
																			 * session.createCriteria(MasAppointmentType
																			 * .class) .add(Restrictions.eq(
																			 * "appointmentTypeCode",appointmentTypeCode
																			 * )).uniqueResult();
																			 * 
																			 * if(appointmentType!=null) {
																			 * appointmentTypeId =
																			 * appointmentType.getAppointmentTypeId(); }
																			 * 
																			 * }catch (Exception e) { e.getMessage();
																			 * e.printStackTrace();
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return
																			 * appointmentTypeId; }
																			 * 
																			 * 
																			 * @Override public long
																			 * getRelationIdFromCode(String
																			 * relationCode) {
																			 * 
																			 * long relationId=0; MasRelation
																			 * masRelation = null; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try {
																			 * masRelation=(MasRelation)
																			 * session.createCriteria(MasRelation.class)
																			 * .add(Restrictions.eq("relationCode",
																			 * relationCode)).uniqueResult();
																			 * 
																			 * if(masRelation!=null) { relationId =
																			 * masRelation.getRelationId(); }
																			 * 
																			 * }catch (Exception e) { e.getMessage();
																			 * e.printStackTrace();
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return relationId;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * public List<Object[]>
																			 * getInvestigationResultEmptyForOrderNo_old
																			 * (String orderNo,Long mainChargeCodeId) {
																			 * List<Object[]> listObject=null; Integer
																			 * count=0; try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); StringBuilder sbQuery =
																			 * new StringBuilder(); sbQuery.
																			 * append("  select dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME,ohd.ORDERHD_ID,odt.LAB_MARK,odt.urgent,odt.ORDER_DATE, ohd.VISIT_ID,ohd.OTHER_INVESTIGATION,"
																			 * ); sbQuery.append(
																			 * " ohd.DEPARTMENT_ID,ohd.HOSPITAL_ID,odt.ORDERDT_ID,rdt.RESULT_ENTRY_DT_ID,rht.RESULT_ENTRY_HD_ID,rdt.RESULT,dgUom.UOM_NAME,dgmas.MIN_NORMAL_VALUE,dgmas.MAX_NORMAL_VALUE,dgmas.investigation_Type,dgmas.SUB_CHARGECODE_ID,dgmas.MAIN_CHARGECODE_ID from  DG_ORDER_HD ohd "
																			 * ); sbQuery.
																			 * append(" join DG_ORDER_DT odt on  ohd.orderhd_id=odt.orderhd_id join DG_MAS_INVESTIGATION "
																			 * ); sbQuery.
																			 * append(" dgmas on dgmas.INVESTIGATION_ID=odt.INVESTIGATION_ID left join DG_UOM  dgUom on dgUom.UOM_ID=dgmas.UOM_ID "
																			 * ); sbQuery.
																			 * append(" left join DG_RESULT_ENTRY_DT rdt on odt.ORDERDT_ID=rdt.ORDERDT_ID left join DG_RESULT_ENTRY_HD rht on ohd.ORDERHD_ID= rht.ORDERHD_ID "
																			 * ); sbQuery.
																			 * append(" where  rdt.result IS NULL and ohd.order_no =:orderNo  and   dgmas.MAIN_CHARGECODE_ID =:mainChargeCodeId "
																			 * );
																			 * 
																			 * Query query =
																			 * session.createSQLQuery(sbQuery.toString()
																			 * );
																			 * 
																			 * query.setParameter("orderNo", orderNo);
																			 * query.setParameter("mainChargeCodeId",
																			 * mainChargeCodeId); listObject =
																			 * query.list();
																			 * if(CollectionUtils.isNotEmpty(listObject)
																			 * ) { count=listObject.size(); } }
																			 * catch(Exception e) { e.printStackTrace();
																			 * }
																			 * 
																			 * return listObject; }
																			 * 
																			 * 
																			 * @Override public List<Object[]>
																			 * getInvestigationResultEmptyForOrderNo(
																			 * String orderNo,Long mainChargeCodeId) {
																			 * List<Object[]> listObject=null; try {
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); StringBuilder sbQuery =
																			 * new StringBuilder();
																			 * /////////////////////////////////////////
																			 * ///////////////
																			 * 
																			 * sbQuery.
																			 * append("	SELECT  DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,ORDER_HD.ORDERHD_ID,ORDER_DT.LAB_MARK,ORDER_DT.urgent,ORDER_DT.ORDER_DATE, "
																			 * ); sbQuery.
																			 * append(" ORDER_HD.VISIT_ID,ORDER_HD.OTHER_INVESTIGATION, "
																			 * ); sbQuery.
																			 * append(" ORDER_HD.DEPARTMENT_ID,ORDER_HD.HOSPITAL_ID,ORDER_DT.ORDERDT_ID,''as RESULT_ENTRY_DT_ID, "
																			 * ); sbQuery.
																			 * append("  '' As RESULT_ENTRY_HD_ID,'' As RESULT ,DU.UOM_NAME,DMI.MIN_NORMAL_VALUE,DMI.MAX_NORMAL_VALUE, "
																			 * ); sbQuery.
																			 * append("  DMI.investigation_Type,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,'' as RIDC_ID,ORDER_DT.INVESTIGATION_REMARKS,'' as RANGE_VALUE "
																			 * ); sbQuery.append(" FROM    "
																			 * +DG_ORDER_HD+" ORDER_HD LEFT OUTER JOIN  "
																			 * +DG_ORDER_DT+" ORDER_DT ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID "
																			 * ); //sbQuery.
																			 * append(" LEFT OUTER JOIN  DG_RESULT_ENTRY_HD HD ON HD.ORDERHD_ID= ORDER_HD.ORDERHD_ID AND HD.SUB_CHARGECODE_ID=MSC.SUB_CHARGECODE_ID "
																			 * ); //sbQuery.
																			 * append(" LEFT OUTER JOIN  DG_RESULT_ENTRY_DT DT ON DT.RESULT_ENTRY_HD_ID=HD.RESULT_ENTRY_HD_ID  AND DT.INVESTIGATION_ID=DMI.INVESTIGATION_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_UOM+" DU ON  DU.UOM_ID=DMI.UOM_ID ");
																			 * sbQuery.
																			 * append(" WHERE     UPPER(DMI.INVESTIGATION_TYPE) IN ('S','T')   And   trim(ORDER_HD.order_no) =:orderNo  and   DMI.MAIN_CHARGECODE_ID =:mainChargeCodeId and ORDER_DT.ORDER_STATUS='P' "
																			 * );
																			 * 
																			 * sbQuery.append(" UNION "); sbQuery.
																			 * append(" SELECT  DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,ORDER_HD.ORDERHD_ID,ORDER_DT.LAB_MARK,ORDER_DT.urgent,ORDER_DT.ORDER_DATE, "
																			 * ); sbQuery.
																			 * append(" ORDER_HD.VISIT_ID,ORDER_HD.OTHER_INVESTIGATION, "
																			 * ); sbQuery.
																			 * append("  ORDER_HD.DEPARTMENT_ID,ORDER_HD.HOSPITAL_ID,ORDER_DT.ORDERDT_ID,null As RESULT_ENTRY_DT_ID, "
																			 * ); sbQuery.
																			 * append(" NULL As RESULT_ENTRY_HD_ID,NULL AS RESULT,DU.UOM_NAME,DMI.MIN_NORMAL_VALUE,DMI.MAX_NORMAL_VALUE, "
																			 * ); sbQuery.
																			 * append(" DMI.investigation_Type,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,NULL As RIDC_ID,ORDER_DT.INVESTIGATION_REMARKS, NULL As RANGE_VALUE   "
																			 * ); sbQuery.append(" FROM    "
																			 * +DG_ORDER_DT+" ORDER_DT LEFT OUTER JOIN  "
																			 * +DG_ORDER_HD+" ORDER_HD ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID "
																			 * ); sbQuery.append("  LEFT OUTER JOIN  "
																			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
																			 * ); sbQuery.append("  LEFT OUTER JOIN  "
																			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID "
																			 * ); sbQuery.append("    LEFT OUTER JOIN  "
																			 * +DG_UOM+" DU ON  DU.UOM_ID=DMI.UOM_ID ");
																			 * sbQuery.
																			 * append(" WHERE   UPPER(DMI.INVESTIGATION_TYPE) ='M'  and trim(ORDER_HD.order_no) =:orderNo  and   DMI.MAIN_CHARGECODE_ID =:mainChargeCodeId and ORDER_DT.ORDER_STATUS='P' "
																			 * );
																			 * 
																			 * 
																			 * Query query =
																			 * session.createSQLQuery(sbQuery.toString()
																			 * );
																			 * 
																			 * query.setParameter("orderNo", orderNo);
																			 * query.setParameter("mainChargeCodeId",
																			 * mainChargeCodeId); listObject =
																			 * query.list(); //transation.commit(); }
																			 * catch(Exception e) { e.printStackTrace();
																			 * } finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection();
																			 * 
																			 * } return listObject; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public List<Object[]>
																			 * getInvestigationResultEmptyForOrderNoForUnique
																			 * (String orderNo,Long mainChargeCodeId) {
																			 * List<Object[]> listObject=null; Integer
																			 * count=0; try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); StringBuilder sbQuery =
																			 * new StringBuilder(); sbQuery.
																			 * append("  select dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME "
																			 * ); sbQuery.append(
																			 * "   from  "+DG_ORDER_HD+" ohd ");
																			 * sbQuery.append(" join "
																			 * +DG_ORDER_DT+" odt on  ohd.orderhd_id=odt.orderhd_id join "
																			 * +DG_MAS_INVESTIGATION ); sbQuery.
																			 * append(" dgmas on dgmas.INVESTIGATION_ID=odt.INVESTIGATION_ID left join "
																			 * +DG_UOM+"  dgUom on dgUom.UOM_ID=dgmas.UOM_ID "
																			 * ); sbQuery.append(" left join "
																			 * +DG_RESULT_ENTRY_DT+" rdt on odt.ORDERDT_ID=rdt.ORDERDT_ID left join "
																			 * +DG_RESULT_ENTRY_HD+" rht on ohd.ORDERHD_ID= rht.ORDERHD_ID "
																			 * ); sbQuery.
																			 * append(" where  rdt.result IS NULL and ohd.order_no =:orderNo  and   dgmas.MAIN_CHARGECODE_ID =:mainChargeCodeId "
																			 * ); sbQuery.
																			 * append(" group by dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME "
																			 * ); Query query =
																			 * session.createSQLQuery(sbQuery.toString()
																			 * );
																			 * 
																			 * query.setParameter("orderNo", orderNo);
																			 * query.setParameter("mainChargeCodeId",
																			 * mainChargeCodeId); listObject =
																			 * query.list();
																			 * if(CollectionUtils.isNotEmpty(listObject)
																			 * ) { count=listObject.size(); } }
																			 * catch(Exception e) { e.printStackTrace();
																			 * }
																			 * 
																			 * return listObject; }
																			 * 
																			 * 
																			 * 
																			 * @Override public String
																			 * saveOrUpdateUploadData(String[]
																			 * investigationIdValues, String[]
																			 * dgOrderDtIdValues, String[] dgOrderHdIds,
																			 * String[] dgResultDtIds, String[]
																			 * dgResultHdIds, String[] resultInvss, Long
																			 * patientId, String hospitalId, String
																			 * userId,String orderNumberInv,String[]
																			 * rangeValue,String []
																			 * rmsIdFinalValue,HashMap<String, Object>
																			 * payload) { Session session=null;
																			 * Transaction tx=null; String
																			 * satusOfMessage=""; String
																			 * statusOfPatient=""; try {
																			 * 
																			 * session=
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * tx=session.beginTransaction();
																			 * 
																			 * HashMap<String, String>
																			 * mapInvestigationMap = new HashMap<>();
																			 * //visitDao.saveOrUpdate(visit); // Long
																			 * visitId=visit.getVisitId(); String
																			 * finalValue = ""; Integer counter = 1;
																			 * Date date=new Date(); for (int i = 0; i <
																			 * investigationIdValues.length; i++) {
																			 * 
																			 * if (StringUtils.isNotEmpty(
																			 * investigationIdValues[i].toString()) &&
																			 * !investigationIdValues[i].
																			 * equalsIgnoreCase("0")) { finalValue +=
																			 * investigationIdValues[i].trim(); if
																			 * (!dgOrderDtIdValues[i].equalsIgnoreCase(
																			 * "0") &&
																			 * StringUtils.isNotBlank(dgOrderDtIdValues[
																			 * i])) { for (int m = i; m <
																			 * dgOrderDtIdValues.length; m++) {
																			 * finalValue += "," +
																			 * dgOrderDtIdValues[m].trim(); if (m == i)
																			 * { break; } } } else { finalValue += "," +
																			 * "0"; }
																			 * 
																			 * if
																			 * (!dgOrderHdIds[i].equalsIgnoreCase("0")
																			 * &&
																			 * StringUtils.isNotBlank(dgOrderHdIds[i]))
																			 * { for (int j = i; j <
																			 * dgOrderHdIds.length; j++) { finalValue +=
																			 * "," + dgOrderHdIds[j].trim(); if (j == i)
																			 * { break; } } } else { finalValue += "," +
																			 * "0"; } if (i < dgResultDtIds.length &&
																			 * StringUtils.isNotBlank(dgResultDtIds[i])
																			 * &&
																			 * !dgResultDtIds[i].equalsIgnoreCase("0")
																			 * &&!dgResultDtIds[i].equalsIgnoreCase(
																			 * "undefined") ) { for (int k = i; k <
																			 * dgResultDtIds.length; k++) { finalValue
																			 * += "," + dgResultDtIds[k].trim(); if (k
																			 * == i) { break; } } } else { finalValue +=
																			 * "," + "0"; }
																			 * 
																			 * if (i < dgResultHdIds.length &&
																			 * StringUtils.isNotBlank(dgResultHdIds[i])
																			 * &&
																			 * !dgResultHdIds[i].trim().equalsIgnoreCase
																			 * ("0")
																			 * &&!dgResultHdIds[i].equalsIgnoreCase(
																			 * "undefined")) { for (int k = i; k <
																			 * dgResultHdIds.length; k++) { finalValue
																			 * += "," + dgResultHdIds[k].trim(); if (k
																			 * == i) { break; } } } else { finalValue +=
																			 * "," + "0"; }
																			 * 
																			 * 
																			 * if (i < resultInvss.length &&
																			 * StringUtils.isNotBlank(resultInvss[i])) {
																			 * for (int k = i; k < resultInvss.length;
																			 * k++) { finalValue += "," +
																			 * resultInvss[k].trim(); if (k == i) {
																			 * break; } } } else { finalValue += "," +
																			 * "0"; }
																			 * 
																			 * if (i < rangeValue.length &&
																			 * StringUtils.isNotBlank(rangeValue[i])) {
																			 * for (int k = i; k < rangeValue.length;
																			 * k++) { finalValue += "," +
																			 * rangeValue[k].trim(); if (k == i) {
																			 * break; } } } else { finalValue += "," +
																			 * "0"; }
																			 * 
																			 * if (i < rmsIdFinalValue.length &&
																			 * StringUtils.isNotBlank(rmsIdFinalValue[i]
																			 * )) { for (int k = i; k <
																			 * rmsIdFinalValue.length; k++) { finalValue
																			 * += "," + rmsIdFinalValue[k].trim(); if (k
																			 * == i) { break; } } } else { finalValue +=
																			 * "," + "0"; }
																			 * 
																			 * mapInvestigationMap.put(
																			 * investigationIdValues[i].trim()+"@#"+
																			 * counter, finalValue); finalValue = "";
																			 * counter++; } }
																			 * 
																			 * Long dgOrderHdId=null;
																			 * if(StringUtils.isEmpty(orderNumberInv) ||
																			 * orderNumberInv.equalsIgnoreCase("0")) {
																			 * DgOrderhd dgOrderhd=new DgOrderhd();
																			 * dgOrderhd.setHospitalId(Long.parseLong(
																			 * hospitalId));
																			 * dgOrderhd.setLastChgBy(Long.parseLong(
																			 * userId)); dgOrderhd.setOrderDate(new
																			 * Date()); dgOrderhd.setOrderStatus("P");
																			 * dgOrderhd.setPatientId(patientId);
																			 * dgOrderHdId=
																			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(
																			 * dgOrderhd); }
																			 * 
																			 * 
																			 * counter = 1; Long
																			 * dgResultEntryHdFId=null; for (String
																			 * investigationId : investigationIdValues)
																			 * { if
																			 * (StringUtils.isNotEmpty(investigationId))
																			 * { if (mapInvestigationMap.containsKey(
																			 * investigationId.trim()+"@#"+counter)) {
																			 * String investigationValue =
																			 * mapInvestigationMap.get(investigationId.
																			 * trim()+"@#"+counter);
																			 * 
																			 * if (StringUtils.isNotEmpty(
																			 * investigationValue)) {
																			 * 
																			 * String[] finalValueInvestigation =
																			 * investigationValue.split(",");
																			 * 
																			 * 
																			 * if (finalValueInvestigation[5] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[5]) &&
																			 * !finalValueInvestigation[5].equals("0")
																			 * && finalValueInvestigation[2] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[2]) &&
																			 * !finalValueInvestigation[2].equals("0") )
																			 * { DgResultEntryDt dgResultEntryDt = null;
																			 * if (finalValueInvestigation[3] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[3] ) &&
																			 * !finalValueInvestigation[3].
																			 * equalsIgnoreCase("0") &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[3])) {
																			 * dgResultEntryDt = medicalExamDAO.
																			 * getDgResultEntryDtByDgResultEntryId(Long.
																			 * parseLong(finalValueInvestigation[3]));
																			 * if(dgResultEntryDt==null) {
																			 * dgResultEntryDt=new DgResultEntryDt(); }
																			 * } else { dgResultEntryDt=new
																			 * DgResultEntryDt();
																			 * 
																			 * }
																			 * 
																			 * if (finalValueInvestigation != null) {
																			 * 
																			 * if (finalValueInvestigation != null &&
																			 * finalValueInvestigation[0] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[0])) {
																			 * dgResultEntryDt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].trim
																			 * ().toString()));
																			 * 
																			 * }
																			 * 
																			 * if (StringUtils.isNotEmpty(
																			 * finalValueInvestigation[1]) &&
																			 * !finalValueInvestigation[1].equals("0"))
																			 * dgResultEntryDt.setOrderDtId(Long.
																			 * parseLong(finalValueInvestigation[1].trim
																			 * ().toString()));
																			 * 
																			 * 
																			 * if (finalValueInvestigation[5] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[5]) &&
																			 * !finalValueInvestigation[5].equals("0"))
																			 * dgResultEntryDt.setResult(
																			 * finalValueInvestigation[5].toString());
																			 * 
																			 * if (finalValueInvestigation[6] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[6]) &&
																			 * !finalValueInvestigation[6].equals("0"))
																			 * dgResultEntryDt.setRangeValue(
																			 * finalValueInvestigation[6].toString());
																			 * 
																			 * if (finalValueInvestigation[7] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[7]) &&
																			 * !finalValueInvestigation[7].equals("0"))
																			 * dgResultEntryDt.setRidcId(Long.parseLong(
																			 * finalValueInvestigation[7].trim().
																			 * toString()));
																			 * 
																			 * }
																			 * 
																			 * DgResultEntryHd dgResultEntryHd=null;
																			 * Long dgResultentryId=null; if
																			 * (finalValueInvestigation[4] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[4]) &&
																			 * !finalValueInvestigation[4].equals("0"))
																			 * dgResultEntryHd= medicalExamDAO.
																			 * getDgResultEntryHdByPatientIdAndHospitalId
																			 * (Long.parseLong(finalValueInvestigation[4
																			 * ].trim().toString())) ;
																			 * 
																			 * if(dgResultEntryHd==null) {
																			 * 
																			 * if(patientId!=null &&
																			 * StringUtils.isNotEmpty(hospitalId)) {
																			 * dgResultEntryHd= medicalExamDAO.
																			 * getDgResultEntryHdByPatientIdAndHospitalIds
																			 * (patientId,Long.parseLong(hospitalId)) ;
																			 * 
																			 * } if(dgResultEntryHd!=null) {
																			 * dgResultentryId=dgResultEntryHd.
																			 * getResultEntryId(); } }
																			 * 
																			 * if(dgResultEntryHd!=null) {
																			 * dgResultentryId=dgResultEntryHd.
																			 * getResultEntryId(); }
																			 * 
																			 * if(dgResultEntryHd==null &&
																			 * dgResultEntryHdFId==null) {
																			 * 
																			 * dgResultEntryHd=new DgResultEntryHd();
																			 * if(StringUtils.isNotEmpty(hospitalId))
																			 * dgResultEntryHd.setHospitalId(Long.
																			 * parseLong(hospitalId)); else {
																			 * dgResultEntryHd.setHospitalId(null); }
																			 * if(patientId!=null)
																			 * dgResultEntryHd.setPatientId(patientId);
																			 * else {
																			 * dgResultEntryHd.setPatientId(null); }
																			 * if(StringUtils.isNotEmpty(userId))
																			 * dgResultEntryHd.setLastChgBy(Long.
																			 * parseLong(userId)); else {
																			 * dgResultEntryHd.setLastChgBy(null); }
																			 * dgResultEntryHd.setLastChgDate(new
																			 * Date());
																			 * dgResultEntryHd.setInvestigationId(
																			 * Long.parseLong(finalValueInvestigation[0]
																			 * .trim().toString()));
																			 * dgResultEntryHd.setResultDate(new
																			 * Date()); if(StringUtils.isNotEmpty(
																			 * finalValueInvestigation[2]) &&
																			 * !finalValueInvestigation[2].equals("0") )
																			 * dgResultEntryHd.setOrderHdId(Long.
																			 * parseLong(finalValueInvestigation[2].trim
																			 * ().toString()));
																			 * dgResultEntryHdFId=dgResultEntryHdDao.
																			 * saveOrUpdateDgResultEntryHdInv(
																			 * dgResultEntryHd); //dgResultEntryHdFId
																			 * =dgResultEntryHd.getResultEntryId();
																			 * //saveOrUpdateDgResultEntryHd(
																			 * dgResultEntryHd) ;
																			 * //dgResultEntryHdFId=dgResultentryId; }
																			 * if(dgResultEntryHdFId!=null) {
																			 * dgResultentryId=dgResultEntryHdFId; }
																			 * dgResultEntryDt.setResultEntryId(
																			 * dgResultEntryHdFId);
																			 * 
																			 * if (finalValueInvestigation[1] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[1]) &&
																			 * finalValueInvestigation[1].equals("0")) {
																			 * DgOrderdt dgOrderdt=new DgOrderdt();
																			 * if(finalValueInvestigation[0]!=null)
																			 * dgOrderdt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString()));
																			 * if(StringUtils.isNotEmpty(userId))
																			 * dgOrderdt.setLastChgBy(Long.parseLong(
																			 * userId)); else {
																			 * dgOrderdt.setLastChgBy(null); }
																			 * if(dgOrderHdIds[0]!=null &&
																			 * StringUtils.isNotEmpty(dgOrderHdIds[0]))
																			 * { dgOrderdt.setOrderhdId(Long.parseLong(
																			 * dgOrderHdIds[0].trim().toString())); }
																			 * 
																			 * dgOrderdt.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgOrderdt.setOrderDate(new Date());
																			 * //Long dgOrderDtId=
																			 * saveUpdADgOrderDt(dgOrderdt); Long
																			 * dgOrderDtId=dgOrderdtDao.
																			 * saveOrUpdateDgOrderdtInv(dgOrderdt);
																			 * //Long
																			 * dgOrderDtId=dgOrderdt.getOrderdtId();
																			 * dgResultEntryDt.setOrderDtId(dgOrderDtId)
																			 * ; } dgResultEntryDtDao.
																			 * saveOrUpdateDgResultEntryDtInv(
																			 * dgResultEntryDt);
																			 * //saveOrUpdateDgResultEntryDt(
																			 * dgResultEntryDt) ; }
																			 * 
																			 * else { if (finalValueInvestigation[1] !=
																			 * null && StringUtils.isNotBlank(
																			 * finalValueInvestigation[1]) &&
																			 * finalValueInvestigation[1].equals("0") &&
																			 * dgOrderHdIds[0]!=null) {
																			 * 
																			 * DgOrderdt dgOrderdt=new DgOrderdt();
																			 * dgOrderdt.setOrderStatus("P");
																			 * dgOrderdt.setLabMark("O");
																			 * dgOrderdt.setUrgent("n");
																			 * dgOrderdt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString()));
																			 * if(StringUtils.isNotEmpty(userId))
																			 * dgOrderdt.setLastChgBy(Long.parseLong(
																			 * userId)); else {
																			 * dgOrderdt.setLastChgBy(null); }
																			 * dgOrderdt.setOrderhdId(dgOrderHdId);
																			 * dgOrderdt.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgOrderdt.setOrderDate(new Date()); //
																			 * saveUpdADgOrderDt(dgOrderdt); Long
																			 * orderDtId=dgOrderdtDao.
																			 * saveOrUpdateDgOrderdtInv(dgOrderdt);
																			 * //Long orderDtId=
																			 * dgOrderdt.getOrderdtId();
																			 * List<DgResultEntryHd>listDgResultEntryHd=
																			 * null; if(dgResultEntryHdFId==null &&
																			 * dgResultEntryHdFId==null) {
																			 * listDgResultEntryHd=dgResultEntryHdDao.
																			 * findByOrderIdAndPatientId(patientId,
																			 * dgOrderHdId); } DgResultEntryHd
																			 * dgResultEntryHd =null;
																			 * if(CollectionUtils.isEmpty(
																			 * listDgResultEntryHd) ) { dgResultEntryHd
																			 * =new DgResultEntryHd(); } else {
																			 * //dgResultEntryHd=listDgResultEntryHd.get
																			 * (0); } //DgResultEntryHd dgResultEntryHd
																			 * =new DgResultEntryHd();
																			 * if(dgResultEntryHdFId==null) {
																			 * dgResultEntryHd.setHospitalId(Long.
																			 * parseLong(hospitalId));
																			 * //dgResultEntryHd.setVisitId(visitId);
																			 * dgResultEntryHd.setOrderHdId(dgOrderHdId)
																			 * ;
																			 * dgResultEntryHd.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString()));
																			 * dgResultEntryHd.setPatientId(patientId);
																			 * dgResultEntryHd.setLastChgBy(Long.
																			 * parseLong(userId));
																			 * dgResultEntryHd.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgResultEntryHd.setResultDate(new
																			 * Date());
																			 * dgResultEntryHdFId=dgResultEntryHdDao.
																			 * saveOrUpdateDgResultEntryHdInv(
																			 * dgResultEntryHd); //
																			 * dgResultEntryHdFId=dgResultEntryHd.
																			 * getResultEntryId(); } DgResultEntryDt
																			 * dgResultEntryDt=new DgResultEntryDt();
																			 * dgResultEntryDt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString())); dgResultEntryDt.setResult(
																			 * finalValueInvestigation[5].toString());
																			 * 
																			 * if (finalValueInvestigation[6] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[6]) &&
																			 * !finalValueInvestigation[6].equals("0"))
																			 * dgResultEntryDt.setRangeValue(
																			 * finalValueInvestigation[6].toString());
																			 * 
																			 * 
																			 * if (finalValueInvestigation[7] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[7]) &&
																			 * !finalValueInvestigation[7].equals("0"))
																			 * dgResultEntryDt.setRidcId(Long.parseLong(
																			 * finalValueInvestigation[7].trim().
																			 * toString()));
																			 * dgResultEntryDt.setOrderDtId(orderDtId);
																			 * dgResultEntryDt.setResultEntryId(
																			 * dgResultEntryHdFId); dgResultEntryDtDao.
																			 * saveOrUpdateDgResultEntryDtInv(
																			 * dgResultEntryDt); } } } }
																			 * 
																			 * } counter++; }
																			 * 
																			 * 
																			 * session.flush(); session.clear();
																			 * tx.commit(); satusOfMessage=
																			 * 1+"##"+"Upload data successfully "; }
																			 * catch(Exception e) { if (tx != null) {
																			 * try { // tx.rollback(); satusOfMessage=0+
																			 * "##"+"Data is not updated because something is wrong"
																			 * +e.toString() ; } catch(Exception re) {
																			 * satusOfMessage=0+
																			 * "##"+"Data is not updated because something is wrong"
																			 * +re.toString() ; re.printStackTrace(); }
																			 * } satusOfMessage=0+
																			 * "##"+"Data is not updated because something is wrong"
																			 * +e.toString(); e.printStackTrace(); }
																			 * finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection();
																			 * 
																			 * } return satusOfMessage; }
																			 * 
																			 * int countForRange=0; int
																			 * countForResult=0; int
																			 * countForOrderDtId=0; int
																			 * countForOrderHdId=0; int
																			 * countForRIDCId=0; int
																			 * countForUmoSubInv=0;
																			 * 
																			 * int countSubMainChargeCodeIdForSub=0; int
																			 * countMainChargeCodeIdForSub=0; protected
																			 * void deInitGlobalValue() {
																			 * countForRange=0; countForResult=0;
																			 * countForOrderDtId=0; countForOrderHdId=0;
																			 * countForRIDCId=0; countForUmoSubInv=0;
																			 * countSubMainChargeCodeIdForSub=0;
																			 * countMainChargeCodeIdForSub=0; }
																			 * 
																			 * @Override
																			 * 
																			 * @Transactional public String
																			 * saveOrUpdateUploadData(String[]
																			 * investigationIdValues, String[]
																			 * dgOrderDtIdValues, String[] dgOrderHdIds,
																			 * String[] dgResultDtIds, String[]
																			 * dgResultHdIds, String[] resultInvss, Long
																			 * patientId, String hospitalId, String
																			 * userId,String orderNumberInv,String[]
																			 * rangeValue,String []
																			 * rmsIdFinalValue,HashMap<String, Object>
																			 * payload,Long visitId) { Session
																			 * session=null; Transaction tx=null; String
																			 * satusOfMessage=""; String
																			 * statusOfPatient="";
																			 * 
																			 * try {
																			 * 
																			 * session=
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * tx=session.beginTransaction();
																			 * deInitGlobalValue(); HashMap<String,
																			 * String> mapInvestigationMap = new
																			 * HashMap<>();
																			 * //visitDao.saveOrUpdate(visit); // Long
																			 * visitId=visit.getVisitId(); String
																			 * finalValue = ""; Integer counter = 1;
																			 * Date date=new Date(); String[]
																			 * investigationTypes=null;
																			 * if(payload.containsKey(
																			 * "investigationType")) {
																			 * 
																			 * String investigationType=payload.get(
																			 * "investigationType").toString();
																			 * investigationType=OpdServiceImpl.
																			 * getReplaceString(investigationType);
																			 * investigationTypes=investigationType.
																			 * split(",");
																			 * 
																			 * } String[] resultSubInvs=null;
																			 * if(payload.containsKey("resultSubInv")) {
																			 * String
																			 * resultSubInv=payload.get("resultSubInv").
																			 * toString(); resultSubInv=OpdServiceImpl.
																			 * getReplaceString(resultSubInv);
																			 * //resultSubInvs=resultSubInv.split(",");
																			 * resultSubInvs=resultSubInv.split("@@@###"
																			 * ); }
																			 * 
																			 * String[] rangeSubInvess=null;
																			 * if(payload.containsKey("rangeSubInves"))
																			 * { String
																			 * rangeSubInves=payload.get("rangeSubInves"
																			 * ).toString();
																			 * rangeSubInves=OpdServiceImpl.
																			 * getReplaceString(rangeSubInves);
																			 * rangeSubInvess=rangeSubInves.split(",");
																			 * }
																			 * 
																			 * String[]
																			 * subInvestigationNameIdAndInvs=null;
																			 * if(payload.containsKey(
																			 * "subInvestigationNameIdAndInv")) { String
																			 * subInvestigationNameIdAndInv=payload.get(
																			 * "subInvestigationNameIdAndInv").toString(
																			 * ); subInvestigationNameIdAndInv=
																			 * OpdServiceImpl.getReplaceString(
																			 * subInvestigationNameIdAndInv);
																			 * subInvestigationNameIdAndInvs=
																			 * subInvestigationNameIdAndInv.split(",");
																			 * }
																			 * 
																			 * 
																			 * String[] dgOrderDtIdValueSubInvess=null;
																			 * if(payload.containsKey(
																			 * "dgOrderDtIdValueSubInves")) { String
																			 * dgOrderDtIdValueSubInves=payload.get(
																			 * "dgOrderDtIdValueSubInves").toString();
																			 * dgOrderDtIdValueSubInves=OpdServiceImpl.
																			 * getReplaceString(dgOrderDtIdValueSubInves
																			 * ); dgOrderDtIdValueSubInvess=
																			 * dgOrderDtIdValueSubInves.split(","); }
																			 * 
																			 * String[] dgOrderHdIdSubInvess=null;
																			 * if(payload.containsKey(
																			 * "dgOrderHdIdSubInves")) { String
																			 * dgOrderHdIdSubInves=payload.get(
																			 * "dgOrderHdIdSubInves").toString();
																			 * dgOrderHdIdSubInves=OpdServiceImpl.
																			 * getReplaceString(dgOrderHdIdSubInves);
																			 * dgOrderHdIdSubInvess=dgOrderHdIdSubInves.
																			 * split(","); }
																			 * 
																			 * String[] UOMSubss=null;
																			 * if(payload.containsKey("UOMSub")) {
																			 * String
																			 * UOMSub=payload.get("UOMSub").toString();
																			 * UOMSub=OpdServiceImpl.getReplaceString(
																			 * UOMSub); UOMSubss=UOMSub.split(","); }
																			 * 
																			 * String[] subChargecodeIds=null;
																			 * if(payload.containsKey(
																			 * "subChargecodeIdForInv")) { String
																			 * subChargecodeId=payload.get(
																			 * "subChargecodeIdForInv").toString();
																			 * subChargecodeId=OpdServiceImpl.
																			 * getReplaceString(subChargecodeId);
																			 * subChargecodeIds=subChargecodeId.split(
																			 * ","); }
																			 * 
																			 * 
																			 * String[] mainChargecodeIdVals=null;
																			 * if(payload.containsKey(
																			 * "mainChargecodeIdValForInv")) { String
																			 * mainChargecodeIdVal=payload.get(
																			 * "mainChargecodeIdValForInv").toString();
																			 * mainChargecodeIdVal=OpdServiceImpl.
																			 * getReplaceString(mainChargecodeIdVal);
																			 * mainChargecodeIdVals=mainChargecodeIdVal.
																			 * split(","); }
																			 * 
																			 * 
																			 * String[] subChargecodeIdsForSubs=null;
																			 * if(payload.containsKey(
																			 * "subChargecodeIdForSub")) { String
																			 * subChargecodeIdForSub=payload.get(
																			 * "subChargecodeIdForSub").toString();
																			 * subChargecodeIdForSub=OpdServiceImpl.
																			 * getReplaceString(subChargecodeIdForSub);
																			 * subChargecodeIdsForSubs=
																			 * subChargecodeIdForSub.split(","); }
																			 * 
																			 * 
																			 * String[]
																			 * mainChargecodeIdValsForSubs=null;
																			 * if(payload.containsKey(
																			 * "mainChargecodeIdValForSub")) { String
																			 * mainChargecodeIdValForSub=payload.get(
																			 * "mainChargecodeIdValForSub").toString();
																			 * mainChargecodeIdValForSub=OpdServiceImpl.
																			 * getReplaceString(
																			 * mainChargecodeIdValForSub);
																			 * mainChargecodeIdValsForSubs=
																			 * mainChargecodeIdValForSub.split(","); }
																			 * 
																			 * 
																			 * /////////////////////////////////////////
																			 * /////////////////
																			 * 
																			 * String[]
																			 * investigationResultDateArray=null;
																			 * if(payload.containsKey(
																			 * "investigationResultDate")) { String
																			 * investigationResultDate=payload.get(
																			 * "investigationResultDate").toString();
																			 * investigationResultDate=OpdServiceImpl.
																			 * getReplaceString(investigationResultDate)
																			 * ; investigationResultDateArray=
																			 * investigationResultDate.split(","); }
																			 * String[] resultNumberArray=null;
																			 * if(payload.containsKey("resultNumber")) {
																			 * String
																			 * resultNumber=payload.get("resultNumber").
																			 * toString(); resultNumber=OpdServiceImpl.
																			 * getReplaceString(resultNumber);
																			 * resultNumberArray=resultNumber.split(",")
																			 * ; }
																			 * /////////////////////////////////////////
																			 * ////////////////////
																			 * 
																			 * 
																			 * 
																			 * 
																			 * for (int i = 0; i <
																			 * investigationIdValues.length; i++) {
																			 * 
																			 * if (StringUtils.isNotEmpty(
																			 * investigationIdValues[i].toString()) &&
																			 * !investigationIdValues[i].
																			 * equalsIgnoreCase("0")) { finalValue +=
																			 * investigationIdValues[i].trim(); if
																			 * (!dgOrderDtIdValues[i].equalsIgnoreCase(
																			 * "0") &&
																			 * StringUtils.isNotBlank(dgOrderDtIdValues[
																			 * i])) { for (int m = i; m <
																			 * dgOrderDtIdValues.length; m++) {
																			 * finalValue += "###" +
																			 * dgOrderDtIdValues[m].trim(); if (m == i)
																			 * { break; } } } else { finalValue += "###"
																			 * + "0"; }
																			 * 
																			 * if
																			 * (!dgOrderHdIds[i].equalsIgnoreCase("0")
																			 * &&
																			 * StringUtils.isNotBlank(dgOrderHdIds[i]))
																			 * { for (int j = i; j <
																			 * dgOrderHdIds.length; j++) { finalValue +=
																			 * "###" + dgOrderHdIds[j].trim(); if (j ==
																			 * i) { break; } } } else { finalValue +=
																			 * "###" + "0"; } if (i <
																			 * dgResultDtIds.length &&
																			 * StringUtils.isNotBlank(dgResultDtIds[i])
																			 * &&
																			 * !dgResultDtIds[i].equalsIgnoreCase("0")
																			 * &&!dgResultDtIds[i].equalsIgnoreCase(
																			 * "undefined") ) { for (int k = i; k <
																			 * dgResultDtIds.length; k++) { finalValue
																			 * += "###" + dgResultDtIds[k].trim(); if (k
																			 * == i) { break; } } } else { finalValue +=
																			 * "###" + "0"; }
																			 * 
																			 * if (i < dgResultHdIds.length &&
																			 * StringUtils.isNotBlank(dgResultHdIds[i])
																			 * &&
																			 * !dgResultHdIds[i].trim().equalsIgnoreCase
																			 * ("0")
																			 * &&!dgResultHdIds[i].equalsIgnoreCase(
																			 * "undefined")) { for (int k = i; k <
																			 * dgResultHdIds.length; k++) { finalValue
																			 * += "###" + dgResultHdIds[k].trim(); if (k
																			 * == i) { break; } } } else { finalValue +=
																			 * "###" + "0"; } int tempI=0;
																			 * if(resultInvss!=null &&
																			 * resultInvss.length>0) { tempI=i; i++; if
																			 * (i < resultInvss.length &&
																			 * StringUtils.isNotBlank(resultInvss[i])) {
																			 * 
																			 * for (int k = i; k < resultInvss.length;
																			 * k++) { finalValue += "###" +
																			 * resultInvss[k].trim();
																			 * 
																			 * if (k == i) { i=tempI; tempI=0; break; }
																			 * } } else { i=tempI; tempI=0; finalValue
																			 * += "###" + "0"; } } else { i=tempI;
																			 * tempI=0; finalValue += "###" + "0"; }
																			 * 
																			 * if (i < rangeValue.length &&
																			 * StringUtils.isNotBlank(rangeValue[i])) {
																			 * for (int k = i; k < rangeValue.length;
																			 * k++) { finalValue += "###" +
																			 * rangeValue[k].trim(); if (k == i) {
																			 * break; } } } else { finalValue += "###" +
																			 * "0"; }
																			 * 
																			 * if (i < rmsIdFinalValue.length &&
																			 * StringUtils.isNotBlank(rmsIdFinalValue[i]
																			 * )) { for (int k = i; k <
																			 * rmsIdFinalValue.length; k++) { finalValue
																			 * += "###" + rmsIdFinalValue[k].trim(); if
																			 * (k == i) { break; } } } else { finalValue
																			 * += "###" + "0"; }
																			 * 
																			 * 
																			 * if (i < investigationTypes.length &&
																			 * StringUtils.isNotBlank(investigationTypes
																			 * [i])) { for (int k = i; k <
																			 * investigationTypes.length; k++) {
																			 * finalValue += "###" +
																			 * investigationTypes[k].trim(); if (k == i)
																			 * { break; } } } else { finalValue += "###"
																			 * + "0"; }
																			 * 
																			 * 
																			 * if (i < subChargecodeIds.length &&
																			 * StringUtils.isNotBlank(subChargecodeIds[i
																			 * ])) { for (int k = i; k <
																			 * subChargecodeIds.length; k++) {
																			 * finalValue += "###" +
																			 * subChargecodeIds[k].trim(); if (k == i) {
																			 * break; } } } else { finalValue += "###" +
																			 * "0"; }
																			 * 
																			 * 
																			 * if (i < mainChargecodeIdVals.length &&
																			 * StringUtils.isNotBlank(
																			 * mainChargecodeIdVals[i])) { for (int k =
																			 * i; k < mainChargecodeIdVals.length; k++)
																			 * { finalValue += "###" +
																			 * mainChargecodeIdVals[k].trim(); if (k ==
																			 * i) { break; } } } else { finalValue +=
																			 * "###" + "0"; }
																			 * 
																			 * if (investigationResultDateArray!=null &&
																			 * i < investigationResultDateArray.length
																			 * && StringUtils.isNotBlank(
																			 * investigationResultDateArray[i])) { for
																			 * (int k = i; k <
																			 * investigationResultDateArray.length; k++)
																			 * { finalValue += "###" +
																			 * investigationResultDateArray[k].trim();
																			 * if (k == i) { break; } } } else {
																			 * finalValue += "###" + "0"; }
																			 * 
																			 * if (resultNumberArray!=null && i <
																			 * resultNumberArray.length &&
																			 * StringUtils.isNotBlank(resultNumberArray[
																			 * i])) { for (int k = i; k <
																			 * resultNumberArray.length; k++) {
																			 * finalValue += "###" +
																			 * resultNumberArray[k].trim(); if (k == i)
																			 * { break; } } } else { finalValue += "###"
																			 * + "0"; }
																			 * 
																			 * 
																			 * 
																			 * mapInvestigationMap.put(
																			 * investigationIdValues[i].trim()+"@#"+
																			 * counter, finalValue); finalValue = "";
																			 * counter++; } }
																			 * 
																			 * Long dgOrderHdId=null; DgOrderhd
																			 * dgOrderhd=null;
																			 * if(StringUtils.isEmpty(orderNumberInv) ||
																			 * orderNumberInv.equalsIgnoreCase("0")) {
																			 * dgOrderhd=new DgOrderhd();
																			 * dgOrderhd.setHospitalId(Long.parseLong(
																			 * hospitalId));
																			 * dgOrderhd.setLastChgBy(Long.parseLong(
																			 * userId));
																			 * dgOrderhd.setDoctorId(Long.parseLong(
																			 * userId)); dgOrderhd.setOrderDate(new
																			 * Date()); dgOrderhd.setOrderStatus("C");
																			 * dgOrderhd.setPatientId(patientId);
																			 * dgOrderhd.setVisitId(visitId);
																			 * dgOrderHdId=dgOrderhdDao.
																			 * saveOrUpdateDgOrderHdInv(dgOrderhd); //
																			 * dgOrderhdDao.saveOrUpdate(dgOrderhd); //
																			 * dgOrderHdId=dgOrderhd.getOrderhdId(); }
																			 * else { if(dgOrderHdIds!=null ) {
																			 * DgOrderhd dgOrderhd=dgOrderhdDao.
																			 * getDgOrderHdByOrderHdId(Long.parseLong(
																			 * dgOrderHdIds[0].toString()));
																			 * dgOrderhd.setOrderStatus("C");
																			 * dgOrderHdId=dgOrderhdDao.
																			 * saveOrUpdateDgOrderHdInv(dgOrderhd);
																			 * dgOrderHdId=dgOrderhd.getOrderhdId();
																			 * dgOrderHdId=Long.parseLong(dgOrderHdIds[0
																			 * ].toString());
																			 * 
																			 * 
																			 * } }
																			 * 
																			 * 
																			 * counter = 1;
																			 * 
																			 * Long dgResultEntryHdFId=null;
																			 * 
																			 * for (String investigationId :
																			 * investigationIdValues) { if
																			 * (StringUtils.isNotEmpty(investigationId))
																			 * { if (mapInvestigationMap.containsKey(
																			 * investigationId.trim()+"@#"+counter)) {
																			 * String investigationValue =
																			 * mapInvestigationMap.get(investigationId.
																			 * trim()+"@#"+counter); String[]
																			 * finalValueInvestigation=null;
																			 * 
																			 * if (StringUtils.isNotEmpty(
																			 * investigationValue)) {
																			 * 
																			 * finalValueInvestigation =
																			 * investigationValue.split("###");
																			 * 
																			 * if(finalValueInvestigation[8] != null &&
																			 * !finalValueInvestigation[8].equals("0")
																			 * && (finalValueInvestigation[8].
																			 * equalsIgnoreCase("s")||
																			 * finalValueInvestigation[8].
																			 * equalsIgnoreCase("t"))) {
																			 * 
																			 * 
																			 * if (finalValueInvestigation[5] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[5]) &&
																			 * !finalValueInvestigation[5].toString().
																			 * equalsIgnoreCase("0") &&
																			 * finalValueInvestigation[2] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[2]) &&
																			 * !finalValueInvestigation[2].toString().
																			 * equalsIgnoreCase("0") ) {
																			 * 
																			 * 
																			 * DgResultEntryDt dgResultEntryDt = null;
																			 * if (finalValueInvestigation[3] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[3] ) &&
																			 * !finalValueInvestigation[3].
																			 * equalsIgnoreCase("0") &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[3])) {
																			 * dgResultEntryDt = medicalExamDAO.
																			 * getDgResultEntryDtByDgResultEntryId(Long.
																			 * parseLong(finalValueInvestigation[3]));
																			 * if(dgResultEntryDt==null) {
																			 * dgResultEntryDt=new DgResultEntryDt(); }
																			 * } else { dgResultEntryDt=new
																			 * DgResultEntryDt();
																			 * 
																			 * }
																			 * 
																			 * if (finalValueInvestigation != null) {
																			 * 
																			 * if (finalValueInvestigation != null &&
																			 * finalValueInvestigation[0] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[0])) {
																			 * dgResultEntryDt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].trim
																			 * ().toString()));
																			 * 
																			 * }
																			 * 
																			 * if (StringUtils.isNotEmpty(
																			 * finalValueInvestigation[1]) &&
																			 * !finalValueInvestigation[1].toString().
																			 * equalsIgnoreCase("0")) {
																			 * 
																			 * DgOrderdt dgOrderdt=dgOrderdtDao.
																			 * getDgOrderdtByOrderDtId(Long.parseLong(
																			 * finalValueInvestigation[1].trim()));
																			 * dgOrderdt.setOrderStatus("C"); Long
																			 * dgOrderDtId =medicalExamDAO.
																			 * saveOrUpdateDgOrderdtForDgOrderdt(
																			 * dgOrderdt);
																			 * dgResultEntryDt.setOrderDtId(dgOrderDtId)
																			 * ; }
																			 * 
																			 * if (finalValueInvestigation[5] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[5]) &&
																			 * !finalValueInvestigation[5].equals("0"))
																			 * {
																			 * 
																			 * 
																			 * Character charVal=null; String result="";
																			 * charVal =
																			 * finalValueInvestigation[5].charAt(
																			 * finalValueInvestigation[5].length() - 1);
																			 * 
																			 * if(charVal!=null && charVal.equals(','))
																			 * { result =
																			 * finalValueInvestigation[5].toString().
																			 * substring(0,
																			 * finalValueInvestigation[5].toString().
																			 * length() - 1); dgResultEntryDt.setResult(
																			 * MedicalExamDAOImpl.getHtmlText(result));
																			 * } else { dgResultEntryDt.setResult(
																			 * MedicalExamDAOImpl.getHtmlText(
																			 * finalValueInvestigation[5].toString()));
																			 * }
																			 * 
																			 * 
																			 * int index=finalValueInvestigation[5].
																			 * lastIndexOf(","); if(index==-1) {
																			 * dgResultEntryDt.setResult(
																			 * finalValueInvestigation[5].toString()); }
																			 * else { String result =
																			 * finalValueInvestigation[5].toString().
																			 * substring(0,
																			 * finalValueInvestigation[5].toString().
																			 * length() - 1);
																			 * dgResultEntryDt.setResult(result); }
																			 * 
																			 * }
																			 * 
																			 * if (finalValueInvestigation[6] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[6]) &&
																			 * !finalValueInvestigation[6].equals("0"))
																			 * dgResultEntryDt.setRangeValue(
																			 * finalValueInvestigation[6].toString());
																			 * 
																			 * if (finalValueInvestigation[7] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[7]) &&
																			 * !finalValueInvestigation[7].equals("0"))
																			 * dgResultEntryDt.setRidcId(Long.parseLong(
																			 * finalValueInvestigation[7].trim().
																			 * toString()));
																			 * 
																			 * }
																			 * 
																			 * DgResultEntryHd dgResultEntryHd=null;
																			 * Long dgResultentryId=null; if
																			 * (finalValueInvestigation[4] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[4]) &&
																			 * !finalValueInvestigation[4].equals("0"))
																			 * dgResultEntryHd= medicalExamDAO.
																			 * getDgResultEntryHdByPatientIdAndHospitalId
																			 * (Long.parseLong(finalValueInvestigation[4
																			 * ].trim().toString())) ;
																			 * 
																			 * if(dgResultEntryHd==null) {
																			 * 
																			 * if(patientId!=null &&
																			 * StringUtils.isNotEmpty(hospitalId)) {
																			 * dgResultEntryHd= medicalExamDAO.
																			 * getDgResultEntryHdByPatientIdAndHospitalIds
																			 * (patientId,Long.parseLong(hospitalId)) ;
																			 * } if(StringUtils.isNotEmpty(
																			 * finalValueInvestigation[9]) &&
																			 * !finalValueInvestigation[9].equals("0")
																			 * && StringUtils.isNotEmpty(
																			 * finalValueInvestigation[10]) &&
																			 * !finalValueInvestigation[10].equals("0")
																			 * && dgOrderHdId!=null) {
																			 * 
																			 * Criterion
																			 * cr1=Restrictions.eq("subChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[9]
																			 * )); Criterion
																			 * cr2=Restrictions.eq("mainChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[10
																			 * ])); Criterion
																			 * cr3=Restrictions.eq("orderHdId",
																			 * dgOrderHdId);
																			 * List<DgResultEntryHd>listDgResultEntryHd=
																			 * dgResultEntryHdDao.findByCriteria(cr1,cr2
																			 * ,cr3);
																			 * 
																			 * dgResultEntryHd =dgResultEntryHdDao.
																			 * getDgResultEntryHdByInvestigationIdAndOrderhdId
																			 * (Long.parseLong(finalValueInvestigation[9
																			 * ]),Long.parseLong(finalValueInvestigation
																			 * [10]) ,dgOrderHdId);
																			 * 
																			 * if(CollectionUtils.isNotEmpty(
																			 * listDgResultEntryHd))
																			 * dgResultEntryHd=listDgResultEntryHd.get(0
																			 * ); } if(dgResultEntryHd!=null) {
																			 * dgResultentryId=dgResultEntryHd.
																			 * getResultEntryId(); } }
																			 * 
																			 * if(dgResultEntryHd!=null) {
																			 * dgResultentryId=dgResultEntryHd.
																			 * getResultEntryId(); }
																			 * 
																			 * if(dgResultEntryHd==null) {
																			 * 
																			 * Criterion
																			 * cr1=Restrictions.eq("subChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[9]
																			 * )); Criterion
																			 * cr2=Restrictions.eq("mainChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[10
																			 * ])); Criterion
																			 * cr3=Restrictions.eq("orderHdId",
																			 * dgOrderHdId);
																			 * List<DgResultEntryHd>listDgResultEntryHd=
																			 * dgResultEntryHdDao.findByCriteria(cr1,cr2
																			 * ,cr3); if(CollectionUtils.isNotEmpty(
																			 * listDgResultEntryHd))
																			 * dgResultEntryHd=listDgResultEntryHd.get(0
																			 * ); dgResultEntryHd =dgResultEntryHdDao.
																			 * getDgResultEntryHdByInvestigationIdAndOrderhdId
																			 * (Long.parseLong(finalValueInvestigation[9
																			 * ]),Long.parseLong(finalValueInvestigation
																			 * [10]) ,dgOrderHdId);
																			 * 
																			 * if(dgResultEntryHd==null)
																			 * dgResultEntryHd=new DgResultEntryHd();
																			 * if(StringUtils.isNotEmpty(hospitalId))
																			 * dgResultEntryHd.setHospitalId(Long.
																			 * parseLong(hospitalId)); else {
																			 * dgResultEntryHd.setHospitalId(null); }
																			 * if(patientId!=null)
																			 * dgResultEntryHd.setPatientId(patientId);
																			 * else {
																			 * dgResultEntryHd.setPatientId(null); }
																			 * if(StringUtils.isNotEmpty(userId))
																			 * dgResultEntryHd.setLastChgBy(Long.
																			 * parseLong(userId)); else {
																			 * dgResultEntryHd.setLastChgBy(null); }
																			 * dgResultEntryHd.setLastChgDate(new
																			 * Date());
																			 * //dgResultEntryHd.setInvestigationId(
																			 * Long.parseLong(finalValueInvestigation[0]
																			 * .trim().toString()));
																			 * dgResultEntryHd.setResultDate(new
																			 * Date()); if(dgOrderHdId!=null)
																			 * dgResultEntryHd.setOrderHdId(dgOrderHdId)
																			 * ; dgResultEntryHd.setResultStatus("C");
																			 * dgResultEntryHd.setCreatedBy(Long.
																			 * parseLong(userId));
																			 * 
																			 * if(StringUtils.isNotEmpty(
																			 * finalValueInvestigation[9]) &&
																			 * !finalValueInvestigation[9].equals("0") )
																			 * dgResultEntryHd.setSubChargecodeId(Long.
																			 * parseLong(finalValueInvestigation[9]));
																			 * 
																			 * if(StringUtils.isNotEmpty(
																			 * finalValueInvestigation[10]) &&
																			 * !finalValueInvestigation[10].equals("0")
																			 * )
																			 * dgResultEntryHd.setMainChargecodeId(Long.
																			 * parseLong(finalValueInvestigation[10]));
																			 * dgResultEntryHd.setCreatedBy(Long.
																			 * parseLong(userId));
																			 * 
																			 * dgResultEntryHd.setVerifiedOn(new
																			 * Date());
																			 * dgResultEntryHd.setVerified("V");
																			 * dgResultEntryHd.setResultVerifiedBy(Long.
																			 * parseLong(userId));
																			 * dgResultEntryHd.setVisitId(visitId);
																			 * 
																			 * dgResultEntryHdDao.saveOrUpdate(
																			 * dgResultEntryHd); dgResultentryId
																			 * =dgResultEntryHd.getResultEntryId();
																			 * 
																			 * dgResultEntryHdFId=medicalExamDAO.
																			 * saveOrUpdateDgResultEntryHd(
																			 * dgResultEntryHd) ;
																			 * //dgResultEntryHdFId=dgResultentryId; }
																			 * if(dgResultEntryHdFId!=null) {
																			 * dgResultentryId=dgResultEntryHdFId; }
																			 * dgResultEntryDt.setResultEntryId(
																			 * dgResultentryId);
																			 * 
																			 * if (finalValueInvestigation[1] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[1]) &&
																			 * finalValueInvestigation[1].toString().
																			 * equalsIgnoreCase("0")) { DgOrderdt
																			 * dgOrderdt=new DgOrderdt();
																			 * if(finalValueInvestigation[0]!=null)
																			 * dgOrderdt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString()));
																			 * if(StringUtils.isNotEmpty(userId))
																			 * dgOrderdt.setLastChgBy(Long.parseLong(
																			 * userId)); else {
																			 * dgOrderdt.setLastChgBy(null); }
																			 * if(dgOrderHdIds[0]!=null &&
																			 * StringUtils.isNotEmpty(dgOrderHdIds[0]))
																			 * { dgOrderdt.setOrderhdId(Long.parseLong(
																			 * dgOrderHdIds[0].trim().toString())); }
																			 * 
																			 * 
																			 * dgOrderdt.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgOrderdt.setOrderDate(new Date());
																			 * //Long dgOrderDtId=
																			 * saveUpdADgOrderDt(dgOrderdt);
																			 * dgOrderdt.setOrderStatus("C");
																			 * //dgOrderdtDao.saveOrUpdate(dgOrderdt);
																			 * 
																			 * 
																			 * if (finalValueInvestigation != null &&
																			 * finalValueInvestigation[11] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[11]) &&
																			 * !finalValueInvestigation[11].equals("0"))
																			 * { Date resultOfflineDate =
																			 * HMSUtil.convertStringTypeDateToDateType(
																			 * finalValueInvestigation[11]);
																			 * dgOrderdt.setResultOffLineDate(
																			 * resultOfflineDate); } if
																			 * (finalValueInvestigation != null &&
																			 * finalValueInvestigation[12] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[12]) &&
																			 * !finalValueInvestigation[12].equals("0"))
																			 * { dgOrderdt.setResultOffLineNumber(
																			 * finalValueInvestigation[12]); }
																			 * 
																			 * 
																			 * 
																			 * Long dgOrderDtId =medicalExamDAO.
																			 * saveOrUpdateDgOrderdtForDgOrderdt(
																			 * dgOrderdt);
																			 * 
																			 * //Long
																			 * dgOrderDtId=dgOrderdt.getOrderdtId();
																			 * dgResultEntryDt.setOrderDtId(dgOrderDtId)
																			 * ; }
																			 * 
																			 * 
																			 * dgResultEntryDt.setValidated("V");
																			 * dgResultEntryDt.setResultDetailStatus("C"
																			 * );
																			 * 
																			 * if (finalValueInvestigation[8] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[8]) &&
																			 * !finalValueInvestigation[8].equals("0"))
																			 * dgResultEntryDt.setResultType(
																			 * finalValueInvestigation[8].trim().
																			 * toString());
																			 * 
																			 * 
																			 * if (finalValueInvestigation != null &&
																			 * finalValueInvestigation[11] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[11]) &&
																			 * !finalValueInvestigation[11].equals("0"))
																			 * { Date resultOfflineDate =
																			 * HMSUtil.convertStringTypeDateToDateType(
																			 * finalValueInvestigation[11]);
																			 * dgResultEntryDt.setResultOffLineDate(
																			 * resultOfflineDate); } if
																			 * (finalValueInvestigation != null &&
																			 * finalValueInvestigation[12] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[12]) &&
																			 * !finalValueInvestigation[12].equals("0"))
																			 * { dgResultEntryDt.setResultOffLineNumber(
																			 * finalValueInvestigation[12]); }
																			 * 
																			 * //dgResultEntryDtDao.saveOrUpdate(
																			 * dgResultEntryDt); dgResultEntryDtDao.
																			 * saveOrUpdateDgResultEntryDtInv(
																			 * dgResultEntryDt) ; }
																			 * 
																			 * else { if (finalValueInvestigation[1] !=
																			 * null && StringUtils.isNotBlank(
																			 * finalValueInvestigation[1]) &&
																			 * finalValueInvestigation[1].equals("0") &&
																			 * dgOrderHdIds[0]!=null) {
																			 * 
																			 * DgOrderdt dgOrderdt=new DgOrderdt();
																			 * dgOrderdt.setOrderStatus("C");
																			 * dgOrderdt.setLabMark("O");
																			 * dgOrderdt.setUrgent("n");
																			 * dgOrderdt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString()));
																			 * if(StringUtils.isNotEmpty(userId))
																			 * dgOrderdt.setLastChgBy(Long.parseLong(
																			 * userId)); else {
																			 * dgOrderdt.setLastChgBy(null); }
																			 * dgOrderdt.setOrderhdId(dgOrderHdId);
																			 * dgOrderdt.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgOrderdt.setOrderDate(new Date()); //
																			 * saveUpdADgOrderDt(dgOrderdt); //
																			 * dgOrderdtDao.saveOrUpdate(dgOrderdt);
																			 * 
																			 * 
																			 * if (finalValueInvestigation != null &&
																			 * finalValueInvestigation[11] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[11]) &&
																			 * !finalValueInvestigation[11].equals("0"))
																			 * { Date resultOfflineDate =
																			 * HMSUtil.convertStringTypeDateToDateType(
																			 * finalValueInvestigation[11]);
																			 * dgOrderdt.setResultOffLineDate(
																			 * resultOfflineDate); } if
																			 * (finalValueInvestigation != null &&
																			 * finalValueInvestigation[12] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[12]) &&
																			 * !finalValueInvestigation[12].equals("0"))
																			 * { dgOrderdt.setResultOffLineNumber(
																			 * finalValueInvestigation[12]); }
																			 * 
																			 * 
																			 * medicalExamDAO.
																			 * saveOrUpdateDgOrderdtForDgOrderdt(
																			 * dgOrderdt);
																			 * 
																			 * DgResultEntryHd dgResultEntryHd =null;
																			 * Criterion
																			 * cr1=Restrictions.eq("subChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[9]
																			 * )); Criterion
																			 * cr2=Restrictions.eq("mainChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[10
																			 * ])); Criterion
																			 * cr3=Restrictions.eq("orderHdId",
																			 * dgOrderHdId);
																			 * List<DgResultEntryHd>listDgResultEntryHd=
																			 * dgResultEntryHdDao.findByCriteria(cr1,cr2
																			 * ,cr3); if(CollectionUtils.isNotEmpty(
																			 * listDgResultEntryHd))
																			 * dgResultEntryHd=listDgResultEntryHd.get(0
																			 * );
																			 * 
																			 * dgResultEntryHd =dgResultEntryHdDao.
																			 * getDgResultEntryHdByInvestigationIdAndOrderhdId
																			 * (Long.parseLong(finalValueInvestigation[9
																			 * ]),Long.parseLong(finalValueInvestigation
																			 * [10]) ,dgOrderHdId);
																			 * 
																			 * if(dgResultEntryHd==null)
																			 * dgResultEntryHd=new DgResultEntryHd();
																			 * 
																			 * dgResultEntryHd.setHospitalId(Long.
																			 * parseLong(hospitalId));
																			 * //dgResultEntryHd.setVisitId(visitId);
																			 * if(dgOrderHdId!=null)
																			 * dgResultEntryHd.setOrderHdId(dgOrderHdId)
																			 * ; //
																			 * dgResultEntryHd.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString()));
																			 * dgResultEntryHd.setPatientId(patientId);
																			 * dgResultEntryHd.setLastChgBy(Long.
																			 * parseLong(userId));
																			 * dgResultEntryHd.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgResultEntryHd.setResultDate(new
																			 * Date());
																			 * dgResultEntryHd.setResultStatus("C");
																			 * dgResultEntryHd.setCreatedBy(Long.
																			 * parseLong(userId));
																			 * 
																			 * dgResultEntryHd.setVerifiedOn(new
																			 * Date());
																			 * dgResultEntryHd.setVerified("V");
																			 * dgResultEntryHd.setResultVerifiedBy(Long.
																			 * parseLong(userId));
																			 * 
																			 * if(StringUtils.isNotEmpty(
																			 * finalValueInvestigation[9]) &&
																			 * !finalValueInvestigation[9].equals("0") )
																			 * dgResultEntryHd.setSubChargecodeId(Long.
																			 * parseLong(finalValueInvestigation[9]));
																			 * 
																			 * if(StringUtils.isNotEmpty(
																			 * finalValueInvestigation[10]) &&
																			 * !finalValueInvestigation[10].equals("0")
																			 * )
																			 * dgResultEntryHd.setMainChargecodeId(Long.
																			 * parseLong(finalValueInvestigation[10]));
																			 * dgResultEntryHd.setVisitId(visitId);
																			 * 
																			 * //dgResultEntryHdDao.saveOrUpdate(
																			 * dgResultEntryHd);
																			 * dgResultEntryHdFId=medicalExamDAO.
																			 * saveOrUpdateDgResultEntryHd(
																			 * dgResultEntryHd) ;
																			 * //dgResultEntryHdFId=dgResultEntryHd.
																			 * getResultEntryId();
																			 * 
																			 * DgResultEntryDt dgResultEntryDt=new
																			 * DgResultEntryDt();
																			 * dgResultEntryDt.setInvestigationId(Long.
																			 * parseLong(finalValueInvestigation[0].
																			 * toString())); if
																			 * (finalValueInvestigation[5] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[5]) &&
																			 * !finalValueInvestigation[5].equals("0"))
																			 * {
																			 * 
																			 * Character charVal=null; String result="";
																			 * charVal =
																			 * finalValueInvestigation[5].charAt(
																			 * finalValueInvestigation[5].length() - 1);
																			 * 
																			 * if(charVal!=null && charVal.equals(','))
																			 * { result =
																			 * finalValueInvestigation[5].toString().
																			 * substring(0,
																			 * finalValueInvestigation[5].toString().
																			 * length() - 1); dgResultEntryDt.setResult(
																			 * MedicalExamDAOImpl.getHtmlText(result));
																			 * } else { dgResultEntryDt.setResult(
																			 * MedicalExamDAOImpl.getHtmlText(
																			 * finalValueInvestigation[5].toString()));
																			 * }
																			 * 
																			 * 
																			 * }
																			 * 
																			 * 
																			 * 
																			 * if (finalValueInvestigation[6] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[6]) &&
																			 * !finalValueInvestigation[6].equals("0"))
																			 * dgResultEntryDt.setRangeValue(
																			 * finalValueInvestigation[6].toString());
																			 * 
																			 * 
																			 * if (finalValueInvestigation[7] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[7]) &&
																			 * !finalValueInvestigation[7].equals("0"))
																			 * dgResultEntryDt.setRidcId(Long.parseLong(
																			 * finalValueInvestigation[7].trim().
																			 * toString()));
																			 * 
																			 * if (finalValueInvestigation[8] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[8]) &&
																			 * !finalValueInvestigation[8].equals("0"))
																			 * dgResultEntryDt.setResultType(
																			 * finalValueInvestigation[8].trim().
																			 * toString());
																			 * 
																			 * 
																			 * if (finalValueInvestigation != null &&
																			 * finalValueInvestigation[11] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[11]) &&
																			 * !finalValueInvestigation[11].equals("0"))
																			 * { Date resultOfflineDate =
																			 * HMSUtil.convertStringTypeDateToDateType(
																			 * finalValueInvestigation[11]);
																			 * dgResultEntryDt.setResultOffLineDate(
																			 * resultOfflineDate); } if
																			 * (finalValueInvestigation != null &&
																			 * finalValueInvestigation[12] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[12]) &&
																			 * !finalValueInvestigation[12].equals("0"))
																			 * { dgResultEntryDt.setResultOffLineNumber(
																			 * finalValueInvestigation[12]); }
																			 * 
																			 * 
																			 * dgResultEntryDt.setOrderDtId(dgOrderdt.
																			 * getOrderdtId());
																			 * dgResultEntryDt.setResultEntryId(
																			 * dgResultEntryHdFId);
																			 * dgResultEntryDt.setResultDetailStatus("C"
																			 * ); dgResultEntryDt.setValidated("V");
																			 * 
																			 * 
																			 * 
																			 * 
																			 * medicalExamDAO.
																			 * saveOrUpdateDgResultEntryDt(
																			 * dgResultEntryDt); //
																			 * dgResultEntryDtDao.saveOrUpdate(
																			 * dgResultEntryDt); } }
																			 * 
																			 * if(dgOrderHdIds!=null &&
																			 * finalValueInvestigation[2]!=null &&
																			 * !finalValueInvestigation[2].
																			 * equalsIgnoreCase("0")) {
																			 * 
																			 * List<DgOrderdt>
																			 * listDgOrderdt=dgOrderhdDao.
																			 * getDgOrderDtByOrderHdId(Long.parseLong(
																			 * finalValueInvestigation[2].toString()));
																			 * if(CollectionUtils.isEmpty(listDgOrderdt)
																			 * ) { dgOrderhd=dgOrderhdDao.
																			 * getDgOrderHdByOrderHdId(Long.parseLong(
																			 * finalValueInvestigation[2].toString().
																			 * trim())); if(dgOrderhd!=null) {
																			 * dgOrderhd.setOrderStatus("C");
																			 * dgOrderHdId=dgOrderhdDao.
																			 * saveOrUpdateDgOrderHdInv(dgOrderhd); } }
																			 * }
																			 * 
																			 * 
																			 * }
																			 * 
																			 * else {
																			 * 
																			 * saveUpdateSubInvestigation(
																			 * investigationId,finalValueInvestigation,
																			 * rangeSubInvess,resultSubInvs,
																			 * subInvestigationNameIdAndInvs,
																			 * dgOrderDtIdValueSubInvess,
																			 * dgOrderHdIdSubInvess,
																			 * patientId,hospitalId,userId,dgOrderHdId,
																			 * date, UOMSubss,subChargecodeIdsForSubs,
																			 * mainChargecodeIdValsForSubs,visitId);
																			 * 
																			 * } } }
																			 * 
																			 * } counter++; }
																			 * 
																			 * 
																			 * session.flush(); session.clear();
																			 * tx.commit(); satusOfMessage=
																			 * 1+"##"+"Data uploaded successfully "; }
																			 * catch(Exception e) { if (tx != null) {
																			 * try { tx.rollback(); satusOfMessage=0+
																			 * "##"+"Data is not updated because something is wrong"
																			 * +e.toString() ; } catch(Exception re) {
																			 * satusOfMessage=0+
																			 * "##"+"Data is not updated because something is wrong"
																			 * +re.toString() ; re.printStackTrace(); }
																			 * } satusOfMessage=0+
																			 * "##"+"Data is not updated because something is wrong"
																			 * +e.toString(); e.printStackTrace(); }
																			 * finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection();
																			 * 
																			 * } return satusOfMessage; }
																			 * 
																			 * 
																			 * @Transactional public void
																			 * saveUpdateSubInvestigation(String
																			 * investigationId,String[]
																			 * finalValueInvestigation,String[]
																			 * rangeSubInvess,
																			 * String[]resultSubInvs,String[]
																			 * subInvestigationNameIdAndInvs,String[]
																			 * dgOrderDtIdValueSubInvess,String[]
																			 * dgOrderHdIdSubInvess,
																			 * 
																			 * Long patientId, String hospitalId, String
																			 * userId,Long dgOrderHdId,Date date,
																			 * String[]
																			 * UOMSubss,String[]subChargecodeIdsForSubs,
																			 * String[] mainChargecodeIdValsForSubs
																			 * ,Long visitId) { HashMap<String, String>
																			 * mapSubInvestigationMap = new HashMap<>();
																			 * String finalValue = "";
																			 * 
																			 * Integer counter=1; for (int i = 0; i <
																			 * subInvestigationNameIdAndInvs.length;
																			 * i++) { String [] invesAndSubInves=
																			 * subInvestigationNameIdAndInvs[i].split(
																			 * "@@"); if(invesAndSubInves[1]!=null &&
																			 * invesAndSubInves[1].equalsIgnoreCase(
																			 * investigationId.trim())) {
																			 * 
																			 * if
																			 * (StringUtils.isNotEmpty(invesAndSubInves[
																			 * 0].toString()) &&
																			 * !invesAndSubInves[0].equalsIgnoreCase("0"
																			 * )) { finalValue = investigationId.trim();
																			 * finalValue +="###"+
																			 * invesAndSubInves[0].trim();
																			 * 
																			 * 
																			 * 
																			 * if (i<dgOrderDtIdValueSubInvess.length &&
																			 * !dgOrderDtIdValueSubInvess[
																			 * countForOrderDtId].equalsIgnoreCase("0")
																			 * && StringUtils.isNotBlank(
																			 * dgOrderDtIdValueSubInvess[
																			 * countForOrderDtId])) { finalValue +=
																			 * "###" + dgOrderDtIdValueSubInvess[
																			 * countForOrderDtId].trim(); } else {
																			 * finalValue += "###" + "0"; }
																			 * 
																			 * if (i<dgOrderHdIdSubInvess.length &&
																			 * !dgOrderHdIdSubInvess[countForOrderHdId].
																			 * equalsIgnoreCase("0") &&
																			 * StringUtils.isNotBlank(
																			 * dgOrderHdIdSubInvess[countForOrderHdId]))
																			 * {
																			 * 
																			 * finalValue += "###" +
																			 * dgOrderHdIdSubInvess[countForOrderHdId].
																			 * trim(); } else { finalValue += "###" +
																			 * "0"; }
																			 * 
																			 * if (i < rangeSubInvess.length &&
																			 * StringUtils.isNotBlank(rangeSubInvess[
																			 * countForRange]) &&
																			 * !rangeSubInvess[countForRange].
																			 * equalsIgnoreCase("0")
																			 * &&!rangeSubInvess[countForRange].
																			 * equalsIgnoreCase("undefined") ) {
																			 * finalValue += "###" +
																			 * rangeSubInvess[countForRange].trim(); }
																			 * else { finalValue += "###" + "0"; }
																			 * if(resultSubInvs!=null &&
																			 * resultSubInvs.length>0) {
																			 * 
																			 * int tempI=i; int
																			 * tempCountForResult=countForResult; i++;
																			 * countForResult++; if (i <
																			 * resultSubInvs.length &&
																			 * StringUtils.isNotBlank(resultSubInvs[
																			 * countForResult]) &&
																			 * !resultSubInvs[countForResult].trim().
																			 * equalsIgnoreCase("0")
																			 * &&!resultSubInvs[countForResult].
																			 * equalsIgnoreCase("undefined")) {
																			 * finalValue += "###" +
																			 * resultSubInvs[countForResult].trim(); i
																			 * =tempI;
																			 * countForResult=tempCountForResult; } else
																			 * { finalValue += "###" + "0"; i =tempI;
																			 * countForResult=tempCountForResult; } }
																			 * else { finalValue += "###" + "0"; }
																			 * 
																			 * if (i < UOMSubss.length &&
																			 * StringUtils.isNotBlank(UOMSubss[
																			 * countForUmoSubInv]) &&
																			 * !UOMSubss[countForUmoSubInv].trim().
																			 * equalsIgnoreCase("0")
																			 * &&!UOMSubss[countForUmoSubInv].
																			 * equalsIgnoreCase("undefined")) {
																			 * finalValue += "###" +
																			 * UOMSubss[countForUmoSubInv].trim();
																			 * 
																			 * } else { finalValue += "###" + "0"; }
																			 * 
																			 * if (i < subChargecodeIdsForSubs.length &&
																			 * StringUtils.isNotBlank(
																			 * subChargecodeIdsForSubs[
																			 * countSubMainChargeCodeIdForSub]) &&
																			 * !subChargecodeIdsForSubs[
																			 * countSubMainChargeCodeIdForSub].trim().
																			 * equalsIgnoreCase("0")
																			 * &&!subChargecodeIdsForSubs[
																			 * countSubMainChargeCodeIdForSub].
																			 * equalsIgnoreCase("undefined")) {
																			 * finalValue += "###" +
																			 * subChargecodeIdsForSubs[
																			 * countSubMainChargeCodeIdForSub].trim();
																			 * 
																			 * } else { finalValue += "###" + "0"; }
																			 * 
																			 * if (i <
																			 * mainChargecodeIdValsForSubs.length &&
																			 * StringUtils.isNotBlank(
																			 * mainChargecodeIdValsForSubs[
																			 * countMainChargeCodeIdForSub]) &&
																			 * !mainChargecodeIdValsForSubs[
																			 * countMainChargeCodeIdForSub].trim().
																			 * equalsIgnoreCase("0")
																			 * &&!mainChargecodeIdValsForSubs[
																			 * countMainChargeCodeIdForSub].
																			 * equalsIgnoreCase("undefined")) {
																			 * finalValue += "###" +
																			 * mainChargecodeIdValsForSubs[
																			 * countMainChargeCodeIdForSub].trim();
																			 * 
																			 * } else { finalValue += "###" + "0"; }
																			 * 
																			 * 
																			 * mapSubInvestigationMap.put(
																			 * invesAndSubInves[0].trim()+"@#"+counter,
																			 * finalValue);
																			 * countForRange=countForRange+1;
																			 * countForResult=countForResult+1;
																			 * countForOrderDtId=countForOrderDtId+1;
																			 * countForOrderHdId=countForOrderHdId+1;
																			 * countForUmoSubInv=countForUmoSubInv+1;
																			 * countSubMainChargeCodeIdForSub=
																			 * countSubMainChargeCodeIdForSub+1;
																			 * countMainChargeCodeIdForSub=
																			 * countMainChargeCodeIdForSub+1; finalValue
																			 * = ""; counter++;
																			 * System.out.println("countForRange"+
																			 * countForRange+"countForResult"+
																			 * countForResult); } }
																			 * 
																			 * } counter=1; Long
																			 * dgResultEntryHdFIdM=null; for(String
																			 * subInves:subInvestigationNameIdAndInvs) {
																			 * String []
																			 * invesAndSubInves=subInves.split("@@"); if
																			 * (mapSubInvestigationMap.containsKey(
																			 * invesAndSubInves[0].trim()+"@#"+counter))
																			 * { String finalValueOfMap =
																			 * mapSubInvestigationMap.get(
																			 * invesAndSubInves[0].trim()+"@#"+counter);
																			 * 
																			 * String[] finalValueOfMaps
																			 * =finalValueOfMap.split("###"); DgOrderhd
																			 * dgOrderhd=null;
																			 * if(finalValueOfMaps[3]!="" &&
																			 * !finalValueOfMaps[3].equalsIgnoreCase("0"
																			 * )) { //dgOrderhd=dgOrderhdDao.find(Long.
																			 * parseLong(finalValueOfMaps[3].trim()));
																			 * //dgOrderHdId=dgOrderhd.getOrderhdId();
																			 * dgOrderhd=dgOrderhdDao.
																			 * getDgOrderHdByOrderHdId(Long.parseLong(
																			 * finalValueOfMaps[3].trim()));
																			 * dgOrderHdId=dgOrderhd.getOrderhdId(); }
																			 * if(dgOrderHdId==null)
																			 * if(StringUtils.isNotEmpty(hospitalId) &&
																			 * patientId!=null) {
																			 * dgOrderhd=dgOrderhdDao.
																			 * getDgOrderHdByHospitalIdAndPatient(Long.
																			 * parseLong(hospitalId),patientId);
																			 * dgOrderHdId=dgOrderhd.getOrderhdId(); }
																			 * if(dgOrderhd==null && dgOrderHdId==null)
																			 * { dgOrderhd=new DgOrderhd();
																			 * dgOrderhd.setHospitalId(Long.parseLong(
																			 * hospitalId));
																			 * dgOrderhd.setLastChgBy(Long.parseLong(
																			 * userId));
																			 * dgOrderhd.setPatientId(patientId);
																			 * dgOrderhd.setOrderDate(new Date());
																			 * dgOrderhd.setOrderStatus("C");
																			 * dgOrderhd.setVisitId(visitId);
																			 * //dgOrderhdDao.saveOrUpdate(dgOrderhd);
																			 * dgOrderhdDao.saveOrUpdateDgOrderHdInv(
																			 * dgOrderhd);
																			 * dgOrderHdId=dgOrderhd.getOrderhdId();
																			 * 
																			 * }
																			 * 
																			 * 
																			 * DgOrderdt dgOrderdt=null;
																			 * 
																			 * 
																			 * if(finalValueOfMaps[2]!="" &&
																			 * !finalValueOfMaps[2].equalsIgnoreCase("0"
																			 * )) { //dgOrderdt=dgOrderdtDao.find(Long.
																			 * parseLong(finalValueOfMaps[2].trim()));
																			 * dgOrderdt=dgOrderdtDao.
																			 * getDgOrderdtByOrderDtId(Long.parseLong(
																			 * finalValueOfMaps[2].trim())); }
																			 * if(dgOrderdt==null) {
																			 * if(finalValueOfMaps[0]!=null &&
																			 * !finalValueOfMaps[0].equalsIgnoreCase(
																			 * "0") && dgOrderHdId!=null) {
																			 * 
																			 * Criterion
																			 * cr1=Restrictions.eq("investigationId",
																			 * Long.parseLong(finalValueOfMaps[0].trim()
																			 * )); Criterion
																			 * cr2=Restrictions.eq("orderhdId",
																			 * dgOrderHdId);
																			 * List<DgOrderdt>listDgOrderdt=dgOrderdtDao
																			 * .findByCriteria(cr1,cr2);
																			 * if(CollectionUtils.isNotEmpty(
																			 * listDgOrderdt)) {
																			 * dgOrderdt=listDgOrderdt.get(0); }
																			 * dgOrderdt =dgOrderdtDao.
																			 * getDgOrderdtByInvestigationIdAndOrderhdId
																			 * (Long.parseLong(finalValueOfMaps[0].trim(
																			 * )),dgOrderHdId);
																			 * 
																			 * } } if(dgOrderdt==null) { dgOrderdt=new
																			 * DgOrderdt();
																			 * dgOrderdt.setOrderStatus("C");
																			 * dgOrderdt.setLabMark("O");
																			 * dgOrderdt.setUrgent("n");
																			 * if(finalValueOfMaps[0]!=null)
																			 * dgOrderdt.setInvestigationId(Long.
																			 * parseLong(finalValueOfMaps[0].toString())
																			 * ); if(StringUtils.isNotEmpty(userId))
																			 * dgOrderdt.setLastChgBy(Long.parseLong(
																			 * userId)); else {
																			 * dgOrderdt.setLastChgBy(null); }
																			 * dgOrderdt.setOrderhdId(dgOrderHdId);
																			 * dgOrderdt.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgOrderdt.setOrderDate(new Date()); //
																			 * saveUpdADgOrderDt(dgOrderdt);
																			 * //dgOrderdtDao.saveOrUpdate(dgOrderdt);
																			 * Long dgOrderDtId =medicalExamDAO.
																			 * saveOrUpdateDgOrderdtForDgOrderdt(
																			 * dgOrderdt);
																			 * 
																			 * } if(dgOrderdt!=null) {
																			 * dgOrderdt.setOrderStatus("C"); Long
																			 * dgOrderDtId =medicalExamDAO.
																			 * saveOrUpdateDgOrderdtForDgOrderdt(
																			 * dgOrderdt); }
																			 * 
																			 * DgResultEntryHd dgResultEntryHd=null;
																			 * 
																			 * if (finalValueInvestigation[4] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[4]) &&
																			 * !finalValueInvestigation[4].equals("0"))
																			 * dgResultEntryHd= medicalExamDAO.
																			 * getDgResultEntryHdByPatientIdAndHospitalId
																			 * (Long.parseLong(finalValueInvestigation[4
																			 * ].trim().toString())) ;
																			 * 
																			 * if(dgResultEntryHd==null) {
																			 * 
																			 * if(patientId!=null &&
																			 * StringUtils.isNotEmpty(hospitalId)) {
																			 * dgResultEntryHd= medicalExamDAO.
																			 * getDgResultEntryHdByPatientIdAndHospitalIdAndInves
																			 * (patientId,Long.parseLong(hospitalId),
																			 * Long.parseLong(finalValueOfMaps[0].
																			 * toString())) ;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * if(StringUtils.isNotEmpty(
																			 * finalValueOfMaps[7]) &&
																			 * !finalValueOfMaps[7].equals("0") &&
																			 * StringUtils.isNotEmpty(finalValueOfMaps[8
																			 * ]) && !finalValueOfMaps[8].equals("0") &&
																			 * dgOrderHdId!=null) {
																			 * 
																			 * Criterion
																			 * cr1=Restrictions.eq("subChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[9]
																			 * )); Criterion
																			 * cr2=Restrictions.eq("mainChargecodeId",
																			 * Long.parseLong(finalValueInvestigation[10
																			 * ])); Criterion
																			 * cr3=Restrictions.eq("orderHdId",
																			 * dgOrderHdId);
																			 * List<DgResultEntryHd>listDgResultEntryHd=
																			 * dgResultEntryHdDao.findByCriteria(cr1,cr2
																			 * ,cr3); if(CollectionUtils.isNotEmpty(
																			 * listDgResultEntryHd))
																			 * dgResultEntryHd=listDgResultEntryHd.get(0
																			 * ); dgResultEntryHd =dgResultEntryHdDao.
																			 * getDgResultEntryHdByInvestigationIdAndOrderhdId
																			 * (Long.parseLong(finalValueOfMaps[7]),Long
																			 * .parseLong(finalValueOfMaps[8])
																			 * ,dgOrderHdId); }
																			 * 
																			 * 
																			 * if(dgResultEntryHd!=null) {
																			 * dgResultEntryHdFIdM=dgResultEntryHd.
																			 * getResultEntryId(); } }
																			 * 
																			 * if(dgResultEntryHd!=null) {
																			 * dgResultEntryHdFIdM=dgResultEntryHd.
																			 * getResultEntryId(); }
																			 * 
																			 * 
																			 * if(dgResultEntryHdFIdM==null) {
																			 * dgResultEntryHd =new DgResultEntryHd();
																			 * dgResultEntryHd.setHospitalId(Long.
																			 * parseLong(hospitalId));
																			 * //dgResultEntryHd.setVisitId(visitId);
																			 * dgResultEntryHd.setOrderHdId(dgOrderHdId)
																			 * ;
																			 * //dgResultEntryHd.setInvestigationId(Long
																			 * .parseLong(finalValueInvestigation[0].
																			 * toString()));
																			 * dgResultEntryHd.setPatientId(patientId);
																			 * dgResultEntryHd.setLastChgBy(Long.
																			 * parseLong(userId));
																			 * dgResultEntryHd.setLastChgDate(new
																			 * Timestamp(date.getTime()));
																			 * dgResultEntryHd.setResultDate(new
																			 * Date());
																			 * dgResultEntryHd.setResultStatus("C");
																			 * dgResultEntryHd.setCreatedBy(Long.
																			 * parseLong(userId));
																			 * dgResultEntryHd.setVerifiedOn(new
																			 * Date());
																			 * dgResultEntryHd.setVerified("V");
																			 * dgResultEntryHd.setResultVerifiedBy(Long.
																			 * parseLong(userId));
																			 * 
																			 * if(StringUtils.isNotEmpty(
																			 * finalValueOfMaps[7]) &&
																			 * !finalValueOfMaps[7].equals("0") )
																			 * dgResultEntryHd.setSubChargecodeId(Long.
																			 * parseLong(finalValueOfMaps[7]));
																			 * 
																			 * if(StringUtils.isNotEmpty(
																			 * finalValueOfMaps[8]) &&
																			 * !finalValueOfMaps[8].equals("0") )
																			 * dgResultEntryHd.setMainChargecodeId(Long.
																			 * parseLong(finalValueOfMaps[8]));
																			 * dgResultEntryHd.setVisitId(visitId);
																			 * 
																			 * dgResultEntryHdDao.saveOrUpdate(
																			 * dgResultEntryHd);
																			 * dgResultEntryHdFIdM=dgResultEntryHd.
																			 * getResultEntryId();
																			 * 
																			 * dgResultEntryHdFIdM=medicalExamDAO.
																			 * saveOrUpdateDgResultEntryHd(
																			 * dgResultEntryHd) ;
																			 * 
																			 * }
																			 * 
																			 * DgResultEntryDt dgResultEntryDt=new
																			 * DgResultEntryDt();
																			 * dgResultEntryDt.setInvestigationId(Long.
																			 * parseLong(finalValueOfMaps[0].toString())
																			 * );
																			 * 
																			 * if (finalValueOfMaps[5] != null &&
																			 * StringUtils.isNotBlank(finalValueOfMaps[5
																			 * ]) && !finalValueOfMaps[5].equals("0")) {
																			 * //String result =
																			 * finalValueOfMaps[5].toString().substring(
																			 * 0,
																			 * finalValueOfMaps[5].toString().length() -
																			 * 1); //dgResultEntryDt.setResult(result);
																			 * //dgResultEntryDt.setResult(
																			 * finalValueOfMaps[5].toString());
																			 * 
																			 * 
																			 * int
																			 * index=finalValueOfMaps[5].lastIndexOf(","
																			 * ); if(index==-1) {
																			 * dgResultEntryDt.setResult(
																			 * finalValueOfMaps[5].toString()); } else {
																			 * String result =
																			 * finalValueOfMaps[5].toString().substring(
																			 * 0,
																			 * finalValueOfMaps[5].toString().length() -
																			 * 1); dgResultEntryDt.setResult(result); }
																			 * 
																			 * 
																			 * Character charVal=null; String result="";
																			 * charVal = finalValueOfMaps[5].charAt(
																			 * finalValueOfMaps[5].length() - 1);
																			 * 
																			 * if(charVal!=null && charVal.equals(','))
																			 * { result =
																			 * finalValueOfMaps[5].toString().substring(
																			 * 0,
																			 * finalValueOfMaps[5].toString().length() -
																			 * 1); dgResultEntryDt.setResult(
																			 * MedicalExamDAOImpl.getHtmlText(result));
																			 * } else { dgResultEntryDt.setResult(
																			 * MedicalExamDAOImpl.getHtmlText(
																			 * finalValueOfMaps[5].toString())); }
																			 * 
																			 * 
																			 * 
																			 * }
																			 * 
																			 * 
																			 * if (finalValueOfMaps[4] != null &&
																			 * StringUtils.isNotBlank(finalValueOfMaps[4
																			 * ]) && !finalValueOfMaps[4].equals("0"))
																			 * dgResultEntryDt.setRangeValue(
																			 * finalValueOfMaps[4].toString());
																			 * 
																			 * if (finalValueOfMaps[1] != null &&
																			 * StringUtils.isNotBlank(finalValueOfMaps[1
																			 * ]) && !finalValueOfMaps[1].equals("0"))
																			 * dgResultEntryDt.setSubInvestigationId(
																			 * Long.parseLong(finalValueOfMaps[1].
																			 * toString()));
																			 * 
																			 * 
																			 * if (finalValueInvestigation[7] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[7]) &&
																			 * !finalValueInvestigation[7].equals("0"))
																			 * dgResultEntryDt.setRidcId(Long.parseLong(
																			 * finalValueInvestigation[7].trim().
																			 * toString()));
																			 * 
																			 * if (finalValueInvestigation[8] != null &&
																			 * StringUtils.isNotBlank(
																			 * finalValueInvestigation[8]) &&
																			 * !finalValueInvestigation[8].equals("0"))
																			 * dgResultEntryDt.setResultType(
																			 * finalValueInvestigation[8].trim().
																			 * toString());
																			 * 
																			 * 
																			 * 
																			 * dgResultEntryDt.setOrderDtId(dgOrderdt.
																			 * getOrderdtId());
																			 * dgResultEntryDt.setResultEntryId(
																			 * dgResultEntryHdFIdM);
																			 * dgResultEntryDt.setResultDetailStatus("C"
																			 * ); dgResultEntryDt.setValidated("V");
																			 * 
																			 * //dgResultEntryDtDao.saveOrUpdate(
																			 * dgResultEntryDt); medicalExamDAO.
																			 * saveOrUpdateDgResultEntryDt(
																			 * dgResultEntryDt);
																			 * 
																			 * 
																			 * if(dgOrderHdId!=null) {
																			 * 
																			 * if(finalValueOfMaps!=null &&
																			 * finalValueOfMaps[3]!=null &&
																			 * !finalValueOfMaps[3].equalsIgnoreCase("0"
																			 * )) { List<DgOrderdt>
																			 * listDgOrderdt=dgOrderhdDao.
																			 * getDgOrderDtByOrderHdId(Long.parseLong(
																			 * finalValueOfMaps[3].trim().toString()));
																			 * if(CollectionUtils.isEmpty(listDgOrderdt)
																			 * ) { dgOrderhd=dgOrderhdDao.
																			 * getDgOrderHdByOrderHdId(Long.parseLong(
																			 * finalValueOfMaps[3].toString().trim()));
																			 * if(dgOrderhd!=null) {
																			 * dgOrderhd.setOrderStatus("C");
																			 * dgOrderHdId=dgOrderhdDao.
																			 * saveOrUpdateDgOrderHdInv(dgOrderhd); } }
																			 * } }
																			 * 
																			 * counter++; }
																			 * 
																			 * } }
																			 * 
																			 * 
																			 * @Override public List<MasAppointmentType>
																			 * findMasAppointmentTypeByAppCode() {
																			 * Criterion cr1=Restrictions.eq(
																			 * "appointmentTypeCode",
																			 * "OPD").ignoreCase(); return
																			 * masAppointmentTypeDao.findByCriteria(cr1)
																			 * ; }
																			 * 
																			 * @Override public long
																			 * savePatientFromUploadDocument(JSONObject
																			 * jsonObj) { String uhidNO=""; String
																			 * serviceNo=""; long patientRelationId=0;
																			 * long registrationTypeId=0; long
																			 * patientId=0;
																			 * 
																			 * String
																			 * serviceTypeCodeIcg=HMSUtil.getProperties(
																			 * "adt.properties",
																			 * "SERVICE_TYPE_CODE_ICG"); long
																			 * serviceTypeIdIcg =
																			 * getServiceTypeIdFromCode(
																			 * serviceTypeCodeIcg);
																			 * 
																			 * 
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx =
																			 * session.beginTransaction();
																			 * 
																			 * try { Patient patient=new Patient();
																			 * 
																			 * if(jsonObj.getJSONArray("serviceNo")!=
																			 * null) { serviceNo =
																			 * jsonObj.getJSONArray("serviceNo").
																			 * getString(0);
																			 * patient.setServiceNo(serviceNo); }
																			 * 
																			 * if(jsonObj.getJSONArray("empName")!=null)
																			 * { String empName =
																			 * jsonObj.getJSONArray("empName").getString
																			 * (0); patient.setEmployeeName(empName); }
																			 * 
																			 * if(jsonObj.getJSONArray("employeeId")!=
																			 * null &&
																			 * jsonObj.getJSONArray("employeeId").
																			 * getLong(0)!=0) { Long empId =
																			 * jsonObj.getJSONArray("employeeId").
																			 * getLong(0);
																			 * //patient.setEmployeeId(empId); }
																			 * 
																			 * if(jsonObj.getJSONArray("empRankId")!=
																			 * null &&
																			 * jsonObj.getJSONArray("empRankId").getLong
																			 * (0)!=0) { Long
																			 * rankId=jsonObj.getJSONArray("empRankId").
																			 * getLong(0); patient.setRankId(rankId); }
																			 * 
																			 * if(jsonObj.getJSONArray("empTradeId")!=
																			 * null &&
																			 * jsonObj.getJSONArray("empTradeId").
																			 * getLong(0)!=0) { Long tradeId=
																			 * jsonObj.getJSONArray("empTradeId").
																			 * getLong(0); patient.setTradeId(tradeId);
																			 * }
																			 * 
																			 * if(jsonObj.getJSONArray("empUnitId")!=
																			 * null &&
																			 * jsonObj.getJSONArray("empUnitId").getLong
																			 * (0)!=0) { Long
																			 * unitId=jsonObj.getJSONArray("empUnitId").
																			 * getLong(0); patient.setUnitId(unitId); }
																			 * 
																			 * if(jsonObj.getJSONArray(
																			 * "empMaritalStatusId")!=null &&
																			 * jsonObj.getJSONArray("empMaritalStatusId"
																			 * ).getLong(0)!=0) { Long maritalstarusId =
																			 * jsonObj.getJSONArray("empMaritalStatusId"
																			 * ).getLong(0); patient.setMaritalStatusId(
																			 * maritalstarusId); }
																			 * 
																			 * if(jsonObj.getJSONArray(
																			 * "empRecordOfficeId")!=null &&
																			 * jsonObj.getJSONArray("empRecordOfficeId")
																			 * .getLong(0)!=0) { Long
																			 * recordofficeId=jsonObj.getJSONArray(
																			 * "empRecordOfficeId").getLong(0);
																			 * patient.setRecordOfficeAddressId(
																			 * recordofficeId); }
																			 * 
																			 * if(jsonObj.getJSONArray(
																			 * "empServiceJoinDate")!=null) { String
																			 * empServiceJoinDate=jsonObj.getJSONArray(
																			 * "empServiceJoinDate").getString(0); try {
																			 * patient.setServiceJoinDate(HMSUtil.
																			 * convertStringDateToUtilDate(
																			 * empServiceJoinDate, "dd/MM/yyyy")); }
																			 * catch (Exception e) { // TODO
																			 * Auto-generated catch block
																			 * e.printStackTrace(); } }
																			 * 
																			 * if(jsonObj.getJSONArray("name")!=null) {
																			 * String patientName =
																			 * jsonObj.getJSONArray("name").getString(0)
																			 * ; patient.setPatientName(patientName); }
																			 * 
																			 * if(jsonObj.getJSONArray("dateOfBirth")!=
																			 * null) { String patientDOB =
																			 * jsonObj.getJSONArray("dateOfBirth").
																			 * getString(0); if(!patientDOB.isEmpty()) {
																			 * try { patient.setDateOfBirth(HMSUtil.
																			 * convertStringDateToUtilDate(patientDOB,
																			 * "dd/MM/yyyy")); } catch (Exception e) {
																			 * // TODO Auto-generated catch block
																			 * e.printStackTrace(); } } }
																			 * 
																			 * 
																			 * if(jsonObj.getJSONArray("genderId")!=null
																			 * &&
																			 * jsonObj.getJSONArray("genderId").getLong(
																			 * 0)!=0) { Long
																			 * patientGenderId=jsonObj.getJSONArray(
																			 * "genderId").getLong(0);
																			 * patient.setAdministrativeSexId(
																			 * patientGenderId); }
																			 * 
																			 * if(jsonObj.getJSONArray("relationId")!=
																			 * null &&
																			 * jsonObj.getJSONArray("relationId").
																			 * getLong(0)!=0) {
																			 * patientRelationId=jsonObj.getJSONArray(
																			 * "relationId").getLong(0);
																			 * patient.setRelationId(patientRelationId);
																			 * }
																			 * 
																			 * if(jsonObj.getJSONArray(
																			 * "registrationTypeId")!=null &&
																			 * jsonObj.getJSONArray("registrationTypeId"
																			 * ).getLong(0)!=0) {
																			 * registrationTypeId=jsonObj.getJSONArray(
																			 * "registrationTypeId").getLong(0);
																			 * patient.setRegistrationTypeId(
																			 * registrationTypeId); }
																			 * 
																			 * patient.setServiceStatusId(
																			 * serviceTypeIdIcg);
																			 * 
																			 * String uhidNo =
																			 * jsonObj.getJSONArray("uhidNo").getString(
																			 * 0); if(!uhidNo.isEmpty()) { // no need to
																			 * save patient }else {
																			 * session.save(patient); tx.commit();
																			 * patientId = patient.getPatientId(); }
																			 * }catch(Exception e) { tx.rollback();
																			 * patientId=0;
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace(); }
																			 * 
																			 * return patientId;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @Override public long
																			 * savePatientForEmergency(JSONObject
																			 * jsonObj) { String uhidNO=""; String
																			 * serviceNo=""; long patientRelationId=0;
																			 * long registrationTypeId=0; long
																			 * patientId=0; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); Transaction tx =
																			 * session.beginTransaction(); try { Patient
																			 * patient=new Patient();
																			 * 
																			 * try { String
																			 * serviceTypeCodeIcg=HMSUtil.getProperties(
																			 * "adt.properties",
																			 * "SERVICE_TYPE_CODE_ICG"); long
																			 * serviceTypeIdIcg =
																			 * getServiceTypeIdFromCode(
																			 * serviceTypeCodeIcg);
																			 * patient.setServiceStatusId(
																			 * serviceTypeIdIcg); } catch(Exception e) {
																			 * e.printStackTrace(); }
																			 * if(jsonObj.get("serviceNo")!=null) {
																			 * serviceNo =
																			 * (String.valueOf(jsonObj.get("serviceNo"))
																			 * ); patient.setServiceNo(serviceNo); }
																			 * 
																			 * if(jsonObj.get("empName")!=null) { String
																			 * empName =
																			 * (String.valueOf(jsonObj.get("empName")));
																			 * patient.setEmployeeName(empName); }
																			 * 
																			 * if(jsonObj.get("employeeId")!=null &&
																			 * !jsonObj.get("employeeId").equals("") &&
																			 * !jsonObj.get("employeeId").equals("0")) {
																			 * Long empId =
																			 * (Long.parseLong(String.valueOf(jsonObj.
																			 * get("employeeId"))));
																			 * //patient.setEmployeeId(empId); }
																			 * 
																			 * if(jsonObj.get("empRankId")!=null &&
																			 * !jsonObj.get("empRankId").equals("") &&
																			 * !jsonObj.get("empRankId").equals("0")) {
																			 * Long
																			 * rankId=(Long.parseLong(String.valueOf(
																			 * jsonObj.get("empRankId"))));
																			 * patient.setRankId(rankId); }
																			 * 
																			 * if(jsonObj.get("empTradeId")!=null &&
																			 * !jsonObj.get("empTradeId").equals("")&&
																			 * !jsonObj.get("empTradeId").equals("0")) {
																			 * Long tradeId=
																			 * (Long.parseLong(String.valueOf(jsonObj.
																			 * get("empTradeId"))));
																			 * patient.setTradeId(tradeId); }
																			 * 
																			 * if(jsonObj.get("empUnitId")!=null &&
																			 * !jsonObj.get("empUnitId").equals("")
																			 * &&!jsonObj.get("empUnitId").equals("0"))
																			 * { Long
																			 * unitId=(Long.parseLong(String.valueOf(
																			 * jsonObj.get("empUnitId"))));
																			 * patient.setUnitId(unitId); }
																			 * 
																			 * if(jsonObj.get("empMaritalStatusId")!=
																			 * null &&
																			 * !jsonObj.get("empMaritalStatusId").equals
																			 * ("")&&!jsonObj.get("empMaritalStatusId").
																			 * equals("0")) { Long maritalstarusId =
																			 * (Long.parseLong(String.valueOf(jsonObj.
																			 * get("empMaritalStatusId"))));
																			 * patient.setMaritalStatusId(
																			 * maritalstarusId); }
																			 * 
																			 * if(jsonObj.get("empRecordOfficeId")!=null
																			 * &&
																			 * !jsonObj.get("empRecordOfficeId").equals(
																			 * "") &&!jsonObj.get("empRecordOfficeId").
																			 * equals("0")) { Long
																			 * recordofficeId=(Long.parseLong(String.
																			 * valueOf(jsonObj.get("empRecordOfficeId"))
																			 * )); patient.setRecordOfficeAddressId(
																			 * recordofficeId); }
																			 * 
																			 * if(jsonObj.get("empServiceJoinDate")!=
																			 * null &&
																			 * !jsonObj.get("empServiceJoinDate").equals
																			 * ("")) { String
																			 * empServiceJoinDate=(String.valueOf(
																			 * jsonObj.get("empServiceJoinDate"))); try
																			 * { patient.setServiceJoinDate(HMSUtil.
																			 * convertStringDateToUtilDate(
																			 * empServiceJoinDate, "dd/MM/yyyy")); }
																			 * catch (Exception e) { // TODO
																			 * Auto-generated catch block
																			 * e.printStackTrace(); } }
																			 * 
																			 * if(jsonObj.get("name")!=null &&
																			 * !jsonObj.get("name").equals("")) { String
																			 * patientName =
																			 * (String.valueOf(jsonObj.get("name")));
																			 * patient.setPatientName(patientName); }
																			 * 
																			 * if(jsonObj.get("dateOfBirth")!=null &&
																			 * !jsonObj.get("dateOfBirth").equals("")) {
																			 * String patientDOB =
																			 * (String.valueOf(jsonObj.get("dateOfBirth"
																			 * ))); try {
																			 * patient.setDateOfBirth(HMSUtil.
																			 * convertStringDateToUtilDate(patientDOB,
																			 * "dd/MM/yyyy")); } catch (Exception e) {
																			 * // TODO Auto-generated catch block
																			 * e.printStackTrace(); } }
																			 * 
																			 * 
																			 * if(jsonObj.get("genderId")!=null &&
																			 * !jsonObj.get("genderId").equals("")&&!
																			 * jsonObj.get("genderId").equals("0")) {
																			 * Long
																			 * patientGenderId=(Long.parseLong(String.
																			 * valueOf(jsonObj.get("genderId"))));
																			 * patient.setAdministrativeSexId(
																			 * patientGenderId); }
																			 * 
																			 * if(jsonObj.get("relationId")!=null &&
																			 * !jsonObj.get("relationId").equals("")&&!
																			 * jsonObj.get("relationId").equals("0")) {
																			 * patientRelationId=(Long.parseLong(String.
																			 * valueOf(jsonObj.get("relationId"))));
																			 * patient.setRelationId(patientRelationId);
																			 * }
																			 * 
																			 * if(jsonObj.get("registrationTypeId")!=
																			 * null &&
																			 * !jsonObj.get("registrationTypeId").equals
																			 * ("")&&!jsonObj.get("registrationTypeId").
																			 * equals("0")) {
																			 * registrationTypeId=(Long.parseLong(String
																			 * .valueOf(jsonObj.get("registrationTypeId"
																			 * )))); patient.setRegistrationTypeId(
																			 * registrationTypeId); }
																			 * 
																			 * uhidNO = jsonObj.getString("uhidNo");
																			 * 
																			 * if(!uhidNO.isEmpty()) { // no need to
																			 * save patient }else {
																			 * session.save(patient); tx.commit();
																			 * patientId = patient.getPatientId(); }
																			 * }catch(Exception e) { tx.rollback();
																			 * patientId=0;
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace(); }
																			 * 
																			 * return patientId;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasDepartment>
																			 * getDepartmentListFromAppointmentSession(
																			 * long hospitalId) { // TODO Auto-generated
																			 * method stub List<MasDepartment>
																			 * hospitalwiseDepartmentList = new
																			 * ArrayList<MasDepartment>();
																			 * 
																			 * Session session=null;
																			 * 
																			 * try { session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * hospitalwiseDepartmentList=session.
																			 * createCriteria(MasAppointmentSession.
																			 * class)
																			 * .add(Restrictions.eq("Status","y").
																			 * ignoreCase())
																			 * .createAlias("masDepartment", "md")
																			 * .add(Restrictions.eq("md.status","y").
																			 * ignoreCase()) .createAlias("masHospital",
																			 * "hospital") .add(Restrictions.eq(
																			 * "hospital.hospitalId", hospitalId))
																			 * .setProjection(Projections.distinct(
																			 * Projections.property("masDepartment")))
																			 * .addOrder(Order.asc("masDepartment"))
																			 * .list();
																			 * 
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return
																			 * hospitalwiseDepartmentList; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasRank>
																			 * getMasRankList() { List<MasRank> rankList
																			 * = new ArrayList<MasRank>(); try { Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); rankList =
																			 * session.createCriteria(MasRank.class)
																			 * .addOrder(Order.asc("rankName")).list();
																			 * }catch(Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return rankList;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasUnit>
																			 * getMasUnitList() { List<MasUnit> unitList
																			 * = new ArrayList<MasUnit>(); try { Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); unitList =
																			 * session.createCriteria(MasUnit.class)
																			 * .addOrder(Order.asc("unitName")).list();
																			 * }catch(Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return unitList; }
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasTrade>
																			 * getMasTradeList() { List<MasTrade>
																			 * tradeList = new ArrayList<MasTrade>();
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); tradeList =
																			 * session.createCriteria(MasTrade.class)
																			 * .addOrder(Order.asc("tradeName")).list();
																			 * }catch(Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return tradeList; }
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasCommand>
																			 * getMasCommandList() { List<MasCommand>
																			 * commandList = new
																			 * ArrayList<MasCommand>(); try { Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); commandList =
																			 * session.createCriteria(MasCommand.class)
																			 * .addOrder(Order.asc("commandName")).list(
																			 * ); }catch(Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return commandList;
																			 * }
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasMaritalStatus>
																			 * getMasMaritalStatusList() {
																			 * List<MasMaritalStatus> mStatusList = new
																			 * ArrayList<MasMaritalStatus>(); try {
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); mStatusList =
																			 * session.createCriteria(MasMaritalStatus.
																			 * class)
																			 * .addOrder(Order.asc("maritalStatusName"))
																			 * .list(); }catch(Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return mStatusList;
																			 * }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public
																			 * List<MasRecordOfficeAddress>
																			 * getMasRecordOfficeList() {
																			 * List<MasRecordOfficeAddress> officeList =
																			 * new ArrayList<MasRecordOfficeAddress>();
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); officeList =
																			 * session.createCriteria(
																			 * MasRecordOfficeAddress.class)
																			 * .addOrder(Order.asc(
																			 * "recordOfficeAddressName")).list();
																			 * }catch(Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return officeList; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public Map<String, Object>
																			 * opdEmergencySavePatient(JSONObject
																			 * jObject) { Map<String,Object> map = new
																			 * HashMap<String, Object>(); long
																			 * visitId=0; long patientId=0;
																			 * 
																			 * String
																			 * uhidNO=jObject.getString("uhidNo");
																			 * 
																			 * if(!uhidNO.isEmpty()) { patientId =
																			 * getPatientFromUhidNo(uhidNO); visitId =
																			 * createOpdEmergency(jObject,patientId);
																			 * }else { patientId =
																			 * savePatientForEmergency(jObject); visitId
																			 * = createOpdEmergency(jObject,patientId);
																			 * }
																			 * 
																			 * map.put("patientId", patientId);
																			 * map.put("visitId", visitId);
																			 * 
																			 * return map;
																			 * 
																			 * }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public long
																			 * createOpdEmergency(JSONObject
																			 * jObject,long patientId) { long visitId=0;
																			 * long departmentId=
																			 * Long.parseLong(jObject.getString(
																			 * "departmentId")); long priorityId =
																			 * Long.parseLong(jObject.getString(
																			 * "priorityId")); long hospitalId
																			 * =jObject.getLong("hospitalId"); String
																			 * visitFlag="R"; Long
																			 * masappointTypeId=null; Session session =
																			 * null; Transaction tx=null;
																			 * if(session==null) {
																			 * session=getHibernateUtils.
																			 * getHibernateUtlis().OpenSession(); }
																			 * if(tx==null) { tx =
																			 * session.beginTransaction(); } Date
																			 * visitDate =new Date();
																			 * 
																			 * Visit visit = new Visit();
																			 * 
																			 * visit.setVisitDate(new
																			 * Timestamp(visitDate.getTime()));
																			 * visit.setLastChgDate(new
																			 * Timestamp(visitDate.getTime()));
																			 * 
																			 * visit.setPriority(priorityId);
																			 * visit.setVisitStatus("w");
																			 * 
																			 * if (jObject.getString("visitFlag")!="" )
																			 * { visitFlag =
																			 * jObject.getString("visitFlag");
																			 * visit.setVisitFlag(visitFlag); }
																			 * 
																			 * if(jObject.has("examTypeId") &&
																			 * jObject.getString("examTypeId")!=null &&
																			 * !jObject.getString("examTypeId").equals(
																			 * "")) { String[]
																			 * examTypeIds=jObject.getString(
																			 * "examTypeId").split("@@");
																			 * if(examTypeIds!=null &&
																			 * examTypeIds.length>0)
																			 * visit.setExamId(Long.parseLong(
																			 * examTypeIds[0])); //examTypeIds[2]; }
																			 * if(jObject.has("documentType") &&
																			 * jObject.getString("documentType")!=null
																			 * &&
																			 * !jObject.getString("documentType").equals
																			 * ("")) { String
																			 * appoinmentTypeCode=jObject.getString(
																			 * "documentType");
																			 * if(StringUtils.isNotEmpty(
																			 * appoinmentTypeCode)) { Criterion
																			 * cr1=Restrictions.eq(
																			 * "appointmentTypeCode",
																			 * appoinmentTypeCode.toString());
																			 * List<MasAppointmentType>
																			 * listMasAppointmentType
																			 * =masAppointmentTypeDao.findByCriteria(cr1
																			 * ); if(CollectionUtils.isNotEmpty(
																			 * listMasAppointmentType)) {
																			 * masappointTypeId=listMasAppointmentType.
																			 * get(0).getAppointmentTypeId();
																			 * visit.setAppointmentTypeId(
																			 * masappointTypeId); } } }
																			 * 
																			 * visit.setHospitalId(hospitalId);
																			 * //visit.setDepartmentId(departmentId);
																			 * visit.setPatientId(patientId);
																			 * 
																			 * //long appointmentSessionId =
																			 * getAppointmentSessionId(hospitalId,
																			 * departmentId,masappointTypeId);
																			 * 
																			 * //visit.setSessionId(appointmentSessionId
																			 * ); session.save(visit); tx.commit();
																			 * visitId = visit.getVisitId();
																			 * session.close(); return visitId; }
																			 * 
																			 * @Override public Map<String, Object>
																			 * getLabData(JSONObject json) { // TODO
																			 * Auto-generated method stub Map<String,
																			 * Object> map = new HashMap<String,
																			 * Object>(); int pageSize =
																			 * Integer.parseInt(HMSUtil.getProperties(
																			 * "adt.properties", "pageSize").trim());
																			 * String mainChargeCode= ""; List<Object[]>
																			 * listObject=null; List totalMatches = new
																			 * ArrayList(); int pageNo=0; if
																			 * (json.get("PN") != null) pageNo =
																			 * Integer.parseInt(json.get("PN").toString(
																			 * ));
																			 * 
																			 * 
																			 * String serviceNo=""; String name=""; Date
																			 * orderStartDate=null; Date
																			 * orderEndDate=null; Date
																			 * finalorderEndDate=null; long relation=0;
																			 * String startDate=""; String endDate="";
																			 * 
																			 * if (json.get("flag") != null) { String
																			 * flag = json.get("flag").toString();
																			 * if(flag.equalsIgnoreCase("l"))
																			 * mainChargeCode=
																			 * HMSUtil.getProperties("adt.properties",
																			 * "MAIN_CHARGE_CODE_LAB"); else
																			 * mainChargeCode=
																			 * HMSUtil.getProperties("adt.properties",
																			 * "MAIN_CHARGE_CODE_RADIO");
																			 * 
																			 * } if (json.get("serviceNo") != null)
																			 * serviceNo =
																			 * json.get("serviceNo").toString();
																			 * 
																			 * if(json.has("name")) name =
																			 * json.getString("name");
																			 * if(json.has("relation")) relation =
																			 * Long.parseLong(json.get("relation").
																			 * toString()); ///start
																			 * if(json.has("fromDate")) { startDate =
																			 * json.getString("fromDate"); endDate =
																			 * json.getString("toDate");
																			 * if((!startDate.isEmpty() &&
																			 * startDate!=null) && (!endDate.isEmpty()
																			 * && endDate!=null)) { orderStartDate =
																			 * HMSUtil.convertStringDateToUtilDate(
																			 * startDate, "dd/MM/yyyy"); orderEndDate =
																			 * HMSUtil.convertStringDateToUtilDate(
																			 * endDate, "dd/MM/yyyy");
																			 * 
																			 * Calendar cal = Calendar.getInstance();
																			 * cal.setTime(orderEndDate);
																			 * cal.set(Calendar.HOUR_OF_DAY, 0);
																			 * cal.set(Calendar.MINUTE, 59);
																			 * cal.set(Calendar.SECOND, 0);
																			 * cal.set(Calendar.MILLISECOND, 0); //Date
																			 * from = cal.getTime();
																			 * cal.set(Calendar.HOUR_OF_DAY, 23);
																			 * finalorderEndDate = cal.getTime();
																			 * 
																			 * } }
																			 * 
																			 * 
																			 * Criteria cr=null; Criterion
																			 * criterion=null; Criterion crrr=null;
																			 * Criterion crrr1=null; Criterion
																			 * crvisit=null; try {
																			 * 
																			 * Long mainChargeId=null;
																			 * if(mainChargeCode!=null) {
																			 * MasMainChargecode mmcc=null; mmcc =
																			 * opdMasterDaoImpl.getMainChargeCode(
																			 * mainChargeCode);
																			 * mainChargeId=mmcc.getMainChargecodeId();
																			 * System.out.println("mainChargeId :"
																			 * +mainChargeId);
																			 * 
																			 * }
																			 * 
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); //cr =
																			 * session.createCriteria(DgOrderdt.class,
																			 * "dgOrderdt") cr =
																			 * session.createCriteria(DgResultEntryHd.
																			 * class,"dgResultEntryHd") .createAlias(
																			 * "dgResultEntryHd.dgResultEntryDt",
																			 * "dgResultEntryDt") cr =
																			 * session.createCriteria(DgResultEntryDt.
																			 * class,"dgResultEntryDt") .createAlias(
																			 * "dgResultEntryDt.dgResultEntryHd",
																			 * "dgResultEntryHd")
																			 * .createAlias("dgResultEntryHd.dgOrderhd",
																			 * "dgOrderHd")
																			 * 
																			 * .createAlias("dgResultEntryDt.dgOrderdt",
																			 * "dgOrderdt")
																			 * 
																			 * .createAlias("dgOrderHd.masHospital",
																			 * "masHospital",JoinType.LEFT_OUTER_JOIN)
																			 * 
																			 * .createAlias(
																			 * "dgResultEntryDt.dgMasInvestigation",
																			 * "dgMasInvestigation",JoinType.
																			 * LEFT_OUTER_JOIN) .createAlias(
																			 * "dgResultEntryDt.dgSubMasInvestigation",
																			 * "dgSubMasInvestigation",JoinType.
																			 * LEFT_OUTER_JOIN) //.createAlias(
																			 * "dgSubMasInvestigation.dgNormalValue",
																			 * "dgNormalValue",JoinType.LEFT_OUTER_JOIN)
																			 * .createAlias("dgMasInvestigation.masUOM",
																			 * "masUOM",JoinType.LEFT_OUTER_JOIN)
																			 * .createAlias(
																			 * "dgSubMasInvestigation.masUOM",
																			 * "subMasUOM",JoinType.LEFT_OUTER_JOIN)
																			 * //.createAlias("dgResultEntryDt.masUOM",
																			 * "masUOM",JoinType.LEFT_OUTER_JOIN)
																			 * .createAlias("dgOrderHd.patient",
																			 * "patient") .createAlias(
																			 * "patient.masAdministrativeSex",
																			 * "masAdministrativeSex",JoinType.
																			 * LEFT_OUTER_JOIN)
																			 * .createAlias("patient.masRelation",
																			 * "masRelation",JoinType.LEFT_OUTER_JOIN)
																			 * 
																			 * 
																			 * .add(Restrictions.eq(
																			 * "dgMasInvestigation.mainChargecodeId",
																			 * mainChargeId))
																			 * .add(Restrictions.eq("patient.serviceNo",
																			 * serviceNo)) .add(Restrictions.isNotNull(
																			 * "dgResultEntryDt.result"))
																			 * .add(Restrictions.eq(
																			 * "dgResultEntryDt.resultDetailStatus","C")
																			 * .ignoreCase() ) .add(Restrictions.eq(
																			 * "dgResultEntryDt.validated","V").
																			 * ignoreCase()) .addOrder(Order.desc(
																			 * "dgResultEntryHd.resultDate"));
																			 * 
																			 * 
																			 * 
																			 * if(!startDate.equalsIgnoreCase("") &&
																			 * !endDate.equalsIgnoreCase("")) {
																			 * crrr=Restrictions.ge(
																			 * "dgResultEntryHd.resultDate",
																			 * orderStartDate); crrr1=Restrictions.le(
																			 * "dgResultEntryHd.resultDate",
																			 * finalorderEndDate);
																			 * cr.add(crrr).add(crrr1); }
																			 * 
																			 * 
																			 * if(!name.isEmpty() && name != null) {
																			 * cr.add(Restrictions.ilike(
																			 * "patient.patientName", "%"
																			 * +name.trim()+"%",MatchMode.ANYWHERE)); }
																			 * if(relation !=0) {
																			 * cr.add(Restrictions.eq(
																			 * "patient.relationId",relation)); }
																			 * ProjectionList projectionList =
																			 * Projections.projectionList();
																			 * projectionList.add(Projections.property(
																			 * "dgResultEntryDt.resultEntryDetailId").as
																			 * ("resultEntryDetailId"));
																			 * projectionList.add(Projections.property(
																			 * "dgResultEntryHd.resultEntryId").as(
																			 * "resultEntryId"));
																			 * projectionList.add(Projections.property(
																			 * "patient.patientName").as("patientName"))
																			 * ;
																			 * projectionList.add(Projections.property(
																			 * "masHospital.hospitalName").as(
																			 * "hospitalName"));
																			 * projectionList.add(Projections.property(
																			 * "patient.dateOfBirth").as("dateOfBirth"))
																			 * ;
																			 * 
																			 * projectionList.add(Projections.property(
																			 * "masRelation.relationName").as(
																			 * "relationName"));
																			 * projectionList.add(Projections.property(
																			 * "masAdministrativeSex.administrativeSexName"
																			 * ).as("administrativeSexName"));
																			 * projectionList.add(Projections.property(
																			 * "dgMasInvestigation.investigationName").
																			 * as("investigationName"));
																			 * projectionList.add(Projections.property(
																			 * "masUOM.UOMName").as("UOMName"));
																			 * projectionList.add(Projections.property(
																			 * "masUOM.UOMCode").as("UOMCode"));
																			 * 
																			 * projectionList.add(Projections.property(
																			 * "dgResultEntryHd.resultDate").as(
																			 * "orderDate"));
																			 * projectionList.add(Projections.property(
																			 * "dgResultEntryDt.result").as("result"));
																			 * projectionList.add(Projections.property(
																			 * "dgMasInvestigation.minNormalValue").as(
																			 * "minNormalValue"));
																			 * projectionList.add(Projections.property(
																			 * "dgMasInvestigation.maxNormalValue").as(
																			 * "maxNormalValue"));
																			 * projectionList.add(Projections.property(
																			 * "dgOrderdt.labMark").as("labMark"));
																			 * projectionList.add(Projections.property(
																			 * "dgOrderHd.orderhdId").as("orderhdId"));
																			 * projectionList.add(Projections.property(
																			 * "dgResultEntryDt.ridcId").as("ridcId"));
																			 * projectionList.add(Projections.property(
																			 * "dgResultEntryDt.rangeValue").as("range")
																			 * );
																			 * projectionList.add(Projections.property(
																			 * "dgSubMasInvestigation.subInvestigationName"
																			 * ).as("subInvestigationName"));
																			 * projectionList.add(Projections.property(
																			 * "dgResultEntryDt.resultType").as(
																			 * "resultType"));
																			 * 
																			 * projectionList.add(Projections.property(
																			 * "subMasUOM.UOMName").as("subMasUOM"));
																			 * 
																			 * //projectionList.add(Projections.property
																			 * ("dgNormalValue.normalValue").as(
																			 * "normalValue"));
																			 * 
																			 * cr.setProjection(projectionList);
																			 * totalMatches=cr.list();
																			 * cr.setFirstResult((pageSize) * (pageNo -
																			 * 1)); cr.setMaxResults(pageSize);
																			 * listObject=cr.list();
																			 * 
																			 * 
																			 * 
																			 * }catch (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); }
																			 * map.put("listObject", listObject);
																			 * map.put("totalMatches",
																			 * totalMatches.size()); return map; }
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasState>
																			 * getStateListFromDistrict(long districtId)
																			 * {
																			 * 
																			 * List<MasState> masStateList = new
																			 * ArrayList<MasState>(); List<Long>
																			 * masStateIdList = new ArrayList<Long>();
																			 * 
																			 * try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); masStateIdList =
																			 * session.createCriteria(MasDistrict.class)
																			 * .add(Restrictions.eq("districtId",
																			 * districtId))
																			 * .setProjection(Projections.property(
																			 * "masState.stateId")).list();
																			 * 
																			 * if (masStateIdList.size() > 0) {
																			 * masStateList =
																			 * session.createCriteria(MasState.class)
																			 * .add(Restrictions.in("stateId",
																			 * masStateIdList)).list(); } } catch
																			 * (Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.getMessage();
																			 * e.printStackTrace(); } finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return masStateList;
																			 * 
																			 * }
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasReligion>
																			 * getMasReligionList() { List<MasReligion>
																			 * rStatusList = new
																			 * ArrayList<MasReligion>(); try { Session
																			 * session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); rStatusList =
																			 * session.createCriteria(MasReligion.class)
																			 * .add(Restrictions.eq("status","y").
																			 * ignoreCase())
																			 * .addOrder(Order.asc("religionName")).list
																			 * (); }catch(Exception e) {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); e.printStackTrace();
																			 * }finally {
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return rStatusList;
																			 * }
																			 * 
																			 * 
																			 * 
																			 * @org.springframework.transaction.
																			 * annotation.Transactional public
																			 * List<Object[]>
																			 * getDgMasInvestigationByInvestigationIds_old
																			 * (List<Long> investigationId,String
																			 * orderNo, Long mainChargeCodeId){
																			 * 
																			 * List<Object[]> listObject=null; Integer
																			 * count=0; try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); StringBuilder sbQuery =
																			 * new StringBuilder(); sbQuery.
																			 * append("  select  dgSubMInv.SUB_INVESTIGATION_ID,dgSubMInv.SUB_INVESTIGATION_NAME, odt.ORDERDT_ID,ohd.orderhd_id,rdt.RESULT_ENTRY_DT_ID, rht.RESULT_ENTRY_HD_ID,rdt.result_type,"
																			 * ); sbQuery.
																			 * append("  dgmas.INVESTIGATION_TYPE, dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_NAME,dgUom.UOM_NAME, "
																			 * ); sbQuery.
																			 * append("  TO_CHAR(rdt.RESULT) as resulttt,rdt.range_value,rdt.ridc_id,dgSubMInv.SUB_INVESTIGATION_CODE,dgmas.SUB_CHARGECODE_ID,dgmas.MAIN_CHARGECODE_ID from  "
																			 * ); sbQuery.
																			 * append("  DG_SUB_MAS_INVESTIGATION dgSubMInv   left join  DG_MAS_INVESTIGATION  dgmas on dgmas.INVESTIGATION_ID=dgSubMInv.INVESTIGATION_ID "
																			 * ); sbQuery.
																			 * append("  left join DG_ORDER_DT odt on  odt.INVESTIGATION_ID=dgmas.INVESTIGATION_ID  left join DG_ORDER_HD ohd on ohd.orderhd_id=odt.orderhd_id  "
																			 * ); sbQuery.
																			 * append("  left join DG_UOM  dgUom on dgUom.UOM_ID=dgSubMInv.UOM_ID left join DG_RESULT_ENTRY_DT rdt on odt.ORDERDT_ID=rdt.ORDERDT_ID "
																			 * ); sbQuery.
																			 * append(" left join DG_RESULT_ENTRY_HD rht on ohd.ORDERHD_ID= rht.ORDERHD_ID "
																			 * ); sbQuery.
																			 * append(" where  rdt.result IS NULL and ohd.order_no =:orderNo  and   dgmas.MAIN_CHARGECODE_ID =:mainChargeCodeId  and  odt.INVESTIGATION_ID in :investigationIds "
																			 * );
																			 * 
																			 * sbQuery.
																			 * append(" group by dgSubMInv.SUB_INVESTIGATION_ID, dgSubMInv.SUB_INVESTIGATION_NAME, dgmas.INVESTIGATION_TYPE, dgmas.INVESTIGATION_ID,"
																			 * ); sbQuery.
																			 * append(" dgmas.INVESTIGATION_NAME, dgUom.UOM_NAME, odt.ORDERDT_ID, ohd.orderhd_id, rdt.RESULT_ENTRY_DT_ID, rht.RESULT_ENTRY_HD_ID, TO_CHAR(rdt.RESULT),"
																			 * ); sbQuery.
																			 * append(" dgSubMInv.SUB_INVESTIGATION_CODE, rdt.result_type, rdt.range_value, rdt.ridc_id, dgmas.SUB_CHARGECODE_ID,dgmas.MAIN_CHARGECODE_ID "
																			 * );
																			 * 
																			 * Query query =
																			 * session.createSQLQuery(sbQuery.toString()
																			 * );
																			 * 
																			 * query.setParameter("orderNo", orderNo);
																			 * query.setParameter("mainChargeCodeId",
																			 * mainChargeCodeId);
																			 * query.setParameterList(
																			 * "investigationIds", investigationId);
																			 * listObject = query.list();
																			 * if(CollectionUtils.isNotEmpty(listObject)
																			 * ) { count=listObject.size(); } }
																			 * catch(Exception e) { e.printStackTrace();
																			 * }
																			 * 
																			 * return listObject; }
																			 * 
																			 * 
																			 * 
																			 * @Override public List<Object[]>
																			 * getDgMasInvestigationByInvestigationIds(
																			 * List<Long> investigationId,String
																			 * orderNo, Long mainChargeCodeId){
																			 * List<Object[]> listObject=null; try {
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); StringBuilder sbQuery =
																			 * new StringBuilder();
																			 * 
																			 * 
																			 * /////////////////////////////////////////
																			 * ///////////
																			 * 
																			 * sbQuery.
																			 * append(" SELECT  DSMI.SUB_INVESTIGATION_ID,DSMI.SUB_INVESTIGATION_NAME, ORDER_DT.ORDERDT_ID,ORDER_HD.orderhd_id,DT.RESULT_ENTRY_DT_ID, HD.RESULT_ENTRY_HD_ID,DT.result_type, "
																			 * ); sbQuery.
																			 * append(" DMI.INVESTIGATION_TYPE, DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,SDU.UOM_NAME, "
																			 * ); sbQuery.
																			 * append(" DT.RESULT as resulttt,DT.range_value,DT.ridc_id,DSMI.SUB_INVESTIGATION_CODE,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,DT.REMARKS "
																			 * ); sbQuery.append(" FROM    "
																			 * +DG_ORDER_DT+" ORDER_DT LEFT OUTER JOIN  "
																			 * +DG_ORDER_HD+" ORDER_HD ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_SUB_MAS_INVESTIGATION+" DSMI ON DSMI.INVESTIGATION_ID=DMI.INVESTIGATION_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_RESULT_ENTRY_HD+" HD ON HD.ORDERHD_ID= ORDER_HD.ORDERHD_ID AND HD.SUB_CHARGECODE_ID=MSC.SUB_CHARGECODE_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_RESULT_ENTRY_DT+" DT ON DT.RESULT_ENTRY_HD_ID=HD.RESULT_ENTRY_HD_ID  AND DT.INVESTIGATION_ID=DMI.INVESTIGATION_ID AND DSMI.SUB_INVESTIGATION_ID= DT.SUB_INVESTIGATION_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_UOM+" SDU ON  SDU.UOM_ID=DSMI.UOM_ID "
																			 * ); //sbQuery.
																			 * append(" WHERE  ORDER_HD.visit_id=:visitId  AND UPPER(DMI.INVESTIGATION_TYPE) ='M' "
																			 * ); sbQuery.
																			 * append(" 	WHERE DT.result IS NULL and trim(ORDER_HD.order_no) =:orderNo  and   DMI.MAIN_CHARGECODE_ID =:mainChargeCodeId  AND UPPER(DMI.INVESTIGATION_TYPE) ='M' "
																			 * ); sbQuery.append(" UNION "); sbQuery.
																			 * append(" SELECT  null As SUB_INVESTIGATION_ID,null as SUB_INVESTIGATION_NAME, ORDER_DT.ORDERDT_ID,ORDER_HD.orderhd_id,DT.RESULT_ENTRY_DT_ID, HD.RESULT_ENTRY_HD_ID,DT.result_type, "
																			 * ); sbQuery.
																			 * append(" DMI.INVESTIGATION_TYPE, DMI.INVESTIGATION_ID,DMI.INVESTIGATION_NAME,DU.UOM_NAME,  "
																			 * ); sbQuery.
																			 * append(" DT.RESULT as resulttt,DT.range_value,DT.ridc_id,null As SUB_INVESTIGATION_CODE,DMI.SUB_CHARGECODE_ID,DMI.MAIN_CHARGECODE_ID,DT.REMARKS "
																			 * ); sbQuery.append(" FROM    "
																			 * +DG_ORDER_HD+" ORDER_HD LEFT OUTER JOIN  "
																			 * +DG_ORDER_DT+" ORDER_DT ON ORDER_DT.ORDERHD_ID= ORDER_HD.ORDERHD_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_MAS_INVESTIGATION+" DMI ON  DMI.INVESTIGATION_ID=ORDER_DT.INVESTIGATION_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +MAS_SUB_CHARGECODE+" MSC ON  MSC.SUB_CHARGECODE_ID=DMI.SUB_CHARGECODE_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_RESULT_ENTRY_HD+" HD ON HD.ORDERHD_ID= ORDER_HD.ORDERHD_ID AND HD.SUB_CHARGECODE_ID=MSC.SUB_CHARGECODE_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_RESULT_ENTRY_DT+" DT ON DT.RESULT_ENTRY_HD_ID=HD.RESULT_ENTRY_HD_ID  AND DT.INVESTIGATION_ID=DMI.INVESTIGATION_ID "
																			 * ); sbQuery.append(" LEFT OUTER JOIN  "
																			 * +DG_UOM+" DU ON  DU.UOM_ID=DMI.UOM_ID ");
																			 * // sbQuery.
																			 * append(" WHERE  ORDER_HD.visit_id=:visitId   AND UPPER(DMI.INVESTIGATION_TYPE) IN ('S','T') And ORDER_DT.INVESTIGATION_ID in:investigationIds "
																			 * );
																			 * 
																			 * sbQuery.
																			 * append(" 	WHERE DT.result IS NULL and trim(ORDER_HD.order_no) =:orderNo  and   DMI.MAIN_CHARGECODE_ID =:mainChargeCodeId "
																			 * ); sbQuery.append(
																			 * "AND UPPER(DMI.INVESTIGATION_TYPE) IN ('S','T') And ORDER_DT.INVESTIGATION_ID in:investigationIds "
																			 * );
																			 * 
																			 * ////////////////////////////////////////
																			 * 
																			 * 
																			 * Query query =
																			 * session.createSQLQuery(sbQuery.toString()
																			 * );
																			 * 
																			 * query.setParameter("orderNo", orderNo);
																			 * query.setParameter("mainChargeCodeId",
																			 * mainChargeCodeId);
																			 * query.setParameterList(
																			 * "investigationIds", investigationId);
																			 * listObject = query.list(); }
																			 * catch(Exception e) { e.printStackTrace();
																			 * }
																			 * 
																			 * return listObject; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override
																			 * 
																			 * @org.springframework.transaction.
																			 * annotation.Transactional public
																			 * List<Object[]>
																			 * getDgMasInvestigationByInvestigationIdsForUnique
																			 * (List<Long> investigationId,String
																			 * orderNo, Long mainChargeCodeId){
																			 * 
																			 * List<Object[]> listObject=null; Integer
																			 * count=0; try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); StringBuilder sbQuery =
																			 * new StringBuilder(); sbQuery.
																			 * append("  select  dgSubMInv.SUB_INVESTIGATION_ID,dgSubMInv.SUB_INVESTIGATION_NAME "
																			 * ); sbQuery.append("    from  ");
																			 * sbQuery.append(
																			 * DG_SUB_MAS_INVESTIGATION+" dgSubMInv   left join  "
																			 * +DG_MAS_INVESTIGATION+"  dgmas on dgmas.INVESTIGATION_ID=dgSubMInv.INVESTIGATION_ID "
																			 * ); sbQuery.append("  left join "
																			 * +DG_ORDER_DT+" odt on  odt.INVESTIGATION_ID=dgmas.INVESTIGATION_ID  left join "
																			 * +DG_ORDER_HD+" ohd on ohd.orderhd_id=odt.orderhd_id  "
																			 * ); sbQuery.append("  left join "
																			 * +DG_UOM+"  dgUom on dgUom.UOM_ID=dgSubMInv.UOM_ID left join "
																			 * +DG_RESULT_ENTRY_DT+" rdt on odt.ORDERDT_ID=rdt.ORDERDT_ID "
																			 * ); sbQuery.append(" left join "
																			 * +DG_RESULT_ENTRY_HD+" rht on ohd.ORDERHD_ID= rht.ORDERHD_ID "
																			 * ); sbQuery.
																			 * append(" where  rdt.result IS NULL and ohd.order_no =:orderNo  and   dgmas.MAIN_CHARGECODE_ID =:mainChargeCodeId  and  odt.INVESTIGATION_ID in :investigationIds "
																			 * );
																			 * 
																			 * sbQuery.
																			 * append(" group by dgSubMInv.SUB_INVESTIGATION_ID, dgSubMInv.SUB_INVESTIGATION_NAME "
																			 * );
																			 * 
																			 * Query query =
																			 * session.createSQLQuery(sbQuery.toString()
																			 * );
																			 * 
																			 * query.setParameter("orderNo", orderNo);
																			 * query.setParameter("mainChargeCodeId",
																			 * mainChargeCodeId);
																			 * query.setParameterList(
																			 * "investigationIds", investigationId);
																			 * listObject = query.list();
																			 * if(CollectionUtils.isNotEmpty(listObject)
																			 * ) { count=listObject.size(); } }
																			 * catch(Exception e) { e.printStackTrace();
																			 * }
																			 * 
																			 * return listObject; }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<MasCommand>
																			 * getRegionFromStation(long uniitId) {
																			 * List<MasCommand> commandList = new
																			 * ArrayList<MasCommand>(); Long
																			 * stationId=null; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try { stationId =(Long)
																			 * session.createCriteria(MasUnit.class).add
																			 * (Restrictions.eq("unitId", uniitId))
																			 * .setProjection(Projections.property(
																			 * "masStation.stationId")).uniqueResult();
																			 * if(stationId!=null) { commandList =
																			 * session.createCriteria(MasStation.class).
																			 * add(Restrictions.eq("stationId",
																			 * stationId))
																			 * .setProjection(Projections.property(
																			 * "masCommand")).list(); } }catch(Exception
																			 * e) { e.printStackTrace(); } return
																			 * commandList; }
																			 * 
																			 * 
																			 * 
																			 * @Override public long
																			 * departmentTypeMaternityIdFromCode(String
																			 * departmentTypeCodeMaternity) { long
																			 * departmentTypeMaternityId=0;
																			 * MasDepartment masDepartment = null;
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try {
																			 * masDepartment=(MasDepartment)
																			 * session.createCriteria(MasDepartment.
																			 * class)
																			 * .add(Restrictions.eq("departmentCode",
																			 * departmentTypeCodeMaternity)).
																			 * uniqueResult();
																			 * 
																			 * if(masDepartment!=null) {
																			 * departmentTypeMaternityId =
																			 * masDepartment.getDepartmentId(); }
																			 * 
																			 * }catch (Exception e) { e.getMessage();
																			 * e.printStackTrace();
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return
																			 * departmentTypeMaternityId; }
																			 * 
																			 * 
																			 * 
																			 * @Override public MasEmployee
																			 * getMasEmployeeFromServiceNo(String
																			 * serviceNo) { MasEmployee masEmployee =
																			 * null; Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); try {
																			 * masEmployee=(MasEmployee)
																			 * session.createCriteria(MasEmployee.class)
																			 * .add(Restrictions.eq("serviceNo",
																			 * serviceNo).ignoreCase()).uniqueResult();
																			 * 
																			 * 
																			 * }catch (Exception e) { e.getMessage();
																			 * e.printStackTrace();
																			 * getHibernateUtils.getHibernateUtlis().
																			 * CloseConnection(); } return masEmployee;
																			 * }
																			 * 
																			 * 
																			 * 
																			 * @SuppressWarnings("unchecked")
																			 * 
																			 * @Override public List<Patient>
																			 * findPatientByServiceNumber(String
																			 * serviceNo,Long selfRelationId) {
																			 * 
																			 * List<Patient> patientList = null; Long
																			 * registrationTypeId =
																			 * Long.parseLong(HMSUtil.getProperties(
																			 * "adt.properties",
																			 * "ICG_REGISTRATION_TYPE_ID")); try {
																			 * Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession();
																			 * 
																			 * patientList =
																			 * session.createCriteria(Patient.class)
																			 * .add(Restrictions.eq("serviceNo",
																			 * serviceNo).ignoreCase())
																			 * .add(Restrictions.eq(
																			 * "masRegistrationType.registrationTypeId",
																			 * registrationTypeId))
																			 * .add(Restrictions.eq("relationId",
																			 * selfRelationId))
																			 * .add(Restrictions.isNotNull("uhidNo")).
																			 * list(); } catch(Exception e) {
																			 * e.printStackTrace(); } return
																			 * patientList; }
																			 * 
																			 * 
																			 * 
																			 * 
																			 * @Override public String
																			 * getMasMedicalCategoryFromDBFunction(Long
																			 * patientId, LocalDate date) { JSONObject
																			 * jsonObj = new JSONObject(); String
																			 * patientMedicalCategory =""; Integer
																			 * patientIdtemp=Integer.parseInt(patientId.
																			 * toString()); try { Session session =
																			 * getHibernateUtils.getHibernateUtlis().
																			 * OpenSession(); session.doWork(new Work()
																			 * { public void execute(Connection
																			 * connection) throws SQLException {
																			 * 
																			 * String fnc = "{ ? = call "
																			 * +databaseScema+".FUN_RETURN_MEDICAL_CAT(?,?) }"
																			 * ; CallableStatement call =
																			 * connection.prepareCall(fnc);
																			 * call.registerOutParameter(1,
																			 * Types.VARCHAR); call.setInt(2,
																			 * patientIdtemp); call.setDate(3,
																			 * java.sql.Date.valueOf(date));
																			 * call.executeUpdate(); String result =
																			 * call.getString(1); String
																			 * medicalCategory=""; if(result!=null &&
																			 * !result.isEmpty()) { medicalCategory
																			 * =result; jsonObj.put("medicalCategory",
																			 * medicalCategory); }else {
																			 * jsonObj.put("medicalCategory",""); }
																			 * 
																			 * } });
																			 * 
																			 * } catch (Exception e) {
																			 * jsonObj.put("medicalCategory","");
																			 * e.printStackTrace(); }
																			 * patientMedicalCategory=
																			 * jsonObj.getString("medicalCategory");
																			 * return patientMedicalCategory;
																			 * 
																			 * 
																			 * }
																			 */

	@SuppressWarnings("unchecked")
	@Override
	public List<MasDistrict> getDistrictList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasDistrict> list = new ArrayList<MasDistrict>();
		try {
			Long stateId = Long.parseLong(String.valueOf(requestData.get("stateId")));
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(MasDistrict.class).add(Restrictions.eq("status", "Y").ignoreCase())
					.createAlias("masState", "state").add(Restrictions.eq("state.stateId", stateId)).addOrder(Order.asc("districtName")).list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasCity> getCityList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasCity> list = new ArrayList<>();
		try {
			Long districtId = null;

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class).add(Restrictions.eq("status", "Y").ignoreCase());

			if (requestData.get("districtId") != null) {
				districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));
				criteria.createAlias("masDistrict", "district").add(Restrictions.eq("district.districtId", districtId));
			}
			list = criteria.addOrder(Order.asc("cityName")).list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasMMU> getMMUList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasMMU> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasMMU.class).add(Restrictions.eq("status", "Y").ignoreCase());

			if (requestData.get("cityId") != null && !String.valueOf(requestData.get("cityId")).equals("")
					&& !String.valueOf(requestData.get("cityId")).equals("null")) {
				criteria.add(Restrictions.eq("cityId", Long.parseLong(String.valueOf(requestData.get("cityId")))));
			}
			list = criteria.addOrder(Order.asc("mmuName")).list();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Map<String, Object> createCampPlan(Map<String, Object> requestData) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			//Long cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));
			//Long clusterId = Long.parseLong(String.valueOf(requestData.get("clusterId")));
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			Long year = Long.parseLong(String.valueOf(requestData.get("year")));
			Long month = Long.parseLong(String.valueOf(requestData.get("month")));
			Long districtId = null;
			

			List<Map<String, Object>> campPlanList = (List<Map<String, Object>>) requestData.get("campPlanList");
			Boolean campFlag = true;
			for (Map<String, Object> campPlan : campPlanList) {
				Long cityId=0L;
				MasCity mCity = null;
				if (!String.valueOf(campPlan.get("cityIdMul")).trim().equals("")) {
					cityId = Long.parseLong(String.valueOf(campPlan.get("cityIdMul")));
				
				  mCity = getMasCityByCityId(cityId,session);//(MasCity) session.get(MasCity.class, cityId);					
				if (mCity != null) {
					districtId = mCity.getMasDistrict().getDistrictId();
				}
			}
				String campDateInStr = String.valueOf(campPlan.get("campDate"));
				String campDateInString = String.valueOf(campPlan.get("campDate"));
				Date campDate = HMSUtil
						.convertStringDateToUtilDateForDatabase(String.valueOf(campPlan.get("campDate")));
				String day = String.valueOf(campPlan.get("day"));
				String campOrweeklyOffOrCL = String.valueOf(campPlan.get("weeklyOff"));
				String startTime = String.valueOf(campPlan.get("startTime"));
				String endTime = String.valueOf(campPlan.get("endTime"));
				Long departmentId = null, zoneId = null, wardId = null;
				if (!String.valueOf(campPlan.get("departmentId")).trim().equals("")) {
					departmentId = Long.parseLong(String.valueOf(campPlan.get("departmentId")));
				}
				String location = String.valueOf(campPlan.get("location"));
				String landmark = String.valueOf(campPlan.get("landmark"));
				if (!String.valueOf(campPlan.get("zoneId")).trim().equals("")) {
					zoneId = Long.parseLong(String.valueOf(campPlan.get("zoneId")));
				}
				if (!String.valueOf(campPlan.get("wardId")).trim().equals("")) {
					wardId = Long.parseLong(String.valueOf(campPlan.get("wardId")));
				}
				// Long zoneId = Long.parseLong(String.valueOf(campPlan.get("zoneId")));
				// Long wardId = Long.parseLong(String.valueOf(campPlan.get("wardId")));
				Double lattitude = null, longitude = null;

				if (!String.valueOf(campPlan.get("longitude")).trim().equals("")) {
					longitude = Double.parseDouble(String.valueOf(campPlan.get("longitude")));
				}
				if (!String.valueOf(campPlan.get("lattitude")).trim().equals("")) {
					lattitude = Double.parseDouble(String.valueOf(campPlan.get("lattitude")));
				}

				Long campId = null;
				if (campPlan.get("id") != null && !campPlan.get("id").equals("")) {
					campId = Long.parseLong(String.valueOf(campPlan.get("id")));
				}
				String campOffFlag = String.valueOf(campPlan.get("campOffFlag"));
				String locationFlag = String.valueOf(campPlan.get("locationFlag"));
				// MasCamp mapCamp = null;
				/*MasCity masCity = null;
				if(cityId!=null && cityId!=0) {
					masCity = new MasCity();
					masCity.setCityId(cityId);
				} */
				MasMMU masMMU = new MasMMU();
				masMMU.setMmuId(mmuId);
				MasZone masZone = new MasZone();
				masZone.setZoneId(zoneId);
				MasWard masWard = new MasWard();
				masWard.setWardId(wardId);
				MasDepartment masDepartment = new MasDepartment();
				masDepartment.setDepartmentId(departmentId);
				MasDistrict masDistrict = new MasDistrict();
				if(districtId!=null)
				masDistrict.setDistrictId(districtId);
				String previousLocation = "";
				if(campId != null) {
					// save camp detail
					MasCamp masCamp = getMasCampByCampIdId(campId,session);//(MasCamp) session.get(MasCamp.class, campId);
					previousLocation = masCamp.getLocation();
					if (masCamp != null) {
						if (campDate.after(HMSUtil.getPreviousDate(new Date()))) {
							campFlag = false;
							if(cityId!=null && cityId!=0) {
							//masCamp.setMasCity(masCity);
							masCamp.setCityId(cityId);
							}
							masCamp.setMmuId(mmuId);
							masCamp.setYear(year);
							masCamp.setMonth(month);
							masCamp.setCampDate(campDate);
							masCamp.setWeeklyOff(campOrweeklyOffOrCL);
							masCamp.setStartTime(HMSUtil.convertStringTimeIntoSQLTime(startTime));
							masCamp.setEndTime(HMSUtil.convertStringTimeIntoSQLTime(endTime));
							masCamp.setLocation(location);
							masCamp.setLandMark(landmark);
							masCamp.setZoneId(zoneId);
							masCamp.setWardId(wardId);
							masCamp.setLongitude(longitude);
							masCamp.setLattitude(lattitude);
							masCamp.setDepartmentId(departmentId);
							masCamp.setDay(day);
							if(districtId!=null)
							masCamp.setDistrictId(districtId);
							//masCamp.setClusterId(clusterId);

							

							List<Patient> patientList = new ArrayList<Patient>();
							boolean smsFlag = false;
							if (campOffFlag.equals("true")) {
								smsFlag = true;
							} else {
								if (locationFlag.equals("true")) {
									smsFlag = true;
								}
							}
							if (smsFlag) {
								List<Visit> visitList = session.createCriteria(Visit.class)
										.add(Restrictions.eq("campId", campId)).list();
								for (Visit visit : visitList) {
									patientList.add(visit.getPatient());
								}
								//System.out.println(
										//"Patient List **************************************" + patientList.toString());
							}

						/*
							if (campOffFlag.equals("true")) {
								for (Patient patient : patientList) {
									String mobileNo = patient.getMobileNumber();
									//String mobileNo = "8510096155";
									String name = patient.getPatientName();
									//String cancellationDate = campDateInString;
									try {
										final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Anupabladhata&var1="+name+"&var2="+campDateInString+"";
										HttpResponse<String> response = Unirest.post(uri).asString();
										System.out.println("SMS send succefully");
									} catch (Exception e) {
										map.put("status", true);
										map.put("msg", "Something went wrong while sending sms");
										return map;
									}
								}

							} else {
								if (locationFlag.equals("true")) {
									System.out.println("befor looping******************************************* "
											+ patientList.size());
									for (Patient patient : patientList) {
										String mobileNo = patient.getMobileNumber();
										//String mobileNo = "8510096155";
										String name = patient.getPatientName();
										String updatedLocation = location;
										String mmuName = masCamp.getMasMMU().getMmuName();
										try {
											final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Medicalunit&var1="+name+"&var2="+previousLocation+"&var3="+updatedLocation+"&var4="+mmuName+"";
											HttpResponse<String> response = Unirest.post(uri).asString();
											System.out.println("SMS send succefully");
										} catch (Exception e) {
											System.out.println("e="+e);
											map.put("status", true);
											map.put("msg", "Something went wrong while sending sms");
											return map;
										}
										
									}
								}
							}
							
							*/
							
							

							session.saveOrUpdate(masCamp);
						}
					}

				} else {
					if (campDate.after(HMSUtil.getPreviousDate(new Date()))) {
						campFlag = false;
						MasCamp mapCamp = new MasCamp();
						mapCamp.setDay(day);
						if(cityId!=null && cityId!=0)
						mapCamp.setCityId(cityId);
						mapCamp.setMmuId(mmuId);
						mapCamp.setYear(year);
						mapCamp.setMonth(month);
						mapCamp.setCampDate(campDate);
						mapCamp.setWeeklyOff(campOrweeklyOffOrCL);
						mapCamp.setStartTime(HMSUtil.convertStringTimeIntoSQLTime(startTime));
						mapCamp.setEndTime(HMSUtil.convertStringTimeIntoSQLTime(endTime));
						mapCamp.setLocation(location);
						mapCamp.setLandMark(landmark);
						mapCamp.setZoneId(zoneId);
						mapCamp.setWardId(wardId);
						mapCamp.setLongitude(longitude);
						mapCamp.setLattitude(lattitude);
						mapCamp.setDepartmentId(departmentId);
						//mapCamp.setClusterId(clusterId);
						mapCamp.setDistrictId(districtId);
						Long savedCampId = Long.parseLong((session.save(mapCamp).toString()));

					}
				}

			}
			if (campFlag) {
				map.put("status", false);
				map.put("msg", "Camp Plan cannot be created for past date.");
				return map;
			}
			tx.commit();
			map.put("status", true);
			map.put("msg", "Camp Plan created.");
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("status", false);
		map.put("msg", "Something went wrong.");
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasZone> getZoneList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasZone> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Long cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));
			Criteria cr = session.createCriteria(MasZone.class).createAlias("masCity", "masCity");
				if(cityId!=0)
					cr.add(Restrictions.eq("masCity.cityId", cityId));
					cr.add(Restrictions.eq("status", "Y").ignoreCase());
					list=cr.list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasWard> getWardList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasWard> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Long cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));
			Criteria cr = session.createCriteria(MasWard.class).createAlias("masCity", "masCity");
					if(cityId!=0)
					cr.add(Restrictions.eq("masCity.cityId", cityId));
					cr.add(Restrictions.eq("status", "Y").ignoreCase());
					
					cr.addOrder(Order.asc("wardName"));
					list=cr.list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCampDetail(Map<String, Object> requestData) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			Long cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			Long year = Long.parseLong(String.valueOf(requestData.get("year")));
			Long month = Long.parseLong(String.valueOf(requestData.get("month")));

			List<MasCamp> list=null;
			Criteria cr = session.createCriteria(MasCamp.class).add(Restrictions.eq("year", year)).add(Restrictions.eq("month", month));
			 	
			if(cityId!=0) {
					cr.add(Restrictions.eq("cityId", cityId));
					}
					cr.add(Restrictions.eq("mmuId", mmuId)).addOrder(Order.asc("campDate"));
					list=cr.list();
			map.put("status", true);
			map.put("msg", "success");
			map.put("list", list);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("status", false);
		map.put("msg", "Some Error occured");
		return map;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	@Transactional
	public Map<String, Object> updatePatientInformation(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		Transaction tx = session.beginTransaction();
		try {
			Long patientId = Long.parseLong(String.valueOf(requestData.get("patientId")));
			
			Long genderId = Long.parseLong(String.valueOf(requestData.get("gender")));
			
			Long age = Long.parseLong(String.valueOf(requestData.get("age")));
			String ageFlag = String.valueOf(requestData.get("ageId"));
			String patientName = String.valueOf(requestData.get("patientName"));
			String ProcessFull = String.valueOf(requestData.get("demo1"));
			
			/*added for profile image*/
			String patientProfileImage =String.valueOf(requestData.get("patientImage"));
			
			if(ageFlag.equalsIgnoreCase("Yrs")) {
				ageFlag = "Y";
			}else if(ageFlag.equalsIgnoreCase("Mths")) {
				ageFlag = "M";
			}else if(ageFlag.equalsIgnoreCase("Days")) {
				ageFlag = "D";
			}
			Date dob = HMSUtil.getDOBFromAge(age.intValue(),ageFlag);
			String mobileNo = String.valueOf(requestData.get("mobileNo"));
			String mobileNoUser = String.valueOf(requestData.get("mobieNo"));
			//Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			System.out.print("Patent Image By Mobileno:"+mobileNoUser  +"--"+requestData.get("mobieNo"));
			List<Patient> plist = new ArrayList<Patient>();
			plist = session.createCriteria(Patient.class)
					.add(Restrictions.eq("mobileNumber", mobileNoUser))
					.add(Restrictions.neOrIsNotNull("patientImage", null)).list();
				//List<Patient>	plist = criteria.list();
				for(Patient p: plist){
					
					//System.out.println("Patent Image By Mobileno:"+p.getPatientImage());
				}
				
				//Image
			Boolean flag = getPatient(patientName, mobileNo, genderId, age);
			
			
			if (flag == null) {
				response.put("status", false);
				response.put("msg", "Something went wrong");
				return response;
			}
			if (flag) {
				response.put("status", false);
				response.put("msg", "Patient is already registered");
				return response;
			}
			
			Long campId = Long.parseLong(String.valueOf(requestData.get("campId")));
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			Long userId = Long.parseLong(String.valueOf(requestData.get("userId")));
			String updateOrAppointFlag = String.valueOf(requestData.get("updateOrAppointFlag"));
			List<String> symptoms = (List<String>) requestData.get("symptoms");
			List<Map<String, Object>> symptomsId = (List<Map<String, Object>>) requestData.get("symptomsId");

			Patient patient = (Patient) session.get(Patient.class, patientId);
			patient.setPatientName(patientName);
			patient.setAge(age);
			patient.setDateOfBirth(dob);
			patient.setAdministrativeSexId(genderId);
			
			String base64String = patientProfileImage;
			if(patientProfileImage == ""){
				
			}else{
			System.out.print("update: "+patientProfileImage);
			 String[] strings = base64String.split(",");
			    String extension;
			    switch (strings[0]) {//check image's extension
			        case "data:image/jpeg;base64":
			            extension = "jpeg";
			            break;
			        case "data:image/png;base64":
			            extension = "png";
			            break;
			        default://should write cases for more images types
			            extension = "jpg";
			            break;
			    }
			    String PatientMatchPercentage ;
			    //convert base64 string to binary data
			    String rootPath = environment.getProperty("service.patientImagePath");
			    String ServerImageUrl = environment.getProperty("server.imageUrl");
			    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
			    String tempPath = environment.getProperty("server.imgMatch");
				   
			    String imgMatch = HMSUtil.getProperties("adt.properties","IMG_MATCH").trim();
			  
			    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			    byte[] data = null;
			   try{
			    data = DatatypeConverter.parseBase64Binary(strings[1]);
			   }catch (Exception e) {
				// TODO: handle exception
			}
			    String fileName = patientId+"-"+ sdf1.format(timestamp)+"."+ extension;
			    String path =null;
			    path = rootPath+"//"+fileName;
			    File file = new File(path);
			    //System.out.println("FilePath:"+""+ServerImageUrl+fileName);
			    //System.out.println("fileName:"+fileName);
			    String completeFilePath = tempPath+"//" + fileName;
			    //String completeFilePath = "C:/temp/" + fileName;
			    //System.out.println("patientProfileImage:="+patientProfileImage);
			    //System.out.println("fileName:="+data);
			    File fileTemp = new File(completeFilePath);
			    System.out.print("imgMatch"+imgMatch +""+tempPath);
			    
                List<Patient> list=new ArrayList<Patient>();
				
			    //store data
			   /* for(Patient p1: plist){
			    	   
			        BufferedImage imgA1 = null;
					BufferedImage imgB1 = null;
					// Try block to check for exception
					if(p1.getPatientImage() != null){
			        try {
			        	try{
			        	  OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileTemp)); 
						        outputStream.write(data);
			        	}catch (Exception e) {
							// TODO: handle exception
						}
			            // Reading file from local directory by
			            // creating object of File class
			            File fileA
			                = new File(rootPath+"//"+p1.getPatientImage());
			            File fileB
			                = new File(completeFilePath);
			  
			            // Reading files
			            imgA1 = ImageIO.read(fileA);
			            imgB1 = ImageIO.read(fileB);
			        }
			  
			        // Catch block to check for exceptions
			        catch (IOException e) {
			            // Display the exceptions on console
			            System.out.println(e);
			        }
			  
			        // Assigning dimensions to image
			        int width22 = 0 ;
			        int height22 = 0 ;
			        int width11 = 0;
			        int height11 =0;
			     
			        // Checking whether the images are of same size or
			        // not
			        if ((width11 != width22) || (height11 != height22)){
			  
			            // Display message straightaway
			            System.out.println("Error: Images dimensions"
			                               + " mismatch");
			        response.put("status", false);
					response.put("msg", "Error: Images dimensions"
                               + " mismatch");
					//return response;
			        }else {
			  
			            // By now, images are of same size
			        	if(imgA1 !=null){
			        		 width11 = imgA1.getWidth();
						      height11 = imgA1.getHeight();
			        	}
			        	 if(imgB1 != null){
			        		 width22 = imgB1.getWidth();
				        	 height22 = imgB1.getHeight();
			        	 }
			        	
			            long difference = 0;
			            // treating images likely 2D matrix
			           // Outer loop for rows(height)
			          if(imgA1 !=null && imgB1!=null){
			            for (int y = 0; y < height11; y++) {
			  
			                // Inner loop for columns(width)
			                for (int x = 0; x < width11; x++) {
			  
			                    int rgbA = imgA1.getRGB(x, y);
			                    int rgbB = imgB1.getRGB(x, y);
			                    int redA = (rgbA >> 16) & 0xff;
			                    int greenA = (rgbA >> 8) & 0xff;
			                    int blueA = (rgbA)&0xff;
			                    int redB = (rgbB >> 16) & 0xff;
			                    int greenB = (rgbB >> 8) & 0xff;
			                    int blueB = (rgbB)&0xff;
			  
			                    difference += Math.abs(redA - redB);
			                    difference += Math.abs(greenA - greenB);
			                    difference += Math.abs(blueA - blueB);
			                }
			            }
			          
			          }
			       
			            double total_pixels = width11 * height11 * 3;
			            double avg_different_pixels
			                = difference / total_pixels;
			  
			            // There are 255 values of pixels in total
			            double percentage
			                = (avg_different_pixels / 255) * 100;
			            percentage = 100 - percentage;
			            // Lastly print the difference percentage
			            System.out.println("Difference Percentage-->"
			                               + percentage);
			            DecimalFormat decimalFormat = new DecimalFormat("#");
			            System.out.println("Difference Percentage Dec-->" +decimalFormat.format(percentage));
			            String inPhotoBase64String = "";
			            String ServerImageUrl1 = environment.getProperty("server.imageUrl");
			            if(p1.getPatientImage() != null){
							String imageFolderPathDirctory = ServerImageUrl1 ;
							String completePath = imageFolderPathDirctory +"/"+ p1.getPatientImage();
							System.out.println("completePath"+completePath);
							File f = new File(completePath);
							if(f.exists()) {
								inPhotoBase64String = encodeFileToBase64Binary(f);
							//	System.out.println("inPhotoBase64String:::"+inPhotoBase64String);
							}
							//System.out.println(inPhotoBase64String);
					
						}
						int newPercent = Integer.parseInt(decimalFormat.format(percentage));
						if(newPercent > Integer.parseInt(imgMatch)){
						 Patient patient2 = new Patient();
						 patient2.setPatientName(p1.getPatientName());
						 patient2.setRegDate(p1.getRegDate());
						 patient2.setPatientImage("data:image/png;base64,"+inPhotoBase64String);
						 list.add(patient2);
						 System.out.print("list count:"+list.size());
						}
			        }
					}
			    }*/
			    
			    
			    if(ProcessFull != ""){
			    	System.out.print("if ProcessFull"+ProcessFull);
			    	if(data!=null){
					    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
					        outputStream.write(data);
					        //System.out.println("PatientId"+patientId);
					     //   patient.setPatientImage(ServerImageUrl+fileName);
					        patient.setPatientImage(fileName);
					        session.update(patient);
					        // previous history mark status to N
					        PatientDataUpload previousHistory =new PatientDataUpload();
					        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
					        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
					        if(patientId!=null){
					        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
									.add(Restrictions.eq("patientId", patientId)).list();
					        //System.out.println("list:"+patientDataUploadsList);
		                     for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
		                    	 patientDataUpload.setStatus("N");
		                    	 session.update(patientDataUpload);
							    }
					       
					       
					        }
					        PatientDataUpload history =new PatientDataUpload();
					        history.setPatientId(patientId);
					        history.setFileData(fileName);
					        history.setStatus("Y");
					        history.setLastchgBy(userId);
					        history.setLastChgDate(timestamp);
					        session.save(history);
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					    }
			    }else{
			    	System.out.print("ELSE ProcessFull"+ProcessFull);
			    // final store
			    if(path !=null || !path.isEmpty() && patientProfileImage !=null){
					if(plist.size() ==0){
					if(data!=null){
					    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
					        outputStream.write(data);
					        //System.out.println("PatientId"+patientId);
					        patient.setPatientImage(fileName);
					        session.update(patient);
					        PatientDataUpload previousHistory =new PatientDataUpload();
					        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
					        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
					        if(patientId!=null){
					        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
									.add(Restrictions.eq("patientId", patientId)).list();
					        //System.out.println("list:"+patientDataUploadsList);
		                     for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
		                    	 patientDataUpload.setStatus("N");
		                    	 session.update(patientDataUpload);
							    }
					        }
					        PatientDataUpload history =new PatientDataUpload();
					        history.setPatientId(patientId);
					        history.setFileData(fileName);
					        history.setStatus("Y");
					        history.setLastchgBy(userId);
					        history.setLastChgDate(timestamp);
					        session.save(history);
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					    }
					}else if(plist.size() !=0 && data!=null){
						//List<Patient> list=new ArrayList<Patient>();
                for(Patient p: plist){
					
					//System.out.println("Patent Image By Mobileno:"+p.getPatientImage());
					
				        BufferedImage imgA = null;
						BufferedImage imgB = null;
						// Try block to check for exception
						if(p.getPatientImage() != null){
				        try {
				        	try{
				        	  OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileTemp)); 
							        outputStream.write(data);
				        	}catch (Exception e) {
								// TODO: handle exception
							}
				            // Reading file from local directory by
				            // creating object of File class
				            File fileA
				                = new File(rootPath+"//"+p.getPatientImage());
				            File fileB
				                = new File(completeFilePath);
				  
				            // Reading files
				            imgA = ImageIO.read(fileA);
				            imgB = ImageIO.read(fileB);
				        }
				  
				        // Catch block to check for exceptions
				        catch (IOException e) {
				            // Display the exceptions on console
				            //System.out.println(e);
				        }
				  
				        // Assigning dimensions to image
				        int width2 = 0 ;
				        int height2 = 0 ;
				        int width1 = 0;
				        int height1 =0;
				     
				        // Checking whether the images are of same size or
				        // not
				        if ((width1 != width2) || (height1 != height2)){
				  
				            // Display message straightaway
				            //System.out.println("Error: Images dimensions"
				                               //+ " mismatch");
				        response.put("status", false);
						response.put("msg", "Error: Images dimensions"
	                               + " mismatch");
						//return response;
				     //   }else {
				  
				            // By now, images are of same size
				        	/*if(imgA !=null){
				        		 width1 = imgA.getWidth();
							      height1 = imgA.getHeight();
				        	}
				        	 if(imgB != null){
				        		 width2 = imgB.getWidth();
					        	 height2 = imgB.getHeight();
				        	 }
				        	
				            long difference = 0;*/
				            // treating images likely 2D matrix
				           // Outer loop for rows(height)
				         /* if(imgA !=null && imgB!=null){
				            for (int y = 0; y < height1; y++) {
				  
				                // Inner loop for columns(width)
				                for (int x = 0; x < width1; x++) {
				  
				                    int rgbA = imgA.getRGB(x, y);
				                    int rgbB = imgB.getRGB(x, y);
				                    int redA = (rgbA >> 16) & 0xff;
				                    int greenA = (rgbA >> 8) & 0xff;
				                    int blueA = (rgbA)&0xff;
				                    int redB = (rgbB >> 16) & 0xff;
				                    int greenB = (rgbB >> 8) & 0xff;
				                    int blueB = (rgbB)&0xff;
				  
				                    difference += Math.abs(redA - redB);
				                    difference += Math.abs(greenA - greenB);
				                    difference += Math.abs(blueA - blueB);
				                }
				            }
				          
				          }
				          
				            double total_pixels = width1 * height1 * 3;
				  
				            // Normalizing the value of different pixels
				            // for accuracy
				  
				            // Note: Average pixels per color component
				            double avg_different_pixels
				                = difference / total_pixels;
				  
				            // There are 255 values of pixels in total
				            double percentage
				                = (avg_different_pixels / 255) * 100;
				            percentage = 100 - percentage;
				            // Lastly print the difference percentage
				            System.out.println("Difference Percentage-->"
				                               + percentage);
				            DecimalFormat decimalFormat = new DecimalFormat("#");
				           // System.out.println("Difference Percentage Dec-->" +decimalFormat.format(percentage));
				           
				            
							int newPercent = Integer.parseInt(decimalFormat.format(percentage));
				           
							  String inPhotoBase64String = "";
					            String ServerImageUrl1 = environment.getProperty("server.imageUrl");
					            if(p.getPatientImage() != null){
									String imageFolderPathDirctory = ServerImageUrl1 ;
									String completePath = imageFolderPathDirctory +"/"+ p.getPatientImage();
									System.out.println("completePath"+completePath);
									File f = new File(completePath);
									if(f.exists()) {
										inPhotoBase64String = encodeFileToBase64Binary(f);
									//	System.out.println("inPhotoBase64String:::"+inPhotoBase64String);
									}
									//System.out.println(inPhotoBase64String);
							
								}*/
								 //newPercent = Integer.parseInt(decimalFormat.format(percentage));
								 
								/* Patient patient2 = new Patient();
								 patient2.setPatientName(p.getPatientName());
								 patient2.setRegDate(p.getRegDate());
								 patient2.setPatientImage("data:image/png;base64,"+inPhotoBase64String);
								 list.add(patient2);*/
							
							/*if(newPercent > Integer.parseInt(imgMatch)){
								   response.put("listImg", list);
									
								   response.put("match", "Match");
								   response.put("status", false);
	                        	   response.put("msg", "Duplicate patent registration photo is found for this mobile number. To verify the existing patient photo, click on Registration History button. Do you want to continue the registration/ appointment?");
	                        	 
	                        	   return response;
	                       }*/
							//else{
                        	   if(data!=null){
                        		   System.out.print("else");
                   			    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                   			        outputStream.write(data);
                   			        //System.out.println("PatientId TRY"+patientId);
                   			     //   patient.setPatientImage(ServerImageUrl+fileName);
                   			        patient.setPatientImage(fileName);
                   			        session.update(patient);
                   			        // previous history mark status to N
                   			        PatientDataUpload previousHistory =new PatientDataUpload();
                   			        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
                   			        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
                   			        if(patientId!=null){
                   			        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
                   							.add(Restrictions.eq("patientId", patientId)).list();
                   			        //System.out.println("list:"+patientDataUploadsList);
                                        for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
                                       	 patientDataUpload.setStatus("N");
                                       	 session.update(patientDataUpload);
                                       	System.out.print("history update");
                   					    }
                   			       
                   			        }
                   			        PatientDataUpload history =new PatientDataUpload();
                   			        history.setPatientId(patientId);
                   			        history.setFileData(fileName);
                   			        history.setStatus("Y");
                   			        history.setLastchgBy(userId);
                   			        history.setLastChgDate(timestamp);
                   			        session.save(history);
                   			        System.out.print("history save");
                   			    } catch (IOException e) {
                   			        e.printStackTrace();
                   			     System.out.print("catch"+e.getMessage());
                   			    }
                   			    }
							//}
                           
							//return response;
							
				        }
					}
				}
                
              
					}
			    }
			    else if(data == null){
					
				}
			    }
			    
			   
			  /*  if(data!=null){
			    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			        outputStream.write(data);
			        System.out.println("PatientId"+patientId);
			     //   patient.setPatientImage(ServerImageUrl+fileName);
			        patient.setPatientImage(fileName);
			        session.update(patient);
			        // previous history mark status to N
			        PatientDataUpload previousHistory =new PatientDataUpload();
			        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
			        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
			        if(patientId!=null){
			        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
							.add(Restrictions.eq("patientId", patientId)).list();
			        System.out.println("list:"+patientDataUploadsList);
                     for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
                    	 patientDataUpload.setStatus("N");
                    	 session.update(patientDataUpload);
					    }
			       
			       
			        }
			        PatientDataUpload history =new PatientDataUpload();
			        history.setPatientId(patientId);
			        history.setFileData(fileName);
			        history.setStatus("Y");
			        history.setLastchgBy(userId);
			        history.setLastChgDate(timestamp);
			        session.save(history);
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    }*/
			}
			
			if (requestData.get("identificationTypeId") != null
					&& !String.valueOf(requestData.get("identificationTypeId")).equals("")
					&& !String.valueOf(requestData.get("identificationTypeId")).equals("null")) {
				Long identificationTypeId = Long.parseLong(String.valueOf(requestData.get("identificationTypeId")));
				patient.setIdentificationTypeId(identificationTypeId);
			}
			if (requestData.get("districtId") != null && !String.valueOf(requestData.get("districtId")).equals("")
					&& !String.valueOf(requestData.get("districtId")).equals("null")) {
				Long districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));
				patient.setDistrictId(districtId);

			}
			if (requestData.get("cityId") != null && !String.valueOf(requestData.get("cityId")).equals("")
					&& !String.valueOf(requestData.get("cityId")).equals("null")) {
				Long cityId = Long.parseLong(String.valueOf(requestData.get("cityId")));
				patient.setCityId(cityId);
			}

			if (requestData.get("patientType") != null && !String.valueOf(requestData.get("patientType")).equals("")
					&& !String.valueOf(requestData.get("patientType")).equals("null")) {
				String patientType = String.valueOf(requestData.get("patientType"));
				patient.setPatientType(patientType);
			}

			if (requestData.get("isFormSubmitted") != null
					&& !String.valueOf(requestData.get("isFormSubmitted")).equals("")
					&& !String.valueOf(requestData.get("isFormSubmitted")).equals("null")) {
				String isFormSubmitted = String.valueOf(requestData.get("isFormSubmitted"));
				patient.setFormSubmitted(isFormSubmitted);
			}

			if (requestData.get("isLabourRegistered") != null
					&& !String.valueOf(requestData.get("isLabourRegistered")).equals("")
					&& !String.valueOf(requestData.get("isLabourRegistered")).equals("null")) {
				String isLabourRegistered = String.valueOf(requestData.get("isLabourRegistered"));
				patient.setLaborRegistered(isLabourRegistered);
			}

			if (requestData.get("occupation") != null && !String.valueOf(requestData.get("occupation")).equals("")
					&& !String.valueOf(requestData.get("occupation")).equals("null")) {
				String occupation = String.valueOf(requestData.get("occupation"));
				patient.setOccuption(occupation);
			}

			if (requestData.get("registrationNo") != null
					&& !String.valueOf(requestData.get("registrationNo")).equals("")
					&& !String.valueOf(requestData.get("registrationNo")).equals("null")) {
				String registrationNo = String.valueOf(requestData.get("registrationNo"));
				patient.setRegNo(registrationNo);
			}

			if (requestData.get("relationId") != null && !String.valueOf(requestData.get("relationId")).equals("")
					&& !String.valueOf(requestData.get("relationId")).equals("null")) {
				Long relationId = Long.parseLong(String.valueOf(requestData.get("relationId")));
				patient.setRelationId(relationId);
			}

			if (requestData.get("identificationNo") != null
					&& !String.valueOf(requestData.get("identificationNo")).equals("")
					&& !String.valueOf(requestData.get("identificationNo")).equals("null")) {
				String identificationNo = String.valueOf(requestData.get("identificationNo"));
				patient.setIdentificationNo(identificationNo);
			}

			if (requestData.get("address") != null && !String.valueOf(requestData.get("address")).equals("")
					&& !String.valueOf(requestData.get("address")).equals("null")) {
				String address = String.valueOf(requestData.get("address"));
				patient.setAddress(address);
			}

			if (requestData.get("pincode") != null && !String.valueOf(requestData.get("pincode")).equals("")
					&& !String.valueOf(requestData.get("pincode")).equals("null")) {
				Long pincode = Long.parseLong(String.valueOf(requestData.get("pincode")));
				patient.setPincode(pincode.intValue());
			}

			if (requestData.get("labourId") != null && !String.valueOf(requestData.get("labourId")).equals("")) {
				Long labourId = Long.parseLong(String.valueOf(requestData.get("labourId")));
				patient.setLabourId(labourId);
			}

			if (requestData.get("bloodGroupId") != null && !String.valueOf(requestData.get("bloodGroupId")).equals("")
					&& !String.valueOf(requestData.get("bloodGroupId")).equals("null")) {
				Long bloodGroupId = Long.parseLong(String.valueOf(requestData.get("bloodGroupId")));
				patient.setBloodGroupId(bloodGroupId);
			}

			if (requestData.get("wardId") != null && !String.valueOf(requestData.get("wardId")).equals("")
					&& !String.valueOf(requestData.get("wardId")).equals("null")) {
				Long wardId = Long.parseLong(String.valueOf(requestData.get("wardId")));
				patient.setWardId(wardId);
			}

			if (requestData.get("castId") != null && !String.valueOf(requestData.get("castId")).equals("")
					&& !String.valueOf(requestData.get("castId")).equals("null")) {
				Long castId = Long.parseLong(String.valueOf(requestData.get("castId")));
				patient.setCastId(castId);
			}

			session.save(patient);
			response.put("status", true);
			response.put("msg", "Patient Information is updated.");

			if (updateOrAppointFlag.equalsIgnoreCase("appointment")) {

				MasCamp camp = (MasCamp) session.get(MasCamp.class, campId);
				if (camp != null) {
					String checkWeeklyOffOrCL = camp.getWeeklyOff();
					if (!checkWeeklyOffOrCL.trim().equalsIgnoreCase("Camp")) {
						response.put("status", false);
						response.put("msg", "Appointment cannot be booked for Weekly Off and CL.");
						return response;
					}
				}

				Date nextDate = HMSUtil.getNextDate(new Date());
				Date todayDate = HMSUtil.getTodayFormattedDate();
				Long departmentId = Long.parseLong(String.valueOf(requestData.get("departmentId")));
				// check visit if appointment is already done
				List<Visit> list = session.createCriteria(Visit.class).add(Restrictions.eq("patientId", patientId))
						.add(Restrictions.eq("departmentId", departmentId)).add(Restrictions.ge("visitDate", todayDate))
						.add(Restrictions.lt("visitDate", nextDate)).list();

				if (list.size() > 0) {
					response.put("status", false);
					response.put("msg", "Appointment is already booked.");
					return response;
				}

				Visit visit = new Visit();
				visit.setCampId(campId);
				visit.setLastChgDate(HMSUtil.getCurrentTimeStamp());
				visit.setDepartmentId(departmentId);
				visit.setMmuId(mmuId);
				visit.setPatientId(patientId);
				visit.setVisitStatus("W");
				visit.setVisitFlag("R");
				visit.setVisitDate(HMSUtil.getCurrentTimeStamp());
				// visit.setUserId(userId);
				Long visitId = Long.parseLong(String.valueOf(session.save(visit)));

				// save data to opdPatientDetail
				OpdPatientDetail opddetails = new OpdPatientDetail();
				Calendar calendar = Calendar.getInstance();
				java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

				opddetails.setPatientId(patientId);
				opddetails.setVisitId(visitId);
				opddetails.setHeight(requestData.get("height").toString());
				opddetails.setWeight(requestData.get("weight").toString());
				opddetails.setTemperature(requestData.get("temperature").toString());
				opddetails.setBpSystolic(requestData.get("bp").toString());
				opddetails.setBpDiastolic(requestData.get("bp1").toString());
				opddetails.setPulse(requestData.get("pulse").toString());
				opddetails.setSpo2(requestData.get("spo2").toString());
				opddetails.setBmi(requestData.get("bmi").toString());
				opddetails.setRr(requestData.get("rr").toString());
				opddetails.setOpdDate(ourJavaTimestampObject);
				opddetails.setLastChgDate(ourJavaTimestampObject);

				String smptoms = "";
				for (String symptom : symptoms) {
					smptoms += symptom + ",";
				}
				if (!smptoms.equals("")) {
					smptoms = smptoms.substring(0, smptoms.length() - 1);
				}
				opddetails.setPatientSymptoms(smptoms);
				String result = opdVitalDetails(opddetails, session);
				JSONObject json = new JSONObject(result);
				Long opdPatientDetailId = Long.parseLong(json.get("id").toString());

				PatientSymptom patientSymptoms = (PatientSymptom) session.createCriteria(PatientSymptom.class)
						.add(Restrictions.eq("visitId", visitId)).uniqueResult();

				if (patientSymptoms == null) {
					for (Map<String, Object> map : symptomsId) {
						patientSymptoms = new PatientSymptom();
						patientSymptoms.setVisitId(visitId);
						patientSymptoms.setPatientId(patientId);
						patientSymptoms.setOpdPatientDetailId(opdPatientDetailId);
						patientSymptoms.setCampId(campId);
						patientSymptoms.setMmuId(mmuId);
						patientSymptoms.setSymptomId(Long.parseLong(String.valueOf(map.get("symptomsId"))));

						session.save(patientSymptoms);
					}
				}

				response.put("status", true);
				response.put("msg", "Appointment is booked.");
			}

			tx.commit();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	}

	@SuppressWarnings("unchecked")
	public Boolean getPatient(String name, String mobileNo, Long genderId, Long age) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		Boolean flag = false;
		try {
			Patient patient = (Patient) session.createCriteria(Patient.class)
					.add(Restrictions.eq("patientName", name.trim()).ignoreCase())
					.add(Restrictions.eq("mobileNumber", mobileNo.trim()).ignoreCase())
					.createAlias("masAdministrativeSex", "gender")
					.add(Restrictions.eq("gender.administrativeSexId", genderId)).add(Restrictions.eq("age", age))
					.uniqueResult();

			if (patient != null) {
				flag = true;
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return null;
	}

	public MasCamp getCampDetailForPatient(Long mmuId, Long departmentId) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		MasMMU masMMU = new MasMMU();
		MasCamp masCamp = null;
		try {
			Date dateObj = new Date();
			Time time = HMSUtil.getCurrentSQLTime();
			masMMU.setMmuId(mmuId);
			Date minDate = new Date();
			minDate.setHours(0);
			minDate.setMinutes(0);
			minDate.setSeconds(0);
			// Date maxDate = minAndMaxDate.get("maxDate");
			Date maxDate = HMSUtil.getNextDate(new Date());
			masCamp = (MasCamp) session.createCriteria(MasCamp.class).createAlias("masMMU", "mmu")
					.add(Restrictions.eq("mmu.mmuId", mmuId)).createAlias("masDepartment", "department")
					.add(Restrictions.eq("department.departmentId", departmentId))
					.add(Restrictions.lt("campDate", maxDate)).add(Restrictions.ge("campDate", minDate)).uniqueResult();
			/*
			 * .add(Restrictions.le("start_time",time)) .add(Restrictions.ge("end_time",
			 * time)).uniqueResult();
			 */
			return masCamp;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masCamp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasReligion> getReligionList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasReligion> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(MasReligion.class).add(Restrictions.eq("status", "Y").ignoreCase()).list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasBloodGroup> getBloodGroupList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasBloodGroup> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(MasBloodGroup.class).add(Restrictions.eq("status", "Y").ignoreCase()).list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@Override
	public List<MasIdentificationType> getIdentificationTypeList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasIdentificationType> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(MasIdentificationType.class).add(Restrictions.eq("status", "Y").ignoreCase())
					.list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasLabor> getLabourTyeList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasLabor> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(MasLabor.class).add(Restrictions.eq("status", "Y").ignoreCase()).list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasCamp> getCampDepartment(Long mmuId) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		MasMMU masMMU = new MasMMU();
		List<MasCamp> masCampList = null;
		try {
			Date dateObj = new Date();
			String date = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
			Time time = HMSUtil.getCurrentSQLTime();

			masMMU.setMmuId(mmuId);
			Map<String, Date> minAndMaxDate = HMSUtil.getMinAndMaxDateAgainstInputDate(date);
			Date minDate = new Date();
			minDate.setHours(0);
			minDate.setMinutes(0);
			minDate.setSeconds(0);
			// Date maxDate = minAndMaxDate.get("maxDate");
			Date maxDate = HMSUtil.getNextDate(new Date());
			masCampList = session.createCriteria(MasCamp.class).createAlias("masMMU", "mmu")
					.add(Restrictions.eq("mmu.mmuId", mmuId)).add(Restrictions.le("campDate", maxDate))
					.add(Restrictions.ge("campDate", minDate)).list();
			/*
			 * .add(Restrictions.le("startTime",time)) .add(Restrictions.ge("endTime",
			 * time)).list();
			 */
			return masCampList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masCampList;
	}
	
	

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public Map<String, Object> createPatientAndMakeAppointment(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		
		try {
			Long genderId = Long.parseLong(String.valueOf(requestData.get("genderId")));
			Long userId = Long.parseLong(String.valueOf(requestData.get("userId")));
			Long age = Long.parseLong(String.valueOf(requestData.get("age")));
			String ageFlag = String.valueOf(requestData.get("ageFlag"));
			String patientName = String.valueOf(requestData.get("patientName"));
			String mobileNo = String.valueOf(requestData.get("mobileNo"));
			
			String mobileNoUser = String.valueOf(requestData.get("mobieNo"));
			System.out.print("Patent Image By Mobileno:"+mobileNo);
			
			String ProcessFull = String.valueOf(requestData.get("demo1"));
			
			Date dob = HMSUtil.getDOBFromAge(age.intValue(),ageFlag);
			Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			Long departmentId = Long.parseLong(String.valueOf(requestData.get("departmentId")));
			/*added for profile image*/
			String patientProfileImage =String.valueOf(requestData.get("patientImage"));
            System.out.print("patient Updated ProfileImage:"+patientProfileImage);
			Long campId = Long.parseLong(String.valueOf(requestData.get("campId")));
			List<String> symptoms = (List<String>) requestData.get("symptoms");
			List<Map<String, Object>> symptomsId = (List<Map<String, Object>>) requestData.get("symptomsId");

			MasMMU masMMU = new MasMMU();
			masMMU.setMmuId(mmuId);

			MasDepartment masDepartment = new MasDepartment();
			masDepartment.setDepartmentId(departmentId);

			// check weekly off or CL
			MasCamp camp = (MasCamp) session.get(MasCamp.class, campId);
			if (camp != null) {
				String checkWeeklyOffOrCL = camp.getWeeklyOff();
				if (!checkWeeklyOffOrCL.trim().equalsIgnoreCase("Camp")) {
					response.put("status", false);
					response.put("msg", "Appointment cannot be booked for Weekly Off and CL.");
					return response;
				}
			}
			
			Boolean flag = getPatient(patientName, mobileNo, genderId, age);
			if (flag == null) {
				response.put("status", false);
				response.put("msg", "Something went wrong");
				return response;
			}
			if (flag) {
				response.put("status", false);
				response.put("msg", "Patient is already registered");
				return response;
			}
			
			List<Patient> patientList = session.createCriteria(Patient.class)
					.add(Restrictions.eq("mobileNumber", mobileNo.trim())).list();
			String uhidNo = "";
			if (patientList.size() < 10) {
				uhidNo = mobileNo + "0" + (patientList.size() + 1);
			} else {
				uhidNo = (mobileNo + (patientList.size() + 1));
			}
			Patient patient = new Patient();
			patient.setPatientName(patientName);
			patient.setMobileNumber(mobileNo);
			patient.setDateOfBirth(dob);
			patient.setAge(age);
			patient.setRegDate(new Date());
			MasAdministrativeSex gender = new MasAdministrativeSex();
			gender.setAdministrativeSexId(genderId);
			Users user = new Users();
			user.setUserId(userId);
			patient.setAdministrativeSexId(genderId);
			patient.setUhidNo(uhidNo);
			patient.setCampId(campId);
			//patient.setPatientImage(patientProfileImage);
			Long patientId = Long.parseLong(String.valueOf(session.save(patient)));
			//System.out.println("Mno:"+patient.getMobileNumber());
				//List<Patient>	plist = criteria.list();
				
			String base64String = patientProfileImage;
			 String[] strings = base64String.split(",");
			    String extension;
			    switch (strings[0]) {//check image's extension
			        case "data:image/jpeg;base64":
			            extension = "jpeg";
			            break;
			        case "data:image/png;base64":
			            extension = "png";
			            break;
			        default://should write cases for more images types
			            extension = "jpg";
			            break;
			    }
			    //convert base64 string to binary data
			    String rootPath = environment.getProperty("service.patientImagePath");
			    String ServerImageUrl = environment.getProperty("server.imageUrl");
			    String tempPath = environment.getProperty("server.imgMatch");
				   
			    String imgMatch = HMSUtil.getProperties("adt.properties","IMG_MATCH").trim();
			  
			    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
			    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			    byte[] data = null;
			    		try{
				    data = DatatypeConverter.parseBase64Binary(strings[1]);
				   }catch (Exception e) {
					// TODO: handle exception
				}
			    		List<Patient> plist = new ArrayList<Patient>();
			    		
			    		plist = session.createCriteria(Patient.class)
								.add(Restrictions.eq("mobileNumber", patient.getMobileNumber()))
										.add(Restrictions.neOrIsNotNull("patientImage", null)).list();
						
			    String fileName = patientId+"-"+ sdf1.format(timestamp)+"."+ extension;
			    String path = rootPath +"//"+fileName;
			    File file = new File(path);
			    String completeFilePath = tempPath +"//" + fileName;
			    //System.out.println("plist:"+plist.size() +"fileName :"+patientProfileImage);
			    File fileTemp = new File(completeFilePath);
			  
				List<Patient> list=new ArrayList<Patient>();
				
			    //store data
			    for(Patient p1: plist){
			    	   
			        BufferedImage imgA1 = null;
					BufferedImage imgB1 = null;
					// Try block to check for exception
					if(p1.getPatientImage() != null){
			        try {
			        	try{
			        	  OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileTemp)); 
						        outputStream.write(data);
			        	}catch (Exception e) {
							// TODO: handle exception
						}
			            // Reading file from local directory by
			            // creating object of File class
			            File fileA
			                = new File(rootPath+"//"+p1.getPatientImage());
			            File fileB
			                = new File(completeFilePath);
			  
			            // Reading files
			            imgA1 = ImageIO.read(fileA);
			            imgB1 = ImageIO.read(fileB);
			        }
			  
			        // Catch block to check for exceptions
			        catch (IOException e) {
			            // Display the exceptions on console
			            //System.out.println(e);
			        }
			  
			        // Assigning dimensions to image
			        int width22 = 0 ;
			        int height22 = 0 ;
			        int width11 = 0;
			        int height11 =0;
			     
			        // Checking whether the images are of same size or
			        // not
			        if ((width11 != width22) || (height11 != height22)){
			  
			            // Display message straightaway
			            //System.out.println("Error: Images dimensions"
			                              // + " mismatch");
			        response.put("status", false);
					response.put("msg", "Error: Images dimensions"
                               + " mismatch");
					//return response;
			        }else {
			  
			            // By now, images are of same size
			        	if(imgA1 !=null){
			        		 width11 = imgA1.getWidth();
						      height11 = imgA1.getHeight();
			        	}
			        	 if(imgB1 != null){
			        		 width22 = imgB1.getWidth();
				        	 height22 = imgB1.getHeight();
			        	 }
			        	
			            long difference = 0;
			            // treating images likely 2D matrix
			           // Outer loop for rows(height)
			          if(imgA1 !=null && imgB1!=null){
			            for (int y = 0; y < height11; y++) {
			  
			                // Inner loop for columns(width)
			                for (int x = 0; x < width11; x++) {
			  
			                    int rgbA = imgA1.getRGB(x, y);
			                    int rgbB = imgB1.getRGB(x, y);
			                    int redA = (rgbA >> 16) & 0xff;
			                    int greenA = (rgbA >> 8) & 0xff;
			                    int blueA = (rgbA)&0xff;
			                    int redB = (rgbB >> 16) & 0xff;
			                    int greenB = (rgbB >> 8) & 0xff;
			                    int blueB = (rgbB)&0xff;
			  
			                    difference += Math.abs(redA - redB);
			                    difference += Math.abs(greenA - greenB);
			                    difference += Math.abs(blueA - blueB);
			                }
			            }
			          
			          }
			       
			            double total_pixels = width11 * height11 * 3;
			            double avg_different_pixels
			                = difference / total_pixels;
			  
			            // There are 255 values of pixels in total
			            double percentage
			                = (avg_different_pixels / 255) * 100;
			            percentage = 100 - percentage;
			            // Lastly print the difference percentage
			            //System.out.println("Difference Percentage-->"
			                              // + percentage);
			            DecimalFormat decimalFormat = new DecimalFormat("#");
			            System.out.println("Difference Percentage Dec-->" +decimalFormat.format(percentage));
			            String inPhotoBase64String = "";
			            String ServerImageUrl1 = environment.getProperty("server.imageUrl");
			            if(p1.getPatientImage() != null){
							String imageFolderPathDirctory = ServerImageUrl1 ;
							String completePath = imageFolderPathDirctory +"/"+ p1.getPatientImage();
							//System.out.println("completePath"+completePath);
							File f = new File(completePath);
							if(f.exists()) {
								inPhotoBase64String = encodeFileToBase64Binary(f);
							//	System.out.println("inPhotoBase64String:::"+inPhotoBase64String);
							}
							//System.out.println(inPhotoBase64String);
					
						}
						int newPercent = Integer.parseInt(decimalFormat.format(percentage));
						if(newPercent > Integer.parseInt(imgMatch)){
						 Patient patient2 = new Patient();
						 patient2.setPatientName(p1.getPatientName());
						 patient2.setRegDate(p1.getRegDate());
						 patient2.setPatientImage("data:image/png;base64,"+inPhotoBase64String);
						 list.add(patient2);
						 System.out.print("list count:"+list.size());
						}
			        }
					}
			    }
			    
			    // new final store
				
			    int newPercent =0;
			    System.out.print("ProcessFull"+ProcessFull);
			    if(ProcessFull != ""){
			    	  System.out.print("ProcessFull IF"+ProcessFull);
			    	if(data!=null){
					    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
					        outputStream.write(data);
					        //System.out.println("PatientId"+patientId);
					        patient.setPatientImage(fileName);
					        session.update(patient);
					        
					        PatientDataUpload previousHistory =new PatientDataUpload();
					        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
					        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
					        if(patientId!=null){
					        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
									.add(Restrictions.eq("patientId", patientId)).list();
					        //System.out.println("list:"+patientDataUploadsList);
		                     for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
		                    	 patientDataUpload.setStatus("N");
		                    	 session.update(patientDataUpload);
							    }
					        }
					        PatientDataUpload history =new PatientDataUpload();
					        history.setPatientId(patientId);
					        history.setFileData(fileName);
					        history.setStatus("Y");
					        history.setLastchgBy(userId);
					        history.setLastChgDate(timestamp);
					        session.save(history);
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					    }
			    }else{
			    
			    	  System.out.print("ProcessFull ELSE"+ProcessFull);
			    
						if(path !=null || !path.isEmpty() && patientProfileImage !=null){
							if(plist.size() ==0){
							if(data!=null){
							    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
							        outputStream.write(data);
							        //System.out.println("PatientId"+patientId);
							        patient.setPatientImage(fileName);
							        session.update(patient);
							        
							        PatientDataUpload previousHistory =new PatientDataUpload();
							        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
							        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
							        if(patientId!=null){
							        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
											.add(Restrictions.eq("patientId", patientId)).list();
							        //System.out.println("list:"+patientDataUploadsList);
				                     for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
				                    	 patientDataUpload.setStatus("N");
				                    	 session.update(patientDataUpload);
									    }
							        }
							        PatientDataUpload history =new PatientDataUpload();
							        history.setPatientId(patientId);
							        history.setFileData(fileName);
							        history.setStatus("Y");
							        history.setLastchgBy(userId);
							        history.setLastChgDate(timestamp);
							        session.save(history);
							    } catch (IOException e) {
							        e.printStackTrace();
							    }
							    }
							}else if(patientProfileImage !=null && ! patientProfileImage.isEmpty()  && patientProfileImage.startsWith("data")){
						    //Temp image store
							
								//List<Patient> list=new ArrayList<Patient>();
						   
			                for(Patient p: plist){
								
							        BufferedImage imgA = null;
									BufferedImage imgB = null;
									// Try block to check for exception
									if(p.getPatientImage() != null){
							        try {
							        	try{
							        	  OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileTemp)); 
										        outputStream.write(data);
							        	}catch (Exception e) {
											// TODO: handle exception
										}
							            // Reading file from local directory by
							            // creating object of File class
							            File fileA
							                = new File(rootPath+"//"+p.getPatientImage());
							            File fileB
							                = new File(completeFilePath);
							  
							            // Reading files
							            imgA = ImageIO.read(fileA);
							            imgB = ImageIO.read(fileB);
							        }
							  
							        // Catch block to check for exceptions
							        catch (IOException e) {
							            // Display the exceptions on console
							            //System.out.println(e);
							        }
							  
							        // Assigning dimensions to image
							        int width2 = 0 ;
							        int height2 = 0 ;
							        int width1 = 0;
							        int height1 =0;
							     
							        // Checking whether the images are of same size or
							        // not
							        if ((width1 != width2) || (height1 != height2)){
							  
							            // Display message straightaway
							            //System.out.println("Error: Images dimensions"
							                              // + " mismatch");
							        response.put("status", false);
									response.put("msg", "Error: Images dimensions"
				                               + " mismatch");
									//return response;
							        }else {
							  
							            // By now, images are of same size
							        	if(imgA !=null){
							        		 width1 = imgA.getWidth();
										      height1 = imgA.getHeight();
							        	}
							        	 if(imgB != null){
							        		 width2 = imgB.getWidth();
								        	 height2 = imgB.getHeight();
							        	 }
							        	
							            long difference = 0;
							            // treating images likely 2D matrix
							           // Outer loop for rows(height)
							          if(imgA !=null && imgB!=null){
							            for (int y = 0; y < height1; y++) {
							  
							                // Inner loop for columns(width)
							                for (int x = 0; x < width1; x++) {
							  
							                    int rgbA = imgA.getRGB(x, y);
							                    int rgbB = imgB.getRGB(x, y);
							                    int redA = (rgbA >> 16) & 0xff;
							                    int greenA = (rgbA >> 8) & 0xff;
							                    int blueA = (rgbA)&0xff;
							                    int redB = (rgbB >> 16) & 0xff;
							                    int greenB = (rgbB >> 8) & 0xff;
							                    int blueB = (rgbB)&0xff;
							  
							                    difference += Math.abs(redA - redB);
							                    difference += Math.abs(greenA - greenB);
							                    difference += Math.abs(blueA - blueB);
							                }
							            }
							          
							          }
							       
							            double total_pixels = width1 * height1 * 3;
							            double avg_different_pixels
							                = difference / total_pixels;
							  
							            // There are 255 values of pixels in total
							            double percentage
							                = (avg_different_pixels / 255) * 100;
							            percentage = 100 - percentage;
							            // Lastly print the difference percentage
							            //System.out.println("Difference Percentage-->"
							                              // + percentage);
							            DecimalFormat decimalFormat = new DecimalFormat("#");
							            //System.out.println("Difference Percentage Dec-->" +decimalFormat.format(percentage));
							            String inPhotoBase64String = "";
							            String ServerImageUrl1 = environment.getProperty("server.imageUrl");
							            if(p.getPatientImage() != null){
											String imageFolderPathDirctory = ServerImageUrl1 ;
											String completePath = imageFolderPathDirctory +"/"+ p.getPatientImage();
											//System.out.println("completePath"+completePath);
											File f = new File(completePath);
											if(f.exists()) {
												inPhotoBase64String = encodeFileToBase64Binary(f);
											//	System.out.println("inPhotoBase64String:::"+inPhotoBase64String);
											}
											//System.out.println(inPhotoBase64String);
									
										}
										 newPercent = Integer.parseInt(decimalFormat.format(percentage));
//										 
//										 Patient patient2 = new Patient();
//										 patient2.setPatientName(p.getPatientName());
//										 patient2.setRegDate(p.getRegDate());
//										 patient2.setPatientImage("data:image/png;base64,"+inPhotoBase64String);
//										 list.add(patient2);
									
									/*
									 * System.out.println("Percentage"+newPercent +"###"+p.getPatientName());
									 * if(newPercent > Integer.parseInt(imgMatch)){ response.put("listImg", list);
									 * 
									 * response.put("match", "Match"); response.put("status", false);
									 * response.put("msg",
									 * "Duplicate patent registration photo is found for this mobile number. To verify the existing patient photo, click on Registration History button. Do you want to continue the registration/ appointment?"
									 * );
									 * 
									 * return response; } else{
									 */
			                        	   if(data!=null){
			                        		   System.out.print("else");
			                   			    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			                   			        outputStream.write(data);
			                   			        //System.out.println("PatientId TRY"+patientId);
			                   			     //   patient.setPatientImage(ServerImageUrl+fileName);
			                   			        patient.setPatientImage(fileName);
			                   			        session.update(patient);
			                   			        // previous history mark status to N
			                   			        PatientDataUpload previousHistory =new PatientDataUpload();
			                   			        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
			                   			        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
			                   			        if(patientId!=null){
			                   			        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
			                   							.add(Restrictions.eq("patientId", patientId)).list();
			                   			        //System.out.println("list:"+patientDataUploadsList);
			                                        for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
			                                       	 patientDataUpload.setStatus("N");
			                                       	 session.update(patientDataUpload);
			                                       	System.out.print("history update");
			                   					    }
			                   			       
			                   			        }
			                   			        PatientDataUpload history =new PatientDataUpload();
			                   			        history.setPatientId(patientId);
			                   			        history.setFileData(fileName);
			                   			        history.setStatus("Y");
			                   			        history.setLastchgBy(userId);
			                   			        history.setLastChgDate(timestamp);
			                   			        session.save(history);
			                   			        System.out.print("history save");
			                   			    } catch (IOException e) {
			                   			        e.printStackTrace();
			                   			     System.out.print("catch"+e.getMessage());
			                   			    }
			                   			    }
										//}
										//response.put("listImg", list);
										//return response;
										
							        }
								}
							}
			               // for(String match:list) { 
			               // response.put("msg", "Duplicate Image Found :" +list  );
			                //}
                     	  // return response;
						}else if(patientProfileImage == null){
							
						}
							
					}
			    }
						
		      // }  else if(patientList.size() ==0){
		    	   
			    /*if(data!=null){
			    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			        outputStream.write(data);
			        System.out.println("PatientId"+patientId);
			        patient.setPatientImage(fileName);
			        session.update(patient);
			        
			        PatientDataUpload previousHistory =new PatientDataUpload();
			        List<PatientDataUpload> patientDataUploadsList = new ArrayList<PatientDataUpload>();
			        // session = getHibernateUtils.getHibernateUtlis().OpenSession();
			        if(patientId!=null){
			        patientDataUploadsList = session.createCriteria(PatientDataUpload.class).add(Restrictions.eq("status", "Y").ignoreCase())
							.add(Restrictions.eq("patientId", patientId)).list();
			        System.out.println("list:"+patientDataUploadsList);
                     for (PatientDataUpload patientDataUpload : patientDataUploadsList) {
                    	 patientDataUpload.setStatus("N");
                    	 session.update(patientDataUpload);
					    }
			        }
			        PatientDataUpload history =new PatientDataUpload();
			        history.setPatientId(patientId);
			        history.setFileData(fileName);
			        history.setStatus("Y");
			        history.setLastchgBy(userId);
			        history.setLastChgDate(timestamp);
			        session.save(history);
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    }*/
			    	
			    	
			    	
		      // }
			// get camp id
			/*
			 * MasCamp masCamp = getCampDetailForPatient(mmuId,departmentId); Long campId =
			 * null; if(masCamp != null) { campId = masCamp.getCampId(); }else {
			 * response.put("status", false); response.put("msg", "Camp is not configured");
			 * return response; }
			 */

			// getAppointment

			Visit visit = new Visit();
			visit.setCampId(campId);
			visit.setLastChgDate(HMSUtil.getCurrentTimeStamp());
			visit.setDepartmentId(departmentId);
			visit.setMmuId(mmuId);
			visit.setPatientId(patientId);
			visit.setVisitStatus("W");
			visit.setVisitFlag("R");
			visit.setVisitDate(HMSUtil.getCurrentTimeStamp());
			visit.setUser(user);
			session.save(visit);
			Long visitId = Long.parseLong(String.valueOf(session.save(visit)));

			// save data to opdPatientDetail
			OpdPatientDetail opddetails = new OpdPatientDetail();
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

			opddetails.setPatientId(patientId);
			opddetails.setVisitId(visitId);
			opddetails.setHeight(requestData.get("height").toString());
			opddetails.setWeight(requestData.get("weight").toString());
			opddetails.setTemperature(requestData.get("temperature").toString());
			opddetails.setBpSystolic(requestData.get("bp").toString());
			opddetails.setBpDiastolic(requestData.get("bp1").toString());
			opddetails.setPulse(requestData.get("pulse").toString());
			opddetails.setSpo2(requestData.get("spo2").toString());
			opddetails.setBmi(requestData.get("bmi").toString());
			opddetails.setRr(requestData.get("rr").toString());
			opddetails.setOpdDate(ourJavaTimestampObject);
			opddetails.setLastChgDate(ourJavaTimestampObject);
			String smptoms = "";
			for (String symptom : symptoms) {
				smptoms += symptom + ",";
			}
			if (!smptoms.equals("")) {
				smptoms = smptoms.substring(0, smptoms.length() - 1);
			}
			opddetails.setPatientSymptoms(smptoms);
			String result = opdVitalDetails(opddetails, session);
			JSONObject json = new JSONObject(result);
			Long opdPatientDetailId = Long.parseLong(json.get("id").toString());

			PatientSymptom patientSymptoms = (PatientSymptom) session.createCriteria(PatientSymptom.class)
					.add(Restrictions.eq("visitId", visitId)).uniqueResult();

			if (patientSymptoms == null) {
				for (Map<String, Object> map : symptomsId) {
					patientSymptoms = new PatientSymptom();
					patientSymptoms.setVisitId(visitId);
					patientSymptoms.setPatientId(patientId);
					patientSymptoms.setOpdPatientDetailId(opdPatientDetailId);
					patientSymptoms.setCampId(campId);
					patientSymptoms.setMmuId(mmuId);
					patientSymptoms.setSymptomId(Long.parseLong(String.valueOf(map.get("symptomsId"))));

					session.save(patientSymptoms);
				}
			}

			response.put("status", true);
			response.put("msg", "Patient is registered and appointment is booked");
			tx.commit();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MMUDepartment> getMMUDepartment(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MMUDepartment> list = new ArrayList<>();
		Long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(MMUDepartment.class).add(Restrictions.eq("mmuId", mmuId))
					.add(Restrictions.eq("status", "Y").ignoreCase()).list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return null;
	}

	public String opdVitalDetails(OpdPatientDetail ob, Session session) {
		JSONObject json = new JSONObject();
		String Result = null;
		Long opdPatientDetailId = null;
		try {
			// Session session=sessionFactory.getCurrentSession();
			// Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdPatientDetail.class);
			cr.add(Restrictions.eq("visitId", ob.getVisitId()));
			OpdPatientDetail list = (OpdPatientDetail) cr.uniqueResult();
			// Transaction t = session.beginTransaction();

			if (list != null) {
				opdPatientDetailId = list.getOpdPatientDetailsId();
				list.setHeight(ob.getHeight());
				list.setWeight(ob.getWeight());
				list.setTemperature(ob.getTemperature());
				list.setBpDiastolic(ob.getBpDiastolic());
				list.setBpSystolic(ob.getBpSystolic());
				list.setPulse(ob.getPulse());
				list.setSpo2(ob.getSpo2());
				list.setBmi(ob.getBmi());
				list.setRr(ob.getRr());
				list.setPatientSymptoms(ob.getPatientSymptoms());
				// list.setOpdDate(ob.getOpdDate());
				session.update(list);
				Long visitId = ob.getVisitId();
				/*
				 * if (visitId != null) { Visit visit = (Visit) session.get(Visit.class,
				 * visitId); if (visit != null) { visit.setVisitStatus("p");
				 * session.update(visit); } }
				 */
				// t.commit();
				Result = "success";

			} else {
				opdPatientDetailId = Long.parseLong(session.save(ob).toString());
				Long visitId = ob.getVisitId();
				/*
				 * if (visitId != null) { Visit visit = (Visit) session.get(Visit.class,
				 * visitId); if (visit != null) { visit.setVisitStatus("p");
				 * session.update(visit); } }
				 */

				// t.commit();
				Result = "success";
				json.put("id", opdPatientDetailId);
			}
		} catch (Exception e) {
			Result = e.getMessage();
			e.printStackTrace();
		}
		Result = "fail";
		json.put("id", opdPatientDetailId);
		return json.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPatientList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Patient> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(Patient.class);
			Date todayDate = HMSUtil.getTodayFormattedDate();
			int pageNo = Integer.parseInt(String.valueOf(requestData.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			if ((requestData.get("mobileNo") != null) && !String.valueOf(requestData.get("mobileNo")).equals("")
					&& !String.valueOf(requestData.get("mobileNo")).equals("null")) {
				criteria.add(Restrictions.eq("mobileNumber", String.valueOf(requestData.get("mobileNo"))));
			}

			if ((requestData.get("patientName") != null) && !String.valueOf(requestData.get("patientName")).equals("")
					&& !String.valueOf(requestData.get("patientName")).equals("null")) {
				// criteria.add(Restrictions.eq("patientName",
				// String.valueOf(requestData.get("patientName"))));
				String patientName = "%" + String.valueOf(requestData.get("patientName")) + "%";
				criteria.add(Restrictions.like("patientName", patientName).ignoreCase());
			}

			if ((requestData.get("uhidNo") != null) && !String.valueOf(requestData.get("uhidNo")).equals("")
					&& !String.valueOf(requestData.get("uhidNo")).equals("null")) {
				criteria.add(Restrictions.eq("uhidNo", String.valueOf(requestData.get("uhidNo"))));
			}

			if ((requestData.get("appointmentDate") != null)
					&& !String.valueOf(requestData.get("appointmentDate")).equals("")
					&& !String.valueOf(requestData.get("appointmentDate")).equals("null")) {
				Date appDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(requestData.get("appointmentDate")));
				Date nextDate = HMSUtil.getNextDate(appDate);
				criteria.createAlias("visit", "visit");
				criteria.add(Restrictions.ge("visit.visitDate", appDate))
						.add(Restrictions.lt("visit.visitDate", nextDate));

			}

			list = criteria.list();
			count = list.size();

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			map.put("count", count);
			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public Map<String, Object> getOnlinePatientList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Visit> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			int pageNo = Integer.parseInt(String.valueOf(requestData.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			Date today = HMSUtil.getTodayFormattedDate();
			Date nextDate = HMSUtil.getNextDate(new Date());
			Criteria criteria = session.createCriteria(Visit.class)
					.add(Restrictions.eq("visitStatus", "P").ignoreCase())
					.add(Restrictions.eq("visitFlag", "N").ignoreCase()).add(Restrictions.ge("visitDate", today))
					.add(Restrictions.le("visitDate", nextDate)).createAlias("patient", "patient");
			
			if ((requestData.get("mmuId") != null) && !String.valueOf(requestData.get("mmuId")).equals("")
					&& !String.valueOf(requestData.get("mmuId")).equals("null") && !String.valueOf(requestData.get("mmuId")).equals("0")) {

				criteria.add(Restrictions.eq("mmuId", Long.parseLong(String.valueOf(requestData.get("mmuId")))));
			}

			if ((requestData.get("mobileNo") != null) && !String.valueOf(requestData.get("mobileNo")).equals("")
					&& !String.valueOf(requestData.get("mobileNo")).equals("null")) {

				criteria.add(Restrictions.eq("patient.mobileNumber", String.valueOf(requestData.get("mobileNo"))));
			}

			if ((requestData.get("patientName") != null) && !String.valueOf(requestData.get("patientName")).equals("")
					&& !String.valueOf(requestData.get("patientName")).equals("null")) {
				// criteria.add(Restrictions.eq("patientName",
				// String.valueOf(requestData.get("patientName"))));
				String patientName = "%" + String.valueOf(requestData.get("patientName")) + "%";
				criteria.add(Restrictions.like("patient.patientName", patientName).ignoreCase());
			}
			list = criteria.list();
			map.put("count", list.size());

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();
			map.put("list", list);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public Visit getPatientDataBasedOnVisit(Long visitId) {
		Visit visit = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			visit = (Visit) session.get(Visit.class, visitId);
			return visit;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return visit;
	}

	@Override
	public String opdVitalDetails(OpdPatientDetail ob) {
		String Result = null;

		try {
			// Session session=sessionFactory.getCurrentSession();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdPatientDetail.class);
			cr.add(Restrictions.eq("visitId", ob.getVisitId()));
			OpdPatientDetail list = (OpdPatientDetail) cr.uniqueResult();
			Transaction t = session.beginTransaction();
			if (list != null) {

				list.setHeight(ob.getHeight());
				list.setWeight(ob.getWeight());
				list.setTemperature(ob.getTemperature());
				list.setBpDiastolic(ob.getBpDiastolic());
				list.setBpSystolic(ob.getBpSystolic());
				list.setPulse(ob.getPulse());
				list.setSpo2(ob.getSpo2());
				list.setBmi(ob.getBmi());
				list.setRr(ob.getRr());
				// list.setOpdDate(ob.getOpdDate());
				session.update(list);
				Long visitId = ob.getVisitId();
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if (visit != null) {
						visit.setVisitStatus("w");
						session.update(visit);
					}
				}
				t.commit();
				Result = "200";

			} else {
				session.save(ob);
				Long visitId = ob.getVisitId();
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if (visit != null) {
						visit.setVisitStatus("w");
						session.update(visit);
					}
				}

				t.commit();
				Result = "200";
			}
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			Result = e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return Result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasWard> getWardListWithoutCity(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasWard> list = new ArrayList<>();
		try {
			Criteria criteria=null;
			if(requestData.containsKey("cityId"))
			{
				if(requestData.get("cityId")!=null && !requestData.get("cityId").equals("null"))
				{	
				Long cityId=Long.parseLong(requestData.get("cityId").toString());
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				criteria = session.createCriteria(MasWard.class).add(Restrictions.eq("cityId",cityId))
						.add(Restrictions.eq("status", "Y").ignoreCase());
				}
				else
				{
					Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
					criteria = session.createCriteria(MasWard.class).add(Restrictions.eq("status", "Y").ignoreCase());
				}
			}
			else
			{
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				criteria = session.createCriteria(MasWard.class).add(Restrictions.eq("status", "Y").ignoreCase());
			}
			
			if(requestData.get("wardName") != null && !String.valueOf(requestData.get("wardName")).equals("") && !String.valueOf(requestData.get("wardName")).equals("null")) {
				String wardName = "%"+String.valueOf(requestData.get("wardName"))+"%";
				criteria = criteria.add(Restrictions.ilike("wardName", wardName));
			}
			
			return criteria.list();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@Override
	public String opdVitalDetails(HashMap<String, Object> payload) {
		String Result = null;

		try {
			// Session session=sessionFactory.getCurrentSession();
			Long opdPatientDetailId = null;

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

			OpdPatientDetail opddetails = new OpdPatientDetail();
			opddetails.setPatientId(Long.parseLong(payload.get("patientId").toString()));
			opddetails.setVisitId(Long.parseLong(payload.get("visitId").toString()));
			opddetails.setCampId(Long.parseLong(payload.get("campId").toString()));
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
			List<String> symptoms = (List<String>) payload.get("symptoms");
			List<Map<String, Object>> symptomsId = (List<Map<String, Object>>) payload.get("symptomsId");
			Long mmuId = Long.parseLong(payload.get("mmuId").toString());

			String smptoms = "";
			for (String symptom : symptoms) {
				smptoms += symptom + ",";
			}
			if (!smptoms.equals("")) {
				smptoms = smptoms.substring(0, smptoms.length() - 1);
			}
			opddetails.setPatientSymptoms(smptoms);

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdPatientDetail.class);
			cr.add(Restrictions.eq("visitId", opddetails.getVisitId()));
			OpdPatientDetail list = (OpdPatientDetail) cr.uniqueResult();
			Transaction t = session.beginTransaction();
			if (list != null) {
				opdPatientDetailId = list.getOpdPatientDetailsId();
				list.setHeight(opddetails.getHeight());
				list.setWeight(opddetails.getWeight());
				list.setTemperature(opddetails.getTemperature());
				list.setBpDiastolic(opddetails.getBpDiastolic());
				list.setBpSystolic(opddetails.getBpSystolic());
				list.setPulse(opddetails.getPulse());
				list.setSpo2(opddetails.getSpo2());
				list.setBmi(opddetails.getBmi());
				list.setRr(opddetails.getRr());
				list.setPatientSymptoms(smptoms);
				// list.setOpdDate(ob.getOpdDate());
				session.update(list);
				Long visitId = opddetails.getVisitId();
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if (visit != null) {
						visit.setVisitStatus("w");
						session.update(visit);
					}
				}

			} else {
				opdPatientDetailId = Long.parseLong((session.save(opddetails).toString()));
				Long visitId = opddetails.getVisitId();
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if (visit != null) {
						visit.setVisitStatus("w");
						session.update(visit);
					}
				}

			}

			PatientSymptom patientSymptoms = (PatientSymptom) session.createCriteria(PatientSymptom.class)
					.add(Restrictions.eq("visitId", opddetails.getVisitId())).uniqueResult();

			if (patientSymptoms == null) {
				for (Map<String, Object> map : symptomsId) {
					patientSymptoms = new PatientSymptom();
					patientSymptoms.setVisitId(opddetails.getVisitId());
					patientSymptoms.setPatientId(opddetails.getPatientId());
					patientSymptoms.setOpdPatientDetailId(opdPatientDetailId);
					patientSymptoms.setCampId(opddetails.getCampId());
					patientSymptoms.setMmuId(mmuId);
					patientSymptoms.setSymptomId(Long.parseLong(String.valueOf(map.get("symptomsId"))));

					session.save(patientSymptoms);
				}
			}

			t.commit();
			Result = "200";
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			Result = e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return Result;
	}

	@Override
	public List<MasRelation> getRelationList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasRelation> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRelation.class)
					.add(Restrictions.eq("status", "Y").ignoreCase());

			list = criteria.list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	public String sendSMS(String mobile, String messageVal) {

		try {
	        String getMobile=mobile;
	        String message=messageVal;
			//final String uri = "https://sms.weblinto.com/smsapi/index?key=2614C35C9E9BDF&campaign=689&routeid=6&type=text&contacts="+getMobile+"&senderid=VESIPL&msg=Dear "+message+" -VESIPL";
			final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+getMobile+"&from=CGMMSY&msg="+message+"";
			MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<String, String>();
			RestTemplate restTemplate = new RestTemplate();
			String responseObject = restTemplate.postForObject(uri, requestHeaders, String.class);
			
			//System.out.println(responseObject.toString());
			//System.out.println("SMS send succefully");
			return responseObject;
		} catch (Exception e) {
			
			return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
		}
	}

	@Override
	public MasMMU getCityIdAndName(Map<String, Object> requestData) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long mmuId = Long.parseLong(String.valueOf(requestData.get("mmuId")));
			MasMMU masMMU = (MasMMU) session.get(MasMMU.class, mmuId);

			return masMMU;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return null;
	}

	@Override
	public Map<String, Object> saveLabourRegistration(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			Labour labour = new Labour();
			if (requestData.get("rationCardCheck") != null && !String.valueOf(requestData.get("rationCardCheck")).equals("")
					&& !String.valueOf(requestData.get("rationCardCheck")).equals("null")) {
				String rationCardCheck = String.valueOf(requestData.get("rationCardCheck"));
				labour.setRationCard(rationCardCheck);
			}
			if (requestData.get("rationCardNo") != null && !String.valueOf(requestData.get("rationCardNo")).equals("")
					&& !String.valueOf(requestData.get("rationCardNo")).equals("null")) {
				String rationCardNo = String.valueOf(requestData.get("rationCardNo"));
				labour.setRationCardNo(rationCardNo);
			}
			if (requestData.get("aadharCardCheck") != null && !String.valueOf(requestData.get("aadharCardCheck")).equals("")
					&& !String.valueOf(requestData.get("aadharCardCheck")).equals("null")) {
				String aadharCardCheck = String.valueOf(requestData.get("aadharCardCheck"));
				labour.setAadhaarCard(aadharCardCheck);
			}
			if (requestData.get("aadharCardNo") != null && !String.valueOf(requestData.get("aadharCardNo")).equals("")
					&& !String.valueOf(requestData.get("aadharCardNo")).equals("null")) {
				String aadharCardNo = String.valueOf(requestData.get("aadharCardNo"));
				labour.setAadhaarCardNo(aadharCardNo);
			}
			if (requestData.get("workerNameENG") != null && !String.valueOf(requestData.get("workerNameENG")).equals("")
					&& !String.valueOf(requestData.get("workerNameENG")).equals("null")) {
				String workerNameENG = String.valueOf(requestData.get("workerNameENG"));
				labour.setLabourEngName(workerNameENG);
			}
			if (requestData.get("workerNameHIN") != null && !String.valueOf(requestData.get("workerNameHIN")).equals("")
					&& !String.valueOf(requestData.get("workerNameHIN")).equals("null")) {
				String workerNameHIN = String.valueOf(requestData.get("workerNameHIN"));
				labour.setLabourHinName(workerNameHIN);
			}
			if (requestData.get("fatherOrHusband") != null && !String.valueOf(requestData.get("fatherOrHusband")).equals("")
					&& !String.valueOf(requestData.get("fatherOrHusband")).equals("null")) {
				String fatherOrHusband = String.valueOf(requestData.get("fatherOrHusband"));
				//to do implement
			}
			if (requestData.get("fatherOrHusbandName") != null && !String.valueOf(requestData.get("fatherOrHusbandName")).equals("")
					&& !String.valueOf(requestData.get("fatherOrHusbandName")).equals("null")) {
				String fatherOrHusbandName = String.valueOf(requestData.get("fatherOrHusbandName"));
				labour.setFatherName(fatherOrHusbandName);
			}
			if (requestData.get("motherName") != null && !String.valueOf(requestData.get("motherName")).equals("")
					&& !String.valueOf(requestData.get("motherName")).equals("null")) {
				String motherName = String.valueOf(requestData.get("motherName"));
				labour.setMotherName(motherName);
			}
			if (requestData.get("dob") != null && !String.valueOf(requestData.get("dob")).equals("")
					&& !String.valueOf(requestData.get("dob")).equals("null")) {
				Date dob = HMSUtil.convertStringDateToUtilDateForDatabase(String.valueOf(requestData.get("dob")));
				labour.setDateOfBirth(dob);
			}
			if (requestData.get("genderId") != null && !String.valueOf(requestData.get("genderId")).equals("")
					&& !String.valueOf(requestData.get("genderId")).equals("null")) {
				Long genderId = Long.parseLong(String.valueOf(requestData.get("genderId")));
				labour.setAdministrativeSexId(genderId);
			}
			if (requestData.get("castId") != null && !String.valueOf(requestData.get("castId")).equals("")
					&& !String.valueOf(requestData.get("castId")).equals("null")) {
				String castId = String.valueOf(requestData.get("castId"));
				labour.setCasteId(castId);
			}
			if (requestData.get("subCast") != null && !String.valueOf(requestData.get("subCast")).equals("")
					&& !String.valueOf(requestData.get("subCast")).equals("null")) {
				String subCast = String.valueOf(requestData.get("subCast"));
				labour.setSubCasteId(subCast);
			}
			if (requestData.get("mobileNo") != null && !String.valueOf(requestData.get("mobileNo")).equals("")
					&& !String.valueOf(requestData.get("mobileNo")).equals("null")) {
				String mobileNo = String.valueOf(requestData.get("mobileNo"));
				labour.setMobileNumber(mobileNo);
			}
			if (requestData.get("marritalStatus") != null && !String.valueOf(requestData.get("marritalStatus")).equals("")
					&& !String.valueOf(requestData.get("marritalStatus")).equals("null")) {
				String marritalStatus = String.valueOf(requestData.get("marritalStatus"));
				labour.setMaritalStatusId(marritalStatus);
			}
			//end of 1st section
			
			//Permanent address
			
			if (requestData.get("districtId") != null && !String.valueOf(requestData.get("districtId")).equals("")
					&& !String.valueOf(requestData.get("districtId")).equals("null")) {
				Long districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));
				labour.setParDistrictId(districtId);
			}
			if (requestData.get("ruralOrUrban") != null && !String.valueOf(requestData.get("ruralOrUrban")).equals("")
					&& !String.valueOf(requestData.get("ruralOrUrban")).equals("null")) {
				String ruralOrUrban = String.valueOf(requestData.get("ruralOrUrban"));
				labour.setParRuralUrban(ruralOrUrban);
			}
			if (requestData.get("street") != null && !String.valueOf(requestData.get("street")).equals("")
					&& !String.valueOf(requestData.get("street")).equals("null")) {
				String street = String.valueOf(requestData.get("street"));
				labour.setParStreet(street);
			}
			
			String assembly = String.valueOf(requestData.get("assembly"));
			labour.setParAssembly(assembly);
			String block = String.valueOf(requestData.get("block"));
			labour.setParBlock(block);
			String panchayat = String.valueOf(requestData.get("panchayat"));
			labour.setParPanchayat(panchayat);
			String gram = String.valueOf(requestData.get("gram"));
			labour.setParGram(gram);
			String houseNo = String.valueOf(requestData.get("houseNo"));
			labour.setParHouserNo(houseNo);
			Integer pin = Integer.parseInt(String.valueOf(requestData.get("pin")));
			labour.setParPincode(pin);
			
			//current address
			
			Long districtId2 = Long.parseLong(String.valueOf(requestData.get("districtId2")));
			labour.setDistrictId(districtId2);
			
			String ruralOrUrban2 = String.valueOf(requestData.get("ruralOrUrban2"));
			labour.setRuralUrban(ruralOrUrban2);
			
			String assembly2 = String.valueOf(requestData.get("assembly2"));
			labour.setAssembly(assembly2);
			String block2 = String.valueOf(requestData.get("block2"));
			labour.setBlock(block2);
			String panchayat2 = String.valueOf(requestData.get("panchayat2"));
			labour.setPanchayat(panchayat2);
			//labour.set
			
			String gram2 = String.valueOf(requestData.get("gram2"));
			labour.setGram(gram2);
			String street2 = String.valueOf(requestData.get("street2"));
			labour.setStreet(street2);
			
			String houseNo2 = String.valueOf(requestData.get("houseNo2"));
			labour.setHouserNo(houseNo2);
			Integer pin2 = Integer.parseInt(String.valueOf(requestData.get("pin2")));
			labour.setPincode(pin2);
			
			String bankAccountCheck = String.valueOf(requestData.get("bankAccountCheck"));
			labour.setBankFlag(bankAccountCheck);
			
			String bankName = String.valueOf(requestData.get("bankName"));
			labour.setBankName(bankName);
			
			String branchName = String.valueOf(requestData.get("branchName"));
			labour.setBankBranchName(branchName);
			
			String accountNo = String.valueOf(requestData.get("accountNo"));
			labour.setAccountNo(accountNo);
			
			String ifscCode = String.valueOf(requestData.get("ifscCode"));
			labour.setIfscNo(ifscCode);
			
			String unNoCheck = String.valueOf(requestData.get("unNoCheck"));
			labour.setUnFlag(unNoCheck);
			
			String unNo = String.valueOf(requestData.get("unNo"));
			labour.setUnNo(unNo); 
			
			String healthStatus = String.valueOf(requestData.get("healthStatus"));
			labour.setHealthStatus(healthStatus); 
			
			String typeOfHandicapped = String.valueOf(requestData.get("typeOfHandicapped"));
			labour.setHandicapped(typeOfHandicapped);
			
			String typeOfIllness = String.valueOf(requestData.get("typeOfIllness"));
			labour.setTypeOfIllness(typeOfIllness);
			
			String literacy = String.valueOf(requestData.get("literacy"));
			labour.setLiteracy(literacy);
			
			String literacySchoolCheck = String.valueOf(requestData.get("literacySchoolCheck"));
			labour.setAdmissionFlag(literacySchoolCheck);
			
			String typeOfEducation = String.valueOf(requestData.get("typeOfEducation"));
			labour.setEducationFlag(typeOfEducation);
			
			String formalEducationStatus = String.valueOf(requestData.get("formalEducation"));
			labour.setEducationStatus(formalEducationStatus);
			
			String levelOfEducation = String.valueOf(requestData.get("levelOfEducation"));
			labour.setEducationLevel(levelOfEducation);
			
			String educationTraining = String.valueOf(requestData.get("educationTraining"));
			labour.setTraining(educationTraining);
			
			String otherEducation = String.valueOf(requestData.get("otherEducation"));
			labour.setOther(otherEducation);
			
			String typeOfSchool = String.valueOf(requestData.get("typeOfSchool"));
			labour.setTypeOfSchool(typeOfSchool);
			
			String meansOfTransport = String.valueOf(requestData.get("meansOfTransport"));
			labour.setTransport(meansOfTransport);
			
			String scholorshipAvailed = String.valueOf(requestData.get("scholorshipAvailed"));
			labour.setSchoolarship(scholorshipAvailed);
			
			String scholorshipDepartment = String.valueOf(requestData.get("scholorshipDepartment"));
			labour.setSchoolarshipDept(scholorshipDepartment);
			
			String scholorshipScheme = String.valueOf(requestData.get("scholorshipScheme"));
			labour.setSchoolarshipDept(scholorshipScheme);
			
			String giveReason = String.valueOf(requestData.get("giveReason"));
			labour.setReason(giveReason);
			
			String familyHeadCheck = String.valueOf(requestData.get("familyHeadCheck"));
			labour.setHeadOfFamily(familyHeadCheck);
			
			String hofGender = String.valueOf(requestData.get("hofGender"));
			labour.setHeadOfFamilySexId(hofGender);
			
			String relationWithHOF = String.valueOf(requestData.get("relationWithHOF"));
			labour.setHeadOfFamilyRelationId(relationWithHOF);
			
			String tinNoCheck = String.valueOf(requestData.get("tinNoCheck"));
			labour.setTinNoFlag(tinNoCheck); 
			
			String tinNo = String.valueOf(requestData.get("tinNo"));
			labour.setTinNo(tinNo);
			
			String registeredCheck = String.valueOf(requestData.get("registeredCheck"));
			labour.setHealthCardFlag(registeredCheck);			
			
			String schemeNo = String.valueOf(requestData.get("schemeNo"));
			labour.setSchemeNo(schemeNo);
			
			String jobCardCheck = String.valueOf(requestData.get("jobCardCheck"));
			labour.setSchemeFlag(jobCardCheck);
			
			String jobCardNo = String.valueOf(requestData.get("jobCardNo"));
			labour.setmJobcardNo(jobCardNo);
			String surveyId = String.valueOf(requestData.get("surveyId"));
			labour.setWorkerId(surveyId);
			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			labour.setLastChgDate(date);
			labour.setLastChgBy(Long.parseLong(requestData.get("userId").toString()));
			Long labourId=(Long)session.save(labour);
			tx.commit();
			response.put("labourId", labourId);
			response.put("status", true);
			response.put("msg", "You have successfully registered");
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		response.put("status", false);
		response.put("msg", "Something went wrong");
		return response;
	}

	@Override
	public Map<String, Object> checkIfPatientIsAlreadyRegistered(Map<String, Object> requestData) {
		try {
			Long genderId = Long.parseLong(String.valueOf(requestData.get("genderId")));
			Long age = Long.parseLong(String.valueOf(requestData.get("age")));
			String patientName = String.valueOf(requestData.get("patientName"));
			String mobileNo = String.valueOf(requestData.get("mobileNo"));
			Map<String, Object> response = new HashMap<>();
			
			Boolean flag = getPatient(patientName, mobileNo, genderId, age);
			if (flag == null) {
				response.put("status", false);
				response.put("msg", "Something went wrong");
			}
			if (flag) {
				response.put("status", false);
				response.put("msg", "Patient is already registered");
			}else {
				response.put("status", true);
			}
			return response;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			
		}
		return null;
	}

	@Override
	public Map<String, Object> deleteCampPlan(Map<String, Object> requestData) {
		Transaction tx = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Long id = Long.parseLong(String.valueOf(requestData.get("id")));
			MasCamp persistentInstance = (MasCamp) session.load(MasCamp.class,
					id);
			if (persistentInstance != null) {
				session.delete(persistentInstance);
				tx.commit();
				map.put("status", true);
				session.clear();
				session.flush();

			}
			return map;
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		map.put("status", false);
		return map;
	}

	@Override
	public Map<String, Object> getFutureCampPlan(Map<String, Object> requestData) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			String cityId = String.valueOf(requestData.get("cityId"));
			//String date = String.valueOf(requestData.get("date"));
			
			Criteria cr = session.createCriteria(MasCamp.class)
					.add(Restrictions.eq("weeklyOff", "Camp").ignoreCase());
			
			if ((requestData.get("date") != null)
					&& !String.valueOf(requestData.get("date")).equals("")
					&& !String.valueOf(requestData.get("date")).equals("null")) {
				Date date = HMSUtil.convertStringTypeDateToDateType(String.valueOf(requestData.get("date")));
				Date nextDate = HMSUtil.getNextDate(date);
				cr.add(Restrictions.ge("campDate", date))
						.add(Restrictions.lt("campDate", nextDate));

			}
			if ((requestData.get("cityId") != null) && !String.valueOf(requestData.get("cityId")).equals("")
					&& !String.valueOf(requestData.get("cityId")).equals("null") && !String.valueOf(requestData.get("cityId")).equals("0")) {
				cr = cr.add(Restrictions.eq("cityId", Long.parseLong(cityId)));
			}
			
			List<MasCamp> masCampList = cr.list();
			response.put("list", masCampList);
			response.put("status",true);
			response.put("msg","Success");
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		//response.put("list", new JSONArray());
		response.put("status",false);
		response.put("msg","Something went wrong");
		return response;
	}
	@Transactional 
	public MasCity getMasCityByCityId(Long cityId,Session session) {
		MasCity masCity=null; 
		try {
			//Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 
			masCity = (MasCity) session.createCriteria(MasCity.class).add(Restrictions.eq("cityId", cityId)).uniqueResult();
					 
					 

			return masCity;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masCity;

}
	@Transactional
	public MasCamp getMasCampByCampIdId(Long campId,Session session) {
		MasCamp masCamp=null; 
		try {
			//Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 
			masCamp = (MasCamp) session.createCriteria(MasCamp.class).add(Restrictions.eq("campId", campId)).uniqueResult();
					 
					 

			return masCamp;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		//	getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masCamp;

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
