package com.mmu.services.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface RadiologyDao {

	Map<String, Object> getResultValidation(HashMap<String, Object> inputJson);

	String getRankUsingServiceNo(String serviceN);

	Map<String, Object> getResultPrintingData(HashMap<String, Object> jsonData);

	Map<String, Object> saveDocumentData(HashMap<String, Object> inputJson);

}
