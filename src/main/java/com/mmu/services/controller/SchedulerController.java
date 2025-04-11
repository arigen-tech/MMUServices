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

import com.mmu.services.service.SchedulerService;

@RestController
@CrossOrigin
@RequestMapping("/scheduler")
public class SchedulerController {

	@Autowired
	private SchedulerService schedulerService;

	
	@RequestMapping(value = "/getMMUListForPoll", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getMMUListForPoll(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return schedulerService.getMMUListForPoll(jsondata, request, response);
	}
	
	
	@RequestMapping(value = "/savePollInfo", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> savePollInfo(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = schedulerService.savePollInfo(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getCampDetailsForMMU", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getCampDetailsForMMU(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return schedulerService.getCampDetailsForMMU(jsondata, request, response);
	}
	
	
	@RequestMapping(value = "/saveVehicleLocation", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveVehicleLocation(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = schedulerService.saveVehicleLocation(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/sendFollowupMsgScheduler", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String sendFollowupMsgScheduler(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return schedulerService.sendFollowupMsgScheduler(jsondata, request, response);
	}
	
	@RequestMapping(value="/getMmedicineRolInfo", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getMmedicineRolInfo(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return schedulerService.getMmedicineRolInfo(map,request,response);
	}
	
	@RequestMapping(value = "/auditUploadData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String auditUploadData(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return schedulerService.auditUploadData(jsondata, request, response);
	}
	
	
}
