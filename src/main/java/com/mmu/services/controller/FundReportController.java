package com.mmu.services.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.FundDashboardService;

@RestController
@CrossOrigin
@RequestMapping("/fundDashboard")
public class FundReportController {
	@Autowired
	FundDashboardService fundDashboardService;
	
	@RequestMapping(value="/getFundUpssData", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getUpssInvoiceData(@RequestBody HashMap<String, Object> map,  HttpServletResponse response) {
		return fundDashboardService.getFundInvoiceData(map,response);
	}
}
