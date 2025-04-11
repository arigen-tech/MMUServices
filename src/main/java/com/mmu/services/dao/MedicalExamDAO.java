package com.mmu.services.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DischargeIcdCode;
import com.mmu.services.entity.FamilyDetail;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasMedicalExamReport;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.PatientDiseaseInfo;
import com.mmu.services.entity.PatientImmunizationHistory;
import com.mmu.services.entity.PatientMedicalCat;
import com.mmu.services.entity.PatientPastMedHistory;
import com.mmu.services.entity.PatientServicesDetail;
import com.mmu.services.entity.ReferralPatientDt;
import com.mmu.services.entity.ReferralPatientHd;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;

@Repository
public interface MedicalExamDAO extends GenericDao<MasMedicalExamReport, Long>{

	/*
	 * Map<String, Object> getValidateMedicalExamList(HashMap<String, Object>
	 * jsonData); Map<String, Object> getValidateMedicalExamDetails(HashMap<String,
	 * Object> jsonData); Long saveOrUpdateVisit(Visit visit); DgOrderhd
	 * getDgOrderHdByVisitIdAndPatient(Long visitId, Long patientId); String
	 * saveOrUpdateMedicalExam(MasMedicalExamReport
	 * masMedicalExamReprt,HashMap<String, Object> jsonData,String patientId, String
	 * hospitalId,String userId); MasMedicalExamReport
	 * getMasMedicalExamReprtByVisitId(Long visitId); Long
	 * saveOrUpdateMasPastMedicalHistory(PatientPastMedHistory
	 * patientPastMedHistory); DgResultEntryHd
	 * getDgResultEntryHdByPatientIdAndHospitalId(Long dgresultHdId); Long
	 * saveOrUpdateDgResultEntryHd(DgResultEntryHd dgResultEntryHd); Long
	 * saveOrUpdateDgResultEntryDt(DgResultEntryDt dgResultEntryDt);
	 * //List<Object[]> getDgMasInvestigationsAndResult(Long visitId);
	 * DgResultEntryDt getDgResultEntryDtByDgResultEntryId(Long dgResultEntryId);
	 * DgResultEntryHd getDgResultEntryHdByPatientIdAndHospitalIds(Long patientId,
	 * Long hospitalId); OpdPatientDetail getOpdPatientDetailByVisitId(Long
	 * visitId); Map<String, Object> getApprovalMedicalExamList(HashMap<String,
	 * Object> jsonData); MasUnit getMasUnitByHospitalId(Long hospitalId);
	 * PatientMedicalCat getPatientMedicalCat(Long patientMedicalCatId); Long
	 * saveOrUpdatePatientMedicalCat(PatientMedicalCat patientMedicalCat);
	 * List<MasHospital> getMasHospitalList(HashMap<String, Object> jsonData); Users
	 * getUserByUserId(Long userId); HashMap<String, Object>
	 * getMedicalExamListByStatus(HashMap<String, Object> jsonData); Long
	 * saveOrUpdatePatient(Patient patient); MasMedicalCategory
	 * getMasMedicalCategoryByCatId(Long medicalCatId); Long
	 * saveOrUpdateMasMedicalCategory(MasMedicalCategory masMedicalCategory);
	 * List<MasUnit> getMasUnitListByHospitalId(Long hospitalId);
	 * PatientImmunizationHistory getPatientImmunizationHistory(Long visitId, Long
	 * itemId); Long
	 * saveOrUpdatePatientImmunizationHistory(PatientImmunizationHistory
	 * patientImmunizationHistory); Map<String, Object>
	 * getPatientImmunizationHistoryByVisitId(Long visitId,HashMap<String, Object>
	 * jsonData); Patient checkPatientForPatientId(Long i); Long
	 * updateOpdPatientDetailMe(OpdPatientDetail opdPatientDetail);
	 * ReferralPatientDt getReferralPatientDtByReferaldtIdMe(Long referalDtId);
	 * ReferralPatientHd getPatientReferalHdByExtHospitalIdMe(Long extHospitalId,
	 * Long opdPatientDetailId, String referalFlagValue); Long
	 * saveOrUpdateReferalHdMe(ReferralPatientHd referralPatientHd); Long
	 * saveOrUpdateReferalDtMe(ReferralPatientDt referralPatientDt); Map<String,
	 * Object> getMEWaitingGridAFMS18(HashMap<String, Object> jsondata); Map<String,
	 * Object> getpatientDetailForAFMS18(HashMap<String, Object> jsonData);
	 * 
	 * String saveUpdateAllTransation(MasMedicalExamReport masMedicalExamReprt,
	 * HashMap<String, Object> payload,String patientId, String hospitalId,String
	 * userId,String saveinDraft); List<PatientServicesDetail>
	 * getServiceDetails(Long parseLong); List<PatientDiseaseInfo>
	 * getPatientDiseaseWoundInjuryDetail(Long parseLong); PatientServicesDetail
	 * getServiceDetailsByServiceDetailId(Long serviceDetailId); PatientDiseaseInfo
	 * getPatientDiseaseWoundInjuryDetailByDiseaseInfoId(Long diseaseInfoId);
	 * Integer getInvestigationResultEmpty(Long visitId); List<MasUnit>
	 * getMasUnitByUnitCode(String unitCode); List<DgMasInvestigation>
	 * getInvestigationListUOM(String mainChargeCode,HashMap<String, Object>
	 * jsondata); String saveOrUpdateMedicalExam3A(MasMedicalExamReport
	 * masMedicalExamReprt, HashMap<String, Object> payload, String patientId,
	 * String hospitalId, String userId); OpdPatientHistory
	 * checkVisitOpdPatientHistoryByVisitIdAndPatientId(Long visitId); Long
	 * saveOrUpdateOpdPatientHis(OpdPatientHistory opdPatientHistory); Users
	 * getUserByUserId(String serviceNumber); List<Object[]>
	 * getMasStoreItemByItemCode(); Map<String, Object>
	 * getAllCompleteMedicalExam(HashMap<String, Object> jsonData); List<Visit>
	 * getPatientVisitForMe(Long visitId);
	 * 
	 * Map<String, Object> getAllMasMedicalExamReportForDigi(HashMap<String, Object>
	 * jsonData); String saveOrUpdateDigifileUpload(MasMedicalExamReport
	 * masMedicalExamReprt, HashMap<String, Object> payload, String patientId,
	 * String hospitalId, String userId); String
	 * saveUpdateAllTransationDigiFileUpload(MasMedicalExamReport
	 * masMedicalExamReprt, HashMap<String, Object> payload, String patientId,
	 * String hospitalId, String userId, String saveInDraft); String
	 * getRankOfUserId(Users users); MasMedicalExamReport
	 * updateInvestigationDgResultForDigiUpload(HashMap<String, Object> payload,
	 * String patientId, String hospitalId, String userId, MasMedicalExamReport
	 * masMedicalExamReport, String[] rangeValue, String[] rmsIdFinalValue,Timestamp
	 * ts); MasMedicalExamReport
	 * saveUpdateForReferalforMeDigiFileUpload(MasMedicalExamReport
	 * masMedicalExamReprt, HashMap<String, Object> payload,String hospitalId);
	 * 
	 * void updateVisitStatus(String visitId, String status); MasMainChargecode
	 * getMainChargeCodeByMainChargeCodeId(Long mainChargeCodeId); void
	 * updateVisitStatusForDigi(String visitId, String status, String
	 * hospitalId,String dateOfExam); String saveUpdateOpdObsistyMe(HashMap<String,
	 * Object> payload); Long saveUpdateOfPatientDocumentDetail(HashMap<String,
	 * Object> payload); PatientImmunizationHistory
	 * getPatientImmunizationHistoryByDate(Long visitId, Long itemId, Long
	 * patientId, Date immunizationDate,String flag); DgResultEntryHd
	 * getDgResultEntryHdByPatientIdAndHospitalIdAndInves(Long patientId, Long
	 * hospitalId, Long investigationId); List<Object[]>
	 * getDgMasInvestigationsAndResultForSubInvestigation(Long visitId);
	 * List<Object[]> getDgMasInvestigationsAndResult(Long visitId, List<Long>
	 * investigationId);
	 * 
	 * Map<String, Object> getMEMBHistory(HashMap<String, Object> jsondata); String
	 * saveOrUpdateMedicalExam3AForDigi(MasMedicalExamReport masMedicalExamReprt,
	 * HashMap<String, Object> payload, String patientId, String hospitalId, String
	 * userId); Long saveOrUpdateDgOrderdtForDgOrderdt(DgOrderdt dgOrderDt); Long
	 * saveOrUpdateMasMedicalExamReport(MasMedicalExamReport masMedicalExamReport);
	 * Users getUserByUserIdList(Long userId); DischargeIcdCode
	 * getDischargeIcdByVisitIdandIcdId(Long visitId, Long IcgId); String
	 * saveOrUpdateMedicalExam2AForDigi(MasMedicalExamReport masMedicalExamReprt,
	 * HashMap<String, Object> payload, String patientId, String hospitalId, String
	 * userId); List<PatientMedicalCat> getPatientMedicalCat(Long patientId, Long
	 * visitId, String fitFlag); void saveOrUpdateFamilyDetail(FamilyDetail
	 * familyDetails); MasMedicalExamReport
	 * updateInvestigationDgResult(HashMap<String, Object> payload, String
	 * patientId, String hospitalId, String userId, MasMedicalExamReport
	 * masMedicalExamReport); PatientImmunizationHistory
	 * getPatientImmunizationHistoryByVisit(Long visitId, Long itemId, Long
	 * patientId); PatientImmunizationHistory
	 * getPatientImmunizationHistoryByImmunizationId(Long immunizationId); Long
	 * saveOrUpdatePatientImmunizationHistoryComm(PatientImmunizationHistory
	 * patientImmunizationHistory, Session session); String
	 * deletePatientMedCat(PatientMedicalCat patientMedicalCat);
	 * MasMedicalExamReport getMasMedicalExamReprtByVisitIdForStatus(Long visitId);
	 * MasRelation checkRelationByName(String relationName); Long
	 * saveUpdateOfPatientDocumentDetailDoc(HashMap<String, Object> payload); String
	 * getRHQByVisitId(Long visitId); String
	 * getMainChargeCodeByMainChargeCodeIdOp(List<Object[]> mainChargeCodeId);
	 * PatientImmunizationHistory getPatientImmunizationHistoryByDateDigi(Long
	 * visitId, Long itemId, Long patientId, Date immunizationDate, String flag);
	 * List<Object[]> getAllRHQ(); List<MasHospital> getRMOList(HashMap<String,
	 * Object> jsonData); String getRHQByVisitId(Long visitId, Long
	 * forwardedUnitId); Map<String, Object> getApprovalData(HashMap<String, Object>
	 * jsonData); Map<String, Object> getTemplateInvestigationoDiver();
	 * MasMedicalExamReport getMasMedicalExamReprtByMedicalId(Long medicalId); Long
	 * saveUpdateMedicalExamApprovalData(MasMedicalExamReport masMedicalExamReport);
	 */
}
