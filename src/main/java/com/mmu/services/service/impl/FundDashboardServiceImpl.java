package com.mmu.services.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmu.services.dao.impl.FundInvoiceDashBoardDaoImpl;
import com.mmu.services.service.FundDashboardService;

@Service
public class FundDashboardServiceImpl implements FundDashboardService{

	@Autowired
	FundInvoiceDashBoardDaoImpl fundDaoImpl;
	@Override
	public String getFundInvoiceData(HashMap<String, Object> requestData, HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)fundDaoImpl.getFundInvoiceUpssWise(requestData,  response);
		JSONObject result = (JSONObject)map.get("fundInvoice_info");
		jsonResponse.put("data", result);
		return jsonResponse.toString(); 
	}

}
