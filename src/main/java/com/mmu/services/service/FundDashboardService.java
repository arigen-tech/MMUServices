package com.mmu.services.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

public interface FundDashboardService {
	public String getFundInvoiceData(HashMap<String, Object> map,HttpServletResponse response);
}
