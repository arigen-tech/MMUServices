package com.mmu.services.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.SchedulerServiceDao;
import com.mmu.services.entity.EmployeeRegistration;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.service.SchedulerService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;
import com.sun.mail.iap.Response;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

//import com.mmu.services.entity.MasMMU;

@Service("SchedulerService")
public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	SchedulerServiceDao schedulerServiceDao;

	
	@Override
	public String getMMUListForPoll(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		Map<String, Object> map = schedulerServiceDao.getMMUListForPoll(jsondata);
		List<EmployeeRegistration> getList = (List<EmployeeRegistration>) map.get("list");
		try {

			Map<String, Object> list = schedulerServiceDao.getMMUListForPoll(jsondata);
			List<MasMMU> mmuList = (List<MasMMU>) list.get("mmu");
			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			if (!mmuList.isEmpty()) {
				for (MasMMU mmu : mmuList) {
					HashMap<String, Object> mmuRec = new HashMap<String, Object>();
					mmuRec.put("mmuId", mmu.getMmuId());
					mmuRec.put("pollNo", mmu.getPollNo());
					mmuRec.put("chassisNo", mmu.getChassisNo());
					c.add(mmuRec);
				}
			} 
			json.put("data", c);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();

	}

	@Override
	public Map<String, Object> savePollInfo(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = schedulerServiceDao.savePollInfo(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public String getCampDetailsForMMU(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		try {

			Map<String, Object> map = schedulerServiceDao.getCampDetailsForMMU(jsondata);
			List<MasCamp> campList = (List<MasCamp>) map.get("mmu");
			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			if (!campList.isEmpty()) {
				System.out.println("campList size:- "+campList.size());
				for (MasCamp camp : campList) {
					HashMap<String, Object> campRec = new HashMap<String, Object>();
					campRec.put("mmuId", camp.getMmuId());
					campRec.put("campLong", camp.getLongitude());
					campRec.put("campLat", camp.getLattitude());
					campRec.put("campId", camp.getCampId());
					campRec.put("chassisNo", camp.getMasMMU().getChassisNo());
					c.add(campRec);
				}
			} 
			json.put("data", c);
			//System.out.println("json.toString() for camp--> "+json.toString());
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public Map<String, Object> saveVehicleLocation(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		try {
			map = schedulerServiceDao.saveVehicleLocation(requestData);

			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	
	}

	@Override
	public String sendFollowupMsgScheduler(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {
			//List<Object[]> getVisitRecord = null;  
			JSONObject nullbalankvalidation = null;
			
				//getVisitRecord= od.getPreviousVisitRecord(Long.parseLong(jsondata.get("patientId").toString(),jsondata));
				Map<String, Object> getDiagnosisData = schedulerServiceDao.getFollowupMsgData();
				if (getDiagnosisData.isEmpty()) {
					json.put("count", 0);
					json.put("data", new JSONArray());
				} else {
					 List<Object[]> list = (List<Object[]>) getDiagnosisData.get("list");
					    String count = String.valueOf(getDiagnosisData.get("count"));
						
					    String patientName=null;
					    String followUpDate=null;
					    String mobileNumber=null;
					    String followUpLocation=null;
					    String followUpLandmarks=null;
					    String patientId=null;
					    String opdPatientDetailsId=null;
									
						try {
							for (Object[] row : list) {
							
							//for (Iterator<?> it = getVisitRecord.iterator(); it.hasNext();) {
							//	Object[] row = (Object[]) it.next();

								HashMap<String, Object> pt = new HashMap<String, Object>();

								
								if (row[0]!= null) {
									followUpDate =row[0].toString();
									Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(followUpDate);  
									followUpDate = HMSUtil.convertDateToStringFormat(date1, "dd/MM/yyyy");
								}
								if (row[1]!= null) {
									mobileNumber =row[1].toString();
									
								}
								if (row[2]!= null) {
									followUpLocation =row[2].toString();
									
								}
								if (row[3]!= null) {
									followUpLandmarks =row[3].toString();
									
								}
								if (row[4]!= null) {
									patientId =row[4].toString();
									
								}
								if (row[5]!= null) {
									opdPatientDetailsId =row[5].toString();
									
								}
								if (row[6]!= null) {
									patientName =row[6].toString();
									
								}
															
								
							
								if(followUpDate!=null)
								{
								pt.put("followUpDate", followUpDate);
								}
								else
								{
									pt.put("followUpDate", "");	
								}
								if(mobileNumber!=null)
								{
								pt.put("mobileNumber", mobileNumber);
								}
								else
								{
									pt.put("mobileNumber", "");	
								}
								if(followUpLocation!=null)
								{
								pt.put("followUpLocation", followUpLocation);
								}
								else
								{
									pt.put("followUpLocation", "");	
								}
								if(followUpLandmarks!=null)
								{
								pt.put("followUpLandmarks", followUpLandmarks);
								}
								else
								{
									pt.put("followUpLandmarks", "");	
								}
								if(opdPatientDetailsId!=null)
								{
								pt.put("opdPatientDetailsId", opdPatientDetailsId);
								}
								else
								{
									pt.put("opdPatientDetailsId", "");	
								}
								if(patientId!=null)
								{
								pt.put("patientId", patientId);
								}
								else
								{
									pt.put("patientId", "");	
								}
								if(patientName!=null)
								{
								pt.put("patientName", patientName);
								}
								else
								{
									pt.put("patientName", "");	
								}
							
								Long mobileNo=Long.parseLong(pt.get("mobileNumber").toString());
								Long opdPDetailsId=Long.parseLong(pt.get("opdPatientDetailsId").toString());
								String followUpLocationForSend=pt.get("followUpLocation").toString();
								String followUpLandmarksForSend=pt.get("followUpLandmarks").toString();
								String followUpDateForSend=pt.get("followUpDate").toString();
								//mobileNo=8076396055l;
								try {
								//final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Apartment&var1="+followUpLocationForSend+"&var2="+followUpLandmarksForSend+"&var3="+followUpDateForSend+"";
									final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMMSY&templatename=Apartment-New&var1="+patientName+"&var2="+followUpLocation+"&var3="+ followUpDateForSend+"";
									HttpResponse<String> resp = Unirest.post(uri).asString();
								System.out.println(resp.getBody());
								System.out.println("Follow Up Message send successfully");
								if(resp.getBody()!=null)
								{
									boolean status=resp.getBody().contains("Status");
									if(status==true)
									{
										String msgUpdate=schedulerServiceDao.updateFollowUpMsgStatus(opdPDetailsId);
									}
								    
								}
								//return resp.getBody();
								} catch (Exception e) {
									return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
								}
					
						/*c.add(pt);
						json.put("followUpMsgSchduler", c);
						
						json.put("count", count);
						json.put("msg", "data get sucessfully......");
						json.put("status", 1);*/
							}
					 }catch (Exception e) {
							e.printStackTrace();
							return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
					}
			
		}
		
		return "MsgDelivered";
   }
	
	
	public String sendFollowUpMsg(HashMap<String, Object> jsondata, HttpServletRequest request)
	{
		Long mobileNo=Long.parseLong(jsondata.get("mobileNo").toString());
		try {
		final String uri = "https://2factor.in/API/V1/5cdc6365-22b5-11ec-a13b-0200cd936042/SMS/"
				+ "9971844845" + "/AUTOGEN/Upayog-New";
		HttpResponse<String> response = Unirest.post(uri).asString();
		System.out.println(response.getBody());
		System.out.println("OTP Generation is completed");
		return response.getBody();
	} catch (Exception e) {
		return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
	}
  }

	@Override
	public String getMmedicineRolInfo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)schedulerServiceDao.getMmedicineRolInfo(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("rolMedicineInfo");
		JSONArray listMedicalCategory15 = new JSONArray(result.get("rol_medicine").toString());
		for (int i = 0; i < listMedicalCategory15.length(); i++) {
			JSONObject mapMedical = listMedicalCategory15.getJSONObject(i);
		    Long mobileNo=Long.parseLong(mapMedical.get("mobile_no").toString());
			String mmudName=mapMedical.get("mmu_name").toString();
			Long count=Long.parseLong(mapMedical.get("count").toString());
		
			            Long mobileNoSend=Long.parseLong(mobileNo.toString());
						Long countValueSend=Long.parseLong(count.toString());
						String mmudNameSend=mmudName;
						//mobileNoSend=8076396055l;
						try {
						//final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNo+"&from=CGMSSY&templatename=Apartment&var1="+followUpLocationForSend+"&var2="+followUpLandmarksForSend+"&var3="+followUpDateForSend+"";
							final String uri = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobileNoSend+"&from=CGMMSY&templatename=Apartment-New&var1="+countValueSend+"&var2="+mmudNameSend+"&var3="+"Thank you"+"";
							HttpResponse<String> resp = Unirest.post(uri).asString();
						System.out.println(resp.getBody());
						System.out.println("Follow Up Message send successfully");
						if(resp.getBody()!=null)
						{
							System.out.println("Message delivered for respective MMU pharma");
						    
						}
						//return resp.getBody();
						} catch (Exception e) {
							return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
						}
			
				/*c.add(pt);
				json.put("followUpMsgSchduler", c);
				
				json.put("count", count);
				json.put("msg", "data get sucessfully......");
				json.put("status", 1);*/
		}


        return "Msg Delivered for MMU Pharma";
		//return jsonResponse.toString(); 
     }

	@Override
	public String auditUploadData(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String map = schedulerServiceDao.auditUploadData(jsondata, response);
		return map.toString(); 
	}



}
