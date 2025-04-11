package com.mmu.services.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.impl.DashBoardDaoImpl;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreInternalIndentT;
import com.mmu.services.entity.Users;
import com.mmu.services.service.DashBoardService;
import com.mmu.services.utils.HMSUtil;


@Service
public class DashBoardServiceImpl implements DashBoardService {
	
	@Autowired
	DashBoardDaoImpl dashDao;
	
	@Override
	public String getDashBoardData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getDashBoardData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("dashboard_data");
		jsonResponse.put("dashboard_data", result);
		return jsonResponse.toString(); 
	}
	
	@Override
	public String getHomePageData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getHomePageData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("homepage_data");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getPandmicZoneData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getPandemicZoneData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("pandemic_info");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getInvoiceData(HashMap<String, Object> requestData, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getInvoiceData(requestData,  response);
		JSONObject result = (JSONObject)map.get("pandemic_info");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getUpssInvoiceData(HashMap<String, Object> requestData, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getUpssInvoiceData(requestData,  response);
		JSONObject result = (JSONObject)map.get("upss_data_info");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getMedicineInvoiceData(HashMap<String, Object> requestData, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getMedicineInvoiceData(requestData,  response);
		JSONObject result = (JSONObject)map.get("pandemic_info");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}


	@Override
	public String getAuthorityWiseData(HashMap<String, Object> requestData, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getAuthorityWiseStatus(requestData,  response);
		JSONObject result = (JSONObject)map.get("authority_wise_status");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}

	
	@Override
	public String getAuditDashBoardData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getAuditDashBoardData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("dashboard_data");
		jsonResponse.put("dashboard_data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getAuditDashBoardAuditorData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getAuditDashBoardAuditorData(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("dashboard_auditor_data");
		jsonResponse.put("dashboard_auditor_data", result);
		return jsonResponse.toString(); 
	}

	@Override
	public String getMMUMedicineData(HashMap<String, Object> jsondata, 	HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)dashDao.getMMUMecineInvoiceData(jsondata, response);
		JSONObject result = (JSONObject)map.get("upss_data_info");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}
	
	 
	 
	@Override
	public String getAllInvoiceDetail(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<StoreInternalIndentT> storeInternalIndentMlList = new ArrayList<StoreInternalIndentT>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<StoreInternalIndentT>> mapApprovindAuthorityList= dashDao.getAllStoreInternalIndentT(jsondata);
			List totalMatches = new ArrayList();
			if (mapApprovindAuthorityList.get("masFinancialYearList") != null) {
				storeInternalIndentMlList = mapApprovindAuthorityList.get("masFinancialYearList");
				totalMatches = mapApprovindAuthorityList.get("totalMatches");
				 {
					 
					 storeInternalIndentMlList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("storeInternalTId", ct.getId());
								 
								
								
								 String indentDate = "";
								
								//Date s =  HMSUtil.convertStringDateToUtilDate(row[2].toString(), "yyyy-MM-dd");
								 if(ct.getStoreInternalIndentM1()!=null && ct.getStoreInternalIndentM1().getDemandDate()!=null)
								 indentDate = HMSUtil.convertDateToStringFormat(
						    		ct.getStoreInternalIndentM1().getDemandDate(), "dd/MM/yyyy");
						     	
						     	mapObj.put("indentDate",indentDate);
						     	String medicineName="";
						     	 
						     	if( ct.getMasStoreItem()!=null && 
						     			ct.getMasStoreItem().getNomenclature()!=null)
						     	  medicineName=ct.getMasStoreItem().getNomenclature();
						     	 mapObj.put("medicineName",medicineName);
								 
						     	 Long qtyRequest=0l;
						     	 
						     	if(ct.getQtyRequest()!=0
						     			 )
						     		qtyRequest= ct.getQtyRequest();
						    	mapObj.put("qtyRequest", qtyRequest);
								
						    	 Long qtyReceived=0l;
						     	  	if( ct.getQtyReceived()!=null
							     			 )
							     		qtyReceived=ct.getQtyReceived();
							    	mapObj.put("qtyReceived", qtyReceived);
								
							    	 Double totalCost=0.0;
							     	  	if(ct.getTotalCost()!=null
								     			 )
							     	  		totalCost=ct.getTotalCost();
								    	mapObj.put("totalCost", totalCost);
									
							 	list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	

}
