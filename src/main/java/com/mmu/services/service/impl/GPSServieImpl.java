package com.mmu.services.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmu.services.dao.GPSDao;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.Users;
import com.mmu.services.service.GPSService;
import com.mmu.services.utils.HMSUtil;

@Service("GPSServie")
public class GPSServieImpl implements GPSService {

	@Autowired
	GPSDao gpsDao;

	@Override
	public String getGPSInfo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		String gpsUrl = HMSUtil.getProperties("adt.properties", "gpsAPIUrl").trim();

		String gpsClientId = HMSUtil.getProperties("adt.properties", "gpsClientId").trim();

		String gpsSecretKey = HMSUtil.getProperties("adt.properties", "gpsSecretKey").trim();

		String authString = gpsClientId + ":" + gpsSecretKey;
		String base64Creds = Base64.encodeBase64String(authString.getBytes());

		//HttpHeaders headers = new HttpHeaders();


		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("X-IBM-Client-Id", gpsClientId.toString());
		headers.add("X-IBM-Client-Secret", gpsSecretKey.toString());
		headers.add("Content-Type", "application/json");
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		LocalDate today = LocalDate.now();

		//System.out.println("Start time: "+LocalTime.now());
		String startDate = today.toString().replaceAll("-", "") + "" + "070000";
		String endDate = today.toString().replaceAll("-", "") + "" + "210000";
		Map<String, Object> map = gpsDao.getAllDistricts(jsondata);

		List<MasCamp> campList = (List<MasCamp>) map.get("campList");
		MasDistrict dist = (MasDistrict) map.get("dist");
		 ObjectMapper mapper = new ObjectMapper(); 
		JSONObject outPutJson = new JSONObject();
		
		outPutJson.put("districtName", dist.getDistrictName());
		outPutJson.put("lat", dist.getLattitude());
		outPutJson.put("lng", dist.getLongitude());
		outPutJson.put("id", dist.getDistrictCode());
		outPutJson.put("totalMMU", campList.size());
		List<Object> outPutList = new ArrayList<Object>();
		for (MasCamp camRec : campList) {
			String startTime="";
			String endTime="";
			String location="";
			String landmark="";
			JSONObject inputJson = new JSONObject();
			Map<String, Object> outMMUMap = new HashMap<String, Object>();
			outMMUMap.put("id", camRec.getMasMMU().getMmuNo());
			outMMUMap.put("name", camRec.getMasMMU().getMmuName());
			String[] chassisNo = new String[] { camRec.getMasMMU().getChassisNo().toString() };
			Long mmuId= Long.parseLong(camRec.getMasMMU().getMmuId().toString());
			System.out.println("mmuId--> "+mmuId);
			//List<Object[]> campList = gpsDao.getCampDetails(today,mmuId);
			//System.out.println("campList on impl--> "+campList.size());
		
			//if(campList.size() > 0) {
				startTime = camRec.getStartTime().toString();
				endTime = 	camRec.getEndTime().toString();
				location = camRec.getLocation();
				landmark = camRec.getLandMark();
			//}
			
			outMMUMap.put("startTime", startTime);
			outMMUMap.put("endTime", endTime);
			outMMUMap.put("location",location);
			outMMUMap.put("landmark", landmark);
			
			String mmu = mmuId.toString();
			
			//commented staff details as the code is taking too much time to get the users details on 13th dec 2023
			
			//List<Users> userList = gpsDao.getUserDetails(mmu);
			//StringBuilder users = new StringBuilder();
			/*
			 * for( Users user:userList) {
			 * 
			 * users.append(user.getUserName()).append("[").append(user.getMasUserType().
			 * getUserTypeName()).append("]").append(", ");
			 * 
			 * }
			 */
			//String staffs = users.toString().replaceAll(", $", "");;
			//outMMUMap.put("staffAvailable", staffs);
			inputJson.put("startDateTime", startDate);
			inputJson.put("endDateTime", endDate);
			inputJson.put("chassisno", chassisNo);
			inputJson.put("latestOnly", true);
			 //System.out.println("inputJson json--->"+inputJson.toString());
			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			HttpEntity<String> requestNew = new HttpEntity<String>(inputJson.toString(), headers);
			ResponseEntity<String> apiResponse = null;
			System.out.println(requestNew.toString());
			apiResponse=restTemplate.postForEntity(gpsUrl, requestNew, String.class);
			
			
			
			 String json = apiResponse.getBody();
			// System.out.println("json--->"+json);
			 String tempStr = json.substring(1, json.length()-1);
			 Map<String, List<HashMap<String, Object>>> responseMap;
			try {
				responseMap = mapper.readValue(tempStr, Map.class);
			
				if(responseMap.get("positiondata") != null) {
				 List<HashMap<String, Object>> apiResp = responseMap.get("positiondata");
				
					  Double lattitude = Double.parseDouble(String.valueOf(apiResp.get(0).get("lattitude")));
					  Double longitude = Double.parseDouble(String.valueOf(apiResp.get(0).get("longitude")));
					  outMMUMap.put("lat", lattitude);
					  outMMUMap.put("lng", longitude);
				} 
				outPutList.add(outMMUMap);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
		outPutJson.put("mmu", outPutList);
		//System.out.println("End time: "+LocalTime.now());
		return outPutJson.toString();
	}

	@Override
	public Map<String, Object> getDistrictListForMap(Map<String, Object> requestData) {


		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> districtList = new ArrayList<Map<String, Object>>();
		try {

			List<MasDistrict> list = (List<MasDistrict>) gpsDao.getDistrictList(requestData);
			if (!list.isEmpty()) {
				for (MasDistrict district : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("districtId", district.getDistrictId());
					map2.put("districtName", district.getDistrictName());
					districtList.add(map2);
				}
				response.put("status", true);
				response.put("list", districtList);
			} else {
				response.put("status", true);
				response.put("list", districtList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", districtList);
		return response;
	
	}

	

	@Override
	public String getCampInfoAllDistrict(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONObject outJson = new JSONObject();
		Map<String, Object> map = gpsDao.getCampInfoAllDistrict(jsondata);
		Map<Long, List<MasCamp>> campMap = new HashMap<Long, List<MasCamp>>();
		
		if(map.get("campMap") != null) {
			campMap = (Map<Long, List<MasCamp>>) map.get("campMap");
		}
		Set<Long> distIds = campMap.keySet();
		
		
		List<HashMap<String, Object>> districtInfo = new ArrayList<HashMap<String, Object>>();
		for(Long distId : distIds) {
			HashMap<String, Object> distInfo = new HashMap<String, Object>();
			distInfo.put("distId", distId);
			List<MasCamp> campList = campMap.get(distId);
			List<HashMap<String, Object>> mmuInfoList = new ArrayList<HashMap<String, Object>>();
			for(MasCamp camp : campList) {
				HashMap<String, Object> mmuInfo = new HashMap<String, Object>();
				
				mmuInfo.put("mmuName", camp.getMasMMU().getMmuName());
				mmuInfo.put("location", camp.getLocation());
				mmuInfo.put("landmark", camp.getLandMark());
				mmuInfo.put("startTime", camp.getStartTime());
				mmuInfo.put("endTime", camp.getEndTime());
				mmuInfoList.add(mmuInfo);
				
				
			}
			distInfo.put("mmu", mmuInfoList);
			districtInfo.add(distInfo);
			outJson.put("data", districtInfo);
		}
		
		
		return outJson.toString();
	}

}
