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

import com.mmu.services.service.LPProcessService;

@RequestMapping("/lpprocess")
@RestController
@CrossOrigin
public class LPProcessController {
	
	 @Autowired
	 LPProcessService lPProcessService;
	 
	/*
	 * @RequestMapping(value="/submitBudgetary", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitBudgetary(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * lPProcessService.submitBudgetary(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getBudgetaryApprovalList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getBudgetaryApprovalList(@RequestBody HashMap<String, String> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> budgetaryApprovalList = new HashMap<String,Object>();
	 * budgetaryApprovalList=lPProcessService.getAllbudgetaryApprovalList(payload,
	 * request, response); if (budgetaryApprovalList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(budgetaryApprovalList, HttpStatus.OK);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getBudgetaryDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getBudgetaryDetails(@RequestBody
	 * HashMap<String, String> jsondata, HttpServletRequest request,
	 * HttpServletResponse response){ Map<String, Object> budgetaryDetails = new
	 * HashMap<String,Object>();
	 * budgetaryDetails=lPProcessService.getBudgetaryDetails(jsondata, request,
	 * response); if (budgetaryDetails.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(budgetaryDetails, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/deleteBudgetaryItems", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> deleteIndentItems(@RequestBody
	 * String jsondata, HttpServletRequest request, HttpServletResponse response){
	 * System.out.println("deleteBudgetaryItems-----"); Map<String, Object>
	 * budgetaryList = new HashMap<String,Object>();
	 * budgetaryList=lPProcessService.deleteIndentItems(jsondata, request,
	 * response); if (budgetaryList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(budgetaryList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/moBudgetaryApproval", method
	 * =RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String moBudgetaryApproval(@RequestBody HashMap<String,
	 * Object>payload, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * return lPProcessService.moBudgetaryApproval(payload, request, response);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * //get getVender list
	 * 
	 * @RequestMapping(value = "/getVenderList", method = RequestMethod.POST) public
	 * ResponseEntity<Map<Long, String>> getRelationList(@RequestBody
	 * HashMap<String, String> payload) { Map<Long, String> vendorList = new
	 * HashMap<Long,String>(); vendorList = lPProcessService.getVendorList(payload);
	 * if (vendorList.isEmpty()) { return new ResponseEntity<Map<Long,
	 * String>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<Long,
	 * String>>(vendorList, HttpStatus.OK); }
	 * 
	 * 
	 * //submit Budgetary Quotation
	 * 
	 * 
	 * @RequestMapping(value="/submitQuotation", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitQuotation(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * lPProcessService.submitBudgetaryQuotation(payload, request, response);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getQuotationForApprovalList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getQuotationForApprovalList(@RequestBody HashMap<String, String> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> quotationApprovalList = new HashMap<String,Object>();
	 * quotationApprovalList=lPProcessService.getQuotationForApprovalList(payload,
	 * request, response); if (quotationApprovalList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(quotationApprovalList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getNameByServiceNo", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getNameByServiceNo(@RequestBody
	 * String jsondata, HttpServletRequest request, HttpServletResponse response){
	 * System.out.println("getNameByServiceNo-----"); Map<String, Object>
	 * responseList = new HashMap<String,Object>();
	 * responseList=lPProcessService.getNameByServiceNo(jsondata, request,
	 * response); if (responseList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/submitCtmForLP", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitCtmForLP(@RequestBody HashMap<String, Object> payload,
	 * HttpServletRequest request, HttpServletResponse response) { return
	 * lPProcessService.submitCtmForLP(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getAllCtmCommittee", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getAllCtmCommittee(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> ctmMemberList = new HashMap<String,Object>();
	 * ctmMemberList=lPProcessService.getCtmMemberList(payload, request, response);
	 * if (ctmMemberList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(ctmMemberList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @RequestMapping(value="/inactivatectmCommittee", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * inactivatectmCommittee(@RequestBody HashMap<String, String> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> responseList = new HashMap<String,Object>();
	 * responseList=lPProcessService.inactivatectmCommittee(payload, request,
	 * response); if (responseList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @RequestMapping(value="/getItemList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getItemList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> itemList = new HashMap<String,Object>();
	 * itemList=lPProcessService.getItemList(payload, request, response); if
	 * (itemList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(itemList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getCommitteeMember", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getCommitteeMember(@RequestBody
	 * String jsondata, HttpServletRequest request, HttpServletResponse response){
	 * System.out.println("getCommitteeMember-----"); Map<String, Object>
	 * responseList = new HashMap<String,Object>();
	 * responseList=lPProcessService.getCommitteeMember(jsondata, request,
	 * response); if (responseList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/approveQuotationPresident", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String approveQuotationPresident(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * lPProcessService.approveQuotationPresident(payload, request, response);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getItemListForSanctionOrder", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getItemListForSupplyOrder(@RequestBody HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response){ Map<String,
	 * Object> itemDetailsDetails = new HashMap<String,Object>();
	 * itemDetailsDetails=lPProcessService.getItemListForSupplyOrder(jsondata,
	 * request, response); if (itemDetailsDetails.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(itemDetailsDetails, HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value="/submitSanctionOrder", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitSanctionOrder(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * lPProcessService.submitSanctionOrder(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getSanctionListForApproval", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * getSanctionListForApproval(@RequestBody HashMap<String, String> payload,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * Map<String, Object> sanctionApprovalList = new HashMap<String,Object>();
	 * sanctionApprovalList=lPProcessService.getSanctionApprovalList(payload,
	 * request, response); if (sanctionApprovalList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(sanctionApprovalList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/approveSanctionOrderDetails", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>>
	 * approveSanctionOrderDetails(@RequestBody HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response){ Map<String,
	 * Object> getSanctionData = new HashMap<String,Object>();
	 * getSanctionData=lPProcessService.getSanctionData(jsondata, request,
	 * response); if (getSanctionData.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(getSanctionData, HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value="/approveSanctionOrder", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String approveSanctionOrder(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * lPProcessService.approveSanctionOrder(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/getLpRateByItemId", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getLpRateByItemId(@RequestBody
	 * String jsondata, HttpServletRequest request, HttpServletResponse response){
	 * System.out.println("getLpRateByItemId-----"); Map<String, Object>
	 * responseList = new HashMap<String,Object>();
	 * responseList=lPProcessService.getLpRateByItemId(jsondata, request, response);
	 * if (responseList.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(responseList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * //get financialYearList
	 * 
	 * @RequestMapping(value = "/financialYearList", method = RequestMethod.POST)
	 * public ResponseEntity<Map<Long, String>> financialYearList(@RequestBody
	 * HashMap<String, String> payload) { Map<Long, String> financialYearList = new
	 * HashMap<Long,String>(); financialYearList =
	 * lPProcessService.financialYearList(); if (financialYearList.isEmpty()) {
	 * return new ResponseEntity<Map<Long, String>>(HttpStatus.NO_CONTENT); } return
	 * new ResponseEntity<Map<Long, String>>(financialYearList, HttpStatus.OK); }
	 * 
	 * 
	 * @RequestMapping(value="/submitFundUtilization", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public String submitFundUtilization(@RequestBody HashMap<String, Object>
	 * payload, HttpServletRequest request, HttpServletResponse response) { return
	 * lPProcessService.submitFundUtilization(payload, request, response);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @RequestMapping(value="/fundUtilizationList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> fundUtilizationList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> fundUtilizationList = new HashMap<String,Object>();
	 * fundUtilizationList=lPProcessService.fundUtilizationList(payload, request,
	 * response); if (fundUtilizationList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(fundUtilizationList, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/getFundUtilizationInShownYear", method =
	 * RequestMethod.POST) public ResponseEntity<Map<String, Object>>
	 * getFundUtilizationInShownYear(@RequestBody Map<String, String> requestData) {
	 * Map<String, Object> fundUtilizationData = new HashMap<String, Object>();
	 * fundUtilizationData =
	 * lPProcessService.getFundUtilizationInShownYear(requestData); if
	 * (fundUtilizationData.isEmpty()) { return new ResponseEntity<Map<String,
	 * Object>>(HttpStatus.NO_CONTENT); } return new ResponseEntity<Map<String,
	 * Object>>(fundUtilizationData, HttpStatus.OK); }
	 * 
	 * //Get anction List
	 * 
	 * @RequestMapping(value="/getSanctionList", method =
	 * RequestMethod.POST,produces="application/json",consumes="application/json")
	 * public ResponseEntity<Map<String, Object>> getSanctionList(@RequestBody
	 * HashMap<String, String> payload, HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * Map<String, Object> sanctionApprovalList = new HashMap<String,Object>();
	 * sanctionApprovalList=lPProcessService.getSanctionList(payload, request,
	 * response); if (sanctionApprovalList.isEmpty()) { return new
	 * ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT); } return new
	 * ResponseEntity<Map<String, Object>>(sanctionApprovalList, HttpStatus.OK);
	 * 
	 * }
	 */
	}
