package com.mmu.services.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmu.services.dao.ApprovalProcessDao;
import com.mmu.services.entity.AnmApmApproval;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.VendorInvoicePayment;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.ApprovalProcessService;
import com.mmu.services.utils.ValidateUtils;

@Service
public class ApprovalProcessServiceImpl implements ApprovalProcessService {
	@Autowired
	ApprovalProcessDao approvalProcessDao;

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Override
	public String saveOrUpdateANMEntryDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Long anmApmId = null;
		JSONObject json = new JSONObject();

		try {
			if (!jsondata.isEmpty()) {
				
					JSONObject nullbalankvalidation = null;
						nullbalankvalidation = ValidateUtils.addANMEntryDetails(jsondata);
						if (nullbalankvalidation.optString("status").equals("0")) {
							return nullbalankvalidation.toString();
						} else {
							anmApmId = approvalProcessDao.saveOrUpdateANMEntryDetails(jsondata);
		
						}
					
				
						if (anmApmId != null && anmApmId!=0) {
							json.put("msg", "Fund Details Saved successfully ");
							json.put("status", "1");
							json.put("anmApmId", anmApmId);
						} else if (anmApmId != null) {
							json.put("msg", " you are not authorized for this activity ");
							json.put("status", "0");
						} else {
							json.put("msg", anmApmId);
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
	public String getAnmOpdOfflineData(JSONObject jsondata) {
        JSONObject json = new JSONObject();
        try {
            List<AnmApmApproval> anmOpdOfflineDetailsDataList = new ArrayList<>();
            List list = new ArrayList();
            if (jsondata != null && jsondata.has("anmApmId")) {
                Map<String, List<AnmApmApproval>> anmOpdOfflineDetailsMapData = approvalProcessDao.getAnmOpdOfflineData(jsondata);
                Date d=null;
                List totalMatches = new ArrayList();
                if (anmOpdOfflineDetailsMapData.get("anmOpdOfflineDetails") != null) {
                	anmOpdOfflineDetailsDataList = anmOpdOfflineDetailsMapData.get("anmOpdOfflineDetails");
                	 totalMatches = anmOpdOfflineDetailsMapData.get("totalMatches");
                	anmOpdOfflineDetailsDataList.forEach(e -> {
                        HashMap<String, Object> mapObj = new HashMap<String, Object>();
                        String forwardName = "";
                       
                        if (e != null) {
                            
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String dateOfEntry = "";
                            String opdDate = "";
                            if (e.getDateOfEntry()!= null)
                            {	
                            	dateOfEntry = formatter.format(e.getDateOfEntry());
                             }
                            if (e.getOpdDate()!= null)
                            {	
                            	opdDate = formatter.format(e.getOpdDate());
                             }

                            mapObj.put("anmApmId", e.getAnmApmId());
                            mapObj.put("cityId", e.getCityId());
                            mapObj.put("mmuId", e.getMmuId());
                            mapObj.put("patientNoApplyLabourRegistration", e.getPatientNoApplyLabourRegistration());
                            mapObj.put("patientNoLabourRegistredDepartment", e.getPatientNoLabourRegistredDepartment());
                            mapObj.put("patientNoMedicineDispensed", e.getPatientNoMedicineDispensed());
                            mapObj.put("status", e.getStatus());
                            mapObj.put("totalNoPatients", e.getTotalNoPatients());
                            mapObj.put("patientNoLabTest", e.getPatientNoLabTest());
                            mapObj.put("dateOfEntry", dateOfEntry);
                            mapObj.put("opdDate", opdDate);
                            mapObj.put("enterBy", e.getUser().getUserName());
                            mapObj.put("mmuName", e.getMasMMU().getMmuName());
                            mapObj.put("cityName", e.getMasCity().getCityName());
                            if(e.getRemarks()!=null)
                            {
                            	 mapObj.put("remarks", e.getRemarks());	
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

	/*
	 * @Override public Map<String, Object> getApprovalFormatData(HashMap<String,
	 * Object> payload, HttpServletRequest request, HttpServletResponse response) {
	 * // TODO Auto-generated method stub
	 * 
	 * List<StoreInternalIndentM> storeInternalIndentMList = new
	 * ArrayList<StoreInternalIndentM>(); List<Map<String,Object>> responseList =
	 * new ArrayList<Map<String,Object>>(); Map<String, Object> map = new
	 * HashMap<String, Object>(); Map<String,Object> responseMap=new HashMap<String,
	 * Object>(); responseMap = approvalProcessDao.getIndentList(payload);
	 * List<MasMedicalExamReport> listMasMedicalExamReportInbox =null;
	 * List<MasMedicalExamReport> listMasMedicalExamReportPendingAndRej =null;
	 * 
	 * List<MasMedicalExamReport> listMBMasMedicalExamReportInbox =null;
	 * List<MasMedicalExamReport> listMBMasMedicalExamReportPendingAndRej =null;
	 * 
	 * List<MasMedicalExamReport> listMasMedExamReportForDigiRejAndApproved =null;
	 * 
	 * int inbox=0; int totalApproval=0; int totalRejected=0; int meInbox=0; int
	 * meApprove=0; int meReject=0; int mbInbox=0; int mbApprove=0; int mbReject=0;
	 * int indentInbox=0; int indentApprove=0; int indentReject=0;
	 * 
	 * int meApproveForDigi=0; int meRejectForDigi=0; int totalApprovalForDigi=0;
	 * int totalRejectedForDigi=0;
	 * 
	 * int count = 1; if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * storeInternalIndentMList = (List<StoreInternalIndentM>)
	 * responseMap.get("storeInternalIndentMList"); if
	 * (!storeInternalIndentMList.isEmpty() && storeInternalIndentMList.size() > 0)
	 * { for(StoreInternalIndentM storeInternalIndentM : storeInternalIndentMList) {
	 * 
	 * Map<String,Object>map1=new HashMap<String, Object>();
	 * if(storeInternalIndentM.getStatus().equalsIgnoreCase("Y")) { inbox++;
	 * indentInbox++;
	 * 
	 * }else if(storeInternalIndentM.getStatus().equalsIgnoreCase("A")) {
	 * totalApproval++; indentApprove++;
	 * 
	 * } else if(storeInternalIndentM.getStatus().equalsIgnoreCase("R")) {
	 * indentReject++; totalRejected++;
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * listMasMedicalExamReportInbox = (List<MasMedicalExamReport>)
	 * responseMap.get("listMasMedicalExamReportInbox"); if
	 * (CollectionUtils.isNotEmpty(listMasMedicalExamReportInbox)) { for
	 * (MasMedicalExamReport masMedicalExamReport : listMasMedicalExamReportInbox) {
	 * 
	 * inbox++; if(masMedicalExamReport!=null &&
	 * masMedicalExamReport.getVisit()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode().equalsIgnoreCase("ME")) { meInbox++; }
	 * if(masMedicalExamReport!=null && masMedicalExamReport.getVisit()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode().equalsIgnoreCase("MB")) { mbInbox++; } }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * if (responseMap.size() > 0 && !responseMap.isEmpty()) {
	 * 
	 * listMasMedicalExamReportPendingAndRej = (List<MasMedicalExamReport>)
	 * responseMap.get("listMasMedicalExamReportPendingAndReject"); if
	 * (CollectionUtils.isNotEmpty(listMasMedicalExamReportPendingAndRej)) { for
	 * (MasMedicalExamReport masMedicalExamReport :
	 * listMasMedicalExamReportPendingAndRej) { if(masMedicalExamReport!=null &&
	 * masMedicalExamReport.getVisit()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode().equalsIgnoreCase("ME")) { if
	 * (masMedicalExamReport.getStatus().equalsIgnoreCase("ac") ) { totalApproval++;
	 * meApprove++;
	 * 
	 * } else if (masMedicalExamReport.getStatus().equalsIgnoreCase("rj")) {
	 * totalRejected++; meReject++; } else if
	 * (masMedicalExamReport.getStatus().equalsIgnoreCase("af") ) { totalApproval++;
	 * meApprove++;
	 * 
	 * } }
	 * 
	 * if(masMedicalExamReport!=null && masMedicalExamReport.getVisit()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode().equalsIgnoreCase("MB")) { if
	 * (masMedicalExamReport.getStatus().equalsIgnoreCase("ac") ) { totalApproval++;
	 * mbApprove++;
	 * 
	 * } else if (masMedicalExamReport.getStatus().equalsIgnoreCase("rj")) {
	 * totalRejected++; mbReject++; } else if
	 * (masMedicalExamReport.getStatus().equalsIgnoreCase("af") ) { totalApproval++;
	 * mbApprove++;
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * ////////////////////////////////////////Used for
	 * Digitization/////////////////////////////////////////////////////////////////
	 * // listMasMedExamReportForDigiRejAndApproved = (List<MasMedicalExamReport>)
	 * responseMap.get("listMasMedExamReportForDigiRejAndApproved"); if
	 * (CollectionUtils.isNotEmpty(listMasMedExamReportForDigiRejAndApproved)) { for
	 * (MasMedicalExamReport masMedicalExamReport :
	 * listMasMedExamReportForDigiRejAndApproved) { if(masMedicalExamReport!=null &&
	 * masMedicalExamReport.getVisit()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null ) { if
	 * (masMedicalExamReport.getStatus().equalsIgnoreCase("ea") ) {
	 * totalApprovalForDigi++; meApproveForDigi++;
	 * 
	 * } else if (masMedicalExamReport.getStatus().equalsIgnoreCase("vr") ||
	 * masMedicalExamReport.getStatus().equalsIgnoreCase("ar")) {
	 * totalRejectedForDigi++; meRejectForDigi++; }
	 * 
	 * }
	 * 
	 * 
	 * if(masMedicalExamReport!=null && masMedicalExamReport.getVisit()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode()!=null &&
	 * masMedicalExamReport.getVisit().getMasAppointmentType().
	 * getAppointmentTypeCode().equalsIgnoreCase("MB")) { if
	 * (masMedicalExamReport.getStatus().equalsIgnoreCase("ac") ) { totalApproval++;
	 * mbApprove++;
	 * 
	 * } else if (masMedicalExamReport.getStatus().equalsIgnoreCase("rj")) {
	 * totalRejected++; mbReject++; } else if
	 * (masMedicalExamReport.getStatus().equalsIgnoreCase("af") ) { totalApproval++;
	 * mbApprove++;
	 * 
	 * } }
	 * 
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * map.put("inbox", inbox); map.put("approve", totalApproval); map.put("reject",
	 * totalRejected);
	 * 
	 * map.put("meInbox", meInbox); map.put("mbInbox", mbInbox);
	 * map.put("indentInbox", indentInbox);
	 * 
	 * map.put("meApprove", meApprove); map.put("mbApprove", mbApprove);
	 * map.put("indentApprove", indentApprove);
	 * 
	 * map.put("meReject", meReject); map.put("mbReject", mbReject);
	 * map.put("indentReject", indentReject);
	 * 
	 * 
	 * map.put("meApproveForDigi", meApproveForDigi); map.put("meRejectForDigi",
	 * meRejectForDigi); map.put("totalApprovalForDigi", totalApprovalForDigi);
	 * map.put("totalRejectedForDigi", totalRejectedForDigi);
	 * 
	 * responseMap.put("data", map); responseMap.put("status",1); return
	 * responseMap; }
	 */

}
