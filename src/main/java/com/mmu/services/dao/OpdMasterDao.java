package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.MasDepartment;

import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasFrequency;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasItemClass;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasSpeciality;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSection;
import com.mmu.services.entity.MasStoreUnit;
import com.mmu.services.entity.MasTreatmentInstruction;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.Users;

@Repository
public interface OpdMasterDao {

	Users checkEmp(Long i);

	List<MasIcd> getIcd(HashMap<String, String> jsondata);

	List<DgMasInvestigation> getInvestigationList(String string, String employeeId,HashMap<String, String> jsondata);

	List<MasStoreItem> getMasStoreItem(HashMap<String, String> jsondata);

	List<MasFrequency> getMasFrequency();

	List<OpdTemplate> getTemplateName(String templateType, Long doctorId);

	List<MasEmpanelledHospital> getEmpanelledHospital(HashMap<String, String> jsondata);

	

	Map<String, Object> getMasNursingCare(HashMap<String, Object> jsonData);

	Map<String,Object> getTemplateInvestigation(HashMap<String,Object> JsonData);
	
	Map<String, Object> getTemplateTreatment(HashMap<String, Object> jsonData);

	Users checkUser(Long i);
	Map<String, Object> executeDbProcedure(long hospitalId,long userId);

	Map<String, Object> executeDbProcedureforStatistics(long userhospitalId, long combohospitalId, long userId);

	List<MasItemClass> getMasItemClass();

	List<MasStoreUnit> getMasStoreUnit();
	
	List<MasStoreItem> getMasStoreItemNip(HashMap<String, String> jsondata);
		
	Map<String, Object> executeProcedureForDashBoard(Map<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);

	String saveOpdNursingCareDetails(HashMap<String, Object> jsondata);

	String saveEmpanlledHospitalDetails(HashMap<String, Object> jsondata);

	List<MasDepartment> getDepartmentList(HashMap<String, Object> map);

	List<MasHospital> getHospitalList();

	MasStoreSection getSectionId(String sectionCode);

	MasItemType getItemTypeId(String itemTypeCode);

	List<MasSpeciality> getSpecialistList(HashMap<String, Object> map);

	List<MasIcd> getIcdByName(String icdName);

	Map<String, Object> getTemplateMedicalAdvice(HashMap<String, Object> jsonData);

	MasMainChargecode getMainChargeCode(String mainChargeCode);

	List<Long> getTemplateInvestigationOp(HashMap<String, Object> jsonData);

	List<MasTreatmentInstruction> getTreatmentInstruction();


		

}
