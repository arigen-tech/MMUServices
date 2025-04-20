package com.mmu.services.service.impl;

import com.mmu.services.dao.AuditDao;
import com.mmu.services.dao.FundHcbDao;
import com.mmu.services.dto.CaptureInvoices;
import com.mmu.services.dto.InvoiceDetail;
import com.mmu.services.entity.*;
import com.mmu.services.service.AuditService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ValidateUtils;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    AuditDao auditDao;
    
    @Autowired
    FundHcbDao fundHcbDao;
    
    public static final Long HEAD_TYPE_ID =1L;

    @Override
    public String captureInspectionChecklistDetails(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                Object id = jsondata.get("inspectionDetailId");
                if (id == null || id.toString().isEmpty()) {
                    JSONObject params = new JSONObject();
                    params.put("PN", "0");
                    params.put("mmuLocation", jsondata.getString("mmuLocation"));
                    params.put("inspectionDate", jsondata.getString("inspectionDate"));

                    List<CaptureInspectionDetails> insDetailList = auditDao.getAllCapturedInspectionChecklist(params).get("capturedInspectionList");
                    if (insDetailList == null || insDetailList.isEmpty()) {
                        this.saveSubmitCapturedInspectionDetails(jsondata, "CREATE", json);
                    } else {
                        json.put("status", 0);
                        json.put("msg", "Can not submit duplicate record for same Inspection Date and MMU Location!");
                    }
                } else {
                    this.saveSubmitCapturedInspectionDetails(jsondata, "UPDATE", json);
                }
            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Creating Inspection details!");
            ex.printStackTrace();
        }

        return json.toString();
    }

    private void saveSubmitCapturedInspectionDetails(JSONObject jsondata, String operatoion, JSONObject json)throws Exception{

        String auditStatus="";
        Long id = null;
        if (operatoion.equals("CREATE")) {
            //MasMMU masMMU = (MasMMU)auditDao.read(MasMMU.class, jsondata.getLong("mmuId"));
        	List<MasCamp>listMas=auditDao.getMasCampFromMMUIdAndDate(jsondata.getLong("mmuId"),jsondata.getString("inspectionDate"));
        	auditStatus=jsondata.getString("auditStatus");
        	CaptureInspectionDetails captureInspectionDetails = new CaptureInspectionDetails();
            //captureInspectionDetails.setCityId(masMMU.getCityId());
        	if(CollectionUtils.isNotEmpty(listMas))
        		captureInspectionDetails.setCityId(listMas.get(0).getCityId());
        	captureInspectionDetails.setMmuLocation(jsondata.getString("mmuLocation"));
            captureInspectionDetails.setMmuId(jsondata.getLong("mmuId"));
            captureInspectionDetails.setVehicleRegNo(jsondata.getString("vehicleRegNo"));
            captureInspectionDetails.setInspectedBy(jsondata.getLong("inspectedBy"));
            captureInspectionDetails.setInspectionTime(jsondata.getString("inspectionTime"));
            captureInspectionDetails.setAuditStatus(jsondata.getString("auditStatus"));
            if(jsondata.has("apmNames"))
                captureInspectionDetails.setApmName(jsondata.getString("apmNames"));
            
            System.out.println("jsondata.has(\"doctorNames\")"+jsondata.has("doctorNames"));
            if(jsondata.has("doctorNames"))
                captureInspectionDetails.setDoctorName(jsondata.getString("doctorNames"));
            
            System.out.println("jsondata.has(\"doctorRegNos\")"+jsondata.has("doctorRegNos"));
            
            
            if(jsondata.has("doctorRegNos"))
                captureInspectionDetails.setDoctorRegNo(jsondata.getString("doctorRegNos"));
            if(jsondata.has("commissionerName"))
                captureInspectionDetails.setCommissionerName(jsondata.getString("commissionerName"));
            if(jsondata.has("nodalOfficerName"))
                captureInspectionDetails.setNodalOfficerName(jsondata.getString("nodalOfficerName"));
            if(jsondata.has("tpaMember1Id") && !jsondata.getString("tpaMember1Id").isEmpty())
                captureInspectionDetails.setTpaMember1Id(jsondata.getLong("tpaMember1Id"));
            if(jsondata.has("tpaMember2Id")&& !jsondata.getString("tpaMember2Id").isEmpty())
                captureInspectionDetails.setTpaMember2Id(jsondata.getLong("tpaMember2Id"));
            //if(jsondata.has("campId"))
               // captureInspectionDetails.setCampId(jsondata.getLong("campId"));

            if (jsondata.getString("inspectionDate") != null && !jsondata.getString("inspectionDate").isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date inspectionDate = formatter.parse(jsondata.getString("inspectionDate"));
                captureInspectionDetails.setInspectionDate(inspectionDate);
            }
            id = auditDao.createRecord(captureInspectionDetails);
        }else {
            id = jsondata.getLong("inspectionDetailId");
            auditStatus=jsondata.getString("auditStatus");
            CaptureInspectionDetails captureInspectionDetails = (CaptureInspectionDetails)auditDao.read(CaptureInspectionDetails.class, id);
            if (jsondata.has("apmNames"))
                captureInspectionDetails.setApmName(jsondata.getString("apmNames"));
            if (jsondata.has("doctorNames"))
                captureInspectionDetails.setDoctorName(jsondata.getString("doctorNames"));
            if (jsondata.has("doctorRegNos"))
                captureInspectionDetails.setDoctorRegNo(jsondata.getString("doctorRegNos"));
            if (jsondata.has("auditStatus"))
                captureInspectionDetails.setAuditStatus(jsondata.getString("auditStatus"));
            if(jsondata.has("commissionerName"))
                captureInspectionDetails.setCommissionerName(jsondata.getString("commissionerName"));
            if(jsondata.has("nodalOfficerName"))
                captureInspectionDetails.setNodalOfficerName(jsondata.getString("nodalOfficerName"));
            if(jsondata.has("tpaMember1Id") && !jsondata.getString("tpaMember1Id").isEmpty())
                captureInspectionDetails.setTpaMember1Id(jsondata.getLong("tpaMember1Id"));
            if(jsondata.has("tpaMember2Id")&& !jsondata.getString("tpaMember2Id").isEmpty())
                captureInspectionDetails.setTpaMember2Id(jsondata.getLong("tpaMember2Id"));

            auditDao.updateRecord(captureInspectionDetails);
        }

        if (id != null) {
            String checklistIds[] = jsondata.getString("checklistIds").split(",");
            String createIncidents[] = jsondata.getString("createIncidents").split(",");
            String remarks[] = jsondata.getString("remarks").split("#");
            String inputValues[] = jsondata.getString("inputValues").split(",");
            String inspectionChecklistIds[] = jsondata.getString("inspectionChecklistIds").split(",");
            String uploadedFiles[] = jsondata.getString("uploadedFiles").split(",");
            String encodedFiles[] = jsondata.getString("encodedFiles").split(",");
            List<Serializable> entities = new ArrayList<>();
            for (int i = 0; i < checklistIds.length; i++) {
                CaptureInspectionChecklist captureInspectionChecklist = null;
                if (operatoion.equals("UPDATE")) {
                    captureInspectionChecklist = (CaptureInspectionChecklist)auditDao.read(CaptureInspectionChecklist.class, Long.parseLong(inspectionChecklistIds[i]));
                } else {
                    captureInspectionChecklist = new CaptureInspectionChecklist();
                    captureInspectionChecklist.setInspectionDetailId(id);
                }

                captureInspectionChecklist.setInspectionChecklistId(Long.parseLong(checklistIds[i]));
                captureInspectionChecklist.setCreateIncident(createIncidents[i]);
                captureInspectionChecklist.setInputValue(inputValues[i]);
                captureInspectionChecklist.setRemarks(remarks[i]);
                
               

                captureInspectionChecklist.setAuditStatus(jsondata.getString("auditStatus"));
                if (!uploadedFiles[i].equals("_"))
                    captureInspectionChecklist.setUploadedFile(uploadedFiles[i]);

                if (!encodedFiles[i].equals("_"))
                    captureInspectionChecklist.setEncodedFile(encodedFiles[i]);

                if (jsondata.getString("inspectionDate") != null && !jsondata.getString("inspectionDate").isEmpty()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    Date inspectionDate = formatter.parse(jsondata.getString("inspectionDate"));
                    captureInspectionChecklist.setIncidentDate(inspectionDate);
                }
                
                if (operatoion.equals("CREATE")) {
                    auditDao.createRecord(captureInspectionChecklist);
                }else {
                    captureInspectionChecklist.setCaptureInspectionChecklistId(Long.parseLong(inspectionChecklistIds[i]));
                    entities.add(captureInspectionChecklist);
                }
            
            }
            if (operatoion.equals("UPDATE")) {
                auditDao.updateMultipleRecords(entities);
            }
            if(auditStatus.equals("S"))
            {	
	            json.put("msg", "Record Saved Successfully!");
	            json.put("status", 1);
            }
            else
            {
            	json.put("msg", "Record Submitted Successfully!");
                json.put("status", 1);
            }
        } else {
            json.put("status", 0);
            json.put("msg", "Error Creating Inspection details!");
        }
    }

    @Override
    public String captureEquipmentChecklistDetails(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                Object id = jsondata.get("equipmentChecklistDetailId");
                if(id == null || id.toString().isEmpty()) {
                    JSONObject params = new JSONObject();
                    params.put("PN", "0");
                    params.put("mmuLocation", jsondata.getString("mmuLocation"));
                    params.put("inspectionDate", jsondata.getString("inspectionDate"));
                    List<CaptureEquipmentChecklistDetails> equipChecklist = auditDao.getAllCapturedEquipmentChecklist(params).get("capturedEquipmentList");

                    if (equipChecklist.isEmpty()) {
                        this.saveSubmitCapturedEquipmentDetails(jsondata, "CREATE", json);
                    } else {
                        json.put("status", 0);
                        json.put("msg", "Can not submit duplicate record for same Inspection Date and MMU Location!");
                    }
                } else {
                    this.saveSubmitCapturedEquipmentDetails(jsondata, "UPDATE", json);
                }
            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Creating Equipment Checklist Details!");
            ex.printStackTrace();
        }

        return json.toString();
    }

    private void saveSubmitCapturedEquipmentDetails(JSONObject jsondata, String operatoion, JSONObject json)throws Exception{
        Long id = null;
        String auditStatus = null;
        if (operatoion.equals("CREATE")) {
            //MasMMU masMMU = (MasMMU)auditDao.read(MasMMU.class, jsondata.getLong("mmuId"));
        	List<MasCamp>listMAsCamp=auditDao.getMasCampFromMMUIdAndDate(jsondata.getLong("mmuId"),jsondata.getString("inspectionDate"));
        	auditStatus=jsondata.getString("auditStatus");
        	CaptureEquipmentChecklistDetails captureEquipmentChecklistDetails = new CaptureEquipmentChecklistDetails();
        	if(CollectionUtils.isNotEmpty(listMAsCamp))
            captureEquipmentChecklistDetails.setCityId(listMAsCamp.get(0).getCityId());
            captureEquipmentChecklistDetails.setMmuLocation(jsondata.getString("mmuLocation"));
            captureEquipmentChecklistDetails.setMmuId(jsondata.getLong("mmuId"));
            captureEquipmentChecklistDetails.setVehicleRegNo(jsondata.getString("vehicleRegNo"));
            captureEquipmentChecklistDetails.setInspectedBy(jsondata.getLong("inspectedBy"));
            captureEquipmentChecklistDetails.setAuditStatus(jsondata.getString("auditStatus"));
            if (jsondata.has("apmNames"))
                captureEquipmentChecklistDetails.setApmName(jsondata.getString("apmNames"));
            if (jsondata.has("doctorNames"))
                captureEquipmentChecklistDetails.setDoctorName(jsondata.getString("doctorNames"));
            if (jsondata.has("doctorRegNos"))
                captureEquipmentChecklistDetails.setDoctorRegNo(jsondata.getString("doctorRegNos"));
            if(jsondata.has("commissionerName"))
                captureEquipmentChecklistDetails.setCommissionerName(jsondata.getString("commissionerName"));
            if(jsondata.has("nodalOfficerName"))
                captureEquipmentChecklistDetails.setNodalOfficerName(jsondata.getString("nodalOfficerName"));
            if(jsondata.has("tpaMember1Id") && !jsondata.getString("tpaMember1Id").isEmpty())
                captureEquipmentChecklistDetails.setTpaMember1Id(jsondata.getLong("tpaMember1Id"));
            if(jsondata.has("tpaMember2Id") && !jsondata.getString("tpaMember2Id").isEmpty())
                captureEquipmentChecklistDetails.setTpaMember2Id(jsondata.getLong("tpaMember2Id"));
            //if(jsondata.has("campId"))
              //  captureEquipmentChecklistDetails.setCampId(jsondata.getLong("campId"));

            if (jsondata.getString("inspectionDate") != null && !jsondata.getString("inspectionDate").isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date inspectionDate = formatter.parse(jsondata.getString("inspectionDate"));
                captureEquipmentChecklistDetails.setInspectionDate(inspectionDate);
            }
            id = auditDao.createRecord(captureEquipmentChecklistDetails);
        } else {
            id = jsondata.getLong("equipmentChecklistDetailId");
            auditStatus=jsondata.getString("auditStatus");
            CaptureEquipmentChecklistDetails captureEquipmentChecklistDetails = (CaptureEquipmentChecklistDetails)auditDao.read(CaptureEquipmentChecklistDetails.class, id);
            if (jsondata.has("apmNames"))
                captureEquipmentChecklistDetails.setApmName(jsondata.getString("apmNames"));
            if (jsondata.has("doctorNames"))
                captureEquipmentChecklistDetails.setDoctorName(jsondata.getString("doctorNames"));
            if (jsondata.has("doctorRegNos"))
                captureEquipmentChecklistDetails.setDoctorRegNo(jsondata.getString("doctorRegNos"));
            if (jsondata.has("auditStatus"))
                captureEquipmentChecklistDetails.setAuditStatus(jsondata.getString("auditStatus"));
            if(jsondata.has("commissionerName"))
                captureEquipmentChecklistDetails.setCommissionerName(jsondata.getString("commissionerName"));
            if(jsondata.has("nodalOfficerName"))
                captureEquipmentChecklistDetails.setNodalOfficerName(jsondata.getString("nodalOfficerName"));
            if(jsondata.has("tpaMember1Id") && !jsondata.getString("tpaMember1Id").isEmpty())
                captureEquipmentChecklistDetails.setTpaMember1Id(jsondata.getLong("tpaMember1Id"));
            if(jsondata.has("tpaMember2Id") && !jsondata.getString("tpaMember2Id").isEmpty())
                captureEquipmentChecklistDetails.setTpaMember2Id(jsondata.getLong("tpaMember2Id"));

            auditDao.updateRecord(captureEquipmentChecklistDetails);
        }

        if (id != null) {
            String checklistIds[] = jsondata.getString("checklistIds").split(",");
            String assignedQuantity[] = jsondata.getString("assignedQuantities").split(",");
            String operationalQuantity[] = jsondata.getString("operationalQuantities").split(",");
            String availableQuantity[] = jsondata.getString("availableQuantities").split(",");
            String penaltyQuantity[] = jsondata.getString("penaltyQuantities").split(",");
            String remarks[] = jsondata.getString("remarks").split("#");
            String equipmentChecklistIds[] = jsondata.getString("equipmentChecklistIds").split(",");
            String uploadedFiles[] = jsondata.getString("uploadedFiles").split(",");
            String encodedFiles[] = jsondata.getString("encodedFiles").split(",");
            List<Serializable> entities = new ArrayList<>();
            for (int i = 0; i < checklistIds.length; i++) {
                CaptureEquipmentChecklist captureEquipmentChecklist = null;
                if (operatoion.equals("UPDATE")) {
                    captureEquipmentChecklist = (CaptureEquipmentChecklist)auditDao.read(CaptureEquipmentChecklist.class, Long.parseLong(equipmentChecklistIds[i]));
                } else {
                    captureEquipmentChecklist = new CaptureEquipmentChecklist();
                    captureEquipmentChecklist.setEquipmentChecklistDetailId(id);
                }

                captureEquipmentChecklist.setEquipmentChecklistId(Long.parseLong(checklistIds[i]));
                captureEquipmentChecklist.setAssignedQuantity(Integer.parseInt(assignedQuantity[i]));
                captureEquipmentChecklist.setOperationalQuantity(Integer.parseInt(operationalQuantity[i]));
                captureEquipmentChecklist.setAvailableQuantity(Integer.parseInt(availableQuantity[i]));
                captureEquipmentChecklist.setAuditStatus(jsondata.getString("auditStatus"));
                captureEquipmentChecklist.setRemarks(remarks[i]);
				/*
				 * if (!remarks[i].equals("_"))
				 * captureEquipmentChecklist.setRemarks(remarks[i]);
				 */

                captureEquipmentChecklist.setPenaltyQuantity(Integer.parseInt(penaltyQuantity[i]));

                if(captureEquipmentChecklist.getPenaltyQuantity() > 0){
                    captureEquipmentChecklist.setCreateIncident("Y");
                }

                if (!uploadedFiles[i].equals("_"))
                    captureEquipmentChecklist.setUploadedFile(uploadedFiles[i]);

                if (!encodedFiles[i].equals("_"))
                    captureEquipmentChecklist.setEncodedFile(encodedFiles[i]);

                if (jsondata.getString("inspectionDate") != null && !jsondata.getString("inspectionDate").isEmpty()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    Date inspectionDate = formatter.parse(jsondata.getString("inspectionDate"));
                    captureEquipmentChecklist.setIncidentDate(inspectionDate);
                }
              
                if (operatoion.equals("CREATE")) {
                    auditDao.createRecord(captureEquipmentChecklist);
                }else {
                    captureEquipmentChecklist.setCaptureEquipmentChecklistId(Long.parseLong(equipmentChecklistIds[i]));
                    entities.add(captureEquipmentChecklist);
                }
            }
            if (operatoion.equals("UPDATE")) {
                auditDao.updateMultipleRecords(entities);
            }
            if(auditStatus.equals("S"))
            {	
	            json.put("msg", "Record Saved Successfully!");
	            json.put("status", 1);
            }
            else
            {
            	json.put("msg", "Record Submitted Successfully!");
                json.put("status", 1);
            }
        } else {
            json.put("status", 0);
            json.put("msg", "Error Creating Equipment details!");
        }
    }

    @Override
    public String getCampLocation(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        List<MasCamp> campList = new ArrayList<MasCamp>();
        List list = new ArrayList();
        if (jsondata != null) {
            Map<String, List<MasCamp>> campDetailList = auditDao.getCampDetail(jsondata);
            MasMMU masMMU = (MasMMU)auditDao.read(MasMMU.class, jsondata.getLong("mmuId"));
            List totalMatches = new ArrayList();
            if (campDetailList.get("campList") != null) {
                campList = campDetailList.get("campList");
                if(campList != null && !campList.isEmpty()){
                    MasCamp masCamp = campList.get(0);
                    String apmDoctorDetail[] = auditDao.getAPMDoctor(masCamp.getCampId(), masMMU.getMmuId());
                    list.add(masCamp.getLocation());

                    if(apmDoctorDetail != null && apmDoctorDetail.length > 0) {
                        json.put("doctorName", apmDoctorDetail[0]);
                        json.put("doctorRegNo", apmDoctorDetail[1]);
                        json.put("apmName", apmDoctorDetail[2]);
                    }
                    json.put("data", list);
                    json.put("vehicleRegNo", masMMU.getMmuNo());
                    json.put("campId", masCamp.getCampId());
                    json.put("count", 1);
                    json.put("msg", "Get Record successfully");
                    json.put("status", 1);
                }
            }

        }
        return json.toString();
    }

    @Override
    public String getAllCapturedEquipmentChecklist(JSONObject jsondata){
        JSONObject json = new JSONObject();
        List<CaptureEquipmentChecklistDetails> equipmentList = new ArrayList<CaptureEquipmentChecklistDetails>();
        List list = new ArrayList();
        if (jsondata != null) {
            Map<String, List<CaptureEquipmentChecklistDetails>> capturedEquipmentChecklist = auditDao.getAllCapturedEquipmentChecklist(jsondata);
            List totalMatches = new ArrayList();
            if (capturedEquipmentChecklist.get("capturedEquipmentList") != null) {
                equipmentList = capturedEquipmentChecklist.get("capturedEquipmentList");
                totalMatches = capturedEquipmentChecklist.get("totalMatches");
                {
                    equipmentList.forEach( equipmentDetail -> {

                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        if (equipmentDetail != null ) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            //MasCity masCity = (MasCity) auditDao.read(MasCity.class, equipmentDetail.getCityId());
                            List<MasCamp>listMas = null;
                            String campDate=formatter.format(equipmentDetail.getInspectionDate());
                            if(equipmentDetail!=null)
                            	 listMas=auditDao.getMasCampFromMMUIdAndDate(equipmentDetail.getMmuId(),campDate);
                            	//masCamp=(MasCamp) auditDao.read(MasCamp.class, equipmentDetail.getCampId());
                            
                             MasMMU masMMU = (MasMMU) auditDao.read(MasMMU.class, equipmentDetail.getMmuId());
                            Users users = (Users) auditDao.read(Users.class, equipmentDetail.getInspectedBy());
                            mapObj.put("equipmentChecklistDetailId", equipmentDetail.getEquipmentChecklistDetailId());
                            mapObj.put("cityId", listMas.get(0).getCityId());
                            mapObj.put("cityName", listMas.get(0).getMasCity().getCityName());
                            mapObj.put("mmuName", masMMU.getMmuName());
                            mapObj.put("mmuId", equipmentDetail.getMmuId());
                            mapObj.put("inspectionDate", formatter.format(equipmentDetail.getInspectionDate()));
                            mapObj.put("vehicleRegNo", equipmentDetail.getVehicleRegNo());
                            mapObj.put("mmuLocation", equipmentDetail.getMmuLocation());
                            mapObj.put("inspectedBy", users.getUserName());
                            mapObj.put("auditStatus", equipmentDetail.getAuditStatus());
                            mapObj.put("apmName", equipmentDetail.getApmName());
                            mapObj.put("doctorName", equipmentDetail.getDoctorName());
                            mapObj.put("doctorRegNo", equipmentDetail.getDoctorRegNo());
                            mapObj.put("commissionerName", equipmentDetail.getCommissionerName());
                            mapObj.put("nodalOfficerName", equipmentDetail.getNodalOfficerName());
                            mapObj.put("tpaMember1Id", equipmentDetail.getTpaMember1Id());
                            mapObj.put("tpaMember2Id", equipmentDetail.getTpaMember2Id());
                            list.add(mapObj);
                        }
                    });

                }

                if (list != null && list.size() > 0) {
                    json.put("data", list);
                    json.put("count", totalMatches.size());
                    json.put("msg", "Get Record successfully");
                    json.put("status", 1);
                } else {
                    json.put("data", list);
                    json.put("count", 0);
                    json.put("msg", "No Record Found");
                    json.put("status", 0);
                }

            }

        }
        return json.toString();
    }

    @Override
    public String getAllCapturedInspectionChecklist(JSONObject jsondata){
        JSONObject json = new JSONObject();
        List<CaptureInspectionDetails> inspectionList = new ArrayList<CaptureInspectionDetails>();
        List list = new ArrayList();
        if (jsondata != null) {
            Map<String, List<CaptureInspectionDetails>> capturedInspectionChecklist = auditDao.getAllCapturedInspectionChecklist(jsondata);
            List totalMatches = new ArrayList();
            if (capturedInspectionChecklist.get("capturedInspectionList") != null) {
                inspectionList = capturedInspectionChecklist.get("capturedInspectionList");
                totalMatches = capturedInspectionChecklist.get("totalMatches");
                {
                    inspectionList.forEach( inspectionDetail -> {

                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        if (inspectionDetail != null ) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            //MasCity masCity = (MasCity) auditDao.read(MasCity.class, inspectionDetail.getCityId());
                             
                            List<MasCamp>listMas = null;
                            String campDate=formatter.format(inspectionDetail.getInspectionDate());
                            if(inspectionDetail!=null)
                            	 listMas=auditDao.getMasCampFromMMUIdAndDate(inspectionDetail.getMmuId(),campDate);
                            
                            MasMMU masMMU = (MasMMU) auditDao.read(MasMMU.class, inspectionDetail.getMmuId());
                            Users users = (Users) auditDao.read(Users.class, inspectionDetail.getInspectedBy());

                            mapObj.put("inspectionDetailId", inspectionDetail.getInspectionDetailId());
                            mapObj.put("cityName", listMas.get(0).getMasCity().getCityName());
                            mapObj.put("mmuName", masMMU.getMmuName());
                            mapObj.put("mmuId", inspectionDetail.getMmuId());
                            mapObj.put("auditStatus", inspectionDetail.getAuditStatus());
                            mapObj.put("inspectionDate", formatter.format(inspectionDetail.getInspectionDate()));
                            mapObj.put("vehicleRegNo", inspectionDetail.getVehicleRegNo());
                            mapObj.put("mmuLocation", inspectionDetail.getMmuLocation());
                            mapObj.put("inspectionTime", inspectionDetail.getInspectionTime());
                            mapObj.put("inspectedBy", users.getUserName());
                            mapObj.put("apmName", inspectionDetail.getApmName());
                            mapObj.put("doctorName", inspectionDetail.getDoctorName());
                            mapObj.put("doctorRegNo", inspectionDetail.getDoctorRegNo());
                            mapObj.put("commissionerName", inspectionDetail.getCommissionerName());
                            mapObj.put("nodalOfficerName", inspectionDetail.getNodalOfficerName());
                            mapObj.put("tpaMember1Id", inspectionDetail.getTpaMember1Id());
                            mapObj.put("tpaMember2Id", inspectionDetail.getTpaMember2Id());
                            list.add(mapObj);
                        }
                    });

                }

                if (list != null && list.size() > 0) {
                    json.put("data", list);
                    json.put("count", totalMatches.size());
                    json.put("msg", "Get Record successfully");
                    json.put("status", 1);
                } else {
                    json.put("data", list);
                    json.put("count", 0);
                    json.put("msg", "No Record Found");
                    json.put("status", 0);
                }

            }

        }
        return json.toString();
    }

    public String getCapturedInspectionChecklist(JSONObject jsondata){
        JSONObject json = new JSONObject();
        Map<String, Object> data = new HashMap<>();
        List<CaptureInspectionChecklist> inspectionChecklist = new ArrayList<CaptureInspectionChecklist>();
        List list = new ArrayList();
        if (jsondata != null) {
            CaptureInspectionDetails captureInspectionDetails = (CaptureInspectionDetails)auditDao.read(CaptureInspectionDetails.class, jsondata.getLong("inspectionDetailId"));
            if (captureInspectionDetails != null){
                HashMap<String, Object> mapObj = new HashMap<String, Object>();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
               // MasCity masCity = (MasCity) auditDao.read(MasCity.class, captureInspectionDetails.getCityId());
                List<MasCamp>listMas = null;
                String campDate=formatter.format(captureInspectionDetails.getInspectionDate());
                if(captureInspectionDetails!=null)
                	 listMas=auditDao.getMasCampFromMMUIdAndDate(captureInspectionDetails.getMmuId(),campDate);
                
                MasMMU masMMU = (MasMMU) auditDao.read(MasMMU.class, captureInspectionDetails.getMmuId());
               
                
                
                Users users = (Users) auditDao.read(Users.class, captureInspectionDetails.getInspectedBy());

                mapObj.put("inspectionDetailId", captureInspectionDetails.getInspectionDetailId());
                mapObj.put("cityName", listMas.get(0).getMasCity().getCityName());
                mapObj.put("mmuName", masMMU.getMmuName());
                mapObj.put("mmuId", captureInspectionDetails.getMmuId());
                mapObj.put("auditStatus", captureInspectionDetails.getAuditStatus());
                mapObj.put("inspectionDate", formatter.format(captureInspectionDetails.getInspectionDate()));
                mapObj.put("vehicleRegNo", captureInspectionDetails.getVehicleRegNo());
                mapObj.put("mmuLocation", captureInspectionDetails.getMmuLocation());
                mapObj.put("inspectionTime", captureInspectionDetails.getInspectionTime());
                mapObj.put("inspectedBy", users.getUserName());
                mapObj.put("apmName", captureInspectionDetails.getApmName());
                mapObj.put("doctorName", captureInspectionDetails.getDoctorName());
                mapObj.put("doctorRegNo", captureInspectionDetails.getDoctorRegNo());
                mapObj.put("commissionerName", captureInspectionDetails.getCommissionerName());
                mapObj.put("nodalOfficerName", captureInspectionDetails.getNodalOfficerName());
                mapObj.put("finalAuditRemarks", captureInspectionDetails.getFinalAuditRemarks());
                mapObj.put("srAuditorRemarks", captureInspectionDetails.getSrAuditorRemarks());
                mapObj.put("auditorId", captureInspectionDetails.getAuditorId());
                mapObj.put("srAuditorId", captureInspectionDetails.getSrAuditorId());
                mapObj.put("tpaMember1Id", captureInspectionDetails.getTpaMember1Id());
                mapObj.put("tpaMember2Id", captureInspectionDetails.getTpaMember2Id());
                data.put("inspectionDetailData", mapObj);
            }
            Map<String, List<CaptureInspectionChecklist>> capturedInspectionChecklist = auditDao.getCapturedInspectionChecklistByDetailId(jsondata);
            List totalMatches = new ArrayList();
            if (capturedInspectionChecklist.get("capturedInspectionChecklist") != null) {
                inspectionChecklist = capturedInspectionChecklist.get("capturedInspectionChecklist");
                totalMatches = capturedInspectionChecklist.get("totalMatches");
              //  List<Long> ids = inspectionChecklist.stream().map(CaptureInspectionChecklist::getInspectionChecklistId).collect(Collectors.toList());
               // Map<Long, MasInspectionChecklist> insMap = auditDao.getInspectionMaster(ids);
                inspectionChecklist.forEach( inspectionChecklistObj -> {
                    HashMap<String, Object> mapObj = new HashMap<String, Object>();
                    if (inspectionChecklistObj != null ) {
                        MasInspectionChecklist masInspectionChecklist = inspectionChecklistObj.getMasInspectionChecklist();//insMap.get(inspectionChecklistObj.getInspectionChecklistId());

                        mapObj.put("inspectionDetailId", inspectionChecklistObj.getInspectionDetailId());
                        mapObj.put("inspectionChecklistId", inspectionChecklistObj.getInspectionChecklistId());
                        mapObj.put("captureInspectionChecklistId", inspectionChecklistObj.getCaptureInspectionChecklistId());
                        mapObj.put("checklistName", masInspectionChecklist.getChecklistName());
                        mapObj.put("auditStatus", inspectionChecklistObj.getAuditStatus());
                        mapObj.put("sequenceNo", masInspectionChecklist.getSequenceNo());
                        mapObj.put("subsequence", masInspectionChecklist.getSubsequence());
                        mapObj.put("typeOfInput", masInspectionChecklist.getTypeOfInput());
                        mapObj.put("inputValue", inspectionChecklistObj.getInputValue());
                        mapObj.put("createIncident", inspectionChecklistObj.getCreateIncident());
                        mapObj.put("remarks", inspectionChecklistObj.getRemarks());
                        mapObj.put("uploadedFile", inspectionChecklistObj.getUploadedFile());
                        list.add(mapObj);
                    }
                });

                data.put("inspectionChecklistData", list);
                List dataList = new ArrayList();
                dataList.add(data);

                if (list != null && list.size() > 0) {
                    json.put("data", dataList);
                    json.put("count", 1);
                    json.put("msg", "Get Record successfully");
                    json.put("status", 1);
                } else {
                    json.put("data", list);
                    json.put("count", 0);
                    json.put("msg", "No Record Found");
                    json.put("status", 0);
                }

            }

        }
        return json.toString();
    }

    @Override
    public String addInspectionChecklistValidationHistory(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                InspectionChecklistValidationHistory inspectionChecklistValidationHistory = new InspectionChecklistValidationHistory();
                inspectionChecklistValidationHistory.setInspectionDetailId(jsondata.getLong("inspectionDetailId"));
                inspectionChecklistValidationHistory.setCaptureInspectionChecklistId(jsondata.getLong("captureInspectionChecklistId"));
                inspectionChecklistValidationHistory.setInspectionChecklistId(jsondata.getLong("inspectionChecklistId"));
                inspectionChecklistValidationHistory.setAuditStatus(jsondata.getString("auditStatus"));
                inspectionChecklistValidationHistory.setQueriedBy(jsondata.getLong("queriedBy"));
                inspectionChecklistValidationHistory.setQueryDate(formatter.parse(jsondata.getString("queryDate")));
                inspectionChecklistValidationHistory.setAuditorRemarks(jsondata.getString("auditorsRemarks"));
                inspectionChecklistValidationHistory.setIsEvidenceFileRequired(jsondata.getString("isEvidenceFileRequired"));
                Long id = auditDao.createRecord(inspectionChecklistValidationHistory);

                // Update Inspection Checklist Status
                this.updateCapturedInspectionChecklistStatus(inspectionChecklistValidationHistory.getCaptureInspectionChecklistId());

                // Update Inspection Checklist Details Table Status
                this.updateCapturedInspectionDetailStatus(jsondata.getLong("inspectionDetailId"), null, null, null, null);
                if(id != null) {
                    json.put("msg", "Record Added Successfully!");
                    json.put("status", 1);
                }else {
                    json.put("status", 0);
                    json.put("msg", "Error Creating Inspection Checklist Validation History!");
                }
            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Creating Inspection Checklist Validation History!");
            ex.printStackTrace();
        }

        return json.toString();
    }

    public String getAllInspectionChecklistValidationHistory(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<InspectionChecklistValidationHistory> inspectionChecklistHistory = new ArrayList<InspectionChecklistValidationHistory>();
            List list = new ArrayList();
            if (jsondata != null) {
                Map<String, List<InspectionChecklistValidationHistory>> capturedInspectionChecklist = auditDao.getAllInspectionChecklistValidationHistory(jsondata);
                List totalMatches = new ArrayList();
                if (capturedInspectionChecklist.get("inspectionHistoryList") != null) {
                    inspectionChecklistHistory = capturedInspectionChecklist.get("inspectionHistoryList");
                    totalMatches = capturedInspectionChecklist.get("totalMatches");
                    inspectionChecklistHistory.forEach(history -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String respondedBy = "";
                        String queriedBy = "";
                        if (history != null) {
                            if (history.getRespondedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, history.getRespondedBy());
                                if (user != null)
                                    respondedBy = user.getUserName();
                            }
                            if (history.getQueriedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, history.getQueriedBy());
                                if (user != null)
                                    queriedBy = user.getUserName();
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String queryDate = "";
                            String responseDate = "";
                            if (history.getQueryDate() != null)
                                queryDate = formatter.format(history.getQueryDate());

                            if (history.getResponseDate() != null)
                                responseDate = formatter.format(history.getResponseDate());

                            mapObj.put("historyId", history.getHistoryId());
                            mapObj.put("inspectionDetailId", history.getInspectionDetailId());
                            mapObj.put("inspectionChecklistId", history.getInspectionChecklistId());
                            mapObj.put("captureInspectionChecklistId", history.getCaptureInspectionChecklistId());
                            mapObj.put("auditorRemarks", history.getAuditorRemarks());
                            mapObj.put("fileName", history.getFileName());
                            mapObj.put("isEvidenceFileRequired", history.getIsEvidenceFileRequired());
                            mapObj.put("respondedBy", respondedBy);
                            mapObj.put("responseDate", responseDate);
                            mapObj.put("auditStatus", history.getAuditStatus());
                            mapObj.put("queryDate", queryDate);
                            mapObj.put("response", history.getResponse());
                            mapObj.put("queriedBy", queriedBy);
                            list.add(mapObj);
                        }
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", 1);
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String updateInspectionChecklistValidationHistory(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                if(jsondata.getLong("historyId") > 0){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    InspectionChecklistValidationHistory inspectionChecklistValidationHistory = (InspectionChecklistValidationHistory)auditDao.read(InspectionChecklistValidationHistory.class, jsondata.getLong("historyId"));
                    if(jsondata.getString("auditStatus").equals("Q")) {
                        inspectionChecklistValidationHistory.setQueriedBy(jsondata.getLong("queriedBy"));
                        inspectionChecklistValidationHistory.setQueryDate(formatter.parse(jsondata.getString("queryDate")));
                        inspectionChecklistValidationHistory.setAuditorRemarks(jsondata.getString("auditorsRemarks"));
                        inspectionChecklistValidationHistory.setIsEvidenceFileRequired(jsondata.getString("isEvidenceFileRequired"));
                    } else if (jsondata.getString("auditStatus").equals("R")){
                        inspectionChecklistValidationHistory.setResponse(jsondata.getString("response"));
                        inspectionChecklistValidationHistory.setRespondedBy(jsondata.getLong("respondedBy"));
                        inspectionChecklistValidationHistory.setResponseDate(formatter.parse(jsondata.getString("responseDate")));
                        inspectionChecklistValidationHistory.setAuditStatus(jsondata.getString("auditStatus"));
                        inspectionChecklistValidationHistory.setFileName(jsondata.getString("fileName"));
                    }

                    // Update Inspection History Table Status
                    Integer id = auditDao.updateRecord(inspectionChecklistValidationHistory);

                    // Update Inspection Checklist Status
                    this.updateCapturedInspectionChecklistStatus(inspectionChecklistValidationHistory.getCaptureInspectionChecklistId());

                    // Update Inspection Checklist Details Table Status
                    this.updateCapturedInspectionDetailStatus(inspectionChecklistValidationHistory.getInspectionDetailId(), null, null, null, null);

                    if(id != null && id == 0) {
                        json.put("msg", "Record Updated Successfully!");
                        json.put("status", 1);
                    }else {
                        json.put("status", 0);
                        json.put("msg", "Error Creating Inspection Checklist Validation History!");
                    }
                }else {
                    json.put("status", 0);
                    json.put("msg", "Error Updating Record: ID column value did not provided!");
                }

            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Creating Inspection Checklist Validation History!");
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String updateInspectionChecklistAuditStatus(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                if(jsondata.has("captureChecklistIds")){
                    JSONArray ids = jsondata.getJSONArray("captureChecklistIds");
                    JSONArray createIncidents = jsondata.getJSONArray("createIncidents");
                    JSONArray auditStatus = jsondata.getJSONArray("auditStatus");
                    for (int i=0;i<ids.length();i++){
                        CaptureInspectionChecklist captureInspectionChecklist = (CaptureInspectionChecklist)auditDao.read(CaptureInspectionChecklist.class, ids.getLong(i));
                        if(auditStatus.getString(i).equals("F")) {
                            captureInspectionChecklist.setAuditStatus(auditStatus.getString(i));
                            captureInspectionChecklist.setCreateIncident(createIncidents.getString(i));
                        } else if (auditStatus.getString(i).equals("V"))
                            captureInspectionChecklist.setAuditStatus(auditStatus.getString(i));

                        auditDao.updateRecord(captureInspectionChecklist);
                    }
                    String finalRemarks = null;
                    String srAuditorRemarks = null;
                    Long auditorId = null;
                    Long srAuditorId = null;
                    if (jsondata.has("finalRemarks"))
                        finalRemarks = jsondata.getString("finalRemarks");

                    if (jsondata.has("srAuditorFinalRemarks"))
                        srAuditorRemarks = jsondata.getString("srAuditorFinalRemarks");

                    if (jsondata.has("auditorId"))
                        auditorId = jsondata.getLong("auditorId");

                    if (jsondata.has("srAuditorId"))
                        srAuditorId = jsondata.getLong("srAuditorId");

                    this.updateCapturedInspectionDetailStatus(jsondata.getLong("inspectionDetailId"), finalRemarks, srAuditorRemarks, auditorId, srAuditorId);
                    json.put("msg", "Record Updated Successfully!");
                    json.put("status", 1);
                }else {
                    json.put("status", 0);
                    json.put("msg", "Error Updating Record: ID column value did not provided!");
                }

            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Creating Inspection Checklist Validation History!");
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String getCapturedEquipmentChecklist(JSONObject jsondata){
        JSONObject json = new JSONObject();
        Map<String, Object> data = new HashMap<>();
        List<CaptureEquipmentChecklist> equipmentChecklist = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        if (jsondata != null) {
            CaptureEquipmentChecklistDetails captureEquipmentChecklistDetails = (CaptureEquipmentChecklistDetails)auditDao.read(CaptureEquipmentChecklistDetails.class, jsondata.getLong("equipmentChecklistDetailId"));
            if (captureEquipmentChecklistDetails != null){
                HashMap<String, Object> mapObj = new HashMap<String, Object>();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                MasCity masCity = (MasCity) auditDao.read(MasCity.class, captureEquipmentChecklistDetails.getCityId());
                MasMMU masMMU = (MasMMU) auditDao.read(MasMMU.class, captureEquipmentChecklistDetails.getMmuId());
                Users users = (Users) auditDao.read(Users.class, captureEquipmentChecklistDetails.getInspectedBy());

                mapObj.put("equipmentDetailId", captureEquipmentChecklistDetails.getEquipmentChecklistDetailId());
                mapObj.put("cityName", masCity.getCityName());
                mapObj.put("mmuName", masMMU.getMmuName());
                mapObj.put("mmuId", captureEquipmentChecklistDetails.getMmuId());
                mapObj.put("auditStatus", captureEquipmentChecklistDetails.getAuditStatus());
                mapObj.put("inspectionDate", formatter.format(captureEquipmentChecklistDetails.getInspectionDate()));
                mapObj.put("vehicleRegNo", captureEquipmentChecklistDetails.getVehicleRegNo());
                mapObj.put("mmuLocation", captureEquipmentChecklistDetails.getMmuLocation());
                mapObj.put("inspectedBy", users.getUserName());
                mapObj.put("apmName", captureEquipmentChecklistDetails.getApmName());
                mapObj.put("doctorName", captureEquipmentChecklistDetails.getDoctorName());
                mapObj.put("doctorRegNo", captureEquipmentChecklistDetails.getDoctorRegNo());
                mapObj.put("commissionerName", captureEquipmentChecklistDetails.getCommissionerName());
                mapObj.put("nodalOfficerName", captureEquipmentChecklistDetails.getNodalOfficerName());
                mapObj.put("finalAuditRemarks", captureEquipmentChecklistDetails.getFinalAuditRemarks());
                mapObj.put("srAuditorRemarks", captureEquipmentChecklistDetails.getSrAuditorRemarks());
                mapObj.put("auditorId", captureEquipmentChecklistDetails.getAuditorId());
                mapObj.put("srAuditorId", captureEquipmentChecklistDetails.getSrAuditorId());
                mapObj.put("tpaMember1Id", captureEquipmentChecklistDetails.getTpaMember1Id());
                mapObj.put("tpaMember2Id", captureEquipmentChecklistDetails.getTpaMember2Id());
                data.put("equipmentDetailData", mapObj);
            }
            Map<String, List<CaptureEquipmentChecklist>> capturedEquipmentChecklist = auditDao.getCapturedEquipmentChecklistByDetailId(jsondata);
            List totalMatches = new ArrayList();
            if (capturedEquipmentChecklist.get("capturedEquipmentChecklists") != null) {
                equipmentChecklist = capturedEquipmentChecklist.get("capturedEquipmentChecklists");
                totalMatches = capturedEquipmentChecklist.get("totalMatches");
                List<Long> equipIds = equipmentChecklist.stream().map(CaptureEquipmentChecklist::getEquipmentChecklistId).collect(Collectors.toList());
                Map<Long, MasEquipmentChecklist> equipmentList = auditDao.getEquipmentMaster(equipIds);
                equipmentChecklist.forEach( equipmentChecklistObj -> {
                    HashMap<String, Object> mapObj = new HashMap<String, Object>();
                    if (equipmentChecklistObj != null ) {
                        MasEquipmentChecklist masEquipmentChecklist = equipmentList.get(equipmentChecklistObj.getEquipmentChecklistId());
                        mapObj.put("equipmentDetailId", equipmentChecklistObj.getEquipmentChecklistDetailId());
                        mapObj.put("equipmentChecklistId", equipmentChecklistObj.getEquipmentChecklistId());
                        mapObj.put("captureEquipmentChecklistId", equipmentChecklistObj.getCaptureEquipmentChecklistId());
                        mapObj.put("auditStatus", equipmentChecklistObj.getAuditStatus());
                        mapObj.put("remarks", equipmentChecklistObj.getRemarks());
                        mapObj.put("assignedQuantity", equipmentChecklistObj.getAssignedQuantity());
                        mapObj.put("operationalQuantity", equipmentChecklistObj.getOperationalQuantity());
                        mapObj.put("availableQuantity", equipmentChecklistObj.getAvailableQuantity());
                        mapObj.put("instrumentName", masEquipmentChecklist.getInstrumentName());
                        mapObj.put("penaltyId", masEquipmentChecklist.getPenaltyId());
                        mapObj.put("quantity", masEquipmentChecklist.getQuantity());
                        mapObj.put("instrumentCode", masEquipmentChecklist.getInstrumentCode());
                        mapObj.put("penaltyQuantity", equipmentChecklistObj.getPenaltyQuantity());
                        mapObj.put("createIncident", equipmentChecklistObj.getCreateIncident());
                        mapObj.put("uploadedFile", equipmentChecklistObj.getUploadedFile());
                        list.add(mapObj);
                    }
                });
                list.sort(Comparator.comparing(m -> m.get("instrumentName").toString(), Comparator.nullsLast(Comparator.naturalOrder())));

                data.put("equipmentChecklistData", list);
                List dataList = new ArrayList();
                dataList.add(data);

                if (list != null && list.size() > 0) {
                    json.put("data", dataList);
                    json.put("count", 1);
                    json.put("msg", "Get Record successfully");
                    json.put("status", 1);
                } else {
                    json.put("data", list);
                    json.put("count", 0);
                    json.put("msg", "No Record Found");
                    json.put("status", 0);
                }

            }

        }
        return json.toString();
    }

    @Override
    public String addEquipmentChecklistValidationHistory(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                EquipmentChecklistValidationHistory equipmentChecklistValidationHistory = new EquipmentChecklistValidationHistory();
                equipmentChecklistValidationHistory.setEquipmentDetailId(jsondata.getLong("equipmentDetailId"));
                equipmentChecklistValidationHistory.setCaptureEquipmentChecklistId(jsondata.getLong("captureEquipmentChecklistId"));
                equipmentChecklistValidationHistory.setEquipmentChecklistId(jsondata.getLong("equipmentChecklistId"));
                equipmentChecklistValidationHistory.setAuditStatus(jsondata.getString("auditStatus"));
                equipmentChecklistValidationHistory.setQueriedBy(jsondata.getLong("queriedBy"));
                equipmentChecklistValidationHistory.setQueryDate(formatter.parse(jsondata.getString("queryDate")));
                equipmentChecklistValidationHistory.setAuditorRemarks(jsondata.getString("auditorsRemarks"));
                equipmentChecklistValidationHistory.setIsEvidenceFileRequired(jsondata.getString("isEvidenceFileRequired"));
                equipmentChecklistValidationHistory.setPenaltyQuantity(jsondata.getInt("penaltyQuantity"));
                equipmentChecklistValidationHistory.setCreateIncident(jsondata.getString("createIncident"));
                Long id = auditDao.createRecord(equipmentChecklistValidationHistory);

                CaptureEquipmentChecklist captureEquipmentChecklist = (CaptureEquipmentChecklist)auditDao.read(CaptureEquipmentChecklist.class, equipmentChecklistValidationHistory.getCaptureEquipmentChecklistId());
                captureEquipmentChecklist.setPenaltyQuantity(jsondata.getInt("penaltyQuantity"));
                captureEquipmentChecklist.setCreateIncident(jsondata.getString("createIncident"));
                auditDao.updateRecord(captureEquipmentChecklist);

                // Update Equipment Captured Checklist Table Status
                this.updateCapturedEquipmentChecklistStatus(equipmentChecklistValidationHistory.getCaptureEquipmentChecklistId());

                // Update Equipment Captured Checklist Details Table Status
                this.updateCapturedEquipmentDetailStatus(equipmentChecklistValidationHistory.getEquipmentDetailId(), null, null, null, null);
                if(id != null) {
                    json.put("msg", "Record Added Successfully!");
                    json.put("status", 1);
                }else {
                    json.put("status", 0);
                    json.put("msg", "Error Creating Equipment Checklist Validation History!");
                }
            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Creating Equipment Checklist Validation History!");
            ex.printStackTrace();
        }

        return json.toString();
    }

    @Override
    public String getAllEquipmentChecklistValidationHistory(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<EquipmentChecklistValidationHistory> equipmentChecklistHistory = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null) {
                Map<String, List<EquipmentChecklistValidationHistory>> capturedEquipmentChecklist = auditDao.getAllEquipmentChecklistValidationHistory(jsondata);
                List totalMatches = new ArrayList();
                if (capturedEquipmentChecklist.get("equipmentHistoryList") != null) {
                    equipmentChecklistHistory = capturedEquipmentChecklist.get("equipmentHistoryList");
                    totalMatches = capturedEquipmentChecklist.get("totalMatches");
                    equipmentChecklistHistory.forEach(history -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String respondedBy = "";
                        String queriedBy = "";
                        if (history != null) {
                            if (history.getRespondedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, history.getRespondedBy());
                                if (user != null)
                                    respondedBy = user.getUserName();
                            }
                            if (history.getQueriedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, history.getQueriedBy());
                                if (user != null)
                                    queriedBy = user.getUserName();
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String queryDate = "";
                            String responseDate = "";
                            if (history.getQueryDate() != null)
                                queryDate = formatter.format(history.getQueryDate());

                            if (history.getResponseDate() != null)
                                responseDate = formatter.format(history.getResponseDate());

                            mapObj.put("historyId", history.getHistoryId());
                            mapObj.put("equipmentDetailId", history.getEquipmentDetailId());
                            mapObj.put("equipmentChecklistId", history.getEquipmentChecklistId());
                            mapObj.put("captureEquipmentChecklistId", history.getCaptureEquipmentChecklistId());
                            mapObj.put("auditorRemarks", history.getAuditorRemarks());
                            mapObj.put("fileName", history.getFileName());
                            mapObj.put("isEvidenceFileRequired", history.getIsEvidenceFileRequired());
                            mapObj.put("respondedBy", respondedBy);
                            mapObj.put("responseDate", responseDate);
                            mapObj.put("auditStatus", history.getAuditStatus());
                            mapObj.put("queryDate", queryDate);
                            mapObj.put("response", history.getResponse());
                            mapObj.put("queriedBy", queriedBy);
                            mapObj.put("penaltyQuantity", history.getPenaltyQuantity());
                            mapObj.put("createIncident", history.getCreateIncident());
                            list.add(mapObj);
                        }
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", 1);
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public String updateEquipmentChecklistValidationHistory(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                if(jsondata.getLong("historyId") > 0){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    EquipmentChecklistValidationHistory equipmentChecklistValidationHistory = (EquipmentChecklistValidationHistory)auditDao.read(EquipmentChecklistValidationHistory.class, jsondata.getLong("historyId"));
                    CaptureEquipmentChecklist captureEquipmentChecklist = (CaptureEquipmentChecklist)auditDao.read(CaptureEquipmentChecklist.class, equipmentChecklistValidationHistory.getCaptureEquipmentChecklistId());
                    if(jsondata.getString("auditStatus").equals("Q")) {
                        equipmentChecklistValidationHistory.setQueriedBy(jsondata.getLong("queriedBy"));
                        equipmentChecklistValidationHistory.setQueryDate(formatter.parse(jsondata.getString("queryDate")));
                        equipmentChecklistValidationHistory.setAuditorRemarks(jsondata.getString("auditorsRemarks"));
                        equipmentChecklistValidationHistory.setIsEvidenceFileRequired(jsondata.getString("isEvidenceFileRequired"));
                        equipmentChecklistValidationHistory.setPenaltyQuantity(jsondata.getInt("penaltyQuantity"));
                        equipmentChecklistValidationHistory.setCreateIncident(jsondata.getString("createIncident"));

                        int quantity = 0;
                        if(captureEquipmentChecklist.getPenaltyQuantity() != null){
                            quantity = captureEquipmentChecklist.getPenaltyQuantity()+jsondata.getInt("penaltyQuantity");
                        }
                        captureEquipmentChecklist.setPenaltyQuantity(quantity);
                        captureEquipmentChecklist.setCreateIncident(jsondata.getString("createIncident"));
                        auditDao.updateRecord(captureEquipmentChecklist);
                    } else if (jsondata.getString("auditStatus").equals("R")){
                        equipmentChecklistValidationHistory.setResponse(jsondata.getString("response"));
                        equipmentChecklistValidationHistory.setRespondedBy(jsondata.getLong("respondedBy"));
                        equipmentChecklistValidationHistory.setResponseDate(formatter.parse(jsondata.getString("responseDate")));
                        equipmentChecklistValidationHistory.setAuditStatus(jsondata.getString("auditStatus"));
                        equipmentChecklistValidationHistory.setFileName(jsondata.getString("fileName"));
                    }

                    // Update Equipment History Table STatus
                    Integer id = auditDao.updateRecord(equipmentChecklistValidationHistory);

                    // Update Equipment Captured Checklist Table Status
                    this.updateCapturedEquipmentChecklistStatus(equipmentChecklistValidationHistory.getCaptureEquipmentChecklistId());

                    // Update Equipment Captured Checklist Details Table Status
                    this.updateCapturedEquipmentDetailStatus(equipmentChecklistValidationHistory.getEquipmentDetailId(), null, null, null, null);

                    if(id != null && id == 0) {
                        json.put("msg", "Record Updated Successfully!");
                        json.put("status", 1);
                    }else {
                        json.put("status", 0);
                        json.put("msg", "Error Creating Equipment Checklist Validation History!");
                    }
                }else {
                    json.put("status", 0);
                    json.put("msg", "Error Updating Record: ID column value did not provided!");
                }

            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Creating Equipment Checklist Validation History!");
            ex.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public String updateEquipmentChecklistAuditStatus(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                if(jsondata.has("captureChecklistIds")){
                    JSONArray ids = jsondata.getJSONArray("captureChecklistIds");
                    for (int i=0;i<ids.length();i++){
                        CaptureEquipmentChecklist captureEquipmentChecklist = (CaptureEquipmentChecklist)auditDao.read(CaptureEquipmentChecklist.class, ids.getLong(i));
                        captureEquipmentChecklist.setAuditStatus(jsondata.getString("auditStatus"));
                        auditDao.updateRecord(captureEquipmentChecklist);
                    }
                    String finalRemarks = null;
                    String srAuditorFinalRemarks = null;
                    Long srAuditorId = null;
                    Long auditorId = null;
                    if (jsondata.has("finalRemarks")) {
                        finalRemarks = jsondata.getString("finalRemarks");
                        auditorId = jsondata.getLong("auditorId");
                    }

                    if (jsondata.has("srAuditorFinalRemarks")) {
                        srAuditorFinalRemarks = jsondata.getString("srAuditorFinalRemarks");
                        srAuditorId = jsondata.getLong("srAuditorId");
                    }

                    this.updateCapturedEquipmentDetailStatus(jsondata.getLong("equipmentChecklistDetailId"), finalRemarks, srAuditorFinalRemarks, auditorId, srAuditorId);
                    json.put("msg", "Record Updated Successfully!");
                    json.put("status", 1);
                }else {
                    json.put("status", 0);
                    json.put("msg", "Error Updating Record: ID column value did not provided!");
                }

            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Updating Equipment Checklist Status!");
            ex.printStackTrace();
        }
        return json.toString();
    }

    private void updateCapturedEquipmentDetailStatus(Long detailId, String finalRemarks, String srAuditorFinalRemarks, Long auditorId, Long srAuditorId){
        if(detailId != null && detailId > 0){
            CaptureEquipmentChecklistDetails captureEquipmentChecklistDetails = (CaptureEquipmentChecklistDetails)auditDao.read(CaptureEquipmentChecklistDetails.class, detailId);
            if(finalRemarks != null)
                captureEquipmentChecklistDetails.setFinalAuditRemarks(finalRemarks);

            if(srAuditorFinalRemarks != null)
                captureEquipmentChecklistDetails.setSrAuditorRemarks(srAuditorFinalRemarks);

            if(auditorId != null && auditorId > 0)
                captureEquipmentChecklistDetails.setAuditorId(auditorId);

            if(srAuditorId != null && srAuditorId > 0)
                captureEquipmentChecklistDetails.setSrAuditorId(srAuditorId);

            JSONObject jsondata = new JSONObject();
            jsondata.put("PN", 0);
            jsondata.put("equipmentChecklistDetailId", detailId);
            List<CaptureEquipmentChecklist> captureEquipmentChecklists = auditDao.getCapturedEquipmentChecklistByDetailId(jsondata).get("capturedEquipmentChecklists");
            if(captureEquipmentChecklists != null && !captureEquipmentChecklists.isEmpty()){
                Map<String, Long> statusCount = captureEquipmentChecklists
                        .stream()
                        .collect(Collectors.groupingBy(CaptureEquipmentChecklist::getAuditStatus, Collectors.counting()));

                if((statusCount.containsKey("Q") && statusCount.get("Q") == captureEquipmentChecklists.size())
                || statusCount.containsKey("Q") && statusCount.containsKey("P") && (statusCount.get("Q")+statusCount.get("P")) == captureEquipmentChecklists.size())
                    captureEquipmentChecklistDetails.setAuditStatus("Q");

                if(statusCount.containsKey("R") && statusCount.get("R") > 0 && statusCount.get("R") < captureEquipmentChecklists.size())
                    captureEquipmentChecklistDetails.setAuditStatus("T");

                if((statusCount.containsKey("R") && statusCount.get("R") > 0 && statusCount.get("R") == captureEquipmentChecklists.size())
                || (statusCount.containsKey("R") && statusCount.get("R") > 0 && !statusCount.containsKey("T") && !statusCount.containsKey("Q")))
                    captureEquipmentChecklistDetails.setAuditStatus("R");

                if(statusCount.containsKey("V") && statusCount.get("V") > 0 && statusCount.get("V") == captureEquipmentChecklists.size())
                    captureEquipmentChecklistDetails.setAuditStatus("V");

                if(statusCount.containsKey("F") && statusCount.get("F") > 0 && statusCount.get("F") == captureEquipmentChecklists.size())
                    captureEquipmentChecklistDetails.setAuditStatus("F");

                auditDao.updateRecord(captureEquipmentChecklistDetails);
            }
        }
    }

    private void updateCapturedInspectionDetailStatus(Long detailId, String finalRemarks, String srAuditorFinalRemarks, Long auditorId, Long srAuditorId){
        if(detailId != null && detailId > 0){
            CaptureInspectionDetails captureInspectionDetails = (CaptureInspectionDetails)auditDao.read(CaptureInspectionDetails.class, detailId);
            if (finalRemarks != null)
                captureInspectionDetails.setFinalAuditRemarks(finalRemarks);

            if (srAuditorFinalRemarks != null)
                captureInspectionDetails.setSrAuditorRemarks(srAuditorFinalRemarks);

            if (auditorId != null && auditorId > 0)
                captureInspectionDetails.setAuditorId(auditorId);

            if (srAuditorId != null && srAuditorId > 0)
                captureInspectionDetails.setSrAuditorId(srAuditorId);

            JSONObject jsondata = new JSONObject();
            jsondata.put("PN", 0);
            jsondata.put("inspectionDetailId", detailId);
            List<CaptureInspectionChecklist> captureInspectionChecklists = auditDao.getCapturedInspectionChecklistByDetailId(jsondata).get("capturedInspectionChecklist");
            if(captureInspectionChecklists != null && !captureInspectionChecklists.isEmpty()){
                Map<String, Long> statusCount = captureInspectionChecklists
                        .stream()
                        .collect(Collectors.groupingBy(CaptureInspectionChecklist::getAuditStatus, Collectors.counting()));

                if((statusCount.containsKey("Q") && statusCount.get("Q") == captureInspectionChecklists.size())
                        || (statusCount.containsKey("Q") && statusCount.containsKey("P") && (statusCount.get("Q")+statusCount.get("P")) == captureInspectionChecklists.size()))
                    captureInspectionDetails.setAuditStatus("Q");

                if((statusCount.containsKey("R") && statusCount.get("R") > 0 && statusCount.get("R") < captureInspectionChecklists.size())
                        || (statusCount.containsKey("T") && statusCount.get("T") > 0))
                    captureInspectionDetails.setAuditStatus("T");

                if(statusCount.containsKey("R") && statusCount.get("R") > 0 && statusCount.get("R") == captureInspectionChecklists.size()
                        || (statusCount.containsKey("R") && statusCount.get("R") > 0 && !statusCount.containsKey("T") && !statusCount.containsKey("Q")))
                    captureInspectionDetails.setAuditStatus("R");

                if(statusCount.containsKey("V") && statusCount.get("V") > 0 && statusCount.get("V") == captureInspectionChecklists.size())
                    captureInspectionDetails.setAuditStatus("V");

                if(statusCount.containsKey("F") && statusCount.get("F") > 0 && statusCount.get("F") == captureInspectionChecklists.size())
                    captureInspectionDetails.setAuditStatus("F");

                auditDao.updateRecord(captureInspectionDetails);
            }
        }
    }

    private void updateCapturedEquipmentChecklistStatus(Long capturedChecklistId){
        if(capturedChecklistId != null && capturedChecklistId > 0){
            CaptureEquipmentChecklist captureEquipmentChecklist = (CaptureEquipmentChecklist)auditDao.read(CaptureEquipmentChecklist.class, capturedChecklistId);
            JSONObject jsondata = new JSONObject();
            jsondata.put("PN", 0);
            jsondata.put("equipmentDetailId", captureEquipmentChecklist.getEquipmentChecklistDetailId());
            jsondata.put("captureEquipmentChecklistId", captureEquipmentChecklist.getCaptureEquipmentChecklistId());

            List<EquipmentChecklistValidationHistory> equipmentHistoryList = auditDao.getAllEquipmentChecklistValidationHistory(jsondata).get("equipmentHistoryList");
            if(equipmentHistoryList != null && !equipmentHistoryList.isEmpty()){
                Map<String, Long> statusCount = equipmentHistoryList
                        .stream()
                        .collect(Collectors.groupingBy(EquipmentChecklistValidationHistory::getAuditStatus, Collectors.counting()));

                if(statusCount.containsKey("Q") && statusCount.size() == 1)
                    captureEquipmentChecklist.setAuditStatus("Q");

                if(statusCount.containsKey("R") && statusCount.containsKey("Q"))
                    captureEquipmentChecklist.setAuditStatus("T");

                if(statusCount.containsKey("R") && statusCount.size() == 1)
                    captureEquipmentChecklist.setAuditStatus("R");

                auditDao.updateRecord(captureEquipmentChecklist);
            }
        }
    }

    private void updateCapturedInspectionChecklistStatus(Long capturedChecklistId){
        if(capturedChecklistId != null && capturedChecklistId > 0){
            CaptureInspectionChecklist captureInspectionChecklist = (CaptureInspectionChecklist)auditDao.read(CaptureInspectionChecklist.class, capturedChecklistId);
            JSONObject jsondata = new JSONObject();
            jsondata.put("PN", 0);
            jsondata.put("inspectionDetailId", captureInspectionChecklist.getInspectionDetailId());
            jsondata.put("captureInspectionChecklistId", captureInspectionChecklist.getCaptureInspectionChecklistId());

            List<InspectionChecklistValidationHistory> inspectionHistoryList = auditDao.getAllInspectionChecklistValidationHistory(jsondata).get("inspectionHistoryList");
            if(inspectionHistoryList != null && !inspectionHistoryList.isEmpty()){
                Map<String, Long> statusCount = inspectionHistoryList
                        .stream()
                        .collect(Collectors.groupingBy(InspectionChecklistValidationHistory::getAuditStatus, Collectors.counting()));

                if(statusCount.containsKey("Q") && statusCount.size() == 1)
                    captureInspectionChecklist.setAuditStatus("Q");

                if(statusCount.containsKey("R") && statusCount.containsKey("Q"))
                    captureInspectionChecklist.setAuditStatus("T");

                if(statusCount.containsKey("R") && statusCount.size() == 1)
                    captureInspectionChecklist.setAuditStatus("R");

                auditDao.updateRecord(captureInspectionChecklist);
            }
        }
    }

    public String getEquipmentPenaltyList(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<Object[]> equipmentPenaltyList = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null) {
                Map<String, List<Object[]>> equipmentPenaltyMap = auditDao.getEquipmentPenaltyList(jsondata);
                List totalMatches = new ArrayList();
                if (equipmentPenaltyMap.get("equipmentPenaltyList") != null) {
                    equipmentPenaltyList = equipmentPenaltyMap.get("equipmentPenaltyList");
                    totalMatches = equipmentPenaltyMap.get("totalMatches");
                    equipmentPenaltyList.forEach(arr -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String description = arr[1].toString();
                        int penaltyQuantity = 0;
                        int assignedQuantity = 0;
                        int availableQuantity = 0;
                        int operationalQuantity = 0;
                        if(arr[3] != null && !arr[3].toString().isEmpty())
                            penaltyQuantity = Integer.parseInt(arr[3].toString());

                        if(arr[4] != null && !arr[4].toString().isEmpty())
                            assignedQuantity = Integer.parseInt(arr[4].toString());

                        if(arr[5] != null && !arr[5].toString().isEmpty())
                            operationalQuantity = Integer.parseInt(arr[5].toString());

                        if(arr[6] != null && !arr[6].toString().isEmpty())
                            availableQuantity = Integer.parseInt(arr[6].toString());

                        if(penaltyQuantity > 0 && availableQuantity < assignedQuantity){
                            description += "- "+(assignedQuantity - availableQuantity)+" quantity is not available.";
                        }
                        if(penaltyQuantity > 0 && operationalQuantity < availableQuantity){
                            description += "- "+(availableQuantity - operationalQuantity)+" quantity is not Operational.";
                        }
                        mapObj.put("incidentDate", arr[0]);
                        mapObj.put("description", description);
                        mapObj.put("penaltyAmount", arr[2]);
                        list.add(mapObj);
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String getInspectionPenaltyList(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<Object[]> inspectionPenaltyList = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null) {
                Map<String, List<Object[]>> inspectionPenaltyMap = auditDao.getInspectionPenaltyList(jsondata);
                List totalMatches = new ArrayList();
                if (inspectionPenaltyMap.get("inspectionPenaltyList") != null) {
                    inspectionPenaltyList = inspectionPenaltyMap.get("inspectionPenaltyList");
                    totalMatches = inspectionPenaltyMap.get("totalMatches");
                    inspectionPenaltyList.forEach(arr -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String description = arr[1].toString();
                        if (arr[3] != null && !arr[3].toString().isEmpty())
                            description += "- " + arr[3];

                        mapObj.put("incidentDate", arr[0]);
                        mapObj.put("description", description);
                        mapObj.put("penaltyAmount", arr[2]);
                        list.add(mapObj);
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String getVendors(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<MasMmuVendor> masMmuVendors = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null) {
                Map<String, List<MasMmuVendor>> masMMUVendorMap = auditDao.getVendors(jsondata);
                List totalMatches = new ArrayList();
                if (masMMUVendorMap.get("vendors") != null) {
                    masMmuVendors = masMMUVendorMap.get("vendors");
                    totalMatches = masMMUVendorMap.get("totalMatches");
                    masMmuVendors.forEach(vendor -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String respondedBy = "";
                        String queriedBy = "";
                        if (vendor != null) {
                            mapObj.put("mmuVendorId", vendor.getMmuVendorId());
                            mapObj.put("mmuVendorCode", vendor.getMmuVendorCode());
                            mapObj.put("mmuVendorName", vendor.getMmuVendorName());
                            mapObj.put("status", vendor.getStatus());
                            list.add(mapObj);
                        }
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", 1);
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public String captureVendorBillDetail(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                JSONObject getparams = new JSONObject();
                getparams.put("PN", 0);
                getparams.put("invoiceNo", jsondata.getString("invoiceNo"));
                //List<CaptureVendorBillDetail> veldorsByInvoiceNo = auditDao.getCapturedVendorBillDetail(getparams).get("capturedVendorBillDetails");
                Boolean recordExists=auditDao.isRecordExists(jsondata.getString("invoiceNo"));
                getparams.remove("invoiceNo");
                getparams.put("billMonth", jsondata.getInt("billMonth"));
                getparams.put("billYear", jsondata.getInt("billYear"));
                getparams.put("mmuIds", jsondata.getString("mmuIds"));
                //List<CaptureVendorBillDetail> veldorsByBillMonthAndYear = auditDao.getCapturedVendorBillDetail(getparams).get("capturedVendorBillDetails");
                Map<String, Object> veldorsByBillMonthAndYear=auditDao.getBillMonthandYear(Long.parseLong(jsondata.get("billMonth").toString()),Long.parseLong(jsondata.get("billYear").toString()),Long.parseLong(jsondata.get("district").toString()),Long.parseLong(jsondata.get("cityId").toString()),jsondata.get("mmuIds").toString());
                List<Object[]> listVeldorsByBillMonthAndYear = (List<Object[]>) veldorsByBillMonthAndYear.get("list");
                if (recordExists && jsondata.getString("captureVendorBillDetailId").equals("")) {
                    json.put("status", 0);
                    json.put("msg", "Vendor Invoice Bill is already exist for given Invoice No.");
                } else if(!listVeldorsByBillMonthAndYear.isEmpty() && jsondata.getString("captureVendorBillDetailId").equals("")){
                    String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
                    json.put("status", 0);
                    json.put("msg", "Vendor invoice already submitted for the selected District, City, MMU, bill month and bill year");
                } else {
                    CaptureVendorBillDetail captureVendorBillDetail = new CaptureVendorBillDetail();
                    captureVendorBillDetail.setVendorId(jsondata.getLong("vendorId"));
                    captureVendorBillDetail.setCityId(jsondata.getLong("cityId"));
                    captureVendorBillDetail.setBillMonth(jsondata.getInt("billMonth"));
                    captureVendorBillDetail.setBillYear(jsondata.getInt("billYear"));
                    captureVendorBillDetail.setInvoiceNo(jsondata.getString("invoiceNo"));
                    captureVendorBillDetail.setCreatedBy(jsondata.getLong("createdBy"));
                    captureVendorBillDetail.setInvoiceAmount(jsondata.getLong("invoiceAmount"));
                    captureVendorBillDetail.setUploadedFileName(jsondata.getString("uploadedFileName"));
                    captureVendorBillDetail.setLastApprovalStatus(jsondata.getString("lastApprovalMsg"));
                    if(!jsondata.has("auditUploadedFileName"))
                    {	
                    captureVendorBillDetail.setPhase(jsondata.getString("phase"));
                    }
                    if(jsondata.getString("vendorFlag").equals("save"))
                    {
                    	captureVendorBillDetail.setVendorStatus("S");	
                    }
                    else
                    {
                    	captureVendorBillDetail.setVendorStatus("C");
                    }
                    captureVendorBillDetail.setPaymentStatus("P");
                    captureVendorBillDetail.setDistrictId(jsondata.getLong("district"));
                    captureVendorBillDetail.setCreatedOn(new Date());
                    if (jsondata.getString("invoiceDate") != null && !jsondata.getString("invoiceDate").isEmpty()) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date invoiceDate = formatter.parse(jsondata.getString("invoiceDate"));
                        captureVendorBillDetail.setInvoiceDate(invoiceDate);
                    }
                    Long id=null;
                    if(jsondata.has("updateStatus") && jsondata.getString("captureVendorBillDetailId").equals(""))
                    {	
                      id = auditDao.createRecord(captureVendorBillDetail);
                    }
                    else
                    {
                    	 if(jsondata.has("auditUploadedFileName"))
                         {
                    		 HashMap<String, Object> hs=new HashMap<>();
                    		 hs.put("auditUploadedFileName",jsondata.getString("auditUploadedFileName"));
                    		 hs.put("lastApprovalMsg",jsondata.getString("lastApprovalMsg"));
                           	if(jsondata.has("auditorRemarks"))
                            {
                            	hs.put("auditorRemarks",jsondata.getString("auditorRemarks"));
                            
                            }
                           	if(jsondata.has("penaltyAmountImposed"))
                            {
                           		if(jsondata.get("penaltyAmountImposed")!=null&&!jsondata.get("penaltyAmountImposed").equals(""))
                           		{	
                           		hs.put("penaltyAmountImposed",jsondata.getDouble("penaltyAmountImposed"));
                           		}
                           		else
                           		{
                           			hs.put("penaltyAmountImposed",null);	
                           		}
                            	 //captureVendorBillDetail.setInitialPenaltyAmount(jsondata.getDouble("penaltyAmountImposed"));	
                            }
                            hs.put("auditorAuthorityId",jsondata.getLong("auditorAuthorityId"));
                            if(jsondata.has("forwordTo"))
                            {	
	                            String str=jsondata.getString("forwordTo");
	                            if(str!=null && str!="")
	                            {	
	    	                    String[] forward=str.split("@@");
	                            hs.put("forwordTo",Long.parseLong(forward[0]));
	                            }
                            }
                            hs.put("captureVendorBillDetailId",jsondata.getLong("captureVendorBillDetailId"));
                            hs.put("action", jsondata.getString("actionIdValue"));
                            auditDao.updateCaptureVendorBillDetail(hs);
                            
                            if (jsondata.has("penaltyDetailsMMUArray")) {
    				        	JSONArray penaltyDetailsMMUArray = jsondata.getJSONArray("penaltyDetailsMMUArray");
    				            for (int i = 0; i < penaltyDetailsMMUArray.length(); i++) {
    				                JSONObject penaltyData = penaltyDetailsMMUArray.getJSONObject(i);
    				                CaptureVendorBillMMUDetail captureVendorBillMMUDetail = new CaptureVendorBillMMUDetail();
    				                captureVendorBillMMUDetail.setPenaltyAmount(Long.parseLong(penaltyData.getString("autoPenaltyAmount")));
    				                if(null!=penaltyData.getString("manualPenaltyAmount")&&!penaltyData.getString("manualPenaltyAmount").equals("")) {
    				                captureVendorBillMMUDetail.setManualPenaltyAmount(Long.parseLong(penaltyData.getString("manualPenaltyAmount")));
    				                }
    				                if(penaltyData.has("penaltyDocsUploads")) {
	    				                if (null!=penaltyData.getString("penaltyDocsUploads")&&!penaltyData.getString("penaltyDocsUploads").equals("")) {
	    				                captureVendorBillMMUDetail.setManualPenaltyFile(penaltyData.getString("penaltyDocsUploads"));
	    				                }
    				                }
    				                if(null!=penaltyData.getString("auditorRemarksMMU")&&!penaltyData.getString("auditorRemarksMMU").equals("")) {
    				                	captureVendorBillMMUDetail.setAuditorsRemarks(penaltyData.getString("auditorRemarksMMU"));
    				                }
    				                auditDao.createOrUpdateMMURecord(captureVendorBillMMUDetail,Long.parseLong(penaltyData.getString("mmuIds")),jsondata.getLong("captureVendorBillDetailId"));
    				            }
    				            json.put("msg", "Record Added Successfully in MMU supporting document!");
    				            json.put("captureVendorBillDetailId", id);
    				            json.put("status", 1);
    				        }
                         }
                    	 else
                    	 {	 
	                    	captureVendorBillDetail.setCaptureVendorBillDetailId(Long.parseLong(jsondata.getString("captureVendorBillDetailId")));
	                    	auditDao.createRecordForUpdate(captureVendorBillDetail);
                    	 }
                    }
                     ////////////////////////Save Vendor_Invoice_Approval table//////////////////////// 
                    if(jsondata.has("auditorAuthorityId") && jsondata.getString("auditorAuthorityId")!=null && !jsondata.getString("lastApprovalMsg").startsWith("Acknowledged") )
                    {	
	                    VendorInvoiceApproval via=new VendorInvoiceApproval();
	                    via.setCaptureVendorBillDetailId(Long.parseLong(jsondata.getString("captureVendorBillDetailId")));
	                    via.setAuthorityId(Long.parseLong(jsondata.getString("auditorAuthorityId")));
	                    via.setAuthorityDate(new Date());
	                    via.setAuthorityName(jsondata.getString("auditorName"));
	                    via.setAuthorityRole(jsondata.getString("auditorAuthorityName"));
	                    via.setAuthorityAction(jsondata.getString("actionIdValue"));
	                    via.setAuthorityRemarks(jsondata.getString("auditorRemarks"));
	                    via.setOrderNo(jsondata.getLong("auditorAuthorityOrderNo"));
	                    via.setUserId(jsondata.getLong("userId"));
	                    if(jsondata.has("forwordTo"))
                        {
		                    String str=jsondata.getString("forwordTo");
		                    if(str!=null && str!="")
	                        {
		                    String[] forward=str.split("@@");
		                    via.setForwardAuthorityId(Long.parseLong(forward[0]));
		                    via.setForwardOrderNo(Long.parseLong(forward[1]));
	                        }
                        }
	                    auditDao.createRecordForUpdate(via);
                    }
                    ////////////////////////////////MMU Details table///////////////////////////////////////
              
                    if (id != null && jsondata.has("firstTime")) {
                        String mmuIds = jsondata.getString("mmuIds");
                        if(mmuIds != null && !mmuIds.isEmpty()){
                            String []mmus = mmuIds.split(",");
                            for(String mmuid: mmus){
                                CaptureVendorBillMMUDetail captureVendorBillMMUDetail = new CaptureVendorBillMMUDetail();
                                captureVendorBillMMUDetail.setCaptureVendorBillDetailId(id);
                                captureVendorBillMMUDetail.setMmuId(Long.parseLong(mmuid));
                                	auditDao.createRecord(captureVendorBillMMUDetail);
                              }
                        }
                        json.put("msg", "Record Added Successfully!");
                        json.put("captureVendorBillDetailId", id);
                        json.put("status", 1);
                    }
                    else if(!jsondata.getString("captureVendorBillDetailId").equals(""))
                    {	
                    	json.put("msg", "Record Added Successfully!");
                        json.put("status", 1);
                    }else {
                        json.put("status", 0);
                        json.put("msg", "Error Capturing Vendor Invoice Bill!");
                    }
                    
				////////////////////////////////Supporting document Details table///////////////////////////////////////
                    if(id==null) {
                    	id=Long.parseLong(jsondata.getString("captureVendorBillDetailId"));
                    }				                    
                    if (id != null && !jsondata.has("auditorAuthorityId") && ("save".equals(jsondata.getString("vendorFlag")) || "submit".equals(jsondata.getString("vendorFlag"))||"submitted".equals(jsondata.getString("vendorFlag")))){
				        	JSONArray supportingDocsArray = jsondata.getJSONArray("supportingDocs");
				            for (int i = 0; i < supportingDocsArray.length(); i++) {
				                JSONObject doc = supportingDocsArray.getJSONObject(i);
				                VendorInvoicSupportingDocs vendorInvoicSupportingDocs = new VendorInvoicSupportingDocs();
				                vendorInvoicSupportingDocs.setDocumentName(doc.getString("docId"));
				                if(doc.has("supportingDocsUpload")) {
					                if(""!=doc.getString("supportingDocsUpload")) {
					                vendorInvoicSupportingDocs.setFileName(doc.getString("supportingDocsUpload"));
					                }
				                }
				                vendorInvoicSupportingDocs.setDocumentNote(doc.getString("medicalDocs"));
				                vendorInvoicSupportingDocs.setCaptureVendorBillDetailId(id);
				                vendorInvoicSupportingDocs.setUserId(jsondata.getLong("createdBy"));
				                System.out.println("Doc value print :::::: "+doc.getString("docId"));
				                if(doc.getString("captureVendorSupportingDocsId").equals("")&&!doc.getString("docId").equals("")) {
				                	auditDao.createRecord(vendorInvoicSupportingDocs);	
				                }else if(doc.has("captureVendorSupportingDocsId")){
				                	if(null!=doc.getString("captureVendorSupportingDocsId")&&doc.getString("captureVendorSupportingDocsId")!="" &&!doc.getString("captureVendorSupportingDocsId").equals("undefined")
				                			&&!doc.getString("docId").equals("")) {
					                	vendorInvoicSupportingDocs.setCaptureVendorSupportingDocsId(Long.parseLong(doc.getString("captureVendorSupportingDocsId")));
					                	auditDao.createRecordForUpdate(vendorInvoicSupportingDocs);
				                	}
				                }
				                
				            }
				            json.put("msg", "Record Added Successfully in MMU supporting document!");
				            json.put("captureVendorBillDetailId", id);
				            json.put("status", 1);
				        }
				           }
				         }
				        }catch (Exception ex) {
				            json.put("status", 0);
				            json.put("msg", "Error Capturing Vendor Invoice Bill!");
				            ex.printStackTrace();
				        }

        return json.toString();
    }
    
   /* public String getCapturedVendorBillDetail(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                Map<String, List<CaptureVendorBillDetail>> capturedEquipmentChecklist;
                if (jsondata.has("type") && ("NodalWaitingData".equals(jsondata.get("type")) || "paymentOfficer".equals(jsondata.get("type")))) {
                    capturedEquipmentChecklist = auditDao.getCapturedVendorBillNodalDetail(jsondata);
                } else {
                    capturedEquipmentChecklist = auditDao.getCapturedVendorBillDetail(jsondata);
                }

                List<CaptureVendorBillDetail> captureVendorBillDetails = capturedEquipmentChecklist.getOrDefault("capturedVendorBillDetails", new ArrayList<>());
                List<?> totalMatches = capturedEquipmentChecklist.getOrDefault("totalMatches", new ArrayList<>());

                if (!captureVendorBillDetails.isEmpty()) {
                    // Batch read users, vendors, cities, and districts
                    List<Long> userIds = captureVendorBillDetails.stream().map(CaptureVendorBillDetail::getCreatedBy).collect(Collectors.toList());
                    List<Long> vendorIds = captureVendorBillDetails.stream().map(CaptureVendorBillDetail::getVendorId).collect(Collectors.toList());
                    List<Long> cityIds = captureVendorBillDetails.stream().map(CaptureVendorBillDetail::getCityId).collect(Collectors.toList());
                    List<Long> districtIds = captureVendorBillDetails.stream().map(CaptureVendorBillDetail::getDistrictId).collect(Collectors.toList());

                    Map<Long, Users> usersMap = auditDao.readUsers(userIds);
                    Map<Long, MasMmuVendor> vendorsMap = auditDao.readVendors(vendorIds);
                    Map<Long, MasCity> citiesMap = auditDao.readCities(cityIds);
                    Map<Long, MasDistrict> districtsMap = auditDao.readDistricts(districtIds);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                    List<Map<String, Object>> list = captureVendorBillDetails.parallelStream().map(e -> {
                        Map<String, Object> mapObj = new HashMap<>();
                        String createdBy = Optional.ofNullable(usersMap.get(e.getCreatedBy())).map(Users::getUserName).orElse("");
                        String vendorName = Optional.ofNullable(vendorsMap.get(e.getVendorId())).map(MasMmuVendor::getMmuVendorName).orElse("");
                        String cityName = Optional.ofNullable(citiesMap.get(e.getCityId())).map(MasCity::getCityName).orElse("");
                        String districtName = Optional.ofNullable(districtsMap.get(e.getDistrictId())).map(MasDistrict::getUpss).orElse("");

                        String createdDate = Optional.ofNullable(e.getCreatedOn()).map(formatter::format).orElse("");
                        String invoiceDate = Optional.ofNullable(e.getInvoiceDate()).map(formatter::format).orElse("");

                        List<Long> ids = Collections.singletonList(e.getCaptureVendorBillDetailId());
                        Collection<CaptureVendorBillMMUDetail> mmuDetails = auditDao.getCapturedMMUDetails(ids).values();

                        List<Map<String, Object>> vendorBillMMUDetailList = mmuDetails.stream().map(mmudetail -> {
                            Map<String, Object> vendorBillMMUDetail = new HashMap<>();
                            MasMMU masMMU = (MasMMU) auditDao.read(MasMMU.class, mmudetail.getMmuId());
                            vendorBillMMUDetail.put("captureVendorBillMMUDetailId", mmudetail.getCaptureVendorBillMMUDetailId());
                            vendorBillMMUDetail.put("captureVendorBillDetailId", mmudetail.getCaptureVendorBillDetailId());
                            vendorBillMMUDetail.put("mmuId", mmudetail.getMmuId());
                            vendorBillMMUDetail.put("mmuName", masMMU.getMmuName());
                            vendorBillMMUDetail.put("auditorsRemarks", mmudetail.getAuditorsRemarks());
                            vendorBillMMUDetail.put("autoPenaltyAmount", Optional.ofNullable(mmudetail.getPenaltyAmount()).orElse(0L));
                            vendorBillMMUDetail.put("manualPenaltyAmount", Optional.ofNullable(mmudetail.getManualPenaltyAmount()).orElse(0L));
                            vendorBillMMUDetail.put("penaltyFileName", mmudetail.getManualPenaltyFile());
                            vendorBillMMUDetail.put("coRemarks", mmudetail.getCoRemarks());
                            vendorBillMMUDetail.put("removePenalty", mmudetail.getRemovePenalty());
                            return vendorBillMMUDetail;
                        }).collect(Collectors.toList());

                        String mmuNames = mmuDetails.stream().map(mmudetail -> {
                            MasMMU masMMU = (MasMMU) auditDao.read(MasMMU.class, mmudetail.getMmuId());
                            return masMMU.getMmuName();
                        }).collect(Collectors.joining(","));
                        String mmuIds = mmuDetails.stream().map(mmudetail -> String.valueOf(mmudetail.getMmuId())).collect(Collectors.joining(","));

                        Collection<VendorInvoicSupportingDocs> suppDocs = auditDao.getVendorSupportingDocs(ids).values();
                        List<Map<String, Object>> vendorSupportingDocsList = suppDocs.stream().map(vendorSDocs -> {
                            Map<String, Object> vendorSupportingDoc = new HashMap<>();
                            vendorSupportingDoc.put("captureVendorSupportingDocsId", vendorSDocs.getCaptureVendorSupportingDocsId());
                            vendorSupportingDoc.put("captureVendorBillDetailId", vendorSDocs.getCaptureVendorBillDetailId());
                            vendorSupportingDoc.put("documentName", vendorSDocs.getDocumentName());
                            vendorSupportingDoc.put("documentNote", vendorSDocs.getDocumentNote());
                            vendorSupportingDoc.put("fileName", vendorSDocs.getFileName());
                            vendorSupportingDoc.put("screen_name", Optional.ofNullable(vendorSDocs.getScreenType()).orElse("vendor"));
                            return vendorSupportingDoc;
                        }).collect(Collectors.toList());

                        String documentNames = suppDocs.stream().map(VendorInvoicSupportingDocs::getDocumentName).collect(Collectors.joining(","));
                        String documentNotes = suppDocs.stream().map(VendorInvoicSupportingDocs::getDocumentNote).collect(Collectors.joining(","));
                        String fileNames = suppDocs.stream().map(VendorInvoicSupportingDocs::getFileName).collect(Collectors.joining(","));
                        String captureVendorSupportingDocsIds = suppDocs.stream().map(doc -> String.valueOf(doc.getCaptureVendorSupportingDocsId())).collect(Collectors.joining(","));

                        mapObj.put("captureVendorBillDetailId", e.getCaptureVendorBillDetailId());
                        mapObj.put("vendorId", e.getVendorId());
                        mapObj.put("vendorName", vendorName);
                        mapObj.put("cityId", e.getCityId());
                        mapObj.put("cityName", cityName);
                        mapObj.put("mmuIds", mmuIds);
                        mapObj.put("mmuNames", mmuNames);
                        mapObj.put("vendorBillMMUDetailList", vendorBillMMUDetailList);
                        mapObj.put("billMonth", e.getBillMonth());
                        mapObj.put("billYear", e.getBillYear());
                        mapObj.put("invoiceNo", e.getInvoiceNo());
                        mapObj.put("invoiceDate", invoiceDate);
                        mapObj.put("invoiceAmount", e.getInvoiceAmount());
                        mapObj.put("uploadedFileName", e.getUploadedFileName());
                        mapObj.put("auditorFileName", e.getAuditorFileName());
                        mapObj.put("imposedPenaltyAmount", Optional.ofNullable(e.getPenaltyAmount()).orElse(0L));
                        mapObj.put("suggestedPenaltyAmount", Optional.ofNullable(e.getAuditorPenaltyAmount()).orElse(0L));
                        mapObj.put("createdBy", createdBy);
                        mapObj.put("createdOn", createdDate);
                        mapObj.put("status", e.getStatus());
                        mapObj.put("district", e.getDistrictId());
                        mapObj.put("districtName", districtName);
                        mapObj.put("vendorStatus", e.getVendorStatus());
                        mapObj.put("paymentStatus", e.getPaymentStatus());
                        mapObj.put("phase", e.getPhase());
                        mapObj.put("supportingDocsId", captureVendorSupportingDocsIds);
                        mapObj.put("supportingDocsNames", documentNames);
                        mapObj.put("supportingDocsNotes", documentNotes);
                        mapObj.put("supportingDocsFileNames", fileNames);
                        mapObj.put("vendorSupportingDocsList", vendorSupportingDocsList);
                        mapObj.put("calculatedPenaltyAmount", vendorBillMMUDetailList.stream().mapToLong(detail -> (Long) detail.getOrDefault("autoPenaltyAmount", 0L) + (Long) detail.getOrDefault("manualPenaltyAmount", 0L)).sum());
                        mapObj.put("initialPenaltyAmount", Optional.ofNullable(e.getInitialPenaltyAmount()).orElse(0L));
                        mapObj.put("pendingApprovedFor", Optional.ofNullable(e.getNextAuthorityId()).orElse(0L));
                        mapObj.put("lastApprovedBy", Optional.ofNullable(e.getCurrentAuthorityId()).orElse(0L));
                        mapObj.put("penaltyAmount", Optional.ofNullable(e.getPenaltyAmount()).orElse(0L));
                        mapObj.put("auditorRemarks", e.getFinalAuditorsRemarks());
                        mapObj.put("lastApprovalMsg", Optional.ofNullable(e.getLastApprovalStatus()).orElse(""));

                        return mapObj;
                    }).collect(Collectors.toList());

                    json.put("data", list);
                    json.put("count", totalMatches.size());
                    json.put("msg", "Get Record successfully");
                    json.put("status", 1);
                } else {
                    json.put("data", new ArrayList<>());
                    json.put("count", 0);
                    json.put("msg", "No Record Found");
                    json.put("status", 0);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json.toString();
    }*/
    public String getCapturedVendorBillDetail(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<CaptureVendorBillDetail> captureVendorBillDetails = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null) {
                Map<String, List<CaptureVendorBillDetail>> capturedEquipmentChecklist;
                if(jsondata.has("type") && (jsondata.get("type").equals("NodalWaitingData")||jsondata.get("type").equals("paymentOfficer"))) {
                	capturedEquipmentChecklist = auditDao.getCapturedVendorBillNodalDetail(jsondata);
                }else {
                	capturedEquipmentChecklist = auditDao.getCapturedVendorBillDetail(jsondata);
                }
                List totalMatches = new ArrayList();
                if (capturedEquipmentChecklist.get("capturedVendorBillDetails") != null) {
                    captureVendorBillDetails = capturedEquipmentChecklist.get("capturedVendorBillDetails");
                    totalMatches = capturedEquipmentChecklist.get("totalMatches");
                    captureVendorBillDetails.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String createdBy = "";
                        String auditorName = "";
                        String vendorName = "";
                        String cityName = "";
                        String districtName="";
                        if (e != null) {
                            if (e.getCreatedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, e.getCreatedBy());
                                if (user != null)
                                    createdBy = user.getUserName();
                            }
                           
                            if (e.getVendorId() != null) {
                                MasMmuVendor vendor = (MasMmuVendor) auditDao.read(MasMmuVendor.class, e.getVendorId());
                                if (vendor != null)
                                    vendorName = vendor.getMmuVendorName();
                            }

                            if (e.getCityId() != null) {
                                MasCity city = (MasCity) auditDao.read(MasCity.class, e.getCityId());
                                if (city != null)
                                    cityName = city.getCityName();
                            }
                            if (e.getDistrictId() != null) {
                                MasDistrict district = (MasDistrict) auditDao.read(MasDistrict.class, e.getDistrictId());
                                if (district != null)
                                	districtName = district.getUpss();
                            }

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String createdDate = "";
                            String invoiceDate = "";
                            if (e.getCreatedOn() != null)
                                createdDate = formatter.format(e.getCreatedOn());

                            if (e.getInvoiceDate() != null)
                                invoiceDate = formatter.format(e.getInvoiceDate());

                            String mmuNames = "";
                            String mmuIds = "";
                            List<Long> ids = new ArrayList<>();
                            Long calculatedPenaltyAmount = 0l;
                            ids.add(e.getCaptureVendorBillDetailId());
                            Collection<CaptureVendorBillMMUDetail> li = auditDao.getCapturedMMUDetails(ids).values();
                            List<Map<String, Object>> vendorBillMMUDetailList = new ArrayList<>();
                            if(li != null && !li.isEmpty()){
                                for(CaptureVendorBillMMUDetail mmudetail: li){
                                    Map<String, Object> vendorBillMMUDetail = new HashMap<>();
                                    MasMMU masMMU = (MasMMU)auditDao.read(MasMMU.class, mmudetail.getMmuId());
                                    vendorBillMMUDetail.put("captureVendorBillMMUDetailId", mmudetail.getCaptureVendorBillMMUDetailId());
                                    vendorBillMMUDetail.put("captureVendorBillDetailId", mmudetail.getCaptureVendorBillDetailId());
                                    vendorBillMMUDetail.put("mmuId", mmudetail.getMmuId());
                                    vendorBillMMUDetail.put("mmuName", masMMU.getMmuName());
                                    vendorBillMMUDetail.put("auditorsRemarks", mmudetail.getAuditorsRemarks());
                                    if(mmudetail != null && null!=mmudetail.getPenaltyAmount()&& mmudetail.getPenaltyAmount() != null) {
                                    vendorBillMMUDetail.put("autoPenaltyAmount", mmudetail.getPenaltyAmount());
                                    calculatedPenaltyAmount=calculatedPenaltyAmount+mmudetail.getPenaltyAmount(); 
                                    }
                                    if(mmudetail != null && null!= mmudetail.getManualPenaltyAmount()&& mmudetail.getManualPenaltyAmount() != null) {
                                    vendorBillMMUDetail.put("manualPenaltyAmount", mmudetail.getManualPenaltyAmount());
                                    calculatedPenaltyAmount=calculatedPenaltyAmount+mmudetail.getManualPenaltyAmount();
                                    }
                                    vendorBillMMUDetail.put("penaltyFileName", mmudetail.getManualPenaltyFile()); 
                                    vendorBillMMUDetail.put("coRemarks", mmudetail.getCoRemarks());
                                    vendorBillMMUDetail.put("removePenalty", mmudetail.getRemovePenalty());
                                    vendorBillMMUDetailList.add(vendorBillMMUDetail);
                                    if(masMMU != null) {
                                        mmuNames += masMMU.getMmuName() + ",";
                                        mmuIds += masMMU.getMmuId() + ",";
                                    }
                                }
                                mmuNames = mmuNames.substring(0, mmuNames.length()-1);
                                mmuIds = mmuIds.substring(0, mmuIds.length()-1);
                            }
                            
                            String documentNames="";
                            String documentNotes="";
                            String fileNames="";
                            String captureVendorSupportingDocsIds="";
                            Collection<VendorInvoicSupportingDocs> suppDocs = auditDao.getVendorSupportingDocs(ids).values();
                            List<Map<String, Object>> vendorSupportingDocsList = new ArrayList<>();
                            if(li != null && !li.isEmpty()){
                                for(VendorInvoicSupportingDocs vendorSDocs: suppDocs){
                                    Map<String, Object> vendorBillMMUDetail = new HashMap<>();
                                    VendorInvoicSupportingDocs vendorSupportingDocs = (VendorInvoicSupportingDocs)auditDao.read(VendorInvoicSupportingDocs.class, vendorSDocs.getCaptureVendorSupportingDocsId());
                                    vendorBillMMUDetail.put("captureVendorSupportingDocsId", vendorSDocs.getCaptureVendorSupportingDocsId());
                                    vendorBillMMUDetail.put("captureVendorBillDetailId", vendorSDocs.getCaptureVendorBillDetailId());
                                    vendorBillMMUDetail.put("documentName", vendorSDocs.getDocumentName());
                                    vendorBillMMUDetail.put("documentNote", vendorSDocs.getDocumentNote());
                                    vendorBillMMUDetail.put("fileName", vendorSDocs.getFileName());
                                    if(null!=vendorSDocs.getScreenType() && vendorSDocs.getScreenType().equals("nodal_officer")) {
                                    vendorBillMMUDetail.put("screen_name", vendorSDocs.getScreenType());
                                    }else if(null!=vendorSDocs.getScreenType() && vendorSDocs.getScreenType().equals("paymentScreen")) {
                                        vendorBillMMUDetail.put("screen_name", vendorSDocs.getScreenType());
                                    }else {
                                    	 vendorBillMMUDetail.put("screen_name", "vendor");
                                    }
                                    
                                    vendorSupportingDocsList.add(vendorBillMMUDetail);
                                    if(vendorSupportingDocs != null) {
                                    	captureVendorSupportingDocsIds += vendorSupportingDocs.getCaptureVendorSupportingDocsId() + ",";
                                    	documentNames += vendorSupportingDocs.getDocumentName() + ",";
                                    	documentNotes += vendorSupportingDocs.getDocumentNote() + ",";
                                    	fileNames += vendorSupportingDocs.getFileName() + ",";
                                    }
                                }
                                if (documentNames != null && !documentNames.isEmpty()) {
                                    documentNames = documentNames.substring(0, documentNames.length() - 1);
                                } else {
                                    // Handle the case where documentNames is null or empty if needed
                                    documentNames = "";
                                }
                                if (documentNotes != null && !documentNotes.isEmpty()) {
                                documentNotes = documentNotes.substring(0, documentNotes.length()-1);
                                }else {
                                    // Handle the case where documentNames is null or empty if needed
                                	documentNotes = "";
                                }
                                if (fileNames != null && !fileNames.isEmpty()) {
                                fileNames = fileNames.substring(0, fileNames.length()-1);
                                }else {
                                    // Handle the case where documentNames is null or empty if needed
                                	fileNames = "";
                                }
                                if (captureVendorSupportingDocsIds != null && !captureVendorSupportingDocsIds.isEmpty()) {
                                captureVendorSupportingDocsIds = captureVendorSupportingDocsIds.substring(0, captureVendorSupportingDocsIds.length()-1);
                                }else {
                                    // Handle the case where documentNames is null or empty if needed
                                	captureVendorSupportingDocsIds = "";
                                }
                            }
                            
                            mapObj.put("captureVendorBillDetailId", e.getCaptureVendorBillDetailId());
                            mapObj.put("vendorId", e.getVendorId());
                            mapObj.put("vendorName", vendorName);
                            mapObj.put("cityId", e.getCityId());
                            mapObj.put("cityName", cityName);
                            mapObj.put("mmuIds", mmuIds);
                            mapObj.put("mmuNames", mmuNames);
                            mapObj.put("vendorBillMMUDetailList", vendorBillMMUDetailList);
                            mapObj.put("billMonth", e.getBillMonth());
                            mapObj.put("billYear", e.getBillYear());
                            mapObj.put("invoiceNo", e.getInvoiceNo());
                            mapObj.put("invoiceDate", invoiceDate);
                            mapObj.put("invoiceAmount", e.getInvoiceAmount());
                            mapObj.put("uploadedFileName", e.getUploadedFileName());
                            mapObj.put("auditorFileName", e.getAuditorFileName());
                            if(null!=e.getPenaltyAmount()) {
                            	mapObj.put("imposedPenaltyAmount", e.getPenaltyAmount());
                            }else {
                            	mapObj.put("imposedPenaltyAmount", "");
                            }
                            if(null!=e.getAuditorPenaltyAmount()) {
                            	mapObj.put("suggestedPenaltyAmount", e.getAuditorPenaltyAmount());
                            }else {
                            	mapObj.put("suggestedPenaltyAmount", e.getAuditorPenaltyAmount());
                            }
                            mapObj.put("createdBy", createdBy);
                            mapObj.put("createdOn", createdDate);
                            mapObj.put("status", e.getStatus());
                            mapObj.put("district", e.getDistrictId());
                            mapObj.put("districtName", districtName);
                            mapObj.put("vendorStatus", e.getVendorStatus());
                            mapObj.put("paymentStatus", e.getPaymentStatus());
                            mapObj.put("phase", e.getPhase());
                            mapObj.put("supportingDocsId", captureVendorSupportingDocsIds);
                            mapObj.put("supportingDocsNames", documentNames);
                            mapObj.put("supportingDocsNotes", documentNotes);
                            mapObj.put("supportingDocsFileNames", fileNames);
                            mapObj.put("vendorSupportingDocsList", vendorSupportingDocsList);
                            mapObj.put("calculatedPenaltyAmount", calculatedPenaltyAmount);
                            if(e.getInitialPenaltyAmount()!=null)
                            {
                            	mapObj.put("initialPenaltyAmount", e.getInitialPenaltyAmount());	
                            }
                            else
                            {
                            	mapObj.put("initialPenaltyAmount", "");	
                            }
                            if(e.getNextAuthorityId()!=null)
                            {
                            	mapObj.put("pendingApprovedFor", e.getNextAuthorityId()); 	
                            }
                            else
                            {
                            	mapObj.put("pendingApprovedFor",""); 	
                            }
                            if(e.getCurrentAuthorityId()!=null)
                            {
                            	mapObj.put("lastApprovedBy", e.getCurrentAuthorityId());
                            }
                            else
                            {
                            	mapObj.put("lastApprovedBy", "");	
                            }
                            if(null!=e.getPenaltyAmount())
                            {
                            	mapObj.put("penaltyAmount", e.getPenaltyAmount());	
                            }
                            else
                            {
                            	mapObj.put("penaltyAmount", "");
                            }
                            mapObj.put("auditorFileName", e.getAuditorFileName());
                            mapObj.put("auditorRemarks", e.getFinalAuditorsRemarks());
                            if(e.getLastApprovalStatus()!=null)
                            {	
                            mapObj.put("lastApprovalMsg", e.getLastApprovalStatus());
                            }
                            else
                            {
                            	mapObj.put("lastApprovalMsg", "");	
                            }
                            list.add(mapObj);
                        }
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String getVendorsMMUAndCity(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            Map<MasCity, List<MasMMU>> data = new HashMap<>();
            if (jsondata != null) {
                Map<String, Map<MasCity, List<MasMMU>>> vendorsMMUS = auditDao.getVendorsMMUAndCity(jsondata);
                if (vendorsMMUS.get("vendorsMMUS") != null) {
                    data = vendorsMMUS.get("vendorsMMUS");

                    JSONObject cityMMU = new JSONObject();
                    data.forEach((k, v) -> {
                        JSONObject city = new JSONObject();
                        city.put("cityId", k.getCityId());
                        city.put("cityName", k.getCityName());

                        List<JSONObject> values = v.stream().map(m -> {
                            JSONObject mmu = new JSONObject();
                            mmu.put("mmuId",m.getMmuId());
                            mmu.put("mmuName",m.getMmuName());
                            return mmu;
                        }).collect(Collectors.toList());

                        JSONArray val = new JSONArray();
                        val.put(city);
                        val.put(values);
                        cityMMU.put(k.getCityId().toString(), val);
                    });
                    if (cityMMU != null && cityMMU.length() > 0) {
                        json.put("data", cityMMU);
                        json.put("count", cityMMU.length());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", data);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    public String getVendorsPenalty(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> vendorPenaltyList = new ArrayList<>();
            if (jsondata != null) {
                Map<String, List<Map<String, Object>>> vendorPenaltyMap = auditDao.getVendorsPenalty(jsondata);
                if (vendorPenaltyMap.get("vendorInsEquipPenaltyList") != null) {
                    vendorPenaltyList = vendorPenaltyMap.get("vendorInsEquipPenaltyList");
                    Map<String, Double> mmuPenaltySum = vendorPenaltyList
                            .stream()
                            .collect(Collectors.groupingBy(r -> r.get("mmuName").toString()+"@@"+r.get("mmuId").toString(), Collectors.summingDouble(r -> (Double) r.get("penaltyAmount"))));
                    
                    Map<String, List<Map<String, Object>>> mmuPenaltyId = vendorPenaltyList.stream().collect(Collectors.groupingBy(r->r.get("mmuId").toString()));
                    
                    Map<String, List<Map<String, Object>>> mmuPenalty = vendorPenaltyList.stream().collect(Collectors.groupingBy(r->r.get("mmuName").toString()));
                    JSONObject data = new JSONObject();
                    data.put("mmuPenaltySum", mmuPenaltySum);
                    data.put("mmuPenalty", mmuPenalty);
                 
                    if (vendorPenaltyList != null && vendorPenaltyList.size() > 0) {
                        json.put("data", data);
                        json.put("count", 1);
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", vendorPenaltyList);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public String updateVendorBillRemarks(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            if (jsondata != null) {
                Long captureVendorBillDetailId = jsondata.getLong("captureVendorBillDetailId");
                String remarksType = jsondata.getString("remarksType");
                CaptureVendorBillDetail captureVendorBillDetail = (CaptureVendorBillDetail)auditDao.read(CaptureVendorBillDetail.class, captureVendorBillDetailId);
                captureVendorBillDetail.setStatus(jsondata.getString("status"));
                if("AUDITOR".equals(remarksType))
                    captureVendorBillDetail.setAuditorFileName(jsondata.getString("fileName"));
                auditDao.updateRecord(captureVendorBillDetail);

                JSONArray vendorBillAuditorDataArr = new JSONArray(jsondata.getString("vendorBillAuditorData"));
                for(int i=0;i<vendorBillAuditorDataArr.length();i++){
                    JSONObject obj = vendorBillAuditorDataArr.getJSONObject(i);
                    Long id = obj.getLong("id");
                    CaptureVendorBillMMUDetail captureVendorBillMMUDetail = (CaptureVendorBillMMUDetail)auditDao.read(CaptureVendorBillMMUDetail.class, id);

                    if("CO".equals(remarksType)) {
                        captureVendorBillMMUDetail.setCoRemarks(obj.getString("remarks"));
                        captureVendorBillMMUDetail.setRemovePenalty(obj.getString("removePenalty"));
                    } else captureVendorBillMMUDetail.setAuditorsRemarks(obj.getString("remarks"));

                    auditDao.updateRecord(captureVendorBillMMUDetail);
                }

                json.put("msg", "Record Updated successfully");
                json.put("status", 1);
            }
        }catch (Exception ex) {
            json.put("status", 0);
            json.put("msg", "Error Capturing Vendor Invoice Bill!");
            ex.printStackTrace();
        }

        return json.toString();
    }

	@Override
	public String getVendorInvoiceApprovalDetail(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            List<VendorInvoiceApproval> captureVendorBillDetails = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null) {
                Map<String, List<VendorInvoiceApproval>> vendorInvoiceApproval = auditDao.getVendorInvoiceApprovalDetail(jsondata);
                List totalMatches = new ArrayList();
                if (vendorInvoiceApproval.get("capturedVendorBillDetails") != null) {
                    captureVendorBillDetails = vendorInvoiceApproval.get("capturedVendorBillDetails");
                    totalMatches = vendorInvoiceApproval.get("totalMatches");
                    captureVendorBillDetails.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String forwardName = "";
                       
                        if (e != null) {
                            if (e.getForwardAuthorityId() != null) {
                                MasAuthority user = (MasAuthority) auditDao.read(MasAuthority.class, e.getForwardAuthorityId());
                                if (user != null)
                                	forwardName = user.getAuthorityName();
                            }
                           

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String authorityDate = "";
                            String invoiceDate = "";
                            if (e.getAuthorityDate() != null)
                            {	
                            	authorityDate = formatter.format(e.getAuthorityDate());
                             }

                            mapObj.put("vendorInvoiceApprovalId", e.getVendorInvoiceApprovalId());
                            mapObj.put("captureVendorBillDetailId", e.getCaptureVendorBillDetailId());
                            mapObj.put("authorityId", e.getAuthorityId());
                            mapObj.put("authorityName", e.getAuthorityName());
                            mapObj.put("authorityRemarks", e.getAuthorityRemarks());
                            mapObj.put("authorityRole", e.getAuthorityRole());
                            mapObj.put("orderNo", e.getOrderNo());
                            mapObj.put("forwardAuthorityId", e.getForwardAuthorityId());
                            mapObj.put("forwardOrderNo", e.getForwardOrderNo());
                            mapObj.put("forwardName", forwardName);
                            mapObj.put("authorityDate", authorityDate);
                            mapObj.put("actionValue", e.getAuthorityAction());
                            
                            list.add(mapObj);
                        }
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }
	
	public static HashMap<String, Object> jsonToMap(JSONObject json) {
        HashMap<String, Object> map = new HashMap<>();
        Iterator<String> keys = json.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);
            map.put(key, value);
        }

        return map;
    }


	@Override
	public String saveOrUpdateAuthorityVendorBillDetails(JSONObject jsonWebdata, HttpServletRequest request,
			HttpServletResponse response) {
		String authorityDetails = null;
		JSONObject json = new JSONObject();
		JSONArray newForm=new JSONArray(jsonWebdata.get("captureNodalOfficerForm").toString());
		String jsonString = (String) newForm.get(0);
		JSONObject jsondataGetVal = new JSONObject(jsonString);
		//JSONObject jsondataGetVal = (JSONObject) newForm.get(0);
		//HashMap<String, Object> jsondata=jsondataGetVal;
		HashMap<String, Object> jsondata = jsonToMap(jsondataGetVal);
		try {
			////////////////////////////////Supporting document Details table///////////////////////////////////////
			JSONArray noteSheetArray=new JSONArray(jsonWebdata.get("noteSheetArray").toString());
			if (noteSheetArray!=null && !jsonWebdata.isNull("noteSheetArray")) {
				//JSONArray noteSheetArray = new JSONArray(jsonString);
				for (int i = 0; i < noteSheetArray.length(); i++) {
					JSONObject doc = noteSheetArray.getJSONObject(i);
					if(!doc.getString("noteSheet").equals("")) {
					VendorInvoicSupportingDocs vendorInvoicSupportingDocs = new VendorInvoicSupportingDocs();
					vendorInvoicSupportingDocs.setFileName(doc.getString("noteSheetDocsUploads"));
					vendorInvoicSupportingDocs.setDocumentNote(doc.getString("noteSheet"));
					vendorInvoicSupportingDocs.setScreenType(doc.getString("screenType"));
					vendorInvoicSupportingDocs.setCaptureVendorBillDetailId(
							Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
					vendorInvoicSupportingDocs.setUserId(Long.parseLong(jsondata.get("userId").toString()));
					auditDao.createRecord(vendorInvoicSupportingDocs);
					}
				}
				json.put("msg", "Record Added Successfully in MMU supporting document by nodal oficer!");
			} 
			
		if (!jsondata.isEmpty()) {
			if(jsondata.containsKey("tdsDeduction"))
				{
					
					Long finalAmount=(long)Double.parseDouble(jsondata.get("finalAmount").toString());
				    getFundDetails(finalAmount,Long.parseLong(jsondata.get("districtId").toString()),Long.parseLong(jsondata.get("cityId").toString()),jsondata.get("phase").toString());
				    //this code covered payment recepit document upload and remarks in supprting docs table
				    VendorInvoicSupportingDocs vendorInvoicSupportingDocs = new VendorInvoicSupportingDocs();
				    vendorInvoicSupportingDocs.setFileName(jsonWebdata.get("paymentRecepitFileName").toString());
					vendorInvoicSupportingDocs.setDocumentNote(jsondata.get("paymentRemarks").toString());
					vendorInvoicSupportingDocs.setScreenType("paymentScreen");
					vendorInvoicSupportingDocs.setCaptureVendorBillDetailId(Long.parseLong(jsondata.get("captureVendorBillDetailId").toString()));
					vendorInvoicSupportingDocs.setUserId(Long.parseLong(jsondata.get("userId").toString()));
					auditDao.createRecord(vendorInvoicSupportingDocs);
					///this is end /////////
				    authorityDetails = auditDao.savePaymentVendorBillDetails(jsondata);
				}
				else
				{	
					JSONObject nullbalankvalidation = null;
						nullbalankvalidation = ValidateUtils.addAuthorityDetails(jsondata);
						if (nullbalankvalidation.optString("status").equals("0")) {
							return nullbalankvalidation.toString();
						} else {
							authorityDetails = auditDao.saveOrUpdateAuthorityVendorBillDetails(jsondata);
		
						}
				}	
				
				if (authorityDetails != null && authorityDetails.equalsIgnoreCase("Successfully saved")) {
					json.put("msg", "Authority Details Saved successfully ");
					json.put("status", "1");
				} else if (authorityDetails != null && authorityDetails.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", authorityDetails);
					json.put("status", "0");
				}

			} else {
				json.put("status", "0");
				json.put("msg", "json not contain any object");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
		
	}

	@Override
	public String getVendorInvoicePaymentDetail(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            List<VendorInvoicePayment> captureVendorBillDetails = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null && jsondata.has("captureVendorBillDetailId")) {
                Map<String, List<VendorInvoicePayment>> vendorInvoiceApproval = auditDao.getVendorInvoicePaymentDetail(jsondata);
                Date d=null;
                if (vendorInvoiceApproval.get("capturedVendorBillDetails") != null) {
                    captureVendorBillDetails = vendorInvoiceApproval.get("capturedVendorBillDetails");
                    captureVendorBillDetails.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String forwardName = "";
                       
                        if (e != null) {
                            
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String paymentDate = "";
                            if (e.getPaymentDate()!= null)
                            {	
                            	paymentDate = formatter.format(e.getPaymentDate());
                             }

                            mapObj.put("vendorInvoicePaymentId", e.getVendorInvoicePaymentId());
                            mapObj.put("amountPaid", e.getAmountPaid());
                            mapObj.put("invoiceAmount", e.getInvoiceAmount());
                            mapObj.put("modeOfPayment", e.getModeOfPayment());
                            mapObj.put("penaltyAmount", e.getPenaltyAmount());
                            mapObj.put("tdsDeduction", e.getTdsDeduction());
                            mapObj.put("transactionNumber", e.getTransactionNumber());
                            mapObj.put("paymentDate", paymentDate);
                                                     
                            list.add(mapObj);
                        }
                    });

                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
            else {
                json.put("count", 0);
                json.put("msg", "No Record Found");
                json.put("status", 0);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

	@Override
	public String saveFundAllocationDetails(HashMap<String, Object> getJsondata, HttpServletRequest request,
			HttpServletResponse response) {
	    Long fund = null;
		JSONObject json = new JSONObject();
		JSONArray newForm=new JSONArray(getJsondata.get("headMainData").toString());
		JSONObject jsondata = (JSONObject) newForm.get(0);
		String letterName=getJsondata.get("fundLetterName").toString();
		//System.out.println("Payload Opd Data "+jsondata.toString());
		
		try {
			if (null!=jsondata)
			{
				
				if(jsondata.has("actionId"))
				{
					fund = auditDao.saveFundAllocationApprovalDetails(getJsondata,letterName);	
				}
				else
				{	
				fund = auditDao.saveFundAllocationDetails(getJsondata,letterName);
				}
			
              
               //if (opd != null ||oph != null  && opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) {
			if (fund != null && fund!=0) {
					json.put("msg", "Fund Details Saved successfully ");
					json.put("status", "1");
					json.put("fundAllocationHdId", fund);
				} else if (fund != null) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", fund);
					json.put("status", "0");
				}
			
		} else {
			json.put("status", "0");
			json.put("msg", "json not contain any object");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	return json.toString();
	
	}

	@Override
	public String getFundAllocationHdDetails(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<FundAllocationHd> fundAllocationHdDetails = new ArrayList<>();
            List list = new ArrayList();
            List<Map<String, Object>> fundAllocationDtDetailList = new ArrayList<>();
            if (jsondata != null) {
                Map<String, List<FundAllocationHd>> capturedEquipmentChecklist = auditDao.getFundAllocationHdDetails(jsondata);
                List totalMatches = new ArrayList();
                if (capturedEquipmentChecklist.get("fundAllocationDetails") != null) {
                	fundAllocationHdDetails = capturedEquipmentChecklist.get("fundAllocationDetails");
                    totalMatches = capturedEquipmentChecklist.get("totalMatches");
                    fundAllocationHdDetails.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                      
                        String createdBy = "";
                        String auditorName = "";
                        String vendorName = "";
                        String cityName = "";
                        String districtName="";
                        String finanicalYear="";
                        if (e != null) {
                            if (e.getCreatedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, e.getCreatedBy());
                                if (user != null)
                                    createdBy = user.getUserName();
                            }
                            
                            if (e.getFinancialId() != null) {
                                MasStoreFinancial finanYr = (MasStoreFinancial) auditDao.read(MasStoreFinancial.class, e.getFinancialId());
                                if (finanYr != null)
                                	finanicalYear = finanYr.getYearDescription();
                            }
                           
                           
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String createdDate = "";
                            String fundAllocationDate = "";
                            String invoiceDate = "";
                            if (e.getCreatedOn() != null)
                                createdDate = formatter.format(e.getCreatedOn());
                            if(e.getFundAllocationDate()!=null)
                            	fundAllocationDate=formatter.format(e.getFundAllocationDate());
                            	
                            String upssNames = "";
                            String upssId = "";
                            String cityNames = "";
                            String cityId = "";
                            String headNames = "";
                            String headId = "";
                            String allocatedAmount = "";
                            List<Long> ids = new ArrayList<>();
                            ids.add(e.getFundAllocationHdId());
                            Collection<FundAllocationDt> li = auditDao.getFundAllocationDtDetails(ids).values();
                           
                            if(li != null && !li.isEmpty()){
                                for(FundAllocationDt funddetail: li){
                                    Map<String, Object> fundAllocationDetail = new HashMap<>();
                                    MasDistrict masDistrict = (MasDistrict)auditDao.read(MasDistrict.class, funddetail.getDistrictId());
                                    MasCity masCity=null;
                                    if(funddetail.getCityId()!=null)
                                    {	
                                     masCity = (MasCity)auditDao.read(MasCity.class, funddetail.getCityId());
                                    }
                                    MasHeadType masHeadType = (MasHeadType)auditDao.read(MasHeadType.class, funddetail.getHeadTypeId());
                                    fundAllocationDetail.put("allocatedAmount", funddetail.getAllocatedAmount());
                                    fundAllocationDetail.put("fundAllocationHdId", funddetail.getFundAllocationHdId());
                                    if(funddetail.getFundAllocationDtId()!=null)
                                    {	
                                    fundAllocationDetail.put("fundAllocationDtId", funddetail.getFundAllocationDtId());
                                    }
                                    else
                                    {
                                    	fundAllocationDetail.put("fundAllocationDtId", "");	
                                    }
                                    fundAllocationDetail.put("upssName", masDistrict.getDistrictName());
                                    fundAllocationDetail.put("upssId", masDistrict.getDistrictId());
                                    fundAllocationDetail.put("headName", masHeadType.getHeadTypeName());
                                    fundAllocationDetail.put("headId", masHeadType.getHeadTypeId());
                                    fundAllocationDetail.put("allocatedAmount", allocatedAmount);
                                    fundAllocationDetail.put("fundAllocationHdId", e.getFundAllocationHdId());
                                    fundAllocationDetail.put("fileName", e.getFileName());
                                    fundAllocationDetail.put("createdBy", createdBy);
                                    fundAllocationDetail.put("createdDate", createdDate);
                                    
                                    if(e.getLetterNo()!=null)
                                    {
                                    	fundAllocationDetail.put("letterNo", e.getLetterNo()); 	
                                    }
                                    else
                                    {
                                    	fundAllocationDetail.put("letterNo",""); 	
                                    }
                                    if(e.getTotalAmount()!=null)
                                    {
                                    	fundAllocationDetail.put("totalAmount", e.getTotalAmount());
                                    }
                                    else
                                    {
                                    	fundAllocationDetail.put("totalAmount", "");	
                                    }
                                    fundAllocationDtDetailList.add(fundAllocationDetail);
                                if(masDistrict != null) {
                                	upssNames += masDistrict.getDistrictName() + ",";
                                	upssId += masDistrict.getDistrictId()+ ",";
                                    }
                                if(masCity != null) {
                                	cityName += masCity.getCityName() + ",";
                                	cityId += masCity.getCityId()+ ",";
                                    }
                                if(masHeadType != null) {
                                	headNames += masHeadType.getHeadTypeName() + ",";
                                	headId += masHeadType.getHeadTypeId()+ ",";
                                    }
                                if(funddetail.getAllocatedAmount() != null) {
                                	allocatedAmount += funddetail.getAllocatedAmount()+ ",";
                                	
                                    }
                                }
                                upssNames = upssNames.substring(0, upssNames.length()-1);
                                upssId = upssId.substring(0, upssId.length()-1);
                                headNames = headNames.substring(0, headNames.length()-1);
                                headId = headId.substring(0, headId.length()-1);
                                allocatedAmount = allocatedAmount.substring(0, allocatedAmount.length()-1);
                            }
                               
                            mapObj.put("fundAllocationHdId", e.getFundAllocationHdId());
                            mapObj.put("fileName", e.getFileName());
                            mapObj.put("createdBy", createdBy);
                            mapObj.put("createdDate", createdDate);
                            mapObj.put("cityName", cityName);
                            mapObj.put("upssId", upssId);
                            mapObj.put("upssNames", upssNames);
                            mapObj.put("headNames", headNames);
                            mapObj.put("headId", headId);
                            mapObj.put("allocatedAmount", allocatedAmount);
                            mapObj.put("finanicalYear", finanicalYear);
                            mapObj.put("status", e.getStatus());
                            mapObj.put("fundAllocationDate", fundAllocationDate);
                            mapObj.put("cityId", cityId);
                            mapObj.put("cityName", cityName);
                            mapObj.put("phaseVal", e.getPhase());
                            if(e.getLetterNo()!=null)
                            {
                            	mapObj.put("letterNo", e.getLetterNo()); 	
                            }
                            else
                            {
                            	mapObj.put("letterNo",""); 	
                            }
                            if(e.getTotalAmount()!=null)
                            {
                            	mapObj.put("totalAmount", e.getTotalAmount());
                            }
                            else
                            {
                            	mapObj.put("totalAmount", "");	
                            }
                           
                            list.add(mapObj);
                        }
                    });
                        
                    if (list != null && fundAllocationDtDetailList.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

	@Override
	public Long getFundAvailableBalance(JSONObject jsonObject) {
		
		  //Long availableBalance=fundHcbDao.getFundOpernationalBalance(Long.parseLong(jsonObject.getString("upssId")), Long.parseLong(jsonObject.getString("headTypeId")));
		  Map<String, Object> nextAuthorityDetails=fundHcbDao.getFundOpernationalUpdatedBalance(Long.parseLong(jsonObject.getString("upssId")), Long.parseLong(jsonObject.getString("cityId")),Long.parseLong(jsonObject.getString("headTypeId")),jsonObject.getString("phase"));
		  Long  interestAmount =0l;
		   interestAmount = fundHcbDao.getCaptureHdCityPhaseLevel(Long.parseLong(jsonObject.getString("upssId")), Long.parseLong(jsonObject.getString("cityId")),Long.parseLong(jsonObject.getString("headTypeId")),jsonObject.getString("phase") );
		
		  Long availableBalance = 0l;
			 List<Object[]> listCamp = (List<Object[]>) nextAuthorityDetails.get("list");
				for (Object[] row : listCamp) {
					if (row[0]!= null) {
						availableBalance=Long.parseLong(row[0].toString());
					
						
					}
				
				 }
				availableBalance=availableBalance+interestAmount;
		// TODO Auto-generated method stub
		return availableBalance;
	}
	
	public Map<String, Integer> getFundDetails(Long invoiceAmount,Long districtId,Long cityId,String phase){
		Map<String, Integer> map = new HashMap<>();
		String status = "Status";
		//Long invoiceAmount = captureInvoices.getInvoiceDetails().stream().mapToLong(InvoiceDetail::getInoviceAmount).sum();
		FundHcb lasthcbData = fundHcbDao.getFundHcbCityLevel(districtId,cityId, HEAD_TYPE_ID,phase);
		
		
		if (lasthcbData != null && invoiceAmount < lasthcbData.getHcbBalance()) {
			map.put(status, 0);
		}
		try {
			FundHcb fundDetails = fundHcbDao.getFundHcbCity(districtId,cityId, HEAD_TYPE_ID, HMSUtil.getTodayFormattedDate(),phase);
			if(fundDetails!=null) {
				fundDetails.setOpeningBalance(fundDetails.getHcbBalance());
				fundDetails.setDebitCredit(0-invoiceAmount);
				fundDetails.setHcbBalance(fundDetails.getHcbBalance()-invoiceAmount);
				fundDetails.setPhase(phase);
				fundHcbDao.updateFundHcb(fundDetails);
			}else {
				FundHcb fundHcb = new FundHcb();
				fundHcb.setPhase(phase);
				fundHcb.setUpssId(districtId);
				fundHcb.setCityId(cityId);
				fundHcb.setHeadTypeId(HEAD_TYPE_ID);
				fundHcb.setHcbDate(HMSUtil.getTodayFormattedDate());
				fundHcb.setOpeningBalance(lasthcbData.getHcbBalance());
				fundHcb.setDebitCredit(0-invoiceAmount);
				fundHcb.setHcbBalance( lasthcbData.getHcbBalance()-invoiceAmount);				
				fundHcbDao.saveFundHcb(fundHcb);
			}
			map.put(status, 1);
		} catch (Exception e) {
			map.put(status, 2);
		}
		
		return map;
	}
	
	@Override
	public String getDgFundAllcationHdDt(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object[]> listObject = null;
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

				if (jsondata.get("fundAllocationHdId") != null) {
					listObject = auditDao.getDgFundAllcationHdDt(Long.parseLong(jsondata.get("fundAllocationHdId").toString()));
				}
				Long fundAllocationHdId = null;
				Long finanicalId = null;
				String finanicalIdText="";
				Date finanicalYearStartDate=null;
				Date finanicalYearEndDate=null;
				String fundAllocationDate="";
				String letterNo = "";
				Long totalAmount=null;
				String fileName = "";
				Long fundAllocationDtId = null;
				Long districtId = null;
				Long headTypeId = null;
				Long allocatedAmount = null;
				String status="";
				String createdOn="";
				String createdBy="";
				Long createdUserId=null;
				Long cityId=null;
				String cityName=null;
				String phase=null;
				//String orderStustus="";
				if (listObject != null) {
					try {
						for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
							Object[] row = (Object[]) it.next();

							/*
							 * for(Object dgMasInvestigation : listObject) { Object row=dgMasInvestigation;
							 */
							HashMap<String, Object> pt = new HashMap<String, Object>();

							if (row[0] != null) {
								fundAllocationHdId = Long.parseLong(row[0].toString());
							}
							if (row[1] != null) {
								finanicalId = Long.parseLong(row[1].toString());
								MasStoreFinancial masStoreFinicial = (MasStoreFinancial) auditDao.read(MasStoreFinancial.class, finanicalId);
                                if (masStoreFinicial != null)
                                	finanicalIdText = masStoreFinicial.getFinancialYear();
                                finanicalYearStartDate=masStoreFinicial.getStartDate();
                                finanicalYearEndDate=masStoreFinicial.getEndDate();
							}

							if (row[2] != null) {
								fundAllocationDate = row[2].toString();
								Date dd1 = HMSUtil.dateFormatteryyyymmdd(fundAllocationDate);
								fundAllocationDate = HMSUtil.convertDateToStringFormat(
										dd1, "dd/MM/yyyy");
							}

						

							if (row[3] != null) {
								letterNo = row[3].toString();
								
							}
							if (row[4] != null) {
								totalAmount = Long.parseLong(row[4].toString());
							}
						
							if (row[5] != null) {
								fileName = row[5].toString();
							}
							if (row[6] != null) {
								fundAllocationDtId = Long.parseLong(row[6].toString());
							}
							if (row[7] != null) {
								districtId = Long.parseLong(row[7].toString());
							}
							if (row[8] != null) {
								headTypeId =  Long.parseLong(row[8].toString());
							}
							if (row[9] != null) {
								allocatedAmount =  Long.parseLong(row[9].toString());
							}
							if (row[11] != null) {
								status =  row[11].toString();
							}
							if (row[12] != null) {
								 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
								 createdOn = formatter.format(row[12]);
							}
							if (row[13] != null) {
								Long userId=Long.parseLong(row[13].toString());
								createdUserId=Long.parseLong(row[13].toString());
						        Users user = (Users) auditDao.read(Users.class, userId);
                                if (user != null)
                                    createdBy = user.getUserName();
                            
							}
							if (row[14] != null) {
								cityId =  Long.parseLong(row[14].toString());
								MasCity city = (MasCity) auditDao.read(MasCity.class, cityId);
                                if (city != null)
                                    cityName = city.getCityName();
                            
							}
							if (row[15] != null) {
								phase =  row[15].toString();
						         
							}
							pt.put("fundAllocationHdId", fundAllocationHdId);
							pt.put("finanicalId", finanicalId);
							pt.put("fundAllocationDate", fundAllocationDate);
							pt.put("letterNo", letterNo);
							pt.put("totalAmount", totalAmount);
							pt.put("fileName", fileName);
							pt.put("phaseVal", phase);
							if(fundAllocationDtId!=null)
							{	
							pt.put("fundAllocationDtId", fundAllocationDtId);
							}
							else
							{
								pt.put("fundAllocationDtId", "");	
							}
							pt.put("districtId", districtId);
							pt.put("headTypeId", headTypeId);
							pt.put("allocatedAmount", allocatedAmount);
							pt.put("status", status);
							pt.put("createdOn", createdOn);
							pt.put("createdBy", createdBy);
							pt.put("createdUserId", createdOn);
							pt.put("createdUserId", createdUserId);
							pt.put("cityId", cityId);
							pt.put("cityName", cityName);
							pt.put("finanicalIdText", finanicalIdText);
							pt.put("finanicalYearStartDate", finanicalYearStartDate);
							pt.put("finanicalYearEndDate", finanicalYearEndDate);
							c.add(pt);
						}
						json.put("listObject", c);
						json.put("msg", "Fund Allocation List  get  sucessfull... ");
						json.put("status", "1");

					}

					catch (Exception e) {
						e.printStackTrace();
						return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
				} else {
					try {
						json.put("msg", "Visit ID data not found");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			
		}
		return json.toString();

	}
	
	@Override
	public String deleteFundAllocationDtDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String fundAllocationDt = null;
		try {
			if (!jsondata.isEmpty()) {
				
				fundAllocationDt = auditDao.deleteFundAllocationDtDetails(jsondata);
		// TODO Auto-generated method stub
				if (fundAllocationDt != null && fundAllocationDt.equalsIgnoreCase("statusUpdated")) {
					json.put("msg", "recordsDeleted");
					json.put("status", "1");
				} else if (fundAllocationDt != null && fundAllocationDt.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", fundAllocationDt);
					json.put("status", "0");
				}

			} else {
				json.put("status", "0");
				json.put("msg", "json not contain any object");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String getFundAllocationAmount(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)auditDao.getFundAllocationAmount(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("asp_fund_allocation");
		jsonResponse.put("asp_fund_allocation", result);
		return jsonResponse.toString(); 
}

	@Override
	public Long getPenaltyAuthorityDetailsByUpss(HashMap<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		
		  //Long availableBalance=fundHcbDao.getFundOpernationalBalance(Long.parseLong(jsonObject.getString("upssId")), Long.parseLong(jsonObject.getString("headTypeId")));
		  Map<String, Object> nextAuthorityDetails=auditDao.getPenaltyAuthId(Long.parseLong(map.get("upssId").toString()));
			 Long availableBalance = 0l;
			 List<Object[]> listCamp = (List<Object[]>) nextAuthorityDetails.get("list");
				for (Object[] row : listCamp) {
					if (row[0]!= null) {
						availableBalance=Long.parseLong(row[0].toString());
					
						
					}
				
				 }
		// TODO Auto-generated method stub
		return availableBalance;
	}
	
	
	 
	@Override
	public String saveCaptureInterestDetails(HashMap<String, Object> getJsondata, HttpServletRequest request,
			HttpServletResponse response) {
	    Long captureInterestHdId = null;
		JSONObject json = new JSONObject();
		JSONArray newForm=new JSONArray(getJsondata.get("headMainData").toString());
		JSONObject jsondata = (JSONObject) newForm.get(0);
		//String letterName=getJsondata.get("fundLetterName").toString();
		//System.out.println("Payload Opd Data "+jsondata.toString());
		
		try {
			if (null!=jsondata)
			{
				
				if(jsondata.has("actionId"))
				{
					captureInterestHdId = auditDao.saveCaptureInterestDetails(getJsondata);	
				}
				
				  else { 
					  captureInterestHdId = auditDao.saveCaptureInterestDetails(getJsondata); }
				 
			
              
               //if (opd != null ||oph != null  && opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) {
			if (captureInterestHdId != null && captureInterestHdId!=0) {
					json.put("msg", captureInterestHdId);
					json.put("status", "1");
					json.put("fundAllocationHdId", captureInterestHdId);
				} else if (captureInterestHdId != null) {
					json.put("msg", captureInterestHdId);
					json.put("status", "0");
				} else {
					json.put("msg", captureInterestHdId);
					json.put("status", "0");
				}
			
		} else {
			json.put("status", "0");
			json.put("msg", "json not contain any object");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	return json.toString();
	
	}
	
	@Override
	public String getAllCaptureInterestDetails (JSONObject jsondata){ 

        JSONObject json = new JSONObject();
        try {
        	List<CaptureInterestHd> captureInterestDetails = new ArrayList<>();
            List list = new ArrayList();
            List<Map<String, Object>> captureInterestDetailList = new ArrayList<>();
              if (jsondata != null) {
            	  Map<String, List<CaptureInterestHd>> capturedEquipmentChecklist = auditDao.getAllCaptureInterestDetails(jsondata);
                List totalMatches = new ArrayList();
                if (capturedEquipmentChecklist.get("captureInterestDetails") != null) {
                	captureInterestDetails = capturedEquipmentChecklist.get("captureInterestDetails");
                    totalMatches = capturedEquipmentChecklist.get("totalMatches");
               
                    captureInterestDetails.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                      
                        String createdBy = "";
                        String auditorName = "";
                        String vendorName = "";
                        String cityName = "";
                        String districtName="";
                        String finanicalYear="";
                        if (e != null) {
                            if (e.getCreatedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, e.getCreatedBy());
                                if (user != null)
                                    createdBy = user.getUserName();
                            }
                            
                            if (e.getFinancialId() != null) {
                                MasStoreFinancial finanYr = (MasStoreFinancial) auditDao.read(MasStoreFinancial.class, e.getFinancialId());
                                if (finanYr != null)
                                	finanicalYear = finanYr.getYearDescription();
                            }
                           
                           
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String createdDate = "";
                            String fundAllocationDate = "";
                            String invoiceDate = "";
                            if (e.getCreatedOn() != null)
                                createdDate = formatter.format(e.getCreatedOn());
                             	
                            String upssNames = "";
                            String upssId = "";
                            String cityNames = "";
                            String cityId = "";
                            String headNames = "";
                            String headId = "";
                            String allocatedAmount = "";
                            List<Long> ids = new ArrayList<>();
                            ids.add(e.getCaptureInterestHdId());
                            Collection<CaptureInterestDt> li = auditDao.getCaptureInterestDtDetails(ids).values();
                           
                            if(li != null && !li.isEmpty()){
                                for(CaptureInterestDt funddetail: li){
                                    Map<String, Object> fundAllocationDetail = new HashMap<>();
                                    MasDistrict masDistrict = (MasDistrict)auditDao.read(MasDistrict.class, funddetail.getDistrictId());
                                    MasCity masCity=null;
                                    if(funddetail.getCityId()!=null)
                                    {	
                                     masCity = (MasCity)auditDao.read(MasCity.class, funddetail.getCityId());
                                    }
                                    MasHeadType masHeadType = (MasHeadType)auditDao.read(MasHeadType.class, funddetail.getHeadTypeId());
                                    
                                    fundAllocationDetail.put("interest", funddetail.getInterest());
                                    fundAllocationDetail.put("captureInterestHdId", funddetail.getCaptureInterestHdId());
                                   
                                    if(funddetail.getCaptureInterestDtId()!=null)
                                    {	
                                    fundAllocationDetail.put("captureInterestDtId", funddetail.getCaptureInterestDtId());
                                    }
                                    else
                                    {
                                    	fundAllocationDetail.put("captureInterestDtId", "");	
                                    }
                                    fundAllocationDetail.put("upssName", masDistrict.getDistrictName());
                                    fundAllocationDetail.put("upssId", masDistrict.getDistrictId());
                                    fundAllocationDetail.put("headName", masHeadType.getHeadTypeName());
                                    fundAllocationDetail.put("headId", masHeadType.getHeadTypeId());
                                    fundAllocationDetail.put("allocatedAmount", allocatedAmount);
                                   // fundAllocationDetail.put("fundAllocationHdId", e.getFundAllocationHdId());
                                   // fundAllocationDetail.put("fileName", e.getFileName());
                                    fundAllocationDetail.put("createdBy", createdBy);
                                    fundAllocationDetail.put("createdDate", createdDate);
                                    
                                     
                                if(masDistrict != null) {
                                	upssNames += masDistrict.getDistrictName() + ",";
                                	upssId += masDistrict.getDistrictId()+ ",";
                                    }
                                if(masCity != null) {
                                	cityName += masCity.getCityName() + ",";
                                	cityId += masCity.getCityId()+ ",";
                                    }
                                if(masHeadType != null) {
                                	headNames += masHeadType.getHeadTypeName() + ",";
                                	headId += masHeadType.getHeadTypeId()+ ",";
                                    }
                                if(funddetail.getInterest() != null) {
                                	allocatedAmount += funddetail.getInterest()+ ",";
                                	
                                    }
                                }
                                upssNames = upssNames.substring(0, upssNames.length()-1);
                                upssId = upssId.substring(0, upssId.length()-1);
                                headNames = headNames.substring(0, headNames.length()-1);
                                headId = headId.substring(0, headId.length()-1);
                                allocatedAmount = allocatedAmount.substring(0, allocatedAmount.length()-1);
                            }
                               
                            mapObj.put("captureInterestHdId", e.getCaptureInterestHdId());
                             mapObj.put("createdBy", createdBy);
                            mapObj.put("createdDate", createdDate);
                            mapObj.put("cityName", cityName);
                            mapObj.put("upssId", upssId);
                            mapObj.put("upssNames", upssNames);
                            mapObj.put("headNames", headNames);
                            mapObj.put("headId", headId);
                            mapObj.put("allocatedAmount", allocatedAmount);
                            mapObj.put("finanicalYear", finanicalYear);
                            mapObj.put("status", e.getStatus());
                            mapObj.put("fundAllocationDate", fundAllocationDate);
                            mapObj.put("cityId", cityId);
                            mapObj.put("phaseVal", e.getPhase());
                            mapObj.put("cityName", cityName);
                            
                            list.add(mapObj);
                        }
                    });
                        
                    if (list != null && capturedEquipmentChecklist.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    
	}
	
	
	//@Override
	public String getAllCaptureInterestDetailsOld(JSONObject jsondata){
        JSONObject json = new JSONObject();
        try {
            List<CaptureInterestHd> captureInterestDetails = new ArrayList<>();
            List list = new ArrayList();
            List<Map<String, Object>> captureInterestDetailList = new ArrayList<>();
            if (jsondata != null) {
                Map<String, List<CaptureInterestHd>> capturedEquipmentChecklist = auditDao.getAllCaptureInterestDetails(jsondata);
                List totalMatches = new ArrayList();
                if (capturedEquipmentChecklist.get("captureInterestDetails") != null) {
                	captureInterestDetails = capturedEquipmentChecklist.get("captureInterestDetails");
                    totalMatches = capturedEquipmentChecklist.get("totalMatches");
                    captureInterestDetails.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                      
                        String createdBy = "";
                     
                       
                        String cityName = "";
                        String districtName="";
                        String finanicalYear="";
                        String approvedBy="";
                        String upssNames = "";
                        String upssId = "";
                        String cityNames = "";
                        String cityId = "";
                        String headNames = "";
                        String headId = "";
                  
                        String createdDate = "";
                        String approvedDate = "";
                        String invoiceDate = "";
                        if (e != null) {
                           /* if (e.getCreateBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, e.getCreateBy());
                                if (user != null)
                                    createdBy = user.getUserName();
                            }*/
                            if (e.getApprovedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, e.getApprovedBy());
                                if (user != null)
                                	approvedBy = user.getUserName();
                            }
                            
                            if (e.getFinancialId() != null) {
                                MasStoreFinancial finanYr = (MasStoreFinancial) auditDao.read(MasStoreFinancial.class, e.getFinancialId());
                                if (finanYr != null)
                                	finanicalYear = finanYr.getYearDescription();
                            }
                           
                           
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                           
                            if (e.getCreatedOn() != null)
                                createdDate = formatter.format(e.getCreatedOn());
                            
                            if(e.getApprovedDate()!=null)
                            	approvedDate=formatter.format(e.getApprovedDate());
                              
                           /* MasDistrict masDistrict = (MasDistrict)auditDao.read(MasDistrict.class, e.getDistrictId());
                            MasCity masCity=null;
                            if(e.getCityId()!=null)
                            {	
                             masCity = (MasCity)auditDao.read(MasCity.class, e.getCityId());
                            }
                            MasHeadType masHeadType = (MasHeadType)auditDao.read(MasHeadType.class, e.getHeadTypeId());
                            mapObj.put("interest", e.getInterest());
                    
                            mapObj.put("captureInterestId", e.getCaptureInterestId());
                    
                            if(masDistrict != null) {
                            	upssNames += masDistrict.getDistrictName() + ",";
                            	upssId += masDistrict.getDistrictId()+ ",";
                                }
                            if(masCity != null) {
                            	cityName += masCity.getCityName() + ",";
                            	cityId += masCity.getCityId()+ ",";
                                }
                            if(masHeadType != null) {
                            	headNames += masHeadType.getHeadTypeName() + ",";
                            	headId += masHeadType.getHeadTypeId()+ ",";
                                }
                            
                             */
                            upssNames = upssNames.substring(0, upssNames.length()-1);
                            upssId = upssId.substring(0, upssId.length()-1);
                            headNames = headNames.substring(0, headNames.length()-1);
                            headId = headId.substring(0, headId.length()-1);
                            mapObj.put("createdBy", createdBy);
                            mapObj.put("createdDate", createdDate);
                            mapObj.put("approvedBy", approvedBy);
                            mapObj.put("approvedDate", approvedDate);
                            mapObj.put("cityName", cityName);
                            mapObj.put("upssId", upssId);
                             
                            mapObj.put("finanicalYear", finanicalYear);
                            mapObj.put("status", e.getStatus());
                            
                              list.add(mapObj);
                        }
                    });
                        
                    if (list != null && list.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }
	
	
	@Override
	public String getCaptureInterestHdDt(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object[]> listObject = null;
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

				if (jsondata.get("captureInterestHdId") != null) {
					listObject = auditDao.getCaptureInterestHdDt(Long.parseLong(jsondata.get("captureInterestHdId").toString()));
				}
				Long captureInterestHdId = null;
				Long finanicalId = null;
				String finanicalIdText="";
				Date finanicalYearStartDate=null;
				Date finanicalYearEndDate=null;
				String fundAllocationDate="";
				//String letterNo = "";
				//Long totalAmount=null;
				//String fileName = "";
				Long captureInterestDtId = null;
				Long districtId = null;
				Long headTypeId = null;
				Long interest = null;
				String status="";
				String createdOn="";
				String createdBy="";
				Long createdUserId=null;
				Long cityId=null;
				String cityName=null;
				String phase=null;
				//String orderStustus="";
				if (listObject != null) {
					try {
						for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
							Object[] row = (Object[]) it.next();

							/*
							 * for(Object dgMasInvestigation : listObject) { Object row=dgMasInvestigation;
							 */
							HashMap<String, Object> pt = new HashMap<String, Object>();
							 /*sbQuery.append(" select fah.capture_interest_hd_id,fah.financial_id,fah.created_on ");
							    sbQuery.append( "fad.capture_interest_dt_id,fad.district_id,fad.head_type_id,fad.interest,");
							    sbQuery.append(" fad.allocation_flag,fah.status,fah.created_on,fah.created_by,fad.city_id  from " +capture_interest_hd+ " fah "); 
							  */
							if (row[0] != null) {
								captureInterestHdId = Long.parseLong(row[0].toString());
							}
							if (row[1] != null) {
								finanicalId = Long.parseLong(row[1].toString());
								MasStoreFinancial masStoreFinicial = (MasStoreFinancial) auditDao.read(MasStoreFinancial.class, finanicalId);
                                if (masStoreFinicial != null)
                                	finanicalIdText = masStoreFinicial.getFinancialYear();
                                finanicalYearStartDate=masStoreFinicial.getStartDate();
                                finanicalYearEndDate=masStoreFinicial.getEndDate();
							}

							if (row[2] != null) {
								fundAllocationDate = row[2].toString();
								Date dd1 = HMSUtil.dateFormatteryyyymmdd(fundAllocationDate);
								fundAllocationDate = HMSUtil.convertDateToStringFormat(
										dd1, "dd/MM/yyyy");
							}

						

							 
							if (row[3] != null) {
								captureInterestDtId = Long.parseLong(row[3].toString());
							}
							if (row[4] != null) {
								districtId = Long.parseLong(row[4].toString());
							}
							if (row[5] != null) {
								headTypeId =  Long.parseLong(row[5].toString());
							}
							if (row[6] != null) {
								interest =  Long.parseLong(row[6].toString());
							}
						
							if (row[8] != null) {
								status =  row[8].toString();
							}
							
							if (row[9] != null) {
								 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
								 createdOn = formatter.format(row[9]);
							}
							if (row[10] != null) {
								Long userId=Long.parseLong(row[10].toString());
								createdUserId=Long.parseLong(row[10].toString());
						        Users user = (Users) auditDao.read(Users.class, userId);
                                if (user != null)
                                    createdBy = user.getUserName();
                            
							}
							if (row[11] != null) {
								cityId =  Long.parseLong(row[11].toString());
								MasCity city = (MasCity) auditDao.read(MasCity.class, cityId);
                                if (city != null)
                                    cityName = city.getCityName();
                            
							}
							if (row[12] != null) {
								phase =   row[12].toString();
							}
							 
							
							pt.put("captureInterestHdId", captureInterestHdId);
							pt.put("finanicalId", finanicalId);
							pt.put("fundAllocationDate", fundAllocationDate);
							 
							 
							if(captureInterestDtId!=null)
							{	
							pt.put("captureInterestDtId", captureInterestDtId);
							}
							else
							{
								pt.put("captureInterestDtId", "");	
							}
							pt.put("districtId", districtId);
							pt.put("headTypeId", headTypeId);
							pt.put("interest", interest);
							pt.put("status", status);
							pt.put("createdOn", createdOn);
							pt.put("createdBy", createdBy);
							pt.put("createdUserId", createdOn);
							pt.put("createdUserId", createdUserId);
							pt.put("cityId", cityId);
							pt.put("cityName", cityName);
							pt.put("finanicalIdText", finanicalIdText);
							pt.put("finanicalYearStartDate", finanicalYearStartDate);
							pt.put("finanicalYearEndDate", finanicalYearEndDate);
							pt.put("phaseVal", phase);
							
							c.add(pt);
						}
						json.put("listObject", c);
						json.put("msg", "Fund Allocation List  get  sucessfull... ");
						json.put("status", "1");

					}

					catch (Exception e) {
						e.printStackTrace();
						return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
				} else {
					try {
						json.put("msg", "Visit ID data not found");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			
		}
		return json.toString();

	}
	@Override
	public String deleteCaptureInterestDtDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String fundAllocationDt = null;
		try {
			if (!jsondata.isEmpty()) {
				
				fundAllocationDt = auditDao.deleteCaptureInterestDtDetails(jsondata);
		// TODO Auto-generated method stub
				if (fundAllocationDt != null && fundAllocationDt.equalsIgnoreCase("statusUpdated")) {
					json.put("msg", "recordsDeleted");
					json.put("status", "1");
				} else if (fundAllocationDt != null && fundAllocationDt.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", fundAllocationDt);
					json.put("status", "0");
				}

			} else {
				json.put("status", "0");
				json.put("msg", "json not contain any object");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String saveUCDocumentUploadDetails(HashMap<String, Object> getJsondata, HttpServletRequest request,
			HttpServletResponse response) {
	    Long fund = null;
		JSONObject json = new JSONObject();
		JSONArray newForm=new JSONArray(getJsondata.get("headMainData").toString());
		JSONObject jsondata = (JSONObject) newForm.get(0);
		String letterName=getJsondata.get("fundLetterName").toString();
		//System.out.println("Payload Opd Data "+jsondata.toString());
		
		try {
			if (null!=jsondata)
			{
				//Long checkCount=auditDao.getCountUCUploadHd(Long.parseLong(jsondata.get("upssId").toString()),Long.parseLong(jsondata.get("financialYear").toString()),jsondata.get("phaseVal").toString());
				//if(checkCount<2 || !jsondata.get("ucUploadHdId").equals("") )
				//{	
				fund = auditDao.saveUCDocumentUploadDetails(getJsondata,letterName);
				//}
				//else
				//{
					//fund=2l;
					
				//}
		
               //if (opd != null ||oph != null  && opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) {
			if (fund != null && fund!=0 && fund!=2) {
					json.put("msg", "Fund Details Saved successfully ");
					json.put("status", "1");
					json.put("fundAllocationHdId", fund);
				}
				/*else if (fund != null && fund==2) {
					json.put("msg", "2 UCs are already uploaded for the selected UPSS,Phase and Year!");
					json.put("status", "2");
				}*/
				else if (fund != null) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", fund);
					json.put("status", "0");
				}
			
		} else {
			json.put("status", "0");
			json.put("msg", "json not contain any object");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	return json.toString();
	
	}

	@Override
	public String getUcUploadDocumentDetail(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            List<UcUploadHd> fundAllocationHdDetails = new ArrayList<>();
            List list = new ArrayList();
            List<Map<String, Object>> fundAllocationDtDetailList = new ArrayList<>();
            if (jsondata != null) {
                Map<String, List<UcUploadHd>> capturedEquipmentChecklist = auditDao.getUcUploadDocumentDetail(jsondata);
                List totalMatches = new ArrayList();
                if (capturedEquipmentChecklist.get("fundAllocationDetails") != null) {
                	fundAllocationHdDetails = capturedEquipmentChecklist.get("fundAllocationDetails");
                    totalMatches = capturedEquipmentChecklist.get("totalMatches");
                    fundAllocationHdDetails.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                      
                        String createdBy = "";
                        String auditorName = "";
                        String vendorName = "";
                        String cityName = "";
                        String districtName="";
                        String finanicalYear="";
                        if (e != null) {
                            if (e.getCreatedBy() != null) {
                                Users user = (Users) auditDao.read(Users.class, e.getCreatedBy());
                                if (user != null)
                                    createdBy = user.getUserName();
                            }
                            
                            if (e.getFinancialId() != null) {
                                MasStoreFinancial finanYr = (MasStoreFinancial) auditDao.read(MasStoreFinancial.class, e.getFinancialId());
                                if (finanYr != null)
                                	finanicalYear = finanYr.getYearDescription();
                            }
                            MasDistrict masDistrict = (MasDistrict)auditDao.read(MasDistrict.class, e.getUpssId());
                            String upssNames = "";
                            Long upssId;
                            upssId=e.getUpssId();
                            upssNames=masDistrict.getUpss();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String createdDate = "";
                            String entryDate = "";
                            String fundAllocationDate = "";
                            String invoiceDate = "";
                            if (e.getUploadDate() != null)
                                createdDate = formatter.format(e.getUploadDate());
                            if (e.getEntryDate()!= null)
                                entryDate = formatter.format(e.getEntryDate());
                         
                           
                            String headNames = "";
                            String headId = "";
                            String allocatedAmount = "";
                            List<Long> ids = new ArrayList<>();
                            ids.add(e.getUcUploadHdId());
                            Collection<UcUploadDt> li = auditDao.getUcUploadDtDetails(ids).values();
                           
                            if(li != null && !li.isEmpty()){
                                for(UcUploadDt funddetail: li){
                                    Map<String, Object> fundAllocationDetail = new HashMap<>();
                                    MasHeadType masHeadType = (MasHeadType)auditDao.read(MasHeadType.class, funddetail.getHeadTypeId());
                                   
                                    fundAllocationDetail.put("availableBalance()", funddetail.getAvailableBalance());
                                    fundAllocationDetail.put("availableUtilization", funddetail.getAvailableUtilization());
                                    if(funddetail.getUcUploadDtId()!=null)
                                    {	
                                    fundAllocationDetail.put("ucUploadDtId", funddetail.getUcUploadDtId());
                                    }
                                    else
                                    {
                                    	fundAllocationDetail.put("ucUploadDtId", "");	
                                    }
                                                                   
                                    if(e.getFileName()!=null)
                                    {
                                    	fundAllocationDetail.put("fileName", e.getFileName()); 	
                                    }
                                    else
                                    {
                                    	fundAllocationDetail.put("fileName",""); 	
                                    }
                                    
                                    fundAllocationDtDetailList.add(fundAllocationDetail);
                                
                                if(masHeadType != null) {
                                	headNames += masHeadType.getHeadTypeName() + ",";
                                	headId += masHeadType.getHeadTypeId()+ ",";
                                    }
                              
                              
                                headNames = headNames.substring(0, headNames.length()-1);
                                headId = headId.substring(0, headId.length()-1);
                               
                            }
                               
                            mapObj.put("fundAllocationHdId", e.getUcUploadHdId());
                            mapObj.put("fileName", e.getFileName());
                            mapObj.put("createdBy", createdBy);
                            mapObj.put("entryDate", entryDate);
                            mapObj.put("createdDate", createdDate);
                            mapObj.put("upssId", upssId);
                            mapObj.put("upssNames", upssNames);
                            mapObj.put("headNames", headNames);
                            mapObj.put("headId", headId);
                            mapObj.put("availableBalance", allocatedAmount);
                            mapObj.put("availableUtilization", allocatedAmount);
                            mapObj.put("finanicalYear", finanicalYear);
                            mapObj.put("phaseVal", e.getPhase());
                            mapObj.put("status", e.getUploadFlag());
                            if(e.getFileName()!=null)
                            {
                            	mapObj.put("fileName", e.getFileName()); 	
                            }
                            else
                            {
                            	mapObj.put("letterNo",""); 	
                            }
                           
                            }
                            list.add(mapObj);
                        }
                    });
                        
                    if (list != null && fundAllocationDtDetailList.size() > 0) {
                        json.put("data", list);
                        json.put("count", totalMatches.size());
                        json.put("msg", "Get Record successfully");
                        json.put("status", 1);
                    } else {
                        json.put("data", list);
                        json.put("count", 0);
                        json.put("msg", "No Record Found");
                        json.put("status", 0);
                    }

                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return json.toString();
    }

	@Override
	public String getUcUploadDocumentHdDt(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object[]> listObject = null;
		List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
		if (!jsondata.isEmpty()) {

				if (jsondata.get("ucUploadHdId") != null) {
					listObject = auditDao.getUcUploadDocumentHdDt(Long.parseLong(jsondata.get("ucUploadHdId").toString()));
				}
				Long ucUploadHdId = null;
				Long upssId = null;
				String phase=null;
				String uploadDate=null;
				String fileName = "";
				Long createdBy=null;
				String remarks="";
				Long finanicalId = null;
				String finanicalIdText="";
				Date finanicalYearStartDate=null;
				Date finanicalYearEndDate=null;
				String fundAllocationDate="";
				String entryDate="";
				Long ucUploadDtId = null;
				Long headTypeId = null;
				Long availableBalance = null;
				Long availableUtilization = null;
				String status="";
				String createdOn="";
				String certificateNo="";
				Long createdUserId=null;
				Long cityId=null;
				String cityName=null;
				
				//String orderStustus="";
				if (listObject != null) {
					try {
						for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
							Object[] row = (Object[]) it.next();

							/*
							 * for(Object dgMasInvestigation : listObject) { Object row=dgMasInvestigation;
							 */
							HashMap<String, Object> pt = new HashMap<String, Object>();

							if (row[0] != null) {
								ucUploadHdId = Long.parseLong(row[0].toString());
							}
							if (row[1] != null) {
								
								upssId = Long.parseLong(row[1].toString());
								
							}

							if (row[2] != null) {
								phase = row[2].toString();
								/*fundAllocationDate = row[2].toString();
								Date dd1 = HMSUtil.dateFormatteryyyymmdd(fundAllocationDate);
								fundAllocationDate = HMSUtil.convertDateToStringFormat(
										dd1, "dd/MM/yyyy");*/
							}

						

							if (row[3] != null) {
								uploadDate = row[3].toString();
								Date dd1 = HMSUtil.dateFormatteryyyymmdd(uploadDate);
								uploadDate = HMSUtil.convertDateToStringFormat(
										dd1, "dd/MM/yyyy");
								
							}
							if (row[4] != null) {
								fileName = row[4].toString();
							}
						
							if (row[5] != null) {
								createdBy = Long.parseLong(row[5].toString());
							}
							if (row[6] != null) {
								remarks = row[6].toString();
							}
							if (row[7] != null) {
								finanicalId = Long.parseLong(row[7].toString());
								MasStoreFinancial masStoreFinicial = (MasStoreFinancial) auditDao.read(MasStoreFinancial.class, finanicalId);
                                if (masStoreFinicial != null)
                                	finanicalIdText = masStoreFinicial.getFinancialYear();
                                finanicalYearStartDate=masStoreFinicial.getStartDate();
                                finanicalYearEndDate=masStoreFinicial.getEndDate();
							}
							if (row[8] != null) {
								entryDate = row[8].toString();
								Date dd1 = HMSUtil.dateFormatteryyyymmdd(entryDate);
								entryDate = HMSUtil.convertDateToStringFormat(
										dd1, "dd/MM/yyyy");
							}
							if (row[9] != null) {
								ucUploadDtId =  Long.parseLong(row[9].toString());
							}
							if (row[10] != null) {
								headTypeId =  Long.parseLong(row[10].toString());
							}
							if (row[11] != null) {
								availableBalance =  Long.parseLong(row[11].toString());
							}
							if (row[12] != null) {
								availableUtilization =  Long.parseLong(row[12].toString());
							}
							if (row[13] != null) {
								certificateNo = row[13].toString();
							}
							if (row[14] != null) {
								status = row[14].toString();
							}
							/*if (row[12] != null) {
								 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
								 createdOn = formatter.format(row[12]);
							}
							if (row[13] != null) {
								Long userId=Long.parseLong(row[13].toString());
								createdUserId=Long.parseLong(row[13].toString());
						        Users user = (Users) auditDao.read(Users.class, userId);
                                if (user != null)
                                    createdBy = user.getUserName();
                            
							}
							if (row[14] != null) {
								cityId =  Long.parseLong(row[14].toString());
								MasCity city = (MasCity) auditDao.read(MasCity.class, cityId);
                                if (city != null)
                                    cityName = city.getCityName();
                            
							}
							if (row[15] != null) {
								phase =  row[15].toString();
						         
							}*/
							pt.put("ucUploadHdId", ucUploadHdId);
							pt.put("upssId",upssId);
							pt.put("certificateNo",certificateNo);
							pt.put("phase", phase);
							pt.put("uploadDate", uploadDate);
							pt.put("fileName", fileName);
							pt.put("createdBy", createdBy);
							pt.put("remarks", remarks);
							pt.put("status", status);
							pt.put("finanicalId", finanicalId);
							if(ucUploadDtId!=null)
							{	
							pt.put("ucUploadDtId", ucUploadDtId);
							}
							else
							{
								pt.put("ucUploadDtId", "");	
							}
							pt.put("headTypeId", headTypeId);
							pt.put("availableBalance", availableBalance );
							pt.put("availableUtilization", availableUtilization );
							
							pt.put("finanicalIdText", finanicalIdText);
							pt.put("finanicalYearStartDate", finanicalYearStartDate);
							pt.put("finanicalYearEndDate", finanicalYearEndDate);
							c.add(pt);
						}
						json.put("listObject", c);
						json.put("msg", "Fund Allocation List  get  sucessfull... ");
						json.put("status", "1");

					}

					catch (Exception e) {
						e.printStackTrace();
						return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
				} else {
					try {
						json.put("msg", "Visit ID data not found");
						json.put("status", 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			
		}
		return json.toString();

	}

	@Override
	public String deleteUCUploadDtDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String ucAllocationDt = null;
		try {
			if (!jsondata.isEmpty()) {
				
				ucAllocationDt = auditDao.deleteUCUploadDtDetails(jsondata);
		// TODO Auto-generated method stub
				if (ucAllocationDt != null && ucAllocationDt.equalsIgnoreCase("statusUpdated")) {
					json.put("msg", "recordsDeleted");
					json.put("status", "1");
				} else if (ucAllocationDt != null && ucAllocationDt.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", ucAllocationDt);
					json.put("status", "0");
				}

			} else {
				json.put("status", "0");
				json.put("msg", "json not contain any object");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String deleteDocument(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String documentDelete = null;
		try {
			if (!jsondata.isEmpty()) {
				
				documentDelete = auditDao.documentDelete(jsondata);
		// TODO Auto-generated method stub
				if (documentDelete != null && documentDelete.equalsIgnoreCase("statusUpdated")) {
					json.put("msg", "recordsDeleted");
					json.put("status", "1");
				} else if (documentDelete != null && documentDelete.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", documentDelete);
					json.put("status", "0");
				}

			} else {
				json.put("status", "0");
				json.put("msg", "json not contain any object");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}
	
}
