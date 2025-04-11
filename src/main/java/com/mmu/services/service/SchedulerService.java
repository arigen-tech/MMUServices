package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface SchedulerService {
	
	String getMMUListForPoll(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> savePollInfo(Map<String, Object> requestData);

	String getCampDetailsForMMU(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> saveVehicleLocation(Map<String, Object> requestData);

	String sendFollowupMsgScheduler(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getMmedicineRolInfo(HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response);

	String auditUploadData(HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response);

}
