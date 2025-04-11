package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface GPSService {

	String getGPSInfo(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);

	Map<String, Object> getDistrictListForMap(Map<String, Object> requestData);


	String getCampInfoAllDistrict(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

}
