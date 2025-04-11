package com.mmu.services.service;

import java.util.List;
import java.util.Map;

import com.mmu.services.dto.CaptureInvoices;
import com.mmu.services.dto.MedicalStore;
import com.mmu.services.dto.SourceOfMedicine;

public interface MedicineInvoiceService {
	List<SourceOfMedicine> getSourceOfMedicines();
	List<MedicalStore> getMedicalStores();
	List<MedicalStore> getMedicalStores(Long id,Long districtId);
	Map<String, Object> saveCaptureInvoices(CaptureInvoices captureInvoices);
	Map<String, Object> updateCaptureInvoices(CaptureInvoices captureInvoices);
	Map<String, Object> fetchAllList();
	Map<String, Object> fetchAllList(Map<String, Object> params);
	CaptureInvoices getCaptureInvoices(Long Id);
	CaptureInvoices getCaptureInvoicesBasedOnBatchNo(Integer Id);
	
	Map<String, Object> getInvoiceReprtsDetails(Map<String, Object> params);
	Map<String, Object> fetchAllInvoiceList(Map<String, Object> params);
	 
}
