package com.mmu.services.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.StoresService;

@RequestMapping("/stores")
@RestController
@CrossOrigin

public class StoresController {

	@Autowired
	private StoresService storesService;

	@RequestMapping(value = "/showOpeningBalance", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> showOpeningBalance() {
		Map<String, Object> dataList = new HashMap<String, Object>();
		dataList = storesService.getDataForOpeningBalance();
		if (dataList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(dataList, HttpStatus.OK);
	}

	@RequestMapping(value = "/getStoreItemList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getStoreItemList(@RequestBody Map<String, String> requestData) {
		Map<String, Object> storeItemList = new HashMap<String, Object>();
		storeItemList = storesService.getStoreItemList(requestData);
		if (storeItemList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(storeItemList, HttpStatus.OK);
	}

	@RequestMapping(value = "/submitStoreData", method = RequestMethod.POST)
	public ResponseEntity<String> submitStoreData(@RequestBody String requestData) {
		String result = storesService.submitStoreData(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/getOpeningBalanceWaitingList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getOpeningBalanceWaitingList(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> openingBalanceWaitingList = new HashMap<String, Object>();
		openingBalanceWaitingList = storesService.getOpeningBalanceWaitingList(requestData);
		if (openingBalanceWaitingList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(openingBalanceWaitingList, HttpStatus.OK);
	}

	@RequestMapping(value = "/getOpeningBalanceDetailsForApproval", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getOpeningBalanceDetailsForApproval(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> openingBalanceDetailsForApproval = new HashMap<String, Object>();
		openingBalanceDetailsForApproval = storesService.getOpeningBalanceDetailsForApproval(requestData);
		if (openingBalanceDetailsForApproval.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(openingBalanceDetailsForApproval, HttpStatus.OK);
	}

	@RequestMapping(value = "/submitOpeningBalanceDataForApproval", method = RequestMethod.POST)
	public ResponseEntity<String> submitOpeningBalanceDataForApproval(@RequestBody String requestData) {
		String result = storesService.submitOpeningBalanceDataForApproval(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDataForStockStatusReport", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getDataForStockStatusReport(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> dataForStockStatus = new HashMap<String, Object>();
		dataForStockStatus = storesService.getDataForStockStatusReport(requestData);
		if (dataForStockStatus.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(dataForStockStatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/generateStockStatusReport", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> generateStockStatusReport(@RequestBody Map<String, String> requestData) {
		Map<String, Object> stockStatusReport = new HashMap<String, Object>();
		stockStatusReport = storesService.generateStockStatusReport(requestData);
		if (stockStatusReport.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(stockStatusReport, HttpStatus.OK);
	}

	@RequestMapping(value = "/getReceivingIndentWaitingList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getReceivingIndentWaitingList(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> receivingIndentWaitingList = new HashMap<String, Object>();
		receivingIndentWaitingList = storesService.getReceivingIndentWaitingList(requestData);
		if (receivingIndentWaitingList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(receivingIndentWaitingList, HttpStatus.OK);
	}

	@RequestMapping(value = "/receivingIndentInInventory", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> receivingIndentInInventory(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> receivingIndentList = new HashMap<String, Object>();
		receivingIndentList = storesService.receivingIndentInInventory(requestData);
		if (receivingIndentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(receivingIndentList, HttpStatus.OK);
	}

	@RequestMapping(value = "/addToStockIssuedIndent", method = RequestMethod.POST)
	public ResponseEntity<String> addToStockIssuedIndent(@RequestBody String requestData) {
		String result = storesService.addToStockIssuedIndent(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/getStoreItemListForAutoComplete", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getStoreItemListForAutoComplete(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> itemList = new HashMap<String, Object>();
		itemList = storesService.getStoreItemListForAutoComplete(requestData);
		if (itemList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(itemList, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatePhysicalStockTaking", method = RequestMethod.POST)
	public ResponseEntity<String> updatePhysicalStockTaking(@RequestBody String requestData) {
		String result = storesService.updatePhysicalStockTaking(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/stockTakingWaitingListData", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> stockTakingWaitingListData(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> stockTakingWaitingList = new HashMap<String, Object>();
		stockTakingWaitingList = storesService.stockTakingWaitingListData(requestData);
		if (stockTakingWaitingList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(stockTakingWaitingList, HttpStatus.OK);
	}

	@RequestMapping(value = "/stockTakingDetailsForApproval", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> stockTakingDetailsForApproval(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> stockTakingDetails = new HashMap<String, Object>();
		stockTakingDetails = storesService.stockTakingDetailsForApproval(requestData);
		if (stockTakingDetails.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(stockTakingDetails, HttpStatus.OK);
	}

	@RequestMapping(value = "/submitStockingTakingDataForApproval", method = RequestMethod.POST)
	public ResponseEntity<String> submitStockingTakingDataForApproval(@RequestBody String requestData) {
		String result = storesService.submitStockingTakingDataForApproval(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDataForCreateSupplyOrder", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getDataForCreateSupplyOrder(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> dataForCreateSupplyOrder = new HashMap<String, Object>();
		dataForCreateSupplyOrder = storesService.getDataForCreateSupplyOrder(requestData);
		if (dataForCreateSupplyOrder.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(dataForCreateSupplyOrder, HttpStatus.OK);
	}

	@RequestMapping(value = "/getSupplyOrderSanctionData", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getSupplyOrderSanctionData(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> sancationDataForSO = new HashMap<String, Object>();
		sancationDataForSO = storesService.getSupplyOrderSanctionData(requestData);
		if (sancationDataForSO.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(sancationDataForSO, HttpStatus.OK);
	}

	@RequestMapping(value = "/saveSupplyOrder", method = RequestMethod.POST)
	public ResponseEntity<String> saveSupplyOrder(@RequestBody String requestData) {
		String response = storesService.saveSupplyOrder(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/supplyOrderWaitingList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> showSupplyOrderListData(@RequestBody Map<String, String> requestData) {
		Map<String, Object> supplyOrderListData = new HashMap<String, Object>();
		supplyOrderListData = storesService.showSupplyOrderListData(requestData);
		if (supplyOrderListData.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(supplyOrderListData, HttpStatus.OK);
	}

	@RequestMapping(value = "/supplyOrderWaitingListForPendingApproval", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> supplyOrderWaitingListForPendingApproval(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> supplyOrderListData = new HashMap<String, Object>();
		supplyOrderListData = storesService.supplyOrderWaitingListForPendingApproval(requestData);
		if (supplyOrderListData.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(supplyOrderListData, HttpStatus.OK);
	}

	@RequestMapping(value = "/supplyOrderDetailsForApproval", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> supplyOrderDetailsForApproval(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> supplyOrderDetails = new HashMap<String, Object>();
		supplyOrderDetails = storesService.supplyOrderDetailsForApproval(requestData);
		if (supplyOrderDetails.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(supplyOrderDetails, HttpStatus.OK);
	}

	@RequestMapping(value = "/saveOrSubmitSupplyOrderForApproval", method = RequestMethod.POST)
	public ResponseEntity<String> saveOrSubmitSupplyOrderForApproval(@RequestBody String requestData) {
		String response = storesService.saveOrSubmitSupplyOrderForApproval(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRVWaitingListAgainstSo", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getRVWaitingListAgainstSo(@RequestBody Map<String, String> requestData) {
		Map<String, Object> rVWaitingListAgainstSo = new HashMap<String, Object>();
		rVWaitingListAgainstSo = storesService.getRVWaitingListAgainstSo(requestData);
		if (rVWaitingListAgainstSo.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(rVWaitingListAgainstSo, HttpStatus.OK);
	}

	@RequestMapping(value = "/submitRvAgainstSupplyOrder", method = RequestMethod.POST)
	public ResponseEntity<String> submitRvAgainstSupplyOrder(@RequestBody String requestData) {
		String response = storesService.submitRvAgainstSupplyOrder(requestData);
		if (response.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/submitDirectReceivingSo", method = RequestMethod.POST)
	public ResponseEntity<String> submitDirectReceivingSo(@RequestBody String requestData) {
		String result = storesService.submitDirectReceivingSo(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/createBudgetaryForBackDateLP", method = RequestMethod.POST)
	public ResponseEntity<String> createBudgetaryForBackDateLP(@RequestBody String requestData) {
		String result = storesService.createBudgetaryForBackDateLP(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteRowFromDatabase", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> deleteRowFromDatabase(@RequestBody Map<String, String> requestData) {
		Map<String, Object> rowStatus = new HashMap<String, Object>();
		rowStatus = storesService.deleteRowFromDatabase(requestData);
		if (rowStatus.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(rowStatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/getSupplierListForStore", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getSupplierListForStore(@RequestBody Map<String, String> requestData) {
		Map<String, Object> supplierData = new HashMap<String, Object>();
		supplierData = storesService.getSupplierListForStore(requestData);
		if (supplierData.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(supplierData, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAuNameListForStore", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getAuNameListForStore(@RequestBody Map<String, String> requestData) {
		Map<String, Object> auNameList = new HashMap<String, Object>();
		auNameList = storesService.getAuNameListForStore(requestData);
		if (auNameList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(auNameList, HttpStatus.OK);
	}

	@RequestMapping(value = "/showItemListForbackDateBudgetary", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> showItemListForbackDateBudgetary(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> itemListForBackDateBudgetary = new HashMap<String, Object>();
		itemListForBackDateBudgetary = storesService.showItemListForbackDateBudgetary(requestData);
		if (itemListForBackDateBudgetary.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(itemListForBackDateBudgetary, HttpStatus.OK);
	}

	@RequestMapping(value = "/getPvmsItemForExcel", method = RequestMethod.POST)
	public ResponseEntity<String> getPvmsItemForExcel(@RequestBody String requestData) {
		String dataList = storesService.getPvmsItemForExcel(requestData);
		if (dataList.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(dataList, HttpStatus.OK);
	}

	@RequestMapping(value = "/importPVMSItemFromExcel", method = RequestMethod.POST)
	public ResponseEntity<String> importPVMSItemFromExcel(@RequestBody String requestData) {
		String excelData = storesService.importPVMSItemFromExcel(requestData);
		if (excelData.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(excelData, HttpStatus.OK);
	}

	@RequestMapping(value = "/importPvmsdataReceivedFromiAushadhi", method = RequestMethod.POST)
	public ResponseEntity<String> importPvmsdataReceivedFromiAushadhi(@RequestBody String requestData) {
		String excelData = storesService.importPvmsdataReceivedFromiAushadhi(requestData);
		if (excelData.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(excelData, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getReceivingIndentWaitingListCo", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getReceivingIndentWaitingListCo(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> receivingIndentWaitingList = new HashMap<String, Object>();
		receivingIndentWaitingList = storesService.getReceivingIndentWaitingListCo(requestData);
		if (receivingIndentWaitingList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(receivingIndentWaitingList, HttpStatus.OK);
	}

	@RequestMapping(value = "/receivingIndentInInventoryCo", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> receivingIndentInInventoryCo(
			@RequestBody Map<String, String> requestData) {
		Map<String, Object> receivingIndentList = new HashMap<String, Object>();
		receivingIndentList = storesService.receivingIndentInInventoryCo(requestData);
		if (receivingIndentList.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(receivingIndentList, HttpStatus.OK);
	}

	@RequestMapping(value = "/addToStockIssuedIndentCo", method = RequestMethod.POST)
	public ResponseEntity<String> addToStockIssuedIndentCo(@RequestBody String requestData) {
		String result = storesService.addToStockIssuedIndentCo(requestData);
		if (result.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/generateStockStatusReportCo", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> generateStockStatusReportCo(@RequestBody Map<String, String> requestData) {
		Map<String, Object> stockStatusReport = new HashMap<String, Object>();
		stockStatusReport = storesService.generateStockStatusReportCo(requestData);
		if (stockStatusReport.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(stockStatusReport, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/generateStockStatusReportDo", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> generateStockStatusReportDo(@RequestBody Map<String, String> requestData) {
		Map<String, Object> stockStatusReport = new HashMap<String, Object>();
		stockStatusReport = storesService.generateStockStatusReportDo(requestData);
		if (stockStatusReport.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String, Object>>(stockStatusReport, HttpStatus.OK);
	}

}
