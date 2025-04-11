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
import org.springframework.web.servlet.ModelAndView;

import com.mmu.services.service.PatientRegistrationService;

@RestController
@CrossOrigin
@RequestMapping("/registration")
public class PatientRegistrationController {

	@Autowired
	private PatientRegistrationService patientRegistrationService;

	/*
	 * @RequestMapping(value = "/patients", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>> findPatientAndDependentFromEmployee(
	 * 
	 * @RequestBody Map<String, String> requestData) { Map<String, Object>
	 * patientAndDependentFromEmployee = new HashMap<String, Object>();
	 * patientAndDependentFromEmployee =
	 * patientRegistrationService.findPatientAndDependentFromEmployee(requestData);
	 * if (patientAndDependentFromEmployee.isEmpty()) { return new	
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(patientAndDependentFromEmployee,
	 * HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/departmentBloodGroupAndMedicalCategory", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getRecordsFordepartmentBloodGroupAndMedicalCategory(
	 * 
	 * @RequestBody Map<String, String> requestData) { Map<String, Object>
	 * recordsFordepartmentBloodGroupAndMedicalCategory = new HashMap<String,
	 * Object>(); recordsFordepartmentBloodGroupAndMedicalCategory =
	 * patientRegistrationService
	 * .getRecordsFordepartmentBloodGroupAndMedicalCategory(requestData); if
	 * (recordsFordepartmentBloodGroupAndMedicalCategory.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String,
	 * Object>>(recordsFordepartmentBloodGroupAndMedicalCategory, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/tokenNoForDepartmentMultiVisit", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getTokenNoForDepartmentMultiVisit(
	 * 
	 * @RequestBody Map<String, Object> requestData) { Map<String, Object>
	 * tokenNoForDepartmentMultiVisit = new HashMap<String, Object>();
	 * tokenNoForDepartmentMultiVisit =
	 * patientRegistrationService.getTokenNoForDepartmentMultiVisit(requestData); if
	 * (tokenNoForDepartmentMultiVisit.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(tokenNoForDepartmentMultiVisit,
	 * HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/submitPatientDetails", method = RequestMethod.POST)
	 * public ResponseEntity<String> submitPatientDetails(@RequestBody String
	 * requestData) { String result =
	 * patientRegistrationService.submitPatientDetails(requestData); if
	 * (result.isEmpty()) { return new
	 * ResponseEntity<String>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<String>(result, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/showAppointmentForOthers", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * showAppointmentForOthers(@RequestBody Map<String, String> requestData) {
	 * Map<String, Object> showAppointmentForOthers = new HashMap<String, Object>();
	 * showAppointmentForOthers =
	 * patientRegistrationService.showAppointmentForOthers(requestData); if
	 * (showAppointmentForOthers.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(showAppointmentForOthers, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/tokenNoOfDepartmentForOthers", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getTokenNoOfDepartmentForOthers(
	 * 
	 * @RequestBody Map<String, Object> requestData) { Map<String, Object>
	 * tokenNoForOthers = new HashMap<String, Object>();
	 * System.out.println("tokenNoForOthers :" + requestData); tokenNoForOthers =
	 * patientRegistrationService.getTokenNoOfDepartmentForOthers(requestData); if
	 * (tokenNoForOthers.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(tokenNoForOthers, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/submitPatientDetailsForOthers", method =
	 * RequestMethod.POST) public ResponseEntity<String>
	 * submitPatientDetailsForOthers(@RequestBody String requestData) {
	 * 
	 * String result =
	 * patientRegistrationService.submitPatientDetailsForOthers(requestData); if
	 * (result.isEmpty()) { return new
	 * ResponseEntity<String>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<String>(result, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/searchOthersRegisteredPatient", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * searchOthersRegisteredPatient(
	 * 
	 * @RequestBody Map<String, String> requestData) { Map<String, Object>
	 * patientDetails = new HashMap<String, Object>(); patientDetails =
	 * patientRegistrationService.searchOthersRegisteredPatient(requestData); if
	 * (patientDetails.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(patientDetails, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/patientListForUploadDocument", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * patientListForUploadDocument(
	 * 
	 * @RequestBody Map<String, String> requestData) { Map<String, Object>
	 * patientList = new HashMap<String, Object>(); patientList =
	 * patientRegistrationService.patientListForUploadDocument(requestData); if
	 * (patientList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(patientList, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/patientAppointmentHistory", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * patientAppointmentHistory(@RequestBody Map<String, String> requestData) {
	 * Map<String, Object> visitHistoryList = new HashMap<String, Object>();
	 * visitHistoryList =
	 * patientRegistrationService.patientAppointmentHistory(requestData); if
	 * (visitHistoryList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(visitHistoryList, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/visitCancellation", method = RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>>
	 * patientVisitCancellation(@RequestBody Map<String, String> requestData) {
	 * Map<String, Object> cancelVisitStatus = new HashMap<String, Object>();
	 * cancelVisitStatus =
	 * patientRegistrationService.patientVisitCancellation(requestData); if
	 * (cancelVisitStatus.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(cancelVisitStatus, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/getAppointmentTypeList", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String getAppointmentTypeList(@RequestBody
	 * HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse
	 * response) { return patientRegistrationService.getAppointmentTypeList(request,
	 * response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/getExamSubType", method = RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>> getExamSubType(@RequestBody
	 * Map<String, Object> requestData) { Map<String, Object> examSubTypeList = new
	 * HashMap<String, Object>(); examSubTypeList =
	 * patientRegistrationService.getExamSubType(requestData); if
	 * (examSubTypeList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(examSubTypeList, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/getFutureAppointmentWaitingList", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getFutureAppointmentWaitingList(
	 * 
	 * @RequestBody Map<String, Object> requestData) { Map<String, Object>
	 * futureAppointmentWaitingList = new HashMap<String, Object>();
	 * futureAppointmentWaitingList =
	 * patientRegistrationService.getFutureAppointmentWaitingList(requestData); if
	 * (futureAppointmentWaitingList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(futureAppointmentWaitingList,
	 * HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/getInvestigationEmptyResultByOrderNo", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String
	 * getInvestigationEmptyResultByOrderNo(@RequestBody HashMap<String, Object>
	 * map, HttpServletRequest request, HttpServletResponse response) { return
	 * patientRegistrationService.getInvestigationEmptyResultByOrderNo(map, request,
	 * response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/submitInvestigationUp", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String submitInvestigationUp(@RequestBody
	 * HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse
	 * response) { return patientRegistrationService.submitInvestigationUp(map,
	 * request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/savePatientFromUploadDocument", method =
	 * RequestMethod.POST) public ResponseEntity<String>
	 * savePatientFromUploadDocument(@RequestBody String requestData) { String
	 * result =
	 * patientRegistrationService.savePatientFromUploadDocument(requestData); if
	 * (result.isEmpty()) { return new
	 * ResponseEntity<String>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<String>(result, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/opdEmergencySavePatient", method =
	 * RequestMethod.POST) public ResponseEntity<String>
	 * opdEmergencySavePatient(@RequestBody String requestData) { String result =
	 * patientRegistrationService.opdEmergencySavePatient(requestData); if
	 * (result.isEmpty()) { return new
	 * ResponseEntity<String>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<String>(result, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/getStateFromDistrict", method = RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>> getStateFromDistrict(@RequestBody
	 * Map<String, Object> requestData) { Map<String, Object> stateFromDistrict =
	 * new HashMap<String, Object>(); stateFromDistrict =
	 * patientRegistrationService.getStateFromDistrict(requestData); if
	 * (stateFromDistrict.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(stateFromDistrict, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value = "/getRegionFromStation", method = RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>> getRegionFromStation(@RequestBody
	 * Map<String, Object> requestData) { Map<String, Object> regionFromStation =
	 * new HashMap<String, Object>(); regionFromStation =
	 * patientRegistrationService.getRegionFromStation(requestData); if
	 * (regionFromStation.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(regionFromStation, HttpStatus.OK); }
	 */

