package com.mmu.services.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.mmu.services.entity.MMUDepartment;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasAppointmentType;
import com.mmu.services.entity.MasBloodGroup;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasCommand;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasIdentificationType;
import com.mmu.services.entity.MasLabor;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.MasMaritalStatus;
import com.mmu.services.entity.MasMedExam;
import com.mmu.services.entity.MasMedicalCategory;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRecordOfficeAddress;
import com.mmu.services.entity.MasRegistrationType;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasReligion;
import com.mmu.services.entity.MasServiceType;
import com.mmu.services.entity.MasState;
import com.mmu.services.entity.MasTrade;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.MasWard;
import com.mmu.services.entity.MasZone;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.Visit;



@Repository
public interface PatientRegistrationDao  {

	/*
	 * Map<String,Object> findPatientAndDependentFromEmployee(String serviceNo);
	 * List<MasDepartment> getDepartmentList(); List<MasBloodGroup>
	 * getBloodGroupList(); List<MasMedicalCategory> getMedicalCategoryList();
	 * Map<String, Object> getTokenNoForDepartmentMultiVisit(long departmentId, long
	 * hospitalId,long appointmentTypeId, String visitFlag, String visitDate,long
	 * patientId); List<MasRegistrationType> getRegistrationTypeList();
	 * List<MasAdministrativeSex> getGenderList(); List<MasIdentificationType>
	 * getIdentificationList(); List<MasServiceType> getServiceTypeList(); long
	 * getPatientFromUhidNo(String uhidNO); long saveVisitForRegisteredPatient(Visit
	 * visit); long savePatient(Patient patient); List<MasRelation>
	 * getRelationList(); Map<String, List<Patient>>
	 * searchOthersRegisteredPatient(String uhinNo, String patientName, String
	 * serviceNo, String mobileNo,long registrationTypeId); Map<String, Object>
	 * getPatientTypeCodeAndRelationCode(long patientRelationId, long
	 * registrationTypeId); String getHinIdOthers(String patientCode); long
	 * getAppointmentSessionId(long hospitalId, long departmentId, long
	 * appointmentTypeId);
	 * 
	 * Map<String,Object> patientListForUploadDocument(String serviceNo);
	 * 
	 * //by Anita Map<String, Object> findPatientAndVisitList(JSONObject json,
	 * String serviceNo, Timestamp fromdateTime, Timestamp todateTime); void
	 * cancelAppointment(long visitId);
	 * 
	 * 
	 * String rescheduleAppointment(long visitId, long tokenNo, Timestamp dateTime);
	 * 
	 * List<Visit> getPatientAppointmentHistory(long uhidNo, String startDate,
	 * String endDate); boolean getPatientVisitCancellation(long visitId);
	 * 
	 * Map<String, Object> findOpdList(String serviceNo); Map<String, Object>
	 * checkVisitExist(long deptId, long appointmentTypeId, long hospitalId, String
	 * uhidNo, String visitFlag, Date visitDate); List<MasAppointmentType>
	 * getAppointmentTypeList();
	 * 
	 * boolean checkExistingOtherPatient(String mobileNo,String patientFirstName);
	 * Patient getPatient(long patientId); void updatePatientNok2Details(Patient
	 * patient);
	 * 
	 * Map<String, Object> getFutureAppointmentWaitingList(long hospitalId, int
	 * pageNo); List<MasState> getStateList(); List<MasDistrict> getDistrictList();
	 * long getNoOfDependentForServiceNo(String serviceNo, long patientRelationId);
	 * boolean checkExitingPatient(String uhidNO); List<MasRank>
	 * getEmpRankAndTrade(String empRankCode); List<MasUnit> getEmpUnitId(String
	 * empUnitCode); Map<String,Object> submitPatientDetails(JSONObject jObject);
	 * long submitPatientDetailsForOthers(JSONObject jObject); long
	 * getAppointmentTypeIdFromCode(String appointmentTypeCode); long
	 * getRelationIdFromCode(String relationCode); List<Object[]>
	 * getInvestigationResultEmptyForOrderNo(String orderNo,Long mainChargeCodeId);
	 * String saveOrUpdateUploadData(String[] investigationIdValues, String[]
	 * dgOrderDtIdValues, String[] dgOrderHdIds, String[] dgResultDtIds, String[]
	 * dgResultHdIds, String[] resultInvss, Long patientId, String hospitalId,
	 * String userId,String orderNumberInv,String[] rangeValues,String []
	 * saveOrUpdateUploadData,HashMap<String, Object> payload,Long visitId);
	 * List<MasAppointmentType> findMasAppointmentTypeByAppCode(); long
	 * savePatientFromUploadDocument(JSONObject jObject); Map<String, Object>
	 * getLabData(JSONObject json); List<MasDepartment>
	 * getDepartmentListFromAppointmentSession(long hospitalId); List<MasRank>
	 * getMasRankList(); List<MasUnit> getMasUnitList(); List<MasTrade>
	 * getMasTradeList(); List<MasCommand> getMasCommandList();
	 * List<MasMaritalStatus> getMasMaritalStatusList();
	 * List<MasRecordOfficeAddress> getMasRecordOfficeList(); Map<String, Object>
	 * opdEmergencySavePatient(JSONObject jObject); long
	 * savePatientForEmergency(JSONObject jsonObj); List<MasState>
	 * getStateListFromDistrict(long districtId); List<MasReligion>
	 * getMasReligionList(); List<Object[]>
	 * getDgMasInvestigationByInvestigationIds(List<Long> listInvestigation, String
	 * string, Long mainChargeCodeId); List<Object[]>
	 * getInvestigationResultEmptyForOrderNoForUnique(String orderNo, Long
	 * mainChargeCodeId); List<Object[]>
	 * getDgMasInvestigationByInvestigationIdsForUnique(List<Long> investigationId,
	 * String orderNo, Long mainChargeCodeId); long createOpdEmergency(JSONObject
	 * jObject, long patientId); List<MasCommand> getRegionFromStation(long
	 * uniitId); long departmentTypeMaternityIdFromCode(String
	 * departmentTypeCodeMaternity); MasEmployee getMasEmployeeFromServiceNo(String
	 * string); List<MasMedExam> getExamList(long appTypeId, Map<String, Object>
	 * jsonData); List<Patient> findPatientByServiceNumber(String serviceNo, Long
	 * registrationTypeId); String getHinNo(String sNo, long relationId, long
	 * registrationTypeId); String getMasMedicalCategoryFromDBFunction(Long
	 * patientId, LocalDate date);
	 */
	List<MasDistrict> getDistrictList(Map<String, Object> requestData);
	List<MasCity> getCityList(Map<String, Object> requestData);
	List<MasMMU> getMMUList(Map<String, Object> requestData);
	Map<String, Object> createCampPlan(Map<String, Object> requestData);
	List<MasZone> getZoneList(Map<String, Object> requestData);
	List<MasWard> getWardList(Map<String, Object> requestData);
	Map<String, Object> getCampDetail(Map<String, Object> requestData);
	//Map<String, Object> savePatientRegistrationAndAppointment(Map<String, Object> requestData);
	List<MasReligion> getReligionList(Map<String, Object> requestData);
	List<MasBloodGroup> getBloodGroupList(Map<String, Object> requestData);
	List<MasIdentificationType> getIdentificationTypeList(Map<String, Object> requestData);
	List<MasLabor> getLabourTyeList(Map<String, Object> requestData);
	List<MasCamp> getCampDepartment(Long mmuId);
	Map<String, Object> updatePatientInformation(Map<String, Object> requestData);
	Map<String, Object> createPatientAndMakeAppointment(Map<String, Object> requestData);
	List<MMUDepartment> getMMUDepartment(Map<String, Object> requestData);
	Map<String, Object> getPatientList(Map<String, Object> requestData);
	Map<String, Object> getOnlinePatientList(Map<String, Object> requestData);
	Visit getPatientDataBasedOnVisit(Long visitId);
	String opdVitalDetails(OpdPatientDetail opddetails);
	List<MasWard> getWardListWithoutCity(Map<String, Object> requestData);
	String opdVitalDetails(HashMap<String, Object> payload);
	List<MasRelation> getRelationList(Map<String, Object> requestData);
	MasMMU getCityIdAndName(Map<String, Object> requestData);
	Map<String, Object> saveLabourRegistration(Map<String, Object> requestData);
	Map<String, Object> checkIfPatientIsAlreadyRegistered(Map<String, Object> requestData);
	Map<String, Object> deleteCampPlan(Map<String, Object> requestData);
	Map<String, Object> getFutureCampPlan(Map<String, Object> requestData);
	
}
