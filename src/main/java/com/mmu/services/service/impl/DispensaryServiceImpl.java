package com.mmu.services.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.DispensaryDao;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.MasStoreSupplierNew;
import com.mmu.services.entity.MasStoreSupplierType;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientPrescriptionDt;
import com.mmu.services.entity.PatientPrescriptionHd;
import com.mmu.services.entity.StoreCoInternalIndentM;
import com.mmu.services.entity.StoreCoInternalIndentT;
import com.mmu.services.entity.StoreDoInternalIndentM;
import com.mmu.services.entity.StoreDoInternalIndentT;
import com.mmu.services.entity.StoreGrnT;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreInternalIndentT;
import com.mmu.services.entity.StoreIssueM;
import com.mmu.services.entity.StoreIssueT;
import com.mmu.services.entity.StoreItemBatchStock;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.DispensaryService;
import com.mmu.services.utils.CommonUtil;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;
@Repository
public class DispensaryServiceImpl implements DispensaryService {

	@Autowired
	DispensaryDao dispensaryDao;

	@Autowired
	GetHibernateUtils getHibernateUtils;
	

	public String submitDispenceryIndent(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		long indentMId=dispensaryDao.submitDispenceryIndent(jsondata);
		if (indentMId != 0 && indentMId != -1) {
				jsonObj.put("indentMId", indentMId);
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Record Saved Successfully");

			}else {
				if (indentMId == -1) {
			    jsonObj.put("status", 0);
				jsonObj.put("msg", "Something went wrong Record not Saved ");

			}else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Error occured");
			}
		}
		return jsonObj.toString();
		
		
		
	}

	public static String getReplaceString(String replaceValue) {
		return replaceValue.replaceAll("[\\[\\]]", "");
	}

	@Override
	public Map<String, Object> getAllListOfIndentList(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNo = 0;

		if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") != null) {
			pageNo = Long.parseLong(requestData.get("PN").toString());
		}

		responseMap = dispensaryDao.getIndentList(pageNo, requestData);
		int count = 1;
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentMList = (List<StoreInternalIndentM>) responseMap.get("storeInternalIndentMList");
			if (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0) {
				for (StoreInternalIndentM storeInternalIndentM : storeInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("indentId", storeInternalIndentM.getId());
					map1.put("indentNo", storeInternalIndentM.getDemandNo());
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentM.getDemandDate()));
					map1.put("fromDept", storeInternalIndentM.getDepartment1().getDepartmentName());// need to change
					map1.put("mmuName", storeInternalIndentM.getMasMMU().getMmuName());// need to change
					//map1.put("toDeptId", storeInternalIndentM.getDepartment2().getDepartmentId());
					map1.put("createdBy", storeInternalIndentM.getUser2().getUserName());
					if(storeInternalIndentM.getUser2().getMasUserType() != null) {
						map1.put("createdByDsg", storeInternalIndentM.getUser2().getMasUserType().getUserTypeName());
					}
					map1.put("statusId", storeInternalIndentM.getStatus());
					if (storeInternalIndentM.getStatus().equalsIgnoreCase("N"))
						map1.put("status", "Pending for Submission");
					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("Y"))
						map1.put("status", "Pending for Approval");

					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("A")) {
						map1.put("status", "Pending For Issuer");

					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("R")) {
						map1.put("status", "Rejected");

					}
					data.put(count++, map1);

				}

			}

		}
		Integer tm = (Integer) responseMap.get("totalMatches");
		map.put("data", data);
		map.put("count", tm);
		map.put("status", "1");
		return map;

	}

	@Override
	public Map<String, Object> getIndentListForTracking(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNo = 0;

		if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") != null) {
			pageNo = Long.parseLong(requestData.get("PN").toString());
		}

		responseMap = dispensaryDao.getIndentList(pageNo, requestData);
		int count = 1;
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentMList = (List<StoreInternalIndentM>) responseMap.get("storeInternalIndentMList");
			if (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0) {
				for (StoreInternalIndentM storeInternalIndentM : storeInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("indentId", storeInternalIndentM.getId());
					map1.put("indentNo", storeInternalIndentM.getDemandNo());
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentM.getDemandDate()));
					//map1.put("toDept", storeInternalIndentM.getDepartment2().getDepartmentName());// need to change
					//map1.put("toDeptId", storeInternalIndentM.getDepartment2().getDepartmentId());
					
					//map1.put("fwcParentHospitalName", storeInternalIndentM.getFwcParentMasHospital()!=null?storeInternalIndentM.getFwcParentMasHospital().getHospitalName():"");
					
					map1.put("approvedBy", "");// need to change
					map1.put("statusId", storeInternalIndentM.getStatus());
					map1.put("createdBy", storeInternalIndentM.getUser2().getUserName());
					if (storeInternalIndentM.getStatus().equalsIgnoreCase("N"))
						map1.put("status", "Pending for Submission");
					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("Y"))
						map1.put("status", "Pending for APM Approval");
					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("A")) {
						map1.put("status", "Pending For Doctor Approval");
						if(storeInternalIndentM.getApmUser() != null) {
							map1.put("approvedBy",storeInternalIndentM.getApmUser().getUserName());
						}
						//map1.put("approvedBy", storeInternalIndentM.getUser3().getUserName());// need to change
					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("R")) {
						map1.put("status", "Rejected by APM");
						if(storeInternalIndentM.getApmUser() != null) {
							map1.put("approvedBy",storeInternalIndentM.getApmUser().getUserName());
						}
					}else if (storeInternalIndentM.getStatus().equalsIgnoreCase("U")) {
						map1.put("status", "Pending For CO Approval");
						if(storeInternalIndentM.getAuditorUser() != null) {
							map1.put("approvedBy",storeInternalIndentM.getAuditorUser().getUserName());
						}
					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("X")) {
						map1.put("status", "Rejected by Auditor");
						if(storeInternalIndentM.getAuditorUser() != null) {
							map1.put("approvedBy",storeInternalIndentM.getAuditorUser().getUserName());
						}
					}
					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("C")) {
						//map1.put("status", "Pending for Issue");
						map1.put("status", "Pending for DO approval");
						//map1.put("approvedBy", storeInternalIndentM.getUser3().getUserName());// need to change
					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("Z")) {
						map1.put("status", "Rejected by CO");
						if(storeInternalIndentM.getCoUser() != null) {
							map1.put("approvedBy",storeInternalIndentM.getCoUser().getUserName());
						}
					}else if (storeInternalIndentM.getStatus().equalsIgnoreCase("I")) {
						map1.put("status", "Pending for Receiving");
						//map1.put("approvedBy", storeInternalIndentM.getUser3().getUserName());// need to change
					}else if (storeInternalIndentM.getStatus().equalsIgnoreCase("O")) {
						map1.put("status", "Received");
						//map1.put("approvedBy", storeInternalIndentM.getUser3().getUserName());// need to change
					}else if(storeInternalIndentM.getStatus().equalsIgnoreCase("P")) {
						map1.put("status", "Partially Issued");
						//map1.put("approvedBy", storeInternalIndentM.getUser3().getUserName());// need to change
					}
					
					/*
					 * else if (storeInternalIndentM.getStatus().equalsIgnoreCase("I")) {
					 * map1.put("status", "Pending for receive"); map1.put("approvedBy",
					 * storeInternalIndentM.getUser3().getUserName());// need to change } else if
					 * (storeInternalIndentM.getStatus().equalsIgnoreCase("C")) { map1.put("status",
					 * "Received"); map1.put("approvedBy",
					 * storeInternalIndentM.getUser3().getUserName());// need to change }
					 */

					data.put(count++, map1);
				}
			}
			Integer tm = (Integer) responseMap.get("totalMatches");
			map.put("data", data);
			map.put("count", tm);

		}
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentDetails(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

		List<StoreInternalIndentT> storeInternalIndentTList = new ArrayList<StoreInternalIndentT>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject(jsondata);
		long indentMid = Long.parseLong(json.getString("indentId"));
		responseMap = dispensaryDao.getIndentDetails(jsondata);
		int count = 1;
		String pvmsNo = "";
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentTList = (List<StoreInternalIndentT>) responseMap.get("storeInternalIndentTList");
			if (!storeInternalIndentTList.isEmpty() && storeInternalIndentTList.size() > 0) {
				for (StoreInternalIndentT storeInternalIndentT : storeInternalIndentTList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					/*
					 * if(storeInternalIndentT.getStoreInternalIndentM1().getStatus().
					 * equalsIgnoreCase("N")
					 * ||storeInternalIndentT.getStoreInternalIndentM1().getStatus().
					 * equalsIgnoreCase("Y")) {
					 */
					map1.put("status", storeInternalIndentT.getStoreInternalIndentM1().getStatus());
					map1.put("id", storeInternalIndentT.getId());
					map1.put("indentNo", storeInternalIndentT.getStoreInternalIndentM1().getDemandNo());
					map1.put("indentMid", storeInternalIndentT.getStoreInternalIndentM1().getId());
					map1.put("indentdate", HMSUtil
							.changeDateToddMMyyyy(storeInternalIndentT.getStoreInternalIndentM1().getDemandDate()));
					map1.put("fromDept",
							storeInternalIndentT.getStoreInternalIndentM1().getDepartment1().getDepartmentName());// need
																													// to
																													// change
					//map1.put("toDept",	storeInternalIndentT.getStoreInternalIndentM1().getDepartment2().getDepartmentName());// need
																													// to
																													// change
					//map1.put("toDeptId",storeInternalIndentT.getStoreInternalIndentM1().getDepartment2().getDepartmentId());
					map1.put("createdBy", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getUserName());
					if(storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType() != null) {
						map1.put("createdByDsg", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType().getUserTypeName());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getAuditorUser() != null) {
						map1.put("approvedBy", storeInternalIndentT.getStoreInternalIndentM1().getAuditorUser().getUserName());
						if(storeInternalIndentT.getStoreInternalIndentM1().getAuditorUser().getMasUserType() != null) {
							map1.put("approvedByDsg", storeInternalIndentT.getStoreInternalIndentM1().getAuditorUser().getMasUserType().getUserTypeName());
						}
					}
					pvmsNo = storeInternalIndentT.getMasStoreItem().getNomenclature() + "["
							+ storeInternalIndentT.getMasStoreItem().getPvmsNo() + "]";
					map1.put("NomPvmsNo", pvmsNo);
					map1.put("accountingUnit",
							storeInternalIndentT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
					map1.put("requiredQty", storeInternalIndentT.getQtyRequest());
					map1.put("availableStock", storeInternalIndentT.getAvailableStock());
					map1.put("stockDisp", storeInternalIndentT.getDispStock());
					map1.put("stockStore", storeInternalIndentT.getStoresStock());
					map1.put("remark", storeInternalIndentT.getReasonForDemand());
					map1.put("itemId", storeInternalIndentT.getMasStoreItem().getItemId());
					map1.put("pvmsNo", storeInternalIndentT.getMasStoreItem().getPvmsNo());
					
					if(storeInternalIndentT.getStoreInternalIndentM1().getApmUser() != null) {
						map1.put("apmName", storeInternalIndentT.getStoreInternalIndentM1().getApmUser().getUserName());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getApmDate() != null) {
						map1.put("apmDate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentT.getStoreInternalIndentM1().getApmDate()));
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getApmRemarks() != null) {
						map1.put("apmRemarks", storeInternalIndentT.getStoreInternalIndentM1().getApmRemarks());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getApmFlag() != null) {
						String flag = storeInternalIndentT.getStoreInternalIndentM1().getApmFlag();
						String action = "";
						if(flag.equalsIgnoreCase("Y")) {
							action = "Approved";
						}else {
							action = "Rejected";
						}
						map1.put("apmAction", action);
					}
					
					
					if(storeInternalIndentT.getStoreInternalIndentM1().getAuditorUser() != null) {
						map1.put("auditorName", storeInternalIndentT.getStoreInternalIndentM1().getAuditorUser().getUserName());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getAuditorDate() != null) {
						map1.put("auditorDate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentT.getStoreInternalIndentM1().getAuditorDate()));
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getAuditorRemarks() != null) {
						map1.put("auditorRemarks", storeInternalIndentT.getStoreInternalIndentM1().getAuditorRemarks());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getAuditorFlag() != null) {
						String flag = storeInternalIndentT.getStoreInternalIndentM1().getAuditorFlag();
						String action = "";
						if(flag.equalsIgnoreCase("Y")) {
							action = "Approved";
						}else {
							action = "Rejected";
						}
						map1.put("auditorAction", action);
					}
					
					if(storeInternalIndentT.getStoreInternalIndentM1().getCoUser() != null) {
						map1.put("coName", storeInternalIndentT.getStoreInternalIndentM1().getCoUser().getUserName());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getCoDate() != null) {
						map1.put("coDate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentT.getStoreInternalIndentM1().getCoDate()));
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getCoRemarks() != null) {
						map1.put("coRemarks", storeInternalIndentT.getStoreInternalIndentM1().getCoRemarks());
					}
					map1.put("receivedQty", HMSUtil.convertNullToEmptyString(storeInternalIndentT.getQtyReceived()));
					if(storeInternalIndentT.getStoreInternalIndentM1().getCoFlag() != null) {
						String coFlag = storeInternalIndentT.getStoreInternalIndentM1().getCoFlag();
						String action = "";
						if(coFlag.equalsIgnoreCase("Y")) {
							action = "Approved";
						}else {
							action = "Rejected";
						}
						map1.put("coAction", action);
					}
					List<Map<String,Object>> receivedDetail = new ArrayList<Map<String,Object>>(); 
					if(!storeInternalIndentT.getStoreIssueT().isEmpty()) {
						List<StoreIssueT> issueTList = storeInternalIndentT.getStoreIssueT();
						for(StoreIssueT t : issueTList) {
							Map<String,Object> storeIssueTData = new HashMap<String, Object>();
							storeIssueTData.put("issueNo", t.getStoreIssueM().getIssueNo());
							storeIssueTData.put("issueDate", HMSUtil.changeDateToddMMyyyy(t.getIssueDate()));
							storeIssueTData.put("batchNo", HMSUtil.convertNullToEmptyString(t.getBatchNo()));
							storeIssueTData.put("receivedQty", HMSUtil.convertNullToEmptyString(t.getQtyReceived()));
							receivedDetail.add(storeIssueTData);
						}
					}
					map1.put("receivedDetail",receivedDetail);
					
					data.put(count++, map1);	

					map.put("data", data);
					map.put("count", count - 1);

				}
			}

		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentDetailsCo(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

		List<StoreInternalIndentT> storeInternalIndentTList = new ArrayList<StoreInternalIndentT>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject(jsondata);
		long indentMid = Long.parseLong(json.getString("indentId"));
		responseMap = dispensaryDao.getIndentDetailsCo(jsondata);
		int count = 1;
		String pvmsNo = "";
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentTList = (List<StoreInternalIndentT>) responseMap.get("storeInternalIndentTList");
			if (!storeInternalIndentTList.isEmpty() && storeInternalIndentTList.size() > 0) {
				for (StoreInternalIndentT storeInternalIndentT : storeInternalIndentTList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					/*
					 * if(storeInternalIndentT.getStoreInternalIndentM1().getStatus().
					 * equalsIgnoreCase("N")
					 * ||storeInternalIndentT.getStoreInternalIndentM1().getStatus().
					 * equalsIgnoreCase("Y")) {
					 */
					map1.put("status", storeInternalIndentT.getStoreInternalIndentM1().getStatus());
					map1.put("id", storeInternalIndentT.getId());
					map1.put("indentNo", storeInternalIndentT.getStoreInternalIndentM1().getDemandNo());
					map1.put("indentMid", storeInternalIndentT.getStoreInternalIndentM1().getId());
					map1.put("indentdate", HMSUtil
							.changeDateToddMMyyyy(storeInternalIndentT.getStoreInternalIndentM1().getDemandDate()));
					map1.put("fromDept",
							storeInternalIndentT.getStoreInternalIndentM1().getDepartment1().getDepartmentName());// need
																													// to
																													// change
					//map1.put("toDept",	storeInternalIndentT.getStoreInternalIndentM1().getDepartment2().getDepartmentName());// need
																													// to
																													// change
					//map1.put("toDeptId",storeInternalIndentT.getStoreInternalIndentM1().getDepartment2().getDepartmentId());
					map1.put("createdBy", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getUserName());
					if(storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType() != null) {
						map1.put("createdByDsg", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType().getUserTypeName());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getApmUser() != null) {
						map1.put("approvedBy", storeInternalIndentT.getStoreInternalIndentM1().getApmUser().getUserName());
						if(storeInternalIndentT.getStoreInternalIndentM1().getApmUser().getMasUserType() != null) {
							map1.put("approvedByDsg", storeInternalIndentT.getStoreInternalIndentM1().getApmUser().getMasUserType().getUserTypeName());
						}
					}
					pvmsNo = storeInternalIndentT.getMasStoreItem().getNomenclature() + "["
							+ storeInternalIndentT.getMasStoreItem().getPvmsNo() + "]";
					map1.put("NomPvmsNo", pvmsNo);
					map1.put("accountingUnit",
							storeInternalIndentT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
					map1.put("requiredQty", storeInternalIndentT.getQtyRequest());
					map1.put("availableStock", storeInternalIndentT.getAvailableStock());
					map1.put("stockDisp", storeInternalIndentT.getDispStock());
					map1.put("stockStore", storeInternalIndentT.getStoresStock());
					map1.put("remark", storeInternalIndentT.getReasonForDemand());
					map1.put("itemId", storeInternalIndentT.getMasStoreItem().getItemId());
					map1.put("pvmsNo", storeInternalIndentT.getMasStoreItem().getPvmsNo());
				
					map1.put("issuedQty", storeInternalIndentT.getQtyIssued() == null ? 0:storeInternalIndentT.getQtyIssued());
					if(storeInternalIndentT.getStoreInternalIndentM1().getCoFlag() != null) {
						String coFlag = storeInternalIndentT.getStoreInternalIndentM1().getCoFlag();
						String action = "";
						if(coFlag.equalsIgnoreCase("Y")) {
							action = "Approved";
						}else {
							action = "Rejected";
						}
						map1.put("coAction", action);
					}
					
					data.put(count++, map1);	

					map.put("data", data);
					map.put("count", count - 1);

				}
			}

		}
		return map;
	}

	@Override
	public Map<String, Object> deleteIndentItems(String jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Map<String, Object> responseMap = new HashMap<String, Object>();
		JSONObject json = new JSONObject(jsondata);
		String indentArr = json.get("aParam").toString();
		int status = 0;
		String indentArr1 = getReplaceString(indentArr);
		String[] indentArr2 = indentArr1.split(",");
		for (String indentTid : indentArr2) {

			long indentId = Long.parseLong(indentTid.substring(1, indentTid.length() - 1));
			status = dispensaryDao.deleteIndentItem(indentId);
		}
		if (status == 1) {
			responseMap.put("status", status);
			responseMap.put("msg", "Data has been Deleted");
		} else if (status == 0) {
			responseMap.put("status", status);
			responseMap.put("msg", "Error has been occured");
		}
		return responseMap;
	}

	@Override
	public String updateIndentDispencery(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONObject json = new JSONObject(jsondata);
		StoreInternalIndentM storeInternalIndentM = new StoreInternalIndentM();

		String btnvalue = "";
		String btnvalue1 = "";
		String submitValue = "";
		String submitValue1 = "";

		if (jsondata != null) {
			if (!jsondata.get("indentDate").toString().isEmpty() && jsondata.get("indentDate") != null) {
				String indentDate = jsondata.get("indentDate").toString();
				String indentDate1 = getReplaceString(indentDate);
				Date indentDate2 = HMSUtil.convertStringDateToUtilDate(indentDate1, "dd/MM/yyyy");
				storeInternalIndentM.setDemandDate(new Timestamp(indentDate2.getTime()));
			}
			if (json.has("save")) {
				btnvalue = jsondata.get("save").toString();
				btnvalue1 = getReplaceString(btnvalue);
			}
			if (json.has("submit")) {
				btnvalue = jsondata.get("submit").toString();
				btnvalue1 = getReplaceString(btnvalue);
			}

			/*
			 * String departmentId = jsondata.get("departmentId").toString(); String
			 * departmentId1 = getReplaceString(departmentId); long store =
			 * Long.parseLong(departmentId1);
			 */

			String nomenclature = jsondata.get("nomenclature").toString();
			String nomenclature1 = getReplaceString(nomenclature);
			String[] nomenclatureValue = nomenclature1.split(",");

			String requiredQty = jsondata.get("requiredQty").toString();
			String requiredQty1 = getReplaceString(requiredQty);
			String[] requiredQtyValue = requiredQty1.split(",");
			int itemLength = requiredQtyValue.length;

			String accountingUnit1 = jsondata.get("accountingUnit1").toString();
			String accountingUnit = getReplaceString(accountingUnit1);
			String[] accountingUnitValue = accountingUnit.split(",");

			String availableStock = jsondata.get("availableStock").toString();
			String availableStock1 = getReplaceString(availableStock);
			String[] availableStockValue = availableStock1.split(",");

			String stockInDispencery = jsondata.get("stockInDispencery").toString();
			String stockInDispencery1 = getReplaceString(stockInDispencery);
			String[] stockInDispenceryValue = stockInDispencery1.split(",");

			String stockInStore = jsondata.get("stockInStore").toString();
			String stockInStore1 = getReplaceString(stockInStore);
			String[] stockInStoreValue = stockInStore1.split(",");

			String remarks1 = jsondata.get("remarks1").toString();
			String remarks = getReplaceString(remarks1);
			String[] remarks1Value = remarks.split(",");

			String itemIdNom = jsondata.get("itemIdNom1").toString();
			String itemIdNom1 = getReplaceString(itemIdNom);
			String[] itemIdNomValue = itemIdNom1.split(",");

			String pvmsNo1 = jsondata.get("pvmsNo1").toString();
			String pvmsNo = getReplaceString(pvmsNo1);
			String[] pvmsNo1Value = pvmsNo.split(",");

			String indent = jsondata.get("indentId1").toString();
			String indentId1 = getReplaceString(indent);
			String[] indentList = indentId1.split(",");

			String indentMid = jsondata.get("indentMid1").toString();
			String indentMid1 = getReplaceString(indentMid);
			String[] indentMid1List = indentMid1.split(",");
			long indentmId = Long.parseLong(indentMid1List[0].trim());
			MasStoreItem masStoreItem = null;
			MasHospital masHospital = null;

			String dept1 =  jsondata.get("loginDepartmentId").toString();;//come from session
			long dept=Long.parseLong(dept1);
			Users users = null;
			MasDepartment masDepartment = null;
			//MasDepartment masStore = null;

			masDepartment = dispensaryDao.getMasDepartment(dept);
			//masStore = dispensaryDao.getMasDepartment(store);
			// save data in table
			//if (jsondata.get("userId") != null) {
				String userId = jsondata.get("userId").toString();
				users = dispensaryDao.getUser(Long.parseLong(userId));
			//}
			if (jsondata.get("mmuId") != null) {
				String mmuId = jsondata.get("mmuId").toString();
				//masHospital = dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId));
			}

			Date date = new Date();
			long indentMId = 0;
			long indentId = 0;

			String action = "";

			// update Indent
			// StoreInternalIndentM storeInternalIndentM1=null;
			storeInternalIndentM = dispensaryDao.getStoreInternalIndentM(indentmId);
			if (storeInternalIndentM != null) {
				/// for Approve Indent

				String approvalRemark = "";
				if (json.has("action")) {
					action = jsondata.get("action").toString();
					action = getReplaceString(action);
					if (action.equalsIgnoreCase("Approve")) {
						storeInternalIndentM.setStatus("A");
						storeInternalIndentM.setApmId(users.getUserId());
						storeInternalIndentM.setApmFlag("Y");
						storeInternalIndentM.setApmDate(new Date());
					}
					else {
						storeInternalIndentM.setStatus("R");
						storeInternalIndentM.setApprovalDate(new Timestamp(date.getTime()));
						storeInternalIndentM.setUser3(users); // need to chk
						storeInternalIndentM.setLastChgDate(new Timestamp(date.getTime()));
						storeInternalIndentM.setApmId(users.getUserId());
						storeInternalIndentM.setApmFlag("N");
						storeInternalIndentM.setApmDate(new Date());
					}
					
					storeInternalIndentM.setUser1(users);
					if (json.has("approvalRemark")) {
						approvalRemark = jsondata.get("approvalRemark").toString();
						approvalRemark = getReplaceString(approvalRemark);
						storeInternalIndentM.setApprovalRemarks(approvalRemark);
						storeInternalIndentM.setApmRemarks(approvalRemark);

					}

				}

				// End
				else {
					storeInternalIndentM.setLastChgDate(new Timestamp(date.getTime()));
					storeInternalIndentM.setDepartment1(masDepartment);// need to change
					if (btnvalue1.equalsIgnoreCase("save"))
						storeInternalIndentM.setStatus("N");// n=save,y=submit,a=approve,r=reject
					if (btnvalue1.equalsIgnoreCase("submit"))
						storeInternalIndentM.setStatus("Y");
					//storeInternalIndentM.setDepartment2(masStore);
					storeInternalIndentM.setUser1(users);
					storeInternalIndentM.setUser2(users);
					
				}
				dispensaryDao.updateStoreInternalIndentM(storeInternalIndentM);
			}
			// StoreInternalIndentT storeInternalIndentT1=null;
			for (int i = 0; i < itemLength; i++) {
				StoreInternalIndentT storeInternalIndentT = new StoreInternalIndentT();
				if (!indentList[i].trim().isEmpty() && indentList[i] != null) {
					storeInternalIndentT = dispensaryDao.getStoreInternalIndentT(Long.parseLong(indentList[i].trim()));
					// storeInternalIndentT.setId(Long.parseLong(indentList[i].trim()));
				}
				if (requiredQtyValue[i] != null && !requiredQtyValue[i].isEmpty())
					storeInternalIndentT.setQtyRequest(Long.parseLong(requiredQtyValue[i].trim()));

				if (remarks1Value[i] != null && !remarks1Value[i].isEmpty())
					storeInternalIndentT.setReasonForDemand(remarks1Value[i].trim());

				if (itemIdNomValue[i] != null && !itemIdNomValue[i].isEmpty()) {
					masStoreItem = dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[i].trim()));
					storeInternalIndentT.setMasStoreItem(masStoreItem);
				}
				if (availableStockValue[i] != null && !availableStockValue[i].trim().isEmpty()) {
					storeInternalIndentT.setAvailableStock(Long.parseLong(availableStockValue[i].trim()));
				}
				if (stockInDispenceryValue[i] != null && !stockInDispenceryValue[i].trim().isEmpty()) {
					storeInternalIndentT.setDispStock(Long.parseLong(stockInDispenceryValue[i].trim()));
				}
				if (stockInStoreValue[i] != null && !stockInStoreValue[i].trim().isEmpty()) {
					storeInternalIndentT.setStoresStock(Long.parseLong(stockInStoreValue[i].trim()));
				}
				storeInternalIndentT.setDepartment1(masDepartment);// need to chk
				storeInternalIndentT.setStoreInternalIndentM1(storeInternalIndentM);// need to chk
				indentId = dispensaryDao.saveOrUpdatSstoreInternalIndentT(storeInternalIndentT);
			}

			// String dtObj = dispensaryDao.saveDispensaryIndent(jsondata);

			if (indentId != 0) {
				jsonObj.put("indentMId", indentmId);
				jsonObj.put("status", 1);
				if (action.equalsIgnoreCase("") || action.trim().isEmpty())
					jsonObj.put("msg", "Record Updated Successfully");
				if (action.equalsIgnoreCase("Approve"))
					jsonObj.put("msg", "Indent has been Approved");
				else if (action.equalsIgnoreCase("Reject"))
					jsonObj.put("msg", "Indent has been Rejected");

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Error occured");
			}
		}
		return jsonObj.toString();
	}

	
	@Override
	public String updateIndentDispenceryByAuditor(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONObject json = new JSONObject(jsondata);
		StoreInternalIndentM storeInternalIndentM = new StoreInternalIndentM();

		String btnvalue = "";
		String btnvalue1 = "";
		String submitValue = "";
		String submitValue1 = "";

		if (jsondata != null) {
			if (!jsondata.get("indentDate").toString().isEmpty() && jsondata.get("indentDate") != null) {
				String indentDate = jsondata.get("indentDate").toString();
				String indentDate1 = getReplaceString(indentDate);
				Date indentDate2 = HMSUtil.convertStringDateToUtilDate(indentDate1, "dd/MM/yyyy");
				storeInternalIndentM.setDemandDate(new Timestamp(indentDate2.getTime()));
			}
			if (json.has("save")) {
				btnvalue = jsondata.get("save").toString();
				btnvalue1 = getReplaceString(btnvalue);
			}
			if (json.has("submit")) {
				btnvalue = jsondata.get("submit").toString();
				btnvalue1 = getReplaceString(btnvalue);
			}

			/*
			 * String departmentId = jsondata.get("departmentId").toString(); String
			 * departmentId1 = getReplaceString(departmentId); long store =
			 * Long.parseLong(departmentId1);
			 */

			String nomenclature = jsondata.get("nomenclature").toString();
			String nomenclature1 = getReplaceString(nomenclature);
			String[] nomenclatureValue = nomenclature1.split(",");

			String requiredQty = jsondata.get("requiredQty").toString();
			String requiredQty1 = getReplaceString(requiredQty);
			String[] requiredQtyValue = requiredQty1.split(",");
			int itemLength = requiredQtyValue.length;

			String accountingUnit1 = jsondata.get("accountingUnit1").toString();
			String accountingUnit = getReplaceString(accountingUnit1);
			String[] accountingUnitValue = accountingUnit.split(",");

			String availableStock = jsondata.get("availableStock").toString();
			String availableStock1 = getReplaceString(availableStock);
			String[] availableStockValue = availableStock1.split(",");

			String stockInDispencery = jsondata.get("stockInDispencery").toString();
			String stockInDispencery1 = getReplaceString(stockInDispencery);
			String[] stockInDispenceryValue = stockInDispencery1.split(",");

			String stockInStore = jsondata.get("stockInStore").toString();
			String stockInStore1 = getReplaceString(stockInStore);
			String[] stockInStoreValue = stockInStore1.split(",");

			String remarks1 = jsondata.get("remarks1").toString();
			String remarks = getReplaceString(remarks1);
			String[] remarks1Value = remarks.split(",");

			String itemIdNom = jsondata.get("itemIdNom1").toString();
			String itemIdNom1 = getReplaceString(itemIdNom);
			String[] itemIdNomValue = itemIdNom1.split(",");

			String pvmsNo1 = jsondata.get("pvmsNo1").toString();
			String pvmsNo = getReplaceString(pvmsNo1);
			String[] pvmsNo1Value = pvmsNo.split(",");

			String indent = jsondata.get("indentId1").toString();
			String indentId1 = getReplaceString(indent);
			String[] indentList = indentId1.split(",");

			String indentMid = jsondata.get("indentMid1").toString();
			String indentMid1 = getReplaceString(indentMid);
			String[] indentMid1List = indentMid1.split(",");
			long indentmId = Long.parseLong(indentMid1List[0].trim());
			MasStoreItem masStoreItem = null;
			MasHospital masHospital = null;

			String dept1 =  jsondata.get("loginDepartmentId").toString();;//come from session
			long dept=Long.parseLong(dept1);
			Users users = null;
			MasDepartment masDepartment = null;
			//MasDepartment masStore = null;

			masDepartment = dispensaryDao.getMasDepartment(dept);
			//masStore = dispensaryDao.getMasDepartment(store);
			// save data in table
			//if (jsondata.get("userId") != null) {
				String userId = jsondata.get("userId").toString();
				users = dispensaryDao.getUser(Long.parseLong(userId));
			//}
			if (jsondata.get("mmuId") != null) {
				String mmuId = jsondata.get("mmuId").toString();
				//masHospital = dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId));
			}

			Date date = new Date();
			long indentMId = 0;
			long indentId = 0;

			String action = "";

			// update Indent
			// StoreInternalIndentM storeInternalIndentM1=null;
			storeInternalIndentM = dispensaryDao.getStoreInternalIndentM(indentmId);
			if (storeInternalIndentM != null) {
				/// for Approve Indent

				String approvalRemark = "";
				if (json.has("action")) {
					action = jsondata.get("action").toString();
					action = getReplaceString(action);
					if (action.equalsIgnoreCase("Approve")) {
						storeInternalIndentM.setStatus("U");
						storeInternalIndentM.setAuditorId(users.getUserId());
						storeInternalIndentM.setAuditorFlag("Y");
						storeInternalIndentM.setAuditorDate(new Date());
					}
					else {
						storeInternalIndentM.setStatus("X");
						storeInternalIndentM.setApprovalDate(new Timestamp(date.getTime()));
						storeInternalIndentM.setUser3(users); // need to chk
						storeInternalIndentM.setLastChgDate(new Timestamp(date.getTime()));
						storeInternalIndentM.setAuditorId(users.getUserId());
						storeInternalIndentM.setAuditorFlag("N");
						storeInternalIndentM.setAuditorDate(new Date());
					}
					
					storeInternalIndentM.setUser1(users);
					if (json.has("approvalRemark")) {
						approvalRemark = jsondata.get("approvalRemark").toString();
						approvalRemark = getReplaceString(approvalRemark);
						storeInternalIndentM.setApprovalRemarks(approvalRemark);
						storeInternalIndentM.setAuditorRemarks(approvalRemark);

					}

				}

				// End
				else {
					storeInternalIndentM.setLastChgDate(new Timestamp(date.getTime()));
					storeInternalIndentM.setDepartment1(masDepartment);// need to change
					if (btnvalue1.equalsIgnoreCase("save"))
						storeInternalIndentM.setStatus("N");// n=save,y=submit,a=approve,r=reject
					if (btnvalue1.equalsIgnoreCase("submit"))
						storeInternalIndentM.setStatus("Y");
					//storeInternalIndentM.setDepartment2(masStore);
					storeInternalIndentM.setUser1(users);
					storeInternalIndentM.setUser2(users);
					
				}
				dispensaryDao.updateStoreInternalIndentM(storeInternalIndentM);
			}
			// StoreInternalIndentT storeInternalIndentT1=null;
			for (int i = 0; i < itemLength; i++) {
				StoreInternalIndentT storeInternalIndentT = new StoreInternalIndentT();
				if (!indentList[i].trim().isEmpty() && indentList[i] != null) {
					storeInternalIndentT = dispensaryDao.getStoreInternalIndentT(Long.parseLong(indentList[i].trim()));
					// storeInternalIndentT.setId(Long.parseLong(indentList[i].trim()));
				}
				if (requiredQtyValue[i] != null && !requiredQtyValue[i].isEmpty())
					storeInternalIndentT.setQtyRequest(Long.parseLong(requiredQtyValue[i].trim()));

				if (remarks1Value[i] != null && !remarks1Value[i].isEmpty())
					storeInternalIndentT.setReasonForDemand(remarks1Value[i].trim());

				if (itemIdNomValue[i] != null && !itemIdNomValue[i].isEmpty()) {
					masStoreItem = dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[i].trim()));
					storeInternalIndentT.setMasStoreItem(masStoreItem);
				}
				if (availableStockValue[i] != null && !availableStockValue[i].trim().isEmpty()) {
					storeInternalIndentT.setAvailableStock(Long.parseLong(availableStockValue[i].trim()));
				}
				if (stockInDispenceryValue[i] != null && !stockInDispenceryValue[i].trim().isEmpty()) {
					storeInternalIndentT.setDispStock(Long.parseLong(stockInDispenceryValue[i].trim()));
				}
				if (stockInStoreValue[i] != null && !stockInStoreValue[i].trim().isEmpty()) {
					storeInternalIndentT.setStoresStock(Long.parseLong(stockInStoreValue[i].trim()));
				}
				storeInternalIndentT.setDepartment1(masDepartment);// need to chk
				storeInternalIndentT.setStoreInternalIndentM1(storeInternalIndentM);// need to chk
				indentId = dispensaryDao.saveOrUpdatSstoreInternalIndentT(storeInternalIndentT);
			}

			// String dtObj = dispensaryDao.saveDispensaryIndent(jsondata);

			if (indentId != 0) {
				jsonObj.put("indentMId", indentmId);
				jsonObj.put("status", 1);
				if (action.equalsIgnoreCase("") || action.trim().isEmpty())
					jsonObj.put("msg", "Record Updated Successfully");
				if (action.equalsIgnoreCase("Approve"))
					jsonObj.put("msg", "Indent has been Approved");
				else if (action.equalsIgnoreCase("Reject"))
					jsonObj.put("msg", "Indent has been Rejected");

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Error occured");
			}
		}
		return jsonObj.toString();
	}
	
	
	@Override
	public String updateIndentDispenceryByCO(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		JSONObject json = new JSONObject(jsondata);
		StoreInternalIndentM storeInternalIndentM = new StoreInternalIndentM();

		String btnvalue = "";
		String btnvalue1 = "";
		String submitValue = "";
		String submitValue1 = "";

		if (jsondata != null) {
			if (!jsondata.get("indentDate").toString().isEmpty() && jsondata.get("indentDate") != null) {
				String indentDate = jsondata.get("indentDate").toString();
				String indentDate1 = getReplaceString(indentDate);
				Date indentDate2 = HMSUtil.convertStringDateToUtilDate(indentDate1, "dd/MM/yyyy");
				storeInternalIndentM.setDemandDate(new Timestamp(indentDate2.getTime()));
			}
			if (json.has("save")) {
				btnvalue = jsondata.get("save").toString();
				btnvalue1 = getReplaceString(btnvalue);
			}
			if (json.has("submit")) {
				btnvalue = jsondata.get("submit").toString();
				btnvalue1 = getReplaceString(btnvalue);
			}

			String nomenclature = jsondata.get("nomenclature").toString();
			String nomenclature1 = getReplaceString(nomenclature);
			String[] nomenclatureValue = nomenclature1.split(",");

			String requiredQty = jsondata.get("requiredQty").toString();
			String requiredQty1 = getReplaceString(requiredQty);
			String[] requiredQtyValue = requiredQty1.split(",");
			int itemLength = requiredQtyValue.length;

			String accountingUnit1 = jsondata.get("accountingUnit1").toString();
			String accountingUnit = getReplaceString(accountingUnit1);
			String[] accountingUnitValue = accountingUnit.split(",");

			String availableStock = jsondata.get("availableStock").toString();
			String availableStock1 = getReplaceString(availableStock);
			String[] availableStockValue = availableStock1.split(",");
			
			String approvedQty = jsondata.get("approvedQty").toString();
			String approvedQty1 = getReplaceString(approvedQty);
			String[] approvedQtyValue = approvedQty1.split(",");

			String stockInDispencery = jsondata.get("stockInDispencery").toString();
			String stockInDispencery1 = getReplaceString(stockInDispencery);
			String[] stockInDispenceryValue = stockInDispencery1.split(",");

			String stockInStore = jsondata.get("stockInStore").toString();
			String stockInStore1 = getReplaceString(stockInStore);
			String[] stockInStoreValue = stockInStore1.split(",");

			String remarks1 = jsondata.get("remarks1").toString();
			String remarks = getReplaceString(remarks1);
			String[] remarks1Value = remarks.split(",");

			String itemIdNom = jsondata.get("itemIdNom1").toString();
			String itemIdNom1 = getReplaceString(itemIdNom);
			String[] itemIdNomValue = itemIdNom1.split(",");

			String pvmsNo1 = jsondata.get("pvmsNo1").toString();
			String pvmsNo = getReplaceString(pvmsNo1);
			String[] pvmsNo1Value = pvmsNo.split(",");

			String indent = jsondata.get("indentId1").toString();
			String indentId1 = getReplaceString(indent);
			String[] indentList = indentId1.split(",");

			String indentMid = jsondata.get("indentMid1").toString();
			String indentMid1 = getReplaceString(indentMid);
			String[] indentMid1List = indentMid1.split(",");
			long indentmId = Long.parseLong(indentMid1List[0].trim());
			MasStoreItem masStoreItem = null;
			MasHospital masHospital = null;

			String dept1 =  jsondata.get("loginDepartmentId").toString();;//come from session
			long dept=Long.parseLong(dept1);
			Users users = null;
			MasDepartment masDepartment = null;
			//MasDepartment masStore = null;

			masDepartment = dispensaryDao.getMasDepartment(dept);
			//masStore = dispensaryDao.getMasDepartment(store);
			// save data in table
			if (jsondata.get("userId") != null) {
				String userId = jsondata.get("userId").toString();
				users = dispensaryDao.getUser(Long.parseLong(userId));
			}
			/*
			 * if (jsondata.get("mmuId") != null) { String mmuId =
			 * jsondata.get("mmuId").toString(); //masHospital =
			 * dispensaryDao.gettoMasHospital(Long.parseLong(hospitalId)); }
			 */

			Date date = new Date();
			long indentMId = 0;
			long indentId = 0;

			String action = "";

			// update Indent
			// StoreInternalIndentM storeInternalIndentM1=null;
			storeInternalIndentM = dispensaryDao.getStoreInternalIndentM(indentmId);
			if (storeInternalIndentM != null) {
				/// for Approve Indent

				String approvalRemark = "";
				if (json.has("action")) {
					action = jsondata.get("action").toString();
					action = getReplaceString(action);
					if (action.equalsIgnoreCase("Approve")) {
						storeInternalIndentM.setStatus("C");
						storeInternalIndentM.setCoId(users.getUserId());
						storeInternalIndentM.setCoFlag("Y");
						storeInternalIndentM.setCoDate(new Date());
					}
					else {
						storeInternalIndentM.setStatus("Z");
						storeInternalIndentM.setApprovalDate(new Timestamp(date.getTime()));
						storeInternalIndentM.setUser3(users); // need to chk
						storeInternalIndentM.setLastChgDate(new Timestamp(date.getTime()));
						storeInternalIndentM.setCoId(users.getUserId());
						storeInternalIndentM.setCoFlag("N");
						storeInternalIndentM.setCoDate(new Date());
					}
					
					storeInternalIndentM.setUser1(users);
					if (json.has("approvalRemark")) {
						approvalRemark = jsondata.get("approvalRemark").toString();
						approvalRemark = getReplaceString(approvalRemark);
						storeInternalIndentM.setApprovalRemarks(approvalRemark);
						storeInternalIndentM.setCoRemarks(approvalRemark);
						storeInternalIndentM.setCoRemarks(approvalRemark);
					}

				}

				// End
				else {
					storeInternalIndentM.setLastChgDate(new Timestamp(date.getTime()));
					storeInternalIndentM.setDepartment1(masDepartment);// need to change
					if (btnvalue1.equalsIgnoreCase("save"))
						storeInternalIndentM.setStatus("N");// n=save,y=submit,a=approve,r=reject
					if (btnvalue1.equalsIgnoreCase("submit"))
						storeInternalIndentM.setStatus("Y");
					//storeInternalIndentM.setDepartment2(masStore);
					storeInternalIndentM.setUser1(users);
					storeInternalIndentM.setUser2(users);
					
				}
				dispensaryDao.updateStoreInternalIndentM(storeInternalIndentM);
				
			}
			// StoreInternalIndentT storeInternalIndentT1=null;
			for (int i = 0; i < itemLength; i++) {
				StoreInternalIndentT storeInternalIndentT = new StoreInternalIndentT();
				if (!indentList[i].trim().isEmpty() && indentList[i] != null) {
					storeInternalIndentT = dispensaryDao.getStoreInternalIndentT(Long.parseLong(indentList[i].trim()));
					// storeInternalIndentT.setId(Long.parseLong(indentList[i].trim()));
				}
				if (requiredQtyValue[i] != null && !requiredQtyValue[i].isEmpty())
					storeInternalIndentT.setQtyRequest(Long.parseLong(requiredQtyValue[i].trim()));

				if (remarks1Value[i] != null && !remarks1Value[i].isEmpty())
					storeInternalIndentT.setReasonForDemand(remarks1Value[i].trim());

				/*
				 * if (itemIdNomValue[i] != null && !itemIdNomValue[i].isEmpty()) { masStoreItem
				 * = dispensaryDao.getMasStoreItem(Long.parseLong(itemIdNomValue[i].trim()));
				 * storeInternalIndentT.setMasStoreItem(masStoreItem); }
				 */
				if (availableStockValue[i] != null && !availableStockValue[i].trim().isEmpty()) {
					storeInternalIndentT.setAvailableStock(Long.parseLong(availableStockValue[i].trim()));
				}
				if (approvedQtyValue[i] != null && !approvedQtyValue[i].trim().isEmpty()) {
					storeInternalIndentT.setApprovedQty(Long.parseLong(approvedQtyValue[i].trim()));
				}
				if (stockInDispenceryValue[i] != null && !stockInDispenceryValue[i].trim().isEmpty()) {
					storeInternalIndentT.setDispStock(Long.parseLong(stockInDispenceryValue[i].trim()));
				}
				if (stockInStoreValue[i] != null && !stockInStoreValue[i].trim().isEmpty()) {
					storeInternalIndentT.setStoresStock(Long.parseLong(stockInStoreValue[i].trim()));
				}
				storeInternalIndentT.setDepartment1(masDepartment);// need to chk
				storeInternalIndentT.setStoreInternalIndentM1(storeInternalIndentM);// need to chk
				indentId = dispensaryDao.saveOrUpdatSstoreInternalIndentT(storeInternalIndentT);
			}

			// String dtObj = dispensaryDao.saveDispensaryIndent(jsondata);

			if (indentId != 0) {
				jsonObj.put("indentMId", indentmId);
				jsonObj.put("status", 1);
				if (action.equalsIgnoreCase("") || action.trim().isEmpty())
					jsonObj.put("msg", "Record Updated Successfully");
				if (action.equalsIgnoreCase("Approve"))
					jsonObj.put("msg", "Indent has been Approved");
				else if (action.equalsIgnoreCase("Reject"))
					jsonObj.put("msg", "Indent has been Rejected");

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Error occured");
			}
		}
		return jsonObj.toString();
	}
	/// end Anita

	@SuppressWarnings("unchecked")
	@Override
	public String getPendingPrescriptionList(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = dispensaryDao.getPendingPrescriptionList(jsondata);
		if (!map.isEmpty()) {
			int count = Integer.parseInt(String.valueOf(map.get("count")));
			List<PatientPrescriptionHd> list = (List<PatientPrescriptionHd>) map.get("prescriptionList");
			List<Map<String, Object>> PrescriptionList = new ArrayList<>();
			if (list.size() > 0) {
				for (PatientPrescriptionHd hd : list) {
					Map<String, Object> data = new HashMap<>();
					String pres_date = "", mobileNo = "", patientName = "", empName = "", age = "",
							gender = "", id = "";
					if (hd.getPrescriptionDate() != null && !hd.getPrescriptionDate().equals("")) {
						pres_date = HMSUtil.changeDateToddMMyyyy(hd.getPrescriptionDate());
					}
					data.put("pres_date", pres_date);
					if (hd.getPatient() != null) {
						mobileNo = HMSUtil.convertNullToEmptyString(hd.getPatient().getMobileNumber());
						patientName = HMSUtil.convertNullToEmptyString(hd.getPatient().getPatientName());
						/*
						 * empName =
						 * HMSUtil.convertNullToEmptyString(hd.getPatient().getEmployeeName()); if
						 * (hd.getPatient().getMasRelation() != null) { relation = HMSUtil
						 * .convertNullToEmptyString(hd.getPatient().getMasRelation().getRelationName())
						 * ; }
						 */
						if (hd.getPatient().getDateOfBirth() != null) {
							Period p = ProjectUtils.getDOB(hd.getPatient().getDateOfBirth());
							Date date = hd.getPatient().getDateOfBirth();
							age = HMSUtil.calculateAgeFromDOB(date);
						}
						if (hd.getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString(
									hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}

					}
					data.put("id", hd.getPrescriptionHdId());
					data.put("mobileNo", mobileNo);
					data.put("patient_name", patientName);
					data.put("emp_name", empName);
					data.put("age", age);
					data.put("gender", gender);
					
					PrescriptionList.add(data);
				}
				obj.put("pendingList", PrescriptionList);
				obj.put("count", count);
			} else {
				obj.put("pendingList", new JSONArray());
				obj.put("count", "0");
			}

		} else {
			obj.put("pendingList", new JSONArray());
			obj.put("count", "0");
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getPrescriptionHeader(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = dispensaryDao.getPrescriptionHeader(jsondata);
		List<Map<String, Object>> patientDetail = new ArrayList<>();
		if (!map.isEmpty()) {
			List<PatientPrescriptionHd> headerInfo = (List<PatientPrescriptionHd>) map.get("headerDetail");
			if (headerInfo != null) {
				Map<String, Object> patientData = new HashMap<>();
				for (PatientPrescriptionHd pHd : headerInfo) {
					patientData.put("header_id", pHd.getPrescriptionHdId());
					String mobileNo = "", patientName = "", relation = "", empName = "", gender = "", patientTypeFlag="",patientId="";
					if (pHd.getPatient() != null) {
						mobileNo = HMSUtil.convertNullToEmptyString(pHd.getPatient().getMobileNumber());
						patientName = HMSUtil.convertNullToEmptyString(pHd.getPatient().getPatientName());
						patientTypeFlag = HMSUtil.convertNullToEmptyString(pHd.getPatient().getPatientType());
						patientId = String.valueOf(pHd.getPatient().getPatientId());

						if (pHd.getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString(
									pHd.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}
					}
					patientData.put("mobileNo", mobileNo);
					patientData.put("patient_name", patientName);
					patientData.put("relation", relation);
					patientData.put("employee_name", empName);
					patientData.put("patientTypeFlag", patientTypeFlag);
					patientData.put("patientId", patientId);
					String age = "", prescriptionDate = "";
					if (pHd.getPatient().getDateOfBirth() != null) {
						Date date = pHd.getPatient().getDateOfBirth();
						age = HMSUtil.calculateAgeFromDOB(date);
					}
					if (pHd.getPrescriptionDate() != null) {
						prescriptionDate = HMSUtil.changeDateToddMMyyyy(pHd.getPrescriptionDate());
					}
					patientData.put("prescription_date", prescriptionDate);
					patientData.put("age", age);
					patientData.put("gender", gender);
					String doctorName = "";
					if (pHd.getDoctorIds() != null) {
						doctorName = HMSUtil.convertNullToEmptyString(pHd.getDoctorIds().getUserName());
					}

					patientData.put("doctor", doctorName);
					// patientDetail.add(patientData);

				}
				obj.put("headerInfo", patientData);
			} else {
				obj.put("headerInfo", new JSONArray());
			}
		} else {
			obj.put("headerInfo", new JSONArray());
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getPrescriptionDetail(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Session session = null;
		try {
			Map<String, Object> map = dispensaryDao.getPrescriptionDetail(jsondata);
			List<Map<String, Object>> pt = new ArrayList<>();
			List<Map<String, Object>> patientDetail = new ArrayList<>();
			if (!map.isEmpty()) {
				List<PatientPrescriptionDt> dtList = (List<PatientPrescriptionDt>) map.get("detailList");
				if (dtList != null) {
					// dtList.forEach(items -> {
					session = getHibernateUtils.getHibernateUtlis().OpenSession();
					for (PatientPrescriptionDt items : dtList) {
						Map<String, Object> data = new HashMap<>();
						String pvsmNo = "", nomenclature = "", au = "", frequency = "", freq_id = "", auId = "", itemTypeId="";
						if (items.getMasStoreItem() != null) {
							pvsmNo = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getPvmsNo());
							nomenclature = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getNomenclature());
							if(items.getMasStoreItem().getMasItemType() != null) {
								itemTypeId = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getMasItemType().getItemTypeCode());
							}
							if (items.getMasStoreItem().getMasStoreUnit() != null) {
								au = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitName());
								auId = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitId());
							}
						}

						if (items.getMasFrequency() != null) {
							freq_id = items.getMasFrequency().getFrequencyId().toString();
							frequency = HMSUtil.convertNullToEmptyString(items.getMasFrequency().getFrequencyName());
						}
						data.put("detail_id", items.getPrescriptionDtId());
						data.put("header_id", items.getPatientPrescriptionHd().getPrescriptionHdId());
						data.put("pvsm_no", HMSUtil.convertNullToEmptyString(pvsmNo));
						data.put("nomenclature", HMSUtil.convertNullToEmptyString(nomenclature));
						data.put("itemTypeId",itemTypeId);
						data.put("au", HMSUtil.convertNullToEmptyString(au));
						data.put("au_id", auId);
						data.put("qty_prescribed", HMSUtil.convertNullToEmptyString(items.getTotal()));
						data.put("dosage", HMSUtil.convertNullToEmptyString(items.getDosage()));
						data.put("frequency", HMSUtil.convertNullToEmptyString(frequency));
						data.put("freq_id", freq_id);
						data.put("no_of_days", HMSUtil.convertNullToEmptyString(items.getNoOfDays()));
						data.put("item_id", HMSUtil.convertNullToEmptyString(items.getItemId()));

						// get batch detail
						double dispStock = 0;
						ArrayList<Map<String, Object>> batchList = new ArrayList<>();
						long itemId = items.getItemId();
						long mmuId = ((Number) jsondata.get("mmuId")).longValue();
						long departmentId = ((Number) jsondata.get("department_id")).longValue();
						int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
						Date date = new Date();
						Calendar c = Calendar.getInstance();
						c.setTime(date);
						c.add(Calendar.DATE, expiryDays);
						date = c.getTime();
						String sql = "select  stock_id, BATCH_NO, EXPIRY_DATE, CLOSING_STOCK from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and mmu_id =:mmuId and DEPARTMENT_ID =:dept_id and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
						Query query = session.createSQLQuery(sql);
						query.setLong("item_id", itemId);
						query.setLong("mmuId", mmuId);
						query.setLong("dept_id", departmentId);
						query.setDate("exp_date", date);
						List<Object[]> result = query.list();
						for (Object[] row : result) {
							Map<String, Object> batchData = new HashMap<>();
							if (row[3] != null) {
								dispStock += ((Double) row[3]).intValue();
							}
							String stockId = "", batchNo = "", expiryDate = "", batchStock = "";
							if (row[0] != null) {
								stockId = row[0].toString();
							}
							batchData.put("stock_id", stockId);

							if (row[1] != null) {
								batchNo = row[1].toString();
							}
							batchData.put("batch_no", batchNo);

							if (row[2] != null) {
								expiryDate = HMSUtil.changeDateToddMMyyyy((Date) row[2]);
							}
							batchData.put("date_of_expiry", expiryDate);

							if (row[3] != null) {
								batchStock = row[3].toString();
							}
							int bStock = (int) Double.parseDouble(batchStock);
							batchData.put("batch_stock", bStock);
							batchList.add(batchData);
						}
						// sort by expiry date
						Collections.sort(batchList, new Comparator<Map<String, Object>>() {
							public int compare(Map<String, Object> m1, Map<String, Object> m2) {

								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
								Date data1 = null;
								Date date2 = null;
								try {
									data1 = formatter.parse((String) m1.get("date_of_expiry"));
									date2 = formatter.parse((String) m2.get("date_of_expiry"));
								} catch (Exception pe) {
									pe.printStackTrace();
								}

								return (data1).compareTo(date2); // ascending order
								// return ((String) m2.get("date_of_expiry")).compareTo((String)
								// m1.get("date_of_expiry")); //descending order
							}
						});
						// get store stock
						//long deptId = Integer.parseInt(HMSUtil.getProperties("adt.properties", "STORE_DEPARTMENT_ID"));
						String code = HMSUtil.getProperties("adt.properties", "STORE_DEPARTMENT_CODE");
						long deptId = CommonUtil.getDepartmentIdAgainstCode(session,code);
						System.out.println("***********************************deptID********************************* "+deptId);
						String sql2 = "select CLOSING_STOCK from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and mmu_id =:mmuId and DEPARTMENT_ID =:dept_id and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
						Query query2 = session.createSQLQuery(sql2);
						query2.setLong("item_id", itemId);
						query2.setLong("mmuId", mmuId);
						query2.setLong("dept_id", deptId);
						query2.setDate("exp_date", date);
						System.out.println("sql2 " + sql2);
						List<BigDecimal> stockList = query2.list();
						int storeStock = 0;
						for (BigDecimal value : stockList) {
							storeStock += Integer.parseInt(value.toString());
						}
						data.put("store_stock", storeStock);
						/*
						 * if(storeStock > 0) { data.put("store_stock", storeStock); } else {
						 * data.put("store_stock", ""); }
						 */
						data.put("batchList", batchList);
						data.put("disp_stock", dispStock);
						/*
						 * if(dispStock > 0) { data.put("disp_stock", dispStock); }else {
						 * data.put("disp_stock", ""); }
						 */

						pt.add(data);
					}
					;
					obj.put("detailList", pt);

				} else {
					obj.put("detailList", new JSONArray());

				}
			} else {
				obj.put("detailList", new JSONArray());
			}
			return obj.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getBatchDetail(Map<String, Object> jsondata) {
		Map<String, Object> map = dispensaryDao.getBatchDetail(jsondata);
		Map<String, Object> noStockdata = new HashMap<>();
		JSONObject obj = new JSONObject();
		String expiry_date=null;
		if (!map.isEmpty()) {
			List<StoreItemBatchStock> dispensaryList = (List<StoreItemBatchStock>) map.get("dispensaryList");
			//List<StoreItemBatchStock> storeList = (List<StoreItemBatchStock>) map.get("storeList");
			Map<String, Object> mapData = new HashMap<>();
			if (dispensaryList != null) {
				List<Map<String, Object>> batchList = new ArrayList<>();
				Integer dispStock = 0;

				for (StoreItemBatchStock items : dispensaryList) {
					Map<String, Object> data = new HashMap<>();
					data.put("id", items.getStockId());
					data.put("batch_no", items.getBatchNo());					
					
					if (expiry_date == null) {
						expiry_date = HMSUtil.changeDateToddMMyyyy(items.getExpiryDate()); // Set the first expiry date
				    }
					
					data.put("batch_stock", items.getClosingStock());
					dispStock = dispStock + items.getClosingStock().intValue();
					batchList.add(data);
				}
			
				mapData.put("expiry_date",expiry_date != null? expiry_date : "" );
				mapData.put("disp_stock", dispStock);
				mapData.put("batch_list", batchList);
				mapData.put("store_stock",0);
				
				
				obj.put("batchData", mapData);
			} else {
				obj.put("batchData", noStockdata);
			}

		} else {
			
			obj.put("batchData", noStockdata);
		}
		return obj.toString();
	}

	@Override
	public String issueMedicineFromDispensary(Map<String, Object> jsondata) {
		String data = dispensaryDao.issueMedicineFromDispensary(jsondata);
		JSONObject obj = new JSONObject();
		obj.put("msg", data);
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getPartialWaitingList(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = dispensaryDao.getPartialWaitingList(jsondata);
		if (!map.isEmpty()) {
			int count = Integer.parseInt(String.valueOf(map.get("count")));
			List<PatientPrescriptionHd> list = (List<PatientPrescriptionHd>) map.get("prescriptionList");
			List<Map<String, Object>> PrescriptionList = new ArrayList<>();
			if (list.size() > 0) {
				for (PatientPrescriptionHd hd : list) {
					Map<String, Object> data = new HashMap<>();
					String pres_date = "", mobileNo = "", patientName = "", empName = "", relation = "", age = "",
							gender = "", id = "";
					if (hd.getPrescriptionDate() != null && !hd.getPrescriptionDate().equals("")) {
						pres_date = HMSUtil.changeDateToddMMyyyy(hd.getPrescriptionDate());
					}
					data.put("pres_date", pres_date);
					if (hd.getPatient() != null) {
						mobileNo = HMSUtil.convertNullToEmptyString(hd.getPatient().getMobileNumber());
						patientName = HMSUtil.convertNullToEmptyString(hd.getPatient().getPatientName());
						/*
						 * empName =
						 * HMSUtil.convertNullToEmptyString(hd.getPatient().getEmployeeName()); if
						 * (hd.getPatient().getMasRelation() != null) { relation = HMSUtil
						 * .convertNullToEmptyString(hd.getPatient().getMasRelation().getRelationName())
						 * ; }
						 */
						if (hd.getPatient().getDateOfBirth() != null) {
							Date date = hd.getPatient().getDateOfBirth();
							age = HMSUtil.calculateAgeFromDOB(date);
						}
						if (hd.getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString(
									hd.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}

					}
					data.put("id", hd.getPrescriptionHdId());
					data.put("mobileNo", mobileNo);
					data.put("patient_name", patientName);
					data.put("emp_name", empName);
					data.put("relation", relation);
					data.put("age", age);
					data.put("gender", gender);
					PrescriptionList.add(data);
				}
				obj.put("pendingList", PrescriptionList);
				obj.put("count", count);
			} else {
				obj.put("pendingList", new JSONArray());
				obj.put("count", "0");
			}

		} else {
			obj.put("pendingList", new JSONArray());
			obj.put("count", "0");
		}
		return obj.toString();
	}

	@Override
	public String getPartialIssueHeader(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = dispensaryDao.getPartialIssueHeader(jsondata);
		List<Map<String, Object>> patientDetail = new ArrayList<>();
		if (!map.isEmpty()) {
			List<PatientPrescriptionHd> headerInfo = (List<PatientPrescriptionHd>) map.get("headerDetail");
			if (headerInfo != null) {
				Map<String, Object> patientData = new HashMap<>();
				for (PatientPrescriptionHd pHd : headerInfo) {
					patientData.put("header_id", pHd.getPrescriptionHdId());
					String mobileNo = "", patientName = "", relation = "", empName = "", gender = "";
					if (pHd.getPatient() != null) {
						mobileNo = HMSUtil.convertNullToEmptyString(pHd.getPatient().getMobileNumber());
						patientName = HMSUtil.convertNullToEmptyString(pHd.getPatient().getPatientName());
						/*
						 * empName =
						 * HMSUtil.convertNullToEmptyString(pHd.getPatient().getEmployeeName()); if
						 * (pHd.getPatient().getMasRelation() != null) { relation = HMSUtil
						 * .convertNullToEmptyString(pHd.getPatient().getMasRelation().getRelationName()
						 * ); }
						 */

						if (pHd.getPatient().getMasAdministrativeSex() != null) {
							gender = HMSUtil.convertNullToEmptyString(
									pHd.getPatient().getMasAdministrativeSex().getAdministrativeSexName());
						}
					}
					patientData.put("mobileNo", mobileNo);
					patientData.put("patient_name", patientName);
					patientData.put("relation", relation);
					patientData.put("employee_name", empName);
					String age = "", prescriptionDate = "";
					if (pHd.getPatient().getDateOfBirth() != null) {
						Date date = pHd.getPatient().getDateOfBirth();
						age = HMSUtil.calculateAgeFromDOB(date);
					}
					if (pHd.getPrescriptionDate() != null) {
						prescriptionDate = HMSUtil.changeDateToddMMyyyy(pHd.getPrescriptionDate());
					}
					patientData.put("prescription_date", prescriptionDate);
					patientData.put("age", age);
					patientData.put("gender", gender);
					String doctorName = "";
					if (pHd.getDoctorIds() != null) {
						doctorName = HMSUtil.convertNullToEmptyString(pHd.getDoctorIds().getUserName());
					}

					patientData.put("doctor", doctorName);
					// patientDetail.add(patientData);

				}
				obj.put("headerInfo", patientData);
			} else {
				obj.put("headerInfo", new JSONArray());
			}
		} else {
			obj.put("headerInfo", new JSONArray());
		}
		return obj.toString();
	}

	@Override
	public String getPartialIssueDetails(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Session session = null;
		try {
			Map<String, Object> map = dispensaryDao.getPartialIssueDetails(jsondata);
			List<Map<String, Object>> pt = new ArrayList<>();
			List<Map<String, Object>> patientDetail = new ArrayList<>();
			if (!map.isEmpty()) {
				List<PatientPrescriptionDt> dtList = (List<PatientPrescriptionDt>) map.get("detailList");
				if (dtList != null) {
					// dtList.forEach(items -> {
					session = getHibernateUtils.getHibernateUtlis().OpenSession();
					for (PatientPrescriptionDt items : dtList) {
						Map<String, Object> data = new HashMap<>();
						String pvsmNo = "", nomenclature = "", au = "", frequency = "", freq_id = "", auId = "", itemTypeId="";
						if (items.getMasStoreItem() != null) {
							pvsmNo = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getPvmsNo());
							nomenclature = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getNomenclature());
							if(items.getMasStoreItem().getMasItemType() != null) {
								itemTypeId = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getMasItemType().getItemTypeCode());
							}							
							if (items.getMasStoreItem().getMasStoreUnit() != null) {
								au = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitName());
								auId = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitId());
							}
						}

						if (items.getMasFrequency() != null) {
							freq_id = items.getMasFrequency().getFrequencyId().toString();
							frequency = HMSUtil.convertNullToEmptyString(items.getMasFrequency().getFrequencyName());
						}
						data.put("detail_id", items.getPrescriptionDtId());
						data.put("header_id", items.getPatientPrescriptionHd().getPrescriptionHdId());
						data.put("pvsm_no", HMSUtil.convertNullToEmptyString(pvsmNo));
						data.put("nomenclature", HMSUtil.convertNullToEmptyString(nomenclature));
						data.put("itemTypeId",itemTypeId);
						data.put("au", HMSUtil.convertNullToEmptyString(au));
						data.put("au_id", auId);
						long prescribedQuantity = items.getTotal() - items.getQtyIssued();
						data.put("qty_prescribed", HMSUtil.convertNullToEmptyString(prescribedQuantity));
						data.put("dosage", HMSUtil.convertNullToEmptyString(items.getDosage()));
						data.put("frequency", HMSUtil.convertNullToEmptyString(frequency));
						data.put("freq_id", freq_id);
						data.put("no_of_days", HMSUtil.convertNullToEmptyString(items.getNoOfDays()));
						data.put("item_id", HMSUtil.convertNullToEmptyString(items.getItemId()));

						// get batch detail
						int dispStock = 0;
						ArrayList<Map<String, Object>> batchList = new ArrayList<>();
						long itemId = items.getItemId();
						long hospitalId = ((Number) jsondata.get("hospital_id")).longValue();
						long departmentId = ((Number) jsondata.get("department_id")).longValue();
						int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
						Date date = new Date();
						Calendar c = Calendar.getInstance();
						c.setTime(date);
						c.add(Calendar.DATE, expiryDays);
						date = c.getTime();
						String sql = "select  stock_id, BATCH_NO, EXPIRY_DATE, CLOSING_STOCK from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and hospital_id =:hospital_id and DEPARTMENT_ID =:dept_id and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
						Query query = session.createSQLQuery(sql);
						query.setLong("item_id", itemId);
						query.setLong("hospital_id", hospitalId);
						query.setLong("dept_id", departmentId);
						query.setDate("exp_date", date);
						List<Object[]> result = query.list();
						for (Object[] row : result) {
							Map<String, Object> batchData = new HashMap<>();
							if (row[3] != null) {
								dispStock += ((BigDecimal) row[3]).intValue();
							}
							String stockId = "", batchNo = "", expiryDate = "", batchStock = "";
							if (row[0] != null) {
								stockId = row[0].toString();
							}
							batchData.put("stock_id", stockId);

							if (row[1] != null) {
								batchNo = row[1].toString();
							}
							batchData.put("batch_no", batchNo);

							if (row[2] != null) {
								expiryDate = HMSUtil.changeDateToddMMyyyy((Date) row[2]);
							}
							batchData.put("date_of_expiry", expiryDate);

							if (row[3] != null) {
								batchStock = row[3].toString();
							}
							batchData.put("batch_stock", batchStock);
							batchList.add(batchData);
						}
						// sort list by date_of_expiry
						Collections.sort(batchList, new Comparator<Map<String, Object>>() {
							public int compare(Map<String, Object> m1, Map<String, Object> m2) {

								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
								Date data1 = null;
								Date date2 = null;
								try {
									data1 = formatter.parse((String) m1.get("date_of_expiry"));
									date2 = formatter.parse((String) m2.get("date_of_expiry"));
								} catch (Exception pe) {
									pe.printStackTrace();
								}

								return (data1).compareTo(date2); // ascending order
								// return ((String) m2.get("date_of_expiry")).compareTo((String)
								// m1.get("date_of_expiry")); //descending order
							}
						});

						// get store stock
						//long deptId = Integer.parseInt(HMSUtil.getProperties("adt.properties", "STORE_DEPARTMENT_ID"));
						String code = HMSUtil.getProperties("adt.properties", "STORE_DEPARTMENT_CODE");
						long deptId = CommonUtil.getDepartmentIdAgainstCode(session,code);
						String sql2 = "select CLOSING_STOCK from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and hospital_id =:hospital_id and DEPARTMENT_ID =:dept_id and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
						Query query2 = session.createSQLQuery(sql2);
						query2.setLong("item_id", itemId);
						query2.setLong("hospital_id", hospitalId);
						query2.setLong("dept_id", deptId);
						query2.setDate("exp_date", date);
						System.out.println("sql2 " + sql2);
						List<BigDecimal> stockList = query2.list();
						int storeStock = 0;
						for (BigDecimal value : stockList) {
							storeStock += Integer.parseInt(value.toString());
						}
						data.put("store_stock", storeStock);
						data.put("batchList", batchList);
						data.put("disp_stock", dispStock);
						if (dispStock > 0) {
							data.put("disp_stock", dispStock);
						} else {
							data.put("disp_stock", "");
						}

						pt.add(data);
					}
					;
					obj.put("detailList", pt);

				} else {
					obj.put("detailList", new JSONArray());

				}
			} else {
				obj.put("detailList", new JSONArray());
			}
			return obj.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return obj.toString();
	}

	@Override
	public String partialIssueMedicineFromDispensary(Map<String, Object> jsondata) {
		String data = dispensaryDao.partialIssueMedicineFromDispensary(jsondata);
		JSONObject obj = new JSONObject();
		obj.put("msg", data);
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String indentIssueWaitingList(Map<String, Object> jsondata) {
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject();

		responseMap = dispensaryDao.indentIssueWaitingList(jsondata);
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentMList = (List<StoreInternalIndentM>) responseMap.get("storeInternalIndentMList");
			if (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0) {
				for (StoreInternalIndentM storeInternalIndentM : storeInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("indentId", storeInternalIndentM.getId());
					map1.put("indentNo", storeInternalIndentM.getDemandNo());
					map1.put("indentDate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentM.getDemandDate()));
					map1.put("mmuName", storeInternalIndentM.getMasMMU().getMmuName());// need to change
					if(storeInternalIndentM.getStatus().equals("C")) {
						map1.put("status", "Pending for issue");
					}else {
						map1.put("status", "Partially Issue");
					}
					responseList.add(map1);
				}

			}

		}
		Integer tm = (Integer) responseMap.get("count");
		json.put("data", responseList);
		json.put("count", tm);
		json.put("status", true);
		return json.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getIndentIssueHeader(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		try {
			Map<String, Object> data = dispensaryDao.getIndentIssueHeader(jsondata);
			if (!data.isEmpty()) {
				List<StoreInternalIndentM> list = (List<StoreInternalIndentM>) data.get("headerList");
				if (!list.isEmpty()) {
					List<Map<String, Object>> headerList = new ArrayList<>();
					for (StoreInternalIndentM indentM : list) {
						Map<String, Object> map = new HashMap<>();
						String fromDept = "", requestedBy = "", FromDeptId = "";

						if (indentM.getDepartment1() != null) {
							fromDept = HMSUtil.convertNullToEmptyString(indentM.getDepartment1().getDepartmentName());
							FromDeptId = HMSUtil.convertNullToEmptyString(indentM.getDepartment1().getDepartmentId());
						}
						if (indentM.getUsers4() != null) {
							requestedBy = HMSUtil.convertNullToEmptyString(indentM.getUsers4().getUserName());
						}
						String demandDate = "";
						if(indentM.getDemandDate() != null) {
							demandDate = HMSUtil.changeDateToddMMyyyy(indentM.getDemandDate());
						}else {
							demandDate = "";
						}
						map.put("header_id", indentM.getId());
						map.put("indent_no", HMSUtil.convertNullToEmptyString(indentM.getDemandNo()));
						map.put("indent_date", demandDate);
						map.put("from_dept", fromDept);
						map.put("requested_by", requestedBy);
						map.put("from_dept_id", FromDeptId);
						headerList.add(map);
					}
					obj.put("headerList", headerList);
				} else {
					obj.put("headerDetail", new JSONArray());
				}
			} else {
				obj.put("headerDetail", new JSONArray());
			}
			return obj.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getIndentIssueDetails(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Session session = null;
		try {
			Long cityId = Long.parseLong(String.valueOf(jsondata.get("cityId")));
			Map<String, Object> data = dispensaryDao.getIndentIssueDetails(jsondata);
			if (!data.isEmpty()) {
				List<StoreInternalIndentT> list = (List<StoreInternalIndentT>) data.get("detailList");
				if (list != null && !list.isEmpty()) {
					session = getHibernateUtils.getHibernateUtlis().OpenSession();
					List<Map<String, Object>> pt = new ArrayList<>();
					for (StoreInternalIndentT items : list) {
						Map<String, Object> map = new HashMap<>();
						String pvsmNo = "", nomenclature = "", au = "", auId = "", itemId = "";
						if (items.getMasStoreItem() != null) {
							pvsmNo = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getPvmsNo());
							nomenclature = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getNomenclature());
							itemId = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getItemId());
							if (items.getMasStoreItem().getMasStoreUnit() != null) {
								au = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitName());
								auId = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitId());
							}
						}

						map.put("detail_id", items.getId());
						map.put("header_id", items.getStoreInternalIndentM1().getId());
						map.put("pvsm_no", HMSUtil.convertNullToEmptyString(pvsmNo));
						map.put("nomenclature", HMSUtil.convertNullToEmptyString(nomenclature));
						map.put("au", HMSUtil.convertNullToEmptyString(au));
						map.put("au_id", auId);
						map.put("qty_requested", HMSUtil.convertNullToEmptyString(items.getApprovedQty()));
						map.put("item_id", HMSUtil.convertNullToEmptyString(itemId));
						map.put("item_id", HMSUtil.convertNullToEmptyString(itemId));
						if(items.getQtyIssued() == null) {
							map.put("previousIssuedQty",0);
						}else {
							map.put("previousIssuedQty",items.getQtyIssued());
						}
						
						// get batch detail & dispensary stock
						int dispStock = 0;
						ArrayList<Map<String, Object>> batchList = new ArrayList<>();
						int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
						Date date = new Date();
						Calendar c = Calendar.getInstance();
						c.setTime(date);
						c.add(Calendar.DATE, expiryDays);
						date = c.getTime();
						String sql = "select  stock_id, BATCH_NO, EXPIRY_DATE, CLOSING_STOCK, MANUFACTURE_DATE from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and city_id =:cityId and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
						Query query = session.createSQLQuery(sql);
						query.setLong("item_id", Long.parseLong(itemId));
						query.setLong("cityId", cityId);
						query.setDate("exp_date", date);
						List<Object[]> result = query.list();
						for (Object[] row : result) {
							Map<String, Object> batchData = new HashMap<>();
							if (row[3] != null) {
								dispStock += ((Number) row[3]).intValue();
							}
							String stockId = "", batchNo = "", expiryDate = "", manufacturingDate = "";
							int batchStock = 0;
							if (row[0] != null) {
								stockId = row[0].toString();
							}
							batchData.put("stock_id", stockId);

							if (row[1] != null) {
								batchNo = row[1].toString();
							}
							batchData.put("batch_no", batchNo);

							if (row[2] != null) {
								expiryDate = HMSUtil.changeDateToddMMyyyy((Date) row[2]);
							}
							batchData.put("expiry_date", expiryDate);
							if (row[4] != null) {
								manufacturingDate = HMSUtil.changeDateToddMMyyyy((Date) row[4]);
							}
							batchData.put("date_of_manufacturing", manufacturingDate);

							if (row[3] != null) {
								batchStock = ((Number) row[3]).intValue();
							}
							batchData.put("batch_stock", batchStock);
							batchList.add(batchData);
						}

						// sort by expiry date
						Collections.sort(batchList, new Comparator<Map<String, Object>>() {
							public int compare(Map<String, Object> m1, Map<String, Object> m2) {

								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
								Date data1 = null;
								Date date2 = null;
								try {
									data1 = formatter.parse((String) m1.get("expiry_date"));
									date2 = formatter.parse((String) m2.get("expiry_date"));
								} catch (Exception pe) {
									pe.printStackTrace();
								}

								return (data1).compareTo(date2); // ascending order
								// return ((String) m2.get("date_of_expiry")).compareTo((String)
								// m1.get("date_of_expiry")); //descending order
							}
						});

						// get store stock
						map.put("batchList", batchList);
						map.put("disp_stock", dispStock);
						if (dispStock > 0) {
							map.put("disp_stock", dispStock);
						} else {
							map.put("disp_stock", 0);
						}

						pt.add(map);
					}
					obj.put("detailList", pt);
				} else {
					obj.put("detailList", new JSONArray());
				}
			} else {
				obj.put("detailList", new JSONArray());
			}
			return obj.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj.toString();
	}

	@Override
	public String indentIssue(Map<String, Object> jsondata) {
		Map<String,Object> data = dispensaryDao.indentIssue(jsondata);
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		return obj.toString();
	}

	@Override
	public String getIssuNoAndIndentNo(Map<String, Object> jsondata) {
		Map<String, Object> data = dispensaryDao.getIssuNoAndIndentNo(jsondata);
		JSONObject obj = new JSONObject();
		if (data.isEmpty()) {
			obj.put("data", new JSONArray());
		} else {
			List<StoreIssueM> list = (List<StoreIssueM>) data.get("list");
			if (list.isEmpty()) {
				obj.put("data", new JSONArray());
			} else {
				List<Map<String, Object>> issueNoList = new ArrayList<>();
				for (StoreIssueM storeIssueM : list) {
					Map<String, Object> map = new HashMap<>();
					String issueNo = HMSUtil.convertNullToEmptyString(storeIssueM.getIssueNo());
					String indentNo = "";
					if (storeIssueM.getStoreInternalIndentM() != null) {
						indentNo = HMSUtil
								.convertNullToEmptyString(storeIssueM.getStoreInternalIndentM().getDemandNo());
					}
					map.put("id", storeIssueM.getId());
					map.put("issueNo", issueNo);
					map.put("indentNo", indentNo);
					map.put("issueDate", HMSUtil.changeDateToddMMyyyy(storeIssueM.getIssueDate()));
					issueNoList.add(map);
				}
				obj.put("data", issueNoList);
			}
		}
		return obj.toString();
	}

	@Override
	public String getDrugExpiryList(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		try {
			Map<String, Object> data = dispensaryDao.getDrugExpiryList(jsondata);
			if (data.isEmpty()) {
				obj.put("count", 0);
				obj.put("drugExpiryList", new JSONArray());
			} else {
				List<StoreItemBatchStock> list = (List<StoreItemBatchStock>) data.get("list");
				String count = String.valueOf(data.get("count"));
				if (list.isEmpty()) {
					obj.put("count", count);
					obj.put("drugExpiryList", new JSONArray());
				} else {
					List<Map<String, Object>> drugExpiryList = new ArrayList<>();
					for (StoreItemBatchStock storeItemBatchStock : list) {
						Map<String, Object> map = new HashMap<>();
						String pvmsNo = "", nomenclature = "", au = "", expiryDate = "";
						if (storeItemBatchStock.getMasStoreItem() != null) {
							pvmsNo = HMSUtil
									.convertNullToEmptyString(storeItemBatchStock.getMasStoreItem().getPvmsNo());
							nomenclature = HMSUtil
									.convertNullToEmptyString(storeItemBatchStock.getMasStoreItem().getNomenclature());
							au = HMSUtil.convertNullToEmptyString(
									storeItemBatchStock.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
						}
						map.put("pvms_no", pvmsNo);
						map.put("nomenclature", nomenclature);
						map.put("au", au);
						map.put("batch_no", HMSUtil.convertNullToEmptyString(storeItemBatchStock.getBatchNo()));
						map.put("closing_stock",
								HMSUtil.convertNullToEmptyString(storeItemBatchStock.getClosingStock()));
						if (storeItemBatchStock.getExpiryDate() != null) {
							expiryDate = HMSUtil.changeDateToddMMyyyy(storeItemBatchStock.getExpiryDate());
						}
						map.put("expiry_date", expiryDate);
						drugExpiryList.add(map);
					}
					obj.put("count", count);
					obj.put("drugExpiryList", drugExpiryList);
					return obj.toString();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		obj.put("count", 0);
		obj.put("drugExpiryList", new JSONArray());
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getROLDataList(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		try {
			Map<String, Object> data = dispensaryDao.getROLDataList(jsondata);
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if (data.isEmpty()) {
				obj.put("count", 0);
				obj.put("rolList", new JSONArray());
			} else {
				List<Object[]> list = (List<Object[]>) data.get("list");
				String count = String.valueOf(data.get("count"));
				if (list.isEmpty()) {
					obj.put("count", count);
					obj.put("rolList", new JSONArray());
				} else {
					List<Map<String, Object>> rolList = new ArrayList<>();
					for (Object[] object : list) {
						Map<String, Object> map = new HashMap<>();
						String pvmsNo = "", nomenClature = "", unitName = "", closingStock = "", rolQty = "";
						if (object[0] != null) {
							pvmsNo = object[0].toString();
						}
						if (object[1] != null) {
							nomenClature = object[1].toString();
						}
						if (object[2] != null) {
							unitName = object[2].toString();
						}
						if (object[3] != null) {
							closingStock = object[3].toString();
						}
						if (object[4] != null) {
							rolQty = object[4].toString();
						}
						map.put("pvmsNo", pvmsNo);
						map.put("nomenClature", nomenClature);
						map.put("unitName", unitName);
						map.put("closingStock", closingStock);
						map.put("rol_d", rolQty);
						rolList.add(map);

					}
					obj.put("count", count);
					obj.put("rolList", rolList);
					return obj.toString();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		obj.put("count", 0);
		obj.put("rolList", new JSONArray());
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getVisitListAndPrescriptionId(Map<String, Object> jsondata) {
		JSONObject json = new JSONObject();

		if (jsondata.get("patientId") == null || jsondata.get("patientId").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain Patient Id as a  key or value or it is null\"}";

		}

		else {

			Map<String, Object> map = dispensaryDao.getVisitListAndPrescriptionId(jsondata);
			List<PatientPrescriptionHd> getPatinetDetails = (List<PatientPrescriptionHd>) map.get("list");
			List<HashMap<String, Object>> p = new ArrayList<HashMap<String, Object>>();
			try {

				for (PatientPrescriptionHd ptDetails : getPatinetDetails) {
					HashMap<String, Object> pt = new HashMap<String, Object>();
					
					String visitDate = "", departmentNo="";
					if(ptDetails.getVisit().getVisitDate() != null) {
						visitDate = HMSUtil.changeDateToddMMyyyy(ptDetails.getVisit().getVisitDate());
					}
					/*Timestamp vd = ptDetails.getVisit().getVisitDate();
					Calendar lCal = Calendar.getInstance();
					lCal.setTime(vd);
					int yr = lCal.get(Calendar.YEAR);
					int mn = lCal.get(Calendar.MONTH) + 1;
					int dt = lCal.get(Calendar.DATE);
					LocalDate visitDate = LocalDate.of(yr, mn, dt); // Birth date*/	
					pt.put("visitDate", visitDate);
					pt.put("visitNo", ptDetails.getVisitId());
					long visitId = ptDetails.getVisitId();
					//String prescriptionId = dispensaryDao.getPrescriptionId(visitId);
					pt.put("prescriptionId", ptDetails.getPrescriptionHdId());
					pt.put("tokenNo", HMSUtil.convertNullToEmptyString(ptDetails.getVisit().getTokenNo()));
					if(ptDetails.getVisit().getMasDepartment() != null) {
						departmentNo = HMSUtil.convertNullToEmptyString(ptDetails.getVisit().getMasDepartment().getDepartmentName());
					}
					pt.put("departmentNo", departmentNo);
					p.add(pt);

				}
				if (p != null && p.size() > 0) {
					json.put("data", p);
					json.put("count", p.size());
					json.put("msg", "Patinet List  get  sucessfull... ");
					json.put("status", "1");
				} else {
					return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
				}
			} catch (Exception e) {
				System.out.println(e);
			}

			return json.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getNisRegisterData(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = dispensaryDao.getNisRegisterData(jsondata);
		if (map.isEmpty()) {
			obj.put("count", 0);
			obj.put("nisData", new JSONArray());
		} else {
			int count = (int) map.get("count");
			List<Object[]> list = (List<Object[]>) map.get("list");
			if (list.isEmpty()) {
				obj.put("count", 0);
				obj.put("nisData", new JSONArray());
			} else {
				List<Map<String, Object>> nisList = new ArrayList<>();
				for (Object[] dt : list) {
					Map<String, Object> data = new HashMap<>();
					String pvmsNo = "", nomenclature = "";
					if (dt[0] != null) {
						pvmsNo = HMSUtil.convertNullToEmptyString(dt[0]);
					}
					if (dt[1] != null) {
						nomenclature = HMSUtil.convertNullToEmptyString(dt[1]);
					}
					long nisQty = 0;
					if(dt[2] != null) {
						nisQty = Long.parseLong(String.valueOf(dt[2]));
					}
					
					data.put("pvmsNo", pvmsNo);
					data.put("nomenclature", nomenclature);
					data.put("nisQty", nisQty);
					nisList.add(data);
				}
				obj.put("nisData", nisList);
				obj.put("count", count);
			}
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getDailyIssueSummaryData(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Map<String,Object> map = dispensaryDao.getDailyIssueSummaryData(jsondata);
		if(map.isEmpty()) {
			obj.put("count", 0);
			obj.put("dailyIssueList", new JSONArray());
		}else {
			int count = (int)map.get("count");
			List<StoreIssueT> list = (List<StoreIssueT>)map.get("list");
			if(list.isEmpty()) {
				obj.put("count", 0);
				obj.put("dailyIssueList", new JSONArray());
			}else {
				List<Map<String,Object>> issueList = new ArrayList<>();
				for(StoreIssueT storeIssueT : list) {
					Map<String,Object> data = new HashMap<>();
					String mobileNo="", patientName="", relation="",nomenclature="",prescribedBy="", pharmascist="", nisFlag="N", balanceStock="", issueDate=null, expiryDate=null;
					if(storeIssueT.getStoreIssueM() != null && storeIssueT.getStoreIssueM().getPatientPrescriptionHd() != null && storeIssueT.getStoreIssueM().getPatientPrescriptionHd().getPatient() != null)
					{
						mobileNo = HMSUtil.convertNullToEmptyString(storeIssueT.getStoreIssueM().getPatientPrescriptionHd().getPatient().getMobileNumber());
						patientName = HMSUtil.convertNullToEmptyString(storeIssueT.getStoreIssueM().getPatientPrescriptionHd().getPatient().getPatientName());
						/*
						 * if(storeIssueT.getStoreIssueM().getPatientPrescriptionHd().getPatient().
						 * getMasrelation() != null) { relation =
						 * HMSUtil.convertNullToEmptyString(storeIssueT.getStoreIssueM().
						 * getPatientPrescriptionHd().getPatient().getMasrelation().getRelationName());
						 * }
						 */
					}
					if(storeIssueT.getMasStoreItem() != null) {
						nomenclature = HMSUtil.convertNullToEmptyString(storeIssueT.getMasStoreItem().getNomenclature());
						if(storeIssueT.getStoreIssueM().getPatientPrescriptionHd() != null && storeIssueT.getStoreIssueM().getPatientPrescriptionHd().getDoctorIds() != null) {
							prescribedBy += storeIssueT.getStoreIssueM().getPatientPrescriptionHd().getDoctorIds().getUserName();
						}
					}
					if(storeIssueT.getUsers() != null) {
						pharmascist	+= HMSUtil.convertNullToEmptyString(storeIssueT.getUsers().getUserName());												
					}
					if(storeIssueT.getPatientPrescriptionDt() != null) {
						/*
						 * if(storeIssueT.getPatientPrescriptionDt().getNisFlag() != null) { nisFlag =
						 * storeIssueT.getPatientPrescriptionDt().getNisFlag(); }
						 */
						
						balanceStock = HMSUtil.convertNullToEmptyString(storeIssueT.getPatientPrescriptionDt().getQtyBalance());
					}
					if(storeIssueT.getIssueDate() != null) {
						issueDate = HMSUtil.changeDateToddMMyyyy(storeIssueT.getIssueDate());						
					}
					if(storeIssueT.getExpiryDate() != null) {
						expiryDate = HMSUtil.changeDateToddMMyyyy(storeIssueT.getExpiryDate());
					}
					
					data.put("issued_date", HMSUtil.convertNullToEmptyString(issueDate));
					data.put("mobileNo", mobileNo);
					data.put("patientName", patientName);
					data.put("relation", relation);				
					data.put("nomenclature", nomenclature);
					data.put("qty_issued", HMSUtil.convertNullToEmptyString(storeIssueT.getQtyIssued()));
					data.put("nis_flag", nisFlag);
					data.put("balance_stock", balanceStock);
					data.put("batchNo", HMSUtil.convertNullToEmptyString(storeIssueT.getBatchNo()));
					data.put("expiryDate", HMSUtil.convertNullToEmptyString(expiryDate));
					data.put("prescribedBy", prescribedBy);
					data.put("pharmascist", pharmascist);
					issueList.add(data);
				}
				obj.put("count", count);
				obj.put("dailyIssueList", issueList);
			}
		}
		return obj.toString();
	}

	@Override
	public Map<String, Object> getAvailableStock(String jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		
		JSONObject json = new JSONObject(jsondata);
		String dispId = String.valueOf(json.get("departmentId"));
		Map<String,Object> responseMap=new HashMap<String, Object>();
		String itemId = json.get("itemId").toString();
		String mmuId="";
		if(json.has("flag"))
		{
			 mmuId = json.get("mmuId1").toString();
		}
		else
		{
			 mmuId = json.get("mmuId").toString();
		}
		
		Map<String, Object> inputJson = new HashMap<String, Object>();
		inputJson.put("mmuId", mmuId);
		inputJson.put("department_id", dispId);
		inputJson.put("item_id", itemId);
		String stockData = getBatchDetail(inputJson);
		JSONObject jobj = new JSONObject(stockData);
		JSONObject jobject =  (JSONObject) jobj.get("batchData");
		int storeStock = (int) jobject.get("store_stock");
		int availableStock = (int) jobject.get("disp_stock");
		responseMap.put("storeStock", storeStock);
		responseMap.put("dispStock", availableStock);
		responseMap.put("availableStock", availableStock);
		responseMap.put("expiry_date", jobject.get("expiry_date"));
		responseMap.put("status", 1);
		return responseMap;
	}

	@Override
	public String getDepartmentIdAgainstCode(Map<String, Object> payload) {
		Map<String,Object> map =  dispensaryDao.getDepartmentIdAgainstCode(payload);
		String departmentId = String.valueOf(map.get("departmentId"));
		JSONObject obj = new JSONObject();
		obj.put("departmentId", departmentId);
		return obj.toString();
	}
	@Override
	public String getItemTypeId(Map<String, Object> payload) {
		JSONObject obj = new JSONObject();
		Long id = dispensaryDao.getItemTypeId(payload);
		if(id != null) {
			obj.put("itemId", String.valueOf(id));
		}else {
			obj.put("itemId", "");
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getRegisteredPatientList(Map<String, Object> payload) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> patientList = new ArrayList<Map<String, Object>>();
		try {
			
			response = dispensaryDao.getRegisteredPatientList(payload);
			//List<Patient> list = (List<Patient>) response.get("list");
			List<Object[]> list = (List<Object[]>) response.get("list");
			int count = (int) response.get("count");
			if (!list.isEmpty()) {
				/*for (Patient patient : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("id", patient.getPatientId());
					map2.put("name", patient.getPatientName());
					map2.put("age", HMSUtil.calculateAgeFromDOB(patient.getDateOfBirth()));
					map2.put("mobileNo", patient.getMobileNumber());
					map2.put("gender", patient.getMasAdministrativeSex().getAdministrativeSexName());
					patientList.add(map2);
				}
				response.put("status", true);
				response.put("count", count);
				response.put("list", patientList);
			} */
			for (Object[] patient : list) {
				Map<String, Object> map2 = new HashMap<>();
				
				if(patient[0]!=null)
				map2.put("id", patient[0]);
				else
					map2.put("id", "");
				
				if(patient[1]!=null)
				map2.put("name", patient[1]);
				else
					map2.put("name", "");
				
				if(patient[2]!=null) {
					SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");	
					Date date=oldFormat.parse(patient[2].toString());
					map2.put("age", HMSUtil.calculateAgeFromDOB(date));
				}
				else {
					map2.put("age","");
				}
				if(patient[3]!=null)
				map2.put("mobileNo", patient[3]);
				else
					map2.put("mobileNo", "");
				if(patient[4]!=null)	
					map2.put("gender", patient[4]);
				else
					map2.put("gender", "");
				
				patientList.add(map2);
			}
			response.put("status", true);
			response.put("count", count);
			response.put("list", patientList);
		}
			
			
			else {
				response.put("status", true);
				response.put("count", count);
				response.put("list", patientList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("count", "0");
		response.put("list", patientList);
		return response;
	}

	@Override
	public Map<String, Object> getRegisteredPatientDetail(Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> patientList = new ArrayList<Map<String, Object>>();
		try {
			
			Map<String, Object> data = dispensaryDao.getRegisteredPatientDetail(requestData);
			Patient patient = (Patient) data.get("patient");
			if (patient != null) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("id", patient.getPatientId());
				map2.put("name", patient.getPatientName());
				map2.put("age", HMSUtil.calculateAgeFromDOB(patient.getDateOfBirth()));
				map2.put("mobileNo", patient.getMobileNumber());
				map2.put("gender", patient.getMasAdministrativeSex().getAdministrativeSexName());
				patientList.add(map2);
				response.put("status", true);
				response.put("list", patientList);
			} else {
				response.put("status", true);
				response.put("list", patientList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", patientList);
		return response;
	}

	@Override
	public Map<String, Object> updateRegistrationDetail(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = dispensaryDao.updatePatientInformation(requestData);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> getPendingListForAuditor(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response) {
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNo = 0;

		if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") != null) {
			pageNo = Long.parseLong(requestData.get("PN").toString());
		}

		responseMap = dispensaryDao.getPendingListForAuditor(pageNo, requestData);
		int count = 1;
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentMList = (List<StoreInternalIndentM>) responseMap.get("storeInternalIndentMList");
			if (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0) {
				for (StoreInternalIndentM storeInternalIndentM : storeInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("indentId", storeInternalIndentM.getId());
					map1.put("indentNo", storeInternalIndentM.getDemandNo());
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentM.getDemandDate()));
					map1.put("fromDept", storeInternalIndentM.getDepartment1().getDepartmentName());// need to change
					map1.put("mmuName", storeInternalIndentM.getMasMMU().getMmuName());// need to change
					//map1.put("toDeptId", storeInternalIndentM.getDepartment2().getDepartmentId());
					map1.put("createdBy", storeInternalIndentM.getUser2().getUserName());
					if(storeInternalIndentM.getUser2().getMasUserType() != null) {
						map1.put("createdByDsg", storeInternalIndentM.getUser2().getMasUserType().getUserTypeName());
					}
					map1.put("statusId", storeInternalIndentM.getStatus());
					if (storeInternalIndentM.getStatus().equalsIgnoreCase("N"))
						map1.put("status", "Pending for Submission");
					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("Y"))
						map1.put("status", "Pending for Approval");

					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("A")) {
						map1.put("status", "Pending For Issuer");

					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("R")) {
						map1.put("status", "Rejected");

					}
					data.put(count++, map1);

				}

			}

		}
		Integer tm = (Integer) responseMap.get("totalMatches");
		map.put("data", data);
		map.put("count", tm);
		map.put("status", "1");
		return map;
	}

	@Override
	public Map<String, Object> getPendingListForCO(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response) {
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNo = 0;

		if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") != null) {
			pageNo = Long.parseLong(requestData.get("PN").toString());
		}

		responseMap = dispensaryDao.getPendingListForCO(pageNo, requestData);
		int count = 1;
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentMList = (List<StoreInternalIndentM>) responseMap.get("storeInternalIndentMList");
			if (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0) {
				for (StoreInternalIndentM storeInternalIndentM : storeInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("indentId", storeInternalIndentM.getId());
					map1.put("indentNo", storeInternalIndentM.getDemandNo());
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentM.getDemandDate()));
					map1.put("fromDept", storeInternalIndentM.getDepartment1().getDepartmentName());// need to change
					map1.put("mmuName", storeInternalIndentM.getMasMMU().getMmuName());// need to change
					//map1.put("toDeptId", storeInternalIndentM.getDepartment2().getDepartmentId());
					map1.put("createdBy", storeInternalIndentM.getUser2().getUserName());
					if(storeInternalIndentM.getUser2().getMasUserType() != null) {
						map1.put("createdByDsg", storeInternalIndentM.getUser2().getMasUserType().getUserTypeName());
					}
					if(storeInternalIndentM.getApmUser() != null) {
						map1.put("approvedBy", storeInternalIndentM.getAuditorUser().getUserName());
					}else {
						map1.put("approvedBy", "");
					}
					if(storeInternalIndentM.getApmUser() != null && storeInternalIndentM.getApmUser().getMasUserType() != null) {
						map1.put("approvedByDsg", storeInternalIndentM.getAuditorUser().getMasUserType().getUserTypeName());
					}
					map1.put("statusId", storeInternalIndentM.getStatus());
					if (storeInternalIndentM.getStatus().equalsIgnoreCase("N"))
						map1.put("status", "Pending for Submission");
					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("Y"))
						map1.put("status", "Pending for Approval");

					else if (storeInternalIndentM.getStatus().equalsIgnoreCase("A")) {
						map1.put("status", "Pending For Issuer");

					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("R")) {
						map1.put("status", "Rejected");

					}
					data.put(count++, map1);

				}

			}

		}
		Integer tm = (Integer) responseMap.get("totalMatches");
		map.put("data", data);
		map.put("count", tm);
		map.put("status", "1");
		return map;
	}

	@Override
	public Map<String, Object> displayItemListCO(Map<String, Object> requestData) {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Map<String,Object>> responseList = new ArrayList<>();
		Map<String,Object> map = dispensaryDao.displayItemListCO(requestData);
		if(!map.isEmpty()) {
			List<Object[]> list = (List<Object[]>) map.get("list");
			if(!list.isEmpty()) {
				for(Object[] storeT : list) {
					Long itemId = (Long) storeT[0];
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("nomenclature", storeT[1]);
					data.put("pvmsNo",storeT[6]);
					data.put("au",storeT[2]);
					data.put("availableQuantity",storeT[4]);
					data.put("requiredQty",storeT[3]);
					data.put("approvedQty",HMSUtil.convertNullToEmptyString(storeT[5]));
					data.put("itemId",itemId);
					
					responseList.add(data);
					
					response.put("status", true);
					response.put("data", responseList);   
				}
			}else {
				response.put("status", false);
				response.put("msg", "Item List is not available");
				response.put("data", responseList); 
			}
		}else {
			response.put("status", false);
			response.put("msg", "Item List is not available");
			response.put("data", responseList); 
		}
		return response;
	}

	@Override
	public Map<String, Object> forwardToDisctrict(Map<String, Object> requestData) {
		
		Map<String, Object> responseMap = new HashMap<>();
		try {
			responseMap = dispensaryDao.forwardToDisctrict(requestData);
			
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseMap;
	}
	
	@Override
	public Map<String, Object> getPendingListForDO(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response) {
		List<StoreCoInternalIndentM> storeCoInternalIndentMList = new ArrayList<StoreCoInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNo = 0;

		if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") != null) {
			pageNo = Long.parseLong(requestData.get("PN").toString());
		}

		responseMap = dispensaryDao.getPendingListForDO(pageNo, requestData);
		int count = (int) responseMap.get("count");
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeCoInternalIndentMList = (List<StoreCoInternalIndentM>) responseMap.get("list");
			if (!storeCoInternalIndentMList.isEmpty() && storeCoInternalIndentMList.size() > 0) {
				for (StoreCoInternalIndentM storeCoInternalIndentM : storeCoInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("storeCoMId", storeCoInternalIndentM.getId());
					map1.put("indentNo", HMSUtil.convertNullToEmptyString(storeCoInternalIndentM.getDemandNo()));
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeCoInternalIndentM.getDemandDate()));
					map1.put("cityName", storeCoInternalIndentM.getMasCity().getCityName());
					map1.put("createdBy", storeCoInternalIndentM.getLastChgBy().getUserName());// need to change
					responseList.add(map1);
				}
			}
		}
		map.put("data", responseList);
		map.put("count", count);
		map.put("status", true);
		return map;
	}

	@Override
	public Map<String, Object> indentValidationDO(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();

		responseMap = dispensaryDao.indentValidationDO(payload);
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			List<StoreCoInternalIndentT> storeCoInternalIndentTList = (List<StoreCoInternalIndentT>) responseMap.get("list");
			if (!storeCoInternalIndentTList.isEmpty() && storeCoInternalIndentTList.size() > 0) {
				for (StoreCoInternalIndentT storeCoInternalIndentT : storeCoInternalIndentTList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					
					map1.put("storeCoMId", storeCoInternalIndentT.getStoreCoInternalIndentM1().getId());
					map1.put("storeCoTId", storeCoInternalIndentT.getId());
					map1.put("nomenclature", storeCoInternalIndentT.getMasStoreItem().getNomenclature());
					map1.put("pvmsNo", storeCoInternalIndentT.getMasStoreItem().getPvmsNo());
					map1.put("au", storeCoInternalIndentT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
					map1.put("requiredQty", storeCoInternalIndentT.getQtyRequest());
					map1.put("availableStock", storeCoInternalIndentT.getAvailableStock());
					map1.put("approvedQty", HMSUtil.convertNullToEmptyString(storeCoInternalIndentT.getApprovedQty()));
					map1.put("itemId", storeCoInternalIndentT.getMasStoreItem().getItemId());
					responseList.add(map1);
				}
			}
		}
		map.put("data", responseList);
		map.put("status", true);
		return map;
	}

	@Override
	public Map<String, Object> updateIndentDispenceryByDO(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			responseMap = dispensaryDao.updateIndentDispenceryByDO(jsondata);
			
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> displayItemListDO(Map<String, Object> requestData) {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Map<String,Object>> responseList = new ArrayList<>();
		Map<String,Object> map = dispensaryDao.displayItemListDO(requestData);
		if(!map.isEmpty()) {
			List<Object[]> list = (List<Object[]>) map.get("list");
			if(!list.isEmpty()) {
				for(Object[] storeT : list) {
					Long itemId = (Long) storeT[0];
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("nomenclature", storeT[1]);
					data.put("au",storeT[2]);
					data.put("availableQuantity",storeT[4]);
					data.put("requiredQty",storeT[3]);
					data.put("approvedQty",HMSUtil.convertNullToEmptyString(storeT[5]));
					data.put("pvmsNo",HMSUtil.convertNullToEmptyString(storeT[6]));
					data.put("itemId",itemId);
					responseList.add(data);
					
					response.put("status", true);
					response.put("data", responseList);   
				}
			}else {
				response.put("status", false);
				response.put("msg", "Item List is not available");
				response.put("data", responseList);   
			}
		}else {
			response.put("status", false);
			response.put("msg", "Item List is not available");
			response.put("data", responseList);   
		}
		return response;
	}

	@Override
	public Map<String, Object> mmuWiseIndentDetail(Map<String, Object> requestData) {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = dispensaryDao.mmuWiseIndentDetail(requestData);
		//Boolean status = (Boolean) map.get("status");
		if(!map.isEmpty()) {
			List<StoreInternalIndentT> list = (List<StoreInternalIndentT>) map.get("list");
			for(StoreInternalIndentT storeCoT: list) {
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("mmuName", storeCoT.getStoreInternalIndentM1().getMasMMU().getMmuName());
				data.put("indentNo", HMSUtil.convertNullToEmptyString(storeCoT.getStoreInternalIndentM1().getDemandNo()));
				data.put("indentDate", HMSUtil.changeDateToddMMyyyy(storeCoT.getStoreInternalIndentM1().getDemandDate()));
				data.put("requiredQty", storeCoT.getQtyRequest());
				data.put("availableQty", storeCoT.getAvailableStock());
				data.put("approvedQty", storeCoT.getApprovedQty());
				data.put("nomenclature", storeCoT.getMasStoreItem().getNomenclature());
				responseList.add(data);
			}
			response.put("status", true);
			response.put("list", responseList);
			response.put("msg","Success.");
		}else {
			response.put("status", false);
			response.put("msg","Something went wrong.");
		}
		
		return response;
	}

	@Override
	public Map<String, Object> submitDoItemsAndGeneratePo(Map<String, Object> requestData) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			responseMap = dispensaryDao.submitDoItemsAndGeneratePo(requestData);
			
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getCityWiseIndentList(Map<String, Object> requestData) {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = dispensaryDao.getCityWiseIndentList(requestData);
		//Boolean status = (Boolean) map.get("status");
		if(!map.isEmpty()) {
			List<StoreCoInternalIndentT> list = (List<StoreCoInternalIndentT>) map.get("list");
			for(StoreCoInternalIndentT storeCoT: list) {
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("cityName", storeCoT.getStoreCoInternalIndentM1().getMasCity().getCityName());
				data.put("requiredQty", storeCoT.getQtyRequest());
				data.put("availableQty", storeCoT.getAvailableStock());
				data.put("approvedQty", storeCoT.getApprovedQty());
				data.put("nomenclature", storeCoT.getMasStoreItem().getNomenclature());
				data.put("itemId",storeCoT.getMasStoreItem().getItemId());
				responseList.add(data);
			}
			response.put("status", true);
			response.put("list", responseList);
			response.put("msg","Success.");
		}else {
			response.put("status", false);
			response.put("msg","Something went wrong.");
		}
		
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMasSupplierList(Map<String, Object> requestData) {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = dispensaryDao.getMasSupplierList(requestData);
		if(!map.isEmpty()) {
			List<MasStoreSupplier> list = (List<MasStoreSupplier>) map.get("list");
			for(MasStoreSupplier supplier: list) {
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("name", supplier.getSupplierName());
				data.put("id", supplier.getSupplierId());
				responseList.add(data);
			}
			response.put("status", true);
			response.put("list", responseList);
			response.put("msg","Success.");
		}else {
			response.put("status", false);
			response.put("msg","Something went wrong.");
		}
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMasSupplierListNew(Map<String, Object> requestData) {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = dispensaryDao.getMasSupplierListNew(requestData);
		if(!map.isEmpty()) {
			List<MasStoreSupplierNew> list = (List<MasStoreSupplierNew>) map.get("list");
			for(MasStoreSupplierNew supplier: list) {
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("name", supplier.getSupplierName());
				data.put("id", supplier.getSupplierId());
				responseList.add(data);
			}
			response.put("status", true);
			response.put("list", responseList);
			response.put("msg","Success.");
		}else {
			response.put("status", false);
			response.put("msg","Something went wrong.");
		}
		
		return response;
	}

	@Override
	public Map<String, Object> getMasSupplierTypeList(Map<String, Object> requestData) {
		Map<String,Object> response = new HashMap<String, Object>();
		List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = dispensaryDao.getMasSupplierTypeList(requestData);
		//Boolean status = (Boolean) map.get("status");
		if(!map.isEmpty()) {
			List<MasStoreSupplierType> list = (List<MasStoreSupplierType>) map.get("list");
			for(MasStoreSupplierType supplierType: list) {
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("name", supplierType.getSupplierTypeName());
				data.put("id", supplierType.getSupplierTypeId());
				responseList.add(data);
			}
			response.put("status", true);
			response.put("list", responseList);
			response.put("msg","Success.");
		}else {
			response.put("status", false);
			response.put("msg","Something went wrong.");
		}
		
		return response;
	}

	@Override
	public String getRvWaitingList(Map<String, Object> payload) {
		JSONObject obj = new JSONObject();
		Map<String, Object> map = dispensaryDao.getRvWaitingList(payload);
		if (!map.isEmpty()) {
			int count = Integer.parseInt(String.valueOf(map.get("count")));
			List<StoreDoInternalIndentM> list = (List<StoreDoInternalIndentM>) map.get("list");
			List<Map<String, Object>> rvList = new ArrayList<>();
			if (list.size() > 0) {
				for (StoreDoInternalIndentM hd : list) {
					Map<String, Object> data = new HashMap<>();
					data.put("id", hd.getId());
					data.put("poNo", HMSUtil.convertNullToEmptyString(hd.getPoNo()));
					data.put("poDate", HMSUtil.changeDateToddMMyyyy(hd.getPoDate()));
					data.put("vendorType", hd.getMasStoreSupplierType().getSupplierTypeName());
					data.put("vendorName", hd.getMasStoreSupplierNew().getSupplierName()); 
					data.put("typeOfPo", hd.getTypeOfPo());
					if(hd.getStatus() == null) {
						data.put("status", "New");
					}else if(hd.getStatus().equalsIgnoreCase("P")) {
						data.put("status", "Partially Received");
					}
					
					rvList.add(data);
				}
				obj.put("list", rvList);
				obj.put("count", count);
			} else {
				obj.put("list", new JSONArray());
				obj.put("count", "0");
			}

		} else {
			obj.put("list", new JSONArray());
			obj.put("count", "0");
		}
		return obj.toString();
	}

	@Override
	public Map<String, Object> rvDetail(HashMap<String, String> payload, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();

		responseMap = dispensaryDao.rvDetail(payload);
		StoreDoInternalIndentM storeM =  (StoreDoInternalIndentM) responseMap.get("hdList");
		map.put("poDate", HMSUtil.changeDateToddMMyyyy(storeM.getPoDate()));
		map.put("poNo",HMSUtil.convertNullToEmptyString(storeM.getPoNo()));
		map.put("vendorType",storeM.getMasStoreSupplierType().getSupplierTypeName());
		map.put("vendorName",storeM.getMasStoreSupplierNew().getSupplierName());
		map.put("rvNotes",storeM.getRvNotes());
		map.put("itemTypeId",storeM.getMasStoreSupplierType().getSupplierTypeId());
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			List<StoreDoInternalIndentT> storeDoInternalIndentTList = (List<StoreDoInternalIndentT>) responseMap.get("list");
			if (!storeDoInternalIndentTList.isEmpty() && storeDoInternalIndentTList.size() > 0) {
				for (StoreDoInternalIndentT storeDoInternalIndentT : storeDoInternalIndentTList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("id", storeDoInternalIndentT.getId());
					map1.put("itemId", storeDoInternalIndentT.getMasStoreItem().getItemId());
					map1.put("nomenclature", storeDoInternalIndentT.getMasStoreItem().getNomenclature());
					map1.put("au", storeDoInternalIndentT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
					map1.put("requiredQty", storeDoInternalIndentT.getQtyRequest());
					map1.put("approvedQty", storeDoInternalIndentT.getApprovedQty());
					map1.put("poQty", storeDoInternalIndentT.getPoQty());
					List<StoreGrnT> storeGrnTList = storeDoInternalIndentT.getStoreGrnTList();
					long totalReceived = 0;
					for(StoreGrnT grnT : storeGrnTList) {
						totalReceived = totalReceived +grnT.getReceivedQty();
					}
					map1.put("totalReceived", totalReceived);
					responseList.add(map1);
				}
			}
		}
		map.put("list", responseList);
		map.put("status", true);
		return map;
	}

	@Override
	public Map<String,Object> indentIssueWaitingListForDO(Map<String, Object> payload) {
		List<StoreCoInternalIndentM> storeCoInternalIndentMList = new ArrayList<StoreCoInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		responseMap = dispensaryDao.indentIssueWaitingListForDO(payload);
		int count = (int) responseMap.get("count");
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeCoInternalIndentMList = (List<StoreCoInternalIndentM>) responseMap.get("list");
			if (!storeCoInternalIndentMList.isEmpty() && storeCoInternalIndentMList.size() > 0) {
				for (StoreCoInternalIndentM storeCoInternalIndentM : storeCoInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("storeCoMId", storeCoInternalIndentM.getId());
					map1.put("indentNo", HMSUtil.convertNullToEmptyString(storeCoInternalIndentM.getDemandNo()));
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeCoInternalIndentM.getDemandDate()));
					map1.put("cityName", storeCoInternalIndentM.getMasCity().getCityName());
					String status = "";
					if(storeCoInternalIndentM.getStatus().equals("A")) {
						status = "Pending for issue";
					}else if(storeCoInternalIndentM.getStatus().equals("P")) {
						status = "Partially issue";
					}
					map1.put("status", status);
					map1.put("createdBy", storeCoInternalIndentM.getLastChgBy().getUserName());// need to change
					responseList.add(map1);
				}
			}
		}
		map.put("data", responseList);
		map.put("count", count);
		map.put("status", true);
		return map;
	}

	@Override
	public String submitRvDetailAgainstPo(Map<String, Object> payload) {
		String data = dispensaryDao.submitRvDetailAgainstPo(payload);
		JSONObject obj = new JSONObject();
		obj.put("msg", data);
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getIndentIssueHeaderDo(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		try {
			Map<String, Object> data = dispensaryDao.getIndentIssueHeaderDo(jsondata);
			if (!data.isEmpty()) {
				List<StoreCoInternalIndentM> list = (List<StoreCoInternalIndentM>) data.get("headerList");
				if (!list.isEmpty()) {
					List<Map<String, Object>> headerList = new ArrayList<>();
					for (StoreCoInternalIndentM indentM : list) {
						Map<String, Object> map = new HashMap<>();
						String cityName = "", requestedBy = "";

						if (indentM.getCoUser() != null) {
							requestedBy = HMSUtil.convertNullToEmptyString(indentM.getCoUser().getUserName());
						}
						
						if (indentM.getMasCity() != null) {
							cityName = HMSUtil.convertNullToEmptyString(indentM.getMasCity().getCityName());
						}
						
						String demandDate = "";
						if(indentM.getDemandDate() != null) {
							demandDate = HMSUtil.changeDateToddMMyyyy(indentM.getDemandDate());
						}
						
						map.put("header_id", indentM.getId());
						map.put("indent_no", HMSUtil.convertNullToEmptyString(indentM.getDemandNo()));
						map.put("indent_date", demandDate);
						map.put("requested_by", requestedBy);
						map.put("cityName", cityName);
						headerList.add(map);
					}
					obj.put("headerList", headerList);
				} else {
					obj.put("headerDetail", new JSONArray());
				}
			} else {
				obj.put("headerDetail", new JSONArray());
			}
			return obj.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getIndentIssueDetailsDo(Map<String, Object> jsondata) {
		JSONObject obj = new JSONObject();
		Session session = null;
		try {
			Long districtId = Long.parseLong(String.valueOf(jsondata.get("districtId")));
			Map<String, Object> data = dispensaryDao.getIndentIssueDetailsDo(jsondata);
			if (!data.isEmpty()) {
				List<StoreCoInternalIndentT> list = (List<StoreCoInternalIndentT>) data.get("detailList");
				if (list != null && !list.isEmpty()) {
					session = getHibernateUtils.getHibernateUtlis().OpenSession();
					List<Map<String, Object>> pt = new ArrayList<>();
					for (StoreCoInternalIndentT items : list) {
						Map<String, Object> map = new HashMap<>();
						String pvsmNo = "", nomenclature = "", au = "", auId = "", itemId = "";
						if (items.getMasStoreItem() != null) {
							pvsmNo = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getPvmsNo());
							nomenclature = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getNomenclature());
							itemId = HMSUtil.convertNullToEmptyString(items.getMasStoreItem().getItemId());
							if (items.getMasStoreItem().getMasStoreUnit() != null) {
								au = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitName());
								auId = HMSUtil.convertNullToEmptyString(
										items.getMasStoreItem().getMasStoreUnit().getStoreUnitId());
							}
						}

						map.put("detail_id", items.getId());
						map.put("header_id", items.getStoreCoInternalIndentM1().getId());
						map.put("pvsm_no", HMSUtil.convertNullToEmptyString(pvsmNo));
						map.put("nomenclature", HMSUtil.convertNullToEmptyString(nomenclature));
						map.put("au", HMSUtil.convertNullToEmptyString(au));
						map.put("au_id", auId);
						map.put("qty_requested", HMSUtil.convertNullToEmptyString(items.getApprovedQty()));
						map.put("item_id", HMSUtil.convertNullToEmptyString(itemId));
						map.put("item_id", HMSUtil.convertNullToEmptyString(itemId));
						if(items.getQtyIssued() == null) {
							map.put("previousQtyissued", 0);
						}else {
							map.put("previousQtyissued", items.getQtyIssued());
						}
						// get batch detail & dispensary stock
						int dispStock = 0;
						ArrayList<Map<String, Object>> batchList = new ArrayList<>();
						int expiryDays = Integer.parseInt(HMSUtil.getProperties("adt.properties", "EXPIRY_DAYS"));
						Date date = new Date();
						Calendar c = Calendar.getInstance();
						c.setTime(date);
						c.add(Calendar.DATE, expiryDays);
						date = c.getTime();
						String sql = "select  stock_id, BATCH_NO, EXPIRY_DATE, CLOSING_STOCK, MANUFACTURE_DATE from STORE_ITEM_BATCH_STOCK where ITEM_ID =:item_id and district_id =:districtId and CLOSING_STOCK > 0 and EXPIRY_DATE >:exp_date";
						Query query = session.createSQLQuery(sql);
						query.setLong("item_id", Long.parseLong(itemId));
						query.setLong("districtId", districtId);
						query.setDate("exp_date", date);
						List<Object[]> result = query.list();
						for (Object[] row : result) {
							Map<String, Object> batchData = new HashMap<>();
							if (row[3] != null) {
								dispStock += ((Number) row[3]).intValue();
							}
							String stockId = "", batchNo = "", expiryDate = "", batchStock = "", manufacturingDate = "";
							if (row[0] != null) {
								stockId = row[0].toString();
							}
							batchData.put("stock_id", stockId);

							if (row[1] != null) {
								batchNo = row[1].toString();
							}
							batchData.put("batch_no", batchNo);

							if (row[2] != null) {
								expiryDate = HMSUtil.changeDateToddMMyyyy((Date) row[2]);
							}
							batchData.put("expiry_date", expiryDate);
							if (row[4] != null) {
								manufacturingDate = HMSUtil.changeDateToddMMyyyy((Date) row[4]);
							}
							batchData.put("date_of_manufacturing", manufacturingDate);

							if (row[3] != null) {
								Long batchStockCount = (long) (Double.parseDouble(row[3].toString()));
								batchStock = batchStockCount.toString();
							}
							batchData.put("batch_stock", batchStock);
							batchList.add(batchData);
						}

						// sort by expiry date
						Collections.sort(batchList, new Comparator<Map<String, Object>>() {
							public int compare(Map<String, Object> m1, Map<String, Object> m2) {

								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
								Date data1 = null;
								Date date2 = null;
								try {
									data1 = formatter.parse((String) m1.get("expiry_date"));
									date2 = formatter.parse((String) m2.get("expiry_date"));
								} catch (Exception pe) {
									pe.printStackTrace();
								}

								return (data1).compareTo(date2); // ascending order
								// return ((String) m2.get("date_of_expiry")).compareTo((String)
								// m1.get("date_of_expiry")); //descending order
							}
						});

						// get store stock
						map.put("batchList", batchList);
						map.put("disp_stock", dispStock);
						if (dispStock > 0) {
							map.put("disp_stock", dispStock);
						} else {
							map.put("disp_stock", 0);
						}

						pt.add(map);
					}
					obj.put("detailList", pt);
				} else {
					obj.put("detailList", new JSONArray());
				}
			} else {
				obj.put("detailList", new JSONArray());
			}
			return obj.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj.toString();
	}

	@Override
	public String indentIssueDo(Map<String, Object> jsondata) {
		String data = dispensaryDao.indentIssueDo(jsondata);
		JSONObject obj = new JSONObject();
		obj.put("msg", data);
		return obj.toString();
	}

	@Override
	public Map<String, Object> getIndentListForTrackingCo(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response) {
		List<StoreInternalIndentM> storeInternalIndentMList = new ArrayList<StoreInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNo = 0;

		if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") != null) {
			pageNo = Long.parseLong(requestData.get("PN").toString());
		}

		responseMap = dispensaryDao.getIndentListCo(pageNo, requestData);
		int count = 1;
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentMList = (List<StoreInternalIndentM>) responseMap.get("storeInternalIndentMList");
			if (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0) {
				String[] coStatusArr = {"U","C","Z","P"};
				List<String> coStatusList = Arrays.asList(coStatusArr);
				for (StoreInternalIndentM storeInternalIndentM : storeInternalIndentMList) {
					if(storeInternalIndentM.getCoId() == null) {
						continue; 
					}else {
						if(coStatusList.contains(storeInternalIndentM.getStatus()) || storeInternalIndentM.getCoFlag().equalsIgnoreCase("I")) {
							
						}else {
							continue;
						}
					}
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("indentId", storeInternalIndentM.getId());
					map1.put("indentNo", storeInternalIndentM.getDemandNo());
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeInternalIndentM.getDemandDate()));
					
					map1.put("approvedBy", "");// need to change
					map1.put("statusId", storeInternalIndentM.getStatus());
					map1.put("createdBy", storeInternalIndentM.getUser1().getUserName());
					
					if (storeInternalIndentM.getStatus().equalsIgnoreCase("U")) {
						map1.put("status", "Pending For CO Approval");
						if(storeInternalIndentM.getAuditorUser() != null) {
							map1.put("approvedBy",storeInternalIndentM.getAuditorUser().getUserName());
						}
					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("C")) {
						map1.put("status", "Pending for Issue");
					} else if (storeInternalIndentM.getStatus().equalsIgnoreCase("Z")) {
						map1.put("status", "Rejected by CO");
						if(storeInternalIndentM.getCoUser() != null) {
							map1.put("approvedBy",storeInternalIndentM.getCoUser().getUserName());
						}
					}else if (storeInternalIndentM.getCoFlag() != null && storeInternalIndentM.getCoFlag().equalsIgnoreCase("I")) {
						map1.put("status", "Issued");
					}else if (storeInternalIndentM.getCoFlag() != null && storeInternalIndentM.getCoFlag().equalsIgnoreCase("O")) {
						map1.put("status", "Received");
					}else if(storeInternalIndentM.getCoFlag() != null && storeInternalIndentM.getCoFlag().equalsIgnoreCase("P")) {
						map1.put("status", "Partially Issued");
					}

					data.put(count++, map1);
				}
			}
			Integer tm = (Integer) responseMap.get("totalMatches");
			map.put("data", data);
			map.put("count", tm);

		}
		return map;
	}

	@Override
	public Map<String, Object> getIndentListForTrackingDo(HashMap<String, String> requestData, HttpServletRequest request,
			HttpServletResponse response) {
		List<StoreDoInternalIndentM> storeInternalIndentMList = new ArrayList<StoreDoInternalIndentM>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		long pageNo = 0;

		if (!requestData.get("PN").toString().isEmpty() && requestData.get("PN") != null) {
			pageNo = Long.parseLong(requestData.get("PN").toString());
		}

		responseMap = dispensaryDao.getIndentListDo(pageNo, requestData);
		int count = 1;
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentMList = (List<StoreDoInternalIndentM>) responseMap.get("storeInternalIndentMList");
			if (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0) {
				for (StoreDoInternalIndentM storeDoInternalIndentM : storeInternalIndentMList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("indentId", storeDoInternalIndentM.getId());
					map1.put("indentNo", storeDoInternalIndentM.getDemandNo());
					map1.put("indentdate", HMSUtil.changeDateToddMMyyyy(storeDoInternalIndentM.getDemandDate()));
					
					map1.put("approvedBy", "");// need to change
					map1.put("statusId", storeDoInternalIndentM.getStatus());
					map1.put("createdBy", "");
					
					if (storeDoInternalIndentM.getStatus() == null) {
						map1.put("status", "Pending for RV");
					}else if (storeDoInternalIndentM.getStatus().equalsIgnoreCase("P")) {
						map1.put("status", "Partially received against RV");
					}else if (storeDoInternalIndentM.getStatus().equalsIgnoreCase("I")) {
						map1.put("status", "Pending For Issue");
					}
					
					data.put(count++, map1);
				}
			}
			Integer tm = (Integer) responseMap.get("totalMatches");
			map.put("data", data);
			map.put("count", tm);

		}
		return map;
	}

	@Override
	public String getDrugList(Map<String, Object> payload) {
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		JSONObject json = new JSONObject();
		
		responseMap = dispensaryDao.getDrugList(payload);
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			List<StoreItemBatchStock> storeItemBatchList = (List<StoreItemBatchStock>) responseMap.get("list");
			if (!storeItemBatchList.isEmpty() && storeItemBatchList.size() > 0) {
				for (StoreItemBatchStock storeItemBatchStock : storeItemBatchList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", storeItemBatchStock.getStockId());
					map.put("drugName", storeItemBatchStock.getMasStoreItem().getNomenclature());
					if(storeItemBatchStock.getMasStoreItem().getMasStoreUnit1() != null) {
						map.put("au", storeItemBatchStock.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
					}else {
						map.put("au", "");
					}
					map.put("batchNo", storeItemBatchStock.getBatchNo());
					map.put("availableStock", storeItemBatchStock.getClosingStock());
					map.put("previousUnitRate", HMSUtil.convertNullToEmptyString(storeItemBatchStock.getMrp()));
					map.put("expiryDate", HMSUtil.changeDateToddMMyyyy(storeItemBatchStock.getExpiryDate()));
					responseList.add(map);
				}
			}
			
			json.put("list", responseList);

		}
		return json.toString();
	}

	@Override
	public String updateUnitRate(Map<String, Object> payload) {
		String data = dispensaryDao.updateUnitRate(payload);
		JSONObject obj = new JSONObject();
		obj.put("msg", data);
		return obj.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentDetailsForTracking(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

		List<StoreInternalIndentT> storeInternalIndentTList = new ArrayList<StoreInternalIndentT>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject(jsondata);
		long indentMid = Long.parseLong(json.getString("indentId"));
		responseMap = dispensaryDao.getIndentDetailsForTracking(jsondata);
		int count = 1;
		String pvmsNo = "";
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentTList = (List<StoreInternalIndentT>) responseMap.get("storeInternalIndentTList");
			if (!storeInternalIndentTList.isEmpty() && storeInternalIndentTList.size() > 0) {
				for (StoreInternalIndentT storeInternalIndentT : storeInternalIndentTList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("status", storeInternalIndentT.getStoreInternalIndentM1().getStatus());
					map1.put("id", storeInternalIndentT.getId());
					map1.put("indentNo", storeInternalIndentT.getStoreInternalIndentM1().getDemandNo());
					map1.put("indentMid", storeInternalIndentT.getStoreInternalIndentM1().getId());
					map1.put("indentdate", HMSUtil
							.changeDateToddMMyyyy(storeInternalIndentT.getStoreInternalIndentM1().getDemandDate()));
					map1.put("fromDept",
							storeInternalIndentT.getStoreInternalIndentM1().getDepartment1().getDepartmentName());// need
					map1.put("createdBy", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getUserName());
					if(storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType() != null) {
						map1.put("createdByDsg", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType().getUserTypeName());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getApmUser() != null) {
						map1.put("approvedBy", storeInternalIndentT.getStoreInternalIndentM1().getApmUser().getUserName());
						if(storeInternalIndentT.getStoreInternalIndentM1().getApmUser().getMasUserType() != null) {
							map1.put("approvedByDsg", storeInternalIndentT.getStoreInternalIndentM1().getApmUser().getMasUserType().getUserTypeName());
						}
					}
					pvmsNo = storeInternalIndentT.getMasStoreItem().getNomenclature() + "["
							+ storeInternalIndentT.getMasStoreItem().getPvmsNo() + "]";
					map1.put("NomPvmsNo", pvmsNo);
					map1.put("accountingUnit",
							storeInternalIndentT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
					map1.put("requiredQty", storeInternalIndentT.getQtyRequest());
					map1.put("availableStock", storeInternalIndentT.getAvailableStock());
					map1.put("stockDisp", storeInternalIndentT.getDispStock());
					map1.put("stockStore", storeInternalIndentT.getStoresStock());
					map1.put("remark", storeInternalIndentT.getReasonForDemand());
					map1.put("itemId", storeInternalIndentT.getMasStoreItem().getItemId());
					map1.put("pvmsNo", storeInternalIndentT.getMasStoreItem().getPvmsNo());
					if(storeInternalIndentT.getQtyIssued() == null) {
						map1.put("issuedQty", "0");
					}else {
						map1.put("issuedQty", HMSUtil.convertNullToEmptyString(storeInternalIndentT.getQtyIssued()));
					}
					
					map1.put("receivedQty", HMSUtil.convertNullToEmptyString(storeInternalIndentT.getQtyReceived()));
					
				}
			}

		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIndentDetailsForTrackingCo(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

		List<StoreInternalIndentT> storeInternalIndentTList = new ArrayList<StoreInternalIndentT>();
		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<Integer, Map<String, Object>> data = new HashMap<Integer, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = new JSONObject(jsondata);
		long indentMid = Long.parseLong(json.getString("indentId"));
		responseMap = dispensaryDao.getIndentDetailsForTrackingCo(jsondata);
		int count = 1;
		String pvmsNo = "";
		if (responseMap.size() > 0 && !responseMap.isEmpty()) {
			storeInternalIndentTList = (List<StoreInternalIndentT>) responseMap.get("storeInternalIndentTList");
			if (!storeInternalIndentTList.isEmpty() && storeInternalIndentTList.size() > 0) {
				for (StoreInternalIndentT storeInternalIndentT : storeInternalIndentTList) {
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("status", storeInternalIndentT.getStoreInternalIndentM1().getStatus());
					map1.put("id", storeInternalIndentT.getId());
					map1.put("indentNo", storeInternalIndentT.getStoreInternalIndentM1().getDemandNo());
					map1.put("indentMid", storeInternalIndentT.getStoreInternalIndentM1().getId());
					map1.put("indentdate", HMSUtil
							.changeDateToddMMyyyy(storeInternalIndentT.getStoreInternalIndentM1().getDemandDate()));
					map1.put("createdBy", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getUserName());
					if(storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType() != null) {
						map1.put("createdByDsg", storeInternalIndentT.getStoreInternalIndentM1().getUser2().getMasUserType().getUserTypeName());
					}
					if(storeInternalIndentT.getStoreInternalIndentM1().getCoUser() != null) {
						map1.put("approvedBy", storeInternalIndentT.getStoreInternalIndentM1().getCoUser().getUserName());
					}
					pvmsNo = storeInternalIndentT.getMasStoreItem().getNomenclature() + "["
							+ storeInternalIndentT.getMasStoreItem().getPvmsNo() + "]";
					map1.put("NomPvmsNo", pvmsNo);
					map1.put("accountingUnit",
							storeInternalIndentT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
					map1.put("requiredQty", storeInternalIndentT.getApprovedQty());
					map1.put("availableStock", storeInternalIndentT.getAvailableStock());
					map1.put("remark", storeInternalIndentT.getReasonForDemand());
					map1.put("itemId", storeInternalIndentT.getMasStoreItem().getItemId());
					map1.put("pvmsNo", storeInternalIndentT.getMasStoreItem().getPvmsNo());
					if(storeInternalIndentT.getQtyIssued() == null) {
						map1.put("issuedQty", "0");
					}else {
						map1.put("issuedQty", HMSUtil.convertNullToEmptyString(storeInternalIndentT.getQtyIssued()));
					}
					
					map1.put("receivedQty", HMSUtil.convertNullToEmptyString(storeInternalIndentT.getQtyReceived()));
					
				}
			}

		}
		return map;
	}

	@Override
	public Map<String, Object> getIndentDetailsForTrackingDo(HashMap<String, String> jsondata,
			HttpServletRequest request, HttpServletResponse response) {
		
		return null;
	}

}
