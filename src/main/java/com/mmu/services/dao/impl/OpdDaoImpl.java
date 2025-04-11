package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Array;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.mmu.services.dao.OpdDao;
import com.mmu.services.entity.AuditException;
import com.mmu.services.entity.AuditOpd;
import com.mmu.services.entity.ChildVacatinationChart;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.FwcObjDetail;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasAnesthesia;
import com.mmu.services.entity.MasAppointmentType;
import com.mmu.services.entity.MasAudit;
import com.mmu.services.entity.MasDepartment;

import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasFrequency;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasIdealWeight;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasNursingCare;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRegistrationType;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasServiceType;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSection;
import com.mmu.services.entity.MasTemplate;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdDisposalDetail;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.OpdTemplateMedicalAdvice;
import com.mmu.services.entity.OpdTemplateTreatment;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDataUpload;
import com.mmu.services.entity.PatientFamilyHistory;
import com.mmu.services.entity.PatientImmunizationHistory;
import com.mmu.services.entity.PatientImpantHistory;
import com.mmu.services.entity.PatientPrescriptionDt;
import com.mmu.services.entity.PatientPrescriptionHd;
import com.mmu.services.entity.PatientSymptom;
import com.mmu.services.entity.ProcedureDt;
import com.mmu.services.entity.ProcedureHd;
import com.mmu.services.entity.ReferralPatientDt;
import com.mmu.services.entity.ReferralPatientHd;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.CommonUtil;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

@Repository
@Transactional
public class OpdDaoImpl implements OpdDao {

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;
    
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String visitTable=databaseScema+"."+"VISIT";
	final String masDepartmentTable=databaseScema+"."+"MAS_DEPARTMENT";
	final String opdPatientDetailsTable=databaseScema+"."+"OPD_PATIENT_DETAILS";
	final String usersTable=databaseScema+"."+"USERS";
		
	final String dgResultEntryHdTable=databaseScema+"."+"DG_RESULT_ENTRY_HD";
	final String dgResultEntryDtTable=databaseScema+"."+"DG_RESULT_ENTRY_DT";
	final String dgMasInvestigationTable=databaseScema+"."+"DG_MAS_INVESTIGATION";
	final String dgUomTable=databaseScema+"."+"DG_UOM";
	final String patientTable=databaseScema+"."+"PATIENT";
	final String masUnitView=databaseScema+"."+"VU_MAS_UNIT";
	final String masHospitalTable=databaseScema+"."+"MAS_HOSPITAL";
	
	
	
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
				list.setIdealWeight(ob.getIdealWeight());
				list.setVaration(ob.getVaration());
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
						visit.setVisitStatus("p");
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
						visit.setVisitStatus("p");
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

	

	@Override
	public String opdObsisty(HashMap<String, Object> jsondata) {
		String Result=null;
		/*Transaction t = null;
		try { 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		//Criteria cr = session.createCriteria(OpdObesityHd.class);
		t = session.beginTransaction();
		Long visitId = Long.parseLong((String) jsondata.get("visitId"));
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		Date currentDate=ProjectUtils.getCurrentDate();
			/cr.add(Restrictions.eq("visitId", visitId));
			// list = (OpdObesityHd) cr.uniqueResult();
			if (list != null) {
				list.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
				if (jsondata.get("varation") != null && !jsondata.get("varation").equals("")) {
					BigDecimal bd = new BigDecimal(String.valueOf(jsondata.get("varation")));
					list.setVaration(bd);
				}
				
				list.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
				list.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId").toString())));
				list.setLastChgDate(ourJavaTimestampObject);
				list.setIniDate(currentDate);
				session.update(list);
				Result = "200";

			} else {
				OpdObesityHd oohd=new OpdObesityHd();
				
				  LocalDate currentDate1 = LocalDate.now(); // 2016-06-17
				  DayOfWeek dow = currentDate1.getDayOfWeek(); // FRIDAY
				  int dom = currentDate1.getDayOfMonth(); // 17
				  int doy = currentDate1.getDayOfYear(); // 169
				  String m = currentDate1.getMonth()+""; // JUNE
				  System.out.println("months"+m);
					
                
				oohd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
				oohd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
				oohd.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
				oohd.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
				oohd.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
				oohd.setOverweightFlag(String.valueOf(jsondata.get("obsistyMark")));
				oohd.setLastChgDate(ourJavaTimestampObject);
				if (jsondata.get("varation") != null && !jsondata.get("varation").equals("")) {
					BigDecimal bd = new BigDecimal(String.valueOf(jsondata.get("varation")));
					oohd.setVaration(bd);
				}
				oohd.setIniDate(currentDate);
				oohd.setLastChgDate(ourJavaTimestampObject);
				long obesistyId = Long.parseLong(session.save(oohd).toString());
				System.out.println(obesistyId);
				t.commit();
				t=session.beginTransaction();
				   OpdObesityDt oodt=new OpdObesityDt();
				   if (jsondata.get("bmi") != null && !jsondata.get("bmi").equals("")) {
				   BigDecimal bmi = new BigDecimal(String.valueOf(jsondata.get("bmi")));
				   oodt.setBmi(bmi);
				   }
				   oodt.setObesityDate(currentDate);
				   if (jsondata.get("height") != null && !jsondata.get("height").equals("")) {
				   BigDecimal height = new BigDecimal(String.valueOf(jsondata.get("height")));
				   oodt.setHeight(height);
				   }
				   if (jsondata.get("weight") != null && !jsondata.get("weight").equals("")) {
				   BigDecimal weight = new BigDecimal(String.valueOf(jsondata.get("weight")));
				   oodt.setWeight(weight);
				   }
				   if (jsondata.get("idealWeight") != null && !jsondata.get("idealWeight").equals("")) {
				   BigDecimal idealWeight = new BigDecimal(String.valueOf(jsondata.get("idealWeight")));
				   oodt.setIdealWeight(idealWeight);
				   }
				   if (jsondata.get("varation") != null && !jsondata.get("varation").equals("")) {
						BigDecimal bd = new BigDecimal(String.valueOf(jsondata.get("varation")));
						oodt.setVariation(bd);
					}
				   oodt.setMonth(m);
				   oodt.setObesityHdId(obesistyId);
				   session.save(oodt);
			
				t.commit();
				Result = "200";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}*/
		return Result;
	}

