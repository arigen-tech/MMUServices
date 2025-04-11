package com.mmu.services.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DgOrderhdDao;
import com.mmu.services.dao.MasterDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.OpdDao;
import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.dao.OpdPatientDetailDao;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.AuditException;
import com.mmu.services.entity.ChildVacatinationChart;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasAnesthesia;
import com.mmu.services.entity.MasAudit;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasFrequency;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasTreatmentInstruction;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdDisposalDetail;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.OpdTemplateMedicalAdvice;
import com.mmu.services.entity.OpdTemplateTreatment;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDataUpload;
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
import com.mmu.services.service.DispensaryService;
import com.mmu.services.service.OpdService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;
import com.mmu.services.utils.ValidateUtils;

@Repository
public class OpdServiceImpl implements OpdService {

	@Autowired
	private Environment environment;
	
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	OpdDao od;

	@Autowired
	OpdPatientDetailDao opdPatientDetailDao;

	@Autowired
	DgOrderhdDao dgOrderhdDao;
	@Autowired
	OpdMasterDao md;
	@Autowired
	SystemAdminDao systemAdminDao;
	
	@Autowired
	MasterDao masterDao;
	 
	@Autowired
	MedicalExamDAO medicalExamDAO;
	
	@Autowired
	PatientRegistrationDao patientRegDao;
	
	@Autowired 
	DispensaryService dispensaryService;
	
