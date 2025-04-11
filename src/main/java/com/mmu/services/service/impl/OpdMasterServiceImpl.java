package com.mmu.services.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.mmu.services.dao.DispensaryDao;
import com.mmu.services.dao.MasterDao;
import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.MasDepartment;

import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasFrequency;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasItemClass;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasNursingCare;
import com.mmu.services.entity.MasSpeciality;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreUnit;
import com.mmu.services.entity.MasTreatmentInstruction;
import com.mmu.services.entity.MasUserType;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.OpdTemplateMedicalAdvice;
import com.mmu.services.entity.OpdTemplateTreatment;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.DispensaryService;
import com.mmu.services.service.OpdMasterService;
import com.mmu.services.utils.HMSUtil;

@Repository
public class OpdMasterServiceImpl implements OpdMasterService{

	@Autowired
	OpdMasterDao md;
	
	
	@Autowired
	MasterDao masterDao;
	
	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	@Autowired 
	DispensaryDao dispensaryDao;
	
	@Autowired 
	DispensaryService dispensaryService;
	

	@Autowired
	SessionFactory sessionFactory;
	
	
	/////////////////////// MasDepartmrent get Method /////////////////////////
	@Override
	public String departmentList(HashMap<String, Object> map) {
		JSONObject json = new JSONObject();
		List<Map<String,Object>> departmentList = new ArrayList<>();
		List<MasDepartment> list = (List<MasDepartment>) md.getDepartmentList(map);
		if (list.size() == 0) {
			json.put("status", "0");
			json.put("msg", "Data not found");
		} else {
			for(int i=0;i<list.size();i++) {
				Map<String,Object> data = new HashMap<>();
				data.put("departmentname", HMSUtil.convertNullToEmptyString(list.get(i).getDepartmentName()));
				data.put("departmentId", list.get(i).getDepartmentId());
				departmentList.add(data);
			}
			json.put("departmentList", departmentList);
			json.put("msg", "department List  get  sucessfull... ");
			json.put("status", "1");
		}
		return json.toString();

	}
	
	//////////////////////// Get Master ICD Business Logic //////////////////////

