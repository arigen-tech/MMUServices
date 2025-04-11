package com.mmu.services.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface StoresService {

	Map<String, Object> getStoreItemList(Map<String, String> requestData);

	String submitStoreData(String requestData);

	Map<String, Object> getDataForOpeningBalance();

	Map<String, Object> getOpeningBalanceWaitingList(Map<String, String> requestData);

	Map<String, Object> getOpeningBalanceDetailsForApproval(Map<String, String> requestData);

	String submitOpeningBalanceDataForApproval(String requestData);

	Map<String, Object> generateStockStatusReport(Map<String, String> requestData);

	Map<String, Object> getDataForStockStatusReport(Map<String, String> requestData);

	Map<String, Object> getReceivingIndentWaitingList(Map<String, String> requestData);

	Map<String, Object> receivingIndentInInventory(Map<String, String> requestData);

	String addToStockIssuedIndent(String requestData);

	Map<String, Object> getDataForCreateSupplyOrder(Map<String, String> requestData);

	Map<String, Object> getStoreItemListForAutoComplete(Map<String, String> requestData);

	String updatePhysicalStockTaking(String requestData);

	Map<String, Object> stockTakingWaitingListData(Map<String, String> requestData);

	Map<String, Object> stockTakingDetailsForApproval(Map<String, String> requestData);

	String submitStockingTakingDataForApproval(String requestData);

	Map<String, Object> getSupplyOrderSanctionData(Map<String, String> requestData);

	String saveSupplyOrder(String requestData);

	Map<String, Object> showSupplyOrderListData(Map<String, String> requestData);

	Map<String, Object> supplyOrderDetailsForApproval(Map<String, String> requestData);

	String saveOrSubmitSupplyOrderForApproval(String requestData);

	Map<String, Object> getRVWaitingListAgainstSo(Map<String, String> requestData);

	String submitRvAgainstSupplyOrder(String requestData);

	String submitDirectReceivingSo(String requestData);

	Map<String, Object> deleteRowFromDatabase(Map<String, String> requestData);

	Map<String, Object> supplyOrderWaitingListForPendingApproval(Map<String, String> requestData);

	Map<String, Object> getSupplierListForStore(Map<String, String> requestData);

	String getPvmsItemForExcel(String requestData);

	String importPVMSItemFromExcel(String requestData);

	String importPvmsdataReceivedFromiAushadhi(String requestData);

	Map<String, Object> getAuNameListForStore(Map<String, String> requestData);

	Map<String, Object> showItemListForbackDateBudgetary(Map<String, String> requestData);

	String createBudgetaryForBackDateLP(String requestData);

	Map<String, Object> getReceivingIndentWaitingListCo(Map<String, String> requestData);

	Map<String, Object> receivingIndentInInventoryCo(Map<String, String> requestData);

	String addToStockIssuedIndentCo(String requestData);

	Map<String, Object> generateStockStatusReportCo(Map<String, String> requestData);

	Map<String, Object> generateStockStatusReportDo(Map<String, String> requestData);

}
