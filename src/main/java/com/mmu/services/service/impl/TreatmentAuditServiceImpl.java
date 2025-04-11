package com.mmu.services.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.dao.TreatmentAuditDao;
import com.mmu.services.entity.MasAudit;
import com.mmu.services.entity.Visit;
import com.mmu.services.service.DispensaryService;
import com.mmu.services.service.TreatmentAuditService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ValidateUtils;

@Repository
public class TreatmentAuditServiceImpl implements TreatmentAuditService {
	
	@Autowired
	TreatmentAuditDao treatmentAuditDao;
	
	@Autowired
	OpdMasterDao md;
	
	@Autowired 
	DispensaryService dispensaryService;

	@Override
	public String getAudittWatingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
	
		JSONObject json = new JSONObject();
		
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE ID as a  key or value or it is null\"}";
			} else {
				
				//Users checkEmp = od.checkUser(Long.parseLong(jsondata.get("employeeId").toString()));
				
				//if (checkEmp != null) {
					
					//List<Visit> getvisit = od.getVisit(jsondata);
					Map<String,Object> map = treatmentAuditDao.getAuditVisit(jsondata);
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
								if(v.getMmuId() != null && v.getMasMmu().getMmuId()!=null)
								{
									pt.put("mmuName",v.getMasMmu().getMmuName());
									pt.put("mmuId",v.getMasMmu().getMmuId());
								}
								else
								{
									pt.put("mmuName","");
									pt.put("mmuId","");
								}
								if(v.getMasCamp() != null && v.getMasCamp().getCityId()!=null)
								{
									pt.put("cityName",v.getMasCamp().getMasCity().getCityName());
									pt.put("cityId",v.getMasCamp().getMasCity().getCityId());
								}
								else
								{
									pt.put("cityName","");
									pt.put("cityId","");
								}
								
								if(v.getVisitDate()!=null)
								{
									String visitDate=null;
									Timestamp vd=v.getVisitDate();
									Calendar lCal = Calendar.getInstance();
								    lCal.setTime(vd);
					                int yr=lCal.get(Calendar.YEAR);
					                int mn=lCal.get(Calendar.MONTH) + 1;
					                int dt=lCal.get(Calendar.DATE);
					                
					               
					                LocalDate visitDatess = LocalDate.of(yr,mn,dt) ; //Birth date
					                
					                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
					                String text = visitDatess.format(formatters);
					                
					                visitDate =text;
					                pt.put("visitDate", visitDate);
								}
								else
								{
									 pt.put("visitDate", "");
								}
							if(v.getVisitId()!=null && v.getAuditExceptionRef()!=null && v.getAuditExceptionRef().getExceptionDate()!=null)
								{
									String auditDate=null;
									Date vd=v.getAuditExceptionRef().getExceptionDate();
									Calendar lCal = Calendar.getInstance();
								    lCal.setTime(vd);
					                int yr=lCal.get(Calendar.YEAR);
					                int mn=lCal.get(Calendar.MONTH) + 1;
					                int dt=lCal.get(Calendar.DATE);
					                
					               
					                LocalDate visitDatess = LocalDate.of(yr,mn,dt) ; //Birth date
					                
					                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
					                String text = visitDatess.format(formatters);
					                
					                auditDate =text;
					                pt.put("auditDate", auditDate);
								}
								else
								{
									 pt.put("auditDate", "");
								}
								if(v.getVisitId()!=null && v.getAuditExceptionRef()!=null && v.getAuditExceptionRef().getUser()!=null && v.getAuditExceptionRef().getUser().getUserName()!=null)
								{
									
					                pt.put("auditorName", v.getAuditExceptionRef().getUser().getUserName());
								}
								else
								{
									 pt.put("auditorName", "");
								}
								
								
								
								c.add(pt);
							//}
						}
						
						
						if(c != null && c.size()>0){
							//s("Waitin List:"+c);
							
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
	public String getPatientDianosisDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = treatmentAuditDao.getMasIcdByVisitPatAndOpdPD(Long.parseLong(jsondata.get("visitId").toString()),jsondata);
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					    List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
						String icdId = null;
						String icdName = null;
						String icdCode=null;
						String communicableFlag=null;
						String infectiousFlag=null;
						String auditDischargeIcdCodeId=null;
						String auditIcdName=null;
						String auditIcdCode=null;
						String auditStatus=null;
						String auditRemarks=null;
						String auditMainDischargeId=null;
					
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0] != null) {
									icdId =row[0].toString();
								}
								
								if (row[1] != null) {
									icdName =row[1].toString();
								}
								if (row[2] != null && row[2] != "") {
									icdCode =row[2].toString();
								}
								
								if (row[3] != null && row[3] != "") {
									auditDischargeIcdCodeId =row[3].toString();
								}
								if (row[4] != null && row[4] != "") {
									auditIcdName =row[4].toString();
								}
								if (row[5] != null && row[5] != "") {
									auditIcdCode =row[5].toString();
								}
								if (row[6] != null && row[6] != "") {
									auditStatus =row[6].toString();
								}
								if (row[7] != null && row[7] != "") {
									auditRemarks =row[7].toString();
								}
								if (row[8] != null && row[8] != "") {
									communicableFlag =row[8].toString();
								}
								if (row[9] != null && row[9] != "") {
									infectiousFlag =row[9].toString();
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
								if(auditDischargeIcdCodeId!=null )
								{
								 	
								pt.put("auditDischargeIcdCodeId", auditDischargeIcdCodeId);
								}
								else
								{
									pt.put("auditDischargeIcdCodeId", "");	
								}
								
								if(auditIcdName!=null )
								{
								 	
								pt.put("auditIcdName", auditIcdName);
								}
								else
								{
									pt.put("auditIcdName", "");	
								}
								if(auditIcdCode!=null )
								{
								 	
								pt.put("auditIcdCode", auditIcdCode);
								}
								else
								{
									pt.put("auditIcdCode", "");	
								}
								if(auditStatus!=null )
								{
								 	
								pt.put("auditStatus", auditStatus);
								}
								else
								{
									pt.put("auditStatus", "");	
								}
								if(auditRemarks!=null )
								{
								 	
								pt.put("auditRemarks", auditRemarks);
								}
								else
								{
									pt.put("auditRemarks", "");	
								}
								if(auditMainDischargeId!=null )
								{
								 	
								pt.put("auditMainDischargeId", auditMainDischargeId);
								}
								else
								{
									pt.put("auditMainDischargeId", "");	
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
	public String getInvestigationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = treatmentAuditDao.getDgMasInvestigations(Long.parseLong(jsondata.get("visitId").toString()));
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					    List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
						String investgationId = null;
						String investgationName = null;
						String auditInvestgationId=null;
						String auditInvestgationName=null;
						String auditStatus=null;
						String auditRemarks=null;
						String auditMainDischargeId=null;
					
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0] != null) {
									investgationId =row[0].toString();
								}
								
								if (row[1] != null) {
									investgationName =row[1].toString();
								}
								if (row[2] != null && row[2] != "") {
									auditInvestgationId =row[2].toString();
								}
								
								if (row[3] != null && row[3] != "") {
									auditInvestgationName =row[3].toString();
								}
								if (row[4] != null && row[4] != "") {
									auditStatus =row[4].toString();
								}
								if (row[5] != null && row[5] != "") {
									auditRemarks =row[5].toString();
								}
								
								
							
								if(investgationId!=null)
								{
								pt.put("investgationId", investgationId);
								}
								else
								{
									pt.put("investgationId", "");	
								}
								
								if(investgationName!=null)
								{
								pt.put("investgationName", investgationName);
								}
								else
								{
									pt.put("investgationName", "");	
								}
								if(auditInvestgationId!=null )
								{
								 	
								pt.put("auditInvestgationId", auditInvestgationId);
								}
								else
								{
									pt.put("auditInvestgationId", "");	
								}
								if(auditInvestgationName!=null )
								{
								 	
								pt.put("auditInvestgationName", auditInvestgationName);
								}
								else
								{
									pt.put("auditInvestgationName", "");	
								}
								
								
								if(auditStatus!=null )
								{
								 	
								pt.put("auditStatus", auditStatus);
								}
								else
								{
									pt.put("auditStatus", "");	
								}
								if(auditRemarks!=null )
								{
								 	
								pt.put("auditRemarks", auditRemarks);
								}
								else
								{
									pt.put("auditRemarks", "");	
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
	public String getTreatmentPatientDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = treatmentAuditDao.getTreatementDetail(Long.parseLong(jsondata.get("visitId").toString()));
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					    List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
					    String treatmentId=null;
					    String treatmentName=null;
					    String frequencyId = null;
						String frequencyName = "";
						String frequencyCode = "";
						String dosage = "";
						String noOfDays = null;
						String total = null;
						String instruction = "";
						String PVMSno="";
						String dispUnitId=null;
						String status="";
						String auditTreatmentId=null;
						String auditTreatmentName=null;
						String auditStatus=null;
						String auditRemarks=null;
						String auditMainDischargeId=null;
						String storeUnitId=null;
					
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0] != null) {
									treatmentId =row[0].toString();
								}
								
								if (row[1] != null) {
									treatmentName =row[1].toString();
								}
								if (row[2] != null && row[2] != "") {
									auditTreatmentId =row[2].toString();
								}
								
								if (row[3] != null && row[3] != "") {
									auditTreatmentName =row[3].toString();
								}
								if (row[4] != null && row[4] != "") {
									auditStatus =row[4].toString();
								}
								if (row[5] != null && row[5] != "") {
									auditRemarks =row[5].toString();
								}
								if (row[6] != null && row[6] != "") {
									frequencyId =row[6].toString();
								}
								if (row[7] != null && row[7] != "") {
									frequencyName =row[7].toString();
								}
								if (row[8] != null && row[8] != "") {
									noOfDays =row[8].toString();
								}
								if (row[9] != null && row[9] != "") {
									dosage =row[9].toString();
								}
								if (row[10] != null && row[10] != "") {
									total =row[10].toString();
								}
								if (row[11] != null && row[11] != "") {
									 instruction=row[11].toString();
								}
								if (row[12] != null && row[12] != "") {
									storeUnitId =row[12].toString();
								}
								if (row[13] != null && row[13] != "") {
									PVMSno =row[12].toString();
								}
								
								
								
							
								if(treatmentId!=null)
								{
								pt.put("treatmentId", treatmentId);
								}
								else
								{
									pt.put("treatmentId", "");	
								}
								
								if(treatmentName!=null)
								{
								pt.put("treatmentName", treatmentName);
								}
								else
								{
									pt.put("treatmentName", "");	
								}
								if(auditTreatmentId!=null )
								{
								 	
								pt.put("auditTreatmentId", auditTreatmentId);
								}
								else
								{
									pt.put("auditTreatmentId", "");	
								}
								if(auditTreatmentName!=null )
								{
								 	
								pt.put("auditTreatmentName", auditTreatmentName);
								}
								else
								{
									pt.put("auditTreatmentName", "");	
								}
								
								
								if(auditStatus!=null )
								{
								 	
								pt.put("auditStatus", auditStatus);
								}
								else
								{
									pt.put("auditStatus", "");	
								}
								if(auditRemarks!=null )
								{
								 	
								pt.put("auditRemarks", auditRemarks);
								}
								else
								{
									pt.put("auditRemarks", "");	
								}
								
								if(frequencyId!=null )
								{
								 	
								pt.put("frequencyId", frequencyId);
								}
								else
								{
									pt.put("frequencyId", "");	
								}
								if(frequencyName!=null )
								{
								 	
								pt.put("frequencyName", frequencyName);
								}
								else
								{
									pt.put("frequencyName", "");	
								}
								if(noOfDays!=null )
								{
								 	
								pt.put("noOfDays", noOfDays);
								}
								else
								{
									pt.put("noOfDays", "");	
								}
								if(dosage!=null )
								{
								 	
								pt.put("dosage", dosage);
								}
								else
								{
									pt.put("dosage", "");	
								}
								if(total!=null )
								{
								 	
								pt.put("total", total);
								}
								else
								{
									pt.put("total", "");	
								}
								if(instruction!=null )
								{
								 	
								pt.put("instruction", instruction);
								}
								else
								{
									pt.put("instruction", "");	
								}
								if(storeUnitId!=null )
								{
								 	
								pt.put("storeUnitId", storeUnitId);
								}
								else
								{
									pt.put("storeUnitId", "");	
								}
								if(PVMSno!=null )
								{
								 	
								pt.put("PVMSno", PVMSno);
								}
								else
								{
									pt.put("PVMSno", "");	
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
	public String saveTreatmentAuditDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

	    String opd = null;
		JSONObject json = new JSONObject();
	
	try {
		if (!jsondata.isEmpty())
		{
			JSONObject nullbalankvalidation = null;
			nullbalankvalidation = ValidateUtils.addVitalPreConsulataionDetailsvalidation(jsondata);
			if (nullbalankvalidation.optString("status").equals("0"))
			{
				return nullbalankvalidation.toString();
			}
			else 
			{
				 opd = treatmentAuditDao.saveTreatmentAuditDetails(jsondata);
				
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
	public String getRecommendedDiagnosisAllDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = treatmentAuditDao.getRecommendedDiagnosisAllDetail(jsondata.get("patientSympotnsId").toString());
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					    List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
					    String recomAuditDiagnosis=null;
					    String icdId=null;
					    String communicableFlag=null;
					    String infectiousFlag=null;
									
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0]!= null) {
									recomAuditDiagnosis =row[0].toString();
								}
								if (row[1]!= null) {
									icdId =row[1].toString();
								}
								if (row[2]!= null) {
									communicableFlag =row[2].toString();
								}
								if (row[3]!= null) {
									infectiousFlag =row[3].toString();
								}
															
								
							
								if(recomAuditDiagnosis!=null)
								{
								pt.put("recomAuditDiagnosis", recomAuditDiagnosis);
								}
								else
								{
									pt.put("recomAuditDiagnosis", "");	
								}
								if(icdId!=null)
								{
								pt.put("icdId", icdId);
								}
								else
								{
									pt.put("icdId", "");	
								}
								if(communicableFlag!=null)
								{
								pt.put("communicableFlag", communicableFlag);
								}
								else
								{
									pt.put("communicableFlag", "");	
								}
								if(infectiousFlag!=null)
								{
								pt.put("infectiousFlag", infectiousFlag);
								}
								else
								{
									pt.put("infectiousFlag", "");	
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
	public String getRecommendedInvestgationAllDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = treatmentAuditDao.getRecommendedInvestgationAllDetail(jsondata.get("patientSympotnsId").toString());
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					 List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
					    String recomAuditInvstgationName=null;
					    String recomAuditInvstgationId=null;
									
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0]!= null) {
									recomAuditInvstgationName =row[0].toString();
								}
								if (row[1]!= null) {
									recomAuditInvstgationId =row[1].toString();
								}
															
								
							
								if(recomAuditInvstgationName!=null)
								{
								pt.put("recomAuditInvstgationName", recomAuditInvstgationName);
								}
								else
								{
									pt.put("recomAuditInvstgationName", "");	
								}
								if(recomAuditInvstgationId!=null)
								{
								pt.put("recomAuditInvstgationId", recomAuditInvstgationId);
								}
								else
								{
									pt.put("recomAuditInvstgationId", "");	
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
	public String getRecommendedTreatmentAllDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = treatmentAuditDao.getRecommendedTreatmentAllDetail(jsondata.get("patientSympotnsId").toString());
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					 List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
					    String recomAuditTreatmentName=null;
					    String recomAuditTreatmentId=null;
					    String dispUnitId=null;
					    String itemClassId=null;

						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0]!= null) {
									recomAuditTreatmentName =row[0].toString();
								}
								if (row[1]!= null) {
									recomAuditTreatmentId =row[1].toString();
									jsondata.put("itemId", recomAuditTreatmentId);
									//int avStock=getAvailableStock(jsondata);
									pt.put("availableStock", 0);
								}
								if (row[2]!= null) {
									dispUnitId =row[2].toString();
									jsondata.put("dispUnitId", dispUnitId);
								}
								if (row[3]!= null) {
									itemClassId =row[3].toString();
									jsondata.put("itemClassId", itemClassId);
								}

								
							
								if(recomAuditTreatmentName!=null)
								{
								pt.put("recomAuditTreatmentName", recomAuditTreatmentName);
								}
								else
								{
									pt.put("recomAuditTreatmentName", "");	
								}
								if(recomAuditTreatmentId!=null)
								{
								pt.put("recomAuditTreatmentId", recomAuditTreatmentId);
								}
								else
								{
									pt.put("recomAuditTreatmentId", "");	
								}
								if(dispUnitId!=null)
								{
								pt.put("dispUnitId", dispUnitId);
								}
								else
								{
									pt.put("dispUnitId", "");	
								}
								if(itemClassId!=null)
								{
								pt.put("itemClassId", itemClassId);
								}
								else
								{
									pt.put("itemClassId", "");
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
	public String getAIDiagnosisDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
			List<Long> getDiagnosisData = treatmentAuditDao.getAIDiagnosisDetail(jsondata.get("patientSympotnsId").toString(),Long.parseLong(jsondata.get("diagnosisId").toString()));
			if (getDiagnosisData != null && ! CollectionUtils.isEmpty(getDiagnosisData) ) {
				
				try {
					HashMap<String, Object> iw = new HashMap<String, Object>();
					iw.put("diagnosisCount", getDiagnosisData.get(0));
					c.add(iw);
					json.put("data", c);
					json.put("msg", "data get sucessfully......");
					json.put("status", 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}
		return json.toString();
	}

	@Override
	public String getAIInvestgationDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
			List<Long> getInvestgationCount = treatmentAuditDao.getAIInvestgationDetail(jsondata.get("patientSympotnsId").toString(),Long.parseLong(jsondata.get("investgationId").toString()));
			if (getInvestgationCount != null && ! CollectionUtils.isEmpty(getInvestgationCount) ) {
				
				try {
					HashMap<String, Object> iw = new HashMap<String, Object>();
					iw.put("investgationCount", getInvestgationCount.get(0));
					c.add(iw);
					json.put("data", c);
					json.put("msg", "data get sucessfully......");
					json.put("status", 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}
		return json.toString();
	}

	@Override
	public String getAITreatmentDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
			List<Long> getTreatmentData = treatmentAuditDao.getAITreatmentDetail(jsondata.get("patientSympotnsId").toString(),Long.parseLong(jsondata.get("treatmentId").toString()));
			if (getTreatmentData != null && ! CollectionUtils.isEmpty(getTreatmentData) ) {
				
				try {
					HashMap<String, Object> iw = new HashMap<String, Object>();
					iw.put("treatmentCount", getTreatmentData.get(0));
					c.add(iw);
					json.put("data", c);
					json.put("msg", "data get sucessfully......");
					json.put("status", 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}
		return json.toString();
	}
	
public int getAvailableStock(HashMap<String, String> jsondata) {
		
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

@Override
public String getAllSymptomsForOpd(HashMap<String, String> jsondata, HttpServletRequest request,
		HttpServletResponse response) {
	JSONObject json = new JSONObject();
	List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
	if (!jsondata.isEmpty()) {
		//List<Object[]> getVisitRecord = null;  
		JSONObject nullbalankvalidation = null;
		
			//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
			Map<String, Object> getDiagnosisData = treatmentAuditDao.getAllSymptomsForOpd(jsondata.get("name").toString());
			if (getDiagnosisData.isEmpty()) {
				json.put("count", 0);
				json.put("data", new JSONArray());
			} else {
				 List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
				    String count = String.valueOf(getDiagnosisData.get("count"));
					
				    String id=null;
				    String name=null;
				    String code=null;
								
					try {
						for (Object[] row : list) {
						
						//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
						//	Object[] row = (Object[]) it.next();

							HashMap<String, Object> pt = new HashMap<String, Object>();

							
							if (row[0]!= null) {
								id =row[0].toString();
							}
							if (row[1]!= null) {
								code =row[1].toString();
								
							}
							if (row[2]!= null) {
								name =row[2].toString();
								
							}
														
							
						
							if(id!=null)
							{
							pt.put("id", id);
							}
							else
							{
								pt.put("id", "");	
							}
							if(name!=null)
							{
							pt.put("name", name);
							}
							else
							{
								pt.put("name", "");	
							}
							if(code!=null)
							{
							pt.put("code", code);
							}
							else
							{
								pt.put("code", "");	
							}
						
							
				
					c.add(pt);
					json.put("list", c);
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
public String getAllIcdForOpd(HashMap<String, String> jsondata, HttpServletRequest request,
		HttpServletResponse response) {
	JSONObject json = new JSONObject();
	List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
	if (!jsondata.isEmpty()) {
		//List<Object[]> getVisitRecord = null;  
		JSONObject nullbalankvalidation = null;
		
			//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
			Map<String, Object> getDiagnosisData = treatmentAuditDao.getAllIcdForOpd(jsondata.get("name").toString());
			if (getDiagnosisData.isEmpty()) {
				json.put("count", 0);
				json.put("data", new JSONArray());
			} else {
				 List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
				    String count = String.valueOf(getDiagnosisData.get("count"));
					
				    String id=null;
				    String name=null;
				    String code=null;
								
					try {
						for (Object[] row : list) {
						
						//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
						//	Object[] row = (Object[]) it.next();

							HashMap<String, Object> pt = new HashMap<String, Object>();

							
							if (row[0]!= null) {
								id =row[0].toString();
							}
							if (row[1]!= null) {
								code =row[1].toString();
								
							}
							if (row[2]!= null) {
								name =row[2].toString();
								
							}
														
							
						
							if(id!=null)
							{
							pt.put("icdId", id);
							}
							else
							{
								pt.put("icdId", "");	
							}
							if(name!=null)
							{
							pt.put("icdName", name);
							}
							else
							{
								pt.put("icdName", "");	
							}
							if(code!=null)
							{
							pt.put("icdCode", code);
							}
							else
							{
								pt.put("icdCode", "");	
							}
						
							
				
					c.add(pt);
					json.put("ICDList", c);
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
public String getExpiryMedicine(HashMap<String, Object> jsondata, HttpServletRequest request,
		HttpServletResponse response) {
	JSONObject jsonResponse = new JSONObject();
	Map<String,Object> map = (Map<String,Object>)treatmentAuditDao.getExpiryMedicine(jsondata, request, response);
	JSONObject result = (JSONObject)map.get("asp_medicine");
	jsonResponse.put("asp_medicine", result);
	return jsonResponse.toString(); 
}

}
