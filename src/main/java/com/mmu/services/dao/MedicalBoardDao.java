package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasEmployeeCategory;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.PatientMedBoard;
import com.mmu.services.entity.PatientMedBoardChecklist;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.Visit;

@Repository
public interface MedicalBoardDao {

	/*
	 * Map<String, Object> getValidateMedicalBoardList(HashMap<String, Object>
	 * jsondata);
	 * 
	 * String saveOpdInvestigation(DgOrderhd orderhd, List<DgOrderdt> dgorderdt);
	 * 
	 * String savePreliminaryMBDetails(HashMap<String, Object> jsondata);
	 * 
	 * List<PatientMedicalCat> getPatientMedicalBoardDetails(Long visitId);
	 * 
	 * Map<String, Object> getPatientMedicalBoardDetailsDgOrder(HashMap<String,
	 * Object> jsonData);
	 * 
	 * String opdVitalDetails(OpdPatientDetail opddetails, HashMap<String, Object>
	 * payload);
	 * 
	 * String saveReferforOpinionMBDetails(HashMap<String, Object> jsondata);
	 * 
	 * Map<String, Object> getValidateMedicalBoardWaitingList(HashMap<String,
	 * Object> jsonData);
	 * 
	 * List<OpdPatientDetail> getVitalRecord(Long visitId);
	 * 
	 * List<Visit> getPatientVisit(Long visitId);
	 * 
	 * List<MasMedicalCategory> getMasMedicalCategory();
	 * 
	 * Map<String, Object> getValidateMedicalExamDetails(HashMap<String, Object>
	 * jsondata);
	 * 
	 * List<MasHospital> getHospitalList();
	 * 
	 * List<MasEmpanelledHospital> getEmpanelledHospital(HashMap<String, String>
	 * jsonData);
	 * 
	 * List<MasDepartment> getDepartmentsList();
	 * 
	 * List<Object[]> getDgMasInvestigationsAndResult(Long visitId);
	 * 
	 * Long saveOrUpdateMedicalExam(MasMedicalExamReport masMedicalExamReprt);
	 * 
	 * PatientMedicalCat getPatientMedicalCat(Long patientMedicalCatId);
	 * 
	 * Long saveOrUpdatePatientMedicalCat(PatientMedicalCat patientMedicalCat);
	 * 
	 * String saveAmsfForm16Details(HashMap<String, Object> jsondata);
	 * 
	 * Map<String, Object> getMOValidateMedicalBoardWaitingList(HashMap<String,
	 * Object> jsonData);
	 * 
	 * List<MasMedicalExamReport> getMasMedicalExamReport(Long visitId);
	 * 
	 * String saveUpdateSpeicialistOpinionMBDetails(HashMap<String, Object>
	 * jsondata);
	 * 
	 * String saveAmsfForm15Details(HashMap<String, Object> jsondata);
	 * 
	 * List<PatientMedBoard> getPatientMedBoardReports(Long visitId);
	 * 
	 * String saveAmsf15CheckList(HashMap<String, Object> jsondata);
	 * 
	 * List<PatientMedBoardChecklist> getCheckList(Long visitId);
	 * 
	 * List<MasEmployeeCategory> getAllEmployeeCategory();
	 * 
	 * String dataDigitizationsaveAmsfForm15(HashMap<String, Object> jsondata);
	 * 
	 * 
	 * HashMap<String, Object> dataDigitizationSaveSpecialistOpinion(HashMap<String,
	 * Object> jsondata);
	 * 
	 * List<PatientMedicalCat> getPatientMedicalBoardDetailsCategory(Long visitId);
	 * 
	 * Long getInvestigationsVisitId(Long patientId);
	 * 
	 * String opdObsisty(JSONObject payload);
	 * 
	 * List<DischargeIcdCode> getDischargeIcdCode(Long visitId);
	 * 
	 * List<MasMedicalCategory> getMasMedicalCategoryName(String icdName);
	 */

	

	/*MasMedicalExamReport getMasMedicalExamReprtByVisitId(Long visitId);

	MasUnit getMasUnitByHospitalId(Long hospitalId);

	OpdPatientDetail getOpdPatientDetailByVisitId(Long visitId);

	Long saveOrUpdateVisit(Visit visit);

	DgResultEntryDt getDgResultEntryDtByDgResultEntryId(Long dgResultEntryId);

	DgResultEntryHd getDgResultEntryHdByPatientIdAndHospitalId(Long dgResultHdId);

	DgResultEntryHd getDgResultEntryHdByPatientIdAndHospitalIds(Long patientId, Long hospitalId);

	Long saveOrUpdateDgResultEntryDt(DgResultEntryDt dgResultEntryDt);

	Long saveOrUpdateDgResultEntryHd(DgResultEntryHd dgResultEntryHd);*/

	


}
