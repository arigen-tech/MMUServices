package com.mmu.services.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface AuditService {

    String captureInspectionChecklistDetails(JSONObject jsondata);

    String captureEquipmentChecklistDetails(JSONObject jsondata);

    String getCampLocation(JSONObject jsondata);

    String getAllCapturedEquipmentChecklist(JSONObject jsondata);

    String getAllCapturedInspectionChecklist(JSONObject jsondata);

    String getCapturedInspectionChecklist(JSONObject jsondata);

    String addInspectionChecklistValidationHistory(JSONObject jsondata);

    String getAllInspectionChecklistValidationHistory(JSONObject jsondata);

    String updateInspectionChecklistValidationHistory(JSONObject jsondata);

    String updateInspectionChecklistAuditStatus(JSONObject jsondata);

    String getCapturedEquipmentChecklist(JSONObject jsondata);

    String addEquipmentChecklistValidationHistory(JSONObject jsondata);

    String getAllEquipmentChecklistValidationHistory(JSONObject jsondata);

    String updateEquipmentChecklistValidationHistory(JSONObject jsondata);

    String updateEquipmentChecklistAuditStatus(JSONObject jsondata);

    String getEquipmentPenaltyList(JSONObject jsondata);

    String getInspectionPenaltyList(JSONObject jsondata);

    String getVendors(JSONObject jsondata);

    String captureVendorBillDetail(JSONObject jsondata);

    String getCapturedVendorBillDetail(JSONObject jsondata);

    String getVendorsMMUAndCity(JSONObject jsondata);

    String getVendorsPenalty(JSONObject jsondata);

    String updateVendorBillRemarks(JSONObject jsondata);

	String getVendorInvoiceApprovalDetail(JSONObject jsonObject);
	
	String saveOrUpdateAuthorityVendorBillDetails(JSONObject jsondata,HttpServletRequest request,
			HttpServletResponse response);

	String getVendorInvoicePaymentDetail(JSONObject jsonObject);

	String saveFundAllocationDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getFundAllocationHdDetails(JSONObject jsondata);

	Long getFundAvailableBalance(JSONObject jsonObject);

	String getDgFundAllcationHdDt(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String deleteFundAllocationDtDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getFundAllocationAmount(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	Long getPenaltyAuthorityDetailsByUpss(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response);

	String saveCaptureInterestDetails(HashMap<String, Object> getJsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getAllCaptureInterestDetails(JSONObject jsonObject);

	String getCaptureInterestHdDt(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String deleteCaptureInterestDtDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String saveUCDocumentUploadDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getUcUploadDocumentDetail(JSONObject jsonObject);

	String getUcUploadDocumentHdDt(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String deleteUCUploadDtDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String deleteDocument(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);

	String getPenaltyRegister(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

}
