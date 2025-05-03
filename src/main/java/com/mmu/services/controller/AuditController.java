package com.mmu.services.controller;

import com.mmu.services.service.AuditService;
import com.mmu.services.service.DashBoardService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/audit")
@RestController
@CrossOrigin
public class AuditController {

    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/captureInspectionChecklistDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String updateInspectionChecklist(@RequestBody HashMap<String, Object> payload) {
        return auditService.captureInspectionChecklistDetails(new JSONObject(payload));
    }

    @RequestMapping(value = "/captureEquipmentChecklistDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String captureEquipmentChecklistDetails(@RequestBody HashMap<String, Object> payload) {
        return auditService.captureEquipmentChecklistDetails(new JSONObject(payload));
    }

    @RequestMapping(value = "/getCampLocation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getCampLocation(@RequestBody HashMap<String, Object> payload) {
        return auditService.getCampLocation(new JSONObject(payload));
    }

    @RequestMapping(value = "/getAllCapturedEquipmentChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getAllCapturedEquipmentChecklist(@RequestBody HashMap<String, Object> payload) {
        return auditService.getAllCapturedEquipmentChecklist(new JSONObject(payload));
    }

    @RequestMapping(value = "/getAllCapturedInspectionChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getAllCapturedInspectionChecklist(@RequestBody HashMap<String, Object> payload) {
        return auditService.getAllCapturedInspectionChecklist(new JSONObject(payload));
    }

    @RequestMapping(value = "/getCapturedInspectionChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getCapturedInspectionChecklist(@RequestBody HashMap<String, Object> payload) {
        return auditService.getCapturedInspectionChecklist(new JSONObject(payload));
    }

    @RequestMapping(value = "/addInspectionChecklistValidationHistory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String addInspectionChecklistValidationHistory(@RequestBody HashMap<String, Object> payload) {
        return auditService.addInspectionChecklistValidationHistory(new JSONObject(payload));
    }

    @RequestMapping(value = "/getAllInspectionChecklistValidationHistory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getAllInspectionChecklistValidationHistory(@RequestBody HashMap<String, Object> payload) {
        return auditService.getAllInspectionChecklistValidationHistory(new JSONObject(payload));
    }

    @RequestMapping(value = "/updateInspectionChecklistValidationHistory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String updateInspectionChecklistValidationHistory(@RequestBody HashMap<String, Object> payload) {
        return auditService.updateInspectionChecklistValidationHistory(new JSONObject(payload));
    }

    @RequestMapping(value = "/updateInspectionChecklistAuditStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String updateInspectionChecklistAuditStatus(@RequestBody HashMap<String, Object> payload) {
        return auditService.updateInspectionChecklistAuditStatus(new JSONObject(payload));
    }

    @RequestMapping(value = "/getCapturedEquipmentChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getCapturedEquipmentChecklist(@RequestBody HashMap<String, Object> payload) {
        return auditService.getCapturedEquipmentChecklist(new JSONObject(payload));
    }

    @RequestMapping(value = "/addEquipmentChecklistValidationHistory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String addEquipmentChecklistValidationHistory(@RequestBody HashMap<String, Object> payload) {
        return auditService.addEquipmentChecklistValidationHistory(new JSONObject(payload));
    }

    @RequestMapping(value = "/getAllEquipmentChecklistValidationHistory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getAllEquipmentChecklistValidationHistory(@RequestBody HashMap<String, Object> payload) {
        return auditService.getAllEquipmentChecklistValidationHistory(new JSONObject(payload));
    }

    @RequestMapping(value = "/updateEquipmentChecklistValidationHistory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String updateEquipmentChecklistValidationHistory(@RequestBody HashMap<String, Object> payload) {
        return auditService.updateEquipmentChecklistValidationHistory(new JSONObject(payload));
    }

    @RequestMapping(value = "/updateEquipmentChecklistAuditStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String updateEquipmentChecklistAuditStatus(@RequestBody HashMap<String, Object> payload) {
        return auditService.updateEquipmentChecklistAuditStatus(new JSONObject(payload));
    }

    @RequestMapping(value = "/getEquipmentPenaltyList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getEquipmentPenaltyList(@RequestBody HashMap<String, Object> payload) {
        return auditService.getEquipmentPenaltyList(new JSONObject(payload));
    }

    @RequestMapping(value = "/getInspectionPenaltyList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getInspectionPenaltyList(@RequestBody HashMap<String, Object> payload) {
        return auditService.getInspectionPenaltyList(new JSONObject(payload));
    }