	@Override
	public String getICD(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
		} 
		else
		{
				
				List<MasIcd> mst_icd = md.getIcd(jsondata);
			    if (mst_icd.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    
			    	json.put("ICDList", mst_icd);
			    	json.put("msg", "ICD List  get  sucessfull... ");
			    	json.put("status", "1");

			   }

		

		return json.toString();
	}
        }finally
        {
        	System.out.println("Hi");
        }
	}
	

	@Override
	public String getInvestigation(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
		} 
		else
		{
				
				List<DgMasInvestigation> mst_investigation = md.getInvestigationList(jsondata.get("mainChargeCode").toString(),jsondata.get("employeeId").toString(),jsondata);
			    if (mst_investigation.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    
			    	json.put("InvestigationList", mst_investigation);
			    	json.put("msg", "Investigation List  get  sucessfull... ");
			    	json.put("status", "1");
			    	json.put("size",mst_investigation.size());

			   }

		

		
	}
        }
        catch(Exception e)	{
		e.printStackTrace();
	     }
        return json.toString();
        
        
	}

	@Override
	public String getMasStoreItem(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
		} 
		else
		{
				
				List<MasStoreItem> mst_store = md.getMasStoreItem(jsondata);
				
			    if (mst_store.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    	/*List<HashMap<String, Object>> mst_storeItem = new ArrayList<HashMap<String, Object>>();
			    	
			    	for (MasStoreItem msi : mst_store) {
			    		
			    		HashMap<String, Object> mst = new HashMap<String, Object>();
			    		
			    		mst.put("itemId", msi.getItemId());
			    		mst.put("pvmsNo", msi.getPvmsNo());
			    		mst.put("nomenclature", msi.getNomenclature());
							/*
							 * if(msi.getMasStoreUnit()!=null &&
							 * StringUtils.isNotBlank(msi.getMasStoreUnit().getStoreUnitName()))
							 * mst.put("dispUnit",msi.getMasStoreUnit().getStoreUnitName());
							 */
			    		
			    		//mst_storeItem.add(mst);*/
				    
				    	json.put("MasStoreItemList", mst_store);
				    	json.put("Size", mst_store.size());
				    	json.put("msg", "MasStoreItemList  get  sucessfull... ");
				    	json.put("status", "1");
			    	//}
			    }
			  

		
	}
        }
        catch(Exception e)	{
    		e.printStackTrace();
	     }
        return json.toString();
	}

	@Override
	public String getMasFrequency(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
		} 
		else
		{
			
				List<MasFrequency> mas_frequency = md.getMasFrequency();
			    if (mas_frequency.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    	
			    	json.put("MasFrequencyList", mas_frequency);
			    	json.put("msg", "MasFrequencyList  get  sucessfull... ");
			    	json.put("status", "1");

			   }

	}
        }
        catch(Exception e)	{
    		e.printStackTrace();
	     }
        return json.toString();
	}

	@Override
	public String getTemplateName(HashMap<String, String> jsondata,HttpServletRequest request) {
		
		JSONObject json = new JSONObject();
		if (jsondata.get("templateType") == null || jsondata.get("templateType").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain Template Type as a  key or value or it is null\"}";
		}
		else
		{
		Long doctorId=Long.parseLong(jsondata.get("doctorId").toString());	
		List<OpdTemplate> opdTemplates = md.getTemplateName(jsondata.get("templateType"),doctorId);
	    json.put("template", opdTemplates);
		}
		return json.toString();
	}
	@Override
	public String getEmpanelledHospital(HashMap<String, String> jsondata, HttpServletRequest request) {
		
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
		} 
		else
		{
			
				List<MasEmpanelledHospital> mas_empaneHospital = md.getEmpanelledHospital(jsondata);
			    if (mas_empaneHospital.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    	json.put("masEmpanelledHospital", mas_empaneHospital);
			    	json.put("msg", "MasFrequencyList  get  sucessfull... ");
			    	List<MasDepartment> departmentList = masterDao.getDepartmentsList();
			    	List<MasHospital> masMasHospitalList= md.getHospitalList();
			    	  
			    	  json.put("departmentList", departmentList);
			    	  json.put("masMasHospitalList", masMasHospitalList);
			    	  
				    	json.put("msg", "MasFrequencyList  get  sucessfull... ");
				    	
			    	json.put("status", "1");

			   }

	
	}
        }
        catch(Exception e)	{
    		e.printStackTrace();
	     }
        return json.toString();
	}

	@Override
	public String getDisposalList(HashMap<String, String> jsondata,HttpServletRequest request) {
		     JSONObject json = new JSONObject();
       		
	
        return json.toString();
	}

	@Override
	public String getMasNursingCare(HashMap<String, Object> jsondata, HttpServletRequest request)  {
		 JSONObject json = new JSONObject();
			if (jsondata.get("nursingType") == null || jsondata.get("nursingType").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain Nursing Type as a  key or value or it is null\"}";
				
			}
					
			else {
				
				
				Map<String,Object> map = md.getMasNursingCare(jsondata);
				List<MasNursingCare> getInvestigationData = (List<MasNursingCare>) map.get("list");
				List<HashMap<String, Object>> mc = new ArrayList<HashMap<String, Object>>();
				try
				{
					
				for (MasNursingCare masNur : getInvestigationData) {
					HashMap<String, Object> mn = new HashMap<String, Object>();
					mn.put("nursingCode", masNur.getNursingCode());
					mn.put("nursingId",masNur.getNursingId());
					mn.put("nursingType",masNur.getNursingType());
					mn.put("nursingName", masNur.getNursingName());
					
					mc.add(mn);
					
			}
				if(mc != null && mc.size()>0){
					json.put("data", mc);
					json.put("count", mc.size());
					json.put("msg", "Visit List  get  sucessfull... ");
					json.put("status", "1");
				}else{
					return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
				}
				}
				catch(Exception e)
				{
				  System.out.println(e);
				}

				return json.toString();
			}
		
	}

	@Override
	public String getTemplateInvestData(HashMap<String, Object> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		
		if (jsondata.get("templateId") == null || jsondata.get("templateId").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain Template ID as a  key or value or it is null\"}";
			
		}
				
		else {
			
			
			Map<String,Object> map = md.getTemplateInvestigation(jsondata);
			List<OpdTemplateInvestigation> getInvestigationData = (List<OpdTemplateInvestigation>) map.get("list");
			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			try
			{
				
			for (OpdTemplateInvestigation tempInve : getInvestigationData) {
				HashMap<String, Object> ti = new HashMap<String, Object>();
				ti.put("templateName", tempInve.getOpdTemplate().getTemplateCode());
				ti.put("templateCode",tempInve.getOpdTemplate().getTemplateName());
				ti.put("templateInvestgationId",tempInve.getInvestigationId());
				ti.put("templateDataId", tempInve.getTemplateInvestigationId());
				ti.put("investigationName", tempInve.getDgMasInvestigation().getInvestigationName());
				
				c.add(ti);
				
		}
			if(c != null && c.size()>0){
				json.put("data", c);
				json.put("count", c.size());
				json.put("msg", "Visit List  get  sucessfull... ");
				json.put("status", "1");
			}else{
				return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
			}
			}
			catch(Exception e)
			{
			  System.out.println(e);
			}

			return json.toString();
		}
	}

	@Override
	public String getTemplateTreatmentData(HashMap<String, Object> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if (jsondata.get("templateId") == null || jsondata.get("templateId").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain Template ID as a  key or value or it is null\"}";
			
		}
				
		else {
						
			Map<String,Object> map = md.getTemplateTreatment(jsondata);
			List<OpdTemplateTreatment> getTreatmentTempData = (List<OpdTemplateTreatment>) map.get("list");
			List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
			try
			{
				
			for (OpdTemplateTreatment tempTreatment : getTreatmentTempData) {
				if(tempTreatment.getMasStoreItem().getStatus().equals("y"))
				{	
				HashMap<String, Object> ti = new HashMap<String, Object>();
				ti.put("templateCode",tempTreatment.getOpdTemplate().getTemplateCode());
				ti.put("templateCode",tempTreatment.getOpdTemplate().getTemplateName());
				ti.put("treatmentItemId",tempTreatment.getItemId());
				ti.put("treatmentTemplPId",tempTreatment.getTreatmentTemplateId());
				if(tempTreatment.getMasStoreItem().getNomenclature()!=null && tempTreatment.getMasStoreItem().getNomenclature()!="")
				{
				ti.put("itemIdName",tempTreatment.getMasStoreItem().getNomenclature());
				}
				else
				{
					ti.put("itemIdName","");	
				}
				if(tempTreatment.getMasStoreItem().getMasStoreUnit().getStoreUnitName()!=null && tempTreatment.getMasStoreItem().getMasStoreUnit().getStoreUnitName()!="")
				{
				ti.put("dispUnit",tempTreatment.getMasStoreItem().getMasStoreUnit().getStoreUnitName());
				ti.put("dispUnitIdVal",tempTreatment.getMasStoreItem().getMasStoreUnit().getStoreUnitId());
				}
				else
				{
					ti.put("dispUnit","");	
					ti.put("dispUnitIdVal","");	
				}
				
				if(tempTreatment.getMasStoreItem().getPvmsNo()!=null && tempTreatment.getMasStoreItem().getPvmsNo()!="")
				{
				ti.put("itemIdCode",tempTreatment.getMasStoreItem().getPvmsNo());
				}
				else
				{
					ti.put("itemIdCode","");	
				}
				if(tempTreatment.getFrequencyId()!=null)
				{
				ti.put("frequencyId",tempTreatment.getFrequencyId());
				}
				else
				{
					ti.put("frequencyId","");	
				}
				if(tempTreatment.getMasFrequency().getFrequencyName()!=null && tempTreatment.getMasFrequency().getFrequencyName()!="")
				{
				ti.put("frequencyName",tempTreatment.getMasFrequency().getFrequencyName());
				}
				else
				{
					ti.put("frequencyName","");	
				}
				if(tempTreatment.getDosage()!=null && tempTreatment.getDosage()!="")
				{	
				ti.put("dosage", tempTreatment.getDosage());
				}
				else
				{
					ti.put("dosage", "");	
				}
				if(tempTreatment.getNoofdays()!=null)
				{
				ti.put("noOfDays",tempTreatment.getNoofdays());
				}
				else
				{
					ti.put("noOfDays","");	
				}
				if(tempTreatment.getTotal()!=null)
				{
				ti.put("total",tempTreatment.getTotal());
				}
				else
				{
					ti.put("total","");	
				}
				if(tempTreatment.getInstruction()!=null)
				{
				ti.put("instrcution",tempTreatment.getInstruction());
				}
				else
				{
					ti.put("instrcution","");	
				}
				if(tempTreatment.getMasStoreItem()!=null && tempTreatment.getMasStoreItem().getItemClassId()!=null)
				{
				ti.put("itemClassId",tempTreatment.getMasStoreItem().getItemClassId());
				}
				else
				{
					ti.put("itemClassId","");	
				}
				
				if(tempTreatment.getMasStoreItem().getItemId()!=null)
				{
				ti.put("itemId",tempTreatment.getMasStoreItem().getItemId());
				jsondata.put("itemId", tempTreatment.getMasStoreItem().getItemId());
				int avStock=getAvailableStock(jsondata);
				ti.put("availableStock", avStock);
				}
				else
				{
					ti.put("itemId","");	
				}
				
				c.add(ti);
			}	
		}
			if(c != null && c.size()>0){
				json.put("data", c);
				json.put("count", c.size());
				json.put("msg", "Treatment List  get  sucessfull... ");
				json.put("status", "1");
			}else{
				return "{\"status\":\"0\",\"msg\":\"Data Not Found\"}";
			}
			}
			catch(Exception e)
			{
			  System.out.println(e);
			}

			return json.toString();
		}
	}

	/*
	 * @Override public String executeDbProcedure(String jsondata) { JSONObject json
	 * = new JSONObject(jsondata); JSONObject respJson = new JSONObject(); long
	 * hospitalId = Long.parseLong(json.get("hospitalId").toString()); boolean
	 * respFlag=false; List list=null; Map<String, Object> mapObj =
	 * md.executeDbProcedure(hospitalId); if(mapObj!=null && !mapObj.isEmpty() ) {
	 * list = (List)mapObj.get("unitlist"); respJson.put("status", "1");
	 * respJson.put("data", list); respJson.put("msg", "Procedure executed"); } else
	 * { respJson.put("status", "0"); respJson.put("msg", "Procedure not executed");
	 * }
	 * 
	 * return respJson.toString(); }
	 */
	@Override
	public String executeDbProcedure(String jsondata){
			JSONObject json = new JSONObject(jsondata);
			JSONObject respJson = new JSONObject();
			long hospitalId = Long.parseLong(json.get("hospitalId").toString());
			long userId = Long.parseLong(json.get("userId").toString());
			
			boolean respFlag=false;
			List list=null;
			Map<String, Object> mapObj = md.executeDbProcedure(hospitalId,userId);
			if(mapObj!=null && !mapObj.isEmpty() ) {
				list = (List)mapObj.get("unitlist");				
				respJson.put("status", "1");
				respJson.put("data", list);
				respJson.put("msg", "Procedure executed");
			}
			else {
				respJson.put("status", "0");
				respJson.put("msg", "Procedure not executed");
			}
			
		return respJson.toString();
	}	
	
	
	@Override
	public String executeDbProcedureforStatistics(String jsondata){
			JSONObject json = new JSONObject(jsondata);
			JSONObject respJson = new JSONObject();
			long userhospitalId = Long.parseLong(json.get("userhospitalId").toString());
			long combohospitalId = Long.parseLong(json.get("combohospitalId").toString());
			long userId = Long.parseLong(json.get("userId").toString());
			
			
			System.out.println("userhospitalId="+userhospitalId);
			System.out.println("combohospitalId="+combohospitalId);
			System.out.println("userId="+userId);
			boolean respFlag=false;
			List list=null;
			Map<String, Object> mapObj = md.executeDbProcedureforStatistics(userhospitalId,combohospitalId,userId);
			if(mapObj!=null && !mapObj.isEmpty() ) {
								
				respJson.put("status", "1");
				respJson.put("data", mapObj.get("stat_summary"));
				respJson.put("msg", "Procedure executed");
			}
			else {
				respJson.put("status", "0");
				respJson.put("msg", "Procedure not executed");
			}
			
		return respJson.toString();
	}

	@Override
	public String getMasItemClass(HashMap<String, Object> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
		} 
		else
		{
				
				List<MasItemClass> mas_itemClass = md.getMasItemClass();
			    if (mas_itemClass.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    	
			    	json.put("MasItemClassList", mas_itemClass);
			    	json.put("msg", "MasItemClassList  get  sucessfull... ");
			    	json.put("status", "1");

			   }

		
	}
        }
        catch(Exception e)	{
    		e.printStackTrace();
	     }
        return json.toString();
}

	@Override
	public String getDispUnit(HashMap<String, Object> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
			} else {
				
					List<MasStoreUnit> mas_storeUnit = md.getMasStoreUnit();
					if (mas_storeUnit.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					} else {
						
						json.put("MasItemClassList", mas_storeUnit);
						json.put("msg", "MasItemClassList  get  sucessfull... ");
						json.put("status", "1");

					}

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	@Override
	public String getMasStoreItemNip(HashMap<String, String> jsondata, HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("") && jsondata.get("hospitalId") == null && jsondata.get("hospitalId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId and hospitalId as a  key or value or it is null\"}";
		} 
		else
		{
				
				List<MasStoreItem> mst_storeNip = md.getMasStoreItemNip(jsondata);
				
			    if (mst_storeNip.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    	
			    	
				    	json.put("MasStoreItemList", mst_storeNip);
				    	json.put("Size", mst_storeNip.size());
				    	json.put("msg", "MasStoreItemNipList  get  sucessfull... ");
				    	json.put("status", "1");
			    	//}
			    }
			   

		
	}
        }
        catch(Exception e)	{
    		e.printStackTrace();
	     }
        return json.toString();
	}
	
	
	//rajdeo
		@Override
		public String executeProcedureForDashBoard(Map<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) {
			JSONObject json = new JSONObject();
			Map<String, Object> mapObj = md.executeProcedureForDashBoard(jsondata,request,response);
			JSONObject result = (JSONObject)mapObj.get("dashboardData");
			json.put("dashboardData", result);
			json.put("status", 1);
			return json.toString();
		}

		@Override
		public String saveMasNursingCare(HashMap<String, Object> jsondata, HttpServletRequest request) {
			String opdNursingCare = null;
		
			JSONObject json = new JSONObject();	
			try {
				if (!jsondata.isEmpty())
				{
									
						opdNursingCare = md.saveOpdNursingCareDetails(jsondata);
					
	                  
	                   //if (opd != null ||oph != null  && opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) {
					if (opdNursingCare != null && opdNursingCare.equalsIgnoreCase("200")) {
							json.put("msg", "Opd Patinet Details Saved successfully ");
							json.put("status", "1");
						} else if (opdNursingCare != null && opdNursingCare.equalsIgnoreCase("403")) {
							json.put("msg", " you are not authorized for this activity ");
							json.put("status", "0");
						} else {
							json.put("msg", opdNursingCare);
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
		public String saveEmpanlledHospital(HashMap<String, Object> jsondata, HttpServletRequest request) {
			
			String opdEmpanlledHospital = null;
			JSONObject json = new JSONObject();	
			try {
				if (!jsondata.isEmpty())
				{
									
					opdEmpanlledHospital = md.saveEmpanlledHospitalDetails(jsondata);
					
	                  
	                   //if (opd != null ||oph != null  && opd.equalsIgnoreCase("200")||oph.equalsIgnoreCase("200")) {
					if (opdEmpanlledHospital != null && opdEmpanlledHospital.equalsIgnoreCase("200")) {
							json.put("msg", "Opd Patinet Details Saved successfully ");
							json.put("status", "1");
						} else if (opdEmpanlledHospital != null && opdEmpanlledHospital.equalsIgnoreCase("403")) {
							json.put("msg", " you are not authorized for this activity ");
							json.put("status", "0");
						} else {
							json.put("msg", opdEmpanlledHospital);
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
		public String getHospitalList(HashMap<String, String> jsondata, HttpServletRequest request) {
			JSONObject json = new JSONObject();
	        try
	        {
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
			{
				return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
			} 
			else
			{
					
					List<MasHospital> mst_hospital = md.getHospitalList();
				    if (mst_hospital.size() == 0)
				    {
				    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				    } 
				    else 
				    {
				    	
				    	json.put("HospitalList", mst_hospital);
				    	json.put("msg", "Hospital List  get  sucessfull... ");
				    	json.put("status", "1");

				   }

			

			return json.toString();
		}
	        }finally
	        {
	        	System.out.println("Hi Hospital");
	        }
		}

		@Override
		public String getSpecialistList(HashMap<String, Object> jsondata) {
			JSONObject json = new JSONObject();
	        try
	        {
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
			{
				return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
			} 
			else
			{
					
					List<MasSpeciality> mstSpecialist = md.getSpecialistList(jsondata);
					
				    if (mstSpecialist.size() == 0)
				    {
				    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				    } 
				    else 
				    {
				        	json.put("MasSpecialistList", mstSpecialist);
					    	json.put("Size", mstSpecialist.size());
					    	json.put("msg", "MasSpecialistList  get  sucessfull... ");
					    	json.put("status", "1");
				    	//}
				    }
		

			
		}
	        }
	        catch(Exception e)	{
	    		e.printStackTrace();
		     }
	        return json.toString();
		}

////////////////////////Get Master ICD Business Logic //////////////////////

	 

		@Override
		public String getICDListByName(HashMap<String, String> jsondata, HttpServletRequest request) {
			JSONObject json = new JSONObject();
			try {
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
			} else {
				
				String icdName=jsondata.get("icdName"); 
				List<MasIcd> mst_icd = md.getIcdByName(icdName);
				if (mst_icd.size() == 0) {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				} else {
					json.put("ICDList", mst_icd);
					json.put("msg", "ICD List  get  sucessfull... ");
					json.put("status", "1");
			
				}
			
			
			return json.toString();
			}
			} finally {
			System.out.println("Exception Occured");
			}
		}

		@Override
		public String getTemplateMedicalAdviceData(HashMap<String, Object> jsondata, HttpServletRequest request) {
			JSONObject json = new JSONObject();
			
			/*if (jsondata.get("templateId") == null || jsondata.get("templateId").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain Template ID as a  key or value or it is null\"}";
				
			}
			else {*/
				Map<String,Object> map = md.getTemplateMedicalAdvice(jsondata);
				List<OpdTemplateMedicalAdvice> getMAdviceData = (List<OpdTemplateMedicalAdvice>) map.get("list");
				List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
				try
				{
					
				for (OpdTemplateMedicalAdvice tempMAdvice : getMAdviceData) {
					HashMap<String, Object> ti = new HashMap<String, Object>();
					//ti.put("templateName", tempMAdvice.getOpdTemplate().getTemplateCode());
					//ti.put("templateCode",tempMAdvice.getOpdTemplate().getTemplateName());
					ti.put("templateMadviceId",tempMAdvice.getTemplateMadviceId());
					ti.put("medicalAdvice", tempMAdvice.getMedicalAdvice());
					
					c.add(ti);
					
			}
				if(c != null && c.size()>0){
					json.put("data", c);
					json.put("count", c.size());
					json.put("msg", "Visit List  get  sucessfull... ");
					json.put("status", "1");
				}else{
					return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
				}
				}
				catch(Exception e)
				{
				  System.out.println(e);
				}

				return json.toString();
			//}
		}

		public MasMainChargecode getMainChargeCode(String mainChargeCode)
		{
			Session session =null;
			MasMainChargecode mainChargeId=null;
			try {
				session=getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria cr = session.createCriteria(MasMainChargecode.class)
						.add(Restrictions.eq("mainChargecodeCode", mainChargeCode));
				List<MasMainChargecode>listInvHd=cr.list();
				if(CollectionUtils.isNotEmpty(listInvHd)) {
					mainChargeId=listInvHd.get(0);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
			return mainChargeId;
		}

		@Override
		public String geTreatmentInstruction(HashMap<String, Object> jsondata, HttpServletRequest request) {
			JSONObject json = new JSONObject();
			try {
				if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
					return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
				} else {
					
						List teatmentInstruction = md.getTreatmentInstruction();
						if (teatmentInstruction.size() == 0) {
							return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
						} else {
							
							json.put("teatmentInstruction", teatmentInstruction);
							json.put("msg", "teatmentInstruction  get  sucessfull... ");
							json.put("status", "1");

						}

					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json.toString();
		}
		
		@Override
		public int getAvailableStock(HashMap<String, Object> jsondata) {
			
			JSONObject json = new JSONObject(jsondata);
			String dispId = String.valueOf(json.get("departmentId"));
			Map<String,Object> responseMap=new HashMap<String, Object>();
			String itemId = json.get("itemId").toString();
			String mmuId = json.get("mmuId1").toString();
			Map<String, Object> inputJson = new HashMap<String, Object>();
			inputJson.put("mmuId", mmuId);
			inputJson.put("department_id", "2051");
			inputJson.put("item_id", itemId);
			String stockData = dispensaryService.getBatchDetail(inputJson);
			JSONObject jobj = new JSONObject(stockData);
			JSONObject jobject =  (JSONObject) jobj.get("batchData");
			int storeStock = (int) jobject.get("store_stock");
			int availableStock = (int) jobject.get("disp_stock");
			responseMap.put("storeStock", storeStock);
			responseMap.put("dispStock", availableStock);
			responseMap.put("availableStock", availableStock);
			responseMap.put("status", 1);
			return availableStock;
		}

		

	
}