	@Override
	public MasEmployee checkEmp(Long i) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasEmployee.class);
		cr.add(Restrictions.eq("employeeId", i));
		MasEmployee list = (MasEmployee) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}
	
	@Override
	public Users checkUser(Long i) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Users.class);
		cr.add(Restrictions.eq("userId", i));
		Users list = (Users) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public Patient checkPatient(Long i) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Patient.class);
		cr.add(Restrictions.eq("patientId", i));
		Patient list = (Patient) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public MasAdministrativeSex checkGender(Long i) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasAdministrativeSex.class);
		cr.add(Restrictions.eq("administrativeSexId", i));
		MasAdministrativeSex list = (MasAdministrativeSex) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public MasRelation checkRelation(Long i) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasRelation.class);
		cr.add(Restrictions.eq("relationId", i));
		MasRelation list = (MasRelation) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public Map<String,Object> getPreconsulationVisit(HashMap<String,Object> jsonData) {
		
		List<Visit> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String opdPre = "opdPre";
			String checkOdPre = jsonData.get("opdPre").toString();

			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date from = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE,59);
			Date to = cal.getTime();

			//System.out.println(from);
			//System.out.println(to);
			Criteria cr = null;
			;
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");
			long hospitalId = Long.parseLong((String.valueOf(jsonData.get("hospitalId"))));
			//System.out.println("PreConsulation hospitalId "+hospitalId);
			String serviceNo = (String) jsonData.get("serviceNo");
			String patientName = (String) jsonData.get("patientName");
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			Long departmentTypeIdGeneral=null;
			String departmentTypeCodeGeneral = HMSUtil.getProperties("adt.properties", "DEPARTMENT_TYPE_CODE_GENERAL").trim();
			 if(departmentTypeCodeGeneral!=null && departmentTypeCodeGeneral!="")
				{	
					MasDepartment mss=null;
					mss = getDepartmentCode(departmentTypeCodeGeneral);
					if(mss!=null)
					departmentTypeIdGeneral=mss.getDepartmentId();
					//System.out.println("departmentTypeCodeGeneral :::::::" +departmentTypeIdGeneral);
				}
			 Long departmentTypeIdChild=null;
				String departmentTypeCodeChild = HMSUtil.getProperties("adt.properties", "DEPARTMENT_TYPE_CODE_CHILD").trim();
				 if(departmentTypeCodeChild!=null && departmentTypeCodeChild!="")
					{	
						MasDepartment mss=null;
						mss = getDepartmentCode(departmentTypeCodeChild);
						if(mss!=null)
						departmentTypeIdChild=mss.getDepartmentId();
						//System.out.println("departmentTypeIdChild :::::::" +departmentTypeIdChild);
					}
				 Long departmentTypeIdMaternity=null;
					String departmentTypeCodeMaternity = HMSUtil.getProperties("adt.properties", "DEPARTMENT_TYPE_CODE_MATERNITY").trim();
					 if(departmentTypeCodeMaternity!=null && departmentTypeCodeMaternity!="")
						{	
							MasDepartment mss=null;
							mss = getDepartmentCode(departmentTypeCodeMaternity);
							if(mss!=null)
							departmentTypeIdMaternity=mss.getDepartmentId();
							//System.out.println("departmentTypeIdMaternity :::::::" +departmentTypeIdMaternity);
						}
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if(jsonData.get("opdType")!=null && jsonData.get("opdType").equals("F"))
			{
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masHospital", "mh")
						 .createAlias("visit.patient", "patient")
						 .createAlias("masAppointmentType", "masAppointmentType") 
						 .add(Restrictions.eq("masAppointmentType.appointmentTypeCode", "OPD").ignoreCase())
						.addOrder(Order.asc("tokenNo"));
				
			
			}
			else
			{
			  cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masHospital", "mh")
					.createAlias("visit.patient", "patient")
					.createAlias("masAppointmentType", "masAppointmentType") 
					.add(Restrictions.eq("masAppointmentType.appointmentTypeCode", "OPD").ignoreCase())
					.add(Restrictions.ne("departmentId",departmentTypeIdGeneral))
					.add(Restrictions.ne("departmentId",departmentTypeIdChild))
					.add(Restrictions.ne("departmentId",departmentTypeIdMaternity))
					.addOrder(Order.asc("tokenNo"));
			}  
			Criterion c1 = null;
			Criterion c2 = Restrictions.between("visitDate", from, to);
			Criterion c3 = Restrictions.eq("mh.hospitalId", hospitalId);
			Criterion c4 = null;
			if(jsonData.get("opdType")!=null && jsonData.get("opdType").equals("F"))
			{
			c4= Restrictions.or(Restrictions.eq("departmentId", departmentTypeIdGeneral), Restrictions.eq("departmentId", departmentTypeIdChild), Restrictions.eq("departmentId", departmentTypeIdMaternity));
			}
			if (checkOdPre.equalsIgnoreCase("C")) {
				c1 = Restrictions.eq("visitStatus", "c");
			}
			else {
				if (checkOdPre.equals(opdPre)) {

					c1 = Restrictions.eq("visitStatus", "w");
				} else {
					c1 = Restrictions.or(Restrictions.eq("visitStatus", "w"), Restrictions.eq("visitStatus", "p"));

				}
			}

			if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {
				 cr.add(Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase());
			}
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {
				String pName = "%" + patientName + "%";
				  cr.add(Restrictions.like("patient.patientName", pName).ignoreCase());
			}
			
			if(jsonData.get("opdType")!=null && jsonData.get("opdType").equals("F"))
			{
		 	   cr.add(c1).add(c2).add(c3).add(c4);
			}
			else
			{
				cr.add(c1).add(c2).add(c3);
			}
			list = cr.list();
			count = list.size();
			//System.out.println(count);
			map.put("count", count);

			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
			cr = cr.setMaxResults(pagingSize);
			list = cr.list();

			//System.out.println("");
			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String,Object> getVisit(HashMap<String,Object> jsonData) {
		
		List<Visit> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String opdPre = "opdPre";
			String checkOdPre = jsonData.get("opdPre").toString();

			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date from = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE,59);
			Date to = cal.getTime();

			String hqlQery="";
			Criteria cr = null;
			;
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");
			
			
			// changes done by Rahul for all unit
			//long hospitalId = Long.parseLong((String.valueOf(jsonData.get("hospitalId"))));
			Long [] hospitalList=null;
			String [] hospitals= jsonData.get("hospitalId").toString().split(",");
		    hospitalList = HMSUtil.convertFromStringToLongArray(hospitals);
			
			Long departmentIdFilter=null;
		//	Long departmentId = Long.parseLong((String.valueOf(jsonData.get("departmentId"))));
			String mobileNo = (String) jsonData.get("mobileNo");
			String patientName = (String) jsonData.get("patientName");
			if(jsonData.get("departmentId")!=null && !jsonData.get("departmentId").equals(""))
			{	
			 departmentIdFilter=Long.parseLong((String) jsonData.get("departmentId"));
			}
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
						
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if(jsonData.get("opdType")!=null && jsonData.get("opdType").equals("F"))
			{
				cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						 .createAlias("visit.patient", "patient")
						 //.createAlias("masAppointmentType", "masAppointmentType") 
						 //.add(Restrictions.eq("masAppointmentType.appointmentTypeCode", "OPD").ignoreCase())
						.addOrder(Order.asc("tokenNo"));
				
				
			}
			else
			{
				   cr = session.createCriteria(Visit.class, "visit").createAlias("visit.masMmu", "mh")
						 .createAlias("visit.patient", "patient")
						 //.createAlias("masAppointmentType", "masAppointmentType") 
						 //.add(Restrictions.eq("masAppointmentType.appointmentTypeCode", "OPD").ignoreCase())
						
						.addOrder(Order.asc("tokenNo")); 
			}
			Criterion c1 = null;
			Criterion c2 = Restrictions.between("visitDate", from, to);
			if (jsonData.containsKey("dateOfOpd") && jsonData.get("dateOfOpd")!= null && !jsonData.get("dateOfOpd").equals("") && !jsonData.get("dateOfOpd").equals("null")) {
				 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	                Date insDate = formatter.parse(jsonData.get("dateOfOpd").toString());
	                //cr.add(Restrictions.eq("visitDate", insDate)); 
	                //Date date1 = new Date(insDate);
	    			Calendar cal1 = Calendar.getInstance();
	    			cal1.setTime(insDate);
	    			cal1.set(Calendar.HOUR_OF_DAY, 0);
	    			cal1.set(Calendar.MINUTE, 0);
	    			cal1.set(Calendar.SECOND, 0);
	    			cal1.set(Calendar.MILLISECOND, 0);
	    			Date from1 = cal1.getTime();
	    			cal1.set(Calendar.HOUR_OF_DAY, 23);
	    			cal1.set(Calendar.MINUTE,59);
	    			Date to1 = cal1.getTime();
	    			c2 = Restrictions.between("visitDate", from1, to1);
	    			to=to1;
	    			from=from1;
				//cr.add(Restrictions.eq("departmentId", departmentIdFilter));
			}
			Criterion c3 = Restrictions.in("mh.mmuId", hospitalList);
			

			if (checkOdPre.equalsIgnoreCase("C")) {
				c1 = Restrictions.eq("visitStatus", "c");
				hqlQery="select v.visit_id from visit v join Patient p on v.patient_id=p.patient_id where v.visit_status='c' and v.mmu_id in("+jsonData.get("hospitalId").toString()+") and v.visit_date between '"+from+"' and '"+to+"'";
			}
			else {
				if (checkOdPre.equals(opdPre)) {

					c1 = Restrictions.eq("visitStatus", "w");
					hqlQery="select v.visit_id from visit v join Patient p on v.patient_id=p.patient_id  where v.visit_status='W' and v.mmu_id in("+jsonData.get("hospitalId").toString()+") and v.visit_date between '"+from+"' and '"+to+"'";
					
				} else {
					c1 = Restrictions.eq("visitStatus", "w").ignoreCase();
					hqlQery="select v.visit_id from visit v join Patient p on v.patient_id=p.patient_id  where v.visit_status='W' and v.mmu_id in("+jsonData.get("hospitalId").toString()+") and v.visit_date between '"+from+"' and '"+to+"'";
				}
			
			}

			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {
				String mNumber = "%" + mobileNo + "%";
				cr.add(Restrictions.like("patient.mobileNumber", mNumber));
				hqlQery+=" and UPPER(p.mobile_number) like UPPER('"+mNumber+"')";
			}
			if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {
				String pName = "%" + patientName + "%";
				  cr.add(Restrictions.like("patient.patientName", pName).ignoreCase());
				  //cr.add(Restrictions.or(Restrictions.like("patient.patientName", pName,MatchMode.ANYWHERE),Restrictions.eq("patient.patientName", patientName).ignoreCase() ));
				  hqlQery+=" and UPPER(p.patient_name) like UPPER('"+pName+"')";
			}
						
			if(jsonData.get("opdType")!=null && jsonData.get("opdType").equals("F"))
			{
		 	   cr.add(c1).add(c2).add(c3);
			}
			else
			{
				cr.add(c1).add(c2).add(c3);
			}
			
			Query queryHiber = (Query) session.createSQLQuery(hqlQery);
			queryHiber.setCacheable(true);
			List<Object[]> objectList = (List<Object[]>) queryHiber.list();
			//System.out.println("objectList="+objectList.size());
			//list = cr.list();
			//count = 115;
			map.put("count", objectList.size());
			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
	    	cr = cr.setMaxResults(pagingSize);
			list = cr.list();

			//System.out.println("");
			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}

	/////////////////////// for visit,patient and OpdPatinetDetails for get
	/////////////////////// Visit/////////////////////

	@Override
	public List<Visit> getPatientVisit(Long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Visit.class);
		cr.add(Restrictions.eq("visitId", visitId));
		List<Visit> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List<OpdPatientDetail> getVitalRecord(Long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(OpdPatientDetail.class);
		cr.add(Restrictions.eq("visitId", visitId));
		List<OpdPatientDetail> list = cr.list();
		//System.out.println(list.size());
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}
	
	
	public List<Object[]> getPatientPreviousHistory(Long patientId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasIdealWeight> list = null;
		List<Object[]> searchList = null;
		try {	
			String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
			String OPD_PATIENT_DETAILS=databaseScema+"."+"OPD_PATIENT_DETAILS";
			//String Query ="SELECT VT.VISIT_DATE,ICD_DIAGNOSIS,WORKING_DIAGNOSIS,CHIEF_COMPLAIN,FAMILY_HISTORY,PAST_MEDICAL_HISTORY,PRESENT_ILLNESS_HISTORY,VT.VISIT_ID FROM  VISIT VT LEFT OUTER JOIN  OPD_PATIENT_DETAILS OPD ON OPD.VISIT_ID=VT.VISIT_ID LEFT OUTER JOIN  OPD_PATIENT_HISTORY OPH ON OPH.VISIT_ID=VT.VISIT_ID  WHERE   VT.PATIENT_ID='"+patientId+"' AND Upper(VT.VISIT_STATUS)='C' ORDER BY VISIT_DATE DESC";
			String Query ="select patient_symptoms,past_history from "+OPD_PATIENT_DETAILS+ " where patient_id='"+patientId+"' " ;
		//System.out.println(Query);
		if (Query != null) 
		{
			searchList = session.createSQLQuery(Query).list();
		} 
		else
		{
			//System.out.println("No Record Found");
		}
		return searchList;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return searchList;
	}
	

	
	@Override
	
	public Map<String,Object> getPreviousVitalRecord(HashMap<String,Object> jsonData){
	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");
		Long patientId=Long.parseLong(jsonData.get("patientId").toString());
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int count = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		
		Criteria cr = session.createCriteria(OpdPatientDetail.class)
				      .addOrder(Order.desc("opdDate"));
		//cr.add(Restrictions.isNotNull("height"));
		cr.add(Restrictions.eq("patientId", patientId));
		List<OpdPatientDetail> list = cr.list();
		
		list = cr.list();
		count = list.size();
		map.put("count", count);

		cr = cr.setFirstResult(pagingSize * (pageNo - 1));
		cr = cr.setMaxResults(pagingSize);
		list = cr.list();
		map.put("list", list);
		//System.out.println(list.size());
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return map;
	}
	
	@Override
	
	public Map<String, Object> getPreviousVisitRecord(Long patientId,HashMap<String, Object> jsonData) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<MasIdealWeight> list = null;
		List<Object[]> searchList = null;
		int count = 0;
		int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
				
		try {	
			String Query ="SELECT  VT.VISIT_DATE,ICD_DIAGNOSIS,patient_symptoms,VT.VISIT_ID,MD.DEPARTMENT_NAME,U.USER_NAME,MU.MMU_NAME FROM  visit VT LEFT OUTER JOIN  opd_Patient_Details OPD ON OPD.VISIT_ID=VT.VISIT_ID LEFT OUTER JOIN  mas_Department MD ON MD.DEPARTMENT_ID=VT.DEPARTMENT_ID LEFT OUTER JOIN  users U ON OPD.DOCTOR_ID=U.USER_ID LEFT OUTER JOIN  MAS_MMU MU ON MU.MMU_ID=VT.MMU_ID WHERE   VT.PATIENT_ID='"+patientId+"' AND Upper(VT.VISIT_STATUS)='C' ORDER BY VISIT_DATE DESC";
			//String Query ="SELECT  VT.VISIT_DATE,ICD_DIAGNOSIS,WORKING_DIAGNOSIS,past_history,patient_symptoms,VT.VISIT_ID,MD.DEPARTMENT_NAME,U.FIRST_NAME FROM  "+visitTable+" VT  LEFT OUTER JOIN  "+opdPatientDetailsTable+" OPD ON OPD.VISIT_ID=VT.VISIT_ID  LEFT OUTER JOIN  "+masDepartmentTable+" MD ON MD.DEPARTMENT_ID=VT.DEPARTMENT_ID LEFT OUTER JOIN  "+usersTable+" U ON OPD.DOCTOR_ID=U.USER_ID WHERE   VT.PATIENT_ID='"+patientId+"' AND Upper(VT.VISIT_STATUS)='C' AND VT.APPOINTMENT_TYPE_ID='1' ORDER BY VISIT_DATE DESC";
			
		//System.out.println(Query);
		if (Query != null) 
		{
			searchList = session.createSQLQuery(Query).list();
			
		} 
		else
		{
			//System.out.println("No Record Found");
		}
		Query query = session.createSQLQuery(Query.toString());
		count = searchList.size();
	     
	 	 int listCount = searchList.size();
	     query = query.setFirstResult(pagingSize * (pageNo - 1));
	     query = query.setMaxResults(pagingSize);
	     searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;	
	}
	
	

	@Override
	public int listPaginatedVisit(int firstResult, int maxResults) {
		int paginatedCount = 0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Criteria criteria = session.createCriteria(Visit.class);
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);
			List<Visit> visit = (List<Visit>) criteria.list();
			if (visit != null) {
				paginatedCount = visit.size();
				//System.out.println("Total Results: " + paginatedCount);
				for (Visit visit1 : visit) {
					//System.out.println("Retrieved visit using Criteria. Name: " + visit.get(0));
				}
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return paginatedCount;
	}

	@Override
	public List<Patient> getPatinet() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Patient.class);
		List<Patient> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List<Object[]> getSearchPatinet(String patinetName) {
		Session session = sessionFactory.getCurrentSession();
		List<Object[]> searchList;
		String Query = "select p,v from patient p,visit v where PATIENT_NAME='" + patinetName
				+ "' AND visit.patient_id = patient.patient_id ";
		searchList = session.createSQLQuery(Query).list();
		//System.out.println(Query);
		// sessionFactory.close();
		return searchList;
	}

	@Override
	public List<MasEmployee> getEmployee() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasEmployee.class);
		List<MasEmployee> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	/////////////////// check Patient Family History /////////////////
	@Override
	public List<PatientFamilyHistory> getFamilyHistory() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(PatientFamilyHistory.class);
		List<PatientFamilyHistory> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	

	////////////////// Update Vital Details ////////////////////
	@Override
	public OpdPatientDetail checkVisitOpdPatientDetails(Long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(OpdPatientDetail.class);
		cr.add(Restrictions.eq("visitId", visitId));
		OpdPatientDetail list = (OpdPatientDetail) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}


	//////////////////// save opd details on opdTemplate ////////////////////
	//////////////////// ////////////////////
	@Override
	public String opdTemplate(OpdTemplate ob, OpdTemplateInvestigation opdinv) {

		String Result = null;
		Transaction t = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdTemplate.class);
			cr.add(Restrictions.eq("visitId", ob.getTemplateId()));
			OpdTemplate list = (OpdTemplate) cr.uniqueResult();
			t = session.beginTransaction();
			
			Serializable id = session.save(ob);

			// OpdTemplateInvestigation opdtInv= new OpdTemplateInvestigation();
			opdinv.setTemplateId(Long.valueOf(id.toString()));

			//System.out.println("hi this is id" + id);

			Serializable id2 = session.save(opdinv);

			//System.out.println("hi this is id2=====" + id2);
			t.commit();
			Result = "200";
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			// }
		} catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			Result = null;
			e.printStackTrace();
		}
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return Result;
	}

	@Override
	public String opdTemplatenewMethod(OpdTemplate opdTemp, List<OpdTemplateInvestigation> opdInvestigationList) {

		String result = null;
		Transaction t = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();

			Criteria cr = session.createCriteria(OpdTemplate.class);
			cr.add(Restrictions.eq("templateName", opdTemp.getTemplateName()));
			cr.add(Restrictions.eq("doctorId", opdTemp.getDoctorId()));
			OpdTemplate opdTemplate = (OpdTemplate) cr.uniqueResult();
			if(opdTemplate!=null) {
				
				return "Template Name Already Exists";
			}
			Serializable id = session.save(opdTemp);

			for (OpdTemplateInvestigation single : opdInvestigationList) {

				single.setTemplateId(Long.valueOf(id.toString()));
				session.save(single);

			}

			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			t.rollback();
            ex.printStackTrace();
			return "500";

		}

		return "200";
	}
	
	@Override
	public String opdupdateInvestigationTemplate(OpdTemplate opdTemp, List<OpdTemplateInvestigation> opdInvestigationList) {

		String result = null;
		Transaction t = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();
			Serializable id = null;
			Criteria cr = session.createCriteria(OpdTemplate.class);
			cr.add(Restrictions.eq("templateName", opdTemp.getTemplateName()));
			cr.add(Restrictions.eq("doctorId", opdTemp.getDoctorId()));
			OpdTemplate opdTemplate = (OpdTemplate) cr.uniqueResult();
			Long s=opdTemp.getTemplateId();	
			if(opdTemp.getTemplateId().equals(""))
			{	
				if(opdTemplate!=null) {
					
					return "Template Name Already Exists";
				}
			}
			if(opdTemplate!=null)
			{	
			session.clear();
			session.saveOrUpdate(opdTemp);
			}
			else
			{	
				id = session.save(opdTemp);
			}
			
               for (OpdTemplateInvestigation single : opdInvestigationList) {
            	   if(opdTemplate==null)
            	   {   
					single.setTemplateId(Long.valueOf(id.toString()));
            	   }
					session.save(single);
	
				  }
			
			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			t.rollback();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
            ex.printStackTrace();
			return "500";

		}
		
		return "200";
	}
	
	@Override
	public String deleteInvestigationTemplate(HashMap<String, Object> jsondata) {
		String result = null;
		Transaction tx = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			Long templateInvestigationId = Long.parseLong(jsondata.get("templateInvestigationId").toString());
			if (templateInvestigationId != null && !templateInvestigationId.equals("")) {
				OpdTemplateInvestigation ddt = (OpdTemplateInvestigation) session.createCriteria(OpdTemplateInvestigation.class)
						.add(Restrictions.eq("templateInvestigationId", templateInvestigationId)).uniqueResult();
				session.delete(ddt);
				tx.commit();
				result = "success";
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			tx.rollback();
			result = "error";
			return result;
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;

	}
	
	@Override
	public String saveTreatTemplatenewMethod(OpdTemplate opdTemp, List<OpdTemplateTreatment> opdTreatmentTempList) {

		String result = null;
		Transaction t = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();
			Criteria cr = session.createCriteria(OpdTemplate.class);
			cr.add(Restrictions.eq("templateName", opdTemp.getTemplateName()));
			cr.add(Restrictions.eq("doctorId", opdTemp.getDoctorId()));
			OpdTemplate opdTemplate = (OpdTemplate) cr.uniqueResult();
			if(opdTemplate!=null) {

				return "Template Name Already Exists";
			}
			Serializable id = session.save(opdTemp);

			for (OpdTemplateTreatment single : opdTreatmentTempList) {

				single.setTemplateId(Long.valueOf(id.toString()));
				session.save(single);

			}

			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			t.rollback();

			return "500";

		}

		return "200";
	}
	

	@Override
	public String saveOpdInvestigation(DgOrderhd orderhd, List<DgOrderdt> dgorderdt) {

		String result = null;
		Transaction t = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();
			Serializable id = session.save(orderhd);

			for (DgOrderdt single : dgorderdt) {

				single.setOrderhdId(Long.valueOf(id.toString()));
				session.save(single);

			}

			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
			t.rollback();

			return "500";

		}

		return "200";
	}

	@Override
	public String saveOpdPrescription(PatientPrescriptionHd pphd, List<PatientPrescriptionDt> patientPrescDT) {

		String result = null;
		Transaction t = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();
			Serializable id = session.save(pphd);

			for (PatientPrescriptionDt single : patientPrescDT) {

				single.setPrescriptionHdId(Long.valueOf(id.toString()));
				session.save(single);

			}

			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
			t.rollback();

			return "500";

		}

		return "200";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String saveOpdPatientDetails(HashMap<String, Object> getJsondata) {
		JSONArray newForm=new JSONArray(getJsondata.get("opdMainData").toString());
		JSONObject jsondata = (JSONObject) newForm.get(0);
		
		Date currentDate = ProjectUtils.getCurrentDate();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(OpdPatientDetail.class);
		Transaction tx = session.beginTransaction();
		Long visitId = Long.parseLong((String) jsondata.get("visitId"));
		String aiDiagnosisFlag="N";
		String aiInvestgationFlag="N";
		String aTreatmentFlag="N";
		Long patientId;
		Long hospitalId;
		Long userId;
		Long opdId;
		Long itemTypeNivId=null;
		Long headerNivId = null;
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		String procedureType = null;
		cr.add(Restrictions.eq("visitId", visitId));
		OpdPatientDetail opdlist = (OpdPatientDetail) cr.uniqueResult();
		try {

			if (opdlist != null) {
				opdlist.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
				opdlist.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
				opdlist.setPatientSymptoms(String.valueOf(jsondata.get("symptomsText")));
				opdlist.setPastHistory(String.valueOf(jsondata.get("pastMedicalHistory")));
				opdlist.setOpdDate(ourJavaTimestampObject);
				opdlist.setIcdDiagnosis(String.valueOf(jsondata.get("icdDiagnosis")));
				opdlist.setWorkingDiagnosis(String.valueOf(jsondata.get("workingDiagnosis")));
				opdlist.setHeight(String.valueOf(jsondata.get("height")));
				opdlist.setWeight(String.valueOf(jsondata.get("weight")));
				opdlist.setIdealWeight(String.valueOf(jsondata.get("idealWeight")));
				//opdlist.setRecmndMedAdvice(String.valueOf(jsondata.get("otherPresecription")));
				//opdlist.setOtherInvestigation(String.valueOf(jsondata.get("otherInvestigation")));
				opdlist.setLastChgDate(ourJavaTimestampObject);
				opdlist.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
				opdlist.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
			
				if (jsondata.get("varation") != null && !jsondata.get("varation").equals("")) {
					Double bd = new Double(String.valueOf(jsondata.get("varation")));
					opdlist.setVaration(bd);
				}
				opdlist.setTemperature(String.valueOf(jsondata.get("temperature")));
				opdlist.setBpSystolic(String.valueOf(jsondata.get("bp")));
				opdlist.setBpDiastolic(String.valueOf(jsondata.get("bp1")));
				opdlist.setPulse(String.valueOf(jsondata.get("pulse")));
				opdlist.setSpo2(String.valueOf(jsondata.get("spo2")));
				opdlist.setBmi(String.valueOf(jsondata.get("bmi")));
				opdlist.setRr(String.valueOf(jsondata.get("rr")));
				
				//List<String> listofInvestigationVal = (List<String>) jsondata.get("listofInvestigation");
		    	JSONArray listofInvestigationVal = new JSONArray(jsondata.get("listofInvestigation").toString());
                //System.out.println("listofInvestigationVal "+listofInvestigationVal);
				if(listofInvestigationVal.isNull(0)) {
					opdlist.setLabFlag("N");
				}
				else
				{
					opdlist.setLabFlag("Y");
				}
				
				//List<String> listofPrescriptionVal = (List<String>) jsondata.get("listofPrescription");
				JSONArray listofPrescriptionVal = new JSONArray(jsondata.get("listofPrescription").toString());
				if(listofPrescriptionVal.isNull(0)) {
					opdlist.setDispensaryFlag("N");
				}
				else
				{
					opdlist.setDispensaryFlag("Y");
				}
				
				//List<String> listofReferallHDVal = (List<String>) jsondata.get("listofReferallHD");
				JSONArray listofReferallHDVal = new JSONArray(jsondata.get("listofReferallHD").toString());
				
				if(listofReferallHDVal.isNull(0)) {
					opdlist.setReerralFlag("N");
				}
				else
				{
					opdlist.setReerralFlag("Y");
				}
				
				
				if(jsondata.get("followUpFlagValue").equals("Y"))
				{
					Long mmuIdForCamp=Long.parseLong(jsondata.get("hospitalId").toString());
					String mmuFollowUpDate="";
					try {
					    String startDateString = jsondata.get("followupDate").toString();
					    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					    if(StringUtils.isNotEmpty(startDateString)) {
					    //System.out.println(sdf2.format(sdf.parse(startDateString)));
					    mmuFollowUpDate=sdf2.format(sdf.parse(startDateString));
					    }
					    } catch (ParseException e) {
					    e.printStackTrace();
					}
					if(mmuIdForCamp!=null && mmuFollowUpDate!=null && StringUtils.isNotEmpty(mmuFollowUpDate)) 
					{	
					Map<String, Object> nextcampDetails=getCampAdrressByDate(mmuIdForCamp,mmuFollowUpDate);
					List<Object[]> listCamp = (List<Object[]>) nextcampDetails.get("list");
					for (Object[] row : listCamp) {
						if (row[0]!= null) {
							String location=row[0].toString();
							opdlist.setFollowUpLocation(location);
							
						}
						if (row[1]!= null) {
							String landmark=row[1].toString();
							opdlist.setFollowUpLandmarks(landmark);
						}
					 }
					}
					
					opdlist.setFollowUpFlag("Y");
					if(jsondata.get("followUpdays")=="17")
					{		
					Date followUpDate=HMSUtil.convertStringTypeDateToDateType(jsondata.get("followupDate").toString());
					opdlist.setFollowupDate(followUpDate);
					}
					if(jsondata.get("followUpdays")=="16")
					{
            			opdlist.setFollowUpDays((long) 3);
						opdlist.setSosFlag("Y");
						Date followUpDate=HMSUtil.convertStringTypeDateToDateType(jsondata.get("followupDate").toString());
						opdlist.setFollowupDate(followUpDate);
					}
		    		else
					{
		    		if(StringUtils.isNotEmpty(jsondata.get("followUpdays").toString())) {
		    			opdlist.setFollowUpDays(Long.parseLong(String.valueOf(jsondata.get("followUpdays"))));
						Date followUpDate=HMSUtil.convertStringTypeDateToDateType(jsondata.get("followupDate").toString());
						opdlist.setFollowupDate(followUpDate);
					}
					}
				}
				if(jsondata.get("markMlc").equals("Y"))
				{
					opdlist.setMlcFlag("Y");
					opdlist.setPoliceStation(String.valueOf(jsondata.get("policeStation")));
					opdlist.setTreatedAs(String.valueOf(jsondata.get("treatedAs")));
					opdlist.setPoliceName(String.valueOf(jsondata.get("mlcPloiceName")));
					opdlist.setDesignation(String.valueOf(jsondata.get("mlcDesignation")));
					opdlist.setIdNumber(String.valueOf(jsondata.get("mlcIdNumber")));
					
				}
				opdlist.setRecmmdMedAdvice(String.valueOf(jsondata.get("doctorAdditionalNote")));
				opdlist.setEcgRemarks(String.valueOf(jsondata.get("ecgRemarks")));
				//System.out.println("jsondata.get(\"campId\")="+jsondata.get("campId"));
				if (jsondata.get("campId") != null) {
					
					//System.out.println("jsondata.get(\"campId\")="+jsondata.get("campId"));
					opdlist.setCampId(Long.parseLong(jsondata.get("campId").toString()));
				}
				//opdlist.setmmu(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
				session.update(opdlist);
				opdId = opdlist.getOpdPatientDetailsId();
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if (visit != null) {
						visit.setVisitStatus("c");
						if(jsondata.get("userId")!=null && jsondata.get("userId")!="")
						{	
						visit.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
						}
						session.update(visit);
					}
				}
		
				////////////////////////////Patient Symptons ///////////////////////////////////////////////////////////////
				if (jsondata.get("symptomsValue") != null) {
					//List<HashMap<String, Object>> listSymptomsValue = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("symptomsValue");
					JSONArray listSymptomsValue = new JSONArray(jsondata.get("symptomsValue").toString());
					if (listSymptomsValue != null) {

						List<PatientSymptom> patientSymptons = new ArrayList<>();
						//for (HashMap<String, Object> listSymptomsOpd : listSymptomsValue) {
						for (int k = 0; k < listSymptomsValue.length(); k++) {
							JSONObject listSymptomsOpd = listSymptomsValue.getJSONObject(k);
							
							String inputString = String.valueOf(listSymptomsOpd.get("symptomsId"));
							//System.out.println("Special Char sympotonsId"+inputString);
					        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
					        for (int i=0; i < inputString.length() ; i++)
					        {
					            char ch = inputString.charAt(i);
					            if(specialCharactersString.contains(Character.toString(ch)))
					            {
					                //System.out.println(inputString+ " contains special character");
					                break;
					            }    
					       else if(i == inputString.length()-1)     
					       {    
					    	PatientSymptom ps = new PatientSymptom();
							ps.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
							ps.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
							ps.setLastChgDate(ourJavaTimestampObject);
							ps.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
							ps.setMmuId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
							ps.setOpdPatientDetailId(opdId);
							ps.setSymptomId(Long.parseLong(String.valueOf(listSymptomsOpd.get("symptomsId"))));
							if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
								
								ps.setCampId(Long.parseLong(jsondata.get("campId").toString()));
							}
							patientSymptons.add(ps);
							session.save(ps);
					       }
					     }      
						}
					}
				}
						
				///////////////////////// Discharge ICD Code Details Entry///////////////////////// /////////////////////////////
				if (jsondata.get("icdValue") != null) {
					//List<HashMap<String, Object>> listIcdValue = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("icdValue");
					JSONArray listIcdValue = new JSONArray(jsondata.get("icdValue").toString());
					if (listIcdValue != null) {

						List<DischargeIcdCode> dischargeDT = new ArrayList<>();
						//for (HashMap<String, Object> icdOpd : listIcdValue) {
						for (int i = 0; i < listIcdValue.length(); i++) {
							JSONObject icdOpd = listIcdValue.getJSONObject(i);
							DischargeIcdCode disIcdCode = new DischargeIcdCode();
							disIcdCode.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
							disIcdCode.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
							disIcdCode.setLastChgDate(ourJavaTimestampObject);
							disIcdCode.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
							if(icdOpd.get("auditDiagonsisValue").equals("F"))
							{
							 disIcdCode.setActionFlag("F");
							 aiDiagnosisFlag="Y";
							}
							else
							{
								disIcdCode.setActionFlag("T");
								
							}
							disIcdCode.setOpdPatientDetailsId(opdId);
							disIcdCode.setIcdId(Long.parseLong(String.valueOf(icdOpd.get("icdId"))));
							if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
								
								disIcdCode.setCampId(Long.parseLong(jsondata.get("campId").toString()));
							}
							dischargeDT.add(disIcdCode);
							session.save(disIcdCode);
							String markDisease=String.valueOf(icdOpd.get("markDiseaseVal"));
							String markInfectious =String.valueOf(icdOpd.get("markInfectiousVal"));
							Long icdId=Long.parseLong(String.valueOf(icdOpd.get("icdId")));
							if(icdId!=null )
							{	
								String qryStringHd  ="update MasIcd v set v.communicableFlag='"+markDisease+"',v.infectionsFlag='"+markInfectious+"'  where v.icdId='"+icdId+"' " ;
								Query queryHd = session.createQuery(qryStringHd);
						        int countHd = queryHd.executeUpdate();
								//System.out.println(countHd + " Record(s) Visit Reject Records Updated.");
							
							}
							
							
                        
						}
						 if(aiDiagnosisFlag.equals("Y"))
							{
							 opdlist.setDiagnosisFlag("Y");
							}
							else
							{
								opdlist.setDiagnosisFlag("N");	
							}
					}
				}
		
				///////////// Referal Details Section ////////////////////////////////
				if (jsondata.get("listofReferallHD") != null) {
					//List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofReferallHD");
					JSONArray list = new JSONArray(jsondata.get("listofReferallHD").toString());
					if (list != null) {
						//for (HashMap<String, Object> map : list) {
						for (int i = 0; i < list.length(); i++) {
							JSONObject map = list.getJSONObject(i);
							
							String extId = (String) map.get("extHospitalId");
							Long empID = Long.parseLong(extId);
							String referralNote = (String) map.get("referralNote");
							String doctorNote = (String) map.get("doctorNote");
							patientId = Long.parseLong((String) map.get("patientId"));
							hospitalId = Long.parseLong(String.valueOf(map.get("hospitalId")));
							// String treatmentType=(String)map.get("treatmentType");
							ReferralPatientHd header = null;
							if (empID != null && opdId != null) {
								header = getReferralPatientHdByExeHosAndOpdPd(empID, opdId);
							}
							Long id = null;
							if (header == null) {

								header = new ReferralPatientHd();
								header.setPatientId(patientId);
								header.setMmuId(hospitalId);
								header.setExtHospitalId(empID);
								header.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								header.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								header.setLastChgDate(ourJavaTimestampObject);
								header.setReferralIniDate(currentDate);
								header.setTreatmentType("E");
								header.setReferralIniDate(currentDate);
								header.setReferralNote(String.valueOf(referralNote));
								header.setDoctorNote(String.valueOf(doctorNote));
								header.setStatus("W");
								header.setOpdPatientDetailsId(opdId);
								if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
									
									header.setCampId(Long.parseLong(jsondata.get("campId").toString()));
								}
								id = Long.parseLong(session.save(header).toString());
							} else {
								id = header.getRefrealHdId();
							}
							Long extHosId = header.getExtHospitalId();
							//System.out.println(extHosId);
							//List<HashMap<String, Object>> referralList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofReferalDT");
							JSONArray referralList = new JSONArray(map.get("listofReferalDT").toString());
							//for (HashMap<String, Object> reffralMap : referralList) {
							for (int k = 0; k < referralList.length(); k++) {
								JSONObject reffralMap = referralList.getJSONObject(i);
								ReferralPatientDt refDt = new ReferralPatientDt();
								refDt.setLastChgDate(ourJavaTimestampObject);
								refDt.setExtDepartment(String.valueOf(reffralMap.get("extDepartment")));
								refDt.setRefrealHdId(id);
								session.save(refDt);

							}
						}

					}
				}
				////////////////////////////// Internal Refferal
				////////////////////////////// ////////////////////////////////////////////
				if (jsondata.get("listofInternalReferallHD") != null) {
					//List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofInternalReferallHD");
					JSONArray list = new JSONArray(jsondata.get("listofInternalReferallHD").toString());
					if (list != null) {
						for (int i = 0; i < list.length(); i++) {
							JSONObject map = list.getJSONObject(i);

							String intHosId = (String) map.get("intHospitalId");
							Long empID = Long.parseLong(intHosId);
							String referralNote = (String) map.get("referralNote");
							String doctorNote = (String) map.get("doctorNote");
							patientId = Long.parseLong((String) map.get("patientId"));
							hospitalId = Long.parseLong(String.valueOf(map.get("hospitalId")));
							// String treatmentType=(String)map.get("treatmentType");
							ReferralPatientHd intHeader = null;
							if (empID != null && opdId != null) {
								intHeader = getInternalReferralPatientHdByOpdPd(empID, opdId);
							}
							Long id = null;
							if (intHeader == null) {

								intHeader = new ReferralPatientHd();
								intHeader.setPatientId(patientId);
								intHeader.setMmuId(hospitalId);
								intHeader.setIntHospitalId(empID);
								intHeader.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								intHeader.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								intHeader.setLastChgDate(ourJavaTimestampObject);
								intHeader.setReferralIniDate(currentDate);
								intHeader.setTreatmentType("I");
								intHeader.setReferralIniDate(currentDate);
								intHeader.setReferralNote(String.valueOf(referralNote));
								intHeader.setDoctorNote(String.valueOf(doctorNote));
								intHeader.setStatus("W");
								if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
									
									intHeader.setCampId(Long.parseLong(jsondata.get("campId").toString()));
								}
								intHeader.setOpdPatientDetailsId(opdId);

								id = Long.parseLong(session.save(intHeader).toString());
							} else {
								id = intHeader.getRefrealHdId();
							}
							Long intHospitalId = intHeader.getIntHospitalId();
							//System.out.println(intHospitalId);
							//List<HashMap<String, Object>> diagnosisList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofIntReferalDT");
							JSONArray diagnosisList = new JSONArray(map.get("listofIntReferalDT").toString());
							
							//for (HashMap<String, Object> diagnosisMap : diagnosisList) {
							for (int k = 0; k < diagnosisList.length(); k++) {
								JSONObject diagnosisMap = diagnosisList.getJSONObject(i);
								long diagnosisId = Long.parseLong((String) diagnosisMap.get("diagnosisId"));
								ReferralPatientDt refDt = new ReferralPatientDt();
								refDt.setDiagnosisId(diagnosisId);
								refDt.setLastChgDate(ourJavaTimestampObject);
								refDt.setReferralDate(currentDate);
								refDt.setIntDepartmentId(
										Long.valueOf(String.valueOf(diagnosisMap.get("intDepartment"))));
								refDt.setInstruction(String.valueOf(diagnosisMap.get("instruction")));
								refDt.setRefrealHdId(id);
								session.save(refDt);

							}
						}

					}
				}
				

				///////////// Nursing Care Details Section ////////////////////////////////
				if (jsondata.get("listofNursingCareHD") != null) {
					//List<HashMap<String, Object>> listNursing = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofNursingCareHD");
					JSONArray listNursing = new JSONArray(jsondata.get("listofNursingCareHD").toString());
					if (listNursing != null) {
						//for (HashMap<String, Object> map : listNursing) {
						for (int i = 0; i < listNursing.length(); i++) {
							JSONObject map = listNursing.getJSONObject(i);
							visitId = Long.parseLong((String) map.get("visitId"));
							patientId = Long.parseLong((String) map.get("patientId"));
							hospitalId = Long.parseLong(String.valueOf(map.get("hospitalId")));
							userId = Long.parseLong(String.valueOf(map.get("userId")));
							// String treatmentType=(String)map.get("treatmentType");
							procedureType = (String) map.get("procedureType");
							ProcedureHd pHd = new ProcedureHd();
							pHd.setVisitId(visitId);
							pHd.setPatientId(patientId);
							pHd.setMmuId(hospitalId);
							pHd.setStatus("N");
							pHd.setRequisitionDate(ourJavaTimestampObject);
							pHd.setLastChgDate(ourJavaTimestampObject);
							pHd.setProcedureType(procedureType);
							pHd.setLastChgBy(userId);
							pHd.setDoctorId(userId);
							pHd.setOpdPatientDetailsId(opdId);
							if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
								
								pHd.setCampId(Long.parseLong(jsondata.get("campId").toString()));
							}
							Long headerId = Long.parseLong(session.save(pHd).toString());
							//List<HashMap<String, Object>> nursingList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofNursingDT");
							JSONArray nursingList = new JSONArray(map.get("listofNursingDT").toString());
							//for (HashMap<String, Object> nursingMap : nursingList) {
							for (int k = 0; k < nursingList.length(); k++) {
								JSONObject nursingMap = nursingList.getJSONObject(k);
								ProcedureDt refNursingDt = new ProcedureDt();
								if (nursingMap.get("nursingId") != null && nursingMap.get("nursingId") != "") {
									Long nursingId = Long.parseLong((String) nursingMap.get("nursingId"));
									refNursingDt.setProcedureId(nursingId);
								}
								refNursingDt.setStatus("N");
								
								refNursingDt.setRemarks(String.valueOf(nursingMap.get("remarks")));
								refNursingDt.setProcedureDate(ourJavaTimestampObject);
								refNursingDt.setLastChgDate(ourJavaTimestampObject);
								refNursingDt.setAppointmentDate(ourJavaTimestampObject);
								refNursingDt.setProcedureHdId(headerId);
								session.save(refNursingDt);

							}
						}

					}
				}
				//////////////////////////////////////// Investigation Section
				//////////////////////////////////////// ////////////////////////////////////////
				if (jsondata.get("listofInvestigation") != null) {
					//List<HashMap<String, Object>> listInvestigation = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofInvestigation");
					JSONArray listInvestigation = new JSONArray(jsondata.get("listofInvestigation").toString());
					if (listInvestigation != null) {
						//for (HashMap<String, Object> map : listInvestigation) {
						for (int i = 0; i < listInvestigation.length(); i++) {
							JSONObject map = listInvestigation.getJSONObject(i);
							// DgOrderhd orderhd = new DgOrderhd();
							DgOrderhd orderhd = null;
							Long headerInveId = null;
							
							Date orderDate = HMSUtil.getTodayFormattedDate();
							//System.out.println("today"+orderDate);//Today's date
							Long visitId11 = (Long.parseLong(String.valueOf(jsondata.get("visitId"))));
							if (orderDate != null && visitId11 != null) {
								orderhd = getOrderDatebyInvestigation(orderDate, visitId11);
							}
						
							if (orderhd == null) {
								orderhd = new DgOrderhd();
								orderhd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
								if(jsondata.get("departmentId")!=null && !jsondata.get("departmentId").equals(""))
								{
								orderhd.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
								}
								orderhd.setOrderDate(orderDate);
								orderhd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
								orderhd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
								// orderhd.setOtherInvestigation(String.valueOf(jsondata.get("otherInvestigation")));
								orderhd.setOrderStatus("P");
								orderhd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
								orderhd.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
									
									orderhd.setCampId(Long.parseLong(jsondata.get("campId").toString()));
								}
								orderhd.setLastChgDate(ourJavaTimestampObject);
								headerInveId = Long.parseLong(session.save(orderhd).toString());
							} else {
								headerInveId = orderhd.getOrderhdId();
							}
							//List<HashMap<String, Object>> invList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofInvestigationDT");
							JSONArray invList = new JSONArray(map.get("listofInvestigationDT").toString());
							//for (HashMap<String, Object> invMap : invList) {
							for (int K = 0; K < invList.length(); K++) {
								JSONObject invMap = invList.getJSONObject(K);
								DgOrderdt ob1 = new DgOrderdt();
								if (invMap.get("investigationId") != null && invMap.get("investigationId") != "") {
									ob1.setInvestigationId(Long.valueOf(invMap.get("investigationId").toString()));
								}
								if (invMap.get("auditInvestigationValue") != null && invMap.get("auditInvestigationValue").equals("F")) {
									ob1.setActionFlag("F");
									aiInvestgationFlag="Y";
								}
								else
								{
									ob1.setActionFlag("T");
									
								}
								ob1.setLastChgDate(ourJavaTimestampObject);
								ob1.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								ob1.setOrderStatus("P");
								ob1.setOrderhdId(headerInveId);
								session.save(ob1);

							}
							if(aiInvestgationFlag.equals("Y"))
							{
								opdlist.setInvestigationFlag("Y");
							}
							else
							{
								opdlist.setInvestigationFlag("N");	
							}
							
						}
						

					}
				}

