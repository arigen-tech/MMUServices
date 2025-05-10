package com.mmu.services.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.ApprovalProcessService;

@RequestMapping("/approval")
@RestController
@CrossOrigin

public class ApprovalProcessController {
	
	@Autowired
	private ApprovalProcessService approvalProcessService;
	
//-------------------------Approval Process-----------------------------------------//	
	
	/*
	 * @RequestMapping(value="/getApprovalFormatData", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * submitDispenceryIndent(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { Map<String,
	 * Object> resData = new HashMap<String,Object>();
	 * resData=approvalProcessService.getApprovalFormatData(payload, request,
	 * response); if (resData.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(resData, HttpStatus.OK);
	 * 
	 * 
	 * 
	 * }
	 */
	
	 @RequestMapping(value="/saveOrUpdateANMEntryDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
		public String saveOrUpdateANMEntryDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response)
		{
			return approvalProcessService.saveOrUpdateANMEntryDetails(jsondata, request, response);
		}
	 
	 @RequestMapping(value = "/getAnmOpdOfflineData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	    public String getAnmOpdOfflineData(@RequestBody HashMap<String, Object> payload) {
	        return approvalProcessService.getAnmOpdOfflineData(new JSONObject(payload));
	    }
	    
	   @RequestMapping(value="/saveOrUpdateAttendanceOfflineData", method = RequestMethod.POST,produces="application/json",consumes="application/json")
		public String saveOrUpdateAttendanceOfflineData(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response)
		{
			return approvalProcessService.saveOrUpdateOpdOfflineData(jsondata, request, response);
		}
	
}
