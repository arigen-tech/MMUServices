package com.mmu.services.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.GPSService;

@RestController
@CrossOrigin
@RequestMapping("/gps")
public class GPSController {

	
	@Autowired
	private GPSService gpsService;
	
	@RequestMapping(value="/getGPSInfo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getGPSInfo(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return gpsService.getGPSInfo(jsondata, request, response);
	}
	
	
	@RequestMapping(value = "/getDistrictListForMap", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getIdTypeList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> distList = new HashMap<String, Object>();
		distList = gpsService.getDistrictListForMap(requestData);
		if (distList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(distList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getCampInfoAllDistrict", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getCampInfoAllDistrict(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return gpsService.getCampInfoAllDistrict(jsondata, request, response);
	}
	
}
