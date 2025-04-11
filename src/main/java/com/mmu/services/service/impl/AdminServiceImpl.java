package com.mmu.services.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.AdminDao;
import com.mmu.services.entity.DoctorRoaster;
import com.mmu.services.entity.MasAppointmentSession;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDoctorMapping;
import com.mmu.services.service.AdminService;
import com.mmu.services.utils.HMSUtil;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDao ado;

	@Override
	public String getDepartmentList(HashMap<String, Object> map) {
		JSONObject json = new JSONObject();
		List<Map<String,Object>> departmentList = new ArrayList<>();
		//List<MasDepartment> list = (List<MasDepartment>) ado.getDepartmentList(map);
		Map<String,Object> output = ado.getDepartmentList(map);
		String flag = "";
		if(map.containsKey("flag")) {
			flag = (String) map.get("flag");
		}
		if(flag.equalsIgnoreCase("roaster")) {
			List<MasDepartment> list = (List<MasDepartment>) output.get("list");
			if (list.size() == 0) {
				json.put("status", "0");
				json.put("msg", "Data not found");
			} else {
				for(int i=0;i<list.size();i++) {
					Map<String,Object> data = new HashMap<>();
					data.put("departmentname", HMSUtil.convertNullToEmptyString(list.get(i).getDepartmentName()));
					data.put("departmentId", list.get(i).getDepartmentId());
					departmentList.add(data);
				}
				json.put("departmentList", departmentList);
				json.put("msg", "department List  get  sucessfull... ");
				json.put("status", "1");
			}
			return json.toString();
			
		}else {
			List<MasDepartment> list = (List<MasDepartment>) output.get("list");
			if (list.size() == 0) {
				json.put("status", "0");
				json.put("msg", "Data not found");
			} else {
				for(int i=0;i<list.size();i++) {
					Map<String,Object> data = new HashMap<>();
					data.put("departmentname", HMSUtil.convertNullToEmptyString(list.get(i).getDepartmentName()));
					data.put("departmentId", list.get(i).getDepartmentId());
					departmentList.add(data);
				}
				json.put("departmentList", departmentList);
				json.put("msg", "department List  get  sucessfull... ");
				json.put("status", "1");
			}
			return json.toString();
		}

	}

	@Override
	public String getDoctorList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsobj = new JSONObject();
		List<MasDoctorMapping> list = ado.getDoctorList(jsondata);
		if (list.size() == 0) {
			return "{\"status\",:\"0\",\"msg\",\"Data not found\"}";
		} else {
			jsobj.put("doctorList", list);
			jsobj.put("msg", "Doctor List get successfull");
			jsobj.put("status", "1");
		}

		return jsobj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getDoctorRoaster(HashMap<String, Object> jsondata) {
		Map<String, Object> mapdata = ado.getDoctorRoaster(jsondata);
		//System.out.println("list in adminservice " + mapdata.size());
		JSONObject json = new JSONObject();
		
		List<Map<String,Object>> roasterList = new ArrayList<>();
		List<Map<String,Object>> configurationList = new ArrayList<>();
		List<Map<String,Object>> appSessionList = new ArrayList<>();
		List<DoctorRoaster> list =  (List<DoctorRoaster>) mapdata.get("doctorRoasterDetail");
		List<MasAppointmentSession> list2 =  (List<MasAppointmentSession>) mapdata.get("checkboxConfigurationDetail");
		List<MasAppointmentSession> sessionList =  (List<MasAppointmentSession>) mapdata.get("appointmentSessionList");
		
		if (list.size() == 0) {
			json.put("status", "0");
			json.put("msg", "Data not found");
		} else {
			for(DoctorRoaster doctorRoaster : list) {
				Map<String,Object> jsonResult = new HashMap<>();
				jsonResult.put("id", doctorRoaster.getId());
				jsonResult.put("roasterDate", HMSUtil.convertDateToStringFormat(doctorRoaster.getRoasterDate(),"dd/MM/yyyy"));
				jsonResult.put("roasterValue", doctorRoaster.getRoasterValue());
				/*jsonResult.put("departmentId", doctorRoaster.getMasDepartment().getDepartmentId());
				jsonResult.put("hospitalId", doctorRoaster.getMasHospital().getHospitalId());
				jsonResult.put("lastChgBy", doctorRoaster.getUser().getLastChgBy());
				jsonResult.put("lastChgDate", doctorRoaster.getLastChgDate());*/
				roasterList.add(jsonResult);				
			}
			json.put("doctorRoasterList", roasterList);
			json.put("msg", "Doctor Roaster Detail get successfull");
			json.put("status", "1");
		}
		if (list2.size() > 0) {

			for (MasAppointmentSession apSessionList : list2) {
				Map<String, Object> configurationMap = new HashMap<>();
				if (apSessionList.getMasAppointmentType() != null) {
					configurationMap.put("id", apSessionList.getMasAppointmentType().getAppointmentTypeId());
					configurationMap.put("appintment_Type_Name",
							apSessionList.getMasAppointmentType().getAppointmentTypeName());
				}
				configurationList.add(configurationMap);
			}
		}
		json.put("checkboxConfigurationList", configurationList);
		if (sessionList.size() > 0) {
			sessionList.forEach(items -> {
				Map<String, Object> sessionMap = new HashMap<>();
				if (items.getMasAppointmentType() != null) {
					sessionMap.put("id", items.getMasAppointmentType().getAppointmentTypeId());
					sessionMap.put("appintment_Type_Name", items.getMasAppointmentType().getAppointmentTypeName());
				}
				appSessionList.add(sessionMap);
			});
		}
		json.put("appSessionList", appSessionList);
		return json.toString();
	}

	public String submitDepartmentRoaster(HashMap<String, Object> jsondata) {
		JSONObject jsonobj = new JSONObject();

		String row1 = (String) jsondata.get("row1");
		String row2 = (String) jsondata.get("row2");
		String row3 = (String) jsondata.get("row3");
		String row4 = (String) jsondata.get("row4");
		String row5 = (String) jsondata.get("row5");
		String row6 = (String) jsondata.get("row6");
		String row7 = (String) jsondata.get("row7");

		String[] row1data = row1.split("#");
		String[] row2data = row2.split("#");
		String[] row3data = row3.split("#");
		String[] row4data = row4.split("#");
		String[] row5data = row5.split("#");
		String[] row6data = row6.split("#");
		String[] row7data = row7.split("#");

		List<String[]> alldata = new ArrayList<>();
		alldata.add(row1data);
		alldata.add(row2data);
		alldata.add(row3data);
		alldata.add(row4data);
		alldata.add(row5data);
		alldata.add(row6data);
		alldata.add(row7data);
		/*
		 * List<Date> dateList = new ArrayList<>(); List<String> roasterValue = new
		 * ArrayList<>();
		 */

		String changeTime = null;
		long changeBy = 0;
		long dept_id = 0;
		long hostpitalID = 0;
		long doctorID = 0;

		/*
		 * List<Long> rowID = new ArrayList<>(); if(jsondata.get("row_id") != null) {
		 * rowID = (List<Long>) jsondata.get("row_id"); }
		 * 
		 * List<Date> roasterDate = new ArrayList<>(); if(jsondata.get("roaster_date")
		 * != null) { roasterDate = (List<Date>) jsondata.get("roaster_date"); }
		 * 
		 * List<String> roasterValue = new ArrayList<>();
		 * if(jsondata.get("roaster_value") != null) { roasterValue = (List<String>)
		 * jsondata.get("roaster_value"); }
		 */

		String changeDate = (String) jsondata.get("current_date");
		// java.sql.Date changeDate =
		// CommonUtil.convertStringDateToSQLDate((String)jsondata.get("current_date"));

		if (jsondata.get("change_time") != null) {
			changeTime = (String) jsondata.get("change_time");
		}

		if (jsondata.get("change_by") != null) {
			changeBy = Long.parseLong((String) jsondata.get("change_by"));
		}

		if (jsondata.get("deptID") != null) {
			dept_id = Long.parseLong((String) jsondata.get("deptID"));
		}

		if (jsondata.get("hospital_id") != null) {
			hostpitalID = Long.parseLong((String) jsondata.get("hospital_id"));
		}
		if (jsondata.get("doctorID") != null) {
			doctorID = Long.parseLong((String) jsondata.get("doctorID"));
		}

		String roasterMsg = ado.submitDepartmentRoaster(alldata, changeDate, changeTime, changeBy, dept_id, hostpitalID);
		if (roasterMsg != null && !roasterMsg.equals("")) {
			jsonobj.put("Msg", roasterMsg);
			jsonobj.put("status", 1);
		} else {
			jsonobj.put("Msg", roasterMsg);
			jsonobj.put("status", 0);
		}
		return jsonobj.toString();
	}

	@Override
	public String getDepartmentListBasedOnDepartmentType(HashMap<String, Object> map) {
		JSONObject json = new JSONObject();
		List<Map<String,Object>> departmentList = new ArrayList<>();
		List<Map<String,Object>> citytList = new ArrayList<>();
		List<MasDepartment> list = (List<MasDepartment>) ado.getDepartmentListBasedOnDepartmentType(map);
		List<MasCity> listMasCity = (List<MasCity>) ado.getAllCity(map);
		if(listMasCity.size()>0) {
			listMasCity.stream().forEach((masCity->{
				Map<String,Object> data = new HashMap<>();
				data.put("cityId", masCity.getCityId());
				data.put("cityName", masCity.getCityName());
				citytList.add(data);
			}));
			}
		
		if (list.size() == 0) {
			json.put("status", "0");
			json.put("msg", "Data not found");
			json.put("citytList", citytList);
			 
		} else {
			for(int i=0;i<list.size();i++) {
				Map<String,Object> data = new HashMap<>();
				data.put("departmentname", HMSUtil.convertNullToEmptyString(list.get(i).getDepartmentName()));
				data.put("departmentId", list.get(i).getDepartmentId());
				data.put("departmentType", HMSUtil.convertNullToEmptyString(list.get(i).getMasDepartmentType().getDepartmentTypeCode()));
				departmentList.add(data);
			}
			
			
			
			json.put("departmentList", departmentList);
			json.put("msg", "department List  get  sucessfull... ");
			json.put("status", "1");
			json.put("citytList", citytList);
		}
		return json.toString();
	}

	
}