///////////////////////////////////// NIP and Presecription sections///////////////////////////////////// //////////////////////////////////////

// Presec-Section
				if (jsondata.get("listofPrescription") != null) {
					//List<HashMap<String, Object>> listPrescription = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofPrescription");
					JSONArray listPrescription = new JSONArray(jsondata.get("listofPrescription").toString());
					//List<HashMap<String, Object>> nipCheck = (List<HashMap<String, Object>>) jsondata.get("listofNip");
					Long headerPrescriptionId = null;
					if (listPrescription != null && !listPrescription.isNull(0)) {
						PatientPrescriptionHd pphd = new PatientPrescriptionHd();
						pphd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
						pphd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
						pphd.setStatus(String.valueOf(jsondata.get("prescriptionStatus")));
						if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
							
							pphd.setCampId(Long.parseLong(jsondata.get("campId").toString()));
						}
						pphd.setPrescriptionDate(ourJavaTimestampObject);
						pphd.setLastChgDate(ourJavaTimestampObject);
						//pphd.setInjectionStatus(String.valueOf(jsondata.get("injectionStatus")));
						/*if (nipCheck != null && !nipCheck.equals("")) {
							pphd.setNivStatus("Y");

						} else {
							pphd.setNivStatus(String.valueOf(jsondata.get("nipStatus")));
						}*/
						pphd.setDoctorId(Long.parseLong(jsondata.get("userId").toString()));
						pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
						pphd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
						pphd.setOpdPatientDetailsId(opdId);
						headerPrescriptionId = Long.parseLong(session.save(pphd).toString());
						// pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
						List<PatientPrescriptionDt> patientPrescDT = new ArrayList<>();
						//for (HashMap<String, Object> singleopd : listPrescription) {
						for (int i = 0; i < listPrescription.length(); i++) {
							JSONObject singleopd = listPrescription.getJSONObject(i);
							PatientPrescriptionDt ppdt1 = new PatientPrescriptionDt();
							if (singleopd.get("itemId") != null && singleopd.get("itemId") != "") {
								ppdt1.setItemId(Long.valueOf(singleopd.get("itemId").toString()));
							}
							if (singleopd.get("frequencyId") != null && singleopd.get("frequencyId") != "") {
								ppdt1.setFrequencyId(Long.valueOf(singleopd.get("frequencyId").toString()));
							}
							if (singleopd.get("dosage") != null && singleopd.get("dosage").toString() != "") {
								ppdt1.setDosage(String.valueOf(singleopd.get("dosage")));
							}
							if (singleopd.get("noOfDays") != null && singleopd.get("noOfDays") != "") {
								ppdt1.setNoOfDays(Long.valueOf(singleopd.get("noOfDays").toString()));
							}
							if (singleopd.get("total") != null && singleopd.get("total") != "") {
								ppdt1.setTotal(Long.valueOf(singleopd.get("total").toString().trim()));
							}
							if (singleopd.get("auditTreatmentFlag") != null && singleopd.get("auditTreatmentFlag").equals("F")) {
								ppdt1.setActionFlag("F");
								aTreatmentFlag="Y";
							}
							else
							{
								ppdt1.setActionFlag("T");
								
							}
							ppdt1.setInstruction(String.valueOf(singleopd.get("instruction")));
							//ppdt1.setInjectionStatus("N");
							ppdt1.setStatus("P");
							ppdt1.setLastChgDate(ourJavaTimestampObject);
							// patientPrescDT.add(ppdt1);
							ppdt1.setPrescriptionHdId(headerPrescriptionId);
							;
							session.save(ppdt1);
							
							//MasStoreItem msitDispUpdate = new MasStoreItem();
							Long itemId = Long.parseLong((String) singleopd.get("itemId"));
							Criteria dispUnitId = session.createCriteria(MasStoreItem.class);
							dispUnitId.add(Restrictions.eq("itemId", itemId));
							MasStoreItem msitDispUpdate = (MasStoreItem) dispUnitId.uniqueResult();
							msitDispUpdate.setDispUnitId(Long.parseLong(String.valueOf(singleopd.get("dispunitId"))));
							session.update(msitDispUpdate);
							
							
						}

						if(aTreatmentFlag.equals("Y"))
						{
							opdlist.setPrescriptionFlag("Y");
						}
						else
						{
							opdlist.setPrescriptionFlag("N");
						}

					}

				}
				
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if(aiDiagnosisFlag.equals("Y") ||aiInvestgationFlag.equals("Y")||aTreatmentFlag.equals("Y")) {
						visit.setAiAuditException("Y");						
					}
					else
					{
						visit.setAiAuditException("N");	
					}
					session.update(visit);
				}
			
          
			   }

			else {

				OpdPatientDetail opddetails = new OpdPatientDetail();
				opddetails.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
				opddetails.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
				opddetails.setPatientSymptoms(String.valueOf(jsondata.get("symptomsText")));
				opddetails.setPastHistory(String.valueOf(jsondata.get("pastMedicalHistory")));
				opddetails.setIcdDiagnosis(String.valueOf(jsondata.get("icdDiagnosis")));
				opddetails.setWorkingDiagnosis(String.valueOf(jsondata.get("workingDiagnosis")));
				opddetails.setHeight(String.valueOf(jsondata.get("height")));
				opddetails.setOpdDate(ourJavaTimestampObject);
				//opddetails.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
				opddetails.setWeight(String.valueOf(jsondata.get("weight")));
				opddetails.setIdealWeight(String.valueOf(jsondata.get("idealWeight")));
				opddetails.setLastChgDate(ourJavaTimestampObject);
				opddetails.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
				opddetails.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
				//opddetails.setRecmndMedAdvice(String.valueOf(jsondata.get("otherPresecription")));
				//opddetails.setOtherInvestigation(String.valueOf(jsondata.get("otherInvestigation")));
				
				
				if (jsondata.get("varation") != null && !jsondata.get("varation").equals("")) {
					Double bd = new Double(String.valueOf(jsondata.get("varation")));
					opddetails.setVaration(bd);
				}
				
				opddetails.setTemperature(String.valueOf(jsondata.get("temperature")));
				opddetails.setBpSystolic(String.valueOf(jsondata.get("bp")));
				opddetails.setBpDiastolic(String.valueOf(jsondata.get("bp1")));
				opddetails.setPulse(String.valueOf(jsondata.get("pulse")));
				opddetails.setSpo2(String.valueOf(jsondata.get("spo2")));
				opddetails.setBmi(String.valueOf(jsondata.get("bmi")));
				opddetails.setRr(String.valueOf(jsondata.get("rr")));
				
				//List<String> listofInvestigationVal = (List<String>) jsondata.get("listofInvestigation");
		    	JSONArray listofInvestigationVal = new JSONArray(jsondata.get("listofInvestigation").toString());
                //System.out.println("listofInvestigationVal "+listofInvestigationVal);
				if(listofInvestigationVal.isNull(0)) {
					opddetails.setLabFlag("N");
				}
				else
				{
					opddetails.setLabFlag("Y");
				}
				
				//List<String> listofPrescriptionVal = (List<String>) jsondata.get("listofPrescription");
				JSONArray listofPrescriptionVal = new JSONArray(jsondata.get("listofPrescription").toString());
				if(listofPrescriptionVal.isNull(0)) {
					opddetails.setDispensaryFlag("N");
				}
				else
				{
					opddetails.setDispensaryFlag("Y");
				}
				
				//List<String> listofReferallHDVal = (List<String>) jsondata.get("listofReferallHD");
				JSONArray listofReferallHDVal = new JSONArray(jsondata.get("listofReferallHD").toString());
				
				if(listofReferallHDVal.isNull(0)) {
					opddetails.setReerralFlag("N");
				}
				else
				{
					opddetails.setReerralFlag("Y");
				}
				
				if(jsondata.get("followUpFlagValue").equals("Y"))
				{
					Long mmuIdForCamp=Long.parseLong(jsondata.get("hospitalId").toString());
					String mmuFollowUpDate="";
					try {
					    String startDateString = jsondata.get("followupDate").toString();
					    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					    //System.out.println(sdf2.format(sdf.parse(startDateString)));
					    mmuFollowUpDate=sdf2.format(sdf.parse(startDateString));
					} catch (ParseException e) {
					    e.printStackTrace();
					}
					if(mmuIdForCamp!=null && mmuFollowUpDate!=null) 
					{	
					Map<String, Object> nextcampDetails=getCampAdrressByDate(mmuIdForCamp,mmuFollowUpDate);
					List<Object[]> listCamp = (List<Object[]>) nextcampDetails.get("list");
					for (Object[] row : listCamp) {
						if (row[0]!= null) {
							String location=row[0].toString();
							opddetails.setFollowUpLocation(location);
							
						}
						if (row[1]!= null) {
							String landmark=row[1].toString();
							opddetails.setFollowUpLandmarks(landmark);
						}
					 }
					}
					opddetails.setFollowUpFlag("Y");
					if(jsondata.get("followUpdays")=="17")
					{		
					Date followUpDate=HMSUtil.convertStringTypeDateToDateType(jsondata.get("followupDate").toString());
					opddetails.setFollowupDate(followUpDate);
					}
				   if(jsondata.get("followUpdays")=="16")
					{	
					   opddetails.setFollowUpDays((long) 3);
						opddetails.setSosFlag("Y");
						Date followUpDate=HMSUtil.convertStringTypeDateToDateType(jsondata.get("followupDate").toString());
						opddetails.setFollowupDate(followUpDate);
					
					}
					else
					{
						opddetails.setFollowUpDays(Long.parseLong(String.valueOf(jsondata.get("followUpdays"))));
						Date followUpDate=HMSUtil.convertStringTypeDateToDateType(jsondata.get("followupDate").toString());
						opddetails.setFollowupDate(followUpDate);
						
					}
			    }
				if(jsondata.get("markMlc").equals("Y"))
				{
					opddetails.setMlcFlag("Y");
					opddetails.setPoliceStation(String.valueOf(jsondata.get("policeStation")));
					opddetails.setTreatedAs(String.valueOf(jsondata.get("treatedAs")));
					opddetails.setPoliceName(String.valueOf(jsondata.get("mlcPloiceName")));
					opddetails.setDesignation(String.valueOf(jsondata.get("mlcDesignation")));
					opddetails.setIdNumber(String.valueOf(jsondata.get("mlcIdNumber")));
					
				}
				opddetails.setRecmmdMedAdvice(String.valueOf(jsondata.get("doctorAdditionalNote")));
				opddetails.setEcgRemarks(String.valueOf(jsondata.get("ecgRemarks")));
				if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
					
					opddetails.setCampId(Long.parseLong(jsondata.get("campId").toString()));
				}
				opdId = Long.parseLong(session.save(opddetails).toString());
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if (visit != null) {
						visit.setVisitStatus("c");
						if(jsondata.get("userId")!=null && jsondata.get("userId")!="")
						{	
						visit.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
						}
						session.update(visit);
					}
				}
			
				
				////////////////////////////Patient Symptons ///////////////////////////////////////////////////////////////
				if (jsondata.get("symptomsValue") != null) {
					//List<HashMap<String, Object>> listSymptomsValue = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("symptomsValue");
					JSONArray listSymptomsValue = new JSONArray(jsondata.get("symptomsValue").toString());
					if (listSymptomsValue != null) {

						List<PatientSymptom> patientSymptons = new ArrayList<>();
						//for (HashMap<String, Object> listSymptomsOpd : listSymptomsValue) {
						for (int k = 0; k < listSymptomsValue.length(); k++) {
							JSONObject listSymptomsOpd = listSymptomsValue.getJSONObject(k);
							
							String inputString = String.valueOf(listSymptomsOpd.get("symptomsId"));
							//System.out.println("Special Char sympotonsId"+inputString);
					        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
					        for (int i=0; i < inputString.length() ; i++)
					        {
					            char ch = inputString.charAt(i);
					            if(specialCharactersString.contains(Character.toString(ch)))
					            {
					                //System.out.println(inputString+ " contains special character");
					                break;
					            }    
					       else if(i == inputString.length()-1)     
					       {    
					    	PatientSymptom ps = new PatientSymptom();
							ps.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
							ps.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
							ps.setLastChgDate(ourJavaTimestampObject);
							ps.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
							ps.setMmuId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
							ps.setOpdPatientDetailId(opdId);
							ps.setSymptomId(Long.parseLong(String.valueOf(listSymptomsOpd.get("symptomsId"))));
							if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
								
								ps.setCampId(Long.parseLong(jsondata.get("campId").toString()));
							}
							patientSymptons.add(ps);
							session.save(ps);
					       }
					     }      
						}
					}
				}
						
				///////////////////////// Discharge ICD Code Details Entry///////////////////////// /////////////////////////////
				if (jsondata.get("icdValue") != null) {
					//List<HashMap<String, Object>> listIcdValue = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("icdValue");
					JSONArray listIcdValue = new JSONArray(jsondata.get("icdValue").toString());
					if (listIcdValue != null) {

						List<DischargeIcdCode> dischargeDT = new ArrayList<>();
						//for (HashMap<String, Object> icdOpd : listIcdValue) {
						for (int i = 0; i < listIcdValue.length(); i++) {
							JSONObject icdOpd = listIcdValue.getJSONObject(i);
							DischargeIcdCode disIcdCode = new DischargeIcdCode();
							disIcdCode.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
							disIcdCode.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
							disIcdCode.setLastChgDate(ourJavaTimestampObject);
							disIcdCode.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
							if(icdOpd.get("auditDiagonsisValue").equals("F"))
							{
							 disIcdCode.setActionFlag("F");
							 aiDiagnosisFlag="Y";
							}
							else
							{
								disIcdCode.setActionFlag("T");
								
							}
							disIcdCode.setOpdPatientDetailsId(opdId);
							disIcdCode.setIcdId(Long.parseLong(String.valueOf(icdOpd.get("icdId"))));
							if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
								
								disIcdCode.setCampId(Long.parseLong(jsondata.get("campId").toString()));
							}
							dischargeDT.add(disIcdCode);
							session.save(disIcdCode);
							String markDisease=String.valueOf(icdOpd.get("markDiseaseVal"));
							String markInfectious =String.valueOf(icdOpd.get("markInfectiousVal"));
							Long icdId=Long.parseLong(String.valueOf(icdOpd.get("icdId")));
							if(icdId!=null )
							{	
								String qryStringHd  ="update MasIcd v set v.communicableFlag='"+markDisease+"',v.infectionsFlag='"+markInfectious+"'  where v.icdId='"+icdId+"' " ;
								Query queryHd = session.createQuery(qryStringHd);
						        int countHd = queryHd.executeUpdate();
								//System.out.println(countHd + " Record(s) Visit Reject Records Updated.");
							
							}
							
							
                        
						}
						 if(aiDiagnosisFlag.equals("Y"))
							{
							 opddetails.setDiagnosisFlag("Y");
							}
							else
							{
								opddetails.setDiagnosisFlag("N");	
							}
					}
				}
		
				///////////// Referal Details Section ////////////////////////////////
				if (jsondata.get("listofReferallHD") != null) {
					//List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofReferallHD");
					JSONArray list = new JSONArray(jsondata.get("listofReferallHD").toString());
					if (list != null) {
						//for (HashMap<String, Object> map : list) {
						for (int i = 0; i < list.length(); i++) {
							JSONObject map = list.getJSONObject(i);
							
							String extId = (String) map.get("extHospitalId");
							Long empID = Long.parseLong(extId);
							String referralNote = (String) map.get("referralNote");
							String doctorNote = (String) map.get("doctorNote");
							patientId = Long.parseLong((String) map.get("patientId"));
							hospitalId = Long.parseLong(String.valueOf(map.get("hospitalId")));
							// String treatmentType=(String)map.get("treatmentType");
							ReferralPatientHd header = null;
							if (empID != null && opdId != null) {
								header = getReferralPatientHdByExeHosAndOpdPd(empID, opdId);
							}
							Long id = null;
							if (header == null) {

								header = new ReferralPatientHd();
								header.setPatientId(patientId);
								header.setMmuId(hospitalId);
								header.setExtHospitalId(empID);
								header.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								header.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								header.setLastChgDate(ourJavaTimestampObject);
								header.setReferralIniDate(currentDate);
								header.setTreatmentType("E");
								header.setReferralIniDate(currentDate);
								header.setReferralNote(String.valueOf(referralNote));
								header.setDoctorNote(String.valueOf(doctorNote));
								header.setStatus("W");
								header.setOpdPatientDetailsId(opdId);
								if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
									
									header.setCampId(Long.parseLong(jsondata.get("campId").toString()));
								}
								id = Long.parseLong(session.save(header).toString());
							} else {
								id = header.getRefrealHdId();
							}
							Long extHosId = header.getExtHospitalId();
							//System.out.println(extHosId);
							//List<HashMap<String, Object>> referralList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofReferalDT");
							JSONArray referralList = new JSONArray(map.get("listofReferalDT").toString());
							//for (HashMap<String, Object> reffralMap : referralList) {
							for (int k = 0; k < referralList.length(); k++) {
								JSONObject reffralMap = referralList.getJSONObject(i);
								ReferralPatientDt refDt = new ReferralPatientDt();
								refDt.setLastChgDate(ourJavaTimestampObject);
								refDt.setExtDepartment(String.valueOf(reffralMap.get("extDepartment")));
								refDt.setRefrealHdId(id);
								session.save(refDt);

							}
						}

					}
				}
				////////////////////////////// Internal Refferal
				////////////////////////////// ////////////////////////////////////////////
				if (jsondata.get("listofInternalReferallHD") != null) {
					//List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofInternalReferallHD");
					JSONArray list = new JSONArray(jsondata.get("listofInternalReferallHD").toString());
					if (list != null) {
						for (int i = 0; i < list.length(); i++) {
							JSONObject map = list.getJSONObject(i);

							String intHosId = (String) map.get("intHospitalId");
							Long empID = Long.parseLong(intHosId);
							String referralNote = (String) map.get("referralNote");
							String doctorNote = (String) map.get("doctorNote");
							patientId = Long.parseLong((String) map.get("patientId"));
							hospitalId = Long.parseLong(String.valueOf(map.get("hospitalId")));
							// String treatmentType=(String)map.get("treatmentType");
							ReferralPatientHd intHeader = null;
							if (empID != null && opdId != null) {
								intHeader = getInternalReferralPatientHdByOpdPd(empID, opdId);
							}
							Long id = null;
							if (intHeader == null) {

								intHeader = new ReferralPatientHd();
								intHeader.setPatientId(patientId);
								intHeader.setMmuId(hospitalId);
								intHeader.setIntHospitalId(empID);
								intHeader.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								intHeader.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								intHeader.setLastChgDate(ourJavaTimestampObject);
								intHeader.setReferralIniDate(currentDate);
								intHeader.setTreatmentType("I");
								intHeader.setReferralIniDate(currentDate);
								intHeader.setReferralNote(String.valueOf(referralNote));
								intHeader.setDoctorNote(String.valueOf(doctorNote));
								intHeader.setStatus("W");
								if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
									
									intHeader.setCampId(Long.parseLong(jsondata.get("campId").toString()));
								}
								intHeader.setOpdPatientDetailsId(opdId);

								id = Long.parseLong(session.save(intHeader).toString());
							} else {
								id = intHeader.getRefrealHdId();
							}
							Long intHospitalId = intHeader.getIntHospitalId();
							//System.out.println(intHospitalId);
							//List<HashMap<String, Object>> diagnosisList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofIntReferalDT");
							JSONArray diagnosisList = new JSONArray(map.get("listofIntReferalDT").toString());
							
							//for (HashMap<String, Object> diagnosisMap : diagnosisList) {
							for (int k = 0; k < diagnosisList.length(); k++) {
								JSONObject diagnosisMap = diagnosisList.getJSONObject(i);
								long diagnosisId = Long.parseLong((String) diagnosisMap.get("diagnosisId"));
								ReferralPatientDt refDt = new ReferralPatientDt();
								refDt.setDiagnosisId(diagnosisId);
								refDt.setLastChgDate(ourJavaTimestampObject);
								refDt.setReferralDate(currentDate);
								refDt.setIntDepartmentId(
										Long.valueOf(String.valueOf(diagnosisMap.get("intDepartment"))));
								refDt.setInstruction(String.valueOf(diagnosisMap.get("instruction")));
								refDt.setRefrealHdId(id);
								session.save(refDt);

							}
						}

					}
				}
				

				///////////// Nursing Care Details Section ////////////////////////////////
				if (jsondata.get("listofNursingCareHD") != null) {
					//List<HashMap<String, Object>> listNursing = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofNursingCareHD");
					JSONArray listNursing = new JSONArray(jsondata.get("listofNursingCareHD").toString());
					if (listNursing != null) {
						//for (HashMap<String, Object> map : listNursing) {
						for (int i = 0; i < listNursing.length(); i++) {
							JSONObject map = listNursing.getJSONObject(i);
							visitId = Long.parseLong((String) map.get("visitId"));
							patientId = Long.parseLong((String) map.get("patientId"));
							hospitalId = Long.parseLong(String.valueOf(map.get("hospitalId")));
							userId = Long.parseLong(String.valueOf(map.get("userId")));
							// String treatmentType=(String)map.get("treatmentType");
							procedureType = (String) map.get("procedureType");
							ProcedureHd pHd = new ProcedureHd();
							pHd.setVisitId(visitId);
							pHd.setPatientId(patientId);
							pHd.setMmuId(hospitalId);
							pHd.setStatus("N");
							pHd.setRequisitionDate(ourJavaTimestampObject);
							pHd.setLastChgDate(ourJavaTimestampObject);
							pHd.setProcedureType(procedureType);
							pHd.setLastChgBy(userId);
							pHd.setDoctorId(userId);
							pHd.setOpdPatientDetailsId(opdId);
							if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
								
								pHd.setCampId(Long.parseLong(jsondata.get("campId").toString()));
							}
							Long headerId = Long.parseLong(session.save(pHd).toString());
							//List<HashMap<String, Object>> nursingList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofNursingDT");
							JSONArray nursingList = new JSONArray(map.get("listofNursingDT").toString());
							//for (HashMap<String, Object> nursingMap : nursingList) {
							for (int k = 0; k < nursingList.length(); k++) {
								JSONObject nursingMap = nursingList.getJSONObject(k);
								ProcedureDt refNursingDt = new ProcedureDt();
								if (nursingMap.get("nursingId") != null && nursingMap.get("nursingId") != "") {
									Long nursingId = Long.parseLong((String) nursingMap.get("nursingId"));
									refNursingDt.setProcedureId(nursingId);
								}
								refNursingDt.setStatus("N");
																
								refNursingDt.setRemarks(String.valueOf(nursingMap.get("remarks")));
								refNursingDt.setProcedureDate(ourJavaTimestampObject);
								refNursingDt.setLastChgDate(ourJavaTimestampObject);
								refNursingDt.setAppointmentDate(ourJavaTimestampObject);
								refNursingDt.setProcedureHdId(headerId);
								session.save(refNursingDt);

							}
						}

					}
				}
				//////////////////////////////////////// Investigation Section
				//////////////////////////////////////// ////////////////////////////////////////
				if (jsondata.get("listofInvestigation") != null) {
					//List<HashMap<String, Object>> listInvestigation = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofInvestigation");
					JSONArray listInvestigation = new JSONArray(jsondata.get("listofInvestigation").toString());
					if (listInvestigation != null) {
						//for (HashMap<String, Object> map : listInvestigation) {
						for (int i = 0; i < listInvestigation.length(); i++) {
							JSONObject map = listInvestigation.getJSONObject(i);
							// DgOrderhd orderhd = new DgOrderhd();
							DgOrderhd orderhd = null;
							Long headerInveId = null;
							
							Date orderDate = HMSUtil.getTodayFormattedDate();
							//System.out.println("today"+orderDate);//Today's date
							Long visitId11 = (Long.parseLong(String.valueOf(jsondata.get("visitId"))));
							if (orderDate != null && visitId11 != null) {
								orderhd = getOrderDatebyInvestigation(orderDate, visitId11);
							}
						
							if (orderhd == null) {
								orderhd = new DgOrderhd();
								orderhd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
								if(jsondata.get("departmentId")!=null && !jsondata.get("departmentId").equals(""))
								{
								orderhd.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
								}
								orderhd.setOrderDate(orderDate);
								orderhd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
								orderhd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
								// orderhd.setOtherInvestigation(String.valueOf(jsondata.get("otherInvestigation")));
								orderhd.setOrderStatus("P");
								orderhd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
								orderhd.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
									
									orderhd.setCampId(Long.parseLong(jsondata.get("campId").toString()));
								}
								orderhd.setLastChgDate(ourJavaTimestampObject);
								headerInveId = Long.parseLong(session.save(orderhd).toString());
							} else {
								headerInveId = orderhd.getOrderhdId();
							}
							//List<HashMap<String, Object>> invList = (List<HashMap<String, Object>>) (Object) map
							//		.get("listofInvestigationDT");
							JSONArray invList = new JSONArray(map.get("listofInvestigationDT").toString());
							//for (HashMap<String, Object> invMap : invList) {
							for (int K = 0; K < invList.length(); K++) {
								JSONObject invMap = invList.getJSONObject(K);
								DgOrderdt ob1 = new DgOrderdt();
								if (invMap.get("investigationId") != null && invMap.get("investigationId") != "") {
									ob1.setInvestigationId(Long.valueOf(invMap.get("investigationId").toString()));
								}
								if (invMap.get("auditInvestigationValue") != null && invMap.get("auditInvestigationValue").equals("F")) {
									ob1.setActionFlag("F");
									aiInvestgationFlag="Y";
								}
								else
								{
									ob1.setActionFlag("T");
									
								}
								ob1.setLastChgDate(ourJavaTimestampObject);
								ob1.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
								ob1.setOrderStatus("P");
								ob1.setOrderhdId(headerInveId);
								session.save(ob1);

							}

							if(aiInvestgationFlag.equals("Y"))
							{
								opddetails.setInvestigationFlag("Y");
							}
							else
							{
								opddetails.setInvestigationFlag("N");	
							}

						
						}
						
					}
					
				}

