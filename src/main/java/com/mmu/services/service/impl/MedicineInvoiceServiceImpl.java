package com.mmu.services.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.DispensaryDao;
import com.mmu.services.dao.FundHcbDao;
import com.mmu.services.dao.MedicineInvoiceDao;
import com.mmu.services.dto.CaptureInvoices;
import com.mmu.services.dto.InvoiceDetail;
import com.mmu.services.dto.MedicalStore;
import com.mmu.services.dto.MedicineInvoiceList;
import com.mmu.services.dto.SourceOfMedicine;
import com.mmu.services.entity.CaptureMedicineInvoiceDetails;
import com.mmu.services.entity.FundHcb;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.MasStoreSupplierNew;
import com.mmu.services.entity.MasStoreSupplierType;
import com.mmu.services.service.MedicineInvoiceService;
import com.mmu.services.utils.CommonUtil;
import com.mmu.services.utils.HMSUtil;

@Service
public class MedicineInvoiceServiceImpl implements MedicineInvoiceService{
	public static final Long HEAD_TYPE_ID =3L;
	
	@Autowired
	private DispensaryDao dispensaryDao;
	
	@Autowired
	private MedicineInvoiceDao medicalInvoice;
	
	@Autowired
	FundHcbDao fundHcbDao;
	
	@Override
	public List<SourceOfMedicine> getSourceOfMedicines() {
		List<SourceOfMedicine> sourceOfMedicines = new ArrayList<>();
		List<MasStoreSupplierType> masStoreSupplierTypeList=  dispensaryDao.getMasStoreSupplierType();
		if(!masStoreSupplierTypeList.isEmpty()) {
			sourceOfMedicines = masStoreSupplierTypeList.stream().map(e->new SourceOfMedicine(e.getSupplierTypeId(), 
					e.getSupplierTypeCode(), e.getSupplierTypeName(), e.getStatus().equalsIgnoreCase("Y"))).collect(Collectors.toList());
		}
		return sourceOfMedicines ;
	}

	@Override
	public List<MedicalStore> getMedicalStores() {
		List<MedicalStore> medicalStores = new ArrayList<>();
		List<MasStoreSupplierNew> masStoreSupplierTypeList=  dispensaryDao.getMasStoreSupplierNew();
		
		if(!masStoreSupplierTypeList.isEmpty()) {
			medicalStores = masStoreSupplierTypeList
								.stream()
								.map(e->new MedicalStore(e.getSupplierId(),e.getSupplierCode(),e.getSupplierName())).collect(Collectors.toList());
			 
		}
		return medicalStores;
	}

	@Override
	public List<MedicalStore> getMedicalStores(Long id,Long districtId) {
		List<MedicalStore> medicalStores = new ArrayList<>();
		List<MasStoreSupplierNew> masStoreSupplierTypeList=  dispensaryDao.getMasStoreSupplierNew(id,districtId);
		
		if(!masStoreSupplierTypeList.isEmpty()) {
			medicalStores = masStoreSupplierTypeList
								.stream()
								.map(e->new MedicalStore(e.getSupplierId(),e.getSupplierCode(),e.getSupplierName())).collect(Collectors.toList());
			 
		}
		return medicalStores;
	}

	@Override
	public Map<String, Object> saveCaptureInvoices(CaptureInvoices captureInvoices) {
		Map<String,Object> response = new HashMap<>();
		if(duplicateCheck(captureInvoices)) {
			response.put("msg", "Record already exists.");
			response.put("status", 0);
			return response;
		}
		List<InvoiceDetail> invoiceList = captureInvoices.getInvoiceDetails();
	
		Long result =  0l;
		Integer lastBatch = medicalInvoice.lastBatch();
		int currentValue = lastBatch!=null ?lastBatch.intValue():0;
		long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);
		if(!captureInvoices.getAction().equalsIgnoreCase("P")) {
			Map<String,Long> fundDeatils = getFundDetails(captureInvoices);
			if(fundDeatils.get("Status")!=1) {
                Long balancShow=null;
                if(fundDeatils.get("balance")!=null)
                {
                	balancShow=fundDeatils.get("balance");	
                }else {
                	balancShow=0l;
                }
				response.put("msg", "Invoice amount should not be more than allocated fund !Current Balance is :"+balancShow+"");
				response.put("status", 0);
				return response;
			}
		}
		
