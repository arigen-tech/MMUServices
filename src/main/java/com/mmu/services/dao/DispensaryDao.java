package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSupplierNew;
import com.mmu.services.entity.MasStoreSupplierType;
import com.mmu.services.entity.StoreInternalIndentM;
import com.mmu.services.entity.StoreInternalIndentT;
import com.mmu.services.entity.Users;

@Repository
public interface DispensaryDao {
	/// ----------------create Indent Start(Anita)-------------------------------//

	long savestoreInternalIndentT(StoreInternalIndentT storeInternalIndentT);

	MasStoreItem getMasStoreItem(long itemId);

	MasHospital gettoMasHospital(long hospitalId);

	Users getUser(long userId);

	long savestoreInternalIndentM(StoreInternalIndentM storeInternalIndentM);

	Map<String, Object> getIndentList(long pageNo, HashMap<String, String> requestData);

	Map<String, Object> getIndentDetails(HashMap<String, String> jsondata);

	int deleteIndentItem(long indentId);

	StoreInternalIndentT getStoreInternalIndentT(long parseLong);

	long saveOrUpdatSstoreInternalIndentT(StoreInternalIndentT storeInternalIndentT);

	StoreInternalIndentM getStoreInternalIndentM(long indentmId);

	void updateStoreInternalIndentM(StoreInternalIndentM storeInternalIndentM);

	MasDepartment getMasDepartment(long dept);

	/// ----------------create Indent End(Anita)-------------------------------//

	Map<String, Object> getPendingPrescriptionList(Map<String, Object> jsondata);

	Map<String, Object> getPrescriptionHeader(Map<String, Object> jsondata);

	Map<String, Object> getPrescriptionDetail(Map<String, Object> jsondata);

	Map<String, Object> getBatchDetail(Map<String, Object> jsondata);

	String issueMedicineFromDispensary(Map<String, Object> jsondata);

	Map<String, Object> getPartialWaitingList(Map<String, Object> jsondata);

	Map<String, Object> getPartialIssueHeader(Map<String, Object> jsondata);

	Map<String, Object> getPartialIssueDetails(Map<String, Object> jsondata);

	String partialIssueMedicineFromDispensary(Map<String, Object> jsondata);

	Map<String, Object> indentIssueWaitingList(Map<String, Object> jsondata);

	Map<String, Object> getIndentIssueHeader(Map<String, Object> jsondata);

	Map<String, Object> getIndentIssueDetails(Map<String, Object> jsondata);

	Map<String, Object> indentIssue(Map<String, Object> jsondata);

	Map<String, Object> getIssuNoAndIndentNo(Map<String, Object> jsondata);

	Map<String, Object> getDrugExpiryList(Map<String, Object> jsondata);

	Map<String, Object> getROLDataList(Map<String, Object> jsondata);

	Map<String, Object> getVisitListAndPrescriptionId(Map<String, Object> jsondata);

	String getPrescriptionId(long jsondata);

	Map<String, Object> getNisRegisterData(Map<String, Object> jsondata);

	Map<String, Object> getDailyIssueSummaryData(Map<String, Object> jsondata);

	Map<String, Object> getDepartmentIdAgainstCode(Map<String, Object> payload);

	Long getItemTypeId(Map<String, Object> payload);

	long submitDispenceryIndent(HashMap<String, Object> jsondata);

	Map<String, Object> getRegisteredPatientList(Map<String, Object> payload);

	Map<String, Object> getRegisteredPatientDetail(Map<String, Object> payload);

	Map<String, Object> updatePatientInformation(Map<String, Object> requestData);

	Map<String, Object> getPendingListForAuditor(long pageNo, HashMap<String, String> requestData);

	Map<String, Object> getPendingListForCO(long pageNo, HashMap<String, String> requestData);

	Map<String, Object> displayItemListCO(Map<String, Object> requestData);

	Map<String, Object> forwardToDisctrict(Map<String, Object> requestData);

	Map<String, Object> getPendingListForDO(long pageNo, HashMap<String, String> requestData);

	Map<String, Object> indentValidationDO(HashMap<String, String> payload);

	Map<String, Object> updateIndentDispenceryByDO(HashMap<String, Object> jsondata);

	Map<String, Object> displayItemListDO(Map<String, Object> requestData);

	Map<String, Object> mmuWiseIndentDetail(Map<String, Object> requestData);

	Map<String, Object> submitDoItemsAndGeneratePo(Map<String, Object> requestData);

	Map<String, Object> getCityWiseIndentList(Map<String, Object> requestData);

	Map<String, Object> getMasSupplierList(Map<String, Object> requestData);

	Map<String, Object> getMasSupplierTypeList(Map<String, Object> requestData);

	Map<String, Object> getRvWaitingList(Map<String, Object> payload);

	Map<String, Object> rvDetail(HashMap<String, String> payload);

	Map<String, Object> indentIssueWaitingListForDO(Map<String, Object> payload);

	String submitRvDetailAgainstPo(Map<String, Object> payload);

	Map<String, Object> getIndentIssueHeaderDo(Map<String, Object> jsondata);

	Map<String, Object> getIndentIssueDetailsDo(Map<String, Object> jsondata);

	String indentIssueDo(Map<String, Object> jsondata);

	Map<String, Object> getIndentListCo(long pageNo, HashMap<String, String> requestData);

	Map<String, Object> getIndentListDo(long pageNo, HashMap<String, String> requestData);

	Map<String, Object> getDrugList(Map<String, Object> payload);

	String updateUnitRate(Map<String, Object> payload);

	Map<String, Object> getIndentDetailsForTracking(HashMap<String, String> jsondata);

	Map<String, Object> getIndentDetailsForTrackingCo(HashMap<String, String> jsondata);

	Map<String, Object> getMasSupplierListNew(Map<String, Object> requestData);

	Map<String, Object> getIndentDetailsCo(HashMap<String, String> jsondata);
	
	 List<MasStoreSupplierType> getMasStoreSupplierType();
	 List<MasStoreSupplierNew> getMasStoreSupplierNew();
	 List<MasStoreSupplierNew> getMasStoreSupplierNew(Long id, Long districtId);

	 
}
