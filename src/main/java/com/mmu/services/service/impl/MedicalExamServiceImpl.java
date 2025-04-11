package com.mmu.services.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.CommonDao;
import com.mmu.services.dao.DgNormalValueDao;
import com.mmu.services.dao.DgOrderdtDao;
import com.mmu.services.dao.DgOrderhdDao;
import com.mmu.services.dao.MasterDao;
import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.OpdDao;
import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.dao.OpdPatientDetailDao;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgNormalValue;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DiverOrderDet;
import com.mmu.services.entity.MasDesignationMapping;
import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasServiceType;
import com.mmu.services.entity.MasSpeciality;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDiseaseInfo;
import com.mmu.services.entity.PatientImmunizationHistory;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.PatientPastMedHistory;
import com.mmu.services.entity.PatientServicesDetail;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.MedicalExamService;
import com.mmu.services.service.impl.MedicalExamServiceImpl;
import com.mmu.services.service.impl.OpdServiceImpl;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.JavaUtils;
import com.mmu.services.utils.ProjectUtils;
import com.mmu.services.utils.UtilityServices;

@Repository
public class MedicalExamServiceImpl implements MedicalExamService {
	@Autowired
	MedicalExamDAO medicalExamDAO;

	@Autowired
	DgOrderhdDao dgOrderhdDao;
	@Autowired
	DgOrderdtDao dgOrderdtDao; 
	@Autowired
	OpdDao opdDao;
	 
	@Autowired
	MasterDao masterDao;
	@Autowired
	OpdMasterDao md;

	@Autowired
	SystemAdminDao systemAdminDao;
	
	//@Autowired
	//VisitDao visitDao;
	 
	@Autowired
	OpdPatientDetailDao opdPatientDetailDao;
	@Autowired
	CommonDao cd;
	@Autowired
	DgNormalValueDao dgNormalValueDao;
	
	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	/**
	 * 
	 * @Description:This method getMEWaitingList() is used for get Patient
	 *                   information
	 * @param: jsondata
	 * @param: request
	 * @param: response
	 */
/*	@Override
	public String getMEWaitingList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<Visit> listVisit = null;
		Map<String, Object> map = null;
		if (!jsondata.isEmpty()) {
			map = medicalExamDAO.getValidateMedicalExamList(jsondata);
			int count =0;
			if(MapUtils.isNotEmpty(map)) {
				if(map.get("count")!=null)
			  count = (int) map.get("count");
				if(map.get("listVisit")!=null)
					listVisit = (List<Visit>) map.get("listVisit");
			}
			if (CollectionUtils.isNotEmpty(listVisit)) {
				for (Visit visit : listVisit) {
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("patientName", visit.getPatient().getPatientName());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}

					jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
						jsonMap.put("rankId", visit.getPatient().getMasRank().getRankId());
					} else {
						jsonMap.put("rankName", "");
						jsonMap.put("rankId", "");
					}
					jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());
					jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					String visitstatus = "";

					if (jsondata.get("flagForList").toString().equalsIgnoreCase("a")) {
						if (visit.getExamStatus().equalsIgnoreCase("I")) {
							visitstatus = "Initiated";
						}
						if (visit.getExamStatus().equalsIgnoreCase("s")) {
							visitstatus = "Saved";
						}
					} else {
						MasMedicalExamReport masMedicalExamReport = null;
						masMedicalExamReport = visit.getMasMedicalExamReport();
						if (masMedicalExamReport == null) {
							visitstatus = "New";
						}

						if (masMedicalExamReport != null && visit.getMasMedicalExamReport().getStatus()!=null) {

							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("s")) {
								visitstatus = "Saved";
							}

							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("af")
									|| visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("f")) {
								visitstatus = "Forwarded";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rj")) {
								visitstatus = "Rejected";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("pe")) {
								visitstatus = "Pending";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("ac")) {
								visitstatus = "Approved";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rf")) {
								visitstatus = "Referred";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("po")) {
								visitstatus = "New";
							}
						}
						else {
							visitstatus = "New";
						}
					}
					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());

					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());

					if (visit.getOpdPatientDetails() != null
							&& visit.getOpdPatientDetails().getOpdPatientDetailsId() != null) {
						jsonMap.put("opdPatientDetailId", visit.getOpdPatientDetails().getOpdPatientDetailsId());
					} else {
						jsonMap.put("opdPatientDetailId", "");
					}

					jsonMap.put("tokenNo", visit.getTokenNo());
					
					String meAge="";
					if(visit!=null && StringUtils.isNotEmpty(visit.getMeAge())) {
						meAge=visit.getMeAge();
					}
					else {
						meAge="";
					}
					
					jsonMap.put("meAge", meAge);
					
					
					if(visit.getPatient() != null && visit.getPatient().getMasrelation() != null)
					{
						jsonMap.put("relation", visit.getPatient().getMasrelation().getRelationName());
					}
					else
					{
						jsonMap.put("relation", "");
					}
					String meAgeNew="";
					if(visit!=null  && visit.getMeAge()!=null) {
						//Date s = visit.getPatient().getDateOfBirth();
						
						meAgeNew=getAgeAtTimeOfMe(visit.getMeAge());//HMSUtil.calculateAge(s);
					}
					else {
						meAgeNew="";
					}
					
					jsonMap.put("meAgeNew", meAgeNew);
					
					responseList.add(jsonMap);
				}
				if (responseList != null && responseList.size() > 0) {
					json.put("data", responseList);
					json.put("status", 1);
					json.put("count", count);

				} else {
					json.put("data", responseList);
					json.put("msg", "Data not found");
					json.put("status", 0);
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
		return json.toString();
	}

	*//**
	 * @Description: This method getPatientDetailToValidate() is used for get list
	 *               of patient Validate or not.
	 * @param: jsondata
	 * @param: request
	 * @param: response
	 * 
	 *//*

	@Override
	public String getPatientDetailToValidate(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		List<Object> responseListForNew = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		//List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		//List<HashMap<String, Object>> c1 = new ArrayList<HashMap<String, Object>>();
		List<DgMasInvestigation> listDgMasInvestigation = null;
		List<Object[]>listObject=null;
		try {
			Map<String, Object> mapObject = medicalExamDAO.getValidateMedicalExamDetails(jsondata);

			List<Visit> listVisit = (List<Visit>) mapObject.get("listVisit");
			if (jsondata.get("flagForForm") != null && jsondata.get("flagForForm").toString().equalsIgnoreCase("f1") ) {
				listDgMasInvestigation = (List<DgMasInvestigation>) mapObject.get("listDgMasInvestigation");
			
				listObject=(List<Object[]>) mapObject.get("listOfInvestigationList");
				
				 
			}
			List<OpdPatientDetail> getvitalDetails = opdDao
					.getVitalRecord(Long.parseLong(jsondata.get("visitId").toString()));

			if (CollectionUtils.isNotEmpty(listVisit)) {
				for (Visit visit : listVisit) {
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("patientName", visit.getPatient().getPatientName());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}

					//jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
					
					if (visit.getPatient() != null && visit.getPatient().getMasAdministrativeSex() != null) {
						jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						jsonMap.put("genderId", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
					}
					else {
						jsonMap.put("gender","");
						jsonMap.put("genderId","");
					}
					
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
						jsonMap.put("rankId", visit.getPatient().getMasRank().getRankId());
					} else {
						jsonMap.put("rankName", "");
						jsonMap.put("rankId", "");
					}
					jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());
					jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					String visitstatus = "";
					if (StringUtils.isNotEmpty(visit.getExamStatus())) {
						if (visit.getExamStatus().equalsIgnoreCase("I")) {
							visitstatus = "Initiated";
						}
						if (visit.getExamStatus().equalsIgnoreCase("S")) {
							visitstatus = "Saved";
						}
						if (visit.getExamStatus().equalsIgnoreCase("V")
								|| visit.getExamStatus().equalsIgnoreCase("T")) {
							visitstatus = "Validated";
						}
						if (visit.getExamStatus().equalsIgnoreCase("F")) {
							visitstatus = "Forwarded";
						}
					}

					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());

					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());
					jsonMap.put("examTypeId", visit.getExamId());
					jsonMap.put("appointmentId", visit.getAppointmentTypeId());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						String dateOfBirth = HMSUtil.convertDateToStringFormat(visit.getPatient().getDateOfBirth(),
								"dd/MM/yyyy");

						jsonMap.put("dateOfBirth", dateOfBirth);
					}

					if (visit.getPatient() != null && visit.getPatient().getServiceJoinDate() != null) {

						Date s1 = visit.getPatient().getServiceJoinDate();
						Period serviceDate = ProjectUtils.getDOB(s1);
						jsonMap.put("totalService", serviceDate.getYears());
					}
					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unit", visit.getPatient().getMasUnit().getUnitName());
					}
					if (visit.getPatient() != null && visit.getPatient().getMedicalCategoryId() != null) {
						jsonMap.put("medicalCategory",
								visit.getPatient().getMasMedicalCategory().getMedicalCategoryName());
						String medicalCategoryDate = "";
						if (visit.getPatient().getMasMedicalCategory().getLastChgDate() != null) {
							medicalCategoryDate = HMSUtil.convertDateToStringFormat(
									visit.getPatient().getMasMedicalCategory().getLastChgDate(), "dd/MM/yyyy");
							jsonMap.put("medicalCategoryDate", medicalCategoryDate);
						} else {
							jsonMap.put("medicalCategoryDate", "");
						}

					} else {
						jsonMap.put("medicalCategory", "");
					}
					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null) {
						jsonMap.put("tradeBranch", visit.getPatient().getMasTrade().getTradeName());
					} else {
						jsonMap.put("tradeBranch", "");
					}
					
					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null) {
						jsonMap.put("branchOrTradeIdOn", visit.getPatient().getMasTrade().getTradeId());
					} else {
						jsonMap.put("branchOrTradeIdOn", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unitIdOn", visit.getPatient().getMasUnit().getUnitId());
					} else {
						jsonMap.put("unitIdOn", "");
					}
					
					String dateOfExam ="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMediceExamDate()!=null) {
						  dateOfExam = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getMediceExamDate(), "dd/MM/yyyy");
					}
					else {
					  dateOfExam = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
					
					}
					jsonMap.put("dateOfExam", dateOfExam);
					
					//String dateOfExam = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
					//jsonMap.put("dateOfExam", dateOfExam);

					if (visit.getOpdPatientDetails() != null
							&& visit.getOpdPatientDetails().getOpdPatientDetailsId() != null) {
						jsonMap.put("opdPatientDetailId", visit.getOpdPatientDetails().getOpdPatientDetailsId());
					} else {
						jsonMap.put("opdPatientDetailId", "");
					}
				
					if (visit.getMasMedicalExamReport() != null) {
					if(visit.getMasMedicalExamReport().getHeight()!=null) {
						jsonMap.put("pcHeight", visit.getMasMedicalExamReport().getHeight());
					}
					else {
						jsonMap.put("pcHeight", "");
					}
					if(visit.getMasMedicalExamReport().getWeight()!=null) {
						jsonMap.put("pcWeight", visit.getMasMedicalExamReport().getWeight());	
					}
					else {
						jsonMap.put("pcWeight","");
					}
					if(visit.getMasMedicalExamReport().getIdealweight()!=null) {
						jsonMap.put("pcIdealWeight", visit.getMasMedicalExamReport().getIdealweight());	
					}
					else {
						jsonMap.put("pcIdealWeight", "");
					}
					
					
					if(visit.getMasMedicalExamReport().getOverweight()!=null) {
						jsonMap.put("pcOverWeight", visit.getMasMedicalExamReport().getOverweight());	
					}
					else {
						jsonMap.put("pcOverWeight", "");
					}
					
					if(visit.getMasMedicalExamReport().getWaist()!=null) {
						jsonMap.put("waist", visit.getMasMedicalExamReport().getWaist());	
					}
					else {
						jsonMap.put("waist", "");
					}
					
					if(visit.getMasMedicalExamReport().getChestfullexpansion()!=null) {
						jsonMap.put("pcChestFullExpansion", visit.getMasMedicalExamReport().getChestfullexpansion());	
					}
					else {
						jsonMap.put("pcChestFullExpansion", "");
					}

					if(visit.getMasMedicalExamReport().getRangeofexpansion()!=null) {
						jsonMap.put("pcRangeOfExpansion", visit.getMasMedicalExamReport().getRangeofexpansion());	
					}
					else {
						jsonMap.put("pcRangeOfExpansion", "");
					}
					}
					
					String meAgeNew="";
					//if(visit!=null  && visit.getPatient()!=null&& visit.getPatient().getDateOfBirth()!=null) {
					if(visit!=null  && visit.getMeAge()!=null) {		
					//Date s = visit.getPatient().getDateOfBirth();
						//meAgeNew=HMSUtil.calculateAge(s);
						meAgeNew=getAgeAtTimeOfMe(visit.getMeAge());
					}
					else {
						meAgeNew="";
					}
					jsonMap.put("meAgeNew", meAgeNew);
					
					
					
					
					String dateOfPatient = "";
					if (visit.getMasMedicalExamReport()!=null &&  visit.getMasMedicalExamReport().getDateofcommun() != null) {
						dateOfPatient = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getDateofcommun(),
								"dd/MM/yyyy");
					}

					jsonMap.put("dateOfPatient", dateOfPatient);
					if(visit.getPatient().getDiverFlag()!=null){
					jsonMap.put("divenFlag", visit.getPatient().getDiverFlag());
					}
					else {
						jsonMap.put("divenFlag", "");
					}
					responseList.add(jsonMap);
				
				}
				
				 Long investigationId=null;
				 String investigationName="";
				 Long dgOrderdt=null;
				 Long dgOrderHdId=null;

				 String investigationType="";
				 String investigationRemarks="";
				 String otherInvestigation="";
				 String labMark="";
				 if (listObject != null) {
				  
				   for (Iterator<?> it = listObject.iterator(); it.hasNext();) { 
					   Object[] row = (Object[]) it.next();
					   
					   HashMap<String, Object> pt = new HashMap<String, Object>();

						if (row[0] != null) {
							investigationId = Long.parseLong(row[0].toString());
						}
						if (row[1] != null) {
							investigationName = row[1].toString();
						}
						
						if (row[2] != null) {
							investigationType = row[2].toString();
						}
						if (row[3] != null) {
							dgOrderdt = Long.parseLong(row[3].toString());
						}
					   
						if (row[4] != null) {
							dgOrderHdId = Long.parseLong(row[4].toString());
						}
						if (row[5] != null) {
							investigationRemarks = row[5].toString();
						}
						else {
							investigationRemarks="";
						}
						if (row[6] != null) {
							otherInvestigation = row[6].toString();
						}
						if (row[7] != null) {
							labMark = row[7].toString();
						}
						
						pt.put("investigationId", investigationId);
						pt.put("investigationName", investigationName);
						pt.put("investigationType", investigationType);
						pt.put("dgOrderdt", dgOrderdt);
						pt.put("dgOrderHdId", dgOrderHdId);
						
						pt.put("investigationRemarks", investigationRemarks);
						pt.put("otherInvestigation", otherInvestigation);
						pt.put("labMark", labMark);
						responseListForNew.add(pt);				 
						}
				  }
				
				  if (CollectionUtils.isNotEmpty(listDgMasInvestigation)) {
						for (DgMasInvestigation dgMasInvestigation : listDgMasInvestigation) {
							HashMap<String, Object> jsonMap = new HashMap<String, Object>();
							
							if(dgMasInvestigation.getInvestigationId()!=null) {
								jsonMap.put("investigationId",dgMasInvestigation.getInvestigationId());
							}
							else {
								jsonMap.put("investigationId","");
							}
							if(StringUtils.isNotEmpty(dgMasInvestigation.getInvestigationName())) {
								jsonMap.put("investigationName",dgMasInvestigation.getInvestigationName());
							}
							else {
								jsonMap.put("investigationName","");
							}
							if(StringUtils.isNotEmpty(dgMasInvestigation.getInvestigationType())) {
								jsonMap.put("investigationType",dgMasInvestigation.getInvestigationType());
							}
							else {
								jsonMap.put("investigationType","");
							}
							jsonMap.put("dgOrderdt","");
							jsonMap.put("dgOrderHdId","");
							jsonMap.put("investigationRemarks", "");
							jsonMap.put("otherInvestigation", "");
							jsonMap.put("labMark", "");
							responseListForNew.add(jsonMap);		
				

						}
				  }
				  
				
				
				
				if (responseList != null && responseList.size() > 0) {
					json.put("listVisit", responseList);
					json.put("listDgMasInvestigation", responseListForNew);
 
					json.put("status", 1);

				} else {
					json.put("listVisit", responseList);
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			} else {
				try {
					json.put("msg", "Visit ID data not found");
					json.put("status", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	*//**
	 * @DEscription: This is method submitMedicalExamByMo() is used for submit the
	 *               form record.
	 * @param: payload
	 * @param: request,
	 * @param: response
	 *//*
	@Override
	public String submitMedicalExamByMo(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
try {
		String status = "fail";
		String patientId = payload.get("patientId").toString();
		patientId = OpdServiceImpl.getReplaceString(patientId);

		String visitId = payload.get("visitId").toString();
		visitId = OpdServiceImpl.getReplaceString(visitId);
		
		
		//Criterion cr1=Restrictions.eq("visitId", Long.parseLong(visitId));
		//List<MasMedicalExamReport> listMasMedicalExamReprt = medicalExamDAO.findByCriteria(cr1);
		MasMedicalExamReport masMedicalExamReprt =null;
		if(StringUtils.isNotEmpty(visitId) && !visitId.equalsIgnoreCase(""))
		  masMedicalExamReprt = medicalExamDAO.getMasMedicalExamReprtByVisitId(Long.parseLong(visitId));
		//MasMedicalExamReport masMedicalExamReprt=null;
		if (masMedicalExamReprt==null) {
			masMedicalExamReprt = new MasMedicalExamReport();
			masMedicalExamReprt.setVisitId(Long.parseLong(visitId));
		}
			
			 * else { masMedicalExamReprt=listMasMedicalExamReprt.get(0); }
			 
		masMedicalExamReprt=getPhysicalCapacityCommon(payload,  masMedicalExamReprt);
		//medicalExamDAO.saveOrUpdate(masMedicalExamReprt);
		medicalExamDAO.saveOrUpdateMasMedicalExamReport(masMedicalExamReprt);
		String hospitalId = payload.get("hospitalId").toString();
		hospitalId = OpdServiceImpl.getReplaceString(hospitalId);

		String userId = payload.get("userId").toString();
		userId = OpdServiceImpl.getReplaceString(userId);

		String departmentId = payload.get("departmentId").toString();
		departmentId = OpdServiceImpl.getReplaceString(departmentId);

		String checkForForm = payload.get("checkForForm").toString();
		checkForForm = OpdServiceImpl.getReplaceString(checkForForm);
		status = validateFromForm(payload, departmentId, hospitalId, visitId, patientId, userId);
		String[] msgAll=null;
		if(StringUtils.isNotEmpty(status)) {
			msgAll=status.split("##");
		}
		
		 * if(StringUtils.isNotEmpty(status)) {
		 * updatePatientHistory(payload,Long.parseLong(patientId),Long.parseLong(userId)
		 * ,Long.parseLong(visitId)); }
		 
		
		json.put("visitId", visitId);
		json.put("status", msgAll[1]);
		json.put("visitStatus", msgAll[0]);
}catch(Exception e) {
	e.printStackTrace();
	json.put("visitStatus", "Something is wrong ");
	json.put("status", "Investigation not Submitted Successfully.");
	return json.toString();
}
		return json.toString();
	}

	*//**
	 * @Description :Method is used for updatePatientHistory
	 * @param payload
	 * @param patientId
	 * @param userId
	 * @param visitId
	 *//*
	public void updatePatientHistory(HashMap<String, Object> payload, Long patientId, Long userId, Long visitId) {
		try {
			PatientPastMedHistory patientPastMedHistory = new PatientPastMedHistory();
			patientPastMedHistory.setPatientId(patientId);
			patientPastMedHistory.setLastChgBy(userId);
			String meType = payload.get("meType").toString();
			meType = OpdServiceImpl.getReplaceString(meType);
			patientPastMedHistory.setPatientHistory(meType);
			patientPastMedHistory.setLastChgDate(new Date());
			String age = payload.get("age").toString();
			age = OpdServiceImpl.getReplaceString(age);
			patientPastMedHistory.setAge(age);
			patientPastMedHistory.setDateOfExam(new Date());
			// Change this field
			patientPastMedHistory.setExistingMedicalCat(1l);
			patientPastMedHistory.setPlaceOfExam("Delhi");
			patientPastMedHistory.setVisitId(visitId);
			medicalExamDAO.saveOrUpdateMasPastMedicalHistory(patientPastMedHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	*//**
	 * @Description:Method is used for validate form data
	 * @param payload
	 * @param departmentId
	 * @param hospitalId
	 * @param visitId
	 * @param patientId
	 * @param userId
	 * @return
	 *//*
	@SuppressWarnings("null")
	@Transactional
	public String validateFromForm(HashMap<String, Object> payload, String departmentId, String hospitalId,
			String visitId, String patientId, String userId) {
		String status = "";
		String visitMsg="";
		Session session=null;
		Transaction tx=null;
		try {
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			tx=session.beginTransaction();
			String investigationIdValue = payload.get("investigationIdValue").toString();
			investigationIdValue = OpdServiceImpl.getReplaceString(investigationIdValue);
			String valiCheckValue = payload.get("valiCheckValue").toString();
			valiCheckValue = OpdServiceImpl.getReplaceString(valiCheckValue);
			String[] investigationIdValues=null;
			if(StringUtils.isNotEmpty(investigationIdValue) )
			 investigationIdValues = investigationIdValue.split(",");
			String[] labMark=null;
			if(StringUtils.isNotEmpty(valiCheckValue) )
			 labMark = valiCheckValue.split(",");

			String urgentCheckValue = payload.get("ugentCheckValue").toString();
			urgentCheckValue = OpdServiceImpl.getReplaceString(urgentCheckValue);
			String[] urgentCheckValues = urgentCheckValue.split(",");
			String[] investigationRemarksValues =null;
			if(payload.containsKey("investigationRemarks")) {
			String investigationRemarks = payload.get("investigationRemarks").toString();
			investigationRemarks = OpdServiceImpl.getReplaceString(investigationRemarks);
			 investigationRemarksValues = investigationRemarks.split(",");
			}
			
			
			String otherInvestigation="";
			if(payload.containsKey("otherInvestigation")) {
				  otherInvestigation = payload.get("otherInvestigation").toString();
				otherInvestigation = OpdServiceImpl.getReplaceString(otherInvestigation);
				 
				}
			
			String dgOrderDtIdValue = payload.get("dgOrderDtIdValue").toString();
			dgOrderDtIdValue = OpdServiceImpl.getReplaceString(dgOrderDtIdValue);
			String[] dgOrderDtIdValues = dgOrderDtIdValue.split(",");
			
			String dgOrderHdId = payload.get("dgOrderHdId").toString();
			dgOrderHdId = OpdServiceImpl.getReplaceString(dgOrderHdId);
			String[] dgOrderHdIds = dgOrderHdId.split(",");
			 String saveStatus="";
			if(payload.containsKey("saveStatus")) {
			    saveStatus=payload.get("saveStatus").toString();
			  saveStatus= OpdServiceImpl.getReplaceString(saveStatus);
			}
			Date dateOfInvestigationValue = null;
			 String dateOfInvestigation="";
				if(payload.containsKey("dateOfInvestigation") && payload.get("dateOfInvestigation")!=null) {
					dateOfInvestigation=payload.get("dateOfInvestigation").toString();
					dateOfInvestigation= OpdServiceImpl.getReplaceString(dateOfInvestigation);

					if (StringUtils.isNotEmpty(dateOfInvestigation) && dateOfInvestigation != null && MedicalExamServiceImpl.checkDateFormat(dateOfInvestigation)) {
						dateOfInvestigationValue = HMSUtil.convertStringTypeDateToDateType(dateOfInvestigation.trim());
						 
					}
				}
			
			
			String finalValue = "";
			Integer counter = 1;
			Date date = new Date();
			HashMap<String, String> mapInvestigationMap = new HashMap<>();
			if (investigationIdValues != null &&   investigationIdValues.length > 0) {
				for (int i = 0; i < investigationIdValues.length; i++) {
					
					
					/////////////////////////////////////////////////////
					
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

						if (!dgOrderHdIds[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(dgOrderHdIds[i])) {
							for (int j = i; j < dgOrderHdIds.length; j++) {
								finalValue += "," + dgOrderHdIds[j].trim();
								if (j == i) {
									break;
								}
							}
						} else {
							finalValue += "," + "0";
						}
						if (i < labMark.length && StringUtils.isNotBlank(labMark[i]) && !labMark[i].equalsIgnoreCase("0") &&!labMark[i].equalsIgnoreCase("undefined") ) {
							for (int k = i; k < labMark.length; k++) {
								finalValue += "," + labMark[k].trim();
								if (k == i) {
									break;
								}
							}
						} else {
							finalValue += "," + "0";
						}
					 
						if (i < investigationRemarksValues.length && StringUtils.isNotBlank(investigationRemarksValues[i]) && !investigationRemarksValues[i].trim().equalsIgnoreCase("0") &&!investigationRemarksValues[i].equalsIgnoreCase("undefined")) {
							for (int k = i; k < investigationRemarksValues.length; k++) {
								finalValue += "," + investigationRemarksValues[k].trim();
								if (k == i) {
									break;
								}
							}
						} else {
							finalValue += "," + "0";
						}
						mapInvestigationMap.put(investigationIdValues[i].trim()+"@#"+counter, finalValue);
						finalValue = "";
						counter++;
					}
				}
					
				Long dgResultEntryHdFId=null;
				DgOrderhd dgOrderhd =null;
				if(dgOrderHdIds!=null && dgOrderHdIds[0].equalsIgnoreCase("")) {
					dgOrderhd = new DgOrderhd();
				}
				else {
					dgOrderhd =dgOrderhdDao.getDgOrderhdByDgOrderhdId(Long.parseLong(dgOrderHdIds[0].trim()));	
				}
					
					if (StringUtils.isNotEmpty(departmentId))
						dgOrderhd.setDepartmentId(Long.parseLong(departmentId));
					if (StringUtils.isNotEmpty(hospitalId))
						dgOrderhd.setHospitalId(Long.parseLong(hospitalId));
					if (StringUtils.isNotEmpty(visitId))
						dgOrderhd.setVisitId(Long.parseLong(visitId));
					if (StringUtils.isNotEmpty(userId)) {
						dgOrderhd.setLastChgBy(Long.parseLong(userId));
						dgOrderhd.setDoctorId(Long.parseLong(userId));
					}
					if (StringUtils.isNotEmpty(patientId)) {
						dgOrderhd.setPatientId(Long.parseLong(patientId));
					}
					dgOrderhd.setLastChgDate(new Timestamp(date.getTime()));
					if(dateOfInvestigationValue!=null) {
						dgOrderhd.setOrderDate(dateOfInvestigationValue);
					}
					else {
					dgOrderhd.setOrderDate(new Date());
					}
					// dgOrderhd.setOrderDate(HMSUtil.convertStringTypeDateToDateType(new Date()));
					dgOrderhd.setOrderStatus("P");
					//Changes required UAT
					if(StringUtils.isNotEmpty(otherInvestigation))
					dgOrderhd.setOtherInvestigation(otherInvestigation);
					
					dgResultEntryHdFId=dgOrderhdDao.saveOrUpdateDgOrderHd(dgOrderhd);
					
					////////////////////////////////////Working on Diven section/////////////////////////////////////////////
					if(payload.containsKey("markAsDivenVal")) {
						
						String markAsDivenVal = payload.get("markAsDivenVal").toString();
						markAsDivenVal = OpdServiceImpl.getReplaceString(markAsDivenVal);
						
					if(payload.containsKey("investigationIdValueForDriven") && markAsDivenVal.equalsIgnoreCase("y") ) {
						saveUpdateDivenSection(payload,dgResultEntryHdFId,  userId);
					}
						else {
						////////////////////////////////////////////When unchecked Mark As Diven////////////////////////////////////////////////////////////////
						
						List<DgOrderdt>listDgOrderdt=dgOrderdtDao.getDgOrderHdtByVisitIdForDiver(Long.parseLong(visitId) ,Long.parseLong(patientId));
						if(CollectionUtils.isNotEmpty(listDgOrderdt)) {
						 List<Long>dgOrderdtList=new ArrayList<>();
						   for(DgOrderdt  dg:listDgOrderdt) {
							   dgOrderdtList.add(dg.getOrderdtId());
						   }
						   if(dgOrderdtList!=null && dgOrderdtList.size()>0) {
						   dgOrderhdDao.deleteDivenSection(dgOrderdtList);
						   String patientIdV=payload.get("patientId").toString();
							patientIdV = OpdServiceImpl.getReplaceString(patientIdV);
						   if(patientIdV!=null && patientIdV!="") {
								Patient patient= medicalExamDAO.checkPatientForPatientId(Long.parseLong(patientIdV));
								if(patient!=null) {
									patient.setDiverFlag(null);
								   medicalExamDAO.saveOrUpdatePatient(patient);
									}
								}
						   }
							}
						}
					
					}
					////////////////////////////////////////////////////////////////////////////////
					
				counter = 1;
				
				for (String investigationId : investigationIdValues) {
					if (StringUtils.isNotEmpty(investigationId)) {
						if (mapInvestigationMap.containsKey(investigationId.trim()+"@#"+counter)) {
							String investigationValue = mapInvestigationMap.get(investigationId.trim()+"@#"+counter);

							if (StringUtils.isNotEmpty(investigationValue)) {

								String[] finalValueInvestigation = investigationValue.split(",");
								
								
							if (finalValueInvestigation[0] != null &&  StringUtils.isNotBlank(finalValueInvestigation[0]) && !finalValueInvestigation[0].equals("0")) {
								
								//DgOrderdt dgOrderdt=dgOrderdtDao.find(Long.parseLong(finalValueInvestigation[1]));
								DgOrderdt dgOrderdt=dgOrderdtDao.getDgOrderdtByOrderDtId(Long.parseLong(finalValueInvestigation[1].trim()));
								if(dgOrderdt==null) {
									dgOrderdt = new DgOrderdt();
								}
								  
								dgOrderdt.setOrderhdId(dgResultEntryHdFId);
									dgOrderdt.setInvestigationId(Long.parseLong(finalValueInvestigation[0].trim()));
									dgOrderdt.setLastChgBy(Long.parseLong(userId));
									dgOrderdt.setOrderStatus("P");
									if (StringUtils.isNotEmpty(finalValueInvestigation[3]) && !finalValueInvestigation[3].equalsIgnoreCase(""))
										dgOrderdt.setLabMark(finalValueInvestigation[3].trim());
									//dgOrderdt.setOrderStatus("P");
									dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
									if(dateOfInvestigationValue!=null) {
										dgOrderdt.setOrderDate(dateOfInvestigationValue);
									}
									else {
										dgOrderdt.setOrderDate(new Date());
									}
									
									// if(StringUtils.isNotEmpty(urgentCheckValues[i]) &&
									// !urgentCheckValues[i].equalsIgnoreCase(""))
									
									if(finalValueInvestigation[4]!=null && !finalValueInvestigation[4].equalsIgnoreCase("") && !finalValueInvestigation[4].equalsIgnoreCase("0"))
										dgOrderdt.setInvestigationRemarks(finalValueInvestigation[4]);
									dgOrderdt.setUrgent("n");
								
								dgOrderhdDao.saveOrUpdateDgOrderdt(dgOrderdt);
								
							}
						}
						}
						
					}
					counter++;
				}
					///////////////////////////////////////////////////////
				if(StringUtils.isNotEmpty(saveStatus) && saveStatus.equalsIgnoreCase("v")) {
					status = "Investigation Validated Successfully.";
				}
				else {
					status = "Investigation Submitted Successfully.";
				}
				}
			
			else {
				if(StringUtils.isNotEmpty(saveStatus) && saveStatus.equalsIgnoreCase("v")) {
					status = "Investigation Validated Successfully.";
				}
				else {
				status = "Investigation Submitted Successfully.";
				}
			}
			 
			visitMsg = updateVisitforCommon(visitId,saveStatus);
			visitMsg+="##"+status;
			session.flush();
	        session.clear();
	        tx.commit();
			
		} catch (Exception e) {
			if (tx != null) {
				 try {
			            tx.rollback();
			          
			        } catch(Exception re) {
			            
			           re.printStackTrace();
			        }
			}
			e.printStackTrace();
			status = "Investigation not Submitted Successfully."+e.toString();
			visitMsg+="##"+status;
		}
		finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
			}
		return visitMsg;
	}

	@Transactional
	public String saveUpdateDivenSection(HashMap<String, Object> payload,Long dgOrderHdId,String userId) {
		String patientIdV=payload.get("patientId").toString();
		patientIdV = OpdServiceImpl.getReplaceString(patientIdV);
	if(patientIdV!=null && patientIdV!="") {
		Patient patient= medicalExamDAO.checkPatientForPatientId(Long.parseLong(patientIdV));
		if(patient!=null && patient.getDiverFlag()==null) {
			patient.setDiverFlag("Y");
		   medicalExamDAO.saveOrUpdatePatient(patient);
		}
		}
		String drivenValueCheck = payload.get("drivenValueCheck").toString();
		drivenValueCheck = OpdServiceImpl.getReplaceString(drivenValueCheck);
		String[] drivenValueCheckA = drivenValueCheck.split(",");
		
		
		String drivenDeleteInvestigation = payload.get("drivenDeleteInvestigation").toString();
		drivenDeleteInvestigation = OpdServiceImpl.getReplaceString(drivenDeleteInvestigation);
		String[] drivenDeleteInvestigationA = drivenDeleteInvestigation.split(",");
		
		
		//////////////////////////////////////////////////////////
		   if(drivenDeleteInvestigation!=null) {
			   List<Long>dgOrderdtList=new ArrayList<>();
			   for(String dg:drivenDeleteInvestigationA) {
				   if(StringUtils.isNotEmpty(dg))
				   dgOrderdtList.add(Long.parseLong(dg));
			   }
			   if(dgOrderdtList!=null && dgOrderdtList.size()>0)
				   dgOrderhdDao.deleteDivenSection(dgOrderdtList);
		   }
		
		///////////////////////////////////////./////////////////////
		
		
		
		String investigationIdValueForDriven = payload.get("investigationIdValueForDriven").toString();
		investigationIdValueForDriven = OpdServiceImpl.getReplaceString(investigationIdValueForDriven);
		String[] investigationIdValueForDrivenArr = investigationIdValueForDriven.split(",");
		
		String dgOrderdtDriven = payload.get("dgOrderdtDriven").toString();
		dgOrderdtDriven = OpdServiceImpl.getReplaceString(dgOrderdtDriven);
		String[] dgOrderdtDrivenA = dgOrderdtDriven.split(",");
		
		 
		
		String lastDoneOnDate = payload.get("lastDoneOnDate").toString();
		lastDoneOnDate = OpdServiceImpl.getReplaceString(lastDoneOnDate);
		String[] lastDoneOnDateA = lastDoneOnDate.split(",");
		
		
		String doneOnDate = payload.get("doneOnDate").toString();
		doneOnDate = OpdServiceImpl.getReplaceString(doneOnDate);
		String[] doneOnDateA = doneOnDate.split(",");
		
		String durationDivenr = payload.get("durationDivenr").toString();
		durationDivenr = OpdServiceImpl.getReplaceString(durationDivenr);
		String[] durationDivenrA = durationDivenr.split(",");
		
		
		
		String nextDueOnDate = payload.get("nextDueOnDate").toString();
		nextDueOnDate = OpdServiceImpl.getReplaceString(nextDueOnDate);
		String[] nextDueOnDateA = nextDueOnDate.split(",");
		
		String orderDiverId = payload.get("orderDiverId").toString();
		orderDiverId = OpdServiceImpl.getReplaceString(orderDiverId);
		String[] orderDiverIdA = orderDiverId.split(",");
		 
		
		String allOrderDiverId=getOrderDiverData(investigationIdValueForDrivenArr, drivenValueCheckA
				,payload,lastDoneOnDateA,doneOnDateA,nextDueOnDateA,durationDivenrA,userId,patientIdV);
		
		String [] allOrderDiverIdA=null;
		if(allOrderDiverId!=null) {
			allOrderDiverIdA=allOrderDiverId.split(",");
			
		}
		Integer counter=0;
		
		if(investigationIdValueForDrivenArr!=null)
		//for(String investigation :investigationIdValueForDrivenArr) {
			for(String diverOrderIdAndInvestigation :allOrderDiverIdA) {
			Long investigationIdFina=null;
			Long orderDtId=null;
			Long diverOrderDtId=null;
			if(drivenValueCheckA!=null) {
				for(String valueDivenVal:drivenValueCheckA) {
					String [] allValue=valueDivenVal.split("##");
					if(investigation.trim().equalsIgnoreCase(allValue[0].trim())) {
						investigationIdFina=Long.parseLong(investigation.trim());
						if(allValue[2]!=null && allValue[2].trim()!="")
						diverOrderDtId=Long.parseLong(allValue[2].trim());
						if(allValue.length==2) {
							if(allValue[1]!=null && allValue[1]!="")
								orderDtId=Long.parseLong(allValue[1].trim());
						}
						break;
					}
				}
			} 
			
			String [] allValue=null;
			if(diverOrderIdAndInvestigation.contains("##"))
				allValue=diverOrderIdAndInvestigation.split("##");
			
			
			if(allValue!=null  && allValue.length>0 && !allValue[0].trim().toString().equalsIgnoreCase("")) {
				DgOrderdt dgOrderdt=null;
				List<DgOrderdt>lsiDgOrderdt=null;
			if(allValue!=null && allValue[0]!=null) {
				//dgOrderdt=dgOrderdtDao.getDgOrderdtByOrderDtId(orderDtId);
				if(allValue.length>0 && allValue[0].trim()!=null &&!allValue[0].trim().toString().equalsIgnoreCase(""))
				lsiDgOrderdt=dgOrderdtDao.getDgOrderdtByDiverId(Long.parseLong(allValue[0].trim().toString()));
				if(CollectionUtils.isNotEmpty(lsiDgOrderdt)) {
					dgOrderdt=lsiDgOrderdt.get(0);
				}
				else {
					dgOrderdt=new DgOrderdt();
					dgOrderdt.setLastChgBy(Long.parseLong(userId));
					dgOrderdt.setOrderStatus("P");
					Date date=new Date();
					dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
					dgOrderdt.setUrgent("n");
					dgOrderdt.setOrderhdId(dgOrderHdId);
					Date dateOfInvestigationValue = null;
					 String dateOfInvestigation="";
						if(payload.containsKey("dateOfInvestigation") && payload.get("dateOfInvestigation")!=null) {
							dateOfInvestigation=payload.get("dateOfInvestigation").toString();
							dateOfInvestigation= OpdServiceImpl.getReplaceString(dateOfInvestigation);

							if (StringUtils.isNotEmpty(dateOfInvestigation) && dateOfInvestigation != null && MedicalExamServiceImpl.checkDateFormat(dateOfInvestigation)) {
								dateOfInvestigationValue = HMSUtil.convertStringTypeDateToDateType(dateOfInvestigation.trim());
								 
							}
						}
						if(dateOfInvestigationValue!=null) {
							dgOrderdt.setOrderDate(dateOfInvestigationValue);
						}
						else {
							dgOrderdt.setOrderDate(new Date());
						}
				}
			}
			else {
				dgOrderdt=new DgOrderdt();

				dgOrderdt.setOrderhdId(dgOrderHdId);
				Date dateOfInvestigationValue = null;
				 String dateOfInvestigation="";
					if(payload.containsKey("dateOfInvestigation") && payload.get("dateOfInvestigation")!=null) {
						dateOfInvestigation=payload.get("dateOfInvestigation").toString();
						dateOfInvestigation= OpdServiceImpl.getReplaceString(dateOfInvestigation);

						if (StringUtils.isNotEmpty(dateOfInvestigation) && dateOfInvestigation != null && MedicalExamServiceImpl.checkDateFormat(dateOfInvestigation)) {
							dateOfInvestigationValue = HMSUtil.convertStringTypeDateToDateType(dateOfInvestigation.trim());
							 
						}
					}
					if(dateOfInvestigationValue!=null) {
						dgOrderdt.setOrderDate(dateOfInvestigationValue);
					}
					else {
						dgOrderdt.setOrderDate(new Date());
					}
					if(allOrderDiverIdA!=null && allOrderDiverIdA.length>0)
					for(String orderDetId:allOrderDiverIdA) {
						if(orderDetId!=null && !orderDetId.trim().equalsIgnoreCase("")) {
							String [] finalOrderDetId=orderDetId.split("##");
							if(finalOrderDetId.length>1)
							if(investigationIdFina.toString().equalsIgnoreCase(finalOrderDetId[1].trim())) {
								if(finalOrderDetId[0]!=null && !finalOrderDetId[0].trim().equalsIgnoreCase(""))
								dgOrderdt.setOrderDetId(Long.parseLong(finalOrderDetId[0].trim()));
							}
							break;
						}
						
					}
					
					
					dgOrderdt.setLastChgBy(Long.parseLong(userId));
					dgOrderdt.setOrderStatus("P");
					Date date=new Date();
					dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
					dgOrderdt.setUrgent("n");
			}
			if(allValue!=null && allValue.length>0 && allValue[0]!=null )
			dgOrderdt.setOrderDetId(Long.parseLong(allValue[0].trim()));
			if(allValue!=null && allValue.length>0 && allValue[1]!=null )
			dgOrderdt.setInvestigationId(Long.parseLong(allValue[1].trim()));
			dgOrderhdDao.saveOrUpdateDgOrderdt(dgOrderdt);
				
		}
			
			
			
			
			counter++;
		}
		
		
		
		return null;
		
	}
	
	@Transactional
	public String getOrderDiverData(String [] investigationIdValueForDrivenArr,String[]drivenValueCheckA
			,HashMap<String, Object> payload,String[] lastDoneOnDateA
			,String [] doneOnDateA,String [] nextDueOnDateA,String[] durationDivenrA,String userId,String patientId) {
		
		Integer counter=0;
		String orderDetDiver="";
		String diverOrderdtId = payload.get("diverOrderdtId").toString();
		diverOrderdtId = OpdServiceImpl.getReplaceString(diverOrderdtId);
		String[] diverOrderdtIdA = diverOrderdtId.split(",");
		
		if(investigationIdValueForDrivenArr!=null)
		for(String investigation :investigationIdValueForDrivenArr) {
			
			Long investigationIdFina=null;
			Long orderDiverDtId=null;
			
			
			Long orderDtId=null;
			int countT=0;
			if(drivenValueCheckA!=null) {
				for(String valueDivenVal:drivenValueCheckA) {
					String [] allValue=valueDivenVal.split("##");
					if(investigation.trim().equalsIgnoreCase(allValue[0].trim())) {
						investigationIdFina=Long.parseLong(investigation.trim());
						if(allValue.length>=3 && !allValue[2].trim().equalsIgnoreCase("0")) {
							if(allValue[2]!=null && allValue[2].trim()!="")
								orderDiverDtId=Long.parseLong(allValue[2].trim());
							countT++;
							//break;
						}
						if(allValue.length>=2 && !allValue[1].trim().equalsIgnoreCase("0")) {
							if(allValue[1]!=null && allValue[1].trim()!="")
								orderDtId=Long.parseLong(allValue[1].trim());
							 
						}
						//if(countT>0)
							//break;
					}
				}
			}	
					
					if(countT==0) {
						int count=0;
						for(String diverOrderdtIds:diverOrderdtIdA) {
							String [] allValues=diverOrderdtIds.split("##");
							if(allValues.length>=2 ) {
							if(  investigation.trim().equalsIgnoreCase(allValues[0].trim())) {
								orderDiverDtId=Long.parseLong(allValues[1].trim());
								count++;
								//break;
							  }
							}
						}
						//if(count>0) {
							//break;
						//}
					}
					 
				//}
			//}
			
			DiverOrderDet dgOrderdt=null;
			if(orderDiverDtId!=null) {
				dgOrderdt=dgOrderdtDao.getDiverDgOrderdtByOrderDtId(orderDiverDtId);
				if(dgOrderdt!=null) {
				if(investigationIdFina!=null) {
						dgOrderdt.setDiverAction("Y");
					}
				}
				else {
					dgOrderdt=new DiverOrderDet();
					 Long investigationTeee=null;
					 if(investigationIdFina!=null) {
							dgOrderdt.setDiverAction("Y");
						}
					
					 if(investigationIdFina==null) {
						 investigationTeee=Long.parseLong(investigation.trim());
					 }
					
					 if(investigationIdFina!=null)
						dgOrderdt.setInvestigationId(investigationIdFina);
					 else {
						 dgOrderdt.setInvestigationId(investigationTeee);
					 }	
					 dgOrderdt.setLastChgBy(Long.parseLong(userId));
						Date date=new Date();
						dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
				}
			}
			else {
				dgOrderdt=new DiverOrderDet();
				 Long investigationTeee=null;
				 if(investigationIdFina!=null) {
						dgOrderdt.setDiverAction("Y");
					}
				
				 if(investigationIdFina==null) {
					 investigationTeee=Long.parseLong(investigation.trim());
				 }
				
				 if(investigationIdFina!=null)
					dgOrderdt.setInvestigationId(investigationIdFina);
				 else {
					 dgOrderdt.setInvestigationId(investigationTeee);
				 }	
				 dgOrderdt.setLastChgBy(Long.parseLong(userId));
					Date date=new Date();
					dgOrderdt.setLastChgDate(new Timestamp(date.getTime()));
			}
			
			
			
			
			Date lastDoneOnDateValue = null;
			if(lastDoneOnDateA[counter]!=null && !lastDoneOnDateA[counter].trim().equalsIgnoreCase("")) {
				lastDoneOnDateValue = HMSUtil.convertStringTypeDateToDateType(lastDoneOnDateA[counter].trim());
				if(lastDoneOnDateValue!=null) {
				dgOrderdt.setDiverLastDoneOn(lastDoneOnDateValue);
				}
			}
			
			Date doneOnDateAValue = null;
			if(doneOnDateA[counter]!=null && !doneOnDateA[counter].trim().equalsIgnoreCase("")) {
				doneOnDateAValue = HMSUtil.convertStringTypeDateToDateType(doneOnDateA[counter].trim());
				if(doneOnDateAValue!=null) {
				dgOrderdt.setDiverDoneOn(doneOnDateAValue);
				}
			}
			
			Date nextDueOnDateAValue = null;
			if(nextDueOnDateA[counter]!=null && !nextDueOnDateA[counter].trim().equalsIgnoreCase("")) {
				nextDueOnDateAValue = HMSUtil.convertStringTypeDateToDateType(nextDueOnDateA[counter].trim());
				if(nextDueOnDateAValue!=null) {
				dgOrderdt.setDiverNextDueOn(nextDueOnDateAValue);
				}
			}
			if(durationDivenrA[counter]!=null && !durationDivenrA[counter].trim().equalsIgnoreCase(""))
				dgOrderdt.setDiverDuration(durationDivenrA[counter].trim());
			
			String visitId=payload.get("visitId").toString();
			visitId = OpdServiceImpl.getReplaceString(visitId);
			if(StringUtils.isNotEmpty(visitId))
			dgOrderdt.setVisitId(Long.parseLong(visitId.trim()));
			if(StringUtils.isNotEmpty(patientId))
			dgOrderdt.setPatientId(Long.parseLong(patientId.trim()));
			
			Long oredrDetId=dgOrderhdDao.saveOrUpdateDiverDgOrderdt(dgOrderdt);
			 if(investigationIdFina!=null) {
			if(StringUtils.isEmpty(orderDetDiver)) {
				orderDetDiver=""+oredrDetId+"##"+investigationIdFina;
			}
			else {
				orderDetDiver+=","+	oredrDetId+"##"+investigationIdFina;
			}
			 }
			counter++;
		}
		return orderDetDiver;
	}
	
	*//**
	 * Description:Method is used for update visit information.
	 * 
	 * @param visitId
	 * @param status
	 * @return
	 *//*
	public String updateVisitforCommon(String visitId,String saveStatus) {
		//JSONObject json = new JSONObject();
		String statusVist="";
		try {
			//List<Visit> lisVisit = opdDao.getPatientVisit(Long.parseLong(visitId));
			List<Visit> lisVisit = medicalExamDAO.getPatientVisitForMe(Long.parseLong(visitId));
			if (CollectionUtils.isNotEmpty(lisVisit)) {
				Visit visit = lisVisit.get(0);
				if(StringUtils.isNotEmpty(saveStatus) && saveStatus.equalsIgnoreCase("v")) {
					visit.setExamStatus("V");
				}
				if(StringUtils.isNotEmpty(saveStatus) && saveStatus.equalsIgnoreCase("s")) {
					visit.setExamStatus("S");
				}
				medicalExamDAO.saveOrUpdateVisit(visit);
				//json.put("statusMsg", status);
				//json.put("msg", "Data is successfully submitted");
				//json.put("status", 1);
				if(visit!=null && visit.getVisitId()!=0)
				statusVist="Data is successfully submitted";
				else {
					statusVist="Data is successfully not submitted";
				}
			} else {
				statusVist="Visit not exist";
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusVist="Something is wrong"+e.toString();
		}
		return statusVist;
	}

	*//**
	 * @Description:Method is used for submitMedicalExamByMA
	 * @param: payload
	 * @param:HttpServletRequest request
	 * @param: HttpServletResponse
	 *             response
	 *//*

	@Override
	@Transactional
	public String submitMedicalExamByMA(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		Long medicalExamId = null;
		String msgStatussss = "";
		MasMedicalExamReport masMedicalExamReprt = null;
		String visitId = "";
		String status = "";
		String msgStatus ="";
		Integer countInvesResultEmpty=0;
		String msgForPatient=""; 
		try {
			// saveOrUpdateMedicalCategory(payload);
			// saveOrUpdateMedicalCatComposite(payload);

			try {
				 
				visitId = payload.get("visitId").toString();
				visitId = OpdServiceImpl.getReplaceString(visitId);
				
				masMedicalExamReprt = medicalExamDAO.getMasMedicalExamReprtByVisitId(Long.parseLong(visitId));
				if (masMedicalExamReprt == null) {
					masMedicalExamReprt = new MasMedicalExamReport();
				}
				if(payload.containsKey("rankId")) {
					String rankId = payload.get("rankId").toString();
					rankId = OpdServiceImpl.getReplaceString(rankId);
					if(StringUtils.isNotEmpty(rankId))
					masMedicalExamReprt.setRankId(Long.parseLong(rankId));
					}
				if(payload.containsKey("medicalCompositeNameValue") && payload.get("medicalCompositeNameValue")!=null) {
				String medicalCompositeNameValue = payload.get("medicalCompositeNameValue").toString();
				medicalCompositeNameValue = OpdServiceImpl.getReplaceString(medicalCompositeNameValue);
				if (StringUtils.isNotEmpty(medicalCompositeNameValue)) {
					masMedicalExamReprt.setMedicalCategoryId(Long.parseLong(medicalCompositeNameValue));
				}
				}
				if(payload.containsKey("medicalCompositeDate") && payload.get("medicalCompositeDate")!=null) {
					String medicalCompositeDate = payload.get("medicalCompositeDate").toString();
					medicalCompositeDate = OpdServiceImpl.getReplaceString(medicalCompositeDate);

				if (StringUtils.isNotEmpty(medicalCompositeDate) && medicalCompositeDate != null && MedicalExamServiceImpl.checkDateFormat(medicalCompositeDate)) {
					Date medicalCompositeDateValue = null;
					medicalCompositeDateValue = HMSUtil.convertStringTypeDateToDateType(medicalCompositeDate.trim());
					Timestamp catDate = new Timestamp(medicalCompositeDateValue.getTime());
					if(catDate!=null)
					masMedicalExamReprt.setCategorydate(medicalCompositeDateValue);
				}
				}
			} catch (Exception e) {
				status = "0";
				e.printStackTrace();
			}
			if(payload.containsKey("dateOfExam")) {
			String dateOfExam = payload.get("dateOfExam").toString();
			dateOfExam = OpdServiceImpl.getReplaceString(dateOfExam);
			
			if (StringUtils.isNotEmpty(dateOfExam) && !dateOfExam.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateOfExam)) {
				Date dateOfExamValue = HMSUtil.convertStringTypeDateToDateType(dateOfExam.trim());
				if(dateOfExamValue!=null)
				masMedicalExamReprt.setMediceExamDate(dateOfExamValue);
				 
			}
			}

			
			if(payload.containsKey("doeORDoc")) {
				String doeORDoc = payload.get("doeORDoc").toString();
				doeORDoc = OpdServiceImpl.getReplaceString(doeORDoc);
				if (StringUtils.isNotEmpty(doeORDoc) && !doeORDoc.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(doeORDoc)) {
					Date doeORDocValue = HMSUtil.convertStringTypeDateToDateType(doeORDoc.trim());
					if(doeORDocValue!=null)
						masMedicalExamReprt.setDateOfCompletion(doeORDocValue);
					
				}
				}

			if(StringUtils.isNotEmpty(visitId) && !visitId.equalsIgnoreCase(""))
			masMedicalExamReprt.setVisitId(Long.parseLong(visitId));
			String patientId ="";
			if(payload.containsKey("patientId") && payload.get("patientId")!=null) {
			  patientId = payload.get("patientId").toString();
			patientId = OpdServiceImpl.getReplaceString(patientId);
			if(StringUtils.isNotEmpty(patientId) && !patientId.equalsIgnoreCase(""))
			masMedicalExamReprt.setPatientId(Long.parseLong(patientId));
			}
			String hospitalId = payload.get("hospitalId").toString();
			hospitalId = OpdServiceImpl.getReplaceString(hospitalId);
			masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));

			String saveInDraft = "";
			if(payload.containsKey("saveInDraft") && payload.get("saveInDraft")!=null) {
			  saveInDraft = payload.get("saveInDraft").toString();
			saveInDraft = OpdServiceImpl.getReplaceString(saveInDraft);
			}
			 
				if(payload.containsKey("authority") && payload.get("authority")!=null) {
				String authority = payload.get("authority").toString();
				authority = OpdServiceImpl.getReplaceString(authority);
				if (StringUtils.isNotEmpty(authority) && !authority.equalsIgnoreCase("")) {
					masMedicalExamReprt.setAuthority(authority);
				}
				}
				if(payload.containsKey("place") && payload.get("place")!=null) {
				String place = payload.get("place").toString();
				place = OpdServiceImpl.getReplaceString(place);
				if (StringUtils.isNotEmpty(place) && !place.equalsIgnoreCase("")) {
					masMedicalExamReprt.setPlace(place);
				}
				}
				String dateAME = payload.get("dateAME").toString();
				dateAME = OpdServiceImpl.getReplaceString(dateAME);
				if (StringUtils.isNotEmpty(dateAME) && !dateAME.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateAME)) {
					Date dateAMEValue = HMSUtil.convertStringTypeDateToDateType(dateAME.trim());
					if(dateAMEValue!=null)
					masMedicalExamReprt.setDateMedicalBoardExam(dateAMEValue);
				}

			 
			if (StringUtils.isNotEmpty(saveInDraft) && !saveInDraft.equalsIgnoreCase("draftMa")) {
				String petStatus = payload.get("pet").toString();
				petStatus = OpdServiceImpl.getReplaceString(petStatus);
				if (petStatus != null && !petStatus.equalsIgnoreCase("0")) {
					masMedicalExamReprt.setPetStatusOn(petStatus.trim());

				} else {
					masMedicalExamReprt.setPetStatusOn(null);
				}
				if (masMedicalExamReprt != null && masMedicalExamReprt.getForwardUnitId() == null)
					masMedicalExamReprt.setForwardUnitId(null);
				
				String petDateValue = payload.get("petDateValue").toString();
				petDateValue = OpdServiceImpl.getReplaceString(petDateValue);
				if(StringUtils.isNotEmpty(petDateValue) && MedicalExamServiceImpl.checkDateFormat(petDateValue)) {
				Date medicalCompositeDateValue = null;
				medicalCompositeDateValue = HMSUtil.convertStringTypeDateToDateType(petDateValue.trim());
				if(medicalCompositeDateValue!=null) {
				Timestamp catDate = new Timestamp(medicalCompositeDateValue.getTime());
				if(catDate!=null)
					masMedicalExamReprt.setPetDate(medicalCompositeDateValue);
				}
				}
				}
			

			String userId = payload.get("userId").toString();
			userId = OpdServiceImpl.getReplaceString(userId);
			masMedicalExamReprt.setLastChgBy(Long.parseLong(userId));
			Users users=null;
			String designationIdCurrent=null;
			if(StringUtils.isNotEmpty(userId)) {
				 users=medicalExamDAO.getUserByUserId(Long.parseLong(userId));
				 if(users!=null) {
					 String currentUserDesignation=users.getDesignationId();
					 if(StringUtils.isNotEmpty(currentUserDesignation)) {
						 String [] currentUserDesignationVal=currentUserDesignation.split(",");
						 if(currentUserDesignationVal!=null && currentUserDesignationVal.length>0) {
							 designationIdCurrent=currentUserDesignationVal[0];
						 }
					 }
				 }
			}
			
			masMedicalExamReprt.setVisitId(Long.parseLong(visitId));
			String hospitalId = payload.get("hospitalId").toString();
			hospitalId = OpdServiceImpl.getReplaceString(hospitalId);
			
			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftMa")) {

				masMedicalExamReprt.setStatus("s");
				masMedicalExamReprt.setApprovedBy("MA");
				masMedicalExamReprt.setMaUserId(Long.parseLong(userId));
			
				masMedicalExamReprt.setMaDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMaDesignationId(Long.parseLong(designationIdCurrent.trim()));
				if(StringUtils.isNotEmpty(hospitalId) && !hospitalId.equalsIgnoreCase(""))
				masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));
			}

			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftMo")) {

				masMedicalExamReprt.setApprovedBy("MO");
				if(StringUtils.isNotEmpty(userId) && !userId.equalsIgnoreCase(""))
				masMedicalExamReprt.setMoUserId(Long.parseLong(userId));
				String finalObservationMo = payload.get("finalObservationMo").toString();
				finalObservationMo = OpdServiceImpl.getReplaceString(finalObservationMo);
				masMedicalExamReprt.setFinalobservation(finalObservationMo);
				
				masMedicalExamReprt.setMoDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent) && !designationIdCurrent.equalsIgnoreCase(""))
					masMedicalExamReprt.setMoDesignationId(Long.parseLong(designationIdCurrent.trim()));
				
				//masMedicalExamReprt = getActionForMedicalExam(payload, masMedicalExamReprt);
				if(StringUtils.isNotEmpty(hospitalId) && !hospitalId.equalsIgnoreCase(""))
				masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));
			}
			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftRMo")) {

				masMedicalExamReprt.setApprovedBy("RMO");
				masMedicalExamReprt.setCmUserId(Long.parseLong(userId));

				String finalObservationRmo = payload.get("finalObservationRmo").toString();
				finalObservationRmo = OpdServiceImpl.getReplaceString(finalObservationRmo);
				masMedicalExamReprt.setAaFinalObservation(finalObservationRmo);

				String remarksRmo = payload.get("remarksRmo").toString();
				remarksRmo = OpdServiceImpl.getReplaceString(remarksRmo);
				masMedicalExamReprt.setAaRemarks(remarksRmo);

				masMedicalExamReprt.setCmDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent) && !designationIdCurrent.equalsIgnoreCase(""))
					masMedicalExamReprt.setCmDesignationId(Long.parseLong(designationIdCurrent.trim()));
			
				
				//masMedicalExamReprt = getActionForMedicalExam(payload, masMedicalExamReprt);
				masMedicalExamReprt.setRmoHospitalId(Long.parseLong(hospitalId));
			}

			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftPRMo")) {

				masMedicalExamReprt.setApprovedBy("PRMo");
				masMedicalExamReprt.setMdUserId(Long.parseLong(userId));
				String finalObservationPRmo = payload.get("finalObservationPRmo").toString();
				finalObservationPRmo = OpdServiceImpl.getReplaceString(finalObservationPRmo);
				masMedicalExamReprt.setPaFinalobservation(finalObservationPRmo);

				String remarksPRmo = payload.get("remarksPRmo").toString();
				remarksPRmo = OpdServiceImpl.getReplaceString(remarksPRmo);
				masMedicalExamReprt.setPaRemarks(remarksPRmo);

				masMedicalExamReprt.setMdDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMdDesignationId(Long.parseLong(designationIdCurrent.trim()));
			
				//masMedicalExamReprt = getActionForMedicalExam(payload, masMedicalExamReprt);
				masMedicalExamReprt.setPdmsHospitalId(Long.parseLong(hospitalId));
			}
			
			try {
				if (StringUtils.isNotEmpty(hospitalId)) {
					List<MasUnit> listMasUnit = medicalExamDAO.getMasUnitListByHospitalId(Long.parseLong(hospitalId));
					if (CollectionUtils.isNotEmpty(listMasUnit))
						masMedicalExamReprt.setUnitId(listMasUnit.get(0).getUnitId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(payload.containsKey("unitIdOn") && payload.get("unitIdOn")!=null) {
				String unitOrSlip=payload.get("unitIdOn").toString();
				unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
				
			if(StringUtils.isNotEmpty(unitOrSlip) && !unitOrSlip.equalsIgnoreCase("undefined")) {
				masMedicalExamReprt.setUnitId(Long.parseLong(unitOrSlip));
			}
			}
			if(payload.containsKey("branchOrTradeIdOn") && payload.get("branchOrTradeIdOn")!=null) {
			String branchOrTrade=payload.get("branchOrTradeIdOn").toString();
			branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
			if(StringUtils.isNotEmpty(branchOrTrade) && !branchOrTrade.equalsIgnoreCase("undefined")) {
				masMedicalExamReprt.setBranchId(Long.parseLong(branchOrTrade));
			}
			}
			
			if(payload.containsKey("dob")) {
				String dateOfBirth = payload.get("dob").toString();
				dateOfBirth = OpdServiceImpl.getReplaceString(dateOfBirth);
				if (StringUtils.isNotEmpty(dateOfBirth) && !dateOfBirth.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateOfBirth)) {
					Date dateOfBirthD = HMSUtil.convertStringTypeDateToDateType(dateOfBirth.trim());
					if(dateOfBirthD!=null)
					masMedicalExamReprt.setDateOfBirth(dateOfBirthD);
				}
				}
			
		if(payload.containsKey("meAgeForOn")) {
			String meAgeForOn = payload.get("meAgeForOn").toString();
			meAgeForOn = OpdServiceImpl.getReplaceString(meAgeForOn);
			String ageForMe="";
			if (StringUtils.isNotEmpty(meAgeForOn) && !meAgeForOn.equalsIgnoreCase("")) {
			if (StringUtils.isNotEmpty(meAgeForOn)) {
				String[] ageMale = meAgeForOn.split("/");
				ageForMe = ageMale[0];
			}
				masMedicalExamReprt.setApparentAge(ageForMe);
			}
		}
		
		if(payload.containsKey("totalService")) {
				String totalService = payload.get("totalService").toString();
				totalService = OpdServiceImpl.getReplaceString(totalService);
				if (StringUtils.isNotEmpty(totalService)) {
					masMedicalExamReprt.setTotalService(totalService);
				}
				}
			
			
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = submitDentalAndPhysicalCapacity(payload, masMedicalExamReprt);
			}
			
			// masMedicalExamReprt=lifeStyleFactors( payload, masMedicalExamReprt);
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = centralNervousSystemAndOthers(payload, masMedicalExamReprt);
			}
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = respiratoryAndGastroIntestinalSystem(payload, masMedicalExamReprt);
			}
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = cardioVascularSystem(payload, masMedicalExamReprt);
			}
			//masMedicalExamReprt = investigationData(payload, masMedicalExamReprt);
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = submitHearing(payload, masMedicalExamReprt);
			}
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = submitVision(payload, masMedicalExamReprt);
			}
			masMedicalExamReprt.setLastChangedDate(new Date());
			
			if(payload.containsKey("typeOfCommission")) {
			String typeOfCommission = payload.get("typeOfCommission").toString();
			typeOfCommission = OpdServiceImpl.getReplaceString(typeOfCommission);
			if(StringUtils.isNotEmpty(typeOfCommission)&& !typeOfCommission.equalsIgnoreCase("0")) {
				masMedicalExamReprt.setTypeofcommision(typeOfCommission);
			}
				
			}
			if(payload.containsKey("serviceOfEmployee")) {
			String serviceOfEmployee = payload.get("serviceOfEmployee").toString();
			serviceOfEmployee = OpdServiceImpl.getReplaceString(serviceOfEmployee);
			if(StringUtils.isNotEmpty(serviceOfEmployee) && !serviceOfEmployee.equalsIgnoreCase("0")) {
				masMedicalExamReprt.setServiceTypeId(Long.parseLong(serviceOfEmployee)); 
			}
			
			}
			if(masMedicalExamReprt!=null) {
			  msgStatus = medicalExamDAO.saveOrUpdateMedicalExam(masMedicalExamReprt, payload, patientId,
					hospitalId, userId);
			}
			if(masMedicalExamReprt==null) {
				msgStatus=0+"##"+"Data is not updated because something is wrong";
				}
			
			String[] msgStatusa = msgStatus.split("##");
			medicalExamId = Long.parseLong(msgStatusa[0]);
			msgStatussss = msgStatusa[1];
			 msgForPatient=msgStatusa[2];
			if (medicalExamId != 0) {
				status = "1";
				countInvesResultEmpty=medicalExamDAO.getInvestigationResultEmpty(Long.parseLong(visitId));
			} else {
				status = "0";
				countInvesResultEmpty=0;
			}
		} catch (Exception e) {
			status = "0";
			msgStatussss = "Data is not updated because something is wrong";
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("visitId", medicalExamId);
		json.put("status", status);
		json.put("errorMes", msgStatussss);
		json.put("countInvesResultEmptystatus", countInvesResultEmpty);
		json.put("msgForPatient", msgForPatient);
		
		return json.toString();
	}

	
	*//**
	 * @Description: Method is used for get getActionForMedicalExam
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*

	public MasMedicalExamReport getActionForMedicalExam(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		try {

			String actionMe = payload.get("actionMe").toString();
			actionMe = OpdServiceImpl.getReplaceString(actionMe);

			String visitId = payload.get("visitId").toString();
			visitId = OpdServiceImpl.getReplaceString(visitId);

			if (StringUtils.isNotEmpty(actionMe) && (actionMe.equalsIgnoreCase("approveAndClose")
					|| actionMe.equalsIgnoreCase("approveAndForward"))) {
				try {
					if (payload.get("remarksForApproval") != null && payload.get("remarksForApproval") != "") {
						String remarksForApproval = payload.get("remarksForApproval").toString();
						remarksForApproval = OpdServiceImpl.getReplaceString(remarksForApproval);
						masMedicalExamReprt.setRemarksForward(remarksForApproval);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (actionMe.equalsIgnoreCase("approveAndClose")) {
					masMedicalExamReprt.setStatus("ac");

					// Update Visit Status

					updateVisitStatus(visitId, "C");
					return masMedicalExamReprt;
				}
				if (actionMe.equalsIgnoreCase("approveAndForward")) {
					masMedicalExamReprt.setStatus("af");

					String forwardUnitId = payload.get("forwardTo").toString();
					forwardUnitId = OpdServiceImpl.getReplaceString(forwardUnitId);
					masMedicalExamReprt.setForwardUnitId(Long.parseLong(forwardUnitId));
					
					
					String designationForMe = payload.get("designationForMe").toString();
					designationForMe = OpdServiceImpl.getReplaceString(designationForMe);
					masMedicalExamReprt.setFowardedDesignationId(Long.parseLong(designationForMe));
					
					
					updateVisitStatus(visitId, "F");

				}
				return masMedicalExamReprt;
			}
			if (StringUtils.isNotEmpty(actionMe) && actionMe.equalsIgnoreCase("reject")) {

				String remarksReject = payload.get("remarksReject").toString();
				remarksReject = OpdServiceImpl.getReplaceString(remarksReject);
				masMedicalExamReprt.setStatus("rj");
				masMedicalExamReprt.setRemarksRej(remarksReject);
				
				updateVisitStatus(visitId, "R");
				return masMedicalExamReprt;
			}

			if (StringUtils.isNotEmpty(actionMe) && actionMe.equalsIgnoreCase("pending")) {

				String remarksPending = payload.get("remarksPending").toString();
				remarksPending = OpdServiceImpl.getReplaceString(remarksPending);
				masMedicalExamReprt.setStatus("pe");
				masMedicalExamReprt.setRemarksPending(remarksPending);

				String nextAppointmentDate = payload.get("nextAppointmentDate").toString();
				nextAppointmentDate = OpdServiceImpl.getReplaceString(nextAppointmentDate);
				if (StringUtils.isNotBlank(nextAppointmentDate) && !nextAppointmentDate.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(nextAppointmentDate)) {
					Date appoinmentDate = HMSUtil.convertStringTypeDateToDateType(nextAppointmentDate.trim());
					if(appoinmentDate!=null)
					masMedicalExamReprt.setAppointmentDate(appoinmentDate);
				}
				return masMedicalExamReprt;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for submitDentalAndPhysicalCapacity
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*

	public static MasMedicalExamReport submitDentalAndPhysicalCapacity(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		try {
		if (payload.get("totalNoOfTeath") != null) {
			String totalNoOfTeath = payload.get("totalNoOfTeath").toString();
			totalNoOfTeath = OpdServiceImpl.getReplaceString(totalNoOfTeath);
			masMedicalExamReprt.setTotalTeeth(totalNoOfTeath);
		}
		if (payload.get("totalNoOfDefective") != null) {
			String totalNoOfDefective = payload.get("totalNoOfDefective").toString();
			totalNoOfDefective = OpdServiceImpl.getReplaceString(totalNoOfDefective);
			masMedicalExamReprt.setTotalDefectiveTeeth(totalNoOfDefective);
		}

		if (payload.get("totalNoOfDentalPoints") != null) {
			String totalNoOfDentalPoints = payload.get("totalNoOfDentalPoints").toString();
			totalNoOfDentalPoints = OpdServiceImpl.getReplaceString(totalNoOfDentalPoints);
			masMedicalExamReprt.setTotalNoDentalPoint(totalNoOfDentalPoints);
		}

		if (payload.get("missing") != null) {

			String missing = payload.get("missing").toString();
			missing = OpdServiceImpl.getReplaceString(missing);
			masMedicalExamReprt.setMissingTeeth(missing);
		} else {
			masMedicalExamReprt.setMissingTeeth(null);
		}
		if (payload.get("unSavable") != null) {
			String unSavable = payload.get("unSavable").toString();
			unSavable = OpdServiceImpl.getReplaceString(unSavable);
			masMedicalExamReprt.setUnsaveableTeeth(unSavable);
		} else {
			masMedicalExamReprt.setUnsaveableTeeth(null);
		}
		if (payload.get("conditionOfGums") != null) {
			String conditionOfGums = payload.get("conditionOfGums").toString();
			conditionOfGums = OpdServiceImpl.getReplaceString(conditionOfGums);
			masMedicalExamReprt.setConditionofgums(conditionOfGums);
		} else {
			masMedicalExamReprt.setConditionofgums(null);
		}

		if (payload.get("urMChecked") != null) {
			String urMChecked = payload.get("urMChecked").toString();
			urMChecked = OpdServiceImpl.getReplaceString(urMChecked);
			masMedicalExamReprt.setMTUr(urMChecked);
		} else {
			masMedicalExamReprt.setMTUr(null);
		}

		if (payload.get("ulMChecked") != null) {
			String ulMChecked = payload.get("ulMChecked").toString();
			ulMChecked = OpdServiceImpl.getReplaceString(ulMChecked);
			masMedicalExamReprt.setMTUl(ulMChecked);
		} else {
			masMedicalExamReprt.setMTUl(null);
		}

		if (payload.get("llMChecked") != null) {
			String llMChecked = payload.get("llMChecked").toString();
			llMChecked = OpdServiceImpl.getReplaceString(llMChecked);
			masMedicalExamReprt.setMTLl(llMChecked);
		} else {
			masMedicalExamReprt.setMTLl(null);
		}

		if (payload.get("lrMChecked") != null) {
			String lrMChecked = payload.get("lrMChecked").toString();
			lrMChecked = OpdServiceImpl.getReplaceString(lrMChecked);
			masMedicalExamReprt.setMTLr(lrMChecked);

		} else {
			masMedicalExamReprt.setMTLr(null);
		}
		if (payload.get("unUrChecked") != null) {
			String unUrChecked = payload.get("unUrChecked").toString();
			unUrChecked = OpdServiceImpl.getReplaceString(unUrChecked);
			masMedicalExamReprt.setUTUr(unUrChecked);
		} else {
			masMedicalExamReprt.setUTUr(null);
		}
		if (payload.get("unUlChecked") != null) {
			String unUlChecked = payload.get("unUlChecked").toString();
			unUlChecked = OpdServiceImpl.getReplaceString(unUlChecked);
			masMedicalExamReprt.setUTUl(unUlChecked);
		} else {
			masMedicalExamReprt.setUTUl(null);
		}

		if (payload.get("unLlChecked") != null) {
			String unLlChecked = payload.get("unLlChecked").toString();
			unLlChecked = OpdServiceImpl.getReplaceString(unLlChecked);
			masMedicalExamReprt.setUTLl(unLlChecked);
		} else {
			masMedicalExamReprt.setUTLl(null);
		}
		if (payload.get("unLrChecked") != null) {
			String unLrChecked = payload.get("unLrChecked").toString();
			unLrChecked = OpdServiceImpl.getReplaceString(unLrChecked);
			masMedicalExamReprt.setUTLr(unLrChecked);
		} else {
			masMedicalExamReprt.setUTLr(null);
		}

		if (payload.get("dentalOffier") != null) {
			String dentalOffier = payload.get("dentalOffier").toString();
			dentalOffier = OpdServiceImpl.getReplaceString(dentalOffier);
			masMedicalExamReprt.setDentalOfficer(dentalOffier);
		} else {
			masMedicalExamReprt.setDentalOfficer(null);
		}

		if (payload.get("checkupDate") != null) {
			String checkupDate = payload.get("checkupDate").toString();
			checkupDate = OpdServiceImpl.getReplaceString(checkupDate);
			if (checkupDate!=null && !checkupDate.toString().equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(checkupDate)) {
				Date checkUpdateVal=HMSUtil.convertStringTypeDateToDateType(checkupDate.trim());
				if(checkUpdateVal!=null)
					masMedicalExamReprt.setDentalCheckupDate(checkUpdateVal);
		}
			}
		if (payload.get("remarks") != null) {
			String remarks = payload.get("remarks").toString();
			remarks = OpdServiceImpl.getReplaceString(remarks);
			masMedicalExamReprt.setRemarks(remarks);
		}
		masMedicalExamReprt=getPhysicalCapacityCommon(payload,  masMedicalExamReprt);
		
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		 * if(payload.get("sportsman")!=null) { String
		 * sportsMan=payload.get("sportsman").toString();
		 * sportsMan=OpdServiceImpl.getReplaceString(sportsMan);
		 * masMedicalExamReprt.setSportman(sportsMan); }
		 

		return masMedicalExamReprt;
	}
	
	public static MasMedicalExamReport getPhysicalCapacityCommon(HashMap<String, Object> payload,MasMedicalExamReport masMedicalExamReprt) {
		if (payload.get("height") != null && !payload.get("height").toString().equalsIgnoreCase("")) {
			String height = payload.get("height").toString();
			height = OpdServiceImpl.getReplaceString(height);
			if (StringUtils.isNotBlank(height) && !height.equalsIgnoreCase(""))
				masMedicalExamReprt.setHeight(height);
		}

		if (payload.get("weight") != null) {
			String weight = payload.get("weight").toString();
			weight = OpdServiceImpl.getReplaceString(weight);
			if (StringUtils.isNotBlank(weight) && !weight.equalsIgnoreCase(""))
				masMedicalExamReprt.setWeight(weight);
		}

		if (payload.get("idealWeight") != null) {
			String idealWeight = payload.get("idealWeight").toString();
			idealWeight = OpdServiceImpl.getReplaceString(idealWeight);
			if (StringUtils.isNotBlank(idealWeight) && !idealWeight.equalsIgnoreCase(""))
				masMedicalExamReprt.setIdealweight(idealWeight);
		}

		if (payload.get("overWeight") != null) {
			String overWeight = payload.get("overWeight").toString();
			overWeight = OpdServiceImpl.getReplaceString(overWeight);
			masMedicalExamReprt.setOverweight(overWeight);
		}

		
		 * if(payload.get("bodyFat")!=null) { String
		 * bodyFat=payload.get("bodyFat").toString();
		 * bodyFat=OpdServiceImpl.getReplaceString(bodyFat);
		 * masMedicalExamReprt.setBodyfat(bodyFat); }
		 

		if (payload.get("waist") != null) {
			String waist = payload.get("waist").toString();
			waist = OpdServiceImpl.getReplaceString(waist);
			masMedicalExamReprt.setWaist(waist);
		}

		
		 * if(payload.get("bmi")!=null) { String bmi=payload.get("bmi").toString();
		 * bmi=OpdServiceImpl.getReplaceString(bmi); masMedicalExamReprt.setBhi(bmi); }
		 * if(payload.get("hip")!=null) { String hip=payload.get("hip").toString();
		 * hip=OpdServiceImpl.getReplaceString(hip); masMedicalExamReprt.setHips(hip); }
		 * if(payload.get("whr")!=null) { String whr=payload.get("whr").toString();
		 * whr=OpdServiceImpl.getReplaceString(whr); masMedicalExamReprt.setWhr(whr); }
		 
		
		 * if(payload.get("skinFoldExpansion")!=null) { String
		 * skinFoldExpansion=payload.get("skinFoldExpansion").toString();
		 * skinFoldExpansion=OpdServiceImpl.getReplaceString(skinFoldExpansion);
		 * masMedicalExamReprt.setSkin(skinFoldExpansion); }
		 
		if (payload.get("chestFullExpansion") != null) {
			String chestFullExpansion = payload.get("chestFullExpansion").toString();
			chestFullExpansion = OpdServiceImpl.getReplaceString(chestFullExpansion);
			masMedicalExamReprt.setChestfullexpansion(chestFullExpansion);
		}

		if (payload.get("rangeOfExpansion") != null) {
			String rangeOfExpansion = payload.get("rangeOfExpansion").toString();
			rangeOfExpansion = OpdServiceImpl.getReplaceString(rangeOfExpansion);
			masMedicalExamReprt.setRangeofexpansion(rangeOfExpansion);
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for submitVision
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*
	public static MasMedicalExamReport submitVision(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		try {
		if (payload.get("distantWithoutGlasses") != null) {
			String distantWithoutGlasses = payload.get("distantWithoutGlasses").toString();
			distantWithoutGlasses = OpdServiceImpl.getReplaceString(distantWithoutGlasses);
			masMedicalExamReprt.setWthoutGlassesRDistant(distantWithoutGlasses);
		}

		if (payload.get("distantWithoutGlassesL") != null) {
			String distantWithoutGlassesL = payload.get("distantWithoutGlassesL").toString();
			distantWithoutGlassesL = OpdServiceImpl.getReplaceString(distantWithoutGlassesL);
			masMedicalExamReprt.setWithoutGlassesLDistant(distantWithoutGlassesL);
		}

		if (payload.get("nearWithoutGlasses") != null) {
			String nearWithoutGlasses = payload.get("nearWithoutGlasses").toString();
			nearWithoutGlasses = OpdServiceImpl.getReplaceString(nearWithoutGlasses);
			masMedicalExamReprt.setWithoutGlassesRNearvision(nearWithoutGlasses);
		}
		if (payload.get("nearWithoutGlassesL") != null) {
			String nearWithoutGlassesL = payload.get("nearWithoutGlassesL").toString();
			nearWithoutGlassesL = OpdServiceImpl.getReplaceString(nearWithoutGlassesL);
			masMedicalExamReprt.setWithoutGlassesLNearvision(nearWithoutGlassesL);
		}

		if (payload.get("cpWithoutGlasses") != null) {
			String cpWithoutGlasses = payload.get("cpWithoutGlasses").toString();
			cpWithoutGlasses = OpdServiceImpl.getReplaceString(cpWithoutGlasses);
			masMedicalExamReprt.setNearVisionWithoutGlassCp(cpWithoutGlasses);
		}

		if (payload.get("distantWithGlasses") != null) {
			String distantWithGlasses = payload.get("distantWithGlasses").toString();
			distantWithGlasses = OpdServiceImpl.getReplaceString(distantWithGlasses);
			masMedicalExamReprt.setWithGlassesRDistant(distantWithGlasses);
		}

		if (payload.get("distantWithGlassesL") != null) {
			String distantWithGlassesL = payload.get("distantWithGlassesL").toString();
			distantWithGlassesL = OpdServiceImpl.getReplaceString(distantWithGlassesL);
			masMedicalExamReprt.setWithGlassesLDistant(distantWithGlassesL);
		}

		if (payload.get("nearWithGlasses") != null) {
			String nearWithGlasses = payload.get("nearWithGlasses").toString();
			nearWithGlasses = OpdServiceImpl.getReplaceString(nearWithGlasses);
			masMedicalExamReprt.setWithGlassesRNearvision(nearWithGlasses);
		}

		if (payload.get("nearWithGlassesL") != null) {
			String nearWithGlassesL = payload.get("nearWithGlassesL").toString();
			nearWithGlassesL = OpdServiceImpl.getReplaceString(nearWithGlassesL);
			masMedicalExamReprt.setWithGlassesLNearvision(nearWithGlassesL);
		}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for submitHearing
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*
	public static MasMedicalExamReport submitHearing(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		// Hearing

		try {
			if (payload.get("fwR") != null) {
				String fwR = payload.get("fwR").toString();
				fwR = OpdServiceImpl.getReplaceString(fwR);
				if (StringUtils.isNotEmpty(fwR))
					masMedicalExamReprt.setEarHearingRfw(Long.parseLong(fwR));
			}
			if (payload.get("fwL") != null && !payload.get("fwL").equals("")) {
				String fwL = payload.get("fwL").toString();
				fwL = OpdServiceImpl.getReplaceString(fwL);
				if (StringUtils.isNotEmpty(fwL))
					masMedicalExamReprt.setEarHearingLfw(Long.parseLong(fwL));
			}

			if (payload.get("fwBoth") != null && !payload.get("fwBoth").equals("")) {
				String fwBoth = payload.get("fwBoth").toString();
				fwBoth = OpdServiceImpl.getReplaceString(fwBoth);
				if (StringUtils.isNotEmpty(fwBoth))
					masMedicalExamReprt.setEarHearingBothFw(Long.parseLong(fwBoth));
			}
			if (payload.get("cvR") != null && !payload.get("cvR").equals("")) {
				String cvR = payload.get("cvR").toString();
				cvR = OpdServiceImpl.getReplaceString(cvR);
				if (StringUtils.isNotEmpty(cvR))
					masMedicalExamReprt.setHearingRcv(Long.parseLong(cvR));
			}
			if (payload.get("cvL") != null && !payload.get("cvL").equals("")) {
				String cvL = payload.get("cvL").toString();
				cvL = OpdServiceImpl.getReplaceString(cvL);
				if (StringUtils.isNotEmpty(cvL))
					masMedicalExamReprt.setHearingLcv(Long.parseLong(cvL));
			}

			if (payload.get("cvBoth") != null && !payload.get("cvBoth").equals("")) {
				String cvBoth = payload.get("cvBoth").toString();
				cvBoth = OpdServiceImpl.getReplaceString(cvBoth);
				if (StringUtils.isNotEmpty(cvBoth))
					masMedicalExamReprt.setHearingBothCv(Long.parseLong(cvBoth));
			}
			if (payload.get("tmR") != null && !payload.get("tmR").equals("")) {
				String tmR = payload.get("tmR").toString();
				tmR = OpdServiceImpl.getReplaceString(tmR);
				if (StringUtils.isNotEmpty(tmR))
					masMedicalExamReprt.setTympanicr(tmR);
			}
			if (payload.get("tmL") != null && !payload.get("tmL").equals("")) {
				String tmL = payload.get("tmL").toString();
				tmL = OpdServiceImpl.getReplaceString(tmL);
				if (StringUtils.isNotEmpty(tmL))
					masMedicalExamReprt.setTympanicl(tmL);
			}

			if (payload.get("mobilityR") != null && !payload.get("mobilityR").equals("")) {
				String mobilityR = payload.get("mobilityR").toString();
				mobilityR = OpdServiceImpl.getReplaceString(mobilityR);
				if (StringUtils.isNotEmpty(mobilityR))
					masMedicalExamReprt.setMobilityr(mobilityR);
			}

			if (payload.get("mobilityL") != null && !payload.get("mobilityL").equals("")) {
				String mobilityL = payload.get("mobilityL").toString();
				mobilityL = OpdServiceImpl.getReplaceString(mobilityL);
				if (StringUtils.isNotEmpty(mobilityL))
					masMedicalExamReprt.setMobilityl(mobilityL);
			}
			if (payload.get("noseThroatSinuses") != null && !payload.get("noseThroatSinuses").equals("")) {
				String noseThroatSinuses = payload.get("noseThroatSinuses").toString();
				noseThroatSinuses = OpdServiceImpl.getReplaceString(noseThroatSinuses);
				if (StringUtils.isNotEmpty(noseThroatSinuses))
					masMedicalExamReprt.setNoseThroatSinuses(noseThroatSinuses);
			}

			if (payload.get("audiometryRecord") != null && !payload.get("audiometryRecord").equals("")) {
				String audiometryRecord = payload.get("audiometryRecord").toString();
				audiometryRecord = OpdServiceImpl.getReplaceString(audiometryRecord);
				if (StringUtils.isNotEmpty(audiometryRecord))
					masMedicalExamReprt.setAudiometryRecord(audiometryRecord);

				if (StringUtils.isNotEmpty(audiometryRecord) && audiometryRecord.equalsIgnoreCase("Others")) {
					String audiometryRecordForOther = payload.get("audiometryRecordForOther").toString();
					audiometryRecordForOther = OpdServiceImpl.getReplaceString(audiometryRecordForOther);
					masMedicalExamReprt.setAudiometryRecordOthers(audiometryRecordForOther);
				}

				if (payload.get("noseSinuses") != null && !payload.get("noseSinuses").equals("")) {
					String noseSinuses = payload.get("noseSinuses").toString();
					noseSinuses = OpdServiceImpl.getReplaceString(noseSinuses);
					if (StringUtils.isNotEmpty(noseSinuses))
						masMedicalExamReprt.setNose(noseSinuses);
				}
				if (payload.get("throatSinuses") != null && !payload.get("throatSinuses").equals("")) {
					String throatSinuses = payload.get("throatSinuses").toString();
					throatSinuses = OpdServiceImpl.getReplaceString(throatSinuses);
					if (StringUtils.isNotEmpty(throatSinuses))
						masMedicalExamReprt.setNosethroat(throatSinuses);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:method is used for investigationData
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*
	public MasMedicalExamReport investigationData(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {

		if (payload.get("clinicalNotes") != null) {
			String clinicalNotes = payload.get("clinicalNotes").toString();
			clinicalNotes = OpdServiceImpl.getReplaceString(clinicalNotes);
			masMedicalExamReprt.setRemarksClinical(clinicalNotes);
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for cardioVascularSystem
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*
	public static MasMedicalExamReport cardioVascularSystem(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		try {
		if (payload.get("pulse") != null && !payload.get("pulse").equals("")) {
			String pulse = payload.get("pulse").toString();
			pulse = OpdServiceImpl.getReplaceString(pulse);
			masMedicalExamReprt.setPulseRates(pulse);
		}
		if (payload.get("bp") != null) {
			String bp = payload.get("bp").toString();
			bp = OpdServiceImpl.getReplaceString(bp);
			masMedicalExamReprt.setBpSystolic(bp);

		}

		if (payload.get("bp1") != null) {
			String bp1 = payload.get("bp1").toString();
			bp1 = OpdServiceImpl.getReplaceString(bp1);
			masMedicalExamReprt.setBpDiastolic(bp1);

		}
		if (payload.get("peripheralPulsations") != null) {
			String peripheralPulsations = payload.get("peripheralPulsations").toString();
			peripheralPulsations = OpdServiceImpl.getReplaceString(peripheralPulsations);
			masMedicalExamReprt.setPeripheralPulsations(peripheralPulsations);
		}

		if (payload.get("heartSize") != null) {
			String heartSize = payload.get("heartSize").toString();
			heartSize = OpdServiceImpl.getReplaceString(heartSize);
			masMedicalExamReprt.setHeartSize(heartSize);
		}
		if (payload.get("sounds") != null) {
			String sounds = payload.get("sounds").toString();
			sounds = OpdServiceImpl.getReplaceString(sounds);
			masMedicalExamReprt.setSounds(sounds);
		}
		if (payload.get("rhythm") != null) {
			String rhythm = payload.get("rhythm").toString();
			rhythm = OpdServiceImpl.getReplaceString(rhythm);
			masMedicalExamReprt.setRhythm(rhythm);
		}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for respiratoryAndGastroIntestinalSystem
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*
	public static MasMedicalExamReport respiratoryAndGastroIntestinalSystem(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		try {
			if (payload.get("respiratorySystem") != null) {
				String respiratorySystem = payload.get("respiratorySystem").toString();
				respiratorySystem = OpdServiceImpl.getReplaceString(respiratorySystem);
				masMedicalExamReprt.setRespiratorySystem(respiratorySystem);
			}
			// Gastro Intestinal System

			String liverPalpable = payload.get("liverPalpable").toString();
			liverPalpable = OpdServiceImpl.getReplaceString(liverPalpable);
			if (StringUtils.isNotEmpty(liverPalpable) && liverPalpable.equalsIgnoreCase("Yes")) {
				if (payload.get("liver") != null) {
					String liver = payload.get("liver").toString();
					liver =OpdServiceImpl.getReplaceString(liver);
					if(StringUtils.isNotEmpty(liver)) {
					masMedicalExamReprt.setLiver(Double.parseDouble(liver));
					}
					else {
						masMedicalExamReprt.setLiver(-1d);
					}
				}
				else {
					masMedicalExamReprt.setLiver(-1d);
				}
			} else {
				masMedicalExamReprt.setLiver(null);
			}

			String spleenPalpable = payload.get("spleenPalpable").toString();
			spleenPalpable = OpdServiceImpl.getReplaceString(spleenPalpable);
			if (StringUtils.isNotEmpty(spleenPalpable) && spleenPalpable.equalsIgnoreCase("Yes")) {
				if (payload.get("spleen") != null) {
					String spleen = payload.get("spleen").toString();
					spleen =OpdServiceImpl.getReplaceString(spleen);
					if(StringUtils.isNotEmpty(spleen)) {
					masMedicalExamReprt.setSpleen(Double.parseDouble(spleen));
				}
					else {
						masMedicalExamReprt.setSpleen(-1d);
					}	
				}
				else {
					masMedicalExamReprt.setSpleen(-1d);
				}
			} else {
				masMedicalExamReprt.setSpleen(null);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for centralNervousSystemAndOthers
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*
	public static MasMedicalExamReport centralNervousSystemAndOthers(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		// Central Nervous System
		try {
			if (payload.get("higherMentalFunction") != null) {
				String higherMentalFunction = payload.get("higherMentalFunction").toString();
				higherMentalFunction = OpdServiceImpl.getReplaceString(higherMentalFunction);
				masMedicalExamReprt.setHigherMentalFunction(higherMentalFunction);
			}
			if (payload.get("speech") != null) {
				String speech = payload.get("speech").toString();
				speech = OpdServiceImpl.getReplaceString(speech);
				masMedicalExamReprt.setSpeech(speech);
			}

			if (payload.get("reflexes") != null) {
				String reflexes = payload.get("reflexes").toString();
				reflexes = OpdServiceImpl.getReplaceString(reflexes);
				masMedicalExamReprt.setReflexes(reflexes);
			}
			if (payload.get("tremors") != null) {
				String tremors = payload.get("tremors").toString();
				tremors = OpdServiceImpl.getReplaceString(tremors);
				masMedicalExamReprt.setTremors(tremors);
			}
			if (payload.get("selfBalancingTest") != null) {
				String selfBalancingTest = payload.get("selfBalancingTest").toString();
				selfBalancingTest = OpdServiceImpl.getReplaceString(selfBalancingTest);
				masMedicalExamReprt.setSelfBalancingTest(selfBalancingTest);
			}

			// Others

			if (payload.get("locomotorSystem") != null) {
				String locomotorSystem = payload.get("locomotorSystem").toString();
				locomotorSystem = OpdServiceImpl.getReplaceString(locomotorSystem);
				masMedicalExamReprt.setLocomoterSystem(locomotorSystem);
			}
			if (payload.get("spine") != null) {
				String spine = payload.get("spine").toString();
				spine = OpdServiceImpl.getReplaceString(spine);
				masMedicalExamReprt.setSpine(spine);
			}

			if (payload.get("hernia") != null) {
				String hernia = payload.get("hernia").toString();
				hernia = OpdServiceImpl.getReplaceString(hernia);
				masMedicalExamReprt.setHerniaMusic(hernia);
			}

			if (payload.get("hydrocele") != null) {
				String hydrocele = payload.get("hydrocele").toString();
				hydrocele = OpdServiceImpl.getReplaceString(hydrocele);
				masMedicalExamReprt.setHydrocele(hydrocele);
			}
			if (payload.get("haemorrhoids") != null) {
				String haemorrhoids = payload.get("haemorrhoids").toString();
				haemorrhoids = OpdServiceImpl.getReplaceString(haemorrhoids);
				masMedicalExamReprt.setHemorrhoids(haemorrhoids);
			}

			String age = payload.get("age").toString();
			age = OpdServiceImpl.getReplaceString(age);
			String gender = "";
			try {
			if (StringUtils.isNotEmpty(age)) {
				String[] ageMale = age.split("/");
				gender = ageMale[1];
			}
			}
			catch(Exception e) {e.printStackTrace();}
			if (StringUtils.isNotEmpty(gender) && gender.equalsIgnoreCase("FEMALE")) {
				if (payload.get("breast") != null) {
					String breast = payload.get("breast").toString();
					breast = OpdServiceImpl.getReplaceString(breast);
					masMedicalExamReprt.setBreasts(breast);
				}
				masMedicalExamReprt = getGynaecologicalExam(payload, masMedicalExamReprt);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for getGynaecologicalExam
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*

	public static MasMedicalExamReport getGynaecologicalExam(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		try {

			String mensturalHistory = payload.get("mensturalHistory").toString();
			mensturalHistory = OpdServiceImpl.getReplaceString(mensturalHistory);
			masMedicalExamReprt.setMenstrualHistory(mensturalHistory);

			String lmpSelect = payload.get("lmpSelect").toString();
			lmpSelect = OpdServiceImpl.getReplaceString(lmpSelect);
			masMedicalExamReprt.setLmpStatus(lmpSelect);

			String lMP = payload.get("lMP").toString();
			lMP = OpdServiceImpl.getReplaceString(lMP);
			if (StringUtils.isNotEmpty(lMP) && !lMP.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(lMP)) {
				Date lMPDate = HMSUtil.convertStringTypeDateToDateType(lMP.trim());
				if(lMPDate!=null)
				masMedicalExamReprt.setLmp(lMPDate);
			}

			String nosOfPregnancies = payload.get("nosOfPregnancies").toString();
			nosOfPregnancies = OpdServiceImpl.getReplaceString(nosOfPregnancies);
			if(StringUtils.isNotEmpty(nosOfPregnancies))
			masMedicalExamReprt.setNoOfPregnancies(Long.parseLong(nosOfPregnancies));

			String nosOfAbortions = payload.get("nosOfAbortions").toString();
			nosOfAbortions = OpdServiceImpl.getReplaceString(nosOfAbortions);
			if(StringUtils.isNotEmpty(nosOfAbortions))
			masMedicalExamReprt.setNoOfAbortions(Long.parseLong(nosOfAbortions));

			String nosOfChildren = payload.get("nosOfChildren").toString();
			nosOfChildren = OpdServiceImpl.getReplaceString(nosOfChildren);
			if(StringUtils.isNotEmpty(nosOfChildren))
			masMedicalExamReprt.setNoOfChildren(Long.parseLong(nosOfChildren));

			String childDateOfLastConfinement = payload.get("childDateOfLastConfinement").toString();
			childDateOfLastConfinement = OpdServiceImpl.getReplaceString(childDateOfLastConfinement);
			if (StringUtils.isNotEmpty(childDateOfLastConfinement) && !childDateOfLastConfinement.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(childDateOfLastConfinement)) {
				Date lastConfinementDate = HMSUtil.convertStringTypeDateToDateType(childDateOfLastConfinement.trim());
				if(lastConfinementDate!=null)
				masMedicalExamReprt.setLastConfinementDate(lastConfinementDate);
			}

			String vaginalDischarge = payload.get("vaginalDischarge").toString();
			vaginalDischarge = OpdServiceImpl.getReplaceString(vaginalDischarge);
			masMedicalExamReprt.setVaginalDischarge(vaginalDischarge);

			// Discuss with field
			
			 * String vaginalDateOfLastConfinement =
			 * payload.get("vaginalDateOfLastConfinement").toString();
			 * vaginalDateOfLastConfinement =
			 * OpdServiceImpl.getReplaceString(vaginalDateOfLastConfinement);
			 * masMedicalExamReprt.setDateof
			 

			String usgAbdomen = payload.get("usgAbdomen").toString();
			usgAbdomen = OpdServiceImpl.getReplaceString(usgAbdomen);
			masMedicalExamReprt.setUsgAbdomen(usgAbdomen);

			String prolapse = payload.get("prolapse").toString();
			prolapse = OpdServiceImpl.getReplaceString(prolapse);
			masMedicalExamReprt.setProlapse(prolapse);

		} catch (Exception e) {
		
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}

	*//**
	 * @Description:Method is used for lifeStyleFactors
	 * @param payload
	 * @param masMedicalExamReprt
	 * @return
	 *//*

	public MasMedicalExamReport lifeStyleFactors(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {

		// Life Style Factors
		if (payload.get("coronaryRiskFactors") != null) {
			String coronaryRiskFactors = payload.get("coronaryRiskFactors").toString();
			coronaryRiskFactors = OpdServiceImpl.getReplaceString(coronaryRiskFactors);
			masMedicalExamReprt.setCoronoryRiskFactor(coronaryRiskFactors);
		}
		if (payload.get("familyHistory") != null) {
			String familyHistory = payload.get("familyHistory").toString();
			familyHistory = OpdServiceImpl.getReplaceString(familyHistory);
			masMedicalExamReprt.setRelevantFamilyHistory(familyHistory);
		}
		if (payload.get("smokerCheckedLt10") != null) {
			String smokerCheckedLt10 = payload.get("smokerCheckedLt10").toString();

			smokerCheckedLt10 = OpdServiceImpl.getReplaceString(smokerCheckedLt10);
			if (StringUtils.isNotBlank(smokerCheckedLt10) && smokerCheckedLt10.equalsIgnoreCase("< 10")) {
				masMedicalExamReprt.setSmoker("9");
			}
		}

		if (payload.get("smokerCheckedGt10") != null) {
			String smokerCheckedGt10 = payload.get("smokerCheckedGt10").toString();

			smokerCheckedGt10 = OpdServiceImpl.getReplaceString(smokerCheckedGt10);
			if (StringUtils.isNotBlank(smokerCheckedGt10) && smokerCheckedGt10.equalsIgnoreCase("> 10")) {
				masMedicalExamReprt.setSmoker("10");
			}
		}

		if (payload.get("alcohol") != null) {
			String alcohol = payload.get("alcohol").toString();
			alcohol = OpdServiceImpl.getReplaceString(alcohol);
			masMedicalExamReprt.setAlcoholDrugRelated(alcohol);
		}
		if (payload.get("adviceToReduceWeight") != null) {
			String adviceToReduceWeight = payload.get("adviceToReduceWeight").toString();
			adviceToReduceWeight = OpdServiceImpl.getReplaceString(adviceToReduceWeight);
			masMedicalExamReprt.setRegularExercise(adviceToReduceWeight);
		}
		if (payload.get("allergy") != null) {
			String allergy = payload.get("allergy").toString();
			allergy = OpdServiceImpl.getReplaceString(allergy);
			masMedicalExamReprt.setAllergies(allergy);
		}
		if (payload.get("remarkAdvice") != null) {
			String remarkAdvice = payload.get("remarkAdvice").toString();
			remarkAdvice = OpdServiceImpl.getReplaceString(remarkAdvice);
			masMedicalExamReprt.setCommandRemarks(remarkAdvice);
		}

		return masMedicalExamReprt;
	}

	*//**
	 * @DEscription:Method is used getAFMSF3BForMOOrMA
	 * @param: HashMap<String,
	 *             Object> jsondata,
	 * @param: HttpServletRequest
	 *             request
	 * @param: HttpServletResponse
	 *             response
	 *//*

	@Override
	public String getAFMSF3BForMOOrMA(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		String rankEmployee="";
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		try {
			MasMedicalExamReport masMedicalExamReprt=null;
			if(jsondata.get("visitId")!=null && jsondata.get("visitId")!="")
			 masMedicalExamReprt = medicalExamDAO
					.getMasMedicalExamReprtByVisitId(Long.parseLong(jsondata.get("visitId").toString()));
			
			List<MasServiceType>listMasServiceType=masterDao.getServiceTypeList();
			
			Users users = medicalExamDAO.getUserByUserId(Long.parseLong(jsondata.get("userId").toString()));
			OpdObesityHd	opdObesityHd = opdPatientDetailDao
					.getOpdObesityHd(Long.parseLong(jsondata.get("visitId").toString()));
			
			if (masMedicalExamReprt != null) {
				HashMap<String, Object> jsonMap = new HashMap<String, Object>();

				jsonMap.put("totalNoOfTeath", masMedicalExamReprt.getTotalTeeth());
				jsonMap.put("visitId", masMedicalExamReprt.getVisitId());
				jsonMap.put("patientId", masMedicalExamReprt.getPatientId());

				jsonMap.put("totalNoOfDefective", masMedicalExamReprt.getTotalDefectiveTeeth());
				jsonMap.put("totalNoOfDentalPoints", masMedicalExamReprt.getTotalNoDentalPoint());
				jsonMap.put("missing", masMedicalExamReprt.getMissingTeeth());
				jsonMap.put("unSavable", masMedicalExamReprt.getUnsaveableTeeth());
				jsonMap.put("conditionOfGums", masMedicalExamReprt.getConditionofgums());

				jsonMap.put("missingTeethUr", masMedicalExamReprt.getMTUr());
				jsonMap.put("missingTeethUl", masMedicalExamReprt.getMTUl());
				jsonMap.put("missingTeethLL", masMedicalExamReprt.getMTLl());
				jsonMap.put("missingTeethLR", masMedicalExamReprt.getMTLr());

				jsonMap.put("unsavableTeethUr", masMedicalExamReprt.getUTUr());
				jsonMap.put("unsavableTeethUl", masMedicalExamReprt.getUTUl());
				jsonMap.put("unsavableTeethLl", masMedicalExamReprt.getUTLl());
				jsonMap.put("unsavableTeethLr", masMedicalExamReprt.getUTLr());

				jsonMap.put("pcHeight", masMedicalExamReprt.getHeight());
				jsonMap.put("pcWeight", masMedicalExamReprt.getWeight());
				jsonMap.put("pcIdealWeight", masMedicalExamReprt.getIdealweight());

				
				 * String variation=""; if(masMedicalExamReprt.getOverweight()!=null) { if
				 * (Long.parseLong(masMedicalExamReprt.getOverweight()) > 0) { variation =
				 * "+"+masMedicalExamReprt.getOverweight(); }else { variation =
				 * masMedicalExamReprt.getOverweight()+""; } }
				 

				jsonMap.put("pcOverWeight", masMedicalExamReprt.getOverweight());

				jsonMap.put("pcBmi", masMedicalExamReprt.getBhi());
				jsonMap.put("pcBodyFat", masMedicalExamReprt.getBodyfat());
				jsonMap.put("pcWaist", masMedicalExamReprt.getWaist());
				jsonMap.put("pcHip", masMedicalExamReprt.getHips());

				jsonMap.put("pcWhr", masMedicalExamReprt.getWhr());
				jsonMap.put("pcSkinFoldExpansion", masMedicalExamReprt.getSkin());
				jsonMap.put("pcChestFullExpansion", masMedicalExamReprt.getChestfullexpansion());
				jsonMap.put("pcRangeOfExpansion", masMedicalExamReprt.getRangeofexpansion());
				jsonMap.put("pcSportsman", masMedicalExamReprt.getSportman());
				jsonMap.put("fwR", masMedicalExamReprt.getEarHearingRfw());
				jsonMap.put("fwL", masMedicalExamReprt.getEarHearingLfw());
				jsonMap.put("fwBoth", masMedicalExamReprt.getEarHearingBothFw());

				jsonMap.put("cvR", masMedicalExamReprt.getHearingRcv());
				jsonMap.put("cvL", masMedicalExamReprt.getHearingLcv());
				jsonMap.put("cvBoth", masMedicalExamReprt.getHearingBothCv());
				jsonMap.put("tmR", masMedicalExamReprt.getTympanicr());
				jsonMap.put("tmL", masMedicalExamReprt.getTympanicl());
				jsonMap.put("mobilityR", masMedicalExamReprt.getMobilityr());
				jsonMap.put("mobilityL", masMedicalExamReprt.getMobilityl());
				jsonMap.put("noiseThroatSinuses", masMedicalExamReprt.getNoseThroatSinuses());
				jsonMap.put("audiometryRecord", masMedicalExamReprt.getAudiometryRecord());

				jsonMap.put("clinicalNotes", masMedicalExamReprt.getRemarksClinical());
				// jsonMap.put("cvL",masMedicalExamReprt.getHearingLcv());
				// jsonMap.put("cvBoth",masMedicalExamReprt.getHearingBothCv());
				// jsonMap.put("tmR",masMedicalExamReprt.getHear);
				// jsonMap.put("mobilityR",masMedicalExamReprt.getMobilityr());
				// jsonMap.put("mobilityL",masMedicalExamReprt.getMobilityl());
				// jsonMap.put("noiseThroatSinuses",masMedicalExamReprt.getNoseThroatSinuses());
				// jsonMap.put("audiometryRecord",masMedicalExamReprt.getAudiometryRecord());

				jsonMap.put("pluse", masMedicalExamReprt.getPulseRates());
				jsonMap.put("bp", masMedicalExamReprt.getBpSystolic());
				jsonMap.put("bp1", masMedicalExamReprt.getBpDiastolic());
				jsonMap.put("heartSize", masMedicalExamReprt.getHeartSize());
				jsonMap.put("sounds", masMedicalExamReprt.getSounds());
				jsonMap.put("rhythm", masMedicalExamReprt.getRhythm());
				jsonMap.put("respiratorySystem", masMedicalExamReprt.getRespiratorySystem());
				jsonMap.put("liver", masMedicalExamReprt.getLiver());
				jsonMap.put("spleen", masMedicalExamReprt.getSpleen());

				jsonMap.put("higherMentalFunction", masMedicalExamReprt.getHigherMentalFunction());
				jsonMap.put("speech", masMedicalExamReprt.getSpeech());
				jsonMap.put("reflexes", masMedicalExamReprt.getReflexes());
				jsonMap.put("tremors", masMedicalExamReprt.getTremors());
				jsonMap.put("selfBalancingTest", masMedicalExamReprt.getSelfBalancingTest());
				jsonMap.put("locomotorSystem", masMedicalExamReprt.getLocomoterSystem());
				jsonMap.put("spine", masMedicalExamReprt.getSpine());
				jsonMap.put("hernia", masMedicalExamReprt.getHerniaMusic());

				jsonMap.put("hydrocele", masMedicalExamReprt.getHydrocele());
				jsonMap.put("haemorrhoids", masMedicalExamReprt.getHemorrhoids());
				jsonMap.put("breast", masMedicalExamReprt.getBreasts());
				jsonMap.put("coronaryRiskFactors", masMedicalExamReprt.getCoronoryRiskFactor());
				jsonMap.put("familyHistory", masMedicalExamReprt.getRelevantFamilyHistory());
				jsonMap.put("smokerCheckedLt10", masMedicalExamReprt.getSmoker());
				jsonMap.put("alcohol", masMedicalExamReprt.getAlcoholDrugRelated());
				jsonMap.put("allergy", masMedicalExamReprt.getAllergies());

				// jsonMap.put("adviceToReduceWeight",masMedicalExamReprt.getRed);
				jsonMap.put("remarks", masMedicalExamReprt.getRemarks());
				jsonMap.put("remarkAdvice", masMedicalExamReprt.getCommandRemarks());
				jsonMap.put("dentalOffier", masMedicalExamReprt.getDentalOfficer());
				String checkupDate = "";
				if (masMedicalExamReprt.getDentalCheckupDate() != null) {
					checkupDate = HMSUtil.convertDateToStringFormat(masMedicalExamReprt.getDentalCheckupDate(),
							"dd/MM/yyyy");
				}

				jsonMap.put("checkupDate", checkupDate);

				String dateOfPatient = "";
				if (masMedicalExamReprt!=null &&  masMedicalExamReprt.getDateofcommun() != null) {
					dateOfPatient = HMSUtil.convertDateToStringFormat(masMedicalExamReprt.getDateofcommun(),
							"dd/MM/yyyy");
				}

				jsonMap.put("dateOfPatient", dateOfPatient);
				
				jsonMap.put("distantWithoutGlasses", masMedicalExamReprt.getWthoutGlassesRDistant());
				jsonMap.put("distantWithoutGlassesL", masMedicalExamReprt.getWithoutGlassesLDistant());

				jsonMap.put("nearWithoutGlasses", masMedicalExamReprt.getWithoutGlassesRNearvision());
				jsonMap.put("nearWithoutGlassesL", masMedicalExamReprt.getWithoutGlassesLNearvision());

				jsonMap.put("cpWithoutGlasses", masMedicalExamReprt.getNearVisionWithoutGlassCp());

				jsonMap.put("distantWithGlasses", masMedicalExamReprt.getWithGlassesRDistant());
				jsonMap.put("distantWithGlassesL", masMedicalExamReprt.getWithGlassesLDistant());

				jsonMap.put("nearWithGlasses", masMedicalExamReprt.getWithGlassesRNearvision());
				jsonMap.put("nearWithGlassesL", masMedicalExamReprt.getWithGlassesLNearvision());

				if (masMedicalExamReprt.getVisit().getOpdPatientDetails() != null) {
					jsonMap.put("opdPatientDetailId",
							masMedicalExamReprt.getVisit().getOpdPatientDetails().getOpdPatientDetailsId());
				}
				jsonMap.put("chiefComplaint", masMedicalExamReprt.getChiefComplaint());
				jsonMap.put("pollar", masMedicalExamReprt.getPollor());

				jsonMap.put("ordema", masMedicalExamReprt.getEdema());
				jsonMap.put("cyanosis", masMedicalExamReprt.getCyanosis());

				jsonMap.put("hairnail", masMedicalExamReprt.getHairNail());
				jsonMap.put("icterus", masMedicalExamReprt.getIcterus());

				jsonMap.put("lymphNode", masMedicalExamReprt.getLymphNode());
				jsonMap.put("clubbing", masMedicalExamReprt.getClubbing());

				jsonMap.put("gcs", masMedicalExamReprt.getGcs());
				
				jsonMap.put("tremors", masMedicalExamReprt.getTremors());
				
				jsonMap.put("geTremors", masMedicalExamReprt.getGeTremors());
				jsonMap.put("others", masMedicalExamReprt.getOthers());
				jsonMap.put("remarksForReferal", masMedicalExamReprt.getRemarksRef());
				jsonMap.put("remarksReject", masMedicalExamReprt.getRemarksRej());
				jsonMap.put("remarksPending", masMedicalExamReprt.getRemarksPending());

				String appoinmentDateOfExam = "";
				if (masMedicalExamReprt.getAppointmentDate() != null) {
					appoinmentDateOfExam = HMSUtil.convertDateToStringFormat(masMedicalExamReprt.getAppointmentDate(),
							"dd/MM/yyyy");
				}
				jsonMap.put("nextAppointmentDate", appoinmentDateOfExam);

				jsonMap.put("remarksForApproval", masMedicalExamReprt.getRemarksForward());

				jsonMap.put("status", masMedicalExamReprt.getStatus());
				jsonMap.put("finalObservationMo", masMedicalExamReprt.getFinalobservation());
				jsonMap.put("finalObservationRmo", masMedicalExamReprt.getAaFinalObservation());
				jsonMap.put("remarksRmo", masMedicalExamReprt.getAaRemarks());
				jsonMap.put("paFinalobservation", masMedicalExamReprt.getPaFinalobservation());
				jsonMap.put("remarksPRmo", masMedicalExamReprt.getPaRemarks());
				jsonMap.put("petStatus", masMedicalExamReprt.getPetStatusOn());
				jsonMap.put("peripheralPulsations", masMedicalExamReprt.getPeripheralPulsations());
				String dateOfExam = "";
				if (masMedicalExamReprt.getPetDate() != null) {
					dateOfExam = HMSUtil.convertDateToStringFormat(masMedicalExamReprt.getPetDate(), "dd/MM/yyyy");
				}

				jsonMap.put("petDate", dateOfExam);

				jsonMap.put("regularExercise", masMedicalExamReprt.getRegularExercise());

				String maUserDetail = "";
				if (masMedicalExamReprt.getUsersMA() != null
						&& masMedicalExamReprt.getUsersMA().getFirstName() != null) {
					maUserDetail = masMedicalExamReprt.getUsersMA().getFirstName();
					rankEmployee=getRankOfUserId(masMedicalExamReprt.getUsersMA());

				}
				if (masMedicalExamReprt.getUsersMA() != null
						&& masMedicalExamReprt.getUsersMA().getLastName() != null) {
					maUserDetail += "" + masMedicalExamReprt.getUsersMA().getLastName();
				}

				if (masMedicalExamReprt.getUsersMA() != null) {
				
					if(StringUtils.isNotEmpty(rankEmployee)) {
						maUserDetail+=" "+rankEmployee;
					}
					jsonMap.put("maUser", maUserDetail);
				
				}
				else {
					if (users.getFirstName() != null) {
						maUserDetail = users.getFirstName();
						rankEmployee=getRankOfUserId(users);

					}
					if (users.getLastName() != null) {
						maUserDetail += "" + users.getLastName();
					}
					if(StringUtils.isNotEmpty(rankEmployee)) {
						maUserDetail+=" "+rankEmployee;
					}
					jsonMap.put("maUser", maUserDetail);
				}

				String moUserDetail = "";
				if (masMedicalExamReprt.getUsersMO() != null
						&& masMedicalExamReprt.getUsersMO().getFirstName() != null) {
					moUserDetail = masMedicalExamReprt.getUsersMO().getFirstName();
				}
				if (masMedicalExamReprt.getUsersMO() != null
						&& masMedicalExamReprt.getUsersMO().getLastName() != null) {
					moUserDetail += "" + masMedicalExamReprt.getUsersMO().getLastName();
				}

				if (masMedicalExamReprt.getUsersMO() != null) {
					rankEmployee=getRankOfUserId(masMedicalExamReprt.getUsersMO());
					if(StringUtils.isNotEmpty(rankEmployee)) {
						//moUserDetail+=" "+rankEmployee;
						moUserDetail=rankEmployee+" "+ moUserDetail;
					}
					jsonMap.put("moUser", moUserDetail);
				} else {
					if (users.getFirstName() != null) {
						moUserDetail = users.getFirstName();

					}
					if (users.getLastName() != null) {
						moUserDetail += "" + users.getLastName();
					}
					rankEmployee=getRankOfUserId(users);
					if(StringUtils.isNotEmpty(rankEmployee)) {
						//moUserDetail+=" "+rankEmployee;
						moUserDetail=rankEmployee+" "+ moUserDetail;
					}
					jsonMap.put("moUser", moUserDetail);
				}

				String rmoUserDetail = "";
				if (masMedicalExamReprt.getUsersCM() != null
						&& masMedicalExamReprt.getUsersCM().getFirstName() != null) {
					rmoUserDetail = masMedicalExamReprt.getUsersCM().getFirstName();
					rankEmployee=getRankOfUserId(masMedicalExamReprt.getUsersCM());
				}
				if (masMedicalExamReprt.getUsersCM() != null
						&& masMedicalExamReprt.getUsersCM().getLastName() != null) {
					rmoUserDetail += "" + masMedicalExamReprt.getUsersCM().getLastName();
					
				}

				if (masMedicalExamReprt.getUsersCM() != null) {
					if(StringUtils.isNotEmpty(rankEmployee)) {
						//rmoUserDetail+=" "+rankEmployee;
						rmoUserDetail=rankEmployee+" "+rmoUserDetail;
					}
					jsonMap.put("rMoUser", rmoUserDetail);
				} else {

					if (users.getFirstName() != null) {
						rmoUserDetail = users.getFirstName();

					}
					if (users.getLastName() != null) {
						rmoUserDetail += "" + users.getLastName();
					}
					rankEmployee=getRankOfUserId(users);
					 	
					if(StringUtils.isNotEmpty(rankEmployee)) {
						//rmoUserDetail+=" "+rankEmployee;
						rmoUserDetail=rankEmployee+" "+rmoUserDetail;
					}
					jsonMap.put("rMoUser", rmoUserDetail);
				}
				
				
				String prmoUserDetail = "";
				if (masMedicalExamReprt.getUsersMd() != null
						&& masMedicalExamReprt.getUsersMd().getFirstName() != null) 
				{
					prmoUserDetail = masMedicalExamReprt.getUsersMd().getFirstName();
				}
				if (masMedicalExamReprt.getUsersMd() != null
						&& masMedicalExamReprt.getUsersMd().getLastName() != null) {
					prmoUserDetail += "" + masMedicalExamReprt.getUsersMd().getLastName();
				}
				

				if (masMedicalExamReprt.getUsersMd() != null) {
					rankEmployee=getRankOfUserId(masMedicalExamReprt.getUsersMd());
					if(StringUtils.isNotEmpty(rankEmployee)) {
						//prmoUserDetail+=" "+rankEmployee;
						prmoUserDetail=rankEmployee +" "+prmoUserDetail;
					}
					jsonMap.put("pRMoUser", prmoUserDetail);
				} else {
					if (users.getFirstName() != null) {
						prmoUserDetail = users.getFirstName();

					}
					if (users.getLastName() != null) {
						prmoUserDetail += "" + users.getLastName();
					}
					rankEmployee=getRankOfUserId(users);
					if(StringUtils.isNotEmpty(rankEmployee)) {
						//prmoUserDetail+=" "+rankEmployee;
						prmoUserDetail=rankEmployee +" "+prmoUserDetail;
					}
					jsonMap.put("pRMoUser", prmoUserDetail);
				}

				if (masMedicalExamReprt.getForwardUnitId() != null) {
					jsonMap.put("hospitalIdFoward", masMedicalExamReprt.getForwardUnitId());
				} else {

					jsonMap.put("hospitalIdFoward", "");
				}

				if (masMedicalExamReprt.getApprovedBy() != null) {
					jsonMap.put("approvedBy", masMedicalExamReprt.getApprovedBy());
				} else {
					jsonMap.put("approvedBy", "");
				}

				// gynaologist detail
				if (masMedicalExamReprt.getMenstrualHistory() != null) {
					jsonMap.put("mensturalHistory", masMedicalExamReprt.getMenstrualHistory());
				} else {
					jsonMap.put("mensturalHistory", "");
				}

				if (masMedicalExamReprt.getLmpStatus() != null) {
					jsonMap.put("lmpSelect", masMedicalExamReprt.getLmpStatus());
				} else {
					jsonMap.put("lmpSelect", "");
				}

				String lMPDate = "";
				if (masMedicalExamReprt.getLmp() != null) {
					lMPDate = HMSUtil.convertDateToStringFormat(masMedicalExamReprt.getLmp(), "dd/MM/yyyy");

				}
				jsonMap.put("lMP", lMPDate);

				if (masMedicalExamReprt.getNoOfPregnancies() != null) {
					jsonMap.put("nosOfPregnancies", masMedicalExamReprt.getNoOfPregnancies());
				} else {
					jsonMap.put("nosOfPregnancies", "");
				}

				if (masMedicalExamReprt.getNoOfAbortions() != null) {
					jsonMap.put("nosOfAbortions", masMedicalExamReprt.getNoOfAbortions());
				} else {
					jsonMap.put("nosOfAbortions", "");
				}

				if (masMedicalExamReprt.getNoOfChildren() != null) {
					jsonMap.put("nosOfChildren", masMedicalExamReprt.getNoOfChildren());
				} else {
					jsonMap.put("nosOfChildren", "");
				}

				String childDateOfLastConfinementDate = "";
				if (masMedicalExamReprt.getDentalCheckupDate() != null) {
					childDateOfLastConfinementDate = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getLastConfinementDate(), "dd/MM/yyyy");
				}
				jsonMap.put("childDateOfLastConfinement", childDateOfLastConfinementDate);

				if (masMedicalExamReprt.getVaginalDischarge() != null) {
					jsonMap.put("vaginalDischarge", masMedicalExamReprt.getVaginalDischarge());
				} else {
					jsonMap.put("vaginalDischarge", "");
				}

				if (masMedicalExamReprt.getUsgAbdomen() != null) {
					jsonMap.put("usgAbdomen", masMedicalExamReprt.getUsgAbdomen());
				} else {
					jsonMap.put("usgAbdomen", "");
				}

				if (masMedicalExamReprt.getProlapse() != null) {
					jsonMap.put("prolapse", masMedicalExamReprt.getProlapse());
				} else {
					jsonMap.put("prolapse", "");
				}

				if (masMedicalExamReprt.getAudiometryRecordOthers() != null) {
					jsonMap.put("audiometryRecordForOther", masMedicalExamReprt.getAudiometryRecordOthers());
				} else {
					jsonMap.put("audiometryRecordForOther", "");
				}
				
				
				
				/////////////////////////////Changes of 3A Form//////////////////////////////////////
				if (masMedicalExamReprt.getEcgRDMT() != null) {
					jsonMap.put("ecgRDMT",masMedicalExamReprt.getEcgRDMT());
				} else {
					jsonMap.put("ecgRDMT", "");
				}
				
				if (masMedicalExamReprt.getEcgDated() != null) {
					String ecgDated = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getEcgDated(), "dd/MM/yyyy");
					
					jsonMap.put("ecgDated",ecgDated);
				} else {
					jsonMap.put("ecgDated", "");
				}
				
				if (masMedicalExamReprt.getEcgReport() != null) {
					jsonMap.put("ecgReport",masMedicalExamReprt.getEcgReport());
				} else {
					jsonMap.put("ecgReport", "");
				}
				
				
				if (masMedicalExamReprt.getEcgAMT() != null) {
					jsonMap.put("ecgAMT",masMedicalExamReprt.getEcgAMT());
				} else {
					jsonMap.put("ecgAMT", "");
				}
				
				
				
				if (masMedicalExamReprt.getEcgAmtDated() != null) {
					String ecgAmtDated = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getEcgAmtDated(), "dd/MM/yyyy");
					jsonMap.put("ecgAmtDated",ecgAmtDated);
				} else {
					jsonMap.put("ecgAmtDated", "");
				}
				
				
				
				if (masMedicalExamReprt.getEcgAmtReport() != null) {
					jsonMap.put("ecgAmtReport",masMedicalExamReprt.getEcgAmtReport());
				} else {
					jsonMap.put("ecgAmtReport", "");
				}
				
				
				
				if (masMedicalExamReprt.getxRayChestPANos() != null) {
					jsonMap.put("xRayChestPANos",masMedicalExamReprt.getxRayChestPANos());
				} else {
					jsonMap.put("xRayChestPANos", "");
				}
				
				
				if (masMedicalExamReprt.getxRayChestPANosDated() != null) {
					String xRayChestPANosDatedV = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getxRayChestPANosDated(), "dd/MM/yyyy");
					jsonMap.put("xRayChestPANosDated",xRayChestPANosDatedV);
				} else {
					jsonMap.put("xRayChestPANosDated", "");
				}
				
				
				if (masMedicalExamReprt.getxRayChestPANosReport() != null) {
					jsonMap.put("xRayChestPANosReport",masMedicalExamReprt.getxRayChestPANosReport());
				} else {
					jsonMap.put("xRayChestPANosReport", "");
				}
				
				
				if (masMedicalExamReprt.getRemarksOfLab() != null) {
					jsonMap.put("remarksOfLab",masMedicalExamReprt.getRemarksOfLab());
				} else {
					jsonMap.put("remarksOfLab", "");
				}
				
				
				
				if (masMedicalExamReprt.getDateOfLab() != null) {
					
					String dateOfLab = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getDateOfLab(), "dd/MM/yyyy");
					
					jsonMap.put("dateOfLab",dateOfLab);
				} else {
					jsonMap.put("dateOfLab", "");
				}
				
				
				if (masMedicalExamReprt.getSignatureOfEyeSpecialist() != null) {
					jsonMap.put("signatureOfEyeSpecialist",masMedicalExamReprt.getSignatureOfEyeSpecialist());
				} else {
					jsonMap.put("signatureOfEyeSpecialist", "");
				}
				
				
				if (masMedicalExamReprt.getRemarksOfSurgery() != null) {
					jsonMap.put("remarksOfSurgery",masMedicalExamReprt.getRemarksOfSurgery());
				} else {
					jsonMap.put("remarksOfSurgery", "");
				}
				
				if (masMedicalExamReprt.getDateOfSurgery() != null) {
					
					String dateOfSurgery = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getDateOfSurgery(), "dd/MM/yyyy");
					jsonMap.put("dateOfSurgery",dateOfSurgery);
				} else {
					jsonMap.put("dateOfSurgery", "");
				}
				
				
				
				if (masMedicalExamReprt.getSignatureOfSurgicalSpecialist() != null) {
					jsonMap.put("signatureOfSurgicalSpecialist",masMedicalExamReprt.getSignatureOfSurgicalSpecialist());
				} else {
					jsonMap.put("signatureOfSurgicalSpecialist", "");
				}
				
				

				if (masMedicalExamReprt.getRemarksOfDental() != null) {
					jsonMap.put("remarksOfDental",masMedicalExamReprt.getRemarksOfDental());
				} else {
					jsonMap.put("remarksOfDental", "");
				}
				
				
				if (masMedicalExamReprt.getDateOfDental() != null) {
					
					String dateOfDental = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getDateOfDental(), "dd/MM/yyyy");
					jsonMap.put("dateOfDental",dateOfDental);
				} else {
					jsonMap.put("dateOfDental", "");
				}
				
				
				if (masMedicalExamReprt.getSignatureOfDentalOfficer() != null) {
					jsonMap.put("signatureOfDentalOfficer",masMedicalExamReprt.getSignatureOfDentalOfficer());
				} else {
					jsonMap.put("signatureOfDentalOfficer", "");
				}
				

				if (masMedicalExamReprt.getEvidenceOfTrachoma() != null) {
					jsonMap.put("evidenceOfTrachoma",masMedicalExamReprt.getEvidenceOfTrachoma());
				} else {
					jsonMap.put("evidenceOfTrachoma", "");
				}
				
				if (masMedicalExamReprt.getBinocularVisionGrade() != null) {
					jsonMap.put("binocularVisionGrade",masMedicalExamReprt.getBinocularVisionGrade());
				} else {
					jsonMap.put("binocularVisionGrade", "");
				}
				
				
				
				if (masMedicalExamReprt.getManifestHypermetropiaMyopia() != null) {
					jsonMap.put("manifestHypermetropiaMyopia",masMedicalExamReprt.getManifestHypermetropiaMyopia());
				} else {
					jsonMap.put("manifestHypermetropiaMyopia", "");
				}
				
				if (masMedicalExamReprt.getCoverTest() != null) {
					jsonMap.put("coverTest",masMedicalExamReprt.getCoverTest());
				} else {
					jsonMap.put("coverTest", "");
				}
				
				if (masMedicalExamReprt.getDiaphragmTest() != null) {
					jsonMap.put("diaphragmTest",masMedicalExamReprt.getDiaphragmTest());
				} else {
					jsonMap.put("diaphragmTest", "");
				}
				
				if (masMedicalExamReprt.getFundiMedia() != null) {
					jsonMap.put("fundiMedia",masMedicalExamReprt.getFundiMedia());
				} else {
					jsonMap.put("fundiMedia", "");
				}
				
				
				
				if (masMedicalExamReprt.getSpecialField() != null) {
					jsonMap.put("fieldsSpecial",masMedicalExamReprt.getSpecialField());
				} else {
					jsonMap.put("fieldsSpecial", "");
				}
				
				
				if (masMedicalExamReprt.getNightVisualCapacity() != null) {
					jsonMap.put("nightVisualCapacity",masMedicalExamReprt.getNightVisualCapacity());
				} else {
					jsonMap.put("nightVisualCapacity", "");
				}
				
				if (masMedicalExamReprt.getConvergenceC() != null) {
					jsonMap.put("convergenceC",masMedicalExamReprt.getConvergenceC());
				} else {
					jsonMap.put("convergenceC", "");
				}
				
				
				if (masMedicalExamReprt.getConvergenceSc() != null) {
					jsonMap.put("convergenceSC",masMedicalExamReprt.getConvergenceSc());
				} else {
					jsonMap.put("convergenceSC", "");
				}
				
				
				if (masMedicalExamReprt.getAccommodationR() != null) {
					jsonMap.put("accomodationR",masMedicalExamReprt.getAccommodationR());
				} else {
					jsonMap.put("accomodationR", "");
				}
				
				
				if (masMedicalExamReprt.getAccommodationL() != null) {
					jsonMap.put("accomodationL",masMedicalExamReprt.getAccommodationL());
				} else {
					jsonMap.put("accomodationL", "");
				}

				if (masMedicalExamReprt.getManifestHypeMyopiaRemarks() != null) {
					jsonMap.put("manifestHypeMyopiaRemarks",masMedicalExamReprt.getManifestHypeMyopiaRemarks());
				} else {
					jsonMap.put("manifestHypeMyopiaRemarks", "");
				}
				
				if (masMedicalExamReprt.getManifestHypeMyopiaDate() != null) {
					
					String manifestHypeMyopiaDate = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getManifestHypeMyopiaDate(), "dd/MM/yyyy");
					jsonMap.put("manifestHypeMyopiaDate",manifestHypeMyopiaDate);
				} else {
					jsonMap.put("manifestHypeMyopiaDate", "");
				}
				
				
				if (masMedicalExamReprt.getNoseThroatSinusesRemarks() != null) {
					jsonMap.put("noseThroatSinusesRemarks",masMedicalExamReprt.getNoseThroatSinusesRemarks());
				} else {
					jsonMap.put("noseThroatSinusesRemarks", "");
				}
				
				
				if (masMedicalExamReprt.getNoseThroatSinusesDate() != null) {
					String noseThroatSinusesDate = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getNoseThroatSinusesDate(), "dd/MM/yyyy");
					jsonMap.put("noseThroatSinusesDate",noseThroatSinusesDate);
				} else {
					jsonMap.put("noseThroatSinusesDate", "");
				}
				
				if (masMedicalExamReprt.getRemarksOfGynaecology() != null) {
					 jsonMap.put("remarksOfGynaecology",masMedicalExamReprt.getRemarksOfGynaecology());
				} else {
					jsonMap.put("remarksOfGynaecology", "");
				}
				
				
				if (masMedicalExamReprt.getDateOfGynaecology() != null) {
					String dateOfGynaecology = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getDateOfGynaecology(), "dd/MM/yyyy");
					jsonMap.put("dateOfGynaecology",dateOfGynaecology);
				} else {
					jsonMap.put("dateOfGynaecology", "");
				}
				
				if (masMedicalExamReprt.getSignatureOfGynaecologist() != null) {
					 jsonMap.put("signatureOfGynaecologist",masMedicalExamReprt.getSignatureOfGynaecologist());
				} else {
					jsonMap.put("signatureOfGynaecologist", "");
				}
				
				//////////////////////////////For 2A from////////////////////////////////////////////////////////
		
				if (masMedicalExamReprt.getBatchNo() != null) {
					 jsonMap.put("batchNo",masMedicalExamReprt.getBatchNo());
				} else {
					jsonMap.put("batchNo", "");
				}
		
				if (masMedicalExamReprt.getChestNo() != null) {
					 jsonMap.put("chestNo",masMedicalExamReprt.getChestNo());
				} else {
					jsonMap.put("chestNo", "");
				}
				
				
				if (masMedicalExamReprt.getRollNo() != null) {
					 jsonMap.put("rollNo",masMedicalExamReprt.getRollNo());
				} else {
					jsonMap.put("rollNo", "");
				}
				
				if (masMedicalExamReprt.getMaritalStatus() != null) {
					 jsonMap.put("maritalStatusId",masMedicalExamReprt.getMaritalStatus());
				} else {
					jsonMap.put("maritalStatusId", "");
				}
				
				
				if (masMedicalExamReprt.getHoursOfFlown() != null) {
					 jsonMap.put("hoursFlown",masMedicalExamReprt.getHoursOfFlown());
				} else {
					jsonMap.put("hoursFlown", "");
				}
				
				

				if (masMedicalExamReprt.getParmanentAddress() != null) {
					 jsonMap.put("permanentAddress",masMedicalExamReprt.getParmanentAddress());
				} else {
					jsonMap.put("permanentAddress", "");
				}
				
				

				if (masMedicalExamReprt.getIdentificationMarks1() != null) {
					 jsonMap.put("identificationMarks1",masMedicalExamReprt.getIdentificationMarks1());
				} else {
					jsonMap.put("identificationMarks1", "");
				}
				if (masMedicalExamReprt.getIdentificationMarks2() != null) {
					 jsonMap.put("identificationMarks2",masMedicalExamReprt.getIdentificationMarks2());
				} else {
					jsonMap.put("identificationMarks2", "");
				}
				
				
				if (masMedicalExamReprt.getHypertension() != null) {
					 jsonMap.put("hypertension",masMedicalExamReprt.getHypertension());
				} else {
					jsonMap.put("hypertension", "");
				}
				

				if (masMedicalExamReprt.getHeartDisease() != null) {
					 jsonMap.put("heartDisease",masMedicalExamReprt.getHeartDisease());
				} else {
					jsonMap.put("heartDisease", "");
				}
				
				if (masMedicalExamReprt.getDiabetes() != null) {
					 jsonMap.put("diabetes",masMedicalExamReprt.getDiabetes());
				} else {
					jsonMap.put("diabetes", "");
				}
				
				if (masMedicalExamReprt.getBleedingDisorder() != null) {
					 jsonMap.put("bleedingDisorders",masMedicalExamReprt.getBleedingDisorder());
				} else {
					jsonMap.put("bleedingDisorders", "");
				}
				
				if (masMedicalExamReprt.getMentalDisease() != null) {
					 jsonMap.put("mentalDisease",masMedicalExamReprt.getMentalDisease());
				} else {
					jsonMap.put("mentalDisease", "");
				}
				
				if (masMedicalExamReprt.getNightBlindnessForFamily() != null) {
					 jsonMap.put("nightBlindnessForFamily",masMedicalExamReprt.getNightBlindnessForFamily());
				} else {
					jsonMap.put("nightBlindnessForFamily", "");
				}
				
				if (masMedicalExamReprt.getChronicBronchitis() != null) {
					 jsonMap.put("chronicBronchitisAsthma",masMedicalExamReprt.getChronicBronchitis());
				} else {
					jsonMap.put("chronicBronchitisAsthma", "");
				}
				
				if (masMedicalExamReprt.getPleurisy() != null) {
					 jsonMap.put("pleurisyTuberculosis",masMedicalExamReprt.getPleurisy());
				} else {
					jsonMap.put("pleurisyTuberculosis", "");
				}
				if (masMedicalExamReprt.getRheumatismFrequentSorethroat() != null) {
					 jsonMap.put("rhuematismFrequentSoreThroat",masMedicalExamReprt.getRheumatismFrequentSorethroat());
				} else {
					jsonMap.put("rhuematismFrequentSoreThroat", "");
				}
				
				if (masMedicalExamReprt.getChronicIndigestion() != null) {
					 jsonMap.put("chronicIndigestion",masMedicalExamReprt.getChronicIndigestion());
				} else {
					jsonMap.put("chronicIndigestion", "");
				}
				
				if (masMedicalExamReprt.getKidneyBladderTrouble() != null) {
					 jsonMap.put("kidneyBladderTrouble",masMedicalExamReprt.getKidneyBladderTrouble());
				} else {
					jsonMap.put("kidneyBladderTrouble", "");
				}
				
				if (masMedicalExamReprt.getStd() != null) {
					 jsonMap.put("std",masMedicalExamReprt.getStd());
				} else {
					jsonMap.put("std", "");
				}
				
				if (masMedicalExamReprt.getJaundice() != null) {
					 jsonMap.put("jaundice",masMedicalExamReprt.getJaundice());
				} else {
					jsonMap.put("jaundice", "");
				}
				
				if (masMedicalExamReprt.getAirSeaCarTrainSickness() != null) {
					 jsonMap.put("airSeaCarTrainSickness",masMedicalExamReprt.getAirSeaCarTrainSickness());
				} else {
					jsonMap.put("airSeaCarTrainSickness", "");
				}
				
				if (masMedicalExamReprt.getTrachoma() != null) {
					 jsonMap.put("trachoma",masMedicalExamReprt.getTrachoma());
				} else {
					jsonMap.put("trachoma", "");
				}
				
				if (masMedicalExamReprt.getNightBlindness() != null) {
					 jsonMap.put("nightBlindness",masMedicalExamReprt.getNightBlindness());
				} else {
					jsonMap.put("nightBlindness", "");
				}
				
				if (masMedicalExamReprt.getLaserTreatementSurgeryForE() != null) {
					 jsonMap.put("laserTreatmentSurgeryForEye",masMedicalExamReprt.getLaserTreatementSurgeryForE());
				} else {
					jsonMap.put("laserTreatmentSurgeryForEye", "");
				}
				
				if (masMedicalExamReprt.getAnyOtherEyeDisease() != null) {
					 jsonMap.put("anyOtherEyenDisease",masMedicalExamReprt.getAnyOtherEyeDisease());
				} else {
					jsonMap.put("anyOtherEyenDisease", "");
				}
				
				if (masMedicalExamReprt.getDischargeFromEars() != null) {
					 jsonMap.put("dischargeFromEars",masMedicalExamReprt.getDischargeFromEars());
				} else {
					jsonMap.put("dischargeFromEars", "");
				}

				if (masMedicalExamReprt.getAnyOtherEarDisease() != null) {
					 jsonMap.put("anyOtherEarDisease",masMedicalExamReprt.getAnyOtherEarDisease());
				} else {
					jsonMap.put("anyOtherEarDisease", "");
				}
				
				if (masMedicalExamReprt.getFrequentCoughColdSinusitis() != null) {
					 jsonMap.put("frequentCoughColdSinusitis",masMedicalExamReprt.getFrequentCoughColdSinusitis());
				} else {
					jsonMap.put("frequentCoughColdSinusitis", "");
				}
				
				if (masMedicalExamReprt.getNervousBreakdownMentalIllne() != null) {
					 jsonMap.put("nervousBreakdownMentalIllness",masMedicalExamReprt.getNervousBreakdownMentalIllne());
				} else {
					jsonMap.put("nervousBreakdownMentalIllness", "");
				}
				
				if (masMedicalExamReprt.getFitsFaintingAttack() != null) {
					 jsonMap.put("fitsFaintingAttacks",masMedicalExamReprt.getFitsFaintingAttack());
				} else {
					jsonMap.put("fitsFaintingAttacks", "");
				}
				
				if (masMedicalExamReprt.getSevereHeadInjury() != null) {
					 jsonMap.put("severeHeadInjury",masMedicalExamReprt.getSevereHeadInjury());
				} else {
					jsonMap.put("severeHeadInjury", "");
				}
				
				if (masMedicalExamReprt.getBreastDiseaseDischarge() != null) {
					 jsonMap.put("breastDiseaseDischarge",masMedicalExamReprt.getBreastDiseaseDischarge());
				} else {
					jsonMap.put("breastDiseaseDischarge", "");
				}
				if (masMedicalExamReprt.getAmenorrhoeaDysmemhorrheas() != null) {
					 jsonMap.put("amenorrhoeaDysmemhorrheas",masMedicalExamReprt.getAmenorrhoeaDysmemhorrheas());
				} else {
					jsonMap.put("amenorrhoeaDysmemhorrheas", "");
				}
				
				if (masMedicalExamReprt.getMenonhagia() != null) {
					 jsonMap.put("menorrhagia",masMedicalExamReprt.getMenonhagia());
				} else {
					jsonMap.put("menorrhagia", "");
				}
				if (masMedicalExamReprt.getPregnancy() != null) {
					 jsonMap.put("pregnancy",masMedicalExamReprt.getPregnancy());
				} else {
					jsonMap.put("pregnancy", "");
				}
				
				if (masMedicalExamReprt.getAbortion() != null) {
					 jsonMap.put("abortion",masMedicalExamReprt.getAbortion());
				} else {
					jsonMap.put("abortion", "");
				}
				
				if (masMedicalExamReprt.getMediyUnfitBraArmForRejected() != null) {
					 jsonMap.put("mediyUnfitBranchArmedForcesRejected",masMedicalExamReprt.getMediyUnfitBraArmForRejected());
				} else {
					jsonMap.put("mediyUnfitBranchArmedForcesRejected", "");
				}
				
				if (masMedicalExamReprt.getMediyUnfitBraArmForDischarged() != null) {
					 jsonMap.put("mediyUnfitBranchArmedForcesDischarged",masMedicalExamReprt.getMediyUnfitBraArmForDischarged());
				} else {
					jsonMap.put("mediyUnfitBranchArmedForcesDischarged", "");
				}
				
				if (masMedicalExamReprt.getIllOperatInjDisDurat() != null) {
					 jsonMap.put("illnessOperationInjuryDiseaseDuration",masMedicalExamReprt.getIllOperatInjDisDurat());
				} else {
					jsonMap.put("illnessOperationInjuryDiseaseDuration", "");
				}
				if (masMedicalExamReprt.getIllOperatInjDiseaDuratStayHos() != null) {
					 jsonMap.put("illnessOperationInjuryDiseaseDurationStayHospital",masMedicalExamReprt.getIllOperatInjDiseaDuratStayHos());
				} else {
					jsonMap.put("illnessOperationInjuryDiseaseDurationStayHospital", "");
				}
				
				if (masMedicalExamReprt.getOtherInformationHealth() != null) {
					 jsonMap.put("otherInformationHealth",masMedicalExamReprt.getOtherInformationHealth());
				} else {
					jsonMap.put("otherInformationHealth", "");
				}
				
				if (masMedicalExamReprt.getSignatureMedicalOfficer() != null) {
					 jsonMap.put("signatureMedicalOfficer",masMedicalExamReprt.getSignatureMedicalOfficer());
				} else {
					jsonMap.put("signatureMedicalOfficer", "");
				}
				String dateOfMedicalOfficer="";
				if (masMedicalExamReprt.getSignatureMedicalOfficerDate() != null) {
					dateOfMedicalOfficer = HMSUtil.convertDateToStringFormat(masMedicalExamReprt.getSignatureMedicalOfficerDate(),
							"dd/MM/yyyy");
				
					
					 jsonMap.put("signatureMedicalOfficerDate",dateOfMedicalOfficer);
				} else {
					jsonMap.put("signatureMedicalOfficerDate", "");
				}
				
				if (masMedicalExamReprt.getSignatureOfCanditate() != null) {
					 jsonMap.put("signatureOfCanditate",masMedicalExamReprt.getSignatureOfCanditate());
				} else {
					jsonMap.put("signatureOfCanditate", "");
				}
				
				String dateOfCandidate="";
				if (masMedicalExamReprt.getSignatureOfCanditateDate() != null) {
					dateOfCandidate = HMSUtil.convertDateToStringFormat(masMedicalExamReprt.getSignatureOfCanditateDate(),
							"dd/MM/yyyy");
				
					
					 jsonMap.put("signatureOfCanditateDate",dateOfCandidate);
				} else {
					jsonMap.put("signatureOfCanditateDate", "");
				}
				 
				
				
				//////////////////////////////3A////////////////////////////////////////////
				if(masMedicalExamReprt.getSignatureMedicalSpecialist() !=null )
				{
					jsonMap.put("signatureOfMedicalSpecialist",masMedicalExamReprt.getSignatureMedicalSpecialist());
				}
				else {
					jsonMap.put("signatureOfMedicalSpecialist","");
				}
				
				if(masMedicalExamReprt.getExternalEarR() !=null )
				{
					jsonMap.put("externalEarR",masMedicalExamReprt.getExternalEarR());
				}
				else {
					jsonMap.put("externalEarR","");
				}
				if(masMedicalExamReprt.getExternalEarL() !=null )
				{
					jsonMap.put("externalEarL",masMedicalExamReprt.getExternalEarL());
				}
				else {
					jsonMap.put("externalEarL","");
				}
				if(masMedicalExamReprt.getMiddleEar() !=null )
				{
					jsonMap.put("middleEarR",masMedicalExamReprt.getMiddleEar());
				}
				else {
					jsonMap.put("middleEarR","");
				}
				
				if(masMedicalExamReprt.getMiddleEarR() !=null )
				{
					jsonMap.put("middleEarL",masMedicalExamReprt.getMiddleEarR());
				}
				else {
					jsonMap.put("middleEarL","");
				}
				
				if(masMedicalExamReprt.getInnerEarR() !=null )
				{
					jsonMap.put("innerEarR",masMedicalExamReprt.getInnerEarR());
				}
				else {
					jsonMap.put("innerEarR","");
				}
				
				if(masMedicalExamReprt.getInnerEarL() !=null )
				{
					jsonMap.put("innerEarL",masMedicalExamReprt.getInnerEarL());
				}
				else {
					jsonMap.put("innerEarL","");
				}
				if(masMedicalExamReprt.getSignatureOfENTSpecialist() !=null )
				{
					jsonMap.put("signatureOfENTSpecialist",masMedicalExamReprt.getSignatureOfENTSpecialist());
				}
				else {
					jsonMap.put("signatureOfENTSpecialist","");
				}
				
				
				if(masMedicalExamReprt.getNose() !=null )
				{
					jsonMap.put("noseSinuses",masMedicalExamReprt.getNose());
				}
				else {
					jsonMap.put("noseSinuses","");
				}
				if(masMedicalExamReprt.getNosethroat() !=null )
				{
					jsonMap.put("throatSinuses",masMedicalExamReprt.getNosethroat());
				}
				else {
					jsonMap.put("throatSinuses","");
				}
				
				

				if(opdObesityHd!=null) {
					if(opdObesityHd!=null && opdObesityHd.getCloseDate()!=null) {
						jsonMap.put("obsistyCloseDate", opdObesityHd.getCloseDate());
					}
					else {
						jsonMap.put("obsistyCloseDate", "");
					}
					if(opdObesityHd!=null && opdObesityHd.getOverweightFlag()!=null)
					{	
						jsonMap.put("obesityOverWeightFlag",opdObesityHd.getOverweightFlag());
					}
					else
					{
						jsonMap.put("obesityOverWeightFlag","");	
					}
					
					if(opdObesityHd!=null) {
						jsonMap.put("overweightFlag", opdObesityHd.getOverweightFlag());
					}
					else {
						jsonMap.put("overweightFlag", "");
					}
					
					}
				
				
				
				if(masMedicalExamReprt.getMemberPlace() !=null )
				{
					jsonMap.put("memberPlace",masMedicalExamReprt.getMemberPlace());
				}
				else {
					jsonMap.put("memberPlace","");
				}
				
				
				if (masMedicalExamReprt.getMemberDate() != null) {
					String memberDate = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getMemberDate(), "dd/MM/yyyy");
					jsonMap.put("memberDate",memberDate);
				} else {
					jsonMap.put("memberDate", "");
				}
				
				
				if(masMedicalExamReprt.getRankDesiMember1() !=null )
				{
					jsonMap.put("rankDesiMember1",masMedicalExamReprt.getRankDesiMember1());
				}
				else {
					jsonMap.put("rankDesiMember1","");
				}
				
				
				if(masMedicalExamReprt.getNameMember1() !=null )
				{
					jsonMap.put("nameMember1",masMedicalExamReprt.getNameMember1());
				}
				else {
					jsonMap.put("nameMember1","");
				}
				
				
				
				if(masMedicalExamReprt.getRankDesiMember2() !=null )
				{
					jsonMap.put("rankDesiMember2",masMedicalExamReprt.getRankDesiMember2());
				}
				else {
					jsonMap.put("rankDesiMember2","");
				}
				

				if(masMedicalExamReprt.getNameMember2() !=null )
				{
					jsonMap.put("nameMember2",masMedicalExamReprt.getNameMember2());
				}
				else {
					jsonMap.put("nameMember2","");
				}
				
				
				
				
				if(masMedicalExamReprt.getRankDesiPresident() !=null )
				{
					jsonMap.put("rankDesiPresident",masMedicalExamReprt.getRankDesiPresident());
				}
				else {
					jsonMap.put("rankDesiPresident","");
				}
				
				
				
				if(masMedicalExamReprt.getBoardpresident() !=null )
				{
					jsonMap.put("namePresident",masMedicalExamReprt.getBoardpresident());
				}
				else {
					jsonMap.put("namePresident","");
				}
				
				
				
				
/////////////////////////////
				if(masMedicalExamReprt.getMemberPlaceSub() !=null )
				{
					jsonMap.put("memberPlaceSub",masMedicalExamReprt.getMemberPlaceSub());
				}
				else {
					jsonMap.put("memberPlaceSub","");
				}
				
				
				if (masMedicalExamReprt.getMemberDateSub() != null) {
					String memberDate = HMSUtil
							.convertDateToStringFormat(masMedicalExamReprt.getMemberDateSub(), "dd/MM/yyyy");
					jsonMap.put("memberDateSub",memberDate);
				} else {
					jsonMap.put("memberDateSub", "");
				}
				
				
				if(masMedicalExamReprt.getRankDesiMember1Sub() !=null )
				{
					jsonMap.put("rankDesiMember1Sub",masMedicalExamReprt.getRankDesiMember1Sub());
				}
				else {
					jsonMap.put("rankDesiMember1Sub","");
				}
				
				
				if(masMedicalExamReprt.getNameMember1Sub() !=null )
				{
					jsonMap.put("nameMember1Sub",masMedicalExamReprt.getNameMember1Sub());
				}
				else {
					jsonMap.put("nameMember1Sub","");
				}
				
				
				
				if(masMedicalExamReprt.getRankDesiMember2Sub() !=null )
				{
					jsonMap.put("rankDesiMember2Sub",masMedicalExamReprt.getRankDesiMember2Sub());
				}
				else {
					jsonMap.put("rankDesiMember2Sub","");
				}
				

				if(masMedicalExamReprt.getNameMember2Sub() !=null )
				{
					jsonMap.put("nameMember2Sub",masMedicalExamReprt.getNameMember2Sub());
				}
				else {
					jsonMap.put("nameMember2Sub","");
				}
				
				
				
				
				if(masMedicalExamReprt.getRankDesiPresidentSub() !=null )
				{
					jsonMap.put("rankDesiPresidentSub",masMedicalExamReprt.getRankDesiPresidentSub());
				}
				else {
					jsonMap.put("rankDesiPresidentSub","");
				}
				
				
				
				if(masMedicalExamReprt.getNamePresidentSub() !=null )
				{
					jsonMap.put("namePresidentSub",masMedicalExamReprt.getNamePresidentSub());
				}
				else {
					jsonMap.put("namePresidentSub","");
				}
				
				jsonMap.put("paFinalobservationSubsequent", masMedicalExamReprt.getPaAfroFinalObser());
				
				
				if(masMedicalExamReprt.getLegLength() !=null )
				{
					jsonMap.put("legLength",masMedicalExamReprt.getLegLength());
				}
				else {
					jsonMap.put("legLength","");
				}
				if(masMedicalExamReprt.getPhysique() !=null )
				{
					jsonMap.put("physique",masMedicalExamReprt.getPhysique());
				}
				else {
					jsonMap.put("physique","");
				}
				if(masMedicalExamReprt.getSkin() !=null )
				{
					jsonMap.put("skin",masMedicalExamReprt.getSkin());
				}
				else {
					jsonMap.put("skin","");
				}
				if(masMedicalExamReprt.getErLocrine() !=null )
				{
					jsonMap.put("erlocrine",masMedicalExamReprt.getErLocrine());
				}
				else {
					jsonMap.put("erlocrine","");
				}
				if(masMedicalExamReprt.getAnyOtheAbnormalities() !=null )
				{
					jsonMap.put("anyOtherAbnormalities",masMedicalExamReprt.getAnyOtheAbnormalities());
				}
				else {
					jsonMap.put("anyOtherAbnormalities","");
				}
				
				if(masMedicalExamReprt.getUpperLimbs() !=null )
				{
					jsonMap.put("upperLimbs",masMedicalExamReprt.getUpperLimbs());
				}
				else {
					jsonMap.put("upperLimbs","");
				}

				if(masMedicalExamReprt.getLowerLimbs() !=null )
				{
					jsonMap.put("lowerLimbs",masMedicalExamReprt.getLowerLimbs());
				}
				else {
					jsonMap.put("lowerLimbs","");
				}
				
				if(masMedicalExamReprt.getLumber() !=null )
				{
					jsonMap.put("lumbarSacral",masMedicalExamReprt.getLumber());
				}
				else {
					jsonMap.put("lumbarSacral","");
				}
				if(masMedicalExamReprt.getGenitoUriraryPerineum() !=null )
				{
					jsonMap.put("genitoUrinaryPerineum",masMedicalExamReprt.getGenitoUriraryPerineum());
				}
				else {
					jsonMap.put("genitoUrinaryPerineum","");
				}
				   
				if(masMedicalExamReprt.getHerniaMusic() !=null )
				{
					jsonMap.put("herniaMuscle",masMedicalExamReprt.getHerniaMusic());
				}
				else {
					jsonMap.put("herniaMuscle","");
				}  
				  
				Long serviceType = null;
				if (masMedicalExamReprt != null
						&& masMedicalExamReprt.getServiceTypeId() != null) {
					serviceType=masMedicalExamReprt.getServiceTypeId();
					
				}  
				jsonMap.put("serviceType", serviceType);
				
				/////////////////////////////////////////////////////////////////////////////////////////////////////
				
				responseList.add(jsonMap);

				if (responseList != null && responseList.size() > 0) {
					json.put("listOfResponse", responseList);
					json.put("listMasServiceType", listMasServiceType);
					json.put("status", 1);

				} else {
					json.put("listOfResponse", responseList);
					json.put("listMasServiceType", listMasServiceType);
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			} else {
				try {
					json.put("listOfResponse", responseList);
					json.put("listMasServiceType", listMasServiceType);
					
					json.put("msg", "data not found");
					json.put("status", 0);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public String getRankOfUserId(Users users) {
		List<MasEmployee> listMasEmployee=null;
		String rankOfEmployee="";
		 if(users!=null && StringUtils.isNotEmpty(users.getServiceNo())) {
			 listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(users.getServiceNo());	
		 	}
		if(CollectionUtils.isNotEmpty(listMasEmployee)) {
						if(listMasEmployee.get(0).getMasRank()!=null ) {
							MasRank masRank=systemAdminDao.getRankByRankCode((listMasEmployee.get(0).getMasRank()));
							if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
								rankOfEmployee=masRank.getRankName();
						}
				}
					return rankOfEmployee;
	}
	
	
	*//**
	 * @Description:Method is used for getInvestigationAndResult
	 * @param:HashMap<String, Object>
	 *                            jsondata
	 * @param:HttpServletRequest request,
	 * @param:HttpServletResponse response
	 * 
	 * 
	 *//*
	
	
	
	
	///////////////////////////////////////////////////////////////
	@Override
	@Transactional
	public String getInvestigationAndResult(HashMap<String, Object> jsondata,HttpServletRequest request, HttpServletResponse response) {
			JSONObject json = new JSONObject();
			List<Object[]> listObject = null;
		//	List<Object[]> listObject1 = null;
			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> responseList = new ArrayList<HashMap<String, Object>>();
			Long mainChargeCodeId=null;
			try {
				if (jsondata.containsKey("visitId") && jsondata.get("visitId") != null && StringUtils.isNotEmpty(jsondata.get("visitId").toString())) {
					listObject = medicalExamDAO
							.getDgMasInvestigationsAndResultForSubInvestigation(Long.parseLong(jsondata.get("visitId").toString()));
				}
				Long investigationId = null;
				String investigationName = "";
				Long orderHdId = null;
				String urgent = "";
				String labMark = "";
				String orderDate = "";
				Long visitId = null;
				String otherInvestigation = "";
				Long departId = null;
				Long hospitalId = null;
				Long dgOrderDtId = null;
				Long dgResultDt = null;
				Long dgResultHd = null;
				String result = "";
				String uomName="";
				String minNormalValue = "";
				String maxNormalValue="";
				String investigationType="";
				Long mainChargeCodeForInv=null;
				Long subChargeCodeIdForInv=null;
				String investigationRemarks="";
				String rangeValue="";
				String reultOfflineNumber="";
				String resultOffLineDate="";
				List<Long>listInvestigation=new ArrayList<>();
				if (listObject!=null) {
					for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
						Object[] row = (Object[]) it.next();
					 
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
							labMark = row[3].toString();
						}
						if (row[4] != null) {
							urgent = row[4].toString();
						}

						if (row[5] != null) {
							orderDate = row[5].toString();
							Date dd1 = HMSUtil.dateFormatteryyyymmdd(orderDate);
							 
							orderDate = HMSUtil.convertDateToStringFormat(dd1, "dd/MM/yyyy");

						} else {
							orderDate = "";
						}
						if (row[6] != null) {
							visitId = Long.parseLong(row[6].toString());
						}
						if (row[7] != null) {
							otherInvestigation = row[7].toString();
						}
						if (row[8] != null) {
							departId = Long.parseLong(row[8].toString());
						}
						if (row[9] != null) {
							hospitalId = Long.parseLong(row[9].toString());
						}
						if (row[10] != null) {
							dgOrderDtId = Long.parseLong(row[10].toString());
						} else {
							dgOrderDtId = null;
						}

						if (row[11] != null) {
							dgResultDt = Long.parseLong(row[11].toString());
						} else {
							dgResultDt = null;
						}
						if (row[12] != null) {
							dgResultHd = Long.parseLong(row[12].toString());
						} else {
							dgResultHd = null;
						}
						if (row[13] != null) {
							result = row[13].toString();
						} else {
							result = "";
						}
						if (row[14] != null) {
							uomName = row[14].toString();
						} else {
							uomName = "";
						}
						if (row[15] != null) {
							minNormalValue = row[15].toString();
						} else {
							minNormalValue = "";
						}
						if (row[16] != null) {
							maxNormalValue = row[16].toString();
						} else {
							maxNormalValue = "";
						}
						
						if (row[17] != null) {
							investigationType = row[17].toString();
							if(investigationType!="" && investigationType.equalsIgnoreCase("m"))
								listInvestigation.add(investigationId);
						} else {
							investigationType = "";
						}
						
						
						
						if (row[18] != null) {
							subChargeCodeIdForInv = Long.parseLong(row[18].toString());
						}
						 else {
							 subChargeCodeIdForInv =null;
						 }
						
						if (row[19] != null) {
							mainChargeCodeForInv = Long.parseLong(row[19].toString());
						}
						 else {
							 mainChargeCodeForInv =null;
						 }
						String mainChargeCodeNameForInve="";
						if (row[19] != null) {
							
							MasMainChargecode mmcc=null;
							mmcc = medicalExamDAO.getMainChargeCodeByMainChargeCodeId(Long.parseLong(row[19].toString()));
							mainChargeCodeNameForInve=mmcc.getMainChargecodeCode();
							}
						else {
							mainChargeCodeNameForInve="";
						}
						
						Long ridcIdInvVal=null;
						if (row[20] != null) {
							ridcIdInvVal = Long.parseLong(row[20].toString());
						}
						 else {
							 ridcIdInvVal =null;
						 }
						

						if (row[21] != null) {
							investigationRemarks =  row[21].toString();
						}
						 else {
							 investigationRemarks ="";
						 }
						if (row[22] != null) {
							rangeValue =  row[22].toString();
						}
						 else {
							 rangeValue ="";
						 }
						
						if (row[23] != null) {
							reultOfflineNumber =  row[23].toString();
						
						}
						 else {
							 reultOfflineNumber ="";
						 }
						
						
						if (row[24] != null) {
							resultOffLineDate =  row[24].toString();
							Date dd1 = HMSUtil.dateFormatteryyyymmdd(resultOffLineDate);
							 
							resultOffLineDate = HMSUtil.convertDateToStringFormat(dd1, "dd/MM/yyyy");
						}
						 else {
							 resultOffLineDate ="";
						 }
						
						pt.put("investigationName", investigationName);
						pt.put("investigationId", investigationId);
						pt.put("labMark", labMark);
						pt.put("urgent", urgent);
						pt.put("orderDate", orderDate);
						pt.put("visitId", visitId);
						pt.put("otherInvestigation", otherInvestigation);
						pt.put("departId", departId);
						pt.put("hospitalId", hospitalId);
						pt.put("dgOrderDtId", dgOrderDtId);
						pt.put("orderHdId", orderHdId);

						pt.put("dgResultDt", dgResultDt);
						pt.put("dgResultHd", dgResultHd);
						pt.put("result", result);
						pt.put("uomName", uomName);
						
						pt.put("minNormalValue", minNormalValue);
						pt.put("maxNormalValue", maxNormalValue);
						pt.put("investigationType", investigationType);
						
						pt.put("mainChargeCodeForInv", mainChargeCodeForInv);
						pt.put("subChargeCodeIdForInv", subChargeCodeIdForInv);
						pt.put("mainChargeCodeNameForInve", mainChargeCodeNameForInve);
						pt.put("ridcIdInvVal", ridcIdInvVal);
						
						pt.put("investigationRemarks",investigationRemarks);
						pt.put("rangeValue",rangeValue);
						
						pt.put("reultOfflineNumber",reultOfflineNumber);
						pt.put("resultOffLineDate",resultOffLineDate);
						c.add(pt);
					//}
						if(investigation==1) {
							break;
						}
					}
				//}
					json.put("listObject", c);
					json.put("status", "1");
				}
				
				else {
					json.put("listObject", c);
					json.put("status", "0");

				}
				
		List<Object[]> listDgMasInvestigation=null;
	 	
		if(CollectionUtils.isNotEmpty(listInvestigation))
		listDgMasInvestigation = medicalExamDAO
				.getDgMasInvestigationsAndResult(Long.parseLong(jsondata.get("visitId").toString()),listInvestigation);
			  
			  String genderCode= cd. getAdminSexCode(jsondata);
				
				Long subInvestigationId=null;
				String subInvestigationName="";
				Long orderDtIdForSub=null;
				Long orderHdIdForSub=null;
				Long resultEntryDtidForSub=null;
				Long resultEntryHdidForSub=null;
				Long investigationIdSub=null;
				String rangeSub="";
				String umoNameSub="";
				String investigationTypeSub="";
				Long ridcIdSub=null;
				String resultForSub="";
				String investigationNameSub="";
				String subInvestigationCadeForSub="";
				Long mainChargeCodeIdForSub=null;
				Long subMainChargeCodeIdForSub=null;
				String investigationRemarksForSub=""; 
				if (listDgMasInvestigation != null) {
					
					for (Iterator<?> it1 = listDgMasInvestigation.iterator(); it1.hasNext();) {
						Object[] row1 = (Object[]) it1.next();
						HashMap<String, Object> jsonMap = new HashMap<String, Object>();
						if (row1[0] != null) {
							subInvestigationId = Long.parseLong(row1[0].toString());
							jsonMap.put("subInvestigationId",subInvestigationId);
						}
						else {
							subInvestigationId=null;
						}
						 
						jsonMap.put("subInvestigationId",subInvestigationId);
						 
						
						if (row1[1] != null) {
							subInvestigationName = row1[1].toString();
						}
						else {
							subInvestigationName="";
						}
						jsonMap.put("subInvestigationName",subInvestigationName);
						
						if (row1[2] != null) {
							orderDtIdForSub = Long.parseLong(row1[2].toString());
						}
						else {
							orderDtIdForSub=null;
						}
						jsonMap.put("orderDtIdForSub",orderDtIdForSub);
						
						
						if (row1[3] != null) {
							orderHdIdForSub =  Long.parseLong(row1[3].toString());
						}
						else {
							orderHdIdForSub=null;	
						}
						jsonMap.put("orderHdIdForSub",orderHdIdForSub);
						
						
						if (row1[4] != null) {
							resultEntryDtidForSub = Long.parseLong(row1[4].toString());
						}
						else {
							resultEntryDtidForSub=null;
						}
						jsonMap.put("resultEntryDtidForSub",resultEntryDtidForSub);
						
						
						if (row1[5] != null) {
							resultEntryHdidForSub =  Long.parseLong(row1[5].toString());
						}
						else {
							resultEntryHdidForSub=null;	
						}
						jsonMap.put("resultEntryHdidForSub",resultEntryHdidForSub);
						
						
						if (row1[7] != null) {
							investigationTypeSub = row1[7].toString();
						}
						else {
							investigationTypeSub="";
						}
						jsonMap.put("investigationTypeSub",investigationTypeSub);
						
						
						if (row1[8] != null) {
							investigationIdSub = Long.parseLong(row1[8].toString());
						}
						else {
							investigationIdSub=null;
						}
						jsonMap.put("investigationIdSub",investigationIdSub);
						
						
						if (row1[9] != null) {
							investigationNameSub =  row1[9].toString();
						}
						else {
							investigationNameSub="";
						}
						jsonMap.put("investigationNameSub",investigationNameSub);
						
						
						if (row1[10] != null) {
							umoNameSub =  row1[10].toString();
						}
						else {
							umoNameSub="";
						}
						jsonMap.put("umoNameSub",umoNameSub);
						
						if (row1[11] != null) {
							resultForSub =  row1[11].toString();
						}
						else {
							resultForSub="";
						}
						jsonMap.put("resultForSub",resultForSub);
						
						if (row1[12] != null) {
							rangeSub =  row1[12].toString();
						}
						else {
							rangeSub="";
						}
						
						
						if(StringUtils.isEmpty(rangeSub)) { 
							String range="";
							List<DgNormalValue>listDgNormalValue=null;
							if(row1[0]!=null) {
							Criterion cr1=Restrictions.eq("subInvestigationId", Long.parseLong(row1[0].toString()));
							 listDgNormalValue=dgNormalValueDao.findByCriteria(cr1);
							}
							if(CollectionUtils.isNotEmpty(listDgNormalValue)) {
								for(DgNormalValue dgNormalValue :listDgNormalValue) {
									if(dgNormalValue!=null && dgNormalValue.getSex().equalsIgnoreCase(genderCode)) {
										
										if(dgNormalValue.getMinNormalValue()!=null)
											rangeSub=dgNormalValue.getMinNormalValue();
										if(dgNormalValue.getMaxNormalValue()!=null)
											rangeSub+= "-"+ dgNormalValue.getMaxNormalValue();
										
									}
								}
							}
							
						}
						
						jsonMap.put("mainChargeCodeIdSub", mainChargeCodeId);
						
						
						String mainChargeCodeName="";
						if (row1[16]!= null) {
							
							MasMainChargecode mmcc=null;
							mmcc = medicalExamDAO.getMainChargeCodeByMainChargeCodeId(Long.parseLong(row1[16].toString()));
							if(mmcc!=null)
								mainChargeCodeName=mmcc.getMainChargecodeCode();
							}
						else {
							mainChargeCodeName="";
						}
							jsonMap.put("mainChargeCodeNameForSub", mainChargeCodeName);
						
						jsonMap.put("rangeSub",rangeSub);
						jsonMap.put("subInvestigationCadeForSub",subInvestigationCadeForSub);
						if (row1[13] != null) {
							ridcIdSub =  Long.parseLong(row1[13].toString());
						}
						else {
							ridcIdSub=null;
						}
						if (row1[14] != null) {
							subInvestigationCadeForSub =  row1[14].toString();
						}
						else {
							subInvestigationCadeForSub="";
						}
						jsonMap.put("ridcIdSub",ridcIdSub);
						
						
						if (row1[16] != null) {
							mainChargeCodeIdForSub = Long.parseLong( row1[16].toString());
						}
						else {
							mainChargeCodeIdForSub=null;
						}
						if (row1[15] != null) {
							subMainChargeCodeIdForSub = Long.parseLong( row1[15].toString());
						}
						else {
							subMainChargeCodeIdForSub = null;
						}
						

						if (row1[17] != null) {
							investigationRemarksForSub =  row1[17].toString();
						}
						 else {
							 investigationRemarksForSub ="";
						 }
						
						
						jsonMap.put("mainChargeCodeIdForSub",mainChargeCodeIdForSub);
						jsonMap.put("subMainChargeCodeIdForSub",subMainChargeCodeIdForSub);
						jsonMap.put("investigationRemarksForSub",investigationRemarksForSub);
						responseList.add(jsonMap);
						
					}
							
			}
		
			if (responseList != null && responseList.size() > 0) {
						json.put("subInvestigationData", responseList);
						json.put("status", 1);

					} else {
						json.put("subInvestigationData", responseList);
						json.put("msg", "Data not found");
						json.put("status", 0);
					}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json.toString();
	}

	
	//////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	 
	public String getInvestigationAndResult_old(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object[]> listObject = null;
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		try {
			if (jsondata.containsKey("visitId") && jsondata.get("visitId") != null && StringUtils.isNotEmpty(jsondata.get("visitId").toString())) {
				listObject = medicalExamDAO
						.getDgMasInvestigationsAndResult(Long.parseLong(jsondata.get("visitId").toString()),null);
			}
			
			
			
			
			
			
			Long investigationId = null;
			String investigationName = "";
			Long orderHdId = null;
			String urgent = "";
			String labMark = "";
			String orderDate = "";
			Long visitId = null;
			String otherInvestigation = "";
			Long departId = null;
			Long hospitalId = null;
			Long dgOrderDtId = null;
			Long dgResultDt = null;
			Long dgResultHd = null;
			String result = "";
			String uomName="";
			Long ridcId=null;
			String rangeVal="";
			String investigationRemarks="";
			String mainChargeCodeName="";
			if (listObject != null) {

				for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
					Object[] row = (Object[]) it.next();

					
					 * for(Object dgMasInvestigation : listObject) { Object row=dgMasInvestigation;
					 
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
						labMark = row[3].toString();
					}
					if (row[4] != null) {
						urgent = row[4].toString();
					}

					if (row[5] != null) {
						orderDate = row[5].toString();
						Date dd1 = HMSUtil.dateFormatteryyyymmdd(orderDate);
						// Date dd1 =HMSUtil.convertStringDateToUtilDateForDatabase(orderDate);
						//orderDate = HMSUtil.getDateWithoutTime(dd1);
						orderDate = HMSUtil.convertDateToStringFormat(dd1, "dd/MM/yyyy");

					} else {
						orderDate = "";
					}
					if (row[6] != null) {
						visitId = Long.parseLong(row[6].toString());
					}
					if (row[7] != null) {
						otherInvestigation = row[7].toString();
					}
					if (row[8] != null) {
						departId = Long.parseLong(row[8].toString());
					}
					if (row[9] != null) {
						hospitalId = Long.parseLong(row[9].toString());
					}
					if (row[10] != null) {
						dgOrderDtId = Long.parseLong(row[10].toString());
					} else {
						dgOrderDtId = null;
					}

					if (row[11] != null) {
						dgResultDt = Long.parseLong(row[11].toString());
					} else {
						dgResultDt = null;
					}
					if (row[12] != null) {
						dgResultHd = Long.parseLong(row[12].toString());
					} else {
						dgResultHd = null;
					}
					if (row[13] != null) {
						result = row[13].toString();
					} else {
						result = "";
					}
					if (row[14] != null) {
						uomName = row[14].toString();
					} else {
						uomName = "";
					}
					if (row[15] != null) {
						ridcId = Long.parseLong(row[15].toString());
					} else {
						ridcId = null;
					}
					if (row[16] != null) {
						rangeVal = row[16].toString();
					} else {
						rangeVal = "";
					}
					
					if (row[17] != null) {
						investigationRemarks = row[17].toString();
					} else {
						investigationRemarks = "";
					}
					if (row[18] != null) {
						
					MasMainChargecode mmcc=null;
					mmcc = medicalExamDAO.getMainChargeCodeByMainChargeCodeId(Long.parseLong(row[18].toString()));
					mainChargeCodeName=mmcc.getMainChargecodeCode();
					}
					
					pt.put("investigationName", investigationName);
					pt.put("investigationId", investigationId);
					pt.put("labMark", labMark);
					pt.put("urgent", urgent);
					pt.put("orderDate", orderDate);
					pt.put("visitId", visitId);
					pt.put("otherInvestigation", otherInvestigation);
					pt.put("departId", departId);
					pt.put("hospitalId", hospitalId);
					pt.put("dgOrderDtId", dgOrderDtId);
					pt.put("orderHdId", orderHdId);

					pt.put("dgResultDt", dgResultDt);
					pt.put("dgResultHd", dgResultHd);
					pt.put("result", result);
					pt.put("uomName", uomName);
					pt.put("ridcId", ridcId);
					pt.put("rangeVal", rangeVal);
					pt.put("investigationRemarks", investigationRemarks);
					pt.put("mainChargeCodeName", mainChargeCodeName);
					
					c.add(pt);
				}
			}
			json.put("listObject", c);
			json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
			json.put("status", "1");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	*//**
	 * @Description:Method is used for updateVisitStatus
	 * @param visitId
	 * @param status
	 *//*
	public void updateVisitStatus(String visitId, String status) {
		try {
			List<Visit> listVisit = opdDao.getPatientVisit(Long.parseLong(visitId));
			Visit visit = listVisit.get(0);
			visit.setExamStatus(status);
			if(status!=null && status.equalsIgnoreCase("C"))
				visit.setVisitStatus("c");
			medicalExamDAO.saveOrUpdateVisit(visit);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*//**
	 * @Description:Method is used for getMEApprovalWaitingGrid
	 * @param:HashMap<String, Object>
	 *                            jsondata
	 * @param:HttpServletRequest request
	 * @param:HttpServletResponse response
	 *//*
	@Override
	public String getMEApprovalWaitingGrid(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<MasMedicalExamReport> listMasMedicalExamReport = null;
		Map<String, Object> map = null;
		if (!jsondata.isEmpty()) {
			map = medicalExamDAO.getApprovalMedicalExamList(jsondata);
			int count = (int) map.get("count");
			listMasMedicalExamReport = (List<MasMedicalExamReport>) map.get("listVisit");

			if (CollectionUtils.isNotEmpty(listMasMedicalExamReport)) {
				for (MasMedicalExamReport masMedicalExamReport : listMasMedicalExamReport) {

					Visit visit = masMedicalExamReport.getVisit();
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("patientName", visit.getPatient().getPatientName());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}

					jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
					} else {
						jsonMap.put("rankName", "");
					}
					jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());
					jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					String visitstatus = "";
					if (StringUtils.isNotEmpty(visit.getExamStatus())) {
						if (visit.getExamStatus().equalsIgnoreCase("I")) {
							visitstatus = "Initiate";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("s")) {
							visitstatus = "Saved";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("af")
								|| masMedicalExamReport.getStatus().equalsIgnoreCase("f")) {
							visitstatus = "Forwarded";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("rj")) {
							visitstatus = "Rejected";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("pe")) {
							visitstatus = "Pending";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("ac")) {
							visitstatus = "Approved";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("rf")) {
							visitstatus = "Referred";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("po")) {
							visitstatus = "New";
						}

					}

					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());

					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());

					jsonMap.put("tokenNo", visit.getTokenNo());
					// jsonMap.put("employeeName", visit.getMasEmployee().getEmployeeName());
						String medicalCategory="";
					if (visit.getPatient() != null && visit.getPatient().getMedicalCategoryId() != null) {
						medicalCategory=visit.getPatient().getMasMedicalCategory().getMedicalCategoryName();
					}
					jsonMap.put("medicalCategory", medicalCategory);
					String approvedUSer = "";
					if (masMedicalExamReport.getUsersMA() != null
							&& masMedicalExamReport.getUsersMA().getFirstName() != null) {
						approvedUSer = masMedicalExamReport.getUsersMA().getFirstName();
					}
					if (masMedicalExamReport.getUsersMA() != null
							&& masMedicalExamReport.getUsersMA().getLastName() != null) {
						approvedUSer += " " + masMedicalExamReport.getUsersMA().getLastName();
					}
					if (jsondata.get("flagForList").toString().equalsIgnoreCase("c")) {

						jsonMap.put("approvedBy", approvedUSer);

					}
					String approvedMoUser = "";
					if (masMedicalExamReport.getUsersMO() != null
							&& masMedicalExamReport.getUsersMO().getFirstName() != null) {
						approvedMoUser = masMedicalExamReport.getUsersMO().getFirstName();
					}
					if (masMedicalExamReport.getUsersMO() != null
							&& masMedicalExamReport.getUsersMO().getLastName() != null) {
						approvedMoUser += " " + masMedicalExamReport.getUsersMO().getLastName();
					}
					if (jsondata.get("flagForList").toString().equalsIgnoreCase("d")) {
						jsonMap.put("approvedBy", approvedMoUser);

					}

					String approvedRMoUser = "";
					if (masMedicalExamReport.getUsersCM() != null
							&& masMedicalExamReport.getUsersCM().getFirstName() != null) {
						approvedRMoUser = masMedicalExamReport.getUsersCM().getFirstName();
					}
					if (masMedicalExamReport.getUsersCM() != null
							&& masMedicalExamReport.getUsersCM().getLastName() != null) {
						approvedRMoUser += " " + masMedicalExamReport.getUsersMO().getLastName();
					}
					if (jsondata.get("flagForList").toString().equalsIgnoreCase("e")) {
						jsonMap.put("approvedBy", approvedRMoUser);

					}

					if (masMedicalExamReport.getMediceExamDate() != null) {
						String examDate = HMSUtil.convertDateToStringFormat(masMedicalExamReport.getMediceExamDate(), "dd/MM/yyyy");
						//String examDate = HMSUtil.getDateWithoutTime(masMedicalExamReport.getMediceExamDate());
						jsonMap.put("mediceExamDate", examDate);
					}
					if(visit.getPatient()!=null && visit.getPatient().getFitFlag() !=null )
					{
						jsonMap.put("fitFlag",visit.getPatient().getFitFlag());
					}
					else {
						jsonMap.put("fitFlag","");
					}
					responseList.add(jsonMap);
				}
				if (responseList != null && responseList.size() > 0) {
					json.put("data", responseList);
					json.put("status", 1);
					json.put("count", count);

				} else {
					json.put("data", responseList);
					json.put("msg", "Data not found");
					json.put("status", 0);
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
		return json.toString();
	}

	*//**
	 * @Description:Method is used for getPatientDetailOfVisitId;
	 * @param:HashMap<String, Object>
	 *                            jsondata
	 * @param: HttpServletRequest
	 *             request
	 * @param:HttpServletResponse response
	 *//*

	@Override
	@Transactional
	public String getPatientDetailOfVisitId(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<MasServiceType>listMasServiceType=null;
		try {
			Map<String, Object> mapObject = medicalExamDAO.getValidateMedicalExamDetails(jsondata);

			List<Visit> listVisit = (List<Visit>) mapObject.get("listVisit");
			listMasServiceType=masterDao.getServiceTypeList();
			OpdObesityHd	opdObesityHd =null;
			
				if(jsondata.get("patientId")!=null) {
					List<OpdObesityHd> getObsistyDetails = opdDao.getObsisityRecord(Long.parseLong(jsondata.get("patientId").toString()));
					if(CollectionUtils.isNotEmpty(getObsistyDetails)) {
						opdObesityHd=getObsistyDetails.get(0);
					}
				}
				 	 
			if (CollectionUtils.isNotEmpty(listVisit)) {
				for (Visit visit : listVisit) {
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("patientName", visit.getPatient().getPatientName());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getMasAdministrativeSex() != null) {
						jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						jsonMap.put("genderId", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
					}
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
						jsonMap.put("rankId", visit.getPatient().getMasRank().getRankId());
					} else {
						jsonMap.put("rankName", "");
						jsonMap.put("rankId", "");
					}
					jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());
					jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					String visitstatus = "";
					MasMedicalExamReport masMedicalExamReport = null;
					masMedicalExamReport = visit.getMasMedicalExamReport();
					if (masMedicalExamReport == null) {
						visitstatus = "New";
					}

					if (masMedicalExamReport != null && visit.getMasMedicalExamReport().getStatus()!=null) {

						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("s")) {
							visitstatus = "Saved";
						}

						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("af")
								|| visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("f")) {
							visitstatus = "Forwarded";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rj")) {
							visitstatus = "Rejected";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("pe")) {
							visitstatus = "Pending";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("ac")) {
							visitstatus = "Approved";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("po")) {
							visitstatus = "New";
						}
					}
					else {
						visitstatus = "New";
					}
					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());

					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());
					jsonMap.put("examTypeId", visit.getExamId());
					jsonMap.put("appointmentId", visit.getAppointmentTypeId());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						String dateOfBirth = HMSUtil.convertDateToStringFormat(visit.getPatient().getDateOfBirth(),
								"dd/MM/yyyy");

						jsonMap.put("dateOfBirth", dateOfBirth);
					}
					if (visit.getPatient() != null && visit.getPatient().getServiceJoinDate() != null) {

						Date s1 = visit.getPatient().getServiceJoinDate();
						Period serviceDate = ProjectUtils.getDOB(s1);
						jsonMap.put("totalService", serviceDate.getYears()+" years");
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getTotalService()!=null) {
						jsonMap.put("totalService",visit.getMasMedicalExamReport().getTotalService()+" years");
					}
					 
					
					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unit", visit.getPatient().getMasUnit().getUnitName());
					}
					if (visit.getPatient() != null && visit.getPatient().getMedicalCategoryId() != null) {
						jsonMap.put("medicalCategory",
								visit.getPatient().getMasMedicalCategory().getMedicalCategoryName());
						jsonMap.put("medicalCompositeNameValue", visit.getPatient().getMedicalCategoryId());
						String medicalCategoryDate = "";
						if (visit.getPatient().getMasMedicalCategory().getLastChgDate() != null) {
							medicalCategoryDate = HMSUtil.convertDateToStringFormat(
									visit.getPatient().getMasMedicalCategory().getLastChgDate(), "dd/MM/yyyy");
							jsonMap.put("medicalCategoryDate", medicalCategoryDate);
						} else {
							jsonMap.put("medicalCategoryDate", "");
						}

					} else {
						jsonMap.put("medicalCategory", "");
						jsonMap.put("medicalCompositeNameValue", "");
						jsonMap.put("medicalCategoryDate", "");
					}
					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null) {
						jsonMap.put("tradeBranch", visit.getPatient().getMasTrade().getTradeName());
					} else {
						jsonMap.put("tradeBranch", "");
					}
					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null) {
						jsonMap.put("branchOrTradeIdOn", visit.getPatient().getMasTrade().getTradeId());
					} else {
						jsonMap.put("branchOrTradeIdOn", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unitIdOn", visit.getPatient().getMasUnit().getUnitId());
					} else {
						jsonMap.put("unitIdOn", "");
					}
					String dateOfExam ="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMediceExamDate()!=null) {
						  dateOfExam = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getMediceExamDate(), "dd/MM/yyyy");
					}
					else {
					  dateOfExam = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
					
					}
					jsonMap.put("dateOfExam", dateOfExam);
					//String dateOfExam = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
					//jsonMap.put("dateOfExam", dateOfExam);

					String dateOfPatient = "";
					if (visit.getMasMedicalExamReport()!=null &&  visit.getMasMedicalExamReport().getDateofcommun() != null) {
						dateOfPatient = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getDateofcommun(),
								"dd/MM/yyyy");
					}

					jsonMap.put("dateOfPatient", dateOfPatient);
					
					
					if (visit.getOpdPatientDetails() != null
							&& visit.getOpdPatientDetails().getOpdPatientDetailsId() != null) {
						jsonMap.put("opdPatientDetailId", visit.getOpdPatientDetails().getOpdPatientDetailsId());
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getCns())) {
						jsonMap.put("cns", visit.getOpdPatientDetails().getCns());
						}
						else {
							jsonMap.put("cns","");
						}
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getChestResp())) {
							jsonMap.put("chest", visit.getOpdPatientDetails().getChestResp());
							}
							else {
								jsonMap.put("chest","");
							}
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getMusculoskeletal())) {
							jsonMap.put("musculoskeletal", visit.getOpdPatientDetails().getMusculoskeletal());
							}
							else {
								jsonMap.put("musculoskeletal","");
							}
						
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getCvs())) {
							jsonMap.put("cvs", visit.getOpdPatientDetails().getCvs());
							}
							else {
								jsonMap.put("cvs","");
							}
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getSkin())) {
							jsonMap.put("skin", visit.getOpdPatientDetails().getSkin());
							}
							else {
								jsonMap.put("skin","");
							}
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getGi())) {
							jsonMap.put("gi", visit.getOpdPatientDetails().getGi());
							}
							else {
								jsonMap.put("gi","");
							}
						
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getGenitoUrinary())) {
							jsonMap.put("genitoUrinary", visit.getOpdPatientDetails().getGenitoUrinary());
							}
							else {
								jsonMap.put("genitoUrinary","");
							}
						
						
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getSystemOther())) {
							jsonMap.put("sysOthers", visit.getOpdPatientDetails().getSystemOther());
							}
							else {
								jsonMap.put("sysOthers","");
							}
						if(visit.getPatient()!=null && visit.getPatient().getFitFlag() !=null )
						{
							jsonMap.put("fitFlag",visit.getPatient().getFitFlag());
						}
						else {
							jsonMap.put("fitFlag","");
						}
					} else {
						jsonMap.put("opdPatientDetailId", "");
						
						jsonMap.put("cns","");
						jsonMap.put("chest","");
						jsonMap.put("musculoskeletal","");
						jsonMap.put("cvs","");
						jsonMap.put("skin","");
						jsonMap.put("gi","");
						jsonMap.put("genitoUrinary","");
						jsonMap.put("sysOthers","");
					}

					if (visit.getMasMedicalExamReport() != null) {

						if (visit.getMasMedicalExamReport().getAuthority() != null) {
							jsonMap.put("authority", visit.getMasMedicalExamReport().getAuthority());
						} else {
							jsonMap.put("authority", "");
						}
						if (visit.getMasMedicalExamReport().getPlace() != null) {
							jsonMap.put("place", visit.getMasMedicalExamReport().getPlace());
						} else {
							jsonMap.put("place", "");
						}

					} else {
						jsonMap.put("authority", "");
						jsonMap.put("place", "");
					}
					String dateAME = "";
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDateMedicalBoardExam() != null) {
						dateAME = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDateMedicalBoardExam(), "dd/MM/yyyy");
						jsonMap.put("dateAME", dateAME);
					} else {
						jsonMap.put("dateAME", "");
					}

					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getApprovedBy() != null) {
						jsonMap.put("approvedBy", visit.getMasMedicalExamReport().getApprovedBy());
					} else {
						jsonMap.put("approvedBy", "");
					}
					Long forwardedDesignationId=null;
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getFowardedDesignationId() != null) {
						forwardedDesignationId=visit.getMasMedicalExamReport().getFowardedDesignationId();
					}
					jsonMap.put("forwardedDesignationId", forwardedDesignationId);
					
					
					String doeORDoc = "";
					Date dateOfCompletion=null;
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDateOfCompletion() != null) {
						doeORDoc = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDateOfCompletion(), "dd/MM/yyyy");
						dateOfCompletion=visit.getMasMedicalExamReport().getDateOfCompletion();
						jsonMap.put("doeORDoc", doeORDoc);
						jsonMap.put("serviceJoinDate", doeORDoc);
					} 
					else if (visit.getPatient() != null && visit.getPatient().getServiceJoinDate() != null) {
						doeORDoc = HMSUtil.convertDateToStringFormat(
								visit.getPatient().getServiceJoinDate(), "dd/MM/yyyy");
						dateOfCompletion=visit.getPatient().getServiceJoinDate();
						jsonMap.put("doeORDoc", doeORDoc);
						jsonMap.put("serviceJoinDate", doeORDoc);
					}
					else {
						jsonMap.put("doeORDoc", "");
						jsonMap.put("serviceJoinDate", "");
					}
					
					Date dateOfMedicalExamVal=null;
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMediceExamDate()!=null) {
						dateOfMedicalExamVal=visit.getMasMedicalExamReport().getMediceExamDate();
					}
					else {
						dateOfMedicalExamVal=visit.getMasMedicalExamReport().getDaterelease();
					}
					 
					if(dateOfMedicalExamVal!=null  && dateOfCompletion!=null) {
						String totalService=HMSUtil.getTotalServiceDateBetweenTwoDate(dateOfMedicalExamVal,dateOfCompletion);
						jsonMap.put("totalService",totalService);
					}
					
					
					
					if(visit.getPatient()!=null && visit.getPatient().getMasEmployeeCategory()!=null)
					{
						jsonMap.put("masEmployeeCategory",visit.getPatient().getMasEmployeeCategory().getEmployeeCategoryName());
					}
					else {
						jsonMap.put("masEmployeeCategory","");
					}
					 
					if(visit.getPatient() != null && visit.getPatient().getMasrelation() != null)
					{
						jsonMap.put("relation", visit.getPatient().getMasrelation().getRelationName());
					}
					else
					{
						jsonMap.put("relation", "");
					}
					if(visit.getPatient()!=null && visit.getPatient().getFitFlag() !=null )
					{
						jsonMap.put("fitFlag",visit.getPatient().getFitFlag());
					}
					else {
						jsonMap.put("fitFlag","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getTypeofcommision() !=null )
					{
						jsonMap.put("typeOfcommision",visit.getMasMedicalExamReport().getTypeofcommision());
					}
					else {
						jsonMap.put("typeOfcommision","");
					}
					
					Long serviceType = null;
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getServiceTypeId() != null) {
						serviceType=visit.getMasMedicalExamReport().getServiceTypeId();
						
					}  
					jsonMap.put("serviceType", serviceType);
					
					if(opdObesityHd!=null) {
					if(opdObesityHd!=null && opdObesityHd.getCloseDate()!=null) {
						jsonMap.put("obsistyCloseDate", opdObesityHd.getCloseDate());
					}
					else {
						jsonMap.put("obsistyCloseDate", "");
					}
					
					if(opdObesityHd!=null && opdObesityHd.getOverweightFlag()!=null)
					{	
						jsonMap.put("obesityOverWeightFlag",opdObesityHd.getOverweightFlag());
					}
					else
					{
						jsonMap.put("obesityOverWeightFlag","");	
					}
					}
					
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getSignatureOfOfficer() !=null )
					{
						jsonMap.put("signatureOfOfficer",visit.getMasMedicalExamReport().getSignatureOfOfficer());
					}
					else {
						jsonMap.put("signatureOfOfficer","");
					}
		 		
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getRidcId() !=null )
					{
						jsonMap.put("ridcId",visit.getMasMedicalExamReport().getRidcId());
					}
					else {
						jsonMap.put("ridcId","");
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMeRidcId() !=null )
					{
						jsonMap.put("meRidcId",visit.getMasMedicalExamReport().getMeRidcId());
					}
					else {
						jsonMap.put("meRidcId","");
					}
					if(visit.getRidcId()!=null)
					{
						jsonMap.put("meRidcIdForVisit",visit.getRidcId());
					}
					else {
						jsonMap.put("meRidcIdForVisit","");
					}
					
					
					String meAgeNew="";
				//	if(masMedicalExamReport!=null  && masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getPatient()!=null && masMedicalExamReport.getVisit().getPatient().getDateOfBirth()!=null) {
					if(masMedicalExamReport!=null  && masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getMeAge()!=null) {
					//Date s =masMedicalExamReport.getVisit().getPatient().getDateOfBirth();
						meAgeNew=getAgeAtTimeOfMe(masMedicalExamReport.getVisit().getMeAge());//HMSUtil.calculateAge(s);
					}
					else {
						meAgeNew="";
					}
					jsonMap.put("meAgeNew",meAgeNew);
					
					c.add(jsonMap);
				}
				if (c != null && c.size() > 0) {
					json.put("data", c);
					json.put("listMasServiceType", listMasServiceType);
					json.put("msg", "Data  found");
					json.put("status", 1);

				} else {
					json.put("data", c);
					json.put("listMasServiceType", listMasServiceType);
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			} else {
				try {
					json.put("msg", "Visit ID data not found");
					json.put("status", 0);
					json.put("listMasServiceType", listMasServiceType);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			json.put("msg", "Visit ID data not found");
			json.put("status", 0);
			json.put("listMasServiceType", listMasServiceType);
			e.printStackTrace();
		}
		return json.toString();

	}

	*//**
	 * @Description:Method is used for getUnitDetail
	 * @param:HashMap<String, Object>
	 *                            jsondata
	 * @param:HttpServletRequest request
	 * @param:HttpServletResponse response
	 *//*
	@Override
	public String getUnitDetail(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		try {
			List<MasHospital> listMasHospital = medicalExamDAO.getMasHospitalList(jsondata);

			if (CollectionUtils.isNotEmpty(listMasHospital)) {
				for (MasHospital masHospital : listMasHospital) {
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("hospitalId", masHospital.getHospitalId());
					jsonMap.put("hospitalName", masHospital.getHospitalName());
					// jsonMap.put("unitId", masHospital.getMasUnit().getUnitId());

					c.add(jsonMap);
				}
				if (c != null && c.size() > 0) {
					json.put("dataMasHospital", c);
					json.put("status", 1);

				} else {
					json.put("data", c);
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			} else {
				try {
					json.put("msg", "Hospital  data not found");
					json.put("status", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	*//**
	 * @Description:Method is used for getApprovalListByFlag
	 * @param:HashMap<String, Object>
	 *                            jsondata
	 * @param: HttpServletRequest
	 *             request
	 * @param:HttpServletResponse response
	 *//*
	@Override
	public String getApprovalListByFlag(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<MasMedicalExamReport> listMasMedicalExamReport = null;
		Map<String, Object> map = null;
		List<Object[]>listRHQObject=null;
		List<MasHospital>listMasHospital=null;
		int countForSeacrh=0;
		if (!jsondata.isEmpty()) {
			map = medicalExamDAO.getMedicalExamListByStatus(jsondata);
			int count = (int) map.get("count");
			listMasMedicalExamReport = (List<MasMedicalExamReport>) map.get("listMasMedicalExamReport");
			listRHQObject=medicalExamDAO.getAllRHQ();
			listMasHospital=medicalExamDAO.getRMOList(jsondata);
			
			if (CollectionUtils.isNotEmpty(listMasMedicalExamReport)) {
			
				String codeME = HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_ME");
				String codeMB = HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_MB");
				String meMbFlag=jsondata.get("membFlab").toString();
				String rhqDropDown="";
				String designationName="";
				if(jsondata.containsKey("rhqDropDown") && jsondata.get("rhqDropDown")!=null)
				  rhqDropDown=jsondata.get("rhqDropDown").toString();
				
				if(jsondata.containsKey("designationName") && jsondata.get("designationName")!=null)
				  designationName=jsondata.get("designationName").toString();
				
				for (MasMedicalExamReport masMedicalExamReport : listMasMedicalExamReport) {
					
					Visit visit = masMedicalExamReport.getVisit(); 	
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("patientName", visit.getPatient().getPatientName());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}

					jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
						jsonMap.put("rankId", visit.getPatient().getMasRank().getRankId());
					} else {
						jsonMap.put("rankName", "");
						jsonMap.put("rankId", "");
					}
					jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());
					jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					String visitstatus = "";
					if (StringUtils.isNotEmpty(visit.getExamStatus())) {
						if (visit.getExamStatus().equalsIgnoreCase("I")) {
							visitstatus = "Initiated";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("s")) {
							visitstatus = "Saved";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("af")
								|| masMedicalExamReport.getStatus().equalsIgnoreCase("f")) {
							visitstatus = "Forwarded";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("rj")) {
							visitstatus = "Rejected";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("pe")) {
							visitstatus = "Pending";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("ac")) {
							visitstatus = "Approved";
						}
						if (masMedicalExamReport.getStatus().equalsIgnoreCase("rf")) {
							visitstatus = "Referred";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("po")) {
							visitstatus = "New";
						}
					}

					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());
					if (visit.getMasMedicalExamReport() != null && visit.getMasMedicalExamReport().getApprovedBy() != null) {
						jsonMap.put("approvedBy", visit.getMasMedicalExamReport().getApprovedBy());
					} else {
						jsonMap.put("approvedBy", "");
					}
					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());

					jsonMap.put("tokenNo", visit.getTokenNo());
					// jsonMap.put("employeeName", visit.getMasEmployee().getEmployeeName());
					String medicalCategory="";
					if (visit.getPatient() != null && visit.getPatient().getMedicalCategoryId() != null) {
						medicalCategory=visit.getPatient().getMasMedicalCategory().getMedicalCategoryName();
					}
					
					
					if(codeME.equalsIgnoreCase(meMbFlag)) {
					if(visit.getPatient()!=null && visit.getPatient().getMedicalCategoryId() == null) {
						List<PatientMedicalCat>listPatientMedicalCat=medicalExamDAO.getPatientMedicalCat(visit.getPatient().getPatientId(),visit.getVisitId(),"visit");
						if(CollectionUtils.isNotEmpty(listPatientMedicalCat))
						for(PatientMedicalCat pmc:listPatientMedicalCat) {
							if(StringUtils.isEmpty(medicalCategory))
							medicalCategory=pmc.getMasMedicalCategory().getMedicalCategoryName();
							else {
									medicalCategory+=","+pmc.getMasMedicalCategory().getMedicalCategoryName();
								}
							}
						}
					
					}
					
					if(codeMB.equalsIgnoreCase(meMbFlag)) {
						if(visit.getPatient()!=null && visit.getPatient().getMedicalCategoryId() == null) {
							List<PatientMedicalCat>listPatientMedicalCat=medicalExamDAO.getPatientMedicalCat(visit.getPatient().getPatientId(),visit.getVisitId(),"mbStatus");
							if(CollectionUtils.isNotEmpty(listPatientMedicalCat))
							for(PatientMedicalCat pmc:listPatientMedicalCat) {
								if(StringUtils.isEmpty(medicalCategory) && pmc.getMasMedicalCategory()!=null && pmc.getMasMedicalCategory().getMedicalCategoryName()!="")
								medicalCategory=pmc.getMasMedicalCategory().getMedicalCategoryName();
								else if(pmc.getMasMedicalCategory()!=null && pmc.getMasMedicalCategory().getMedicalCategoryName()!=""){
										medicalCategory+=","+pmc.getMasMedicalCategory().getMedicalCategoryName();
									}
								}
							}
						
						}
					
					
					jsonMap.put("medicalCategory",medicalCategory);
							 
					String moUser = "";
					String rMoUser = "";
					if (masMedicalExamReport.getUsersMO() != null
							&& masMedicalExamReport.getUsersMO().getFirstName() != null) {
						moUser = masMedicalExamReport.getUsersMO().getFirstName();
					}
					if (masMedicalExamReport.getUsersMO() != null
							&& masMedicalExamReport.getUsersMO().getLastName() != null) {
						moUser += "" + masMedicalExamReport.getUsersMO().getLastName();
					}
					if (masMedicalExamReport != null && masMedicalExamReport.getUsersMO() != null) {
						jsonMap.put("moUser", moUser);
					} else {
						jsonMap.put("moUser", "");
					}

					if (masMedicalExamReport.getUsersCM() != null
							&& masMedicalExamReport.getUsersCM().getFirstName() != null) {
						rMoUser = masMedicalExamReport.getUsersCM().getFirstName();
					}
					if (masMedicalExamReport.getUsersCM() != null
							&& masMedicalExamReport.getUsersCM().getLastName() != null) {
						rMoUser += "" + masMedicalExamReport.getUsersCM().getLastName();
					}

					if (masMedicalExamReport != null && masMedicalExamReport.getUsersCM() != null) {
						jsonMap.put("rMoUser", rMoUser);
					} else {
						jsonMap.put("rMoUser", "");
					}

					 
					String examDate = "";
					if (masMedicalExamReport.getMoDate() != null) {
						examDate = HMSUtil.convertDateToStringFormat(masMedicalExamReport.getMoDate(),
								"dd/MM/yyyy");
						
					}
					else if(masMedicalExamReport.getMediceExamDate()!=null){
						examDate = HMSUtil.convertDateToStringFormat(masMedicalExamReport.getMediceExamDate(),
								"dd/MM/yyyy");
					}
					else
					{
						examDate ="";
					}
					
					jsonMap.put("mediceExamDate", examDate);
					String meAgeNew="";
					//if(masMedicalExamReport!=null  && masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getPatient()!=null && masMedicalExamReport.getVisit().getPatient().getDateOfBirth()!=null) {
					if(masMedicalExamReport!=null  && masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getMeAge()!=null ) {		
					//Date s =masMedicalExamReport.getVisit().getPatient().getDateOfBirth();
						meAgeNew= getAgeAtTimeOfMe(masMedicalExamReport.getVisit().getMeAge());//HMSUtil.calculateAge(s);
					}
					else {
						meAgeNew="";
					}
					jsonMap.put("meAgeNew",meAgeNew);
					String rhqValue="";
					if(masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getVisitId()!=null && masMedicalExamReport.getForwardUnitId()!=null) {
					  rhqValue=medicalExamDAO.getRHQByVisitId(masMedicalExamReport.getVisit().getVisitId(),masMedicalExamReport.getForwardUnitId());
					}
					else {
						rhqValue="";
					}
					jsonMap.put("rhqValue",rhqValue);
					
					String miRoom="";
					if(masMedicalExamReport.getMasHospital()!=null) {
						miRoom=masMedicalExamReport.getMasHospital().getHospitalName();
					}
					else {
						miRoom="";	 
					}
					
					jsonMap.put("miRoom",miRoom);
					
					if(StringUtils.isNotEmpty(designationName) && rhqDropDown!=null && !rhqDropDown.equalsIgnoreCase("0")){
						Long hospitalId=null;
						if(rhqDropDown!=null && !rhqDropDown.equalsIgnoreCase("0")) {
						  hospitalId=Long.parseLong(rhqDropDown.trim());
						if(designationName.equalsIgnoreCase("RMO") && masMedicalExamReport.getMasHospital()!=null 
								&& masMedicalExamReport.getMasHospital().getHospitalId()!=null && masMedicalExamReport.getMasHospital().getHospitalId().toString().equalsIgnoreCase(hospitalId.toString())) {
							countForSeacrh++;
							responseList.add(jsonMap);
							
						}
						else if(designationName.equalsIgnoreCase("PDMS") && masMedicalExamReport.getForwardUnitId()!=null 
								&&  masMedicalExamReport.getForwardUnitId().toString().equalsIgnoreCase(hospitalId.toString())) {
							countForSeacrh++;
							responseList.add(jsonMap);
						}
						}
					}
					else {
					responseList.add(jsonMap);
					}
				}
					if(countForSeacrh!=0) {
						count=countForSeacrh;
					}
				if (responseList != null && responseList.size() > 0) {
					json.put("data", responseList);
					json.put("status", 1);
					json.put("count", count);
					json.put("listRHQObject", listRHQObject);
					json.put("listMasHospital", listMasHospital);

				} else {
					json.put("data", responseList);
					json.put("msg", "Data not found");
					json.put("status", 0);
					json.put("listRHQObject", listRHQObject);
					json.put("listMasHospital", listMasHospital);
				}
				 
				
			} else {
				try {
					json.put("msg", "Visit ID data not found");
					json.put("status", 0);
					json.put("listRHQObject", listRHQObject);
					json.put("listMasHospital", listMasHospital);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		return json.toString();

	}

	*//**
	 * @Description:Method is used for getPatientReferalDetailMe
	 * @param:HashMap<String, String>
	 *                            jsondata
	 * @param: HttpServletRequest
	 *             request
	 * @param:HttpServletResponse response
	 *//*

	@Override
	public String getPatientReferalDetailMe(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<MasEmpanelledHospital> masEmpanelledHospitalList = null;
		List<MasIcd> listMasIcd = null;
		Long visitId = null;
		Long patientId = null;
		Long opdPatientDetailId = null;
		if (!jsondata.isEmpty()) {
			List<Object[]> listReferralPatientDt = null;
			masEmpanelledHospitalList = md.getEmpanelledHospital(jsondata);
			//List<MasDepartment> departmentList = masterDao.getDepartmentsList();
			List<MasSpeciality> departmentList= md.getSpecialistList(null);
			if (jsondata.get("opdPatientDetailId") != null && jsondata.get("opdPatientDetailId") != "") {
				listReferralPatientDt = dgOrderhdDao
						.getReferralPatientDtList(Long.parseLong(jsondata.get("opdPatientDetailId").toString()));
				try {
					if (jsondata.get("visitId") != null) {
						visitId = Long.parseLong(jsondata.get("visitId").toString());
					}
					if (jsondata.get("opdPatientDetailId") != null) {
						opdPatientDetailId = Long.parseLong(jsondata.get("opdPatientDetailId").toString());
					}

					if (jsondata.get("patientId") != null) {
						patientId = Long.parseLong(jsondata.get("patientId").toString());
					}
					listMasIcd = dgOrderhdDao.getMasIcdByVisitPatAndOpdPD(visitId, patientId, opdPatientDetailId);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (listReferralPatientDt != null) {
				try {

					for (Iterator<?> it = listReferralPatientDt.iterator(); it.hasNext();) {

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
						String masCode = "";
						String referalNotes = "";
						String referalDate = "";
						Long intDepartmentId = null;
						Long intHospitalId = null;
						Long ridcId=null;
						String finalNotes="";
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

						if (row[4] != null) {
							diagonisId = Long.parseLong(row[4].toString());
						}

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
							//referalDate = HMSUtil.getDateWithoutTime(dd1);
							  referalDate = HMSUtil.convertDateToStringFormat(dd1, "dd/MM/yyyy");
						}

						if (row[14] != null) {
							intDepartmentId = Long.parseLong(row[14].toString());
						}
						if (row[15] != null) {
							intHospitalId = Long.parseLong(row[15].toString());
						}
						if (row[16] != null) {
							ridcId= Long.parseLong(row[16].toString());
						}
						if (row[12] != null) {
							finalNotes = row[12].toString();
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
						pt.put("ridcId", ridcId);
						pt.put("finalNotes", finalNotes);
						
						c.add(pt);
					}
					json.put("listReferralPatientDt", c);

					json.put("masEmpanelledHospitalList", masEmpanelledHospitalList);
					json.put("departmentList", departmentList);

					json.put("listMasIcd", listMasIcd);
					json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
					json.put("status", "1");
				}

				catch (Exception e) {
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}
			}
			if (listReferralPatientDt == null) {
				json.put("masEmpanelledHospitalList", masEmpanelledHospitalList);
				json.put("departmentList", departmentList);

				json.put("listMasIcd", listMasIcd);
				json.put("msg", "OPD Patients Visit List  get  sucessfull... ");
				json.put("status", "1");
			}

			else {
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

	*//**
	 * @Description:Method is used for get getImmunisationHistory();
	 * @param:HashMap<String, Object>
	 *                            jsondata
	 * @param:HttpServletRequest request
	 * @param:HttpServletResponse response
	 *//*
	@Override
	public String getImmunisationHistory(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		int count=0;
		try {
			Map<String, Object> mapPatientImmunizationHistor=null;
			List<PatientImmunizationHistory> listPatientImmunizationHistor=null;
			if(jsondata.containsKey("visitId") && jsondata.get("visitId")!=null && StringUtils.isNotEmpty(jsondata.get("visitId").toString())) {
			mapPatientImmunizationHistor = medicalExamDAO
					.getPatientImmunizationHistoryByVisitId(Long.parseLong(jsondata.get("visitId").toString()), jsondata);
				count = (int) mapPatientImmunizationHistor.get("count");
			  listPatientImmunizationHistor = (List<PatientImmunizationHistory>) mapPatientImmunizationHistor.get("listPatientImmunizationHistory");
			  
			  if(jsondata.containsKey("flagForForm") && jsondata.get("flagForForm")!=null && jsondata.get("flagForForm").toString().equalsIgnoreCase("f")) {
				  jsondata.put("flagForForm", "me");
				  mapPatientImmunizationHistor = medicalExamDAO
							.getPatientImmunizationHistoryByVisitId(Long.parseLong(jsondata.get("visitId").toString()), jsondata);
				  
				  List<PatientImmunizationHistory> listPatientImmunizationHistor2 = (List<PatientImmunizationHistory>) mapPatientImmunizationHistor.get("listPatientImmunizationHistory");
				  
				  if(CollectionUtils.isNotEmpty(listPatientImmunizationHistor2) && CollectionUtils.isNotEmpty(listPatientImmunizationHistor)) {
					  if(CollectionUtils.isNotEmpty(listPatientImmunizationHistor2))
					  listPatientImmunizationHistor.addAll(listPatientImmunizationHistor2);
				  }
			  
			  }
			
			}
		
			if (CollectionUtils.isNotEmpty(listPatientImmunizationHistor)) {
				for (PatientImmunizationHistory patientImmunizationHistory : listPatientImmunizationHistor) {

					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("immunizationId", patientImmunizationHistory.getImmunizationId());
					jsonMap.put("itemId", patientImmunizationHistory.getMasStoreItem().getItemId());
					jsonMap.put("itemCode", patientImmunizationHistory.getMasStoreItem().getNomenclature());
					jsonMap.put("pvmsNo", patientImmunizationHistory.getMasStoreItem().getPvmsNo());
					String immunizationDateValue = "";
					if (patientImmunizationHistory.getImmunizationDate() != null) {
						immunizationDateValue = HMSUtil.convertDateToStringFormat(
								patientImmunizationHistory.getImmunizationDate(), "dd/MM/yyyy");
					} else {
						immunizationDateValue = "";
					}
					String duration="";
					if (patientImmunizationHistory.getDuration() != null) {
						duration =  patientImmunizationHistory.getDuration() ;
					} else {
						duration = "";
					}
					
					String immunizationNextDateValue = "";
					if (patientImmunizationHistory.getNextDueDate() != null) {
						immunizationNextDateValue = HMSUtil.convertDateToStringFormat(
								patientImmunizationHistory.getNextDueDate(), "dd/MM/yyyy");
					} else {
						immunizationNextDateValue = "";
					}
					
					String prescriptionDateValue = "";
					if (patientImmunizationHistory.getPrescriptionDate() != null) {
						prescriptionDateValue = HMSUtil.convertDateToStringFormat(
								patientImmunizationHistory.getPrescriptionDate(), "dd/MM/yyyy");
					} else {
						prescriptionDateValue = "";
					}
					jsonMap.put("prescriptionDateValue", prescriptionDateValue);
					jsonMap.put("immunizationDate", immunizationDateValue);
					jsonMap.put("duration", duration);
					
					jsonMap.put("immunizationNextDateValue", immunizationNextDateValue);
					c.add(jsonMap);
				}
				if (c != null && c.size() > 0) {
					json.put("listMasStoreItem", c);
					json.put("status", 1);
					json.put("count", count);

				} else {
					json.put("listMasStoreItem", c);
					json.put("msg", "Data not found");
					json.put("status", 0);
					json.put("count", count);
				}
			} else {
			//if(jsondata.containsKey("flagForForm") && jsondata.get("flagForForm")!=null && !jsondata.get("flagForForm").toString().equalsIgnoreCase("h")) {
				String itemId = UtilityServices.getValueOfPropByKey("itemId");
				String itemCode = UtilityServices.getValueOfPropByKey("itemCode");
				String[] itemIds = null;
				String[] itemCodes = null;
				String[] pvmsNoValue = null;
				String pvmsNo = UtilityServices.getValueOfPropByKey("pvmsNo");
				
				count=2;
				if (StringUtils.isNotEmpty(itemId)) {
					itemIds = itemId.split(",");
				}
				if (StringUtils.isNotEmpty(itemCode)) {
					itemCodes = itemCode.split(",");
				}
				if (StringUtils.isNotEmpty(pvmsNo)) {
					pvmsNoValue = pvmsNo.split(",");
				}
				List<Object[]>listMasStoreItem= medicalExamDAO.getMasStoreItemByItemCode();
				
				
				int i = 0;
				if(CollectionUtils.isNotEmpty(listMasStoreItem))
				for (Object obj : listMasStoreItem) {
					Object[]rows=(Object[]) obj;
					// MasStoreItem masStoreItem=new MasStoreItem();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					if(rows[0]!=null)
						jsonMap.put("itemId", rows[0]);
					else {
						jsonMap.put("itemId", "");
					}
					if(rows[1]!=null) {
						jsonMap.put("pvmsNo", rows[1]);
					}
					else {
						jsonMap.put("pvmsNo", "");
					}
					
					if(rows[2]!=null) {
						jsonMap.put("itemCode",rows[2]);
					}
					else {
						jsonMap.put("itemCode", "");
					}
					
					
					//jsonMap.put("itemCode", itemCodes[i]);
					jsonMap.put("immunizationDate", "");
					jsonMap.put("duration", "");
					
					jsonMap.put("immunizationNextDateValue", "");
					jsonMap.put("immunizationId", "");
					//jsonMap.put("pvmsNo", pvmsNoValue[i]);
					c.add(jsonMap);

					// masStoreItem.setItemId(Long.parseLong(item));
					// masStoreItem.setNomenclature(itemCodes[i]);
					// listMasStoreItem.add(masStoreItem);
					i++;
				}

				json.put("listMasStoreItem", c);
				json.put("msg", "Immunisation history not avilable");
				json.put("status", 0);
			}
		//}
			if (c != null && c.size() > 0) {
				json.put("listMasStoreItem", c);
				json.put("status", 1);
				json.put("count", count);

			} else {
				json.put("listMasStoreItem", c);
				json.put("msg", "Data not found");
				json.put("status", 0);
				json.put("count", count);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();

	}

	@Override
	public String getMEWaitingGridAFMS18(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<Visit> listVisit = null;
		Map<String, Object> map = null;
		if (!jsondata.isEmpty()) {
			map = medicalExamDAO.getMEWaitingGridAFMS18(jsondata);
			int count = (int) map.get("count");
			listVisit = (List<Visit>) map.get("listVisit");
			
			if (CollectionUtils.isNotEmpty(listVisit)) {
				if(CollectionUtils.isNotEmpty(listVisit) && ((listVisit.get(0).getMasMedicalExamReport()==null || (listVisit.get(0).getMasMedicalExamReport()!=null && (listVisit.get(0).getMasMedicalExamReport().getStatus()==null || listVisit.get(0).getMasMedicalExamReport().getStatus().equalsIgnoreCase("")))))) {
				for (Visit visit : listVisit) {
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					if(visit.getPatient()!=null && StringUtils.isNotEmpty(visit.getPatient().getPatientName())) {
						jsonMap.put("patientName", visit.getPatient().getPatientName());
					}
					else {
					jsonMap.put("patientName", "");
					}
					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}
					String meAgeNew="";
					//if(visit!=null  &&   visit.getPatient()!=null &&visit.getPatient().getDateOfBirth()!=null) {
					if(visit!=null  &&   visit.getMeAge()!=null) {	
				//	Date s =visit.getPatient().getDateOfBirth();
						meAgeNew=getAgeAtTimeOfMe(visit.getMeAge());//HMSUtil.calculateAge(s);
					}
					else {
						meAgeNew="";
					}
					jsonMap.put("meAgeNew",meAgeNew);
					
					if(visit.getPatient()!=null && visit.getPatient().getMasAdministrativeSex()!=null
							&& visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName()!=null) {
						jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
					}
					else {
						jsonMap.put("gender", "");
					}
					
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
						jsonMap.put("rankId", visit.getPatient().getMasRank().getRankId());
					} else {
						jsonMap.put("rankName", "");
						jsonMap.put("rankId", "");
					}
					if(visit.getMasMedExam()!=null && StringUtils.isNotEmpty(visit.getMasMedExam().getMedicalExamName())) {
						jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());	
					}
					else {
						jsonMap.put("meTypeName", "");	
					}
					if(visit.getMasMedExam()!=null && StringUtils.isNotEmpty(visit.getMasMedExam().getMedicalExamCode())) {
						jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					}
					else {
						jsonMap.put("meTypeCode", "");
					}
					
					String visitstatus = "";

					if (jsondata.get("flagForList").toString().equalsIgnoreCase("a")) {
						if (visit.getExamStatus().equalsIgnoreCase("I")) {
							visitstatus = "Initiated";
						}
					} else {
						MasMedicalExamReport masMedicalExamReport = null;
						masMedicalExamReport = visit.getMasMedicalExamReport();
						if (masMedicalExamReport == null) {
							visitstatus = "New";
						}

						if (masMedicalExamReport != null && visit.getMasMedicalExamReport().getStatus()!=null) {

							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("s")) {
								visitstatus = "Saved";
							}

							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("af")
									|| visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("f")) {
								visitstatus = "Forwarded";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rj")) {
								visitstatus = "Rejected";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("pe")) {
								visitstatus = "Pending";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("ac")) {
								visitstatus = "Approved";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rf")) {
								visitstatus = "Referred";
							}
							if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("po")) {
								visitstatus = "New";
							}
						}
						else {
							visitstatus = "New";
						}
					}
					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());

					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());

					if (visit.getOpdPatientDetails() != null
							&& visit.getOpdPatientDetails().getOpdPatientDetailsId() != null) {
						jsonMap.put("opdPatientDetailId", visit.getOpdPatientDetails().getOpdPatientDetailsId());
					} else {
						jsonMap.put("opdPatientDetailId", "");
					}

					jsonMap.put("tokenNo", visit.getTokenNo());
					responseList.add(jsonMap);
				}
				if (responseList != null && responseList.size() > 0) {
					json.put("data", responseList);
					json.put("status", 1);
					json.put("count", count);

				} else {
					json.put("data", responseList);
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			} 
			
				else {
					try {
						json.put("msg", "Patient is already Submitted Data.");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}	
				
			}
			else {
				try {
					json.put("msg", "Service Number not associate with Patient.");
					json.put("status", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			

		}
		return json.toString();
	}

	@Override
	public String getPatientDetailOfVisitIdAfms18(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		try {
			Map<String, Object> mapObject = medicalExamDAO.getpatientDetailForAFMS18(jsondata);

			List<Visit> listVisit = (List<Visit>) mapObject.get("listVisit");
			List<MasServiceType>listMasServiceType=masterDao.getServiceTypeList();
			OpdObesityHd opdObesityHd=null;
			if(jsondata.get("patientId")!=null) {
				List<OpdObesityHd> getObsistyDetails = opdDao.getObsisityRecord(Long.parseLong(jsondata.get("patientId").toString()));
				if(CollectionUtils.isNotEmpty(getObsistyDetails)) {
					opdObesityHd=getObsistyDetails.get(0);
				}
			}
			Users users=null;
			try {
				users= medicalExamDAO.getUserByUserId(Long.parseLong(jsondata.get("userId").toString()));
			}
			
			catch(Exception e) {
				e.printStackTrace();
			}
			List<MasEmployee> listMasEmployee=null;
			if(users!=null)
				  listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(users.getServiceNo());
			if (CollectionUtils.isNotEmpty(listVisit)) {
				for (Visit visit : listVisit) {
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("patientName", visit.getPatient().getPatientName());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getMasAdministrativeSex() != null) {
						jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						jsonMap.put("genderId", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
					}
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
						jsonMap.put("rankId", visit.getPatient().getMasRank().getRankId());
					} else {
						jsonMap.put("rankName", "");
						jsonMap.put("rankId", "");
					}
					jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());
					jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					String visitstatus = "";
					MasMedicalExamReport masMedicalExamReport = null;
					masMedicalExamReport = visit.getMasMedicalExamReport();
					if (masMedicalExamReport == null) {
						visitstatus = "New";
					}

					if (masMedicalExamReport != null && visit.getMasMedicalExamReport().getStatus()!=null) {

						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("s")) {
							visitstatus = "Saved";
						}

						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("af")
								|| visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("f")) {
							visitstatus = "Forwarded";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rj")) {
							visitstatus = "Rejected";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("pe")) {
							visitstatus = "Pending";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("ac")) {
							visitstatus = "Approved";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("po")) {
							visitstatus = "New";
						}
					}
					else {
						visitstatus = "New";
					}
					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());

					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());
					jsonMap.put("examTypeId", visit.getExamId());
					jsonMap.put("appointmentId", visit.getAppointmentTypeId());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						String dateOfBirth = HMSUtil.convertDateToStringFormat(visit.getPatient().getDateOfBirth(),
								"dd/MM/yyyy");

						jsonMap.put("dateOfBirth", dateOfBirth);
					}
					 
					if (visit.getPatient() != null && visit.getPatient().getServiceJoinDate() != null) {

						Date s1 = visit.getPatient().getServiceJoinDate();
						Period serviceDate = ProjectUtils.getDOB(s1);
						jsonMap.put("totalService", serviceDate.getYears()+" years");
					} 
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getTotalService()!=null) {
						jsonMap.put("totalService",visit.getMasMedicalExamReport().getTotalService()+" years");
					}
					 
					
					
					
					
					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unit", visit.getPatient().getMasUnit().getUnitName());
					}
					 
					if (visit.getPatient() != null && visit.getPatient().getMedicalCategoryId() != null && StringUtils.isNotEmpty(visit.getPatient().getMasMedicalCategory().getMedicalCategoryName())) {
						jsonMap.put("medicalCategory",
								visit.getPatient().getMasMedicalCategory().getMedicalCategoryName());
						jsonMap.put("medicalCompositeNameValue", visit.getPatient().getMedicalCategoryId());
						String medicalCategoryDate = "";
						if (visit.getPatient().getMasMedicalCategory().getLastChgDate() != null) {
							medicalCategoryDate = HMSUtil.convertDateToStringFormat(
									visit.getPatient().getMasMedicalCategory().getLastChgDate(), "dd/MM/yyyy");
							jsonMap.put("medicalCategoryDate", medicalCategoryDate);
						} else {
							jsonMap.put("medicalCategoryDate", "");
						}

					} else {
						jsonMap.put("medicalCategory", "");
						jsonMap.put("medicalCompositeNameValue", "");
						jsonMap.put("medicalCategoryDate", "");
					}
					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null && StringUtils.isNotEmpty(visit.getPatient().getMasTrade().getTradeName())) {
						jsonMap.put("tradeBranch", visit.getPatient().getMasTrade().getTradeName());
					} else {
						jsonMap.put("tradeBranch", "");
					}
					
					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null) {
						jsonMap.put("branchOrTradeIdOn", visit.getPatient().getMasTrade().getTradeId());
					} else {
						jsonMap.put("branchOrTradeIdOn", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unitIdOn", visit.getPatient().getMasUnit().getUnitId());
					} else {
						jsonMap.put("unitIdOn", "");
					}
					
					String dateOfExam ="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMediceExamDate()!=null) {
						  dateOfExam = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getMediceExamDate(), "dd/MM/yyyy");
					}
					else {
					  dateOfExam = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
					
					}
					jsonMap.put("dateOfExam", dateOfExam);
					
					
					String dateOfPatient = "";
					if (visit.getMasMedicalExamReport()!=null &&  visit.getMasMedicalExamReport().getDateofcommun() != null) {
						dateOfPatient = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getDateofcommun(),
								"dd/MM/yyyy");
					}

					jsonMap.put("dateOfPatient", dateOfPatient);
					
					
					
					if (visit.getOpdPatientDetails() != null
							&& visit.getOpdPatientDetails().getOpdPatientDetailsId() != null) {
						jsonMap.put("opdPatientDetailId", visit.getOpdPatientDetails().getOpdPatientDetailsId());
						
						if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getCns())) {
							jsonMap.put("cns", visit.getOpdPatientDetails().getCns());
							}
							else {
								jsonMap.put("cns","");
							}
							if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getChestResp())) {
								jsonMap.put("chest", visit.getOpdPatientDetails().getChestResp());
								}
								else {
									jsonMap.put("chest","");
								}
							
							if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getMusculoskeletal())) {
								jsonMap.put("musculoskeletal", visit.getOpdPatientDetails().getMusculoskeletal());
								}
								else {
									jsonMap.put("musculoskeletal","");
								}
							
							
							if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getCvs())) {
								jsonMap.put("cvs", visit.getOpdPatientDetails().getCvs());
								}
								else {
									jsonMap.put("cvs","");
								}
							
							if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getSkin())) {
								jsonMap.put("skin", visit.getOpdPatientDetails().getSkin());
								}
								else {
									jsonMap.put("skin","");
								}
							
							if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getGi())) {
								jsonMap.put("gi", visit.getOpdPatientDetails().getGi());
								}
								else {
									jsonMap.put("gi","");
								}
							
							
							if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getGenitoUrinary())) {
								jsonMap.put("genitoUrinary", visit.getOpdPatientDetails().getGenitoUrinary());
								}
								else {
									jsonMap.put("genitoUrinary","");
								}
							
							
							
							if(StringUtils.isNotEmpty(visit.getOpdPatientDetails().getSystemOther())) {
								jsonMap.put("sysOthers", visit.getOpdPatientDetails().getSystemOther());
								}
								else {
									jsonMap.put("sysOthers","");
								}
						
							
							
					} else {
						jsonMap.put("opdPatientDetailId", "");
						
						jsonMap.put("cns","");
						jsonMap.put("chest","");
						jsonMap.put("musculoskeletal","");
						jsonMap.put("cvs","");
						jsonMap.put("skin","");
						jsonMap.put("gi","");
						jsonMap.put("genitoUrinary","");
						jsonMap.put("sysOthers","");
					}

					if (visit.getMasMedicalExamReport() != null) {

						if (visit.getMasMedicalExamReport().getAuthority() != null) {
							jsonMap.put("authority", visit.getMasMedicalExamReport().getAuthority());
						} else {
							jsonMap.put("authority", "");
						}
						if (visit.getMasMedicalExamReport().getPlace() != null) {
							jsonMap.put("place", visit.getMasMedicalExamReport().getPlace());
						} else {
							jsonMap.put("place", "");
						}

					} else {
						jsonMap.put("authority", "");
						jsonMap.put("place", "");
					}
					String dateAME = "";
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDateMedicalBoardExam() != null) {
						dateAME = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDateMedicalBoardExam(), "dd/MM/yyyy");
						jsonMap.put("dateAME", dateAME);
					} else {
						jsonMap.put("dateAME", "");
					}

					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getApprovedBy() != null) {
						jsonMap.put("approvedBy", visit.getMasMedicalExamReport().getApprovedBy());
					} else {
						jsonMap.put("approvedBy", "");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIdentificationMarks1()!=null) {
						jsonMap.put("identificationMarks1", visit.getMasMedicalExamReport().getIdentificationMarks1());
					}
					else {
						jsonMap.put("identificationMarks1","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIdentificationMarks2()!=null) {
						jsonMap.put("identificationMarks2", visit.getMasMedicalExamReport().getIdentificationMarks2());
					}
					else {
						jsonMap.put("identificationMarks2","");
					}
					String disabilityBeforeJoining="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getDisabilitybefore()!=null) {
						disabilityBeforeJoining=visit.getMasMedicalExamReport().getDisabilitybefore();
					}
					jsonMap.put("disabilityBeforeJoining", disabilityBeforeJoining);
					String marritalStatus="";
					if( visit.getPatient()!=null && visit.getPatient().getMasMaritalStatus()!=null)
						marritalStatus=visit.getPatient().getMasMaritalStatus().getMaritalStatusName();
					jsonMap.put("marritalStatus", marritalStatus);
					String address="";
					if( visit.getPatient()!=null && visit.getPatient().getAddressLine1()!=null) {
						address=visit.getPatient().getAddressLine1();
					}
					jsonMap.put("address",address);
					
					String authority="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getAuthority()!=null) {
						authority=visit.getMasMedicalExamReport().getAuthority();
					}
					jsonMap.put("authority",authority);
						
					
					String dateOfReport = "";
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDateOfReporting() != null) {
						dateOfReport = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDateOfReporting(), "dd/MM/yyyy");
						
					}  
					jsonMap.put("dateOfReport", dateOfReport);
					
					String dateOfRelease = "";
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDaterelease() != null) {
						dateOfRelease = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDaterelease(), "dd/MM/yyyy");
					}  
					jsonMap.put("dateOfRelease", dateOfRelease);
					
					Long serviceType = null;
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getServiceTypeId() != null) {
						serviceType=visit.getMasMedicalExamReport().getServiceTypeId();
						
					}  
					jsonMap.put("serviceType", serviceType);
					
					String particularsOfPreviousService="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getParticularofpreviousservice()!=null) {
						particularsOfPreviousService=visit.getMasMedicalExamReport().getParticularofpreviousservice();
					}
					jsonMap.put("particularsOfPreviousService",particularsOfPreviousService);
					
					
					String disabilityPensionRecieved="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getReductionDisablePension()!=null) {
						disabilityPensionRecieved=visit.getMasMedicalExamReport().getReductionDisablePension();
					}
					jsonMap.put("disabilityPensionRecieved",disabilityPensionRecieved);
					
					String claimAnyDisability="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getClamingdisability()!=null) {
						claimAnyDisability=visit.getMasMedicalExamReport().getClamingdisability();
					}
					jsonMap.put("claimAnyDisability",claimAnyDisability);
					
					
					String anyOtherInformation="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getAnyOtherInformationAboutYo()!=null) {
						anyOtherInformation=visit.getMasMedicalExamReport().getAnyOtherInformationAboutYo();
					}
					jsonMap.put("anyOtherInformation",anyOtherInformation);
					
					String dateOfWitness="";
					 
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getPatientWitness()!=null) {
						 Long witnessId=null;
						if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getWitnessSig()!=null) {
							witnessId=visit.getMasMedicalExamReport().getWitnessSig();
						}
						jsonMap.put("witnessId",witnessId);
							jsonMap.put("serviceNoEmployee",visit.getMasMedicalExamReport().getPatientWitness().getServiceNo());
							String rankOfEmployee="";
						if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getPatientWitness()!=null && visit.getMasMedicalExamReport().getPatientWitness().getMasRank()!=null  ) {
							MasRank masRank=null;
							if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getPatientWitness()!=null && visit.getMasMedicalExamReport().getPatientWitness().getMasRank()!=null) {
							  masRank=visit.getMasMedicalExamReport().getPatientWitness().getMasRank();//systemAdminDao.getRankByRankCode((visit.getMasMedicalExamReport().getPatientWitness().getMasRank()));
							}
							if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
							jsonMap.put("rankOfEmployee",  masRank.getRankName());
							else
								jsonMap.put("rankOfEmployee", "");
							 
							}
						
						String employeeName="";
						if(visit.getMasMedicalExamReport().getPatientWitness()!=null && visit.getMasMedicalExamReport().getPatientWitness().getEmployeeName()!=null) {
							employeeName=visit.getMasMedicalExamReport().getPatientWitness().getEmployeeName();
						}
							jsonMap.put("signatureOfWitness",employeeName);
							
					}
					String  signatureOfIndividual="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIndividualPatient()!=null) {
						if(visit.getMasMedicalExamReport().getIndividualPatient().getEmployeeName()!=null)
						signatureOfIndividual=visit.getMasMedicalExamReport().getIndividualPatient().getEmployeeName();
						//if(visit.getMasMedicalExamReport()..getIndividualEmployees().getEmployeeName()!=null)
							//signatureOfIndividual+=""+visit.getMasMedicalExamReport().getIndividualEmployees().getEmployeeName();
					}
					jsonMap.put("signatureOfIndividual",signatureOfIndividual);
					Long signatureOfIndividualId=null;
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIndividualSig()!=null) {
						signatureOfIndividualId=visit.getMasMedicalExamReport().getIndividualSig();
					}
					jsonMap.put("signatureOfIndividualId",signatureOfIndividualId);
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getWitnessDate()!=null) {
						dateOfWitness = HMSUtil.convertDateToStringFormat(
							visit.getMasMedicalExamReport().getWitnessDate(), "dd/MM/yyyy");
					}
					jsonMap.put("dateOfWitness",dateOfWitness);
					 
					Long forwardedDesignationId=null;
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getFowardedDesignationId()!=null) {
						forwardedDesignationId=visit.getMasMedicalExamReport().getFowardedDesignationId();
					}
					jsonMap.put("forwardedDesignationId",forwardedDesignationId);
					
					String carefullyExaminedServiceNo="";
					if(users!=null && users.getServiceNo()!=null) {
						carefullyExaminedServiceNo=users.getServiceNo();
					}
					jsonMap.put("carefullyExaminedServiceNo",carefullyExaminedServiceNo);
					
					String carefullyExaminedEmployeeName="";
					if(users!=null) {
						if(users.getFirstName()!=null)
						carefullyExaminedEmployeeName=users.getFirstName();
						if(users.getLastName()!=null)
							carefullyExaminedEmployeeName+=" "+users.getLastName();
					}
					jsonMap.put("carefullyExaminedEmployeeName",carefullyExaminedEmployeeName);
					
					String carefullyExaminedRank="";
					if(CollectionUtils.isNotEmpty(listMasEmployee)) {
						if(listMasEmployee.get(0).getMasRank()!=null ) {
							MasRank masRank=systemAdminDao.getRankByRankCode((listMasEmployee.get(0).getMasRank()));
							if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
								carefullyExaminedRank=masRank.getRankName();
						}
						}
					jsonMap.put("carefullyExaminedRank",carefullyExaminedRank);
					
					String carefullyUnitName="";
					if(CollectionUtils.isNotEmpty(listMasEmployee)) {
						if(listMasEmployee.get(0).getMasUnit()!=null ) {
							List<MasUnit>listMasUnit=medicalExamDAO.getMasUnitByUnitCode(listMasEmployee.get(0).getMasUnit());
							if(CollectionUtils.isNotEmpty(listMasUnit))
							carefullyUnitName=listMasUnit.get(0).getUnitName();
						}
					}
					jsonMap.put("carefullyUnitName",carefullyUnitName);
					if(visit.getPatient()!=null && visit.getPatient().getFitFlag() !=null )
					{
						jsonMap.put("fitFlag",visit.getPatient().getFitFlag());
					}
					else {
						jsonMap.put("fitFlag","");
					}
					
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getRemarksOfLab() !=null )
					{
						jsonMap.put("remarksOfLab",visit.getMasMedicalExamReport().getRemarksOfLab());
					}
					else {
						jsonMap.put("remarksOfLab","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getSignatureMedicalSpecialist() !=null )
					{
						jsonMap.put("signatureOfMedicalSpecialist",visit.getMasMedicalExamReport().getSignatureMedicalSpecialist());
					}
					else {
						jsonMap.put("signatureOfMedicalSpecialist","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getExternalEarR() !=null )
					{
						jsonMap.put("externalEarR",visit.getMasMedicalExamReport().getExternalEarR());
					}
					else {
						jsonMap.put("externalEarR","");
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getExternalEarL() !=null )
					{
						jsonMap.put("externalEarL",visit.getMasMedicalExamReport().getExternalEarL());
					}
					else {
						jsonMap.put("externalEarL","");
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMiddleEar() !=null )
					{
						jsonMap.put("middleEarR",visit.getMasMedicalExamReport().getMiddleEar());
					}
					else {
						jsonMap.put("middleEarR","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMiddleEarR() !=null )
					{
						jsonMap.put("middleEarL",visit.getMasMedicalExamReport().getMiddleEarR());
					}
					else {
						jsonMap.put("middleEarL","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getInnerEarR() !=null )
					{
						jsonMap.put("innerEarR",visit.getMasMedicalExamReport().getInnerEarR());
					}
					else {
						jsonMap.put("innerEarR","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getInnerEarL() !=null )
					{
						jsonMap.put("innerEarL",visit.getMasMedicalExamReport().getInnerEarL());
					}
					else {
						jsonMap.put("innerEarL","");
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getSignatureOfENTSpecialist() !=null )
					{
						jsonMap.put("signatureOfENTSpecialist",visit.getMasMedicalExamReport().getSignatureOfENTSpecialist());
					}
					else {
						jsonMap.put("signatureOfENTSpecialist","");
					}
					
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getNose() !=null )
					{
						jsonMap.put("noseSinuses",visit.getMasMedicalExamReport().getNose());
					}
					else {
						jsonMap.put("noseSinuses","");
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getNosethroat() !=null )
					{
						jsonMap.put("throatSinuses",visit.getMasMedicalExamReport().getNosethroat());
					}
					else {
						jsonMap.put("throatSinuses","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getTypeofcommision() !=null )
					{
						jsonMap.put("typeOfcommision",visit.getMasMedicalExamReport().getTypeofcommision());
					}
					else {
						jsonMap.put("typeOfcommision","");
					}
					if(opdObesityHd!=null) {
					if(opdObesityHd!=null && opdObesityHd.getCloseDate()!=null) {
						jsonMap.put("obsistyCloseDate", opdObesityHd.getCloseDate());
					}
					else {
						jsonMap.put("obsistyCloseDate", "");
					}
					if(opdObesityHd!=null && opdObesityHd.getOverweightFlag()!=null)
					{	
						jsonMap.put("obesityOverWeightFlag",opdObesityHd.getOverweightFlag());
					}
					else
					{
						jsonMap.put("obesityOverWeightFlag","");	
					}
					}
					if(visit.getPatient() != null && visit.getPatient().getMasrelation() != null)
					{
						jsonMap.put("relation", visit.getPatient().getMasrelation().getRelationName());
					}
					else
					{
						jsonMap.put("relation", "");
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getRidcId()!=null)
					{
						jsonMap.put("ridcId", masMedicalExamReport.getRidcId());
					}
					else {
						jsonMap.put("ridcId","");
					} 
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMeRidcId() !=null )
					{
						jsonMap.put("meRidcId",visit.getMasMedicalExamReport().getMeRidcId());
					}
					else {
						jsonMap.put("meRidcId","");
					}
					if(visit.getRidcId()!=null)
					{
						jsonMap.put("meRidcIdForVisit",visit.getRidcId());
					}
					else {
						jsonMap.put("meRidcIdForVisit","");
					}
					
					String meAgeNew="";
					if(masMedicalExamReport!=null  && masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getMeAge()!=null) {
						//Date s =masMedicalExamReport.getVisit().getPatient().getDateOfBirth();
						meAgeNew=getAgeAtTimeOfMe(visit.getMeAge());//HMSUtil.calculateAge(s);
					}
					else {
						meAgeNew="";
					}
					jsonMap.put("meAgeNew",meAgeNew);
					 
					String doeORDoc = "";
					Date dateOfCompletion=null;
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDateOfCompletion() != null) {
						doeORDoc = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDateOfCompletion(), "dd/MM/yyyy");
						dateOfCompletion=visit.getMasMedicalExamReport().getDateOfCompletion();
						jsonMap.put("doeORDoc", doeORDoc);
						jsonMap.put("serviceJoinDate", doeORDoc);
					} 
					else if (visit.getPatient() != null && visit.getPatient().getServiceJoinDate() != null) {
						doeORDoc = HMSUtil.convertDateToStringFormat(
								visit.getPatient().getServiceJoinDate(), "dd/MM/yyyy");
						dateOfCompletion=visit.getPatient().getServiceJoinDate();
						jsonMap.put("doeORDoc", doeORDoc);
						jsonMap.put("serviceJoinDate", doeORDoc);
					}
					else {
						jsonMap.put("doeORDoc", "");
						jsonMap.put("serviceJoinDate", "");
					}
					
					
					Date dateOfMedicalExamVal=null;
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMediceExamDate()!=null) {
						dateOfMedicalExamVal=visit.getMasMedicalExamReport().getMediceExamDate();
					}
					else {
						dateOfMedicalExamVal=visit.getMasMedicalExamReport().getDaterelease();
					}
					 
					if(dateOfMedicalExamVal!=null  && dateOfCompletion!=null) {
						String totalService=HMSUtil.getTotalServiceDateBetweenTwoDate(dateOfMedicalExamVal,dateOfCompletion);
						jsonMap.put("totalService",totalService);
					}
					
					c.add(jsonMap);
				}
				if (c != null && c.size() > 0) {
					json.put("data", c);
					json.put("listMasServiceType", listMasServiceType);
					json.put("status", 1);
					

				} else {
					json.put("data", c);
					json.put("listMasServiceType", listMasServiceType);
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			} else {
				try {
					json.put("data", c);
					json.put("msg", "Visit ID data not found");
					json.put("status", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			json.put("data", c);
			json.put("msg", "Visit ID data not found");
			json.put("status", 0);
			e.printStackTrace();
		}
		return json.toString();

	}

	@Override
	public String submitMedicalExamByMAForm18(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String errorMessage="";
		Long medicalExamId=0l;
	  	String status="";
	  	String msgStatussss="";
	  	String msgForPatient="";
	  	Integer countInvesResultEmpty=0;
		try {
			MasMedicalExamReport masMedicalExamReprt=null;
			String visitId = payload.get("visitId").toString();
			visitId = OpdServiceImpl.getReplaceString(visitId);
			
			String patientId = payload.get("patientId").toString();
			patientId = OpdServiceImpl.getReplaceString(patientId);
			
			String userId = payload.get("userId").toString();
			userId = OpdServiceImpl.getReplaceString(userId);
			
			
			//String hospitalId = payload.get("hospitalId").toString();
		//	hospitalId = OpdServiceImpl.getReplaceString(hospitalId);
			
			masMedicalExamReprt = medicalExamDAO.getMasMedicalExamReprtByVisitId(Long.parseLong(visitId));
			if (masMedicalExamReprt == null) {
				masMedicalExamReprt = new MasMedicalExamReport();
			}
			
			if(payload.containsKey("skin")) {
				String skin = payload.get("skin").toString();
				skin = OpdServiceImpl.getReplaceString(skin);
				if(StringUtils.isNotEmpty(skin))
				masMedicalExamReprt.setSkin(skin);
				}
			
			if(payload.containsKey("rankId")) {
				String rankId = payload.get("rankId").toString();
				rankId = OpdServiceImpl.getReplaceString(rankId);
				if(StringUtils.isNotEmpty(rankId))
				masMedicalExamReprt.setRankId(Long.parseLong(rankId));
				}
			
			
			
			if(StringUtils.isNotEmpty(visitId))
			masMedicalExamReprt.setVisitId(Long.parseLong(visitId));
			
			masMedicalExamReprt.setMediceExamDate(new Date());
			if(StringUtils.isNotEmpty(patientId))
			masMedicalExamReprt.setPatientId(Long.parseLong(patientId));

			
			String authorityForBoard = payload.get("authorityForBoard").toString();
			authorityForBoard = OpdServiceImpl.getReplaceString(authorityForBoard);
			if(StringUtils.isNotEmpty(authorityForBoard)) {
				masMedicalExamReprt.setAuthority(authorityForBoard);
			}
			
			
			String dateOfRelease = payload.get("dateOfRelease").toString();
			dateOfRelease = OpdServiceImpl.getReplaceString(dateOfRelease);
			if(StringUtils.isNotEmpty(dateOfRelease) && !dateOfRelease.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateOfRelease)) {
				Date dateOfReleaseValue = HMSUtil.convertStringTypeDateToDateType(dateOfRelease.trim());
				if(dateOfReleaseValue!=null)
				masMedicalExamReprt.setDaterelease(dateOfReleaseValue);
			}
			
			String dateOfReport= payload.get("dateOfReport").toString();
			dateOfReport = OpdServiceImpl.getReplaceString(dateOfReport);
			if(StringUtils.isNotEmpty(dateOfReport) && !dateOfReport.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateOfReport)) {
				Date dateOfReportV = HMSUtil.convertStringTypeDateToDateType(dateOfReport.trim());
			if(dateOfReportV!=null)
				masMedicalExamReprt.setDateOfReporting(dateOfReportV);
			}
			 
			String serviceOfEmployee = payload.get("serviceOfEmployee").toString();
			serviceOfEmployee = OpdServiceImpl.getReplaceString(serviceOfEmployee);
			if(StringUtils.isNotEmpty(serviceOfEmployee) &&  !serviceOfEmployee.equalsIgnoreCase("0")) {
				masMedicalExamReprt.setServiceTypeId(Long.parseLong(serviceOfEmployee)); 
			}
			
			String particularsOfPreviousService = payload.get("particularsOfPreviousService").toString();
			particularsOfPreviousService = OpdServiceImpl.getReplaceString(particularsOfPreviousService);
			if(StringUtils.isNotEmpty(particularsOfPreviousService)) {
				masMedicalExamReprt.setParticularofpreviousservice(particularsOfPreviousService);
			}
		
			String disabilityPensionRecieved = payload.get("disabilityPensionRecieved").toString();
			disabilityPensionRecieved = OpdServiceImpl.getReplaceString(disabilityPensionRecieved);
			if(StringUtils.isNotEmpty(disabilityPensionRecieved)) {
				if(disabilityPensionRecieved.equalsIgnoreCase("Yes")) {
					masMedicalExamReprt.setReductionDisablePension("Y");
				}else {
				masMedicalExamReprt.setReductionDisablePension("N");
				}
				}
			String disabilityBeforeJoining = payload.get("disabilityBeforeJoining").toString();
			disabilityBeforeJoining = OpdServiceImpl.getReplaceString(disabilityBeforeJoining);
			if(StringUtils.isNotEmpty(disabilityBeforeJoining)) {
					masMedicalExamReprt.setDisabilitybefore(disabilityBeforeJoining);
				}
		
			
			String claimAnyDisability = payload.get("claimAnyDisability").toString();
			claimAnyDisability = OpdServiceImpl.getReplaceString(claimAnyDisability);
			if(StringUtils.isNotEmpty(claimAnyDisability))
			masMedicalExamReprt.setClamingdisability(claimAnyDisability);
			
			
			String anyOtherInformation = payload.get("anyOtherInformation").toString();
			anyOtherInformation = OpdServiceImpl.getReplaceString(anyOtherInformation);
			if(StringUtils.isNotEmpty(anyOtherInformation))
			masMedicalExamReprt.setAnyOtherInformationAboutYo(anyOtherInformation);
			
			
			 
			 String witnessOfEmployee = payload.get("witnessOfEmployee").toString();
			 witnessOfEmployee = OpdServiceImpl.getReplaceString(witnessOfEmployee);
				 if(StringUtils.isNotEmpty(witnessOfEmployee)) {
					 masMedicalExamReprt.setWitnessSig(Long.parseLong(witnessOfEmployee)); 
				 }
				 
			 
			String signatureOfIndividual = payload.get("signatureOfIndividual").toString();
			signatureOfIndividual = OpdServiceImpl.getReplaceString(signatureOfIndividual);
			if(StringUtils.isNotEmpty(signatureOfIndividual))
				masMedicalExamReprt.setIndividualDigitalSign(signatureOfIndividual);
			
			 * if(StringUtils.isNotEmpty(userId)) {
			 * masMedicalExamReprt.setIndividualSig(Long.parseLong(userId)); }
			 
			if(payload.containsKey("serviceNo") && payload.get("serviceNo")!=null) {
				String serviceNo = OpdServiceImpl.getReplaceString(payload.get("serviceNo").toString().trim());
				//List<MasEmployee>listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(serviceNo.toString());
				List<Patient>listPatient=systemAdminDao.getPatientForAdmin(serviceNo.toString());
				if(CollectionUtils.isNotEmpty(listPatient))
				masMedicalExamReprt.setIndividualSig(listPatient.get(0).getPatientId());
			}
			
			String dateOfWitness = payload.get("dateOfWitness").toString();
			dateOfWitness = OpdServiceImpl.getReplaceString(dateOfWitness);
			if(StringUtils.isNotEmpty(dateOfWitness) && !dateOfWitness.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateOfWitness)) {
				Date dateOfWitnessV = HMSUtil.convertStringTypeDateToDateType(dateOfWitness.trim());
				if(dateOfWitnessV!=null)
				masMedicalExamReprt.setWitnessDate(dateOfWitnessV);
			}
			 
			masMedicalExamReprt.setLastChgDate(new Date());
			
			String identificationMarks1 = payload.get("identificationMarks1").toString();
			identificationMarks1 = OpdServiceImpl.getReplaceString(identificationMarks1);
			if(StringUtils.isNotEmpty(identificationMarks1))
			masMedicalExamReprt.setIdentificationMarks1(identificationMarks1);
			
			String identificationMarks2 = payload.get("identificationMarks2").toString();
			identificationMarks2 = OpdServiceImpl.getReplaceString(identificationMarks2);
			if(StringUtils.isNotEmpty(identificationMarks2)){
				masMedicalExamReprt.setIdentificationMarks2(identificationMarks2);
			}
			
			
			String saveInDraft = payload.get("saveInDraft").toString();
			saveInDraft = OpdServiceImpl.getReplaceString(saveInDraft);

			
			
			if (StringUtils.isNotEmpty(saveInDraft) && (!saveInDraft.equalsIgnoreCase("draftMa18") && !saveInDraft.equalsIgnoreCase("portalDraftMa18"))) {
				if(payload.containsKey("pet")) {
				String petStatus = payload.get("pet").toString();
				petStatus = OpdServiceImpl.getReplaceString(petStatus);
				if (petStatus != null && !petStatus.equalsIgnoreCase("")&& petStatus.equalsIgnoreCase("0")) {
					masMedicalExamReprt.setPetStatusOn(petStatus.trim());

				} else {
					masMedicalExamReprt.setPetStatusOn(null);
				}
				}
				if (masMedicalExamReprt != null && masMedicalExamReprt.getForwardUnitId() == null)
					masMedicalExamReprt.setForwardUnitId(null);
				if(payload.containsKey("petDateValue")) {
				String petDateValue = payload.get("petDateValue").toString();
				petDateValue = OpdServiceImpl.getReplaceString(petDateValue);
				if(StringUtils.isNotEmpty(petDateValue) && !petDateValue.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(petDateValue)) {
				Date medicalCompositeDateValue = null;
				medicalCompositeDateValue = HMSUtil.convertStringTypeDateToDateType(petDateValue.trim());
				if(medicalCompositeDateValue!=null) {
				Timestamp catDate = new Timestamp(medicalCompositeDateValue.getTime());
				masMedicalExamReprt.setPetDate(medicalCompositeDateValue);
				}
				}
				}
				
			}
			String designationIdCurrent=null;
			if(StringUtils.isNotEmpty(saveInDraft) && !saveInDraft.equalsIgnoreCase("portalDraftMa18")) {
			if(StringUtils.isNotEmpty(userId))
				masMedicalExamReprt.setLastChgBy(Long.parseLong(userId));
			Users users=null;
		
		if(StringUtils.isNotEmpty(userId)) {
			 users=medicalExamDAO.getUserByUserId(Long.parseLong(userId));
			 if(users!=null) {
				 String currentUserDesignation=users.getDesignationId();
				 if(StringUtils.isNotEmpty(currentUserDesignation)) {
					 String [] currentUserDesignationVal=currentUserDesignation.split(",");
					 if(currentUserDesignationVal!=null && currentUserDesignationVal.length>0) {
						 designationIdCurrent=currentUserDesignationVal[0];
					 }
				 }
			 }
		}
			}
		String hospitalId = payload.get("hospitalId").toString();
			hospitalId = OpdServiceImpl.getReplaceString(hospitalId);

			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftMa18")) {

				masMedicalExamReprt.setStatus("s");
				masMedicalExamReprt.setApprovedBy("MA");
				masMedicalExamReprt.setMaUserId(Long.parseLong(userId));

				masMedicalExamReprt.setMaDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMaDesignationId(Long.parseLong(designationIdCurrent.trim()));
				masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));	
			}

			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftMo18")) {

				masMedicalExamReprt.setApprovedBy("MO");
				masMedicalExamReprt.setMoUserId(Long.parseLong(userId));
				String finalObservationMo = payload.get("finalObservationMo").toString();
				finalObservationMo = OpdServiceImpl.getReplaceString(finalObservationMo);
				masMedicalExamReprt.setFinalobservation(finalObservationMo);
				
				masMedicalExamReprt.setMoDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMoDesignationId(Long.parseLong(designationIdCurrent.trim()));
		
				
				//masMedicalExamReprt = getActionForMedicalExam(payload, masMedicalExamReprt);
				masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));	
			}
			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftRMo18")) {

				masMedicalExamReprt.setApprovedBy("RMO");
				masMedicalExamReprt.setCmUserId(Long.parseLong(userId));

				String finalObservationRmo = payload.get("finalObservationRmo").toString();
				finalObservationRmo = OpdServiceImpl.getReplaceString(finalObservationRmo);
				masMedicalExamReprt.setAaFinalObservation(finalObservationRmo);

				String remarksRmo = payload.get("remarksRmo").toString();
				remarksRmo = OpdServiceImpl.getReplaceString(remarksRmo);
				masMedicalExamReprt.setAaRemarks(remarksRmo);

				masMedicalExamReprt.setCmDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setCmDesignationId(Long.parseLong(designationIdCurrent.trim()));
			
				
				//masMedicalExamReprt = getActionForMedicalExam(payload, masMedicalExamReprt);
				masMedicalExamReprt.setRmoHospitalId(Long.parseLong(hospitalId));	
			}

			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftPRMo18")) {

				masMedicalExamReprt.setApprovedBy("PRMo");
				masMedicalExamReprt.setMdUserId(Long.parseLong(userId));
				String finalObservationPRmo = payload.get("finalObservationPRmo").toString();
				finalObservationPRmo = OpdServiceImpl.getReplaceString(finalObservationPRmo);
				masMedicalExamReprt.setPaFinalobservation(finalObservationPRmo);
				masMedicalExamReprt.setMdDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMdDesignationId(Long.parseLong(designationIdCurrent.trim()));
				
				masMedicalExamReprt.setPdmsHospitalId(Long.parseLong(hospitalId));
			}
			
			try {
				if (StringUtils.isNotEmpty(hospitalId)) {
					List<MasUnit> listMasUnit = medicalExamDAO.getMasUnitListByHospitalId(Long.parseLong(hospitalId));
					if (CollectionUtils.isNotEmpty(listMasUnit))
						masMedicalExamReprt.setUnitId(listMasUnit.get(0).getUnitId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			if (StringUtils.isNotEmpty(saveInDraft) && !saveInDraft.equalsIgnoreCase("portalDraftMa18"))  {
				if(payload.containsKey("dateAME")) {
				String dateAME = payload.get("dateAME").toString();
				dateAME = OpdServiceImpl.getReplaceString(dateAME);
				if (StringUtils.isNotEmpty(dateAME) && !dateAME.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateAME)) {
					Date dateAMEValue = HMSUtil.convertStringTypeDateToDateType(dateAME.trim());
					if(dateAMEValue!=null)
					masMedicalExamReprt.setDateMedicalBoardExam(dateAMEValue);
				}
				}
				
				if(payload.containsKey("unitIdOn") && payload.get("unitIdOn")!=null) {
					String unitOrSlip=payload.get("unitIdOn").toString();
					unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
					
				if(StringUtils.isNotEmpty(unitOrSlip) && !unitOrSlip.equalsIgnoreCase("undefined")) {
					masMedicalExamReprt.setUnitId(Long.parseLong(unitOrSlip));
				}
				}
				if(payload.containsKey("branchOrTradeIdOn") && payload.get("branchOrTradeIdOn")!=null) {
				String branchOrTrade=payload.get("branchOrTradeIdOn").toString();
				branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
				if(StringUtils.isNotEmpty(branchOrTrade) && !branchOrTrade.equalsIgnoreCase("undefined")) {
					masMedicalExamReprt.setBranchId(Long.parseLong(branchOrTrade));
				}
				}
				
				if(payload.containsKey("dob")) {
					String dateOfBirth = payload.get("dob").toString();
					dateOfBirth = OpdServiceImpl.getReplaceString(dateOfBirth);
					if (StringUtils.isNotEmpty(dateOfBirth) && MedicalExamServiceImpl.checkDateFormat(dateOfBirth)) {
						Date dateOfBirthD = HMSUtil.convertStringTypeDateToDateType(dateOfBirth.trim());
						if(dateOfBirthD!=null)
						masMedicalExamReprt.setDateOfBirth(dateOfBirthD);
					}
					}
				
			if(payload.containsKey("meAgeForOn")) {
				String meAgeForOn = payload.get("meAgeForOn").toString();
				meAgeForOn = OpdServiceImpl.getReplaceString(meAgeForOn);
				String ageForMe="";
				if (StringUtils.isNotEmpty(meAgeForOn) && !meAgeForOn.equalsIgnoreCase("")) {
				if (StringUtils.isNotEmpty(meAgeForOn)) {
					String[] ageMale = meAgeForOn.split("/");
					ageForMe = ageMale[0];
				}
					masMedicalExamReprt.setApparentAge(ageForMe);
				}
			}
			
			if(payload.containsKey("totalService")) {
					String totalService = payload.get("totalService").toString();
					totalService = OpdServiceImpl.getReplaceString(totalService);
					if (StringUtils.isNotEmpty(totalService)) {
						masMedicalExamReprt.setTotalService(totalService);
					}
					}
				
			masMedicalExamReprt = submitDentalAndPhysicalCapacity(payload, masMedicalExamReprt);
			masMedicalExamReprt = centralNervousSystemAndOthers(payload, masMedicalExamReprt);
			masMedicalExamReprt = respiratoryAndGastroIntestinalSystem(payload, masMedicalExamReprt);
			masMedicalExamReprt = cardioVascularSystem(payload, masMedicalExamReprt);
			masMedicalExamReprt = submitHearing(payload, masMedicalExamReprt);
			masMedicalExamReprt = submitVision(payload, masMedicalExamReprt);
			} 
			masMedicalExamReprt.setLastChangedDate(new Date());
			errorMessage=medicalExamDAO.saveUpdateAllTransation(masMedicalExamReprt,payload,
					   patientId,   hospitalId,   userId,saveInDraft);
			  	 medicalExamId=0l;
			  	 status="";
			  	 msgStatussss="";
				String[] msgStatusa = errorMessage.split("##");
				medicalExamId = Long.parseLong(msgStatusa[0]);
				msgStatussss = msgStatusa[1];
				msgForPatient=msgStatusa[2];
				if (medicalExamId != 0) {
					status = "1";
					countInvesResultEmpty=medicalExamDAO.getInvestigationResultEmpty(Long.parseLong(visitId));
				} else {
					status = "0";
				}
			} catch (Exception e) {
				status = "0";
				countInvesResultEmpty=0;
				e.printStackTrace();
			}
			JSONObject json = new JSONObject();
			json.put("visitId", medicalExamId);
			json.put("status", status);
			json.put("errorMes", msgStatussss);
			json.put("countInvesResultEmptystatus", countInvesResultEmpty);
			json.put("msgForPatient", msgForPatient);
			
			return json.toString();
	}

	
	public List<PatientDiseaseInfo> getPatientDiseaseInfo(HashMap<String, Object> payload, String patientId,
			String userId) {
		List<PatientDiseaseInfo> listPatientDiseaseInfo = null;
		PatientDiseaseInfo patientDiseaseInfo = null;
		try {
			// Disease,Wound Or Injury Details
			
			String patientDiagnosisId = payload.get("patientDiagnosisId").toString();
			patientDiagnosisId = OpdServiceImpl.getReplaceString(patientDiagnosisId);

			String[] patientDiagnosisIdV = patientDiagnosisId.split(",");
			
			
			String icdDiagnaosis = payload.get("icdDiagnaosis").toString();
			icdDiagnaosis = OpdServiceImpl.getReplaceString(icdDiagnaosis);

			String[] icdDiagnaosisValue = icdDiagnaosis.split(",");

			String firstStartedDate = payload.get("firstStartedDate").toString();
			firstStartedDate = OpdServiceImpl.getReplaceString(firstStartedDate);
			String[] firstStartedDateValue = firstStartedDate.split(",");

			String firstStartedPlace = payload.get("firstStartedPlace").toString();
			firstStartedPlace = OpdServiceImpl.getReplaceString(firstStartedPlace);
			String[] firstStartedPlaceValue = firstStartedPlace.split(",");

			String whereTreated = payload.get("whereTreated").toString();
			whereTreated = OpdServiceImpl.getReplaceString(whereTreated);
			String[] whereTreatedValue = whereTreated.split(",");

			String approximatePeriodsTreatedFrom = payload.get("approximatePeriodsTreatedFrom").toString();
			approximatePeriodsTreatedFrom = OpdServiceImpl.getReplaceString(approximatePeriodsTreatedFrom);
			String[] approximatePeriodsTreatedFromValue = approximatePeriodsTreatedFrom.split(",");

			String approximatePeriodsTreatedTo = payload.get("approximatePeriodsTreatedTo").toString();
			approximatePeriodsTreatedTo = OpdServiceImpl.getReplaceString(approximatePeriodsTreatedTo);
			String[] approximatePeriodsTreatedToValue = approximatePeriodsTreatedTo.split(",");

			Integer counter = 1;
			String finalValue = "";
			HashMap<String, String> mapPatientDiseaseInfo = new HashMap<>();
			for (int i = 0; i < icdDiagnaosisValue.length; i++) {

				if (StringUtils.isNotEmpty(icdDiagnaosisValue[i].toString())
						&& !icdDiagnaosisValue[i].equalsIgnoreCase("0")) {
					finalValue += icdDiagnaosisValue[i].trim();

					if (!firstStartedDateValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(firstStartedDateValue[i])) {
						finalValue += "," + firstStartedDateValue[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (!firstStartedPlaceValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(firstStartedPlaceValue[i])) {

						finalValue += "," + firstStartedPlaceValue[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (!whereTreatedValue[i].equalsIgnoreCase("0") && StringUtils.isNotBlank(whereTreatedValue[i])) {
						finalValue += "," + whereTreatedValue[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (!approximatePeriodsTreatedFromValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(approximatePeriodsTreatedFromValue[i])) {
						finalValue += "," + approximatePeriodsTreatedFromValue[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (!approximatePeriodsTreatedToValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(approximatePeriodsTreatedToValue[i])) {
						finalValue += "," + approximatePeriodsTreatedToValue[i].trim();
					} else {
						finalValue += "," + "0";
					}
					if (!patientDiagnosisIdV[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(patientDiagnosisIdV[i])) {
						finalValue += "," + patientDiagnosisIdV[i].trim();
					} else {
						finalValue += "," + "0";
					}
					
					mapPatientDiseaseInfo.put(icdDiagnaosisValue[i].trim() + "@#" + counter, finalValue);
					finalValue = "";
					counter++;
				}
			}

			counter = 1;

			if (icdDiagnaosisValue != null && icdDiagnaosisValue.length > 0) {
				listPatientDiseaseInfo = new ArrayList<>();
				listPatientDiseaseInfo = new ArrayList<>();
				for (String icdDiagnosis : icdDiagnaosisValue) {
					if (mapPatientDiseaseInfo.containsKey(icdDiagnosis.trim() + "@#" + counter)) {
						String patientDiagonosisAllValue = mapPatientDiseaseInfo
								.get(icdDiagnosis.trim() + "@#" + counter);
						if (StringUtils.isNotEmpty(patientDiagonosisAllValue)) {

							String[] finalValuepatientDiagonosis = patientDiagonosisAllValue.split(",");
								
							if(StringUtils.isNotEmpty(finalValuepatientDiagonosis[6])&& !finalValuepatientDiagonosis[6].equalsIgnoreCase("0")) {
								patientDiseaseInfo=medicalExamDAO.getPatientDiseaseWoundInjuryDetailByDiseaseInfoId(Long.parseLong(finalValuepatientDiagonosis[6].trim().toString()));
							}
							else {
							patientDiseaseInfo = new PatientDiseaseInfo();
							}
							if (finalValuepatientDiagonosis[0] != null
									&& !finalValuepatientDiagonosis[0].equalsIgnoreCase("")) {
								patientDiseaseInfo.setIcdId(Long.parseLong(finalValuepatientDiagonosis[0]));
							}
							patientDiseaseInfo.setBeforeFlag("N");

							if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[1]) && MedicalExamServiceImpl.checkDateFormat(finalValuepatientDiagonosis[1])) {
								Date firstStartDate = null;
								firstStartDate = HMSUtil
										.convertStringTypeDateToDateType(finalValuepatientDiagonosis[1].trim());
								if(firstStartDate!=null)
								patientDiseaseInfo.setStDate(firstStartDate);
							}

							if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[2])) {
								patientDiseaseInfo.setStPlace(finalValuepatientDiagonosis[2]);
							}

							if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[3])) {
								patientDiseaseInfo.setTreatedPlace(finalValuepatientDiagonosis[3]);
							}

							if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[4]) && MedicalExamServiceImpl.checkDateFormat(finalValuepatientDiagonosis[4])) {
								Date treatedDate = null;
								treatedDate = HMSUtil.convertStringTypeDateToDateType(finalValuepatientDiagonosis[4].trim());
								if(treatedDate!=null)
								patientDiseaseInfo.setFromDate(treatedDate);
							}

							if (StringUtils.isNotEmpty(finalValuepatientDiagonosis[5]) && MedicalExamServiceImpl.checkDateFormat(finalValuepatientDiagonosis[5])) {
								Date treatedTo = null;
								treatedTo = HMSUtil.convertStringTypeDateToDateType(finalValuepatientDiagonosis[5].trim());
								if(treatedTo!=null)
								patientDiseaseInfo.setToDate(treatedTo);
							}

							patientDiseaseInfo.setLastChgBy(Long.parseLong(userId));
							Date date = new Date();
							patientDiseaseInfo.setLastChgDate(new Timestamp(date.getTime()));
							patientDiseaseInfo.setPatientId(Long.parseLong(patientId));
							listPatientDiseaseInfo.add(patientDiseaseInfo);
						}
					}
					counter += 1;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPatientDiseaseInfo;
	}
	
	public List<PatientDiseaseInfo>getPatientDiseaseForBeforeJoined(HashMap<String, Object> payload, String patientId,
			String userId) {
		List<PatientDiseaseInfo> listPatientDiseaseInfo = null;
		try {

			PatientDiseaseInfo patientDiseaseInfo = null;
			String disabilityBeforeJoining = payload.get("disabilityBeforeJoining").toString();
			disabilityBeforeJoining = OpdServiceImpl.getReplaceString(disabilityBeforeJoining);
			if (StringUtils.isNotEmpty(disabilityBeforeJoining) && disabilityBeforeJoining.equalsIgnoreCase("Yes")) {

				String armedForcesPatientDiagnosisId = payload.get("armedForcesPatientDiagnosisId").toString();
				armedForcesPatientDiagnosisId = OpdServiceImpl.getReplaceString(armedForcesPatientDiagnosisId);
				String[] armedForcesPatientDiagnosisIdValue = armedForcesPatientDiagnosisId.split(",");
				
				String armedForcesFrom = payload.get("armedForcesFrom").toString();
				armedForcesFrom = OpdServiceImpl.getReplaceString(armedForcesFrom);
				String[] armedForcesFromValue = armedForcesFrom.split(",");

				String armedForcesTo = payload.get("armedForcesTo").toString();
				armedForcesTo = OpdServiceImpl.getReplaceString(armedForcesTo);
				String[] armedForcesToValue = armedForcesTo.split(",");

				String armedForcesDetails = payload.get("armedForcesDetails").toString();
				armedForcesDetails = OpdServiceImpl.getReplaceString(armedForcesDetails);
				String[] armedForcesDetailsValue = armedForcesDetails.split(",");
				listPatientDiseaseInfo = new ArrayList<>();

				Integer counter = 1;
				String finalValue = "";
				HashMap<String, String> mapPatientDiseaseInfo = new HashMap<>();
				for (int i = 0; i < armedForcesFromValue.length; i++) {

					if (StringUtils.isNotEmpty(armedForcesFromValue[i].toString())
							&& !armedForcesFromValue[i].equalsIgnoreCase("0")) {
						finalValue += armedForcesFromValue[i].trim();

						if (!armedForcesToValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(armedForcesToValue[i])) {
							finalValue += "," + armedForcesToValue[i].trim();
						} else {
							finalValue += "," + "0";
						}

						if (!armedForcesDetailsValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(armedForcesDetailsValue[i])) {

							finalValue += "," + armedForcesDetailsValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
						
						if (!armedForcesPatientDiagnosisIdValue[i].equalsIgnoreCase("0")
								&& StringUtils.isNotBlank(armedForcesPatientDiagnosisIdValue[i])) {

							finalValue += "," + armedForcesPatientDiagnosisIdValue[i].trim();
						} else {
							finalValue += "," + "0";
						}
						
						mapPatientDiseaseInfo.put(armedForcesFromValue[i].trim() + "@#" + counter, finalValue);
						finalValue = "";
						counter++;
					}
				}

				counter = 1;
				for (String objArmedForcesFrom : armedForcesFromValue) {
					if (StringUtils.isNotEmpty(objArmedForcesFrom)) {
						patientDiseaseInfo = new PatientDiseaseInfo();
						if (mapPatientDiseaseInfo.containsKey(objArmedForcesFrom.trim() + "@#" + counter)) {
							String patientArmedForcesFrom = mapPatientDiseaseInfo
									.get(objArmedForcesFrom.trim() + "@#" + counter);
							if (StringUtils.isNotEmpty(patientArmedForcesFrom)) {
								String[] finalValuepatientArmedForces = patientArmedForcesFrom.split(",");
								
								if(StringUtils.isNotEmpty(finalValuepatientArmedForces[3])&& !finalValuepatientArmedForces[3].equalsIgnoreCase("0")) {
									patientDiseaseInfo=medicalExamDAO.getPatientDiseaseWoundInjuryDetailByDiseaseInfoId(Long.parseLong(finalValuepatientArmedForces[3].trim().toString()));
								}
								else {
								patientDiseaseInfo = new PatientDiseaseInfo();
								}
								
								if (finalValuepatientArmedForces[0] != null
										&& !finalValuepatientArmedForces[0].equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(finalValuepatientArmedForces[0])) {
									Date armedForcesFromV = null;
									armedForcesFromV = HMSUtil
											.convertStringTypeDateToDateType(finalValuepatientArmedForces[0].trim());
									if(armedForcesFromV!=null)
									patientDiseaseInfo.setFromDate(armedForcesFromV);
								}

								if (finalValuepatientArmedForces[1] != null
										&& !finalValuepatientArmedForces[1].equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(finalValuepatientArmedForces[1])) {
									Date armedForcesToV = null;
									armedForcesToV = HMSUtil
											.convertStringTypeDateToDateType(finalValuepatientArmedForces[1].trim());
									if(armedForcesToV!=null)
									patientDiseaseInfo.setToDate(armedForcesToV);
								}
								if (finalValuepatientArmedForces[2] != null
										&& !finalValuepatientArmedForces[2].equalsIgnoreCase("")) {
									patientDiseaseInfo.setRemarks(finalValuepatientArmedForces[2]);
								}
								patientDiseaseInfo.setBeforeFlag("Y");
								patientDiseaseInfo.setLastChgBy(Long.parseLong(userId));
								Date date = new Date();
								patientDiseaseInfo.setLastChgDate(new Timestamp(date.getTime()));
								patientDiseaseInfo.setPatientId(Long.parseLong(patientId));
								patientDiseaseInfo.setIcdId(null);
								listPatientDiseaseInfo.add(patientDiseaseInfo);
							}
						}

					}
					counter += 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPatientDiseaseInfo;
	}
	
	
	
	
	public List<PatientServicesDetail> getPatientServicesDetail(HashMap<String, Object> payload, String patientId,
			String userId) {
		List<PatientServicesDetail> listPatientServicesDetail = null;

		PatientServicesDetail patientServicesDetail = null;
		try {
			
			
			
			String serviceDetailId = payload.get("serviceDetailId").toString();
			serviceDetailId = OpdServiceImpl.getReplaceString(serviceDetailId);
			String[] serviceDetailIdValue = serviceDetailId.split(",");
			
			String serviceDetailFrom = payload.get("serviceDetailFrom").toString();
			serviceDetailFrom = OpdServiceImpl.getReplaceString(serviceDetailFrom);
			String[] serviceDetailFromValue = serviceDetailFrom.split(",");

			String serviceDetailTo = payload.get("serviceDetailTo").toString();
			serviceDetailTo = OpdServiceImpl.getReplaceString(serviceDetailTo);
			String[] serviceDetailToValue = serviceDetailTo.split(",");

			String serviceDetailPlace = payload.get("serviceDetailPlace").toString();
			serviceDetailPlace = OpdServiceImpl.getReplaceString(serviceDetailPlace);
			String[] serviceDetailPlaceValue = serviceDetailPlace.split(",");

			String serviceDetailPf = payload.get("serviceDetailPf").toString();
			serviceDetailPf = OpdServiceImpl.getReplaceString(serviceDetailPf);
			String[] serviceDetailPfValue = serviceDetailPf.split(",");
			Integer counter = 1;
			String finalValue = "";
			HashMap<String, String> mapServiceDetailInfo = new HashMap<>();
			for (int i = 0; i < serviceDetailFromValue.length; i++) {

				if (StringUtils.isNotEmpty(serviceDetailFromValue[i].toString())
						&& !serviceDetailFromValue[i].equalsIgnoreCase("0")) {
					finalValue += serviceDetailFromValue[i].trim();

					if (!serviceDetailToValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(serviceDetailToValue[i])) {
						finalValue += "," + serviceDetailToValue[i].trim();
					} else {
						finalValue += "," + "0";
					}

					if (!serviceDetailPlaceValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(serviceDetailPlaceValue[i])) {

						finalValue += "," + serviceDetailPlaceValue[i].trim();
					} else {
						finalValue += "," + "0";
					}
					if (!serviceDetailPfValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(serviceDetailPfValue[i])) {

						finalValue += "," + serviceDetailPfValue[i].trim();
					} else {
						finalValue += "," + "0";
					}
					if (!serviceDetailIdValue[i].equalsIgnoreCase("0")
							&& StringUtils.isNotBlank(serviceDetailIdValue[i])) {

						finalValue += "," + serviceDetailIdValue[i].trim();
					} else {
						finalValue += "," + "0";
					}
					
					
					mapServiceDetailInfo.put(serviceDetailFromValue[i].trim() + "@#" + counter, finalValue);
					finalValue = "";
					counter++;
				}
			}

			counter = 1;

			if (serviceDetailFromValue != null && serviceDetailFromValue.length > 0) {
				listPatientServicesDetail = new ArrayList<>();
				for (String objServiceDetail : serviceDetailFromValue) {

					if (mapServiceDetailInfo.containsKey(objServiceDetail.trim() + "@#" + counter)) {
						String patientServiceDetailFrom = mapServiceDetailInfo
								.get(objServiceDetail.trim() + "@#" + counter);
						if (StringUtils.isNotEmpty(patientServiceDetailFrom)) {
							String[] finalValueServiceDetail = patientServiceDetailFrom.split(",");

							if(finalValueServiceDetail[4]!=null && StringUtils.isNotEmpty(finalValueServiceDetail[4]) &&  !finalValueServiceDetail[0].equalsIgnoreCase("") ) {
								patientServicesDetail=medicalExamDAO.getServiceDetailsByServiceDetailId(Long.parseLong(finalValueServiceDetail[4].trim().toString()));
							}
							else {
							patientServicesDetail = new PatientServicesDetail();
							}
							if (finalValueServiceDetail[0] != null
									&& !finalValueServiceDetail[0].equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(finalValueServiceDetail[0]))  {
								Date fromDateServiceDetail = null;
								fromDateServiceDetail = HMSUtil
										.convertStringTypeDateToDateType(finalValueServiceDetail[0].trim());
								if(fromDateServiceDetail!=null)
								patientServicesDetail.setFromDate(fromDateServiceDetail);
							}

							if (finalValueServiceDetail[1] != null
									&& !finalValueServiceDetail[1].equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(finalValueServiceDetail[1].trim())) {
								Date toDateServiceDetail = null;
								toDateServiceDetail = HMSUtil
										.convertStringTypeDateToDateType(finalValueServiceDetail[1]);
								if(toDateServiceDetail!=null)
								patientServicesDetail.setToDate(toDateServiceDetail);
							}

							if (finalValueServiceDetail[2] != null
									&& !finalValueServiceDetail[2].equalsIgnoreCase("")) {
								patientServicesDetail.setPlace(finalValueServiceDetail[2]);
							}
							if (finalValueServiceDetail[3] != null
									&& !finalValueServiceDetail[3].equalsIgnoreCase("")) {
								patientServicesDetail.setPf(finalValueServiceDetail[3]);
							}
							if (StringUtils.isNotEmpty(patientId)) {
								patientServicesDetail.setPatientId(Long.parseLong(patientId));
							}
							if (StringUtils.isNotEmpty(userId))
								patientServicesDetail.setLastChgBy(Long.parseLong(userId));
							Date date = new Date();
							patientServicesDetail.setLastChgDate(new Timestamp(date.getTime()));
							listPatientServicesDetail.add(patientServicesDetail);

						}
					}
					counter += 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listPatientServicesDetail;

	}

	@Override
	public String getServiceDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<PatientServicesDetail>listPatientServicesDetail =null;
		try {
			if (jsondata.get("patientId") != null) {
				 listPatientServicesDetail = medicalExamDAO
						.getServiceDetails(Long.parseLong(jsondata.get("patientId").toString()));
			}
			if (CollectionUtils.isNotEmpty(listPatientServicesDetail)) {


				for (PatientServicesDetail patientServicesDetail : listPatientServicesDetail) {

					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					if(patientServicesDetail.getServiceDetailsId()!=null) 
					jsonMap.put("serviceDetailsId", patientServicesDetail.getServiceDetailsId());
					else
						jsonMap.put("serviceDetailsId", "");
					String pf="";
					if(patientServicesDetail.getPf()!=null) 
						pf=patientServicesDetail.getPf();
					jsonMap.put("pf", pf);
					
					String fromDate="";
					if(patientServicesDetail.getFromDate()!=null) {
						fromDate = HMSUtil.convertDateToStringFormat(
								patientServicesDetail.getFromDate(), "dd/MM/yyyy");
					}
					jsonMap.put("fromDate", fromDate);
					 
					Long patientId=null; 
					if(patientServicesDetail.getPatientId()!=null)
					{
					patientId=patientServicesDetail.getPatientId();	
					}
					jsonMap.put("patientId",patientId);
					String place=null; 
					if(patientServicesDetail.getPlace()!=null)
					{
						place=patientServicesDetail.getPlace();	
					}
					jsonMap.put("place",place);
					String toDate=null; 
					if(patientServicesDetail.getToDate()!=null)
					{
						toDate = HMSUtil.convertDateToStringFormat(
								patientServicesDetail.getToDate(), "dd/MM/yyyy");
					}
					jsonMap.put("toDate",toDate);
 
					c.add(jsonMap);
				}
			}
			if(CollectionUtils.isNotEmpty(listPatientServicesDetail)) {
			json.put("listPatientServicesDetail", c);
			json.put("msg", "Service detail data");
			json.put("status", "1");
			}
			else {
				json.put("listPatientServicesDetail", c);
				json.put("msg", "Service detail data is Empty.");
				json.put("status", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	
	}

	@Override
	public String getPatientDiseaseWoundInjuryDetail(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {


		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		List<PatientDiseaseInfo>listPatientDiseaseInfo =null;
		try {
			if (jsondata.get("patientId") != null) {
				listPatientDiseaseInfo = medicalExamDAO
						.getPatientDiseaseWoundInjuryDetail(Long.parseLong(jsondata.get("patientId").toString()));
			}
			 
			if (CollectionUtils.isNotEmpty(listPatientDiseaseInfo)) {


				for (PatientDiseaseInfo patientDiseaseInfo : listPatientDiseaseInfo) {

					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					if(patientDiseaseInfo.getDiseaseInfoId()!=null) 
					jsonMap.put("diseaseInfoId", patientDiseaseInfo.getDiseaseInfoId());
					else
						jsonMap.put("diseaseInfoId", "");
					
					if(patientDiseaseInfo.getBeforeFlag()!=null) 
					jsonMap.put("beforeFlag", patientDiseaseInfo.getBeforeFlag());
					else {
						jsonMap.put("beforeFlag", "");
					}
					
					Long icdId=null;
					if(patientDiseaseInfo.getIcdId()!=null) {
						icdId=patientDiseaseInfo.getIcdId();
					}
					jsonMap.put("icdId",icdId);
					
					String icdDiagonosisName="";
					if(patientDiseaseInfo.getMasIcd()!=null) {
						icdDiagonosisName=patientDiseaseInfo.getMasIcd().getIcdName();
					}
					jsonMap.put("icdDiagonosisName",icdDiagonosisName);
					
					String icdCode="";
					if(patientDiseaseInfo.getMasIcd()!=null) {
						icdCode=patientDiseaseInfo.getMasIcd().getIcdCode();
					}
					jsonMap.put("icdCode",icdCode);
					
					Long patientId=null; 
					if(patientDiseaseInfo.getPatientId()!=null)
					{
					patientId=patientDiseaseInfo.getPatientId();	
					}
					jsonMap.put("patientId",patientId);
					
					
					
					String remarks=null; 
					if(patientDiseaseInfo.getRemarks()!=null)
					{
					remarks=patientDiseaseInfo.getRemarks();	
					}
					jsonMap.put("remarks",remarks);
					
					String firstStartDate=null; 
					if(patientDiseaseInfo.getStDate()!=null)
					{
						firstStartDate = HMSUtil.convertDateToStringFormat(
								patientDiseaseInfo.getStDate(), "dd/MM/yyyy");
					}
					jsonMap.put("firstStartDate",firstStartDate);
					
					
					String firstStartPlace=null; 
					if(patientDiseaseInfo.getStPlace()!=null)
					{
						firstStartPlace =   patientDiseaseInfo.getStPlace();
					}
					jsonMap.put("firstStartPlace",firstStartPlace);
					
					String treatedPlace=null; 
					if(patientDiseaseInfo.getTreatedPlace()!=null)
					{
						treatedPlace =  patientDiseaseInfo.getTreatedPlace();
					}
					jsonMap.put("treatedPlace",treatedPlace);
					
					String fromDate="";
					if(patientDiseaseInfo.getFromDate()!=null) {
						fromDate = HMSUtil.convertDateToStringFormat(
								patientDiseaseInfo.getFromDate(), "dd/MM/yyyy");
					}
					jsonMap.put("approximateFromDate", fromDate);
					 
					
					String toDate=null; 
					if(patientDiseaseInfo.getToDate()!=null)
					{
						toDate = HMSUtil.convertDateToStringFormat(
								patientDiseaseInfo.getToDate(), "dd/MM/yyyy");
					}
					jsonMap.put("approximatetoDate",toDate);
				
					
					
					c.add(jsonMap);
				}
			}
			if (c != null && c.size() > 0) {
				json.put("listPatientDiseaseInfo", c);
				json.put("msg", "PatientDiseaseInfo Data  found");
				json.put("status", 1);

			} else {
				json.put("listPatientDiseaseInfo", c);
				json.put("msg", "PatientDiseaseInfo Data not found");
				json.put("status", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	
	
	}

	@Override
	public String getMasEmployeeDetailForService(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONObject json = new JSONObject();
		
		// List<MasEmployee> listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(payload.get("serviceNo").toString());
		List<Patient>listPatient=systemAdminDao.getPatientForAdmin(payload.get("serviceNo").toString()); 
		//List<MasEmployee> listMasEmployeeIndivi=null;
		List<Patient> listPatientIndivi=null;
		 if(payload.containsKey("serviceNoForIndivisual") && payload.get("serviceNoForIndivisual")!=null) {
			 //listMasEmployeeIndivi=systemAdminDao.getMasEmployeeForAdmin(payload.get("serviceNoForIndivisual").toString());
			 listPatientIndivi=systemAdminDao.getPatientForAdmin(payload.get("serviceNoForIndivisual").toString()); 
		 }
		 
		 
		// Users users=systemAdminDao.getUserbyUserId(Long.parseLong(payload.get("userId").toString()));
		List<HashMap<String, Object>> p = new ArrayList<HashMap<String, Object>>();
		try {

		//	for (MasEmployee masEmployee : listMasEmployee) {
			for (Patient patient : listPatient) {
				HashMap<String, Object> pt = new HashMap<String, Object>();
				pt.put("serviceNo", patient.getServiceNo().trim());
				String ranks="";
				if(patient!=null &&  patient.getMasRank()!=null) {
					MasRank masRank=null;
					if(patient.getMasRank()!=null)
					   masRank=patient.getMasRank();//systemAdminDao.getRankByRankCode((masEmployee.getMasRank()));
					if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName())) {
						ranks=masRank.getRankName();
					}
				}
				pt.put("rank", ranks);
				pt.put("emplyeeName", patient.getEmployeeName());
				pt.put("emplyeeId", patient.getPatientId());
				String userName="";
				if(users.getFirstName()!=null) {
					userName=users.getFirstName();
				}
				if(users.getLastName()!=null) {
					userName+=" "+users.getLastName();
				}
				if(CollectionUtils.isNotEmpty(listPatientIndivi) && listPatientIndivi.get(0)!=null && listPatientIndivi.get(0).getEmployeeName()!=null) {
					userName=listPatientIndivi.get(0).getEmployeeName();
				}
				pt.put("userName",userName);
				p.add(pt);

			}
			
			if (p != null && p.size() > 0) {
				json.put("masEmployeeList", p);
				json.put("count", p.size());
				json.put("msg", "Users List  get  sucessfull... ");
				json.put("status", "1");
			} else {
				return "{\"status\":\"0\",\"msg\":\"User Does Not Exist.\"}";
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return json.toString();

		
	}

	@Override
	public String getMasDesignationMappingByUnitId(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Long forwardedUnitId=null;
		List<Object> responseList = new ArrayList<Object>();
		try {
		if(payload.get("forwardedUnitId")!=null) {
			forwardedUnitId=Long.parseLong(payload.get("forwardedUnitId").toString());
		// List<MasHospital>listMasHospital=systemAdminDao.getMasHospitalByUnitId(forwardedUnitId);
		 List<MasDesignationMapping> listMasDesignationMapping=systemAdminDao.getMassDesiByUnitId(forwardedUnitId);
		 for (MasDesignationMapping masDesignationMapping : listMasDesignationMapping) {
				String designationName="";
				if(StringUtils.isNotEmpty(masDesignationMapping.getDesignationId()))
				designationName	= systemAdminDao.getMasDesignationByDesignationId(masDesignationMapping.getDesignationId());	
				
				HashMap<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("masId", masDesignationMapping.getId());
				if(StringUtils.isNotEmpty(designationName)) {
				String [] desigArray=designationName.split("##");
				if(desigArray!=null && desigArray.length>0) {
				jsonMap.put("designationId", desigArray[1]);
				jsonMap.put("designamtionName", desigArray[0]);
				}
				else {
					jsonMap.put("designationId","");
					jsonMap.put("designamtionName", "");
				}
				}
				else {
					jsonMap.put("designationId", "");
					jsonMap.put("designamtionName", "");
						
				}
				responseList.add(jsonMap);
		 		} 
				}
		if (responseList != null && responseList.size() > 0) {
			json.put("dataDesignationList", responseList);
			json.put("status", 1);
			 
		} else {
			json.put("dataDesignationList", responseList);
			json.put("msg", "Data not found");
			json.put("status", 0);
		}
			}
		 catch (Exception e) {
			System.out.println(e);
		}

		return json.toString();
	}

	@Override
	public String getInvestigationListUOM(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
			JSONObject json = new JSONObject();
	        try
	        {
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
			{
				return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
			} 
			else
			{
					Users checkEmp = md.checkUser(Long.parseLong(jsondata.get("employeeId").toString()));
				    if(checkEmp!=null)
				    {
				    List<DgMasInvestigation> mst_investigation =null;
				    if(jsondata.get("mainChargeCode")!=null)	
					  mst_investigation = medicalExamDAO.getInvestigationListUOM(jsondata.get("mainChargeCode").toString(),  jsondata);
				    if (mst_investigation==null && mst_investigation.size() == 0)
				    {
				    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				    } 
				    else 
				    {
				    	json.put("InvestigationList", checkEmp);
				    	json.put("InvestigationList", mst_investigation);
				    	json.put("msg", "Investigation List  get  sucessfull... ");
				    	json.put("status", "1");
				    	json.put("size",mst_investigation.size());

				   }

			}
		   else
		   {
			   return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID Not Found\"}";
	    	
			}

			
		}
	        }
	        catch(Exception e)	{
			e.printStackTrace();
		     }
	        return json.toString();
	        
	        
		}

	@Override
	public String submitMedicalExamByMA3A(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {

		Long medicalExamId = null;
		String msgStatussss = "";
		MasMedicalExamReport masMedicalExamReprt = null;
		String visitId = "";
		String status = "";
		String msgStatus ="";
		Integer countInvesResultEmpty=0;
		String msgForPatient="";
		try {
			//try {

				visitId = payload.get("visitId").toString();
				visitId = OpdServiceImpl.getReplaceString(visitId);

				masMedicalExamReprt = medicalExamDAO.getMasMedicalExamReprtByVisitId(Long.parseLong(visitId));
				if (masMedicalExamReprt == null) {
					masMedicalExamReprt = new MasMedicalExamReport();
				}

				
				if(payload.containsKey("doeORDoc")) {
					String doeORDoc = payload.get("doeORDoc").toString();
					doeORDoc = OpdServiceImpl.getReplaceString(doeORDoc);
					if (StringUtils.isNotEmpty(doeORDoc) && !doeORDoc.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(doeORDoc)) {
						Date doeORDocValue = HMSUtil.convertStringTypeDateToDateType(doeORDoc.trim());
						if(doeORDocValue!=null)
							masMedicalExamReprt.setDateOfCompletion(doeORDocValue);
						
					}
					}
				//New field Added For Lab
				String remarksOfLab = payload.get("remarksOfLab").toString();
				remarksOfLab = OpdServiceImpl.getReplaceString(remarksOfLab);
				if(StringUtils.isNotEmpty(remarksOfLab)) {
					masMedicalExamReprt.setRemarksOfLab(remarksOfLab); 
				}
				
				String dateOfLab = payload.get("dateOfLab").toString();
				dateOfLab = OpdServiceImpl.getReplaceString(dateOfLab);
				if(StringUtils.isNotEmpty(dateOfLab) && MedicalExamServiceImpl.checkDateFormat(dateOfExam)) {
					Date dateOfLabValue = HMSUtil.convertStringTypeDateToDateType(dateOfLab);
					masMedicalExamReprt.setDateOfLab(dateOfLabValue) ; 
				}
				String signatureOfEyeSpecialist = payload.get("signatureOfEyeSpecialist").toString();
				signatureOfEyeSpecialist = OpdServiceImpl.getReplaceString(signatureOfEyeSpecialist);
				if(StringUtils.isNotEmpty(signatureOfEyeSpecialist)) {
					masMedicalExamReprt.setSignatureOfEyeSpecialist(signatureOfEyeSpecialist);
				}
				masMedicalExamReprt = MedicalExamServiceImpl.getLabValue(payload, masMedicalExamReprt);
				  End of Lab  
				
				//New Field added for Surgery
				
				String remarksOfSurgery = payload.get("remarksOfSurgery").toString();
				remarksOfSurgery = OpdServiceImpl.getReplaceString(remarksOfSurgery);
				if(StringUtils.isNotEmpty(remarksOfSurgery)) {
					masMedicalExamReprt.setRemarksOfSurgery(remarksOfSurgery);
				}
				
				String dateOfSurgery = payload.get("dateOfSurgery").toString();
				dateOfSurgery = OpdServiceImpl.getReplaceString(dateOfSurgery);
				if(StringUtils.isNotEmpty(dateOfSurgery)) {
					Date dateOfSurgeryValue = HMSUtil.convertStringTypeDateToDateType(dateOfSurgery);
					masMedicalExamReprt.setDateOfSurgery(dateOfSurgeryValue);
				}
				String signatureOfSurgicalSpecialist = payload.get("signatureOfSurgicalSpecialist").toString();
				signatureOfSurgicalSpecialist = OpdServiceImpl.getReplaceString(signatureOfSurgicalSpecialist);
				if(StringUtils.isNotEmpty(signatureOfSurgicalSpecialist)) {
					masMedicalExamReprt.setSignatureOfSurgicalSpecialist(signatureOfSurgicalSpecialist);
				}
				masMedicalExamReprt = MedicalExamServiceImpl.getSurgeryValue(payload, masMedicalExamReprt);
				 
				
				  End of Surgery  
			 
				//New field added for Gynaecology
				String remarksOfGynaecology = payload.get("remarksOfGynaecology").toString();
				remarksOfGynaecology = OpdServiceImpl.getReplaceString(remarksOfGynaecology);
				if(StringUtils.isNotEmpty(remarksOfGynaecology)) {
					//masMedicalExamReprt.setServiceTypeId(Long.parseLong(serviceOfEmployee)); 
					masMedicalExamReprt.setRemarksOfGynaecology(remarksOfGynaecology);
				}
				
				String dateOfGynaecology = payload.get("dateOfGynaecology").toString();
				dateOfGynaecology = OpdServiceImpl.getReplaceString(dateOfGynaecology);
				if(StringUtils.isNotEmpty(dateOfGynaecology)) {
					Date dateOfGynaecologyValue = HMSUtil.convertStringTypeDateToDateType(dateOfGynaecology);
					masMedicalExamReprt.setDateOfGynaecology(dateOfGynaecologyValue);
				}
				String signatureOfGynaecologist = payload.get("signatureOfGynaecologist").toString();
				signatureOfGynaecologist = OpdServiceImpl.getReplaceString(signatureOfGynaecologist);
				if(StringUtils.isNotEmpty(signatureOfGynaecologist)) {
					masMedicalExamReprt.setSignatureOfGynaecologist(signatureOfGynaecologist);
				}
				masMedicalExamReprt = MedicalExamServiceImpl.getGynaecologySomeValue(  payload,
						  masMedicalExamReprt);
				
				  End of Gynaecology  
				
				New Filed added for Dental
				String remarksOfDental = payload.get("remarksOfDental").toString();
				remarksOfDental = OpdServiceImpl.getReplaceString(remarksOfDental);
				if(StringUtils.isNotEmpty(remarksOfDental)) {
					masMedicalExamReprt.setRemarksOfDental(remarksOfDental);
				}
				
				String dateOfDental = payload.get("dateOfDental").toString();
				dateOfDental = OpdServiceImpl.getReplaceString(dateOfDental);
				if(StringUtils.isNotEmpty(dateOfDental)) {
					Date dateOfDentalValue = HMSUtil.convertStringTypeDateToDateType(dateOfDental);
					masMedicalExamReprt.setDateOfDental(dateOfDentalValue);
				}
				String signatureOfDentalOfficer = payload.get("signatureOfDentalOfficer").toString();
				signatureOfDentalOfficer = OpdServiceImpl.getReplaceString(signatureOfDentalOfficer);
				if(StringUtils.isNotEmpty(signatureOfDentalOfficer)) {
					masMedicalExamReprt.setSignatureOfDentalOfficer(signatureOfDentalOfficer);
				}  Neetu told this same 
				  End of Dental  
				 
				String binocularVisionGrade = payload.get("binocularVisionGrade").toString();
				binocularVisionGrade = OpdServiceImpl.getReplaceString(binocularVisionGrade);
				if(StringUtils.isNotEmpty(binocularVisionGrade)) {
					masMedicalExamReprt.setBinocularVisionGrade(binocularVisionGrade);
				}
				String evidenceOfTrachoma = payload.get("evidenceOfTrachoma").toString();
				evidenceOfTrachoma = OpdServiceImpl.getReplaceString(evidenceOfTrachoma);
				if(StringUtils.isNotEmpty(evidenceOfTrachoma)) {
					masMedicalExamReprt.setEvidenceOfTrachoma(evidenceOfTrachoma);
				}
				
				masMedicalExamReprt = MedicalExamServiceImpl.getVisionSomeValue(payload, masMedicalExamReprt);
				if(payload.get("serviceOfEmployee")!=null) {
				String serviceOfEmployee = payload.get("serviceOfEmployee").toString();
				serviceOfEmployee = OpdServiceImpl.getReplaceString(serviceOfEmployee);
				if(StringUtils.isNotEmpty(serviceOfEmployee) &&  !serviceOfEmployee.equalsIgnoreCase("0")) {
					masMedicalExamReprt.setServiceTypeId(Long.parseLong(serviceOfEmployee)); 
				}
				}
				String medicalCompositeNameValue = payload.get("medicalCompositeNameValue").toString();
				medicalCompositeNameValue = OpdServiceImpl.getReplaceString(medicalCompositeNameValue);
				if (StringUtils.isNotEmpty(medicalCompositeNameValue)) {
					masMedicalExamReprt.setMedicalCategoryId(Long.parseLong(medicalCompositeNameValue));
				}
				String medicalCompositeDate = payload.get("medicalCompositeDate").toString();
				medicalCompositeDate = OpdServiceImpl.getReplaceString(medicalCompositeDate);

				if (StringUtils.isNotEmpty(medicalCompositeDate) && medicalCompositeDate != null && MedicalExamServiceImpl.checkDateFormat(medicalCompositeDate)) {
					Date medicalCompositeDateValue = null;
					medicalCompositeDateValue = HMSUtil.convertStringTypeDateToDateType(medicalCompositeDate.trim());
					if(medicalCompositeDateValue!=null) {
					Timestamp catDate = new Timestamp(medicalCompositeDateValue.getTime());
					masMedicalExamReprt.setCategorydate(medicalCompositeDateValue);
				}}

			} catch (Exception e) {
				status = "0";
				e.printStackTrace();
			}

			masMedicalExamReprt.setMediceExamDate(new Date());
			masMedicalExamReprt.setVisitId(Long.parseLong(visitId));
			String patientId = payload.get("patientId").toString();
			patientId = OpdServiceImpl.getReplaceString(patientId);
			masMedicalExamReprt.setPatientId(Long.parseLong(patientId));

			String hospitalId = payload.get("hospitalId").toString();
			hospitalId = OpdServiceImpl.getReplaceString(hospitalId);
			masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));

			String saveInDraft = payload.get("saveInDraft").toString();
			saveInDraft = OpdServiceImpl.getReplaceString(saveInDraft);

			//try {
				if(payload.containsKey("authority")) {
				String authority = payload.get("authority").toString();
				authority = OpdServiceImpl.getReplaceString(authority);
				if (StringUtils.isNotEmpty(authority) && !authority.equalsIgnoreCase("")) {
					masMedicalExamReprt.setAuthority(authority);
				}
				}
				String place = payload.get("place").toString();
				place = OpdServiceImpl.getReplaceString(place);
				if (StringUtils.isNotEmpty(place) && !place.equalsIgnoreCase("")) {
					masMedicalExamReprt.setPlace(place);
				}

				String dateAME = payload.get("dateAME").toString();
				dateAME = OpdServiceImpl.getReplaceString(dateAME);
				if (StringUtils.isNotEmpty(dateAME) && !dateAME.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(dateAME.trim())) {
					Date dateAMEValue = HMSUtil.convertStringTypeDateToDateType(dateAME.trim());
					if(dateAMEValue!=null)
					masMedicalExamReprt.setDateMedicalBoardExam(dateAMEValue);
				}

			} catch (Exception e) {
				status = "0";
				e.printStackTrace();
			}
			if (StringUtils.isNotEmpty(saveInDraft) && !saveInDraft.equalsIgnoreCase("draftMa3A")) {
				String petStatus = payload.get("pet").toString();
				petStatus = OpdServiceImpl.getReplaceString(petStatus);
				if (petStatus != null && !petStatus.equalsIgnoreCase("0") && petStatus.equalsIgnoreCase("y")) {
					masMedicalExamReprt.setPetStatus(petStatus.trim());

				} else {
					//masMedicalExamReprt.setPetStatus(petStatus.trim());
				}
				if (masMedicalExamReprt != null && masMedicalExamReprt.getForwardUnitId() == null)
					masMedicalExamReprt.setForwardUnitId(null);
				
				String petDateValue = payload.get("petDateValue").toString();
				petDateValue = OpdServiceImpl.getReplaceString(petDateValue);
				if(StringUtils.isNotEmpty(petDateValue)) {
				Date medicalCompositeDateValue = null;
				medicalCompositeDateValue = HMSUtil.convertStringTypeDateToDateType(petDateValue);
				Timestamp catDate = new Timestamp(medicalCompositeDateValue.getTime());
				masMedicalExamReprt.setPetDate(catDate);
				}
				}
			//try {
				
			
			 * } catch (Exception e) { e.printStackTrace(); }
			 

			String userId = payload.get("userId").toString();
			userId = OpdServiceImpl.getReplaceString(userId);
			masMedicalExamReprt.setLastChgBy(Long.parseLong(userId));
			Users users=null;
			String designationIdCurrent=null;
			if(StringUtils.isNotEmpty(userId)) {
				 users=medicalExamDAO.getUserByUserId(Long.parseLong(userId));
				 if(users!=null) {
					 String currentUserDesignation=users.getDesignationId();
					 if(StringUtils.isNotEmpty(currentUserDesignation)) {
						 String [] currentUserDesignationVal=currentUserDesignation.split(",");
						 if(currentUserDesignationVal!=null && currentUserDesignationVal.length>0) {
							 designationIdCurrent=currentUserDesignationVal[0];
						 }
					 }
				 }
			}
			
			masMedicalExamReprt.setVisitId(Long.parseLong(visitId));

			String hospitalId = payload.get("hospitalId").toString();
			hospitalId = OpdServiceImpl.getReplaceString(hospitalId);
		//	masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));
			
			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftMa3A")) {

				masMedicalExamReprt.setStatus("s");
				masMedicalExamReprt.setApprovedBy("MA");
				masMedicalExamReprt.setMaUserId(Long.parseLong(userId));
			
				masMedicalExamReprt.setMaDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMaDesignationId(Long.parseLong(designationIdCurrent.trim()));
				masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));
			}

			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftMo3A")) {

				masMedicalExamReprt.setApprovedBy("MO");
				masMedicalExamReprt.setMoUserId(Long.parseLong(userId));
				String finalObservationMo = payload.get("finalObservationMo").toString();
				finalObservationMo = OpdServiceImpl.getReplaceString(finalObservationMo);
				masMedicalExamReprt.setFinalobservation(finalObservationMo);
				
				masMedicalExamReprt.setMoDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMoDesignationId(Long.parseLong(designationIdCurrent.trim()));
			
				
			//	masMedicalExamReprt = getActionForMedicalExam(payload, masMedicalExamReprt);
				masMedicalExamReprt.setHospitalId(Long.parseLong(hospitalId));
			}
			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftRMo3A")) {

				masMedicalExamReprt.setApprovedBy("RMO");
				masMedicalExamReprt.setCmUserId(Long.parseLong(userId));

				String finalObservationRmo = payload.get("finalObservationRmo").toString();
				finalObservationRmo = OpdServiceImpl.getReplaceString(finalObservationRmo);
				masMedicalExamReprt.setAaFinalObservation(finalObservationRmo);

				String remarksRmo = payload.get("remarksRmo").toString();
				remarksRmo = OpdServiceImpl.getReplaceString(remarksRmo);
				masMedicalExamReprt.setAaRemarks(remarksRmo);

				masMedicalExamReprt.setCmDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setCmDesignationId(Long.parseLong(designationIdCurrent.trim()));
			
				
				//masMedicalExamReprt = getActionForMedicalExam(payload, masMedicalExamReprt);
				masMedicalExamReprt.setRmoHospitalId(Long.parseLong(hospitalId));
			}

			if (StringUtils.isNotEmpty(saveInDraft) && saveInDraft.equalsIgnoreCase("draftPRMo3A")) {

				masMedicalExamReprt.setApprovedBy("PRMo");
				masMedicalExamReprt.setMdUserId(Long.parseLong(userId));
				String finalObservationPRmo = payload.get("finalObservationPRmo").toString();
				finalObservationPRmo = OpdServiceImpl.getReplaceString(finalObservationPRmo);
				masMedicalExamReprt.setPaFinalobservation(finalObservationPRmo);

				String remarksPRmo = payload.get("remarksPRmo").toString();
				remarksPRmo = OpdServiceImpl.getReplaceString(remarksPRmo);
				masMedicalExamReprt.setPaRemarks(remarksPRmo);

				masMedicalExamReprt.setMdDate(new Date());
				if(StringUtils.isNotEmpty(designationIdCurrent))
					masMedicalExamReprt.setMdDesignationId(Long.parseLong(designationIdCurrent.trim()));
			
				masMedicalExamReprt.setPdmsHospitalId(Long.parseLong(hospitalId));
			}
			
			if (StringUtils.isNotEmpty(hospitalId)) {
				List<MasUnit> listMasUnit = medicalExamDAO.getMasUnitListByHospitalId(Long.parseLong(hospitalId));
				if (CollectionUtils.isNotEmpty(listMasUnit))
					masMedicalExamReprt.setUnitId(listMasUnit.get(0).getUnitId());
			}
			
			if(payload.containsKey("rankId")) {
				String rankId = payload.get("rankId").toString();
				rankId = OpdServiceImpl.getReplaceString(rankId);
				if(StringUtils.isNotEmpty(rankId))
				masMedicalExamReprt.setRankId(Long.parseLong(rankId));
				}
			if(payload.containsKey("unitIdOn") && payload.get("unitIdOn")!=null) {
				String unitOrSlip=payload.get("unitIdOn").toString();
				unitOrSlip=OpdServiceImpl.getReplaceString(unitOrSlip);
				
			if(StringUtils.isNotEmpty(unitOrSlip) && !unitOrSlip.equalsIgnoreCase("undefined")) {
				masMedicalExamReprt.setUnitId(Long.parseLong(unitOrSlip));
			}
			}
			if(payload.containsKey("branchOrTradeIdOn") && payload.get("branchOrTradeIdOn")!=null) {
			String branchOrTrade=payload.get("branchOrTradeIdOn").toString();
			branchOrTrade=OpdServiceImpl.getReplaceString(branchOrTrade);
			if(StringUtils.isNotEmpty(branchOrTrade) && !branchOrTrade.equalsIgnoreCase("undefined")) {
				masMedicalExamReprt.setBranchId(Long.parseLong(branchOrTrade));
			}
			}
			
			
			if(payload.containsKey("dob")) {
				String dateOfBirth = payload.get("dob").toString();
				dateOfBirth = OpdServiceImpl.getReplaceString(dateOfBirth);
				if (StringUtils.isNotEmpty(dateOfBirth) && MedicalExamServiceImpl.checkDateFormat(dateOfBirth)) {
					Date dateOfBirthD = HMSUtil.convertStringTypeDateToDateType(dateOfBirth.trim());
					if(dateOfBirthD!=null)
					masMedicalExamReprt.setDateOfBirth(dateOfBirthD);
				}
				}
			
		if(payload.containsKey("meAgeForOn")) {
			String meAgeForOn = payload.get("meAgeForOn").toString();
			meAgeForOn = OpdServiceImpl.getReplaceString(meAgeForOn);
			String ageForMe="";
			if (StringUtils.isNotEmpty(meAgeForOn) && !meAgeForOn.equalsIgnoreCase("")) {
			if (StringUtils.isNotEmpty(meAgeForOn)) {
				String[] ageMale = meAgeForOn.split("/");
				ageForMe = ageMale[0];
			}
				masMedicalExamReprt.setApparentAge(ageForMe);
			}
		}
		
		if(payload.containsKey("totalService")) {
				String totalService = payload.get("totalService").toString();
				totalService = OpdServiceImpl.getReplaceString(totalService);
				if (StringUtils.isNotEmpty(totalService)) {
					masMedicalExamReprt.setTotalService(totalService);
				}
				}
			
		
		if(payload.get("dateOfPatient")!=null) {
			String dateOfPatient = payload.get("dateOfPatient").toString();
			dateOfPatient = OpdServiceImpl.getReplaceString(dateOfPatient);
			Date dateOfPatientD = null;
			if(StringUtils.isNotEmpty(dateOfPatient) && MedicalExamServiceImpl.checkDateFormat(dateOfPatient)) {
				dateOfPatientD = HMSUtil.convertStringTypeDateToDateType(dateOfPatient.trim());
				if(dateOfPatientD!=null)
				masMedicalExamReprt.setDateofcommun(dateOfPatientD); 
			}
			}
		
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = submitDentalAndPhysicalCapacity(payload, masMedicalExamReprt);
			}
			// masMedicalExamReprt=lifeStyleFactors( payload, masMedicalExamReprt);
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = centralNervousSystemAndOthers(payload, masMedicalExamReprt);
			}
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = respiratoryAndGastroIntestinalSystem(payload, masMedicalExamReprt);
			}
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = cardioVascularSystemFo3A(payload, masMedicalExamReprt);
			}
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = submitHearing(payload, masMedicalExamReprt);
			}
			
			if(masMedicalExamReprt!=null) {
			masMedicalExamReprt = submitVision(payload, masMedicalExamReprt);
			}
			// For Respiratory System
			if(masMedicalExamReprt!=null) {
				masMedicalExamReprt = getRespiratorySystem(payload, masMedicalExamReprt);
				}
			
			masMedicalExamReprt =getSpecialExaminationWhenApplicable3A(payload, masMedicalExamReprt);
			
			if (StringUtils.isNotEmpty(saveInDraft) &&  !saveInDraft.equalsIgnoreCase("draftMa3A")) {
			masMedicalExamReprt =	getCommonInfoValue(payload, masMedicalExamReprt);
			}
			String typeOfCommission = payload.get("typeOfCommission").toString();
			typeOfCommission = OpdServiceImpl.getReplaceString(typeOfCommission);
			if(StringUtils.isNotEmpty(typeOfCommission) &&  !typeOfCommission.equalsIgnoreCase("0")) {
				masMedicalExamReprt.setTypeofcommision(typeOfCommission); 
			}
			String signatureOfOfficer = payload.get("signatureOfOfficer").toString();
			signatureOfOfficer = OpdServiceImpl.getReplaceString(signatureOfOfficer);
			if(StringUtils.isNotEmpty(signatureOfOfficer)) {
				masMedicalExamReprt.setSignatureOfOfficer(signatureOfOfficer);
			}
			
			
			if(masMedicalExamReprt!=null) {
			  msgStatus = medicalExamDAO.saveOrUpdateMedicalExam3A(masMedicalExamReprt, payload, patientId,
					hospitalId, userId);
			}
			if(masMedicalExamReprt==null) {
				msgStatus=0+"##"+"Data is not updated because something is wrong";
				}
			
			String[] msgStatusa = msgStatus.split("##");
			medicalExamId = Long.parseLong(msgStatusa[0]);
			msgStatussss = msgStatusa[1];
			 msgForPatient=msgStatusa[2];
			if (medicalExamId != 0) {
				status = "1";
				countInvesResultEmpty=medicalExamDAO.getInvestigationResultEmpty(Long.parseLong(visitId));
			} else {
				status = "0";
				countInvesResultEmpty=0;
			}
		} catch (Exception e) {
			status = "0";
			msgStatussss = "Data is not updated because something is wrong";
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("visitId", medicalExamId);
		json.put("status", status);
		json.put("errorMes", msgStatussss);
		json.put("countInvesResultEmptystatus", countInvesResultEmpty);
		json.put("msgForPatient", msgForPatient);
		return json.toString();
	
	}

	
	public static MasMedicalExamReport getSpecialExaminationWhenApplicable3A(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		if (payload.get("manifestHypermetropiaMyopia") != null) {
			String manifestHypermetropiaMyopia = payload.get("manifestHypermetropiaMyopia").toString();
			manifestHypermetropiaMyopia = OpdServiceImpl.getReplaceString(manifestHypermetropiaMyopia);
			masMedicalExamReprt.setManifestHypermetropiaMyopia(manifestHypermetropiaMyopia);
		}
		if (payload.get("coverTest") != null) {
			String coverTest = payload.get("coverTest").toString();
			coverTest = OpdServiceImpl.getReplaceString(coverTest);
			masMedicalExamReprt.setCoverTest(coverTest);
		}
		
		if (payload.get("diaphragmTest") != null) {
			String diaphragmTest = payload.get("diaphragmTest").toString();
			diaphragmTest = OpdServiceImpl.getReplaceString(diaphragmTest);
			masMedicalExamReprt.setDiaphragmTest(diaphragmTest);
		}
		
		
		if (payload.get("fundiMedia") != null) {
			String fundiMedia = payload.get("fundiMedia").toString();
			fundiMedia = OpdServiceImpl.getReplaceString(fundiMedia);
			masMedicalExamReprt.setFundiMedia(fundiMedia);
		}
		if (payload.get("fieldSpecial") != null) {
			String specialField = payload.get("fieldSpecial").toString();
			specialField = OpdServiceImpl.getReplaceString(specialField);
			masMedicalExamReprt.setSpecialField(specialField); 
		}
		
		if (payload.get("nightVisualCapacity") != null) {
			String nightVisualCapacity = payload.get("nightVisualCapacity").toString();
			nightVisualCapacity = OpdServiceImpl.getReplaceString(nightVisualCapacity);
			masMedicalExamReprt.setNightVisualCapacity(nightVisualCapacity);
		}
		
		///////////////////////////////////////////////////////
		
		
		if (payload.get("convergenceC") != null) {
			String convergenceC = payload.get("convergenceC").toString();
			convergenceC = OpdServiceImpl.getReplaceString(convergenceC);
			if(StringUtils.isNotEmpty(convergenceC)) {
				//masMedicalExamReprt.setConvergenceC(Long.parseLong(convergenceC));
				masMedicalExamReprt.setConvergenceC(convergenceC);	
			}
			}
		
		
		if (payload.get("convergenceSC") != null) {
			String convergenceSC = payload.get("convergenceSC").toString();
			convergenceSC = OpdServiceImpl.getReplaceString(convergenceSC);
			if(StringUtils.isNotEmpty(convergenceSC))
			//masMedicalExamReprt.setConvergenceSc(Long.parseLong(convergenceSC));
				masMedicalExamReprt.setConvergenceSc(convergenceSC);
		}
		if (payload.get("accomodationR") != null) {
			String accomodationR = payload.get("accomodationR").toString();
			accomodationR = OpdServiceImpl.getReplaceString(accomodationR);
			masMedicalExamReprt.setAccommodationR(accomodationR); 
		}
		
		if (payload.get("accomodationL") != null) {
			String accomodationL = payload.get("accomodationL").toString();
			accomodationL = OpdServiceImpl.getReplaceString(accomodationL);
			masMedicalExamReprt.setAccommodationL(accomodationL);
		}
		
		////////////////////////////////////
		if (payload.get("manifestHypermetropiaMyopiaRemarks") != null) {
			String manifestHypermetropiaMyopiaRemarks = payload.get("manifestHypermetropiaMyopiaRemarks").toString();
			manifestHypermetropiaMyopiaRemarks = OpdServiceImpl.getReplaceString(manifestHypermetropiaMyopiaRemarks);
			 
			masMedicalExamReprt.setManifestHypeMyopiaRemarks(manifestHypermetropiaMyopiaRemarks);
		}
		if (payload.get("manifestHypermetropiaMyopiaDate") != null) {
			String manifestHypermetropiaMyopiaDate = payload.get("manifestHypermetropiaMyopiaDate").toString();
			manifestHypermetropiaMyopiaDate = OpdServiceImpl.getReplaceString(manifestHypermetropiaMyopiaDate);
			if(StringUtils.isNotEmpty(manifestHypermetropiaMyopiaDate) && MedicalExamServiceImpl.checkDateFormat(manifestHypermetropiaMyopiaDate)) {
				Date	manifestHypermetropiaMyopiaDateValue = HMSUtil.convertStringTypeDateToDateType(manifestHypermetropiaMyopiaDate.trim());
				if(manifestHypermetropiaMyopiaDateValue!=null)
				masMedicalExamReprt.setManifestHypeMyopiaDate(manifestHypermetropiaMyopiaDateValue); 
			}
		}
		
		
		if (payload.get("noseThroatSinusesRemarks") != null) {
			String noseThroatSinusesRemarks = payload.get("noseThroatSinusesRemarks").toString();
			noseThroatSinusesRemarks = OpdServiceImpl.getReplaceString(noseThroatSinusesRemarks);
			masMedicalExamReprt.setNoseThroatSinusesRemarks(noseThroatSinusesRemarks);
		}
		
		
		if (payload.get("noseThroatSinusesDate") != null) {
			String noseThroatSinusesDate = payload.get("noseThroatSinusesDate").toString();
			noseThroatSinusesDate = OpdServiceImpl.getReplaceString(noseThroatSinusesDate);
			if(StringUtils.isNotEmpty(noseThroatSinusesDate) && MedicalExamServiceImpl.checkDateFormat(noseThroatSinusesDate)) {
			Date	noseThroatSinusesDateValue = HMSUtil.convertStringTypeDateToDateType(noseThroatSinusesDate.trim());
			if(noseThroatSinusesDateValue!=null)
			masMedicalExamReprt.setNoseThroatSinusesDate(noseThroatSinusesDateValue); 
			}
		}
		
		
		if (payload.get("signatureOfENTSpecialist") != null) {
			String signatureOfENTSpecialist = payload.get("signatureOfENTSpecialist").toString();
			signatureOfENTSpecialist = OpdServiceImpl.getReplaceString(signatureOfENTSpecialist);
			masMedicalExamReprt.setSignatureOfENTSpecialist(signatureOfENTSpecialist);
		}
		if (payload.containsKey("signatureOfMedicalSpecialist") && payload.get("signatureOfMedicalSpecialist") != null) {
			String signatureOfMedicalSpecialist = payload.get("signatureOfMedicalSpecialist").toString();
			signatureOfMedicalSpecialist = OpdServiceImpl.getReplaceString(signatureOfMedicalSpecialist);
			masMedicalExamReprt.setSignatureMedicalSpecialist(signatureOfMedicalSpecialist);
		}
		
		if (payload.get("externalEarR") != null) {
			String externalEarR = payload.get("externalEarR").toString();
			externalEarR = OpdServiceImpl.getReplaceString(externalEarR);
			masMedicalExamReprt.setExternalEarR(externalEarR);
		}
		
		if (payload.get("externalEarL") != null) {
			String externalEarL = payload.get("externalEarL").toString();
			externalEarL = OpdServiceImpl.getReplaceString(externalEarL);
			masMedicalExamReprt.setExternalEarL(externalEarL);
		}
		
		
		if (payload.get("middleEarR") != null) {
			String middleEarR = payload.get("middleEarR").toString();
			middleEarR = OpdServiceImpl.getReplaceString(middleEarR);
			masMedicalExamReprt.setMiddleEarR(middleEarR);
		}
		
		if (payload.get("middleEarL") != null) {
			String middleEarL = payload.get("middleEarL").toString();
			middleEarL = OpdServiceImpl.getReplaceString(middleEarL);
			masMedicalExamReprt.setMiddleEar(middleEarL);
		}
		
		if (payload.get("innerEarR") != null) {
			String innerEarR = payload.get("innerEarR").toString();
			innerEarR = OpdServiceImpl.getReplaceString(innerEarR);
			masMedicalExamReprt.setInnerEarR(innerEarR);
		}
		
		if (payload.get("innerEarL") != null) {
			String innerEarL = payload.get("innerEarL").toString();
			innerEarL = OpdServiceImpl.getReplaceString(innerEarL);
			masMedicalExamReprt.setInnerEarL(innerEarL);
		}
		return masMedicalExamReprt;
	}
	
	
	
	
	public static MasMedicalExamReport cardioVascularSystemFo3A(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		try {
		if (payload.get("pulse") != null && !payload.get("pulse").equals("")) {
			String pulse = payload.get("pulse").toString();
			pulse = OpdServiceImpl.getReplaceString(pulse);
			masMedicalExamReprt.setPulseRates(pulse);
		}
		if (payload.get("bp") != null) {
			String bp = payload.get("bp").toString();
			bp = OpdServiceImpl.getReplaceString(bp);
			masMedicalExamReprt.setBpSystolic(bp);

		}

		if (payload.get("bp1") != null) {
			String bp1 = payload.get("bp1").toString();
			bp1 = OpdServiceImpl.getReplaceString(bp1);
			masMedicalExamReprt.setBpDiastolic(bp1);

		}
		if (payload.get("peripheralPulsations") != null) {
			String peripheralPulsations = payload.get("peripheralPulsations").toString();
			peripheralPulsations = OpdServiceImpl.getReplaceString(peripheralPulsations);
			masMedicalExamReprt.setPeripheralPulsations(peripheralPulsations);
		}

		if (payload.get("heartSize") != null) {
			String heartSize = payload.get("heartSize").toString();
			heartSize = OpdServiceImpl.getReplaceString(heartSize);
			masMedicalExamReprt.setHeartSize(heartSize);
		}
		if (payload.get("sounds") != null) {
			String sounds = payload.get("sounds").toString();
			sounds = OpdServiceImpl.getReplaceString(sounds);
			masMedicalExamReprt.setSounds(sounds);
		}
		if (payload.get("rhythm") != null) {
			String rhythm = payload.get("rhythm").toString();
			rhythm = OpdServiceImpl.getReplaceString(rhythm);
			masMedicalExamReprt.setRhythm(rhythm);
		}
		if (payload.get("ecgRDMT") != null) {
			String ecgRDMT = payload.get("ecgRDMT").toString();
			ecgRDMT = OpdServiceImpl.getReplaceString(ecgRDMT);
			masMedicalExamReprt.setEcgRDMT(ecgRDMT);
		}
		Date ecgDatedDateValue = null;
		if (payload.get("ecgDated") != null) {
			String ecgDated = payload.get("ecgDated").toString();
			ecgDated = OpdServiceImpl.getReplaceString(ecgDated);
			if(StringUtils.isNotEmpty(ecgDated) ) {
				ecgDatedDateValue = HMSUtil.convertStringTypeDateToDateType(ecgDated);
				masMedicalExamReprt.setEcgDated(ecgDatedDateValue);
			}
			}
		
		if (payload.get("ecgReport") != null) {
			String ecgReport = payload.get("ecgReport").toString();
			ecgReport = OpdServiceImpl.getReplaceString(ecgReport);
			masMedicalExamReprt.setEcgReport(ecgReport);
		}
		if (payload.get("ecgAMT") != null) {
			String ecgAMT = payload.get("ecgAMT").toString();
			ecgAMT = OpdServiceImpl.getReplaceString(ecgAMT);
			masMedicalExamReprt.setEcgAMT(ecgAMT);
		}
		
		if (payload.get("ecgAmtDated") != null) {
			String ecgAmtDated = payload.get("ecgAmtDated").toString();
			ecgAmtDated = OpdServiceImpl.getReplaceString(ecgAmtDated);
			if(StringUtils.isNotEmpty(ecgAmtDated)) {
			ecgDatedDateValue = HMSUtil.convertStringTypeDateToDateType(ecgAmtDated);
			masMedicalExamReprt.setEcgAmtDated(ecgDatedDateValue);
		}
		}
		if (payload.get("ecgAmtReport") != null) {
			String ecgAmtReport = payload.get("ecgAmtReport").toString();
			ecgAmtReport = OpdServiceImpl.getReplaceString(ecgAmtReport);
			masMedicalExamReprt.setEcgAmtReport(ecgAmtReport);
		}
		
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return masMedicalExamReprt;
	}
	public static MasMedicalExamReport getRespiratorySystem(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt) {
		if (payload.get("xRayChestPANos") != null) {
			String xRayChestPANos = payload.get("xRayChestPANos").toString();
			xRayChestPANos = OpdServiceImpl.getReplaceString(xRayChestPANos);
			//masMedicalExamReprt.setxRayChestPANos(xRayChestPANos);
		}
		if (payload.get("xRayChestPANosDated") != null) {
			String xRayChestPANosDated = payload.get("xRayChestPANosDated").toString();
			xRayChestPANosDated = OpdServiceImpl.getReplaceString(xRayChestPANosDated);
			if(StringUtils.isNotEmpty(xRayChestPANosDated) && MedicalExamServiceImpl.checkDateFormat(xRayChestPANosDated)) {
			Date xRayChestPANosDatedVal = HMSUtil.convertStringTypeDateToDateType(xRayChestPANosDated.trim());
			//masMedicalExamReprt.setxRayChestPANosDated(xRayChestPANosDatedVal);
		}
		}
		
		if (payload.get("xRayChestPANosReport") != null) {
			String xRayChestPANosReport = payload.get("xRayChestPANosReport").toString();
			xRayChestPANosReport = OpdServiceImpl.getReplaceString(xRayChestPANosReport);
			//masMedicalExamReprt.setxRayChestPANosReport(xRayChestPANosReport);
		}
		return masMedicalExamReprt;
	}
	
	
	@Override
	public String getPatientDetailOfVisitIdAfms18P(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		try {
			Map<String, Object> mapObject = medicalExamDAO.getpatientDetailForAFMS18(jsondata);

			List<Visit> listVisit = (List<Visit>) mapObject.get("listVisit");
			List<MasServiceType>listMasServiceType=masterDao.getServiceTypeList();
			Users users=null;
			try {
				users= medicalExamDAO.getUserByUserId(jsondata.get("serviceNumber").toString());
			}
			
			catch(Exception e) {
				e.printStackTrace();
			}
			List<MasEmployee> listMasEmployee=null;
			if(users!=null)
				  listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(users.getServiceNo());
			else {
				listMasEmployee=systemAdminDao.getMasEmployeeForAdmin(jsondata.get("serviceNumber").toString());
			}
			if (CollectionUtils.isNotEmpty(listVisit)) {
				for (Visit visit : listVisit) {
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("patientName", visit.getPatient().getPatientName());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						Date s = visit.getPatient().getDateOfBirth();
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getMasAdministrativeSex() != null) {
						jsonMap.put("gender", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						jsonMap.put("genderId", visit.getPatient().getMasAdministrativeSex().getAdministrativeSexId());
					}
					if (visit.getPatient() != null && visit.getPatient().getMasRank() != null) {
						jsonMap.put("rankName", visit.getPatient().getMasRank().getRankName());
					} else {
						jsonMap.put("rankName", "");
					}
					jsonMap.put("meTypeName", visit.getMasMedExam().getMedicalExamName());
					jsonMap.put("meTypeCode", visit.getMasMedExam().getMedicalExamCode());
					String visitstatus = "";
					MasMedicalExamReport masMedicalExamReport = null;
					masMedicalExamReport = visit.getMasMedicalExamReport();
					if (masMedicalExamReport == null) {
						visitstatus = "New";
					}

					if (masMedicalExamReport != null && visit.getMasMedicalExamReport().getStatus()!=null) {

						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("s")) {
							visitstatus = "Saved";
						}

						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("af")
								|| visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("f")) {
							visitstatus = "Forwarded";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("rj")) {
							visitstatus = "Rejected";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("pe")) {
							visitstatus = "Pending";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("ac")) {
							visitstatus = "Approved";
						}
						if (visit.getMasMedicalExamReport().getStatus().equalsIgnoreCase("po")) {
							visitstatus = "New";
						}
					}
					else {
						visitstatus = "New";
					}
					jsonMap.put("status", visitstatus);
					jsonMap.put("visitId", visit.getVisitId());
					jsonMap.put("patientId", visit.getPatientId());

					jsonMap.put("serviceNo", visit.getPatient().getServiceNo());
					jsonMap.put("departmentId", visit.getDepartmentId());
					jsonMap.put("examTypeId", visit.getExamId());
					jsonMap.put("appointmentId", visit.getAppointmentTypeId());

					if (visit.getPatient() != null && visit.getPatient().getDateOfBirth() != null) {
						String dateOfBirth = HMSUtil.convertDateToStringFormat(visit.getPatient().getDateOfBirth(),
								"dd/MM/yyyy");

						jsonMap.put("dateOfBirth", dateOfBirth);
					}

					if (visit.getPatient() != null && visit.getPatient().getServiceJoinDate() != null) {

						Date s1 = visit.getPatient().getServiceJoinDate();
						Period serviceDate = ProjectUtils.getDOB(s1);
						jsonMap.put("totalService", serviceDate.getYears());
					}
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getTotalService()!=null) {
						jsonMap.put("totalService",visit.getMasMedicalExamReport().getTotalService());
					}
					
					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unit", visit.getPatient().getMasUnit().getUnitName());
					}
					 
					if (visit.getPatient() != null && visit.getPatient().getMedicalCategoryId() != null) {
						jsonMap.put("medicalCategory",
								visit.getPatient().getMasMedicalCategory().getMedicalCategoryName());
						jsonMap.put("medicalCompositeNameValue", visit.getPatient().getMedicalCategoryId());
						String medicalCategoryDate = "";
						if (visit.getPatient().getMasMedicalCategory().getLastChgDate() != null) {
							medicalCategoryDate = HMSUtil.convertDateToStringFormat(
									visit.getPatient().getMasMedicalCategory().getLastChgDate(), "dd/MM/yyyy");
							jsonMap.put("medicalCategoryDate", medicalCategoryDate);
						} else {
							jsonMap.put("medicalCategoryDate", "");
						}

					} else {
						jsonMap.put("medicalCategory", "");
						jsonMap.put("medicalCompositeNameValue", "");
						jsonMap.put("medicalCategoryDate", "");
					}
					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null) {
						jsonMap.put("tradeBranch", visit.getPatient().getMasTrade().getTradeName());
					} else {
						jsonMap.put("tradeBranch", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getTradeId() != null) {
						jsonMap.put("branchOrTradeIdOn", visit.getPatient().getMasTrade().getTradeId());
					} else {
						jsonMap.put("branchOrTradeIdOn", "");
					}

					if (visit.getPatient() != null && visit.getPatient().getUnitId() != null) {
						jsonMap.put("unitIdOn", visit.getPatient().getMasUnit().getUnitId());
					} else {
						jsonMap.put("unitIdOn", "");
					}
					String dateOfExam ="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getMediceExamDate()!=null) {
						  dateOfExam = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getMediceExamDate(), "dd/MM/yyyy");
					}
					else {
					  dateOfExam = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
					
					}
					jsonMap.put("dateOfExam", dateOfExam);
					
					String dateOfPatient = "";
					if (visit.getMasMedicalExamReport()!=null &&  visit.getMasMedicalExamReport().getDateofcommun() != null) {
						dateOfPatient = HMSUtil.convertDateToStringFormat(visit.getMasMedicalExamReport().getDateofcommun(),
								"dd/MM/yyyy");
					}

					jsonMap.put("dateOfPatient", dateOfPatient);
					
					//String dateOfExam = HMSUtil.convertDateToStringFormat(new Date(), "dd/MM/yyyy");
					//jsonMap.put("dateOfExam", dateOfExam);

					if (visit.getOpdPatientDetails() != null
							&& visit.getOpdPatientDetails().getOpdPatientDetailsId() != null) {
						jsonMap.put("opdPatientDetailId", visit.getOpdPatientDetails().getOpdPatientDetailsId());
					} else {
						jsonMap.put("opdPatientDetailId", "");
					}

					if (visit.getMasMedicalExamReport() != null) {

						if (visit.getMasMedicalExamReport().getAuthority() != null) {
							jsonMap.put("authority", visit.getMasMedicalExamReport().getAuthority());
						} else {
							jsonMap.put("authority", "");
						}
						if (visit.getMasMedicalExamReport().getPlace() != null) {
							jsonMap.put("place", visit.getMasMedicalExamReport().getPlace());
						} else {
							jsonMap.put("place", "");
						}

					} else {
						jsonMap.put("authority", "");
						jsonMap.put("place", "");
					}
					String dateAME = "";
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDateMedicalBoardExam() != null) {
						dateAME = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDateMedicalBoardExam(), "dd/MM/yyyy");
						jsonMap.put("dateAME", dateAME);
					} else {
						jsonMap.put("dateAME", "");
					}

					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getApprovedBy() != null) {
						jsonMap.put("approvedBy", visit.getMasMedicalExamReport().getApprovedBy());
					} else {
						jsonMap.put("approvedBy", "");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIdentificationMarks1()!=null) {
						jsonMap.put("identificationMarks1", visit.getMasMedicalExamReport().getIdentificationMarks1());
					}
					else {
						jsonMap.put("identificationMarks1","");
					}
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIdentificationMarks2()!=null) {
						jsonMap.put("identificationMarks2", visit.getMasMedicalExamReport().getIdentificationMarks2());
					}
					else {
						jsonMap.put("identificationMarks2","");
					}
					String disabilityBeforeJoining="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getDisabilitybefore()!=null) {
						disabilityBeforeJoining=visit.getMasMedicalExamReport().getDisabilitybefore();
					}
					jsonMap.put("disabilityBeforeJoining", disabilityBeforeJoining);
					String marritalStatus="";
					if( visit.getPatient()!=null && visit.getPatient().getMasMaritalStatus()!=null)
						marritalStatus=visit.getPatient().getMasMaritalStatus().getMaritalStatusName();
					jsonMap.put("marritalStatus", marritalStatus);
					String address="";
					if( visit.getPatient()!=null && visit.getPatient().getAddressLine1()!=null) {
						address=visit.getPatient().getAddressLine1();
					}
					
					if( visit.getPatient()!=null && visit.getPatient().getAddressLine2()!=null) {
						address= address + "," + visit.getPatient().getAddressLine2();
					}
					
					if( visit.getPatient()!=null && visit.getPatient().getAddressLine3()!=null) {
						address= address + "," + visit.getPatient().getAddressLine3();
					}
					
					if( visit.getPatient()!=null && visit.getPatient().getAddressLine4()!=null) {
						address= address + "," + visit.getPatient().getAddressLine4();
					}
					
					if( visit.getPatient()!=null && visit.getPatient().getPincode()!=null) {
						address= address + "," + visit.getPatient().getPincode();
					}
					
					jsonMap.put("address",address);
					
					String authority="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getAuthority()!=null) {
						authority=visit.getMasMedicalExamReport().getAuthority();
					}
					jsonMap.put("authority",authority);
						
					
					String dateOfReport = "";
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDateOfReporting() != null) {
						dateOfReport = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDateOfReporting(), "dd/MM/yyyy");
						
					}  
					jsonMap.put("dateOfReport", dateOfReport);
					
					String dateOfRelease = "";
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getDaterelease() != null) {
						dateOfRelease = HMSUtil.convertDateToStringFormat(
								visit.getMasMedicalExamReport().getDaterelease(), "dd/MM/yyyy");
					}  
					jsonMap.put("dateOfRelease", dateOfRelease);
					
					Long serviceType = null;
					if (visit.getMasMedicalExamReport() != null
							&& visit.getMasMedicalExamReport().getServiceTypeId() != null) {
						serviceType=visit.getMasMedicalExamReport().getServiceTypeId();
						
					}  
					jsonMap.put("serviceType", serviceType);
					
					String particularsOfPreviousService="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getParticularofpreviousservice()!=null) {
						particularsOfPreviousService=visit.getMasMedicalExamReport().getParticularofpreviousservice();
					}
					jsonMap.put("particularsOfPreviousService",particularsOfPreviousService);
					
					
					String disabilityPensionRecieved="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getReductionDisablePension()!=null) {
						disabilityPensionRecieved=visit.getMasMedicalExamReport().getReductionDisablePension();
					}
					jsonMap.put("disabilityPensionRecieved",disabilityPensionRecieved);
					
					String claimAnyDisability="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getClamingdisability()!=null) {
						claimAnyDisability=visit.getMasMedicalExamReport().getClamingdisability();
					}
					jsonMap.put("claimAnyDisability",claimAnyDisability);
					
					
					String anyOtherInformation="";
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getAnyOtherInformationAboutYo()!=null) {
						anyOtherInformation=visit.getMasMedicalExamReport().getAnyOtherInformationAboutYo();
					}
					jsonMap.put("anyOtherInformation",anyOtherInformation);
					
					String dateOfWitness="";
					 
					if( visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getPatientWitness()!=null) {
						 Long witnessId=null;
						if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getWitnessSig()!=null) {
							witnessId=visit.getMasMedicalExamReport().getWitnessSig();
						}
						jsonMap.put("witnessId",witnessId);
							jsonMap.put("serviceNoEmployee",visit.getMasMedicalExamReport().getPatientWitness().getServiceNo());
							String rankOfEmployee="";
						if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getPatientWitness()!=null && visit.getMasMedicalExamReport().getPatientWitness().getMasRank()!=null  ) {
							//MasRank masRank=systemAdminDao.getRankByRankCode((visit.getMasMedicalExamReport().getMasEmployee().getMasRank()));
							
							MasRank masRank=null;
							if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getPatientWitness()!=null && visit.getMasMedicalExamReport().getPatientWitness().getMasRank()!=null) {
							  masRank=visit.getMasMedicalExamReport().getPatientWitness().getMasRank();//systemAdminDao.getRankByRankCode((visit.getMasMedicalExamReport().getPatientWitness().getMasRank()));
							}
							if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
							jsonMap.put("rankOfEmployee",  masRank.getRankName());
							else
								jsonMap.put("rankOfEmployee", "");
							 
							}
						
						String employeeName="";
						if(visit.getMasMedicalExamReport().getPatientWitness().getEmployeeName()!=null) {
							employeeName=visit.getMasMedicalExamReport().getPatientWitness().getEmployeeName();
						}
							jsonMap.put("signatureOfWitness",employeeName);
							
					}
					String  signatureOfIndividual="";
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIndividualPatient()!=null) {
						if(visit.getMasMedicalExamReport().getIndividualPatient().getEmployeeName()!=null)
						signatureOfIndividual=visit.getMasMedicalExamReport().getIndividualPatient().getEmployeeName();
						//if(visit.getMasMedicalExamReport().getIndividualEmployees().getEmployeeName()!=null)
							//signatureOfIndividual+=""+visit.getMasMedicalExamReport()getIndividualEmployees().getEmployeeName();
					}
					jsonMap.put("signatureOfIndividual",signatureOfIndividual);
					Long signatureOfIndividualId=null;
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getIndividualSig()!=null) {
						signatureOfIndividualId=visit.getMasMedicalExamReport().getIndividualSig();
					}
					jsonMap.put("signatureOfIndividualId",signatureOfIndividualId);
					
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getWitnessDate()!=null) {
						dateOfWitness = HMSUtil.convertDateToStringFormat(
							visit.getMasMedicalExamReport().getWitnessDate(), "dd/MM/yyyy");
					}
					jsonMap.put("dateOfWitness",dateOfWitness);
					 
					Long forwardedDesignationId=null;
					if(visit.getMasMedicalExamReport()!=null && visit.getMasMedicalExamReport().getFowardedDesignationId()!=null) {
						forwardedDesignationId=visit.getMasMedicalExamReport().getFowardedDesignationId();
					}
					jsonMap.put("forwardedDesignationId",forwardedDesignationId);
					
					String carefullyExaminedServiceNo="";
					if(users!=null && users.getServiceNo()!=null) {
						carefullyExaminedServiceNo=users.getServiceNo();
					}
					jsonMap.put("carefullyExaminedServiceNo",carefullyExaminedServiceNo);
					
					String carefullyExaminedEmployeeName="";
					if(users!=null) {
						if(users.getFirstName()!=null)
						carefullyExaminedEmployeeName=users.getFirstName();
						if(users.getLastName()!=null)
							carefullyExaminedEmployeeName+=" "+users.getLastName();
					}
					jsonMap.put("carefullyExaminedEmployeeName",carefullyExaminedEmployeeName);
					
					String carefullyExaminedRank="";
					if(CollectionUtils.isNotEmpty(listMasEmployee)) {
						if(listMasEmployee.get(0).getMasRank()!=null ) {
							MasRank masRank=systemAdminDao.getRankByRankCode((listMasEmployee.get(0).getMasRank()));
							if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
								carefullyExaminedRank=masRank.getRankName();
						}
						}
					jsonMap.put("carefullyExaminedRank",carefullyExaminedRank);
					
					String carefullyUnitName="";
					if(CollectionUtils.isNotEmpty(listMasEmployee)) {
						if(listMasEmployee.get(0).getMasUnit()!=null ) {
							List<MasUnit>listMasUnit=medicalExamDAO.getMasUnitByUnitCode(listMasEmployee.get(0).getMasUnit());
							if(CollectionUtils.isNotEmpty(listMasUnit))
							carefullyUnitName=listMasUnit.get(0).getUnitName();
						}
					}
					jsonMap.put("carefullyUnitName",carefullyUnitName);
					String hospitalName="";
					if(visit.getMasHospital()!=null && StringUtils.isNotEmpty(visit.getMasHospital().getHospitalName())) {
						  hospitalName= visit.getMasHospital().getHospitalName();		
					}
					else {
						hospitalName="";
					}
					
					jsonMap.put("hospitalName",hospitalName);
					
					long employeeIdForIndividualSignature=0;
					if(CollectionUtils.isNotEmpty(listMasEmployee)) {
						if(listMasEmployee.get(0).getEmployeeId()!=0 ) {
								employeeIdForIndividualSignature=listMasEmployee.get(0).getEmployeeId();
						}
					}
					jsonMap.put("employeeIdForIndividualSignature",employeeIdForIndividualSignature);
					
					c.add(jsonMap);
				}
				if (c != null && c.size() > 0) {
					json.put("data", c);
					json.put("listMasServiceType", listMasServiceType);
					json.put("status", 1);
					

				} else {
					json.put("data", c);
					json.put("listMasServiceType", listMasServiceType);
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			} else {
				try {
					json.put("msg", "Visit ID data not found");
					json.put("status", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();

	}

	@Override
	public String getMedicalExamListCommon(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		//List<Visit> listVisit = null;
		List<MasMedicalExamReport>listVisit=null;
		Map<String, Object> map = null;
		String showViewAndReport="";
		int count = 0;
		if (!jsondata.isEmpty()) {
			if( jsondata.containsKey("approvalFlag") && jsondata.get("approvalFlag")!=null  && jsondata.get("approvalFlag").toString().equalsIgnoreCase("y")) {
				map = medicalExamDAO.getApprovalData(jsondata);
				  count = (int) map.get("count");
			}
			else {
			map = medicalExamDAO.getAllCompleteMedicalExam(jsondata);
			  count = (int) map.get("count");
			}
			listVisit = (List<MasMedicalExamReport>) map.get("listVisit");
			String codeME = HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_ME");
			String codeMB = HMSUtil.getProperties("adt.properties", "APPOINTMENT_TYPE_CODE_MB");
			
			if (CollectionUtils.isNotEmpty(listVisit)) {
				//for (MasMedicalExamReport masMedicalExamReport : listVisit) {
				for (Iterator<?> it = listVisit.iterator(); it.hasNext();) {
					String meMbFlag=jsondata.get("mestatus").toString();
					Object[] row = (Object[]) it.next();
					LocalDate today = LocalDate.now();
					HashMap<String, Object> jsonMap = new HashMap<String, Object>();
					//if(masMedicalExamReport!=null && masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getPatient()!=null)
					if(row[0]!=null)
					jsonMap.put("patientName", row[0].toString());

					//if ( masMedicalExamReport.getVisit()!=null && 
						//	   masMedicalExamReport.getVisit().getPatient() != null &&  masMedicalExamReport.getVisit().getPatient().getDateOfBirth() != null) {
					if(row[1]!=null) {
					Date s =  HMSUtil.convertStringDateToUtilDate(row[1].toString(), "yyyy-MM-dd");
						Calendar lCal = Calendar.getInstance();
						lCal.setTime(s);
						int yr = lCal.get(Calendar.YEAR);
						int mn = lCal.get(Calendar.MONTH) + 1;
						int dt = lCal.get(Calendar.DATE);

						LocalDate birthday = LocalDate.of(yr, mn, dt); // Birth date
						System.out.println("birthday" + birthday);
						Period p = Period.between(birthday, today);
						jsonMap.put("age", p.getYears());
					} else {
						jsonMap.put("age", "");
					}
					//if(masMedicalExamReport.getVisit()!=null &&  masMedicalExamReport.getVisit().getPatient()!=null && masMedicalExamReport.getVisit()
						//	.getPatient().getMasAdministrativeSex()!=null && masMedicalExamReport.getVisit().getPatient().getMasAdministrativeSex().getAdministrativeSexName()!=null)	
					if(row[2]!=null)
					jsonMap.put("gender",  row[2].toString());
					
					//if ( masMedicalExamReport.getVisit().getMasMedicalExamReport() != null &&  masMedicalExamReport.getVisit().getMasMedicalExamReport().getMasRank() != null) {
					if(row[4]!=null) {	
					jsonMap.put("rankName",  row[4].toString());
					} else {
						if(row[3]!=null)
						jsonMap.put("rankName", row[3].toString());
					}
					//if(masMedicalExamReport.getVisit()!=null &&  masMedicalExamReport.getVisit().getMasMedExam()!=null &&  masMedicalExamReport.getVisit().getMasMedExam().getMedicalExamName()!=null)
					if(row[5]!=null)
					jsonMap.put("meTypeName",  row[5].toString());
					//if(masMedicalExamReport.getVisit()!=null &&  masMedicalExamReport.getVisit().getMasMedExam()!=null &&  masMedicalExamReport.getVisit().getMasMedExam().getMedicalExamCode()!=null)
					if(row[6]!=null)
					jsonMap.put("meTypeCode",  row[6].toString());
					String visitstatus = "";

					 
					//	MasMedicalExamReport masMedicalExamReport1 = null;
						//masMedicalExamReport = visit.getMasMedicalExamReport();
						//if (masMedicalExamReport == null) {
						if(row[7]==null) {			
							visitstatus = "New";
						}

						//if (masMedicalExamReport != null && masMedicalExamReport.getStatus()!=null) {
						if(row[7]!=null) {
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("s")) {
							if(row[7].toString().equalsIgnoreCase("s")) {	
							visitstatus = "Saved";
							}

							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("af")
								//	|| masMedicalExamReport.getStatus().equalsIgnoreCase("f")) {
							if(row[7].toString().equalsIgnoreCase("af") || row[7].toString().equalsIgnoreCase("f")){		
							visitstatus = "Forwarded";
							}
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("rj")) {
							if(row[7].toString().equalsIgnoreCase("rj")) {			
							visitstatus = "Rejected";
							}
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("pe")) {
							if(row[7].toString().equalsIgnoreCase("pe")) {		
								visitstatus = "Pending";
							}
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("ac")) {
							if(row[7].toString().equalsIgnoreCase("ac")) {
								visitstatus = "Approved";
								showViewAndReport="yes";
							}
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("rf")) {
							if(row[7].toString().equalsIgnoreCase("rf")) {
								visitstatus = "Referred";
							}
							
						//	if (masMedicalExamReport.getStatus().equalsIgnoreCase("es")) {
							if(row[7].toString().equalsIgnoreCase("es")) {
								visitstatus = "Submit";
							}
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("et")) {
							if(row[7].toString().equalsIgnoreCase("et")) {
								visitstatus = "Saved";
							}
							
							
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("ev")) {
							if(row[7].toString().equalsIgnoreCase("ev")) {
								visitstatus="Verified";
							}
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("vr")) {
							if(row[7].toString().equalsIgnoreCase("vr")) {
								visitstatus="Rejected";
							}
							
							
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("ea")
							//		 ) {
							if(row[7].toString().equalsIgnoreCase("ea")) {
								visitstatus="Approved";
								showViewAndReport="yes";
							}
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("ar")) {
							if(row[7].toString().equalsIgnoreCase("ar")) {
								visitstatus="Rejected";
							}
							
							//if (masMedicalExamReport.getStatus().equalsIgnoreCase("po")) {
							if(row[7].toString().equalsIgnoreCase("po")) {
								visitstatus = "New";
							}
						}
						else {
							visitstatus = "New";
						}
						
					jsonMap.put("status", visitstatus);
					if(row[8]!=null)
					jsonMap.put("visitId", row[8].toString());
					else {
						jsonMap.put("visitId","");
					}
					if(row[9]!=null)
					jsonMap.put("patientId", row[9].toString());
					else {
						jsonMap.put("patientId","");
					}
					if(row[10]!=null)
					jsonMap.put("serviceNo", row[10].toString());
					else {
						jsonMap.put("serviceNo","");
					}
					if(row[11]!=null)
					jsonMap.put("departmentId",   row[11].toString());
					else {
						jsonMap.put("departmentId","");
					}
					jsonMap.put("showViewAndReport", showViewAndReport);
					if ( masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getOpdPatientDetails() != null
							&&  masMedicalExamReport.getVisit().getOpdPatientDetails().getOpdPatientDetailsId() != null) {
						jsonMap.put("opdPatientDetailId",  masMedicalExamReport.getVisit().getOpdPatientDetails().getOpdPatientDetailsId());
					} else {
						jsonMap.put("opdPatientDetailId", "");
					}
					Long medicalExamId=null;
				//if( masMedicalExamReport!=null) {
					
					if(row[19]!=null &&  codeME.equalsIgnoreCase(row[19].toString())) {
					if(row[12]!=null) {
						medicalExamId	= Long.parseLong(row[12].toString());
					}
					}
					else {
						if(row[8]!=null)
							medicalExamId	= Long.parseLong(row[8].toString());
					}
					jsonMap.put("medicalExamId",  medicalExamId);
					//jsonMap.put("tokenNo",  masMedicalExamReport.getVisit().getTokenNo());
					String medicalCategory="";

					if (row[9]!=null &&  row[23]!=null) {
						medicalCategory=
								row[23].toString();
					}
					
								
				if(codeME.equalsIgnoreCase(meMbFlag)) {
									//if(masMedicalExamReport.getVisit().getPatient()!=null && masMedicalExamReport.getVisit().getPatient().getMedicalCategoryId() == null) {
					if(row[9]!=null && row[22] == null) {						
					List<PatientMedicalCat>listPatientMedicalCat=medicalExamDAO.getPatientMedicalCat(Long.parseLong(row[9].toString()),Long.parseLong(row[8].toString()),"visit");
										if(CollectionUtils.isNotEmpty(listPatientMedicalCat))
										for(PatientMedicalCat pmc:listPatientMedicalCat) {
											if(StringUtils.isEmpty(medicalCategory))
											medicalCategory=pmc.getMasMedicalCategory().getMedicalCategoryName();
											else {
													medicalCategory+=","+pmc.getMasMedicalCategory().getMedicalCategoryName();
												}
											}
										}
									
									}
									
									if(codeMB.equalsIgnoreCase(meMbFlag)) {
										if(row[9]!=null && row[22] == null) {
											List<PatientMedicalCat>listPatientMedicalCat=medicalExamDAO.getPatientMedicalCat(Long.parseLong(row[9].toString()),Long.parseLong(row[8].toString()),"mbStatusCom");
											if(CollectionUtils.isNotEmpty(listPatientMedicalCat))
											for(PatientMedicalCat pmc:listPatientMedicalCat) {
												if(StringUtils.isEmpty(medicalCategory) && pmc.getMasMedicalCategory()!=null && pmc.getMasMedicalCategory().getMedicalCategoryName()!="")
												medicalCategory=pmc.getMasMedicalCategory().getMedicalCategoryName();
												else if(pmc.getMasMedicalCategory()!=null && pmc.getMasMedicalCategory().getMedicalCategoryName()!=""){
														medicalCategory+=","+pmc.getMasMedicalCategory().getMedicalCategoryName();
													}
												}
											}
										
										}
					
					
						jsonMap.put("medicalCategory",medicalCategory);
					 
					
					 
					String medicalExamDate = "";
					//if ( masMedicalExamReport.getMediceExamDate()!=null ) {
					if(row[13]!=null) {	
						Date ddds =  HMSUtil.convertStringDateToUtilDate(row[13].toString(), "yyyy-MM-dd");
						medicalExamDate = HMSUtil.convertDateToStringFormat(
							ddds, "dd/MM/yyyy");
						jsonMap.put("medicalExamDate", medicalExamDate);
					} else {
						if(row[18]!=null) {	
						Date ddds =  HMSUtil.convertStringDateToUtilDate(row[18].toString(), "yyyy-MM-dd");
						medicalExamDate = HMSUtil.convertDateToStringFormat(
								ddds, "dd/MM/yyyy");
							jsonMap.put("medicalExamDate", medicalExamDate);
						}
						else
						jsonMap.put("medicalExamDate", "");
					}
					----------------------------Start------Age comparision----------------------------------------------------------	
					
					----------------------------end------Age comparision----------------------------------------------------------
					
					if( masMedicalExamReport.getVisit().getPatient()!=null &&  masMedicalExamReport.getVisit().getPatient().getFitFlag() !=null )
					{
						jsonMap.put("fitFlag", masMedicalExamReport.getVisit().getPatient().getFitFlag());
					}
					else {
						jsonMap.put("fitFlag","");
					}
					
					//if(masMedicalExamReport.getRidcId()!=null)
					if(row[14]!=null)
					{
						jsonMap.put("ridcValue", row[14].toString());
					}
					else {
						if(row[25]!=null) {
							jsonMap.put("ridcValue", row[25].toString());
						}
						else {
						jsonMap.put("ridcValue","");
						}
					}
					
					String meAgeNew="";
					//if(masMedicalExamReport!=null  && masMedicalExamReport.getVisit()!=null && masMedicalExamReport.getVisit().getPatient()!=null && masMedicalExamReport.getVisit().getPatient().getDateOfBirth()!=null) {
				if(row[17]!=null) {
					Date s =  HMSUtil.convertStringDateToUtilDate(row[17].toString(), "yyyy-MM-dd");
					//Date s =masMedicalExamReport.getVisit().getPatient().getDateOfBirth();
						meAgeNew=HMSUtil.calculateAge(s);
					}
					else {
						meAgeNew="";
					}
					jsonMap.put("meAgeNew",meAgeNew);
					responseList.add(jsonMap);
				
					
					String dateAgeAtTimeOfMe="";
					
					if(row[19]!=null &&  codeME.equalsIgnoreCase(row[19].toString())) {
						//if(row[20]!=null)
						//dateAgeAtTimeOfMe=getAgeAtTimeOfMe(row[20].toString());
						
						if(row[15]!=null && row[15].toString().trim().equalsIgnoreCase("R") && row[21]!=null) {
							dateAgeAtTimeOfMe=row[21].toString().trim()+" years";
						}
						else {
						Date medicalExamDateNew = null;
						if(row[13]!=null) {
						  medicalExamDateNew =  HMSUtil.convertStringDateToUtilDate(row[13].toString(), "yyyy-MM-dd");
						}
						if(medicalExamDateNew==null && row[18]!=null) {
							medicalExamDateNew =  HMSUtil.convertStringDateToUtilDate(row[18].toString(), "yyyy-MM-dd");
						}
						   
						Date dateOfBirth =null;
						if(row[1]!=null)	
						  dateOfBirth =  HMSUtil.convertStringDateToUtilDate(row[1].toString(), "yyyy-MM-dd");
						
						if(medicalExamDateNew!=null && dateOfBirth!=null) {
							dateAgeAtTimeOfMe=HMSUtil.getDateBetweenTwoDate(medicalExamDateNew,dateOfBirth);
						}
						}
						
					}
					else{
						Date medicalExamDateNew = null;
						if(row[13]!=null) {
						  medicalExamDateNew =  HMSUtil.convertStringDateToUtilDate(row[13].toString(), "yyyy-MM-dd");
						}
						   
						Date dateOfBirth =null;
						if(row[1]!=null)	
						  dateOfBirth =  HMSUtil.convertStringDateToUtilDate(row[1].toString(), "yyyy-MM-dd");
						
						if(medicalExamDateNew!=null && dateOfBirth!=null) {
							dateAgeAtTimeOfMe=HMSUtil.getDateBetweenTwoDate(medicalExamDateNew,dateOfBirth);
						}
					}
					
					//jsonMap.put("dateAgeAtTimeOfMe", dateAgeAtTimeOfMe);
						jsonMap.put("meAgeNew",dateAgeAtTimeOfMe);
						
						responseList.add(jsonMap);
					
				
					if(row[15]!=null)
					{
						jsonMap.put("visitFlag", row[15].toString());
					}
					else {
						jsonMap.put("visitFlag","");
					}
					if(row[16]!=null)
					{
						jsonMap.put("approvedBy", row[16].toString());
					}
					else {
						jsonMap.put("approvedBy","");
					}
				
					
					if(row[24]!=null)
					{
						jsonMap.put("meApprovalRidcId", row[24].toString());
					}
					else {
						jsonMap.put("meApprovalRidcId","");
					}
					
				}
				
				
				
				
				if (responseList != null && responseList.size() > 0) {
					json.put("data", responseList);
					json.put("status", 1);
					json.put("count", count);

				} else {
					json.put("data", responseList);
					json.put("msg", "Data not found");
					json.put("status", 0);
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
		return json.toString();
	
	}

	public static String getAgeAtTimeOfMe(String age) {
		String [] ageSplitVal=age.split(" ");
		String ageNew="";
		if(ageSplitVal!=null) {
			ageNew=ageSplitVal[0] +" "+"years";
		}
		return ageNew;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getMEMBHistory(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List list = new ArrayList();
		 Map<String, Object> map = medicalExamDAO.getMEMBHistory(jsondata);
		 if(map.get("medicalExamReportsList")!=null) {
			 List<Visit> visitList = (List<Visit>) map.get("medicalExamReportsList");
			 int count = (int) map.get("count");
			 if(CollectionUtils.isNotEmpty(visitList) && visitList.size()>0) {
				 
				visitList.forEach(visitdt -> {
				Map<String, Object> mapobj = new HashMap<String, Object>();
					 
				if(visitdt.getPatient()!=null && visitdt.getPatient().getPatientName()!=null) {
					mapobj.put("patientName", visitdt.getPatient().getPatientName());	
				}else {
					mapobj.put("patientName","");
				}
				
				if(visitdt.getPatient()!=null && visitdt.getPatient().getMasAdministrativeSex()!=null && visitdt.getPatient().getMasAdministrativeSex().getAdministrativeSexName()!=null) {
					mapobj.put("gender", visitdt.getPatient().getMasAdministrativeSex().getAdministrativeSexName());	
				}else {
					mapobj.put("gender","");
				}
				if(visitdt.getPatient()!=null && visitdt.getPatient().getMasRank()!=null && visitdt.getPatient().getMasRank().getRankName()!=null) {
					mapobj.put("rankName", visitdt.getPatient().getMasRank().getRankName());	
				}else {
					mapobj.put("rankName","");
				}
				if(visitdt.getMasAppointmentType()!=null && visitdt.getMasAppointmentType().getAppointmentTypeName()!=null) {
					mapobj.put("meTypeName", visitdt.getMasAppointmentType().getAppointmentTypeName());	
				}else {
					mapobj.put("meTypeName","");
				}
				if(visitdt.getMasAppointmentType()!=null && visitdt.getMasAppointmentType().getAppointmentTypeCode()!=null) {
					mapobj.put("meTypeCode", visitdt.getMasAppointmentType().getAppointmentTypeCode());	
				}else {
					mapobj.put("meTypeCode","");
				}
				if(visitdt.getExamStatus()!=null) {
					mapobj.put("status", visitdt.getExamStatus());	
				}else {
					mapobj.put("status","");
				}
				if(visitdt.getVisitId()!=null) {
					mapobj.put("visitId", visitdt.getVisitId());	
				}else {
					mapobj.put("visitId","");
				}
				if(visitdt.getPatient()!=null && visitdt.getPatient().getPatientId()!=null) {
					mapobj.put("patientId", visitdt.getPatient().getPatientId());	
				}else {
					mapobj.put("patientId","");
				}
				
				if(visitdt.getPatient()!=null && visitdt.getPatient().getServiceNo()!=null) {
					mapobj.put("serviceNo", visitdt.getPatient().getServiceNo());	
				}else {
					mapobj.put("serviceNo","");
				}
				
				if(visitdt.getTokenNo()!=null) {
					mapobj.put("tokenNo", visitdt.getTokenNo());	
				}else {
					mapobj.put("tokenNo","");
				}
				
				if(visitdt.getMasMedExam()!=null && visitdt.getMasMedExam().getMedicalExamName()!=null) {
					mapobj.put("medicalCategory", visitdt.getMasMedExam().getMedicalExamName());	
				}else {
					mapobj.put("medicalCategory","");
				}
				
				if(visitdt.getMasMedicalExamReport()!=null && visitdt.getMasMedicalExamReport().getMediceExamDate()!=null) {
					mapobj.put("medicalExamDate", HMSUtil.convertDateToStringFormat(visitdt.getMasMedicalExamReport().getMediceExamDate(), "dd/MM/yyyy"));	
				}else {
					mapobj.put("medicalExamDate","");
				}
				
				if(visitdt.getPatient()!=null && visitdt.getPatient().getDateOfBirth()!=null) {
					mapobj.put("age", JavaUtils.calculateAgefromDob(visitdt.getPatient().getDateOfBirth()));	
				}else {
					mapobj.put("age","");
				}
				
				 list.add(mapobj);
				 
				 });
				 json.put("msg", "Record Fetch Successfully");
				 json.put("data", list);
				 json.put("count", count);
				 json.put("status", "success");
			 }else {
				 json.put("msg", "No Record Found");
				 json.put("data", list);
				 json.put("count", 0);
				 
		
			 }
			 
		 }
		 
		return json.toString();
	}

	
	public static MasMedicalExamReport getLabValue(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt){

String remarksOfLab = payload.get("remarksOfLab").toString();
				remarksOfLab = OpdServiceImpl.getReplaceString(remarksOfLab);
				if(StringUtils.isNotEmpty(remarksOfLab)) {
					masMedicalExamReprt.setRemarksOfLab(remarksOfLab); 
				}
				
				String dateOfLab = payload.get("dateOfLab").toString();
				dateOfLab = OpdServiceImpl.getReplaceString(dateOfLab);
				if(StringUtils.isNotEmpty(dateOfLab) && MedicalExamServiceImpl.checkDateFormat(dateOfLab)) {
					Date dateOfLabValue = HMSUtil.convertStringTypeDateToDateType(dateOfLab.trim());
					if(dateOfLabValue!=null)
					masMedicalExamReprt.setDateOfLab(dateOfLabValue) ; 
				}
				String signatureOfEyeSpecialist = payload.get("signatureOfEyeSpecialist").toString();
				signatureOfEyeSpecialist = OpdServiceImpl.getReplaceString(signatureOfEyeSpecialist);
				if(StringUtils.isNotEmpty(signatureOfEyeSpecialist)) {
					masMedicalExamReprt.setSignatureOfEyeSpecialist(signatureOfEyeSpecialist);
				}
				return masMedicalExamReprt;
}

	
	
	public static MasMedicalExamReport getSurgeryValue(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt){

				String remarksOfSurgery = payload.get("remarksOfSurgery").toString();
				remarksOfSurgery = OpdServiceImpl.getReplaceString(remarksOfSurgery);
				if(StringUtils.isNotEmpty(remarksOfSurgery)) {
					masMedicalExamReprt.setRemarksOfSurgery(remarksOfSurgery);
				}
				
				String dateOfSurgery = payload.get("dateOfSurgery").toString();
				dateOfSurgery = OpdServiceImpl.getReplaceString(dateOfSurgery);
				if(StringUtils.isNotEmpty(dateOfSurgery) && MedicalExamServiceImpl.checkDateFormat(dateOfSurgery)) {
					Date dateOfSurgeryValue = HMSUtil.convertStringTypeDateToDateType(dateOfSurgery.trim());
					if(dateOfSurgeryValue!=null)
					masMedicalExamReprt.setDateOfSurgery(dateOfSurgeryValue);
				}
				String signatureOfSurgicalSpecialist = payload.get("signatureOfSurgicalSpecialist").toString();
				signatureOfSurgicalSpecialist = OpdServiceImpl.getReplaceString(signatureOfSurgicalSpecialist);
				if(StringUtils.isNotEmpty(signatureOfSurgicalSpecialist)) {
					masMedicalExamReprt.setSignatureOfSurgicalSpecialist(signatureOfSurgicalSpecialist);
				}
				return masMedicalExamReprt;
}
	 
	

public static MasMedicalExamReport getVisionSomeValue(HashMap<String, Object> payload,
			MasMedicalExamReport masMedicalExamReprt){

String binocularVisionGrade = payload.get("binocularVisionGrade").toString();
				binocularVisionGrade = OpdServiceImpl.getReplaceString(binocularVisionGrade);
				if(StringUtils.isNotEmpty(binocularVisionGrade)) {
					masMedicalExamReprt.setBinocularVisionGrade(binocularVisionGrade);
				}
				String evidenceOfTrachoma = payload.get("evidenceOfTrachoma").toString();
				evidenceOfTrachoma = OpdServiceImpl.getReplaceString(evidenceOfTrachoma);
				if(StringUtils.isNotEmpty(evidenceOfTrachoma)) {
					masMedicalExamReprt.setEvidenceOfTrachoma(evidenceOfTrachoma);
				}
				return masMedicalExamReprt;
}


public static MasMedicalExamReport getGynaecologySomeValue(HashMap<String, Object> payload,
		MasMedicalExamReport masMedicalExamReprt){

String remarksOfGynaecology = payload.get("remarksOfGynaecology").toString();
			remarksOfGynaecology = OpdServiceImpl.getReplaceString(remarksOfGynaecology);
			if(StringUtils.isNotEmpty(remarksOfGynaecology)) {
				//masMedicalExamReprt.setServiceTypeId(Long.parseLong(serviceOfEmployee)); 
				masMedicalExamReprt.setRemarksOfGynaecology(remarksOfGynaecology);
			}
			
			String dateOfGynaecology = payload.get("dateOfGynaecology").toString();
			dateOfGynaecology = OpdServiceImpl.getReplaceString(dateOfGynaecology);
			if(StringUtils.isNotEmpty(dateOfGynaecology) && MedicalExamServiceImpl.checkDateFormat(dateOfGynaecology)) {
				Date dateOfGynaecologyValue = HMSUtil.convertStringTypeDateToDateType(dateOfGynaecology.trim());
				if(dateOfGynaecologyValue!=null)
				masMedicalExamReprt.setDateOfGynaecology(dateOfGynaecologyValue);
			}
			String signatureOfGynaecologist = payload.get("signatureOfGynaecologist").toString();
			signatureOfGynaecologist = OpdServiceImpl.getReplaceString(signatureOfGynaecologist);
			if(StringUtils.isNotEmpty(signatureOfGynaecologist)) {
				masMedicalExamReprt.setSignatureOfGynaecologist(signatureOfGynaecologist);
			}
			return masMedicalExamReprt;
}


public static MasMedicalExamReport getCommonInfoValue(HashMap<String, Object> payload,
		MasMedicalExamReport masMedicalExamReprt){

if(payload.containsKey("memberPlace")) {
			String memberPlace = payload.get("memberPlace").toString();
			memberPlace = OpdServiceImpl.getReplaceString(memberPlace);
			if (StringUtils.isNotEmpty(memberPlace) && !memberPlace.equalsIgnoreCase("")) {
				masMedicalExamReprt.setMemberPlace(memberPlace);
			}
		}
		
		if(payload.containsKey("memberDate")) {
			  String memberDate = payload.get("memberDate").toString();
			  memberDate = OpdServiceImpl.getReplaceString(memberDate);
			
			if (StringUtils.isNotEmpty(memberDate) && !memberDate.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(memberDate)) {
				Date memberDateVal = HMSUtil.convertStringTypeDateToDateType(memberDate.trim());
				if(memberDateVal!=null)
				masMedicalExamReprt.setMemberDate(memberDateVal);
			}
			}

		if(payload.containsKey("rankDesiMember1")) {
			String rankDesiMember1 = payload.get("rankDesiMember1").toString();
			rankDesiMember1 = OpdServiceImpl.getReplaceString(rankDesiMember1);
			if (StringUtils.isNotEmpty(rankDesiMember1) && !rankDesiMember1.equalsIgnoreCase("")) {
				masMedicalExamReprt.setRankDesiMember1(rankDesiMember1);
			}
		}
		
		
		if(payload.containsKey("nameMember1")) {
			String nameMember1 = payload.get("nameMember1").toString();
			nameMember1 = OpdServiceImpl.getReplaceString(nameMember1);
			if (StringUtils.isNotEmpty(nameMember1) && !nameMember1.equalsIgnoreCase("")) {
				masMedicalExamReprt.setNameMember1(nameMember1);
			}
		}
		
		if(payload.containsKey("rankDesiMember2")) {
			String rankDesiMember2 = payload.get("rankDesiMember2").toString();
			rankDesiMember2 = OpdServiceImpl.getReplaceString(rankDesiMember2);
			if (StringUtils.isNotEmpty(rankDesiMember2) && !rankDesiMember2.equalsIgnoreCase("")) {
				masMedicalExamReprt.setRankDesiMember2(rankDesiMember2);
			}
		}
		
		
		if(payload.containsKey("nameMember2")) {
			String nameMember2 = payload.get("nameMember2").toString();
			nameMember2 = OpdServiceImpl.getReplaceString(nameMember2);
			if (StringUtils.isNotEmpty(nameMember2) && !nameMember2.equalsIgnoreCase("")) {
				masMedicalExamReprt.setNameMember2(nameMember2);
			}
		}			
		
		
		if(payload.containsKey("rankDesiPresident")) {
			String rankDesiPresident = payload.get("rankDesiPresident").toString();
			rankDesiPresident = OpdServiceImpl.getReplaceString(rankDesiPresident);
			if (StringUtils.isNotEmpty(rankDesiPresident) && !rankDesiPresident.equalsIgnoreCase("")) {
				masMedicalExamReprt.setRankDesiPresident(rankDesiPresident);
			}
		}
		
		
		if(payload.containsKey("namePresident")) {
			String namePresident = payload.get("namePresident").toString();
			namePresident = OpdServiceImpl.getReplaceString(namePresident);
			if (StringUtils.isNotEmpty(namePresident) && !namePresident.equalsIgnoreCase("")) {
				masMedicalExamReprt.setBoardpresident(namePresident);
			}
		}			
		
			return masMedicalExamReprt;
}



public static MasMedicalExamReport getCommonInfoValue2(HashMap<String, Object> payload,
		MasMedicalExamReport masMedicalExamReprt){

if(payload.containsKey("memberPlaceSub")) {
			String memberPlace = payload.get("memberPlaceSub").toString();
			memberPlace = OpdServiceImpl.getReplaceString(memberPlace);
			if (StringUtils.isNotEmpty(memberPlace) && !memberPlace.equalsIgnoreCase("")) {
				masMedicalExamReprt.setMemberPlaceSub(memberPlace); 
			}
		}
		
		if(payload.containsKey("memberDateSub")) {
			  String memberDate = payload.get("memberDateSub").toString();
			  memberDate = OpdServiceImpl.getReplaceString(memberDate);
			
			if (StringUtils.isNotEmpty(memberDate) && !memberDate.equalsIgnoreCase("") && MedicalExamServiceImpl.checkDateFormat(memberDate)) {
				Date memberDateVal = HMSUtil.convertStringTypeDateToDateType(memberDate.trim());
				if(memberDateVal!=null)
				masMedicalExamReprt.setMemberDateSub(memberDateVal);
			}
			}

		if(payload.containsKey("rankDesiMember1Sub")) {
			String rankDesiMember1 = payload.get("rankDesiMember1Sub").toString();
			rankDesiMember1 = OpdServiceImpl.getReplaceString(rankDesiMember1);
			if (StringUtils.isNotEmpty(rankDesiMember1) && !rankDesiMember1.equalsIgnoreCase("")) {
				masMedicalExamReprt.setRankDesiMember1Sub(rankDesiMember1);
			}
		}
		
		
		if(payload.containsKey("nameMember1Sub")) {
			String nameMember1 = payload.get("nameMember1Sub").toString();
			nameMember1 = OpdServiceImpl.getReplaceString(nameMember1);
			if (StringUtils.isNotEmpty(nameMember1) && !nameMember1.equalsIgnoreCase("")) {
				masMedicalExamReprt.setNameMember1Sub(nameMember1);
			}
		}
		
		if(payload.containsKey("rankDesiMember2Sub")) {
			String rankDesiMember2 = payload.get("rankDesiMember2Sub").toString();
			rankDesiMember2 = OpdServiceImpl.getReplaceString(rankDesiMember2);
			if (StringUtils.isNotEmpty(rankDesiMember2) && !rankDesiMember2.equalsIgnoreCase("")) {
				masMedicalExamReprt.setRankDesiMember2Sub(rankDesiMember2);
			}
		}
		
		
		if(payload.containsKey("nameMember2Sub")) {
			String nameMember2 = payload.get("nameMember2Sub").toString();
			nameMember2 = OpdServiceImpl.getReplaceString(nameMember2);
			if (StringUtils.isNotEmpty(nameMember2) && !nameMember2.equalsIgnoreCase("")) {
				masMedicalExamReprt.setNameMember2Sub(nameMember2);
			}
		}			
		
		
		if(payload.containsKey("rankDesiPresidentSub")) {
			String rankDesiPresident = payload.get("rankDesiPresidentSub").toString();
			rankDesiPresident = OpdServiceImpl.getReplaceString(rankDesiPresident);
			if (StringUtils.isNotEmpty(rankDesiPresident) && !rankDesiPresident.equalsIgnoreCase("")) {
				masMedicalExamReprt.setRankDesiPresidentSub(rankDesiPresident);
			}
		}
		
		
		if(payload.containsKey("namePresidentSub")) {
			String namePresident = payload.get("namePresidentSub").toString();
			namePresident = OpdServiceImpl.getReplaceString(namePresident);
			if (StringUtils.isNotEmpty(namePresident) && !namePresident.equalsIgnoreCase("")) {
				masMedicalExamReprt.setNamePresidentSub(namePresident);
			}
		}			
		
			return masMedicalExamReprt;
}


public static Boolean checkDateFormat(String dateOfExam) {
	if(StringUtils.isNotEmpty(dateOfExam)) {
	String[] dataOfYear1=dateOfExam.split("/");
	if(dataOfYear1!=null && dataOfYear1.length>=3) 
	{
	 if(dataOfYear1[2].trim().length()!=4) {
	 return false;
	 }
	 else {
		return true;
	 }
	 }
	else
		return false;
	}
	return false;
}

@Override
@Transactional
public String submitApprovalDate(HashMap<String, Object> payload, HttpServletRequest request,
		HttpServletResponse response) {
	JSONObject json = new JSONObject();
	try {
		String medicalExamId=payload.get("medicalExamId").toString();
		medicalExamId=OpdServiceImpl.getReplaceString(medicalExamId);
		
		String meApprovedRmsId=payload.get("meApprovedRmsId").toString();
		meApprovedRmsId=OpdServiceImpl.getReplaceString(meApprovedRmsId);
		MasMedicalExamReport masMedicalExamReport=null;	
		if(StringUtils.isNotEmpty(medicalExamId)) {
		  masMedicalExamReport=medicalExamDAO.getMasMedicalExamReprtByMedicalId(Long.parseLong(medicalExamId));
		}
		if(masMedicalExamReport!=null && meApprovedRmsId!=null) {
			masMedicalExamReport.setMeApprovalRidcId(Long.parseLong(meApprovedRmsId));
		}
		Long messageVal=0l;
		if(masMedicalExamReport!=null)
			messageVal=	medicalExamDAO.saveUpdateMedicalExamApprovalData(masMedicalExamReport);
	json.put("message", messageVal+"##");
	}
	catch(Exception e) {
		e.printStackTrace();
		json.put("message", "0"+"##"+e);
	}
	return json.toString();

}

@Override
public String getTemplateInvestDataForDiver(HashMap<String, Object> jsondata, HttpServletRequest request) {
	JSONObject json = new JSONObject();
	
	if (jsondata.get("visitId") == null || jsondata.get("visitId").toString().trim().equals("")) {
		return "{\"status\":\"0\",\"msg\":\"json is not contain Template ID as a  key or value or it is null\"}";
		
	}
			
	else {
		Long visitId=null;
		Long patientId=null;
		if (jsondata.get("visitId") != null ) {
		  visitId=Long.parseLong(jsondata.get("visitId").toString());
		 
		}
		if (jsondata.get("patientId") != null && !jsondata.get("patientId").toString().trim().equalsIgnoreCase("")) {
		 patientId=Long.parseLong(jsondata.get("patientId").toString());
		}
		Map<String,Object> map = medicalExamDAO.getTemplateInvestigationoDiver();
		List<OpdTemplateInvestigation> getInvestigationData = (List<OpdTemplateInvestigation>) map.get("list");
		List<DiverOrderDet>listDiverOrderDet=dgOrderdtDao.getDiverDgOrderHdtByVisitIdForDiver(visitId,patientId);
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		
		try
		{
			String  drivenValueCheck="";
		for (OpdTemplateInvestigation tempInve : getInvestigationData) {
			HashMap<String, Object> ti = new HashMap<String, Object>();
			ti.put("templateName", tempInve.getOpdTemplate().getTemplateCode());
			ti.put("templateCode",tempInve.getOpdTemplate().getTemplateName());
			ti.put("templateInvestgationId",tempInve.getInvestigationId());
			ti.put("templateDataId", tempInve.getTemplateInvestigationId());
			ti.put("investigationName", tempInve.getDgMasInvestigation().getInvestigationName());
			String doneOnDate =null;
			String nextDueOnDate =null;
			String lastDoneOnDate =null;
			String duration=null;
			String dgOrderdtDiver=null;
			String diverAction=null;
			String diverOrderdtId=null;
			if(CollectionUtils.isNotEmpty(listDiverOrderDet)) {
			for (DiverOrderDet dgOrderdt : listDiverOrderDet) {
				if(dgOrderdt.getInvestigationId().toString().equalsIgnoreCase(tempInve.getInvestigationId().toString())) {
					
					
					  doneOnDate = HMSUtil.convertDateToStringFormat(dgOrderdt.getDiverDoneOn(),
							"dd/MM/yyyy");
					
					  nextDueOnDate = HMSUtil.convertDateToStringFormat(dgOrderdt.getDiverNextDueOn(),
							"dd/MM/yyyy");
					
					
					  lastDoneOnDate = HMSUtil.convertDateToStringFormat(dgOrderdt.getDiverLastDoneOn(),
							"dd/MM/yyyy");
					
					duration=dgOrderdt.getDiverDuration();
					 
					
					if(StringUtils.isEmpty(diverOrderdtId)) {
						diverOrderdtId=""+dgOrderdt.getInvestigationId()+"##"+dgOrderdt.getOrderDetId().toString()+",";
					}
					else {
						diverOrderdtId+=""+dgOrderdt.getInvestigationId()+"##"+dgOrderdt.getOrderDetId().toString()+",";
					}
					
					if(visitId!=null && dgOrderdt.getVisitId()!=null && visitId.toString().equalsIgnoreCase(dgOrderdt.getVisitId().toString())) {
						List<DgOrderdt>listDiverOrderId=dgOrderdtDao.getDgOrderdtByDiverId(dgOrderdt.getOrderDetId());
						
					if(CollectionUtils.isNotEmpty(listDiverOrderId)) {
						dgOrderdtDiver=listDiverOrderId.get(0).getOrderdtId().toString();
					}
				if(dgOrderdtDiver!=null) {
					if(StringUtils.isEmpty(drivenValueCheck)) {
						drivenValueCheck=""+dgOrderdt.getInvestigationId()+"##"+dgOrderdtDiver+"##"+dgOrderdt.getOrderDetId()+",";
					}
					else {
						drivenValueCheck+=""+dgOrderdt.getInvestigationId()+"##"+dgOrderdtDiver+"##"+dgOrderdt.getOrderDetId()+",";
					}
				
					//break;
					} 
						else {
								if(StringUtils.isEmpty(drivenValueCheck)) {
									 drivenValueCheck=""+0+"##"+0+"##"+dgOrderdt.getOrderDetId()+","; 
									 } else {
										 drivenValueCheck+=""+0+"##"+0+"##"+dgOrderdt.getOrderDetId()+ ","; 
									  } }
				
				Date date=new Date();
				if(dgOrderdt.getDiverNextDueOn()!=null) {
					 String toDate = HMSUtil.convertDateToStringFormat(
							 date, "dd/MM/yyyy");
					 
					 String nextDueDate = HMSUtil.convertDateToStringFormat(
							 dgOrderdt.getDiverNextDueOn(), "dd/MM/yyyy");
						String [] dateA=toDate.split("/");
						String [] dateB=nextDueDate.split("/");
						
					 if(dateA[2].trim().equalsIgnoreCase(dateB[2].trim())) {
							 diverAction=dgOrderdt.getDiverAction();
						 }
					 	}
									 
					}
				else {
					Date date=new Date();
					if(dgOrderdt.getDiverNextDueOn()!=null) {
						 String toDate = HMSUtil.convertDateToStringFormat(
								 date, "dd/MM/yyyy");
						 
						 String nextDueDate = HMSUtil.convertDateToStringFormat(
								 dgOrderdt.getDiverNextDueOn(), "dd/MM/yyyy");
							String [] dateA=toDate.split("/");
							String [] dateB=nextDueDate.split("/");
							
						 if(dateA[2].trim().equalsIgnoreCase(dateB[2].trim())) {
							 
							 if(StringUtils.isEmpty(drivenValueCheck)) {
									drivenValueCheck=""+dgOrderdt.getInvestigationId()+"##"+""+",";
								}
							 else {
								 drivenValueCheck+=""+dgOrderdt.getInvestigationId()+"##"+""+",";
							 }
							// if(diverOrderdtId!=null)
								 diverAction=dgOrderdt.getDiverAction();
								
							 }
						 }
					 break;
					}
				}
				}
			
			}
			ti.put("diverOrderdtId", diverOrderdtId);
				ti.put("diverAction", diverAction);
				ti.put("duration", duration);
				ti.put("dgOrderdtDiver", dgOrderdtDiver);
				if(doneOnDate==null)
				  doneOnDate = HMSUtil.convertDateToStringFormat(new Date(),
						"dd/MM/yyyy");
				
				ti.put("doneOnDate",doneOnDate);
				ti.put("nextDueOnDate",nextDueOnDate);
				ti.put("lastDoneOnDate",lastDoneOnDate);
				c.add(ti);
			
	}
		if(c != null && c.size()>0){
			json.put("data", c);
			json.put("drivenValueCheck", drivenValueCheck);
			json.put("count", c.size());
			json.put("msg", "Visit List  get  sucessfull... ");
			json.put("status", "1");
		}else{
			return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
		}
		}
		catch(Exception e)
		{
		  System.out.println(e);
		}

		return json.toString();
	}
}

*/
}
