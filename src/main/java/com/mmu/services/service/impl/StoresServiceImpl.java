package com.mmu.services.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.LPProcessDao;
import com.mmu.services.dao.StoresDAO;
import com.mmu.services.entity.MasItemClass;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.MasStoreGroup;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSection;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.MasStoreSupplierType;
import com.mmu.services.entity.MasStoreUnit;
import com.mmu.services.entity.StoreBalanceM;
import com.mmu.services.entity.StoreBalanceT;
import com.mmu.services.entity.StoreCoInternalIndentM;
import com.mmu.services.entity.StoreDoInternalIndentM;
import com.mmu.services.entity.StoreDoInternalIndentT;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreIssueM;
import com.mmu.services.entity.StoreIssueT;
import com.mmu.services.entity.StorePoM;
import com.mmu.services.entity.StorePoT;
import com.mmu.services.entity.StoreSoM;
import com.mmu.services.entity.StoreSoT;
import com.mmu.services.entity.StoreStockTakingM;
import com.mmu.services.entity.StoreStockTakingT;
import com.mmu.services.entity.TempDirectReceivingForBackLp;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.DispensaryService;
import com.mmu.services.service.StoresService;
import com.mmu.services.utils.HMSUtil;



@Service("StoresService")
public class StoresServiceImpl implements StoresService {

	@Autowired
	StoresDAO storesDAO;
	
	@Autowired
	LPProcessDao lPProcessDao;
	
	 @Autowired
	DispensaryService dispenceryService; 
	
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Override
	public Map<String, Object> getStoreItemList(Map<String, String> requestData) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, Object>> storeItemList = new ArrayList<Map<String, Object>>();
		List<Object[]> itemList = new ArrayList<Object[]>();
		long itemGroupId=0;
		long itemTypeId=0;
		long itemSectionId=0;
		long itemCategoryId = 0;
		long itemClassId = 0;
		String pvmsNo="";
		String nomenclturId="";
		
		
		if(requestData.get("itemGroupId")!=null && !requestData.get("itemGroupId").toString().isEmpty()) {
			 itemGroupId = Long.parseLong(requestData.get("itemGroupId"));
		}
		
		if(requestData.get("itemTypeId")!=null && !requestData.get("itemTypeId").toString().isEmpty()) {
			itemTypeId = Long.parseLong(requestData.get("itemTypeId"));
		}
		
		if(requestData.get("itemSectionId")!=null && !requestData.get("itemSectionId").toString().isEmpty()) {
			itemSectionId = Long.parseLong(requestData.get("itemSectionId"));
		}
		
		if(requestData.get("itemCategoryId")!=null && !requestData.get("itemCategoryId").toString().isEmpty()) {
			itemCategoryId = Long.parseLong(requestData.get("itemCategoryId"));
		}
		if(requestData.get("itemClassId")!=null && !requestData.get("itemClassId").toString().isEmpty()) {
			itemClassId = Long.parseLong(requestData.get("itemClassId"));
		}
	
		if(requestData.get("pvmsNo")!=null && !requestData.get("pvmsNo").toString().isEmpty()) {
			pvmsNo = requestData.get("pvmsNo").toString();
		}
		
		if(requestData.get("nomenclturId")!=null && !requestData.get("nomenclturId").toString().isEmpty()) {
			nomenclturId = requestData.get("nomenclturId").toString();
		}
		
