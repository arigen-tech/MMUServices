package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface DispensaryService {
	//anita start
		String submitDispenceryIndent(HashMap<String, Object> payload, HttpServletRequest request,
				HttpServletResponse response);

		Map<String, Object> getAllListOfIndentList(HashMap<String, String> payload, HttpServletRequest request,
				HttpServletResponse response);

		Map<String, Object> getIndentListForTracking(HashMap<String, String> payload, HttpServletRequest request,
				HttpServletResponse response);

		Map<String, Object> getIndentDetails(HashMap<String, String> jsondata, HttpServletRequest request,
				HttpServletResponse response);

		Map<String, Object> deleteIndentItems(String jsondata, HttpServletRequest request,
				HttpServletResponse response);

		String updateIndentDispencery(HashMap<String, Object> payload, HttpServletRequest request,
				HttpServletResponse response);
		
		//anita end
		
		/*******************************code by Deepak************************************/
	String getPendingPrescriptionList(Map<String, Object> jsondata);	
	
	String getPrescriptionHeader(Map<String, Object> jsondata);
	
	String getPrescriptionDetail(Map<String, Object> jsondata);
	
	String getBatchDetail(Map<String, Object> jsondata);
	
	String issueMedicineFromDispensary(Map<String, Object> jsondata);
	
	String getPartialWaitingList(Map<String, Object> jsondata);
	
	String getPartialIssueHeader(Map<String, Object> jsondata);
	
	String getPartialIssueDetails(Map<String, Object> jsondata);
	
	String partialIssueMedicineFromDispensary(Map<String, Object> jsondata);
	
	String indentIssueWaitingList(Map<String, Object> jsondata);
	
	String getIndentIssueHeader(Map<String, Object> jsondata);
	
	String getIndentIssueDetails(Map<String, Object> jsondata);
	
	String indentIssue(Map<String, Object> jsondata);
	
	String getIssuNoAndIndentNo(Map<String, Object> jsondata);
	
	String getDrugExpiryList(Map<String, Object> jsondata);
	
	String getROLDataList(Map<String, Object> jsondata);
	
	String getVisitListAndPrescriptionId(Map<String,Object> jsondata);

	String getNisRegisterData(Map<String, Object> payload);

	String getDailyIssueSummaryData(Map<String, Object> payload);

	Map<String, Object> getAvailableStock(String jsondata, HttpServletRequest request, HttpServletResponse response);

	String getDepartmentIdAgainstCode(Map<String, Object> payload);

	String getItemTypeId(Map<String, Object> payload);

	Map<String, Object> getRegisteredPatientList(Map<String, Object> payload);

	Map<String, Object> getRegisteredPatientDetail(Map<String, Object> requestData);

	Map<String, Object> updateRegistrationDetail(Map<String, Object> requestData);

	Map<String, Object> getPendingListForAuditor(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getPendingListForCO(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response);

	String updateIndentDispenceryByAuditor(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	String updateIndentDispenceryByCO(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> displayItemListCO(Map<String, Object> requestData);

	Map<String, Object> forwardToDisctrict(Map<String, Object> requestData);

	Map<String, Object> getPendingListForDO(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> indentValidationDO(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response);
	
	Map<String, Object> updateIndentDispenceryByDO(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> displayItemListDO(Map<String, Object> requestData);

	Map<String, Object> mmuWiseIndentDetail(Map<String, Object> requestData);

	Map<String, Object> submitDoItemsAndGeneratePo(Map<String, Object> requestData);

	Map<String, Object> getCityWiseIndentList(Map<String, Object> requestData);

	Map<String, Object> getMasSupplierList(Map<String, Object> requestData);

	Map<String, Object> getMasSupplierTypeList(Map<String, Object> requestData);

	String getRvWaitingList(Map<String, Object> payload);

	Map<String, Object> rvDetail(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> indentIssueWaitingListForDO(Map<String, Object> payload);

	String submitRvDetailAgainstPo(Map<String, Object> payload);

	String getIndentIssueHeaderDo(Map<String, Object> payload);

	String getIndentIssueDetailsDo(Map<String, Object> payload);

	String indentIssueDo(Map<String, Object> payload);

	Map<String, Object> getIndentListForTrackingCo(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response);
	
	Map<String, Object> getIndentListForTrackingDo(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response);

	String getDrugList(Map<String, Object> payload);

	String updateUnitRate(Map<String, Object> payload);

	Map<String, Object> getIndentDetailsForTracking(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getIndentDetailsForTrackingCo(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getIndentDetailsForTrackingDo(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getMasSupplierListNew(Map<String, Object> requestData);

	Map<String, Object> getIndentDetailsCo(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
}
