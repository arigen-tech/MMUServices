package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DgSampleCollectionDt;
import com.mmu.services.entity.DgSampleCollectionHd;
import com.mmu.services.entity.DgSubMasInvestigation;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasSubChargecode;
import com.mmu.services.entity.Patient;

@Repository
public interface LabDao {

	/************************************************ SampleCollection ****************************************  */
	Map<String, Object> getPendingSampleCollectionWaitingListGrid(JSONObject jsonObject);
	Map<String, List<?>>  getPendingSampleCollection(JSONObject jsonObject);
	Map<String, List<?>> getPendingSampleCollectionDetails(JSONObject jsonObject);	
	List<?> getInvestigationListdgOrderdtwise(JSONObject jsonObject);
	Long submitSampleCollectionHeader(DgSampleCollectionHd sampleCollectionHd);
	Long submitSampleCollectionDetails(DgSampleCollectionHd sampleCollectionHd, DgSampleCollectionDt sampleCollectionDt);
	//List<DgSampleCollectionDt> submitSampleCollectionDetails(DgSampleCollectionDt sampleCollectionDt);
	Long getVisitIdFromDgOrderHd(Long orderhdId);
	boolean updateOrderStatusDgOrderHd(Long orderhdId);
	List<DgSampleCollectionDt> getPendingSampleValidateList(JSONObject jsonObject);
	Map<String, Object> getPendingSampleValidateDetails(JSONObject jsonObject);
	List<DgSampleCollectionDt> getPendingSampleValidateDetailsForSampleValidate(JSONObject jsonObject);
	
	boolean submitSampleCollectionHeaderAndDetails(DgSampleCollectionHd sampleCollectionHd, JSONObject jsonObject);
	List<DgOrderdt> getDgOrderdtId(Long orderhdId);
	List<DgSampleCollectionDt> updateOrderStatusDgOrderDt(List<DgSampleCollectionDt> sampleCollectionDtId);
	List<DgSampleCollectionDt> getDgOrderDtsIdsFromSampleCollDt(Long sampleCollHeaderId, String[] investigations);
	boolean updateDgOrderDt(List<DgSampleCollectionDt> dtidList);
	List<DgOrderdt> getDgOrderdtIdForUpdates(Long orderhdId);
	DgSampleCollectionHd getSampleCollectionHdId(Long orderhdId);	
	Long getVisitIdFromDgSampleCollectionHeader(Long sampleCollectionHdId, Session session);
	List<DgSampleCollectionDt> getDgSampleCollectionDtIds(Long sampleCollectionHdId);	
	List<DgSampleCollectionDt> getSampleCollectionDtsIdsubChrgCode(Long sampleCollectionHdId, Long subchargeCodeId, Long investIDArray);
	boolean updateDgSampleCollectionDt(List<DgSampleCollectionDt> sampleCollectionDtIds, String[] reason, String[] additionalRemarks, String acceptflag);
	List<DgSampleCollectionDt> getDgSampleCollectiondtIdForUpdates(List<Long> sampleCollectionHdId, Session session);
	boolean updateStatusDgSampleCollectionHd(Long sampleCollectionHdId);
	
	List<DgSampleCollectionDt> getDgSampleCollectionDtUOMId(Long sampleCollectionHdId);
	
	//List<MasSubChargecode> getAllSubChargeCode(List<?> investigationList);
	List<DgOrderdt> getAllInvestigationFromDgOrderDt(String orderhdId);
	List<MasSubChargecode> getSubChargeCode(Long sampleCollectionHdId);
	Map<String, Object> getPendingSampleValidateListGrid(JSONObject jsonData);
	/************************************************************ Result Entry *********************************************************/
	/**
	 * 
	 * @param jsonObject
	 * @return
	 */
	Map<String, Object> getResultEntryWaitingListGrid(JSONObject jsonObject);
	List<DgSampleCollectionDt> getResultEntryWaitingList(JSONObject jsonObject);
	//List<DgSampleCollectionDt> getResultEntryDetails(Long sampleCollectionHdId, Long subchargeCodeId);
	
	List<DgSampleCollectionDt> getResultEntryDetails(Long sampleCollectionHdId);
	//List<DgSampleCollectionDt> getResultEntryDetailsInvestigations(Long sampleCollectionHdId, Long subchargeCodeId, String investigationType);
	Map<String, Object> getAllInvestigations(Long sampleCollectionHdId);
	//Map<String, Object> getAllInvestigations(Long sampleCollectionHdId, Long subchargeCodeId);
	DgResultEntryDt getResultEntryDetailsObject(JSONObject jsonObject);
	
	DgResultEntryHd getDgResultEntryHd(Long sampleCollectionHdId);
	Long submitDgResultEntry(DgResultEntryHd dgResultEntryHd, Session session);
	Long submitDgResultEntryDt(DgResultEntryDt dgResultEntryDt, Session session);
	List<DgSampleCollectionDt> getDgSampCollDtIdFromDgSamCollDts(Long sampleCollectionHdId, Long subchargecodeId, Session session);
	List<DgMasInvestigation> getTestOrderNoFromDgMasInvestigation(List<Object> investigationsList, Session session);
	List<Object[]> getDgSamCollDtIdFromDgResultEntDts(List<Long> resEntHdIds, Session session);
	boolean updateDgSampleCollectionDtForResultEntry(List<Long> dgSampleCollectionDts, Session session);
	
