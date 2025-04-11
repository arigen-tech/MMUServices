package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.AuditException;
import com.mmu.services.entity.AuditOpd;
import com.mmu.services.entity.ChildVacatinationChart;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasAnesthesia;
import com.mmu.services.entity.MasAudit;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasStoreSection;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.OpdTemplateMedicalAdvice;
import com.mmu.services.entity.OpdTemplateTreatment;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDataUpload;
import com.mmu.services.entity.PatientFamilyHistory;
import com.mmu.services.entity.PatientPrescriptionDt;
import com.mmu.services.entity.PatientPrescriptionHd;
import com.mmu.services.entity.PatientSymptom;
import com.mmu.services.entity.ProcedureDt;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;

@Repository
public interface OpdDao {

	
	String saveOpdPatientDetails(HashMap<String, Object> jsondata);

	

	List<MasEmployee> getEmployee();

	int listPaginatedVisit(int firstResult, int maxResults);

	List<Object[]> getSearchPatinet(String patinetName);

	//List<MasIdealWeight> getIdealWeight(String height);
	
	

	List<Visit> getPatientVisit(Long visitId);

	List<OpdPatientDetail> getVitalRecord(Long visitId);

	Map<String,Object> getPreviousVitalRecord(HashMap<String,Object> json);
	
	List<PatientFamilyHistory> getFamilyHistory();

	//String opdPatinetHistory(OpdPatientHistory ob);

	OpdPatientDetail checkVisitOpdPatientDetails(Long visitId);

	//OpdPatientHistory checkVisitOpdPatientHistory(Long visitId);

	String opdTemplate(OpdTemplate ob,OpdTemplateInvestigation opdinv);

	String opdTemplatenewMethod(OpdTemplate opdTemp, List<OpdTemplateInvestigation> opdInvestigationList);
	
	String saveTreatTemplatenewMethod(OpdTemplate opdTemp, List<OpdTemplateTreatment> opdTreatmentTempList);

	String saveOpdInvestigation(DgOrderhd orderhd, List<DgOrderdt> listofOpdInvest);

	String saveOpdPrescription(PatientPrescriptionHd pphd, List<PatientPrescriptionDt> patientPrescDT);

	String opdObsisty(HashMap<String, Object> jsondata);
	
	String opdVitalDetails(OpdPatientDetail ob);

	MasEmployee checkEmp(Long i);

	Map<String,Object> getVisit(HashMap<String,Object> json);
	
	Map<String, Object> getPreconsulationVisit(HashMap<String, Object> jsonData);

	Patient checkPatient(Long i);

	MasAdministrativeSex checkGender(Long i);

	List<Patient> getPatinet();

	MasRelation checkRelation(Long i);

	//List<Object[]> getPreviousVisitRecord(Long patientId);

	//List<OpdPatientHistory> getPreviousVisitHistory(Long patientId);
	
	Map<String, Object> getObesityWaitingList(HashMap<String, Object> jsondata);
	
	Map<String,Object> getObesityDetails(HashMap<String, Object> jsondata);
	
	List<String> getIdealWeight(Long height,String age,Long genderId);
	
	String saveObesityDetails(HashMap<String, Object> jsondata);
	
	Map<String,Object> referredPatientList(HashMap<String, String> jsondata);
	
	Map<String,Object> referredPatientDetail(HashMap<String, String> jsondata);
	
