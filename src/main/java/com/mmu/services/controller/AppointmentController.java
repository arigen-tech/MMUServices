package com.mmu.services.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.AppointmentService;

@RestController
@CrossOrigin
@RequestMapping("/appointment")
public class AppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	/*
	 * @RequestMapping(value = "/recordsforAppointmentSetUp", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getRecordsForDoctorAppointment(@RequestBody Map<String, String> requestData)
	 * { Map<String, Object> dataForDoctorAppointment = new
	 * HashMap<String,Object>(); dataForDoctorAppointment =
	 * appointmentService.getDataForDoctorAppointment(requestData); if
	 * (dataForDoctorAppointment.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(dataForDoctorAppointment, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/locationWiseAppointmentType", method
	 * =RequestMethod.POST) public ResponseEntity<Map<String,
	 * Object>>getlocationWiseAppointmentType(@RequestBody Map<String, String>
	 * requestData){ Map<String, Object> locationWiseAppoitmentType = new
	 * HashMap<String,Object>(); locationWiseAppoitmentType =
	 * appointmentService.getlocationWiseAppointmentType(requestData);
	 * if(locationWiseAppoitmentType.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String,
	 * Object>>(locationWiseAppoitmentType,HttpStatus.OK); }
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/appointmentSetupDetails", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getappointmentSetupDetails(@RequestBody Map<String, String> requestData) {
	 * Map<String, Object> recordsForAppointmentSetupDetails = new
	 * HashMap<String,Object>(); recordsForAppointmentSetupDetails =
	 * appointmentService.getappointmentSetupDetails(requestData); if
	 * (recordsForAppointmentSetupDetails.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	 * 
	 * }
	 * 
	 * return new ResponseEntity<Map<String,
	 * Object>>(recordsForAppointmentSetupDetails, HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value = "/submitAppointmentSetup", method =
	 * RequestMethod.POST) public ResponseEntity<String>
	 * submitAppointmentSetup(@RequestBody String requestData) {
	 * System.out.println("submitAppointmentSetup: " +requestData); String
	 * recordsForAppointmentSetupDetails =
	 * appointmentService.submitAppointmentSetup(requestData); if
	 * (recordsForAppointmentSetupDetails.isEmpty()) { return new
	 * ResponseEntity<String>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<String>(recordsForAppointmentSetupDetails, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/showappointmentsession", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * showAppointmentSession() { Map<String, Object> appointmentSessionDetails =
	 * new HashMap<String,Object>(); appointmentSessionDetails =
	 * appointmentService.getAppointmentSessionDetails(); if
	 * (appointmentSessionDetails.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(appointmentSessionDetails, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/submitappointmentsession", method =
	 * RequestMethod.POST) public ResponseEntity<String>
	 * submitAppointmentSession(@RequestBody String requestData) { String
	 * appointmentSessionDetails =
	 * appointmentService.submitAppointmentSession(requestData); if
	 * (appointmentSessionDetails.isEmpty()) { return new
	 * ResponseEntity<String>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<String>(appointmentSessionDetails, HttpStatus.OK); }
	 * 
	 * //BY ANITA //get Hospital list
	 * 
	 * @RequestMapping(value = "/getHospitalList", method = RequestMethod.POST)
	 * public ResponseEntity<Map<Long, String>> getHospitalList1() { Map<Long,
	 * String> hospitalList = new HashMap<Long,String>(); hospitalList =
	 * appointmentService.getAllHospitalList1(); if (hospitalList.isEmpty()) {
	 * return new ResponseEntity<Map<Long, String>>(HttpStatus.NO_CONTENT); } return
	 * new ResponseEntity<Map<Long, String>>(hospitalList, HttpStatus.OK); }
	 * 
	 * //get Hospital list from app setup table
	 * 
	 * @RequestMapping(value = "/getHospitalListFromAppSetup", method =
	 * RequestMethod.POST) public ResponseEntity<Map<Long, String>>
	 * getHospitalListFromAppSetup() { Map<Long, String> hospitalList = new
	 * HashMap<Long,String>(); hospitalList =
	 * appointmentService.getHospitalListFromAppSetup(); if (hospitalList.isEmpty())
	 * { return new ResponseEntity<Map<Long, String>>(HttpStatus.NO_CONTENT); }
	 * return new ResponseEntity<Map<Long, String>>(hospitalList, HttpStatus.OK); }
	 * 
	 * //get Gender list
	 * 
	 * @RequestMapping(value = "/getGenderList", method = RequestMethod.POST) public
	 * ResponseEntity<Map<Long, String>> getGenderList() { Map<Long, String>
	 * genderList = new HashMap<Long,String>(); genderList =
	 * appointmentService.getGenderList(); if (genderList.isEmpty()) { return new
	 * ResponseEntity<Map<Long, String>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<Long, String>>(genderList, HttpStatus.OK); }
	 * 
	 * //get Relation list
	 * 
	 * @RequestMapping(value = "/getRelationList", method = RequestMethod.POST)
	 * public ResponseEntity<Map<Long, String>> getRelationList() { Map<Long,
	 * String> relationList = new HashMap<Long,String>(); relationList =
	 * appointmentService.getRelationrList(); if (relationList.isEmpty()) { return
	 * new ResponseEntity<Map<Long, String>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<Long, String>>(relationList, HttpStatus.OK); }
	 * 
	 * 
	 * //get Appointment History
	 * 
	 * @RequestMapping(value = "/getAppointmentHistory", method =
	 * RequestMethod.POST)
	 * 
	 * public ResponseEntity<Map<String, Object>> getAppointmentHistory(@RequestBody
	 * String requestData) throws JSONException, ParseException { Map<String,
	 * Object> appointmentHistoryDetails =
	 * appointmentService.getAppointmentHistory(requestData); if
	 * (appointmentHistoryDetails.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); }
	 * 
	 * return new ResponseEntity<Map<String, Object>>(appointmentHistoryDetails,
	 * HttpStatus.OK); }
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/reschedulePatientAppointment", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * reschedulePatientAppointment(@RequestBody String requestData) throws
	 * JSONException, ParseException { Map<String, Object> appointmentHistoryDetails
	 * =new HashMap<String,Object>(); appointmentHistoryDetails =
	 * appointmentService.reschedulePatientAppointment(requestData); if
	 * (appointmentHistoryDetails.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); }
	 * 
	 * return new ResponseEntity<Map<String, Object>>(appointmentHistoryDetails,
	 * HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value = "/getOpdHistory", method = RequestMethod.POST)
	 * 
	 * public ResponseEntity<Map<String, Object>> getOpdHistory(@RequestBody String
	 * requestData) throws JSONException, ParseException { Map<String, Object>
	 * appointmentHistoryDetails = appointmentService.getOpdHistory(requestData); if
	 * (appointmentHistoryDetails.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); }
	 * 
	 * return new ResponseEntity<Map<String, Object>>(appointmentHistoryDetails,
	 * HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value = "/checkVisitExist", method = RequestMethod.POST)
	 * 
	 * public ResponseEntity<Map<String, Object>> checkVisitExist(@RequestBody
	 * String requestData) throws JSONException, ParseException { Map<String,
	 * Object> visitStatus = appointmentService.checkVisitExistOrNot(requestData);
	 * if (visitStatus.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); }
	 * 
	 * return new ResponseEntity<Map<String, Object>>(visitStatus, HttpStatus.OK); }
	 * 
	 * 
	 * //BY ANITA END
	 * 
	 * @RequestMapping(value="/getAllAppointmentSession",
	 * method=RequestMethod.POST,produces = "application/json", consumes =
	 * "application/json") public String getAllAppointmentSession(@RequestBody
	 * Map<String, Object> requestData) { JSONObject jsonObject = new
	 * JSONObject(requestData); return
	 * appointmentService.getAllAppointmentSession(jsonObject); }
	 * 
	 * @RequestMapping(value = "/hospitals", method =RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>>getHospitalList(){ Map<String, Object>
	 * hospitalList = new HashMap<String,Object>(); hospitalList =
	 * appointmentService.getHospitalList(); if(hospitalList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(hospitalList,HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value = "/updateAppointmentSession", method =
	 * RequestMethod.POST) public ResponseEntity<String>
	 * updateAppointmentSession(@RequestBody String requestData) { String
	 * updatedStatusAppSession =
	 * appointmentService.updateAppointmentSession(requestData); if
	 * (updatedStatusAppSession.isEmpty()) { return new
	 * ResponseEntity<String>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<String>(updatedStatusAppSession, HttpStatus.OK); }
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/getDepartmentListx", method =RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>>getDepartmentList(@RequestBody
	 * Map<String, String> requestData){ Map<String, Object> departmentList = new
	 * HashMap<String,Object>(); departmentList =
	 * appointmentService.getDepartmentList(requestData);
	 * if(departmentList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(departmentList,HttpStatus.OK); }
	 * 
	 * //get Lab History
	 * 
	 * @RequestMapping(value = "/getLabHistory", method = RequestMethod.POST)
	 * 
	 * public ResponseEntity<Map<String, Object>> getLabHistory(@RequestBody String
	 * requestData) throws JSONException, ParseException { Map<String, Object>
	 * appointmentHistoryDetails = appointmentService.getLabHistory(requestData); if
	 * (appointmentHistoryDetails.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); }
	 * 
	 * return new ResponseEntity<Map<String, Object>>(appointmentHistoryDetails,
	 * HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value = "/getDoctorListFromMapping", method
	 * =RequestMethod.POST) public ResponseEntity<Map<String,
	 * Object>>getDoctorListFromMapping(@RequestBody Map<String, String>
	 * requestData){ Map<String, Object> doctorMappingList = new
	 * HashMap<String,Object>(); doctorMappingList =
	 * appointmentService.getDoctorListFromMapping(requestData);
	 * if(doctorMappingList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(doctorMappingList,HttpStatus.OK); }
	 */
}