	boolean updateOrderStatusDgSampleCollectionHd(List<Long> sampleCollectionHdId, Session session);
	List<DgSampleCollectionDt> getDgSampleDtIds(List<Object> invList, Long sampleCollectionHdId);
	//DgResultEntryHd getResultEntryObject(Long investigationid, Long orderHdId, Session session);
	DgResultEntryHd getResultEntryObject(Long subchargeId, Long mainChargeCodeId, Long orderHdId, Session session);
	DgResultEntryDt getInvestigationIdFrmResultEntDt(Long resultEnterHdId);
	List<Object[]> getHeadeIdFrmDgSampleCollectionDt(List<Long> dgSampleCollectionDts, Session session);
	
	/************************************************************ Result Validation ***************************************************/
	Map<String, Object> getResultValidationWaitingListGrid(JSONObject jsonObject);
	List<DgResultEntryHd> getResultValidationWaitingList(JSONObject jsonObject);	
	Map<String, Object> getResultValidationDetails(Long resultEntryDetailId);
	Map<String, Object> getInvestigationDtFromDgResultEntryDt(Long resultEntryDetailId);
	List<DgSubMasInvestigation> getSubInvestigationDetails(List listSubInvId);
	List<DgResultEntryDt> getResultEntDtIds(Long resultEntHdId);
	boolean updateResultEntryHd(Long resultEntHdId, Long userId);
	DgSampleCollectionDt getDgSampleCollectionDt(long sampleCollectionHeaderId);
	//List<DgResultEntryDt> getAllResultEntDtId(List<Long> subInvIds, List<Long> InvIds, Long hdId);
	List<DgResultEntryDt> getAllResultEntDtId(Long resultEntryHdId, String [] parentInvestigationId);
	boolean submitResultValidationDetails(String[] resultDtIds, String[] remarks, String[] resultValues, String[] rangeStatus,  String[] normalRangeValues, String[] subInvestigationIdArray, String[] parentInvIdsArrayValues);
	List<Patient> getPatientList(JSONObject jsonObject);
	Map<String, Object> getLabHistory(JSONObject jsonObject);
	
	Map<String, Object> getResultEntryUpdateDetails(Long resultEntryHdId);
	boolean updateResultEntryDetails(String[] resulEntDtIdArray, String[] remarks, String[] resultValues, Long userId, String[] subInvestigationIdArray, String[] normalRangeValue, String[] parentInvestigationIdArray);
	
	
	/************************************************** OutSide Patient Waiting List *******************************************/
	Map<String, Object> getOutSidePatientWaitingList(JSONObject jsonObject);
	List<DgOrderhd> getOutSidePatientDetails(JSONObject jsonObject);
	Map<String, Object> getOutSidePatientInvestigationDetails(JSONObject jsonObject);
	
	Map<String,Object> getResultUpdateWaitingList(JSONObject jsonObject);
	Map<Long, Object> validateResultEnteredRange(String[] subInvestigationIdsArray, String[] resultValue, String[] normalRangeValue);
	Map<Long, Object> validateResultEnteredRangeForUpdate(String[] subInvestigationIdsArray, String[] resultValue, String[] normalRangeValue, String[] parentInvestigationIdArray);
	Map<String, Object> getSampleRejectedWaitingList(JSONObject jsonObject);
	//Map<Long, Object> validateResultEnteredRangeForSingleParameter(JSONArray investigationId, JSONArray result,	JSONArray normalRange);
	//List<DgSampleCollectionDt> getSampleRejectedResultEntryDetails(Long sampleCollectionHdId, Long subchargeCodeId);
	Map<String, Object> getAllRejectedInvestigations(Long sampleCollectionHdId, Long subchargeCodeId,
			String investigationType);
	//List<DgMasInvestigation> getAllInvestigationsForAllInvestigation(List investigationList);
	//Map<String, Object> validateResultEnteredRangeForUpadate(String[] subInvestigationIdsArray, String[] resultValue, String[] normalRangeValue, List<Long> dtIds);
	Map<String, Object> getSampleRejectedDetails(JSONObject jsonObject);
	List<DgSampleCollectionDt> getAllRejectedInvestigations(JSONObject jsonObject);
	List<DgSampleCollectionDt> getSampleCollectionDtsIdForReject(Long sampleCollectionHdId, Long subchargeCodeId, Long investID);
	boolean updateDgSampleCollectionDtForReject(List<DgSampleCollectionDt> sampleCollectionDtIds, String[] reason,
			String[] additionalRemarks, String acceptflag);
	
	Map<String, Object> findAuthenticateUHID(HashMap<String, Object> jsondata);
	String checkAuthenticateUser(HashMap<String, Object> jsondata);
	String checkAuthenticateServiceNo(HashMap<String, Object> jsondata);
	
	Map<String, Object> getLabHistoryDetails(HashMap<String, Object> jsondata);
	
	Map<String, Object> submitSampleCollectionDetailsAll(JSONObject jsonObject, DgSampleCollectionHd sampleCollectionHd, Long visitId);
	List<DgMasInvestigation> getInvestigationList(Map<String, Object> requestData);
	MasCamp getMasCampFromMMUId(Long mmuId, Session session);
	
	//
	
}