		itemList= storesDAO.getStoreItemList(itemGroupId,itemTypeId,itemSectionId,itemCategoryId,itemClassId,pvmsNo,nomenclturId);
		if(!itemList.isEmpty() && itemList!=null) {
			for(Object[] storeItem:itemList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("itemId", storeItem[0]);
				map.put("pvmsNumber", storeItem[1]);
				map.put("nomenclature", storeItem[2]);
				map.put("unitId", storeItem[3]);
				map.put("unitName", storeItem[4]);
				if(storeItem[5]!=null)
				map.put("inactiveForEntry", storeItem[5]);
				else
					map.put("inactiveForEntry", "");
				storeItemList.add(map);
			}
			if (storeItemList != null && storeItemList.size() > 0) {
				dataMap.put("storeItemList", storeItemList);
				dataMap.put("status", 1);
			}
			
		}else {
			dataMap.put("storeItemList", storeItemList);
			dataMap.put("msg", "Data not found");
			dataMap.put("status", 0);
		}
		return dataMap;
		
	}

	@Override
	public String submitStoreData(String requestData) {
		boolean flag=false;
		
		JSONObject jsonRespObject =new JSONObject();
		JSONObject jObject =new JSONObject(requestData);
		
		flag = storesDAO.submitOpeningBalance(jObject); 
		
	
		 if(flag) {
			 jsonRespObject.put("message", "Opening Balance saved successfully.");
			 jsonRespObject.put("status", "1");
			
		 }else {
			 jsonRespObject.put("message", "Opening Balance can not save.");
			 jsonRespObject.put("status", "0");
		 }
		
		return jsonRespObject.toString();
	}

	@Override
	public Map<String, Object> getDataForOpeningBalance() {
		Map<String,Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> groupList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> itemTypeList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> itemClassList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> sectionList = new ArrayList<HashMap<String, Object>>();
		
		List<MasStoreGroup>respStoreGroupList = storesDAO.getGroupList();
			if(!respStoreGroupList.isEmpty() && respStoreGroupList.size()>0) {
				for(MasStoreGroup storeGroup:respStoreGroupList) {
					HashMap<String,Object> storeGroupMap=new HashMap<String, Object>();
					storeGroupMap.put("storeGroupId", storeGroup.getGroupId());
					storeGroupMap.put("storeGroupName", storeGroup.getGroupName());
					groupList.add(storeGroupMap);
				}
				map.put("groupList",groupList);
		}
		
		
		List<MasItemType>respItemTypepList = storesDAO.getItemTypeList();
			if(!respItemTypepList.isEmpty() && respItemTypepList.size()>0) {
				
				for(MasItemType itemType:respItemTypepList) {
					HashMap<String,Object> itemTypeMap=new HashMap<String, Object>();
					itemTypeMap.put("itemTypeId", itemType.getItemTypeId());
					itemTypeMap.put("itemTypeName", itemType.getItemTypeName());
					itemTypeList.add(itemTypeMap);
				}
				map.put("itemTypeList",itemTypeList);
			
		}
		
		List<MasItemClass>respItemClassList = storesDAO.getItemClassList();
			if(!respItemClassList.isEmpty() && respItemClassList.size()>0) {
				
				for(MasItemClass itemClass:respItemClassList) {
					HashMap<String,Object> itemClassMap=new HashMap<String, Object>();
					itemClassMap.put("itemClassId", itemClass.getItemClassId());
					itemClassMap.put("itemClassName", itemClass.getItemClassName());
					itemClassList.add(itemClassMap);
				}
				map.put("itemClassList",itemClassList);
			
		}
			
			
		
		List<MasStoreSection>respStoreSectionList = storesDAO.getStoreSectionList();
			if(!respStoreSectionList.isEmpty() && respStoreSectionList.size()>0) {
				
				for(MasStoreSection storeSection:respStoreSectionList) {
					HashMap<String,Object> storeSectionMap=new HashMap<String, Object>();
					storeSectionMap.put("sectionId", storeSection.getSectionId());
					storeSectionMap.put("sectionName", storeSection.getSectionName());
					sectionList.add(storeSectionMap);
				}
				map.put("sectionList",sectionList);
		}
		
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOpeningBalanceWaitingList(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> waitingDataList = new ArrayList<HashMap<String, Object>>();
		List<StoreBalanceM> waitingList = new ArrayList<StoreBalanceM>();
		List<Integer> totalMatchs = new ArrayList<Integer>();
		
		String fromDate ="";
		String toDate="";
		String mmuId = requestData.get("mmuId").toString();
		String cityId = requestData.get("cityId").toString();
		String districtId = requestData.get("districtId").toString();
		long departmentId = Long.parseLong(requestData.get("departmentId"));
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		
		  if(requestData.containsKey("fromDate")) {
		  fromDate=requestData.get("fromDate").toString(); 
		  }
		  
		  if(requestData.containsKey("toDate")) {
		  toDate=requestData.get("toDate").toString();
		  }
		 

		respMap=storesDAO.getOpeningBalanceWaitingList(fromDate,toDate,mmuId,cityId,districtId,departmentId,pageNo);
		waitingList = (List<StoreBalanceM>) respMap.get("waitingList");
		totalMatchs= (List<Integer>) respMap.get("totalMatches");
		if(!waitingList.isEmpty() && waitingList.size()>0) {
			
			for(StoreBalanceM storeBalance:waitingList) {
				HashMap<String,Object> storeBalanceMap=new HashMap<String, Object>();
				storeBalanceMap.put("Id", storeBalance.getId());
				storeBalanceMap.put("balanceNo", HMSUtil.convertNullToEmptyString(storeBalance.getBalanceNo()));
				storeBalanceMap.put("balanceDate", HMSUtil.changeDateToddMMyyyy(storeBalance.getBalanceDate()));
				storeBalanceMap.put("departmentName", storeBalance.getMasDepartment().getDepartmentName());
				storeBalanceMap.put("opBalanceStatus", storeBalance.getStatus());
				storeBalanceMap.put("submitBy", storeBalance.getUser().getUserName());
				waitingDataList.add(storeBalanceMap);
			}
			map.put("waitingDataList",waitingDataList);
			map.put("count",totalMatchs.size());
			map.put("status",1);
	}else {
		map.put("waitingDataList",waitingDataList);
		map.put("count",totalMatchs.size());
		map.put("status",0);
	}
		
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOpeningBalanceDetailsForApproval(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> openingBalanceListForApproval = new HashMap<String, Object>();
		List<HashMap<String, Object>> storeBalanceMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> storeBalanceTData = new ArrayList<HashMap<String, Object>>();
		Map<String, Object> storeItemData =new HashMap<String, Object>();
		long balanceM_headerId = Long.parseLong(requestData.get("headerId"));
		openingBalanceListForApproval= storesDAO.getOpeningBalanceDetailsForApproval(balanceM_headerId);
		
		List<StoreBalanceM> storeBalanceM = (List<StoreBalanceM>) openingBalanceListForApproval.get("balanceMDetails");
		if(!storeBalanceM.isEmpty() && storeBalanceM.size()>0) {
			for(StoreBalanceM storeBalance:storeBalanceM) {
				HashMap<String,Object> storeBalanceM_map=new HashMap<String, Object>();
				storeBalanceM_map.put("Id", storeBalance.getId());
				storeBalanceM_map.put("balanceNo", storeBalance.getBalanceNo());
				storeBalanceM_map.put("balanceDate", HMSUtil.changeDateToddMMyyyy(storeBalance.getBalanceDate()));
				storeBalanceM_map.put("departmentName", storeBalance.getMasDepartment().getDepartmentName());
				storeBalanceM_map.put("departmentId", storeBalance.getMasDepartment().getDepartmentId());
				storeBalanceM_map.put("submitBy", storeBalance.getUser().getUserName());
				if(storeBalance.getInvoiceNo()!=null && storeBalance.getInvoiceNo()!="")
				{	
				storeBalanceM_map.put("fileName", storeBalance.getFileName());
				storeBalanceM_map.put("invoiceNumber", storeBalance.getInvoiceNo());
				}
				storeBalanceMData.add(storeBalanceM_map);
			}
			map.put("storeBalanceMData", storeBalanceMData);
		}
		
		List<StoreBalanceT> storeBalanceT = (List<StoreBalanceT>) openingBalanceListForApproval.get("balanceTDetails");
		if(!storeBalanceT.isEmpty() && storeBalanceT.size()>0) {
			for(StoreBalanceT balanceT:storeBalanceT) {
				HashMap<String,Object> storeBalanceT_map=new HashMap<String, Object>();
				storeBalanceT_map.put("Id", balanceT.getId());
				storeBalanceT_map.put("itemId", balanceT.getMasStoreItem().getItemId());
				storeBalanceT_map.put("pvmsNumber", balanceT.getMasStoreItem().getPvmsNo());
				storeBalanceT_map.put("nomenclature", balanceT.getMasStoreItem().getNomenclature());
				storeBalanceT_map.put("unitName", balanceT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
				storeBalanceT_map.put("batchNumber", balanceT.getBatchNo());
				if(balanceT.getManufactureDate()!=null) {
					storeBalanceT_map.put("dom", HMSUtil.changeDateToddMMyyyy(balanceT.getManufactureDate()));
				}else {
					storeBalanceT_map.put("dom", "");
				}
				storeBalanceT_map.put("doe", HMSUtil.changeDateToddMMyyyy(balanceT.getExpiryDate()));
				storeBalanceT_map.put("qty", balanceT.getQty());
				storeBalanceT_map.put("unitRate", balanceT.getUnitRate()!=null?balanceT.getUnitRate():"");
				storeBalanceT_map.put("totalAmount", balanceT.getTotalAmount()!=null?balanceT.getTotalAmount():"");
				storeBalanceT_map.put("vendorId",balanceT.getSupplierId()!=null?balanceT.getSupplierId():"");
				storeBalanceT_map.put("vendorTypeId",balanceT.getSupplierTypeId()!=null?balanceT.getSupplierTypeId():"");
				storeBalanceT_map.put("vendorName",balanceT.getMasStoreSupplier()!=null?balanceT.getMasStoreSupplier().getSupplierName():"");
				storeBalanceT_map.put("vendorTypeName",balanceT.getMasStoreSupplierType()!=null?balanceT.getMasStoreSupplierType().getSupplierTypeName():"");
				storeBalanceTData.add(storeBalanceT_map);
			}
			map.put("storeBalanceTData", storeBalanceTData);
		}
		
		storeItemData = getDataForOpeningBalance();
		map.put("storeItemData", storeItemData);
		
		return map;
	}

	@Override
	public String submitOpeningBalanceDataForApproval(String requestData) {
		boolean flag = false;

		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);

		JSONArray balanceMArr = jObject.getJSONArray("balanceMId");
		long balanceMId  = balanceMArr.getLong(0);
		
		JSONArray statusArr = jObject.getJSONArray("actionId");
		String approvalStatus = statusArr.getString(0);
		
		flag = storesDAO.submitOpeningBalanceDataForApproval(jObject);

		if (flag) {
			if(approvalStatus.equalsIgnoreCase("A")) {
				jsonRespObject.put("balanceMId", balanceMId);
				jsonRespObject.put("message", "Opening Balance Approved.");
				jsonRespObject.put("status", "1");
			}else if(approvalStatus.equalsIgnoreCase("R")) {
				jsonRespObject.put("balanceMId", balanceMId);
				jsonRespObject.put("message", "Opening Balance Rejected.");
				jsonRespObject.put("status", "1");
			}

		} else {
			jsonRespObject.put("message", "Some error occured .");
			jsonRespObject.put("balanceMId", 0);
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	}

	@Override
	public Map<String, Object> generateStockStatusReportCo(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> storeStockData = new ArrayList<HashMap<String, Object>>();
		List<Object[]> stockList = new ArrayList<Object[]>();
		long itemId=0;
		long groupId=0;
		long sectionId=0;
		long cityId=0;
		long deptId=0;
		long mmuId=0;
	
		String radioSelectValue= requestData.get("radioValue").toString();
		
		if(Long.parseLong(requestData.get("itemId").toString())!=0) {
			itemId = Long.parseLong(requestData.get("itemId").toString());
		}
		
		/*
		 * if(Long.parseLong(requestData.get("groupId").toString())!=0) { groupId =
		 * Long.parseLong(requestData.get("groupId").toString()); }
		 * 
		 * if(Long.parseLong(requestData.get("sectionId").toString())!=0) { sectionId =
		 * Long.parseLong(requestData.get("sectionId").toString()); }
		 */
		
		if(Long.parseLong(requestData.get("cityId").toString())!=0) {
			cityId = Long.parseLong(requestData.get("cityId").toString());
		}
		
		if(Long.parseLong(requestData.get("deptId").toString())!=0) {
			deptId = Long.parseLong(requestData.get("deptId").toString());
		}
		
		stockList=  (List<Object[]>) storesDAO.generateStockStatusReportCo(radioSelectValue,itemId,groupId,sectionId,cityId,deptId);
		if(stockList!=null && !stockList.isEmpty()) {
			  for(Object[] stock: stockList ) { 
				  HashMap<String,Object> storeStockMap=new HashMap<String, Object>();
				  	storeStockMap.put("receivedQty", stock[0]); 
				  	storeStockMap.put("issuedQty", stock[1]); 
				  	storeStockMap.put("opBalanceQty", stock[2]); 
				  	storeStockMap.put("closingBalanceQty", stock[3]);
				  	storeStockMap.put("pvmsNo", stock[4]); 
				  	storeStockMap.put("nomenclature", stock[5]); 
				  	
				  	if(radioSelectValue.equalsIgnoreCase("D")) {
				  		
				  		storeStockMap.put("batchNo", stock[6]);
					  	storeStockMap.put("expDate", HMSUtil.changeDateToddMMyyyy((Date) stock[7]));
						storeStockMap.put("acUnit", stock[8]);
						if(stock[9]!=null) {
							storeStockMap.put("manufactureDate", HMSUtil.changeDateToddMMyyyy((Date) stock[9]));
						}else {
							storeStockMap.put("manufactureDate", "");
						}
						if(stock[10]!=null) {
							storeStockMap.put("supplierName", stock[10]);
						}else {
							storeStockMap.put("supplierName", "");
						}
						if(stock[11]!=null) {
							storeStockMap.put("supplierTypeName", stock[11]);
						}else {
							storeStockMap.put("supplierTypeName", "");
						}
						
				  	}else {
				  		storeStockMap.put("acUnit", stock[6]);
				  	}
				  	
				/*
				 * if(radioSelectValue.equalsIgnoreCase("D")) {
				 * storeStockMap.put("hospitalName", stock[10]); }else {
				 * storeStockMap.put("hospitalName", stock[7]); }
				 */
				  	
				  	
				  	storeStockData.add(storeStockMap);
			  }
			 
			  if(radioSelectValue.equalsIgnoreCase("D")) {
				  map.put("storeStockData", storeStockData);
				  map.put("reportType", "D");
				  map.put("status", 1);
				 
			  }else {
				  map.put("storeStockData", storeStockData);
				  map.put("reportType", "S");
				  map.put("status", 1); 
				 
			  }
			
		}else {
			map.put("storeStockData", storeStockData);
			map.put("status", 0);
		}
		return map;
	}

	@Override
	public Map<String, Object> getDataForStockStatusReport(Map<String, String> requestData) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, Object>> storeItemList = new ArrayList<Map<String, Object>>();
		List<Object[]> itemList = new ArrayList<Object[]>();
		List<HashMap<String, Object>> groupList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> sectionList = new ArrayList<HashMap<String, Object>>();
		
		
		long itemGroupId=0;
		long itemTypeId=0;
		long itemSectionId=0;
		long itemCategoryId = 0;
		long itemClassId = 0;
		String pvmsNo="";
		String nomenclturId="";
		
		
		List<MasStoreGroup>respStoreGroupList = storesDAO.getGroupList();
		if(!respStoreGroupList.isEmpty() && respStoreGroupList.size()>0) {
			for(MasStoreGroup storeGroup:respStoreGroupList) {
				HashMap<String,Object> storeGroupMap=new HashMap<String, Object>();
				storeGroupMap.put("storeGroupId", storeGroup.getGroupId());
				storeGroupMap.put("storeGroupName", storeGroup.getGroupName());
				groupList.add(storeGroupMap);
			}
			dataMap.put("groupList",groupList);
	}
		
		List<MasStoreSection>respStoreSectionList = storesDAO.getStoreSectionList();
		if(!respStoreSectionList.isEmpty() && respStoreSectionList.size()>0) {
			
			for(MasStoreSection storeSection:respStoreSectionList) {
				HashMap<String,Object> storeSectionMap=new HashMap<String, Object>();
				storeSectionMap.put("sectionId", storeSection.getSectionId());
				storeSectionMap.put("sectionName", storeSection.getSectionName());
				sectionList.add(storeSectionMap);
			}
			dataMap.put("sectionList",sectionList);
	}
		
		
	
		
		if(requestData.get("itemGroupId")!=null && !requestData.get("itemGroupId").toString().isEmpty()) {
			 itemGroupId = Long.parseLong(requestData.get("itemGroupId"));
		}
		
		if(requestData.get("itemTypeId")!=null && !requestData.get("itemTypeId").toString().isEmpty()) {
			itemTypeId = Long.parseLong(requestData.get("itemTypeId"));
		}
		
		if(requestData.get("itemSectionId")!=null && !requestData.get("itemSectionId").toString().isEmpty()) {
			itemSectionId = Long.parseLong(requestData.get("itemSectionId"));
		}
		
		if(requestData.get("itemCategoryId")!=null && !requestData.get("itemCategoryId").toString().isEmpty()) {
			itemCategoryId = Long.parseLong(requestData.get("itemCategoryId"));
		}
		if(requestData.get("itemClassId")!=null && !requestData.get("itemClassId").toString().isEmpty()) {
			itemClassId = Long.parseLong(requestData.get("itemClassId"));
		}
	
		if(requestData.get("pvmsNo")!=null && !requestData.get("pvmsNo").toString().isEmpty()) {
			pvmsNo = requestData.get("pvmsNo").toString();
		}
		
		if(requestData.get("nomenclturId")!=null && !requestData.get("nomenclturId").toString().isEmpty()) {
			nomenclturId = requestData.get("nomenclturId").toString();
		}
		
		
		itemList= storesDAO.getStoreItemList(itemGroupId,itemTypeId,itemSectionId,itemCategoryId,itemClassId,pvmsNo,nomenclturId);
		if(!itemList.isEmpty() && itemList!=null) {
			for(Object[] storeItem:itemList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("itemId", storeItem[0]);
				map.put("pvmsNumber", storeItem[1]);
				map.put("nomenclature", storeItem[2]);
				storeItemList.add(map);
			}
			if (storeItemList != null && storeItemList.size() > 0) {
				dataMap.put("storeItemList", storeItemList);
				dataMap.put("status", 1);
			}
			
		}else {
			dataMap.put("storeItemList", storeItemList);
			dataMap.put("msg", "Data not found");
			dataMap.put("status", 0);
		}
		
		return dataMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getReceivingIndentWaitingList(Map<String, String> requestData) {

		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		
		List<HashMap<String, Object>> waitingListData = new ArrayList<HashMap<String, Object>>();
		List<StoreIssueM> waitingList = new ArrayList<StoreIssueM>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		
		String fromDate ="";
		String toDate="";
		String indentNo="";
		long departmentId=0;
		long hospitalId=0;
		
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		int mmuId = Integer.parseInt(requestData.get("mmuId"));
		respMap=storesDAO.getReceivingIndentWaitingList(fromDate,toDate,mmuId,indentNo,departmentId,pageNo);
		
		waitingList = (List<StoreIssueM>) respMap.get("waitingListData");
		totalMatches = (List<Integer>) respMap.get("totalMatches");
		
		if(waitingList.size()>0) {
			for(StoreIssueM storeIssueIndent:waitingList) {
				HashMap<String,Object> storeIssuedIndentMap=new HashMap<String, Object>();
				storeIssuedIndentMap.put("Id", storeIssueIndent.getStoreInternalIndentM().getId());
				storeIssuedIndentMap.put("indentNo", storeIssueIndent.getStoreInternalIndentM().getDemandNo());
				storeIssuedIndentMap.put("indentDate", HMSUtil.changeDateToddMMyyyy(storeIssueIndent.getStoreInternalIndentM().getDemandDate()));
				storeIssuedIndentMap.put("issueNo", storeIssueIndent.getIssueNo());
				storeIssuedIndentMap.put("storeIssueMId",storeIssueIndent.getId());
				storeIssuedIndentMap.put("issueDate", HMSUtil.changeDateToddMMyyyy(storeIssueIndent.getIssueDate()));
				waitingListData.add(storeIssuedIndentMap);
			}
			map.put("waitingListData",waitingListData);
			map.put("count",totalMatches.size());
			map.put("status",1);
	}else {
		map.put("waitingListData",waitingListData);
		map.put("count",totalMatches.size());
		map.put("status",0);
	}
		
		return map;
	
	}

	// Newly added code
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> receivingIndentInInventory(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> receivingIndentListForInventory = new HashMap<String, Object>();
		List<HashMap<String, Object>> storeInternalIndentMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> storeInternalIndentTData = new ArrayList<HashMap<String, Object>>();
		long internalM_headerId = Long.parseLong(requestData.get("indentId"));
		long storeIssueMId = Long.parseLong(requestData.get("storeIssueMId"));
		receivingIndentListForInventory= storesDAO.receivingIndentInInventory(internalM_headerId,storeIssueMId);
		
		List<StoreInternalIndentM> internalIndentM = (List<StoreInternalIndentM>) receivingIndentListForInventory.get("internalMList");
		//issueMList
		List<StoreIssueM> issueIndentM = (List<StoreIssueM>) receivingIndentListForInventory.get("issueMList");
		if(internalIndentM.size()>0) {
			for(StoreInternalIndentM indentM:internalIndentM) {
				HashMap<String,Object> internM_map=new HashMap<String, Object>();
				internM_map.put("indentMId", indentM.getId());
				internM_map.put("indentNo", indentM.getDemandNo());
				internM_map.put("indentDate", HMSUtil.changeDateToddMMyyyy(indentM.getDemandDate()));
				internM_map.put("issueNo", issueIndentM.get(0).getIssueNo());
				internM_map.put("issueDate", HMSUtil.changeDateToddMMyyyy(issueIndentM.get(0).getIssueDate()));
				
				storeInternalIndentMData.add(internM_map);
			}
			map.put("storeInternalIndentMData", storeInternalIndentMData);
			
			List<StoreIssueT> issueIndentT = (List<StoreIssueT>) receivingIndentListForInventory.get("issueTList");
			if (issueIndentT.size() > 0) {
				for (StoreIssueT indentT : issueIndentT) {
					HashMap<String, Object> issueIndentT_map = new HashMap<String, Object>();
					issueIndentT_map.put("issueTId", indentT.getId());
					issueIndentT_map.put("indentTId", indentT.getStoreInternalIndentT().getId());
					
					issueIndentT_map.put("pvsmNo", indentT.getMasStoreItem().getPvmsNo());
					issueIndentT_map.put("nomenclature", indentT.getMasStoreItem().getNomenclature());
					issueIndentT_map.put("au", indentT.getMasStoreItem().getMasStoreUnit().getStoreUnitName());
					issueIndentT_map.put("auId", indentT.getMasStoreItem().getMasStoreUnit().getStoreUnitId());
					issueIndentT_map.put("itemId", indentT.getMasStoreItem().getItemId());
					issueIndentT_map.put("stockId", indentT.getStoreItemBatchStock()!=null?indentT.getStoreItemBatchStock().getStockId():0);
					issueIndentT_map.put("batchNo", indentT.getStoreItemBatchStock()!=null?indentT.getStoreItemBatchStock().getBatchNo():"");
					
					if(indentT.getStoreItemBatchStock()!=null && indentT.getStoreItemBatchStock().getManufactureDate()!=null) {
						issueIndentT_map.put("dom", HMSUtil.changeDateToddMMyyyy(indentT.getStoreItemBatchStock().getManufactureDate()));
					}else {
						issueIndentT_map.put("dom", "");
					}
					 
					issueIndentT_map.put("doe", indentT.getStoreItemBatchStock()!=null?HMSUtil.changeDateToddMMyyyy(indentT.getStoreItemBatchStock().getExpiryDate()):"");
					issueIndentT_map.put("qtyDemand", indentT.getQtyRequest());
					issueIndentT_map.put("qtyIssued", indentT.getQtyIssued());
					issueIndentT_map.put("qtyReceived", indentT.getQtyIssued()); // This column will use in store_internal_indent_t and will be update.
					
					if(indentT.getStoreInternalIndentT().getQtyReceived() != null) {
						issueIndentT_map.put("previousReceivedQty",indentT.getStoreInternalIndentT().getQtyReceived());
					}else {
						issueIndentT_map.put("previousReceivedQty",0);
					}
					storeInternalIndentTData.add(issueIndentT_map);
				}
				map.put("storeInternalIndentTData", storeInternalIndentTData);
			}
			map.put("status",1);
		}else {
			map.put("status",0);
		}
		return map;
	}

	@Override
	public String addToStockIssuedIndent(String requestData) {
		boolean flag = false;

		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
		
		JSONArray indentMIArr = jObject.getJSONArray("indentMId");
		long indentMId = indentMIArr.getLong(0);
		
		flag = storesDAO.addToStockIssuedIndent(jObject);

		if (flag) {
			jsonRespObject.put("message", "Stock added successfully .");
			jsonRespObject.put("indentMId", indentMId);
			jsonRespObject.put("status", "1");

		} else {
			jsonRespObject.put("message", "Stock is not added .");
			jsonRespObject.put("indentMId", 0);
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	}


	@Override
	public Map<String, Object> getStoreItemListForAutoComplete(Map<String, String> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> respStoreItemList = new ArrayList<HashMap<String, Object>>();
		List<MasStoreItem> itemList = new ArrayList<MasStoreItem>();
		String pvmsNo = "";
		String nomenclature = "";
		String itemId = "";
		
		if (requestData.get("pvmsNo") != null && !requestData.get("pvmsNo").toString().isEmpty()) {
			pvmsNo = requestData.get("pvmsNo").toString();
		}

		if (requestData.get("nomenclturId") != null && !requestData.get("nomenclturId").toString().isEmpty()) {
			nomenclature = requestData.get("nomenclturId").toString();
		}
		

		if (requestData.get("itemId") != null && !requestData.get("itemId").toString().isEmpty()) {
			itemId = requestData.get("itemId").toString();
		}


		itemList = storesDAO.getStoreItemListForAutoComplete(pvmsNo, nomenclature,itemId);
		if (itemList.size() > 0) {
			for (MasStoreItem item : itemList) {
				HashMap<String, Object> masStoreItemMap = new HashMap<String, Object>();
				masStoreItemMap.put("itemId", item.getItemId());
				masStoreItemMap.put("pvmsNumber", item.getPvmsNo());
				masStoreItemMap.put("nomenclature", item.getNomenclature());
				masStoreItemMap.put("au", item.getMasStoreUnit1()!=null?item.getMasStoreUnit1().getStoreUnitName():"");
				respStoreItemList.add(masStoreItemMap);
			}
			map.put("storeItemList", respStoreItemList);
			map.put("count", respStoreItemList.size());
			map.put("status", 1);
		} else {
			map.put("storeItemList", respStoreItemList);
			map.put("count", respStoreItemList.size());
			map.put("status", 0);
		}
		return map;
	}
	
	@Override
	public String updatePhysicalStockTaking(String requestData) {
		boolean flag = false;
		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
		
		flag = storesDAO.updatePhysicalStockTaking(jObject);
		if (flag) {
			jsonRespObject.put("message", "Physical stock taking  done successfully.");
			jsonRespObject.put("status", "1");
		} else {
			jsonRespObject.put("message", "Some error occured .");
			jsonRespObject.put("status", "0");
		}
		return jsonRespObject.toString();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> stockTakingWaitingListData(Map<String, String> requestData) {

		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> waitingDataList = new ArrayList<HashMap<String, Object>>();
		List<StoreStockTakingM> waitingList = new ArrayList<StoreStockTakingM>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		
		String fromDate ="";
		String toDate="";
		String mmuId = requestData.get("mmuId");
		String cityId = requestData.get("cityId");
		String districtId = requestData.get("districtId");
		long departmentId = Long.parseLong(requestData.get("departmentId"));
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		
		 if(requestData.containsKey("fromDate")) {
			  fromDate=requestData.get("fromDate").toString(); 
			  }
			  
			  if(requestData.containsKey("toDate")) {
			  toDate=requestData.get("toDate").toString();
			  }
		
			 respMap=storesDAO.stockTakingWaitingListData(fromDate,toDate,mmuId,cityId,districtId,departmentId,pageNo);
			 waitingList = (List<StoreStockTakingM>) respMap.get("waitingList");
			 totalMatches = (List<Integer>) respMap.get("totalMatches");
			 
		if(!waitingList.isEmpty() && waitingList.size()>0) {
			
			for(StoreStockTakingM takingM:waitingList) {
				HashMap<String,Object> takingM_map=new HashMap<String, Object>();
				takingM_map.put("Id", takingM.getTakingMId());
				takingM_map.put("stockTakingNo", takingM.getStockTakingNo());
				takingM_map.put("stockTakingDate", HMSUtil.changeDateToddMMyyyy(takingM.getPhysicalDate()));
				takingM_map.put("departmentName", takingM.getMasDepartment().getDepartmentName());
				takingM_map.put("stockTakingStatus", takingM.getStatus());
				takingM_map.put("submitBy", takingM.getLastChgBy().getUserName());
				waitingDataList.add(takingM_map);
			}
			map.put("waitingDataList",waitingDataList);
			map.put("count",totalMatches.size());
			map.put("status",1);
	}else {
		map.put("waitingDataList",waitingDataList);
		map.put("count",totalMatches.size());
		map.put("status",0);
	}
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> stockTakingDetailsForApproval(Map<String, String> requestData) {

		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> stockTakingListForApproval = new HashMap<String, Object>();
		List<HashMap<String, Object>> stockTakingMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> stockTakingTData = new ArrayList<HashMap<String, Object>>();
		long takingM_headerId = Long.parseLong(requestData.get("headerId"));
		stockTakingListForApproval= storesDAO.stockTakingDetailsForApproval(takingM_headerId);
		
		List<StoreStockTakingM> stockTakingM = (List<StoreStockTakingM>) stockTakingListForApproval.get("takingMDetails");
		if(!stockTakingM.isEmpty() && stockTakingM.size()>0) {
			for(StoreStockTakingM stockTaking:stockTakingM) {
				HashMap<String,Object> stockTakingM_map=new HashMap<String, Object>();
				stockTakingM_map.put("Id", stockTaking.getTakingMId());
				stockTakingM_map.put("stockTakingNo", stockTaking.getStockTakingNo());
				stockTakingM_map.put("stockTakingDate", HMSUtil.changeDateToddMMyyyy(stockTaking.getPhysicalDate()));
				stockTakingM_map.put("departmentName", stockTaking.getMasDepartment().getDepartmentName());
				stockTakingM_map.put("departmentId", stockTaking.getMasDepartment().getDepartmentId());
				stockTakingM_map.put("reasonStockTaking", stockTaking.getReason());
				stockTakingM_map.put("submitBy", stockTaking.getLastChgBy().getUserName());
				stockTakingMData.add(stockTakingM_map);
			}
			map.put("stockTakingMData", stockTakingMData);
		}
		
		List<StoreStockTakingT> stockTakingT = (List<StoreStockTakingT>) stockTakingListForApproval.get("takingTDetails");
		if (!stockTakingT.isEmpty() && stockTakingT.size() > 0) {
			for (StoreStockTakingT storeTakingT : stockTakingT) {
				HashMap<String, Object> storeBalanceT_map = new HashMap<String, Object>();
				storeBalanceT_map.put("Id", storeTakingT.getTakingTId());
				storeBalanceT_map.put("stockId", storeTakingT.getStoreItemBatchStock().getStockId());
				storeBalanceT_map.put("itemId", storeTakingT.getMasStoreItem().getItemId());
				storeBalanceT_map.put("pvmsNumber", storeTakingT.getMasStoreItem().getPvmsNo());
				storeBalanceT_map.put("nomenclature", storeTakingT.getMasStoreItem().getNomenclature());
				storeBalanceT_map.put("batchNumber", storeTakingT.getBatchNo());
				storeBalanceT_map.put("doe", HMSUtil.changeDateToddMMyyyy(storeTakingT.getExpiryDate()));
				storeBalanceT_map.put("computedStock", storeTakingT.getComputedStock());
				storeBalanceT_map.put("physicalStock", storeTakingT.getStoreStockService());
				storeBalanceT_map.put("surplus", storeTakingT.getStockSurplus()!=null?storeTakingT.getStockSurplus():"");
				storeBalanceT_map.put("deficient", storeTakingT.getStockDeficient()!=null?storeTakingT.getStockDeficient():"");
				storeBalanceT_map.put("remarks", storeTakingT.getRemarks()!=null?storeTakingT.getRemarks():"");
				stockTakingTData.add(storeBalanceT_map);
			}
			map.put("stockTakingTData", stockTakingTData);
		}
		return map;
	}
	
	

	@Override
	public String submitStockingTakingDataForApproval(String requestData) {
		boolean flag = false;

		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
		JSONArray stockTakingMArr = jObject.getJSONArray("takingMId");
		long takingMId  = stockTakingMArr.getLong(0);
		
		JSONArray statusArr = jObject.getJSONArray("actionId");
		String approvalStatus = statusArr.getString(0);
		
		flag = storesDAO.submitStockingTakingDataForApproval(jObject);

		if (flag) {
			if(approvalStatus.equalsIgnoreCase("A")) {
				jsonRespObject.put("takingMId", takingMId);
				jsonRespObject.put("message", "Stock updated  successfully.");
				jsonRespObject.put("status", "1");
			}else if(approvalStatus.equalsIgnoreCase("R")) {
				jsonRespObject.put("takingMId", takingMId);
				jsonRespObject.put("message", "Stock updation rejected.");
				jsonRespObject.put("status", "1");
			}
		} else {
			jsonRespObject.put("message", "Some error occured .");
			jsonRespObject.put("takingMId", 0);
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	
	}

	@Override
	public Map<String, Object> getDataForCreateSupplyOrder(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<StoreSoM> soMList = new ArrayList<StoreSoM>();
		List<MasStoreSupplierType> supplierTypeList = new ArrayList<MasStoreSupplierType>();
		List<HashMap<String, Object>> storeSoMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> supplierTypeData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> financialYearData = new ArrayList<HashMap<String, Object>>();
		List<MasStoreFinancial> financialList = new ArrayList<MasStoreFinancial>();
		
		long hospitalId= Long.parseLong(requestData.get("hospitalId").toString());
		long departmentId= Long.parseLong(requestData.get("departmentId").toString());
		
		supplierTypeList = storesDAO.getsupplierTypeList();
		if(supplierTypeList.size()>0) {
			for(MasStoreSupplierType supplier: supplierTypeList ) {
				HashMap<String,Object> supplier_map=new HashMap<String, Object>();
				supplier_map.put("supplierTypeId", supplier.getSupplierTypeId());
				supplier_map.put("supplierTypeName", supplier.getSupplierTypeName());
				supplierTypeData.add(supplier_map);
			}
			map.put("supplierTypeData",supplierTypeData);
		}else {
			map.put("supplierTypeData",supplierTypeData);
		}
		
		
		//financialList = lPProcessDao.financialYearList();
		if(financialList.size()>0) {
			for (MasStoreFinancial masStoreFinancial : financialList) {
				HashMap<String, Object> financialYearMap = new HashMap<String, Object>();
				financialYearMap.put("id", masStoreFinancial.getFinancialId());
				financialYearMap.put("name", masStoreFinancial.getFinancialYear());
				financialYearData.add(financialYearMap);
			}
			map.put("financialYearData", financialYearData);
		}else {
			map.put("financialYearData", financialYearData);
		}
		
		soMList = storesDAO.getDataForCreateSupplyOrder(hospitalId,departmentId);
		
		if(soMList.size()>0) {
			for(StoreSoM soM : soMList) {
				HashMap<String,Object> soM_map=new HashMap<String, Object>();
				soM_map.put("soMId", soM.getSoMId());
				soM_map.put("soMSanctionNo", soM.getSanctionNo());
				storeSoMData.add(soM_map);
			}
			map.put("storeSoMData", storeSoMData);
			map.put("count", soMList.size());
			map.put("status", 1);
		}else {
			map.put("status", 0);
			map.put("supplierTypeList", supplierTypeList);
			map.put("count", soMList.size());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getSupplyOrderSanctionData(Map<String, String> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> stockSoMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> stockSoTData = new ArrayList<HashMap<String, Object>>();
		Map<String, Object> sanctionSupplyOrderData = new HashMap<String, Object>();
		
		
		long soMId = Long.parseLong(requestData.get("soMId").toString());
		sanctionSupplyOrderData = storesDAO.getSupplyOrderSanctionData(soMId);
		List<StoreSoM> supplyOrderM = (List<StoreSoM>)sanctionSupplyOrderData.get("storeSoMData");
		
		if(supplyOrderM.size()>0) {
			for(StoreSoM soM:supplyOrderM) {
				HashMap<String,Object> stockSoM_map=new HashMap<String, Object>();
				stockSoM_map.put("soMId", soM.getSoMId());
				stockSoM_map.put("rfpNo", soM.getRfpNo());
				stockSoM_map.put("quotationNo", soM.getQuatationNo());
				stockSoM_map.put("quotationMId", soM.getStoreQuotationM().getQuotationMId());
				stockSoM_map.put("departmentName", soM.getMasDepartment().getDepartmentName());
				stockSoM_map.put("departmentId", soM.getMasDepartment().getDepartmentId());
				stockSoM_map.put("vendor", soM.getMasStoreSupplier().getSupplierName());
				stockSoM_map.put("vendorId", soM.getMasStoreSupplier().getSupplierId());
				stockSoM_map.put("stockist", soM.getMasStoreSupplier().getMasStoreSupplierType()!=null?soM.getMasStoreSupplier().getMasStoreSupplierType().getSupplierTypeName():"");
				stockSoM_map.put("stockistId", soM.getMasStoreSupplier().getMasStoreSupplierType()!=null?soM.getMasStoreSupplier().getMasStoreSupplierType().getSupplierTypeId():0);
				stockSoMData.add(stockSoM_map);
			}
			map.put("stockSoMData", stockSoMData);
		}
		
		List<StoreSoT> soT = (List<StoreSoT>)sanctionSupplyOrderData.get("storeSoTData");
		if (soT.size() > 0) {
			for (StoreSoT storeSoT : soT) {
				HashMap<String, Object> storeSoT_map = new HashMap<String, Object>();
				storeSoT_map.put("soTId", storeSoT.getSoTId());
				storeSoT_map.put("itemId", storeSoT.getMasStoreItem().getItemId());
				storeSoT_map.put("quotationTId", storeSoT.getStoreQuotationT().getQuotationTId());
				storeSoT_map.put("pvmsNumber", storeSoT.getMasStoreItem().getPvmsNo());
				storeSoT_map.put("nomenclature", storeSoT.getMasStoreItem().getNomenclature());
				storeSoT_map.put("au", storeSoT.getMasStoreUnit().getStoreUnitName());
				storeSoT_map.put("qtyRequired", storeSoT.getItemQty());
				storeSoT_map.put("unitRate", storeSoT.getUnitRate());
				storeSoT_map.put("amtValue", storeSoT.getItemValue());
				stockSoTData.add(storeSoT_map);
			}
			map.put("stockSoTData", stockSoTData);
		}
	
		return map;
	}

	@Override
	public String saveSupplyOrder(String requestData) {
		boolean flag = false;
		JSONObject jsonRespObject =new JSONObject();
		JSONObject jObject =new JSONObject(requestData);
		flag = storesDAO.saveSupplyOrder(jObject); 
		 if(flag) {
			 jsonRespObject.put("message", "Supply order created successfully.");
			 jsonRespObject.put("status", "1");
		 }else {
			 jsonRespObject.put("message", "Supply order can not created.");
			 jsonRespObject.put("status", "0");
		 }
		return jsonRespObject.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> showSupplyOrderListData(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		List<StorePoM> poMList = new ArrayList<StorePoM>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		List<MasStoreSupplier> supplierList = new ArrayList<MasStoreSupplier>();
		List<HashMap<String, Object>> supplierData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> storePoMData = new ArrayList<HashMap<String, Object>>();
		
		long vendorId= 0;
		long departmentId=0;
		long hospitalId= Long.parseLong(requestData.get("hospitalId").toString());
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		
		if(requestData.get("departmentId")!=null && !requestData.get("departmentId").toString().isEmpty()) {
			departmentId= Long.parseLong(requestData.get("departmentId").toString());
		}
		if(requestData.get("vendorId")!=null && !requestData.get("vendorId").toString().isEmpty())	{
			vendorId=Long.parseLong(requestData.get("vendorId").toString());
		}
		
		respMap = storesDAO.getDataForSupplyOrderList(hospitalId,departmentId,vendorId,pageNo);
		poMList = (List<StorePoM>) respMap.get("poMList");
		totalMatches=(List<Integer>) respMap.get("totalMatches");
		if(poMList.size()>0) {
			for(StorePoM poM : poMList) {
				HashMap<String,Object> poM_map=new HashMap<String, Object>();
				poM_map.put("poMId", poM.getPoMId());
				poM_map.put("soNo", poM.getPoNumber());
				poM_map.put("year", poM.getMasStoreFinancial()!=null?poM.getMasStoreFinancial().getFinancialYear():"");
				poM_map.put("soDate", HMSUtil.changeDateToddMMyyyy(poM.getPoDate()));
				poM_map.put("quotationNo", poM.getStoreQuotationM().getQuotationNo());
				poM_map.put("supplierName", poM.getMasStoreSupplier().getSupplierName());
				//poM_map.put("preparedBy", poM.getLastChgBy().getFirstName());
				poM_map.put("status", poM.getStatus());
				storePoMData.add(poM_map);
			}
			map.put("storePoMData",storePoMData);
			map.put("count",totalMatches.size());
			map.put("status",1);
			
		}else {
			map.put("storePoMData",storePoMData);
			map.put("count",totalMatches.size());
			map.put("status",0);
		}
		
		supplierList = storesDAO.getsupplierList(hospitalId);
		if(supplierList.size()>0) {
			for(MasStoreSupplier supplier: supplierList ) {
				HashMap<String,Object> supplier_map=new HashMap<String, Object>();
				supplier_map.put("supplierId", supplier.getSupplierId());
				supplier_map.put("supplierName", supplier.getSupplierName());
				supplierData.add(supplier_map);
			}
			map.put("supplierData",supplierData);
		}
		
		return map;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> supplyOrderWaitingListForPendingApproval(Map<String, String> requestData) {

		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		List<StorePoM> poMList = new ArrayList<StorePoM>();
		List<MasStoreSupplier> supplierList = new ArrayList<MasStoreSupplier>();
		List<HashMap<String, Object>> supplierData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> storePoMData = new ArrayList<HashMap<String, Object>>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		long vendorId= 0;
		long departmentId=0;
		long hospitalId= Long.parseLong(requestData.get("hospitalId").toString());
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		
		
		if(requestData.get("departmentId")!=null && !requestData.get("departmentId").toString().isEmpty()) {
			departmentId= Long.parseLong(requestData.get("departmentId").toString());
		}
		if(requestData.get("vendorId")!=null && !requestData.get("vendorId").toString().isEmpty())	{
			vendorId=Long.parseLong(requestData.get("vendorId").toString());
		}
		
		respMap = storesDAO.supplyOrderWaitingListForPendingApproval(hospitalId,departmentId,vendorId,pageNo);
		poMList = (List<StorePoM>) respMap.get("poMList");
		totalMatches = (List<Integer>) respMap.get("totalMatches");
		if(poMList.size()>0) {
			for(StorePoM poM : poMList) {
				HashMap<String,Object> poM_map=new HashMap<String, Object>();
				poM_map.put("poMId", poM.getPoMId());
				poM_map.put("soNo", poM.getPoNumber());
				poM_map.put("year", poM.getMasStoreFinancial()!=null?poM.getMasStoreFinancial().getFinancialYear():"");
				poM_map.put("soDate", HMSUtil.changeDateToddMMyyyy(poM.getPoDate()));
				poM_map.put("quotationNo", poM.getStoreQuotationM().getQuotationNo());
				poM_map.put("supplierName", poM.getMasStoreSupplier().getSupplierName());
				//poM_map.put("preparedBy", poM.getLastChgBy().getFirstName());
				poM_map.put("status", poM.getStatus());
				storePoMData.add(poM_map);
			}
			map.put("storePoMData",storePoMData);
			map.put("count",totalMatches.size());
			map.put("status",1);
			
		}else {
			map.put("storePoMData",storePoMData);
			map.put("count",totalMatches.size());
			map.put("status",0);
		}
		
		supplierList = storesDAO.getsupplierList(hospitalId);
		if(supplierList.size()>0) {
			for(MasStoreSupplier supplier: supplierList ) {
				HashMap<String,Object> supplier_map=new HashMap<String, Object>();
				supplier_map.put("supplierId", supplier.getSupplierId());
				supplier_map.put("supplierName", supplier.getSupplierName());
				supplierData.add(supplier_map);
			}
			map.put("supplierData",supplierData);
		}
		
		return map;
	
	}
	
	
	@Override
	public String saveOrSubmitSupplyOrderForApproval(String requestData) {
		boolean flag = false;
		long poMId=0;
		
		JSONObject jsonRespObject =new JSONObject();
		JSONObject jObject =new JSONObject(requestData);
		
		JSONArray requestTypeArr = jObject.getJSONArray("requestType");
		String requestTypeStatus = requestTypeArr.getString(0);
		
		JSONArray poMIdArr = jObject.getJSONArray("poMId");
		poMId =  poMIdArr.getLong(0);
		
		flag = storesDAO.saveOrSubmitSupplyOrderForApproval(jObject); 
		
		 if(flag) {
			 if(requestTypeStatus.equalsIgnoreCase("e")) {
				 jsonRespObject.put("message", "Supply order saved successfully.");
				 jsonRespObject.put("poMId", poMId);
				 jsonRespObject.put("status", "1");
			 }else {
				 String actionId="";
				 if(jObject.has("actionId")) {
					 JSONArray actionArr = jObject.getJSONArray("actionId");
					 actionId = actionArr.getString(0);
					 
					  if(!actionId.isEmpty() && !actionId.equalsIgnoreCase("0")) {
						  if(actionId.equalsIgnoreCase("A")) {
							  jsonRespObject.put("message", "Supply order approved successfully.");
							  jsonRespObject.put("poMId", poMId);
							  jsonRespObject.put("actionFlag",actionId );
							  jsonRespObject.put("status", "1");
						  }else {
							  jsonRespObject.put("message", "Supply order rejected.");
							  jsonRespObject.put("poMId", poMId);
							  jsonRespObject.put("actionFlag",actionId );
							  jsonRespObject.put("status", "1");
						  }
						  
						}
					 
				 }else {
					 jsonRespObject.put("message", "Supply order submitted successfully.");
					 jsonRespObject.put("poMId", poMId);
					 jsonRespObject.put("status", "1"); 
				 }
			 }
			 
		 }else {
			 poMId=0;
			 jsonRespObject.put("message", "Some error occured.");
			 jsonRespObject.put("poMId", poMId);
			 jsonRespObject.put("status", "0");
		 }
		return jsonRespObject.toString();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getRVWaitingListAgainstSo(Map<String, String> requestData) {

		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		Map<String,Object> respMapPoM = new HashMap<String, Object>();
		
		List<StorePoM> storePoMList = new ArrayList<StorePoM>();
		List<StorePoM> poNoList = new ArrayList<StorePoM>();
		List<MasStoreSupplier> supplierList = new ArrayList<MasStoreSupplier>();
		List<HashMap<String, Object>> storePoMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> supplierData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> poNumberData = new ArrayList<HashMap<String, Object>>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		
		
		long vendorId= 0;
		long departmentId=0;
		String fromDate="";
		String toDate="";
		String soNo="";
		long hospitalId= Long.parseLong(requestData.get("hospitalId").toString());
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		
		
		if(requestData.get("departmentId")!=null && !requestData.get("departmentId").toString().isEmpty()) {
			departmentId= Long.parseLong(requestData.get("departmentId").toString());
		}
		if(requestData.get("vendorId")!=null && !requestData.get("vendorId").toString().isEmpty())	{
			vendorId=Long.parseLong(requestData.get("vendorId").toString());
		};
		
		if(requestData.containsKey("fromDate") && !requestData.get("fromDate").toString().isEmpty()) {
			  fromDate=requestData.get("fromDate").toString(); 
			}
			  
		if(requestData.containsKey("toDate") && !requestData.get("toDate").toString().isEmpty()) {
			  toDate=requestData.get("toDate").toString();
		}
		
		if(requestData.containsKey("soNo") && !requestData.get("soNo").toString().isEmpty()) {
			soNo=requestData.get("soNo").toString(); 
			}
			  
		
		supplierList = storesDAO.getsupplierList(hospitalId);
		if(supplierList.size()>0) {
			for(MasStoreSupplier supplier: supplierList ) {
				HashMap<String,Object> supplier_map=new HashMap<String, Object>();
				supplier_map.put("supplierId", supplier.getSupplierId());
				supplier_map.put("supplierName", supplier.getSupplierName());
				supplierData.add(supplier_map);
			}
			map.put("supplierData",supplierData);
		}
		
		respMap = storesDAO.getRVWaitingListAgainstSo(hospitalId,departmentId,0,"","","",pageNo);
		poNoList = (List<StorePoM>) respMap.get("poMList");

		if(poNoList.size()>0) {
			for(StorePoM poNo: poNoList ) {
				HashMap<String,Object> po_map=new HashMap<String, Object>();
				po_map.put("poNumber", poNo.getPoNumber());
				poNumberData.add(po_map);
			}
			map.put("poNumberData",poNumberData);
		}
		
		respMapPoM = storesDAO.getRVWaitingListAgainstSo(hospitalId,departmentId,vendorId,fromDate,toDate,soNo,pageNo);
		storePoMList = (List<StorePoM>) respMapPoM.get("poMList");
		totalMatches = (List<Integer>) respMapPoM.get("totalMatches");
		if(storePoMList.size()>0) {
			for(StorePoM poM : storePoMList) {
				HashMap<String,Object> poM_map=new HashMap<String, Object>();
				poM_map.put("poMId",poM.getPoMId());
				poM_map.put("soNo",poM.getPoNumber());
				poM_map.put("soDate",HMSUtil.changeDateToddMMyyyy(poM.getPoDate()));
				poM_map.put("supplierName",poM.getMasStoreSupplier().getSupplierName());
				poM_map.put("deliveryDate",HMSUtil.changeDateToddMMyyyy(poM.getDeliveryDate()));
				//poM_map.put("approvedBy",poM.getApprovedBy().getFirstName());
				poM_map.put("receivedStatus",poM.getReceivedStatus()!=null?poM.getReceivedStatus():"");
				storePoMData.add(poM_map);
			}
			map.put("storePoMData", storePoMData);
			map.put("count", totalMatches.size());
			map.put("status", 1);
		}else {
			map.put("status", 0);
			map.put("supplierData", supplierData);
			map.put("count", totalMatches.size());
		}
		return map;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> supplyOrderDetailsForApproval(Map<String, String> requestData) {
		//Create RV Against SO Details 
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> supplyOrderDetails = new HashMap<String, Object>();
		List<HashMap<String, Object>> storePoMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> storePoTData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> tempData = new ArrayList<HashMap<String, Object>>();
		
		
		long poM_headerId = Long.parseLong(requestData.get("headerId"));
		supplyOrderDetails= storesDAO.supplyOrderDetailsForApproval(poM_headerId);
		
		List<StoreDoInternalIndentM> storePoMList = (List<StoreDoInternalIndentM>) supplyOrderDetails.get("poMDetails");
		if(!storePoMList.isEmpty() && storePoMList.size()>0) {
			for(StoreDoInternalIndentM poM:storePoMList) {
				HashMap<String,Object> storePoM_map=new HashMap<String, Object>();
				storePoM_map.put("poMId", poM.getId());
				storePoM_map.put("supplyOrderNo", poM.getPoNo());
				storePoM_map.put("soDate", HMSUtil.changeDateToddMMyyyy(poM.getPoDate()));
				storePoM_map.put("vendorName", poM.getMasStoreSupplierNew().getSupplierName());
				storePoM_map.put("vendorNameId", poM.getMasStoreSupplierNew().getSupplierId());
				storePoM_map.put("stockist", poM.getMasStoreSupplierType().getSupplierTypeName());
				storePoM_map.put("rvNotes", poM.getRvNotes());
				storePoM_map.put("status", poM.getStatus());
				storePoMData.add(storePoM_map);
			}
			map.put("storePoMData", storePoMData);
			map.put("poMStatus", storePoMList.get(0).getStatus());
		}
		
		List<StoreDoInternalIndentT> storePoT = (List<StoreDoInternalIndentT>) supplyOrderDetails.get("poTDetails");
		Map<String,Object> inputForManufacturerList = new HashMap<>();
		inputForManufacturerList.put("id", storePoMList.get(0).getMasStoreSupplierType().getSupplierTypeId());
		Map<String, Object> manufacturerData = dispenceryService.getMasSupplierList(inputForManufacturerList);
		map.put("manufacturerList",manufacturerData);  
		if (!storePoT.isEmpty() && storePoT.size() > 0) {
			for (StoreDoInternalIndentT poT : storePoT) {
				HashMap<String, Object> storePoT_map = new HashMap<String, Object>();
				storePoT_map.put("poTId", poT.getId());
				storePoT_map.put("itemId", poT.getMasStoreItem().getItemId());
				storePoT_map.put("pvmsNumber", poT.getMasStoreItem().getPvmsNo());
				storePoT_map.put("nomenclature", poT.getMasStoreItem().getNomenclature());
				storePoT_map.put("au", poT.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
				storePoT_map.put("qtyRequired", poT.getPoQty());
				storePoT_map.put("preReceivedQty", poT.getQtyReceived()!=null?poT.getQtyReceived():"");
				
				
				if(poT.getUnitRate() != null) {
					storePoT_map.put("unitRate", poT.getUnitRate());
				}else {
					storePoT_map.put("unitRate", "");
				}
				
				storePoT_map.put("amtValue", "");
				storePoTData.add(storePoT_map);
			}
			map.put("storePoTData", storePoTData);
		}
		
		
		/*
		 * List<TempDirectReceivingForBackLp> tempObject =
		 * (List<TempDirectReceivingForBackLp>)
		 * supplyOrderDetails.get("tempObjectList"); if(tempObject.size()>0) { for
		 * (TempDirectReceivingForBackLp temp : tempObject) { HashMap<String, Object>
		 * tempObjectMap = new HashMap<String, Object>(); tempObjectMap.put("tempId",
		 * temp.getTempId()); tempObjectMap.put("itemId",
		 * temp.getMasStoreItem().getItemId()); tempObjectMap.put("batchNo",
		 * temp.getBatchNo()); tempObjectMap.put("dom",
		 * HMSUtil.changeDateToddMMyyyy(temp.getDom())); tempObjectMap.put("doe",
		 * HMSUtil.changeDateToddMMyyyy(temp.getDoe())); tempData.add(tempObjectMap); }
		 * map.put("tempData", tempData); }
		 */
		
		return map;
	
	}

	
	@Override
	public String submitRvAgainstSupplyOrder(String requestData) {
		long returnGrnMId = 0;

		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
	
		returnGrnMId = storesDAO.submitRvAgainstSupplyOrder(jObject);

		if (returnGrnMId!=0) {
			if(returnGrnMId>0) {
				jsonRespObject.put("message", "Stock updated successfully .");
				jsonRespObject.put("grnMId", returnGrnMId);
				jsonRespObject.put("status", "1");
			}else {
				jsonRespObject.put("message", "Some error occured.");
				jsonRespObject.put("grnMId", 0);
				jsonRespObject.put("status", "0");
			}
		} else {
			jsonRespObject.put("message", "Atleast one item quantity required to update the stock.");
			jsonRespObject.put("grnMId", 0);
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	
	}

	
	@Override
	public String submitDirectReceivingSo(String requestData) {
		boolean submitFlag = false;
		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
	
		submitFlag = storesDAO.submitDirectReceivingSo(jObject);

		if (submitFlag) {
			jsonRespObject.put("message", "Stock updated successfully .");
			jsonRespObject.put("status", "1");
		} else {
			jsonRespObject.put("message", "Some error occured .");
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	
	}
	
	
	/*
	@Override
	public String submitDirectReceivingSo(String requestData) {
		long grnMId = 0;
		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
	
		grnMId = storesDAO.submitDirectReceivingSo(jObject);

		if (grnMId!=0) {
			jsonRespObject.put("message", "Stock updated successfully .");
			jsonRespObject.put("grnMId", grnMId);
			jsonRespObject.put("status", "1");
		} else {
			jsonRespObject.put("message", "Some error occured .");
			jsonRespObject.put("grnMId", 0);
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	
	}
	*/

	@Override
	public Map<String, Object> deleteRowFromDatabase(Map<String, String> requestData) {
		boolean flag=false;
		Map<String,Object> map = new HashMap<String, Object>();
		long headerMId= Long.parseLong(requestData.get("headerMId").toString());
		long detailTId= Long.parseLong(requestData.get("headerTId").toString());
		String dataFlag = requestData.get("dataFlag").toString();
		flag=storesDAO.deleteRowFromDatabase(headerMId,detailTId,dataFlag);
		if(flag) {
			map.put("status", 1);
		}else {
			map.put("status", 0);
		}
		return map;
	}

	@Override
	public Map<String, Object> getSupplierListForStore(Map<String, String> requestData) {
	
		Map<String,Object> map = new HashMap<String, Object>();
		List<MasStoreSupplier> supplierList = new ArrayList<MasStoreSupplier>();
		List<HashMap<String, Object>> supplierData = new ArrayList<HashMap<String, Object>>();
		
		long hospitalId=0;
		if(requestData.get("hospitalId")!=null && !requestData.get("hospitalId").toString().isEmpty()) {
			hospitalId= Long.parseLong(requestData.get("hospitalId").toString());
		}
		supplierList = storesDAO.getsupplierList(hospitalId);
		if(supplierList.size()>0) {
			for(MasStoreSupplier supplier: supplierList ) {
				HashMap<String,Object> supplier_map=new HashMap<String, Object>();
				supplier_map.put("supplierId", supplier.getSupplierId());
				supplier_map.put("supplierName", supplier.getSupplierName());
				supplierData.add(supplier_map);
			}
			map.put("supplierData",supplierData);
			map.put("status",1);
		}else {
			map.put("supplierData",supplierData);
			map.put("status",0);
		}
		
		return map;
	}

	@Override
	public String getPvmsItemForExcel(String requestData) {
		JSONObject jsonRespObject = new JSONObject();
		Map<String,Object> map = new HashMap<String, Object>();
		map = storesDAO.getPvmsItemForExcel(requestData);
		jsonRespObject.put("data", map);
		return jsonRespObject.toString();
	}

	@Override
	public String importPVMSItemFromExcel(String requestData) {
		JSONObject jsonRespObject = new JSONObject();
		Map<String,Object> map = new HashMap<String, Object>();
		map = storesDAO.importPVMSItemFromExcel(requestData);
		jsonRespObject.put("data", map);
		return jsonRespObject.toString();
	}

	@Override
	public String importPvmsdataReceivedFromiAushadhi(String requestData) {
		JSONObject jsonRespObject = new JSONObject();
		Map<String,Object> map = new HashMap<String, Object>();
		map = storesDAO.importPvmsdataReceivedFromiAushadhi(requestData);
		jsonRespObject.put("data", map);
		return jsonRespObject.toString();
	}

	@Override
	public Map<String, Object> getAuNameListForStore(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<MasStoreUnit> auNameList = new ArrayList<MasStoreUnit>();
		List<HashMap<String, Object>> auData = new ArrayList<HashMap<String, Object>>();
		auNameList = storesDAO.getAuNameListForStore();
		if(auNameList.size()>0) {
			for(MasStoreUnit aunit:auNameList) {

				HashMap<String,Object> au_map=new HashMap<String, Object>();
				au_map.put("auId", aunit.getStoreUnitId());
				au_map.put("auName", aunit.getStoreUnitName());
				auData.add(au_map);
			
			}
			map.put("accountUnitList",auData);
			map.put("status",1);
		}else {
			map.put("accountUnitList",auData);
			map.put("status",0);
		}
		
	return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> showItemListForbackDateBudgetary(Map<String, String> requestData) {

		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> itemListForBudgetary = new ArrayList<HashMap<String, Object>>();
		List<TempDirectReceivingForBackLp> tempTableObjectList = new ArrayList<TempDirectReceivingForBackLp>();
		//List<Integer> totalMatchs = new ArrayList<Integer>();
		
		String fromDate ="";
		String toDate="";
		long hospitalId = Long.parseLong(requestData.get("hospitalId"));
		long departmentId = Long.parseLong(requestData.get("departmentId"));
		long supplierId = Long.parseLong(requestData.get("supplierId"));
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		
		  if(requestData.containsKey("fromDate")) {
			  		fromDate=requestData.get("fromDate").toString(); 
		  }
		  
		  if(requestData.containsKey("toDate")) {
			  		toDate=requestData.get("toDate").toString();
		  }
		 

		respMap=storesDAO.getItemListForbackDateBudgetary(fromDate,toDate,hospitalId,departmentId,supplierId,pageNo);
		tempTableObjectList = (List<TempDirectReceivingForBackLp>) respMap.get("backDateItemsList");
		//totalMatchs= (List<Integer>) respMap.get("totalMatches");
		if(!tempTableObjectList.isEmpty() && tempTableObjectList.size()>0) {
			for(TempDirectReceivingForBackLp itemList:tempTableObjectList) {
				HashMap<String,Object> temptableBckLp=new HashMap<String, Object>();
				temptableBckLp.put("tempId", itemList.getTempId());
				temptableBckLp.put("batchNo", itemList.getBatchNo());
				temptableBckLp.put("dom",  HMSUtil.changeDateToddMMyyyy(itemList.getDom()));
				temptableBckLp.put("doe",  HMSUtil.changeDateToddMMyyyy(itemList.getDoe()));
				temptableBckLp.put("receivedQty", itemList.getReceivedQty());
				temptableBckLp.put("receivingDate", HMSUtil.changeDateToddMMyyyy(itemList.getReceivingDate()));
				temptableBckLp.put("itemId", itemList.getMasStoreItem().getItemId());
				temptableBckLp.put("pvms", itemList.getMasStoreItem().getPvmsNo());
				temptableBckLp.put("nomenclature", itemList.getMasStoreItem().getNomenclature());
				temptableBckLp.put("au", itemList.getMasStoreItem().getMasStoreUnit1().getStoreUnitName());
				
				itemListForBudgetary.add(temptableBckLp);
			}
			map.put("itemListForBudgetary",itemListForBudgetary);
			//map.put("count",totalMatchs.size());
			map.put("status",1);
			map.put("message","Record found.");
	}else {
		map.put("itemListForBudgetary",itemListForBudgetary);
		//map.put("count",totalMatchs.size());
		map.put("status",0);
		map.put("message","No record found.");
	}
		
		return map;
	
	}

	@Override
	public String createBudgetaryForBackDateLP(String requestData) {
		long budgetaryMId = 0;
		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
	
		budgetaryMId = storesDAO.createBudgetaryForBackDateLP(jObject);

		if (budgetaryMId!=0) {
			jsonRespObject.put("message", "Budgetary created successfully .");
			jsonRespObject.put("budgetaryMId", budgetaryMId);
			jsonRespObject.put("status", "1");
		} else {
			jsonRespObject.put("message", "Some error occured .");
			jsonRespObject.put("budgetaryMId", 0);
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getReceivingIndentWaitingListCo(Map<String, String> requestData) {

		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> respMap = new HashMap<String, Object>();
		
		List<HashMap<String, Object>> waitingListData = new ArrayList<HashMap<String, Object>>();
		List<StoreIssueM> waitingList = new ArrayList<StoreIssueM>();
		List<Integer> totalMatches = new ArrayList<Integer>();
		
		String fromDate ="";
		String toDate="";
		String indentNo="";
		long departmentId=0;
		long hospitalId=0;
		
		int pageNo = Integer.parseInt(requestData.get("pageNo"));
		int cityId = Integer.parseInt(requestData.get("cityId"));
		respMap=storesDAO.getReceivingIndentWaitingListCo(fromDate,toDate,cityId,indentNo,departmentId,pageNo);
		
		waitingList = (List<StoreIssueM>) respMap.get("waitingListData");
		totalMatches = (List<Integer>) respMap.get("totalMatches");
		
		if(waitingList.size()>0) {
			for(StoreIssueM storeIssueIndent:waitingList) {
				HashMap<String,Object> storeIssuedIndentMap=new HashMap<String, Object>();
				storeIssuedIndentMap.put("Id", storeIssueIndent.getStoreCoInternalIndentM().getId());
				storeIssuedIndentMap.put("indentNo", storeIssueIndent.getStoreCoInternalIndentM().getDemandNo());
				storeIssuedIndentMap.put("indentDate", HMSUtil.changeDateToddMMyyyy(storeIssueIndent.getStoreCoInternalIndentM().getDemandDate()));
				storeIssuedIndentMap.put("issueNo", storeIssueIndent.getIssueNo());
				storeIssuedIndentMap.put("storeIssueMId",storeIssueIndent.getId());
				storeIssuedIndentMap.put("issueDate", HMSUtil.changeDateToddMMyyyy(storeIssueIndent.getIssueDate()));
				waitingListData.add(storeIssuedIndentMap);
			}
			map.put("waitingListData",waitingListData);
			map.put("count",totalMatches.size());
			map.put("status",1);
	}else {
		map.put("waitingListData",waitingListData);
		map.put("count",totalMatches.size());
		map.put("status",0);
	}
		
		return map;
	
	}

	// Newly added code
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> receivingIndentInInventoryCo(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> receivingIndentListForInventory = new HashMap<String, Object>();
		List<HashMap<String, Object>> storeCoInternalIndentMData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> storeInternalIndentTData = new ArrayList<HashMap<String, Object>>();
		long internalM_headerId = Long.parseLong(requestData.get("indentId"));
		long storeIssueMId = Long.parseLong(requestData.get("storeIssueMId"));
		//long mmuId = Long.parseLong(requestData.get("mmuId"));
		receivingIndentListForInventory= storesDAO.receivingIndentInInventoryCo(internalM_headerId,storeIssueMId);
		
		List<StoreCoInternalIndentM> internalIndentM = (List<StoreCoInternalIndentM>) receivingIndentListForInventory.get("internalMList");
		//issueMList
		List<StoreIssueM> issueIndentM = (List<StoreIssueM>) receivingIndentListForInventory.get("issueMList");
		if(internalIndentM.size()>0) {
			for(StoreCoInternalIndentM indentM:internalIndentM) {
				HashMap<String,Object> internM_map=new HashMap<String, Object>();
				internM_map.put("indentMId", indentM.getId());
				internM_map.put("indentNo", HMSUtil.convertNullToEmptyString(indentM.getDemandNo()));
				internM_map.put("indentDate", HMSUtil.changeDateToddMMyyyy(indentM.getDemandDate()));
				internM_map.put("issueNo", issueIndentM.get(0).getIssueNo());
				internM_map.put("issueDate", HMSUtil.changeDateToddMMyyyy(issueIndentM.get(0).getIssueDate()));
				storeCoInternalIndentMData.add(internM_map);
			}
			map.put("storeInternalIndentMData", storeCoInternalIndentMData);
			
			List<StoreIssueT> issueIndentT = (List<StoreIssueT>) receivingIndentListForInventory.get("issueTList");
			if (issueIndentT.size() > 0) {
				for (StoreIssueT indentT : issueIndentT) {
					HashMap<String, Object> issueIndentT_map = new HashMap<String, Object>();
					issueIndentT_map.put("issueTId", indentT.getId());
					issueIndentT_map.put("indentTId", indentT.getStoreCoInternalIndentT().getId());
					
					issueIndentT_map.put("pvsmNo", indentT.getMasStoreItem().getPvmsNo());
					issueIndentT_map.put("nomenclature", indentT.getMasStoreItem().getNomenclature());
					issueIndentT_map.put("au", indentT.getMasStoreItem().getMasStoreUnit().getStoreUnitName());
					issueIndentT_map.put("auId", indentT.getMasStoreItem().getMasStoreUnit().getStoreUnitId());
					issueIndentT_map.put("itemId", indentT.getMasStoreItem().getItemId());
					issueIndentT_map.put("stockId", indentT.getStoreItemBatchStock()!=null?indentT.getStoreItemBatchStock().getStockId():0);
					issueIndentT_map.put("batchNo", indentT.getStoreItemBatchStock()!=null?indentT.getStoreItemBatchStock().getBatchNo():"");
					
					if(indentT.getStoreItemBatchStock()!=null && indentT.getStoreItemBatchStock().getManufactureDate()!=null) {
						issueIndentT_map.put("dom", HMSUtil.changeDateToddMMyyyy(indentT.getStoreItemBatchStock().getManufactureDate()));
					}else {
						issueIndentT_map.put("dom", "");
					}
					 
					issueIndentT_map.put("doe", indentT.getStoreItemBatchStock()!=null?HMSUtil.changeDateToddMMyyyy(indentT.getStoreItemBatchStock().getExpiryDate()):"");
					issueIndentT_map.put("qtyDemand", indentT.getQtyRequest());
					issueIndentT_map.put("qtyIssued", indentT.getQtyIssued());
					issueIndentT_map.put("qtyReceived", indentT.getQtyIssued()); // This column will use in store_internal_indent_t and will be update.
					//issueIndentT_map.put("previousReceivedQty", indentT.getStoreCoInternalIndentT().getQtyReceived());
					if(indentT.getStoreCoInternalIndentT().getQtyReceived() == null) {
						issueIndentT_map.put("previousReceivedQty", 0);
					}else {
						issueIndentT_map.put("previousReceivedQty", indentT.getStoreCoInternalIndentT().getQtyReceived());
					}
					storeInternalIndentTData.add(issueIndentT_map);
				}
				map.put("storeInternalIndentTData", storeInternalIndentTData);
			}
			map.put("status",1);
		}else {
			map.put("status",0);
		}
		return map;
	}

	@Override
	public String addToStockIssuedIndentCo(String requestData) {
		boolean flag = false;

		JSONObject jsonRespObject = new JSONObject();
		JSONObject jObject = new JSONObject(requestData);
		
		JSONArray indentMIArr = jObject.getJSONArray("indentMId");
		long indentMId = indentMIArr.getLong(0);
		
		flag = storesDAO.addToStockIssuedIndentCo(jObject);

		if (flag) {
			jsonRespObject.put("message", "Stock added successfully .");
			jsonRespObject.put("indentMId", indentMId);
			jsonRespObject.put("status", "1");

		} else {
			jsonRespObject.put("message", "Stock is not added .");
			jsonRespObject.put("indentMId", 0);
			jsonRespObject.put("status", "0");
		}

		return jsonRespObject.toString();
	}
	
	@Override
	public Map<String, Object> generateStockStatusReport(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> storeStockData = new ArrayList<HashMap<String, Object>>();
		List<Object[]> stockList = new ArrayList<Object[]>();
		long itemId=0;
		long groupId=0;
		long sectionId=0;
		long mmuId=0;
		long deptId=0;
	
		String radioSelectValue= requestData.get("radioValue").toString();
		
		if(Long.parseLong(requestData.get("itemId").toString())!=0) {
			itemId = Long.parseLong(requestData.get("itemId").toString());
		}
		
		/*
		 * if(Long.parseLong(requestData.get("groupId").toString())!=0) { groupId =
		 * Long.parseLong(requestData.get("groupId").toString()); }
		 * 
		 * if(Long.parseLong(requestData.get("sectionId").toString())!=0) { sectionId =
		 * Long.parseLong(requestData.get("sectionId").toString()); }
		 */
		
		if(Long.parseLong(requestData.get("mmuId").toString())!=0) {
			mmuId = Long.parseLong(requestData.get("mmuId").toString());
		}
		
		
		if(Long.parseLong(requestData.get("deptId").toString())!=0) {
			deptId = Long.parseLong(requestData.get("deptId").toString());
		}
		
		stockList=  (List<Object[]>) storesDAO.generateStockStatusReport(radioSelectValue,itemId,groupId,sectionId,mmuId,deptId);
		if(stockList!=null && !stockList.isEmpty()) {
			  for(Object[] stock: stockList ) { 
				  HashMap<String,Object> storeStockMap=new HashMap<String, Object>();
				  	storeStockMap.put("receivedQty", stock[0]); 
				  	storeStockMap.put("issuedQty", stock[1]); 
				  	storeStockMap.put("opBalanceQty", stock[2]); 
				  	storeStockMap.put("closingBalanceQty", stock[3]);
				  	storeStockMap.put("pvmsNo", stock[4]); 
				  	storeStockMap.put("nomenclature", stock[5]); 
				  	
				  	if(radioSelectValue.equalsIgnoreCase("D")) {
				  		storeStockMap.put("batchNo", stock[6]);
					  	storeStockMap.put("expDate", HMSUtil.changeDateToddMMyyyy((Date) stock[7]));
						storeStockMap.put("acUnit", stock[8]);
						if(stock[9]!=null) {
							storeStockMap.put("manufactureDate", HMSUtil.changeDateToddMMyyyy((Date) stock[9]));
						}else {
							storeStockMap.put("manufactureDate", "");
						}
						if(stock[10]!=null) {
							storeStockMap.put("supplierName", stock[10]);
						}else {
							storeStockMap.put("supplierName", "");
						}
						if(stock[11]!=null) {
							storeStockMap.put("supplierTypeName", stock[11]);
						}else {
							storeStockMap.put("supplierTypeName", "");
						}
				  	}else {
				  		storeStockMap.put("acUnit", stock[6]);
				  	}
				  	
				/*
				 * if(radioSelectValue.equalsIgnoreCase("D")) {
				 * storeStockMap.put("hospitalName", stock[10]); }else {
				 * storeStockMap.put("hospitalName", stock[7]); }
				 */
				  	
				  	
				  	storeStockData.add(storeStockMap);
			  }
			 
			  if(radioSelectValue.equalsIgnoreCase("D")) {
				  map.put("storeStockData", storeStockData);
				  map.put("reportType", "D");
				  map.put("status", 1);
				 
			  }else {
				  map.put("storeStockData", storeStockData);
				  map.put("reportType", "S");
				  map.put("status", 1); 
				 
			  }
			
		}else {
			map.put("storeStockData", storeStockData);
			map.put("status", 0);
		}
		return map;
	}

	@Override
	public Map<String, Object> generateStockStatusReportDo(Map<String, String> requestData) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> storeStockData = new ArrayList<HashMap<String, Object>>();
		List<Object[]> stockList = new ArrayList<Object[]>();
		long itemId=0;
		long groupId=0;
		long sectionId=0;
		long districtId=0;
		long deptId=0;
		long mmuId=0;
	
		String radioSelectValue= requestData.get("radioValue").toString();
		
		if(Long.parseLong(requestData.get("itemId").toString())!=0) {
			itemId = Long.parseLong(requestData.get("itemId").toString());
		}
		
		/*
		 * if(Long.parseLong(requestData.get("groupId").toString())!=0) { groupId =
		 * Long.parseLong(requestData.get("groupId").toString()); }
		 * 
		 * if(Long.parseLong(requestData.get("sectionId").toString())!=0) { sectionId =
		 * Long.parseLong(requestData.get("sectionId").toString()); }
		 */
		
		if(Long.parseLong(requestData.get("districtId").toString())!=0) {
			districtId = Long.parseLong(requestData.get("districtId").toString());
		}
		
		if(Long.parseLong(requestData.get("deptId").toString())!=0) {
			deptId = Long.parseLong(requestData.get("deptId").toString());
		}
		
		stockList=  (List<Object[]>) storesDAO.generateStockStatusReportDo(radioSelectValue,itemId,groupId,sectionId,districtId,deptId);
		if(stockList!=null && !stockList.isEmpty()) {
			  for(Object[] stock: stockList ) { 
				  HashMap<String,Object> storeStockMap=new HashMap<String, Object>();
				  	storeStockMap.put("receivedQty", stock[0]); 
				  	storeStockMap.put("issuedQty", stock[1]); 
				  	storeStockMap.put("opBalanceQty", stock[2]); 
				  	storeStockMap.put("closingBalanceQty", stock[3]);
				  	storeStockMap.put("pvmsNo", stock[4]); 
				  	storeStockMap.put("nomenclature", stock[5]); 
				  	
				  	if(radioSelectValue.equalsIgnoreCase("D")) {
				  		storeStockMap.put("batchNo", stock[6]);
					  	storeStockMap.put("expDate", HMSUtil.changeDateToddMMyyyy((Date) stock[7]));
						storeStockMap.put("acUnit", stock[8]);
						if(stock[9]!=null) {
							storeStockMap.put("manufactureDate", HMSUtil.changeDateToddMMyyyy((Date) stock[9]));
						}else {
							storeStockMap.put("manufactureDate", "");
						}
						if(stock[10]!=null) {
							storeStockMap.put("supplierName", stock[10]);
						}else {
							storeStockMap.put("supplierName", "");
						}
						if(stock[11]!=null) {
							storeStockMap.put("supplierTypeName", stock[11]);
						}else {
							storeStockMap.put("supplierTypeName", "");
						}
						
				  	}else {
				  		storeStockMap.put("acUnit", stock[6]);
				  	}
				  	
				/*
				 * if(radioSelectValue.equalsIgnoreCase("D")) {
				 * storeStockMap.put("hospitalName", stock[10]); }else {
				 * storeStockMap.put("hospitalName", stock[7]); }
				 */
				  	
				  	
				  	storeStockData.add(storeStockMap);
			  }
			 
			  if(radioSelectValue.equalsIgnoreCase("D")) {
				  map.put("storeStockData", storeStockData);
				  map.put("reportType", "D");
				  map.put("status", 1);
				 
			  }else {
				  map.put("storeStockData", storeStockData);
				  map.put("reportType", "S");
				  map.put("status", 1); 
				 
			  }
			
		}else {
			map.put("storeStockData", storeStockData);
			map.put("status", 0);
		}
		return map;
	}
	
}
