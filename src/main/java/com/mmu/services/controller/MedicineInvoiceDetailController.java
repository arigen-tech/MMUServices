package com.mmu.services.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmu.services.dto.CaptureInvoices;
import com.mmu.services.dto.MedicalStore;
import com.mmu.services.dto.SourceOfMedicine;
import com.mmu.services.service.MedicineInvoiceService;

@RestController
@CrossOrigin
@RequestMapping("/medicalInvoice")
public class MedicineInvoiceDetailController {
	@Autowired
	private MedicineInvoiceService medicineInvoiceService;
	ObjectMapper objectMapper = new ObjectMapper();
		
	@RequestMapping(value = "/sourceOfMedicine")
	public ResponseEntity<String> getSourceOfMedicine(){
		Map<String,Object> mapResponse =  new HashMap<>();
		String response = null;
		try {
			List<SourceOfMedicine> sourceOfMedicines= medicineInvoiceService.getSourceOfMedicines();
			getResponse(mapResponse, sourceOfMedicines);
			response = objectMapper.writeValueAsString(mapResponse);
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getMedicalStores")
	public ResponseEntity<String> getMedicalStore(){
		Map<String,Object> mapResponse =  new HashMap<>();
		String response = null;
		List<MedicalStore> medicalStore =  medicineInvoiceService.getMedicalStores();
		try {
			getResponse(mapResponse, medicalStore);
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/getMedicalStore/{id}/{districtId}")
	public ResponseEntity<String> getMedicalStore(@PathVariable Long id,@PathVariable Long districtId){
		Map<String,Object> mapResponse =  new HashMap<>();
		String response = null;
		
		List<MedicalStore> medicalStore =  medicineInvoiceService.getMedicalStores(id,districtId);
		try {
			getResponse(mapResponse, medicalStore);
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/captureInvoice", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> saveMedicalInvoice(@RequestBody CaptureInvoices captureInvoice){
		
		String response = null;
		Map<String,Object> mapResponse =  null;
		try {
			 if(captureInvoice.getBatchNo()!=null && !captureInvoice.getBatchNo().isEmpty()) {
				 mapResponse =  medicineInvoiceService.updateCaptureInvoices(captureInvoice);
			 }else {
			 mapResponse = medicineInvoiceService.saveCaptureInvoices(captureInvoice);
			 }
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fetchInvoices1", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> getMedicalInvoice(@RequestBody Map<String,Object> mapData){
		
		String response = null;
		try {
			Map<String,Object> mapResponse = medicineInvoiceService.fetchAllList(mapData);
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fetchInvoices", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> getMedicalInvoiceGroup(@RequestBody Map<String,Object> mapData){
		
		String response = null;
		try {
			Map<String,Object> mapResponse = medicineInvoiceService.fetchAllInvoiceList(mapData);
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editInvoiceList_old", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> editInvoiceList(@RequestBody Map<String,Object> mapData){
		
		String response = null;
		try {
			Map<String,Object> mapResponse = new HashMap<>();
			String Id =  (String) mapData.get("Id");
			CaptureInvoices captureInvoices = medicineInvoiceService.getCaptureInvoices(Long.valueOf(Id));
			mapResponse.put("data", captureInvoices);
			mapResponse.put("msg", "Get Record successfully");
			mapResponse.put("status", 1);
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editInvoiceList", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> editInvoiceListNew(@RequestBody Map<String,Object> mapData){
		
		String response = null;
		try {
			Map<String,Object> mapResponse = new HashMap<>();
			String Id =  (String) mapData.get("Id");
			CaptureInvoices captureInvoices = medicineInvoiceService.getCaptureInvoicesBasedOnBatchNo(Integer.valueOf(Id));
			mapResponse.put("data", captureInvoices);
			mapResponse.put("msg", "Get Record successfully");
			mapResponse.put("status", 1);
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getInvoiceDashboard", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> getInvoiceDashboardData(@RequestBody Map<String,Object> mapData){
		
		String response = null;
		try {
			Map<String,Object> mapResponse = new HashMap<>();
			String Id =  (String) mapData.get("Id");
			Object captureInvoices = medicineInvoiceService.getCaptureInvoices(Long.valueOf(Id));
			mapResponse.put("data", captureInvoices);
			mapResponse.put("msg", "Get Record successfully");
			mapResponse.put("status", 1);
			response = objectMapper.writeValueAsString(mapResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	private void getResponse(Map<String,Object> mapResponse,Collection<?> collection){
		if (collection != null && !collection.isEmpty()) {
			mapResponse.put("data", collection);
			mapResponse.put("count", collection.size());
			mapResponse.put("msg", "Get Record successfully");
			mapResponse.put("status", 1);
		} else {
			mapResponse.put("data", collection);
			mapResponse.put("count", 0);
			mapResponse.put("msg", "No Record Found");
			mapResponse.put("status", 0);
		}	
	}
}
