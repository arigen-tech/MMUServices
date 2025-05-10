package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface ApprovalProcessService {

	String saveOrUpdateANMEntryDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAnmOpdOfflineData(JSONObject jsonObject);

	String saveOrUpdateOpdOfflineData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	/*
	 * Map<String, Object> getApprovalFormatData(HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response);
	 */

}
