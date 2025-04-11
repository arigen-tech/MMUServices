package com.mmu.services.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DispensaryDao;
import com.mmu.services.dao.LPProcessDao;
import com.mmu.services.dao.PatientRegistrationDao;
import com.mmu.services.entity.FundUtilization;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreLpType;
import com.mmu.services.entity.MasStoreLpc;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.StoreBudgetaryM;
import com.mmu.services.entity.StoreBudgetaryT;
import com.mmu.services.entity.StorePoT;
import com.mmu.services.entity.StoreQuotationM;
import com.mmu.services.entity.StoreQuotationT;
import com.mmu.services.entity.StoreSoM;
import com.mmu.services.entity.StoreSoT;
import com.mmu.services.entity.Users;
import com.mmu.services.service.LPProcessService;
import com.mmu.services.utils.HMSUtil;

@Repository
@Transactional
public class LPProcessServiceImpl implements LPProcessService {

	@Autowired
	DispensaryServiceImpl dispenceryService;

	@Autowired
	DispensaryDao dispensaryDao;

	@Autowired
	LPProcessDao lPProcessDao;

	@Autowired
	PatientRegistrationDao patientRegistrationDao;

	/*
	 * @Override public String submitBudgetary(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); long
	 * budgetaryMId=lPProcessDao.saveBudgetary(jsondata); if (budgetaryMId != 0 &&
	 * budgetaryMId != -1) { jsonObj.put("budgetaryId", budgetaryMId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else { if (budgetaryMId == -1) { jsonObj.put("status", 0);
	 * jsonObj.put("msg", "Something went wrong Record not Saved ");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString(); }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object>
	 * getAllbudgetaryApprovalList(HashMap<String, String> requestData,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub // Modified by rahul List<StoreBudgetaryM>
	 * storeBudgetaryMList = new ArrayList<StoreBudgetaryM>(); Map<String, Object>
	 * responseMap = new HashMap<String, Object>(); Map<Integer, Map<String,
	 * Object>> data = new HashMap<Integer, Map<String, Object>>(); Map<String,
	 * Object> map = new HashMap<String, Object>(); long pageNo = 0;
	 * 
	 * if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") !=
	 * null) { pageNo = Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * responseMap = lPProcessDao.getBudgetrayApprovalList(pageNo, requestData);
	 * 
	 * int count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * storeBudgetaryMList = (List<StoreBudgetaryM>)
	 * responseMap.get("storeBudgetaryMList"); if (!storeBudgetaryMList.isEmpty() &&
	 * storeBudgetaryMList.size() > 0) { for (StoreBudgetaryM storeBudgetaryM :
	 * storeBudgetaryMList) { Map<String, Object> map1 = new HashMap<String,
	 * Object>(); map1.put("budgetaryMId", storeBudgetaryM.getBudgetaryMId());
	 * map1.put("lpTypeFlag",storeBudgetaryM.getLpTypeFlag() != null ?
	 * storeBudgetaryM.getLpTypeFlag() : "");
	 * 
	 * if (storeBudgetaryM.getApproxCost() != null) { map1.put("approxCost",
	 * storeBudgetaryM.getApproxCost()); } else { map1.put("approxCost", ""); }
	 * 
	 * if (storeBudgetaryM.getReqDate() != null) { map1.put("reqDate",
	 * HMSUtil.changeDateToddMMyyyy(storeBudgetaryM.getReqDate())); } else {
	 * map1.put("reqDate", ""); } map1.put("createdBy",
	 * storeBudgetaryM.getReqBY().getFirstName());
	 * 
	 * if (storeBudgetaryM.getQuotationRemark() != null) map1.put("remarks",
	 * storeBudgetaryM.getQuotationRemark()); else map1.put("remarks", "");
	 * 
	 * if (storeBudgetaryM.getFinalApprovedDate() != null) map1.put("coApproveDate",
	 * HMSUtil.changeDateToddMMyyyy(storeBudgetaryM.getFinalApprovedDate())); else
	 * map1.put("coApproveDate", "");
	 * 
	 * if (storeBudgetaryM.getFinalApprovedBy() != null) map1.put("coApproveBy",
	 * storeBudgetaryM.getFinalApprovedBy().getFirstName()); else
	 * map1.put("coApproveBy", "");
	 * 
	 * map1.put("reqNo", storeBudgetaryM.getReqNo());
	 * 
	 * if (storeBudgetaryM.getMoApprovedDate() != null) map1.put("moApproveDate",
	 * HMSUtil.changeDateToddMMyyyy(storeBudgetaryM.getMoApprovedDate())); else
	 * map1.put("moApproveDate", "");
	 * 
	 * if (storeBudgetaryM.getMoApprovedBy() != null) map1.put("moApproveBy",
	 * storeBudgetaryM.getMoApprovedBy().getFirstName()); else
	 * map1.put("moApproveBy", ""); if (storeBudgetaryM.getMasStoreLpType() != null)
	 * map1.put("lpType", storeBudgetaryM.getMasStoreLpType().getLpName());// need
	 * to change else map1.put("lpType", ""); map1.put("statusId",
	 * storeBudgetaryM.getStatus()); if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("p")) { map1.put("status",
	 * "Pending for Approval(MO)"); map1.put("Qstatus", "New"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("W")) { map1.put("status",
	 * "Pending for Approval(CO)"); map1.put("Qstatus", "New"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("L")) { map1.put("status",
	 * "Pending For Approval(LPC)"); map1.put("Qstatus", "New"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("A")) { map1.put("status",
	 * "Approved by(CO)"); map1.put("Qstatus", "New"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("R")) { map1.put("status",
	 * "Rejected by(CO)"); map1.put("Qstatus", "New"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("N")) { map1.put("status",
	 * "Quotation Saved"); map1.put("Qstatus", "Saved"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("Y")) { map1.put("status",
	 * " Quotation Submitted"); map1.put("Qstatus", "Submit"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("M")) { map1.put("status",
	 * "Quotation Approved"); map1.put("Qstatus", "approved"); } else if
	 * (storeBudgetaryM.getStatus().equalsIgnoreCase("K")) { map1.put("status",
	 * "Quotation Rejected"); map1.put("Qstatus", "Quotation Rejected"); }
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map;
	 * 
	 * }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getBudgetaryDetails(HashMap<String,
	 * String> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * // TODO Auto-generated method stub // Modified by rahul List<StoreBudgetaryT>
	 * storeBudgetaryTList = new ArrayList<StoreBudgetaryT>(); Map<String, Object>
	 * responseMap = new HashMap<String, Object>(); Map<Integer, Map<String,
	 * Object>> data = new HashMap<Integer, Map<String, Object>>(); Map<String,
	 * Object> map = new HashMap<String, Object>(); JSONObject json = new
	 * JSONObject(jsondata); long budgetaryId =
	 * Long.parseLong(json.getString("budgetaryId")); responseMap =
	 * lPProcessDao.getIndentDetails(jsondata); int count = 1; String pvmsNo = "";
	 * if (responseMap.size() > 0 && !responseMap.isEmpty()) { storeBudgetaryTList =
	 * (List<StoreBudgetaryT>) responseMap.get("storeBudgetaryTList"); if
	 * (!storeBudgetaryTList.isEmpty() && storeBudgetaryTList.size() > 0) { for
	 * (StoreBudgetaryT storeBudgetaryT : storeBudgetaryTList) { Map<String, Object>
	 * map1 = new HashMap<String, Object>();
	 * 
	 * map1.put("status", storeBudgetaryT.getStoreBudgetaryM().getStatus());
	 * map1.put("id", storeBudgetaryT.getBudgetaryTId()); map1.put("reqNo",
	 * storeBudgetaryT.getStoreBudgetaryM().getReqNo()); map1.put("budgetaryMId",
	 * storeBudgetaryT.getStoreBudgetaryM().getBudgetaryMId());
	 * 
	 * if (storeBudgetaryT.getStoreBudgetaryM().getReqDate() != null) {
	 * map1.put("reqDate",
	 * HMSUtil.changeDateToddMMyyyy(storeBudgetaryT.getStoreBudgetaryM().getReqDate(
	 * ))); } else { map1.put("reqDate", ""); }
	 * 
	 * if (storeBudgetaryT.getStoreBudgetaryM().getMoApprovedDate() != null) {
	 * map1.put("moApproveDate",
	 * HMSUtil.changeDateToddMMyyyy(storeBudgetaryT.getStoreBudgetaryM().
	 * getMoApprovedDate())); } else { map1.put("moApproveDate", ""); }
	 * 
	 * map1.put("createdBy",
	 * storeBudgetaryT.getStoreBudgetaryM().getReqBY().getFirstName());
	 * map1.put("approxCost", storeBudgetaryT.getStoreBudgetaryM().getApproxCost());
	 * map1.put("lpTypeFlag",
	 * storeBudgetaryT.getStoreBudgetaryM().getLpTypeFlag()!=null?storeBudgetaryT.
	 * getStoreBudgetaryM().getLpTypeFlag():"");
	 * 
	 * double lastLpRate = storeBudgetaryT.getLastLpRate() != null?
	 * storeBudgetaryT.getLastLpRate().doubleValue(): 0; long qtyRequired =
	 * storeBudgetaryT.getQtyRequried(); map1.put("totalCost", Math.round(lastLpRate
	 * * qtyRequired * 100.0) / 100.0);
	 * 
	 * pvmsNo = storeBudgetaryT.getMasStoreItem1().getNomenclature() + "["+
	 * storeBudgetaryT.getMasStoreItem1().getPvmsNo() + "]"; map1.put("NomPvmsNo",
	 * pvmsNo);
	 * map1.put("accountingUnit",storeBudgetaryT.getMasStoreItem1().getMasStoreUnit1
	 * ().getStoreUnitName()); map1.put("requiredQty",
	 * storeBudgetaryT.getQtyRequried()); map1.put("itemId",
	 * storeBudgetaryT.getMasStoreItem1().getItemId()); map1.put("pvmsNo",
	 * storeBudgetaryT.getMasStoreItem1().getPvmsNo()); map1.put("lpunitrate",
	 * storeBudgetaryT.getLastLpRate()); data.put(count++, map1);
	 * 
	 * // lpTypeFlag put two time one in details and second in header.
	 * if(storeBudgetaryT.getStoreBudgetaryM().getLpTypeFlag()!=null) {
	 * map.put("lpTypeFlag", storeBudgetaryT.getStoreBudgetaryM().getLpTypeFlag());
	 * map.put("vendorId",storeBudgetaryT.getTempDirectReceivingForBackLp().
	 * getMasStoreSupplier().getSupplierId()); }else { map.put("lpTypeFlag", ""); }
	 * map.put("data", data);
	 * 
	 * map.put("count", count - 1);
	 * 
	 * } }
	 * 
	 * } return map; }
	 * 
	 * @Override public String moBudgetaryApproval(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); JSONObject
	 * json = new JSONObject(jsondata); StoreBudgetaryM storeBudgetaryM = new
	 * StoreBudgetaryM();
	 * 
	 * String covalue=""; String covalue1=""; String lpcValue=""; String
	 * lpcValue1="";
	 * 
	 * if (jsondata != null) { if(!jsondata.get("lpDate").toString().isEmpty() &&
	 * jsondata.get("lpDate")!=null) { String lpDate =
	 * jsondata.get("lpDate").toString(); String lpDate1 =
	 * dispenceryService.getReplaceString(lpDate); Date lpDate2 =
	 * HMSUtil.convertStringDateToUtilDate(lpDate1, "dd/MM/yyyy");
	 * storeBudgetaryM.setReqDate(new Timestamp(lpDate2.getTime())); }
	 * 
	 * 
	 * 
	 * String lpcType; if(json.has("co")) { covalue = jsondata.get("co").toString();
	 * covalue1 = dispenceryService.getReplaceString(covalue); lpcType=covalue1; }
	 * if(json.has("lpc")) { lpcValue = jsondata.get("lpc").toString(); lpcValue1 =
	 * dispenceryService.getReplaceString(lpcValue); lpcType=lpcValue1; }
	 * 
	 * 
	 * String departmentId = jsondata.get("departmentId").toString(); String
	 * departmentId1 = dispenceryService.getReplaceString(departmentId); long store
	 * = Long.parseLong(departmentId1);
	 * 
	 * 
	 * String nomenclature = jsondata.get("nomenclature").toString(); String
	 * nomenclature1 = dispenceryService.getReplaceString(nomenclature); String[]
	 * nomenclatureValue = nomenclature1.split(",");
	 * 
	 * String requiredQty = jsondata.get("requiredQty").toString(); String
	 * requiredQty1 = dispenceryService.getReplaceString(requiredQty); String[]
	 * requiredQtyValue = requiredQty1.split(","); int itemLength =
	 * requiredQtyValue.length;
	 * 
	 * String accountingUnit1 = jsondata.get("accountingUnit1").toString(); String
	 * accountingUnit = dispenceryService.getReplaceString(accountingUnit1);
	 * String[] accountingUnitValue = accountingUnit.split(",");
	 * 
	 * String approxCost = jsondata.get("approxCost").toString(); String approxCost1
	 * = dispenceryService.getReplaceString(approxCost); String[] approxCostValue =
	 * approxCost1.split(",");
	 * 
	 * String lpunitrate = jsondata.get("lpunitrate").toString(); String lpunitrate1
	 * = dispenceryService.getReplaceString(lpunitrate); String[] lpunitrateValue =
	 * lpunitrate1.split(",");
	 * 
	 * String itemIdNom = jsondata.get("itemIdNom").toString(); String itemIdNom1 =
	 * dispenceryService.getReplaceString(itemIdNom); String[] itemIdNomValue =
	 * itemIdNom1.split(",");
	 * 
	 * String pvmsNo1 = jsondata.get("pvmsNo1").toString(); String pvmsNo =
	 * dispenceryService.getReplaceString(pvmsNo1); String[] pvmsNo1Value =
	 * pvmsNo.split(",");
	 * 
	 * String budgetaryTId1 = jsondata.get("budgetaryTId1").toString(); String
	 * budgetaryTId= dispenceryService.getReplaceString(budgetaryTId1); String[]
	 * budgetaryTIdList = budgetaryTId.split(",");
	 * 
	 * String budgetaryMId = jsondata.get("budgetaryMId1").toString(); String
	 * budgetaryMId1= dispenceryService.getReplaceString(budgetaryMId); String[]
	 * budgetaryMId1List = budgetaryMId1.split(","); long
	 * budMId=Long.parseLong(budgetaryMId1List[0].trim()); MasStoreItem
	 * masStoreItem=null; MasHospital masHospital=null;
	 * 
	 * Users users=null; MasStoreLpType masStoreLpType=null; MasStoreLpType
	 * masStoreLpType1=new MasStoreLpType(); //get lp type
	 * masStoreLpType=lPProcessDao.getMasStoreLpType((new
	 * BigDecimal(approxCostValue[0]).longValue())); //save data in table
	 * if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * users=dispensaryDao.getUser(Long.parseLong(userId)); }
	 * 
	 * 
	 * Date date = new Date(); long budgMId=0; long budgTId=0;
	 * 
	 * String action="";
	 * 
	 * 
	 * //update storeBudgetaryM
	 * 
	 * storeBudgetaryM=lPProcessDao.getStoreBudgetaryM(budMId);
	 * if(storeBudgetaryM!=null) { ///for Approve
	 * 
	 * String approvalRemark=""; if(json.has("action")) { action =
	 * jsondata.get("action").toString(); action =
	 * dispenceryService.getReplaceString(action);
	 * 
	 * if(action.equalsIgnoreCase("Approve")) { storeBudgetaryM.setStatus("A");
	 * }else { storeBudgetaryM.setStatus("R");
	 * removeDataFromTempTableForBackLp(storeBudgetaryM);
	 * 
	 * }
	 * 
	 * 
	 * if(!json.getJSONArray("approvalDateOn").getString(0).isEmpty() &&
	 * json.getJSONArray("approvalDateOn").getString(0)!=null) {
	 * 
	 * storeBudgetaryM.setFinalApprovedDate(new Timestamp(HMSUtil
	 * .convertStringDateToUtilDateForDatabase(json.getJSONArray("approvalDateOn").
	 * getString(0)) .getTime())); }else { storeBudgetaryM.setFinalApprovedDate(new
	 * Timestamp(date.getTime())); } storeBudgetaryM.setFinalApprovedBy(users);
	 * //need to chk storeBudgetaryM.setApproxCost(new
	 * BigDecimal(approxCostValue[0]));
	 * //storeBudgetaryM.setFinalRemark(finalRemark);(new
	 * Timestamp(date.getTime()));
	 * 
	 * 
	 * if(json.has("approvalRemark")) { approvalRemark =
	 * jsondata.get("approvalRemark").toString(); approvalRemark =
	 * dispenceryService.getReplaceString(approvalRemark);
	 * storeBudgetaryM.setFinalRemark(approvalRemark);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * //End else { if(!json.getJSONArray("approvalDateOn").getString(0).isEmpty()
	 * && json.getJSONArray("approvalDateOn").getString(0)!=null) {
	 * 
	 * storeBudgetaryM.setMoApprovedDate(new Timestamp(HMSUtil
	 * .convertStringDateToUtilDateForDatabase(json.getJSONArray("approvalDateOn").
	 * getString(0)) .getTime())); }else { storeBudgetaryM.setMoApprovedDate(new
	 * Timestamp(date.getTime())); } storeBudgetaryM.setMoApprovedBy(users);
	 * storeBudgetaryM.setApproxCost(new BigDecimal(approxCostValue[0]));
	 * //storeBudgetaryM.setMasStoreLpType(masStoreLpType);//need to change
	 * if(covalue1.equalsIgnoreCase("FORWARD TO CO")) {
	 * storeBudgetaryM.setStatus("W");//p=submit,a=approve,r=reject,w=forward to
	 * co,l=forward to lpc masStoreLpType1.setLpTypeId(2L);//2=LP CASH & Carry
	 * storeBudgetaryM.setMasStoreLpType(masStoreLpType1); } else
	 * if(lpcValue1.equalsIgnoreCase("FORWARD TO LPC")) {
	 * storeBudgetaryM.setStatus("L"); masStoreLpType1.setLpTypeId(1L);//1=LPC
	 * storeBudgetaryM.setMasStoreLpType(masStoreLpType1); }
	 * 
	 * } lPProcessDao.updateStoreBudgetaryM(storeBudgetaryM); }
	 * 
	 * for (int i = 0; i < itemLength; i++) { StoreBudgetaryT storeBudgetaryT=new
	 * StoreBudgetaryT(); if(!budgetaryTIdList[i].trim().isEmpty() &&
	 * budgetaryTIdList[i]!=null) {
	 * storeBudgetaryT=lPProcessDao.getStoreBudgetaryT(Long.parseLong(
	 * budgetaryTIdList[i].trim()));
	 * //storeInternalIndentT.setId(Long.parseLong(indentList[i].trim())); }
	 * if(requiredQtyValue[i]!=null && !requiredQtyValue[i].isEmpty())
	 * storeBudgetaryT.setQtyRequried(Long.parseLong(requiredQtyValue[i].trim()));
	 * 
	 * 
	 * 
	 * if(itemIdNomValue[i]!=null && !itemIdNomValue[i].isEmpty()) {
	 * masStoreItem=dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[i].
	 * trim())); storeBudgetaryT.setMasStoreItem(masStoreItem); }
	 * if(lpunitrateValue[i]!=null && !lpunitrateValue[i].trim().isEmpty())
	 * storeBudgetaryT.setLastLpRate(new BigDecimal(lpunitrateValue[i].trim()));
	 * 
	 * 
	 * storeBudgetaryT.setStoreBudgetaryM(storeBudgetaryM);//need to chk budgTId=
	 * lPProcessDao.saveOrUpdateStoreBudgetaryT(storeBudgetaryT); }
	 * 
	 * 
	 * if (budgTId !=0) { jsonObj.put("budgetaryId", budgetaryMId);
	 * jsonObj.put("budMId", budMId); jsonObj.put("status", 1);
	 * if(action.equalsIgnoreCase("") || action.trim().isEmpty()) jsonObj.put("msg",
	 * "MO Approval has been done Successfully");
	 * if(action.equalsIgnoreCase("Approve")) jsonObj.put("msg",
	 * "Approval has been done"); else if(action.equalsIgnoreCase("Reject"))
	 * jsonObj.put("msg", "Budgetary has been Rejected ");
	 * 
	 * } else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } }
	 * return jsonObj.toString(); }
	 * 
	 * private void removeDataFromTempTableForBackLp(StoreBudgetaryM
	 * storeBudgetaryM) {
	 * lPProcessDao.removeDataFromTempTableForBackLp(storeBudgetaryM);
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> deleteIndentItems(String jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); JSONObject json = new JSONObject(jsondata); String budMId =
	 * json.get("aParam").toString(); int status=0; String budArr1 =
	 * dispenceryService.getReplaceString(budMId); String[] budArr2 =
	 * budArr1.split(","); for (String budTid : budArr2) {
	 * 
	 * long budId=Long.parseLong(budTid.substring(1, budTid.length()-1)); status=
	 * lPProcessDao.deleteIndentItem(budId); } if(status==1) {
	 * responseMap.put("status", status); responseMap.put("msg",
	 * "Data has been Deleted"); } else if(status==0) { responseMap.put("status",
	 * status); responseMap.put("msg", "Error has been occured"); } return
	 * responseMap; }
	 * 
	 * @Override public Map<Long, String> getVendorList(HashMap<String, String>
	 * jsondata) { // TODO Auto-generated method stub Map<Long, String> map = new
	 * HashMap<Long, String>(); List<MasStoreSupplier> vendorList = new
	 * ArrayList<MasStoreSupplier>(); String hospitalId=""; if
	 * (jsondata.get("hospitalId") != null) { hospitalId =
	 * jsondata.get("hospitalId").toString(); } vendorList =
	 * lPProcessDao.getVendorList(Long.parseLong(hospitalId)); for (MasStoreSupplier
	 * masStoreSupplier : vendorList) { map.put(masStoreSupplier.getSupplierId(),
	 * masStoreSupplier.getSupplierName()); }
	 * 
	 * return map; }
	 * 
	 * @Override public String submitBudgetaryQuotation(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); long
	 * quotationMId=lPProcessDao.saveBudgetaryQuotation(jsondata); if (quotationMId
	 * != 0 && quotationMId != -1) { jsonObj.put("quotationMId", quotationMId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else if (quotationMId == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Something went wrong Record not Saved ");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); }
	 * 
	 * return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object>
	 * getQuotationForApprovalList(HashMap<String, String> requestData,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub List<StoreQuotationM> storeQuotationMList = new
	 * ArrayList<StoreQuotationM>(); List<Object[]> objectArrayList = new
	 * ArrayList<>(); List<Object> responseList = new ArrayList<Object>();
	 * Map<String,Object> responseMap=new HashMap<String, Object>(); Map<Integer,
	 * Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
	 * Map<String, Object> map = new HashMap<String, Object>(); long pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = lPProcessDao.getQuotationApprovalList(pageNo,requestData); int
	 * count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * objectArrayList = (List<Object[]>) responseMap.get("storeQuotationMList"); if
	 * (!objectArrayList.isEmpty() && objectArrayList.size() > 0) { for( Object[]
	 * data1 : objectArrayList) { Long budMid = Long.parseLong(data1[0].toString());
	 * String qDate = data1[1].toString(); Long userId =
	 * Long.parseLong(data1[2].toString()); Double l1Cost =
	 * Double.parseDouble(data1[3].toString()); // Long storeQMId =
	 * Long.parseLong(data1[4].toString()); Map<String,Object>map1=new
	 * HashMap<String, Object>(); Date qodate=null; Users users=null;
	 * users=dispensaryDao.getUser(userId); StoreBudgetaryM
	 * storeBudgetaryM=lPProcessDao.getStoreBudgetaryM(budMid); String[] arr =
	 * qDate.split(" "); try { qodate = HMSUtil.dateFormatteryyyymmdd(arr[0]); }
	 * catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } String quotationdate =
	 * HMSUtil.convertDateToStringFormat(qodate,"dd/MM/yyyy"); List<StoreQuotationM>
	 * storeQuotationMlist = storeBudgetaryM.getStoreQuotationMs(); long
	 * storeQMId=0; for(StoreQuotationM storeQuotationM : storeQuotationMlist) {
	 * 
	 * if(storeQuotationM.getL1Status()!=null)
	 * if(storeQuotationM.getL1Status().equalsIgnoreCase("Y")) {
	 * map1.put("storeQMId", storeQuotationM.getQuotationMId());
	 * storeQMId=storeQuotationM.getQuotationMId();
	 * if(storeQuotationM.getStoreSoM()!=null &&
	 * storeQuotationM.getStoreSoM().getStatus().equalsIgnoreCase("N"))
	 * 
	 * map1.put("status", "Saved"); else if(storeQuotationM.getStoreSoM()!=null &&
	 * storeQuotationM.getStoreSoM().getStatus().equalsIgnoreCase("R"))
	 * map1.put("status", "Rejected"); else map1.put("status", "New"); } }
	 * 
	 * map1.put("quotationDate", quotationdate); map1.put("lpType",
	 * storeBudgetaryM.getMasStoreLpType().getLpName()); map1.put("lpTypeId",
	 * storeBudgetaryM.getMasStoreLpType().getLpTypeId());
	 * if(storeBudgetaryM.getStoreQuotationMs().get(0).getApprovedBy()!=null)
	 * map1.put("qutApprovedBy",
	 * storeBudgetaryM.getStoreQuotationMs().get(0).getApprovedBy().getFirstName());
	 * map1.put("leastCost", l1Cost); map1.put("budgetaryMId", budMid);
	 * map1.put("quotationBy", users.getFirstName()); map1.put("reqDate",
	 * HMSUtil.changeDateToddMMyyyy(storeBudgetaryM.getReqDate()));
	 * map1.put("reqNo", storeBudgetaryM.getReqNo());
	 * 
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map;
	 * 
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getNameByServiceNo(String jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub MasEmployee masEmployee=null; Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); JSONObject json = new
	 * JSONObject(jsondata); String serviceNo =
	 * json.get("memberService").toString(); masEmployee=
	 * lPProcessDao.getNameByServiceNo(serviceNo); String rank="";
	 * if(masEmployee!=null) { String empRankCode = masEmployee.getMasRank()!=null?
	 * masEmployee.getMasRank():"0"; List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode);
	 * if(!mRankList.isEmpty() && mRankList.size()>0)
	 * rank=mRankList.get(0).getRankName(); responseMap.put("name",
	 * rank+" "+masEmployee.getEmployeeName());
	 * responseMap.put("age",HMSUtil.calculateAgeNoOfYear(masEmployee.getDob()));
	 * responseMap.put("empId", masEmployee.getEmployeeId()); responseMap.put("msg",
	 * "success"); responseMap.put("status", 1); } else { responseMap.put("status",
	 * 0); responseMap.put("msg", "Service No does not exist"); } return
	 * responseMap; }
	 * 
	 * @Override public String submitCtmForLP(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject json = new JSONObject(jsondata);
	 * JSONObject jsonObj = new JSONObject(); MasStoreLpc masStoreLpc = new
	 * MasStoreLpc(); Date fromDate2=null; Date toDate2=null; long masStoreLpcId=0;
	 * String status="0"; if (jsondata != null) {
	 * if(!jsondata.get("fromDate").toString().isEmpty()) { String fromDate =
	 * jsondata.get("fromDate").toString(); String fromDate1 =
	 * dispenceryService.getReplaceString(fromDate); fromDate2 =
	 * HMSUtil.convertStringDateToUtilDate(fromDate1, "dd/MM/yyyy"); }
	 * if(!jsondata.get("toDate").toString().isEmpty()) { String toDate =
	 * jsondata.get("toDate").toString(); String toDate1 =
	 * dispenceryService.getReplaceString(toDate); toDate2 =
	 * HMSUtil.convertStringDateToUtilDate(toDate1, "dd/MM/yyyy"); }
	 * 
	 * MasHospital masHospital = null; String hospitalId=""; if
	 * (jsondata.get("hospitalId") != null) { hospitalId =
	 * jsondata.get("hospitalId").toString(); masHospital =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); }
	 * status=lPProcessDao.checkCommitteeExist(fromDate2,toDate2,Long.parseLong(
	 * hospitalId)); if(status.equalsIgnoreCase("0")) {
	 * 
	 * String m2ServiceNo = jsondata.get("m2ServiceNo").toString(); String
	 * m2ServiceNo2 = dispenceryService.getReplaceString(m2ServiceNo);
	 * 
	 * String m1ServiceNo = jsondata.get("m1ServiceNo").toString(); String
	 * m2ServiceNo1 = dispenceryService.getReplaceString(m1ServiceNo);
	 * 
	 * String pServiceNo = jsondata.get("pServiceNo").toString(); String pServiceNo1
	 * = dispenceryService.getReplaceString(pServiceNo);
	 * 
	 * String m1Name = jsondata.get("m1Name").toString(); String m1Name1 =
	 * dispenceryService.getReplaceString(m1Name);
	 * 
	 * String m2Name = jsondata.get("m2Name").toString(); String m2Name2 =
	 * dispenceryService.getReplaceString(m2Name);
	 * 
	 * 
	 * String pName = jsondata.get("pName").toString(); String pName1 =
	 * dispenceryService.getReplaceString(pName);
	 * 
	 * 
	 * 
	 * String pEmpId = jsondata.get("pEmpId").toString(); String pEmpId1 =
	 * dispenceryService.getReplaceString(pEmpId);
	 * 
	 * 
	 * String m2EmpId = jsondata.get("m2EmpId").toString(); String m2EmpId2 =
	 * dispenceryService.getReplaceString(m2EmpId);
	 * 
	 * 
	 * String m1EmpId = jsondata.get("m1EmpId").toString(); String m1EmpId1 =
	 * dispenceryService.getReplaceString(m1EmpId);
	 * 
	 * 
	 * String letterNo = jsondata.get("letterNo").toString(); String letterNo1 =
	 * dispenceryService.getReplaceString(letterNo);
	 * 
	 * String ridcId=""; if(!jsondata.get("ridcId").toString().isEmpty()) { ridcId =
	 * jsondata.get("ridcId").toString();
	 * 
	 * } Users users=new Users(); //save data in table
	 * if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * //users=dispensaryDao.getUser(Long.parseLong(userId));
	 * users.setUserId(Long.parseLong(userId)); }
	 * masStoreLpc.setFromDate(fromDate2); masStoreLpc.setToDate(toDate2);
	 * masStoreLpc.setLastChgDate(new Timestamp(new Date().getTime()));
	 * masStoreLpc.setLastChgBy(users); masStoreLpc.setLetterNo(letterNo1);
	 * masStoreLpc.setMember1Id(Long.parseLong(m1EmpId1));
	 * masStoreLpc.setMember2Id(Long.parseLong(m2EmpId2));
	 * masStoreLpc.setPresidentId(Long.parseLong(pEmpId1));
	 * if(!ridcId.equalsIgnoreCase(""))
	 * masStoreLpc.setRidcId(Long.parseLong(ridcId));
	 * masStoreLpc.setMasHospital(masHospital); masStoreLpc.setStatus("Y");
	 * masStoreLpcId= lPProcessDao.saveMasStoreLpc(masStoreLpc); if (masStoreLpcId
	 * != 0) { jsonObj.put("masStoreLpcId", masStoreLpcId); jsonObj.put("status",
	 * 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * } else if(status.equalsIgnoreCase("1")){ jsonObj.put("status", 1);
	 * jsonObj.put("msg", "Committee Already exist for this duration"); } else{
	 * jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); } } else
	 * if(status.equalsIgnoreCase("1")){ jsonObj.put("status", 1);
	 * jsonObj.put("msg", "Committee Already exist for this duration"); } else{
	 * jsonObj.put("status", 0); jsonObj.put("msg", "Error occured");
	 * 
	 * } }
	 * 
	 * 
	 * return jsonObj.toString();
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getCtmMemberList(HashMap<String, String>
	 * requestData, HttpServletRequest request, HttpServletResponse response) { //
	 * TODO Auto-generated method stub List<MasStoreLpc> masStoreLpcList = new
	 * ArrayList<MasStoreLpc>(); List<Map<String,Object>> responseList = new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); long pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); } String hospitalId=""; if
	 * (requestData.get("hospitalId") != null) { hospitalId =
	 * requestData.get("hospitalId").toString(); }
	 * 
	 * responseMap =
	 * lPProcessDao.getCtmMemberList(pageNo,requestData,Long.parseLong(hospitalId));
	 * int count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * masStoreLpcList = (List<MasStoreLpc>) responseMap.get("masStoreLpcList"); if
	 * (!masStoreLpcList.isEmpty() && masStoreLpcList.size() > 0) { for(MasStoreLpc
	 * masStoreLpc : masStoreLpcList) { MasEmployee
	 * masEmployee1=lPProcessDao.getMasEmployee(masStoreLpc.getMember1Id());
	 * MasEmployee
	 * masEmployee2=lPProcessDao.getMasEmployee(masStoreLpc.getMember2Id());
	 * MasEmployee
	 * president=lPProcessDao.getMasEmployee(masStoreLpc.getPresidentId());
	 * 
	 * String empRankCode = masEmployee1.getMasRank()!=null?
	 * masEmployee1.getMasRank():"0"; String empRankCode1 =
	 * masEmployee2.getMasRank()!=null? masEmployee2.getMasRank():"0"; String
	 * empRankCode2 = president.getMasRank()!=null? president.getMasRank():"0";
	 * 
	 * List<MasRank> mRankList =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode); List<MasRank>
	 * mRankList1 = patientRegistrationDao.getEmpRankAndTrade(empRankCode1);
	 * List<MasRank> mRankList2 =
	 * patientRegistrationDao.getEmpRankAndTrade(empRankCode2); String empRank1="";
	 * String empRank2=""; String presidentRank=""; if(!mRankList.isEmpty() &&
	 * mRankList.size()>0) empRank1 = mRankList.get(0).getRankName();
	 * if(!mRankList1.isEmpty() && mRankList1.size()>0) empRank2 =
	 * mRankList1.get(0).getRankName(); if(!mRankList2.isEmpty() &&
	 * mRankList2.size()>0) presidentRank = mRankList2.get(0).getRankName();
	 * 
	 * Map<String,Object>map1=new HashMap<String, Object>(); map1.put("id",
	 * masStoreLpc.getLpcId()); map1.put("fromDate",
	 * HMSUtil.changeDateToddMMyyyy(masStoreLpc.getFromDate())); map1.put("toDate",
	 * HMSUtil.changeDateToddMMyyyy(masStoreLpc.getToDate()));
	 * map1.put("member1",empRank1+" "+masEmployee1.getEmployeeName());
	 * map1.put("member2", empRank2+" "+masEmployee2.getEmployeeName());
	 * map1.put("president", presidentRank+" "+president.getEmployeeName());
	 * map1.put("letterNo", masStoreLpc.getLetterNo()); map1.put("ridcId",
	 * masStoreLpc.getRidcId()); if(masStoreLpc.getStatus().equalsIgnoreCase("Y"))
	 * map1.put("status", "Active"); else map1.put("status", "Inactive");
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map;
	 * 
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> inactivatectmCommittee(HashMap<String,
	 * String> jsondata, HttpServletRequest request, HttpServletResponse response) {
	 * // TODO Auto-generated method stub
	 * 
	 * //JSONObject json = new JSONObject(jsondata); JSONObject jsonObj = new
	 * JSONObject(jsondata); Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); MasStoreLpc masStoreLpc = new MasStoreLpc(); Users users=new
	 * Users(); long id=0; Long
	 * lpcId=Long.parseLong(jsondata.get("lpcId").toString());
	 * masStoreLpc=lPProcessDao.getMasStoreLpc(lpcId);
	 * masStoreLpc.setLastChgDate(new Timestamp(new Date().getTime()));
	 * if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * //users=dispensaryDao.getUser(Long.parseLong(userId));
	 * users.setUserId(Long.parseLong(userId)); } masStoreLpc.setLastChgBy(users);
	 * masStoreLpc.setStatus("N"); id =
	 * lPProcessDao.inactivateCommittee(masStoreLpc); if (id != 0) {
	 * responseMap.put("status", 1); responseMap.put("msg",
	 * "Committee has been Inactivated"); }else{ responseMap.put("status", 0);
	 * responseMap.put("msg", "Error occured"); } return responseMap; }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Map<String, Object> getItemList(HashMap<String, String>
	 * payload, HttpServletRequest request, HttpServletResponse response) {
	 * List<StoreQuotationT> storeQuotationTList = new ArrayList<StoreQuotationT>();
	 * List<StoreQuotationM> storeQuotationMList = new ArrayList<StoreQuotationM>();
	 * Map<String,Object> responseMap=new HashMap<String, Object>(); Map<Integer,
	 * Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
	 * Map<Integer, Map<String, Object>> data1 = new HashMap<Integer, Map<String,
	 * Object>>(); Map<String, Object> map = new HashMap<String, Object>();
	 * 
	 * List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
	 * StoreQuotationT storeQuotationT = new StoreQuotationT();
	 * 
	 * responseMap = lPProcessDao.getItemList(payload); String pvmsNo=""; int i=0;
	 * int count=0; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * storeQuotationTList = (List<StoreQuotationT>)
	 * responseMap.get("storeQuotationTList"); if (!storeQuotationTList.isEmpty() &&
	 * storeQuotationTList.size() > 0) {
	 * 
	 * List<StoreBudgetaryT> storeBudgetaryTList =
	 * storeQuotationTList.get(0).getStoreQuotationM().getStoreBudgetaryM().
	 * getStoreBudgetaryTs();
	 * 
	 * 
	 * for(StoreBudgetaryT storeBudgetaryT : storeBudgetaryTList) { i=0; ///need to
	 * chk Map<String,Object>map1=new HashMap<String, Object>();
	 * pvmsNo=storeBudgetaryT.getMasStoreItem1().getNomenclature() + "[" +
	 * storeBudgetaryT.getMasStoreItem1().getPvmsNo()+"]"; map1.put("NomPvmsNo",
	 * pvmsNo); map1.put("accountingUnit",
	 * storeBudgetaryT.getMasStoreItem1().getMasStoreUnit1().getStoreUnitName());
	 * map1.put("requiredQty",storeBudgetaryT.getQtyRequried());
	 * map1.put("itemId",storeBudgetaryT.getMasStoreItem1().getItemId());
	 * map1.put("pvmsNo",storeBudgetaryT.getMasStoreItem1().getPvmsNo());
	 * map1.put("lpunitrate",storeBudgetaryT.getLastLpRate());
	 * //map1.put("vtotalCost", storeQuotationT1);
	 * 
	 * for(StoreQuotationT storeQuotationT1 : storeQuotationTList) {
	 * if(storeBudgetaryT.getMasStoreItem1().getItemId().intValue()==
	 * storeQuotationT1.getMasStoreItem().getItemId().intValue()) { i++;
	 * map1.put("vendorId"+i,
	 * storeQuotationT1.getMasStoreSupplier().getSupplierId());
	 * map1.put("vendorName"+i,
	 * storeQuotationT1.getMasStoreSupplier().getSupplierName());
	 * map1.put("unitRate"+i,storeQuotationT1.getUnitRate());
	 * map1.put("totalCost"+i,storeQuotationT1.getTotalCost());
	 * 
	 * 
	 * } } data.put(count++, map1); map.put("totalVendor", i); } } int j=0; Long
	 * min=0l; String l1VendorName=""; String quotationDate=""; long l1VendorId=0;
	 * String lpTypeFlag=""; storeQuotationMList = (List<StoreQuotationM>)
	 * responseMap.get("storeQuotationMList"); if (!storeQuotationMList.isEmpty() &&
	 * storeQuotationMList.size() > 0) {
	 * quotationDate=HMSUtil.changeDateToddMMyyyy(storeQuotationMList.get(0).
	 * getQuotationDate()); for(StoreQuotationM storeQuotationM :
	 * storeQuotationMList) { Map<String,Object>map1=new HashMap<String,Object>();
	 * j++; if(j==1) { min=storeQuotationM.getTotalCost().longValue();
	 * l1VendorName=storeQuotationM.getMasStoreSupplier().getSupplierName();
	 * l1VendorId=storeQuotationM.getMasStoreSupplier().getSupplierId(); }
	 * if(storeQuotationM.getTotalCost().longValue()<min){
	 * min=storeQuotationM.getTotalCost().longValue();
	 * l1VendorId=storeQuotationM.getMasStoreSupplier().getSupplierId();
	 * l1VendorName=storeQuotationM.getMasStoreSupplier().getSupplierName(); }
	 * 
	 * map1.put("vcost"+j, storeQuotationM.getTotalCost()); map1.put("VendorName"+j,
	 * storeQuotationM.getMasStoreSupplier().getSupplierName());
	 * map1.put("ridcId"+j, storeQuotationM.getRidcId()); data1.put(j, map1); } }
	 * 
	 * if(storeQuotationMList.get(0).getLpTypeFlag()!=null &&
	 * storeQuotationMList.get(0).getLpTypeFlag().equalsIgnoreCase("B") ) {
	 * lpTypeFlag=storeQuotationMList.get(0).getLpTypeFlag(); map.put("lpTypeFlag",
	 * lpTypeFlag); }else { map.put("lpTypeFlag", lpTypeFlag); }
	 * 
	 * map.put("l1Vendor", l1VendorName); map.put("l1VendorId", l1VendorId);
	 * map.put("quotationDate", quotationDate); } map.put("data", data);
	 * map.put("vcostdata", data1); map.put("status", "1"); map.put("count", count);
	 * return map; }
	 * 
	 * @Override public Map<String, Object> getCommitteeMember(String jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub MasStoreLpc masStoreLpc=null; Map<String,Object>
	 * responseMap=new HashMap<String, Object>(); JSONObject json = new
	 * JSONObject(jsondata); Date quotationDate1=null; if(json.has("quotationDate"))
	 * { String quotationDate = json.get("quotationDate").toString(); quotationDate1
	 * = HMSUtil.convertStringDateToUtilDate(quotationDate, "dd/MM/yyyy"); }else {
	 * //qd=HMSUtil.changeDateToddMMyyyy(date); quotationDate1 = new Date(); }
	 * String hospitalId=""; if (json.get("hospitalId") != null) { hospitalId =
	 * json.get("hospitalId").toString(); }
	 * 
	 * masStoreLpc=
	 * lPProcessDao.getCommitteeMembers(quotationDate1,Long.parseLong(hospitalId));
	 * 
	 * if(masStoreLpc!=null) { MasEmployee
	 * masEmployee1=lPProcessDao.getMasEmployee(masStoreLpc.getMember1Id());
	 * MasEmployee
	 * masEmployee2=lPProcessDao.getMasEmployee(masStoreLpc.getMember2Id());
	 * MasEmployee
	 * president=lPProcessDao.getMasEmployee(masStoreLpc.getPresidentId());
	 * responseMap.put("memberName1",masEmployee1.getEmployeeName());
	 * responseMap.put("memberName2",masEmployee2.getEmployeeName());
	 * responseMap.put("president",president.getEmployeeName());
	 * //responseMap.put("empId", masEmployee.getEmployeeId());
	 * responseMap.put("msg", "success"); responseMap.put("status", 1); } else {
	 * responseMap.put("status", 0); responseMap.put("msg",
	 * "Committee does not exist"); } return responseMap; }
	 * 
	 * @SuppressWarnings("static-access")
	 * 
	 * @Override public String approveQuotationPresident(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub List<StoreQuotationM> storeQuotationMList = new
	 * ArrayList<StoreQuotationM>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); JSONObject json = new JSONObject(jsondata);
	 * JSONObject jsonObj = new JSONObject(jsondata); MasStoreLpc masStoreLpc = new
	 * MasStoreLpc(); Date quotationDate=null; Date toDate2=null; String budId="";
	 * int flag=0; long masStoreLpcId=0; String status="0"; String vendorId1=""; if
	 * (jsondata != null) {
	 * if(!jsondata.get("quotApproveDate").toString().isEmpty()) { String fromDate =
	 * jsondata.get("quotApproveDate").toString(); String fromDate1 =
	 * dispenceryService.getReplaceString(fromDate); quotationDate =
	 * HMSUtil.convertStringDateToUtilDate(fromDate1, "dd/MM/yyyy"); }
	 * if(!jsondata.get("budgetaryId").toString().isEmpty()) { String budgetaryId =
	 * jsondata.get("budgetaryId").toString(); budId =
	 * dispenceryService.getReplaceString(budgetaryId);
	 * 
	 * }
	 * 
	 * if(!jsondata.get("vendorId").toString().isEmpty()) { String vendorId =
	 * jsondata.get("vendorId").toString(); vendorId1=
	 * dispenceryService.getReplaceString(vendorId);
	 * 
	 * } Users users=new Users(); if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString();
	 * //users=dispensaryDao.getUser(Long.parseLong(userId));
	 * users.setUserId(Long.parseLong(userId)); } //update status in storeBudgetary
	 * table M=Approve String action=""; String approvalRemark=""; StoreBudgetaryM
	 * storeBudgetaryM=lPProcessDao.getStoreBudgetaryM(Long.parseLong(budId));
	 * if(json.has("action")) { action = jsondata.get("action").toString(); action =
	 * dispenceryService.getReplaceString(action);
	 * if(action.equalsIgnoreCase("Approve"))
	 * storeBudgetaryM.setStatus("M");//Quotation Approved else
	 * storeBudgetaryM.setStatus("K");//Quotation Rejected
	 * 
	 * if(json.has("approvalRemark")) { approvalRemark =
	 * jsondata.get("approvalRemark").toString(); approvalRemark =
	 * dispenceryService.getReplaceString(approvalRemark);
	 * 
	 * 
	 * } } storeBudgetaryM.setQuotationRemark(approvalRemark);;
	 * lPProcessDao.updateStoreBudgetaryM(storeBudgetaryM);
	 * 
	 * long quotationMId=0; long quotMId=0; long lpTypeId=0; int status1=0;
	 * 
	 * responseMap=lPProcessDao.getStoreQuotationM(Long.parseLong(budId));
	 * storeQuotationMList = (List<StoreQuotationM>)
	 * responseMap.get("storeQuotationMList");
	 * 
	 * if(action.equalsIgnoreCase("Reject")) { if (!storeQuotationMList.isEmpty() &&
	 * storeQuotationMList.size() > 0) { for(StoreQuotationM storeQuotationM :
	 * storeQuotationMList) {
	 * 
	 * status1= lPProcessDao.deleteQuotation(storeQuotationM); } } }
	 * 
	 * if (!storeQuotationMList.isEmpty() && storeQuotationMList.size() > 0) {
	 * for(StoreQuotationM storeQuotationM : storeQuotationMList) {
	 * if(storeQuotationM.getMasStoreSupplier().getSupplierId()==Long.parseLong(
	 * vendorId1)) { storeQuotationM.setL1Status("Y");
	 * quotMId=storeQuotationM.getQuotationMId(); }else {
	 * storeQuotationM.setL1Status("N"); }
	 * 
	 * if(action.equalsIgnoreCase("Approve")) {
	 * 
	 * storeQuotationM.setStatus("M"); //A=Approve }else {
	 * storeQuotationM.setStatus("K");//K=rejected
	 * removeDataFromTempTableForBackLp(storeQuotationM.getStoreBudgetaryM()); }
	 * 
	 * //storeQuotationM.setStatus("M"); storeQuotationM.setRemarks(approvalRemark);
	 * storeQuotationM.setApprovedBy(users); storeQuotationM.setApprovedDate(new
	 * Timestamp(new Date().getTime()));
	 * quotationMId=storeQuotationM.getQuotationMId();
	 * flag=lPProcessDao.updateStoreQuotationM(storeQuotationM); } } if(flag != 0) {
	 * jsonObj.put("status", 1); jsonObj.put("quotationMId", quotMId);
	 * 
	 * jsonObj.put("lpTypeId", storeBudgetaryM.getMasStoreLpType().getLpTypeId());
	 * if(action.equalsIgnoreCase("Approve")) jsonObj.put("msg",
	 * "Quotation has been Approved"); else if(action.equalsIgnoreCase("Reject"))
	 * jsonObj.put("msg", "Quotation has been Rejected "); //jsonObj.put("msg",
	 * "Quotation has been Approved");
	 * 
	 * } else{ jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); }
	 * 
	 * 
	 * } return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object>
	 * getItemListForSupplyOrder(HashMap<String, String> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub List<StoreQuotationT> storeQuotationTList = new
	 * ArrayList<StoreQuotationT>(); List<Map<String,Object>> responseList = new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); JSONObject json = new JSONObject(jsondata); long
	 * storeQMId = Long.parseLong(json.getString("storeQMId")); long budgetaryId =
	 * Long.parseLong(json.getString("budgetaryId")); String status =
	 * json.getString("status"); //add fields in case of save as draft and reject
	 * Map<String, Object> sanctionData = new HashMap<String, Object>();
	 * if(status.equalsIgnoreCase("Rejected")||status.equalsIgnoreCase("Saved")) {
	 * StoreSoM storeSoM=lPProcessDao.getSanctionData(storeQMId);
	 * 
	 * sanctionData.put("sanctionId",storeSoM.getSoMId());
	 * sanctionData.put("fileNo",storeSoM.getFileNo());
	 * sanctionData.put("sanctionPurpose",storeSoM.getOrderPurpose());
	 * sanctionData.put("quantumItem",storeSoM.getQuatationOfItem());
	 * sanctionData.put("authorityLetter",storeSoM.getRefOfGovtAuthority());
	 * sanctionData.put("expenditure",storeSoM.getBookedBudget());
	 * sanctionData.put("videNote",storeSoM.getVideNoteNumber());
	 * sanctionData.put("cda",storeSoM.getSancOverruling());
	 * sanctionData.put("remarks",storeSoM.getRemarks()); sanctionData.put("reqNo",
	 * storeSoM.getRfpNo()); sanctionData.put("quotationNo",
	 * storeSoM.getQuatationNo()); sanctionData.put("storeSoMId",
	 * storeSoM.getSoMId()); sanctionData.put("fileNo", storeSoM.getFileNo());
	 * sanctionData.put("createSanctionDate", storeSoM.getOrderDate());
	 * //sanctionData.put("orderPurpose", storeSoM.getOrderPurpose());
	 * sanctionData.put("vendorName",
	 * storeSoM.getMasStoreSupplier().getSupplierName());
	 * sanctionData.put("vendorId",storeSoM.getMasStoreSupplier().getSupplierId());
	 * sanctionData.put("valueOfOrder", storeSoM.getOrderValue());
	 * 
	 * sanctionData.put("issueUnder", storeSoM.getIssuedUnder());
	 * sanctionData.put("payingAgency", storeSoM.getPayingAgency());
	 * sanctionData.put("uoNumber", storeSoM.getUoNo());
	 * sanctionData.put("commWithSanction", storeSoM.getCommWithSanc());
	 * sanctionData.put("sanctionIssued", storeSoM.getSancOverruling());
	 * sanctionData.put("totalAmount", storeSoM.getTotalAmt());
	 * sanctionData.put("totalAmountWords", storeSoM.getTotalAmtInWords());
	 * sanctionData.put("budgetaryId", budgetaryId);
	 * 
	 * if(storeSoM.getRidcId()!=null) sanctionData.put("ridcId",
	 * storeSoM.getRidcId()); else sanctionData.put("ridcId", "");
	 * 
	 * }
	 * 
	 * 
	 * responseMap = lPProcessDao.getL1ItemList(jsondata); int count = 1; String
	 * pvmsNo=""; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * storeQuotationTList = (List<StoreQuotationT>)
	 * responseMap.get("storeQuotationTList"); if (!storeQuotationTList.isEmpty() &&
	 * storeQuotationTList.size() > 0) { for(StoreQuotationT storeQuotationT :
	 * storeQuotationTList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * 
	 * map1.put("storeQuotationTId", storeQuotationT.getQuotationTId());
	 * map1.put("quotationNo",
	 * storeQuotationT.getStoreQuotationM().getQuotationNo()); map1.put("reqNo",
	 * storeQuotationT.getStoreQuotationM().getStoreBudgetaryM().getReqNo());
	 * map1.put("storeQMId", storeQMId); map1.put("budgetaryId", budgetaryId);
	 * pvmsNo=storeQuotationT.getMasStoreItem().getNomenclature() + "[" +
	 * storeQuotationT.getMasStoreItem().getPvmsNo()+"]"; map1.put("NomPvmsNo",
	 * pvmsNo); map1.put("accountingUnit",
	 * storeQuotationT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
	 * map1.put("requiredQty",storeQuotationT.getQtyRequried());
	 * map1.put("itemId",storeQuotationT.getMasStoreItem().getItemId());
	 * map1.put("pvmsNo",storeQuotationT.getMasStoreItem().getPvmsNo());
	 * map1.put("unitrate",storeQuotationT.getUnitRate());
	 * map1.put("l1Cost",storeQuotationT.getStoreQuotationM().getTotalCost());
	 * map1.put("l1CostWord",HMSUtil.convert(storeQuotationT.getStoreQuotationM().
	 * getTotalCost().intValue())); map1.put("date",HMSUtil.changeDateToddMMyyyy(new
	 * Date())); String address="";
	 * if(storeQuotationT.getMasStoreSupplier().getAddress1()!=null)
	 * address=address+storeQuotationT.getMasStoreSupplier().getAddress1()+" ";
	 * if(storeQuotationT.getMasStoreSupplier().getAddress2()!=null)
	 * address=address+storeQuotationT.getMasStoreSupplier().getAddress2()+" ";
	 * if(storeQuotationT.getMasStoreSupplier().getPinNo()!=null)
	 * address=address+storeQuotationT.getMasStoreSupplier().getPinNo()+" ";
	 * map1.put("vendorName",storeQuotationT.getMasStoreSupplier().getSupplierName()
	 * +" , "+address);
	 * map1.put("vendorId",storeQuotationT.getMasStoreSupplier().getSupplierId());
	 * double lastLpRate = storeQuotationT.getUnitRate().doubleValue(); long
	 * qtyRequired = storeQuotationT.getQtyRequried(); map1.put("totalCost",
	 * Math.round(lastLpRate*qtyRequired * 100.0) / 100.0);
	 * 
	 * String finyear=""; int year = Calendar.getInstance().get(Calendar.YEAR);
	 * 
	 * int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
	 * System.out.println("Financial month : " + month); if (month < 3) { finyear=
	 * (year - 1) + "-" + year; } else { finyear= year + "-" + (year + 1); }
	 * map1.put("finyear",finyear);
	 * //map1.put("totalValue",storeQuotationT.getUnitRate()*storeQuotationT.
	 * getQtyRequried());//need to change data.put(count++, map1);
	 * 
	 * map.put("lpTypeFlag",
	 * storeQuotationT.getStoreQuotationM().getLpTypeFlag()!=null?storeQuotationT.
	 * getStoreQuotationM().getLpTypeFlag():""); map.put("data", data);
	 * map.put("count", count-1); map.put("sanctionData", sanctionData);
	 * map.put("status", status);
	 * 
	 * } }
	 * 
	 * } return map; }
	 * 
	 * @Override public String submitSanctionOrder(HashMap<String, Object> jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject jsonObj = new JSONObject(); long
	 * storeSoMId=lPProcessDao.submitSanctionOrder(jsondata); if (storeSoMId != 0 &&
	 * storeSoMId != -1) { jsonObj.put("storeSoMId", storeSoMId);
	 * jsonObj.put("status", 1); jsonObj.put("msg", "Record Saved Successfully");
	 * 
	 * }else if (storeSoMId == -1) { jsonObj.put("status", 0); jsonObj.put("msg",
	 * "Something went wrong Record not Saved ");
	 * 
	 * }else { jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); }
	 * 
	 * return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getSanctionApprovalList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<StoreSoM> storeSOList =
	 * new ArrayList<StoreSoM>(); List<Map<String,Object>> responseList = new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); long pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * responseMap = lPProcessDao.getSanctionApprovalList(pageNo,requestData); int
	 * count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * storeSOList = (List<StoreSoM>) responseMap.get("storeSoMList"); if
	 * (!storeSOList.isEmpty() && storeSOList.size() > 0) { for(StoreSoM storeSoM :
	 * storeSOList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * map1.put("reqNo", storeSoM.getRfpNo()); map1.put("id", storeSoM.getSoMId());
	 * map1.put("lpType",
	 * storeSoM.getStoreQuotationM().getStoreBudgetaryM().getMasStoreLpType().
	 * getLpName()); map1.put("leastCost", storeSoM.getTotalAmt());
	 * map1.put("storeSoMId", storeSoM.getSoMId()); map1.put("quotationApprovedBy",
	 * storeSoM.getStoreQuotationM().getApprovedBy().getFirstName());
	 * map1.put("sanctionOrderCreatedBy",storeSoM.getLastChgBy().getFirstName());
	 * 
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map;
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getSanctionData(HashMap<String, String>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub List<StoreSoM> storeSoMList = new
	 * ArrayList<StoreSoM>(); List<Map<String,Object>> responseList = new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); JSONObject json = new JSONObject(jsondata); long
	 * storeSoMId = Long.parseLong(json.getString("storeSoMId")); responseMap =
	 * lPProcessDao.getSanctionData(jsondata); int count = 1; String pvmsNo=""; if
	 * (responseMap.size() > 0 && !responseMap.isEmpty()) { storeSoMList =
	 * (List<StoreSoM>) responseMap.get("storeSoMList"); if (!storeSoMList.isEmpty()
	 * && storeSoMList.size() > 0) { for(StoreSoM storeSoM : storeSoMList) {
	 * Map<String,Object>map1=new HashMap<String, Object>();
	 * 
	 * map1.put("reqNo", storeSoM.getRfpNo()); map1.put("quotationNo",
	 * storeSoM.getQuatationNo()); map1.put("storeSoMId", storeSoM.getSoMId());
	 * if(storeSoM.getRidcId()!=null) map1.put("ridcId", storeSoM.getRidcId()); else
	 * map1.put("ridcId", ""); map1.put("fileNo", storeSoM.getFileNo());
	 * map1.put("createSanctionDate", storeSoM.getOrderDate());
	 * map1.put("orderPurpose", storeSoM.getOrderPurpose()); map1.put("vendorName",
	 * storeSoM.getMasStoreSupplier().getSupplierName());
	 * 
	 * map1.put("quotationOfItem", storeSoM.getQuatationOfItem());
	 * map1.put("valueOfOrder", storeSoM.getOrderValue());
	 * map1.put("refOfGovtAuthority", storeSoM.getRefOfGovtAuthority());
	 * 
	 * map1.put("issueUnder", storeSoM.getIssuedUnder()); map1.put("payingAgency",
	 * storeSoM.getPayingAgency()); map1.put("bookedBudget",
	 * storeSoM.getBookedBudget());
	 * 
	 * map1.put("videNoteNumber", storeSoM.getVideNoteNumber());
	 * map1.put("uoNumber", storeSoM.getUoNo()); map1.put("commWithSanction",
	 * storeSoM.getCommWithSanc());
	 * 
	 * 
	 * map1.put("sanctionIssued", storeSoM.getSancOverruling());
	 * map1.put("totalAmount", storeSoM.getTotalAmt()); map1.put("totalAmountWords",
	 * storeSoM.getTotalAmtInWords());
	 * 
	 * if(storeSoM.getLpTypeFlag()!=null &&
	 * storeSoM.getLpTypeFlag().equalsIgnoreCase("B")) { map1.put("lpTypeFlag",
	 * storeSoM.getLpTypeFlag()); }else { map1.put("lpTypeFlag", ""); }
	 * 
	 * map.put("data", map1); List<StoreSoT> storeSoTList =
	 * storeSoM.getStoreSoTList(); for(StoreSoT storeSoT : storeSoTList) {
	 * Map<String,Object>map2=new HashMap<String, Object>();
	 * 
	 * pvmsNo=storeSoT.getMasStoreItem().getNomenclature() + "[" +
	 * storeSoT.getMasStoreItem().getPvmsNo()+"]"; map2.put("NomPvmsNo", pvmsNo);
	 * map2.put("accountingUnit",
	 * storeSoT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
	 * map2.put("requiredQty",storeSoT.getItemQty());
	 * map2.put("itemId",storeSoT.getMasStoreItem().getItemId());
	 * map2.put("pvmsNo",storeSoT.getMasStoreItem().getPvmsNo());
	 * map2.put("unitrate",storeSoT.getUnitRate());
	 * map2.put("totalValue",storeSoT.getUnitRate().multiply(storeSoT.getItemQty()))
	 * ;//need to change data.put(count++, map2);
	 * 
	 * 
	 * } } map.put("itemdata", data); map.put("count", count-1); }
	 * 
	 * } return map; }
	 * 
	 * @SuppressWarnings("static-access")
	 * 
	 * @Override public String approveSanctionOrder(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub List<StoreQuotationM> storeQuotationMList = new
	 * ArrayList<StoreQuotationM>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); JSONObject json = new JSONObject(jsondata);
	 * JSONObject jsonObj = new JSONObject(jsondata); MasStoreLpc masStoreLpc = new
	 * MasStoreLpc(); Date quotationDate=null; Date toDate2=null; String budId="";
	 * int flag=0; long masStoreLpcId=0; int status=0; String storeSoMId1=""; if
	 * (jsondata != null) { if(!jsondata.get("storeSoMId").toString().isEmpty()) {
	 * String storeSoMId = jsondata.get("storeSoMId").toString(); storeSoMId1 =
	 * dispenceryService.getReplaceString(storeSoMId);
	 * 
	 * }
	 * 
	 * String itemIdNom = jsondata.get("itemIdNom").toString(); String itemIdNom1 =
	 * dispenceryService.getReplaceString(itemIdNom); String[] itemIdNomValue =
	 * itemIdNom1.split(","); int itemLength = itemIdNomValue.length;
	 * 
	 * Users users=new Users(); if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString(); users.setUserId(Long.parseLong(userId)); }
	 * String action=""; String approvalRemark=""; StoreSoM
	 * storeSoM=lPProcessDao.getStoreSoM(Long.parseLong(storeSoMId1)); String
	 * lpTypeFlag= storeSoM.getLpTypeFlag()!=null?storeSoM.getLpTypeFlag():"";
	 * if(json.has("action")) { action = jsondata.get("action").toString(); action =
	 * dispenceryService.getReplaceString(action);
	 * if(action.equalsIgnoreCase("Approve")) { storeSoM.setStatus("A");//A=Approve
	 * 
	 * 
	 * if(!lpTypeFlag.isEmpty() && lpTypeFlag.equalsIgnoreCase("B")) { for (int j =
	 * 0; j < itemLength;j++) { long tempId =
	 * lPProcessDao.getTempIdFromBudgetarty(storeSoM.getStoreQuotationM().
	 * getStoreBudgetaryM(), Long.parseLong(itemIdNomValue[j].trim()));
	 * 
	 * lPProcessDao.updateTempTableStatus(null,tempId,"sanction"); }
	 * 
	 * }
	 * 
	 * }else { storeSoM.setStatus("R");//R=reject StoreQuotationM storeQuotationM =
	 * storeSoM.getStoreQuotationM(); storeQuotationM.setStatus("M");// int
	 * status1=lPProcessDao.updateStoreQuotationM(storeQuotationM);
	 * 
	 * // updating tempTable Status
	 * //removeDataFromTempTableForBackLp(storeQuotationM.getStoreBudgetaryM()); }
	 * if(json.has("approvalRemark")) { approvalRemark =
	 * jsondata.get("approvalRemark").toString(); approvalRemark =
	 * dispenceryService.getReplaceString(approvalRemark);
	 * 
	 * } } //storeSoM.setStatus("A");//A=Approve
	 * storeSoM.setRemarks(approvalRemark); storeSoM.setApprovedBy(users);
	 * storeSoM.setApprovedDate(new Timestamp(new Date().getTime()));
	 * storeSoM.setLastChgBy(users); storeSoM.setLastChgDate(new Timestamp(new
	 * Date().getTime())); status=lPProcessDao.updateStoreSoM(storeSoM); if(status
	 * != 0) { jsonObj.put("status", 1); jsonObj.put("storeSoMId", storeSoMId1);
	 * if(action.equalsIgnoreCase("Approve")) jsonObj.put("msg",
	 * "Sanction order has been Approved"); else
	 * if(action.equalsIgnoreCase("Reject")) jsonObj.put("msg",
	 * "Sanction order has been Rejected ");
	 * 
	 * 
	 * } else{ jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); }
	 * 
	 * 
	 * } return jsonObj.toString(); }
	 * 
	 * @Override public Map<String, Object> getLpRateByItemId(String jsondata,
	 * HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); JSONObject json = new JSONObject(jsondata); StorePoT
	 * storePoT=null; int status=0; String itemId = json.get("itemId").toString();
	 * String hospitalId = json.get("hospitalId").toString();
	 * if(json.has("departmentId")){ String departmentId =
	 * json.get("departmentId").toString(); storePoT=
	 * lPProcessDao.getLpRate(itemId,hospitalId,departmentId); if(storePoT!=null) {
	 * responseMap.put("status", 1); responseMap.put("lpRate",
	 * storePoT.getUnitRate()); responseMap.put("msg", "success"); } else {
	 * responseMap.put("status", 1); responseMap.put("lpRate", 0);
	 * responseMap.put("msg", "success"); } } else { responseMap.put("status", -1);
	 * responseMap.put("lpRate", -1); responseMap.put("msg",
	 * "Please select Department"); }
	 * 
	 * return responseMap; }
	 * 
	 * @Override public Map<Long, String> financialYearList() { // TODO
	 * Auto-generated method stub Map<Long, String> map = new HashMap<Long,
	 * String>(); List<MasStoreFinancial> financialList = new
	 * ArrayList<MasStoreFinancial>();
	 * 
	 * financialList = lPProcessDao.financialYearList(); for (MasStoreFinancial
	 * masStoreFinancial : financialList) {
	 * map.put(masStoreFinancial.getFinancialId(),
	 * masStoreFinancial.getFinancialYear()); }
	 * 
	 * return map; }
	 * 
	 * @Override public String submitFundUtilization(HashMap<String, Object>
	 * jsondata, HttpServletRequest request, HttpServletResponse response) { // TODO
	 * Auto-generated method stub JSONObject json = new JSONObject(jsondata);
	 * JSONObject jsonObj = new JSONObject(); FundUtilization fundUtilization = new
	 * FundUtilization(); Date allotmentDate2=null; String status="0"; long
	 * fundUtilizationId=0; if (jsondata != null) {
	 * if(!jsondata.get("allotmentDate").toString().isEmpty()) { String
	 * allotmentDate = jsondata.get("allotmentDate").toString(); String
	 * allotmentDate1 = dispenceryService.getReplaceString(allotmentDate);
	 * allotmentDate2= HMSUtil.convertStringDateToUtilDate(allotmentDate1,
	 * "dd/MM/yyyy"); }
	 * 
	 * 
	 * MasHospital masHospital = null; MasDepartment masDepartment=null; String
	 * hospitalId=""; String departmentId=""; if (jsondata.get("hospitalId") !=
	 * null) { hospitalId = jsondata.get("hospitalId").toString(); masHospital =
	 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); }
	 * 
	 * if (jsondata.get("departmentId") != null) { departmentId =
	 * jsondata.get("departmentId").toString(); masDepartment =
	 * dispensaryDao.getMasDepartment(Long.parseLong(departmentId)); }
	 * 
	 * String authorityNo = jsondata.get("authorityNo").toString(); String
	 * authorityNo2 = dispenceryService.getReplaceString(authorityNo);
	 * 
	 * //check Authoroty no exist or not
	 * status=lPProcessDao.checkAuthorityNoExist(authorityNo2,Long.parseLong(
	 * hospitalId),Long.parseLong(departmentId));
	 * 
	 * MasStoreFinancial masStoreFinancial=new MasStoreFinancial();
	 * if(status.equalsIgnoreCase("0")) { String finYear =
	 * jsondata.get("finYear").toString(); String finYear1 =
	 * dispenceryService.getReplaceString(finYear);
	 * masStoreFinancial.setFinancialId(Long.parseLong(finYear1));
	 * 
	 * 
	 * String amtAlloted = jsondata.get("amtAlloted").toString(); String amtAlloted1
	 * = dispenceryService.getReplaceString(amtAlloted);
	 * 
	 * String refundedAmt = jsondata.get("refundedAmt").toString(); String
	 * refundedAmt1 = dispenceryService.getReplaceString(refundedAmt);
	 * 
	 * 
	 * 
	 * Users users=new Users(); //save data in table
	 * if(jsondata.get("userId")!=null) { String userId =
	 * jsondata.get("userId").toString(); users.setUserId(Long.parseLong(userId)); }
	 * fundUtilization.setAmountAllotted(new BigDecimal(amtAlloted1.trim()));
	 * //fundUtilization.setAmountAvil(new BigDecimal(amtAlloted1.trim()));
	 * fundUtilization.setLastChgDate(new Timestamp(new Date().getTime()));
	 * fundUtilization.setLastChgBy(users);
	 * fundUtilization.setAuthorityNo(authorityNo2.trim());
	 * fundUtilization.setMasDepartment(masDepartment);
	 * fundUtilization.setMasHospital(masHospital);
	 * fundUtilization.setMasStoreFinancial(masStoreFinancial);
	 * fundUtilization.setRefundedAmount(new BigDecimal(refundedAmt1.trim()));
	 * fundUtilizationId= lPProcessDao.saveFundUtilization(fundUtilization); if
	 * (fundUtilizationId != 0) { jsonObj.put("fundUtilizationId",
	 * fundUtilizationId); jsonObj.put("status", 1); jsonObj.put("msg",
	 * "Record Saved Successfully");
	 * 
	 * } else{ jsonObj.put("status", 0); jsonObj.put("msg", "Error occured"); }
	 * 
	 * } else if(status.equalsIgnoreCase("1")){ jsonObj.put("status", 1);
	 * jsonObj.put("msg", "Authority No  Already exist "); } else{
	 * jsonObj.put("status", 0); jsonObj.put("msg", "Error occured");
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * return jsonObj.toString();
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> fundUtilizationList(HashMap<String,
	 * String> requestData, HttpServletRequest request, HttpServletResponse
	 * response) { // TODO Auto-generated method stub List<FundUtilization>
	 * fundUtilizationList = new ArrayList<FundUtilization>();
	 * List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
	 * Map<String,Object> responseMap=new HashMap<String, Object>(); Map<Integer,
	 * Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
	 * Map<String, Object> map = new HashMap<String, Object>(); long pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); } String hospitalId="";
	 * String departmentId=""; if (requestData.get("hospitalId") != null) {
	 * hospitalId = requestData.get("hospitalId").toString(); }
	 * 
	 * if (requestData.get("departmentId") != null) { departmentId =
	 * requestData.get("departmentId").toString(); }
	 * 
	 * responseMap =
	 * lPProcessDao.fundUtilizationList(pageNo,requestData,Long.parseLong(hospitalId
	 * ),Long.parseLong(departmentId)); int count = 1; if (responseMap.size() > 0 &&
	 * !responseMap.isEmpty()) { fundUtilizationList = (List<FundUtilization>)
	 * responseMap.get("fundUtilizationList"); if (!fundUtilizationList.isEmpty() &&
	 * fundUtilizationList.size() > 0) { for(FundUtilization fundUtilization :
	 * fundUtilizationList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * map1.put("id", fundUtilization.getId());
	 * map1.put("year",fundUtilization.getMasStoreFinancial().getFinancialYear());
	 * map1.put("finYearId",fundUtilization.getMasStoreFinancial().getFinancialId())
	 * ; map1.put("authorityNo",fundUtilization.getAuthorityNo());
	 * map1.put("amountAlloted",fundUtilization.getAmountAllotted());
	 * map1.put("refundedAmount",fundUtilization.getRefundedAmount());
	 * map1.put("amountAvailable",
	 * fundUtilization.getAmountAvil()!=null?fundUtilization.getAmountAvil():"");
	 * data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * } } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map;
	 * 
	 * }
	 * 
	 * 
	 * @Override public Map<String, Object>
	 * getFundUtilizationInShownYear(Map<String, String> requestData) {
	 * Map<String,Object> responseMap = new HashMap<String, Object>();
	 * List<HashMap<String, Object>> utilizedFundData = new
	 * ArrayList<HashMap<String, Object>>();
	 * 
	 * long hospitalId = Long.parseLong(requestData.get("hospitalId")); long
	 * departmentId = Long.parseLong(requestData.get("departmentId")); long yearId =
	 * Long.parseLong(requestData.get("yearId")); int pageNo =
	 * Integer.parseInt(requestData.get("pageNo"));
	 * 
	 * responseMap=lPProcessDao.getFundUtilizationInShownYear(hospitalId,
	 * departmentId,yearId,pageNo);
	 * 
	 * return responseMap;
	 * 
	 * }
	 * 
	 * @Override public Map<String, Object> getSanctionList(HashMap<String, String>
	 * requestData, HttpServletRequest request, HttpServletResponse response) { //
	 * TODO Auto-generated method stub List<StoreSoM> storeSOList = new
	 * ArrayList<StoreSoM>(); List<Map<String,Object>> responseList = new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> responseMap=new
	 * HashMap<String, Object>(); Map<Integer, Map<String, Object>> data = new
	 * HashMap<Integer, Map<String, Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); long pageNo=0;
	 * 
	 * if(!requestData.get("PN").toString().isEmpty() &&
	 * requestData.get("PN")!=null) { pageNo
	 * =Long.parseLong(requestData.get("PN").toString()); }
	 * 
	 * 
	 * responseMap = lPProcessDao.getSanctionApprovalList(pageNo,requestData); int
	 * count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * storeSOList = (List<StoreSoM>) responseMap.get("storeSoMList"); if
	 * (!storeSOList.isEmpty() && storeSOList.size() > 0) { for(StoreSoM storeSoM :
	 * storeSOList) { Map<String,Object>map1=new HashMap<String, Object>();
	 * map1.put("reqNo", storeSoM.getRfpNo()); map1.put("id", storeSoM.getSoMId());
	 * map1.put("lpType",
	 * storeSoM.getStoreQuotationM().getStoreBudgetaryM().getMasStoreLpType().
	 * getLpName()); map1.put("leastCost", storeSoM.getTotalAmt());
	 * map1.put("storeSoMId", storeSoM.getSoMId());
	 * map1.put("sanctionOrderCreatedBy",storeSoM.getCreatedBy().getFirstName());
	 * map1.put("orderDate",HMSUtil.changeDateToddMMyyyy(storeSoM.getOrderDate()));
	 * map1.put("sanctionNumber",storeSoM.getSanctionNo());
	 * map1.put("quotationBy",storeSoM.getStoreQuotationM().getApprovedBy().
	 * getFirstName());
	 * map1.put("quotationOn",HMSUtil.changeDateToddMMyyyy(storeSoM.
	 * getStoreQuotationM().getApprovedDate())); if(storeSoM.getApprovedBy()!=null)
	 * map1.put("sanctionApprovedBy",storeSoM.getApprovedBy().getFirstName()); else
	 * map1.put("sanctionApprovedBy","");
	 * 
	 * if(storeSoM.getApprovedDate()!=null)
	 * map1.put("sanctionApprovedOn",HMSUtil.changeDateToddMMyyyy(storeSoM.
	 * getApprovedDate())); else map1.put("sanctionApprovedOn","");
	 * 
	 * map1.put("vendorName",storeSoM.getMasStoreSupplier().getSupplierName());
	 * if(storeSoM.getStatus().equalsIgnoreCase("Y"))
	 * map1.put("status","Sanction Order Created");
	 * if(storeSoM.getStatus().equalsIgnoreCase("A"))
	 * map1.put("status","Sanction Order Approved");
	 * if(storeSoM.getStatus().equalsIgnoreCase("C"))
	 * map1.put("status","Supply Order Created");
	 * if(storeSoM.getStatus().equalsIgnoreCase("R"))
	 * map1.put("status","Sanction Order Rejected"); data.put(count++, map1);
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * } Integer tm = (Integer) responseMap.get("totalMatches"); map.put("data",
	 * data); map.put("count", tm); map.put("status", "1"); return map;
	 * 
	 * }
	 * 
	 */

}
