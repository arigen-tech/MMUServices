package com.mmu.services.dao;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;


@Repository
public interface SchedulerServiceDao {

	
	Map<String, Object> getMMUListForPoll(HashMap<String,Object> json);

	Map<String, Object> savePollInfo(Map<String, Object> requestData);

	Map<String, Object> getCampDetailsForMMU(HashMap<String, Object> jsondata);

	Map<String, Object> saveVehicleLocation(Map<String, Object> requestData);

	Map<String, Object> getFollowupMsgData();

	String updateFollowUpMsgStatus(Long opdPatientDetailsId);

	Map<String, Object> getMmedicineRolInfo(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String auditUploadData(HashMap<String, String> jsondata, HttpServletResponse response);
}
