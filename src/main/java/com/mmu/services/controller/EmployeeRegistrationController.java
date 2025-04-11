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

import com.mmu.services.service.EmployeeRegistrationService;

@RestController
@CrossOrigin
@RequestMapping("/empRegistration")
public class EmployeeRegistrationController {

	@Autowired
	private EmployeeRegistrationService empRegistrationService;

	
	
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveEmployee(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.saveEmployee(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/savedEmployeeList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String savedEmployeeList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return empRegistrationService.savedEmployeeList(jsondata, request, response);
	}
	
	@RequestMapping(value="/getEmployeeDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getEmployeeDetails(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return empRegistrationService.getEmployeeDetails(jsondata, request, response);
	}
	
	@RequestMapping(value = "/saveAPMAction", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveAPMAction(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.saveAPMAction(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveAUDAction", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveAUDAction(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.saveAUDAction(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveCHMOAction", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveCHMOAction(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.saveCHMOAction(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/saveUPSSAction", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveUPSSAction(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.saveUPSSAction(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/checkDuplicateMobile", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> checkDuplicateMobile(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.checkDuplicateMobile(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getIdTypeList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getIdTypeList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> idTypeList = new HashMap<String, Object>();
		idTypeList = empRegistrationService.getIdTypeList(requestData);
		if (idTypeList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(idTypeList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllUserTypeForEmpReg", method = RequestMethod.POST)
	public String getAllUserTypeForEmpReg(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String getAllUserType = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllUserType = empRegistrationService.getAllUserTypeForEmpReg(jsonObject, request, response);
		return getAllUserType;
	}
	@RequestMapping(value="/pendingAttendanceList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String pendingAttendanceList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return empRegistrationService.pendingAttendanceList(jsondata, request, response);
	}
	
	@RequestMapping(value = "/saveAttendance", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveAttendance(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.saveAttendance(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/pendingListPhoto", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String pendingListPhoto(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return empRegistrationService.pendingListPhoto(jsondata, request, response);
	}
	
	@RequestMapping(value = "/savePhotoValidation", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> savePhotoValidation(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.savePhotoValidation(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveAttendanceAudit", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveAttendanceAudit(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.saveAttendanceAudit(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAttendanceYears", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getAttendanceYears(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> attendanceYears = new HashMap<String, Object>();
		attendanceYears = empRegistrationService.getAttendanceYears(requestData);
		if (attendanceYears.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(attendanceYears, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAttendanceMonths", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getAttendanceMonths(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> attendanceMonths = new HashMap<String, Object>();
		attendanceMonths = empRegistrationService.getAttendanceMonths(requestData);
		if (attendanceMonths.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(attendanceMonths, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getPenaltyList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPenaltyList(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return empRegistrationService.getPenaltyList(jsondata, request, response);
	}
	
	
	
	@RequestMapping(value = "/auditAttendanceHistory", method = RequestMethod.POST)
	public String auditAttendanceHistory(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		return empRegistrationService.auditAttendanceHistory(requestData);
	
	}
	
	@RequestMapping(value = "/checkDuplicateIMEI", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> checkDuplicateIMEI(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.checkDuplicateIMEI(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getEmpListForEdit", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getEmpListForEdit(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return empRegistrationService.getEmpListForEdit(jsondata, request, response);
	}
	
	@RequestMapping(value="/getEmployeeRecordForUpdate", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getEmployeeRecordForUpdate(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return empRegistrationService.getEmployeeRecordForUpdate(jsondata, request, response);
	}
	
	@RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updateEmployee(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = empRegistrationService.updateEmployee(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