		for(InvoiceDetail invoiceDetail: invoiceList) {
			CaptureMedicineInvoiceDetails invoiceDetails =  new CaptureMedicineInvoiceDetails();
			invoiceDetails.setDistrictId(captureInvoices.getDistrictId());
			invoiceDetails.setCityId(captureInvoices.getCityId());
			invoiceDetails.setBillYear(captureInvoices.getYear());
			invoiceDetails.setBillMonth(captureInvoices.getMonth());
			
			invoiceDetails.setSupplierTypeId(invoiceDetail.getSourceOfMedicine());
			invoiceDetails.setSupplierId(invoiceDetail.getMedicalStore());
			invoiceDetails.setInvoiceDate(CommonUtil.convertStringDate(invoiceDetail.getInvoiceDate()));
			invoiceDetails.setInvoiceNumber(invoiceDetail.getInvoiceNum());
			invoiceDetails.setInvoiceAmount(invoiceDetail.getInoviceAmount());
			invoiceDetails.setInvoiceDoc(invoiceDetail.getFileName());
			invoiceDetails.setAction(captureInvoices.getAction());
			invoiceDetails.setLastChgBy(captureInvoices.getUserId());
			invoiceDetails.setLastChangeDate(date);
			invoiceDetails.setBatchNo(currentValue+1);
			invoiceDetails.setUserCityId(captureInvoices.getUserCityId());
			invoiceDetails.setHeadTypeId(captureInvoices.getHeadTypeId());
			invoiceDetails.setPhase(captureInvoices.getPhase());
			
			result = medicalInvoice.saveMedicineInvoice(invoiceDetails);	
		}
		if (result >0) {
		
			response.put("msg", "Data saved Successfully");
			response.put("status", 1);
		} else {
	
			response.put("msg", "Data Not saved");
			response.put("status", 0);
		}	
		return response;
		
		
	}
	
	private boolean duplicateCheck(CaptureInvoices captureInvoices) {
		List<InvoiceDetail> invoiceList = captureInvoices.getInvoiceDetails();
		for(InvoiceDetail invoiceDetail:invoiceList) {
			if(invoiceDetail.getInvoiceId()!=null &&  invoiceDetail.getInvoiceId()>0) {
				continue;
			}
			Integer response = medicalInvoice.getInvoiceData(captureInvoices.getDistrictId(), 
					captureInvoices.getCityId(), captureInvoices.getMonth(), captureInvoices.getYear(), invoiceDetail.getSourceOfMedicine(), invoiceDetail.getInvoiceNum());
			if(response>0) {
				return true;
			}
		}
		return false;
	}
	
	public Map<String, Long> getFundDetails(CaptureInvoices captureInvoices){
		Map<String, Long> map = new HashMap<>();
		String status = "Status";
		Long invoiceAmount = captureInvoices.getInvoiceDetails().stream().mapToLong(InvoiceDetail::getInoviceAmount).sum();
		
		//captureInvoices.getPhase();
		//Integer month=captureInvoices.getMonth();
		/*if(month==4) {
			Integer yearPre=captureInvoices.getYear();
			Integer yearNext=yearPre+1;
			String finalcialYear=""+yearPre+"-"+yearNext;
		}*/
		
		
		//MasStoreFinancial financialYearVal=fundHcbDao.getMasStoreFinancilaYear(finalcialYear);
		//Long  interestAmount =0l;
		  // interestAmount = fundHcbDao.getCaptureHdCityPhaseLevel(Long.valueOf(captureInvoices.getDistrictId().intValue()),Long.valueOf(captureInvoices.getCityId().intValue()),
			//	captureInvoices.getHeadTypeId(),captureInvoices.getPhase() );
		
		try {
			//FundHcb lasthcbData = fundHcbDao.getFundHcb(Long.valueOf(captureInvoices.getDistrictId().intValue()), HEAD_TYPE_ID);
			
			FundHcb lasthcbData = fundHcbDao.getFundHcbCityLevel(Long.valueOf(captureInvoices.getDistrictId().intValue()),Long.valueOf(captureInvoices.getCityId().intValue()), captureInvoices.getHeadTypeId(),captureInvoices.getPhase());
			System.out.println("first row balance is ::"+lasthcbData.getHcbBalance());			 
			if (lasthcbData != null && invoiceAmount <= (lasthcbData.getHcbBalance())) {
				//FundHcb fundDetails = fundHcbDao.getFundHcb(Long.valueOf(captureInvoices.getDistrictId().intValue()), HEAD_TYPE_ID, HMSUtil.getTodayFormattedDate());
				FundHcb fundDetails = fundHcbDao.getFundHcbCity(Long.valueOf(captureInvoices.getDistrictId().intValue()),Long.valueOf(captureInvoices.getCityId().intValue()), captureInvoices.getHeadTypeId(), HMSUtil.getTodayFormattedDate(),captureInvoices.getPhase());
				if(fundDetails!=null) {
					//fundDetails.setOpeningBalance(fundDetails.getHcbBalance());
					fundDetails.setPhase(captureInvoices.getPhase());
					fundDetails.setDebitCredit(0-invoiceAmount);
					fundDetails.setHcbBalance(fundDetails.getHcbBalance()-invoiceAmount);
					fundHcbDao.updateFundHcb(fundDetails);
				}else {
					FundHcb fundHcb = new FundHcb();
					fundHcb.setUpssId(Long.valueOf(captureInvoices.getDistrictId()));
					fundHcb.setHeadTypeId(captureInvoices.getHeadTypeId());
					fundHcb.setHcbDate(HMSUtil.getTodayFormattedDate());
					fundHcb.setOpeningBalance(lasthcbData.getHcbBalance());
					fundHcb.setDebitCredit(0-invoiceAmount);
					fundHcb.setFundFlag("U");
					fundHcb.setCityId(Long.valueOf(captureInvoices.getCityId()));
					fundHcb.setHcbBalance( lasthcbData.getHcbBalance()-invoiceAmount);	
					fundHcb.setPhase(captureInvoices.getPhase());
					fundHcbDao.saveFundHcb(fundHcb);
				}
				System.out.println("current last row balance is ::"+lasthcbData.getHcbBalance());	
				map.put(status, 1l);
				map.put("balance", lasthcbData.getHcbBalance());
			}else {
				map.put(status, 0l);
				map.put("balance", lasthcbData.getHcbBalance());
			}
		} catch (Exception e) {
			map.put(status, 2l);
		}
		
		
		return map;
	}

	@Override
	public Map<String, Object> fetchAllList() {
		Map<String,Object> response = new HashMap<>();
		
		List<MedicineInvoiceList> result = medicalInvoice.findAllInvoiceList();
		response.put("msg", "Medicine List get Successfully");
		response.put("count", result.size());
		response.put("data", result);
		response.put("status", !result.isEmpty()?1:0);
		return response;
	}

	@Override
	public Map<String, Object> fetchAllList(Map<String, Object> params) {
		Map<String,Object> response = new HashMap<>();
		
		Map<String,Object> result = medicalInvoice.findAllInvoiceList(params);
		
		
		response.put("msg", "Medicine List get Successfully");
		response.put("count", result.get("totalMatches"));
		response.put("data", result.get("medicineList"));
		response.put("status", !result.isEmpty()?1:0);
		return response;
	}

	@Override
	public CaptureInvoices getCaptureInvoices(Long Id) {
		List<CaptureMedicineInvoiceDetails> result = medicalInvoice.findCaptureInvoiceList(Id);
		CaptureInvoices captureInvoices = new CaptureInvoices();
		captureInvoices.setYear(result.get(0).getBillYear());
		captureInvoices.setMonth(result.get(0).getBillMonth());
		captureInvoices.setCityId(result.get(0).getCityId());
		captureInvoices.setDistrictId(result.get(0).getDistrictId());
		captureInvoices.setAction(result.get(0).getAction());
		captureInvoices.setPhase(result.get(0).getPhase());
		captureInvoices.setBatchNo(String.valueOf(result.get(0).getBatchNo()));
		List<InvoiceDetail>	invoiceList = result.stream().map(e->	 
				new InvoiceDetail(e.getSupplierTypeId(), e.getSupplierId(), HMSUtil.convertUtilDateToddMMyyyy(e.getInvoiceDate()), e.getInvoiceNumber(), e.getInvoiceAmount(), e.getInvoiceDoc(),e.getMedicineInvoiceId())
		).collect(Collectors.toList());
		captureInvoices.setInvoiceDetails(invoiceList);
		return captureInvoices;
	}
	
	@Override
	public CaptureInvoices getCaptureInvoicesBasedOnBatchNo(Integer Id) {
		List<CaptureMedicineInvoiceDetails> result = medicalInvoice.findCaptureInvoiceByBatch(Id);
		CaptureInvoices captureInvoices = new CaptureInvoices();
		captureInvoices.setYear(result.get(0).getBillYear());
		captureInvoices.setMonth(result.get(0).getBillMonth());
		captureInvoices.setCityId(result.get(0).getCityId());
		captureInvoices.setDistrictId(result.get(0).getDistrictId());
		captureInvoices.setAction(result.get(0).getAction());
		captureInvoices.setBatchNo(String.valueOf(result.get(0).getBatchNo()));
		captureInvoices.setPhase(String.valueOf(result.get(0).getPhase()));
		
		List<InvoiceDetail>	invoiceList = result.stream().map(e->	 
				new InvoiceDetail(e.getSupplierTypeId(), e.getSupplierId(), HMSUtil.convertUtilDateToddMMyyyy(e.getInvoiceDate()), e.getInvoiceNumber(), e.getInvoiceAmount(), e.getInvoiceDoc(),e.getMedicineInvoiceId())
		).collect(Collectors.toList());
		captureInvoices.setInvoiceDetails(invoiceList);
		return captureInvoices;
	}

	@Override
	public Map<String, Object> updateCaptureInvoices(CaptureInvoices captureInvoices) {
		Map<String,Object> response = new HashMap<>();
		if(duplicateCheck(captureInvoices)) {
			response.put("msg", "Record already exists.");
			response.put("status", 0);
			return response;
		}
		List<InvoiceDetail> invoiceList = captureInvoices.getInvoiceDetails();
		
		Long result =  0l;
		List<Long> previousInvoices = medicalInvoice.getInvoiceId(Integer.valueOf(captureInvoices.getBatchNo()));
		long d = System.currentTimeMillis();
		Timestamp date = new Timestamp(d);
		if(!captureInvoices.getAction().equalsIgnoreCase("P")) {
			Map<String,Long> fundDeatils = getFundDetails(captureInvoices);
			if(fundDeatils.get("Status")!=1) {
				 Long balancShow=null; 
				if(fundDeatils.get("balance")!=null)
	                {
	                	balancShow=fundDeatils.get("balance");	
	                }else {
	                	balancShow=0l;
	                }
				response.put("msg", "Invoice amount should not be more than allocated fund ! Current Balance is :"+balancShow+"");
				response.put("status", 0);
				return response;
			}
		}
		
		for(InvoiceDetail invoiceDetail: invoiceList) {
			CaptureMedicineInvoiceDetails invoiceDetails =  new CaptureMedicineInvoiceDetails();
			invoiceDetails.setDistrictId(captureInvoices.getDistrictId());
			invoiceDetails.setCityId(captureInvoices.getCityId());
			invoiceDetails.setBillYear(captureInvoices.getYear());
			invoiceDetails.setBillMonth(captureInvoices.getMonth());
			
			invoiceDetails.setSupplierTypeId(invoiceDetail.getSourceOfMedicine());
			invoiceDetails.setSupplierId(invoiceDetail.getMedicalStore());
			invoiceDetails.setInvoiceDate(CommonUtil.convertStringDate(invoiceDetail.getInvoiceDate()));
			invoiceDetails.setInvoiceNumber(invoiceDetail.getInvoiceNum());
			invoiceDetails.setInvoiceAmount(invoiceDetail.getInoviceAmount());
			invoiceDetails.setInvoiceDoc(invoiceDetail.getFileName());
			invoiceDetails.setAction(captureInvoices.getAction());
			invoiceDetails.setLastChgBy(captureInvoices.getUserId());
			invoiceDetails.setLastChangeDate(date);
			invoiceDetails.setBatchNo(Integer.valueOf(captureInvoices.getBatchNo()));
			invoiceDetails.setHeadTypeId(captureInvoices.getHeadTypeId());
			invoiceDetails.setPhase(captureInvoices.getPhase());
			if(invoiceDetail.getInvoiceId()!= null && invoiceDetail.getInvoiceId()>0) {
				result = medicalInvoice.updateMedicineInvoice(invoiceDetails,invoiceDetail.getInvoiceId());
				previousInvoices.remove(invoiceDetail.getInvoiceId());
			}
			else {
				result = medicalInvoice.saveMedicineInvoice(invoiceDetails);
			}
		}
		if (result >0) {
		
			response.put("msg", "Data saved Successfully");
			response.put("status", 1);
			if(!previousInvoices.isEmpty()) {
				for(Long id:previousInvoices) {
					medicalInvoice.deleteCaptureInvoice(id);
				}	
			}
		} else {
	
			response.put("msg", "Data Not saved");
			response.put("status", 0);
		}	
		return response;
		
		
	}

	@Override
	public Map<String, Object> getInvoiceReprtsDetails(Map<String, Object> params) {
		Map<String,Object> response = new HashMap<>();
		return response;
	}

	@Override
	public Map<String, Object> fetchAllInvoiceList(Map<String, Object> params) {
		Map<String,Object> response = new HashMap<>();
		
		Map<String,Object> result = medicalInvoice.findAllInvoiceListBasedOnGroup(params);
		
		
		response.put("msg", "Medicine List get Successfully");
		response.put("count", result.get("totalMatches"));
		response.put("data", result.get("medicineList"));
		response.put("status", !result.isEmpty()?1:0);
		return response;
	}


}
