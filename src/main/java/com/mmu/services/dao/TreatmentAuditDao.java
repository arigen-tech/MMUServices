package com.mmu.services.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.AuditException;
import com.mmu.services.entity.MasAudit;

@Repository
public interface TreatmentAuditDao {

	Map<String, Object> getAuditVisit(HashMap<String, Object> jsondata);

	Map<String, Object> getMasIcdByVisitPatAndOpdPD(Long visitId, HashMap<String, String> jsondata);

	Map<String, Object> getDgMasInvestigations(Long visitId);

	Map<String, Object> getTreatementDetail(Long visitId);

	String saveTreatmentAuditDetails(HashMap<String, Object> jsondata);

	Map<String, Object> getRecommendedDiagnosisAllDetail(String patientSympotnsId);

	Map<String, Object> getRecommendedInvestgationAllDetail(String patientSympotnsId);

	Map<String, Object> getRecommendedTreatmentAllDetail(String patientSympotnsId);

	AuditException checkExistingAuditData(Long visitId, boolean status);

	List<Long> getAIDiagnosisDetail(String patientSympotnsId, Long diagnosisId);

	List<Long> getAIInvestgationDetail(String patientSympotnsId, Long investgationId);

	List<Long> getAITreatmentDetail(String patientSympotnsId, Long treatmentId);

	Map<String, Object> getAllSymptomsForOpd(String name);

	Map<String, Object> getAllIcdForOpd(String name);

	Map<String, Object> getExpiryMedicine(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	
}