	 Map<String,Object> updateReferralDetail(HashMap<String, Object> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	Map<String,Object> getAdmissionDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	public Map<String,Object> getPendingDischargeList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
		
	String savePatientAdmission(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	
	String saveNewAdmission(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	Map<String,Object> nursingCareWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,HttpServletResponse response);
	
	Map<String,Object> getNursingCareDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	Map<String,Object> getProcedureDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);  
	
	String saveProcedureDetail(Map<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	Map<String, Object> physioTherapyWaitingList(Map<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	Map<String,Object> getphysioTherapyDetail(HashMap<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> getOpdReportsDetailsbyServiceNo(HashMap<String, Object> jsonData);

	Map<String, Object> getOpdReportsDetailsbyPatinetId(HashMap<String, Object> jsonData);

	Users checkUser(Long i);

	//////////////by dhiraj /////////////////////////
	Map<String, Object> minorSurgeryWaitingList(HashMap<String, String> jsondata, HttpServletRequest request,
	HttpServletResponse response);

	Map<String,Object> getMinorSurgeryDetail(HashMap<String, String> jsondata, HttpServletRequest request,
	HttpServletResponse response);
	List<MasAnesthesia> getAnesthesiaList();
	String saveMinorSurgery(HashMap<String, Object> jsondata);
	String deleteMinorSurgery(HashMap<String, Object> jsondata);
	
	String findAuthenticatePatient(HashMap<String, Object> jsondata);
	String findCheckForAuthenticatePatient(HashMap<String, Object> jsondata);

	String deleteInvestigationTemplate(HashMap<String, Object> jsondata);

	String opdupdateInvestigationTemplate(OpdTemplate opdTemp, List<OpdTemplateInvestigation> opdInvestigationList);

	List<Object[]> getPatientPreviousHistory(Long patientId);
	
	String validateMinorSurgery(HashMap<String, Object> jsondata);

	MasItemType getItemTypeIdNiv(String itemTypeCode);

	//List<Object[]> getPreviousDgMasInvestigationsAndResult(Long patientId);

	//Map<String, Object> getPreviousInvestigations(HashMap<String, Object> jsonData);
	Map<String, Object> getPreviousDgMasInvestigationsAndResult(Long patientId, HashMap<String, Object> jsonData);
	
	//List<OpdObesityHd> getObsisityRecord(Long visitId);

	String opdupdateTreatmentTemplate(OpdTemplate opdTemp, List<OpdTemplateTreatment> opdTreatmentList);

	Map<String, Object> getPreviousVisitRecord(Long patientId, HashMap<String, Object> jsonData);
			
	
	String opdTemplateMedicalAdvice(OpdTemplate opdTemp, OpdTemplateMedicalAdvice opdTemplateMedicalList);

	String childVacatinationChart(List<ChildVacatinationChart> cvc);

	List<ChildVacatinationChart> getchildVacatinationChart(Long patientId);
	//String checkAuthenticateEHR(HashMap<String, Object> jsondata);
	String authenticateUHID(HashMap<String, Object> jsondata);
	Long getPatientIdUHIDWise(HashMap<String, Object> jsondata);
	String getRankBySerivceNo(String serviceNo);

	String getRelationName(long relationId);

	List<MasUnit> getUnitList(JSONObject jsonObject);

	List<MasRank> getRankList();

	List<MasAdministrativeSex> getGenderList();

	Long getPatientFromUhidNo(String uhidNO);

	Patient getPatient(Long patientId);

	boolean updatePatientDetails(String serviceNo, Long patientId, Long adminSexId,
			String patientDOB);

	Long createPatient(JSONObject jsonObj);
	
	Patient getPatientByServiceNo(JSONObject jsonObject);

	String updateVisitStatusReject(HashMap<String, Object> jsondata);
	
	Map<String, Object> getAdmissionDischargeRegister(Map<String, String> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	MasStoreSection getSectionId(String sectionCode);
	
	//String saveObesityEntry(HashMap<String, String> jsondata, HttpServletRequest request, HttpServletResponse response);
	
	Long createPatientForSHO(JSONObject jsondata);

	List<PatientSymptom> getPatientSymptom(Long visitId);

	String deletePatientSymptom(HashMap<String, Object> jsondata);

	String deleteRefferalDetailsNo(Long refferalPatinetHd, Long refferalPatientDt, Long opdPatientDetailsId);

	Map<String, Object> getMasterDoctorRemarks(String idRemarks);

	List<AuditException> getAuditOpdData(Long visitId);

	List<MasAudit> getUniqueNameID(Long auditId);

	List<PatientDataUpload> getPatientActiveImage(Long patientId);

	Map<String, Object> getPatientRecord(long patientId, HashMap<String, Object> jsondata);

	Map<String, Object> getOpdPreviousAuditorRemarks(Long patientId, HashMap<String, Object> jsondata);


	}
