package com.mmu.services.dao;

import java.util.Date;
import java.util.Map;

import com.mmu.services.entity.FundHcb;
import com.mmu.services.entity.MasStoreFinancial;

public interface FundHcbDao {
	public Long getHcbFund();
	public FundHcb getFundHcb(Long upss_id,Long head_type_id,Date currentDate);
	//public FundHcb getFundHcb(Long upss_id,Long cityId,Long head_type_id,Date currentDate);
	public FundHcb getFundHcb(Long upss_id,Long head_type_id);
	public Long updateFundHcb(FundHcb fundHcb);
	public Long saveFundHcb(FundHcb fundHcb);
	public Long saveHcbFund(Long upss_id,Long head_type_id,Date currentDate,Long openingBalance,Long drCR,Long hcbBalance);
	Long getFundOpernationalBalance(Long upss_id, Long head_type_id);
	//Map<String, Object> getFundOpernationalUpdatedBalance(Long upss_id, Long head_type_id);
	//Map<String, Object> getFundOpernationalUpdatedBalance(Long upss_id, Long cityId, Long head_type_id);
	//FundHcb getFundHcbCityLevel(Long upss_id, Long cityId, Long head_type_id);
	FundHcb getFundHcbCity(Long upss_id, Long cityId, Long head_type_id, Date currentDate, String phase);
	 
	 
	 
	MasStoreFinancial getMasStoreFinancilaYear(String year);
	Long getCaptureHdCityPhaseLevel(Long upss_id, Long cityId, Long head_type_id, String phase);
	FundHcb getFundHcbCityLevel(Long upss_id, Long cityId, Long head_type_id, String phase);
	Map<String, Object> getFundOpernationalUpdatedBalance(Long upss_id, Long cityId, Long head_type_id, String phase);
	
}
