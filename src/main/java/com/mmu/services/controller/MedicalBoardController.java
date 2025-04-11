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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.service.MedicalBoardService;
import com.mmu.services.service.impl.OpdServiceImpl;

@RequestMapping("/medicalBoard")
@RestController
@CrossOrigin
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MedicalBoardController {

	@Autowired
	MedicalBoardService mbs;

	/*
	 * @RequestMapping(value="/getPIMBWaitingList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getPIMBWaitingList(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getPIMBWaitingList(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMBPatientDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMBPatientDetails(@RequestBody HashMap<String, String>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.MbPatientDetails(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMedicalBoardAutocomplete", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMedicalBoardAutocomplete(@RequestBody HashMap<String,
	 * String> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * return mbs.getMedicalBoardAutocomplete(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/savePiMBInvestigation", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String savePiMBInvestigation(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.savePiMBInvestigation(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMbPreAssestmentWaitingList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMbPreAssestmentWaitingList(@RequestBody HashMap<String,
	 * Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * return mbs.getMbPreAssestmentWaitingList(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getMbPreAssestmentDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMbPreAssestmentDetails(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getMbPreAssestmentDetails(jsondata, request, response); }
	 * 
	 * @JsonBackReference
	 * 
	 * @RequestMapping(value="/saveMbPreAssestmentDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String saveMbPreAssestmentDetails(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.saveMbPreAssestmentDetails(payload, request, response); }
	 * 
	 * @RequestMapping(value="/saveReferforOpinionMBDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String saveReferforOpinionMBDetails(@RequestBody HashMap<String,
	 * Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * return mbs.saveReferforOpinionMBDetails(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getPatientDetailToValidate", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getPatientDetailToValidate(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getPatientDetailToValidate(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/getPatientReferalDetail", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getPatientReferalDetail(@RequestBody HashMap<String, String>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getPatientReferalDetail(jsondata, request, response); }
	 * 
	 * @RequestMapping(value = "/getInvestigationAndResult") public String
	 * getInvestigationAndResult(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getInvestigationAndResult(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/submitMedicalBoardByTranscription", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String
	 * submitMedicalBoardByTranscription(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.submitMedicalBoardByTranscription(payload, request, response);
	 * 
	 * }
	 * 
	 * ////////////////////////////save AMSF form details controller 01st Aug 2019
	 * ////////////////////////
	 * 
	 * @RequestMapping(value = "/saveAmsfForm16Details", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String saveAmsfForm16Details(@RequestBody
	 * HashMap<String, Object> jsondata, HttpServletRequest request,
	 * HttpServletResponse response) { return mbs.saveAmsfForm16Details(jsondata,
	 * request, response); }
	 * 
	 * 
	 * @RequestMapping(value="/getMOValidateWaitingList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getMOValidateWaitingList(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getMOValidateWaitingList(jsondata, request, response); }
	 * ////////////////////////////get AMSF 16 form details controller 03rd Aug 2019
	 * ////////////////////////
	 * 
	 * @RequestMapping(value = "/getAmsfFormDetails", method = RequestMethod.POST,
	 * produces = "application/json", consumes = "application/json") public String
	 * getAmsfFormDetails(@RequestBody HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getAmsfFormDetails(jsondata, request, response); }
	 * 
	 * ////////////////////////////save and update specialist opinion controller
	 * 17th Aug 2019 ////////////////////////
	 * 
	 * 
	 * @RequestMapping(value = "/saveUpdateSpecialistMBDetails", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String saveUpdateSpecialistMBDetails(@RequestBody
	 * HashMap<String, Object> jsondata, HttpServletRequest request,
	 * HttpServletResponse response) { return
	 * mbs.saveUpdateSpecialistMBDetails(jsondata, request, response); }
	 * 
	 * ////////////////////////////save AMSF 15 form details controller 14 Sep 2019
	 * ////////////////////////
	 * 
	 * @RequestMapping(value = "/saveAmsfForm15Details", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String saveAmsfForm15Details(@RequestBody
	 * HashMap<String, Object> jsondata, HttpServletRequest request,
	 * HttpServletResponse response) { return mbs.saveAmsfForm15Details(jsondata,
	 * request, response); }
	 * 
	 * ////////////////////////////get AMSF 15 form details controller 03rd Aug 2019
	 * ////////////////////////
	 * 
	 * @RequestMapping(value = "/getAmsf15FormDetails", method = RequestMethod.POST,
	 * produces = "application/json", consumes = "application/json") public String
	 * getAmsf15FormDetails(@RequestBody HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getAmsf15FormDetails(jsondata, request, response); }
	 * ////////////////////////////save AMSF 15 CheckList details controller 17 Sep
	 * 2019 ////////////////////////
	 * 
	 * @RequestMapping(value = "/saveAmsf15CheckList", method = RequestMethod.POST,
	 * produces = "application/json", consumes = "application/json") public String
	 * saveAmsf15CheckList(@RequestBody HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.saveAmsf15CheckList(jsondata, request, response); }
	 * 
	 * ////////////////////////////get AMSF 15 CheckList details controller 19 Sep
	 * 2019 ////////////////////////
	 * 
	 * @RequestMapping(value = "/getAmsf15CheckList", method = RequestMethod.POST,
	 * produces = "application/json", consumes = "application/json") public String
	 * getAmsf15CheckList(@RequestBody HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getAmsf15CheckList(jsondata, request, response); }
	 * 
	 * @RequestMapping(value = "/getAllEmployeeCategory", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String getAllEmployeeCategory(HttpServletRequest
	 * request, HttpServletResponse response) { String employeeCategory = "";
	 * employeeCategory = mbs.getAllEmployeeCategory(request, response); return
	 * employeeCategory; }
	 * 
	 * ////////////////////////////save AMSF 15 CheckList details controller 17 Sep
	 * 2019 ////////////////////////
	 * 
	 * 
	 * @RequestMapping(value = "/dataDigitizationSaveAmsfForm15", method =
	 * RequestMethod.POST, produces = "application/json", consumes =
	 * "application/json") public String dataDigitizationSaveAmsfForm15(@RequestBody
	 * HashMap<String, Object> jsondata, HttpServletRequest request,
	 * HttpServletResponse response) { return
	 * mbs.dataDigitizationSaveAmsfForm15(jsondata, request, response); }
	 * 
	 * @RequestMapping(value="/dataDigitizationSaveSpecialistOpinion", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String dataDigitizationSaveSpecialistOpinion(@RequestBody
	 * HashMap<String, Object> jsondata, HttpServletRequest request,
	 * HttpServletResponse response) { return
	 * mbs.dataDigitizationSaveSpecialistOpinion(jsondata, request, response); }
	 * 
	 * @RequestMapping(value = "/dataDigitizationSaveSpecialistOpinionNew", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * dataDigitizationSaveSpecialistOpinionNew(@RequestBody Map<String, Object>
	 * requestData) { Map<String, Object> dataDigitizationSaveSpecialistOpinionNew =
	 * new HashMap<String,Object>(); dataDigitizationSaveSpecialistOpinionNew =
	 * mbs.dataDigitizationSaveSpecialistOpinionNew(requestData); if
	 * (dataDigitizationSaveSpecialistOpinionNew.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(dataDigitizationSaveSpecialistOpinionNew,
	 * HttpStatus.OK); }
	 * 
	 * @RequestMapping(value="/getCategoryDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String getCategoryDetails(@RequestBody HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { return
	 * mbs.getCategoryDetails(jsondata, request, response); }
	 */
}
