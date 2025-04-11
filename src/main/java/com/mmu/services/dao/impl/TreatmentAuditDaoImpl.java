package com.mmu.services.dao.impl;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.TreatmentAuditDao;
import com.mmu.services.entity.AuditException;
import com.mmu.services.entity.AuditOpd;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.MasAudit;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientPrescriptionDt;
import com.mmu.services.entity.PatientPrescriptionHd;
import com.mmu.services.entity.PatientSymptom;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

@Repository
@Transactional
public class TreatmentAuditDaoImpl implements TreatmentAuditDao {

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;

	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String DG_ORDER_DT = databaseScema + "." + "DG_ORDER_DT";
	final String DG_ORDER_HD = databaseScema + "." + "DG_ORDER_HD";
	final String DG_MAS_INVESTIGATION = databaseScema + "." + "DG_MAS_INVESTIGATION";

	final String PATIENT_PRESCRIPTION_HD = databaseScema + "." + "PATIENT_PRESCRIPTION_HD";
	final String PATIENT_PRESCRIPTION_DT = databaseScema + "." + "PATIENT_PRESCRIPTION_DT";
	final String MAS_STORE_ITEM = databaseScema + "." + "MAS_STORE_ITEM";
	final String MAS_STORE_UNIT = databaseScema + "." + "MAS_STORE_UNIT";
	final String MAS_FREQUENCY = databaseScema + "." + "MAS_FREQUENCY";
	final String REFERRAL_PATIENT_DT = databaseScema + "." + "REFERRAL_PATIENT_DT";
	final String REFERRAL_PATIENT_HD = databaseScema + "." + "REFERRAL_PATIENT_HD";
	final String MAS_EMPANELLED_HOSPITAL = databaseScema + "." + "MAS_EMPANELLED_HOSPITAL";
	final String MAS_ICD = databaseScema + "." + "MAS_ICD";
	final String MAS_DEPARTMENT = databaseScema + "." + "MAS_DEPARTMENT";
	final String PROCEDURE_DT = databaseScema + "." + "PROCEDURE_DT";
	final String PROCEDURE_HD = databaseScema + "." + "PROCEDURE_HD";
	final String MAS_NURSING_CARE = databaseScema + "." + "MAS_NURSING_CARE";

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAuditVisit(HashMap<String, Object> jsonData) {

		System.out.println("Audit Waiting List");
		List<Visit> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String opdPre = "opdPre";
			String checkOdPre = jsonData.get("opdPre").toString();

			Criteria cr = null;
			;
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");

			// changes done by Rahul for all unit
			// long hospitalId =
			// Long.parseLong((String.valueOf(jsonData.get("hospitalId"))));
			// Long [] hospitalList=null;
			// String [] hospitals= jsonData.get("hospitalId").toString().split(",");
			// hospitalList = HMSUtil.convertFromStringToLongArray(hospitals);

			//Long cityIdFilter = null;
			Long mmuIdFilter = null;
			String uhidNo=null;
			String mobileNo = (String) jsonData.get("mobileNo");
			String patientName = (String) jsonData.get("patientName");
			 
			if (jsonData.get("uhidNo") != null && !jsonData.get("uhidNo").equals("")) {
				uhidNo = (String) jsonData.get("uhidNo");
			}
			if (jsonData.get("mmuIdVal") != null && !jsonData.get("mmuIdVal").equals("")) {
				mmuIdFilter = Long.parseLong((String) jsonData.get("mmuIdVal"));
			}
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
            String ruuningException="Y";
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if (jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && !jsonData.get("exceptionChecked").equals("Y")) {
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						.createAlias("visit.patient", "patient")
				
						.addOrder(Order.desc("visitDate"));
				cr.add(Restrictions.isNull("runningException"));
				cr.add(Restrictions.eq("visitStatus", "c"));

			}
			else if (jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y")&&jsonData.get("exceptionType").equals("withException")) {
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						.createAlias("visit.patient", "patient")
						.addOrder(Order.desc("visitDate"));
				cr.add(Restrictions.isNull("runningException"));
				cr.add(Restrictions.eq("visitStatus", "c"));
				cr.add(Restrictions.eq("aiAuditException", "Y"));

			}
			else if (jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y") &&jsonData.get("exceptionType").equals("diagnois")) {
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						.createAlias("visit.patient", "patient")
						.createAlias("visit.opdPatientDetails", "opdPatientDetails")
						.addOrder(Order.desc("visitDate"));
				cr.add(Restrictions.isNull("runningException"));
				cr.add(Restrictions.eq("visitStatus", "c"));
				//cr.add(Restrictions.eq("aiAuditException", "Y"));
				cr.add(Restrictions.eq("opdPatientDetails.diagnosisFlag", "Y"));

			}
			else if (jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y") &&jsonData.get("exceptionType").equals("investigation")) {
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						.createAlias("visit.patient", "patient")
						.createAlias("visit.opdPatientDetails", "opdPatientDetails")
						.addOrder(Order.desc("visitDate"));
				cr.add(Restrictions.isNull("runningException"));
				cr.add(Restrictions.eq("visitStatus", "c"));
				//cr.add(Restrictions.eq("aiAuditException", "Y"));
				cr.add(Restrictions.eq("opdPatientDetails.investigationFlag", "Y"));

			}
			else if (jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y") &&jsonData.get("exceptionType").equals("treatment")) {
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						.createAlias("visit.patient", "patient")
						.createAlias("visit.opdPatientDetails", "opdPatientDetails")
						.addOrder(Order.desc("visitDate"));
				cr.add(Restrictions.isNull("runningException"));
				cr.add(Restrictions.eq("visitStatus", "c"));
				//cr.add(Restrictions.eq("aiAuditException", "Y"));
				cr.add(Restrictions.eq("opdPatientDetails.prescriptionFlag", "Y"));

			}else {
				System.out.println("else condition");
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						.createAlias("visit.patient", "patient")
						
						  .createAlias("visit.auditExceptionT", "auditExceptionT")
						 

						.addOrder(Order.desc("visitDate"));
				cr.add(Restrictions.isNotNull("runningException"));
				if(jsonData.get("exceptionChecked").equals("Y")&&jsonData.get("exceptionType").equals("withException"))
				{
					cr.add(Restrictions.isNotNull("runningException"));	
				}
			}
			
			if (jsonData.get("mmuIdVal") != null && !jsonData.get("mmuIdVal").equals("")
					&& !jsonData.get("mmuIdVal").equals("null")) {
				cr.add(Restrictions.eq("mh.mmuId", mmuIdFilter));
			}
			
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {
				String pName = "%" + patientName + "%";
				cr.add(Restrictions.like("patient.patientName", pName).ignoreCase());				
			}
			if (null!=uhidNo) {
				cr.add(Restrictions.eq("patient.uhidNo", uhidNo));
			}
			
			Criterion c1 = null;
	
			/*if (checkOdPre.equalsIgnoreCase("C")) {
				c1 = Restrictions.eq("visitStatus", "c");
			}
			else if(checkOdPre.equalsIgnoreCase("C") &&jsonData.get("exceptionChecked").equals("Y") && jsonData.get("exceptionType").equals("diagnois"))
			{
				c1 = Restrictions.eq("opdPatientDetails.diagnosisFlag", "Y");
			}
			else if(checkOdPre.equalsIgnoreCase("C") &&jsonData.get("exceptionChecked").equals("Y")&& jsonData.get("exceptionType").equals("investigation"))
			{
				c1 = Restrictions.eq("opdPatientDetails.investigationFlag", "Y");
			}
			else if(checkOdPre.equalsIgnoreCase("C") &&jsonData.get("exceptionChecked").equals("Y") && jsonData.get("exceptionType").equals("treatment"))
			{
				c1 = Restrictions.eq("opdPatientDetails.prescriptionFlag", "Y");
			}
			else if(checkOdPre.equalsIgnoreCase("F") && jsonData.get("exceptionType")=="")
			{
				c1 = Restrictions.eq("runningException", "Y");
			}
			else if(checkOdPre.equalsIgnoreCase("F") && jsonData.get("exceptionType").equals("withException"))
			{
				c1 =Restrictions.eq("auditExceptionT.finalFlag", "R");
			}
			else if(checkOdPre.equalsIgnoreCase("F") && jsonData.get("exceptionType").equals("diagnois"))
			{
				c1 =Restrictions.eq("auditExceptionT.diagnosisFlag", "Y");
			}
			else if(checkOdPre.equalsIgnoreCase("F") && jsonData.get("exceptionType").equals("investigation"))
			{
				c1 =Restrictions.eq("auditExceptionT.investigationFlag", "Y");
			}
			else if(checkOdPre.equalsIgnoreCase("F") && jsonData.get("exceptionType").equals("treatment"))
			{
				c1 =Restrictions.eq("auditExceptionT.treatmentFlag", "Y");
			}
			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
				String mNumber = "%" + mobileNo + "%";
				cr.add(Restrictions.like("patient.mobileNumber", mNumber));
			}
			
		
			if (checkOdPre.equalsIgnoreCase("C")) {
				cr.add(c1);
				//cr.setMaxResults(50);
			} else {
				cr.add(c1);
			}*/
			
			String qry="";
			if (jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("")) {
				 qry = "select visit_id from visit v\r\n" + 
						"left outer join patient p on p.patient_id=v.patient_id\r\n" + 
						"left outer join mas_mmu m on v.mmu_id=m.mmu_id\r\n" + 
						"where v.running_exception is null and visit_status='c'  ";
				
			}
			else if(jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y") && jsonData.get("exceptionType").equals("withException"))
			{
				qry = "select visit_id from visit v\r\n" + 
						"left outer join patient p on p.patient_id=v.patient_id\r\n" + 
						"left outer join mas_mmu m on v.mmu_id=m.mmu_id\r\n" + 
						"where v.running_exception is null and visit_status='c' and ai_audit_exception='Y' ";	
			
			}
			else if(jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y")&& jsonData.get("exceptionType").equals("diagnois"))
			{
				qry = "select v.visit_id from visit v\r\n" + 
						"left outer join patient p on p.patient_id=v.patient_id\r\n" + 
						"left outer join mas_mmu m on v.mmu_id=m.mmu_id\r\n" + 
						"left outer join OPD_PATIENT_DETAILS od on v.visit_id=od.visit_id\r\n" + 
						"where v.running_exception is null and visit_status='c' and ai_audit_exception='Y' and od.diagnosis_flag='Y' ";	
			
			}
			else if(jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y")&& jsonData.get("exceptionType").equals("investigation"))
			{
				qry = "select v.visit_id from visit v\r\n" + 
						"left outer join patient p on p.patient_id=v.patient_id\r\n" + 
						"left outer join mas_mmu m on v.mmu_id=m.mmu_id\r\n" + 
						"left outer join OPD_PATIENT_DETAILS od on v.visit_id=od.visit_id\r\n" + 
						"where v.running_exception is null and visit_status='c' and ai_audit_exception='Y' and od.investigation_flag='Y' ";	
			
			}
			else if(jsonData.get("opdPre") != null && jsonData.get("opdPre").equals("c") && jsonData.get("exceptionChecked").equals("Y")&& jsonData.get("exceptionType").equals("treatment"))
			{
				qry = "select v.visit_id from visit v\r\n" + 
						"left outer join patient p on p.patient_id=v.patient_id\r\n" + 
						"left outer join mas_mmu m on v.mmu_id=m.mmu_id\r\n" + 
						"left outer join OPD_PATIENT_DETAILS od on v.visit_id=od.visit_id\r\n" + 
						"where v.running_exception is null and visit_status='c' and ai_audit_exception='Y' and od.prescription_flag='Y' ";	
			
			}
			else
			{ 
				qry = "select visit_id from visit v\r\n" + 
					"left outer join patient p on p.patient_id=v.patient_id\r\n" + 
					"left outer join mas_mmu m on v.mmu_id=m.mmu_id\r\n" + 
					"where v.running_exception is not null " ;
				
			}
			if (jsonData.get("mmuIdVal") != null && !jsonData.get("mmuIdVal").equals("")
					&& !jsonData.get("mmuIdVal").equals("null")) {
				qry= qry +" and v.mmu_id="+mmuIdFilter;
			}
			
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {
				String pName = "'%" + patientName + "%'";
				qry= qry +" and p.patient_name ilike "+pName;
			}
					

		Query queryHiber = (Query) session.createSQLQuery(qry);

		

		List<Object[]> objectList = (List<Object[]>) queryHiber.list();
		
		System.out.println("objectList="+objectList.size());
			
			//list = cr.list();
			
			System.out.println("pageNo="+pageNo);
			
			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
			cr = cr.setMaxResults(pagingSize);
			list = cr.list();
			
			map.put("count", objectList.size());			
			
			map.put("list", list);
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<String, Object> getMasIcdByVisitPatAndOpdPD(Long visitId, HashMap<String, String> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			
			String Query = "SELECT 	audit.opd_icd_code_id as ICD_ID,di1.ICD_NAME ICD_NAME,di1.ICD_CODE, discharge_icd_code_id AUDIT_ICD_ID," + 
					" string_agg(distinct di2.ICD_NAME,', ' order by di2.ICD_NAME) AUDIT_ICD_NAME,di2.ICD_CODE AUDIT_ICD_CODE,audit.status,audit.auditor_remarks," + 
					" di1.communicable_flag,di1.infectious_flag" + 
					" FROM audit_opd audit" + 
					" LEFT OUTER JOIN  mas_icd di1 ON di1.icd_id=audit.opd_icd_code_id" + 
					" LEFT OUTER JOIN  mas_icd di2 ON di2.icd_id=audit.discharge_icd_code_id" + 
					" WHERE 	 opd_icd_code_id is not null and visit_id='"+visitId+ "' " + 
					" group by opd_icd_code_id,discharge_icd_code_id,di1.ICD_NAME,audit.status," + 
					" audit.auditor_remarks,di1.communicable_flag,di1.infectious_flag," + 
					" di1.ICD_CODE ,di2.ICD_CODE";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public  Map<String, Object> getDgMasInvestigations(Long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			
			String Query = "select audit.OPD_INVESTIGATION_ID,di1.INVESTIGATION_NAME,di2.INVESTIGATION_ID AUDIT_INVESTIGATION_ID," + 
					" string_agg(distinct di2.INVESTIGATION_NAME,', ' order by di2.INVESTIGATION_NAME) AUDIT_INVESTIGATION_NAME,audit.status,audit.auditor_remarks" + 
					" FROM 	audit_opd audit" + 
					" LEFT OUTER JOIN  dg_mas_investigation di1 ON di1.investigation_id=audit.opd_investigation_id" + 
					" LEFT OUTER JOIN  dg_mas_investigation di2 ON di2.investigation_id=audit.investigation_id" + 
					" WHERE 	 opd_investigation_id is not null and visit_id='"+visitId+ "'" + 
					" group by audit.OPD_INVESTIGATION_ID,di1.INVESTIGATION_NAME,di2.INVESTIGATION_ID ," + 
					" audit.status,audit.auditor_remarks";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object>  getTreatementDetail(Long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			
			String Query = "Select  audit.opd_item_id,di1.nomenclature,di2.ITEM_ID AUDIT_INVESTIGATION_ID," + 
					" string_agg(distinct di2.nomenclature,', ' order by di2.nomenclature) AUDIT_INVESTIGATION_NAME,audit.status,audit.auditor_remarks," + 
					" mf.FREQUENCY_ID,mf.FREQUENCY_NAME,ppdt.NO_OF_DAYS,ppdt.dosage,ppdt.total,ppdt.instruction," + 
					" msu.STORE_UNIT_NAME,di1.PVMS_NO" + 
					" FROM 	audit_opd audit" + 
					" LEFT OUTER JOIN  PATIENT_PRESCRIPTION_HD pphd ON audit.VISIT_ID= pphd.VISIT_ID" + 
					" LEFT OUTER JOIN  PATIENT_PRESCRIPTION_DT ppdt on pphd.PRESCRIPTION_HD_ID=ppdt.PRESCRIPTION_HD_ID AND ppdt.ITEM_ID=audit.opd_item_id" + 
					" LEFT OUTER JOIN  mas_store_item di1 ON di1.ITEM_ID=audit.opd_ITEM_ID" + 
					" LEFT OUTER JOIN  MAS_STORE_UNIT msu on msu.STORE_UNIT_ID=di1.DISP_UNIT_ID " + 
					" LEFT OUTER JOIN  MAS_FREQUENCY MF on MF.FREQUENCY_ID=ppdt.FREQUENCY_ID" + 
					" LEFT OUTER JOIN  mas_store_item di2 ON di2.ITEM_ID=audit.ITEM_ID" + 
					" WHERE 	 opd_item_id is not null and audit.visit_id='"+visitId+ "'" + 
					" group by audit.opd_item_id,di1.nomenclature,di2.ITEM_ID ," + 
					" audit.status,audit.auditor_remarks" + 
					",mf.FREQUENCY_ID,mf.FREQUENCY_NAME,ppdt.NO_OF_DAYS,ppdt.dosage,ppdt.total,ppdt.instruction," + 
					" msu.STORE_UNIT_NAME,di1.PVMS_NO";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public String saveTreatmentAuditDetails(HashMap<String, Object> jsondata) {
		Date currentDate = ProjectUtils.getCurrentDate();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(OpdPatientDetail.class);
		Transaction tx = session.beginTransaction();
		Long visitId = Long.parseLong((String) jsondata.get("visitId"));
		Long patientId;
		Long hospitalId;
		Long userId;
		Long opdId;
		Long itemTypeNivId = null;
		Long headerNivId = null;
		Long auditId =null;
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		String procedureType = null;
		cr.add(Restrictions.eq("visitId", visitId));
		OpdPatientDetail opdlist = (OpdPatientDetail) cr.uniqueResult();
		try {

			if (opdlist != null) {
				opdlist.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
				opdlist.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));

				// opdlist.setmmu(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
				session.update(opdlist);
				opdId = opdlist.getOpdPatientDetailsId();
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if (visit != null) {
						visit.setRunningException("Y");
						/*if (jsondata.get("userId") != null && jsondata.get("userId") != "") {
							visit.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
						}*/
						session.update(visit);
					}
				}
				////////////////////////Treatment Audit Exception/////////////////////////////////////////////////////////
				AuditException auditExc=new AuditException();
				AuditException checkVisitId = checkExistingAuditData(visitId, true);
				if (checkVisitId != null) {
					return  "Sorry, Prescription audit is already submitted for this OPD record";
				}
				auditExc.setDiagnosisFlag(String.valueOf(jsondata.get("dianosisException")));
				auditExc.setInvestigationFlag(String.valueOf(jsondata.get("investgationFlag")));
				auditExc.setTreatmentFlag(String.valueOf(jsondata.get("treatmentFlag")));
				auditExc.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
				auditExc.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
				auditExc.setMmuId(Long.parseLong(String.valueOf(jsondata.get("mmuId"))));
				auditExc.setRemarks(String.valueOf(jsondata.get("auditorRemrks")));
				auditExc.setFinalFlag(String.valueOf(jsondata.get("finalFlag")));
				auditExc.setLastChgDate(ourJavaTimestampObject);
				auditExc.setExceptionDate(currentDate);
				 if(jsondata.get("actual_fee") !=""){
                     System.out.println("Save AuditId:"+jsondata.get("actual_fee"));
                     auditExc.setAuditId(Long.parseLong(String.valueOf(jsondata.get("actual_fee"))));
                     }else{
                    	 auditExc.setAuditId(Long.parseLong(String.valueOf("4")));
                     }
				session.save(auditExc);
				
				
				
		///////////////////////// OPD Audit Details ///////////////////////// Entry/////////////////////////
	if (jsondata.get("symptomsValue") != null) {
    List<HashMap<String, Object>> listSymptomsValue = (List<HashMap<String, Object>>) jsondata.get("symptomsValue");
  	for (HashMap<String, Object> symptomsValue : listSymptomsValue) {		
		if (jsondata.get("icdDiagnosis") != null) {
			List<HashMap<String, Object>> listIcdValue = (List<HashMap<String, Object>>) jsondata
					.get("icdDiagnosis");
			if (listIcdValue != null) {
					for (HashMap<String, Object> icdOpd : listIcdValue) {
					List<HashMap<String, Object>> icdList = (List<HashMap<String, Object>>) (Object) icdOpd.get("icdId");
			
					for (HashMap<String, Object> icdMap : icdList) {
						AuditOpd auditOpd=new AuditOpd();
						if(icdMap.get("diagnosisId") != null)
						{	
						auditOpd.setDischargeIcdCodeId(Long.valueOf(icdMap.get("diagnosisId").toString()));
						auditOpd.setOpdIcdCodeId(Long.valueOf(icdMap.get("mainDiagnosisId").toString()));
						auditOpd.setPatientSymptomsId(Long.valueOf(symptomsValue.get("symptomsId").toString()));
						auditOpd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("mmuId"))));
					    String action=icdMap.get("action").toString();
                        String remarks=icdMap.get("remarks").toString();
                        auditOpd.setStatus(action);
                        auditOpd.setAuditorRemarks(remarks);
                        auditOpd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
                        auditOpd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
                        auditOpd.setLastChgDate(ourJavaTimestampObject);
                        /*if(jsondata.get("actual_fee") !=""){
                        System.out.println("Save AuditId:"+jsondata.get("actual_fee"));
                        auditOpd.setAuditId(Long.parseLong(String.valueOf(jsondata.get("actual_fee"))));
                        }else{
                        	auditOpd.setAuditId(Long.parseLong(String.valueOf("4")));
                        }*/
                        session.save(auditOpd);
						}
					}
					
				}
			}
		}
  	 }
  	}		
	
	//////////////////////////////Investigation section ///////////////////////////////////////////
	if (jsondata.get("symptomsValue") != null) {
	    List<HashMap<String, Object>> listSymptomsValue = (List<HashMap<String, Object>>) jsondata.get("symptomsValue");
	  	for (HashMap<String, Object> symptomsValue : listSymptomsValue) {		
			if (jsondata.get("investigationData") != null) {
				List<HashMap<String, Object>> listInvValue = (List<HashMap<String, Object>>) jsondata
						.get("investigationData");
				if (listInvValue != null) {
						for (HashMap<String, Object> invOpd : listInvValue) {
						List<HashMap<String, Object>> invList = (List<HashMap<String, Object>>) (Object) invOpd.get("investgationId");
				
						for (HashMap<String, Object> invMap : invList) {
							AuditOpd auditOpd=new AuditOpd();
							if(invMap.get("invId") != null && invMap.get("invId") !="")
							{	
							auditOpd.setInvestigationId(Long.valueOf(invMap.get("invId").toString()));
							auditOpd.setOpdInvestigationId(Long.valueOf(invMap.get("mainInvId").toString()));
							auditOpd.setPatientSymptomsId(Long.valueOf(symptomsValue.get("symptomsId").toString()));
							auditOpd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("mmuId"))));
						    String action=invMap.get("action").toString();
	                        String remarks=invMap.get("remarks").toString();
	                        auditOpd.setStatus(action);
	                        auditOpd.setAuditorRemarks(remarks);
	                        auditOpd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
	                        auditOpd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
	                        auditOpd.setLastChgDate(ourJavaTimestampObject);
	                        session.save(auditOpd);
							}
						}
						
					}
				}
			}
	  	 }
	  	}	
	
				////////////////////////////// Treatment Drugs section
				////////////////////////////// ///////////////////////////////////////////
				if (jsondata.get("symptomsValue") != null) {
					List<HashMap<String, Object>> listSymptomsValue = (List<HashMap<String, Object>>) jsondata
							.get("symptomsValue");
					for (HashMap<String, Object> symptomsValue : listSymptomsValue) {
						if (jsondata.get("treatmentData") != null) {
							List<HashMap<String, Object>> listTreatValue = (List<HashMap<String, Object>>) jsondata
									.get("treatmentData");
							if (listTreatValue != null) {
								for (HashMap<String, Object> invOpd : listTreatValue) {
									List<HashMap<String, Object>> treatmentIdList = (List<HashMap<String, Object>>) (Object) invOpd
											.get("treatmentId");

									for (HashMap<String, Object> treatMap : treatmentIdList) {
										AuditOpd auditOpd = new AuditOpd();
										if(treatMap.get("treatId") != null && treatMap.get("treatId") !="")
										{	
										auditOpd.setItemId(Long.valueOf(treatMap.get("treatId").toString()));
										auditOpd.setOpdItemId(Long.valueOf(treatMap.get("mainTreatmentId").toString()));
										auditOpd.setPatientSymptomsId(Long.valueOf(symptomsValue.get("symptomsId").toString()));
										auditOpd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("mmuId"))));
										String action = treatMap.get("action").toString();
										String remarks = treatMap.get("remarks").toString();
										auditOpd.setStatus(action);
										auditOpd.setAuditorRemarks(remarks);
										auditOpd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
										auditOpd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
										auditOpd.setLastChgDate(ourJavaTimestampObject);
										session.save(auditOpd);
										}
									}

								}
							}
						}
					}
				}	

				

			}

			tx.commit();

		} catch (Exception ex) {

			// System.out.println("Exception e="+ex.);
			ex.printStackTrace();
			tx.rollback();
			System.out.println("Exception Message Print ::" + ex.toString());
			return ex.toString();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return "Successfully saved";
	}

	public DgOrderhd getOrderDatebyInvestigation(Long visitId) {
		Session session = null;
		DgOrderhd investigationHeaderHd = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(DgOrderhd.class).add(Restrictions.eq("visitId", visitId));
			List<DgOrderhd> listInvHd = cr.list();
			if (CollectionUtils.isNotEmpty(listInvHd)) {
				investigationHeaderHd = listInvHd.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return investigationHeaderHd;
	}
	
	public PatientPrescriptionHd getDurgsHeaderId(Long visitId) {
		Session session = null;
		PatientPrescriptionHd dugrsHeaaderId = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(PatientPrescriptionHd.class).add(Restrictions.eq("visitId", visitId));
			List<PatientPrescriptionHd> listInvHd = cr.list();
			if (CollectionUtils.isNotEmpty(listInvHd)) {
				dugrsHeaaderId = listInvHd.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dugrsHeaaderId;
	}

	@Override
	public Map<String, Object> getRecommendedDiagnosisAllDetail(String patientSympotnsId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;
		
		try {
			
			String Query = "select MAS_ICD.ICD_NAME ||' ['||MAS_ICD.ICD_CODE ||']',MAS_ICD.ICD_ID,communicable_flag,infectious_flag" + 
					" FROM audit_opd_icd, MAS_ICD" + 
					" WHERE audit_opd_icd.icdid=MAS_ICD.ICD_ID" + 
					" and  audit_opd_flag=1 and" + 
					" audit_opd_icd.patient_symptoms ='"+patientSympotnsId+"'"+ 
					" order by 1;";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> getRecommendedInvestgationAllDetail(String patientSympotnsId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			
			String Query = "select INVESTIGATION_NAME,INVESTIGATION_ID FROM audit_opd_investigation, DG_MAS_INVESTIGATION" + 
					" WHERE audit_opd_investigation.investigationid=DG_MAS_INVESTIGATION.INVESTIGATION_ID" + 
					" and audit_opd_flag=1 and audit_opd_investigation.patient_symptoms ='"+patientSympotnsId+"' order by 1;";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public Map<String, Object> getRecommendedTreatmentAllDetail(String patientSympotnsId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			
			String Query = "select MAS_STORE_ITEM.NOMENCLATURE||'['||MAS_STORE_ITEM.PVMS_NO ||']',MAS_STORE_ITEM.ITEM_ID," + 
					" MAS_STORE_ITEM.DISP_UNIT_ID,MAS_STORE_ITEM.ITEM_CLASS_ID" + 
					" FROM audit_opd_item, MAS_STORE_ITEM" + 
					" WHERE audit_opd_item.ITEMID=MAS_STORE_ITEM.ITEM_ID" + 
					" and  audit_opd_flag=1 and" + 
					" audit_opd_item.patient_symptoms ='"+patientSympotnsId+"'" + 
					" order by 1;";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public AuditException checkExistingAuditData(Long visitId, boolean status) {
		Session session1=null;
		session1= getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Criteria cr = session1.createCriteria(AuditException.class);
			cr.add(Restrictions.eq("visitId", visitId));
			AuditException entity = (AuditException) cr.uniqueResult();
			//session1.close();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			//session1.close();
			return null;
		} finally {
			//session.close();
		}
	}
	
	@Override
	public List<Long> getAIDiagnosisDetail(String patientSympotnsId,Long diagnosisId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<BigInteger, Object> map = new HashMap<>();
		List<Long> searchList = null;
		int count = 0;
		
		try {
			
			String Query = "SELECT COUNT(icdid)" + 
					" FROM audit_opd_icd" + 
					" WHERE audit_opd_icd.patient_symptoms ='"+patientSympotnsId+"' and icdid='"+diagnosisId+"' and  audit_opd_flag=2";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return searchList;
	}
	
	@Override
	public List<Long> getAIInvestgationDetail(String patientSympotnsId,Long investgationId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<BigInteger, Object> map = new HashMap<>();
		List<Long> searchList = null;
		int count = 0;

		try {
			  
			String Query = "SELECT COUNT(investigationid) FROM audit_opd_investigation" + 
					" WHERE audit_opd_investigation.patient_symptoms ='"+patientSympotnsId+"'" + 
					" and investigationid='"+investgationId+"' and audit_opd_flag=2";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return searchList;
	}
	
	@Override
	public List<Long> getAITreatmentDetail(String patientSympotnsId,Long treatmentId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<BigInteger, Object> map = new HashMap<>();
		List<Long> searchList = null;
		int count = 0;
		  
		try {
			
			String Query = "SELECT COUNT(itemid)" + 
					" FROM audit_opd_item" + 
					" WHERE audit_opd_item.patient_symptoms ='"+patientSympotnsId+"' and itemid='"+treatmentId+"'  and  audit_opd_flag=2";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return searchList;
	}
	
	@Override
	public Map<String, Object> getAllSymptomsForOpd(String name) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			name=name.toUpperCase();
		/*String Query = "select   ps.symptoms_id, ms.symptoms_code, ms.symptoms_name " + 
					" from patient_symptoms ps" + 
					" left outer join mas_symptoms ms on ms.symptoms_id=ps.symptoms_id" + 
					" GROUP BY ps.symptoms_id, ms.symptoms_code, ms.symptoms_name\r\n" + 
					" ORDER BY COUNT(*) DESC" + 
					" LIMIT    25";*/
			String Query = "select ms.symptoms_id, ms.symptoms_code,  ms.symptoms_name " + 
					" from mas_symptoms ms where ms.most_common_user='y' order by ms.symptoms_name asc";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public Map<String, Object> getAllIcdForOpd(String name) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<Object[]> searchList = null;
		int count = 0;

		try {
			name=name.toUpperCase();
			/*String Query = "select  ds.icd_id,ms.icd_code,ms.icd_name" + 
					" from discharge_icd_code ds" + 
					" left outer join mas_icd ms on ms.icd_id=ds.icd_id" + 
					" GROUP BY ds.icd_id,ms.icd_code,ms.icd_name" + 
					" ORDER BY COUNT(*) DESC" + 
					" LIMIT    25";*/
			String Query = "select ms.icd_id,ms.icd_code,ms.icd_name " + 
					" from mas_icd ms where ms.most_common_user='Y' order by ms.icd_name asc";

			System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();

			} else {
				System.out.println("No Record Found");
			}
			Query query = session.createSQLQuery(Query.toString());
			count = searchList.size();

			int listCount = searchList.size();

			searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@Override
	public Map<String, Object> getExpiryMedicine(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		
		//int userId = 2;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
		JSONObject jsonObject = new JSONObject();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection connection) throws SQLException {
				
				int mmuId=1;
				JSONArray jsonArray1 = new JSONArray();				
				JSONArray jsonArray2 = new JSONArray();	
				
				
				
				if(jsondata.containsKey("mmuId")) {
					mmuId = Integer.parseInt(jsondata.get("mmuId").toString());
					}
				
				String queryString = "SELECT asp_medicine_dashboard(?)";				
				PreparedStatement    stmt = connection.prepareCall(queryString);
				stmt.setInt(1, mmuId);
				connection.setAutoCommit(false);
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs1 = (ResultSet) o;
						while (rs1.next()) {
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs1.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs1.getObject(i + 1)));
							}
							jsonArray1.put(jsonObj);
						}
						jsonObject.put("expiry_medicine", jsonArray1);
					}
				  }
				
				if (rs.next()) {
					Object o = rs.getObject(1);
					if (o instanceof ResultSet) {
						ResultSet rs2 = (ResultSet) o;
						while (rs2.next()) {
							int columnCount = rs2.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for (int i = 0; i < columnCount; i++) {
								jsonObj.put(rs2.getMetaData().getColumnLabel(i + 1), HMSUtil.convertNullToEmptyString(rs2.getObject(i + 1)));
							}
							jsonArray2.put(jsonObj);
						}
						jsonObject.put("total_data", jsonArray2);
					}
				}
				
			    }
		   });
			
				map.put("asp_medicine", jsonObject);
				return map;
		}

	
	
}
