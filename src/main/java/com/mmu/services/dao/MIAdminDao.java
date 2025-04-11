package com.mmu.services.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mmu.services.entity.AppAuditEquipmentDt;
import com.mmu.services.entity.AppEquipmentDt;
import com.mmu.services.entity.AppEquipmentHd;
import com.mmu.services.entity.EmployeeBloodGroup;
import com.mmu.services.entity.HivStdRegister;
import com.mmu.services.entity.HospitalVisitRegister;
import com.mmu.services.entity.InjuryRegister;
import com.mmu.services.entity.MasBloodGroup;

import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasEmployeeCategory;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRegistrationType;
import com.mmu.services.entity.MasServiceType;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.MilkTestingRegister;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.SanitaryDiary;
import com.mmu.services.entity.WaterTestingRegister;



@Repository
public interface MIAdminDao {

	/*
	 * Map<String, Object> getDisposalTypeList(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * Map<String, Object> getAllDisposalTypeList(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * List<MasEmployeeCategory> getEmpCategoryList();
	 * 
	 * List<MasMedicalCategory> getMedCategoryList();
	 * 
	 * Map<String, Object> getAmeReportList(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * Map<String, Object> getMedicalStatistic(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * List<MasBloodGroup> getBloodGroupList();
	 * 
	 * Map<String, Object> getSimilarBloodGroupPatient(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * List<MasUnit> getMasUnitList();
	 * 
	 * long saveOrUpdate(SanitaryDiary sanitaryDiary);
	 * 
	 * Map<String, Object> getSanitaryReportList(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * Patient getNameByServiceNo(String serviceNo, String hospitalId);
	 * 
	 * long saveOrUpdateInjuryRegister(InjuryRegister injuryRegister);
	 * 
	 * Map<String, Object> getInjuryReportList(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * long saveOrUpdateHospitalVisitRegister(HospitalVisitRegister
	 * hospitalVisitRegister);
	 * 
	 * Map<String, Object> getHospitalVisitReportList(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * long saveOrUpdateHivStdRegister(HivStdRegister hivStdRegister);
	 * 
	 * Map<String, Object> getHivStdList(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * long saveOrUpdateMilkTesting(MilkTestingRegister milkTestingRegister);
	 * 
	 * Map<String, Object> getMilkTestingList(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * long saveWaterTesting(WaterTestingRegister waterTestingRegister);
	 * 
	 * Map<String, Object> getWaterTestingList(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * List<MasEmployee> getEmpList(long parseLong);
	 * 
	 * Map<String, Object> getDangerousDrugRegisterList(long pageNo, HashMap<String,
	 * String> requestData);
	 * 
	 * Map<String, Object> getManufactureList(Map<String, String> requestData);
	 * 
	 * long submitEquipmentDetails(HashMap<String, Object> jsondata);
	 * 
	 * Map<String, Object> getEquipmentDetails(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * long submitWarrantydetails(HashMap<String, Object> jsondata);
	 * 
	 * AppEquipmentDt getAppEquipmentDt(HashMap<String, Object> jsondata);
	 * 
	 * long submitAccessarydetails(HashMap<String, Object> jsondata);
	 * 
	 * AppEquipmentHd getEquipmentDetails(String itemId, String hospitalId);
	 * 
	 * Map<String, Object> getEquipmentList(long pageNo, HashMap<String, String>
	 * requestData);
	 * 
	 * Map<String, Object> getEquipmentDetailsForstoreLedger(long pageNo,
	 * HashMap<String, String> requestData);
	 * 
	 * List<AppEquipmentDt> appEquipmentDtList(long parseLong, String rvNumber);
	 * 
	 * int submitBoardOutDetails(HashMap<String, Object> jsondata);
	 * 
	 * List<AppEquipmentHd> getAppEquipmentHdList(String itemId, String hospitalId);
	 * 
	 * long saveOrUpdateAppAuditEquipmentDt(AppAuditEquipmentDt
	 * appAuditEquipmentDt);
	 * 
	 * int update(AppEquipmentDt appEquipmentDt);
	 * 
	 * List<MasDisposal> getDisposalList();
	 * 
	 * MasEmployee getEmpInfoByServiceNo(String serviceNo, String hospitalId);
	 * 
	 * MasUnit getMasUnit(String masUnit);
	 * 
	 * MasRank getMasRank(String masRank);
	 * 
	 * long saveOrUpdateEmployeeBloodGroup(EmployeeBloodGroup employeeBloodGroup);
	 * 
	 * MasUnit getMasUnitById(long unitId);
	 * 
	 * MasRank getMasRankById(long rankId);
	 * 
	 * long submitHospitalVisitRegister(HashMap<String, Object> jsondata);
	 * 
	 * Map<String, Object> getMedicalCategorylistForHIVstd(String jsondata);
	 * 
	 * long saveOrUpdateHivStdRegisterData(HashMap<String, Object> jsondata);
	 * 
	 * List<MasRank> getRankList();
	 * 
	 * EmployeeBloodGroup getemployeeBloodGroup(String serviceNo, String
	 * hospitalId);
	 * 
	 * EmployeeBloodGroup getEmployeeBloodGroupById(long empBloodGroupId);
	 * 
	 * long saveOrUpdatePatientDetails(Patient patient);
	 * 
	 * MasServiceType getServiceType(String serviceTypeCode);
	 * 
	 * MasRegistrationType getRegistrationType(String registrationTypeCode);
	 * 
	 * Map<String, Object> getHospitalVisitDetails(HashMap<String, String>
	 * requestData);
	 * 
	 */
}