	@RequestMapping(value = "/getDistrictList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getDistrictList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> districtList = new HashMap<String, Object>();
		districtList = patientRegistrationService.getDistrictList(requestData);
		if (districtList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(districtList, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCityList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getCityList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> cityList = new HashMap<String, Object>();
		cityList = patientRegistrationService.getCityList(requestData);
		if (cityList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(cityList, HttpStatus.OK);
	}

	@RequestMapping(value = "/getMMUList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getMMUList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> mmuList = new HashMap<String, Object>();
		mmuList = patientRegistrationService.getMMUList(requestData);
		if (mmuList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(mmuList, HttpStatus.OK);
	}

	@RequestMapping(value = "/getWardList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getWardList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> wardList = new HashMap<String, Object>();
		wardList = patientRegistrationService.getWardList(requestData);
		if (wardList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(wardList, HttpStatus.OK);
	}

	@RequestMapping(value = "/getZoneList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getZoneList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> zoneList = new HashMap<String, Object>();
		zoneList = patientRegistrationService.getZoneList(requestData);
		if (zoneList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(zoneList, HttpStatus.OK);
	}

	@RequestMapping(value = "/createCampPlan", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createCampPlan(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = patientRegistrationService.createCampPlan(requestData);
		/*
		 * Map<String, Object> zoneList =
		 * patientRegistrationService.getZoneList(requestData); response.put("zoneList",
		 * zoneList.get("list"));
		 */
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCampDetail", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getCampDetail(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> campDetail = new HashMap<String, Object>();
		campDetail = patientRegistrationService.getCampDetail(requestData);
		
		if (campDetail.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(campDetail, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updatePatientInformationOrMakeAppointment", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updatePatientRegistrationAndCreateAppointment(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = patientRegistrationService.updatePatientInformation(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getReligionList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getReligionList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> religionList = new HashMap<String, Object>();
		religionList = patientRegistrationService.getReligionList(requestData);
		if (religionList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(religionList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getBloodGroupList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getBloodGroupList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> bloodGroupList = new HashMap<String, Object>();
		bloodGroupList = patientRegistrationService.getBloodGroupList(requestData);
		if (bloodGroupList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(bloodGroupList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getLabourTyeList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getLabourTyeList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> labourTypeList = new HashMap<String, Object>();
		labourTypeList = patientRegistrationService.getLabourTyeList(requestData);
		if (labourTypeList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(labourTypeList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getIdentificationTypeList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getIdentificationTypeList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> identificationTypeList = new HashMap<String, Object>();
		identificationTypeList = patientRegistrationService.getIdentificationTypeList(requestData);
		if (identificationTypeList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(identificationTypeList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCampDepartment", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getCampDepartment(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> campDepartment = new HashMap<String, Object>();
		campDepartment = patientRegistrationService.getCampDepartment(requestData);
		if (campDepartment.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(campDepartment, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/createPatientAndMakeAppointment", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createPatientAndMakeAppointment(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = patientRegistrationService.createPatientAndMakeAppointment(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/checkIfPatientIsAlreadyRegistered", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> checkIfPatientIsAlreadyRegistered(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		response = patientRegistrationService.checkIfPatientIsAlreadyRegistered(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getMMUDepartment", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getMMUDepartment(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> campDepartment = new HashMap<String, Object>();
		campDepartment = patientRegistrationService.getMMUDepartment(requestData);
		if (campDepartment.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(campDepartment, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPatientList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getPatientList(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = patientRegistrationService.getPatientList(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getOnlinePatientList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getOnlinePatientList(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = patientRegistrationService.getOnlinePatientList(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPatientDataBasedOnVisit", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getPatientDataBasedOnVisit(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = patientRegistrationService.getPatientDataBasedOnVisit(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveVitalDetailsAndUpdateVisit", method = RequestMethod.POST)
	public ResponseEntity<String> saveVitalDetailsAndUpdateVisit(@RequestBody HashMap<String, Object> requestData) {
		String response = patientRegistrationService.saveVitalDetailsAndUpdateVisit(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getSDCMUserTypeDetailsSDCM", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getSDCMUserTypeDetailsSDCM(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> sdcmList = new HashMap<String, Object>();
		if(requestData.containsKey("Dist"))
		{	
		
			sdcmList = patientRegistrationService.getDistrictList(requestData);
		if (sdcmList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		}
		else if(requestData.containsKey("City"))
		{
			sdcmList = patientRegistrationService.getCityList(requestData);
			if (sdcmList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
		}
		else if(requestData.containsKey("MMU"))
		{
			sdcmList = patientRegistrationService.getMMUList(requestData);
			if (sdcmList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
		}
		
		return new ResponseEntity<Map<String, Object>>(sdcmList, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/getWardListWithoutCity", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getWardListWithoutCity(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> wardList = new HashMap<String, Object>();
		wardList = patientRegistrationService.getWardListWithoutCity(requestData);
		if (wardList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(wardList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getRelationList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getRelationList(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> relationList = new HashMap<String, Object>();
		relationList = patientRegistrationService.getRelationList(requestData);
		if (relationList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(relationList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCityIdAndName", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getCityIdAndName(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> cityData = new HashMap<String, Object>();
		cityData = patientRegistrationService.getCityIdAndName(requestData);
		if (cityData.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(cityData, HttpStatus.OK);
	}
	
	//saveLabourRegistration
	@RequestMapping(value = "/saveLabourRegistration", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> saveLabourRegistration(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> cityData = new HashMap<String, Object>();
		cityData = patientRegistrationService.saveLabourRegistration(requestData);
		if (cityData.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(cityData, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteCampPlan", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> deleteCampPlan(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> campData = new HashMap<String, Object>();
		campData = patientRegistrationService.deleteCampPlan(requestData);
		if (campData.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(campData, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getFutureCampPlan", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getFutureCampPlan(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> futurePlan = new HashMap<String, Object>();
		futurePlan = patientRegistrationService.getFutureCampPlan(requestData);
		if (futurePlan.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(futurePlan, HttpStatus.OK);
	}
	
	

}