 	@Override
	public String PreConsPatientWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID as a  key or value or it is null\"}";
			} else {

				Users checkEmp = od.checkUser(Long.parseLong(jsondata.get("employeeId").toString()));

				if (checkEmp != null) {

					// List<Visit> getvisit = od.getVisit(jsondata);
					Map<String, Object> map = od.getVisit(jsondata);
					int count = (int) map.get("count");
					List<Visit> getvisit = (List<Visit>) map.get("list");
					List<Patient> getPatinet = od.getPatinet();

					if (getvisit.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					}

					else {

						List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
						
						for (Visit v : getvisit) {
							
							Patient p = od.checkPatient(v.getPatientId());
							//MasEmployee me=od.checkEmp(v.getDoctorId());
						    
							for(Patient p1 : getPatinet)
							{
                          	 MasAdministrativeSex mg=od.checkGender(p1.getAdministrativeSexId());
                          	 //MasRelation mr=od.checkRelation(p1.getRelationId());
							 //for(MasEmployee ms : getDoctor)
							 //{
							  
							  if (p!= null ) {
								HashMap<String, Object> pt = new HashMap<String, Object>();
								pt.put("status", v.getVisitStatus());
								pt.put("tokenNo", v.getTokenNo());
								pt.put("priority", v.getPriority());
								pt.put("patientName", p.getPatientName());
								pt.put("patientName", p.getPatientName());
								pt.put("dateOfBirth", p.getDateOfBirth());
								pt.put("gender", mg.getAdministrativeSexName());
								//pt.put("relationName", mr.getRelationName());
								//pt.put("doctorname", checkEmp.getFirstName());
								pt.put("departmentName",v.getMasDepartment().getDepartmentName());
								//pt.put("patientName", v.getPatient().getPatientName());
								c.add(pt);
							}
							//}
							}

						}

						json.put("Visit List", c);
						// json.put("Visit List", getvisit);
						json.put("msg", "Visit List  get  sucessfull... ");
						json.put("status", "1");
						

					}

				} else {
					return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID Not Found\"}";

				}

				return json.toString();
			}
		} finally {
			//System.out.println("Exception Occured");
		}

		} 
	

	///////////////////////////// Mapped Multiple Table /////////////////////////////////////////////
	
	@Override
	public String PreConsPatientWatingListMapped(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
	
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE ID as a  key or value or it is null\"}";
			} else {
				
				Users checkEmp = od.checkUser(Long.parseLong(jsondata.get("employeeId").toString()));
				
				if (checkEmp != null ) {
					
					//List<Visit> getvisit = od.getVisit(jsondata);
					Map<String,Object> map = od.getPreconsulationVisit(jsondata);
					int count = (int) map.get("count");
					List<Visit> getvisit = (List<Visit>) map.get("list");
							
					if (getvisit.size() == 0)
					{
						return "{\"status\":\"0\",\"msg\":\"Visit Data not found\"}";
					}

					else {

						List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
						try
						{
							
						for (Visit v : getvisit) {
							 LocalDate today = LocalDate.now();
							 v.getVisitDate();
							
							//if (v.getVisitStatus().equals("w")){
								
								HashMap<String, Object> pt = new HashMap<String, Object>();
							   
								if(v.getVisitStatus()!=null)
								{
								pt.put("status", v.getVisitStatus());
								}
								else
								{
									pt.put("status", "");
								}
								if(v.getTokenNo()!=null)
								{
								pt.put("tokenNo", v.getTokenNo());
								}
								else
								{
									pt.put("tokenNo", "");
								}
								if(v.getPriority()!=null)
								{
								pt.put("priority", v.getPriority());
								}
								else
								{
									pt.put("priority", "");
								}
								if(v.getPatient()!=null && v.getPatient().getPatientName()!=null)
								{
								pt.put("patientName", v.getPatient().getPatientName());
								}
								else
								{
									pt.put("patientName", "");
								}
								
								 if(v.getPatient()!=null && v.getPatient().getDateOfBirth()!=null)
								    {
									Date s=v.getPatient().getDateOfBirth();
								    Calendar lCal = Calendar.getInstance();
								    lCal.setTime(s);
	                                int yr=lCal.get(Calendar.YEAR);
	                                int mn=lCal.get(Calendar.MONTH) + 1;
	                                int dt=lCal.get(Calendar.DATE);
	                               
									//System.out.println("today"+today);//Today's date
									LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
									//System.out.println("birthday"+birthday);
									Period p = Period.between(birthday, today);
									pt.put("age", p.getYears());
									String ageFull = HMSUtil.calculateAge(s);
									pt.put("ageFull", ageFull);
								    }
								 else
								 {
									 pt.put("age", "");
								 }
								
								if(v.getPatient() != null && v.getPatient().getMasAdministrativeSex()!=null && v.getPatient().getMasAdministrativeSex().getAdministrativeSexName() != null)
								{
								pt.put("gender", v.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
								pt.put("genderId", v.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
								}
								else
								{
									pt.put("gender", "");
									pt.put("genderId", "");
								}
								
								if(v.getUser()!=null && v.getUser().getUserName()!=null)
								{	
								pt.put("doctorname",v.getUser().getUserName());
								}
								else
								{
									pt.put("doctorname","");	
								}
								if(v.getMasDepartment() != null && v.getMasDepartment().getDepartmentName()!=null)
								{
								pt.put("departmentName",v.getMasDepartment().getDepartmentName());
								}
								else
								{
									pt.put("departmentName","");	
								}
								pt.put("visitId", v.getVisitId());
								if(v.getPatient() != null && v.getPatient().getPatientId()!=null)
								{
								pt.put("patientId", v.getPatient().getPatientId());
								}
								//pt.put("patientName", v.getPatient().getPatientName());
								c.add(pt);							
								
							//}						
						
						}
						if(c != null && c.size()>0){
							json.put("data", c);
							json.put("count", count);
							json.put("msg", "Visit List  get  sucessfull... ");
							json.put("status", "1");
						}
						/*
						 * else{ return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}"; }
						 */

					}
					catch(Exception e)
					{
					  e.printStackTrace();
					  //System.out.println(e);
					}

					}
				}
					else
				{
					return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID Not Found\"}";

				}

				return json.toString();
			}
		
	}
	
	
	@Override
	public String addVitalPreConsulataionDetails(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {

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
					opddetails.setIdealWeight(payload.get("idealWeight").toString());
					if (payload.get("varation") != null && !payload.get("varation").equals("")) 
					{
					Double bd = new Double(payload.get("varation").toString());
					opddetails.setVaration(bd);
					}
					opddetails.setTemperature(payload.get("temperature").toString());
					opddetails.setBpSystolic(payload.get("bp").toString());
					opddetails.setBpDiastolic(payload.get("bp1").toString());
					opddetails.setPulse(payload.get("pulse").toString());
					opddetails.setSpo2(payload.get("spo2").toString());
					opddetails.setBmi(payload.get("bmi").toString());
					opddetails.setRr(payload.get("rr").toString());
					opddetails.setOpdDate(ourJavaTimestampObject);
					opddetails.setLastChgDate(ourJavaTimestampObject);
				

					String resp = od.opdVitalDetails(opddetails);
				

					if (resp != null && resp.equalsIgnoreCase("200")) {
						json.put("msg", " Vitals Details Insert successfully ");
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

	//////////////////// OPD Waiting List API /////////////////////////////////////////


	@Override
	public String OpdPatientWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
	
		JSONObject json = new JSONObject();
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE ID as a  key or value or it is null\"}";
			} else {
				
				//Users checkEmp = od.checkUser(Long.parseLong(jsondata.get("employeeId").toString()));
				
				//if (checkEmp != null) {
					
					//List<Visit> getvisit = od.getVisit(jsondata);
					Map<String,Object> map = od.getVisit(jsondata);
					int count = (int) map.get("count");
					List<Visit> getvisit = (List<Visit>) map.get("list");
					if (getvisit.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					}

					else {

						List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
						
						try{
						for (Visit v : getvisit) {
							//if (v.getVisitStatus().equals("w")||v.getVisitStatus().equalsIgnoreCase("p")){					
								HashMap<String, Object> pt = new HashMap<String, Object>();
								
								if(v.getPatient()!=null && v.getPatient().getDateOfBirth()!=null)
								{
								Date s=v.getPatient().getDateOfBirth();
							    Calendar lCal = Calendar.getInstance();
							    lCal.setTime(s);
                                int yr=lCal.get(Calendar.YEAR);
                                int mn=lCal.get(Calendar.MONTH) + 1;
                                int dt=lCal.get(Calendar.DATE);
                                LocalDate today = LocalDate.now();
								LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
								Period p = Period.between(birthday, today);
								pt.put("age", p.getYears());
								String ageFull = HMSUtil.calculateAge(s);
								pt.put("ageFull", ageFull);
								}
								else
								{
									pt.put("age", "");	
								}
								pt.put("visitId", v.getVisitId());
								pt.put("patientId", v.getPatientId());
								pt.put("mmuId", v.getMasMmu().getMmuId());
								pt.put("tokenNo", v.getTokenNo());
								
								if( v.getPatient() != null  && v.getPatient().getPatientName()!=null)
								{
								pt.put("patinetname", v.getPatient().getPatientName());
								}
								else
								{
									pt.put("patinetname", "");
								}
								if(v.getPriority()!=null)
								{
								pt.put("priority", v.getPriority());
								}
								else
								{
									pt.put("priority", "");
								}
								if(v.getPatient() != null && v.getPatient().getMobileNumber()!=null)
								{
								pt.put("mobileNumber", v.getPatient().getMobileNumber());
								}
								else
								{
									pt.put("mobileNumber", "");
								}
								
								
								if(v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getDispensaryFlag()!=null)
								{
									if(	v.getOpdPatientDetails().getDispensaryFlag().equalsIgnoreCase("Y"))
									{	
									pt.put("presFlag", "y");
									}
									else
									{
										pt.put("presFlag", "n");
									}
						         }
								
								if(v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getReerralFlag()!=null)
								{
									if( v.getOpdPatientDetails().getReerralFlag().equalsIgnoreCase("Y"))
									{	
									pt.put("referralFlag", "y");
									}
									else
									{
										pt.put("referralFlag", "n");
									}
								}
								if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getMlcFlag() != null)
								{
								pt.put("mlcFlag", v.getOpdPatientDetails().getMlcFlag());
								}
								else
								{
									pt.put("mlcFlag", "N");	
								}
								
								if(v.getPatient() != null && v.getPatient().getMasAdministrativeSex()!=null && v.getPatient().getMasAdministrativeSex().getAdministrativeSexName() != null)
								{
								pt.put("gender", v.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
								pt.put("genderId", v.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
								}
								else
								{
									pt.put("gender", "");
									pt.put("genderId", "");
								}
								if(v.getPatient().getMobileNumber()!=null)
								{
								pt.put("mobileNumber", v.getPatient().getMobileNumber());
								}
								else
								{
									pt.put("mobileNumber", "");	
								}
								
								
								if(v.getMasDepartment() != null && v.getMasDepartment().getDepartmentName()!=null)
								{
								pt.put("departmentName",v.getMasDepartment().getDepartmentName());
								}
								else
								{
									pt.put("departmentName","");
								}
								if(v.getMasDepartment() != null && v.getMasDepartment().getDepartmentCode()!=null)
								{
									pt.put("departmentCode",v.getMasDepartment().getDepartmentCode());	
								}
								else
								{
									pt.put("departmentCode","");
								}
								if(v.getPatient() != null && v.getPatient().getPatientType()!=null)
								{
									pt.put("patientType",v.getPatient().getPatientType());	
								}
								else
								{
									pt.put("patientType","");
								}
								if(v.getUser() != null && v.getUser().getUserName()!=null)
								{
									pt.put("doctorName",v.getUser().getUserName());	
								}
								else
								{
									pt.put("doctorName","");
								}
								
								c.add(pt);
							//}
						}
						if(c != null && c.size()>0){
							json.put("data", c);
							json.put("count",count);
							json.put("msg", "Visit List  get  sucessfull... ");
							json.put("status", "1");
						}else{
							return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
						}

					}
						
						 catch(Exception e)
						{
							 e.printStackTrace();
							 return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
						}
					}

				/*} else
				{
					return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE ID Not Found\"}";

				}*/

				return json.toString();
			}
		
	}




	@Override
	public String searchPatientWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		/*if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE ID as a  key or value or it is null\"}";
		} else {*/
			
			//MasEmployee checkEmp = od.checkEmp(Long.parseLong(jsondata.get("employeeId").toString()));
			String patinetName=	jsondata.get("patinetName").toString();
			//if (checkEmp != null ) {
				
			     //List<Visit> getvisit = od.getVisit(jsondata);
			Map<String,Object> map = od.getVisit(jsondata);
			int count = (int) map.get("count");
			List<Visit> getvisit = (List<Visit>) map.get("list");
				List<Object[]> getSearchPatinet = od.getSearchPatinet(patinetName);
				if (getvisit.size() == 0)
				{
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}

				else {

					List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
					//Visit v= new Visit();
					
					try
					{
						if(getSearchPatinet != null && getSearchPatinet.size() > 0)
						{
						//if (v.getStatus().equals("w"))
					for (Visit v : getvisit) {
						
							HashMap<String, Object> pt = new HashMap<String, Object>();
						    
							Date s=v.getPatient().getDateOfBirth();
						    Calendar lCal = Calendar.getInstance();
						    lCal.setTime(s);
                            int yr=lCal.get(Calendar.YEAR);
                            int mn=lCal.get(Calendar.MONTH) + 1;
                            int dt=lCal.get(Calendar.DATE);
                            LocalDate today = LocalDate.now();
							//System.out.println("today"+today);//Today's date
							LocalDate birthday = LocalDate.of(yr,mn,dt) ; //Birth date
							//System.out.println("birthday"+birthday);
							Period p1 = Period.between(birthday, today);
							
							pt.put("status",v.getVisitStatus());
							pt.put("tokenNo", v.getTokenNo());
							pt.put("priority", v.getPriority());
							pt.put("patientName", v.getPatient().getPatientName());
							pt.put("age", p1.getYears());
							String ageFull = HMSUtil.calculateAge(s);
							pt.put("ageFull", ageFull);
							pt.put("gender", v.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
							//pt.put("doctorname", checkEmp.getFirstName());
							pt.put("departmentName",v.getMasDepartment().getDepartmentName());
							pt.put("status", v.getVisitStatus());
							pt.put("priority", v.getPriority());
							pt.put("visitId", v.getVisitId());
							pt.put("patientId", v.getPatient().getPatientId());
							
							c.add(pt);
						}
						}
					else
					{
						json.put("msg", "Search Data Not Found");
					}
					
						
					json.put("data", c);
					json.put("count", c.size());
					// json.put("Visit List", getvisit);
					json.put("msg", "Visit List  get  sucessfull... ");
					json.put("status", "1");
					}

				
				catch(Exception e)
				{
				  //System.out.println(e);
				}

				}
			/*}
				else
			{
				return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID Not Found\"}";

			}*/

			return json.toString();
		//}
	
}
	//////////////////////  Get Ideal Weight 01-feb-2019 @KrishnaThakur ///////////////////////////////////////
	
	/*@Override
	public String idealWeight(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.checkIdealWeight(jsondata);
			if (nullbalankvalidation.optString("status").equals("0")) {
				return nullbalankvalidation.toString();
			} 
		else 
		{
			
			List<String> idealWeight = od.getIdealWeight(Long.parseLong(jsondata.get("height").toString()), jsondata.get("age"));
			
			if (idealWeight != null && ! CollectionUtils.isEmpty(idealWeight) ) {
				
				try {
					HashMap<String, Object> iw = new HashMap<String, Object>();
					iw.put("idealWeight", idealWeight.get(0));
					c.add(iw);
					json.put("data", c);
					json.put("msg", "data get sucessfully......");
					json.put("status", 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} 
		else {
				try {
					json.put("msg", "data not found");
					json.put("status", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		}
		return json.toString();
		
	}
	*/



	

////////////////////////////// Get OPD Patient Details,Vitals Details for OPD Main #20/02/2019 /////////////// 
	@Override
	public String OpdPatientDetails(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		Long patientId=null;
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.checkPatientVisit(jsondata);
			if (nullbalankvalidation.optString("status").equals("0")) {
				return nullbalankvalidation.toString();
			} 
		else 
		{
			List<MasAudit> masAuditsList = masterDao.getAuditList();
			List<Visit> getPatientVisit = od.getPatientVisit(Long.parseLong(jsondata.get("visitId").toString()));
			List<OpdPatientDetail> getvitalDetails = od.getVitalRecord(Long.parseLong(jsondata.get("visitId").toString()));
			List<AuditException> getAuditopd = od.getAuditOpdData(Long.parseLong(jsondata.get("visitId").toString()));
			//System.out.print("auditList:"+getAuditopd);
			Long auditId = null;
			String AuditExceptionName ="";
			

			if (getPatientVisit != null && ! CollectionUtils.isEmpty(getPatientVisit) ) {
				
				try {
					for (Visit v : getPatientVisit) {
						//if (v.getVisitStatus().equals("w")||v.getVisitStatus().equals("p")){					
								HashMap<String, Object> pt = new HashMap<String, Object>();
								if(v.getPatient().getDateOfBirth()!=null)
								{
								Date s=v.getPatient().getDateOfBirth();
								Period p=ProjectUtils.getDOB(s);
								pt.put("age", p.getYears());
								String ageFull = HMSUtil.calculateAge(s);
								pt.put("ageFull", ageFull);
								}
								
								pt.put("visitId", v.getVisitId());
								pt.put("patientId", v.getPatientId());
								patientId= v.getPatientId();
								//List<OpdObesityHd> getObsistyDetails = od.getObsisityRecord(patientId);
						
								//String empMedicalCategory = patientRegDao.getMasMedicalCategoryFromDBFunction(patientId,new Date());
								//pt.put("empMedicalCategory",empMedicalCategory);
								
								pt.put("tokenNo", v.getTokenNo());
								if(v.getVisitStatus()!=null && v.getVisitStatus()!="")
								{	
								pt.put("visitStatus", v.getVisitStatus());
								}
																
								if(v.getPatient().getPatientName()!=null)
								{
								pt.put("patientName",v.getPatient().getPatientName());
								}
								else
								{
									pt.put("patientName","");
								}
								
								
								if(v.getPatient()!=null && v.getPatient().getMasAdministrativeSex()!=null && v.getPatient().getMasAdministrativeSex().getAdministrativeSexName()!=null)
								{
								pt.put("gender", v.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
								pt.put("genderId", v.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
								}
								
								
								if(v.getPatient()!=null && v.getPatient().getDateOfBirth()!=null ) {
									String dateOfBirth = HMSUtil.convertDateToStringFormat(v.getPatient().getDateOfBirth(), "dd/MM/yyyy");

									//String dateOfBirth = HMSUtil.getDateWithoutTime1(v.getPatient().getDateOfBirth());
								pt.put("dob", dateOfBirth);
								}
								
								pt.put("pincode", v.getPatient().getPincode());
								pt.put("mobileno", v.getPatient().getMobileNumber());
								
								
								if(v.getOpdPatientDetails()!=null && v.getOpdPatientDetails().getOpdPatientDetailsId()!=null)
								{
								pt.put("opdPatientDetailId",v.getOpdPatientDetails().getOpdPatientDetailsId());
								}
								if(v.getDepartmentId()!=null)
								{
								pt.put("departmentId",v.getDepartmentId());
								}
								if(v.getMasDepartment() != null && v.getMasDepartment().getDepartmentCode()!=null)
								{
									pt.put("departmentCode",v.getMasDepartment().getDepartmentCode());	
								}
								else
								{
									pt.put("departmentCode","");
								}
								
								if(v.getPatient() != null && v.getPatient().getPatientType()!=null)
								{
									pt.put("patientType",v.getPatient().getPatientType());	
								}
								else
								{
									pt.put("patientType","");
								}
								
							if(getvitalDetails!=null && ! CollectionUtils.isEmpty(getvitalDetails))
							{	
								pt.put("height",v.getOpdPatientDetails().getHeight());
								pt.put("idealWeight",v.getOpdPatientDetails().getIdealWeight());
								pt.put("weight",v.getOpdPatientDetails().getWeight());
								String variation = "";
								if(v.getOpdPatientDetails()!=null && v.getOpdPatientDetails().getVaration()!=null) {
								  if ( v.getOpdPatientDetails().getVaration().compareTo(Double.MAX_VALUE) > 0) {
								   variation = "+"+v.getOpdPatientDetails().getVaration();
								  
								  }else {
								   variation = v.getOpdPatientDetails().getVaration()+"";
								  }
								}
								pt.put("varation", variation);
								pt.put("tempature",v.getOpdPatientDetails().getTemperature());
								pt.put("bp",v.getOpdPatientDetails().getBpSystolic());
								pt.put("bp1",v.getOpdPatientDetails().getBpDiastolic());
								pt.put("pulse",v.getOpdPatientDetails().getPulse());
								pt.put("spo2", v.getOpdPatientDetails().getSpo2());
								pt.put("bmi",v.getOpdPatientDetails().getBmi());
								pt.put("rr",v.getOpdPatientDetails().getRr());
																
							}
							
							String  recommendedMedicalAdvice="";
							/*if(v.getOpdPatientDetails()!=null &&  v.getOpdPatientDetails().getRecmndMedAdvice()!=null) {
								recommendedMedicalAdvice =   v.getOpdPatientDetails().getRecmndMedAdvice();
								}
							pt.put("recommendedMedicalAdvice",recommendedMedicalAdvice);*/
							String otherInvestigation = "";
							if (v.getOpdPatientDetails() != null
									&& v.getOpdPatientDetails().getOtherInvestigation() != null) {
								otherInvestigation = v.getOpdPatientDetails().getOtherInvestigation();
							}
							pt.put("otherInvestigation", otherInvestigation);
							String patientSymmptons = "";
							if (v.getOpdPatientDetails() != null
									&& v.getOpdPatientDetails().getPatientSymptoms() != null) {
								patientSymmptons = v.getOpdPatientDetails().getPatientSymptoms();
							}
							pt.put("patientSymmptons", patientSymmptons);
														
							String patientPastMedicalHistory = "";
							if (v.getOpdPatientDetails() != null
									&& v.getOpdPatientDetails().getPastHistory() != null) {
								patientPastMedicalHistory = v.getOpdPatientDetails().getPastHistory();
							}
							pt.put("patientPastMedicalHistory", patientPastMedicalHistory);
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getFollowUpFlag() != null)
							{
							pt.put("followUpFlag", v.getOpdPatientDetails().getFollowUpFlag());
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getFollowUpDays() != null)
							{
							pt.put("followUpDays", v.getOpdPatientDetails().getFollowUpDays());
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getFollowupDate() != null)
							{
								Date s1=v.getOpdPatientDetails().getFollowupDate();
							    Calendar lCal = Calendar.getInstance();
							    lCal.setTime(s1);
                                int yr=lCal.get(Calendar.YEAR);
                                int mn=lCal.get(Calendar.MONTH) + 1;
                                int dt=lCal.get(Calendar.DATE);
                             	pt.put("followUpDate", dt+"/"+mn+"/"+yr);
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getMlcFlag() != null)
							{
							pt.put("mlcFlag", v.getOpdPatientDetails().getMlcFlag());
							}
							else
							{
								pt.put("mlcFlag", "N");	
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getPoliceStation() != null)
							{
							pt.put("policeStation", v.getOpdPatientDetails().getPoliceStation());
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getTreatedAs() != null)
							{
							pt.put("treatedAs", v.getOpdPatientDetails().getTreatedAs());
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getRecmmdMedAdvice() != null)
							{
								String idRemarks=v.getOpdPatientDetails().getRecmmdMedAdvice();
								if(idRemarks!="" && idRemarks.matches("^[0-9].*$"))
								{	
								List<Long> list = Stream.of(idRemarks.split(",")).map(Long::parseLong).collect(Collectors.toList());
								//System.out.println("list of value "+ list);
								Map<String, Object> nextValDetails=od.getMasterDoctorRemarks(idRemarks);
								List<Object[]> listCamp = (List<Object[]>) nextValDetails.get("list");
								ArrayList<String> recommededText = new ArrayList<String>();
								ArrayList<String> recommededId = new ArrayList<String>();
								for (Object[] row : listCamp) {
									if (row[0]!= null) {
										String remaksId=row[0].toString();
										recommededId.add(remaksId);
									}
									if (row[1]!= null) {
										String remarksText=row[1].toString();
										recommededText.add(remarksText);
									}
								 }
									pt.put("doctorRecommededId",recommededId);		
									pt.put("doctorAdditionalNote",recommededText);
								}
								else
								{
									pt.put("doctorRecommededId","");		
									pt.put("doctorAdditionalNote","");
								}
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getPoliceName() != null)
							{
							pt.put("mlcPloiceName", v.getOpdPatientDetails().getPoliceName());
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getDesignation() != null)
							{
							pt.put("mlcDesignation", v.getOpdPatientDetails().getDesignation());
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getIdNumber() != null)
							{
							pt.put("mlcIdNumber", v.getOpdPatientDetails().getIdNumber());
							}
							if (v.getOpdPatientDetails() != null && v.getOpdPatientDetails().getEcgRemarks() != null)
							{
							pt.put("ecgRemarks", v.getOpdPatientDetails().getEcgRemarks());
							}
							if (v.getAuditExceptionT() != null && v.getAuditExceptionT().getRemarks() != null)
							{
							pt.put("auditorRemarks", v.getAuditExceptionT().getRemarks());
							}
							if (v.getAuditExceptionT() != null && v.getAuditExceptionT().getFinalFlag() != null)
							{
							pt.put("audtiorFinalFlag", v.getAuditExceptionT().getFinalFlag());
							}
							List<MasAudit> masAuditsList1 = new ArrayList<>();
							if(getAuditopd != null && ! CollectionUtils.isEmpty(getAuditopd)){
								for (AuditException audit : getAuditopd) {
									
									auditId=audit.getAuditId();
									//System.out.println("AudidId:"+auditId);
								}
								//System.out.println("AudidId:"+auditId);
								masAuditsList1 = od.getUniqueNameID(auditId);
								for(MasAudit masAudit : masAuditsList1){
								//System.out.println("MasauditName:"+masAudit.getAuditName());
								AuditExceptionName =masAudit.getAuditName();
								}
								pt.put("AuditExceptionName", AuditExceptionName);
							}
							String ImgName ="";
							String inPhotoBase64String = "";
							String rootPath = environment.getProperty("server.imageUrl");
							String imageFolderPathDirctory = rootPath ;
							
							//System.out.println(inPhotoBase64String);
							List<PatientDataUpload> patientDataUploads = new ArrayList<>();
							patientDataUploads = od.getPatientActiveImage(v.getPatientId());
							for(PatientDataUpload img :patientDataUploads ){
								//System.out.print("Image Name:"+img.getFileData());
								ImgName = img.getFileData();
							}
							String completePath = imageFolderPathDirctory +"/"+ ImgName;
							//System.out.println("completePath"+completePath);
							File f = new File(completePath);
							if(f.exists()) {
								inPhotoBase64String = encodeFileToBase64Binary(f);
								//System.out.println("inPhotoBase64String:::"+inPhotoBase64String);
							}
							pt.put("patientImage", inPhotoBase64String);
							pt.put("masAuditsList", masAuditsList);
							
							
						
							c.add(pt);
						//}
						
						/*else
						{
							return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
						}*/
	
							json.put("data", c);
							json.put("size", c.size());
							// json.put("Visit List", getvisit);
							json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
							json.put("status", "1");

					}
				}
	
				catch(Exception e)
				{
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}
				} 
			else {
				try {
						json.put("msg", "Visit ID data not found");
						json.put("status", 0);
					}
				    catch (JSONException e)
				    {
						e.printStackTrace();
					}
				}
			}
		
		}
		return json.toString();
	
}


	@Override
	public String familyHistoryDetails(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return "No Data";
	}

	 /////////////////////////////// save opd patinet details created by @KrishnaThakur 27th Feb 2019 ///////////////////

	@Override
	public String saveOpdPatientDetails(HashMap<String, Object> getJsondata, HttpServletRequest request,
			HttpServletResponse response) {
		    String opd = null;
			JSONObject json = new JSONObject();
			JSONArray newForm=new JSONArray(getJsondata.get("opdMainData").toString());
			JSONObject jsondata = (JSONObject) newForm.get(0);
			//System.out.println("Payload Opd Data "+jsondata.toString());
			if(jsondata.get("visitId").equals(""))
			{
				return "visitId cannot be blank";
			}
			if(jsondata.get("patientId").equals(""))
			{
				return "patientId cannot be blank";
			}
		try {
			if (null!=jsondata)
			{
				
				
					 opd = od.saveOpdPatientDetails(getJsondata);
			
					 if(jsondata.get("precriptionDtValue")!=null && jsondata.get("precriptionDtValue")!="") {
					//String precriptionDtIdVal = payload.get("precriptionDtIdVal").toString();
					//precriptionDtIdVal = getReplaceString(precriptionDtIdVal);
					
						 updateOpdCurrentMedication(getJsondata,null,null);
				} 
                  
                   //if (opd != null ||oph != null  && opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) {
				if (opd != null && opd.equalsIgnoreCase("Successfully saved")) {
						json.put("msg", "Opd Patinet Details Saved successfully ");
						json.put("status", "1");
					} else if (opd != null && opd.equalsIgnoreCase("403")) {
						json.put("msg", " you are not authorized for this activity ");
						json.put("status", "0");
					} else {
						json.put("msg", opd);
						json.put("status", "0");
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
	public String saveOpdTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String opdT = null;
		
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		OpdTemplate opdTemp = new OpdTemplate();
		//OpdTemplateInvestigation opdTempInv = new OpdTemplateInvestigation();
		
		try {
			
		if (!jsondata.isEmpty())
		{
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		
		//opdTemp.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
		opdTemp.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("doctorId"))));
		//opdTemp.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
		opdTemp.setTemplateCode(String.valueOf(jsondata.get("templateCode")));
		opdTemp.setTemplateName(String.valueOf(jsondata.get("templateName")));
		opdTemp.setTemplateType(String.valueOf(jsondata.get("templateType")));
		opdTemp.setLastChgDate(ourJavaTimestampObject);
		opdTemp.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId").toString())));

		//opdTempInv.setStatus(status);
		
		List<HashMap<String, Object>> listofOpdTemp = (List<HashMap<String, Object>>) jsondata.get("listofInvestigationTemplate");
		    List<OpdTemplateInvestigation>  opdInvestigationList= new ArrayList<>();
		   
		for (HashMap<String, Object> singleopd: listofOpdTemp)
		{
			OpdTemplateInvestigation ob1=new OpdTemplateInvestigation();
			if(singleopd.get("investigationId")!=null && singleopd.get("investigationId")!="")
			{
			ob1.setInvestigationId(Long.valueOf(singleopd.get("investigationId").toString()));
			ob1.setLastChgDate(ourJavaTimestampObject);
			ob1.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId").toString())));

			opdInvestigationList.add(ob1);	
			}
			
		}
				
		 opdT = od.opdTemplatenewMethod(opdTemp,opdInvestigationList);
		 
		  if (opdT != null  && opdT.equalsIgnoreCase("200"))
		  {
				json.put("msg", "Opd Template Details Saved successfully ");
				json.put("status", "1");
		  } else if (opdT != null && opdT.equalsIgnoreCase("403"))
		  {
				json.put("msg", " you are not authorized for this activity ");
				json.put("status", "0");
		   } else
		   {		
			   json.put("msg", opdT);
				json.put("status", "0");
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
	public String updateOpdInvestigationTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String opdT = null;
		
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		OpdTemplate opdTemp = new OpdTemplate();
		//OpdTemplateInvestigation opdTempInv = new OpdTemplateInvestigation();
		
		try {
			
		if (!jsondata.isEmpty())
		{
		//opdTemp.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
		 
		opdTemp.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("doctorId"))));
		//opdTemp.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
		opdTemp.setTemplateCode(String.valueOf(jsondata.get("templateCode")));
		opdTemp.setTemplateName(String.valueOf(jsondata.get("templateName")));
		opdTemp.setTemplateType(String.valueOf(jsondata.get("templateType")));
		
		opdTemp.setTemplateId(Long.parseLong(String.valueOf(jsondata.get("templateId"))));
		//opdTempInv.setStatus(status);
		
		List<HashMap<String, Object>> listofOpdTemp = (List<HashMap<String, Object>>) jsondata.get("listofInvestigationTemplate");
		    List<OpdTemplateInvestigation>  opdInvestigationList= new ArrayList<>();
		   
		for (HashMap<String, Object> singleopd: listofOpdTemp)
		{
			OpdTemplateInvestigation ob1=new OpdTemplateInvestigation();
			if(singleopd.get("investigationId")!=null && singleopd.get("investigationId")!="")
			{
			ob1.setInvestigationId(Long.valueOf(singleopd.get("investigationId").toString()));
			ob1.setTemplateId(Long.parseLong(String.valueOf(jsondata.get("templateId"))));
			opdInvestigationList.add(ob1);	
			}
			
		}
				
		 opdT = od.opdupdateInvestigationTemplate(opdTemp,opdInvestigationList);
		 
		  if (opdT != null  && opdT.equalsIgnoreCase("200"))
		  {
				json.put("msg", "Opd Template Details Saved successfully ");
				json.put("status", "1");
		  } else if (opdT != null && opdT.equalsIgnoreCase("403"))
		  {
				json.put("msg", " you are not authorized for this activity ");
				json.put("status", "0");
		   } else
		   {		
			   json.put("msg", opdT);
				json.put("status", "0");
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
	public String deleteOpdInvestigationTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		if (jsondata != null) {

			String dtObj = od.deleteInvestigationTemplate(jsondata);
			if (dtObj != null && dtObj.equalsIgnoreCase("success")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Deleted Successfully");

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Record Not Found");
			}
		}
		return jsonObj.toString();
		
	}



///////////////////////// add opd treatment Template by @krishna /////////////////////////
	@Override
	public String saveOpdTreatementTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String opdT = null;
		
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		OpdTemplate opdTemp = new OpdTemplate();
		//OpdTemplateInvestigation opdTempInv = new OpdTemplateInvestigation();
		
		try {
			
		if (!jsondata.isEmpty())
		{
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
				
		//opdTemp.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
		opdTemp.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("doctorId"))));
		//opdTemp.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
		opdTemp.setTemplateCode(String.valueOf(jsondata.get("templateCode")));
		opdTemp.setTemplateName(String.valueOf(jsondata.get("templateName")));
		opdTemp.setTemplateType(String.valueOf(jsondata.get("templateType")));
		opdTemp.setLastChgDate(ourJavaTimestampObject);
		opdTemp.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId").toString())));

		//opdTempInv.setStatus(status);
		
		List<HashMap<String, Object>> listofOpdTemp = (List<HashMap<String, Object>>) jsondata.get("listofTreatmentTemplate");
		    List<OpdTemplateTreatment>  opdTreatmentTempList= new ArrayList<>();
		   
		for (HashMap<String, Object> singleopd: listofOpdTemp)
		{
			OpdTemplateTreatment ob=new OpdTemplateTreatment();
			ob.setItemId(Long.valueOf(singleopd.get("itemId").toString()));
			ob.setDosage(String.valueOf(singleopd.get("dosage")));
			if(singleopd.get("frequencyId")!=null && singleopd.get("frequencyId")!="")
			{
			ob.setFrequencyId(Long.valueOf(singleopd.get("frequencyId").toString()));
			}
			else
			{
			ob.setFrequencyId((long) 1);	
			}
			if(singleopd.get("noOfDays")!=null && singleopd.get("noOfDays")!="")
			{
			ob.setNoofdays(Long.valueOf(singleopd.get("noOfDays").toString()));
			}
			else
			{
				ob.setNoofdays((long) 1);	
			}
			if(singleopd.get("total")!=null && singleopd.get("total")!="")
			{
			ob.setTotal(Long.valueOf(singleopd.get("total").toString()));
			}
			else
			{
			ob.setTotal((long) 0);	
			}
			ob.setInstruction(String.valueOf(singleopd.get("instruction")));
			ob.setLastChgDate(ourJavaTimestampObject);
			ob.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId").toString())));

			opdTreatmentTempList.add(ob);			
		}
				
		 opdT = od.saveTreatTemplatenewMethod(opdTemp,opdTreatmentTempList);
		 
		  if (opdT != null  && opdT.equalsIgnoreCase("200"))
		  {
				json.put("msg", "Opd Template Details Saved successfully ");
				json.put("status", "1");
		  } else if (opdT != null && opdT.equalsIgnoreCase("403"))
		  {
				json.put("msg", " you are not authorized for this activity ");
				json.put("status", "0");
		   } else
		   {		
			   json.put("msg", opdT);
				json.put("status", "0");
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
	public String saveInvestigationDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
	     String opdInvesti = null;
		
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		DgOrderhd orderhd = new DgOrderhd();
		//OpdTemplateInvestigation opdTempInv = new OpdTemplateInvestigation();
		
		try {
			
		if (!jsondata.isEmpty())
		{
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			orderhd.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
			orderhd.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
			orderhd.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
			orderhd.setMmuId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
			//orderhd.setOrderDate(ourJavaTimestampObject);
			orderhd.setOrderStatus(String.valueOf(jsondata.get("orderStatus")));
			
			
			List<HashMap<String, Object>> listofOpdInvest = (List<HashMap<String, Object>>) jsondata.get("listofInvestigation");
			    List<DgOrderdt>  dgorderdt= new ArrayList<>();
			   
			for (HashMap<String, Object> singleopd: listofOpdInvest)
			{
				DgOrderdt ob1=new DgOrderdt();
				ob1.setInvestigationId(Long.valueOf(singleopd.get("investigationId").toString()));
				ob1.setLabMark(String.valueOf(singleopd.get("labMark")));
				//ob1.setUrgent(String.valueOf(singleopd.get("urgent")));
				dgorderdt.add(ob1);			
			}
					
			opdInvesti = od.saveOpdInvestigation(orderhd,dgorderdt);
		 
		  if (opdInvesti != null  && opdInvesti.equalsIgnoreCase("200"))
		  {
				json.put("msg", "Opd Template Details Saved successfully ");
				json.put("status", "1");
		  } else if (opdInvesti != null && opdInvesti.equalsIgnoreCase("403"))
		  {
				json.put("msg", " you are not authorized for this activity ");
				json.put("status", "0");
		   } else
		   {		
			   json.put("msg", opdInvesti);
				json.put("status", "0");
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
	public String saveReferalDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		//System.out.println(jsondata.toString());
		OpdPatientDetail opdPDt = new OpdPatientDetail();
		String result = od.saveOpdPatientDetails(jsondata);
		JSONObject json = new JSONObject();
		json.put("msg", result);
		return json.toString();
	}
	
//////////////////////////////// OPD Reports ///////////////////////////////////////////////////////
	
	@Override
	public String getOpdReportsDetailsbyServiceNo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata.get("serviceNo") == null || jsondata.get("serviceNo").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain Service No as a  key or value or it is null\"}";
			
		}
				
		else {
			Map<String, Object> map = od.getOpdReportsDetailsbyServiceNo(jsondata);
			List<Patient> getPatinetDetails = (List<Patient>) map.get("list");
			String error = (String) map.get("errorInServiceNo");
			List<HashMap<String, Object>> p = new ArrayList<HashMap<String, Object>>();
			try {
				if (getPatinetDetails != null && getPatinetDetails.size() > 0) {
					for (Patient ptDetails : getPatinetDetails) {
						HashMap<String, Object> pt = new HashMap<String, Object>();
						pt.put("patinetName", ptDetails.getPatientName());
						pt.put("patientId", ptDetails.getPatientId());
						pt.put("patinetUHIDNo", ptDetails.getUhidNo());
						p.add(pt);
					}
					if (p != null && p.size() > 0) {
						json.put("data", p);
						json.put("count", p.size());
						json.put("msg", "Patinet List  get  sucessfull... ");
						json.put("status", "1");
					}

				} else {
					if (!error.isEmpty() && error.equalsIgnoreCase("errorInServiceNo")) {
						json.put("msg", "No Record found.");
						json.put("status", "2");
					} else {
						json.put("msg", "EHR does not exist");
						json.put("status", "0");
					}

				}
			} catch (Exception e) {
				//System.out.println(e);
				e.printStackTrace();
			}

			return json.toString();
		}
	
	}

    
	@Override
	public String getOpdReportsDetailsbyPatinetId(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata.get("patientId") == null || jsondata.get("patientId").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain Patient Id as a  key or value or it is null\"}";
			
		}
				
		else {
					
			Map<String,Object> map = od.getOpdReportsDetailsbyPatinetId(jsondata);
			List<Visit> getPatinetDetails = (List<Visit>) map.get("list");
			List<HashMap<String, Object>> p = new ArrayList<HashMap<String, Object>>();
			try
			{
				
			for (Visit ptDetails : getPatinetDetails) {
				HashMap<String, Object> pt = new HashMap<String, Object>();
				
				Timestamp vd=ptDetails.getVisitDate();
				Calendar lCal = Calendar.getInstance();
			    lCal.setTime(vd);
                int yr=lCal.get(Calendar.YEAR);
                int mn=lCal.get(Calendar.MONTH) + 1;
                int dt=lCal.get(Calendar.DATE);
                LocalDate visitDate = LocalDate.of(yr,mn,dt) ; //Birth date
                pt.put("visitDate",HMSUtil.convertDateToStringFormat(ptDetails.getVisitDate(), "dd/MM/yyyy"));
				pt.put("visitNo",ptDetails.getVisitId());
				pt.put("tokenNo",ptDetails.getTokenNo());
				if(ptDetails.getMasDepartment()!=null &&ptDetails.getMasDepartment().getDepartmentName()!=null)
				{	
				pt.put("departmentNo",ptDetails.getMasDepartment().getDepartmentName());
				}
				else
				{
					pt.put("departmentNo","");	
				}
				p.add(pt);
				
		}
			if(p != null && p.size()>0){
				json.put("data", p);
				json.put("count", p.size());
				json.put("msg", "Patinet List  get  sucessfull... ");
				json.put("status", "1");
			}else{
				return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
			}
			}
			catch(Exception e)
			{
			  //System.out.println(e);
			}

			return json.toString();
		}
	
	}
	
	@Override
	public String getOpdReportsDetailsbyVisitId(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata.get("visitId") == null || jsondata.get("visitId").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain Visit Id as a  key or value or it is null\"}";
			
		}
				
		else {
					
			Map<String,Object> map = od.getOpdReportsDetailsbyPatinetId(jsondata);
			List<Visit> getPatinetDetails = (List<Visit>) map.get("list");
			List<HashMap<String, Object>> p = new ArrayList<HashMap<String, Object>>();
			try
			{
				
			for (Visit ptDetails : getPatinetDetails) {
				HashMap<String, Object> pt = new HashMap<String, Object>();
				
				Timestamp vd=ptDetails.getVisitDate();
				Calendar lCal = Calendar.getInstance();
			    lCal.setTime(vd);
                int yr=lCal.get(Calendar.YEAR);
                int mn=lCal.get(Calendar.MONTH) + 1;
                int dt=lCal.get(Calendar.DATE);
                LocalDate visitDate = LocalDate.of(yr,mn,dt) ; //Birth date
				pt.put("visitDate",visitDate);
				pt.put("visitNo",ptDetails.getVisitId());
				pt.put("patientName",ptDetails.getPatient().getPatientName());
				if(ptDetails.getUser()!= null && ptDetails.getUser().getUserName()!= null)
				{
				pt.put("doctor",ptDetails.getUser().getUserName());
				}
				Date s=ptDetails.getPatient().getDateOfBirth();
				Period dob=ProjectUtils.getDOB(s);
				pt.put("age",dob);
				if(ptDetails.getOpdPatientDetails()!= null && ptDetails.getOpdPatientDetails().getIcdDiagnosis()!= null)
				{
				pt.put("icdDiagnosis",ptDetails.getOpdPatientDetails().getIcdDiagnosis());
				}
				if(ptDetails.getMasDepartment()!= null && ptDetails.getMasDepartment().getDepartmentName()!= null)
				{
				pt.put("departmentNo",ptDetails.getMasDepartment().getDepartmentName());
				}
			
				//pt.put("prescrptionHdId",ptDetails.getPatinetPrescriptionHd().getPrescriptionHdId());
				
				
				p.add(pt);
				
		}
			if(p != null && p.size()>0){
				json.put("data", p);
				json.put("count", p.size());
				json.put("msg", "OpdData List  get  sucessfull... ");
				json.put("status", "1");
			}else{
				return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
			}
			}
			catch(Exception e)
			{
			  //System.out.println(e);
			}

			return json.toString();
		}
	
	}
////////////////////////////////OPD Previous Visit Record ///////////////////////////////////////////////////////
	@Override
	public String getOpdPreviousVisitRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getVisitRecord = od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString()),jsondata);
				if (getVisitRecord.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					    List<Object[]> list = (List<Object[]>) getVisitRecord.get("list");
					    String count = String.valueOf(getVisitRecord.get("count"));
						String visistDate = null;
						String icdDiagnosis = null;
						String patientSymptoms = null;
						String mmuName=null;
						String visitId="";
						String departmentName="";
						String doctorName="";
						/*String orderDate = "";
						Long visitId = null;
						String otherInvestigation = "";
						Long departId = null;
						Long hospitalId = null;
						Long dgOrderDtId = null;*/
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								if (row[0] != null) {
									Timestamp vd=(Timestamp) row[0];
									Calendar lCal = Calendar.getInstance();
								    lCal.setTime(vd);
					                int yr=lCal.get(Calendar.YEAR);
					                int mn=lCal.get(Calendar.MONTH) + 1;
					                int dt=lCal.get(Calendar.DATE);
					                
					               
					                LocalDate visitDate = LocalDate.of(yr,mn,dt) ; //Birth date
					                
					                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
					                String text = visitDate.format(formatters);
					                
					                visistDate =text;
									//pt.put("visitDate",visitDate);
									
													
								}
								if (row[1] != null) {
									icdDiagnosis =row[1].toString();
								}
								
								if (row[2] != null) {
									patientSymptoms =row[2].toString();
								}
								if (row[3] != null && row[3] != "") {
									visitId =row[3].toString();
								}
								if (row[4] != null) {
									departmentName =row[4].toString();
								}
								if (row[5] != null) {
									doctorName =row[5].toString();
								}
								if (row[6] != null) {
									mmuName =row[6].toString();
								}
								
								
								if(visistDate!=null)
								{
								pt.put("visistDate", visistDate);
								}
								else
								{
									pt.put("visistDate", "");
								}
								if(icdDiagnosis!=null)
								{
								pt.put("icdDiagnosis", icdDiagnosis);
								}
								else
								{
									pt.put("icdDiagnosis", "");	
								}
								if(patientSymptoms!=null)
								{
								pt.put("patientSymptoms", patientSymptoms);
								}
								else
								{
									pt.put("patientSymptoms", "");	
								}
								
								if(visitId!=null)
								{
								pt.put("visitId", visitId);
								}
								else
								{
									pt.put("visitId", "");	
								}
								if(departmentName!=null && departmentName!=null )
								{
								 	
								pt.put("departmentName", departmentName);
								}
								else
								{
									pt.put("departmentName", "");	
								}
								if(doctorName!=null)
								{
								pt.put("doctorName", doctorName);
								}
								else
								{
									pt.put("doctorName", "");	
								}
								if(mmuName!=null)
								{
								pt.put("mmuName", mmuName);
								}
								else
								{
									pt.put("mmuName", "");	
								}
								
								
					
						c.add(pt);
						json.put("data", c);
						json.put("count", count);
						json.put("msg", "data get sucessfully......");
						json.put("status", 1);
							}
					 }catch (Exception e) {
							e.printStackTrace();
							return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
					}
			
		}
		return json.toString();
	}
	

	@Override
	public String getOpdPreviousVitalRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			//List<Visit> getvisit = od.getVisit(jsondata);
			Map<String,Object> map = od.getPreviousVitalRecord(jsondata);
			int count = (int) map.get("count");
			List<OpdPatientDetail> getvitalDetails = (List<OpdPatientDetail>) map.get("list");
			if (getvitalDetails.size() == 0) {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		
		else 
		{
			if (getvitalDetails != null && ! CollectionUtils.isEmpty(getvitalDetails) ) {
				
				try {
					for (OpdPatientDetail v : getvitalDetails) {
						//if (v.getVisitStatus().equals("w")||v.getVisitStatus().equals("p")){					
								HashMap<String, Object> pt = new HashMap<String, Object>();
								String bpS,bpD=null;
								if(v.getHeight()!=null)
								{	
								pt.put("height",v.getHeight());
								}
								else
								{
									pt.put("height","");	
								}
								if(v.getIdealWeight()!=null)
								{	
								pt.put("idealWeight",v.getIdealWeight());
								}
								else
								{
								pt.put("idealWeight","");	
								}
								if(v.getWeight()!=null)
								{	
								pt.put("weight",v.getWeight());
								}
								else
								{
									pt.put("weight","");	
								}
								if(v.getVaration()!=null)
								{	
								pt.put("varation", v.getVaration());
								}
								else
								{
									pt.put("varation","");	
								}
								if(v.getTemperature()!=null)
								{	
								pt.put("tempature",v.getTemperature());
								}
								else
								{
									pt.put("tempature","");	
								}
								if(v.getBpSystolic()!=null)
								{	
								 bpS=v.getBpSystolic();
								}
								else
								{
									bpS="";	
								}
								if(v.getBpDiastolic()!=null)
								{	
								 bpD=v.getBpDiastolic();
								}
								else
								{
									bpD="";	
								}
								
								if(v.getBpDiastolic()!=null && v.getBpSystolic()!=null)
								{
									String bp=bpS+"/"+bpD;
									pt.put("bp",bp);
								}
																
								else
								{
									pt.put("bp","");	
								}
								if(v.getPulse()!=null)
								{	
								pt.put("pulse",v.getPulse());
								}
								else
								{
									pt.put("pulse","");	
								}
								if( v.getSpo2()!=null)
								{	
								pt.put("spo2", v.getSpo2());
								}
								else
								{
									pt.put("spo2", "");	
								}
								if(v.getBmi()!=null)
								{	
								pt.put("bmi",v.getBmi());
								}
								else
								{
									pt.put("bmi","");	
								}
								if(v.getRr()!=null)
								{	
								pt.put("rr",v.getRr());
								}
								else
								{
									pt.put("rr","");	
								}
								if(v.getOpdDate()!=null)
								{
								Timestamp vd=(v.getOpdDate());
								Calendar lCal = Calendar.getInstance();
							    lCal.setTime(vd);
				                int yr=lCal.get(Calendar.YEAR);
				                int mn=lCal.get(Calendar.MONTH) + 1;
				                int dt=lCal.get(Calendar.DATE);
				                
				               
				                LocalDate visitDate = LocalDate.of(yr,mn,dt) ; //Birth date
				                
				                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
				                String text = visitDate.format(formatters);
				                
				                String visistDate = text;
				                pt.put("visitdate",visistDate);
								}
								else
								{
								pt.put("visitdate","");	
								}
								if(v.getEcgRemarks()!=null)
								{
									pt.put("ecgRemarks",v.getEcgRemarks());	
								}
								else
								{
									pt.put("ecgRemarks","");
								}
								if(v.getVisitId()!=null)
								{
									pt.put("visitId",v.getVisitId());	
								}
								else
								{
									pt.put("visitId","");
								}
								
																
								c.add(pt);
					
					}
							json.put("data", c);
							json.put("count", count);
							// json.put("Visit List", getvisit);
							json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
							json.put("status", "1");

					}
				
	
				catch(Exception e)
				{
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}
				} 
			else {
				try {
						json.put("msg", "Patient ID data not found");
						json.put("status", 0);
					}
				    catch (JSONException e)
				    {
						e.printStackTrace();
					}
				}
			}
		
		}
		return json.toString();
	
	}
	
	
	@Override
	public String getPreviousInvestigationAndResult(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			Map<String, Object> data = od.getPreviousDgMasInvestigationsAndResult(Long.parseLong(payload.get("patientId").toString()),payload);
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if (data.isEmpty()) {
				obj.put("count", 0);
				obj.put("data", new JSONArray());
			} else {
				List<Object[]> list = (List<Object[]>) data.get("list");
				String count = String.valueOf(data.get("count"));
				if (list.isEmpty()) {
					obj.put("count", count);
					obj.put("data", new JSONArray());
				} else {
					List<Map<String, Object>> dataResult = new ArrayList<>();
					for (Object[] row : list) {
						Map<String, Object> pt = new HashMap<>();
						Long investigationId = null;
						String resultNo = null;
						String investigationName = "";
						String orderDate = "";
						String result = "";
						String rangeValue = "";
						String uomName="";
						String mainChargeCode="";
						String enterBy="";
						String verifiedBy="";
						String ridcId="";
						String subInvestigationName="";
						if (row[0] != null) {
							investigationId = Long.parseLong(row[0].toString());
						}
						if (row[1] != null) {
							investigationName = row[1].toString();
						}

						if (row[2] != null) {
							resultNo =row[2].toString();
						}
						else
						{
							resultNo="";
						}

						if (row[3] != null) {
							orderDate = row[3].toString();
							Date dd1 = HMSUtil.dateFormatteryyyymmdd(orderDate);
							// Date dd1 =HMSUtil.convertStringDateToUtilDateForDatabase(orderDate);
							//orderDate = HMSUtil.getDateWithoutTime(dd1);
							orderDate = HMSUtil.convertDateToStringFormat(dd1, "dd/MM/yyyy");
						}
						else {
							orderDate = "";
						}
						if (row[4] != null) {
							result = row[4].toString();
						}
						if (row[5] != null) {
							rangeValue = row[5].toString();
						}

						if (row[6] != null) {
							uomName = row[6].toString();
						} else {
							uomName = "";
						}
						if (row[7] != null) {
							mainChargeCode = row[7].toString();
						} else {
							mainChargeCode = "";
						}
						if (row[8] != null) {
							enterBy = row[8].toString();
						} else {
							enterBy = "";
						}
						if (row[9] != null) {
							ridcId = row[9].toString();
						} else {
							ridcId = "";
						}
						
						 if (row[10] != null) {
							 verifiedBy = row[10].toString();
							 } 
						 else { 
							 verifiedBy =""; 
						}
						
						 if (row[11] != null) {
							 subInvestigationName = row[11].toString();
							 } 
						 else { 
							 subInvestigationName =""; 
						}
						pt.put("investigationName", investigationName);
						pt.put("investigationId", investigationId);
						pt.put("orderDate", orderDate);
						pt.put("result", result);
						pt.put("range", rangeValue);
						pt.put("uomName", uomName);
						pt.put("mainChargeCode",mainChargeCode);
						pt.put("enterBy",enterBy);
						pt.put("ridcId",ridcId);
						pt.put("verifiedBy",verifiedBy);
						pt.put("subInvestigationName", subInvestigationName);
						dataResult.add(pt);

					}
					obj.put("count", count);
					obj.put("data", dataResult);
					return obj.toString();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		obj.put("count", 0);
		obj.put("data", new JSONArray());
		return obj.toString();
	}

	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getObesityWaitingList(HashMap<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		map = od.getObesityWaitingList(jsondata);
		JSONObject json = new JSONObject();
		if (map.isEmpty()) {
			json.put("status", "0");
			json.put("msg", "No Record found");
		} else {
			json.put("status", "1");
			json.put("msg", "Obesity Waiting List got successfull");
			json.put("count", map.get("count"));
			List<HashMap<String, Object>> patientObesityList = new ArrayList<>();
			
			json.put("patientObesityList", patientObesityList);
		}
		return json.toString();
	}

	@Override
	public String getObesityDetails(HashMap<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<>();
		//map = od.getObesityDetails(jsondata);

		JSONObject json = new JSONObject();
		/*List<OpdObesityHd> obesityList1 = (List<OpdObesityHd>) map.get("obesityList");
		List<OpdObesityDt> obesityDetailList1 = (List<OpdObesityDt>) map.get("obesityDetailList");
		List<Map<String, Object>> obesityList = new ArrayList<>();
		List<Map<String, Object>> obsesityDetailList = new ArrayList<>();
		for (OpdObesityHd list1 : obesityList1) {
			Map<String, Object> map1 = new HashMap<>();
			String serviceNo = "", patientName="", age="", gender="",genderId="",dob="";
			Date date = null;
			if(list1.getPatient() != null) {
				patientName = HMSUtil.convertNullToEmptyString(list1.getPatient().getPatientName());
				if(list1.getPatient().getDateOfBirth() != null) {
					date = list1.getPatient().getDateOfBirth();
					Period p = ProjectUtils.getDOB(date);
					age = String.valueOf(p.getYears());
					dob = HMSUtil.calculateAge(date);
				}
				if(list1.getPatient().getMasAdministrativeSex() != null) {
					gender = list1.getPatient().getMasAdministrativeSex().getAdministrativeSexName();
					genderId = String.valueOf(list1.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
				}
			}
			map1.put("serviceNo", serviceNo);
			map1.put("patientName", patientName);	
			map1.put("age", age);
			map1.put("dob", dob);
			map1.put("gender", gender);
			map1.put("header_id", list1.getObesityHdId());
			map1.put("gender_id", genderId);
			obesityList.add(map1);
		}
		for (OpdObesityDt list2 : obesityDetailList1) {
			Map<String, Object> map2 = new HashMap<>();
			map2.put("headerId", list2.getOpdObesityHd().getObesityHdId());
			map2.put("date", HMSUtil.convertNullToEmptyString(HMSUtil.changeDateToddMMyyyy(list2.getObesityDate())));
			map2.put("month", HMSUtil.convertNullToEmptyString(list2.getMonth()));
			map2.put("height", HMSUtil.convertNullToEmptyString(list2.getHeight()));
			map2.put("weight", HMSUtil.convertNullToEmptyString(list2.getWeight()));
			map2.put("idealWeight", HMSUtil.convertNullToEmptyString(list2.getIdealWeight()));
			String variation = "";
			if(list2.getVariation() != null) {
				if (list2.getVariation().compareTo(BigDecimal.ZERO) > 0) {
					variation = "+"+list2.getVariation();
				}else {
					variation = list2.getVariation()+"";
				}
			}			
			map2.put("variation", variation);
			map2.put("bmi", HMSUtil.convertNullToEmptyString(list2.getBmi()));
			obsesityDetailList.add(map2);
		}
		json.put("obesityList", obesityList);
		json.put("obsesityDetailList", obsesityDetailList);*/
		return json.toString();
	}

	@Override
	public String idealWeight(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.checkIdealWeight(jsondata);
			if (nullbalankvalidation.optString("status").equals("0")) {
				return nullbalankvalidation.toString();
			} else {
				List<String> idealWeight = od.getIdealWeight(Long.parseLong(jsondata.get("height").toString()),
						jsondata.get("age"),Long.parseLong(jsondata.get("genderId").toString()));
				if (idealWeight != null && !CollectionUtils.isEmpty(idealWeight)) {
					try {
						HashMap<String, Object> iw = new HashMap<String, Object>();
						iw.put("idealWeight", idealWeight.get(0));
						c.add(iw);
						json.put("data", c);
						json.put("msg", "data get sucessfully......");
						json.put("status", 1);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					try {
						json.put("msg", "data not found");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return json.toString();
	}

	public String saveObesityDetails(HashMap<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject obj = new JSONObject();
		String msg = od.saveObesityDetails(jsondata);
		obj.put("msg", msg);
		return obj.toString();
	}

	@Override
	public String referredPatientList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = od.referredPatientList(jsondata);
		JSONObject obj = new JSONObject();
		List<Map<String, Object>> headerList = new ArrayList<>();
		if (!map.isEmpty()) {
			List<ReferralPatientHd> patientList = (List<ReferralPatientHd>) map.get("referredPatientList");
			if (patientList.size() > 0) {
				for (ReferralPatientHd list : patientList) {
					Map<String, Object> jsonData = new HashMap<>();
					String service_no = "", patient_name = "", gender = "", rank = "", mobileNo = "",
							referredHospital = "", id = "", count = "", referredDate = "",referralType="";
					Date date = null;
					if (list.getPatient() != null) {
						patient_name = HMSUtil.convertNullToEmptyString((String) list.getPatient().getPatientName());
						String format = "dd-MM-yyyy";
						/*
						 * date =
						 * HMSUtil.convertDateToStringFormat((Date)list.getPatient().getDateOfBirth(),
						 * format); System.out.println("date is "+date);
						 */
						date = (Date) list.getPatient().getDateOfBirth();
						if (list.getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString((String) list.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}
						
						mobileNo = HMSUtil.convertNullToEmptyString(list.getPatient().getMobileNumber());
						referredDate = HMSUtil.changeDateToddMMyyyy(list.getReferralIniDate());
						//System.out.println("date is " + referredDate);
						// referredDate = (Date) list.getReferralIniDate();
						id = list.getRefrealHdId() + "";

					}
					if (list.getMasImpanneledHospital() != null) {
						referredHospital = (String) list.getMasImpanneledHospital().getImpanneledHospitalName();
					}
					/*if(list.getOpdPatientDetail() != null && list.getOpdPatientDetail().getVisit() != null && list.getOpdPatientDetail().getVisit().getMasAppointmentType() != null) {
						referralType = list.getOpdPatientDetail().getVisit().getMasAppointmentType().getAppointmentTypeCode();
					}*/

					jsonData.put("id", id);
					jsonData.put("service_no", service_no);
					jsonData.put("patient_name", patient_name);
					if (date == null) {
						jsonData.put("age", "");
					} else {
						String age = HMSUtil.calculateAge(date);
						jsonData.put("age", age);

					}
					jsonData.put("gender", gender);
					jsonData.put("rank", rank);
					jsonData.put("mobile_no", mobileNo);
					jsonData.put("referred_hospital", referredHospital);
					jsonData.put("referral_date", referredDate);
					jsonData.put("referralType", referralType);
					/*
					 * if(referredDate == null) { jsonData.put("referral_date", referredDate); }else
					 * { jsonData.put("referral_date", referredDate); }
					 */

					headerList.add(jsonData);
				}
				obj.put("Status", "0");
				obj.put("msg", "Referral List got successfull");
				obj.put("referral_list", headerList);
				obj.put("count", map.get("count"));
			}
		} else {
			obj.put("status", "0");
			obj.put("msg", "No Record Found");
			obj.put("referral_list", new JSONArray());
			obj.put("count", 0);
		}
		return obj.toString();
	}

	@Override
	public String referredPatientDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = od.referredPatientDetail(jsondata);
		JSONObject obj = new JSONObject();
		List<Map<String, Object>> headerList = new ArrayList<>();
		List<Map<String, Object>> detailList = new ArrayList<>();
		if (!map.isEmpty()) {
			List<ReferralPatientHd> headerInfo = (List<ReferralPatientHd>) map.get("referredHeaderList");
			List<ReferralPatientDt> detailInfo = (List<ReferralPatientDt>) map.get("referredDetailList");

			if (headerInfo.size() > 0) {
				for (ReferralPatientHd list : headerInfo) {
					Map<String, Object> jsonData = new HashMap<>();
					String service_no = "", patient_name = "", gender = "", rank = "", mobileNo = "", patientId = "",relation="";
					Date date = null;
					if (list.getPatient() != null) {
						patientId = list.getPatient().getPatientId() + "";
						patient_name = HMSUtil.convertNullToEmptyString(list.getPatient().getPatientName());
						if(list.getPatient().getDateOfBirth() != null) {
							date = (Date) list.getPatient().getDateOfBirth();
						}						
						if (list.getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString(list.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}
						
						if(list.getPatient().getMobileNumber() != null) {
							mobileNo = HMSUtil.convertNullToEmptyString(list.getPatient().getMobileNumber());
						}
						
					}
					jsonData.put("id", list.getRefrealHdId());
					jsonData.put("service_no", service_no);
					jsonData.put("patient_id", patientId);
					jsonData.put("patient_name", patient_name);
					jsonData.put("relation", relation);
					if (date == null) {
						jsonData.put("age", "");
					} else {
						String age = HMSUtil.calculateAge(date);
						jsonData.put("age", age);
					}
					jsonData.put("gender", gender);
					jsonData.put("rank", rank);
					jsonData.put("mobile_no", mobileNo);
					headerList.add(jsonData);
				}

				obj.put("header_list", headerList);
			} else {
				obj.put("header_list", new JSONArray());
			}

			if (detailInfo.size() > 0) {
				for (ReferralPatientDt referralPatientDt : detailInfo) {
					Map<String, Object> map2 = new HashMap<>();
					String hospitalName = "", diagnosisName = "";
					if (referralPatientDt.getReferralPatientHd() != null) {
						hospitalName = referralPatientDt.getReferralPatientHd().getMasImpanneledHospital()
								.getImpanneledHospitalName();
					}
					map2.put("id", referralPatientDt.getRefrealDtId());
					map2.put("header_id", referralPatientDt.getRefrealHdId());
					map2.put("hospital_name", hospitalName);
					map2.put("final_note", HMSUtil.convertNullToEmptyString(referralPatientDt.getFinalNote()));
					map2.put("department_name", HMSUtil.convertNullToEmptyString(referralPatientDt.getExtDepartment()));
					if (referralPatientDt.getMasIcd() != null) {
						diagnosisName = referralPatientDt.getMasIcd().getIcdName();
					}
					map2.put("diagnosis_name", diagnosisName);
					map2.put("referral_date", HMSUtil.changeDateToddMMyyyy(referralPatientDt.getReferralPatientHd().getReferralIniDate()));
					if (referralPatientDt.getDisease() == null) {
						map2.put("notifiable_desease", "N");
					} else {
						map2.put("notifiable_desease", referralPatientDt.getDisease());
					}
					map2.put("instructions", HMSUtil.convertNullToEmptyString(referralPatientDt.getInstruction()));
					if (referralPatientDt.getMb() == null) {
						map2.put("mark_mb", "N");
					} else {
						map2.put("mark_mb", referralPatientDt.getMb());
					}
					if (referralPatientDt.getAdmitted() == null) {
						map2.put("mark_admitted", "N");
					} else {
						map2.put("mark_admitted", referralPatientDt.getAdmitted());
					}
					if (referralPatientDt.getClose() == null) {
						map2.put("close", "N");
					} else {
						map2.put("close", referralPatientDt.getClose());
					}

					detailList.add(map2);

				}
				obj.put("detail_list", detailList);
			} else {
				obj.put("detail_list", new JSONArray());
			}

		} else {
			obj.put("headerList", new JSONArray());
			obj.put("DetailList", new JSONArray());
		}
		return obj.toString();
	}

	@Override
	public String updateReferralDetail(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseJson = od.updateReferralDetail(jsondata, request, response);
		JSONObject obj = new JSONObject();
		obj.put("msg", responseJson.get("msg"));
		//System.out.println("msg is " + responseJson.get("msg"));
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAdmissionDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		JSONArray jsArray = new JSONArray();
		
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getPendingDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> jsonResponse = od.getPendingDischargeList(jsondata, request, response);
		JSONObject obj = new JSONObject();
		
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String dischargeMain(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		
		return obj.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String admissionMain(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		List<Map<String, Object>> jsonList = new ArrayList<>();
		

		return obj.toString();
	}

	@Override
	public String savePatientAdmission(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String result = od.savePatientAdmission(jsondata, request, response);
		JSONObject obj = new JSONObject();
		obj.put("msg", result);
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getServiceWisePatientList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		//Map<String, Object> map = od.getServiceWisePatientList(jsondata, request, response);
		JSONObject obj = new JSONObject();
		
		return obj.toString();

	}

	public String saveNewAdmission(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String serviceNo = "";
		Long patientId = null;
		Long adminSexId = null;
		Long rankId = null;
		Long unitId = null;
		String patientDOB = "";
		
		if(jsondata.get("serviceNo").toString()!=null && !jsondata.get("serviceNo").toString().equalsIgnoreCase("")) {
			serviceNo = jsondata.get("serviceNo").toString().toUpperCase();
		}
		
		if(jsondata.get("patientId").toString()!=null && !jsondata.get("patientId").toString().equalsIgnoreCase("")) {
			patientId = Long.parseLong(jsondata.get("patientId").toString());
		}
		
		if(jsondata.get("rankId").toString()!=null && !jsondata.get("rankId").toString().equalsIgnoreCase("")) {
			rankId = Long.parseLong(jsondata.get("rankId").toString());
		}
		
		if(jsondata.get("genderId").toString()!=null && !jsondata.get("genderId").toString().equalsIgnoreCase("")) {
			adminSexId = Long.parseLong(jsondata.get("genderId").toString());
		}
		
		if(jsondata.get("unitId").toString()!=null && !jsondata.get("unitId").toString().equalsIgnoreCase("")) {
			unitId = Long.parseLong(jsondata.get("unitId").toString());
		}
		
		if(jsondata.get("patientDOB").toString()!=null && !jsondata.get("patientDOB").toString().equalsIgnoreCase("")) {
			patientDOB = jsondata.get("patientDOB").toString();
		}
		
		Patient patient = null;
		String uhidNO=String.valueOf(jsondata.get("uhidNo"));
		 if(!uhidNO.isEmpty()) {
			    patientId = od.getPatientFromUhidNo(uhidNO);
				patient= od.getPatient(patientId);
				if(patient!=null) {
					if(!adminSexId.equals(patient.getAdministrativeSexId()) || 
							 !patientDOB.equals(HMSUtil.convertDateToStringFormat(patient.getDateOfBirth(), "dd/MM/yyyy"))) {
						boolean updateResult =	od.updatePatientDetails(serviceNo,patientId,adminSexId, patientDOB);
				}
			}
		 }else {
			 	JSONObject jsonObj = new JSONObject(jsondata);
				 patientId = od.createPatient(jsonObj);
		 }
		
		jsondata.put("patient_id", String.valueOf(patientId));
		String result = od.saveNewAdmission(jsondata, request, response);
		JSONObject obj = new JSONObject();
		obj.put("msg", result);
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String nursingCareWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		List<Map<String, Object>> nursingCareWaitingList = new ArrayList<>();
		Map<String, Object> map = od.nursingCareWaitingList(jsondata, request, response);
		if (map != null && !map.isEmpty()) {
			List<ProcedureHd> list = (List<ProcedureHd>) map.get("nursingCareList");
			if (list != null && !list.isEmpty()) {
				for (ProcedureHd hd : list) {
					Map<String, Object> data = new HashMap<>();
					String tokenNo = "", name = "", firstName = "", middleName = "", lastName = "", opdDate = "",
							priority = "", patientName = "", age = "", gender = "", status = "", departmentName="", serviceNo="";
					if (hd.getVisit() != null) {
						tokenNo = hd.getVisit().getTokenNo() + "";

						/*if (hd.getVisit().getIntDoctorId() != null) {
							firstName = hd.getVisit().getDoctorId().getFirstName() + "";
							middleName = hd.getVisit().getDoctorId().getMiddleName() + "";
							lastName = hd.getVisit().getDoctorId().getLastName() + "";
						}*/
						name = firstName + " " + middleName + " " + lastName;
						priority = hd.getVisit().getPriority() + "";
						if(hd.getVisit().getMasDepartment() != null) {
							departmentName = HMSUtil.convertNullToEmptyString(hd.getVisit().getMasDepartment().getDepartmentName());
						}	
					}
					data.put("id", hd.getProcedureHdId());
					data.put("token_no", HMSUtil.convertNullToEmptyString(tokenNo));
					
				
					data.put("doctor_name", departmentName);
					data.put("priority", HMSUtil.convertNullToEmptyString(priority));					
					if(hd.getOpdPatientDetails() != null) {
						opdDate = HMSUtil.convertNullToEmptyString(HMSUtil.changeDateToddMMyyyy(hd.getOpdPatientDetails().getOpdDate()));
					}
					 
					data.put("opd_date", opdDate);
					if (hd.getPatient() != null) {
						patientName = hd.getPatient().getPatientName();
						Date date = hd.getPatient().getDateOfBirth();
						age = HMSUtil.calculateAge(date);
						if (hd.getPatient().getMasAdministrativeSex() != null) {
							gender = hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName().toString();
						}
					}
					data.put("patient_name", HMSUtil.convertNullToEmptyString(patientName));
					data.put("age", age);
					data.put("gender", HMSUtil.convertNullToEmptyString(gender));
					data.put("status", HMSUtil.convertNullToEmptyString(hd.getStatus()));
					nursingCareWaitingList.add(data);

				}
				obj.put("status", "1");
				obj.put("count", map.get("count"));
				obj.put("nursingCareWaitingList", nursingCareWaitingList);
			} else {
				obj.put("status", "0");
				obj.put("count", "0");
				obj.put("nursingCareWaitingList", new JSONArray());
			}
		} else {
			obj.put("status", "0");
			obj.put("count", "");
			obj.put("nursingCareWaitingList", new JSONArray());
		}

		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getNursingCareDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, Object> patientMap = new HashMap<>();
		Map<String, Object> map = od.getNursingCareDetail(jsondata, request, response);
		if (map != null && !map.isEmpty()) {
			List<ProcedureDt> list = (List<ProcedureDt>) map.get("detailList");
			List<ProcedureDt> list2 = new ArrayList<>();
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				if (ids.contains(list.get(i).getMasNursingCare().getNursingId())) {
					continue;
				}
				ids.add(list.get(i).getMasNursingCare().getNursingId());
				list2.add(list.get(i));

			}
			if (list2 != null && list2.size() > 0) {
				List<Map<String, Object>> nursingDetailList = new ArrayList<>();
				String patientName = "", age = "", gender = "", icdDiagnosis = "", workingDiagnosis="", procedureName = "", frequency = "",
						noOfDays = "", finalStatus = "", procedureId ="";

				for (ProcedureDt dt : list2) {
					Map<String, Object> data = new HashMap<>();
					if(dt.getProcedureHd().getPatient() != null) {
						patientName = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getPatient().getPatientName());
						if(dt.getProcedureHd().getPatient().getDateOfBirth() != null) {
							Date date = dt.getProcedureHd().getPatient().getDateOfBirth();
							age = HMSUtil.calculateAge(date);
						}
						if(dt.getProcedureHd().getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}
					}
					
					if(dt.getProcedureHd().getOpdPatientDetails() != null) {
						icdDiagnosis = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getOpdPatientDetails().getIcdDiagnosis());
						workingDiagnosis = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getOpdPatientDetails().getWorkingDiagnosis());
					}
					if(dt.getMasNursingCare() != null) {
						procedureName = HMSUtil.convertNullToEmptyString(dt.getMasNursingCare().getNursingName());
						procedureId = HMSUtil.convertNullToEmptyString(dt.getMasNursingCare().getNursingId());
					}
										
					patientMap.put("patientName", patientName);
					patientMap.put("age",age);
					patientMap.put("gender",gender);
					patientMap.put("header_id", HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getProcedureHdId()));
					patientMap.put("icd_diagnosis", icdDiagnosis);
					patientMap.put("working_diagnosis", workingDiagnosis);
					
					data.put("id", dt.getProcedureDtId());
					data.put("procedureName", procedureName);
					if(dt.getMasFrequency() != null) {
						frequency = dt.getMasFrequency().getFrequencyName();
					}
					data.put("frequency", frequency);
					data.put("noOfDays", HMSUtil.convertNullToEmptyString(dt.getNoOfDays()));
					data.put("finalStatus", HMSUtil.convertNullToEmptyString(dt.getFinalProcedureStatus()));
					data.put("procedure_id", procedureId);
					
					nursingDetailList.add(data);
				}
				obj.put("status", "1");
				obj.put("patient_detail", patientMap);
				obj.put("nursingDetailList", nursingDetailList);
			} else {
				obj.put("status", "0");
				obj.put("patient_detail", patientMap);
				obj.put("nursingCareDetailList", new JSONArray());
			}
		} else {
			obj.put("status", "0");
			obj.put("patient_detail", patientMap);
			obj.put("nursingCareDetailList", new JSONArray());
		}
		return obj.toString();

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getProcedureDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = od.getProcedureDetail(jsondata, request, response);
		JSONObject obj = new JSONObject();
		Map<String, Object> detailInfo = new HashMap<>();
		if (map != null && !map.isEmpty()) {
			List<ProcedureDt> list = (List<ProcedureDt>) map.get("detailList");
			if (list != null && list.size() > 0) {
				List<Map<String, Object>> detailList = new ArrayList<>();
				for (ProcedureDt dt : list) {
					Map<String, Object> detailMap = new HashMap<>();
					detailInfo.put("id", dt.getProcedureDtId());
					detailInfo.put("procedure_name", dt.getMasNursingCare().getNursingName());
					detailInfo.put("frequency", dt.getMasFrequency().getFrequencyName());
					detailInfo.put("feq", dt.getMasFrequency().getFeq());
					detailInfo.put("no_of_days", dt.getNoOfDays());
					detailInfo.put("op_remarks", dt.getRemarks());
					String procedureID = "", frequency_id="", header_id = "";
					if(dt.getMasNursingCare() != null) {
						procedureID = HMSUtil.convertNullToEmptyString(dt.getMasNursingCare().getNursingId());
					}
					detailInfo.put("procedure_id", procedureID);
					if(dt.getMasFrequency() != null) {
						frequency_id = HMSUtil.convertNullToEmptyString(dt.getMasFrequency().getFrequencyId());
					}
					detailInfo.put("frequency_id", frequency_id);
					if(dt.getProcedureHd() != null) {
						header_id = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getProcedureHdId());
					}
					detailInfo.put("header_id", header_id);
					Date date = dt.getAppointmentDate();
					String appDate = HMSUtil.changeDateToddMMyyyy(date);
					detailMap.put("appointment_date", appDate);
					Date pDate = dt.getProcedureDate();
					String proDate = "";
					if(pDate != null) {
						proDate = HMSUtil.changeDateToddMMyyyy(pDate);
					}					
					detailMap.put("procedure_date", proDate);
					String nurRemarks = "";
					if (dt.getNursingRemark() != null) {
						nurRemarks = dt.getNursingRemark();
					}
					detailMap.put("nursing_remarks", nurRemarks);
					//System.out.println("dt.getStatus() "+dt.getStatus());
					detailMap.put("status", HMSUtil.convertNullToEmptyString(dt.getStatus()));
					detailMap.put("id", dt.getProcedureDtId());
					detailList.add(detailMap);
				}
				obj.put("status", "1");
				obj.put("detailList", detailList);
				obj.put("detailInfo", detailInfo);

			}
		} else {
			obj.put("status", "0");
			obj.put("detailList", new JSONArray());
			obj.put("detailInfo", detailInfo);
		}
		return obj.toString();
	}

	@Override
	public Map<String, Object> getWaitingPatientList(Map mapForDS) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveProcedureDetail(Map<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String result = od.saveProcedureDetail(jsondata, request, response);
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String physioTherapyWaitingList(Map<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> physioTherapyWaitingList = new ArrayList<>();
		JSONObject obj = new JSONObject();
		Map<String, Object> map = (Map<String, Object>) od.physioTherapyWaitingList(jsondata, request, response);
		if (map != null && !map.isEmpty()) {
			List<ProcedureHd> list = (List<ProcedureHd>) map.get("physioTherapyWaitingList");
			if (list != null && !list.isEmpty()) {
				for (ProcedureHd hd : list) {
					Map<String, Object> data = new HashMap<>();
					String serviceNo = "", name = "", firstName = "", middleName = "", lastName = "", opdDate = "",
							priority = "", patientName = "", age = "", gender = "", status = "", departmentName="";
					if (hd.getVisit() != null) {
						// tokenNo = hd.getVisit().getTokenNo()+"";

					/*	if (hd.getVisit().getIntDoctorId() != null) {
							firstName = hd.getVisit().getDoctorId().getFirstName() + "";
							middleName = hd.getVisit().getDoctorId().getMiddleName() + "";
							lastName = hd.getVisit().getDoctorId().getLastName() + "";
						}*/
						name = firstName + " " + middleName + " " + lastName;
						priority = hd.getVisit().getPriority() + "";
						if(hd.getVisit().getMasDepartment() != null) {
							departmentName = HMSUtil.convertNullToEmptyString(hd.getVisit().getMasDepartment().getDepartmentName());
						}						
					}
					data.put("id", hd.getProcedureHdId());
					// data.put("token_no", tokenNo);
					data.put("doctor_name", departmentName);
					data.put("priority", HMSUtil.convertNullToEmptyString(priority));
					/*
					 * if(hd.getOpdPatientDetail() != null) { opdDate =
					 * HMSUtil.convertDateToStringFormat(hd.getOpdPatientDetail().getOpdDate(),
					 * "dd-MM-yyyy"); }
					 */
					data.put("opd_date", HMSUtil.changeDateToddMMyyyy(hd.getOpdPatientDetails().getOpdDate()));
					if (hd.getPatient() != null) {
						patientName = hd.getPatient().getPatientName();
						Date date = hd.getPatient().getDateOfBirth();
						age = HMSUtil.calculateAge(date);
						if (hd.getPatient().getMasAdministrativeSex() != null) {
							gender = hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName().toString();
						}
					}
					data.put("service_no", HMSUtil.convertNullToEmptyString(serviceNo));
					data.put("patient_name", HMSUtil.convertNullToEmptyString(patientName));
					data.put("age", HMSUtil.convertNullToEmptyString(age));
					data.put("gender", HMSUtil.convertNullToEmptyString(gender));
					data.put("status", HMSUtil.convertNullToEmptyString(hd.getStatus()));
					physioTherapyWaitingList.add(data);

				}
				obj.put("status", "1");
				obj.put("count", map.get("count"));
				obj.put("physioTherapyWaitingList", physioTherapyWaitingList);
			} else {
				obj.put("status", "0");
				obj.put("count", "");
				obj.put("physioTherapyWaitingList", new JSONArray());
			}
		} else {
			obj.put("status", "0");
			obj.put("count", "");
			obj.put("physioTherapyWaitingList", new JSONArray());
		}

		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getphysioTherapyDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, Object> patientMap = new HashMap<>();
		Map<String, Object> map = od.getphysioTherapyDetail(jsondata, request, response);
		if (map != null && !map.isEmpty()) {
			List<ProcedureDt> list = (List<ProcedureDt>) map.get("detailList");
			List<ProcedureDt> list2 = new ArrayList<>();
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				if (ids.contains(list.get(i).getMasNursingCare().getNursingId())) {
					continue;
				}
				ids.add(list.get(i).getMasNursingCare().getNursingId());
				list2.add(list.get(i));

			}
			//System.out.println("size of list is " + list.size() + " & size of list2 is " + list2.size());
			if (list2 != null && list2.size() > 0) {
				List<Map<String, Object>> physioDetailList = new ArrayList<>();
				String patientName = "", age = "", gender = "", icdDiagnosis = "", workingDiagnosis="", procedureName = "", frequency = "",
						noOfDays = "", finalStatus = "", nursing_remarks, startDate="", procedureId="";
				
				for (ProcedureDt dt : list2) {
					Map<String, Object> data = new HashMap<>();
					if(dt.getProcedureHd().getPatient() != null) {
						patientName = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getPatient().getPatientName());
						if(dt.getProcedureHd().getPatient().getDateOfBirth() != null) {
							Date date = dt.getProcedureHd().getPatient().getDateOfBirth();
							age = HMSUtil.calculateAge(date);
						}
						if(dt.getProcedureHd().getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}
					}
					if(dt.getProcedureHd().getOpdPatientDetails() != null) {
						icdDiagnosis = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getOpdPatientDetails().getIcdDiagnosis());
						workingDiagnosis = HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getOpdPatientDetails().getWorkingDiagnosis());
					}
					if(dt.getMasNursingCare() != null) {
						procedureName = HMSUtil.convertNullToEmptyString(dt.getMasNursingCare().getNursingName());
						procedureId = HMSUtil.convertNullToEmptyString(dt.getMasNursingCare().getNursingId());
					}
					if(dt.getAppointmentDate() != null) {
						startDate = HMSUtil.changeDateToddMMyyyy(dt.getAppointmentDate());
					}
					if(dt.getMasFrequency() != null) {
						frequency = HMSUtil.convertNullToEmptyString(dt.getMasFrequency().getFrequencyName());
					}
					patientMap.put("patientName",patientName);
					patientMap.put("age", age);
					patientMap.put("gender", gender);
					patientMap.put("header_id",HMSUtil.convertNullToEmptyString(dt.getProcedureHd().getProcedureHdId()));
					patientMap.put("icd_diagnosis", icdDiagnosis);
					patientMap.put("working_diagnosis", workingDiagnosis);
					data.put("id", HMSUtil.convertNullToEmptyString(dt.getProcedureDtId()));
					data.put("physiotherapy_name",procedureName);
					data.put("start_date", startDate);
					data.put("frequency", frequency);
					data.put("noOfDays", HMSUtil.convertNullToEmptyString(dt.getNoOfDays()));
					data.put("op_remarks", HMSUtil.convertNullToEmptyString(dt.getRemarks()));
					data.put("finalStatus", HMSUtil.convertNullToEmptyString(dt.getFinalProcedureStatus()));
					data.put("procedure_id", procedureId);
					physioDetailList.add(data);
				}
				obj.put("status", "1");
				obj.put("patient_detail", patientMap);
				obj.put("physioDetailList", physioDetailList);
			} else {
				obj.put("status", "0");
				obj.put("patient_detail", patientMap);
				obj.put("physioDetailList", new JSONArray());
			}
		} else {
			obj.put("status", "0");
			obj.put("patient_detail", patientMap);
			obj.put("physioDetailList", new JSONArray());
		}
		return obj.toString();

	}

	////////////////////////////// Examination Detail ///////////////
	@Override
	public String getExaminationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.checkPatientVisit(jsondata);
			if (nullbalankvalidation.optString("status").equals("0")) {
				return nullbalankvalidation.toString();
			} else {
				OpdPatientDetail opdPatientDetail = null;
				if (jsondata.get("visitId") != null) {
					opdPatientDetail = opdPatientDetailDao
							.getOpdPatientDetails(Long.parseLong(jsondata.get("visitId").toString()));
					
				}
				if (opdPatientDetail != null) {
					try {

						HashMap<String, Object> pt = new HashMap<String, Object>();
						
						pt.put("workingdiagnosis", opdPatientDetail.getWorkingDiagnosis());
						pt.put("patientSymptons", opdPatientDetail.getPatientSymptoms());
						pt.put("patientPastHistory", opdPatientDetail.getPastHistory());
						pt.put("opdPatientDetailId", opdPatientDetail.getOpdPatientDetailsId());
						//pt.put("allergyId", opdPatientDetail.getAllergyId());
						/*if(opdObesityHd!=null) {
							pt.put("overweightFlag", opdObesityHd.getOverweightFlag());
						}
						else {
							pt.put("overweightFlag", "");
						}*/
						
						c.add(pt);
						
						
						json.put("data", c);
						//json.put("opdObesityHd", opdObesityHd);
						json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
						json.put("status", "1");

					}

					catch (Exception e) {
						e.printStackTrace();
						return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
				} else {
					try {
						json.put("msg", "Visit ID data not found");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return json.toString();
	}

	////////////////////////////// Examination Detail ///////////////
	@Override
	public String getInvestigationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object[]> listObject = null;
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.checkPatientVisit(jsondata);
			if (nullbalankvalidation.optString("status").equals("0")) {
				return nullbalankvalidation.toString();
			} else {
				List<DgMasInvestigation> listDgMasInvestigation = null;
				if (jsondata.get("visitId") != null) {
					listObject = dgOrderhdDao.getDgMasInvestigations(Long.parseLong(jsondata.get("visitId").toString()),Long.parseLong(jsondata.get("opdPatientDetailId").toString()));
				}
				Long investigationId = null;
				String investigationName = "";
				Long orderHdId = null;
				String orderDate = "";
				Long visitId = null;
				Long departId = null;
				Long hospitalId = null;
				Long dgOrderDtId = null;
				String orderStustus="";
				if (listObject != null) {
					try {
						for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
							Object[] row = (Object[]) it.next();

							/*
							 * for(Object dgMasInvestigation : listObject) { Object row=dgMasInvestigation;
							 */
							HashMap<String, Object> pt = new HashMap<String, Object>();

							if (row[0] != null) {
								investigationId = Long.parseLong(row[0].toString());
							}
							if (row[1] != null) {
								investigationName = row[1].toString();
							}

							if (row[2] != null) {
								orderHdId = Long.parseLong(row[2].toString());
							}

						

							if (row[3] != null) {
								orderDate = row[3].toString();
								Date dd1 = HMSUtil.dateFormatteryyyymmdd(orderDate);
								orderDate = HMSUtil.convertDateToStringFormat(
										dd1, "dd/MM/yyyy");
							}
							if (row[4] != null) {
								visitId = Long.parseLong(row[4].toString());
							}
						
							if (row[5] != null) {
								departId = Long.parseLong(row[5].toString());
							}
							if (row[6] != null) {
								hospitalId = Long.parseLong(row[6].toString());
							}
							if (row[7] != null) {
								dgOrderDtId = Long.parseLong(row[7].toString());
							}
							if (row[8] != null) {
								orderStustus =  row[8].toString();
							}
							else {
								orderStustus = "";
							}
							pt.put("investigationName", investigationName);
							pt.put("investigationId", investigationId);
							pt.put("orderDate", orderDate);
							pt.put("visitId", visitId);
							pt.put("departId", departId);
							pt.put("hospitalId", hospitalId);
							pt.put("dgOrderDtId", dgOrderDtId);
							pt.put("orderHdId", orderHdId);
							pt.put("orderStustus", orderStustus);
							c.add(pt);
						}
						json.put("listObject", c);
						json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
						json.put("status", "1");

					}

					catch (Exception e) {
						e.printStackTrace();
						return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
				} else {
					try {
						json.put("msg", "Visit ID data not found");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return json.toString();

	}

	@Override
	public String getTreatmentPatientDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object[]> listObject = null;
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.checkPatientVisit(jsondata);
			if (nullbalankvalidation.optString("status").equals("0")) {
				return nullbalankvalidation.toString();
			} else {
				if (jsondata.get("visitId") != null) {
					listObject = dgOrderhdDao.getTreatementDetail(Long.parseLong(jsondata.get("visitId").toString()),Long.parseLong(jsondata.get("opdPatientDetailId").toString()));
				}
				List<MasFrequency> mas_frequency = md.getMasFrequency();
				List<MasTreatmentInstruction> masTreatmentInstruction = md.getTreatmentInstruction();
				Long itemId = null;
				String nomenclature = "";
				
				Long frequencyId = null;
				String frequencyName = "";
				String frequencyCode = "";
				String dosage = "";
				Long noOfDays = null;
				Long precryptionHdId = null;
				Long precriptionDtId = null;
				//String dispStock = "";
				Long total = null;
				String instruction = "";
				String storeStoke = "";
				String PVMSno="";
				String otherTreatement="";
				Long itemTypeId=null;
				//String nisFlag="";
				//String immunizationFlag="";
				Long dispUnitId=null;
				Long itemClassId=null;
				String status="";
				if (listObject != null) {
					try {
						for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
							Object[] row = (Object[]) it.next();

							
							
							/*
							 * for(Object dgMasInvestigation : listObject) { Object row=dgMasInvestigation;
							 */
							HashMap<String, Object> pt = new HashMap<String, Object>();

							if (row[0] != null) {
								nomenclature = row[0].toString();
							}
							else {
								nomenclature="";
							}
							if (row[1] != null) {
								itemId = Long.parseLong(row[1].toString());
							}
							else {
								itemId =null;
							}

							if (row[2] != null) {
								frequencyId = Long.parseLong(row[2].toString());
							}
							else {
								frequencyId=null;
							}

							// odt.LAB_MARK,odt.urgent,odt.ORDER_DATE
							if (row[3] != null) {
								frequencyName = row[3].toString();
							}
							else {
								frequencyName="";
							}
							if (row[4] != null) {
								frequencyCode = row[4].toString();
							}
							else {
								frequencyCode="";
							}

							if (row[5] != null) {
								noOfDays = Long.parseLong(row[5].toString());
							}
							else {
								noOfDays=null;
							}
							if (row[6] != null) {
								dosage = row[6].toString();
							}
							else {
								dosage="";
							}
							if (row[7] != null) {
								precryptionHdId = Long.parseLong(row[7].toString());
							}
							else {
								precryptionHdId=null;
							}
							if (row[8] != null) {
								precriptionDtId = Long.parseLong(row[8].toString());
							}
							else {
								precriptionDtId=null;
							}
						

							if (row[9] != null) {
								total = Long.parseLong(row[9].toString());
							}
							else {
								total=null;
							}
							if (row[10] != null) {
								instruction = row[10].toString();
							}
							else {
								instruction="";
							}
							if (row[11] != null) {
								storeStoke = row[11].toString();
							}
							else {
								storeStoke="";
							}
							if (row[13] != null) {
								PVMSno = row[13].toString();
							}
							else {
								PVMSno="";
							}
							
							if (row[14] != null) {
								itemTypeId = Long.parseLong(row[14].toString());
							}
							else {
								itemTypeId=null;
							}
							
							
							
							if (row[15] != null) {
								dispUnitId =Long.parseLong(row[15].toString());
							}
							else {
								dispUnitId=null;
							}
							if (row[16] != null) {
								itemClassId =  Long.parseLong(row[16].toString());
							}
							else {
								itemClassId=null;
							}
							if (row[17] != null) {
								status =   row[17].toString();
							}
							else {
								status="";
							}
							pt.put("nomenclature", nomenclature);
							pt.put("itemId", itemId);
							pt.put("frequencyId", frequencyId);
							pt.put("frequencyName", frequencyName);
							pt.put("frequencyCode", frequencyCode);
							pt.put("dosage", dosage);
							pt.put("noOfDays", noOfDays);
							pt.put("precryptionHdId", precryptionHdId);
							pt.put("precriptionDtId", precriptionDtId);
							//pt.put("dispStock", dispStock);
							pt.put("total", total);
							pt.put("instruction", instruction);
							pt.put("storeStoke", storeStoke);
							pt.put("PVMSno", PVMSno);
							//pt.put("otherTreatement", otherTreatement);
							pt.put("itemTypeId", itemTypeId);
							
							//pt.put("nisFlag", nisFlag);
							//pt.put("immunizationFlag", immunizationFlag);
							
							pt.put("dispUnitId", dispUnitId);
							pt.put("itemClassId", itemClassId);
							pt.put("status", status);
							c.add(pt);
						}
						Long itemTypeIdPvms=null;
						  String itemTypeCodePvms=HMSUtil.getProperties("adt.properties", "itemTypeCodePvms").trim();
						  if(itemTypeCodePvms!=null && itemTypeCodePvms!="")
							{	
								MasItemType mty=null;
								mty = md.getItemTypeId(itemTypeCodePvms);
								itemTypeIdPvms=mty.getItemTypeId();
								//System.out.println("itemTypeId" +itemId);
							}
						json.put("listObject", c);
						json.put("visitId", jsondata.get("visitId"));
						json.put("MasFrequencyList", mas_frequency);
						json.put("masTreatmentInstruction", masTreatmentInstruction);
						json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
						json.put("itemTypeIdPvms", itemTypeIdPvms);
						json.put("status", "1");

					}

					catch (Exception e) {
						e.printStackTrace();
						return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
				} else {
					try {
						json.put("msg", "Visit ID data not found");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return json.toString();

	}
	
	@Override
	@Transactional
	public String submitPatientRecall(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String visitId = "";
		String opdPatientDetaiId = "";
		String patientId = "";
		Transaction tx = null;
		JSONObject json = new JSONObject();
		try {

			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			if (payload.get("precriptionDtValue") != null) {
				// String precriptionDtIdVal = payload.get("precriptionDtIdVal").toString();
				// precriptionDtIdVal = getReplaceString(precriptionDtIdVal);

				updateCurrentMedication(payload, null, null);
			}

			if (payload.get("opdPatientDetailId") != null) {
				opdPatientDetaiId = payload.get("opdPatientDetailId").toString();
				opdPatientDetaiId = getReplaceString(opdPatientDetaiId);
			}
			
			if (payload.get("VisitID") != null) {
				visitId = payload.get("VisitID").toString();
				visitId = getReplaceString(visitId);
			}
			// SaveUpdate DgOrderDt
			String investigationIdValue = "";
			if (payload.get("investigationIdValue") != null) {
				investigationIdValue = payload.get("investigationIdValue").toString();
				investigationIdValue = getReplaceString(investigationIdValue);
			}
			String investigationDate = "";
			if (payload.get("investigationDate") != null) {
				investigationDate = payload.get("investigationDate").toString();
				investigationDate = getReplaceString(investigationDate);
			}
			String labradiologyCheck1 = "";
			if (payload.get("labradiologyCheck") != null) {
				labradiologyCheck1 = payload.get("labradiologyCheck").toString();
				labradiologyCheck1 = getReplaceString(labradiologyCheck1);
			}
			String urgent1 = "";
			if (payload.get("urgent") != null && !payload.get("urgent").equals("")) {
				urgent1 = payload.get("urgent").toString();
				urgent1 = getReplaceString(urgent1);
			}

			String marksAsLabValue = "";
			if (payload.get("marksAsLabValue") != null && !payload.get("marksAsLabValue").equals("")) {
				marksAsLabValue = payload.get("marksAsLabValue").toString();
				marksAsLabValue = getReplaceString(marksAsLabValue);
			}

			String urgentValue = "";
			if (payload.get("urgentValue") != null && !payload.get("urgentValue").equals("")) {
				urgentValue = payload.get("urgentValue").toString();
				urgentValue = getReplaceString(urgentValue);
			}
			String dgOrderDtIdValue = "";
			if (payload.get("dgOrderDtIdValue") != null) {
				dgOrderDtIdValue = payload.get("dgOrderDtIdValue").toString();
				dgOrderDtIdValue = getReplaceString(dgOrderDtIdValue);
			}
			String dgOrderHdIdValue = "";
			if (payload.get("dgOrderHdId") != null) {
				dgOrderHdIdValue = payload.get("dgOrderHdId").toString();
				dgOrderHdIdValue = getReplaceString(dgOrderHdIdValue);
			}
			saveUpdateOpdPatientDetail(payload);
			String obsistyCheckAlready = "";
			if (payload.get("obsistyCheckAlready") != null) {
				obsistyCheckAlready = payload.get("obsistyCheckAlready").toString();
				obsistyCheckAlready = getReplaceString(obsistyCheckAlready);
			}
		
			saveOrUpDatedDgOrderDt(investigationIdValue, investigationDate, marksAsLabValue, urgentValue,
					dgOrderDtIdValue, dgOrderHdIdValue, payload);

			// SaveUpdate Mass store Item
			String itemIdValue = "";
			if (payload.get("itemId") != null) {
				itemIdValue = payload.get("itemId").toString();
				itemIdValue = getReplaceString(itemIdValue);
			}
			String prescriptionHdId = "";
			if (payload.get("prescriptionHdId") != null) {
				prescriptionHdId = payload.get("prescriptionHdId").toString();
				prescriptionHdId = getReplaceString(prescriptionHdId);
			}
			String prescriptionDtIdValue = "";
			if (payload.get("prescriptionDtId") != null) {
				prescriptionDtIdValue = payload.get("prescriptionDtId").toString();
				prescriptionDtIdValue = getReplaceString(prescriptionDtIdValue);
			}
			String dispensingUnitValue = "";
			if (payload.get("dispensingUnit1") != null) {
				dispensingUnitValue = payload.get("dispensingUnit1").toString();
				dispensingUnitValue = getReplaceString(dispensingUnitValue);
			}
			String dosage1Value = "";
			if (payload.get("dosage1") != null) {
				dosage1Value = payload.get("dosage1").toString();
				dosage1Value = getReplaceString(dosage1Value);
			}
			String frequency1Value = "";
			if (payload.get("frequencyTre") != null) {
				frequency1Value = payload.get("frequencyTre").toString();
				frequency1Value = getReplaceString(frequency1Value);
			}
			String total1Value = "";
			if (payload.get("total1") != null) {
				total1Value = payload.get("total1").toString();
				total1Value = getReplaceString(total1Value);
			}
			String remarks1Value = "";
			if (payload.get("instuctionFill") != null) {
				remarks1Value = payload.get("instuctionFill").toString();
				remarks1Value = getReplaceString(remarks1Value);
			}
			String noOfDays1Value = "";
			if (payload.get("noOfDays1") != null) {
				noOfDays1Value = payload.get("noOfDays1").toString();
				noOfDays1Value = getReplaceString(noOfDays1Value);
			}
			String closingStock1 = "";
			if (payload.get("closingStock1") != null) {
				closingStock1 = payload.get("closingStock1").toString();
				closingStock1 = getReplaceString(closingStock1);
			}

			String statusOfPvsms = "";
			if (payload.get("statusOfPvsms") != null) {
				statusOfPvsms = payload.get("statusOfPvsms").toString();
				statusOfPvsms = getReplaceString(statusOfPvsms);
			}

			saveOrUpDatedMassStoreItems(itemIdValue, dispensingUnitValue, dosage1Value, frequency1Value, noOfDays1Value,
					total1Value, remarks1Value, closingStock1, prescriptionDtIdValue, prescriptionHdId, payload,
					statusOfPvsms);

			// For Referal update and inserrt only save contain one yes
			patientId = payload.get("patientId").toString();
			patientId = getReplaceString(patientId);

			String opdPatientDetailsId = payload.get("opdPatientDetailId").toString();
			opdPatientDetailsId = getReplaceString(opdPatientDetailsId);

			String referralForNew = payload.get("referralForNew").toString();
			if (StringUtils.isNotBlank(referralForNew) && referralForNew.contains("1")) {

				String referHospitalList = payload.get("referHospitalList").toString();
				referHospitalList = getReplaceString(referHospitalList);
				String referHospitalValues = "";
				if (StringUtils.isNotBlank(referHospitalList)) {
					if (referHospitalList.contains("@")) {
						String[] referHospitalListValue = referHospitalList.split(",");
						int count = 0;
						for (int i = count; i < referHospitalListValue.length; i++) {
							String[] referHospitalListValueNew = referHospitalListValue[i].split("@");
							referHospitalValues += referHospitalListValueNew[0] + ",";
							// count+=2;
						}
					}
					referHospitalList = referHospitalValues;
				}

				String departmentValue = payload.get("departmentValue").toString();
				departmentValue = getReplaceString(departmentValue);
				String finalDepartmentValue = "";
				if (payload.get("referTo") != null) {
					String referToValue = payload.get("referTo").toString();
					referToValue = getReplaceString(referToValue);
					if (referToValue.equalsIgnoreCase("I")) {
						if (StringUtils.isNotBlank(departmentValue)) {
							if (departmentValue.contains("@")) {
								String[] departmentListValue = departmentValue.split(",");
								int count = 0;
								for (int i = count; i < departmentListValue.length; i++) {
									String[] departListValueNew = departmentListValue[i].split("@");
									finalDepartmentValue += departListValueNew[0] + ",";
									// count+=2;
								}
							}
							departmentValue = finalDepartmentValue;
						}
					}
				}

				//String diagonsisId = payload.get("diagonsisId").toString();
				//diagonsisId = getReplaceString(diagonsisId);

				String hos = "1";//payload.get("hos").toString();
				//hos = getReplaceString(hos);
                hos="1";
				String referalPatientDtValue = payload.get("referalPatientDt").toString();
				referalPatientDtValue = getReplaceString(referalPatientDtValue);

				String referalPatientHdValue = payload.get("referalPatientHd").toString();
				referalPatientHdValue = getReplaceString(referalPatientHdValue);

				String referralNote = "";
				String referVisitDate = "";
				if (payload.get("referralNote") != null) {
					referralNote = payload.get("referralNote").toString();
					referralNote = getReplaceString(referralNote);
				}

				if (payload.get("referVisitDate") != null) {
					referVisitDate = payload.get("referVisitDate").toString();
					referVisitDate = getReplaceString(referVisitDate);
				}
				
				String doctorNote = "";
				if (payload.get("doctorRemarks") != null) {
					doctorNote = payload.get("doctorRemarks").toString();
					doctorNote = getReplaceString(doctorNote);
				}

				saveOrUpdateReferrPatient(referHospitalList, departmentValue, hos, referalPatientDtValue,
						referalPatientHdValue, opdPatientDetailsId, patientId, referralNote, doctorNote,referVisitDate, payload);
			}
			if (StringUtils.isNotBlank(referralForNew) && referralForNew.contains("0"))
			{
				List<String> list = (List<String>) payload.get("referalPatientHd");
				if(null!=list) {
					
				String referalPatientHdValue = payload.get("referalPatientHd").toString();
				referalPatientHdValue = getReplaceString(referalPatientHdValue);
				
				String referalPatientDtValue = payload.get("referalPatientDt").toString();
				referalPatientDtValue = getReplaceString(referalPatientDtValue);
				
				od.deleteRefferalDetailsNo(Long.parseLong(referalPatientHdValue),Long.parseLong(referalPatientDtValue),Long.parseLong(opdPatientDetailsId));
				}
				
			}

			saveOrUpdateProcedure(payload, visitId, opdPatientDetaiId, patientId);

			/* Nip working start */
			if (payload.get("itemIdNomNip") != null) {
				String prescriptionNivDtId = "";
				if (payload.get("prescriptionNivDtId") != null) {
					prescriptionNivDtId = payload.get("prescriptionNivDtId").toString();
					prescriptionNivDtId = getReplaceString(prescriptionNivDtId);
				}
				String prescriptionNivHdId = "";
				if (payload.get("prescriptionNivHdId") != null) {
					prescriptionNivHdId = payload.get("prescriptionNivHdId").toString();
					prescriptionNivHdId = getReplaceString(prescriptionNivHdId);
				}
				String nipItemValue = "";
				if (payload.get("itemIdNomNip") != null) {
					nipItemValue = payload.get("itemIdNomNip").toString();
					nipItemValue = getReplaceString(nipItemValue);
				}
				if (StringUtils.isNotBlank(nipItemValue) && !nipItemValue.trim().equalsIgnoreCase(",")) {
					String dispensingUnitNip1 = "";
					if (payload.get("dispensingUnitNip1") != null) {
						dispensingUnitNip1 = payload.get("dispensingUnitNip1").toString();
						dispensingUnitNip1 = getReplaceString(dispensingUnitNip1);
					}
					String dosageNip1 = "";
					if (payload.get("dosageNip1") != null) {
						dosageNip1 = payload.get("dosageNip1").toString();
						dosageNip1 = getReplaceString(dosageNip1);
					}
					String nipfrequency1 = "";
					if (payload.get("nipfrequency1") != null) {
						nipfrequency1 = payload.get("nipfrequency1").toString();
						nipfrequency1 = getReplaceString(nipfrequency1);
					}
					String nipHospitalValues = "";
					if (StringUtils.isNotBlank(nipfrequency1)) {
						if (nipfrequency1.contains("@")) {
							String[] nipFrequencyListValue = nipfrequency1.split(",");
							int count = 0;
							for (int i = count; i < nipFrequencyListValue.length; i++) {
								String[] nipFrequencyListValueNew = nipFrequencyListValue[i].split("@");
								nipHospitalValues += nipFrequencyListValueNew[1] + ",";
								// count+=2;
							}
						} else {
							nipHospitalValues = nipfrequency1;
						}
					}
					String noOfDaysNip1 = "";
					if (payload.get("noOfDaysNip1") != null) {
						noOfDaysNip1 = payload.get("noOfDaysNip1").toString();
						noOfDaysNip1 = getReplaceString(noOfDaysNip1);
					}
					String totalNip1 = "";
					if (payload.get("totalNip1") != null) {
						totalNip1 = payload.get("totalNip1").toString();
						totalNip1 = getReplaceString(totalNip1);
					}
					String remarksNip1 = "";
					if (payload.get("remarksNip1") != null) {
						remarksNip1 = payload.get("remarksNip1").toString();
						remarksNip1 = getReplaceString(remarksNip1);
					}

					String statusOfNiv = "";
					if (payload.get("statusOfNiv") != null) {
						statusOfNiv = payload.get("statusOfNiv").toString();
						statusOfNiv = getReplaceString(statusOfNiv);
					}

					saveOrUpDatedMassStoreItems(nipItemValue, dispensingUnitNip1, dosageNip1, nipHospitalValues,
							noOfDaysNip1, totalNip1, remarksNip1, "", prescriptionNivDtId, prescriptionNivHdId, payload,
							statusOfNiv);

				}

			}
			if (payload.get("newNip1") != null) {
				String prescriptionNivDtId = "";
				if (payload.get("prescriptionNivDtId") != null) {
					prescriptionNivDtId = payload.get("prescriptionNivDtId").toString();
					prescriptionNivDtId = getReplaceString(prescriptionNivDtId);
				}
				String prescriptionNivHdId = "";
				if (payload.get("prescriptionNivHdId") != null) {
					prescriptionNivHdId = payload.get("prescriptionNivHdId").toString();
					prescriptionNivHdId = getReplaceString(prescriptionNivHdId);
				}
				String newNip1 = "";
				if (payload.get("newNip1") != null) {
					newNip1 = payload.get("newNip1").toString();
					newNip1 = getReplaceString(newNip1);
				}
				if (StringUtils.isNotEmpty(newNip1) && !newNip1.trim().equalsIgnoreCase(",")) {
					String class1 = "";
					if (payload.get("class1") != null) {
						class1 = payload.get("class1").toString();
						class1 = getReplaceString(class1);
					}
					String au1 = "";
					if (payload.get("au1") != null) {
						au1 = payload.get("au1").toString();
						au1 = getReplaceString(au1);
					}
					String dispensingUnitNip1 = "";
					if (payload.get("dispensingUnitNip1Niv") != null) {
						dispensingUnitNip1 = payload.get("dispensingUnitNip1Niv").toString();
						dispensingUnitNip1 = getReplaceString(dispensingUnitNip1);
					}
					String dosageNip1 = "";
					if (payload.get("dosageNip1") != null) {
						dosageNip1 = payload.get("dosageNip1").toString();
						dosageNip1 = getReplaceString(dosageNip1);
					}
					String nipfrequency1 = "";
					if (payload.get("nipfrequency1") != null) {
						nipfrequency1 = payload.get("nipfrequency1").toString();
						nipfrequency1 = getReplaceString(nipfrequency1);
					}

					String nipHospitalValues = "";
					if (StringUtils.isNotBlank(nipfrequency1)) {
						if (nipfrequency1.contains("@")) {
							String[] nipFrequencyListValue = nipfrequency1.split(",");
							int count = 0;
							for (int i = count; i < nipFrequencyListValue.length; i++) {
								String[] nipFrequencyListValueNew = nipFrequencyListValue[i].split("@");
								nipHospitalValues += nipFrequencyListValueNew[1] + ",";
								// count+=2;
							}
						} else {
							nipHospitalValues = nipfrequency1;
						}
					}
					String noOfDaysNip1 = "";
					if (payload.get("noOfDaysNip1") != null) {
						noOfDaysNip1 = payload.get("noOfDaysNip1").toString();
						noOfDaysNip1 = getReplaceString(noOfDaysNip1);
					}
					String totalNip1 = "";
					if (payload.get("totalNip1") != null) {
						totalNip1 = payload.get("totalNip1").toString();
						totalNip1 = getReplaceString(totalNip1);
					}
					String remarksNip1 = "";
					if (payload.get("remarksNip1") != null) {
						remarksNip1 = payload.get("remarksNip1").toString();
						remarksNip1 = getReplaceString(remarksNip1);
					}

					String statusOfNewNiv = "";
					if (payload.get("statusOfNewNiv") != null) {
						statusOfNewNiv = payload.get("statusOfNewNiv").toString();
						statusOfNewNiv = getReplaceString(statusOfNewNiv);
					}

					saveOrUpdateNewNip("", dispensingUnitNip1, dosageNip1, nipHospitalValues, noOfDaysNip1, totalNip1,
							remarksNip1, "", prescriptionNivDtId, prescriptionNivHdId, payload, statusOfNewNiv);

				}
			}
			/* Nip End */

			/*
			 * SaveOrUpdate PatientImplantHistory
			 */

			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			json.put("visitId", visitId);
			json.put("opdPatientDetaiId", opdPatientDetaiId);
			json.put("opdMessage", "OPD not Updated Successfully");

			if (tx != null) {
				try {
					tx.rollback();

				} catch (Exception re) {
					re.printStackTrace();
					json.put("visitId", visitId);
					json.put("opdPatientDetaiId", opdPatientDetaiId);
					json.put("opdMessage", "OPD not Updated Successfully");
					getHibernateUtils.getHibernateUtlis().CloseConnection();
					return json.toString();
				}
			}
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return json.toString();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		json.put("visitId", visitId);
		json.put("opdPatientDetaiId", opdPatientDetaiId);
		json.put("opdMessage", "OPD Updated Successfully");
		return json.toString();
	}
	
	
	
	// Saveupdadate Referal candidate
	public void saveOrUpdateReferrPatient(String referHospitalList, String departmentValue,
			String hos, String referalPatientDtValue, String referalPatientHdValue, String opdPatientDetailsId,
			String patient,String referalNotes,String doctorNote,String referalDates,HashMap<String, Object> payload) {
		String referToValue="";
		//try {

			String[] referHospitalListValue = referHospitalList.split(",");
			String[] departmentValueArray = departmentValue.split(",");
			//String[] diagonsisIdValue = diagonsisId.split(",");
			String[] hosValue = hos.split(",");

			String[] referalPatientDtValueArray = referalPatientDtValue.split(",");
			String[] referalPatientHdValueArray = referalPatientHdValue.split(",");
			HashMap<String, String> mapInvestigationMap = new HashMap<>();

			String finalValue = "";
			Integer counter = 1;
			String hospitalId="";
			String userId="";
			try {
			hospitalId = payload.get("hospitalId").toString();
			hospitalId = getReplaceString(hospitalId);

			userId = payload.get("userIdVal").toString();
			userId = getReplaceString(userId);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < referHospitalListValue.length; i++) {

				if (StringUtils.isNotEmpty(referHospitalListValue[i].toString())
						&& !referHospitalListValue[i].equalsIgnoreCase("0")) {
					finalValue += referHospitalListValue[i].trim();
					if (!referalPatientDtValueArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(referalPatientDtValueArray[i])) {
						for (int m = i; m < referalPatientDtValueArray.length; m++) {
							finalValue += "," + referalPatientDtValueArray[m].trim();
							if (m == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}

					if (!departmentValueArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(departmentValueArray[i])) {
						for (int j = i; j < departmentValueArray.length; j++) {
							finalValue += "," + departmentValueArray[j].trim();
							if (j == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}
					/*if (i < diagonsisIdValue.length && StringUtils.isNotBlank(diagonsisIdValue[i])) {
						for (int k = i; k < diagonsisIdValue.length; k++) {
							finalValue += "," + diagonsisIdValue[k].trim();
							if (k == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}*/
					if (i < hosValue.length && StringUtils.isNotBlank(hosValue[i])) {
						for (int l = i; l < hosValue.length; l++) {
							finalValue += "," + hosValue[l].trim();
							if (l == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}
					mapInvestigationMap.put(referHospitalListValue[i].trim() + "@#" + counter, finalValue);
					finalValue = "";
					counter++;
				}
			}
			counter = 1;
			Date date=new Date();
			for (String referHospitalId : referHospitalListValue) {
				if (StringUtils.isNotEmpty(referHospitalId)) {
					if (mapInvestigationMap.containsKey(referHospitalId.trim() + "@#" + counter)) {
						String referHospitalIdValue = mapInvestigationMap.get(referHospitalId.trim() + "@#" + counter);

						if (StringUtils.isNotEmpty(referHospitalIdValue)) {

							String[] finalValueReferal = referHospitalIdValue.split(",");
							ReferralPatientDt referralPatientDt = null;
							if (finalValueReferal[1] != null && !finalValueReferal[1].equalsIgnoreCase("0")
									&& StringUtils.isNotBlank(finalValueReferal[1])) {
								referralPatientDt = dgOrderhdDao.getReferralPatientDtByReferaldtId(
										Long.parseLong(finalValueReferal[1].toString()));
							} else {
								referralPatientDt = new ReferralPatientDt();
								if (referalPatientHdValueArray != null
										&& StringUtils.isNotBlank(referalPatientHdValueArray[0])) {
									referralPatientDt
											.setRefrealHdId(Long.parseLong(referalPatientHdValueArray[0].toString()));
								}

							}

							if (finalValueReferal != null) {
								/*  referToValue= payload.get("referTo").toString();
								referToValue=	getReplaceString(referToValue);
								if( StringUtils.isNotEmpty(referToValue) && referToValue.equalsIgnoreCase("I")) {
									
									if (StringUtils.isNotEmpty(finalValueReferal[2]) && !finalValueReferal[2].equals("0")) {
										referralPatientDt.setIntDepartmentId(Long.parseLong(finalValueReferal[2].toString()));
									}
								}
								else {*/
								if (StringUtils.isNotEmpty(finalValueReferal[2]) && !finalValueReferal[2].equals("0")) {
									referralPatientDt.setExtDepartment(finalValueReferal[2]);
								}
								//}
								

								if (finalValueReferal[0] != null &&   StringUtils.isNotBlank(finalValueReferal[0]) && !finalValueReferal[0].equals("0")) {
										
									ReferralPatientHd referralPatientHd = dgOrderhdDao
											.getPatientReferalHdByExtHospitalId(
													Long.parseLong(finalValueReferal[0].trim()),
													Long.parseLong(opdPatientDetailsId.trim()),referToValue);
									
									if (referralPatientHd != null) {
										
									if(StringUtils.isNotEmpty(referToValue) && referToValue.equalsIgnoreCase("I")) {
										referralPatientHd.setIntHospitalId(Long.parseLong(finalValueReferal[0].trim()));
									}	
									else {	
										referralPatientHd.setExtHospitalId(Long.parseLong(finalValueReferal[0].trim()));
									}
									} else {
										referralPatientHd = new ReferralPatientHd();
										
										if(StringUtils.isNotEmpty(referToValue) && referToValue.equalsIgnoreCase("I")) {
											referralPatientHd.setIntHospitalId(Long.parseLong(finalValueReferal[0].trim()));
										}
										else {
										referralPatientHd.setExtHospitalId(Long.parseLong(finalValueReferal[0].trim()));
										}
										referralPatientHd.setStatus("W");
										if(StringUtils.isNotBlank(hospitalId)) {
											Long hospitalIdValue=Long.parseLong(hospitalId);
											referralPatientHd.setMmuId(hospitalIdValue);
										}
										
									}
									
									if(StringUtils.isNotEmpty(referalNotes)) {
										referralPatientHd.setReferralNote(referalNotes);
									}
									if(StringUtils.isNotEmpty(doctorNote)) {
										referralPatientHd.setDoctorNote(doctorNote);
									}
									//if(StringUtils.isNotBlank(referalDates)) {
										//Date referalDate = HMSUtil.convertStringDateToUtilDate(new Date(), "yyyy-MM-dd");
										referralPatientHd.setReferralIniDate(new Date());
									//}
									referralPatientHd.setOpdPatientDetailsId(Long.parseLong(opdPatientDetailsId));
									referralPatientHd.setPatientId(Long.parseLong(patient));
									referralPatientHd.setTreatmentType("E");
									referralPatientHd.setLastChgBy(Long.parseLong(userId));
									referralPatientHd.setDoctorId(Long.parseLong(userId));
									referralPatientHd.setLastChgDate(new Timestamp(date.getTime()));
									Long referalPatientHdId = dgOrderhdDao.saveOrUpdateReferalHd(referralPatientHd);
									referralPatientDt.setRefrealHdId(referalPatientHdId);
								}

							}
							
							referralPatientDt.setLastChgDate(new Timestamp(date.getTime()));
							dgOrderhdDao.saveOrUpdateReferalDt(referralPatientDt);
						}
					}
				}
				counter++;
			}

		/*
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	public static String getReplaceString(String replaceValue) {
		return replaceValue.replaceAll("[\\[\\]]", "");
	}

	public static String getReplaceStringOnlyLastAndFirst(String replaceValue) {
		String value= StringUtils.removeStart(StringUtils.removeEnd(replaceValue, "]"), "[");
		return value;
	}
	public void saveOrUpDatedDgOrderDt(String investigationIdValue, String investigationDate, String labradiologyCheck1,
			String urgent1, String dgOrderDtIdValue, String dgOrderHdIdValue, HashMap<String, Object> payload) {
		//try {

			String[] investigationIdValues = investigationIdValue.split(",");
			String[] investigationDateValues = investigationDate.split(",");
			String[] labradiologyCheck1Values = labradiologyCheck1.split(",");
			String[] urgent1Values = urgent1.split(",");
      
			String[] dgOrderDtIdValues = dgOrderDtIdValue.split(",");
			String[] dgOrderHdIdValues = dgOrderHdIdValue.split(",");
			HashMap<String, String> mapInvestigationMap = new HashMap<>();

			String otherInvestigation = payload.get("otherInvestigation").toString();
			otherInvestigation = getReplaceString(otherInvestigation);
			String departmentId="";
			if(payload.containsKey("departmentId")) {
			  departmentId = payload.get("departmentId").toString();
			departmentId = getReplaceString(departmentId);
			}
			String statusOfInves="";
			if(payload.containsKey("statusOfInves")) {
				statusOfInves = payload.get("statusOfInves").toString();
				statusOfInves = getReplaceString(statusOfInves);
				}
			String[] statusOfInvesArray = statusOfInves.split(",");
			
			String finalValue = "";
			DgOrderhd dgOrderhd = null;
			Integer counter = 1;
			Date date=new Date();
			Long userId=null;
			if(payload.get("userIdVal")!=null) {
				String userIdV = payload.get("userIdVal").toString();
				userIdV = getReplaceString(userIdV);
				userId =Long.parseLong(userIdV.trim());
			}
			
			for (int i = 0; i < investigationIdValues.length; i++) {

				if (StringUtils.isNotEmpty(investigationIdValues[i].toString())
						&& !investigationIdValues[i].equalsIgnoreCase("0")) {
					finalValue += investigationIdValues[i].trim();
					if (!dgOrderDtIdValues[i].equalsIgnoreCase("0") && StringUtils.isNotBlank(dgOrderDtIdValues[i])) {
						for (int m = i; m < dgOrderDtIdValues.length; m++) {
							finalValue += "," + dgOrderDtIdValues[m].trim();
							if (m == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}

					if (i < investigationDateValues.length && !investigationDateValues[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(investigationDateValues[i])) {
						for (int j = i; j < investigationDateValues.length; j++) {
							finalValue += "," + investigationDateValues[j].trim();
							if (j == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}
					if (i < labradiologyCheck1Values.length && StringUtils.isNotBlank(labradiologyCheck1Values[i])) {
						for (int k = i; k < labradiologyCheck1Values.length; k++) {
							finalValue += "," + labradiologyCheck1Values[k].trim();
							if (k == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "O";
					}
					if (i < urgent1Values.length && StringUtils.isNotBlank(urgent1Values[i])) {
						for (int l = i; l < urgent1Values.length; l++) {
							finalValue += "," + urgent1Values[l].trim();
							if (l == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "N";
					}
					
					if (i < statusOfInvesArray.length && StringUtils.isNotBlank(statusOfInvesArray[i])) {
						for (int l = i; l < statusOfInvesArray.length; l++) {
							finalValue += "," + statusOfInvesArray[l].trim();
							if (l == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "P";
					}
					
					mapInvestigationMap.put(investigationIdValues[i].trim() + "@#" + counter, finalValue);
					finalValue = "";
					counter++;
				}

			}
			counter = 1;
			for (String investigationId : investigationIdValues) {
				if (StringUtils.isNotEmpty(investigationId)) {
					if (mapInvestigationMap.containsKey(investigationId.trim() + "@#" + counter)) {
						String investigationValue = mapInvestigationMap.get(investigationId.trim() + "@#" + counter);

						if (StringUtils.isNotEmpty(investigationValue)) {

							String[] finalValueInvestigation = investigationValue.split(",");
							DgOrderdt dgOrderdt = null;
							if (finalValueInvestigation[1] != null && StringUtils.isNotBlank(finalValueInvestigation[1] ) && !finalValueInvestigation[1].equalsIgnoreCase("0")
									&& StringUtils.isNotBlank(finalValueInvestigation[1])) {
								dgOrderdt = dgOrderhdDao
										.getDgOrderDtByDgOrderdtId(Long.parseLong(finalValueInvestigation[1]));
							} else {
								dgOrderdt = new DgOrderdt();
								if(StringUtils.isNotEmpty(finalValueInvestigation[5]))
									dgOrderdt.setOrderStatus(finalValueInvestigation[5].toString());
								
								dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
								dgOrderdt.setLastChgBy(userId);
							}

							if (finalValueInvestigation != null) {

								if (dgOrderHdIdValues != null && dgOrderHdIdValues[0] != null
										&& StringUtils.isNotBlank(dgOrderHdIdValues[0])) {
									dgOrderdt.setOrderhdId(Long.parseLong(dgOrderHdIdValues[0].toString()));

									dgOrderhd = dgOrderhdDao
											.getDgOrderhdByDgOrderhdId(Long.parseLong(dgOrderHdIdValues[0].toString()));
									if(StringUtils.isNotEmpty(otherInvestigation)) {
										//dgOrderhd.setOtherInvestigation(otherInvestigation);
									}
									if(dgOrderhd!=null && dgOrderhd.getOrderhdId()==null)
										dgOrderhd.setOrderStatus("P");
									
									dgOrderhd.setLastChgBy(userId);
									dgOrderhd.setLastChgDate(new Timestamp(date.getTime()));
									dgOrderhd.setDoctorId(userId);
									if(StringUtils.isNotEmpty(departmentId))
									dgOrderhd.setDepartmentId(Long.parseLong(departmentId));
									dgOrderhdDao.saveOrUpdateDgOrderHd(dgOrderhd);
								} else {
									Date orderDate = HMSUtil.getTodayFormattedDate();
									String visitId = payload.get("VisitID").toString();
									visitId = getReplaceString(visitId);
									DgOrderhd dgOrderDt=dgOrderhdDao.getDgOrderHdByVisitId(Long.parseLong(visitId.trim()),orderDate);
									Long dgOrderHdId=null;
									
									if(dgOrderDt!=null) {
										dgOrderHdId=dgOrderDt.getOrderhdId();
										
									}
									else {
										String hospitalId="";
										dgOrderhd = new DgOrderhd();
										if(payload.get("hospitalId")!=null) {
										  hospitalId = payload.get("hospitalId").toString();
										  hospitalId = getReplaceString(hospitalId);
										}
										String patientId = payload.get("patientId").toString();
										patientId = getReplaceString(patientId);

										dgOrderhd.setVisitId(Long.parseLong(visitId.trim()));
										if (StringUtils.isNotBlank(hospitalId))
											dgOrderhd.setMmuId(Long.parseLong(hospitalId.trim()));
										if (StringUtils.isNotBlank(patientId))
											dgOrderhd.setPatientId(Long.parseLong(patientId));
										//dgOrderhd.setOtherInvestigation(otherInvestigation);
										
										try {
											if(dgOrderhd!=null && dgOrderhd.getOrderhdId()==null)
												dgOrderhd.setOrderStatus("P");
											dgOrderhd.setLastChgDate(new Timestamp(date.getTime()));
										}
										catch(Exception e) {e.printStackTrace();}
										
										if (StringUtils.isNotEmpty(finalValueInvestigation[2])
												&& !finalValueInvestigation[2].equals("0")) {
											try {
												dgOrderhd.setOrderDate(
														HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[2].toString()));
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} 
										}
										dgOrderhd.setLastChgBy(userId);
										dgOrderhd.setLastChgDate(new Timestamp(date.getTime()));
										dgOrderhd.setDoctorId(userId);
										if(StringUtils.isNotEmpty(departmentId))
										dgOrderhd.setDepartmentId(Long.parseLong(departmentId));
										dgOrderHdId = dgOrderhdDao.saveOrUpdateDgOrderHd(dgOrderhd);
									}
									dgOrderdt.setOrderhdId(dgOrderHdId);

								}

								if (StringUtils.isNotEmpty(finalValueInvestigation[0]) && !finalValueInvestigation[0].equals("0"))
									dgOrderdt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].toString()));

								if (StringUtils.isNotEmpty(finalValueInvestigation[2])
										&& !finalValueInvestigation[2].equals("0")) {
									try {
										dgOrderdt.setOrderDate(
												HMSUtil.convertStringTypeDateToDateType(finalValueInvestigation[2].toString()));
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								if (finalValueInvestigation[3] != null &&  StringUtils.isNotBlank(finalValueInvestigation[3]) && !finalValueInvestigation[3].equals("0"))
									dgOrderdt.setLabMark(finalValueInvestigation[3].toString());
								else {
									dgOrderdt.setLabMark("O");
								}

							}
							Date date1=new Date();
							dgOrderdt.setLastChgDate(new Timestamp(date1.getTime()));
							dgOrderhdDao.saveOrUpdateDgOrderdt(dgOrderdt);
						}
					}
					counter++;
				}

			}

		/*
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	// Save opdate MassStoreItems

	public void saveOrUpDatedMassStoreItems(String items, String dispUnit, String dosages, String frequencys,
			String days, String totals, String instructions, String stocks, String precriptionDtId,
			String precriptionHdId, HashMap<String, Object> payload,String statusOfNiv) {
		
		String itemIdWhenImmunization = "";
		String hospitalId = "";
		String patientId = "", visitId = "";
		hospitalId = payload.get("hospitalId").toString();
		hospitalId = getReplaceString(hospitalId);
		patientId = payload.get("patientId").toString();
		patientId = getReplaceString(patientId);

		visitId = payload.get("VisitID").toString();
		visitId = getReplaceString(visitId);

		String[] itemsValue = items.split(",");

		String[] dispUnitValue = dispUnit.split(",");
		String[] dosagesValue = dosages.split(",");
		String[] frequencysValue = frequencys.split(",");

		String[] daysValue = days.split(",");
		String[] totalsValue = totals.split(",");

		String[] instructionsValue = instructions.split(",");
		String[] stocksValue = stocks.split(",");
		String[] precriptionHdIdValue = precriptionHdId.split(",");

		String[] precriptionDtIdValue = precriptionDtId.split(",");
		String[] statusValue = statusOfNiv.split(",");
		
		

		HashMap<String, String> mapInvestigationMap = new HashMap<>();

		Integer counter = 1;
		String finalValue = "";
		Long userId = null;
		Date date = new Date();
		if (payload.get("userIdVal") != null) {
			String userIdV = payload.get("userIdVal").toString();
			userIdV = getReplaceString(userIdV);
			userId = Long.parseLong(userIdV);
		}
		for (int i = 0; i < itemsValue.length; i++) {

			if (StringUtils.isNotEmpty(itemsValue[i].toString()) && !itemsValue[i].equalsIgnoreCase("0")) {

				finalValue += itemsValue[i].trim();

				if (i < precriptionDtIdValue.length && !precriptionDtIdValue[i].equalsIgnoreCase("0")
						&& StringUtils.isNotBlank(precriptionDtIdValue[i])) {
					for (int m = i; m < precriptionDtIdValue.length; m++) {
						finalValue += "," + precriptionDtIdValue[m].trim();
						if (m == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < dispUnitValue.length && !dispUnitValue[i].equalsIgnoreCase("0")
						&& StringUtils.isNotBlank(dispUnitValue[i])) {
					for (int j = i; j < dispUnitValue.length; j++) {
						finalValue += "," + dispUnitValue[j].trim();
						if (j == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < dosagesValue.length && StringUtils.isNotBlank(dosagesValue[i])) {
					for (int k = i; k < dosagesValue.length; k++) {
						finalValue += "," + dosagesValue[k].trim();
						if (k == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}
				if (i < frequencysValue.length && StringUtils.isNotBlank(frequencysValue[i])) {
					for (int l = i; l < frequencysValue.length; l++) {
						finalValue += "," + frequencysValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < daysValue.length && StringUtils.isNotBlank(daysValue[i])) {
					for (int l = i; l < daysValue.length; l++) {
						finalValue += "," + daysValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}
				
				if (i < totalsValue.length && StringUtils.isNotBlank(totalsValue[i])) {
					for (int l = i; l < totalsValue.length; l++) {
						finalValue += "," + totalsValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < instructionsValue.length && StringUtils.isNotBlank(instructionsValue[i])) {
					for (int l = i; l < instructionsValue.length; l++) {
						finalValue += "," + instructionsValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < stocksValue.length && StringUtils.isNotBlank(stocksValue[i])) {
					for (int l = i; l < stocksValue.length; l++) {
						finalValue += "," + stocksValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < statusValue.length && StringUtils.isNotBlank(statusValue[i])) {
					for (int l = i; l < statusValue.length; l++) {
						finalValue += "," + statusValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				mapInvestigationMap.put(itemsValue[i].trim() + "@#" + counter, finalValue);
				finalValue = "";
				counter++;
			}
		}
		counter = 1;
		PatientPrescriptionHd patientPrescriptionHd = null;
		for (String item : itemsValue) {
			if (StringUtils.isNotEmpty(item)) {
				if (mapInvestigationMap.containsKey(item.trim() + "@#" + counter)) {
					String itemValue = mapInvestigationMap.get(item.trim() + "@#" + counter);

					if (StringUtils.isNotEmpty(itemValue)) {

						String[] finalValueItem = itemValue.split(",");
						PatientPrescriptionDt patientPrescriptionDt = null;
						if (finalValueItem[1] != null && !finalValueItem[1].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(finalValueItem[1])) {
							patientPrescriptionDt = dgOrderhdDao
									.getMasStoreItemByPatientPrecriptionDtId(Long.parseLong(finalValueItem[1]));
							if (patientPrescriptionDt == null) {
								patientPrescriptionDt = new PatientPrescriptionDt();
							}

						} else {
							patientPrescriptionDt = new PatientPrescriptionDt();
						}

						if (finalValueItem != null) {
							if (finalValueItem[0] != null && StringUtils.isNotEmpty(finalValueItem[0])
									&& !finalValueItem[0].equals("0")) {
								patientPrescriptionDt.setItemId(Long.parseLong(finalValueItem[0].trim().toString()));

								if (finalValueItem[2] != null && StringUtils.isNotEmpty(finalValueItem[2])
										&& !finalValueItem[2].equals("0")) {
									// patientPrescriptionDt.setDispStock(Long.parseLong(finalValueItem[2]));
									MasStoreItem msitDispUpdate = null;
									msitDispUpdate = dgOrderhdDao.getMasStoreItemByItemId(
											Long.parseLong((finalValueItem[0].trim().toString())));
									msitDispUpdate.setDispUnitId(Long.parseLong(finalValueItem[2].trim()));
									dgOrderhdDao.saveOrUpdateMasStoreItem(msitDispUpdate);
								}

								if (finalValueItem[3] != null && StringUtils.isNotEmpty(finalValueItem[3])
										&& !finalValueItem[3].equals("0"))
									patientPrescriptionDt.setDosage(finalValueItem[3]);

								if (finalValueItem[4] != null && StringUtils.isNotEmpty(finalValueItem[4])
										&& !finalValueItem[4].equals("0")) {
									String symptomId=finalValueItem[4].toString();
									String[] parts=symptomId.split("@");
									String part1 = parts[1];
									patientPrescriptionDt.setFrequencyId(Long.parseLong(part1));
								}

								if (finalValueItem[5] != null && StringUtils.isNotEmpty(finalValueItem[5])
										&& !finalValueItem[5].equals("0")) {
									patientPrescriptionDt.setNoOfDays(Long.parseLong(finalValueItem[5]));
								}

								if (finalValueItem[6] != null && StringUtils.isNotEmpty(finalValueItem[6])
										&& !finalValueItem[6].equals("0") && !finalValueItem[6].equals("NaN")) {
									patientPrescriptionDt.setTotal(Long.parseLong(finalValueItem[6]));
								}
								if (finalValueItem[7] != null && StringUtils.isNotEmpty(finalValueItem[7])
										&& !finalValueItem[7].equals("0")) {

									patientPrescriptionDt.setInstruction(finalValueItem[7]);

								}
								if (finalValueItem[8] != null && StringUtils.isNotEmpty(finalValueItem[8])
										&& !finalValueItem[8].equals("0") && !finalValueItem[8].equals("0/0")) {
									patientPrescriptionDt.setStoreStock(Long.parseLong(finalValueItem[8]));
								}

								if (precriptionHdIdValue[0] != null && StringUtils.isNotBlank(precriptionHdIdValue[0]) && (finalValueItem[9].equals("C")||finalValueItem[9].equals("P"))) {
									patientPrescriptionDt.setPrescriptionHdId(Long.parseLong(precriptionHdIdValue[0].trim().toString()));
									patientPrescriptionHd = dgOrderhdDao.getPatientPrecriptionHdByPPHdId(
											Long.parseLong(precriptionHdIdValue[0].trim().toString()));
									// patientPrescriptionHd.setOtherTreatment(recommendedMedicalAdvice);
									patientPrescriptionHd.setLastChgBy(userId);
									patientPrescriptionHd.setPrescriptionDate(new Timestamp(date.getTime()));

									dgOrderhdDao.saveOrUpdatePatientPrescriptionHd(patientPrescriptionHd);
								} else {
                                   
									patientPrescriptionHd = dgOrderhdDao.getPatientPrecriptionHdByVisitId(Long.parseLong(visitId.trim()));
									Long patientPrescriptionHdId = null;
									if (patientPrescriptionHd != null ) {
										patientPrescriptionHdId = patientPrescriptionHd.getPrescriptionHdId();
										// patientPrescriptionHd.setOtherTreatment(recommendedMedicalAdvice);

									} else {
										patientPrescriptionHd = new PatientPrescriptionHd();
										patientPrescriptionHd.setLastChgBy(userId);
										patientPrescriptionHd.setLastChgDate(new Timestamp(date.getTime()));
										patientPrescriptionHd.setDoctorId(userId);

										patientPrescriptionHd.setVisitId(Long.parseLong(visitId.trim()));
										// patientPrescriptionHd.setOtherTreatment(recommendedMedicalAdvice);

										String opdPatientDetaiId = payload.get("opdPatientDetailId").toString();
										opdPatientDetaiId = getReplaceString(opdPatientDetaiId);
										patientPrescriptionHd.setOpdPatientDetailsId(Long.parseLong(opdPatientDetaiId));
										patientPrescriptionHd.setPrescriptionDate(new Timestamp(date.getTime()));
										/*
										 * } catch(Exception e) { e.printStackTrace(); }
										 */
										if (patientPrescriptionHd != null
												&& patientPrescriptionHd.getPrescriptionHdId() == null)
											patientPrescriptionHd.setStatus("P");
										//patientPrescriptionHd.setInjectionStatus("N");

										if (StringUtils.isNotBlank(hospitalId))
											patientPrescriptionHd.setMmuId(Long.parseLong(hospitalId.trim()));
										if (StringUtils.isNotBlank(patientId))
											patientPrescriptionHd.setPatientId(Long.parseLong(patientId));
									}
									patientPrescriptionHd.setLastChgDate(new Timestamp(date.getTime()));
									if(!finalValueItem[9].equals("C"))
									{	
									patientPrescriptionHdId = dgOrderhdDao.saveOrUpdatePatientPrescriptionHd(patientPrescriptionHd);
									}
									patientPrescriptionDt.setPrescriptionHdId(patientPrescriptionHdId);
								}
								patientPrescriptionDt.setLastChgDate(new Timestamp(date.getTime()));
								/*if (finalValueItem[11] != null && StringUtils.isNotEmpty(finalValueItem[11])
										&& !finalValueItem[11].equals("0")) {*/
									patientPrescriptionDt.setStatus("P");
								//}
									if( !finalValueItem[9].equals("C"))
									{	
										dgOrderhdDao.saveOrUpdatePatientPrecriptionDt(patientPrescriptionDt);
									}
							}
						}
					}
				}
			}
			counter++;
		}

		//saveUpdatePatientImmunizationHistory(itemIdWhenImmunization, visitId, patientId, userId);
	}
	
	
	/*@Transactional
	public void saveUpdatePatientImmunizationHistory(String itemIds,String visitId,String patientId,Long userId) {
		
		String []itemsVal=itemIds.split(",");
		PatientImmunizationHistory patientImmunizationHistory=null;
		for(String ss:itemsVal) {
			
			if(StringUtils.isNotEmpty(ss)) {
				patientImmunizationHistory=medicalExamDAO.getPatientImmunizationHistoryByVisit(Long.parseLong(visitId),Long.parseLong(ss),Long.parseLong(patientId));
			}
			if(StringUtils.isNotEmpty(ss)) {
			if(patientImmunizationHistory==null) {
				patientImmunizationHistory=new PatientImmunizationHistory();
				patientImmunizationHistory.setPrescriptionDate(new Date());
				patientImmunizationHistory.setVisitId(Long.parseLong(visitId));
				patientImmunizationHistory.setLastChgBy(userId);
				patientImmunizationHistory.setPatientId(Long.parseLong(patientId));
				patientImmunizationHistory.setItemId(Long.parseLong(ss));
				Date date=new Date();		
				patientImmunizationHistory.setLastChgDate(new Timestamp(date.getTime()));
				medicalExamDAO.saveOrUpdatePatientImmunizationHistory(patientImmunizationHistory);
			}
			}
		}
	}*/
	
	
	@Transactional
	public void saveUpdateOpdPatientDetail(HashMap<String, Object> obj) {
		String opdPatientDetailId = "";
		if (obj.get("opdPatientDetailId") != null) {
			opdPatientDetailId = obj.get("opdPatientDetailId").toString();
			opdPatientDetailId = getReplaceString(opdPatientDetailId);
		}
		
				
		Long opdPatientDetailIdValue = null;
		if (opdPatientDetailId != null) {
			opdPatientDetailIdValue = Long.parseLong(opdPatientDetailId.trim());
		}
		OpdPatientDetail opdPatientDetail = null;
	
		if (opdPatientDetailIdValue != null) {
			opdPatientDetail = opdPatientDetailDao.getOpdPatientDetailsByOpdPatientDetailId(opdPatientDetailIdValue);
		}
		if (opdPatientDetail == null) {
			opdPatientDetail = new OpdPatientDetail();
		}
		/*String recommendedMedicalAdvice = "";
		if (obj.get("recommendedMedicalAdvice") != null) {
			recommendedMedicalAdvice = obj.get("recommendedMedicalAdvice").toString();
			recommendedMedicalAdvice = getReplaceString(recommendedMedicalAdvice);
			opdPatientDetail.setRecmndMedAdvice(recommendedMedicalAdvice);
		}*/
		
		if (obj.get("itemId") != null) {
			List<String> itemId = (List<String>) obj.get("itemId");
			if(itemId.get(0).isEmpty()) {
				opdPatientDetail.setDispensaryFlag("N");
			}
			else
			{
				opdPatientDetail.setDispensaryFlag("Y");
			}
		}
		if (obj.get("investigationIdValue") != null) {
			List<String> dgOrderHdId = (List<String>) obj.get("investigationIdValue");
			if(dgOrderHdId.get(0).isEmpty()) {
				opdPatientDetail.setLabFlag("N");
			}
			else
			{
				opdPatientDetail.setLabFlag("Y");
			}
		}
		if (obj.get("referralForNew") != null) {
			String referralForNew =obj.get("referralForNew").toString();
			referralForNew = getReplaceString(referralForNew);
			if(!referralForNew.equals("1")) {
				opdPatientDetail.setReerralFlag("N");
			}
			else
			{
				opdPatientDetail.setReerralFlag("Y");
			}
		}	
		
		
		
		String otherInvestigation = "";
		if (obj.get("otherInvestigation") != null) {
			otherInvestigation = obj.get("otherInvestigation").toString();
			otherInvestigation = getReplaceString(otherInvestigation);
			opdPatientDetail.setOtherInvestigation(otherInvestigation);
		}
		
		String doctorRemarksArrayVal = "";
		if (obj.get("doctorRemarksArrayVal") != null) {
			doctorRemarksArrayVal = obj.get("doctorRemarksArrayVal").toString();
			doctorRemarksArrayVal = getReplaceString(doctorRemarksArrayVal);
			opdPatientDetail.setRecmmdMedAdvice(doctorRemarksArrayVal);
		}
		
		String markMLC = "";
		if (obj.get("markAsMlcFlag") != null) {
			markMLC = obj.get("markAsMlcFlag").toString();
			markMLC = getReplaceString(markMLC);
			if(markMLC.equals("Y"))
			{	
			opdPatientDetail.setMlcFlag("Y");
			}
			else
			{
				opdPatientDetail.setMlcFlag("N");
			}
		}
		
		String mlcPloiceStation = "";
		if (obj.get("mlcPloiceStation") != null) {
			mlcPloiceStation = obj.get("mlcPloiceStation").toString();
			mlcPloiceStation = getReplaceString(mlcPloiceStation);
			opdPatientDetail.setPoliceStation(mlcPloiceStation);
		}
		
		String mlcTreatedAs = "";
		if (obj.get("mlcTreatedAs") != null) {
			mlcTreatedAs = obj.get("mlcTreatedAs").toString();
			mlcTreatedAs = getReplaceString(mlcTreatedAs);
			opdPatientDetail.setTreatedAs(mlcTreatedAs);
		}
		
		String mlcPloiceName = "";
		if (obj.get("mlcPloiceName") != null) {
			mlcPloiceName = obj.get("mlcPloiceName").toString();
			mlcPloiceName = getReplaceString(mlcPloiceName);
			opdPatientDetail.setPoliceName(mlcPloiceName);
		}
		
		String mlcDesignation = "";
		if (obj.get("mlcDesignation") != null) {
			mlcDesignation = obj.get("mlcDesignation").toString();
			mlcDesignation = getReplaceString(mlcDesignation);
			opdPatientDetail.setDesignation(mlcDesignation);
		}
		
		String mlcIdNumber = "";
		if (obj.get("mlcIdNumber") != null) {
			mlcIdNumber = obj.get("mlcIdNumber").toString();
			mlcIdNumber = getReplaceString(mlcIdNumber);
			opdPatientDetail.setIdNumber(mlcIdNumber);
		}
		
		String followUpFlag = "";
		if (obj.get("followUpFlagValRecall") != null) {
			followUpFlag = obj.get("followUpFlagValRecall").toString();
			followUpFlag = getReplaceString(followUpFlag);
			if(followUpFlag=="Y")
			{	
			opdPatientDetail.setFollowUpFlag("Y");
			}
			else
			{
				opdPatientDetail.setFollowUpFlag("N");
			}
		}
		
		String followUpChecked = "";
		if (obj.get("followUpChecked") != null && !obj.get("followUpChecked").equals("N")) {
			followUpChecked = obj.get("followUpChecked").toString();
			followUpChecked = getReplaceString(followUpChecked);
			opdPatientDetail.setFollowUpFlag(followUpChecked);
		}
		
		String nextFollowUpDate = "";
		if (obj.get("nextFollowUpDate") != null) {
			List<String> list = (List<String>) obj.get("nextFollowUpDate");
			if(!list.get(0).equals("")) {
				nextFollowUpDate = list.get(0);
				Date d=HMSUtil.convertStringTypeDateToDateType(nextFollowUpDate);
				opdPatientDetail.setFollowupDate(d);
			}
			
			//nextFollowUpDate = getReplaceString(nextFollowUpDate);
			opdPatientDetail.setFollowUpFlag(followUpChecked);
		}
		String noOfDaysFollowUp = "";
		if (obj.get("noOfDaysFollowUp") != null) {
			noOfDaysFollowUp = getReplaceString(obj.get("noOfDaysFollowUp").toString());
			if(!noOfDaysFollowUp.equals(""))
			{	
			if(!noOfDaysFollowUp.equals("16"))
			{
				opdPatientDetail.setFollowUpDays(Long.parseLong(noOfDaysFollowUp));
			}
			else
			{
				opdPatientDetail.setFollowUpDays((long) 3);
			}
		 }
		}
	
		Date date = new Date();
		opdPatientDetail.setLastChgDate(new Timestamp(date.getTime()));
		
		if (obj.get("Address") != null) {
			String address = obj.get("Address").toString();
			address = getReplaceString(address);
		}
		Long patientIdValue = null;
		String patientId = "";
		if (obj.get("patientId") != null) {
			patientId = obj.get("patientId").toString();
			patientId = getReplaceString(patientId);
			patientIdValue = Long.parseLong(patientId.trim());
			opdPatientDetail.setPatientId(patientIdValue);
			// Patient patient = opdPatientDetailDao.getPatientByPatientId(patientIdValue);
		}

		
		if (obj.get("height") != null) {
			String height = obj.get("height").toString();
			height = getReplaceString(height.trim());
			opdPatientDetail.setHeight(height);
		}
		
		

		if (obj.get("patientId") != null) {
			String PatientIDValue = getReplaceString(obj.get("patientId").toString());
			if (StringUtils.isNotBlank(PatientIDValue)) {
				Long PatientID = Long.parseLong(PatientIDValue.trim());
				opdPatientDetail.setPatientId(PatientID);
			}
		}

		if (obj.get("rr") != null) {
			String rr = obj.get("rr").toString();
			rr = getReplaceString(rr.trim());
			opdPatientDetail.setRr(rr);
		}

		
		

		if (obj.get("bp") != null) {
			String bp = obj.get("bp").toString();
			bp = getReplaceString(bp.trim());
			opdPatientDetail.setBpSystolic(bp);
		}
		if (obj.get("bp1") != null) {
			String bp1 = obj.get("bp1").toString();
			bp1 = getReplaceString(bp1.trim());
			opdPatientDetail.setBpDiastolic(bp1);
		}
		//OpdPatientHistory opdPatientHistory = null;
		String visitID = "";
		if (obj.get("VisitID") != null) {
			visitID = getReplaceString(obj.get("VisitID").toString());
			// OpdPatient History
			//opdPatientHistory = opdPatientDetailDao.getOpdPatientHistoryByVisitId(Long.parseLong(visitID));
		}
		String allergyHistory = "";
		String userId = "";
		String mmuId = null;
		if (obj.get("userIdVal") != null) {
			userId = obj.get("userIdVal").toString();
			userId = getReplaceString(userId);
		}
		
		
		if (obj.get("bmi") != null) {
			String bmi = obj.get("bmi").toString();
			bmi = getReplaceString(bmi.trim());
			opdPatientDetail.setBmi(bmi);
		}

		
		
		if (obj.get("tempature") != null) {
			String temperature = obj.get("tempature").toString();
			temperature = getReplaceString(temperature.trim());
			opdPatientDetail.setTemperature(temperature);
		}
		/*if (obj.get("workingdiagnosis") != null) {
			String workingdiagnosis = obj.get("workingdiagnosis").toString();
			workingdiagnosis = getReplaceString(workingdiagnosis.trim());
			opdPatientDetail.setWorkingDiagnosis(workingdiagnosis);
		}*/
		if (obj.get("pastMedicalHistory") != null) {
			String pastMedicalHistory = obj.get("pastMedicalHistory").toString();
			pastMedicalHistory = getReplaceString(pastMedicalHistory.trim());
			opdPatientDetail.setPastHistory(pastMedicalHistory);
		}
		
	
		if(obj.get("variant_in_weight")!=null  ) {
			String variant_in_weight = obj.get("variant_in_weight").toString();
			variant_in_weight = getReplaceString(variant_in_weight.trim());
				if(StringUtils.isNotBlank(variant_in_weight))
				opdPatientDetail.setVaration(new Double(variant_in_weight));
				}
	

		// Working on ICgDiagonosis

		/*
		 * if(obj.get("diagonsisText")!=null) { String icdDiagnosis =
		 * obj.get("diagonsisText").toString(); icdDiagnosis =
		 * getReplaceStringOnlyLastAndFirst(icdDiagnosis.trim());
		 * opdPatientDetail.setIcdDiagnosis(icdDiagnosis); }
		 */

		if (obj.get("icdDiagnosisValeText") != null) {
			String icdDiagnosis = obj.get("icdDiagnosisValeText").toString();
			icdDiagnosis = getReplaceStringOnlyLastAndFirst(icdDiagnosis.trim());
			opdPatientDetail.setIcdDiagnosis(icdDiagnosis);
		}

		// Dischage IcdUpdate
		if (obj.get("diagnosisIdvalRecall") != null) {

			String icdIdValue = obj.get("diagnosisIdvalRecall").toString();
			icdIdValue = getReplaceString(icdIdValue.trim());
			// opdPatientDetail.setIcdDiagnosis(icdDiagnosis);
			String[] icdCodeArray = icdIdValue.split(",");

			if (obj.get("userIdVal") != null) {
				userId = obj.get("userIdVal").toString();// multiple value
				userId = getReplaceString(userId.trim());
			}
			if (icdIdValue != "" && StringUtils.isNotEmpty(visitID) && patientIdValue != null
					&& opdPatientDetailIdValue != null)
				dgOrderhdDao.updateAndInsertDischargeICDCode(icdCodeArray, Long.parseLong(visitID), patientIdValue,
						opdPatientDetailIdValue, userId);

		}
		
		if (obj.get("patientSymptonsValeText") != null) {
			String patintSymtons = obj.get("patientSymptonsValeText").toString();
			patintSymtons = getReplaceStringOnlyLastAndFirst(patintSymtons.trim());
			opdPatientDetail.setPatientSymptoms(patintSymtons);
		}

		// Dischage IcdUpdate
		if (obj.get("patientSympotnsValue") != null && !obj.get("patientSympotnsValue").equals("")) {

			String patientSympotnsValueCheck = obj.get("patientSympotnsValue").toString();
			patientSympotnsValueCheck = getReplaceString(patientSympotnsValueCheck.trim());
			// opdPatientDetail.setIcdDiagnosis(icdDiagnosis);
			String[] patientSympotnsValueArray = patientSympotnsValueCheck.split(",");

			if (obj.get("userIdVal") != null) {
				userId = obj.get("userIdVal").toString();// multiple value
				userId = getReplaceString(userId.trim());
			}
			if (obj.get("hospitalId") != null) {
				mmuId = obj.get("hospitalId").toString();// multiple value
				mmuId = getReplaceString(mmuId.trim());
			}
			if (patientSympotnsValueCheck!="" && !patientSympotnsValueCheck.contentEquals("abc") && StringUtils.isNotEmpty(visitID) && patientIdValue != null
					&& opdPatientDetailIdValue != null)
				dgOrderhdDao.updateAndInsertpatientSympotnsValue(patientSympotnsValueArray, Long.parseLong(visitID), patientIdValue,
						opdPatientDetailIdValue, userId, Long.parseLong(mmuId));

		}

		// String nomenclature1 = obj.get("nomenclature1").toString();
		if (obj.get("spo2") != null) {
			String spo2 = obj.get("spo2").toString();
			spo2 = getReplaceString(spo2.trim());
			opdPatientDetail.setSpo2(spo2);
		}
		if (obj.get("pulse") != null) {
			String pulse = obj.get("pulse").toString();// multiple value
			pulse = getReplaceString(pulse.trim());
			opdPatientDetail.setPulse(pulse);
		}

		if (obj.get("ideal_weight") != null) {
			String idealWeight = obj.get("ideal_weight").toString();
			idealWeight = getReplaceString(idealWeight.trim());
			opdPatientDetail.setIdealWeight(idealWeight);
		}
		if (obj.get("Weight") != null) {
			String Weight = obj.get("Weight").toString();
			Weight = getReplaceString(Weight.trim());
			opdPatientDetail.setWeight(Weight);
		}

		opdPatientDetail = saveOpdDisposableDetails(obj, opdPatientDetail, patientId, visitID, userId);

	
		////////////////////////////////////////////////////////////

		updateOpdPatientDetail(opdPatientDetail);
	}

	public Long updatePatient(Patient patient) {
		return dgOrderhdDao.saveOrUpdatePatient(patient);
	}

	/*public Long updatePatientHistory(OpdPatientHistory opdPatientHistory) {
		return dgOrderhdDao.updatePatientHistory(opdPatientHistory);
	}*/

	public Long updateOpdPatientDetail(OpdPatientDetail opdPatientDetail) {
		return dgOrderhdDao.updateOpdPatientDetail(opdPatientDetail);
	}

	@Override
	public String getPatientHistoryDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		/*List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.checkPatientVisit(jsondata);
			if (nullbalankvalidation.optString("status").equals("0")) {
				return nullbalankvalidation.toString();
			} else {
				//List<OpdPatientHistory> listOpdPatientHistory = null;
				List<PatientImpantHistory> listPatientImpantHistory = null;
				
				if (jsondata.get("patientId") != null) {
					  listPatientImpantHistory= dgOrderhdDao.getPatientHistoryImpByPatientId(Long.parseLong(jsondata.get("patientId").toString()));
				}
				
				
				}
			}
		}*/
		return json.toString();

	}

	@Override
	public String getPatientReferalDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<MasEmpanelledHospital> masEmpanelledHospitalList = null;
		List<MasHospital> masMasHospitalList = null;
		List<MasIcd>listMasIcd=null;
		Long visitId=null;
		Long patientId=null; 
		Long opdPatientDetailId=null;
		if (!jsondata.isEmpty()) {
			List<Object[]> listReferralPatientDt = null;
			if (jsondata.get("opdPatientDetailId") != null && jsondata.get("opdPatientDetailId")!="") {
				listReferralPatientDt = dgOrderhdDao
						.getReferralPatientDtList(Long.parseLong(jsondata.get("opdPatientDetailId").toString()));

				
				//if(jsondata.get("flagForRefer")!=null && jsondata.get("flagForRefer").equalsIgnoreCase("In")) {
					masMasHospitalList= md.getHospitalList();
				/*}
				else {*/
				masEmpanelledHospitalList = md.getEmpanelledHospital(jsondata);
				//}
				try {
				if(jsondata.get("visitId")!=null) {
					visitId=Long.parseLong(jsondata.get("visitId").toString());
				}
				if(jsondata.get("opdPatientDetailId")!=null) {
					opdPatientDetailId=Long.parseLong(jsondata.get("opdPatientDetailId").toString());
				}
				
				if(jsondata.get("patientId")!=null) {
					patientId=Long.parseLong(jsondata.get("patientId").toString());
				}
				 //listMasIcd= dgOrderhdDao.getMasIcdByVisitPatAndOpdPD(visitId,patientId,opdPatientDetailId);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			if (listReferralPatientDt != null) {
				try {
					Long masEmpanalId = null;
					String masEmpanalName = "";
					Long masDepatId = null;
					String massDeptName = "";
					Long diagonisId = null;
					String daiganosisName = "";
					String instruction = "";
					Long referalPatientDt = null;
					Long referalPatientHd = null;
					String exDepartmentValue = "";
					String masCode="";
					String referalNotes="";
					String referalDate="";
					Long intDepartmentId=null; 
					Long intHospitalId=null;
					String doctorNote="";
					
					for (Iterator<?> it = listReferralPatientDt.iterator(); it.hasNext();) {
						Object[] row = (Object[]) it.next();

						HashMap<String, Object> pt = new HashMap<String, Object>();

						if (row[0] != null) {
							masEmpanalId = Long.parseLong(row[0].toString());
						}
						if (row[1] != null) {
							masEmpanalName = row[1].toString();
						}
						if (row[2] != null) {
							masDepatId = Long.parseLong(row[2].toString());
						}
						if (row[3] != null) {
							massDeptName = row[3].toString();
						}

						 
						  if (row[4] != null) { diagonisId = Long.parseLong(row[4].toString()); }
						  
						if (row[5] != null) {
							daiganosisName = row[5].toString();
						}
						if (row[6] != null) {
							instruction = row[6].toString();
						}
						if (row[7] != null) {
							referalPatientDt = Long.parseLong(row[7].toString());
						}
						if (row[8] != null) {
							referalPatientHd = Long.parseLong(row[8].toString());
						}

						if (row[9] != null) {
							exDepartmentValue = row[9].toString();
						}
						if (row[10] != null) {
							masCode = row[10].toString();
						}
						
						if (row[11] != null) {
							referalNotes = row[11].toString();
						}
						if (row[13] != null) {
							referalDate = row[13].toString();
							Date dd1 = HMSUtil.dateFormatteryyyymmdd(referalDate);
							referalDate = HMSUtil.getDateWithoutTime(dd1);
						}
						
						if (row[14] != null) {
							intDepartmentId = Long.parseLong(row[14].toString());
						}
						if (row[15] != null) {
							intHospitalId = Long.parseLong(row[15].toString());
						}
						if (row[16] != null) {
							doctorNote = row[16].toString();
						}
						
						pt.put("masEmpanalId", masEmpanalId);
						pt.put("masEmpanalName", masEmpanalName);
						pt.put("masDepatId", masDepatId);
						pt.put("massDeptName", massDeptName);
						pt.put("diagonisId", diagonisId);
						pt.put("daiganosisName", daiganosisName);
						pt.put("instruction", instruction);

						pt.put("referalPatientDt", referalPatientDt);
						pt.put("referalPatientHd", referalPatientHd);
						pt.put("exDepartmentValue", exDepartmentValue);
						pt.put("masCode", masCode);
						pt.put("referalNotes", referalNotes);
						pt.put("referalDate", referalDate);
						pt.put("intDepartmentId", intDepartmentId);
						pt.put("intHospitalId", intHospitalId);
						pt.put("doctorNote", doctorNote);
						c.add(pt);
					}
					json.put("listReferralPatientDt", c);
				
					List<MasDepartment> departmentList = masterDao.getDepartmentsList();
					
					//	if(jsondata.get("flagForRefer")!=null && jsondata.get("flagForRefer").equalsIgnoreCase("In")) {
						json.put("masMasHospitalList", masMasHospitalList);
					/*}
					else {*/
					json.put("masEmpanelledHospitalList", masEmpanelledHospitalList);
					//}
					json.put("departmentList", departmentList);
					
					json.put("listMasIcd", listMasIcd);
					json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
					json.put("status", "1");

				}

				catch (Exception e) {
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}
			} else {
				try {
					json.put("msg", "patientDetail ID data not found");
					json.put("status", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return json.toString();

	}

	// Delete for common functyion like INVESTIGATION,TREATMENT,REFERAL
	@Override
	public String deleteGridRow(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String flag = "";
		String status = "";
		if (!jsondata.get("flag").equalsIgnoreCase("0")) {
			flag = jsondata.get("flag");
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("1")) {
			Long dgOrderDt = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (dgOrderDt != null) {
				status = dgOrderhdDao.deleteInvestigatRow(dgOrderDt, flag);

			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("2")) {
			Long patientPrecriptionDt = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (patientPrecriptionDt != null) {
				status = dgOrderhdDao.deleteInvestigatRow(patientPrecriptionDt, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("3")) {
			String referPatientDtOrDiagnosis = jsondata.get("valueForDelete").toString();
			String[] refrerandDisChargeValue = null;
			if (StringUtils.isNotBlank(referPatientDtOrDiagnosis)) {
				refrerandDisChargeValue = referPatientDtOrDiagnosis.split("&&&");
			}
			
			// Delete DisChanrgeCode
			Long referalDt = null;
			if (StringUtils.isNotBlank(referPatientDtOrDiagnosis) && referPatientDtOrDiagnosis.contains("&&&")) {
				if (StringUtils.isNotBlank(refrerandDisChargeValue[1])
						&& refrerandDisChargeValue[1].equalsIgnoreCase("0")) {
					Long visitId = Long.parseLong(jsondata.get("visitId").toString());
					Long opdPatientDetailId = Long.parseLong(jsondata.get("opdPatientDetailId").toString());
					Long patientId = Long.parseLong(jsondata.get("patientId").toString());
					status = dgOrderhdDao.deleteChangeIcdCode(
							Long.parseLong(refrerandDisChargeValue[0].toString().trim()), visitId, opdPatientDetailId,
							patientId);
				} else {
					referalDt = Long.parseLong(refrerandDisChargeValue[1].toString());
					if (referalDt != null) {
						status = dgOrderhdDao.deleteInvestigatRow(referalDt, flag);
					}
				}
			} else {
				// Long referalDt = Long.parseLong(jsondata.get("valueForDelete").toString());
				referalDt = Long.parseLong(refrerandDisChargeValue[0].toString());
				if (referalDt != null) {
					status = dgOrderhdDao.deleteInvestigatRow(referalDt, flag);
				}
			}
		}

		if (flag.equalsIgnoreCase("4")) {

			Long opdPatientDetailId = Long.parseLong(jsondata.get("opdPatientDetailId").toString());
			Long patientId = Long.parseLong(jsondata.get("patientId").toString());

			Map<String, Object> mapObject = dgOrderhdDao.getPatientReferalHdByVisitIdAndOpdPdAndPatient(patientId,
					opdPatientDetailId);

			List<ReferralPatientHd> listReferralPatientHd = (List<ReferralPatientHd>) mapObject
					.get("listReferralPatientHd");
			List<ReferralPatientDt> listReferralPatientDt = (List<ReferralPatientDt>) mapObject
					.get("listReferralPatientDt");

			if (CollectionUtils.isNotEmpty(listReferralPatientHd)
					&& CollectionUtils.isNotEmpty(listReferralPatientDt)) {
				status = dgOrderhdDao.deleteForReferalTypeNo(listReferralPatientDt, listReferralPatientHd);
			}

		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("5")) {
			Long procedureDt = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (procedureDt != null) {
				status = dgOrderhdDao.deleteInvestigatRow(procedureDt, flag);
			}
		}

		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("10")) {
			Long rmsId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (rmsId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(rmsId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("11")) {
			Long rmsId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (rmsId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(rmsId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("15")) {
			Long rmsId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (rmsId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(rmsId, flag);
			}
		}

		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("600")) {
			//Change for update opdPatientDetail
			//Long opdDisposalDetailId = Long.parseLong(jsondata.get("valueForDelete").toString());
			String opdDisposalDetailIdV =  jsondata.get("valueForDelete").toString();
			Long opdDisposalDetailId=null;
			Long opdPAtientDetail=null;
			if(StringUtils.isNotEmpty(opdDisposalDetailIdV)) {
				String[]arrayVa=opdDisposalDetailIdV.split("##");
				if(arrayVa!=null) {
				opdDisposalDetailId=Long.parseLong(arrayVa[0].toString().trim());
				opdPAtientDetail=Long.parseLong(arrayVa[1].toString().trim());
				}
			}
			
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("601")) {
			Long dgOrderdtId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (dgOrderdtId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(dgOrderdtId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("701")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("702")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}

		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("f1001")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("i1005")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("t1006")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("a1010")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("Im1005")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}

		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("servi006")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}

		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("diseasi007")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}

		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("beforeNewWarmi008")) {

			Long templateId = Long.parseLong(jsondata.get("valueForDelete").toString());
			if (templateId != null) {
				status = dgOrderhdDao.deleteInvestigatRow(templateId, flag);
			}
		}
		
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("deletePatientSymptons")) {
			String patientSymptons = jsondata.get("valueForDelete").toString();
			String[] patientSymptonsDisChargeValue = null;
			if (StringUtils.isNotBlank(patientSymptons)) {
				patientSymptonsDisChargeValue = patientSymptons.split("&&&");
				if (patientSymptonsDisChargeValue != null) {
					
					status = dgOrderhdDao.deleteInvestigatRow(Long.parseLong(patientSymptonsDisChargeValue[0].toString().trim()), flag);
				}
			}
		}
		
		if (jsondata.get("valueForDelete") != null && flag.equalsIgnoreCase("40")) {
			//Change for update opdPatientDetail
			//Long opdDisposalDetailId = Long.parseLong(jsondata.get("valueForDelete").toString());
			String opdDiagnsisDetailIdValue =  jsondata.get("valueForDelete").toString();
			Long opdDiagnosisDetailId=null;
			Long opdPatientDiagnosisDetail=null;
			if(StringUtils.isNotEmpty(opdDiagnsisDetailIdValue)) {
				String[]arrayVa=opdDiagnsisDetailIdValue.split("##");
				if (opdDiagnsisDetailIdValue != null) {
					
					status = dgOrderhdDao.deleteInvestigatRow(Long.parseLong(arrayVa[0].toString().trim()), flag);
				}
			}
			
		}

		json.put("status", status);
		return json.toString();
	}

//User for Nursing
	@Override
	public String getPocedureDetailRecall(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json =null;
		try {
			  json = new JSONObject();
			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			List<MasEmpanelledHospital> masEmpanelledHospitalList = null;
			List<OpdDisposalDetail> opdDisposalDetailList =null;
			Long visitId=null;
			//List<MasDisposal> masDisposal=null;
			if(jsondata.get("visitId")!=null ) {
				visitId=Long.parseLong(jsondata.get("visitId"));
			}
			
			if (!jsondata.isEmpty()) {
				List<Object[]> listProcedureDt = null;
				if (jsondata.get("opdPatientDetailId") != null) {
					listProcedureDt = dgOrderhdDao
							.getProcedureDtList(Long.parseLong(jsondata.get("opdPatientDetailId").toString()),
									Long.parseLong(jsondata.get("visitId").toString()));
					String nursingName="";	
					Long noOfDays=null;
					String nursingRemarks="";
					Long procedureDtId=null;
					Long procedureHdId=null;
					Long frequencyId=null;
					Long nursingId=null;
					String proceduretype="";
					String frequencyName="";
					Long procedureId=null;
					String remarks="";
					String status="";
					if(listProcedureDt!=null) {
					for (Iterator<?> it = listProcedureDt.iterator(); it.hasNext();) {
						Object[] row = (Object[]) it.next();
						HashMap<String, Object> pt = new HashMap<String, Object>();
						if(row[0]!=null) {
							nursingName= row[0].toString();
						}
						
						if(row[1]!=null) {
							frequencyName= row[1].toString();
						}
						
						
						if(row[2]!=null) {
							noOfDays= Long.parseLong(row[2].toString());
						}
						else {
							noOfDays=0l;
						}
						
						if(row[3]!=null) {
							nursingRemarks= row[3].toString();
						}
						if(row[4]!=null) {
							nursingId= Long.parseLong(row[4].toString());
						}
						if(row[5]!=null) {
							procedureId= Long.parseLong(row[5].toString());
						}
						if(row[9]!=null) {
							procedureDtId= Long.parseLong(row[9].toString());
						}
						if(row[6]!=null) {
							procedureHdId= Long.parseLong(row[6].toString());
						}
						if(row[7]!=null) {
							frequencyId= Long.parseLong(row[7].toString());
						}
						else {
							frequencyId=0l;
						}
						if(row[8]!=null) {
							proceduretype=  row[8].toString();
						}
						if(row[10]!=null) {
							remarks= row[10].toString();
						}
						if(row[11]!=null) {
							status= row[11].toString();
						}
						
						pt.put("nursingName", nursingName);
						pt.put("noOfDays", noOfDays);
						pt.put("nursingRemarks", nursingRemarks);
						pt.put("remarks", remarks);
						pt.put("nursingId", nursingId);
						pt.put("procedureDtId", procedureDtId);
						pt.put("procedureHdId", procedureHdId);
						pt.put("frequencyId", frequencyId);
						pt.put("proceduretype", proceduretype);
						pt.put("frequencyName", frequencyName);
						pt.put("procedureId", procedureId);
						pt.put("status", status);
						c.add(pt);
					}
					
					List<MasFrequency> mas_frequency = md.getMasFrequency();
					json.put("listOfProcedure", c);
					json.put("masFrequency", mas_frequency);
					json.put("OpdDisposalDetailList", opdDisposalDetailList);
				
					json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
					json.put("status", "1");
				}
					else {
						json.put("msg", "Visit ID data not found");
						json.put("OpdDisposalDetailList", opdDisposalDetailList);
						json.put("status", 0);
					}
				}
				else {
					json.put("msg", "OpdPatientDetail Not Exist");
					json.put("OpdDisposalDetailList", opdDisposalDetailList);
					json.put("status", 0);
				//	return "{\"status\":\"0\",\"msg\":\"OpdPatientDetail Not Exist }";
				return json.toString();
				}
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	
/////////////////////////code by dhiraj ///////////////////

	@SuppressWarnings("unchecked")
	@Override
	public String minorSurgeryWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		List<Map<String, Object>> minorSurgeryWaitingList = new ArrayList<>();
		Map<String, Object> map = od.minorSurgeryWaitingList(jsondata, request, response);
		if (map != null && !map.isEmpty()) {
			List<ProcedureHd> list = (List<ProcedureHd>) map.get("minorSurgeryWaitingList");
			if (list != null && !list.isEmpty()) {
				String name = "", firstName = "", middleName = "", lastName = "", opdDate = "", priority = "",
						patientName = "", age = "", gender = "", status = "", departmentName = "", serviceNo = "";
				for (ProcedureHd hd : list) {
					Map<String, Object> data = new HashMap<>();

					if (hd.getVisit() != null) {

						/*
						 * if (hd.getVisit().getIntDoctorId() != null) { firstName =
						 * hd.getVisit().getDoctorId().getFirstName() + ""; middleName =
						 * hd.getVisit().getDoctorId().getMiddleName() + ""; lastName =
						 * hd.getVisit().getDoctorId().getLastName() + ""; }
						 */
						name = firstName + " " + middleName + " " + lastName;
						priority = hd.getVisit().getPriority() + "";
						if (hd.getVisit().getMasDepartment() != null) {
							departmentName = HMSUtil
									.convertNullToEmptyString(hd.getVisit().getMasDepartment().getDepartmentName());
						}
					}
					data.put("id", hd.getProcedureHdId());
					data.put("doctor_name", departmentName);
					data.put("priority", priority);
					if (hd.getOpdPatientDetails() != null) {
						
						opdDate = HMSUtil.convertNullToEmptyString(HMSUtil.changeDateToddMMyyyy(hd.getOpdPatientDetails().getOpdDate()));
					}

					data.put("opd_date", opdDate);
					if (hd.getPatient() != null) {
						patientName = hd.getPatient().getPatientName();
						age =  HMSUtil.calculateAge(hd.getPatient().getDateOfBirth());
						if (hd.getPatient().getMasAdministrativeSex() != null) {
							gender = hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName().toString();
						}

					}
					data.put("patient_name", patientName);
					data.put("age", age);
					data.put("gender", gender);
					data.put("serviceNo", serviceNo);
					data.put("status", hd.getStatus());
					minorSurgeryWaitingList.add(data);

				}
				obj.put("status", "1");
				obj.put("count", map.get("count"));
				obj.put("minorSurgeryWaitingList", minorSurgeryWaitingList);
			} else {
				obj.put("status", "0");
				obj.put("count", "0");
				obj.put("minorSurgeryWaitingList", new JSONArray());
			}
		} else {
			obj.put("status", "0");
			obj.put("count", "");
			obj.put("minorSurgeryWaitingList", new JSONArray());
		}

		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getMinorSurgeryDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		String vitalData = "";
		List<OpdPatientDetail> vitalRecord = new ArrayList<OpdPatientDetail>();
		Map<String, Object> patientMap = new HashMap<>();
		Map<String, Object> vitalMap = new HashMap<>();
		Map<String, Object> map = od.getMinorSurgeryDetail(jsondata, request, response);
		List<MasAnesthesia> anesthesiaList = od.getAnesthesiaList();
		if (map != null && !map.isEmpty()) {
			List<ProcedureDt> list = (List<ProcedureDt>) map.get("detailList");
			List<ProcedureDt> list2 = new ArrayList<>();
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				if (ids.contains(list.get(i).getMasNursingCare().getNursingId())) {
					continue;
				}
				ids.add(list.get(i).getMasNursingCare().getNursingId());
				list2.add(list.get(i));

			}
			if (list2 != null && list2.size() > 0) {
				List<Map<String, Object>> nursingDetailList = new ArrayList<>();
				@SuppressWarnings("unused")
				String patientName = "", age = "", gender = "", diagnosis = "", prescribedBy = "", serviceNo = "",
						opdDate = "", remarks = "";

				for (ProcedureDt dt : list2) {
					//System.out.println("id=" + dt.getProcedureHd().getPatient().getPatientId());
					Map<String, Object> data = new HashMap<>();
					patientMap.put("patientName", dt.getProcedureHd().getPatient().getPatientName());
					patientMap.put("patientId", dt.getProcedureHd().getPatient().getPatientId());
					//patientMap.put("serviceNo", dt.getProcedureHd().getPatient().getServiceNo());

					
					  if(dt.getProcedureHd().getVisitId() !=null) { 
					  //HashMap<String, String>  visitmap=new HashMap<String, String>();
					  patientMap.put("visitId",dt.getProcedureHd().getVisitId().toString());
					  //visitmap.put("visitId",dt.getProcedureHd().getVisitId().toString());
					  //vitalData=OpdPatientDetails(visitmap, request, response);
					 //System.out.println("vitalData"+vitalData);
					  
					 }				 

					patientMap.put("opdDate", HMSUtil.convertNullToEmptyString(HMSUtil.convertDateToStringFormat(
							dt.getProcedureHd().getOpdPatientDetails().getOpdDate(), "dd/MM/yyyy")));
					patientMap.put("age",
							ProjectUtils.getDOB(dt.getProcedureHd().getPatient().getDateOfBirth()).getYears());
					patientMap.put("fullage",HMSUtil.calculateAge(dt.getProcedureHd().getPatient().getDateOfBirth()));
					patientMap.put("genderId",
							dt.getProcedureHd().getPatient().getMasAdministrativeSex().getAdministrativeSexId());
					patientMap.put("gender",
							dt.getProcedureHd().getPatient().getMasAdministrativeSex().getAdministrativeSexName());
					patientMap.put("header_id", dt.getProcedureHd().getProcedureHdId());
					patientMap.put("icd_diagnosis", HMSUtil
							.convertNullToEmptyString(dt.getProcedureHd().getOpdPatientDetails().getIcdDiagnosis()));
					patientMap.put("working_diagnosis", HMSUtil.convertNullToEmptyString(
							dt.getProcedureHd().getOpdPatientDetails().getWorkingDiagnosis()));

					data.put("id", dt.getProcedureDtId());
					data.put("nCode", dt.getMasNursingCare().getNursingCode());
					data.put("minorSurgryName",
							dt.getMasNursingCare().getNursingName() != null ? dt.getMasNursingCare().getNursingName()
									: "");
					/*data.put("userName",
							dt.getProcedureHd().getUser() != null ? dt.getProcedureHd().getUser().getFirstName() : "");
*/
					data.put("prescribedBy",
							dt.getProcedureHd().getUser() != null ? dt.getProcedureHd().getUser().getUserId() : "");
					data.put("rankName",od.getRankBySerivceNo(jsondata.get("serviceNo").toString()));
					data.put("procedure_id", dt.getMasNursingCare().getNursingId());					
					data.put("remarks", dt.getNursingRemark() != null ? dt.getNursingRemark() : "");
					data.put("anethesiaId",dt.getMasAnesthesia()!=null?dt.getMasAnesthesia().getAnesthesiaId():0);
					data.put("anethesiaName",dt.getMasAnesthesia()!=null?dt.getMasAnesthesia().getAnesthesiaName():"");
					data.put("status",dt.getStatus()!=null ? dt.getStatus() :"");
					nursingDetailList.add(data);

				}

				//System.out.println("patientMap=" + patientMap);
				obj.put("status", "1");
				obj.put("patient_detail", patientMap);
				obj.put("nursingDetailList", nursingDetailList);
				obj.put("vitalDetailst", vitalData);
				obj.put("anesthesiaList", anesthesiaList);
			} else {
				obj.put("status", "0");
				obj.put("patient_detail", patientMap);
				obj.put("nursingCareDetailList", new JSONArray());
			}
		} else {
			obj.put("status", "0");
			obj.put("patient_detail", patientMap);
			obj.put("nursingCareDetailList", new JSONArray());
		}
		return obj.toString();

	}

	@Override
	public String getAnesthesiaList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasAnesthesia> anesthesiaList = od.getAnesthesiaList();
		if (anesthesiaList != null && anesthesiaList.size() > 0) {

			jsonObj.put("data", anesthesiaList);
			jsonObj.put("count", anesthesiaList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", anesthesiaList);
			jsonObj.put("count", 0);
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		//System.out.println(jsonObj.toString());
		return jsonObj.toString();
	}

	@Override
	public String saveMinorSurgery(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

			String dtObj = od.saveMinorSurgery(jsondata);

			if (dtObj != null && dtObj.equalsIgnoreCase("success")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Record Added Successfully");

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Error occured");
			}
		}
		return jsonObj.toString();
	}
	
	@Override
	public String validateMinorSurgery(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

			String dtObj = od.validateMinorSurgery(jsondata);

			if (dtObj != null && dtObj.equalsIgnoreCase("msExists")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Minor surgery name already added");

			} 
		}
		return jsonObj.toString();
	}
	
	@Override
	public String deleteMinorSurgery(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

			String dtObj = od.deleteMinorSurgery(jsondata);
			if (dtObj != null && dtObj.equalsIgnoreCase("success")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Minor surgery Deleted Successfully");

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Error occured");
			}
		}
		return jsonObj.toString();
	}

///////////////////////////////////////////////////

// Save Updadate Procedure candidate
	public void saveOrUpdateProcedure(HashMap<String, Object> payload,String visitId,String opdPatientDetaiId,String patientId)
	{
		String hospitalId = "";
		String userId = "";
	//	try {

			String procedureNameNursingId = payload.get("procedureNameNursingId").toString();
			procedureNameNursingId = getReplaceString(procedureNameNursingId);

			String procedureDtIdValue = payload.get("procedureDtIdValue").toString();
			procedureDtIdValue = getReplaceString(procedureDtIdValue);

			String procedureHdId = payload.get("procedureHdId").toString();
			procedureHdId = getReplaceString(procedureHdId);

			String frequencyNursing = payload.get("freProcedure").toString();
			frequencyNursing = getReplaceString(frequencyNursing);
				hospitalId = payload.get("hospitalId").toString();
				hospitalId = getReplaceString(hospitalId);

				userId = payload.get("userIdVal").toString();
				userId = getReplaceString(userId);
		 
			String procedureFreValues = "";
			if (StringUtils.isNotBlank(frequencyNursing)) {
				if (frequencyNursing.contains("@")) {
					String[] preFreListValue = frequencyNursing.split(",");
					int count = 0;
					for (int i = count; i < preFreListValue.length; i++) {
						 if(StringUtils.isNotEmpty(preFreListValue[i])) {
						String[] procedureListValueNew = preFreListValue[i].split("@");
						procedureFreValues += procedureListValueNew[1] + ",";
						 }
						 else {
							 procedureFreValues += "0" + ",";
						 }
						// count+=2;
					}
				}
				frequencyNursing = procedureFreValues;
			}

			//String noOfDaysNursing = payload.get("noOfDaysPro").toString();
			//noOfDaysNursing = getReplaceString(noOfDaysNursing);

			//String procedureType = payload.get("procedureType").toString();
			//procedureType = getReplaceString(procedureType);

			String remarkNursing = payload.get("remark_nursing").toString();
			remarkNursing = getReplaceString(remarkNursing);
			String statusOfPro ="";
			if( payload.get("statusOfPro")!=null) {
			  statusOfPro = payload.get("statusOfPro").toString();
			  statusOfPro = getReplaceString(statusOfPro);
			}
			String[] procedureNameNursingIdValue = procedureNameNursingId.split(",");
			String[] procedureDtIdValueArray = procedureDtIdValue.split(",");
			String[] procedureHdIdValueArray = procedureHdId.split(",");

			String[] proFreListValue = frequencyNursing.split(",");

			//String[] noOfDaysNursingValueArray = noOfDaysNursing.split(",");

			//String[] procedureTypeValue = procedureType.split(",");

			String[] remarkNursingValue = remarkNursing.split(",");
			String[] statusOfProArray = statusOfPro.split(",");

			HashMap<String, String> mapInvestigationMap = new HashMap<>();

			String finalValue = "";
			Integer counter = 1;
			
			if(procedureNameNursingIdValue!=null && procedureNameNursingIdValue.length>0) {
			for (int i = 0; i < procedureNameNursingIdValue.length; i++) {

				if (StringUtils.isNotEmpty(procedureNameNursingIdValue[i].toString())
						&& !procedureNameNursingIdValue[i].equalsIgnoreCase("0")) {
					finalValue += procedureNameNursingIdValue[i].trim();
					if (!procedureDtIdValueArray[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(procedureDtIdValueArray[i])) {
						for (int m = i; m < procedureDtIdValueArray.length; m++) {
							finalValue += "," + procedureDtIdValueArray[m].trim();
							if (m == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}

					if ( i < proFreListValue.length && !proFreListValue[i].equalsIgnoreCase("0") && StringUtils.isNotBlank(proFreListValue[i]) ) {
						for (int j = i; j < proFreListValue.length; j++) {
							finalValue += "," + proFreListValue[j].trim();
							if (j == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}
					
					if ( i < remarkNursingValue.length && StringUtils.isNotBlank(remarkNursingValue[i])) {
						for (int l = i; l < remarkNursingValue.length; l++) {
							finalValue += "," + remarkNursingValue[l].trim();
							if (l == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "0";
					}
					
					if ( i < statusOfProArray.length && StringUtils.isNotBlank(statusOfProArray[i])) {
						for (int l = i; l < statusOfProArray.length; l++) {
							finalValue += "," + statusOfProArray[l].trim();
							if (l == i) {
								break;
							}
						}
					} else {
						finalValue += "," + "N";
					}
					
					mapInvestigationMap.put(procedureNameNursingIdValue[i].trim() + "@#" + counter, finalValue);
					finalValue = "";
					counter++;
				}
			}
			counter = 1;
			for (String procedureNursingId : procedureNameNursingIdValue) {
				if (StringUtils.isNotEmpty(procedureNursingId)) {
					if (mapInvestigationMap.containsKey(procedureNursingId.trim() + "@#" + counter)) {
						String procedureIdValue = mapInvestigationMap.get(procedureNursingId.trim() + "@#" + counter);

						if (StringUtils.isNotEmpty(procedureIdValue)) {

							String[] finalValueProcedure = procedureIdValue.split(",");
							ProcedureDt procedureDt = null;
							if (finalValueProcedure[1] != null && !finalValueProcedure[1].equalsIgnoreCase("0")
									&& StringUtils.isNotBlank(finalValueProcedure[1])) {

								procedureDt = dgOrderhdDao.getProcedureDtByProcedureDtId(
										Long.parseLong(finalValueProcedure[1].toString()));

							} else {

								procedureDt = new ProcedureDt();
								if (procedureHdIdValueArray != null
										&& StringUtils.isNotBlank(procedureHdIdValueArray[0])) {
									procedureDt.setProcedureHdId(Long.parseLong(procedureHdIdValueArray[0].toString()));
								}

							}

							if (finalValueProcedure != null) {

								if (StringUtils.isNotEmpty(finalValueProcedure[0])
										&& !finalValueProcedure[0].equals("0")) {
									procedureDt.setProcedureId(Long.parseLong(finalValueProcedure[0]));
								}

								
								if (finalValueProcedure[3] != null && StringUtils.isNotBlank(finalValueProcedure[3])
										&& !finalValueProcedure[3].equals("0")) {
									procedureDt.setRemarks(finalValueProcedure[3].toString());
								}

								if (finalValueProcedure[0] != null && StringUtils.isNotBlank(finalValueProcedure[0])
										&& !finalValueProcedure[0].equals("0")) {
									ProcedureHd procedureHd = null;
									if (finalValueProcedure[4] != null && StringUtils.isNotBlank(finalValueProcedure[4])
											&& !finalValueProcedure[4].equals("0")) {
										procedureHd = dgOrderhdDao.getProcedureHdByVisitIdAndType(
												Long.parseLong(visitId), finalValueProcedure[4].trim());
									}

									if (procedureHd != null) {
										procedureHd.setProcedureType(finalValueProcedure[4].trim());
										if(StringUtils.isNotBlank(finalValueProcedure[4]) && finalValueProcedure[4].trim().equalsIgnoreCase("M")) {
											procedureDt.setFrequencyId(null);
											procedureDt.setNoOfDays(null);
										}
										
									} else {
										procedureHd = new ProcedureHd();
										procedureHd.setProcedureType(finalValueProcedure[4].trim());
										if(StringUtils.isNotBlank(finalValueProcedure[4]) && finalValueProcedure[4].trim().equalsIgnoreCase("M")) {
											procedureDt.setFrequencyId(null);
											procedureDt.setNoOfDays(null);
										}
									}
									
											
									
									if(procedureHd!=null && procedureHd.getProcedureHdId()==0)
										procedureHd.setStatus("N");
									Date date = new Date();
									procedureHd.setRequisitionDate(new Timestamp(date.getTime()));
									if (StringUtils.isNotBlank(hospitalId))
										procedureHd.setMmuId(Long.parseLong(hospitalId));
									if (StringUtils.isNotBlank(userId)) {
										procedureHd.setLastChgBy(Long.parseLong(userId));
										procedureHd.setDoctorId(Long.parseLong(userId));
									}
									if (StringUtils.isNotBlank(patientId))
										procedureHd.setPatientId(Long.parseLong(patientId));
									if (StringUtils.isNotBlank(opdPatientDetaiId))
										procedureHd.setOpdPatientDetailsId(Long.parseLong(opdPatientDetaiId));
									if (StringUtils.isNotBlank(visitId))
										procedureHd.setVisitId(Long.parseLong(visitId));
									procedureHd.setLastChgDate(new Timestamp(date.getTime()));
									procedureHd.setLastChgBy(Long.parseLong(userId));
									Long procedureHdIdValue = dgOrderhdDao.saveOrUpdateProcedureHd(procedureHd);
									if (procedureHdIdValue != null)
										procedureDt.setProcedureHdId(procedureHdIdValue);
								}
							}
							Date date =new Date();
							procedureDt.setLastChgDate(new Timestamp(date.getTime()));
							procedureDt.setAppointmentDate(new Timestamp(date.getTime()));
							dgOrderhdDao.saveOrUpdateProcedureDd(procedureDt);
						}
					}
				}
				counter++;
			}
			
			}
		/*
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}



//////////////////////////////////////////////////



	@Override
	public String authenticateUser(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

		String data = od.findAuthenticatePatient(jsondata);
		if (data != null && data.equalsIgnoreCase("success")) {
		jsonObj.put("data", data);
		jsonObj.put("status", 1);
		jsonObj.put("msg", "Authenticate user");
		
		} else {
		jsonObj.put("status", 0);
		jsonObj.put("msg", "Error occured");
		}
		}
		return jsonObj.toString();
	}

	@Override
	public String checkForAuthenticateUser(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

		String data = od.findCheckForAuthenticatePatient(jsondata);
		if (data != null) {
		jsonObj.put("data", data);
		jsonObj.put("status", 1);
		jsonObj.put("msg", "Authenticate user");
		
		} else {
		jsonObj.put("status", 0);
		jsonObj.put("msg", "Error occured");
		}
		}
		return jsonObj.toString();
	}


	@Override
	public String showCurrentMedication(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object[]> listObject = null;
		Map<String, Object> map = null;
		Integer count = 0;
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			if (jsondata.get("patientId") != null) {
				map = dgOrderhdDao.getTreatementDetailByPatientId(Long.parseLong(jsondata.get("patientId").toString()),
						Integer.parseInt(jsondata.get("pageNo").toString()),
						Long.parseLong(jsondata.get("departmentId").toString()));
				count = (Integer) map.get("count");
				listObject = (List<Object[]>) map.get("list");
			}
			List<MasFrequency> mas_frequency = md.getMasFrequency();

			String nomenclature = "";
			Long itemId = null;
			Long frequencyId = null;
			String frequencyName = "";
			String frequencyCode = "";
			String dosage = "";
			Long noOfDays = null;
			Long precryptionHdId = null;
			Long precriptionDtId = null;
			String dispStock = "";
			Long total = null;
			String instruction = "";
			String storeStoke = "";
			String PVMSno = "";
			String otherTreatement = "";
			Long itemStopdBy = null;
			String itemStopDate = null;
			Long itemStopStatus = null;
			String precriptionDate = "";
			String departmentName = "";
			String precribedByDoc = "";
			Long itemClassId = null;
			if (listObject != null) {

				try {
					for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
						precribedByDoc = "";
						Object[] row = (Object[]) it.next();
						HashMap<String, Object> pt = new HashMap<String, Object>();

						if (row[0] != null) {
							nomenclature = row[0].toString();
						}
						if (row[1] != null) {
							itemId = Long.parseLong(row[1].toString());
							jsondata.put("itemId", itemId);
							int avStock=getAvailableStock(jsondata);
							pt.put("availableStock", avStock);
						}

						if (row[2] != null) {
							frequencyId = Long.parseLong(row[2].toString());
						}

						if (row[3] != null) {
							frequencyName = row[3].toString();
						}
						if (row[4] != null) {
							frequencyCode = row[4].toString();
						}

						if (row[5] != null) {
							noOfDays = Long.parseLong(row[5].toString());
						}
						if (row[6] != null) {
							dosage = row[6].toString();
						}
						if (row[7] != null) {
							precryptionHdId = Long.parseLong(row[7].toString());
						}
						if (row[8] != null) {
							precriptionDtId = Long.parseLong(row[8].toString());
						}
						if (row[13] != null) {
							dispStock = row[13].toString();
						}

						if (row[10] != null) {
							total = Long.parseLong(row[10].toString());
						}
						if (row[11] != null) {
							instruction = row[11].toString();
						}
						if (row[12] != null) {
							storeStoke = row[12].toString();
						}
						if (row[14] != null) {
							PVMSno = row[14].toString();
						}
						

						if (row[15] != null) {
							itemStopdBy = Long.parseLong(row[15].toString());
						}
						if (row[16] != null) {
							Date dd = (Date) row[16];
							itemStopDate = HMSUtil.convertDateToStringFormat(dd, "dd/MM/yyyy");

						}
						if (row[17] != null) {
							itemStopStatus = Long.parseLong(row[17].toString());
						} else {
							itemStopStatus = 0l;
						}
						if (row[18] != null) {
							Date precriptionDateValue = (Date) row[18];
							precriptionDate = HMSUtil.convertDateToStringFormat(precriptionDateValue, "dd/MM/yyyy");
						}
						if (row[19] != null) {
							departmentName = row[19].toString();
						}
						if (row[20] != null) {
							Users users = systemAdminDao.getUserbyUserId(Long.parseLong(row[20].toString()));
							if (users != null && users.getUserName() != null) {
								precribedByDoc = users.getUserName() + " ";
							}
							if (users != null && users.getUserName() != null) {
								precribedByDoc += users.getUserName();
							}

							if (row[21] != null) {
								itemClassId = Long.parseLong(row[21].toString());
							}
						}

						pt.put("nomenclature", nomenclature);
						pt.put("itemId", itemId);
						pt.put("frequencyId", frequencyId);
						pt.put("frequencyName", frequencyName);
						pt.put("frequencyCode", frequencyCode);
						pt.put("dosage", dosage);
						pt.put("noOfDays", noOfDays);
						pt.put("precryptionHdId", precryptionHdId);
						pt.put("precriptionDtId", precriptionDtId);
						pt.put("dispStock", dispStock);
						pt.put("total", total);
						pt.put("instruction", instruction);
						pt.put("storeStoke", storeStoke);
						pt.put("PVMSno", PVMSno);
						pt.put("otherTreatement", otherTreatement);
						pt.put("itemStopDate", itemStopDate);
						pt.put("itemStopStatus", itemStopStatus);

						pt.put("precriptionDate", precriptionDate);
						pt.put("departmentName", departmentName);
						pt.put("precribedByDoc", precribedByDoc);
						pt.put("itemClassId", itemClassId);
						if (itemStopdBy != null) {
							Users users = systemAdminDao.getUserbyUserId(itemStopdBy);
							String lastNameVal = "";
							if (users.getUserName() != null)
								lastNameVal = users.getUserName();
							pt.put("itemStopByUserName", users.getUserName() + " " + lastNameVal);
						}

						c.add(pt);
					}

					json.put("listObject", c);
					json.put("visitId", jsondata.get("visitId"));
					json.put("count", count);
					json.put("MasFrequencyList", mas_frequency);
					json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
					json.put("status", "1");

				}

				catch (Exception e) {
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}
			} else {
				try {
					// json.put("msg", "Visit ID data not found");
					// json.put("status", 0);

					json.put("listObject", c);
					json.put("visitId", jsondata.get("visitId"));
					json.put("count", count);
					json.put("MasFrequencyList", mas_frequency);
					json.put("msg", "Visit ID data not found ");
					json.put("status", "0");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		return json.toString();
	}
	
	@Override
	public String updateCurrentMedication(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		Long data=null;
		if (jsondata != null) {
			String userId=jsondata.get("userIdVal").toString();
			userId=getReplaceString(userId);
			Long userIdValue=Long.parseLong(userId.toString());
			
			String patientPreciptionDtIddd=jsondata.get("precriptionDtValue").toString();
			patientPreciptionDtIddd=getReplaceString(patientPreciptionDtIddd);
			String [] patientPreciptionValuee=patientPreciptionDtIddd.split(",");
			List<Long>listPatientPreIdValue=new ArrayList<>();
			for(String ss:patientPreciptionValuee) {
				if(StringUtils.isNotEmpty(ss)) {
			
					listPatientPreIdValue.add(Long.parseLong(ss.toString().trim()));
			}
			}
			List<PatientPrescriptionDt> listPatientPrescriptionDt=null;
			if(CollectionUtils.isNotEmpty(listPatientPreIdValue))
          listPatientPrescriptionDt=dgOrderhdDao.getPatientPrecriptionDtIdByPatientId(listPatientPreIdValue);
        if(CollectionUtils.isNotEmpty(listPatientPrescriptionDt))
		for(PatientPrescriptionDt patientPrescriptionDt:listPatientPrescriptionDt) {
			
			patientPrescriptionDt.setItemStopDate(new Date());
			patientPrescriptionDt.setItemStopStatus(1l);
			patientPrescriptionDt.setItemStopBy(userIdValue);
			
          data = dgOrderhdDao.saveOrUpdatePatientPrecriptionDt(patientPrescriptionDt);
		}
       if (data != null) {
		jsonObj.put("data", data);
		jsonObj.put("status", 1);
		jsonObj.put("msg", "Successful update");
		
		} else {
		jsonObj.put("status", 0);
		jsonObj.put("msg", "Error occured");
		}
		}
		return jsonObj.toString();
	}

	
	public void saveOrUpdateNewNip(String items, String dispUnit, String dosages, String frequencys,
			String days, String totals, String instructions, String stocks, String precriptionDtId,
			String precriptionHdId, HashMap<String, Object> payload,String statusOfNewNiv) {
		Long itenIdValue = null;
		// try {

		String newNip1 = payload.get("newNip1").toString();
		newNip1 = getReplaceString(newNip1);

		String class1 = payload.get("class1").toString();
		class1 = getReplaceString(class1);

		String au1 = payload.get("au1").toString();
		au1 = getReplaceString(au1);

		String[] newNipValue = newNip1.split(",");
		String[] class1Value = class1.split(",");
		String[] au1Value = au1.split(",");

		String[] itemsValue = items.split(",");
		String[] dispUnitValue = dispUnit.split(",");
		String[] dosagesValue = dosages.split(",");
		String[] frequencysValue = frequencys.split(",");

		String[] daysValue = days.split(",");
		String[] totalsValue = totals.split(",");

		String[] instructionsValue = instructions.split(",");
		String[] stocksValue = stocks.split(",");
		String[] precriptionHdIdValue = precriptionHdId.split(",");

		String[] precriptionDtIdValue = precriptionDtId.split(",");
		String recommendedMedicalAdvice = payload.get("recommendedMedicalAdvice").toString();
		recommendedMedicalAdvice = getReplaceString(recommendedMedicalAdvice);
		String userId = "";
		if (payload.get("userIdVal") != null) {
			userId = payload.get("userIdVal").toString();
			userId = getReplaceString(userId);
		}
		String[] statusOfNewNivArray = statusOfNewNiv.split(",");

		HashMap<String, String> mapInvestigationMap = new HashMap<>();

		Integer counter = 1;
		String finalValue = "";
		String visitId = "";
		for (int i = 0; i < newNipValue.length; i++) {

			if (StringUtils.isNotEmpty(newNipValue[i].toString()) && !newNipValue[i].equalsIgnoreCase("0")) {

				finalValue += newNipValue[i].trim();

				if (i < precriptionDtIdValue.length && !precriptionDtIdValue[i].equalsIgnoreCase("0")
						&& StringUtils.isNotBlank(precriptionDtIdValue[i])) {
					for (int m = i; m < precriptionDtIdValue.length; m++) {
						finalValue += "," + precriptionDtIdValue[m].trim();
						if (m == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < dispUnitValue.length && !dispUnitValue[i].equalsIgnoreCase("0")
						&& StringUtils.isNotBlank(dispUnitValue[i])) {
					for (int j = i; j < dispUnitValue.length; j++) {
						finalValue += "," + dispUnitValue[j].trim();
						if (j == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < dosagesValue.length && StringUtils.isNotBlank(dosagesValue[i])) {
					for (int k = i; k < dosagesValue.length; k++) {
						finalValue += "," + dosagesValue[k].trim();
						if (k == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}
				if (i < frequencysValue.length && StringUtils.isNotBlank(frequencysValue[i])) {
					for (int l = i; l < frequencysValue.length; l++) {
						finalValue += "," + frequencysValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < daysValue.length && StringUtils.isNotBlank(daysValue[i])) {
					for (int l = i; l < daysValue.length; l++) {
						finalValue += "," + daysValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < totalsValue.length && StringUtils.isNotBlank(totalsValue[i])) {
					for (int l = i; l < totalsValue.length; l++) {
						finalValue += "," + totalsValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < instructionsValue.length && StringUtils.isNotBlank(instructionsValue[i])) {
					for (int l = i; l < instructionsValue.length; l++) {
						finalValue += "," + instructionsValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < stocksValue.length && StringUtils.isNotBlank(stocksValue[i])) {
					for (int l = i; l < stocksValue.length; l++) {
						finalValue += "," + stocksValue[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < class1Value.length && StringUtils.isNotBlank(class1Value[i])) {
					for (int l = i; l < class1Value.length; l++) {
						finalValue += "," + class1Value[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < au1Value.length && StringUtils.isNotBlank(au1Value[i])) {
					for (int l = i; l < au1Value.length; l++) {
						finalValue += "," + au1Value[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "0";
				}

				if (i < statusOfNewNivArray.length && StringUtils.isNotBlank(statusOfNewNivArray[i])) {
					for (int l = i; l < statusOfNewNivArray.length; l++) {
						finalValue += "," + statusOfNewNivArray[l].trim();
						if (l == i) {
							break;
						}
					}
				} else {
					finalValue += "," + "p";
				}

				mapInvestigationMap.put(newNipValue[i].trim() + "@#" + counter, finalValue);
				finalValue = "";

			}
			counter++;
		}
		counter = 1;
		PatientPrescriptionHd patientPrescriptionHd = null;
		Date date = new Date();
		String itemTypeCodeNiv = HMSUtil.getProperties("adt.properties", "itemTypeCodeNIP").trim();
		MasItemType mty = null;
		mty = od.getItemTypeIdNiv(itemTypeCodeNiv);
		for (String newNipValueObj : newNipValue) {
			if (StringUtils.isNotEmpty(newNipValueObj)) {
				if (mapInvestigationMap.containsKey(newNipValueObj.trim() + "@#" + counter)) {
					String newNipValueObjVal = mapInvestigationMap.get(newNipValueObj.trim() + "@#" + counter);

					if (StringUtils.isNotEmpty(newNipValueObjVal)) {

						String[] finalValueItem = newNipValueObjVal.split(",");
						MasStoreItem masStoreItem = null;
						if (finalValueItem[0] != null && !finalValueItem[0].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(finalValueItem[0])) {

							List<MasStoreItem> listMasStoreItem = dgOrderhdDao
									.getMasStoreItemByItemName(finalValueItem[0].trim());
							if (CollectionUtils.isNotEmpty(listMasStoreItem)) {
								masStoreItem = listMasStoreItem.get(0);
							} else {
								masStoreItem = new MasStoreItem();
								masStoreItem.setItemTypeId(mty.getItemTypeId());
								masStoreItem.setLastChgDate(new Timestamp(date.getTime()));
								masStoreItem.setStatus("y");
								masStoreItem.setTypeOfItem("E");

							}
						} else {
							masStoreItem = new MasStoreItem();
							masStoreItem.setItemTypeId(mty.getItemTypeId());
							masStoreItem.setLastChgDate(new Timestamp(date.getTime()));
							masStoreItem.setStatus("y");
							masStoreItem.setTypeOfItem("E");
						}

						if (finalValueItem != null) {

							if (finalValueItem[0] != null && StringUtils.isNotEmpty(finalValueItem[0])
									&& !finalValueItem[0].equals("0")) {
								masStoreItem.setNomenclature(finalValueItem[0].trim());

								if (finalValueItem[9] != null)

									masStoreItem.setItemClassId(Long.parseLong(finalValueItem[9].trim()));

								if (finalValueItem[10] != null) {
									masStoreItem.setDispUnitId(Long.parseLong(finalValueItem[10].trim()));
									masStoreItem.setItemUnitId(Long.parseLong(finalValueItem[10].trim()));
								}

								if (payload.get("hospitalId") != null) {
									String hospitalId = payload.get("hospitalId").toString();
									hospitalId = getReplaceString(hospitalId);
									masStoreItem.setHospitalId(Long.parseLong(hospitalId));

								}

								if (payload.get("userId") != null) {
									userId = payload.get("userId").toString();
									userId = getReplaceString(userId);
									masStoreItem.setLastChgBy(Long.parseLong(userId));
								}

								// masStoreItem.setItemTypeId(3L);
								itenIdValue = dgOrderhdDao.updateMasStoreItem(masStoreItem);

								PatientPrescriptionDt patientPrescriptionDt = dgOrderhdDao
										.getMasStoreItemByPatientPrecriptionDtId(itenIdValue);

								if (patientPrescriptionDt == null) {
									patientPrescriptionDt = new PatientPrescriptionDt();
								}

								/*
								 * if (finalValueItem[2]!=null && StringUtils.isNotEmpty(finalValueItem[2]) &&
								 * !finalValueItem[2].equals("0")) {
								 * patientPrescriptionDt.setDispStock(Long.parseLong(finalValueItem[2])); }
								 */

								if (finalValueItem[3] != null && StringUtils.isNotEmpty(finalValueItem[3])
										&& !finalValueItem[3].equals("0"))
									patientPrescriptionDt.setDosage(finalValueItem[3]);

								if (finalValueItem[4] != null && StringUtils.isNotEmpty(finalValueItem[4])
										&& !finalValueItem[4].equals("0")) {
									patientPrescriptionDt.setFrequencyId(Long.parseLong(finalValueItem[4]));
								}

								if (finalValueItem[5] != null && StringUtils.isNotEmpty(finalValueItem[5])
										&& !finalValueItem[5].equals("0")) {
									patientPrescriptionDt.setNoOfDays(Long.parseLong(finalValueItem[5]));
								}

								if (finalValueItem[6] != null && StringUtils.isNotEmpty(finalValueItem[6])
										&& !finalValueItem[6].equals("0") && !finalValueItem[6].equals("NaN")) {
									patientPrescriptionDt.setTotal(Long.parseLong(finalValueItem[6]));
								}
								if (finalValueItem[7] != null && StringUtils.isNotEmpty(finalValueItem[7])
										&& !finalValueItem[7].equals("0")) {
									try {
										patientPrescriptionDt.setInstruction(finalValueItem[7]);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (finalValueItem[8] != null && StringUtils.isNotEmpty(finalValueItem[8])
										&& !finalValueItem[8].equals("0")) {
									patientPrescriptionDt.setStoreStock(Long.parseLong(finalValueItem[8]));
								}

								if (precriptionHdIdValue[0] != null
										&& StringUtils.isNotBlank(precriptionHdIdValue[0])) {
									patientPrescriptionDt.setPrescriptionHdId(
											Long.parseLong(precriptionHdIdValue[0].trim().toString()));
									patientPrescriptionHd = dgOrderhdDao.getPatientPrecriptionHdByPPHdId(
											Long.parseLong(precriptionHdIdValue[0].trim().toString()));
									// patientPrescriptionHd.setOtherTreatment(recommendedMedicalAdvice);
									if (StringUtils.isNotEmpty(userId))
										patientPrescriptionHd.setLastChgBy(Long.parseLong(userId));
									patientPrescriptionHd.setLastChgDate(new Timestamp(date.getTime()));
									dgOrderhdDao.saveOrUpdatePatientPrescriptionHd(patientPrescriptionHd);
								} else {
									visitId = payload.get("VisitID").toString();
									visitId = getReplaceString(visitId);

									patientPrescriptionHd = dgOrderhdDao
											.getPatientPrecriptionHdByVisitId(Long.parseLong(visitId.trim()));
									Long patientPrescriptionHdId = null;
									if (patientPrescriptionHd != null) {
										patientPrescriptionHdId = patientPrescriptionHd.getPrescriptionHdId();
										// patientPrescriptionHd.setOtherTreatment(recommendedMedicalAdvice);
									} else {
										patientPrescriptionHd = new PatientPrescriptionHd();
										String hospitalId = "";
										String patientId = "";
										try {
											hospitalId = payload.get("hospitalId").toString();
											hospitalId = getReplaceString(hospitalId);

											patientId = payload.get("patientId").toString();
											patientId = getReplaceString(patientId);
										} catch (Exception e) {
											e.printStackTrace();
										}
										patientPrescriptionHd.setVisitId(Long.parseLong(visitId.trim()));
										// patientPrescriptionHd.setOtherTreatment(recommendedMedicalAdvice);

										try {
											String opdPatientDetaiId = payload.get("opdPatientDetailId").toString();
											opdPatientDetaiId = getReplaceString(opdPatientDetaiId);
											patientPrescriptionHd
													.setOpdPatientDetailsId(Long.parseLong(opdPatientDetaiId));
											Date date1 = new Date();
											patientPrescriptionHd.setPrescriptionDate(new Timestamp(date1.getTime()));
										} catch (Exception e) {
											e.printStackTrace();
										}
										if (patientPrescriptionHd != null
												&& patientPrescriptionHd.getPrescriptionHdId() == null) {
											patientPrescriptionHd.setStatus("P");
										}
										//patientPrescriptionHd.setInjectionStatus("N");

										if (StringUtils.isNotBlank(hospitalId))
											patientPrescriptionHd.setMmuId(Long.parseLong(hospitalId.trim()));
										if (StringUtils.isNotBlank(patientId))
											patientPrescriptionHd.setPatientId(Long.parseLong(patientId));
									}
									if (StringUtils.isNotEmpty(userId)) {
										patientPrescriptionHd.setLastChgBy(Long.parseLong(userId));
										patientPrescriptionHd.setDoctorId(Long.parseLong(userId));
									}
									patientPrescriptionHd.setLastChgDate(new Timestamp(date.getTime()));
									patientPrescriptionHdId = dgOrderhdDao
											.saveOrUpdatePatientPrescriptionHd(patientPrescriptionHd);
									patientPrescriptionDt.setPrescriptionHdId(patientPrescriptionHdId);
								}
								patientPrescriptionDt.setLastChgDate(new Timestamp(date.getTime()));
								if (finalValueItem[11] != null && StringUtils.isNotEmpty(finalValueItem[11])
										&& !finalValueItem[11].equals("0")) {
									patientPrescriptionDt.setStatus(finalValueItem[11].toString());
								}
								//patientPrescriptionDt.setItemStopStatus(0l);
								patientPrescriptionDt.setItemId(itenIdValue);
								dgOrderhdDao.saveOrUpdatePatientPrecriptionDt(patientPrescriptionDt);
							}
						}
					}
				}
			}
			counter++;
		}

		/*
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}
@Transactional
	public OpdPatientDetail saveOpdDisposableDetails(HashMap<String, Object> payload,OpdPatientDetail opdPatientDetail,String patientId, String visitID,String userId) {
	String disposalId = "";
	if (payload.get("disposalId") != null) {
		disposalId = payload.get("disposalId").toString();
		disposalId = getReplaceString(disposalId);
	}

	String disposalFinalVal = "";
	String disposalCodeFinalVal = "";
	String[] disposalIdArray = null;
	String[] disposalCodeArray = null;
	if (StringUtils.isNotEmpty(disposalId)) {
		disposalIdArray = disposalId.split(",");

		if (disposalIdArray != null) {
			for (String ss : disposalIdArray) {
				if (StringUtils.isNotEmpty(ss)) {
					String[] disposalTemp = ss.split("@");
					disposalFinalVal += disposalTemp[0] + ",";
					disposalCodeFinalVal+=disposalTemp[1] + ",";
				}
			}
		}

	}
	if (StringUtils.isNotEmpty(disposalFinalVal)) {
		disposalIdArray = disposalFinalVal.split(",");
	}
	if (StringUtils.isNotEmpty(disposalCodeFinalVal)) {
		disposalCodeArray = disposalCodeFinalVal.split(",");
	}
	String opdDisposableId = "";
	if (payload.get("opdDisposableId") != null) {
		opdDisposableId = payload.get("opdDisposableId").toString();
		opdDisposableId = getReplaceString(opdDisposableId);
	}
	String[] opdDisposableIdArray = null;

	if (StringUtils.isNotEmpty(opdDisposableId)) {
		opdDisposableIdArray = opdDisposableId.split(",");
	}
	String disposalDays = "";
	if (payload.get("disposalDays") != null) {
		disposalDays = payload.get("disposalDays").toString();
		disposalDays = getReplaceString(disposalDays);
	}
	String[] disposalDaysArray = null;

	if (StringUtils.isNotEmpty(disposalDays)) {
		disposalDaysArray = disposalDays.split(",");
	}
	String hospitalId = "";
	if (payload.get("hospitalId") != null) {
		hospitalId = payload.get("hospitalId").toString();
		hospitalId = getReplaceString(hospitalId);
	}
	Integer counter = 1;
	String finalValue = "";
	String visitId = "";
	HashMap<String, String> mapInvestigationMap = new HashMap<>();
	if (disposalIdArray != null && disposalIdArray.length > 0)
		for (int i = 0; i < disposalIdArray.length; i++) {

			if (StringUtils.isNotEmpty(disposalIdArray[i].toString())
					&& !disposalIdArray[i].equalsIgnoreCase("0")) {

				finalValue += disposalIdArray[i].trim();

				if (opdDisposableIdArray != null && i < opdDisposableIdArray.length
						&& !opdDisposableIdArray[i].equalsIgnoreCase("0")
						&& StringUtils.isNotBlank(opdDisposableIdArray[i])) {
					finalValue += "," + opdDisposableIdArray[i].trim();

				} else {
					finalValue += "," + "0";
				}

				if (disposalDaysArray != null && i < disposalDaysArray.length
						&& !disposalDaysArray[i].equalsIgnoreCase("0")
						&& StringUtils.isNotBlank(disposalDaysArray[i])) {
					finalValue += "," + disposalDaysArray[i].trim();

				} else {
					finalValue += "," + "0";
				}
				
				if (disposalCodeArray != null && i < disposalCodeArray.length
						&& !disposalCodeArray[i].equalsIgnoreCase("0")
						&& StringUtils.isNotBlank(disposalCodeArray[i])) {
					finalValue += "," + disposalCodeArray[i].trim();

				} else {
					finalValue += "," + "0";
				}
				
				mapInvestigationMap.put(disposalIdArray[i].toString().trim() + "@#" + counter, finalValue);
				finalValue = "";

			}
			counter++;
		}

	counter = 1;
	if (disposalIdArray != null)
		for (String disposableVal : disposalIdArray) {}

	return opdPatientDetail;
}


	@Override
	public String updateOpdTreatmentTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String opdT = null;
		
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		OpdTemplate opdTemp = new OpdTemplate();
		//OpdTemplateInvestigation opdTempInv = new OpdTemplateInvestigation();
		try {
			
		if (!jsondata.isEmpty())
		{
		//opdTemp.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
		 
		opdTemp.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("doctorId"))));
		//opdTemp.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
		opdTemp.setTemplateCode(String.valueOf(jsondata.get("templateCode")));
		opdTemp.setTemplateName(String.valueOf(jsondata.get("templateName")));
		opdTemp.setTemplateType(String.valueOf(jsondata.get("templateType")));
		if(jsondata.get("templateId")!=null && jsondata.get("templateId")!="")
		{
			opdTemp.setTemplateId(Long.parseLong(String.valueOf(jsondata.get("templateId"))));
		}
		//opdTempInv.setStatus(status);
		
		List<HashMap<String, Object>> listofOpdTemp = (List<HashMap<String, Object>>) jsondata.get("listofTreatmentTemplate");
		    List<OpdTemplateTreatment>  opdTreatmentList= new ArrayList<>();
		   
		for (HashMap<String, Object> singleopd: listofOpdTemp)
		{
			OpdTemplateTreatment ob1=new OpdTemplateTreatment();
			if(singleopd.get("itemId")!=null && singleopd.get("itemId")!="")
			{
			if(singleopd.get("treatmentItemPId")!=null &&singleopd.get("treatmentItemPId")!="")
			{	
			 ob1.setTreatmentTemplateId(Long.valueOf(singleopd.get("treatmentItemPId").toString()));	
			}
			ob1.setItemId(Long.valueOf(singleopd.get("itemId").toString()));
			ob1.setDosage(singleopd.get("dosage").toString());
			ob1.setFrequencyId(Long.valueOf(singleopd.get("frequencyId").toString()));
			ob1.setNoofdays(Long.valueOf(singleopd.get("noOfDays").toString()));
			ob1.setTotal(Long.valueOf(singleopd.get("total").toString()));
			ob1.setInstruction(String.valueOf(singleopd.get("instruction")));
			if(jsondata.get("templateId")!=null && jsondata.get("templateId")!="")
			{
			ob1.setTemplateId(Long.parseLong(String.valueOf(jsondata.get("templateId"))));
			}
			opdTreatmentList.add(ob1);
			}
			
		}
				
		 opdT = od.opdupdateTreatmentTemplate(opdTemp,opdTreatmentList);
		 
		  if (opdT != null  && opdT.equalsIgnoreCase("200"))
		  {
				json.put("msg", "Opd Template Details Saved successfully ");
				json.put("status", "1");
		  } else if (opdT != null && opdT.equalsIgnoreCase("403"))
		  {
				json.put("msg", " you are not authorized for this activity ");
				json.put("status", "0");
		   } else
		   {		
			   json.put("msg", opdT);
				json.put("status", "0");
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
	public String saveOpdMedicalAdviceTemplates(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String opdT = null;
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		OpdTemplate opdTemp = new OpdTemplate();
		//OpdTemplateInvestigation opdTempInv = new OpdTemplateInvestigation();
		
		try {
			
		if (!jsondata.isEmpty())
		{
		//opdTemp.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
		opdTemp.setDoctorId(Long.parseLong(String.valueOf(jsondata.get("doctorId"))));
		//opdTemp.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
		opdTemp.setTemplateCode(String.valueOf(jsondata.get("templateCode")));
		opdTemp.setTemplateName(String.valueOf(jsondata.get("templateName")));
		opdTemp.setTemplateType(String.valueOf(jsondata.get("templateType")));
		
		OpdTemplateMedicalAdvice opdMedicalAdvice=new OpdTemplateMedicalAdvice();
		opdMedicalAdvice.setMedicalAdvice(String.valueOf(jsondata.get("medicalAdice")));
		opdMedicalAdvice.setLastChgDate(ourJavaTimestampObject);
				
		 opdT = od.opdTemplateMedicalAdvice(opdTemp,opdMedicalAdvice);
		 
		  if (opdT != null  && opdT.equalsIgnoreCase("200"))
		  {
				json.put("msg", "Opd Template Details Saved successfully ");
				json.put("status", "1");
		  } else if (opdT != null && opdT.equalsIgnoreCase("403"))
		  {
				json.put("msg", " you are not authorized for this activity ");
				json.put("status", "0");
		   } else
		   {		
			   json.put("msg", opdT);
				json.put("status", "0");
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
	public String saveorUpdateChildImunization(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String childVac = null;
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		//ChildVacatinationChart cvc = new ChildVacatinationChart();
		//OpdTemplateInvestigation opdTempInv = new OpdTemplateInvestigation();
		
		try {
			
		if (!jsondata.isEmpty())
		{
		//opdTemp.setDepartmentId(Long.parseLong(String.valueOf(jsondata.get("departmentId"))));
			List<HashMap<String, Object>> listofChildVacatination = (List<HashMap<String, Object>>) jsondata.get("listofChildVacatination");
		    List<ChildVacatinationChart>  childVacatination= new ArrayList<>();
		   
		    for (HashMap<String, Object> singleopd: listofChildVacatination)
			{	
		    ChildVacatinationChart cvc=new ChildVacatinationChart();
		    cvc.setVisitId(Long.parseLong(String.valueOf(jsondata.get("visitId"))));
			cvc.setPatientId(Long.parseLong(String.valueOf(jsondata.get("patientId"))));
			cvc.setLastChgBy(Long.parseLong(String.valueOf(jsondata.get("userId"))));
			cvc.setHospitalId(Long.parseLong(String.valueOf(jsondata.get("hospitalId"))));
			if(singleopd.get("chartId")!=null &&singleopd.get("chartId")!="")
			{	
				cvc.setChartId(Long.valueOf(singleopd.get("chartId").toString()));	
			}
			cvc.setOrderNo(Long.parseLong(String.valueOf(singleopd.get("orderNo"))));
			cvc.setAgeType(String.valueOf(singleopd.get("ageType")));
			cvc.setVaccineType(String.valueOf(singleopd.get("vaccineType")));
			if(singleopd.get("dueDate")!=null && singleopd.get("dueDate")!="")
			{	
				Date dueDate = HMSUtil.convertStringTypeDateToDateType(singleopd.get("dueDate").toString());
				cvc.setDueDate(dueDate);
			}
			cvc.setGivenFlag("Y");
			if(singleopd.get("givenDate")!=null && singleopd.get("givenDate")!="")
			{	
				Date givenDate = HMSUtil.convertStringTypeDateToDateType(singleopd.get("givenDate").toString());
				cvc.setGivenDate(givenDate);
			}
			cvc.setPlaceOfVacatination(String.valueOf(singleopd.get("placeOfVacatination")));
			cvc.setRemarks(String.valueOf(singleopd.get("remarks")));
			cvc.setLastChgDate(ourJavaTimestampObject);
			childVacatination.add(cvc);
			}	
			childVac = od.childVacatinationChart(childVacatination);
		 
		  if (childVac != null  && childVac.equalsIgnoreCase("200"))
		  {
				json.put("msg", "Opd Child Vacatination Details Saved successfully ");
				json.put("status", "1");
		  } else if (childVac != null && childVac.equalsIgnoreCase("403"))
		  {
				json.put("msg", " you are not authorized for this activity ");
				json.put("status", "0");
		   } else
		   {		
			   json.put("msg", childVac);
				json.put("status", "0");
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
	public String getChildImunizationRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

			//List<Visit> getvisit = od.getVisit(jsondata);
			Long patientid=Long.parseLong(jsondata.get("patientId").toString());
			List<ChildVacatinationChart> getChildVacatination = od.getchildVacatinationChart(patientid);
			
			if (getChildVacatination.size() == 0) {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		
		else 
		{
			if (getChildVacatination != null && ! CollectionUtils.isEmpty(getChildVacatination) ) {
				
				try {
					for (ChildVacatinationChart v : getChildVacatination) {
						//if (v.getVisitStatus().equals("w")||v.getVisitStatus().equals("p")){					
								HashMap<String, Object> pt = new HashMap<String, Object>();
									
								pt.put("chartId",v.getChartId());
								
								if(v.getOrderNo()!=null)
								{	
								pt.put("orderNo",v.getOrderNo());
								}
								else
								{
									pt.put("orderNo","");	
								}
								if(v.getAgeType()!=null)
								{	
								pt.put("ageType",v.getAgeType());
								}
								else
								{
								pt.put("ageType","");	
								}
								if(v.getVaccineType()!=null)
								{	
								pt.put("vaccineType",v.getVaccineType());
								}
								else
								{
									pt.put("vaccineType","");	
								}
								if(v.getDueDate()!=null)
								{
									Date vd=(v.getDueDate());
									Calendar lCal = Calendar.getInstance();
								    lCal.setTime(vd);
					                int yr=lCal.get(Calendar.YEAR);
					                int mn=lCal.get(Calendar.MONTH) + 1;
					                int dt=lCal.get(Calendar.DATE);
					                
					               
					                LocalDate dueDateC = LocalDate.of(yr,mn,dt) ; //Birth date
					                
					                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
					                String text = dueDateC.format(formatters);
					                
					                String dueDate = text;
								pt.put("dueDate", dueDate);
								}
								else
								{
									pt.put("dueDate","");	
								}
								if(v.getGivenDate()!=null)
								{
									Date vd=(v.getGivenDate());
									Calendar lCal = Calendar.getInstance();
								    lCal.setTime(vd);
					                int yr=lCal.get(Calendar.YEAR);
					                int mn=lCal.get(Calendar.MONTH) + 1;
					                int dt=lCal.get(Calendar.DATE);
					                
					               
					                LocalDate givenDateC = LocalDate.of(yr,mn,dt) ; //Birth date
					                
					                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
					                String text = givenDateC.format(formatters);
					                
					                String givenDate = text;	
								pt.put("givenDate",givenDate);
								}
								else
								{
									pt.put("givenDate","");	
								}
								if(v.getGivenFlag()!=null)
								{	
									pt.put("givenFlag",v.getGivenFlag());
								}
								else
								{
									pt.put("givenFlag","");
								}
								if(v.getPlaceOfVacatination()!=null)
								{	
								pt.put("placeOfVacatination",v.getPlaceOfVacatination());
								}
								else
								{
									pt.put("placeOfVacatination","");	
								}
								if( v.getRemarks()!=null)
								{	
								pt.put("remarks", v.getRemarks());
								}
								else
								{
									pt.put("remarks", "");	
								}
												
								c.add(pt);
					
					        }
							json.put("data", c);
							json.put("msg", "Child Vacatination Chart List  get  sucessfull... ");
							json.put("status", "1");

					}
				
	
				catch(Exception e)
				{
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}
				} 
			else {
				try {
						json.put("msg", "Patient ID data not found");
						json.put("status", 0);
					}
				    catch (JSONException e)
				    {
						e.printStackTrace();
					}
				}
			}
		
		}
		return json.toString();
	
	}

	/*@Override
	public String checkAuthenticateEHR(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		if (jsondata != null) {

			String data = od.checkAuthenticateEHR(jsondata);
			if (!data.isEmpty() && data.equalsIgnoreCase("success")) {
				jsonObj.put("status", 1);
				jsonObj.put("data", data);
				jsonObj.put("msg", "Authenticate Service No");
			} else if (!data.isEmpty() && data.equalsIgnoreCase("failure")) {
				jsonObj.put("status", 0);
				jsonObj.put("data", data);
				jsonObj.put("msg", "service No does not exist in this Unit");
			}

		}
		return jsonObj.toString();
	}*/
 	
	/*@Override
	public String authenticateUHID(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

		String data = od.authenticateUHID(jsondata);
		if (data != null && data.equalsIgnoreCase("success")) {
		jsonObj.put("data", data);
		jsonObj.put("status", 1);
		jsonObj.put("msg", "Authenticate user");
		
		} else {
			jsonObj.put("data", data);
			jsonObj.put("status", 0);
		jsonObj.put("msg", "Error occured");
		}
		}
		return jsonObj.toString();
	}*/
	
	@Override
	public String getPatientIdUHIDWise(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		if (jsondata != null) {
		Long patientId = od.getPatientIdUHIDWise(jsondata);
		if (patientId != null ) {
		jsonObj.put("patientId", patientId);
		jsonObj.put("status", 1);
		jsonObj.put("msg", "Authenticate user");
		
		} else {
			jsonObj.put("patientId", patientId);
			jsonObj.put("status", 0);
		jsonObj.put("msg", "Patient Does not Exist");
		}
		}
		return jsonObj.toString();
	}
	
	@Override
	public Map<String, Object> patientListForEmployeeAndDependent(Map<String, String> requestData) {
		

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> patientListofEmpAndDependent = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		/*List<Patient> existingPatientList= new ArrayList<Patient>();
		List<MasEmployee> empAndDependentPatientList= new ArrayList<MasEmployee>();
		List<MasEmployeeDependent> dependentPatientList= new ArrayList<MasEmployeeDependent>();
		
		String serviceNo = "";
		String name="";
		String dateOfBirth="";
		String empServiceJoinDate="";
		String age="0";
		String empName="";
		String relation="";
		long genderId=0;
		long relationId=0;
		long empRankId=0;
		long empTradeId=0;
		long employeeId=0;
		long empUnitId=0;
		long empCommandId=0;
		long empRecordOfficeId=0;
		long empMaritalStatusId=0;
		Long employeeCategoryId=null;
		MasEmployee employeeObject=null;
		if (!requestData.isEmpty() && requestData != null ) {
			if (requestData.get("serviceNo") != null && !requestData.get("serviceNo").isEmpty()) {
				serviceNo = requestData.get("serviceNo").trim();
				employeeObject = patientRegDao.getMasEmployeeFromServiceNo(serviceNo);
				if(employeeObject!=null) {
				patientListofEmpAndDependent = patientRegDao.findPatientAndDependentFromEmployee(serviceNo);
				if(patientListofEmpAndDependent.size()>0) {
					existingPatientList = (List<Patient>)patientListofEmpAndDependent.get("patientList");
					if(existingPatientList.size()>0) {
						int rowCount=0;
						for(Patient patient : existingPatientList) {
							Map<String, Object> responsePatientMap = new HashMap<String, Object>();
							String patientName = "";
							if (patient.getPatientName() != null) {
								patientName = patient.getPatientName().trim();
							}
							
							responsePatientMap.put("Id", ++rowCount);
							responsePatientMap.put("patientId", patient.getPatientId()!=null ? patient.getPatientId():"");
							responsePatientMap.put("uhidNo", patient.getUhidNo()!=null ? patient.getUhidNo():"");
							responsePatientMap.put("serviceNo",patient.getServiceNo()!=null ? patient.getServiceNo():"");
							responsePatientMap.put("name",patientName!=null ?patientName :"");
							responsePatientMap.put("genderId",patient.getMasAdministrativeSex()!=null && patient.getMasAdministrativeSex().getAdministrativeSexId() !=null ? patient.getMasAdministrativeSex().getAdministrativeSexId():"");
							responsePatientMap.put("empRankId",patient.getRankId()!=null ? patient.getRankId():"");
							responsePatientMap.put("empRankName",patient.getMasRank()!=null && patient.getMasRank().getRankName()!=null ? patient.getMasRank().getRankName():"");
							responsePatientMap.put("empUnitId",patient.getUnitId()!=null ? patient.getUnitId():"");
							responsePatientMap.put("empUnitName",patient.getMasUnit()!=null && patient.getMasUnit().getUnitName()!=null ? patient.getMasUnit().getUnitName():"");
							responsePatientMap.put("empName",patient.getEmployeeName()!=null ? patient.getEmployeeName():"");
							responsePatientMap.put("relation",patient.getMasRelation()!=null && patient.getMasRelation().getRelationName()!=null ? patient.getMasRelation().getRelationName():"");
							responsePatientMap.put("dateOfBirth",patient.getDateOfBirth()!=null ? HMSUtil.changeDateToddMMyyyy(patient.getDateOfBirth()):"");
							responsePatientMap.put("age", patient.getDateOfBirth() !=null ? HMSUtil.calculateAge(patient.getDateOfBirth()) :"");
							responsePatientMap.put("employeeCategoryId", patient.getEmployeeCategoryId()!=null ? patient.getEmployeeCategoryId():"");
							responsePatientMap.put("relationId", patient.getRelationId()!=null ? patient.getRelationId():"");
							responsePatientMap.put("empServiceJoinDate", patient.getServiceJoinDate()!=null ? HMSUtil.convertDateToStringFormat(patient.getServiceJoinDate(), "dd/MM/yyyy"):"");
							responsePatientMap.put("empRecordOfficeId", patient.getRecordOfficeAddressId()!=null ? patient.getRecordOfficeAddressId():"");
							
							data.put(++rowCount, responsePatientMap);
						}
						
						dependentPatientList = (List<MasEmployeeDependent>)patientListofEmpAndDependent.get("employeeDependentList");
						if(dependentPatientList.size()>0  ) {
							for(MasEmployeeDependent depList :dependentPatientList ) {
								Map<String, Object> responseDepMap = new HashMap<String, Object>();
								String depPatientName = "";
								if (depList.getEmployeeDependentName() != null) {
									depPatientName = depList.getEmployeeDependentName().trim();
								}
								//Patient related data
								name = depPatientName;
								if(depList.getDateOfBirth()!=null) {
									dateOfBirth=HMSUtil.changeDateToddMMyyyy(depList.getDateOfBirth());
									age=HMSUtil.calculateAge(depList.getDateOfBirth());
								}else {
									dateOfBirth="";
								}
								
								genderId = depList.getMasAdministrativeSex()!=null?depList.getMasAdministrativeSex().getAdministrativeSexId():0;
								relationId=depList.getMasRelation()!=null?depList.getMasRelation().getRelationId():0;
								relation= od.getRelationName(relationId);
								//Employee related 
								serviceNo= employeeObject.getServiceNo();
								String empRankCode = employeeObject.getMasRank()!=null? employeeObject.getMasRank():"0";
								List<MasRank> mRankList = patientRegDao.getEmpRankAndTrade(empRankCode);
								if(!mRankList.isEmpty() && mRankList.size()>0) {
									empRankId = mRankList.get(0).getRankId();
									empTradeId = mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().getTradeId():0;
									
								}else {
									empRankId = 0;
									empTradeId = 0;
								}
								empName= employeeObject.getEmployeeName()!=null?employeeObject.getEmployeeName():""; 
								employeeId = employeeObject.getEmployeeId();
								if(employeeObject.getDoe()!=null) {
									empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(employeeObject.getDoe());
								}else {
									empServiceJoinDate="";
								}
								
								// This getMasUnit() gives unitCode not unit object. 
								String empUnitCode = employeeObject.getMasUnit()!=null?employeeObject.getMasUnit():"0";
								List<MasUnit> unitList = patientRegDao.getEmpUnitId(empUnitCode);
								if(unitList.size()>0) {
									empUnitId = unitList.get(0).getUnitId();
									if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null && unitList.get(0).getMasStation().getMasCommand()!=null) {
										empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId(); // This is station Id
									}else {
										empCommandId=0;
									}
								}else {
									empUnitId = 0;
								}
								empRecordOfficeId=employeeObject.getMasRecordOfficeAddress()!=null?employeeObject.getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
								empMaritalStatusId=employeeObject.getMasMaritalStatus()!=null?employeeObject.getMasMaritalStatus().getMaritalStatusId():0;
								employeeCategoryId = employeeObject.getMasEmployeeCategory()!=null && employeeObject.getMasEmployeeCategory().getEmployeeCategoryId()!=null ? employeeObject.getMasEmployeeCategory().getEmployeeCategoryId():0;
								
								responseDepMap.put("Id", ++rowCount);
								responseDepMap.put("uhidNo", "");
								responseDepMap.put("patientId","0");
								responseDepMap.put("employeeId", employeeId);
								responseDepMap.put("name", name);
								responseDepMap.put("genderId", genderId);
								responseDepMap.put("dateOfBirth", dateOfBirth);
								responseDepMap.put("relationId", relationId);
								responseDepMap.put("serviceNo",serviceNo);
								responseDepMap.put("empRankId",empRankId);
								responseDepMap.put("empTradeId",empTradeId);
								responseDepMap.put("empName",empName);
								responseDepMap.put("age",age);
								responseDepMap.put("relation",relation);
								responseDepMap.put("empServiceJoinDate",empServiceJoinDate);
								responseDepMap.put("empUnitId",empUnitId);
								responseDepMap.put("empCommandId",empCommandId);
								responseDepMap.put("empRecordOfficeId",empRecordOfficeId);
								responseDepMap.put("empMaritalStatusId",empMaritalStatusId);
								responseDepMap.put("employeeCategoryId",employeeCategoryId);
								data.put(++rowCount, responseDepMap);
								
							}
						}
						
						
						empAndDependentPatientList = (List<MasEmployee>)patientListofEmpAndDependent.get("employeeList");
							if(empAndDependentPatientList.size()>0  ) {
							for(MasEmployee ms : empAndDependentPatientList) {
								
								String selfRelationCode = HMSUtil.getProperties("adt.properties", "SELF_RELATION_CODE").trim();
								relationId =  patientRegDao.getRelationIdFromCode(selfRelationCode);
								relation=od.getRelationName(relationId);
								
								Map<String, Object> responseEmpMap = new HashMap<String, Object>();
								String patientName = "";
								if (ms.getEmployeeName() != null) {
									patientName = ms.getEmployeeName().trim();
								}
								serviceNo= ms.getServiceNo();
								employeeId=ms.getEmployeeId();
								
								String empRankCode = ms.getMasRank()!=null? ms.getMasRank():"0";
								List<MasRank> mRankList = patientRegDao.getEmpRankAndTrade(empRankCode);
								if(!mRankList.isEmpty() && mRankList.size()>0) {
									empRankId = mRankList.get(0).getRankId();
									empTradeId = mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().getTradeId():0;
									
								}else {
									empRankId = 0;
									empTradeId = 0;
								}
							
								empName= patientName;
								if(ms.getDoe()!=null) {
									empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(ms.getDoe());
								}else {
									empServiceJoinDate="";
								}
								
								if(ms.getDob()!=null) {
									dateOfBirth=HMSUtil.changeDateToddMMyyyy(ms.getDoe());
									age=HMSUtil.calculateAge(ms.getDoe());
								}else {
									dateOfBirth="";
								}
								
								// This getMasUnit gives unit code not unit object
								String empUnitCode = ms.getMasUnit()!=null?ms.getMasUnit():"0";
								List<MasUnit> unitList = patientRegDao.getEmpUnitId(empUnitCode);
								if(unitList.size()>0) {
									empUnitId = unitList.get(0).getUnitId();
									if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null && unitList.get(0).getMasStation().getMasCommand()!=null) {
										empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId(); // This is station Id
									}else {
										empCommandId=0;
									}
									
								}else {
									empUnitId = 0;
								}
								
								empRecordOfficeId=ms.getMasRecordOfficeAddress()!=null?ms.getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
								empMaritalStatusId=ms.getMasMaritalStatus()!=null?ms.getMasMaritalStatus().getMaritalStatusId():0;
								
								name = patientName;
								genderId = ms.getMasAdministrativeSex()!=null?ms.getMasAdministrativeSex().getAdministrativeSexId():0;
								employeeCategoryId = ms.getMasEmployeeCategory()!=null && ms.getMasEmployeeCategory().getEmployeeCategoryId()!=null ? ms.getMasEmployeeCategory().getEmployeeCategoryId() :0;
								
								responseEmpMap.put("Id", ++rowCount);
								responseEmpMap.put("uhidNo", "");
								responseEmpMap.put("patientId","0");
								responseEmpMap.put("employeeId", employeeId);
								responseEmpMap.put("name", name);
								responseEmpMap.put("genderId", genderId);
								responseEmpMap.put("dateOfBirth", dateOfBirth);
								responseEmpMap.put("relationId", relationId);
								responseEmpMap.put("relation", relation);
								responseEmpMap.put("serviceNo",serviceNo);
								responseEmpMap.put("empRankId",empRankId);
								responseEmpMap.put("empTradeId",empTradeId);
								responseEmpMap.put("empName",empName);
								responseEmpMap.put("empServiceJoinDate",empServiceJoinDate);
								responseEmpMap.put("empUnitId",empUnitId);
								responseEmpMap.put("empCommandId",empCommandId);
								responseEmpMap.put("empRecordOfficeId",empRecordOfficeId);
								responseEmpMap.put("empMaritalStatusId",empMaritalStatusId);
								responseEmpMap.put("employeeCategoryId",employeeCategoryId);
								responseEmpMap.put("age",age);
								
								
								data.put(++rowCount, responseEmpMap);
							}
						}
							map.put("data", data);
							map.put("count", data.size());
							map.put("msg","List of Detail");
							map.put("status", "1");
							return map;
							
					}else {
						empAndDependentPatientList = (List<MasEmployee>)patientListofEmpAndDependent.get("employeeList");
						int rowCount=0;
						
						for (MasEmployee ms : empAndDependentPatientList) {
							String selfRelationCode = HMSUtil.getProperties("adt.properties", "SELF_RELATION_CODE").trim();
							relationId =  patientRegDao.getRelationIdFromCode(selfRelationCode);
							relation=od.getRelationName(relationId);
							Map<String, Object> responseEmpMap = new HashMap<String, Object>();
						
							String patientName = "";
							if (ms.getEmployeeName() != null) {
								patientName = ms.getEmployeeName();
							}

							empName= patientName;
							serviceNo = ms.getServiceNo();
							employeeId = ms.getEmployeeId();
							
							String empRankCode = ms.getMasRank()!=null? ms.getMasRank():"0";
							List<MasRank> mRankList = patientRegDao.getEmpRankAndTrade(empRankCode);
							if(!mRankList.isEmpty() && mRankList.size()>0) {
								empRankId = mRankList.get(0).getRankId();
								empTradeId = mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().getTradeId():0;
								
							}else {
								empRankId = 0;
								empTradeId = 0;
							}
							
							if(ms.getDoe()!=null) {
								empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(ms.getDoe());
							}else {
								empServiceJoinDate="";
							}
							
							if(ms.getDob()!=null) {
								dateOfBirth =HMSUtil.changeDateToddMMyyyy(ms.getDob());
								age=HMSUtil.calculateAge(ms.getDob());
							}else {
								dateOfBirth="";
							}
							
							
							String empUnitCode = ms.getMasUnit()!=null?ms.getMasUnit():"0";
							
							List<MasUnit> unitList = patientRegDao.getEmpUnitId(empUnitCode);
							if(!unitList.isEmpty() && unitList.size()>0) {
								empUnitId = unitList.get(0).getUnitId();
								if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null && unitList.get(0).getMasStation().getMasCommand()!=null) {
									empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId(); // This is station Id
								}else {
									empCommandId=0;
								}
								
							}else {
								empUnitId = 0;
							}
							
							empRecordOfficeId=ms.getMasRecordOfficeAddress()!=null?ms.getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
							empMaritalStatusId=ms.getMasMaritalStatus()!=null?ms.getMasMaritalStatus().getMaritalStatusId():0;
							
							name = patientName;
							genderId = ms.getMasAdministrativeSex()!=null?ms.getMasAdministrativeSex().getAdministrativeSexId():0;
							employeeCategoryId = ms.getMasEmployeeCategory()!=null && ms.getMasEmployeeCategory().getEmployeeCategoryId()!=null ? ms.getMasEmployeeCategory().getEmployeeCategoryId() :0;
							
							responseEmpMap.put("Id", ++rowCount);
							responseEmpMap.put("uhidNo", "");
							responseEmpMap.put("employeeId", employeeId);
							responseEmpMap.put("patientId","0");
							responseEmpMap.put("name", name);
							responseEmpMap.put("genderId", genderId);
							responseEmpMap.put("dateOfBirth", dateOfBirth);
							responseEmpMap.put("relationId", relationId);
							responseEmpMap.put("relation", relation);
							responseEmpMap.put("serviceNo",serviceNo);
							responseEmpMap.put("empRankId",empRankId);
							responseEmpMap.put("empTradeId",empTradeId);
							responseEmpMap.put("empName",empName);
							responseEmpMap.put("empServiceJoinDate",empServiceJoinDate);
							responseEmpMap.put("empUnitId",empUnitId);
							responseEmpMap.put("empCommandId",empCommandId);
							responseEmpMap.put("empRecordOfficeId",empRecordOfficeId);
							responseEmpMap.put("empMaritalStatusId",empMaritalStatusId);
							responseEmpMap.put("employeeCategoryId",employeeCategoryId);
							responseEmpMap.put("age",age);
							
							data.put(++rowCount, responseEmpMap);

							List<MasEmployeeDependent> dependentList = (List<MasEmployeeDependent>)patientListofEmpAndDependent.get("employeeDependentList");
							if (dependentList.size()>0) {
								for (MasEmployeeDependent depList : dependentList) {
									Map<String, Object> responseDepMap = new HashMap<String, Object>();
									String depPatientName = "";
									if (depList.getEmployeeDependentName() != null) {
										depPatientName = depList.getEmployeeDependentName().trim();
									}
									name = depPatientName;
									if(depList.getDateOfBirth()!=null) {
										dateOfBirth = HMSUtil.changeDateToddMMyyyy(depList.getDateOfBirth());
										age=HMSUtil.calculateAge(depList.getDateOfBirth());
									}else {
										dateOfBirth = "";
									}
									
									genderId = depList.getMasAdministrativeSex()!=null?depList.getMasAdministrativeSex().getAdministrativeSexId():0;
									employeeCategoryId = depList.getMasEmployee()!=null && depList.getMasEmployee().getMasEmployeeCategory()!=null && depList.getMasEmployee().getMasEmployeeCategory().getEmployeeCategoryId()!=null ? depList.getMasEmployee().getMasEmployeeCategory().getEmployeeCategoryId():0; 
									
									relationId=depList.getMasRelation()!=null?depList.getMasRelation().getRelationId():0;
									relation=od.getRelationName(relationId);
									
									//Employee related 
									serviceNo= employeeObject!=null?employeeObject.getServiceNo():"";
									if(mRankList.size()>0) {
										empRankId = mRankList.get(0).getRankId();
										empTradeId = mRankList.get(0).getMasTrade()!=null?mRankList.get(0).getMasTrade().getTradeId():0;
										
									}else {
										empRankId = 0;
										empTradeId = 0;
									}
									// This getMasUnit() gives unitCode not unit object. 
									if(unitList.size()>0) {
										empUnitId = unitList.get(0).getUnitId();
										if(unitList.get(0)!=null && unitList.get(0).getMasStation()!=null && unitList.get(0).getMasStation().getMasCommand()!=null) {
											empCommandId=unitList.get(0).getMasStation().getMasCommand().getCommandId(); // This is station Id
										}else {
											empCommandId=0;
										}
									}else {
										empUnitId = 0;
									}
									empName= employeeObject!=null?employeeObject.getEmployeeName():""; 
									employeeId =employeeObject!=null?employeeObject.getEmployeeId():0;
									if(employeeObject.getDoe()!=null) {
										empServiceJoinDate=HMSUtil.changeDateToddMMyyyy(employeeObject.getDoe());
									}else {
										empServiceJoinDate="";
									}
							
									empRecordOfficeId=employeeObject.getMasRecordOfficeAddress()!=null?employeeObject.getMasRecordOfficeAddress().getRecordOfficeAddressId():0;
									empMaritalStatusId=employeeObject.getMasMaritalStatus()!=null?employeeObject.getMasMaritalStatus().getMaritalStatusId():0;
									
									responseDepMap.put("Id", ++rowCount);
									responseDepMap.put("uhidNo", "");
									responseDepMap.put("employeeId", employeeId);
									responseDepMap.put("patientId","0");
									responseDepMap.put("name", name);
									responseDepMap.put("genderId", genderId);
									responseDepMap.put("dateOfBirth", dateOfBirth);
									responseDepMap.put("relationId", relationId);
									responseDepMap.put("relation", relation);
									responseDepMap.put("serviceNo",serviceNo);
									responseDepMap.put("empRankId",empRankId);
									responseDepMap.put("empTradeId",empTradeId);
									responseDepMap.put("empName",empName);
									responseDepMap.put("empServiceJoinDate",empServiceJoinDate);
									responseDepMap.put("empUnitId",empUnitId);
									responseDepMap.put("empCommandId",empCommandId);
									responseDepMap.put("empRecordOfficeId",empRecordOfficeId);
									responseDepMap.put("employeeCategoryId",employeeCategoryId);
									responseDepMap.put("age",age);
									
									data.put(++rowCount, responseDepMap);
								}
							}
							map.put("data", data);
							map.put("count", data.size());
							map.put("msg","List of Detail");
							map.put("status", "1");
							return map;
						}
					}
				}
				
				}
			}
		}
			map.put("msg","Service No. does not exist.");
			map.put("count", data.size());
			map.put("status", "0");*/
			return map;
	
	}
	
	@Override
	public String getUnitList(JSONObject jsonObject) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		List<MasUnit> unitList = od.getUnitList(jsonObject);
		if(CollectionUtils.isNotEmpty(unitList)) {
			unitList.forEach(unit -> {
				Map<String, Object> mData = new HashMap<String, Object>();
				mData.put("unitId", unit.getUnitId()!=null ? unit.getUnitId():"");
				mData.put("unitName", unit.getUnitName()!=null ? unit.getUnitName():"");
				list.add(mData);
			});
			json.put("data", list);
			json.put("status", 1);
			json.put("msg", "Record Fetch Successfully");
		}else {
			json.put("status", 0);
			json.put("msg", "No Record Found");
		}
		return json.toString();
	
	}
	
	@Override
	public String getRankList(JSONObject jsonObject) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		List<MasRank> masRankList = od.getRankList();
		if(CollectionUtils.isNotEmpty(masRankList)) {
			masRankList.forEach(rank -> {
				Map<String, Object> mData = new HashMap<String, Object>();
				mData.put("rankId", rank.getRankId()!=0 ? rank.getRankId():"");
				mData.put("rankName", rank.getRankName()!=null ? rank.getRankName():"");
				list.add(mData);
			});
			json.put("data", list);
			json.put("status", 1);
			json.put("msg", "Record Fetch Successfully");
		}else {
			json.put("status", 0);
			json.put("msg", "No Record Found");
		}
		return json.toString();
	}

	@Override
	public String getGenderList(JSONObject jsonObject) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		List<MasAdministrativeSex> administrativeSexList = od.getGenderList();
		if(CollectionUtils.isNotEmpty(administrativeSexList)) {
			administrativeSexList.forEach(adminsex -> {
				Map<String, Object> mData = new LinkedHashMap<String, Object>();
				mData.put("administrativeSexId", adminsex.getAdministrativeSexId()!=0 ? adminsex.getAdministrativeSexId():"");
				mData.put("administrativeSexName", adminsex.getAdministrativeSexName()!=null ? adminsex.getAdministrativeSexName():"");
				list.add(mData);
			});
			json.put("data", list);
			json.put("status", 1);
			json.put("msg", "Record Fetch Successfully");
		}else {
			json.put("status", 0);
			json.put("msg", "No Record Found");
		}
		return json.toString();
	}
	
	@Override
	public String rejectOpdWaitingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String opdVisit = null;
		try {
			if (!jsondata.isEmpty()) {
				
				opdVisit = od.updateVisitStatusReject(jsondata);
		// TODO Auto-generated method stub
				if (opdVisit != null && opdVisit.equalsIgnoreCase("statusUpdated")) {
					json.put("msg", "visitStatusUpdated");
					json.put("status", "1");
				} else if (opdVisit != null && opdVisit.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", opdVisit);
					json.put("status", "0");
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
	public String getPatientByServiceNo(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		 
		JSONObject json = new JSONObject();
		try {
		
			Patient patientObj=  od.getPatientByServiceNo(jsonObject);
			if(patientObj!=null) {
				
				//json.put("relationId",patientObj.getRelationId());
				json.put("patientId",patientObj.getPatientId());
				json.put("patientName",patientObj.getPatientName() !=null ? patientObj.getPatientName() :"");
				json.put("status", 1);
				json.put("msg", "Get Record Successfully..");
			}
			
			else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}
		
			}	
				
			catch(Exception e) {
				e.printStackTrace();
			}
			
			return json.toString();
		}
	
	@Override
	public String getAdmissionDischargeRegister(Map<String, String> jsondata, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> getAdmissionDischargeRegister = new ArrayList<>();
		JSONObject obj = new JSONObject();
		

		return obj.toString();
	}
	
	@Override
	public String saveObesityEntry(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String serviceNo = "";
		Long patientId = null;
		Long adminSexId = null;
		Long rankId = null;
		Long unitId = null;
		String patientDOB = "";

		if (jsondata.get("serviceNo").toString() != null
				&& !jsondata.get("serviceNo").toString().equalsIgnoreCase("")) {
			serviceNo = jsondata.get("serviceNo").toString().toUpperCase();
		}

		if (jsondata.get("patientId").toString() != null
				&& !jsondata.get("patientId").toString().equalsIgnoreCase("")) {
			patientId = Long.parseLong(jsondata.get("patientId").toString());
		}

		if (jsondata.get("rankId").toString() != null && !jsondata.get("rankId").toString().equalsIgnoreCase("")) {
			rankId = Long.parseLong(jsondata.get("rankId").toString());
		}

		if (jsondata.get("genderId").toString() != null && !jsondata.get("genderId").toString().equalsIgnoreCase("")) {
			adminSexId = Long.parseLong(jsondata.get("genderId").toString());
		}

		if (jsondata.get("unitId").toString() != null && !jsondata.get("unitId").toString().equalsIgnoreCase("")) {
			unitId = Long.parseLong(jsondata.get("unitId").toString());
		}

		if (jsondata.get("patientDOB").toString() != null
				&& !jsondata.get("patientDOB").toString().equalsIgnoreCase("")) {
			patientDOB = jsondata.get("patientDOB").toString();
		}

		Patient patient = null;
		String uhidNO = String.valueOf(jsondata.get("uhidNo"));
		if (!uhidNO.isEmpty()) {
			patientId = od.getPatientFromUhidNo(uhidNO);
			patient = od.getPatient(patientId);
			if (patient != null) {
				if (!adminSexId.equals(patient.getAdministrativeSexId())
						|| !patientDOB
								.equals(HMSUtil.convertDateToStringFormat(patient.getDateOfBirth(), "dd/MM/yyyy"))) {
					boolean updateResult = od.updatePatientDetails(serviceNo, patientId, adminSexId,
							patientDOB);
				}
			}
		} else {
			JSONObject jsonObj = new JSONObject(jsondata);
			patientId = od.createPatientForSHO(jsonObj);
		}

		jsondata.put("patient_id", String.valueOf(patientId));
		String result="s";
		//String result = od.saveObesityEntry(jsondata, request, response);
		return result;
	}


	@Override
	public String getPatientSympotons(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		 
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> ps = new ArrayList<HashMap<String, Object>>();
		try {
		
			List<PatientSymptom> patientSymptonsObj = od.getPatientSymptom(Long.parseLong(jsonObject.get("visitId").toString()));
			if (patientSymptonsObj != null && ! CollectionUtils.isEmpty(patientSymptonsObj) ) {
				
				for (PatientSymptom v : patientSymptonsObj) {
					HashMap<String, Object> pt = new HashMap<String, Object>();
					pt.put("visitId", v.getVisitId());
					pt.put("patientSymptomsId", v.getPatientSymptomsId());
					pt.put("symptomId", v.getSymptomId());
					pt.put("symptomName", v.getMasSymptom().getName());
					ps.add(pt);
				    
				}
				
			
			}
			
			else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}
			
			//json.put("relationId",patientObj.getRelationId());
			json.put("patientSymptoms",ps);
			json.put("patientName",ps.size());
			json.put("status", 1);
			json.put("msg", "Get Record Successfully..");
			
		
			}	
				
			catch(Exception e) {
				e.printStackTrace();
			}
			
			return json.toString();
		}


	@Override
	public String deletePatientSymptom(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String opdPatientSymptom = null;
		try {
			if (!jsondata.isEmpty()) {
				
				opdPatientSymptom = od.deletePatientSymptom(jsondata);
		// TODO Auto-generated method stub
				if (opdPatientSymptom != null && opdPatientSymptom.equalsIgnoreCase("statusUpdated")) {
					json.put("msg", "recordsDeleted");
					json.put("status", "1");
				} else if (opdPatientSymptom != null && opdPatientSymptom.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", opdPatientSymptom);
					json.put("status", "0");
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
	public String getPatientDianosisDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = dgOrderhdDao.getMasIcdByVisitPatAndOpdPD(Long.parseLong(jsondata.get("visitId").toString()),jsondata);
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					    List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						String diagnosisDtValue = null;
						String icdId = null;
						String icdName = null;
						String icdCode=null;
						String communicableFlag=null;
						String infectiousFlag=null;
					
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								if (row[0] != null) {
									diagnosisDtValue =row[0].toString();
								}
								if (row[1] != null) {
									icdId =row[1].toString();
								}
								
								if (row[2] != null) {
									icdName =row[2].toString();
								}
								if (row[3] != null && row[3] != "") {
									icdCode =row[3].toString();
								}
								if (row[4] != null && row[4] != "") {
									communicableFlag =row[4].toString();
								}
								if (row[5] != null && row[5] != "") {
									infectiousFlag =row[5].toString();
								}
								
								
							
								if(diagnosisDtValue!=null)
								{
								pt.put("diagnosisDtValue", diagnosisDtValue);
								}
								else
								{
									pt.put("diagnosisDtValue", "");	
								}
								if(icdId!=null)
								{
								pt.put("icdId", icdId);
								}
								else
								{
									pt.put("icdId", "");	
								}
								
								if(icdName!=null)
								{
								pt.put("icdName", icdName);
								}
								else
								{
									pt.put("icdName", "");	
								}
								if(icdCode!=null )
								{
								 	
								pt.put("icdCode", icdCode);
								}
								else
								{
									pt.put("icdCode", "");	
								}
								
								if(communicableFlag!=null )
								{
								 	
								pt.put("communicableFlag", communicableFlag);
								}
								else
								{
									pt.put("communicableFlag", "N");	
								}
								
								if(infectiousFlag!=null )
								{
								 	
								pt.put("infectiousFlag", infectiousFlag);
								}
								else
								{
									pt.put("infectiousFlag", "N");	
								}
								
								
								
								
					
						c.add(pt);
						json.put("data", c);
						json.put("count", count);
						json.put("msg", "data get sucessfully......");
						json.put("status", 1);
							}
					 }catch (Exception e) {
							e.printStackTrace();
							return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
					}
			
		}
		return json.toString();
	}
	

	public String updateOpdCurrentMedication(HashMap<String, Object> getjsondata, HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONArray newForm=new JSONArray(getjsondata.get("opdMainData").toString());
		JSONObject jsondata = (JSONObject) newForm.get(0);
		
		JSONObject jsonObj = new JSONObject();
		Long data=null;
		if (jsondata != null) {
			String userId=jsondata.get("userId").toString();
			userId=getReplaceString(userId);
			Long userIdValue=Long.parseLong(userId.toString());
			
			String patientPreciptionDtIddd=jsondata.get("precriptionDtValue").toString();
			patientPreciptionDtIddd=getReplaceString(patientPreciptionDtIddd);
			String [] patientPreciptionValuee=patientPreciptionDtIddd.split(",");
			List<Long>listPatientPreIdValue=new ArrayList<>();
			for(String ss:patientPreciptionValuee) {
				if(StringUtils.isNotEmpty(ss)) {
			
					listPatientPreIdValue.add(Long.parseLong(ss.toString().trim()));
			}
			}
			List<PatientPrescriptionDt> listPatientPrescriptionDt=null;
			if(CollectionUtils.isNotEmpty(listPatientPreIdValue))
          listPatientPrescriptionDt=dgOrderhdDao.getPatientPrecriptionDtIdByPatientId(listPatientPreIdValue);
        if(CollectionUtils.isNotEmpty(listPatientPrescriptionDt))
		for(PatientPrescriptionDt patientPrescriptionDt:listPatientPrescriptionDt) {
			
			patientPrescriptionDt.setItemStopDate(new Date());
			patientPrescriptionDt.setItemStopStatus(1l);
			patientPrescriptionDt.setItemStopBy(userIdValue);
			
          data = dgOrderhdDao.saveOrUpdatePatientPrecriptionDt(patientPrescriptionDt);
		}
       if (data != null) {
		jsonObj.put("data", data);
		jsonObj.put("status", 1);
		jsonObj.put("msg", "Successful update");
		
		} else {
		jsonObj.put("status", 0);
		jsonObj.put("msg", "Error occured");
		}
		}
		return jsonObj.toString();
	}
  
	public int getAvailableStock(HashMap<String, Object> jsondata) {
		
		JSONObject json = new JSONObject(jsondata);
		String dispId = String.valueOf(json.get("departmentId"));
		Map<String,Object> responseMap=new HashMap<String, Object>();
		String itemId = json.get("itemId").toString();
		String mmuId = json.get("mmuId1").toString();
		Map<String, Object> inputJson = new HashMap<String, Object>();
		inputJson.put("mmuId", mmuId);
		inputJson.put("department_id", "2051");
		inputJson.put("item_id", itemId);
		String stockData = dispensaryService.getBatchDetail(inputJson);
		JSONObject jobj = new JSONObject(stockData);
		JSONObject jobject =  (JSONObject) jobj.get("batchData");
		int storeStock = (int) jobject.get("store_stock");
		int availableStock = (int) jobject.get("disp_stock");
		responseMap.put("storeStock", storeStock);
		responseMap.put("dispStock", availableStock);
		responseMap.put("availableStock", availableStock);
		responseMap.put("status", 1);
		return availableStock;
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


	@Override
	public String getPatientHistoryRecord(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		
			JSONObject json = new JSONObject();
			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			if (!jsondata.isEmpty()) {
				//List<Object[]> getVisitRecord = null;  
				JSONObject nullbalankvalidation = null;
				
					//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
					Map<String, Object> getVisitRecord = od.getPatientRecord(Long.parseLong(jsondata.get("patientId").toString()),jsondata);
					//System.out.print("getVisitRecord"+getVisitRecord);
					if (getVisitRecord.isEmpty()) {
						json.put("count", 0);
						json.put("data", new JSONArray());
						
					} else{
						 List<Object[]> list = (List<Object[]>) getVisitRecord.get("list");
						 String count = String.valueOf(getVisitRecord.get("count"));
						 String PatientDataUploadId = null;
						 String PatientId = null;
						 String fileData = null;
						 String status = null;
						 String lastchgBy = null;
						 String lastchgDate = null;
						 String userName = null;
						 try {
						for (Object[] row : list) {
							
							HashMap<String, Object> pt = new HashMap<String, Object>();
							if (row[0] != null) {
								PatientDataUploadId =row[0].toString();
							}
							if (row[1] != null) {
								PatientId =row[1].toString();
							}
							if (row[2] != null) {
								fileData =row[2].toString();
							}
							if (row[3] != null) {
								status =row[3].toString();
							}
							if (row[4] != null) {
								lastchgBy =row[4].toString();
							}
							if (row[5] != null) {
								lastchgDate =row[5].toString();
							}
							if (row[6] != null) {
								userName =row[6].toString();
							}
							if(PatientDataUploadId!=null)
							{
							pt.put("PatientDataUploadId", PatientDataUploadId);
							}
							else
							{
								pt.put("PatientDataUploadId", "");
							}
							
							if(PatientId!=null)
							{
							pt.put("PatientId", PatientId);
							}
							else
							{
								pt.put("PatientId", "");
							}
							
							if(fileData!=null)
							{
								  String inPhotoBase64String = "";
						            String ServerImageUrl1 = environment.getProperty("server.imageUrl");
						           
										String imageFolderPathDirctory = ServerImageUrl1 ;
										String completePath = imageFolderPathDirctory +"/"+ fileData;
										//System.out.println("completePath"+completePath);
										File f = new File(completePath);
										if(f.exists()) {
											inPhotoBase64String = encodeFileToBase64Binary(f);
											//System.out.println("inPhotoBase64String:::"+inPhotoBase64String);
										}
										//System.out.println(inPhotoBase64String);
								
										
							pt.put("fileData", "data:image/jpg;base64,"+inPhotoBase64String);
							}
							else
							{
								pt.put("fileData", "");
							}
							
							if(status!=null)
							{
							pt.put("status", status);
							}
							else
							{
								pt.put("status", "");
							}
							
							if(lastchgBy!=null)
							{
							pt.put("lastchgBy", lastchgBy);
							}
							else
							{
								pt.put("lastchgBy", "");
							}
							
							if(lastchgDate!=null)
							{
							pt.put("lastchgDate", lastchgDate);
							}
							else
							{
								pt.put("lastchgDate", "");
							}
							
							if(lastchgDate!=null)
							{
							pt.put("userName", userName);
							}
							else
							{
								pt.put("userName", "");
							}
							
							 c.add(pt);
								json.put("data", c);
								json.put("count", count);
								json.put("msg", "data get sucessfully......");
								json.put("status", 1);
						}
					
						 }
						 catch (Exception e) {
							// TODO: handle exception
						}
						 }
					
					
					/*else {
						    List<Object[]> list = (List<Object[]>) getVisitRecord.get("list");
						    String count = String.valueOf(getVisitRecord.get("count"));
							String visistDate = null;
							String icdDiagnosis = null;
							String patientSymptoms = null;
							String mmuName=null;
							String visitId="";
							String departmentName="";
							String doctorName="";
							String orderDate = "";
							Long visitId = null;
							String otherInvestigation = "";
							Long departId = null;
							Long hospitalId = null;
							Long dgOrderDtId = null;
							try {
								for (Object[] row : list) {
								
								//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
								//	Object[] row = (Object[]) it.next();

									HashMap<String, Object> pt = new HashMap<String, Object>();

									if (row[0] != null) {
										Timestamp vd=(Timestamp) row[0];
										Calendar lCal = Calendar.getInstance();
									    lCal.setTime(vd);
						                int yr=lCal.get(Calendar.YEAR);
						                int mn=lCal.get(Calendar.MONTH) + 1;
						                int dt=lCal.get(Calendar.DATE);
						                
						               
						                LocalDate visitDate = LocalDate.of(yr,mn,dt) ; //Birth date
						                
						                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
						                String text = visitDate.format(formatters);
						                
						                visistDate =text;
										//pt.put("visitDate",visitDate);
										
														
									}
									if (row[1] != null) {
										icdDiagnosis =row[1].toString();
									}
									
									if (row[2] != null) {
										patientSymptoms =row[2].toString();
									}
									if (row[3] != null && row[3] != "") {
										visitId =row[3].toString();
									}
									if (row[4] != null) {
										departmentName =row[4].toString();
									}
									if (row[5] != null) {
										doctorName =row[5].toString();
									}
									if (row[6] != null) {
										mmuName =row[6].toString();
									}
									
									
									if(visistDate!=null)
									{
									pt.put("visistDate", visistDate);
									}
									else
									{
										pt.put("visistDate", "");
									}
									if(icdDiagnosis!=null)
									{
									pt.put("icdDiagnosis", icdDiagnosis);
									}
									else
									{
										pt.put("icdDiagnosis", "");	
									}
									if(patientSymptoms!=null)
									{
									pt.put("patientSymptoms", patientSymptoms);
									}
									else
									{
										pt.put("patientSymptoms", "");	
									}
									
									if(visitId!=null)
									{
									pt.put("visitId", visitId);
									}
									else
									{
										pt.put("visitId", "");	
									}
									if(departmentName!=null && departmentName!=null )
									{
									 	
									pt.put("departmentName", departmentName);
									}
									else
									{
										pt.put("departmentName", "");	
									}
									if(doctorName!=null)
									{
									pt.put("doctorName", doctorName);
									}
									else
									{
										pt.put("doctorName", "");	
									}
									if(mmuName!=null)
									{
									pt.put("mmuName", mmuName);
									}
									else
									{
										pt.put("mmuName", "");	
									}
									
									
						
							c.add(pt);
							json.put("data", c);
							json.put("count", count);
							json.put("msg", "data get sucessfully......");
							json.put("status", 1);
								}
						 }catch (Exception e) {
								e.printStackTrace();
								return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
						}
						}*/
				
			}
			return json.toString();
	}

	//////////////////////////////// OPD Previous Auditor Record
	//////////////////////////////// ///////////////////////////////////////////////////////
	@Override
	public String getOpdPreviousAuditorRemarks(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			// List<Object[]> getVisitRecord = null;
			JSONObject nullbalankvalidation = null;

			// getVisitRecord=
			// od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
			Map<String, Object> getVisitRecord = od.getOpdPreviousAuditorRemarks(Long.parseLong(jsondata.get("patientId").toString()), jsondata);
			if (getVisitRecord.isEmpty()) {
				json.put("count", 0);
				json.put("data", new JSONArray());
			} else {
				List<Object[]> list = (List<Object[]>) getVisitRecord.get("list");
				String count = String.valueOf(getVisitRecord.get("count"));
				String visistDate = null;
				String auditDate = null;
				String remarks = null;
				String auditBy = null;
				String exception = "";
			
				String doctorName = "";
				/*
				 * String orderDate = ""; Long visitId = null; String otherInvestigation = "";
				 * Long departId = null; Long hospitalId = null; Long dgOrderDtId = null;
				 */
				try {
					for (Object[] row : list) {

						// for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
						// Object[] row = (Object[]) it.next();

						HashMap<String, Object> pt = new HashMap<String, Object>();

						if (row[0] != null) {
							Timestamp vd = (Timestamp) row[0];
							Calendar lCal = Calendar.getInstance();
							lCal.setTime(vd);
							int yr = lCal.get(Calendar.YEAR);
							int mn = lCal.get(Calendar.MONTH) + 1;
							int dt = lCal.get(Calendar.DATE);

							LocalDate visitDate = LocalDate.of(yr, mn, dt); // Birth date

							DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
							String text = visitDate.format(formatters);

							visistDate = text;
							// pt.put("visitDate",visitDate);

						}
						if (row[1] != null) {
							//Timestamp vd1 = (Timestamp) row[1];
							//Calendar lCal1 = (Calendar) row[1];
							//lCal1.setTime(vd1);
							//int yr1 = lCal1.get(Calendar.YEAR);
							//int mn1 = lCal1.get(Calendar.MONTH) + 1;
							//int dt1 = lCal1.get(Calendar.DATE);

							 auditDate = HMSUtil.convertDateToStringFormat((Date) row[1], "dd/MM/yyyy");
							//DateTimeFormatter formatters = DateTimeFormatter.ofPattern(row[1],"dd/MM/uuuu");
							//String text1 = visitDate.format(formatters);

							//auditDate = text1;
						}

						if (row[2] != null) {
							remarks = row[2].toString();
						}
						if (row[3] != null && row[3] != "") {
							auditBy = row[3].toString();
						}
						if (row[4] != null) {
							exception = row[4].toString();
						}
						

						if (visistDate != null) {
							pt.put("visistDate", visistDate);
						} else {
							pt.put("visistDate", "");
						}
						if (auditDate != null) {
							pt.put("auditDate", auditDate);
						} else {
							pt.put("auditDate", "");
						}
						if (remarks != null) {
							pt.put("remarks", remarks);
						} else {
							pt.put("remarks", "");
						}

						if (auditBy != null) {
							pt.put("auditBy", auditBy);
						} else {
							pt.put("auditBy", "");
						}
						if (exception != null && exception != null) {

							pt.put("exception", exception);
						} else {
							pt.put("exception", "");
						}
						
						c.add(pt);
						json.put("data", c);
						json.put("count", count);
						json.put("msg", "data get sucessfully......");
						json.put("status", 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}
			}

		}
		return json.toString();
	}
	
}


