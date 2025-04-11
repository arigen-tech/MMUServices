package com.mmu.services.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.impl.MISDaoImpl;
import com.mmu.services.service.MISService;


@Service
public class MISServiceImpl implements MISService {
	
	@Autowired
	MISDaoImpl misDao;
	
	
	@Override
	public String getDaiDidiClinicData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getDaiDidiClinicData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("daiDidiClinic_data");
		jsonResponse.put("daiDidiClinic_data", result);
		return jsonResponse.toString(); 
	}
	
	@Override
	public String getLabourBeneficiaryData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getLabourBeneficiaryData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("labourBeneficiary_data");
		jsonResponse.put("labourBeneficiary_data", result);
		return jsonResponse.toString(); 
	}
	
	@Override
	public String getMMSSYInfoData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getMMSSYInfoData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("mmssyInfo_data");
		jsonResponse.put("mmssyInfo_data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getDaiDidiClinicData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getDaiDidiClinicData2(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("daiDidiClinic_data");
		jsonResponse.put("daiDidiClinic_data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getLabourBeneficiaryData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getLabourBeneficiaryData2(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("labourBeneficiary_data");
		jsonResponse.put("labourBeneficiary_data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getMMSSYInfoData2(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getMMSSYInfoData2(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("mmssyInfo_data");
		jsonResponse.put("mmssyInfo_data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String aiAuditReport(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.aiAuditReport(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("aiAuditReport_data");
		jsonResponse.put("aiAuditReport_data", result);
		return jsonResponse.toString(); 
	}
	@Override
	public String edlReport(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getdlReportData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("edlReport_data");
		jsonResponse.put("edlReport_data", result);
		return jsonResponse.toString();
	}

	@Override
	public String labReport(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getLabReportData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("labReport_data");
		jsonResponse.put("labReport_data", result);
		return jsonResponse.toString();
	}

	@Override
	public String medicineIssueReport(HashMap<String, Object> jsondata, HttpServletRequest request,
									  HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getmedicineIssueReportData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("medicineIssueReport_data");
		jsonResponse.put("medicineIssueReport_data", result);
		return jsonResponse.toString();
	}

	@Override
	public String edlReportCityWise(HashMap<String, Object> jsondata, HttpServletRequest request,
									HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.edlReportCityWise(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("edlReportCityWise_data");
		jsonResponse.put("edlReportCityWise_data", result);
		return jsonResponse.toString();
	}

	@Override
	public String getAttendanceRegister(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {JSONObject jsonResponse = new JSONObject();
			Map<String,Object> map = (Map<String,Object>)misDao.getAttendanceRegister(jsondata, request, response);
			JSONObject result = (JSONObject)map.get("opdRegsiter_data");
			jsonResponse.put("opdRegsiter_data", result);
			return jsonResponse.toString();
	}

	@Override
	public String getAuditOpdRegister(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getAuditOpdRegister(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("opdRegsiter_data");
		jsonResponse.put("opdRegsiter_data", result);
		return jsonResponse.toString();
	
		/*
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!map.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = misDao.getAuditOpdRegister(map);
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					 List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
					    String mmuName=null;
					    String patientName=null;
					    String dateOfBirth=null;
					    String age=null;
					    String uhidNo=null;
					    String doctorName=null;
					    String gender=null;
					    String visitDate=null;
					    String icdDiagnosis=null;
					    String patientSympton=null;
					    String referralFlag=null;
					    String dispensaryFlag=null;
					    String labFlag=null;
									
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0]!= null) {
									mmuName =row[0].toString();
								}
								if (row[1]!= null) {
									patientName =row[1].toString();
									
								}
								if (row[2]!= null) {
									dateOfBirth =row[2].toString();
									
								}
								if (row[3]!= null) {
									age =row[3].toString();
									
								}
								if (row[4]!= null) {
									uhidNo =row[4].toString();
									
								}
								if (row[5]!= null) {
									doctorName =row[5].toString();
									
								}
								if (row[6]!= null) {
									gender =row[6].toString();
									
								}
								if (row[7]!= null) {
									visitDate =row[7].toString();
									
								}
								if (row[8]!= null) {
									icdDiagnosis =row[8].toString();
									
								}
								if (row[9]!= null) {
									patientSympton =row[9].toString();
									
								}
								if (row[10]!= null) {
									referralFlag =row[10].toString();
									
								}
								if (row[11]!= null) {
									dispensaryFlag =row[11].toString();
									
								}
								if (row[12]!= null) {
									labFlag =row[12].toString();
									
								}
								 
													
								if(mmuName!=null)
								{
								pt.put("mmuName", mmuName);
								}
								else
								{
									pt.put("mmuName", "");	
								}
								if(patientName!=null)
								{
								pt.put("patientName", patientName);
								}
								else
								{
									pt.put("patientName", "");	
								}
								if(dateOfBirth!=null)
								{
								pt.put("dateOfBirth", dateOfBirth);
								}
								else
								{
									pt.put("dateOfBirth","");	
								}
								if(age!=null)
								{
								pt.put("age", age);
								}
								else
								{
									pt.put("age","");	
								}
								if(uhidNo!=null)
								{
								pt.put("uhidNo", uhidNo);
								}
								else
								{
									pt.put("uhidNo","");	
								}
								if(doctorName!=null)
								{
								pt.put("doctorName", doctorName);
								}
								else
								{
									pt.put("doctorName","");	
								}
								if(gender!=null)
								{
								pt.put("gender", gender);
								}
								else
								{
									pt.put("gender","");	
								}
								if(visitDate!=null)
								{
								pt.put("visitDate", visitDate);
								}
								else
								{
									pt.put("visitDate","");	
								}
								if(icdDiagnosis!=null)
								{
								pt.put("icdDiagnosis", icdDiagnosis);
								}
								else
								{
									pt.put("icdDiagnosis","");	
								}
								if(patientSympton!=null)
								{
								pt.put("patientSympton", patientSympton);
								}
								else
								{
									pt.put("patientSympton","");	
								}
								if(referralFlag!=null)
								{
								pt.put("referralFlag", referralFlag);
								}
								else
								{
									pt.put("referralFlag","");	
								}
								if(dispensaryFlag!=null)
								{
								pt.put("dispensaryFlag", dispensaryFlag);
								}
								else
								{
									pt.put("dispensaryFlag","");	
								}
								if(labFlag!=null)
								{
								pt.put("labFlag", labFlag);
								}
								else
								{
									pt.put("labFlag","");	
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
*/}

	@Override
	public String getAttendanceInOutTime(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
			JSONObject jsonResponse = new JSONObject();
			Map<String,Object> map = (Map<String,Object>)misDao.getAttendanceInOutTime(jsondata, request, response);
			JSONObject result = (JSONObject)map.get("opdRegsiter_data");
			jsonResponse.put("opdRegsiter_data", result);
			return jsonResponse.toString();
	}

	@Override
	public String getLabRegister(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)misDao.getLabRegister(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("opdRegsiter_data");
		jsonResponse.put("opdRegsiter_data", result);
		return jsonResponse.toString();
}


}
