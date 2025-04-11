package com.mmu.services.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterService {

	String departmentList(HashMap<String, String> jsondata, HttpServletRequest request);

	String getICD(HashMap<String, String> jsondata, HttpServletRequest request);

	String getAllStates(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/******************************
	 * MAS COMMAND
	 ************************************************/
	String getCommandTypeList(HttpServletRequest request, HttpServletResponse response);

	String addCommand(JSONObject json, HttpServletRequest request, HttpServletResponse response);

	String getAllCommand(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);

	String getCommand(HashMap<String, Object> command, HttpServletRequest request);

	String updateCommand(HashMap<String, Object> command, HttpServletRequest request, HttpServletResponse response);

	String statusCommand(HashMap<String, Object> command, HttpServletRequest request, HttpServletResponse response);

	
	/*************************** MAS UNIT ***********************************/
	String addUnits(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String getAllUnit(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String getCommandList(HttpServletRequest request, HttpServletResponse response);

	String getUnitTypeList(HttpServletRequest request, HttpServletResponse response);

	String updateUnit(HashMap<String, Object> unitPayload, HttpServletRequest request, HttpServletResponse response);

	String updateStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/*********************************
	 * MAS HOSPITAL
	 ***************************************/
	String addMasHospital(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getUnitNameList(HttpServletRequest request, HttpServletResponse response);

	String getAllHospital(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateHospitalMasterStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateHospitalDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/*************************************
	 * MAS RELATION
	 *************************************************************************/
	String getAllRelation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateRelationDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateRelationStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addRelation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/****************************************
	 * ADD DISPOSAL
	 ********************************************************************/
	String addDisposal(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllDisposal(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateDisposalDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateDisposalStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/****************************************
	 * MAS APPOINTMENT TYPE
	 *************************************************************/
	String addAppointmentType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllAppointmentType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateAppointmentTypeDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);

	String updateAppointmentTypeStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/****************************************
	 * MAS DEPARTMENT
	 *******************************************************************/
	String getAllDepartment(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getDepartmentTypeList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addDepartment(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateDepartmentDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateDepartmentStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/****************************************
	 * MAS FREQUENCY
	 *********************************************************************/
	String getAllOpdFrequency(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addOpdFrequency(Map<String, Object> jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateOpdFrequencyDetails(Map<String, Object> jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateOpdFrequencyStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/************************************************************************************************/
	String getAllEmpanelledHospital(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addEmpanelledHospital(String impanneledHospPayload, HttpServletRequest request, HttpServletResponse response);

	String updateEmpanelledHospital(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getHospitalListByRegion(HttpServletRequest request, HttpServletResponse response);

	/**************************************
	 * MAS IDEAL WEIGHT
	 *************************************************/
	String getAllIdealWeight(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAge(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateIdealWeight(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String addIdealWeight(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getMasRange(JSONObject json, HttpServletRequest request, HttpServletResponse response);

	/**************************************
	 * Phsiotherapy
	 *************************************************/

	String getAllPhsiotherapy(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addPhsiotherapy(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updatePhysiotherapyDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/**************************************
	 * MAS SERVICE TYPE
	 *************************************************/
	String getAllServiceType(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);

	String updateServiceType(HashMap<String, Object> serviceType, HttpServletRequest request,
			HttpServletResponse response);

	String addServiceType(JSONObject json, HttpServletRequest request, HttpServletResponse response);

	String statusServiceType(HashMap<String, Object> serviceType, HttpServletRequest request,
			HttpServletResponse response);
	
	/******************************MAS RANK************************************************/
	String addRank(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllRank(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String getRank(HashMap<String, Object> rank, HttpServletRequest request);
	String updateRank(HashMap<String, Object> rank, HttpServletRequest request,HttpServletResponse response);
	String statusRank(HashMap<String, Object> rank, HttpServletRequest request,HttpServletResponse response);
	String getEmployeeCategoryList(HttpServletRequest request, HttpServletResponse response);
	
	/******************************MAS Trade************************************************/
	String addTrade(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllTrade(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String getTrade(HashMap<String, Object> trade, HttpServletRequest request);
	String updateTrade(HashMap<String, Object> trade, HttpServletRequest request,HttpServletResponse response);
	String statusTrade(HashMap<String, Object> trade, HttpServletRequest request,HttpServletResponse response);
	String getServiceTypeList(HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS RELIGION*********************************************************************/
	String getAllReligion(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addReligion(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateReligionDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateReligionStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	

	/****************************************MAS Identification*********************************************************************/
	String getAllIdentificationType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addIdentificationType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateIdentificationType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateIdentificationTypeStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS EMPLOYEE CATEGORY*********************************************************************/
	String getAllEmployeeCategory(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addEmployeeCategory(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateEmployeeCategoryDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateEmployeeCategoryStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS Administrative Sex*********************************************************************/
	String getAllAdministrativeSex(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addAdministrativeSex(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateAdministrativeSexDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateAdministrativeSexStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS Medical Category*********************************************************************/
	String getAllMedicalCategory(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addMedicalCategory(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateMedicalCategoryDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateMedicalCategoryStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS Blood Group*********************************************************************/
	String getAllBloodGroup(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addBloodGroup(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateBloodGroupDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateBloodGroupStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS Sample*********************************************************************/
	String getAllSample(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addSample(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateSampleDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateSampleStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS UOM*********************************************************************/
	String getAllUOM(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addUOM(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateUOMDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateUOMStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);

	/****************************************MAS Item Unit*********************************************************************/
	String getAllItemUnit(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addItemUnit(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateItemUnitDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateItemUnitStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	
	
	/****************************************MAS MainChargecode*********************************************************************/
	String addMainChargecode(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllMainChargecode(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String updateMainChargecodeDetails(HashMap<String, Object> mainChargecode, HttpServletRequest request,HttpServletResponse response);
	String updateMainChargecodeStatus(HashMap<String, Object> mainChargecode, HttpServletRequest request,HttpServletResponse response);
	String getDepartmentList(HttpServletRequest request, HttpServletResponse response);
	
	/****************************************Users*********************************************************************/
	String addUsers(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllUsers(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String updateUsersDetails(HashMap<String, Object> users, HttpServletRequest request,HttpServletResponse response);
	String updateUsersStatus(HashMap<String, Object> users, HttpServletRequest request,HttpServletResponse response);
	String getHospitalList(HttpServletRequest request, HttpServletResponse response);

	/****************************************MAS Role*********************************************************************/
	String getAllRole(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addRole(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateRoleDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateRoleStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	/****************************************Mas Range*********************************************************************/
	String getAllRange(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String addRange(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String updateRangeDetails(HashMap<String, Object> range, HttpServletRequest request,HttpServletResponse response);
	String updateRangeStatus(HashMap<String, Object> range, HttpServletRequest request,HttpServletResponse response);
	String getGenderList(HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS StoreGroup*********************************************************************/
	String getAllStoreGroup(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addStoreGroup(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateStoreGroup(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateStoreGroupStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS ItemType*********************************************************************/
	String getAllItemType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addItemType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateItemType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateItemTypeStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/******************************MAS StoreSection************************************************/
	String addStoreSection(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllStoreSection(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String getStoreSection(HashMap<String, Object> storeSection, HttpServletRequest request);
	String updateStoreSection(HashMap<String, Object> storeSection, HttpServletRequest request,HttpServletResponse response);
	String statusStoreSection(HashMap<String, Object> storeSection, HttpServletRequest request,HttpServletResponse response);
	//String getItemTypeList(HttpServletRequest request, HttpServletResponse response);
	
	/******************************MAS ItemClass************************************************/
	String addItemClass(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllItemClass(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String getItemClass(HashMap<String, Object> itemClass, HttpServletRequest request);
	String updateItemClass(HashMap<String, Object> itemClass, HttpServletRequest request,HttpServletResponse response);
	String statusItemClass(HashMap<String, Object> itemClass, HttpServletRequest request,HttpServletResponse response);
	String getStoreSectionList(HttpServletRequest request, HttpServletResponse response);
	
	
	/******************************MAS Section************************************************/
	String addSection(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllSection(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String getSection(HashMap<String, Object> section, HttpServletRequest request);
	String updateSection(HashMap<String, Object> section, HttpServletRequest request,HttpServletResponse response);
	String statusSection(HashMap<String, Object> section, HttpServletRequest request,HttpServletResponse response);
	
	/******************************MAS Item************************************************/
	String addItem(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllItem(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String getItem(HashMap<String, Object> item, HttpServletRequest request);
	String updateItem(HashMap<String, Object> item, HttpServletRequest request,HttpServletResponse response);
	String statusItem(HashMap<String, Object> item, HttpServletRequest request,HttpServletResponse response);
	
	/******************************ME/MB Master************************************************/
	String addMEMBMaster(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllMEMBMaster(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String updateMEMBMaster(HashMap<String, Object> memb, HttpServletRequest request, HttpServletResponse response);
	String updateMEMBStatus(HashMap<String, Object> memb, HttpServletRequest request,HttpServletResponse response);
	
	/******************************ME Investigation Master************************************************/
	String getInvestigationNameList(HttpServletRequest request, HttpServletResponse response);
	String saveMEInvestigation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getAllInvestigationMapping(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String updateMEInvestStatus(HashMap<String, Object> meinv, HttpServletRequest request, HttpServletResponse response);
	String updateInvestigationMapping(HashMap<String, Object> exam, HttpServletRequest request,HttpServletResponse response);
	
	String getAllNiv(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	
	

	/******************************Sub Type Master************************************************/
	String getMainTypeList(HttpServletRequest request, HttpServletResponse response);
	String addSubType(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllSubTypeDetails(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	String updateSubTypeStatus(HashMap<String, Object> meinv, HttpServletRequest request, HttpServletResponse response);
	String updateSubTypeDetails(HashMap<String, Object> mainChargecode, HttpServletRequest request,HttpServletResponse response);
	
	
	/****************************************Vendor Type*********************************************************************/
	String getAllVendorType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addVendorType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateVendorType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateVendorTypeStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************Vendor Master*********************************************************************/
	String getAllVendor(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addVendor(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateVendor(HashMap<String, Object> vendorPayload, HttpServletRequest request,HttpServletResponse response);
	String updateVendorStatus(HashMap<String, Object> vendor, HttpServletRequest request, HttpServletResponse response);
	String getStateList(HttpServletRequest request, HttpServletResponse response);
	String getDistrictList(HashMap<String, Object> payload,HttpServletRequest request, HttpServletResponse response);
	String getDistrictListById(HashMap<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response);
	
	/******************************Sample Container Master************************************************/
	
	String addSampleContainer(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllSampleContainer(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateSampleContainerStatus(HashMap<String, Object> subtype, HttpServletRequest request, HttpServletResponse response);
	String updateSampleContainer(HashMap<String, Object> collection, HttpServletRequest request,HttpServletResponse response);
	
	/****************************************Department Type*********************************************************************/
	String getAllDepartmentType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addDepartmentType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDepartmentType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDepartmentTypeStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/******************************Investigation UOM Master************************************************/
	String addInvestigationUOM(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllInvestigationUOM(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateInvestigationUOMStatus(HashMap<String, Object> uom, HttpServletRequest request,HttpServletResponse response);
	String updateInvestigationUOM(HashMap<String, Object> uom, HttpServletRequest request,HttpServletResponse response);
   
	/******************************Investigation Master************************************************/
	String getAllMainChargeList(HttpServletRequest request, HttpServletResponse response);
	String getAllModalityList(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllSampleList(HttpServletRequest request, HttpServletResponse response);
	String getAllCollectionList(HttpServletRequest request, HttpServletResponse response);
	String getAllUOMList(HttpServletRequest request, HttpServletResponse response);
	String addInvestigation(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllInvestigationDetails(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateInvestigationStatus(HashMap<String, Object> inv, HttpServletRequest request,	HttpServletResponse response);
	String updateInvestigation(HashMap<String, Object> inv, HttpServletRequest request, HttpServletResponse response);
    
	/******************************Sub Investigation Master************************************************/
	
	String getAllSubInvestigationDetails(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);	
	String updateSubInvestigation(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	String deleteSunbInvestigationById(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	String deleteFixedValueById(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);
	
	/******************************DepartmentAndDoctorMapping Master************************************************/
	String addDepartmentAndDoctorMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllDepartmentAndDoctorMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateDepartmentAndDoctorMappingStatus(HashMap<String, Object> dnd, HttpServletRequest request,HttpServletResponse response);
	String updateDepartmentAndDoctorMapping(HashMap<String, Object> dnd, HttpServletRequest request,HttpServletResponse response);

	/******************************Employee Master************************************************/
	String getAllEmployee(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllUnitList(HttpServletRequest request, HttpServletResponse response);
    
	/******************************Fixed Value Master************************************************/
	String updateFixedValue(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllFixeValueById(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String validateFixedValue(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
    
	/******************************Normal Value Master************************************************/
	String updateNormalValue(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllNormalValueById(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	
	String validateServiceNo(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	/****************************************MAS Discharge Status*********************************************************************/
	String getAllDischargeStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addDischargeStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDischargeStatusDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDischargeStatusStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS Bed Status*********************************************************************/
	String getAllBedStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addBedStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateBedStatusDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateBedStatusStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/******************************Bed Master************************************************/
	String addBed(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllBed(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateBedStatus(HashMap<String, Object> bed, HttpServletRequest request,HttpServletResponse response);
	String updateBed(HashMap<String, Object> bed, HttpServletRequest request,HttpServletResponse response);
	
	/****************************************MAS Speciality*********************************************************************/
	String getAllSpeciality(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addSpeciality(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateSpecialityDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateSpecialityStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS AdmissionType*********************************************************************/
	String getAllAdmissionType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addAdmissionType(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateAdmissionTypeDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateAdmissionTypeStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS DisposedTo*********************************************************************/
	String getAllDisposedTo(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addDisposedTo(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDisposedToDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDisposedToStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS Condition*********************************************************************/
	String getAllCondition(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addCondition(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateConditionDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateConditionStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS Diet*********************************************************************/
	String getAllDiet(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addDiet(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDietDetails(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDietStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
 
	/****************************************MAS Disease*********************************************************************/
	String addDisease(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllDisease(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateDiseaseStatus(HashMap<String, Object> inv, HttpServletRequest request, HttpServletResponse response);
	String updateDisease(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);    
	
	/****************************************MAS Document *********************************************************************/
	String addDocument(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllDocument(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateDocumentStatus(HashMap<String, Object> dis, HttpServletRequest request, HttpServletResponse response);
	String updateDocument(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
    
	/****************************************MAS Bank *********************************************************************/
	String addBank(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateBankStatus(HashMap<String, Object> bank, HttpServletRequest request, HttpServletResponse response);
	String getAllBank(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateBankDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
    
	/****************************************Account Type *********************************************************************/
	String addAccountType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateAccountTypeStatus(HashMap<String, Object> actstatus, HttpServletRequest request,
			HttpServletResponse response);
	String getAllAccountType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateAccountType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	/****************************************MAS ICD_Diagnosis*********************************************************************/
	String getAllDiagnosis(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String addDiagnosis(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDiagnosis(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	String updateDiagnosisStatus(JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response);
	
	/******************************MAS MedicalExamSchedule************************************************/
	String addMedicalExamSchedule(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllMedicalExamSchedule(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getMedicalExamSchedule(HashMap<String, Object> medicalExamSchedule, HttpServletRequest request);
	String updateMedicalExamSchedule(HashMap<String, Object> medicalExamSchedule, HttpServletRequest request,HttpServletResponse response);
	String medicalExamScheduleStatus(HashMap<String, Object> medicalExamSchedule, HttpServletRequest request,HttpServletResponse response);
	String getRankCategoryList(HttpServletRequest request, HttpServletResponse response);
	
	/******************************FWC Master************************************************/
	String addFWC(JSONObject json, HttpServletRequest request, HttpServletResponse response);
	String getAllFWC(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);
	//String getItemClass(HashMap<String, Object> itemClass, HttpServletRequest request);
	String updateFWC(HashMap<String, Object> fwc, HttpServletRequest request,HttpServletResponse response);
	String statusFWC(HashMap<String, Object> fwc, HttpServletRequest request,HttpServletResponse response);
	String getMIRoomList(HttpServletRequest request, HttpServletResponse response);
	
	/******************************Disease Type Master************************************************/
	String addDiseaseType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllDiseaseType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateDiseaseTypeStatus(HashMap<String, Object> dis, HttpServletRequest request,
			HttpServletResponse response);
	String updateDiseaseType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	/******************************Disease Mapping Master************************************************/
	String addDiseaseMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllDiseaseMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateDiseaseMappingStatus(HashMap<String, Object> dis, HttpServletRequest request,
			HttpServletResponse response);
	String updateDiseaseMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/******************************MMU Master************************************************/
	
	String addMMU(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateMMUStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String getAllMMU(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateMMU(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getAllCity(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllMMUVendor(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllMMUType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String validateRegNo(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	
	/******************************User Type Master************************************************/
	String addUserType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllUserType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateUserTypeStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateUserType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	/***********************************Mas City MASTER ***********************************/
	
	String addCity(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateCityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateCity(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String getAllDistrict(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	/***********************************Mas Zone MASTER ***********************************/
	String addZone(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllZone(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateZoneStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateZone(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
   
	String getAllSymptoms(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	/***********************************Mas Ward MASTER ***********************************/
	String addWard(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllWard(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateWardStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateWard(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/***********************************Mas District MASTER ***********************************/
	
	String addDistrict(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateDistrictStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateDistrict(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
   
	/***********************************MAS Treatment Instructions MASTER ***********************************/
	
	String addTreatmentInstructions(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllTreatmentInstructions(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateTreatmentInstructionsStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateTreatmentInstructions(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/***********************************MAS Sign Symtoms MASTER ***********************************/
	
	String addSignSymtoms(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllSignSymtoms(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateSignSymtomsStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateSignSymtoms(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/***********************************MAS Labour MASTER ***********************************/	
	String addLabour(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllLabour(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateLabourStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateLabour(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/**********************************MAS Penalty Master*************************************/
	String getAllPenalty(JSONObject jsondata);
	String addPenalty(JSONObject jsondata);
	String updatePenaltyStatus(HashMap<String, Object> statusPayload);
	String updatePenalty(JSONObject jsonObject);

	/**********************************MAS Equipment Checklist Master*************************************/
	String getAllEquipmentChecklist(JSONObject jsondata);
	String addEquipmentChecklist(JSONObject jsondata);
	String updateEquipmentChecklistStatus(HashMap<String, Object> statusPayload);
	String updateEquipmentChecklist(JSONObject jsonObject);

	/**********************************MAS Inspection Checklist Master*************************************/
	String getAllInspectionChecklist(JSONObject jsondata);
	String addInspectionChecklist(JSONObject jsondata);
	String updateInspectionChecklistStatus(HashMap<String, Object> statusPayload);
	String updateInspectionChecklist(JSONObject jsonObject);

	String getMMUHierarchicalList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String getFrequentlyUsedSymptomsList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);
   
	/***********************************Mmu Department Mapping MASTER ***********************************/	
	
	String addDeptMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllDeptMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateDeptMappingStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateDeptMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	
	/***********************************Supplier Type MASTER ***********************************/	
	String getAllSupplierType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String addSupplierType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateSupplierType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateSupplierTypeStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	
	String getAllAuditorName(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	/***************************************
	 * Treatment Advice Master
	 ***********************************************************************/
	String getAllTreatmentAdvice(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String addTreatmentAdvice(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateTreatmentAdvice(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
	String updateTreatmentAdviceStatus(JSONObject Sample, HttpServletRequest request, HttpServletResponse response);

	/**************************************
	 * Manufacturer Master
	 **************************************************/
	String addManufacturer(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String getAllManufacturer(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);
	String updateManufacturerStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);
	String updateManufacturer(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getLegacyCityMasterData(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String saveOrUpdateLgacyData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String addCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllCityMmuMapping(JSONObject jsondata);
	String addCityMmuMapping(JSONObject jsondata);

	String addCityCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllCityCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addDistrictCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllDistrictCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getClusterByDistrict(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getCityByCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateClusterStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
							   HttpServletResponse response);

	String updateDistrictClusterStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
									   HttpServletResponse response);

	String updateCityClusterStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
								   HttpServletResponse response);

	String getAllSociety(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addSociety(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateSocietyStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response);

	String updateSociety(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getCityList(HashMap<String, Object> payload, HttpServletRequest request, HttpServletResponse response);

	String getSocietyList(HashMap<String, Object> payload, HttpServletRequest request, HttpServletResponse response);

	String addSocietyCity(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllCitySociety(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateSocietyCityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response);

	String addFundSchemeMaster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateFundSchemeStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response);

	String getAllFundScheme(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getMMUByCityCluster(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getMmuByCityMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addAuthority(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateAuthority(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String getAuthority(HashMap<String, String> jsondata, HttpServletRequest request);
    
	String getIndendeCityList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateCityMMuStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String updateCityMMUMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getMmuByDistrictId(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	

	String getStoreFinancialYear(HashMap<String, String> payload, HttpServletRequest request);

	String getMasHeadType(HashMap<String, String> payload, HttpServletRequest request);
	String getAllMasHead(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String addHead(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateHead(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateHeadStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String addApprovalAuthority(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllApprovalAuthority(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String getAllOrderNumber(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String updateApprovalAuthority(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateApprovalAuthorityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String checkFinalApproval(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String addApprovalAuthorityMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllApprovalAuthorityMapping(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String updateApprovalAuthorityMapping(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);

	String updateApprovalAuthorityMappingStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String getAllFinancialYear(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String addFinancialYear(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String updateFinancialYear(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateFinancialYearStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String checkFinancialYear(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String addPenalityApprovalAuthority(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String getAllPenalityApprovalAuthority(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response);

	String updatePenalityApprovalAuthority(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);

	String updatePenalityApprovalAuthorityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String getAllMasStoreSupplier(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String addMMUManufac(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String updateUpssManu(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateUpssManuStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String getAllUpssManu(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response);

	String getMasPhase(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response);

	String addUpssPhaseMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getAllUpssPhaseMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String updateUpssPhaseStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String updateUpssPhase(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	String getStoreFutureFinancialYear(HashMap<String, String> payload, HttpServletRequest request);

	String getAllUpssManufactureMapping(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response);

	String addCityMmuPhaseMapping(JSONObject jsonObject);

	String getAllCityMmuPhaseMapping(JSONObject jsonObject);

	String updateCityMMuStatusPhaseMapping(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response);

	String updateCityMMUPhaseMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

}