///////////////////////////////////// NIP and Presecription sections///////////////////////////////////// //////////////////////////////////////

// Presec-Section
				if (jsondata.get("listofPrescription") != null) {
					//List<HashMap<String, Object>> listPrescription = (List<HashMap<String, Object>>) (Object) jsondata
					//		.get("listofPrescription");
					JSONArray listPrescription = new JSONArray(jsondata.get("listofPrescription").toString());
					//List<HashMap<String, Object>> nipCheck = (List<HashMap<String, Object>>) jsondata.get("listofNip");
					Long headerPrescriptionId = null;
					if (listPrescription != null && !listPrescription.isNull(0)) {
						PatientPrescriptionHd pphd = new PatientPrescriptionHd();
						pphd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
						pphd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
						pphd.setStatus(String.valueOf(jsondata.get("prescriptionStatus")));
						if (jsondata.get("campId") != null && !jsondata.get("campId").equals("")) {
							
							pphd.setCampId(Long.parseLong(jsondata.get("campId").toString()));
						}
						pphd.setPrescriptionDate(ourJavaTimestampObject);
						pphd.setLastChgDate(ourJavaTimestampObject);
						//pphd.setInjectionStatus(String.valueOf(jsondata.get("injectionStatus")));
						/*if (nipCheck != null && !nipCheck.equals("")) {
							pphd.setNivStatus("Y");

						} else {
							pphd.setNivStatus(String.valueOf(jsondata.get("nipStatus")));
						}*/
						pphd.setDoctorId(Long.parseLong(jsondata.get("userId").toString()));
						pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
						pphd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
						pphd.setOpdPatientDetailsId(opdId);
						headerPrescriptionId = Long.parseLong(session.save(pphd).toString());
						// pphd.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));
						List<PatientPrescriptionDt> patientPrescDT = new ArrayList<>();
						//for (HashMap<String, Object> singleopd : listPrescription) {
						for (int i = 0; i < listPrescription.length(); i++) {
							JSONObject singleopd = listPrescription.getJSONObject(i);
							PatientPrescriptionDt ppdt1 = new PatientPrescriptionDt();
							if (singleopd.get("itemId") != null && singleopd.get("itemId") != "") {
								ppdt1.setItemId(Long.valueOf(singleopd.get("itemId").toString()));
							}
							if (singleopd.get("frequencyId") != null && singleopd.get("frequencyId") != "") {
								ppdt1.setFrequencyId(Long.valueOf(singleopd.get("frequencyId").toString()));
							}
							if (singleopd.get("dosage") != null && singleopd.get("dosage").toString() != "") {
								ppdt1.setDosage(String.valueOf(singleopd.get("dosage")));
							}
							if (singleopd.get("noOfDays") != null && singleopd.get("noOfDays") != "") {
								ppdt1.setNoOfDays(Long.valueOf(singleopd.get("noOfDays").toString()));
							}
							if (singleopd.get("total") != null && singleopd.get("total") != "") {
								ppdt1.setTotal(Long.valueOf(singleopd.get("total").toString().trim()));
							}
							if (singleopd.get("auditTreatmentFlag") != null && singleopd.get("auditTreatmentFlag").equals("F")) {
								ppdt1.setActionFlag("F");
								aTreatmentFlag="Y";
							
							}
							else
							{
								ppdt1.setActionFlag("T");
								
							}
							ppdt1.setInstruction(String.valueOf(singleopd.get("instruction")));
							//ppdt1.setInjectionStatus("N");
							ppdt1.setStatus("P");
							ppdt1.setLastChgDate(ourJavaTimestampObject);
							// patientPrescDT.add(ppdt1);
							ppdt1.setPrescriptionHdId(headerPrescriptionId);
							;
							session.save(ppdt1);
							
							//MasStoreItem msitDispUpdate = new MasStoreItem();
							Long itemId = Long.parseLong((String) singleopd.get("itemId"));
							Criteria dispUnitId = session.createCriteria(MasStoreItem.class);
							dispUnitId.add(Restrictions.eq("itemId", itemId));
							MasStoreItem msitDispUpdate = (MasStoreItem) dispUnitId.uniqueResult();
							msitDispUpdate.setDispUnitId(Long.parseLong(String.valueOf(singleopd.get("dispunitId"))));
							session.update(msitDispUpdate);
							
							
						}
						if(aTreatmentFlag.equals("Y"))
						{
							opddetails.setPrescriptionFlag("Y");
						}
						else
						{
							opddetails.setPrescriptionFlag("N");
						}
						// opdPrescription=od.saveOpdPrescription(pphd, patientPrescDT);

					}

				}
				
				if (visitId != null) {
					Visit visit = (Visit) session.get(Visit.class, visitId);
					if(aiDiagnosisFlag.equals("Y") ||aiInvestgationFlag.equals("Y")||aTreatmentFlag.equals("Y")) {
						visit.setAiAuditException("Y");						
					}
					else
					{
						visit.setAiAuditException("N");	
					}
					session.update(visit);
				}
				

			}
			tx.commit();

		} catch (Exception ex) {

			// //System.out.println("Exception e="+ex.);
			ex.printStackTrace();
			tx.rollback();
			//System.out.println("Exception Message Print ::" + ex.toString());
			return ex.toString();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return "Successfully saved";
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getObesityWaitingList(HashMap<String, Object> jsondata) {
		
		Map<String, Object> map = new HashMap<String, Object>();/*
        
		long hospitalId = (Integer) jsondata.get("hospitalId");
		Session session = getHibernateUtils.getHibernateUtlis().getCurrentSession();
		@SuppressWarnings("unchecked")
		String serviceNo = String.valueOf(jsondata.get("service_no"));
		String SpageNo = String.valueOf(jsondata.get("pageNo"));
		String obesityFlag = String.valueOf(jsondata.get("obesityFlag"));
		int pageNo = Integer.parseInt(SpageNo);
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int count = 0;
		String patientName = String.valueOf(jsondata.get("patient_name")).trim();
		System.out.println("service no " + serviceNo);
		List<OpdObesityHd> patientObesityList = new ArrayList<OpdObesityHd>();
		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = null;

		criteria = session.createCriteria(OpdObesityHd.class, "csr")
				.add(Restrictions.eq("masHospital.hospitalId", hospitalId)).add(Restrictions.isNull("closeDate"));

		criteria = criteria.createAlias("csr.patient", "pt");
		if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {

			criteria = criteria.add(Restrictions.eq("pt.serviceNo", serviceNo).ignoreCase());

		}

		if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {
			String pName = "%" + patientName + "%";
			criteria = criteria.add(Restrictions.like("pt.patientName", pName).ignoreCase());

		}
		
		if (obesityFlag != null && !obesityFlag.equals("") && !obesityFlag.equals("null")) {
			if(obesityFlag.equals("1")) {
				criteria = criteria.add(Restrictions.like("overweightFlag", "N").ignoreCase());
			}else if(obesityFlag.equals("2")) {
				criteria = criteria.add(Restrictions.like("overweightFlag", "Y").ignoreCase());
			}
		}
		criteria = criteria.addOrder(Order.desc("iniDate"));
		
		patientObesityList = criteria.list();
		count = patientObesityList.size();

		criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
		criteria = criteria.setMaxResults(pagingSize);
		patientObesityList = criteria.list();

		if (patientObesityList != null) {
			map.put("patientList", patientObesityList);
			map.put("count", count);
		}*/
		return map;
	}

	/*
	 * @Override public Map<String,Object> getObesityDetails(HashMap<String, Object>
	 * jsondata){ long headerId = (Integer)jsondata.get("id"); Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Map<String,Object>
	 * jsonData = new HashMap<>();
	 * 
	 * @SuppressWarnings("unchecked") List<OpdObesityHd> obesityList =
	 * session.createCriteria(OpdObesityHd.class)
	 * .add(Restrictions.eq("obesityHdId",headerId)) .list();
	 * 
	 * @SuppressWarnings("unchecked") List<OpdObesityDt> obesityDetailList =
	 * session.createCriteria(OpdObesityDt.class)
	 * .add(Restrictions.eq("opdObesityHd.obesityHdId",headerId)) .list();
	 * 
	 * jsonData.put("obesityList", obesityList); jsonData.put("obesityDetailList",
	 * obesityDetailList); return jsonData; }
	 */

	@Override
	public Map<String, Object> getObesityDetails(HashMap<String, Object> jsondata) {
		
		Map<String, Object> jsonData = new HashMap<>();
		/*long headerId = (Integer) jsondata.get("id");
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		@SuppressWarnings("unchecked")
		List<OpdObesityHd> obesityList = session.createCriteria(OpdObesityHd.class)
				.add(Restrictions.eq("obesityHdId", headerId)).list();
		@SuppressWarnings("unchecked")
		List<OpdObesityDt> obesityDetailList = session.createCriteria(OpdObesityDt.class)
				.add(Restrictions.eq("opdObesityHd.obesityHdId", headerId))
				.addOrder(Order.asc("obesityDtId")).list();
		
		jsonData.put("obesityList", obesityList);
		jsonData.put("obesityDetailList", obesityDetailList);*/
		return jsonData;
	}

	@SuppressWarnings("unused")
	@Override
	public List<String> getIdealWeight(Long height, String age, Long genderId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasIdealWeight> list = null;
		List<String> searchList = null;
		try {
			String Query = "select MIW.WEIGHT from MAS_IDEAL_WEIGHT MIW INNER  join MAS_RANGE A on  A.RANGE_id =  MIW.AGE_RANGE_ID and '"
					+ age + "' between A.FROM_RANGE and A.TO_range and A.range_Flag='A' AND A.GENDER_ID='" + genderId
					+ "' INNER  join MAS_RANGE H on  H.RANGE_id =  MIW.HEIGHT_RANGE_ID and '" + height
					+ "' between H.FROM_RANGE and H.TO_range and H.range_Flag='H' AND A.GENDER_ID='" + genderId
					+ "' WHERE  MIW.GENDER_ID='" + genderId + "'";

			/*
			 * String Query = "select weight as idealWeight from mas_ideal_weight where '" +
			 * age + "' between FROM_AGE and TO_AGE and '" + height +
			 * "' between FROM_HEIGHT and TO_HEIGHT and GENDER_ID = '"+genderId+"'";
			 */
			//System.out.println(Query);
			if (Query != null) {
				searchList = session.createSQLQuery(Query).list();
			} else {
				//System.out.println("No Record Found");
			}
			return searchList;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return searchList;
	}

	@Override
	public String saveObesityDetails(HashMap<String, Object> jsondata) {
		/*Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			OpdObesityHd opdHeader = new OpdObesityHd();
			Map<String, Object> jsonData = new HashMap<>();
			Transaction tx = session.beginTransaction();
			// get input value
			long headerId = (Integer.valueOf((int) jsondata.get("header_id")));
			// long headerId = Long.parseLong(hId) ;

			String date = (String) jsondata.get("obesity_date");
			Date obesityDate = HMSUtil.convertStringTypeDateToDateType(date);

			String month = (String.valueOf(jsondata.get("month_name")));

			String h = (String.valueOf(jsondata.get("height")));
			BigDecimal height = new BigDecimal(h);

			int w = (Integer.valueOf((int) jsondata.get("weight")));
			BigDecimal weight = new BigDecimal(w);

			String iw = (String.valueOf(jsondata.get("ideal_weight")));
			BigDecimal idealWeight = new BigDecimal(iw);

			String v = (String.valueOf(jsondata.get("variation")));
			BigDecimal variation = new BigDecimal(v);
			// BigDecimal variation = new
			// BigDecimal(String.valueOf(jsondata.get("variation")));
			
			 * long b = (Integer) jsondata.get("bmi"); BigDecimal bmi =
			 * BigDecimal.valueOf(b).movePointLeft(0);
			 

			// BigDecimal bmi = new BigDecimal(String.valueOf(jsondata.get("bmi")));

			String obesityCheck = String.valueOf(jsondata.get("obesity_check"));

			opdHeader = (OpdObesityHd) session.get(OpdObesityHd.class, headerId);
			if (opdHeader != null) {

				opdHeader.setVaration(variation);
				session.update(opdHeader);
			}

			if (obesityCheck.equals("y")) {
				opdHeader = (OpdObesityHd) session.get(OpdObesityHd.class, headerId);
				if (opdHeader != null) {

					opdHeader.setCloseDate(obesityDate);
					session.update(opdHeader);
				}
			}

			
			 * OpdObesityHd opdHd = new OpdObesityHd(); opdHd.setObesityHdId(headerId);
			 
			OpdObesityDt dt = new OpdObesityDt();
			// set input value to entity and save
			// dt.setOpdObesityHd(opdHd);
			dt.setObesityHdId(headerId);
			dt.setObesityDate(obesityDate);
			dt.setMonth(month);
			dt.setHeight(height);
			dt.setWeight(weight);
			dt.setIdealWeight(idealWeight);
			dt.setVariation(variation);
			if (jsondata.get("bmi") != null && !jsondata.get("bmi").equals("")) {
				BigDecimal bmi = new BigDecimal(String.valueOf(jsondata.get("bmi")));
				dt.setBmi(bmi);
			}

			session.save(dt);
			tx.commit();
			return "Data saved Successfully";
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}*/
		return "Error occured while saving Patien data";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> referredPatientList(HashMap<String, String> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			long hospitalId = Long.parseLong(String.valueOf(jsondata.get("hospital_id")));
			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			String appointmentCodeForOpd = HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_OPD");
			String appointmentCodeForME = HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_ME");
			int count = 0;
			String mobileNo = String.valueOf(jsondata.get("mobile_no"));
			String patientName = String.valueOf(jsondata.get("patient_name"));
			String serviceNo = String.valueOf(jsondata.get("serviceNo"));
			List<ReferralPatientHd> list = new ArrayList<>();

			Criteria criteria = null;

			criteria = session.createCriteria(ReferralPatientHd.class, "rph").add(Restrictions.isNull("intHospitalId"))
					.add(Restrictions.eq("status", "W")).createAlias("rph.masHospital2", "mih")
					.add(Restrictions.eq("mih.hospitalId", hospitalId))
					.createAlias("opdPatientDetail", "opd")
					.createAlias("opd.visit", "visit")
					.createAlias("visit.masAppointmentType", "mat")
					.add(Restrictions.or(Restrictions.eq("mat.appointmentTypeCode", appointmentCodeForOpd).ignoreCase(), Restrictions.eq("mat.appointmentTypeCode", appointmentCodeForME)))
					.addOrder(Order.desc("rph.referralIniDate"));

			criteria = criteria.createAlias("rph.patient", "patient");
			if (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {

				criteria = criteria.add(Restrictions.eq("patient.mobileNumber", mobileNo.trim()));

			} else if (patientName != null && !patientName.equals("") && !patientName.equals("null")) {

				String pName = "%" + patientName + "%";
				criteria = criteria.add(Restrictions.like("patient.patientName", pName).ignoreCase());

			} else if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {

				criteria = criteria.add(Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase());

			}

			list = criteria.list();
			count = list.size();

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();

			if (list != null && list.size() > 0) {
				map.put("referredPatientList", list);
				map.put("count", count);
			}
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> referredPatientDetail(HashMap<String, String> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			long id = Long.parseLong(String.valueOf(jsondata.get("id")));
			List<ReferralPatientHd> headerList = session.createCriteria(ReferralPatientHd.class, "rph")
					.add(Restrictions.eq("id", id)).list();

			/*
			 * List<ReferralPatientDt> detailList = (List<ReferralPatientDt>)
			 * session.createCriteria(ReferralPatientDt.class)
			 * .add(Restrictions.eq("referralPatientHd.id", id))
			 * .add(Restrictions.isNull("masDepartment")) .add(Restrictions.eq("mb", "N"))
			 * .add(Restrictions.eq("admitted", "N")) .add(Restrictions.eq("close",
			 * "N")).list();
			 */

			List<ReferralPatientDt> detailList = (List<ReferralPatientDt>) session
					.createCriteria(ReferralPatientDt.class).add(Restrictions.eq("referralPatientHd.id", id))
					.add(Restrictions.isNull("masDepartment")).list();
			/*
			 * .add(Restrictions.isNull("mb")) .add(Restrictions.isNull("admitted"))
			 * .add(Restrictions.isNull("disease"))
			 * .add(Restrictions.isNull("close")).list();
			 */

			if (detailList.size() > 0) {
				boolean flag = true;
				for (ReferralPatientDt rpdt : detailList) {
					String disease = rpdt.getDisease();
					String mb = rpdt.getMb();
					String admission = rpdt.getAdmitted();
					String close = rpdt.getClose();
					if ((disease == null) && (mb == null) && (admission == null) && (close == null)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					return map;
				}
			}

			if (headerList != null) {
				map.put("referredHeaderList", headerList);
			}

			if (detailList != null) {
				map.put("referredDetailList", detailList);
			}

			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> updateReferralDetail(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		boolean flag = true;
		try {
			String message = "";
			boolean admissionFlag = false;
			long headerId = Long.parseLong((String) jsondata.get("header_id"));
			long patientId = Long.parseLong((String) jsondata.get("patient_id"));
			long hospitalId = Long.parseLong(jsondata.get("hospital_id") + "");
			List<HashMap<String, String>> detailList = (List<HashMap<String, String>>) (jsondata.get("detail_list"));
			int listSize = detailList.size();
			ReferralPatientHd referralPatientHd = null;
			List<HashMap<String, String>> list = (List<HashMap<String, String>>) jsondata.get("detail_list");
			Transaction tx;
			tx = session.beginTransaction();
			try {
				// for (int i = 0; i < jsondata.size(); i++) {
				for (HashMap<String, String> data : detailList) {

					long id = Long.parseLong(String.valueOf((String) data.get("id")));
					String finalNote = String.valueOf(((String) data.get("final_note")));
					String notifiable_disease = String.valueOf(((String) data.get("notifiable_disease")));
					if (notifiable_disease.trim().equals("N")) {
						notifiable_disease = null;
					}
					String markMb = String.valueOf(((String) data.get("mark_mb")));
					if (markMb.trim().equals("N")) {
						markMb = null;
					}else {
						Date visitDate =new Date();

						Visit visit = new Visit();

						visit.setVisitDate(new Timestamp(visitDate.getTime()));
						visit.setLastChgDate(new Timestamp(visitDate.getTime()));

						visit.setPriority(new Long(1));
						visit.setVisitStatus("m");
						visit.setVisitFlag("L");
						//visit.setExamStatus("T");
						//visit.setExamId(new Long(5));
						
						String appoinmentTypeCode="MB";
						Long masappointTypeId =  getAppointmentTypeId(appoinmentTypeCode,session);
						//visit.setAppointmentTypeId(masappointTypeId);

						visit.setMmuId(hospitalId);
						visit.setPatientId(patientId);
						session.save(visit);
					}
					String markAdmitted = String.valueOf(((String) data.get("mark_admitted")));
					if (markAdmitted.trim().equalsIgnoreCase("N")) {
						markAdmitted = null;
					}else if(markAdmitted.trim().equalsIgnoreCase("Y")) {
						admissionFlag = true;
					}
					String close = String.valueOf(((String) data.get("close")));
					if (close.trim().equals("N")) {
						close = null;
					}
					ReferralPatientDt referralPatientDt = (ReferralPatientDt) session.get(ReferralPatientDt.class, id);
					if (referralPatientDt != null) {

						referralPatientDt.setFinalNote(finalNote);
						referralPatientDt.setDisease(notifiable_disease);
						referralPatientDt.setMb(markMb);
						referralPatientDt.setClose(close);
						referralPatientDt.setAdmitted(markAdmitted);
						session.update(referralPatientDt);

					} else {
						message = "Patient detail could not be update";
						map.put("msg", message);
						return map;
					}

					// }

				}
				tx.commit();

				List<ReferralPatientDt> dtList = (List<ReferralPatientDt>) session
						.createCriteria(ReferralPatientDt.class).add(Restrictions.eq("referralPatientHd.id", headerId))
						.add(Restrictions.isNull("masDepartment")).add(Restrictions.isNull("mb"))
						.add(Restrictions.isNull("admitted")).add(Restrictions.isNull("disease"))
						.add(Restrictions.isNull("close")).list();

				if (dtList.isEmpty()) {
					referralPatientHd = (ReferralPatientHd) session.get(ReferralPatientHd.class, headerId);
					if (referralPatientHd != null) {
						tx = session.beginTransaction();
						referralPatientHd.setStatus("D");
						session.update(referralPatientHd);
						tx.commit();
					}
				}
				

			} catch (Exception ex) {
				flag = false;
				tx.rollback();
				ex.printStackTrace();
				message = "Records updated Failed";
				map.put("msg", message);
			} finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				if (flag) {
					message = "Records updated";
					map.put("msg", message);
				} else {
					message = "Patient detail could not be update";
					map.put("msg", message);
				}

			}
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAdmissionDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPendingDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;

	}

	
	
	@Override
	public String savePatientAdmission(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
				return "Save";
}


	@Override
	public String saveNewAdmission(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		
		return "Error while saving admission";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> nursingCareWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			long hospital_id = Long.parseLong(jsondata.get("hospital_id"));
			Date fromDate = null;
			if (jsondata.get("from_date") != null && !jsondata.get("from_date").equals("")) {
				fromDate = HMSUtil.convertStringTypeDateToDateType(jsondata.get("from_date"));
			}
			Date toDate = null;
			if (jsondata.get("to_date") != null && !jsondata.get("to_date").equals("")) {
				toDate = HMSUtil.convertStringTypeDateToDateType(jsondata.get("to_date"));
			}

			String SpageNo = String.valueOf(jsondata.get("pageNo"));
			int pageNo = Integer.parseInt(SpageNo);
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
			List<ProcedureHd> ncWaitingList = new ArrayList<>();
			Criteria criteria = session.createCriteria(ProcedureHd.class, "phd").createAlias("phd.masHospital", "mh")
					.add(Restrictions.eq("mh.hospitalId", hospital_id)).add(Restrictions.eq("status", "N"))
					.add(Restrictions.eq("procedureType", "N")).createAlias("phd.opdPatientDetails", "opd")
					.addOrder(Order.desc("opd.opdDate"));

			if (fromDate != null && !fromDate.equals("")) {
				criteria = criteria.add(Restrictions.ge("opd.opdDate", fromDate));
			}
			if (toDate != null && !toDate.equals("")) {
				toDate.setHours(23);
				toDate.setMinutes(59);
				toDate.setSeconds(59);
				criteria = criteria.add(Restrictions.le("opd.opdDate", toDate));
			}

			// map.put("nursingCareList", ncWaitingList);
			ncWaitingList = criteria.list();
			count = ncWaitingList.size();
			map.put("count", count);

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			ncWaitingList = criteria.list();
			map.put("nursingCareList", ncWaitingList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNursingCareDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		long headerId = Long.parseLong(jsondata.get("header_id"));
		List<ProcedureDt> list = session.createCriteria(ProcedureDt.class)
				.add(Restrictions.eq("procedureHd.procedureHdId", headerId)).list();
		// .setResultTransformer(Transformers.aliasToBean(ProcedureDt.class));
		// .setResultTransformer(Transformers.aliasToBean(ProcedureDt.class));

		map.put("detailList", list);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getProcedureDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		// Transaction tx=null;
		// tx=session.beginTransaction();
		try {

			String[] ids = jsondata.get("header_id").split("\\.");
			List<ProcedureDt> list = null;
			long header_id = Long.parseLong(ids[0]);
			long procedure_id = Long.parseLong(ids[1]);
			list = session.createCriteria(ProcedureDt.class).add(Restrictions.eq("procedureId", procedure_id))
					.add(Restrictions.eq("procedureHdId", header_id)).list();
			map.put("detailList", list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.clear();
			session.flush();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings({ "finally", "unchecked" })
	@Override
	public String saveProcedureDetail(Map<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "";
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		String result = "";
		try {
			/*
			 * Map<String, Object> complete = (Map<String, Object>)jsondata.get(0);
			 * Map<String, Object> pending = (Map<String, Object>)jsondata.get(1);
			 */

			// info for updates
			long id = Long.parseLong((String) jsondata.get("id"));
			Date procedureDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("procedure_date")));

			String nursingRemarks = (String) jsondata.get("nursing_remarks");

			// info for saving
			Date appointmentDate = null;
			if (jsondata.get("appointment_date") != null && !jsondata.get("appointment_date").equals("")) {
				appointmentDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("appointment_date")));
			}

			long headerId = Long.parseLong((String) jsondata.get("header_id"));
			long procedureId = Long.parseLong((String) jsondata.get("procedure_id"));
			long frequencyId = Long.parseLong((String) jsondata.get("frequency_id"));
			long noOfDays = Long.parseLong((String) jsondata.get("no_of_days"));
			String final_status = (String) jsondata.get("final_status");
			String opRemarks = (String) jsondata.get("op_remarks");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ProcedureDt pDt = (ProcedureDt) session.get(ProcedureDt.class, id);
			if (pDt != null) {
				Timestamp ts = new Timestamp(procedureDate.getTime());
				pDt.setProcedureDate(ts);
				pDt.setNursingRemark(nursingRemarks);
				pDt.setStatus("Y");
				pDt.setLastChgDate(ourJavaTimestampObject);
				session.update(pDt);
				tx.commit();
				tx = null;
				msg = "Records Saved Successfully";
			}

			if (final_status.equals("Y")) {
				tx = session.beginTransaction();
				session.createSQLQuery("update procedure_dt set FINAL_PROCEDURE_STATUS = 'Y' where PROCEDURE_HD_ID = "
						+ headerId + " and PROCEDURE_ID = " + procedureId + "").executeUpdate();
				tx.commit();
				tx = null;

			}

			if (final_status.equals("Y")) {
				List<ProcedureDt> procedureDtList = session.createCriteria(ProcedureDt.class, "pDt")
						.add(Restrictions.eq("procedureHdId", headerId))
						.add(Restrictions.or(Restrictions.eq("status", "N"), Restrictions.isNull("status"))).list();

				if (procedureDtList.isEmpty()) {
					tx = session.beginTransaction();
					ProcedureHd procedureHd = (ProcedureHd) session.get(ProcedureHd.class, headerId);
					if (procedureHd != null) {
						procedureHd.setStatus("Y");
						procedureHd.setLastChgDate(ourJavaTimestampObject);
						session.update(procedureHd);
						tx.commit();
						tx = null;
						msg = "Records Saved Successfully";

					}
				}
				return msg;
			}

			tx = session.beginTransaction();
			ProcedureDt dt = new ProcedureDt();
			if (appointmentDate != null && !appointmentDate.equals("")) {
				Timestamp ts = new Timestamp(appointmentDate.getTime());
				dt.setAppointmentDate(ts);
			}
			ProcedureHd hd = new ProcedureHd();
			hd.setProcedureHdId(headerId);
			// dt.setProcedureHd(hd);
			dt.setProcedureHdId(headerId);
			MasNursingCare ms = new MasNursingCare();
			ms.setNursingId(procedureId);
			// dt.setMasNursingCare(ms);
			dt.setProcedureId(procedureId);
			MasFrequency msf = new MasFrequency();
			msf.setFrequencyId(frequencyId);
//				dt.setMasFrequency(msf);
			dt.setFrequencyId(frequencyId);
			dt.setNoOfDays(noOfDays);
			dt.setRemarks(opRemarks);
			dt.setLastChgDate(ourJavaTimestampObject);
			dt.setProcedureDate(ourJavaTimestampObject);
			session.save(dt);
			tx.commit();
			tx = null;
			msg = "Records Saved Successfully";
			return msg;
		} catch (Exception ex) {
			msg = "Error While Saving Records";
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return msg;
		}

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public Map<String, Object> physioTherapyWaitingList(Map<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		long hospital_id = Long.parseLong(jsondata.get("hospital_id"));
		String serviceNo = jsondata.get("service_no");
		String SpageNo = String.valueOf(jsondata.get("pageNo"));
		Date fromDate = null;
		if (jsondata.get("from_date") != null && !jsondata.get("from_date").equals("")) {
			fromDate = HMSUtil.convertStringTypeDateToDateType(jsondata.get("from_date"));
		}
		Date toDate = null;
		if (jsondata.get("to_date") != null && !jsondata.get("to_date").equals("")) {
			toDate = HMSUtil.convertStringTypeDateToDateType(jsondata.get("to_date"));
		}
		int pageNo = Integer.parseInt(SpageNo);
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int count = 0;
		List<ProcedureHd> ptWaitingList = new ArrayList<>();
		Criteria criteria = session.createCriteria(ProcedureHd.class, "phd").createAlias("phd.masHospital", "mh")
				.add(Restrictions.eq("mh.hospitalId", hospital_id)).add(Restrictions.eq("status", "N"))
				.add(Restrictions.eq("procedureType", "P")).createAlias("phd.opdPatientDetails", "opd")
				.addOrder(Order.desc("opd.opdDate"));

		// map.put("physioTherapyWaitingList", ptWaitingList);

		if (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals(null)) {
			criteria = criteria.createAlias("phd.patient", "pt").add(Restrictions.eq("pt.serviceNo", serviceNo).ignoreCase());
		}
		
		if (fromDate != null && !fromDate.equals("")) {	
			criteria = criteria.add(Restrictions.ge("opd.opdDate", fromDate));
			
		}
		if (toDate != null && !toDate.equals("")) {		
			toDate.setHours(23);
			toDate.setMinutes(59);
			toDate.setSeconds(59);
			criteria = criteria.add(Restrictions.le("opd.opdDate", toDate));
		}
		ptWaitingList = criteria.list();
		count = ptWaitingList.size();

		criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
		criteria = criteria.setMaxResults(pagingSize);
		ptWaitingList = criteria.list();
		map.put("physioTherapyWaitingList", ptWaitingList);
		map.put("count", count);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getphysioTherapyDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		long headerId = Long.parseLong(jsondata.get("header_id"));
		List<ProcedureDt> list = session.createCriteria(ProcedureDt.class, "pt").createAlias("pt.procedureHd", "pHd")
				.createAlias("pt.masNursingCare", "mnc").add(Restrictions.eq("pHd.procedureHdId", headerId)).list();

		map.put("detailList", list);
		return map;
	}

	@Override
	public Map<String,Object> getOpdReportsDetailsbyServiceNo(HashMap<String,Object> jsonData) {
		
		List<Patient> list =null;
		String serviceNo = (String) jsonData.get("serviceNo");
		Map<String,Object> map = new HashMap<String, Object>();
		try{
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
	/*	List<MasEmployee> employeeList = session.createCriteria(MasEmployee.class)
				.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase()).list();
		
		if(!CollectionUtils.sizeIsEmpty(employeeList)) {
			Criteria cr = session.createCriteria(Patient.class)
							.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase());
					list = cr.list();
			map.put("list", list);
			map.put("errorInServiceNo", "");
			}else {
			 map.put("errorInServiceNo", "errorInServiceNo");
			}*/
		
		Criteria cr = session.createCriteria(Patient.class)
				.add(Restrictions.disjunction()
						.add(Restrictions.eq("mobileNumber", serviceNo).ignoreCase())
						.add(Restrictions.eq("uhidNo", serviceNo).ignoreCase()));
		list = cr.list();
		
		//System.out.println(list.size());
		if(list.size()>0) {
			map.put("list", list);
			map.put("errorInServiceNo", "");
		}else {
			 map.put("errorInServiceNo", "errorInServiceNo");
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getOpdReportsDetailsbyPatinetId(HashMap<String,Object> jsonData) {
		
		List<Visit> list =null;
		//long visitId = Long.parseLong((String) jsonData.get("visitId"));
		long patientId = Long.parseLong((String) jsonData.get("patientId"));
		Map<String,Object> map = new HashMap<String, Object>();
		try{
		Long appointmentType=(long) 1;	
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = null;
		if(jsonData.get("reportsType").equals("opd"))
		{	
		cr=session.createCriteria(Visit.class)
				      .add(Restrictions.eq("patientId", patientId))
				      .add(Restrictions.eq("visitStatus", "c").ignoreCase()).addOrder(Order.desc("visitDate"));
		}
		else
		 {
		
			cr=session.createCriteria(Visit.class)
						.add(Restrictions.eq("patientId", patientId))/*
																		 * .add(Restrictions.eq("appointmentTypeId",
																		 * appointmentType))
																		 */
				      .addOrder(Order.desc("visitDate"));	
		 }
		 list= cr.list();
		 map.put("list", list);
		 getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

public ReferralPatientHd getReferralPatientHdByExeHosAndOpdPd(Long exeHoss,Long opdPdId) {
	Session session =null;
	ReferralPatientHd referralPatientHd=null;
	try {
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(ReferralPatientHd.class)
				.add(Restrictions.eq("extHospitalId", exeHoss)).add(Restrictions.eq("opdPatientDetailsId", opdPdId));
		List<ReferralPatientHd>listReferralPatientHd=cr.list();
		if(CollectionUtils.isNotEmpty(listReferralPatientHd)) {
			referralPatientHd=listReferralPatientHd.get(0);
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	} 
	return referralPatientHd;
}

public ReferralPatientHd getInternalReferralPatientHdByOpdPd(Long intHoss,Long opdPatientId) {
	Session session =null;
	ReferralPatientHd referralPatientHd=null;
	try {
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(ReferralPatientHd.class)
				.add(Restrictions.eq("intHospitalId", intHoss)).add(Restrictions.eq("opdPatientDetailsId", opdPatientId));
		List<ReferralPatientHd>listReferralPatientHd=cr.list();
		if(CollectionUtils.isNotEmpty(listReferralPatientHd)) {
			referralPatientHd=listReferralPatientHd.get(0);
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	} 
	return referralPatientHd;
}

public DgOrderhd getOrderDatebyInvestigation(Date orderdate,Long visitId) {
	Session session =null;
	DgOrderhd investigationHeaderHd=null;
	try {
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(DgOrderhd.class)
				.add(Restrictions.eq("orderDate", orderdate)).add(Restrictions.eq("visitId", visitId));
		List<DgOrderhd>listInvHd=cr.list();
		if(CollectionUtils.isNotEmpty(listInvHd)) {
			investigationHeaderHd=listInvHd.get(0);
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	} 
	return investigationHeaderHd;
	}

public PatientPrescriptionHd getPatientPresecriptionHd(Long opdPatientId,Long visitId) {
	Session session =null;
	PatientPrescriptionHd ppHeaderId=null;
	try {
		session=getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(PatientPrescriptionHd.class)
				.add(Restrictions.eq("opdPatientDetailsId", opdPatientId)).add(Restrictions.eq("visitId", visitId));
		List<PatientPrescriptionHd>listInvHd=cr.list();
		if(CollectionUtils.isNotEmpty(listInvHd)) {
			ppHeaderId=listInvHd.get(0);
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	} 
	return ppHeaderId;
}





//////////////////////code by dhiraj /////////////////

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@Override
	public Map<String, Object> minorSurgeryWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		try {
			long hospital_id = Long.parseLong(jsondata.get("hospital_id"));
			Date fromDate = null;
			String serviceNo = jsondata.get("service_no");
			if (jsondata.get("from_date") != null && !jsondata.get("from_date").equals("")) {				
				fromDate = HMSUtil.convertStringTypeDateToDateType(jsondata.get("from_date"));
			}
			Date toDate = null;
			if (jsondata.get("to_date") != null && !jsondata.get("to_date").equals("")) {				
				toDate = HMSUtil.convertStringTypeDateToDateType(jsondata.get("to_date"));
			}

			String SpageNo = String.valueOf(jsondata.get("pageNo"));
			int pageNo = Integer.parseInt(SpageNo);
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;

			List<ProcedureHd> msWaitingList = new ArrayList<>();
			Criteria criteria = session.createCriteria(ProcedureHd.class, "phd").createAlias("phd.masHospital", "mh")
					.createAlias("phd.patient", "pt").add(Restrictions.eq("mh.hospitalId", hospital_id))
					.add(Restrictions.eq("procedureType", "M")).add(Restrictions.eq("status", "N"))
					.createAlias("phd.opdPatientDetails", "opd").addOrder(Order.desc("opd.opdDate"));

			if ((fromDate != null && !fromDate.equals("")) && (toDate != null && !toDate.equals(""))) {
				toDate.setHours(23);
				toDate.setMinutes(59);
				toDate.setSeconds(59);
				criteria = criteria.add(Restrictions.ge("phd.requisitionDate", fromDate));
				criteria = criteria.add(Restrictions.le("phd.requisitionDate", toDate));
			}

			else if (fromDate != null && !fromDate.equals("")) {
							
				criteria = criteria.add(Restrictions.ge("phd.requisitionDate", fromDate));
			}
			else if (toDate != null && !toDate.equals("")) {				
				toDate.setHours(23);
				toDate.setMinutes(59);
				toDate.setSeconds(59);
				criteria = criteria.add(Restrictions.le("phd.requisitionDate", toDate));
			}

			if (serviceNo != null && !serviceNo.equals("")) {

				criteria = criteria.add(Restrictions.eq("pt.serviceNo", serviceNo).ignoreCase());

			}
			
			msWaitingList = criteria.list();
			count = msWaitingList.size();
			map.put("count", count);

			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			msWaitingList = criteria.list();
			map.put("minorSurgeryWaitingList", msWaitingList);
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMinorSurgeryDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		long headerId = Long.parseLong(jsondata.get("header_id"));

		List<ProcedureDt> list = session.createCriteria(ProcedureDt.class)
				.add(Restrictions.eq("procedureHd.procedureHdId", headerId)).list();	

		map.put("detailList", list);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasAnesthesia> getAnesthesiaList() {
		List<MasAnesthesia> anesthesiaList = new ArrayList<MasAnesthesia>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasAnesthesia.class);
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("anesthesiaId").as("anesthesiaId"));
			projectionList.add(Projections.property("anesthesiaName").as("anesthesiaName"));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("status", "Y"));
			anesthesiaList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasAnesthesia.class))
					.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return anesthesiaList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String saveMinorSurgery(HashMap<String, Object> jsondata) {
		long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);	
		Transaction tx = null;
		Session session = null;
		ProcedureDt refNursingDt = null;
		String result = "";
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {

			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			if (jsondata.get("listofMinorDT") != null) {
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> listNursing = (List<HashMap<String, Object>>) jsondata
						.get("listofMinorDT");
               Long headerId=null;
               Long patientId=null;
               String height=null;
			   String idealWeight=null;
			   String weight=null;
			   Double varation=null;
			   String temperature=null;
			   String bpDiastolic=null;
			   String bpSystolic=null;
			   String pulse=null;
			   String spo2=null;
			   String bmi=null;
			   String rr=null;
			   Long masNursingId=null;
				if (listNursing != null) {
					for (HashMap<String, Object> map : listNursing) {
						headerId=Long.parseLong(map.get("headerId").toString());
						if (map.get("msId").toString() != null && !map.get("msId").toString().equals("")) {
							String value1 = map.get("msId").toString();
							Criteria criteria = session.createCriteria(MasNursingCare.class)
									.add(Restrictions.eq("nursingCode", value1));
							List<MasNursingCare> nurmasList = criteria.list();
							if(nurmasList !=null && nurmasList.size()>0) {
								masNursingId = nurmasList.get(0).getNursingId();
							}
							Criteria cr = session.createCriteria(ProcedureDt.class)
									.add(Restrictions.eq("procedureId", masNursingId));
							cr.add(Restrictions.eq("procedureHdId", headerId));
							List<ProcedureDt> dtlist = cr.list();
							if (dtlist != null && dtlist.size() > 0) {

								refNursingDt = dtlist.get(0);

								if (map.get("anethesiaType").toString() != null
										&& !map.get("anethesiaType").toString().equals("")) {

									String value3 = map.get("anethesiaType").toString();
									MasAnesthesia mas = new MasAnesthesia();
									mas.setAnesthesiaId(Long.parseLong(value3));
									refNursingDt.setMasAnesthesia(mas);
									refNursingDt.setStatus("Y");

								}

								if (map.get("userId").toString() != null && !map.get("userId").toString().equals("")) {
									Long userId = Long.parseLong(map.get("userId").toString());
									Users user = new Users();
									user.setUserId(userId);
									refNursingDt.setUsers(user);

								}

								if (map.get("remarks").toString() != null) {
									String value4 = map.get("remarks").toString();
									refNursingDt.setNursingRemark(value4);
								}
								
								refNursingDt.setLastChgDate(date);
								tx = session.beginTransaction();
								session.update(refNursingDt);
								session.flush();
								tx.commit();
								tx = null;

							} else {
								refNursingDt = new ProcedureDt();
								Long masNurId=null;
								if (map.get("msId").toString() != null && !map.get("msId").toString().equals("")) {
									String value2 = map.get("msId").toString();
									Criteria cr1 = session.createCriteria(MasNursingCare.class)
											.add(Restrictions.eq("nursingCode", value2));
									
									List<MasNursingCare> nmList = cr1.list();
									if(nmList !=null && nmList.size()>0) {
										masNurId = nurmasList.get(0).getNursingId();
									}
									if(masNurId !=null) {
									refNursingDt.setProcedureId(masNurId);
									}
								}

								if (map.get("anethesiaType").toString() != null
										&& !map.get("anethesiaType").toString().equals("")) {
									String value3 = map.get("anethesiaType").toString();
									MasAnesthesia mas = new MasAnesthesia();
									mas.setAnesthesiaId(Long.parseLong(value3));
									refNursingDt.setMasAnesthesia(mas);
									refNursingDt.setStatus("Y");
								} else {
									refNursingDt.setStatus("N");
								}
								if (map.get("remarks").toString() != null
										) {
									String value4 = map.get("remarks").toString();
									refNursingDt.setNursingRemark(value4);
								}

								if (map.get("headerId").toString() != null
										&& !map.get("headerId").toString().equals("")) {
									String value5 = map.get("headerId").toString();
									refNursingDt.setProcedureHdId(Long.parseLong(value5));
								}
								if (map.get("userId").toString() != null && !map.get("userId").toString().equals("")) {
									Long userId = Long.parseLong(map.get("userId").toString());
									Users user = new Users();
									user.setUserId(userId);
									refNursingDt.setUsers(user);

								}
								refNursingDt.setLastChgDate(date);
								tx = session.beginTransaction();								
								refNursingDt.setAppointmentDate(timestamp);
								refNursingDt.setProcedureDate(timestamp);
								session.save(refNursingDt);
								session.flush();
								tx.commit();
								tx = null;
							}

						}
					
                   
					result = "success";
				}
					OpdPatientDetail opdPatientDetail=new OpdPatientDetail();
					
					patientId=Long.parseLong(jsondata.get("patientId").toString());
					if(jsondata.get("patientId").toString() !=null && !jsondata.get("patientId").toString().equals("")) {
						opdPatientDetail.setPatientId(patientId);
					}
					boolean flag = false;
					height=jsondata.get("height").toString();
					if(height !=null && !height.isEmpty()) {
						opdPatientDetail.setHeight(height);	
						flag = true;
					}
					idealWeight=jsondata.get("idealWeight").toString();
					if(idealWeight !=null && !idealWeight.isEmpty()) {
						opdPatientDetail.setIdealWeight(idealWeight);
						flag = true;
					}
					weight=jsondata.get("weight").toString();
					if(weight !=null && !weight.isEmpty()) {
						opdPatientDetail.setWeight(weight);
						flag = true;
					}
					
					if(jsondata.get("varation").toString() !=null && !jsondata.get("varation").toString().equals("")) {
						varation=new Double(jsondata.get("varation").toString());
						opdPatientDetail.setVaration(varation);
						flag = true;
					}
					temperature=jsondata.get("temperature").toString();
					if(temperature !=null && !temperature.isEmpty()) {
						opdPatientDetail.setTemperature(temperature);
						flag = true;
					}
					bpDiastolic=jsondata.get("bp1").toString();
					if(bpDiastolic !=null && !bpDiastolic.isEmpty()) {
						opdPatientDetail.setBpDiastolic(bpDiastolic);
						flag = true;
					}
					bpSystolic=jsondata.get("bp").toString();
					if(bpSystolic !=null && !bpSystolic.isEmpty()) {
						opdPatientDetail.setBpSystolic(bpSystolic);
						flag = true;
					}
					pulse=jsondata.get("pulse").toString();
					if(pulse !=null && !pulse.isEmpty()) {
						opdPatientDetail.setPulse(pulse);
						flag = true;
					}
					spo2=jsondata.get("spo2").toString();
					if(spo2 !=null && !spo2.isEmpty()) {
						opdPatientDetail.setSpo2(spo2);
						flag = true;
					}
					bmi=jsondata.get("bmi").toString();
					if(bmi !=null && !bmi.isEmpty()) {
						opdPatientDetail.setBmi(bmi);
						flag = true;
					}
					
					rr=jsondata.get("rr").toString();
					if(rr !=null && !rr.isEmpty()) {
						opdPatientDetail.setRr(rr);
						flag = true;
					}
					if(flag == true) {					
					
					opdPatientDetail.setOpdDate(timestamp);	
					opdPatientDetail.setLastChgBy(Long.parseLong(jsondata.get("userId").toString()));					
					 tx = session.beginTransaction();
					  session.save(opdPatientDetail);	
					  session.flush();
					  tx.commit();
					  tx = null;
					  result = "success";
					}
					}
					@SuppressWarnings("unchecked")
					List<ProcedureDt> procedureDtList = session.createCriteria(ProcedureDt.class, "pDt")
							.add(Restrictions.eq("procedureHdId", headerId)).add(Restrictions.eq("status", "N"))
							.list();

					if (procedureDtList.isEmpty()) {
						ProcedureHd procedureHd = null;
						procedureHd = (ProcedureHd) session.get(ProcedureHd.class, headerId);
						if (procedureHd != null) {
							tx = session.beginTransaction();
							procedureHd.setStatus("Y");
							session.update(procedureHd);
							session.flush();
							tx.commit();
							tx = null;

						}
					}	
					
			}
         
		} catch (Exception ex) {

			ex.printStackTrace();
			tx.rollback();
			result = "error";
			return result;
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;

	}

	@Override
	public String deleteMinorSurgery(HashMap<String, Object> jsondata) {
		Transaction tx = null;
		Session session = null;
		String result = "";
		try {
			Long procedureDtId = Long.parseLong(jsondata.get("procedureDtId").toString());
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if (procedureDtId != null && !procedureDtId.equals("")) {
				ProcedureDt pdt = (ProcedureDt) session.createCriteria(ProcedureDt.class)
						.add(Restrictions.eq("procedureDtId", procedureDtId)).uniqueResult();
				tx = session.beginTransaction();
				session.delete(pdt);
				tx.commit();
				result = "success";
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			tx.rollback();
			result = "error";
			return result;
		} finally {
			tx.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;

	}
	
	@Override
	public String validateMinorSurgery(HashMap<String, Object> jsondata) {
		Transaction tx = null;
		Session session = null;
		String result = "";
		String msname=jsondata.get("msName").toString().replaceAll("&amp;", "%&%");
		List<ProcedureDt> pdtlist=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(ProcedureDt.class,"pdt");
			criteria.createAlias("pdt.procedureHd", "visit");
			criteria.createAlias("pdt.masNursingCare", "mnc");
			criteria.add(Restrictions.like("mnc.nursingName", msname));
			criteria.add(Restrictions.eq("visit.visitId", Long.parseLong(jsondata.get("visitId").toString())));
			pdtlist=criteria.list();
			if(pdtlist !=null && pdtlist.size()>0) {
				result="msExists";
			}
			
		} catch (Exception ex) {

			ex.printStackTrace();
			
			
		} finally {
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return result;

	}
	
	
	@Override
	public String findAuthenticatePatient(HashMap<String, Object> jsondata) {
		String status="";
		Session session =null;
		
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(Patient.class)
					.add(Restrictions.eq("uhidNo",   jsondata.get("uhidNo").toString()).ignoreCase())
					.add(Restrictions.eq("patientId",  Long.parseLong(jsondata.get("patientId").toString())))
					;
			
					ProjectionList projectionList = Projections.projectionList();
					projectionList.add(Projections.property("patientId").as("patientId"))
					 .add(projectionList) ;
			List<Patient>listPatient=cr.list();
			if(CollectionUtils.isNotEmpty(listPatient)) {
				status="success";
			}
			else {
				status="error";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return status;

	}
	@Override
	public String findCheckForAuthenticatePatient(HashMap<String, Object> jsondata) {
		String status="";
		Session session =null;
		 List<Object[]> listObject=null;
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			//Long uhid=Long.parseLong(jsondata.get("uhidNo").toString());
			Long patientId=Long.parseLong(jsondata.get("patientId").toString());
			Long visitId=Long.parseLong(jsondata.get("visitId").toString());
			 StringBuilder sbQuery = new StringBuilder();
			/*
			 * sbQuery.append("select p.PATIENT_ID,mu.UNIT_NAME from "
			 * +patientTable+" p join "+masUnitView+" mu on mu.UNIT_ID=p.UNIT_ID  ");
			 * sbQuery.append(" join "+visitTable+" v on p.PATIENT_ID=v.PATIENT_ID join "
			 * +masHospitalTable+" mh on mh.HOSPITAL_ID=v.HOSPITAL_ID  ");
			 * sbQuery.append(" and mh.UNIT_ID = mu.UNIT_ID where v.visit_id=:visitId  and "
			 * ) ; sbQuery.append(" p.patient_Id=:patientId ") ; Query query =
			 * session.createSQLQuery(sbQuery.toString()); query.setParameter("visitId",
			 * visitId); query.setParameter("patientId", patientId); //
			 * query.setParameter("uhidNo", uhid); listObject= query.list();
			 */	 
							 
			
			if(CollectionUtils.isNotEmpty(listObject)) {
				status="popUpNot";
			}
			else {
				status="popUpShow";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return status;

	}
	
	@Override
	public MasItemType getItemTypeIdNiv(String itemTypeCode)
	{
		Session session =null;
		MasItemType itemTypeIdNiv=null;
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasItemType.class)
					.add(Restrictions.eq("itemTypeCode", itemTypeCode));
			List<MasItemType>listInvHd=cr.list();
			if(CollectionUtils.isNotEmpty(listInvHd)) {
				itemTypeIdNiv=listInvHd.get(0);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return itemTypeIdNiv;
	}

	@Override
	public Map<String, Object> getPreviousDgMasInvestigationsAndResult(Long patientId,HashMap<String, Object> jsonData) {
		Transaction transation=null;
		Long mainChargeCode=null;
		Map<String, Object> map = new HashMap<>();
		try {
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");
			mainChargeCode=Long.parseLong(jsonData.get("mainChargeCode").toString());
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
		  Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		  transation=session.beginTransaction();
		 StringBuilder sbQuery = new StringBuilder(); 
		    sbQuery.append("select dgmas.INVESTIGATION_ID,dgmas.INVESTIGATION_name,HD.RESULT_NO,HD.RESULT_DATE,DT.RESULT,DT.RANGE_VALUE ,dgUom.UOM_NAME,dgmas.MAIN_CHARGECODE_ID,U1.USER_NAME as u1FirstName,DT.RIDC_ID,U2.USER_NAME as u2FirstName ,DSMI.SUB_INVESTIGATION_NAME from  "+dgResultEntryHdTable+" HD");
		    sbQuery.append(" left join "+dgResultEntryDtTable+" DT on HD.RESULT_ENTRY_HD_ID= DT.RESULT_ENTRY_HD_ID");
		    sbQuery.append(" left join  "+dgMasInvestigationTable+" dgmas on dgmas.INVESTIGATION_ID=DT.INVESTIGATION_ID");
		    sbQuery.append(" left join "+dgUomTable+"  dgUom on dgUom.UOM_ID=dgmas.UOM_ID"); 
		    sbQuery.append(" left join "+usersTable+" U1   on U1.USER_ID=HD.RESULT_VERIFIED_BY");
		    sbQuery.append("  left join "+usersTable+" U2   on U2.USER_ID=HD.CREATED_BY");
		    sbQuery.append("    LEFT OUTER JOIN  DG_SUB_MAS_INVESTIGATION DSMI ON DSMI.SUB_INVESTIGATION_ID=DT.SUB_INVESTIGATION_ID ");
		   
		    sbQuery.append(" where HD.PATIENT_ID =:patientId and dgmas.MAIN_CHARGECODE_ID=:mainChargeCode");
		    Query query = session.createSQLQuery(sbQuery.toString());
            //System.out.println("query "+query);
		    query.setParameter("patientId", patientId);
		    query.setParameter("mainChargeCode", mainChargeCode);
		     List<Object[]> listObject = query.list();
		     count = listObject.size();
		     
		 	 int listCount = listObject.size();
		     query = query.setFirstResult(pagingSize * (pageNo - 1));
		     query = query.setMaxResults(pagingSize);
		     listObject = query.list();

				map.put("count", count);
				map.put("list", listObject);
		     
		    transation.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
		return map;	
	}
	
	/*@Override
	public List<OpdObesityHd> getObsisityRecord(Long patientId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(OpdObesityHd.class);
		cr.add(Restrictions.eq("patientId", patientId));
		List<OpdObesityHd> list = cr.list();
		System.out.println(list.size());
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}*/
	
	@Override
	public String opdupdateTreatmentTemplate(OpdTemplate opdTemp, List<OpdTemplateTreatment> opdTreatmentList) {

		String result = null;
		Transaction t = null;
		Session session = null;
		Serializable id = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();

			Criteria cr = session.createCriteria(OpdTemplate.class);
			cr.add(Restrictions.eq("templateName", opdTemp.getTemplateName()));
			cr.add(Restrictions.eq("doctorId", opdTemp.getDoctorId()));
			OpdTemplate opdTemplate = (OpdTemplate) cr.uniqueResult();
			/*if(opdTemplate!=null) {
				
				return "Template Name Already Exists";
			}*/
			//session.saveOrUpdate(opdTemp);
			if(opdTemplate!=null)
			{	
			session.clear();
			session.saveOrUpdate(opdTemp);
			for (OpdTemplateTreatment single : opdTreatmentList) {

				//single.setTemplateId(Long.valueOf(id.toString()));
				session.saveOrUpdate(single);

			}

			}
			else
			{	
				id = session.save(opdTemp);
				for (OpdTemplateTreatment single : opdTreatmentList) {

					 if(opdTemplate==null)
	          	   {
						single.setTemplateId(Long.valueOf(id.toString()));
	          	   }
						session.save(single);
				 }
			}
			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			t.rollback();
            ex.printStackTrace();
			return "500";

		}

		return "200";
	}

	@Override
	public String opdTemplateMedicalAdvice(OpdTemplate opdTemp,OpdTemplateMedicalAdvice opdTemplateMedicalList) {

		String result = null;
		Transaction t = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();

			Criteria cr = session.createCriteria(OpdTemplate.class);
			cr.add(Restrictions.eq("templateName", opdTemp.getTemplateName()));
			cr.add(Restrictions.eq("doctorId", opdTemp.getDoctorId()));
			OpdTemplate opdTemplate = (OpdTemplate) cr.uniqueResult();
			if(opdTemplate!=null) {
				
				return "Template Name Already Exists";
			}
			Serializable id = session.save(opdTemp);
			opdTemplateMedicalList.setOpdTemplateId(Long.valueOf(id.toString()));
			session.save(opdTemplateMedicalList);

			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			t.rollback();
            ex.printStackTrace();
			return "500";

		}

		return "200";
	}
	
	public MasDepartment getDepartmentCode(String departmentCode)
	{
		Session session =null;
		MasDepartment departmentId=null;
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasDepartment.class)
					.add(Restrictions.eq("departmentCode", departmentCode));
			List<MasDepartment>listInvHd=cr.list();
			if(CollectionUtils.isNotEmpty(listInvHd)) {
				departmentId=listInvHd.get(0);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return departmentId;
	}
	
	public String childVacatinationChart(List<ChildVacatinationChart> childVacation) {

		String result = null;
		Transaction t = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			t = session.beginTransaction();
			for (ChildVacatinationChart single : childVacation) {
            		session.saveOrUpdate(single);
    			}
			t.commit();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		} catch (Exception ex) {
			t.rollback();
            ex.printStackTrace();
			return "500";

		}

		return "200";
	}
	
	@Override
	public List<ChildVacatinationChart> getchildVacatinationChart(Long patientId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(ChildVacatinationChart.class);
		cr.add(Restrictions.eq("patientId", patientId));
		List<ChildVacatinationChart> list = cr.list();
		//System.out.println(list.size());
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}


	public Long getAppointmentTypeId(String code, Session session) {
		Long id = new Long(0);
		try {
			
			List<MasAppointmentType> list = session.createCriteria(MasAppointmentType.class)
					.add(Restrictions.eq("appointmentTypeCode", code)).list();
			
			id = list.get(0).getAppointmentTypeId();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return id;
	}

	/*@Override
	public String checkAuthenticateEHR(HashMap<String, Object> jsondata) {
		Session session = null;
		String authenticateStatus = "";
		String serviceNo = "";
		Long hospitalId = null;
		Long patientId=null;
		
		if(jsondata.containsKey("serviceNo")) {
			if(!jsondata.get("serviceNo").equals("") && jsondata.get("serviceNo")!=null) {
				serviceNo = jsondata.get("serviceNo").toString().toUpperCase();
				System.out.println("serviceNo :: "+serviceNo);
			}
		}
		
		if(jsondata.containsKey("hospitalId")) {
			if(!jsondata.get("hospitalId").equals("") && jsondata.get("hospitalId")!=null) {
				hospitalId = Long.parseLong(jsondata.get("hospitalId").toString());
				
			}
		}
		
		if(jsondata.containsKey("patientId")) {
			if(!jsondata.get("patientId").equals("") && jsondata.get("patientId")!=null) {
				patientId = Long.parseLong(jsondata.get("patientId").toString());
				
			}
		}
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			boolean flag = authenticateServiceNumber(serviceNo, session);

			if (flag == true) {
				Criteria cr = session.createCriteria(Patient.class)
						.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase());

				List<Patient> pList = cr.list();
				if (pList != null && pList.size() > 0) {
					Long unitIdFromPatient = null;//pList.get(0).getUnitId();
					Criteria cri1 = session.createCriteria(MasHospital.class)
							.add(Restrictions.eq("hospitalId", hospitalId));
					List<MasHospital> hospitalsList = cri1.list();
					Long unitIdFromMasUnit = hospitalsList.get(0).getMasUnit().getUnitId();

					
					StringBuilder queryBuilderRmo = new StringBuilder();
					queryBuilderRmo.append(" SELECT COUNT(*) as counter, A.MI_UNIT FROM public.VU_MAS_MIUNIT A " + 
										" WHERE A.MI_UNIT in (SELECT MI_UNIT FROM public.VU_MAS_MIUNIT WHERE UNIT_ID=:unitIdFromPatient) AND " + 
										" A.UNIT_ID=:unitIdFromMasUnit ");
					
					queryBuilderRmo.append(" group by A.MI_UNIT ");
					
					Query query1 = session.createSQLQuery(queryBuilderRmo.toString());
					query1.setParameter("unitIdFromPatient", unitIdFromPatient);
					query1.setParameter("unitIdFromMasUnit", unitIdFromMasUnit);
					
					
				    List<Object[]> objectList1 = query1.list();
				    int counter = objectList1.size();
				    if(counter>0) {
				    	authenticateStatus = "success";
				    }else {
						
						 * StringBuilder queryBuilderMo = new StringBuilder(); queryBuilderMo.
						 * append("WITH RECURSIVE cte AS (SELECT  1 CountA, VU_MAS_EMPLOYEE.ICG_UNIT_ID"
						 * + " FROM public.VU_MAS_EMPLOYEE" +
						 * " WHERE   VU_MAS_EMPLOYEE.SERVICE_NO =:serviceNo" +
						 * " AND 	UNIT_ID IN (SELECT  VU.UNIT_ID::char FROM public.VU_MAS_UNIT VU, ship.MAS_HOSPITAL MH WHERE   MH.UNIT_ID=VU.UNIT_ID  AND UPPER(STATUS)='Y' AND VU.UNIT_ID=:unitIdFromMasUnit) "
						 * +
						 * " UNION ALL SELECT  1, VU_MAS_EMPLOYEE.ICG_UNIT_ID FROM public.VU_MAS_EMPLOYEE WHERE   VU_MAS_EMPLOYEE.SERVICE_NO =:serviceNo AND UNIT_ID IN (SELECT  VU.UNIT_ID::char FROM ship.VU_MAS_UNIT VU, ship.MAS_HOSPITAL MH WHERE   MH.UNIT_ID=VU.UNIT_ID  AND UPPER(STATUS)='Y' AND VU.UNIT_CODE = VU.UNIT_PARENT_ID::char))"
						 * );
						 * 
						 * queryBuilderMo.
						 * append("SELECT count(*),ICG_UNIT_ID FROM cte group by ICG_UNIT_ID");
						 * 
						 * Query query2 = session.createSQLQuery(queryBuilderMo.toString());
						 * query2.setParameter("serviceNo", serviceNo);
						 * query2.setParameter("unitIdFromMasUnit", unitIdFromMasUnit);
						 * 
						 * 
						 * List<Object[]> objectList2 = query2.list(); int counterMo =
						 * objectList2.size();
						 
						
						 * if (counterMo > 0) { authenticateStatus = "success"; } else {
						 * authenticateStatus = "failure"; }
						 
				    }
				}
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return "success";
	}*/
	
	@SuppressWarnings("unchecked")
	private boolean authenticateServiceNumber(String serviceNo, Session session) {
		boolean flag = false;
		try {
			Criteria crr = session.createCriteria(Patient.class)
					.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase());
			List<Patient> patientList = crr.list();
			if(patientList!=null && patientList.size()>0) {
				flag = true;
			}else {
				return flag;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
		
	}
	@Override
	public String authenticateUHID(HashMap<String, Object> jsondata) {
		String status="";
		Session session =null;
		String uhidNo = "";
		Long patientId = null;
		if(jsondata.containsKey("uhidNo")) {
			if(!jsondata.get("uhidNo").equals("") && jsondata.get("uhidNo")!=null) {
				uhidNo = jsondata.get("uhidNo").toString();
				//System.out.println("uhidNo :: "+uhidNo);
			}
		}
		if(jsondata.containsKey("patientId")) {
			if(!jsondata.get("patientId").equals("") && jsondata.get("patientId")!=null) {
				patientId = Long.parseLong(jsondata.get("patientId").toString());
				//System.out.println("patientId :: "+patientId);
			}
		}
		
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(Patient.class).add(Restrictions.ilike("uhidNo", jsondata.get("uhidNo").toString()));
					
					List<Patient>listPatient=criteria.list();
					if(listPatient!=null && listPatient.size()>0) {
						//patientId = listPatient.get(0).getPatientId();
						Criteria cr = session.createCriteria(Patient.class)
								.add(Restrictions.ilike("uhidNo",uhidNo))
								.add(Restrictions.eq("patientId", patientId));
						
								ProjectionList projectionList = Projections.projectionList();
								projectionList.add(Projections.property("patientId").as("patientId"))
								 .add(projectionList) ;
						List<Patient>listPatient1=cr.list();
						if(CollectionUtils.isNotEmpty(listPatient1)) {
							status="success";
						}
						else {
							status="error";
						}
					}else {
						status="error";
					}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return status;
	}
	
	@Override
	public Long getPatientIdUHIDWise(HashMap<String, Object> jsondata) {
		Long patientId=null;
		String uhidNo="";
		Session session =null;
		if(jsondata.containsKey("uhidNo")) {
			if(!jsondata.get("uhidNo").equals("") && jsondata.get("uhidNo")!=null) {
				uhidNo = jsondata.get("uhidNo").toString();
				
			}
		}
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(Patient.class).add(Restrictions.ilike("uhidNo", jsondata.get("uhidNo").toString()));
				
				List<Patient>listPatient=criteria.list();
				if(listPatient!=null && listPatient.size()>0) {
					patientId = listPatient.get(0).getPatientId();
				}	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return patientId;
	}



	@Override
	public String getRankBySerivceNo(String serviceNo) {
		
		String rankName="";
		String rankCode="";
		Session session =null;
		List<MasRank> masrankList=null;
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasEmployee.class).add(Restrictions.eq("serviceNo", serviceNo));
				
				List<MasEmployee> listEmployee=criteria.list();
				if(listEmployee!=null && listEmployee.size()>0) {
					rankCode = listEmployee.get(0).getMasRank();
					if(rankCode !=null && !rankCode.isEmpty()) {
						Criteria cr = session.createCriteria(MasRank.class).add(Restrictions.eq("rankCode", rankCode));
						masrankList=cr.list();
						if(masrankList !=null && masrankList.size() >0) {
							rankName=masrankList.get(0).getRankName();
						}
					}
				}	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return rankName;
		
	}
	
	@Override
	public String getRelationName(long relationId) {
		String relationName="";
		List<MasRelation> relationList = new ArrayList<MasRelation>(); 
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			relationList = session.createCriteria(MasRelation.class).add(Restrictions.eq("relationId", relationId)).list();
			if(!relationList.isEmpty() && relationList.size()>0) {
				relationName = relationList.get(0).getRelationName();
			}else {
				relationName="";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return relationName;
	}
	
	@Override
	public List<MasAdministrativeSex> getGenderList() {
		List<MasAdministrativeSex> adminSexList = new ArrayList<MasAdministrativeSex>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr=null;
		try {
			cr = session.createCriteria(MasAdministrativeSex.class)
					.add(Restrictions.eq("status", "Y").ignoreCase())
					.addOrder(Order.asc("administrativeSexName"));
			if(!cr.list().isEmpty() && cr.list().size()>0) {
				adminSexList=cr.list();
			}
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return adminSexList;
	}
	
	@Override
	public List<MasRank> getRankList() {
		List<MasRank> masRankList = new ArrayList<MasRank>();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr=null;
		try {
			cr = session.createCriteria(MasRank.class).addOrder(Order.asc("rankName"));
			if(!cr.list().isEmpty() && cr.list().size()>0) {
				masRankList=cr.list();
			}
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masRankList;
	
	}
	
	@Override
	public List<MasUnit> getUnitList(JSONObject jsonObject) {
		List<MasUnit> unitList = null;		
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasUnit.class)
					.createAlias("masStation", "masStation");
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.property("unitId").as("unitId"));
				projectionList.add(Projections.property("unitName").as("unitName"));
			cr.setProjection(projectionList);
			cr.addOrder(Property.forName("unitName").asc());
						
			unitList = cr.setResultTransformer(new AliasToBeanResultTransformer(MasUnit.class)).list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return unitList;
	
	}
	
	@Override
	public Long getPatientFromUhidNo(String uhidNO) {
			Patient patient = null;
			long patientId = 0;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			try {
				patient = (Patient) session.createCriteria(Patient.class).add(Restrictions.eq("uhidNo", uhidNO).ignoreCase())
						.uniqueResult();
				if (patient != null) {
					patientId = patient.getPatientId();
				}

			} catch (Exception e) {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				e.printStackTrace();
			}
			return patientId;
		}
	
	@Override
	public Patient getPatient(Long patientId) {
		Patient patient =null;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		patient =   (Patient) session.get(Patient.class, patientId);
		return patient;
		
	}
	
	@Override
	public boolean updatePatientDetails(String serviceNo, Long patientId, Long adminSexId, String patientDOB) {
		boolean flag = false;
		Long updatedResult = null;
		Transaction tx = null;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		tx = session.beginTransaction();
		try {
			Patient patientoObject = (Patient)session.get(Patient.class, patientId);
			//patientoObject.setRankId(rankId);
			//patientoObject.setUnitId(unitId);
			//patientoObject.setAdministrativeSexId(adminSexId);
			//patientoObject.setDateOfBirth(HMSUtil.convertStringDateToUtilDate(patientDOB, "dd/MM/yyyy"));
			session.update(patientoObject);
			
			tx.commit();
			flag = true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}
	
	@Override
	public Long createPatient(JSONObject jsondata) {
		String uhidNO="";
		String serviceNo="";
		long patientRelationId=0;
		long registrationTypeId=0;
		long employeeCategoryId=0;
		long patientId=0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			
			 Patient patient=new Patient();
				
			 try {
			 String serviceTypeCodeIcg=HMSUtil.getProperties("adt.properties", "SERVICE_TYPE_CODE_ICG");
			 long serviceTypeIdIcg = getServiceTypeIdFromCode(serviceTypeCodeIcg);
			 //patient.setServiceStatusId(serviceTypeIdIcg);
			 }
			 catch(Exception e) {
				 e.printStackTrace();
			 }
			 if(jsondata.get("serviceNo")!=null && !jsondata.get("serviceNo").toString().equalsIgnoreCase("")) {
					serviceNo = (String.valueOf(jsondata.get("serviceNo")).toUpperCase());
					//patient.setServiceNo(serviceNo);
					}
			
			 if(jsondata.get("employeeName")!=null && !jsondata.get("employeeName").toString().equalsIgnoreCase("")) {
					String empName = (String.valueOf(jsondata.get("employeeName")));
					//patient.setEmployeeName(empName);
				}
			
			
			 if(jsondata.get("rankId")!=null && !jsondata.get("rankId").equals("") && !jsondata.get("rankId").equals("0")) {
					Long rankId=(Long.parseLong(String.valueOf(jsondata.get("rankId"))));
					//patient.setRankId(rankId);
				}
			 
			 if(jsondata.get("unitId")!=null && !jsondata.get("unitId").equals("") &&!jsondata.get("unitId").equals("0")) {
				 Long unitId=(Long.parseLong(String.valueOf(jsondata.get("unitId"))));
				 //patient.setUnitId(unitId);
			 }
			 
			 if(jsondata.get("empMaritalStatusId")!=null && !jsondata.get("empMaritalStatusId").equals("")&&!jsondata.get("empMaritalStatusId").equals("0")) {
					Long maritalstarusId = (Long.parseLong(String.valueOf(jsondata.get("empMaritalStatusId"))));
					//patient.setMaritalStatusId(maritalstarusId);
				}
			 
			if(jsondata.get("empRecordOfficeId")!=null && !jsondata.get("empRecordOfficeId").equals("") &&!jsondata.get("empRecordOfficeId").equals("0")) {
				Long  recordofficeId=(Long.parseLong(String.valueOf(jsondata.get("empRecordOfficeId"))));
				//patient.setRecordOfficeAddressId(recordofficeId);
			}
			
			 if(jsondata.get("empServiceJoinDate")!=null && !jsondata.get("empServiceJoinDate").equals("")) {
				 String  empServiceJoinDate=(String.valueOf(jsondata.get("empServiceJoinDate").toString()));
					try {
						//patient.setServiceJoinDate(HMSUtil.convertStringDateToUtilDate(empServiceJoinDate, "dd/MM/yyyy"));
					} catch (Exception e) {
						e.printStackTrace();
					}
			 }
			 
			if(jsondata.get("patientName")!=null && !jsondata.get("patientName").equals("")) {
					String patientName = (String.valueOf(jsondata.get("patientName")));
					patient.setPatientName(patientName);
				}
				
				if(jsondata.get("patientDOB")!=null && !jsondata.get("patientDOB").equals("")) {
					 String patientDOB = (String.valueOf(jsondata.get("patientDOB")));
					 try {
						 patient.setDateOfBirth(HMSUtil.convertStringDateToUtilDate(patientDOB, "dd/MM/yyyy"));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
				 }
				

				if(jsondata.get("genderId")!=null && !jsondata.get("genderId").equals("")&&!jsondata.get("genderId").equals("0")) {
					Long patientGenderId=(Long.parseLong(String.valueOf(jsondata.get("genderId"))));
					//patient.setAdministrativeSexId(patientGenderId);
				}

				if(jsondata.get("relationId")!=null && !jsondata.get("relationId").equals("")&&!jsondata.get("relationId").equals("0")) {
					patientRelationId=(Long.parseLong(String.valueOf(jsondata.get("relationId"))));
					//patient.setRelationId(patientRelationId);
				}
				
				if(jsondata.get("employeeCategoryId")!=null && !jsondata.get("employeeCategoryId").equals("")&&!jsondata.get("employeeCategoryId").equals("0")) {
					employeeCategoryId=(Long.parseLong(String.valueOf(jsondata.get("employeeCategoryId"))));
					//patient.setEmployeeCategoryId(employeeCategoryId);
				}
				
				if(jsondata.get("registrationTypeId")!=null && !jsondata.get("registrationTypeId").equals("")&&!jsondata.get("registrationTypeId").equals("0")) {
					registrationTypeId=(Long.parseLong(String.valueOf(jsondata.get("registrationTypeId"))));
					//patient.setRegistrationTypeId(registrationTypeId);
				}
			
				uhidNO = getHinNo(serviceNo,patientRelationId,registrationTypeId);
				//patient.setUhidNo(uhidNO);
				long existingPatientId = getPatientFromUhidNo(uhidNO);
				if(existingPatientId!=0) {
					patientId = existingPatientId;
				}else {
					session.save(patient);
					tx.commit();
					patientId = patient.getPatientId();
				}
		}catch(Exception e) {
			tx.rollback();
			patientId=0;
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		
		return patientId;
	
	}
	
	public String getHinNo(String serviceNo,long patientRelationId,long registrationTypeId) {

		Map<String, Object> serviceAndRelationMap = new HashMap<String, Object>();
		long IcgRegistrationTypeId =Long.parseLong(HMSUtil.getProperties("adt.properties", "ICG_REGISTRATION_TYPE_ID"));
		
		String uhidNO = "";
		String patientCode = "";
		String relationCode = "";
		long dependentCount=0;
		//boolean existsUhidNo=false;
	
		serviceAndRelationMap = getPatientTypeCodeAndRelationCode(patientRelationId,registrationTypeId);

		if (serviceAndRelationMap.get("relationCode") != null) {
			relationCode = (String) serviceAndRelationMap.get("relationCode");
		}
		if (serviceAndRelationMap.get("registrationTypeCode") != null) {
			patientCode = (String) serviceAndRelationMap.get("registrationTypeCode");
		}
		if (registrationTypeId==IcgRegistrationTypeId) {
			if(relationCode.equalsIgnoreCase("N") || relationCode.equalsIgnoreCase("D")) {
				dependentCount = getNoOfDependentForServiceNo(serviceNo,patientRelationId);
				if(dependentCount!=0) {
					relationCode=relationCode+(dependentCount+1);
					uhidNO= patientCode.concat(serviceNo).concat(relationCode);	
				}else {
					relationCode=relationCode+1;
					uhidNO= patientCode.concat(serviceNo).concat(relationCode);
				}
			}else {
				uhidNO = patientCode.concat(serviceNo).concat(relationCode);
			}
			
		} else {
			String maxSequenceNo = "";
			maxSequenceNo = getHinIdOthers(patientCode);
			Integer i;
			if (!maxSequenceNo.equals("")) {
				i = Integer.parseInt(maxSequenceNo) + 1;

			} else {
				i = 01;
			}
			String seqNo = "";
			if (i <= 9) {
				seqNo = "0" + i.toString();
			} else {
				seqNo = i.toString();
			}
			uhidNO = patientCode.concat(seqNo.toString());
		}
		return uhidNO;	
	}
	
	private String getHinIdOthers(String patientCode) {

		String previousHinNo = "";
		String maxSequenceNo = "";
		List<Patient> previousHinNoList = new ArrayList<Patient>();
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
				previousHinNoList = session.createCriteria(Patient.class).createAlias("masRegistrationType", "mt")
						.add(Restrictions.eq("mt.registrationTypeCode", Long.parseLong(patientCode))).list();
			
			if (previousHinNoList.size() > 0) {

				ArrayList hinNoSequenceList = new ArrayList();
				for (Patient patient : previousHinNoList) {
					
						previousHinNo = (patient.getUhidNo());						
						String sequenceNo = previousHinNo.substring(1);
						int i = Integer.parseInt(sequenceNo);
						hinNoSequenceList.add(i);
						
					
				}

				if (hinNoSequenceList.size() > 0) {
					maxSequenceNo = Collections.max(hinNoSequenceList)
							.toString();
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		//	session.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return maxSequenceNo;
	
	}
	
	private long getServiceTypeIdFromCode(String serviceTypeCodeIcg) {
		long serviceTypeIdIcg=0;
		MasServiceType masService = null;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			masService=(MasServiceType) session.createCriteria(MasServiceType.class)
			.add(Restrictions.eq("serviceTypeCode",serviceTypeCodeIcg)).uniqueResult();
			
			if(masService!=null) {
				serviceTypeIdIcg = masService.getServiceTypeId();
			}
			
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return serviceTypeIdIcg;
	}
	
	private Map<String, Object> getPatientTypeCodeAndRelationCode(long patientRelationId, long registrationTypeId) {

		List<MasRelation> masRelationList = new ArrayList<MasRelation>();
		List<MasRegistrationType> registrationTypeList = new ArrayList<MasRegistrationType>();	
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String relationCode = "";
			String registrationTypeCode = "";

			
			masRelationList = session.createCriteria(MasRelation.class)
					/* .add(Restrictions.eq("status", "y").ignoreCase()) */
					.add(Restrictions.idEq(patientRelationId)).list();
			
			
			for (MasRelation masRelation : masRelationList) {
				relationCode =String.valueOf(masRelation.getRelationCode()) ;
				map.put("relationCode", relationCode);
			}	
			
			registrationTypeList = session.createCriteria(MasRegistrationType.class)
					.add(Restrictions.eq("status", "y").ignoreCase())
					.add(Restrictions.idEq(registrationTypeId)).list();
				
			for (MasRegistrationType registrationType : registrationTypeList) {
				registrationTypeCode = String.valueOf(registrationType.getRegistrationTypeCode());
				map.put("registrationTypeCode", registrationTypeCode);
			}
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		}
		return map;
	
	}
	
	private long getNoOfDependentForServiceNo(String serviceNo, long patientRelationId) {

		long  count=0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
		count=	(long) session.createCriteria(Patient.class)
			.add(Restrictions.eq("serviceNo",serviceNo)).add(Restrictions.eq("masRelation.relationId", patientRelationId))
			.setProjection(Projections.rowCount()).uniqueResult();
			
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.getMessage();
			e.printStackTrace();
		}
		return count;
		
	
	}
	
	@Override
	public String updateVisitStatusReject(HashMap<String, Object> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction t = session.beginTransaction();
		
		try {
		
		//String vistIdString=jsondata.get("visitId").toString();
		String flag=jsondata.get("mbVisit").toString();
		List<HashMap<String, Object>> vistIdString = (List<HashMap<String, Object>>) (Object)jsondata.get("visitId");
		if (vistIdString != null) {
      	  for (HashMap<String, Object> visitMultiple : vistIdString)
		  {	
			String vistId=(String) visitMultiple.get("visitId");
			if(vistId!=null && flag.equals("OPD"))
			{	
			String qryStringHd  ="update Visit v set v.visitStatus='R' where v.visitId='"+vistId+"' " ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			//System.out.println(countHd + " Record(s) OPD Visit Reject Records Updated.");
			
			}
		
		}	
		}  
		}catch (Exception e) {
			e.printStackTrace();
			return "500";
			// TODO: handle exception
		}
		finally
		{
		
			t.commit();
		}
		return "statusUpdated";
	}
	
	@Override
	public Patient getPatientByServiceNo(JSONObject jsonObject) {
		Session session=null;		
		Patient patientObj	=null;
		List<Patient> patientList=new ArrayList<Patient>();
		
		
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr=session.createCriteria(Patient.class);			
				cr.add(Restrictions.and(Restrictions.eq("serviceNo", jsonObject.get("serviceNo").toString()).ignoreCase(),
						Restrictions.eq("relationId", Long.parseLong(jsonObject.get("relationId").toString()))));	
				patientList=cr.list();
			if(CollectionUtils.isNotEmpty(patientList)) {
				patientObj=patientList.get(0);
			}
			
		 }
	
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			 
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
		}
		return patientObj;
	}
	
	@Override
	public MasStoreSection getSectionId(String sectionCode)
	{
		Session session =null;
		MasStoreSection sectionId=null;
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasStoreSection.class)
					.add(Restrictions.eq("sectionCode", sectionCode));
			List<MasStoreSection>listInvHd=cr.list();
			if(CollectionUtils.isNotEmpty(listInvHd)) {
				sectionId=listInvHd.get(0);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return sectionId;
	}
	
	@Override
	public Map<String, Object> getAdmissionDischargeRegister(Map<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		
		return map;
	}
	
	@Override
	public Long createPatientForSHO(JSONObject jsondata) {
		String uhidNO="";
		String serviceNo="";
		long patientRelationId=0;
		long registrationTypeId=0;
		long employeeCategoryId=0;
		long patientId=0;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			
			 Patient patient=new Patient();
				
			 try {
			 String serviceTypeCodeIcg=HMSUtil.getProperties("adt.properties", "SERVICE_TYPE_CODE_ICG");
			 long serviceTypeIdIcg = getServiceTypeIdFromCode(serviceTypeCodeIcg);
			 //patient.setServiceStatusId(serviceTypeIdIcg);
			 }
			 catch(Exception e) {
				 e.printStackTrace();
			 }
			 if(jsondata.get("serviceNo")!=null && !jsondata.get("serviceNo").toString().equalsIgnoreCase("")) {
					serviceNo = (String.valueOf(jsondata.get("serviceNo")).toUpperCase());
					//patient.setServiceNo(serviceNo);
					}
			
			 if(jsondata.get("employeeName")!=null && !jsondata.get("employeeName").toString().equalsIgnoreCase("")) {
					String empName = (String.valueOf(jsondata.get("employeeName")));
					//patient.setEmployeeName(empName);
				}
			
			
			 if(jsondata.get("rankId")!=null && !jsondata.get("rankId").equals("") && !jsondata.get("rankId").equals("0")) {
					Long rankId=(Long.parseLong(String.valueOf(jsondata.get("rankId"))));
					//patient.setRankId(rankId);
				}
			 
			 if(jsondata.get("unitId")!=null && !jsondata.get("unitId").equals("") &&!jsondata.get("unitId").equals("0")) {
				 Long unitId=(Long.parseLong(String.valueOf(jsondata.get("unitId"))));
				 //patient.setUnitId(unitId);
			 }
			 
			 if(jsondata.get("empMaritalStatusId")!=null && !jsondata.get("empMaritalStatusId").equals("")&&!jsondata.get("empMaritalStatusId").equals("0")) {
					Long maritalstarusId = (Long.parseLong(String.valueOf(jsondata.get("empMaritalStatusId"))));
					//patient.setMaritalStatusId(maritalstarusId);
				}
			 
			if(jsondata.get("empRecordOfficeId")!=null && !jsondata.get("empRecordOfficeId").equals("") &&!jsondata.get("empRecordOfficeId").equals("0")) {
				Long  recordofficeId=(Long.parseLong(String.valueOf(jsondata.get("empRecordOfficeId"))));
				//patient.setRecordOfficeAddressId(recordofficeId);
			}
			
			 if(jsondata.get("empServiceJoinDate")!=null && !jsondata.get("empServiceJoinDate").equals("")) {
				 String  empServiceJoinDate=(String.valueOf(jsondata.get("empServiceJoinDate").toString()));
					try {
						//patient.setServiceJoinDate(HMSUtil.convertStringDateToUtilDate(empServiceJoinDate, "dd/MM/yyyy"));
					} catch (Exception e) {
						e.printStackTrace();
					}
			 }
			 
			if(jsondata.get("patientName")!=null && !jsondata.get("patientName").equals("")) {
					String patientName = (String.valueOf(jsondata.get("patientName")));
					patient.setPatientName(patientName);
				}
				
				if(jsondata.get("patientDOB")!=null && !jsondata.get("patientDOB").equals("")) {
					 String patientDOB = (String.valueOf(jsondata.get("patientDOB")));
					 try {
						 patient.setDateOfBirth(HMSUtil.convertStringDateToUtilDate(patientDOB, "dd/MM/yyyy"));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
				 }
				

				if(jsondata.get("genderId")!=null && !jsondata.get("genderId").equals("")&&!jsondata.get("genderId").equals("0")) {
					Long patientGenderId=(Long.parseLong(String.valueOf(jsondata.get("genderId"))));
					//patient.setAdministrativeSexId(patientGenderId);
				}

				if(jsondata.get("relationId")!=null && !jsondata.get("relationId").equals("")&&!jsondata.get("relationId").equals("0")) {
					patientRelationId=(Long.parseLong(String.valueOf(jsondata.get("relationId"))));
					//patient.setRelationId(patientRelationId);
				}
				
				if(jsondata.get("employeeCategoryId")!=null && !jsondata.get("employeeCategoryId").equals("")&&!jsondata.get("employeeCategoryId").equals("0")) {
					employeeCategoryId=(Long.parseLong(String.valueOf(jsondata.get("employeeCategoryId"))));
					//patient.setEmployeeCategoryId(employeeCategoryId);
				}
				
				if(jsondata.get("registrationTypeId")!=null && !jsondata.get("registrationTypeId").equals("")&&!jsondata.get("registrationTypeId").equals("0")) {
					registrationTypeId=(Long.parseLong(String.valueOf(jsondata.get("registrationTypeId"))));
					//patient.setRegistrationTypeId(registrationTypeId);
				}
			
				uhidNO = getHinNo(serviceNo,patientRelationId,registrationTypeId);
				//patient.setUhidNo(uhidNO);
				long existingPatientId = getPatientFromUhidNo(uhidNO);
				if(existingPatientId!=0) {
					patientId = existingPatientId;
				}else {
					session.save(patient);
					tx.commit();
					patientId = patient.getPatientId();
				}
		}catch(Exception e) {
			tx.rollback();
			patientId=0;
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		
		return patientId;
	
	}



	@Override
	public List<PatientSymptom> getPatientSymptom(Long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(PatientSymptom.class);
		cr.add(Restrictions.eq("visitId", visitId));
		List<PatientSymptom> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}
	
	@Override
	public String deletePatientSymptom(HashMap<String, Object> jsondata) {
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction t = session.beginTransaction();
		String visitId=jsondata.get("visitId").toString();
		String symptomId=jsondata.get("symptomId").toString();
		String[] parts=symptomId.split("&");
		String part1 = parts[0];
		//System.out.println("symptomId "+part1);
		if(visitId!=null)
		{	
		String qryStringHd  ="delete from PatientSymptom v where v.visitId='"+visitId+"' and v.symptomId='"+part1+"'" ;
		Query queryHd = session.createQuery(qryStringHd);
        int countHd = queryHd.executeUpdate();
		//System.out.println(countHd + " Record(s) Visit Reject Records Updated.");
		t.commit();
		}
		
		}catch (Exception e) {
			e.printStackTrace();
			return "500";
			// TODO: handle exception
		}
		return "statusUpdated";
	}
	
	@Override
	public String deleteRefferalDetailsNo(Long refferalPatinetHd,Long refferalPatientDt,Long opdPatientDetailsId) {
		try {
			Session session=null;
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
		
		
		if(refferalPatientDt!=null)
		{	
		String qryStringDt  ="delete from ReferralPatientDt rt where rt.refrealHdId='"+refferalPatinetHd+"' " ;
		Query queryDt = session.createQuery(qryStringDt);
        int countDt = queryDt.executeUpdate();
		//System.out.println(countDt + " Record(s) RefferalDt deleted.");
		}
		if(refferalPatinetHd!=null)
		{	
		String qryStringHd  ="delete from ReferralPatientHd rh where rh.opdPatientDetailsId='"+opdPatientDetailsId+"' " ;
		Query queryHd = session.createQuery(qryStringHd);
        int countHd = queryHd.executeUpdate();
		//System.out.println(countHd + " Record(s) RefferalHD deleted.");
		}
		
		}catch (Exception e) {
			e.printStackTrace();
			return "500";
			// TODO: handle exception
		}
		return "statusUpdated";
	}


	public Map<String, Object> getCampAdrressByDate(Long mmuId,String nextCampDate) {
		Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<String> searchList = null;
		try {
			String Query = "SELECT location,landmark FROM MAS_CAMP WHERE CAMP_DATE = (select min(camp_date) from mas_camp where camp_date >='"+nextCampDate+"' and mmu_id='"+mmuId+"' and weekly_off='Camp' ) AND" + 
					" mmu_id='"+mmuId+"' and weekly_off='Camp'";
			//System.out.println(Query);
			if (Query != null) {
				searchList = session1.createSQLQuery(Query).list();
			} else {
				//System.out.println("No Record Found");
			}
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return map;
	}
	
	@Override
	public Map<String, Object> getMasterDoctorRemarks(String idRemarks) {
		Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<String> searchList = null;
		try {
			String Query = "select template_madvice_id,medical_advice from opd_template_medical_advice where template_madvice_id in("+idRemarks+")";
			//System.out.println(Query);
			if (Query != null) {
				searchList = session1.createSQLQuery(Query).list();
			} else {
				//System.out.println("No Record Found");
			}
			map.put("list", searchList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return map;
	}



	@Override
	public List<AuditException> getAuditOpdData(Long visitId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		//Criteria cr = session.createCriteria(AuditOpd.class);
		Criteria cr = session.createCriteria(AuditException.class);
		
		
		cr.add(Restrictions.eq("visitId", visitId));
		List<AuditException> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}



	@Override
	public List<MasAudit> getUniqueNameID(Long auditId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasAudit.class);
		cr.add(Restrictions.eq("auditId", auditId));
		List<MasAudit> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}



	@Override
	public List<PatientDataUpload> getPatientActiveImage(Long patientId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(PatientDataUpload.class);
		cr.add(Restrictions.eq("patientId", patientId));
		cr.add(Restrictions.eq("status", "Y"));
		List<PatientDataUpload> list = cr.list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}



	@Override
	public Map<String, Object> getPatientRecord(long patientId, HashMap<String, Object> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<PatientDataUpload> list = null;
		List<Object[]> searchList = null;
		int count = 0;
		int pageNo = Integer.parseInt(jsondata.get("pageNo") + "");
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
				
		try {	
			//	String Query = "SELECT patient_data_upload_id, patient_id,file_data, status, last_chg_by,last_chg_date FROM patient_data_upload WHERE patient_id ='"+patientId+"'";
	
			String Query ="SELECT p.patient_data_upload_id AS PatientDataUploadId, p.patient_id AS PatientId ,p.file_data AS fileData, p.status AS status, "
					+ "p.last_chg_by AS lastchgBy ,p.last_chg_date AS lastchgDate, u.user_name AS userName"
					+ " FROM patient_data_upload AS p "
					+ "LEFT OUTER JOIN users AS u ON u.user_id = p.last_chg_by "
					+ "WHERE patient_id ='"+patientId+"' ORDER BY p.last_chg_date DESC";
			//System.out.println(Query);
		if (Query != null) 
		{
			searchList = session.createSQLQuery(Query).list();
			
		} 
		else
		{
			//System.out.println("No Record Found");
		}
		Query query = session.createSQLQuery(Query.toString());
		count = searchList.size();
	     
	 	 int listCount = searchList.size();
	     query = query.setFirstResult(pagingSize * (pageNo - 1));
	     query = query.setMaxResults(pagingSize);
	     searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
			//System.out.println("patent historylist:"+searchList.toString());
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;	
	}



	@Override
	public Map<String, Object> getOpdPreviousAuditorRemarks(Long patientId, HashMap<String, Object> jsonData) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String, Object> map = new HashMap<>();
		List<MasIdealWeight> list = null;
		List<Object[]> searchList = null;
		int count = 0;
		int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
				
		try {	
			String Query ="SELECT  VT.VISIT_DATE,AE.exception_date,AE.remarks,U.USER_NAME,MA.audit_name" + 
					" FROM  visit VT LEFT OUTER JOIN  audit_exception AE ON AE.VISIT_ID=VT.VISIT_ID" + 
					" LEFT OUTER JOIN  users U ON AE.last_chg_by=U.USER_ID" + 
					" LEFT OUTER JOIN  mas_audit MA ON MA.audit_id=AE.audit_id" + 
					" WHERE VT.PATIENT_ID='"+patientId+"' AND Upper(VT.VISIT_STATUS)='C' and AE.exception_date is not null ORDER BY VISIT_DATE DESC";
			//String Query ="SELECT  VT.VISIT_DATE,ICD_DIAGNOSIS,WORKING_DIAGNOSIS,past_history,patient_symptoms,VT.VISIT_ID,MD.DEPARTMENT_NAME,U.FIRST_NAME FROM  "+visitTable+" VT  LEFT OUTER JOIN  "+opdPatientDetailsTable+" OPD ON OPD.VISIT_ID=VT.VISIT_ID  LEFT OUTER JOIN  "+masDepartmentTable+" MD ON MD.DEPARTMENT_ID=VT.DEPARTMENT_ID LEFT OUTER JOIN  "+usersTable+" U ON OPD.DOCTOR_ID=U.USER_ID WHERE   VT.PATIENT_ID='"+patientId+"' AND Upper(VT.VISIT_STATUS)='C' AND VT.APPOINTMENT_TYPE_ID='1' ORDER BY VISIT_DATE DESC";
			
		//System.out.println(Query);
		if (Query != null) 
		{
			searchList = session.createSQLQuery(Query).list();
			
		} 
		else
		{
			//System.out.println("No Record Found");
		}
		Query query = session.createSQLQuery(Query.toString());
		count = searchList.size();
	     
	 	 int listCount = searchList.size();
	     query = query.setFirstResult(pagingSize * (pageNo - 1));
	     query = query.setMaxResults(pagingSize);
	     searchList = query.list();

			map.put("count", count);
			map.put("list", searchList);
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;	
	}
	

	


}