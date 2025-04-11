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

import com.mmu.services.service.MIAdminService;


@RequestMapping("/miAdmin")
@RestController
@CrossOrigin
public class MIAdminController {
	 @Autowired
	 MIAdminService mIAdminService;
	 
	/*
	 * @RequestMapping(value="/getDisposalTypeList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getDisposalTypeList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> disposalTypeList = new HashMap<String,Object>();
	 * disposalTypeList=mIAdminService.getAllDisposalTypeList(payload, request,
	 * response); if (disposalTypeList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(disposalTypeList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getAttCTypeReportList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getAttCTypeReportList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> attCTypeReportList = new HashMap<String,Object>();
	 * attCTypeReportList=mIAdminService.getAllAttCTypeReportList(payload, request,
	 * response); if (attCTypeReportList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(attCTypeReportList, HttpStatus.OK);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * //getEmployeeCategory
	 * 
	 * @RequestMapping(value = "/getEmployeeCategory", method = RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>> getEmployeeCategory() {
	 * Map<String, Object> employeeCategory = new HashMap<String, Object>();
	 * employeeCategory = mIAdminService.getEmployeeCategory(); if
	 * (employeeCategory.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(employeeCategory, HttpStatus.OK); }
	 * 
	 * 
	 * //getMedCategory
	 * 
	 * @RequestMapping(value = "/getMedCategory", method = RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>> getMedCategory() { Map<String,
	 * Object> medCategory = new HashMap<String, Object>(); medCategory =
	 * mIAdminService.getMedCategory(); if (medCategory.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(medCategory, HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value="/getAmeReportList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getAmeReportList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> ameReportList = new HashMap<String,Object>();
	 * ameReportList=mIAdminService.getAllAReportList(payload, request, response);
	 * if (ameReportList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(ameReportList, HttpStatus.OK);
	 * 
	 * 
	 * } //Medical Exam statistics
	 * 
	 * @RequestMapping(value="/getMasMedicalStatistics", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getMasMedicalStatistics(@RequestBody HashMap<String, String> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> masMedicalStatisticsList = new HashMap<String,Object>();
	 * masMedicalStatisticsList=mIAdminService.masMedicalStatisticsList(payload,
	 * request, response); if (masMedicalStatisticsList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(masMedicalStatisticsList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * //Blood Group Register
	 * 
	 * @RequestMapping(value = "/getBloodGroup", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>> getBloodGroup() { Map<String, Object>
	 * getBloodGroup = new HashMap<String, Object>(); getBloodGroup =
	 * mIAdminService.getBloodGroup(); if (getBloodGroup.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(getBloodGroup, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value="/getBloodGroupRegister", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getBloodGroupRegister(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> getBloodGroupRegister = new HashMap<String,Object>();
	 * getBloodGroupRegister=mIAdminService.getBloodGroupRegister(payload, request,
	 * response); if (getBloodGroupRegister.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(getBloodGroupRegister, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * //Get getUnitList
	 * 
	 * @RequestMapping(value = "/getUnitList", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>> getUnitList() { Map<String, Object>
	 * getUnitList = new HashMap<String, Object>(); getUnitList =
	 * mIAdminService.getUnitList(); if (getUnitList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(getUnitList, HttpStatus.OK); } //Submit
	 * Sanitary Diary
	 * 
	 * @RequestMapping(value="/submitSanitaryDiary", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitSanitaryDiary(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitSanitary(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getSanitaryReportList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getSanitaryReportList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> sanitaryReportList = new HashMap<String,Object>();
	 * sanitaryReportList=mIAdminService.getSanitaryReportList(payload, request,
	 * response); if (sanitaryReportList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(sanitaryReportList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getPatientDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getPatientDetails(@RequestBody
	 * String jsondata, HttpServletRequest request, HttpServletResponse response){
	 * System.out.println("getPatientDetails-----"); Map<String, Object>
	 * responseList = new HashMap<String,Object>();
	 * responseList=mIAdminService.getNameByServiceNo(jsondata, request, response);
	 * if (responseList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * //Submit Injury Report
	 * 
	 * @RequestMapping(value="/submitInjuryRegister", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitInjuryRegister(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitInjuryRegister(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getInjuryRegister", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getInjuryRegister(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> injuryRegisterList = new HashMap<String,Object>();
	 * injuryRegisterList=mIAdminService.getSanitaryReportList(payload, request,
	 * response); if (injuryRegisterList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(injuryRegisterList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getInjuryReportList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getInjuryReportList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> injuryReportList = new HashMap<String,Object>();
	 * injuryReportList=mIAdminService.getInjuryReportList(payload, request,
	 * response); if (injuryReportList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(injuryReportList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * //Submit Hospital Visit Register
	 * 
	 * @RequestMapping(value="/submitHospitalVisitRegister", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitHospitalVisitRegister(@RequestBody HashMap<String,
	 * Object> payload, HttpServletRequest request, HttpServletResponse response) {
	 * return mIAdminService.submitHospitalVisitRegister(payload, request,
	 * response);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getHospitalVisitList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getHospitalVisitList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> hospitalVisitList = new HashMap<String,Object>();
	 * hospitalVisitList=mIAdminService.getHospitalVisitList(payload, request,
	 * response); if (hospitalVisitList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(hospitalVisitList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * //Submit HIV/STD REGISTER
	 * 
	 * @RequestMapping(value="/submitHivStdRegister", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitHivStdRegister(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitHivStdRegister(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getHivStdList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getHivStdList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> hivStdList = new HashMap<String,Object>();
	 * hivStdList=mIAdminService.getHivStdList(payload, request, response); if
	 * (hivStdList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(hivStdList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * // Submit Milk Testing
	 * 
	 * @RequestMapping(value="/submitMilkTesting", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitMilkTesting(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitMilkTesting(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getMilkTestingList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getMilkTestingList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> milkTestingList = new HashMap<String,Object>();
	 * milkTestingList=mIAdminService.getMilkTestingList(payload, request,
	 * response); if (milkTestingList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(milkTestingList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * // Submit Water Testing
	 * 
	 * @RequestMapping(value="/submitWaterTesting", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitWaterTesting(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitWaterTesting(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getWaterTestingList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getWaterTestingList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> milkTestingList = new HashMap<String,Object>();
	 * milkTestingList=mIAdminService.getWaterTestingList(payload, request,
	 * response); if (milkTestingList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(milkTestingList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value = "/getEmpList", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>> getEmpList(@RequestBody HashMap<String,
	 * String> payload, HttpServletRequest request, HttpServletResponse response) {
	 * Map<String, Object> empList = new HashMap<String, Object>(); empList =
	 * mIAdminService.getEmpList(payload); if (empList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(empList, HttpStatus.OK); }
	 * 
	 * @RequestMapping(value="/dangerousDrugRegisterList1", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * dangerousDrugRegisterList1(@RequestBody HashMap<String, String> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> dangerousDrugRegisterList = new HashMap<String,Object>();
	 * dangerousDrugRegisterList=mIAdminService.getDangerousDrugRegisterList(
	 * payload, request, response); if (dangerousDrugRegisterList.isEmpty()) {
	 * return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); }
	 * return new ResponseEntity<Map<String, Object>>(dangerousDrugRegisterList,
	 * HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/dangerousDrugRegisterList",
	 * method=RequestMethod.POST, produces="application/json",
	 * consumes="application/json") public String
	 * dangerousDrugRegisterList(@RequestBody HashMap<String, String> map,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.getDangerousDrugRegisterList1(map,request,response); }
	 * 
	 * @RequestMapping(value = "/getManufactureList", method = RequestMethod.POST)
	 * public ResponseEntity<Map<String, Object>> getManufactureList(@RequestBody
	 * Map<String, String> requestData) { Map<String, Object> getManufactureList =
	 * new HashMap<String,Object>(); getManufactureList =
	 * mIAdminService.getManufactureList(requestData); if
	 * (getManufactureList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT);
	 * 
	 * }
	 * 
	 * return new ResponseEntity<Map<String, Object>>(getManufactureList,
	 * HttpStatus.OK); } // submitEquipmentDetails
	 * 
	 * @RequestMapping(value="/submitEquipmentDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitEquipmentDetails(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitEquipmentDetails(payload, request, response);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getEquipmentDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getEquipmentDetails(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> equipmentDetails = new HashMap<String,Object>();
	 * equipmentDetails=mIAdminService.getEquipmentDetails(payload, request,
	 * response); if (equipmentDetails.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(equipmentDetails, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * //submitWarrantydetails
	 * 
	 * @RequestMapping(value="/submitWarrantydetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitWarrantydetails(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitWarrantydetails(payload, request, response);
	 * 
	 * } // submitAccessarydetails
	 * 
	 * @RequestMapping(value="/submitAccessarydetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitAccessarydetails(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitAccessarydetails(payload, request, response);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getEquipmentDetailsByItemId", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getEquipmentDetailsByItemId(@RequestBody String jsondata, HttpServletRequest
	 * request, HttpServletResponse response){
	 * System.out.println("getEquipmentDetailsByItemId-----"); Map<String, Object>
	 * responseList = new HashMap<String,Object>();
	 * responseList=mIAdminService.getEquipmentDetailsByItemId(jsondata, request,
	 * response); if (responseList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getEquipmentReportList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getEquipmentReportList(@RequestBody HashMap<String, String> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> responseList = new HashMap<String,Object>();
	 * responseList=mIAdminService.getEquipmentReportList(payload, request,
	 * response); if (responseList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getEquipmentDetailsForstoreLedger", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getEquipmentDetailsForstoreLedger(@RequestBody HashMap<String, String>
	 * payload, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> equipmentDetails = new HashMap<String,Object>();
	 * equipmentDetails=mIAdminService.getEquipmentDetailsForstoreLedger(payload,
	 * request, response); if (equipmentDetails.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(equipmentDetails, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * // submitBoardOutDetails
	 * 
	 * @RequestMapping(value="/submitBoardOutDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitBoardOutDetails(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitBoardOutDetails(payload, request, response);
	 * 
	 * } // submitAuditDetails
	 * 
	 * @RequestMapping(value="/submitAuditDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitAuditDetails(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitAuditDetails(payload, request, response);
	 * 
	 * }
	 * 
	 * //disposalList
	 * 
	 * @RequestMapping(value = "/disposalList", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>> disposalList() { Map<String, Object>
	 * disposalList = new HashMap<String, Object>(); disposalList =
	 * mIAdminService.getDisposalList(); if (disposalList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(disposalList, HttpStatus.OK); }
	 * 
	 * //getEmpDetails
	 * 
	 * @RequestMapping(value="/getEmpDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getEmpDetails(@RequestBody String
	 * jsondata, HttpServletRequest request, HttpServletResponse response){
	 * System.out.println("getEmpDetails-----"); Map<String, Object> responseList =
	 * new HashMap<String,Object>();
	 * responseList=mIAdminService.getEmpDetailsByServiceNo(jsondata, request,
	 * response); if (responseList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * // submitBloodGroupRegister
	 * 
	 * @RequestMapping(value="/submitBloodGroupRegister", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitBloodGroupRegister(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * mIAdminService.submitBloodGroupRegister(payload, request, response);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getMedicalCategory", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getMedicalCategory(@RequestBody
	 * String jsondata, HttpServletRequest request, HttpServletResponse response){
	 * System.out.println("getMedicalCategory-----"); Map<String, Object>
	 * responseList = new HashMap<String,Object>();
	 * responseList=mIAdminService.getMedicalCategory(jsondata, request, response);
	 * if (responseList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * //getRankList
	 * 
	 * @RequestMapping(value = "/getRankList", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, Object>> getRankList() { Map<String, Object>
	 * getRankList = new HashMap<String, Object>(); getRankList =
	 * mIAdminService.getRankList(); if (getRankList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(getRankList, HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value="/getDetailsForHospitalRegister", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getDetailsForHospitalRegister(@RequestBody HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response){
	 * 
	 * Map<String, Object> detailsOfHospitalVisitList = new
	 * HashMap<String,Object>();
	 * detailsOfHospitalVisitList=mIAdminService.getDetailsForHospitalRegister(
	 * jsondata, request, response); if (detailsOfHospitalVisitList.isEmpty()) {
	 * return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); }
	 * return new ResponseEntity<Map<String, Object>>(detailsOfHospitalVisitList,
	 * HttpStatus.OK);
	 * 
	 * }
	 */
						
}
