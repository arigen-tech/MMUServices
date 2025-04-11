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

import com.mmu.services.service.DispensaryService;


@RequestMapping("/dispencery")
@RestController
@CrossOrigin
public class DispensaryController {
	
    @Autowired
	DispensaryService dispenceryService;
	
//-------------------------srtart(Anita) Create Indent Code From here------------------------------------------//	
	
    @RequestMapping(value="/submitDispenceryIndent", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String submitDispenceryIndent(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return dispenceryService.submitDispenceryIndent(payload, request, response);
		
		
	}
	

	@RequestMapping(value="/getAllListOfIndentList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  getAllListOfIndentList(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.getAllListOfIndentList(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/getPendingListForAuditor", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  getPendingListForAuditor(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.getPendingListForAuditor(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getPendingListForCO", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  getPendingListForCO(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.getPendingListForCO(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/getIndentListForTracking", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  getIndentListForTracking(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.getIndentListForTracking(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
		
		
	}
	
	@RequestMapping(value="/getIndentListForTrackingCo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  getIndentListForTrackingCo(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.getIndentListForTrackingCo(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
		
		
	}
	
	@RequestMapping(value="/getIndentListForTrackingDo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  getIndentListForTrackingDo(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.getIndentListForTrackingDo(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
		
		
	}
	
	@RequestMapping(value="/getIndentDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public  ResponseEntity<Map<String, Object>> getIndentDetails(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response){
	Map<String, Object> indentList = new HashMap<String,Object>();
	indentList=dispenceryService.getIndentDetails(jsondata, request, response);
	if (indentList.isEmpty()) {
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	
	}
	
	@RequestMapping(value="/getIndentDetailsCo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public  ResponseEntity<Map<String, Object>> getIndentDetailsCo(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response){
	Map<String, Object> indentList = new HashMap<String,Object>();
	indentList=dispenceryService.getIndentDetailsCo(jsondata, request, response);
	if (indentList.isEmpty()) {
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	
	}
	
	
	@RequestMapping(value="/deleteIndentItems", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public  ResponseEntity<Map<String, Object>> deleteIndentItems(@RequestBody String jsondata, HttpServletRequest request,
			HttpServletResponse response){
	Map<String, Object> indentList = new HashMap<String,Object>();
	indentList=dispenceryService.deleteIndentItems(jsondata, request, response);
	if (indentList.isEmpty()) {
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	
	}
	
	
	@RequestMapping(value="/updateIndentDispencery", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateIndentDispencery(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return dispenceryService.updateIndentDispencery(payload, request, response);
	}
	
	@RequestMapping(value="/updateIndentDispenceryByAuditor", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateIndentDispenceryByAuditor(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return dispenceryService.updateIndentDispenceryByAuditor(payload, request, response);
	}
	
	@RequestMapping(value="/updateIndentDispenceryByCo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateIndentDispenceryByCo(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return dispenceryService.updateIndentDispenceryByCO(payload, request, response);
	}
	

	//code by Deepak	
	@RequestMapping(value="/getPendingPrescriptionList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPendingPrescriptionList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getPendingPrescriptionList(payload);
		
	}
	
	@RequestMapping(value="/getPrescriptionHeader", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPrescriptionHeader(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getPrescriptionHeader(payload);
		
	}
	
	@RequestMapping(value="/getPrescriptionDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPrescriptionDetail(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getPrescriptionDetail(payload);
		
	}
	
	@RequestMapping(value="/getBatchDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getBatchDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getBatchDetail(payload);
		
	}
	
	@RequestMapping(value="/issueMedicineFromDispensary", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String issueMedicineFromDispensary(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String data = dispenceryService.issueMedicineFromDispensary(payload);
		return data;
	}
	
	@RequestMapping(value="/getPartialWaitingList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPartialWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getPartialWaitingList(payload);
		
	}
	
	@RequestMapping(value="/getPartialIssueHeader", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPartialIssueHeader(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getPartialIssueHeader(payload);
		
	}
	
	@RequestMapping(value="/getPartialIssueDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPartialIssueDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getPartialIssueDetails(payload);
		
	}
	
	@RequestMapping(value="/partialIssueMedicineFromDispensary", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String partialIssueMedicineFromDispensary(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String data = dispenceryService.partialIssueMedicineFromDispensary(payload);
		return data;
	}
	
	
	@RequestMapping(value="/indentIssueWaitingList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String indentIssueWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.indentIssueWaitingList(payload);		
	}
	
	@RequestMapping(value="/getIndentIssueHeader", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getIndentIssueHeader(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getIndentIssueHeader(payload);
		
	}
	
	@RequestMapping(value="/getIndentIssueDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getIndentIssueDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getIndentIssueDetails(payload);
		
	}
	
	@RequestMapping(value="/indentIssue", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String indentIssue(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String data = dispenceryService.indentIssue(payload);
		return data;
	}
	
	@RequestMapping(value="/getIssuNoAndIndentNo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getIssuNoAndIndentNo(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String data = dispenceryService.getIssuNoAndIndentNo(payload);
		return data;
	}	
	
	@RequestMapping(value="/getDrugExpiryList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getDrugExpiryList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getDrugExpiryList(payload);		
	}	
	
	@RequestMapping(value="/getROLDataList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getROLDataList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getROLDataList(payload);		
	}
	
	@RequestMapping(value="/getVisitListAndPrescriptionId", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getVisitListAndPrescriptionId(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getVisitListAndPrescriptionId(payload);		
	}
	
	@RequestMapping(value="/getNisRegisterData", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getNisRegisterData(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getNisRegisterData(payload);		
	}
	
	@RequestMapping(value="/getDailyIssueSummaryData", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getDailyIssueSummaryData(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getDailyIssueSummaryData(payload);		
	}
	
	
	@RequestMapping(value="/getAvailableStock", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public  ResponseEntity<Map<String, Object>> getAvailableStock(@RequestBody String jsondata, HttpServletRequest request,
			HttpServletResponse response){
	Map<String, Object> responseList = new HashMap<String,Object>();
	responseList=dispenceryService.getAvailableStock(jsondata, request, response);
	if (responseList.isEmpty()) {
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<Map<String, Object>>(responseList, HttpStatus.OK);
	
	}
	
	@RequestMapping(value="/getDepartmentIdAgainstCode", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getDepartmentIdAgainstCode(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getDepartmentIdAgainstCode(payload);	
	}
	
	@RequestMapping(value="/getItemTypeId", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getItemTypeId(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getItemTypeId(payload);	
	}
	
	@RequestMapping(value="/getRegisteredPatientList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> getOnlinePatientList(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.getRegisteredPatientList(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/getRegisteredPatientDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> getRegisteredPatientDetail(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.getRegisteredPatientDetail(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateRegistrationDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> updateRegistrationDetail(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.updateRegistrationDetail(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/displayItemListCO", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> displayItemListCO(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.displayItemListCO(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/forwardToDisctrict", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> forwardToDisctrict(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.forwardToDisctrict(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getPendingListForDO", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  getPendingListForDO(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.getPendingListForDO(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/indentValidationDO", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  indentValidationDO(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.indentValidationDO(payload, request, response);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateIndentDispenceryByDO", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> updateIndentDispenceryByDO(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = dispenceryService.updateIndentDispenceryByDO(payload, request, response);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@RequestMapping(value="/displayItemListDO", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> displayItemListDO(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.displayItemListDO(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getMmuWiseIndentList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> mmuWiseIndentDetail(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.mmuWiseIndentDetail(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/submitDoItemsAndGeneratePo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> submitDoItemsAndGeneratePo(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.submitDoItemsAndGeneratePo(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getCityWiseIndentList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> getCityWiseIndentList(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.getCityWiseIndentList(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getMasSupplierList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> getMasSupplierList(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.getMasSupplierList(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getMasSupplierListNew", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> getMasSupplierListNew(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.getMasSupplierListNew(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getMasSupplierTypeList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> getMasSupplierTypeList(@RequestBody Map<String, Object> requestData){
		Map<String, Object> response = new HashMap<String, Object>();
		response = dispenceryService.getMasSupplierTypeList(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getRvWaitingList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getRvWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getRvWaitingList(payload);
		
	}
	
	@RequestMapping(value="/rvDetail", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>>  rvDetail(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> reDetail = new HashMap<String,Object>();
		reDetail=dispenceryService.rvDetail(payload, request, response);
		if (reDetail.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(reDetail, HttpStatus.OK);
	}
	
	@RequestMapping(value="/indentIssueWaitingListForDO", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public ResponseEntity<Map<String, Object>> indentIssueWaitingListForDO(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> indentList = new HashMap<String,Object>();
		indentList=dispenceryService.indentIssueWaitingListForDO(payload);
		if (indentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	}
	
	//submitRvDetailAgainstPo
	@RequestMapping(value="/submitRvDetailAgainstPo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String submitRvDetailAgainstPo(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String data = dispenceryService.submitRvDetailAgainstPo(payload);
		return data;
	}
	
	@RequestMapping(value="/getIndentIssueHeaderDo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getIndentIssueHeaderDo(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getIndentIssueHeaderDo(payload);
		
	}
	
	@RequestMapping(value="/getIndentIssueDetailsDo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getIndentIssueDetailsDo(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getIndentIssueDetailsDo(payload);
		
	}
	
	@RequestMapping(value="/indentIssueDo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String indentIssueDo(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String data = dispenceryService.indentIssueDo(payload);
			return data;
	}
	
	@RequestMapping(value="/getDrugList", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getDrugList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		return 	dispenceryService.getDrugList(payload);
		
	}
	
	@RequestMapping(value="/updateUnitRate", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String updateUnitRate(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String data = dispenceryService.updateUnitRate(payload);
		return data;
	}
	
	@RequestMapping(value="/getIndentDetailsForTracking", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public  ResponseEntity<Map<String, Object>> getIndentDetailsForTracking(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response){
	Map<String, Object> indentList = new HashMap<String,Object>();
	indentList=dispenceryService.getIndentDetailsForTracking(jsondata, request, response);
	if (indentList.isEmpty()) {
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	
	}
	
	@RequestMapping(value="/getIndentDetailsForTrackingCo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public  ResponseEntity<Map<String, Object>> getIndentDetailsForTrackingCo(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response){
	Map<String, Object> indentList = new HashMap<String,Object>();
	indentList=dispenceryService.getIndentDetailsForTrackingCo(jsondata, request, response);
	if (indentList.isEmpty()) {
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	
	}
	
	@RequestMapping(value="/getIndentDetailsForTrackingDo", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public  ResponseEntity<Map<String, Object>> getIndentDetailsForTrackingDo(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response){
	Map<String, Object> indentList = new HashMap<String,Object>();
	indentList=dispenceryService.getIndentDetailsForTrackingDo(jsondata, request, response);
	if (indentList.isEmpty()) {
		return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<Map<String, Object>>(indentList, HttpStatus.OK);
	
	}
	
	
	
}