    @RequestMapping(value = "/getVendors", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getVendors(@RequestBody HashMap<String, Object> payload) {
        return auditService.getVendors(new JSONObject(payload));
    }

    @RequestMapping(value = "/captureVendorBillDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String captureVendorBillDetail(@RequestBody HashMap<String, Object> payload) {
        return auditService.captureVendorBillDetail(new JSONObject(payload));
    }

    @RequestMapping(value = "/getCapturedVendorBillDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getAllCapturedVendorBillDetail(@RequestBody HashMap<String, Object> payload) {
        return auditService.getCapturedVendorBillDetail(new JSONObject(payload));
    }

    @RequestMapping(value = "/getVendorsMMUAndCity", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getVendorsMMUAndCity(@RequestBody HashMap<String, Object> payload) {
        return auditService.getVendorsMMUAndCity(new JSONObject(payload));
    }

    @RequestMapping(value = "/getVendorsPenalty", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getVendorsPenalty(@RequestBody HashMap<String, Object> payload) {
        return auditService.getVendorsPenalty(new JSONObject(payload));
    }

    @RequestMapping(value = "/updateVendorBillRemarks", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String updateVendorBillAuditorRemarks(@RequestBody HashMap<String, Object> payload) {
        return auditService.updateVendorBillRemarks(new JSONObject(payload));
    }
    
    @RequestMapping(value = "/getVendorInvoiceApprovalDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getVendorInvoiceApprovalDetail(@RequestBody HashMap<String, Object> payload) {
        return auditService.getVendorInvoiceApprovalDetail(new JSONObject(payload));
    }
    
    @RequestMapping(value="/saveOrUpdateAuthorityVendorBillDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveOrUpdateAuthorityVendorBillDetails(@RequestBody HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response)
	{
		return auditService.saveOrUpdateAuthorityVendorBillDetails(new JSONObject(payload),request,response);
	}
    
    @RequestMapping(value = "/getVendorInvoicePaymentDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getVendorInvoicePaymentDetail(@RequestBody HashMap<String, Object> payload) {
        return auditService.getVendorInvoicePaymentDetail(new JSONObject(payload));
    }
    
	///////////////////////////save fund details controller 22th oct 2022 ////////////////////////
		
	@RequestMapping(value="/saveFundAllocationDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveFundAllocationDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
	HttpServletResponse response)
	{
	return auditService.saveFundAllocationDetails(jsondata, request, response);
	}
	
	@RequestMapping(value = "/getFundAllocationHdDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getFundAllocationHdDetails(@RequestBody HashMap<String, Object> payload) {
        return auditService.getFundAllocationHdDetails(new JSONObject(payload));
    }
	
	@RequestMapping(value = "/getFundAvailableBalance", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public Long getFundAvailableBalance(@RequestBody HashMap<String, Object> payload) {
        return auditService.getFundAvailableBalance(new JSONObject(payload));
    }
	
	@RequestMapping(value="/getDgFundAllcationHdDt", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getDgFundAllcationHdDt(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return auditService.getDgFundAllcationHdDt(jsondata, request, response);
	}
	
	@RequestMapping(value = "/deleteFundAllocationDtDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String deleteFundAllocationDtDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return auditService.deleteFundAllocationDtDetails(jsondata, request, response);
	}
	
	@RequestMapping(value="/getFundAllocationAmount", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public String getFundAllocationAmount(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return auditService.getFundAllocationAmount(map,request,response);
	}
	
	@RequestMapping(value="/getPenaltyAuthorityDetailsByUpss", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public Long getPenaltyAuthorityDetailsByUpss(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		return auditService.getPenaltyAuthorityDetailsByUpss(map,request,response);
	}
	@RequestMapping(value="/saveCaptureInterestDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveCaptureInterestDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
	HttpServletResponse response)
	{
	return auditService.saveCaptureInterestDetails(jsondata, request, response);
	}
	@RequestMapping(value = "/getAllCaptureInterestDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getAllCaptureInterestDetails(@RequestBody HashMap<String, Object> payload) {
        return auditService.getAllCaptureInterestDetails(new JSONObject(payload));
    }
	@RequestMapping(value="/getCaptureInterestHdDt", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getCaptureInterestHdDt(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return auditService.getCaptureInterestHdDt(jsondata, request, response);
	}
	
	@RequestMapping(value = "/deleteCaptureInterestDtDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String deleteCaptureInterestDtDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return auditService.deleteCaptureInterestDtDetails(jsondata, request, response);
	}
	
	@RequestMapping(value="/saveUCDocumentUploadDetails", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String saveUCDocumentUploadDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
	HttpServletResponse response)
	{
	return auditService.saveUCDocumentUploadDetails(jsondata, request, response);
	}
	
	 @RequestMapping(value = "/getUcUploadDocumentDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	    public String getUcUploadDocumentDetail(@RequestBody HashMap<String, Object> payload) {
	        return auditService.getUcUploadDocumentDetail(new JSONObject(payload));
	    }

	@RequestMapping(value = "/getUcUploadDocumentHdDt", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getUcUploadDocumentHdDt(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		return auditService.getUcUploadDocumentHdDt(jsondata, request, response);
	}
	
	@RequestMapping(value = "/deleteUCUploadDtDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String deleteUCUploadDtDetails(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return auditService.deleteUCUploadDtDetails(jsondata, request, response);
	}
	
	@RequestMapping(value = "/deleteDocument", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String deleteDocument(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) 
	{
		return auditService.deleteDocument(jsondata, request, response);
	}
	
	@RequestMapping(value="/getPenaltyRegister", method = RequestMethod.POST,produces="application/json",consumes="application/json")
	public String getPenaltyRegister(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response)
	{
		return auditService.getPenaltyRegister(jsondata, request, response);
	}
}
