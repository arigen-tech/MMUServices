package com.mmu.services.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mmu.services.dto.MedicineInvoiceList;
import com.mmu.services.entity.CaptureMedicineInvoiceDetails;

@Repository
public interface MedicineInvoiceDao {
	public long saveMedicineInvoice(CaptureMedicineInvoiceDetails medicineInvoiceDetails);
	public List<MedicineInvoiceList> findAllInvoiceList();
	public Map<String,Object> findAllInvoiceList(Map<String,Object> params);
	public Integer lastBatch();
	public List<CaptureMedicineInvoiceDetails> findCaptureInvoiceList(Long Id);
	public Long updateMedicineInvoice(CaptureMedicineInvoiceDetails invoiceDetails,Long id);
	public List<Long> getInvoiceId(Integer batchNo);
	public Integer deleteCaptureInvoice(Long Id);
	public Object getInvoiceReports(Map<String,Object> params);
	public Map<String,Object> findAllInvoiceListBasedOnGroup(Map<String, Object> params);
	public List<CaptureMedicineInvoiceDetails> findCaptureInvoiceByBatch(Integer Id);
	
	public Integer getInvoiceData(Integer districtId,Integer cityId,Integer month,Integer year,Integer supplierTypeId,String invoiceNumber); 
}
