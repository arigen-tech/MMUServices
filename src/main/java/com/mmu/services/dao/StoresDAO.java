package com.mmu.services.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MasItemClass;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasStoreGroup;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSection;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.MasStoreSupplierType;
import com.mmu.services.entity.MasStoreUnit;
import com.mmu.services.entity.StoreSoM;

@Repository
public interface StoresDAO {

	List<Object[]> getStoreItemList(long itemGroupId, long itemTypeId, long itemSectionId, long itemCategoryId,
			long itemClassId, String pvmsNo, String nomenclturId);

	boolean submitOpeningBalance(JSONObject jObject);

	List<MasStoreGroup> getGroupList();

	List<MasItemType> getItemTypeList();

	List<MasItemClass> getItemClassList();

	List<MasStoreSection> getStoreSectionList();

	Map<String, Object> getOpeningBalanceWaitingList(String fromDate, String toDate, String mmuId, String cityId,
			String districtId, long departmentId2, int pageNo);

	Map<String, Object> getOpeningBalanceDetailsForApproval(long balanceM_headerId);

	boolean submitOpeningBalanceDataForApproval(JSONObject jObject);

	List<Object[]> generateStockStatusReport(String radioSelectValue, long itemId, long groupId, long sectionId,
			long mmuId, long deptId);

	Map<String, Object> getReceivingIndentWaitingList(String fromDate, String toDate, long hospitalId, String indentNo,
			long departmentId, int pageNo);

	Map<String, Object> receivingIndentInInventory(long internalM_headerId, long storeIssueMId);

	boolean addToStockIssuedIndent(JSONObject jObject);

	List<MasStoreItem> getStoreItemListForAutoComplete(String pvmsNo, String nomenclature, String itemId);

	boolean updatePhysicalStockTaking(JSONObject jObject);

	Map<String, Object> stockTakingDetailsForApproval(long takingM_headerId);

	boolean submitStockingTakingDataForApproval(JSONObject jObject);

	List<StoreSoM> getDataForCreateSupplyOrder(long hospitalId, long departmentId);

	Map<String, Object> getSupplyOrderSanctionData(long soMId);

	List<MasStoreSupplierType> getsupplierTypeList();

	boolean saveSupplyOrder(JSONObject jObject);

	List<MasStoreSupplier> getsupplierList(long hospitalId);

	Map<String, Object> getDataForSupplyOrderList(long hospitalId, long departmentId, long vendorId, int pageNo);

	Map<String, Object> supplyOrderDetailsForApproval(long poM_headerId);

	boolean saveOrSubmitSupplyOrderForApproval(JSONObject jObject);

	Map<String, Object> getRVWaitingListAgainstSo(long hospitalId, long departmentId, long vendorId, String fromDate,
			String toDate, String soNo, int pageNo);

	long submitRvAgainstSupplyOrder(JSONObject jObject);

	boolean submitDirectReceivingSo(JSONObject jObject);

	boolean deleteRowFromDatabase(long headerMId, long detailTId, String dataFlag);

	Map<String, Object> supplyOrderWaitingListForPendingApproval(long hospitalId, long departmentId, long vendorId,
			int pageNo);

	Map<String, Object> getPvmsItemForExcel(String requestData);

	Map<String, Object> importPVMSItemFromExcel(String requestData);

	Map<String, Object> importPvmsdataReceivedFromiAushadhi(String requestData);

	List<MasStoreUnit> getAuNameListForStore();

	Map<String, Object> getItemListForbackDateBudgetary(String fromDate, String toDate, long hospitalId,
			long departmentId, long supplierId, int pageNo);

	long createBudgetaryForBackDateLP(JSONObject jObject);

	String getIssuedByName(Long storeIssueIndentMId);

	Map<String, Object> receivingIndentInInventoryCo(long internalM_headerId, long storeIssueMId);

	boolean addToStockIssuedIndentCo(JSONObject jObject);

	Map<String, Object> getReceivingIndentWaitingListCo(String fromDate, String toDate, long mmuId, String indentNo,
			long departmentId, int pageNo);
	
	List<Object[]> generateStockStatusReportCo(String radioSelectValue, long itemId, long groupId, long sectionId,
			long cityId, long deptId);

	List<Object[]> generateStockStatusReportDo(String radioSelectValue, long itemId, long groupId, long sectionId,
			long districtId, long deptId);

	Map<String, Object> stockTakingWaitingListData(String fromDate, String toDate, String mmuId, String cityId,
			String districtId, long departmentId, int pageNo);

}
