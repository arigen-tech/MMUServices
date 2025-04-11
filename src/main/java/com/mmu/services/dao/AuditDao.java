package com.mmu.services.dao;

import com.mmu.services.entity.*;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Repository
public interface AuditDao {

    /***********************************Audit Common Operation ***********************************/
    boolean isRecordAlreadyExists(String keyColumn, String columnValue, Class entityClass);
    Long createRecord(Serializable entity);

    Object read(Class entityClass, Serializable id);

    Map<String, List<MasCamp>> getCampDetail(JSONObject jsondata);

    Map<String, List<CaptureEquipmentChecklistDetails>> getAllCapturedEquipmentChecklist(JSONObject jsondata);

    Map<String, List<CaptureInspectionDetails>> getAllCapturedInspectionChecklist(JSONObject jsondata);

    Map<String, List<CaptureInspectionChecklist>> getCapturedInspectionChecklistByDetailId(JSONObject jsondata);

    Map<String, List<InspectionChecklistValidationHistory>> getAllInspectionChecklistValidationHistory(JSONObject jsondata);

    Integer updateRecord(Serializable entity);

    Map<String, List<CaptureEquipmentChecklist>> getCapturedEquipmentChecklistByDetailId(JSONObject jsondata);

    Map<String, List<EquipmentChecklistValidationHistory>> getAllEquipmentChecklistValidationHistory(JSONObject jsondata);

    Map<String, List<Object[]>> getEquipmentPenaltyList(JSONObject jsondata);

    Map<String, List<Object[]>> getInspectionPenaltyList(JSONObject jsondata);

    Map<String, List<MasMmuVendor>> getVendors(JSONObject jsondata);

    Map<String, List<CaptureVendorBillDetail>> getCapturedVendorBillDetail(JSONObject jsondata);

    Map<String, Map<MasCity, List<MasMMU>>> getVendorsMMUAndCity(JSONObject jsondata);

    Map<String, List<Map<String, Object>>> getVendorsPenalty(JSONObject jsondata);

    Map<Long, MasEquipmentChecklist> getEquipmentMaster(List<Long> ids);

    Map<Long, MasInspectionChecklist> getInspectionMaster(List<Long> ids);

    Integer updateMultipleRecords(List<Serializable> entities);

    String[] getAPMDoctor(Long campId, Long mmuId);

    Map<Long, CaptureVendorBillMMUDetail> getCapturedMMUDetails(List<Long> ids);
	Long createRecordForUpdate(Serializable entity);
	String updateCaptureVendorBillDetail(HashMap<String, Object> jsonObject);
	Map<String, List<VendorInvoiceApproval>> getVendorInvoiceApprovalDetail(JSONObject jsondata);
	String saveOrUpdateAuthorityVendorBillDetails(HashMap<String, Object> jsondata);
	String savePaymentVendorBillDetails(HashMap<String, Object> jsondata);
	Map<String, List<VendorInvoicePayment>> getVendorInvoicePaymentDetail(JSONObject jsondata);
	 
		List<MasCamp> getMasCampFromMMUIdAndDate(Long mmuId, String date);
	
		Map<String, Object> getBillMonthandYear(Long month,Long year,Long district,Long cityId,String mmuIds);
		Long saveFundAllocationDetails(HashMap<String, Object> getJsondata, String letterName);
		Map<String, List<FundAllocationHd>> getFundAllocationHdDetails(JSONObject jsondata);
		Map<Long, FundAllocationDt> getFundAllocationDtDetails(List<Long> ids);
		List<Object[]> getDgFundAllcationHdDt(Long fundAllocationHdId);
		String deleteFundAllocationDtDetails(HashMap<String, Object> jsondata);
		Long saveFundAllocationApprovalDetails(HashMap<String, Object> getJsondata, String letterName);
		FundHcb getFundHcb(Long upss_id, Long cityId, Long head_type_id, Date currentDate,String phase);
		Map<String, Object> getFundAllocationAmount(HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response);
    	Map<String, Object> getPenaltyAuthId(Long upss_id);
		Long saveCaptureInterestDetails(HashMap<String, Object> getJsondata );
		Map<String, List<CaptureInterestHd>> getAllCaptureInterestDetails(JSONObject jsondata);
		Map<Long, CaptureInterestDt> getCaptureInterestDtDetails(List<Long> ids);
		List<Object[]> getCaptureInterestHdDt(Long fundAllocationHdId);
		String deleteCaptureInterestDtDetails(HashMap<String, Object> jsondata);
		Long saveUCDocumentUploadDetails(HashMap<String, Object> getJsondata, String letterName);
		Map<String, List<UcUploadHd>> getUcUploadDocumentDetail(JSONObject jsondata);
		Map<Long, UcUploadDt> getUcUploadDtDetails(List<Long> ids);
		List<Object[]> getUcUploadDocumentHdDt(Long ucUploadHdId);
		Long getCountUCUploadHd(Long upssId, Long financialId, String phaseVal);
		String deleteUCUploadDtDetails(HashMap<String, Object> jsondata);
	    Map<Long, VendorInvoicSupportingDocs> getVendorSupportingDocs(List<Long> ids);
		Long createOrUpdateMMURecord(Serializable entity, Long captureVendorBillMMUDetailId,
				Long captureVendorBillDetailId);
		Map<String, List<CaptureVendorBillDetail>> getCapturedVendorBillNodalDetail(JSONObject jsondata);
		boolean isRecordExists(String invoiceNo);
		Map<Long, Users> readUsers(List<Long> userIds);
		Map<Long, MasMmuVendor> readVendors(List<Long> vendorIds);
		Map<Long, MasCity> readCities(List<Long> cityIds);
		Map<Long, MasDistrict> readDistricts(List<Long> districtIds);
		String documentDelete(HashMap<String, Object> jsondata);
		
	
}
