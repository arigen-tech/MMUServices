package com.mmu.services.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mmu.services.entity.*;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterDao {

	List<MasDepartment> getDepartmentList();

	MasEmployee checkEmp(Long i);

	List<MasIcd> getIcd();

	Map<String, List<MasState>> getAllStates(JSONObject jsonObject);

	MasState checkMasState(String stateCode);

	/*********************************
	 * MAS COMMAND
	 **************************************************/
	List<MasCommand> validateMasCommand(String commandCode, String commandName);

	String addMasCommand(MasCommand masCommand);

	MasCommand chkCommand(String cmdName);

	/* List<MasCommand> getAllCommand(JSONObject jsonObj); */
	Map<String, List<MasCommand>> getAllCommand(JSONObject jsonObj);

	List<MasCommand> getCommand(String cmdName);

	String updateCommand(Long commandId, String commandCode, String commandName, Long commandTypeId, Long userId);

	String updateCommandStatus(Long commandId, String commandCode, String status, Long userId);

	List<MasCommandType> getCommandTypeList();

	/************************* MAS UNIT ************************************/
	Map<String, List<MasUnit>> getAllUnit(JSONObject jsondata);

	List<MasCommand> getCommandList();

	List<MasUnit> validateUnit(String unitName);

	String addMasUnit(MasUnit masUnit);

	List<MasUnitType> getUnitTypeList();

	String updateUnit(Long uId, String uName, Long commandId, String unitAddress, Long uTypeId,Long userId);

	MasUnit checkUnit(String unitName);

	String updateUnitStatus(Long uId, String uName, String status,Long userId);

	/*******************************
	 * MAS HOSPITAL
	 **********************************************/
	List<MasUnit> getUnitNameList();

	Map<String, Object> getAllHospital(JSONObject jsondata);

	//MasHospital checkMasHospital(String hospitalCode);
	
	MasHospital checkMiRoom (Long unitId,String unitName,String status,Long userId);

	String updateHospitalMasterStatus(Long hospitalId,String status,Long userId);

	String updateHospitalDetails(Long hospitalId, String hospitalName, Long unitId, Long userId,String status);

	List<MasHospital> validateMasHospital(String hospitalCode, String hospitalName);

	String addMasHospital(MasHospital hospital);

	/********************************
	 * MAS RELATION
	 **********************************************************/
	Map<String, List<MasRelation>> getAllRelation(JSONObject jsonObject);

	String updateRelationDetails(Long relationId, String relationCode, String relationName, Long userId);

	MasRelation checkMasRelation(Long relationCode);

	String updateRelationStatus(Long realtionId, Long relationCode, String status,Long userId);

	List<MasRelation> validateRelation(Long relationCode, String relationName);

	String addRelation(MasRelation masRelation);



	/**********************************
	 * MAS APPOINTMENT TYPE
	 ********************************************************************/
	List<MasAppointmentType> validateAppointmentType(String appointmentTypeCode, String appointmentTypeName);
	
	List<MasAppointmentType> validateAppointmentTypeUpdate(String appointmentTypeName);

	String addAppointmentType(MasAppointmentType masAppointmentType);

	Map<String, List<MasAppointmentType>> getAllAppointmentType(JSONObject jsonObject);

	String updateAppointmentTypeDetails(Long appointmentTypeId, String appointmentTypeCode, String appointmentTypeName,Long userId);

	MasAppointmentType checkMasAppointmentType(String appointmentTypeCode);

	String updateAppointmentTypeStatus(Long appointmentTypeId, String appointmentTypeCode, String status, Long userId);

	/**********************************
	 * MAS DEPARTMENT
	 ****************************************************************/
	List<MasDepartmentType> getDepartmentTypeList();

	Map<String, List<MasDepartment>> getAllDepartment(JSONObject jsonObject);

	String addDepartment(MasDepartment masDepartment);

	List<MasDepartment> validateDepartment(String departmentCode, String departmentName,Long departmentTypeId);
	List<MasDepartment> validateDepartmentUpdate(Long departmentTypeId, String departmentName);

	MasDepartment checkDepartment(String departmentCode);

	String updateDepartmentStatus(Long departmentId, String departmentCode, String status, Long userId);

	String updateDepartmentDetails(Long departmentId, String departmentCode, String departmentName,
			Long departmentTypeId, Long userId);

	/********************************
	 * MAS FREQUENCY
	 *********************************************************/
	Map<String, List<MasFrequency>> getAllOpdFrequency(JSONObject jsonObject);

	List<MasFrequency> validateFrequency(String frequencyCode, String frequencyName);
	
	List<MasFrequency> validateFrequencyUpdate(String frequencyName,Double feq);

	String addOpdFrequency(MasFrequency masFrequency);

	String updateFrequencyDetails(Long frequencyId, String frequencyCode, String frequencyName,String frequencyHinName,Double feq,Long userId);

	MasFrequency checkFrequency(String frequencyCode);

	String updateOpdFrequencyStatus(Long frequencyId, String frequencyCode, String status,Long userId);

	/*******************************
	 * MAS EMPANELLED HOSPITAL
	 *************************************/
	Map<String, List<MasEmpanelledHospital>> getAllEmpanelledHospital(JSONObject jsonObject);

	List<MasEmpanelledHospital> validateEmpanelledHospital(String empanelledHospitalName,String empanelledHospitalCode);

	String addEmpanelledHospital(MasEmpanelledHospital masEmpanelledHospital);

	String updateEmpanelledHospital(JSONObject jsonObject);
	List<MasHospital> getHospitalListByRegion();	
	String getMasHospitalById(String hosptId);

	/*******************************
	 * MAS SERVICE TYPE
	 **************************************************/
	Map<String, List<MasServiceType>> getAllServiceType(JSONObject jsonObj);

	String updateServiceType(Long serviceTypeId, String serviceTypeName,Long userId);

	String addServiceType(MasServiceType masServiceType);

	String updateServiceTypeStatus(Long serviceTypeId, String commandCode, String status);

	/*******************************
	 * MAS IDEAL WEIGHT
	 **************************************************/
	Map<String, List<MasIdealWeight>> getAllIdealWeight(JSONObject jsonObject);

	Map<String, List<MasNursingCare>> getAllmNursingData(JSONObject jsonObject);

	List<MasNursingCare> validateMasNursing(String nursingCode, String nursingName, String nursingType);
	List<MasNursingCare> validateMasNursingUpdate(String nursingName, String nursingType);
	
	List<MasIdealWeight> validateIdealWeight(Long genderId, Long ageId, Long heightId);

	String addMasNursing(MasNursingCare nursingObj);
	
	String addIdealWeight (MasIdealWeight idealWeightObj);

	String updateMasNursing(JSONObject jsonObject);
	

	List<MasIdealWeight> getAge(JSONObject jsonObject);
	
	String updateIdealWeight(JSONObject jsonObject);
	
	
	/*************************MAS RANK************************************/
	List<MasRank> validateMasRank(String rankCode, String rankName);
	List<MasRank> validateMasRankUpdate(Long employeeCategoryId, String rankName);
	String addMasRank(MasRank masRank);
	MasRank chkRank(String  rankName);
	Map<String, List<MasRank>> getAllRank(JSONObject jsonObj);
	List<MasRank> getRank(String rankName);	
	String updateRank(Long rankId, String rankCode, String rankName, Long employeeCategoryId, Long userId);
	String updateRankStatus(Long rankId, String rankCode,String status, Long userId);
	List<MasEmployeeCategory> getEmployeeCategoryList();
	
	/************************** TRADE MASTER ****************************/
	List<MasTrade> validateMasTrade(String tradeName);
	List<MasTrade> validateMasTradeUpdate(String tradeName);
	String addMasTrade(MasTrade masTrade);
	MasTrade checkTrade(String  tradeName);
	Map<String, List<MasTrade>> getAllTrade(JSONObject jsonObj);
	List<MasTrade> getTrade(String tradeName);	
	String updateTrade(Long tradeId,String tradeName, Long serviceTypeId,Long userId);
	String updateTradeStatus(Long tradeId,String tradeName,String status,Long userId);
	List<MasServiceType> getServiceTypeList();
	
	/********************************MAS RELIGION*********************************************************/
	Map<String, List<MasReligion>> getAllReligion(JSONObject jsonObject);
	List<MasReligion> validateReligion(String religionCode, String religionName);
	List<MasReligion> validateReligionUpdate(String religionCode, String religionName);
	String addReligion(MasReligion masReligion);
	String updateReligionDetails(Long religionId, String religionCode, String religionName, Long userId);
	MasReligion checkReligion(String religionCode);
	String updateReligionStatus(Long religionId, String religionCode, String status, Long userId);
	
	/********************************Identification Type Master*********************************************************/
	Map<String, List<MasIdentificationType>> getAllIdentification(JSONObject jsonObject);
	List<MasIdentificationType> validateIdentification(String identificationCode, String identificationName);
	List<MasIdentificationType> validateIdentificationUpdate(String identificationName);
	String addIdentification(MasIdentificationType masIdentification);
	String updateIdentification(Long identificationTypeId, String identificationCode, String identificationName,Long userId);
	MasIdentificationType checkIdentification(String identificationCode);
	String updateIdentificationStatus(Long identificationTypeId, String identificationCode, String status,Long userId);
	
	/********************************MAS EMPLOYEE CATEGORY*********************************************************/
	Map<String, List<MasEmployeeCategory>> getAllEmployeeCategory(JSONObject jsonObject);
	List<MasEmployeeCategory> validateEmployeeCategory(String employeeCategoryCode, String employeeCategoryName);
	List<MasEmployeeCategory> validateEmployeeCategoryUpdate(String employeeCategoryCode, String employeeCategoryName);
	String addEmployeeCategory(MasEmployeeCategory masEmployeeCategory);
	String updateEmployeeCategoryDetails(Long employeeCategoryId, String employeeCategoryCode, String employeeCategoryName,Long userId);
	MasEmployeeCategory checkEmployeeCategory(String employeeCategoryCode);
	String updateEmployeeCategoryStatus(Long employeeCategoryId, String employeeCategoryCode, String status,Long userId);
	
	/********************************MAS Administrative Sex*********************************************************/
	Map<String, List<MasAdministrativeSex>> getAllAdministrativeSex(JSONObject jsonObject);
	List<MasAdministrativeSex> validateAdministrativeSex(String AdministrativeSexCode, String AdministrativeSexName);
	List<MasAdministrativeSex> validateAdministrativeSexUpdate(String AdministrativeSexCode, String AdministrativeSexName);
	String addAdministrativeSex(MasAdministrativeSex masAdministrativeSex);
	String updateAdministrativeSexDetails(Long administrativeSexId, String administrativeSexCode, String administrativeSexName,Long userId);
	MasAdministrativeSex checkAdministrativeSex(String administrativeSexCode);
	String updateAdministrativeSexStatus(Long administrativeSexId, String administrativeSexCode, String status,Long userId);
	
	/********************************MAS Medical Category*********************************************************/
	Map<String, List<MasMedicalCategory>> getAllMedicalCategory(JSONObject jsonObject);
	List<MasMedicalCategory> validateMedicalCategory(Long MedicalCategoryCode, String MedicalCategoryName);
	List<MasMedicalCategory> validateMedicalCategoryUpdate(Long MedicalCategoryCode, String MedicalCategoryName);
	String addMedicalCategory(MasMedicalCategory masMedicalCategory);
	String updateMedicalCategoryDetails(Long medicalCategoryId, Long medicalCategoryCode, String medicalCategoryName,Long userId, String fitFlag);
	MasMedicalCategory checkMedicalCategory(Long medicalCategoryCode);
	String updateMedicalCategoryStatus(Long medicalCategoryId, Long medicalCategoryCode, String status,Long userId);
	List<MasMedicalCategory> validateFitFlag();
	
	/********************************MAS Blood Group*********************************************************/
	Map<String, List<MasBloodGroup>> getAllBloodGroup(JSONObject jsonObject);
	List<MasBloodGroup> validateBloodGroup(Long BloodGroupCode, String BloodGroupName);
	List<MasBloodGroup> validateBloodGroupUpdate(Long BloodGroupCode, String BloodGroupName);
	String addBloodGroup(MasBloodGroup masBloodGroup);
	String updateBloodGroupDetails(Long bloodGroupId, Long bloodGroupCode, String bloodGroupName,Long userId);
	MasBloodGroup checkBloodGroup(String bloodGroupCode);
	String updateBloodGroupStatus(Long bloodGroupId, Long bloodGroupCode, String status,Long userId);


	/********************************MAS Sample*********************************************************/
	Map<String, List<MasSample>> getAllSample(JSONObject jsonObject);
	List<MasSample> validateSample(String sampleCode, String sampleDescription);
	List<MasSample> validateSampleUpdate(String sampleDescription);
	String addSample(MasSample masSample);
	String updateSampleDetails(Long sampleId, String sampleCode, String sampleDescription,Long userId);
	MasSample checkSample(String sampleyCode);
	String updateSampleStatus(Long sampleId, String sampleCode, String status,Long userId);
	
	/********************************MAS UOM*********************************************************/
	Map<String, List<MasUOM>> getAllUOM(JSONObject jsonObject);
	List<MasUOM> validateUOM(String UOMCode, String UOMName);
	List<MasUOM> validateUOMUpdate(String UOMCode, String UOMName);
	String addUOM(MasUOM masUOM);
	String updateUOMDetails(Long UOMId, String UOMCode, String UOMName,Long userId);
	MasUOM checkUOM(String UOMCode);
	String updateUOMStatus(Long UOMId, String UOMCode, String status,Long userId);
	
	/********************************MAS Item Unit*********************************************************/
	Map<String, List<MasStoreUnit>> getAllItemUnit(JSONObject jsonObject);
	List<MasStoreUnit> validateItemUnit(String storeUnitName);
	List<MasStoreUnit> validateItemUnitUpdate(String storeUnitName);
	String addItemUnit(MasStoreUnit masStoreUnit);
	String updateItemUnitDetails(Long storeUnitId, String storeUnitName,Long userId);
	MasStoreUnit checkItemUnit(String storeUnitName);
	String updateItemUnitStatus(Long storeUnitId, String storeUnitName ,String status,Long userId);
	
	/********************************Users*********************************************************/
	List<Users> validateUsers(String loginName, String firstName);
	List<Users> validateUsersUpdate(String loginName, Long hospitalId);
	String addUsers(Users users);
	Users checkUsers(String  loginName);
	Map<String, List<Users>> getAllUsers(JSONObject jsonObj);
	List<Users> getUsers(String loginName);	
	String updateUsers(Long userId,String loginName,String firstName, Long hospitalId);
	String updateUsersStatus(Long userId,String loginName,String status);
	List<MasHospital> getHospitalList();
	
	/********************************MainChargecode*********************************************************/
	List<MasMainChargecode> validateMainChargecode(String mainChargecodeCode, String mainChargecodeName);
	List<MasMainChargecode> validateMainChargecodeUpdate(String mainChargecodeName, Long departmentId );
	String addMainChargecode(MasMainChargecode mainChargecode);
	MasMainChargecode checkMainChargecode(String  mainChargecodeCode);
	Map<String, List<MasMainChargecode>> getAllMainChargecode(JSONObject jsonObj);
	List<MasMainChargecode> getMainChargecode(String mainChargecodeCode);	
	String updateMainChargecode(Long mainChargecodeId,String mainChargecodeCode,String mainChargecodeName, Long departmentId,Long userId);
	String updateMainChargecodeStatus(Long mainChargecodeId,String status);
	List<MasDepartment> getDepartmentsList();
	
	/********************************MAS ROLE*********************************************************/
	Map<String, List<MasRole>> getAllRole(JSONObject jsonObject);
	List<MasRole> validateRole(String roleCode, String roleName);
	List<MasRole> validateRoleUpdate(String roleName);
	String addRole(MasRole masRole);
	String updateRoleDetails(Long roleId, String roleCode, String roleName,Long userId);
	MasRole checkRole(String roleCode);
	String updateRoleStatus(Long roleId, String roleCode, String status,Long userId);
	
	/********************************Range***************************************************************/
	List<MasRange> validateRange(Long fromRange, Long toRange, String rangeFlag);
	String addRange(MasRange range);
	Map<String, List<MasRange>> getAllRange(JSONObject jsonObj);
	MasRange checkRange(Long fromRange);
	String updateRange(Long rangeId,Long fromRange, Long toRange,Long userId);
	String updateRangeStatus(Long rangeId,Long fromRange,String status,Long userId); 
	List<MasAdministrativeSex> getGenderList(); 
	List<MasRange> getMasRange();
	
	/********************************MAS StoreGroup*********************************************************/
	Map<String, List<MasStoreGroup>> getAllStoreGroup(JSONObject jsonObject);
	List<MasStoreGroup> validateStoreGroup(String groupCode, String groupName);
	List<MasStoreGroup> validateStoreGroupUpdate(String groupName);
	String addStoreGroup(MasStoreGroup masStoreGroup);
	String updateStoreGroup(Long groupId, String groupCode, String groupName, Long userId);
	MasStoreGroup checkStoreGroup(String groupCode);
	String updateStoreGroupStatus(Long groupId, String groupCode, String status, Long userId);
	
	/********************************MAS ItemType*********************************************************/
	Map<String, List<MasItemType>> getAllItemType(JSONObject jsonObject);
	List<MasItemType> validateItemType(String itemTypeCode, String itemTypeName);
	List<MasItemType> validateItemTypeUpdate(String itemTypeName);
	String addItemType(MasItemType masItemType);
	String updateItemType(Long itemTypeId, String itemTypeCode, String itemTypeName,Long userId);
	MasItemType checkItemType(String itemTypeCode);
	String updateItemTypeStatus(Long itemTypeId, String itemTypeCode, String status,Long userId);
	
	
	/*************************MAS StoreSection************************************/
	List<MasStoreSection> validateMasStoreSection(String sectionCode,String sectionName);
	List<MasStoreSection> validateMasStoreSectionUpdate(String sectionName);
	String addMasStoreSection(MasStoreSection masStoreSection);
	MasStoreSection chkStoreSection(String  sectionName);
	Map<String, List<MasStoreSection>> getAllStoreSection(JSONObject jsonObj);
	List<MasStoreSection> getStoreSection(String sectionName);	
	String updateStoreSection(Long sectionId, String sectionName, Long userId);
	String updateStoreSectionStatus(Long sectionId, String status,Long userId);
	//List<MasItemType> getItemTypeList();
	
	/*************************MAS ItemClass************************************/
	List<MasItemClass> validateMasItemClass(String itemClassCode, String itemClassName);
	List<MasItemClass> validateMasItemClassUpdate(Long sectionId, String itemClassName);
	String addMasItemClass(MasItemClass masItemClass);
	MasItemClass chkItemClass(String  itemClassName);
	Map<String, List<MasItemClass>> getAllItemClass(JSONObject jsonObj);
	List<MasItemClass> getItemClass(String itemClassName);	
	String updateItemClass(Long itemClassId, String itemClassName, Long sectionId,Long userId);
	String updateItemClassStatus(Long itemClassId, String status);
	List<MasStoreSection> getStoreSectionList();
	
	/*************************MAS Section************************************/
	List<MasSection> validateMasSection(String sectionCode, String sectionName);
	List<MasSection> validateMasSectionUpdate(String sectionName);
	String addMasSection(MasSection masSection);
	MasSection chkSection(String  sectionName);
	Map<String, List<MasSection>> getAllSection(JSONObject jsonObj);
	List<MasSection> getSection(String sectionName);	
	String updateSection(Long sectionId, String sectionCode, String sectionName, Long hospitalId,Long userId);
	String updateSectionStatus(Long sectionId, String sectionCode,String status,Long userId);
	
	/*************************MAS Item************************************/
	List<MasStoreItem> validateMasStoreItem(String pvmsNo, String nomenclature);
	List<MasStoreItem> validateMasStoreItemUpdate(String pvmsNo,String nomenclature);
	String addMasStoreItem(MasStoreItem masStoreItem);
	MasStoreItem chkStoreItem(String  pvmsNo, String nomenclature);
	Map<String, List<MasStoreItem>> getAllStoreItem(JSONObject jsonObj);
	List<MasStoreItem> getStoreItem(String pvmsNo);	
	String updateStoreItem(MasStoreItem objMaStoreItem);
	String updateStoreItemStatus(Long itemId, String status,Long userId);
	
	/*************************ME/MB Master************************************/
	String addMEMBMaster(MasMedExam masMedExam);
	List<MasMedExam> validateMEMBMaster(String examName,String examCode);
	List<MasMedExam> validateMEMBMasterUpdate(String examName);
	Map<String, List<MasMedExam>> getAllMEMBMaster(JSONObject jsonObj);
	String updateMEMBMaster(Long membId, String examName,String examCode,Long userId,String onlineOffline);
	String updateMEMBStatus(Long examId, String status);
	
	/*************************ME Investigation Master************************************/
	List<DgMasInvestigation> getInvestigationNameList();
	String saveMEInvestigation(MasInvestignationMapping masInvestignationMapping);
	Map<String, List<MasInvestignationMapping>> getAllInvestigationMapping(JSONObject jsonObj);
	String getMasDesignationByDesignationId(String masDesigtionId);
	String updateMEInvestStatus(Long invId, String status);	
	String updateInvestigationMapping(Long invMapId, String invId, String age, Long userid);	
	List<MasInvestignationMapping> getAllMEMappingList();
	List<MasInvestignationMapping> getAllMEMappingById(Long masInv);
	
	
	/*************************Sub Type Master************************************/
	
	List<MasMainChargecode> getMainTypeList();
	List<MasSubChargecode> validateSubType(String masSubChargecodeCode, String masSubChargecodeName);
	String addSubType(MasSubChargecode masSubChargecode);
	Map<String, List<MasSubChargecode>>  getAllSubTypeDetails(JSONObject jsonObj);
	String updateSubTypeStatus(Long subtypeId, String status);
	List<MasSubChargecode> validateSubTypeUpdate(String subTypeCode, String subTypeName);
	String updateSubTypeDetails(Long subChargecodeId, String subChargecodeCode, String subChargecodeName, Long mainTypeId,Long userId);
	
	/********************************Vendor Type*********************************************************/
	Map<String, List<MasMmuType>> getAllVendorType(JSONObject jsonObject);
	List<MasMmuType> validateVendorType(String supplierTypeCode, String supplierTypeName);
	List<MasMmuType> validateVendorTypeUpdate(String supplierTypeCode, String supplierTypeName);
	String addVendorType(MasMmuType masStoreSupplierType);
	String updateVendorTypeDetails(Long supplierTypeId, String supplierTypeCode, String supplierTypeName, Long userId);
	MasMmuType checkVendorType(String supplierTypeCode);
	String updateVendorTypeStatus(Long supplierTypeId, String supplierTypeCode, String status, Long userId);
	
	/*************************MAS Vendor************************************/
	List<MasStoreSupplierNew> validateMasStoreSupplier(String supplierCode,String supplierName);
	List<MasStoreSupplierNew> validateMasStoreSupplierUpdate(String supplierCode,String pinNo, String tinNo,String licenceNo, String mobileno);
	String addMasStoreSupplier(MasStoreSupplierNew masStoreSupplier);
	MasStoreSupplier chkStoreSupplier(String pinNo, String tinNo,String licenceNo, String mobileno);
	Map<String, List<MasStoreSupplierNew>> getAllVendor(JSONObject jsonObj);
	List<MasDistrict> getDistrictListById(JSONObject jsondata);
	String updateStoreSupplier(JSONObject jsondata);
	String updateStoreSupplierStatus(Long supplierId, String supplierCode, String status,Long userId);
	List<MasState> getStateList();
	Map<String,List<MasDistrict>> getDistrictList(JSONObject jsondata);
	
	/*************************Sample Container Master************************************/
	String addSampleContainer(DgMasCollection masCollection);
	String validateSampleContainer(String collectionCode, String collectionName);
	Map<String, List<DgMasCollection>> getAllSampleContainer(JSONObject jsondata);
	String updateSampleContainerStatus(Long collectionId, String status);
	String updateSampleContainer(Long collectionId, String collectionCode, String collectionName,Long userId); 
	String validateSampleContainerName(String collectionName);
	
	/*************************Investigation UOM Master************************************/
	String addInvestigationUOM(MasUOM dguom);
	List<MasUOM> validateInvestigationUOM(String uomCode, String uomName);
	Map<String, List<MasUOM>> getAllInvestigationUOM(JSONObject jsondata);
	String updateInvestigationUOMStatus(Long uomId, String status);
	String updateInvestigationUOM(Long uomId, String uomCode, String uomName, Long userId);
	List<MasUOM> validateInvestigationUOMName(String uomName);
	
	/*************************Investigation UOM Master************************************/
	List<MasMainChargecode> getAllMainChargeList();
	List<MasSubChargecode> getAllModalityList(JSONObject json);
	List<MasSample> getAllSampleList();
	List<DgMasCollection> getAllCollectionList();
	List<MasUOM> getAllUOMList();
	String addInvestigation(DgMasInvestigation dgmas);
	String validateInvestigationName(String invName);
	Map<String, List<DgMasInvestigation>> getAllInvestigationDetails(JSONObject jsondata);

	

	

	/********************************Department Type*********************************************************/
	Map<String, List<MasDepartmentType>> getAllDepartmentType(JSONObject jsonObject);
	List<MasDepartmentType> validateDepartmentType(String departmentTypeCode, String departmentTypeName);
	List<MasDepartmentType> validateDepartmentTypeUpdate(String departmentTypeCode, String departmentTypeName);
	String addDepartmentType(MasDepartmentType masDepartmentType);
	String updateDepartmentTypeDetails(Long departmentTypeId, String departmentTypeCode, String departmentTypeName, Long userId);
	MasDepartmentType checkDepartmentType(String departmentTypeCode);
	String updateDepartmentTypeStatus(Long departmentTypeId, String departmentTypeCode, String status, Long userId);
	String updateInvestigationStatus(Long invId, String status);
	String updateInvestigation(JSONObject jsondata);
	
	/******************************Sub Investigation Master************************************************/	
	Map<String, List<DgSubMasInvestigation>> getAllSubInvestigationDetails(JSONObject jsondata);	
	String updateSubInvestigation(HashMap<String, Object> jsondata);
	//String validateSubInvestigation(HashMap<String, Object> jsondata);
	String validateLoincCode(String loincCode);
	MasStoreItem getMasStoreItemById(Long masid);
	String deleteSunbInvestigationById(Long subInvId);
	String deleteFixedValueById(Long fixedValueid);
	
	/*************************DoctorMapping Master************************************/
	String addMasDoctorMapping(MasDoctorMapping masDoctorMapping);
	List<MasDoctorMapping> validateMasDoctorMapping(Long doctorId, Long departmentId);
	List<MasDoctorMapping> validateMasDoctorMappingUpdate(Long doctorId, Long departmentId);
	Map<String, List<MasDoctorMapping>> getAllDoctorMapping(JSONObject jsondata);
	String updateMasDoctorMappingStatus(Long doctorMapId, String status,Long userId,Long hospitalId);
	String updateMasDoctorMapping(Long doctorMapId, Long departmentId,Long doctorId,Long userId,Long hospitalId);
	
	/*************************Employee Master************************************/
	Map<String, List<MasEmployee>> getAllEmployee(JSONObject jsondata);
	MasRank getRankByRankCode(String rankCode);
	MasUnit getMasUnitByUnitCode(String unitCode);
	List<MasUnit> getAllUnitList();

	/*************************Fixed Value Master************************************/
	
	String updateFixedValue(HashMap<String, Object> jsondata);
	Map<String, List<DgFixedValue>> getAllFixeValueById(JSONObject jsondata);
	String validateFixedValue(JSONObject jsondata);
	
	/*************************Normal Value Master************************************/
	String updateNormalValue(HashMap<String, Object> jsondata);
	Map<String, List<DgNormalValue>> getAllNormalValueById(JSONObject jsondata);
	String validateServiceNo(JSONObject jsondata);
	
	/********************************MAS Discharge Status*********************************************************/
	Map<String, List<MasDischargeStatus>> getAllDischargeStatus(JSONObject jsondata);
	List<MasDischargeStatus> validateDischargeStatus(String dischargeStatusCode, String dischargeStatusName);
	List<MasDischargeStatus> validateDischargeStatusUpdate(String dischargeStatusCode, String dischargeStatusName);
	String addDischargeStatus(MasDischargeStatus masDischargeStatus);
	String updateDischargeStatusDetails(Long dischargeStatusId, String dischargeStatusCode, String dischargeStatusName,Long userId);
	MasDischargeStatus checkDischargeStatus(String dischargeStatusCode);
	String updateDischargeStatusStatus(Long dischargeStatusId, String dischargeStatusCode, String status,Long userId);
	
	/********************************MAS BedStatus*********************************************************/
	Map<String, List<MasBedStatus>> getAllBedStatus(JSONObject jsondata);
	List<MasBedStatus> validateBedStatus(String bedStatusCode, String bedStatusName);
	List<MasBedStatus> validateBedStatusUpdate(String bedStatusCode, String bedStatusName);
	String addBedStatus(MasBedStatus masBedStatus);
	String updateBedStatusDetails(Long bedStatusId, String bedStatusCode, String bedStatusName,Long userId);
	MasBedStatus checkBedStatus(String bedStatusCode);
	String updateBedStatusStatus(Long bedStatusId, String bedStatusCode, String status,Long userId);
	
	/********************************MAS Bed*********************************************************/
	Map<String, List<MasBed>> getAllBed(JSONObject jsondata);
	List<MasBed> validateBed(String bedNo, Long departmentId, Long hospitalId);
	List<MasBed> validateBedUpdate(String bedNo, Long departmentId, Long hospitalId);
	String addBed(MasBed masBed);
	String updateBed(Long bedId,String bedNo, Long departmentId,Long userId,Long hospitalId);
	MasBed checkBed(String bedNo);
	String updateBedStatus(Long bedId,String status,Long userId,Long hospitalId);
	
	/********************************MAS Speciality*********************************************************/
	Map<String, List<MasSpeciality>> getAllSpeciality(JSONObject jsondata);
	List<MasSpeciality> validateSpeciality(String specialityCode, String specialityName);
	List<MasSpeciality> validateSpecialityUpdate(String specialityCode, String specialityName);
	String addSpeciality(MasSpeciality masSpeciality);
	String updateSpecialityDetails(Long specialityId, String specialityCode, String specialityName,Long userId);
	MasSpeciality checkSpeciality(String specialityCode);
	String updateSpecialityStatus(Long specialityId, String specialityCode, String status,Long userId);
	
	/********************************MAS AdmissionType*********************************************************/
	Map<String, List<MasAdmissionType>> getAllAdmissionType(JSONObject jsondata);
	List<MasAdmissionType> validateAdmissionType(String admissionTypeCode, String admissionTypeName);
	List<MasAdmissionType> validateAdmissionTypeUpdate(String admissionTypeCode, String admissionTypeName);
	String addAdmissionType(MasAdmissionType masAdmissionType);
	String updateAdmissionTypeDetails(Long admissionTypeId, String admissionTypeCode, String admissionTypeName,Long userId);
	MasAdmissionType checkAdmissionType(String admissionTypeCode);
	String updateAdmissionTypeStatus(Long admissionTypeId, String admissionTypeCode, String status,Long userId);
	
	/********************************MAS DisposedTo*********************************************************/
	Map<String, List<MasDisposedTo>> getAllDisposedTo(JSONObject jsondata);
	List<MasDisposedTo> validateDisposedTo(String disposedToCode, String disposedToName);
	List<MasDisposedTo> validateDisposedToUpdate(String disposedToCode, String disposedToName);
	String addDisposedTo(MasDisposedTo masDisposedTo);
	String updateDisposedToDetails(Long disposedToId, String disposedToCode, String disposedToName,Long userId);
	MasDisposedTo checkDisposedTo(String disposedToCode);
	String updateDisposedToStatus(Long disposedToId, String disposedToCode, String status,Long userId);
	
	/********************************MAS Condition*********************************************************/
	Map<String, List<MasPatientCondition>> getAllCondition(JSONObject jsondata);
	List<MasPatientCondition> validateCondition(String conditionName);
	List<MasPatientCondition> validateConditionUpdate(String conditionName);
	String addCondition(MasPatientCondition masPatientCondition);
	String updateConditionDetails(Long conditionId,String conditionName,Long userId);
	MasPatientCondition checkCondition(String conditionName);
	String updateConditionStatus(Long conditionId, String status,Long userId);
	
	/********************************MAS Diet*********************************************************/
	Map<String, List<MasDiet>> getAllDiet(JSONObject jsondata);
	List<MasDiet> validateDiet(String dietCode, String dietName);
	List<MasDiet> validateDietUpdate(String dietCode, String dietame);
	String addDiet(MasDiet masDiet);
	String updateDietDetails(Long dietId, String dietCode, String dietName,Long userId);
	MasDiet checkDiet(String dietCode);
	String updateDietStatus(Long dietId, String dietCode, String status,Long userId);
	Map<String, List<MasStoreItem>> getAllStoreNiv(JSONObject jsonObj);

	/********************************MAS Disease *********************************************************/
	String addDisease(MasDisease masDisease);
	List<MasDisease> validateDisease(String diseaseCode, String diseaseName);
	Map<String, List<MasDisease>> getAllDisease(JSONObject jsondata);
	String updateDiseaseStatus(Long id, String status);
	String updateDisease(MasDisease masDisease);
	
	/********************************MAS Document *********************************************************/
	List<MasDocument> validateDocument(String documentCode, String documentName);
	String addDocument(MasDocument masDocument);
	Map<String, List<MasDocument>> getAllDocument(JSONObject jsondata);
	String updateDocumentStatus(Long id, String status);
	String updateDocument(MasDocument masDocument);
    
	/********************************MAS Bank *********************************************************/
	List<MasBank> validateBankDetails(String bankCode, String bankName);
	String addBank(MasBank masBank);
	String updateBankStatus(Long id, String status);
	Map<String, List<MasBank>> getAllBank(JSONObject jsondata);
	String updateBankDetails(MasBank masBank);
    
	/********************************Account Type *********************************************************/
	List<MasAccountType> validateAccountType(String accountTypeCode, String accountTypeName);
	List<MasAccountType> validateAccountTypeUpdate(String accountTypeName);
	String addAccountType(MasAccountType masAccountType);
	String updateAccountTypeStatus(Long id, String status);
	Map<String, List<MasAccountType>> getAllAccountType(JSONObject jsondata);
	String updateAccountType(MasAccountType masAccountType);

	/********************************MAS ICD_Diagnosis*********************************************************/
	Map<String, List<MasIcd>> getAllDiagnosis(JSONObject jsonObject);
	List<MasIcd> validateDiagnosis(String icdCode, String icdName);
	List<MasIcd> validateDiagnosisUpdate(String icdName);
	String addDiagnosis(MasIcd masIcd);
	String updateDiagnosis(Long icdId, String icdCode, String icdName,Long userId,String communicable, String infectious, String mfDiagnosis);
	MasIcd checkDiagnosis(String icdCode);
	String updateDiagnosisStatus(Long icdId, String icdCode, String status,Long userId);

	/*************************MedicalExamSchedule************************************/
	List<CategoryDue> validateMedicalExamSchedule(Long employeeCategoryId);
	List<CategoryDue> validateMedicalExamScheduleUpdate(Long categoryId);
	String addMedicalExamSchedule(CategoryDue categoryDue);
	Map<String, Object> getAllMedicalExamSchedule(JSONObject jsondata);
	String updateMedicalExamSchedule(Long categoryDueId, Long employeeCategoryId,Long fromMonth,Long toMonth,Long userId);
	String updateMedicalExamScheduleStatus(Long categoryDueId, String status);
	List<MasEmployeeCategory> getRankCategoryList();
	
	/*************************FWC Master************************************/
	List<MasHospital> validateFWC(String hospitalName);
	List<MasHospital> validateFWCUpdate(Long hospitalId1, String hospitalName);
	String addFWC(MasHospital masHospital);
	MasHospital chkFWC(String  hospitalName);
	Map<String, List<MasHospital>> getAllFWC(JSONObject jsonObj);
	String updateFWC(Long hospitalId, String hospitalName, Long hospitalId1,Long userId);
	String updateFWCStatus(Long hospitalId, String status);
	List<MasHospital> getMIRoomList();
	
	/*************************Disease Type Master************************************/
	
	String addDiseaseType(MasDiseaseType masDiseaseType);
	List<MasDiseaseType> validateDiseaseType(String diseaseTypeCode, String diseaseTypeName);
	Map<String, List<MasDiseaseType>> getAllDiseaseType(JSONObject jsondata);
	String updateDiseaseTypeStatus(Long id, String status);
	String updateDiseaseType(MasDiseaseType masDiseaseType);
	
	/*************************Disease Type Master************************************/
	
	String addDiseaseMapping(MasDiseaseMapping masDiseaseMapping);
	boolean validateDiseaseById(Long diseaseId);
	Map<String, List<MasDiseaseMapping>> getAllDiseaseMapping(JSONObject jsondata);
	String updateDiseaseMappingStatus(Long id, String status);
	String updateDiseaseMapping(MasDiseaseMapping masDiseaseMapping);
	
	String getMasIcdById(String diagnosis);

	/*************************MMU Master************************************/
	
	String addMMU(MasMMU massMMU);
	String updateMMUStatus(Long id, String status);
	Map<String, List<MasMMU>> getAllMMU(JSONObject jsondata);
	String updateMMU(MasMMU masmmu);
	Map<String, List<MasCity>> getAllCity(JSONObject jsondata);
	Map<String, List<MasMmuVendor>> getAllMMUVendor(JSONObject jsondata);
	Map<String, List<MasMmuType>> getAllMMUType(JSONObject jsondata);
	String validateRegNo(JSONObject jsondata);

	/*************************Mas User Type Master************************************/
	
	String addUserType(MasUserType masUserType);
	Map<String, List<MasUserType>> getAllUserType(JSONObject jsondata);
	String updateUserTypeStatus(Long id, String status);
	String updateUserType(MasUserType masUserType);
	List<MasUserType> validateMasUserType(String userTypeCode, String userTypeName);
   
	/*************************Mas City Master************************************/
	String addCity(MasCity masCity);
	List<MasCity> validateMasCity(String cityCode, String cityName);
	String updateCityStatus(Long id, String status,String indentCity);
	String updateCity(MasCity masCity);
	Map<String, List<MasDistrict>> getAllDistrict(JSONObject jsondata);

	/*************************Mas Zone Master************************************/
	
	String addZone(MasZone masZone);
	List<MasZone> validateMasZone(String zoneCode, String zoneName);
	Map<String, List<MasZone>> getAllZone(JSONObject jsondata);
	String updateZoneStatus(Long id, String status);
	String updateZone(MasZone masZone);	
	
	List<MasSymptoms> getAllSymptoms(JSONObject jsonObject);
	
	/*************************Mas Ward Master************************************/
	String addWard(MasWard masWard);
	List<MasWard> validateWardCode(String wardCode);
	List<MasWard> validateWardName(String wardName, Long cityId);
	Map<String, List<MasWard>> getAllWard(JSONObject jsondata);
	String updateWardStatus(Long id, String status);
	String updateWard(MasWard masWard);
	
	/*************************Mas District Master************************************/

	String addDistrict(MasDistrict masDistrict);
	List<MasDistrict> validateMasDistrict(String districtCode, String districtName);
	String updateDistrict(MasDistrict masDistrict);
	String updateDistrictStatus(Long id, String status);

	/***********************************MAS Treatment Instructions MASTER ***********************************/
	String addTreatmentInstructions(MasTreatmentInstruction masTreatmentInstruction);
	List<MasTreatmentInstruction> validateMasTreatmentInstruction(String instructionsCode, String instructionsName);
	Map<String, List<MasTreatmentInstruction>> getAllTreatmentInstructions(JSONObject jsondata);
	String updateTreatmentInstructionsStatus(Long id, String status);
	String updateTreatmentInstructions(MasTreatmentInstruction masTreatmentInstruction);

	/***********************************MAS Sign Symtoms MASTER ***********************************/
	
	String addSignSymtoms(MasSymptoms masSymptoms);
	List<MasSymptoms> validateMasSymptoms(String code, String name);
	Map<String, List<MasSymptoms>> getAllSignSymtoms(JSONObject jsondata);
	String updateSignSymtomsStatus(Long id, String status);
	String updateSignSymtoms(MasSymptoms masSymptoms);

	/***********************************MAS Labour MASTER ***********************************/
	String addLabour(MasLabor masLabor);
	List<MasLabor> validateMasLabor(String laborCode, String laborName);
	Map<String, List<MasLabor>> getAllLabour(JSONObject jsondata);
	String updateLabourStatus(Long id, String status);
	String updateLabour(MasLabor masLabor);

	Map<String, Object> getMMUHierarchicalList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response);

	List<MasSymptoms> getFrequentlyUsedSymptomsList(JSONObject jsonObject);
    
	/***********************************MMU Department Mapping MASTER ***********************************/
	
	String addDeptMapping(MMUDepartment mmuDepartment);
	Map<String, List<MMUDepartment>> getAllDeptMapping(JSONObject jsondata);
	String updateDeptMappingStatus(Long id, String status);
	List<MMUDepartment> validateMMUDepartment(Long mmuId, Long deptId);
	String updateDeptMapping(MMUDepartment mmuDepartment);
	

	/***********************************MAS Penalty MASTER ***********************************/
	Map<String, List<MasPenalty>> getAllPenalty(JSONObject jsondata);
	String updatePenaltyStatus(Long id, String status);
	String updatePenalty(MasPenalty masPenalty);

	/***********************************MAS Equipment Checklist MASTER ***********************************/
	Map<String, List<MasEquipmentChecklist>> getAllEquipmentChecklist(JSONObject jsondata);
	String updateEquipmentChecklistStatus(Long id, String status);
	String updateEquipmentChecklist(MasEquipmentChecklist masEquipmentChecklist);

	/***********************************MAS Inspection Checklist MASTER ***********************************/
	Map<String, List<MasInspectionChecklist>> getAllInspectionChecklist(JSONObject jsondata);
	String updateInspectionChecklistStatus(Long id, String status);
	String updateInspectionChecklist(MasInspectionChecklist masInspectionChecklist);

	/***********************************MAS Common Operation ***********************************/
	boolean isRecordAlreadyExists(String keyColumn, String columnValue, Class entityClass);
	String createRecord(Serializable entity);

	Object read(Class entityClass, Serializable id);

	/****************************Supplier Type Master *********************************************************/
	
	Map<String, List<MasStoreSupplierType>> getAllSupplierType(JSONObject jsondata);
	List<MasStoreSupplierType> validateSupplierType(String supplierTypeCode, String supplierTypeName);
	List<MasStoreSupplierType> validateSupplierTypeUpdate(String supplierTypeCode, String supplierTypeName);
	String addSupplierType(MasStoreSupplierType masStoreSupplierType);
	String updateSupplierTypeDetails(Long supplierTypeId, String supplierTypeCode, String supplierTypeName,
			Long userId);
	MasStoreSupplierType checkSupplierType(String supplierTypeCode);
	String updateSupplierTypeStatus(Long supplierTypeId, String supplierTypeCode, String status, Long userId);

	Map<String, List<Users>> getAllAuditorName(JSONObject jsondata);

	/****************************Treatment Advice Master*********************************************************/
	Map<String, List<OpdTemplateMedicalAdvice>> getAllTreatmentAdvice(JSONObject jsondata);
	List<OpdTemplateMedicalAdvice> validateAdviceName(String adviceName);
	String addTreatmentAdvice(OpdTemplateMedicalAdvice madvice);
	String updateTreatmentAdvice(Long Id, String adviceName, Long userId);
	String updateTreatmentAdviceStatus(Long adviceId);

	/**************************************
	 * Manufacturer Master
	 **************************************************/
	String addManufacturer(MasManufacturer masManufacturer);
	Map<String, List<MasManufacturer>> getAllManufacturer(JSONObject jsondata);
	String updateManufacturerStatus(Long id, String status);
	List<MasManufacturer> validateMasManufacturer(String manufacturerName, Long suplyId);
	String updateManufacturer(MasManufacturer masManufacturer);
	String addCluster(MasCluster masCluster);
	Map<String, List<MasCluster>> getAllCluster(JSONObject jsondata);

	String updateMasCluster(Long Id, String clusterName, String status, Long userId);

	Map<String, List<Object[]>> getAllCityMmuMapping(JSONObject jsondata);

	String addCityCluster(ClusterCityMapping clusterCityMapping);

	Map<String, List<ClusterCityMapping>> getAllCityCluster(JSONObject jsonObj);

	String addDistrictCluster(ClusterDistrictMapping clusterDistrictMapping);

	Map<String, List<ClusterDistrictMapping>> getAllDistrictCluster(JSONObject jsonObj);

	Map<String, List<MasCity>> getClusterByDistrict(JSONObject jsonObj);

	Map<String, List<MasCity>> getClusterByCity(JSONObject jsondata);

	String updateClusterStatus(Long id, String status);

	String updateMasDistrictClusterMap(Long Id, Long clusterId, Long districtId, String status, Long userId);

	String updateDistrictClusterStatus(Long id, String status);

	String updateCityClusterStatus(Long id, String status);

	String updateMasCityClusterMap(Long cityClusterId, Long clusterId, Long cityId, String status, Long userIdUpdate);

	/*************************Mas Society Master************************************/
	String addSociety(MasSociety masSociety);

	List<MasSociety> validateMasSociety(String societyCode, String societyName);

	List<LagecyData> getLegaCityMasterData(Integer cityId);

	String saveOrUpdateLgacyData(HashMap<String, Object> jsondata);
	Map<String, List<MasSociety>> getAllSociety(JSONObject jsondata);

	String updateSociety(MasSociety masSociety);

	String updateSocietyStatus(long societyId, String status);

	Map<String, List<MasCity>> getCityList(JSONObject jsondata);

	Map<String, List<MasSociety>> getSocietyList(JSONObject jsondata);

	List<SocietyCityMapping> validateSocietyCitymapping(long cityId, long societyId);

	String addSocietyCity(SocietyCityMapping societyCityMap);

	Map<String, List<SocietyCityMapping>> getAllCitySociety(JSONObject jsonData);

	String updateSocietyCityStatus(long societyCityId, String status);

	String updateSocietyCity(Long societyCityId, Long societyId, Long cityId, String status, Long userId);

	List<FundSchemeMaster> validateFundSchemeMaster(String fundSchemeCode, String fundSchemeName);

	String addFundSchemeMaster(FundSchemeMaster fundSchemeMaster);

	Map<String, List<FundSchemeMaster>> getAllFundScheme(JSONObject jsonData);

	String upateFundSchemeMaster(Long fundSchemeId, String fundSchemeCode, String fundSchemeName, String status, long userId);

	String updateFundSchemeStatus(long fundSchemeId, String status);

	Map<String, List<CityMmuMapping>> getMMUByCityCluster(JSONObject jsondata);


	Map<String, List<CityMmuMapping>> getMmuByCityMapping(JSONObject jsonData);

	List<MasAuthority> getMasAuthorityList();

	Map<String, List<MasCity>> getIndendeCityList(JSONObject jsondata);

	 

	String updateCityMMUStatus(Long id, String status, Long mmuId, Long cityId);

	List<CityMmuMapping> validateCityMmuMapping(String cityCode, String mmuId);

	List<MasAudit> getAuditList();

	Map<String, List<MasMMU>> getMmuByDistrictId(JSONObject jsonData);

	 

	MasMMU getMasMMU(Long mmuId);

	
	List<MasStoreFinancial> getMasStoreFinancial();

	List<MasHeadType> getMasHadType();

	Map<String, List<MasHead>> getAllMasHead(JSONObject jsondata);

	List<MasHead> validateMasHead(String headCode, String headName);

	String addHead(MasHead masHead);

	String updateHead(MasHead masHead);

	String updateHeadStatus(Long id, String status);

	Map<String, List<ApprovingAuthority>> getAllApprovalAuthority(JSONObject jsondata);

	Map<String, Object> getAllOrderNumber(JSONObject jsondata);

	List<ApprovingAuthority> validateApprovingAuthority(String authorityCode, String authorityName);

	String addApprovalAuthority(ApprovingAuthority approvingAuthority);

	String updateApprovingAuthority(ApprovingAuthority approvingAuthority);

	String updateApprovalAuthorityStatus(Long id, String status);

	String checkFinalApproval(JSONObject jsondata);

	List<ApprovingAuthority> validateOrderNumber(String orderNumber);

	String addApprovalAuthorityMapping(ApprovingMapping approvingAuthority);

	List<ApprovingMapping> validateApprovingMapping(Long authorityId, Long userTypeId);

	Map<String, List<ApprovingMapping>> getAllApprovalAuthorityMapping(JSONObject jsondata);

	String updateApprovingAuthorityMapping(ApprovingMapping approvingAuthority);

	String updateApprovalAuthorityMappingStatus(Long id, String status);

	Map<String, List<MasStoreFinancial>> getAllFinancialYear(JSONObject jsondata);

	List<MasStoreFinancial> validateFinancialYear(String financialYear);

	String addFinancialYear(MasStoreFinancial masStoreFinancial);

	String updateFinancialYear(MasStoreFinancial masStoreFinancial);

	String updateFinancialYearStatus(Long id, String status);

	String checkFinancialYear(JSONObject jsondata);

	List<PenaltyAuthorityConfig> validatePenaltyAuthorityConfig(String uppsId, String authorityName);

	String addPenalityApprovalAuthority(PenaltyAuthorityConfig penaltyAuthorityConfig);

	Map<String, List<PenaltyAuthorityConfig>> getAllPenalityApprovalAuthority(JSONObject jsondata);

	String updatePenalityApprovingAuthority(PenaltyAuthorityConfig penaltyAuthorityConfig);

	String updatePenalityApprovalAuthorityStatus(Long id, String status);

	Map<String, List<MasStoreSupplier>> getAllMasStoreSupplier(JSONObject jsondata);

	String addMMUManufac(UpssManufacturerMapping massMMU);

	 

	List<UpssManufacturerMapping> validateUpssManufacturerMapping(Long itemId, Long districtId);

	String updateUppsManu(UpssManufacturerMapping masmmu);

	String updateUpssManuStatus(Long id, String status);

	Map<String, List<UpssManufacturerMapping>> getAllUpssManu(JSONObject jsondata);

	Map<String, List<MasDistrict>> getFilterDistrict(JSONObject jsondata);

	Map<String, List<MasPhase>> getMasPhase(JSONObject jsonObj);

	List<UpssPhaseMapping> validateUpssPhaseMapping(Long phaseId, Long upssId);

	String addUpssPhaseMapping(UpssPhaseMapping massMMU);

	Map<String, List<UpssPhaseMapping>> getAllUpssPhaseMapping(JSONObject jsondata);

	String updateUpssPhaseStatus(Long id, String status);

	String updateUppsPhase(UpssPhaseMapping masmmu);

	List<MasStoreFinancial> getMasStoreFutureFinancial();

	Map<String, List<UpssManufacturerMapping>> getAllUpssManufactureMapping(JSONObject jsondata,Long districtId);

	Long getCityDistrictId(Long cityId);

	Long getMMUDistrictId(Long mmuId);

	List getFinancialYearFilter(String startDate);

	List<CityMmuInvoiceMapping> validateCityMmuInvoiceMapping(String cityId, String mmuId, String phaseId);

	Map<String, List<Object[]>> getAllCityMmuPhaseMapping(JSONObject jsondata);

	String updateCityMMUPhaseStatus(Long id, String status, Long mmuId, Long cityId,Long phaseId);

	Map<String, List<CityMmuInvoiceMapping>> getMmuByCityMMUPhaseMapping(JSONObject jsonData);

}
