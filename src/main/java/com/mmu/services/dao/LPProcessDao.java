package com.mmu.services.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.FundUtilization;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.MasStoreLpType;
import com.mmu.services.entity.MasStoreLpc;
import com.mmu.services.entity.MasStoreSupplier;
import com.mmu.services.entity.StoreBudgetaryM;
import com.mmu.services.entity.StoreBudgetaryT;
import com.mmu.services.entity.StorePoT;
import com.mmu.services.entity.StoreQuotationM;
import com.mmu.services.entity.StoreQuotationT;
import com.mmu.services.entity.StoreSoM;
import com.mmu.services.entity.StoreSoT;


@Repository
public interface LPProcessDao {

	//long saveLPBudgetary(StoreBudgetaryM storeBudgetaryM);

	//long savestoreBudgetaryT(StoreBudgetaryT storeBudgetaryT);

	
	/*
	 * Map<String, Object> getBudgetrayApprovalList(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * Map<String, Object> getIndentDetails(HashMap<String, String> jsondata);
	 * 
	 * StoreBudgetaryM getStoreBudgetaryM(long budMId);
	 * 
	 * MasStoreLpType getmasStoreLpType(Long lpTypeId);
	 * 
	 * void updateStoreBudgetaryM(StoreBudgetaryM storeBudgetaryM);
	 * 
	 * StoreBudgetaryT getStoreBudgetaryT(long budTId);
	 * 
	 * long saveOrUpdateStoreBudgetaryT(StoreBudgetaryT storeBudgetaryT);
	 * 
	 * int deleteIndentItem(long indentId);
	 * 
	 * List<MasStoreSupplier> getVendorList(long hospitalId);
	 * 
	 * MasStoreSupplier getMasStoreSupplier(long parseLong);
	 * 
	 * long saveStoreQuotationM(StoreQuotationM storeQuotationM);
	 * 
	 * long saveStoreQuotationT(StoreQuotationT storeQuotationT);
	 * 
	 * Map<String, Object> getQuotationApprovalList(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * MasEmployee getNameByServiceNo(String serviceNo);
	 * 
	 * long saveMasStoreLpc(MasStoreLpc masStoreLpc);
	 * 
	 * Map<String, Object> getCtmMemberList(long pageNo, HashMap<String, String>
	 * requestData,long hospitalId);
	 * 
	 * MasEmployee getMasEmployee(Long member1Id);
	 * 
	 * MasStoreLpc getMasStoreLpc(Long lpcId);
	 * 
	 * String checkCommitteeExist(Date fromDate2, Date toDate2,long hospitalId);
	 * 
	 * long inactivateCommittee(MasStoreLpc masStoreLpc);
	 * 
	 * Map<String, Object> getItemList(HashMap<String, String> payload);
	 * 
	 * MasStoreLpc getCommitteeMembers(Date quotationDate1,long hospitalId);
	 * 
	 * Map<String, Object> getStoreQuotationM(long budId);
	 * 
	 * int updateStoreQuotationM(StoreQuotationM storeQuotationM);
	 * 
	 * Map<String, Object> getL1ItemList(HashMap<String, String> jsondata);
	 * 
	 * long saveStoreSoM(StoreSoM storeSoM);
	 * 
	 * long saveStoreSoT(StoreSoT storeSoT);
	 * 
	 * Map<String, Object> getSanctionApprovalList(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * Map<String, Object> getSanctionData(HashMap<String, String> jsondata);
	 * 
	 * StoreSoM getStoreSoM(long storeSoMId);
	 * 
	 * int updateStoreSoM(StoreSoM storeSoM);
	 * 
	 * StorePoT getLpRate(String itemId, String hospitalId, String departmentId);
	 * 
	 * MasStoreLpType getMasStoreLpType(long approxCost);
	 * 
	 * List<MasStoreFinancial> financialYearList();
	 * 
	 * long saveFundUtilization(FundUtilization fundUtilization);
	 * 
	 * Map<String, Object> fundUtilizationList(long pageNo, HashMap<String, String>
	 * requestData, long hospitalId, long departmentId);
	 * 
	 * String checkAuthorityNoExist(String authorityNo, long hospitalId, long
	 * departmentId);
	 * 
	 * Map<String, Object> getFundUtilizationInShownYear(long hospitalId, long
	 * departmentId, long yearId, int pageNo);
	 * 
	 * long saveBudgetary(HashMap<String, Object> jsondata);
	 * 
	 * StoreSoM getSanctionData(long storeQMId); long
	 * saveBudgetaryQuotation(HashMap<String, Object> jsondata);
	 * 
	 * long submitSanctionOrder(HashMap<String, Object> jsondata);
	 * 
	 * int deleteQuotation(StoreQuotationM storeQuotationM);
	 * 
	 * void removeDataFromTempTableForBackLp(StoreBudgetaryM storeBudgetaryM);
	 * 
	 * long getTempIdFromBudgetarty(StoreBudgetaryM storeBudgetaryM, long itemId);
	 * 
	 * void updateTempTableStatus(Session sessionP, long tempId, String value);
	 * 
	 */
}
